package com.tiket.web.dashboard.page.accommodation.external_supplier.dashboard;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class SidebarPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator SIDEBAR_NAV_ITEMS = new Locator("SIDEBAR_NAV_ITEMS", By.xpath("//nav[@data-testid='nav-container']"));

    public SidebarPage(Session<T> session) {
        super(session);
    }

    public boolean isSidebarDisplayed() {
        return driverTools.waitForElementToAppear(SIDEBAR_NAV_ITEMS, 15).isPresent();
    }
}
