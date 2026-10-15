package com.tiket.web.dashboard.page.ttd.Ignis.IgnisUpdateThingsToDoListPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class IgnisUpdateThingsToDoListPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisUpdateThingsToDoListPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisUpdateThingsToDoListPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisUpdateThingsToDoListPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisUpdateThingsToDoListPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator PRODUCT_SEARCH_BAR = new Locator("PRODUCT_SEARCH_BAR", By.xpath("//div[@class='search-input-with-button']//input"));
    private static final Locator PRODUCT_NAME_CHECKBOX = new Locator("PRODUCT_NAME_CHECKBOX", By.xpath("//div[@class='product-list-container']//label"));
    private static final Locator SELECT_ALL_PACKAGE_CHECKBOX = new Locator("SELECT_ALL_PACKAGE_CHECKBOX", By.xpath("//div[@class='selector package-container']//div[@class='quick-actions']//div[@class='checkbox label-right']"));
    private static final Locator PRIMARY_BUTTON = new Locator("PRIMARY_BUTTON", By.xpath("//button[contains(@class,'btn-primary')]"));
    private static final Locator CLOSED_COLLAPSE_SECTION = new Locator("CLOSED_COLLAPSE_SECTION", By.xpath("//div[@class='collapsible product close' or @class='collapsible package close'][1]"));
    private static final Locator OPENED_COLLAPSE_PACKAGE_SECTION = new Locator("OPENED_COLLAPSE_PACKAGE_SECTION", By.xpath("//div[@class='collapsible package open']"));
    private static final Locator POPUP_PRIMARY_KEY = new Locator("POPUP_PRIMARY_KEY", By.xpath("//div[contains(@class,'modal')]//button[contains(@class,'variant-primary')]"));
    private static final Locator SELLING_DATE_OPTION = new Locator("SELLING_DATE_OPTION", By.xpath("//section[@class='date-options'][1]//div[@class='radio-info']"));
    private static final Locator PUBLISH_DATE_OPTION = new Locator("PUBLISH_DATE_OPTION", By.xpath("//section[@class='date-options'][2]//div[@class='radio-info']"));

    public void inputProductSearchBar(String productName) {
        driverTools.waitForElementToAppear(PRODUCT_SEARCH_BAR);
        driverTools.clearInput(PRODUCT_SEARCH_BAR);
        driverTools.sendKeys(PRODUCT_SEARCH_BAR, productName);
        driverTools.dummyWait(1);
    }

    public void selectProduct(String productName) {
        List<WebElement> productResultList = driverTools.elementsUntilExist(PRODUCT_NAME_CHECKBOX).get();

        boolean found = false;
        for (WebElement e : productResultList) {
            String elementText = driverTools.getText(e);
            if (elementText.equalsIgnoreCase(productName)) {
                found = true;
                driverTools.click(e);
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("There is no product with title '"+productName+"'");
        }
    }

    public void selectAllPackage() {
        driverTools.waitForElementToAppear(SELECT_ALL_PACKAGE_CHECKBOX);
        driverTools.click(SELECT_ALL_PACKAGE_CHECKBOX);
    }

    public void clickPrimaryButton() {
        driverTools.waitForElementToAppear(PRIMARY_BUTTON);
        driverTools.click(PRIMARY_BUTTON);
    }

    public void expandAllProductPackages() {
        int max_count = 10;
        int count = 0;
        while (driverTools.waitForElementToAppear(CLOSED_COLLAPSE_SECTION,1).isPresent() && count < max_count) {
            driverTools.click(CLOSED_COLLAPSE_SECTION);
            count++;
        }
    }

    public void updateAllPackages(String editOfflinePrice, String editOfferedPrice) {
        int packageCount = driverTools.elementsUntilExist(OPENED_COLLAPSE_PACKAGE_SECTION).get().size();

        for (int i = 1 ; i <= packageCount ; i++) {
            driverTools.dummyWait(1);
            fillOfflinePrice(editOfflinePrice, i);
            fillOfferedPrice(editOfferedPrice, i);
        }
    }

    private void fillOfferedPrice(String editOfferedPrice, int index) {
        Locator locator = getOfferedPriceInputFormByIndex(index);
        driverTools.waitForElementToAppear(locator);
        driverTools.clearInput(locator);
        driverTools.sendKeys(locator, editOfferedPrice);
    }

    private Locator getOfferedPriceInputFormByIndex(int index) {
        return new Locator("OFFERED_PRICE_INPUT_"+index, By.xpath("//div[@class='collapsible package open']["+index+"]//input[@name='publishedPriceInCents']"));
    }

    private void fillOfflinePrice(String editOfflinePrice, int index) {
        Locator locator = getOfflinePriceInputFormByIndex(index);
        driverTools.waitForElementToAppear(locator);
        driverTools.clearInput(locator);
        driverTools.sendKeys(locator, editOfflinePrice);

    }

    private Locator getOfflinePriceInputFormByIndex(int index) {
        return new Locator("OFFLINE_PRICE_INPUT_"+index, By.xpath("//div[@class='collapsible package open']["+index+"]//input[@name='gimmickPriceInCents']"));
    }

    public void clickPopupPrimaryButton() {
        driverTools.waitForElementToAppear(POPUP_PRIMARY_KEY);
        driverTools.click(POPUP_PRIMARY_KEY);
    }

    public void chooseSellingDate(boolean isAnyTime, String targetDateAndTime) {
        List<WebElement> radioButtonElement = driverTools.elementsUntilExist(SELLING_DATE_OPTION).get();

        if (isAnyTime) {
            driverTools.click(radioButtonElement.get(0));
        } else {
            throw new NotImplementedException("Need to implement the further steps");
        }
    }

    public void choosePublishDate(boolean isAnyTime, String targetDateAndTime) {
        List<WebElement> radioButtonElement = driverTools.elementsUntilExist(PUBLISH_DATE_OPTION).get();

        if (isAnyTime) {
            driverTools.click(radioButtonElement.get(0));
        } else {
            throw new NotImplementedException("Need to implement the further steps");
        }
    }

    public void updateOpenDay(String targetDay) {
        String[] targetDayArray1 = targetDay.split("-");

        for (String e: targetDayArray1) {
            Locator locator = getDayCheckboxLocatorByText(e);
            if (driverTools.waitForElementToAppear(locator).isPresent()) {
                driverTools.click(locator);
            } else {
                throw new NoSuchElementException("There is no such day option for '"+e+"'");
            }
        }
    }

    private Locator getDayCheckboxLocatorByText(String targetDay) {
        String targetDayId = targetDay.split("/")[0];
        String targetDayEn = targetDay.split("/")[1];

        return new Locator("DAY_CHECK_BOX_TEXTS_"+targetDayEn.toUpperCase(), By.xpath("//div[@class='day-selector']//label[text()='"+targetDayEn+"' or text()='"+targetDayId+"']"));
    }
}
