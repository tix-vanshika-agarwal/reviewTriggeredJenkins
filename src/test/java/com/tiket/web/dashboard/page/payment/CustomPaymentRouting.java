package com.tiket.web.dashboard.page.payment;

import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.*;

import java.util.List;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;

public class CustomPaymentRouting<T extends WebDriver> extends BasePage<T> {
    public CustomPaymentRouting(Session<T> session) { super(session); }

    private static final String paymentPath = "/admin/payment_v2";
    private static final Locator LOADING = new Locator("LOADING", By.xpath("//*[@data-testid='loading']"));
    private static final Locator TITLE_CUSTOM_PAYMENT_ROUTING = new Locator("TITLE_CUSTOM_PAYMENT_ROUTING", By.xpath("//h1[contains(text(),'Custom Payment Routing')]"));
    private static final Locator CARD_CONFIG_SIDEMENU = new Locator("CARD_CONFIG_SIDEMENU", By.xpath("//*[@data-testid='sidemenu']//*[contains(text(),'Card Payment Configuration')]"));
    private static final Locator CUSTOM_PAYMENT_ROUTING_SUBMENU = new Locator("CUSTOM_PAYMENT_ROUTING_SUBMENU", By.xpath("//*[contains(@data-testid,'submenu')]//*[contains(text(),'Custom Payment Routing')]"));
    private String rowStatusXPath = "//td[@data-testid='row-cell-%d-status']";
    private String rowActionDropdownXPath = "//td[@data-testid='row-cell-%d-action']//div[@data-testid='dropdown-btn-default']";
    private static final Locator EDIT_DROPDOWN_OPTION = new Locator("EDIT_DROPDOWN_OPTION", By.xpath("//div[@data-testid='dropdown-item-Edit']"));
    private static final Locator EDIT_PAGE_HEADER = new Locator("EDIT_PAGE_HEADER", By.xpath("//h1[text()='Edit Custom Payment Routing']"));
    private static final Locator ACTIVATE_RULE_TOGGLE_CHECKBOX = new Locator("ACTIVATE_RULE_TOGGLE_CHECKBOX", By.xpath("//span[text()='Activate Rule']/following-sibling::div//input[@type='checkbox']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[@type='submit' and text()='Save']"));
    private static final Locator CONFIRM_SAVE_CHANGES_BUTTON = new Locator("CONFIRM_SAVE_CHANGES_BUTTON", By.xpath("//footer[contains(@class, 'modal_footer')]//button[normalize-space(text())='Save Changes']"));
    private static final Locator TABLE_ROWS = new Locator("TABLE_ROWS", By.xpath("//tbody/tr[contains(@data-testid, 'row-key-')]"));
    public boolean isDefaultCardRoutingPageOpened() {
        if (!driverTools.waitForElementToDisappear(LOADING)) {
            logger.log("Element not disappeared or even not appeared");
        }
        return driverTools.waitForElementToPresence(TITLE_CUSTOM_PAYMENT_ROUTING, 3).isPresent();
    }

    public void openCustomPaymentRoutingPage(int waitSec) {
        if (!isDefaultCardRoutingPageOpened()) {
            Deeplink deeplink = new Deeplink("ADMIN_CARE", "https://gatotkaca.tiket.com", "https://preprod.tiket.com", "https://tiket.com");
            String url = switch (Configs.ENVIRONMENT) {
                case PROD -> deeplink.prodDeeplink()+paymentPath;
                case PREPROD -> deeplink.preprodDeeplink()+paymentPath;
                case GK -> deeplink.gkDeeplink()+paymentPath;
                default -> deeplink.gkDeeplink()+paymentPath;
            };
            driverTools.get(url);
            if (!driverTools.getCurrentURL().contains(url)) Assertion.skip("Failed to open payment dashboard "+url);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement cardConfig = driverTools.waitForElementToPresence(CARD_CONFIG_SIDEMENU).orElseThrow();
            driverTools.clickWithJavascriptExecutor(cardConfig);
            WebElement cardRouting = driverTools.waitForElementToPresence(CUSTOM_PAYMENT_ROUTING_SUBMENU).orElseThrow();
            driverTools.clickWithJavascriptExecutor(cardRouting);
            driverTools.waitForElementToPresence(TITLE_CUSTOM_PAYMENT_ROUTING, 3);
        }
    }

    public boolean editRuleIfActive(int rowIndex) {
        Locator statusLoc = new Locator("ROW_STATUS_" + rowIndex, By.xpath(String.format(rowStatusXPath, rowIndex)));
        Locator actionDropdownLoc = new Locator("ROW_ACTION_" + rowIndex, By.xpath(String.format(rowActionDropdownXPath, rowIndex)));

        String currentStatus = driverTools.getText(statusLoc).orElseThrow().trim();
        if (currentStatus.equalsIgnoreCase("Inactive")) {
            return false;
        }

        driverTools.waitForElementToBeClickable(actionDropdownLoc, 2).orElseThrow();
        driverTools.clickWithJavascriptExecutor(actionDropdownLoc);

        driverTools.waitForElementToBeClickable(EDIT_DROPDOWN_OPTION, 2).orElseThrow();
        driverTools.click(EDIT_DROPDOWN_OPTION);

        return true;
    }

    public void turnOffActivateRuleToggle() {
        driverTools.waitForElementToAppear(EDIT_PAGE_HEADER, 2).orElseThrow(() -> new NoSuchElementException("Edit Custom Payment Routing page did not load."));
        driverTools.clickWithJavascriptExecutor(ACTIVATE_RULE_TOGGLE_CHECKBOX);

    }

    public void clickSaveAndConfirm() {
        driverTools.waitForElementToBeClickable(SAVE_BUTTON, 5).orElseThrow();
        driverTools.click(SAVE_BUTTON);

        driverTools.waitForElementToBeClickable(CONFIRM_SAVE_CHANGES_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("Confirmation modal 'Save Changes' button not found."));
        driverTools.click(CONFIRM_SAVE_CHANGES_BUTTON);
    }

    public void deactivateAllActiveRules() {
        driverTools.waitForElementsToAppear(TABLE_ROWS, 2).orElseThrow(
                () -> new NoSuchElementException("Table did not load.")
        );

        int totalRows = driverTools.elements(TABLE_ROWS).orElse(java.util.Collections.emptyList()).size();
        for (int i = 0; i < totalRows; i++) {
            driverTools.waitForElementsToAppear(TABLE_ROWS, 2);
            boolean didNavigateToEdit = editRuleIfActive(i);
            if (didNavigateToEdit) {
                turnOffActivateRuleToggle();
                clickSaveAndConfirm();
                int currentRow = i;
                driverTools.waitForElementsToAppear(TABLE_ROWS, 1).orElseThrow(() -> new NoSuchElementException("Table did not reload after saving row " + (currentRow+1)));
                Locator updatedStatusLoc = new Locator("UPDATED_ROW_STATUS_" + currentRow, By.xpath(String.format(rowStatusXPath, currentRow)));
                String updatedStatus = driverTools.getText(updatedStatusLoc).orElse("").trim();
                Assertion.assertThat("Verify the rule status updated to Inactive for row " + (currentRow+1), updatedStatus, is(equalToIgnoringCase("Inactive")));
            }
        }
    }

    public void verifyAllRulesAreInactive() {
        int totalRows = driverTools.elements(TABLE_ROWS).orElse(java.util.Collections.emptyList()).size();
        for (int rowIndex = 0; rowIndex < totalRows; rowIndex++) {
            Locator statusLoc = new Locator("ROW_STATUS_" + rowIndex, By.xpath(String.format(rowStatusXPath, rowIndex)));
            String currentStatus = driverTools.getText(statusLoc).orElseThrow().trim();
            Assertion.assertThat("Verify final status for row " + (rowIndex+1) + " is Inactive", currentStatus, is(equalToIgnoringCase("Inactive")));
        }
    }
}
