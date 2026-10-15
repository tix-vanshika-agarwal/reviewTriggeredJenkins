package com.tiket.web.dashboard.page.ttd.CMS.CMSProductManagementEditProductPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import static com.tiket.core.CoreFunctions.generateRandomString;

import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.openqa.selenium.By.xpath;

public class CMSProductManagementEditProductPageWeb<T extends WebDriver> extends WebPage<T> {
    public CMSProductManagementEditProductPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSProductManagementEditProductPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSProductManagementEditProductPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSProductManagementEditProductPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator DESCRIPTION_INPUT_EN = new Locator("DESCRIPTION_INPUT_EN", By.xpath("(//div[@class='input-box active']/div[text()='Description']/following-sibling::input[@name='description'])[2]"));
    private static final Locator DESCRIPTION_INPUT_ID = new Locator("DESCRIPTION_INPUT_ID", By.xpath("(//div[@class='input-box active']/div[text()='Description']/following-sibling::input[@name='description'])[6]"));
    private static final Locator SAVE_AND_CLOSE_BUTTON = new Locator("SAVE_AND_CLOSE_BUTTON", By.xpath("//button[@data-testid='button-submit' and normalize-space()='Save & Close']"));
    private static final Locator SEAT_MAP_TYPE_DISPLAYED = new Locator("SEAT_MAP_TYPE_DISPLAYED", By.xpath("//section[@class='form-group product--package-management']//div[contains(@class,'tix-react-select__single-value')]"));
    private static final Locator SEAT_MAP_TYPE_DROPDOWN = new Locator("SEAT_MAP_TYPE_DROPDOWN", By.xpath("//section[@class='form-group product--package-management']//div[contains(@class,'tix-react-select__control')]"));
    private static final Locator SEAT_MAP_TYPE_DROPDOWN_SELECTION = new Locator("SEAT_MAP_TYPE_DROPDOWN_SELECTION", By.xpath("//div[contains(@class,'tix-react-select__option')]"));
    private static final Locator SEAT_MAP_ADD_NEW_DATE = new Locator("SEAT_MAP_ADD_NEW_DATE", xpath("//div[@class='right-fly-seatmap']"));
    private static final Locator ADD_VENUE = new Locator("ADD_VENUE", xpath("(//input[contains(@id, 'react-select') and @type='text'])[9]"));
    private static final Locator SEAT_MAP_ADD_DATE_BUTTON = new Locator("SEAT_MAP_ADD_DATE_BUTTON", xpath("//div[@class='modal-content']//button[@data-testid='button-submit' and contains(@class,'primary')]"));
    private static final Locator EVENT_DATE= new Locator("EVENT_DATE", xpath("//input[contains(@class, 'datepicker-box')]"));
    private static final Locator NEXT_MONTH = new Locator("NEXT_MONTH", xpath("//button[contains(@class, 'react-datepicker__navigation--next') and text()='Next Month']"));
    private static final Locator SELECTED_SEAT_MAP= new Locator("SELECTED_SEAT_MAP", xpath("//input[@name='seat-map-code']"));

    public void goToPage(String productId) {
        switch (Configs.ENVIRONMENT) {
            case GK -> {driver.get("https://gatotkaca.tiket.com/admin/event-ms/to-do/product-management/product-list/edit-product?productId="+productId);}
            case PREPROD -> {driver.get("https://preprod.tiket.com/admin/event-ms/to-do/product-management/product-list/edit-product?productId="+productId);}
            case PROD -> {driver.get("https://www.tiket.com/admin/event-ms/to-do/product-management/product-list/edit-product?productId="+productId);}
        }
        driverTools.waitForWebToLoad();
    }

    public void clickPackageSchedule(String packageName) {
        Locator locator = getScheduleButtonLocatorByPackageName(packageName);
        driverTools.waitForElementToAppear(locator);
        driverTools.scrollElementToCenter(locator);
        driverTools.click(locator);

        driverTools.switchToWindowContainingUrl("product-management/schedule-manange");
    }

    private Locator getScheduleButtonLocatorByPackageName(String packageName) {
        if (packageName.contains("/")) {
            String packageNameId = packageName.split("/")[0];
            String packageNameEn = packageName.split("/")[1];
            return new Locator("SCHEDULE_BUTTON", By.xpath("//section[contains(@class,'package-management')][2]//table[@class='table-tiket']//tr[./td[text()='" + packageNameId + "'] or ./td[text()='" + packageNameEn + "']]//div[@class='icon-action__item'][2]"));
        } else {
            return new Locator("SCHEDULE_BUTTON", By.xpath("//section[contains(@class,'package-management')][2]//table[@class='table-tiket']//tr[./td[text()='" + packageName + "']]//div[@class='icon-action__item'][2]"));
        }
    }

    public String editDescription(){
        String description = generateRandomString(20);
        driverTools.waitForElementToAppear(DESCRIPTION_INPUT_EN);
        driverTools.scrollElementToCenter(DESCRIPTION_INPUT_EN);
        driverTools.clearInput(DESCRIPTION_INPUT_EN);
        driverTools.sendKeys(DESCRIPTION_INPUT_EN, description);
        driverTools.clearInput(DESCRIPTION_INPUT_ID);
        driverTools.sendKeys(DESCRIPTION_INPUT_ID, description);
        return description;
    }

    public void clickSaveAndCloseButton() {
        driverTools.waitForElementToAppear(SAVE_AND_CLOSE_BUTTON);
        driverTools.scrollElementToCenter(SAVE_AND_CLOSE_BUTTON);
        driverTools.click(SAVE_AND_CLOSE_BUTTON);
        driverTools.waitForWebToLoad();
        driverTools.dummyWait(1);
    }

    public boolean verifyIsDescriptionUpdated(String description) {
        Locator ITINERARY_TEXT = new Locator("ITINERARY_TEXT", By.xpath("//section[@role='contentinfo']//span[normalize-space()='Itinerary']/ancestor::section//ol//li//*[contains(text(),'" + description + "')]"));
        driverTools.waitForElementToAppear(ITINERARY_TEXT);
        return driverTools.isElementDisplayed(ITINERARY_TEXT);
    }

    private void changeSeatMap(String seatMapType) {
        String currentSeatMapOpened = driverTools.getText(SEAT_MAP_TYPE_DISPLAYED).orElseThrow();
        if (!seatMapType.equalsIgnoreCase(currentSeatMapOpened)) {
            driverTools.waitForElementToAppear(SEAT_MAP_TYPE_DROPDOWN);
            driverTools.click(SEAT_MAP_TYPE_DROPDOWN);
            driverTools.dummyWait(1);

            boolean found = false;
            List<WebElement> dropdownSelection = driverTools.elementsUntilExist(SEAT_MAP_TYPE_DROPDOWN_SELECTION).get();
            for (WebElement e : dropdownSelection) {
                String elementText = driverTools.getText(e);
                if (elementText.equalsIgnoreCase(seatMapType)) {
                    driverTools.click(e);
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new NoSuchElementException("There is no '"+seatMapType+"' seat map type");
            }
        }
    }

    public void addSeatMapSchedule(String seatMapType, String date) {
        if (!seatMapSelected(seatMapType)) {
            changeSeatMap(seatMapType);
        }
        clickAddNewSeatMapDate();
        selectSeatMapVenue(seatMapType);
        selectScheduleDate(date);
        submitAddSeatMapSchedule();
    }

    private boolean seatMapSelected(String seatMapType) {
        driverTools.waitForElementToAppear(SELECTED_SEAT_MAP);
        driverTools.scrollElementToCenter(SELECTED_SEAT_MAP);
        String selectedSeatMapName = driverTools.getValue(SELECTED_SEAT_MAP);
        return seatMapType.equalsIgnoreCase(selectedSeatMapName);
    }

    public void clickAddNewSeatMapDate(){
        driverTools.waitForElementToAppear(SEAT_MAP_ADD_NEW_DATE,30);
        driverTools.clickWithJavascriptExecutor(SEAT_MAP_ADD_NEW_DATE);
    }

    public void selectSeatMapVenue(String seatMapVenueName) {
        driverTools.waitForElementToBeClickable(ADD_VENUE);
        driverTools.clickElementByCoordinateDWeb(ADD_VENUE);

        driverTools.sendKeys(ADD_VENUE, seatMapVenueName);
        Locator SELECT_VENUE = new Locator("SELECT_VENUE", By.xpath("//div[contains(@class, 'tix-react-select__option') and text()='" + seatMapVenueName + "']"));
        driverTools.click(SELECT_VENUE);
    }

    public void selectScheduleDate(String date) {
        driverTools.waitForElementToAppear(EVENT_DATE);
        driverTools.click(EVENT_DATE);

        LocalDate today = LocalDate.now();
        String[] dateArray = date.split("-");
        int diffMonth = ((12 * Integer.parseInt(dateArray[0])) + Integer.parseInt(dateArray[1])) - ((12 * today.getYear()) + today.getMonthValue());

        for (int i = 0 ; i < diffMonth ; i++) {
            driverTools.click(NEXT_MONTH);
        }

        String newDateNumber = String.valueOf(Integer.parseInt(dateArray[2]));
        Locator locator = getDateLocatorByDateNumber(newDateNumber);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
    }

    private Locator getDateLocatorByDateNumber(String dateNumber) {
        return new Locator("DATE_"+dateNumber, xpath("(//div[@role='button' and text()='"+dateNumber+"'])[1]"));
    }

    public void submitAddSeatMapSchedule() {
        driverTools.waitForElementToAppear(SEAT_MAP_ADD_DATE_BUTTON);
        driverTools.click(SEAT_MAP_ADD_DATE_BUTTON);
    }
}
