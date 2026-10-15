package com.tiket.page.flight.generaltravelinformationpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class FlightGeneralTravelInformationPage <T extends WebDriver> extends BasePage<T> {
    public FlightGeneralTravelInformationPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightGeneralTravelInformationPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new FlightGeneralTravelInformationPageAndroid<>(session);}
            case IOS -> {return new FlightGeneralTravelInformationPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public static final Locator BANNER_TRAVEL_INFORMATION_PAGE = new Locator("BANNER_TRAVEL_INFORMATION_PAGE", AppiumBy.xpath("//*[contains(@class,'Banner_banner_background')]"));
    public static final Locator TITLE_TRAVEL_INFORMATION_PAGE = new Locator("TITLE_TRAVEL_INFORMATION_PAGE",AppiumBy.xpath("//*[contains(@class,'title_text') and (contains(text(),'Penerbangan ke Indonesia') or contains(text(),'Flights to Indonesia'))]"));
    public static final Locator CONTENT_BOX_TRAVEL_INFORMATION_PAGE = new Locator("CONTENT_BOX_TRAVEL_INFORMATION_PAGE",AppiumBy.xpath("//*[contains(@data-testid,'content')]"));


    public boolean verifyGeneralTravelInformationPage(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();

        String getUrl = driverTools.getCurrentURL();
        boolean isURLContainsInfoAsText = getUrl.contains("info");
        logger.log("is url contains 'info' as text : " + isURLContainsInfoAsText);
        driverTools.waitForElementToAppear(BANNER_TRAVEL_INFORMATION_PAGE);

        boolean isBannerDisplayed = driverTools.isElementDisplayed(BANNER_TRAVEL_INFORMATION_PAGE);
        logger.log("is banner displayed on travel information page : " + isBannerDisplayed);

        boolean isTitleDisplayed = driverTools.isElementDisplayed(TITLE_TRAVEL_INFORMATION_PAGE);
        logger.log("is title displayed on travel information page : " + isTitleDisplayed);

        boolean isContentBoxDisplayed = driverTools.isElementDisplayed(CONTENT_BOX_TRAVEL_INFORMATION_PAGE);
        logger.log("is content box displayed on travel information page : " + isContentBoxDisplayed);

        driverTools.switchToNative();
        return isURLContainsInfoAsText && isBannerDisplayed && isTitleDisplayed
                && isContentBoxDisplayed ;
    }

    public abstract void clickBackBtn();
}
