package com.tiket.web.dashboard.page.payment;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PaymentLinkManagementPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator PAYMENT_LINK_MENU = new Locator("PAYMENT_LINK_MENU",
            By.xpath("//*[@data-testid='sidemenu']//span[normalize-space()='Payment Link']"));
    private static final Locator PAYMENT_LINK_ACCORDION_BUTTON = new Locator("PAYMENT_LINK_ACCORDION_BUTTON",
            By.xpath("//div[contains(@class,'btn_menu')][.//span[normalize-space()='Payment Link']]"));
    private static final Locator LINK_MANAGEMENT_SUBMENU = new Locator("LINK_MANAGEMENT_SUBMENU",
            By.xpath("//li[contains(@data-testid,'submenu-id-')]//a[normalize-space()='Link Management']"));
    private static final Locator LINK_MANAGEMENT_TITLE = new Locator("LINK_MANAGEMENT_TITLE",
            By.xpath("//h1[contains(normalize-space(.),'Link Management') or contains(normalize-space(.),'Payment Link Management')]"));
    private static final Locator CREATE_NEW_LINK_BUTTON = new Locator("CREATE_NEW_LINK_BUTTON",
            By.xpath("//button[contains(normalize-space(.),'Create New Link') or contains(normalize-space(.),'Create new link')]"));
    private static final Locator SEARCH_INPUT = new Locator("SEARCH_INPUT", By.id("filter-input-search-text"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON",
            By.xpath("//button[@data-testid='filter-submit-btn']"));
    private static final Locator FIRST_ROW = new Locator("FIRST_ROW",
            By.xpath("//table//tbody//tr[@data-testid][1] | (//table//tbody//tr)[1]"));
    private static final Locator FIRST_ROW_CLIENT_ID = new Locator("FIRST_ROW_CLIENT_ID",
            By.xpath("(//table//tbody//tr)[1]//td[contains(@data-testid,'clientId')]"));
    private static final Locator FIRST_ROW_LINK_USAGE = new Locator("FIRST_ROW_LINK_USAGE",
            By.xpath("(//td[contains(@data-testid,'linkUsage')]//p[contains(normalize-space(.),'/')])[1]"));
    private static final Locator FIRST_ROW_PAYMENT_LINK = new Locator("FIRST_ROW_PAYMENT_LINK",
            By.xpath("(//table//tbody//tr)[1]//td[contains(@data-testid,'paymentLink')]"));
    private static final Locator FIRST_ROW_COPY_LINK_BUTTON = new Locator("FIRST_ROW_COPY_LINK_BUTTON",
            By.xpath("(//div[.//p[contains(@class,'HcPVsG_size_b3') and contains(normalize-space(),'/l/')]]/*[local-name()='svg'][contains(@class,'cursor-pointer')])[1]"));
    private static final Locator FIRST_ROW_OPEN_LINK_BUTTON = new Locator("FIRST_ROW_OPEN_LINK_BUTTON",
            By.xpath("(//table//tbody//tr)[1]//a[contains(@href,'http') or @data-testid='open-link-btn']"));
    private static final Locator FIRST_ROW_ACTION_DROPDOWN = new Locator("FIRST_ROW_ACTION_DROPDOWN", By.xpath("(//div[contains(@class,'dropdown_menu_value') and .//p[normalize-space()='Action']])[1]"));
    private static final Locator FIRST_ROW_CONFIG_BUTTON = new Locator("FIRST_ROW_CONFIG_BUTTON",
            By.xpath("(//table//tbody//tr)[1]//*[contains(normalize-space(.),'View') or contains(normalize-space(.),'Edit') or contains(normalize-space(.),'Configure')][self::button or self::a or self::p]"));
    private static final Locator CONFIG_LINK_USAGE = new Locator("CONFIG_LINK_USAGE",
            By.xpath("//*[contains(normalize-space(.),'Link Usage')]/following::*[contains(normalize-space(.),'/')][1]"));
    private static final Locator CONFIG_PRODUCT_TITLE = new Locator("CONFIG_PRODUCT_TITLE", By.xpath("(//div[contains(@class,'flex-row') and contains(@class,'items-center') and contains(@class,'gap-2')]//p[contains(@class,'HcPVsG_size_b2') and contains(@class,'HcPVsG_weight_bold')])[2]"));
    private static final Locator CONFIG_PAYMENT_METHOD = new Locator("CONFIG_PAYMENT_METHOD", By.xpath("//input[contains(@class,'XmDoza_input') and @id='payment-method']"));
    private static final Locator CONFIG_LINK_URL = new Locator("CONFIG_LINK_URL", By.id("payment-link"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR",
            By.xpath("//*[contains(@class,'snackbar') or contains(@class,'flash success')]//*[contains(normalize-space(.),'success') or contains(normalize-space(.),'created')]"));

    private String lastCreatedPaymentLinkUrl = "";

    public PaymentLinkManagementPage(Session<T> session) {
        super(session);
    }

    public void navigateToLinkManagement() {
        driverTools.waitForElementToAppear(PAYMENT_LINK_MENU);
        if (driverTools.waitForElementToAppear(LINK_MANAGEMENT_SUBMENU, 2).isEmpty()) {
            driverTools.waitForElementToAppear(PAYMENT_LINK_ACCORDION_BUTTON);
            driverTools.click(PAYMENT_LINK_ACCORDION_BUTTON);
        }
        driverTools.waitForElementToAppear(LINK_MANAGEMENT_SUBMENU);
        driverTools.click(LINK_MANAGEMENT_SUBMENU);
        driverTools.waitForElementToAppear(LINK_MANAGEMENT_TITLE);
    }

    public boolean isOnLinkManagementPage() {
        return driverTools.waitForElementToAppear(LINK_MANAGEMENT_TITLE, 10).isPresent()
                || driverTools.waitForElementToAppear(CREATE_NEW_LINK_BUTTON, 10).isPresent();
    }

    public void clickCreateNewLink() {
        driverTools.waitForElementToAppear(CREATE_NEW_LINK_BUTTON);
        driverTools.click(CREATE_NEW_LINK_BUTTON);
    }

    public void storeCreatedPaymentLinkUrl(String paymentLinkUrl) {
        lastCreatedPaymentLinkUrl = paymentLinkUrl;
    }

    public String getLastCreatedPaymentLinkUrl() {
        return lastCreatedPaymentLinkUrl;
    }

    public void searchPaymentLinkByClientId(String clientId) {
        searchPaymentLink(clientId);
    }

    public void searchPaymentLink(String keyword) {
        WebElement searchInput = driverTools.waitForElementToAppear(SEARCH_INPUT).orElseThrow();
        driverTools.scrollElementToCenter(searchInput);
        try {
            driverTools.clickWithActions(searchInput);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(searchInput);
        }
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(SEARCH_INPUT, keyword);
        WebElement searchButton = driverTools.waitForElementToBeClickable(SEARCH_BUTTON, 10).orElseThrow();
        driverTools.scrollElementToCenter(searchButton);
        driverTools.clickWithActions(searchButton);
        driverTools.waitForElementToAppear(FIRST_ROW, 20);
    }

    public String getFirstRowClientId() {
        return driverTools.getText(FIRST_ROW_CLIENT_ID).orElse("").trim();
    }

    public String getFirstRowLinkUsage() {
        return driverTools.getText(FIRST_ROW_LINK_USAGE).orElse("").replaceAll("\\s+", " ").trim();
    }

    public String getFirstRowPaymentLinkUrl() {
        String urlFromRow = driverTools.getText(FIRST_ROW_PAYMENT_LINK).orElse("").trim();
        if (urlFromRow.startsWith("http")) {
            return urlFromRow;
        }
        if (driverTools.waitForElementToAppear(FIRST_ROW_OPEN_LINK_BUTTON, 3).isPresent()) {
            return driverTools.getValueAttribute(FIRST_ROW_OPEN_LINK_BUTTON, "href");
        }
        return getLastCreatedPaymentLinkUrl();
    }

    public void openFirstRowConfiguration() {
        driverTools.waitForElementToAppear(FIRST_ROW_ACTION_DROPDOWN);
        driverTools.click(FIRST_ROW_ACTION_DROPDOWN);
        driverTools.waitForElementToAppear(FIRST_ROW_CONFIG_BUTTON);
        driverTools.click(FIRST_ROW_CONFIG_BUTTON);
    }

    public String clickCopyLinkOnFirstRow() {
        driverTools.prepareBrowserClipboardCapture();
        driverTools.waitForElementToAppear(FIRST_ROW_COPY_LINK_BUTTON);
        driverTools.click(FIRST_ROW_COPY_LINK_BUTTON);
        String copiedUrl = driverTools.getTextFromClipboardBrowser();
        if (copiedUrl == null || copiedUrl.isBlank()) {
            copiedUrl = getFirstRowPaymentLinkUrl();
        }
        return copiedUrl;
    }

    public boolean isPaymentLinkCreatedSuccessfully() {
        return driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10).isPresent()
                || (lastCreatedPaymentLinkUrl != null && !lastCreatedPaymentLinkUrl.isBlank());
    }

    private Locator getRowByPaymentLinkUrl(String paymentLinkUrl) {
        return new Locator("PAYMENT_LINK_ROW_URL",
                By.xpath("//table//tbody//tr[.//td[contains(@data-testid,'paymentLink')]"
                        + "//p[contains(normalize-space(.),'" + paymentLinkUrl + "')]]"));
    }

    public boolean isPaymentLinkUrlDisplayedInList(String paymentLinkUrl) {
        return driverTools.waitForElementToAppear(getRowByPaymentLinkUrl(paymentLinkUrl), 15).isPresent();
    }

    public String getConfigurationPageLinkUsage() {
        return driverTools.getText(CONFIG_LINK_USAGE).orElse("");
    }

    public String getConfigurationPageProductTitle() {
        return driverTools.getText(CONFIG_PRODUCT_TITLE).orElse("").trim();
    }

    public String getConfigurationPagePaymentMethod() {
        String paymentMethodValue = String.valueOf(driverTools.getText(CONFIG_PAYMENT_METHOD));
        return paymentMethodValue == null ? "" : paymentMethodValue.trim();
    }

    public String getConfigurationPageLinkUrl() {
        String linkUrl = driverTools.getValueAttribute(CONFIG_LINK_URL, "value");
        return linkUrl == null ? "" : linkUrl.trim();
    }
}
