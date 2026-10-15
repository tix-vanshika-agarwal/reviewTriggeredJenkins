package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;



public class AffiliateOrderTableInternalDashboard<T extends WebDriver> extends BasePage<T> {
    public AffiliateOrderTableInternalDashboard(Session<T> session) {
        super(session);

    }
    private static final Locator ORDER_TABLE = new Locator("ORDER_TABLE", By.xpath("//*[self::p or self::span][normalize-space()='Order Table']"));
    private static final Locator ITINERARY_ID = new Locator("ITINERARY_ID", new By.ByXPath("//span[normalize-space()='Itinerary ID']"));
    private static final Locator ITINERARY_ID_TEXTFIELD = new Locator("ITINERARY_ID_TEXTFIELD", new By.ByXPath("//input[@id='itinerary-id']"));
    private static final Locator ORDER_TABLE_SEARCH_BTN = new Locator("ORDER_TABLE_SEARCH_BTN", new By.ByXPath("//button[normalize-space()='Search']"));
    private static final Locator ORDER_TABLE_ITINERARY_ID_DISPLAYED = new Locator("ORDER_TABLE_ITINERARY_ID_DISPLAYED", new By.ByXPath("//p[normalize-space()='b2b']"));
    private static final Locator ORDER_ID_TEXTFIELD = new Locator("ORDER_ID_TEXTFIELD", new By.ByXPath("//input[@placeholder='Order ID']"));
    private static final Locator ORDER_TABLE_RESET_BTN = new Locator("ORDER_TABLE_RESET_BTN", new By.ByXPath("//button[normalize-space()='Reset Filter']"));
    private static final Locator AFFILIATE_AND_REFERENCE_ID_BUTTON = new Locator("AFFILIATE_AND_REFERENCE_ID_BUTTON", By.xpath("//button[.//span[normalize-space()='Affiliate and Reference ID']]"));
    private static final Locator SEARCH_PARTNER_INPUT = new Locator("SEARCH_PARTNER_INPUT", By.id("search-partner"));
    private static final Locator FIRST_PARTNER_DROPDOWN_OPTION = new Locator("FIRST_PARTNER_DROPDOWN_OPTION", By.xpath("//ul[contains(@class,'InputSearch_dropdown__xk0ct')]//li[1]"));
    private static final Locator AFFILIATE_REF_ID_INPUT = new Locator("AFFILIATE_REF_ID_INPUT", By.id("affiliate-ref-id"));
    private static final Locator AFFILIATE_REF_ID_DISPLAYED = new Locator("AFFILIATE_REF_ID_DISPLAYED", By.xpath("//p[normalize-space()='Affiliate Ref ID']/following-sibling::div//p[contains(@class,'text-ellipsis')]"));
    private static final Locator PARTNER_NAME_DISPLAYED = new Locator("PARTNER_NAME_DISPLAYED", By.xpath("//p[normalize-space()='Partner Name']/following-sibling::text()[1]"));
    private static final Locator REFUND_DETAILS_BUTTON = new Locator("REFUND_DETAILS_BUTTON", By.xpath("//button[normalize-space()='View Details']"));

    public void clickOrderTable() {
        driverTools.waitForElementToAppear(ORDER_TABLE, 10);
        driverTools.click(ORDER_TABLE);
    }

    public void clickItinerary() {
        driverTools.click(ITINERARY_ID);
    }

    public void enterItineraryId(String itineraryId) {
        driverTools.waitForElementToAppear(ITINERARY_ID_TEXTFIELD);
        driverTools.click(ITINERARY_ID_TEXTFIELD);
        driverTools.sendKeys(ITINERARY_ID_TEXTFIELD,itineraryId);
    }

    public void clickOrderTableSearchButton() {
        driverTools.click(ORDER_TABLE_SEARCH_BTN);
    }

    public boolean checkDataDisplayed() {
        driverTools.waitForElementToAppear(ORDER_TABLE_ITINERARY_ID_DISPLAYED);
        return driverTools.isElementEnabled(ORDER_TABLE_ITINERARY_ID_DISPLAYED);

    }

    public void enterOrderId(String orderId) {
        driverTools.waitForElementToAppear(ORDER_ID_TEXTFIELD);
        driverTools.click(ORDER_ID_TEXTFIELD);
        driverTools.sendKeys(ORDER_ID_TEXTFIELD,orderId);
    }

    public void clickOrderTableResetButton() {
        driverTools.click(ORDER_TABLE_RESET_BTN);
    }

    public boolean isEnableOrderTableResetButton() {
        return driverTools.isElementEnabled(ORDER_TABLE_RESET_BTN);
    }

    public void clickAffiliateAndReferenceIdButton() {
        driverTools.click(AFFILIATE_AND_REFERENCE_ID_BUTTON);
    }

    public void searchByPartnerAndReferenceId(String partnerName, String affiliateRefId) {
        driverTools.waitForElementToAppear(SEARCH_PARTNER_INPUT);
        driverTools.sendKeys(SEARCH_PARTNER_INPUT, partnerName);
        driverTools.waitForElementToAppear(PARTNER_NAME_DISPLAYED, 1);
        driverTools.click(FIRST_PARTNER_DROPDOWN_OPTION);
        driverTools.sendKeys(AFFILIATE_REF_ID_INPUT, affiliateRefId);
    }

    public boolean isAffiliateRefIdDisplayed(String affiliateRefId) {
        driverTools.waitForElementToAppear(AFFILIATE_REF_ID_DISPLAYED);
        return driverTools.getText(AFFILIATE_REF_ID_DISPLAYED).get().contains(affiliateRefId);
    }

    public void goToRefundInfo() {
        driverTools.waitForElementToAppear(REFUND_DETAILS_BUTTON, 10);
        int handleCountBeforeClick = driverTools.getWindowHandles().size();
        driverTools.clickWithJavascriptExecutor(REFUND_DETAILS_BUTTON);
        driverTools.waitForNewTabAndSwitchToLatest(handleCountBeforeClick);
    }
}