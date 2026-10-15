package com.tiket.page.flight;

import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.b2b.PostInterimDetailsApi;
import com.tiket.api.b2b.PostInterimTransactionsApi;
import com.tiket.api.flight.*;
import com.tiket.api.flight.dashboard.FlightEticketIssueApi;
import com.tiket.api.flight.dashboard.FlightGetBookingCodeApi;
import com.tiket.api.payment.PaidCheckerApi;
import com.tiket.api.payment.SubmitPaymentApiB2B;
import com.tiket.api.payment.SubmitPaymentPutApiB2B;
import com.tiket.api.payment.SubmitSakukuApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.corporate.login.CorporateLoginPage;
import com.tiket.page.flight.searchresultpage.FlightSearchResultPage;
import com.tiket.page.utilities.discovery.promo.PromoPage;
import com.tiket.pojo.b2b.corporate.EmployeeData;
import com.tiket.pojo.b2b.corporate.PostInterimDetailsApiFlight;
import com.tiket.pojo.b2b.corporate.PostInterimDetailsApiResponse;
import com.tiket.pojo.b2b.corporate.PostInterimTransactionsApiBodyFlight;
import com.tiket.pojo.flight.*;
import com.tiket.pojo.payment.PaidCheckerResponse;
import com.tiket.pojo.payment.SubmitSakukuResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.log;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public abstract class FlightPage <T extends WebDriver> extends BasePage<T> {

    public FlightPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {
                return new FlightPageIOS<>(session);
            }
            case ANDROID -> {
                return new FlightPageAndroid<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private String finalDepartureDate;
    private String finalReturnDate;
    private String finalAirline;
    private String finalAirlineRet;
    String fullName;
    String title;
    int paxNumber;
    private String finalOrigin;
    private String finalDestination;

    private HashMap<String,String> getAirlineCodeToNameMap(HashMap<String,FlightSearchResponse.AirlineDetail> airlineDetail){
        HashMap<String,String> mp = new HashMap<>();
        for (String key : airlineDetail.keySet()){
            String displayName = airlineDetail.get(key).displayName;
            mp.put(key,displayName);
        }
        return mp;
    }

    private boolean matchAirline(String airlineNameActual,String airlineNameExpected){
        String[] airlineExpected = airlineNameExpected.split(" ");
        String[] airlineActual = airlineNameActual.split(" ");
        //logger.log("expected airline name -> " + airlineNameExpected.toLowerCase() + " actual airline name ->" + airlineNameActual.toLowerCase());
        boolean flag = airlineExpected.length == airlineActual.length;

        if (flag) {
            for (int i = 0; i < airlineExpected.length; i++) {
                flag = flag & airlineActual[i].equalsIgnoreCase(airlineExpected[i]);
            }
        }
        return flag;
    }

    private FlightSearchResponse.Flight flightSearchByName(FlightSearchApi.Inputs inputs,String airline) throws ExecutionException, InterruptedException {
        FlightSearchApi flightSearchApi = new FlightSearchApi(inputs);
        ApiResponse flightSearchResponse = flightSearchApi.hitApi(apiTools);

        Gson gson = new Gson();
        FlightSearchResponse flightSearchResponsePojo = gson.fromJson(flightSearchResponse.getBody(), FlightSearchResponse.class);

        if(flightSearchResponsePojo.code.equals("SUCCESS")) {
            logger.log("search list->" + flightSearchResponsePojo.data.searchList.departureFlights.size());
            logger.log("searchList->" + flightSearchResponsePojo.data.toString());
            HashMap<String, String> airlineMap = getAirlineCodeToNameMap(flightSearchResponsePojo.data.airlines);
            for (FlightSearchResponse.Flight depFlight : flightSearchResponsePojo.data.searchList.departureFlights) {
                String airlineCode = depFlight.marketingAirlineCode;
                if (matchAirline(airlineMap.get(airlineCode), airline))
                    return depFlight;
                }
        }
        return null;
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
                    logger.log("search response -> " + flightSearchResponse.getBody());
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

    private FlightSearchResponse.Flight flightSearchByNameUsingStream(FlightSearchApi.Inputs inputs,String airline, String authorization,String currency,String deviceId) throws ExecutionException, InterruptedException {
        ArrayList<String> requestItems = flightSearchGetRequestItems(inputs);
        logger.log("requestItems->" + requestItems);
        FlightStreamingApi.Inputs flightStreamingApiInputs = new FlightStreamingApi.Inputs(requestItems,authorization,"OW","", currency, deviceId);
        return flightSearchStreamByName(flightStreamingApiInputs,airline);
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

    private FlightSearchResponse.Flight flightSearchStreamByName(FlightStreamingApi.Inputs inputs,String airline) throws ExecutionException, InterruptedException{
       FlightStreamingApi flightStreamingApi = new FlightStreamingApi(inputs);
       int retry = 10;
       while (retry>=0){
           ApiResponse flightStreamingResponse = flightStreamingApi.hitApi(apiTools);

           Gson gson = new Gson();
           FlightSearchResponse flightSearchResponsePojo = gson.fromJson(flightStreamingResponse.getBody(), FlightSearchResponse.class);

              if(flightSearchResponsePojo.code.equals("SUCCESS")){
                  logger.log("searchList->" + flightSearchResponsePojo.data.searchList.departureFlights.size());
                  HashMap<String, String> airlineMap = getAirlineCodeToNameMap(flightSearchResponsePojo.data.airlines);
                  for (FlightSearchResponse.Flight depFlight : flightSearchResponsePojo.data.searchList.departureFlights) {
                      String airlineCode = depFlight.marketingAirlineCode;
                      if (matchAirline(airlineMap.get(airlineCode), airline))
                          return depFlight;
                  }
              }
              Thread.sleep(5000);
           retry--;
       }
       return null;
    }

    public String selectOriginAndDestinationType(String loc){
        return switch (loc.length()) {
            case 3 -> "AIRPORT";
            case 4 -> "CITY";
            default -> throw new NotImplementedException("Unimplemented location type!");
        };
    }

    public FlightAutocompleteResponse.Airport getAirportData(String airportName) {
        try{
            String accessToken = getToken();
            logger.log("accessToken-> " + accessToken);

            FlightAutocompleteApi.Inputs inputs = new FlightAutocompleteApi.Inputs(airportName,accessToken);
            FlightAutocompleteResponse flightAutocompleteResponse = flightAutocompleteApi(inputs);
            List<FlightAutocompleteResponse.Airport> airports = flightAutocompleteResponse.data.airports;
            for (FlightAutocompleteResponse.Airport airport : airports) {
                logger.log("Airport Name->" + airport.airportName + ", Airport Code->" + airport.airportCode);
                if (airport.airportName.toLowerCase().contains(airportName.toLowerCase())) {
                    return airport;
                }
            }
        }
        catch (Exception e) {
            logger.log("Error occurred: "+ e);
            Assertion.skip("Flight Booking via API failed. Skipping the subsequent steps.");
        }
        return null;
    }

    private FlightAutocompleteResponse flightAutocompleteApi (FlightAutocompleteApi.Inputs inputs) throws ExecutionException,InterruptedException {
        FlightAutocompleteApi flightAutocompleteApi = new FlightAutocompleteApi(inputs);
        ApiResponse response = flightAutocompleteApi.hitApi(apiTools);
        JSONObject jsonObject = new JSONObject(response.getBody());
        log("Flight Autocomplete API response->" + jsonObject);
        Gson gson = new Gson();
        return gson.fromJson(response.getBody(), FlightAutocompleteResponse.class);
    }

    public String getDate(String data) {
        HashMap<String, String> map = new HashMap<>();
        map.put("departureDate", finalDepartureDate);
        map.put("returnDate", finalReturnDate);
        map.put("airline", finalAirline);
        map.put("airlineRet", finalAirlineRet);
        map.put("originAirport", finalOrigin);
        map.put("destinationAirport", finalDestination);
        return map.get(data);
    }

    public void setDataOneWay(
            int daysToDepartureDate,
            String airline
    ){
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        finalDepartureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        finalAirline = airline;
    }

    public void setDataRoundTrip(
            int daysToDepartureDate,
            int duration,
            String airline,
            String airlineReturn
    ){
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        finalDepartureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);
        finalReturnDate = CoreFunctions.addToDateYYYY_MM_DD(finalDepartureDate, duration);
        finalAirline = airline;
        finalAirlineRet = airlineReturn;
    }

    public void checkWebview(){
        driverTools.switchToWebView();
        String getUrl = driverTools.getCurrentURL();
        Assertion.assertThat("redirected to different page that is "+ getUrl,getUrl,is(containsString("info")));
        driverTools.switchToNative();
    }

    public boolean checkNative(){
        PromoPage<T> promoPage = PromoPage.getInstance(session);
        boolean isOnPromoPage = promoPage.isPromoListPageValid();
        Assertion.assertThat("is redirected to promo page",isOnPromoPage,is(true));
        return isOnPromoPage;
    }

    public void floatingIconRedirection(){
        FlightSearchResultPage<T> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
        flightSearchResultPage.waitForFloatingIconToDisappeared();
        // Newer Android SRP: sticky promo banner Apply keeps user on SRP with Cancel / applied state.
        if (flightSearchResultPage.isPromoInfinityEntryHandled()) {
            Assertion.assertThat("Promo infinity applied on SRP", true, is(true));
            return;
        }
        if(!checkNative()){
            checkWebview();
        }
        flightSearchResultPage.backFromFloatingIcon();
    }

    public void enableDontKeepActivities() {
        driverTools.enableDontKeepActivities();
    }

    @SneakyThrows
    public String createOrderFlightB2BDomestic(
            String username,
            String password,
            String originStation,
            String destinationStation,
            String departureDate,
            String cabinClass,
            String originType,
            String destinationType,
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
            CorporateLoginPage<T> corporateLoginPage = CorporateLoginPage.getInstance(session);
            String loginAccessToken = corporateLoginPage.getAccessTokenB2BCorporate(username,password);
            String accessToken = loginAccessToken;
            loginAccessToken = "Bearer " + loginAccessToken;
            logger.log("accessToken for dashboard request->" + loginAccessToken);

            String deviceId = CoreFunctions.generateDeviceId();
            //flight search api
            FlightSearchApi.Inputs flightSearchApiInputs = new FlightSearchApi.Inputs(
                    originStation.toUpperCase(), destinationStation.toUpperCase(),selectOriginAndDestinationType(originStation) , selectOriginAndDestinationType(destinationStation), departureDate, adult, child, infant,
                    normalizeSearchType(searchType),loginAccessToken,"IDR",deviceId
            );
            FlightSearchResponse.Flight departureFlight = flightSearchByName(flightSearchApiInputs,airline);
            if(departureFlight==null){
                departureFlight =  flightSearchByNameUsingStream(flightSearchApiInputs,airline,loginAccessToken,"IDR",deviceId);
            }
            logger.log("Flight Search API response->" + (departureFlight != null));
            String flightId = Objects.requireNonNull(departureFlight).flightId;
            logger.log("Flight ID----->"+flightId);
            String supplierID = departureFlight.fareDetail.fares.get(0).supplierId;
            String SUPPLIER_ID_ENCRYPTED_SUFFIX_TEXT = "-ENC";
            if (supplierID != null && supplierID.endsWith(SUPPLIER_ID_ENCRYPTED_SUFFIX_TEXT)) {
                supplierID = supplierID.substring(0, supplierID.length() - SUPPLIER_ID_ENCRYPTED_SUFFIX_TEXT.length());
            }
            logger.log("Supplier ID----->"+supplierID);

            //flight cart post api to get cart id

            FlightCartApi.Inputs flightCartApiInputs = new FlightCartApi.Inputs(flightId, supplierID, accessToken);
            String cartId = flightCartApi(flightCartApiInputs);
            log("Cart ID---->"+cartId);

            FlightAutocompleteResponse.Airport originData = getAirportData(originStation);
            FlightAutocompleteResponse.Airport destinationData = getAirportData(destinationStation);
            PostInterimDetailsApiFlight postInterimDetailsApiFlight = getBodyPostInterimDetailsApiFlight(cartId,originData,destinationData,"false",departureDate,"");
            PostInterimDetailsApi.Inputs postInterimDetailsApi = new PostInterimDetailsApi.Inputs(accessToken,null,postInterimDetailsApiFlight);
            PostInterimDetailsApiResponse postInterimDetailsApiResponse = postInterimDetail(postInterimDetailsApi);

            PostInterimTransactionsApiBodyFlight postInterimTransactionsApiBodyFlight = getBodyPostInterimTransactionsApiBodyFlight(postInterimDetailsApiFlight);
            PostInterimTransactionsApi.Inputs postInterimTransactionsApiInputs = new PostInterimTransactionsApi.Inputs(accessToken,null,postInterimTransactionsApiBodyFlight);
            String corporateInterimID = postInterimTransactions(postInterimTransactionsApiInputs);
            logger.log("corporateInterimID-> " + corporateInterimID);

            String phoneNumber = corporateLoginPage.getUserDataB2B(username, password, "accountPhoneNumber");

            //flight booking api
            FlightBookingAPiDomesticCorporate.Inputs flightBookingApiInputs = new FlightBookingAPiDomesticCorporate.Inputs(cartId, username, accessToken, passengerNames, phoneNumber,corporateInterimID);
            FlightBookingDetailDomestic flightBookingDetailDomestic = flightBookingApiDomesticCorporate(flightBookingApiInputs);
            log("Flight Booking API response->" + flightBookingDetailDomestic);
            long orderId = flightBookingDetailDomestic.data.successData.orderId;
            String orderHash = flightBookingDetailDomestic.data.successData.orderHash;
            logger.log("orderId: " + orderId);
            logger.log("orderHash: " + orderHash);
            fullName = passengerNames[0];

            //b2b payment api
            SubmitPaymentApiB2B.Inputs submitPaymentApiInputs = new SubmitPaymentApiB2B.Inputs(accessToken, orderHash, orderId, employeeData.data.employeeID,employeeData.data.corporateID);
            String paymentResponse = submitPaymentB2B(submitPaymentApiInputs);
            logger.log("paymentStatus"+ paymentResponse);

            SubmitPaymentPutApiB2B.Inputs submitPaymentPutApiInputs = new SubmitPaymentPutApiB2B.Inputs(accessToken,orderHash, orderId, employeeData.data.employeeID,employeeData.data.corporateID, "testing only");
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

    private PostInterimDetailsApiResponse postInterimDetail(PostInterimDetailsApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PostInterimDetailsApi postInterimDetailsApi = new PostInterimDetailsApi(inputs);
        ApiResponse postInterimDetailsApiResponse = postInterimDetailsApi.hitApi(apiTools);
        Gson gson = new Gson();
        PostInterimDetailsApiResponse postInterimDetailsApiResponse1 = gson.fromJson(postInterimDetailsApiResponse.getBody(),PostInterimDetailsApiResponse.class);
        logger.log("postInterimDetailsApiResponse1 -> " + postInterimDetailsApiResponse1.code);
        return postInterimDetailsApiResponse1;
    }

    private String postInterimTransactions(PostInterimTransactionsApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PostInterimTransactionsApi postInterimTransactionsApi = new PostInterimTransactionsApi(inputs);
        ApiResponse apiResponse = postInterimTransactionsApi.hitApi(apiTools);
        JSONObject object = new JSONObject(apiResponse.getBody());
        logger.log("res-> " + object);
        return object.getJSONObject("data").getString("corporateInterimID");
    }

    private String flightCartApi(FlightCartApi.Inputs inputs) throws ExecutionException, InterruptedException {
        FlightCartApi flightCartApi = new FlightCartApi(inputs, null);
        ApiResponse flightCartResponse = flightCartApi.hitApi(apiTools);
        log("Flight Cart API response->" + flightCartResponse.getBody());
        Gson gson = new Gson();
        FlightCartId flightCartId = gson.fromJson(flightCartResponse.getBody(), FlightCartId.class);
        logger.log("cartId->" + flightCartId.data.cartId);
        return flightCartId.data.cartId;
    }

    private FlightBookingDetail flightBookingApi(FlightBookingApi.Inputs inputs) throws ExecutionException, InterruptedException {
        FlightBookingApi flightBookingApi = new FlightBookingApi(inputs);
        ApiResponse flightBookingResponse = flightBookingApi.hitApi(apiTools);
        Gson gson = new Gson();
        FlightBookingDetail flightBookingDetail = gson.fromJson(flightBookingResponse.getBody(), FlightBookingDetail.class);
        logger.log("Flight Booking API response->" + flightBookingDetail.toString());
        return flightBookingDetail;
    }

    private FlightBookingDetailDomestic flightBookingApiDomestic(FlightBookingApiDomestic.Inputs inputs) throws ExecutionException, InterruptedException {
        FlightBookingApiDomestic flightBookingApi = new FlightBookingApiDomestic(inputs);
        ApiResponse flightBookingResponse = flightBookingApi.hitApi(apiTools);
        Gson gson = new Gson();
        FlightBookingDetailDomestic flightBookingDetailDomestic = gson.fromJson(flightBookingResponse.getBody(), FlightBookingDetailDomestic.class);
        logger.log("Flight Booking API response->" + flightBookingResponse.getBody());

        return flightBookingDetailDomestic;
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

    @SneakyThrows
    public String getBookingCode(String orderID, String email) {
        FlightGetBookingCodeApi.Inputs inputs = new FlightGetBookingCodeApi.Inputs(orderID, email, "1");
        FlightGetBookingCodeApi flightGetBookingCodeApi = new FlightGetBookingCodeApi(inputs);
        for (int tries = 0; tries < 5; tries++) {
            ApiResponse apiResponse = flightGetBookingCodeApi.hitApi(apiTools);
            Gson gson = new Gson();
            FlightBookingCodeDetail response = gson.fromJson(apiResponse.getBody(), FlightBookingCodeDetail.class);
            if (!response.data.content.isEmpty()) {
                String bookingCode = response.data.content.get(0).bookingCode;
                logger.log("Booking code -> " + bookingCode);
                return bookingCode;
            }
            Thread.sleep(2000);
        }
        return null;
    }

    @SneakyThrows
    public Boolean flightEticketIssuedRequest(String orderId, String bookingCode, String email){
       try {
            String[] passengerNames = {fullName};
            FlightEticketIssueApi.Inputs inputs = new FlightEticketIssueApi.Inputs(orderId, bookingCode, email, passengerNames, "mr", 0);
            FlightEticketIssueApi flightEticketIssueApi = new FlightEticketIssueApi(inputs);
            ApiResponse apiResponse = flightEticketIssueApi.hitApi(apiTools);
            JSONObject response = new JSONObject(apiResponse.getBody());
            logger.log("Eticket issue api response -> " +response.toString());

       } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("ETicket Not issued. Skipping the subsequent steps.");
       }

        return true;
    }

    @SneakyThrows
    public Boolean flightEticketIssuedRequest(String orderId, String bookingCode, String email, String[] passengerNames){
        try {
            FlightEticketIssueApi.Inputs inputs = new FlightEticketIssueApi.Inputs(orderId, bookingCode, email, passengerNames, "mr", 0);
            FlightEticketIssueApi flightEticketIssueApi = new FlightEticketIssueApi(inputs);
            ApiResponse apiResponse = flightEticketIssueApi.hitApi(apiTools);
            JSONObject response = new JSONObject(apiResponse.getBody());
            logger.log("Eticket issue api response -> " +response.toString());

        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("ETicket Not issued. Skipping the subsequent steps.");
        }

        return true;
    }

    /**
     * This method is used to get a token.
     *
     * @return String Returns the access token.
     * @throws Exception
     */
    private String getToken() throws Exception {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
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
                normalizeSearchType(searchType), accessToken,"IDR", deviceId
        );

        FlightSearchResponse.Flight departureFlight = flightSearchByName(flightSearchApiInputs,airline,10);
        if(departureFlight==null){
            // The synchronous /v3/search endpoint can return `searchCompleted:false`,
            // empty `departureFlights`, and a list of streaming `requestItems`. In that
            // mode the non-streaming retry loop will never see flights — they are
            // delivered through the streaming endpoint. Mirror the B2B path
            // (createOrderFlightB2BDomestic) and fall back to the streaming search.
            logger.log("Non-streaming search returned no flights; falling back to streaming search.");
            departureFlight = flightSearchByNameUsingStream(flightSearchApiInputs, airline, accessToken, "IDR", deviceId);
        }
        if(departureFlight==null){
            throw new NotFoundException("Flight not found");
        }

        logger.log("Flight Search API response->" + departureFlight);
        String flightId = departureFlight.flightId;
        String supplierID = departureFlight.fareDetail.fares.get(0).supplierId;
        logger.log("Flight ID----->"+flightId);
        logger.log("Supplier ID----->"+supplierID);

        //flight cart post api to get cart id
        FlightCartApi.Inputs flightCartApiInputs = new FlightCartApi.Inputs(flightId, supplierID, accessToken);
        String cartId = flightCartApi(flightCartApiInputs);
        log("Cart ID---->"+cartId);

        //flight booking api
        FlightBookingApiDomestic.Inputs flightBookingApiInputs = new FlightBookingApiDomestic.Inputs(cartId, username, accessToken, passengerNames, phoneNumber, adult);
        FlightBookingDetailDomestic flightBookingDetailDomestic = flightBookingApiDomestic(flightBookingApiInputs);
        log("Flight Booking API response->" + flightBookingDetailDomestic);
        long orderId = flightBookingDetailDomestic.data.successData.orderId;
        String orderHash = flightBookingDetailDomestic.data.successData.orderHash;

        logger.log("orderId->" + orderId);
        logger.log("orderHash->" + orderHash);

        //Sakuku payment api
        SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, orderId);
        SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
        if (!submitSakukuResponse.message.equals("SUCCESS")) Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
        BigInteger grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
        logger.log("Grand Total -> " + grandTotal);

        String paymentStatus = paidCheckerStatus(120,String.valueOf(orderId));
        if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");

        return String.valueOf(orderId);
    }

    private SubmitSakukuResponse submitSakuku(SubmitSakukuApi.Inputs inputs) throws InterruptedException, ExecutionException {
        SubmitSakukuApi submitSakukuApi = new SubmitSakukuApi(inputs);
        ApiResponse apiResponse = submitSakukuApi.hitApi(apiTools);
        Gson gson = new Gson();
        logger.log("response submit Sakuku : " + apiResponse.getBody());
        return gson.fromJson(apiResponse.getBody(), SubmitSakukuResponse.class);
    }

    private String paidCheckerStatus(long duration,String orderId) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        long waitTime = duration * 1000;
        boolean paymentSuccessful = false;
        String paymentStatus = null;

        while (System.currentTimeMillis() - startTime < waitTime && !paymentSuccessful) {
            PaidCheckerApi.Inputs paidCheckerApiInputs = new PaidCheckerApi.Inputs(String.valueOf(orderId));
            paymentStatus = hitPaidChecker(paidCheckerApiInputs, 1);

            if (paymentStatus.equals("SUCCESS")) {
                paymentSuccessful = true;
            } else {
                Thread.sleep(5000);
            }
        }
        return paymentStatus;
    }

    private String hitPaidChecker(PaidCheckerApi.Inputs inputs, int retry) throws InterruptedException, ExecutionException {
        boolean paid = false;
        String status = "";
        for (int i = 0; i < retry; i++){
            PaidCheckerApi paidCheckerApi = new PaidCheckerApi(inputs);
            ApiResponse apiResponse = paidCheckerApi.hitApi(apiTools);
            Gson gson = new Gson();
            PaidCheckerResponse paidCheckerResponse = gson.fromJson(apiResponse.getBody(), PaidCheckerResponse.class);
            paid = paidCheckerResponse.data.paid;
            if (paid) {
                status = paidCheckerResponse.data.paymentStatus;
                break;
            }
            Thread.sleep(500);
        }
        return status;
    }

    /**
     * Normalize a trip-type / search-type string to the canonical API form expected by
     * {@code FlightSearchApi}'s {@code searchType} query param ({@code ONE_WAY} /
     * {@code ROUND_TRIP}). The {@code /v3/search} endpoint only accepts those two
     * exact spellings — anything else is rejected with HTTP 400.
     * <p>
     * Accepts <em>any</em> case/separator variation of {@code oneway} / {@code roundtrip}.
     * Whitespace, underscores, and hyphens are collapsed before matching, so all of
     * the following resolve correctly:
     * <pre>
     *   oneway, ONE_WAY, One-Way, one way, One Way, ONE WAY, OneWay, ONEWAY, oneWay,
     *   one--way, one  way, "  one_way  "
     *   roundtrip, ROUND_TRIP, Round-Trip, round trip, Round Trip, ROUND TRIP,
     *   RoundTrip, ROUNDTRIP, roundTrip, round--trip, round  trip
     * </pre>
     * Unknown values are returned untouched so callers using a custom backend convention
     * are not silently broken; {@code null} is preserved as {@code null}.
     */
    private String normalizeSearchType(String searchType) {
        if (searchType == null) {
            return null;
        }
        String collapsed = searchType.replaceAll("[\\s_\\-]+", "").toLowerCase();
        return switch (collapsed) {
            case "oneway" -> "ONE_WAY";
            case "roundtrip" -> "ROUND_TRIP";
            default -> searchType;
        };
    }
}

