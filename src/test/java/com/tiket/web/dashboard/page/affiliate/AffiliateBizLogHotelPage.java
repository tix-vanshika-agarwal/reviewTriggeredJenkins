package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.b2b.corporate.OrderDetailResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;

public class AffiliateBizLogHotelPage<T extends WebDriver> extends BasePage<T> {
    public AffiliateBizLogHotelPage(Session<T> session){super(session);}

    private static final Locator HOTEL_API = new Locator("HOTEL_API", By.xpath("//p[text()='Hotel API']"));
    private static final Locator BUSINESS_LOG_MENU = new Locator("BUSINESS_LOG_MENU", By.xpath("//div[normalize-space()='Business Log']"));
    private static final Locator SEARCH_PARTNER_FIELD = new Locator("SEARCH_PARTNER_FIELD", By.xpath("//input[@id='search-by-partners-name-or-id']"));
    private static final Locator SEARCH_BTN = new Locator("SEARCH_BTN", By.xpath("//button[normalize-space()='Search']"));
    private Locator getHotelBizLogLocator(String partnerName) {
        String xpathTemplate = "//p[normalize-space(text())='%s']/following-sibling::p[contains(@class, 'text-ellipsis')]";
        String finalXpath = String.format(xpathTemplate, partnerName);
        return new Locator("Value for partner: " + partnerName, By.xpath(finalXpath));
    }

    public void goToAffiliateHotelBusinessLog(){
        if (Configs.ENVIRONMENT == Environment.GK){
            driver.get("https://gatotkaca.tiket.com/admin/affiliate");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD){
            driver.get("https://preprod.tiket.com/admin/affiliate");
        } else if (Configs.ENVIRONMENT == Environment.PROD){
            driver.get("https://tiket.com/admin/affiliate");
        }
    }

    public void clickHotelAPIBusinessLogMenu(){
        driverTools.waitForElementToAppear(HOTEL_API,5);
        driverTools.click(HOTEL_API);
        driverTools.waitForElementToAppear(BUSINESS_LOG_MENU, 5);
        driverTools.click(BUSINESS_LOG_MENU);
    }

    public void insertHotelAPIBizLogPartnerID(String partnerId){
        driverTools.waitForElementToAppear(SEARCH_PARTNER_FIELD,5);
        driverTools.click(SEARCH_PARTNER_FIELD);
        driverTools.sendKeys(SEARCH_PARTNER_FIELD, partnerId);
    }

    public void clickSearchHotelBizlogByID(){
        driverTools.click(SEARCH_BTN);
    }

    public String getPartnerIDResult(String partnerName) {
        Locator firstOrderData = getHotelBizLogLocator(partnerName);
        driverTools.waitForElementToAppear(firstOrderData, 4).orElseThrow();
        return driverTools.getText(firstOrderData).orElseThrow();
    }
}
