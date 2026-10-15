package com.tiket.page.carrental.bookingpage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.pojo.carrental.CarRentalBookingPageData;
import com.tiket.pojo.carrental.CarRentalDetail;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.nft.car_rental.bookingForm_Page.CarRentalBookingPageWeb;
import com.tiket.web.page.nft.car_rental.productDetail_page.CarRentalReviewBookingPageWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public abstract class CarRentalBookingPage <T extends WebDriver> extends BasePage<T>  {

    public CarRentalBookingPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CarRentalBookingPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new CarRentalBookingPageAndroid <>(session);}
            case IOS -> {return new CarRentalBookingPageIOS <>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private void switchToWebviewCarRentalsBookingPage(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("/sewa-mobil/booking");
        driverTools.waitForWebToLoad();
    }

    public void choosePickUpLocation(String pickupLocation){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        carRentalBookingPage.setPickUpLocation(pickupLocation);
        driverTools.switchToNative();
    }

    public CarRentalBookingPageData getPickUpAndDropOffData(){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        CarRentalBookingPageData carRentalBookingPageData = carRentalBookingPage.getPickUpAndDropOffData();
        driverTools.switchToNative();
        return carRentalBookingPageData;
    }

    public void choosePickUpLocationAndToggleForDropOff(String pickupLocation){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        carRentalBookingPage.setPickUpLocation(pickupLocation);
        carRentalBookingPage.ensureDropOffLocationSet(pickupLocation);
        driverTools.switchToNative();
    }

    public void fillGuestDetailOrder(String email,String phoneNumber){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        carRentalBookingPage.fillContactDetail(CoreFunctions.randomName(), phoneNumber, email);
        driverTools.switchToNative();
    }

    public String getAddSpecialRequestBookingSummery(){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        String specialRequest = carRentalBookingPage.getAddSpecialRequestBookingSummery();
        driverTools.switchToNative();
        return specialRequest;
    }

    public boolean isDriverTypeCorrect(String driverType){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        String selectedDriverType = carRentalBookingPage.getSelectedDriverType();
        logger.log("Selected driver type: " + selectedDriverType);
        Assertion.assertThat("Driver type is as expected", selectedDriverType.toLowerCase(), is(containsString(driverType.toLowerCase())));
        driverTools.switchToNative();
        return selectedDriverType.equalsIgnoreCase(driverType);

    }

    public boolean isDateAndTimeCorrectPickUp(String dateSelected, String timeSelected){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        List<String> pickUpDateAndTime = carRentalBookingPage.getPickUpDateAndTime();
        String pickUpDate = pickUpDateAndTime.get(0).trim();
        String pickUpTime = pickUpDateAndTime.get(1).trim();
        logger.log("Pick up date: " + pickUpDate + ", Pick up time: " + pickUpTime);
        Assertion.assertThat("Pick up date is as expected", pickUpDate, is(containsString(dateSelected)));
        Assertion.assertThat("Pick up time is as expected", pickUpTime, is(containsString(timeSelected)));
        driverTools.switchToNative();
        return pickUpDate.equalsIgnoreCase(dateSelected) && pickUpTime.equalsIgnoreCase(timeSelected);
    }

    public boolean isDateAndTimeCorrectDropOff(String dateSelected,int duration,String timeSelected,int timeDuration12Or24HourFormat){
        dateSelected = CoreFunctions.shiftDateByDaysWithFormat(dateSelected,duration,"d MMM",timeDuration12Or24HourFormat);
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        String dropOffDate = carRentalBookingPage.getDropOffDateAndTime();
        if(dropOffDate.contains(":")){
            String[] dropOffDateAndTime = dropOffDate.split(",");
            dropOffDate = dropOffDateAndTime[0].trim();
            String dropOffTime = dropOffDateAndTime[1].trim();
            logger.log("Drop off time: " + dropOffTime);
            Assertion.assertThat("Drop off time is as expected", dropOffTime, is(containsString(timeSelected)));
        }
        logger.log("Drop off date: " + dropOffDate);
        Assertion.assertThat("Drop off date is as expected", dropOffDate, is(containsString(dateSelected)));
        driverTools.switchToNative();
        return dropOffDate.equalsIgnoreCase(dateSelected);
    }

    public boolean verifyDateAndTimeBookingSummery(CarRentalDetail carRentalDetail){
        boolean isDateAndTimeCorrectPickUp = isDateAndTimeCorrectPickUp(carRentalDetail.landingPageData.dateSelected, carRentalDetail.landingPageData.timeSelected);
        boolean isDateCorrectDropOff = isDateAndTimeCorrectDropOff(carRentalDetail.landingPageData.dateSelected, carRentalDetail.duration==0?carRentalDetail.srpData.duration:carRentalDetail.duration,carRentalDetail.landingPageData.timeSelected,carRentalDetail.landingPageData.timeDurationFormat);
        return isDateAndTimeCorrectPickUp && isDateCorrectDropOff;
    }

    public boolean verifyVendorName(String vendorName){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        String vendorNameOnBookingPage = carRentalBookingPage.getVendorNameBookingSummery();
        logger.log("Vendor name on booking page: " + vendorNameOnBookingPage);
        Assertion.assertThat("Vendor name is as expected", vendorNameOnBookingPage, is(containsString(vendorName)));
        driverTools.switchToNative();
        return vendorNameOnBookingPage.equalsIgnoreCase(vendorName);
    }

    public boolean verifyCarName(String carName){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        String carNameOnBookingPage = carRentalBookingPage.getVehicleNameBookingSummery();
        logger.log("Car name on booking page: " + carNameOnBookingPage);
        Assertion.assertThat("Car name is as expected", carNameOnBookingPage, is(containsString(carName)));
        driverTools.switchToNative();
        return carNameOnBookingPage.equalsIgnoreCase(carName);
    }

    public boolean selectedPackageCorrect(String carRentalPackage){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        String selectedPackage = carRentalBookingPage.isPackageSelectedBookingSummery(carRentalPackage);
        logger.log("Selected package: " + selectedPackage);
        Assertion.assertThat("Selected package is as expected", selectedPackage, is(containsString(carRentalPackage)));
        driverTools.switchToNative();
        return selectedPackage.equalsIgnoreCase(carRentalPackage);
    }

    public boolean verifyCarRentalDetail(CarRentalDetail carRentalDetail){
        boolean isDriverTypeCorrect = isDriverTypeCorrect(carRentalDetail.landingPageData.driverType);
        boolean isVendorNameCorrect = verifyVendorName(carRentalDetail.getVendorName());
        boolean isCarNameCorrect = verifyCarName(carRentalDetail.getCarName());
        boolean isDriverTypeAndDateAndTimeCorrect = verifyDateAndTimeBookingSummery(carRentalDetail);
        boolean isPackageCorrect = true;
        if((carRentalDetail.landingPageData.driverType.equals("with") || carRentalDetail.landingPageData.driverType.equals("Dengan") && carRentalDetail.reviewPageData!=null && carRentalDetail.reviewPageData.carRentalPackage!=null && !carRentalDetail.reviewPageData.carRentalPackage.isEmpty())){
            isPackageCorrect = selectedPackageCorrect(carRentalDetail.getCarRentalPackage());
        }
        return isDriverTypeCorrect && isVendorNameCorrect && isCarNameCorrect && isDriverTypeAndDateAndTimeCorrect && isPackageCorrect;
    }

    public void setContinueToPayment(){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        carRentalBookingPage.selectPayment();
        carRentalBookingPage.priceChangePopup();
        driverTools.switchToNative();
    }

    public void addExtraFacilities(String extraFacilities){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        carRentalBookingPage.addSpecialRequest(extraFacilities);
        driverTools.switchToNative();
    }

    public String verifyExtraFacilities(){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        String extraFacilitiesAdded = carRentalBookingPage.getSpecialRequest();
        logger.log("Extra facilities added: " + extraFacilitiesAdded);
        driverTools.switchToNative();
        return extraFacilitiesAdded;
    }
    public void continueToCompleteBookingPage(){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        carRentalBookingPage.continueBookFormMWEB();
        driverTools.switchToNative();
    }

    public void chooseDropOffLocation(String dropOffLocation){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        carRentalBookingPage.setSetDropOffLocation(dropOffLocation);
        driverTools.switchToNative();
    }

    public abstract String addAllInPackage(String currency);
    public abstract CarRentalDetail addSpecialRequest(String currency);
    public boolean checkPriceDetail(CarRentalDetail carRentalDetail){
        switchToWebviewCarRentalsBookingPage();
        CarRentalReviewBookingPageWeb<?> carRentalReviewBookingPage = CarRentalReviewBookingPageWeb.getInstance(session);
        boolean isPriceDetailSectionDisplayed = carRentalReviewBookingPage.verifyPriceDetailSection(carRentalDetail);
        driverTools.switchToNative();
        return isPriceDetailSectionDisplayed;
    }

    public boolean verifyPickupArea(){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        boolean isPickupAreaDisplayed = !carRentalBookingPage.verifiesPickUpArea().isEmpty();
        driverTools.switchToNative();
        return isPickupAreaDisplayed;
    }

    public boolean verifyDropOffArea(){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        boolean isPickupAreaDisplayed = !carRentalBookingPage.verifiesDropOffArea().isEmpty();
        driverTools.switchToNative();
        return isPickupAreaDisplayed;
    }

    public void addSpecialRequestWifi(String request){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        carRentalBookingPage.addSpecialRequest(request);
        driverTools.switchToNative();
    }

    public boolean verifySpecialRequestAdded(String specialRequest){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        boolean isSpecialRequestAdded = carRentalBookingPage.getSpecialRequest().equals(specialRequest);
        driverTools.switchToNative();
        return isSpecialRequestAdded;
    }

    public boolean verifyPackageAllIn(){
        switchToWebviewCarRentalsBookingPage();
        CarRentalBookingPageWeb<?> carRentalBookingPage = CarRentalBookingPageWeb.getInstance(session);
        boolean isPackageAllInAdded = carRentalBookingPage.isPackageSelectedBookingSummery("All-In").contains("All-In");
        driverTools.switchToNative();
        return isPackageAllInAdded;
    }

    public abstract void searchChoosePickUpLocation(String pickupLocation);
    public abstract String getPricePickupSearch();
    public abstract void confirmPickUpLocation();
    public abstract void clickSameReturn();
    public abstract String getPricePickup();
    public abstract String getPriceDropOff();
    public abstract String getPriceArea();
    public abstract void clickPriceDetail();
    public abstract List<String> getPriceDetail();
    public abstract void closePriceDetail();
    public abstract String getTotalPrice();

    public String getVehicleNameFromBookingSummary() {
        switchToWebviewCarRentalsBookingPage();
        String vehicleName = CarRentalBookingPageWeb.getInstance(session).getVehicleNameBookingSummery();
        driverTools.switchToNative();
        return vehicleName;
    }

    public String getTotalPriceFromBookingSummary() {
        switchToWebviewCarRentalsBookingPage();
        String totalPrice = CarRentalBookingPageWeb.getInstance(session).getFooterTotalPrice();
        driverTools.switchToNative();
        return totalPrice;
    }
}
