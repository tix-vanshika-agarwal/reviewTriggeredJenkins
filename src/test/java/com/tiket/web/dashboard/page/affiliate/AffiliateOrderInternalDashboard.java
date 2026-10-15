package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AffiliateOrderInternalDashboard<T extends WebDriver> extends BasePage<T> {
    public AffiliateOrderInternalDashboard(Session<T> session) {
        super(session);
    }

    private static final Locator ORDER_MENU = new Locator("ORDER_MENU", By.xpath("//div[contains(@class, 'Menu_side__bar__menu__text')]//p[text()='Order']"));
    private static final Locator NON_API_PRODUCTS_MENU = new Locator("NON_API_PRODUCTS_MENU", By.xpath("//div[contains(@class, 'Menu_side__bar__menu__children__item') and normalize-space()='Non-API Products']"));
    private static final Locator SEARCH_BY_DROPDOWN = new Locator("SEARCH_BY_DROPDOWN", By.xpath("//div[contains(@class, 'InputSearch_select_search')]"));
    private static final Locator PARTNER_SEARCH_INPUT = new Locator("PARTNER_SEARCH_INPUT", By.xpath("//div[./input[@name='select-partner-id']]//input[@role='combobox']"));
    private static final Locator PARTNER_DROPDOWN_FIRST_OPTION = new Locator("PARTNER_DROPDOWN_FIRST_OPTION", By.xpath("//div[contains(@class, 'option')][1]"));
    private static final Locator START_DATE_BOX = new Locator("START_DATE_BOX", By.xpath("//input[@id='start-date']"));
    private static final Locator CAMPAIGN_TYPE_DROPDOWN = new Locator("CAMPAIGN_TYPE_DROPDOWN", By.xpath("//div[./input[@name='select-campaign-type']]//div[contains(@class, 'InputSearch_select_search')]"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[contains(@class, 'Button_variant_tertiary') and normalize-space()='Reset Filter']"));

    private String startDate;

    private Locator getSearchByOptionLocator(String optionText) {
        return new Locator("SEARCH_BY_" + optionText, By.xpath("//div[contains(@class, 'option') and contains(text(), '" + optionText + "')]"));
    }

    public void clickOrderMenu() {
        driverTools.waitForElementToAppear(ORDER_MENU, 5);
        driverTools.scrollToElement(ORDER_MENU);
        driverTools.click(ORDER_MENU);
    }

    public void clickNonApiProducts() {
        driverTools.waitForElementToAppear(NON_API_PRODUCTS_MENU, 5);
        driverTools.click(NON_API_PRODUCTS_MENU);
    }

    public void selectSearchBy(String optionText) {
        driverTools.waitForElementToAppear(SEARCH_BY_DROPDOWN, 5);
        driverTools.click(SEARCH_BY_DROPDOWN);
        Locator option = getSearchByOptionLocator(optionText);
        driverTools.waitForElementToAppear(option, 5);
        driverTools.click(option);
    }

    public void enterPartnerIdAndSelect(String partnerId) {
        driverTools.waitForElementToAppear(PARTNER_SEARCH_INPUT, 5);
        driverTools.click(PARTNER_SEARCH_INPUT);
        driverTools.sendKeys(PARTNER_SEARCH_INPUT, partnerId);
        driverTools.waitForElementToAppear(PARTNER_DROPDOWN_FIRST_OPTION, 5);
        driverTools.click(PARTNER_DROPDOWN_FIRST_OPTION);
    }

    public void selectCampaignType(String campaignType) {
        driverTools.waitForElementToAppear(CAMPAIGN_TYPE_DROPDOWN, 5);
        driverTools.click(CAMPAIGN_TYPE_DROPDOWN);
        Locator option = new Locator("CAMPAIGN_TYPE_" + campaignType, By.xpath("//div[contains(@class, 'option') and contains(text(), '" + campaignType + "')]"));
        driverTools.waitForElementToAppear(option, 5);
        driverTools.click(option);
    }

    public void setStartDate(int daysOffset) {
        driverTools.waitForElementToAppear(START_DATE_BOX);
        driverTools.click(START_DATE_BOX);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -daysOffset);
        startDate = new SimpleDateFormat("d MMMM yyyy EEEE", Locale.ENGLISH).format(calendar.getTime());
        Locator startDateLocator = new Locator("START_DATE", By.xpath("//span[@aria-label='" + startDate + "']"));
        driverTools.waitForElementToAppear(startDateLocator, 10);
        driverTools.click(startDateLocator);
    }

    public boolean isOrderDateOnOrAfterStartDate() {
        try {
            String orderDateText = getFirstRowColumnText(COL_DATE);
            SimpleDateFormat tableDateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
            SimpleDateFormat startDateFormat = new SimpleDateFormat("d MMMM yyyy EEEE", Locale.ENGLISH);
            Date orderDate = tableDateFormat.parse(orderDateText);
            Date start = startDateFormat.parse(startDate);
            return !orderDate.before(start);
        } catch (Exception e) {
            logger.log("Date parsing failed: " + e.getMessage());
            return false;
        }
    }

    public void clickResetFilter() {
        driverTools.waitForElementToAppear(RESET_FILTER_BUTTON, 5);
        driverTools.click(RESET_FILTER_BUTTON);
    }

    private Locator getFilterContainerLocator(String filterName) {
        return new Locator("FILTER_CONTAINER_" + filterName, By.xpath("//div[contains(@id,'placeholder') and normalize-space()='" + filterName + "']/ancestor::div[@aria-disabled]"));
    }

    public boolean isFilterDisabled(String filterName) {
        Locator container = getFilterContainerLocator(filterName);
        driverTools.waitForElementToAppear(container, 5);
        String ariaDisabled = driverTools.getValueAttribute(container, "aria-disabled");
        return "true".equals(ariaDisabled);
    }

    public boolean isStartDateDisabled() {
        driverTools.waitForElementToAppear(START_DATE_BOX, 5);
        return !driverTools.isElementEnabled(START_DATE_BOX);
    }

    public static final int COL_ORDER_ID = 1;
    public static final int COL_PARTNER_NAME = 2;
    public static final int COL_TYPE = 3;
    public static final int COL_DATE = 6;

    private Locator getFirstRowColumnLocator(int columnIndex) {
        return new Locator("FIRST_ROW_COL_" + columnIndex, By.xpath("(//tr[contains(@class, 'hover:bg')])[1]/td[" + columnIndex + "]"));
    }

    public String getFirstRowColumnText(int columnIndex) {
        Locator columnLocator = getFirstRowColumnLocator(columnIndex);
        driverTools.waitForElementToAppear(columnLocator, 5);
        return driverTools.getAttributeTextContent(columnLocator).orElseThrow().trim();
    }

    public boolean isFirstRowColumnContains(int columnIndex, String expectedText) {
        driverTools.dummyWait(4);
        return getFirstRowColumnText(columnIndex).contains(expectedText);
    }
}