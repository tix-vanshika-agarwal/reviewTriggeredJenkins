package com.tiket.page.carrental.searchresultpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.carrental.searchpage.SearchPageCarRental;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

import static com.tiket.core.CoreFunctions.splitLanguageData;

public class SearchResultPageCarRentalAndroid <T extends WebDriver> extends SearchResultPageCarRental<T> {

    private static final Locator SEE_CAR_IN_NEARBY_LOCATION_BTN = new Locator("SEE_CAR_IN_NEARBY_LOCATION_BTN",AppiumBy.id("tds_btn_primary_info_view"));
    private static final Locator CHOOSE_CAR = new Locator("CHOOSE_CAR", AppiumBy.xpath("(//*[contains(@resource-id,'cv_fleet_item')])[1]"));
    private static final Locator OK_BTN_IN_TRANSMISSION_INFO_POPUP = new Locator("OK_BTN_IN_TRANSMISSION_INFO_POPUP", AppiumBy.xpath("//*[@text='OK']"));
    private static final Locator CHOOSE_VENDOR = new Locator("CHOOSE_VENDOR", AppiumBy.xpath("(//*[contains(@resource-id,'cv_vendor_item')])[1]"));
    private static final Locator CHOSE_VENDOR_HEADER = new Locator("CHOOSE_VENDOR_HEADER", AppiumBy.id("tv_select_venue_title"));
    private static final Locator ALL_IN_TAB = new Locator("ALL_IN_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'Paket All-In') or contains(@text,'All-In Package'))]"));
    private static final Locator REGULAR_TAB = new Locator("REGULAR_TAB", AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and (contains(@text,'Paket Reguler') or contains(@text,'Regular Package'))]"));
    private static final Locator REGULAR_BANNER = new Locator("REGULAR_BANNER", AppiumBy.xpath("//*[contains(@resource-id,'tv_banner_description') and (contains(@text,'belum termasuk') or contains(@text,'does not include'))]"));
    private static final Locator ALL_IN_BANNER = new Locator("ALL_IN_BANNER", AppiumBy.xpath("//*[contains(@resource-id,'tv_banner_description') and (contains(@text,'sudah termasuk') or contains(@text,'includes'))]"));
    private static final Locator CAR_TYPE = new Locator("CAR_TYPE", AppiumBy.id("tv_fleet_name"));
    private static final Locator CAR_PRICE = new Locator("CAR_PRICE", AppiumBy.id("tv_fleet_price"));
    private static final Locator CHANGE_SEARCH = new Locator("CHANGE_SEARCH", AppiumBy.id("tds_appbar_item_button"));
    private static final Locator CLOSE_GPS = new Locator("CLOSE_GPS", AppiumBy.id("tds_btn_secondary_info_view"));
    private static final Locator VENDOR_PRICE = new Locator("VENDOR_PRICE", AppiumBy.id("tv_fleet_final_price"));
    private static final Locator PDP_PRICE = new Locator("PDP_PRICE", AppiumBy.id("tv_price"));

    public SearchResultPageCarRentalAndroid(Session<T> session) {
        super(session);
    }

    private void pickCarInNearbyArea(){
        driverTools.waitForElementToAppear(SEE_CAR_IN_NEARBY_LOCATION_BTN,15).ifPresent(driverTools::click);
    }

    private void skipCarRentalPackageInfo(){
        driverTools.waitForElementToAppear(OK_BTN_IN_TRANSMISSION_INFO_POPUP,15).ifPresent(driverTools::click);
    }

    private Locator getPackageLocator(String rentalPackage){
        return new Locator("RENTAL_PACKAGE_"+rentalPackage, AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and contains(@text,'"+rentalPackage+"')]"));
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
        return new Locator("CAR_TYPE_NAME", AppiumBy.xpath("(//*[contains(@resource-id,'tv_fleet_name') and contains(@text,'"+carType+"')])[1]"));
    }

    private Locator getLocatorForVendorName(String vendorName){
        return new Locator("CAR_VENDOR_NAME", AppiumBy.xpath("(//*[contains(@resource-id,'tv_vendor_name') and contains(@text,'"+vendorName+"')])[1]"));
    }

    @Override
    public void chooseCarWithCarTypeNameAndVendorName(String carType,String vendorName) {
        WebElement carName = driverTools.swipeToElement(getLocatorForCarModelName(carType), Swipe.UP,4).orElseThrow();
        driverTools.click(carName);
        driverTools.waitForElementToAppear(CHOSE_VENDOR_HEADER,5).orElseThrow();
        driverTools.click(getLocatorForVendorName(vendorName));
    }

    private Locator getLocationLocator(String location){
        return new Locator("Location locator",AppiumBy.xpath("//*[contains(@text,'Rent in "+location+"') or contains(@text,'Sewa di "+location+"')]"));
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
        return new Locator("Date locator",AppiumBy.xpath("//*[(contains(@text,'"+dateId+"') and contains(@text,'"+duration+" hari') and contains(@text,'"+driverTypeId+"')) or  (contains(@text,'"+dateEng+"') and contains(@text,'"+duration+" day') and contains(@text,'"+driverTypeEng+"'))]"));
    }

    @Override
    public void changeLocation(String location){
        SearchPageCarRental<T> searchPageCarRental = SearchPageCarRental.getInstance(session);
        searchPageCarRental.chooseLocation(location);
    }

    @Override
    public void changeDate(String date){
        SearchPageCarRental<T> searchPageCarRental = SearchPageCarRental.getInstance(session);
        searchPageCarRental.selectDate(date);
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
            case "Paket Reguler", "Regular Package" -> status = driverTools.isElementDisplayed(REGULAR_BANNER);
            case "Paket All-In", "All-In Package" -> status = driverTools.isElementDisplayed(ALL_IN_BANNER);
        }
        logger.log("packageBanner status -> " + status);
        return status;
    }

    @Override
    public boolean carListContent() {
        Optional<WebElement> carType = driverTools.waitForElementToAppear(CAR_TYPE);
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
            WebElement Type = driverTools.waitForElementToAppear(rentalPaLocator,10).orElseThrow();
            driverTools.click(Type);
        }
    }

    private Locator transmissionTypeLocator(String rentalPackage){
        String rentalPackageEN = rentalPackage.split("/")[0];
        String rentalPackageIN = rentalPackage.split("/")[1];
        return new Locator("RENTAL_PACKAGE_"+rentalPackage, AppiumBy.xpath("//*[contains(@resource-id,'tds_chips_text') and contains(@text,'"+rentalPackageEN+"') or contains(@text,'"+rentalPackageIN+"')]"));
    }

    @Override
    public boolean carListTransmissionType(String transmissionType) {
        return driverTools.waitForElementToPresence(carListTransmissionLocator(transmissionType)).isPresent();
    }

    private Locator carListTransmissionLocator(String transmissionType) {
        return new Locator("TRANSMISSION_TYPE_LIST",
                AppiumBy.xpath("//*[contains(@resource-id,'tiv_last_capacity')]//*[contains(@resource-id,'tv_text') and (@text='" + splitLanguageData(transmissionType, "/")[0] + "' or @text='" + splitLanguageData(transmissionType, "/")[1] + "')]"));
    }

    @Override
    public String getPriceCarType() {
        String carTypePrice = driverTools.getText(CAR_PRICE).orElseThrow();
        logger.log("Car type price: "+ carTypePrice);
        return carTypePrice;    }

    @Override
    public String getPriceCarVendor() {
        String carVendorPrice = driverTools.getText(driverTools.waitForElementToAppear(VENDOR_PRICE).orElseThrow());
        logger.log("Car type price: "+ carVendorPrice);
        return carVendorPrice;
    }

    @Override
    public String getPricePDP() {
        String carPDPPrice = driverTools.getText(driverTools.swipeToElement(PDP_PRICE,Swipe.UP,5).orElseThrow());
        logger.log("Car PDP price: "+ carPDPPrice);
        return carPDPPrice;
    }
}
