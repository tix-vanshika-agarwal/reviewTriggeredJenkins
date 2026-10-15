package com.tiket.web.page.nft.car_rental.productDetail_page;

import com.tiket.common.XPathUtils;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CarRentalReviewBookingPageMWeb <T extends WebDriver> extends CarRentalReviewBookingPageWeb<T> {

    public CarRentalReviewBookingPageMWeb(Session<T> session) {
        super(session);
    }

    private static final Locator RENTAL_PACKAGE_TEXT = new Locator("RENTAL_PACKAGE_TEXT", new By.ByXPath("//*[contains(@class, 'PackageSection_main')]"));
    private static final Locator CHECK_AREA_CTA = new Locator("CHECK_AREA_CTA", new By.ByXPath("//*[contains(@id, 'cta-text') and contains(text(), 'area')]"));
    private static final Locator YOUR_RENTAL_AREA = new Locator("YOUR_RENTAL_AREA", new By.ByXPath("//*[contains(@class, 'CoverageAreasAllInResponsive_main_content')]"));
    private static final Locator COVERAGE_AREA_LOCATION = new Locator("COVERAGE_AREA_LOCATION", new By.ByXPath("(//*[contains(@class, 'BottomSheet_body_title_wrapper')])"));
    private static final Locator SELECTED_PACKAGE_TITLE = new Locator("SELECTED_PACKAGE_TITLE", new By.ByXPath("//*[contains(@class, 'PackageSection_selected_item')]//*[contains(@class,'PackageSection_package_title')]"));

    public Locator getPackageTypeChipLocator(String carRentalPackage) {
        String carRentalPackageIndo = carRentalPackage;
        if (carRentalPackage.contains("/")){
            String[] typeParts = carRentalPackage.split("/");
            carRentalPackage = typeParts[0];
            carRentalPackageIndo = typeParts[1];
        }
        String xpath = "(//*[contains(@class, 'PackageSection_package_title') and ("
                + XPathUtils.containsIgnoreCase("normalize-space(.)", carRentalPackage)
                + " or "
                + XPathUtils.containsIgnoreCase("normalize-space(.)", carRentalPackageIndo)
                + ")])[1]";
        return new Locator("PACKAGE_TYPE_CHIP", new By.ByXPath(xpath));
    }

    public void navigateToRentalAreaPage(String carRentalPackage) {
        selectPackageType(carRentalPackage);
        checkCoverageArea();
    }

    public String selectPackageType(String carRentalPackage) {
        Locator PACKAGE_TYPE_CHIP = getPackageTypeChipLocator(carRentalPackage);
        driverTools.waitForElementToPresence(RENTAL_PACKAGE_TEXT);
        driverTools.scrollIntoWebView(PACKAGE_TYPE_CHIP);
        driverTools.waitForElementToPresence(PACKAGE_TYPE_CHIP);
        driverTools.clickWithJavascriptExecutor(PACKAGE_TYPE_CHIP);

        WebElement rentalPac = driverTools.waitForElementToPresence(PACKAGE_TYPE_CHIP).orElseThrow();
        String rentalPacText = driverTools.getText(rentalPac);
        logger.log("selected rental package mweb class: " + rentalPacText);
        return rentalPacText;
    }

    public void checkCoverageArea() {
        if (driverTools.isElementDisplayed(CHECK_AREA_CTA)) {
            driverTools.clickWithJavascriptExecutor(CHECK_AREA_CTA);
        }
        else {
            throw new DriverToolsException("Check Area CTA is not displayed");
        }
    }

    public void verifyRentalArea(String location) {
        // not implemented for mweb
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
        driverTools.waitForElementToAppear(OPEN_PRICE_BOTTOM_SHEET);
        driverTools.clickWithJavascriptExecutor(OPEN_PRICE_BOTTOM_SHEET);
        driverTools.waitForElementToAppear(BASE_PRICE_UNDER_BOTTOM_SHEET).orElseThrow();
        String basePrice = driverTools.getText(BASE_PRICE_UNDER_BOTTOM_SHEET).orElseThrow();

        if (driverTools.waitForElementToAppear(CLOSE_PRICE_BOTTOM_SHEET).isPresent()) {
            driverTools.click(CLOSE_PRICE_BOTTOM_SHEET);
        } else {
            logger.log("Close button on price bottom sheet is not present, falling back to device back");
            driverTools.back();
        }
        return basePrice;
    }
}
