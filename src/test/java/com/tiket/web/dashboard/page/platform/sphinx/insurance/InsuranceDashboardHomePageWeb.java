package com.tiket.web.dashboard.page.platform.sphinx.insurance;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

public class InsuranceDashboardHomePageWeb<T extends WebDriver> extends WebPage<T> {
    public InsuranceDashboardHomePageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> InsuranceDashboardHomePageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new InsuranceDashboardHomePageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new InsuranceDashboardHomePageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator SIDEBAR_MENUS = new Locator("SIDEBAR_MENUS", By.xpath("//*[contains(@class,'Sidebar_sidebar_submenus')]"));

    public void clickSidebarMenu(String choice){
        driverTools.dummyWait(3);
        List<WebElement> menus = driverTools.waitForElementsToAppear(SIDEBAR_MENUS).orElse(null);
        if (menus == null || menus.isEmpty()) throw new NoSuchElementException("No menu to select");

        for(WebElement menu : menus){
            if (choice.equalsIgnoreCase(driverTools.getText(menu))) {
                driverTools.click(menu);
                break;
            }
        }
    }
}
