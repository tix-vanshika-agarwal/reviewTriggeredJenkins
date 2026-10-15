package com.tiket.page.carrental.reviewbookingpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.carrental.CarRentalDetail;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class CarRentalReviewBookingPageAndroid <T extends WebDriver> extends CarRentalReviewBookingPage<T> {

    private static final Locator BTN_CONTINUE = new Locator("BTN_CONTINUE", AppiumBy.id("btn_continue"));
    private static final Locator SEE_RENTAL_AREAS = new Locator("SEE_RENTAL_AREAS", AppiumBy.id("tv_bottom_navigation"));
    private static final Locator MAPS_IMAGE = new Locator("MAPS_IMAGE", AppiumBy.id("iv_image"));
    private static final Locator RENTAL_AREA_NAME = new Locator("RENTAL_AREA_NAME", AppiumBy.id("tv_top_section"));
    private static final Locator LIST_OF_AREA = new Locator("LIST_OF_AREA", AppiumBy.id("tv_description"));
    private static final Locator CAR_VENDOR_HEADING = new Locator("CAR_VENDOR_HEADING", AppiumBy.xpath("//*[contains(@class,'TextView') and contains(@text,'Detail') and contains(@text,'Vendor')]"));
    private static final Locator RENTAL_TIME_HEADING = new Locator("RENTAL_TIME_HEADING", AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and contains(@text,'Waktu Sewa') or contains(@text,'Rental Time')]"));
    private static final Locator RENTAL_AREA_HEADING = new Locator("RENTAL_AREA_HEADING", AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and contains(@text,'Area')]"));
    private static final Locator REGULAR_PACKAGE_HEADING = new Locator("REGULAR_PACKAGE_HEADING", AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and contains(@text,'Paket Reguler') or contains(@text,'Regular Package')]"));
    private static final Locator ALLIN_PACKAGE_HEADING = new Locator("ALLIN_PACKAGE_HEADING", AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and contains(@text,'All-in')]"));
    private static final Locator INCLUDED_SECTION = new Locator("INCLUDED_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,\"What's Included\") or contains(@text,'Sudah Termasuk')]"));
    private static final Locator NOT_INCLUDED_SECTION = new Locator("NOT_INCLUDED_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'Belum Termasuk') or contains(@text,'Not Included')]"));
    private static final Locator GENERAL_SECTION = new Locator("GENERAL_SECTION", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'Umum') or contains(@text,'General')]"));
    private static final Locator CAR_AND_VENDOR_DETAIL_TITLE = new Locator("CAR_AND_VENDOR_DETAIL_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and ( contains(@text,'Detail Mobil dan Vendor') or contains(@text,'Car and Vendor Details'))]"));
    private static final Locator BAGGAGE_DETAILS = new Locator("BAGGAGE_DETAILS", AppiumBy.xpath("//*[contains(@resource-id,'tv_text') and ( contains(@text,'baggage') or contains(@text,'koper'))]"));
    private static final Locator PASSENGER_COUNT_DETAILS = new Locator("PASSENGER_COUNT_DETAILS", AppiumBy.xpath("//*[contains(@resource-id,'tv_text') and ( contains(@text,'passengers') or contains(@text,'penumpang'))]"));
    private static final Locator PICKUP_DATE_DETAILS = new Locator("PICKUP_DATE_DETAILS", AppiumBy.id("tv_pickup_date_info"));
    private static final Locator DROP_OFF_DATE_DETAILS = new Locator("DROP_OFF_DATE_DETAILS", AppiumBy.id("tv_return_date_info"));
    private static final Locator RENTALS_AREA_DETAILS = new Locator("RENTALS_AREA_DETAILS", AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and (contains(@text,'Area Sewamu') or contains(@text,'Your Rental Area'))]"));
    private static final Locator COVERAGE_AREA_TITTLE = new Locator("COVERAGE_AREA_TITTLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Coverage Areas') or contains(@text,'Cakupan Area'))]"));
    private static final Locator CLOSE_BTN = new Locator("CLOSE_BTN", AppiumBy.id("iv_close"));
    private static final Locator ABOUT_PACKAGE = new Locator("ABOUT_PACKAGE", AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and (contains(@text,'Tentang Paket') or (contains(@text,'Package') and contains(@text,'About')))]"));
    private static final Locator ABOUT_SECTION_WITHOUT_DRIVER = new Locator("ABOUT_SECTION_WITHOUT_DRIVER", AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and (contains(@text,'Tentang Sewa Lepas Kunci') or contains(@text,'About Rental Self Drive'))]"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", AppiumBy.id("tv_price"));
    private static final Locator FLEET_NAME = new Locator("FLEET_NAME", AppiumBy.id("tv_fleet_name"));

    public CarRentalReviewBookingPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void clickSeeRentalAreas() {
        driverTools.swipeToElement(SEE_RENTAL_AREAS,Swipe.UP,2);
        driverTools.click(SEE_RENTAL_AREAS);
    }
    @Override
    public boolean isMapsImageVisible() {
       return driverTools.isElementDisplayed(MAPS_IMAGE);
    }

    @Override
    public String getCarRentalAreaName(){
    String rentalAreaName = driverTools.getText(driverTools.swipeToElement(RENTAL_AREA_NAME,Swipe.UP,3).orElseThrow());
    logger.log("Rental Area is : "+ rentalAreaName);
    return rentalAreaName;
    }

    @Override
    public String verifyCarRentalAreaName(String rentalAreaName) {
        String rentalAreaNameText = driverTools.getText(getRentalAreaNameTitle(rentalAreaName)).orElseThrow();
        logger.log("Rental Area text on Title: " + rentalAreaNameText);
        String[] words = rentalAreaNameText.split("\\s+");
        return words[words.length - 1];
    }

    private Locator getRentalAreaNameTitle(String Area) {
        return new Locator("RENTAL_AREA_NAME_TITLE" + Area, AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'"+Area+"')]"));
    }

    @Override
    public boolean verifyListOfArea() {
        return driverTools.isElementDisplayed(LIST_OF_AREA);
    }

    @Override
    public boolean verifyAllSectionsOnThePage() {
        boolean carVendor = driverTools.swipeToElement(CAR_VENDOR_HEADING,Swipe.UP,3).isPresent();
        logger.log("carVendor section presence : " + carVendor);
        return carVendor;
    }

    @Override
    public boolean includedAndNonIncludedSections() {
        boolean included = driverTools.isElementDisplayed(INCLUDED_SECTION);
        driverTools.swipeUp();
        boolean notIncluded = driverTools.isElementDisplayed(NOT_INCLUDED_SECTION);

        return included && notIncluded;
    }

    private boolean isDetailCorrect(String type, Locator locator){
        driverTools.swipeDown(2);
        WebElement detailElement = driverTools.swipeToElement(locator,Swipe.UP,4).orElseThrow();
        boolean detail = driverTools.isElementDisplayed(detailElement);
        logger.log(type + "displayed : " + detail);
        return detail;
    }

    private boolean checkCoverage(){
        WebElement seeAllCoverage = driverTools.waitForElementToAppear(SEE_RENTAL_AREAS).orElseThrow();
        driverTools.click(seeAllCoverage);
        WebElement closeElement = driverTools.waitForElementToAppear(CLOSE_BTN).orElseThrow();
        boolean isAllCoverageTittleDisplayed = isDetailCorrect("all coverage tittle",COVERAGE_AREA_TITTLE);
        driverTools.click(closeElement);
        return isAllCoverageTittleDisplayed;
    }

    @Override
    public String getPrice(String currency){
        WebElement priceElement = driverTools.swipeToElement(TOTAL_PRICE,Swipe.UP,5).orElseThrow();
        return driverTools.getText(priceElement);
    }

    @Override
    public boolean verifyAllSectionsOnPageWithoutDriver( ) {
        boolean carVendor = driverTools.swipeToElement(CAR_VENDOR_HEADING,Swipe.UP,3).isPresent();
        boolean rentalTime = driverTools.swipeToElement(RENTAL_TIME_HEADING,Swipe.UP,3).isPresent();
        boolean GeneralSection = driverTools.swipeToElement(GENERAL_SECTION,Swipe.UP,3).isPresent();
        boolean notIncluded = driverTools.swipeToElement(NOT_INCLUDED_SECTION,Swipe.UP,3).isPresent();
        return carVendor && rentalTime && notIncluded && GeneralSection;
    }

    @Override
    public String getFleetName() {
        String fleetName = driverTools.getText(FLEET_NAME).orElseThrow();
        return fleetName;
    }

    @Override
    public List<String> getPickUpAndDropOffDate() {
        List<String> pickUpDroOffDate = new ArrayList<String>();
        String pickupDate = driverTools.getText(PICKUP_DATE_DETAILS).orElseThrow();
        String dropOffDate = driverTools.getText(DROP_OFF_DATE_DETAILS).orElseThrow();
        logger.log("pick up date : " + pickupDate);
        logger.log("drop off date : " + dropOffDate);
        pickUpDroOffDate.add(pickupDate);
        pickUpDroOffDate.add(dropOffDate);

        return pickUpDroOffDate;
    }
}
