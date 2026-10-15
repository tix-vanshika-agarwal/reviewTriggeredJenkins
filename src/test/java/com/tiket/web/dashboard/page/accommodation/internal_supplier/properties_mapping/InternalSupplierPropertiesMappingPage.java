package com.tiket.web.dashboard.page.accommodation.internal_supplier.properties_mapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.web.dashboard.page.accommodation.internal_supplier.dashboard.InternalSupplierDashboardPage;

public class InternalSupplierPropertiesMappingPage<T extends WebDriver> extends BasePage<T> {
    public InternalSupplierPropertiesMappingPage(Session<T> session) {
        super(session);
    }

    private static final Locator PROPERTIES_MAPPING_MENU = new Locator("PROPERTIES_MAPPING_MENU", By.xpath("//span[text()='Properties Mapping']"));
    private static final Locator MAP_PROPERTIES_BUTTON = new Locator("MAP_PROPERTIES_BUTTON", By.xpath("//span[text()='Map Properties']"));
    private static final Locator PROPERTIES_MAPPING_LOG_BUTTON = new Locator("PROPERTIES_MAPPING_LOG_BUTTON", By.xpath("//span[text()='Properties Mapping Log']"));
    private static final Locator MAP_PROPERTIES_PAGE_TITLE = new Locator("MAP_PROPERTIES_PAGE_TITLE", By.xpath("//span[@id='title-page-id' and normalize-space()='Map Properties']"));
    private static final Locator PROPERTIES_MAPPING_LOG_PAGE_TITLE = new Locator("PROPERTIES_MAPPING_LOG_PAGE_TITLE", By.xpath("//span[@id='title-page-id' and normalize-space()='Properties Mapping Log']"));
    private static final Locator PROPOSE_ANOTHER_PROPERTY_BUTTON = new Locator("PROPOSE_ANOTHER_PROPERTY_BUTTON", By.xpath("//button[normalize-space()='Propose another property']"));
    private static final Locator PROPERTY_URL_INPUT = new Locator("PROPERTY_URL_INPUT", By.xpath("//*[@name='propertyUrl']"));
    private static final Locator SUBMIT_MAP_BUTTON = new Locator("SUBMIT_MAP_BUTTON", By.xpath("//*[@data-testid='submit-another-property']"));
    private static final Locator MAP_ACTION_OPTION = new Locator("MAP_ACTION_OPTION", By.xpath("//*[normalize-space()='Map']"));
    private static final Locator ADD_AS_NEW_ACTION_OPTION = new Locator("ADD_AS_NEW_ACTION_OPTION", By.xpath("//*[translate(normalize-space(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='add as new']"));
    private static final Locator VENDOR_DROPDOWN_BUTTON = new Locator("VENDOR_DROPDOWN_BUTTON", By.xpath("//*[@data-testid='dropdown-vendor-name']"));
    private static final Locator SEARCH_INPUT_VENDOR_PROPERTY = new Locator("SEARCH_INPUT_VENDOR_PROPERTY", By.xpath("//input[@name='vendorId']"));
    private static final Locator SEARCH_BY_VENDOR_PROPERTY_ID = new Locator("SEARCH_BY_VENDOR_PROPERTY_ID", By.xpath("//*[@id='search-by-vendor-property-id']"));

    public void clickPropertiesMappingMenu() {
        driverTools.click(PROPERTIES_MAPPING_MENU);
    }

    public void clickMapPropertiesButton() {
        driverTools.click(MAP_PROPERTIES_BUTTON);
    }

    public void clickPropertiesMappingLogButton() {
        driverTools.click(PROPERTIES_MAPPING_LOG_BUTTON);
    }

    public void goToMapPropertiesPage() {
        clickPropertiesMappingMenu();
        clickMapPropertiesButton();
        driverTools.waitForElementToAppear(MAP_PROPERTIES_PAGE_TITLE, 15);
    }

    public void goToPropertiesMappingLogPage() {
        clickPropertiesMappingMenu();
        clickPropertiesMappingLogButton();
        driverTools.waitForElementToAppear(PROPERTIES_MAPPING_LOG_PAGE_TITLE, 15);
    }

    public boolean isMapPropertiesPageLoaded() {
        return driverTools.waitForElementToAppear(MAP_PROPERTIES_PAGE_TITLE, 15).isPresent();
    }

    public boolean isPropertiesMappingLogPageLoaded() {
        return driverTools.waitForElementToAppear(PROPERTIES_MAPPING_LOG_PAGE_TITLE, 15).isPresent();
    }

    private static Locator selectActionButtonByVendorPropertyId(String vendorPropertyId, int propertyIdIndex, int actionIndex) {
        String xpath = "//tr[./td[" + propertyIdIndex + "][normalize-space()='" + vendorPropertyId + "']]/td[" + actionIndex + "]//*[contains(normalize-space(),'Select Action') and contains(@class,'Text')]";
        return new Locator("SELECT_ACTION_" + vendorPropertyId, By.xpath(xpath));
    }

    private static Locator selectActionButtonByPropertyName(String propertyName, int propertyNameIndex, int actionIndex) {
        String xpath = "//tr[./td[" + propertyNameIndex + "][normalize-space()='" + propertyName + "']]/td[" + actionIndex + "]//*[contains(normalize-space(),'Select Action') and contains(@class,'Text')]";
        return new Locator("SELECT_ACTION_" + propertyName.replace(" ", "_"), By.xpath(xpath));
    }

    public void clickSelectActionMapByPropertyName(String propertyName) {
        int propertyNameIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Property Name");
        int actionIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Action");
        Locator selectActionButton = selectActionButtonByPropertyName(propertyName, propertyNameIndex, actionIndex);
        driverTools.waitForElementToBeClickable(selectActionButton, 10);
        driverTools.scrollElementToCenter(selectActionButton);
        driverTools.clickWithJavascriptExecutor(selectActionButton);
        driverTools.waitForElementToAppear(MAP_ACTION_OPTION, 5);
        driverTools.clickWithJavascriptExecutor(MAP_ACTION_OPTION);
        driverTools.switchToNewTab();
        driverTools.waitForWebToLoad();
    }

    public void clickSelectActionAddAsNewByPropertyName(String propertyName) {
        int propertyNameIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Property Name");
        int actionIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Action");
        Locator selectActionButton = selectActionButtonByPropertyName(propertyName, propertyNameIndex, actionIndex);
        driverTools.waitForElementToBeClickable(selectActionButton, 10);
        driverTools.scrollElementToCenter(selectActionButton);
        driverTools.clickWithJavascriptExecutor(selectActionButton);
        driverTools.waitForElementToAppear(ADD_AS_NEW_ACTION_OPTION, 5);
        driverTools.clickWithJavascriptExecutor(ADD_AS_NEW_ACTION_OPTION);
    }

    public String getPropertyNameToMap(List<String> propertyNames, String excludePropertyName) {
        for (String propertyName : propertyNames) {
            if (!propertyName.equalsIgnoreCase(excludePropertyName)) {
                return propertyName;
            }
        }
        throw new IllegalStateException("No property name available to map after excluding: " + excludePropertyName);
    }

    public String getLongLatByPropertyName(String propertyName) {
        int propertyNameIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Property Name");
        int longLatIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Long Lat");
        String xpath = "//tr[./td[" + propertyNameIndex + "][normalize-space()='" + propertyName + "']]/td[" + longLatIndex + "]";
        Locator longLatCell = new Locator("LONG_LAT_" + propertyName.replace(" ", "_"), By.xpath(xpath));
        String longLat = driverTools.waitForElementToAppear(longLatCell, 10)
                .map(WebElement::getText)
                .orElse("")
                .trim();
        logger.log("[MapProperties] long lat for " + propertyName + ": " + longLat);
        return longLat;
    }

    public Map<String, String> getPropertyLongLatMap(List<String> propertyNames) {
        Map<String, String> propertyLongLatMap = new LinkedHashMap<>();
        for (String propertyName : propertyNames) {
            propertyLongLatMap.put(propertyName, getLongLatByPropertyName(propertyName));
        }
        logger.log("[MapProperties] property long lat map: " + propertyLongLatMap);
        return propertyLongLatMap;
    }

    public String getVendorPropertyIdByPropertyName(String propertyName) {
        int propertyNameIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Property Name");
        int vendorPropertyIdIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Vendor Property ID");
        String xpath = "//tr[./td[" + propertyNameIndex + "][normalize-space()='" + propertyName + "']]/td[" + vendorPropertyIdIndex + "]";
        Locator vendorPropertyIdCell = new Locator("VENDOR_PROPERTY_ID_" + propertyName.replace(" ", "_"), By.xpath(xpath));
        String vendorPropertyId = driverTools.waitForElementToAppear(vendorPropertyIdCell, 10)
                .map(WebElement::getText)
                .orElse("")
                .trim();
        logger.log("[MapProperties] vendor property id for " + propertyName + ": " + vendorPropertyId);
        return vendorPropertyId;
    }

    public void clickSelectActionMapByVendorPropertyId(String vendorPropertyId) {
        int propertyIdIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Vendor Property ID");
        int actionIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Action");
        Locator selectActionButton = selectActionButtonByVendorPropertyId(vendorPropertyId, propertyIdIndex, actionIndex);
        driverTools.waitForElementToBeClickable(selectActionButton, 10);
        driverTools.scrollElementToCenter(selectActionButton);
        driverTools.clickWithJavascriptExecutor(selectActionButton);
        driverTools.waitForElementToAppear(MAP_ACTION_OPTION, 5);
        driverTools.clickWithJavascriptExecutor(MAP_ACTION_OPTION);
        driverTools.switchToNewTab();
        driverTools.waitForWebToLoad();
    }

    public void clickProposeAnotherProperty() {
        driverTools.waitForElementToBeClickable(PROPOSE_ANOTHER_PROPERTY_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(PROPOSE_ANOTHER_PROPERTY_BUTTON);
    }

    public void inputPropertyUrl(String propertyUrl) {
        driverTools.waitForElementToAppear(PROPERTY_URL_INPUT, 10);
        driverTools.clearInput(PROPERTY_URL_INPUT);
        driverTools.sendKeys(PROPERTY_URL_INPUT, propertyUrl);
    }

    public void confirmMerging() {
        driverTools.waitForElementToAppear(SUBMIT_MAP_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(SUBMIT_MAP_BUTTON);
        driverTools.waitForElementToDisappear(SUBMIT_MAP_BUTTON, 10);
    }

    public void switchToSupplierDashboardTab() {
        driverTools.switchToWindowContainingUrl("supplier-dashboard");
        driverTools.waitForWebToLoad();
    }

    public List<String> getPropertyNamesFromTable(int maxCount) {
        int propertyNameIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Property Name");
        List<String> propertyNames = new ArrayList<>();
        List<WebElement> rows = driverTools.getDriver().findElements(By.xpath("//tbody/tr"));
        int limit = Math.min(maxCount, rows.size());
        for (int i = 0; i < limit; i++) {
            try {
                WebElement nameCell = rows.get(i).findElement(By.xpath("./td[" + propertyNameIndex + "]"));
                String propertyName = nameCell.getText().trim();
                if (!propertyName.isEmpty()) {
                    propertyNames.add(propertyName);
                }
            } catch (StaleElementReferenceException e) {
                rows = driverTools.getDriver().findElements(By.xpath("//tbody/tr"));
                i--;
            }
        }
        logger.log("[MapProperties] property names from table: " + propertyNames);
        return propertyNames;
    }

    public void filterByVendorName(String vendorName) {
        driverTools.waitForElementToAppear(VENDOR_DROPDOWN_BUTTON);
        driverTools.clickWithJavascriptExecutor(VENDOR_DROPDOWN_BUTTON);
        Locator vendorOption = vendorOption(vendorName);
        driverTools.waitForElementToAppear(vendorOption, 10);
        driverTools.clickWithJavascriptExecutor(vendorOption);
    }

    private static Locator vendorOption(String vendorName) {
        return new Locator("VENDOR_OPTION_" + vendorName, By.xpath(String.format(
                "//*[contains(@data-testid,'dropdown-vendor-name')]/*[normalize-space()='%s']",
                vendorName)));
    }

    public void inputVendorPropertyIdInLog(String vendorPropertyId) {
        driverTools.waitForElementToAppear(SEARCH_INPUT_VENDOR_PROPERTY, 5);
        driverTools.scrollElementToCenter(SEARCH_INPUT_VENDOR_PROPERTY);
        driverTools.click(SEARCH_INPUT_VENDOR_PROPERTY);
        driverTools.clearInput(SEARCH_INPUT_VENDOR_PROPERTY);
        driverTools.sendKeys(SEARCH_INPUT_VENDOR_PROPERTY, vendorPropertyId);
    }

    public boolean isDatePresentInTable(int dayOffset) {
        String expectedDateTxt = getTableDateFormat(dayOffset);
        return InternalSupplierDashboardPage.getInstance(session).isTextPresentInTable(expectedDateTxt);
    }

    private String getTableDateFormat(int dayOffset) {
        LocalDate targetDate = LocalDate.now().plusDays(dayOffset);
        return targetDate.format(DateTimeFormatter.ofPattern("EEEE, d MMM yyyy", Locale.ENGLISH));
    }

    public void searchByVendorPropertyId(String vendorPropertyId) {
        driverTools.waitForElementToAppear(SEARCH_BY_VENDOR_PROPERTY_ID, 5);
        driverTools.scrollElementToCenter(SEARCH_BY_VENDOR_PROPERTY_ID);
        driverTools.click(SEARCH_BY_VENDOR_PROPERTY_ID);
        driverTools.clearInput(SEARCH_BY_VENDOR_PROPERTY_ID);
        driverTools.sendKeys(SEARCH_BY_VENDOR_PROPERTY_ID, vendorPropertyId + Keys.ENTER);
    }

    public String getPropertyNameByVendorPropertyId(String vendorPropertyId) {
        int vendorPropertyIdIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Vendor Property ID");
        int propertyNameIndex = InternalSupplierDashboardPage.getInstance(session).getIndexOfColumnTitle("Property Name");
        String xpath = "//tr[./td[" + vendorPropertyIdIndex + "][normalize-space()='" + vendorPropertyId + "']]/td[" + propertyNameIndex + "]";
        Locator propertyNameCell = new Locator("PROPERTY_NAME_" + vendorPropertyId, By.xpath(xpath));
        String propertyName = driverTools.waitForElementToAppear(propertyNameCell, 10)
                .map(WebElement::getText)
                .orElse("")
                .trim();
        logger.log("[MapProperties] property name for vendor property id " + vendorPropertyId + ": " + propertyName);
        return propertyName;
    }

    public String getActionTypeByVendorPropertyIdAndPropertyName(String vendorPropertyId, String propertyName) {
        InternalSupplierDashboardPage<?> dashboardPage = InternalSupplierDashboardPage.getInstance(session);
        int vendorPropertyIdIndex = dashboardPage.getIndexOfColumnTitle("Vendor Property ID");
        int propertyNameIndex = dashboardPage.getIndexOfColumnTitle("Vendor Property Name");
        int actionTypeIndex = dashboardPage.getIndexOfColumnTitle("Action Type");
        String xpath = "//tr[./td[" + vendorPropertyIdIndex + "][normalize-space()='" + vendorPropertyId + "']"
                + " and ./td[" + propertyNameIndex + "][normalize-space()='" + propertyName + "']]/td[" + actionTypeIndex + "]";
        Locator actionTypeCell = new Locator(
                "ACTION_TYPE_" + vendorPropertyId + "_" + propertyName.replace(" ", "_"),
                By.xpath(xpath));
        String actionType = driverTools.waitForElementToAppear(actionTypeCell, 10)
                .map(WebElement::getText)
                .orElse("")
                .trim();
        logger.log("[PropertiesMappingLog] action type for " + propertyName + " (" + vendorPropertyId + "): " + actionType);
        return actionType;
    }
}
