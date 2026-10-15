package com.tiket.page.ferry.FerrySearchPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.time.LocalDateTime;
import java.util.Map;

public abstract class FerrySearchPage <T extends WebDriver> extends BasePage<T> {

    private static final Locator PORT_ORIGIN_INPUT = new Locator("PORT_ORIGIN_INPUT", AppiumBy.xpath("(//div[contains(@class,'PortSegment_container')])[1]"));
    private static final Locator PORT_DESTINATION_INPUT = new Locator("PORT_DESTINATION_INPUT", AppiumBy.xpath("(//div[contains(@class,'PortSegment_container')])[2]"));
    private static final Locator FERRY_SEARCH_PAGE_MODAL = new Locator("FERRY_SEARCH_PAGE_MODAL", AppiumBy.cssSelector("#modal-root"));
    private static final Locator FERRY_SUBMIT_BTN = new Locator("FERRY_SUBMIT_BTN", AppiumBy.xpath("//button[contains(@class,'submit_button')]"));
    private static final Locator SKIP_COACHMARK_BTN = new Locator("SKIP_COACHMARK_BTN", AppiumBy.xpath("//*[contains(text(), 'Lewati') or contains(text(), 'Skip')]"));
    private static final Locator TICKET_TYPE_CONTAINER = new Locator("TICKET_TYPE_CONTAINER", AppiumBy.xpath("//div[contains(@class,'VehicleInput_container')]"));
    private static final Locator DATE_SECTION = new Locator("DATE_SECTION", AppiumBy.xpath("//div[contains(@class,'DateTimeInput_container_date') and span]"));
    private static final Locator SAVE_CALENDAR = new Locator("SAVE_CALENDAR", AppiumBy.xpath("//button//span[contains(text(),'Simpan') or contains(text(),'Save')]"));
    private static final Locator TICKET_QTY_SAVE_BUTTON = new Locator("TICKET_QTY_SAVE_BUTTON", AppiumBy.xpath("//*[contains(@class,'BottomSheet_bottom_sheet')]//*[contains(@class,'Button_button')]"));
    private static final Locator TICKET_QTY = new Locator("TICKET_QTY", AppiumBy.xpath("//*[contains(@class,'PassengerInput_input_text')]"));
    private static final Locator X_BOTTOM_SHEET = new Locator("X_BOTTOM_SHEET", AppiumBy.xpath("//button[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator ALLOW_ACCESS_BUTTON_IOS = new Locator("ALLOW_ACCESS_BUTTON_IOS", AppiumBy.xpath("//*[@type='XCUIElementTypeButton' and @name='Allow']"));
    private static final Locator ALLOW_ACCESS_BUTTON_ANDROID = new Locator("ALLOW_ACCESS_BUTTON_ANDROID", AppiumBy.xpath("//*[@resource-id='com.android.permissioncontroller:id/permission_allow_one_time_button']"));

    private static Map<String,String> ferryTicketTypeIdToEn = Map.of(
            "Mobil Penumpang", "Passenger Car",
            "Pejalan Kaki", "Pedestrian",
            "Sepeda", "Bicycle",
            "Truk Pickup", "Pickup Truck"
    );

    private static Map<String,String> ferryTicketTypeEnToId = Map.of(
            "Passenger Car", "Mobil Penumpang",
            "Pedestrian", "Pejalan Kaki",
            "Bicycle", "Sepeda",
            "Pickup Truck", "Truk Pickup"
    );


    public FerrySearchPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FerrySearchPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {
                return new FerrySearchPageIOS<>(session);
            }
            case ANDROID -> {
                return new FerrySearchPageAndroid<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }


    public abstract void openViaDeeplink();

    public void skipCoachMark(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("ferry");
        driverTools.waitForElementToAppear(SKIP_COACHMARK_BTN);
        driverTools.element(SKIP_COACHMARK_BTN).ifPresent(driverTools::click);
        driverTools.switchToNative();
    }

    public void allowLocationPopup(){
        driverTools.switchToNative();
        if (Configs.PLATFORM == Platform.IOS){
            driverTools.waitForElementToPresence(ALLOW_ACCESS_BUTTON_IOS,18).ifPresent(driverTools::click);
        }
        else if (Configs.PLATFORM == Platform.ANDROID) {
            driverTools.waitForElementToPresence(ALLOW_ACCESS_BUTTON_ANDROID, 18).ifPresent(driverTools::click);
        }
    }

    private Locator getPortSuggestionLocator(String portName){
        return new Locator("PORT_"+portName.toUpperCase()+"_LOCATOR", AppiumBy.xpath("//div[contains(@class,'List_list_content') and div[p[contains(text(),'"+portName+"')]]]"));
    }

    private Locator getVehicleTypeLocator(String vehicleType){
        logger.log("Vehcile type : " + vehicleType);
        logger.log("DEBUG -> map data :  " + ferryTicketTypeIdToEn);
        String vehicleTypeEn = ferryTicketTypeIdToEn.get(vehicleType) == null ? vehicleType : ferryTicketTypeIdToEn.get(vehicleType);
        String vehicleTypeId = ferryTicketTypeEnToId.get(vehicleType) == null ? vehicleType : ferryTicketTypeEnToId.get(vehicleType);
        return new Locator("VEHICLE_TYPE_"+vehicleType, AppiumBy.xpath("//span[contains(@class,'VehiclePopup_title') and (contains(text(),'"+vehicleTypeEn+"') or contains(text(),'"+vehicleTypeId+"'))]"));
    }

    public void inputOrigin(String origin){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("ferry");
        driverTools.click(PORT_ORIGIN_INPUT); //reason : element click intercepted error
        driverTools.waitForElementToAppear(FERRY_SEARCH_PAGE_MODAL,2);
        driverTools.click(getPortSuggestionLocator(origin));
        driverTools.switchToNative();
    }

    public void inputDestination(String destination){
        driverTools.switchToWebViewUrl("ferry");
        driverTools.click(PORT_DESTINATION_INPUT); //reason : elemet click intercepted error
        driverTools.waitForElementToAppear(FERRY_SEARCH_PAGE_MODAL,2);
        driverTools.click(getPortSuggestionLocator(destination));
        driverTools.switchToNative();
    }

    public void selectTicketType(String ticketType){
        driverTools.switchToWebViewUrl("ferry");
        driverTools.click(TICKET_TYPE_CONTAINER);
        driverTools.waitForElementToAppear(getVehicleTypeLocator(ticketType),3);
        driverTools.click(getVehicleTypeLocator(ticketType));
        if(!(driverTools.waitForElementToDisappear(getVehicleTypeLocator(ticketType)))) {
            driverTools.click(X_BOTTOM_SHEET);
        }
        driverTools.switchToNative();
    }

    public void submitSearch(){
        driverTools.switchToWebViewUrl("ferry");
        driverTools.click(FERRY_SUBMIT_BTN);
        driverTools.switchToNative();
    }

    private Locator getDateLocator(String date){
        String year = CoreFunctions.getSectionOfString(date," ", 2);
        String month = CoreFunctions.getSectionOfString(date," ", 1);
        String day = CoreFunctions.getSectionOfString(date," ", 0);
        String monthId = CoreFunctions.monthEngToId(month);
        String locatorTextEng = day + " " + month +" "+ year;
        String locatorTextId = day + " " + monthId +" "+ year;

        logger.log("date locator : " + locatorTextEng);
        return new Locator(locatorTextEng,AppiumBy.xpath("//*[contains(@aria-label,'" + locatorTextEng + "') or contains(@aria-label,'" + locatorTextId + "')]"));
    }

    public void selectDate(String dateDeparture){
        String targetDateDeparture = "";

        // Check if the dateDeparture contains "+" (for a relative day selection)
        if (dateDeparture.contains("+")) {
            // Select the day based on today's date + given days in dateDeparture
            LocalDateTime today = LocalDateTime.now().plusDays(Integer.parseInt(dateDeparture.substring(1)));
            targetDateDeparture += today.getDayOfMonth() + " " + CoreFunctions.getMonthName(today.getMonthValue(), "en") + " " + today.getYear();
        } else if (dateDeparture.equals(CoreFunctions.getCurrentDate("d MMMM yyyy"))) {
            // If dateDeparture is equal to the current date, switch to the next day
            LocalDateTime nextDay = LocalDateTime.now().plusDays(1);
            targetDateDeparture += nextDay.getDayOfMonth() + " " + CoreFunctions.getMonthName(nextDay.getMonthValue(), "en") + " " + nextDay.getYear();
        } else {
            // If dateDeparture is explicitly provided, use it directly
            targetDateDeparture += dateDeparture;
        }

        driverTools.switchToWebView();
        driverTools.click(DATE_SECTION);
        driverTools.click(getDateLocator(targetDateDeparture));
        driverTools.click(SAVE_CALENDAR);
        driverTools.switchToNative();
    }


    public void resetTicketQty() {
        setTicketQty("adult", 1);
        setTicketQty("child", 0);
        setTicketQty("infant", 0);
        setTicketQty("elder", 0);
    }

    public void openTicketQty() {
        driverTools.switchToWebView();
        driverTools.click(TICKET_QTY);
        driverTools.switchToNative();
    }

    public void setTicketQty(String ticketQtyType, int ticketQtyCountInt) {

        int ticketType = switch (ticketQtyType.toLowerCase()) {
            case "adult" -> 1;
            case "child" -> 2;
            case "infant" -> 3;
            case "elder" -> 4;
            default -> {
                logger.fail("Unknown ticket type: " + ticketQtyType);
                yield -1;
            }
        };

        if (ticketType == -1) return;

        driverTools.switchToWebView();

        int currentQty = Integer.parseInt(driverTools.getValueAttribute(getTicketQtyCountLocator(ticketType), "value"));
        int diff = ticketQtyCountInt - currentQty;

        logger.log("Current qty: " + currentQty + ", Desired qty: " + ticketQtyCountInt);

        if (diff > 0) {
            clickTicketQtyIncrementLocator(ticketType, diff);
        } else if (diff < 0) {
            clickTicketQtyDecreaseLocator(ticketType, -diff);
        } else {
            logger.log("Quantity already correct. No action needed.");
        }

        driverTools.switchToNative();
    }

    /**
     * Sets ticket quantities for all passenger types at once
     * @param adult Number of adult passengers
     * @param child Number of child passengers
     * @param infant Number of infant passengers
     * @param elder Number of elderly passengers
     */
    public void setTicketQty(int adult, int child, int infant, int elder) {
        logger.log("Setting ticket quantities - Adult: " + adult + ", Child: " + child + ", Infant: " + infant + ", Elder: " + elder);
        
        driverTools.switchToWebView();
        
        // Set adult count
        if (adult > 0) {
            setTicketQtyForType(1, adult);
        }
        
        // Set child count
        if (child >= 0) {
            setTicketQtyForType(2, child);
        }
        
        // Set infant count
        if (infant >= 0) {
            setTicketQtyForType(3, infant);
        }
        
        // Set elder count
        if (elder >= 0) {
            setTicketQtyForType(4, elder);
        }
        
        driverTools.switchToNative();
    }

    private void setTicketQtyForType(int ticketType, int desiredQty) {
        int currentQty = Integer.parseInt(driverTools.getValueAttribute(getTicketQtyCountLocator(ticketType), "value"));
        int diff = desiredQty - currentQty;

        String ticketTypeName = switch (ticketType) {
            case 1 -> "Adult";
            case 2 -> "Child";
            case 3 -> "Infant";
            case 4 -> "Elder";
            default -> "Unknown";
        };

        logger.log(ticketTypeName + " - Current qty: " + currentQty + ", Desired qty: " + desiredQty);

        if (diff > 0) {
            clickTicketQtyIncrementLocator(ticketType, diff);
        } else if (diff < 0) {
            clickTicketQtyDecreaseLocator(ticketType, -diff);
        } else {
            logger.log(ticketTypeName + " quantity already correct. No action needed.");
        }
    }

    public void saveTicketQty() {
        driverTools.switchToWebView();
        driverTools.clickWithJavascriptExecutor(TICKET_QTY_SAVE_BUTTON);
        driverTools.switchToNative();
    }

    private void clickTicketQtyDecreaseLocator(int ticketType, int i) {
        for(int j = 0; j < i; j++) {
            driverTools.click(getTicketDecreaseLocator(ticketType));
        }
    }

    @SneakyThrows
    private void clickTicketQtyIncrementLocator(int ticketType, int count){
        for (int i = 0; i < count; i++) {
            logger.log("Increment click #" + (i + 1) + " for ticketType: " + ticketType);

            // Wait until the increment button is clickable and then click it
            driverTools.waitForElementToBeClickable(getTicketIncrementLocator(ticketType))
                    .ifPresentOrElse(
                            driverTools::click,
                            () -> logger.fail("Increment button not clickable for ticketType: " + ticketType)
                    );
        }
    }

    private Locator getTicketQtyCountLocator(int ticketType) {
        return new Locator("TICKET_QTY" + ticketType + "_CURRENT",AppiumBy.xpath("//*[contains(@class,'PassengerPopup_counter')][" + ticketType + "]//*[contains(@class,'QuantityEditor_quantity_input')]"));
    }

    private Locator getTicketIncrementLocator(int ticketType) {
        return new Locator("INCREMENT_TICKET_QTY" + ticketType,AppiumBy.xpath("//*[contains(@class,'PassengerPopup_counter')][" + ticketType + "]//*[contains(@class,'QuantityEditor_operation_button')][2]"));
    }

    private Locator getTicketDecreaseLocator(int ticketType) {
        return new Locator("DECREASE_TICKET_QTY" + ticketType,AppiumBy.xpath("//*[contains(@class,'PassengerPopup_counter')][" + ticketType + "]//*[contains(@class,'QuantityEditor_operation_button')][1]"));
    }
}
