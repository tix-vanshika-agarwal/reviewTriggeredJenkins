package com.tiket.web.dashboard.page.flight;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FlightAddCountryFormPage<T extends WebDriver> extends BasePage<T> {

    public FlightAddCountryFormPage(Session<T> session) {
        super(session);
    }

    private static final Locator COUNTRY_CODE_BAR = new Locator("COUNTRY_CODE_BAR", By.xpath("//input[contains(@name,'code')]"));
    private static final Locator COUNTRY_NAME_BAR = new Locator("COUNTRY_NAME_BAR", By.xpath("//input[contains(@name,'name')]"));
    private static final Locator COUNTRY_CURRENCY_BAR = new Locator("COUNTRY_CURRENCY_BAR", By.xpath("//input[contains(@name,'currency')]"));
    /** First {@code tixselect} after currency — scopes region away from other page selects. */
    private static final String REGION_TIXSELECT_XPATH =
            "//input[contains(@name,'currency')]/following::div[@class='tixselect'][1]";
    private static final Locator COUNTRY_REGION_DROPDOWN = new Locator("COUNTRY_REGION_DROPDOWN",
            By.xpath(REGION_TIXSELECT_XPATH));
    private static final Locator COUNTRY_REGION_SELECT_ARROW = new Locator("COUNTRY_REGION_SELECT_ARROW",
            By.xpath(REGION_TIXSELECT_XPATH + "//span[contains(@class,'Select-arrow-zone')]"));
    private static final Locator COUNTRY_REGION_DROPDOWN_ITEM = new Locator("COUNTRY_REGION_DROPDOWN_ITEM",
            By.xpath("//div[contains(@class,'Select-menu')]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator SUBMIT_BTN = new Locator("SUBMIT_BTN", By.xpath("//div[contains(@class,'row')]//span[contains(@class,'MuiButton-label')]"));
    private static final Locator CLEAR_DROPDOWN_VALUE = new Locator("CLEAR_DROPDOWN_VALUE",
            By.xpath(REGION_TIXSELECT_XPATH + "//*[@class='Select-clear-zone']"));

    public void fillCountryDetails(String countryCode, String countryName, String countryCurrency) {
        driverTools.clearInput(COUNTRY_CODE_BAR);
        driverTools.sendKeys(COUNTRY_CODE_BAR, countryCode);

        driverTools.clearInput(COUNTRY_NAME_BAR);
        driverTools.sendKeys(COUNTRY_NAME_BAR, countryName);

        driverTools.clearInput(COUNTRY_CURRENCY_BAR);
        driverTools.sendKeys(COUNTRY_CURRENCY_BAR, countryCurrency);

        if (driverTools.waitForElementToPresence(CLEAR_DROPDOWN_VALUE).isPresent()) {
            driverTools.click(CLEAR_DROPDOWN_VALUE);
        }
        selectRandomDropdownOption();

        driverTools.click(SUBMIT_BTN);
    }

    public void selectRandomDropdownOption() {
        driverTools.scrollIntoWebViewOptional(COUNTRY_REGION_DROPDOWN);
        driverTools.waitForElementToAppear(COUNTRY_REGION_SELECT_ARROW, 5);
        driverTools.click(COUNTRY_REGION_SELECT_ARROW);
        if (!driverTools.waitForElementToAppear(COUNTRY_REGION_DROPDOWN_ITEM, 2).isPresent()) {
            driverTools.clickElementByCoordinateDWeb(COUNTRY_REGION_SELECT_ARROW);
        }
        driverTools.waitForElementToAppear(COUNTRY_REGION_DROPDOWN_ITEM, 5).orElseThrow();

        List<WebElement> dropdownOptions = driverTools.elementsUntilExist(COUNTRY_REGION_DROPDOWN_ITEM).orElseThrow();
        int maxVisibleIndex = Math.min(4, dropdownOptions.size() - 1);
        WebElement option = dropdownOptions.get(CoreFunctions.generateRandomInt(0, maxVisibleIndex));
        try {
            driverTools.click(option);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(option);
        }
    }
}


