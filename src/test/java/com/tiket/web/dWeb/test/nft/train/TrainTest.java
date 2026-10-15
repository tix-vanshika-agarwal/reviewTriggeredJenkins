package com.tiket.web.dWeb.test.nft.train;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.pojo.train.TrainClass;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.nft.train.search_Page.TrainSearchPageWeb;
import com.tiket.web.page.nft.train.searchResult_Page.TrainSearchResultPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class TrainTest extends BaseTest {
    @Author(names = "Hafizh")
    @Module(name = "HomePageWeb")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTrainIsStillDisplayedNonIDRGuest() {
        Map<String, String> param = getParam("testVerifyTrainIsStillDisplayedNonIDRGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User is redirected back to home page");
        if (Configs.ENVIRONMENT == Environment.GK|| Configs.ENVIRONMENT == Environment.PREPROD) {
            Assertion.assertThat("Verify train is still displayed on home page", homePage.verifyVerticalIsDisplayed("Train"), is(true));
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            Assertion.assertThat("Verify train is still displayed on home page", homePage.verifyVerticalIsDisplayed("Trains"), is(true));
        }
    }

    @Author(names = "Hafizh")
    @Module(name = "HomePageWeb")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTrainIsStillDisplayedNonIDRLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyTrainIsStillDisplayedNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User change to " + currency + " currency");
        homePage.selectCurrency(currency);

        step("User is redirected back to home page");
        if (Configs.ENVIRONMENT == Environment.GK|| Configs.ENVIRONMENT == Environment.PREPROD) {
            Assertion.assertThat("Verify train is still displayed on home page", homePage.verifyVerticalIsDisplayed("Train"), is(true));
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            Assertion.assertThat("Verify train is still displayed on home page", homePage.verifyVerticalIsDisplayed("Trains"), is(true));
        }
    }

    @Author(names = "Hafizh")
    @Module(name = "SRP")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTrainFilterByClassLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyTrainFilterByClassLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String trainClassName = param.get("trainClass");
        TrainClass trainClass = new TrainClass(trainClassName);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step ("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open train vertical");
        if (Configs.ENVIRONMENT == Environment.GK|| Configs.ENVIRONMENT == Environment.PREPROD) {
            homePage.selectEnglishLanguageOnly();
            homePage.openVerticalPage("Train");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            homePage.openVerticalPage("Trains");
        }

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle one way");
        trainSearchPage.toggleTripType(tripType);

        step("User select departure date");
        trainSearchPage.selectDate(departureDate);

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User open filter & sort chips");
        trainSearchResultPage.clickFilterChips();

        step("User click class type section");
        trainSearchResultPage.selectClassType();

        step("User select class type");
        trainSearchResultPage.filterByClass(trainClass);

        step("User click save button");
        trainSearchResultPage.clickSaveButton();

        step("Assert train class type shown in SRP is correct");
        Assertion.assertThat("Verify class type in SRP after filter", trainSearchResultPage.verifyClass(trainClass), is(true));
    }

    @Author(names = "Hafizh")
    @Module(name = "SRP")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTrainFilterByClassGuest() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyTrainFilterByClassGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String trainClassName = param.get("trainClass");
        TrainClass trainClass = new TrainClass(trainClassName);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step ("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open train vertical");
        if (Configs.ENVIRONMENT == Environment.GK|| Configs.ENVIRONMENT == Environment.PREPROD) {
            homePage.selectEnglishLanguageOnly();
            homePage.openVerticalPage("Train");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            homePage.openVerticalPage("Trains");
        }

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle one way");
        trainSearchPage.toggleTripType(tripType);

        step("User select departure date");
        trainSearchPage.selectDate(departureDate);

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User open filter & sort chips");
        trainSearchResultPage.clickFilterChips();

        step("User click class type section");
        trainSearchResultPage.selectClassType();

        step("User select class type");
        trainSearchResultPage.filterByClass(trainClass);

        step("User click save button");
        trainSearchResultPage.clickSaveButton();

        step("Assert train class type shown in SRP is correct");
        Assertion.assertThat("Verify class type in SRP after filter", trainSearchResultPage.verifyClass(trainClass), is(true));
    }

    @Author(names = "Hafizh")
    @Module(name = "SRP")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTrainFilterByNameGuest() throws InterruptedException {
        Map<String, String> param = getParam("testVerifyTrainFilterByNameGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String trainName = param.get("trainName");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step ("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open train vertical");
        if (Configs.ENVIRONMENT == Environment.GK|| Configs.ENVIRONMENT == Environment.PREPROD) {
            homePage.selectEnglishLanguageOnly();
            homePage.openVerticalPage("Train");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            homePage.openVerticalPage("Trains");
        }

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle one way");
        trainSearchPage.toggleTripType(tripType);

        step("User select departure date");
        trainSearchPage.selectDate(departureDate);

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User open filter & sort chips");
        trainSearchResultPage.clickFilterChips();

        step("User click train name section");
        trainSearchResultPage.selectTrainNameSectionInFilter();

        step("User select train name");
        trainSearchResultPage.filterByName(trainName);

        step("User click save button");
        trainSearchResultPage.clickSaveButton();

        step("Assert train name shown in SRP is correct");
        Assertion.assertThat("Verify train name in SRP after filter", trainSearchResultPage.verifyName(trainName), is(true));
    }

    @Author(names = "Hafizh")
    @Module(name = "SRP")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testVerifyTrainFilterByNameLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("testVerifyTrainFilterByNameLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String originStation = param.get("originStation");
        String destinationStation = param.get("destinationStation");
        String tripType = param.get("tripType");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String trainName = param.get("trainName");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TrainSearchPageWeb<?> trainSearchPage = TrainSearchPageWeb.getInstance(session);
        TrainSearchResultPageWeb<?> trainSearchResultPage = TrainSearchResultPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step ("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User open train vertical");
        if (Configs.ENVIRONMENT == Environment.GK|| Configs.ENVIRONMENT == Environment.PREPROD) {
            homePage.selectEnglishLanguageOnly();
            homePage.openVerticalPage("Train");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            homePage.openVerticalPage("Trains");
        }

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User select origin station");
        trainSearchPage.selectOrigin(originStation);

        step("User select destination station");
        trainSearchPage.selectDestination(destinationStation);

        step("User toggle one way");
        trainSearchPage.toggleTripType(tripType);

        step("User select departure date");
        trainSearchPage.selectDate(departureDate);

        step("User search train");
        trainSearchPage.searchTrain();

        step("User skip pop-up");
        trainSearchPage.clickSkipPopup();

        step("User open filter & sort chips");
        trainSearchResultPage.clickFilterChips();

        step("User click train name section");
        trainSearchResultPage.selectTrainNameSectionInFilter();

        step("User select train name");
        trainSearchResultPage.filterByName(trainName);

        step("User click save button");
        trainSearchResultPage.clickSaveButton();

        step("Assert train name shown in SRP is correct");
        Assertion.assertThat("Verify train name in SRP after filter", trainSearchResultPage.verifyName(trainName), is(true));
    }
}