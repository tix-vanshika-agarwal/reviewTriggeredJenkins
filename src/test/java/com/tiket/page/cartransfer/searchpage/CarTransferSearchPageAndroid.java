package com.tiket.page.cartransfer.searchpage;

import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.cartransfer.TripDetails;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public class CarTransferSearchPageAndroid  <T extends WebDriver> extends CarTransferSearchPage<T>{

    private static final Locator SEARCH_FIELD_1 = new Locator("SEARCH_FIELD_1", AppiumBy.xpath("//android.widget.TextView[@text=\"Select Pickup Location\" or @text=\"Pilih Bandara atau Stasiun\"]"));
    private static final Locator INPUT_SEARCH_FIELD = new Locator("INPUT_SEARCH_FIELD", AppiumBy.id("tds_search_et"));
    private static final Locator SEARCH_FIELD_2 = new Locator("SEARCH_FIELD_2", AppiumBy.id("//android.widget.TextView[@text=\"Going anywhere?\" or @text=\"Mau ke mana?\"]"));
    private static final Locator CONFIRM_PICKUP_LOCATION = new Locator("CONFIRM_PICKUP_LOCATION", AppiumBy.id("btn_save"));
    private static final Locator TRANSFER_OUT_FLAG = new Locator("TRANSFER_OUT_FLAG", AppiumBy.xpath("//*[contains(@text,'Pick you up anytime') or contains(@text,'Mobil selalu standby')]"));
    private static final Locator PLACE_FULL_ADDRESS = new Locator("PLACE_FULL_ADDRESS", AppiumBy.id("tv_place_address"));
    private static final Locator PLACE_FULL_NAME = new Locator("PLACE_FULL_ADDRESS", AppiumBy.id("tv_place"));
    private static final Locator ALLOW_ACCESS_BUTTON = new Locator("ALLOW_ACCESS_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'tds_button_view') and (@text='Allow access' or @text='Beri izin akses')]"));
    private static final Locator PERMISSION_TYPE_ALLOW_ONCE = new Locator("PERMISSION_TYPE_ALLOW_ONCE", AppiumBy.xpath("//*[@text=\"Only this time\" or @text=\"Hanya kali ini\"]"));
    public static final Locator TURN_ON_LOCATION = new Locator("TURN_ON_LOCATION", AppiumBy.xpath("//*[contains(@resource-id,'button1') and contains(@text,'Turn on')]"));

    public CarTransferSearchPageAndroid(Session<T> session) {
        super(session);
    }

    private Locator getAiportSugestionLocator(String airport){
        return new Locator("AIRPORT_LOCATOR_SUGESTION", AppiumBy.xpath("(//*[contains(@resource-id,'tds_title_list') and (contains(@text,'"+airport+"'))])[1]"));
    }

    private Locator getDestinationSugestionLocator(String destination){
        return new Locator("DESTINATION_LOCATOR_SUGESTION", AppiumBy.xpath("(//*[contains(@resource-id,'tds_title_list') and (contains(@text,'"+destination+"'))])[1]"));
    }

    private boolean isTransferOut(){
        Optional<WebElement> transferOutFlag = driverTools.element(TRANSFER_OUT_FLAG);
        Boolean isOut = transferOutFlag.isPresent();
        logger.log("Is transfer out? " + isOut);
        return isOut;
    }

    private void clickConfirmPickupLocation(){
        WebElement confirmPickupLocation = driverTools.waitForElementToAppear(CONFIRM_PICKUP_LOCATION, 5).orElseThrow();
        driverTools.click(confirmPickupLocation);
    }

    private void openAndSendInputForField1(String location){
        WebElement airportSearchField = driverTools.waitForElementToAppear(SEARCH_FIELD_1,15).orElseThrow();
        driverTools.click(airportSearchField);
        WebElement inputAirportField = driverTools.waitForElementToAppear(INPUT_SEARCH_FIELD,5).orElseThrow();
        driverTools.sendKeys(inputAirportField, location);
    }

    private void inputSearchField1(String location){
        openAndSendInputForField1(location);
        final Locator atSugestion = this.getAiportSugestionLocator(location);
        WebElement atSugestionElement = driverTools.waitForElementToAppear(atSugestion,5).orElseThrow();
        driverTools.click(atSugestionElement);
    }

    private void openAndSendInputForField2(String location){
        WebElement destinationSearchField = driverTools.waitForElementToAppear(SEARCH_FIELD_2,15).orElseThrow();
        driverTools.click(destinationSearchField);
        WebElement inputDestinationField = driverTools.waitForElementToAppear(INPUT_SEARCH_FIELD,15).orElseThrow();
        driverTools.sendKeys(inputDestinationField,location);
    }

    private void inputSearchField2(String location){
        openAndSendInputForField2(location);
        final Locator destinationSugestion = this.getDestinationSugestionLocator(location);
        driverTools.click(destinationSugestion);
    }

    @Override
    public void choosePickup(String airport, String destination){
        boolean selectingTransferOut = this.isTransferOut();
        if(!selectingTransferOut){
            this.inputSearchField1(destination);
            this.clickConfirmPickupLocation();
            this.inputSearchField2(airport);
        }
        else if(selectingTransferOut){
            this.inputSearchField1(airport);
            this.inputSearchField2(destination);
            this.clickConfirmPickupLocation();
        }
    }

    private Locator getLocationAddressSuggestionLocator(String location){
        return new Locator("LOCATIONS_ADDRESS_SUGGESTION", AppiumBy.xpath("(//*[contains(@resource-id,'tds_title_list') and (contains(@text,'"+location+"'))]//following-sibling::*[contains(@resource-id,'tds_subtitle_list')])[1]"));
    }

    private String removeSubStringsBetweenRoundBraces(String data){
        return data.replaceFirst(" \\(.*?\\)", "");
    }

    private String getAddress(String location){
        WebElement addressElement = driverTools.element(getLocationAddressSuggestionLocator(location)).orElseThrow();
        String address = driverTools.getText(addressElement);
        logger.log("Name(Title): " + location);
        logger.log("Address: " + address);
        return address;
    }

    private String[] inputSearchField1AndGetAddress(String location){
        String[] infoLocationNameAndAddress = new String[2];
        openAndSendInputForField1(location);
        final Locator atSuggestion = this.getAiportSugestionLocator(location);
        WebElement atSuggestionElement = driverTools.waitForElementToAppear(atSuggestion,5).orElseThrow();
        infoLocationNameAndAddress[0] = removeSubStringsBetweenRoundBraces(driverTools.getText(atSuggestionElement));
        infoLocationNameAndAddress[1] = getAddress(infoLocationNameAndAddress[0]);
        driverTools.click(atSuggestionElement);
        return infoLocationNameAndAddress;
    }

    private String[] inputSearchField2AndGetAddress(String location){
        String[] infoLocationNameAndAddress = new String[2];
        openAndSendInputForField2(location);
        final Locator destinationSuggestion = this.getDestinationSugestionLocator(location);
        WebElement atSuggestionElement = driverTools.waitForElementToAppear(destinationSuggestion,5).orElseThrow();
        infoLocationNameAndAddress[0] = removeSubStringsBetweenRoundBraces(driverTools.getText(atSuggestionElement));
        infoLocationNameAndAddress[1] = getAddress(infoLocationNameAndAddress[0]);
        driverTools.click(destinationSuggestion);
        return infoLocationNameAndAddress;
    }

    private String[] clickConfirmPickupLocationAndGetAddress(){
        WebElement confirmPickupLocation = driverTools.waitForElementToAppear(CONFIRM_PICKUP_LOCATION, 5).orElseThrow();

        String[] infoLocationNameAndAddress = new String[2];
        WebElement nameElement = driverTools.element(PLACE_FULL_NAME).orElseThrow();
        String name = driverTools.getText(nameElement);
        logger.log("Name(title) of Pickup location " + name);

        WebElement addressElement = driverTools.element(PLACE_FULL_ADDRESS).orElseThrow();
        String address = driverTools.getText(addressElement);
        logger.log("Address of Pickup location " + address);

        infoLocationNameAndAddress[0] = name;
        infoLocationNameAndAddress[1] = address;
        driverTools.click(confirmPickupLocation);
        return infoLocationNameAndAddress;
    }

    @Override
    public TripDetails choosePickupAndGetAddress(String airport, String destination){
        boolean selectingTransferOut = this.isTransferOut();
        TripDetails tripDetails = new TripDetails();
        String[] infoNameAndAddressOrigin,infoNameAndAddressDestination;
        if(!selectingTransferOut){
            this.inputSearchField1(destination);
            infoNameAndAddressOrigin = this.clickConfirmPickupLocationAndGetAddress();
            infoNameAndAddressDestination = this.inputSearchField2AndGetAddress(airport);
        }
        else {
            infoNameAndAddressOrigin = this.inputSearchField1AndGetAddress(airport);
            this.inputSearchField2(destination);
            infoNameAndAddressDestination = this.clickConfirmPickupLocationAndGetAddress();
        }
        tripDetails.origin = infoNameAndAddressOrigin[0];
        tripDetails.originAddress = infoNameAndAddressOrigin[1];
        tripDetails.destination = infoNameAndAddressDestination[0];
        tripDetails.destinationAddress = infoNameAndAddressDestination[1];
        return tripDetails;
    }

    @Override
    public void openViaDeeplink() {
        Deeplink deepLink = new Deeplink("CAR_SEARCH_PAGE",
                "tiket://m.gatotkaca.tiket.com/airport-transfer",
                "tiket://m.preprod.tiket.com/airport-transfer",
                "tiket://m.tiket.com/airport-transfer");
        driverTools.deeplink(deepLink);

    }

    @Override
    public void allowLocationAccess() {
        driverTools.waitForElementToAppear(ALLOW_ACCESS_BUTTON).ifPresent(driverTools::click);
        driverTools.waitForElementToBeClickable(PERMISSION_TYPE_ALLOW_ONCE).ifPresent(driverTools::click);
        driverTools.waitForElementToPresence(TURN_ON_LOCATION).ifPresent(driverTools::click);
    }
}
