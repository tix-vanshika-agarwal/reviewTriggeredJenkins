package com.tiket.web.dashboard.page.ttd.Ignis.IgnisManageTicketDetailsPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class IgnisManageTicketDetailsPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisManageTicketDetailsPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisManageTicketDetailsPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisManageTicketDetailsPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisManageTicketDetailsPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", By.xpath("//input[@name='search-product']"));
    private static final Locator TABS = new Locator("TABS", By.xpath("//div[@role='tablist']/child::*"));
    private static final Locator PAGE_PRIMARY_BUTTON = new Locator("PAGE_PRIMARY_BUTTON", By.xpath("//div[@class='mgmt-footer-menu']//button[contains(@class,'primary')]"));
    private static final Locator SNACKBAR_MESSAGE = new Locator("SNACKBAR_MESSAGE", By.xpath("//div[@class='snackbar-container']"));
    private static final Locator LAST_TAB_ACTIVE = new Locator("LAST_TAB_ACTIVE", By.xpath("//div[@role='tablist']/child::*[last()]"));

    public void inputSearchBar(String productName) {
        driverTools.waitForElementToAppear(SEARCH_BAR);
        driverTools.clearInput(SEARCH_BAR);
        driverTools.sendKeys(SEARCH_BAR, productName);
        driverTools.dummyWait(1);
    }

    public void clickProduct(String productName) {
        Locator locator = getProductExpandLocatorByProductName(productName);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
    }

    private Locator getProductExpandLocatorByProductName(String productName) {
        return new Locator("PRODUCT_EXPAND", By.xpath("//div[@class='package-details-product-item' and .//div[@class='heading']//p[text()='"+productName+"']]"));
    }

    public void clickPackage(String packageName) {
        Locator locator = getPackageLocatorByPackageName(packageName);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
    }

    private Locator getPackageLocatorByPackageName(String packageName) {
        return new Locator("PACKAGE", By.xpath("//div[contains(@class,'package-name-container')]/p[text()='"+packageName+"']"));
    }

    public void clickNextAndSave() {
        List<WebElement> tabsElement = driverTools.elementsUntilExist(TABS).get();

        for (int i = 0; i < tabsElement.size()-1; i++) {
            driverTools.waitForElementToAppear(PAGE_PRIMARY_BUTTON);
            driverTools.click(PAGE_PRIMARY_BUTTON);
        }
    }

    public boolean isSnackBarVisible() {
        return  driverTools.waitForElementToPresence(SNACKBAR_MESSAGE).isPresent();
    }

    public boolean isActiveTabLast() {
        return  driverTools.waitForElementsToAppear(LAST_TAB_ACTIVE).isPresent();
    }
}
