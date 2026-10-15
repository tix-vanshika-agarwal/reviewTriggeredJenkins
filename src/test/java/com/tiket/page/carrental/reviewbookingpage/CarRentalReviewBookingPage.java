package com.tiket.page.carrental.reviewbookingpage;

import com.tiket.appium.Swipe;
import com.tiket.common.XPathUtils;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.carrental.CarRentalDetail;
import com.tiket.pojo.carrental.CarRentalReviewPageData;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.nft.car_rental.productDetail_page.CarRentalReviewBookingPageWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Environment;

import java.util.List;
import java.util.Map;

public abstract class CarRentalReviewBookingPage <T extends WebDriver> extends BasePage<T> {

    private static final Locator PACKAGE_SECTION = new Locator("PACKAGE_SECTION", new By.ByXPath("//*[contains(@class, 'PackageSection_package_title') and " + XPathUtils.containsIgnoreCase("normalize-space(.)", "All-In") + "]"));


    public CarRentalReviewBookingPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CarRentalReviewBookingPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new CarRentalReviewBookingPageAndroid <>(session);}
            case IOS -> {return new CarRentalReviewBookingPageIOS <>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private void switchToWebviewCarRentalsReviewPage(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("/sewa-mobil");
        driverTools.waitForWebToLoad();
    }
    
    public void continueFromReviewDetailCar(){
        switchToWebviewCarRentalsReviewPage();
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        carRentalReviewBookingPage.proceedToBookingPage();
        driverTools.switchToNative();
    }

    public abstract boolean verifyAllSectionsOnThePage();
    public abstract boolean includedAndNonIncludedSections();
    public abstract boolean verifyAllSectionsOnPageWithoutDriver();
    public abstract void clickSeeRentalAreas();
    public abstract boolean isMapsImageVisible();
    public abstract String getCarRentalAreaName();
    public abstract String verifyCarRentalAreaName(String rentalAreaName);
    public  abstract boolean verifyListOfArea();
    public abstract String getPrice(String currency);
    public abstract String getFleetName();
    public abstract List<String> getPickUpAndDropOffDate();

    public CarRentalReviewPageData selectYourRentalPackage(String rentalPackage){
        if (rentalPackage == null || rentalPackage.isEmpty()) {
            throw new IllegalArgumentException("Rental package name cannot be null or empty.");}
        driverTools.swipeUp(2);
        switchToWebviewCarRentalsReviewPage();
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);

        if (Configs.ENVIRONMENT == Environment.PROD) {
            try {
                rentalPackage = carRentalReviewBookingPage.selectPackageType(rentalPackage);
            } catch (Exception e) {
                logger.log("The package is not available, selecting default package: " + e.getMessage());
                rentalPackage = carRentalReviewBookingPage.selectDefaultPackage();
            }
        } else {
            rentalPackage = carRentalReviewBookingPage.selectPackageType(rentalPackage);
        }
        String packagePrice = carRentalReviewBookingPage.getSelectedPackagePrice(rentalPackage);
        CarRentalReviewPageData rentalReviewPageData = new CarRentalReviewPageData(rentalPackage,packagePrice);
        driverTools.switchToNative();
        return rentalReviewPageData;
    }

    public boolean verifyRegionNameNavigationBar(String locationSelected){
        switchToWebviewCarRentalsReviewPage();
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        boolean isRegionNameMatch = carRentalReviewBookingPage.verifyRegionNameInNavigationBar(locationSelected);
        driverTools.switchToNative();
        return isRegionNameMatch;
    }

    public boolean verifyCarName(String carNameSrp){
        switchToWebviewCarRentalsReviewPage();
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        boolean isCarNameMatch = carRentalReviewBookingPage.verifyCarName(carNameSrp);
        driverTools.switchToNative();
        return isCarNameMatch;
    }

    public boolean verifyCarVendorDetails(String carVendorSrp){
        switchToWebviewCarRentalsReviewPage();
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        boolean isCarVendorMatch = carRentalReviewBookingPage.verifyCarVendorName(carVendorSrp);
        driverTools.switchToNative();
        return isCarVendorMatch;
    }

    public boolean verifyReviewModule(){
        switchToWebviewCarRentalsReviewPage();
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        boolean isReviewModuleDisplayed = carRentalReviewBookingPage.verifyCarReviewSection();
        driverTools.switchToNative();
        return isReviewModuleDisplayed;
    }

    public boolean verifyImportantToReadSection(){
        switchToWebviewCarRentalsReviewPage();
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        boolean isImportantToReadSectionDisplayed = carRentalReviewBookingPage.verifyCarHowToReadInfoSection();
        driverTools.switchToNative();
        return isImportantToReadSectionDisplayed;
    }

    public boolean verifyWhatYouNeedToPrepareSection(){
        switchToWebviewCarRentalsReviewPage();
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        boolean isImportantToReadSectionDisplayed = carRentalReviewBookingPage.verifyWhatYouNeedToPrepareSection();
        driverTools.switchToNative();
        return isImportantToReadSectionDisplayed;
    }

    public boolean verifyPackageSelectionSection(String carRentalPackage){
        switchToWebviewCarRentalsReviewPage();
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        boolean isPackageSelectionSectionDisplayed = carRentalReviewBookingPage.verifyPackageSelectionSection(carRentalPackage);
        driverTools.switchToNative();
        return isPackageSelectionSectionDisplayed;
    }

    public boolean verifyPriceDetailSection(CarRentalDetail carRentalDetail){
        switchToWebviewCarRentalsReviewPage();
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        boolean isPriceDetailSectionDisplayed = carRentalReviewBookingPage.verifyPriceDetailSection(carRentalDetail);
        driverTools.switchToNative();
        return isPriceDetailSectionDisplayed;
    }

    public boolean checkCarRentalDetail(CarRentalDetail carRentalDetail){
        if(carRentalDetail==null){
            logger.warn("Car rental detail is null");
            return true;
        }
        boolean verifyRegionName = verifyRegionNameNavigationBar(carRentalDetail.landingPageData.locationSelected);
        boolean verifyCarName = verifyCarName(carRentalDetail.srpData.carName);
        boolean verifyCarVendor = verifyCarVendorDetails(carRentalDetail.srpData.vendorName);
        return verifyRegionName && verifyCarName && verifyCarVendor;
    }

    public boolean verifyVariousSectionOnThePage(String driverType,CarRentalDetail carRentalDetail){
        boolean verifyReviewSection = verifyReviewModule();
        boolean verifyImportantToReadSection = verifyImportantToReadSection();
        boolean verifyPackageSelectionSection = true,verifyWHatYouNeedToPrepare = true;
        if(!driverType.equals("without_driver")){
            if(carRentalDetail.reviewPageData!=null && carRentalDetail.reviewPageData.carRentalPackage!=null && !carRentalDetail.reviewPageData.carRentalPackage.isEmpty()){
                verifyPackageSelectionSection = verifyPackageSelectionSection(carRentalDetail.reviewPageData.carRentalPackage);
            }
        }else {
            verifyWHatYouNeedToPrepare = verifyWhatYouNeedToPrepareSection();
        }
        return verifyReviewSection && verifyImportantToReadSection && verifyPackageSelectionSection && verifyWHatYouNeedToPrepare;
    }

    public boolean detailCarRental(String carName){
        switchToWebviewCarRentalsReviewPage();
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        boolean isDetailCarRentalDisplayed = carRentalReviewBookingPage.verifyReviewPageIsDisplayed(carName);
        driverTools.switchToNative();
        return isDetailCarRentalDisplayed;
    }

    public boolean detailAboutPackage(){
        switchToWebviewCarRentalsReviewPage();
        driverTools.swipeToElement(PACKAGE_SECTION, Swipe.UP,2);
        boolean isDetailAboutPackageDisplayed = driverTools.waitForElementToAppear(PACKAGE_SECTION,10).isPresent();
        driverTools.switchToNative();
        return isDetailAboutPackageDisplayed;
    }

    public String getPrice(){
        switchToWebviewCarRentalsReviewPage();
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        String price = carRentalReviewBookingPage.getTotalPrice();
        driverTools.switchToNative();
        return price;
    }
    /**
     * Translates the package name to another language (if applicable).
     * Supports translation to the target language (e.g., Indonesian).
     *
     * @param packageName The package name in English.
     * @return The translated package name or the same name if no translation is available.
     */
    public String translateToOtherLanguage(String packageName) {
        Map<String, String> translations = Map.of(
                "Car Only", "Mobil Aja",
                "All-in Package", "Paket All-in",
                "Fuel Package", "Paket Bensin"
        );
        return translations.getOrDefault(packageName, packageName); // Return original if no translation
    }
}


