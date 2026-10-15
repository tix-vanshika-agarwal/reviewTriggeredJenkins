package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.payment.ConfigUserTickerPOST;
import com.tiket.api.payment.MultiCurrencyConfigGET;
import com.tiket.api.payment.MultiCurrencyConfigPUT;
import com.tiket.api.payment.PaidCheckerApi;
import com.tiket.api.payment.PaymentCorePromoApplyPOST;
import com.tiket.api.payment.SubmitCreditCardApi;
import com.tiket.api.payment.PaymentGroupApi;
import com.tiket.api.payment.SubmitSakukuApi;
import com.tiket.api.payment.*;
import com.tiket.api.paymentDweb.BCAVAGetTokenApi;
import com.tiket.api.order.InternalOrderDetailApi;
import com.tiket.api.paymentDweb.BCAVAInquiryApi;
import com.tiket.api.paymentDweb.BCAVANotifyApi;
import com.tiket.api.paymentDweb.VAStagingGenerateTokenApi;
import com.tiket.api.paymentDweb.VAStagingHitPaymentApi;
import com.tiket.api.paymentDweb.BCAVASnapAccessTokenApi;
import com.tiket.api.paymentDweb.BCAVASnapInquiryApi;
import com.tiket.api.paymentDweb.BCAVASnapPaymentApi;
import com.tiket.api.paymentDweb.BCAVAInternalInquiryApi;
import com.tiket.api.paymentDweb.BCAVAInternalPaymentApi;
import com.tiket.api.paymentDweb.MandiriVASnapAccessTokenApi;
import com.tiket.api.paymentDweb.MandiriVASnapInquiryApi;
import com.tiket.api.paymentDweb.MandiriVASnapPaymentApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.order.GeneralVerticalInternalOrderDetail;
import com.tiket.pojo.payment.*;
import com.tiket.pojo.paymentDweb.BCAVASnapAccessTokenResponse;
import com.tiket.pojo.paymentDweb.BCAVASnapInquiryResponse;
import com.tiket.pojo.paymentDweb.BCAVASnapPaymentResponse;
import com.tiket.pojo.paymentDweb.MandiriVASnapAccessTokenResponse;
import com.tiket.pojo.paymentDweb.MandiriVASnapInquiryResponse;
import com.tiket.pojo.paymentDweb.MandiriVASnapPaymentResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.pojo.paymentDweb.OrderDetailResponse;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import lombok.Getter;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class PaymentApi<T extends WebDriver> extends BasePage<T> {

    private static final int INSTALLMENT_DETAIL_MAX_ATTEMPTS = 5;
    public double grandTotal;

    public PaymentApi(Session<T> session) { super(session); }

    public PaymentCorePromoApplyResponse applyPromoGv(String accessToken, String orderId, String orderHash, String discountCode, String currency, String paymentType, String productType, Long totalPrice) {
        return applyPromoGv(accessToken, orderId, orderHash, discountCode, currency, paymentType, productType, totalPrice, null, null, null, null);
    }

    public PaymentCorePromoApplyResponse applyPromoGv(String accessToken, String orderId, String orderHash, String discountCode, String currency, String paymentType, String productType, Long totalPrice, String deviceId) {
        return applyPromoGv(accessToken, orderId, orderHash, discountCode, currency, paymentType, productType, totalPrice, null, null, null, deviceId);
    }

    public PaymentCorePromoApplyResponse applyPromoGv(String accessToken, String orderId, String orderHash, String discountCode, String currency, String paymentType, String productType, Long totalPrice, String cardNumber, String cardNumberToken, String binNumber) {
        return applyPromoGv(accessToken, orderId, orderHash, discountCode, currency, paymentType, productType, totalPrice, cardNumber, cardNumberToken, binNumber, null);
    }

    public PaymentCorePromoApplyResponse applyPromoGv(String accessToken, String orderId, String orderHash, String discountCode, String currency, String paymentType, String productType, Long totalPrice, String cardNumber, String cardNumberToken, String binNumber, String deviceId) {
        if (currency.isEmpty()) { currency = "IDR"; }
        if (paymentType.isEmpty()) { paymentType = "SAKUKU"; }
        if (productType.isEmpty()) { productType = "TIXHOTEL"; }

        PaymentCorePromoApplyPOST.Inputs inputs = new PaymentCorePromoApplyPOST.Inputs(accessToken, orderId, orderHash, discountCode, currency, paymentType, productType, totalPrice, cardNumber, cardNumberToken, binNumber, deviceId);
        PaymentCorePromoApplyPOST paymentCorePromoApplyPOST = new PaymentCorePromoApplyPOST(inputs);
        ApiResponse apiResponse = paymentCorePromoApplyPOST.hitApi(apiTools);
        CoreFunctions.log("Response apply promo/gv -> "+apiResponse.getBody());
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), PaymentCorePromoApplyResponse.class);
    }

    /**
     * Hits the payment page landing endpoint and returns the order's grand total before any promo is applied,
     * read from {@code data.sidebarPayment.default.grandTotal}.
     */
    public long getPriceBeforePromo(String accessToken, String orderId, String orderHash) {
        ApiResponse apiResponse = new LandingPageApiV2(new LandingPageApiV2.Input(accessToken, orderId, orderHash)).hitApi(apiTools);
        CoreFunctions.log("Response payment landing -> " + apiResponse.getBody());
        return Math.round(new JSONObject(apiResponse.getBody())
                .getJSONObject("data").getJSONObject("sidebarPayment").getJSONObject("default").getDouble("grandTotal"));
    }

    /**
     * Hits the payment page's promo suggestion endpoint and returns the first promo that cuts the price
     * when paid with Sakuku: an eligible promo code (not a gift card) that gives a plain discount, no cashback.
     * Returns null when nothing suitable is suggested.
     */
    public PaymentCorePromoSuggestionResponse.Promo choosePromoFromSuggestion(String accessToken, String orderId, String orderHash, String currency, String productType, long totalPrice) {
        PaymentCorePromoSuggestionPOST.Inputs inputs = new PaymentCorePromoSuggestionPOST.Inputs(accessToken, orderId, orderHash, currency, productType, totalPrice);
        ApiResponse apiResponse = new PaymentCorePromoSuggestionPOST(inputs).hitApi(apiTools);
        CoreFunctions.log("Response promo suggestion -> " + apiResponse.getBody());

        PaymentCorePromoSuggestionResponse response = new Gson().fromJson(apiResponse.getBody(), PaymentCorePromoSuggestionResponse.class);
        if (response == null || response.data == null) return null;
        return response.data.stream()
                .filter(promo -> promo.eligible && "PROMO_CODE".equals(promo.source))
                .filter(promo -> promo.amount > 0 && promo.cashback == 0)
                .filter(PaymentCorePromoSuggestionResponse.Promo::isPayableWithSakuku)
                .findFirst()
                .orElse(null);
    }

    /**
     * This method is used to hit the BCA VA API for payment processing.
     * It retrieves a token, performs an inquiry, and sends a notification.
     *
     * @param requestID The request ID for the transaction.
     * @param orderId   The order ID associated with the transaction.
     */
    @SneakyThrows
    public void hitBCAVAApi(
            String requestID,
            String orderId,
            String channelType
    ){
        requestID = requestID.replaceAll("\\s+", "");
        logger.log("requestID-->" + requestID);

        String customerCode = requestID.substring(0,5);
        String customerNumber = requestID.substring(5);
        String transactionDate = CoreFunctions.getCurrentDateTime("dd/MM/yyyy HH:mm:ss");

        BCAVAGetTokenApi.Input BCAVAGetTokenApi = new BCAVAGetTokenApi.Input();
        String token = BCAVAgetToken(BCAVAGetTokenApi);
        logger.log("token-->" + token);

        logger.log("Hitting the inquiry API");
        BCAVAInquiryApi.Input BCAVAInquiryApi = new BCAVAInquiryApi.Input(customerCode,customerNumber,requestID,channelType,transactionDate,"",token);
        BCAHitInquiryResponse bcaHitInquiryResponse = bcaVAHitInquiry(BCAVAInquiryApi,orderId);

        logger.log("Hitting the notify API");
        BCAVANotifyApi.Input BCAVANotifyApi = new BCAVANotifyApi.Input(customerCode, customerNumber, requestID, channelType, transactionDate, token, bcaHitInquiryResponse.CustomerName, bcaHitInquiryResponse.CurrencyCode ,bcaHitInquiryResponse.TotalAmount , bcaHitInquiryResponse.TotalAmount);
        BCAVAHitNotify(BCAVANotifyApi,orderId);
    }

    @SneakyThrows
    public String getBCAVASnapAccessToken() {
        BCAVASnapAccessTokenApi api = new BCAVASnapAccessTokenApi(new BCAVASnapAccessTokenApi.Input());
        ApiResponse response = api.hitApi(apiTools);

        Gson gson = new Gson();
        BCAVASnapAccessTokenResponse tokenResponse = gson.fromJson(response.getBody(), BCAVASnapAccessTokenResponse.class);
        if (tokenResponse.accessToken == null || tokenResponse.accessToken.isBlank()) {
            throw new RuntimeException("Failed to retrieve BCA Snap access token. Response: " + response.getBody());
        }

        return tokenResponse.accessToken;
    }

    @SneakyThrows
    public BCAVASnapInquiryResponse hitBCAVASnapInquiry(String accessToken, String orderId) {
        BCAVASnapInquiryApi api = new BCAVASnapInquiryApi(new BCAVASnapInquiryApi.Input(accessToken, orderId));
        ApiResponse response = api.hitApi(apiTools);
        logger.log("BCA Snap inquiry response: " + response.getBody());
        Gson gson = new Gson();
        BCAVASnapInquiryResponse inquiryResponse = gson.fromJson(response.getBody(), BCAVASnapInquiryResponse.class);
        if (inquiryResponse.responseCode == null || !"2002400".equals(inquiryResponse.responseCode)) {
            throw new RuntimeException("BCA Snap inquiry failed. Response: " + response.getBody());
        }
        logger.log("BCA Snap inquiry successful for orderId: " + orderId);
        return inquiryResponse;
    }

    @SneakyThrows
    public BCAVASnapPaymentResponse hitBCAVASnapPayment(String accessToken, String orderId, long paymentAmount,
                                                        BCAVASnapInquiryResponse inquiryResponse) {
        String virtualAccountName = inquiryResponse.virtualAccountData != null
                && inquiryResponse.virtualAccountData.virtualAccountName != null
                ? inquiryResponse.virtualAccountData.virtualAccountName
                : "tiket.com Customer";
        String subCompany = inquiryResponse.virtualAccountData != null
                && inquiryResponse.virtualAccountData.subCompany != null
                ? inquiryResponse.virtualAccountData.subCompany
                : "00000";

        BCAVASnapPaymentApi api = new BCAVASnapPaymentApi(
                new BCAVASnapPaymentApi.Input(accessToken, orderId, paymentAmount, virtualAccountName, subCompany));
        ApiResponse response = api.hitApi(apiTools);
        logger.log("BCA Snap payment response: " + response.getBody());
        Gson gson = new Gson();
        BCAVASnapPaymentResponse paymentResponse = gson.fromJson(response.getBody(), BCAVASnapPaymentResponse.class);
        if (paymentResponse.responseCode == null
                || paymentResponse.responseMessage == null
                || !paymentResponse.responseMessage.toLowerCase().contains("success")) {
            throw new RuntimeException("BCA Snap payment failed. Response: " + response.getBody());
        }
        logger.log("BCA Snap payment successful for orderId: " + orderId);
        return paymentResponse;
    }

  @SneakyThrows
  public String getMandiriVASnapAccessToken() {
    MandiriVASnapAccessTokenApi api = new MandiriVASnapAccessTokenApi(new MandiriVASnapAccessTokenApi.Input());
    ApiResponse response = api.hitApi(apiTools);

    Gson gson = new Gson();
    MandiriVASnapAccessTokenResponse tokenResponse = gson.fromJson(response.getBody(), MandiriVASnapAccessTokenResponse.class);
    if (tokenResponse.accessToken == null || tokenResponse.accessToken.isBlank()) {
      throw new RuntimeException("Failed to retrieve Mandiri Snap access token. Response: " + response.getBody());
    }

    return tokenResponse.accessToken;
  }

    private String BCAVAgetToken(BCAVAGetTokenApi.Input bookingData) throws InterruptedException, ExecutionException {
        BCAVAGetTokenApi BCAVAgentTokenApi = new BCAVAGetTokenApi(bookingData);
        ApiResponse BCAVAgetTokengApiApiResponse = BCAVAgentTokenApi.hitApi(apiTools);
        logger.log("debug : " + BCAVAgetTokengApiApiResponse.getBody());
        String responseString = BCAVAgetTokengApiApiResponse.getBody();
        JSONObject jsonResponse = new JSONObject(responseString);
        String token = jsonResponse.getString("access_token");
        return token;
    }

    private BCAHitInquiryResponse  bcaVAHitInquiry(BCAVAInquiryApi.Input bookingData,String orderId) throws InterruptedException, ExecutionException {
        BCAVAInquiryApi BCAVAHitInquiryApi = new BCAVAInquiryApi(bookingData);
        ApiResponse bcaVAHitInquiryApiResponse = BCAVAHitInquiryApi.hitApi(apiTools);
        JSONObject jsonResponse = new JSONObject(bcaVAHitInquiryApiResponse.getBody());
        logger.log("debug : " + jsonResponse.toString(2));
        Gson gson = new Gson();
        BCAHitInquiryResponse bcaHitInquiryApiResponse = gson.fromJson(bcaVAHitInquiryApiResponse.getBody(), BCAHitInquiryResponse.class);
        if(bcaHitInquiryApiResponse.InquiryReason.English.contains("Success")){
            logger.log("payment process in progress");
            logger.log("check order id");
            for(BCAHitInquiryResponse.FreeText freeText : bcaHitInquiryApiResponse.FreeTexts) {
                if(freeText.English.contains(orderId)) {
                    logger.log("Order ID: " + freeText.English);
                }
            }
            return bcaHitInquiryApiResponse;
        }else {
            logger.log("payment process failed");
            logger.log("Response body: " + bcaVAHitInquiryApiResponse.getBody());
            throw new RuntimeException("Payment process failed, please check the orderId or other parameters.");
        }
    }

    private void BCAVAHitNotify(BCAVANotifyApi.Input bookingData,String orderId) throws InterruptedException, ExecutionException {
        BCAVANotifyApi bcaVAHitNotifyApi = new BCAVANotifyApi(bookingData);
        ApiResponse bcaVANotifyApiResponse = bcaVAHitNotifyApi.hitApi(apiTools);
        Gson gson = new Gson();
        BCAVANotifyApiResponse bcavaNotifyApiResponse = gson.fromJson(bcaVANotifyApiResponse.getBody(), BCAVANotifyApiResponse.class);
        if(bcavaNotifyApiResponse.PaymentFlagReason.English.contains("Success")){
            logger.log("Payment notification successful");
            logger.log("check order id");
            for(BCAVANotifyApiResponse.FreeText freeText : bcavaNotifyApiResponse.FreeTexts) {
                if(freeText.English.contains(orderId)) {
                    logger.log("Order ID: " + freeText.English);
                }
            }
        } else {
            logger.log("Payment notification failed");
            logger.log("Response body: " + bcaVANotifyApiResponse.getBody());
            throw new RuntimeException("Payment notification failed, please check the parameters.");
        }
    }

    @SneakyThrows
    public double waitForPaymentSuccessful(String orderId, int triesMax) {
        String status = "";
        BigDecimal TotalPrice = BigDecimal.ZERO;
        for(int tries=0;tries<triesMax;tries++){
            JSONObject response = getInternalOrderData(orderId);
            GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
            status = generalVerticalInternalOrderDetail.data.orderStatus;
            TotalPrice = generalVerticalInternalOrderDetail.data.price;
            logger.log("Total Price: " + TotalPrice);

            logger.log("Order status: "+status);

            if(status.equalsIgnoreCase("ETICKET_ISSUED") || status.equalsIgnoreCase("PROCESSING_ETICKET")){
                break;
            }
            Thread.sleep(5000);
        }
        return TotalPrice.doubleValue();
    }

    @SneakyThrows
    public double getTotalPriceViaApi(String orderId) {
        String status = "";
        BigDecimal totalPrice = BigDecimal.ZERO;
        JSONObject response = getInternalOrderData(orderId);
        GeneralVerticalInternalOrderDetail generalVerticalInternalOrderDetail = getVerticalInternalOrderDtata(response);
        status = generalVerticalInternalOrderDetail.data.orderStatus;
        totalPrice = generalVerticalInternalOrderDetail.data.price;
        logger.log("Total Price: " + totalPrice);
        logger.log("Order status: " + status);
        return totalPrice.doubleValue();
    }

    @SneakyThrows
    public JSONObject getInternalOrderData(String orderId){
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        JSONObject response = orderDetailPage.getInternalOrderDetail(orderId);
        return response;
    }


    public OrderDetailResponse getOrderDetails(String orderId) {
        InternalOrderDetailApi api = new InternalOrderDetailApi(new InternalOrderDetailApi.Inputs(orderId));
        ApiResponse response = api.hitApi(apiTools);
        logger.log("Order detail response received for orderId: " + orderId);
        return new Gson().fromJson(response.getBody(), OrderDetailResponse.class);
    }
    public GeneralVerticalInternalOrderDetail getVerticalInternalOrderDtata(JSONObject responseInternalOrder){
        Gson gson = new Gson();
        return gson.fromJson(responseInternalOrder.toString(),GeneralVerticalInternalOrderDetail.class);
    }

    /**
     * This method submits a Sakuku payment using the provided access token, order ID, and order hash.
     * It returns the reference ID of the payment if successful.
     *
     * @param accessToken The access token for authentication.
     * @param orderId The ID of the order to be paid.
     * @param orderHash The hash of the order.
     * @return The reference ID of the submitted Sakuku payment.
     */
    @SneakyThrows
    public String submitSakukuPayment(String accessToken, long orderId, String orderHash) {
        return submitSakukuPayment(accessToken, orderId, orderHash, false);
    }

    /** {@code viaGateway} pays the way the payment page does, which an order with an applied promo needs. */
    @SneakyThrows
    public String submitSakukuPayment(String accessToken, long orderId, String orderHash, boolean viaGateway) {
        SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, orderId, viaGateway);
        SubmitSakukuResponse submitSakukuResponse = null;

        final int MAX_TRIES = 5;
        boolean isSuccess = false;
        for (int i = 0; i < MAX_TRIES; i++) {
            submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (submitSakukuResponse.message.equals("SUCCESS")) {
                isSuccess = true;
                break;
            }
        }

        if (!isSuccess) Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
        double grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.doubleValue();

        logger.log("Grand Total -> "+grandTotal);
        this.grandTotal = grandTotal;
        logger.log("Reference Id -> "+submitSakukuResponse.data.sidebarPayment.referenceId);

        return submitSakukuResponse.data.sidebarPayment.referenceId;
    }

    private SubmitSakukuResponse submitSakuku(SubmitSakukuApi.Inputs inputs) throws InterruptedException, ExecutionException {
        SubmitSakukuApi submitSakukuApi = new SubmitSakukuApi(inputs);
        ApiResponse apiResponse = submitSakukuApi.hitApi(apiTools);
        Gson gson = new Gson();
        logger.log("response submit Sakuku : " + apiResponse.getBody());
        SubmitSakukuResponse submitSakukuResponse = gson.fromJson(apiResponse.getBody(), SubmitSakukuResponse.class);
        return submitSakukuResponse;
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

    public Map<String, Boolean> getPaymentMethodStatuses(String orderHash, String referenceId, String accessToken) {
        return getPaymentMethodStatuses(orderHash, referenceId, accessToken, "IDR", "sg");
    }

    public Map<String, Boolean> getPaymentMethodStatuses(String orderHash, String referenceId, String accessToken, String currency, String countryCode) {
        PaymentGroupApi.Inputs inputs = new PaymentGroupApi.Inputs(
                orderHash, referenceId, "DESKTOP", accessToken, currency, countryCode);
        ApiResponse apiResponse = new PaymentGroupApi(inputs).hitApi(apiTools);
        logger.log("Response payment-group -> " + apiResponse.getBody());
        PaymentGroupResponse response = new Gson().fromJson(apiResponse.getBody(), PaymentGroupResponse.class);
        Map<String, Boolean> statuses = new HashMap<>();
        if (response.data != null && response.data.commonGroup != null) {
            for (PaymentGroupResponse.PaymentGroup group : response.data.commonGroup) {
                if (group.items != null) {
                    for (PaymentGroupResponse.PaymentMethodItem item : group.items) {
                        statuses.putIfAbsent(item.paymentMethod, item.status);
                    }
                }
            }
        }
        logger.log("All payment method statuses -> " + statuses);
        return statuses;
    }

    public String updateCurrencyMessage(List<String> messageEn, List<String> messageId) {
        ConfigUserTickerPOST.Inputs inputs = new ConfigUserTickerPOST.Inputs(messageEn, messageId);
        ConfigUserTickerPOST configUserTickerPOST = new ConfigUserTickerPOST(inputs);
        ApiResponse apiResponse = configUserTickerPOST.hitApi(apiTools);
        int status = apiResponse.getStatusCode();
        logger.log("Response update currency message -> ["+status+"] "+apiResponse.getBody());
        if (status < 200 || status >= 300) {
            throw new RuntimeException("POST currency message failed ["+status+"] -> "+apiResponse.getBody());
        }
        JSONObject responseObj = new JSONObject(apiResponse.getBody());
        return responseObj.getString("code");
    }

    /**
     * Logs in via UNM and reads the payment dashboard transaction list, returning "orderId:tenor" of every row that belongs to
     * the given orders, oldest payment first. Retries until {@code orderIds.size()} rows are found, since the dashboard can lag
     * behind the payment. An order id listed twice expects two rows (one order paid in two installments).
     */
    public List<String> getInstallmentOrderTenorSequence(String username, String password, List<String> orderIds) {
        String accessToken = new Unm<T>(session).getAccessToken(username, password, "B2C");
        String today = CoreFunctions.getCurrentDateYYYY_MM_DD();
        PaymentDashboardTransactionApi transactionApi = new PaymentDashboardTransactionApi(new PaymentDashboardTransactionApi.Inputs(
                accessToken, CoreFunctions.addToDateYYYY_MM_DD(today, -15), CoreFunctions.addToDateYYYY_MM_DD(today, 15)));

        List<JSONObject> matchedRows = new ArrayList<>();
        for (int attempt = 1; attempt <= 10 && matchedRows.size() < orderIds.size(); attempt++) {
            ApiResponse apiResponse = transactionApi.hitApi(apiTools);
            CoreFunctions.log("Payment dashboard transaction API attempt " + attempt + "/10 status -> " + apiResponse.getStatusCode()
                    + " body -> " + apiResponse.getBody());
            matchedRows.clear();
            if (apiResponse.getStatusCode() == 200) {
                JSONArray rows = new JSONObject(apiResponse.getBody()).getJSONObject("data").getJSONObject("table").getJSONArray("content");
                for (int i = 0; i < rows.length(); i++) {
                    if (orderIds.contains(rows.getJSONObject(i).optString("orderId"))) {
                        matchedRows.add(rows.getJSONObject(i));
                    }
                }
            }
            if (matchedRows.size() < orderIds.size()) {
                driverTools.dummyWait(3);
            }
        }
        matchedRows.sort(Comparator.comparingLong(row -> row.optLong("transactionDate")));
        return matchedRows.stream().map(row -> row.optString("orderId") + ":" + row.optString("tenor")).toList();
    }

    public InstallmentDetailResult hitInstallmentDetail(long referenceId, String cardNumber, String accessToken, String orderHash) {
        return hitInstallmentDetail(referenceId, cardNumber, accessToken, orderHash,
                result -> result.statusCode() == 200);
    }

    public InstallmentDetailResult hitInstallmentDetailUntilInactive(long referenceId, String cardNumber,
                                                                     String accessToken, String orderHash) {
        return hitInstallmentDetail(referenceId, cardNumber, accessToken, orderHash,
                this::isInactiveInstallmentDetailReflected);
    }

    public InstallmentDetailResult hitInstallmentDetailUntilActive(long referenceId, String cardNumber,
                                                                   String accessToken, String orderHash) {
        return hitInstallmentDetail(referenceId, cardNumber, accessToken, orderHash,
                this::isActiveInstallmentDetailReflected);
    }

    private InstallmentDetailResult hitInstallmentDetail(long referenceId, String cardNumber, String accessToken,
                                                         String orderHash, Predicate<InstallmentDetailResult> stopWhen) {
        driverTools.dummyWait(30); //wait for 30 secs for changes on UI to be reflected onto backend
        InstallmentDetailApi installmentDetailApi = new InstallmentDetailApi(
                new InstallmentDetailApi.Inputs(referenceId, cardNumber, accessToken, orderHash));
        Gson gson = new Gson();
        InstallmentDetailResult lastResult = null;

        for (int attempt = 1; attempt <= INSTALLMENT_DETAIL_MAX_ATTEMPTS; attempt++) {
            ApiResponse apiResponse = installmentDetailApi.hitApi(apiTools);
            CoreFunctions.log("Installment detail API attempt " + attempt + "/" + INSTALLMENT_DETAIL_MAX_ATTEMPTS
                    + " status -> " + apiResponse.getStatusCode() + " body -> " + apiResponse.getBody());

            InstallmentDetailResponse response = null;
            try {
                response = gson.fromJson(apiResponse.getBody(), InstallmentDetailResponse.class);
            } catch (Exception e) {
                logger.log("Failed to parse installment detail API response on attempt " + attempt + ": "
                        + e.getMessage());
            }

            lastResult = new InstallmentDetailResult(apiResponse.getStatusCode(), response, apiResponse.getBody());
            if (stopWhen.test(lastResult)) {
                return lastResult;
            }

            if (attempt < INSTALLMENT_DETAIL_MAX_ATTEMPTS) {
                logger.log("Installment detail API state not yet reflected (status " + apiResponse.getStatusCode()
                        + "), retrying in 1 second...");
                driverTools.dummyWait(1);
            }
        }

        return lastResult;
    }

    private boolean isInactiveInstallmentDetailReflected(InstallmentDetailResult result) {
        return result.statusCode() == 200
                && result.body() != null
                && result.body().contains("BusinessLogicException");
    }

    private boolean isActiveInstallmentDetailResponseReflected(InstallmentDetailResult result) {
        return result.statusCode() == 200
                && result.response() != null
                && "SUCCESS".equals(result.response().code)
                && result.body() != null
                && !result.body().contains("BusinessLogicException");
    }

    private boolean isActiveInstallmentDetailReflected(InstallmentDetailResult result) {
        if (isActiveInstallmentDetailResponseReflected(result)) {
            return true;
        }
        return result.statusCode() == 200
                && result.response() != null
                && "Promo is inactive Updated".equals(result.response().code);
    }

    public InstallmentDetailResult hitInstallmentDetail(long referenceId, String cardNumber, String accessToken) {
        return hitInstallmentDetail(referenceId, cardNumber, accessToken, "");
    }

    public double sumFinanceAndBankPercent(String finance, String bank) {
        return parsePercentage(finance) + parsePercentage(bank);
    }

    public Map<Integer, Double> buildExpectedChargePercentages(String duration1, String finance1, String bank1,
                                                                String duration2, String finance2, String bank2,
                                                                String duration3, String finance3, String bank3) {
        Map<Integer, Double> expected = new LinkedHashMap<>();
        expected.put(Integer.parseInt(duration1), sumFinanceAndBankPercent(finance1, bank1));
        expected.put(Integer.parseInt(duration2), sumFinanceAndBankPercent(finance2, bank2));
        expected.put(Integer.parseInt(duration3), sumFinanceAndBankPercent(finance3, bank3));
        return expected;
    }

    public void assertInstallmentDetailResponse(InstallmentDetailResult result, String currency,
                                                 Map<Integer, Double> expectedChargePercentageByMonth) {
        assertActiveInstallmentDetailResponse(result, currency, expectedChargePercentageByMonth);
    }

    public void assertInactiveToActiveInstallmentDetailResponse(InstallmentDetailResult result) {
        Assertion.assertThat("Installment detail API status is 200 OK when active", result.statusCode(), is(200));
        Assertion.assertThat("Installment detail API response is parsed", result.response(), notNullValue());
        Assertion.assertThat("Installment detail API code is 'Promo is inactive Updated' when active", result.response().code, is("Promo is inactive Updated"));

    }

    public void assertActiveInstallmentDetailResponse(InstallmentDetailResult result, String currency,
                                                      Map<Integer, Double> expectedChargePercentageByMonth) {
        Assertion.assertThat("Installment detail API status is 200 OK when active", result.statusCode(), is(200));
        Assertion.assertThat("Installment detail API response is parsed", result.response(), notNullValue());
        Assertion.assertThat("Installment detail API code is SUCCESS when active", result.response().code, is("SUCCESS"));
        Assertion.assertThat("Installment detail API does not return business logic error when active",
                result.body(), not(containsString("BusinessLogicException")));

        List<InstallmentDetailResponse.InstallmentOption> options = result.response().data;
        Assertion.assertThat("Installment detail API data is present when active", options, notNullValue());

        InstallmentDetailResponse.InstallmentOption monthOne = findInstallmentOption(options, 1);
        Assertion.assertThat("Installment month 1 is present when active", monthOne, notNullValue());

        for (InstallmentDetailResponse.InstallmentOption option : options) {
            Assertion.assertThat("Currency matches for installment month " + option.installmentMonth,
                    option.currency, is(currency));
            Assertion.assertThat("Enable is true for installment month " + option.installmentMonth,
                    option.enable, is(true));
        }

        for (Map.Entry<Integer, Double> entry : expectedChargePercentageByMonth.entrySet()) {
            int month = entry.getKey();
            double expectedChargePercentage = entry.getValue();
            InstallmentDetailResponse.InstallmentOption option = findInstallmentOption(options, month);
            Assertion.assertThat("Installment month " + month + " is present when active", option, notNullValue());
            Assertion.assertThat("payMonthly is not null for installment month " + month,
                    option.payMonthly, notNullValue());
            Assertion.assertThat("grandTotal is not null for installment month " + month,
                    option.grandTotal, notNullValue());
            Assertion.assertThat("charge is not null for installment month " + month,
                    option.charge, notNullValue());
            Assertion.assertThat("monthlyCharge is not null for installment month " + month,
                    option.monthlyCharge, notNullValue());
            Assertion.assertThat("chargePercentage matches finance + bank for installment month " + month,
                    option.chargePercentage, is(expectedChargePercentage));
        }
    }

    public void assertInactiveInstallmentDetailResponse(InstallmentDetailResult result) {
        Assertion.assertThat("Installment detail API status is 200 OK when inactive", result.statusCode(), is(200));
        Assertion.assertThat("Installment detail API returns business logic error when inactive",
                result.body(), containsString("BusinessLogicException"));
    }

    private InstallmentDetailResponse.InstallmentOption findInstallmentOption(
            List<InstallmentDetailResponse.InstallmentOption> options, int installmentMonth) {
        if (options == null) {
            return null;
        }
        return options.stream()
                .filter(option -> option.installmentMonth == installmentMonth)
                .findFirst()
                .orElse(null);
    }

    private double parsePercentage(String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }
        return Double.parseDouble(value.replace("%", "").trim());
    }

    /**
     * Submits a credit card payment for an order and returns the payment redirect URL (acsUrl)
     * that points to the payment gateway's hosted payment or 3DS page.
     *
     * @param accessToken      Bearer token of the user
     * @param orderHash        Order hash from the booking response
     * @param referenceId      Order ID to be used as reference
     * @param cardNumber       Credit card number
     * @param cardHolderName   Name of the card holder
     * @param cardExpiredMonth Expiry month (e.g. "12")
     * @param cardExpiredYear  Expiry year (e.g. "2027")
     * @param cardCvvNumber    CVV number
     * @return The acsUrl / redirect URL returned by the payment gateway, or the raw response body if unavailable
     */
    @SneakyThrows
    public String submitCreditCardPayment(
            String accessToken,
            String orderHash,
            long referenceId,
            String cardNumber,
            String cardHolderName,
            String cardExpiredMonth,
            String cardExpiredYear,
            String cardCvvNumber
    ) {
        SubmitCreditCardApi.Inputs inputs = new SubmitCreditCardApi.Inputs(
                accessToken, orderHash, referenceId,
                cardNumber, cardHolderName, cardExpiredMonth, cardExpiredYear, cardCvvNumber
        );
        SubmitCreditCardApi submitCreditCardApi = new SubmitCreditCardApi(inputs);
        ApiResponse apiResponse = submitCreditCardApi.hitApi(apiTools);
        logger.log("CC payment response -> " + apiResponse.getBody());
        Gson gson = new Gson();
        SubmitCreditCardResponse response = gson.fromJson(apiResponse.getBody(), SubmitCreditCardResponse.class);
        if (response != null
                && response.data != null
                && response.data.thirdPartyResponse != null
                && response.data.thirdPartyResponse.data != null
                && response.data.thirdPartyResponse.data.acsUrl != null) {
            logger.log("acsUrl (payment gateway redirect) -> " + response.data.thirdPartyResponse.data.acsUrl);
            return response.data.thirdPartyResponse.data.acsUrl;
        }
        logger.log("acsUrl not found in response, returning raw body");
        return apiResponse.getBody();
    }

    /**
     * Calls the Currency Configuration Client API and returns the configuration of a single currency
     * (identified by its currency code), or throws if the currency is not present in the response.
     */
    public MultiCurrencyConfigResponse.Currency getCurrencyConfig(String currencyCode) {
        MultiCurrencyConfigGET.Inputs inputs = new MultiCurrencyConfigGET.Inputs(currencyCode);
        MultiCurrencyConfigGET api = new MultiCurrencyConfigGET(inputs);
        ApiResponse apiResponse = api.hitApi(apiTools);
        logger.log("Response multi-currency config -> "+apiResponse.getBody());
        MultiCurrencyConfigResponse response = new Gson().fromJson(apiResponse.getBody(), MultiCurrencyConfigResponse.class);
        if (response == null || response.data == null || response.data.content == null) {
            throw new RuntimeException("Currency configuration response is empty for code: "+currencyCode);
        }
        return response.data.content.stream()
                .filter(currency -> currency.code != null && currency.code.equalsIgnoreCase(currencyCode))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Currency "+currencyCode+" not found in configuration response"));
    }

    /** Vertical currency status from the client API as a UI label: ACTIVE (1), INACTIVE (0), REVIEW (-1). */
    public String getVerticalCurrencyStatusViaApi(String currencyCode) {
        int verticalIsActive = getCurrencyConfig(currencyCode).verticalIsActive;
        return switch (verticalIsActive) {
            case 1 -> "ACTIVE";
            case 0 -> "INACTIVE";
            case -1 -> "REVIEW";
            default -> String.valueOf(verticalIsActive);
        };
    }

    public boolean isUserCurrencyActiveViaApi(String currencyCode) {
        return getCurrencyConfig(currencyCode).userIsActive;
    }

    public boolean isConsumerVisibleViaApi(String currencyCode) {
        return getCurrencyConfig(currencyCode).consumerVisible;
    }

    public boolean isSuggestedViaApi(String currencyCode) {
        return getCurrencyConfig(currencyCode).isSuggested;
    }

    /**
     * Polls the client API until the vertical currency status matches {@code expected} (a UI label) or the timeout
     * elapses, then returns the last observed value. The dashboard PUT and the GET-back can lag the UI spinner by a
     * moment, so an immediate single read after a UI change is flaky; this absorbs that propagation delay.
     */
    public String waitForVerticalCurrencyStatusViaApi(String currencyCode, String expected) {
        return pollViaApi(() -> getVerticalCurrencyStatusViaApi(currencyCode), expected.trim().toUpperCase());
    }

    public boolean waitForUserCurrencyActiveViaApi(String currencyCode, boolean expected) {
        return pollViaApi(() -> isUserCurrencyActiveViaApi(currencyCode), expected);
    }

    public boolean waitForConsumerVisibleViaApi(String currencyCode, boolean expected) {
        return pollViaApi(() -> isConsumerVisibleViaApi(currencyCode), expected);
    }

    public boolean waitForSuggestedViaApi(String currencyCode, boolean expected) {
        return pollViaApi(() -> isSuggestedViaApi(currencyCode), expected);
    }

    /** Polls {@code supplier} up to ~10s (20 x 500ms) until it equals {@code expected}; returns the last value read. */
    private <V> V pollViaApi(java.util.function.Supplier<V> supplier, V expected) {
        V value = supplier.get();
        for (int attempt = 0; attempt < 20 && !expected.equals(value); attempt++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            value = supplier.get();
        }
        return value;
    }

    private void putCurrencyConfig(String jsonBody) {
        MultiCurrencyConfigPUT api = new MultiCurrencyConfigPUT(new MultiCurrencyConfigPUT.Inputs(jsonBody));
        ApiResponse apiResponse = api.hitApi(apiTools);
        int status = apiResponse.getStatusCode();
        logger.log("Response PUT multi-currency config "+jsonBody+" -> ["+status+"] "+apiResponse.getBody());
        // Fail fast on a non-2xx so a silently-rejected update (during a config change or a cleanup restore) does not
        // go unnoticed and leave the shared environment in an unexpected state.
        if (status < 200 || status >= 300) {
            throw new RuntimeException("PUT multi-currency config failed ["+status+"] for body "+jsonBody+" -> "+apiResponse.getBody());
        }
    }

    /**
     * Sets the vertical currency status via API. Accepts a UI label (ACTIVE/INACTIVE/REVIEW). The backend enforces the
     * same transition rules as the panel: ACTIVE and INACTIVE are not directly interchangeable and must pass through
     * REVIEW, so the jump is routed automatically.
     */
    public void setVerticalCurrencyStatusViaApi(String currencyCode, String status) {
        String target = status.trim().toUpperCase();
        String current = getVerticalCurrencyStatusViaApi(currencyCode);
        if (current.equals(target)) {
            return;
        }
        if ((current.equals("ACTIVE") && target.equals("INACTIVE"))
                || (current.equals("INACTIVE") && target.equals("ACTIVE"))) {
            putVerticalCurrencyStatus(currencyCode, "REVIEW");
            // The backend only allows adjacent transitions, so let the intermediate REVIEW state actually land
            // (PUT/GET propagation lags) before the final hop - a back-to-back PUT can be rejected or read stale.
            waitForVerticalCurrencyStatusViaApi(currencyCode, "REVIEW");
        }
        putVerticalCurrencyStatus(currencyCode, target);
    }

    private void putVerticalCurrencyStatus(String currencyCode, String status) {
        String id = getCurrencyConfig(currencyCode).id;
        String value = switch (status.trim().toUpperCase()) {
            case "ACTIVE" -> "1";
            case "INACTIVE" -> "0";
            case "REVIEW" -> "-1";
            default -> status;
        };
        putCurrencyConfig("{\"id\":\""+id+"\",\"verticalIsActive\":\""+value+"\"}");
    }

    public void setUserCurrencyViaApi(String currencyCode, boolean active) {
        String id = getCurrencyConfig(currencyCode).id;
        putCurrencyConfig("{\"id\":\""+id+"\",\"userIsActive\":"+active+"}");
    }

    public void setConsumerVisibleViaApi(String currencyCode, boolean visible) {
        String id = getCurrencyConfig(currencyCode).id;
        putCurrencyConfig("{\"id\":\""+id+"\",\"consumerVisible\":"+visible+"}");
    }

    public void setSuggestedViaApi(String currencyCode, boolean suggested) {
        String id = getCurrencyConfig(currencyCode).id;
        putCurrencyConfig("{\"helperCode\":\""+currencyCode+"\",\"id\":\""+id+"\",\"isSuggested\":"+suggested+"}");
    }

    /** Returns the codes of all currencies currently flagged as suggested in the client configuration. */
    public List<String> getSuggestedCurrenciesViaApi() {
        MultiCurrencyConfigGET.Inputs inputs = new MultiCurrencyConfigGET.Inputs("");
        MultiCurrencyConfigGET api = new MultiCurrencyConfigGET(inputs);
        ApiResponse apiResponse = api.hitApi(apiTools);
        int status = apiResponse.getStatusCode();
        MultiCurrencyConfigResponse response = new Gson().fromJson(apiResponse.getBody(), MultiCurrencyConfigResponse.class);
        // Distinguish "no suggested currencies" from a failed/malformed response: returning an empty list on an error
        // would make the cleanup restore skip unsetting flags that the test added, polluting shared preprod.
        if (status < 200 || status >= 300 || response == null || response.data == null || response.data.content == null) {
            throw new RuntimeException("GET suggested currencies failed/malformed ["+status+"] -> "+apiResponse.getBody());
        }
        List<String> codes = new ArrayList<>();
        for (MultiCurrencyConfigResponse.Currency currency : response.data.content) {
            if (currency.isSuggested && currency.code != null) {
                codes.add(currency.code.toUpperCase());
            }
        }
        logger.log("Suggested currencies via API -> "+codes);
        return codes;
    }


    public BCAVASnapInquiryResponse hitBCAVAInternalInquiry(String accessToken, String customerNo, String virtualAccountNo, String orderId) {
        BCAVAInternalInquiryApi api = new BCAVAInternalInquiryApi(
                new BCAVAInternalInquiryApi.Input(accessToken, customerNo, virtualAccountNo, orderId));
        ApiResponse response = api.hitApi(apiTools);
        logger.log("BCA VA internal inquiry response: " + response.getBody());
        BCAVASnapInquiryResponse inquiry = new Gson().fromJson(response.getBody(), BCAVASnapInquiryResponse.class);
        if (inquiry.responseCode == null || !"2002400".equals(inquiry.responseCode)) {
            throw new RuntimeException("BCA VA internal inquiry failed: " + response.getBody());
        }
        return inquiry;
    }

    public BCAVASnapPaymentResponse hitBCAVAInternalPayment(String accessToken, String customerNo,
            String virtualAccountNo, long amount, BCAVASnapInquiryResponse inquiry, String orderId) {
        String virtualAccountName = inquiry.virtualAccountData != null
                && inquiry.virtualAccountData.virtualAccountName != null
                ? inquiry.virtualAccountData.virtualAccountName : "tiket.com Customer";
        String subCompany = inquiry.virtualAccountData != null
                && inquiry.virtualAccountData.subCompany != null
                ? inquiry.virtualAccountData.subCompany : "00000";
        BCAVAInternalPaymentApi api = new BCAVAInternalPaymentApi(
                new BCAVAInternalPaymentApi.Input(accessToken, customerNo, virtualAccountNo, amount, virtualAccountName, subCompany, orderId));

        ApiResponse response = api.hitApi(apiTools);
        logger.log("BCA VA internal payment response: " + response.getBody());
        BCAVASnapPaymentResponse payment = new Gson().fromJson(response.getBody(), BCAVASnapPaymentResponse.class);
        if (payment.responseCode == null || payment.responseMessage == null
                || !payment.responseMessage.toLowerCase().contains("success")) {
            throw new RuntimeException("BCA VA internal payment failed: " + response.getBody());
        }
        return payment;
    }

    @SneakyThrows
    public BCAVASnapPaymentResponse hitBCAVASnapPaymentFromOrderDetails(String orderId) {
        OrderDetailResponse orderDetail = getOrderDetails(orderId);
        if (orderDetail.data == null || orderDetail.data.contact == null
                || orderDetail.data.contact.getCustomerNo() == null
                || orderDetail.data.getVirtualAccountNo() == null) {
            throw new RuntimeException("Order detail missing required BCA VA fields for orderId: " + orderId);
        }
        String customerNo = orderDetail.data.contact.getCustomerNo();
        String virtualAccountNo = orderDetail.data.getVirtualAccountNo();
        long totalAmount = orderDetail.data.getTotalAmountAsLong();
        logger.log("BCA VA customerNo: " + customerNo + ", virtualAccountNo: " + virtualAccountNo + ", totalAmount: " + totalAmount);

        String accessToken = getBCAVASnapAccessToken();
        BCAVASnapInquiryResponse inquiry = hitBCAVAInternalInquiry(accessToken, customerNo, virtualAccountNo,orderId);
        return hitBCAVAInternalPayment(accessToken, customerNo, virtualAccountNo, totalAmount, inquiry, orderId);
    }

  public MandiriVASnapInquiryResponse hitMandiriVAInternalInquiry(String accessToken, String customerNo, String virtualAccountNo, String orderId) {
    MandiriVASnapInquiryApi api = new MandiriVASnapInquiryApi(
      new MandiriVASnapInquiryApi.Input(accessToken, customerNo, virtualAccountNo, orderId));
    ApiResponse response = api.hitApi(apiTools);
    logger.log("Mandiri VA internal inquiry response: " + response.getBody());
    MandiriVASnapInquiryResponse inquiry = new Gson().fromJson(response.getBody(), MandiriVASnapInquiryResponse.class);
    if (inquiry.responseCode == null || !"2002400".equals(inquiry.responseCode)) {
      throw new RuntimeException("Mandiri VA internal inquiry failed: " + response.getBody());
    }
    return inquiry;
  }

  public boolean hitNicepayBriSnapVaSimulator(String bankCd, String orderId, long totalAmount, String vacctNo) {
    String amount = String.valueOf(totalAmount);

    VAStagingGenerateTokenApi tokenApi = new VAStagingGenerateTokenApi(new VAStagingGenerateTokenApi.Input(bankCd, amount));
    ApiResponse tokenResponse = tokenApi.hitApi(apiTools);
    logger.log("Nicepay VA generate token response: " + tokenResponse.getBody());
    String token = new org.json.JSONObject(tokenResponse.getBody()).getString("code");

    String paymentId = UUID.randomUUID().toString();
    VAStagingHitPaymentApi paymentNotifApi = new VAStagingHitPaymentApi(new VAStagingHitPaymentApi.Input(bankCd, amount, orderId, token, vacctNo, paymentId));
    ApiResponse notifResponse = paymentNotifApi.hitApi(apiTools);
    logger.log("Nicepay VA payment notification response: " + notifResponse.getBody());
    String code = new org.json.JSONObject(notifResponse.getBody()).getString("code");
    return code.equals("SUCCESS") || code.equals("06");
  }

  public MandiriVASnapPaymentResponse hitMandiriVAInternalPayment(String accessToken, String customerNo,
    String virtualAccountNo, long amount, MandiriVASnapInquiryResponse inquiry, String orderId) {
    String virtualAccountName = inquiry.virtualAccountData != null
      && inquiry.virtualAccountData.virtualAccountName != null
      ? inquiry.virtualAccountData.virtualAccountName : "tiket.com Customer";
    String subCompany = inquiry.virtualAccountData != null
      && inquiry.virtualAccountData.subCompany != null
      ? inquiry.virtualAccountData.subCompany : "00000";
    MandiriVASnapPaymentApi api = new MandiriVASnapPaymentApi(
      new MandiriVASnapPaymentApi.Input(accessToken, customerNo, virtualAccountNo, amount, virtualAccountName, subCompany, orderId));

    ApiResponse response = api.hitApi(apiTools);
    logger.log("Mandiri VA internal payment response: " + response.getBody());
    MandiriVASnapPaymentResponse payment = new Gson().fromJson(response.getBody(), MandiriVASnapPaymentResponse.class);
    if (payment.responseCode == null || payment.responseMessage == null
      || !payment.responseMessage.toLowerCase().contains("success")) {
      throw new RuntimeException("Mandiri VA internal payment failed: " + response.getBody());
    }
    return payment;
  }

  public void topUpDanaBalance(String phoneNumber, long amount) {
    try {
      DanaTopUpApi api = new DanaTopUpApi(new DanaTopUpApi.Inputs(phoneNumber, amount));
      ApiResponse response = api.hitApi(apiTools);
      logger.log("DANA top-up response (" + response.getStatusCode() + "): " + response.getBody());
    } catch (Exception e) {
      logger.log("DANA top-up failed: " + e.getMessage());
    }
  }
}
