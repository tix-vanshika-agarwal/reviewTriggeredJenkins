package com.tiket.web.dashboard.page.order;

import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.ferry.AddToCartFerryApi;
import com.tiket.api.ferry.SearchScheduleFerryApi;
import com.tiket.api.ferry.SubmitBookingFerryApi;
import com.tiket.api.orderDweb.LandingPageApi;
import com.tiket.api.orderDweb.WaitingForPaymentOrderApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.ferry.AddToCartFerryResponse;
import com.tiket.pojo.ferry.SearchScheduleFerryResponse;
import com.tiket.pojo.ferry.SubmitBookingFerryResponse;
import com.tiket.pojo.orderDweb.LandingPageResponse;
import com.tiket.pojo.orderDweb.WaitingForPaymentOrderResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CreateFerryOrderPage <T extends WebDriver> extends BasePage<T>{
    
    public CreateFerryOrderPage(Session<T> session) { super(session); }

    String orderId;
    String orderHash;
    BigDecimal grandTotal;
    private static final Gson gson = new Gson();

    private String getToken() throws ExecutionException, InterruptedException {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer " + accessToken;
    }

    private SearchScheduleFerryResponse searchFerry(SearchScheduleFerryApi.Input input)
            throws InterruptedException, ExecutionException {
        SearchScheduleFerryApi searchScheduleFerryApi = new SearchScheduleFerryApi(input);
        ApiResponse searchScheduleFerryResponse = searchScheduleFerryApi.hitApi(apiTools);
        logger.log("DEBUG raw search result : " + searchScheduleFerryResponse.getBody());
        SearchScheduleFerryResponse responsePojo = gson.fromJson(searchScheduleFerryResponse.getBody(),
                SearchScheduleFerryResponse.class);
        return responsePojo;
    }

    private SearchScheduleFerryResponse.Schedule filterSchedule(SearchScheduleFerryResponse.Data searchResultData) {
        return searchResultData.schedules.stream().filter(data -> data.availableQuota > 0).findFirst().orElseThrow();
    }

    private AddToCartFerryResponse addToCart(AddToCartFerryApi.Input input)
            throws InterruptedException, ExecutionException {
        AddToCartFerryApi addToCartFerryApi = new AddToCartFerryApi(input);
        ApiResponse addToCartFerryResponse = addToCartFerryApi.hitApi(apiTools);
        AddToCartFerryResponse responsePojo = gson.fromJson(addToCartFerryResponse.getBody(),
                AddToCartFerryResponse.class);
        return responsePojo;
    }

    private SubmitBookingFerryResponse submitBooking(SubmitBookingFerryApi.Input bookingData)
            throws InterruptedException, ExecutionException {
        SubmitBookingFerryApi submitBookingFerryApi = new SubmitBookingFerryApi(bookingData);
        ApiResponse submitBookingFerryApiResponse = submitBookingFerryApi.hitApi(apiTools);
        logger.log("debug : " + submitBookingFerryApiResponse.getBody());
        SubmitBookingFerryResponse responsePojo = gson.fromJson(submitBookingFerryApiResponse.getBody(),
                SubmitBookingFerryResponse.class);
        return responsePojo;
    }

    @SneakyThrows
    public List<String> createWaitingPaymentOrderFerry(
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
            int elderly,
            String account) {
        try {

            WaitingForPaymentOrderApi.Input waitingForPaymentOrderInputs = new WaitingForPaymentOrderApi.Input(account);
            WaitingForPaymentOrderResponse waitingForPaymentOrderResponse = submitBookingWaitingOrder(waitingForPaymentOrderInputs);
            if (waitingForPaymentOrderResponse != null && waitingForPaymentOrderResponse.data != null) {
                List<WaitingForPaymentOrderResponse.OrderItem> orderItems = waitingForPaymentOrderResponse.data.list;
                WaitingForPaymentOrderResponse.Content content = orderItems.get(0).content;
                List<WaitingForPaymentOrderResponse.Product> products = content.products;
                String orderType = products.get(0).orderType;
                if(orderType.equals("FERRY")){
                    if (!orderItems.isEmpty()) {
                        WaitingForPaymentOrderResponse.OrderData orderData = orderItems.get(0).orderData;
                        if (orderData != null) {
                            orderId = orderData.orderId;
                            orderHash = orderData.orderHash;
                        }
                    }
                 }
                }
                //hitting the landing api if got success then return payment methods
                LandingPageApi.Input landingApi = new LandingPageApi.Input(orderId, orderHash);
                LandingPageResponse landingApiResponse = submitBookingLanding(landingApi);
                if("SUCCESS".equals(landingApiResponse.code)) {
                    List<String> paymentMethods = new ArrayList<>();
                    if (landingApiResponse.data != null && landingApiResponse.data.entryPayment != null) {
                        List<LandingPageResponse.Recommendation> recommendations = landingApiResponse.data.entryPayment.recommendations;
                        for (LandingPageResponse.Recommendation recommendation : recommendations) {
                            paymentMethods.add(recommendation.paymentMethod);
                        }
                    }
                    return paymentMethods;
                 }
                 else{
                    Unm<T> unm = new Unm<>(session);

                    LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
                    String token = authData.accessToken;
                    boolean loginStatus = authData.accountUsername.equals(email);
                    logger.log("loginStatus-> " + email + ", " + loginStatus);

                    // search for ferry
                    SearchScheduleFerryApi.Input searchQuery = new SearchScheduleFerryApi.Input(token, origin, destination,
                            departureDate, departureTime, vehicleType, adult, child, infant, elderly);
                    SearchScheduleFerryResponse searchResponse = searchFerry(searchQuery);
                    SearchScheduleFerryResponse.Schedule selectedSchedule = this.filterSchedule(searchResponse.data);
                    grandTotal = selectedSchedule.price.finalPrice;
                    logger.log("grand total without service fee: " + grandTotal);
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
                    SubmitBookingFerryApi.Input submitBookingInput = new SubmitBookingFerryApi.Input(authData.accountUsername, token, licensePlate, cartId, secretId, adult, child, infant);
                    SubmitBookingFerryResponse responseSubmitBooking = submitBooking(submitBookingInput);
                    orderId = (responseSubmitBooking.data.order.orderId);
                    orderHash = (responseSubmitBooking.data.order.orderHash);
                    //get ferry total price + service fee

                    if (responseSubmitBooking.code.equals("SUCCESS")){
                        LandingPageApi.Input landingApi2 = new LandingPageApi.Input(orderId, orderHash);
                        LandingPageResponse landingApiResponse2 = submitBookingLanding(landingApi2);
                        if("SUCCESS".equals(landingApiResponse2.code)) {
                            List<String> paymentMethods = new ArrayList<>();
                            if (landingApiResponse2.data != null && landingApiResponse2.data.entryPayment != null) {
                                List<LandingPageResponse.Recommendation> recommendations = landingApiResponse2.data.entryPayment.recommendations;
                                for (LandingPageResponse.Recommendation recommendation : recommendations) {
                                    paymentMethods.add(recommendation.paymentMethod);
                                }
                            }
                            return paymentMethods;
                        }
                        else{
                            Assertion.skip("Skipping the subsequent steps.");
                        }
                    }
                    else {
                        Assertion.skip("Ferry Booking via API failed. Skipping the subsequent steps.");
                    }
                }
            } catch (Exception e) {
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
                Assertion.skip("Ferry Booking via API failed. Skipping the subsequent steps.");
            }
        return null;
    }

    private LandingPageResponse submitBookingLanding(LandingPageApi.Input bookingData)
            throws InterruptedException, ExecutionException {
                LandingPageApi submitBookingFerryApi = new LandingPageApi(bookingData);
        ApiResponse submitBookingFerryApiResponse = submitBookingFerryApi.hitApi(apiTools);
        logger.log("debug : " + submitBookingFerryApiResponse.getBody());
        LandingPageResponse responsePojo = gson.fromJson(submitBookingFerryApiResponse.getBody(),
        LandingPageResponse.class);
        return responsePojo;
    }

    private WaitingForPaymentOrderResponse submitBookingWaitingOrder(WaitingForPaymentOrderApi.Input bookingData)
            throws InterruptedException, ExecutionException {
                WaitingForPaymentOrderApi waitingForPaymentOrderApi = new WaitingForPaymentOrderApi(bookingData);
        ApiResponse waitingForPaymentOrderApiResponse = waitingForPaymentOrderApi.hitApi(apiTools);
        logger.log("debug : " + waitingForPaymentOrderApiResponse.getBody());
        WaitingForPaymentOrderResponse responsePojo = gson.fromJson(waitingForPaymentOrderApiResponse.getBody(),
        WaitingForPaymentOrderResponse.class);
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
            }
            else{
                Assertion.skip("Skipping the subsequent steps.");
            }
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Ferry Booking via API failed. Skipping the subsequent steps.");
        }
        return Collections.emptyList();
    }
}
