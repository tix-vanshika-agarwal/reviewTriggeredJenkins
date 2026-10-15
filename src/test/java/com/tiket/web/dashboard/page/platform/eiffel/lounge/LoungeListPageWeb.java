package com.tiket.web.dashboard.page.platform.eiffel.lounge;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class LoungeListPageWeb<T extends WebDriver> extends WebPage<T> {
    public LoungeListPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> LoungeListPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new LoungeListPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new LoungeListPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator CREATE_LOUNGE_BUTTON = new Locator("CREATE_LOUNGE_BUTTON",
            By.xpath("//button[normalize-space()='Create Lounge']"));
    private static final Locator CREATE_LOUNGE_POPUP = new Locator("CREATE_LOUNGE_POPUP",
            By.cssSelector("section[class*='Modal_modal_popup']"));
    private static final Locator AIRPORT_DROPDOWN = new Locator("AIRPORT_DROPDOWN",
            By.cssSelector("section[class*='Modal_modal_popup'] input[placeholder='Airport Code']"));
    private static final Locator LOUNGE_NAME_INPUT = new Locator("LOUNGE_NAME_INPUT",
            By.id("lounge-name"));
    private static final Locator LOUNGE_ADMINISTRATOR_SEARCH_BAR = new Locator("LOUNGE_ADMINISTRATOR_SEARCH_BAR",
            By.cssSelector("section[class*='Modal_modal_popup'] input[placeholder='Search Email']"));
    private static final Locator CREATE_LOUNGE_POPUP_CONFIRM = new Locator("CREATE_LOUNGE_POPUP_CONFIRM",
            By.xpath("//section[contains(@class,'Modal_modal_popup')]//button[normalize-space()='Create Lounge']"));
    private static final Locator LOUNGE_SEARCH_BAR = new Locator("LOUNGE_SEARCH_BAR",
            By.cssSelector("input[class*='SearchBox_accessibility_search']"));
    private static final Locator LOUNGE_SEARCH_BAR_SEARCH_BUTTON = new Locator("LOUNGE_SEARCH_BAR_SEARCH_BUTTON",
            By.xpath("//button[normalize-space()='Search']"));

    private static final Locator LOUNGE_DETAILS_POPUP = new Locator("LOUNGE_DETAILS_POPUP",
            By.cssSelector("section[class*='Modal_modal_popup']"));
    private static final Locator LOUNGE_STATUS_SWITCH = new Locator("LOUNGE_STATUS_SWITCH",
            By.xpath("//p[normalize-space()='Activate Lounge']/following-sibling::div[contains(@class,'Toggle_container')]"));
    private static final Locator LOUNGE_STATUS_CHECKBOX = new Locator("LOUNGE_STATUS_CHECKBOX",
            By.xpath("//p[normalize-space()='Activate Lounge']/following-sibling::div[contains(@class,'Toggle_container')]//input[@type='checkbox']"));
    private static final Locator LOUNGE_DETAILS_POPUP_SAVE_BUTTON = new Locator("LOUNGE_DETAILS_POPUP_SAVE_BUTTON",
            By.xpath("//section[contains(@class,'Modal_modal_popup')]//button[normalize-space()='Update Lounge']"));
    private static final Locator LOADING_OVERLAY = new Locator("LOADING_OVERLAY",
            By.cssSelector("[data-testid='loading-container']"));

    private void waitForLoadingOverlayToDisappear() {
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 15);
    }

    public void clickCreateLoungeButton() {
        waitForLoadingOverlayToDisappear();
        driverTools.waitForElementToAppear(CREATE_LOUNGE_BUTTON, 10);
        driverTools.click(CREATE_LOUNGE_BUTTON);
    }

    public void selectAirport(String airport) {
        driverTools.waitForElementToAppear(CREATE_LOUNGE_POPUP, 5);
        driverTools.click(AIRPORT_DROPDOWN);
        Locator selectionLoc = new Locator("SELECTION_" + airport,
                By.xpath("//div[@data-testid='option-box' and normalize-space()='" + airport + "']"));
        driverTools.scrollElementToCenter(selectionLoc);
        driverTools.clickElementByCoordinateDWeb(selectionLoc);
    }

    public void inputLoungeName(String name) {
        driverTools.sendKeys(LOUNGE_NAME_INPUT, name);
    }

    public void selectLoungeAdmin(String[] admins) {
        for (String admin : admins) {
            driverTools.clearInput(LOUNGE_ADMINISTRATOR_SEARCH_BAR);
            driverTools.sendKeys(LOUNGE_ADMINISTRATOR_SEARCH_BAR, admin);
            Locator selection = new Locator("SELECTION_" + admin,
                    By.xpath("//label[contains(@class,'Check_check') and contains(.,'" + admin + "')]"));
            driverTools.scrollElementToCenter(selection);
            driverTools.click(selection);
        }
    }

    public void confirmCreateLounge() {
        driverTools.scrollElementToCenter(CREATE_LOUNGE_POPUP_CONFIRM);
        driverTools.click(CREATE_LOUNGE_POPUP_CONFIRM);
    }

    public void searchLounge(String input) {
        waitForLoadingOverlayToDisappear();
        driverTools.waitForElementToAppear(LOUNGE_SEARCH_BAR, 5);
        driverTools.click(LOUNGE_SEARCH_BAR);
        driverTools.clearInput(LOUNGE_SEARCH_BAR);
        driverTools.sendKeys(LOUNGE_SEARCH_BAR, input);
        driverTools.clickElementByCoordinateDWeb(LOUNGE_SEARCH_BAR_SEARCH_BUTTON);
        driverTools.dummyWait(1);
        waitForLoadingOverlayToDisappear();
    }

    public boolean verifyLoungeHasBeenCreated(String loungeName, String loungeCode) {
        int loungeCodeIndex = getIndexTableColumn("Lounge Code");
        int loungeNameIndex = getIndexTableColumn("Lounge Name");

        WebElement lounge = driverTools
                .waitForElementToAppear(new Locator("TARGET_LOUNGE", By.xpath("//table/tbody/tr[normalize-space(./td["
                        + loungeCodeIndex + "]) = '" + loungeCode + "' and normalize-space(./td[" + loungeNameIndex
                        + "]) = '" + loungeName + "']")))
                .orElse(null);

        return lounge != null;
    }

    public void clickEditButton(String loungeName) {
        searchLounge(loungeName);
        Locator loc = new Locator("EDIT_BUTTON", By.xpath("//table/tbody/tr[1]//button[normalize-space()='Edit']"));
        driverTools.clickElementByCoordinateDWeb(loc);
    }

    /**
     * Flips the Activate Lounge toggle, confirming the backing checkbox actually changed — a click on the
     * just-opened (still-settling) modal can be swallowed and would otherwise save the unchanged status.
     */
    public void changeStatusOfLounge() {
        driverTools.waitForElementToAppear(LOUNGE_DETAILS_POPUP, 5);
        driverTools.waitForElementToAppear(LOUNGE_STATUS_SWITCH, 5);
        driverTools.toggleUntilStateChanges(LOUNGE_STATUS_SWITCH, LOUNGE_STATUS_CHECKBOX, 3);
    }

    public void clickSaveLoungeDetailsPopup() {
        driverTools.click(LOUNGE_DETAILS_POPUP_SAVE_BUTTON);
    }

    /**
     * Idempotently drives the lounge to {@code targetStatus} ("Active"/"Inactive"). Reads the current status
     * from the list and only opens edit + toggles + saves when it differs, so it is safe to call as a test
     * precondition or reset regardless of the lounge's leftover state from a previous run.
     * <p>
     * The "activate lounge" test previously blind-toggled the status: when the lounge was already Active
     * (left over from an earlier run whose reset never completed) the toggle deactivated it and the
     * "verify Active" assertion then failed. Establishing the starting state here makes the test deterministic.
     */
    public void ensureLoungeStatus(String loungeName, String targetStatus) {
        searchLounge(loungeName);
        int statusIndex = getIndexTableColumn("Status");
        Locator statusCell = new Locator("LOUNGE_STATUS", By.xpath("//table/tbody/tr[1]/td[" + statusIndex + "]"));
        driverTools.waitForElementToAppear(statusCell, 10);
        String current = driverTools.getText(statusCell).map(String::trim).orElse("");
        logger.log("ensureLoungeStatus: current=" + current + ", target=" + targetStatus);
        if (current.equalsIgnoreCase(targetStatus)) {
            return;
        }
        Locator editButton = new Locator("EDIT_BUTTON",
                By.xpath("//table/tbody/tr[1]//button[normalize-space()='Edit']"));
        driverTools.clickElementByCoordinateDWeb(editButton);
        driverTools.waitForElementToAppear(LOUNGE_DETAILS_POPUP, 5);
        driverTools.waitForElementToAppear(LOUNGE_STATUS_SWITCH, 5);
        driverTools.toggleUntilStateChanges(LOUNGE_STATUS_SWITCH, LOUNGE_STATUS_CHECKBOX, 3);
        driverTools.click(LOUNGE_DETAILS_POPUP_SAVE_BUTTON);
        driverTools.waitForElementToDisappear(LOUNGE_DETAILS_POPUP, 10);
    }

    public boolean verifyLoungeStatus(String loungeName, String statusTarget) {
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(LOUNGE_SEARCH_BAR, 10);
        searchLounge(loungeName);

        int statusIndex = getIndexTableColumn("Status");

        Locator statusCell = new Locator("LOUNGE_STATUS",
                By.xpath("//table/tbody/tr[1]/td[" + statusIndex + "]"));
        driverTools.waitForElementToAppear(statusCell, 10);
        String status = driverTools.getText(statusCell).orElseThrow().trim();

        logger.log("STATUS = " + status);
        logger.log("STATUS TARGET = " + statusTarget);

        return status.equalsIgnoreCase(statusTarget);
    }

    private int getIndexTableColumn(String columnTitle) {
        List<WebElement> cols = driverTools.getDriver()
                .findElements(By.xpath("//th[.='" + columnTitle + "']/preceding-sibling::th"));
        return cols.size() + 1;
    }
}
