package com.tiket.web.dashboard.page.accommodation.external_supplier.rooms_mapping;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.dashboard.page.accommodation.internal_supplier.dashboard.InternalSupplierDashboardPage;

public class ExternalSupplierRoomsMappingPage<T extends WebDriver> extends InternalSupplierDashboardPage<T> {

    public ExternalSupplierRoomsMappingPage(Session<T> session) {
        super(session);
    }

    @Override
    public void clickMapButtonByPropertyId(String propertyId) {
        int propertyIdIndex = getIndexOfColumnTitle("Vendor Property ID");
        int actionIndex = getIndexOfColumnTitle("Action");
        String xpath = "//tr[./td[" + propertyIdIndex + "][normalize-space()='" + propertyId + "']]/td[" + actionIndex + "]//a[contains(@class,'Button') or contains(normalize-space(),'Map')]";
        Locator mapButton = new Locator("EXTERNAL_MAP_BUTTON_" + propertyId, By.xpath(xpath));
        driverTools.waitForElementToBeClickable(mapButton, 10);
        driverTools.scrollElementToCenter(mapButton);
        driverTools.clickWithJavascriptExecutor(mapButton);
        driverTools.dummyWait(2);
        logger.log("Clicked map button for property ID: " + propertyId);
        driverTools.tabSwitch("detail");
        driverTools.waitForWebToLoad();
    }

    public void switchToSupplyDashboardTab() {
        driverTools.switchToWindowContainingUrl("supply");
        driverTools.waitForWebToLoad();
    }

    public void clickSegmentedButton(String tableLabel, String label) {
        String xpath = "//span[contains(text(),'" + tableLabel + "')]/ancestor::div[contains(@class,'title-toolbar')]//button[.//span[normalize-space()='" + label + "']]";
        Locator button = new Locator("SEGMENTED_BUTTON_" + tableLabel.toUpperCase().replace(" ", "_") + "_" + label.toUpperCase(), By.xpath(xpath));
        driverTools.waitForElementToBeClickable(button, 5);
        driverTools.scrollElementToCenter(button);
        driverTools.clickWithJavascriptExecutor(button);
        driverTools.dummyWait(2);
    }

    public void ensureRoomMappedToGroup(String vendorRoomId, String roomGroupName) {
        clickSegmentedButton("Mapped List", "ID");
        searchByVendorRoomId("Mapped List", vendorRoomId);
        if (isTextPresentInTableMap("Mapped List", vendorRoomId)) {
            logger.log("[Precondition] Room " + vendorRoomId + " is already mapped, skipping map step");
            return;
        }
        logger.log("[Precondition] Room " + vendorRoomId + " is not mapped, mapping it to room group " + roomGroupName);
        clickSegmentedButton("Unmapped Rooms", "ID");
        searchByVendorRoomId("Unmapped Rooms", vendorRoomId);
        selectRoomById(vendorRoomId);
        clickMapButtonByRoomGroupId(roomGroupName);
        clickContinueOnPopup();
    }
}
