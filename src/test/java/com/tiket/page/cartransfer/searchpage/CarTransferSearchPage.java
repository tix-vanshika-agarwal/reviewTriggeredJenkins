package com.tiket.page.cartransfer.searchpage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.cartransfer.TripDetails;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.nft.airport_transfer.landing_page.AirportTransferLandingPageWeb;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.core.Is.is;

public abstract class CarTransferSearchPage <T extends WebDriver> extends BasePage<T> {

    public CarTransferSearchPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CarTransferSearchPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new CarTransferSearchPageAndroid <>(session);}
            case IOS -> {return new CarTransferSearchPageIOS <>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }


    public enum TransferType {
        transferOut,transferIn;

        public static TransferType parse(String carTransferType){
            switch(carTransferType){
            case "transfer_in" -> {return transferIn;}
            case "transfer_out" -> {return transferOut;}
            default -> throw new NotImplementedException(String.format("Given transfer type : %s not exist", carTransferType));
            }
        }
    }

    private static final Locator CLOSE_POPUP_BUTTON = new Locator("CLOSE_POPUP_BUTTON", AppiumBy.xpath("//button[contains(@class,'Coachmark_coachmark_skip_bt') or contains(@class,'Coachmark_coachmark_done_btn')]"));
    private static final Locator CLOSE_POPUP_BUTTON_BRAZE = new Locator("CLOSE_POPUP_BUTTON_BRAZE", AppiumBy.xpath("//*[contains(@class,'close_button') and not(contains(@class,'floating'))]"));
    private static final Locator CLEAR_SEARCH = new Locator("CLEAR_SEARCH", AppiumBy.xpath("//button[text()='Clear' or text()='Hapus']"));
    private static final Locator TIME_SEARCH_PAGE = new Locator("TIME_SEARCH_PAGE", AppiumBy.xpath("(//span[contains(@class,'DateTimeInputRevamp_text') and contains(@class,'Text_weight_bold')])[2]"));
    public static final Locator CHOOSE_AIRPORT = new Locator("CHOOSE_AIRPORT",AppiumBy.xpath("//span[contains(@class, 'LocationInputRevamp_text') and (text()='Select airport' or text()='Pilih bandara')]"));
    private static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", AppiumBy.xpath("//button[contains(@class, 'BottomSheet_close_button')]"));
    private static final Locator SEARCH_SUGGESTION = new Locator("SEARCH_SUGGESTION", AppiumBy.xpath("//div[contains(@class, 'List_list_content')]//p[contains(@class, 'List_title')]"));
    public static final Locator SEARCH_BAR_FOR_AIRPORT = new Locator("SEARCH_BAR_FOR_AIRPORT",AppiumBy.xpath("//input[@placeholder='Find airport' or @placeholder='Cari bandara']"));
    private static final Locator SEARCH_SUGGESTION_REGION = new Locator("SEARCH_SUGGESTION_REGION", AppiumBy.xpath("//p[contains(@class, 'List_description')]"));

    private void switchToWebviewAirportTransfer() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("airport-transfer");
        driverTools.waitForWebToLoad();
    }

    public void closeInformationPopUp() {
        switchToWebviewAirportTransfer();
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        airportTransferLandingPageWeb.clickSkipButton();
        driverTools.switchToNative();
    }

    /**
     * Closes popups on the Car Transfer Search Page.
     * <p>
     * This method will attempt to close up to 3 popups, but will not try to click the same popup more than once.
     * If a popup is clicked, it will not be retried in subsequent iterations.
     * Handles both standard and Braze popups.
     */
    public void closePopups() {
        boolean popupClosed = false;
        boolean brazeClosed = false;
        int attempts = 0;
        while (attempts < 3 && (!popupClosed || !brazeClosed)) {
            if (!popupClosed) {
                if (driverTools.waitForElementToPresence(CLOSE_POPUP_BUTTON).isPresent()) {
                    driverTools.click(CLOSE_POPUP_BUTTON);
                    popupClosed = true;
                }
            }
            attempts++;
        }
    }

    public abstract void choosePickup(String airport, String destination);

    public void choosePickups(String transferType, String airport, String destination){
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        airportTransferLandingPageWeb.fillTransferFields(transferType, airport, destination);
    }

    public abstract TripDetails choosePickupAndGetAddress(String airport, String destination);

    public void chooseTransferType(String transferType){
       AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
       airportTransferLandingPageWeb.selectTransferType(transferType);
    }

    public void searchCar(){
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        airportTransferLandingPageWeb.seeCarListOnSRP();
    }

    public void toggleForLaterOrder(){
        logger.log("select date 2 days from current date");
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb =AirportTransferLandingPageWeb.getInstance(session);
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, CoreFunctions.generateRandomInt(1,3));
        airportTransferLandingPageWeb.selectDate(departureDate);
    }

    public abstract void openViaDeeplink();
    public abstract void allowLocationAccess();

    public void clearRecentSearch(){
        if (driverTools.isElementDisplayed(CLEAR_SEARCH))
            driverTools.clickWithJavascriptExecutor(CLEAR_SEARCH);
    }

    /** Recent search lives inside the destination picker: opens it and checks the newest entry. */
    public void verifyRecentSearch(String transferType, String destination){
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        Assertion.assertThat("latest recent search matches the last searched destination",
                airportTransferLandingPageWeb.getLatestRecentSearch(transferType), containsStringIgnoringCase(destination));
    }

    public void clickRecentSearch(){
        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb = AirportTransferLandingPageWeb.getInstance(session);
        airportTransferLandingPageWeb.selectLatestRecentSearch();
    }

    public String getTimeSearchPage(){
        WebElement timeElement = driverTools.element(TIME_SEARCH_PAGE).orElseThrow();
        return driverTools.getText(timeElement);
    }

    public String checkDestinationMatch(String destination) {
        WebElement ele = driverTools.element(getDestinationLocator(destination)).orElseThrow();
        String text = driverTools.getText(ele);
        logger.log("Destination text: " + text);
        return text;
    }

    private Locator getDestinationLocator(String destinationName) {
        String xpath = String.format(
                "//span[contains(@class, 'LocationInputRevamp_airport_name') and contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),%s)]",
                CoreFunctions.toXPathLiteral(destinationName.toLowerCase()));
        return new Locator("DESTINATION_LOCATION", AppiumBy.xpath(xpath));
    }

    public void clickAirportSearch(){
        driverTools.switchToWebViewUrl("airport-transfer");
        driverTools.clickWithJavascriptExecutor(CHOOSE_AIRPORT);
        driverTools.waitForElementToAppear(CLOSE_BUTTON);
        driverTools.switchToNative();
    }

    public boolean verifyNearestLocations(){
        driverTools.switchToWebViewUrl("airport-transfer");
        driverTools.waitForElementToAppear(SEARCH_SUGGESTION).orElseThrow();
        List<WebElement> popularLocations = driverTools.elements(SEARCH_SUGGESTION).orElseThrow();
        logger.log("Recommended Locations:");
        for( WebElement location : popularLocations){
            logger.log(driverTools.getText(location));
        }
        driverTools.switchToNative();
        return !popularLocations.isEmpty();
    }

    public boolean checkAirport(String itaCode, String airportName, String region){
        driverTools.switchToWebViewUrl("airport-transfer");
        String isAirport1Correct = verifySuggestions(itaCode);
        Assertion.assertThat("Ita code is correct", isAirport1Correct, containsString(itaCode));
        logger.log("is itaCode correct: " + isAirport1Correct);

        String isAirport2Correct = verifySuggestions(airportName);
        Assertion.assertThat("Airport name is correct", isAirport2Correct, containsString(airportName));
        logger.log("is airportName correct: " + isAirport2Correct);

        String isAirportWithRegion3Correct = verifySuggestionsRegion(region);
        Assertion.assertThat("Region is correct", isAirportWithRegion3Correct, containsString(region));
        logger.log("is region correct: " + isAirportWithRegion3Correct);
        driverTools.switchToNative();
        return true;
    }

    private String verifySuggestions(String location){
        driverTools.clear(SEARCH_BAR_FOR_AIRPORT);
        driverTools.sendKeys(SEARCH_BAR_FOR_AIRPORT,location);
        driverTools.waitForElementToAppear(SEARCH_SUGGESTION).orElseThrow();
        List<WebElement> suggestedLocations = driverTools.elements(SEARCH_SUGGESTION).orElseThrow();
        logger.log("Recommended Locations for "+location+": ");

        for( WebElement suggestion : suggestedLocations){
            String suggestionText = driverTools.getText(suggestion);
            logger.log("Suggestion by "+location+" search type: " + suggestionText);
            if(suggestionText.contains(location)){
                return suggestionText;
            }
        }
        return "not applicable";
    }

    private String verifySuggestionsRegion(String location){
        driverTools.clear(SEARCH_BAR_FOR_AIRPORT);
        driverTools.sendKeys(SEARCH_BAR_FOR_AIRPORT,location);
        driverTools.waitForElementToAppear(SEARCH_SUGGESTION_REGION).orElseThrow();
        List<WebElement> suggestedLocations = driverTools.elements(SEARCH_SUGGESTION_REGION).orElseThrow();
        logger.log("Recommended Locations for "+location+": ");

        for( WebElement suggestion : suggestedLocations){
            String suggestionText = driverTools.getText(suggestion);
            logger.log("Suggestion by "+location+" search type: " + suggestionText);
            if(suggestionText.contains(location)){
                return suggestionText;
            }
        }
        return "not applicable";
    }

    public void selectDate(int daysToDepartureDate){
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);

        AirportTransferLandingPageWeb<?> airportTransferLandingPageWeb =AirportTransferLandingPageWeb.getInstance(session);
        airportTransferLandingPageWeb.selectDate(departureDate);
    }
}
