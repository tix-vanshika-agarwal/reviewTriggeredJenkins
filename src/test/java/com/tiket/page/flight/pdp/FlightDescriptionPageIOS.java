package com.tiket.page.flight.pdp;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FlightDescriptionPageIOS <T extends WebDriver> extends FlightDescriptionPage<T>{
    public FlightDescriptionPageIOS(Session<T> session) {
        super(session);
    }

    public static final Locator YELLOW_BANNER = new Locator("YELLOW_BANNER", AppiumBy.accessibilityId("flight_note"));
    public static final Locator REFUND_100P_TAG = new Locator("REFUND_100P_TAG", AppiumBy.xpath("//*[contains(@label,'100% Refund option') or contains(@label,'Bisa 100% refund')]"));

    @Override
    public boolean verifyStopOver(){
        //fixme implementation pending -> unique element not present
        return true;
    }

    @Override
    public boolean isAirlineLogoDisplayedPDP(){
        //fixme implementation pending -> unique element not present
        return true;
    }

    private Locator getRouteLocatorViaText(String route){
        return new Locator("ROUTE_PDP", AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@name,'"+route+"')])[2]"));
    }

    @Override
    public boolean isAirlineRouteCorrectPDP(String route){
        return driverTools.isElementDisplayed(getRouteLocatorViaText(route));
    }

    @Override
    public String checkYellowBannerPDP(){
        WebElement yellowBannerPDP = driverTools.waitForElementToAppear(YELLOW_BANNER).orElseThrow();
        return driverTools.getText(yellowBannerPDP);
    }

    @Override
    public void clickYellowBannerPDP(){
        driverTools.click(YELLOW_BANNER);
    }

    @Override
    public boolean is100pRefundTagDisplayed(){
        return driverTools.isElementDisplayed(REFUND_100P_TAG);
    }
}
