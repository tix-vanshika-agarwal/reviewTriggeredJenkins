package com.tiket.web.page.accommodation.hotels.PDP;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.page.accommodation.hotels.PDP.PDPPageWeb;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.tiket.core.CoreFunctions.generateRandomInt;

public class PDPPageMweb<T extends WebDriver> extends PDPPageWeb<T> {

    private static final Locator SEE_ROOM_BUTTON = new Locator("SEE_ROOM_BUTTON",new By.ByXPath("(//button[@data-testid='button-see-room'])[2]"));
    private static final Locator PRICE_PDP = new Locator("PRICE_PDP", new By.ByXPath("(//div[contains(@class, 'Text_variant_price') and contains(@class, 'Text_align_left')])[1]"));
    private static final Locator DATE_FIELD_BUTTON = new Locator("DATE_FIELD_BUTTON", new By.ByXPath("//button[@data-testid='room-list-search-button-open-mobile-ui']"));

    public PDPPageMweb(Session<T> session) {
        super(session);
    }

    public void clickNextAvailableDate(){
        driverTools.click(NEXT_AVAILABLE_DATE_LIST_ITEM);
        driverTools.waitForElementsToAppear(SEE_ROOM_BUTTON);
    }
    protected List<Locator> getStartAndEndDateLocators(String type, String date) {
        String[] stayDate = date.split("-");

        if(stayDate[0].equals("0")){
            return null;
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

        return List.of(SELECTED_START_DATE, SELECTED_END_DATE);
    }
    protected Locator getDateElement(String elementName, int date) {
        return new Locator(elementName, AppiumBy.xpath("(//*[not(contains(@class,'disabled')) and contains(@class,'Day_day_number') and text()='" + date + "'])[1]"));
    }
    protected Locator getDateElement(String elementName, int startDate, int endDate) {
        return new Locator(elementName, AppiumBy.xpath("//*[not(contains(@class,'disabled')) and contains(@class,'Day_day_number') and text()='" + startDate + "']//following::*//*[contains(@class,'Day_day_number') and text()='" + endDate + "']"));
    }
    public void clickSeeRoomButton(){
        driverTools.click(SEE_ROOM_BUTTON);
    }

    @Override
    public String getHotelPriceDetail() {
        WebElement priceText = driverTools.waitForElementToAppear(PRICE_PDP).orElseThrow();
        return driverTools.getText(priceText);
    }
    public void setStayDatePDP(String type, String date) {
        List<Locator> startAndEndDateLocators = getStartAndEndDateLocators(type, date);
        driverTools.waitForElementToAppear(startAndEndDateLocators.get(0));
        driverTools.clickWithJavascriptExecutor(startAndEndDateLocators.get(0));
        driverTools.clickWithJavascriptExecutor(startAndEndDateLocators.get(1));
    }
}
