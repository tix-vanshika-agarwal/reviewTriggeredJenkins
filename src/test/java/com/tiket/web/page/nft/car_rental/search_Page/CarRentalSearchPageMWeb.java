package com.tiket.web.page.nft.car_rental.search_Page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.tiket.web.page.nft.car_rental.searchResult_Page.CarRentalSearchResultPageWeb.DATE_FORM;
import static com.tiket.web.page.nft.car_rental.searchResult_Page.CarRentalSearchResultPageWeb.LOCATION_FORM;

public class CarRentalSearchPageMWeb <T extends WebDriver> extends CarRentalSearchPageWeb<T> {
    private static final Locator LOCATION_FORM_SRP = new Locator("LOCATION_FORM_SRP", AppiumBy.xpath("//span[contains(@class,'SRPHeaderMobile_rent_place')]"));
    private static final Locator DATE_FORM_SRP = new Locator("DATE_FORM_SRP", AppiumBy.xpath("//span[contains(@class,'SRPHeaderMobile_schedule_rent_data')]"));
    private static final Locator FAQ_LINK = new Locator("FAQ_LINK", new By.ByXPath("(//a[contains(@id, 'notification-banner')])[1]"));

    public CarRentalSearchPageMWeb(Session<T> session) {
        super(session);
    }

    public Locator getDurationTextLocator(String duration) {

        return new Locator("DURATION_TEXT", new By.ByXPath("(//div[contains(@class, 'WheelPicker_slider') and starts-with(text(), '" + duration + "')])[1]"));
    }

    public void selectDuration(String duration) {
        driverTools.clickWithJavascriptExecutor(DURATION_INPUT_TEXT);
        Locator DURATION_TEXT = getDurationTextLocator(duration);
        WebElement durationText = driverTools.waitForElementToAppear(DURATION_TEXT).orElseThrow();
        driverTools.scrollIntoWebView(durationText);
        driverTools.clickWithJavascriptExecutor(DURATION_TEXT);
        driverTools.waitForElementToAppear(SAVE_BUTTON).ifPresent(driverTools::click);
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
                String locationText = driverTools.getText(locationForm);
                if (locationText.startsWith("Rent in ")) {
                    locationText = locationText.substring(8);
                } else if (locationText.startsWith("Sewa Mobil di ")) {
                    locationText = locationText.substring(14);
                }
                return locationText;
            } else if(type.equals("date")){
                WebElement dateForm = driverTools.element(DATE_FORM_SRP).orElseThrow();
                return driverTools.getText(dateForm).replaceAll("(\\d{1,2} [A-Za-z]{3}).*", "$1").trim();
            } else if(type.equals("duration")){
                WebElement durationForm = driverTools.element(DATE_FORM_SRP).orElseThrow();
                String text = driverTools.getText(durationForm);
                String duration = text.replaceAll("(?i).*?(\\d+\\s*(?:-|–)?\\s*(?:day|days|night|nights|hour|hours)).*", "$1").trim();
                return duration;
            }
            else{
                WebElement durationForm = driverTools.element(DATE_FORM_SRP).orElseThrow();
                return driverTools.getText(durationForm)
                    .replaceAll(".*?(With Driver|Self Drive|Dengan Sopir|Lepas Kunci)$", "$1")
                    .trim();
            }
        }
    }

    public String clickFaqLink() {
        WebElement faqLink = driverTools.waitForElementToAppear(FAQ_LINK).orElseThrow();
        driverTools.scrollIntoWebView(faqLink);
        driverTools.forceOpenLinkInSameTab();
        driverTools.clickWithJavascriptExecutor(FAQ_LINK);
        String currentUrl = driver.getCurrentUrl();
        logger.log("current url:" + currentUrl);
        driver.navigate().back();
        return currentUrl;
    }
}