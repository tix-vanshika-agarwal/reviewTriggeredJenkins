package com.tiket.page.train;

import com.google.gson.Gson;
import com.tiket.api.order.CompleteOrderListApi;
import com.tiket.api.order.OrderListApi;
import com.tiket.api.payment.PaidCheckerApi;
import com.tiket.api.payment.SubmitPaymentApi;
import com.tiket.api.payment.SubmitSakukuApi;
import com.tiket.api.train.TrainCartApi;
import com.tiket.api.train.TrainOrderApi;
import com.tiket.api.train.TrainOrderV7Api;
import com.tiket.api.train.TrainSearchApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Exception.ApiEnvironmentException;
import com.tiket.model.Exception.ApiToolsException;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.pojo.order.CompleteOrderListResponse;
import com.tiket.pojo.order.OrderListResponse;
import com.tiket.pojo.payment.PaidCheckerResponse;
import com.tiket.pojo.payment.SubmitSakukuResponse;
import com.tiket.pojo.train.TrainCartResponse;
import com.tiket.pojo.train.TrainInternalOrderDetail;
import com.tiket.pojo.train.TrainOrderResponse;
import com.tiket.pojo.train.TrainSearchResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TrainPage<T extends WebDriver> extends BasePage<T> {

    public TrainPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TrainPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {
                return new TrainPageIOS<>(session);
            }
            case ANDROID -> {
                return new TrainPageAndroid<>(session);
            }
            case DWEB, AMWEB, IMWEB-> {
                return new TrainPageWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    long orderId;
    String trainName;
    BigDecimal grandTotal;
    String originStation;
    String destinationStation;
    /** Arrival date at the destination station, formatted yyyy-MM-dd. */
    String arrivalDate;
    @Getter
    String orderHash;

    @SneakyThrows
    public Boolean createIssuedOrderApi(
            String email,
            String password,
            String name,
            String phone,
            String idNumber,
            String originCity,
            String destinationCity,
            String startDate,
            String tripType,
            int adultCount,
            int infantCount
    ) {
        try {
            Unm<T> unm = new Unm<>(session);
            LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
            String accessToken = authData.accessToken;

            TrainSearchApi.Inputs trainSearchApi = new TrainSearchApi.Inputs(originCity, destinationCity, startDate, tripType, adultCount, infantCount);
            String trainJourneyCode = getTrainJourney(trainSearchApi);
            logger.log("Available journeys: " + trainJourneyCode);

            TrainCartApi.Inputs trainCartApi = new TrainCartApi.Inputs(accessToken, trainJourneyCode, adultCount, infantCount);
            TrainCartResponse.Data data = addToCart(trainCartApi);
            String id = data.id;
            String secret = data.secret;
            logger.log("Cart ID: " + id);
            logger.log("Secret: " + secret);
            logger.log("State: " + data.state);

            TrainOrderApi.Inputs trainBookApi = new TrainOrderApi.Inputs(accessToken, id, secret, name, phone, email, idNumber, true);
            TrainOrderResponse.Data orderResponse = bookTrain(trainBookApi);
            orderId = orderResponse.order.id;
            orderHash = orderResponse.order.hash;
            logger.log("Order state: " + orderResponse.state);
            logger.log("Order ID: " + orderResponse.order.id);
            logger.log("Order Hash: " + orderResponse.order.hash);

            originStation = orderResponse.journeys.depart.schedules.get(0).departureStation.code;
            destinationStation = orderResponse.journeys.depart.schedules.get(0).arrivalStation.code;
            trainName = orderResponse.journeys.depart.schedules.get(0).trainName;

            SubmitPaymentApi.Inputs submitPaymentApiInputs = new SubmitPaymentApi.Inputs(orderResponse.order.hash, orderId);
            JSONObject paymentResponse = submitPayment(submitPaymentApiInputs);
            grandTotal = paymentResponse.getJSONObject("sidebarPayment").getBigDecimal("grandTotal");
            logger.log("grandTotal-> " + grandTotal);

            CompleteOrderListApi.Inputs completeOrderListApi = new CompleteOrderListApi.Inputs( accessToken);
            return completeOrderList(completeOrderListApi, orderId);
        }
        catch(Exception e){
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Train Booking via API failed. Skipping the subsequent steps.");
        }
        return false;
    }

    private boolean completeOrderList(CompleteOrderListApi.Inputs inputs, long orderId) throws ExecutionException, InterruptedException {
        logger.log("*********initial order id**********"+orderId);
        Thread.sleep(1000);
        CompleteOrderListApi CompleteOrderListApi = new CompleteOrderListApi(inputs);
        ApiResponse apiResponse = CompleteOrderListApi.hitApi(apiTools);
        CompleteOrderListResponse response = new Gson().fromJson(apiResponse.getBody(), CompleteOrderListResponse.class);
        logger.log("*********Response after**********"+apiResponse.getBody());
        for (int i = 0; i < response.data.views.size() - 1; i++) {
            logger.log("*********Response 1st loop**********"+response.data.views.get(i).id);
            if ("activeCards".equals(response.data.views.get(i).id)) {
                int sizeOfCards = response.data.views.get(i).activeCards.cards.size();
                int j = 0;
                do {
                    String cardOrderIdString = response.data.views.get(i).activeCards.cards.get(j).orderData.orderId;
                    logger.log("*********Response Order Id************"+cardOrderIdString);

                    long cardOrderId = Long.parseLong(cardOrderIdString);
                    if (orderId == cardOrderId) {
                        return true;
                    }
                    j++;
                }
                while(j < sizeOfCards-1);
            } else if (i==response.data.views.size()-1) {
                return false;
            }
        }
        return false;
    }

    @SneakyThrows
    public String createWaitingPaymentOrderApi(
            String email,
            String password,
            String name,
            String phone,
            String idNumber,
            String originCity,
            String destinationCity,
            String startDate,
            String tripType,
            int adultCount,
            int infantCount
    ) {
        Unm<T> unm = new Unm<>(session);
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
        String accessToken = authData.accessToken;
        String phoneNumber = authData.accountPhoneNumber;
        String nameAccount = authData.accountFirstName + " " + authData.accountLastName;

        TrainSearchApi.Inputs trainSearchApi = new TrainSearchApi.Inputs(originCity, destinationCity, startDate, tripType, adultCount, infantCount);
        String trainJourneyCode = getTrainJourney(trainSearchApi);
        logger.log("Available journeys: " + trainJourneyCode);

        TrainCartApi.Inputs trainCartApi = new TrainCartApi.Inputs(accessToken,trainJourneyCode,adultCount,infantCount);
        TrainCartResponse.Data data = addToCart(trainCartApi);
        String id = data.id;
        String secret = data.secret;
        logger.log("Cart ID: " + id);
        logger.log("Secret: " + secret);
        logger.log("State: " + data.state);

        TrainOrderApi.Inputs trainBookApi = new TrainOrderApi.Inputs(accessToken,id, secret, nameAccount, phoneNumber, email, idNumber, true);
        TrainOrderResponse.Data orderResponse = bookTrain(trainBookApi);

        orderId = orderResponse.order.id;
        orderHash = orderResponse.order.hash;
        logger.log("Order state: " + orderResponse.state);
        logger.log("Order ID: " + orderResponse.order.id);
        logger.log("Order Hash: " + orderResponse.order.hash);

        originStation = orderResponse.journeys.depart.schedules.get(0).departureStation.code;
        destinationStation = orderResponse.journeys.depart.schedules.get(0).arrivalStation.code;
        trainName = orderResponse.journeys.depart.schedules.get(0).trainName;

        OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
        TrainInternalOrderDetail trainOrderDetailResponse = orderDetailPage.getTrainInternalOrderData(String.valueOf(orderId));
        logger.log("grand total : "+trainOrderDetailResponse.data.price);
        grandTotal = trainOrderDetailResponse.data.price;

        return String.valueOf(orderId);
    }

    private String getTrainJourney(TrainSearchApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TrainSearchApi trainSearchApi = new TrainSearchApi(inputs);
        ApiResponse trainSearchApiResponse = trainSearchApi.hitApi(apiTools);

        Gson gson = new Gson();
        TrainSearchResponse searchResponse = gson.fromJson(trainSearchApiResponse.getBody(), TrainSearchResponse.class);

        if(searchResponse.data.departJourneys.count == 0){
            throw new ApiToolsException("No journeys available, try changing date or stations!");
        }
        return searchResponse.data.departJourneys.journeys.get(0).id;
    }

    private TrainCartResponse.Data addToCart(TrainCartApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TrainCartApi trainCartApi = new TrainCartApi(inputs);
        ApiResponse trainCartApiResponse = trainCartApi.hitApi(apiTools);

        Gson gson = new Gson();
        TrainCartResponse cartResponse = gson.fromJson(trainCartApiResponse.getBody(), TrainCartResponse.class);

        return cartResponse.data;
    }

    private TrainOrderResponse.Data bookTrain(TrainOrderApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TrainOrderApi trainOrderApi = new TrainOrderApi(inputs);
        ApiResponse trainOrderApiResponse = trainOrderApi.hitApi(apiTools);

        Gson gson = new Gson();
        TrainOrderResponse orderResponse = gson.fromJson(trainOrderApiResponse.getBody(), TrainOrderResponse.class);

        return orderResponse.data;
    }

    private SubmitSakukuResponse submitSakuku(SubmitSakukuApi.Inputs inputs) throws InterruptedException, ExecutionException {
        SubmitSakukuApi submitSakukuApi = new SubmitSakukuApi(inputs);
        ApiResponse apiResponse = submitSakukuApi.hitApi(apiTools);
        Gson gson = new Gson();
        logger.log("response submit Sakuku : " + apiResponse.getBody());
        return gson.fromJson(apiResponse.getBody(), SubmitSakukuResponse.class);
    }

    private String paidCheckerStatus(long duration) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        long waitTime = duration * 1000;
        boolean paymentSuccessful = false;
        String paymentStatus = null;

        while (System.currentTimeMillis() - startTime < waitTime && !paymentSuccessful) {
            PaidCheckerApi.Inputs paidCheckerApiInputs = new PaidCheckerApi.Inputs(String.valueOf(orderId));
            paymentStatus = hitPaidChecker(paidCheckerApiInputs, 1);

            if ("SUCCESS".equalsIgnoreCase(paymentStatus)) {
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
        for (int i = 0; i < retry; i++) {
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

    private JSONObject submitPayment(SubmitPaymentApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitPaymentApi submitPaymentApi = new SubmitPaymentApi(inputs);
        ApiResponse apiResponse = submitPaymentApi.hitApi(apiTools);

        JSONArray responseArray = new JSONArray(apiResponse.getBody());
        return responseArray.getJSONObject(0)
                .getJSONObject("data")
                .getJSONObject("paymentSubmit")
                .getJSONObject("data");
    }

    private boolean orderList(OrderListApi.Inputs inputs, long orderId) throws ExecutionException, InterruptedException {
        OrderListApi orderListApi = new OrderListApi(inputs);
        String expectedStatus = "ETICKET_ISSUED";
        String status = null;

        int count = 0;
        do {
            ApiResponse apiResponse = orderListApi.hitApi(apiTools);
            OrderListResponse response = new Gson().fromJson(apiResponse.getBody(), OrderListResponse.class);
            status = response.data.content.stream()
                    .filter(content -> content.orderId == orderId)
                    .findFirst()
                    .orElseThrow()
                    .orderStatus;
            count++;
            Thread.sleep(1000);
        } while(!expectedStatus.equals(status) && count < 180);

        return expectedStatus.equals(status);
    }

    public String issuedOrderData(String data) {
        HashMap<String, String> issuedOrder = new HashMap<>();
        issuedOrder.put("trainName", trainName);
        issuedOrder.put("orderId", String.valueOf(orderId));
        issuedOrder.put("grandTotal", "IDR "+grandTotal.toString());
        issuedOrder.put("originStation",originStation);
        issuedOrder.put("destinationStation",destinationStation);
        return issuedOrder.get(data);
    }

    /** Returns the leading yyyy-MM-dd of a train date-time string (e.g. "2026-10-01 08:00:00"), or null if absent. */
    private static String toIsoDate(String dateTime) {
        if (dateTime == null || !dateTime.matches("^\\d{4}-\\d{2}-\\d{2}.*")) {
            return null;
        }
        return dateTime.substring(0, 10);
    }

    public long getOrderId(){
        return this.orderId;
    }

    public BigDecimal getGrandTotal(){
        return this.grandTotal;
    }
}
