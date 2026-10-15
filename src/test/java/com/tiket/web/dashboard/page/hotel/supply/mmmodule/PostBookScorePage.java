package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PostBookScorePage <T extends WebDriver> extends BasePage<T> {
    public PostBookScorePage(Session<T> session) {
        super(session);
    }

    private static final Locator POST_BOOK_SCORE_TITLE_LABEL = new Locator("POST_BOOK_SCORE_TITLE_LABEL", new By.ByXPath("//span[contains(@class,'h3')][contains(text(),'Post-Book')]"));
    private static final Locator SEARCH_TYPE_VENDOR_TAB = new Locator("SEARCH_TYPE_VENDOR_TAB", new By.ByXPath("//button[@data-testid=\"vendor_id\"]"));
    private static final Locator FILTER_VENDOR_NAME_INPUT = new Locator("FILTER_VENDOR_NAME_INPUT", new By.ByName("vendorName"));
    private static final Locator FILTER_VENDOR_ID_INPUT = new Locator("FILTER_VENDOR_ID_INPUT", new By.ByName("vendorId"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[contains(@class,'primary')]"));
    private static final Locator ADD_NEW_REPORT_MODAL = new Locator("ADD_NEW_REPORT_MODAL", new By.ByXPath("//*[contains(@class,\"modal_popup\")]"));
    private static final Locator REPORT_REASON_TEXTAREA = new Locator("REPORT_REASON_TEXTAREA", new By.ById("text-area-report-modal"));
    private static final Locator SUBMIT_REPORT_BUTTON = new Locator("SUBMIT_REPORT_BUTTON", new By.ById("report-button-submit"));
    private static final Locator CONFIRM_SUBMIT_REPORT_BUTTON = new Locator("CONFIRM_SUBMIT_REPORT_BUTTON", new By.ByXPath("//button[@data-testid=\"button-submit-confirmation-update\"]"));
    private static final Locator SUCCESSFUL_SNACKBAR = new Locator("SUCCESSFUL_SNACKBAR", new By.ByXPath("//div[contains(@class,'Snackbar_text_content')]"));
    private static final Locator DOWNLOAD_CSV_BUTTON = new Locator("DOWNLOAD_CSV_BUTTON",
            By.xpath("//button[@data-testid='button-download-file']"));
    private static final Locator ACCOMMODATION_TYPE_DROPDOWN = new Locator("ACCOMMODATION_TYPE_DROPDOWN",
            By.xpath("(//button[contains(@class,'styles_clickable_icon')])[3]"));

    private static final Locator HOTEL_TYPE = new Locator("HOTEL_TYPE",
            By.xpath("//li[@data-value='Hotel']"));

    private static final Locator COLUMN_HOTEL = new Locator("COLUMN_HOTEL", new By.ByXPath("(//tr[1]//th//div)[2]"));
    private static final Locator COLUMN_ACCOM_TYPE = new Locator("COLUMN_ACCOM_TYPE", new By.ByXPath("(//tr[1]//th//div)[4]"));
    private static final Locator COLUMN_HOTEL_RESULT = new Locator("COLUMN_HOTEL_RESULT", new By.ByXPath("(//tr[1]//td)[2]//a"));
    private static final Locator COLUMN_ACCOMTYPE_RESULT = new Locator("COLUMN_ACCOMTYPE_RESULT", new By.ByXPath("(//tr[1]//td)[4]"));

    private static Locator getFilterVendorNameDropdownList(String text) {
        return new Locator("VENDOR_NAME_LIST", new By.ByXPath(String.format("//li[@data-name=\"vendorName\"][text()='%s']", text.toUpperCase())));
    }

    private static Locator getSearchResultHotelNameByIndex(int index, String text) {
        return new Locator("SEARCH_RESULT_HOTEL_NAME_LABEL", new By.ByXPath(String.format("//tr[@data-testid=\"tr-table-body\"][%d]/td[2]/a[text()='%s']", index, text)));
    }

    private static Locator getSearchResultScoreStatusByHotelName(int index, String text) {
        return new Locator("SEARCH_RESULT_SCORE_STATUS_LABEL", new By.ByXPath(String.format("//tr[@data-testid=\"tr-table-body\"][%d]/td[2]/a[text()='%s']/parent::td/following-sibling::td[3]//div[@class=\"flex items-center\"]", index, text)));
    }

    private static Locator getReportButtonByHotelName(String text) {
        return new Locator("REPORT_BUTTON", new By.ByXPath(String.format("//a[text()='%s']/ancestor::td/following-sibling::td[last()]//button[@data-testid='button-report']", text)));
    }

    private static Locator getReportScoreStatusRadioByName(String text) {
        String ariaLabel = switch (text) {
            case "GOOD" -> "score-status-Good";
            case "BAD" -> "score-status-Bad";
            case "VERY_BAD" -> "score-status-Very Bad";
            default -> "";
        };
        return new Locator("REPORT_SCORE_STATUS_RADIO", new By.ByXPath(String.format("//input[@aria-label='%s']/ancestor::label", ariaLabel)));
    }

    public boolean isPostBookScorePageDisplayed() {
        WebElement tab = driverTools.waitForElementToBeClickable(SEARCH_TYPE_VENDOR_TAB).orElseThrow();
        return driverTools.isElementDisplayed(tab);
    }

    public void clickSearchByVendorTab() {
        WebElement tab = driverTools.waitForElementToBeClickable(SEARCH_TYPE_VENDOR_TAB).orElseThrow();
        driverTools.click(tab);
    }

    public void selectFilterVendorName(String text) {
        WebElement input = driverTools.waitForElementToBeClickable(FILTER_VENDOR_NAME_INPUT).orElseThrow();
        driverTools.click(input);
        WebElement list = driverTools.waitForElementToBeClickable(getFilterVendorNameDropdownList(text)).orElseThrow();
        driverTools.click(list);
    }

    public void enterFilterVendorId(String text) {
        WebElement input = driverTools.waitForElementToPresence(FILTER_VENDOR_ID_INPUT).orElseThrow();
        driverTools.sendKeys(input, text);
    }

    public void clickSearch() {
        WebElement button = driverTools.waitForElementToBeClickable(SEARCH_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isSearchResultDisplayedByHotelName(String text) {
        WebElement label = driverTools.waitForElementToAppear(getSearchResultHotelNameByIndex(1, text)).orElseThrow();
        return driverTools.isElementDisplayed(label);
    }

    public boolean isSearchResultScoreStatusDisplayedByHotelName(String hotelName, String scoreStatus) {
        String scoreLabel = switch (scoreStatus) {
            case "GOOD" -> "Good";
            case "BAD" -> "Bad";
            case "VERY_BAD" -> "Very Bad";
            default -> "";
        };
        WebElement label = driverTools.waitForElementToAppear(getSearchResultScoreStatusByHotelName(1, hotelName)).orElseThrow();
        return label.getText().equals(scoreLabel);
    }

    public void clickReportByHotelName(String text) {
        WebElement button = driverTools.waitForElementToBeClickable(getReportButtonByHotelName(text)).orElseThrow();
        driverTools.click(button);
    }

    public boolean isAddNewReportModalDisplayed() {
        WebElement modal = driverTools.waitForElementToAppear(ADD_NEW_REPORT_MODAL).orElseThrow();
        return driverTools.isElementDisplayed(modal);
    }

    public void selectReportScoreStatusRadio(String text) {
        WebElement radio = driverTools.waitForElementToBeClickable(getReportScoreStatusRadioByName(text)).orElseThrow();
        try {
            driverTools.click(radio);
            driverTools.waitForElementToAppear(REPORT_REASON_TEXTAREA).orElseThrow();
        } catch (Exception e) {
            driverTools.click(radio);
            driverTools.waitForElementToAppear(REPORT_REASON_TEXTAREA).orElseThrow();
        }
    }

    public void enterReportScoreStatusReason(String text) {
        WebElement textarea = driverTools.waitForElementToAppear(REPORT_REASON_TEXTAREA).orElseThrow();
        driverTools.sendKeys(textarea, text);
    }

    public void submitReport() {
        WebElement button = driverTools.waitForElementToBeClickable(SUBMIT_REPORT_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void confirmSubmitReport() {
        WebElement button = driverTools.waitForElementToBeClickable(CONFIRM_SUBMIT_REPORT_BUTTON, 10).orElseThrow();
        driverTools.click(button);
    }

    public boolean isSuccessfulSnackbarAppeared() {
        WebElement snackbar = driverTools.waitForElementToAppear(SUCCESSFUL_SNACKBAR, 10).orElseThrow();
        return driverTools.isElementDisplayed(snackbar);
    }
    public void clickDownloadCsv() {
        WebElement button = driverTools.waitForElementToBeClickable(DOWNLOAD_CSV_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void selectAccommodationType(String accommodationType) {
        WebElement dropdown = driverTools.waitForElementToBeClickable(ACCOMMODATION_TYPE_DROPDOWN).orElseThrow();
        driverTools.click(dropdown);
        WebElement option = driverTools.waitForElementToBeClickable(HOTEL_TYPE).orElseThrow();
        driverTools.click(option);
    }

    public String getColumnHeader(){
        driverTools.waitForElementToAppear(COLUMN_HOTEL);
        return driverTools.getText(COLUMN_HOTEL).orElseThrow();
    }

    public String getColumnAccomType(){
        driverTools.waitForElementToAppear(COLUMN_ACCOM_TYPE);
        return driverTools.getText(COLUMN_ACCOM_TYPE).orElseThrow();
    }

    public String getColumnResultHotel(){
        driverTools.waitForElementToAppear(COLUMN_HOTEL_RESULT);
        return driverTools.getText(COLUMN_HOTEL_RESULT).orElseThrow();
    }

    public String getColumnResultAccomType(){
        driverTools.waitForElementToAppear(COLUMN_ACCOMTYPE_RESULT);
        return driverTools.getText(COLUMN_ACCOMTYPE_RESULT).orElseThrow();
    }
}
