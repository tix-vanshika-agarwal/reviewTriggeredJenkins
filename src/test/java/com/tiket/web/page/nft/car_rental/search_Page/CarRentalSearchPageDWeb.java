package com.tiket.web.page.nft.car_rental.search_Page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static com.tiket.web.page.nft.car_rental.searchResult_Page.CarRentalSearchResultPageWeb.DATE_FORM;
import static com.tiket.web.page.nft.car_rental.searchResult_Page.CarRentalSearchResultPageWeb.LOCATION_FORM;

public class CarRentalSearchPageDWeb <T extends WebDriver> extends CarRentalSearchPageWeb<T> {

    private static final Locator FAQ_LINK = new Locator("FAQ_LINK", new By.ByXPath("(//a[contains(@id, 'notification-banner')])[2]"));
    private static final Locator LOCATION_FORM_SRP = new Locator("LOCATION_FORM_SRP", AppiumBy.xpath("(//span[contains(@class,'ChangeSearch_pickup_location')])[2]"));
    private static final Locator DATE_FORM_SRP = new Locator("DATE_FORM_SRP", AppiumBy.xpath("(//div[contains(@class,'ChangeSearch_date')]/span)[2]"));
    private static final Locator DRIVER_FORM = new Locator("DRIVER_FORM", AppiumBy.xpath("(//span[contains(@class,'ChangeSearch_driver_option')])[2]"));

    public CarRentalSearchPageDWeb(Session<T> session) {
        super(session);
    }

    public Locator getDurationTextLocator(String duration) {

        return new Locator("DURATION_TEXT", new By.ByXPath("(//*[contains(@class, 'List_title') and starts-with(text(), '" + duration + "')])[1]"));
    }

    public void selectDuration(String duration) {
        driverTools.clickWithJavascriptExecutor(DURATION_INPUT_TEXT);
        Locator DURATION_TEXT = getDurationTextLocator(duration);
        WebElement durationText = driverTools.waitForElementToAppear(DURATION_TEXT).orElseThrow();
        driverTools.scrollIntoWebView(durationText);
        driverTools.clickWithJavascriptExecutor(DURATION_TEXT);
    }

    public String getFormValue(String type, String page){
        if(page.equals("SP")){
            if(type.equals("location")){
                WebElement locationForm = driverTools.element(LOCATION_FORM).orElseThrow();
                return driverTools.getText(locationForm);
            } else{
                WebElement dateForm = driverTools.element(DATE_FORM).orElseThrow();
                return driverTools.getText(dateForm).split(",")[1].trim();
            }
        } else{
            if(type.equals("location")){
                WebElement locationForm = driverTools.element(LOCATION_FORM_SRP).orElseThrow();
                return driverTools.getText(locationForm);
            } else if(type.equals("date")){
                WebElement dateForm = driverTools.waitForElementToAppear(DATE_FORM_SRP, 5).orElseThrow();
                return driverTools.getText(dateForm).split(",")[1].trim().replaceAll("\\s+25$", "");
            } else{
                WebElement driverForm = driverTools.waitForElementToAppear(DRIVER_FORM).orElseThrow();
                return driverTools.getText(driverForm);
            }
        }
    }

    public String clickFaqLink() {
        WebElement faqLink = driverTools.waitForElementToAppear(FAQ_LINK).orElseThrow();
        driverTools.scrollIntoWebView(faqLink);
        driverTools.clickWithJavascriptExecutor(FAQ_LINK);

        // get all window handles
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());

        // switch to second tab (index 1)
        driver.switchTo().window(tabs.get(1));

        // get current URL of second tab
        String currentUrl = driver.getCurrentUrl();
        logger.log("current url:" + currentUrl);

        // close second tab
        driver.close();

        // switch back to first tab
        driver.switchTo().window(tabs.get(0));
        return currentUrl;
    }
}