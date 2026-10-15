package com.tiket.page.promo.gift_card_landing_page;

import com.google.gson.Gson;
import com.tiket.api.giftCard.*;
import com.tiket.core.CoreFunctions;
import com.tiket.apiPage.Unm;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.giftCard.*;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Instant;
import java.util.List;

public abstract class GiftCardPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", AppiumBy.xpath("//button[contains(@class, 'Button_variant_primary')]//span[text()='Log in']"));
    private static final Locator LOGIN_NOTICE = new Locator("LOGIN_NOTICE", AppiumBy.xpath("//h2[contains(text(), 'Log in') and contains(text(), 'gift card')]"));
    private static final Locator GIFT_CODE_INPUT = new Locator("GIFT_CODE_INPUT", AppiumBy.xpath("//input[@id='masukin-kode-penukaran' or @id='enter-the-redeem-code']"));
    private static final Locator REDEEM_CODE_BUTTON = new Locator("REDEEM_CODE_BUTTON", AppiumBy.xpath("//button[contains(@class, 'redeem_button')]"));
    private static final Locator ALREADY_REDEEMED_LABEL = new Locator("ALREADY_REDEEMED_LABEL", AppiumBy.xpath("//*[contains(text(), 'has been redeemed') or contains(text(), 'udah ditukar')]"));
    private static final Locator SUCCESSFUL_REDEEM_HEADER = new Locator("SUCCESSFUL_REDEEM_HEADER", AppiumBy.xpath("//h1[text()='Hore, kamu dapet hadiah!' or text()=\"Yay, you've received a gift!\"]"));
    private static final Locator SPLIT_OPTIONS = new Locator("SPLIT_OPTIONS", AppiumBy.xpath("//label[contains(@class, 'SplitVoucherForm_option')]"));
    private static final Locator SPLIT_OPTION_RADIO_BUTTONS = new Locator("SPLIT_OPTION_RADIO_BUTTONS", AppiumBy.xpath("//input[contains(@class, 'SplitVoucherForm_radio')]"));
    private static final Locator SPLIT_OPTION_SUBMIT_BUTTON = new Locator("SPLIT_OPTION_SUBMIT_BUTTON", AppiumBy.xpath("//div[contains(@class, 'SplitVoucherForm_card')]//button[@type='submit']"));
    private static final Locator GIFT_CARD_SCRATCHER = new Locator("GIFT_CARD_SCRATCHER", AppiumBy.xpath("//div[contains(@class, 'RedeemGiftCardFigure_scratch')]"));
    private static final Locator CLAIM_BUTTON = new Locator("CLAIM_BUTTON", AppiumBy.xpath("//button[contains(@class, 'OnboardingRedeemGiftCard_claim_button')]"));

    private static final String GIFT_CARD_URL_PATH = "gift-card";


    public GiftCardPage(Session<T> session) {
        super(session);
    }

    /**
     * The app keeps webviews from earlier tests alive, so switching by context alone lands on
     * whichever tiket.com window comes first. Target the gift card url instead.
     */
    protected void switchToGiftCardWebView() {
        driverTools.waitForWebview();
        if (driverTools.switchToWebViewUrl(GIFT_CARD_URL_PATH).isEmpty()) {
            driverTools.switchToWebView();
        }
    }

    public static <T extends WebDriver> GiftCardPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new GiftCardPageAndroid<>(session);
            }
            case IOS -> {
                return new GiftCardPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void clickRedeemGiftCard();

    public boolean isLoginDialogShown() {
        switchToGiftCardWebView();

        boolean value = driverTools.isElementPresent(LOGIN_BUTTON) && driverTools.isElementPresent(LOGIN_NOTICE);

        driverTools.switchToNative();
        return value;
    }

    public abstract void clickSendGiftCard();

    public void inputGiftCode(String giftCode) {
        switchToGiftCardWebView();

        WebElement giftCodeInput = driverTools.waitForElementToPresence(GIFT_CODE_INPUT).orElseThrow();
        giftCodeInput.sendKeys(giftCode);

        driverTools.hideKeyboard();
        driverTools.switchToNative();
    }

    public void clickCodeRedeemButton() {
        switchToGiftCardWebView();
        driverTools.clickElementByCoordinate(REDEEM_CODE_BUTTON);
        driverTools.switchToNative();
    }

    public boolean codeRedeemSuccessful() {
        switchToGiftCardWebView();
        boolean value = !driverTools.isElementDisplayed(ALREADY_REDEEMED_LABEL) && successfulRedeemPageAppears();
        driverTools.switchToNative();
        return value;
    }

    public boolean successfulRedeemPageAppears() {
        switchToGiftCardWebView();
        boolean value = driverTools.isElementDisplayed(SUCCESSFUL_REDEEM_HEADER);
        driverTools.switchToNative();
        return value;
    }

    public GiftCardOrderResponse createGiftCardOrderViaAPI(
            String currency, int voucherAmount,
            String profileId, String emailAddress, String fullName,
            String phone, String title,
            String loginEmail, String loginPassword
    ) {
        Gson gson = new Gson();

        GiftCardGetInventoryApi.Inputs giftCardGetInventoryApiInput = new GiftCardGetInventoryApi.Inputs(currency);
        GiftCardGetInventoryApi giftCardGetInventoryApi = new GiftCardGetInventoryApi(giftCardGetInventoryApiInput);
        ApiResponse inventoryApiResponse = giftCardGetInventoryApi.hitApi(apiTools);
        GiftCardInventoryResponse giftCardInventoryResponse = gson.fromJson(inventoryApiResponse.getBody(), GiftCardInventoryResponse.class);

        int randomIndex = CoreFunctions.generateRandomInt(0, giftCardInventoryResponse.data.inventories.size() - 1);
        String randomInventoryId = giftCardInventoryResponse.data.inventories.get(randomIndex).id;
        String searchKey = giftCardInventoryResponse.data.searchKey;

        GiftCardCreateCartApi.Inputs giftCardCreateCartApiInput = new GiftCardCreateCartApi.Inputs(searchKey, randomInventoryId, voucherAmount, currency, profileId, emailAddress, fullName, phone, title);
        GiftCardCreateCartApi giftCardCreateCartApi = new GiftCardCreateCartApi(giftCardCreateCartApiInput);
        ApiResponse cartApiResponse = giftCardCreateCartApi.hitApi(apiTools);
        GiftCardCartResponse giftCardCartResponse = gson.fromJson(cartApiResponse.getBody(), GiftCardCartResponse.class);

        String cartId = giftCardCartResponse.data.cartId;
        String cartSecret = giftCardCartResponse.data.cartSecret;

        LoginDataUnmResponse.Data loginData = new Unm<>(session).unmLoginApi(loginEmail, loginPassword);
        String accountId = String.valueOf(loginData.accountId);
        String username = loginData.accountUsername;
        String cookieHeader = buildGiftCardCookieHeader(loginData, currency);

        GiftCardCreateGCOrderApi.Inputs giftCardCreateGCOrderApiInput = new GiftCardCreateGCOrderApi.Inputs(
                cartId,
                cartSecret,
                Instant.now().toString(),
                profileId,
                emailAddress,
                fullName,
                phone,
                title,
                currency,
                fullName,
                "Asia/Calcutta",
                cookieHeader,
                accountId,
                username,
                ""
        );
        GiftCardCreateGCOrderApi giftCardCreateGCOrderApi = new GiftCardCreateGCOrderApi(giftCardCreateGCOrderApiInput);
        ApiResponse orderApiResponse = giftCardCreateGCOrderApi.hitApi(apiTools);
        JSONObject orderJson = new JSONObject(orderApiResponse.getBody());
        // A failed order carries no booking data, so the body is safe to log and is the only
        // way to see the backend's error code.
        if (orderJson.isNull("data")) {
            throw new IllegalStateException("Create gift card order returned no data. HTTP "
                    + orderApiResponse.getStatusCode() + ", body: " + orderApiResponse.getBody());
        }
        GiftCardOrderResponse orderResponse = gson.fromJson(orderJson.getJSONObject("data").toString(), GiftCardOrderResponse.class);
        logger.log("Created gift card order, HTTP " + orderApiResponse.getStatusCode()
                + ", orderId " + (orderResponse.order == null ? null : orderResponse.order.orderId));
        return orderResponse;
    }

    private String buildGiftCardCookieHeader(LoginDataUnmResponse.Data loginData, String currency) {
        StringBuilder cookie = new StringBuilder();
        appendCookie(cookie, "device_id", loginData.uniqueId);
        appendCookie(cookie, "uniqueId", loginData.uniqueId);
        appendCookie(cookie, "userlang", loginData.lang == null || loginData.lang.isBlank() ? "en" : loginData.lang);
        appendCookie(cookie, "tiket_currency", currency == null || currency.isBlank() ? "IDR" : currency.toUpperCase());
        appendCookie(cookie, "country_code", "id");
        appendCookie(cookie, "session_access_token", loginData.accessToken);
        appendCookie(cookie, "session_refresh_token", loginData.refreshToken);
        return cookie.toString();
    }

    private void appendCookie(StringBuilder cookie, String name, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        if (cookie.length() > 0) {
            cookie.append("; ");
        }
        cookie.append(name).append("=").append(value);
    }

    public String verifyGiftCardOrderPayment(String orderId, String orderHash) {
        GiftCardIssuanceApi.Inputs giftCardIssuanceApiInputs = new GiftCardIssuanceApi.Inputs(orderId, orderHash);
        GiftCardIssuanceApi giftCardIssuanceApi = new GiftCardIssuanceApi(giftCardIssuanceApiInputs);
        ApiResponse apiResponse = giftCardIssuanceApi.hitApi(apiTools);
        logger.log("Got response ->" + apiResponse);
        GiftCardIssuanceResponse giftCardIssuanceResponse = new Gson().fromJson(apiResponse.getBody(), GiftCardIssuanceResponse.class);
        return giftCardIssuanceResponse.data.bookingStatus;
    }

    public String verifyissuance(String orderId, String orderHash) {
        GiftCardGetOrderDetailsApi.Inputs giftCardGetOrderDetailsApiInputs = new GiftCardGetOrderDetailsApi.Inputs(orderId, orderHash);
        GiftCardGetOrderDetailsApi giftCardGetOrderDetailsApi = new GiftCardGetOrderDetailsApi(giftCardGetOrderDetailsApiInputs);
        ApiResponse apiResponse = giftCardGetOrderDetailsApi.hitApi(apiTools);
        logger.log("Got response ->" + apiResponse);
        JSONObject jsonData = new JSONObject(apiResponse.getBody());
        GiftCardOrderDetailsResponse giftCardOrderDetailsResponse = new Gson().fromJson(jsonData.getJSONObject("data").toString(), GiftCardOrderDetailsResponse.class);
        return giftCardOrderDetailsResponse.bookingStatus;
    }

    public String getRedeemCode(String orderId, String orderHash) {
        GiftCardGetOrderDetailsApi.Inputs giftCardGetOrderDetailsApiInputs = new GiftCardGetOrderDetailsApi.Inputs(orderId, orderHash);
        GiftCardGetOrderDetailsApi giftCardGetOrderDetailsApi = new GiftCardGetOrderDetailsApi(giftCardGetOrderDetailsApiInputs);
        ApiResponse apiResponse = giftCardGetOrderDetailsApi.hitApi(apiTools);
        logger.log("Got response ->" + apiResponse);
        JSONObject jsonData = new JSONObject(apiResponse.getBody());
        GiftCardOrderDetailsResponse giftCardOrderDetailsResponse = new Gson().fromJson(jsonData.getJSONObject("data").toString(), GiftCardOrderDetailsResponse.class);
        String redeemCode = giftCardOrderDetailsResponse.inventories.get(0).bookingItems.get(0).redeemCode;
        logger.log("Redeem Code: " + redeemCode);
        return redeemCode;
    }

    public void scratchGiftCard() {
        switchToGiftCardWebView();
        driverTools.click(GIFT_CARD_SCRATCHER);
        driverTools.clickWithJavascriptExecutor(CLAIM_BUTTON);
        driverTools.switchToNative();
    }

    public boolean verifySplitOption() {
        switchToGiftCardWebView();
        boolean value = driverTools.isElementDisplayed(SPLIT_OPTIONS);
        driverTools.switchToNative();
        return value;
    }

    public void splitGiftCard() {
        switchToGiftCardWebView();
        List<WebElement> radioButtons = driverTools.elements(SPLIT_OPTION_RADIO_BUTTONS).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CoreFunctions.getRandomObject(radioButtons));

        driverTools.clickWithJavascriptExecutor(SPLIT_OPTION_SUBMIT_BUTTON);
        driverTools.switchToNative();
    }
}
