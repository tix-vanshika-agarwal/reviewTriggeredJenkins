package com.tiket.web.dashboard.page.order;

import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.carrental.*;
import com.tiket.api.orderDweb.LandingPageApi;
import com.tiket.api.orderDweb.WaitingForPaymentOrderApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.carrental.*;
import com.tiket.pojo.orderDweb.LandingPageResponse;
import com.tiket.pojo.orderDweb.WaitingForPaymentOrderResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class CreateCarRentalOrderPage<T extends WebDriver> extends BasePage<T> {

    public CreateCarRentalOrderPage(Session<T> session) {
        super(session);
    }

    private static final Gson gson = new Gson();
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

    @SneakyThrows
    public List<String> createProductTillPaymentApi(
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
            int rentalDuration,
            String account
    ) {
        try {
            WaitingForPaymentOrderApi.Input waitingForPaymentOrderInputs = new WaitingForPaymentOrderApi.Input(account);
            WaitingForPaymentOrderResponse waitingForPaymentOrderResponse = submitBookingWaitingOrder(waitingForPaymentOrderInputs);
            if (waitingForPaymentOrderResponse != null && waitingForPaymentOrderResponse.data != null) {
                List<WaitingForPaymentOrderResponse.OrderItem> orderItems = waitingForPaymentOrderResponse.data.list;
                WaitingForPaymentOrderResponse.Content content = orderItems.get(0).content;
                List<WaitingForPaymentOrderResponse.Product> products = content.products;
                String orderType = products.get(0).orderType;
                if (orderType.equals("CAR")) {
                    if (!orderItems.isEmpty()) {
                        WaitingForPaymentOrderResponse.OrderData orderData = orderItems.get(0).orderData;
                        if (orderData != null) {
                            orderId = orderData.orderId;
                            orderHash = orderData.orderHash;
                        }
                    }
                }
            }
            LandingPageApi.Input landingApi = new LandingPageApi.Input(orderId, orderHash);
            LandingPageResponse landingApiResponse = submitBookingLanding(landingApi);
            if ("SUCCESS".equals(landingApiResponse.code)) {
                List<String> paymentMethods = new ArrayList<>();
                if (landingApiResponse.data != null && landingApiResponse.data.entryPayment != null) {
                    List<LandingPageResponse.Recommendation> recommendations = landingApiResponse.data.entryPayment.recommendations;
                    for (LandingPageResponse.Recommendation recommendation : recommendations) {
                        paymentMethods.add(recommendation.paymentMethod);
                    }
                }
                return paymentMethods;
            } else {
                Unm<T> unm = new Unm<>(session);

                LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
                String accessToken = authData.accessToken;
                boolean loginStatus = authData.accountUsername.equals(email);
                logger.log("loginStatus-> " + email + ", " + loginStatus);

                CarRentalAutoCompleteApi.Inputs carRentalAutoCompleteApiInputs = new CarRentalAutoCompleteApi.Inputs(accessToken,location, driverOption);
                List<String> carRentalAutoCompleteData = carRentalAutoComplete(carRentalAutoCompleteApiInputs);
                regionalId = carRentalAutoCompleteData.get(0);
                placeId = carRentalAutoCompleteData.get(1);
                CarRentalSearchLocationApi.Inputs carRentalSearchLocationInputs = new CarRentalSearchLocationApi.Inputs(accessToken,placeId);
                List<String> carRentalSearchLocationData = carRentalSearchLocation(carRentalSearchLocationInputs);
                latitude = carRentalSearchLocationData.get(0);
                longitude = carRentalSearchLocationData.get(1);
                CarRentalGetLocationIdApi.Inputs carRentalGetLocationIdInputs = new CarRentalGetLocationIdApi.Inputs(regionalId, latitude, longitude, driverOption, accessToken);
                locationId = carRentalGetLocationId(carRentalGetLocationIdInputs);
                StringBuilder pickup = new StringBuilder(pickupTime);
                pickup.setCharAt(2, '-');
                String pickupTimeUpdate = pickup.toString();
                SelectCarRentalApi.Inputs selectCarRentalApiInputs = new SelectCarRentalApi.Inputs(accessToken,driverOption, regionalId, startDate, pickupTimeUpdate, latitude, longitude);
                List<String> selectCarRentalData = selectCarRental(selectCarRentalApiInputs);
                carRegionalId = selectCarRentalData.get(0);
                carName = selectCarRentalData.get(1);
                carRentalLocation = selectCarRentalData.get(2);
                SubmitBookingApi.Inputs submitBookingApiInputs = new SubmitBookingApi.Inputs(accessToken, carRegionalId, email, phoneNumber, driverOption, location, locationId, startDate, pickupTime, type, fullName, nameTitle, rentalDuration);
                SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
                orderId = submitBookingResponse.data.orderId;
                orderHash = submitBookingResponse.data.orderHash;
                if (submitBookingResponse.code.equals("SUCCESS")) {
                    LandingPageApi.Input landingApi2 = new LandingPageApi.Input(orderId, orderHash);
                    LandingPageResponse landingApiResponse2 = submitBookingLanding(landingApi2);
                    if ("SUCCESS".equals(landingApiResponse2.code)) {
                        List<String> paymentMethods = new ArrayList<>();
                        if (landingApiResponse2.data != null && landingApiResponse2.data.entryPayment != null) {
                            List<LandingPageResponse.Recommendation> recommendations = landingApiResponse2.data.entryPayment.recommendations;
                            for (LandingPageResponse.Recommendation recommendation : recommendations) {
                                paymentMethods.add(recommendation.paymentMethod);
                            }
                        }
                        return paymentMethods;
                    } else {
                        Assertion.skip("Skipping the subsequent steps.");
                    }
                }
            }
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Car Booking via API failed. Skipping the subsequent steps.");
        }
        return null;
    }

    public Map<String, String> createCarRentalProductTillPaymentApi(
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
        try {

            Unm<T> unm = new Unm<>(session);

            LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
            String accessToken = authData.accessToken;
            boolean loginStatus = authData.accountUsername.equals(email);
            logger.log("loginStatus-> " + email + ", " + loginStatus);

            Map<String, String> result = new HashMap<>();

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
            carRegionalId = selectCarRental(selectCarRentalApiInputs).get(0);
            logger.log("carRegionalId -> " + carRegionalId);
            carName = selectCarRental(selectCarRentalApiInputs).get(1);
            logger.log("carName -> " + carName);
            carRentalLocation = selectCarRental(selectCarRentalApiInputs).get(2);
            logger.log("carRentalLocation -> " + carRentalLocation);

            SubmitBookingApi.Inputs submitBookingApiInputs = new SubmitBookingApi.Inputs(accessToken, carRegionalId, email, phoneNumber, driverOption, location, locationId, startDate, pickupTime, type, fullName, nameTitle,1);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
            orderId = submitBookingResponse.data.orderId;
            orderHash = submitBookingResponse.data.orderHash;
            logger.log("orderId-> " + orderId);
            logger.log("orderHash " + orderHash);
            if (orderId == null) {
                Assertion.skip("Car Booking via API failed. Skipping the subsequent steps.");
            }
            result.put("orderId", orderId);
            result.put("orderHash", orderHash);
            return result;
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Car Booking via API failed. Skipping the subsequent steps.");
        }
        return null;
    }

    public Map<String, String> createGuestCarRentalTillPaymentApi(
            String email,
            String phoneNumber,
            String driverOption,
            String location,
            String startDate,
            String pickupTime,
            String type,
            String fullName,
            String nameTitle,
            int rentalDuration
    ) {
        try {
            String accessToken = getToken();

            Map<String, String> result = new HashMap<>();

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
            carRegionalId = selectCarRental(selectCarRentalApiInputs).get(0);
            logger.log("carRegionalId -> " + carRegionalId);
            carName = selectCarRental(selectCarRentalApiInputs).get(1);
            logger.log("carName -> " + carName);
            carRentalLocation = selectCarRental(selectCarRentalApiInputs).get(2);
            logger.log("carRentalLocation -> " + carRentalLocation);

            SubmitBookingApi.Inputs submitBookingApiInputs = new SubmitBookingApi.Inputs(accessToken, carRegionalId, email, phoneNumber, driverOption, location, locationId, startDate, pickupTime, type, fullName, nameTitle,rentalDuration);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
            orderId = submitBookingResponse.data.orderId;
            orderHash = submitBookingResponse.data.orderHash;
            logger.log("orderId-> " + orderId);
            logger.log("orderHash " + orderHash);
            if (orderId == null) {
                Assertion.skip("Car Booking via API failed. Skipping the subsequent steps.");
            }
            result.put("orderId", orderId);
            result.put("orderHash", orderHash);
            return result;
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Car Booking via API failed. Skipping the subsequent steps.");
        }
        return null;
    }

    private LandingPageResponse submitBookingLanding(LandingPageApi.Input bookingData)
            throws InterruptedException, ExecutionException {
        LandingPageApi submitBookingCarApi = new LandingPageApi(bookingData);
        ApiResponse submitBookingCarApiResponse = submitBookingCarApi.hitApi(apiTools);
        LandingPageResponse responsePojo = gson.fromJson(submitBookingCarApiResponse.getBody(), LandingPageResponse.class);
        return responsePojo;
    }

    private WaitingForPaymentOrderResponse submitBookingWaitingOrder(WaitingForPaymentOrderApi.Input bookingData)
            throws InterruptedException, ExecutionException {
        WaitingForPaymentOrderApi waitingForPaymentOrderApi = new WaitingForPaymentOrderApi(bookingData);
        ApiResponse waitingForPaymentOrderApiResponse = waitingForPaymentOrderApi.hitApi(apiTools);
        WaitingForPaymentOrderResponse responsePojo = gson.fromJson(waitingForPaymentOrderApiResponse.getBody(), WaitingForPaymentOrderResponse.class);
        return responsePojo;
    }

    @SneakyThrows
    public List<String> getPaymentRecommendationList(String referenceId, String orderHash) {
        try {
            LandingPageApi.Input landingApi = new LandingPageApi.Input(referenceId, orderHash);
            LandingPageResponse landingApiResponse = submitBookingLanding(landingApi);

            if ("SUCCESS".equals(landingApiResponse.code)) {
                List<String> paymentMethods = new ArrayList<>();
                if (landingApiResponse.data != null && landingApiResponse.data.entryPayment != null) {
                    List<LandingPageResponse.Recommendation> recommendations = landingApiResponse.data.entryPayment.recommendations;
                    for (LandingPageResponse.Recommendation recommendation : recommendations) {
                        paymentMethods.add(recommendation.paymentMethod);
                    }
                }
                return paymentMethods;
            } else {
                Assertion.skip("Skipping the subsequent steps.");
            }
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Car Booking via API failed. Skipping the subsequent steps.");
        }
        return Collections.emptyList();
    }

    private String getToken() throws ExecutionException, InterruptedException {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer " + accessToken;
    }

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

    private SubmitBookingResponse submitBooking(SubmitBookingApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitBookingApi submitBookingApi = new SubmitBookingApi(inputs);
        ApiResponse submitBookingApiResponse = submitBookingApi.hitApi(apiTools);
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

    public String getOrderId() {
        return this.orderId;
    }
}