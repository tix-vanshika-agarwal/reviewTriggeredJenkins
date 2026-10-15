package com.tiket.page.train.searchresultpage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;

public class TrainSearchResultPageAndroid <T extends WebDriver> extends TrainSearchResultPage<T> {
    public TrainSearchResultPageAndroid(Session<T> session) {
        super(session);
    }

    public static final Locator WHOOSH_OR_TRAIN_1_CARD = new Locator("WHOOSH_OR_TRAIN_1_CARD", AppiumBy.id("tv_train_name"));
    private static final Locator ROUTE_INFO = new Locator("ROUTE_INFO", AppiumBy.id("tds_header_navigation_appbar"));
    public static final Locator SEARCH_RESULT_DATE_AND_PAX_INFO = new Locator("SEARCH_RESULT_DATE_AND_PAX_INFO",AppiumBy.id("tds_sub_header_navigation_appbar"));
    public static final Locator BACK = new Locator("BACK",AppiumBy.id("tds_icon_navigation_toolbar"));
    private static final Locator NO_TRAIN_AVAILABLE = new Locator("NO_TRAIN_AVAILABLE", AppiumBy.id("tds_txt_header_info_view"));
    private static final Locator CROSS_SELL_BTN = new Locator("CROSS_SELL_BTN", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and contains(@text,'Search bus and shuttle') or  contains(@text,'Cari bus dan travel')]"));
    public static final Locator BOOKING_FORM_PAGE = new Locator( "BOOKING_FORM_PAGE", AppiumBy.id("ps_booking"));
    private static final Locator WHOOSH_OR_TRAIN_1_CARD_PRICE = new Locator("WHOOSH_OR_TRAIN_1_CARD_PRICE", AppiumBy.xpath("(//*[contains(@resource-id,'tv_main_price')])[1]"));
    private static final Locator WHOOSH_OR_TRAIN_1_CARD_SLASHED_PRICE = new Locator("WHOOSH_OR_TRAIN_1_CARD_SLASHED_PRICE", AppiumBy.xpath("(//*[contains(@resource-id,'tv_main_price')])[1]//preceding-sibling::android.widget.TextView[contains(@resource-id,'tv_slash_price')]"));

    @Override
    public boolean verifySearch(TrainAndWhooshGeneralData trainAndWhooshGeneralData){
        WebElement routeInfoElement = driverTools.waitForElementToAppear(ROUTE_INFO).orElseThrow();
        String routeInfoText = driverTools.getText(routeInfoElement);
        logger.log("Route info text: " + routeInfoText);
        // Use helper method to extract origin and destination
        String[] routeParts = CoreFunctions.getOriginAndDestinationFromRouteInfo(routeInfoText);
        String originInApp = routeParts[0];
        String destinationInApp = routeParts[1];
        boolean isRouteInfoCorrect = originInApp.contains(trainAndWhooshGeneralData.originStation) && destinationInApp.contains(trainAndWhooshGeneralData.destinationStation);
        logger.log("is route info correct: " + isRouteInfoCorrect + " (Expected: " + trainAndWhooshGeneralData.originStation + " to " + trainAndWhooshGeneralData.destinationStation + ", Actual: " + originInApp + " to " + destinationInApp + ")");
        Assertion.assertThat("Origin station should match", originInApp,containsString(trainAndWhooshGeneralData.originStation));
        Assertion.assertThat("Destination station should match", destinationInApp,containsString(trainAndWhooshGeneralData.destinationStation));

        String departDateEnExpected = trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_En;
        String departDateIdExpected = trainAndWhooshGeneralData.whooshDateFormatted_dd_MMM_Id;
        logger.log("Departure date (EN): " + departDateEnExpected);
        logger.log("Departure date (ID): " + departDateIdExpected);
        WebElement dateAndPaxInfoElement = driverTools.waitForElementToAppear(SEARCH_RESULT_DATE_AND_PAX_INFO).orElseThrow();
        String actualDateAndPaxInfo = driverTools.getText(dateAndPaxInfoElement);
        boolean isDateCorrect = actualDateAndPaxInfo.contains(departDateEnExpected) || actualDateAndPaxInfo.contains(departDateIdExpected);
        logger.log("is date correct: " + isDateCorrect + " (Expected: " + departDateEnExpected + " or " + departDateIdExpected + ", Actual: " + actualDateAndPaxInfo + ")");
        Assertion.assertThat("Date should match", actualDateAndPaxInfo, anyOf(containsString(departDateEnExpected), containsString(departDateIdExpected)));

        //check pax info
        int totalPaxInApp = getNumberOfPassengers(actualDateAndPaxInfo);
        boolean isPaxInfoCorrect = totalPaxInApp == trainAndWhooshGeneralData.numberOfPassenger;
        logger.log("is pax info correct: " + isPaxInfoCorrect + " (Expected: " + trainAndWhooshGeneralData.numberOfPassenger + ", Actual: " + totalPaxInApp + ")");
        Assertion.assertThat("Total number of passengers should match", totalPaxInApp, is(trainAndWhooshGeneralData.numberOfPassenger));

        boolean isWhooshVisible = driverTools.waitForElementToAppear(WHOOSH_OR_TRAIN_1_CARD).isPresent();
        logger.log("Is whoosh visible: " + isWhooshVisible);
        return isWhooshVisible && isRouteInfoCorrect && isDateCorrect && isPaxInfoCorrect;
    }

    private int getNumberOfPassengers(String paxInfoText) {
        // Match the number immediately before a word (Passenger, Penumpang, etc.)
        Matcher matcher = Pattern.compile("(\\d+)\\s*\\p{L}+").matcher(paxInfoText);
        int passengerCount = -1;
        // Iterate through all matches and take the **last one** (ignores date numbers)
        while (matcher.find()) {
            passengerCount = Integer.parseInt(matcher.group(1));
        }
        if (passengerCount != -1) {
            return passengerCount;
        }
        throw new IllegalArgumentException("No passenger count found in text: " + paxInfoText);
    }

    @Override
    public void backToSearchPage(){
        driverTools.click(BACK);
    }

    @Override
    public boolean verifySRPHaveNoResult() {
        return driverTools.isElementDisplayed(NO_TRAIN_AVAILABLE);
    }

    @Override
    public void clickCrossSell() {
        driverTools.click(CROSS_SELL_BTN);
        driverTools.waitForElementToDisappear(CROSS_SELL_BTN);
    }

    @Override
    public void selectTrain(){
        driverTools.swipeDown();
        driverTools.waitForElementToAppear(WHOOSH_OR_TRAIN_1_CARD);
        driverTools.click(WHOOSH_OR_TRAIN_1_CARD);
        driverTools.waitForElementToAppear(BOOKING_FORM_PAGE);
    }

    @Override
    public TrainAndWhooshPrices selectTrainAndGetPricePerPax(){
        driverTools.swipeDown();
        WebElement pricePerPaxElement = driverTools.waitForElementToAppear(WHOOSH_OR_TRAIN_1_CARD_PRICE,10).orElseThrow();
        Optional<WebElement> slashedPriceElement = driverTools.waitForElementToAppear(WHOOSH_OR_TRAIN_1_CARD_SLASHED_PRICE, 2);
        String pricePerPaxText = driverTools.getText(pricePerPaxElement);
        Optional<String> slashedPriceText = slashedPriceElement.map(driverTools::getText);
        logger.log("Price per pax text: " + pricePerPaxText);
        driverTools.click(WHOOSH_OR_TRAIN_1_CARD_PRICE);
        return new TrainAndWhooshPrices(pricePerPaxText, slashedPriceText);
    }
}
