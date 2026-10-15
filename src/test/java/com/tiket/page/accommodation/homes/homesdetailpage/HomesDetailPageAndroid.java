package com.tiket.page.accommodation.homes.homesdetailpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomesDetailPageAndroid<T extends WebDriver> extends HomesDetailPage<T> {

    public HomesDetailPageAndroid(Session<T> session) { super(session); }

    public static final Locator LOYALTY_BENEFIT = new Locator("LOYALTY_BENEFIT", AppiumBy.id("tv_loyalty"));
    private static final Locator SIMILAR_PLACES_BUTTON = new Locator("SIMILAR_PLACES_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (contains(@text,'Similar places') or contains(@text,'Akomodasi serupa'))]"));
    private static final Locator FIRST_SIMILAR_HOTEL_NAME = new Locator("FIRST_SIMILAR_HOTEL_NAME", AppiumBy.xpath("(//*[contains(@resource-id,'rv_similar_hotel')]//*[contains(@resource-id,'tv_hotel_name')])[1]"));
    private static final Locator FIRST_SIMILAR_HOTEL_LOCATION = new Locator("FIRST_SIMILAR_HOTEL_LOCATION", AppiumBy.xpath("(//*[contains(@resource-id,'rv_similar_hotel')]//*[contains(@resource-id,'tv_location_info')])[1]"));

    @Override
    public boolean checkLoyaltyBenefit() {
        boolean loyaltyBenefit = driverTools.swipeToElement(LOYALTY_BENEFIT, Swipe.UP, 5).isPresent();
        return loyaltyBenefit;
    }

    @Override
    public boolean isSimilarPlacesButtonShown() {
        return driverTools.swipeToElement(SIMILAR_PLACES_BUTTON, Swipe.UP, 20).isPresent();
    }

    @Override
    public void clickSimilarPlacesButton() {
        WebElement similarPlacesButton = driverTools.swipeToElement(SIMILAR_PLACES_BUTTON, Swipe.UP, 20).orElseThrow();
        driverTools.click(similarPlacesButton);
    }

    @Override
    public String getFirstSimilarPlaceName() {
        WebElement hotelName = driverTools.swipeToElement(FIRST_SIMILAR_HOTEL_NAME, Swipe.UP, 10).orElseThrow();
        String name = driverTools.getText(hotelName);
        logger.log("first similar place name -> " + name);
        return name;
    }

    @Override
    public String getFirstSimilarPlaceLocation() {
        WebElement hotelLocation = driverTools.swipeToElement(FIRST_SIMILAR_HOTEL_LOCATION, Swipe.UP, 10).orElseThrow();
        String location = driverTools.getText(hotelLocation);
        logger.log("first similar place location -> " + location);
        return location;
    }

    @Override
    public void selectFirstSimilarPlaceHotel() {
        WebElement hotel = driverTools.swipeToElement(FIRST_SIMILAR_HOTEL_NAME, Swipe.UP, 10).orElseThrow();
        driverTools.click(hotel);
    }
}