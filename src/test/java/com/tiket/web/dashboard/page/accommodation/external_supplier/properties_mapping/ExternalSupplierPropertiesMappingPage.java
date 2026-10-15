package com.tiket.web.dashboard.page.accommodation.external_supplier.properties_mapping;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.dashboard.page.accommodation.internal_supplier.dashboard.InternalSupplierDashboardPage;
import com.tiket.web.dashboard.page.accommodation.internal_supplier.properties_mapping.InternalSupplierPropertiesMappingPage;

public class ExternalSupplierPropertiesMappingPage<T extends WebDriver> extends InternalSupplierPropertiesMappingPage<T> {

    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", By.xpath("//button[text()='Continue']"));

    public ExternalSupplierPropertiesMappingPage(Session<T> session) {
        super(session);
    }

    public void clickMapByVendorPropertyId(String vendorPropertyId) {
        int propertyIdIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Vendor Property ID");
        int actionIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Action");
        String xpath = "//tr[./td[" + propertyIdIndex + "][normalize-space()='" + vendorPropertyId + "']]/td[" + actionIndex + "]//a[contains(@class,'Button')]";
        Locator mapButton = new Locator("MAP_BUTTON_" + vendorPropertyId, By.xpath(xpath));
        driverTools.waitForElementToBeClickable(mapButton, 10);
        driverTools.scrollElementToCenter(mapButton);
        driverTools.clickWithJavascriptExecutor(mapButton);
        driverTools.switchToNewTab();
        driverTools.waitForWebToLoad();
    }

    public void switchToSupplyDashboardTab() {
        driverTools.switchToWindowContainingUrl("supply");
        driverTools.waitForWebToLoad();
    }

    public void clickContinueIfPresent() {
        if (driverTools.waitForElementToAppear(CONTINUE_BUTTON).isPresent()) {
            driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
            driverTools.waitForElementToDisappear(CONTINUE_BUTTON, 5);
        }
    }
}
