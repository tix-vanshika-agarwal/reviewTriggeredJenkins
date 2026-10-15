package com.tiket.web.dashboard.page.flight;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.List;

public class FlightAddAirlineFormPage <T extends WebDriver> extends BasePage<T> {

    public FlightAddAirlineFormPage(Session<T> session) {
        super(session);
    }

    private static final Locator AIRLINE_NAME = new Locator("AIRLINE_NAME", By.name("name"));
    private static final Locator AIRLINE_CODE = new Locator("AIRLINE_CODE", By.name("code"));
    private static final Locator AIRLINE_DISPLAY_NAME = new Locator("AIRLINE_DISPLAY_NAME", By.name("displayName"));
    private static final Locator AIRLINE_SHORT_NAME = new Locator("AIRLINE_SHORT_NAME", By.name("shortName"));
    private static final Locator AIRLINE_URL = new Locator("AIRLINE_URL", By.name("url"));
    private static final Locator LEGACY_AIRLINE_NAME = new Locator("LEGACY_AIRLINE_NAME", By.name("legacyAirlineName"));
    private static final Locator AIRLINE_ID = new Locator("AIRLINE_ID", By.name("legacyId"));
    private static final Locator IS_PARTNER_DROPDOWN = new Locator("IS_PARTNER_DROPDOWN", By.xpath("//div[@class='tixselect' and ./label[@for='partner']]//span[@class='Select-arrow-zone']"));
    private static final Locator IS_PARTNER_ITEM = new Locator("IS_PARTNER_ITEM", By.xpath("//*[text()='FALSE']"));
    private static final Locator INPUT_IMG = new Locator("INPUT_IMG", By.id("icon"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//span[contains(text(),'Submit')]"));
    private static final Locator IMG_UPLOAD = new Locator("IMG_UPLOAD", By.xpath("//input[@id='icon' and contains(@class, 'hidden')]"));
    private static final Locator AIRLINE_TYPE_DROPDOWN = new Locator("AIRLINE_TYPE_DROPDOWN", By.xpath("//div[@class='tixselect' and ./label[@for='airlineType']]//span[@class='Select-arrow-zone']"));
    private static final Locator DROPDOWN_ITEM = new Locator("DROPDOWN_ITEM", By.xpath("//div[contains(@class,'VirtualizedSelectOption')]"));

    public void addAirlineForm(String airlineName, String airlineCode, String airlineDisplayName, String airlineShortName, String airlineUrl, String legacyAirlineName, String legacyId, String imagePath) {
        driverTools.waitForElementToAppear(AIRLINE_NAME, 5);
        driverTools.sendKeys(AIRLINE_NAME, airlineName);

        driverTools.waitForElementToAppear(AIRLINE_CODE, 5);
        driverTools.sendKeys(AIRLINE_CODE, airlineCode);

        driverTools.waitForElementToAppear(AIRLINE_DISPLAY_NAME, 5);
        driverTools.sendKeys(AIRLINE_DISPLAY_NAME, airlineDisplayName);

        driverTools.waitForElementToAppear(AIRLINE_SHORT_NAME, 5);
        driverTools.sendKeys(AIRLINE_SHORT_NAME, airlineShortName);

        selectAirlineType();
        selectIsPartner();
        uploadImage(imagePath);

        driverTools.waitForElementToAppear(IMG_UPLOAD, 2);
        driverTools.waitForElementToAppear(AIRLINE_URL, 5);
        driverTools.sendKeys(AIRLINE_URL, airlineUrl);

        driverTools.waitForElementToAppear(LEGACY_AIRLINE_NAME, 5);
        driverTools.sendKeys(LEGACY_AIRLINE_NAME, legacyAirlineName);

        driverTools.waitForElementToAppear(AIRLINE_ID, 5);
        driverTools.sendKeys(AIRLINE_ID, legacyId);

        driverTools.waitForElementToAppear(SUBMIT_BUTTON, 5);
        driverTools.click(SUBMIT_BUTTON);
    }

    public void selectAirlineType() {
        driverTools.waitForElementToBeClickable(AIRLINE_TYPE_DROPDOWN, 5);
        driverTools.click(AIRLINE_TYPE_DROPDOWN);
        List<WebElement> listDropdownItem = driverTools.elements(DROPDOWN_ITEM).orElseThrow();
        WebElement targetElement = listDropdownItem.get(CoreFunctions.generateRandomInt(0,listDropdownItem.size()-1));
        driverTools.waitForElementToAppear(targetElement, 5);
        driverTools.click(targetElement);
    }

    public void uploadImage(String imagePath) {
        File file = new File(imagePath);
        driverTools.waitForElementToAppear(INPUT_IMG, 5);
        driverTools.sendKeys(INPUT_IMG, file.getAbsolutePath());
    }

    public void selectIsPartner() {
        driverTools.waitForElementToBeClickable(IS_PARTNER_DROPDOWN, 5);
        driverTools.click(IS_PARTNER_DROPDOWN);
        driverTools.waitForElementToAppear(IS_PARTNER_ITEM, 5);
        driverTools.click(IS_PARTNER_ITEM);
    }

    public void updateAirlineForm(String updatedAirlineName, String updatedAirlineCode, String updatedAirlineDisplayName, String updatedAirlineShortName, String updatedAirlineUrl, String updatedLegacyAirlineName, String updatedLegacyId) {
        driverTools.waitForElementToAppear(AIRLINE_NAME, 5);
        driverTools.clearInput(AIRLINE_NAME);
        driverTools.sendKeys(AIRLINE_NAME, updatedAirlineName);

        driverTools.waitForElementToAppear(AIRLINE_CODE, 5);
        driverTools.clearInput(AIRLINE_CODE);
        driverTools.sendKeys(AIRLINE_CODE, updatedAirlineCode);

        driverTools.waitForElementToAppear(AIRLINE_DISPLAY_NAME, 5);
        driverTools.clearInput(AIRLINE_DISPLAY_NAME);
        driverTools.sendKeys(AIRLINE_DISPLAY_NAME, updatedAirlineDisplayName);

        driverTools.waitForElementToAppear(AIRLINE_SHORT_NAME, 5);
        driverTools.clearInput(AIRLINE_SHORT_NAME);
        driverTools.sendKeys(AIRLINE_SHORT_NAME, updatedAirlineShortName);

        driverTools.waitForElementToAppear(AIRLINE_URL, 5);
        driverTools.clearInput(AIRLINE_URL);
        driverTools.sendKeys(AIRLINE_URL, updatedAirlineUrl);

        driverTools.waitForElementToAppear(LEGACY_AIRLINE_NAME, 5);
        driverTools.clearInput(LEGACY_AIRLINE_NAME);
        driverTools.sendKeys(LEGACY_AIRLINE_NAME, updatedLegacyAirlineName);

        driverTools.waitForElementToAppear(AIRLINE_ID, 5);
        driverTools.clearInput(AIRLINE_ID);
        driverTools.sendKeys(AIRLINE_ID, updatedLegacyId);

        driverTools.waitForElementToAppear(SUBMIT_BUTTON, 5);
        driverTools.click(SUBMIT_BUTTON);
    }
}