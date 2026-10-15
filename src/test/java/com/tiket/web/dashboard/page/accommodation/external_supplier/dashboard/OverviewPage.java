package com.tiket.web.dashboard.page.accommodation.external_supplier.dashboard;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OverviewPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator OVERVIEW_MENU = new Locator("OVERVIEW_MENU",By.xpath("//button[.//span[@data-testid='nav-label' and normalize-space()='Overview']]"));
    private static final Locator OVERVIEW_PAGE_TITLE = new Locator("OVERVIEW_PAGE_TITLE", By.xpath("//span[@id='title-page-id' and normalize-space()='Overview']"));
    private static final Locator PROPERTY_OVERVIEW_SECTION = new Locator("PROPERTY_OVERVIEW_SECTION", By.xpath("//p[normalize-space()='Property Overview']"));
    private static final Locator PROPERTY_OVERVIEW_CARDS_CONTAINER = new Locator("PROPERTY_OVERVIEW_CARDS_CONTAINER", By.xpath("//p[normalize-space()='Property Overview']/ancestor::div[contains(@class,'flex-col')][1]//div[contains(@class,'justify-between') and contains(@class,'flex-wrap')]"));
    private static final Locator ROOM_OVERVIEW_SECTION = new Locator("ROOM_OVERVIEW_SECTION", By.xpath("//p[normalize-space()='Room Overview']"));
    private static final Locator ROOM_OVERVIEW_CARDS_CONTAINER = new Locator("ROOM_OVERVIEW_CARDS_CONTAINER", By.xpath("//p[normalize-space()='Room Overview']/ancestor::div[contains(@class,'flex-col')][1]//div[contains(@class,'justify-between') and contains(@class,'flex-wrap')]"));

    public OverviewPage(Session<T> session) {
        super(session);
    }

    public void goToOverview() {
        driverTools.waitForElementToAppear(OVERVIEW_MENU, 15).orElseThrow();
        driverTools.waitForElementToBeClickable(OVERVIEW_MENU, 10).orElseThrow();
        driverTools.click(OVERVIEW_MENU);
    }

    public boolean isOverviewPageLoaded() {
        return driverTools.waitForElementToAppear(OVERVIEW_PAGE_TITLE, 15).isPresent();
    }

    public boolean isPropertyOverviewDisplayed() {
        boolean sectionVisible = driverTools.waitForElementToAppear(PROPERTY_OVERVIEW_SECTION, 15).isPresent();
        boolean hasData = driverTools.waitForElementToAppear(PROPERTY_OVERVIEW_CARDS_CONTAINER, 10).isPresent();
        logger.log("[PropertyOverview] section visible: " + sectionVisible + ", has data cards: " + hasData);
        return sectionVisible && hasData;
    }

    public boolean isRoomOverviewDisplayed() {
        boolean sectionVisible = driverTools.waitForElementToAppear(ROOM_OVERVIEW_SECTION, 15).isPresent();
        boolean hasData = driverTools.waitForElementToAppear(ROOM_OVERVIEW_CARDS_CONTAINER, 10).isPresent();
        logger.log("[RoomOverview] section visible: " + sectionVisible + ", has data cards: " + hasData);
        return sectionVisible && hasData;
    }
}
