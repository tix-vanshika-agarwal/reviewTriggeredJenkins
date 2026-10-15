package com.tiket.web.mWeb.test.nft.car_rental;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.nft.car_rental.searchResult_Page.CarRentalSearchResultPageWeb;
import com.tiket.web.page.nft.car_rental.search_Page.CarRentalSearchPageWeb;

import org.testng.annotations.Test;

import java.util.Map;

import javax.security.auth.login.FailedLoginException;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class CarRentalTest extends BaseTest {

    @Author(names = "Areeb")
    @Module(name = "HomePageWeb")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyCarRentalIsStillDisplayedNonIDRGuestMweb() {
        Map<String, String> param = getParam("testVerifyCarRentalIsStillDisplayedNonIDRGuestMweb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Verify the home page is opened");
        homePage.waitForHomePageToOpen();
        Assertion.assertThat("Verify the home page is opened", homePage.isHomePageOpened(), is(true));

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User is redirected back to home page");
        Assertion.assertThat("Verify Car Rentals is still displayed on home page", homePage.verifyVerticalIsDisplayed("Car"), is(true));
    }

    @Author(names = "Clement")
    @Module(name = "SRP")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFiltersPageWithoutDriverLoginMWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyFiltersPageWithoutDriverLoginMWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User open car rental vertical");
        homePage.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User click on filter and sort options");
        carRentalSearchResultPage.clickFilterBtn();

        step("User select Transmission filter");
        carRentalSearchResultPage.clickTransmissionFilter();

        step("User selects Automatic");
        carRentalSearchResultPage.selectAMT();

        step("User saves the filter");
        carRentalSearchResultPage.clickSave();

        step("Check whether filter appears or not");
        Assertion.assertThat("Verify filter appears or not", carRentalSearchResultPage.isElementDisplayingText(), is(true));

        step("Checks whether there is any car with those filters or not");
        Assertion.assertThat("Checking for the list of cars", carRentalSearchResultPage.checkList(), is(true));

        step("User verifies whether the filter is same or not");
        Assertion.assertThat("Same filter or not", carRentalSearchResultPage.getTransmissionType(), is("Automatic"));

        step("User click on filter and sort options");
        carRentalSearchResultPage.clickFilterBtn();

        step("User select Transmission filter");
        carRentalSearchResultPage.clickTransmissionFilter();

        step("User deselects Automatic transmission type");
        carRentalSearchResultPage.selectAMT();

        step("User selects Manual transmission type");
        carRentalSearchResultPage.selectManual();

        step("User saves the filter");
        carRentalSearchResultPage.clickSave();

        step("Check whether filter appears or not");
        Assertion.assertThat("Verify filter appears or not", carRentalSearchResultPage.isElementDisplayingText(), is(true));

        step("Checks whether there is any car with those filters or not");
        Assertion.assertThat("Checking for the list of cars", carRentalSearchResultPage.checkList(), is(true));

        step("User verifies whether the filter is same or not");
        Assertion.assertThat("Transmission type is Manual or not", carRentalSearchResultPage.getTransmissionType(), is("Manual"));
    }

    @Author(names = "Clement")
    @Module(name = "SRP")
    @Tribe(name = "CarRental")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyFiltersPageWithoutDriverGuestMWeb() {
        Map<String, String> param = getParam("testVerifyFiltersPageWithoutDriverGuestMWeb");
        BaseTest.paramsThreadLocal.set(param);
        
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CarRentalSearchPageWeb<?> carRentalSearchPage = CarRentalSearchPageWeb.getInstance(session);
        CarRentalSearchResultPageWeb<?> carRentalSearchResultPage = CarRentalSearchResultPageWeb.getInstance(session);

        String driverType = param.get("driverType");
        String location = param.get("location");
        String duration = param.get("duration");

        step("User open home page");
        homePageWeb.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User open car rental vertical");
        homePageWeb.openVerticalPageViaURL("sewa-mobil");

        step("User click skip button");
        carRentalSearchPage.clickSkipButton();

        step("User select driver type");
        carRentalSearchPage.selectDriverType(driverType);

        step("User select rental location by recommendation");
        carRentalSearchPage.selectLocationByRecommendation(location);

        step("User select rental duration");
        carRentalSearchPage.selectDuration(duration);

        step("User search car rental");
        carRentalSearchPage.searchCarRental();

        step("User click on filter and sort options");
        carRentalSearchResultPage.clickFilterBtn();

        step("User select Transmission filter");
        carRentalSearchResultPage.clickTransmissionFilter();

        step("User selects Automatic transmission type");
        carRentalSearchResultPage.selectAMT();

        step("User saves the filter");
        carRentalSearchResultPage.clickSave();

        step("Check whether filter appears or not");
        Assertion.assertThat("Verify filter appears or not", carRentalSearchResultPage.isElementDisplayingText(), is(true));

        step("Checks whether there is any car with those filters or not");
        Assertion.assertThat("Checking for the list of cars", carRentalSearchResultPage.checkList(), is(true));

        step("User verifies whether the filter is same or not");
        Assertion.assertThat("Transmission type is Automatic or not", carRentalSearchResultPage.getTransmissionType(), is("Automatic"));

        step("User click on filter and sort options");
        carRentalSearchResultPage.clickFilterBtn();

        step("User select Transmission filter");
        carRentalSearchResultPage.clickTransmissionFilter();

        step("User deselects Automatic transmission type");
        carRentalSearchResultPage.selectAMT();

        step("User selects Manual transmission type");
        carRentalSearchResultPage.selectManual();

        step("User saves the filter");
        carRentalSearchResultPage.clickSave();

        step("Check whether filter appears or not");
        Assertion.assertThat("Verify filter appears or not", carRentalSearchResultPage.isElementDisplayingText(), is(true));

        step("Checks whether there is any car with those filters or not");
        Assertion.assertThat("Checking for the list of cars", carRentalSearchResultPage.checkList(), is(true));

        step("User verifies whether the filter is same or not");
        Assertion.assertThat("Transmission type is Manual or not", carRentalSearchResultPage.getTransmissionType(), is("Manual"));
    }

}
