package com.tiket.web.dashboard.page.payment;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.bson.Document;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.tiket.api.payment.PaymentConfigApi;
import com.tiket.api.payment.PaymentGroupApi;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.payment.PaymentConfigResponse;
import com.tiket.pojo.payment.PaymentGroupResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class PaymentMethods<T extends WebDriver> extends BasePage<T> {
    private static final Gson gson = new Gson();
    private static final List<String> PLATFORMS = List.of("IOS", "ANDROID", "MOBILE", "DESKTOP");

    private static final Locator PAYMENT_METHOD_MASTER_SIDEMENU = new Locator("PAYMENT_METHOD_MASTER_SIDEMENU", new By.ByXPath("//*[@data-testid='sidemenu']//*[contains(text(),'Payment Method Master')]"));
    private static final Locator PAYMENT_METHODS_SUBMENU = new Locator("PAYMENT_METHODS_SUBMENU", new By.ByXPath("//*[contains(@data-testid,'submenu')]//*[contains(text(),'Payment Methods')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//*[@data-testid='btn-save']"));
    private static final Locator SNACKBAR_WRAPPER = new Locator("SNACKBAR_WRAPPER", new By.ByXPath("//*[contains(@class,'snackbar_wrapper')]"));
    private static final Locator ACTIVE_INACTIVE_TOGGLE_LABEL = new Locator("ACTIVE_INACTIVE_TOGGLE_LABEL", new By.ByXPath("//span[normalize-space()='Active / Inactive']/ancestor::div[contains(@class,'justify-between')][1]//label"));
    private static final Locator REASON_BAHASA = new Locator("REASON_BAHASA", new By.ByXPath("//*[@id='reason-in-bahasa-indonesia']"));
    private static final Locator REASON_ENGLISH = new Locator("REASON_ENGLISH", new By.ByXPath("//*[@id='reason-in-english']"));
    private static final Locator MODAL_SUBMIT_BUTTON = new Locator("MODAL_SUBMIT_BUTTON", new By.ByXPath("//footer[contains(@class,'modal_footer')]//button[normalize-space()='Submit']"));
    private static final Locator SET_DEACTIVATION_SCHEDULE_TOGGLE_LABEL = new Locator("SET_DEACTIVATION_SCHEDULE_TOGGLE_LABEL", new By.ByXPath("//span[normalize-space()='Set Deactivation Schedule']/ancestor::div[contains(@class,'justify-between')][1]//label"));
    private static final Locator DEACTIVATION_PERIOD_LABEL = new Locator("DEACTIVATION_PERIOD_LABEL", new By.ByXPath("//p[normalize-space()='Deactivation Period']"));
    private static final Locator DEACTIVATION_FROM_DATE = new Locator("DEACTIVATION_FROM_DATE", new By.ByXPath("//p[normalize-space()='Deactivation Period']/following-sibling::div[1]//input"));
    private static final Locator DEACTIVATION_TO_DATE = new Locator("DEACTIVATION_TO_DATE", new By.ByXPath("//p[normalize-space()='Deactivation Period']/following-sibling::div[2]//input"));
    private static final Locator DEACTIVATION_SCHEDULE_LABEL = new Locator("DEACTIVATION_SCHEDULE_LABEL", new By.ByXPath("//p[normalize-space()='Deactivation Schedule']"));
    private static final Locator REPETITION_DROPDOWN = new Locator("REPETITION_DROPDOWN", new By.ByXPath("//input[@placeholder='Repetition']"));
    private static final Locator SCHEDULE_MESSAGE_ENGLISH = new Locator("SCHEDULE_MESSAGE_ENGLISH", new By.ByXPath("//*[@id='enter-the-message-in-english']"));
    private static final Locator SCHEDULE_MESSAGE_BAHASA = new Locator("SCHEDULE_MESSAGE_BAHASA", new By.ByXPath("//*[@id='enter-the-message-in-bahasa-indonesia']"));
    private static final Locator IMPORTANT_INFO_LABEL = new Locator("IMPORTANT_INFO_LABEL", new By.ByXPath("//span[normalize-space()='Important Info']"));
    private static final Locator PAYMENT_METHOD_CONFIGURATION_PAGE_TITLE = new Locator("PAYMENT_METHOD_CONFIGURATION_PAGE_TITLE", new By.ByXPath("//h1[normalize-space()='Payment Method Configuration']"));

    public static final List<String> PLATFORM_COLUMNS = List.of("iOS", "Android", "Mobile", "Desktop");

    Map<String, String> columnToId = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("Payment Method", "paymentMethodName"),
            new AbstractMap.SimpleEntry<>("Status", "paymentStatus"),
            new AbstractMap.SimpleEntry<>("Next Deactivation Time", "nextTurnOff"),
            new AbstractMap.SimpleEntry<>("Repetition", "repeat"),
            new AbstractMap.SimpleEntry<>("iOS", "ios"),
            new AbstractMap.SimpleEntry<>("Android", "android"),
            new AbstractMap.SimpleEntry<>("Mobile", "mobile"),
            new AbstractMap.SimpleEntry<>("Desktop", "desktop"),
            new AbstractMap.SimpleEntry<>("Message for (DE) Deactivation", "message"),
            new AbstractMap.SimpleEntry<>("Configure", "action")
    );

    public PaymentMethods(Session<T> session) {
        super(session);
    }

    private String paymentMethodRowXPath(String paymentMethod) {
        String paymentMethodLower = paymentMethod.toLowerCase();
        return "//tr[contains(@data-testid,'row-key')][.//td[contains(@data-testid,'paymentMethodName')]//*[translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='"
                + paymentMethodLower + "']]";
    }

    private Locator getCellByMethod(String paymentMethod, String columnName) {
        String columnKey = columnToId.get(columnName);
        return new Locator(
                "CELL_" + columnKey.toUpperCase() + "_BY_METHOD_" + paymentMethod.toUpperCase(),
                By.xpath(paymentMethodRowXPath(paymentMethod) + "//td[contains(@data-testid,'-" + columnKey + "')]")
        );
    }

    private Locator getSetScheduleByMethod(String paymentMethod) {
        return new Locator(
                "SET_SCHEDULE_BY_METHOD_" + paymentMethod.toUpperCase(),
                By.xpath(paymentMethodRowXPath(paymentMethod) + "//*[normalize-space()='Set schedule']")
        );
    }

    private Locator getPaymentStatusTextByMethod(String paymentMethod) {
        return new Locator(
                "STATUS_TEXT_BY_METHOD_" + paymentMethod.toUpperCase(),
                By.xpath(paymentMethodRowXPath(paymentMethod) + "//td[contains(@data-testid,'paymentStatus')]//span")
        );
    }

    private Locator getPlatformToggleByMethod(String paymentMethod, String platform) {
        String columnKey = columnToId.get(platform);
        return new Locator(
                "PLATFORM_TOGGLE_" + columnKey.toUpperCase() + "_BY_METHOD_" + paymentMethod.toUpperCase(),
                By.xpath(paymentMethodRowXPath(paymentMethod) + "//td[contains(@data-testid,'-" + columnKey + "')]//label")
        );
    }

    private Locator getImportantInfoEditor(String lang) {
        return new Locator("IMPORTANT_INFO_EDITORS", new By.ByXPath("//*[contains(@class,'ql-editor')][contains(@data-placeholder,'"+lang+"')]"));
    }

    private Locator getButtonByText(String text) {
        return new Locator("BUTTON_"+text.toUpperCase(), new By.ByXPath("//*[contains(text(),'"+text+"')]"));
    }

    public void openPaymentMethodsConfig() {
        driverTools.click(PAYMENT_METHOD_MASTER_SIDEMENU);
        driverTools.click(PAYMENT_METHODS_SUBMENU);
    }

    public void openConfigPaymentMethod(String paymentMethod) {
        WebElement action = driverTools.waitForElementToAppear(getCellByMethod(paymentMethod, "Configure")).orElseThrow();
        driverTools.scrollElementToCenter(action);
        driverTools.waitForElementToBeClickable(action).ifPresentOrElse(driverTools::click, () -> {
            throw new RuntimeException("Unable to click element" + action);
        });
    }

    public void openSetScheduleInPayMethodConfig(String paymentMethod) {
        WebElement action = driverTools.waitForElementToAppear(getSetScheduleByMethod(paymentMethod)).orElseThrow();
        driverTools.scrollElementToCenter(action);
        driverTools.waitForElementToBeClickable(action).ifPresentOrElse(driverTools::click, () -> {
            throw new RuntimeException("Unable to click element" + action);
        });
    }

    public void openSetSchedule(String paymentMethod) {
        openSetScheduleInPayMethodConfig(paymentMethod);
    }

    public String getPaymentStatusText(String paymentMethod) {
        WebElement status = driverTools.waitForElementToAppear(getPaymentStatusTextByMethod(paymentMethod)).orElseThrow();
        driverTools.scrollElementToCenter(status);
        return driverTools.getText(status).trim();
    }

    private WebElement getActiveInactiveCheckbox() {
        WebElement toggle = driverTools.waitForElementToAppear(ACTIVE_INACTIVE_TOGGLE_LABEL).orElseThrow();
        driverTools.scrollElementToCenter(toggle);
        return driverTools.findElementInElement(toggle, By.cssSelector("input[type='checkbox']"));
    }

    public boolean isActiveInactiveToggleClickable() {
        return getActiveInactiveCheckbox().isEnabled();
    }

    public boolean isActiveInactiveToggleOn() {
        return getActiveInactiveCheckbox().isSelected();
    }

    private WebElement getSetDeactivationScheduleCheckbox() {
        WebElement toggle = driverTools.waitForElementToAppear(SET_DEACTIVATION_SCHEDULE_TOGGLE_LABEL).orElseThrow();
        driverTools.scrollElementToCenter(toggle);
        return driverTools.findElementInElement(toggle, By.cssSelector("input[type='checkbox']"));
    }

    public boolean isSetDeactivationScheduleToggleOn() {
        return getSetDeactivationScheduleCheckbox().isSelected();
    }

    public void clickSetDeactivationScheduleToggle() {
        WebElement toggle = driverTools.waitForElementToAppear(SET_DEACTIVATION_SCHEDULE_TOGGLE_LABEL).orElseThrow();
        driverTools.scrollElementToCenter(toggle);
        driverTools.click(toggle);
    }

    public void setSetDeactivationScheduleToggle(boolean on) {
        if (isSetDeactivationScheduleToggleOn() != on) {
            clickSetDeactivationScheduleToggle();
        }
    }

    public void verifyDeactivationScheduleElementsVisible() {
        Assertion.assertThat("Deactivation Period is visible", isElementVisible(DEACTIVATION_PERIOD_LABEL), is(true));
        Assertion.assertThat("Deactivation From date is visible", isElementVisible(DEACTIVATION_FROM_DATE), is(true));
        Assertion.assertThat("Deactivation To date is visible", isElementVisible(DEACTIVATION_TO_DATE), is(true));
        Assertion.assertThat("Deactivation Schedule is visible", isElementVisible(DEACTIVATION_SCHEDULE_LABEL), is(true));
        Assertion.assertThat("Repetition dropdown is visible", isElementVisible(REPETITION_DROPDOWN), is(true));
        Assertion.assertThat("Schedule message English is visible", isElementVisible(SCHEDULE_MESSAGE_ENGLISH), is(true));
        Assertion.assertThat("Schedule message Bahasa is visible", isElementVisible(SCHEDULE_MESSAGE_BAHASA), is(true));
    }

    public void verifyImportantInfoSectionVisible() {
        Assertion.assertThat("Important Info section is visible", isElementVisible(IMPORTANT_INFO_LABEL), is(true));
        Assertion.assertThat("Important Info English editor is visible", isElementVisible(getImportantInfoEditor("English")), is(true));
        Assertion.assertThat("Important Info Indonesia editor is visible", isElementVisible(getImportantInfoEditor("Indonesia")), is(true));
    }

    private boolean isElementVisible(Locator locator) {
        return driverTools.waitForElementToAppear(locator).isPresent();
    }

    public void editImportantInfo(String lang, String message){
        WebElement editor = driverTools.waitForElementToAppear(getImportantInfoEditor(lang)).orElseThrow();
        driverTools.clearInput(editor);
        driverTools.sendKeys(editor, message);
    }

    public String getImportantInfoText(String lang) {
        WebElement editor = driverTools.waitForElementToAppear(getImportantInfoEditor(lang)).orElseThrow();
        return driverTools.getText(editor);
    }

    public void saveChanges() {
        driverTools.scrollElementToCenter(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    public void clickButton(String text) {
        WebElement button = driverTools.waitForElementToAppear(getButtonByText(text)).orElseThrow();
        driverTools.scrollElementToCenter(button);
        driverTools.click(button);
    }

    public void clickPlatformToggle(String paymentMethod, String platform) {
        WebElement toggle = driverTools.waitForElementToAppear(getPlatformToggleByMethod(paymentMethod, platform)).orElseThrow();
        driverTools.scrollElementToCenter(toggle);
        driverTools.click(toggle);
        clickButton("Yes");
    }

    public boolean isPlatformToggleEnabled(String paymentMethod, String platform) {
        WebElement cell = driverTools.waitForElementToAppear(getCellByMethod(paymentMethod, platform)).orElseThrow();
        driverTools.scrollElementToCenter(cell);
        WebElement input = driverTools.findElementInElement(cell, By.cssSelector("input[type='checkbox']"));
        return input.isSelected();
    }

    public void verifyAllPlatformTogglesEnabled(String paymentMethod, boolean expected) {
        for (String platform : PLATFORM_COLUMNS) {
            Assertion.assertThat(
                    "Platform toggle " + platform + " for " + paymentMethod,
                    isPlatformToggleEnabled(paymentMethod, platform),
                    is(expected)
            );
        }
    }

    public void setAllPlatformToggles(String paymentMethod, boolean enabled) {
        for (String platform : PLATFORM_COLUMNS) {
            if (isPlatformToggleEnabled(paymentMethod, platform) != enabled) {
                clickPlatformToggle(paymentMethod, platform);
            }
        }
    }

    public String getBrowserCookieHeader() {
        StringBuilder cookieHeader = new StringBuilder();
        for (Cookie cookie : driver.manage().getCookies()) {
            if (cookieHeader.length() > 0) {
                cookieHeader.append("; ");
            }
            cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue());
        }
        return cookieHeader.toString();
    }

    public String getSnackbarMessage() {
        Optional<WebElement> snackbar = driverTools.waitForElementToAppear(SNACKBAR_WRAPPER);
        String message = snackbar.map(WebElement::getText).orElse("");
        driverTools.waitForElementToDisappear(SNACKBAR_WRAPPER);
        return message;
    }

    public boolean getStatusPerChannel(String query, String platform) {
        String documentString = getDocumentOnPaymentConfig(query).toJson();
        JSONObject documentJson = new JSONObject(documentString);
        return documentJson.getJSONObject("statusPerChannel").getBoolean(platform.toUpperCase());
    }

    public void clickToggleActiveInactive() {
        WebElement toggle = driverTools.waitForElementToAppear(ACTIVE_INACTIVE_TOGGLE_LABEL).orElseThrow();
        driverTools.scrollElementToCenter(toggle);
        driverTools.click(toggle);
    }

    public void clickModalSubmitButton() {
        WebElement button = driverTools.waitForElementToAppear(MODAL_SUBMIT_BUTTON).orElseThrow();
        driverTools.scrollElementToCenter(button);
        driverTools.click(button);
    }

    public void waitForPaymentMethodConfigurationPage() {
        driverTools.waitForElementToAppear(PAYMENT_METHOD_CONFIGURATION_PAGE_TITLE).orElseThrow(
                () -> new NoSuchElementException("Payment Method Configuration page not loaded")
        );
    }

    public void submitScheduleChanges() {
        saveChanges();
        clickModalSubmitButton();
        waitForPaymentMethodConfigurationPage();
    }

    public void deactivatePaymentMethodOnSchedule(String reasonBahasa, String reasonEnglish) {
        if (isActiveInactiveToggleOn()) {
            clickToggleActiveInactive();
            setReasonDeactivation(reasonBahasa, reasonEnglish);
        }
        submitScheduleChanges();
    }

    public void activatePaymentMethodOnSchedule() {
        if (!isActiveInactiveToggleOn()) {
            clickToggleActiveInactive();
        }
        submitScheduleChanges();
    }

    public void setReasonDeactivation(String reasonBahasa, String reasonEnglish) {
        WebElement reasonInputBahasa = driverTools.waitForElementToAppear(REASON_BAHASA).orElseThrow();
        WebElement reasonInputEnglish = driverTools.waitForElementToAppear(REASON_ENGLISH).orElseThrow();
        driverTools.clearInput(reasonInputBahasa);
        driverTools.sendKeys(reasonInputBahasa, reasonBahasa);
        driverTools.clearInput(reasonInputEnglish);
        driverTools.sendKeys(reasonInputEnglish, reasonEnglish);
    }

    public boolean getPaymentStatus(String query) {
        String documentString = getDocumentOnPaymentConfig(query).toJson();
        JSONObject documentJson = new JSONObject(documentString);
        return documentJson.getBoolean("paymentStatus");
    }

    public Document getDocumentOnPaymentConfig(String query) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDB("mongo", "tix_payment_core", "payment_config", query);
        List<Document> listResult = new ArrayList<>();
        result.into(listResult);
        if (listResult.isEmpty()) {
            throw new NoSuchElementException("Document not found for query: " + query);
        }
        return listResult.get(0);
    }

    @SneakyThrows
    public PaymentConfigResponse getPaymentConfigFromApi(String currency) {
        PaymentConfigApi paymentConfigApi = new PaymentConfigApi(new PaymentConfigApi.Inputs(getBrowserCookieHeader(), currency));
        ApiResponse apiResponse = paymentConfigApi.hitApi(apiTools);
        logger.log("debug : " + apiResponse.getBody());
        return gson.fromJson(apiResponse.getBody(), PaymentConfigResponse.class);
    }

    public String findPaymentMethodWithAllPlatformsEnabled(String currency) {
        PaymentConfigResponse response = getPaymentConfigFromApi(currency);
        if (response.data == null || response.data.content == null) {
            throw new NoSuchElementException("Payment config API returned empty data for currency " + currency);
        }
        for (PaymentConfigResponse.PaymentConfig config : response.data.content) {
            if (config.paymentName != null && config.paymentStatus && hasAllPlatformsEnabled(config.statusPerChannel)) {
                return config.paymentName;
            }
        }
        throw new NoSuchElementException("No payment method found with all platforms enabled for currency " + currency);
    }

    private boolean hasAllPlatformsEnabled(Map<String, Boolean> statusPerChannel) {
        if (statusPerChannel == null) {
            return false;
        }
        for (String channel : PLATFORMS) {
            if (!Boolean.TRUE.equals(statusPerChannel.get(channel))) {
                return false;
            }
        }
        return true;
    }

    public String preparePaymentMethodForConfigurationTest(String currency, String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isBlank()) {
            paymentMethod = findPaymentMethodWithAllPlatformsEnabled(currency);
        }
        if (!isPaymentMethodActiveFromConfigApi(currency, paymentMethod)) {
            openSetSchedule(paymentMethod);
            activatePaymentMethodOnSchedule();
        }
        if (!isAllPlatformsEnabledFromConfigApi(currency, paymentMethod)) {
            setAllPlatformToggles(paymentMethod, true);
        }
        verifyAllPlatformsEnabledFromConfigApi(currency, paymentMethod);
        verifyAllPlatformTogglesEnabled(paymentMethod, true);
        return paymentMethod;
    }

    public void disableEachPlatformCumulativelyAndVerifyGroupApi(
            String paymentMethod,
            String orderHash,
            String referenceId,
            String accessToken,
            String currency,
            String countryCode
    ) {
        boolean iosEnabled = true;
        boolean androidEnabled = true;
        boolean mobileEnabled = true;
        boolean desktopEnabled = true;

        for (String platform : PLATFORM_COLUMNS) {
            clickPlatformToggle(paymentMethod, platform);
            if ("iOS".equals(platform)) {
                iosEnabled = false;
            } else if ("Android".equals(platform)) {
                androidEnabled = false;
            } else if ("Mobile".equals(platform)) {
                mobileEnabled = false;
            } else if ("Desktop".equals(platform)) {
                desktopEnabled = false;
            }
            assertPaymentMethodStatusOnAllPlatforms(
                    orderHash, referenceId, accessToken, paymentMethod, currency, countryCode,
                    iosEnabled, androidEnabled, mobileEnabled, desktopEnabled
            );
        }
    }

    public void verifyPaymentListingStatus(String paymentMethod, String expectedStatus) {
        Assertion.assertThat(
                "Verify payment status on listing",
                getPaymentStatusText(paymentMethod),
                equalToIgnoringCase(expectedStatus)
        );
    }

    public PaymentConfigResponse.PaymentConfig getPaymentConfigByMethod(String currency, String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new IllegalArgumentException("paymentMethod must not be null or blank");
        }
        PaymentConfigResponse response = getPaymentConfigFromApi(currency);
        if (response.data == null || response.data.content == null) {
            throw new NoSuchElementException("Payment config API returned empty data for currency " + currency);
        }
        for (PaymentConfigResponse.PaymentConfig config : response.data.content) {
            if (config.paymentName != null && paymentMethod.equalsIgnoreCase(config.paymentName)) {
                return config;
            }
        }
        throw new NoSuchElementException("Payment method not found in config API: " + paymentMethod);
    }

    public boolean isPaymentMethodActiveFromConfigApi(String currency, String paymentMethod) {
        PaymentConfigResponse.PaymentConfig config = getPaymentConfigByMethod(currency, paymentMethod);
        return config.paymentStatus;
    }

    public boolean isAllPlatformsEnabledFromConfigApi(String currency, String paymentMethod) {
        PaymentConfigResponse.PaymentConfig config = getPaymentConfigByMethod(currency, paymentMethod);
        return config.paymentStatus && hasAllPlatformsEnabled(config.statusPerChannel);
    }

    public void verifyAllPlatformsEnabledFromConfigApi(String currency, String paymentMethod) {
        PaymentConfigResponse.PaymentConfig config = getPaymentConfigByMethod(currency, paymentMethod);
        Assertion.assertThat("Verify payment status from config API", config.paymentStatus, is(true));
        for (String channel : PLATFORMS) {
            Assertion.assertThat(
                    "Verify " + channel + " status from config API",
                    config.statusPerChannel != null && Boolean.TRUE.equals(config.statusPerChannel.get(channel)),
                    is(true)
            );
        }
    }

    public PaymentGroupResponse getPaymentGroupFromApi(
            String orderHash,
            String referenceId,
            String platform,
            String accessToken,
            String currency,
            String countryCode
    ) {
        PaymentGroupApi paymentGroupApi = new PaymentGroupApi(new PaymentGroupApi.Inputs(orderHash, referenceId, platform, accessToken, currency, countryCode));
        ApiResponse apiResponse = paymentGroupApi.hitApi(apiTools);
        logger.log("debug : " + apiResponse.getBody());
        return gson.fromJson(apiResponse.getBody(), PaymentGroupResponse.class);
    }

    public boolean getPaymentMethodStatusFromGroupApi(
            String orderHash,
            String referenceId,
            String platform,
            String accessToken,
            String paymentMethodName,
            String currency,
            String countryCode
    ) {
        PaymentGroupResponse response = getPaymentGroupFromApi(orderHash, referenceId, platform, accessToken, currency, countryCode);
        Boolean status = findPaymentMethodStatus(response, paymentMethodName);
        if (status == null) {
            throw new RuntimeException("Payment method " + paymentMethodName + " not found in payment group response");
        }
        return status;
    }

    private Boolean findPaymentMethodStatus(PaymentGroupResponse response, String paymentMethodName) {
        if (response.data == null) {
            return null;
        }
        if (response.data.commonGroup != null) {
            for (PaymentGroupResponse.PaymentGroup group : response.data.commonGroup) {
                Boolean status = findPaymentMethodStatusInItems(group.items, paymentMethodName);
                if (status != null) {
                    return status;
                }
            }
        }
        if (response.data.promotedGroup != null) {
            return findPaymentMethodStatusInItems(response.data.promotedGroup.items, paymentMethodName);
        }
        return null;
    }

    private PaymentGroupResponse.PaymentMethodItem findPaymentMethodItemInItems(List<PaymentGroupResponse.PaymentMethodItem> items, String paymentMethodName) {
        if (items == null) {
            return null;
        }
        for (PaymentGroupResponse.PaymentMethodItem item : items) {
            if (paymentMethodName.equalsIgnoreCase(item.paymentMethod)
                    || paymentMethodName.equalsIgnoreCase(item.paymentName)) {
                return item;
            }
        }
        return null;
    }

    private Boolean findPaymentMethodStatusInItems(List<PaymentGroupResponse.PaymentMethodItem> items, String paymentMethodName) {
        PaymentGroupResponse.PaymentMethodItem item = findPaymentMethodItemInItems(items, paymentMethodName);
        if (item == null) {
            return null;
        }
        return item.status;
    }

    private PaymentGroupResponse.PaymentMethodItem findPaymentMethodItem(PaymentGroupResponse response, String paymentMethodName) {
        if (response.data == null) {
            return null;
        }
        if (response.data.commonGroup != null) {
            for (PaymentGroupResponse.PaymentGroup group : response.data.commonGroup) {
                PaymentGroupResponse.PaymentMethodItem item = findPaymentMethodItemInItems(group.items, paymentMethodName);
                if (item != null) {
                    return item;
                }
            }
        }
        if (response.data.promotedGroup != null) {
            return findPaymentMethodItemInItems(response.data.promotedGroup.items, paymentMethodName);
        }
        return null;
    }

    private String extractCustomPromoMessage(PaymentGroupResponse.PaymentMethodItem item) {
        if (item == null || item.label == null) {
            return null;
        }
        String label = item.label.trim();
        if (label.isBlank()) {
            return null;
        }
        return label.replaceAll("<[^>]*>", "").trim();
    }

    public void verifyProductDisplayConfigInGroupApi(
            String orderHash,
            String referenceId,
            String accessToken,
            Map<String, String> listPayment,
            List<String> paymentMethods,
            String currency,
            String countryCode
    ) {
        PaymentGroupResponse response = getPaymentGroupFromApi(
                orderHash, referenceId, "DESKTOP", accessToken, currency, countryCode
        );
        Assertion.assertThat("Payment group API code", response.code, is("SUCCESS"));

        for (String paymentMethod : paymentMethods) {
            String configValue = listPayment.get(paymentMethod);
            Boolean status = findPaymentMethodStatus(response, paymentMethod);
            switch (configValue) {
                case "Hide" -> Assertion.assertThat(
                        "Hidden payment method " + paymentMethod + " is not returned in payment-group API",
                        status,
                        nullValue()
                );
                case "Disable" -> Assertion.assertThat(
                        "Disabled payment method " + paymentMethod + " status in payment-group API",
                        status,
                        is(false)
                );
                case "Enable", "PartialEnable" -> Assertion.assertThat(
                        "Enabled payment method " + paymentMethod + " is available in payment-group API",
                        status,
                        is(true)
                );
                default -> throw new IllegalArgumentException("Unknown payment config value: " + configValue);
            }
        }
    }
    public void verifyPaymentMethodEnabledOnAllPlatforms(
            String orderHash,
            String referenceId,
            String accessToken,
            String paymentMethodName,
            String currency,
            String countryCode
    ) {
        assertPaymentMethodStatusOnAllPlatforms(
                orderHash, referenceId, accessToken, paymentMethodName, currency, countryCode,
                true, true, true, true
        );
    }

    public void verifyPaymentMethodDisabledOnAllPlatforms(
            String orderHash,
            String referenceId,
            String accessToken,
            String paymentMethodName,
            String currency,
            String countryCode
    ) {
        assertPaymentMethodStatusOnAllPlatforms(
                orderHash, referenceId, accessToken, paymentMethodName, currency, countryCode,
                false, false, false, false
        );
    }

    private void assertPaymentMethodStatusOnAllPlatforms(
            String orderHash,
            String referenceId,
            String accessToken,
            String paymentMethodName,
            String currency,
            String countryCode,
            boolean iosEnabled,
            boolean androidEnabled,
            boolean mobileEnabled,
            boolean desktopEnabled
    ) {
        assertPaymentMethodStatusOnPlatform(orderHash, referenceId, "IOS", accessToken, paymentMethodName, currency, countryCode, iosEnabled);
        assertPaymentMethodStatusOnPlatform(orderHash, referenceId, "ANDROID", accessToken, paymentMethodName, currency, countryCode, androidEnabled);
        assertPaymentMethodStatusOnPlatform(orderHash, referenceId, "MOBILE", accessToken, paymentMethodName, currency, countryCode, mobileEnabled);
        assertPaymentMethodStatusOnPlatform(orderHash, referenceId, "DESKTOP", accessToken, paymentMethodName, currency, countryCode, desktopEnabled);
    }

    private void assertPaymentMethodStatusOnPlatform(
            String orderHash,
            String referenceId,
            String platform,
            String accessToken,
            String paymentMethodName,
            String currency,
            String countryCode,
            boolean expectedStatus
    ) {
        boolean actualStatus = getPaymentMethodStatusFromGroupApi(
                orderHash, referenceId, platform, accessToken, paymentMethodName, currency, countryCode
        );
        Assertion.assertThat(
                "Payment method " + paymentMethodName + " status on platform " + platform,
                actualStatus,
                is(expectedStatus)
        );
    }

    public String getCustomPromoMessageFromGroupApi(
            String orderHash,
            String referenceId,
            String platform,
            String accessToken,
            String paymentMethodName,
            String currency,
            String countryCode
    ) {
        PaymentGroupResponse response = getPaymentGroupFromApi(
                orderHash, referenceId, platform, accessToken, currency, countryCode
        );
        PaymentGroupResponse.PaymentMethodItem item = findPaymentMethodItem(response, paymentMethodName);
        if (item == null) {
            return null;
        }
        return extractCustomPromoMessage(item);
    }

    public void verifyCustomPromoMessageOnPlatforms(
            String orderHash,
            String referenceId,
            String accessToken,
            String paymentMethodName,
            String expectedMessage,
            String currency,
            String countryCode,
            boolean iosEnabled,
            boolean androidEnabled,
            boolean mobileEnabled,
            boolean desktopEnabled
    ) {
        assertCustomPromoMessageOnPlatform(orderHash, referenceId, "IOS", accessToken, paymentMethodName, expectedMessage, currency, countryCode, iosEnabled);
        assertCustomPromoMessageOnPlatform(orderHash, referenceId, "ANDROID", accessToken, paymentMethodName, expectedMessage, currency, countryCode, androidEnabled);
        assertCustomPromoMessageOnPlatform(orderHash, referenceId, "MOBILE", accessToken, paymentMethodName, expectedMessage, currency, countryCode, mobileEnabled);
        assertCustomPromoMessageOnPlatform(orderHash, referenceId, "DESKTOP", accessToken, paymentMethodName, expectedMessage, currency, countryCode, desktopEnabled);
    }

    private void assertCustomPromoMessageOnPlatform(
            String orderHash,
            String referenceId,
            String platform,
            String accessToken,
            String paymentMethodName,
            String expectedMessage,
            String currency,
            String countryCode,
            boolean shouldHaveMessage
    ) {
        String actualMessage = getCustomPromoMessageFromGroupApi(
                orderHash, referenceId, platform, accessToken, paymentMethodName, currency, countryCode
        );
        if (shouldHaveMessage) {
            Assertion.assertThat(
                    "Custom promo message for " + paymentMethodName + " on " + platform,
                    actualMessage,
                    anyOf(containsStringIgnoringCase(expectedMessage), is(expectedMessage))
            );
        } else {
            Assertion.assertThat(
                    "Configured custom promo message should not appear for " + paymentMethodName + " on " + platform,
                    actualMessage,
                    anyOf(nullValue(), emptyOrNullString(), not(containsStringIgnoringCase(expectedMessage)))
            );
        }
    }
}
