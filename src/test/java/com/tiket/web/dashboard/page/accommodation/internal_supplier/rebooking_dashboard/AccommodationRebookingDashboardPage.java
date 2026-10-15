package com.tiket.web.dashboard.page.accommodation.internal_supplier.rebooking_dashboard;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public abstract class AccommodationRebookingDashboardPage<T extends WebDriver> extends WebPage<T> {
    public AccommodationRebookingDashboardPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AccommodationRebookingDashboardPage<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new AccommodationRebookingDashboardPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new AccommodationRebookingDashboardPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator ORDER_ID_CELLS = new Locator("ORDER_ID_CELLS", By.xpath("//tbody/tr/td[2]")); // Order ID is in 2nd column
    private static final Locator FIRST_REBOOKING_BUTTON = new Locator("FIRST_REBOOKING_BUTTON", By.xpath("(//button[normalize-space(text())='Rebooking'])[1]"));
    private static final Locator ROOM_CARDS = new Locator("ROOM_CARDS", By.xpath("//div[contains(@class,'post-room-card') or contains(@class,'room-card')]"));
    private static final Locator FIRST_SELECT_BUTTON = new Locator("FIRST_SELECT_BUTTON", By.xpath("(//button[normalize-space(text())='select' or normalize-space(text())='SELECT' or (contains(@class,'button') and contains(@class,'primary'))])[1]"));
    private static final Locator CONTINUE_BOOKING_BUTTON = new Locator("CONTINUE_BOOKING_BUTTON", By.xpath("//button[normalize-space(text())='Continue Booking']"));
    private static final Locator POPUP_CONTINUE_BOOKING_BUTTON = new Locator("POPUP_CONTINUE_BOOKING_BUTTON", By.xpath("//button[normalize-space(text())='Continue Booking' and contains(@class,'Button_button__Mtbiw')]"));
    private static final Locator SUCCESS_MESSAGE = new Locator("SUCCESS_MESSAGE", By.xpath("//span[normalize-space(text())='Yay! The booking was successful']"));
    private static final Locator GO_TO_HOTEL_LOG_BUTTON = new Locator("GO_TO_HOTEL_LOG_BUTTON", By.xpath("//button[normalize-space(text())='Go to Hotel Log']"));
    private static final Locator FIRST_SHOW_LOG_BUTTON = new Locator("FIRST_SHOW_LOG_BUTTON", By.xpath("(//button[normalize-space(text())='Show Log'])[1]"));
    private static final Locator NEW_DATA_BUTTON = new Locator("NEW_DATA_BUTTON", By.xpath("//button[normalize-space(text())='New Data' and contains(@class,'btn-success')]"));
    private static final Locator OLD_DATA_BUTTON = new Locator("OLD_DATA_BUTTON", By.xpath("//button[normalize-space(text())='Old Data' and contains(@class,'btn-danger')]"));
    private static final Locator ORDER_ID_SEARCH_FIELD = new Locator("ORDER_ID_SEARCH_FIELD", By.xpath("//input[@placeholder='Keyword' and contains(@name,'orderId')]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[normalize-space(text())='Search']"));
    private static final Locator FILTER_RESULT_ROWS = new Locator("FILTER_RESULT_ROWS", By.xpath("//tbody/tr"));

    public void goToAccommodationRebookingDashboard() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {
                driver.get("https://gatotkaca.tiket.com/admin/cs/new-dashboard/hotel/new-rebooking/list");
            }
            case PREPROD -> {
                driver.get("https://preprod.tiket.com/admin/cs/new-dashboard/hotel/new-rebooking/list");
            }
            case PROD -> {
                driver.get("https://www.tiket.com/admin/cs/new-dashboard/hotel/new-rebooking/list");
            }
        }
        driverTools.dummyWait(3);
    }

    public String selectFailedOrder(String orderIdOrIndex) {
        driverTools.waitForElementToAppear(FILTER_RESULT_ROWS, 5);
        
        List<WebElement> orderRows = driverTools.elementsUntilExist(FILTER_RESULT_ROWS).orElse(new ArrayList<>());
        if (orderRows.isEmpty()) {
            throw new NoSuchElementException("No failed orders found");
        }

        WebElement targetRow;
        String selectedOrderId = "";

        switch (orderIdOrIndex.toLowerCase()) {
            case "first" -> {
                targetRow = orderRows.get(0);
                selectedOrderId = getOrderIdFromRow(targetRow);
            }
            case "random" -> {
                int randomIndex = CoreFunctions.generateRandomInt(0, orderRows.size() - 1);
                targetRow = orderRows.get(randomIndex);
                selectedOrderId = getOrderIdFromRow(targetRow);
            }
            default -> {
                targetRow = findOrderRowById(orderIdOrIndex);
                selectedOrderId = orderIdOrIndex;
            }
        }

        driverTools.click(targetRow);
        logger.log("Selected failed order: " + selectedOrderId);
        return selectedOrderId;
    }

    public void clickRebookingButton() {
        driverTools.waitForElementToAppear(FIRST_REBOOKING_BUTTON, 5);
        driverTools.click(FIRST_REBOOKING_BUTTON);
        driverTools.dummyWait(1);
    }

    public int checkRoomAvailability() {
        driverTools.waitForElementToAppear(ROOM_CARDS, 10);
        List<WebElement> rooms = driverTools.elementsUntilExist(ROOM_CARDS).orElse(new ArrayList<>());
        return rooms.size();
    }

    public void selectRoom(int roomIndex) {
        driverTools.waitForElementToAppear(FIRST_SELECT_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(FIRST_SELECT_BUTTON);
        driverTools.dummyWait(2);
        logger.log("Selected first available room");
    }

    public void clickContinueBooking() {
        driverTools.waitForElementToAppear(CONTINUE_BOOKING_BUTTON, 3);
        driverTools.click(CONTINUE_BOOKING_BUTTON);
        driverTools.dummyWait(1);
    }

    public boolean handleBookingPopup(String action) {
        driverTools.waitForElementToAppear(POPUP_CONTINUE_BOOKING_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(POPUP_CONTINUE_BOOKING_BUTTON);
        driverTools.dummyWait(3);
        return true;
    }

    public boolean isSuccessPageLoaded() {
        driverTools.dummyWait(3);
        return driverTools.waitForElementToAppear(SUCCESS_MESSAGE, 10).isPresent();
    }

    public Optional<String> getSuccessMessage() {
        if (driverTools.isElementDisplayed(SUCCESS_MESSAGE)) {
            return driverTools.getText(SUCCESS_MESSAGE);
        }
        return Optional.empty();
    }

    public boolean isSuccessPageByURL() {
        String currentUrl = driverTools.getCurrentURL();
        logger.log("Current URL after rebooking: " + currentUrl);
        return currentUrl.contains("success") || currentUrl.contains("confirmation") || 
               currentUrl.contains("booking-done") || currentUrl.contains("accommodation");
    }

    public void clickGoToHotelLogButton() {
        driverTools.waitForElementToAppear(GO_TO_HOTEL_LOG_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(GO_TO_HOTEL_LOG_BUTTON);
        driverTools.dummyWait(15);
        logger.log("Clicked 'Go to Hotel Log' button");
    }

    public boolean isHotelLogPageLoaded() {
        String currentUrl = driverTools.getCurrentURL();
        logger.log("Current URL after clicking Hotel Log: " + currentUrl);
        return currentUrl.contains("hotel") && (currentUrl.contains("log") || currentUrl.contains("new-dashboard"));
    }


    public void clickShowLogActionItem() {
        driverTools.waitForElementToAppear(FIRST_SHOW_LOG_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(FIRST_SHOW_LOG_BUTTON);
        driverTools.dummyWait(3);
        logger.log("Clicked 'Show Log' action item button");
    }

    public boolean isShowLogButtonClickable() {
        try {
            Optional<WebElement> showLogButtonOpt = driverTools.waitForElementToAppear(FIRST_SHOW_LOG_BUTTON, 5);
            if (showLogButtonOpt.isPresent()) {
                WebElement showLogButton = showLogButtonOpt.get();
                boolean isEnabled = showLogButton.isEnabled();
                boolean isDisplayed = showLogButton.isDisplayed();
                logger.log("Show Log button - Enabled: " + isEnabled + ", Displayed: " + isDisplayed);
                return isEnabled && isDisplayed;
            }
            return false;
        } catch (Exception e) {
            logger.log("Error checking Show Log button clickability: " + e.getMessage());
            return false;
        }
    }

    public boolean isHotelLogDetailPageLoaded() {
        try {
            driverTools.waitForElementToAppear(NEW_DATA_BUTTON, 10);
            
            boolean newDataButtonDisplayed = driverTools.isElementDisplayed(NEW_DATA_BUTTON);
            boolean oldDataButtonDisplayed = driverTools.isElementDisplayed(OLD_DATA_BUTTON);
            String currentUrl = driverTools.getCurrentURL();

            return newDataButtonDisplayed && oldDataButtonDisplayed && currentUrl.contains("log");
        } catch (Exception e) {
            logger.log("Error checking hotel log detail page: " + e.getMessage());
            return false;
        }
    }


    public void searchByOrderId(String orderId) {
        driverTools.waitForElementToAppear(ORDER_ID_SEARCH_FIELD, 5);
        driverTools.sendKeys(ORDER_ID_SEARCH_FIELD, orderId);
        driverTools.click(SEARCH_BUTTON);
        driverTools.dummyWait(3);
    }

    public boolean isOrderIdInFilterResults(String expectedOrderId) {
        try {
            driverTools.waitForElementToAppear(FILTER_RESULT_ROWS, 5);
            List<WebElement> resultRows = driverTools.elementsUntilExist(FILTER_RESULT_ROWS).orElse(new ArrayList<>());
            
            if (resultRows.isEmpty()) {
                logger.log("No filter results found");
                return false;
            }

            for (WebElement row : resultRows) {
                try {
                    WebElement orderIdCell = row.findElement(By.xpath(".//td[2]")); // Order ID is in 2nd column
                    String cellText = driverTools.getText(orderIdCell);
                    if (cellText.contains(expectedOrderId)) {
                        logger.log("Order ID " + expectedOrderId + " found in filter results");
                        return true;
                    }
                } catch (Exception e) {
                }
            }
            
            logger.log("Order ID " + expectedOrderId + " not found in filter results");
            return false;
        } catch (Exception e) {
            logger.log("Error checking filter results: " + e.getMessage());
            return false;
        }
    }


    public int getFilterResultCount() {
        try {
            driverTools.waitForElementToAppear(FILTER_RESULT_ROWS, 5);
            List<WebElement> resultRows = driverTools.elementsUntilExist(FILTER_RESULT_ROWS).orElse(new ArrayList<>());
            int count = resultRows.size();
            return count;
        } catch (Exception e) {
            logger.log("Error getting filter result count: " + e.getMessage());
            return 0;
        }
    }

    private String getOrderIdFromRow(WebElement row) {
        try {
            WebElement orderIdCell = row.findElement(By.xpath(".//td[2]")); // Order ID is in 2nd column
            return driverTools.getText(orderIdCell);
        } catch (Exception e) {
            logger.log("Could not extract order ID from row");
            return "UNKNOWN";
        }
    }

    private WebElement findOrderRowById(String orderId) {
        List<WebElement> orderCells = driverTools.elementsUntilExist(ORDER_ID_CELLS).orElse(new ArrayList<>());
        
        for (WebElement cell : orderCells) {
            String cellText = driverTools.getText(cell);
            if (cellText.contains(orderId)) {
                return cell.findElement(By.xpath("./ancestor::tr"));
            }
        }
        
        throw new NoSuchElementException("Order ID " + orderId + " not found in failed orders list");
    }
}
