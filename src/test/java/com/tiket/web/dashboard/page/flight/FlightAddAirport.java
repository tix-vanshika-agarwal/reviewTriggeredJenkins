package com.tiket.web.dashboard.page.flight;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlightAddAirport<T extends WebDriver> extends BasePage<T> {

    public FlightAddAirport(Session<T> session) {
        super(session);
    }

    private static final Locator LANGUAGE_DROPDOWN = new Locator("LANGUAGE_DROPDOWN", By.id("mui-component-select-name[0].lang"));
    private static final Locator CONTENT_BAR = new Locator("CONTENT_BAR", By.xpath("//input[@name='name[0].content' and @type='text']"));
    private static final Locator ALIAS_INPUT_CONTAINER = new Locator("ALIAS_INPUT_CONTAINER", By.id("alias"));
    private static final Locator ALIAS_INPUT = new Locator("ALIAS_INPUT",
            By.xpath("//div[@id='alias']//ul[contains(@class,'InputTag__List')]//input"));
    private static final Locator CODE_INPUT = new Locator("CODE_INPUT", By.xpath("//input[@id='code' and @name='code' and @type='text']"));
    private static final Locator LATITUDE_FIELD = new Locator("LATITUDE_FIELD", By.xpath("//input[@id='latitude' and @name='latitude']"));
    private static final Locator LONGITUDE_FIELD = new Locator("LONGITUDE_FIELD", By.xpath("//input[@id='longitude' and @name='longitude']"));
    private static final Locator DROPDOWN_OPTIONS = new Locator("DROPDOWN_OPTIONS", By.xpath("//ul[@role='listbox']//li[@role='option']"));
    private static final Locator COUNTRY_DROPDOWN = new Locator("COUNTRY_DROPDOWN", By.xpath("//input[@id='countryCode']/ancestor::div[contains(@class,'Select-control')]"));
    private static final Locator CITY_DROPDOWN = new Locator("CITY_DROPDOWN", By.xpath("//input[@id='cityCode']/ancestor::div[contains(@class,'Select-control')]"));
    private static final Locator CITY_DROPDOWN_ENABLED = new Locator("CITY_DROPDOWN_ENABLED",
            By.xpath("//input[@id='cityCode']/ancestor::div[contains(@class,'Select') and not(contains(@class,'is-disabled'))]//div[contains(@class,'Select-control')]"));
    private static final Locator SELECT_MENU_OPTIONS = new Locator("SELECT_MENU_OPTIONS",
            By.xpath("//div[contains(@class,'Select-menu')]//div[contains(@class,'VirtualizedSelectOption')]"));
    private static final Locator SUBMIT_BTN = new Locator("SUBMIT_BTN", By.xpath("//button[@type='submit' and .//span[text()='Submit']]"));
    private static final Locator EDIT_BTN = new Locator("EDIT_BTN", By.xpath("(//button[contains(@class,'btn-warning')])[1]"));
    private static final int MAX_LOCATION_RETRIES = 2;
    private static final Locator LOADING_OVERLAY = new Locator("LOADING_OVERLAY", By.xpath("//div[@class='loading-overlay']"));

    /**
     * With some preprod country/city records (e.g. Albania / Tirana) the airport is saved but the form never closes
     * and shows no error. When the form stays open, go back to the table: if the airport was saved, continue from
     * there; otherwise reopen an empty form and try again with another country.
     */
    public void fillAirportDetails(String content, String alias, String code, String latitude, String longitude) {
        Set<String> rejectedCountries = new HashSet<>();
        for (int attempt = 1; ; attempt++) {
            fillFields(content, alias, code, latitude, longitude);
            selectRandomLanguage();
            String country = selectRandomCountry(rejectedCountries);
            String city = selectRandomCity();
            if (submitForm()) {
                return;
            }
            String reason = "Airport form is still open after submit with country '" + country + "' and city '" + city + "'. "
                    + "Messages on form: [" + getFormMessages() + "], selected values: [" + getSelectedValues() + "]";
            logger.warn(reason);

            FlightAirportDashboardPage<T> airportDashboardPage = new FlightAirportDashboardPage<>(session);
            airportDashboardPage.clickAirport();
            if (airportDashboardPage.isAirportPresent(code)) {
                logger.warn("Airport '" + code + "' was saved although the form did not close, continuing from the table.");
                return;
            }
            if (attempt > MAX_LOCATION_RETRIES) {
                throw new IllegalStateException(reason + ". Airport '" + code + "' was not saved.");
            }
            logger.warn("Airport '" + code + "' was not saved, retrying on a new form with another country.");
            rejectedCountries.add(country);
            airportDashboardPage.clickAddCountryBtn();
        }
    }

    private void fillFields(String content, String alias, String code, String latitude, String longitude) {
        waitForFormToLoad();
        driverTools.sendKeys(CONTENT_BAR, content);
        driverTools.sendKeys(CODE_INPUT, code);
        driverTools.sendKeys(LATITUDE_FIELD, latitude);
        driverTools.sendKeys(LONGITUDE_FIELD, longitude);
        driverTools.scrollIntoWebViewOptional(ALIAS_INPUT_CONTAINER);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(ALIAS_INPUT, alias);
        WebElement aliasField = driverTools.element(ALIAS_INPUT).orElseThrow();
        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", aliasField);
        new Actions(driver).sendKeys(Keys.TAB).perform();
    }

    public void selectRandomLanguage() {
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 15);
        driverTools.click(LANGUAGE_DROPDOWN);
        driverTools.waitForElementToAppear(DROPDOWN_OPTIONS);
        List<WebElement> options = driverTools.elementsUntilExist(DROPDOWN_OPTIONS).orElseThrow();
        if (!options.isEmpty()) {
            options.get(CoreFunctions.generateRandomInt(0, options.size() - 1)).click();
        }
    }

    public String selectRandomCountry(Set<String> excludedCountries) {
        String country = selectRandomVirtualizedOption(COUNTRY_DROPDOWN, "country", excludedCountries);
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 10);
        return country;
    }

    public String selectRandomCity() {
        driverTools.waitForElementToAppear(CITY_DROPDOWN_ENABLED, 10).orElseThrow();
        return selectRandomVirtualizedOption(CITY_DROPDOWN, "city", Set.of());
    }

    private String selectRandomVirtualizedOption(Locator dropdown, String fieldName, Set<String> excludedTexts) {
        driverTools.scrollIntoWebViewOptional(dropdown);
        driverTools.click(dropdown);
        if (!driverTools.waitForElementToAppear(SELECT_MENU_OPTIONS, 3).isPresent()) {
            driverTools.clickElementByCoordinateDWeb(dropdown);
        }
        driverTools.waitForElementToAppear(SELECT_MENU_OPTIONS, 5).orElseThrow();
        List<WebElement> options = driverTools.elementsUntilExist(SELECT_MENU_OPTIONS).orElseThrow();
        List<WebElement> candidates = options.subList(0, Math.min(5, options.size())).stream()
                .filter(option -> !excludedTexts.contains(option.getText().trim()))
                .toList();
        if (candidates.isEmpty()) {
            throw new IllegalStateException("No " + fieldName + " option left to select, excluded: " + excludedTexts);
        }
        WebElement selected = candidates.get(CoreFunctions.generateRandomInt(0, candidates.size() - 1));
        String selectedText = selected.getText().trim();
        logger.log("Selecting " + fieldName + ": " + selectedText);
        try {
            driverTools.click(selected);
        } catch (ElementClickInterceptedException e) {
            driverTools.clickWithJavascriptExecutor(selected);
        }
        return selectedText;
    }

    public void updateAirport(String content, String alias, String code, String latitude, String longitude){
        driverTools.click(EDIT_BTN);
        driverTools.waitForElementToAppear(LOADING_OVERLAY, 5);
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 5);
        driverTools.clear(CONTENT_BAR);
        driverTools.clear(CODE_INPUT);
        driverTools.clear(LATITUDE_FIELD);
        driverTools.clear(LONGITUDE_FIELD);
        driverTools.sendKeys(CODE_INPUT, code);
        driverTools.sendKeys(LATITUDE_FIELD, latitude);
        driverTools.sendKeys(LONGITUDE_FIELD, longitude);
        driverTools.sendKeys(CONTENT_BAR, content);
        submitAndWaitForFormToClose();
    }

    private void waitForFormToLoad() {
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 15);
        driverTools.waitForElementToAppear(CONTENT_BAR, 10).orElseThrow(() ->
                new IllegalStateException("Airport form did not load"));
    }

    /**
     * The table reloads behind a loading overlay after submit. Callers act on the table right away,
     * so wait until the form is gone and fail here, with a clear reason, if it never closes.
     */
    private void submitAndWaitForFormToClose() {
        if (!submitForm()) {
            throw new IllegalStateException("Airport form is still open after submit, a field may be invalid or not selected. "
                    + "Messages on form: [" + getFormMessages() + "], selected values: [" + getSelectedValues() + "]");
        }
    }

    private boolean submitForm() {
        driverTools.click(SUBMIT_BTN);
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 15);
        boolean closed = driverTools.waitForElementToDisappear(SUBMIT_BTN, 15);
        driverTools.waitForElementToDisappear(LOADING_OVERLAY, 15);
        return closed;
    }

    private String getFormMessages() {
        return (String) ((JavascriptExecutor) driver).executeScript(
                "return Array.from(document.querySelectorAll(\".Mui-error, [class*='error'], [class*='Snackbar'], [class*='toast'], [role='alert']\"))"
                        + ".filter(e => e.offsetParent !== null && e.innerText && e.innerText.trim())"
                        + ".map(e => e.innerText.trim()).filter((t, i, a) => a.indexOf(t) === i).join(' | ');");
    }

    private String getSelectedValues() {
        return (String) ((JavascriptExecutor) driver).executeScript(
                "return Array.from(document.querySelectorAll(\".Select-value-label, .MuiSelect-select\"))"
                        + ".map(e => e.innerText.trim()).join(' | ');");
    }
}
