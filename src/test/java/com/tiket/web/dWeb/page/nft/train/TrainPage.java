package com.tiket.web.dWeb.page.nft.train;

import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.payment.SubmitSakukuApi;
import com.tiket.api.train.TrainCartApi;
import com.tiket.api.train.TrainOrderApi;
import com.tiket.api.train.TrainOrderV7Api;
import com.tiket.api.train.TrainSearchApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Exception.ApiToolsException;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.payment.SubmitSakukuResponse;
import com.tiket.pojo.train.TrainCartResponse;
import com.tiket.pojo.train.TrainOrderResponse;
import com.tiket.pojo.train.TrainSearchResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

public class TrainPage<T extends WebDriver> extends BasePage<T> {

    public TrainPage(Session<T> session) {
        super(session);
    }

    long orderId;
    String trainName;
    BigDecimal grandTotal;
    String originStation;
    String destinationStation;

    private String getToken() throws ExecutionException, InterruptedException {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
    }

    private String getTrainJourney(TrainSearchApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TrainSearchApi trainSearchApi = new TrainSearchApi(inputs);
        ApiResponse trainSearchApiResponse = trainSearchApi.hitApi(apiTools);

        Gson gson = new Gson();
        TrainSearchResponse searchResponse = gson.fromJson(trainSearchApiResponse.getBody(), TrainSearchResponse.class);

        if (searchResponse == null || searchResponse.data == null || searchResponse.data.departJourneys == null) {
            throw new ApiToolsException("Train journey search returned no data for "
                    + inputs.originCity() + "-" + inputs.destinationCity() + " on " + inputs.startDate()
                    + " (ttype=" + inputs.tripType() + "). Response: " + trainSearchApiResponse.getBody());
        }
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

        if (orderResponse == null || orderResponse.data == null || orderResponse.data.journeys == null) {
            throw new ApiToolsException("Train cart booking did not return journey details. Response: "
                    + trainOrderApiResponse.getBody());
        }
        return orderResponse.data;
    }

    private TrainOrderResponse.Data checkoutOrder(TrainOrderV7Api.Inputs inputs) throws ExecutionException, InterruptedException {
        ApiResponse checkoutApiResponse = new TrainOrderV7Api(inputs).hitApi(apiTools);

        Gson gson = new Gson();
        TrainOrderResponse checkoutResponse = gson.fromJson(checkoutApiResponse.getBody(), TrainOrderResponse.class);

        if (checkoutResponse == null || checkoutResponse.data == null || checkoutResponse.data.order == null
                || checkoutResponse.data.order.id == 0) {
            throw new ApiToolsException("Train checkout did not return a monolith order. Response: "
                    + checkoutApiResponse.getBody());
        }
        return checkoutResponse.data;
    }

    @SneakyThrows
    public String createIssuedOrderApi(
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
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                Unm<T> unm = new Unm<>(session);

                LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
                String accessToken = authData.accessToken;
                boolean loginStatus = authData.accountUsername.equals(email);
                logger.log("loginStatus-> " + email + ", " + loginStatus);

                TrainSearchApi.Inputs trainSearchApi = new TrainSearchApi.Inputs(originCity, destinationCity, startDate, tripType, adultCount, infantCount, accessToken, authData.refreshToken);
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
                logger.log("Order state: " + orderResponse.state);

                originStation = orderResponse.journeys.depart.schedules.get(0).departureStation.code;
                destinationStation = orderResponse.journeys.depart.schedules.get(0).arrivalStation.code;
                trainName = orderResponse.journeys.depart.schedules.get(0).trainName;

                // The v6 call above only stores passenger/contact on the cart and comes back with order id/hash
                // still 0; the v7 checkout is what creates the real monolith order the payment step needs.
                TrainOrderResponse.Data checkout = checkoutOrder(new TrainOrderV7Api.Inputs(accessToken, id, secret));
                orderId = checkout.order.id;
                logger.log("Order ID: " + checkout.order.id);
                logger.log("Order Hash: " + checkout.order.hash);

                SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, checkout.order.hash, orderId);
                SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
                grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal;
                logger.log("Grand Total -> "+grandTotal);
                logger.log("Reference Id -> "+submitSakukuResponse.data.sidebarPayment.referenceId);

                return String.valueOf(orderId);
            }
            catch(Exception e){
                logger.log("Attempt " + attempt + "/" + maxAttempts + " failed: " + ExceptionUtils.getStackTrace(e));
                if (attempt == maxAttempts) {
                    Assertion.skip("Train Booking via API failed after " + maxAttempts + " attempts. Skipping the subsequent steps.");
                } else {
                    logger.log("Retrying train booking via API (attempt " + (attempt + 1) + "/" + maxAttempts + ")");
                }
            }
        }
        return "";
    }

    private SubmitSakukuResponse submitSakuku(SubmitSakukuApi.Inputs inputs) throws InterruptedException, ExecutionException {
        SubmitSakukuApi submitSakukuApi = new SubmitSakukuApi(inputs);
        ApiResponse apiResponse = submitSakukuApi.hitApi(apiTools);
        Gson gson = new Gson();
        logger.log("response submit Sakuku : " + apiResponse.getBody());
        SubmitSakukuResponse submitSakukuResponse = gson.fromJson(apiResponse.getBody(), SubmitSakukuResponse.class);
        return submitSakukuResponse;
    }
}
