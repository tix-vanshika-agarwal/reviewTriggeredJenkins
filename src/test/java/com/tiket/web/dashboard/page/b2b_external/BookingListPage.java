package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.TiketMysqlDBManager;
import com.tiket.dto.CorporateOrderDTO;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import nl.jiankai.mapper.ResultSetMapperFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.*;

public class BookingListPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator BOOKING_LIST_SIDEBAR_ITEM = new Locator("BOOKING_LIST_SIDEBAR_ITEM", new By.ByXPath("//span[text()='Confirmed Bookings']"));
    private static final Locator BOOKING_LIST_HEADER = new Locator("BOOKING_LIST_HEADER", new By.ByXPath("//div[contains(@class,'w-full px-4')]//span[contains(text(), 'Booking List')]"));
    private static final Locator SEARCH_TEXTFIELD = new Locator("SEARCH_TEXTFIELD", new By.ByXPath("//input[@id='order-id']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[normalize-space()='Search']"));
    private static final Locator CLOSE_DIALOG = new Locator("CLOSE_DIALOG", new By.ByXPath("//i[@class='tix tix-cancel']"));
    private static final Locator PRICE_DETAILS_TAB = new Locator("PRICE_DETAILS_TAB", new By.ByXPath("//span[contains(@class,'px-4') and text()='Price Details']"));
    private static final Locator DOWNLOAD_CSV_BUTTON = new Locator("DOWNLOAD_CSV_BUTTON", new By.ByXPath("//button//span[contains(text(), 'Download Booking List')]"));
    private static final Locator PRICE_HEADER = new Locator("PRICE_HEADER", new By.ByXPath("(//div[@class='text-[20px] font-bold truncate'])[1]"));
    private static final Locator PRICE_DETAIL = new Locator("PRICE_DETAIL", new By.ByXPath("//*[contains(@class, 'PriceDetail_total')]//*[contains(text(), 'IDR')]"));
    private static final Locator ORDER_ID = new Locator("ORDER_ID", new By.ByXPath("//*[contains(text(), 'Order ID : ')]"));
    private static final Locator EMAIL = new Locator("EMAIL", new By.ByXPath("//*[contains(text(), 'Email : ')]"));
    private static final Locator ROUTE = new Locator("DEPARTURE_STATION", new By.ByXPath("(//span[contains(@class, 'font-bold') and contains(., '(') and contains(., ')')])[1]"));
    private static final Locator DEPARTURE_DATE = new Locator("DEPARTURE_DATE", new By.ByXPath("(//*[contains(@class, 'ProductTypes_hotel_desc')]//p)[3]"));
    private static final Locator TRAIN_NAME = new Locator("DEPARTURE_TIME", new By.ByXPath("((//*[contains(@class, 'ProductTypes_table_data')])[1]//p)[1]"));
    private static final Locator TRAIN_CLASS = new Locator("TRAIN_CLASS", new By.ByXPath("((//*[contains(@class, 'ProductTypes_table_data')])[1]//p)[2]"));
    private static final Locator TOTAL_BOOKINGS = new Locator("TOTAL_BOOKINGS", new By.ByXPath("//span[contains(text(), 'Showing 1 to')]"));
    private static final Locator PASSENGERS = new Locator("PASSENGERS", new By.ByXPath("(//*[contains(@class, 'ProductTypes_table_data')])[2]//p"));
    private static final Locator PRICE_DETAILS = new Locator("PRICE_DETAILS", new By.ByXPath("//div//p[contains(text(), 'IDR') or contains(text(), 'Included')]/ancestor::div[1]"));
    private static final Locator PURCHASE_DATE_TEXT = new Locator("PURCHASE_DATE_TEXT", By.xpath("//span[contains(text(), 'Purchase Date')]"));
    private static final Locator SCROLL = new Locator("SCROLL", new By.ByXPath("//th[contains(text(),'Passenger Details')]"));
    private static final Locator NO_DATA = new Locator("NO_DATA", new By.ByXPath("//span[@class='pt-4 pb-3 Text_text__DSnue Text_size_h3__qFeEO']"));
    private static final Locator MORE_ACTIONS_BTN = new Locator("MORE_ACTIONS_BTN", By.xpath("//div[contains(@class,'Card_card_container')]//div[contains(@class,'relative inline-block')]"));
    private static final Locator SHARE_RECEIPT = new Locator("SHARE_RECEIPT", By.xpath("//li[contains(., 'Share Receipt') and contains(@class, 'cursor-pointer')]"));
    private static final Locator SEE_RECEIPT = new Locator("SEE_RECEIPT", By.xpath("//li[contains(., 'See Receipt') and contains(@class, 'cursor-pointer')]"));
    private static final Locator SHARE_VIA_EMAIL_BTN = new Locator("SHARE_VIA_EMAIL_BTN", By.xpath("//button[normalize-space()='Share Via Email']"));
    private static final Locator SHARE_POPUP_TEXT = new Locator("SHARE_POPUP_TEXT", By.xpath("//span[normalize-space(text())='You can share this order receipt with your employees or download it.']"));
    private static final Locator BOOKINGS_SIDEBAR = new Locator("BOOKINGS_SIDEBAR", By.xpath("//span[text()='Bookings']"));

    public BookingListPage(Session<T> session) {
        super(session);
    }

    public void goToBookingListPage() {
        driverTools.waitForElementToAppear(BOOKINGS_SIDEBAR);
        driverTools.click(BOOKINGS_SIDEBAR);
        driverTools.waitForElementToAppear(BOOKING_LIST_SIDEBAR_ITEM);
        try {
            driverTools.click(BOOKING_LIST_SIDEBAR_ITEM);
        } catch (Exception e) {
            driverTools.clickElementByCoordinateDWeb(BOOKING_LIST_SIDEBAR_ITEM);
        }
        driverTools.waitForElementToAppear(BOOKING_LIST_HEADER).orElseThrow();
    }

    public void clickOnMoreActionsBtn() {
        driverTools.waitForElementToAppear(MORE_ACTIONS_BTN, 2);
        if (driverTools.isElementDisplayed(MORE_ACTIONS_BTN)) {
            driverTools.click(MORE_ACTIONS_BTN);
        }
    }

    public void clickOnShareReceiptOption() {
        driverTools.waitForElementToAppear(SHARE_RECEIPT, 2);
        if (driverTools.isElementDisplayed(SHARE_RECEIPT)) {
            driverTools.click(SHARE_RECEIPT);
        }
    }

    public void clickOnShareViaEmailBtn() {
        driverTools.waitForElementToAppear(SHARE_VIA_EMAIL_BTN, 2);
        if (driverTools.isElementDisplayed(SHARE_VIA_EMAIL_BTN)) {
            driverTools.click(SHARE_VIA_EMAIL_BTN);
        }
    }

    public boolean isShareReceiptPopupDisplayed() {
        driverTools.waitForElementToDisappear(SHARE_POPUP_TEXT, 5);
        return driverTools.isElementDisplayed(SHARE_POPUP_TEXT);
    }

    public boolean checkReceiptOpensInNewTab(String orderId) {
        WebElement seeReceiptLink = driverTools.waitForElementToAppear(SEE_RECEIPT).orElseThrow();
        driverTools.scrollIntoWebView(seeReceiptLink);

        // Save current tabs before clicking
        Set<String> oldTabs = driver.getWindowHandles();

        driverTools.clickWithJavascriptExecutor(SEE_RECEIPT);

        // Wait until a new tab appears
        String newTabHandle = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> {
                    Set<String> currentTabs = driver.getWindowHandles();
                    currentTabs.removeAll(oldTabs);
                    return currentTabs.size() > 0 ? currentTabs.iterator().next() : null;
                });

        // Switch to the new tab
        driver.switchTo().window(newTabHandle);

        // Wait for a valid URL
        String newTabUrl = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> {
                    String url = d.getCurrentUrl();
                    return (url != null && !url.isEmpty() && url.startsWith("http")) ? url : null;
                });

        logger.log("New tab URL: " + newTabUrl);

        boolean result = newTabUrl.contains(orderId);

        // Close new tab and go back
        driver.close();
        driver.switchTo().window(oldTabs.iterator().next());

        return result;
    }

    public void closeOnBoardingDialog() {
        driverTools.waitForElementToAppear(CLOSE_DIALOG, 2);
        if (driverTools.isElementDisplayed(CLOSE_DIALOG)) {
            driverTools.click(CLOSE_DIALOG);
        }
    }

    public void setSearchTextfield(String orderID) {
        driverTools.waitForElementToAppear(SEARCH_TEXTFIELD);
        driverTools.click(SEARCH_TEXTFIELD);
        driverTools.sendKeys(SEARCH_TEXTFIELD, orderID);
    }

    public void hitSearchButton() throws InterruptedException {
        driverTools.waitForElementToBeClickable(SEARCH_BUTTON).orElseThrow();
        driverTools.click(SEARCH_BUTTON);
        Thread.sleep(3000);
    }

    public boolean checkIfSearchedOrderExists(String orderID) {
        Locator ORDERID_DETAIL_TEXT = new Locator("ORDERID_DETAIL_TEXT", new By.ByXPath("//span[contains(text(),'Order ID') and contains(.,'" + orderID + "')]"));
        driverTools.waitForElementToAppear(ORDERID_DETAIL_TEXT, 60).orElseThrow();
        return driverTools.isElementDisplayed(ORDERID_DETAIL_TEXT);
    }

    public String getTotalPriceInHeader() {
        driverTools.waitForElementToAppear(PRICE_HEADER);
        return driverTools.getText(PRICE_HEADER).orElseThrow();//.replace(",", ".");
    }

    public void clickPriceDetailsTab() {
        driverTools.maximizeWindow();
        driverTools.waitForElementToAppear(PRICE_DETAILS_TAB);
        driverTools.clickWithJavascriptExecutor(PRICE_DETAILS_TAB);
        driverTools.waitForElementToAppear(PRICE_HEADER);
    }

    public String getTotalPriceInPriceDetails() {
        driverTools.waitForElementToAppear(PRICE_DETAIL);
        driverTools.waitForElementToAppear(PRICE_DETAIL);
        return driverTools.getText(PRICE_DETAIL).orElseThrow();
    }

    public boolean downloadCSV() throws InterruptedException {
        driverTools.waitForElementToPresence(DOWNLOAD_CSV_BUTTON);
        if (!driverTools.isElementDisplayed(DOWNLOAD_CSV_BUTTON)) {
            return false;
        }
        driverTools.clickWithJavascriptExecutor(DOWNLOAD_CSV_BUTTON);
        Thread.sleep(3000);
        return true;
    }

    public HashMap<String, Object> getOrderDetailsB2BDashboard() {
        HashMap<String, Object> orderDetailsMap = new HashMap<>();
        orderDetailsMap.put("orderIDFromDetail", getOrderIDFromDetail());
        orderDetailsMap.put("email", getEmail());
        orderDetailsMap.put("departureStation", getDepartureStation());
        orderDetailsMap.put("arrivalStation", getArrivalStation());
        orderDetailsMap.put("departureDate", getDepartureDate());
        orderDetailsMap.put("totalPrice", getTotalPriceInHeader());
        orderDetailsMap.put("trainName", getTrainName());
        orderDetailsMap.put("trainClass", getTrainClass());
        orderDetailsMap.put("passengers", getPassengers());
        return orderDetailsMap;
    }

    public String getEmail() {
        String email = driverTools.getText(EMAIL).orElseThrow();
        email = email.replaceAll("Email : ", "");
        return email;
    }

    public void printMap(HashMap orderDetailMap) {
        List arrayListOrderMap = Arrays.asList(orderDetailMap); // method 1
        logger.log("From Your Orders: " + arrayListOrderMap);
    }

    public String getOrderIDFromDetail() {
        driverTools.waitForElementToAppear(ORDER_ID, 60);
        String email = driverTools.getText(ORDER_ID).orElseThrow();
        email = email.replaceAll("Order ID : ", "");
        return email;
    }

    public String getDepartureStation() {
        String trainRoute = driverTools.getText(ROUTE).orElseThrow();
        String departureStation = trainRoute.split("\\r?\\n")[0].trim();
        departureStation = departureStation.toLowerCase();
        return departureStation;
    }

    public String getArrivalStation() {
        String trainRoute = driverTools.getText(ROUTE).orElseThrow();
        String departureStation = trainRoute.split("\\r?\\n")[1].trim();
        departureStation = departureStation.toLowerCase();
        return departureStation;
    }

    public String getDepartureDate() {
        String departureDate = driverTools.getText(DEPARTURE_DATE).orElseThrow();
        departureDate = departureDate.split(", ")[1];
        departureDate = departureDate.toLowerCase();
        return departureDate;
    }

    public ArrayList<String> getPassengers() {
        List<WebElement> passengerNameElements = (driverTools.elements(PASSENGERS)).orElseThrow();
        ArrayList<String> passengers = new ArrayList<>();
        for (WebElement passengerNameElement : passengerNameElements) {
            String passengerName = driverTools.getText(passengerNameElement);
            passengerName = passengerName.toLowerCase();
            passengerName = passengerName.substring(passengerName.indexOf(". ") + 2);
            passengerName = passengerName.replace(".", "");
            passengers.add(passengerName);
        }
        return passengers;
    }

    public String getTrainName() {
        String trainName = driverTools.getText(TRAIN_NAME).orElseThrow();
        trainName = trainName.toLowerCase();
        trainName = trainName.replace("-", "").trim();
        trainName = trainName.toLowerCase();
        return trainName;
    }

    public String getTrainClass() {
        String trainClass = driverTools.getText(TRAIN_CLASS).orElseThrow().trim();
        trainClass = trainClass.toLowerCase();
        return trainClass;
    }

    public HashMap<String, String> getPriceDetails() {
        clickPriceDetailsTab();
        List<WebElement> priceDetailRows = driverTools.elements(PRICE_DETAILS).orElseThrow();
        HashMap<String, String> priceDetailBreakdown = new HashMap<>();
        for (WebElement priceDetailRow : priceDetailRows) {
            String feeName = driverTools.getText(priceDetailRow.findElement(By.xpath("//p[1]")));
            String fee = driverTools.getText(priceDetailRow.findElement(By.xpath("//p[2]")));
            fee = fee.replace(",", ".");
            priceDetailBreakdown.put(feeName, fee);
        }

        return priceDetailBreakdown;
    }

    public HashMap<String, Object> getDataDB(String orderID) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        String sqlQuery = "SELECT * FROM tix_corporate.corporate_orders co WHERE co.order_id=" + orderID;
        var db = TiketMysqlDBManager.getInstance("corporate_order");
        var dbExecute = ResultSetMapperFactory.getResultSetMapperIdentity().map(db.executeQuery(sqlQuery),
                CorporateOrderDTO.class);
        CorporateOrderDTO result;
        if (dbExecute.stream().count() < 1) {
            String msg = "Data not found in Database!";
            logger.fail(msg);
            throw new Exception(msg);
        } else {
            result = dbExecute.get(0);
        }
        logger.log("Data found in Database : \n" + result.toString());
        map.put("OrderID", result.getOrder_id());
        map.put("productType", result.getProduct_type());
        map.put("totalPrice", result.getAmount());
        map.put("paymentStatus", result.getPayment_status());
        logger.log("STRING amount: " + String.valueOf(result.getAmount()));
        return map;
    }

    public int convertAmountToInt(String amountString) {
        amountString = amountString.replace("IDR ", "").replace(".", "").replace(",", "");
        return Integer.parseInt(amountString);
    }

    public int getNumberOfBookingList() {
        String paginationText = driverTools.getText(TOTAL_BOOKINGS).orElseThrow();
        logger.log(paginationText);
        logger.log("Total: " + Integer.parseInt(paginationText.split("of ")[1]));
        return Integer.parseInt(paginationText.split("of ")[1]);
    }

    public File getDownloadedCSV() throws FileNotFoundException, InterruptedException {
        String dateString = CoreFunctions.getCurrentDateTime();
        ArrayList<File> files = getBookingListCSV();
        int retryCounter = 0;
        while (files.isEmpty() && retryCounter < 5) {
            Thread.sleep(2000);
            files = getBookingListCSV();
            retryCounter++;
            logger.log("No.of Searches: " + retryCounter);
        }

        if (files.isEmpty()) throw new FileNotFoundException();
        files.sort(Comparator.comparingLong(File::lastModified).reversed());
        return files.get(0);
    }

    public List<List<String>> getCSVContent(File FileCSV) {
        List<List<String>> csvContent = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(FileCSV.toURI()))) {
            while (scanner.hasNextLine()) {
                csvContent.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        logger.log(String.valueOf(csvContent.size()));
        csvContent.remove(0);
        csvContent.remove(csvContent.size() - 1);
        logger.log(String.valueOf(csvContent.size()));
        return csvContent;
    }


    public boolean checkOrderIDInCSV(String orderID, List<List<String>> csvContent) {
        boolean isExist = false;

        for (int i = 0; i < csvContent.size(); i++) {
            List<String> csvRow = csvContent.get(i);
            logger.log("Row" + i + ": " + csvRow.toString());
            if (csvRow.get(5).equals(orderID)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    private ArrayList<File> getBookingListCSV() {
        String dateStringFileFormat = CoreFunctions.getCurrentDateDD_MM_YYYY();
        Long currentTimeInMillis = System.currentTimeMillis();
        String home = System.getProperty("user.home");
        File downloadsDirectory = new File(home + "/Downloads/");
        ArrayList<File> files = new ArrayList<>();
        for (File fileEntry : downloadsDirectory.listFiles()) {
            boolean isDownloadedInPast60Seconds = fileEntry.lastModified() > (currentTimeInMillis - 60000);
            boolean isCorrectNamingConvention = fileEntry.getName().contains("booking_lists_" + dateStringFileFormat);
            boolean isNotDirectory = !fileEntry.isDirectory();
            if (isDownloadedInPast60Seconds && isCorrectNamingConvention && isNotDirectory) {
                files.add(fileEntry);
            }
        }
        return files;
    }

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    public String getPurchaseDateText() {
        driverTools.waitForElementToAppear(PURCHASE_DATE_TEXT);
        return driverTools.getText(PURCHASE_DATE_TEXT).orElseThrow();
    }

    public Map<String, String> getFlightDetails() {
        Map<String, String> flightDetails = new HashMap<>();

        flightDetails.put("Name", driverTools.getText(new Locator("FLIGHT_PASSENGER_NAME", By.xpath("//p[contains(@class, 'ProductTypes_passenger_name_text')]"))).orElse(""));
        flightDetails.put("Price", driverTools.getText(new Locator("FLIGHT_PRICE", By.xpath("//*[contains(text(), 'IDR') and contains(text(), '.')]"))).orElse(""));


        return flightDetails;
    }
}
