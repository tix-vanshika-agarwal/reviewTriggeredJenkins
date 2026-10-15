package com.tiket.page.carrental.reviewbookingpage;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.carrental.CarRentalDetail;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;

public class CarRentalReviewBookingPageIOS <T extends WebDriver> extends CarRentalReviewBookingPage<T> {

    private static final Locator BTN_CONTINUE = new Locator("BTN_CONTINUE", AppiumBy.name("continue_button"));
    private static final Locator CAR_VENDOR_HEADING = new Locator("CAR_VENDOR_HEADING", AppiumBy.iOSClassChain("**/XCUIElementTypeOther[`name == 'Vehicle and Vendor Details'`]"));
    private static final Locator RENTAL_TIME_HEADING = new Locator("RENTAL_TIME_HEADING", AppiumBy.name("time_heading"));
    private static final Locator RENTAL_AREA_HEADING = new Locator("RENTAL_AREA_HEADING", AppiumBy.name("area_heading"));
    private static final Locator PACKAGE_HEADING = new Locator("PACKAGE_HEADING", AppiumBy.name("package_heading"));
    private static final Locator INCLUDED_SECTION = new Locator("INCLUDED_SECTION", AppiumBy.name("included_section"));
    private static final Locator NOT_INCLUDED_SECTION = new Locator("NOT_INCLUDED_SECTION", AppiumBy.name("not_included_section"));
    private static final Locator GENERAL_SECTION = new Locator("GENERAL_SECTION", AppiumBy.name("general_section"));
    private static final Locator BAGGAGE_DETAILS = new Locator("BAGGAGE_DETAILS", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"cr_baggage\"])[2]"));
    private static final Locator PASSENGER_COUNT_DETAILS = new Locator("PASSENGER_COUNT_DETAILS", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"cr_passenger\"])[2]"));
    private static final Locator PICKUP_DATE_DETAILS = new Locator("PICKUP_DATE_DETAILS", AppiumBy.name("pickup_date"));
    private static final Locator DROP_OFF_DATE_DETAILS = new Locator("DROP_OFF_DATE_DETAILS", AppiumBy.name("dropoff_date"));
    private static final Locator RENTALS_AREA_DETAILS = new Locator("RENTALS_AREA_DETAILS", AppiumBy.name("area_name"));
    private static final Locator SEE_COVERAGE_AREA = new Locator("SEE_COVERAGE_AREA", AppiumBy.name("see_coverage"));
    private static final Locator COVERAGE_AREA_TITTLE = new Locator("COVERAGE_AREA_TITTLE", AppiumBy.name("coverage_title"));
    private static final Locator CLOSE_BTN = new Locator("CLOSE_BTN", AppiumBy.id("ic cancel grey"));
    private static final Locator LIST_OF_AREA = new Locator("LIST_OF_AREA", AppiumBy.name("coverage_area"));
    private static final Locator MAPS_IMAGE = new Locator("MAPS_IMAGE", AppiumBy.id("coverage_map"));
    private static final Locator FLEET_NAME = new Locator("FLEET_NAME", AppiumBy.xpath("//*[contains(@name,'detail_heading')]/..//following-sibling::*//XCUIElementTypeStaticText[@name='cr_type']"));
    private static final Locator PICKUP_AND_DROPOFF = new Locator("PICKUP_AND_DROPOFF",AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,\"Pickup\") or contains(@label,\"Tanggal Selesai\")]/preceding-sibling::XCUIElementTypeStaticText[contains(@name,\",\")]"));

    public CarRentalReviewBookingPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void clickSeeRentalAreas() {
        driverTools.swipeToElement(SEE_COVERAGE_AREA,Swipe.UP,2);
        driverTools.click(SEE_COVERAGE_AREA);
    }

    @Override
    public boolean isMapsImageVisible() {
        return driverTools.isElementDisplayed(MAPS_IMAGE);
    }

    @Override
    public String getCarRentalAreaName(){
        driverTools.isElementDisplayed(RENTALS_AREA_DETAILS);
        String rentalAreaName = driverTools.getValueAttribute(RENTALS_AREA_DETAILS,"label");
        logger.log("Rental Area is : "+ rentalAreaName);
        return rentalAreaName;  }

    public String verifyCarRentalAreaName(String rentalAreaName) {
        String rentalAreaNameText = driverTools.getValueAttribute(getRentalAreaNameTitle(rentalAreaName),"label");
        logger.log("Rental Area text on Title: " + rentalAreaNameText);
        String[] words = rentalAreaNameText.split("\\s+");
        return words[words.length - 1];
    }

    private Locator getRentalAreaNameTitle(String Area) {
        return new Locator("RENTAL_AREA_NAME_TITLE" + Area, AppiumBy.xpath("//XCUIElementTypeStaticText[@name='coverage_title' and (@label='Coverage Areas in "+Area+"' or @label='Cakupan Area di "+Area+"')]"));
    }

    @Override
    public boolean verifyListOfArea() {
        return driverTools.isElementDisplayed(LIST_OF_AREA);
    }

    @Override
    public boolean verifyAllSectionsOnThePage() {
        driverTools.swipeDown(2);
        boolean carVendor = driverTools.waitForElementToAppear(CAR_VENDOR_HEADING,2).isPresent();
        return carVendor;
    }

    @Override
    public boolean includedAndNonIncludedSections() {
        boolean included = driverTools.isElementDisplayed(INCLUDED_SECTION);
        logger.log("is included section visible ? " + included);
        boolean notIncluded = driverTools.swipeToElement(NOT_INCLUDED_SECTION, Swipe.UP, 10).isPresent();
        logger.log("is not included section visible ? " + notIncluded);
        return included && notIncluded;
    }

    @Override
    public boolean verifyAllSectionsOnPageWithoutDriver() {
            boolean carVendor = driverTools.isElementDisplayed(CAR_VENDOR_HEADING);
            boolean rentalTime = driverTools.isElementDisplayed(RENTAL_TIME_HEADING);
            logger.log("is car vendor visible? " + carVendor);
            logger.log("is rental time visible? " + rentalTime);
            boolean GeneralSection = driverTools.swipeToElement(GENERAL_SECTION,Swipe.UP,3).isPresent();
            logger.log("is general section visible? " + GeneralSection);
            driverTools.swipeToElement(NOT_INCLUDED_SECTION, Swipe.UP, 10);
            boolean notIncluded = driverTools.isElementDisplayed(NOT_INCLUDED_SECTION);
            return carVendor && rentalTime && notIncluded && GeneralSection;
    }

    private boolean isDetailCorrect(String type, Locator locator){
        WebElement detailElement = driverTools.swipeToElement(locator,Swipe.UP,2).orElseThrow();
        boolean detail = driverTools.isElementDisplayed(detailElement);
        logger.log(type + "displayed : " + detail);
        return detail;
    }

    private boolean checkCoverage(){
        WebElement seeAllCoverage = driverTools.waitForElementToAppear(SEE_COVERAGE_AREA).orElseThrow();
        driverTools.click(seeAllCoverage);
        WebElement closeElement = driverTools.waitForElementToAppear(CLOSE_BTN).orElseThrow();
        boolean isAllCoverageTittleDisplayed = isDetailCorrect("all coverage tittle",COVERAGE_AREA_TITTLE);
        driverTools.click(closeElement);
        return isAllCoverageTittleDisplayed;
    }

    private Locator getPriceLocator(String currency){
        return new Locator("PRICE_LOCATOR",AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'"+currency+"')]"));
    }

    @Override
    public String getPrice(String currency){
        WebElement priceElement = driverTools.swipeToElement(getPriceLocator(currency),Swipe.UP,3).orElseThrow();
        return driverTools.getText(priceElement);
    }

    @Override
    public String getFleetName() {
        driverTools.waitForElementToAppear(CAR_VENDOR_HEADING,5);
        String fleetName = driverTools.getText(FLEET_NAME).orElseThrow();
        return fleetName;
    }

    @Override
    public List<String> getPickUpAndDropOffDate() {
        return driverTools.elements(PICKUP_AND_DROPOFF).map(elements -> {
            List<String> dates = elements.stream().map(driverTools::getText).toList();
            if (!dates.isEmpty()) logger.log("Pickup Date: " + dates.get(0));
            if (dates.size() > 1) logger.log("Drop-off Date: " + dates.get(1));
            return dates;
        }).orElse(List.of());
    }
}
