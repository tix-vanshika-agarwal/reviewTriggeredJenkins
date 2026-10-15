package com.tiket.web.dWeb.page.nft.car_transfer;

import com.google.gson.Gson;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardApi;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.carTransfer.CarTransferAddToCartApi;
import com.tiket.api.carTransfer.CarTransferAdminConfirmOrderApi;
import com.tiket.api.carTransfer.CarTransferCheckOutApi;
import com.tiket.api.carTransfer.CarTransferGetCatalogueIdApi;
import com.tiket.api.payment.PaidCheckerApi;
import com.tiket.api.payment.SubmitSakukuApi;
import com.tiket.apiPage.CarTransferApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.cartransfer.AddToCartResponseAT;
import com.tiket.pojo.cartransfer.CheckOutResponseAT;
import com.tiket.pojo.cartransfer.GetCatalogueIdResponseAT;
import com.tiket.pojo.payment.PaidCheckerResponse;
import com.tiket.pojo.payment.SubmitSakukuResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CarTransferPage <T extends WebDriver> extends BasePage<T> {

    public CarTransferPage(Session<T> session) {
        super(session);
    }

    int orderId;
    String orderName;
    BigDecimal grandTotal;
    String pickupPlace;
    String pickupAddress;
    String dropPlace;
    String dropAddress;
    String orderHash;
    String salutation;
    String guestName;
    String phoneNumber;
    String carType;

    private String getAdminAccessTokenDashboardAuth() throws ExecutionException, InterruptedException{
        POSTGetAdminAccessTokenDashboardApi postGetAdminAccessTokenDashboardApi = new POSTGetAdminAccessTokenDashboardApi();
        ApiResponse response = postGetAdminAccessTokenDashboardApi.hitApi(apiTools);
        String isSuccessAuth = new JSONArray(response.getBody()).getJSONObject(0).getJSONObject("data").getJSONObject("loginUser").get("message").toString();
        logger.log("res : " + isSuccessAuth);
        if("SUCCESS".equals(isSuccessAuth)){
            Map<String, ArrayList<Object>> header = response.getHeaderParams();
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

    private String getToken() throws ExecutionException, InterruptedException {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
    }

    private String CarTransferGetCatalogueId(CarTransferGetCatalogueIdApi.Inputs inputs) throws ExecutionException, InterruptedException {
        CarTransferGetCatalogueIdApi carTransferGetCatalogueIdApi = new CarTransferGetCatalogueIdApi(inputs);
        ApiResponse carTransferGetCatalogueIdApiResponse = carTransferGetCatalogueIdApi.hitApi(apiTools);

        Gson gson = new Gson();
        GetCatalogueIdResponseAT getCatalogueIdResponseAT = gson.fromJson(carTransferGetCatalogueIdApiResponse.getBody(), GetCatalogueIdResponseAT.class);

        return getCatalogueIdResponseAT.data.items.get(0).fleets.get(0).catalogueItemId;
    }

    private AddToCartResponseAT CarTransferAddToCart(CarTransferAddToCartApi.Inputs inputs) throws ExecutionException, InterruptedException{
        CarTransferAddToCartApi carTransferAddToCartApi = new CarTransferAddToCartApi(inputs);
        ApiResponse carTransferAddToCartApiResponse = carTransferAddToCartApi.hitApi(apiTools);
        Gson gson = new Gson();
        AddToCartResponseAT addToCartResponseAT = gson.fromJson(carTransferAddToCartApiResponse.getBody(), AddToCartResponseAT.class);
        setDataFromAddToCart(addToCartResponseAT);

        return addToCartResponseAT;
    }

    private CheckOutResponseAT CarTransferCheckOut(CarTransferCheckOutApi.Inputs inputs)  throws ExecutionException, InterruptedException {
        CarTransferCheckOutApi carTransferCheckOutApi = new CarTransferCheckOutApi(inputs);
        ApiResponse carTransferCheckOutResponse = carTransferCheckOutApi.hitApi(apiTools);

        Gson gson = new Gson();
        CheckOutResponseAT checkOutResponse = gson.fromJson(carTransferCheckOutResponse.getBody(), CheckOutResponseAT.class);

        return checkOutResponse;
    }

    public String confirmOrder(String orderId,String adminName,String adminPhoneNumber) throws ExecutionException, InterruptedException{
        String dashboardToken = getAdminAccessTokenDashboardAuth();
        CarTransferAdminConfirmOrderApi.Inputs inputs = new CarTransferAdminConfirmOrderApi.Inputs(dashboardToken,adminName,adminPhoneNumber,orderId);
        CarTransferAdminConfirmOrderApi carRentalAdminConfirmOrderApi = new CarTransferAdminConfirmOrderApi(inputs);
        ApiResponse response = carRentalAdminConfirmOrderApi.hitApi(apiTools);
        logger.log("response confirm order: " + response.getBody());
        JSONObject data = new JSONObject(response.getBody());
        return data.getString("code");
    }

    @SneakyThrows
    public String createPaidOrderApi(
            String username,
            String password,
            String origin,
            String destination,
            String type,
            String rangeDate,
            String pickupTime,
            int pax
    ) {
        try {
            CarTransferApi<?> carTransferApi = new CarTransferApi<>(session);
            String orderIdString = carTransferApi.createPaidOrderApiPayUsingSakuku(
                    username, password, origin, destination, type, rangeDate, pickupTime, pax);
            logger.log("Order ID: " + orderIdString);
            return orderIdString;
        }
        catch(Exception e){
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Airport transfer booking via API failed. Skipping the subsequent steps.");
        }
        return "";
    }

    @SneakyThrows
    public String createIssuedOrderApi(
            String username,
            String password,
            String adminName,
            String adminPhoneNumber,
            String origin,
            String destination,
            String type,
            String rangeDate,
            String pickupTime,
            int pax
    ) {
        try {
            CarTransferApi<?> carTransferApi = new CarTransferApi<>(session);
            String orderIdString = carTransferApi.createIssuedOrderApiPayUsingSakuku(
                    username, password, adminName, adminPhoneNumber,
                    origin, destination, type, rangeDate, pickupTime, pax);
            logger.log("Order ID: " + orderIdString);
            return orderIdString;
        }
        catch(Exception e){
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Airport transfer booking via API failed. Skipping the subsequent steps.");
        }
        return "";
    }

    private String paidCheckerStatus(long duration) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        long waitTime = duration * 1000;
        boolean paymentSuccessful = false;
        String paymentStatus = null;

        while (System.currentTimeMillis() - startTime < waitTime && !paymentSuccessful) {
            PaidCheckerApi.Inputs paidCheckerApiInputs = new PaidCheckerApi.Inputs(String.valueOf(orderId));
            paymentStatus = hitPaidChecker(paidCheckerApiInputs, 3);

            logger.log("Payment Status -> " + paymentStatus);
            if (paymentStatus.equals("SUCCESS")) {
                paymentSuccessful = true;
            } else {
                Thread.sleep(5000);
            }
        }
        return paymentStatus;
    }

    private SubmitSakukuResponse submitSakuku(SubmitSakukuApi.Inputs inputs) throws InterruptedException, ExecutionException {
        SubmitSakukuApi submitSakukuApi = new SubmitSakukuApi(inputs);
        ApiResponse apiResponse = submitSakukuApi.hitApi(apiTools);
        Gson gson = new Gson();
        logger.log("response submit Sakuku : " + apiResponse.getBody());
        SubmitSakukuResponse submitSakukuResponse = gson.fromJson(apiResponse.getBody(), SubmitSakukuResponse.class);
        return submitSakukuResponse;
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

    private void setDataFromAddToCart(AddToCartResponseAT addToCartResponseAT){
        pickupPlace = addToCartResponseAT.data.origin.name;
        pickupAddress = addToCartResponseAT.data.origin.address;
        dropPlace = addToCartResponseAT.data.destination.name;
        dropAddress = addToCartResponseAT.data.destination.address;
        orderName = addToCartResponseAT.data.fleet.name;
        carType = addToCartResponseAT.data.fleet.name;
    }
}
