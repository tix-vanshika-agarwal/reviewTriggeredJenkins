package com.tiket.web.dashboard.page.ttd.CMS.CMSSupplierConfigToDoListPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CMSSupplierConfigToDoListPageWeb<T extends WebDriver> extends WebPage<T> {
    public CMSSupplierConfigToDoListPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSSupplierConfigToDoListPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSSupplierConfigToDoListPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSSupplierConfigToDoListPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator ACTION_BUTTON = new Locator("ACTION_BUTTON", By.xpath("(//button[contains(@class, 'btn-action-table')])[1]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("//li[contains(@class,'MuiListItem-button')]//span[text()='Edit']"));
    private static final Locator PACKAGE_MAPPING_TOGGLE = new Locator("PACKAGE_MAPPING_TOGGLE", By.xpath("//div[contains(@class, 'tix-toggle') and contains(@class, 'v4')]"));
    private static final Locator APPLY_BUTTON = new Locator("APPLY_BUTTON", By.xpath("//button[normalize-space(text())='Apply']"));
    private static final Locator OFFER_UPDATE_ALERT = new Locator("OFFER_UPDATE_ALERT", By.xpath("//div[@role='alert' and contains(text(), 'Successful update Offer')]"));
    private static final Locator SUPPLIER_FILTER_DROPDOWN = new Locator("SUPPLIER_FILTER_DROPDOWN", By.xpath("(//div[contains(@class,'select-field-v2')]//div[contains(@class,'tix-react-select__dropdown-indicator')])[2]"));
    private static final Locator SUPPLIER_OPTION_ANCOL = new Locator("SUPPLIER_OPTION_ANCOL", By.xpath("//div[contains(@class,'tix-react-select__option') and normalize-space()='ANCOL']"));
    private static final Locator ROW_SIZE = new Locator("ROW_SIZE", By.xpath("//span[contains(text(), 'Showing')]"));


    public void goToRWSMappingPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {
                driver.get("https://gatotkaca.tiket.com/admin/event-ms/to-do/supplier-configuration/rws");
            }
            case PREPROD -> {
                driver.get("https://preprod.tiket.com/admin/event-ms/to-do/supplier-configuration/rws");
            }
            case PROD -> {
                driver.get("https://www.tiket.com/admin/event-ms/to-do/supplier-configuration/rws");
            }
        }
        driverTools.waitForWebToLoad();
    }

    public void goToSupplierProductPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {
                driver.get("https://gatotkaca.tiket.com/admin/event-ms/to-do/supplier-configuration/supplier-product");
            }
            case PREPROD -> {
                driver.get("https://preprod.tiket.com/admin/event-ms/to-do/supplier-configuration/supplier-product");
            }
            case PROD -> {
                driver.get("https://www.tiket.com/admin/event-ms/to-do/supplier-configuration/supplier-product");
            }
        }
        driverTools.waitForWebToLoad();
    }


    public void clickActionButton() {
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(ACTION_BUTTON);
        driverTools.clickWithJavascriptExecutor(ACTION_BUTTON);
    }

    public void clickEditButton() {
        driverTools.waitForElementToAppear(EDIT_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_BUTTON);
    }

    public void updateToggleButton() {
        driverTools.waitForElementToAppear(PACKAGE_MAPPING_TOGGLE);
        driverTools.clickWithJavascriptExecutor(PACKAGE_MAPPING_TOGGLE);
    }

    public void clickOnApplyButton() {
        driverTools.waitForElementToAppear(APPLY_BUTTON);
        driverTools.clickWithJavascriptExecutor(APPLY_BUTTON);
    }

    public boolean isOfferUpdateAlertDisplayed() {
        driverTools.waitForElementToAppear(OFFER_UPDATE_ALERT);
        return driverTools.isElementDisplayed(OFFER_UPDATE_ALERT);
    }

    public void selectSupplierFilter() {
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(SUPPLIER_FILTER_DROPDOWN);
        driverTools.click(SUPPLIER_FILTER_DROPDOWN);
        driverTools.clickWithJavascriptExecutor(SUPPLIER_FILTER_DROPDOWN);
        driverTools.waitForElementToAppear(SUPPLIER_OPTION_ANCOL);
        driverTools.clickWithJavascriptExecutor(SUPPLIER_OPTION_ANCOL);
    }


    public int getRowSize() {
        driverTools.waitForElementToAppear(ROW_SIZE);
        String text = driverTools.getText(ROW_SIZE).orElse("0");
        char lastChar = text.charAt(text.length() - 1);
        int rowSize = Character.getNumericValue(lastChar);
        return rowSize;
    }

    public Locator getSupplierSectionLocator(int rowIndex) {
        return new Locator("SUPPLIER_SECTION_ROW_" + rowIndex,
                By.xpath("//table//tbody/tr[" + rowIndex + "]/td[5]"));
    }

    public boolean areAllSuppliersAncol() {
        int rowCount = getRowSize();
        for (int i = 1; i <= rowCount; i++) {
            Locator supplierLocator = getSupplierSectionLocator(i);
            driverTools.waitForElementToAppear(supplierLocator);
            String supplierText = driverTools.getText(supplierLocator).orElse("").trim();
            if (!supplierText.equalsIgnoreCase("Ancol")) {
                logger.log("Row " + i + ": Supplier is not Ancol -> " + supplierText);
                return false;
            }
        }
        return true;
    }
}
