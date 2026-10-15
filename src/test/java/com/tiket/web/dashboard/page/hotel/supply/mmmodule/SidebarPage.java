package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SidebarPage<T extends WebDriver> extends BasePage<T> {
    public enum SidebarMenu {
        HOTEL_CORE("Hotel Core"),
        HOTEL_LIBRARY("Hotel Library"),
        HOTEL_ARCHIVE("Hotel Archive"),
        HOTEL_MERGE("Hotel Merge"),
        MANUAL_MERGE("Manual Merge"),
        DAILY_MERGE("Daily Merge"),
        ROOM_MASTER_DATA("Room Master Data"),
        ROOM_CONFIGURATION("Room Configuration"),
        ROOM_LIST ("Room List"),
        SYNC("Sync"),
        LOCATION_POLYGON("Location Polygon"),
        NEARBY("Nearby"),
        WEIGHT_UPDATE("Weight Update"),
        HOTEL_CONTENT("Hotel Content"),
        MAIN_PHOTO("Main Photo"),
        AURORA("Aurora"),
        NO_OF_HOTELS_PER_LOCATION("Number of Hotels Per Location"),
        PROPERTY_SCORING("Property Scoring"),
        POST_BOOK_SCORE("Post-Book Score"),
        ADDRESS_COMPLETION("Address Completion"),
        SETTINGS("Settings"),
        SHUTDOWN_VENDOR("Turn On/Off Vendor")
        ;

        private final String menuLabel;

        private SidebarMenu(String menuLabel) {
            this.menuLabel = menuLabel;
        }

        public String getMenuLabel() {
            return menuLabel;
        }
    }

    public SidebarPage(Session<T> session) {
        super(session);
    }

    private static Locator getSidebarMenuByName(String menuName) {
        return new Locator("SIDEBAR_MENU", new By.ByXPath(String.format("//button[.//span[normalize-space(text())='%s']]", menuName)));
    }

    private static Locator getSidebarSubmenuByName(String submenuName) {
        return new Locator("SIDEBAR_SUBMENU", new By.ByXPath(String.format("//a[contains(@class,\"nav-sub-list\")][text()='%s']", submenuName)));
    }

    private static Locator getSidebarSubmenuByNameNext(String submenuName) {
        return new Locator("SIDEBAR_SUBMENU", new By.ByXPath(String.format("//span[contains(@class,'text-left')][text()='%s']", submenuName)));
    }

    public void clickSidebarMenu(SidebarMenu menuName) {
        WebElement menu = driverTools.waitForElementToPresence(getSidebarMenuByName(menuName.menuLabel)).orElseThrow();
        try {
            driverTools.click(menu);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(menu);
        }
    }

    public void clickSidebarSubmenu(SidebarMenu menuName) {
        WebElement submenu = null;
        try {
            submenu = driverTools.waitForElementToPresence(getSidebarSubmenuByName(menuName.menuLabel), 10).orElseThrow();
        } catch (Exception e) {
            submenu = driverTools.waitForElementToPresence(getSidebarSubmenuByNameNext(menuName.menuLabel), 10).orElseThrow();
        }
        driverTools.waitForElementToBeClickable(submenu, 10);
        try {
            driverTools.click(submenu);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(submenu);
        }
    }

    public void clickSyncSubmenu(String submenuName) {
        WebElement submenu = driverTools.waitForElementToPresence(getSidebarSubmenuByNameNext(submenuName), 10).orElseThrow();
        driverTools.scrollToElement(submenu);
        driverTools.click(submenu);
    }
}
