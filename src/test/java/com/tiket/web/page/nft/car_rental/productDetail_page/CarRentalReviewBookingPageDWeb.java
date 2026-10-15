package com.tiket.web.page.nft.car_rental.productDetail_page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class CarRentalReviewBookingPageDWeb <T extends WebDriver> extends CarRentalReviewBookingPageWeb<T> {

    public CarRentalReviewBookingPageDWeb(Session<T> session) {
        super(session);
    }

    private static final Locator RENTAL_AREA_TITLE_TEXT = new Locator("RENTAL_AREA_TITLE_TEXT", new By.ByXPath("//*[contains(@class, 'h3') and contains(text(), 'Area')]"));
    private static final Locator LOCATION_REVIEW_PAGE = new Locator("LOCATION_REVIEW_PAGE", new By.ByXPath("//div[contains(@class, 'CoverageAreas') and contains(@class, 'map_label')]//span[contains(@class, 'Text_weight_bold')]"));
    private static final Locator RENTAL_AREA_SECTION = new Locator("RENTAL_AREA_SECTION", new By.ByXPath("//*[contains(@class, 'CoverageAreas') and contains(@class, 'section')]"));
    private static final Locator YOUR_RENTAL_AREA = new Locator("YOUR_RENTAL_AREA", new By.ByXPath("//*[contains(@class, 'CoverageAreasAllInResponsive_main_content')]"));
    private static final Locator COVERAGE_AREA_LOCATION = new Locator("COVERAGE_AREA_LOCATION", new By.ByXPath("(//*[contains(@class, 'CoverageAreasAllInResponsive_map_label')])[2]"));

    public void navigateToRentalAreaPage(String carRentalPackage) {
        WebElement rentalAreaTitle =  driverTools.waitForElementToAppear(RENTAL_AREA_TITLE_TEXT).orElseThrow();
        driverTools.scrollToElement(rentalAreaTitle);
    }

    public void verifyRentalArea(String location) {
        WebElement rentalAreaSection = driverTools.waitForElementToAppear(RENTAL_AREA_SECTION).orElseThrow();
        driverTools.scrollIntoWebView(rentalAreaSection);
        Assertion.assertThat("Rental area section is displayed", driverTools.isElementDisplayed(RENTAL_AREA_SECTION), is(true));

        WebElement locationReview = driverTools.waitForElementToAppear(LOCATION_REVIEW_PAGE).orElseThrow();
        String locationReviewPage = driverTools.getText(locationReview);
        Assertion.assertThat("Rental area is the same as inputted location", locationReviewPage, containsString(location));
    }

    public boolean verifyReviewPageIsDisplayedForWithDriver(String location) {
        driverTools.scrollToElement(YOUR_RENTAL_AREA);
        driverTools.waitForElementToAppear(YOUR_RENTAL_AREA);
        boolean isYourRentalAreaSectionDisplayed = driverTools.isElementDisplayed(YOUR_RENTAL_AREA);
        String coverageAreaLocationName = driverTools.getText(COVERAGE_AREA_LOCATION).orElseThrow();
        logger.log("Expected location from test data: " + location);
        logger.log("Displayed location on review page: " + coverageAreaLocationName);

        boolean isCoverageAreaLocationName = coverageAreaLocationName.toLowerCase().contains(location.toLowerCase());
        return isYourRentalAreaSectionDisplayed && isCoverageAreaLocationName;
    }

    @Override
    public String getTotalPrice(){
        driverTools.waitForElementToAppear(OPEN_PRICE_BOTTOM_SHEET).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(BASE_PRICE_UNDER_BOTTOM_SHEET).orElseThrow();

        driverTools.click(CLOSE_PRICE_BOTTOM_SHEET);
        return driverTools.getText(BASE_PRICE_UNDER_BOTTOM_SHEET).orElseThrow();
    }
}
