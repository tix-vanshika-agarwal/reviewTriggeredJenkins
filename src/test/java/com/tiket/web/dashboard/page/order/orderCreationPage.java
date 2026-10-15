package com.tiket.web.dashboard.page.order;

import com.google.gson.Gson;
import com.tiket.api.HitGetUrl;
import com.tiket.api.accommodation.hotel.*;
import com.tiket.api.accommodation.hotel.booking.SubmitBookingApiV2;
import com.tiket.api.accommodation.hotel.booking.SubmitBookingMultiRoomApi;
import com.tiket.api.accommodation.hotel.prebook.PreBookGeneratingCartApi;
import com.tiket.api.accommodation.hotel.prebook.PreBookMultiRoomApi;
import com.tiket.api.accommodation.hotel.room.SelectRoomApi;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardApi;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.carrental.*;
import com.tiket.api.discount.ApplyDiscountApi;
import com.tiket.api.discount.GiftVoucherCreateApi;
import com.tiket.api.ferry.AddToCartFerryApi;
import com.tiket.api.ferry.SearchScheduleFerryApi;
import com.tiket.api.ferry.SubmitBookingFerryApi;
import com.tiket.api.flight.*;
import com.tiket.api.flight.dashboard.FlightEticketIssueApi;
import com.tiket.api.flight.dashboard.FlightGetBookingCodeApi;
import com.tiket.api.loyalty.*;
import com.tiket.api.order.InternalOrderDetailApi;
import com.tiket.api.orderDweb.LandingPageApi;
import com.tiket.api.payment.*;
import com.tiket.api.ttd.*;
import com.tiket.api.unm.*;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Exception.ApiEnvironmentException;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.accommodation.hotel.*;
import com.tiket.pojo.auth.MfaMethodsResponse;
import com.tiket.pojo.b2b.corporate.EmployeeData;
import com.tiket.pojo.carrental.CarRentalAutoCompleteResponse;
import com.tiket.pojo.carrental.CarRentalGetLocationIdResponse;
import com.tiket.pojo.carrental.CarRentalSearchLocationResponse;
import com.tiket.pojo.carrental.SelectCarRentalResponse;
import com.tiket.pojo.discount.DiscountResponse;
import com.tiket.pojo.discount.GiftVoucherCreateResponse;
import com.tiket.pojo.ferry.AddToCartFerryResponse;
import com.tiket.pojo.ferry.SearchScheduleFerryResponse;
import com.tiket.pojo.ferry.SubmitBookingFerryResponse;
import com.tiket.pojo.flight.FlightBookingCodeDetail;
import com.tiket.pojo.flight.FlightBookingDetailDomestic;
import com.tiket.pojo.flight.FlightCartId;
import com.tiket.pojo.flight.FlightSearchResponse;
import com.tiket.pojo.loyalty.ActiveTiketPointsResponse;
import com.tiket.pojo.loyalty.ApplyTiketPointsResponse;
import com.tiket.pojo.loyalty.OtpGenerationResponse;
import com.tiket.pojo.orderDweb.LandingPageResponse;
import com.tiket.pojo.payment.*;
import com.tiket.pojo.room.SelectRoomResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.web.dWeb.page.account.AccountPage;
import io.restassured.response.Response;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.log;
import static org.hamcrest.Matchers.*;

public class orderCreationPage<T extends WebDriver> extends BasePage<T> {
    
    public orderCreationPage(Session<T> session) { super(session); }
    private static final Gson gson = new Gson();
    // account data
    private String accessToken;
    String totalTiketPoints;

    int orderId;
    int orderDetailId;
    String orderHash;
    BigInteger grandTotal = new BigInteger("0");
    String productType;

    //hotel only
    String hotelName;
    String hotelRoomName;
    String nativeHotelColor = "#0064D2";
    public List<String> guestNames;
    BigInteger totalPrice;
    int itineraryID;
    String BookingToken;
    String checkInDate;
    String UtcTime;

    //Todo Only
    private String productName;
    private String productCategory;
    //Car Rental
    String referenceId;
    String regionalId;
    String placeId;
    String latitude;
    String longitude;
    String locationId;
    String carRegionalId;
    String carName;
    String carRentalLocation;
    //Car Transfer
    String pickupPlace;
    String pickupAddress;
    String dropPlace;
    String dropAddress;
    String orderName;
    String carType;

    AccountPage<?> accountPage = new AccountPage<>(session);

    public String getAccessToken(){
        logger.log("get access token -> "+accessToken);
        return this.accessToken;
    }

    public int getOrderId(){
        logger.log("get Order Id -> "+orderId);
        return this.orderId;
    }

    public int getOrderDetailId(){
        logger.log("get Order Detail Id -> "+orderDetailId);
        return this.orderDetailId;
    }

    public String getOrderHash(){
        logger.log("get Order Hash -> "+orderHash);
        return this.orderHash;
    }

    public String getHotelRoomName(){
        logger.log("get Hotel Room Name -> "+hotelRoomName);
        return this.hotelRoomName;
    }

    public BigInteger getGrandTotal(){
        logger.log("get Grand Total Payment -> "+grandTotal);
        return this.grandTotal;
    }

    public String getProductType() {
        logger.log("get Product Type -> "+productType);
        return this.productType;
    }

    public String getProductName() {
        logger.log("get Product Name -> "+productName);
        return this.productName;
    }

    public String getProductCategory() {
        logger.log("get Product Category -> "+productCategory);
        return this.productCategory;
    }

    public String generateIdempotentKey() {
        long timestamp = System.currentTimeMillis();

        Random random = new Random();
        int randomNumber = random.nextInt(1000000); // Change this range as needed

        return "automation-" + timestamp + "-" + randomNumber;
    }

    public int getItineraryID(){
        logger.log("get Itinerary ID -> "+itineraryID);
        return this.itineraryID;
    }

    public String getUtcTime() {
        logger.log("get Utc Time -> "+UtcTime);
        return this.UtcTime;
    }

    public String getBookingToken(){
        logger.log("get Booking Token -> "+BookingToken);
        return this.BookingToken;
    }

    public String getCheckInDate(){
        logger.log("get Check In Date -> "+checkInDate);
        return this.checkInDate;
    }

    // Order Creation Hotel
    private HotelAutoCompleteResponse hotelAutoComplete(HotelAutocompleteApi.Inputs inputs) throws ExecutionException, InterruptedException {
        HotelAutocompleteApi hotelAutocompleteApi = new HotelAutocompleteApi(inputs);
        ApiResponse apiResponse = hotelAutocompleteApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), HotelAutoCompleteResponse.class);
    }

    private SelectRoomResponse.RatePlan selectRoom(SelectRoomApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
        ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);

        logger.log("Room item color" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).itemColor);
        logger.log("Available rooms" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).availableRoom);
        logger.log("Cancellation policies not null?" + (selectRoomResponse.data.rooms.get(0).ratePlans.get(0).cancellationPolicies.policies != null));

        return selectRoomResponse.data.rooms.stream()
            .flatMap(roomList -> roomList.ratePlans.stream())
            .filter(ratePlan -> ratePlan.availableRoom >= inputs.room())
            .filter(ratePlan -> ratePlan.cancellationPolicies.policies != null)
            .filter(ratePlan -> Objects.equals(ratePlan.itemColor, nativeHotelColor))
            .findFirst()
            .orElseThrow();
    }

    private SelectRoomResponse.RatePlan selectTheMostExpensiveRoom(SelectRoomApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
        ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);

        logger.log("Room item color" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).itemColor);
        logger.log("Available rooms" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).availableRoom);
        logger.log("Cancellation policies not null?" + (selectRoomResponse.data.rooms.get(0).ratePlans.get(0).cancellationPolicies.policies != null));

        Optional<SelectRoomResponse.RatePlan> mostExpensiveRoom = selectRoomResponse.data.rooms.stream()
                .flatMap(roomList -> roomList.ratePlans.stream())
                .filter(ratePlan -> ratePlan.availableRoom >= inputs.room())
                .filter(ratePlan -> ratePlan.cancellationPolicies != null && ratePlan.cancellationPolicies.policies != null)
                .filter(ratePlan -> Objects.equals(ratePlan.itemColor, nativeHotelColor))
                .max(Comparator.comparingInt(rp -> rp.rateInfo.price.totalRateWithTax));

        mostExpensiveRoom.ifPresent(topRoom -> logger.log("Highest Price: " + topRoom.rateInfo.price.totalRateWithTax));
        return mostExpensiveRoom.orElseThrow();
    }

    private SelectRoomResponse.RatePlan selectRoom(SelectRoomApi.Inputs inputs, String vendor, boolean isRefundable, boolean isPayNow) throws ExecutionException, InterruptedException {
        SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
        ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);

        logger.log("Room item color" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).itemColor);
        logger.log("Available rooms" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).availableRoom);
        logger.log("Cancellation policies not null?" + (selectRoomResponse.data.rooms.get(0).ratePlans.get(0).cancellationPolicies.policies != null));

        String vendorColor = mapVendorToItemColor(vendor);
        String cancellationPolicyType;
        String paymentOption;

        if (!isRefundable) cancellationPolicyType = "NO_CANCELLATION";
        else {
            cancellationPolicyType = "FREE_CANCELLATION";
        }

        if (!isPayNow) paymentOption = "pay_at_hotel";
        else {
            paymentOption = "pay_now";
        }

        logger.log("==== FILTER CRITERIA ====");
        logger.log("Looking for vendorColor: " + vendorColor);
        logger.log("Looking for cancellationPolicyType: " + cancellationPolicyType);
        logger.log("Looking for paymentOption: " + paymentOption);

        // Log all available rate plans before filtering
        logger.log("==== ALL AVAILABLE RATE PLANS ====");
        selectRoomResponse.data.rooms.stream()
                .flatMap(roomList -> roomList.ratePlans.stream())
                .forEach(ratePlan -> {
                    logger.log("RatePlan -> itemColor: " + ratePlan.itemColor 
                            + ", cancellationPolicyType: " + ratePlan.cancellationPolicies.type 
                            + ", paymentOption: " + ratePlan.paymentOption 
                            + ", availableRoom: " + ratePlan.availableRoom);
                });

        return selectRoomResponse.data.rooms.stream()
                .flatMap(roomList -> roomList.ratePlans.stream())
                .filter(ratePlan -> ratePlan.availableRoom >= inputs.room())
                .filter(ratePlan -> ratePlan.cancellationPolicies.type.equalsIgnoreCase(cancellationPolicyType))
                .filter(ratePlan -> ratePlan.paymentOption.equalsIgnoreCase(paymentOption))
                .filter(ratePlan -> Objects.equals(ratePlan.itemColor, vendorColor))
                .findFirst()
                .orElseThrow();
    }

    private String selectRoomName(SelectRoomApi.Inputs inputs, String vendor, boolean isRefundable, boolean isPayNow) throws ExecutionException, InterruptedException {
        SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
        ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);

        logger.log("Room item color" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).itemColor);
        logger.log("Available rooms" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).availableRoom);
        logger.log("Cancellation policies not null?" + (selectRoomResponse.data.rooms.get(0).ratePlans.get(0).cancellationPolicies.policies != null));

        String vendorColor = mapVendorToItemColor(vendor);


        return selectRoomResponse.data.rooms.stream()
                .filter(roomList -> roomList.ratePlans.stream()
                        .anyMatch(ratePlan -> ratePlan.availableRoom >= inputs.room()
                                && ratePlan.cancellationPolicies.policies != null
                                && Objects.equals(ratePlan.itemColor, vendorColor)))
                .findFirst()
                .orElseThrow()
                .groupName;
    }


    private String selectRoomName(SelectRoomApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
        ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);

        logger.log("Room item color" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).itemColor);
        logger.log("Available rooms" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).availableRoom);
        logger.log("Cancellation policies not null?" + (selectRoomResponse.data.rooms.get(0).ratePlans.get(0).cancellationPolicies.policies != null));

        return selectRoomResponse.data.rooms.stream()
                .filter(roomList -> roomList.ratePlans.stream()
                        .anyMatch(ratePlan -> ratePlan.availableRoom >= inputs.room()
                                && ratePlan.cancellationPolicies.policies != null
                                && Objects.equals(ratePlan.itemColor, nativeHotelColor)))
                .findFirst()
                .orElseThrow()
                .groupName;
    }

    private String mapVendorToItemColor(String vendor) {
        return switch (vendor.toUpperCase()) {
            case "AGODA" -> "#FF0000";
            case "TIKET" -> "#0064D2";
            case "WEBBEDS" -> "#30B6C5";
            case "MEITUAN" -> "#0BAE54";
            case "RAKUTEN" -> "#BF00FF";
            case "HOTELBEDS" -> "#FCA12F";
            case "EXPEDIA_RAPID" -> "#30F639";
            case "TRAVELGATE_MGHO" -> "#ED96D0";
            case "TRAVELGATE_LOGITEST" -> "#8C909E";
            case "TRAVELGATE_HOTELTEST" -> "#4D4F56";
            case "BCOM" -> "#A3CFFF";
            case "AGODA_V2" -> "#FC7474";
            case "AGODA_V3" -> "#000000";
            default -> throw new IllegalArgumentException("Unknown vendor: " + vendor);
        };
    }

    private SelectRoomResponse.RatePlan selectRoomMostExpansive(SelectRoomApi.Inputs inputs) throws ExecutionException, InterruptedException {
        Gson gson = new Gson();
        for(int attempt = 0; attempt<=3; attempt++){
            SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
            ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
            logger.log("Select Room Response : "+selectRoomApiResponse.getBody());
            SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
            if(!selectRoomResponse.data.rooms.isEmpty()){
                if (selectRoomResponse.data.rooms.size() == 1) {
                    logger.softWarning("Only one room available, can't get the expansive room");
                    return selectRoomResponse.data.rooms.get(0).ratePlans.stream().findFirst().orElseThrow();
                }
                return selectRoomResponse.data.rooms.stream()
                        .flatMap(roomList -> roomList.ratePlans.stream())
                        .filter(room -> room.availableRoom >= inputs.room() && room.cancellationPolicies.policies != null)
                        .max(Comparator.comparingDouble(room -> room.rateInfo.price.totalRateWithTax))
                        .orElseThrow();
            }
            Thread.sleep(10000);
        }
        throw new RuntimeException("Hotel Room not found");
    }

    private String prebook(PreBookGeneratingCartApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PreBookGeneratingCartApi preBookGeneratingCartApi = new PreBookGeneratingCartApi(inputs);
        ApiResponse preBookCartApiResponse = preBookGeneratingCartApi.hitApi(apiTools);
        Gson gson = new Gson();
        PreBookGeneratingCartResponse generatingCartResponse = gson.fromJson(preBookCartApiResponse.getBody(),PreBookGeneratingCartResponse.class);
        String cartId = generatingCartResponse.data.hotels.get(0).detail.rooms.get(0).cartId;
        logger.log("cart id: " + cartId);
        return cartId;
    }

    private SubmitBookingResponse submitBooking(SubmitBookingApiV2.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitBookingApiV2 submitBookingApi = new SubmitBookingApiV2(inputs);
        ApiResponse submitBookingApiResponse = submitBookingApi.hitApi(apiTools);
        logger.log("Hotel book status : "+submitBookingApiResponse.getStatusCode());
        logger.log("Hotel response : "+submitBookingApiResponse.getBody().toString());
        Gson gson = new Gson();
        return gson.fromJson(submitBookingApiResponse.getBody(), SubmitBookingResponse.class);
    }

    @SneakyThrows
    public String paymentChecker() throws ExecutionException, InterruptedException {
        String submitPaidPayment = hitPaymentChecker();
        logger.log("submitPaidPayment-> " + submitPaidPayment);
        return submitPaidPayment;
    }

    private String hitPaymentChecker() throws ExecutionException, InterruptedException {
        PaymentCheckerApi paymentCheckerApi = new PaymentCheckerApi();
        ApiResponse apiResponse = paymentCheckerApi.hitApi(apiTools);
        Gson gson = new Gson();
        PaymentCheckerResponse paymentCheckerResponse = gson.fromJson(apiResponse.getBody(), PaymentCheckerResponse.class);
        return paymentCheckerResponse.message;
    }

    public ActiveTiketPointsResponse getActivePoint(ActiveTiketPointsApi.Inputs inputs) throws ExecutionException, InterruptedException {
        ActiveTiketPointsApi tiketPointsApi = new ActiveTiketPointsApi(inputs);
        ApiResponse tiketPointsApiResponse = tiketPointsApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(tiketPointsApiResponse.getBody(), ActiveTiketPointsResponse.class);
    }

    public OtpGenerationResponse generateOtp(OtpGenerationApi.Inputs inputs) throws ExecutionException, InterruptedException {
        OtpGenerationApi otpGenerationApi = new OtpGenerationApi(inputs);
        ApiResponse otpGenerationApiResponse = otpGenerationApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(otpGenerationApiResponse.getBody(), OtpGenerationResponse.class);
    }

    public ApplyTiketPointsResponse applyTiketPoint(ApplyTiketPointsApi.Inputs inputs) throws ExecutionException, InterruptedException {
        ApplyTiketPointsApi applyTiketPointsApi = new ApplyTiketPointsApi(inputs);
        ApiResponse applyTiketPointsApiResponse = applyTiketPointsApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(applyTiketPointsApiResponse.getBody(), ApplyTiketPointsResponse.class);
    }

    public ApplyTiketPointsResponse applyTiketPointV2(ApplyTiketPointsApiV2.Inputs inputs) throws ExecutionException, InterruptedException {
        ApplyTiketPointsApiV2 applyTiketPointsApi = new ApplyTiketPointsApiV2(inputs);
        ApiResponse applyTiketPointsApiResponse = applyTiketPointsApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(applyTiketPointsApiResponse.getBody(), ApplyTiketPointsResponse.class);
    }

    private String paidCheckerStatus(long duration) throws ExecutionException, InterruptedException {
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

    public String paidCheckerStatus(long duration, String orderId) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        long waitTime = duration * 1000;
        boolean paymentSuccessful = false;
        String paymentStatus = null;

        while (System.currentTimeMillis() - startTime < waitTime && !paymentSuccessful) {
            PaidCheckerApi.Inputs paidCheckerApiInputs = new PaidCheckerApi.Inputs(orderId);
            paymentStatus = hitPaidChecker(paidCheckerApiInputs, 1);

            if (paymentStatus.equals("SUCCESS")) {
                paymentSuccessful = true;
            } else {
                Thread.sleep(5000);
            }
        }
        return paymentStatus;
    }

    private boolean hitAcsUrl(String callback) throws InterruptedException {
        driverTools.maximizeWindow();
        driverTools.get(callback);

        String currentUrl = driverTools.getCurrentURL();
        int i = 0;
        while (!currentUrl.contains("complete") && i<50){
            currentUrl = driverTools.getCurrentURL();
            Thread.sleep(1000);
            i++;
        }

        return currentUrl.contains("complete") || currentUrl.contains(String.valueOf(orderId));    }

    public void injectTiketPoint(int pdAccountId, int pdPoint, String pdProcessType, int pdRatio, String username) throws ExecutionException, InterruptedException {
        AddActivePointsApi.Inputs inputs = new AddActivePointsApi.Inputs(pdAccountId, pdPoint, pdProcessType, pdRatio, username);
        AddActivePointsApi api = new AddActivePointsApi(inputs);
        ApiResponse apiResponse = api.hitApi(apiTools);
        logger.log("Inject point amount "+pdPoint+" via api success: (" + apiResponse.getStatusCode() + ")");
    }

    public void injectTiketPointWithThreshold(
        int pdAccountId, String pdProcessType, int pdRatio,
        String username, String password, int pointsThreshold) throws ExecutionException, InterruptedException
    {
        if (accessToken.isEmpty()) {
            accessToken = accountPage.getAccessTokenUNM(username, password);
        }
        logger.log("accessToken-> " + accessToken);

        ActiveTiketPointsApi.Inputs tiketPointsApiInputs = new ActiveTiketPointsApi.Inputs(accessToken);
        ActiveTiketPointsResponse activeTiketPointsResponse = getActivePoint(tiketPointsApiInputs);
        int points = Integer.parseInt(activeTiketPointsResponse.data);
        logger.log("Active points -> " + points);
        if (points < pointsThreshold) {
            int pdPoint = pointsThreshold - points;
            injectTiketPoint(pdAccountId, pdPoint, pdProcessType, pdRatio, username);
        }
    }

    public GiftVoucherCreateResponse createGvApi(String username,
                              String idempotentKey,
                              String apiKey,
                              int discountAmount,
                              int cashbackAmount,
                              int maximumVoucherCashbackAmount,
                              int discountPercentage,
                              int maximumVoucherAmount,
                              int minimumTransactionAmount,
                              String currency,
                              String rangeAddDate,
                              String discountScheme,
                              String giftVoucherAdjustmentCode,
                              List<String> userEmails, List<String> validityRestriction) throws ExecutionException, InterruptedException {
        GiftVoucherCreateApi.Inputs inputs = new GiftVoucherCreateApi.Inputs(username, idempotentKey, apiKey, discountAmount, cashbackAmount, maximumVoucherCashbackAmount, discountPercentage, maximumVoucherAmount, minimumTransactionAmount, currency, rangeAddDate, discountScheme, giftVoucherAdjustmentCode, userEmails, validityRestriction);
        GiftVoucherCreateApi api = new GiftVoucherCreateApi(inputs);
        ApiResponse apiResponse = api.hitApi(apiTools);
        logger.log("Create GV response: "+apiResponse.getBody());

        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), GiftVoucherCreateResponse.class);
    }

    @SneakyThrows
    public void createWaitingPaymentOrderHotel(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,

            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency
    ) {
        try {
            accessToken = "Bearer " + accountPage.getAccessTokenUNM(username, password);

            logger.log("Hotel Name -> " + query);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
            hotelName = hotelAutoCompleteResponse.data.contents.get(0).name;
            logger.log("hotelId-> " + hotelId);
            logger.log("hotel name->" + hotelName);
            logger.log("Date: " + startDate);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"en", "");
            SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs);
            String roomName = selectRoomName(selectRoomApiInputs);
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName-> " + hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            String cartID = prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, currency, false, "", "id", hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
            orderHash = submitBookingResponse.data.orderHash;
            orderId = submitBookingResponse.data.orderId;
            orderDetailId = submitBookingResponse.data.orderDetailId;
            totalPrice = BigInteger.valueOf(submitBookingResponse.data.price.intValue());
            productType = "TIXHOTEL";
            logger.log("orderId-> " + orderId);

            logger.log("message-> " + submitBookingResponse.message);
            if (!submitBookingResponse.message.equalsIgnoreCase("SUCCESS")) Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
    }

    public DiscountResponse applyDiscount(ApplyDiscountApi.Inputs inputs) throws ExecutionException, InterruptedException {
        ApplyDiscountApi applyDiscountApi = new ApplyDiscountApi(inputs);
        ApiResponse apiResponse = applyDiscountApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), DiscountResponse.class);
    }

    public void applyPromoGv(String discountCode, String productType, String currency) throws ExecutionException, InterruptedException {
        ApplyDiscountApi.Inputs inputs = new ApplyDiscountApi.Inputs(accessToken, String.valueOf(orderId), orderHash, discountCode, productType, totalPrice, currency);
        DiscountResponse discountResponse = applyDiscount(inputs);
        if (!discountResponse.message.equalsIgnoreCase("SUCCESS")) Assertion.skip("Apply promo/GV via API is failed. Skipping the subsequent steps.");
    }

    public void applyTiketPoints() {
        try {
            ActiveTiketPointsApi.Inputs tiketPointsApiInputs = new ActiveTiketPointsApi.Inputs(accessToken);
            ActiveTiketPointsResponse activeTiketPointsResponse = getActivePoint(tiketPointsApiInputs);
            totalTiketPoints = activeTiketPointsResponse.data;
            BigInteger totalPoints = new BigInteger(totalTiketPoints);
            grandTotal = grandTotal.add(totalPoints);
            logger.log("totalTiketPoints -> " + totalTiketPoints);
            logger.log("grandTotal -> " + grandTotal);

            OtpGenerationApi.Inputs otpGenerationApiInputs = new OtpGenerationApi.Inputs(accessToken);
            OtpGenerationResponse otpGenerationResponse = generateOtp(otpGenerationApiInputs);
            String trxId = otpGenerationResponse.data.trxId;
            logger.log("trxId-> " + trxId);

            ApplyTiketPointsApi.Inputs applyTiketPointsApiInputs = new ApplyTiketPointsApi.Inputs(accessToken, orderId, trxId);
            ApplyTiketPointsResponse applyTiketPointsResponse = applyTiketPoint(applyTiketPointsApiInputs);
            boolean tiketPointsStatus = orderId == applyTiketPointsResponse.data.orderId;
            logger.log("tiketPointsStatus-> " + tiketPointsStatus);

            if (!tiketPointsStatus) Assertion.skip("Apply tiket point via API failed. Skipping the subsequent steps.");
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Apply tiket points via API failed. Skipping the subsequent steps.");
        }
    }

    public void applyTiketPointsV2(String token, int orderId, String productType) {
        try {
            ActiveTiketPointsApi.Inputs tiketPointsApiInputs = new ActiveTiketPointsApi.Inputs(token);
            ActiveTiketPointsResponse activeTiketPointsResponse = getActivePoint(tiketPointsApiInputs);
            totalTiketPoints = activeTiketPointsResponse.data;
            BigInteger totalPoints = new BigInteger(totalTiketPoints);
            grandTotal = grandTotal.add(totalPoints);
            logger.log("totalTiketPoints -> " + totalTiketPoints);
            logger.log("grandTotal -> " + grandTotal);

            ApplyTiketPointsApiV2.Inputs applyTiketPointsApiInputs = new ApplyTiketPointsApiV2.Inputs(token, orderId, productType);
            ApplyTiketPointsResponse applyTiketPointsResponse = applyTiketPointV2(applyTiketPointsApiInputs);
            boolean tiketPointsStatus = orderId == applyTiketPointsResponse.data.orderId;
            logger.log("tiketPointsStatus-> " + tiketPointsStatus);

            if (!tiketPointsStatus) Assertion.skip("Apply tiket point via API failed. Skipping the subsequent steps.");
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Apply tiket points via API failed. Skipping the subsequent steps.");
        }
    }

    public SubmitTiketPointsResponse paidTiketPoints(SubmitTiketPointsApi.Inputs inputs) throws InterruptedException, ExecutionException {
        SubmitTiketPointsApi submitTiketPointsApi = new SubmitTiketPointsApi(inputs);
        ApiResponse response = submitTiketPointsApi.hitApi(apiTools);
        logger.log("Submit tiket point API response -> "+response.getBody());
        Gson gson = new Gson();
        SubmitTiketPointsResponse result = gson.fromJson(response.getBody(), SubmitTiketPointsResponse.class);
        return result;
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

    public void payWaitingPaymentHotelOrderApi() {
        try {
            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, orderId);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!submitSakukuResponse.message.equals("SUCCESS")) Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            grandTotal = grandTotal.add(submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger());
            logger.log("Grand Total -> " + grandTotal);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
    }

    private String hitPaidChecker(PaidCheckerApi.Inputs inputs, int retry) throws InterruptedException, ExecutionException {
        boolean paid = false;
        String status = "";
        for (int i = 0; i < retry; i++){
            PaidCheckerApi paidCheckerApi = new PaidCheckerApi(inputs);
            ApiResponse apiResponse = paidCheckerApi.hitApi(apiTools);
            logger.log("paid checker ->" + apiResponse.getBody());
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

    public void createHotelSakukuIssuedOrderApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,

            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency
    ) {
        try {
            String accessToken = "Bearer "+accountPage.getAccessTokenUNM(username, password);
            logger.log("accessToken-> " + accessToken);
            logger.log("query " + query);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
            hotelName = hotelAutoCompleteResponse.data.contents.get(0).name;
            logger.log("hotelId-> " + hotelId);
            logger.log("hotel name->" + hotelName);

            logger.log("Date: " + startDate);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"en", "");
            SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs);
            String roomName = selectRoomName(selectRoomApiInputs);
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName-> " + hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            String cartID = prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, "", false, "", "id", hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();
            orderId = submitBookingResponse.data.orderId;

            logger.log("orderId-> " + orderId);

            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, submitBookingResponse.data.orderHash, orderId);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!submitSakukuResponse.message.equals("SUCCESS")) Assertion.skip("Paid using SAKUKU is failed. Skipping the subsequent steps.");
            grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> " + grandTotal);
            logger.log("Reference Id -> "+submitSakukuResponse.data.sidebarPayment.referenceId);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
    }

    public void createHotelSakukuIssuedOrderApiWithHotelId(
            String username,
            String password,
            String startDate,
            String hotelId,
            int adult,
            int night,
            int room,

            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency
    ) {
        try {
            String accessToken = "Bearer "+accountPage.getAccessTokenUNM(username, password);
            logger.log("accessToken-> " + accessToken);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"en", "");
            SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs);
            String roomName = selectRoomName(selectRoomApiInputs);
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName-> " + hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            String cartID = prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, "", false, "", "id", hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();
            orderId = submitBookingResponse.data.orderId;

            logger.log("orderId-> " + orderId);

            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, submitBookingResponse.data.orderHash, orderId);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!submitSakukuResponse.message.equals("SUCCESS")) Assertion.skip("Paid using SAKUKU is failed. Skipping the subsequent steps.");
            grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> " + grandTotal);
            logger.log("Reference Id -> "+submitSakukuResponse.data.sidebarPayment.referenceId);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
    }

    public void createHotelSakukuIssuedOrderApiWithHotelIdAndTheMostExpensive(
            String username,
            String password,
            String startDate,
            String hotelId,
            int adult,
            int night,
            int room,

            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency
    ) {
        try {
            String accessToken = "Bearer "+accountPage.getAccessTokenUNM(username, password);
            logger.log("accessToken-> " + accessToken);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"en", "");
            SelectRoomResponse.RatePlan selectedRoom = selectTheMostExpensiveRoom(selectRoomApiInputs);
            String roomName = selectRoomName(selectRoomApiInputs);
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName-> " + hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            String cartID = prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, "", false,"", "id", hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();
            orderId = submitBookingResponse.data.orderId;

            logger.log("orderId-> " + orderId);

            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, submitBookingResponse.data.orderHash, orderId);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!submitSakukuResponse.message.equals("SUCCESS")) Assertion.skip("Paid using SAKUKU is failed. Skipping the subsequent steps.");
            grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> " + grandTotal);
            logger.log("Reference Id -> "+submitSakukuResponse.data.sidebarPayment.referenceId);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
    }

    public void createRefundableHotelSakukuIssuedOrderApiSelectVendor(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,

            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency,

            boolean refundable,
            boolean isPayNow,
            String vendor
    ) {
        int maxRetries = 3;
        int attempt = 0;
        Exception lastException = null;

        logger.log("VENDOR NAME:" + vendor);

        while (attempt < maxRetries) {
            attempt++;
            try {
                logger.log("=== Hotel Booking Attempt " + attempt + " of " + maxRetries + " ===");
                
                // Step 1: Get Access Token (with retry)
                String accessToken = null;
                for (int tokenAttempt = 1; tokenAttempt <= 3; tokenAttempt++) {
                    try {
                        logger.log("Getting access token (attempt " + tokenAttempt + "/3)");
                        accessToken = "Bearer " + accountPage.getAccessTokenUNM(username, password);
                        logger.log("accessToken-> " + accessToken);
                        break;
                    } catch (Exception e) {
                        if (tokenAttempt == 3) throw e;
                        logger.log("Failed to get access token, retrying...");
                        Thread.sleep(2000);
                    }
                }

                // Step 2: Hotel Autocomplete (with retry)
                String hotelId = null;
                for (int autocompleteAttempt = 1; autocompleteAttempt <= 3; autocompleteAttempt++) {
                    try {
                        logger.log("Hotel autocomplete (attempt " + autocompleteAttempt + "/3) for query: " + query);
                        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
                        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
                        hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
                        hotelName = hotelAutoCompleteResponse.data.contents.get(0).name;
                        logger.log("hotelId-> " + hotelId);
                        logger.log("hotel name->" + hotelName);
                        break;
                    } catch (Exception e) {
                        if (autocompleteAttempt == 3) throw e;
                        logger.log("Hotel autocomplete failed, retrying...");
                        Thread.sleep(2000);
                    }
                }
                logger.log("Date: " + startDate);

                // Step 3: Select Room (with retry)
                SelectRoomResponse.RatePlan selectedRoom = null;
                String roomName = null;
                for (int selectRoomAttempt = 1; selectRoomAttempt <= 3; selectRoomAttempt++) {
                    try {
                        logger.log("SELECTING ROOM (attempt " + selectRoomAttempt + "/3)");
                        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate, "en", "");
                        selectedRoom = selectRoom(selectRoomApiInputs, vendor, refundable, isPayNow);
                        logger.log("ROOM IS SELECTED");
                        roomName = selectRoomName(selectRoomApiInputs, vendor, refundable, isPayNow);
                        hotelRoomName = roomName;
                        logger.log("ROOM NAME SELECTED " + roomName);
                        break;
                    } catch (Exception e) {
                        if (selectRoomAttempt == 3) throw e;
                        logger.log("Room selection failed, retrying...");
                        Thread.sleep(2000);
                    }
                }

                int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
                int maxChildOccupancy = selectedRoom.maxChildOccupancy;
                logger.log("hotelRoomName-> " + hotelRoomName);

                // Step 4: Prebook / Get Cart (with retry)
                String cartID = null;
                for (int prebookAttempt = 1; prebookAttempt <= 3; prebookAttempt++) {
                    try {
                        logger.log("Prebook - generating cart (attempt " + prebookAttempt + "/3)");
                        PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate, maxRoomOccupancy, maxChildOccupancy, currency);
                        cartID = prebook(preBookCartApiInputs);
                        logger.log("cartID-> " + cartID);
                        break;
                    } catch (Exception e) {
                        if (prebookAttempt == 3) throw e;
                        logger.log("Prebook failed, retrying...");
                        Thread.sleep(2000);
                    }
                }

                // Step 5: Submit Booking V2 (with retry)
                SubmitBookingResponse submitBookingResponse = null;
                for (int bookingAttempt = 1; bookingAttempt <= 3; bookingAttempt++) {
                    try {
                        logger.log("Submit booking V2 (attempt " + bookingAttempt + "/3)");
                        SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, "", false, "", "id", hotelId, startDate);
                        submitBookingResponse = submitBooking(submitBookingApiInputs);
                        guestNames = submitBookingApiInputs.getGuestNames();
                        orderId = submitBookingResponse.data.orderId;
                        logger.log("orderId-> " + orderId);
                        if (orderId == 0) {
                            throw new RuntimeException("Booking returned orderId=0 (code: " + submitBookingResponse.code + "), retrying booking...");
                        }
                        break;
                    } catch (Exception e) {
                        if (bookingAttempt == 3) throw e;
                        logger.log("Submit booking failed, retrying...");
                        Thread.sleep(2000);
                    }
                }

                // Step 6: Submit Sakuku Payment (with retry)
                SubmitSakukuResponse submitSakukuResponse = null;
                for (int paymentAttempt = 1; paymentAttempt <= 3; paymentAttempt++) {
                    try {
                        logger.log("Submit Sakuku payment (attempt " + paymentAttempt + "/3)");
                        SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, submitBookingResponse.data.orderHash, orderId);
                        submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
                        if (!submitSakukuResponse.message.equals("SUCCESS")) {
                            if (paymentAttempt == 3) {
                                Assertion.skip("Paid using SAKUKU is failed after 3 attempts. Skipping the subsequent steps.");
                            }
                            throw new RuntimeException("Sakuku payment failed: " + submitSakukuResponse.message);
                        }
                        grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
                        logger.log("Grand Total -> " + grandTotal);
                        logger.log("Reference Id -> " + submitSakukuResponse.data.sidebarPayment.referenceId);
                        break;
                    } catch (Exception e) {
                        if (paymentAttempt == 3) throw e;
                        logger.log("Sakuku payment failed, retrying...");
                        Thread.sleep(2000);
                    }
                }

                // Step 7: Check Payment Status (with retry)
                String paymentStatus = null;
                for (int statusCheckAttempt = 1; statusCheckAttempt <= 3; statusCheckAttempt++) {
                    try {
                        logger.log("Checking payment status (attempt " + statusCheckAttempt + "/3)");
                        paymentStatus = paidCheckerStatus(120);
                        if (!paymentStatus.equalsIgnoreCase("SUCCESS")) {
                            if (statusCheckAttempt == 3) {
                                Assertion.skip("Order not paid after 3 status checks. Skipping the subsequent steps.");
                            }
                            throw new RuntimeException("Payment status not SUCCESS: " + paymentStatus);
                        }
                        break;
                    } catch (Exception e) {
                        if (statusCheckAttempt == 3) throw e;
                        logger.log("Payment status check failed, retrying...");
                        Thread.sleep(5000);
                    }
                }
                
                logger.log("=== Hotel Booking Successful on attempt " + attempt + " ===");
                return; // Success, exit the method
                
            } catch (Exception e) {
                lastException = e;
                logger.log("Error occurred on attempt " + attempt + ": " + ExceptionUtils.getStackTrace(e));
                
                if (attempt < maxRetries) {
                    logger.log("Retrying entire hotel booking flow... (attempt " + (attempt + 1) + " of " + maxRetries + ")");
                    try {
                        Thread.sleep(3000); // Wait 3 seconds before retrying entire flow
                    } catch (InterruptedException ie) {
                        logger.log("Sleep interrupted: " + ie.getMessage());
                    }
                } else {
                    logger.log("All " + maxRetries + " attempts failed. Skipping the subsequent steps.");
                }
            }
        }
        
        logger.log("Hotel Booking via API failed after " + maxRetries + " attempts.");
        if (lastException != null) {
            logger.log("Last error: " + ExceptionUtils.getStackTrace(lastException));
        }
        Assertion.skip("Hotel Booking via API failed after " + maxRetries + " attempts. Skipping the subsequent steps.");
    }

    @SneakyThrows
    public void createHotelCreditCardIssuedOrderApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,

            String currency,
            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String cardNumber,
            String expiry,
            String cvv,
            String cardName
    ) {

        String[] expirySplit = expiry.split("/");
        String cardExpiredMonth = expirySplit[0];
        String cardExpiredYear = expirySplit[1];

        try {
            String accessToken = "Bearer "+accountPage.getAccessTokenUNM(username, password);
            logger.log("accessToken-> " + accessToken);
            logger.log("query " + query);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
            hotelName = hotelAutoCompleteResponse.data.contents.get(0).name;
            logger.log("hotelId-> " + hotelId);
            logger.log("hotel name->" + hotelName);

            logger.log("Date: " + startDate);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"en", currency);
            SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs);
            String roomName = selectRoomName(selectRoomApiInputs);
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName-> " + hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            String cartID = prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, currency, false, "", "id", hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();
            orderId = submitBookingResponse.data.orderId;
            orderDetailId = submitBookingResponse.data.orderDetailId;
            logger.log("orderId-> " + orderId);

            SubmitCreditCardApi.Inputs submitCreditCardApiInputs = new SubmitCreditCardApi.Inputs(accessToken, submitBookingResponse.data.orderHash, orderId, cardNumber, cardName, cardExpiredMonth, cardExpiredYear, cvv);
            JSONObject submitCreditCardResponse = submitCreditCardResponseJSON(submitCreditCardApiInputs);
            grandTotal = submitCreditCardResponse.getJSONObject("sidebarPayment").getBigInteger("grandTotal");
            logger.log("grandTotal-> " + grandTotal);

            String thirdPartyResponseUrl = submitCreditCardResponse.getJSONObject("thirdPartyResponse").getJSONObject("data").getString("acsUrl");
            logger.log("thirdPartyResponse URL -> " + thirdPartyResponseUrl);

            if (!thirdPartyResponseUrl.isEmpty()){
                if (!hitAcsUrl(thirdPartyResponseUrl)) Assertion.skip("CC Payment failed. Skipping the subsequent steps.");
            }
            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
    }

    @SneakyThrows
    public Map<String,String> createWaitingPaymentOrderApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency,
            String language
    ) throws ExecutionException, InterruptedException {
        Map<String, String> result = new HashMap<>();
        String accessToken = "Bearer "+accountPage.getAccessTokenUNM(username, password);
        logger.log("accessToken-> " + accessToken);

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        String hotelId=null;
        for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
            if (content.name.toLowerCase().contains(query.toLowerCase())) {
                hotelId = content.publicId;
                break;
            }
        }
        logger.log("hotelId -> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"id", currency);
        SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs);
        String roomName = selectRoomName(selectRoomApiInputs);
        hotelRoomName = roomName;
        int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
        int maxChildOccupancy = selectedRoom.maxChildOccupancy;
        logger.log("hotelRoomName-> " + hotelRoomName);

        PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
        String cartID = prebook(preBookCartApiInputs);
        logger.log("cartID-> " + cartID);

        SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, currency,false, "", language, hotelId, startDate);
        SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
        guestNames = submitBookingApiInputs.getGuestNames();
        orderId = submitBookingResponse.data.orderId;
        orderHash = submitBookingResponse.data.orderHash;
        grandTotal = BigInteger.valueOf(submitBookingResponse.data.price.intValue());
        logger.log("orderId-> " + orderId);
        logger.log("orderHash-> " + orderHash);
        String orderIdStr = Integer.toString(orderId);
        result.put("orderId", orderIdStr);
        result.put("orderHash", orderHash);
        return result;
    }

    @SneakyThrows
    public void createHotelFullTiketPointsIssuedOrderApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,

            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            int accountId,
            String currency
    ) {
        try {
            accessToken = "Bearer "+accountPage.getAccessTokenUNM(username, password);

            logger.log("Hotel Name -> " + query);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
            hotelName = hotelAutoCompleteResponse.data.contents.get(0).name;
            logger.log("hotelId-> " + hotelId);
            logger.log("hotel name->" + hotelName);

            logger.log("Date: " + startDate);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"en", "");
            SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs);
            String roomName = selectRoomName(selectRoomApiInputs);
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName-> " + hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            String cartID = prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, "", false, "", "en", hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();
            orderId = submitBookingResponse.data.orderId;
            orderDetailId = submitBookingResponse.data.orderDetailId;
            logger.log("orderId-> " + orderId);

            InternalOrderDetailApi.Inputs input = new InternalOrderDetailApi.Inputs(String.valueOf(orderId));
            InternalOrderDetailApi internalOrderDetailApi = new InternalOrderDetailApi(input);
            ApiResponse response = internalOrderDetailApi.hitApi(apiTools);
            JSONObject internalOrderData = new JSONObject(response.getBody()).getJSONObject("data");
            String totalPayment = "";
            try {
                totalPayment = String.valueOf(internalOrderData.getInt("totalAmount"));
            } catch (JSONException e) {
                totalPayment = String.valueOf(internalOrderData.getInt("price"));
            }
            logger.log("Total payment -> "+totalPayment);

            injectTiketPointWithThreshold(accountId, "REWARD", 1, username, password, Integer.parseInt(totalPayment));

            ApplyTiketPointsApiV2.Inputs applyTiketPointsApiInputs = new ApplyTiketPointsApiV2.Inputs(accessToken, orderId, "TIXHOTEL");
            ApplyTiketPointsResponse applyTiketPointsResponse = applyTiketPointV2(applyTiketPointsApiInputs);
            boolean tiketPointsStatus = orderId == applyTiketPointsResponse.data.orderId;
            logger.log("tiketPointsStatus-> " + tiketPointsStatus);

            SubmitTiketPointsApi.Inputs submitTiketPointsApiInputs = new SubmitTiketPointsApi.Inputs(String.valueOf(orderId), submitBookingResponse.data.orderHash);
            SubmitTiketPointsResponse submitTiketPointsResponse = paidTiketPoints(submitTiketPointsApiInputs);

            if (!submitTiketPointsResponse.message.equals("SUCCESS")) Assertion.skip("Tiket point payment is failed. Skipping the subsequent steps.");
            grandTotal = submitTiketPointsResponse.data.sidebarPayment.subTotal.toBigInteger();
            logger.log("Grand Total -> " + grandTotal);
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
    }

    @SneakyThrows
    public void createIssuedHotelOrderApiB2BCorp(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,

            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            EmployeeData employeeData,
            boolean selectRoomMostExpansive,
            String currency
            ) {
        try {
            String accessToken = "Bearer " + accountPage.getAccessTokenUNM(username,password);
            logger.log("accessToken for dashboard request->" + accessToken);

            logger.log("query " + query);
            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            String hotelId=null;
            hotelName=null;
            for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
                if (query.equalsIgnoreCase(content.name)) {
                    hotelId = content.publicId;
                    hotelName = content.name;
                    break;
                }
            }
            logger.log("hotelId-> " + hotelId);
            logger.log("hotel name->" + hotelName);
            logger.log("Date: "+startDate);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"id", "");
            SelectRoomResponse.RatePlan selectedRoom;
            if (selectRoomMostExpansive) {
                selectedRoom = selectRoomMostExpansive(selectRoomApiInputs);
            } else {
                selectedRoom = selectRoom(selectRoomApiInputs);
            }
            String roomName = selectRoomName(selectRoomApiInputs);
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName-> " + hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            String cartID = prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, "",false, "", "id", hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();
            orderId = submitBookingResponse.data.orderId;
            logger.log("orderId-> " + orderId);

            SubmitPaymentApiB2B.Inputs submitPaymentApiInputs = new SubmitPaymentApiB2B.Inputs(accessToken, submitBookingResponse.data.orderHash, orderId, employeeData.data.employeeID,employeeData.data.corporateID);
            String paymentResponse = submitPaymentB2B(submitPaymentApiInputs);
            logger.log("paymentStatus"+ paymentResponse);

            SubmitPaymentPutApiB2B.Inputs submitPaymentPutApiInputs = new SubmitPaymentPutApiB2B.Inputs(accessToken,submitBookingResponse.data.orderHash, orderId, employeeData.data.employeeID,employeeData.data.corporateID, "testing only");
            String paymentResponsePut = submitPaymentB2BPUT(submitPaymentPutApiInputs);
            logger.log("paymentStatus"+ paymentResponsePut);

            if(paymentResponsePut.contains("CORPORATE_BILLING_ERROR")){
                Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
            }
        }
        catch(Exception e){
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
    }
    // Order Creation TTD
    private HashMap<String, String> ttdSelectProduct(TTDSelectProductApi.Inputs inputs, boolean refundable) throws ExecutionException, InterruptedException {
        TTDSelectProductApi ttdSelectProductApi = new TTDSelectProductApi(inputs);
        ApiResponse ttdSelectProductResponse = ttdSelectProductApi.hitApi(apiTools);
        JSONObject ttdSelectProductResponseObject = new JSONObject(ttdSelectProductResponse.getBody());
        JSONObject data = ttdSelectProductResponseObject.getJSONObject("data");

        String productID = data.getString("id");
        String productName = data.getJSONArray("translations").getJSONObject(0).getString("title");
        String productCategory = data.getString("primaryCategory");

        HashMap<String, String> result = new HashMap<>();
        result.put("productID", productID);
        result.put("productName", productName);
        result.put("productCategory", productCategory);

        for (int i = 0; i < data.getJSONArray("packages").length(); i++) {
            String packageCode = data.getJSONArray("packages").getJSONObject(i).getString("code");
            String displayRefundTier = data.getJSONArray("packages").getJSONObject(i).getString("displayRefundTier");
            boolean zeroQuestionnaire = data.getJSONArray("packages").getJSONObject(i).getJSONArray("questionnaires").isEmpty();
            if (!displayRefundTier.equalsIgnoreCase("NON_REFUND") && zeroQuestionnaire) {
                result.put("refundablePackageCode", packageCode);
                break;
            }
        }

        return result;
    }

    private String ttdChoosePackageApi(TTDChoosePackageApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDChoosePackageApi ttdChoosePackageApi = new TTDChoosePackageApi(inputs);
        ApiResponse ttdChoosePackageResponse = ttdChoosePackageApi.hitApi(apiTools);
        JSONObject ttdChoosePackageResponseObject = new JSONObject(ttdChoosePackageResponse.getBody());
        JSONArray data = ttdChoosePackageResponseObject.getJSONArray("data").getJSONObject(0).getJSONArray("packageCodes");

        return data.getString(0);
    }

    private String ttdChoosePackageScheduleApi(TTDChoosePackageScheduleApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDChoosePackageScheduleApi ttdChoosePackageScheduleApi = new TTDChoosePackageScheduleApi(inputs);
        ApiResponse ttdChoosePackageScheduleResponse = ttdChoosePackageScheduleApi.hitApi(apiTools);
        JSONObject ttdChoosePackageScheduleResponseObject = new JSONObject(ttdChoosePackageScheduleResponse.getBody());
        logger.log("Choose package schedule response : " + ttdChoosePackageScheduleResponseObject.toString());
        JSONObject data = ttdChoosePackageScheduleResponseObject.getJSONArray("data").getJSONObject(ttdChoosePackageScheduleResponseObject.getJSONArray("data").length()-1);

        return data.getString("id");
    }

    // Step 2 of TTD end to end booking chain: get the first product schedule id
    private String ttdGetProductSchedulesApi(TTDGetProductSchedulesApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDGetProductSchedulesApi ttdGetProductSchedulesApi = new TTDGetProductSchedulesApi(inputs);
        ApiResponse ttdGetProductSchedulesResponse = ttdGetProductSchedulesApi.hitApi(apiTools);
        JSONObject ttdGetProductSchedulesResponseObject = new JSONObject(ttdGetProductSchedulesResponse.getBody());
        logger.log("Get product schedules response : " + ttdGetProductSchedulesResponseObject.toString());
        JSONObject firstSchedule = ttdGetProductSchedulesResponseObject.getJSONArray("data").getJSONObject(0);

        return firstSchedule.getString("id");
    }

    /** Step 2 query params for {@link TTDGetProductSchedulesApi}, all sourced from the test data sheet. */
    public record TTDScheduleSearchParams(
            String packageCode,
            String sortAttributes,
            String sortDirection,
            String dateTimeFrom,
            String dateTimeTo,
            String pageSize,
            String pageNumber
    ) {}

    @SneakyThrows
    public String ttdGetFirstProductScheduleId(String accessToken, String productId, TTDScheduleSearchParams search) {
        TTDGetProductSchedulesApi.Inputs inputs = new TTDGetProductSchedulesApi.Inputs(
                accessToken,
                productId,
                search.packageCode(),
                search.sortAttributes(),
                search.sortDirection(),
                search.dateTimeFrom(),
                search.dateTimeTo(),
                search.pageSize(),
                search.pageNumber()
        );
        return ttdGetProductSchedulesApi(inputs);
    }

    /** Contact/collector/price-tier details for Step 3, all sourced from the test data sheet. */
    public record TTDTravellerDetails(
            String salutation,
            String firstName,
            String lastName,
            String emailAddress,
            String countryCode,
            String phoneNumber,
            String nationality,
            long profileId,
            String priceTierCode,
            int quantity,
            String currency
    ) {}

    // Step 3 of TTD end to end booking chain: create the order using the Step 2 product schedule id
    @SneakyThrows
    public ApiResponse ttdCreateOrderApiV2(String accessToken, String productScheduleId, TTDTravellerDetails traveller) {
        TTDCreateOrderV2Api.Inputs inputs = new TTDCreateOrderV2Api.Inputs(
                accessToken,
                productScheduleId,
                traveller.salutation(),
                traveller.firstName(),
                traveller.lastName(),
                traveller.emailAddress(),
                traveller.countryCode(),
                traveller.phoneNumber(),
                traveller.nationality(),
                traveller.profileId(),
                traveller.priceTierCode(),
                traveller.quantity(),
                traveller.currency()
        );
        TTDCreateOrderV2Api ttdCreateOrderV2Api = new TTDCreateOrderV2Api(inputs);
        ApiResponse response = ttdCreateOrderV2Api.hitApi(apiTools);
        logger.log("Create order (v2) response : " + response.getBody());
        return response;
    }

    // Step 4 of TTD end to end booking chain: poll for the coreOrderId/coreOrderHash using the Step 3 create-order id
    @SneakyThrows
    public HashMap<String, String> ttdCheckOrderIdHash(String accessToken, ApiResponse createOrderResponse) {
        JSONObject createOrderResponseObject = new JSONObject(createOrderResponse.getBody());
        String createOrderId = createOrderResponseObject.getJSONObject("data").getString("id");
        logger.log("createOrderID-> " + createOrderId);

        TTDCheckOrderIDHashApi.Inputs inputs = new TTDCheckOrderIDHashApi.Inputs(accessToken, createOrderId);
        return ttdCheckOrderIDHashApi(inputs);
    }

    // Step 5 of TTD end to end booking chain: submit SAKUKU payment using the Step 4 orderHash/referenceId,
    // via the shared SubmitSakukuApi (bypasses gateway, hits payment cluster directly)
    @SneakyThrows
    public ApiResponse ttdSubmitSakukuV2(String accessToken, String orderHash, long referenceId) {
        SubmitSakukuApi.Inputs inputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, referenceId);
        SubmitSakukuApi submitSakukuApi = new SubmitSakukuApi(inputs);
        ApiResponse response = submitSakukuApi.hitApi(apiTools);
        logger.log("Submit Sakuku (v2) response : " + response.getBody());
        return response;
    }

    /** Result of {@link #createTTDBookingViaApi}: the Sakuku payment response plus the order id it paid for. */
    public record TTDBookingResult(ApiResponse paymentResponse, long orderId) {}

    // TTD end to end booking via API: chains Steps 1-5 (access token -> product schedule -> create order -> check order id hash -> submit Sakuku payment)
    @SneakyThrows
    public TTDBookingResult createTTDBookingViaApi(String username, String password, String productId, TTDScheduleSearchParams search, TTDTravellerDetails traveller) {
        String accessToken = "Bearer " + accountPage.getAccessTokenUNM(username, password);
        logger.log("accessToken-> " + accessToken);

        String productScheduleId = ttdGetFirstProductScheduleId(accessToken, productId, search);
        logger.log("productScheduleId-> " + productScheduleId);

        ApiResponse createOrderResponse = ttdCreateOrderApiV2(accessToken, productScheduleId, traveller);

        HashMap<String, String> orderIdHash = ttdCheckOrderIdHash(accessToken, createOrderResponse);
        String orderHash = orderIdHash.get("orderHash");
        long referenceId = Long.parseLong(orderIdHash.get("orderID"));
        logger.log("orderHash-> " + orderHash + ", referenceId-> " + referenceId);

        ApiResponse paymentResponse = ttdSubmitSakukuV2(accessToken, orderHash, referenceId);
        return new TTDBookingResult(paymentResponse, referenceId);
    }

    private List<String> ttdCheckAvailablePriceTier(TTDSelectProductApi.Inputs inputs, String packageCode, long minPrice) throws ExecutionException, InterruptedException {
        TTDSelectProductApi ttdSelectProductApi = new TTDSelectProductApi(inputs);
        ApiResponse ttdSelectProductResponse = ttdSelectProductApi.hitApi(apiTools);
        JSONObject ttdSelectProductResponseObject = new JSONObject(ttdSelectProductResponse.getBody());

        JSONArray packagesData = ttdSelectProductResponseObject.getJSONObject("data").getJSONArray("packages");
        List<String> availablePriceTiers = new ArrayList<>();

        for (Object packageDetail : packagesData) {
            if (packageDetail instanceof JSONObject) {
                JSONObject packageDetail2 = (JSONObject) packageDetail;
                if (packageDetail2.getString("code").equalsIgnoreCase(packageCode)) {
                    JSONArray availablePriceTier = packageDetail2.getJSONArray("priceTiers");
                    availablePriceTier.forEach(item -> {
                        JSONObject obj = (JSONObject) item;
                        if (obj.getLong("finalPrice") > minPrice) availablePriceTiers.add(obj.getString("code"));
                    });
                    break;
                }
            }
        }
        logger.log("Available price tiers: "+availablePriceTiers);
        return availablePriceTiers;
    }

    private String ttdCreateOrderApi(TTDCreateOrderApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDCreateOrderApi ttdCreateOrderApi = new TTDCreateOrderApi(inputs);
        ApiResponse ttdCreateOrderResponse = ttdCreateOrderApi.hitApi(apiTools);
        JSONObject ttdCreateOrderResponseObject = new JSONObject(ttdCreateOrderResponse.getBody());
        logger.log("response: \n" + ttdCreateOrderResponse.getBody());
        JSONObject data = ttdCreateOrderResponseObject.getJSONObject("data");
        logger.log("data: \n" + ttdCreateOrderResponseObject.toString());
        return data.getString("id");
    }

    private HashMap<String, String> ttdCheckOrderIDHashApi(TTDCheckOrderIDHashApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDCheckOrderIDHashApi ttdCheckOrderIDHashApi = new TTDCheckOrderIDHashApi(inputs);
        JSONObject data = new JSONObject();
        HashMap<String, String> result = new HashMap<>();

        for (int i = 0; i < 30; i++) {
            ApiResponse ttdCheckOrderIDHashResponse = ttdCheckOrderIDHashApi.hitApi(apiTools);
            JSONObject ttdCheckOrderIDHashResponseObject = new JSONObject(ttdCheckOrderIDHashResponse.getBody());
            data = ttdCheckOrderIDHashResponseObject.getJSONObject("data");

            if (data.isNull("coreOrderId") || data.isNull("coreOrderHash")) {
                continue;
            } else {
                result.put("orderID", data.getString("coreOrderId"));
                result.put("orderHash", data.getString("coreOrderHash"));
                break;
            }
        }

        return result;
    }

    private SubmitCreditCardResponse submitCreditCardResponse(SubmitCreditCardApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitCreditCardApi submitCreditCardApi = new SubmitCreditCardApi(inputs);
        ApiResponse submitCreditCard = submitCreditCardApi.hitApi(apiTools);
        Gson gson = new Gson();
        logger.log("response submit cc : " + submitCreditCard.getBody());
        return gson.fromJson(submitCreditCard.getBody(), SubmitCreditCardResponse.class);
    }

    public JSONObject submitCreditCardResponseJSON(SubmitCreditCardApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitCreditCardApi submitCreditCardApi = new SubmitCreditCardApi(inputs);
        ApiResponse submitCreditCard = submitCreditCardApi.hitApi(apiTools);

        JSONObject responseArray = new JSONObject(submitCreditCard.getBody());
        return responseArray.getJSONObject("data");
    }

    public JSONObject submitCreditCardV2ResponseJSON(SubmitCreditCardV2.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitCreditCardV2 submitCreditCardV2 = new SubmitCreditCardV2(inputs);
        ApiResponse submitCreditCard = submitCreditCardV2.hitApi(apiTools);

        String responseBody = submitCreditCard.getBody();
        logger.log(responseBody);
        return new JSONObject(responseBody);
    }

    public JSONObject submitBookingLanding(LandingPageApiV2.Input bookingData) throws InterruptedException, ExecutionException {
        LandingPageApiV2 landingApi = new LandingPageApiV2(bookingData);
        ApiResponse landingApiV2Response = landingApi.hitApi(apiTools);
        String responseBody = landingApiV2Response.getBody();
        logger.log(responseBody);
        return new JSONObject(responseBody);
    }

    @SneakyThrows
    public Boolean createWaitingForPaymentOrderUsingCCApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String cardNumber,
            String cardExpiredMonth,
            String cardExpiredYear,
            String cvv,
            String cardName,
            String currency
    ) {
        try {
            String accessToken = "Bearer "+accountPage.getAccessTokenUNM(username, password);
            logger.log("accessToken-> " + accessToken);
            logger.log("query " + query);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
            hotelName = hotelAutoCompleteResponse.data.contents.get(0).name;
            logger.log("hotelId-> " + hotelId);
            logger.log("hotel name->" + hotelName);
            logger.log("Date: "+startDate);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"en", "");
            SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs);
            String roomName = selectRoomName(selectRoomApiInputs);
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName-> " + hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            String cartID = prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, "",false, "", "id", hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();
            orderId = submitBookingResponse.data.orderId;
            logger.log("orderId-> " + orderId);

            SubmitCreditCardApi.Inputs submitCreditCardApiInputs = new SubmitCreditCardApi.Inputs (accessToken, submitBookingResponse.data.orderHash, orderId, cardNumber, cardName, cardExpiredMonth, cardExpiredYear, cvv);
            SubmitCreditCardResponse submitCreditCardResponse = submitCreditCardResponse(submitCreditCardApiInputs);
        }
        catch(Exception e){
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
                Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
            }
        return false;
    }

    public Boolean openUrl(String url) throws InterruptedException, ExecutionException{
        HitGetUrl.Inputs input = new HitGetUrl.Inputs(url);
        HitGetUrl hitGetUrl = new HitGetUrl(input);
        ApiResponse response = hitGetUrl.hitApi(apiTools);
        int actualStatusCode = response.getStatusCode();
        logger.log("actualStatusCode-> " + actualStatusCode);
        return actualStatusCode == 200;
    }

    public SubmitSakukuResponse submitSakuku(SubmitSakukuApi.Inputs inputs) throws InterruptedException, ExecutionException {
        SubmitSakukuApi submitSakukuApi = new SubmitSakukuApi(inputs);
        ApiResponse apiResponse = null;
        for (int i = 0; i < 10; i++) {
            try {
                apiResponse = submitSakukuApi.hitApi(apiTools);
                break;
            } catch (ApiEnvironmentException e) {
                logger.log("Hitting SAKUKU payment failed. Hitting again for 10 times until succeed.");
            }
        }

        if (apiResponse == null) {
            logger.log("Failed to submit SAKUKU payment after 10 attempts. apiResponse is null.");
            throw new IllegalStateException("submitSakuku failed after 10 retries: no successful API response received");
        }

        Gson gson = new Gson();
        logger.log("response submit Sakuku : " + apiResponse.getBody());
        SubmitSakukuResponse submitSakukuResponse = gson.fromJson(apiResponse.getBody(), SubmitSakukuResponse.class);
        return submitSakukuResponse;
    }

    @SneakyThrows
    public void createIssuedOrderApiTodo(
            String username,
            String password,
            String productUrlName,
            boolean refundable,
            String currency,

            String contactFullName,
            String contactSalutation,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation
    ) {
        try{
            String accessToken = "Bearer "+accountPage.getAccessTokenUNM(username, password);
            logger.log("accessToken-> " + accessToken);

            TTDSelectProductApi.Inputs ttdProductSelectApiInputs = new TTDSelectProductApi.Inputs(accessToken, productUrlName);
            HashMap<String, String> selectedProductData = ttdSelectProduct(ttdProductSelectApiInputs, refundable);
            productName = selectedProductData.get("productName");
            String selectedProductID = selectedProductData.get("productID");
            productCategory = selectedProductData.get("productCategory");
            logger.log("selectedProductID-> " + selectedProductID);
            logger.log("selectedProductName-> " + productName);
            logger.log("selectedProductCategory-> " + productCategory);

            String selectedPackageID = "";
            if (refundable) {
                String refundablePackageCode = selectedProductData.get("refundablePackageCode");
                logger.log("refundablePackageCode-> " + refundablePackageCode);

                if (refundablePackageCode != null) {
                    selectedPackageID += refundablePackageCode;
                } else {
                    TTDChoosePackageApi.Inputs ttdChoosePackageApiInputs = new TTDChoosePackageApi.Inputs(accessToken, selectedProductID);
                    selectedPackageID += ttdChoosePackageApi(ttdChoosePackageApiInputs);
                }
                logger.log("PackageID-> " + selectedPackageID);
            } else {
                TTDChoosePackageApi.Inputs ttdChoosePackageApiInputs = new TTDChoosePackageApi.Inputs(accessToken, selectedProductID);
                selectedPackageID += ttdChoosePackageApi(ttdChoosePackageApiInputs);
                logger.log("PackageID-> " + selectedPackageID);
            }

            TTDChoosePackageScheduleApi.Inputs ttdChoosePackageScheduleApiInputs = new TTDChoosePackageScheduleApi.Inputs(accessToken, selectedProductID, selectedPackageID, 3, 1);
            String selectedPackageScheduleID = ttdChoosePackageScheduleApi(ttdChoosePackageScheduleApiInputs);
            logger.log("PackageScheduleID-> " + selectedPackageScheduleID);

            List<String> availablePriceTier = ttdCheckAvailablePriceTier(ttdProductSelectApiInputs, selectedPackageID, 10000);
            String selectedPriceTiers = availablePriceTier.get(CoreFunctions.generateRandomInt(0, availablePriceTier.size() - 1));
            logger.log("selectedPriceTiers-> " + selectedPriceTiers);

            TTDCreateOrderApi.Inputs ttdCreateOrderApiInputs = TTDCreateOrderApi.Inputs.legacy(currency, accessToken, selectedPackageScheduleID, contactFullName, username, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, selectedPriceTiers,0);
            String createOrderID = ttdCreateOrderApi(ttdCreateOrderApiInputs);
            logger.log("createOrderID-> " + createOrderID);

            TTDCheckOrderIDHashApi.Inputs ttdCheckOrderIDHashApiInputs = new TTDCheckOrderIDHashApi.Inputs(accessToken, createOrderID);
            HashMap<String, String> createOrderIDHash = ttdCheckOrderIDHashApi(ttdCheckOrderIDHashApiInputs);
            orderId = Integer.parseInt(createOrderIDHash.get("orderID"));
            String orderHash = createOrderIDHash.get("orderHash");
            logger.log("OrderID-> " + orderId);
            logger.log("OrderHash-> " + orderHash);

            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, orderId);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!submitSakukuResponse.message.equals("SUCCESS")) Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            List<SubmitSakukuResponse.OrderDetail> orderDetails = submitSakukuResponse.data.sidebarPayment.orderDetail;
            for (SubmitSakukuResponse.OrderDetail orderDetail : orderDetails) {
                if (orderDetail.productType.equals("EVENT")) {
                    orderDetailId = Integer.parseInt(orderDetail.referenceDetailId);
                    logger.log("Order Detail ID -> "+orderDetailId);
                    break;
                }
            }
            logger.log("Grand Total -> "+grandTotal);
            logger.log("Reference Id -> "+submitSakukuResponse.data.sidebarPayment.referenceId);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");
        }catch(Exception e){
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
                Assertion.skip("TTD Booking via API failed. Skipping the subsequent steps.");
        }
    }

    @SneakyThrows
    public void createIssuedOrderApiTodoCard(
            String username,
            String password,
            String currency,

            String productUrlName,
            boolean refundable,

            String contactFullName,
            String contactSalutation,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation,

            String cardNumber,
            String expiry,
            String cvv,
            String cardName

    ) {
        try{
            String accessToken = "Bearer "+accountPage.getAccessTokenUNM(username, password);
            logger.log("accessToken-> " + accessToken);

            TTDSelectProductApi.Inputs ttdProductSelectApiInputs = new TTDSelectProductApi.Inputs(accessToken, productUrlName);
            HashMap<String, String> selectedProductData = ttdSelectProduct(ttdProductSelectApiInputs, refundable);
            productName = selectedProductData.get("productName");
            String selectedProductID = selectedProductData.get("productID");
            productCategory = selectedProductData.get("productCategory");
            logger.log("selectedProductID-> " + selectedProductID);
            logger.log("selectedProductName-> " + productName);
            logger.log("selectedProductCategory-> " + productCategory);

            String selectedPackageID = "";
            if (refundable) {
                String refundablePackageCode = selectedProductData.get("refundablePackageCode");
                logger.log("refundablePackageCode-> " + refundablePackageCode);

                selectedPackageID += refundablePackageCode;
                logger.log("PackageID-> " + selectedPackageID);
            } else {
                TTDChoosePackageApi.Inputs ttdChoosePackageApiInputs = new TTDChoosePackageApi.Inputs(accessToken, selectedProductID);
                selectedPackageID += ttdChoosePackageApi(ttdChoosePackageApiInputs);
                logger.log("PackageID-> " + selectedPackageID);
            }

            TTDChoosePackageScheduleApi.Inputs ttdChoosePackageScheduleApiInputs = new TTDChoosePackageScheduleApi.Inputs(accessToken, selectedProductID, selectedPackageID, 3, 1);
            String selectedPackageScheduleID = ttdChoosePackageScheduleApi(ttdChoosePackageScheduleApiInputs);
            logger.log("PackageScheduleID-> " + selectedPackageScheduleID);

            List<String> availablePriceTier = ttdCheckAvailablePriceTier(ttdProductSelectApiInputs, selectedPackageID, 10000);
            String selectedPriceTiers = availablePriceTier.get(CoreFunctions.generateRandomInt(0, availablePriceTier.size() - 1));
            logger.log("selectedPriceTiers-> " + selectedPriceTiers);

            TTDCreateOrderApi.Inputs ttdCreateOrderApiInputs = TTDCreateOrderApi.Inputs.legacy(currency, accessToken, selectedPackageScheduleID, contactFullName, username, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, selectedPriceTiers,0);
            String createOrderID = ttdCreateOrderApi(ttdCreateOrderApiInputs);
            logger.log("createOrderID-> " + createOrderID);

            TTDCheckOrderIDHashApi.Inputs ttdCheckOrderIDHashApiInputs = new TTDCheckOrderIDHashApi.Inputs(accessToken, createOrderID);
            HashMap<String, String> createOrderIDHash = ttdCheckOrderIDHashApi(ttdCheckOrderIDHashApiInputs);
            orderId = Integer.parseInt(createOrderIDHash.get("orderID"));
            String orderHash = createOrderIDHash.get("orderHash");
            logger.log("OrderID-> " + orderId);
            logger.log("OrderHash-> " + orderHash);

            String[] expirySplit = expiry.split("/");
            String cardExpiredMonth = expirySplit[0];
            String cardExpiredYear = expirySplit[1];

            SubmitCreditCardApi.Inputs submitCreditCardApiInputs = new SubmitCreditCardApi.Inputs(accessToken, orderHash, orderId, cardNumber, cardName, cardExpiredMonth, cardExpiredYear, cvv);
            JSONObject submitCreditCardResponse = submitCreditCardResponseJSON(submitCreditCardApiInputs);
            grandTotal = submitCreditCardResponse.getJSONObject("sidebarPayment").getBigInteger("grandTotal");
            logger.log("grandTotal-> " + grandTotal);

            String thirdPartyResponseUrl = submitCreditCardResponse.getJSONObject("thirdPartyResponse").getJSONObject("data").getString("acsUrl");
            logger.log("thirdPartyResponse URL -> " + thirdPartyResponseUrl);
            if (!thirdPartyResponseUrl.equalsIgnoreCase("")) {
                if (!hitAcsUrl(thirdPartyResponseUrl)) Assertion.skip("CC Payment failed. Skipping the subsequent steps.");
            }
            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");
        }catch(Exception e){
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
                Assertion.skip("TTD Booking via API failed. Skipping the subsequent steps.");
        }
    }

    @SneakyThrows
    public void createGuestWaitingPaymentOrderApiTodo(
            String productUrlName,
            boolean refundable,
            String currency,
            String contactFullName,
            String contactSalutation,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation
    ) {
        createGuestWaitingPaymentOrderApiTodo(productUrlName, refundable, currency, contactFullName,
                contactSalutation, guestFullName, guestEmailAddress, guestSalutation, 1);
    }

    @SneakyThrows
    public void createGuestWaitingPaymentOrderApiTodo(
            String productUrlName,
            boolean refundable,
            String currency,
            String contactFullName,
            String contactSalutation,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation,
            int guestCount
    ) {
        try{
            String accessToken = getToken();
            logger.log("accessToken-> " + accessToken);

            TTDSelectProductApi.Inputs ttdProductSelectApiInputs = new TTDSelectProductApi.Inputs(accessToken, productUrlName);
            HashMap<String, String> selectedProductData = ttdSelectProduct(ttdProductSelectApiInputs, refundable);
            productName = selectedProductData.get("productName");
            String selectedProductID = selectedProductData.get("productID");
            productCategory = selectedProductData.get("productCategory");
            logger.log("selectedProductID-> " + selectedProductID);
            logger.log("selectedProductName-> " + productName);
            logger.log("selectedProductCategory-> " + productCategory);

            String selectedPackageID = "";
            if (refundable) {
                String refundablePackageCode = selectedProductData.get("refundablePackageCode");
                logger.log("refundablePackageCode-> " + refundablePackageCode);

                selectedPackageID += refundablePackageCode;
                logger.log("PackageID-> " + selectedPackageID);
            } else {
                TTDChoosePackageApi.Inputs ttdChoosePackageApiInputs = new TTDChoosePackageApi.Inputs(accessToken, selectedProductID);
                selectedPackageID += ttdChoosePackageApi(ttdChoosePackageApiInputs);
                logger.log("PackageID-> " + selectedPackageID);
            }

            TTDChoosePackageScheduleApi.Inputs ttdChoosePackageScheduleApiInputs = new TTDChoosePackageScheduleApi.Inputs(accessToken, selectedProductID, selectedPackageID, 7, 1);
            String selectedPackageScheduleID = ttdChoosePackageScheduleApi(ttdChoosePackageScheduleApiInputs);
            logger.log("PackageScheduleID-> " + selectedPackageScheduleID);

            List<String> availablePriceTier = ttdCheckAvailablePriceTier(ttdProductSelectApiInputs, selectedPackageID, 10000);
            String selectedPriceTiers = availablePriceTier.get(CoreFunctions.generateRandomInt(0, availablePriceTier.size() - 1));
            logger.log("selectedPriceTiers-> " + selectedPriceTiers);

            TTDCreateOrderApi.Inputs ttdCreateOrderApiInputs = TTDCreateOrderApi.Inputs.legacy(currency, accessToken, selectedPackageScheduleID, contactFullName, guestEmailAddress, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, selectedPriceTiers, 0, guestCount);
            String createOrderID = ttdCreateOrderApi(ttdCreateOrderApiInputs);
            logger.log("createOrderID-> " + createOrderID);

            TTDCheckOrderIDHashApi.Inputs ttdCheckOrderIDHashApiInputs = new TTDCheckOrderIDHashApi.Inputs(accessToken, createOrderID);
            HashMap<String, String> createOrderIDHash = ttdCheckOrderIDHashApi(ttdCheckOrderIDHashApiInputs);
            orderId = Integer.parseInt(createOrderIDHash.get("orderID"));
            orderHash = createOrderIDHash.get("orderHash");
            logger.log("OrderID-> " + orderId);
            logger.log("OrderHash-> " + orderHash);
        } catch(Exception e){
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
                Assertion.skip("TTD Booking via API failed. Skipping the subsequent steps.");
        }
    }

    // Order Creation Car Rental
    private List<String> carRentalAutoComplete(CarRentalAutoCompleteApi.Inputs inputs) throws ExecutionException, InterruptedException {
        CarRentalAutoCompleteApi carRentalAutoCompleteApi = new CarRentalAutoCompleteApi(inputs);
        ApiResponse carRentalAutoCompleteResponse = carRentalAutoCompleteApi.hitApi(apiTools);
        Gson gson = new Gson();
        CarRentalAutoCompleteResponse response = gson.fromJson(carRentalAutoCompleteResponse.getBody(), CarRentalAutoCompleteResponse.class);
        regionalId = response.data.get(0).regionalId;
        placeId = response.data.get(0).placeId;
        return Arrays.asList(regionalId, placeId);
    }

    private List<String> carRentalSearchLocation(CarRentalSearchLocationApi.Inputs inputs) throws ExecutionException, InterruptedException {
        CarRentalSearchLocationApi carRentalSearchLocationApi = new CarRentalSearchLocationApi(inputs);
        ApiResponse carRentalSearchLocationResponse = carRentalSearchLocationApi.hitApi(apiTools);
        Gson gson = new Gson();
        CarRentalSearchLocationResponse response = gson.fromJson(carRentalSearchLocationResponse.getBody(), CarRentalSearchLocationResponse.class);
        latitude = response.data.latitude;
        longitude = response.data.longitude;
        return Arrays.asList(latitude, longitude);
    }

    private String carRentalGetLocationId(CarRentalGetLocationIdApi.Inputs inputs) throws ExecutionException, InterruptedException {
        CarRentalGetLocationIdApi carRentalGetLocationIdApi = new CarRentalGetLocationIdApi(inputs);
        ApiResponse carRentalGetLocationIdResponse = carRentalGetLocationIdApi.hitApi(apiTools);
        Gson gson = new Gson();
        CarRentalGetLocationIdResponse response = gson.fromJson(carRentalGetLocationIdResponse.getBody(), CarRentalGetLocationIdResponse.class);
        locationId = response.data.zone.id;
        return locationId;
    }

    private List<String> selectCarRental(SelectCarRentalApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SelectCarRentalApi selectCarRentalApi = new SelectCarRentalApi(inputs);
        ApiResponse selectCarRentalResponse = selectCarRentalApi.hitApi(apiTools);
        Gson gson = new Gson();
        SelectCarRentalResponse response = gson.fromJson(selectCarRentalResponse.getBody(), SelectCarRentalResponse.class);
        logger.log("response car rental : " + selectCarRentalResponse.getBody());
        carRegionalId = response.data.fleets.get(0).carRegionalId;
        carName = response.data.fleets.get(0).name;
        carRentalLocation = response.data.regionName;
        return Arrays.asList(carRegionalId, carName, carRentalLocation);
    }

    private com.tiket.pojo.carrental.SubmitBookingResponse submitBookingCar(com.tiket.api.carrental.SubmitBookingApi.Inputs inputs) throws ExecutionException, InterruptedException {
        com.tiket.api.carrental.SubmitBookingApi submitBookingApi = new com.tiket.api.carrental.SubmitBookingApi(inputs);
        ApiResponse submitBookingApiResponse = submitBookingApi.hitApi(apiTools);
        logger.log("Api response : \n" + submitBookingApiResponse.getBody());
        Gson gson = new Gson();
        return gson.fromJson(submitBookingApiResponse.getBody(), com.tiket.pojo.carrental.SubmitBookingResponse.class);
    }

    private String getAdminAccessTokenDashboardAuth() throws ExecutionException, InterruptedException{
        POSTGetAdminAccessTokenDashboardApi postGetAdminAccessTokenDashboardApi = new POSTGetAdminAccessTokenDashboardApi();
        ApiResponse response = postGetAdminAccessTokenDashboardApi.hitApi(apiTools);
        String isSuccessAuth = new JSONArray(response.getBody()).getJSONObject(0).getJSONObject("data").getJSONObject("loginUser").get("message").toString();
        if("SUCCESS".equals(isSuccessAuth)){
            Map<String,ArrayList<Object>> header = response.getHeaderParams();
            ArrayList<Object>  listSetCookies = header.get("set-cookie");
            String accessToken = listSetCookies.get(0).toString().contains("session_access_token")?listSetCookies.get(0).toString():listSetCookies.get(1).toString();
            logger.log("token : " + accessToken);
            int start = accessToken.indexOf('=') + 1;
            int end= accessToken.indexOf(';');
            accessToken = accessToken.substring(start,end);
            logger.log("session_access_token= " + accessToken);
            return accessToken;
        }
        return null;
    }

    public String confirmOrder(String orderId,String adminName,String adminPhoneNumber) throws ExecutionException, InterruptedException{
        String dashboardToken = getAdminAccessTokenDashboardAuth();
        CarRentalAdminConfirmOrderApi.Inputs inputs = new CarRentalAdminConfirmOrderApi.Inputs(adminName,adminPhoneNumber,orderId,dashboardToken);
        CarRentalAdminConfirmOrderApi carRentalAdminConfirmOrderApi = new CarRentalAdminConfirmOrderApi(inputs);
        ApiResponse response = carRentalAdminConfirmOrderApi.hitApi(apiTools);
        JSONObject data = new JSONObject(response.getBody());
        return data.getString("code");
    }

    public String issuedOrderData(String data) {
        HashMap<String, String> issuedOrder = new HashMap<>();
        issuedOrder.put("orderId", referenceId);
        issuedOrder.put("carName", carName);
        issuedOrder.put("carRentalLocation", carRentalLocation);
        logger.log("got value :"+issuedOrder.get(data)+" for this key: "+data);
        return issuedOrder.get(data);
    }

    @SneakyThrows
    public void createIssuedOrderApiCar(
            String email,
            String phoneNumber,
            String password,
            String driverOption,
            String location,
            String startDate,
            String pickupTime,
            String type,
            String fullName,
            String nameTitle,
            String adminName,
            String adminPhoneNumber,
            int rentalDuration
    ) {
        try {
            String accessToken = "Bearer "+accountPage.getAccessTokenUNM(email, password);
            logger.log("accessToken-> " + accessToken);

            CarRentalAutoCompleteApi.Inputs carRentalAutoCompleteApiInputs = new CarRentalAutoCompleteApi.Inputs(accessToken,location, driverOption);
            regionalId = carRentalAutoComplete(carRentalAutoCompleteApiInputs).get(0);
            logger.log("regionalId -> " + regionalId);
            placeId = carRentalAutoComplete(carRentalAutoCompleteApiInputs).get(1);
            logger.log("placeId -> " + placeId);

            CarRentalSearchLocationApi.Inputs carRentalSearchLocationInputs = new CarRentalSearchLocationApi.Inputs(accessToken,placeId);
            latitude = carRentalSearchLocation(carRentalSearchLocationInputs).get(0);
            logger.log("latitude -> " + latitude);
            longitude = carRentalSearchLocation(carRentalSearchLocationInputs).get(1);
            logger.log("longitude -> " + longitude);

            CarRentalGetLocationIdApi.Inputs carRentalGetLocationIdInputs = new CarRentalGetLocationIdApi.Inputs(regionalId, latitude, longitude, driverOption, accessToken);
            locationId = carRentalGetLocationId(carRentalGetLocationIdInputs);
            logger.log("locationId -> " + locationId);

            StringBuilder pickup = new StringBuilder(pickupTime);
            pickup.setCharAt(2, '-');
            String pickupTimeUpdate = pickup.toString();

            SelectCarRentalApi.Inputs selectCarRentalApiInputs = new SelectCarRentalApi.Inputs(accessToken,driverOption, regionalId, startDate, pickupTimeUpdate, latitude, longitude);
            carRegionalId = selectCarRental(selectCarRentalApiInputs).get(0);
            logger.log("carRegionalId -> " + carRegionalId);
            carName = selectCarRental(selectCarRentalApiInputs).get(1);
            logger.log("carName -> " + carName);
            carRentalLocation = selectCarRental(selectCarRentalApiInputs).get(2);
            logger.log("carRentalLocation -> " + carRentalLocation);

            com.tiket.api.carrental.SubmitBookingApi.Inputs submitBookingApiInputs = new com.tiket.api.carrental.SubmitBookingApi.Inputs(accessToken, carRegionalId,
                                            email, phoneNumber, driverOption, location, locationId, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
            com.tiket.pojo.carrental.SubmitBookingResponse submitBookingResponse = submitBookingCar(submitBookingApiInputs);
            referenceId = submitBookingResponse.data.orderId;
            orderId = Integer.parseInt(referenceId);
            long orderIdLong = Long.parseLong(referenceId);
            logger.log("orderId-> " + referenceId);

            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, submitBookingResponse.data.orderHash, orderIdLong);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!submitSakukuResponse.message.equals("SUCCESS")) Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> "+grandTotal);
            logger.log("Reference Id -> "+submitSakukuResponse.data.sidebarPayment.referenceId);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");

            String completeOrderCode = confirmOrder(referenceId, adminName, adminPhoneNumber);
            logger.log("Is confirm order success: " + completeOrderCode);

            if (!(completeOrderCode.equals("SUCCESS") || completeOrderCode.equals("INVALID_STATE")))
                Assertion.skip("Car rental order confirmation via API is failed. Skipping the subsequent steps.");
        }catch(Exception e){
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Car Booking via API failed. Skipping the subsequent steps.");
        }
    }

    private SearchScheduleFerryResponse searchFerry(SearchScheduleFerryApi.Input input) throws InterruptedException, ExecutionException {
        SearchScheduleFerryApi searchScheduleFerryApi = new SearchScheduleFerryApi(input);
        ApiResponse searchScheduleFerryResponse = searchScheduleFerryApi.hitApi(apiTools);
        logger.log("DEBUG raw search result : " + searchScheduleFerryResponse.getBody());
        Gson gson = new Gson();
        SearchScheduleFerryResponse responsePojo = gson.fromJson(searchScheduleFerryResponse.getBody(),
                SearchScheduleFerryResponse.class);
        return responsePojo;
    }

    private SearchScheduleFerryResponse.Schedule filterSchedule(SearchScheduleFerryResponse.Data searchResultData) {
        return searchResultData.schedules.stream().filter(data -> data.availableQuota > 0).findFirst().orElseThrow();
    }

    private AddToCartFerryResponse addToCart(AddToCartFerryApi.Input input) throws InterruptedException, ExecutionException {
        AddToCartFerryApi addToCartFerryApi = new AddToCartFerryApi(input);
        ApiResponse addToCartFerryResponse = addToCartFerryApi.hitApi(apiTools);
        Gson gson = new Gson();
        AddToCartFerryResponse responsePojo = gson.fromJson(addToCartFerryResponse.getBody(),
                AddToCartFerryResponse.class);
        return responsePojo;
    }

    private SubmitBookingFerryResponse submitBooking(SubmitBookingFerryApi.Input bookingData) throws InterruptedException, ExecutionException {
        SubmitBookingFerryApi submitBookingFerryApi = new SubmitBookingFerryApi(bookingData);
        ApiResponse submitBookingFerryApiResponse = submitBookingFerryApi.hitApi(apiTools);
        logger.log("debug : " + submitBookingFerryApiResponse.getBody());
        Gson gson = new Gson();
        SubmitBookingFerryResponse responsePojo = gson.fromJson(submitBookingFerryApiResponse.getBody(),
                SubmitBookingFerryResponse.class);
        return responsePojo;
    }

    @SneakyThrows
    public void createIssuedOrderApiFerry(
            String email,
            String password,
            String origin,
            String destination,
            String departureDate,
            String departureTime,
            String vehicleType,
            int adult,
            int child,
            int infant,
            int elderly) {
        try {
            Unm<T> unm = new Unm<>(session);
            String token = unm.getAccessToken(email,password,"B2C");
            logger.log("accessToken-> " + accessToken);

            // search for ferry
            SearchScheduleFerryApi.Input searchQuery = new SearchScheduleFerryApi.Input(token, origin, destination,
                    departureDate, departureTime, vehicleType, adult, child, infant, elderly);
            SearchScheduleFerryResponse searchResponse = searchFerry(searchQuery);
            SearchScheduleFerryResponse.Schedule selectedSchedule = this.filterSchedule(searchResponse.data);
            String scheduleId = selectedSchedule.id;

            // add to cart ferry
            AddToCartFerryApi.Input cartInput = new AddToCartFerryApi.Input(token, scheduleId, adult, child, infant,
                    elderly);
            AddToCartFerryResponse addToCartResponse = addToCart(cartInput);
            Thread.sleep(2000);
            String cartId = addToCartResponse.data.cartId;
            String secretId = addToCartResponse.data.secret;
            boolean licensePlate = addToCartResponse.data.schedules.get(0).vehicleType.isLicensePlateRequired;

            // submit booking
            SubmitBookingFerryApi.Input submitBookingInput = new SubmitBookingFerryApi.Input(email, token, licensePlate, cartId, secretId, adult, child, infant);
            SubmitBookingFerryResponse responseSubmitBooking = submitBooking(submitBookingInput);
            orderId = Integer.parseInt((responseSubmitBooking.data.order.orderId));
            orderHash = (responseSubmitBooking.data.order.orderHash);

            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, orderId);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!submitSakukuResponse.message.equals("SUCCESS")) Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> "+grandTotal);
            logger.log("Reference Id -> "+submitSakukuResponse.data.sidebarPayment.referenceId);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");

        }catch(Exception e){
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Ferry via API failed. Skipping the subsequent steps.");
        }
    }

    private String getAuthCode(POSTAuthLoginUNMApi.Inputs inputs) throws ExecutionException, InterruptedException {
        POSTAuthLoginUNMApi postGenerateAuthCodeB2BApi = new POSTAuthLoginUNMApi(inputs);
        ApiResponse authCodeResponse = postGenerateAuthCodeB2BApi.hitApi(apiTools);
        JSONObject authCodeObject = new JSONObject(authCodeResponse.getBody());
        logger.log("auth code res -> " + authCodeObject);
        if(authCodeObject.getString("code").contains("MFA_REQUIRED")){
            logger.log("==MFA_REQUIRED==");
            return removeExtraFromUrlToGetAuthCode(removeMFA(authCodeObject.getJSONObject("data").getString("token")));
        }
        String redirectURL = authCodeObject.getJSONObject("data").getString("redirectUrl");
        return removeExtraFromUrlToGetAuthCode(redirectURL);
    }

     private String removeExtraFromUrlToGetAuthCode(String  url){
        String part1 = url.split("authCode=")[1];
        return part1.split("&ref=")[0];
    }

    private String getServiceTicket(GETAuthLoginUNMApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GETAuthLoginUNMApi getServiceTicketB2BApi = new GETAuthLoginUNMApi(inputs);
        ApiResponse serviceTicketResponse = getServiceTicketB2BApi.hitApi(apiTools);
        JSONObject serviceTicketObject = new JSONObject(serviceTicketResponse.getBody());
        logger.log("service ticket res -> " + serviceTicketObject);

        return serviceTicketObject.getJSONObject("data").getString("serviceTicket");
    }

    private String getAccessToken(POSTServiceTicketUNMApi.Inputs inputs,String username) throws ExecutionException, InterruptedException {
        POSTServiceTicketUNMApi postGenerateAccessTokenB2BApi = new POSTServiceTicketUNMApi(inputs);
        ApiResponse accessTokenResponse = postGenerateAccessTokenB2BApi.hitApi(apiTools);
        JSONObject accessTokenObject = new JSONObject(accessTokenResponse.getBody());
        logger.log("access token res -> " + accessTokenObject);
        if(accessTokenObject.getJSONObject("data").getBoolean("isB2BCorp")){
            return accessTokenObject.getJSONObject("data").getString("accessToken");
        }
        else {
            throw new NotImplementedException("the account use " + username + " is not b2b account");
        }
    }

    private String getChannelId(String type){
        return switch (type) {
            case "WA_OTP" -> "WHATS_APP";
            case "SMS_OTP" -> "SMS";
            case "EMAIL_OTP" -> "EMAIL";
            default -> throw new NotImplementedException("The type " + type + " is not implemented");
        };
    }

    @SneakyThrows
    private String removeMFA(String token) throws ExecutionException, InterruptedException {
        logger.log("==proceedings with mfa==");

        GETMfaMethodsUNMApi.Inputs getMfaMethodsInputs = new GETMfaMethodsUNMApi.Inputs(token);
        MfaMethodsResponse mfaMethod = getMfaMethods(getMfaMethodsInputs);
        logger.log("otpRecipientId : " + mfaMethod.data.otpRecipientId);
        logger.log("token : " + mfaMethod.data.token);
        String type = mfaMethod.data.methods.get(0).type;
        logger.log("type : " + type);
        String channel = getChannelId(type);

        POSTOtpGenerateUNMApi.Inputs postOtpGenerateUnmInputs = new POSTOtpGenerateUNMApi.Inputs(mfaMethod.data.otpRecipientId,"LOGIN_OTP",channel);

        String otpId = postOtpGenerateUnm(postOtpGenerateUnmInputs);
        logger.log("otpID : " + otpId);

        POSTOtpVerifyUNMApi.Inputs postOtpVerifyUNMApiInputs = new POSTOtpVerifyUNMApi.Inputs(otpId,"LOGIN_OTP");
        String passCode = postOtpVerifyUNMApi(postOtpVerifyUNMApiInputs);
        logger.log("passCode : " + passCode);

        POSTMfaSubmitUNMApi.Inputs postMfaSubmitUNMApiInputs = new POSTMfaSubmitUNMApi.Inputs(passCode,mfaMethod.data.token,type);
        String redirectUrl = postMfaSubmitUNMApi(postMfaSubmitUNMApiInputs);
        logger.log("redirect url : " + redirectUrl);
        return redirectUrl;
    }

    private MfaMethodsResponse getMfaMethods(GETMfaMethodsUNMApi.Inputs inputs)throws ExecutionException, InterruptedException{
        GETMfaMethodsUNMApi getMfaMethodsUNMApi = new GETMfaMethodsUNMApi(inputs);
        ApiResponse getMfaMethodsUNMApiResponse = getMfaMethodsUNMApi.hitApi(apiTools);
        JSONObject postOtpGenerateUNMApiObject = new JSONObject(getMfaMethodsUNMApiResponse.getBody());
        logger.log("code mfa methods res : -> " + postOtpGenerateUNMApiObject);

        Gson gson = new Gson();
        MfaMethodsResponse mfaMethodsResponse = gson.fromJson(getMfaMethodsUNMApiResponse.getBody(), MfaMethodsResponse.class);
        logger.log("code mfa methods : " + mfaMethodsResponse.code);
        return mfaMethodsResponse;
    }

    private String postOtpGenerateUnm(POSTOtpGenerateUNMApi.Inputs inputs)throws ExecutionException, InterruptedException{
        POSTOtpGenerateUNMApi postOtpGenerateUNMApi = new POSTOtpGenerateUNMApi(inputs);
        ApiResponse postOtpGenerateUNMApiResponse = postOtpGenerateUNMApi.hitApi(apiTools);
        JSONObject postOtpGenerateUNMApiObject = new JSONObject(postOtpGenerateUNMApiResponse.getBody());
        logger.log("code otp generate : -> " + postOtpGenerateUNMApiObject.getString("code"));
        return postOtpGenerateUNMApiObject.getJSONObject("data").getString("otpId");
    }

    private String postOtpVerifyUNMApi(POSTOtpVerifyUNMApi.Inputs inputs)throws ExecutionException, InterruptedException{
        POSTOtpVerifyUNMApi postOtpVerifyUNMApi = new POSTOtpVerifyUNMApi(inputs);
        ApiResponse postOtpVerifyUNMApiResponse = postOtpVerifyUNMApi.hitApi(apiTools);
        JSONObject postOtpVerifyUNMApiObject = new JSONObject(postOtpVerifyUNMApiResponse.getBody());
        logger.log("code verify otp methods : -> " + postOtpVerifyUNMApiObject.getString("code"));
        return postOtpVerifyUNMApiObject.getJSONObject("data").getString("passCode");
    }

    private String postMfaSubmitUNMApi(POSTMfaSubmitUNMApi.Inputs inputs)throws ExecutionException, InterruptedException{
        POSTMfaSubmitUNMApi postMfaSubmitUNMApi = new POSTMfaSubmitUNMApi(inputs);
        ApiResponse postMfaSubmitUNMApiResponse = postMfaSubmitUNMApi.hitApi(apiTools);
        JSONObject postMfaSubmitUNMApiObject = new JSONObject(postMfaSubmitUNMApiResponse.getBody());
        logger.log("code mfa submit : -> " + postMfaSubmitUNMApiObject.getString("code"));
        return postMfaSubmitUNMApiObject.getJSONObject("data").getString("redirectUrl");
    }


     @SneakyThrows
    public String getAccessTokenB2BCorporate(String username, String password) throws ExecutionException, InterruptedException {
        POSTAuthLoginUNMApi.Inputs authCodeInputs = new POSTAuthLoginUNMApi.Inputs(username,password);
        String authCode = getAuthCode(authCodeInputs);
        logger.log("auth code : " + authCode);

        GETAuthLoginUNMApi.Inputs getServiceTicketInputs = new GETAuthLoginUNMApi.Inputs(authCode);
        String serviceTicket = getServiceTicket(getServiceTicketInputs);
        logger.log("service ticket : " + serviceTicket);

        POSTServiceTicketUNMApi.Inputs accessTokenInputs = new POSTServiceTicketUNMApi.Inputs(serviceTicket);
        String accessToken = getAccessToken(accessTokenInputs,username);
        logger.log("access token : " + accessToken);
        return accessToken;
    }

    @SneakyThrows
    public Map<String,String> createWaitForPaymentApiB2B(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency
    ) {
        try {
            Map<String, String> result = new HashMap<>();
            String accessToken = "Bearer " + getAccessTokenB2BCorporate(username,password);
            logger.log("accessToken for dashboard request->" + accessToken);

            logger.log("query " + query);
            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            String hotelId=null;
            hotelName=null;
            for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
                if (query.equalsIgnoreCase(content.name)) {
                    hotelId = content.publicId;
                    hotelName = content.name;
                    break;
                }
            }
            logger.log("hotelId-> " + hotelId);
            logger.log("hotel name->" + hotelName);
            logger.log("Date: "+startDate);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"id", "");
            SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs);
            String roomName = selectRoomName(selectRoomApiInputs);
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName-> " + hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            String cartID = prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, "",false, "", "id", hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();
            orderId = submitBookingResponse.data.orderId;
            logger.log("orderId-> " + orderId);

            String order = String.valueOf(orderId);
            result.put("orderId", order);
            result.put("orderHash", orderHash);
            return result;
        }
        catch(Exception e){
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
        return null;
    }
    private PreBookMultiRoomResponse preBookMultiRoom(PreBookMultiRoomApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PreBookMultiRoomApi preBookMultiRoomApi = new PreBookMultiRoomApi(inputs);
        ApiResponse apiResponse = preBookMultiRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), PreBookMultiRoomResponse.class);
    }

    private SubmitBookingMultiRoomResponse submitBookingMultiRoom(SubmitBookingMultiRoomApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitBookingMultiRoomApi submitBookingMultiRoomApi = new SubmitBookingMultiRoomApi(inputs);
        ApiResponse apiResponse = submitBookingMultiRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), SubmitBookingMultiRoomResponse.class);
    }

    private SelectRoomResponse findRoom(SelectRoomApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
        ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
    }

    @SneakyThrows
    public String createOrderHotelMultiRoomApi(
            String username,
            String password,
            String hotelName,
            int adult,
            int night,
            int room,
            String startDate,
            int qtyEachRoom,
            String currency) {
        try {
            Unm<T> unm = new Unm<>(session);
            String authorization = unm.getAccessToken(username,password,"B2C");
            logger.log("accessToken-> " + accessToken);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, username, hotelName, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
            logger.log("hotelId-> " + hotelId);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(authorization, username, adult, hotelId, night, room, startDate, "id", currency);
            SelectRoomResponse selectRoomResponse = findRoom(selectRoomApiInputs);
            String[] roomIdArray = new String[room];
            String[] rateCodeArray = new String[room];
            int[] maxOccupancyArray = new int[room];
            int totalRoomList = selectRoomResponse.data.rooms.size();
            logger.log("totalRoomList -> " + totalRoomList);
            int roomIndex = 0;
            int roomTypeIndex = 0;
            for (int i = 0; i < room; i++) {
                roomIdArray[i] = selectRoomResponse.data.rooms.get(roomIndex).ratePlans.get(roomTypeIndex).roomId;
                rateCodeArray[i] = selectRoomResponse.data.rooms.get(roomIndex).ratePlans.get(roomTypeIndex).rateCode;
                maxOccupancyArray[i] = selectRoomResponse.data.rooms.get(roomIndex).ratePlans.get(roomTypeIndex).maxAdultOccupancy;
                if (roomTypeIndex == (selectRoomResponse.data.rooms.get(roomIndex).ratePlans.size() - 1) && (roomIndex + 1) < totalRoomList) {
                    roomIndex++;
                    roomTypeIndex = 0;
                } else if ((roomIndex + 1) == totalRoomList) {
                    roomTypeIndex = 0;
                } else {
                    roomTypeIndex++;
                }
            }
            logger.log("roomIdArray-> " + Arrays.toString(roomIdArray));
            logger.log("rateCodeArray-> " + Arrays.toString(rateCodeArray));
            logger.log("maxOccupancyArray-> " + Arrays.toString(maxOccupancyArray));

            PreBookMultiRoomApi.Inputs preBookMultiRoomApiInputs = new PreBookMultiRoomApi.Inputs(authorization, adult, hotelId, night, startDate, maxOccupancyArray, qtyEachRoom, rateCodeArray, roomIdArray);
            PreBookMultiRoomResponse preBookMultiRoomResponse = preBookMultiRoom(preBookMultiRoomApiInputs);
            Gson gson = new Gson();

            // Converting POJO to JSON string
            String jsonString = gson.toJson(preBookMultiRoomResponse);
            logger.log("preBookMultiRoomResponse-> " + jsonString);
            List<String> cardIdArray = new ArrayList<>();
            for (int i = 0; i < room; i++) {
                cardIdArray.add(preBookMultiRoomResponse.data.hotels.get(0).detail.rooms.get(i).cartId);
            }
            logger.log("cardIdArray-> " + cardIdArray);

            SubmitBookingMultiRoomApi.Inputs submitBookingMultiRoomApiInputs = new SubmitBookingMultiRoomApi.Inputs(authorization, currency, username, hotelId, room, qtyEachRoom, cardIdArray, false, "");
            SubmitBookingMultiRoomResponse submitBookingMultiRoomResponse = submitBookingMultiRoom(submitBookingMultiRoomApiInputs);
            orderId = submitBookingMultiRoomResponse.data.orderId;
            logger.log("orderId-> " + orderId);
            orderHash = submitBookingMultiRoomResponse.data.orderHash;
            logger.log("orderHash-> " + orderHash);

            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, orderId);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!submitSakukuResponse.message.equals("SUCCESS"))
                Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> " + grandTotal);
            logger.log("Reference Id -> " + submitSakukuResponse.data.sidebarPayment.referenceId);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS"))
                Assertion.skip("Order not paid. Skipping the subsequent steps.");

        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("MultiOrder Hotel via API failed. Skipping the subsequent steps.");
        }
        return String.valueOf(orderId);
    }

    private HashMap<String,String> getAirlineCodeToNameMap(HashMap<String,FlightSearchResponse.AirlineDetail> airlineDetail){
        HashMap<String,String> mp = new HashMap<>();
        for (String key : airlineDetail.keySet()){
            String displayName = airlineDetail.get(key).displayName;
            mp.put(key,displayName);
        }
        return mp;
    }

    private String selectOriginAndDestinationType(String loc){
        return switch (loc.length()) {
            case 3 -> "AIRPORT";
            case 4 -> "CITY";
            default -> throw new NotImplementedException("Unimplemented location type!");
        };
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

    private FlightBookingDetailDomestic flightBookingApiDomestic(FlightBookingApiDomestic.Inputs inputs) throws ExecutionException, InterruptedException {
        FlightBookingApiDomestic flightBookingApi = new FlightBookingApiDomestic(inputs);
        ApiResponse flightBookingResponse = flightBookingApi.hitApi(apiTools);
        Gson gson = new Gson();
        FlightBookingDetailDomestic flightBookingDetailDomestic = gson.fromJson(flightBookingResponse.getBody(), FlightBookingDetailDomestic.class);
        logger.log("Flight Booking API response->" + flightBookingResponse.getBody());

        return flightBookingDetailDomestic;
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

    private boolean matchAirline(String airlineNameActual,String airlineNameExpected){
        String[] airlineExpected = airlineNameExpected.split(" ");
        String[] airlineActual = airlineNameActual.split(" ");
        logger.log("expected airline name -> " + airlineNameExpected.toLowerCase() + " actual airline name ->" + airlineNameActual.toLowerCase());
        boolean flag = airlineExpected.length == airlineActual.length;

        if (flag) {
            for (int i = 0; i < airlineExpected.length; i++) {
                flag = flag & airlineActual[i].equalsIgnoreCase(airlineExpected[i]);
            }
        }
        return flag;
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
            String phoneNumber) {
        try {
            Unm<T> unm = new Unm<>(session);
            String accessToken = unm.getAccessToken(username,password,"B2C");
            logger.log("accessToken-> " + accessToken);
            String deviceId = CoreFunctions.generateDeviceId();
            //      flight search api
            FlightSearchApi.Inputs flightSearchApiInputs = new FlightSearchApi.Inputs(
                    originStation.toUpperCase(), destinationStation.toUpperCase(),selectOriginAndDestinationType(originStation) , selectOriginAndDestinationType(destinationStation), departureDate, adult, child, infant,
                    searchType, accessToken,"IDR",deviceId);

            FlightSearchResponse.Flight departureFlight = flightSearchByName(flightSearchApiInputs,airline);

            logger.log("Flight Search API response->" + (departureFlight != null));
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
            long orderIdLong = flightBookingDetailDomestic.data.successData.orderId;
            orderId = Math.toIntExact(orderIdLong);
            orderHash = flightBookingDetailDomestic.data.successData.orderHash;

            logger.log("orderId->" + orderId);
            logger.log("orderHash->" + orderHash);

            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, orderId);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!submitSakukuResponse.message.equals("SUCCESS"))
                Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> " + grandTotal);
            logger.log("Reference Id -> " + submitSakukuResponse.data.sidebarPayment.referenceId);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS"))
                Assertion.skip("Order not paid. Skipping the subsequent steps.");

            //get booking code
            String bookingCode = getBookingCode(String.valueOf(orderId),username);

            //trigger issued flight
            flightEticketIssuedRequest(String.valueOf(orderId), bookingCode, username, passengerNames);

        }catch (Exception e){
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Flight via API failed. Skipping the subsequent steps.");
        }
        return String.valueOf(orderId);
    }

    @SneakyThrows
    private Boolean flightEticketIssuedRequest(String orderId, String bookingCode, String email, String[] passengerNames){
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

    @SneakyThrows
    private String getBookingCode(String orderID, String email) throws InterruptedException {
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

    private String getToken() throws ExecutionException, InterruptedException {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer " + accessToken;
    }

    public void createOrderByHotelIdAndGetItineraryID(String username, String startDate, String hotelId, int adult, int night, int room, String guestFullName, String guestTitle, String currency){
        try {
            List<Integer> hotelIdList = new ArrayList<>();
            hotelIdList.add(Integer.valueOf(hotelId));
            List<Integer> childAgesList = new ArrayList<>();

            HotelAvailabilityApi.Inputs hotelAvailabilityApiInputs = new HotelAvailabilityApi.Inputs(adult, childAgesList, hotelIdList, night, room, startDate, currency);
            ApiResponse hotelAvailabilityApiResponse = new HotelAvailabilityApi(hotelAvailabilityApiInputs).hitApi(apiTools);
            logger.log("Hotel Avail Search Response "+ hotelAvailabilityApiResponse.getBody());

            String responseBody = hotelAvailabilityApiResponse.getBody();
            JSONObject jsonResponse = new JSONObject(responseBody);
            String apiCode = jsonResponse.getString("code");
            Assertion.assertThat("Verify Availability API response code is SUCCESS", apiCode, equalTo("SUCCESS"));
            Assertion.assertThat("Verify Availability API message is SUCCESS",jsonResponse.getString("message") , equalTo("SUCCESS"));
            JSONObject dataObject = jsonResponse.getJSONObject("data");
            JSONArray hotelsArray = dataObject.getJSONArray("hotels");
            JSONObject firstHotel = hotelsArray.getJSONObject(0);
            int responseHotelId = firstHotel.getInt("hotelId");
            Assertion.assertThat("Verify Availability response hotel ID matches Expected = "+hotelId+ " Actual = "+responseHotelId, responseHotelId, equalTo(Integer.parseInt(hotelId)));

            JSONArray productsArray = firstHotel.getJSONArray("products");
            JSONObject firstProduct = productsArray.getJSONObject(0);

            int availableRoom = firstProduct.getInt("availableRoom");
            Assertion.assertThat("Verify rooms are available = "+availableRoom, availableRoom, greaterThan(0));

            int roomId = firstProduct.getInt("roomId");
            int ratePlanId = firstProduct.getInt("ratePlanId");
            String ratePlanCode = firstProduct.getString("ratePlanCode");

            JSONArray ratesArray = firstProduct.getJSONArray("rates");
            String capturedStartDate = ratesArray.getJSONObject(0).getString("date");
            Assertion.assertThat("Verify Availability check-in date matches input parameter Expected Checkin data = "+startDate+" Actual CheckIn Date = "+capturedStartDate, capturedStartDate, equalTo(startDate));

            logger.log("Successfully Verified and Captured API Data:");
            logger.log("Hotel ID: " + responseHotelId);
            logger.log("Room ID: " + roomId);
            logger.log("Rate Plan ID: " + ratePlanId);
            logger.log("Rate Plan Code: " + ratePlanCode);
            logger.log("Available Room: " + availableRoom);
            logger.log("Start Date: " + capturedStartDate);

            logger.log("Executing Hotel Prebook API...");
            HotelPrebookApi.Inputs prebookInputs = new HotelPrebookApi.Inputs(adult, childAgesList, responseHotelId, night, ratePlanCode, ratePlanId, room, roomId, capturedStartDate, currency);
            ApiResponse prebookResponse = new HotelPrebookApi(prebookInputs).hitApi(apiTools);
            logger.log("Hotel Prebook Response: " + prebookResponse.getBody());
            JSONObject prebookJson = new JSONObject(prebookResponse.getBody());
            Assertion.assertThat("Verify Prebook API response code is SUCCESS", prebookJson.getString("code"), equalTo("SUCCESS"));
            Assertion.assertThat("Verify Prebook API message is SUCCESS",prebookJson.getString("message") , equalTo("SUCCESS"));
            JSONObject prebookData = prebookJson.getJSONObject("data");
            JSONObject prebookHotel = prebookData.getJSONArray("hotels").getJSONObject(0);
            JSONObject prebookProduct = prebookHotel.getJSONArray("products").getJSONObject(0);
            Assertion.assertThat("Verify hotel ID in Prebook matches Availability Expected = "+responseHotelId+ " Actual ="+prebookHotel.getInt("hotelId"), prebookHotel.getInt("hotelId"), equalTo(responseHotelId));
            Assertion.assertThat("Verify room ID in Prebook matches Availability Expected = "+roomId+ " Actual ="+prebookProduct.getInt("roomId"), prebookProduct.getInt("roomId"), equalTo(roomId));
            Assertion.assertThat("Verify rate plan ID in Prebook matches Availability expected = "+ratePlanId+" Actual = "+prebookProduct.getInt("ratePlanId"), prebookProduct.getInt("ratePlanId"), equalTo(ratePlanId));
            Assertion.assertThat("Verify rate plan code in Prebook matches Availability expected = " + ratePlanCode + " Actual = " + prebookProduct.getString("ratePlanCode"), prebookProduct.getString("ratePlanCode"), equalTo(ratePlanCode));

            int prebookAvailableRoom = prebookProduct.getInt("availableRoom");
            Assertion.assertThat("Verify room count in Prebook matches Availability count exactly expected = "+availableRoom+" Actual = "+prebookAvailableRoom, prebookAvailableRoom, equalTo(availableRoom));
            String prebookStartDate = prebookProduct.getJSONArray("rates").getJSONObject(0).getString("date");
            Assertion.assertThat("Verify Prebook check-in date matches input parameter expected = "+startDate+" Actual = "+prebookStartDate, prebookStartDate, equalTo(startDate));

            logger.log("Executing Hotel Book API...");
            String[] nameParts = guestFullName.split(" ", 2);
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : ".";
            String bookingEmail = username;
            String bookingPhone = "123456789";
            long totalPrice = 125000L;
            HotelBookApi.Inputs bookInputs = new HotelBookApi.Inputs(responseHotelId, roomId, ratePlanId, capturedStartDate, night, room, adult, childAgesList, currency, bookingEmail, firstName, lastName, guestTitle, bookingPhone, totalPrice);
            ApiResponse bookResponse = new HotelBookApi(bookInputs).hitApi(apiTools);
            logger.log("Hotel Book Response: " + bookResponse.getBody());
            JSONObject bookJson = new JSONObject(bookResponse.getBody());
            Assertion.assertThat("Verify Book API response code is SUCCESS", bookJson.getString("code"), equalTo("SUCCESS"));
            Assertion.assertThat("Verify Book API message is SUCCESS", bookJson.getString("message"), equalTo("SUCCESS"));
            JSONObject bookData = bookJson.getJSONObject("data");
            Object itineraryIdObj = bookData.get("itineraryId");
            Assertion.assertThat("Verify booking registration itinerary ID exists and is not null", itineraryIdObj, notNullValue());
            int itineraryIdInt = bookData.getInt("itineraryId");
            String bookingToken = bookData.getString("bookingToken");
            String itineraryId = String.valueOf(itineraryIdInt);
            Assertion.assertThat("Verify booking registration itinerary ID is valid (greater than 0) Itinerary Id = "+itineraryId, itineraryIdInt, greaterThan(0));
            Assertion.assertThat("Verify hotel ID in Book response matches Expected = "+responseHotelId+ " Actual = "+bookData.getInt("hotelId"), bookData.getInt("hotelId"), equalTo(responseHotelId));
            Assertion.assertThat("Verify booking token is not null, Booking Token "+bookingToken, bookingToken, notNullValue());

            String bookStartDate = bookData.getString("startDate");
            logger.log("Extracted Start Date from Book API: " + bookStartDate);
            Assertion.assertThat("Verify Book check-in date matches input parameter, Expected = "+startDate+" Actual = "+bookStartDate, bookStartDate, equalTo(startDate));
            JSONObject roomData = bookData.getJSONObject("roomData");
            Assertion.assertThat("Verify room ID in Book response matches, expected = "+roomId+ " Actual = "+roomData.getInt("roomId"), roomData.getInt("roomId"), equalTo(roomId));
            Assertion.assertThat("Verify rate plan ID in Book response matches expected = "+ratePlanId+ " Actual = "+roomData.getInt("ratePlanId"), roomData.getInt("ratePlanId"), equalTo(ratePlanId));
            Assertion.assertThat("Verify rate plan code in Book response matches expected = "+ratePlanCode+ " Actual = "+roomData.getString("ratePlanCode"), roomData.getString("ratePlanCode"), equalTo(ratePlanCode));
            logger.log("Captured Booking Information -> Itinerary ID: " + itineraryId + " | Token: " + bookingToken);

            logger.log("Executing Hotel Book Confirm API...");
            HotelBookConfirmApi.Inputs confirmInputs = new HotelBookConfirmApi.Inputs(itineraryId, bookingToken);
            ApiResponse confirmResponse = new HotelBookConfirmApi(confirmInputs).hitApi(apiTools);

            logger.log("Hotel Book Confirm Response: " + confirmResponse.getBody());
            JSONObject confirmJson = new JSONObject(confirmResponse.getBody());
            JSONObject confirmData = confirmJson.getJSONObject("data");
            Assertion.assertThat("Verify Book Confirm API response code is SUCCESS", confirmJson.getString("code"), equalTo("SUCCESS"));
            Assertion.assertThat("Verify Book Confirm API response message is SUCCESS", confirmJson.getString("message"), equalTo("SUCCESS"));
            Assertion.assertThat("Verify Book Confirm API errors object is null", confirmJson.isNull("errors"), equalTo(true));
            Assertion.assertThat("Verify Book Confirm itinerary ID matches, expected = "+itineraryId+ " Actual = "+confirmData.getInt("itineraryId"), confirmData.getInt("itineraryId"), equalTo(Integer.parseInt(itineraryId)));
            Assertion.assertThat("Verify supplier is not null", confirmData.get("supplier"), notNullValue());
            Assertion.assertThat("Verify additionalInfo object is not null", confirmData.getJSONObject("additionalInfo"), notNullValue());
            logger.log("Hotel Order fully created and confirmed successfully! Itinerary ID: " + itineraryId);
            long serverTimeMilli = confirmJson.getLong("serverTime");
            String formattedUtcTime = CoreFunctions.convertServerTimeToUTCFormat(serverTimeMilli);
            logger.log("Captured Confirm API Server Time (UTC): " + formattedUtcTime);
            itineraryID = itineraryIdInt;
            BookingToken = bookingToken;
            checkInDate = bookStartDate;
            UtcTime = formattedUtcTime;
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
    }



}
