package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApiWithInputs;
import com.tiket.api.b2b.PostInterimDetailsApi;
import com.tiket.api.b2b.PostInterimTransactionsApi;
import com.tiket.api.flight.*;
import com.tiket.api.flight.FlightLiveActivityJobApi;
import com.tiket.api.flight.dashboard.*;
import com.tiket.pojo.flight.dashboard.AirportByCityCodeResponse;
import com.tiket.pojo.flight.dashboard.DynamicSupplierMappingByAirlineResponse;
import com.tiket.pojo.flight.dashboard.IntegratorConfigResponse;
import com.tiket.api.payment.SubmitCreditCardApi;
import com.tiket.api.payment.SubmitPaymentApiB2B;
import com.tiket.api.payment.SubmitPaymentPutApiB2B;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.b2b.corporate.EmployeeData;
import com.tiket.pojo.b2b.corporate.PostInterimDetailsApiFlight;
import com.tiket.pojo.b2b.corporate.PostInterimDetailsApiResponse;
import com.tiket.pojo.b2b.corporate.PostInterimTransactionsApiBodyFlight;
import com.tiket.pojo.flight.*;
import com.tiket.pojo.flight.dashboard.FormGeneratorsApiResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;

import java.math.BigInteger;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static com.tiket.core.CoreFunctions.log;
import static org.hamcrest.Matchers.is;

public class FlightApi <T extends WebDriver> extends BasePage<T> {
    public FlightApi(Session<T> session) {
        super(session);
    }

    private static final int MAX_RETRIES = 5;
    private static final int RETRY_DELAY_MS = 1500;

    /**
     * While true, successful streaming responses update {@link #lastOneWayStreamingAirlineCodeToName}.
     * Only {@link #findDynamicFlightSearchDataOneWayStreaming} enables this; {@link #findDate} does not.
     */
    private boolean captureOneWayStreamingAirlineDirectory;
    /** Code to display name from the latest OW streaming payload while capture was enabled; immutable snapshot. */
    private Map<String, String> lastOneWayStreamingAirlineCodeToName = Map.of();
    private static final int SCHEDULE_INGESTION_POST_ENABLE_WAIT_MS = 5000;
    /** The return-leg stream that serves plain return fares, and what {@link #findDateRT} reads. */
    public static final String RETURN_STREAM = "RT_RETURN";
    /** The return-leg stream behind the "Round-trip Deals" filter, whose fares are smart-trip. */
    public static final String RETURN_STREAM_ROUND_TRIP_DEALS = "RT_RETURN_SMART_TRIP";
    /** Issuance is asynchronous, so the order is polled for five minutes before giving up. */
    private static final int ISSUED_POLL_ATTEMPTS = 15;
    private static final long ISSUED_POLL_DELAY_MS = 5000L;
    /** One issuance record exists per booking code, and a virtual-interline order holds several. */
    private static final String ISSUANCE_RECORD_PAGE_SIZE = "10";
    /** What issuance calls a ticketed record; it says ISSUED_IN_PROGRESS while it is still working. */
    private static final String ISSUANCE_STATUS_SUCCESS = "SUCCESS";
    private static final String ISSUANCE_STATUS_ISSUED_FAILED = "ISSUED_FAILED";
    private static final boolean SKIP_WEEKENDS = true;
    private static final boolean SKIP_HOLIDAYS = false; // you can plug a holiday list later

    @SneakyThrows
    public String getBookingCode(String orderID, String email) {
        FlightGetBookingCodeApi.Inputs inputs = new FlightGetBookingCodeApi.Inputs(orderID, email, "1");
        FlightGetBookingCodeApi flightGetBookingCodeApi = new FlightGetBookingCodeApi(inputs);
        for (int tries = 0; tries < 5; tries++) {
            ApiResponse apiResponse = flightGetBookingCodeApi.hitApi(apiTools);
            Gson gson = new Gson();
            FlightBookingCodeDetail response = gson.fromJson(apiResponse.getBody(), FlightBookingCodeDetail.class);
            if (response != null && response.data != null && response.data.content != null
                    && !response.data.content.isEmpty()) {
                String bookingCode = response.data.content.get(0).bookingCode;
                logger.log("Booking code -> " + bookingCode);
                return bookingCode;
            }
            Thread.sleep(2000);
        }
        return null;
    }

    /**
     * Number of issuance records the order holds, one per booking code — 1 for a round trip on a
     * single booking code, 2 for a virtual-interline pair. Answers 0 when no record ever appears,
     * so 0 means unknown rather than a single code.
     */
    @SneakyThrows
    public int getBookingCodeCount(String orderID, String email) {
        FlightGetBookingCodeApi.Inputs inputs = new FlightGetBookingCodeApi.Inputs(orderID, email, "1");
        FlightGetBookingCodeApi flightGetBookingCodeApi = new FlightGetBookingCodeApi(inputs);
        for (int tries = 0; tries < 5; tries++) {
            ApiResponse apiResponse = flightGetBookingCodeApi.hitApi(apiTools);
            Gson gson = new Gson();
            FlightBookingCodeDetail response = gson.fromJson(apiResponse.getBody(), FlightBookingCodeDetail.class);
            if (response != null && response.data != null && response.data.content != null
                    && !response.data.content.isEmpty()) {
                logger.log("Booking code count -> " + response.data.totalElements);
                return response.data.totalElements;
            }
            Thread.sleep(2000);
        }
        return 0;
    }

    /**
     * Waits until every issuance record of the order reports SUCCESS, which is how an order issued
     * through a mock-success integrator is confirmed. Answers {@code false} when it never gets there.
     */
    @SneakyThrows
    public boolean waitForOrderIssued(String orderId, String email) {
        FlightGetBookingCodeApi api = new FlightGetBookingCodeApi(
                new FlightGetBookingCodeApi.Inputs(orderId, email, ISSUANCE_RECORD_PAGE_SIZE));
        Gson gson = new Gson();
        String lastSeen = "no issuance record";

        for (int tries = 0; tries < ISSUED_POLL_ATTEMPTS; tries++) {
            ApiResponse apiResponse = api.hitApi(apiTools);
            FlightBookingCodeDetail response =
                    gson.fromJson(apiResponse.getBody(), FlightBookingCodeDetail.class);

            if (response != null && response.data != null && response.data.content != null
                    && !response.data.content.isEmpty()) {
                List<String> statuses = new ArrayList<>();
                for (FlightBookingCodeDetail.Content record : response.data.content) {
                    statuses.add(record.status == null ? "null" : record.status);
                }
                lastSeen = String.join(", ", statuses);
                logger.log("Polling issuance status of order " + orderId + " -> " + lastSeen);

                boolean allIssued = true;
                for (String status : statuses) {
                    allIssued &= ISSUANCE_STATUS_SUCCESS.equalsIgnoreCase(status);
                    if (ISSUANCE_STATUS_ISSUED_FAILED.equalsIgnoreCase(status)) {
                        // Issuance does not leave this state by itself.
                        logger.log("Order " + orderId + " reports " + status + ", which issuance does "
                                + "not recover from. Not waiting any longer.");
                        return false;
                    }
                }
                // Only the first page was read, so an order with more records than were returned is
                // not yet known to be fully issued.
                boolean everyRecordRead = response.data.content.size() >= response.data.totalElements;
                if (allIssued && everyRecordRead) {
                    return true;
                }
                if (allIssued) {
                    logger.log("Order " + orderId + " reports " + response.data.content.size() + " of "
                            + response.data.totalElements + " issuance records as issued, so the rest "
                            + "are still unread.");
                }
            }
            Thread.sleep(ISSUED_POLL_DELAY_MS);
        }
        logger.log("Order " + orderId + " never issued. Last issuance status seen: " + lastSeen);
        return false;
    }

    /**
     * The suppliers the order's issuance records name, empty until a record exists. Which supplier
     * serves a booking is decided by priority at booking time, not by the test.
     */
    @SneakyThrows
    public Set<String> getIssuanceSupplierCodes(String orderID, String email) {
        FlightGetBookingCodeApi api = new FlightGetBookingCodeApi(
                new FlightGetBookingCodeApi.Inputs(orderID, email, ISSUANCE_RECORD_PAGE_SIZE));
        ApiResponse apiResponse = api.hitApi(apiTools);
        FlightBookingCodeDetail response =
                new Gson().fromJson(apiResponse.getBody(), FlightBookingCodeDetail.class);

        Set<String> supplierCodes = new LinkedHashSet<>();
        if (response != null && response.data != null && response.data.content != null) {
            for (FlightBookingCodeDetail.Content record : response.data.content) {
                if (record.supplierCode != null && !record.supplierCode.isBlank()) {
                    supplierCodes.add(record.supplierCode);
                }
            }
        }
        logger.log("Order " + orderID + " was served by supplier(s) " + supplierCodes);
        return supplierCodes;
    }

    public Boolean toggleScheduleIngestion(boolean enabled) throws InterruptedException {
        FlightScheduleIngestionToggleApi.Inputs inputs =
                new FlightScheduleIngestionToggleApi.Inputs(enabled);
        FlightScheduleIngestionToggleApi api =
                new FlightScheduleIngestionToggleApi(inputs);

        Gson gson = new Gson();

        for (int tries = 0; tries < 5; tries++) {
            ApiResponse apiResponse = api.hitApi(apiTools);

            FlightToggleResponse response = gson.fromJson(apiResponse.getBody(), FlightToggleResponse.class);

            if (apiResponse.getStatusCode() == 200 && response != null && response.data != null && response.data.status.equalsIgnoreCase("enable")) {
                logger.log("Response ->"+ response);
                logger.log("Config enabled -> " + response.code);
                logger.log("Config datatype -> " + (response.code != null ? response.code.getClass().getSimpleName() : "null"));
                Assertion.assertThat("Response code is SUCCESS", response.code, is("SUCCESS"));
                if (enabled) {
                    logger.log("Wait for changes to reflect on the UI side.");
                    Thread.sleep(SCHEDULE_INGESTION_POST_ENABLE_WAIT_MS);
                    logger.log("Schedule ingestion post-enable wait complete.");
                }
                return true;
            }

        }
        logger.skip("Failed to enable toggle schedule ingestion after multiple attempts.");
        return false;
    }



    @SneakyThrows
    public Boolean flightEticketIssuedRequest(String orderId, String bookingCode, String email,String fullName,int retry){
        try {
            String[] passengerNames = {fullName};
            FlightEticketIssueApi.Inputs inputs = new FlightEticketIssueApi.Inputs(orderId, bookingCode, email, passengerNames, "mrs", 0);
            FlightEticketIssueApi flightEticketIssueApi = new FlightEticketIssueApi(inputs);
            JSONObject response = new JSONObject();

            for(int i=1;i<=retry;i++){
                ApiResponse apiResponse = flightEticketIssueApi.hitApi(apiTools);
                response = new JSONObject(apiResponse.getBody());
                logger.log("Eticket issue api response retry " + i + " -> " +response);
                if(!response.getString("code").contains("DATA_NOT_EXIST")){
                    return true;
                }
                Thread.sleep(12000);
            }
            if(response.getString("code").contains("DATA_NOT_EXIST")){
                return false;
            }
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("ETicket Not issued. Skipping the subsequent steps.");
        }

        return true;
    }

    public Boolean flightEticketIssuedRequestMultipleGuest(String orderId, String bookingCode, String email, List<String> passengerNames, int retry){
        try {
            FlightEticketIssueApi.Inputs inputs = new FlightEticketIssueApi.Inputs(orderId, bookingCode, email, passengerNames.toArray(String[]::new), "mrs", 0);
            FlightEticketIssueApi flightEticketIssueApi = new FlightEticketIssueApi(inputs);
            JSONObject response = new JSONObject();

            for(int i=1;i<=retry;i++){
                ApiResponse apiResponse = flightEticketIssueApi.hitApi(apiTools);
                response = new JSONObject(apiResponse.getBody());
                logger.log("Eticket issue api response retry " + i + " -> " +response);
                if(!response.getString("code").contains("DATA_NOT_EXIST")){
                    return true;
                }
                Thread.sleep(12000);
            }
            if(response.getString("code").contains("DATA_NOT_EXIST")){
                return false;
            }
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("ETicket Not issued. Skipping the subsequent steps.");
        }

        return true;
    }

    @SneakyThrows
    public String createOrderFlightB2CDomestic(
            String username,
            String password,
            String originStation,
            String destinationStation,
            String departureDate,
            String searchType,
            int adult,
            int child,
            int infant,
            String airline,
            String[] passengerNames,
            String phoneNumber
    ) {
        Unm<T> login = new Unm<>(session);
        String accessToken = login.getAccessToken(username, password, "B2C");
        logger.log("accessToken -> " + accessToken);
        String deviceId = CoreFunctions.generateDeviceId();

        //flight search api
        FlightSearchApi.Inputs flightSearchApiInputs = new FlightSearchApi.Inputs(
                originStation.toUpperCase(), destinationStation.toUpperCase(),selectOriginAndDestinationType(originStation) , selectOriginAndDestinationType(destinationStation), departureDate, adult, child, infant,
                searchType, accessToken,"IDR", deviceId
        );

        FlightSearchResponse.Flight departureFlight = flightSearchByName(flightSearchApiInputs,airline,10);
        if(departureFlight==null){
            departureFlight =  flightSearchByNameUsingStream(flightSearchApiInputs,airline,accessToken,"IDR",deviceId);
            if (departureFlight == null) {
                throw new NotFoundException("Flight not found");
            }
        }

        logger.log("Flight Search API response->" + departureFlight);
        String flightId = departureFlight.flightId;
        String supplierID = departureFlight.fareDetail.fares.get(0).supplierId;
        logger.log("Flight ID----->"+flightId);
        logger.log("Supplier ID----->"+supplierID);

        //flight cart post api to get cart id
        FlightCartApi.Inputs flightCartApiInputs = new FlightCartApi.Inputs(flightId, supplierID, accessToken);
        String cartId = flightCartApi(flightCartApiInputs, null);
        log("Cart ID---->"+cartId);

        //flight booking api
        FlightBookingApiDomestic.Inputs flightBookingApiInputs = new FlightBookingApiDomestic.Inputs(cartId, username, accessToken, passengerNames, phoneNumber, adult);
        FlightBookingDetailDomestic flightBookingDetailDomestic = flightBookingApiDomestic(flightBookingApiInputs);
        log("Flight Booking API response->" + flightBookingDetailDomestic);
        long orderId = flightBookingDetailDomestic.data.successData.orderId;
        String orderHash = flightBookingDetailDomestic.data.successData.orderHash;

        logger.log("orderId->" + orderId);
        logger.log("orderHash->" + orderHash);

        PaymentApi<?> paymentApi = new PaymentApi<>(session);
        String referenceId = paymentApi.submitSakukuPayment(accessToken,orderId,orderHash);
        logger.log("referenceId-> " + referenceId);

        String paymentStatus = paymentApi.paidCheckerStatus(120,""+orderId);
        if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");

        return String.valueOf(orderId);
    }

    /**
     * Books a B2C international one-way flight, mirroring {@link #createOrderFlightB2CDomestic} but
     * sending the international passenger shape (passport, not identity number) via
     * {@link FlightBookingApiInternational}.
     */
    @SneakyThrows
    public String createOrderFlightB2CInternational(
            String username,
            String password,
            String originStation,
            String destinationStation,
            String departureDate,
            String searchType,
            int adult,
            int child,
            int infant,
            String airline,
            String[] passengerNames,
            String phoneNumber
    ) {
        Unm<T> login = new Unm<>(session);
        String accessToken = login.getAccessToken(username, password, "B2C");
        logger.log("accessToken -> " + accessToken);
        String deviceId = CoreFunctions.generateDeviceId();

        FlightSearchApi.Inputs flightSearchApiInputs = new FlightSearchApi.Inputs(
                originStation.toUpperCase(), destinationStation.toUpperCase(), selectOriginAndDestinationType(originStation), selectOriginAndDestinationType(destinationStation), departureDate, adult, child, infant,
                searchType, accessToken, "IDR", deviceId
        );

        FlightSearchResponse.Flight departureFlight = flightSearchByName(flightSearchApiInputs, airline, 10);
        if (departureFlight == null) {
            departureFlight = flightSearchByNameUsingStream(flightSearchApiInputs, airline, accessToken, "IDR", deviceId);
            if (departureFlight == null) {
                throw new NotFoundException("Flight not found");
            }
        }

        logger.log("Flight Search API response->" + departureFlight);
        String flightId = departureFlight.flightId;
        String supplierID = departureFlight.fareDetail.fares.get(0).supplierId;
        logger.log("Flight ID----->" + flightId);
        logger.log("Supplier ID----->" + supplierID);

        FlightCartApi.Inputs flightCartApiInputs = new FlightCartApi.Inputs(flightId, supplierID, accessToken);
        String cartId = flightCartApi(flightCartApiInputs, null);
        log("Cart ID---->" + cartId);

        FlightBookingDetailInternational booking = flightBookingApiInternational(
                new FlightBookingApiInternational.Inputs(cartId, username, accessToken, passengerNames, phoneNumber, adult));
        log("Flight Booking API response->" + booking);
        long orderId = booking.data.successData.orderId;
        String orderHash = booking.data.successData.orderHash;

        logger.log("orderId->" + orderId);
        logger.log("orderHash->" + orderHash);

        PaymentApi<?> paymentApi = new PaymentApi<>(session);
        String referenceId = paymentApi.submitSakukuPayment(accessToken, orderId, orderHash);
        logger.log("referenceId-> " + referenceId);

        String paymentStatus = paymentApi.paidCheckerStatus(120, "" + orderId);
        if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");

        return String.valueOf(orderId);
    }

    /**
     * Books a B2C round trip flown by one airline both ways, picking the return leg out of the
     * Round-trip Deals stream. Kept for callers written against that behaviour; a case that does not
     * need a Round-trip Deals fare should name {@link #RETURN_STREAM} instead.
     */
    public String createOrderFlightB2CRoundTrip(
            String username,
            String password,
            String originStation,
            String destinationStation,
            String departureDate,
            String returnDate,
            String searchType,
            int adult,
            int child,
            int infant,
            String airline,
            String[] passengerNames,
            String phoneNumber
    ) {
        return createOrderFlightB2CRoundTrip(username, password, originStation, destinationStation,
                departureDate, returnDate, searchType, adult, child, infant, airline, airline,
                RETURN_STREAM_ROUND_TRIP_DEALS, passengerNames, phoneNumber);
    }

    /**
     * Books a B2C round trip, domestic or international alike.
     * <p>
     * Naming a different airline per leg books the two legs with whichever suppliers serve them,
     * which is how an order ends up with a booking code per leg rather than one for the pair.
     * </p>
     *
     * @param returnStreamType which return-leg stream the return flight is picked from —
     *                         {@link #RETURN_STREAM} for a plain return fare, or
     *                         {@link #RETURN_STREAM_ROUND_TRIP_DEALS} when the case needs a
     *                         Round-trip Deals fare. {@link #findDateRT} resolves its return leg
     *                         from {@link #RETURN_STREAM}, so a case that resolved a schedule there
     *                         and books here has to ask for the same stream to find it again
     */
    @SneakyThrows
    public String createOrderFlightB2CRoundTrip(
            String username,
            String password,
            String originStation,
            String destinationStation,
            String departureDate,
            String returnDate,
            String searchType,
            int adult,
            int child,
            int infant,
            String airline,
            String airlineReturn,
            String returnStreamType,
            String[] passengerNames,
            String phoneNumber
    ) {
        Unm<T> login = new Unm<>(session);
        String accessToken = login.getAccessToken(username, password, "B2C");
        String deviceId = CoreFunctions.generateDeviceId();

        FlightRoundTripSearchApi.Inputs flightRoundTripSearchApiInputs = new FlightRoundTripSearchApi.Inputs(
                originStation, destinationStation, "AIRPORT", "AIRPORT",
                departureDate, returnDate,
                adult, child, infant, searchType, accessToken, "IDR", deviceId
        );

        FlightStreamResponse.DepartureFlight departureFlight;
        FlightStreamResponse.ReturnFlight returnFlight;
        try {
            ArrayList<String> requestItems = flightSearchGetRequestItemsRoundTripDepart(flightRoundTripSearchApiInputs);
            if (requestItems == null || requestItems.isEmpty()) {
                logger.log("No request items found for the flight search API inputs.");
                return null;
            }

            departureFlight = flightSearchStreamByNameDynamicData(
                    requestItems.stream().toList(), "RT_DEPARTURE", returnDate, accessToken, airline, "", child, "IDR", deviceId
            );

            if (departureFlight == null) throw new NotFoundException("Can't found airline for depart");

            logger.log("->>> searching for return flight <<<-");
            FlightSearchNextApi.Inputs flightSearchNextApiInputs = new FlightSearchNextApi.Inputs(
                    originStation, destinationStation, "AIRPORT", "AIRPORT",
                    departureDate, returnDate,
                    departureFlight.flightId, departureFlight.fareId,
                    departureFlight.fareDetail.fares.get(0).supplierId,
                    accessToken,
                    departureFlight, "IDR", deviceId
            );

            ArrayList<String> requestItemsReturn = flightSearchGetRequestItemsRoundTripReturn(flightSearchNextApiInputs);
            if (requestItemsReturn == null || requestItemsReturn.isEmpty()) {
                logger.log("No request items found for the return leg of flight search API inputs.");
                return null;
            }

            returnFlight = flightSearchStreamByNameDynamicDataReturn(
                    requestItemsReturn,
                    isBlankString(returnStreamType) ? RETURN_STREAM : returnStreamType,
                    returnDate, accessToken, airlineReturn, "", child, "IDR", deviceId
            );

            if (returnFlight == null) throw new NotFoundException("Can't found airline for return");

        } catch (Exception e) {
            logger.log("Exception in validateDateRoundTrip: " + e.getLocalizedMessage());
            return null;
        }

        FlightCartApi.InputsRoundTrip flightCartApiInputs = new FlightCartApi.InputsRoundTrip(
                departureFlight.flightId,
                departureFlight.fareDetail.fares.get(0).supplierId,
                returnFlight.flightId,
                returnFlight.fareDetail.fares.get(0).supplierId,
                accessToken);
        String cartId = flightCartApi(null, flightCartApiInputs);
        log("Cart ID---->"+cartId);

        // A domestic passenger is stated as one full name and an identity number, an international
        // one as a first and last name and a passport. Booking answers RUNTIME_ERROR when it is sent
        // the shape the route does not use, so the route decides which is sent.
        long orderId;
        String orderHash;
        if (isDomesticRoute(accessToken, originStation, destinationStation)) {
            FlightBookingDetailDomestic booking = flightBookingApiDomestic(
                    new FlightBookingApiDomestic.Inputs(cartId, username, accessToken, passengerNames,
                            phoneNumber, adult));
            log("Flight Booking API response->" + booking);
            orderId = booking.data.successData.orderId;
            orderHash = booking.data.successData.orderHash;
        } else {
            FlightBookingDetailInternational booking = flightBookingApiInternational(
                    new FlightBookingApiInternational.Inputs(cartId, username, accessToken,
                            passengerNames, phoneNumber, adult));
            log("Flight Booking API response->" + booking);
            orderId = booking.data.successData.orderId;
            orderHash = booking.data.successData.orderHash;
        }

        logger.log("orderId->" + orderId);
        logger.log("orderHash->" + orderHash);
        return String.valueOf(orderId);
    }

    @SneakyThrows
    public String createOrderFlightB2BDomestic(
            String username,
            String password,
            String originStation,
            String destinationStation,
            String departureDate,
            String searchType,
            int adult,
            int child,
            int infant,
            String airline,
            EmployeeData employeeData,
            String firstName,
            String lastName
    ) {
        try {
            String[] passengerNames = {firstName + " " + lastName};
            Unm<T> unm = new Unm<>(session);
            LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(username, password);
            String loginAccessToken = loginDataUnmResponse.accessToken;
            logger.log("accessToken for dashboard request->" + loginAccessToken);
            String deviceId = CoreFunctions.generateDeviceId();
            logger.log("deviceId for request->" + deviceId);
            //flight search api
            FlightSearchApi.Inputs flightSearchApiInputs = new FlightSearchApi.Inputs(
                    originStation.toUpperCase(), destinationStation.toUpperCase(),selectOriginAndDestinationType(originStation) , selectOriginAndDestinationType(destinationStation), departureDate, adult, child, infant,
                    searchType,loginAccessToken,"IDR",deviceId
            );
            FlightSearchResponse.Flight departureFlight = flightSearchByName(flightSearchApiInputs,airline,10);
            if(departureFlight==null){
                departureFlight =  flightSearchByNameUsingStream(flightSearchApiInputs,airline,loginAccessToken,"IDR",deviceId,false);
            }
            logger.log("Flight Search API response->" + (departureFlight != null));
            String flightId = Objects.requireNonNull(departureFlight).flightId;
            logger.log("Flight ID----->"+flightId);
            String supplierID = departureFlight.fareDetail.fares.get(0).supplierId;
            logger.log("Supplier ID----->"+supplierID);

            //flight cart post api to get cart id

            FlightCartApi.Inputs flightCartApiInputs = new FlightCartApi.Inputs(flightId, supplierID, loginAccessToken);
            String cartId = flightCartApi(flightCartApiInputs, null);
            logger.log("Cart ID---->"+cartId);

            FlightAutocompleteResponse.Airport originData = getAirportData(loginAccessToken,originStation);
            FlightAutocompleteResponse.Airport destinationData = getAirportData(loginAccessToken,destinationStation);
            PostInterimDetailsApiFlight postInterimDetailsApiFlight = getBodyPostInterimDetailsApiFlight(cartId,originData,destinationData,"false",departureDate,"");
            PostInterimDetailsApi.Inputs postInterimDetailsApi = new PostInterimDetailsApi.Inputs(loginAccessToken,null,postInterimDetailsApiFlight);
            PostInterimDetailsApiResponse postInterimDetailsApiResponse = postInterimDetail(postInterimDetailsApi);

            PostInterimTransactionsApiBodyFlight postInterimTransactionsApiBodyFlight = getBodyPostInterimTransactionsApiBodyFlight(postInterimDetailsApiFlight);
            PostInterimTransactionsApi.Inputs postInterimTransactionsApiInputs = new PostInterimTransactionsApi.Inputs(loginAccessToken,null,postInterimTransactionsApiBodyFlight);
            String corporateInterimID = postInterimTransactions(postInterimTransactionsApiInputs);
            logger.log("corporateInterimID-> " + corporateInterimID);

            String phoneNumber = loginDataUnmResponse.accountPhoneNumber;

            //flight booking api
            FlightBookingAPiDomesticCorporate.Inputs flightBookingApiInputs = new FlightBookingAPiDomesticCorporate.Inputs(cartId, username, loginAccessToken, passengerNames, phoneNumber,corporateInterimID);
            FlightBookingDetailDomestic flightBookingDetailDomestic = flightBookingApiDomesticCorporate(flightBookingApiInputs);
            logger.log("Flight Booking API response->" + flightBookingDetailDomestic);
            long orderId = flightBookingDetailDomestic.data.successData.orderId;
            String orderHash = flightBookingDetailDomestic.data.successData.orderHash;
            logger.log("orderId: " + orderId);
            logger.log("orderHash: " + orderHash);
            String fullName = passengerNames[0];

            //b2b payment api
            SubmitPaymentApiB2B.Inputs submitPaymentApiInputs = new SubmitPaymentApiB2B.Inputs(loginAccessToken, orderHash, orderId, employeeData.data.employeeID,employeeData.data.corporateID);
            String paymentResponse = submitPaymentB2B(submitPaymentApiInputs);
            logger.log("paymentStatus"+ paymentResponse);

            SubmitPaymentPutApiB2B.Inputs submitPaymentPutApiInputs = new SubmitPaymentPutApiB2B.Inputs(loginAccessToken,orderHash, orderId, employeeData.data.employeeID,employeeData.data.corporateID, "testing only");
            String paymentResponsePut = submitPaymentB2BPUT(submitPaymentPutApiInputs);
            logger.log("paymentStatus"+ paymentResponsePut);

            if(paymentResponsePut.contains("CORPORATE_BILLING_ERROR") && paymentResponsePut.contains("RUNTIME_ERROR")){
                Assertion.skip("Flight Booking via API failed. Skipping the subsequent steps.");
            }
            return String.valueOf(orderId);
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Flight Booking via API failed. Skipping the subsequent steps.");
        }
        return null;
    }

    private String removeBearerAndExtraSpace(String accessToken) {
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            return accessToken.split("Bearer ")[1].trim();
        }
        return accessToken;
    }

    public String selectOriginAndDestinationType(String loc){
        return switch (loc.length()) {
            case 3 -> "AIRPORT";
            case 4 -> "CITY";
            default -> throw new NotImplementedException("Unimplemented location type!");
        };
    }

    private FlightSearchResponse.Flight flightSearchByName(FlightSearchApi.Inputs inputs,String airline,int retry) throws ExecutionException, InterruptedException {
        FlightSearchApi flightSearchApi = new FlightSearchApi(inputs);
        ApiResponse flightSearchResponse;

        Gson gson = new Gson();
        FlightSearchResponse flightSearchResponsePojo;

        for (int tries = 0; tries < retry; tries++) {
            try {
                flightSearchResponse = flightSearchApi.hitApi(apiTools);
                if(flightSearchResponse.getStatusCode() == 200){
                    flightSearchResponsePojo = gson.fromJson(flightSearchResponse.getBody(), FlightSearchResponse.class);
                    if(flightSearchResponsePojo.code.equals("SUCCESS") && !flightSearchResponsePojo.data.searchList.departureFlights.isEmpty()) {
                        logger.log("search list->" + flightSearchResponsePojo.data.searchList.departureFlights.size());
                        logger.log("searchList->" + flightSearchResponsePojo.data.toString());
                        HashMap<String, String> airlineMap = getAirlineCodeToNameMap(flightSearchResponsePojo.data.airlines);
                        for (FlightSearchResponse.Flight depFlight : flightSearchResponsePojo.data.searchList.departureFlights) {
                            String airlineCode = depFlight.marketingAirlineCode;
                            if (matchAirline(airlineMap.get(airlineCode), airline))
                                return depFlight;
                        }
                    }
                }
            }catch (Exception e){
                logger.log("got exception: " + e.getLocalizedMessage());
            }
            Thread.sleep(2000);
        }
        return null;
    }

    private HashMap<String,String> getAirlineCodeToNameMap(HashMap<String,FlightSearchResponse.AirlineDetail> airlineDetail){
        HashMap<String,String> mp = new HashMap<>();
        for (String key : airlineDetail.keySet()){
            String displayName = airlineDetail.get(key).displayName;
            mp.put(key,displayName);
        }
        return mp;
    }

    private HashMap<String,String> getAirlineCodeToNameMapStream(Map<String,FlightStreamResponse.AirlineDetail> airlineDetail){
        HashMap<String,String> mp = new HashMap<>();
        for (String key : airlineDetail.keySet()){
            String displayName = airlineDetail.get(key).displayName;
            mp.put(key,displayName);
        }
        return mp;
    }

    /**
     * When {@link #captureOneWayStreamingAirlineDirectory} is on, stores {@code data.airlines} as code → display name
     * (falls back to name, shortName, or map key) for tests that use {@link #findDynamicFlightSearchDataOneWayStreaming}.
     */
    private void recordLastOneWayStreamingAirlineDirectoryIfEnabled(FlightStreamResponse flightStreamResponse) {
        if (!captureOneWayStreamingAirlineDirectory) {
            return;
        }
        if (flightStreamResponse == null
                || flightStreamResponse.data == null
                || flightStreamResponse.data.airlines == null
                || flightStreamResponse.data.airlines.isEmpty()) {
            return;
        }
        Map<String, String> next = new LinkedHashMap<>();
        for (Map.Entry<String, FlightStreamResponse.AirlineDetail> e : flightStreamResponse.data.airlines.entrySet()) {
            FlightStreamResponse.AirlineDetail d = e.getValue();
            if (d == null) {
                continue;
            }
            String code = d.code != null && !d.code.isBlank() ? d.code : e.getKey();
            String label = d.displayName;
            if (label == null || label.isBlank()) {
                label = d.name;
            }
            if (label == null || label.isBlank()) {
                label = d.shortName;
            }
            if (label == null || label.isBlank()) {
                label = code;
            }
            next.put(code, label);
        }
        lastOneWayStreamingAirlineCodeToName = Collections.unmodifiableMap(next);
        logger.log("Captured streaming airline directory (" + next.size() + " entries) for one-way dynamic search.");
    }

    private boolean matchAirline(String airlineNameActual, String airlineNameExpected) {
        if (airlineNameActual == null || airlineNameExpected == null) {
            return false;
        }

        airlineNameActual = airlineNameActual.trim();
        airlineNameExpected = airlineNameExpected.trim();

        if (airlineNameActual.isEmpty() || airlineNameExpected.isEmpty()) {
            return false;
        }

        String[] airlineActual = airlineNameActual.split("\\s+");
        String[] airlineExpected = airlineNameExpected.split("\\s+");

        if (airlineActual.length != airlineExpected.length) {
            return false;
        }

        for (int i = 0; i < airlineExpected.length; i++) {
            if (!airlineActual[i].equalsIgnoreCase(airlineExpected[i])) {
                return false;
            }
        }
        logger.log("Matched airline: " + airlineNameActual + " with " + airlineNameExpected);
        return true;
    }


    private FlightSearchResponse.Flight flightSearchByNameUsingStream(FlightSearchApi.Inputs inputs,String airline, String authorization,String currency,String deviceId) throws ExecutionException, InterruptedException {
        return flightSearchByNameUsingStream(inputs, airline, authorization, currency, deviceId, true);
    }

    private FlightSearchResponse.Flight flightSearchByNameUsingStream(FlightSearchApi.Inputs inputs,String airline, String authorization,String currency,String deviceId, boolean requireRescheduleAllowed) throws ExecutionException, InterruptedException {
        ArrayList<String> requestItems = flightSearchGetRequestItems(inputs);
        logger.log("requestItems->" + requestItems);
        return flightSearchStreamByName(requestItems, airline, authorization, currency, deviceId, requireRescheduleAllowed);
    }

    private ArrayList<String> flightSearchGetRequestItems(FlightSearchApi.Inputs inputs) throws ExecutionException, InterruptedException {
        FlightSearchApi flightSearchApi = new FlightSearchApi(inputs);
        int retry = 5;
        while (retry>=0){
            ApiResponse flightSearchResponse = flightSearchApi.hitApi(apiTools);

            Gson gson = new Gson();
            FlightSearchResponse flightSearchResponsePojo = gson.fromJson(flightSearchResponse.getBody(), FlightSearchResponse.class);
            if (flightSearchResponsePojo.code.equals("SUCCESS") && !flightSearchResponsePojo.data.requestItems.isEmpty()){
                logger.log("search list->" + flightSearchResponsePojo.data.searchList.departureFlights.size());
                return flightSearchResponsePojo.data.requestItems;
            }
            retry--;
        }
        return null;
    }

    private FlightSearchResponse.Flight flightSearchStreamByName(ArrayList<String> requestItems,String airline,String authorization,String currency,String deviceId) throws ExecutionException, InterruptedException{
        return flightSearchStreamByName(requestItems, airline, authorization, currency, deviceId, true);
    }

    private FlightSearchResponse.Flight flightSearchStreamByName(ArrayList<String> requestItems,String airline,String authorization,String currency,String deviceId, boolean requireRescheduleAllowed) throws ExecutionException, InterruptedException{
        int retry = 10;
        while (retry>=0){
            FlightStreamingApi.Inputs flightStreamingApiInputs = new FlightStreamingApi.Inputs(requestItems,authorization,"OW","",currency,deviceId);
            FlightStreamingApi flightStreamingApi = new FlightStreamingApi(flightStreamingApiInputs);
            ApiResponse flightStreamingResponse = flightStreamingApi.hitApi(apiTools);

            Gson gson = new Gson();
            FlightSearchResponse flightSearchResponsePojo = gson.fromJson(flightStreamingResponse.getBody(), FlightSearchResponse.class);

            if(flightSearchResponsePojo.code.equals("SUCCESS")){
                logger.log("searchList->" + flightSearchResponsePojo.data.searchList.departureFlights.size());
                HashMap<String, String> airlineMap = getAirlineCodeToNameMap(flightSearchResponsePojo.data.airlines);
                for (FlightSearchResponse.Flight depFlight : flightSearchResponsePojo.data.searchList.departureFlights) {
                    String airlineCode = depFlight.marketingAirlineCode;
                    if (!matchAirline(airlineMap.get(airlineCode), airline)) {
                        continue;
                    }
                    if (requireRescheduleAllowed
                            && (depFlight.labelTags == null
                                || depFlight.labelTags.policyInfo == null
                                || !depFlight.labelTags.policyInfo.contains("RESCHEDULE_YES"))) {
                        continue;
                    }
                    return depFlight;
                }
                if(!flightSearchResponsePojo.data.requestItems.isEmpty()){
                    requestItems = flightSearchResponsePojo.data.requestItems;
                }else {
                    return null;
                }
            }
            Thread.sleep(RETRY_DELAY_MS);
            retry--;
        }
        return null;
    }

    private static final String ENCODED_SUPPLIER_SUFFIX = "-ENC";
    private static final String DEFAULT_CONTACT_PHONE = "82246486083";

    public record MultiCityOrder(String orderId, String orderHash) {}

    private record MultiCityLeg(String flightId, String supplierId, long paxPrice, boolean comboMC,
                                String departureTime, String arrivalTime, int segment) {}

    private record MultiCityCart(List<FlightCartV6Api.CartDetail> cartDetails, String credentialCode) {}

    public MultiCityOrder createMultiCityOrderWaitingForPayment(
            String username, String password, List<String[]> routes, List<String> departureDates,
            List<String> airlineCodes, int adult, int child, int infant, String cabinClass,
            String phoneNumber) {
        return createMultiCityOrderWaitingForPayment(username, password, routes, departureDates,
                airlineCodes, adult, child, infant, cabinClass, phoneNumber, null);
    }

    public MultiCityOrder createMultiCityOrderWaitingForPayment(
            String username, String password, List<String[]> routes, List<String> departureDates,
            List<String> airlineCodes, int adult, int child, int infant, String cabinClass,
            String phoneNumber, Consumer<Set<String>> onLegsPicked) {
        return createMultiCityOrderWaitingForPayment(username, password, routes, departureDates,
                airlineCodes, adult, child, infant, cabinClass, phoneNumber, onLegsPicked, false);
    }

    public MultiCityOrder createMultiCityOrderWaitingForPayment(
            String username, String password, List<String[]> routes, List<String> departureDates,
            List<String> airlineCodes, int adult, int child, int infant, String cabinClass,
            String phoneNumber, Consumer<Set<String>> onLegsPicked, boolean corporate) {
        try {
            if (routes.size() != departureDates.size()) {
                throw new IllegalArgumentException("routes (" + routes.size() + ") and departureDates ("
                        + departureDates.size() + ") must have the same size");
            }
            String accessToken = new Unm<T>(session).getAccessToken(username, password, "B2C");

            List<FlightMultiCitySearchApi.Route> apiRoutes = new ArrayList<>();
            for (int i = 0; i < routes.size(); i++) {
                apiRoutes.add(new FlightMultiCitySearchApi.Route(routes.get(i)[0], routes.get(i)[1],
                        CoreFunctions.convertDateFormat(departureDates.get(i), "d MMMM yyyy", "yyyy-MM-dd")));
            }
            logger.log("Multi-city routes: " + apiRoutes);

            List<MultiCityLeg> picked = new ArrayList<>();
            for (int leg = 0; leg < apiRoutes.size(); leg++) {
                List<String> requestItems = leg == 0
                        ? multiCitySearchRequestItems(apiRoutes, adult, child, infant, cabinClass, accessToken)
                        : multiCityNextRequestItems(leg + 1, picked, apiRoutes, adult, child, infant, cabinClass, accessToken);
                String airlineCode = airlineCodes == null || airlineCodes.size() <= leg ? "" : airlineCodes.get(leg);
                picked.add(multiCityPickFlight(requestItems, leg + 1, apiRoutes.get(leg), airlineCode, accessToken));
            }

            if (onLegsPicked != null) {
                Set<String> supplierCodes = new LinkedHashSet<>();
                for (MultiCityLeg leg : picked) {
                    if (isSupplierMockIssued(leg.supplierId())) {
                        logger.log("Leg supplier " + leg.supplierId() + " already issues through a mock, so it is left as it is");
                    } else {
                        supplierCodes.add(decodeSupplierId(leg.supplierId()));
                    }
                }
                logger.log("Multi-city legs needing the issuance mock: " + supplierCodes);
                onLegsPicked.accept(supplierCodes);
            }

            if (!corporate) {
                validateMultiCityLegsAreMockIssued(picked, routes);
            }

            List<FlightMultiCityPrpApi.Detail> prpDetails = new ArrayList<>();
            for (MultiCityLeg leg : picked) {
                prpDetails.add(new FlightMultiCityPrpApi.Detail(leg.flightId(), leg.supplierId(), true, leg.comboMC()));
            }
            MultiCityCart cart = multiCityCartDetails(prpDetails, picked, accessToken);

            String cartId = multiCityCart(cart, accessToken);
            logger.log("Multi-city cartId->" + cartId);

            String corporateInterimId = corporate ? multiCityCorporateInterimId(cartId, apiRoutes, accessToken) : null;
            return multiCityBooking(cartId, username, phoneNumber, adult, child, infant, accessToken, corporateInterimId);
        } catch (SkipException e) {
            throw e;
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Multi-city booking via API failed: " + e.getMessage());
        }
        return null;
    }

    /** Creates a multi-city order as a corporate traveler and submits it for approval, so it sits in the dashboard's Pending Approvals. */
    public MultiCityOrder createMultiCityOrderB2BPendingApproval(
            String username, String password, List<String[]> routes, List<String> departureDates,
            List<String> airlineCodes, int adult, int child, int infant, String cabinClass,
            String phoneNumber, EmployeeData employeeData) throws ExecutionException, InterruptedException {
        MultiCityOrder order = createMultiCityOrderWaitingForPayment(username, password, routes, departureDates,
                airlineCodes, adult, child, infant, cabinClass, phoneNumber, null, true);
        String accessToken = new Unm<T>(session).getAccessToken(username, password, "B2C");
        long orderId = Long.parseLong(order.orderId());
        int employeeId = employeeData.data.employeeID;
        int corporateId = employeeData.data.corporateID;
        logger.log("submit payment->" + submitPaymentB2B(new SubmitPaymentApiB2B.Inputs(accessToken, order.orderHash(), orderId, employeeId, corporateId)));
        logger.log("submit payment put->" + submitPaymentB2BPUT(new SubmitPaymentPutApiB2B.Inputs(accessToken, order.orderHash(), orderId, employeeId, corporateId, "testing only")));
        return order;
    }

    private void validateMultiCityLegsAreMockIssued(List<MultiCityLeg> legs, List<String[]> routes) {
        for (int i = 0; i < legs.size(); i++) {
            String supplierId = legs.get(i).supplierId();
            if (!isSupplierMockIssued(supplierId)) {
                Assertion.skip("Flight is not mocked: supplier is NOT mock-issued for leg " + (i + 1)
                        + " " + routes.get(i)[0] + "-" + routes.get(i)[1] + " (supplierId " + supplierId
                        + "). Skipping to avoid a real issuance.");
            }
        }
        logger.log("All " + legs.size() + " multi-city leg(s) are served by mock-issued suppliers.");
    }


    private String multiCityCorporateInterimId(String cartId, List<FlightMultiCitySearchApi.Route> routes, String accessToken) throws ExecutionException, InterruptedException {
        JSONArray segments = new JSONArray();
        for (int i = 0; i < routes.size(); i++) {
            FlightAutocompleteResponse.Airport origin = getAirportData(accessToken, routes.get(i).origin());
            FlightAutocompleteResponse.Airport destination = getAirportData(accessToken, routes.get(i).destination());
            segments.put(new JSONObject()
                    .put("departureCity", origin.cityName).put("departureCityCode", origin.cityCode).put("departureAirportCode", origin.airportCode)
                    .put("arrivalCity", destination.cityName).put("arrivalCityCode", destination.cityCode).put("arrivalAirportCode", destination.airportCode)
                    .put("segmentIndex", i + 1).put("departureDate", routes.get(i).departureDate()));
        }
        String segment = Base64.getEncoder().encodeToString(segments.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));

        PostInterimDetailsApiFlight details = new PostInterimDetailsApiFlight();
        details.productType = "flight";
        details.verticalCartID = cartId;
        details.bookingCriteria = new PostInterimDetailsApiFlight.BookingCriteria();
        details.bookingCriteria.flight = new PostInterimDetailsApiFlight.Flight();
        fillMultiCityFlightCriteria(details.bookingCriteria.flight, cartId, segment);
        postInterimDetail(new PostInterimDetailsApi.Inputs(accessToken, null, details));

        PostInterimTransactionsApiBodyFlight transactions = new PostInterimTransactionsApiBodyFlight();
        transactions.productType = "flight";
        transactions.verticalCartID = cartId;
        transactions.travelPlanDetailID = "0";
        transactions.customFields = new ArrayList<>();
        transactions.costCenters = new ArrayList<>();
        transactions.bookingCriteria = new PostInterimTransactionsApiBodyFlight.BookingCriteria();
        transactions.bookingCriteria.flight = new PostInterimTransactionsApiBodyFlight.Flight();
        fillMultiCityFlightCriteria(transactions.bookingCriteria.flight, cartId, segment);
        String corporateInterimId = postInterimTransactions(new PostInterimTransactionsApi.Inputs(accessToken, null, transactions));
        logger.log("Multi-city corporateInterimID->" + corporateInterimId);
        return corporateInterimId;
    }

    private void fillMultiCityFlightCriteria(PostInterimDetailsApiFlight.Flight flight, String cartId, String segment) {
        flight.productType = "flight";
        flight.cartId = cartId;
        flight.itineraryType = "MULTI_CITY";
        flight.depart = "";
        flight.returnValue = "";
        flight.departureDate = "";
        flight.isRoundTrip = "false";
        flight.returnDate = "";
        flight.segment = segment;
    }

    private void fillMultiCityFlightCriteria(PostInterimTransactionsApiBodyFlight.Flight flight, String cartId, String segment) {
        flight.productType = "flight";
        flight.cartId = cartId;
        flight.itineraryType = "MULTI_CITY";
        flight.depart = "";
        flight.returnValue = "";
        flight.departureDate = "";
        flight.isRoundTrip = "false";
        flight.returnDate = "";
        flight.segment = segment;
    }

    private MultiCityOrder multiCityBooking(String cartId, String email, String phoneNumber,
            int adult, int child, int infant, String accessToken, String corporateInterimId) throws Exception {
        ApiResponse cartResponse = new FlightCartV6GetApi(
                new FlightCartV6GetApi.Inputs(cartId, accessToken)).hitApi(apiTools);
        JSONObject cart = new JSONObject(cartResponse.getBody()).optJSONObject("data");
        if (cart == null) {
            throw new NotFoundException("No cart detail for cartId " + cartId + ". Response: "
                    + CoreFunctions.shorten(cartResponse.getBody(), 500));
        }

        int scale = cart.optJSONObject("multiCurrency") == null ? 0
                : cart.optJSONObject("multiCurrency").optInt("scale", 0);
        List<String> contactFields = contactFormFields(cart);
        List<String> adultFields = passengerFormFields(cart, "adultFormSections");
        List<String> childFields = passengerFormFields(cart, "childFormSections");
        List<String> infantFields = passengerFormFields(cart, "infantFormSections");
        logger.log("Cart mandatory fields -> contact=" + contactFields + " adult=" + adultFields
                + " child=" + childFields + " infant=" + infantFields);

        JSONObject body = new JSONObject()
                .put("cartId", cartId)
                .put("contact", buildContact(contactFields, email, phoneNumber))
                .put("adults", buildPassengers(adultFields, adult, "Adult"))
                .put("childs", buildPassengers(childFields, child, "Child"))
                .put("infants", buildPassengers(infantFields, infant, "Infant"))
                .put("multiInsurances", new JSONArray())
                .put("claimVouchers", new JSONArray())
                .put("scale", scale)
                .put("currencyScale", scale);
        if (corporateInterimId != null) {
            body.put("corporateDataRequest", new JSONObject().put("corporateInterimId", corporateInterimId).put("corporatePaymentPreference", ""));
        }

        ApiResponse bookingResponse = new FlightBookingV7Api(
                new FlightBookingV7Api.Inputs(body.toString(), accessToken)).hitApi(apiTools);
        logger.log("Multi-city booking response->" + bookingResponse.getBody());

        JSONObject data = new JSONObject(bookingResponse.getBody()).optJSONObject("data");
        JSONObject successData = data == null ? null : data.optJSONObject("successData");
        if (successData == null || successData.optString("orderId", "").isBlank()) {
            throw new NotFoundException("Booking did not return an orderId. Response: "
                    + CoreFunctions.shorten(bookingResponse.getBody(), 800));
        }
        String orderId = String.valueOf(successData.get("orderId"));
        String orderHash = successData.optString("orderHash", "");
        logger.log("Multi-city orderId->" + orderId + " orderHash->" + orderHash);
        return new MultiCityOrder(orderId, orderHash);
    }

    private List<String> contactFormFields(JSONObject cart) {
        List<String> fields = new ArrayList<>();
        JSONObject contactSegment = cart.optJSONObject("contactSegment");
        JSONArray contactForm = contactSegment == null ? null : contactSegment.optJSONArray("contactForm");
        if (contactForm != null) {
            for (int i = 0; i < contactForm.length(); i++) {
                fields.add(contactForm.getJSONObject(i).optString("name"));
            }
        }
        return fields;
    }

    private List<String> passengerFormFields(JSONObject cart, String sectionKey) {
        List<String> fields = new ArrayList<>();
        JSONObject passengerFormSection = cart.optJSONObject("passengerFormSection");
        JSONArray sections = passengerFormSection == null ? null : passengerFormSection.optJSONArray(sectionKey);
        if (sections == null) {
            return fields;
        }
        for (int i = 0; i < sections.length(); i++) {
            JSONArray forms = sections.getJSONObject(i).optJSONArray("forms");
            if (forms == null) {
                continue;
            }
            for (int j = 0; j < forms.length(); j++) {
                String name = forms.getJSONObject(j).optString("name");
                if (!name.isBlank() && !fields.contains(name)) {
                    fields.add(name);
                }
            }
        }
        return fields;
    }

    private JSONObject buildContact(List<String> fields, String email, String phoneNumber) {
        String firstName = CoreFunctions.randomNameWithoutTest();
        String lastName = CoreFunctions.randomLastName();
        String phone = isBlankString(phoneNumber) ? DEFAULT_CONTACT_PHONE : phoneNumber.trim();
        JSONObject contact = new JSONObject();
        for (String field : fields) {
            switch (field) {
                case "fullName" -> contact.put(field, firstName + " " + lastName);
                case "firstName" -> contact.put(field, firstName);
                case "lastName" -> contact.put(field, lastName);
                case "title" -> contact.put(field, "Mr");
                case "areaCode" -> contact.put(field, "+62");
                case "phone" -> contact.put(field, phone);
                case "email" -> contact.put(field, email);
                default -> contact.put(field, "");
            }
        }
        return contact;
    }

    private JSONArray buildPassengers(List<String> fields, int count, String paxType) {
        JSONArray passengers = new JSONArray();
        for (int i = 0; i < count; i++) {
            String firstName = CoreFunctions.randomNameWithoutTest();
            String lastName = CoreFunctions.randomLastName();
            JSONObject passenger = new JSONObject();
            for (String field : fields) {
                switch (field) {
                    case "fullName" -> passenger.put(field, firstName + " " + lastName);
                    case "firstName" -> passenger.put(field, firstName);
                    case "lastName" -> passenger.put(field, lastName);
                    case "title" -> passenger.put(field, "Mr");
                    case "nationality" -> passenger.put(field, "ID");
                    case "identityNumber" -> passenger.put(field, String.valueOf(CoreFunctions.randomInt(16)));
                    case "dob" -> passenger.put(field, dateOfBirthFor(paxType));
                    default -> passenger.put(field, "");
                }
            }
            passengers.put(passenger);
        }
        return passengers;
    }

    private String dateOfBirthFor(String paxType) {
        LocalDate today = LocalDate.now();
        return switch (paxType) {
            case "Child" -> today.minusYears(6).toString();
            case "Infant" -> today.minusMonths(10).toString();
            default -> today.minusYears(30).toString();
        };
    }

    private List<String> multiCitySearchRequestItems(List<FlightMultiCitySearchApi.Route> routes, int adult,
            int child, int infant, String cabinClass, String accessToken) throws Exception {
        FlightMultiCitySearchApi api = new FlightMultiCitySearchApi(
                new FlightMultiCitySearchApi.Inputs(routes, adult, child, infant, cabinClass, accessToken));
        return extractRequestItems(api.hitApi(apiTools).getBody(), 1);
    }

    private List<String> multiCityNextRequestItems(int segment, List<MultiCityLeg> picked,
            List<FlightMultiCitySearchApi.Route> routes, int adult, int child, int infant,
            String cabinClass, String accessToken) throws Exception {
        List<FlightMultiCitySearchNextApi.SelectedFlight> selected = new ArrayList<>();
        for (MultiCityLeg leg : picked) {
            selected.add(new FlightMultiCitySearchNextApi.SelectedFlight(leg.paxPrice(), leg.flightId(),
                    leg.comboMC(), leg.supplierId(), leg.departureTime(), leg.arrivalTime(), leg.segment()));
        }
        FlightMultiCitySearchNextApi api = new FlightMultiCitySearchNextApi(
                new FlightMultiCitySearchNextApi.Inputs(segment, selected, routes, adult, child, infant,
                        cabinClass, accessToken));
        return extractRequestItems(api.hitApi(apiTools).getBody(), segment);
    }

    private List<String> extractRequestItems(String body, int segment) {
        JSONObject data = new JSONObject(body).optJSONObject("data");
        JSONArray items = data == null ? null : data.optJSONArray("requestItems");
        if (items == null || items.isEmpty()) {
            throw new NotFoundException("No multi-city requestItems returned for journey " + segment
                    + ". Response: " + CoreFunctions.shorten(body, 500));
        }
        List<String> requestItems = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            requestItems.add(items.getString(i));
        }
        logger.log("Journey " + segment + " requestItems: " + requestItems.size());
        return requestItems;
    }

    private static final int MULTI_CITY_STREAMING_RETRY = 15;
    private static final long MULTI_CITY_STREAMING_RETRY_DELAY_MS = 2000L;

    private MultiCityLeg multiCityPickFlight(List<String> requestItems, int segment,
            FlightMultiCitySearchApi.Route route, String airlineCode, String accessToken) throws Exception {
        FlightMultiCityStreamingApi api = new FlightMultiCityStreamingApi(
                new FlightMultiCityStreamingApi.Inputs(requestItems, accessToken));
        Set<String> airlinesSeen = new LinkedHashSet<>();
        JSONObject flight = null;

        for (int attempt = 1; attempt <= MULTI_CITY_STREAMING_RETRY && flight == null; attempt++) {
            try {
                JSONArray flights = streamingFlights(api.hitApi(apiTools).getBody());
                for (int i = 0; i < flights.length(); i++) {
                    airlinesSeen.addAll(airlinesOf(flights.getJSONObject(i)));
                }
                flight = selectFlightByAirlineCode(flights, airlineCode);
                if (flight == null) {
                    logger.log("Leg " + segment + " attempt " + attempt + "/" + MULTI_CITY_STREAMING_RETRY
                            + ": " + flights.length() + " flight(s), airlines so far " + airlinesSeen
                            + (isBlankString(airlineCode) ? "" : ", still waiting for " + airlineCode));
                }
            } catch (Exception e) {
                logger.log("Leg " + segment + " streaming attempt " + attempt + " failed: " + e.getLocalizedMessage());
            }
            if (flight == null && attempt < MULTI_CITY_STREAMING_RETRY) {
                Thread.sleep(MULTI_CITY_STREAMING_RETRY_DELAY_MS);
            }
        }

        if (flight == null) {
            throw new NotFoundException((isBlankString(airlineCode)
                    ? "No multi-city flight returned"
                    : "No flight operated by airline '" + airlineCode + "'")
                    + " on leg " + segment + " (" + route.origin() + "-" + route.destination() + ") on "
                    + route.departureDate() + " after " + MULTI_CITY_STREAMING_RETRY
                    + " streaming attempts. Airlines returned: " + airlinesSeen);
        }

        JSONObject tracker = flight.optJSONObject("trackerProperties");
        MultiCityLeg leg = new MultiCityLeg(
                flight.getString("flightId"),
                flight.getString("supplierId"),
                tracker == null ? 0L : tracker.optLong("price"),
                flight.optBoolean("comboMC"),
                cardTimeOf(flight, "departure"),
                cardTimeOf(flight, "arrival"),
                flight.optInt("multiCitySegment", segment));
        logger.log("Leg " + segment + " (" + route.origin() + "-" + route.destination() + ") picked airline="
                + airlinesOf(flight) + " flightId=" + leg.flightId() + " supplierId=" + leg.supplierId()
                + " comboMC=" + leg.comboMC());
        return leg;
    }

    private JSONArray streamingFlights(String body) {
        JSONObject data = new JSONObject(body).optJSONObject("data");
        JSONObject searchList = data == null ? null : data.optJSONObject("searchList");
        JSONArray flights = searchList == null ? null : searchList.optJSONArray("departureFlights");
        return flights == null ? new JSONArray() : flights;
    }

    private JSONObject selectFlightByAirlineCode(JSONArray flights, String airlineCode) {
        if (flights.isEmpty()) {
            return null;
        }
        if (isBlankString(airlineCode)) {
            return flights.getJSONObject(0);
        }
        for (int i = 0; i < flights.length(); i++) {
            JSONObject flight = flights.getJSONObject(i);
            if (airlinesOf(flight).stream().anyMatch(code -> code.equalsIgnoreCase(airlineCode.trim()))) {
                return flight;
            }
        }
        return null;
    }


    private String cardTimeOf(JSONObject flight, String leg) {
        JSONObject card = flight.optJSONObject("card");
        JSONObject point = card == null ? null : card.optJSONObject(leg);
        String time = point == null ? "" : point.optString("time", "");
        if (time.isBlank()) {
            logger.log("No card." + leg + ".time on the streaming flight; /next may reject it.");
        }
        return time;
    }

    private List<String> airlinesOf(JSONObject flight) {
        List<String> airlines = new ArrayList<>();
        JSONObject filterProperties = flight.optJSONObject("filterProperties");
        JSONArray codes = filterProperties == null ? null : filterProperties.optJSONArray("airlines");
        if (codes != null) {
            for (int i = 0; i < codes.length(); i++) {
                airlines.add(codes.getString(i));
            }
        }
        return airlines;
    }

    private MultiCityCart multiCityCartDetails(List<FlightMultiCityPrpApi.Detail> prpDetails,
            List<MultiCityLeg> picked, String accessToken) throws Exception {
        FlightMultiCityPrpApi api = new FlightMultiCityPrpApi(
                new FlightMultiCityPrpApi.Inputs(prpDetails, accessToken));
        String body = api.hitApi(apiTools).getBody();

        JSONArray multiCityDetail = collectMultiCityDetail(body);
        if (multiCityDetail.length() != picked.size()) {
            throw new NotFoundException("prp/flight-detail returned " + multiCityDetail.length()
                    + " multiCityDetail entr(ies) for " + picked.size() + " journey(s). Response: "
                    + CoreFunctions.shorten(body, 500));
        }
        List<FlightCartV6Api.CartDetail> cartDetails = new ArrayList<>();
        for (int i = 0; i < multiCityDetail.length(); i++) {
            JSONObject detail = multiCityDetail.getJSONObject(i);
            cartDetails.add(new FlightCartV6Api.CartDetail(
                    detail.getString("flightId"),
                    detail.optString("supplierId", picked.get(i).supplierId()),
                    detail.optString("fareBasisCode", ""),
                    detail.optBoolean("comboMC", picked.get(i).comboMC()),
                    false));
        }
        String credentialCode = collectCredentialCode(body);
        logger.log("Cart details from multiCityDetail: " + cartDetails.size() + " journey(s), credentialCode="
                + credentialCode);
        return new MultiCityCart(cartDetails, credentialCode);
    }

    private JSONArray collectMultiCityDetail(String body) {
        JSONObject data = new JSONObject(body).optJSONObject("data");
        JSONArray searchList = data == null ? null : data.optJSONArray("searchList");
        if (searchList == null) {
            return new JSONArray();
        }
        for (int i = 0; i < searchList.length(); i++) {
            JSONArray items = searchList.getJSONObject(i).optJSONArray("items");
            if (items == null) {
                continue;
            }
            for (int j = 0; j < items.length(); j++) {
                JSONArray multiCityDetail = items.getJSONObject(j).optJSONArray("multiCityDetail");
                if (multiCityDetail != null && !multiCityDetail.isEmpty()) {
                    return multiCityDetail;
                }
            }
        }
        return new JSONArray();
    }

    private String collectCredentialCode(String body) {
        JSONObject data = new JSONObject(body).optJSONObject("data");
        JSONArray searchList = data == null ? null : data.optJSONArray("searchList");
        if (searchList == null) {
            return null;
        }
        for (int i = 0; i < searchList.length(); i++) {
            JSONArray items = searchList.getJSONObject(i).optJSONArray("items");
            if (items == null) {
                continue;
            }
            for (int j = 0; j < items.length(); j++) {
                JSONObject info = items.getJSONObject(j).optJSONObject("additionalInformation");
                String code = info == null ? null : info.optString("credentialCode", null);
                if (code != null && !code.isBlank()) {
                    return code;
                }
            }
        }
        return null;
    }

    private String multiCityCart(MultiCityCart cart, String accessToken) throws Exception {
        FlightCartV6Api api = new FlightCartV6Api(
                new FlightCartV6Api.Inputs(cart.cartDetails(), cart.credentialCode(), accessToken));
        String body = api.hitApi(apiTools).getBody();
        logger.log("Multi-city cart response->" + body);
        JSONObject data = new JSONObject(body).optJSONObject("data");
        String cartId = data == null ? null : data.optString("cartId", null);
        if (cartId == null || cartId.isBlank()) {
            throw new NotFoundException("No cartId returned by v6/cart. Response: " + CoreFunctions.shorten(body, 500));
        }
        return cartId;
    }

    private String flightCartApi(FlightCartApi.Inputs inputs, FlightCartApi.InputsRoundTrip inputsRoundTrip) throws ExecutionException, InterruptedException {
        FlightCartApi flightCartApi = new FlightCartApi(inputs, inputsRoundTrip);
        ApiResponse flightCartResponse = flightCartApi.hitApi(apiTools);
        logger.log("Flight Cart API response->" + flightCartResponse.getBody());
        Gson gson = new Gson();
        FlightCartId flightCartId = gson.fromJson(flightCartResponse.getBody(), FlightCartId.class);
        logger.log("cartId->" + flightCartId.data.cartId);
        return flightCartId.data.cartId;
    }

    public FlightAutocompleteResponse.Airport getAirportData(String accessToken, String airportName) {
        try{
            FlightAutocompleteApi.Inputs inputs = new FlightAutocompleteApi.Inputs(airportName,accessToken);
            FlightAutocompleteResponse flightAutocompleteResponse = flightAutocompleteApi(inputs);
            List<FlightAutocompleteResponse.Airport> airports = flightAutocompleteResponse.data.airports;
            String query = airportName == null ? "" : airportName.trim().toLowerCase();

            for (FlightAutocompleteResponse.Airport airport : airports) {
                logger.log("Airport Name->" + airport.airportName + ", Airport Code->" + airport.airportCode);
                if (airport.airportCode != null && airport.airportCode.equalsIgnoreCase(query)) {
                    logger.log("Find airport data successfull");
                    return airport;
                }
                if (airport.child != null) {
                    for (FlightAutocompleteResponse.Child child : airport.child) {
                        if (child != null && child.airportCode != null && child.airportCode.equalsIgnoreCase(query)) {
                            logger.log("Find airport data successfull (nested under city: " + airport.airportName + ")");
                            return childToAirport(child);
                        }
                    }
                }
            }

            for (FlightAutocompleteResponse.Airport airport : airports) {
                if ((airport.airportName != null && airport.airportName.toLowerCase().contains(query))
                        || (airport.airportCode != null && airport.airportCode.toLowerCase().contains(query))) {
                    logger.log("Find airport data successfull (fuzzy match)");
                    return airport;
                }
                if (airport.child != null) {
                    for (FlightAutocompleteResponse.Child child : airport.child) {
                        if (child == null) continue;
                        if ((child.airportName != null && child.airportName.toLowerCase().contains(query))
                                || (child.airportCode != null && child.airportCode.toLowerCase().contains(query))) {
                            logger.log("Find airport data successfull (fuzzy match, nested under city: " + airport.airportName + ")");
                            return childToAirport(child);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            logger.log("Error occurred: "+ e);
            Assertion.skip("Flight Booking via API failed. Skipping the subsequent steps.");
        }
        logger.log("data not found");
        return null;
    }

    private FlightAutocompleteResponse.Airport childToAirport(FlightAutocompleteResponse.Child child) {
        FlightAutocompleteResponse.Airport airport = new FlightAutocompleteResponse.Airport();
        airport.icon = child.icon;
        airport.cityCode = child.cityCode;
        airport.airportCode = child.airportCode;
        airport.countryCode = child.countryCode;
        airport.airportName = child.airportName;
        airport.cityName = child.cityName;
        airport.countryName = child.countryName;
        airport.timezone = child.timezone;
        airport.type = child.type;
        return airport;
    }

    private FlightAutocompleteResponse flightAutocompleteApi (FlightAutocompleteApi.Inputs inputs) throws ExecutionException,InterruptedException {
        FlightAutocompleteApi flightAutocompleteApi = new FlightAutocompleteApi(inputs);
        ApiResponse response = flightAutocompleteApi.hitApi(apiTools);
        JSONObject jsonObject = new JSONObject(response.getBody());
        logger.log("Flight Autocomplete API response->" + jsonObject);
        Gson gson = new Gson();
        return gson.fromJson(response.getBody(), FlightAutocompleteResponse.class);
    }

    /**
     * This method is used to get a token.
     *
     * @return String Returns the access token.
     * @throws Exception
     */
    private String getToken(String deviceId) throws Exception {
        PayExpressApiWithInputs.Inputs inputs = new PayExpressApiWithInputs.Inputs(deviceId);
        PayExpressApiWithInputs payExpressApi = new PayExpressApiWithInputs(inputs);
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
    }

    private PostInterimDetailsApiFlight getBodyPostInterimDetailsApiFlight(String cartId,FlightAutocompleteResponse.Airport originData,FlightAutocompleteResponse.Airport destinationData,String isRoundTrip,String departureDate,String returnDate){
        PostInterimDetailsApiFlight postInterimDetailsApiFlight = new PostInterimDetailsApiFlight();
        postInterimDetailsApiFlight.productType = "flight";
        postInterimDetailsApiFlight.verticalCartID = cartId;
        postInterimDetailsApiFlight.bookingCriteria = new PostInterimDetailsApiFlight.BookingCriteria();
        postInterimDetailsApiFlight.bookingCriteria.flight = new PostInterimDetailsApiFlight.Flight();
        postInterimDetailsApiFlight.bookingCriteria.flight.cartId = cartId;
        postInterimDetailsApiFlight.bookingCriteria.flight.departureAirportCode = originData.airportCode;
        postInterimDetailsApiFlight.bookingCriteria.flight.departureCityCode = originData.cityCode;
        postInterimDetailsApiFlight.bookingCriteria.flight.arrivalAirportCode = destinationData.airportCode;
        postInterimDetailsApiFlight.bookingCriteria.flight.arrivalCityCode = destinationData.cityCode;
        postInterimDetailsApiFlight.bookingCriteria.flight.isRoundTrip = isRoundTrip;
        postInterimDetailsApiFlight.bookingCriteria.flight.departureDate = departureDate;
        postInterimDetailsApiFlight.bookingCriteria.flight.returnDate = returnDate;
        return postInterimDetailsApiFlight;
    }

    private PostInterimDetailsApiResponse postInterimDetail(PostInterimDetailsApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PostInterimDetailsApi postInterimDetailsApi = new PostInterimDetailsApi(inputs);
        ApiResponse postInterimDetailsApiResponse = postInterimDetailsApi.hitApi(apiTools);
        Gson gson = new Gson();
        PostInterimDetailsApiResponse postInterimDetailsApiResponse1 = gson.fromJson(postInterimDetailsApiResponse.getBody(),PostInterimDetailsApiResponse.class);
        logger.log("postInterimDetailsApiResponse1 -> " + postInterimDetailsApiResponse1.code);
        return postInterimDetailsApiResponse1;
    }

    private PostInterimTransactionsApiBodyFlight getBodyPostInterimTransactionsApiBodyFlight(PostInterimDetailsApiFlight postInterimDetailsApiFlight){
        PostInterimTransactionsApiBodyFlight postInterimTransactionsApiBodyFlight = new PostInterimTransactionsApiBodyFlight();
        postInterimTransactionsApiBodyFlight.verticalCartID = postInterimDetailsApiFlight.verticalCartID;
        postInterimTransactionsApiBodyFlight.travelPlanDetailID =  "0";
        postInterimTransactionsApiBodyFlight.productType = postInterimDetailsApiFlight.productType;

        postInterimTransactionsApiBodyFlight.bookingCriteria = new PostInterimTransactionsApiBodyFlight.BookingCriteria();
        postInterimTransactionsApiBodyFlight.bookingCriteria.flight = new PostInterimTransactionsApiBodyFlight.Flight();
        postInterimTransactionsApiBodyFlight.bookingCriteria.flight.cartId = postInterimDetailsApiFlight.bookingCriteria.flight.cartId;
        postInterimTransactionsApiBodyFlight.bookingCriteria.flight.departureAirportCode = postInterimDetailsApiFlight.bookingCriteria.flight.departureAirportCode;
        postInterimTransactionsApiBodyFlight.bookingCriteria.flight.departureCityCode = postInterimDetailsApiFlight.bookingCriteria.flight.departureCityCode;
        postInterimTransactionsApiBodyFlight.bookingCriteria.flight.arrivalAirportCode = postInterimDetailsApiFlight.bookingCriteria.flight.arrivalAirportCode;
        postInterimTransactionsApiBodyFlight.bookingCriteria.flight.arrivalCityCode = postInterimDetailsApiFlight.bookingCriteria.flight.arrivalCityCode;
        postInterimTransactionsApiBodyFlight.bookingCriteria.flight.isRoundTrip = postInterimDetailsApiFlight.bookingCriteria.flight.isRoundTrip;
        postInterimTransactionsApiBodyFlight.bookingCriteria.flight.departureDate = postInterimDetailsApiFlight.bookingCriteria.flight.departureDate;
        postInterimTransactionsApiBodyFlight.bookingCriteria.flight.returnDate = postInterimDetailsApiFlight.bookingCriteria.flight.returnDate;
        return postInterimTransactionsApiBodyFlight;
    }

    private String postInterimTransactions(PostInterimTransactionsApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PostInterimTransactionsApi postInterimTransactionsApi = new PostInterimTransactionsApi(inputs);
        ApiResponse apiResponse = postInterimTransactionsApi.hitApi(apiTools);
        JSONObject object = new JSONObject(apiResponse.getBody());
        logger.log("res-> " + object);
        return object.getJSONObject("data").getString("corporateInterimID");
    }

    private FlightBookingDetailDomestic flightBookingApiDomesticCorporate(FlightBookingAPiDomesticCorporate.Inputs inputs) throws ExecutionException, InterruptedException {
        FlightBookingAPiDomesticCorporate flightBookingApi = new FlightBookingAPiDomesticCorporate(inputs);
        ApiResponse flightBookingResponse = flightBookingApi.hitApi(apiTools);
        Gson gson = new Gson();
        FlightBookingDetailDomestic flightBookingDetailDomestic = gson.fromJson(flightBookingResponse.getBody(), FlightBookingDetailDomestic.class);
        logger.log("Flight Booking API response->" + flightBookingResponse.getBody());

        return flightBookingDetailDomestic;
    }

    private String submitPaymentB2B(SubmitPaymentApiB2B.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitPaymentApiB2B submitPaymentApi = new SubmitPaymentApiB2B(inputs);
        ApiResponse apiResponse = submitPaymentApi.hitApi(apiTools);

        JSONObject response = new JSONObject(apiResponse.getBody());
        logger.log("Payment api response -> " +response.toString());
        return response.getString("code");
    }

    private String submitPaymentB2BPUT(SubmitPaymentPutApiB2B.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitPaymentPutApiB2B submitPaymentApi = new SubmitPaymentPutApiB2B(inputs);
        ApiResponse apiResponse = submitPaymentApi.hitApi(apiTools);

        JSONObject response = new JSONObject(apiResponse.getBody());
        logger.log("Payment api response -> " +response.toString());
        return response.getString("code");
    }

    /**
     * This method is used to create a flight booking for B2C domestic flights.
     *
     * @param username            The username for login.
     * @param password            The password for login.
     * @param originStation       The origin station code.
     * @param destinationStation  The destination station code.
     * @param departureDate       The departure date in the format "yyyy-MM-dd".
     * @param searchType          The type of search (e.g., "oneway", "roundtrip").
     * @param adult               Number of adults.
     * @param child               Number of children.
     * @param infant              Number of infants.
     * @param airline             The preferred airline name.
     * @param firstName           First name of the passenger.
     * @param lastName            Last name of the passenger.
     * @return                   The order ID as a String if successful, null otherwise.
     */
    @SneakyThrows
    public String createWaitForPaymentFlightB2CDomestic(
            String username,
            String password,
            String originStation,
            String destinationStation,
            String departureDate,
            String searchType,
            int adult,
            int child,
            int infant,
            String airline,
            String firstName,
            String lastName
    ) {
        return createWaitForPaymentFlightB2CDomestic(username, password, originStation, destinationStation,
                departureDate, searchType, adult, child, infant, airline, firstName, lastName, true);
    }

    /**
     * Overload of {@link #createWaitForPaymentFlightB2CDomestic} that uses the same streaming
     * search chain as {@link #findDate} / {@link #validateDate} (i.e.
     * {@link #flightSearchGetRequestItems} + {@link #flightSearchStreamByNameDynamicData}) and
     * adds a {@code reschedule} flag to toggle the {@code RESCHEDULE_YES} policy filter.
     *
     * <ul>
     *   <li>{@code reschedule = true}  - require a flight whose
     *       {@code labelTags.policyInfo} contains {@code RESCHEDULE_YES} (matches the original
     *       behaviour of the method and is required for cancel/reschedule flows).</li>
     *   <li>{@code reschedule = false} - no reschedule filter; the first flight matching the
     *       given {@code airline} is used.</li>
     * </ul>
     *
     * <p>When no flight matches (e.g. the airline has no reschedulable fare on this route/date)
     * the method calls {@link Assertion#skip(String)} with a specific reason instead of
     * throwing a generic {@link NullPointerException}.</p>
     */
    @SneakyThrows
    public String createWaitForPaymentFlightB2CDomestic(
            String username,
            String password,
            String originStation,
            String destinationStation,
            String departureDate,
            String searchType,
            int adult,
            int child,
            int infant,
            String airline,
            String firstName,
            String lastName,
            boolean reschedule
    ) {
        try {
            String[] passengerNames = {firstName + " " + lastName};
            Unm<T> unm = new Unm<>(session);
            LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(username, password);
            String loginAccessToken = loginDataUnmResponse.accessToken;
            logger.log("accessToken for dashboard request->" + loginAccessToken);

            String deviceId = CoreFunctions.generateDeviceId();
            logger.log("deviceId for request->" + deviceId);
            logger.log("reschedule filter -> " + reschedule);

            String currency = "IDR";
            String tag = reschedule ? "[reschedule]" : "";
            String airlineFilter = airline == null ? "" : airline;

            FlightSearchApi.Inputs flightSearchApiInputs = new FlightSearchApi.Inputs(
                    originStation.toUpperCase(), destinationStation.toUpperCase(),
                    selectOriginAndDestinationType(originStation),
                    selectOriginAndDestinationType(destinationStation),
                    departureDate, adult, child, infant,
                    searchType, loginAccessToken, currency, deviceId
            );

            ArrayList<String> requestItems = flightSearchGetRequestItems(flightSearchApiInputs);
            if (requestItems == null || requestItems.isEmpty()) {
                Assertion.skip("No request items from flight search for " + originStation + "-" + destinationStation
                        + " on " + departureDate + ".");
                return null;
            }
            logger.log("requestItems -> " + requestItems);

            FlightStreamResponse.DepartureFlight departureFlight = flightSearchStreamByNameDynamicData(
                    requestItems.stream().toList(), "OW", "", loginAccessToken,
                    airlineFilter, tag, child, currency, deviceId
            );

            if (departureFlight == null) {
                Assertion.skip("No flight found for airline '" + airlineFilter + "' on "
                        + originStation + "-" + destinationStation + " " + departureDate
                        + (reschedule ? " with RESCHEDULE_YES policy" : "") + ".");
                return null;
            }
            logger.log("Flight Search API response->true");

            String flightId = departureFlight.flightId;
            logger.log("Flight ID----->" + flightId);
            String supplierID = departureFlight.fareDetail.fares.get(0).supplierId;
            logger.log("Supplier ID----->" + supplierID);

            FlightCartApi.Inputs flightCartApiInputs = new FlightCartApi.Inputs(flightId, supplierID, loginAccessToken);
            String cartId = flightCartApi(flightCartApiInputs, null);
            logger.log("Cart ID---->" + cartId);
            String phoneNumber = loginDataUnmResponse.accountPhoneNumber;

            FlightBookingApiDomestic.Inputs flightBookingApiInputs = new FlightBookingApiDomestic.Inputs(cartId, username, loginAccessToken, passengerNames, phoneNumber, adult);
            FlightBookingDetailDomestic flightBookingDetailDomestic = flightBookingApiDomestic(flightBookingApiInputs);
            log("Flight Booking API response->" + flightBookingDetailDomestic);
            long orderId = flightBookingDetailDomestic.data.successData.orderId;
            String orderHash = flightBookingDetailDomestic.data.successData.orderHash;

            logger.log("orderId->" + orderId);
            logger.log("orderHash->" + orderHash);

            return String.valueOf(orderId);
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Flight Booking via API failed. Skipping the subsequent steps.");
        }
        return null;
    }

    /**
     * Variant of {@link #createWaitForPaymentFlightB2CDomestic} that, instead of targeting a
     * specific airline, picks the <strong>first available flight whose airline is NOT</strong>
     * the given {@code skipAirline} and creates a wait-for-payment order with it.
     *
     * <p>Typical use case: a test needs to generate an order against any airline <em>other</em>
     * than the one already used in a previous step (e.g. to cover a multi-airline scenario, or
     * to avoid a specific airline that has known test-data issues).</p>
     *
     * <p>If {@code skipAirline} is {@code null} or blank, the first flight returned by the search
     * is used (i.e. behaves like the base method with no airline filter).</p>
     *
     * @param skipAirline the airline name to exclude (case-insensitive, trimmed); may be {@code null} or blank
     * @return the booked order ID, or {@code null} when no suitable flight is found or the booking fails
     *         (in which case {@link Assertion#skip(String)} is invoked to halt the test gracefully)
     */
    @SneakyThrows
    public String createWaitForPaymentFlightB2CDomesticSkipAirline(
            String username,
            String password,
            String originStation,
            String destinationStation,
            String departureDate,
            String searchType,
            int adult,
            int child,
            int infant,
            String skipAirline,
            String firstName,
            String lastName
    ) {
        try {
            String[] passengerNames = {firstName + " " + lastName};
            Unm<T> unm = new Unm<>(session);
            LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(username, password);
            String loginAccessToken = loginDataUnmResponse.accessToken;
            logger.log("accessToken for dashboard request->" + loginAccessToken);

            String deviceId = CoreFunctions.generateDeviceId();
            logger.log("deviceId for request->" + deviceId);

            String currency = "IDR";
            String skip = skipAirline == null ? "" : skipAirline.trim();
            logger.log("skipAirline filter -> '" + skip + "'");

            FlightSearchApi.Inputs flightSearchApiInputs = new FlightSearchApi.Inputs(
                    originStation.toUpperCase(), destinationStation.toUpperCase(),
                    selectOriginAndDestinationType(originStation),
                    selectOriginAndDestinationType(destinationStation),
                    departureDate, adult, child, infant,
                    searchType, loginAccessToken, currency, deviceId
            );

            ArrayList<String> requestItems = flightSearchGetRequestItems(flightSearchApiInputs);
            if (requestItems == null || requestItems.isEmpty()) {
                Assertion.skip("No request items from flight search for " + originStation + "-" + destinationStation
                        + " on " + departureDate + ".");
                return null;
            }
            logger.log("requestItems -> " + requestItems);

            FlightStreamResponse.DepartureFlight departureFlight = flightSearchStreamByNameSkipAirline(
                    requestItems.stream().toList(), "OW", "", loginAccessToken,
                    skip, currency, deviceId
            );

            if (departureFlight == null) {
                Assertion.skip("No flight found (skipAirline='" + skip + "') on "
                        + originStation + "-" + destinationStation + " " + departureDate + ".");
                return null;
            }
            logger.log("Selected flight airline -> " + departureFlight.airlineName
                    + " (code=" + departureFlight.marketingAirlineCode + ")");

            String flightId = departureFlight.flightId;
            logger.log("Flight ID----->" + flightId);
            String supplierID = departureFlight.fareDetail.fares.get(0).supplierId;
            logger.log("Supplier ID----->" + supplierID);

            FlightCartApi.Inputs flightCartApiInputs = new FlightCartApi.Inputs(flightId, supplierID, loginAccessToken);
            String cartId = flightCartApi(flightCartApiInputs, null);
            logger.log("Cart ID---->" + cartId);
            String phoneNumber = loginDataUnmResponse.accountPhoneNumber;

            FlightBookingApiDomestic.Inputs flightBookingApiInputs = new FlightBookingApiDomestic.Inputs(cartId, username, loginAccessToken, passengerNames, phoneNumber, adult);
            FlightBookingDetailDomestic flightBookingDetailDomestic = flightBookingApiDomestic(flightBookingApiInputs);
            log("Flight Booking API response->" + flightBookingDetailDomestic);
            long orderId = flightBookingDetailDomestic.data.successData.orderId;
            String orderHash = flightBookingDetailDomestic.data.successData.orderHash;

            logger.log("orderId->" + orderId);
            logger.log("orderHash->" + orderHash);

            return String.valueOf(orderId);
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Flight Booking via API failed. Skipping the subsequent steps.");
        }
        return null;
    }

    /**
     * Streaming flight search that returns the first {@link FlightStreamResponse.DepartureFlight}
     * whose airline name is NOT equal (case-insensitive) to {@code skipAirline}. When
     * {@code skipAirline} is blank the first non-null flight with a resolvable airline name is
     * returned.
     *
     * <p>Uses the same streaming API, retry loop and airline-code-to-name mapping as
     * {@link #flightSearchStreamByNameDynamicData}, but replaces the match logic with a simple
     * "first non-skipped" selection. Returns {@code null} if nothing is found across all retries.</p>
     */
    private FlightStreamResponse.DepartureFlight flightSearchStreamByNameSkipAirline(
            List<String> requestItems,
            String streamType,
            String returnDate,
            String authorization,
            String skipAirline,
            String currency,
            String deviceId
    ) throws ExecutionException, InterruptedException {

        String skip = skipAirline == null ? "" : skipAirline.trim();
        logger.log("=== FLIGHT SEARCH (skip airline) === skipAirline='" + skip + "'");

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            logger.log("Attempt " + attempt + " to stream flight search (skip airline)");

            FlightStreamingApi.Inputs inputs = new FlightStreamingApi.Inputs(requestItems, authorization, streamType, returnDate, currency, deviceId);
            FlightStreamingApi api = new FlightStreamingApi(inputs);
            ApiResponse response = api.hitApi(apiTools);

            FlightStreamResponse flightStreamResponse = new Gson().fromJson(response.getBody(), FlightStreamResponse.class);

            if (flightStreamResponse != null &&
                    "SUCCESS".equals(flightStreamResponse.code) &&
                    flightStreamResponse.data != null &&
                    flightStreamResponse.data.searchList != null) {

                List<FlightStreamResponse.DepartureFlight> flights = flightStreamResponse.data.searchList.departureFlights;
                Map<String, String> airlineMap = flightStreamResponse.data.airlines != null
                        ? getAirlineCodeToNameMapStream(flightStreamResponse.data.airlines)
                        : new HashMap<>();

                logger.log("Processing " + flights.size() + " flights (skip airline='" + skip + "')");

                for (FlightStreamResponse.DepartureFlight flight : flights) {
                    if (flight == null) continue;
                    if (flight.marketingAirlineCode == null) continue;
                    if (!airlineMap.containsKey(flight.marketingAirlineCode)) continue;

                    String actualAirlineName = airlineMap.get(flight.marketingAirlineCode);

                    if (!skip.isEmpty() && matchAirline(actualAirlineName, skip)) {
                        logger.log("✗ Skip flight " + flight.flightId + " (airline '" + actualAirlineName + "' matches skipAirline)");
                        continue;
                    }

                    flight.airlineName = actualAirlineName;
                    logger.log("✓ Picked flight " + flight.flightId + " with airline '" + actualAirlineName + "'");
                    return flight;
                }
                logger.log("No non-skipped flight in this batch, retrying...");
            }

            if (flightStreamResponse != null && flightStreamResponse.data != null &&
                    flightStreamResponse.data.requestItems != null && !flightStreamResponse.data.requestItems.isEmpty()) {
                requestItems = flightStreamResponse.data.requestItems;
            }

            Thread.sleep(RETRY_DELAY_MS);
        }

        logger.log("⚠ No non-skipped flight found after " + MAX_RETRIES + " attempts. Returning null.");
        return null;
    }

    /**
     * Variant of {@link #createWaitForPaymentFlightB2CDomestic} that accepts a list of candidate
     * airlines and picks the first flight whose airline matches <strong>any</strong> of them.
     *
     * <p>The list is treated as an ordered preference — when the streamed search hits a flight
     * whose airline (case-insensitive, trimmed) appears anywhere in {@code airlines}, that flight
     * is selected and used to create the wait-for-payment order. "First found" is determined by
     * the order in which the stream API returns flights, not by the order of the input list.</p>
     *
     * <p>If {@code airlines} is {@code null} or empty, the first flight returned by the search
     * is used (behaves like the base method with no airline filter).</p>
     *
     * @param airlines candidate airline names (e.g. {@code List.of("Lion Air", "Batik Air",
     *                 "Super Air Jet", "Wings Air")}); entries are matched case-insensitively
     * @return the booked order ID, or {@code null} when no candidate airline has any available
     *         flight on the requested route / date, or the booking fails — in which case
     *         {@link Assertion#skip(String)} is invoked to halt the test gracefully
     */
    @SneakyThrows
    public String createWaitForPaymentFlightB2CDomesticFromAirlines(
            String username,
            String password,
            String originStation,
            String destinationStation,
            String departureDate,
            String searchType,
            int adult,
            int child,
            int infant,
            List<String> airlines,
            String firstName,
            String lastName
    ) {
        try {
            String[] passengerNames = {firstName + " " + lastName};
            Unm<T> unm = new Unm<>(session);
            LoginDataUnmResponse.Data loginDataUnmResponse = unm.unmLoginApi(username, password);
            String loginAccessToken = loginDataUnmResponse.accessToken;
            logger.log("accessToken for dashboard request->" + loginAccessToken);

            String deviceId = CoreFunctions.generateDeviceId();
            logger.log("deviceId for request->" + deviceId);

            String currency = "IDR";
            List<String> candidates = normalizeAirlineCandidates(airlines);
            logger.log("Candidate airlines -> " + candidates);

            FlightSearchApi.Inputs flightSearchApiInputs = new FlightSearchApi.Inputs(
                    originStation.toUpperCase(), destinationStation.toUpperCase(),
                    selectOriginAndDestinationType(originStation),
                    selectOriginAndDestinationType(destinationStation),
                    departureDate, adult, child, infant,
                    searchType, loginAccessToken, currency, deviceId
            );

            ArrayList<String> requestItems = flightSearchGetRequestItems(flightSearchApiInputs);
            if (requestItems == null || requestItems.isEmpty()) {
                Assertion.skip("No request items from flight search for " + originStation + "-" + destinationStation
                        + " on " + departureDate + ".");
                return null;
            }
            logger.log("requestItems -> " + requestItems);

            FlightStreamResponse.DepartureFlight departureFlight = flightSearchStreamByNameFromAirlines(
                    requestItems.stream().toList(), "OW", "", loginAccessToken,
                    candidates, currency, deviceId
            );

            if (departureFlight == null) {
                Assertion.skip("No flight found for any of the candidate airlines " + candidates
                        + " on " + originStation + "-" + destinationStation + " " + departureDate + ".");
                return null;
            }
            logger.log("Selected flight airline -> " + departureFlight.airlineName
                    + " (code=" + departureFlight.marketingAirlineCode + ")");

            String flightId = departureFlight.flightId;
            logger.log("Flight ID----->" + flightId);
            String supplierID = departureFlight.fareDetail.fares.get(0).supplierId;
            logger.log("Supplier ID----->" + supplierID);

            FlightCartApi.Inputs flightCartApiInputs = new FlightCartApi.Inputs(flightId, supplierID, loginAccessToken);
            String cartId = flightCartApi(flightCartApiInputs, null);
            logger.log("Cart ID---->" + cartId);
            String phoneNumber = loginDataUnmResponse.accountPhoneNumber;

            FlightBookingApiDomestic.Inputs flightBookingApiInputs = new FlightBookingApiDomestic.Inputs(cartId, username, loginAccessToken, passengerNames, phoneNumber, adult);
            FlightBookingDetailDomestic flightBookingDetailDomestic = flightBookingApiDomestic(flightBookingApiInputs);
            log("Flight Booking API response->" + flightBookingDetailDomestic);
            long orderId = flightBookingDetailDomestic.data.successData.orderId;
            String orderHash = flightBookingDetailDomestic.data.successData.orderHash;

            logger.log("orderId->" + orderId);
            logger.log("orderHash->" + orderHash);

            return String.valueOf(orderId);
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Flight Booking via API failed. Skipping the subsequent steps.");
        }
        return null;
    }

    /**
     * Streaming flight search that returns the first {@link FlightStreamResponse.DepartureFlight}
     * whose airline name matches any entry in {@code candidateAirlines} (case-insensitive,
     * trimmed). When {@code candidateAirlines} is empty, returns the first non-null flight with
     * a resolvable airline name.
     *
     * <p>Uses the same streaming API, retry loop and airline-code-to-name mapping as
     * {@link #flightSearchStreamByNameDynamicData}, but replaces the match logic with a
     * "first match in list" selection. Returns {@code null} if nothing is found across all retries.</p>
     */
    private FlightStreamResponse.DepartureFlight flightSearchStreamByNameFromAirlines(
            List<String> requestItems,
            String streamType,
            String returnDate,
            String authorization,
            List<String> candidateAirlines,
            String currency,
            String deviceId
    ) throws ExecutionException, InterruptedException {

        List<String> candidates = candidateAirlines == null ? Collections.emptyList() : candidateAirlines;
        logger.log("=== FLIGHT SEARCH (from airline list) === candidates=" + candidates);

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            logger.log("Attempt " + attempt + " to stream flight search (from airline list)");

            FlightStreamingApi.Inputs inputs = new FlightStreamingApi.Inputs(requestItems, authorization, streamType, returnDate, currency, deviceId);
            FlightStreamingApi api = new FlightStreamingApi(inputs);
            ApiResponse response = api.hitApi(apiTools);

            FlightStreamResponse flightStreamResponse = new Gson().fromJson(response.getBody(), FlightStreamResponse.class);

            if (flightStreamResponse != null &&
                    "SUCCESS".equals(flightStreamResponse.code) &&
                    flightStreamResponse.data != null &&
                    flightStreamResponse.data.searchList != null) {

                List<FlightStreamResponse.DepartureFlight> flights = flightStreamResponse.data.searchList.departureFlights != null
                        ? flightStreamResponse.data.searchList.departureFlights
                        : Collections.emptyList();
                Map<String, String> airlineMap = flightStreamResponse.data.airlines != null
                        ? getAirlineCodeToNameMapStream(flightStreamResponse.data.airlines)
                        : new HashMap<>();

                logger.log("Processing " + flights.size() + " flights against candidates " + candidates);

                for (FlightStreamResponse.DepartureFlight flight : flights) {
                    if (flight == null) continue;
                    if (flight.marketingAirlineCode == null) continue;
                    if (!airlineMap.containsKey(flight.marketingAirlineCode)) continue;

                    String actualAirlineName = airlineMap.get(flight.marketingAirlineCode);

                    if (!candidates.isEmpty()) {
                        boolean matched = false;
                        for (String candidate : candidates) {
                            if (matchAirline(actualAirlineName, candidate)) {
                                matched = true;
                                break;
                            }
                        }
                        if (!matched) {
                            logger.log("✗ Skip flight " + flight.flightId + " (airline '" + actualAirlineName
                                    + "' not in candidate list)");
                            continue;
                        }
                    }

                    flight.airlineName = actualAirlineName;
                    logger.log("✓ Picked flight " + flight.flightId + " with airline '" + actualAirlineName + "'");
                    return flight;
                }
                logger.log("No candidate-airline flight in this batch, retrying...");
            }

            if (flightStreamResponse != null && flightStreamResponse.data != null &&
                    flightStreamResponse.data.requestItems != null && !flightStreamResponse.data.requestItems.isEmpty()) {
                requestItems = flightStreamResponse.data.requestItems;
            }

            Thread.sleep(RETRY_DELAY_MS);
        }

        logger.log("⚠ No candidate-airline flight found after " + MAX_RETRIES + " attempts. Returning null.");
        return null;
    }

    /**
     * Normalizes a candidate-airline list: drops nulls / blanks and trims each entry. Returns an
     * empty list (never {@code null}) when the input is {@code null} or contains no usable entry.
     */
    private List<String> normalizeAirlineCandidates(List<String> airlines) {
        if (airlines == null || airlines.isEmpty()) return Collections.emptyList();
        List<String> out = new ArrayList<>();
        for (String a : airlines) {
            if (a == null) continue;
            String t = a.trim();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }

    /**
     * Whether both ends of the route are Indonesian, which is what the platform books as domestic.
     * A country it cannot read is treated as international, the shape that carries more.
     */
    private boolean isDomesticRoute(String accessToken, String origin, String destination) {
        String originCountry = countryCodeOf(accessToken, origin);
        String destinationCountry = countryCodeOf(accessToken, destination);
        boolean domestic = "ID".equalsIgnoreCase(originCountry) && "ID".equalsIgnoreCase(destinationCountry);
        logger.log("Route " + origin + " (" + originCountry + ") -> " + destination + " ("
                + destinationCountry + ") is booked as " + (domestic ? "domestic" : "international"));
        return domestic;
    }

    private String countryCodeOf(String accessToken, String airportCode) {
        FlightAutocompleteResponse.Airport airport = getAirportData(accessToken, airportCode);
        return airport == null ? null : airport.countryCode;
    }

    private FlightBookingDetailDomestic flightBookingApiDomestic(FlightBookingApiDomestic.Inputs inputs) throws ExecutionException, InterruptedException {
        FlightBookingApiDomestic flightBookingApi = new FlightBookingApiDomestic(inputs);
        ApiResponse flightBookingResponse = flightBookingApi.hitApi(apiTools);
        Gson gson = new Gson();
        FlightBookingDetailDomestic flightBookingDetailDomestic = gson.fromJson(flightBookingResponse.getBody(), FlightBookingDetailDomestic.class);
        logger.log("Flight Booking API response->" + flightBookingResponse.getBody());

        return flightBookingDetailDomestic;
    }

    private FlightBookingDetailInternational flightBookingApiInternational(FlightBookingApiInternational.Inputs inputs) throws ExecutionException, InterruptedException {
        FlightBookingApiInternational flightBookingApi = new FlightBookingApiInternational(inputs);
        ApiResponse flightBookingResponse = flightBookingApi.hitApi(apiTools);
        Gson gson = new Gson();
        FlightBookingDetailInternational flightBookingDetailInternational = gson.fromJson(flightBookingResponse.getBody(), FlightBookingDetailInternational.class);
        logger.log("Flight Booking API response->" + flightBookingResponse.getBody());

        return flightBookingDetailInternational;
    }

    /**
     * Maps user-friendly tag descriptions to internal filter keys used by the flight system.
     * <p>
     * The following mappings are supported:
     * <pre>
     * "free reschedule"       → "labelTags/FLEXI"
     * "discounted price"      → "tags/DISCOUNTED_PRICE"
     * "round-trip deals"      → "tags/SMART_TRIP"
     * "diamond"               → "tags/LOYALTY_DIAMOND"
     * "platinum"              → "tags/LOYALTY_PLATINUM"
     * "100% refund" / "100%"  → "tags/100_REFUND"
     * "free airport transfer" → "tags/AIRPORT_TRANSFER"
     * "free antigen test"     → "tags/FREE_ANTIGEN"
     * "meal"                  → "addons/meal"
     * "baggage"               → "addons/baggage"
     * "seat"                  → "addons/seat"
     * </pre>
     *
     * If the input does not match a predefined value (case-insensitive), the original input is returned unchanged.
     *
     * @param data the user-facing tag description (case-insensitive)
     * @return the corresponding internal filter tag, or the original string if no match is found
     */
    private String getCorrectTagForFilter(String data) {
        if (data == null) return "";
        return switch (data.toLowerCase(Locale.ROOT)) {
            case "free reschedule" -> "labelTags/FLEXI";
            case "discounted price" -> "tags/DISCOUNTED_PRICE";
            case "round-trip deals" -> "tags/SMART_TRIP";
            case "diamond" -> "tags/LOYALTY_DIAMOND";
            case "platinum" -> "tags/LOYALTY_PLATINUM";
            case "100% refund", "100%" -> "tags/100_REFUND";
            case "free airport transfer" -> "tags/AIRPORT_TRANSFER";
            case "free antigen test" -> "tags/FREE_ANTIGEN";
            case "meal" -> "addons/meal";
            case "baggage" -> "addons/baggage";
            case "seat" -> "addons/seat";
            case "harga gledek" -> "tags/HARGA_GLEDEK";
            case "promo infinity" -> "promoData/PROMO_INFINITY";
            case "reschedule", "reschedule_yes" -> "labelTags/RESCHEDULE_YES";
            default -> data;
        };
    }

    /**
     * Returns true if {@code type} is one of the four known tag-type namespaces produced by
     * {@link #getCorrectTagForFilter} ({@code tags}, {@code labelTags}, {@code addons},
     * {@code promoData}). Case-insensitive.
     */
    private boolean isKnownTagType(String type) {
        if (type == null) return false;
        String t = type.trim();
        return "tags".equalsIgnoreCase(t)
                || "labelTags".equalsIgnoreCase(t)
                || "addons".equalsIgnoreCase(t)
                || "promoData".equalsIgnoreCase(t);
    }

    /**
     * Strips a single pair of matching outer brackets ({@code [ ]}, {@code ( )}, or {@code { }})
     * from {@code s} and trims the inner content. Returns {@code s} trimmed as-is if no outer
     * bracket pair is present.
     */
    private String stripOuterBrackets(String s) {
        if (s == null) return "";
        String t = s.trim();
        if (t.length() < 2) return t;
        char first = t.charAt(0);
        char last = t.charAt(t.length() - 1);
        if ((first == '[' && last == ']')
                || (first == '(' && last == ')')
                || (first == '{' && last == '}')) {
            return t.substring(1, t.length() - 1).trim();
        }
        return t;
    }

    /**
     * Finds flight dynamic data for one-way flights by validating multiple routes and airlines,
     * including optional airlines and optional origin/destination pairs.
     *
     * @param username            API username (optional).
     * @param password            API password (optional).
     * @param origin              Origin airport code or city.
     * @param destination         Destination airport code or city.
     * @param daysToDepartureDate Days offset from current date to start searching.
     * @param adult               Number of adults.
     * @param child               Number of children.
     * @param infant              Number of infants.
     * @param searchType          Search type (e.g., "OW").
     * @param airline             Primary airline code.
     * @param tag                 Optional flight tag for filtering.
     * @param optionalLocations   Optional alternative origin-destination pairs (delimited string).
     * @param optionalAirline     Optional alternative airlines (delimited string).
     * @return FlightDynamicData  Flight details if found; otherwise null.
     */
    @SneakyThrows
    public FlightDynamicData findDate(
            String username,
            String password,
            String origin,
            String destination,
            int daysToDepartureDate,
            int adult,
            int child,
            int infant,
            String searchType,
            String airline,
            String tag,
            String optionalLocations,
            String optionalAirline,
            String currency
    ) {
        String deviceId = CoreFunctions.generateDeviceId();
        int maxAttempts = 2;
        if(tag.isEmpty() && optionalAirline.isEmpty() && optionalLocations.isEmpty()) {
            maxAttempts = 4;
        }

        Map<String, String> optionalLocationsMap = CoreFunctions.parseDelimitedSectionToMap(optionalLocations);
        Map<String, String> optionalAirlineMap = CoreFunctions.parseDelimitedSectionToMap(optionalAirline);

        //check optional airline
        for(Map.Entry<String, String> entry : optionalAirlineMap.entrySet()) {
            logger.log("Optional airline provided -> " + entry.getValue());
        }

        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);

        logger.log("Departure date provided -> " + departureDate);
        logger.log("Airline provided -> " + airline);

        FlightDynamicData defaultData = new FlightDynamicData(airline, "", origin, destination, departureDate, "", "", "", "", "");

        if (Configs.ENVIRONMENT != Environment.GK && Configs.ENVIRONMENT != Environment.PREPROD) {
            return defaultData;
        }

        String authorization = fetchToken(username, password,deviceId);
        if (authorization == null) {
            Assertion.skip("Skipping due to token API failure.");
            return null;
        }

        searchType = selectTripType(searchType);
        origin = convertToAirportCode(origin);
        destination = convertToAirportCode(destination);

        logBlock("Primary airline with original route");
        FlightDynamicData result = tryValidateDate(origin, destination, departureDate, adult, child, infant, searchType, airline, authorization, tag, maxAttempts,currency,deviceId);
        if (result != null) return result;

        logBlock("Optional airlines with original route");
        for (String optionalAirlineTemp : optionalAirlineMap.values()) {
            result = tryValidateDate(origin, destination, departureDate, adult, child, infant, searchType, optionalAirlineTemp, authorization, tag, maxAttempts,currency,deviceId);
            if (result != null) return result;
        }

        logBlock("Original airline with optional locations");
        for (Map.Entry<String, String> entry : optionalLocationsMap.entrySet()) {
            String optionalOrigin = convertToAirportCode(entry.getKey());
            String optionalDestination = convertToAirportCode(entry.getValue());
            result = tryValidateDate(optionalOrigin, optionalDestination, departureDate, adult, child, infant, searchType, airline, authorization, tag, maxAttempts,currency,deviceId);
            if (result != null) return result;
        }

        logBlock("Optional airlines with optional locations");
        for (Map.Entry<String, String> entry : optionalLocationsMap.entrySet()) {
            String optionalOrigin = convertToAirportCode(entry.getKey());
            String optionalDestination = convertToAirportCode(entry.getValue());
            for (String optionalAirlineTemp : optionalAirlineMap.values()) {
                result = tryValidateDate(optionalOrigin, optionalDestination, departureDate, adult, child, infant, searchType, optionalAirlineTemp, authorization, tag, maxAttempts,currency,deviceId);
                if (result != null) return result;
            }
        }

        Assertion.skip("Skipping flight search due to no valid airline data found after attempts.");
        return null;
    }

    /**
     * One-way streaming flight discovery with no tag, optional location map, or optional airline map (all empty strings).
     * New entry point for callers outside existing {@link #findDate} test usage; delegates to {@link #findDate} without
     * changing its implementation. Unlike {@code findDate} alone, after this call succeeds (or attempts streaming),
     * {@link #getLastOneWayStreamingAirlineCodeToName()} returns airline codes and names from the streaming payload
     * {@code data.airlines}.
     */
    @SneakyThrows
    public FlightDynamicData findDynamicFlightSearchDataOneWayStreaming(
            String username,
            String password,
            String origin,
            String destination,
            int daysToDepartureDate,
            int adult,
            int child,
            int infant,
            String searchType,
            String airline,
            String currency) {
        logger.log("Find dynamic data");
        lastOneWayStreamingAirlineCodeToName = Map.of();
        captureOneWayStreamingAirlineDirectory = true;
        try {
            return findDate(
                    username,
                    password,
                    origin,
                    destination,
                    daysToDepartureDate,
                    adult,
                    child,
                    infant,
                    searchType,
                    airline,
                    "",
                    "",
                    "",
                    currency);
        } finally {
            captureOneWayStreamingAirlineDirectory = false;
        }
    }

    /**
     * Airline directory from the last {@link #findDynamicFlightSearchDataOneWayStreaming} run: IATA-style code →
     * display name (or name / shortName) as returned under {@code data.airlines} in the streaming search response.
     * Empty when that method was not used, streaming did not return airlines, or capture was not active.
     */
    public Map<String, String> getLastOneWayStreamingAirlineCodeToName() {
        return Map.copyOf(lastOneWayStreamingAirlineCodeToName);
    }

    /**
     * Matches any IPv4-like sequence. Used only against the URL's host part
     * (the server we're talking to), not path or query.
     */
    private static final Pattern MOCK_SERVER_IP_LIKE = Pattern.compile(
            "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"
    );

    private boolean isMockIssuedSupplier(String supplierUrl) {
        if (supplierUrl == null || supplierUrl.isEmpty()) return false;

        String url = supplierUrl.toLowerCase();
        logger.log("====== Mock-Issued Supplier Validation ======");
        logger.log("URL: " + supplierUrl);

        boolean hasMockServerIp = false;
        try {
            String host = URI.create(supplierUrl).getHost();
            if (host != null && !host.isEmpty()) {
                hasMockServerIp = MOCK_SERVER_IP_LIKE.matcher(host).matches();
            }
        } catch (Exception e) {
            logger.log("Check 1 - Could not parse URL host (malformed or no host): " + e.getMessage());
        }
        logger.log("Check 1 - IP in host part (mock server): " + hasMockServerIp);
        if (hasMockServerIp) {
            logger.log("Mock server IP detected — instant pass");
            logger.log("==============================================");
            return true;
        }

        boolean hasKrakend = url.contains("krakend");
        logger.log("Check 2 - Contains 'krakend' (informational): " + hasKrakend);

        String issuedMarker = "issued";
        int issuedIndex = url.indexOf(issuedMarker);
        String beyondIssued = (issuedIndex >= 0 && issuedIndex + issuedMarker.length() < url.length())
                ? url.substring(issuedIndex + issuedMarker.length()).trim()
                : null;
        boolean hasMockIssuedSuffix = beyondIssued != null && !beyondIssued.isEmpty();
        logger.log("Check 3 - URL has content beyond 'issued' (not null): " + hasMockIssuedSuffix);

        logger.log("Final result: " + hasMockIssuedSuffix);
        logger.log("==============================================");

        return hasMockIssuedSuffix;
    }


    /**
     * Uses dynamic-supplier-mapping + integrator-config. Missing integrator URL: try next supplier; if none have a URL, test still continues (no skip).
     * URL present and mock-issued: continue. URL present and not mock-issued: skip the test.
     *
     * @param airlineCode (e.g., "JQ" for Jetstar)
     * @param origin      origin airport code (e.g., "CGK")
     * @param destination destination airport code (e.g., "DPS")
     */
    public void validateAllSuppliersAreMockIssuedByAirline(String airlineCode, String origin, String destination) {
        logger.log("====== Validating all suppliers are mock-issued for airline: " + airlineCode + " (" + origin + " -> " + destination + ") ======");
        if (airlineCode == null || airlineCode.trim().isEmpty()) {
            Assertion.skip("Skipping: airline code is null or blank, cannot validate mock-issued suppliers.");
            return;
        }
        if (origin == null || origin.trim().isEmpty() || destination == null || destination.trim().isEmpty()) {
            Assertion.skip("Skipping: origin or destination is null or blank, cannot validate mock-issued suppliers.");
            return;
        }

        try {
            List<String> originsToValidate = resolveOriginsToValidate(origin);
            if (originsToValidate.isEmpty()) {
                Assertion.skip("Skipping: no airport codes to validate for origin " + origin);
                return;
            }

            for (String resolvedOrigin : originsToValidate) {
                logger.log("------ Validating origin: " + resolvedOrigin + " ------");
                boolean shouldContinue = validateSuppliersMockIssuedForOneOrigin(airlineCode, resolvedOrigin, destination);
                if (!shouldContinue) {
                    return;
                }
            }

            logger.log("All origin(s) " + originsToValidate + " passed mock-issued validation. Proceeding with test.");
        } catch (SkipException e) {
            throw e;
        } catch (Exception e) {
            logger.log("Exception during airline supplier validation: " + e.getLocalizedMessage());
            Assertion.skip("Skipping: exception during airline supplier mock-issued check: " + e.getLocalizedMessage());
        }
    }

    /**
     * Non-skipping variant of {@link #validateAllSuppliersAreMockIssuedByAirline}.
     * <p>
     * Intended for payment and other gates: returns {@code true} only when suppliers can be
     * checked and are mock-issued. Missing supplier-mapping data, unresolved origins, or empty
     * supplier codes yield {@code false} so tests do not treat unknown mapping as safe to pay.
     * Discovery flows call the silent helper with {@code lenientWhenUnmapped = true} instead.
     *
     * <p>On {@link Environment#PROD} the supplier-mapping API is <strong>not</strong> hit —
     * the method short-circuits to {@code true} so callers don't need to gate the call behind
     * an env check. This preserves the pre-existing PROD behavior (no extra supplier API
     * traffic) while still letting non-PROD payment gates rely on a truthful result.</p>
     *
     * @param airlineCode airline code (e.g., "JQ")
     * @param origin      origin airport / city code (e.g., "CGK" or "JKTC")
     * @param destination destination airport code (e.g., "DPS")
     * @return {@code true} iff mapping exists and every integrator-mapped supplier is mock-issued
     *         (unknown data counts as not confirmed). Always {@code true} on {@link Environment#PROD}.
     */
    public boolean areAllSuppliersMockIssuedByAirline(String airlineCode, String origin, String destination) {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            logger.log("Skipping mock-issued supplier check on PROD for airline " + airlineCode
                    + " (" + origin + " -> " + destination + "). Returning true (no API call).");
            return true;
        }
        logger.log("====== Checking (non-skipping) all suppliers mock-issued for airline: "
                + airlineCode + " (" + origin + " -> " + destination + ") ======");
        boolean result = areAllSuppliersMockIssuedSilent(airlineCode, origin, destination, false);
        logger.log("Mock-issued check result for airline " + airlineCode + " ("
                + origin + " -> " + destination + "): " + result);
        return result;
    }

    /**
     * If origin is JKTC (city code), calls airport API to get all airport codes for that city;
     * otherwise returns a list containing the single origin.
     */
    private List<String> resolveOriginsToValidate(String origin) {
        String originNorm = origin.trim().toUpperCase();
        if (!"JKTC".equals(originNorm)) {
            return Collections.singletonList(origin.trim());
        }

        logger.log("Origin is JKTC — calling airport API to resolve airport codes for city");
        AirportByCityCodeApi.Inputs inputs = new AirportByCityCodeApi.Inputs("JKTC");
        AirportByCityCodeApi api = new AirportByCityCodeApi(inputs);
        ApiResponse apiResponse = api.hitApi(apiTools);

        Gson gson = new Gson();
        AirportByCityCodeResponse response = gson.fromJson(
                apiResponse.getBody(), AirportByCityCodeResponse.class
        );

        if (response == null || response.data == null || response.data.content == null || response.data.content.isEmpty()) {
            logger.log("Airport API returned no airports for cityCode=JKTC");
            return Collections.emptyList();
        }

        List<String> codes = new ArrayList<>();
        for (AirportByCityCodeResponse.Airport airport : response.data.content) {
            if (airport.code != null && !airport.code.trim().isEmpty()) {
                codes.add(airport.code.trim());
            }
        }
        logger.log("Resolved JKTC to airport codes: " + codes);
        return codes;
    }

    /**
     * No integrator URL per supplier: next supplier. If every supplier has no URL, return true (test continues).
     * Has URL and not mock-issued: skip test. Has URL and mock-issued: next supplier, then success log if any URL was checked.
     */
    private boolean validateSuppliersMockIssuedForOneOrigin(String airlineCode, String origin, String destination) {
        DynamicSupplierMappingByAirlineApi.Inputs inputs =
                new DynamicSupplierMappingByAirlineApi.Inputs(airlineCode, origin, destination);
        DynamicSupplierMappingByAirlineApi api = new DynamicSupplierMappingByAirlineApi(inputs);
        ApiResponse apiResponse = api.hitApi(apiTools);

        Gson gson = new Gson();
        DynamicSupplierMappingByAirlineResponse response = gson.fromJson(
                apiResponse.getBody(), DynamicSupplierMappingByAirlineResponse.class
        );

        if (response == null || response.data == null || response.data.content == null || response.data.content.isEmpty()) {
            logger.log("No supplier mappings for airline " + airlineCode + " origin " + origin + ", moving to next.");
            return true;
        }

        Set<String> uniqueSupplierCodes = new LinkedHashSet<>();
        for (DynamicSupplierMappingByAirlineResponse.SupplierMapping mapping : response.data.content) {
            if (mapping.supplier != null && mapping.supplier.code != null && !mapping.supplier.code.isEmpty()) {
                uniqueSupplierCodes.add(mapping.supplier.code);
            }
        }

        logger.log("Found " + uniqueSupplierCodes.size() + " unique supplier(s) for " + origin + ": " + uniqueSupplierCodes);

        if (uniqueSupplierCodes.isEmpty()) {
            logger.log("No valid supplier codes for airline " + airlineCode + " origin " + origin + ", moving to next.");
            return true;
        }

        int withIntegratorUrl = 0;

        for (String supplierCode : uniqueSupplierCodes) {
            logger.log("--- Checking supplier: " + supplierCode + " ---");
            String supplierUrl = fetchIntegratorConfigUrl(supplierCode);

            if (supplierUrl == null || supplierUrl.isEmpty()) {
                logger.log("Integrator config: no URL mapped for supplier '" + supplierCode + "' - checking next supplier.");
                continue;
            }

            withIntegratorUrl++;
            logger.log("Supplier URL: " + supplierUrl);

            if (!isMockIssuedSupplier(supplierUrl)) {
                Assertion.skip("Skipping: supplier '" + supplierCode + "' is NOT mock-issued. URL: " + supplierUrl);
                return false;
            }

            logger.log("Supplier '" + supplierCode + "' is mock-issued.");
        }

        if (withIntegratorUrl == 0) {
            logger.log("No integrator URL for any of " + uniqueSupplierCodes.size()
                    + " supplier code(s) for origin " + origin + "; continuing test.");
            return true;
        }

        logger.log("All " + withIntegratorUrl + " integrator-mapped supplier(s) for origin " + origin + " are mock-issued.");
        return true;
    }

    /**
     * Decodes an encoded supplier id (the {@code ...-ENC} form returned by search/streaming) into the
     * supplier code the dashboard APIs use.
     *
     * @return the decoded code, or the input unchanged when it cannot be decoded
     */
    public String decodeSupplierId(String encodedSupplierId) {
        if (isBlankString(encodedSupplierId)) {
            return encodedSupplierId;
        }
        String toDecode = encodedSupplierId.trim();
        if (toDecode.toUpperCase().endsWith(ENCODED_SUPPLIER_SUFFIX)) {
            toDecode = toDecode.substring(0, toDecode.length() - ENCODED_SUPPLIER_SUFFIX.length());
        }
        try {
            ApiResponse response = new FlightDecryptApi(new FlightDecryptApi.Inputs(toDecode)).hitApi(apiTools);
            String decoded = new JSONObject(response.getBody()).optString("data", null);
            if (decoded == null || decoded.isBlank()) {
                logger.log("base64/decode returned no data for supplierId " + encodedSupplierId);
                return encodedSupplierId;
            }
            logger.log("Decoded supplierId " + encodedSupplierId + " (sent " + toDecode + ") -> " + decoded);
            return decoded.trim();
        } catch (Exception e) {
            logger.log("Failed to decode supplierId " + encodedSupplierId + ": " + e.getLocalizedMessage());
            return encodedSupplierId;
        }
    }

    /**
     * Checks whether one <strong>known</strong> supplier is mock-issued, i.e. its ISSUED integrator URL
     * points at the internal mock rather than a real airline integrator.
     *
     * <p>Use this when the serving supplier is already known (multi-city search/streaming returns it per
     * journey). It is narrower and cheaper than
     * {@link #areAllSuppliersMockIssuedByAirline(String, String, String)}, which has to validate every
     * supplier mapped to an airline because it cannot tell which one will serve.</p>
     *
     * <p>Always {@code true} on {@link Environment#PROD} (no API call), matching the airline-based check.
     * A supplier with no integrator URL is treated as mock-issued — it is not evidence of real issuance.</p>
     *
     * @param encodedSupplierId supplier id as returned by search/streaming (the {@code ...-ENC} form)
     */
    public boolean isSupplierMockIssued(String encodedSupplierId) {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            logger.log("Skipping mock-issued supplier check on PROD for " + encodedSupplierId + " (returning true).");
            return true;
        }
        String supplierUrl = fetchIntegratorConfigUrl(decodeSupplierId(encodedSupplierId));
        if (supplierUrl == null || supplierUrl.isEmpty()) {
            logger.log("No integrator URL for supplier " + encodedSupplierId + "; not classified as real issued.");
            return true;
        }
        boolean mockIssued = isMockIssuedSupplier(supplierUrl);
        logger.log("Supplier " + encodedSupplierId + " mock-issued=" + mockIssued + " (url=" + supplierUrl + ")");
        return mockIssued;
    }

    private String fetchIntegratorConfigUrl(String decodedSupplierCode) {
        try {
            FlightIntegratorConfigApi.Inputs inputs = new FlightIntegratorConfigApi.Inputs(decodedSupplierCode, "1");
            FlightIntegratorConfigApi configApi = new FlightIntegratorConfigApi(inputs);
            ApiResponse response = configApi.hitApi(apiTools);

            IntegratorConfigResponse configResponse = new Gson().fromJson(
                    response.getBody(), IntegratorConfigResponse.class
            );
            if (configResponse != null
                    && "SUCCESS".equals(configResponse.code)
                    && configResponse.data != null
                    && configResponse.data.content != null
                    && !configResponse.data.content.isEmpty()) {
                return configResponse.data.content.get(0).url;
            }
            logger.log("Integrator config returned no content. Response: " + response.getBody());
            return null;
        } catch (Exception e) {
            logger.log("Exception in fetchIntegratorConfigUrl: " + e.getLocalizedMessage());
            return null;
        }
    }

    public String extractEncodedSupplierIdFromPrpFlightDetail(String body) {
        if (body == null || body.isBlank()) {
            return "";
        }
        try {
            JSONArray details = new JSONObject(body).optJSONArray("details");
            if (details != null && details.length() > 0) {
                return details.getJSONObject(0).optString("supplierId");
            }
        } catch (Exception e) {
            logger.log("Failed to parse prp/flightDetail body: " + e.getMessage());
        }
        Matcher matcher = Pattern.compile("\"supplierId\"\\s*:\\s*\"([^\"]+)\"").matcher(body);
        return matcher.find() ? matcher.group(1) : "";
    }

    public String decodePrpSupplierId(String encodedSupplierId) {
        if (encodedSupplierId == null || encodedSupplierId.isBlank()) {
            return "";
        }
        String decoded = decryptSupplierCode(encodedSupplierId);
        logger.log("Decoded PRP supplierId: " + decoded);
        if (decoded.isBlank()) {
            return "";
        }
        if (!decoded.trim().startsWith("{") && !decoded.trim().startsWith("[")) {
            return firstSupplierCode(decoded.trim());
        }
        String nested = extractSupplierValue(decoded);
        return firstSupplierCode(nested.isBlank() ? decoded.trim() : nested);
    }

    private String firstSupplierCode(String supplierCode) {
        int pipe = supplierCode.indexOf('|');
        return pipe < 0 ? supplierCode : supplierCode.substring(0, pipe).trim();
    }

    private String extractSupplierValue(String decodedJson) {
        try {
            JSONObject json = new JSONObject(decodedJson);
            String direct = json.optString("supplier");
            if (direct.isBlank()) {
                direct = json.optString("supplierCode");
            }
            if (direct.isBlank()) {
                direct = json.optString("supplierId");
            }
            if (!direct.isBlank()) {
                return direct;
            }
            JSONObject fareDetail = json.optJSONObject("fareDetail");
            JSONArray fares = fareDetail == null ? json.optJSONArray("fares") : fareDetail.optJSONArray("fares");
            if (fares != null && fares.length() > 0) {
                return fares.getJSONObject(0).optString("supplierId");
            }
        } catch (Exception e) {
            logger.log("Failed to extract nested supplier from decode payload: " + e.getMessage());
        }
        return "";
    }

    private String decryptSupplierCode(String encryptedSupplierId) {
        if (isBlankString(encryptedSupplierId)) {
            return "";
        }
        ApiResponse response = new FlightDecryptApi(
                new FlightDecryptApi.Inputs(encryptedSupplierId.replaceAll("-ENC$", ""))).hitApi(apiTools);
        return new JSONObject(response.getBody()).optString("data", "");
    }

    /**
     * Looks up the integrator-config-v2 row for {@code action} and {@code supplierCode}, then rewrites its URL.
     * Pass {@code ""} to restore the original URL, or a suffix to break it.
     */
    public boolean updateBookingIntegratorUrl(String urlSuffix, String action, String supplierCode) {
        if (isBlankString(action) || isBlankString(supplierCode)) {
            logger.log("Missing required params for integrator-config-v2 update: action='" + action
                    + "' supplierCode='" + supplierCode + "'");
            return false;
        }
        String cookieHeader = new MemberApi<>(session).getAdminDashboardCookieHeader();
        logger.log("Integrator-config-v2 lookup action='" + action + "' supplierCode='" + supplierCode + "'");
        JSONObject config = getBookingIntegratorConfig(cookieHeader, action, supplierCode);
        if (config == null) {
            return false;
        }
        String configId = config.optString("id");
        String distributionType = config.optString("distributionType");
        String bookingUrl = config.optString("url").replace("-MOCKFAILED", "");
        if (configId.isBlank() || distributionType.isBlank() || bookingUrl.isBlank()) {
            return false;
        }
        PutIntegratorConfigV2Api api = new PutIntegratorConfigV2Api(
                new PutIntegratorConfigV2Api.Inputs(cookieHeader, urlSuffix, configId, action, distributionType, supplierCode, bookingUrl, ""));
        ApiResponse response = api.hitApi(apiTools);
        logger.log("Put integrator-config-v2 id='" + configId + "' urlSuffix='" + urlSuffix + "' response: " + response.getBody());
        JSONObject parsed = new JSONObject(response.getBody());
        return "SUCCESS".equalsIgnoreCase(parsed.optString("code"));
    }

    private JSONObject getBookingIntegratorConfig(String cookieHeader, String action, String supplierCode) {

        GetIntegratorConfigV2Api getApi = new GetIntegratorConfigV2Api(
                new GetIntegratorConfigV2Api.Inputs(cookieHeader, action, "", supplierCode));
        ApiResponse getResponse = getApi.hitApi(apiTools);
        logger.log("Get integrator-config-v2 response: " + getResponse.getBody());
        JSONObject parsed = new JSONObject(getResponse.getBody());
        JSONObject data = parsed.optJSONObject("data");
        JSONArray content = data == null ? null : data.optJSONArray("content");
        JSONObject config = (content != null && !content.isEmpty()) ? content.getJSONObject(0) : null;
        String configId = config == null ? "" : config.optString("id");

        if (!"SUCCESS".equalsIgnoreCase(parsed.optString("code")) || configId.isBlank()) {
            logger.log("Integrator config-v2 GET returned no BOOKING id");
            return null;
        }
        logger.log("Integrator config-v2 BOOKING id: " + configId + " url: " + config.optString("url"));
        return config;
    }


    /**
     * Finds flight dynamic data for round-trip flights by validating multiple date pairs and
     * multiple airline/origin-destination combinations, including optional alternatives.
     *
     * @param origin            Origin airport code or city.
     * @param destination       Destination airport code or city.
     * @param daysToDepartureDate Days offset from current date to start searching.
     * @param duration          Number of days between departure and return flights.
     * @param adult             Number of adults.
     * @param child             Number of children.
     * @param infant            Number of infants.
     * @param searchType        Search type (e.g., "RT").
     * @param airlineDepart     Departure airline code.
     * @param airlineReturn     Return airline code.
     * @param tagDepart               Optional flight tag for filtering.
     * @param tagReturn                Optional flight tag for filtering.
     * @param optionalLocations Optional alternative origin-destination pairs (delimited string).
     * @param optionalAirline   Optional alternative airlines (delimited string).
     */
    public FlightDynamicData findDateRT(
            String username,
            String password,
            String origin,
            String destination,
            int daysToDepartureDate,
            int duration,
            int adult,
            int child,
            int infant,
            String searchType,
            String airlineDepart,
            String airlineReturn,
            String tagDepart,
            String tagReturn,
            String optionalLocations,
            String optionalAirline,
            String currency
    ) {
        String deviceId = CoreFunctions.generateDeviceId();
        int maxAttempts = 2;

        Map<String, String> optionalLocationsMap = CoreFunctions.parseDelimitedSectionToMap(optionalLocations);
        Map<String, String> optionalAirlineMap = CoreFunctions.parseDelimitedSectionToMap(optionalAirline);

        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String returnDate = CoreFunctions.addToDateYYYY_MM_DD(departureDate, duration);

        logger.log("Departure date provided -> " + departureDate);
        logger.log("Return date provided -> " + returnDate);
        logger.log("Airline provided-> " + airlineDepart);

        FlightDynamicData defaultData = new FlightDynamicData(airlineDepart,airlineReturn,origin,destination,departureDate,returnDate,"","","","");

        if (Configs.ENVIRONMENT != Environment.GK && Configs.ENVIRONMENT != Environment.PREPROD) {
            return defaultData;
        }

        String authorization = fetchToken(username, password,deviceId);

        if (authorization == null) {
            Assertion.skip("Skipping due to token API failure.");
            return null;
        }

        searchType = selectTripType(searchType);
        origin = convertToAirportCode(origin);
        destination = convertToAirportCode(destination);

        logBlock("Primary airline with original route");
        FlightDynamicData result= tryValidateDateRT(origin, destination, departureDate, duration,
                adult, child, infant, searchType, airlineDepart, airlineReturn, authorization, tagDepart, tagReturn, maxAttempts,currency,deviceId);
        if (result != null) return result;

        logBlock("Optional airlines with original route");
        for (Map.Entry<String, String> entry : optionalAirlineMap.entrySet()) {
            String optionalAirlineTempDepart = entry.getKey();
            String optionalAirlineTempReturn = entry.getValue();
            result = tryValidateDateRT(origin, destination, departureDate, duration, adult, child, infant,
                    searchType, optionalAirlineTempDepart, optionalAirlineTempReturn, authorization, tagDepart, tagReturn, maxAttempts,currency,deviceId);
            if (result != null) return result;
        }

        logBlock("Optional locations with Original airline");
        for (Map.Entry<String, String> entry : optionalLocationsMap.entrySet()) {
            String optionalOrigin = convertToAirportCode(entry.getKey());
            String optionalDestination = convertToAirportCode(entry.getValue());
            result = tryValidateDateRT(optionalOrigin, optionalDestination, departureDate, duration, adult, child, infant,
                    searchType, airlineDepart, airlineReturn, authorization, tagDepart, tagReturn, maxAttempts,currency,deviceId);
            if (result != null) return result;
        }

        logBlock("Optional locations with optional airlines");
        for (Map.Entry<String, String> entry : optionalLocationsMap.entrySet()) {
            String optionalOrigin = convertToAirportCode(entry.getKey());
            String optionalDestination = convertToAirportCode(entry.getValue());

            for (Map.Entry<String, String> airlineMap : optionalAirlineMap.entrySet()) {
                String optionalAirlineTempDepart = airlineMap.getKey();
                String optionalAirlineTempReturn = airlineMap.getValue();
                result = tryValidateDateRT(optionalOrigin, optionalDestination, departureDate, duration, adult, child, infant,
                        searchType, optionalAirlineTempDepart, optionalAirlineTempReturn, authorization, tagDepart, tagReturn, maxAttempts,currency,deviceId);
                if (result != null) return result;
            }
        }
        Assertion.skip("Skipping flight search due to no valid airline data found after attempts.");
        return null;
    }

    /**
     * Transit-aware variant of {@link #findDateRT}. Accepts every parameter of the base method
     * plus a {@code transit} flag and returns flight dynamic data whose departure and return
     * legs both match the requested transit shape.
     *
     * <ul>
     *   <li>{@code transit = true}  - require flights that have at least one transit stop
     *       (i.e. {@code totalTransit > 0}) on both legs.</li>
     *   <li>{@code transit = false} - require direct flights with no transits
     *       (i.e. {@code totalTransit == 0}) on both legs.</li>
     * </ul>
     *
     * <p>The resulting {@link FlightDynamicData} is populated with the same fields as
     * {@link #findDateRT} and additionally carries {@code totalTransitDepart} and
     * {@code totalTransitReturn} so callers can assert on them.</p>
     *
     * @param transit whether the returned flights must include transit stops
     * @see #findDateRT
     */
    public FlightDynamicData findDateRT(
            String username,
            String password,
            String origin,
            String destination,
            int daysToDepartureDate,
            int duration,
            int adult,
            int child,
            int infant,
            String searchType,
            String airlineDepart,
            String airlineReturn,
            String tagDepart,
            String tagReturn,
            String optionalLocations,
            String optionalAirline,
            String currency,
            boolean transit
    ) {
        String deviceId = CoreFunctions.generateDeviceId();
        int maxAttempts = 2;

        Map<String, String> optionalLocationsMap = CoreFunctions.parseDelimitedSectionToMap(optionalLocations);
        Map<String, String> optionalAirlineMap = CoreFunctions.parseDelimitedSectionToMap(optionalAirline);

        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String returnDate = CoreFunctions.addToDateYYYY_MM_DD(departureDate, duration);

        logger.log("Departure date provided -> " + departureDate);
        logger.log("Return date provided -> " + returnDate);
        logger.log("Airline provided-> " + airlineDepart);
        logger.log("Transit required -> " + transit);

        FlightDynamicData defaultData = new FlightDynamicData(airlineDepart, airlineReturn, origin, destination, departureDate, returnDate, "", "", "", "");

        if (Configs.ENVIRONMENT != Environment.GK && Configs.ENVIRONMENT != Environment.PREPROD) {
            return defaultData;
        }

        String authorization = fetchToken(username, password, deviceId);

        if (authorization == null) {
            Assertion.skip("Skipping due to token API failure.");
            return null;
        }

        searchType = selectTripType(searchType);
        origin = convertToAirportCode(origin);
        destination = convertToAirportCode(destination);

        logBlock("Primary airline with original route (transit=" + transit + ")");
        FlightDynamicData result = tryValidateDateRT(origin, destination, departureDate, duration,
                adult, child, infant, searchType, airlineDepart, airlineReturn, authorization,
                tagDepart, tagReturn, maxAttempts, currency, deviceId, transit);
        if (result != null) return result;

        logBlock("Optional airlines with original route (transit=" + transit + ")");
        for (Map.Entry<String, String> entry : optionalAirlineMap.entrySet()) {
            String optionalAirlineTempDepart = entry.getKey();
            String optionalAirlineTempReturn = entry.getValue();
            result = tryValidateDateRT(origin, destination, departureDate, duration, adult, child, infant,
                    searchType, optionalAirlineTempDepart, optionalAirlineTempReturn, authorization,
                    tagDepart, tagReturn, maxAttempts, currency, deviceId, transit);
            if (result != null) return result;
        }

        logBlock("Optional locations with Original airline (transit=" + transit + ")");
        for (Map.Entry<String, String> entry : optionalLocationsMap.entrySet()) {
            String optionalOrigin = convertToAirportCode(entry.getKey());
            String optionalDestination = convertToAirportCode(entry.getValue());
            result = tryValidateDateRT(optionalOrigin, optionalDestination, departureDate, duration,
                    adult, child, infant, searchType, airlineDepart, airlineReturn, authorization,
                    tagDepart, tagReturn, maxAttempts, currency, deviceId, transit);
            if (result != null) return result;
        }

        logBlock("Optional locations with optional airlines (transit=" + transit + ")");
        for (Map.Entry<String, String> entry : optionalLocationsMap.entrySet()) {
            String optionalOrigin = convertToAirportCode(entry.getKey());
            String optionalDestination = convertToAirportCode(entry.getValue());

            for (Map.Entry<String, String> airlineMap : optionalAirlineMap.entrySet()) {
                String optionalAirlineTempDepart = airlineMap.getKey();
                String optionalAirlineTempReturn = airlineMap.getValue();
                result = tryValidateDateRT(optionalOrigin, optionalDestination, departureDate, duration,
                        adult, child, infant, searchType, optionalAirlineTempDepart, optionalAirlineTempReturn,
                        authorization, tagDepart, tagReturn, maxAttempts, currency, deviceId, transit);
                if (result != null) return result;
            }
        }
        Assertion.skip("Skipping flight search due to no valid airline data matching transit=" + transit + " found after attempts.");
        return null;
    }

    /**
     * Combines {@link #findDateRT} with {@link #validateAllSuppliersAreMockIssuedByAirline}: only
     * returns flight dynamic data for an airline whose suppliers are ALL mock-issued (i.e. not
     * real-issued).
     *
     * <p>{@code airlineDepart} and {@code airlineReturn} are optional:
     * <ul>
     *   <li>When <b>provided</b>, they are validated for mock-issued suppliers first. If any
     *       supplier is real-issued the test is skipped (same contract as
     *       {@link #validateAllSuppliersAreMockIssuedByAirline}).</li>
     *   <li>When <b>blank/null</b>, this method iterates every pair in {@code optionalAirline} and
     *       returns the <b>first</b> airline whose suppliers are all mock-issued and for which a
     *       valid departure/return date pair can be found. Airlines that are real-issued or have
     *       no supplier mappings are silently skipped (no {@code Assertion.skip}) so the loop can
     *       continue to the next candidate.</li>
     * </ul>
     *
     * <p>Route fallbacks ({@code optionalLocations}) still apply once a mock-issued airline is
     * selected, matching the original {@link #findDateRT} fallback order.
     *
     * @param airlineDepart optional departure airline code; if blank, auto-pick first mock-issued
     * @param airlineReturn optional return airline code; defaults to {@code airlineDepart} when
     *                      not provided and {@code airlineDepart} itself was provided
     */
    public FlightDynamicData findDateRTWithMockIssuedAirline(
            String username,
            String password,
            String origin,
            String destination,
            int daysToDepartureDate,
            int duration,
            int adult,
            int child,
            int infant,
            String searchType,
            String airlineDepart,
            String airlineReturn,
            String tagDepart,
            String tagReturn,
            String optionalLocations,
            String optionalAirline,
            String currency
    ) {
        String deviceId = CoreFunctions.generateDeviceId();
        int maxAttempts = 2;

        Map<String, String> optionalLocationsMap = CoreFunctions.parseDelimitedSectionToMap(optionalLocations);
        Map<String, String> optionalAirlineMap = CoreFunctions.parseDelimitedSectionToMap(optionalAirline);

        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        String returnDate = CoreFunctions.addToDateYYYY_MM_DD(departureDate, duration);

        boolean airlineProvided = !isBlankString(airlineDepart);
        logger.log("Departure date provided -> " + departureDate);
        logger.log("Return date provided -> " + returnDate);
        logger.log("Airline provided -> " + (airlineProvided
                ? airlineDepart + "/" + (isBlankString(airlineReturn) ? airlineDepart : airlineReturn)
                : "<none — auto-selecting first mock-issued from optionalAirline>"));

        FlightDynamicData defaultData = new FlightDynamicData(
                airlineDepart, airlineReturn, origin, destination, departureDate, returnDate, "", "", "", "");

        if (Configs.ENVIRONMENT != Environment.GK && Configs.ENVIRONMENT != Environment.PREPROD) {
            return defaultData;
        }

        String authorization = fetchToken(username, password, deviceId);
        if (authorization == null) {
            Assertion.skip("Skipping due to token API failure.");
            return null;
        }

        searchType = selectTripType(searchType);
        String originCode = convertToAirportCode(origin);
        String destinationCode = convertToAirportCode(destination);

        // Build the ordered list of (depart, return) airline pairs to try.
        // - When user provided an explicit airline, that's the only candidate.
        // - Otherwise, iterate optionalAirlineMap in its declared order and auto-pick the first
        //   pair whose suppliers are all mock-issued.
        LinkedHashMap<String, String> airlineCandidates = new LinkedHashMap<>();
        if (airlineProvided) {
            String effectiveReturn = isBlankString(airlineReturn) ? airlineDepart : airlineReturn;
            airlineCandidates.put(airlineDepart, effectiveReturn);
        } else {
            airlineCandidates.putAll(optionalAirlineMap);
        }

        // Pure-discovery path: caller provided neither airlineDepart nor optionalAirline.
        // Run an open SRP search (no airline filter), read back the airline the backend returned
        // for each route, and keep the first one whose suppliers are all mock-issued. This
        // implements the user's "search all airlines and give me the first that is mock-issued"
        // semantics without needing the caller to enumerate candidates up front.
        if (airlineCandidates.isEmpty()) {
            logger.log("No airline candidates provided — switching to discovery mode (open SRP search + mock-check).");
            FlightDynamicData discovered = probeFirstMockIssuedAirlineOnRoutes(
                    originCode, destinationCode,
                    departureDate, duration, adult, child, infant,
                    searchType, authorization,
                    tagDepart, tagReturn, maxAttempts, currency, deviceId,
                    optionalLocationsMap);
            if (discovered != null) return discovered;
            Assertion.skip("Skipping: discovery mode found no route whose first-returned airline is mock-issued.");
            return null;
        }

        for (Map.Entry<String, String> entry : airlineCandidates.entrySet()) {
            String candidateDepart = entry.getKey();
            String candidateReturn = entry.getValue();

            logBlock("Candidate airline " + candidateDepart + "/" + candidateReturn + " — verifying mock-issued");
            boolean departMockIssued = areAllSuppliersMockIssuedSilent(candidateDepart, originCode, destinationCode,
                    true);
            boolean returnMockIssued = candidateDepart.equals(candidateReturn)
                    ? departMockIssued
                    : areAllSuppliersMockIssuedSilent(candidateReturn, destinationCode, originCode, true);

            if (!departMockIssued || !returnMockIssued) {
                logger.log("Airline " + candidateDepart + "/" + candidateReturn
                        + " is NOT fully mock-issued (depart=" + departMockIssued + ", return=" + returnMockIssued + ").");
                if (airlineProvided) {
                    // User explicitly requested this airline — preserve the hard-skip contract of
                    // validateAllSuppliersAreMockIssuedByAirline instead of silently moving on.
                    Assertion.skip("Skipping: provided airline " + candidateDepart + "/" + candidateReturn
                            + " is not fully mock-issued.");
                    return null;
                }
                continue;
            }

            logger.log("Airline " + candidateDepart + "/" + candidateReturn + " is fully mock-issued. Searching dates.");

            logBlock("Mock-issued airline " + candidateDepart + " with original route");
            FlightDynamicData result = tryValidateDateRT(originCode, destinationCode, departureDate, duration,
                    adult, child, infant, searchType, candidateDepart, candidateReturn, authorization,
                    tagDepart, tagReturn, maxAttempts, currency, deviceId);
            if (result != null) return result;

            logBlock("Mock-issued airline " + candidateDepart + " with optional locations");
            for (Map.Entry<String, String> loc : optionalLocationsMap.entrySet()) {
                String optionalOrigin = convertToAirportCode(loc.getKey());
                String optionalDestination = convertToAirportCode(loc.getValue());
                result = tryValidateDateRT(optionalOrigin, optionalDestination, departureDate, duration,
                        adult, child, infant, searchType, candidateDepart, candidateReturn, authorization,
                        tagDepart, tagReturn, maxAttempts, currency, deviceId);
                if (result != null) return result;
            }

            // Mock-issued but no valid dates on any route: if user explicitly requested this
            // airline, fall through to the final skip below; otherwise try the next candidate.
            logger.log("Airline " + candidateDepart + " is mock-issued but no valid dates/routes found.");
        }

        Assertion.skip("Skipping: no mock-issued airline produced a valid date/route combination.");
        return null;
    }

    /**
     * Discovery-mode probe used by {@link #findDateRTWithMockIssuedAirline} when the caller does
     * not provide an airline and also does not supply {@code optionalAirline} to iterate over.
     *
     * <p>For each route (primary first, then each entry in {@code optionalLocationsMap}) this runs
     * an open round-trip search with no airline filter, inspects whichever airline the backend
     * returns for the depart/return legs, and mock-checks those specific codes. The first route
     * whose returned airline(s) are all mock-issued wins.
     *
     * <p>Caveat: the streaming SRP currently returns only the first flight that matches the
     * search, so per route we get exactly one airline to evaluate. To try more airlines, supply
     * {@code optionalAirline} (preferred) or {@code optionalLocations} with more route variants.
     */
    private FlightDynamicData probeFirstMockIssuedAirlineOnRoutes(
            String originCode, String destinationCode,
            String departureDate, int duration,
            int adult, int child, int infant,
            String searchType, String authorization,
            String tagDepart, String tagReturn,
            int maxAttempts, String currency, String deviceId,
            Map<String, String> optionalLocationsMap
    ) {
        logBlock("Discovery probe on primary route " + originCode + " -> " + destinationCode);
        FlightDynamicData result = tryValidateDateRT(originCode, destinationCode, departureDate, duration,
                adult, child, infant, searchType, "", "", authorization,
                tagDepart, tagReturn, maxAttempts, currency, deviceId);
        if (result != null && isDiscoveredAirlineMockIssued(result, originCode, destinationCode)) {
            return result;
        }

        if (optionalLocationsMap != null && !optionalLocationsMap.isEmpty()) {
            logBlock("Discovery probe on optional routes");
            for (Map.Entry<String, String> loc : optionalLocationsMap.entrySet()) {
                String optionalOrigin = convertToAirportCode(loc.getKey());
                String optionalDestination = convertToAirportCode(loc.getValue());
                logger.log("Probing optional route " + optionalOrigin + " -> " + optionalDestination);
                result = tryValidateDateRT(optionalOrigin, optionalDestination, departureDate, duration,
                        adult, child, infant, searchType, "", "", authorization,
                        tagDepart, tagReturn, maxAttempts, currency, deviceId);
                if (result != null && isDiscoveredAirlineMockIssued(result, optionalOrigin, optionalDestination)) {
                    return result;
                }
            }
        }

        return null;
    }

    /**
     * Single-trip variant of {@link #findDateRTWithMockIssuedAirline}. Combines {@link #findDate}
     * with {@link #validateAllSuppliersAreMockIssuedByAirline}: only returns flight dynamic data
     * for an airline whose suppliers are ALL mock-issued (i.e. not real-issued).
     *
     * <p>{@code airline} is optional:
     * <ul>
     *   <li>When <b>provided</b>, it is validated for mock-issued suppliers first. If any
     *       supplier is real-issued the test is skipped (same contract as
     *       {@link #validateAllSuppliersAreMockIssuedByAirline}).</li>
     *   <li>When <b>blank/null</b>, this method iterates every entry in {@code optionalAirline}
     *       and returns the <b>first</b> airline whose suppliers are all mock-issued and for
     *       which a valid departure date can be found. Airlines that are real-issued or have no
     *       supplier mappings are silently skipped (no {@code Assertion.skip}) so the loop can
     *       continue to the next candidate.</li>
     * </ul>
     *
     * <p>Route fallbacks ({@code optionalLocations}) still apply once a mock-issued airline is
     * selected, matching the {@link #findDate} fallback order.
     *
     * @param airline optional airline code; if blank, auto-pick first mock-issued
     * @see #findDateRTWithMockIssuedAirline
     */
    public FlightDynamicData findDateWithMockIssuedAirline(
            String username,
            String password,
            String origin,
            String destination,
            int daysToDepartureDate,
            int adult,
            int child,
            int infant,
            String searchType,
            String airline,
            String tag,
            String optionalLocations,
            String optionalAirline,
            String currency
    ) {
        String deviceId = CoreFunctions.generateDeviceId();
        int maxAttempts = 2;

        Map<String, String> optionalLocationsMap = CoreFunctions.parseDelimitedSectionToMap(optionalLocations);
        Map<String, String> optionalAirlineMap = CoreFunctions.parseDelimitedSectionToMap(optionalAirline);

        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);

        boolean airlineProvided = !isBlankString(airline);
        logger.log("Departure date provided -> " + departureDate);
        logger.log("Airline provided -> " + (airlineProvided
                ? airline
                : "<none — auto-selecting first mock-issued from optionalAirline>"));

        FlightDynamicData defaultData = new FlightDynamicData(
                airline, "", origin, destination, departureDate, "", "", "", "", "");

        if (Configs.ENVIRONMENT != Environment.GK && Configs.ENVIRONMENT != Environment.PREPROD) {
            return defaultData;
        }

        String authorization = fetchToken(username, password, deviceId);
        if (authorization == null) {
            Assertion.skip("Skipping due to token API failure.");
            return null;
        }

        searchType = selectTripType(searchType);
        String originCode = convertToAirportCode(origin);
        String destinationCode = convertToAirportCode(destination);

        // Build the ordered list of candidate airlines to try.
        // - When user provided an explicit airline, that's the only candidate (hard-skip on failure).
        // - Otherwise, iterate optionalAirlineMap values in declared order and auto-pick the
        //   first whose suppliers are all mock-issued.
        LinkedHashSet<String> airlineCandidates = new LinkedHashSet<>();
        if (airlineProvided) {
            airlineCandidates.add(airline);
        } else {
            for (String value : optionalAirlineMap.values()) {
                if (!isBlankString(value)) airlineCandidates.add(value);
            }
        }

        // Pure-discovery path: neither airline nor optionalAirline provided. Run an open SRP
        // search, read back the airline the backend returned for each route, and keep the first
        // one whose suppliers are all mock-issued.
        if (airlineCandidates.isEmpty()) {
            logger.log("No airline candidates provided — switching to discovery mode (open SRP search + mock-check).");
            FlightDynamicData discovered = probeFirstMockIssuedAirlineOnRoutesOneWay(
                    originCode, destinationCode, departureDate,
                    adult, child, infant, searchType, authorization,
                    tag, maxAttempts, currency, deviceId, optionalLocationsMap);
            if (discovered != null) return discovered;
            Assertion.skip("Skipping: discovery mode found no route whose first-returned airline is mock-issued.");
            return null;
        }

        for (String candidate : airlineCandidates) {
            logBlock("Candidate airline " + candidate + " — verifying mock-issued");
            boolean mockIssued = areAllSuppliersMockIssuedSilent(candidate, originCode, destinationCode, true);

            if (!mockIssued) {
                logger.log("Airline " + candidate + " is NOT fully mock-issued.");
                if (airlineProvided) {
                    // User explicitly requested this airline — preserve the hard-skip contract of
                    // validateAllSuppliersAreMockIssuedByAirline instead of silently moving on.
                    Assertion.skip("Skipping: provided airline " + candidate + " is not fully mock-issued.");
                    return null;
                }
                continue;
            }

            logger.log("Airline " + candidate + " is fully mock-issued. Searching dates.");

            logBlock("Mock-issued airline " + candidate + " with original route");
            FlightDynamicData result = tryValidateDate(originCode, destinationCode, departureDate,
                    adult, child, infant, searchType, candidate, authorization, tag, maxAttempts, currency, deviceId);
            if (result != null) return result;

            logBlock("Mock-issued airline " + candidate + " with optional locations");
            for (Map.Entry<String, String> loc : optionalLocationsMap.entrySet()) {
                String optionalOrigin = convertToAirportCode(loc.getKey());
                String optionalDestination = convertToAirportCode(loc.getValue());
                result = tryValidateDate(optionalOrigin, optionalDestination, departureDate,
                        adult, child, infant, searchType, candidate, authorization, tag, maxAttempts, currency, deviceId);
                if (result != null) return result;
            }

            logger.log("Airline " + candidate + " is mock-issued but no valid dates/routes found.");
        }

        Assertion.skip("Skipping: no mock-issued airline produced a valid date/route combination.");
        return null;
    }

    /**
     * Discovery-mode probe used by {@link #findDateWithMockIssuedAirline} when the caller does
     * not provide an airline and also does not supply {@code optionalAirline} to iterate over.
     * One-way analogue of {@link #probeFirstMockIssuedAirlineOnRoutes}.
     */
    private FlightDynamicData probeFirstMockIssuedAirlineOnRoutesOneWay(
            String originCode, String destinationCode,
            String departureDate,
            int adult, int child, int infant,
            String searchType, String authorization,
            String tag,
            int maxAttempts, String currency, String deviceId,
            Map<String, String> optionalLocationsMap
    ) {
        logBlock("Discovery probe on primary route " + originCode + " -> " + destinationCode);
        FlightDynamicData result = tryValidateDate(originCode, destinationCode, departureDate,
                adult, child, infant, searchType, "", authorization, tag, maxAttempts, currency, deviceId);
        if (result != null && isDiscoveredAirlineMockIssued(result, originCode, destinationCode)) {
            return result;
        }

        if (optionalLocationsMap != null && !optionalLocationsMap.isEmpty()) {
            logBlock("Discovery probe on optional routes");
            for (Map.Entry<String, String> loc : optionalLocationsMap.entrySet()) {
                String optionalOrigin = convertToAirportCode(loc.getKey());
                String optionalDestination = convertToAirportCode(loc.getValue());
                logger.log("Probing optional route " + optionalOrigin + " -> " + optionalDestination);
                result = tryValidateDate(optionalOrigin, optionalDestination, departureDate,
                        adult, child, infant, searchType, "", authorization, tag, maxAttempts, currency, deviceId);
                if (result != null && isDiscoveredAirlineMockIssued(result, optionalOrigin, optionalDestination)) {
                    return result;
                }
            }
        }

        return null;
    }

    /**
     * List-based overload of {@link #findDateRTWithMockIssuedAirline}. Accepts the flat airline
     * list produced by {@link CoreFunctions#makeAirlineArrayList(String)} and tries each
     * candidate (used as both depart and return airline) in order.
     *
     * <p>Semantics:
     * <ul>
     *   <li><b>List has exactly one entry</b> — that airline is treated as the primary; if its
     *       suppliers are not all mock-issued the test is hard-skipped (same contract as the
     *       String-API call with {@code airlineDepart} provided).</li>
     *   <li><b>List has more than one entry</b> — all entries are treated as candidates;
     *       iteration continues silently past airlines that fail the mock-issued check, and the
     *       first one whose suppliers are all mock-issued (and yields a valid depart/return
     *       date pair) is returned.</li>
     *   <li><b>List is null/empty</b> — falls through to discovery mode.</li>
     * </ul>
     *
     * @param airlines candidate airline codes (typically from
     *                 {@code CoreFunctions.makeAirlineArrayList(param.get("airline"))})
     * @see #findDateRTWithMockIssuedAirline(String, String, String, String, int, int, int, int, int, String, String, String, String, String, String, String, String)
     */
    public FlightDynamicData findDateRTWithMockIssuedAirline(
            String username,
            String password,
            String origin,
            String destination,
            int daysToDepartureDate,
            int duration,
            int adult,
            int child,
            int infant,
            String searchType,
            List<String> airlines,
            String tagDepart,
            String tagReturn,
            String optionalLocations,
            String currency
    ) {
        String primary;
        String optionalAirline;
        if (airlines == null || airlines.isEmpty()) {
            primary = "";
            optionalAirline = "";
        } else if (airlines.size() == 1) {
            primary = isBlankString(airlines.get(0)) ? "" : airlines.get(0).trim();
            optionalAirline = "";
        } else {
            primary = "";
            optionalAirline = buildOptionalAirlinePairString(airlines);
        }
        return findDateRTWithMockIssuedAirline(
                username, password,
                origin, destination,
                daysToDepartureDate, duration,
                adult, child, infant,
                searchType,
                primary, primary,
                tagDepart, tagReturn,
                optionalLocations, optionalAirline,
                currency);
    }

    /**
     * List-based overload of {@link #findDateWithMockIssuedAirline}. Same semantics as the
     * round-trip list overload — see
     * {@link #findDateRTWithMockIssuedAirline(String, String, String, String, int, int, int, int, int, String, List, String, String, String, String)}.
     *
     * @param airlines candidate airline codes (typically from
     *                 {@code CoreFunctions.makeAirlineArrayList(param.get("airline"))})
     */
    public FlightDynamicData findDateWithMockIssuedAirline(
            String username,
            String password,
            String origin,
            String destination,
            int daysToDepartureDate,
            int adult,
            int child,
            int infant,
            String searchType,
            List<String> airlines,
            String tag,
            String optionalLocations,
            String currency
    ) {
        String primary;
        String optionalAirline;
        if (airlines == null || airlines.isEmpty()) {
            primary = "";
            optionalAirline = "";
        } else if (airlines.size() == 1) {
            primary = isBlankString(airlines.get(0)) ? "" : airlines.get(0).trim();
            optionalAirline = "";
        } else {
            primary = "";
            optionalAirline = buildOptionalAirlinePairString(airlines);
        }
        return findDateWithMockIssuedAirline(
                username, password,
                origin, destination,
                daysToDepartureDate,
                adult, child, infant,
                searchType,
                primary,
                tag,
                optionalLocations, optionalAirline,
                currency);
    }

    /**
     * Converts a flat airline list into the delimited pair string consumed by
     * {@link CoreFunctions#parseDelimitedSectionToMap(String)} — each entry becomes a
     * {@code airline=airline} pair so it iterates correctly for one-way (values only) and
     * round-trip (depart=return) callers. Blank entries are skipped.
     */
    private static String buildOptionalAirlinePairString(List<String> airlines) {
        if (airlines == null || airlines.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (String airline : airlines) {
            if (airline == null || airline.trim().isEmpty()) continue;
            String trimmed = airline.trim();
            if (!first) sb.append("/");
            sb.append(trimmed).append("=").append(trimmed);
            first = false;
        }
        if (first) return "";
        sb.append("]");
        return sb.toString();
    }

    /**
     * Mock-checks the airline(s) that came back from an open SRP search. The depart leg is checked
     * on originCode to destinationCode; the return leg is only checked when it differs from the
     * depart airline, using the reverse route.
     */
    private boolean isDiscoveredAirlineMockIssued(FlightDynamicData data, String originCode, String destinationCode) {
        if (data == null) return false;
        String departAirline = data.airlineCodeDepart;
        String returnAirline = data.airlineCodeReturn;

        if (isBlankString(departAirline)) {
            logger.log("Discovery: depart airline code missing on returned flight - cannot mock-check.");
            return false;
        }

        logger.log("Discovery: returned airline(s) depart=" + departAirline
                + " return=" + returnAirline + " - running mock-issued check.");

        if (!areAllSuppliersMockIssuedSilent(departAirline, originCode, destinationCode, true)) {
            logger.log("Discovery: depart airline " + departAirline + " on "
                    + originCode + "->" + destinationCode + " is NOT fully mock-issued.");
            return false;
        }

        if (!isBlankString(returnAirline) && !returnAirline.equals(departAirline)) {
            if (!areAllSuppliersMockIssuedSilent(returnAirline, destinationCode, originCode, true)) {
                logger.log("Discovery: return airline " + returnAirline + " on "
                        + destinationCode + "->" + originCode + " is NOT fully mock-issued.");
                return false;
            }
        }

        logger.log("Discovery: airline " + departAirline + "/" + returnAirline + " is fully mock-issued. Accepting.");
        return true;
    }

    /**
     * Non-throwing supplier mock-issued check. Never calls Assertion.skip.
     *
     * @param lenientWhenUnmapped true for discovery (empty mapping is OK); false for payment gates
     *                            (empty mapping is treated as not mock-confirmed).
     */
    private boolean areAllSuppliersMockIssuedSilent(String airlineCode, String origin, String destination,
            boolean lenientWhenUnmapped) {
        if (isBlankString(airlineCode) || isBlankString(origin) || isBlankString(destination)) {
            return false;
        }
        try {
            List<String> originsToValidate = resolveOriginsToValidate(origin);
            if (originsToValidate.isEmpty()) {
                if (lenientWhenUnmapped) {
                    logger.log("Silent check: no resolved origins for " + airlineCode + "/" + origin
                            + " -> " + destination + ", treating as non-blocking (discovery).");
                    return true;
                }
                logger.log("Silent check: no resolved origins for " + airlineCode + "/" + origin
                        + " -> " + destination + ", cannot confirm mock-issued (strict).");
                return false;
            }
            for (String resolvedOrigin : originsToValidate) {
                if (!areSuppliersMockIssuedForOneOriginSilent(airlineCode, resolvedOrigin, destination,
                        lenientWhenUnmapped)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            logger.log("areAllSuppliersMockIssuedSilent exception for airline=" + airlineCode
                    + " origin=" + origin + " destination=" + destination + ": " + e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * Silent single-origin mock-issued check. Mirrors
     * {@link #validateSuppliersMockIssuedForOneOrigin} but returns a boolean instead of skipping
     * the test, so it can drive an iteration loop.
     */
    private boolean areSuppliersMockIssuedForOneOriginSilent(String airlineCode, String origin, String destination,
            boolean lenientWhenUnmapped) {
        DynamicSupplierMappingByAirlineApi.Inputs inputs =
                new DynamicSupplierMappingByAirlineApi.Inputs(airlineCode, origin, destination);
        DynamicSupplierMappingByAirlineApi api = new DynamicSupplierMappingByAirlineApi(inputs);
        ApiResponse apiResponse = api.hitApi(apiTools);

        DynamicSupplierMappingByAirlineResponse response = new Gson().fromJson(
                apiResponse.getBody(), DynamicSupplierMappingByAirlineResponse.class);

        if (response == null || response.data == null || response.data.content == null
                || response.data.content.isEmpty()) {
            if (lenientWhenUnmapped) {
                logger.log("Silent check: no supplier mappings for airline " + airlineCode + " origin " + origin
                        + ", treating as non-blocking (discovery).");
                return true;
            }
            logger.log("Silent check: no supplier mappings for airline " + airlineCode + " origin " + origin
                    + ", cannot confirm mock-issued (strict).");
            return false;
        }

        Set<String> uniqueSupplierCodes = new LinkedHashSet<>();
        for (DynamicSupplierMappingByAirlineResponse.SupplierMapping mapping : response.data.content) {
            if (mapping.supplier != null && mapping.supplier.code != null && !mapping.supplier.code.isEmpty()) {
                uniqueSupplierCodes.add(mapping.supplier.code);
            }
        }
        if (uniqueSupplierCodes.isEmpty()) {
            if (lenientWhenUnmapped) {
                logger.log("Silent check: no valid supplier codes for airline " + airlineCode + " origin " + origin
                        + ", treating as non-blocking (discovery).");
                return true;
            }
            logger.log("Silent check: no valid supplier codes for airline " + airlineCode + " origin " + origin
                    + ", cannot confirm mock-issued (strict).");
            return false;
        }

        logger.log("Silent check: found " + uniqueSupplierCodes.size() + " supplier(s) for "
                + airlineCode + "/" + origin + ": " + uniqueSupplierCodes);

        for (String supplierCode : uniqueSupplierCodes) {
            String supplierUrl = fetchIntegratorConfigUrl(supplierCode);
            if (supplierUrl == null || supplierUrl.isEmpty()) {
                // No URL to evaluate — neither clearly mock nor real; skip this supplier but keep
                // checking the rest. This matches the existing lenient behavior in
                // validateSuppliersMockIssuedForOneOrigin.
                logger.log("Silent check: no integrator URL for supplier " + supplierCode + ", skipping.");
                continue;
            }
            if (!isMockIssuedSupplier(supplierUrl)) {
                logger.log("Silent check: supplier " + supplierCode + " is NOT mock-issued (url=" + supplierUrl + ")");
                return false;
            }
        }
        return true;
    }

    private static boolean isBlankString(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String fetchToken(String username, String password,String deviceId) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return getToken(deviceId);
            } else {
                Unm<T> unm = new Unm<>(session);
                return unm.unmLoginApi(username, password).accessToken;
            }
        } catch (Exception e) {
            logger.log("Exception while fetching token: " + e.getLocalizedMessage());
            return null;
        }
    }

    private void logBlock(String label) {
        logger.log("====== Starting validation for " + label + " ======");
    }

    /**
     * Normalize a trip-type string to the API-canonical form used downstream.
     * Accepts both the short, lowercase form ("oneway"/"roundtrip") used by some test data
     * sheets and the canonical API form ("ONE_WAY"/"ROUND_TRIP") used by others, in any
     * case and with optional underscores/hyphens — so a row written as `searchType:ONE_WAY`,
     * `tripType:oneway`, `One-Way`, etc. all resolve correctly.
     */
    private String selectTripType(String trip) {
        if (trip == null) {
            throw new NotImplementedException("Unimplemented trip type: <null>");
        }
        String normalized = trip.replace("_", "").replace("-", "").trim().toLowerCase();
        return switch (normalized) {
            case "roundtrip" -> "ROUND_TRIP";
            case "oneway" -> "ONE_WAY";
            default -> throw new NotImplementedException("Unimplemented trip type: " + trip);
        };
    }

    private String convertToAirportCode(String data){
        if(data.length() == 3 || data.length() == 4){
            return data.toUpperCase();
        }
        return switch (data.toLowerCase()) {
            case "medan" -> "KNO";
            case "pnk" -> "PNK";
            case "jambi" -> "DJB";
            case "surabaya" -> "SUB";
            case "kuala lumpur" -> "KUL";
            default -> {
                String code = getAirportCode(data);
                if (code == null) {
                    logger.log("Warning: No airport code found for '" + data + "'");
                    Assertion.skip("Failed to get airport code via API. Skipping the subsequent steps.");
                }
                yield code;
            }
        };
    }

    @SneakyThrows
    public String getAirportCode(String airportName) {
        try{
            String accessToken = getToken(CoreFunctions.generateDeviceId());
            logger.log("accessToken-> " + accessToken);

            FlightAutocompleteApi.Inputs inputs = new FlightAutocompleteApi.Inputs(airportName,accessToken);
            FlightAutocompleteResponse flightAutocompleteResponse = flightAutocompleteApi(inputs);
            List<FlightAutocompleteResponse.Airport> airports = flightAutocompleteResponse.data.airports;
            for (FlightAutocompleteResponse.Airport airport : airports) {
                logger.log("Airport Name->" + airport.airportName + ", Airport Code->" + airport.airportCode);
                if (airport.airportName.toLowerCase().contains(airportName.toLowerCase())) {
                    return airport.airportCode;
                }
            }
        }
        catch (Exception e) {
            logger.log("Error occurred: "+ e);
            Assertion.skip("Flight Booking via API failed. Skipping the subsequent steps.");
        }
        return null;
    }

    /**
     * Attempts to validate flight availability for multiple dates starting from the given departure date.
     *
     * @param origin        Origin airport code.
     * @param destination   Destination airport code.
     * @param departureDate Initial departure date.
     * @param adult         Number of adults.
     * @param child         Number of children.
     * @param infant        Number of infant passengers.
     * @param searchType    Search type (e.g., "OW").
     * @param airline       Airline code.
     * @param authorization Authorization token.
     * @param tag           Optional flight tag.
     * @param maxAttempts   Maximum number of date attempts.
     * @return FlightDynamicData if a valid flight is found; otherwise, null.
     */
    private FlightDynamicData tryValidateDate(String origin, String destination, String departureDate,
                                              int adult, int child, int infant,
                                              String searchType, String airline,
                                              String authorization, String tag,
                                              int maxAttempts,String currency,String deviceId) {
        for (String attemptDate : generateAttemptDates(departureDate, maxAttempts, tag)) {
            FlightDynamicData flightDynamicData = validateDate(origin, destination, attemptDate, adult, child, infant,
                    searchType, airline, authorization, tag,currency,deviceId);
            if (flightDynamicData != null) {
                logger.log("Departure date found: " + flightDynamicData.departureDate);
                logger.log("airlineDepart found: " + flightDynamicData.airlineDepart);
                logger.log("originAirport found: " + flightDynamicData.originAirport);
                logger.log("destinationAirport found: " + flightDynamicData.destinationAirport);
                String summary = String.format(
                        "Fetched flight dynamic data successfully%n"
                                + "  Requested -> origin: %s, destination: %s, searchType: %s, airline: '%s', tag/filter: '%s', adult: %d, child: %d, infant: %d%n"
                                + "  Found     -> originAirport: %s, destinationAirport: %s, departureDate: %s, airlineDepart: %s (code: %s), departureTime: %s, arrivalTime: %s",
                        origin, destination, searchType, airline, tag, adult, child, infant,
                        flightDynamicData.originAirport, flightDynamicData.destinationAirport,
                        flightDynamicData.departureDate,
                        flightDynamicData.airlineDepart, flightDynamicData.airlineCodeDepart,
                        flightDynamicData.departureTimeDepart, flightDynamicData.arrivalTimeDepart
                );
                Assertion.assertThat(summary, flightDynamicData != null, is(true));
                return flightDynamicData;
            }
        }
        return null;
    }

    /**
     * Attempts to validate round-trip flight availability for multiple pairs of departure and return dates.
     *
     * @param origin         Origin airport code.
     * @param destination    Destination airport code.
     * @param departureDate  Initial departure date.
     * @param returnDaysGap  Number of days between departure and return.
     * @param adult          Number of adults.
     * @param child          Number of child passengers.
     * @param infant         Number of infant passengers.
     * @param searchType     Search type (e.g., "RT").
     * @param airline        Departure airline code.
     * @param airlineReturn  Return airline code.
     * @param authorization  Authorization token for the API.
     * @param tagDepart            Optional flight tag.
     * @param tagReturn             Optional flight tag for return flight.
     * @param maxAttempts    Maximum number of date pairs to attempt.
     * @return FlightDynamicData if a valid round-trip flight is found; otherwise, null.
     */
    private FlightDynamicData tryValidateDateRT(String origin, String destination, String departureDate, int returnDaysGap,
                                                int adult, int child, int infant,
                                                String searchType, String airline, String airlineReturn,
                                                String authorization, String tagDepart, String tagReturn,
                                                int maxAttempts,String currency,String deviceId) {
        for (Pair<String, String> attemptDate : generateAttemptDatePairs(departureDate, maxAttempts, returnDaysGap)) {
            String departDate = attemptDate.first;
            String returnDate = attemptDate.second;

            FlightDynamicData flightDynamicData = validateDateRoundTrip(origin, destination, departDate, returnDate,
                    adult, child, infant, searchType, airline, airlineReturn, authorization, tagDepart, tagReturn,currency,deviceId);

            if (flightDynamicData != null) {
                logger.log("Departure date found: " + flightDynamicData.departureDate);
                logger.log("Return date found: " + flightDynamicData.returnDate);
                logger.log("airlineDepart found: " + flightDynamicData.airlineDepart);
                logger.log("airlineReturn found: " + flightDynamicData.airlineReturn);
                logger.log("originAirport found: " + flightDynamicData.originAirport);
                logger.log("destinationAirport found: " + flightDynamicData.destinationAirport);
                String summary = String.format(
                        "Fetched flight dynamic data successfully%n"
                                + "  Requested -> origin: %s, destination: %s, searchType: %s, airlineDepart: '%s', airlineReturn: '%s', tagDepart: '%s', tagReturn: '%s', adult: %d, child: %d, infant: %d, returnDaysGap: %d%n"
                                + "  Found     -> originAirport: %s, destinationAirport: %s, departureDate: %s, returnDate: %s, airlineDepart: %s (code: %s), airlineReturn: %s (code: %s), depart[%s -> %s], return[%s -> %s]",
                        origin, destination, searchType, airline, airlineReturn, tagDepart, tagReturn,
                        adult, child, infant, returnDaysGap,
                        flightDynamicData.originAirport, flightDynamicData.destinationAirport,
                        flightDynamicData.departureDate, flightDynamicData.returnDate,
                        flightDynamicData.airlineDepart, flightDynamicData.airlineCodeDepart,
                        flightDynamicData.airlineReturn, flightDynamicData.airlineCodeReturn,
                        flightDynamicData.departureTimeDepart, flightDynamicData.arrivalTimeDepart,
                        flightDynamicData.departureTimeReturn, flightDynamicData.arrivalTimeReturn
                );
                Assertion.assertThat(summary, flightDynamicData != null, is(true));
                return flightDynamicData;
            }
        }
        return null;
    }

    /**
     * Transit-aware overload of {@link #tryValidateDateRT}. Iterates over candidate date pairs
     * and delegates to the transit-aware {@link #validateDateRoundTrip} overload so that the
     * selected flights honor the requested transit shape.
     */
    private FlightDynamicData tryValidateDateRT(String origin, String destination, String departureDate, int returnDaysGap,
                                                int adult, int child, int infant,
                                                String searchType, String airline, String airlineReturn,
                                                String authorization, String tagDepart, String tagReturn,
                                                int maxAttempts, String currency, String deviceId, boolean transit) {
        for (Pair<String, String> attemptDate : generateAttemptDatePairs(departureDate, maxAttempts, returnDaysGap)) {
            String departDate = attemptDate.first;
            String returnDate = attemptDate.second;

            FlightDynamicData flightDynamicData = validateDateRoundTrip(origin, destination, departDate, returnDate,
                    adult, child, infant, searchType, airline, airlineReturn, authorization, tagDepart, tagReturn,
                    currency, deviceId, transit);

            if (flightDynamicData != null) {
                logger.log("Departure date found: " + flightDynamicData.departureDate);
                logger.log("Return date found: " + flightDynamicData.returnDate);
                logger.log("airlineDepart found: " + flightDynamicData.airlineDepart);
                logger.log("airlineReturn found: " + flightDynamicData.airlineReturn);
                logger.log("originAirport found: " + flightDynamicData.originAirport);
                logger.log("destinationAirport found: " + flightDynamicData.destinationAirport);
                logger.log("totalTransitDepart found: " + flightDynamicData.totalTransitDepart);
                logger.log("totalTransitReturn found: " + flightDynamicData.totalTransitReturn);
                String summary = String.format(
                        "Fetched flight dynamic data successfully%n"
                                + "  Requested -> origin: %s, destination: %s, searchType: %s, airlineDepart: '%s', airlineReturn: '%s', tagDepart: '%s', tagReturn: '%s', transit: %s, adult: %d, child: %d, infant: %d, returnDaysGap: %d%n"
                                + "  Found     -> originAirport: %s, destinationAirport: %s, departureDate: %s, returnDate: %s, airlineDepart: %s (code: %s), airlineReturn: %s (code: %s), totalTransitDepart: %d, totalTransitReturn: %d, depart[%s -> %s], return[%s -> %s]",
                        origin, destination, searchType, airline, airlineReturn, tagDepart, tagReturn, transit,
                        adult, child, infant, returnDaysGap,
                        flightDynamicData.originAirport, flightDynamicData.destinationAirport,
                        flightDynamicData.departureDate, flightDynamicData.returnDate,
                        flightDynamicData.airlineDepart, flightDynamicData.airlineCodeDepart,
                        flightDynamicData.airlineReturn, flightDynamicData.airlineCodeReturn,
                        flightDynamicData.totalTransitDepart, flightDynamicData.totalTransitReturn,
                        flightDynamicData.departureTimeDepart, flightDynamicData.arrivalTimeDepart,
                        flightDynamicData.departureTimeReturn, flightDynamicData.arrivalTimeReturn
                );
                Assertion.assertThat(summary, flightDynamicData != null, is(true));
                return flightDynamicData;
            }
        }
        return null;
    }

    //aditi
    private List<String> generateAttemptDates(String startDate, int maxAttempts, String tag) {
        List<String> attemptDates = new ArrayList<>();
        Set<String> seenDates = new HashSet<>(); // prevent duplicates

        // Check if tag contains "quiet_zone"
        boolean isQuietZone = tag != null && tag.toLowerCase().contains("quiet_zone");
        
        // If quiet_zone, validate that startDate is Monday, Wednesday, or Friday
        if (isQuietZone) {
            if (!isMondayWednesdayOrFriday(startDate)) {
                logger.log("WARNING: startDate " + startDate + " is not a Monday, Wednesday, or Friday for quiet_zone tag");
                logger.log("Adjusting startDate to next Monday, Wednesday, or Friday");
                // Find the next MWF date
                startDate = getNextMondayWednesdayOrFridayFromDate(startDate);
                logger.log("Adjusted startDate to: " + startDate);
            }
        }

        int step = 0; // distance from startDate (in MWF increments for quiet_zone)
        boolean forward = true; // toggle direction

        while (attemptDates.size() < maxAttempts) {
            String newDate;
            
            if (isQuietZone) {
                // For quiet_zone, skip to next MWF date instead of incrementing by 1
                newDate = getNextMWFDate(startDate, step, forward);
            } else {
                // Original logic: increment/decrement by step days
                int offset = forward ? step : -step;
                newDate = CoreFunctions.addToDateYYYY_MM_DD(startDate, offset);
            }

            if (!seenDates.contains(newDate) && shouldIncludeDate(newDate, tag)) {
                attemptDates.add(newDate);
                seenDates.add(newDate);
            }

            if (!forward) {
                step++; // increase distance after both forward & backward
            }
            forward = !forward; // switch direction
        }

        return attemptDates;
    }

    public record Pair<K, V>(K first, V second) {
    }

    private List<Pair<String, String>> generateAttemptDatePairs(String startDate, int maxAttempts, int returnDaysGap) {
        List<Pair<String, String>> datePairs = new ArrayList<>();
        String currentDate = startDate;

        while (datePairs.size() < maxAttempts) {
            String returnDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, returnDaysGap);

            if (shouldIncludeDate(currentDate) && shouldIncludeDate(returnDate)) {
                datePairs.add(new Pair<>(currentDate, returnDate));
            }

            currentDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, 1);
        }

        return datePairs;
    }

    private boolean shouldIncludeDate(String date) {
        return shouldIncludeDate(date, "");
    }

    private boolean shouldIncludeDate(String date, String tag) {
        if (SKIP_WEEKENDS && isWeekend(date)) return false;
        if (SKIP_HOLIDAYS && isHoliday(date)) return false;
        
        // If tag contains "quiet_zone", only include Monday, Wednesday, or Friday
        if (tag != null && tag.toLowerCase().contains("quiet_zone")) {
            if (!isMondayWednesdayOrFriday(date)) {
                return false;
            }
        }
        
        return true;
    }

    private boolean isWeekend(String date) {
        LocalDate localDate = LocalDate.parse(date);
        DayOfWeek day = localDate.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private boolean isHoliday(String date) {
        // Plug your holiday lookup here. Example:
        //will add api to fetch holidays in future

        return false; // Placeholder, replace with actual holiday check logic
    }

    /**
     * Checks if a given date is a Monday, Wednesday, or Friday.
     * 
     * @param date Date string in format "yyyy-MM-dd"
     * @return true if the date is Monday, Wednesday, or Friday
     */
    private boolean isMondayWednesdayOrFriday(String date) {
        LocalDate localDate = LocalDate.parse(date);
        DayOfWeek day = localDate.getDayOfWeek();
        return day == DayOfWeek.MONDAY || day == DayOfWeek.WEDNESDAY || day == DayOfWeek.FRIDAY;
    }

    /**
     * Gets the next Monday, Wednesday, or Friday from a given date.
     * If the given date is already MWF, returns the date itself.
     * Otherwise, finds the next MWF date.
     * 
     * @param date Date string in format "yyyy-MM-dd"
     * @return Next MWF date string in format "yyyy-MM-dd"
     */
    private String getNextMondayWednesdayOrFridayFromDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        DayOfWeek day = localDate.getDayOfWeek();
        
        // If already MWF, return the date itself
        if (day == DayOfWeek.MONDAY || day == DayOfWeek.WEDNESDAY || day == DayOfWeek.FRIDAY) {
            return date;
        }
        
        // Find the next MWF
        LocalDate nextMonday = localDate.with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate nextWednesday = localDate.with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        LocalDate nextFriday = localDate.with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.FRIDAY));
        
        // Return the earliest of the three
        LocalDate nearestDate = nextMonday;
        if (nextWednesday.isBefore(nearestDate)) {
            nearestDate = nextWednesday;
        }
        if (nextFriday.isBefore(nearestDate)) {
            nearestDate = nextFriday;
        }
        
        return nearestDate.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Gets the nth MWF date from startDate in the specified direction.
     * For quiet_zone tag, this ensures we only skip to MWF dates.
     * 
     * @param startDate Base date in format "yyyy-MM-dd" (must be MWF)
     * @param step Step number (0 = startDate itself, 1 = next/prev MWF, etc.)
     * @param forward true for forward direction, false for backward
     * @return MWF date string in format "yyyy-MM-dd"
     */
    private String getNextMWFDate(String startDate, int step, boolean forward) {
        LocalDate currentDate = LocalDate.parse(startDate);
        
        if (step == 0) {
            return startDate; // Return startDate itself (already validated as MWF)
        }
        
        // Move to the next/previous MWF date step times
        for (int i = 0; i < step; i++) {
            if (forward) {
                // Move forward to next MWF
                currentDate = currentDate.plusDays(1);
                while (!isMondayWednesdayOrFriday(currentDate.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE))) {
                    currentDate = currentDate.plusDays(1);
                }
            } else {
                // Move backward to previous MWF
                currentDate = currentDate.minusDays(1);
                while (!isMondayWednesdayOrFriday(currentDate.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE))) {
                    currentDate = currentDate.minusDays(1);
                }
            }
        }
        
        return currentDate.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Validates the availability of a flight on a specific date for given search parameters.
     *
     * @param origin       The origin airport code.
     * @param destination  The destination airport code.
     * @param departureDate The departure date in YYYY-MM-DD format.
     * @param adult        Number of adult passengers.
     * @param child        Number of child passengers.
     * @param infant       Number of infant passengers.
     * @param searchType   The type of search (e.g., "OW" for one-way).
     * @param airline      The airline code to filter flights.
     * @param authorization Authorization token for the API.
     * @param tag          Optional tag for filtering flights (e.g., meal, baggage).
     * @return A {@link FlightDynamicData} object if a valid flight is found; otherwise, null.
     * @throws Exception Propagates exceptions from called methods (due to @SneakyThrows).
     */
    @SneakyThrows
    private FlightDynamicData validateDate(
            String origin,
            String destination,
            String departureDate,
            int adult,
            int child,
            int infant,
            String searchType,
            String airline,
            String authorization,
            String tag,
            String currency,
            String deviceId
    ) {
        FlightSearchApi.Inputs flightSearchApiInputs = new FlightSearchApi.Inputs(
                origin.toUpperCase(), destination.toUpperCase(),
                selectOriginAndDestinationType(origin),
                selectOriginAndDestinationType(destination),
                departureDate, adult, child, infant,
                searchType, authorization, currency,deviceId
        );
        try {
            ArrayList<String> requestItems = flightSearchGetRequestItems(flightSearchApiInputs);
            if (requestItems == null || requestItems.isEmpty()) {
                logger.log("No request items found for the flight search API inputs.");
                return null;
            }
            logger.log("requestItems -> " + requestItems);

            FlightStreamResponse.DepartureFlight departureFlight = flightSearchStreamByNameDynamicData(
                    requestItems.stream().toList(), "OW", "", authorization, airline, tag, child, currency,deviceId
            );

            boolean airlineExist = departureFlight != null;
            logger.log("Flight Search API response -> " + airlineExist);

            if (airlineExist) {
                FlightDynamicData data = new FlightDynamicData(
                        departureFlight.airlineName, "", origin, destination, departureDate, "",
                        departureFlight.departureTime, departureFlight.arrivalTime, "", ""
                );
                data.airlineCodeDepart = departureFlight.marketingAirlineCode;
                return data;
            }
            return null;
        } catch (Exception e) {
            logger.log("Exception in validateDate: " + e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Validates availability of round-trip flights based on search parameters.
     *
     * This method checks both departure and return legs for valid flights
     * matching the specified airlines and tags.
     *
     * @param origin         The origin airport code.
     * @param destination    The destination airport code.
     * @param departureDate  The departure date in YYYY-MM-DD format.
     * @param returnDate     The return date in YYYY-MM-DD format.
     * @param adult          Number of adults.
     * @param child          Number of child passengers.
     * @param infant         Number of infant passengers.
     * @param searchType     The type of search (e.g., "RT" for round-trip).
     * @param airline        The airline code to filter departure flights.
     * @param airlineReturn  The airline code to filter return flights.
     * @param auth           Authorization token for the API.
     * @param tagDepart            Optional tag for filtering flights (e.g., meal, baggage).
     * @param tagReturn             Optional tag for return flights.
     * @return A {@link FlightDynamicData} object if both departure and return flights are found; otherwise, null.
     * @throws Exception Propagates exceptions from called methods (due to @SneakyThrows).
     */
    @SneakyThrows
    private FlightDynamicData validateDateRoundTrip(
            String origin,
            String destination,
            String departureDate,
            String returnDate,
            int adult,
            int child,
            int infant,
            String searchType,
            String airline,
            String airlineReturn,
            String auth,
            String tagDepart,
            String tagReturn,
            String currency,
            String deviceId
    ) {
        String originType = selectOriginAndDestinationType(origin);
        String destinationType = selectOriginAndDestinationType(destination);

        if(tagReturn.isEmpty())
            tagReturn = tagDepart; // Use the same tag for return if not specified

        logger.log("Check departure date -> " + departureDate);
        logger.log("Check return date -> " + returnDate);

        FlightRoundTripSearchApi.Inputs flightRoundTripSearchApiInputs = new FlightRoundTripSearchApi.Inputs(
                origin, destination, originType, destinationType,
                departureDate, returnDate,
                adult, child, infant, searchType, auth,currency, deviceId
        );

        try {
            ArrayList<String> requestItems = flightSearchGetRequestItemsRoundTripDepart(flightRoundTripSearchApiInputs);
            if (requestItems == null || requestItems.isEmpty()) {
                logger.log("No request items found for the flight search API inputs.");
                return null;
            }

            FlightStreamResponse.DepartureFlight departureFlight = flightSearchStreamByNameDynamicData(
                    requestItems.stream().toList(), "RT_DEPARTURE", returnDate, auth, airline, tagDepart, child, currency, deviceId
            );

            boolean airlineExistDepart = departureFlight != null;
            logger.log("Flight Search API response -> " + airlineExistDepart);

            if (airlineExistDepart) {
                logger.log("->>> searching for return flight <<<-");
                FlightSearchNextApi.Inputs flightSearchNextApiInputs = new FlightSearchNextApi.Inputs(
                        origin, destination, originType, destinationType,
                        departureDate, returnDate,
                        departureFlight.flightId, departureFlight.fareId,
                        departureFlight.fareDetail.fares.get(0).supplierId,
                        auth,
                        departureFlight,currency, deviceId
                );

                ArrayList<String> requestItemsReturn = flightSearchGetRequestItemsRoundTripReturn(flightSearchNextApiInputs);
                if (requestItemsReturn == null || requestItemsReturn.isEmpty()) {
                    logger.log("No request items found for the return leg of flight search API inputs.");
                    return null;
                }

                FlightStreamResponse.ReturnFlight returnFlight = flightSearchStreamByNameDynamicDataReturn(
                        requestItemsReturn, "RT_RETURN", returnDate, auth, airlineReturn, tagReturn, child, currency, deviceId
                );

                boolean airlineExistReturn = returnFlight != null;
                logger.log("Return flight search success -> " + airlineExistReturn);

                if (airlineExistReturn) {
                    FlightDynamicData data = new FlightDynamicData(
                            departureFlight.airlineName, returnFlight.airlineName, origin, destination,
                            departureDate, returnDate,
                            departureFlight.departureTime, departureFlight.arrivalTime,
                            returnFlight.departureTime, returnFlight.arrivalTime
                    );
                    data.airlineCodeDepart = departureFlight.marketingAirlineCode;
                    data.airlineCodeReturn = returnFlight.marketingAirlineCode;
                    return data;
                }
            }
        } catch (Exception e) {
            logger.log("Exception in validateDateRoundTrip: " + e.getLocalizedMessage());
            return null;
        }
        return null;
    }

    /**
     * Transit-aware overload of {@link #validateDateRoundTrip}. Mirrors the base method but
     * routes stream searches through the transit-aware variants and populates
     * {@code totalTransitDepart} / {@code totalTransitReturn} on the returned data.
     */
    private FlightDynamicData validateDateRoundTrip(
            String origin,
            String destination,
            String departureDate,
            String returnDate,
            int adult,
            int child,
            int infant,
            String searchType,
            String airline,
            String airlineReturn,
            String auth,
            String tagDepart,
            String tagReturn,
            String currency,
            String deviceId,
            boolean transit
    ) {
        String originType = selectOriginAndDestinationType(origin);
        String destinationType = selectOriginAndDestinationType(destination);

        if (tagReturn.isEmpty())
            tagReturn = tagDepart;

        logger.log("Check departure date -> " + departureDate);
        logger.log("Check return date -> " + returnDate);
        logger.log("Transit filter -> " + transit);

        FlightRoundTripSearchApi.Inputs flightRoundTripSearchApiInputs = new FlightRoundTripSearchApi.Inputs(
                origin, destination, originType, destinationType,
                departureDate, returnDate,
                adult, child, infant, searchType, auth, currency, deviceId
        );

        try {
            ArrayList<String> requestItems = flightSearchGetRequestItemsRoundTripDepart(flightRoundTripSearchApiInputs);
            if (requestItems == null || requestItems.isEmpty()) {
                logger.log("No request items found for the flight search API inputs.");
                return null;
            }

            FlightStreamResponse.DepartureFlight departureFlight = flightSearchStreamByNameDynamicData(
                    requestItems.stream().toList(), "RT_DEPARTURE", returnDate, auth, airline, tagDepart, child, currency, deviceId, transit
            );

            boolean airlineExistDepart = departureFlight != null;
            logger.log("Flight Search API response -> " + airlineExistDepart);

            if (airlineExistDepart) {
                logger.log("->>> searching for return flight <<<-");
                FlightSearchNextApi.Inputs flightSearchNextApiInputs = new FlightSearchNextApi.Inputs(
                        origin, destination, originType, destinationType,
                        departureDate, returnDate,
                        departureFlight.flightId, departureFlight.fareId,
                        departureFlight.fareDetail.fares.get(0).supplierId,
                        auth,
                        departureFlight, currency, deviceId
                );

                ArrayList<String> requestItemsReturn = flightSearchGetRequestItemsRoundTripReturn(flightSearchNextApiInputs);
                if (requestItemsReturn == null || requestItemsReturn.isEmpty()) {
                    logger.log("No request items found for the return leg of flight search API inputs.");
                    return null;
                }

                FlightStreamResponse.ReturnFlight returnFlight = flightSearchStreamByNameDynamicDataReturn(
                        requestItemsReturn, "RT_RETURN", returnDate, auth, airlineReturn, tagReturn, child, currency, deviceId, transit
                );

                boolean airlineExistReturn = returnFlight != null;
                logger.log("Return flight search success -> " + airlineExistReturn);

                if (airlineExistReturn) {
                    FlightDynamicData data = new FlightDynamicData(
                            departureFlight.airlineName, returnFlight.airlineName, origin, destination,
                            departureDate, returnDate,
                            departureFlight.departureTime, departureFlight.arrivalTime,
                            returnFlight.departureTime, returnFlight.arrivalTime
                    );
                    data.airlineCodeDepart = departureFlight.marketingAirlineCode;
                    data.airlineCodeReturn = returnFlight.marketingAirlineCode;
                    data.totalTransitDepart = departureFlight.totalTransit;
                    data.totalTransitReturn = returnFlight.totalTransit;
                    return data;
                }
            }
        } catch (Exception e) {
            logger.log("Exception in validateDateRoundTrip (transit=" + transit + "): " + e.getLocalizedMessage());
            return null;
        }
        return null;
    }

    private ArrayList<String> flightSearchGetRequestItemsRoundTripDepart(FlightRoundTripSearchApi.Inputs inputs) throws ExecutionException, InterruptedException {
        FlightRoundTripSearchApi flightRoundTripSearchApi = new FlightRoundTripSearchApi(inputs);
        int retry = 5;
        while (retry>=0){
            ApiResponse flightSearchResponse = flightRoundTripSearchApi.hitApi(apiTools);

            Gson gson = new Gson();
            FlightSearchResponse flightSearchResponsePojo = gson.fromJson(flightSearchResponse.getBody(), FlightSearchResponse.class);
            if (flightSearchResponsePojo.code.equals("SUCCESS") && !flightSearchResponsePojo.data.requestItems.isEmpty()){
                logger.log("search list->" + flightSearchResponsePojo.data.searchList.departureFlights.size());
                return flightSearchResponsePojo.data.requestItems;
            }
            retry--;
        }
        return null;
    }

    private ArrayList<String> flightSearchGetRequestItemsRoundTripReturn(FlightSearchNextApi.Inputs inputs) throws ExecutionException, InterruptedException {
        FlightSearchNextApi flightRoundTripSearchApi = new FlightSearchNextApi(inputs);
        int retry = 5;
        while (retry>=0){
            ApiResponse flightSearchResponse = flightRoundTripSearchApi.hitApi(apiTools);

            Gson gson = new Gson();
            FlightSearchResponse flightSearchResponsePojo = gson.fromJson(flightSearchResponse.getBody(), FlightSearchResponse.class);
            if (flightSearchResponsePojo.code.equals("SUCCESS") && !flightSearchResponsePojo.data.requestItems.isEmpty()){
                logger.log("search list->" + flightSearchResponsePojo.data.searchList.departureFlights.size());
                return flightSearchResponsePojo.data.requestItems;
            }
            retry--;
        }
        return null;
    }

    private Pair<List<String>, List<String>> parseTags(String actualTag) {
        List<String> tagTypes = new ArrayList<>();
        List<String> tagValues = new ArrayList<>();

        if (actualTag == null || actualTag.trim().isEmpty()) {
            return new Pair<>(tagTypes, tagValues);
        }

        // Normalize outer brackets so that "[Round-trip Deals]" is treated identically to
        // "Round-trip Deals". parseDelimitedSectionToMap also strips brackets, but we still
        // want the bracket-stripped form available for the short-circuit checks below —
        // otherwise "[Round-trip Deals]" would fall through to the delimited parser and hit
        // the hyphen-as-key-value-delimiter trap.
        String unwrapped = stripOuterBrackets(actualTag);

        // Short-circuit 1: pre-mapped "type/value" pair with a known tag type
        // (e.g. "tags/SMART_TRIP", "addons/meal"). Only triggered when the type is one of the
        // recognized namespaces so unrelated strings that happen to contain "/" fall through
        // to the legacy parser.
        String[] directParts = unwrapped.split("/", 2);
        if (directParts.length == 2 && isKnownTagType(directParts[0])) {
            tagTypes.add(directParts[0].trim());
            tagValues.add(directParts[1].trim());
            return new Pair<>(tagTypes, tagValues);
        }

        // Short-circuit 2: whole input matches a known single filter name
        // (e.g. "Round-trip Deals" -> "tags/SMART_TRIP"). Avoids parseDelimitedSectionToMap
        // silently splitting on the hyphen in "Round-trip".
        String directMapped = getCorrectTagForFilter(unwrapped);
        String[] mappedParts = directMapped.split("/", 2);
        if (mappedParts.length == 2 && !directMapped.equals(unwrapped)) {
            tagTypes.add(mappedParts[0]);
            tagValues.add(mappedParts[1]);
            return new Pair<>(tagTypes, tagValues);
        }

        // Short-circuit 3: `/`-separated list of known filter names
        // (e.g. "[Round-trip Deals/Free Reschedule]" or "[Round-trip Deals/Round-trip Deals]")
        // where every token resolves. This bypasses the hyphen-as-key-value-delimiter trap
        // that parseDelimitedSectionToMap hits when every `/`-pair contains exactly one `-`.
        if (unwrapped.contains("/")) {
            String[] tokens = unwrapped.split("/");
            List<String> tmpTypes = new ArrayList<>();
            List<String> tmpValues = new ArrayList<>();
            boolean allResolved = true;
            for (String raw : tokens) {
                String token = raw.trim();
                if (token.isEmpty()) continue;
                String mapped = getCorrectTagForFilter(token);
                String[] parts = mapped.split("/", 2);
                if (parts.length == 2 && !mapped.equals(token)) {
                    tmpTypes.add(parts[0]);
                    tmpValues.add(parts[1]);
                } else if (parts.length == 2 && isKnownTagType(parts[0])) {
                    tmpTypes.add(parts[0].trim());
                    tmpValues.add(parts[1].trim());
                } else {
                    allResolved = false;
                    break;
                }
            }
            if (allResolved && !tmpTypes.isEmpty()) {
                tagTypes.addAll(tmpTypes);
                tagValues.addAll(tmpValues);
                return new Pair<>(tagTypes, tagValues);
            }
        }

        // Legacy fallback: delimited-section parser. Preserved for any historical input shape
        // (e.g. ";"- or ","-separated) that isn't covered by the short-circuits above.
        Map<String, String> tagMap = CoreFunctions.parseDelimitedSectionToMap(actualTag);
        for (String tag : tagMap.values()) {
            String mapped = getCorrectTagForFilter(tag);
            String[] parts = mapped.split("/", 2);
            if (parts.length == 2) {
                tagTypes.add(parts[0]);
                tagValues.add(parts[1]);
            }
        }
        return new Pair<>(tagTypes, tagValues);
    }


    /**
     * Searches for a flight from streaming API data using optional airline and multiple tag filters.
     * The method will retry a few times and returns the first flight that matches all specified tags
     * and airline (if provided). Supports matching multiple tags (e.g., "[meal/baggage]") — all tags must match.
     *
     * @param requestItems The list of request items (origins/destinations/dates) for the API.
     * @param streamType   The type of stream: e.g., "oneway" or "roundtrip".
     * @param returnDate   The return date (applicable if streamType is roundtrip).
     * @param authorization Bearer token or other auth header for the API.
     * @param airline      Optional airline code to filter (e.g., "QZ" for AirAsia). Empty string for no filter.
     * @param actualTag    A tag filter block like "[meal/baggage]" — multiple tags are ANDed (all must match).
     * @param childCount   Used for filtering tag logic (e.g., to skip if child not allowed).
     * @return The first matching Flight object, or null if none found after retries.
     * @throws ExecutionException if API call fails.
     * @throws InterruptedException if interrupted during retry delay.
     */
    private FlightStreamResponse.DepartureFlight flightSearchStreamByNameDynamicData(
            List<String> requestItems,
            String streamType,
            String returnDate,
            String authorization,
            String airline,
            String actualTag,
            int childCount,
            String currency,
            String deviceId
    ) throws ExecutionException, InterruptedException {

        Pair<List<String>, List<String>> tags = parseTags(actualTag);
        List<String> tagTypes = tags.first;
        List<String> tagValues = tags.second;
        
        logger.log("=== FLIGHT SEARCH FILTER PARAMETERS ===");
        logger.log("Airline filter: '" + airline + "' (empty: " + airline.isEmpty() + ")");
        logger.log("Tag filter: '" + actualTag + "' (empty: " + actualTag.isEmpty() + ")");
        logger.log("Parsed tag types: " + tagTypes);
        logger.log("Parsed tag values: " + tagValues);
        logger.log("Tag count: " + tagTypes.size());
        logger.log("=======================================");

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            logger.log("Attempt " + attempt + " to stream flight search data");

            FlightStreamingApi.Inputs inputs = new FlightStreamingApi.Inputs(requestItems, authorization, streamType, returnDate, currency, deviceId);
            FlightStreamingApi api = new FlightStreamingApi(inputs);
            ApiResponse response = api.hitApi(apiTools);

            FlightStreamResponse flightStreamResponse = new Gson().fromJson(response.getBody(), FlightStreamResponse.class);
            recordLastOneWayStreamingAirlineDirectoryIfEnabled(flightStreamResponse);

            if (flightStreamResponse != null &&
                    "SUCCESS".equals(flightStreamResponse.code) &&
                    flightStreamResponse.data != null &&
                    flightStreamResponse.data.searchList != null) {

                List<FlightStreamResponse.DepartureFlight> flights = flightStreamResponse.data.searchList.departureFlights;
                logger.log("Number of departure flights found: " + flights.size());

                Map<String, String> airlineMap = flightStreamResponse.data.airlines != null
                        ? getAirlineCodeToNameMapStream(flightStreamResponse.data.airlines)
                        : new HashMap<>();

                logger.log("Processing " + flights.size() + " flights for matching criteria");
                logger.log("Search parameters - Airline: '" + airline + "', Tag: '" + actualTag + "'");
                int flightIndex = 0;
                int matchedFlightsCount = 0;
                List<String> matchedFlightIds = new ArrayList<>();
                for (FlightStreamResponse.DepartureFlight flight : flights) {
                    flightIndex++;
                    if (flight == null) {
                        logger.log("Flight #" + flightIndex + " is null, skipping");
                        continue;
                    }

                    logger.log("--- Processing Flight #" + flightIndex + " (ID: " + flight.flightId + ", Airline Code: " + flight.marketingAirlineCode + ") ---");
                    
                    // Log flight's available tags
                    logger.log("Flight's available tags:");
                    if (flight.tags != null && !flight.tags.isEmpty()) {
                        logger.log("  - tags: " + flight.tags);
                    } else {
                        logger.log("  - tags: null or empty");
                    }
                    if (flight.labelTags != null) {
                        if (flight.labelTags.policyInfo != null && !flight.labelTags.policyInfo.isEmpty()) {
                            logger.log("  - labelTags.policyInfo: " + flight.labelTags.policyInfo);
                        }
                        if (flight.labelTags.benefitInfo != null && !flight.labelTags.benefitInfo.isEmpty()) {
                            logger.log("  - labelTags.benefitInfo: " + flight.labelTags.benefitInfo);
                        }
                    } else {
                        logger.log("  - labelTags: null");
                    }
                    if (flight.fareDetail != null) {
                        logger.log("  - fareDetail: present (used for addons matching)");
                        if (flight.fareDetail.fares != null && !flight.fareDetail.fares.isEmpty()) {
                            logger.log("    - supplierId: " + flight.fareDetail.fares.get(0).supplierId);
                        }
                    } else {
                        logger.log("  - fareDetail: null");
                    }
                    
                    // Check early return condition
                    if(airline.isEmpty() && actualTag.isEmpty() && flight.marketingAirlineCode != null && airlineMap.containsKey(flight.marketingAirlineCode)) {
                        // If no airline or tag filter, just return the flight with its airline name
                        flight.airlineName = airlineMap.get(flight.marketingAirlineCode);
                        logger.log("✓ EARLY RETURN: No filters (airline empty, tag empty), returning flight with airline name: " + flight.airlineName);
                        return flight;
                    }

                    boolean airlineMatch = false;
                    if (!airline.isEmpty()) {
                        // Only check airline match if airline filter is provided
                        airlineMatch = isAirlineMatch(flight.marketingAirlineCode, airline, airlineMap);
                        logger.log("AIRLINE MATCH RESULT: " + airlineMatch + " (airline filter: '" + airline + "')");
                    } else {
                        logger.log("Skipping airline match check (airline filter is empty)");
                    }
                    
                    boolean allTagsMatch = true;
                    if (tagTypes.isEmpty()) {
                        logger.log("No tags to match (tagTypes is empty), allTagsMatch = true");
                    } else if (!airline.isEmpty() && !airlineMatch) {
                        allTagsMatch = false;
                        logger.log("Skipping tag check (airline filter did not match)");
                    } else {
                        logger.log("Checking " + tagTypes.size() + " tag(s) for match...");
                        for (int i = 0; i < tagTypes.size(); i++) {
                            String tagType = tagTypes.get(i);
                            String tagValue = tagValues.get(i);
                            logger.log("  Tag " + (i + 1) + ": type='" + tagType + "', value='" + tagValue + "'");
                            boolean tagMatchResult = isTagMatchDepart(flight, tagType, tagValue, flightStreamResponse.data.airports, airlineMatch, childCount);
                            logger.log("  Tag " + (i + 1) + " match result: " + tagMatchResult);
                            if (!tagMatchResult) {
                                allTagsMatch = false;
                                logger.log("  ✗ Tag " + (i + 1) + " did not match, stopping tag check");
                                break;
                            } else {
                                logger.log("  ✓ Tag " + (i + 1) + " matched");
                            }
                        }
                    }
                    logger.log("ALL TAGS MATCH RESULT: " + allTagsMatch);

                    boolean isRoundAndSmartTrip = false;
                    logger.log("CHECK ROUND TRIP AND SMART TRIP: " + flight.flightId);
                    if (flight.roundTrip && flight.labelTags != null
                            && flight.labelTags.benefitInfo != null
                            && flight.labelTags.benefitInfo.contains("SMART_TRIP")) {
                        logger.log("RESULT TRUE");
                        isRoundAndSmartTrip = true;
                    }

                    // Evaluate return conditions
                    boolean condition1 = airline.isEmpty() && allTagsMatch;
                    boolean condition2 = actualTag.isEmpty() && airlineMatch;
                    boolean condition3 = airlineMatch && allTagsMatch;
                    
                    logger.log("Return condition check:");
                    logger.log("  Condition 1 (airline.isEmpty && allTagsMatch): " + condition1 + " (airline empty: " + airline.isEmpty() + ", allTagsMatch: " + allTagsMatch + ")");
                    logger.log("  Condition 2 (actualTag.isEmpty && airlineMatch): " + condition2 + " (tag empty: " + actualTag.isEmpty() + ", airlineMatch: " + airlineMatch + ")");
                    logger.log("  Condition 3 (airlineMatch && allTagsMatch): " + condition3 + " (airlineMatch: " + airlineMatch + ", allTagsMatch: " + allTagsMatch + ")");
                    
                    if (condition1 || condition2 || condition3) {
                        matchedFlightsCount++;
                        matchedFlightIds.add(flight.flightId);
                        logger.log("✓ MATCH FOUND! Flight #" + flightIndex + " meets return criteria");
                        logger.log("  Flight ID: " + flight.flightId);
                        logger.log("  Airline Code: " + flight.marketingAirlineCode);
                        logger.log("  Matched conditions: " + 
                            (condition1 ? "Condition1 " : "") + 
                            (condition2 ? "Condition2 " : "") + 
                            (condition3 ? "Condition3 " : ""));
                        
                        // Return the FIRST matching flight immediately
                        if (isRoundAndSmartTrip) {
                            flight.airlineName = airlineMap.get(flight.marketingAirlineCode);
                            logger.log("Returning flight (RoundTrip+SmartTrip) with airline name: " + flight.airlineName);
                            logger.log("=== SUMMARY: Found " + matchedFlightsCount + " matching flight(s) in this batch, returning first match ===");
                            return flight;
                        }
                        // Set airlineName before returning
                        if (flight.marketingAirlineCode != null && airlineMap.containsKey(flight.marketingAirlineCode)) {
                            flight.airlineName = airlineMap.get(flight.marketingAirlineCode);
                            logger.log("✓ Returning flight with airline name: " + flight.airlineName);
                        } else {
                            logger.log("⚠ Warning: Could not find airline name for code: " + flight.marketingAirlineCode + ", returning flight anyway");
                        }
                        logger.log("=== SUMMARY: Found " + matchedFlightsCount + " matching flight(s) in this batch, returning first match ===");
                        logger.log("=== Other matching flights (if any): " + matchedFlightIds + " ===");
                        return flight;
                    } else {
                        logger.log("✗ Flight does not meet any return criteria, continuing to next flight");
                    }
                    logger.log("--- End Flight #" + flightIndex + " ---");
                }
                
                // Log summary after processing all flights
                logger.log("=== BATCH PROCESSING SUMMARY ===");
                logger.log("Total flights processed: " + flights.size());
                logger.log("Matching flights found: " + matchedFlightsCount);
                if (matchedFlightsCount > 0) {
                    logger.log("Matched flight IDs: " + matchedFlightIds);
                } else {
                    logger.log("No matching flights found in this batch");
                }
                logger.log("Moving to next search attempt...");
                logger.log("================================");
            }
            if (flightStreamResponse != null && flightStreamResponse.data != null &&
                    flightStreamResponse.data.requestItems != null && !flightStreamResponse.data.requestItems.isEmpty()) {
                requestItems = flightStreamResponse.data.requestItems;
            }

            Thread.sleep(RETRY_DELAY_MS);
        }

        logger.log("⚠ No flight found after " + MAX_RETRIES + " attempts. Returning null.");
        logger.log("Final status - Airline: '" + airline + "', Tag: '" + actualTag + "'");
        return null;
    }

    /**
     * Transit-aware overload of {@link #flightSearchStreamByNameDynamicData}. Applies the same
     * airline and tag filters as the base method and additionally rejects any flight whose
     * {@code totalTransit} does not match the requested {@code transit} shape
     * ({@code true} = has transit, {@code false} = direct).
     */
    private FlightStreamResponse.DepartureFlight flightSearchStreamByNameDynamicData(
            List<String> requestItems,
            String streamType,
            String returnDate,
            String authorization,
            String airline,
            String actualTag,
            int childCount,
            String currency,
            String deviceId,
            boolean transit
    ) throws ExecutionException, InterruptedException {

        Pair<List<String>, List<String>> tags = parseTags(actualTag);
        List<String> tagTypes = tags.first;
        List<String> tagValues = tags.second;

        logger.log("=== FLIGHT SEARCH FILTER PARAMETERS (transit=" + transit + ") ===");
        logger.log("Airline filter: '" + airline + "' (empty: " + airline.isEmpty() + ")");
        logger.log("Tag filter: '" + actualTag + "' (empty: " + actualTag.isEmpty() + ")");
        logger.log("Transit filter required: " + transit);
        logger.log("=======================================");

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            logger.log("Attempt " + attempt + " to stream flight search data (transit=" + transit + ")");

            FlightStreamingApi.Inputs inputs = new FlightStreamingApi.Inputs(requestItems, authorization, streamType, returnDate, currency, deviceId);
            FlightStreamingApi api = new FlightStreamingApi(inputs);
            ApiResponse response = api.hitApi(apiTools);

            FlightStreamResponse flightStreamResponse = new Gson().fromJson(response.getBody(), FlightStreamResponse.class);
            recordLastOneWayStreamingAirlineDirectoryIfEnabled(flightStreamResponse);

            if (flightStreamResponse != null &&
                    "SUCCESS".equals(flightStreamResponse.code) &&
                    flightStreamResponse.data != null &&
                    flightStreamResponse.data.searchList != null) {

                List<FlightStreamResponse.DepartureFlight> flights = flightStreamResponse.data.searchList.departureFlights;
                logger.log("Number of departure flights found: " + flights.size());

                Map<String, String> airlineMap = flightStreamResponse.data.airlines != null
                        ? getAirlineCodeToNameMapStream(flightStreamResponse.data.airlines)
                        : new HashMap<>();

                int flightIndex = 0;
                for (FlightStreamResponse.DepartureFlight flight : flights) {
                    flightIndex++;
                    if (flight == null) continue;

                    boolean transitMatch = transit ? flight.totalTransit > 0 : flight.totalTransit == 0;
                    if (!transitMatch) {
                        logger.log("✗ Skip flight #" + flightIndex + " (" + flight.flightId
                                + ") - transit mismatch (required=" + transit
                                + ", totalTransit=" + flight.totalTransit + ")");
                        continue;
                    }

                    if (airline.isEmpty() && actualTag.isEmpty()
                            && flight.marketingAirlineCode != null
                            && airlineMap.containsKey(flight.marketingAirlineCode)) {
                        flight.airlineName = airlineMap.get(flight.marketingAirlineCode);
                        logger.log("✓ EARLY RETURN (transit=" + transit + ") flight " + flight.flightId
                                + " airline=" + flight.airlineName + " totalTransit=" + flight.totalTransit);
                        return flight;
                    }

                    boolean airlineMatch = false;
                    if (!airline.isEmpty()) {
                        airlineMatch = isAirlineMatch(flight.marketingAirlineCode, airline, airlineMap);
                    }

                    boolean allTagsMatch = true;
                    if (!tagTypes.isEmpty() && !airline.isEmpty() && !airlineMatch) {
                        allTagsMatch = false;
                    } else {
                        for (int i = 0; i < tagTypes.size(); i++) {
                            boolean tagMatchResult = isTagMatchDepart(flight, tagTypes.get(i), tagValues.get(i),
                                    flightStreamResponse.data.airports, airlineMatch, childCount);
                            if (!tagMatchResult) {
                                allTagsMatch = false;
                                break;
                            }
                        }
                    }

                    boolean isRoundAndSmartTrip = false;
                    if (flight.roundTrip && flight.labelTags != null
                            && flight.labelTags.benefitInfo != null
                            && flight.labelTags.benefitInfo.contains("SMART_TRIP")) {
                        isRoundAndSmartTrip = true;
                    }

                    boolean condition1 = airline.isEmpty() && allTagsMatch;
                    boolean condition2 = actualTag.isEmpty() && airlineMatch;
                    boolean condition3 = airlineMatch && allTagsMatch;

                    if (condition1 || condition2 || condition3) {
                        if (isRoundAndSmartTrip) {
                            flight.airlineName = airlineMap.get(flight.marketingAirlineCode);
                            logger.log("✓ MATCH (RoundTrip+SmartTrip, transit=" + transit + ") flight "
                                    + flight.flightId + " airline=" + flight.airlineName
                                    + " totalTransit=" + flight.totalTransit);
                            return flight;
                        }
                        if (flight.marketingAirlineCode != null
                                && airlineMap.containsKey(flight.marketingAirlineCode)) {
                            flight.airlineName = airlineMap.get(flight.marketingAirlineCode);
                        }
                        logger.log("✓ MATCH (transit=" + transit + ") flight " + flight.flightId
                                + " airline=" + flight.airlineName
                                + " totalTransit=" + flight.totalTransit);
                        return flight;
                    }
                }
            }

            if (flightStreamResponse != null && flightStreamResponse.data != null &&
                    flightStreamResponse.data.requestItems != null && !flightStreamResponse.data.requestItems.isEmpty()) {
                requestItems = flightStreamResponse.data.requestItems;
            }

            Thread.sleep(RETRY_DELAY_MS);
        }

        logger.log("⚠ No flight matching transit=" + transit + " found after " + MAX_RETRIES + " attempts. Returning null.");
        return null;
    }

    /**
     * Searches for a flight from streaming API data using optional airline and multiple tag filters.
     * The method will retry a few times and returns the first flight that matches all specified tags
     * and airline (if provided). Supports matching multiple tags (e.g., "[meal/baggage]") — all tags must match.
     *
     * @param requestItems The list of request items (origins/destinations/dates) for the API.
     * @param streamType   The type of stream: e.g., "oneway" or "roundtrip".
     * @param returnDate   The return date (applicable if streamType is roundtrip).
     * @param authorization Bearer token or other auth header for the API.
     * @param airline      Optional airline code to filter (e.g., "QZ" for AirAsia). Empty string for no filter.
     * @param actualTag    A tag filter block like "[meal/baggage]" — multiple tags are ANDed (all must match).
     * @param childCount   Used for filtering tag logic (e.g., to skip if child not allowed).
     * @return The first matching Flight object, or null if none found after retries.
     * @throws ExecutionException if API call fails.
     * @throws InterruptedException if interrupted during retry delay.
     */
    private FlightStreamResponse.ReturnFlight flightSearchStreamByNameDynamicDataReturn(
            List<String> requestItems,
            String streamType,
            String returnDate,
            String authorization,
            String airline,
            String actualTag,
            int childCount,
            String currency,
            String deviceId
    ) throws ExecutionException, InterruptedException {
        Pair<List<String>, List<String>> tags = parseTags(actualTag);
        List<String> tagTypes = tags.first;
        List<String> tagValues = tags.second;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            logger.log("Attempt " + attempt + " to stream flight search data");

            FlightStreamingApi.Inputs inputs = new FlightStreamingApi.Inputs(requestItems, authorization, streamType, returnDate, currency, deviceId);
            FlightStreamingApi api = new FlightStreamingApi(inputs);
            ApiResponse response = api.hitApi(apiTools);

            FlightStreamResponse flightStreamResponse = new Gson().fromJson(response.getBody(), FlightStreamResponse.class);

            if (flightStreamResponse != null &&
                    "SUCCESS".equals(flightStreamResponse.code) &&
                    flightStreamResponse.data != null &&
                    flightStreamResponse.data.searchList != null) {

                List<FlightStreamResponse.ReturnFlight> flights = flightStreamResponse.data.searchList.returnFlights;
                flights.addAll(flightStreamResponse.data.searchList.srtReturnFlights);

                logger.log("Number of return flights found: " + flights.size());

                Map<String, String> airlineMap = flightStreamResponse.data.airlines != null
                        ? getAirlineCodeToNameMapStream(flightStreamResponse.data.airlines)
                        : new HashMap<>();

                logger.log("DAFTAR AIRLINES");
                flightStreamResponse.data.airlines.forEach((key, value) -> {
                    logger.log(key + "-" + value.displayName);
                });

                for (FlightStreamResponse.ReturnFlight flight : flights) {
                    if (flight == null) continue;
                    if(airline.isEmpty() && actualTag.isEmpty() && flight.marketingAirlineCode != null && airlineMap.containsKey(flight.marketingAirlineCode)) {
                        // If no airline or tag filter, just return the flight with its airline name
                        flight.airlineName = airlineMap.get(flight.marketingAirlineCode);
                        logger.log("srp is not empty and airline is empty, returning flight with airline name: " + flight.airlineName);
                        return flight;
                    }

                    boolean airlineMatch = isAirlineMatch(flight.marketingAirlineCode, airline, airlineMap);
                    boolean allTagsMatch = true;

                    for (int i = 0; i < tagTypes.size(); i++) {
                        if (!isTagMatchReturn(flight, tagTypes.get(i), tagValues.get(i), flightStreamResponse.data.airports, airlineMatch, childCount)) {
                            allTagsMatch = false;
                            break;
                        }
                    }

                    if ((airline.isEmpty() && allTagsMatch) ||
                            (actualTag.isEmpty() && airlineMatch) ||
                            (airlineMatch && allTagsMatch)) {
                        flight.airlineName = airlineMap.get(flight.marketingAirlineCode);
                        return flight;
                    }
                }
            }
            if (flightStreamResponse != null && flightStreamResponse.data != null &&
                    flightStreamResponse.data.requestItems != null && !flightStreamResponse.data.requestItems.isEmpty()) {
                requestItems = flightStreamResponse.data.requestItems;
            }
            Thread.sleep(RETRY_DELAY_MS);
        }
        return null;
    }

    /**
     * Transit-aware overload of {@link #flightSearchStreamByNameDynamicDataReturn}. Applies the
     * same airline and tag filters as the base method and additionally rejects any return flight
     * whose {@code totalTransit} does not match the requested {@code transit} shape
     * ({@code true} = has transit, {@code false} = direct).
     */
    private FlightStreamResponse.ReturnFlight flightSearchStreamByNameDynamicDataReturn(
            List<String> requestItems,
            String streamType,
            String returnDate,
            String authorization,
            String airline,
            String actualTag,
            int childCount,
            String currency,
            String deviceId,
            boolean transit
    ) throws ExecutionException, InterruptedException {
        if (actualTag == null) actualTag = "";
        if (airline == null) airline = "";

        Pair<List<String>, List<String>> tags = parseTags(actualTag);
        List<String> tagTypes = tags.first;
        List<String> tagValues = tags.second;

        logger.log("=== RETURN FLIGHT SEARCH FILTER PARAMETERS (transit=" + transit + ") ===");
        logger.log("Airline filter: '" + airline + "' (empty: " + airline.isEmpty() + ")");
        logger.log("Tag filter: '" + actualTag + "' (empty: " + actualTag.isEmpty() + ")");
        logger.log("Parsed tag types: " + tagTypes + " values: " + tagValues);
        logger.log("Transit filter required: " + transit);
        logger.log("=======================================");

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            logger.log("Attempt " + attempt + " to stream return flight search data (transit=" + transit + ")");

            FlightStreamingApi.Inputs inputs = new FlightStreamingApi.Inputs(requestItems, authorization, streamType, returnDate, currency, deviceId);
            FlightStreamingApi api = new FlightStreamingApi(inputs);
            ApiResponse response = api.hitApi(apiTools);

            FlightStreamResponse flightStreamResponse = new Gson().fromJson(response.getBody(), FlightStreamResponse.class);

            if (flightStreamResponse != null &&
                    "SUCCESS".equals(flightStreamResponse.code) &&
                    flightStreamResponse.data != null &&
                    flightStreamResponse.data.searchList != null) {

                List<FlightStreamResponse.ReturnFlight> flights = flightStreamResponse.data.searchList.returnFlights;
                flights.addAll(flightStreamResponse.data.searchList.srtReturnFlights);

                logger.log("Number of return flights found: " + flights.size());

                Map<String, String> airlineMap = flightStreamResponse.data.airlines != null
                        ? getAirlineCodeToNameMapStream(flightStreamResponse.data.airlines)
                        : new HashMap<>();

                for (FlightStreamResponse.ReturnFlight flight : flights) {
                    if (flight == null) continue;

                    boolean transitMatch = transit ? flight.totalTransit > 0 : flight.totalTransit == 0;
                    if (!transitMatch) {
                        logger.log("✗ Skip return flight " + flight.flightId
                                + " - transit mismatch (required=" + transit
                                + ", totalTransit=" + flight.totalTransit + ")");
                        continue;
                    }

                    if (airline.isEmpty() && actualTag.isEmpty()
                            && flight.marketingAirlineCode != null
                            && airlineMap.containsKey(flight.marketingAirlineCode)) {
                        flight.airlineName = airlineMap.get(flight.marketingAirlineCode);
                        logger.log("✓ EARLY RETURN (transit=" + transit + ") return flight "
                                + flight.flightId + " airline=" + flight.airlineName
                                + " totalTransit=" + flight.totalTransit);
                        return flight;
                    }

                    boolean airlineMatch = isAirlineMatch(flight.marketingAirlineCode, airline, airlineMap);
                    boolean allTagsMatch = true;

                    for (int i = 0; i < tagTypes.size(); i++) {
                        if (!isTagMatchReturn(flight, tagTypes.get(i), tagValues.get(i),
                                flightStreamResponse.data.airports, airlineMatch, childCount)) {
                            allTagsMatch = false;
                            break;
                        }
                    }

                    if ((airline.isEmpty() && allTagsMatch) ||
                            (actualTag.isEmpty() && airlineMatch) ||
                            (airlineMatch && allTagsMatch)) {
                        flight.airlineName = airlineMap.get(flight.marketingAirlineCode);
                        logger.log("✓ MATCH return (transit=" + transit + ") flight " + flight.flightId
                                + " airline=" + flight.airlineName
                                + " totalTransit=" + flight.totalTransit);
                        return flight;
                    }
                }
            }
            if (flightStreamResponse != null && flightStreamResponse.data != null &&
                    flightStreamResponse.data.requestItems != null && !flightStreamResponse.data.requestItems.isEmpty()) {
                requestItems = flightStreamResponse.data.requestItems;
            }
            Thread.sleep(RETRY_DELAY_MS);
        }
        logger.log("⚠ No return flight matching transit=" + transit + " found after " + MAX_RETRIES + " attempts. Returning null.");
        return null;
    }

    private boolean isAirlineMatch(String airlineCode, String expectedAirline, Map<String, String> airlineMap) {
        logger.log("IS AIRLINE MATCH " + airlineCode + " " + expectedAirline);
        if (expectedAirline == null || expectedAirline.isEmpty()) return false;
        if (!airlineMap.containsKey(airlineCode)) return false;

        String actualAirlineName = airlineMap.get(airlineCode);
        return matchAirline(actualAirlineName, expectedAirline);
    }

    private boolean isTagMatchDepart(FlightStreamResponse.DepartureFlight flight, String tagType, String tagValue,
                                     Map<String, FlightStreamResponse.AirportDetail> flightStreamResponse,
                                     boolean airlineMatch, int childCount) throws ExecutionException, InterruptedException {
        if (tagValue == null || tagValue.isEmpty() || flight == null) return false;

        logger.log("    Attempting to match tagType='" + tagType + "' with tagValue='" + tagValue + "'");

        if ("tags".equalsIgnoreCase(tagType)) {
            if (isSmartTripTag(tagValue)) {
                logger.log("    Checking flight.tags and flight.labelTags.benefitInfo for SMART_TRIP");
                return matchSmartTrip(flight.tags, flight.labelTags);
            }
            logger.log("    Checking flight.tags against tagValue");
            return matchTags(flight.tags, tagValue);
        } else if ("labelTags".equalsIgnoreCase(tagType)) {
            logger.log("    Checking flight.labelTags against tagValue");
            return matchLabelTags(flight.labelTags, tagValue);
        } else if ("addons".equalsIgnoreCase(tagType)) {
            logger.log("    Checking addons - airlineMatch: " + airlineMatch + " (optional for addons matching)");
            logger.log("    Checking flight.fareDetail for addons");
            return matchAddons(flight.departureAirportCode, flight.arrivalAirportCode, flight.marketingAirlineCode,
                    flight.fareDetail, tagValue, flightStreamResponse, childCount);
        } else if ("promoData".equalsIgnoreCase(tagType)) {
            logger.log("    Checking flight.fareDetail for promoData");
            return matchPromoData(flight.fareDetail);
        }

        logger.log("    ✗ Unknown tagType: " + tagType);
        return false;
    }

    private boolean isTagMatchReturn(FlightStreamResponse.ReturnFlight flight, String tagType, String tagValue,
                                     Map<String, FlightStreamResponse.AirportDetail> flightStreamResponse,
                                     boolean airlineMatch, int childCount) throws ExecutionException, InterruptedException {
        if (tagValue == null || tagValue.isEmpty() || flight == null) return false;

        logger.log("    Attempting to match tagType='" + tagType + "' with tagValue='" + tagValue + "' (RETURN)");

        if ("tags".equalsIgnoreCase(tagType)) {
            if (isSmartTripTag(tagValue)) {
                logger.log("    Checking flight.tags and flight.labelTags.benefitInfo for SMART_TRIP (RETURN)");
                return matchSmartTrip(flight.tags, flight.labelTags);
            }
            logger.log("    Checking flight.tags against tagValue");
            return matchTags(flight.tags, tagValue);
        } else if ("labelTags".equalsIgnoreCase(tagType)) {
            logger.log("    Checking flight.labelTags against tagValue");
            return matchLabelTags(flight.labelTags, tagValue);
        } else if ("addons".equalsIgnoreCase(tagType)) {
            logger.log("    Checking addons - airlineMatch: " + airlineMatch + " (optional for addons matching)");
            logger.log("    Checking flight.fareDetail for addons");
            return matchAddons(flight.departureAirportCode, flight.arrivalAirportCode, flight.marketingAirlineCode,
                    flight.fareDetail, tagValue, flightStreamResponse, childCount);
        } else if ("promoData".equalsIgnoreCase(tagType)) {
            logger.log("    Checking flight.fareDetail for promoData");
            return matchPromoData(flight.fareDetail);
        }

        logger.log("    ✗ Unknown tagType: " + tagType);
        return false;
    }

    private boolean matchTags(List<String> tags, String tagValue) {
        if (tags != null) {
            logger.log("Matching tags: " + tags.toString() + " with tagValue: " + tagValue);
            for (String tag : tags) {
                if (tag != null && normalizeTag(tag).equals(normalizeTag(tagValue))) {
                    logger.log("Tag match found: " + tag);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchLabelTags(FlightStreamResponse.LabelTags labelTags, String tagValue) {
        if (labelTags != null && labelTags.policyInfo != null) {
            logger.log("Matching tags: " + labelTags.policyInfo + " with tagValue: " + tagValue);
            for (String label : labelTags.policyInfo) {
                if (label != null && normalizeTag(label).equals(normalizeTag(tagValue))) {
                    logger.log("Label tag match found: " + label);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true when the requested tag value is the SMART_TRIP marker used by the
     * "Round-trip Deals" filter. SMART_TRIP is the only tag value that conceptually lives
     * on both {@code flight.tags} and {@code labelTags.benefitInfo} depending on whether
     * the API marshals the flight as a standalone fare or a round-trip bundle — so we
     * recognize it explicitly to look in both places.
     */
    private boolean isSmartTripTag(String tagValue) {
        return tagValue != null && "SMART_TRIP".equalsIgnoreCase(tagValue.trim());
    }

    /**
     * SMART_TRIP matcher that succeeds when either {@code flight.tags} contains {@code SMART_TRIP}
     * or {@code labelTags.benefitInfo} contains {@code SMART_TRIP}. Return flights returned by the
     * {@code RT_RETURN} stream frequently carry the smart-trip benefit only on
     * {@code labelTags.benefitInfo}, while depart flights carry it on {@code tags}; this helper
     * unifies both shapes so the same filter input matches on both legs.
     */
    private boolean matchSmartTrip(List<String> tags, FlightStreamResponse.LabelTags labelTags) {
        if (matchTags(tags, "SMART_TRIP")) return true;
        if (labelTags != null && labelTags.benefitInfo != null) {
            logger.log("Matching labelTags.benefitInfo: " + labelTags.benefitInfo + " with tagValue: SMART_TRIP");
            for (String info : labelTags.benefitInfo) {
                if (info != null && normalizeTag(info).equals(normalizeTag("SMART_TRIP"))) {
                    logger.log("SMART_TRIP match found in labelTags.benefitInfo: " + info);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchAddons(String originCode, String destCode, String marketingAirlineCode,
                                FlightStreamResponse.FareDetail fareDetail, String tagValue,
                                Map<String, FlightStreamResponse.AirportDetail> airportMap, int childCount)
            throws ExecutionException, InterruptedException {
        if (fareDetail != null && fareDetail.fares != null && !fareDetail.fares.isEmpty()) {
            String supplierCode = fareDetail.fares.get(0).supplierId;
            if (supplierCode != null) {
                supplierCode = supplierCode.replaceAll("-ENC$", "");
                String originCountry = airportMap.get(originCode).countryCode;
                String destCountry = airportMap.get(destCode).countryCode;
                originCountry = destCountry =
                        ("ID".equalsIgnoreCase(originCountry) && "ID".equalsIgnoreCase(destCountry)) ? "ID" : "*";
                return checkAddons(originCountry, destCountry, marketingAirlineCode, tagValue, supplierCode, childCount);
            }
        }
        return false;
    }

    private boolean matchPromoData(FlightStreamResponse.FareDetail fareDetail) {
        if (fareDetail != null && fareDetail.fares != null) {
            for (FlightStreamResponse.Fare fare : fareDetail.fares) {
                if (fare.promoData != null && fare.promoData.promoCode != null && !fare.promoData.promoCode.isEmpty()) {
                    logger.log("Promo code match found: " + fare.promoData.promoCode);
                    return true;
                }
            }
        }
        return false;
    }


    private boolean checkAddons(String countryCodeOrigin,String countryCodeDestination,String airlineCode,String tag,String supplierCode,int childCount) throws ExecutionException, InterruptedException{
        // Prepare input for decrypt API
        FlightDecryptApi.Inputs decryptInputs = new FlightDecryptApi.Inputs(supplierCode);
        FlightDecryptApi decryptApi = new FlightDecryptApi(decryptInputs);
        try {
            ApiResponse decryptResponse = decryptApi.hitApi(apiTools);
            JSONObject jsonObject = new JSONObject(decryptResponse.getBody());
            supplierCode = jsonObject.optString("data", supplierCode);
        }catch (Exception e){
            logger.log("Error decrypting supplier code: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Failed to decrypt supplier code: " + e.getMessage());
            return false;
        }

        FormGeneratorsApi.Inputs formGeneratorsApiInputs = new FormGeneratorsApi.Inputs(
                airlineCode, countryCodeOrigin, countryCodeDestination, supplierCode);
        FormGeneratorsApi formGeneratorsApi = new FormGeneratorsApi(formGeneratorsApiInputs);
        ApiResponse formGeneratorsApiRes = formGeneratorsApi.hitApi(apiTools);
        FormGeneratorsApiResponse formGeneratorsResponse = new Gson().fromJson(formGeneratorsApiRes.getBody(), FormGeneratorsApiResponse.class);
        if (formGeneratorsResponse.code.equals("SUCCESS") && formGeneratorsResponse.data != null && formGeneratorsResponse.data.content != null) {
            String normalizedTag = tag.toLowerCase();
            List<String> adults = formGeneratorsResponse.data.content.get(0).formSectionType.adults;
            List<String> childs = formGeneratorsResponse.data.content.get(0).formSectionType.childs;
            return switch (normalizedTag) {
                case "meal" -> childCount > 0
                        ? (containsAllTags(adults, "departureMeal", "returnMeal")
                        && containsAllTags(childs != null ? childs : Collections.emptyList(), "departureMeal", "returnMeal"))
                        : containsAllTags(adults, "departureMeal", "returnMeal");
                case "baggage" -> childCount > 0
                        ? (containsAllTags(adults, "departureBaggage", "returnBaggage")
                        && containsAllTags(childs != null ? childs : Collections.emptyList(), "departureBaggage", "returnBaggage"))
                        : containsAllTags(adults, "departureBaggage", "returnBaggage");
                case "seat" -> childCount > 0
                        ? (containsAllTags(adults, "departureSeat", "returnSeat")
                        && containsAllTags(childs != null ? childs : Collections.emptyList(), "departureSeat", "returnSeat"))
                        : containsAllTags(adults, "departureSeat", "returnSeat");
                default -> false;
            };
        }
        return false;
    }

    private boolean containsAllTags(List<String> list, String... tags) {
        for (String tag : tags) {
            if (!list.contains(tag)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Normalize a tag by removing spaces and underscores, and converting to lower case.
     * @param tag the tag to normalize
     * @return normalized tag string
     */
    private String normalizeTag(String tag) {
        if (tag == null) return null;
        return tag.replaceAll("[ _]", "").toLowerCase();
    }

    public boolean checkFlightLiveActivity(
            String origin,
            String destination,
            String flightNumber,
            String departureDate,
            String actionType
    ) {
        FlightLiveActivityJobApi.Inputs input =
                new FlightLiveActivityJobApi.Inputs(
                        origin.toUpperCase(),
                        destination.toUpperCase(),
                        flightNumber,
                        departureDate,
                        actionType
                );
        FlightLiveActivityJobApi liveActivityApi = new FlightLiveActivityJobApi(input);
        ApiResponse response = liveActivityApi.hitApi(apiTools);
        boolean responseStatusIsSuccess = response.getStatusCode() == 200;
        boolean responseBodyIsEmpty = response.getBody() == null || response.getBody().trim().isEmpty();

        return responseStatusIsSuccess && responseBodyIsEmpty;
    }

    public boolean updateSpecificFlightStatusViaCallback(
            String origin,
            String destination,
            String flightNumber,
            String departurePlanDate,
            String arrivalPlanDate,
            String flightState
    ) {
        FlightStatusVariflightCallbackManualApi.Inputs input =
                new FlightStatusVariflightCallbackManualApi.Inputs(
                origin,
                destination,
                flightNumber,
                departurePlanDate,
                arrivalPlanDate,
                flightState
        );

        FlightStatusVariflightCallbackManualApi variflightCallbackApi = new FlightStatusVariflightCallbackManualApi(input);
        ApiResponse response = variflightCallbackApi.hitApi(apiTools);
        return response.getStatusCode() == 200;
    }

    public FlightOrderDetailResponse getOrderDetail(String orderId) {
        return getOrderDetail(orderId, null);
    }

    public FlightOrderDetailResponse getOrderDetail(String orderId, String email) {
        FlightGetOrderApi.Inputs input = new FlightGetOrderApi.Inputs(orderId, email);
        FlightGetOrderApi getOrderApi = new FlightGetOrderApi(input);
        ApiResponse response = getOrderApi.hitApi(apiTools);
        return new Gson().fromJson(response.getBody(), FlightOrderDetailResponse.class);
    }
}
