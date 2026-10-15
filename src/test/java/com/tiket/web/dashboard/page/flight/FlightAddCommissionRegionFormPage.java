package com.tiket.web.dashboard.page.flight;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class FlightAddCommissionRegionFormPage<T extends WebDriver> extends BasePage<T> {
    public FlightAddCommissionRegionFormPage(Session<T> session) {
        super(session);
    }

    protected static final Locator LOADING_OVERLAY = new Locator("LOADING_OVERLAY", new By.ByXPath("//div[contains(@class,'loading-overlay')]"));
    protected static final Locator SUBMIT_BTN = new Locator("SUBMIT_BTN", new By.ByXPath("//button[@type='submit']"));
    protected static final Locator NAME_FIELDS = new Locator("NAME_FIELDS", new By.ByXPath("//input[@id='name']"));
    protected static final Locator COUNTRY_INPUT = new Locator("COUNTRY_INPUT", new By.ByXPath("//input[@id='countries']"));
    protected static final Locator COUNTRY_DROPDOWN_ARROW = new Locator("COUNTRY_DROPDOWN_ARROW", new By.ByXPath("//div[contains(@class,'tixselect') and ./label[@for='countries']]//span[contains(@class,'Select-arrow-zone')]"));
    protected static final Locator COUNTRY_DROPDOWN_SELECTION = new Locator("COUNTRY_DROPDOWN_SELECTION", new By.ByXPath("//div[contains(@class,'tixselect') and ./label[@for='countries']]//div[contains(@class,'VirtualizedSelectOption')]"));
    protected static final Locator COUNTRY_SELECTED_VALUE = new Locator("COUNTRY_SELECTED_VALUE", new By.ByXPath("//div[contains(@class,'tixselect') and ./label[@for='countries']]//span[contains(@class,'Select-value-label')]"));
    protected static final Locator COUNTRY_CLEAR_ALL = new Locator("COUNTRY_CLEAR_ALL", new By.ByXPath("//div[contains(@class,'tixselect') and ./label[@for='countries']]//span[contains(@class,'Select-clear-zone')]"));

    public Map<String, String> fillDetails(String name, String countries) {
        Map<String, String> createdCommissionRegion = new HashMap<>();

        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 10);

        driverTools.waitForElementToBeClickable(NAME_FIELDS, 5).orElseThrow();

        name = fillName(name);
        createdCommissionRegion.put("name", name);

        countries = chooseCountries(countries);
        createdCommissionRegion.put("countries", countries);

        return createdCommissionRegion;
    }

    /**
     * The region name is the only handle the dashboard filter offers, so 'random'
     * generates a unique one instead of colliding with earlier runs.
     */
    protected String fillName(String name) {
        driverTools.sendKeys(NAME_FIELDS, name);
        return driverTools.getValue(NAME_FIELDS);
    }

    /**
     * Accepts a pipe separated list, where every entry may also be 'random'.
     * Pipes rather than commas because the test data format splits params on comma.
     * The result is joined the way the dashboard grid renders the Country column.
     */
    protected String chooseCountries(String countries) {
        List<String> selectedCountries = new ArrayList<>();
        for (String country : countries.split("\\|")) {
            selectedCountries.add(chooseCountry(country.trim()));
        }
        return String.join(", ", selectedCountries);
    }

    private String chooseCountry(String country) {
        WebElement countryInput = driverTools.waitForElementToAppear(COUNTRY_INPUT, 5).orElseThrow();

        // Typing narrows the dropdown to the wanted country. For 'random' there is
        // nothing to type, so open the list untouched and type back whichever
        // country it happens to offer first.
        String targetText;
        if (country.equalsIgnoreCase("random")) {
            driverTools.waitForElementToAppear(COUNTRY_DROPDOWN_ARROW, 5).orElseThrow();
            driverTools.click(COUNTRY_DROPDOWN_ARROW);
            targetText = getFirstShownCountry(country);
            driverTools.sendKeys(countryInput, targetText);
        } else {
            driverTools.sendKeys(countryInput, country);
            targetText = getFirstShownCountry(country);
        }

        // The dropdown keeps its first option focused, so that is the one TAB commits.
        countryInput.sendKeys(Keys.TAB);

        return toDisplayedCountry(targetText);
    }

    /** Only the options the virtualized dropdown has rendered can be read. */
    private String getFirstShownCountry(String country) {
        List<WebElement> countrySelection = driverTools.elementsUntilExist(COUNTRY_DROPDOWN_SELECTION).orElse(List.of());
        if (countrySelection.isEmpty()) {
            throw new NoSuchElementException("There is no '"+country+"' country");
        }
        return driverTools.getText(countrySelection.get(0));
    }

    /** The dropdown labels countries as 'Kazakhstan (KZ)'; the grid lists plain 'Kazakhstan'. */
    protected String toDisplayedCountry(String countryOption) {
        String normalizedCountry = countryOption.replaceAll("\\s+", " ").trim();
        return normalizedCountry.replaceAll("\\s*\\([A-Z]{2}\\)$", "");
    }

    public void clickSubmitButton() {
        driverTools.waitForElementToAppear(SUBMIT_BTN, 5);
        driverTools.click(SUBMIT_BTN);
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 10);
    }
}
