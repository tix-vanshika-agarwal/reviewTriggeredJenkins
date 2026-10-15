package com.tiket.page.carrental;

import com.google.gson.Gson;
import com.tiket.api.auth.AdminDashboardLoginResponseParser;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardApi;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardDynamicApi;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.carrental.*;
import com.tiket.api.payment.SubmitPaymentApi;
import com.tiket.api.payment.SubmitSakukuApi;
import com.tiket.apiPage.SmartProfileApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.carrental.*;
import com.tiket.pojo.member.SmartProfileResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.pojo.payment.SubmitSakukuResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;


public abstract class CarRentalPage<T extends WebDriver> extends BasePage<T> {

    public CarRentalPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CarRentalPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {return new CarRentalPageIOS<>(session);}
            case ANDROID -> {return new CarRentalPageAndroid<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
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

    @SneakyThrows
    public String confirmOrder(String orderId,String adminName,String adminPhoneNumber){
        String dashboardToken = getAdminAccessTokenDashboardAuth();
        CarRentalAdminConfirmOrderApi.Inputs inputs = new CarRentalAdminConfirmOrderApi.Inputs(adminName,adminPhoneNumber,orderId,dashboardToken);
        CarRentalAdminConfirmOrderApi carRentalAdminConfirmOrderApi = new CarRentalAdminConfirmOrderApi(inputs);
        ApiResponse response = carRentalAdminConfirmOrderApi.hitApi(apiTools);
        JSONObject data = new JSONObject(response.getBody());
        return data.getString("code");
    }

    String regionalId;
    String placeId;
    String latitude;
    String longitude;
    String locationId;
    String carRegionalId;
    String carName;
    String carRentalLocation;
    String orderId;
    String orderHash;

    public String getLastCreatedOrderId() {
        return orderId;
    }
    String carType;

    @SneakyThrows
    public Boolean createIssuedOrderApi(
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
            String adminPhoneNumber
    ) {
        try {
            Unm<T> unm = new Unm<>(session);
            String accessToken = unm.getAccessToken(email, password,"B2C");
            logger.log("access token: " + accessToken);

            String loginAccessToken = unm.getAccessToken(email,password,"B2C");
            logger.log("accessToken for dashboard request->" + loginAccessToken);

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

            CarRentalGetLocationIdApi.Inputs carRentalGetLocationIdInputs = new CarRentalGetLocationIdApi.Inputs(regionalId, latitude, longitude, driverOption,accessToken);
            locationId = carRentalGetLocationId(carRentalGetLocationIdInputs);
            logger.log("locationId -> " + locationId);

            StringBuilder pickup = new StringBuilder(pickupTime);
            pickup.setCharAt(2, '-');
            String pickupTimeUpdate = pickup.toString();

            SelectCarRentalApi.Inputs selectCarRentalApiInputs = new SelectCarRentalApi.Inputs(accessToken,driverOption, regionalId, startDate, pickupTimeUpdate, latitude, longitude);
            CarRentalData carRentalData = selectCarRental(selectCarRentalApiInputs);
            logger.log("carRegionalId -> " + carRentalData.getCarRegionalId());
            logger.log("carName -> " + carRentalData.getCarName());
            logger.log("carRentalLocation -> " + carRentalData.getCarRentalLocation());
            logger.log("carType -> " + carRentalData.getCarType());

            SubmitBookingApi.Inputs submitBookingApiInputs = new SubmitBookingApi.Inputs(accessToken, carRegionalId, email, phoneNumber, driverOption, location, locationId, startDate, pickupTime, type, fullName, nameTitle,1);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
            orderId = submitBookingResponse.data.orderId;
            orderHash = submitBookingResponse.data.orderHash;
            logger.log("orderId-> " + orderId);

            orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, Long.parseLong(orderId));
            SubmitSakukuResponse submitSakukuResponse = orderCreationPage.submitSakuku(submitSakukuApiInputs);
            if (!submitSakukuResponse.message.equals("SUCCESS")) Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            BigInteger grandTotal =submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> " + grandTotal);

            orderCreationPage.paidCheckerStatus(120,orderId);

            String completeOrderCode = orderCreationPage.confirmOrder(orderId, adminName, adminPhoneNumber);
            logger.log("Is confirm order success: " + completeOrderCode);

            return completeOrderCode.equalsIgnoreCase("SUCCESS");
        }catch(Exception e){
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Car Booking via API failed. Skipping the subsequent steps.");
        }
        return false;
    }

    @SneakyThrows
    public Boolean createIssuedOrderApiV4(
            String email,
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
            String dashboardUsername,
            String dashboardPassword
    ) {
        try {
            Unm<T> unm = new Unm<>(session);
            SmartProfileApi<T> smartProfileApi = new SmartProfileApi<>(session);
            LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
            String accessToken = authData.accessToken;
            logger.log("access token: " + accessToken);

            SmartProfileResponse.Data smartProfileData = smartProfileApi.getSmartProfileData(accessToken, authData.accountFirstName);
            int profileId = smartProfileData.id;
            String profilePhone = smartProfileData.accountPhone;
            String contactTitle = smartProfileData.accountSalutationName != null ? smartProfileData.accountSalutationName : nameTitle;

            CarRentalAutoCompleteApi.Inputs carRentalAutoCompleteApiInputs = new CarRentalAutoCompleteApi.Inputs(accessToken, location, driverOption);
            List<String> autoCompleteData = carRentalAutoComplete(carRentalAutoCompleteApiInputs);
            regionalId = autoCompleteData.get(0);
            logger.log("regionalId -> " + regionalId);
            placeId = autoCompleteData.get(1);
            logger.log("placeId -> " + placeId);

            CarRentalSearchLocationApi.Inputs carRentalSearchLocationInputs = new CarRentalSearchLocationApi.Inputs(accessToken, placeId);
            List<String> searchLocationData = carRentalSearchLocation(carRentalSearchLocationInputs);
            latitude = searchLocationData.get(0);
            logger.log("latitude -> " + latitude);
            longitude = searchLocationData.get(1);
            logger.log("longitude -> " + longitude);

            CarRentalGetLocationIdApi.Inputs carRentalGetLocationIdInputs = new CarRentalGetLocationIdApi.Inputs(regionalId, latitude, longitude, driverOption, accessToken);
            locationId = carRentalGetLocationId(carRentalGetLocationIdInputs);
            logger.log("locationId -> " + locationId);

            StringBuilder pickup = new StringBuilder(pickupTime);
            pickup.setCharAt(2, '-');
            String pickupTimeUpdate = pickup.toString();

            SelectCarRentalApi.Inputs selectCarRentalApiInputs = new SelectCarRentalApi.Inputs(accessToken, driverOption, regionalId, startDate, pickupTimeUpdate, latitude, longitude);
            SelectCarRentalResponse selectCarRentalResponse = hitSelectCarRentalApi(selectCarRentalApiInputs);
            SelectCarRentalResponse.Fleets selectedFleet = selectCarRentalResponse.data.fleets.get(0);
            carRegionalId = selectedFleet.carRegionalId;
            int totalPrice = (int) Math.round(Double.parseDouble(selectedFleet.finalPrice));
            logger.log("carRegionalId -> " + carRegionalId);
            logger.log("totalPrice -> " + totalPrice);

            SubmitBookingV4Api.Inputs submitBookingV4ApiInputs = new SubmitBookingV4Api.Inputs(
                    accessToken, carRegionalId, email, profilePhone, profileId, driverOption, location, locationId,
                    latitude, longitude, placeId, startDate, pickupTime, type, fullName, contactTitle, 1, totalPrice);
            SubmitBookingResponse submitBookingResponse = submitBookingV4(submitBookingV4ApiInputs);
            orderId = submitBookingResponse.data.orderId;
            orderHash = submitBookingResponse.data.orderHash;
            logger.log("orderId-> " + orderId);

            orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, Long.parseLong(orderId));
            SubmitSakukuResponse submitSakukuResponse = orderCreationPage.submitSakuku(submitSakukuApiInputs);
            if (!submitSakukuResponse.message.equals("SUCCESS")) Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            BigInteger grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> " + grandTotal);

            orderCreationPage.paidCheckerStatus(120, orderId);

            // Use member-core REST login
            POSTGetAdminAccessTokenDashboardDynamicApi.Inputs adminLoginInputs =
                    new POSTGetAdminAccessTokenDashboardDynamicApi.Inputs(dashboardUsername, dashboardPassword);
            ApiResponse adminLoginResponse = new POSTGetAdminAccessTokenDashboardDynamicApi(adminLoginInputs).hitApi(apiTools);
            String dashboardToken = AdminDashboardLoginResponseParser.resolveSessionTokenOrNull(adminLoginResponse);
            if (dashboardToken == null || dashboardToken.isBlank()) {
                Assertion.skip("Dashboard admin login failed: session token is missing. Skipping order confirmation.");
            }
            CarRentalAdminConfirmOrderApi.Inputs confirmInputs =
                    new CarRentalAdminConfirmOrderApi.Inputs(adminName, adminPhoneNumber, orderId, dashboardToken);
            ApiResponse confirmResponse = new CarRentalAdminConfirmOrderApi(confirmInputs).hitApi(apiTools);
            String completeOrderCode = new JSONObject(confirmResponse.getBody()).getString("code");
            logger.log("Is confirm order success: " + completeOrderCode);

            return completeOrderCode.equalsIgnoreCase("SUCCESS");
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Car Booking via API failed. Skipping the subsequent steps.");
        }
        return false;
    }

    private JSONObject submitPayment(SubmitPaymentApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitPaymentApi submitPaymentApi = new SubmitPaymentApi(inputs);
        ApiResponse apiResponse = submitPaymentApi.hitApi(apiTools);

        JSONArray responseArray = new JSONArray(apiResponse.getBody());
        return responseArray.getJSONObject(0)
                .getJSONObject("data")
                .getJSONObject("paymentSubmit")
                .getJSONObject("data");
    }


    @SneakyThrows
    public Boolean createProductTillPaymentApi(
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
            int rentalDuration
    ) {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(email, password,"B2C");
        logger.log("access token: " + accessToken);

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

        CarRentalGetLocationIdApi.Inputs carRentalGetLocationIdInputs = new CarRentalGetLocationIdApi.Inputs(regionalId, latitude, longitude, driverOption,accessToken);
        locationId = carRentalGetLocationId(carRentalGetLocationIdInputs);
        logger.log("locationId -> " + locationId);

        StringBuilder pickup = new StringBuilder(pickupTime);
        pickup.setCharAt(2, '-');
        String pickupTimeUpdate = pickup.toString();

        SelectCarRentalApi.Inputs selectCarRentalApiInputs = new SelectCarRentalApi.Inputs(accessToken,driverOption, regionalId, startDate, pickupTimeUpdate, latitude, longitude);
        CarRentalData carRentalData = selectCarRental(selectCarRentalApiInputs);
        logger.log("carRegionalId -> " + carRentalData.getCarRegionalId());
        logger.log("carName -> " + carRentalData.getCarName());
        logger.log("carRentalLocation -> " + carRentalData.getCarRentalLocation());
        logger.log("carType -> " + carRentalData.getCarType());

        SubmitBookingApi.Inputs submitBookingApiInputs = new SubmitBookingApi.Inputs(accessToken, carRegionalId, email, phoneNumber, driverOption, location, locationId, startDate, pickupTime, carType, fullName, nameTitle,rentalDuration);
        SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
        orderId = submitBookingResponse.data.orderId;
        logger.log("orderId-> " + orderId);

        return orderId!=null;
    }

    @SneakyThrows
    public Boolean completeCarrentalOrderApi(String issuedOrderID, String adminName, String adminPhoneNumber) {
        String completeOrderCode = confirmOrder(issuedOrderID, adminName, adminPhoneNumber);
        logger.log("Is complete success: " + completeOrderCode);

        return completeOrderCode.equals("SUCCESS") || completeOrderCode.equals("INVALID_STATE");
    }


    @SneakyThrows
    public Boolean createOrderGuestTillPayment(
            String email,
            String phoneNumber,
            String driverOption,
            String location,
            String startDate,
            String pickupTime,
            String type,
            String fullName,
            String nameTitle
    ) {
        String accessToken = getToken();
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

        CarRentalGetLocationIdApi.Inputs carRentalGetLocationIdInputs = new CarRentalGetLocationIdApi.Inputs(regionalId, latitude, longitude, driverOption,accessToken);
        locationId = carRentalGetLocationId(carRentalGetLocationIdInputs);
        logger.log("locationId -> " + locationId);

        StringBuilder pickup = new StringBuilder(pickupTime);
        pickup.setCharAt(2, '-');
        String pickupTimeUpdate = pickup.toString();

        SelectCarRentalApi.Inputs selectCarRentalApiInputs = new SelectCarRentalApi.Inputs(accessToken,driverOption, regionalId, startDate, pickupTimeUpdate, latitude, longitude);
        CarRentalData carRentalData = selectCarRental(selectCarRentalApiInputs);
        logger.log("carRegionalId -> " + carRentalData.getCarRegionalId());
        logger.log("carName -> " + carRentalData.getCarName());
        logger.log("carRentalLocation -> " + carRentalData.getCarRentalLocation());
        logger.log("carType -> " + carRentalData.getCarType());

        SubmitBookingApi.Inputs submitBookingApiInputs = new SubmitBookingApi.Inputs(accessToken, carRegionalId, email, phoneNumber, driverOption, location, locationId, startDate, pickupTime, type, fullName, nameTitle,1);
        SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
        orderId = submitBookingResponse.data.orderId;
        logger.log("orderId-> " + orderId);

        return orderId!=null;
    }

    private String getToken() throws ExecutionException, InterruptedException {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
    }

    private List<String> carRentalAutoComplete(CarRentalAutoCompleteApi.Inputs inputs) throws ExecutionException, InterruptedException {
        CarRentalAutoCompleteApi carRentalAutoCompleteApi = new CarRentalAutoCompleteApi(inputs);
        ApiResponse carRentalAutoCompleteResponse = carRentalAutoCompleteApi.hitApi(apiTools);
        Gson gson = new Gson();
        CarRentalAutoCompleteResponse response = gson.fromJson(carRentalAutoCompleteResponse.getBody(), CarRentalAutoCompleteResponse.class);
        if (response.data == null || response.data.isEmpty()) {
            throw new IllegalStateException("Car rental auto-complete returned no results");
        }
        regionalId = response.data.get(0).regionalId;
        placeId = response.data.get(0).placeId;
        return Arrays.asList(regionalId, placeId);
    }

    private List<String> carRentalSearchLocation(CarRentalSearchLocationApi.Inputs inputs) throws ExecutionException, InterruptedException {
        CarRentalSearchLocationApi carRentalSearchLocationApi = new CarRentalSearchLocationApi(inputs);
        ApiResponse carRentalSearchLocationResponse = carRentalSearchLocationApi.hitApi(apiTools);
        Gson gson = new Gson();
        CarRentalSearchLocationResponse response = gson.fromJson(carRentalSearchLocationResponse.getBody(), CarRentalSearchLocationResponse.class);
        if (response.data == null) {
            throw new IllegalStateException("Car rental search location returned no data");
        }
        latitude = response.data.latitude;
        longitude = response.data.longitude;
        return Arrays.asList(latitude, longitude);
    }

    private String carRentalGetLocationId(CarRentalGetLocationIdApi.Inputs inputs) throws ExecutionException, InterruptedException {
        CarRentalGetLocationIdApi carRentalGetLocationIdApi = new CarRentalGetLocationIdApi(inputs);
        ApiResponse carRentalGetLocationIdResponse = carRentalGetLocationIdApi.hitApi(apiTools);
        Gson gson = new Gson();
        logger.log("DEBUG : " + carRentalGetLocationIdResponse.getBody());
        CarRentalGetLocationIdResponse response = gson.fromJson(carRentalGetLocationIdResponse.getBody(), CarRentalGetLocationIdResponse.class);
        locationId = response.data.zone.id;
        return locationId;
    }

    private CarRentalData selectCarRental(SelectCarRentalApi.Inputs inputs)
            throws ExecutionException, InterruptedException {

        SelectCarRentalApi selectCarRentalApi = new SelectCarRentalApi(inputs);
        ApiResponse selectCarRentalResponse = selectCarRentalApi.hitApi(apiTools);
        logger.log("response car rental : " + selectCarRentalResponse.getBody());

        Gson gson = new Gson();
        SelectCarRentalResponse response = gson.fromJson(selectCarRentalResponse.getBody(), SelectCarRentalResponse.class);

        // Extract from response according to actual structure
        String carRegionalId = response.data.searchQuery.regionalId;
        String startDate = response.data.searchQuery.startDate;
        String driverOption = response.data.searchQuery.driverOption;
        String currency = response.data.searchQuery.currency;

        // Update instance variables
        this.carRegionalId = carRegionalId;
        this.carName = driverOption; // no car name in response, placeholder
        this.carRentalLocation = carRegionalId; // can replace with actual region if added later
        this.carType = currency; // placeholder

        return new CarRentalData(carRegionalId, driverOption, carRegionalId, currency);
    }

    private SelectCarRentalResponse hitSelectCarRentalApi(SelectCarRentalApi.Inputs inputs) {
        SelectCarRentalApi selectCarRentalApi = new SelectCarRentalApi(inputs);
        ApiResponse selectCarRentalResponse = selectCarRentalApi.hitApi(apiTools);
        logger.log("response car rental : " + selectCarRentalResponse.getBody());
        Gson gson = new Gson();
        return gson.fromJson(selectCarRentalResponse.getBody(), SelectCarRentalResponse.class);
    }

    private SubmitBookingResponse submitBooking(SubmitBookingApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitBookingApi submitBookingApi = new SubmitBookingApi(inputs);
        ApiResponse submitBookingApiResponse = submitBookingApi.hitApi(apiTools);
        logger.log("Api response : \n" + submitBookingApiResponse.getBody());
        Gson gson = new Gson();
        return gson.fromJson(submitBookingApiResponse.getBody(), SubmitBookingResponse.class);
    }

    private SubmitBookingResponse submitBookingV4(SubmitBookingV4Api.Inputs inputs) {
        SubmitBookingV4Api submitBookingV4Api = new SubmitBookingV4Api(inputs);
        ApiResponse submitBookingApiResponse = submitBookingV4Api.hitApi(apiTools);
        logger.log("Api response : \n" + submitBookingApiResponse.getBody());
        Gson gson = new Gson();
        return gson.fromJson(submitBookingApiResponse.getBody(), SubmitBookingResponse.class);
    }

    public String issuedOrderData(String data) {
        HashMap<String, String> issuedOrder = new HashMap<>();
        issuedOrder.put("orderId", orderId);
        issuedOrder.put("carName", carName);
        issuedOrder.put("carRentalLocation", carRentalLocation);
        return issuedOrder.get(data);
    }

    public String getOrderId(){
        return this.orderId;
    }


}
