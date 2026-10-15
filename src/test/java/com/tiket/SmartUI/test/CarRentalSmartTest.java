package com.tiket.SmartUI.test;

import com.tiket.SmartUI.SmartUtils;
import com.tiket.SmartUI.page.car_rental.CarRentalSmartPage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Session;

import com.tiket.page.carrental.bookingpage.CarRentalBookingPage;
import com.tiket.page.carrental.reviewbookingpage.CarRentalReviewBookingPage;
import com.tiket.page.carrental.searchpage.SearchPageCarRental;
import com.tiket.page.carrental.searchresultpage.SearchResultPageCarRental;

import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.payment.PaymentPage;

import com.tiket.pojo.carrental.CarRentalDetail;

import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.smart.CarRental.CarRentalSmartPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class CarRentalSmartTest extends BaseTest {
    @Author(names = {"Naufal Hafiz"})
    @Module(name = "CarRentalSmart")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testCarRentalSmartGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> params = getParam("testCarRentalSmartGuest");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        CarRentalSmartPage carRentalSmartPage = CarRentalSmartPage.getInstance(session);
        CarRentalSmartPageWeb<?> carRentalSmartPageWeb = CarRentalSmartPageWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);

        String username = params.get("username");
        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String dropOffLocation = params.get("dropOffLocation");
        String carRentalPackage = params.get("carRentalPackage");
        String carRentalDuration = params.get("carRentalDuration");
        String dateRental = CoreFunctions.getCurrentLongDate();
        String phoneNumber = params.get("phoneNumber");
        String currency = params.get("currency");
        CarRentalDetail carRentalDetail = new CarRentalDetail();
        int duration = Integer.parseInt(carRentalDuration);
        carRentalDetail.duration = duration;
        String driverType = "without_driver";

        step("Go To Home");
        homePage.gotoHome();

        step("User user app as guest");
        loginPage.loginAsGuest();

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(driverType);

        step("Search for random string for the location");
        webViewPage.switchToWebViewUrl("sewa-mobil");
        carRentalSmartPageWeb.chooseLocation("gghvjhvjvhttchgvgjvu");
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify info view on booking form");
        carRentalSmartPageWeb.closeBottomsheet();

        step("Input car rental location");
        carRentalSmartPageWeb.chooseLocation(Location);
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify Search Form View Bottomsheet");
        carRentalSmartPageWeb.closeBottomsheet();

        carRentalSearchPage.chooseLocation(Location);
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify Search Form View After selected");

        step("get selected data landing page");
        carRentalDetail.landingPageData = carRentalSearchPage.getSelectedDataLandingPage();
        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("Apply automatic transmission filter");
        webViewPage.switchToWebViewUrl("sewa-mobil");
        carRentalSmartPageWeb.applyTransmissionFilter("automatic");
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify Chips on SRP Automatic");

        step("Apply manual transmission filter");
        carRentalSmartPageWeb.applyTransmissionFilter("manual");
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify Chips on SRP Manual");

        step("Choose Car");
        carRentalSmartPageWeb.chooseCar();
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify Card view on SRP and Bottomsheet");
        carRentalSmartPageWeb.chooseVendor();
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify Card view on PDP and Booking form page PDP");

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();
        webViewPage.switchToWebViewUrl("sewa-mobil");
        carRentalSmartPageWeb.waitForElement("booking");
        SmartUtils.takeSmartScreenshot(session.driver(),"CR_[Guest] Verify Card view on PDP and Booking form page Booking Form 1");

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("choose drop off location");
        carRentalBookingPage.chooseDropOffLocation(dropOffLocation);

        step("add special request");
        carRentalBookingPage.addExtraFacilities("carRentalSpecialRequest");

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify Card");

        step("input contact detail");
        carRentalBookingPage.fillGuestDetailOrder(username,phoneNumber);

        step("proceed to payment page");
        carRentalBookingPage.setContinueToPayment();
        webViewPage.switchToWebViewUrl("sewa-mobil");
        carRentalSmartPageWeb.waitForElement("payment");
    }

    @Author(names = {"Naufal Hafiz"})
    @Module(name = "CarRentalSmart")
    @Tribe(name = "CarRental")
    @Vertical(name = "GroundTransport")
    @Test
    public void testCarRentalSmartLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String,String> params = getParam("testCarRentalSmartLogin");
        BaseTest.paramsThreadLocal.set(params);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        CarRentalSmartPage carRentalSmartPage = CarRentalSmartPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        LoginPage <WebDriver> loginPage = LoginPage.getInstance(session);
        CarRentalSmartPageWeb<?> carRentalSmartPageWeb = CarRentalSmartPageWeb.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String Location = params.get("location");
        String pickupLocation = params.get("pickupLocation");
        String dropOffLocation = params.get("dropOffLocation");
        String carRentalDuration = params.get("carRentalDuration");
        String currency = params.get("currency");
        CarRentalDetail carRentalDetail = new CarRentalDetail();

        String loginMethod = params.get("loginType");
        String username = params.get("username");
        String password = params.get("password");

        int duration = Integer.parseInt(carRentalDuration);
        carRentalDetail.duration = duration;
        String driverType = "without_driver";

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginWith(loginMethod, username, password);

        if(Configs.ENVIRONMENT == Environment.PROD) {
            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
        }

        step("Get App Language");
        String language = homePage.getLanguageApp();

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals", language, currency);
        carRentalSearchPage.allowLocationPermission();
        carRentalSearchPage.closeInformationPopUp();

        step("Select car rental type");
        carRentalSearchPage.chooseDriver(driverType);

        step("Search for random string for the location");
        webViewPage.switchToWebViewUrl("sewa-mobil");
        carRentalSmartPageWeb.chooseLocation("gghvjhvjvhttchgvgjvu");
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify info view on booking form");
        carRentalSmartPageWeb.closeBottomsheet();

        step("Input car rental location");
        carRentalSmartPageWeb.chooseLocation(Location);
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify Search Form View Bottomsheet");
        carRentalSmartPageWeb.closeBottomsheet();

        carRentalSearchPage.chooseLocation(Location);
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify Search Form View After selected");

        step("get selected data landing page");
        carRentalDetail.landingPageData = carRentalSearchPage.getSelectedDataLandingPage();

        step("Search car rental");
        carRentalSearchPage.searchCar(carRentalDuration);

        step("Apply automatic transmission filter");
        webViewPage.switchToWebViewUrl("sewa-mobil");
        carRentalSmartPageWeb.applyTransmissionFilter("automatic");
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify Chips on SRP Automatic");

        step("Apply manual transmission filter");
        carRentalSmartPageWeb.applyTransmissionFilter("manual");
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify Chips on SRP Manual");

        step("Choose Car");
        carRentalSmartPageWeb.chooseCar();
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify Card view on SRP and Bottomsheet");
        carRentalSmartPageWeb.chooseVendor();
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify Card view on PDP and Booking form page PDP");

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();
        webViewPage.switchToWebViewUrl("sewa-mobil");
        carRentalSmartPageWeb.waitForElement("booking");
        SmartUtils.takeSmartScreenshot(session.driver(),"CR_[Guest] Verify Card view on PDP and Booking form page Booking Form 1");

        step("pick up location");
        carRentalBookingPage.choosePickUpLocation(pickupLocation);

        step("choose drop off location");
        carRentalBookingPage.chooseDropOffLocation(dropOffLocation);

        step("add special request");
        carRentalBookingPage.addExtraFacilities("carRentalSpecialRequest");

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify Card");

        step("proceed to payment page");
        carRentalBookingPage.setContinueToPayment();
        webViewPage.switchToWebViewUrl("sewa-mobil");
        carRentalSmartPageWeb.waitForElement("payment");
        SmartUtils.takeSmartScreenshot(session.driver(), "CR_[Guest] Verify payment");
    }
}
