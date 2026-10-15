package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.b2b.corporate.OrderDetailResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;

public class AffiliateFlightBusinessLog<T extends WebDriver> extends BasePage<T> {
    public AffiliateFlightBusinessLog(Session<T> session) {
        super(session);
    }

    private static final Locator SEARCH_FIELD = new Locator("SEARCH_FIELD", By.xpath("//input[@id=\"search-by-partners-name-or-id\"]"));
    private static final Locator SEARCH_BTN = new Locator("SEARCH_BTN", By.xpath("//button[text()=\"Search\"]"));
    private static final Locator DOWNLOAD_BTN = new Locator("DOWNLOAD_BTN", By.xpath("//button[normalize-space()='Download logs']"));
    private static final Locator TIMEFRAME_RADIO_BTN = new Locator("TIMEFRAME_RADIO_BTN", By.xpath("//span[normalize-space()='Last 6 hours']"));
    private static final Locator START_DATE_BTN = new Locator("START_DATE_BTN", By.xpath("//div[@class=\"styles_container__xzZrc w-3/5\"]"));
    private static final Locator COMPILE_NEXT_BTN = new Locator("COMPILE_NEXT_BTN", By.xpath("//button[normalize-space()='Next']"));
    private static final Locator CALENDAR_POP_UP = new Locator("CALENDAR_POP_UP", By.xpath("//section[@class=\"Modal_modal_popup__oFyfk Modal_modal_popup_large__jY60X Modal_modal_popup_width_auto__32vvC\"]"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));
    private static final Locator SEND_BTN = new Locator("SEND_BTN", By.xpath("//button[@class=\"Button_button__20aRx Button_full_width__ltMfH Button_horizontal_padding__7QPqY Button_variant_primary__PFcuM\"]"));
    private static final Locator COMPILE_CONFIRMATION_TEXT = new Locator("COMPILE_CONFIRMATION_TEXT", By.xpath("//h2[normalize-space()='Send compiled logs to your email?']"));
    private static final Locator COMPILE_CONFIRMATION_POP_UP = new Locator("COMPILE_CONFIRMATION_POP_UP", By.xpath("//section[@class=\"Modal_modal_popup__oFyfk Modal_modal_popup_small__5tnZS Modal_modal_popup_width_default__GfN4T\"]"));
    private Locator getFlightBizLogLocator(String partnerName) {
        String xpathTemplate = "//p[text()='Partner Name']/following-sibling::p[1]";
        String finalXpath = String.format(xpathTemplate, partnerName);
        return new Locator("Value for partner: " + partnerName, By.xpath(finalXpath));
    }
    private Locator getFlightPartnerIDLocator(String partnerID) {
        String xpathTemplate = "//p[normalize-space()='Partner Name']/following-sibling::p[2]";
        String finalXpath = String.format(xpathTemplate, partnerID);
        return new Locator("PartnerID with value: " + partnerID, By.xpath(finalXpath));
    }

    public void goToAffiliatePage() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/affiliate/flight/logs");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/affiliate/flight/logs");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://tiket.com/admin/affiliate/flight/logs");
        }
    }

    public void searchByPartnerName(String partnerName){
        driverTools.waitForElementToAppear(SEARCH_FIELD,5);
        driverTools.waitForElementToBeClickable(SEARCH_FIELD,5);
        driverTools.click(SEARCH_FIELD);
        driverTools.sendKeys(SEARCH_FIELD,partnerName);
        driverTools.click(SEARCH_BTN);
    }

    public void searchByPartnerID(String partnerID){
        driverTools.waitForElementToAppear(SEARCH_FIELD,5);
        driverTools.waitForElementToBeClickable(SEARCH_FIELD,5);
        driverTools.click(SEARCH_FIELD);
        driverTools.sendKeys(SEARCH_FIELD,partnerID);
        driverTools.click(SEARCH_BTN);
    }

    public void clickDownloadButton(){
        driverTools.waitForElementToBeClickable(DOWNLOAD_BTN,3);
        driverTools.click(DOWNLOAD_BTN);
    }

    public void clickStartDateButton(){
        driverTools.waitForElementToAppear(START_DATE_BTN, 3);
        driverTools.click(START_DATE_BTN);
    }

    public void selectLogType(String orderStatus) {
        Locator orderStatusRadioButton = orderStatusLocator(orderStatus);
        driverTools.waitForElementToAppear(orderStatusRadioButton);

        boolean isDisplayed = driverTools.isElementDisplayed(orderStatusRadioButton);
        Assertion.assertThat("Is order status { " + orderStatus + " } displayed", isDisplayed, is(true));

        driverTools.click(orderStatusRadioButton);
    }

    public void selectTimeframe(String timeframe) {
        Locator timeframeRadioButton = timeframeLocator(timeframe);
        driverTools.waitForElementToAppear(timeframeRadioButton);

        boolean isDisplayed = driverTools.isElementDisplayed(timeframeRadioButton);
        Assertion.assertThat("Is timeframe { " + timeframe + " } displayed", isDisplayed, is(true));

        driverTools.click(timeframeRadioButton);
    }

    private Locator timeframeLocator(String timeframeText) {
        String xpath = "//label[normalize-space()='" + timeframeText + "']";
        return new Locator("TIMEFRAME_" + timeframeText.toUpperCase().replace(" ", "_"), new By.ByXPath(xpath));
    }

    private Locator orderStatusLocator(String orderStatusText) {
        String xpath = "//label[normalize-space()='" + orderStatusText + "']";
        return new Locator("ORDER_STATUS_" + orderStatusText.toUpperCase().replace(" ", "_"), new By.ByXPath(xpath));
    }

    public void clickNextButton(){
        driverTools.waitForElementToBeClickable(COMPILE_NEXT_BTN,3);
        driverTools.click(COMPILE_NEXT_BTN);
    }

    public void clickSendButton() {
        driverTools.waitForElementToAppear(COMPILE_CONFIRMATION_POP_UP, 4);
        driverTools.waitForElementToBeClickable(SEND_BTN, 3);
        driverTools.click(SEND_BTN);
    }

    public String getPartnerNameResult(String partnerName) {
        Locator firstOrderData = getFlightBizLogLocator(partnerName);
        driverTools.waitForElementToAppear(firstOrderData, 4).orElseThrow();
        return driverTools.getText(firstOrderData).orElseThrow();
    }

    public String getPartnerIDResult(String partnerID) {
        Locator firstOrderData = getFlightPartnerIDLocator(partnerID);
        driverTools.waitForElementToAppear(firstOrderData, 4).orElseThrow();
        return driverTools.getText(firstOrderData).orElseThrow();
    }

    public boolean isCalendarPopUpDisplayed() {
        driverTools.waitForElementToAppear(CALENDAR_POP_UP, 5);
        return driverTools.isElementDisplayed(CALENDAR_POP_UP);
    }

    public boolean checkCompileConfirmationText(String expectedConfirmationText){
        driverTools.waitForElementToAppear(COMPILE_CONFIRMATION_TEXT, 2).orElseThrow();
        String confirmationText = driverTools.getText(COMPILE_CONFIRMATION_TEXT).orElseThrow();
        logger.log("Header Text : " + confirmationText);
        return driverTools.isElementPresent(COMPILE_CONFIRMATION_TEXT) && confirmationText.contains(expectedConfirmationText);
    }

    public boolean checkFlightBusinessLogSnackbar(String expectedSnackbarText) {
        driverTools.waitForElementToAppear(SNACKBAR, 2).orElseThrow();
        String snackbarText = driverTools.getText(SNACKBAR).orElseThrow();
        logger.log("Snackbar Text : " + snackbarText);
        return driverTools.isElementPresent(SNACKBAR) && snackbarText.contains(expectedSnackbarText);
    }
}