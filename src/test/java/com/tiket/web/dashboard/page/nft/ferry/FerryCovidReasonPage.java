package com.tiket.web.dashboard.page.nft.ferry;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.*;

import java.util.List;
import java.util.NoSuchElementException;

public class FerryCovidReasonPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator SEARCH_FIELD = new Locator("SEARCH_FIELD", new By.ByXPath("//input[@id='sb-text-input']"));
    private static final Locator FERRY_REASON_CODE_CELL = new Locator("FERRY_REASON_CODE_CELL", new By.ByXPath("//td[@class='rc-table-cell'][2]"));
    private static final Locator FERRY_REASON_VENDOR_CELL = new Locator("FERRY_REASON_VENDOR_CELL", new By.ByXPath("//td[@class='rc-table-cell'][3]"));
    private static final Locator FERRY_REASON_ID_CELL = new Locator("FERRY_REASON_ID_CELL", new By.ByXPath("//td[@class='rc-table-cell'][4]"));
    private static final Locator FERRY_REASON_EN_CELL = new Locator("FERRY_REASON_EN_CELL", new By.ByXPath("//td[@class='rc-table-cell'][5]"));
    private static final Locator ACTIVE_SWITCH = new Locator("ACTIVE_SWITCH", new By.ByXPath("(//div[@class='n8SXEW_toggle n8SXEW_toggle_small'])[1]"));
    private static final Locator ACCEPT_BUTTON = new Locator("ACCEPT_BUTTON", new By.ByXPath("//button[@class='KV-nSG_button KV-nSG_full_width KV-nSG_horizontal_padding KV-nSG_variant_primary']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", new By.ByXPath("//div[@class='_2m8F2q_notification_banner _2m8F2q_variant_success']"));
    private static final Locator ID_DESCRIPTION = new Locator("ID_DESCRIPTION", new By.ByXPath("//textarea[@name='descriptionId']"));
    private static final Locator EN_DESCRIPTION = new Locator("EN_DESCRIPTION", new By.ByXPath("//textarea[@name='descriptionEn']"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", new By.ByXPath("(//span[@class='HcPVsG_text HcPVsG_variant_activity'][normalize-space()='Edit'])[1]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//button[contains(text(), 'Save')]"));

    public FerryCovidReasonPage(Session<T> session) {
        super(session);
    }

    public void goToFerryPage(String username, String password) {
        driverTools.maximizeWindow();

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/member");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/member");
        }

        if (!isFerryLoggedIn()) {
            FerryLoginPage<?> ferryLoginPage = new FerryLoginPage<>(session);
            ferryLoginPage.loginToFerryPage(username, password);
            driverTools.waitForElementToAppear(USERNAME_GREETING, 2);

            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get("https://gatotkaca.tiket.com/admin/ferry");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driverTools.get("https://preprod.tiket.com/admin/ferry");
            }
        }
    }

    public boolean isFerryLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 2);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public void goToCovidReasonTab() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/ferry/covid-reason");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/ferry/covid-reason");
        }
    }

    public boolean verifyTheSRPIsNotEmpty() {
        List<WebElement> classLabels = driverTools.waitForElementsToAppear(FERRY_REASON_CODE_CELL, 3).orElse(null);
        return classLabels != null && !classLabels.isEmpty();
    }

    public boolean verifySRPContainsCode(String code) {
        return verifySRPContainsText(FERRY_REASON_CODE_CELL, code);
    }

    public boolean verifySRPContainsVendorReason(String reason) {
        return verifySRPContainsText(FERRY_REASON_VENDOR_CELL, reason);
    }

    public boolean verifySRPContainsReasonId(String idReason) {
        return verifySRPContainsText(FERRY_REASON_ID_CELL, idReason);
    }

    public boolean verifySRPContainsReasonEn(String enReason) {
        return verifySRPContainsText(FERRY_REASON_EN_CELL, enReason);
    }

    private boolean verifySRPContainsText(Locator cellLocator, String text) {
        List<WebElement> elements = driverTools.elements(cellLocator).get();
        return elements.stream().allMatch(element -> element.getText().contains(text));
    }

    public void clearSearchField() {
        WebElement searchField = driverTools.element(SEARCH_FIELD).get();
        driverTools.clearWithJsExecutor(searchField);
    }


    public void searchFerryCovidReason(String text) {
        int maxRetries = 5;
        searchFerryCovidReasonWithRetries(text, maxRetries);
    }

    private void searchFerryCovidReasonWithRetries(String text, int retriesLeft) {
        if (retriesLeft == 0) {
            throw new RuntimeException("Exceeded maximum retries for searchFerryCovidReason.");
        }

        clearSearchField();
        WebElement searchInput = driverTools.waitForElementToAppear(SEARCH_FIELD).get();
        try {
            searchInput.sendKeys(text);
            searchInput.sendKeys(Keys.RETURN);
        } catch (org.openqa.selenium.StaleElementReferenceException ex) {
            searchFerryCovidReasonWithRetries(text, retriesLeft - 1);
        }
    }

    private void toggleActiveCovidReasonWithRetries(int retriesLeft) {
        if (retriesLeft == 0) {
            throw new RuntimeException("Exceeded maximum retries for toggleActiveCovidReason.");
        }
        logger.log("Attempt: " + (6 - retriesLeft));
        try {
            refreshPage();

            WebElement activateSwitch = driverTools.waitForElementToBeClickable(ACTIVE_SWITCH, 10)
                    .orElseThrow(() -> new NoSuchElementException("Active switch not found or not clickable"));
            driverTools.clickWithJavascriptExecutor(activateSwitch);

            WebElement acceptButton = driverTools.waitForElementToBeClickable(ACCEPT_BUTTON, 10)
                    .orElseThrow(() -> new NoSuchElementException("Accept button not found or not clickable"));
            driverTools.clickWithJavascriptExecutor(acceptButton);

            driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10).orElse(null);

        } catch (Exception ex) {
            logger.log("toggleActiveCovidReason attempt failed with: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
            toggleActiveCovidReasonWithRetries(retriesLeft - 1);
        }
    }

    public void toggleActiveCovidReason() {
        int max_retries = 5;
        toggleActiveCovidReasonWithRetries(max_retries);
    }

    public boolean isSuccessSnackbarDisplayedWithCorrectMessage(String successMessage) {
        WebElement snackbar = driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 10).orElse(null);
        return snackbar != null && snackbar.getText().contains(successMessage);
    }

    public void editCovidReason(String idReason, String enReason) {
        driverTools.waitForElementToAppear(ID_DESCRIPTION);
        driverTools.clearWithJsExecutor(ID_DESCRIPTION);
        driverTools.sendKeys(ID_DESCRIPTION, idReason);
        driverTools.clearWithJsExecutor(EN_DESCRIPTION);
        driverTools.sendKeys(EN_DESCRIPTION, enReason);
        driverTools.click(SAVE_BUTTON);
    }

    public void goToEditTab() {
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        driverTools.click(EDIT_BUTTON);
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }
}