package com.tiket.page.flight.pdp;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FlightDescriptionPageAndroid <T extends WebDriver> extends FlightDescriptionPage<T> {
    public FlightDescriptionPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator STOP_OVER_PDP = new Locator("STOP_OVER_PDP", AppiumBy.id("tv_step_over"));
    public static final Locator AIRLINE_LOGO_PDP = new Locator("AIRLINE_LOGO_PDP", AppiumBy.id("iv_airline_logo"));
    public static final Locator ROUTE_ORIGIN_DESTINATION = new Locator("ROUTE_ORIGIN_DESTINATION", AppiumBy.id("tv_flight_detail_page_title"));
    public static final Locator YELLOW_BANNER = new Locator("YELLOW_BANNER", AppiumBy.id("tv_message"));

    public static final Locator REFUND_100P_TAG = new Locator("REFUND_100P_TAG", AppiumBy.id("tv_refund_status"));

    @Override
    public boolean verifyStopOver(){
        WebElement stopOverElement = driverTools.waitForElementToAppear(STOP_OVER_PDP).orElseThrow();
        String stopOverText = driverTools.getText(stopOverElement);
        logger.log("stop over text: " + stopOverText);
        return stopOverText.contains("Stopover at") || stopOverText.contains("Perhentian di");
    }

    @Override
    public boolean isAirlineLogoDisplayedPDP(){
        return driverTools.isElementDisplayed(AIRLINE_LOGO_PDP);
    }

    @Override
    public boolean isAirlineRouteCorrectPDP(String route){
        logger.log("expected route : " + route);
        WebElement routePDPElement = driverTools.waitForElementToAppear(ROUTE_ORIGIN_DESTINATION).orElseThrow();
        String routePDP = driverTools.getText(routePDPElement);
        logger.log("actual route on pdp : " + routePDP);
        return route.toLowerCase().contains(routePDP.toLowerCase());
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
