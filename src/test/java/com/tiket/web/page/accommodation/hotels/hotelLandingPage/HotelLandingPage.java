package com.tiket.web.page.accommodation.hotels.hotelLandingPage;

import com.tiket.appium.PopupObserver;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static org.hamcrest.Matchers.is;

public class HotelLandingPage<T extends WebDriver> extends BasePage<T>{

    private static final Locator SEARCH_FORM = new Locator("SEARCH_FORM", new By.ByXPath("//*[@data-testid='destination-input-text']"));
    private static final Locator SEARCH_FORM_INPUT = new Locator("SEARCH_FORM_INPUT", new By.ByXPath("//*[@data-testid='destination-search-box']"));
    public static final Locator DATE_FIELD_BUTTON = new Locator("DATE_FIELD_BUTTON", AppiumBy.xpath("//*[@data-testid='date-picker']"));
    private static final Locator SEARCH_SUBMIT_BUTTON = new Locator("SEARCH_SUBMIT_BUTTON", AppiumBy.xpath("//*[@data-testid='search-button-lp']"));
    private static final Locator ROOM_GUEST_BUTTON = new Locator("ROOM_GUEST_BUTTON", AppiumBy.xpath("//*[@data-testid='rooms-and-guests']"));
    private static final Locator CURRENT_ROOM_AMOUNT = new Locator("CURRENT_ROOM_AMOUNT", AppiumBy.xpath("//*[@data-testid='rooms_container']//*[contains(@class,'quantity_input')]"));
    private static final Locator ROOM_PLUS = new Locator("ROOM_PLUS", AppiumBy.xpath("//*[@data-testid='rooms_container']//*[contains(@class,'QuantityEditor_plus_icon')]"));
    private static final Locator ROOM_MINUS= new Locator("ROOM_MINUS", AppiumBy.xpath("//*[@data-testid='rooms_container']//*[contains(@class,'QuantityEditor_minus_icon')]"));
    private static final Locator CURRENT_ADULT_AMOUNT = new Locator("CURRENT_ADULT_AMOUNT", AppiumBy.xpath("//*[@data-testid='guest_container']//*[contains(@class,'quantity_input')]"));
    private static final Locator ADULT_PLUS = new Locator("ADULT_PLUS", AppiumBy.xpath("(//*[@data-testid='guest_container']//*[contains(@class,'button')])[2]"));
    private static final Locator ADULT_MINUS = new Locator("ADULT_MINUS", AppiumBy.xpath("(//*[@data-testid='guest_container']//*[contains(@class,'button')])[1]"));
    private static final Locator DONE_SET_ROOM_GUEST = new Locator("DONE_SET_ROOM_GUEST", AppiumBy.xpath("//button[text()='Done' or text()='Selesai']"));
    private static final Locator FIRST_HOTEL_CARD = new Locator("FIRST_HOTEL_CARD", AppiumBy.xpath("(//h3[@data-testid='product-card-property-name'])[1]"));
    private static final Locator GOT_IT_POPUP = new Locator("GOT_IT_POPUP", AppiumBy.xpath("//button[text()='Got it']"));
    private static final Locator FIRST_HOTEL_CARD_PDP = new Locator("FIRST_HOTEL_CARD_PDP", AppiumBy.xpath("//h1[@data-testid='name']"));

    public HotelLandingPage(Session<T> session) {
        super(session);
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

    private Locator getSearchFormResultLocator(String location){
        return new Locator("SEARCH_FORM_RESULT", new By.ByXPath("//*[@data-testid='item-name']//*[text()='"+location+"']"));
    }

    public void setStayDate(String type, String date){
        String[] stayDate = date.split("-");

        if(stayDate[0].equals("0")){
            return;
        }

        int startDate = Integer.parseInt(stayDate[0]);
        int endDate = Integer.parseInt(stayDate[1]);

        if(!type.equals("add date")){
            if (startDate <= 0
                    || startDate > 31
                    || startDate > endDate
                    || endDate > 31
                    || endDate <= 0) {
                throw new DriverToolsException("Test data 'date' with value '" + date + "' is not valid, please check again");
            }
        }

        if (type.equals("range date")) {
            int randomStartDate = generateRandomInt(startDate, endDate - 1);
            int randomEndDate = generateRandomInt(randomStartDate + 1, endDate);

            startDate = randomStartDate;
            endDate = randomEndDate;
        }

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
    }

    private Locator getDateElement(String elementName, int date) {
        return new Locator(elementName, AppiumBy.xpath("(//*[not(contains(@class,'disabled')) and contains(@class,'Day_day_number') and text()='"+date+"'])[1]"));
    }

    private Locator getDateElement(String elementName, int startDate, int endDate) {
        return new Locator(elementName, AppiumBy.xpath("//*[not(contains(@class,'disabled')) and contains(@class,'Day_day_number') and text()='"+startDate+"']//following::*//*[contains(@class,'Day_day_number') and text()='"+endDate+"']"));
    }

    public void clickSubmit(){
        driverTools.clickWithJavascriptExecutor(SEARCH_SUBMIT_BUTTON);
        handlePopup();
    }

    private void handlePopup(){
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

    public void clickDone(){
        driverTools.clickWithJavascriptExecutor(DONE_SET_ROOM_GUEST);
    }

    public boolean isLandingPageVisible(){
        driverTools.waitForElementToAppear(SEARCH_FORM,5);
        return driverTools.isElementDisplayed(SEARCH_FORM);
    }
}