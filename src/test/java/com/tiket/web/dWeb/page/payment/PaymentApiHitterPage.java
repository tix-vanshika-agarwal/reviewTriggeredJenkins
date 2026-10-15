package com.tiket.web.dWeb.page.payment;

import com.google.gson.Gson;
import com.tiket.api.paymentDweb.*;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.paymentDweb.MandiriTransferResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.ExecutionException;

public class PaymentApiHitterPage<T extends WebDriver> extends BasePage<T> {

    private static final Gson gson = new Gson();
    
    public PaymentApiHitterPage(Session<T> session) {
        super(session);
    }

    @SneakyThrows
    public void hitMandiriTransferApi(
        String referenceId,
        int amount
    ){
        MandiriTransferApi.Input mandiriTransferApi = new MandiriTransferApi.Input(referenceId,amount);
        MandiriTransferResponse mandiriTransferResponse = mandiriTransferPayment(mandiriTransferApi);
        if(mandiriTransferResponse.responseCode.equals("00") && mandiriTransferResponse.responseMessage.equals("Send Success")){
            logger.log("Payment through API is done");
        }
        else{
            Assertion.fail("Not able to complete the payment");
        }
    }

     @SneakyThrows
    public void hitVAStagingGenerateTokenAndHitPayment(
        String bankCd,
        String amount,
        String referenceNo
    ){
        VAStagingGenerateTokenApi.Input vaStagingGenerateTokenApi = new VAStagingGenerateTokenApi.Input(bankCd,amount);
        String token = vaStagingGenerateTokenApi(vaStagingGenerateTokenApi);
        VAStagingHitPaymentApi.Input vaStagingHitPaymentApi = new VAStagingHitPaymentApi.Input(bankCd,amount,referenceNo,token);
        String code = vaStagingHitPayment(vaStagingHitPaymentApi);
        if(code.equals("SUCCESS") || code.equals("06")){
            logger.log("Payment through API is success");
        }
        else{
            Assertion.fail("Not able to complete the payment");
        }
    }

    @SneakyThrows
    public void hitKlikBCAApi(
        String referenceId,
        String amount
    ){
        KlikBCAApi.Input klikBCAApi = new KlikBCAApi.Input(referenceId,amount);
        KlikBCAApiPayment(klikBCAApi);
    }

    @SneakyThrows
    public void hitBCAVAApi(
        String CompanyCode,
        String CustomerNumber,
        String RequestID,
        String ChannelType,
        String TransactionDate,
        String AdditionalData,
        String CustomerName,
        String CurrencyCode,
        String PaidAmount,
        String TotalAmount
    ){
        BCAVAGetTokenApi.Input BCAVAGetTokenApi = new BCAVAGetTokenApi.Input();
        String token = BCAVAgetToken(BCAVAGetTokenApi);
        logger.log("token-->" + token);

        logger.log("Hitting the inquiry API");
        BCAVAInquiryApi.Input BCAVAInquiryApi = new BCAVAInquiryApi.Input(CompanyCode,CustomerNumber,RequestID,ChannelType,TransactionDate,AdditionalData,token);
        BCAVAHitInquiry(BCAVAInquiryApi);

        logger.log("Hitting the notify API");
        BCAVANotifyApi.Input BCAVANotifyApi = new BCAVANotifyApi.Input(CompanyCode, CustomerNumber, RequestID, ChannelType, TransactionDate, token, CustomerName, CurrencyCode ,PaidAmount , TotalAmount);
        BCAVAHitNotify(BCAVANotifyApi);
    }

    private void KlikBCAApiPayment(KlikBCAApi.Input inputData)
    throws InterruptedException, ExecutionException {
        KlikBCAApi klikBCAApi = new KlikBCAApi(inputData);
        ApiResponse klikBCAApiResponse = klikBCAApi.hitApi(apiTools);
        logger.log("debug : " + klikBCAApiResponse.getBody());
    }

    private String vaStagingGenerateTokenApi(VAStagingGenerateTokenApi.Input bookingData)
    throws InterruptedException, ExecutionException {
        VAStagingGenerateTokenApi stagingGenerateTokenApi = new VAStagingGenerateTokenApi(bookingData);
        ApiResponse stagingGenerateTokenApiResponse = stagingGenerateTokenApi.hitApi(apiTools);
        logger.log("debug : " + stagingGenerateTokenApiResponse.getBody());
        String responseString = stagingGenerateTokenApiResponse.getBody();
        JSONObject jsonResponse = new JSONObject(responseString);
        String code = jsonResponse.getString("code");
        return code;
    }


    private String vaStagingHitPayment(VAStagingHitPaymentApi.Input bookingData)
    throws InterruptedException, ExecutionException {
        VAStagingHitPaymentApi vaStagingHitPaymentApi = new VAStagingHitPaymentApi(bookingData);
        ApiResponse vaStagingHitPaymentApiResponse = vaStagingHitPaymentApi.hitApi(apiTools);
        logger.log("debug : " + vaStagingHitPaymentApiResponse.getBody());
        String responseString = vaStagingHitPaymentApiResponse.getBody();
        JSONObject jsonResponse = new JSONObject(responseString);
        String code = jsonResponse.getString("code");
        return code;
    }

    private MandiriTransferResponse mandiriTransferPayment(MandiriTransferApi.Input bookingData)
    throws InterruptedException, ExecutionException {
        MandiriTransferApi hittingMandiriTransferApi = new MandiriTransferApi(bookingData);
        ApiResponse hittingMandiriTransferApiResponse = hittingMandiriTransferApi.hitApi(apiTools);
        logger.log("debug : " + hittingMandiriTransferApiResponse.getBody());
        MandiriTransferResponse responsePojo = gson.fromJson(hittingMandiriTransferApiResponse.getBody(),MandiriTransferResponse.class);
        return responsePojo;
    }

    private String BCAVAgetToken(BCAVAGetTokenApi.Input bookingData)
    throws InterruptedException, ExecutionException {
        BCAVAGetTokenApi BCAVAgetTokengApi = new BCAVAGetTokenApi(bookingData);
        ApiResponse BCAVAgetTokengApiApiResponse = BCAVAgetTokengApi.hitApi(apiTools);
        logger.log("debug : " + BCAVAgetTokengApiApiResponse.getBody());
        String responseString = BCAVAgetTokengApiApiResponse.getBody();
        JSONObject jsonResponse = new JSONObject(responseString);
        String token = jsonResponse.getString("access_token");
        return token;
    }

    private void BCAVAHitInquiry(BCAVAInquiryApi.Input bookingData)
    throws InterruptedException, ExecutionException {
        BCAVAInquiryApi BCAVAHitInquiryApi = new BCAVAInquiryApi(bookingData);
        ApiResponse BCAVAHitInquiryApiResponse = BCAVAHitInquiryApi.hitApi(apiTools);
        logger.log("debug : " + BCAVAHitInquiryApiResponse.getBody());
    }

    private void BCAVAHitNotify(BCAVANotifyApi.Input bookingData)
    throws InterruptedException, ExecutionException {
        BCAVANotifyApi BCAVAHitNotifyApi = new BCAVANotifyApi(bookingData);
        ApiResponse BCAVANotifyApiResponse = BCAVAHitNotifyApi.hitApi(apiTools);
        logger.log("debug : " + BCAVANotifyApiResponse.getBody());
    }
}