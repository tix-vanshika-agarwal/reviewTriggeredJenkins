package com.tiket.web.dashboard.page.ttd.CMS.CMSProductSettingSeatMapPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CMSProductSettingSeatMapPageWeb<T extends WebDriver> extends WebPage<T> {
    public CMSProductSettingSeatMapPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSProductSettingSeatMapPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSProductSettingSeatMapPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSProductSettingSeatMapPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator ADD_NEW_SEAT_MAP = new Locator("ADD_NEW_SEAT_MAP", By.xpath("//a[@class='action-add-component component__add']"));
    private static final Locator SUCCESS_NOTIF_BAR = new Locator("SUCCESS_NOTIF_BAR", By.xpath("//div[@class='nav-bar-alert alert alert-success fade show']"));
    private static final Locator ACTION_DELETE_BUTTON = new Locator("ACTION_DELETE_BUTTON", By.xpath("//li[@role='menuitem'][2]"));
    private static final Locator CONFIRM_DELETE_BUTTON = new Locator("CONFIRM_DELETE_BUTTON", By.xpath("//div[@class='dialog-popup']//button[contains(@class,'default')]"));

    public void goToPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {driver.get("https://gatotkaca.tiket.com/admin/event-ms/to-do/product-configuration/seat-map");}
            case PREPROD -> {driver.get("https://preprod.tiket.com/admin/event-ms/to-do/product-configuration/seat-map");}
            case PROD -> {driver.get("https://www.tiket.com/admin/event-ms/to-do/product-configuration/seat-map");}
        }
        driverTools.waitForWebToLoad();
    }

    public void clickCreateButton() {
        driverTools.waitForElementToAppear(ADD_NEW_SEAT_MAP);
        driverTools.click(ADD_NEW_SEAT_MAP);
        driverTools.waitForWebToLoad();
    }

    public boolean isSuccessNotifBarVisible() {
        return driverTools.waitForElementsToAppear(SUCCESS_NOTIF_BAR).isPresent();
    }

    public boolean isNewlyCreatedSeatMapExist(String seatMapName) {
        String targetElementText = driverTools.getText(getFirstSeatmapNameLocatorByIndex(1)).orElseThrow();
        return seatMapName.equalsIgnoreCase(targetElementText);
    }

    private Locator getFirstSeatmapNameLocatorByIndex(int index) {
        return new Locator("SEATMAP_"+index+"_NAME", By.xpath("//tbody/tr["+index+"]/td[1]/div"));
    }

    public void deleteSeatMap(String seatMapName) {
        clickActionButton(seatMapName);
        clickDeleteButton();
        clickConfirmDeleteButton();
    }

    private void clickConfirmDeleteButton() {
        driverTools.waitForElementToAppear(CONFIRM_DELETE_BUTTON);
        driverTools.click(CONFIRM_DELETE_BUTTON);
    }

    private void clickDeleteButton() {
        driverTools.waitForElementToAppear(ACTION_DELETE_BUTTON);
        driverTools.click(ACTION_DELETE_BUTTON);
    }

    private void clickActionButton(String seatMapName) {
        Locator locator = getActionButtonLocatoByName(seatMapName);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
    }

    private Locator getActionButtonLocatoByName(String seatMapName) {
        return new Locator("ACTION_BUTTON", By.xpath("//tbody/tr[./td[1]/div[text()='"+seatMapName+"']]//button"));
    }
}
