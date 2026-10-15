package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateGeneralConfigPage<T extends WebDriver> extends WebPage<T> {
    public AffiliateGeneralConfigPage(Session<T> session) {
        super(session);
    }

    private static final Locator FIRST_DROPDOWN_CHEVRON = new Locator("FIRST_DROPDOWN_CHEVRON", By.xpath("(//input[@id='more-action'])[1]"));
    private static final Locator COMMISSION_MENU_ITEM = new Locator("COMMISSION_MENU_ITEM", By.xpath("//span[normalize-space(text())='Commission' or normalize-space(text())='Komisi']"));
    private static final Locator ADD_NEW_BUTTON = new Locator("ADD_NEW_BUTTON", By.xpath("//button[normalize-space(text())='Add New' or normalize-space(text())='Tambah Baru']"));
    private static final Locator DROPDOWN_VERTICAL = new Locator("DROPDOWN_VERTICAL", By.xpath("(//div[contains(@class,'css-1xc3v61-indicatorContainer')])[1]"));
    private static final Locator DROPDOWN_PRIORITY = new Locator("DROPDOWN_PRIORITY", By.xpath("(//div[contains(@class,'css-1xc3v61-indicatorContainer')])[3]"));
    private static final Locator DROPDOWN_COMMISSION = new Locator("DROPDOWN_COMMISSION", By.xpath("(//div[contains(@class,'css-1xc3v61-indicatorContainer')])[4]"));
    private static final Locator HOTEL_OPTION = new Locator("HOTEL_OPTION", By.cssSelector("div.css-10wo9uf-option#react-select-4-option-1"));
    private static final Locator CURRENCY_OPTION = new Locator("CURRENCY_OPTION", By.cssSelector("div.css-d7l1ni-option#react-select-6-option-0"));
    private static final Locator PRIORITY = new Locator("PRIORITY", By.cssSelector("div.css-10wo9uf-option#react-select-5-option-3"));
    private static final Locator PROMO_USAGE_DROPDOWN = new Locator("PROMO_USAGE_DROPDOWN", By.xpath("(//button[contains(@class,'styles_clickable_icon__Erwn2')])[1]"));
    private static final Locator PAYMENT_CURRENCIES_DROPDOWN = new Locator("PAYMENT_CURRENCIES_DROPDOWN", By.xpath("(//button[contains(@class,'styles_clickable_icon__Erwn2')])[2]"));
    private static final Locator PRODUCT_TYPE_DROPDOWN = new Locator("PRODUCT_TYPE_DROPDOWN", By.xpath("(//button[contains(@class,'styles_clickable_icon__Erwn2')])[3]"));
    private static final Locator USER_TYPE_DROPDOWN = new Locator("USER_TYPE_DROPDOWN", By.xpath("(//button[contains(@class,'styles_clickable_icon__Erwn2')])[4]"));
    private static final Locator RADIO_OPTION_FLAT = new Locator("RADIO_OPTION_FLAT", By.xpath("//label[contains(@class,'Radio_radio__LBjtz')]//p[text()='Flat']"));
    private static final Locator RADIO_OPTION_RANGED = new Locator("RADIO_OPTION_RANGED", By.xpath("//label[contains(@class,'Radio_radio__LBjtz')]//p[text()='Ranged']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.cssSelector("button.Button_button__20aRx.Button_horizontal_padding__7QPqY.Button_variant_primary__PFcuM"));
    private static final Locator COMMISSION_RULE_NAME_INPUT = new Locator("COMMISSION_RULE_NAME_INPUT", By.cssSelector("input#commission-rule-name[name='name'][type='text']"));
    private static final Locator PROMO_DROPDOWN_ALL_OPTION= new Locator("PROMO_DROPDOWN_ALL_OPTION", By.xpath("(//li[.//p[normalize-space(text())='All']]//input[@type='checkbox'])[1]"));
    private static final Locator PAYMENT_DROPDOWN_ALL_OPTION= new Locator("PAYMENT_DROPDOWN_ALL_OPTION", By.xpath("(//li[.//p[normalize-space(text())='All']]//input[@type='checkbox'])[2]"));
    private static final Locator PRODUCT_TYPE_DROPDOWN_ALL_OPTION= new Locator("PRODUCT_TYPE_DROPDOWN_ALL_OPTION", By.xpath("(//li[.//p[normalize-space(text())='All']]//input[@type='checkbox'])[3]"));
    private static final Locator USER_TYPE_DROPDOWN_ALL_OPTION= new Locator("USER_TYPE_DROPDOWN_ALL_OPTION", By.xpath("(//li[.//p[normalize-space(text())='All']]//input[@type='checkbox'])[4]"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[text()='Submit']"));
    private static final Locator SNACKBAR_MESSAGE = new Locator("SNACKBAR_MESSAGE", By.xpath("//div[contains(@class,'Snackbar_text_content__ePdKr') and text()='New config is created.']"));
    private static final Locator RADIO_NET_REVENUE_SHARING = new Locator("RADIO_NET_REVENUE_SHARING", By.xpath("//label[.//p[text()='Net Revenue Sharing']]"));
    private static final Locator RADIO_NO_COMMISSION = new Locator("RADIO_NO_COMMISSION", By.xpath("//label[.//p[text()='No Commission']]"));
    private static final Locator RADIO_TRANSACTION_AMOUNT_SHARING = new Locator("RADIO_TRANSACTION_AMOUNT_SHARING", By.xpath("//label[.//p[text()='Transaction Amount Sharing']]"));
    private static final Locator START_DATE = new Locator("START_DATE", By.xpath("//input[@id='start-date']"));
    private static final Locator END_DATE = new Locator("END_DATE", By.xpath("//input[@id='end-date']"));
    private static final Locator START_TIME = new Locator("START_TIME", By.xpath("//input[@id='start-time']"));
    private static final Locator END_TIME = new Locator("END_TIME", By.xpath("//input[@id='end-time']"));
    private static final Locator COMMISSION_VALUE_PERCENTAGE = new Locator("COMMISSION_VALUE_PERCENTAGE", By.xpath("//input[@id='percentage']"));
    private static final Locator COMMISSION_VALUE= new Locator("COMMISSION_VALUE", By.xpath("//p[contains(@class, 'Text_size_b2') and contains(text(), '%')]"));

    public void goToCommissionMenu() {
        driverTools.waitForElementToAppear(FIRST_DROPDOWN_CHEVRON, 5).orElseThrow();
        driverTools.click(FIRST_DROPDOWN_CHEVRON);
        driverTools.waitForElementToAppear(COMMISSION_MENU_ITEM, 5).orElseThrow();
        driverTools.click(COMMISSION_MENU_ITEM);
    }

    public void clickAddNewButton() {
        driverTools.waitForElementToAppear(ADD_NEW_BUTTON, 5).orElseThrow();
        driverTools.click(ADD_NEW_BUTTON);
    }

    public void fillGeneralInfoFields(String commissionRuleName) {
        driverTools.dummyWait(1);
        driverTools.click(DROPDOWN_VERTICAL);
        driverTools.click(HOTEL_OPTION);
        driverTools.sendKeys(COMMISSION_RULE_NAME_INPUT, commissionRuleName);
        driverTools.click(DROPDOWN_COMMISSION);
        driverTools.click(CURRENCY_OPTION);
        driverTools.click(DROPDOWN_PRIORITY);
        driverTools.click(PRIORITY);
    }

    public void fillCommissionRules() {
        driverTools.waitForElementToAppear(USER_TYPE_DROPDOWN, 5).orElseThrow();
        driverTools.scrollToElement(USER_TYPE_DROPDOWN);

        driverTools.clickWithJavascriptExecutor(PRODUCT_TYPE_DROPDOWN);
        driverTools.clickWithJavascriptExecutor(PRODUCT_TYPE_DROPDOWN_ALL_OPTION);

        driverTools.clickWithJavascriptExecutor(USER_TYPE_DROPDOWN);
        driverTools.clickWithJavascriptExecutor(USER_TYPE_DROPDOWN_ALL_OPTION);

        driverTools.clickWithJavascriptExecutor(PROMO_USAGE_DROPDOWN);
        driverTools.clickWithJavascriptExecutor(PROMO_DROPDOWN_ALL_OPTION);

        driverTools.clickWithJavascriptExecutor(PAYMENT_CURRENCIES_DROPDOWN);
        driverTools.clickWithJavascriptExecutor(PAYMENT_DROPDOWN_ALL_OPTION);
    }

    public void clickTransactionAmountSharing() {
        driverTools.waitForElementToAppear(RADIO_TRANSACTION_AMOUNT_SHARING, 5).orElseThrow();
        driverTools.scrollToElement(RADIO_TRANSACTION_AMOUNT_SHARING);
        driverTools.click(RADIO_TRANSACTION_AMOUNT_SHARING);
    }

    public void fillCommissionDetails(String value) {
        clickCommissionValueFields();
        driverTools.sendKeys(COMMISSION_VALUE_PERCENTAGE, value);
    }

    public void clickCommissionValueFields(){
        driverTools.waitForElementToAppear(COMMISSION_VALUE_PERCENTAGE, 5).orElseThrow();
        driverTools.scrollToElement(COMMISSION_VALUE_PERCENTAGE);
        driverTools.click(COMMISSION_VALUE_PERCENTAGE);
    }

    public void clickSaveButton() {
        driverTools.waitForElementToAppear(SAVE_BUTTON, 5).orElseThrow();
        driverTools.scrollToElement(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    public void clickSubmitButton() {
        driverTools.waitForElementToAppear(SUBMIT_BUTTON, 5).orElseThrow();
        driverTools.click(SUBMIT_BUTTON);
    }

    public boolean isSubmitButtonEnabled() {
        driverTools.waitForElementToAppear(SUBMIT_BUTTON, 5).orElseThrow();
        return driverTools.isElementDisplayed(SUBMIT_BUTTON);
    }

    public boolean isGeneralConfigCreated() {
        driverTools.waitForElementToAppear(SNACKBAR_MESSAGE, 5).orElseThrow();
        String message = driverTools.getText(SNACKBAR_MESSAGE).orElseThrow();
        return message.contains("New config is created.");
    }

    public boolean isAllCommissionDetailsClickable() {
        driverTools.waitForElementToAppear(RADIO_OPTION_FLAT, 5).orElseThrow();

        boolean isClickable = driverTools.isElementClickable(RADIO_NET_REVENUE_SHARING, 5);
        isClickable &= driverTools.isElementClickable(RADIO_NO_COMMISSION, 5);
        isClickable &= driverTools.isElementClickable(RADIO_TRANSACTION_AMOUNT_SHARING, 5);
        isClickable &= driverTools.isElementClickable(RADIO_OPTION_FLAT, 5);

        return isClickable;
    }

    public boolean checkIfGeneralInfoFieldsAreClickable() {
        driverTools.waitForElementToAppear(DROPDOWN_VERTICAL, 5).orElseThrow();

        boolean isClickable = driverTools.isElementClickable(DROPDOWN_VERTICAL, 5);
        isClickable &= driverTools.isElementClickable(DROPDOWN_COMMISSION, 5);
        isClickable &= driverTools.isElementClickable(DROPDOWN_PRIORITY, 5);

        return isClickable;
    }

    public boolean checkIfCommissionRulesAreClickable() {
        driverTools.waitForElementToAppear(USER_TYPE_DROPDOWN, 5).orElseThrow();
        driverTools.scrollToElement(USER_TYPE_DROPDOWN);

        boolean isClickable = driverTools.isElementClickable(PROMO_USAGE_DROPDOWN, 5);
        isClickable &= driverTools.isElementClickable(PAYMENT_CURRENCIES_DROPDOWN, 5);
        isClickable &= driverTools.isElementClickable(PRODUCT_TYPE_DROPDOWN, 5);
        isClickable &= driverTools.isElementClickable(USER_TYPE_DROPDOWN, 5);

        return isClickable;
    }

    public boolean checkIfCommissionPeriodChipsAreDisplayed() {
        driverTools.waitForElementToAppear(RADIO_OPTION_RANGED, 5).orElseThrow();
        driverTools.scrollToElement(RADIO_OPTION_RANGED);
        driverTools.click(RADIO_OPTION_RANGED);

        driverTools.waitForElementToAppear(START_DATE, 5).orElseThrow();
        boolean isDisplayed = driverTools.isElementDisplayed(START_DATE);
        isDisplayed &= driverTools.isElementDisplayed(END_DATE);
        isDisplayed &= driverTools.isElementDisplayed(START_TIME);
        isDisplayed &= driverTools.isElementDisplayed(END_TIME);

        return isDisplayed;
    }

    public boolean checkIfCommissionValueFieldsAreDisplayed() {
        driverTools.waitForElementToAppear(COMMISSION_VALUE_PERCENTAGE, 5).orElseThrow();
        driverTools.scrollToElement(COMMISSION_VALUE_PERCENTAGE);
        boolean isDisplayed = driverTools.isElementDisplayed(COMMISSION_VALUE_PERCENTAGE);
        return isDisplayed;
    }

    public boolean verifyCommissionValue(String value) {
        driverTools.waitForElementToAppear(COMMISSION_VALUE, 5).orElseThrow();
        String commissionValueText = driverTools.getText(COMMISSION_VALUE).orElseThrow();
        return commissionValueText.contains(value);
    }
}