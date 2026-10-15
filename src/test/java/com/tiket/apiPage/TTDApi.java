package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.payment.SubmitCreditCardApi;
import com.tiket.api.payment.SubmitPaymentApiB2B;
import com.tiket.api.payment.SubmitPaymentPutApiB2B;
import com.tiket.api.ttd.*;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.corporate.login.CorporateLoginPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.pojo.GeneralResponse;
import com.tiket.pojo.b2b.corporate.EmployeeData;
import com.tiket.pojo.payment.SubmitCreditCardResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.is;

public class TTDApi<T extends WebDriver> extends BasePage<T> {

    private String productName;
    private String packageName;
    private long orderId;

    public TTDApi(Session<T> session) {
        super(session);
    }

    public String getProductName() {
        return productName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String publishVoucherSupplier(String orderId, String supplierOrderId) {
        POSTKlookVoucherApi.Inputs inputs = new POSTKlookVoucherApi.Inputs(orderId, supplierOrderId);
        POSTKlookVoucherApi postKlookVoucherApi = new POSTKlookVoucherApi(inputs);
        ApiResponse response = postKlookVoucherApi.hitApi(apiTools);
        logger.log("Response body: " + response.getBody());
        Assertion.assertThat("Response code is 200", response.getStatusCode(), is(200));

        Gson gson = new Gson();
        GeneralResponse responseBody = gson.fromJson(response.getBody(), GeneralResponse.class);
        return responseBody.message;
    }

    public ApiResponse validateCampaignDescriptionInB2C(String productUrl) {
        TTDProductUrlGETApi.Inputs inputs = new TTDProductUrlGETApi.Inputs(
                productUrl,
                "*/*",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36",
                null
        );

        TTDProductUrlGETApi api = new TTDProductUrlGETApi(inputs);
        ApiResponse response = api.hitApi(apiTools);

        logger.log("TTDProductUrl API Response Status: " + response.getStatusCode());
        logger.log("TTDProductUrl API Response Body: " + response.getBody());

        Assertion.assertThat("Response code should be 200", response.getStatusCode(), is(200));

        return response;
    }

    public Map<String, String> createWaitingPaymentOrder(
            String username,
            String password,
            String currency,
            String productUrlName,
            boolean refundable,
            String contactFullName,
            String contactSalutation,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation
    ) throws ExecutionException, InterruptedException {
        TtdCheckoutResult result = prepareTtdOrder(username, password, currency, productUrlName, refundable,
                contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, false);
        Map<String, String> orderData = new HashMap<>();
        orderData.put("orderId", String.valueOf(result.orderId));
        orderData.put("orderHash", result.orderHash);
        return orderData;
    }

    @SneakyThrows
    public String createIssuedOrderApi(
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
        TtdCheckoutResult checkoutResult = prepareTtdOrder(
                username, password, currency, productUrlName, refundable,
                contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation);

        String[] expirySplit = expiry.split("/");
        String cardExpiredMonth = expirySplit[0];
        String cardExpiredYear = expirySplit[1];

        SubmitCreditCardApi.Inputs submitCreditCardApiInputs = new SubmitCreditCardApi.Inputs(
                checkoutResult.accessToken, checkoutResult.orderHash, checkoutResult.orderId,
                cardNumber, cardName, cardExpiredMonth, cardExpiredYear, cvv);
        SubmitCreditCardResponse submitCreditCardResponse = submitCreditCardResponse(submitCreditCardApiInputs);
        if (!currency.equalsIgnoreCase("IDR")) {
            PaymentPage<T> paymentPage = PaymentPage.getInstance(session);
            String acsUrl = submitCreditCardResponse.data.thirdPartyResponse.data.acsUrl;
            logger.log("acsUrl -> " + acsUrl);
            paymentPage.openUrl(acsUrl);
        }

        return String.valueOf(checkoutResult.orderId);
    }

    /**
     * Creates an issued TTD order via API using Sakuku payment.
     * Booking flow mirrors {@link #createIssuedOrderApi}; payment follows {@link HotelApi#createIssuedOrderApi}.
     */
    @SneakyThrows
    public String createIssuedOrderSakukuApi(
            String username,
            String password,
            String currency,
            String productUrlName,
            boolean refundable,
            String contactFullName,
            String contactSalutation,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation
    ) {
        return createIssuedOrderSakukuApi(username, password, currency, productUrlName, refundable,
                contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, true, null);
    }

    /**
     * Same as {@link #createIssuedOrderSakukuApi(String, String, String, String, boolean, String, String, String, String, String)}
     * but books the TTD package on {@code startDate} ({@code yyyy-MM-dd}), matching hotel/train booking dates.
     */
    @SneakyThrows
    public String createIssuedOrderSakukuApi(
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
            String startDate
    ) {
        return createIssuedOrderSakukuApi(username, password, currency, productUrlName, refundable,
                contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, true, startDate, null);
    }

    /**
     * Same as {@link #createIssuedOrderSakukuApi(String, String, String, String, boolean, String, String, String, String, String, String)}
     * but books the TTD package on a date range ({@code yyyy-MM-dd}).
     */
    @SneakyThrows
    public String createIssuedOrderSakukuApi(
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
            String startDate,
            String endDate
    ) {
        return createIssuedOrderSakukuApi(username, password, currency, productUrlName, refundable,
                contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, true, startDate, endDate);
    }

    /**
     * Same as {@link #createIssuedOrderSakukuApi(String, String, String, String, boolean, String, String, String, String, String)}
     * but lets the caller choose whether the create-order request goes through the public API gateway.
     *
     * @param hitGateway when {@code false}, the create-order request bypasses the public API gateway and hits the
     *                   events-v2-order service cluster directly — handy when Cloudflare rate-limits the gateway.
     */
    @SneakyThrows
    public String createIssuedOrderSakukuApi(
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
            boolean hitGateway
    ) {
        return createIssuedOrderSakukuApi(username, password, currency, productUrlName, refundable,
                contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, hitGateway, null, null);
    }



    @SneakyThrows
    public String createIssuedOrderSakukuApi(
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
            boolean hitGateway,
            String startDate
    ) {
        return createIssuedOrderSakukuApi(username, password, currency, productUrlName, refundable,
                contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, hitGateway,
                startDate, null);
    }

    @SneakyThrows
    public String createIssuedOrderSakukuApi(
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
            boolean hitGateway,
            String startDate,
            String endDate
    ) {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                TtdCheckoutResult checkoutResult = prepareTtdOrder(
                        username, password, currency, productUrlName, refundable,
                        contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, hitGateway,
                        startDate, endDate);

                PaymentApi<?> paymentApi = new PaymentApi<>(session);
                paymentApi.submitSakukuPayment(checkoutResult.accessToken, checkoutResult.orderId, checkoutResult.orderHash);

                String paymentStatus = paymentApi.paidCheckerStatus(120, String.valueOf(checkoutResult.orderId));
                if (!"SUCCESS".equalsIgnoreCase(paymentStatus)) {
                    Assertion.skip("Order not paid. Skipping the subsequent steps.");
                }

                logger.log("Waiting for 15 seconds after payment is done");
                driverTools.dummyWait(15);
                return String.valueOf(checkoutResult.orderId);
            } catch (Exception e) {
                logger.log("Attempt " + attempt + "/" + maxAttempts + " failed: " + ExceptionUtils.getStackTrace(e));
                if (attempt == maxAttempts) {
                    Assertion.skip("TTD booking via API with Sakuku failed after " + maxAttempts + " attempts. Skipping the subsequent steps.");
                } else {
                    logger.log("Retrying TTD booking via API (attempt " + (attempt + 1) + "/" + maxAttempts + ")");
                }
            }
        }
        return null;
    }

    private TtdCheckoutResult prepareTtdOrder(
            String username,
            String password,
            String currency,
            String productUrlName,
            boolean refundable,
            String contactFullName,
            String contactSalutation,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation
    ) throws ExecutionException, InterruptedException {
        return prepareTtdOrder(username, password, currency, productUrlName, refundable,
                contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, true, null, null);
    }

    private TtdCheckoutResult prepareTtdOrder(
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
            boolean hitGateway
    ) throws ExecutionException, InterruptedException {
        return prepareTtdOrder(username, password, currency, productUrlName, refundable,
                contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, hitGateway, null, null);
    }

    private TtdCheckoutResult prepareTtdOrder(
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
            boolean hitGateway,
            String startDate
    ) throws ExecutionException, InterruptedException {
        return prepareTtdOrder(username, password, currency, productUrlName, refundable,
                contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, hitGateway,
                startDate, null);
    }

    private TtdCheckoutResult prepareTtdOrder(
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
            boolean hitGateway,
            String startDate,
            String endDate
    ) throws ExecutionException, InterruptedException {
        Unm<T> unm = new Unm<>(session);
        LoginDataUnmResponse.Data loginData = unm.unmLoginApi(username, password);
        String accessToken = loginData.accessToken;
        logger.log("accessToken-> " + accessToken);

        TTDSelectProductApi.Inputs ttdProductSelectApiInputs = new TTDSelectProductApi.Inputs(accessToken, productUrlName);
        HashMap<String, String> selectedProductData = ttdSelectProduct(ttdProductSelectApiInputs, refundable);
        productName = selectedProductData.get("productName");
        String selectedProductID = selectedProductData.get("productID");
        logger.log("selectedProductID-> " + selectedProductID);
        logger.log("selectedProductName-> " + productName);

        String selectedPackageID;
        if (refundable) {
            String refundablePackageCode = selectedProductData.get("refundablePackageCode");
            logger.log("refundablePackageCode-> " + refundablePackageCode);
            selectedPackageID = refundablePackageCode;
        } else {
            TTDChoosePackageApi.Inputs ttdChoosePackageApiInputs = new TTDChoosePackageApi.Inputs(accessToken, selectedProductID);
            List<String> availablePackageCodes = ttdChoosePackageApi(ttdChoosePackageApiInputs);
            selectedPackageID = selectPackageCodeWithPriceTiers(ttdProductSelectApiInputs, availablePackageCodes);
        }
        logger.log("PackageID-> " + selectedPackageID);

        TTDChoosePackageScheduleApi.Inputs ttdChoosePackageScheduleApiInputs =
                new TTDChoosePackageScheduleApi.Inputs(accessToken, selectedProductID, selectedPackageID, 3, 1, startDate, endDate);
        if (startDate != null && !startDate.isBlank()) {
            logger.log("TTD startDate-> " + startDate);
        }
        if (endDate != null && !endDate.isBlank()) {
            logger.log("TTD endDate-> " + endDate);
        }
        String selectedPackageScheduleID = ttdChoosePackageScheduleApi(ttdChoosePackageScheduleApiInputs);
        logger.log("PackageScheduleID-> " + selectedPackageScheduleID);

        List<String> availablePriceTier = ttdCheckAvailablePriceTier(ttdProductSelectApiInputs, selectedPackageID);
        String selectedPriceTiers = randomSelectPriceTier(availablePriceTier);
        logger.log("selectedPriceTiers-> " + selectedPriceTiers);

        JSONObject selectedPackageDetail = ttdFetchPackagesByCode(ttdProductSelectApiInputs).get(selectedPackageID);
        List<TTDCreateOrderV2Api.Inputs.PerBookingAnswer> perBookingAnswers = extractRequiredQuestionnaireAnswers(selectedPackageDetail);

        // The legacy v3/orders endpoint (TTDCreateOrderApi) now 400s unconditionally - it rejects the request
        // with "Field error ... on field 'orders': rejected value [null]; must not be null", meaning the server
        // expects a request envelope this client never sends. v2/orders (TTDCreateOrderV2Api) is confirmed live
        // and working (verified against a real dweb capture) for both questionnaire and non-questionnaire
        // packages, so it is used unconditionally here instead of only for the questionnaire case.
        String[] guestName = splitFullName(guestFullName);
        long finalPrice = extractFinalPrice(selectedPackageDetail, selectedPriceTiers);
        String countryCode = loginData.accountPhoneCode == null || loginData.accountPhoneCode.isBlank()
                ? "+62" : loginData.accountPhoneCode;
        TTDCreateOrderV2Api.Inputs ttdCreateOrderV2ApiInputs = new TTDCreateOrderV2Api.Inputs(
                accessToken, selectedPackageScheduleID, guestSalutation, guestName[0], guestName[1],
                guestEmailAddress, countryCode, loginData.accountPhoneNumber, "Indonesia",
                loginData.accountId, selectedPriceTiers, 1, currency,
                finalPrice, perBookingAnswers, loginData.refreshToken, loginData.uniqueId);
        // Cloudflare fronts the gateway and answers this endpoint with a 403 for non-browser clients, so
        // this path goes straight to the events-v2-order cluster instead (refused on PROD, which has no
        // direct endpoint - see TTDCreateOrderV2Api.ensureDirectServiceSupportedForEnvironment).
        TTDCreateOrderV2Api ttdCreateOrderV2Api = new TTDCreateOrderV2Api(ttdCreateOrderV2ApiInputs, false);
        ApiResponse createOrderV2Response = ttdCreateOrderV2Api.hitApi(apiTools);
        logger.log("createOrderV2 response: \n" + createOrderV2Response.getBody());
        JSONObject createOrderV2Data = new JSONObject(createOrderV2Response.getBody()).getJSONObject("data");
        String createOrderID = createOrderV2Data.getString("id");
        logger.log("createOrderID-> " + createOrderID);

        TTDCheckOrderIDHashApi.Inputs ttdCheckOrderIDHashApiInputs = new TTDCheckOrderIDHashApi.Inputs(accessToken, createOrderID);
        HashMap<String, String> createOrderIDHash = ttdCheckOrderIDHashApi(ttdCheckOrderIDHashApiInputs);
        orderId = Long.parseLong(createOrderIDHash.get("orderID"));
        String orderHash = createOrderIDHash.get("orderHash");
        logger.log("OrderID-> " + orderId);
        logger.log("OrderHash-> " + orderHash);

        return new TtdCheckoutResult(accessToken, orderId, orderHash);
    }

    /**
     * Creates a TTD order via API with a B2B (corporate) account on a specific visit date and pays it using
     * Corporate Billing. When the booker is an approver / admin, the order is auto-approved, so no approve call is needed.
     *
     * @param date visit date in {@code yyyy-MM-dd} format
     * @return the order ID
     */
    @SneakyThrows
    public String createIssuedOrderApiB2B(
            String username,
            String password,
            String currency,
            String productUrlName,
            boolean refundable,
            String contactFullName,
            String contactSalutation,
            String guestFullName,
            String guestSalutation,
            String date,
            EmployeeData employeeData
    ) {
        try {
            TtdCheckoutResult checkoutResult = prepareTtdOrderB2B(
                    username, password, currency, productUrlName, refundable,
                    contactFullName, contactSalutation, guestFullName, username, guestSalutation, true, date);
            String auth = "Bearer " + checkoutResult.accessToken;

            SubmitPaymentApiB2B.Inputs submitPaymentApiInputs = new SubmitPaymentApiB2B.Inputs(
                    auth, checkoutResult.orderHash, checkoutResult.orderId,
                    employeeData.data.employeeID, employeeData.data.corporateID);
            String paymentResponse = submitPaymentB2B(submitPaymentApiInputs);
            logger.log("paymentStatus-> " + paymentResponse);

            SubmitPaymentPutApiB2B.Inputs submitPaymentPutApiInputs = new SubmitPaymentPutApiB2B.Inputs(
                    auth, checkoutResult.orderHash, checkoutResult.orderId,
                    employeeData.data.employeeID, employeeData.data.corporateID, "");
            String paymentResponsePut = submitPaymentB2BPUT(submitPaymentPutApiInputs);
            logger.log("paymentStatus-> " + paymentResponsePut);

            if (paymentResponsePut.contains("CORPORATE_BILLING_ERROR")) {
                Assertion.skip("TTD Booking via API failed. Skipping the subsequent steps.");
            }
            return String.valueOf(checkoutResult.orderId);
        } catch (SkipException e) {
            throw e;
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("TTD Booking via API with B2B account failed. Skipping the subsequent steps.");
        }
        return null;
    }

    /**
     * B2B (corporate) booking chain backing {@link #createIssuedOrderApiB2B}: logs in through the corporate
     * account and books the product/package/schedule/price-tier flow on {@code date}.
     *
     * <p>Deliberately kept separate from {@link #prepareTtdOrder} (not just a {@code useCookieAuth}-style
     * parameter added to it) because that chain carries B2C-only concerns for its questionnaire path
     * (refreshToken/uniqueId from a full UNM login) that a corporate-login access token doesn't have. Forking
     * here instead of touching the shared chain also means this won't conflict with it again as that chain
     * keeps evolving independently.
     */
    private TtdCheckoutResult prepareTtdOrderB2B(
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
            boolean hitGateway,
            String date
    ) throws ExecutionException, InterruptedException {
        CorporateLoginPage<T> corporateLoginPage = CorporateLoginPage.getInstance(session);
        String accessToken = corporateLoginPage.getAccessTokenB2BCorporate(username, password);
        logger.log("accessToken B2B-> " + accessToken);

        TTDSelectProductApi.Inputs ttdProductSelectApiInputs = new TTDSelectProductApi.Inputs(accessToken, productUrlName);
        HashMap<String, String> selectedProductData = ttdSelectProduct(ttdProductSelectApiInputs, refundable);
        productName = selectedProductData.get("productName");
        String selectedProductID = selectedProductData.get("productID");
        logger.log("selectedProductID-> " + selectedProductID);
        logger.log("selectedProductName-> " + productName);

        String selectedPackageID;
        if (refundable) {
            String refundablePackageCode = selectedProductData.get("refundablePackageCode");
            logger.log("refundablePackageCode-> " + refundablePackageCode);
            selectedPackageID = refundablePackageCode;
        } else {
            TTDChoosePackageApi.Inputs ttdChoosePackageApiInputs = new TTDChoosePackageApi.Inputs(accessToken, selectedProductID);
            List<String> availablePackageCodes = ttdChoosePackageApi(ttdChoosePackageApiInputs);
            selectedPackageID = selectPackageCodeWithPriceTiers(ttdProductSelectApiInputs, availablePackageCodes);
        }
        logger.log("PackageID-> " + selectedPackageID);

        TTDChoosePackageScheduleApi.Inputs ttdChoosePackageScheduleApiInputs =
                new TTDChoosePackageScheduleApi.Inputs(accessToken, selectedProductID, selectedPackageID, 3, 1, date, null);
        if (date != null && !date.isBlank()) {
            logger.log("TTD visit date-> " + date);
        }
        String selectedPackageScheduleID = ttdChoosePackageScheduleApi(ttdChoosePackageScheduleApiInputs);
        logger.log("PackageScheduleID-> " + selectedPackageScheduleID);

        List<String> availablePriceTier = ttdCheckAvailablePriceTier(ttdProductSelectApiInputs, selectedPackageID);
        String selectedPriceTiers = randomSelectPriceTier(availablePriceTier);
        logger.log("selectedPriceTiers-> " + selectedPriceTiers);

        TTDCreateOrderApi.Inputs ttdCreateOrderApiInputs = TTDCreateOrderApi.Inputs.legacy(
                currency, accessToken, selectedPackageScheduleID, contactFullName, username, contactSalutation,
                guestFullName, guestEmailAddress, guestSalutation, selectedPriceTiers, 0);
        String createOrderID = ttdCreateOrderApi(ttdCreateOrderApiInputs, hitGateway);
        logger.log("createOrderID-> " + createOrderID);

        TTDCheckOrderIDHashApi.Inputs ttdCheckOrderIDHashApiInputs = new TTDCheckOrderIDHashApi.Inputs(accessToken, createOrderID);
        HashMap<String, String> createOrderIDHash = ttdCheckOrderIDHashApi(ttdCheckOrderIDHashApiInputs);
        orderId = Long.parseLong(createOrderIDHash.get("orderID"));
        String orderHash = createOrderIDHash.get("orderHash");
        logger.log("OrderID-> " + orderId);
        logger.log("OrderHash-> " + orderHash);

        return new TtdCheckoutResult(accessToken, orderId, orderHash);
    }

    private HashMap<String, String> ttdSelectProduct(TTDSelectProductApi.Inputs inputs, boolean refundable)
            throws ExecutionException, InterruptedException {
        TTDSelectProductApi ttdSelectProductApi = new TTDSelectProductApi(inputs);
        ApiResponse ttdSelectProductResponse = ttdSelectProductApi.hitApi(apiTools);
        JSONObject ttdSelectProductResponseObject = new JSONObject(ttdSelectProductResponse.getBody());
        JSONObject data = ttdSelectProductResponseObject.getJSONObject("data");

        String productID = data.getString("id");
        String selectedProductName = data.getJSONArray("translations").getJSONObject(0).getString("title");

        HashMap<String, String> result = new HashMap<>();
        result.put("productID", productID);
        result.put("productName", selectedProductName);

        for (int i = 0; i < data.getJSONArray("packages").length(); i++) {
            String packageCode = data.getJSONArray("packages").getJSONObject(i).getString("code");
            boolean isRefundable = data.getJSONArray("packages").getJSONObject(i).getBoolean("isRefundable");
            boolean zeroQuestionnaire = data.getJSONArray("packages").getJSONObject(i).getJSONArray("questionnaires").isEmpty();
            if (isRefundable && zeroQuestionnaire) {
                result.put("refundablePackageCode", packageCode);
                break;
            }
        }

        return result;
    }

    private List<String> ttdChoosePackageApi(TTDChoosePackageApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDChoosePackageApi ttdChoosePackageApi = new TTDChoosePackageApi(inputs);
        ApiResponse ttdChoosePackageResponse = ttdChoosePackageApi.hitApi(apiTools);
        JSONObject ttdChoosePackageResponseObject = new JSONObject(ttdChoosePackageResponse.getBody());
        JSONArray data = ttdChoosePackageResponseObject.getJSONArray("data").getJSONObject(0).getJSONArray("packageCodes");

        List<String> packageCodes = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            packageCodes.add(data.getString(i));
        }
        return packageCodes;
    }

    /**
     * The availablePackages schedule endpoint can return package codes that are no longer present
     * in the product's own package list (stale/orphaned schedules, e.g. "22"), or that belong to
     * packages needing special handling this simple order flow can't satisfy (FILE_IMPORT/FILE_UPLOAD
     * quota, whitelist redemption codes, required questionnaires) - resulting in a 400 on order
     * creation. Prefer plain numeric package codes (like "1", "2", "4") with a standard NUMBER quota
     * and an ADULT price tier; fall back to any bookable package with a price tier if none match.
     */
    private String selectPackageCodeWithPriceTiers(TTDSelectProductApi.Inputs inputs, List<String> candidatePackageCodes)
            throws ExecutionException, InterruptedException {
        Map<String, JSONObject> packagesByCode = ttdFetchPackagesByCode(inputs);

        String fallbackPackageCode = null;
        for (String packageCode : candidatePackageCodes) {
            JSONObject packageDetail = packagesByCode.get(packageCode);
            if (packageDetail == null) {
                continue;
            }
            List<String> priceTierCodes = extractPriceTierCodes(packageDetail);
            if (priceTierCodes.isEmpty()) {
                continue;
            }
            if (fallbackPackageCode == null) {
                fallbackPackageCode = packageCode;
            }
            if (isSimpleBookablePackage(packageDetail) && priceTierCodes.contains("ADULT")) {
                logger.log("Selected packageCode-> " + packageCode + " with ADULT price tier");
                return packageCode;
            }
        }

        if (fallbackPackageCode != null) {
            logger.log("No simple package with ADULT price tier found; falling back to packageCode-> " + fallbackPackageCode);
            return fallbackPackageCode;
        }

        throw new IllegalStateException(
                "None of the available package codes " + candidatePackageCodes + " have price tiers configured for this product");
    }

    /**
     * Answers every {@code isRequired} questionnaire on the package with a default choice ("1"), so
     * create-order does not 400 on a product like "Preprod Annisa" (packageCode with questionnaire code
     * LIST-ANNISA) where none of the candidate packages is questionnaire-free. Returns an empty list
     * when the package has no required questionnaire, in which case the caller stays on the legacy
     * v3/orders endpoint.
     */
    public static List<TTDCreateOrderV2Api.Inputs.PerBookingAnswer> extractRequiredQuestionnaireAnswers(JSONObject packageDetail) {
        List<TTDCreateOrderV2Api.Inputs.PerBookingAnswer> answers = new ArrayList<>();
        if (packageDetail == null) {
            return answers;
        }
        JSONArray questionnaires = packageDetail.optJSONArray("questionnaires");
        if (questionnaires == null) {
            return answers;
        }
        for (Object questionnaire : questionnaires) {
            if (questionnaire instanceof JSONObject questionnaireJson && questionnaireJson.optBoolean("isRequired", false)) {
                answers.add(new TTDCreateOrderV2Api.Inputs.PerBookingAnswer(
                        "1",
                        questionnaireJson.optString("code"),
                        questionnaireJson.optString("inputType"),
                        questionnaireJson.optBoolean("isPerPax", false)));
            }
        }
        return answers;
    }

    public static long extractFinalPrice(JSONObject packageDetail, String priceTierCode) {
        if (packageDetail == null) {
            return 0;
        }
        JSONArray priceTiers = packageDetail.optJSONArray("priceTiers");
        if (priceTiers == null) {
            return 0;
        }
        for (Object tierItem : priceTiers) {
            if (tierItem instanceof JSONObject priceTier && priceTierCode.equals(priceTier.optString("code"))) {
                return priceTier.optLong("finalPrice", 0);
            }
        }
        return 0;
    }

    private String[] splitFullName(String fullName) {
        String[] parts = fullName.trim().split("\\s+", 2);
        return new String[]{parts[0], parts.length > 1 ? parts[1] : ""};
    }

    /**
     * A required questionnaire is no longer disqualifying here - {@link #extractRequiredQuestionnaireAnswers}
     * lets {@code prepareTtdOrder} answer it and route to {@link TTDCreateOrderV2Api}, so a product whose only
     * bookable package needs one (e.g. "Preprod Annisa" / LIST-ANNISA) is still selected deterministically
     * instead of only ever being picked up by the loose fallback below.
     */
    private boolean isSimpleBookablePackage(JSONObject packageDetail) {
        String code = packageDetail.optString("code");
        boolean isSimpleNumericCode = code.matches("\\d{1,3}");
        boolean isActive = "ACTIVE".equals(packageDetail.optString("productPackageStatus"));
        boolean isRefundable = packageDetail.optBoolean("isRefundable", false);
        boolean isStandardQuota = "NUMBER".equals(packageDetail.optString("quotaType"));
        boolean requiresWhitelist = packageDetail.optJSONObject("packageWhitelist") != null;

        return isSimpleNumericCode && isActive && !isRefundable && isStandardQuota && !requiresWhitelist;
    }

    private HashMap<String, String> ttdCheckOrderIDHashApi(TTDCheckOrderIDHashApi.Inputs inputs)
            throws ExecutionException, InterruptedException {
        TTDCheckOrderIDHashApi ttdCheckOrderIDHashApi = new TTDCheckOrderIDHashApi(inputs);
        HashMap<String, String> result = new HashMap<>();

        for (int i = 0; i < 30; i++) {
            ApiResponse ttdCheckOrderIDHashResponse = ttdCheckOrderIDHashApi.hitApi(apiTools);
            JSONObject ttdCheckOrderIDHashResponseObject = new JSONObject(ttdCheckOrderIDHashResponse.getBody());
            JSONObject data = ttdCheckOrderIDHashResponseObject.getJSONObject("data");

            if (data.isNull("coreOrderId") || data.isNull("coreOrderHash")) {
                Thread.sleep(2000);
                continue;
            }
            result.put("orderID", data.getString("coreOrderId"));
            result.put("orderHash", data.getString("coreOrderHash"));
            break;
        }

        return result;
    }

    private String ttdCreateOrderApi(TTDCreateOrderApi.Inputs inputs, boolean hitGateway) throws ExecutionException, InterruptedException {
        TTDCreateOrderApi ttdCreateOrderApi = new TTDCreateOrderApi(inputs, hitGateway);
        ApiResponse ttdCreateOrderResponse = ttdCreateOrderApi.hitApi(apiTools);
        JSONObject ttdCreateOrderResponseObject = new JSONObject(ttdCreateOrderResponse.getBody());
        logger.log("response: \n" + ttdCreateOrderResponse.getBody());
        JSONObject data = ttdCreateOrderResponseObject.getJSONObject("data");
        packageName = data.getJSONObject("productPackage").getJSONArray("translations").getJSONObject(0).getString("name");
        logger.log("packageName-> " + packageName);
        return data.getString("id");
    }

    private List<String> ttdCheckAvailablePriceTier(TTDSelectProductApi.Inputs inputs, String packageCode)
            throws ExecutionException, InterruptedException {
        JSONObject packageDetail = ttdFetchPackagesByCode(inputs).get(packageCode);
        List<String> availablePriceTiers = packageDetail != null ? extractPriceTierCodes(packageDetail) : new ArrayList<>();
        logger.log("Available price tiers for packageCode " + packageCode + " available price tiers" + availablePriceTiers);
        return availablePriceTiers;
    }

    private Map<String, JSONObject> ttdFetchPackagesByCode(TTDSelectProductApi.Inputs inputs)
            throws ExecutionException, InterruptedException {
        TTDSelectProductApi ttdSelectProductApi = new TTDSelectProductApi(inputs);
        ApiResponse ttdSelectProductResponse = ttdSelectProductApi.hitApi(apiTools);
        JSONObject ttdSelectProductResponseObject = new JSONObject(ttdSelectProductResponse.getBody());

        Map<String, JSONObject> packagesByCode = new HashMap<>();

        JSONObject dataObj = ttdSelectProductResponseObject.optJSONObject("data");
        if (dataObj != null) {
            JSONArray packagesData = dataObj.optJSONArray("packages");

            if (packagesData != null) {
                for (Object packageDetail : packagesData) {
                    if (packageDetail instanceof JSONObject packageDetailJson) {
                        packagesByCode.put(packageDetailJson.optString("code"), packageDetailJson);
                    }
                }
            }
        }

        return packagesByCode;
    }

    private List<String> extractPriceTierCodes(JSONObject packageDetail) {
        List<String> tierCodes = new ArrayList<>();
        JSONArray priceTiers = packageDetail.optJSONArray("priceTiers");
        if (priceTiers != null) {
            for (Object tierItem : priceTiers) {
                if (tierItem instanceof JSONObject priceTier) {
                    String tierCode = priceTier.optString("code");
                    if (!tierCode.isEmpty()) {
                        tierCodes.add(tierCode);
                    }
                }
            }
        }
        return tierCodes;
    }

    private String randomSelectPriceTier(List<String> availablePriceTier) {
        logger.log(availablePriceTier.toString());
        if (availablePriceTier.contains("ADULT")) {
            logger.log("Selected priceTier-> ADULT");
            return "ADULT";
        }
        int size = availablePriceTier.size();
        logger.log("Size value is" + size);
        return availablePriceTier.get(CoreFunctions.generateRandomInt(0, size - 1));
    }

    private String ttdChoosePackageScheduleApi(TTDChoosePackageScheduleApi.Inputs inputs)
            throws ExecutionException, InterruptedException {
        TTDChoosePackageScheduleApi ttdChoosePackageScheduleApi = new TTDChoosePackageScheduleApi(inputs);
        ApiResponse ttdChoosePackageScheduleResponse = ttdChoosePackageScheduleApi.hitApi(apiTools);
        JSONObject ttdChoosePackageScheduleResponseObject = new JSONObject(ttdChoosePackageScheduleResponse.getBody());
        logger.log("Choose package schedule response : " + ttdChoosePackageScheduleResponseObject);
        JSONObject data = ttdChoosePackageScheduleResponseObject.getJSONArray("data").getJSONObject(0);
        return data.getString("id");
    }

    private String ttdChoosePackageScheduleByDateApi(TTDChoosePackageScheduleByDateApi.Inputs inputs, boolean useCookieAuth)
            throws ExecutionException, InterruptedException {
        TTDChoosePackageScheduleByDateApi ttdChoosePackageScheduleByDateApi = new TTDChoosePackageScheduleByDateApi(inputs, useCookieAuth);
        ApiResponse ttdChoosePackageScheduleResponse = ttdChoosePackageScheduleByDateApi.hitApi(apiTools);
        JSONObject ttdChoosePackageScheduleResponseObject = new JSONObject(ttdChoosePackageScheduleResponse.getBody());
        logger.log("Choose package schedule by date response : " + ttdChoosePackageScheduleResponseObject);
        JSONArray data = ttdChoosePackageScheduleResponseObject.optJSONArray("data");
        if (data == null || data.isEmpty()) {
            throw new IllegalStateException("No TTD package schedule available on " + inputs.date()
                    + " for productId " + inputs.productID() + " / packageCode " + inputs.packageCode());
        }
        return data.getJSONObject(0).getString("id");
    }

    private String submitPaymentB2B(SubmitPaymentApiB2B.Inputs inputs) {
        SubmitPaymentApiB2B submitPaymentApi = new SubmitPaymentApiB2B(inputs);
        ApiResponse apiResponse = submitPaymentApi.hitApi(apiTools);
        JSONObject response = new JSONObject(apiResponse.getBody());
        logger.log("Payment api response -> " + response);
        return response.getString("code");
    }

    private String submitPaymentB2BPUT(SubmitPaymentPutApiB2B.Inputs inputs) {
        SubmitPaymentPutApiB2B submitPaymentApi = new SubmitPaymentPutApiB2B(inputs);
        ApiResponse apiResponse = submitPaymentApi.hitApi(apiTools);
        JSONObject response = new JSONObject(apiResponse.getBody());
        logger.log("Payment api response -> " + response);
        return response.getString("code");
    }

    private SubmitCreditCardResponse submitCreditCardResponse(SubmitCreditCardApi.Inputs inputs)
            throws ExecutionException, InterruptedException {
        SubmitCreditCardApi submitCreditCardApi = new SubmitCreditCardApi(inputs);
        ApiResponse submitCreditCard = submitCreditCardApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(submitCreditCard.getBody(), SubmitCreditCardResponse.class);
    }

    private static final class TtdCheckoutResult {
        private final String accessToken;
        private final long orderId;
        private final String orderHash;

        private TtdCheckoutResult(String accessToken, long orderId, String orderHash) {
            this.accessToken = accessToken;
            this.orderId = orderId;
            this.orderHash = orderHash;
        }
    }
}
