package com.tiket.page.ttd;

import com.google.gson.Gson;
import com.tiket.api.auth.AdminDashboardLoginResponseParser;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardDynamicApi;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.order.CoreOrderIDApi;
import com.tiket.api.order.OrderListApi;
import com.tiket.api.payment.SelectPaymentApi;
import com.tiket.api.payment.SubmitCreditCardApi;
import com.tiket.api.payment.SubmitPaymentApi;
import com.tiket.api.ttd.*;
import com.tiket.apiPage.LoginApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.accommodation.hotel.reviewpage.ReviewPage;
import com.tiket.pojo.order.OrderListResponse;
import com.tiket.pojo.payment.SubmitCreditCardResponse;
import com.tiket.pojo.ttd.*;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dWeb.page.account.AccountPage;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.tiket.core.CoreFunctions.getLanguage;
import static com.tiket.core.CoreFunctions.getMonthNumber;
import static org.hamcrest.Matchers.hasItem;

public class TTDPage<T extends WebDriver> extends BasePage<T> {

    public TTDPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TTDPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {
                return new TTDPageIOS<>(session);
            }
            case ANDROID -> {
                return new TTDPageAndroid<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator LPG_PAGE = new Locator("LPG_PAGE", AppiumBy.xpath("(//*[contains(@value, 'Lowest Price Guarantee') or contains(@value, 'Jaminan Harga Termurah')])[1]"));
    private static final Locator LPG_PAGE_ANDROID = new Locator("LPG_PAGE_ANDROID", AppiumBy.xpath("(//*[@class='android.widget.TextView' and contains(@text, 'Lowest Price Guarantee') or contains(@text, 'Jaminan Harga Termurah')])[1]"));

    @SneakyThrows
    public boolean isLpgPageDisplayed() {
        boolean result = false;
        if (Configs.PLATFORM == Platform.IOS){
            result = driverTools.waitForElementToAppear(LPG_PAGE).isPresent();
        }
        if (Configs.PLATFORM == Platform.ANDROID){
            result = driverTools.waitForElementToAppear(LPG_PAGE_ANDROID).isPresent();
        }
        return result;
    }

    String productName;
    String packageName;
    long orderId;
    BigInteger grandTotal;

    @SneakyThrows
    public String createWaitingPaymentOrderApi(
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
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        logger.log("accessToken-> " + accessToken);

        TTDSelectProductApi.Inputs ttdProductSelectApiInputs = new TTDSelectProductApi.Inputs(accessToken, productUrlName);
        HashMap<String, String> selectedProductData = ttdSelectProduct(ttdProductSelectApiInputs, refundable);
        productName = selectedProductData.get("productName");
        String selectedProductID = selectedProductData.get("productID");
        logger.log("selectedProductID-> " + selectedProductID);
        logger.log("selectedProductName-> " + productName);

        String selectedPackageID = "";
        if (refundable) {
            String refundablePackageCode = selectedProductData.get("refundablePackageCode");
            logger.log("refundablePackageCode-> " + refundablePackageCode);

            selectedPackageID += refundablePackageCode;
            logger.log("PackageID-> " + selectedPackageID);
        } else {
            TTDChoosePackageApi.Inputs ttdChoosePackageApiInputs = new TTDChoosePackageApi.Inputs(accessToken, selectedProductID);
            selectedPackageID += ttdChoosePackageCodeViaApi(ttdChoosePackageApiInputs);
            logger.log("PackageCode-> " + selectedPackageID);
        }

        TTDChoosePackageScheduleApi.Inputs ttdChoosePackageScheduleApiInputs = new TTDChoosePackageScheduleApi.Inputs(accessToken, selectedProductID, selectedPackageID, 3, 1);
        String selectedPackageScheduleID = ttdChoosePackageScheduleApi(ttdChoosePackageScheduleApiInputs);
        logger.log("PackageScheduleID-> " + selectedPackageScheduleID);

        List<String> availablePriceTier = ttdCheckAvailablePriceTier(ttdProductSelectApiInputs, selectedPackageID);
        String selectedPriceTiers = randomSelectPriceTier(availablePriceTier);
        logger.log("selectedPriceTiers-> " + selectedPriceTiers);

        TTDCreateOrderApi.Inputs ttdCreateOrderApiInputs = TTDCreateOrderApi.Inputs.legacy(currency, accessToken, selectedPackageScheduleID, contactFullName, username, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, selectedPriceTiers,0);
        String createOrderID = ttdCreateOrderApi(ttdCreateOrderApiInputs);
        logger.log("createOrderID-> " + createOrderID);

        TTDCheckOrderIDHashApi.Inputs ttdCheckOrderIDHashApiInputs = new TTDCheckOrderIDHashApi.Inputs(accessToken, createOrderID);
        HashMap<String, String> createOrderIDHash = ttdCheckOrderIDHashApi(ttdCheckOrderIDHashApiInputs);
        orderId = Long.parseLong(createOrderIDHash.get("orderID"));
        String orderHash = createOrderIDHash.get("orderHash");
        logger.log("OrderID-> " + orderId);
        logger.log("OrderHash-> " + orderHash);

        return String.valueOf(orderId);
    }

    @SneakyThrows
    public String createWaitingPaymentOrderApi(
            String username,
            String password,
            String productUrlName,
            String packageCode,
            String contactFullName,
            String contactSalutation,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation,
            int dateRange,
            int monthRange,
            boolean insurance
    ) {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        logger.log("accessToken-> " + accessToken);

        TTDSelectProductApi.Inputs ttdProductSelectApiInputs = new TTDSelectProductApi.Inputs(accessToken, productUrlName);
        HashMap<String, String> selectedProductData = ttdSelectProduct(ttdProductSelectApiInputs, false);
        productName = selectedProductData.get("productName");
        String selectedProductID = selectedProductData.get("productID");
        logger.log("selectedProductID-> " + selectedProductID);
        logger.log("selectedProductName-> " + productName);
        logger.log("refundablePackageCode-> " + selectedProductData.get("refundablePackageCode"));

        packageCode = selectedProductData.get("refundablePackageCode");
        logger.log("PackageCode-> " + packageCode);

        TTDChoosePackageScheduleApi.Inputs ttdChoosePackageScheduleApiInputs = new TTDChoosePackageScheduleApi.Inputs(accessToken, selectedProductID, packageCode, dateRange, monthRange);
        String selectedPackageScheduleID = ttdChoosePackageScheduleApi(ttdChoosePackageScheduleApiInputs);
        logger.log("PackageScheduleID-> " + selectedPackageScheduleID);

        List<String> availablePriceTier = ttdCheckAvailablePriceTier(ttdProductSelectApiInputs, packageCode);
        String selectedPriceTiers = randomSelectPriceTier(availablePriceTier);
        logger.log("selectedPriceTiers-> " + selectedPriceTiers);

        int insuranceCode = 0;
        if(insurance){
            insuranceCode = getInsuranceCode(ttdProductSelectApiInputs, packageCode);
            logger.log("insurance code-> " + insuranceCode);
        }


        TTDCreateOrderApi.Inputs ttdCreateOrderApiInputs = TTDCreateOrderApi.Inputs.legacy("IDR", accessToken, selectedPackageScheduleID, contactFullName, username, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, selectedPriceTiers,insuranceCode);
        String createOrderID = ttdCreateOrderApi(ttdCreateOrderApiInputs);
        logger.log("createOrderID-> " + createOrderID);

        TTDCheckOrderIDHashApi.Inputs ttdCheckOrderIDHashApiInputs = new TTDCheckOrderIDHashApi.Inputs(accessToken, createOrderID);
        TtdCheckOrderResponse createdOrderIDHash = ttdGetOrderData(ttdCheckOrderIDHashApiInputs);
        orderId = Long.parseLong(createdOrderIDHash.coreOrderId);
        String orderHash = createdOrderIDHash.coreOrderHash;
        grandTotal = createdOrderIDHash.totalAmount;
        logger.log("OrderID-> " + orderId);
        logger.log("OrderHash-> " + orderHash);
        logger.log("Grand total-> " + grandTotal);

        return String.valueOf(orderId);
    }

    public BigInteger getGrandTotal() {
        return grandTotal;
    }


    public String issuedOrderData(String data) {
        HashMap<String, String> issuedOrder = new HashMap<>();
        issuedOrder.put("productName", productName);
        issuedOrder.put("packageName", packageName);
        issuedOrder.put("orderId", String.valueOf(orderId));
        issuedOrder.put("totalPrice", String.valueOf(grandTotal));
        return issuedOrder.get(data);
    }

    @SneakyThrows
    public boolean checkOrderStatus(String username, String password, long orderId) {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        logger.log("accessToken-> " + accessToken);

        OrderListApi.Inputs orderListApiInputs = new OrderListApi.Inputs(username, accessToken);
        return orderList(orderListApiInputs, orderId);
    }

    @SneakyThrows
    private String getToken() {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
    }

    private String getAdminAccessTokenDashboardAuth(String adminUsername, String adminPassword) {
        POSTGetAdminAccessTokenDashboardDynamicApi.Inputs inputs = new POSTGetAdminAccessTokenDashboardDynamicApi.Inputs(adminUsername, adminPassword);
        POSTGetAdminAccessTokenDashboardDynamicApi postGetAdminAccessTokenDashboardApi = new POSTGetAdminAccessTokenDashboardDynamicApi(inputs);
        ApiResponse response = postGetAdminAccessTokenDashboardApi.hitApi(apiTools);
        logger.log("response : " + response.getBody());
        String accessToken = AdminDashboardLoginResponseParser.resolveSessionTokenOrNull(response);
        if (accessToken != null) {
            logger.log("session_access_token= " + accessToken);
            return accessToken;
        }
        return null;
    }

    private HashMap<String, String> ttdSelectProduct(TTDSelectProductApi.Inputs inputs, boolean refundable) throws ExecutionException, InterruptedException {
        TTDSelectProductApi ttdSelectProductApi = new TTDSelectProductApi(inputs);
        ApiResponse ttdSelectProductResponse = ttdSelectProductApi.hitApi(apiTools);
        JSONObject ttdSelectProductResponseObject = new JSONObject(ttdSelectProductResponse.getBody());
        JSONObject data = ttdSelectProductResponseObject.getJSONObject("data");

        String productID = data.getString("id");
        String productName = data.getJSONArray("translations").getJSONObject(0).getString("title");

        HashMap<String, String> result = new HashMap<>();
        result.put("productID", productID);
        result.put("productName", productName);

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

    @SneakyThrows
    private String ttdChoosePackageCodeViaApi(TTDChoosePackageApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDChoosePackageApi ttdChoosePackageApi = new TTDChoosePackageApi(inputs);
        ApiResponse ttdChoosePackageResponse = ttdChoosePackageApi.hitApi(apiTools);
        JSONObject ttdChoosePackageResponseObject = new JSONObject(ttdChoosePackageResponse.getBody());
        JSONArray data = ttdChoosePackageResponseObject.getJSONArray("data").getJSONObject(0).getJSONArray("packageCodes");

        return data.getString(0);
    }

    public String getSeatNoFromApiResponse(String createOrderID) throws ExecutionException, InterruptedException {
        CoreOrderIDApi.Inputs inputs = new CoreOrderIDApi.Inputs(createOrderID);
        CoreOrderIDApi coreOrderIDApi = new CoreOrderIDApi(inputs);
        JSONObject data = new JSONObject();
        ApiResponse CoreOrderIDApi = coreOrderIDApi.hitApi(apiTools);
        JSONObject CoreOrderIDApiResponseObject = new JSONObject(CoreOrderIDApi.getBody());
        data = CoreOrderIDApiResponseObject.getJSONObject("data");
        String seatCode = data.getJSONArray("tickets").getJSONObject(0).getString("seatCode");
        logger.log("seatCode-> " + seatCode);
        return seatCode;
    }

    private HashMap<String, String> ttdCheckOrderIDHashApi(TTDCheckOrderIDHashApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDCheckOrderIDHashApi ttdCheckOrderIDHashApi = new TTDCheckOrderIDHashApi(inputs);
        JSONObject data = new JSONObject();
        HashMap<String, String> result = new HashMap<>();

        for (int i = 0; i < 30; i++) {
            ApiResponse ttdCheckOrderIDHashResponse = ttdCheckOrderIDHashApi.hitApi(apiTools);
            JSONObject ttdCheckOrderIDHashResponseObject = new JSONObject(ttdCheckOrderIDHashResponse.getBody());
            data = ttdCheckOrderIDHashResponseObject.getJSONObject("data");

            if (data.isNull("coreOrderId") || data.isNull("coreOrderHash")) {
                continue;
            } else {
                result.put("orderID", data.getString("coreOrderId"));
                result.put("orderHash", data.getString("coreOrderHash"));
                break;
            }
        }

        return result;
    }

    private TtdCheckOrderResponse ttdGetOrderData(TTDCheckOrderIDHashApi.Inputs inputs) throws ExecutionException, InterruptedException {
        Gson gson = new Gson();
        TTDCheckOrderIDHashApi ttdCheckOrderIDHashApi = new TTDCheckOrderIDHashApi(inputs);
        JSONObject data = new JSONObject();

        for (int i = 0; i < 30; i++) {
            ApiResponse ttdCheckOrderIDHashResponse = ttdCheckOrderIDHashApi.hitApi(apiTools);
            JSONObject ttdCheckOrderIDHashResponseObject = new JSONObject(ttdCheckOrderIDHashResponse.getBody());
            data = ttdCheckOrderIDHashResponseObject.getJSONObject("data");

            if (data.isNull("coreOrderId") || data.isNull("coreOrderHash")) {
                continue;
            } else {
                break;
            }
        }
        logger.log("core response : \n" + data.toString());
        TtdCheckOrderResponse result = gson.fromJson(data.toString(), TtdCheckOrderResponse.class);
        return result;
    }

    private String ttdCreateOrderApi(TTDCreateOrderApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDCreateOrderApi ttdCreateOrderApi = new TTDCreateOrderApi(inputs);
        ApiResponse ttdCreateOrderResponse = ttdCreateOrderApi.hitApi(apiTools);
        JSONObject ttdCreateOrderResponseObject = new JSONObject(ttdCreateOrderResponse.getBody());
        logger.log("response: \n" + ttdCreateOrderResponse.getBody());
        JSONObject data = ttdCreateOrderResponseObject.getJSONObject("data");
        logger.log("data: \n" + ttdCreateOrderResponseObject.toString());
        packageName = data.getJSONObject("productPackage").getJSONArray("translations").getJSONObject(0).getString("name");
        logger.log("packageName-> " + packageName);
        return data.getString("id");
    }

    private List<String> ttdCheckAvailablePriceTier(TTDSelectProductApi.Inputs inputs, String packageCode) throws ExecutionException, InterruptedException {
        TTDSelectProductApi ttdSelectProductApi = new TTDSelectProductApi(inputs);
        ApiResponse ttdSelectProductResponse = ttdSelectProductApi.hitApi(apiTools);
        JSONObject ttdSelectProductResponseObject = new JSONObject(ttdSelectProductResponse.getBody());

        JSONArray packagesData = ttdSelectProductResponseObject.getJSONObject("data").getJSONArray("packages");
        List<String> availablePriceTiers = new ArrayList<>();

        for (Object packageDetail : packagesData) {
            if (packageDetail instanceof JSONObject) {
                JSONObject packageDetail2 = (JSONObject) packageDetail;

                if (packageDetail2.getString("code").equalsIgnoreCase(packageCode)) {
                    JSONArray availablePriceTier = packageDetail2.getJSONArray("priceTiers");

                    availablePriceTier.forEach(item -> {
                        JSONObject obj = (JSONObject) item;
                        availablePriceTiers.add(obj.getString("code"));
                    });

                    break;
                }
            }
        }

        return availablePriceTiers;
    }

    private int getInsuranceCode(TTDSelectProductApi.Inputs inputs, String packageCode) throws ExecutionException, InterruptedException {
        TTDSelectProductApi ttdSelectProductApi = new TTDSelectProductApi(inputs);
        ApiResponse ttdSelectProductResponse = ttdSelectProductApi.hitApi(apiTools);
        JSONObject ttdSelectProductResponseObject = new JSONObject(ttdSelectProductResponse.getBody());

        JSONArray packagesData = ttdSelectProductResponseObject.getJSONObject("data").getJSONArray("packages");

        for (Object packageDetail : packagesData) {
            if (packageDetail instanceof JSONObject) {
                JSONObject packageDetail2 = (JSONObject) packageDetail;

                if (packageDetail2.getString("code").equalsIgnoreCase(packageCode)) {
                    JSONArray availablePriceTier = packageDetail2.getJSONArray("priceTiers");
                   return availablePriceTier.getJSONObject(0).getJSONArray("insurances").getJSONObject(0).getInt("credentialId");
                    }
                }
            }
        return 0;
    }
    private String randomSelectPriceTier(List<String> availablePriceTier) {
        int size = availablePriceTier.size();

        return availablePriceTier.get(CoreFunctions.generateRandomInt(0, size - 1));
    }

    private String ttdChoosePackageScheduleApi(TTDChoosePackageScheduleApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDChoosePackageScheduleApi ttdChoosePackageScheduleApi = new TTDChoosePackageScheduleApi(inputs);
        ApiResponse ttdChoosePackageScheduleResponse = ttdChoosePackageScheduleApi.hitApi(apiTools);
        JSONObject ttdChoosePackageScheduleResponseObject = new JSONObject(ttdChoosePackageScheduleResponse.getBody());
        logger.log("Choose package schedule response : " + ttdChoosePackageScheduleResponseObject.toString());
        JSONObject data = ttdChoosePackageScheduleResponseObject.getJSONArray("data").getJSONObject(0);

        return data.getString("id");
    }

    private long selectPayment(SelectPaymentApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SelectPaymentApi selectPaymentApi = new SelectPaymentApi(inputs);
        ApiResponse apiResponse = selectPaymentApi.hitApi(apiTools);
        JSONArray responseArray = new JSONArray(apiResponse.getBody());
        return responseArray.getJSONObject(0)
                .getJSONObject("data")
                .getJSONObject("paymentSidebar")
                .getJSONObject("data")
                .getLong("referenceId");
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
        } while (!expectedStatus.equals(status) && count < 180);

        return expectedStatus.equals(status);
    }

    @SneakyThrows
    public TTDLandingPagePOJO.Data getLandingPageContent(String language) {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        TTDLandingPageApi.Inputs TTDLandingPageApiUInputs = new TTDLandingPageApi.Inputs(accessToken, userAgent, getLanguage(language), null);
        TTDLandingPagePOJO.Data landingPageData = TTDLandingPageContent(TTDLandingPageApiUInputs);

        return landingPageData;
    }

    @SneakyThrows
    public List<TTDLandingPagePOJO.Data> getAllLandingPageContent(String language, String currency) throws ExecutionException, InterruptedException {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        TTDLandingPageApi.Inputs TTDLandingPageApiUInputs = new TTDLandingPageApi.Inputs(accessToken, userAgent, getLanguage(language), currency);
        List<TTDLandingPagePOJO.Data> landingPageData = AllTTDLandingPageContent(TTDLandingPageApiUInputs);

        return landingPageData;
    }

    @SneakyThrows
    public List<GETLandingPageForYouSectionPOJO.Data> getAllLandingPageForYouSectionContent(String language, String currency) throws ExecutionException, InterruptedException {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        GETLandingPageForYouSectionAPI.Inputs GETLandingPageForYouSectionAPIInputs = new GETLandingPageForYouSectionAPI.Inputs(accessToken, userAgent, getLanguage(language), currency);
        List<GETLandingPageForYouSectionPOJO.Data> landingPageData = AllTTDLandingPageForYouContent(GETLandingPageForYouSectionAPIInputs);

        return landingPageData;
    }

    private List<GETLandingPageForYouSectionPOJO.Data> AllTTDLandingPageForYouContent(GETLandingPageForYouSectionAPI.Inputs inputs) throws ExecutionException, InterruptedException {
        GETLandingPageForYouSectionAPI GETLandingPageForYouSectionAPI = new GETLandingPageForYouSectionAPI(inputs);
        ApiResponse apiResponse = GETLandingPageForYouSectionAPI.hitApi(apiTools);

        Gson gson = new Gson();

        return Arrays.stream(gson.fromJson(apiResponse.getBody(), GETLandingPageForYouSectionPOJO.class).data).toList();
    }


    @SneakyThrows
    public TTDLandingPagePOJO getCompleteLandingPageData(String language) {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        TTDLandingPageApi.Inputs TTDLandingPageApiInputs = new TTDLandingPageApi.Inputs(accessToken, userAgent, getLanguage(language), null);
        return TTDCompleteLandingPageData(TTDLandingPageApiInputs);
    }

    private TTDLandingPagePOJO TTDCompleteLandingPageData(TTDLandingPageApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDLandingPageApi ttdLandingPageApi = new TTDLandingPageApi(inputs);
        ApiResponse apiResponse = ttdLandingPageApi.hitApi(apiTools);

        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), TTDLandingPagePOJO.class);
    }

    private TTDLandingPagePOJO.Data TTDLandingPageContent(TTDLandingPageApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDLandingPageApi ttdLandingPageApi = new TTDLandingPageApi(inputs);
        ApiResponse apiResponse = ttdLandingPageApi.hitApi(apiTools);
        logger.log("response-> " + apiResponse.getBody());

        Gson gson = new Gson();
        JSONObject response = new JSONObject(apiResponse.getBody());
        JSONArray list = response.getJSONArray("data");

        TTDLandingPagePOJO.Data result = null;

        for(int i = 0; i < 5; i++) {
            int totalData = list.length();
            int index = CoreFunctions.generateRandomInt(0, totalData - 3);
            logger.log("index-> " + index);

            result = gson.fromJson(list.getJSONObject(index).toString(), TTDLandingPagePOJO.Data.class);

            if(!(result.templateCode.equalsIgnoreCase("BANNER_CAROUSEL_V2") && result.sectionName.contains("Destination Page"))) {
                break;
            }
        }

        return result;
    }

    private List<TTDLandingPagePOJO.Data> AllTTDLandingPageContent(TTDLandingPageApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDLandingPageApi ttdLandingPageApi = new TTDLandingPageApi(inputs);
        ApiResponse apiResponse = ttdLandingPageApi.hitApi(apiTools);

        Gson gson = new Gson();

        return Arrays.stream(gson.fromJson(apiResponse.getBody(), TTDLandingPagePOJO.class).data).toList();
    }

    @SneakyThrows
    public TTDSRPByLocationPOJO getTTDSRPContentByLocation(String language, String location, String category) {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        String locationPublicID = getLocationPublicID(accessToken, userAgent, location, language);
        logger.log("locationPublicID-> " + locationPublicID);

        TTDSRPByLocationApi.Inputs TTDSRPByLocationInputs = new TTDSRPByLocationApi.Inputs(accessToken, userAgent, getLanguage(language), locationPublicID, category);
        TTDSRPByLocationPOJO TTDSRPContents = TTDSRPContentBYLocation(TTDSRPByLocationInputs);

        return TTDSRPContents;
    }

    private String getLocationPublicID(String accessToken, String userAgent, String location, String language) throws ExecutionException, InterruptedException {
        GETSearchSuggestionResultApi.Inputs inputs = new GETSearchSuggestionResultApi.Inputs(accessToken, userAgent, language, location);
        GETSearchSuggestionResultApi getLocationPublicIdApi = new GETSearchSuggestionResultApi(inputs);
        ApiResponse getSearchSuggestionResultResponse = getLocationPublicIdApi.hitApi(apiTools);

        Gson gson = new Gson();
        GETSearchSuggestionResultResponsePOJO getSearchSuggestionResultResponsePOJO = gson.fromJson(getSearchSuggestionResultResponse.getBody(), GETSearchSuggestionResultResponsePOJO.class);

        List<GETSearchSuggestionResultResponsePOJO.Destination> destinations = getSearchSuggestionResultResponsePOJO.data.destinations;

        for(GETSearchSuggestionResultResponsePOJO.Destination destination : destinations) {
            logger.log("destinationName -> " + destination.translations.get(0).title);
            if(destination.translations.get(0).title.equalsIgnoreCase(location)) {
                logger.log("destinationPublicId -> " + destination.code);
                return destination.code;
            }
        }

        return null;
    }

    private TTDSRPByLocationPOJO TTDSRPContentBYLocation(TTDSRPByLocationApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDSRPByLocationApi ttdsrpByLocationApi = new TTDSRPByLocationApi(inputs);
        ApiResponse apiResponse = ttdsrpByLocationApi.hitApi(apiTools);
        logger.log("response-> " + apiResponse.getBody());

        Gson gson = new Gson();
        TTDSRPByLocationPOJO TTDSRPContents = gson.fromJson(apiResponse.getBody(), TTDSRPByLocationPOJO.class);
        return TTDSRPContents;
    }

    @SneakyThrows
    public TTDPartnerPageModules.Datum getPartnerPageGridBannerTitle(String keyword, String templateCode, String langCode, String appVersion) {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        TTDGetPartnerIdApi.Inputs ttdGetPartnerIdApiInput = new TTDGetPartnerIdApi.Inputs(accessToken, keyword);
        String partnerId = TTDGetPartnerId(ttdGetPartnerIdApiInput);
        logger.log("partnerId-> " + partnerId);

        String userAgent = CoreFunctions.userAgentValue() + " - " + appVersion;
        logger.log("userAgent-> " + userAgent);

        TTDGetPartnerPageModulesApi.Inputs ttdGetPartnerPageModulesApiInputs = new TTDGetPartnerPageModulesApi.Inputs(accessToken, partnerId, langCode, userAgent);
        TTDPartnerPageModules.Datum ttdPartnerPageModule = TTDGetPageModules(ttdGetPartnerPageModulesApiInputs, templateCode);
        logger.log("templateCode-> " + ttdPartnerPageModule.templateCode);
        return ttdPartnerPageModule;
    }

    private String TTDGetPartnerId(TTDGetPartnerIdApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDGetPartnerIdApi ttdGetPartnerIdApi = new TTDGetPartnerIdApi(inputs);
        String partnerId = null;
        int count = 0;
        do {
            ApiResponse apiResponse = ttdGetPartnerIdApi.hitApi(apiTools);
            Gson gson = new Gson();
            TTDGetPartners ttdGetPartners = gson.fromJson(apiResponse.getBody(), TTDGetPartners.class);
            partnerId = ttdGetPartners.data.get(0).partnerId;
            Thread.sleep(1000);
            logger.log("retryCount : " + count);
            count++;
        } while (partnerId == null && count < 5);
        return partnerId;
    }

    private boolean checkIfModuleIsSuitedForTesting(TTDPartnerPageModules.Datum data, String templateCode) {
        boolean isModuleSuitedForTesting = false;
        logger.log("templateCode-------> : " + templateCode);
        if(!data.templateCode.equals(templateCode)) {
            return false;
        }
        switch (data.templateCode) {
            case "BANNER_GRID":
                if (data.moduleDetail.heading.title.contains(":"))
                    isModuleSuitedForTesting = true;
                break;
            case "BANNER_CAROUSEL":
                if ((data.dynamicContents.get(0).chips.get(0).buttonText) != null && (data.dynamicContents.get(0).chips.get(0).bannerContents.get(0).subtitle) != null && (data.dynamicContents.get(0).chips.get(0).bannerContents.get(0).subtitle).contains("Title") && data.moduleDetail.heading.title.contains("Automation")) {
                  isModuleSuitedForTesting = true;
               }
            break;
            //TODO add other condition ;
            default:
                if (data.moduleDetail.heading.title.contains("Automation"))
                    isModuleSuitedForTesting = true;
                break;
        }

        if (data.templateCode.equals(templateCode) && isModuleSuitedForTesting && data.dynamicContents.size() > 0) {
            isModuleSuitedForTesting = true;
        }
        return isModuleSuitedForTesting;
    }

    private TTDPartnerPageModules.Datum TTDGetPageModules(TTDGetPartnerPageModulesApi.Inputs inputs, String templateCode) throws ExecutionException, InterruptedException {
        TTDGetPartnerPageModulesApi ttdGetPartnerPageModulesApi = new TTDGetPartnerPageModulesApi(inputs);
        ArrayList<TTDPartnerPageModules.Datum> arrayData = null;
        int count = 0;
        do {
            ApiResponse apiResponse = ttdGetPartnerPageModulesApi.hitApi(apiTools);
            Gson gson = new Gson();
            TTDPartnerPageModules ttdPartnerPageModules = gson.fromJson(apiResponse.getBody(), TTDPartnerPageModules.class);
            arrayData = ttdPartnerPageModules.data;
            logger.log("retryCount : " + count);
            count++;
        } while (arrayData == null && count < 10);

        for (TTDPartnerPageModules.Datum data : arrayData) {
            if (checkIfModuleIsSuitedForTesting(data, templateCode)) {
                logger.log("templateCode"+templateCode);
                logger.log("data title: " + data.moduleDetail.heading.title);
                return data;
            }
        }
        return null;
    }

    @SneakyThrows
    public TTDPartnerPageModules.Datum getPartnerPageGridBannerTitleLogin(String username, String password, String keyword, String templateCode, String langCode, String appVersion) {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        logger.log("accessToken-> " + accessToken);

        TTDGetPartnerIdApi.Inputs ttdGetPartnerIdApiInput = new TTDGetPartnerIdApi.Inputs(accessToken, keyword);
        String partnerId = TTDGetPartnerId(ttdGetPartnerIdApiInput);
        logger.log("partnerId-> " + partnerId);

        String userAgent = CoreFunctions.userAgentValue() + " - " + appVersion;
        logger.log("userAgent-> " + userAgent);

        TTDGetPartnerPageModulesApi.Inputs ttdGetPartnerPageModulesApiInputs = new TTDGetPartnerPageModulesApi.Inputs(accessToken, partnerId, langCode, userAgent);
        TTDPartnerPageModules.Datum ttdPartnerPageModule = TTDGetPageModules(ttdGetPartnerPageModulesApiInputs, templateCode);
        logger.log("templateCode-> " + ttdPartnerPageModule.templateCode);
        return ttdPartnerPageModule;
    }

    @SneakyThrows
    public TTDPopularDestinationDataPOJO getPopularDestinationData(String language) {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        TTDPopularDestinationApi.Inputs TTDDestinationApiInputs = new TTDPopularDestinationApi.Inputs(accessToken,userAgent, getLanguage(language));
        TTDPopularDestinationApi ttdPopularDestinationApi = new TTDPopularDestinationApi(TTDDestinationApiInputs);
        ApiResponse apiResponse = hitPopularDestinationApi(ttdPopularDestinationApi);

        Gson gson = new Gson();
        TTDPopularDestinationDataPOJO result = gson.fromJson(apiResponse.getBody(), TTDPopularDestinationDataPOJO.class);

        return result;
    }

    private ApiResponse hitPopularDestinationApi(TTDPopularDestinationApi ttdPopularDestinationApi) throws ExecutionException, InterruptedException {
        return ttdPopularDestinationApi.hitApi(apiTools);
    }

    public String getPopularDestinationType(TTDPopularDestinationDataPOJO data, String paramType, String param) {
        String result = "";

        if (!data.data.isEmpty()) {
            switch (paramType.toLowerCase()) {
                case "name":
                    for (TTDPopularDestinationDataPOJO.DestinationData destinationData : data.data) {
                        if (destinationData.translations.get(0).name.equalsIgnoreCase(param)) {
                            result = destinationData.destinationType;
                            break;
                        }
                    }
                    break;
                case "index":
                    result += data.data.get(Integer.parseInt(param)).destinationType;
                    break;
                default:
                    logger.fail("Invalid paramType");
            }
        } else {
            throw new IllegalArgumentException("Data is empty");
        }

        return result;
    }

    private SubmitCreditCardResponse submitCreditCardResponse(SubmitCreditCardApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitCreditCardApi submitCreditCardApi = new SubmitCreditCardApi(inputs);
        ApiResponse submitCreditCard = submitCreditCardApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(submitCreditCard.getBody(), SubmitCreditCardResponse.class);
    }

    public String getLandingPageSectionName(TTDLandingPagePOJO.Data data) {
        return data.moduleDetail.heading.title;
    }

    public String getLandingPageSectionTitle(TTDLandingPagePOJO.Data data) {
        String productName = null;
        if (data.templateCode.contains("BANNER_CAROUSEL")) {
            productName = data.dynamicContents.get(0).chips.get(0).bannerContents.get(0).title;
        } else if (data.templateCode.contains("IMAGE_WITH_CHIPS") || data.templateCode.contains("FLASH_SALE")) {
            productName = data.dynamicContents.get(0).cards.get(0).productName;
        } else {
            throw new NotImplementedException("New TEMPLATE_CODE found : '"+data.templateCode+"'\n please contact this test case author for adjustment ");
        }
        return productName;
    }

    @SneakyThrows
    public String[] verifyNearbyDestinationType(List<String> nearbyDestinationText, String language) {
        List<String> destinationTypeChecked = new ArrayList<>();

        for(String s : nearbyDestinationText) {
            destinationTypeChecked.add(getDestinationType(s, language));
        }
        logger.log("destinationTypeChecked -> " + destinationTypeChecked);

        return destinationTypeChecked.toArray(new String[0]);
    }

    @SneakyThrows
    public String getDestinationType(String destinationName, String language) {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        String destinationType = getDestinationTypeByDestinationName(accessToken, userAgent, destinationName, language);
        logger.log("destinationType-> " + destinationType);

        return destinationType;
    }

    /**
     * Retrieves the public ID of a destination based on the destination name and language.
     *
     * @param destinationName the name of the destination to find the public ID for.
     * @param language the language in which the destination name is specified.
     * @return the public ID of the destination.
     * @throws Exception if an error occurs while retrieving the token or destination public ID.
     */
    @SneakyThrows
    public String getDestinationPublicId(String destinationName, String language) {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        String destinationPublicId = getDestinationPublicId(accessToken, userAgent, destinationName, language);
        logger.log("destinationPublicId-> " + destinationPublicId);

        return destinationPublicId;
    }

    /**
     * Retrieves the public ID of a destination based on the provided access token, user agent, location, and language.
     *
     * @param accessToken the access token required for authentication.
     * @param userAgent the user agent string to be used in the request.
     * @param location the name of the destination for which the public ID is to be retrieved.
     * @param language the language in which the destination name is specified.
     * @return the public ID of the destination if found, otherwise null.
     * @throws ExecutionException if an error occurs during the execution of the API request.
     * @throws InterruptedException if the API request is interrupted.
     */
    @SneakyThrows
    private String getDestinationPublicId(String accessToken, String userAgent, String location, String language) throws ExecutionException, InterruptedException {
        GETSearchSuggestionResultApi.Inputs inputs = new GETSearchSuggestionResultApi.Inputs(accessToken, userAgent, language, location);
        GETSearchSuggestionResultApi getLocationPublicIdApi = new GETSearchSuggestionResultApi(inputs);
        ApiResponse getSearchSuggestionResultResponse = getLocationPublicIdApi.hitApi(apiTools);

        Gson gson = new Gson();
        GETSearchSuggestionResultResponsePOJO getSearchSuggestionResultResponsePOJO = gson.fromJson(getSearchSuggestionResultResponse.getBody(), GETSearchSuggestionResultResponsePOJO.class);

        List<GETSearchSuggestionResultResponsePOJO.Destination> destinations = getSearchSuggestionResultResponsePOJO.data.destinations;

        for(GETSearchSuggestionResultResponsePOJO.Destination destination : destinations) {
            logger.log("destinationName -> " + destination.translations.get(0).title);
            if(destination.translations.get(0).title.equalsIgnoreCase(location)) {
                String jsonObject = gson.toJson(destination, GETSearchSuggestionResultResponsePOJO.Destination.class);
                logger.log("jsonObject -> " + jsonObject);
                logger.log("destinationPublicId -> " + destination.code);
                return destination.code;
            }
        }

        return null;
    }

    private String getDestinationTypeByDestinationName(String accessToken, String userAgent, String location, String language) throws ExecutionException, InterruptedException {
        GETSearchSuggestionResultApi.Inputs inputs = new GETSearchSuggestionResultApi.Inputs(accessToken, userAgent, language, location);
        GETSearchSuggestionResultApi getLocationPublicIdApi = new GETSearchSuggestionResultApi(inputs);
        ApiResponse getSearchSuggestionResultResponse = getLocationPublicIdApi.hitApi(apiTools);

        Gson gson = new Gson();
        GETSearchSuggestionResultResponsePOJO getSearchSuggestionResultResponsePOJO = gson.fromJson(getSearchSuggestionResultResponse.getBody(), GETSearchSuggestionResultResponsePOJO.class);

        List<GETSearchSuggestionResultResponsePOJO.Destination> destinations = getSearchSuggestionResultResponsePOJO.data.destinations;

        for(GETSearchSuggestionResultResponsePOJO.Destination destination : destinations) {
            logger.log("destinationName -> " + destination.translations.get(0).title);
            if(destination.translations.get(0).title.equalsIgnoreCase(location)) {
                logger.log("destinationSuggestionType -> " + destination.suggestionType);
                return destination.suggestionType;
            }
        }

        return null;
    }

    @SneakyThrows
    public double getEstimatedRefundRateCMS(String adminUsername, String adminPassword, String productName, String packageName, String refundReason, String bookedDateString) throws ExecutionException, InterruptedException {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        String[] productDisplayedTitle = productName.split("\\|");
        String[] productActualTitle = productDisplayedTitle[1].split("/");
        String language;
        if(productDisplayedTitle[0].equalsIgnoreCase(productActualTitle[0])) {
            language = "id";
        } else {
            language = "en";
        }
        productName = productDisplayedTitle[0];

        String productId = getProductIdByProductName(language, accessToken, userAgent, productName);
        logger.log("productId-> " + productId);

        String adminAccessToken = getAdminAccessTokenDashboardAuth(adminUsername, adminPassword);
        logger.log("adminAccessToken-> " + adminAccessToken);

        GETProductDetailResponsePOJO getProductDetailResponsePOJO = getProductDetailByProductId(language, adminAccessToken, userAgent, productId);

        String[] bookedDateStringArray = bookedDateString.split(" ");
        LocalDate todayLocalDate = LocalDate.now();

        Date todayDate = new Date(todayLocalDate.getYear()-1900, todayLocalDate.getMonthValue()-1,todayLocalDate.getDayOfMonth());
        Date bookedDate = new Date(Integer.parseInt(bookedDateStringArray[2])-1900, getMonthNumber(bookedDateStringArray[1])-1, Integer.parseInt(bookedDateStringArray[0]));

        long diffInMillies = Math.abs(bookedDate.getTime() - todayDate.getTime());
        int differenceDate = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        logger.log("differenceDate -> " + differenceDate);

        for(GETProductDetailResponsePOJO.Package packagePOJO : getProductDetailResponsePOJO.data.packages) {
            boolean targetPackageFound = false;
            for(GETProductDetailResponsePOJO.Translation1 packageTranslation : packagePOJO.translations) {
                logger.log("packageName -> " + packageTranslation.name);
                if(packageName.equalsIgnoreCase(packageTranslation.name) && packagePOJO.isRefundable) {
                    targetPackageFound = true;
                    break;
                }
            }

            if(!targetPackageFound) {
                continue;
            }


            for(GETProductDetailResponsePOJO.RefundReasonTier packageRefundReasonTier : packagePOJO.refundPriceRateResult.refundReasonTiers) {
                boolean targetRefundReasonFound = false;
                double refundPercentageResult = 0;
                for (GETProductDetailResponsePOJO.Translation2 packageRefundReasonTranslation : packageRefundReasonTier.translations) {
                    logger.log("packageRefundReasonTranslation -> " + packageRefundReasonTranslation.reasonName);
                    if(refundReason.equalsIgnoreCase(packageRefundReasonTranslation.reasonName)) {
                        targetRefundReasonFound = true;
                        break;
                    }
                }

                if(!targetRefundReasonFound) {
                    continue;
                }

                GETProductDetailResponsePOJO.Tier targetRefundTierPercentage = new GETProductDetailResponsePOJO.Tier();
                for(GETProductDetailResponsePOJO.Tier packageRefundTier : packageRefundReasonTier.tiers) {
                    int inDaysValue = 0;
                    switch(packageRefundTier.timeUnit) {
                        case "DAYS" :
                            inDaysValue += packageRefundTier.value;
                            break;
                        case "BEFORE_REDEEM":
                            logger.log("BEFORE_REDEEM time unit found, no need to calculate the days difference");
                            break;
                        default:
                            logger.log("Time unit '" + packageRefundTier.timeUnit + "' not yet supported");
                    }

                    if(inDaysValue <= differenceDate) {
                        if (targetRefundTierPercentage == null) {
                            targetRefundTierPercentage = packageRefundTier;
                            targetRefundTierPercentage.inDaysValue = inDaysValue;
                        } else if (targetRefundTierPercentage.inDaysValue < inDaysValue) {
                            targetRefundTierPercentage = packageRefundTier;
                            targetRefundTierPercentage.inDaysValue = inDaysValue;
                        }
                    }
                    logger.log("inDaysValue -> " + packageRefundTier.inDaysValue);
                    refundPercentageResult = packageRefundTier.refundPercentage;
                    logger.log("refundPercentage -> " + refundPercentageResult);
                }

                return refundPercentageResult;
            }
        }

        return 0;
    }

    @SneakyThrows
    private GETProductDetailResponsePOJO getProductDetailByProductId(String language, String adminAccessToken, String userAgent, String productId) throws ExecutionException, InterruptedException {
        GETProductDetailApi.Inputs inputs = new GETProductDetailApi.Inputs(language, adminAccessToken, userAgent, productId);
        GETProductDetailApi getProductDetailApi = new GETProductDetailApi(inputs);
        ApiResponse getProductDetailResponse = getProductDetailApi.hitApi(apiTools);
        logger.log("getProductDetailResponse -> " + getProductDetailResponse);

        Gson gson = new Gson();

        return gson.fromJson(getProductDetailResponse.getBody(), GETProductDetailResponsePOJO.class);
    }

    @SneakyThrows
    private String getProductIdByProductName(String language, String accessToken, String userAgent, String productName) throws ExecutionException, InterruptedException {
        GETSearchSuggestionResultApi.Inputs inputs = new GETSearchSuggestionResultApi.Inputs(accessToken, userAgent, language, productName);
        GETSearchSuggestionResultApi getLocationPublicIdApi = new GETSearchSuggestionResultApi(inputs);
        ApiResponse getSearchSuggestionResultResponse = getLocationPublicIdApi.hitApi(apiTools);
        logger.log("getSearchSuggestionResultResponse -> " + getSearchSuggestionResultResponse.getBody());

        Gson gson = new Gson();
        GETSearchSuggestionResultResponsePOJO getSearchSuggestionResultResponsePOJO = gson.fromJson(getSearchSuggestionResultResponse.getBody(), GETSearchSuggestionResultResponsePOJO.class);

        List<GETSearchSuggestionResultResponsePOJO.Product> products = getSearchSuggestionResultResponsePOJO.data.products;

        for(GETSearchSuggestionResultResponsePOJO.Product product : products) {
            logger.log("productName -> " + productName);
            logger.log("product -> " + product.translations.get(0).title);
            if(product.translations.get(0).title.equalsIgnoreCase(productName)) {
                logger.log("productId -> " + product.id);
                return product.id;
            }
        }

        return null;
    }

    @SneakyThrows
    public boolean getSupplierId(String env, String language, String productName, String code) throws ExecutionException, InterruptedException {
        String accessToken = getToken();
        logger.log("accessToken -> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent -> " + userAgent);

        TTDVerifySupplierIdKlook.Inputs inputs = new TTDVerifySupplierIdKlook.Inputs(accessToken, userAgent, language, productName, code);
        TTDVerifySupplierIdKlook ttVerifySupplierIdKlook = new TTDVerifySupplierIdKlook(inputs);
        ApiResponse apiResponse = ttVerifySupplierIdKlook.hitApi(apiTools);

        Gson gson = new Gson();

        String responseJson = apiResponse.getBody();
        SupplierId response = gson.fromJson(responseJson, SupplierId.class);

        for (SupplierId.Product product : response.getData()) {
            List<SupplierId.Image> images = product.getImages();
            if (images != null) {
                for (SupplierId.Image image : images) {
                    String supplierCode = image.getSupplierCode();
                    if(env.equalsIgnoreCase("Klook")){
                        if (supplierCode.equalsIgnoreCase("S12")) {
                            logger.log("Supplier Code found: " + supplierCode);
                            return true;
                        }
                    } else if (env.equalsIgnoreCase("bmg")) {
                        if (supplierCode.equalsIgnoreCase("S2")) {
                            logger.log("Supplier Code found: " + supplierCode);
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    
    public TTDLandingPagePOJO.Card getProductCardByPriceDecimal(List<TTDLandingPagePOJO.Data> listOfData, String mathComparator, double requirementPrice, String language, String currency) throws ExecutionException, InterruptedException {
        TTDLandingPagePOJO.Card result = null;

        for(TTDLandingPagePOJO.Data data : listOfData) {
            List<TTDLandingPagePOJO.Card> listOfCard = new ArrayList<>();

            switch (data.templateCode) {
                case "FLASH_SALE":
                    listOfCard = data.dynamicContents.get(0).cards;
                    break;
                case "IMAGE_WITH_CHIPS":
                    listOfCard = getAllCardImageWithChips(data, language, currency);
                    break;
                default:
                    logger.softWarning("Can't handle page module with template code '"+ data.templateCode +"'");
            }


            result = findProductCardByPriceDecimal(listOfCard, mathComparator,requirementPrice);
            if(result != null) {
                result.pageModuleTitle = data.dynamicContents.get(0).title;
                TTDLandingPagePOJO.Card finalResult = result;
                result.chipsName = data.dynamicContents.get(0).chips.stream().filter(chip -> chip.id.equalsIgnoreCase(finalResult.chipsId)).findFirst().orElseThrow().text;

                logger.log("\nproductName -> " + result.productName + "\ndiscountedPrice -> " + result.rateInfo.discountedPrice + "\npageModuleTitle -> " + result.pageModuleTitle + "\nchipsName -> " + result.chipsName);
                return result;
            }
        }

        return null;
    }
    
    public HashMap<String, String> getProductByPriceDecimalForYouSection(List<GETLandingPageForYouSectionPOJO.Data> dataList, String mathComparator, double requirementPrice) {
        HashMap<String, String> result = new HashMap<>();
        for (GETLandingPageForYouSectionPOJO.Data data : dataList) {
            double finalPrice = data.startingFinalPrice / 100.0;
            if (!compareDecimal(finalPrice, mathComparator, requirementPrice)) {
                continue;
            }
            logger.log("\nfinalPrice -> " + finalPrice + "\nstartingPrice -> " + data.startingFinalPrice+"\n Product Name -> " + data.translations.get(0).title);
            result.put("productName", data.translations.get(0).title);
            result.put("finalPrice", String.valueOf(finalPrice));
            return result;
        }
        return result;
    }

    private boolean compareDecimal(double actual, String comparator, double expected) {
        return switch (comparator) {
            case "<" -> actual < expected;
            case ">" -> actual > expected;
            case "<=" -> actual <= expected;
            case ">=" -> actual >= expected;
            case "==" -> Double.compare(actual, expected) == 0;
            default -> throw new IllegalArgumentException("Invalid comparator: " + comparator);
        };
    }

    private TTDLandingPagePOJO.Card findProductCardByPriceDecimal(List<TTDLandingPagePOJO.Card> listOfCard, String mathComparator, double requirementPrice) {
        double actualPrice;
        int dataSize = listOfCard.size();
        logger.log("dataSize -> " + dataSize);
        for (int i = 0; i < dataSize; i++) {
            TTDLandingPagePOJO.Card card = listOfCard.get(i);
            long price = card.rateInfo.discountedPrice;
            int scale = card.rateInfo.scale;
            actualPrice = price/(Math.pow(10,scale));

            switch (mathComparator) {
                case "<":
                    if(actualPrice > 10 && actualPrice % 1 < requirementPrice) {
                        card.rateInfo.formattedPrice = actualPrice;
                        return card;
                    }
                    break;
                case ">=":
                    if(actualPrice > 10 && actualPrice % 1 >= requirementPrice) {
                        card.rateInfo.formattedPrice = actualPrice;
                        return card;
                    }
                    break;
                default:
                    logger.fail("Math comparator '"+ mathComparator +"' is not supported yet");
            }
        }

        return null;
    }

    private List<TTDLandingPagePOJO.Card> getAllCardImageWithChips(TTDLandingPagePOJO.Data data, String language, String currency) throws ExecutionException, InterruptedException {
        List<TTDLandingPagePOJO.Card> listOfCard = new ArrayList<>();
        String pageModuleId = data.id;

        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        for(TTDLandingPagePOJO.Chip chip : data.dynamicContents.get(0).chips) {
            String chipId = chip.id;

            TTDLandingPageChipsApi.Inputs TTDLandingPageApiChipsInputs = new TTDLandingPageChipsApi.Inputs(accessToken, userAgent, getLanguage(language), pageModuleId, chipId, currency);
            TTDLandingPageChipsApi ttdLandingPageChipsApi = new TTDLandingPageChipsApi(TTDLandingPageApiChipsInputs);
            ApiResponse apiResponse = ttdLandingPageChipsApi.hitApi(apiTools);

            Gson gson = new Gson();
            JSONObject response = new JSONObject(apiResponse.getBody());
            JSONObject responseData = response.getJSONObject("data");

            TTDLandingPagePOJO.Data responseDataObject = gson.fromJson(responseData.toString(), TTDLandingPagePOJO.Data.class);
            listOfCard.addAll(responseDataObject.dynamicContents.get(0).cards);
        }

        return listOfCard;
    }

    @SneakyThrows
    public String[] getTTDSRPProductNameList(String productCategory, String productSubCategory, String modifier, String language, String currency) {
        GETProductListTTDSRPPOJO getProductListTTDSRPPOJO = getProductListTTDSRP(productCategory, productSubCategory, modifier, language, currency);

        List<String> productNameList = new ArrayList<>();
        for(GETProductListTTDSRPPOJO.ProductCard productCard : getProductListTTDSRPPOJO.data) {
            logger.log("productCard.translations.get(0).title.toLowerCase() -> " + productCard.translations.get(0).title.toLowerCase());
            productNameList.add(productCard.translations.get(0).title.toLowerCase());
        }

        return productNameList.toArray(new String[0]);
    }

    @SneakyThrows
    public GETProductListTTDSRPPOJO getProductListTTDSRP(String productCategory, String productSubCategory, String modifier, String language, String currency) {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        String productCategoryCodes = getProductCategoryCodes(accessToken, userAgent, productCategory, language, currency);
        logger.log("productCategoryCodes-> " + productCategoryCodes);

        String productSubCategoryIds = productSubCategory.equalsIgnoreCase("all") ? "" : getProductSubCategoryIds(accessToken, userAgent, productSubCategory, language, currency);
        logger.log("productSubCategoryIds-> " + productSubCategoryIds);

        String sortAttributes = getSortAttributeType(modifier);
        logger.log("sortAttributes-> " + sortAttributes);

        String sortDirection = getSortOrderType(modifier);
        logger.log("sortDirection-> " + sortDirection);

        return getTTDSRPProductList(accessToken, userAgent, productCategoryCodes, productSubCategoryIds, sortAttributes, sortDirection, language, currency);
    }

    @SneakyThrows
    public String[] getTTDSRPProductNameList(String productCategory, String modifier, String language, String currency, List<String> selectedPrice) {
        GETProductListTTDSRPPOJO getProductListTTDSRPPOJO = getSearchProductListTTDSRP(productCategory, modifier, language, currency,selectedPrice);

        List<String> productNameList = new ArrayList<>();
        for(GETProductListTTDSRPPOJO.ProductCard productCard : getProductListTTDSRPPOJO.data) {
            logger.log("productCard.translations.get(0).title.toLowerCase() -> " + productCard.translations.get(0).title.toLowerCase());
            productNameList.add(productCard.translations.get(0).title.toLowerCase());
        }

        return productNameList.toArray(new String[0]);
    }

    @SneakyThrows
    public GETProductListTTDSRPPOJO getSearchProductListTTDSRP(String productCategory, String modifier, String language, String currency,List<String> selectedPrice) {
        //price in api is in cents, so we need to multiply by 100
        int minPrice = Integer.parseInt(selectedPrice.get(0).replaceAll("[^0-9]", "")) * 100;
        int maxPrice = Integer.parseInt(selectedPrice.get(1).replaceAll("[^0-9]", "")) * 100;
        logger.log("minPrice -> " + minPrice + ", maxPrice -> " + maxPrice);

        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        String productCategoryCodes = getProductCategoryCodes(accessToken, userAgent, productCategory, language, currency);
        logger.log("productCategoryCodes-> " + productCategoryCodes);

        String sortAttributes = getSortAttributeType(modifier);
        logger.log("sortAttributes-> " + sortAttributes);

        String sortDirection = getSortOrderType(modifier);
        logger.log("sortDirection-> " + sortDirection);

        return getSearchTTDSRPProductList(accessToken, userAgent, productCategoryCodes, sortAttributes, sortDirection, language, currency, minPrice, maxPrice);
    }

    @SneakyThrows
    private GETProductListTTDSRPPOJO getTTDSRPProductList(String accessToken, String userAgent, String productCategoryCodes, String productSubCategoryIds, String sortAttributes, String sortDirection, String language, String currency) {
        GETProductListTTDSRPApi.Inputs getProductListTTDSRPApiInputs = new GETProductListTTDSRPApi.Inputs(accessToken, userAgent, productCategoryCodes, productSubCategoryIds, sortAttributes, sortDirection, language, currency);
        GETProductListTTDSRPApi getProductListTTDSRPApi = new GETProductListTTDSRPApi(getProductListTTDSRPApiInputs);
        ApiResponse apiResponse = getProductListTTDSRPApi.hitApi(apiTools);

        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), GETProductListTTDSRPPOJO.class);
    }

    @SneakyThrows
    private GETProductListTTDSRPPOJO getSearchTTDSRPProductList(String accessToken, String userAgent, String productCategoryCodes, String sortAttributes, String sortDirection, String language, String currency, int minPrice, int maxPrice) {
        GETSearchProductListTTDSRPApi.Inputs getSearchProductListTTDSRPApiInputs = new GETSearchProductListTTDSRPApi.Inputs(accessToken, userAgent, productCategoryCodes, sortAttributes, sortDirection, language, currency, minPrice, maxPrice);
        GETSearchProductListTTDSRPApi getSearchProductListTTDSRPApi = new GETSearchProductListTTDSRPApi(getSearchProductListTTDSRPApiInputs);
        ApiResponse apiResponse = getSearchProductListTTDSRPApi.hitApi(apiTools);

        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), GETProductListTTDSRPPOJO.class);
    }

    private String getSortOrderType(String modifier) {
        switch (modifier.toLowerCase()) {
            case "lowest price":
                return "ASC";
            case "highest price":
                return "DESC";
            default:
                logger.softWarning("Sort order type for modifier '"+ modifier.toLowerCase() +"' is not set yet.\n" +
                        "For now, it will be set to 'DESC' by default.");
        }

        return "DESC";
    }

    private String getSortAttributeType(String modifier) {
        switch (modifier.toLowerCase()) {
            case "lowest price":
                return "sortingFinalPrice";
            case "highest price":
                return "sortingFinalPrice";
            default:
                logger.softWarning("Sort order attributes for modifier '"+ modifier.toLowerCase() +"' is not set yet.\n" +
                        "For now, it will be set to 'popularityScore' by default.");
        }

        return "popularityScore";
    }

    private String getProductSubCategoryIds(String accessToken, String userAgent, String productSubCategory, String language, String currency) {
        //TODO: Implement Methods
        return null;
    }

    @SneakyThrows
    private String getProductCategoryCodes(String accessToken, String userAgent, String productCategory, String language, String currency) {
        GETProductCategoryCodesApi.Inputs getProductCategoryCodesApiInputs = new GETProductCategoryCodesApi.Inputs(accessToken, userAgent, getLanguage(language), currency);
        GETProductCategoryCodesApi getProductCategoryCodesApi = new GETProductCategoryCodesApi(getProductCategoryCodesApiInputs);
        ApiResponse apiResponse = getProductCategoryCodesApi.hitApi(apiTools);

        Gson gson = new Gson();
        GETProductCategoryCodesPOJO getProductCategoryCodesPOJO = gson.fromJson(apiResponse.getBody(), GETProductCategoryCodesPOJO.class);

        for(GETProductCategoryCodesPOJO.ProductCategory productCategoryElement : getProductCategoryCodesPOJO.data) {
            if(productCategoryElement.translations.get(0).name.toLowerCase().contains(productCategory.toLowerCase())) {
                return productCategoryElement.code;
            }
        }

        return null;
    }

    public void assertProductLocation(String productName, String expectedLocation, String language) {
        List<String> actualLocation = getProductLocation(productName, language);
        Assertion.assertThat("Product location is as expected", actualLocation, hasItem(expectedLocation.toLowerCase()));
    }

    @SneakyThrows
    private List<String> getProductLocation(String productName, String language) {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        GETSearchSuggestionResultApi.Inputs inputs = new GETSearchSuggestionResultApi.Inputs(accessToken, userAgent, language, productName);
        GETSearchSuggestionResultApi getLocationPublicIdApi = new GETSearchSuggestionResultApi(inputs);
        ApiResponse getSearchSuggestionResultResponse = getLocationPublicIdApi.hitApi(apiTools);
        logger.log("getSearchSuggestionResultResponse -> " + getSearchSuggestionResultResponse.getBody());

        Gson gson = new Gson();
        GETSearchSuggestionResultResponsePOJO getSearchSuggestionResultResponsePOJO = gson.fromJson(getSearchSuggestionResultResponse.getBody(), GETSearchSuggestionResultResponsePOJO.class);

        List<GETSearchSuggestionResultResponsePOJO.Product> products = getSearchSuggestionResultResponsePOJO.data.products;

        for(GETSearchSuggestionResultResponsePOJO.Product product : products) {
            logger.log("productName -> " + productName);
            logger.log("product -> " + product.translations.get(0).title);
            if(product.translations.get(0).title.equalsIgnoreCase(productName)) {
                logger.log("productId -> " + product.id);
                List<String> productLocations = new ArrayList<>();
                GETSearchSuggestionResultResponsePOJO.Translation productTranslation = product.translations.get(0);
                productLocations.add(productTranslation.area.toLowerCase());
                productLocations.add(productTranslation.city.toLowerCase());
                productLocations.add(productTranslation.region.toLowerCase());
                productLocations.add(productTranslation.country.toLowerCase());

                return productLocations;
            }
        }

        return null;
    }

    @SneakyThrows
    public List<String> getProductTitleOrder(String sectionType, String language, String username, String password) {
        AccountPage<?> accountPage = new AccountPage<>(session);
        String accessToken;
        String refreshToken;
        if (username.isEmpty() || password.isEmpty()) {
            LoginApi<?> loginApi = new LoginApi<>(session);
            accessToken = loginApi.getAllToken().data.accessToken;
            refreshToken = loginApi.getAllToken().data.refreshToken;
        } else {
            accessToken = accountPage.getAccessTokenUNM(username, password);
            refreshToken = "";
        }
        logger.log("accessToken-> " + accessToken);
        logger.log("refreshToken-> " + refreshToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        GETProductTitleOrderApi.Inputs inputs = new GETProductTitleOrderApi.Inputs(accessToken, userAgent, language, refreshToken);
        GETProductTitleOrderApi getProductTitleOrderApi = new GETProductTitleOrderApi(inputs);
        ApiResponse apiResponse = getProductTitleOrderApi.hitApi(apiTools);

        Gson gson = new Gson();
        GETProductTitleOrderPOJO getProductTitleOrderPOJO = gson.fromJson(apiResponse.getBody(), GETProductTitleOrderPOJO.class);

        List<GETProductTitleOrderPOJO.ProductList> productList = new ArrayList<>();
        List<String> result = new ArrayList<>();

        switch (sectionType) {
            case "new activities":
                productList = getProductTitleOrderPOJO.data.recentlyAdded;
                break;
            case "popular events":
                productList = getProductTitleOrderPOJO.data.popularEvents;
                break;
            case "popular attractions":
                productList = getProductTitleOrderPOJO.data.popularAttractions;
                break;
            default:
                logger.fail("Section type '"+ sectionType +"' is not supported yet");
                break;
        }

        for(GETProductTitleOrderPOJO.ProductList product : productList) {
            result.add(product.translations.get(0).title.toLowerCase());
        }

        logger.log("result -> " + result);
        return result;
    }

    /**
     * This method is used to get a list of destination titles from the provided TTDPopularDestinationDataPOJO object.
     *
     * @param ttdPopularDestinationDataPOJO The TTDPopularDestinationDataPOJO object that contains the destination data.
     * @return List<String> Returns a list of destination titles in lower case.
     */
    public List<String> getDestinationTitleList(TTDPopularDestinationDataPOJO ttdPopularDestinationDataPOJO) {
        List<String> result = new ArrayList<>();

        int i = 0;
        for(TTDPopularDestinationDataPOJO.DestinationData destinationData : ttdPopularDestinationDataPOJO.data) {
            logger.log(i + " destinationName -> " + destinationData.translations.get(0).name.toLowerCase());
            result.add(destinationData.translations.get(0).name.toLowerCase());
            i++;
        }

        return result;
    }

    /**
     * Retrieves the landing page group icon data based on the provided language.
     *
     * @param language the language in which the data is to be retrieved.
     * @return a GETLandingPageGroupPOJO object containing the landing page group icon data.
     * @throws Exception if an error occurs while retrieving the token or landing page group icon data.
     */
    @SneakyThrows
    public GETLandingPageGroupPOJO getLandingPageGroupIconData(String language){
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        GETLandingPageGroupIconApi.Inputs inputs = new GETLandingPageGroupIconApi.Inputs(accessToken, userAgent, language);
        GETLandingPageGroupIconApi getLandingPageGroupIconApi = new GETLandingPageGroupIconApi(inputs);
        ApiResponse apiResponse = getLandingPageGroupIconApi.hitApi(apiTools);

        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), GETLandingPageGroupPOJO.class);
    }

    @SneakyThrows
    public String getReviewTopic(String productUrlName,String lang){
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);
        TTDSelectProductApi.Inputs ttdProductSelectApiInputs = new TTDSelectProductApi.Inputs(accessToken, productUrlName);
        HashMap<String, String> selectedProductData = ttdSelectProduct(ttdProductSelectApiInputs, true);
        String selectedProductID = selectedProductData.get("productID");
        logger.log("selectedProductID-> " + selectedProductID);
        ReviewPage<T> reviewPage = ReviewPage.getInstance(this.session);
        return reviewPage.getReviewTopic(accessToken,"TODO",selectedProductID,lang);
    }

    @SneakyThrows
    public List<String> getReviewFromTopic(String productUrlName,String lang, String topic){
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);
        TTDSelectProductApi.Inputs ttdProductSelectApiInputs = new TTDSelectProductApi.Inputs(accessToken, productUrlName);
        HashMap<String, String> selectedProductData = ttdSelectProduct(ttdProductSelectApiInputs, true);
        String selectedProductID = selectedProductData.get("productID");
        logger.log("selectedProductID-> " + selectedProductID);
        ReviewPage<T> reviewPage = ReviewPage.getInstance(this.session);
        return reviewPage.getReviewFromTopic(accessToken,"TODO",selectedProductID,lang,topic);
    }

    @SneakyThrows
    public int getProductCount(String username, String password, boolean hasLoyalty, String productCategory, String language, String currency) {
        AccountPage<?> accountPage = new AccountPage<>(session);
        String accessToken;

        if (username.isEmpty() || password.isEmpty()) {
            LoginApi<?> loginApi = new LoginApi<>(session);
            accessToken = loginApi.getAllToken().data.accessToken;
        } else {
            accessToken = accountPage.getAccessTokenUNM(username, password);
        }
        logger.log("accessToken -> " + accessToken);
        accessToken = "session_access_token=" + accessToken;
        String userAgent = CoreFunctions.userAgentValue();

        String productCategoryCodes = getProductCategoryCodes(accessToken, userAgent, productCategory, language, currency);
        logger.log("productCategoryCodes-> " + productCategoryCodes);

        logger.log("userAgent -> " + userAgent);
        GETProductListCountTTDApi.Inputs inputs = new GETProductListCountTTDApi.Inputs(
                accessToken,
                userAgent,
                hasLoyalty,
                productCategoryCodes
        );

        GETProductListCountTTDApi getProductListCountTTDApi = new GETProductListCountTTDApi(inputs);
        ApiResponse apiResponse = getProductListCountTTDApi.hitApi(apiTools);

        logger.log("API Response Body -> " + apiResponse.getBody());
        Gson gson = new Gson();
        GetCountResponsePOJO responsePojo = gson.fromJson(apiResponse.getBody(), GetCountResponsePOJO.class);

        int count = responsePojo.getData();
        logger.log("Product Count -> " + count);

        return count;
    }

}
