package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AffiliateHotelLogPage <T extends WebDriver> extends WebPage<T> {

    public AffiliateHotelLogPage(Session<T> session) {
        super(session);
    }

    private static final Locator SEARCH_FIELD = new Locator("SEARCH_FIELD", By.xpath("//input[@id='search-by-partners-name-or-id']"));
    private static final Locator SEARCH_BTN = new Locator("SEARCH_BTN", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator DOWNLOAD_LOGS_BUTTON = new Locator("DOWNLOAD_LOGS_BUTTON", By.xpath("//button[normalize-space(.)='Download logs' or normalize-space(.)='Unduh log']"));
    private static final Locator CANCELLATION_CHIP = new Locator("CANCELLATION_CHIP",By.xpath("//label[.//span[normalize-space(text())='Cancellation' or normalize-space(text())='Pembatalan']]"));
    private static final Locator CUSTOM_CHIP = new Locator("CUSTOM_CHIP", By.xpath("//label[.//span[contains(text(), 'Custom') or contains(text(), 'Kustom')]]"));
    private static final Locator START_DATE_BOX = new Locator("START_DATE_BOX", By.xpath("//input[@id='start-date']"));
    private static final Locator END_DATE_BOX = new Locator("END_DATE_BOX", By.xpath("//input[@id='end-date']"));
    private static final Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", By.xpath("//button[normalize-space()='Next' or normalize-space()='Berikutnya']"));
    private static final Locator SEND_BUTTON = new Locator("SEND_BUTTON",By.xpath("//button[normalize-space()='Send' or normalize-space()='Kirim']"));
    private static final Locator SNACKBAR_TOAST = new Locator("SNACKBAR_TOAST",By.xpath("//div[contains(@class,'Snackbar_text_content')]") );
    private static final Locator PRE_BOOKING_CHIP = new Locator("PRE_BOOKING_CHIP", By.xpath("//label[.//span[normalize-space(text())='Pre-booking' or normalize-space(text())='Pra-pemesanan']]"));
    private static final Locator BOOKING_CHIP = new Locator("BOOKING_CHIP", By.xpath("//label[.//span[normalize-space(text())='Booking' or normalize-space(text())='Pemesanan']]"));
    private static final Locator RESET_SEARCH_BTN = new Locator("RESET_SEARCH_BTN", By.xpath("//button[normalize-space()='Reset Search']"));
    private static final Locator SUCESS_MESSAGE = new Locator("SUCESS_MESSAGE", By.xpath("//div[contains(@class, 'Snackbar_text_content') and contains(text(), 'Compiling the data has started successfully')]"));
    private Locator getPartnerIDLocator(String partnerID) {
        String xpathTemplate = "//p[normalize-space()='Partner Name']/following-sibling::p[2]";
        String finalXpath = String.format(xpathTemplate, partnerID);
        return new Locator("PartnerID with value: " + partnerID, By.xpath(finalXpath));
    }
    private static final Locator COMPILED_LOG_POPUP = new Locator("COMPILED_LOG_POPUP",By.xpath("//h2[normalize-space(text())='Send compiled logs to your email?']"));
    private static final Locator START_DATE_POPUP = new Locator("START_DATE_POPUP", By.xpath("//h2[normalize-space(text())='Select Date']"));
    private static final Locator START_TIME_BOX = new Locator("START_TIME_BOX", By.xpath("//input[@id='start-time' and @placeholder='Start Time']"));
    private static final Locator START_TIME_POPUP = new Locator("START_TIME_POPUP", By.xpath("//h2[normalize-space(text())='Start Time']"));
    private static final Locator EMAIL_FIELD = new Locator("EMAIL_FIELD", By.xpath("//input[@id='email' and @type='text']"));
    private static final Locator EVENT_CONFIRMATION = new Locator("EVENT_CONFIRMATION", By.xpath("//span[contains(@class, 'Text_text') and normalize-space(text())='Booking']"));
    public static final Locator ESTIMATED_TIME_DATE = new Locator("ESTIMATED_TIME_DATE", By.xpath("//span[normalize-space(text())='Estimated sent to your email:']/following-sibling::span[1]"));

    private String startDate;
    private String endDate;

    public void goToAffiliateHotelLogPage(){
        if (Configs.ENVIRONMENT == Environment.GK){
            driver.get("https://gatotkaca.tiket.com/admin/affiliate/hotel/log");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD){
            driver.get("https://preprod.tiket.com/admin/affiliate/hotel/log");
        } else if (Configs.ENVIRONMENT == Environment.PROD){
            driver.get("https://tiket.com/admin/affiliate/hotel/log");
        }
    }

    public void searchPartnerHotelBizlogByPartnerID(String partnerID){
        driverTools.waitForElementToAppear(SEARCH_FIELD,10);
        driverTools.click(SEARCH_FIELD);
        driverTools.sendKeys(SEARCH_FIELD, partnerID);
    }

    public void clickSearchFilterButton(){
        driverTools.waitForElementToAppear(SEARCH_BTN,5);
        driverTools.click(SEARCH_BTN);
    }

    public String getPartnerIDResult(String partnerID) {
        Locator firstOrderData = getPartnerIDLocator(partnerID);
        driverTools.waitForElementToAppear(firstOrderData, 4).orElseThrow();
        return driverTools.getText(firstOrderData).orElseThrow();
    }

    public void clickDownloadLogsButton() {
        driverTools.waitForElementToAppear(DOWNLOAD_LOGS_BUTTON, 10);
        driverTools.click(DOWNLOAD_LOGS_BUTTON);
    }

    public void selectCancellationChip() {
        driverTools.waitForElementToAppear(CANCELLATION_CHIP, 10);
        driverTools.waitForElementToBeClickable(CANCELLATION_CHIP);
        driverTools.click(CANCELLATION_CHIP);
    }

    public void selectCustomTimeFrame() {
        driverTools.waitForElementToAppear(CUSTOM_CHIP, 10);
        driverTools.waitForElementToBeClickable(CUSTOM_CHIP);
        driverTools.click(CUSTOM_CHIP);
    }

    public void setStartDate() {
        driverTools.waitForElementToAppear(START_DATE_BOX);
        driverTools.click(START_DATE_BOX);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -3);
        startDate = new SimpleDateFormat("d MMMM yyyy EEEE", Locale.ENGLISH).format(calendar.getTime());
        Locator START_DATE = new Locator("START_DATE", By.xpath("//span[@aria-label='" + startDate + "']"));
        driverTools.waitForElementToAppear(START_DATE, 10);
        driverTools.click(START_DATE);

    }

    public void setEndDate() {
        driverTools.waitForElementToAppear(END_DATE_BOX);
        driverTools.click(END_DATE_BOX);

        Calendar calendar = Calendar.getInstance();
        endDate = new SimpleDateFormat("d MMMM yyyy EEEE").format(calendar.getTime());
        Locator END_DATE = new Locator("END_DATE", By.xpath("//span[@aria-label='" + endDate + "']"));
        driverTools.waitForElementToAppear(END_DATE, 10);
        driverTools.click(END_DATE);
    }

    public boolean isStartStrictlyBeforeEnd() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy EEEE");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            if (!start.before(end)) {
                logger.log("Start date can't be the same or after the end date.");
                return false;
            }

            long daysBetween = (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
            long MAX_DAYS_DIFFERENCE= 3;
            if (daysBetween > MAX_DAYS_DIFFERENCE) {
                logger.log("Start date must be within 3 days before the end date.");
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickNextButton() {
        driverTools.waitForElementToAppear(NEXT_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(NEXT_BUTTON);
    }

    public void clickSendButton() {
        driverTools.waitForElementToAppear(SEND_BUTTON, 10);
        driverTools.click(SEND_BUTTON);
    }
    public boolean isCompileSnackbarVisible() {
        driverTools.waitForElementToAppear(SNACKBAR_TOAST, 10);
        return driverTools.isElementPresent(SNACKBAR_TOAST);
    }

    public void selectPreBookingChip(){
        driverTools.waitForElementToBeClickable(PRE_BOOKING_CHIP);
        driverTools.click(PRE_BOOKING_CHIP);
    }

    public void selectBookingChip(){
        driverTools.waitForElementToAppear(BOOKING_CHIP,5);
        driverTools.waitForElementToBeClickable(BOOKING_CHIP);
        driverTools.click(BOOKING_CHIP);
    }

    public void enterSearchField(String searchFiled){
        driverTools.waitForElementToAppear(SEARCH_FIELD, 10);
        driverTools.sendKeys(SEARCH_FIELD, searchFiled);
        driverTools.click(SEARCH_FIELD);
    }

    public void clickSearchButton() {
        driverTools.waitForElementToAppear(SEARCH_BTN, 10);
        driverTools.click(SEARCH_BTN);
    }

    public void clickResetSearchButton() {
        driverTools.waitForElementToAppear(RESET_SEARCH_BTN, 10);
        driverTools.click(RESET_SEARCH_BTN);
    }

    public boolean isSearchFieldEmpty() {
        boolean isSearchFieldEmpty = driverTools.getAttribute(SEARCH_FIELD.by(), "value").trim().isEmpty();
        return isSearchFieldEmpty;
    }

    public boolean isCompiledLogPopupVisible() {
        driverTools.waitForElementToAppear(COMPILED_LOG_POPUP, 10);
        return driverTools.isElementDisplayed(COMPILED_LOG_POPUP);
    }

    public void clickStartDateBox() {
        driverTools.waitForElementToAppear(START_DATE_BOX);
        driverTools.click(START_DATE_BOX);
    }

    public boolean isStartDateCalendarPopupVisible() {
        driverTools.waitForElementToAppear(START_DATE_POPUP, 10);
        return driverTools.isElementDisplayed(START_DATE_POPUP);
    }

    public void clickStartTimeBox(){
        driverTools.waitForElementToAppear(START_TIME_BOX, 10);
        driverTools.click(START_TIME_BOX);
    }

    public boolean isStartTimePOPupVisible() {
        driverTools.waitForElementToAppear(START_TIME_POPUP, 10);
        return driverTools.isElementDisplayed(START_TIME_POPUP);
    }

    public boolean isCompiledInfoMatches(String username){
        driverTools.waitForElementToAppear(EVENT_CONFIRMATION, 10);
        driverTools.waitForElementToAppear(EMAIL_FIELD,10);

        String eventText = driverTools.getText(EVENT_CONFIRMATION).orElse("");
        String emailValue = driverTools.getAttribute(EMAIL_FIELD.by(), "value");

        return eventText.contains("Booking") && emailValue.contains(username);
    }

    public boolean isDateTimeMatch() {
        driverTools.waitForElementToAppear(ESTIMATED_TIME_DATE, 10);

        String estimatedDateTime = driverTools.getText(ESTIMATED_TIME_DATE).orElse("").trim();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy; HH:mm", Locale.ENGLISH);
        LocalDateTime displayedDateTime;
        try {
            displayedDateTime = LocalDateTime.parse(estimatedDateTime, formatter);
        } catch (Exception e) {
            logger.log("Failed to parse displayed estimated date/time: " + estimatedDateTime);
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        boolean isDateToday = displayedDateTime.toLocalDate().equals(now.toLocalDate());
        long diffMinutes = Math.abs(Duration.between(displayedDateTime, now).toMinutes());
        boolean isTimeWithin20Min = diffMinutes <= 20;

        return isDateToday && isTimeWithin20Min;
    }

    public boolean isSuccessMessageVisible() {
        driverTools.waitForElementToAppear(SUCESS_MESSAGE, 10);
        return driverTools.isElementDisplayed(SUCESS_MESSAGE);
    }

    public boolean isResultAppeared(String partnerID) {
        Locator firstOrderData = getPartnerIDLocator(partnerID);
        return driverTools.isElementPresent(firstOrderData);
    }
}