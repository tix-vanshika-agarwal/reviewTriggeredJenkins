package com.tiket.page.cartransfer.searchpage;

import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.cartransfer.TripDetails;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CarTransferSearchPageIOS  <T extends WebDriver> extends CarTransferSearchPage<T>{

    public static final Locator CHOOSE_AIRPORT = new Locator("CHOOSE_AIRPORT",AppiumBy.accessibilityId("choose_airport"));
    public static final Locator CHOOSE_DEST = new Locator("CHOOSE_DEST",AppiumBy.accessibilityId("choose_destination"));
    private static final Locator CLOSE_BUTTON = new Locator("CLOSE_BUTTON", AppiumBy.accessibilityId("ic cancel grey"));
    public static final Locator CONFIRM_LOCATION_MARK_ON_MAP = new Locator("CONFIRM_LOCATION_MARK_ON_MAP",AppiumBy.accessibilityId("save_button"));
    public static final Locator SEARCH_BAR_FOR_AIRPORT = new Locator("SEARCH_BAR_FOR_AIRPORT",AppiumBy.accessibilityId("airport_searchfield"));
    public static final Locator SEARCH_BAR_FOR_DESTINATION = new Locator("SEARCH_BAR_FOR_DESTINATION",AppiumBy.accessibilityId("location_searchfield"));
    public static final Locator BTN_SWITCH_IN_TRANSFER_OUT_STATE = new Locator("BTN_SWITCH_IN_TRANSFER_OUT_STATE", AppiumBy.name("transfer_type_switch (out)"));
    private static final Locator SEARCH_SUGGESTION = new Locator("SEARCH_SUGGESTION", AppiumBy.accessibilityId("search_suggestion"));
    private static final Locator PLACE_FULL_ADDRESS = new Locator("PLACE_FULL_ADDRESS", AppiumBy.accessibilityId("location_address"));
    private static final Locator PLACE_FULL_NAME = new Locator("PLACE_FULL_NAME", AppiumBy.accessibilityId("place_name_confirm"));
    private static final Locator SEARCH_SUGGESTION_ADDRESS_DETAIL = new Locator("SEARCH_SUGGESTION_ADDRESS_DETAIL", AppiumBy.accessibilityId("address_search"));
    public static final Locator ALLOW_ONCE_PERMISSION_BUTTON = new Locator("ALLOW_ONCE_PERMISSION_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='Allow Once']"));
    public static final Locator ALLOW = new Locator("ALLOW", AppiumBy.xpath("//XCUIElementTypeButton[@name='Allow' or @name='buttonPrimary']"));
    public static final Locator ALLOW_WHILE_USING_APP = new Locator("ALLOW_WHILE_USING_APP", AppiumBy.xpath("//XCUIElementTypeButton[@name='Allow While Using App']"));
    public static final Locator ALLOW_ACCESS = new Locator("ALLOW_ACCESS", AppiumBy.xpath("//*[@name='Allow']"));
    public static final Locator BACK_TO_HOMEPAGE = new Locator("BACK_TO_HOMEPAGE", AppiumBy.xpath("//*[@name='back to homepage' or @name='kembali ke beranda' or @aria-label='back to homepage' or @aria-label='kembali ke beranda']"));

    public CarTransferSearchPageIOS(Session<T> session) {
        super(session);
    }

    private String[] clickConfirmPickupLocationAndGetAddress(){
        WebElement confirmPickupLocation = driverTools.waitForElementToAppear(CONFIRM_LOCATION_MARK_ON_MAP, 5).orElseThrow();

        String[] infoLocationNameAndAddress = new String[2];
        WebElement nameElement = driverTools.element(PLACE_FULL_NAME).orElseThrow();
        String name = driverTools.getText(nameElement);
        logger.log("Name(Title) of Pickup location " + name);

        WebElement addressElement = driverTools.element(PLACE_FULL_ADDRESS).orElseThrow();
        String address = driverTools.getText(addressElement);
        logger.log("Address of Pickup location " + address);

        infoLocationNameAndAddress[0] = name;
        infoLocationNameAndAddress[1] = address;
        driverTools.click(confirmPickupLocation);
        return infoLocationNameAndAddress;
    }

    private void clickConfirmPickupLocation(){
        WebElement confirmPickupLocation = driverTools.waitForElementToAppear(CONFIRM_LOCATION_MARK_ON_MAP, 5).orElseThrow();
        driverTools.click(confirmPickupLocation);
    }

    private void openAndSendInputForField(String location,Locator searchLocatorSearchPage,Locator inputLocator){
        WebElement searchFieldSearchPage = driverTools.waitForElementToAppear(searchLocatorSearchPage,15).orElseThrow();
        driverTools.click(searchFieldSearchPage);
        driverTools.waitForElementToAppear(CLOSE_BUTTON);
        WebElement inputField = driverTools.waitForElementToAppear(inputLocator,15).orElseThrow();
        driverTools.sendKeys(inputField,location);
    }


    private String getAddress(String location){
        WebElement element = driverTools.firstElement(SEARCH_SUGGESTION_ADDRESS_DETAIL).orElseThrow();
        String address = driverTools.getText(element);
        logger.log("Name(Title): " + location);
        logger.log("Address: " + address);
        return address;
    }

    private String removeSubStringsBetweenRoundBraces(String data){
        int l=data.lastIndexOf('(');
        if(l!=-1){
            StringBuilder newData= new StringBuilder();
            for(int i=0;i<l-1;i++){
                newData.append(data.charAt(i));
            }
            data = newData.toString();
        }
        return data;
    }

    private String[] inputSearchFieldAndGetAddress(String location,Locator searchLocator,Locator inputLocator){
        String[] infoLocationNameAndAddress = new String[2];
        openAndSendInputForField(location,searchLocator,inputLocator);
        driverTools.waitForElementToAppear(SEARCH_SUGGESTION).orElseThrow();
        List<WebElement> atSuggestionElement = driverTools.elements(SEARCH_SUGGESTION).orElseThrow();
        infoLocationNameAndAddress[0] = removeSubStringsBetweenRoundBraces(driverTools.getText(atSuggestionElement.get(0)));
        infoLocationNameAndAddress[1] = getAddress(infoLocationNameAndAddress[0]);
        driverTools.click(atSuggestionElement.get(0));
        return infoLocationNameAndAddress;
    }

    private void inputSearchField(String location,Locator searchLocator,Locator inputLocator){
        openAndSendInputForField(location,searchLocator,inputLocator);
        driverTools.waitForElementToAppear(SEARCH_SUGGESTION).orElseThrow();
        List<WebElement> atSuggestionElement = driverTools.elements(SEARCH_SUGGESTION).orElseThrow();
        driverTools.click(atSuggestionElement.get(0));
    }

    @Override
    public void choosePickup(String airport, String destination){
        boolean selectingTransferOut = this.isTransferOut();
        if(!selectingTransferOut){
            inputSearchField(destination,CHOOSE_DEST,SEARCH_BAR_FOR_DESTINATION);
            clickConfirmPickupLocation();
            inputSearchField(airport,CHOOSE_AIRPORT,SEARCH_BAR_FOR_AIRPORT);
        }
        else {
            inputSearchField(airport,CHOOSE_AIRPORT,SEARCH_BAR_FOR_AIRPORT);
            inputSearchField(destination,CHOOSE_DEST,SEARCH_BAR_FOR_DESTINATION);
            clickConfirmPickupLocation();
        }
    }

    @Override
    public TripDetails choosePickupAndGetAddress(String airport, String destination){
        boolean isTransferOut = this.isTransferOut();
        TripDetails tripDetails = new TripDetails();
        String[] infoNameAndAddressOrigin,infoNameAndAddressDestination;
        if(isTransferOut){
            //transfer out input airport first and then input drop off location
            infoNameAndAddressOrigin = inputSearchFieldAndGetAddress(airport,CHOOSE_AIRPORT,SEARCH_BAR_FOR_AIRPORT);
            inputSearchField(destination,CHOOSE_DEST,SEARCH_BAR_FOR_DESTINATION);
            infoNameAndAddressDestination = clickConfirmPickupLocationAndGetAddress();
        }
        else {
            //transfer in input pick up location first and then input drop off location
            inputSearchField(destination,CHOOSE_DEST,SEARCH_BAR_FOR_DESTINATION);
            infoNameAndAddressOrigin = clickConfirmPickupLocationAndGetAddress();
            infoNameAndAddressDestination = inputSearchFieldAndGetAddress(airport,CHOOSE_AIRPORT,SEARCH_BAR_FOR_AIRPORT);
        }
        tripDetails.origin = infoNameAndAddressOrigin[0];
        tripDetails.originAddress = infoNameAndAddressOrigin[1];
        tripDetails.destination = infoNameAndAddressDestination[0];
        tripDetails.destinationAddress = infoNameAndAddressDestination[1];
        return tripDetails;
    }

    private boolean isTransferOut(){
        driverTools.waitForElementToAppear(BTN_SWITCH_IN_TRANSFER_OUT_STATE);
        Optional<WebElement> transferOutFlag = driverTools.element(BTN_SWITCH_IN_TRANSFER_OUT_STATE);
        Boolean isOut = transferOutFlag.isPresent();
        logger.log("Is transfer out? " + isOut);
        return isOut;
    }

    @Override
    public void openViaDeeplink() {
     Deeplink deepLink = new Deeplink("CAR_SEARCH_PAGE",
             "tiket://m.gatotkaca.tiket.com/airport-transfer",
             "tiket://m.preprod.tiket.com/airport-transfer",
             "tiket://m.tiket.com/airport-transfer");
        driverTools.deeplink(deepLink);
        driverTools.clickElementByCoordinate(ALLOW_ACCESS);
    }

    @Override
    public void allowLocationAccess() {
        Map<String, Object> caps = driverTools.getCapabilities();
        Object ltOptionsObj = caps.get("lt:options");
        Map<String, Object> ltOptions = ltOptionsObj instanceof Map ? (Map<String, Object>) ltOptionsObj : caps;
        boolean autoGrantPermissions = Boolean.parseBoolean(String.valueOf(ltOptions.get("autoGrantPermissions")));
        boolean autoAcceptAlerts = Boolean.parseBoolean(String.valueOf(ltOptions.get("autoAcceptAlerts")));

        if (autoGrantPermissions || autoAcceptAlerts) {
            logger.log("Auto permission is enabled, no need to manually allow location access.");
            continueAccessLocation();
            return;
        }

        if (driverTools.waitForElementToPresence(ALLOW_ONCE_PERMISSION_BUTTON, 5).isPresent()) {
            driverTools.click(ALLOW_ONCE_PERMISSION_BUTTON);
            driverTools.waitForElementToDisappear(ALLOW_ONCE_PERMISSION_BUTTON, 5);
        }
        continueAccessLocation();
        if (driverTools.waitForElementToPresence(ALLOW_WHILE_USING_APP, 5).isPresent()) {
            driverTools.click(ALLOW_WHILE_USING_APP);
            driverTools.waitForElementToDisappear(ALLOW_WHILE_USING_APP, 5);
        }
    }

    private void continueAccessLocation() {
        if (driverTools.waitForElementToPresence(ALLOW, 5).isPresent()) {
            driverTools.clickElementByCoordinate(ALLOW);
            driverTools.waitForElementToDisappear(ALLOW, 5);
        }
    }

    public void backToHomePage() {
        driverTools.switchToNative();
        Optional<WebElement> backButton = driverTools.waitForElementToAppear(BACK_TO_HOMEPAGE, 10);
        if (backButton.isPresent()) {
            driverTools.clickElementByCoordinate(backButton.get());
            driverTools.waitForElementToDisappear(BACK_TO_HOMEPAGE, 10);
            return;
        }
        try {
            driverTools.waitForWebview();
            driverTools.switchToWebViewUrl("airport-transfer");
            driverTools.waitForElementToAppear(BACK_TO_HOMEPAGE, 10)
                    .ifPresent(back -> driverTools.clickElementByCoordinate(back));
        } finally {
            driverTools.switchToNative();
        }
    }
}
