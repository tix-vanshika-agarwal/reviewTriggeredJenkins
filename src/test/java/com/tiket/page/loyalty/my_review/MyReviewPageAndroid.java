package com.tiket.page.loyalty.my_review;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.home.HomePageAndroid;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class MyReviewPageAndroid <T extends WebDriver> extends MyReviewPage<T> {

    public MyReviewPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator OPEN_ALL_REVIEWS = new Locator("OPEN_ALL_REVIEWS", AppiumBy.xpath("//android.widget.Button[contains(@text,'Reviews') or contains(@text,'Review')]"));

    @Override
    public Boolean verifyAndClickReviewPopup() {
        WebElement e = driverTools.waitForElementToPresence(OPEN_ALL_REVIEWS).orElseThrow();
        Boolean isThankyouAppeared = driverTools.isElementDisplayed(e);
        driverTools.click(e);
        return isThankyouAppeared;
    }

    @Override
    public void clickReviewStar(String product) {
        driverTools.click(getReviewSliderLocator(product));
    }

    private Locator getReviewSliderLocator(String product) {
        return new Locator(product+"LOCATOR", AppiumBy.xpath("//*[@text='"+product+"']//following-sibling::*[contains(@resource-id,'slider')]"));
    }

    @Override
    public void swipeToCard(String product) {
        driverTools.swipeByDistanceToElement(HomePageAndroid.TIKET_POINTS_CONTAINER,HomePageAndroid.SCREEN,Swipe.DOWN,0.68,20).orElseThrow();
        driverTools.swipeDown(2);
        driverTools.swipeByDistanceToElement(getReviewSliderLocator(product),HomePageAndroid.SCREEN,Swipe.UP,0.35,35).orElseThrow();
        driverTools.swipeByDistance(HomePageAndroid.SCREEN,Swipe.UP,0.05);;
    }
}