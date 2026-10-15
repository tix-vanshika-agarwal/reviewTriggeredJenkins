package com.tiket.page.carrental.searchresultpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.carrental.searchpage.SearchPageCarRental;
import com.tiket.page.common.calendar.CalendarPage;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;
import java.util.Optional;

import static com.tiket.core.CoreFunctions.splitLanguageData;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class SearchResultPageCarRentalIOS <T extends WebDriver> extends SearchResultPageCarRental<T> {

    private static final Locator BTN_OK = new Locator("BTN_OK", AppiumBy.name("Ok"));
    private static final Locator BTN_SEARCH_OTHER = new Locator("BTN_SEARCH_OTHER", AppiumBy.name("continue_button"));
    private static final Locator CHOOSE_CAR = new Locator("CHOOSE_CAR", AppiumBy.name("cr_type"));
    private static final Locator CHOOSE_VENDOR = new Locator("CHOOSE_VENDOR", AppiumBy.name("cr_vendor"));
    private static final Locator CHANGE_SEARCH = new Locator("CHANGE_SEARCH", AppiumBy.name("change_button"));
    private static final Locator WITHOUT_DRIVER_MARKER = new Locator("WITHOUT_DRIVER_MARKER", AppiumBy.name("without_driver_tab (selected)"));
    private static final Locator SELECT_LOCATION_WITH_DRIVER = new Locator("SELECT_LOCATION_WITH_DRIVER", AppiumBy.accessibilityId("cr_location_rental_with_driver"));
    private static final Locator SELECT_LOCATION_WITHOUT_DRIVER = new Locator("SELECT_LOCATION_WITHOUT_DRIVER", AppiumBy.name("cr_location_rental_without_driver"));
    private static final Locator ENTER_PICKUP_CITY = new Locator("ENTER_PICKUP_CITY", AppiumBy.name("search_city"));
    private static final Locator RENTAL_CALENDAR = new Locator("RENTAL_CALENDAR", AppiumBy.name("cr_rental_date"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", AppiumBy.name("save_button"));
    private static final Locator EDIT_DURATION = new Locator("EDIT_DURATION", AppiumBy.name("cr_rental_duration"));
    private static final Locator RENTAL_DURATION_WHEEL_PICKER = new Locator("RENTAL_DURATION_WHEEL_PICKER", AppiumBy.className("XCUIElementTypePickerWheel"));
    private static final Locator ALL_IN_TAB = new Locator("ALL_IN_TAB", AppiumBy.iOSClassChain("**/*[`label CONTAINS 'Paket All-In' OR label CONTAINS 'All-In Package'`]"));
    private static final Locator PACKAGE_BANNER = new Locator("PACKAGE_BANNER", AppiumBy.id("package_banner"));
    private static final Locator REGULAR_TAB = new Locator("REGULAR_TAB", AppiumBy.iOSClassChain("**/*[`label CONTAINS 'Paket Reguler' OR label CONTAINS 'Regular Package'`]"));
    private static final Locator CAR_TYPE = new Locator("CAR_TYPE", AppiumBy.id("cr_type"));
    private static final Locator CAR_PRICE = new Locator("CAR_PRICE", AppiumBy.id("cr_start_price"));
    private static final Locator VENDOR_PRICE = new Locator("VENDOR_PRICE", AppiumBy.name("vendor_price_1"));
    private static final Locator PDP_PRICE = new Locator("PDP_PRICE", AppiumBy.name("price"));
    private static final Locator SAVE_BUTTON_CALENDAR = new Locator("SAVE_BUTTON_CALENDAR", AppiumBy.xpath("//XCUIElementTypeButton[@label='Simpan' or @label='Save']"));
    private static final Locator LOCATION_SRP = new Locator("LOCATION_SRP", AppiumBy.accessibilityId("location_srp"));
    public SearchResultPageCarRentalIOS(Session<T> session) {
        super(session);
    }

    private Locator getPackageLocator(String rentalPackage){
        return new Locator("RENTAL_PACKAGE_"+rentalPackage, AppiumBy.xpath("(//*[contains(@label,'"+rentalPackage+"')])[1]"));
    }

    @Override
    public void chooseCarType(){
        driverTools.click(CHOOSE_CAR);
    }

    @Override
    public void chooseCarVendor(){
        driverTools.click(CHOOSE_VENDOR);
    }

    private Locator getLocatorForCarModelName(String carType){
        return new Locator("CAR_TYPE_NAME", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='cr_type' and contains(@value,'"+carType+"')])[1]"));
    }

    private Locator getLocatorForVendorName(String vendorName){
        return new Locator("CAR_VENDOR_NAME", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='vendor_title' and contains(@value,'"+vendorName+"')])[1]"));
    }

    @Override
    public void chooseCarWithCarTypeNameAndVendorName(String carType,String vendorName){
        WebElement carName = driverTools.swipeToElement(getLocatorForCarModelName(carType), Swipe.UP,4).orElseThrow();
        driverTools.click(carName);
        driverTools.click(getLocatorForVendorName(vendorName));
    }

    private Locator getDateAndDurationLocator(String duration, String date, String driverType){
        String[] dateSplit = date.split(" ");
        String day = dateSplit[0];
        String monthEng = dateSplit[1].substring(0,3);
        String monthId = CoreFunctions.monthShortToShortEngToId(monthEng);
        String dateId = day+" "+monthId;
        String dateEng = day+" "+monthEng;
        String driverTypeEng;
        String driverTypeId;
        switch (driverType) {
            case "with_driver" -> {
                driverTypeEng = "With Driver";
                driverTypeId = "Dengan Sopir";
            }
            case "without_driver" -> {
                driverTypeEng = "Self Drive";
                driverTypeId = "Lepas Kunci";
            }
            default -> throw new NotImplementedException("Not implemented");
        }
        return new Locator("Date locator",AppiumBy.xpath("//XCUIElementTypeStaticText[(contains(@label,'"+dateId+"') and contains(@label,'"+duration+" hari') and contains(@label,'"+driverTypeId+"')) or  (contains(@label,'"+dateEng+"') and contains(@label,'"+duration+" day') and contains(@label,'"+driverTypeEng+"'))]"));
    }

    @Override
    public void changeLocation(String location){
        final Locator CHOOSE_LOCATION  = getChangeLocationLocator(location);
        if(this.isSelectingWithoutDriver()){
            driverTools.click(SELECT_LOCATION_WITHOUT_DRIVER);
        }else{
            driverTools.click(SELECT_LOCATION_WITH_DRIVER);
        }
        driverTools.sendKeys(ENTER_PICKUP_CITY, location);
        driverTools.hideKeyboard(1);
        driverTools.click(CHOOSE_LOCATION);
        driverTools.click(SAVE_BUTTON);
    }

    private boolean isSelectingWithoutDriver(){
        return driverTools.isElementDisplayed(WITHOUT_DRIVER_MARKER);
    }

    private Locator getChangeLocationLocator(String location) {
        return new Locator("CHOOSE_LOCATION", AppiumBy.xpath("//*[@label='"+location+"']"));
    }

    @Override
    public void changeDate(String date){
        WebElement rentalCalendar = driverTools.onScreenElement(RENTAL_CALENDAR);
        driverTools.click(rentalCalendar);

        CalendarPage<T> calendarPage = CalendarPage.getInstance(session);
        calendarPage.selectDate(date);
        WebElement e = driverTools.waitForElementToAppear(SAVE_BUTTON_CALENDAR,10).orElseThrow();
        driverTools.click(e);
    }

    private boolean isCarRentalDurationCorrect(String daysAndEndDate){
        WebElement wheelPicker = driverTools.element(RENTAL_DURATION_WHEEL_PICKER).orElseThrow();
        String wheelPickerValue = wheelPicker.getAttribute("value");
        String daysValue = wheelPickerValue.split(" ")[0];
        logger.log("Wheel picker value -> "+wheelPickerValue);
        logger.log("is car rental duration correct -> "+ daysValue.equals(daysAndEndDate));
        return daysValue.equals(daysAndEndDate);
    }

    public String selectAllInTab() {
        WebElement allInTab = driverTools.waitForElementToPresence(ALL_IN_TAB).orElseThrow();
        String text = driverTools.getText(allInTab);
        logger.log("selectAllInTab text -> " + text);
        driverTools.click(allInTab);
        return text;
    }

    @Override
    public String selectRegularTab() {
        WebElement regularTab = driverTools.waitForElementToPresence(REGULAR_TAB).orElseThrow();
        String text = driverTools.getText(regularTab);
        logger.log("selectRegularTab text -> " + text);
        driverTools.click(regularTab);
        return text;
    }

    @Override
    public boolean packageBanner(String packageTitle) {
        boolean status = false;
        switch (packageTitle) {
            case "Paket Reguler", "Regular Package" -> status = driverTools.isElementDisplayed(PACKAGE_BANNER);
            case "Paket All-In", "All-In Package" -> status = driverTools.isElementDisplayed(PACKAGE_BANNER);
        }
        logger.log("packageBanner status -> " + status);
        return status;
    }

    @Override
    public boolean carListContent() {
        Optional<WebElement> carType = driverTools.waitForElementToPresence(CAR_TYPE);
        boolean carTypeStatus = driverTools.isElementDisplayed(carType.orElseThrow());
        logger.log("carTypeStatus -> " + carTypeStatus);
        boolean carPrice = driverTools.isElementDisplayed(CAR_PRICE);
        logger.log("carPrice -> " + carPrice);
        return carTypeStatus && carPrice;
    }

    @Override
    public void chooseTransmissionType(String transmissionType) {
        //Automatic/Otomatis, Manual/Manual ,All/Semua
        if(transmissionType!=null){
            Locator rentalPaLocator = this.transmissionTypeLocator(transmissionType);
            WebElement type = driverTools.waitForElementToAppear(rentalPaLocator,10).orElseThrow();
            driverTools.click(type);
        }
    }

    private Locator transmissionTypeLocator(String rentalPackage){
        String rentalPackageEN = rentalPackage.split("/")[0];
        String rentalPackageIN = rentalPackage.split("/")[1];
        return new Locator("RENTAL_PACKAGE_"+rentalPackage, AppiumBy.xpath("(//*[contains(@label,'"+rentalPackageEN+"') or contains(@label,'"+rentalPackageIN+"')])[1]"));
    }

    @Override
    public boolean carListTransmissionType(String transmissionType) {
        return driverTools.waitForElementToPresence(carListTransmissionLocator(transmissionType)).isPresent();
    }

    private Locator carListTransmissionLocator(String transmissionType) {
        return new Locator("TRANSMISSION_TYPE_LIST",
                AppiumBy.xpath("//XCUIElementTypeCell//XCUIElementTypeStaticText[@label='" + splitLanguageData(transmissionType, "/")[0] + "' or @label='" + splitLanguageData(transmissionType, "/")[1] + "']"));
    }

    @Override
    public String getPriceCarType() {
        String carTypePrice = driverTools.getText(CAR_PRICE).orElseThrow();
        logger.log("Car type price: "+ carTypePrice);
        return carTypePrice;
    }

    @Override
    public String getPriceCarVendor() {
        String carVendorPrice = driverTools.getText(VENDOR_PRICE).orElseThrow();
        logger.log("Car type price: "+ carVendorPrice);
        return carVendorPrice;
    }

    @Override
    public String getPricePDP() {
        driverTools.swipeToElement(PDP_PRICE,Swipe.UP,3);
        String carPDPPrice = driverTools.getValueAttribute(PDP_PRICE,"label");
        logger.log("Car PDP price: "+ carPDPPrice);
        return carPDPPrice;
    }
}
