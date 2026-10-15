package com.tiket.web.dashboard.page.flight;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class FlightRescheduleChargePage<T extends WebDriver> extends BasePage<T> {
    public FlightRescheduleChargePage(Session<T> session) {
        super(session);
    }

    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[@class='account-card']"));
    private static final Locator ADD_NEW_RESCHEDULE_CHARGE_BTN = new Locator("ADD_NEW_RESCHEDULE_CHARGE_BTN", new By.ByXPath("//div[@class='custom-table__add-button-container']//button"));
    private static final Locator OK_PRIMARY_BUTTON = new Locator("OK_PRIMARY_BUTTON", new By.ByXPath("//div[@class='button']/button[contains(@class,'btn-primary')]"));
    private static final Locator LOADING_OVERLAY = new Locator("LOADING_OVERLAY", new By.ByXPath("//div[contains(@class,'loading-overlay')]"));

    public boolean isLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 3);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public void goToReschedulePageDashboard() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/flight-ms-v2/reschedule-charge");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/flight-ms-v2/reschedule-charge");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://www.tiket.com/admin/flight-ms-v2/reschedule-charge");
        }
    }

    public void clickAddRescheduleChargeBtn() {
        driverTools.waitForElementToAppear(ADD_NEW_RESCHEDULE_CHARGE_BTN, 5);
        driverTools.click(ADD_NEW_RESCHEDULE_CHARGE_BTN);
    }

    public Map<String, String> getDisplayedData(int targetIndex) {
        Map<String, String> displayedCampaign = new HashMap<>();

        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 5);
        driverTools.waitForElementToAppear(ADD_NEW_RESCHEDULE_CHARGE_BTN, 10).orElseThrow(() ->
                new IllegalStateException("Reschedule charge table is not shown, the page may still be on the form after submit"));

        String airlineCodes = getAirlineCode(targetIndex);
        displayedCampaign.put("airlineCodes", airlineCodes);

        String supplierCode = getSupplierCode(targetIndex);
        displayedCampaign.put("supplierCode", supplierCode);

        String currency = getCurrency(targetIndex);
        displayedCampaign.put("currency", currency);

        String chargeType = getChargeType(targetIndex);
        displayedCampaign.put("chargeType", chargeType);

        String value = getValue(targetIndex);
        displayedCampaign.put("value", value);

        String journeyType = getJourneyType(targetIndex);
        displayedCampaign.put("journeyType", journeyType);

        String route = getRoute(targetIndex);
        displayedCampaign.put("route", route);

        return displayedCampaign;
    }

    private String getAirlineCode(int index) {
        WebElement targetElement = getCellElement(getAirlineCodeTextLocatorByIndex(index));
        return driverTools.getText(targetElement);
    }

    private Locator getAirlineCodeTextLocatorByIndex(int index) {
        return new Locator("AIRLINE_CODE_TEXT_" + index, new By.ByXPath("//tbody//tr["+ index +"]/td[1]"));
    }

    private String getSupplierCode(int index) {
        WebElement targetElement = getCellElement(getSupplierCodeTextLocatorByIndex(index));
        return driverTools.getText(targetElement);
    }

    private Locator getSupplierCodeTextLocatorByIndex(int index) {
        return new Locator("SUPPLIER_CODE_TEXT_" + index, new By.ByXPath("//tbody//tr["+ index +"]/td[2]"));
    }

    private String getCurrency(int index) {
        WebElement targetElement = getCellElement(getCurrencyTextLocatorByIndex(index));
        return driverTools.getText(targetElement);
    }

    private Locator getCurrencyTextLocatorByIndex(int index) {
        return new Locator("CURRENCY_TEXT_" + index, new By.ByXPath("//tbody//tr["+ index +"]/td[5]//li[1]/strong"));
    }

    private String getChargeType(int index) {
        WebElement targetElement = getCellElement(getChargeTypeTextLocatorByIndex(index));
        return driverTools.getText(targetElement);
    }

    private Locator getChargeTypeTextLocatorByIndex(int index) {
        return new Locator("CHARGE_TYPE_TEXT_" + index, new By.ByXPath("//tbody//tr["+ index +"]/td[5]//li[2]/strong"));
    }

    private String getValue(int index) {
        WebElement targetElement = getCellElement(getValueTextLocatorByIndex(index));
        String value = driverTools.getText(targetElement).replaceAll("[^0-9]", "");
        return value;
    }

    private Locator getValueTextLocatorByIndex(int index) {
        return new Locator("VALUE_TEXT_" + index, new By.ByXPath("//tbody//tr["+ index +"]/td[5]//li[3]/strong"));
    }

    private String getJourneyType(int index) {
        WebElement targetElement = getCellElement(getJourneyTypeTextLocatorByIndex(index));
        return driverTools.getText(targetElement);
    }

    private Locator getJourneyTypeTextLocatorByIndex(int index) {
        return new Locator("JOURNEY_TYPE_TEXT_" + index, new By.ByXPath("//tbody//tr["+ index +"]/td[5]//li[4]/strong"));
    }

    private String getRoute(int index) {
        WebElement targetElement = getCellElement(getRouteTextLocatorByIndex(index));
        return driverTools.getText(targetElement);
    }

    private Locator getRouteTextLocatorByIndex(int index) {
        return new Locator("ROUTE_TEXT_" + index, new By.ByXPath("//tbody//tr["+ index +"]/td[3]"));
    }

    private WebElement getCellElement(Locator cellLocator) {
        return driverTools.waitForElementToAppear(cellLocator).orElseThrow(() ->
                new NoSuchElementException("Reschedule charge cell not found: " + cellLocator));
    }

    public void deleteRescheduleCharge(int index) {
        clickDeleteButton(index);
        clickOKButton();
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 5);
    }

    private void clickOKButton() {
        driverTools.waitForElementToAppear(OK_PRIMARY_BUTTON).orElseThrow();
        driverTools.click(OK_PRIMARY_BUTTON);
    }

    private void clickDeleteButton(int index) {
        driverTools.waitForElementToAppear(getDeleteButtonLocatorByIndex(index)).orElseThrow();
        driverTools.click(getDeleteButtonLocatorByIndex(index));
    }

    private Locator getDeleteButtonLocatorByIndex(int index) {
        return new Locator("DELETE_BUTTON" + index, new By.ByXPath("//tbody//tr["+ index +"]/td[6]//button[contains(@class,'btn-danger')]"));
    }

    public void clickEditButton(int index) {
        driverTools.waitForElementToAppear(getEditButtonLocatorByIndex(index)).orElseThrow();
        driverTools.click(getEditButtonLocatorByIndex(index));
    }

    private Locator getEditButtonLocatorByIndex(int index) {
        return new Locator("EDIT_BUTTON" + index, new By.ByXPath("//tbody//tr["+ index +"]/td[6]//button[contains(@class,'btn-warning')]"));
    }

    public boolean isExist(Map<String, String> rescheduleCharge, boolean expectedOutcome) {
        Locator targetRescheduleCharge = getRescheduleChargeLocator(rescheduleCharge);
        if (expectedOutcome) {
            driverTools.waitForElementToAppear(targetRescheduleCharge, 3);
        } else {
            driverTools.waitForElementToDisappear(targetRescheduleCharge, 3);
        }
        
        return driverTools.element(targetRescheduleCharge).isPresent();
    }

    private Locator getRescheduleChargeLocator(Map<String, String> rescheduleCharge) {
        return new Locator("RESCHEDULE_CHARGE", new By.ByXPath("//tbody/tr[" +
                                                                "./td[text()='"+rescheduleCharge.get("airlineCodes")+"'] and " +
                                                                "./td[text()='"+rescheduleCharge.get("supplierCode")+"'] and " +
                                                                "./td[text()='"+rescheduleCharge.get("route")+"'] and " +
                                                                "./td//li/strong[text()='"+rescheduleCharge.get("currency")+"'] and " +
                                                                "./td//li/strong[text()='"+rescheduleCharge.get("chargeType")+"'] and " +
                                                                "./td//li/strong[text()='"+rescheduleCharge.get("journeyType").toUpperCase()+"']]"));
    }
}
