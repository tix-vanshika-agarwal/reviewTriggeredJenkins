package com.tiket.web.dashboard.page.affiliate;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateNewPartnerFormPage<T extends WebDriver> extends BasePage<T> {

    public AffiliateNewPartnerFormPage(Session<T> session) {
        super(session);
    }

    private static final Locator COMPANY_NAME = new Locator("COMPANY_NAME", By.name("partnerName"));
    private static final Locator COUNTRY_DROPDOWN = new Locator("COUNTRY_DROPDOWN", By.xpath("(//button[contains(@class,'styles_clickable_icon')])[1]"));
    private static final Locator TIKET_ENTITY_DROPDOWN = new Locator("TIKET_ENTITY_DROPDOWN", By.xpath("(//button[contains(@class,'styles_clickable_icon')])[2]"));
    private static final Locator SETTLEMENT_TYPE_DROPDOWN = new Locator("SETTLEMENT_TYPE_DROPDOWN", By.xpath("(//button[contains(@class,'styles_clickable_icon')])[3]"));
    private static final Locator CURRENCY_DROPDOWN = new Locator("CURRENCY_DROPDOWN", By.xpath("(//button[contains(@class,'styles_clickable_icon')])[4]"));
    private static final Locator FIRST_NAME = new Locator("FIRST_NAME", By.name("accountFirstName"));
    private static final Locator LAST_NAME = new Locator("LAST_NAME", By.name("accountLastName"));
    private static final Locator PARTNER_EMAIL = new Locator("PARTNER_EMAIL", By.name("accountEmail"));
    private static final Locator PARTNER_PHONE = new Locator("PARTNER_PHONE", By.id("phone-number"));

    private static final Locator SAVE_BTN = new Locator("SAVE_BTN", By.xpath("//button[text()='Save']"));
    private static final Locator LATER_BTN = new Locator("LATER_BTN", By.xpath("//button[text()='Later']"));
    private static final Locator ADD_PRODUCT_BTN = new Locator("ADD_PRODUCT_BTN", By.xpath("//button[normalize-space()='Add Products']"));
    private static final Locator CONFIRM_SAVE_BTN = new Locator("CONFIRM_SAVE_BTN", By.xpath("(//button[text()='Save'])[2]"));

    private static final Locator SELECT_PRODUCT_DROPDOWN = new Locator("SELECT_PRODUCT_DROPDOWN", By.xpath("(//button[contains(@class,'styles_clickable_icon')])[5]"));
    private static final Locator CREATE_BUTTON = new Locator("CREATE_BUTTON", By.xpath("//button[text()='Create' and contains(@class, 'Button_button')]"));
    private static final Locator CHANNEL_DROPDOWN_BUTTON = new Locator("CHANNEL_DROPDOWN_BUTTON", By.xpath("//input[@aria-label='channelId Dropdown']/following-sibling::div[contains(@class,'right_slot_wrapper')]//button"));
    private static final Locator LOGIN_STATUS_DROPDOWN_BUTTON = new Locator("LOGIN_STATUS_DROPDOWN_BUTTON", By.xpath("//input[@aria-label='login Dropdown']/following-sibling::div[contains(@class,'right_slot_wrapper')]//button"));
    private static final Locator TESTING_CONTENT_API_INPUT = new Locator("TESTING_CONTENT_API_INPUT", By.id("testing-content-api-optional"));
    private static final Locator PRODUCTION_CONTENT_API_INPUT = new Locator("PRODUCTION_CONTENT_API_INPUT", By.id("production-content-api-optional"));
    private static final Locator INVENTORY = new Locator("INVENTORY", By.xpath("//label[.//span[normalize-space(text())='TIKET']]//span[contains(@class, 'Check_custom_checkbox')]"));
    private static final Locator RATE_KEY = new Locator("RATE_KEY", By.xpath("//label[.//span[normalize-space(text())='Default']]//span[contains(@class, 'Check_custom_checkbox')]"));
    private static final Locator PROMOS = new Locator("PROMOS", By.xpath("//label[.//span[normalize-space(text())='Apps Deal']]//span[contains(@class, 'Check_custom_checkbox')]"));
    private static final Locator RATE_PLANS = new Locator("RATE_PLANS", By.xpath("//label[.//span[normalize-space(text())='Breakfast']]//span[contains(@class, 'Check_custom_checkbox')]"));
    private static final Locator MAX_PROPERTY_IDS_INPUT = new Locator("MAX_PROPERTY_IDS_INPUT", By.xpath("//input[@id='max-number-of-property-ids-in-a-search-request']"));
    private static final Locator CHANNEL_ID_INPUT = new Locator("CHANNEL_ID_INPUT", By.id("channel-id"));
    private static final Locator RATE_TYPE_DROPDOWN_BUTTON = new Locator("RATE_TYPE_DROPDOWN_BUTTON", By.xpath("//input[@aria-label='rateType Dropdown']/following-sibling::div[contains(@class,'right_slot_wrapper')]//button"));
    private static final Locator SLA_ISSUANCE_INPUT = new Locator("SLA_ISSUANCE_INPUT", By.xpath("//input[@id='in-seconds']"));
    private static final Locator NHA_CHECK_IN_GUIDE_INPUT = new Locator("NHA_CHECK_IN_GUIDE_INPUT", By.xpath("//input[@id='email-recipient']"));

    private static final Locator SEARCH_SLA_SEC = new Locator("SEARCH_SLA_SEC", By.name("searchSLA"));
    private static final Locator ISSUANCE_SLA_SEC = new Locator("ISSUANCE_SLA_SEC", By.name("issuanceSLA"));
    private static final Locator BOOKING_SLA_SEC = new Locator("BOOKING_SLA_SEC", By.name("bookingSLA"));

    private static final Locator REFUND_CHARGE_INPUT = new Locator(
            "REFUND_CHARGE_INPUT",
            By.xpath("//input[@name='refundCharge' or @id='amount-in-reseller-preferred-currency' or @id='amount-in-idr']"));


    private static final Locator INVENTORY_VALUE = new Locator("INVENTORY_VALUE", By.xpath("//span[contains(@class,'Check_children')]"));
    private static final Locator PARTNER_ENTITY = new Locator("PARTNER_ENTITY", By.xpath("//span[text()='Agoda']"));
    private static final Locator REFUND_CHARGE = new Locator("REFUND_CHARGE", By.xpath("//input[@id='amount-in-reseller-preferred-currency']"));
    private static final Locator EMAIL = new Locator("EMAIL", By.xpath("//input[@id='email-recipient']"));

    public String createAndAddNewPartner(String country, String tiketEntity, String product, String settlementType, String currency) {

        String companyName = "Automation " + CoreFunctions.randomNameWithoutTest() + CoreFunctions.randomInt(4);

        driverTools.waitForElementToAppear(COMPANY_NAME, 5);
        driverTools.sendKeys(COMPANY_NAME, companyName);
        driverTools.click(COUNTRY_DROPDOWN);
        driverTools.click(getDropdown(country));

        driverTools.click(TIKET_ENTITY_DROPDOWN);
        Locator tiketEntityItem = getTiketEntityItem(tiketEntity);
        driverTools.waitForElementToAppear(tiketEntityItem, 5);
        driverTools.scrollToElement(tiketEntityItem);
        driverTools.clickWithJavascriptExecutor(tiketEntityItem);

        driverTools.scrollToElement(SETTLEMENT_TYPE_DROPDOWN);
        driverTools.clickWithJavascriptExecutor(SETTLEMENT_TYPE_DROPDOWN);
        driverTools.click(getDropdown(settlementType));

        driverTools.scrollToElement(CURRENCY_DROPDOWN);
        driverTools.clickWithJavascriptExecutor(CURRENCY_DROPDOWN);
        driverTools.click(getDropdown(currency));

        driverTools.sendKeys(FIRST_NAME, CoreFunctions.randomNameWithoutTest());
        driverTools.sendKeys(LAST_NAME, CoreFunctions.randomLastName());
        driverTools.sendKeys(PARTNER_EMAIL, CoreFunctions.randomEmail());
        driverTools.sendKeys(PARTNER_PHONE, CoreFunctions.randomPhone());

        driverTools.waitForElementToAppear(SAVE_BTN, 5);
        driverTools.scrollToElement(SAVE_BTN);
        driverTools.click(SAVE_BTN);
        driverTools.waitForElementToAppear(CONFIRM_SAVE_BTN, 5);
        driverTools.click(CONFIRM_SAVE_BTN);
        driverTools.waitForElementToAppear(ADD_PRODUCT_BTN, 5);
        driverTools.clickWithJavascriptExecutor(ADD_PRODUCT_BTN);
        driverTools.waitForElementToAppear(SELECT_PRODUCT_DROPDOWN, 5);
        driverTools.click(SELECT_PRODUCT_DROPDOWN);
        driverTools.click(getDropdown(product));
        driverTools.click(CREATE_BUTTON);

        return companyName;
    }

    public Locator getDropdown(String dropdownName) {
        return new Locator("DROPDOWN_ITEM", By.xpath("//*[text()='" + dropdownName + "']"));
    }
    
    private static void requireNonBlankEntityLabel(String entityLabel) {
        if (entityLabel == null || entityLabel.isBlank()) {
            throw new IllegalArgumentException(
                    "tiketEntity must not be null or blank when selecting a tiket entity item (check test data / param.get(\"tiketEntity\"))");
        }
    }

    private static String toXPathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        StringBuilder xpathLiteral = new StringBuilder("concat(");
        String[] parts = value.split("'", -1);
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                xpathLiteral.append(", \"'\", ");
            }
            xpathLiteral.append("'").append(parts[i]).append("'");
        }
        xpathLiteral.append(")");
        return xpathLiteral.toString();
    }

    public Locator getTiketEntityItem(String entityLabel) {
        requireNonBlankEntityLabel(entityLabel);
        return new Locator(
                "TIKET_ENTITY_" + entityLabel.replace(' ', '_'),
                By.xpath("//li[@data-name='entity' and @data-label=" + toXPathLiteral(entityLabel) + "]"));
    }

    public void addHotelAPI(String channel, String loginStatus, String testingContent, String productionContent, String maxProperty, String emailRecipient, String refundChargeAmount, String issuanceSLAHotel, String bookingSLA) {
        driverTools.click(CHANNEL_DROPDOWN_BUTTON);
        driverTools.waitForElementToAppear(getDropdown(channel), 5);
        driverTools.click(getDropdown(channel));

        driverTools.scrollToElement(TESTING_CONTENT_API_INPUT);
        driverTools.sendKeys(TESTING_CONTENT_API_INPUT, testingContent);
        driverTools.sendKeys(PRODUCTION_CONTENT_API_INPUT, productionContent);

        driverTools.click(LOGIN_STATUS_DROPDOWN_BUTTON);
        driverTools.waitForElementToAppear(getDropdown(loginStatus), 5);
        driverTools.click(getDropdown(loginStatus));

        driverTools.scrollToElement(RATE_KEY);
        driverTools.click(INVENTORY);
        driverTools.click(RATE_KEY);
        driverTools.scrollToElement(RATE_PLANS);
        driverTools.click(PROMOS);
        driverTools.click(RATE_PLANS);

        driverTools.scrollToElement(MAX_PROPERTY_IDS_INPUT);
        driverTools.sendKeys(MAX_PROPERTY_IDS_INPUT, maxProperty);

        driverTools.scrollToElement(REFUND_CHARGE_INPUT);
        driverTools.click(REFUND_CHARGE_INPUT);
        driverTools.clearInput(REFUND_CHARGE_INPUT);
        driverTools.sendKeys(REFUND_CHARGE_INPUT, refundChargeAmount);

        driverTools.scrollToElement(SLA_ISSUANCE_INPUT);
        driverTools.click(SLA_ISSUANCE_INPUT);
        driverTools.sendKeys(SLA_ISSUANCE_INPUT, issuanceSLAHotel);

        driverTools.scrollToElement(BOOKING_SLA_SEC);
        driverTools.sendKeys(BOOKING_SLA_SEC, bookingSLA);

        driverTools.scrollToElement(NHA_CHECK_IN_GUIDE_INPUT);
        driverTools.click(NHA_CHECK_IN_GUIDE_INPUT);
        driverTools.sendKeys(NHA_CHECK_IN_GUIDE_INPUT, emailRecipient);

        driverTools.scrollToElement(SAVE_BTN);
        driverTools.waitForElementToAppear(SAVE_BTN, 5);
        driverTools.click(SAVE_BTN);

        driverTools.waitForElementToAppear(CONFIRM_SAVE_BTN, 5);
        driverTools.click(CONFIRM_SAVE_BTN);
    }

    public void addHotelAPIWithMoreFields(String channel, String loginStatus, String testingContent, String productionContent, String maxProperty, String refundChargeAmount, String issuanceSLA, String bookingSLA, String emailRecipient) {
        driverTools.click(CHANNEL_DROPDOWN_BUTTON);
        driverTools.waitForElementToAppear(getDropdown(channel), 5);
        driverTools.click(getDropdown(channel));

        driverTools.scrollToElement(TESTING_CONTENT_API_INPUT);
        driverTools.sendKeys(TESTING_CONTENT_API_INPUT, testingContent);
        driverTools.sendKeys(PRODUCTION_CONTENT_API_INPUT, productionContent);

        driverTools.click(LOGIN_STATUS_DROPDOWN_BUTTON);
        driverTools.waitForElementToAppear(getDropdown(loginStatus), 5);
        driverTools.click(getDropdown(loginStatus));

        driverTools.scrollToElement(RATE_KEY);
        driverTools.click(PARTNER_ENTITY);
        driverTools.click(INVENTORY_VALUE);
        driverTools.click(RATE_KEY);
        driverTools.scrollToElement(RATE_PLANS);
        driverTools.click(PROMOS);
        driverTools.click(RATE_PLANS);

        driverTools.scrollToElement(MAX_PROPERTY_IDS_INPUT);
        driverTools.sendKeys(MAX_PROPERTY_IDS_INPUT, maxProperty);

        driverTools.clickWithJavascriptExecutor(REFUND_CHARGE);
        driverTools.deleteUsingBackspace(REFUND_CHARGE);
        driverTools.sendKeys(REFUND_CHARGE, refundChargeAmount);

        driverTools.scrollToElement(ISSUANCE_SLA_SEC);
        driverTools.sendKeys(ISSUANCE_SLA_SEC, issuanceSLA);

        driverTools.scrollToElement(BOOKING_SLA_SEC);
        driverTools.sendKeys(BOOKING_SLA_SEC, bookingSLA);

        driverTools.scrollToElement(EMAIL);
        driverTools.sendKeys(EMAIL, emailRecipient);

        driverTools.scrollToElement(SAVE_BTN);
        driverTools.waitForElementToAppear(SAVE_BTN, 5);
        driverTools.click(SAVE_BTN);

        driverTools.waitForElementToAppear(CONFIRM_SAVE_BTN, 5);
        driverTools.click(CONFIRM_SAVE_BTN);
    }


    public void addFlightAPI(String channelId, String rateType,String secondSearchSLA,String issuanceSLA,String refundCharge) {
        driverTools.waitForElementToBeClickable(CHANNEL_ID_INPUT, 3);
        driverTools.clickWithJavascriptExecutor(CHANNEL_ID_INPUT);
        driverTools.sendKeys(CHANNEL_ID_INPUT, channelId);
        driverTools.click(RATE_TYPE_DROPDOWN_BUTTON);
        driverTools.scrollToElement(RATE_TYPE_DROPDOWN_BUTTON);
        driverTools.click(getDropdown(rateType));

        driverTools.clickWithJavascriptExecutor(SEARCH_SLA_SEC);
        driverTools.sendKeys(SEARCH_SLA_SEC, secondSearchSLA);
        driverTools.clickWithJavascriptExecutor(ISSUANCE_SLA_SEC);
        driverTools.sendKeys(ISSUANCE_SLA_SEC, issuanceSLA);

        driverTools.scrollToElement(REFUND_CHARGE_INPUT);
        driverTools.clickWithJavascriptExecutor(REFUND_CHARGE_INPUT);
        driverTools.clearInput(REFUND_CHARGE_INPUT);
        driverTools.sendKeys(REFUND_CHARGE_INPUT, refundCharge);

        driverTools.scrollToElement(SAVE_BTN);
        driverTools.waitForElementToAppear(SAVE_BTN, 5);
        driverTools.click(SAVE_BTN);

        driverTools.waitForElementToAppear(CONFIRM_SAVE_BTN, 5);
        driverTools.click(CONFIRM_SAVE_BTN);
    }

    public String createNewPartner(String country, String tiketEntity, String settlementType, String currency) {

        String companyName = "Automation " + CoreFunctions.randomNameWithoutTest() + CoreFunctions.randomInt(4);

        driverTools.waitForElementToAppear(COMPANY_NAME, 5);
        driverTools.sendKeys(COMPANY_NAME, companyName);
        driverTools.click(COUNTRY_DROPDOWN);
        driverTools.click(getDropdown(country));

        driverTools.click(TIKET_ENTITY_DROPDOWN);
        Locator tiketEntityItem = getTiketEntityItem(tiketEntity);
        driverTools.waitForElementToAppear(tiketEntityItem, 5);
        driverTools.scrollToElement(tiketEntityItem);
        driverTools.clickWithJavascriptExecutor(tiketEntityItem);

        driverTools.click(SETTLEMENT_TYPE_DROPDOWN);
        driverTools.click(getDropdown(settlementType));

        driverTools.click(CURRENCY_DROPDOWN);
        driverTools.click(getDropdown(currency));

        driverTools.sendKeys(FIRST_NAME, CoreFunctions.randomNameWithoutTest());
        driverTools.sendKeys(LAST_NAME, CoreFunctions.randomLastName());
        driverTools.sendKeys(PARTNER_EMAIL, CoreFunctions.randomEmail());
        driverTools.sendKeys(PARTNER_PHONE, CoreFunctions.randomPhone());

        driverTools.waitForElementToAppear(SAVE_BTN, 5);
        driverTools.click(SAVE_BTN);
        driverTools.waitForElementToAppear(CONFIRM_SAVE_BTN, 5);
        driverTools.click(CONFIRM_SAVE_BTN);
        driverTools.waitForElementToAppear(LATER_BTN, 5);
        driverTools.click(LATER_BTN);

        return companyName;
    }
    
}