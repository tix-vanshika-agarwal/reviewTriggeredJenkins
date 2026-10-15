package com.tiket.web.dashboard.page.affiliate;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateTTDBusinessLog<T extends WebDriver> extends WebPage<T> {
    public AffiliateTTDBusinessLog(Session<T> session) {
        super(session);
    }

    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[text()='Search' and contains(@class, 'Button_variant_secondary')]"));
    private static final Locator SEARCH_BY_PARTNER_INPUT = new Locator("SEARCH_BY_PARTNER_INPUT", By.xpath("//input[@placeholder='Search by partner’s name or ID']"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[contains(text(),'Reset')]"));
    private static final Locator DOWNLOAD_LOGS_BUTTON = new Locator("DOWNLOAD_LOGS_BUTTON", By.xpath("(//button[contains(text(),'Download logs')])[1]"));
    private static final Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", By.xpath("//button[contains(text(),'Next')]"));
    private static final Locator SEND_BUTTON = new Locator("SEND_BUTTON", By.xpath("//button[contains(text(),'Send')]"));
    private static final Locator FIRST_DATA_ROW = new Locator("FIRST_DATA_ROW", By.xpath("//div[contains(@class, 'w-full') and contains(@class, 'justify-between') and contains(@class, 'items-center')]"));
    private static final Locator START_TIME_INPUT = new Locator("START_TIME_INPUT", By.xpath("//input[@id='start-time' and @placeholder='Start Time']"));
    private static final Locator START_TIME_POPUP_TITLE = new Locator("START_TIME_POPUP_TITLE", By.xpath("//h2[text()='Start Time']"));
    private static final Locator START_DATE_INPUT = new Locator("START_DATE_INPUT", By.xpath("//input[@id='start-date' and @placeholder='Start Date']"));
    private static final Locator CALENDAR_CONTAINER = new Locator("CALENDAR_CONTAINER", By.xpath("//div[contains(@class,'CalendarDesktop_calendar')]"));
    private static final Locator COMPILED_LOGS_MODAL_HEADING = new Locator("COMPILED_LOGS_MODAL_HEADING", By.xpath("//h2[text()='Send compiled logs to your email?']"));
    private static final Locator SENT_DATETIME_LABEL = new Locator("SENT_DATETIME_LABEL", By.xpath("//span[contains(@class, 'Text_weight_bold') and contains(text(), ':')]"));
    private static final Locator PARTNER_NAME_INPUT = new Locator("PARTNER_NAME_INPUT", By.id("partner-name"));
    private static final Locator MODIFIED_PRODUCTS_RADIO = new Locator("MODIFIED_PRODUCTS_RADIO", By.xpath("//span[contains(@class,'Radio_children') and text()='Modified Products']"));
    private static final Locator END_DATE_INPUT = new Locator("END_DATE_INPUT",By.xpath("//input[@id='end-date' and @placeholder='End Date']"));
    private static final Locator END_TIME_INPUT = new Locator("END_TIME_INPUT", By.xpath("//input[@id='end-time' and @placeholder='End Time']"));
    private static final Locator COMPILED_PARTNER_NAME = new Locator("COMPILED_PARTNER_NAME", By.xpath("(//div[contains(@class,'flex-col')]//span[contains(@class,'Text_text__DSnue')])[1]"));
    private static final Locator COMPILED_EVENT_VALUE = new Locator("COMPILED_EVENT_VALUE", By.xpath("(//div[contains(@class,'flex-col')]//span[contains(@class,'Text_text__DSnue')])[2]"));
    private static final Locator COMPILED_START_TIME = new Locator("COMPILED_START_TIME", By.xpath("(//div[contains(@class,'grid') and contains(@class,'gap-2')]//span[contains(@class,'Text_text__DSnue')])[1]"));
    private static final Locator COMPILED_END_TIME = new Locator("COMPILED_END_TIME", By.xpath("(//div[contains(@class,'grid') and contains(@class,'gap-2')]//span[contains(@class,'Text_text__DSnue')])[2]"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));

    public void searchByPartnerInput(String partnerInput){
        driverTools.waitForElementToAppear(SEARCH_BY_PARTNER_INPUT, 4).orElseThrow();
        driverTools.click(SEARCH_BY_PARTNER_INPUT);
        driverTools.sendKeys(SEARCH_BY_PARTNER_INPUT, partnerInput);
    }

    public void clickSearchButton(){
        driverTools.click(SEARCH_BUTTON);
    }

    public void clickResetButton(){
        driverTools.click(RESET_FILTER_BUTTON);
    }

    public void clickDownloadLogsButton(){
        driverTools.waitForElementToAppear(DOWNLOAD_LOGS_BUTTON, 4).orElseThrow();
        driverTools.click(DOWNLOAD_LOGS_BUTTON);
    }

    public void clickNextButton(){
        driverTools.waitForElementToAppear(NEXT_BUTTON, 4);
        driverTools.scrollToElement(NEXT_BUTTON);
        driverTools.click(NEXT_BUTTON);
    }

    public void clickSendButton(){
        driverTools.waitForElementToAppear(SEND_BUTTON, 4);
        driverTools.scrollToElement(SEND_BUTTON);
        driverTools.click(SEND_BUTTON);
    }

    public void clickStartTimeInput() {
        driverTools.waitForElementToAppear(START_TIME_INPUT, 4).orElseThrow();
        driverTools.click(START_TIME_INPUT);
    }

    public void clickStartDateInput() {
        driverTools.waitForElementToAppear(START_DATE_INPUT, 4).orElseThrow();
        driverTools.click(START_DATE_INPUT);
    }

    public boolean isStartTimePopupDisplayed() {
        driverTools.waitForElementToAppear(START_TIME_POPUP_TITLE, 4).orElseThrow();
        return driverTools.isElementPresent(START_TIME_POPUP_TITLE);
    }

    public boolean isCalendarContainerDisplayed() {
        driverTools.waitForElementToAppear(CALENDAR_CONTAINER, 4).orElseThrow();
        return driverTools.isElementPresent(CALENDAR_CONTAINER);
    }

    public boolean checkTTDBusinessLogSearch(String searchValue){
        driverTools.waitForElementToAppear(FIRST_DATA_ROW, 4).orElseThrow();
        String searchData = driverTools.getText(FIRST_DATA_ROW).orElseThrow();
        logger.log("Search Data : " + searchData);
        return searchData.contains(searchValue);
    }

    public String searchTextBoxIsEmpty(){
        return driverTools.getText(SEARCH_BY_PARTNER_INPUT).orElseThrow();
    }

    public boolean isCompiledLogsModalHeadingDisplayed() {
        driverTools.waitForElementToAppear(COMPILED_LOGS_MODAL_HEADING, 4).orElseThrow();
        return driverTools.isElementPresent(COMPILED_LOGS_MODAL_HEADING);
    }

    public boolean autoSetCompiledLogsData(){
        driverTools.waitForElementToAppear(PARTNER_NAME_INPUT, 4);
        driverTools.scrollToElement(PARTNER_NAME_INPUT);
        String partnerName = driverTools.getText(PARTNER_NAME_INPUT).orElseThrow();

        driverTools.waitForElementToAppear(START_DATE_INPUT, 4);
        driverTools.scrollToElement(START_DATE_INPUT);
        String startDate = driverTools.getText(START_DATE_INPUT).orElseThrow();
        driverTools.waitForElementToAppear(START_TIME_INPUT, 4).orElseThrow();
        String startTime = driverTools.getText(START_TIME_INPUT).orElseThrow();

        driverTools.waitForElementToAppear(END_DATE_INPUT, 4);
        driverTools.scrollToElement(END_DATE_INPUT);
        String endDate = driverTools.getText(END_DATE_INPUT).orElseThrow();
        driverTools.waitForElementToAppear(END_TIME_INPUT, 4).orElseThrow();
        String endTime = driverTools.getText(END_TIME_INPUT).orElseThrow();

        driverTools.waitForElementToAppear(MODIFIED_PRODUCTS_RADIO, 4);
        driverTools.scrollToElement(MODIFIED_PRODUCTS_RADIO);
        String eventType = driverTools.getText(MODIFIED_PRODUCTS_RADIO).orElseThrow();
        clickNextButton();
        return validateCompiledLogsData(partnerName, startDate, startTime, endDate, endTime, eventType);
    }

    public boolean validateCompiledLogsData(String expectedPartnerName, String expectedStartDate, String expectedStartTime, String expectedEndDate, String expectedEndTime, String expectedEventType) {
        driverTools.waitForElementToAppear(COMPILED_PARTNER_NAME, 4);
        driverTools.scrollToElement(COMPILED_PARTNER_NAME);
        String partnerName = driverTools.getText(COMPILED_PARTNER_NAME).orElseThrow();
        logger.log("Partner Name : " + partnerName);

        driverTools.waitForElementToAppear(COMPILED_START_TIME, 4).orElseThrow();
        String startTime = driverTools.getText(COMPILED_START_TIME).orElseThrow();
        logger.log("Start Time : " + startTime);

        driverTools.waitForElementToAppear(COMPILED_END_TIME, 4).orElseThrow();
        String endTime = driverTools.getText(COMPILED_END_TIME).orElseThrow();
        logger.log("End Time : " + endTime);

        driverTools.waitForElementToAppear(COMPILED_EVENT_VALUE, 4).orElseThrow();
        String eventType = driverTools.getText(COMPILED_EVENT_VALUE).orElseThrow();
        logger.log("Event Type : " + eventType);

        return partnerName.contains(expectedPartnerName) && startTime.contains(expectedStartDate) && startTime.contains(expectedStartTime)
                && endTime.contains(expectedEndDate) && endTime.contains(expectedEndTime) && eventType.contains(expectedEventType);
    }

    public boolean isSentDateToday() {
        driverTools.waitForElementToAppear(SENT_DATETIME_LABEL, 4).orElseThrow();
        String sentText = driverTools.getText(SENT_DATETIME_LABEL).orElseThrow();

        // Extract only the date part before the semicolon or space-time separator
        String sentDateStr = sentText.split("[;,]")[1].trim();
        logger.log("Sent Date : " + sentDateStr);
        String currentDateStr = CoreFunctions.getCurrentDate("dd MMM yyyy");
        return sentDateStr.equals(currentDateStr);
    }

    public boolean checkTTDBusinessLogSnackbar(String expectedSnackbarText){
        driverTools.waitForElementToAppear(SNACKBAR, 2).orElseThrow();
        String snackbarText = driverTools.getText(SNACKBAR).orElseThrow();
        logger.log("Snackbar Text : " + snackbarText);
        return driverTools.isElementPresent(SNACKBAR) && snackbarText.contains(expectedSnackbarText);
    }
}

