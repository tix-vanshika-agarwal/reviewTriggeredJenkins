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

import static org.hamcrest.Matchers.*;

public class TrainSearchResultPageIOS <T extends WebDriver> extends TrainSearchResultPage<T> {
    public TrainSearchResultPageIOS(Session<T> session) {
        super(session);
    }

    private static final Locator WHOOSH_OR_TRAIN_1_CARD = new Locator("WHOOSH_OR_TRAIN_1_CARD", AppiumBy.name("train_name_1"));
    private static final Locator BACK_BUTTON_SRP = new Locator("BACK_BUTTON_SRP", AppiumBy.accessibilityId("back"));
    private static final Locator ROUTE_INFO = new Locator("ROUTE_INFO", AppiumBy.accessibilityId("routeLabel"));
    private static final Locator PAX_INFO = new Locator("PAX_INFO", AppiumBy.accessibilityId("passengerLabel"));
    public static final Locator NO_TRAIN_AVAILABLE  = new Locator("NO_TRAIN_AVAILABLE",AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label, 'Kereta belum tersedia di tanggal ini') or contains(@label, 'This route is not yet available') or contains(@label,'No trains is available') or contains(@label,'Kereta belum tersedia di tanggal ini')]"));
    public static final Locator CROSS_SELL_BTN  = new Locator("CROSS_SELL_BTN",AppiumBy.xpath("(//*[contains(@label, 'Cari bus dan travel') or contains(@label, 'Search bus and shuttle')])[last()]"));
    private static final Locator WHOOSH_OR_TRAIN_1_CARD_PRICE = new Locator("WHOOSH_OR_TRAIN_1_CARD_PRICE", AppiumBy.xpath("(//*[contains(@name,'mainPrice')])[1]"));
    private static final Locator WHOOSH_OR_TRAIN_1_CARD_SLASHED_PRICE = new Locator("WHOOSH_OR_TRAIN_1_CARD_SLASHED_PRICE", AppiumBy.xpath("(//*[contains(@name,'mainPrice')])[1]/preceding-sibling::XCUIElementTypeStaticText[contains(@name,'slashPrice')]"));

    @Override
    public boolean verifySearch(TrainAndWhooshGeneralData trainAndWhooshGeneralData){
        WebElement routeInfoElement = driverTools.waitForElementToAppear(ROUTE_INFO).orElseThrow();
        String routeInfoText = driverTools.getText(routeInfoElement);
        logger.log("Route info text: " + routeInfoText);
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
        Locator dateLocator = getDateLocator(departDateEnExpected, departDateIdExpected);
        WebElement dateElement = driverTools.waitForElementToAppear(dateLocator).orElseThrow();
        String actualDate = driverTools.getText(dateElement);
        boolean isDateCorrect = actualDate.contains(departDateEnExpected) || actualDate.contains(departDateIdExpected);
        logger.log("is date correct: " + isDateCorrect + " (Expected: " + departDateEnExpected + " or " + departDateIdExpected + ", Actual: " + actualDate + ")");
        Assertion.assertThat("Date should match", actualDate, anyOf(containsString(departDateEnExpected), containsString(departDateIdExpected)));

        //check pax info
        WebElement paxInfoElement = driverTools.waitForElementToAppear(PAX_INFO).orElseThrow();
        String paxInfoText = driverTools.getText(paxInfoElement);
        int totalPaxInApp = getNumberOfPassengers(paxInfoText);
        boolean isPaxInfoCorrect = totalPaxInApp == trainAndWhooshGeneralData.numberOfPassenger;
        logger.log("is pax info correct: " + isPaxInfoCorrect + " (Expected: " + trainAndWhooshGeneralData.numberOfPassenger + ", Actual: " + totalPaxInApp + ")");
        Assertion.assertThat("Total number of passengers should match", totalPaxInApp, is(trainAndWhooshGeneralData.numberOfPassenger));

        boolean isWhooshVisible = driverTools.waitForElementToAppear(WHOOSH_OR_TRAIN_1_CARD).isPresent();
        logger.log("Is whoosh visible: " + isWhooshVisible);
        return isWhooshVisible && isRouteInfoCorrect && isDateCorrect && isPaxInfoCorrect;
    }

    private int getNumberOfPassengers(String paxInfoText){
        String[] paxParts = paxInfoText.split(" ");
        return Integer.parseInt(paxParts[0].trim());
    }

    private Locator getDateLocator(String dateEn,String dateId){
        return new Locator("DATE_SELECTED", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,',') and (contains(@label,'" + dateEn + "') or contains(@label,'" + dateId + "'))]"));
    }

    @Override
    public void backToSearchPage(){
        driverTools.click(BACK_BUTTON_SRP);
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
        driverTools.waitForElementToAppear(WHOOSH_OR_TRAIN_1_CARD,10);
        driverTools.click(WHOOSH_OR_TRAIN_1_CARD);
    }

    @Override
    public TrainAndWhooshPrices selectTrainAndGetPricePerPax(){
        WebElement pricePerPaxElement = driverTools.waitForElementToAppear(WHOOSH_OR_TRAIN_1_CARD_PRICE,10).orElseThrow();
        Optional<WebElement> slashedPriceElement = driverTools.waitForElementToAppear(WHOOSH_OR_TRAIN_1_CARD_SLASHED_PRICE, 2);
        String pricePerPaxText = driverTools.getText(pricePerPaxElement);
        Optional<String> slashedPriceText = slashedPriceElement.map(driverTools::getText);
        logger.log("Price per pax text: " + pricePerPaxText);
        driverTools.click(WHOOSH_OR_TRAIN_1_CARD_PRICE);
        return new TrainAndWhooshPrices(pricePerPaxText, slashedPriceText);
    }
}
