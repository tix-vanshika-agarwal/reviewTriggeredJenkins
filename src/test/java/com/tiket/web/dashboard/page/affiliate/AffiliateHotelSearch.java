package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateHotelSearch<T extends WebDriver> extends BasePage<T> {
    public AffiliateHotelSearch(Session<T> session) {
        super(session);
    }

    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[text()='Search' and contains(@class, 'Button_variant_secondary')]"));
    private static final Locator PARTNER_ID_INPUT = new Locator("PARTNER_ID_INPUT", By.xpath("//input[@id='partner-id']"));
    private static final Locator PARTNER_NAME_INPUT = new Locator("PARTNER_NAME_INPUT", By.id("partner-name"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[contains(text(),'Reset Filter')]"));
    private static final Locator SEARCH_DROPDOWN = new Locator("SEARCH_DROPDOWN", By.xpath("//input[@id='search-by' and @readonly]"));
    private static final Locator HOTEL_ID_OPTION = new Locator("HOTEL_ID_OPTION", By.xpath("//li[p[normalize-space(text())='Hotel ID']]"));
    private static final Locator KEYWORD_INPUT = new Locator("KEYWORD_INPUT", By.id("keyword"));

    public void searchByPartnerId(String partnerId){
        driverTools.waitForElementToBeClickable(PARTNER_ID_INPUT, 5);
        driverTools.sendKeys(PARTNER_ID_INPUT, partnerId);
        clickSearchButton();
    }

    public void searchByPartnerName(String partnerName){
        driverTools.waitForElementToBeClickable(PARTNER_NAME_INPUT, 5);
        driverTools.sendKeys(PARTNER_NAME_INPUT, partnerName);
        clickSearchButton();
    }

    public void clickSearchButton(){
        driverTools.click(SEARCH_BUTTON);
    }

    public void clickResetFilter(){
        driverTools.click(RESET_FILTER_BUTTON);
    }

    public void searchByHotelId(String hotelId){
        driverTools.waitForElementToAppear(SEARCH_DROPDOWN);
        driverTools.click(SEARCH_DROPDOWN);
        driverTools.waitForElementToAppear(HOTEL_ID_OPTION);
        driverTools.click(HOTEL_ID_OPTION);
        driverTools.sendKeys(KEYWORD_INPUT, hotelId);
        driverTools.click(SEARCH_BUTTON);
    }

}