package com.tiket.test.accommodation.review;

import com.tiket.apiPage.HotelApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.model.SortItem;
import com.tiket.page.accommodation.hotel.HotelPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.reviewpage.ReviewPage;
import com.tiket.page.account.AccountPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.loyalty.my_review.MyReviewPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.ttd.TTDPage;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.page.ttd.searchpage.TTDSearchPage;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.pojo.accommodation.hotel.HotelAutoCompleteResponse;
import com.tiket.pojo.loyalty.PendingReviewResponse;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.generateCheckInOutDate;
import static com.tiket.model.Language.ENGLISH;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class MyReviewTest extends BaseTest {
    @Author(names = {"Yazid"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testFilterReviewTTD() {
        Map<String, String> map = getParam("testFilterReviewTTD");
        BaseTest.paramsThreadLocal.set(map);
        String slugProductName = map.get("slugProductName");
        String category = map.get("category");
        int page = Integer.parseInt(map.get("page"));

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        MyReviewPage<WebDriver> myReviewPage = MyReviewPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on non-login mode");
        homePage.logoutViaDeeplink();

        step("user on the todos detail page");
        ttdSearchResultPage.openPdpDeeplink(slugProductName);
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();

        step("user click on button See All on the review widget");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.clickTabSection("Review", "Reviews", "right");
        ttdProductDetailPageWeb.openAllReview();
        webViewPage.switchToNative();

        step("user should be able to see the review is filtered according to the selected filter category ");
        Assertion.assertThat("Review is filtered according to the selected filter category", myReviewPage.verifyCategoryFilter(category,page), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testSortReviewTTD() {
        Map<String, String> map = getParam("testSortReviewTTD");
        BaseTest.paramsThreadLocal.set(map);
        String slugProductName = map.get("slugProductName");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        MyReviewPage<WebDriver> myReviewPage = MyReviewPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change Language to " + ENGLISH);
        homePage.openAccount();
        accountPage.selectLanguage("ENG");
        settingPage.backToHome();

        step("user on the todos detail page");
        ttdSearchResultPage.openPdpDeeplink(slugProductName);
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();

        step("user click on button See All on the review widget");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.clickTabSection("Review", "Reviews", "right");
        ttdProductDetailPageWeb.openAllReview();
        webViewPage.switchToNative();

        step("user click on sorting on review details page");
        myReviewPage.openSortFilter();
        SortItem selectedSortItem = myReviewPage.selectSortItem();

        step("user should be able to see the review is sorted according to the selected sort");
        Assertion.assertThat("Review is sorted according to the selected sort", myReviewPage.checkSortResult(selectedSortItem), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testFilterReviewHotel() {
        Map<String, String> map = getParam("testFilterReviewHotel");
        BaseTest.paramsThreadLocal.set(map);
        String hotelName = map.get("hotelName");
        String category = map.get("category");

        String accommodationType = "hotel";
        String[] dates = generateCheckInOutDate("1-1", 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        ReviewPage<WebDriver> reviewPage = ReviewPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on non-login mode");
        loginPage.loginAsGuest();

        step("user on the hotel detail page");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", hotelName, accommodationType.toUpperCase());
        int index = hotelApi.findHotelIndex(hotelAutoCompleteResponse , hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(index).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(index).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 1, 1, checkInDate, checkOutDate);
        detailPage.getHotelNameDetail();

        step("user click on button See All on the review widget");
        detailPage.openReviewSection();
        detailPage.seeAllReview();

        step("user should be able to see the review is filtered according to the selected filter");
        reviewPage.verifyCategoryFilter(category);
    }

    @Author(names = {"Kartik"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testSubmitReviewTTD() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testSubmitReviewTTD");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        MyReviewPage<WebDriver> myReviewPage = MyReviewPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        step("Hit API to create Pending Review for TTD");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String username = accountData.email;
        String password = accountData.password;
        String loginType = "email";
        String automationIdentifier = "Automated review TTD "+ CoreFunctions.randomInt(5);
        myReviewPage.postPendingReviewTTD(username, password,automationIdentifier);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Open review from my account page");
        homePage.openAccount();
        accountPage.openMyReviewPage();

        step("fill review star");
        myReviewPage.tellTheStory(automationIdentifier);

        step("fill review text and send");
        myReviewPage.fillReviewItem();
        Assertion.assertThat("Review Submitted Successfully", myReviewPage.verifyAndClickReviewPopup(), is(true));

        step("delete account");
        homePage.goToHomeDeeplink();
        loginPage.loginAsGuest();
        unm.deleteAccountViaApi(accountData.unmData);
    }

    @Author(names = {"Ashish"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testSubmitReviewHotel() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testSubmitReviewHotel");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        MyReviewPage<WebDriver> myReviewPage = MyReviewPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Create new user from the API");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String username = accountData.email;
        String password = accountData.password;
        String loginType = "email";
        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Create dummy pending review from the API");
        String inventoryName = "Automation Review Hotel - " + CoreFunctions.randomInt(5);
        myReviewPage.postPendingReviewHotel(username, password, inventoryName);

        try {
            step("Open review from my account page");
            accountPage.openAccountPageUsingDeeplink();
            accountPage.openMyReviewPage();

            step("fill review star");
            myReviewPage.tellTheStory(inventoryName);

            step("fill review text and send");
            myReviewPage.fillReviewItem();
            Assertion.assertThat("Review Submitted Successfully", myReviewPage.verifyAndClickReviewPopup(), is(true));

            step("assert redirection to review page after submit review");
            myReviewPage.isOnMyReviewForm();
        } finally {
            try {
            step("delete account");
            homePage.goToHomeDeeplink();
            loginPage.loginAsGuest();
            unm.deleteAccountViaApi(accountData.unmData);
            } catch (Exception | AssertionError e) {
                step("delete account failed, ignored: " + e.getMessage());
            }
        }
    }

    @Author(names = {"Prerna"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testSubmitReviewCarRental() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testSubmitReviewCarRental");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        MyReviewPage<WebDriver> myReviewPage = MyReviewPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        step("Hit API to create Pending Review");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String username = accountData.email;
        String password = accountData.password;
        String loginType = "email";
        String automationIdentifier = "Automated review car "+CoreFunctions.randomInt(5);
        myReviewPage.postPendingReviewCarRental(username, password,automationIdentifier);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Open review from my account page");
        homePage.openAccount();
        accountPage.openMyReviewPage();

        step("fill review star");
        myReviewPage.tellTheStory(automationIdentifier);

        step("fill review text and send");
        myReviewPage.fillReviewItem();
        Assertion.assertThat("Review Submitted Successfully", myReviewPage.verifyAndClickReviewPopup(), is(true));

        step("delete account");
        homePage.goToHomeDeeplink();
        loginPage.loginAsGuest();
        unm.deleteAccountViaApi(accountData.unmData);
    }

    @Author(names = {"Zia"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testSubmitReviewAT(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testSubmitReviewAT");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        MyReviewPage<WebDriver> myReviewPage = MyReviewPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        step("Open app");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String username = accountData.email;
        String password = accountData.password;

        step("Hit API to create Pending Review for Airport Transfer");
        String automationIdentifier = "Automated review AT "+CoreFunctions.randomInt(5);
        myReviewPage.postPendingReviewAT(username, password, automationIdentifier);

        step("Open app");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Swipe To Review Section");
        myReviewPage.swipeToCard(automationIdentifier);

        step("fill review star");
        myReviewPage.clickReviewStar(automationIdentifier);
        myReviewPage.tellTheStory(automationIdentifier);

        step("fill review text and send");
        myReviewPage.fillReviewItem();

        step("verify review submitted");
        Assertion.assertThat("Submit review is success", myReviewPage.verifyAndClickReviewPopup(), is(true));

        step("delete account");
        homePage.goToHomeDeeplink();
        loginPage.loginAsGuest();
        unm.deleteAccountViaApi(accountData.unmData);
    }

    @Author(names = {"Dendi"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testHelpfullnessTtd() throws NumberFormatException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testdata = getParam("testHelpfullnessTtd");
        BaseTest.paramsThreadLocal.set(testdata);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        MyReviewPage<WebDriver> myReviewPage = MyReviewPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String email = testdata.get("email");
        String password = testdata.get("password");
        String product = testdata.get("product");
        String automationIdentifier = "Automated review TTD "+CoreFunctions.randomInt(5);

        step("Get access token");
        String accessToken = unm.getAccessToken(email,password,"B2C");

        step("Generate new review");
        String orderId = myReviewPage.postPendingReviewTTD(email, password, automationIdentifier);
        PendingReviewResponse responsePendingReview = myReviewPage.getCurrentPendingReview(accessToken);
        String reviewToken = myReviewPage.getReviewTokenByOrderId(responsePendingReview, orderId);
        boolean isSubmitReviewSuccess = myReviewPage.postSubmitReview(accessToken, reviewToken, reviewToken);
        Assertion.assertThat("Verify submit review success", isSubmitReviewSuccess, is(true));


        step("user open app");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("user login using "+email+" account");
        loginPage.loginWith("email", email, password);

        step("Get app lang");
        String appLang = homePage.getLanguageApp();
        String expectedWording = appLang.equalsIgnoreCase("id") ? testdata.get("expectedWordingId") : testdata.get("expectedWordingEn");

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Dos", "", "");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Open review from ttd product page");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.clickTabSection("Review", "Reviews", "right");
        ttdProductDetailPageWeb.openAllReview();
        webViewPage.switchToNative();

        step("Assert that review page open");
        Assertion.assertThat("Assert review page open", myReviewPage.verifyProductReviewPageOpen(), is(true));

        step("Click helpfulness button");
        myReviewPage.clickHelpfulnessButton();

        step("Check if helpfullness clicked");
        myReviewPage.checkHelfulnessWordingChangeAfterClicked(expectedWording);
    }

    @Author(names = {"Kartik"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testReportReviewTTD() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testReportReviewTTD");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        MyReviewPage<WebDriver> myReviewPage = MyReviewPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String loginType = map.get("loginType");
        String product = map.get("product");

        step("create a new account");
        Unm<WebDriver> unm = new Unm<>(session);

        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String username = accountData.email;
        String password = accountData.password;

        try{
            step("Open app");
            homePage.gotoHome();

            step("Login as " + username + " via deeplink");
            homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

            step("Open TTD landing page");
            homePage.openVerticalPageDeeplink("To Dos", "", "");

            step("Search the product");
            ttdLandingPage.clickSearchBar();
            ttdSearchPage.inputSearchKeyword(product);

            step("select the activity");
            ttdSearchPage.selectRecommendedActivity(product);

            step("Open review from ttd product page");
            webViewPage.switchToWebViewUrl("to-do");
            ttdProductDetailPageWeb.goToPackagesList();
            ttdProductDetailPageWeb.clickTabSection("Review", "Reviews", "right");
            ttdProductDetailPageWeb.openAllReview();
            webViewPage.switchToNative();

            step("Report Review");
            Boolean reviewReported = myReviewPage.reportReviewTTD();

            step("Verify review is reported");
            Assertion.assertThat("Review Reported Successfully", reviewReported, is(true));

            step("go to home");
            homePage.goToHomeDeeplink();
            loginPage.loginAsGuest();
        }finally {
            step("delete account");
            unm.deleteAccountViaApi(accountData.unmData);
        }
    }

    @Author(names = {"Prerna"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testReportReviewHotel() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testReportReviewHotel");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        MyReviewPage<WebDriver> myReviewPage = MyReviewPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        String loginType = map.get("loginType");
        String hotelName = map.get("hotelName");

        step("create a new account");
        Unm<WebDriver> unm = new Unm<>(session);

        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String username = accountData.email;
        String password = accountData.password;

        step("Goto home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay HotelName");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to review section");
        detailPage.openReviewSection();
        detailPage.seeAllReview();

        step("Report Review");
        Boolean reviewReported = myReviewPage.reportReviewTTD();

        step("go to home");
        homePage.goToHomeDeeplink();
        loginPage.loginAsGuest();

        step("delete account");
        unm.deleteAccountViaApi(accountData.unmData);

        step("Verify review is reported");
        Assertion.assertThat("Review Reported Successfully", reviewReported, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testHelpfullnessHotel() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testHelpfullnessHotel");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        MyReviewPage<WebDriver> myReviewPage = MyReviewPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String hotelName = map.get("hotelName");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go to home");
        homePage.gotoHome();

        step("Get access token");
        String accessToken = unm.getAccessToken(username,password,"B2C");

        step("Generate new review");
        String orderId = myReviewPage.postPendingReviewHotel(username, password, hotelName);
        PendingReviewResponse responsePendingReview = myReviewPage.getCurrentPendingReview(accessToken);
        String reviewToken = myReviewPage.getReviewTokenByOrderId(responsePendingReview, orderId);
        boolean isSubmitReviewSuccess = myReviewPage.postSubmitReview(accessToken, reviewToken, reviewToken);
        Assertion.assertThat("Verify submit review success", isSubmitReviewSuccess, is(true));

        step("User login");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Get app lang");
        String appLang = homePage.getLanguageApp();
        String expectedWording = appLang.equalsIgnoreCase("id") ? map.get("expectedWordingId") : map.get("expectedWordingEn");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", appLang, "");

        step("Search stay HotelName");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to review section");
        detailPage.openReviewSection();
        detailPage.seeAllReview();

        step("Assert that review page open");
        Assertion.assertThat("Assert review page open", myReviewPage.verifyProductReviewPageOpen(), is(true));

        step("Click helpfulness button");
        myReviewPage.clickHelpfulnessButton();

        step("Check if helpfullness clicked");
        myReviewPage.checkHelfulnessWordingChangeAfterClicked(expectedWording);
    }

    @Author(names = {"Zia"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testTopicReviewHotel() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testTopicReviewHotel");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        ReviewPage<WebDriver> reviewPage = ReviewPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String accommodationType = "Hotel";
        String hotelName = param.get("hotelName");
        String hotelPublicId = param.get("hotelPublicId");
        String rangeAddDate = param.get("rangeAddDate");
        int totalNight = Integer.parseInt(param.get("night"));
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];

        step("user open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("Get App language");
        String lang = homePage.getLanguageApp();

        step("get review topics and sample review");
        String topic = hotelApi.getReviewTopicPublicId(hotelPublicId,lang);
        String[] topicSplit = topic.split("-");
        String topicLabel = topicSplit[0];
        String topicCode = topicSplit[1];

        step("topic chosen "+topic);
        List<String> reviewsToCheck = hotelApi.getReviewFromTopicPublicId(hotelPublicId,lang,topicCode);
        Assertion.assertThat("Reviews exist",reviewsToCheck,is(not(emptyIterable())));

        step("user click PDP deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", hotelName, accommodationType.toUpperCase());
        int hotelPublicIdIndex = hotelApi.findHotelIndexPublicId(hotelAutoCompleteResponse, hotelPublicId);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(hotelPublicIdIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(hotelPublicIdIndex).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 1, 1, checkInDate, checkOutDate);

        step("Open review");
        detailPage.openReviewSection();
        detailPage.seeAllReview();

        step("Select "+topic+" topic");
        reviewPage.selectTopic(topicLabel);

        step("Verify review appeared same as API");
        List<String> reviewsFromPage = reviewPage.getReviews();
        Assertion.assertThat("Reviews from API and UI are same",reviewsFromPage,is(reviewsToCheck));
    }

    @Author(names = {"Zia"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testTopicReviewTtd() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testTopicReviewTtd");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        ReviewPage<WebDriver> reviewPage = ReviewPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        String slugProductName = param.get("slugProductName");

        step("user open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("Get App language");
        String lang = homePage.getLanguageApp();

        step("get review topics and sample review");
        String topic = ttdPage.getReviewTopic(slugProductName,lang);
        String[] topicSplit = topic.split("-");
        String topicLabel = topicSplit[0];
        String topicCode = topicSplit[1];

        step("topic chosen "+topic);
        List<String> reviewsToCheck = ttdPage.getReviewFromTopic(slugProductName,lang,topicCode);
        Assertion.assertThat("Reviews exist",reviewsToCheck,is(not(emptyIterable())));

        step("open PDP via deeplink");
        ttdSearchResultPage.openPdpDeeplink(slugProductName);

        step("Open the review page");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.clickTabSection("Review", "Reviews", "right");
        ttdProductDetailPageWeb.openAllReview();
        webViewPage.switchToNative();

        step("Select "+topic+" topic");
        reviewPage.selectTopic(topicLabel);

        step("Verify review appeared same as API");
        List<String> reviewsFromPage = reviewPage.getReviews();
        Assertion.assertThat("Reviews from API and UI are same",reviewsFromPage,is(reviewsToCheck));
    }

    @Author(names = {"Zia"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testHotelPdpReviewWidget() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testHotelPdpReviewWidget");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        ReviewPage<WebDriver> reviewPage = ReviewPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String accommodationType = "Hotel";
        String hotelName = param.get("hotelName");
        String rangeAddDate = param.get("rangeAddDate");
        int totalNight = Integer.parseInt(param.get("night"));
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];

        step("user open app");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("user click PDP deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 1, 1, checkInDate, checkOutDate);

        step("Open review");
        detailPage.openReviewSection();

        step("Assert review widget swiping");
        Assertion.assertThat("Hotel review widget can swipe left and right", detailPage.checkReviewWidgetSwipe(), is(true));

        step("Verify can click see all reviews");
        detailPage.seeAllReview();
        Assertion.assertThat("Hotel review see all can redirect", reviewPage.isReviewPageDisplayed(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testReviewSectionWidgetsTTD() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testReviewSectionWidgetsTTD");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String username = map.get("username");
        String product = map.get("product");

        step("Open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();
        homePage.changeCurrency("IDR",username);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Dos", "", "");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Open review from ttd product page");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.verifyReviewSectionExists();
    }

    @Author(names = {"Ashish"})
    @Module(name = "MyReview")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testSortingOnReviewDetailPageHotelsGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testSortingOnReviewDetailPageHotelsGuest");
        BaseTest.paramsThreadLocal.set(param);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        ReviewPage<WebDriver> reviewPage = ReviewPage.getInstance(session);

        String hotelName = param.get("hotelName");

        step("Goto home");
        homePage.gotoHome();

        step("User open app as guest");
        loginPage.loginAsGuest();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay HotelName");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to review section");
        detailPage.openReviewSection();
        detailPage.seeAllReview();

        step("Check review section");
        Assertion.assertThat("Review section is open", reviewPage.isReviewPageDisplayed(), is(true));

        step("check sorted filter review card low to high");
        Assertion.assertThat("Check sorted filter review card low to high", reviewPage.checkSortedReviewCardLowToHigh(), is(true));

        step("check sorted filter review card high to low");
        Assertion.assertThat("Check sorted filter review card high to low", reviewPage.checkSortedReviewCardHighToLow(), is(true));
    }
}