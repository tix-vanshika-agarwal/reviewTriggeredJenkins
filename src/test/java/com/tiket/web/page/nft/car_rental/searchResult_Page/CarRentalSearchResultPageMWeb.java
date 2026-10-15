package com.tiket.web.page.nft.car_rental.searchResult_Page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CarRentalSearchResultPageMWeb<T extends WebDriver> extends CarRentalSearchResultPageWeb<T> {
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", AppiumBy.xpath("//button[contains(@class,'SRPHeaderMobile_change_button')]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.xpath("(//button[contains(@class,'Button_variant_primary')])[2]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", AppiumBy.xpath("//button[contains(@class,'SearchForm_search_button')]"));
    private static final Locator SELF_DRIVE = new Locator("SELF_DRIVE", AppiumBy.xpath("(//span[contains(@class, 'Text_text') and (contains(text(), 'Self Drive') or contains(text(), 'Lepas Kunci'))])"));
    private static final Locator WITH_DRIVER = new Locator("WITH_DRIVER", AppiumBy.xpath("(//span[contains(@class, 'Text_text') and (contains(text(), 'With Driver') or contains(text(), 'Dengan Sopir'))])"));
    private static final Locator LOCATION_INPUT_TEXT = new Locator("LOCATION_INPUT_TEXT", new By.ByXPath("//*[contains(@class, 'LocationInput_input')]"));
    private static final Locator LOCATION_INPUT = new Locator("LOCATION_INPUT",AppiumBy.xpath("//input[@type='text' and @autocomplete='off' and (contains(@placeholder, 'city') or contains(@placeholder, 'kota'))]"));
    private static final Locator SET_PICKUP_LOCATION_BUTTON = new Locator("SET_PICKUP_LOCATION_BUTTON",new By.ByXPath("//button[.//*[contains(@class,'Button_truncation_not_applied') and (contains(text(),'Set Pickup Location') or contains(text(),'Atur Lokasi Jemput'))]]"));
    private static final Locator DATE_FORM_SRP = new Locator("DATE_FORM_SRP", AppiumBy.xpath("//*[contains(@class, 'DateInput_input_date')]"));
    private static final Locator FILTER_AND_SORT_BTN = new Locator("FILTER_AND_SORT_BTN", new By.ByXPath("(//span[normalize-space(text())='Filter & Sort' or normalize-space(text())='Filter & Urutkan'])"));

    public CarRentalSearchResultPageMWeb(Session<T> session) {
        super(session); 
    }

    @Override
    public void editSearchResult(String location, String newDate) {
        driverTools.waitForElementToAppear(EDIT_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.clickWithJavascriptExecutor(LOCATION_INPUT_TEXT);
        driverTools.waitForElementToAppear(LOCATION_INPUT).ifPresent(input -> {
            driverTools.clear(input);
            driverTools.sendKeys(input, location);
        });
        Locator LOCATION_TEXT = getLocationTextLocator(location);
        driverTools.waitForElementToAppear(LOCATION_TEXT);
        driverTools.clickWithJavascriptExecutor(LOCATION_TEXT);
        if (driverTools.isElementPresent(SET_PICKUP_LOCATION_BUTTON)) {
            driverTools.waitForElementToAppear(SET_PICKUP_LOCATION_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
        }

        driverTools.waitForElementToAppear(DATE_FORM_SRP).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.clickWithJavascriptExecutor(getDateLocator(newDate));
        driverTools.waitForElementToAppear(SAVE_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(SEARCH_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    @Override
        public void clickFilterBtn() {
        driverTools.waitForElementToBeClickable(FILTER_AND_SORT_BTN);
        driverTools.click(FILTER_AND_SORT_BTN);
    }

    public void editTotalDaysQueryParam(String queryParam, String newValue) {
        driverTools.editUrlQueryParam(queryParam, newValue);
    }

    @Override
    public void changeDriverType(String driverType, String location, String newDate) {
        driverTools.waitForElementToAppear(EDIT_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
        if (driverType.equals("self-drive")) {
            driverTools.waitForElementToAppear(SELF_DRIVE).ifPresent(driverTools::clickWithJavascriptExecutor);
        } else if (driverType.equals("with-driver")) {
            driverTools.waitForElementToAppear(WITH_DRIVER).ifPresent(driverTools::clickWithJavascriptExecutor);
        }
        driverTools.clickWithJavascriptExecutor(LOCATION_INPUT_TEXT);
        driverTools.waitForElementToAppear(LOCATION_INPUT).ifPresent(input -> {
            driverTools.clear(input);
            driverTools.sendKeys(input, location);
        });
        Locator LOCATION_TEXT = getLocationTextLocator(location);
        driverTools.waitForElementToAppear(LOCATION_TEXT);
        driverTools.clickWithJavascriptExecutor(LOCATION_TEXT);
        if (driverTools.isElementPresent(SET_PICKUP_LOCATION_BUTTON)) {
            driverTools.waitForElementToAppear(SET_PICKUP_LOCATION_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
        }
        driverTools.waitForElementToAppear(DATE_FORM_SRP).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToPresence(getDateLocator(newDate));
        driverTools.clickWithJavascriptExecutor(getDateLocator(newDate));
        driverTools.waitForElementToAppear(SAVE_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(SEARCH_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }
}