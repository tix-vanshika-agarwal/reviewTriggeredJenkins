package com.tiket.web.dashboard.page.payment;

import com.google.gson.Gson;
import com.tiket.api.payment.PromoNudgeApi;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.payment.PromoNudgeResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;

public class DynamicPromoNudgesPage<T extends WebDriver> extends BasePage<T> {

    private static final Gson gson = new Gson();

    private static final Locator PAYMENT_DISPLAY_CUSTOM_CONFIGURATION = new Locator(
            "PAYMENT_DISPLAY_CUSTOM_CONFIGURATION",
            By.xpath("//*[@data-testid='sidemenu']//*[contains(text(),'Payment Display Custom Configuration')]")
    );
    private static final Locator DYNAMIC_PROMO_NUDGES_SUBMENU = new Locator(
            "DYNAMIC_PROMO_NUDGES_SUBMENU",
            By.xpath("//*[contains(@data-testid,'dynamic-promo-nudges')]//a")
    );
    private static final Locator PAGE_TITLE = new Locator(
            "PAGE_TITLE",
            By.xpath("//h1[normalize-space()='Dynamic Promo Nudges']")
    );
    private static final Locator ADD_PAYMENT_METHOD_BUTTON = new Locator(
            "ADD_PAYMENT_METHOD_BUTTON",
            By.xpath("//button[normalize-space()='Add payment method']")
    );
    private static final Locator EDIT_PAGE_TITLE = new Locator(
            "EDIT_PAGE_TITLE",
            By.xpath("//h1[contains(normalize-space(),'Dynamic Promo Nudges Settings')]")
    );
    private static final Locator PAYMENT_METHOD_DROPDOWN_BUTTON = new Locator(
            "PAYMENT_METHOD_DROPDOWN_BUTTON",
            By.xpath("//span[normalize-space()='Payment Method']/following-sibling::div//div[@data-testid='dropdown-btn-default']")
    );
    private static final Locator CUSTOM_MESSAGE_TOGGLE_LABEL = new Locator(
            "CUSTOM_MESSAGE_TOGGLE_LABEL",
            By.xpath("//span[normalize-space()='Custom Message']/following-sibling::div//label")
    );
    private static final Locator CUSTOM_MESSAGE_EN_EDITOR = new Locator(
            "CUSTOM_MESSAGE_EN_EDITOR",
            By.xpath("(//div[contains(@class,'ql-editor')])[1]")
    );
    private static final Locator CUSTOM_MESSAGE_ID_EDITOR = new Locator(
            "CUSTOM_MESSAGE_ID_EDITOR",
            By.xpath("(//div[contains(@class,'ql-editor')])[2]")
    );
    private static final Locator SAVE_BUTTON = new Locator(
            "SAVE_BUTTON",
            By.cssSelector("[data-testid='btn-save']")
    );
    private static final Locator CONFIRM_SAVE_MODAL_BUTTON = new Locator(
            "CONFIRM_SAVE_MODAL_BUTTON",
            By.xpath("//footer[contains(@class,'modal_footer')]//button[normalize-space()='Yes, update' or normalize-space()='Yes, add']")
    );
    private static final Locator CANCEL_SAVE_MODAL_BUTTON = new Locator(
            "CANCEL_SAVE_MODAL_BUTTON",
            By.xpath("//footer[contains(@class,'modal_footer')]//button[normalize-space()='Cancel']")
    );
    private static final Locator CANCEL_EDIT_BUTTON = new Locator(
            "CANCEL_EDIT_BUTTON",
            By.xpath("//button[@data-testid='btn-save']/preceding-sibling::button[normalize-space()='Cancel']")
    );

    Map<String, String> columnToId = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("Payment Method", "paymentMethodName"),
            new AbstractMap.SimpleEntry<>("iOS", "ios"),
            new AbstractMap.SimpleEntry<>("Android", "android"),
            new AbstractMap.SimpleEntry<>("Mobile", "mobile"),
            new AbstractMap.SimpleEntry<>("Desktop", "desktop"),
            new AbstractMap.SimpleEntry<>("Custom Message (EN)", "messageEn"),
            new AbstractMap.SimpleEntry<>("Custom Message (ID)", "messageId"),
            new AbstractMap.SimpleEntry<>("Show Custom Message", "showCustomMessage")
    );

    public DynamicPromoNudgesPage(Session<T> session) {
        super(session);
    }

    public void openDynamicPromoNudgesPage() {
        driverTools.click(PAYMENT_DISPLAY_CUSTOM_CONFIGURATION);
        driverTools.click(DYNAMIC_PROMO_NUDGES_SUBMENU);
        driverTools.waitForElementToAppear(PAGE_TITLE).orElseThrow();
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

    private Locator getEditActionByMethod(String paymentMethod) {
        return new Locator(
                "EDIT_ACTION_BY_METHOD_" + paymentMethod.toUpperCase(),
                By.xpath(paymentMethodRowXPath(paymentMethod) + "//*[@data-testid='dynamic-promo-nudges-action']")
        );
    }

    private Locator getPlatformToggleOnEditPage(String platform) {
        return new Locator(
                "EDIT_PLATFORM_TOGGLE_" + platform.toUpperCase(),
                By.xpath("//span[normalize-space()='" + platform + "']/following-sibling::div//label")
        );
    }

    private Locator getPlatformToggleByMethod(String paymentMethod, String platform) {
        String columnKey = columnToId.get(platform);
        return new Locator(
                "PLATFORM_TOGGLE_" + columnKey.toUpperCase() + "_BY_METHOD_" + paymentMethod.toUpperCase(),
                By.xpath(paymentMethodRowXPath(paymentMethod) + "//td[contains(@data-testid,'-" + columnKey + "')]//label")
        );
    }

    public String getShowCustomMessageStatus(String paymentMethod) {
        WebElement cell = driverTools.waitForElementToAppear(getCellByMethod(paymentMethod, "Show Custom Message")).orElseThrow();
        return driverTools.getText(cell.findElement(By.cssSelector("span"))).trim();
    }

    public String getListingCustomMessageEn(String paymentMethod) {
        return getListingMessageText(paymentMethod, "Custom Message (EN)");
    }

    public String getListingCustomMessageId(String paymentMethod) {
        return getListingMessageText(paymentMethod, "Custom Message (ID)");
    }

    private String getListingMessageText(String paymentMethod, String columnName) {
        WebElement cell = driverTools.waitForElementToAppear(getCellByMethod(paymentMethod, columnName)).orElseThrow();
        String text = driverTools.getText(cell).trim();
        return "-".equals(text) ? "" : text;
    }

    public boolean isPlatformToggleEnabled(String paymentMethod, String platform) {
        WebElement cell = driverTools.waitForElementToAppear(getCellByMethod(paymentMethod, platform)).orElseThrow();
        driverTools.scrollElementToCenter(cell);
        WebElement input = driverTools.findElementInElement(cell, By.cssSelector("input[type='checkbox']"));
        return input.isSelected();
    }

    public void clickPlatformToggle(String paymentMethod, String platform) {
        WebElement toggle = driverTools.waitForElementToAppear(getPlatformToggleByMethod(paymentMethod, platform)).orElseThrow();
        driverTools.scrollElementToCenter(toggle);
        driverTools.click(toggle);
        WebElement confirmButton = driverTools.waitForElementToAppear(CONFIRM_SAVE_MODAL_BUTTON).orElseThrow();
        driverTools.scrollElementToCenter(confirmButton);
        driverTools.click(confirmButton);
        driverTools.waitForElementToAppear(PAGE_TITLE).orElseThrow();
    }

    public void setPlatformToggle(String paymentMethod, String platform, boolean enabled, String currency) {
        if (isPlatformToggleEnabled(paymentMethod, platform) != enabled) {
            clickPlatformToggle(paymentMethod, platform);
            waitForChannelEnabledFromApi(currency, paymentMethod, platform, enabled);
            waitForPlatformToggleEnabled(paymentMethod, platform, enabled);
        }
    }

    public void setAllPlatformToggles(String paymentMethod, boolean enabled, String currency) {
        for (String platform : PaymentMethods.PLATFORM_COLUMNS) {
            setPlatformToggle(paymentMethod, platform, enabled, currency);
        }
    }

    public void verifyAllPlatformTogglesEnabled(String paymentMethod, boolean expected) {
        for (String platform : PaymentMethods.PLATFORM_COLUMNS) {
            Assertion.assertThat(
                    "Platform toggle " + platform + " for " + paymentMethod,
                    isPlatformToggleEnabled(paymentMethod, platform),
                    is(expected)
            );
        }
    }

    public void verifyShowCustomMessageStatus(String paymentMethod, String expectedStatus) {
        Assertion.assertThat(
                "Show custom message status for " + paymentMethod,
                getShowCustomMessageStatus(paymentMethod),
                is(expectedStatus)
        );
    }

    private void waitForChannelEnabledFromApi(String currency, String paymentMethod, String platform, boolean enabled) {
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(d -> {
            PromoNudgeResponse.PromoNudge promoNudge = getPromoNudgeByPaymentMethod(currency, paymentMethod);
            return promoNudge != null && isChannelEnabled(promoNudge, platform) == enabled;
        });
    }

    private void waitForPlatformToggleEnabled(String paymentMethod, String platform, boolean enabled) {
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(d -> {
            try {
                return isPlatformToggleEnabled(paymentMethod, platform) == enabled;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public void openEditConfiguration(String paymentMethod) {
        WebElement editAction = driverTools.waitForElementToAppear(getEditActionByMethod(paymentMethod)).orElseThrow();
        driverTools.scrollElementToCenter(editAction);
        driverTools.waitForElementToBeClickable(editAction).ifPresentOrElse(driverTools::click, () -> {
            throw new RuntimeException("Unable to click element " + editAction);
        });
        driverTools.waitForElementToAppear(EDIT_PAGE_TITLE).orElseThrow();
    }

    public void clickAddPaymentMethod() {
        driverTools.click(ADD_PAYMENT_METHOD_BUTTON);
        driverTools.waitForElementToAppear(EDIT_PAGE_TITLE).orElseThrow();
    }

    public String selectFirstUnconfiguredPaymentMethod(List<String> configuredMethods) {
        driverTools.click(PAYMENT_METHOD_DROPDOWN_BUTTON);
        List<String> configuredUppercase = configuredMethods.stream().map(String::toUpperCase).toList();
        List<WebElement> options = driver.findElements(By.xpath("//*[contains(@class,'styles_dropdown_option__')]//p"));
        for (WebElement option : options) {
            String paymentMethod = option.getText().trim();
            if (!paymentMethod.isBlank() && !configuredUppercase.contains(paymentMethod.toUpperCase())) {
                driverTools.click(option);
                return paymentMethod;
            }
        }
        throw new NoSuchElementException("No unconfigured payment method found in dropdown");
    }

    public void setCustomMessage(String messageEn, String messageId) {
        setCustomMessageToggle(true);
        WebElement editorEn = driverTools.waitForElementToAppear(CUSTOM_MESSAGE_EN_EDITOR).orElseThrow();
        WebElement editorId = driverTools.waitForElementToAppear(CUSTOM_MESSAGE_ID_EDITOR).orElseThrow();
        driverTools.clearInput(editorEn);
        driverTools.sendKeys(editorEn, messageEn);
        driverTools.clearInput(editorId);
        driverTools.sendKeys(editorId, messageId);
    }

    public void setCustomMessageToggle(boolean enabled) {
        WebElement toggle = driverTools.waitForElementToAppear(CUSTOM_MESSAGE_TOGGLE_LABEL).orElseThrow();
        WebElement checkbox = driverTools.findElementInElement(toggle, By.cssSelector("input[type='checkbox']"));
        if (checkbox.isSelected() != enabled) {
            driverTools.click(toggle);
        }
    }

    public void setPlatformToggleOnEditPage(String platform, boolean enabled) {
        WebElement toggle = driverTools.waitForElementToAppear(getPlatformToggleOnEditPage(platform)).orElseThrow();
        WebElement checkbox = driverTools.findElementInElement(toggle, By.cssSelector("input[type='checkbox']"));
        if (checkbox.isSelected() != enabled) {
            driverTools.click(toggle);
        }
    }

    public void setAllPlatformTogglesOnEditPage(boolean enabled) {
        for (String platform : PaymentMethods.PLATFORM_COLUMNS) {
            setPlatformToggleOnEditPage(platform, enabled);
        }
    }

    public void prepareActivePromoNudgeConfiguration(String paymentMethod, String messageEn, String messageId) {
        openEditConfiguration(paymentMethod);
        setCustomMessage(messageEn, messageId);
        setAllPlatformTogglesOnEditPage(true);
        saveConfiguration();
    }

    public void activateCustomMessageConfiguration(String paymentMethod) {
        openEditConfiguration(paymentMethod);
        setCustomMessageToggle(true);
        saveConfiguration();
    }

    public void deactivateCustomMessageConfiguration(String paymentMethod) {
        openEditConfiguration(paymentMethod);
        setCustomMessageToggle(false);
        saveConfiguration();
    }

    public void clickSaveAndCancelWithoutSaving() {
        WebElement saveButton = driverTools.waitForElementToAppear(SAVE_BUTTON).orElseThrow();
        driverTools.scrollElementToCenter(saveButton);
        driverTools.click(saveButton);
        WebElement cancelModalButton = driverTools.waitForElementToAppear(CANCEL_SAVE_MODAL_BUTTON).orElseThrow();
        driverTools.click(cancelModalButton);
        WebElement cancelEditButton = driverTools.waitForElementToAppear(CANCEL_EDIT_BUTTON).orElseThrow();
        driverTools.click(cancelEditButton);
        driverTools.waitForElementToAppear(PAGE_TITLE).orElseThrow();
    }

    public void saveConfiguration() {
        WebElement saveButton = driverTools.waitForElementToAppear(SAVE_BUTTON).orElseThrow();
        driverTools.scrollElementToCenter(saveButton);
        driverTools.click(saveButton);
        WebElement confirmButton = driverTools.waitForElementToAppear(CONFIRM_SAVE_MODAL_BUTTON).orElseThrow();
        driverTools.click(confirmButton);
        driverTools.waitForElementToAppear(PAGE_TITLE).orElseThrow();
    }

    public PromoNudgeResponse getPromoNudgeFromApi(String currency) {
        PromoNudgeApi promoNudgeApi = new PromoNudgeApi(new PromoNudgeApi.Inputs(getBrowserCookieHeader(), currency));
        ApiResponse apiResponse = promoNudgeApi.hitApi(apiTools);
        logger.log("debug : " + apiResponse.getBody());
        return gson.fromJson(apiResponse.getBody(), PromoNudgeResponse.class);
    }

    public PromoNudgeResponse.PromoNudge findFirstActivePromoNudge(String currency) {
        PromoNudgeResponse response = getPromoNudgeFromApi(currency);
        if (response.data == null || response.data.content == null) {
            return null;
        }
        for (PromoNudgeResponse.PromoNudge promoNudge : response.data.content) {
            if (promoNudge.enableCustom) {
                return promoNudge;
            }
        }
        return null;
    }

    public PromoNudgeResponse.PromoNudge findFirstInactivePromoNudge(String currency) {
        PromoNudgeResponse response = getPromoNudgeFromApi(currency);
        if (response.data == null || response.data.content == null) {
            return null;
        }
        for (PromoNudgeResponse.PromoNudge promoNudge : response.data.content) {
            if (!promoNudge.enableCustom) {
                return promoNudge;
            }
        }
        return null;
    }

    public boolean isFullyActive(PromoNudgeResponse.PromoNudge promoNudge) {
        return promoNudge.enableCustom
                && isChannelEnabled(promoNudge, "ios")
                && isChannelEnabled(promoNudge, "android")
                && isChannelEnabled(promoNudge, "mobile")
                && isChannelEnabled(promoNudge, "desktop");
    }

    public PromoNudgeResponse.PromoNudge getPromoNudgeByPaymentMethod(String currency, String paymentMethod) {
        PromoNudgeResponse response = getPromoNudgeFromApi(currency);
        if (response.data == null || response.data.content == null) {
            return null;
        }
        for (PromoNudgeResponse.PromoNudge promoNudge : response.data.content) {
            if (paymentMethod.equalsIgnoreCase(promoNudge.paymentMethod)) {
                return promoNudge;
            }
        }
        return null;
    }

    public List<String> getConfiguredPaymentMethodsFromApi(String currency) {
        PromoNudgeResponse response = getPromoNudgeFromApi(currency);
        List<String> configuredMethods = new ArrayList<>();
        if (response.data == null || response.data.content == null) {
            return configuredMethods;
        }
        for (PromoNudgeResponse.PromoNudge promoNudge : response.data.content) {
            configuredMethods.add(promoNudge.paymentMethod);
        }
        return configuredMethods;
    }

    public String getCustomTextEn(PromoNudgeResponse.PromoNudge promoNudge) {
        if (promoNudge.customText == null || promoNudge.customText.en == null) {
            return "";
        }
        return stripHtml(promoNudge.customText.en);
    }

    public boolean isChannelEnabled(PromoNudgeResponse.PromoNudge promoNudge, String channel) {
        if (promoNudge.activeChannels == null) {
            return false;
        }
        return switch (channel.toLowerCase()) {
            case "ios" -> promoNudge.activeChannels.ios;
            case "android" -> promoNudge.activeChannels.android;
            case "mobile" -> promoNudge.activeChannels.mobile;
            case "desktop" -> promoNudge.activeChannels.desktop;
            default -> false;
        };
    }

    private String stripHtml(String text) {
        return text.replaceAll("<[^>]*>", "").trim();
    }

    private String getBrowserCookieHeader() {
        StringBuilder cookieHeader = new StringBuilder();
        for (Cookie cookie : driver.manage().getCookies()) {
            if (!cookieHeader.isEmpty()) {
                cookieHeader.append("; ");
            }
            cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue());
        }
        return cookieHeader.toString();
    }
}
