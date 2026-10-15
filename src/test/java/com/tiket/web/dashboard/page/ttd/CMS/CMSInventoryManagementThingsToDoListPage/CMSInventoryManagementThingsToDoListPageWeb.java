package com.tiket.web.dashboard.page.ttd.CMS.CMSInventoryManagementThingsToDoListPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CMSInventoryManagementThingsToDoListPageWeb<T extends WebDriver> extends WebPage<T> {
    public CMSInventoryManagementThingsToDoListPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSInventoryManagementThingsToDoListPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSInventoryManagementThingsToDoListPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSInventoryManagementThingsToDoListPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator SEARCH_BAR = new Locator("SEARCH_BAR", By.xpath("//div[contains(@class,'input-box-v2')]//input"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[@type='submit']"));
    private static final Locator SEARCH_BAR_WRAPPER = new Locator("PRODUCT_SEARCH_BAR_WRAPPER", By.xpath("//div[@class='input-name']"));
    private static final Locator LOADING_ANIMATION = new Locator("LOADING_ANIMATION", By.xpath("//div[@class='tix-loading v4']"));

    public void goToPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {driver.get("https://gatotkaca.tiket.com/admin/event-ms/to-do/product-management/ttd-list");}
            case PREPROD -> {driver.get("https://preprod.tiket.com/admin/event-ms/to-do/product-management/ttd-list");}
            case PROD -> {driver.get("https://www.tiket.com/admin/event-ms/to-do/product-management/ttd-list");}
        }
        driverTools.waitForWebToLoad();
    }

    public void inputSearchBar(String productName) {
        driverTools.waitForElementToAppear(SEARCH_BAR);
        driverTools.click(SEARCH_BAR_WRAPPER);
        driverTools.clearInput(SEARCH_BAR);
        driverTools.sendKeys(SEARCH_BAR, productName);
    }

    public void pressSearchButton() {
        driverTools.waitForElementToAppear(SEARCH_BUTTON);
        driverTools.click(SEARCH_BUTTON);
        driverTools.dummyWait(1);
    }

    public void goToTab(int index) {
        Locator locator = getTabLocatorByIndex(index);
        driverTools.waitForElementToAppear(locator);
        driverTools.scrollToElement(locator);
        driverTools.click(locator);
        driverTools.dummyWait(1);
        driverTools.waitForElementToDisappear(LOADING_ANIMATION);
    }

    private Locator getTabLocatorByIndex(int index) {
        return new Locator("TAB_"+index, By.xpath("//div[@class='ttd-tab']/*["+index+"]"));
    }

    public void startProductReview(String productName) {
        Locator locator = getStartReviewButtonByProductName(productName);
        driverTools.waitForElementToAppear(locator);
        driverTools.clickElementByCoordinateDWeb(locator);
        driverTools.dummyWait(2);

        driverTools.closeCurrentTab();
        driverTools.switchToWindowContainingUrl("ttd-list/review-ttd");
    }

    public void startPackageReview(String packageName) {
        Locator locator = getStartReviewButtonByPackageName(packageName);
        driverTools.waitForElementToAppear(locator);
        driverTools.clickElementByCoordinateDWeb(locator);
        driverTools.dummyWait(2);

        driverTools.closeCurrentTab();
        driverTools.switchToWindowContainingUrl("ttd-list/review-package");
    }

    private Locator getStartReviewButtonByPackageName(String packageName) {
        return new Locator("START_REVIEW_BUTTON", By.xpath("//div[@class='tab-package__card-wrapper' and .//h2[text()='"+packageName+"']]//button[contains(@class,'secondary')]"));
    }

    private Locator getStartReviewButtonByProductName(String dataName) {
        return new Locator("START_REVIEW_BUTTON", By.xpath("//div[@class='tab-draft__data-list']/div[@class='accordion-wrapper ' and .//h2[text()='"+dataName+"']]//button"));
    }

    public boolean isStatusNeedReview(String dataName) {
        Locator locator = getNeedReviewBadgeLocatorByPackageName(dataName);
        return driverTools.waitForElementToAppear(locator).isPresent();
    }

    private Locator getNeedReviewBadgeLocatorByPackageName(String packageName) {
        return new Locator("NEED_REVIEW_BADGE", By.xpath("//div[@class='tab-package__card-wrapper' and .//h2[text()='"+packageName+"']]//div[@class='status-info__label label-review']"));
    }
}
