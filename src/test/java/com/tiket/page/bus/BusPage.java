package com.tiket.page.bus;

import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.bus.*;
import com.tiket.api.payment.SubmitPaymentApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.payment.PaymentPage;
import com.tiket.pojo.bus.*;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.ExecutionException;

public abstract class BusPage<T extends WebDriver> extends BasePage<T> {

    public BusPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> BusPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {
                return new BusPageIOS<>(session);
            }
            case ANDROID -> {
                return new BusPageAndroid<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private String getToken() throws ExecutionException, InterruptedException {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
    }

    private BusSearchResponse busSearch(BusSearchApi.Inputs inputs) throws ExecutionException, InterruptedException {
        BusSearchApi busSearchApi = new BusSearchApi(inputs);
        ApiResponse apiResponse = busSearchApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), BusSearchResponse.class);
    }

    private BusSelectScheduleResponse busSelectSchedule(BusSelectScheduleApi.Inputs inputs) throws ExecutionException, InterruptedException {
        BusSelectScheduleApi busSelectScheduleApi = new BusSelectScheduleApi(inputs);
        ApiResponse apiResponse = busSelectScheduleApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), BusSelectScheduleResponse.class);
    }

    private BusSeatLayoutResponse busSeatLayout(BusSeatLayoutApi.Inputs inputs) throws ExecutionException, InterruptedException {
        BusSeatLayoutApi busSeatLayoutApi = new BusSeatLayoutApi(inputs);
        ApiResponse apiResponse = busSeatLayoutApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), BusSeatLayoutResponse.class);
    }

    private BusCartsResponse busCarts(BusCartsApi.Inputs inputs) throws ExecutionException, InterruptedException {
        BusCartsApi busCartsApi = new BusCartsApi(inputs);
        ApiResponse apiResponse = busCartsApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), BusCartsResponse.class);
    }

    private BusCheckoutResponse busCheckout(BusCheckoutApi.Inputs inputs) throws ExecutionException, InterruptedException {
        BusCheckoutApi busCheckoutApi = new BusCheckoutApi(inputs);
        ApiResponse apiResponse = busCheckoutApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), BusCheckoutResponse.class);
    }

    @SneakyThrows
    public BusCheckoutResponse createWaitingPaymentOrderApi(
            String email,
            String password,
            String originId,
            String destinationId,
            String departureDate
    ) {
        Unm<T> unm = new Unm<>(session);
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
        String accessToken = authData.accessToken;
        logger.log("access token : " + accessToken);



        logger.log("loginStatus-> " + email + ", " + authData.accountUsername.equalsIgnoreCase(email));
        String fullName = authData.accountFirstName + " " + authData.accountLastName;
        String profileId = String.valueOf(authData.accountId);
        String phone = authData.accountPhoneNumber;
        int adult = 1;

        BusSearchApi.Inputs busSearchApiInputs = new BusSearchApi.Inputs(accessToken, originId, destinationId, departureDate, adult);
        BusSearchResponse busSearchResponse = busSearch(busSearchApiInputs);
        logger.log("BusSearchApi-> " + busSearchResponse.message);
        String scheduleId = busSearchResponse.data.schedules.get(0).id;
        String scheduleHash = busSearchResponse.data.schedules.get(0).hash;

        BusSeatLayoutApi.Inputs busSeatLayoutApiInputs = new BusSeatLayoutApi.Inputs(accessToken, scheduleId, adult);
        BusSeatLayoutResponse busSeatLayoutResponse = busSeatLayout(busSeatLayoutApiInputs);
        logger.log("BusSeatLayoutApi-> " + busSeatLayoutResponse.message);

        String seatName = busSeatLayoutResponse.data.seatMap.get(0).rows.stream()
                .filter(rows -> rows.available)
                .findFirst()
                .orElseThrow()
                .name;

        BusCartsApi.Inputs busCartsApiInputs = new BusCartsApi.Inputs(accessToken, scheduleId, scheduleHash, seatName, adult);
        BusCartsResponse busCartsResponse = busCarts(busCartsApiInputs);
        logger.log("BusCartsApi-> " + busCartsResponse.message);
        String cartId = busCartsResponse.data.cartId;
        String cartSecret = busCartsResponse.data.secret;
        int totalPrice = busCartsResponse.data.priceDetail.totalPrice;

        BusCheckoutApi.Inputs busCheckoutApiInputs = new BusCheckoutApi.Inputs(accessToken, cartId, cartSecret, fullName, phone, email, profileId, totalPrice, seatName);
        BusCheckoutResponse busCheckoutResponse = busCheckout(busCheckoutApiInputs);
        logger.log("BusCheckoutApi-> " + busCheckoutResponse.message);
        return busCheckoutResponse;
    }

    @SneakyThrows
    public BusCheckoutResponse createIssuedOrder(
            String email,
            String password,
            String originId,
            String destinationId,
            String departureDate,
            String cardNumber,
            String cardHolderName,
            String cardExpiredMonth,
            String cardExpiredYear,
            String cardCvvNumber
    ) {
        Unm<T> unm = new Unm<>(session);
        LoginDataUnmResponse.Data data = unm.unmLoginApi(email, password);
        String accessToken = data.accessToken;


        logger.log("loginStatus-> " + email + ", " + data.accountUsername.equalsIgnoreCase(email));
        String fullName = data.accountFirstName + " " + data.accountLastName;
        String profileId = String.valueOf(data.accountId);
        String phone = data.accountPhoneNumber;
        int adult = 1;

        BusSearchApi.Inputs busSearchApiInputs = new BusSearchApi.Inputs(accessToken, originId, destinationId, departureDate, adult);
        BusSearchResponse busSearchResponse = busSearch(busSearchApiInputs);
        logger.log("BusSearchApi-> " + busSearchResponse.message);
        String scheduleId = busSearchResponse.data.schedules.get(0).id;
        String scheduleHash = busSearchResponse.data.schedules.get(0).hash;

        BusSeatLayoutApi.Inputs busSeatLayoutApiInputs = new BusSeatLayoutApi.Inputs(accessToken, scheduleId, adult);
        BusSeatLayoutResponse busSeatLayoutResponse = busSeatLayout(busSeatLayoutApiInputs);
        logger.log("BusSeatLayoutApi-> " + busSeatLayoutResponse.message);

        String seatName = busSeatLayoutResponse.data.seatMap.get(0).rows.stream()
                .filter(rows -> rows.available)
                .findFirst()
                .orElseThrow()
                .name;

        BusCartsApi.Inputs busCartsApiInputs = new BusCartsApi.Inputs(accessToken, scheduleId, scheduleHash, seatName, adult);
        BusCartsResponse busCartsResponse = busCarts(busCartsApiInputs);
        logger.log("BusCartsApi-> " + busCartsResponse.message);
        String cartId = busCartsResponse.data.cartId;
        String cartSecret = busCartsResponse.data.secret;
        int totalPrice = busCartsResponse.data.priceDetail.totalPrice;

        BusCheckoutApi.Inputs busCheckoutApiInputs = new BusCheckoutApi.Inputs(accessToken, cartId, cartSecret, fullName, phone, email, profileId, totalPrice, seatName);
        BusCheckoutResponse busCheckoutResponse = busCheckout(busCheckoutApiInputs);
        logger.log("BusCheckoutApi-> " + busCheckoutResponse.message);
        logger.log("Order id : " + busCheckoutResponse.data.order.orderId);

        //bus price ussually very low 
        SubmitPaymentApi.Inputs submitPaymentApiInputs = new SubmitPaymentApi.Inputs(busCheckoutResponse.data.order.orderHash,Long.valueOf(busCheckoutResponse.data.order.orderId));
        JSONObject paymentResponse = submitPayment(submitPaymentApiInputs);

        String paymentChecker = PaymentPage.getInstance(session).paymentChecker();

        return busCheckoutResponse;
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
}
