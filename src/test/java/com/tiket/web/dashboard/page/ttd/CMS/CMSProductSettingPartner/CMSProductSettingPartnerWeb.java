package com.tiket.web.dashboard.page.ttd.CMS.CMSProductSettingPartner;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

public class CMSProductSettingPartnerWeb<T extends WebDriver> extends WebPage<T> {
    public CMSProductSettingPartnerWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSProductSettingPartnerWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSProductSettingPartnerDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSProductSettingPartnerMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator SEARCH_PARTNER_INPUT = new Locator("SEARCH_PARTNER_INPUT", By.xpath("//input[@type='text']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[@class='tix-button-v2 vb--contained tb--primary']"));
    private static final Locator PARTNER_TABLE = new Locator("PARTNER_TABLE", By.xpath("//table"));
    private static final Locator ROWS_PARTNER_EDIT_BUTTON = new Locator("ROWS_PARTNER_EDIT_BUTTON", By.xpath("//table/tbody/tr/td[7]//span/*[1]"));

    private static final Locator PARTNER_CONFIG_SECTION = new Locator("PARTNER_CONFIG_SECTION", By.xpath("//div[@class='form-group'][3]"));
    private static final Locator EMAIL_ROWS = new Locator("EMAIL_ROWS", By.xpath("//div[@class='form-group'][3]//table/tbody/tr//button"));
    private static final Locator ACTION_BUTTON_SELECTION = new Locator("ACTION_BUTTON_SELECTION", By.xpath("//li[@role='menuitem']"));
    private static final Locator CLICK_BUTTON_ACTION = new Locator("CLICK_BUTTON_ACTION", By.xpath("(//button[@type='button'][normalize-space()='Action'])[7]"));

    private static final Locator ACTION_EDIT_BUTTON = new Locator("ACTION_EDIT_BUTTON", By.xpath("//li[.//span[normalize-space()='Edit']]"));
    private static final Locator EMAIL_SUBMIT_BUTTON = new Locator("EMAIL_SUBMIT_BUTTON", By.xpath("//button[normalize-space()='Submit Changes']"));
    private static final Locator EMAIL_SUBMIT_SUCCESS = new Locator("EMAIL_SUBMIT_SUCCESS", By.xpath("//div[text()='Successfully Updating Data']"));

    private static final Locator EMAIL_MODAL = new Locator("EMAIL_MODAL", By.xpath("//div[@class='modal-content']"));
    private static final Locator REMINDER_EMAIL_CHECKBOX = new Locator("REMINDER_EMAIL_CHECKBOX", By.xpath("//input[@type='checkbox' and @name='isPrivate']"));
    private static final Locator MODAL_CANCEL_BUTTON = new Locator("MODAL_CANCEL_BUTTON", By.xpath("//button[@class='tix-button-v2 vb--contained tb--disable']"));

    public void goToPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {driver.get("https://gatotkaca.tiket.com/admin/event-ms/to-do/product-configuration/partner");}
            case PREPROD -> {driver.get("https://preprod.tiket.com/admin/event-ms/to-do/product-configuration/partner");}
            case PROD -> {driver.get("https://www.tiket.com/admin/event-ms/to-do/product-configuration/partner");}
        }
        driverTools.waitForWebToLoad();
    }

    public void setPartnerNameFilter(String partnerName) {
        for (int i = 0; i < 3; i++) {
            WebElement e = driverTools.waitForElementToAppear(PARTNER_TABLE, 2).orElse(null);
            if (e != null) break;
            driverTools.refreshWebpage();
            driverTools.waitForWebToLoad();
        }

        if (partnerName != null && !partnerName.isEmpty()) {
            driverTools.clickElementByCoordinateDWeb(SEARCH_PARTNER_INPUT);
            driverTools.clearInput(SEARCH_PARTNER_INPUT);
            driverTools.sendKeysKeyboard(SEARCH_PARTNER_INPUT, partnerName);
            driverTools.dummyWait(1);
            driverTools.click(SEARCH_BUTTON);
        }
    }

    public void clickEditButtonFromRandomTableRow() {
        List<WebElement> rows = driverTools.waitForElementsToAppear(ROWS_PARTNER_EDIT_BUTTON).get();
        if (rows.isEmpty()) {
            throw new NoSuchElementException("There are no Product available");
        }
        int selectedRowIndex = CoreFunctions.generateRandomInt(0, rows.size()-1);
        String selectedRowTextLocator = String.format("//table/tbody/tr[%s]/td[1]/div", selectedRowIndex);
        logger.log(String.format("Selects '%s' rows", driverTools.getText(new Locator("SELECTED_PARTNER_ROW", By.xpath(selectedRowTextLocator)))));
        driverTools.click(rows.get(selectedRowIndex));
    }

    public void scrollUntilPartnerConfigSection() {
        driverTools.dummyWait(3);
        driverTools.scrollElementToCenter(PARTNER_CONFIG_SECTION);  
        scrollPartnerConfigHorizontalToEnd();
        WebElement actionButton = driverTools.waitForElementToAppear(CLICK_BUTTON_ACTION, 10).orElseThrow();
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'nearest', inline: 'end'});", actionButton);
        driverTools.dummyWait(1);
        driverTools.waitForElementToBeClickable(CLICK_BUTTON_ACTION);
        driverTools.click(CLICK_BUTTON_ACTION); 
    }

    public boolean emailSuccessPop() {
        driverTools.click(ACTION_EDIT_BUTTON);
        driverTools.scrollElementToCenter(EMAIL_SUBMIT_BUTTON);
        driverTools.click(EMAIL_SUBMIT_BUTTON);
        driverTools.waitForElementToAppear(EMAIL_SUBMIT_SUCCESS, 3);
        return driverTools.isElementDisplayed(EMAIL_SUBMIT_SUCCESS);
    }

    /** Max horizontal scroll inside {@link #PARTNER_CONFIG_SECTION} so trailing columns (e.g. Action) are visible. */
    private void scrollPartnerConfigHorizontalToEnd() {
        WebElement section = driverTools.waitForElementToAppear(PARTNER_CONFIG_SECTION, 10).orElseThrow();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "var root = arguments[0];"
                        + "var nodes = root.getElementsByTagName('*');"
                        + "for (var i = 0; i < nodes.length; i++) {"
                        + "  var el = nodes[i];"
                        + "  if (el.scrollWidth > el.clientWidth) {"
                        + "    el.scrollLeft = el.scrollWidth;"
                        + "  }"
                        + "}",
                section);
    }

    public boolean verifyIfThereIsOneEmailAsSettlementEmail(){
        List<WebElement> rows = driverTools.waitForElementsToAppear(EMAIL_ROWS).get();
        for (WebElement row : rows) {
            driverTools.click(row);
            driverTools.click(driverTools.waitForElementsToAppear(ACTION_BUTTON_SELECTION).get().get(0));
            driverTools.waitForElementToAppear(EMAIL_MODAL, 3);

            List<WebElement> checkboxes = driverTools.waitForElementsToAppear(REMINDER_EMAIL_CHECKBOX).get();
            if (driverTools.isElementSelected(checkboxes.get(1))) {
                return true;
            }
            driverTools.click(MODAL_CANCEL_BUTTON);
        }
        return false;
    }
}
