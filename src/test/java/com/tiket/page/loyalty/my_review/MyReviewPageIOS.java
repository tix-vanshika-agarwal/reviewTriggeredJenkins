package com.tiket.page.loyalty.my_review;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.account.AccountPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.home.HomePageIOS;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class MyReviewPageIOS <T extends WebDriver> extends MyReviewPage<T> {

    public MyReviewPageIOS(Session<T> session) {
        super(session);
    }

    public static final Locator OPEN_ALL_REVIEWS = new Locator("OPEN_ALL_REVIEWS", AppiumBy.xpath("(//XCUIElementTypeButton[contains(@label,'Lihat Semua Review-mu') or contains(@label,'See All Reviews')])"));

    @Override
    public Boolean verifyAndClickReviewPopup() {
        Boolean isThankyouAppeared = driverTools.waitForElementToAppear(OPEN_ALL_REVIEWS).isPresent();
        driverTools.click(OPEN_ALL_REVIEWS);
        return isThankyouAppeared;
    }

    @Override
    public void clickReviewStar(String product) {
        driverTools.swipeElementToMidScreen(getReviewSliderLocator(product));
        driverTools.click(getReviewSliderLocator(product));
    }

    @Override
    public void swipeToCard(String product) {
        driverTools.swipeByDistanceToElement(getReviewSliderLocator(product),HomePageIOS.SCREEN,Swipe.UP,0.6,50).orElseThrow();
        driverTools.swipeByDistance(HomePageIOS.SCREEN,Swipe.UP,0.05);
    }

    private Locator getReviewSliderLocator(String product) {
        return new Locator(product+"LOCATOR", AppiumBy.xpath("//*[@label='" + product + "']//parent::*//following-sibling::XCUIElementTypeOther//*[@name='thumbImageView']"));
    }

    @Override
    public Boolean verifyReview(String reviewText) {
        HomePage<T> homePage = HomePage.getInstance(session);
        AccountPage<T> accountPage = AccountPage.getInstance(session);
        homePage.openAccount();
        accountPage.openMyReviewPage();
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.swipeToElement(getReviewLocator(reviewText), Swipe.UP, 3);
        boolean isReviewShown = driverTools.isElementDisplayed(getReviewLocator(reviewText));
        driverTools.switchToNative();
        return isReviewShown;
    }
}