package com.tiket.web.dashboard.page.flight;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FlightPriceAdjustmentAddNewFormPage <T extends WebDriver> extends BasePage<T> {
    public FlightPriceAdjustmentAddNewFormPage(Session<T> session) {
        super(session);
    }

    private static final Locator SUPPLIER_DROPDOWN = new Locator("SUPPLIER_DROPDOWN", By.xpath("(//span[contains(@class, 'Select-arrow-zone')])[1]"));
    private static final Locator AIRLINE_CLASS_DROPDOWN = new Locator("AIRLINE_CLASS_DROPDOWN", By.xpath("(//span[contains(@class, 'Select-arrow-zone')])[2]"));
    private static final Locator AIRLINE_DROPDOWN = new Locator("AIRLINE_DROPDOWN", By.xpath("(//span[contains(@class, 'Select-arrow-zone')])[3]"));
    private static final Locator COUNTRY_ORIGIN_DROPDOWN = new Locator("COUNTRY_ORIGIN_DROPDOWN", By.xpath("(//span[contains(@class, 'Select-arrow-zone')])[4]"));
    private static final Locator COUNTRY_DESTINATION_DROPDOWN = new Locator("COUNTRY_DESTINATION_DROPDOWN", By.xpath("(//span[contains(@class, 'Select-arrow-zone')])[5]"));
    private static final Locator TRANSIT_DROPDOWN = new Locator("TRANSIT_DROPDOWN", By.xpath("(//span[contains(@class, 'Select-arrow-zone')])[6]"));
    private static final Locator AIRPORT_ORIGIN_DROPDOWN = new Locator("AIRPORT_ORIGIN_DROPDOWN", By.xpath("(//span[contains(@class, 'Select-arrow-zone')])[7]"));
    private static final Locator AIRPORT_DESTINATION_DROPDOWN = new Locator("AIRPORT_DESTINATION_DROPDOWN", By.xpath("(//span[contains(@class, 'Select-arrow-zone')])[8]"));
    private static final Locator DATE_TIME_PICKER = new Locator("DATE_TIME_PICKER", By.xpath("(//div[contains(@class, 'MuiInputBase-root MuiOutlinedInput-root MuiInputBase-formControl')])[2]"));
    private static final Locator YEAR_MENU_SELECT = new Locator("YEAR_MENU_SELECT", By.xpath("//h6[contains(@class,'subtitle1')]"));
    private static final Locator YEAR_SELECTOR = new Locator("YEAR_SELECTOR", By.xpath("(//div[contains(@class,'yearSelected')]//following-sibling::*)[1]"));
    private static final Locator DATE_SELECT_OK_BTN = new Locator("DATE_SELECT_OK_BTN", By.xpath("//span[contains(text(),'OK')]"));
    private static final Locator MIN_PRICE = new Locator("MIN_PRICE", By.name("priceRange.minimumPrice"));
    private static final Locator MAX_PRICE = new Locator("MAX_PRICE", By.name("priceRange.maximumPrice"));
    private static final Locator MIN_DISCOUNT = new Locator("MIN_DISCOUNT", By.name("discount.percentage"));
    private static final Locator MAX_DISCOUNT = new Locator("MAX_DISCOUNT", By.name("discount.maximumValue"));
    private static final Locator MARKUP_VALUE_BTN = new Locator("MARKUP_VALUE_BTN", By.xpath("//button[contains(@class,'btn-fill')]"));
    private static final Locator MARKUP_VALUE = new Locator("MARKUP_VALUE", By.name("markup.value"));
    private static final Locator SUBMIT_BTN = new Locator("SUBMIT_BTN", By.xpath("//span[contains(text(),'Submit')]"));
    private static final Locator OPTIONS_MENU_BTN = new Locator("OPTIONS_MENU_BTN", By.xpath("(//button[contains(@class, '_tooltip')]//i[contains(@class,'fa-ellipsis-h')])[1]"));
    private static final Locator EDIT_BTN = new Locator("EDIT_BTN",By.xpath("//i[contains(@class, 'fa-pencil')]"));
    private static final String CLEAR_SUPPLIER_DROPDOWN = "span.Select-clear-zone";

    public Locator getDropdown(String dropdownName) {
        return new Locator("DROPDOWN_ITEM", By.xpath("//*[text()='" + dropdownName + "']"));
    }

    public void fillAirlineDetails(String supplier, String airlineClass, String airline) {
        driverTools.click(SUPPLIER_DROPDOWN);
        driverTools.click(getDropdown(supplier));
        driverTools.click(AIRLINE_CLASS_DROPDOWN);
        driverTools.click(getDropdown(airlineClass));
        driverTools.click(AIRLINE_DROPDOWN);
        driverTools.click(getDropdown(airline));
    }

    public void fillTripDetails(String countryOrigin, String countryDestination, String transit, String airportOrigin, String airportDestination) {
        driverTools.click(COUNTRY_ORIGIN_DROPDOWN);
        driverTools.click(getDropdown(countryOrigin));
        driverTools.click(COUNTRY_DESTINATION_DROPDOWN);
        driverTools.click(getDropdown(countryDestination));
        driverTools.click(TRANSIT_DROPDOWN);
        driverTools.click(getDropdown(transit));
        driverTools.click(AIRPORT_ORIGIN_DROPDOWN);
        driverTools.click(getDropdown(airportOrigin));
        driverTools.click(AIRPORT_DESTINATION_DROPDOWN);
        driverTools.click(getDropdown(airportDestination));
        selectDateTime();

    }

    public void selectDateTime(){
        driverTools.clickWithJavascriptExecutor(DATE_TIME_PICKER);
        driverTools.clickWithJavascriptExecutor(YEAR_MENU_SELECT);
        driverTools.clickWithJavascriptExecutor(YEAR_SELECTOR);
        driverTools.clickWithJavascriptExecutor(DATE_SELECT_OK_BTN);
    }

    public void fillAdjustmentRule(String minPrice, String maxPrice, String minDiscount, String maxDiscount, String markupValue) {
        driverTools.sendKeys(MIN_PRICE, minPrice);
        driverTools.sendKeys(MAX_PRICE, maxPrice);
        driverTools.sendKeys(MIN_DISCOUNT, minDiscount);
        driverTools.sendKeys(MAX_DISCOUNT, maxDiscount);
        driverTools.click(MARKUP_VALUE_BTN);
        driverTools.sendKeys(MARKUP_VALUE, markupValue);
    }

    public void editAdjustmentRule(String minPrice, String maxPrice, String minDiscount, String maxDiscount, String markupValue) {
        driverTools.sendKeys(MIN_PRICE, minPrice);
        driverTools.sendKeys(MAX_PRICE, maxPrice);
        driverTools.sendKeys(MIN_DISCOUNT, minDiscount);
        driverTools.sendKeys(MAX_DISCOUNT, maxDiscount);
        driverTools.sendKeys(MARKUP_VALUE, markupValue);
    }

    public void clickSubmitBtn(){
        driverTools.clickWithJavascriptExecutor(SUBMIT_BTN);
    }

    public void updateAdjustment(){
        driverTools.click(OPTIONS_MENU_BTN);
        driverTools.click(EDIT_BTN);
        driverTools.clear(MIN_PRICE);
        driverTools.clear(MAX_PRICE);
        driverTools.clear(MIN_DISCOUNT);
        driverTools.clear(MAX_DISCOUNT);
        driverTools.clear(MARKUP_VALUE);
        clearAllDropdowns();
    }

    public void clearAllDropdowns() {
        List<WebElement> clearIcons = driver.findElements(By.cssSelector(CLEAR_SUPPLIER_DROPDOWN));
        for (WebElement icon : clearIcons) {
            try {
                if (icon.isDisplayed() && icon.isEnabled()) {
                    icon.click();
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }
}