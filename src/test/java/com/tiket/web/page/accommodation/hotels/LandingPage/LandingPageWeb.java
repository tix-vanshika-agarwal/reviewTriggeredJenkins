package com.tiket.web.page.accommodation.hotels.LandingPage;

import com.tiket.appium.PopupObserver;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.accommodation.hotels.SRP.SRPPageDWeb;
import com.tiket.web.page.accommodation.hotels.SRP.SRPPageMweb;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public abstract class LandingPageWeb<T extends WebDriver> extends WebPage<T> {

    private static final Locator SEARCH_FORM = new Locator("SEARCH_FORM", new By.ByXPath("//*[@data-testid='destination-input-text']"));
    private static final Locator SEARCH_FORM_INPUT = new Locator("SEARCH_FORM_INPUT", new By.ByXPath("//*[@data-testid='destination-search-box']"));
    private static final Locator SEARCH_SUBMIT_BUTTON = new Locator("SEARCH_SUBMIT_BUTTON", AppiumBy.xpath("//*[@data-testid='search-button-lp']"));
    private static final Locator CONTINUE_WITHOUT_PROMO_BUTTON = new Locator("CONTINUE_WITHOUT_PROMO_BUTTON", AppiumBy.xpath("//button[@data-role='secondaryCtaClose']"));
    public static final Locator DATE_FIELD_BUTTON = new Locator("DATE_FIELD_BUTTON", AppiumBy.xpath("//*[@data-testid='date-picker']"));
    private static final Locator ROOM_GUEST_BUTTON = new Locator("ROOM_GUEST_BUTTON", AppiumBy.xpath("//*[@data-testid='rooms-and-guests']"));
    private static final Locator CURRENT_ROOM_AMOUNT = new Locator("CURRENT_ROOM_AMOUNT", AppiumBy.xpath("//*[@data-testid='rooms_container']//*[contains(@class,'quantity_input')]"));
    private static final Locator ROOM_PLUS = new Locator("ROOM_PLUS", AppiumBy.xpath("//*[@data-testid='rooms_container']//*[contains(@class,'QuantityEditor_plus_icon')]"));
    private static final Locator ROOM_MINUS= new Locator("ROOM_MINUS", AppiumBy.xpath("//*[@data-testid='rooms_container']//*[contains(@class,'QuantityEditor_minus_icon')]"));
    private static final Locator CURRENT_ADULT_AMOUNT = new Locator("CURRENT_ADULT_AMOUNT", AppiumBy.xpath("//*[@data-testid='guest_container']//*[contains(@class,'quantity_input')]"));
    private static final Locator ADULT_PLUS = new Locator("ADULT_PLUS", AppiumBy.xpath("(//*[@data-testid='guest_container']//*[contains(@class,'button')])[2]"));
    private static final Locator ADULT_MINUS = new Locator("ADULT_MINUS", AppiumBy.xpath("(//*[@data-testid='guest_container']//*[contains(@class,'button')])[1]"));
    private static final Locator DONE_SET_ROOM_GUEST = new Locator("DONE_SET_ROOM_GUEST", AppiumBy.xpath("(//*[contains(@class,'footer_button')]//*[contains(@class,'Button_variant_primary')] | //*[contains(@class,'RoomsAndGuests_button_done')]//*[@aria-label=\"Done\"])"));
    private static final Locator FIRST_HOTEL_CARD = new Locator("FIRST_HOTEL_CARD", AppiumBy.xpath("(//h3[@data-testid='product-card-property-name'])[1]"));
    private static final Locator GOT_IT_POPUP = new Locator("GOT_IT_POPUP", AppiumBy.xpath("//button[text()='Got it' or text()='Lanjut']"));
    private static final Locator FIRST_HOTEL_CARD_PDP = new Locator("FIRST_HOTEL_CARD_PDP", AppiumBy.xpath("//h1[@data-testid='name']\n"));
    private static final Locator SAVE_DATE_BUTTON = new Locator("SAVE_DATE_BUTTON", AppiumBy.xpath("//*[@aria-label=\"save\"]"));
    private static final Locator CURRENT_CHILD_AMOUNT = new Locator("CURRENT_CHILD_AMOUNT", AppiumBy.xpath("//*[@data-testid='children_visiting_container']//*[contains(@class,'quantity_input')]"));
    private static final Locator CHILD_PLUS = new Locator("CHILD_PLUS", AppiumBy.xpath("(//*[@data-testid='children_visiting_container']//*[contains(@class,'button')])[2]"));
    private static final Locator CHILD_MINUS = new Locator("CHILD_MINUS", AppiumBy.xpath("(//*[@data-testid='children_visiting_container']//*[contains(@class,'button')])[1]"));
    private static final Locator CHILD_AGE_INPUT = new Locator("CHILD_AGE_INPUT", AppiumBy.xpath("//*[contains(@class, 'PickerCardInput_child_age_card')]"));
    private static final Locator SELECTED_CHILD_AGE = new Locator("SELECTED_CHILD_AGE", AppiumBy.xpath("//*[contains(@class, 'rmc-picker-item-selected')]"));
    private static final Locator AGE_WHEEL_DONE_BUTTON = new Locator("AGE_WHEEL_DONE_BUTTON", AppiumBy.xpath("//*[contains(@class, 'PickerBottomSheet_button_done')]"));
    private static final Locator AGE_WHEEL_CONTAINER = new Locator("AGE_WHEEL_CONTAINER", AppiumBy.xpath("//*[contains(@class, 'rmc-picker-mask')]"));

    public LandingPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> LandingPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new LandingPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new LandingPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    public abstract Locator getSearchFormResultLocator(String location);

    public void clickSubmit(){
        driverTools.click(SEARCH_SUBMIT_BUTTON);
        handlePopup();
    }

    public void dismissInstallAppPopUp() {
        switch (Configs.PLATFORM) {
            case DWEB, ANDROID, IOS-> {
                return;
            }
            case AMWEB, IMWEB -> {
                try {
                    driverTools.waitForElementsToAppear(CONTINUE_WITHOUT_PROMO_BUTTON);
                    if (driverTools.isElementDisplayed(CONTINUE_WITHOUT_PROMO_BUTTON)) {
                        driverTools.click(CONTINUE_WITHOUT_PROMO_BUTTON);
                    }
                } catch (NoSuchElementException e) {
                    throw new NoSuchElementException(CONTINUE_WITHOUT_PROMO_BUTTON + " not found");
                }
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    public void setStayDate(String type, String date) {
        int[] startAndEndDate = CoreFunctions.setStayDateRange(date);
        int startDate = startAndEndDate[0];
        int endDate = startAndEndDate[1];

        if(type.equals("add date")){
            String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
            String expectedDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate,startDate);
            startDate = Integer.parseInt(expectedDate.split("-")[2]);
            String expectedEndDate = CoreFunctions.addToDateYYYY_MM_DD(expectedDate,endDate);
            endDate = Integer.parseInt(expectedEndDate.split("-")[2]);
        }

        logger.log("stringStartDate is '" + startDate + "', stringEndDate is '" + endDate + "'");

        final Locator SELECTED_START_DATE = getDateElement("SELECTED_START_DATE", startDate);
        final Locator SELECTED_END_DATE = getDateElement("SELECTED_END_DATE", startDate, endDate);

        driverTools.click(DATE_FIELD_BUTTON);

        driverTools.waitForElementToAppear(SELECTED_START_DATE);
        driverTools.clickWithJavascriptExecutor(SELECTED_START_DATE);
        driverTools.clickWithJavascriptExecutor(SELECTED_END_DATE);

        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            driverTools.waitForElementToAppear(SAVE_DATE_BUTTON);
            driverTools.click(SAVE_DATE_BUTTON);
        }
    }

    public Locator getDateElement(String elementName, int date) {
        return new Locator(elementName, AppiumBy.xpath("(//*[not(contains(@class,'disabled')) and contains(@class,'Day_day_number') and text()='"+date+"'])[1]"));
    }

    public Locator getDateElement(String elementName, int startDate, int endDate) {
        return new Locator(elementName, AppiumBy.xpath("//*[not(contains(@class,'disabled')) and contains(@class,'Day_day_number') and text()='"+startDate+"']//following::*//*[contains(@class,'Day_day_number') and text()='"+endDate+"']"));
    }

    public void handlePopup(){
        PopupObserver.inject(driver, List.of(GOT_IT_POPUP));
    }

    public void clickFirstHotelCard(){
        handlePopup();
        if (driverTools.waitForElementToPresence(FIRST_HOTEL_CARD).isPresent())
            driverTools.clickWithJavascriptExecutor(FIRST_HOTEL_CARD);
        Assertion.assertThat("Hotel is opened", driverTools.isElementDisplayed(FIRST_HOTEL_CARD_PDP), is(true));
    }

    public void clickRoomAndGuestButton(){
        driverTools.click(ROOM_GUEST_BUTTON);
    }

    public void setTotalRoom(int room){
        driverTools.waitForElementToAppear(CURRENT_ROOM_AMOUNT);
        int currentRoom = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ROOM_AMOUNT,"value"));
        logger.log("currentRoom is '" + currentRoom + "'");
        if(room > currentRoom){
            for(int i = 0; i < room - currentRoom; i++){
                driverTools.click(ROOM_PLUS);
            }
        } else if(room < currentRoom){
            for(int i = 0; i < currentRoom - room; i++){
                driverTools.click(ROOM_MINUS);
            }
        }
    }

    public void setTotalGuest(int adult){
        driverTools.waitForElementToAppear(CURRENT_ADULT_AMOUNT);
        int currentAdult = Integer.parseInt(driverTools.getValueAttribute(CURRENT_ADULT_AMOUNT,"value"));
        logger.log("currentAdult is '" + currentAdult + "'");
        if(adult > currentAdult){
            for(int i = 0; i < adult - currentAdult; i++){
                driverTools.clickWithJavascriptExecutor(ADULT_PLUS);
            }
        } else if(adult < currentAdult){
            for(int i = 0; i < currentAdult - adult; i++){
                driverTools.clickWithJavascriptExecutor(ADULT_MINUS);
            }
        }
    }

    public void setTotalChildGuest(int child){
        driverTools.waitForElementToAppear(CURRENT_CHILD_AMOUNT);
        String value = driverTools.getValueAttribute(CURRENT_CHILD_AMOUNT, "value");
        int currentChild = 0;

        if (value != null && value.matches("\\d+")) {
            currentChild = Integer.parseInt(value);
        } else {
            System.err.println("Value is not a valid integer: " + value);
        }
        logger.log("currentChild is '" + currentChild + "'");
        if(child > currentChild){
            for(int i = 0; i < child - currentChild; i++){
                driverTools.clickWithJavascriptExecutor(CHILD_PLUS);
            }
        } else if(child < currentChild){
            for(int i = 0; i < currentChild - child; i++){
                driverTools.clickWithJavascriptExecutor(CHILD_MINUS);
            }
        }
    }

    public void setChildrenAges(String ages) throws InterruptedException {
        String[] agesList = ages.split("-");
        List<WebElement> ageInputs = driverTools.elements(CHILD_AGE_INPUT).orElseThrow();

        for (int i = 0; i < agesList.length; i++) {
            logger.log("setting age for child no. " + (i + 1));
            String age = agesList[i];
            WebElement ageInput = ageInputs.get(i);

            driverTools.clickWithJavascriptExecutor(ageInput);

            int currentAge, desiredAge = Integer.parseInt(age);
            WebElement currentAgeElement = driverTools.waitForElementToAppear(SELECTED_CHILD_AGE).orElseThrow();
            currentAge = CoreFunctions.getIntFromString(driverTools.getText(currentAgeElement)).orElseThrow();

            if (currentAge < desiredAge) {
                for (int j = 0; j < desiredAge - currentAge; j++)
                    driverTools.swipeByDistance(AGE_WHEEL_CONTAINER, Swipe.UP, 0.25);
            } else {
                for (int j = 0; j < currentAge - desiredAge; j++)
                    driverTools.swipeByDistance(AGE_WHEEL_CONTAINER, Swipe.DOWN, 0.25);
            }

            currentAge = CoreFunctions.getIntFromString(driverTools.getText(SELECTED_CHILD_AGE).orElseThrow()).orElseThrow();
            Assertion.assertThat("Selected age for child no. "+ (i + 1) +" is correct ("+ age +")", currentAge, is(desiredAge));

            driverTools.clickWithJavascriptExecutor(AGE_WHEEL_DONE_BUTTON);
        }
    }

    public void clickDone(){
        driverTools.clickWithJavascriptExecutor(DONE_SET_ROOM_GUEST);
    }

    public boolean isLandingPageVisible(){
        driverTools.waitForElementToAppear(SEARCH_FORM,5);
        return driverTools.isElementDisplayed(SEARCH_FORM);
    }

    public void searchStayLocation(String location){
        driverTools.waitForElementToPresence(SEARCH_FORM, 5).orElseThrow();
        driverTools.click(SEARCH_FORM);
        driverTools.waitForElementToAppear(SEARCH_FORM_INPUT).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEARCH_FORM_INPUT);
        driverTools.sendKeys(SEARCH_FORM_INPUT, location);
        WebElement searchFormResult = driverTools.waitForElementToAppear(getSearchFormResultLocator(location)).orElseThrow();
        driverTools.click(searchFormResult);
    }

}