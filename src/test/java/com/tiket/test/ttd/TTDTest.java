package com.tiket.test.ttd;

import com.costumeMatcher.CaseInsensitiveSubstringMatcher;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.MemberApi;
import com.tiket.apiPage.OrderApi;
import com.tiket.apiPage.TTDApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.account.AccountPage;
import com.tiket.page.common.phone.settings.DefaultAppsPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.loyalty.ulp_tiket.UlpPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.order.reservation.*;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.refund.RefundPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.ttd.TTDPage;
import com.tiket.page.ttd.destinationpage.TTDDestinationPage;
import com.tiket.page.ttd.explorepage.TTDExplorePage;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.page.ttd.partnerPage.TTDPartnerPage;
import com.tiket.page.ttd.productdetailpage.TTDProductDetailPage;
import com.tiket.page.ttd.searchpage.TTDSearchPage;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.page.ttd.selectedpackagedetailpage.TTDSelectedPackageDetailPage;
import com.tiket.page.ttd.tiketlivepage.TTDTiketLivePage;
import com.tiket.page.ttd.videorentalpage.VideoRentalPage;
import com.tiket.pojo.member.AccountData;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.ttd.*;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.home.InfoPageWeb;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdPackageDetailPage.TtdPackageDetailPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import com.tiket.web.page.ttd.TtdReviewPage.TtdReviewPageWeb;
import com.tiket.web.page.ttd.TtdSelectedPackageDetailPage.TtdSelectedPackageDetailPageWeb;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.model.PriceType.DECIMAL;
import static com.tiket.model.PriceType.THOUSAND;
import static com.tiket.testbase.Assertion.skip;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class TTDTest extends BaseTest {

    //OTW test case
    @Author(names = {"Yazid"})
    @Module(name = "Login")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void checkLoginBanner() {
        Map<String, String> map = getParam("checkLoginBanner");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String productCategoryCodes = map.get("productCategoryCodes");
        String slugProductNamePdp = map.get("slugProductNamePdp");
        String expectedProductNamePdp = map.get("expectedProductNamePdp");
        String slugProductNameBooking = map.get("slugProductNameBooking");
        String expectedProductNameBooking = map.get("expectedProductNameBooking");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        step("User on the home page as guest session");
        homePage.gotoHome();
        loginPage.loginAsGuest();

        step("User is on SRP");
        ttdSearchResultPage.openSrpDeeplink(productCategoryCodes);

        step("User should be able to click login from SRP and will be directed to the login page");
        ttdSearchResultPage.clickJoinNow();

        step("User input credential on login page");
        loginPage.inputEmailOrPhoneNumberNative(username);
        loginPage.loginUNM(loginType, username, password);

        step("When user successfully logged in, user should be directed back to the previous state before login: SRP");
        Assertion.assertThat("User back to the SRP", ttdSearchResultPage.isSearchResultPageShown(), is(true));
        Assertion.assertThat("Login banner on SRP is not there", ttdSearchResultPage.isLoginBannerDisplayed(), is(false));

        step("Back to home and logout");
        homePage.goToHomeDeeplink();
        loginPage.loginAsGuest();

        step("User is on PDP");
        ttdSearchResultPage.openPdpDeeplink(slugProductNamePdp);
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();

        step("User should be able to click login from PDP and will be directed to the login page");
        ttdProductDetailPageWeb.openLoginFromRequiredLogin();

        step("User input credential on login page");
        webViewPage.switchToNative();
        loginPage.inputEmailOrPhoneNumberNative(username);
        loginPage.loginUNM(loginType, username, password);

        step("When user successfully logged in, user should be directed back to the previous state before login: PDP");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("User back to the PDP", ttdProductDetailPageWeb.getProductName(slugProductNamePdp).toLowerCase(), is(expectedProductNamePdp.toLowerCase()));
        Assertion.assertThat("Login banner on PDP is not there", ttdProductDetailPageWeb.isLoginOverlayDisplayed(), is(false));

        step("Back to home and logout");
        homePage.goToHomeDeeplink();
        loginPage.loginAsGuest();

        step("User is on Booking form");
        ttdSearchResultPage.openPdpDeeplink(slugProductNameBooking);
        ttdProductDetailPageWeb.getProductName();
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(1);
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(1, 0);
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("User should be able to click login from Booking Form and will be directed to the login page");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.clickLoginNow();

        step("User input credential on login page");
        webViewPage.switchToNative();
        loginPage.inputEmailOrPhoneNumberNative(username);
        loginPage.loginUNM(loginType, username, password);

        step("When user successfully logged in, user should be directed back to the previous state before login: Booking Form");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("User back to the Booking Form", ttdBookingFormPageWeb.getProductTitle().toLowerCase(), is(expectedProductNameBooking.toLowerCase()));
        Assertion.assertThat("Login banner on Booking Form is not there", ttdBookingFormPageWeb.isLoginNowButtonDisplayed(), is(false));
    }

    //OTW test case
    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void checkNearestCampaign() {
        Map<String, String> map = getParam("checkNearestCampaign");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String productCategoryCodes = map.get("productCategoryCodes");
        String location = map.get("location");
        String locationType = map.get("locationType");
        String campaignName = map.get("campaignName");
        String campaignNameCard = map.get("campaignNameCard");
        int totalActivity = Integer.parseInt(map.get("totalActivity"));

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("User on the home page with login session");
        homePage.gotoHome();
        loginPage.loginWith(loginType, username, password);

        step("User is on SRP");
        ttdSearchResultPage.openSrpDeeplink(productCategoryCodes);

        step("Change the location: " + location);
        ttdSearchResultPage.clickLocation();
        ttdSearchResultPage.inputLocationSearchBar(location);
        ttdSearchResultPage.clickLocationSearchResult(location, locationType);
        Assertion.assertThat("SRP location should be : " + location, ttdSearchResultPage.getLocation(), is(location));

        step("Choose the campaign name: "+campaignName+" from the filter");
        ttdSearchResultPage.clickFilter();
        ttdSearchResultPage.selectPromoFilter(campaignName);
        ttdSearchResultPage.submitFilter();

        if (!campaignNameCard.isEmpty()) campaignName = campaignNameCard;
        step("Check SRP in nearest location user: "+location+" with Category and sorted by Campaign: "+campaignName+", with Campaign on Card: "+campaignNameCard);
        Assertion.assertThat("Show SRP products list with the selected Campaign and user's nearest location", ttdSearchResultPage.checkLocationCampaign(location, campaignName, totalActivity), is(true));
    }

    //OTW test case
    @Author(names = {"Yazid"})
    @Module(name = "Campaign")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void checkRegularCampaign() {
        Map<String, String> param = getParam("checkRegularCampaign");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String sectionTitleHome = param.get("sectionTitleHome");
        String productNameHome = param.get("productNameHome");
        String campaignLabelHome = param.get("campaignLabelHome");
        String sectionTitleTtd = param.get("sectionTitleTtd");
        String productNameTtd = param.get("productNameTtd");
        String campaignLabelTtd = param.get("campaignLabelTtd");
        String productCategoryCodes = param.get("productCategoryCodes");
        String destinationName = param.get("destinationName");
        String destinationType = param.get("destinationType");
        String productNameSrp = param.get("productNameSrp");
        String campaignLabelSrp = param.get("campaignLabelSrp");
        int totalScrollSrp = Integer.parseInt(param.get("totalScrollSrp"));
        String packageName = param.get("packageName");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        step("User on the home page with login session");
        homePage.gotoHome();
        String currentEmail = homePage.getCurrentEmail();
        if(!currentEmail.equals(username)) {
            loginPage.loginAsGuest();
            loginPage.loginWith(loginType, username, password);
        }

        step("User scroll to the \""+sectionTitleHome+"\" & select \""+productNameHome+"\" product");
        homePage.goToSection(sectionTitleHome);
        String campaignLabel = homePage.getCampaignProduct(productNameHome);
        Assertion.assertThat("Campaign label should be visible on the Home Page", campaignLabel.toLowerCase(), is(campaignLabelHome.toLowerCase()));

        step("User is on TTD home and scroll to \""+sectionTitleTtd+"\" section & select \""+productNameTtd+"\" product");
        homePage.goToHomeDeeplink();
        homePage.openVerticalPageDeeplink("To Do", "", "IDR");
        ttdLandingPage.goToSection(sectionTitleTtd);
        campaignLabel = ttdLandingPage.getCampaignProduct(productNameTtd);
        Assertion.assertThat("Campaign label should be visible on the TTD Landing Page", campaignLabel.toLowerCase(), is(campaignLabelTtd.toLowerCase()));

        step("User is on SRP with location as \""+destinationName+"\" & select \""+productNameSrp+"\" product");
        ttdSearchResultPage.openSrpDeeplink(productCategoryCodes);
        ttdSearchResultPage.clickLocation();
        ttdSearchResultPage.inputLocationSearchBar(destinationName);
        ttdSearchResultPage.clickLocationSearchResult(destinationName, destinationType);
        campaignLabel = ttdSearchResultPage.findCampaignProduct(productNameSrp, totalScrollSrp);
        Assertion.assertThat("Campaign label should be visible on the SRP", campaignLabel.toLowerCase(), is(campaignLabelSrp.toLowerCase()));

        step("User is on PDP \""+productNameSrp+"\"");
        ttdSearchResultPage.chooseProduct(productNameSrp, totalScrollSrp);
        webViewPage.switchToWebViewUrl("to-do");
        campaignLabel = ttdProductDetailPageWeb.getCampaignLabel();
        Assertion.assertThat("Campaign label should be visible on the PDP", campaignLabel.toLowerCase(), is(campaignLabelSrp.toLowerCase()));

        step("Create booking on \""+campaignLabel+"\" campaign package: " + packageName);
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicketCampaign(packageName, campaignLabel);
        ttdSelectedPackageDetailPageWeb.chooseTomorrowDate();
        ttdSelectedPackageDetailPageWeb.setTicketQty(1, 0);
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();
        String priceDiscount = ttdBookingFormPageWeb.getDiscountPrice();
        Assertion.assertThat("Price discount is visible on the Booking Form Page price details", priceDiscount, is(containsString("-")));

        step("Successfully booked product with the vertical campaign with the deal amount");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();
        Assertion.assertThat("Order ID should be generated on the payment page", paymentPage.checkOrderID(), is(not(emptyOrNullString())));
        String actualDiscountPrice = paymentPage.getDiscountPrice();
        Assertion.assertThat("Price discount is visible on the Payment Page price details", actualDiscountPrice, is(containsString("-")));
        try {
            Assertion.assertThat("Price discount should be equal with the booking form page", removeDiscount(actualDiscountPrice), is(removeDiscount(priceDiscount)));
        } catch (Exception e) {
            double result = convertPriceSafely(priceDiscount) - convertPriceSafely(actualDiscountPrice);
            Assertion.assertThat("Price discount should be more than or less than 1", result, either(is(-1.0)).or(is(1.0)));
        }
    }

    @Author(names = {"Yazid"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyPriceSeparator() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyPriceSeparator");
        BaseTest.paramsThreadLocal.set(map);
        String currency = map.get("currency");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User use the app as guest");
        loginPage.loginAsGuest();

        String language = "IDN";
        step("Change language to \"" + language + "\"");
        homePage.openAccount();
        accountPage.selectLanguage(language);
        settingPage.backToHome();

        step("Change currency to \"" + currency + "\"");
        homePage.changeCurrency(currency, "");

        step("Open TTD Landing Page");
        language = homePage.getLanguageApp();
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("User will see \".\" for thousand and \",\" for decimal when user language is ID and currency is Non-IDR");
        Assertion.assertThat("thousand price should contains \".\"", ttdLandingPage.findPrice(THOUSAND, currency.toUpperCase()), containsString("."));
        ttdLandingPage.backToHomePage();
        homePage.openVerticalPageDeeplink("To Do", language, currency);
        Assertion.assertThat("decimal price should contains \",\"", ttdLandingPage.findPrice(DECIMAL, currency.toUpperCase()), containsString(","));

        step("Back to Homepage");
        ttdLandingPage.backToHomePage();

        language = "ENG";
        step("Change language to \"" + language + "\"");
        homePage.openAccount();
        accountPage.selectLanguage(language);
        settingPage.backToHome();

        step("Open TTD Landing Page");
        language = homePage.getLanguageApp();
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("User will see \".\" for decimal and \",\" for thousand when user language is EN and currency is Non-IDR");
        Assertion.assertThat("decimal price should contains \".\"", ttdLandingPage.findPrice(DECIMAL, currency.toUpperCase()), containsString("."));
        ttdLandingPage.backToHomePage();
        homePage.openVerticalPageDeeplink("To Do", language, currency);
        Assertion.assertThat("thousand price should contains \",\"", ttdLandingPage.findPrice(THOUSAND, currency.toUpperCase()), containsString(","));
    }

    @Author(names = {"Akbar","Kartik"})
    @Module(name = "DestinationPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkSRPViaCityDestinationPageGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkSRPViaCityDestinationPageGuest");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDDestinationPage<WebDriver> ttdDestinationPage = TTDDestinationPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String keyword = map.get("search_input");
        String type = map.get("search_type");

        step("Go To Home");
        homePage.gotoHome();

        step("User use the app as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click search bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Searching for product to reach on PDP | search_input = '" + keyword + "'");
        ttdSearchPage.inputSearchKeyword(keyword);
        ttdSearchPage.selectRecommendedActivity(keyword);

        step("swipe to bottom to see Destination breadcrumb");
        webViewPage.switchToWebViewUrl("to-do");
        boolean isDestinationBreadcrumbVisible = ttdProductDetailPageWeb.swipeToDestinationSectionBottom();
        Assertion.assertThat("Assert if Destination breadcrumb is Visible", isDestinationBreadcrumbVisible, is(true));

        step("Select city name from Destination breadcrumbs");
        String selectedDestination = ttdProductDetailPageWeb.selectDestinationByHierarchy(type);
        webViewPage.switchToNative();

        step("Asserting '" + type + "' section on destination page");
        boolean isDestinationPagValid = ttdDestinationPage.isDestinationPageValid("",selectedDestination);
        Assertion.assertThat("Assert if the '" + type + "' is already valid", isDestinationPagValid, is(true));
    }

    @Author(names = {"Akbar","Kartik"})
    @Module(name = "DestinationPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkSRPViaCityDestinationPageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkSRPViaCityDestinationPageLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDDestinationPage<WebDriver> ttdDestinationPage = TTDDestinationPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String keyword = map.get("search_input");
        String type = map.get("search_type");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click search bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Searching for product to reach on PDP | search_input = '" + keyword + "'");
        ttdSearchPage.inputSearchKeyword(keyword);
        ttdSearchPage.selectRecommendedActivity(keyword);

        step("swipe to bottom to see Destination breadcrumb");
        webViewPage.switchToWebViewUrl("to-do");
        boolean isDestinationBreadcrumbVisible = ttdProductDetailPageWeb.swipeToDestinationSectionBottom();
        Assertion.assertThat("Assert if Destination breadcrumb is Visible", isDestinationBreadcrumbVisible, is(true));

        step("Select city name from Destination breadcrumbs");
        String selectedDestination = ttdProductDetailPageWeb.selectDestinationByHierarchy(type);
        webViewPage.switchToNative();

        step("Asserting '" + type + "' section on destination page");
        boolean isDestinationPagValid = ttdDestinationPage.isDestinationPageValid("",selectedDestination);
        Assertion.assertThat("Assert if the '" + type + "' is already valid", isDestinationPagValid, is(true));
    }

    @Author(names = {"Akbar","Kartik"})
    @Module(name = "DestinationPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkSRPViaRegionDestinationPageGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkSRPViaRegionDestinationPageGuest");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDDestinationPage<WebDriver> ttdDestinationPage = TTDDestinationPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String keyword = map.get("search_input");
        String type = map.get("search_type");

        step("Go To Home");
        homePage.gotoHome();

        step("User use the app as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click search bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Searching for product to reach on PDP | search_input = '" + keyword + "'");
        ttdSearchPage.inputSearchKeyword(keyword);
        ttdSearchPage.selectRecommendedActivity(keyword);

        step("swipe to bottom to see Destination breadcrumb");
        webViewPage.switchToWebViewUrl("to-do");
        boolean isDestinationBreadcrumbVisible = ttdProductDetailPageWeb.swipeToDestinationSectionBottom();
        Assertion.assertThat("Assert if Destination breadcrumb is Visible", isDestinationBreadcrumbVisible, is(true));

        step("Select city name from Destination breadcrumbs");
        String selectedDestination = ttdProductDetailPageWeb.selectDestinationByHierarchy(type);
        webViewPage.switchToNative();

        step("Asserting '" + type + "' section on destination page");
        boolean isDestinationPagValid = ttdDestinationPage.isDestinationPageValid("",selectedDestination);
        Assertion.assertThat("Assert if the '" + type + "' is already valid", isDestinationPagValid, is(true));
    }

    @Author(names = {"Akbar","Kartik"})
    @Module(name = "DestinationPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkSRPViaRegionDestinationPageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkSRPViaRegionDestinationPageLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDDestinationPage<WebDriver> ttdDestinationPage = TTDDestinationPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String keyword = map.get("search_input");
        String type = map.get("search_type");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click search bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Searching for product to reach on PDP | search_input = '" + keyword + "'");
        ttdSearchPage.inputSearchKeyword(keyword);
        ttdSearchPage.selectRecommendedActivity(keyword);

        step("swipe to bottom to see Destination breadcrumb");
        webViewPage.switchToWebViewUrl("to-do");
        boolean isDestinationBreadcrumbVisible = ttdProductDetailPageWeb.swipeToDestinationSectionBottom();
        Assertion.assertThat("Assert if Destination breadcrumb is Visible", isDestinationBreadcrumbVisible, is(true));

        step("Select city name from Destination breadcrumbs");
        String selectedDestination = ttdProductDetailPageWeb.selectDestinationByHierarchy(type);
        webViewPage.switchToNative();

        step("Asserting '" + type + "' section on destination page");
        boolean isDestinationPagValid = ttdDestinationPage.isDestinationPageValid("",selectedDestination);
        Assertion.assertThat("Assert if the '" + type + "' is already valid", isDestinationPagValid, is(true));
    }

    @Author(names = {"Akbar","Kartik"})
    @Module(name = "DestinationPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkSRPViaCountryDestinationPageGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkSRPViaCountryDestinationPageGuest");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDDestinationPage<WebDriver> ttdDestinationPage = TTDDestinationPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String keyword = map.get("search_input");
        String type = map.get("search_type");

        step("Go To Home");
        homePage.gotoHome();

        step("User use the app as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click search bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Searching for product to reach on PDP | search_input = '" + keyword + "'");
        ttdSearchPage.inputSearchKeyword(keyword);
        ttdSearchPage.selectRecommendedActivity(keyword);

        step("swipe to bottom to see Destination breadcrumb");
        webViewPage.switchToWebViewUrl("to-do");
        boolean isDestinationBreadcrumbVisible = ttdProductDetailPageWeb.swipeToDestinationSectionBottom();
        Assertion.assertThat("Assert if Destination breadcrumb is Visible", isDestinationBreadcrumbVisible, is(true));

        step("Select city name from Destination breadcrumbs");
        String selectedDestination = ttdProductDetailPageWeb.selectDestinationByHierarchy(type);
        webViewPage.switchToNative();

        step("Asserting '" + type + "' section on destination page");
        boolean isDestinationPagValid = ttdDestinationPage.isDestinationPageValid("",selectedDestination);
        Assertion.assertThat("Assert if the '" + type + "' is already valid", isDestinationPagValid, is(true));
    }

    @Author(names = {"Akbar","Kartik"})
    @Module(name = "DestinationPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkSRPViaCountryDestinationPageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkSRPViaCountryDestinationPageLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDDestinationPage<WebDriver> ttdDestinationPage = TTDDestinationPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String keyword = map.get("search_input");
        String type = map.get("search_type");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click search bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Searching for product to reach on PDP | search_input = '" + keyword + "'");
        ttdSearchPage.inputSearchKeyword(keyword);
        ttdSearchPage.selectRecommendedActivity(keyword);

        step("swipe to bottom to see Destination breadcrumb");
        webViewPage.switchToWebViewUrl("to-do");
        boolean isDestinationBreadcrumbVisible = ttdProductDetailPageWeb.swipeToDestinationSectionBottom();
        Assertion.assertThat("Assert if Destination breadcrumb is Visible", isDestinationBreadcrumbVisible, is(true));

        step("Select city name from Destination breadcrumbs");
        String selectedDestination = ttdProductDetailPageWeb.selectDestinationByHierarchy(type);
        webViewPage.switchToNative();

        step("Asserting '" + type + "' section on destination page");
        boolean isDestinationPagValid = ttdDestinationPage.isDestinationPageValid("",selectedDestination);
        Assertion.assertThat("Assert if the '" + type + "' is already valid", isDestinationPagValid, is(true));
    }

    @Author(names = {"Yazid", "Aditi"})
    @Module(name = "Banner")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyLpgBannerLogin() {
        Map<String, String> map = getParam("verifyLpgBannerLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String categoryProduct = map.get("categoryProduct");
        String lpgProductName = map.get("productName");
        int checkTotalCard = Integer.parseInt(map.get("checkTotalCard"));

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Go to the SRP with clicking the category icon: " + categoryProduct);
        ttdLandingPage.openProductByCategory(categoryProduct);
        Assertion.assertThat("Show the LPG banner below the first product card", ttdSearchResultPage.isLpgHeaderBannerDisplayed(), is(true));

        step("Open the LPG banner on header");
        ttdSearchResultPage.clickLpgHeaderBanner();
        Assertion.assertThat("Redirect user to LPG page", ttdPage.isLpgPageDisplayed(), is(true));

        step("Back to the SRP");
        homePage.clickBackBtn();
        Assertion.assertThat("Show the LPG label for every product card", ttdSearchResultPage.checkLpgLabelCard(checkTotalCard, language), is(true));

        step("Select any To Do product on the SRP with LPG label");
        ttdSearchResultPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(lpgProductName);
        ttdSearchPage.selectRecommendedActivity(lpgProductName);
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Show the sticky LPG label on the PDP footer", ttdProductDetailPageWeb.isLpgBannerFooterDisplayed(), is(true));

        step("Select ticket package");
        ttdProductDetailPageWeb.goToPackagesList();

        step("Set total ticket");
        ttdProductDetailPageWeb.chooseTicket(1, false);
        ttdSelectedPackageDetailPageWeb.chooseTimeSlot();
        Assertion.assertThat("Show the LPG label on the package info under refundable section", ttdProductDetailPageWeb.isLpgBottomSheetDisplayed(), is(true));

        step("Go to the booking form by submitting the selected package");
        ttdSelectedPackageDetailPageWeb.setTicketQty(1);
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Show the non-sticky LPG label above the \"Continue to Payment\" button", ttdBookingFormPageWeb.isLpgBannerFooterDisplayed(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkTabbingPDPLogin() {
        Map<String, String> map = getParam("checkTabbingPDPLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        TtdReviewPageWeb<?> ttdReviewPageWeb = TtdReviewPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("Select the recommended product");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Go to package list");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();

        step("Open the review page");
        ttdProductDetailPageWeb.clickTabSection("Review", "Reviews", "right");
        ttdProductDetailPageWeb.openAllReview();
        webViewPage.switchToNative();
        webViewPage.switchToWebViewUrl("review");
        boolean reviewPageDisplayed = ttdReviewPageWeb.reviewPageDisplayed();
        String actualPageTitle = ttdReviewPageWeb.getPageTitle();
        ttdReviewPageWeb.clickBackButton();
        webViewPage.switchToNative();

        step("Validate the tab button and section");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Clicked Tab on the PDP showing the expected PDP section", ttdProductDetailPageWeb.getTheTabSection(), is(true));
        Assertion.assertThat("Review page displayed as expected", reviewPageDisplayed, is(true));
        Assertion.assertThat("Product name on the review page showing as expected", actualPageTitle, is(containsString("Review")));
    }

    @Author(names = {"Yazid"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkTabbingPDPGuest() {
        Map<String, String> map = getParam("checkTabbingPDPGuest");
        BaseTest.paramsThreadLocal.set(map);
        String product = map.get("product");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        TtdReviewPageWeb<?> ttdReviewPageWeb = TtdReviewPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("Select the recommended product");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Go to package list");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();

        step("Open the review page");
        ttdProductDetailPageWeb.clickTabSection("Review", "Reviews", "right");
        ttdProductDetailPageWeb.openAllReview();
        webViewPage.switchToNative();
        webViewPage.switchToWebViewUrl("review");
        boolean reviewPageDisplayed = ttdReviewPageWeb.reviewPageDisplayed();
        String actualPageTitle = ttdReviewPageWeb.getPageTitle();
        ttdReviewPageWeb.clickBackButton();
        webViewPage.switchToNative();

        step("Validate the tab button and section");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Clicked Tab on the PDP showing the expected PDP section", ttdProductDetailPageWeb.getTheTabSection(), is(true));
        Assertion.assertThat("Review page displayed as expected", reviewPageDisplayed, is(true));
        Assertion.assertThat("Product name on the review page showing as expected", actualPageTitle, is(containsString("Review")));
    }

    @Author(names = {"Yazid"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPackageSectionDetailsLogin() {
        Map<String, String> map = getParam("checkPackageSectionDetailsLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        int indexPackage = Integer.parseInt(map.get("indexPackage"));
        String slugProductName = map.get("slugProductName");
        int date = Integer.parseInt(map.get("date"));

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdPackageDetailPageWeb<?> ttdPackageDetailPageWeb = TtdPackageDetailPageWeb.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        InfoPageWeb<?> infoPageWeb = InfoPageWeb.getInstance(session);
        DetailPage<WebDriver> hotelDetailPage = DetailPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("open PDP via deeplink");
        ttdSearchResultPage.openPdpDeeplink(slugProductName);
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();

        step("Given packages on PDP");
        ttdProductDetailPageWeb.goToPackagesList();
        String namePackagePdp = ttdProductDetailPageWeb.getPackageName(indexPackage).replaceAll("[^a-zA-Z]", "");
        String finalPricePackagePdp = ttdProductDetailPageWeb.getFinalPrice(indexPackage);

        step("user click on Details button");
        ttdProductDetailPageWeb.clickDetailButton(indexPackage);

        step("show bottomsheet for package details");
        ttdPackageDetailPageWeb.packageDetailShown();
        String namePackageDetail = ttdPackageDetailPageWeb.getPackageName().replaceAll("[^a-zA-Z]", "");
        String finalPricePackageDetail = ttdPackageDetailPageWeb.getFinalPrice();
        Assertion.assertThat("Package name on the PDP should contain the same on the package detail", namePackagePdp, containsString(namePackageDetail));
        Assertion.assertThat("Final price on the package detail should be equal with the PDP", finalPricePackageDetail, is(finalPricePackagePdp));
        ttdProductDetailPageWeb.closeSection();

        step("user clicks on \"see calendar\"");
        ttdProductDetailPageWeb.openCalendar();

        step("select the date on date picker");
        ttdProductDetailPageWeb.selectDateCalendarPopup(date);
        String selectedDate = ttdProductDetailPageWeb.getDatePackage();
        String expectedDate = String.valueOf(date);
        Assertion.assertThat("should be redirected back to PDP on package sections", selectedDate, containsString(expectedDate));

        step("scroll down until at the bottom section");
        ttdProductDetailPageWeb.goToBottomSection();

        step("click on card \"You Might Also Like\"");
        String actualProductName = ttdProductDetailPageWeb.selectBottomProduct();
        String expectedProductName = ttdProductDetailPageWeb.getProductName(1);
        Assertion.assertThat("should be redirected to certain PDP", actualProductName, is(expectedProductName));
        ttdProductDetailPageWeb.clickBackButton();
        webViewPage.switchToNative();

        step("Click \"Open Hours\" section");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();
        ttdProductDetailPageWeb.openHoursSection();
        Assertion.assertThat("Open hours section: bottomsheet about open hours should be displayed", ttdProductDetailPageWeb.isBottomSheetOpen("Open", "Buka"), is(true));
        ttdProductDetailPageWeb.closeSection();

        step("Click \"Redemption Info\"");
        ttdProductDetailPageWeb.openMoreInfoSection("Redemption Info", "Penukaran Tiket");
        Assertion.assertThat("Redemption info bottomsheet \"redemption info\" will be displayed", ttdProductDetailPageWeb.isBottomSheetOpen("Redemption Info", "Penukaran Tiket"), is(true));
        ttdProductDetailPageWeb.closeSection();

        step("Click \"Maps\" button");
        ttdProductDetailPageWeb.openMapsDirection();
        webViewPage.switchToNative();
        Assertion.assertThat("Location section with View map: should be redirected to maps", hotelDetailPage.verifyRedirectToMaps(), is(true));

        step("Click \"How to Get There\" button");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.clickHowToGetThere();
        Assertion.assertThat("How to get there bottomsheet should be displayed", ttdProductDetailPageWeb.isBottomSheetOpen("How to Get There", "Panduan ke Lokasi"), is(true));
        ttdProductDetailPageWeb.closeSection();

        step("Click \"Contact Details\" button");
        ttdProductDetailPageWeb.clickContactDetails();
        Assertion.assertThat("Contact details bottomsheet should be displayed", ttdProductDetailPageWeb.isBottomSheetOpen("Contact Details", "Detail Kontak"), is(true));
        ttdProductDetailPageWeb.closeSection();

        step("Choose product benefit (eg. tiket Flexi or tiket Clean)");
        String benefitTitle = ttdProductDetailPageWeb.chooseBenefit().toLowerCase();
        webViewPage.switchToNative();
        webViewPage.switchToWebViewUrl("info");
        Assertion.assertThat("Benefit title should be displayed on benefit section", infoPageWeb.getInfoPageTitle().toLowerCase(), containsString(benefitTitle));
    }

    @Author(names = {"Yazid", "Mridul"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPackageSectionDetailsGuest() {
        Map<String, String> map = getParam("checkPackageSectionDetailsGuest");
        BaseTest.paramsThreadLocal.set(map);
        String product = map.get("product");
        int indexPackage = Integer.parseInt(map.get("indexPackage"));

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdPackageDetailPageWeb<?> ttdPackageDetailPageWeb = TtdPackageDetailPageWeb.getInstance(session);
        TTDProductDetailPage<WebDriver> ttdProductDetailPage = TTDProductDetailPage.getInstance(session);
        DetailPage<WebDriver> hotelDetailPage = DetailPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("Select the recommended product");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Given packages on PDP");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        String namePackagePdp = ttdProductDetailPageWeb.getPackageName(indexPackage).replaceAll("[^a-zA-Z]", "");

        step("When non-login user clicks on Details button");
        ttdProductDetailPageWeb.clickDetailButton(indexPackage);
        Assertion.assertThat("Succeed for redirecting to package detail page", ttdPackageDetailPageWeb.packageDetailShown(), is(true));
        Assertion.assertThat("Package name on the PDP should contain the same on the package detail", namePackagePdp, containsString(ttdPackageDetailPageWeb.getPackageName().replaceAll("[^a-zA-Z]", "")));
        ttdProductDetailPageWeb.closeSection();

        step("Scroll down until at the bottom section and click You Might Also Like");
        ttdProductDetailPageWeb.goToBottomSection();
        String actualProductName = ttdProductDetailPageWeb.selectBottomProduct();
        String expectedProductName = ttdProductDetailPageWeb.getProductName();
        Assertion.assertThat("Should be redirected to certain PDP", expectedProductName, is(actualProductName));
        ttdProductDetailPageWeb.clickBackButton();
        webViewPage.switchToNative();

        step("Click Open Hours section");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();
        ttdProductDetailPageWeb.openHoursSection();
        Assertion.assertThat("Open hours bottomsheet should be displayed", ttdProductDetailPageWeb.isBottomSheetOpen("Open Hours", "Jam Buka"), is(true));
        ttdProductDetailPageWeb.closeSection();

        step("Click Redemption Info");
        ttdProductDetailPageWeb.openMoreInfoSection("Redemption Info", "Penukaran Tiket");
        Assertion.assertThat("Redemption info bottomsheet should be displayed", ttdProductDetailPageWeb.isBottomSheetOpen("Redemption Info", "Penukaran Tiket"), is(true));
        ttdProductDetailPageWeb.closeSection();

        step("Click View map in Location section");
        webViewPage.switchToNative();
        ttdProductDetailPage.clickViewMap();
        Assertion.assertThat("Location section with View map: should be redirected to maps", hotelDetailPage.verifyRedirectToMaps(), is(true));

        step("Click How to Get There");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.clickHowToGetThere();
        Assertion.assertThat("How to Get There bottomsheet should be displayed", ttdProductDetailPageWeb.isBottomSheetOpen("How to Get There", "Panduan ke Lokasi"), is(true));
        ttdProductDetailPageWeb.closeSection();

        step("Click Contact Details");
        ttdProductDetailPageWeb.clickContactDetails();
        Assertion.assertThat("Contact bottomsheet with phone number or email should be displayed", ttdProductDetailPageWeb.isContactDetailsContentDisplayed(), is(true));
        ttdProductDetailPageWeb.closeSection();
    }

    @Author(names = {"Yazid"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkOtherPdpPartGuest() {
        Map<String, String> map = getParam("checkOtherPdpPartGuest");
        BaseTest.paramsThreadLocal.set(map);
        String product = map.get("product");
        int date = Integer.parseInt(map.get("date"));

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        DetailPage<WebDriver> hotelDetailPage = DetailPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("Select the recommended product");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Go to the package list");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();

        step("Open the calendar");
        ttdProductDetailPageWeb.openCalendar();

        step("Select date on the calendar popup");
        ttdProductDetailPageWeb.selectDateCalendarPopup(date);

        step("Get the selected date on the package section");
        String selectedDate = ttdProductDetailPageWeb.getDatePackage();
        String expectedDate = String.valueOf(date);

        Assertion.assertThat("Selected date should be show as per expected", selectedDate, containsString(expectedDate));

        step("Go to the location section");
        ttdProductDetailPageWeb.clickTabSection("Lokasi", "Location", "left");

        step("Open the maps direction");
        ttdProductDetailPageWeb.openMapsDirection();

        webViewPage.switchToNative();
        Assertion.assertThat("App redirected to the Maps app", hotelDetailPage.verifyRedirectToMaps(), is(true));

        step("Go to the bottom section");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToBottomSection();

        step("Select randomly the product");
        String actualProductName = ttdProductDetailPageWeb.selectBottomProduct();
        String expectedProductName = ttdProductDetailPageWeb.getProductName(1);

        Assertion.assertThat("Success to redirect to the correct PDP", actualProductName, is(expectedProductName));
    }

    @Author(names = {"Yazid"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkOtherPdpPartLogin() {
        Map<String, String> map = getParam("checkOtherPdpPartLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");
        int date = Integer.parseInt(map.get("date"));

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        DetailPage<WebDriver> hotelDetailPage = DetailPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("Select the recommended product");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Go to the package list");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();

        step("Open the calendar");
        ttdProductDetailPageWeb.openCalendar();

        step("Select date on the calendar popup");
        ttdProductDetailPageWeb.selectDateCalendarPopup(date);

        step("Get the selected date on the package section");
        String selectedDate = ttdProductDetailPageWeb.getDatePackage();
        String expectedDate = String.valueOf(date);

        Assertion.assertThat("Selected date should be show as per expected", selectedDate, containsString(expectedDate));

        step("Go to the location section");
        ttdProductDetailPageWeb.clickTabSection("Lokasi", "Location", "left");

        step("Open the maps direction");
        ttdProductDetailPageWeb.openMapsDirection();

        webViewPage.switchToNative();
        Assertion.assertThat("App redirected to the Maps app", hotelDetailPage.verifyRedirectToMaps(), is(true));

        step("Go to the bottom section");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToBottomSection();

        step("Select randomly the product");
        String actualProductName = ttdProductDetailPageWeb.selectBottomProduct();
        String expectedProductName = ttdProductDetailPageWeb.getProductName(1);

        Assertion.assertThat("Success to redirect to the correct PDP", actualProductName, is(expectedProductName));
    }

    @Author(names = {"Yazid"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkRecentlyViewSectionGuest() {
        Map<String, String> map = getParam("checkRecentlyViewSectionGuest");
        BaseTest.paramsThreadLocal.set(map);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");
        Assertion.assertThat("User should not be shown recently viewed", ttdLandingPage.isRecentlyViewedDisplayed(), is(false));
    }

    @Author(names = {"Yazid"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkRecentlyViewSectionLogin() {
        Map<String, String> map = getParam("checkRecentlyViewSectionLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");
        String productSearch = map.get("productSearch");
        String slugProductNames = map.get("slugProductNames");
        String[] slugProductName = slugProductNames.split("_");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDProductDetailPage<WebDriver> ttdProductDetailPage = TTDProductDetailPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)" + currency);
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        String language = homePage.getLanguageApp();

        step("open some PDP via deeplink");
        ttdSearchResultPage.openSomePdpDeeplink(slugProductName);

        step("open the PDP via search from the TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(productSearch);
        ttdSearchPage.selectRecommendedActivity(productSearch);
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();
        webViewPage.switchToNative();

        step("Open the TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);
        Assertion.assertThat("Recently viewed section should appear", ttdLandingPage.isRecentlyViewedDisplayed(), is(true));

        step("User click see all button");
        ttdLandingPage.clickSeeAllRecentlyViewed();
        Assertion.assertThat("Recently viewed should be less than or equal 15", ttdLandingPage.getTotalRecentlyViewedItems(), is(lessThanOrEqualTo(15)));

        step("User clicks one of the products from the recently viewed popup list");
        String expectedProductName = ttdLandingPage.selectRecentlyViewedItem("random");
        Assertion.assertThat("User will be redirected to the PDP of the selected product", ttdProductDetailPage.checkProductNameNative(expectedProductName), is(true));

        step("User removes a recently viewed product from the recently viewed popup list");
        homePage.openVerticalPageDeeplink("To Do", language, currency);
        ttdLandingPage.clickSeeAllRecentlyViewed();
        String removedProduct = ttdLandingPage.removeFirstRecentlyViewedItem();
        Assertion.assertThat("The removed product should not be in the recently popup list", ttdLandingPage.getFirstRecentlyViewedItem().toLowerCase(), not(removedProduct.toLowerCase()));
        ttdLandingPage.clickBottomsheetCrossButton();

        step("User remove the recently viewed product from the recently viewed section");
        String firstProduct = ttdLandingPage.getFirstProductNameRecentlyViewed().toLowerCase();
        ttdLandingPage.removeFirstRecentlyViewedProduct();
        Assertion.assertThat("The removed product should not be in the recently viewed section", ttdLandingPage.getFirstProductNameRecentlyViewed().toLowerCase(), not(firstProduct.toLowerCase()));
    }

    @Author(names = {"Yazid"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void userClickCategoryIcon() {
        Map<String, String> map = getParam("userClickCategoryIcon");
        BaseTest.paramsThreadLocal.set(map);
        String productCategory = map.get("productCategory");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click "+productCategory+" for the category icon");
        String expectedCategory = ttdLandingPage.openProductByCategory(productCategory);

        step("SRP or Category Page is open");
        String actualCategory = ttdSearchResultPage.getSelectedCategory();

        Assertion.assertThat("The selected category icon redirects to the SRP or the Category Page", actualCategory, containsString(expectedCategory));
        Assertion.assertThat("Check if SRP is shown", ttdSearchResultPage.isSearchResultPageShown(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void userClickCategoryIconLogin() {
        Map<String, String> map = getParam("userClickCategoryIconLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String productCategory = map.get("productCategory");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String appLanguage = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", appLanguage, "IDR");

        step("Click "+productCategory+" for the category icon");
        String expectedCategory = ttdLandingPage.openProductByCategory(productCategory);
        expectedCategory= CoreFunctions.normalizeString(expectedCategory);

        step("SRP or Category Page is open");
        String actualCategory = ttdSearchResultPage.getSelectedCategory();
        actualCategory = CoreFunctions.normalizeString(actualCategory);

        Assertion.assertThat("The selected category icon redirects to the SRP or the Category Page", actualCategory, containsString(expectedCategory));
        Assertion.assertThat("Check if SRP is shown", ttdSearchResultPage.isSearchResultPageShown(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void enableLocationFromLandingPage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        Map<String, String> map = getParam("enableLocationFromLandingPage");
        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Get the Explore Text before location is active");
        String beforeLocationActive = ttdLandingPage.getExploreText();

        step("Allow location from the TTD Landing Page");
        ttdLandingPage.clickEnableLocation();

        step("Get the Explore Text after location is active");
        String afterLocationActive = ttdLandingPage.getExploreText();

        Assertion.assertThat("Explore text should be different", afterLocationActive, not(beforeLocationActive));
    }

    @Author(names = {"Prerna", "Akbar"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void enableLocationFromLandingPageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("enableLocationFromLandingPageLogin");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Get the Explore Text before location is active");
        String beforeLocationActive = ttdLandingPage.getExploreText();

        step("Allow location from the TTD Landing Page");
        ttdLandingPage.clickEnableLocation();

        step("Get the Explore Text after location is active");
        String afterLocationActive = ttdLandingPage.getExploreText();

        Assertion.assertThat("Explore text should be different", afterLocationActive, not(beforeLocationActive));
    }

    @Author(names = {"Yazid", "Akbar","Kartik"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void landingPageAllowLocationLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("landingPageAllowLocationLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Set to allow location access");
        homePage.setLocationService("ON");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");
        String location = ttdLandingPage.getLocationText();
        Assertion.assertThat("Location should contains ALL LOCATION ", location.toLowerCase(), either(containsString("all locations")).or(containsString("semua lokasi")));

        step("Click location text");
        ttdLandingPage.clickLocationText();
        Assertion.assertThat("Show Search destinations with City, Region, Country chips based on user current location", ttdLandingPage.isCurrentLocationOptionAppears(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularAttractionsLogin() {
        Map<String, String> map = getParam("checkPopularAttractionsLogin");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        String language = homePage.getLanguageApp();
        String section = language.equals("id") ? "atraksi" : "attractions";

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the location");
        ttdLandingPage.clickSearchBar();

        List<String> productOrderFromAPI = new ArrayList<>();
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Check product list from API: " + section);
            productOrderFromAPI = ttdPage.getProductTitleOrder("popular attractions", language, username, password);
        }

        step("Select section: " + section);
        ttdSearchPage.selectSection(section);

        Assertion.assertThat("Attractions should have ranks 1-10",
                ttdSearchPage.checkToDoRank(10), is(true));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Check product list order: " + section);
            List<String> productOrderFromUI = ttdSearchPage.getProductTitleOrder();
            Assertion.assertThat("Product title order should match API",
                    productOrderFromUI, is(productOrderFromAPI));
        }

        step("Click see all button");
        ttdSearchPage.clickSeeAllButton();

        Assertion.assertThat(
                "See all button is redirecting to SRP with 'popular attractions' category",
                ttdSearchResultPage.verifyRedirectionToSearchResultPage("popular attractions"),
                is(true)
        );

        ttdSearchResultPage.clickBack();

        step("Select any product");
        ttdLandingPage.clickSearchBar();
        String expectedProductName = ttdSearchPage.clickItemOnList("random");
        webViewPage.switchToWebViewUrl("to-do");

        Assertion.assertThat("User will be redirected to the correct PDP",
                ttdProductDetailPageWeb.getProductName(), is(expectedProductName));
    }

    @Author(names = {"Yazid","Ashish"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularAttractionsGuest() {
        Map<String, String> map = getParam("checkPopularAttractionsGuest");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();
        homePage.goToHomeDeeplink();
        String language = homePage.getLanguageApp();
        String section = language.equals("id") ? "atraksi" : "attractions";

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the location");
        ttdLandingPage.clickSearchBar();
        List<String> productOrderFromAPI = new ArrayList<>();
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Get product list from API: " + section);
            productOrderFromAPI = ttdPage.getProductTitleOrder("popular attractions", language, "", "");
        }

        step("Select section: " + section);
        ttdSearchPage.selectSection(section);
        Assertion.assertThat("Attractions should be have 1-10 of the rank", ttdSearchPage.checkToDoRank(10), is(true));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Check product list order: " + section);
            List<String> productOrderFromUI = ttdSearchPage.getProductTitleOrder();
            Assertion.assertThat("Product title order should be matched with API", productOrderFromUI, is(productOrderFromAPI));
        }

        step("Click see all button");
        ttdSearchPage.clickSeeAllButton();
        Assertion.assertThat("See all button is redirecting to SRP with 'attraction' category", ttdSearchResultPage.verifyRedirectionToSearchResultPage("popular attractions"), is(true));
        ttdSearchResultPage.clickBack();

        step("Select any product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.selectSection(section);
        String expectedProductName = ttdSearchPage.clickItemOnList("random");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("User will be redirected to the correct PDP", ttdProductDetailPageWeb.getProductName(), is(expectedProductName));
    }

    @Author(names = {"Yazid", "Aditi"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularEventsGuest() {
        Map<String, String> map = getParam("checkPopularEventsGuest");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();
        String section = language.equals("id") ? "event" : "events";

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the location");
        ttdLandingPage.clickSearchBar();
        List<String> productOrderFromAPI = new ArrayList<>();
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Get product list from API: " + section);
            productOrderFromAPI = ttdPage.getProductTitleOrder("popular events", language, "", "");
        }

        step("Select section: " + section);
        ttdSearchPage.selectSection(section);
        Assertion.assertThat("Events should be have 1-10 of the rank", ttdSearchPage.checkToDoRank(10), is(true));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            List<String> productOrderFromUI = ttdSearchPage.getProductTitleOrder();
            Assertion.assertThat("Product title order should be matched with API", productOrderFromUI, is(productOrderFromAPI));
        }

        step("Check 'see all' button");
        ttdSearchPage.clickSeeAllButton();
        Assertion.assertThat("See all button is redirecting to SRP with 'event' category", ttdSearchResultPage.verifyRedirectionToSearchResultPage("popular events"), is(true));
        ttdSearchResultPage.clickBack();

        step("Select any product");
        String expectedProductName = ttdSearchPage.clickItemOnList("random");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("User will be redirected to the correct PDP", ttdProductDetailPageWeb.getProductName(), is(expectedProductName));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularEventsLogin() {
        Map<String, String> map = getParam("checkPopularEventsLogin");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();
        String section = language.equals("id") ? "event" : "events";

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", username);

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the location");
        ttdLandingPage.clickSearchBar();
        List<String> productOrderFromAPI = new ArrayList<>();
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Get product list from API: " + section);
            productOrderFromAPI = ttdPage.getProductTitleOrder("popular events", language, username, password);
        }

        step("Select section: " + section);
        ttdSearchPage.selectSection(section);
        Assertion.assertThat("Events should be have 1-10 of the rank", ttdSearchPage.checkToDoRank(10), is(true));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Check product list order: " + section);
            List<String> productOrderFromUI = ttdSearchPage.getProductTitleOrder();
            Assertion.assertThat("Product title order should be matched with API", productOrderFromUI, is(productOrderFromAPI));
        }

        step("Check 'see all' button");
        ttdSearchPage.clickSeeAllButton();
        Assertion.assertThat("See all button is redirecting to SRP with 'event' category", ttdSearchResultPage.verifyRedirectionToSearchResultPage("popular events"), is(true));
        ttdSearchResultPage.clickBack();

        step("Select any product");
        String expectedProductName = ttdSearchPage.clickItemOnList("random");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("User will be redirected to the correct PDP", ttdProductDetailPageWeb.getProductName().trim(), is(expectedProductName.trim()));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkNewActivitiesGuest() {
        Map<String, String> map = getParam("checkNewActivitiesGuest");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        int expectedTotal = 10;

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        homePage.gotoHome();
        String language = homePage.getLanguageApp();
        String section = language.equals("id") ? "aktivitas baru" : "new activities";

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the location");
        ttdLandingPage.clickSearchBar();
        List<String> productOrderFromAPI = new ArrayList<>();
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Get product list from API: " + section);
            productOrderFromAPI = ttdPage.getProductTitleOrder("new activities", language, "", "");
        }

        step("Select section: " + section);
        ttdSearchPage.selectSection(section);
        Assertion.assertThat("Total new activities should be " + expectedTotal, ttdSearchPage.getTotalItems(), is(expectedTotal));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Check product list order: " + section);
            List<String> productOrderFromUI = ttdSearchPage.getProductTitleOrder();
            Assertion.assertThat("Product title order should be matched with API", productOrderFromUI, is(productOrderFromAPI));
        }

        step("Select any product");
        String expectedProductName = ttdSearchPage.clickItemOnList("random");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("User will be redirected to the correct PDP", ttdProductDetailPageWeb.getProductName(), is(expectedProductName));
        ttdProductDetailPageWeb.clickBack();
        webViewPage.switchToNative();

        step("Check 'see all' button");
        ttdSearchPage.clickSeeAllButton();
        Assertion.assertThat("See all button is redirecting to SRP with activated sort and 'all' category", ttdSearchResultPage.verifyRedirectionToSearchResultPage("new activities"), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkNewActivitiesLogin() {
        Map<String, String> map = getParam("checkNewActivitiesLogin");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        int expectedTotal = 10;

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();
        String section = language.equals("id") ? "aktivitas baru" : "new activities";

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the location");
        ttdLandingPage.clickSearchBar();
        List<String> productOrderFromAPI = new ArrayList<>();
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Get product list from API: " + section);
            productOrderFromAPI = ttdPage.getProductTitleOrder("new activities", language, username, password);
        }

        step("Select section: " + section);
        ttdSearchPage.selectSection(section);
        Assertion.assertThat("Total new activities should be " + expectedTotal, ttdSearchPage.getTotalItems(), is(expectedTotal));

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Check product list order: " + section);
            List<String> productOrderFromUI = ttdSearchPage.getProductTitleOrder();
            Assertion.assertThat("Product title order should be matched with API", productOrderFromUI, is(productOrderFromAPI));
        }

        step("Select any product");
        String expectedProductName = ttdSearchPage.clickItemOnList("random");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("User will be redirected to the correct PDP", ttdProductDetailPageWeb.getProductName(), is(expectedProductName));
        ttdProductDetailPageWeb.clickBack();
        webViewPage.switchToNative();

        step("Check 'see all' button");
        ttdSearchPage.clickSeeAllButton();
        Assertion.assertThat("See all button is redirecting to SRP with activated sort and 'all' category", ttdSearchResultPage.verifyRedirectionToSearchResultPage("new activities"), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void searchAreaLogin() {
        Map<String, String> map = getParam("searchAreaLogin");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String areaListString = map.get("area");
        String[] areaList = areaListString.split("-");
        String area = areaList[generateRandomInt(0, areaList.length - 1)];

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the area: " + area);
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(area);

        step("Select first recommended area");
        ttdSearchPage.selectRecommendedDestination(area);
        Assertion.assertThat("Show destination result by the destination name", ttdSearchResultPage.getLocation(), is(area));
    }

    @Author(names = {"Kartik"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void searchAreaGuest() {
        Map<String, String> map = getParam("searchAreaGuest");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        String areaListString = map.get("area");
        String[] areaList = areaListString.split("-");
        String area = areaList[generateRandomInt(0, areaList.length - 1)];

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the area: " + area);
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(area);

        step("Select first recommended area");
        ttdSearchPage.selectRecommendedDestination(area);
        Assertion.assertThat("Show destination result by the destination name", ttdSearchResultPage.getLocation(), is(area));
    }

    @Author(names = {"Akbar"})
    @Module(name = "DestinationPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkEntryPointAutoCompleteSuggestion() {
        Map<String, String> map = getParam("checkEntryPointAutoCompleteSuggestion");
        BaseTest.paramsThreadLocal.set(map);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        String keyword = map.get("search_input");
        String type = map.get("search_type");
        String pageType = "search";

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Do Search on TTD Search Page");
        ttdSearchPage.inputSearchKeyword(keyword);
        Assertion.assertThat("Check Autocomplete Suggestion", ttdSearchResultPage.isAutoCompleteSuggestionValid(keyword, type, pageType), is(true));

        step("Click on Targeted Location");
        ttdSearchPage.selectRecommendedDestination(keyword);
        Assertion.assertThat("Redirected to the SRP", ttdSearchResultPage.isSearchResultPageShown(), is(true));
        Assertion.assertThat("SRP location should be equal with the selected search suggestion", ttdSearchResultPage.getLocation(), is(keyword));
    }

    @Author(names = {"Akbar", "Kartik"})
    @Module(name = "DestinationPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkEntryPointAutoCompleteSuggestionLogin() {
        Map<String, String> map = getParam("checkEntryPointAutoCompleteSuggestionLogin");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String keyword = map.get("search_input");
        String type = map.get("search_type");

        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Do Search on TTD Search Page");
        ttdSearchPage.inputSearchKeyword(keyword);
        Assertion.assertThat("Check Autocomplete Suggestion", ttdSearchResultPage.isAutoCompleteSuggestionValid(keyword, type, "search"), is(true));

        step("Click on Targeted Location");
        ttdSearchPage.selectRecommendedDestination(keyword);
        Assertion.assertThat("Redirected to the SRP", ttdSearchResultPage.isSearchResultPageShown(), is(true));
        Assertion.assertThat("SRP location should be equal with the selected search suggestion", ttdSearchResultPage.getLocation(), is(keyword));
    }

    @Author(names = {"Akbar", "Naufal"})
    @Module(name = "DestinationPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkEntryPointAutoCompleteSuggestionViaExplorePageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkEntryPointAutoCompleteSuggestionViaExplorePageLogin");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDExplorePage<WebDriver> ttdExplorePage = TTDExplorePage.getInstance(session);
        TTDDestinationPage<WebDriver> ttdDestinationPage = TTDDestinationPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String keyword = map.get("search_input");
        String type = map.get("search_type");
        String pageType = "explore";

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Explore Arrow on TTD Landing Page");
        ttdLandingPage.clickEnableLocation();
        ttdLandingPage.clickExploreArrow();
        ttdExplorePage.isExplorePageValid();

        step("Do Search on TTD Search Page");
        ttdExplorePage.inputSearchKeyword(keyword);
        Assertion.assertThat("Check Autocomplete Suggestion", ttdSearchResultPage.isAutoCompleteSuggestionValid(keyword, type, pageType), is(true));

        step("Click on Targeted Location");
        ttdSearchResultPage.selectLocation(keyword, pageType);
        Assertion.assertThat("Assert if Destination Name is valid", ttdDestinationPage.getDestinationName().toLowerCase().trim(), is(keyword.toLowerCase()));
    }

    @Author(names = {"Akbar"})
    @Module(name = "DestinationPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test(dataProvider = "checkEntryListOfDestinationDataProvider")
    public void checkEntryPointListOfDestination(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDExplorePage<WebDriver> ttdExplorePage = TTDExplorePage.getInstance(session);
        TTDDestinationPage<WebDriver> ttdDestinationPage = TTDDestinationPage.getInstance(session);

        String keyword = map.get("search_input");
        String type = map.get("search_type");

        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Explore Arrow on TTD Landing Page");
        ttdLandingPage.clickEnableLocation();
        ttdLandingPage.clickExploreArrow();
        ttdExplorePage.isExplorePageValid();

        step("Searching for destination | search_input = '" + keyword + "'");
        String selectedDestination = ttdExplorePage.selectDestination(keyword, type);

        Assertion.assertThat("Assert if Destination Name is valid", selectedDestination.toLowerCase(), is(ttdDestinationPage.getDestinationNameOrSRPLocation().toLowerCase()));
    }

    @Author(names = {"Akbar"})
    @Module(name = "TTD")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void TTD_checkTopKeywordAndSuggestionLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        Map<String, String> map = getParam("TTD_checkTopKeywordAndSuggestionLogin");

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();
        boolean isTopKeywordDisplayed = ttdSearchPage.isTopKeywordDisplayed();
        Assertion.assertThat("Check if Top Keyword Suggestion is Displayed", isTopKeywordDisplayed, is(true));

        step("Get Keyword list before clicking refresh button");
        List<String> listBeforeRefresh = ttdSearchPage.getTopKeywordList();

        step("click refresh button");
        ttdSearchPage.clickRefreshTopKeyword();

        step("Get Keyword list after clicking refresh button");
        List<String> listAfterRefresh = ttdSearchPage.getTopKeywordList();

        Assertion.assertThat("Top Keyword changes after clicking Refresh button", !listAfterRefresh.equals(listBeforeRefresh), is(true));

        step("Click random keyword");
        String selectedKeyword = ttdSearchPage.clickRandomKeyword();
        Assertion.assertThat("selected keyword is in search bar", ttdSearchPage.getSearchFieldText(), is(selectedKeyword));

        step("Select recommended keyword product");
        ttdSearchPage.selectRecommendedActivity(selectedKeyword);
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("User will be redirected to the correct PDP", ttdProductDetailPageWeb.getProductName().toLowerCase(), containsString(selectedKeyword.toLowerCase()));
    }

    @Author(names = {"Naufal"})
    @Module(name = "TTD")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void TTD_checkTopKeywordAndSuggestionGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("TTD_checkTopKeywordAndSuggestionGuest");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();
        homePage.goToHomeDeeplink();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();
        boolean isTopKeywordDisplayed = ttdSearchPage.isTopKeywordDisplayed();
        Assertion.assertThat("Check if Top Keyword Suggestion is Displayed", isTopKeywordDisplayed, is(true));

        step("Get Keyword list before clicking refresh button");
        List<String> listBeforeRefresh = ttdSearchPage.getTopKeywordList();

        step("click refresh button");
        ttdSearchPage.clickRefreshTopKeyword();

        step("Get Keyword list after clicking refresh button");
        List<String> listAfterRefresh = ttdSearchPage.getTopKeywordList();

        Assertion.assertThat("Top Keyword changes after clicking Refresh button", !listAfterRefresh.equals(listBeforeRefresh), is(true));

        step("Click random keyword");
        String selectedKeyword = ttdSearchPage.clickRandomKeyword();
        Assertion.assertThat("selected keyword is in search bar", ttdSearchPage.getSearchFieldText(), is(selectedKeyword));

        step("Select recommended keyword product");
        ttdSearchPage.selectRecommendedActivity(selectedKeyword);
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("User will be redirected to the correct PDP", ttdProductDetailPageWeb.getProductName().toLowerCase(), containsString(selectedKeyword.toLowerCase()));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularDestinationLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPopularDestinationLogin");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDExplorePage<WebDriver> ttdExplorePage = TTDExplorePage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        homePage.goToHomeDeeplink();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        List<String> popularDestinationsFromAPI = new ArrayList<>();
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Check product list from API: popular destinations");
            TTDPopularDestinationDataPOJO popularDestinationsFromAPIPOJO = ttdPage.getPopularDestinationData(language);
            popularDestinationsFromAPI = ttdPage.getDestinationTitleList(popularDestinationsFromAPIPOJO);
        }

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Click Popular Destination Tab on TTD Search Page");
        ttdSearchPage.selectSection("destinations");

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Check product list order: popular destinations");
            List<String> popularDestinationsFromUI = ttdSearchPage.getProductTitleOrder();
            Assertion.assertThat("Destination title order should be matched with API", popularDestinationsFromUI, is(popularDestinationsFromAPI));
        }

        step("Choose one of the popular destination");
        String clickedPopularDestination = ttdSearchPage.clickItemOnList("random");

        step("Verify Step 1");
        Assertion.assertThat("Assert if Destination Name is valid", clickedPopularDestination, is(containsString(ttdSearchResultPage.getLocation())));

        step("Go Back");
        ttdSearchResultPage.clickBack();

        step("Click see all button on the Popular Destination tab");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.selectSection("destinations");
        ttdSearchPage.clickSeeAllButton();

        step("Verify Step 2");
        Assertion.assertThat("Assert if redirected to destination list page", ttdExplorePage.isExplorePageValid(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "SRP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkExploreByNearbyLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        Map<String, String> map = getParam("checkExploreByNearbyLogin");

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Click Check Nearby Activity");
        ttdSearchPage.clickNearbyActivity();
        ttdSearchResultPage.setAppPermissionAllow();

        step("Verify Search Result Page");
        Assertion.assertThat("Check if SRP is shown", ttdSearchResultPage.isSearchResultPageShown(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularActivities() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        Map<String, String> map = getParam("checkPopularActivities");
        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Click one of Popular Activities on TTD Search Page");
        Map<String, String> clickedPopularActivity = ttdSearchPage.pickAndClickPopularActivity();

        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Assert if Activity Name is Valid", clickedPopularActivity.get("name").toLowerCase(), is(ttdProductDetailPageWeb.getProductName().toLowerCase()));
        Assertion.assertThat("Assert if Activity Type is Valid", clickedPopularActivity.get("type"), is(ttdProductDetailPageWeb.getProductType()));
    }

    @Author(names = {"Akbar"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularPartners() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDProductDetailPage<WebDriver> ttdProductDetailPage = TTDProductDetailPage.getInstance(session);

        Map<String, String> map = getParam("checkPopularPartners");
        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Click one of Popular Partners on TTD Search Page");
        String clickedPopularPartner = ttdSearchPage.pickAndClickPopularPartner();
        Assertion.assertThat("Assert if Partner Name is Valid", clickedPopularPartner.toLowerCase(), is(ttdProductDetailPage.getPartnerName().toLowerCase()));
    }

    @Author(names = {"Prerna"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularPartnersLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPopularPartnersLogin");
        BaseTest.paramsThreadLocal.set(map);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDProductDetailPage<WebDriver> ttdProductDetailPage = TTDProductDetailPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Click one of Popular Partners on TTD Search Page");
        String clickedPopularPartner = ttdSearchPage.pickAndClickPopularPartner();
        Assertion.assertThat("Assert if Partner Name is Valid", clickedPopularPartner.toLowerCase(), is(ttdProductDetailPage.getPartnerName().toLowerCase()));
    }

    @Author(names = {"Akbar"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkRecentlySearchedTTDGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkRecentlySearchedTTDGuest");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String keyword = map.get("search_input");

        step("Go To Home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Do Search on TTD Search Page");
        ttdSearchPage.inputSearchKeyword(keyword);

        step("Choose One of The Search Result");
        ttdSearchPage.selectRecommendedActivity(keyword);

        step("Back to Search Page");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.clickBack();
        webViewPage.switchToNative();
        ttdLandingPage.clickSearchBar();
        Assertion.assertThat("Assert if Recently Searched is displayed and valid", ttdSearchPage.isRecentlySearchedValid(keyword), is(true));

        step("Click on the recently searched item");
        ttdSearchPage.clickRecentlySearchedItem(keyword);
        ttdSearchPage.selectRecommendedActivity(keyword);

        step("Verify if redirected to PDP");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Assert if redirected to PDP", keyword.toLowerCase(), containsString(ttdProductDetailPageWeb.getProductName().toLowerCase()));

        step("Go back to search page and clear recent search");
        ttdProductDetailPageWeb.clickBack();
        webViewPage.switchToNative();
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.clickClearAll();
        Assertion.assertThat("Assert if recent search is cleared", ttdSearchPage.isSearchCleared(), is(true));
    }

    @Author(names = {"Prerna", "Akbar"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkRecentlySearchedTTDLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkRecentlySearchedTTDLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDProductDetailPage<WebDriver> ttdProductDetailPage = TTDProductDetailPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String keyword = map.get("search_input");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String slugProduct = map.get("slugProductNames");
        String[] slugProductName = slugProduct.split("_");

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        String language = homePage.getLanguageApp();

        step("open product to have recently viewed");
        ttdSearchResultPage.openSomePdpDeeplink(slugProductName);

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Recently viewed Products are visible ");
        Assertion.assertThat("Assert if Recently viewed Products are visible", ttdLandingPage.isRecentlyViewedDisplayed(), is(true));

        step("Click see all");
        ttdLandingPage.clickSeeAllRecentlyViewed();
        Assertion.assertThat("Recently viewed should be less than or equal 15", ttdLandingPage.getTotalRecentlyViewedItems(), is(lessThanOrEqualTo(15)));

        step("User clicks one of the products from the recently viewed popup list");
        String expectedProductName = ttdLandingPage.selectRecentlyViewedItem("random");
        Assertion.assertThat("User will be redirected to the PDP of the selected product", ttdProductDetailPage.checkProductNameNative(expectedProductName), is(true));

        step("Click Search Bar on TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");
        ttdLandingPage.clickSearchBar();

        step("Do Search on TTD Search Page");
        ttdSearchPage.inputSearchKeyword(keyword);

        step("Choose One of The Search Result");
        ttdSearchPage.selectRecommendedActivity(keyword);

        step("Back to Search Page");
        homePage.goToHomeDeeplink();
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");
        ttdLandingPage.clickSearchBar();
        Assertion.assertThat("Assert if Recently Searched is displayed and valid", ttdSearchPage.isRecentlySearchedValid(keyword), is(true));

        step("Click on the recently searched item");
        ttdSearchPage.clickRecentlySearchedItem(keyword);
        ttdSearchPage.selectRecommendedActivity(keyword);

        step("Verify if redirected to PDP");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Assert if redirected to PDP", keyword.toLowerCase(), containsString(ttdProductDetailPageWeb.getProductName().toLowerCase()));

        step("Go back to search page and clear recent search");
        ttdProductDetailPageWeb.clickBack();
        webViewPage.switchToNative();
        ttdSearchPage.clickBack();
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.clickClearAll();
        Assertion.assertThat("Assert if recent search is cleared", ttdSearchPage.isSearchCleared(), is(true));
    }

    @Author(names = {"Akbar", "Aditi"})
    @Vertical(name = "TTD")
    @Tribe(name = "Experience")
    @Module(name = "Filter")
    @Test
    public void checkCategoryAndSubCategoryLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        Map<String, String> map = getParam("checkCategoryAndSubCategoryLogin");

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String productCategory = map.get("productCategory");

        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open attractions category");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Looping on Categories and Sub-Categories");
        Assertion.assertThat("Assert if every Sub-Categories has valid result", ttdSearchResultPage.isCategoriesSubCategoriesValid(), is(true));
    }

    @Author(names = {"Prerna, Naufal", "Aditi"})
    @Vertical(name = "TTD")
    @Tribe(name = "Experience")
    @Module(name = "Filter")
    @Test
    public void checkCategoryAndSubCategory() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkCategoryAndSubCategory");
        BaseTest.paramsThreadLocal.set(map);

        String productCategory = map.get("productCategory");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Login as Guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open attractions category");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Looping on Categories and Sub-Categories");
        boolean isCategoriesSubCategoriesValid = ttdSearchResultPage.isCategoriesSubCategoriesValid();

        Assertion.assertThat("Assert if every Sub-Categories has valid result", isCategoriesSubCategoriesValid, is(true));
    }

    @Author(names = {"Yazid", "Akbar","Kartik", "Prerna"})
    @Module(name = "Filter")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void shortByNearestLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        Map<String, String> map = getParam("shortByNearestLogin");

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String totalActivity = map.get("totalActivity");
        String productCategory = map.get("productCategory");

        BaseTest.paramsThreadLocal.set(map);

        step("User go to home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Select product by category");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Click 'Sort' button on bottom navbar");
        ttdSearchResultPage.clickSortByButton("Nearest/Terdekat");
        ttdSearchResultPage.setAppPermissionAllow();

        step("Verify Search Result Page");
        Assertion.assertThat("Activity card locations is matches each other", ttdSearchResultPage.isSortByNearestLocationCorrect(totalActivity), is(true));
    }

    @Author(names = {"Akbar", "Mridul"})
    @Module(name = "Filter")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkSortByHighestRating() {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        Map<String, String> map = getParam("checkSortByHighestRating");

        String productCategory = map.get("productCategory");
        String totalResult = map.get("totalResult");

        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Click 'Sort' button on bottom navbar");
        ttdSearchResultPage.clickSortByButton("Highest Rating/Rating Tertinggi");

        step("Assert The SRP based on sorted by 'Highest Rating'");
        Assertion.assertThat("Assert if SRP is already displayed the valid result content", ttdSearchResultPage.checkResultList("sort", "rating", Integer.parseInt(totalResult), new String[]{"desc"}), is(true));

    }

    @Author(names = {"Naufal", "Kartik"})
    @Module(name = "Filter")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkCombinationOfFiltersLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        Map<String, String> map = getParam("checkCombinationOfFiltersLogin");

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String totalActivity = map.get("totalActivity");
        String filterOption = map.get("filter_option");
        String productCategory = map.get("productCategory");

        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open SRP Category '" + productCategory + "'");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Open filter");
        ttdSearchResultPage.clickFilter();

        step("Choose filter");
        List<String> selectedFilter = ttdSearchResultPage.selectGeneralFilter(filterOption);

        step("Submit filter");
        ttdSearchResultPage.submitFilter();

        Assertion.assertThat("Greed dot icon is displayed", ttdSearchResultPage.isFilterActivated("general"), is(true));
        Assertion.assertThat("All activity card contains '" + filterOption + "' label",
                ttdSearchResultPage.isGeneralFilterApplied(totalActivity, selectedFilter), is(true));
    }

    @Author(names = {"Prerna", "Akbar", "Kartik"})
    @Module(name = "Filter")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkCombinationOfFilters() {
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        Map<String, String> map = getParam("checkCombinationOfFilters");

        String totalActivity = map.get("totalActivity");
        String filterOption = map.get("filter_option");
        String productCategory = map.get("productCategory");

        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("User Login is Guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open SRP Category '" + productCategory + "'");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Open filter");
        ttdSearchResultPage.clickFilter();

        step("Choose filter '" + filterOption + "'");
        List<String> selectedFilter = ttdSearchResultPage.selectGeneralFilter(filterOption);

        step("Submit filter");
        ttdSearchResultPage.submitFilter();

        Assertion.assertThat("Greed dot icon is displayed", ttdSearchResultPage.isFilterActivated("general"), is(true));
        Assertion.assertThat("All activity card contains '" + filterOption + "' label",
                ttdSearchResultPage.isGeneralFilterApplied(totalActivity, selectedFilter), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "Filter")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPriceFilter() {
        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        Map<String, String> map = getParam("checkPriceFilter");
        int totalActivity = Integer.parseInt(map.get("totalActivity"));
        String productCategory = map.get("productCategory");
        String priceChanges = map.get("priceChanges");
        int swipePercentage = Integer.parseInt(map.get("swipePercentage"));
        String currency = map.get("currency");
        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("User Login is Guest");
        loginPage.loginAsGuest();
        homePage.changeCurrency(currency, "");
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Open SRP Category '" + productCategory + "'");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Open filter");
        ttdSearchResultPage.clickFilter();

        step("Choose price filter");
        List<String> selectedPrices = ttdSearchResultPage.selectPriceFilter(priceChanges, swipePercentage);

        step("Submit filter");
        ttdSearchResultPage.submitFilter();

        step("Check product list order: price filter");
        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            String[] ProductNamesOrderAPI = ttdPage.getTTDSRPProductNameList(productCategory, "most popular", language, currency, selectedPrices);
            String[] ProductNamesOrderAPP = ttdSearchResultPage.getProductList(totalActivity);
            String[] ProductOrderAPIAdjusted = Arrays.copyOfRange(ProductNamesOrderAPI, 0, totalActivity);
            Assertion.assertThat("Assert SRP product list order on UI is based on API response by price filter", ProductNamesOrderAPP, arrayContaining(ProductOrderAPIAdjusted));
        }else {
            Assertion.assertThat("succeed to show content on SRP based on selected quick filter", ttdSearchResultPage.isPriceFilterApplied(totalActivity, selectedPrices,currency), is(true));
        }
    }

    @Author(names = {"Ashish"})
    @Module(name = "Filter")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPriceFilterLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        Map<String, String> map = getParam("checkPriceFilterLogin");
        String username = map.get("username");
        String password = map.get("password");
        int totalActivity = Integer.parseInt(map.get("totalActivity"));
        String productCategory = map.get("productCategory");
        String priceChanges = map.get("priceChanges");
        int swipePercentage = Integer.parseInt(map.get("swipePercentage"));
        String currency = map.get("currency");
        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith("email", username, password);
        homePage.changeCurrency(currency, username);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Open SRP Category '" + productCategory + "'");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Open filter");
        ttdSearchResultPage.clickFilter();

        step("Choose price filter");
        List<String> selectedPrices = ttdSearchResultPage.selectPriceFilter(priceChanges, swipePercentage);

        step("Submit filter");
        ttdSearchResultPage.submitFilter();

        step("Check product list order: price filter");
        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            String[] ProductNamesOrderAPI = ttdPage.getTTDSRPProductNameList(productCategory, "most popular", language, currency, selectedPrices);
            String[] ProductNamesOrderAPP = ttdSearchResultPage.getProductList(totalActivity);
            String[] ProductOrderAPIAdjusted = Arrays.copyOfRange(ProductNamesOrderAPI, 0, totalActivity);
            Assertion.assertThat("Assert SRP product list order on UI is based on API response by price filter", ProductNamesOrderAPP, arrayContaining(ProductOrderAPIAdjusted));
        }else {
            Assertion.assertThat("succeed to show content on SRP based on selected quick filter", ttdSearchResultPage.isPriceFilterApplied(totalActivity, selectedPrices,currency), is(true));
        }
    }

    @Author(names = {"Yazid"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void tiketPointsRedeemIDRLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("tiketPointsRedeemIDRLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        String pdProcessType = map.get("pdProcessType");
        int pdRatio = Integer.parseInt(map.get("pdRatio"));
        String currency = "IDR";

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        UlpPage<WebDriver> eliteRewardsPage = UlpPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        MemberApi<WebDriver> memberApi = new MemberApi<>(session);

        step("Go to home page");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Change currency to \"" + currency + "\"");
        homePage.changeCurrency(currency, username);
        String tiketPoints = homePage.getTiketPointAndChangeCurrency(currency);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        webViewPage.switchToWebViewUrl("to-do");
        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Get the total price");
        String totalPaymentText = ttdBookingFormPageWeb.getPrice();
        webViewPage.switchToNative();
        String totalPaymentNumber = String.valueOf(removeText(totalPaymentText));

        //inject tiket point
        step("Inject tiket point if required (tiket points < total price)");
        AccountData.Data accountData = memberApi.getAccountDataByEmail(username);
        String pdAccountId = String.valueOf(accountData.accountId);
        tiketPoints = eliteRewardsPage.injectTiketPointsBasedOnCondition(Integer.parseInt(pdAccountId), Integer.parseInt(totalPaymentNumber), pdProcessType, pdRatio, username, tiketPoints);

        step("Check if order can be book using full tiket points");
        Assertion.assertThat("is tiket point more then or equal to total price ", homePage.canBeBookForFullPoint(totalPaymentNumber, tiketPoints), is(true));

        step("Submit booking");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Successfully redirected to the payment page");
        String orderID = paymentPage.checkOrderID();

        step("Use the tiket points");
        paymentPage.payWithTiketPoint();
        Assertion.assertThat("total price changes to gratis/free", paymentPage.checkTotalPriceChangesToFree(), is(true));

        step("Submit the payment method");
        paymentPage.clickPayButton();

        step("Go to order detail page");
        paymentPage.seeOrderList();
        orderListPage.openActiveTicketViaDeeplink(orderID);

        step("Check the order detail");
        Assertion.assertThat("Order ID on the Order Detail and Payment Page should be equal", orderDetailPage.getOrderID(), is(orderID));
        Assertion.assertThat("Total Price on the Order Detail and Payment page should be equal", orderDetailPage.getTotalPrice(), is(currency + " 0"));
        Assertion.assertThat("Activity Name on the Order Detail and PDP should be equal", orderDetailPage.getTTDProductTitle().toLowerCase(), is(productName.toLowerCase()));
    }

    @Author(names = {"Yazid"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void completeBookingInstantPassSelectedDateProduct() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingInstantPassSelectedDateProduct");
        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", "");
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(false);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("BCA Virtual Account");
        String totalPayment = paymentPage.getTotalPricePayment();
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Success create booking until order list");
        Assertion.assertThat("Order ID on the Order List and Payment Page should be equal", orderListPage.getOrderId(), is(orderID));
        Assertion.assertThat("Total Price on the Order List and Payment page should be equal", orderListPage.getTotalPrice(), is(totalPayment));
        Assertion.assertThat("Activity Name on the Order List and PDP should be equal", orderListPage.getProductTitle().toLowerCase(), is(productName.toLowerCase()));
    }

    @Author(names = {"Akbar"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void completeBookingSpecificSupplier() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingSpecificSupplier");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdPackageDetailPageWeb<?> ttdPackageDetailPageWeb = TtdPackageDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));

        BaseTest.paramsThreadLocal.set(map);

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket();

        step("Change to tomorrow date");
        ttdSelectedPackageDetailPageWeb.chooseTomorrowDate();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(false);

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("BCA Virtual Account");
        String totalPayment = paymentPage.getTotalPricePayment();
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Success create booking until order list");
        Assertion.assertThat("Order ID on the Order List and Payment Page should be equal", String.valueOf(getOrderId(orderListPage.getOrderId())), is(orderID));
        Assertion.assertThat("Total Price on the Order List and Payment page should be equal", orderListPage.getTotalPrice().replace(",", "").replace(".", ""), is(totalPayment.replace(",", "").replace(".", "")));
        Assertion.assertThat("Activity Name on the Order List and PDP should be equal", orderListPage.getProductTitle().toLowerCase().trim(), is(productName.toLowerCase().trim()));
    }

    @Author(names = {"Akbar"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void completeBookingSpecificSupplierLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingSpecificSupplierLogin");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));

        BaseTest.paramsThreadLocal.set(map);

        step("Go to home page");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", username);

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(1, false);

        step("Change to tomorrow date");
        ttdSelectedPackageDetailPageWeb.chooseTomorrowDate();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("BCA Virtual Account");
        String totalPayment = paymentPage.getTotalPricePayment();
        paymentPage.clickPayButton();
        paymentPage.checkNotifyPreviousOrderHasNotBeenCompleted();

        step("Go to the order list");
        paymentPage.seeOrderList();
        homePage.closePromoPopup();

        step("Success create booking until order list");
        Assertion.assertThat("Order ID on the Order List and Payment Page should be equal", orderListPage.matchOrderId(orderID), is(true));
        Assertion.assertThat("Total Price on the Order List and Payment page should be equal", orderListPage.getTotalPrice(), is(totalPayment));
        Assertion.assertThat("Activity Name on the Order List and PDP should be equal", orderListPage.getProductTitle().toLowerCase(), is(productName.toLowerCase()));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Filter")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkFilterLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        Map<String, String> map = getParam("checkFilterLogin");

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String filterOption = map.get("filter_option");
        String totalActivity = map.get("totalActivity");
        String productCategory = map.get("productCategory");
        String language = map.get("language");

        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Set Language App to " + language.toUpperCase());
        String actualLanguage = homePage.getLanguageApp();
        if (!actualLanguage.equalsIgnoreCase("id")) {
            step("Change Language to 'ENG'");
            homePage.openAccount();
            accountPage.selectLanguage("ENG");
            settingPage.backToHome();
        }

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open SRP Category '" + productCategory + "'");
        ttdLandingPage.openProductByCategory(productCategory);

        step("click filter");
        ttdSearchResultPage.clickFilter();

        step("Choose filter");
        List<String> selectedFilter = ttdSearchResultPage.selectGeneralFilter(filterOption);

        step("Submit filter");
        ttdSearchResultPage.submitFilter();

        Assertion.assertThat("Greed dot icon is displayed", ttdSearchResultPage.isFilterActivated("general"), is(true));
        Assertion.assertThat("First '" + totalActivity + "' activity card contains '" + filterOption + "' label", ttdSearchResultPage.isGeneralFilterApplied(totalActivity, selectedFilter), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Filter")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkFilter() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkFilter");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String filterOption1 = map.get("filter_option1");
        String filterOption2 = map.get("filter_option2");
        String filterOption3 = map.get("filter_option3");
        String totalActivity = map.get("totalActivity");
        String productCategory = map.get("productCategory");
        String language = map.get("language");

        step("Go To Home");
        homePage.gotoHome();

        step("Login as Guest");
        loginPage.loginAsGuest();

        step("Set Language App to " + language.toUpperCase());
        String actualLanguage = homePage.getLanguageApp();
        if (!actualLanguage.equalsIgnoreCase("id")) {
            step("Change Language to 'ENG'");
            homePage.openAccount();
            accountPage.selectLanguage("ENG");
            settingPage.backToHome();
        }

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open SRP Category '" + productCategory + "'");
        ttdLandingPage.openProductByCategory(productCategory);

        step("click filter");
        ttdSearchResultPage.clickFilter();

        step("click reset button");
        ttdSearchResultPage.clickReset();

        step("verify that no filters are applied");
        Assertion.assertThat("Green dot icon is not displayed", ttdSearchResultPage.isFilterActivated("general"), is(false));

        step("click filter again");
        ttdSearchResultPage.clickFilter();

        step("Choose filters");
        List<String> selectedFilter1 = ttdSearchResultPage.selectGeneralFilter(filterOption1);
        List<String> selectedFilter2 = ttdSearchResultPage.selectGeneralFilter(filterOption2);
        List<String> selectedFilter3 = ttdSearchResultPage.selectGeneralFilter(filterOption3);

        step("Submit filter");
        ttdSearchResultPage.submitFilter();

        Assertion.assertThat("Green dot icon is displayed", ttdSearchResultPage.isFilterActivated("general"), is(true));
        Assertion.assertThat("First '" + totalActivity + "' activity card contains '" + filterOption1 + "' label", ttdSearchResultPage.isGeneralFilterApplied(totalActivity, selectedFilter1), is(true));
        Assertion.assertThat("First '" + totalActivity + "' activity card contains '" + filterOption1 + "' label", ttdSearchResultPage.isGeneralFilterApplied(totalActivity, selectedFilter2), is(true));
        Assertion.assertThat("First '" + totalActivity + "' activity card contains '" + filterOption1 + "' label", ttdSearchResultPage.isGeneralFilterApplied(totalActivity, selectedFilter3), is(true));
    }

    @Author(names = {"Kartik", "Akbar"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void completeBookingTimeSlotLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        Map<String, String> map = getParam("completeBookingTimeSlotLogin");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        String packageName = map.get("packageName");

        String product = map.get("product");
        BaseTest.paramsThreadLocal.set(map);

        step("Go to home page");
        homePage.gotoHome();

        step("User input Login Details");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", username);

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Change to tomorrow date");
        ttdSelectedPackageDetailPageWeb.chooseTomorrowDate();

        step("Set total ticket");
        String timeSlotText = ttdSelectedPackageDetailPageWeb.chooseTimeSlot();

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);
        Assertion.assertThat("Verify tiket point is displayed", ttdSelectedPackageDetailPageWeb.isTiketPointDisplayed(), is(true));
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Price breakdown on booking form page");
        Assertion.assertThat("Verify price detail breakdown", ttdBookingFormPageWeb.priceBreakDown("IDR"), is(true));

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("BCA Virtual Account");
        String totalPayment = paymentPage.getTotalPricePayment();
        paymentPage.clickPayButton();
        paymentPage.checkNotifyPreviousOrderHasNotBeenCompleted();

        step("Go to the order list");
        paymentPage.seeOrderList();
        String orderIDOrderList = String.valueOf(getOrderId(orderListPage.getOrderId()));
        String totalPriceOrderList = orderListPage.getTotalPrice();
        String activityNameOrderList = orderListPage.getProductTitle().toLowerCase();

        step("Go to the order list and open order detail");
        orderListPage.openFirstWaitingPaymentOrder();
        String timeSlotOrderDetail = orderDetailPage.getTimeSlot().toLowerCase();

        step("Success create booking until order list");
        Assertion.assertThat("Order ID on the Order List and Payment Page should be exist", orderIDOrderList, is(orderID));
        Assertion.assertThat("Total Price on the Order List and Payment page should be equal", totalPriceOrderList, is(totalPayment));
        Assertion.assertThat("Activity Name on the Order List and PDP should be equal", activityNameOrderList, is(productName.toLowerCase()));
        Assertion.assertThat("Time Slot on the Order List and Booking Detail Page should be equal", timeSlotOrderDetail, containsString(timeSlotText));
    }

    @Author(names = {"Prerna", "Akbar"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void completeBookingTimeSlot() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingTimeSlot");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        int packageNumber = Integer.parseInt(map.get("packageNumber"));
        String product = map.get("product");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(packageNumber);

        step("Change to tomorrow date");
        ttdSelectedPackageDetailPageWeb.chooseTomorrowDate();

        step("Set total ticket");
        String timeSlotText = ttdSelectedPackageDetailPageWeb.chooseTimeSlot();

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);
        Assertion.assertThat("Verify tiket point is displayed", ttdSelectedPackageDetailPageWeb.isTiketPointDisplayed(), is(true));
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(false);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Price breakdown on booking form page");
        Assertion.assertThat("Verify price detail breakdown", ttdBookingFormPageWeb.priceBreakDown("IDR"), is(true));

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("BCA Virtual Account");
        String totalPayment = paymentPage.getTotalPricePayment();
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();
        String orderIDOrderList = String.valueOf(getOrderId(orderListPage.getOrderId()));
        String totalPriceOrderList = orderListPage.getTotalPrice();
        String activityNameOrderList = orderListPage.getProductTitle().toLowerCase();

        step("Go to the order detail");
        orderListPage.openFirstWaitingPaymentOrder();
        String timeSlotOrderDetail = orderDetailPage.getTimeSlot().toLowerCase();

        step("Success create booking until order list");
        Assertion.assertThat("Order ID on the Order List and Payment Page should be exist", orderIDOrderList, is(orderID));
        Assertion.assertThat("Total Price on the Order List and Payment page should be equal", totalPriceOrderList, is(totalPayment));
        Assertion.assertThat("Activity Name on the Order List and PDP should be equal", activityNameOrderList, is(productName.toLowerCase()));
        Assertion.assertThat("Time Slot on the Order List and Booking Detail Page should be equal", timeSlotOrderDetail, containsString(timeSlotText));
    }

    @DataProvider(name = "openProductViaDeeplinkLoginDataProvider")
    public Object[][] openProductViaDeeplinkLoginDataProvider() {
        return getObjectParams("openProductViaDeeplinkLogin");
    }

    @Author(names = {"Yazid"})
    @Module(name = "Sample")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test(dataProvider = "openProductViaDeeplinkLoginDataProvider", invocationCount = 10, threadPoolSize = 1) // invocationCount = 10: run 10 times & threadPoolSize = 1: sequentially
    public void openProductViaDeeplinkLogin(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String product = map.get("productName");
        String slugProductName = map.get("slugProductName");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("open PDP via deeplink");
        ttdSearchResultPage.openPdpDeeplink(slugProductName);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();
        Assertion.assertThat("User is on the PDP page", productName.toLowerCase(), containsString(product.toLowerCase()));
    }

    @Author(names = {"Prerna"})
    @Module(name = "TTD")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void TTD_completeBookingProductViaDeeplinkLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> map = getParam("TTD_completeBookingProductViaDeeplinkLogin");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String slugProductName = map.get("slugProductName");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<?> orderDetailPage = OrderDetailPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");
        homePage.refreshHomePage(3);

        step("open PDP via deeplink");
        ttdSearchResultPage.openPdpDeeplink(slugProductName);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();

        step("Choose First Ticket");
        ttdProductDetailPageWeb.chooseTicket();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");
        String totalPayment = paymentPage.getTotalPricePayment();
        paymentPage.clickPayButton();
        paymentPage.checkNotifyPreviousOrderHasNotBeenCompleted();

        step("Go to the order list");
        paymentPage.seeOrderList(orderID);

        step("Success create booking until order list");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        orderDetailPage.hidePromoBannerIfPresent();
        Assertion.assertThat("Order ID on the Order List and Payment Page should be equal", orderDetailPage.getOrderID(), is(orderID));
        Assertion.assertThat("Total Price on the Order List and Payment page should be equal", normalizePrice(orderDetailPage.getTotalPayment()), is(normalizePrice(totalPayment)));
        Assertion.assertThat("Activity Name on the Order List and PDP should be equal", orderDetailPage.getTTDProductTitle().toLowerCase(), is(productName.toLowerCase()));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void hitDestinationUrlAreaLogin() {
        Map<String, String> map = getParam("hitDestinationUrlAreaLogin");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String areaId = map.get("areaId");
        String areaName = map.get("areaName");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Hit the destination URL area: " + areaId);
        ttdSearchResultPage.openSrpDeeplinkUsingPublicId(areaId);
        Assertion.assertThat("Redirected to the SRP", ttdSearchResultPage.getLocation(), is(areaName));
        Assertion.assertThat("No filter is applied", ttdSearchResultPage.isFilterActivated("Category All"), is(false));
    }

    @Author(names = {"Akbar"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Video Rental")
    @Vertical(name = "TTD")
    @Test
    public void completeBookingVideoRentalPurchaseFlowLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingVideoRentalPurchaseFlowLogin");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdPackageDetailPageWeb<?> ttdPackageDetailPageWeb = TtdPackageDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        VideoRentalPage<WebDriver> videoRentalPage = VideoRentalPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String product = map.get("product");
        String paymentMethod = map.get("paymentMethod");

        BaseTest.paramsThreadLocal.set(map);

        step("Go to home page");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(1);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("Submit the payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickPayButton();
        paymentPage.checkNotifyPreviousOrderHasNotBeenCompleted();
        String[] orderIDArray = paymentPage.checkOrderID().split(" ");
        long orderID = Long.parseLong(orderIDArray[orderIDArray.length - 1]);

        step("Wait untill the order is issued");
        ttdPage.checkOrderStatus(username, password, orderID);

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Access and play the video");
        orderListPage.findActiveTicket(productName, "-", Long.toString(orderID), 3);
        orderListPage.clickLinkToVideo();
        videoRentalPage.clickPlayVideo();

        step("Succeed play the video");
        Assertion.assertThat("Video play button should be disappeared", videoRentalPage.isPlayButtonDisplayed(), is(false));
        Assertion.assertThat("Video controller should be existed", videoRentalPage.isVideoControlExisted(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Video Rental")
    @Vertical(name = "TTD")
    @Test
    public void completeBookingVideoRentalPurchaseFlowGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingVideoRentalPurchaseFlowGuest");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSelectedPackageDetailPage<WebDriver> ttdSelectedPackageDetailPage = TTDSelectedPackageDetailPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);

        String product = map.get("product");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(1);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();
        boolean isLoginPopupDisplayed = ttdSelectedPackageDetailPage.isLoginPopupDisplayed();
        ttdSelectedPackageDetailPage.clickLoginButtonPopup();
        boolean isOnLoginPage = loginPage.isOnLoginPage();

        step("Assertion");
        Assertion.assertThat("Login Popup Should Appeared", isLoginPopupDisplayed, is(true));
        Assertion.assertThat("Should be in Login Page", isOnLoginPage, is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "Filter")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkSortByLowestPrice() {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        Map<String, String> map = getParam("checkSortByLowestPrice");
        int totalActivity = Integer.parseInt(map.get("totalActivity"));
        String productCategory = map.get("productCategory");
        String currency = "IDR";
        BaseTest.paramsThreadLocal.set(map);

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();
        homePage.changeCurrency(currency, "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open product category");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Click sort button on search result page");
        ttdSearchResultPage.clickSortByButton("Lowest Price/Harga Terendah");

        step("Get SRP Data from API");
        GETProductListTTDSRPPOJO productDatafromAPI = ttdPage.getProductListTTDSRP(productCategory, "all", "lowest price", language, currency);

        step("Assert SRP sorted by the Lowest price");
        int currencyDecimalDigit = 0;
        Assertion.assertThat("Assert SRP is displaying correct price data and format", ttdSearchResultPage.verifyDisplayedPriceAmountProductCard(currencyDecimalDigit, totalActivity, productDatafromAPI), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void instantExchangePassWithDate() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> map = getParam("instantExchangePassWithDate");
        String product = map.get("product");
        String labelType = map.get("label_type");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        String totalActivity = map.get("totalActivity");
        String filterOption = map.get("filter_option");
        String productCategory = map.get("productCategory");

        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();
        String language = homePage.getLanguageApp();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open SRP Category '" + productCategory + "'");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Open filter");
        ttdSearchResultPage.clickFilter();

        step("Choose filter '" + filterOption + "'");
        List<String> selectedFilter = ttdSearchResultPage.selectGeneralFilter(filterOption);

        step("Submit filter");
        ttdSearchResultPage.submitFilter();

        Assertion.assertThat("Green dot icon is displayed", ttdSearchResultPage.isFilterActivated("general"), is(true));
        Assertion.assertThat("All activity card contains '" + filterOption + "' label",
                ttdSearchResultPage.isGeneralFilterApplied(totalActivity, selectedFilter), is(true));

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();

        step("Check the '" + labelType + "' label on PDP");
        boolean labelCheck = ttdProductDetailPageWeb.checkLabel(labelType);

        step("Select ticket");
        ttdProductDetailPageWeb.chooseTicket(1);

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        String selectedDate = ttdSelectedPackageDetailPageWeb.getSelectedDate();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(false);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("BCA Virtual Account");
        String totalPayment = paymentPage.getTotalPricePayment();
        paymentPage.clickPayButton();
        paymentPage.checkNotifyPreviousOrderHasNotBeenCompleted();

        step("Go to the order list");
        paymentPage.seeOrderList();
        String orderListOrderId = String.valueOf(getOrderId(orderListPage.getOrderId()));
        String orderListTotalPrice = orderListPage.getTotalPrice();
        String orderListProductName = orderListPage.getProductTitle().toLowerCase();

        step("Go to order detail page");
        orderListPage.openOrderDetailWaitingForPayment(orderID);
        String orderDetailDate = orderDetailPage.getTTDValidityPeriod();

        step("Success create booking until order list");
        Assertion.assertThat("On PDP should contain '" + labelType + "' label", labelCheck, is(true));
        Assertion.assertThat("Order ID on the Order List and Payment Page should be equal", orderListOrderId, is(orderID));
        Assertion.assertThat("Total Price on the Order List and Payment page should be equal", CoreFunctions.convertPrice(orderListTotalPrice), is(CoreFunctions.convertPrice(totalPayment)));
        Assertion.assertThat("Activity Name on the Order List and PDP should be equal", orderListProductName, is(productName.toLowerCase()));
        Assertion.assertThat("Date should be aligned with the selected date", orderDetailDate, is(containsString(selectedDate)));
    }

    @DataProvider(name = "checkEntryListOfDestinationDataProvider")
    public Object[][] checkEntryListOfDestinationDataProvider() {
        return getObjectParams("checkEntryPointListOfDestination");
    }

    @Author(names = {"Ashish"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testCompleteBookingSpecificSupplierBySearchGuest() {
        Map<String, String> param = getParam("testCompleteBookingSpecificSupplierBySearchGuest");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        String product = param.get("product");
        int ticketQty = Integer.parseInt(param.get("ticket_qty"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicketRandom();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(false);

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("BCA Virtual Account");
        String totalPayment = paymentPage.getTotalPricePayment();
        paymentPage.clickPayButton();
        paymentPage.checkNotifyPreviousOrderHasNotBeenCompleted();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Success create booking until order list");
        Assertion.assertThat("Order ID on the Order List and Payment Page should be equal", orderListPage.getOrderId(), is(orderID));
        Assertion.assertThat("Total Price on the Order List and Payment page should be equal", CoreFunctions.convertPrice(orderListPage.getTotalPrice()), is(CoreFunctions.convertPrice(totalPayment)));
        Assertion.assertThat("Activity Name on the Order List and PDP should be equal", orderListPage.getProductTitle(), is(productName));
    }

    @Author(names = {"Ashish"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testCompleteBookingSpecificSupplierBySearchLogin() {
        Map<String, String> param = getParam("testCompleteBookingSpecificSupplierBySearchLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        String product = param.get("product");
        int ticketQty = Integer.parseInt(param.get("ticket_qty"));
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicketRandom();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("BCA Virtual Account");
        String totalPayment = paymentPage.getTotalPricePayment();
        paymentPage.clickPayButton();
        paymentPage.checkNotifyPreviousOrderHasNotBeenCompleted();

        step("Go to the order list");
        paymentPage.seeOrderList();
        orderListPage.openOrderDetailWaitingForPayment(orderID);

        step("Success create booking until order list");
        Assertion.assertThat("Order ID on the Order List and Payment Page should be exist", orderDetailPage.getOrderID(), is(orderID));
        Assertion.assertThat("Total Price on the Order List and Payment page should be equal", orderDetailPage.getTotalPayment(), is(totalPayment));
        Assertion.assertThat("Activity Name on the Order List and PDP should be equal", orderDetailPage.getTTDProductTitle().toLowerCase(), is(productName.toLowerCase()));
    }

    @Author(names = {"Kartik, Naufal"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularActivitiesLogin() {
        Map<String, String> param = getParam("checkPopularActivitiesLogin");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Click one of Popular Activities on TTD Search Page");
        Map<String, String> popularActivity = ttdSearchPage.pickAndClickPopularActivity();
        String productName = popularActivity.get("name");
        String productType = popularActivity.get("type");

        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Assert if Activity Name is Displayed and  Valid", productName.toLowerCase(), is(ttdProductDetailPageWeb.getProductName().toLowerCase()));
        Assertion.assertThat("Assert if Activity Type is Displayed and Valid", productType.toLowerCase(), is(ttdProductDetailPageWeb.getProductType().toLowerCase()));
    }

    @Author(names = {"Kartik"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPopularDestinationGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPopularDestinationGuest");

        BaseTest.paramsThreadLocal.set(map);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDExplorePage<WebDriver> ttdExplorePage = TTDExplorePage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();
        homePage.goToHomeDeeplink();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        List<String> popularDestinationsFromAPI = new ArrayList<>();
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Check product list from API: popular destinations");
            TTDPopularDestinationDataPOJO popularDestinationsFromAPIPOJO = ttdPage.getPopularDestinationData(language);
            popularDestinationsFromAPI = ttdPage.getDestinationTitleList(popularDestinationsFromAPIPOJO);
        }

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Click Popular Destination Tab on TTD Search Page");
        ttdSearchPage.selectSection("destinations");

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Check product list order: popular destinations");
            List<String> popularDestinationsFromUI = ttdSearchPage.getProductTitleOrder();
            Assertion.assertThat("Destination title order should be matched with API", popularDestinationsFromUI, is(popularDestinationsFromAPI));
        }

        step("Click one of Popular Destinations on TTD Search Page");
        String clickedPopularDestination = ttdSearchPage.clickItemOnList("random");

        step("Verify Step 1");
        Assertion.assertThat("Assert if Destination Name is valid", clickedPopularDestination, is(containsString(ttdSearchResultPage.getLocation())));

        step("Go Back");
        ttdSearchResultPage.clickBack();

        step("Open Popular Destination Tab on TTD Search Page");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.selectSection("destinations");

        step("Click see all button");
        ttdSearchPage.clickSeeAllButton();

        step("Verify Step 2");
        Assertion.assertThat("Assert if redirected to destination list page", ttdExplorePage.isExplorePageValid(), is(true));
    }

    @Author(names = {"Kartik", "Akbar"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void instantExchangePassWithDateLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("instantExchangePassWithDateLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String totalActivity = map.get("totalActivity");
        String filterOption = map.get("filter_option");
        String productCategory = map.get("productCategory");

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open SRP Category '" + productCategory + "'");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Open filter");
        ttdSearchResultPage.clickFilter();

        step("Choose filter '" + filterOption + "'");
        List<String> selectedFilter = ttdSearchResultPage.selectGeneralFilter(filterOption);

        step("Submit filter");
        ttdSearchResultPage.submitFilter();

        Assertion.assertThat("Green dot icon is displayed", ttdSearchResultPage.isFilterActivated("general"), is(true));
        Assertion.assertThat("All activity card contains '" + filterOption + "' label",
                ttdSearchResultPage.isGeneralFilterApplied(totalActivity, selectedFilter), is(true));

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click search bar on TTD landing page");
        ttdLandingPage.clickSearchBar();

        step("Search the product '" + product + "' and select");
        ttdSearchPage.inputSearchKeyword(product);
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.chooseTicket(1);

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        String selectedDate = ttdSelectedPackageDetailPageWeb.getSelectedDate();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("BCA Virtual Account");
        String totalPayment = paymentPage.getTotalPricePayment();
        paymentPage.clickPayButton();
        paymentPage.checkNotifyPreviousOrderHasNotBeenCompleted();

        step("Go to the order list");
        paymentPage.seeOrderList(orderID);
        homePage.closePromoPopup();
        Assertion.assertThat("Order ID on the Order List and Payment Page should be equal", orderListPage.matchOrderId(orderID), is(true));
        Assertion.assertThat("Total Price on the Order List and Payment page should be equal", orderListPage.getTotalPrice(), is(totalPayment));
        Assertion.assertThat("Activity Name on the Order List and PDP should be equal", orderListPage.getProductTitle().toLowerCase(), is(productName.toLowerCase()));

        step("Go to order detail page");
        orderListPage.openOrderDetailWaitingForPayment(orderID);
        String orderDetailDate = orderDetailPage.getTTDValidityPeriod();

        step("Success create booking until order list");
        Assertion.assertThat("Date should be aligned with the selected date", orderDetailDate, is(containsString(selectedDate)));
    }

    @Author(names = {"Kartik"})
    @Module(name = "SRP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkExploreByNearbyGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkExploreByNearbyGuest");
        BaseTest.paramsThreadLocal.set(map);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Click Check Nearby Activity");
        ttdSearchPage.clickNearbyActivity();
        ttdSearchResultPage.setAppPermissionAllow();

        step("Verify Search Result Page");
        Assertion.assertThat("Check if SRP is shown", ttdSearchResultPage.isSearchResultPageShown(), is(true));
    }

    @Author(names = {"Kartik", "Akbar"})
    @Module(name = "DestinationPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkEntryPointAutoCompleteSuggestionViaExplorePageGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkEntryPointAutoCompleteSuggestionViaExplorePageGuest");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDDestinationPage<WebDriver> ttdDestinationPage = TTDDestinationPage.getInstance(session);
        TTDExplorePage<WebDriver> ttdExplorePage = TTDExplorePage.getInstance(session);

        String keyword = map.get("search_input");
        String type = map.get("search_type");
        String pageType = "explore";

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Explore Arrow on TTD Landing Page");
        ttdLandingPage.clickEnableLocation();
        ttdLandingPage.clickExploreArrow();

        step("Do Search on TTD Search Page");
        ttdExplorePage.isExplorePageValid();
        ttdExplorePage.inputSearchKeyword(keyword);
        Assertion.assertThat("Check Autocomplete Suggestion", ttdSearchResultPage.isAutoCompleteSuggestionValid(keyword, type, pageType), is(true));

        step("Click on Targeted Location");
        ttdSearchResultPage.selectLocation(keyword, pageType);
        Assertion.assertThat("Assert if Destination Name is valid", ttdDestinationPage.getDestinationName().toLowerCase().trim(), is(keyword.toLowerCase()));
    }

    @Author(names = {"Kartik, Naufal"})
    @Module(name = "DestinationPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkEntryPointListOfDestinationLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkEntryPointListOfDestinationLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDExplorePage<WebDriver> ttdExplorePage = TTDExplorePage.getInstance(session);
        TTDDestinationPage<WebDriver> ttdDestinationPage = TTDDestinationPage.getInstance(session);

        String keyword = map.get("search_input");
        String type = map.get("search_type");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Explore Arrow on TTD Landing Page");
        ttdLandingPage.clickEnableLocation();
        ttdLandingPage.clickExploreArrow();
        ttdExplorePage.isExplorePageValid();

        step("Searching for destination | search_input = '" + keyword + "'");
        String selectedDestination = ttdExplorePage.selectDestination(keyword, type);

        Assertion.assertThat("Assert if Destination Name is valid", selectedDestination.toLowerCase(), is(ttdDestinationPage.getDestinationNameOrSRPLocation().toLowerCase()));
    }

    @Author(names = {"Prerna"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkSeeAllPopularActivitiesLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkSeeAllPopularActivitiesLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar");
        ttdLandingPage.clickSearchBar();

        step("click Popular Activity see all button");
        ttdSearchPage.clickPopularActivitySeeAll();

        step("Assertion");
        Assertion.assertThat("Directed To SRP with ALL Tab", ttdSearchResultPage.isSearchResultPageShown() && ttdSearchResultPage.isAllTabShown(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void clearRecentSearchLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("clearRecentSearchLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String product = map.get("product");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("Choose One of The Search Result");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Back to Search Page");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.clickBack();
        webViewPage.switchToNative();
        ttdSearchPage.clickBack();
        ttdLandingPage.clickSearchBar();

        step("User click on Clear All button");
        ttdSearchPage.clickClearAll();
        Assertion.assertThat("Assert if Recently search cleared", ttdSearchPage.isSearchCleared(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkSeeAllPopularDestinationLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkSeeAllPopularDestinationLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDExplorePage<WebDriver> ttdExplorePage = TTDExplorePage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar");
        ttdLandingPage.clickSearchBar();

        step("Click Popular Destination Tab on TTD Search Page");
        ttdSearchPage.selectSection("destinations");

        step("click Popular Destination see all button");
        ttdSearchPage.clickSeeAllButton();

        step("Assertion");
        Assertion.assertThat("Directed To TTD Explore Page", ttdExplorePage.isExplorePageValid(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkXButtonOnSearchPage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkXButtonOnSearchPage");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("searchInput");

        step("Go To Home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar");
        ttdLandingPage.clickSearchBar();

        step("Enter search input");
        ttdSearchPage.inputSearchKeyword(product);

        step("getting text before X button");
        String text_before = ttdSearchPage.getSearchFieldText();

        step("Click Cross(X) button");
        ttdSearchPage.clickClearSearch();

        step("getting text after X button");
        String text_after = ttdSearchPage.getSearchFieldText();

        Assertion.assertThat("Search input is entered in search field", text_before, is(product));
        Assertion.assertThat("Search field is cleared and has default text", text_after, not(product));
    }

    @Author(names = {"Kartik"})
    @Vertical(name = "TTD")
    @Tribe(name = "Experience")
    @Module(name = "SRP")
    @Test
    public void checkDateFilterLogin() {

        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkDateFilterLogin");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String productCategory = map.get("productCategory");
        String date = map.get("date");
        String filterType = map.get("filterType");

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open '" + productCategory + "' category");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Open Date filter");
        ttdSearchResultPage.clickFilter();

        step("Verify Step 1");
        Assertion.assertThat("Date Filter have 3 chips", ttdSearchResultPage.verifyDateFilterChip(), is(true));

        step("Select Date filter");
        ttdSearchResultPage.selectDateFilter(date);
        ttdSearchResultPage.submitFilter();

        step("Verify Step 2");
        Assertion.assertThat("Date Filter have green Dot on it", ttdSearchResultPage.isFilterActivated(filterType), is(true));
    }

    @Author(names = {"Kartik"})
    @Vertical(name = "TTD")
    @Tribe(name = "Experience")
    @Module(name = "SRP")
    @Test
    public void checkDateFilterGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkDateFilterGuest");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        String productCategory = map.get("productCategory");

        String date = map.get("date");
        String filterType = map.get("filterType");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open '" + productCategory + "' category");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Open Date filter");
        ttdSearchResultPage.clickFilter();

        step("Verify Step 1");
        Assertion.assertThat("Date Filter have 3 chips", ttdSearchResultPage.verifyDateFilterChip(), is(true));

        step("Select Date filter");
        ttdSearchResultPage.selectDateFilter(date);

        step("Verify Step 2");
        Assertion.assertThat("Date Filter have green Dot on it", ttdSearchResultPage.isFilterActivated(filterType), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkTopKeywordRefreshLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkTopKeywordRefreshLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar");
        ttdLandingPage.clickSearchBar();

        step("Get Keyword list before clicking refresh button");
        List<String> listBeforeRefresh = ttdSearchPage.getTopKeywordList();

        step("click refresh button");
        ttdSearchPage.clickRefreshTopKeyword();

        step("Get Keyword list after clicking refresh button");
        List<String> listAfterRefresh = ttdSearchPage.getTopKeywordList();

        Assertion.assertThat("Top Keyword changes after clicking Refresh button", !listAfterRefresh.equals(listBeforeRefresh), is(true));
    }

    @Author(names = {"Prerna", "Kartik"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void selectOneTopKeywordLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("selectOneTopKeywordLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar");
        ttdLandingPage.clickSearchBar();

        step("Click random keyword");
        String selectedKeyword = ttdSearchPage.clickRandomKeyword();

        Assertion.assertThat("selected keyword is in search bar", ttdSearchPage.getSearchFieldText(), is(selectedKeyword));
    }

    @Author(names = {"Prerna", "Kartik"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void CheckWebviewAndBottomsheetLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("CheckWebviewAndBottomsheetLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");

        step("Go to home page");
        homePage.gotoHome();

        step("Input Login details");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("click the open hours section");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.openHoursSection();
        Assertion.assertThat("Is bottom sheet open for open Hours", ttdProductDetailPageWeb.isBottomSheetOpen("Open", "Buka"), is(true));
        ttdProductDetailPageWeb.closeSection();

        step("Go to More Tab");
        ttdProductDetailPageWeb.clickTabSection("More", "Lainnya", "left");

        step("click the redemption info section");
        ttdProductDetailPageWeb.openMoreInfoSection("Redemption Info", "Penukaran Tiket");
        Assertion.assertThat("Is bottom sheet open for redemption info", ttdProductDetailPageWeb.isBottomSheetOpen("Redemption Info", "Penukaran Tiket"), is(true));
        ttdProductDetailPageWeb.closeSection();

        step("click the term & condition section");
        ttdProductDetailPageWeb.openMoreInfoSection("Terms & Conditions", "Syarat & Ketentuan");
        Assertion.assertThat("Is bottom sheet open for term & cond  ition", ttdProductDetailPageWeb.isBottomSheetOpen("Terms & Conditions", "Syarat & Ketentuan"), is(true));
        ttdProductDetailPageWeb.closeSection();

        step("click the additional info section");
        ttdProductDetailPageWeb.openMoreInfoSection("Additional Information", "Informasi Tambahan");
        Assertion.assertThat("Is bottom sheet open for additional info", ttdProductDetailPageWeb.isBottomSheetOpen("Additional Information", "Informasi Tambahan"), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "PDP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void CheckWebviewAndBottomsheet() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("CheckWebviewAndBottomsheet");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String product = map.get("product");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("click the open hours section");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.openHoursSection();
        Assertion.assertThat("Is bottom sheet open for open Hours", ttdProductDetailPageWeb.isBottomSheetOpen("Open Hours", "Jam Buka"), is(true));
        ttdProductDetailPageWeb.closeSection();

        step("Go to More Tab");
        ttdProductDetailPageWeb.clickTabSection("More", "Lainnya", "left");

        step("click the redemption info section");
        ttdProductDetailPageWeb.openMoreInfoSection("Redemption Info", "Penukaran Tiket");
        Assertion.assertThat("Is bottom sheet open for redemption info", ttdProductDetailPageWeb.isBottomSheetOpen("Redemption Info", "Penukaran Tiket"), is(true));
        ttdProductDetailPageWeb.closeSection();

        step("click the term & condition section");
        ttdProductDetailPageWeb.openMoreInfoSection("Terms & Conditions", "Syarat & Ketentuan");
        Assertion.assertThat("Is bottom sheet open for term & condition", ttdProductDetailPageWeb.isBottomSheetOpen("Terms & Conditions", "Syarat & Ketentuan"), is(true));
        ttdProductDetailPageWeb.closeSection();

        step("click the additional info section");
        ttdProductDetailPageWeb.openMoreInfoSection("Additional Information", "Informasi Tambahan");
        Assertion.assertThat("Is bottom sheet open for additional info", ttdProductDetailPageWeb.isBottomSheetOpen("Additional Information", "Informasi Tambahan"), is(true));
    }

    @Author(names = {"Prerna", "Mridul"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkLandingPageWithLocationOff() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkLandingPageWithLocationOff");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Disable location service");
        homePage.setLocationService("OFF");

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open ALL location");
        String location = ttdLandingPage.getLocationText();
        Assertion.assertThat("Location should contains ALL LOCATION ", location.toLowerCase(), either(containsString("all locations")).or(containsString("semua lokasi")));

        step("Click location text");
        ttdLandingPage.clickLocationText();
        Assertion.assertThat("Show Search destinations WITHOUT City, Region, Country chips based on user current location", ttdLandingPage.isCurrentLocationOptionAppears(), is(false));
    }

    @Author(names = {"Prerna , Mridul"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkLandingPageWithLocationOffLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkLandingPageWithLocationOffLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go to home page");
        homePage.gotoHome();

        step("User login");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Disable location service");
        homePage.setLocationService("OFF");

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open ALL location");
        String location = ttdLandingPage.getLocationText();
        Assertion.assertThat("Location should contains ALL LOCATION ", location.toLowerCase(), either(containsString("all locations")).or(containsString("semua lokasi")));

        step("Click location text");
        ttdLandingPage.clickLocationText();
        Assertion.assertThat("Show Search destinations WITHOUT City, Region, Country chips based on user current location", ttdLandingPage.isCurrentLocationOptionAppears(), is(false));
    }

    @Author(names = {"Kartik", "Yazid"})
    @Module(name = "Filter")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkSortByNewestGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkSortByNewestGuest");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        String filterType = map.get("filterType");
        String productCategory = map.get("productCategory");

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open any category");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Click sort button on search result page");
        ttdSearchResultPage.clickSortByButton("Newly Added/Baru Ditambahkan");

        step("Assertion");
        Assertion.assertThat("Directed To Search result page With newly added product", ttdSearchResultPage.isSearchResultPageShown(), is(true));
        Assertion.assertThat("Sort Filter have green Dot on it", ttdSearchResultPage.isFilterActivated(filterType), is(true));
    }

    @Author(names = {"Kartik", "Yazid"})
    @Module(name = "Filter")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkSortByNewestLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkSortByNewestLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        String filterType = map.get("filterType");
        String username = map.get("username");
        String password = map.get("password");
        String loginType = map.get("loginType");
        String productCategory = map.get("productCategory");

        step("Go to home page");
        homePage.gotoHome();

        step("Input Login details");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open any category");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Click sort button on search result page");
        ttdSearchResultPage.clickSortByButton("Newly Added/Baru Ditambahkan");

        step("Assertion");
        Assertion.assertThat("Directed To Search result page With newly added product", ttdSearchResultPage.isSearchResultPageShown(), is(true));
        Assertion.assertThat("Sort Filter have green Dot on it", ttdSearchResultPage.isFilterActivated(filterType), is(true));
    }

    @Author(names = {"Kartik", "Yazid", "Akbar"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void landingPageAllowLocationGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("landingPageAllowLocationGuest");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Set to allow location access");
        homePage.setLocationService("ON");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");
        String location = ttdLandingPage.getLocationText();
        Assertion.assertThat("Location should contains ALL LOCATION ", location.toLowerCase(), either(containsString("all locations")).or(containsString("semua lokasi")));

        step("Click location text");
        ttdLandingPage.clickLocationText();
        Assertion.assertThat("Show Search destinations with City, Region, Country chips based on user current location", ttdLandingPage.isCurrentLocationOptionAppears(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "SRP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkExploreByNearbyLocationOnLogin() {

        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkExploreByNearbyLocationOnLogin");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String filterType = map.get("filterType");

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Set to allow location access");
        homePage.setLocationService("ON");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Get Activated Location text on Landing page");
        String activatedLocationText = ttdLandingPage.getLocationText();

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Click Check Nearby Activity");
        ttdSearchPage.clickNearbyActivity();

        step("Verify Search Result Page");
        Assertion.assertThat("Check if location on Both Page is same ", activatedLocationText.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""), containsString(ttdSearchResultPage.getLocation().toLowerCase().replaceAll("[^a-zA-Z0-9]", "")));
        Assertion.assertThat("Sort Filter have green Dot on it", ttdSearchResultPage.isFilterActivated(filterType), is(true));
    }

    @Author(names = {"Prerna", "Yazid"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void completeBookingCategoryExpiryDate() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingCategoryExpiryDate");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdPackageDetailPageWeb<?> ttdPackageDetailPageWeb = TtdPackageDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);

        String product = map.get("product");//      input format: EnProductName/INDProductName
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        int packageIndex = Integer.parseInt(map.get("package_index"));

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", "");

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Find the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        String productName = ttdProductDetailPageWeb.getProductName();
        Assertion.assertThat("Product Name on PDP is same as Product on Test Sheet", productName, is(equalToIgnoringCase(product)));

        step("Go to Package List");
        ttdProductDetailPageWeb.goToPackagesList();

        step("Click Detail");
        ttdProductDetailPageWeb.clickDetailButton(packageIndex);

        step("Get Valid day text Before Selecting ticket");
        String validDayTextBefore = ttdPackageDetailPageWeb.getValidDayText();

        step("Verify validity in card detail");
        Assertion.assertThat("valid day text is there on Package detail", validDayTextBefore, notNullValue());

        step("Select Ticket");
        ttdPackageDetailPageWeb.clickSelectTicketFromDetail();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Find validity period on package detail");
        String validityPeriodPackageDetail = ttdSelectedPackageDetailPageWeb.getValidityPeriod();

        Assertion.assertThat("validity period is there", validityPeriodPackageDetail, notNullValue());

        step("Get Total Price from Price Detail");
        String totalPriceOnPackageDetail = ttdSelectedPackageDetailPageWeb.getTotalPrice();

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("valid date text on booking form is same as earlier", ttdBookingFormPageWeb.getValidDateText(), is(validDayTextBefore));

        step("get validity period from booking form");
        String validityPeriodBookingForm = ttdBookingFormPageWeb.getValidityPeriod();
        Assertion.assertThat("validity period text on booking form is same as earlier", ttdBookingFormPageWeb.compareValidityText(validityPeriodBookingForm, validityPeriodPackageDetail), is(true));

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(false);
        ttdBookingFormPageWeb.fillGuestDetail();

        Assertion.assertThat("price on booking form is same as initial price", ttdBookingFormPageWeb.getPrice(), is(totalPriceOnPackageDetail));

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("BCA Virtual Account");

        step("Get validity period on Payment page");
        paymentPage.openOrderDetail();
        String validityPeriodPaymentPage = paymentPage.getValidityPeriod();
        paymentPage.closeGuestDetail();

        Assertion.assertThat("Validity date on payment page matches with previous validity date", ttdBookingFormPageWeb.compareValidityText(validityPeriodPaymentPage, validityPeriodPackageDetail), is(true));
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();
        orderListPage.refreshPage(3);

        step("Success create booking until order list");
        Assertion.assertThat("Order ID on the Order List and Payment Page should be equal", orderListPage.getOrderId(), containsString(orderID));
        Assertion.assertThat("Activity Name on the Order List and PDP should be equal", orderListPage.getProductTitle().toLowerCase(), is(productName.toLowerCase()));
    }

    @Author(names = {"Kartik", "Yazid"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void completeBookingCategoryExpiryDateLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingCategoryExpiryDateLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String productName = map.get("product");

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", username);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Find the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(productName);
        ttdSearchPage.selectRecommendedActivitySRP(productName);

        step("Go to Package List");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();

        step("Select Ticket");
        ttdProductDetailPageWeb.chooseTicket();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);
        String totalPriceOnPackageDetail = ttdSelectedPackageDetailPageWeb.getTotalPrice();
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Check that the order page is opened");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("User will be directed to Order Details bottomsheet for non-event & expanded view for event", ttdBookingFormPageWeb.isOrderPageOpen(), is(true));

        step("Check if booking form page is opened");
        Assertion.assertThat("Booking form page is opened", ttdBookingFormPageWeb.isBookingInfoDislayed(), is(true));

        step("Activity section is displayed");
        Assertion.assertThat("Activity Information section is present", ttdBookingFormPageWeb.isActivitiesInfoPresent(), is(true));

        step("Product name is correct");
        Assertion.assertThat("Product name on booking form is correct", ttdBookingFormPageWeb.getProductTitle().toLowerCase(), is(productName.toLowerCase()));

        step("Ticket quantity is correct");
        Assertion.assertThat("Ticket quantity is correct", ttdBookingFormPageWeb.getTicketQuantity(), containsString(String.valueOf(ticketQty)));

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        Assertion.assertThat("price on booking form is same as initial price", ttdBookingFormPageWeb.getPrice(), is(totalPriceOnPackageDetail));

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("BCA Virtual Account");
        paymentPage.clickPayButton();
        paymentPage.checkNotifyPreviousOrderHasNotBeenCompleted();

        step("Go to the order list");
        paymentPage.seeOrderList();
        homePage.closePromoPopup();
        orderListPage.openOrderDetailWaitingForPayment(orderID);
        orderDetailPage.getOrderID();

        step("Success create booking until order list");
        Assertion.assertThat("Order ID on the Order List and Payment Page should be equal", orderDetailPage.getOrderID(), is(orderID));

    }

    @Author(names = {"Kartik", "Prerna"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testLandingPageUserClickOnCtaLogin() {
        Map<String, String> map = getParam("testLandingPageUserClickOnCtaLogin");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();
        String language = homePage.getLanguageApp();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", username);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Get landing page data from API");
        TTDLandingPagePOJO.Data ttdLandingPageContent = ttdPage.getLandingPageContent(language);
        String sectionName = ttdPage.getLandingPageSectionName(ttdLandingPageContent);
        String sectionTitle = ttdPage.getLandingPageSectionTitle(ttdLandingPageContent);

        step("Get section Detail on landing Page");
        boolean isSectionVisible = ttdLandingPage.getSectionName(sectionName, sectionTitle);
        boolean isProductNameVisible = ttdLandingPage.verifyRedirectionCTA(sectionTitle);

        step("Assertion");
        Assertion.assertThat("Verify found the section name and its product fetched from API on UI", isSectionVisible, is(true));
        Assertion.assertThat("Verify click CTA and check redirection", isProductNameVisible, is(true));
    }

    @Author(names = {"Kartik", "Prerna"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testLandingPageUserClickOnCtaGuest() {
        Map<String, String> map = getParam("testLandingPageUserClickOnCtaGuest");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", "");

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Get landing page data from API");
        TTDLandingPagePOJO.Data ttdLandingPageContent = ttdPage.getLandingPageContent(language);
        String sectionName = ttdPage.getLandingPageSectionName(ttdLandingPageContent);
        String sectionTitle = ttdPage.getLandingPageSectionTitle(ttdLandingPageContent);

        step("Get section Detail on landing Page");
        boolean isSectionVisible = ttdLandingPage.getSectionName(sectionName, sectionTitle);
        boolean isProductNameVisible = ttdLandingPage.verifyRedirectionCTA(sectionTitle);

        step("Assertion");
        Assertion.assertThat("Verify found the section name and its product fetched from API on UI", isSectionVisible, is(true));
        Assertion.assertThat("Verify click CTA and check redirection", isProductNameVisible, is(true));
    }

    @Author(names = {"Kartik", "Prerna", "Akbar"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testLocationFilterOnSRPLogin() {
        Map<String, String> map = getParam("testLocationFilterOnSRPLogin");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();
        String language = homePage.getLanguageApp();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("open attraction");
        ttdLandingPage.openProductByCategory("attractions");

        step("open locations");
        ttdSearchResultPage.clickLocation();

        step("Choose random location");
        String location = ttdSearchResultPage.selectRandomLocation();

        step("get data from API");
        TTDSRPByLocationPOJO TTDSRPContents = ttdPage.getTTDSRPContentByLocation(language, location, "ATTRACTION");
        Assertion.assertThat("API is working properly with " + (TTDSRPContents.data == null || TTDSRPContents.data.length == 0 ? "'empty result'" : "'" + TTDSRPContents.data.length + "' result"), TTDSRPContents.message, notNullValue());

        step("Compare API data with UI");
        Assertion.assertThat("Data matches with API", ttdSearchResultPage.compareProductNameWithAPI(TTDSRPContents, 3), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Playback")
    @Tribe(name = "Tiket Live")
    @Vertical(name = "TTD")
    @Test
    public void verifyPlayBackOfLiveEventLogin() {
        Map<String, String> map = getParam("verifyPlayBackOfLiveEventLogin");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDTiketLivePage<WebDriver> ttdTiketLivePage = TTDTiketLivePage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String productLink = map.get("productLink");

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("open live event via deeplink");
        homePage.openLiveEventViaDeeplink(productLink);

        step("Click play button");
        ttdTiketLivePage.clickPlayButton();

        Assertion.assertThat("Play button is not there", ttdTiketLivePage.isPlayButtonDisappear(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Playback")
    @Tribe(name = "Tiket Live")
    @Vertical(name = "TTD")
    @Test
    public void verifyPlayBackOfLiveEventGuest() {
        Map<String, String> map = getParam("verifyPlayBackOfLiveEventGuest");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDTiketLivePage<WebDriver> ttdTiketLivePage = TTDTiketLivePage.getInstance(session);

        String productLink = map.get("productLink");

        step("Go To Home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("open live event via deeplink");
        homePage.openLiveEventViaDeeplink(productLink);

        step("Click play button");
        ttdTiketLivePage.clickPlayButton();

        Assertion.assertThat("Play button is not there", ttdTiketLivePage.isPlayButtonDisappear(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Playback")
    @Tribe(name = "Tiket Live")
    @Vertical(name = "TTD")
    @Test
    public void verifyPlayBackFromEventPromotionalBannerLogin() {
        Map<String, String> map = getParam("verifyPlayBackFromEventPromotionalBannerLogin");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDTiketLivePage<WebDriver> ttdTiketLivePage = TTDTiketLivePage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("open video from promotional banner");
        homePage.clickVideo();

        Assertion.assertThat("User is on Tiket Live Page", ttdTiketLivePage.userIsOnTiketLivePage(), is(true));

        step("Click play button");
        ttdTiketLivePage.clickPlayButton();

        Assertion.assertThat("Play button is not there", ttdTiketLivePage.isPlayButtonDisappear(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Playback")
    @Tribe(name = "Tiket Live")
    @Vertical(name = "TTD")
    @Test
    public void verifyPlayBackFromEventPromotionalBannerGuest() {
        Map<String, String> map = getParam("verifyPlayBackFromEventPromotionalBannerGuest");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDTiketLivePage<WebDriver> ttdTiketLivePage = TTDTiketLivePage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Login as Guest");
        loginPage.loginAsGuest();

        step("open video from promotional banner");
        homePage.clickVideo();

        Assertion.assertThat("User is on Tiket Live Page", ttdTiketLivePage.userIsOnTiketLivePage(), is(true));

        step("Click play button");
        ttdTiketLivePage.clickPlayButton();

        Assertion.assertThat("Play button is not there", ttdTiketLivePage.isPlayButtonDisappear(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "PackageDetail")
    @Tribe(name = "Video Rental")
    @Vertical(name = "TTD")
    @Test
    public void bookVideoRentalGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("bookVideoRentalGuest");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSelectedPackageDetailPage<WebDriver> ttdSelectedPackageDetailPage = TTDSelectedPackageDetailPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);

        String product = map.get("product");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(1);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();
        boolean isLoginPopupDisplayed = ttdSelectedPackageDetailPage.isLoginPopupDisplayed();
        ttdSelectedPackageDetailPage.clickLoginButtonPopup();
        boolean isOnLoginPage = loginPage.isOnLoginPage();
        Assertion.assertThat("Login Popup Should Appeared", isLoginPopupDisplayed, is(true));
        Assertion.assertThat("Should be in Login Page", isOnLoginPage, is(true));
    }

    @Author(names = {"Kartik", "Prerna", "Mridul"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testReloadOnLandingPageGuest() {
        Map<String, String> map = getParam("testReloadOnLandingPageGuest");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", "");

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Get inventory Status from API");
        TTDLandingPagePOJO ttdLandingPageCompleteContent = ttdPage.getCompleteLandingPageData(language);
        String[] sectionNameList = ttdLandingPage.getReloadSectionAndChips(ttdLandingPageCompleteContent);

        if (sectionNameList[0] != null) {
            step("Swipe to section having empty inventory");
            ttdLandingPage.findSpecificSection(sectionNameList[0]);

            step("click chips having empty inventory");
            ttdLandingPage.clickChips(sectionNameList[1]);

            Assertion.assertThat("is reload section there?", ttdLandingPage.isReloadSectionVisible(sectionNameList[0]), is(true));
        } else {
            skip("no section with empty inventory data");
        }
    }

    @Author(names = {"Kartik", "Prerna", "Mridul"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testReloadOnLandingPageLogin() {
        Map<String, String> map = getParam("testReloadOnLandingPageLogin");
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();
        String language = homePage.getLanguageApp();

        step("Input login details");
        loginPage.loginWith(loginType, username, password);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Get inventory Status from API");
        TTDLandingPagePOJO ttdLandingPageCompleteContent = ttdPage.getCompleteLandingPageData(language);
        String[] sectionNameList = ttdLandingPage.getReloadSectionAndChips(ttdLandingPageCompleteContent);

        if (sectionNameList[0] != null) {
            step("Swipe to section having empty inventory");
            ttdLandingPage.findSpecificSection(sectionNameList[0]);

            step("click chips having empty inventory");
            ttdLandingPage.clickChips(sectionNameList[1]);

            Assertion.assertThat("is reload section there?", ttdLandingPage.isReloadSectionVisible(sectionNameList[0]), is(true));
        } else {
            skip("no section with empty inventory data");
        }
    }

    @Author(names = {"Akbar"})
    @Module(name = "SRP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkSRPDestinationAreaGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkSRPDestinationAreaGuest");
        BaseTest.paramsThreadLocal.set(map);

        String location = map.get("areaName");
        String locationType = "area";
        String productCategory = map.get("productCategory");
        int productCount = Integer.parseInt(map.get("productCount"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Go To SRP");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Select location area '" + location + "'");
        ttdSearchResultPage.clickLocation();
        ttdSearchResultPage.inputLocationSearchBar(location);
        String clickedLocation = ttdSearchResultPage.clickLocationSearchResult(location, locationType);

        step("click filter");
        ttdSearchResultPage.clickFilter();

        step("click reset button");
        ttdSearchResultPage.clickReset();

        step("Verify Step");
        Assertion.assertThat("Show correct SRP location", ttdSearchResultPage.getLocation().toLowerCase(), is(clickedLocation.toLowerCase()));
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("get data from API");
            TTDSRPByLocationPOJO TTDSRPContents = ttdPage.getTTDSRPContentByLocation(language, location, "");
            Assertion.assertThat("API is working properly with " + (TTDSRPContents.data == null || TTDSRPContents.data.length == 0 ? "'empty result'" : "'" + TTDSRPContents.data.length + "' result"), TTDSRPContents.message, notNullValue());

            step("Compare API data with UI");
            Assertion.assertThat("Data matches with API", ttdSearchResultPage.compareProductNameWithAPI(TTDSRPContents, productCount), is(true));
        }
    }

    @Author(names = {"Akbar"})
    @Module(name = "SRP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkSRPDestinationAreaLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkSRPDestinationAreaLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("areaName");
        String locationType = "area";
        String productCategory = map.get("productCategory");
        int productCount = Integer.parseInt(map.get("productCount"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Go To SRP");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Select location area '" + location + "'");
        ttdSearchResultPage.clickLocation();
        ttdSearchResultPage.inputLocationSearchBar(location);
        String clickedLocation = ttdSearchResultPage.clickLocationSearchResult(location, locationType);

        step("click filter");
        ttdSearchResultPage.clickFilter();

        step("click reset button");
        ttdSearchResultPage.clickReset();

        step("Verify Step");
        Assertion.assertThat("Show correct SRP location", ttdSearchResultPage.getLocation().toLowerCase(), is(clickedLocation.toLowerCase()));
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("get data from API");
            TTDSRPByLocationPOJO TTDSRPContents = ttdPage.getTTDSRPContentByLocation(language, location, "");
            Assertion.assertThat("API is working properly with " + (TTDSRPContents.data == null || TTDSRPContents.data.length == 0 ? "'empty result'" : "'" + TTDSRPContents.data.length + "' result"), TTDSRPContents.message, notNullValue());

            step("Compare API data with UI");
            Assertion.assertThat("Data matches with API", ttdSearchResultPage.compareProductNameWithAPI(TTDSRPContents, productCount), is(true));
        }
    }

    @Author(names = {"Akbar"})
    @Module(name = "SRP")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkNearbySectionDestinationListPageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkNearbySectionDestinationListPageLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDExplorePage<WebDriver> ttdExplorePage = TTDExplorePage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        if(ttdLandingPage.isLocationOff()) {
            ttdLandingPage.clickEnableLocation();

            step("Click Search Bar on TTD SRP");
            ttdSearchResultPage.clickSearchBar();
        } else {
            step("Click Search Bar on TTD SRP");
            ttdLandingPage.clickSearchBar();
        }

        step("Click Popular Destination Tab on TTD Search Page");
        ttdSearchPage.selectSection("destinations");

        step("Click see all button");
        ttdSearchPage.clickSeeAllButton();

        step("Verify Step 1");
        List<String> nearbyDestinationText = ttdExplorePage.getNearbyDestinationChipText();
        Assertion.assertThat("Show search destinations with City, Region, Country chips", ttdExplorePage.getNearbyDestinationChips(), greaterThanOrEqualTo(2));
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Verify Step 1 GK");
            String[] checkedDestinationType = ttdPage.verifyNearbyDestinationType(nearbyDestinationText, language);
            HashSet<String> duplicates = new HashSet<>(List.of(checkedDestinationType));
            Assertion.assertThat("Make sure the chips are showing City, Region, and Country destination type without duplicating", checkedDestinationType.length, is(duplicates.size()));
            Assertion.assertThat("Make sure the chips are showing City, Region, and Country destination type", checkedDestinationType, arrayContaining("CITY", "REGION", "COUNTRY"));
        }

        step("Verify step 2");
        Assertion.assertThat("Assert if the chip is clicked redirect to correct page with valid location name", ttdExplorePage.verifyAllNearbyLocationChips(language), is(true));
    }

    @Author(names = {"Yazid", "Aditi","Mridul Gupta"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkNearbySectionDestinationListPageSRPLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkNearbySectionDestinationListPageSRPLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String productCategory = map.get("productCategory");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Set to allow location access");
        homePage.setLocationService("ON");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Select any category to go to SRP");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Click location button on Search Page (SRP)");
        ttdSearchPage.clickLocationButton();

        step("Assertions for Location Chips");
        Assertion.assertThat("All chip regarding location are displayed", ttdSearchPage.verifyLocationChips(), either(is(greaterThanOrEqualTo(2))).or(is(lessThanOrEqualTo(4))));

        step("Click the City Chip");
        String location = ttdSearchPage.getCityLocationChipText();
        ttdSearchPage.clickCityLocationChip();

        step("Verify redirection to SRP with correct location");
        Assertion.assertThat("User is on SRP with correct location", ttdSearchPage.getLocation().toLowerCase(), is(location.toLowerCase()));
    }

    @Author(names = {"Akbar"})
    @Module(name = "TTD")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void TTD_verifyRefundFlowAndCheckRefundAmountLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("TTD_verifyRefundFlowAndCheckRefundAmountLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticketQty"));
        String reasonTitle = map.get("reasonTitle");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.clickMorePackages();
        ttdProductDetailPageWeb.chooseTicketWithRefundable();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("Submit Sakuku as payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");
        paymentPage.clickPayButton();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Go to order detail page");
        orderListPage.openActiveTicketViaDeeplink(orderID);

        step("Click 'Refund' button");
        orderDetailPage.clickRefundButton();
        refundPage.getBookedDate();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView(reasonTitle);
        String estimatedRefundAmount = refundPage.getEstimatedRefundPoint();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Submit refund");
        refundPage.submitRefundPoint();

        step("check refund submit successfully");
        boolean isRefundSubmitSuccessfully = refundPage.checkRefundSubmission();
        Assertion.assertThat("Successfully submit the refund", isRefundSubmitSuccessfully, is(true));

        step("Go to the refund detail");
        refundPage.viewRefundDetailPage();

        step("Verify total refund");
        String totalRefundDetails = refundPage.getFinalRefund();
        Assertion.assertThat("Total refund on the refund detail page should be " + estimatedRefundAmount, totalRefundDetails, is(estimatedRefundAmount));
    }

    @Author(names = {"Akbar"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyNonIdrCurrencyShowingAccordinglyAllPageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyNonIdrCurrencyShowingAccordinglyAllPageLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");
        String commonProduct = map.get("commonProduct");
        String destinationType = map.get("destinationType");
        String productCategory = map.get("productCategory");
        String productPartner = map.get("productPartner");
        int packageNumber = Integer.parseInt(map.get("packageNumber"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDDestinationPage<WebDriver> ttdDestinationPage = TTDDestinationPage.getInstance(session);
        TTDPartnerPage<WebDriver> ttdPartnerPage = TTDPartnerPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Change currency to '" + currency + "'");
        homePage.changeCurrency(currency, username);

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Check TTD Landing Page");
        Assertion.assertThat("Verify price text of product cards in landing page", ttdLandingPage.verifyDisplayedCurrency(currency), is(true));
        ttdLandingPage.swipeToTop();

        step("Go to TTD SRP");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Check TTD SRP Price Filter");
        ttdSearchResultPage.clickFilter();
        Assertion.assertThat("Verify price text of product cards in SRP price filter", ttdSearchResultPage.verifyDisplayedCurrency(currency, "price filter"), is(true));
        ttdSearchResultPage.closeFilterBottomsheet();

        step("Check TTD SRP");
        Assertion.assertThat("Verify price text of product cards in SRP", ttdSearchResultPage.verifyDisplayedCurrency(currency, "product card"), is(true));
        ttdSearchResultPage.clickBack();

        step("Go to TTD Destination Page");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(commonProduct);
        ttdSearchPage.selectRecommendedActivity(commonProduct);

        step("Check TTD Destination Page");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.swipeToDestinationSectionBottom();
        ttdProductDetailPageWeb.selectDestinationByHierarchy(destinationType);
        webViewPage.switchToNative();
        Assertion.assertThat("Verify price text of product cards in destination page", ttdDestinationPage.verifyDisplayedCurrency(currency), is(true));
        ttdDestinationPage.clickBack();
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.clickBack();
        webViewPage.switchToNative();

        step("Go to TTD PDP 1");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(productPartner);
        ttdSearchPage.selectRecommendedActivity(productPartner);

        step("Check TTD PDP 1");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Verify price text of display price in PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "display price"), is(true));

        step("Check TTD Partner Page");
        ttdProductDetailPageWeb.clickProductPartner();
        webViewPage.switchToNative();
        Assertion.assertThat("Verify price text of product cards in partner page", ttdPartnerPage.verifyDisplayedCurrency(currency, "partner product"), is(true));
        ttdPartnerPage.clickBack();
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.clickBack();
        webViewPage.switchToNative();

        step("Go to TTD PDP 2");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(commonProduct);
        ttdSearchPage.selectRecommendedActivity(commonProduct);

        step("Check TTD PDP 2");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Verify price text of pdp recommendation product cards in PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "pdp recommendation"), is(true));
        Assertion.assertThat("Verify price text of package list in PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "package list"), is(true));
        ttdProductDetailPageWeb.clickDetailButton(packageNumber);
        Assertion.assertThat("Verify price text in package detail in PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "package detail"), is(true));

        step("Go to TTD ticket quantity page");
        ttdProductDetailPageWeb.closeSection();
        ttdProductDetailPageWeb.chooseTicket(packageNumber);

        step("Check TTD ticket quantity page");
        Assertion.assertThat("Verify price text of price tier in ticket quantity page", ttdSelectedPackageDetailPageWeb.verifyDisplayedCurrency(currency, "price tier"), is(true));

        step("Go to TTD booking form page");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(1);
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Check TTD booking form page");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Verify price text in booking form page", ttdBookingFormPageWeb.verifyDisplayedCurrency(currency, "booking page"), is(true));
        Assertion.assertThat("Verify price text of price detail in booking form page", ttdBookingFormPageWeb.verifyDisplayedCurrency(currency, "price detail"), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyNonIdrCurrencyShowingAccordinglyAllPageGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyNonIdrCurrencyShowingAccordinglyAllPageGuest");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String commonProduct = map.get("commonProduct");
        String destinationType = map.get("destinationType");
        String productCategory = map.get("productCategory");
        String productPartner = map.get("productPartner");
        int packageNumber = Integer.parseInt(map.get("packageNumber"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDDestinationPage<WebDriver> ttdDestinationPage = TTDDestinationPage.getInstance(session);
        TTDPartnerPage<WebDriver> ttdPartnerPage = TTDPartnerPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("user is on non-login mode");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Change currency to '" + currency + "'");
        homePage.changeCurrency(currency, "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Check TTD Landing Page");
        Assertion.assertThat("Verify price text of product cards in landing page", ttdLandingPage.verifyDisplayedCurrency(currency), is(true));
        ttdLandingPage.swipeToTop();

        step("Go to TTD SRP");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Check TTD SRP Price Filter");
        ttdSearchResultPage.clickFilter();
        Assertion.assertThat("Verify price text of product cards in SRP price filter", ttdSearchResultPage.verifyDisplayedCurrency(currency, "price filter"), is(true));
        ttdSearchResultPage.closeFilterBottomsheet();

        step("Check TTD SRP");
        Assertion.assertThat("Verify price text of product cards in SRP", ttdSearchResultPage.verifyDisplayedCurrency(currency, "product card"), is(true));
        ttdSearchResultPage.clickBack();

        step("Go to TTD Destination Page");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(commonProduct);
        ttdSearchPage.selectRecommendedActivity(commonProduct);

        step("Check TTD Destination Page");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.swipeToDestinationSectionBottom();
        ttdProductDetailPageWeb.selectDestinationByHierarchy(destinationType);
        webViewPage.switchToNative();
        Assertion.assertThat("Verify price text of product cards in destination page", ttdDestinationPage.verifyDisplayedCurrency(currency), is(true));
        ttdDestinationPage.clickBack();
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.clickBack();
        webViewPage.switchToNative();

        step("Go to TTD PDP 1");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(productPartner);
        ttdSearchPage.selectRecommendedActivity(productPartner);

        step("Check TTD PDP 1");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Verify price text of display price in PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "display price"), is(true));

        step("Check TTD Partner Page");
        ttdProductDetailPageWeb.clickProductPartner();
        webViewPage.switchToNative();
        Assertion.assertThat("Verify price text of product cards in partner page", ttdPartnerPage.verifyDisplayedCurrency(currency, "partner product"), is(true));
        ttdPartnerPage.clickBack();
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.clickBack();
        webViewPage.switchToNative();

        step("Go to TTD PDP 2");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(commonProduct);
        ttdSearchPage.selectRecommendedActivity(commonProduct);

        step("Check TTD PDP 2");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Verify price text of pdp recommendation product cards in PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "pdp recommendation"), is(true));
        Assertion.assertThat("Verify price text of package list in PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "package list"), is(true));
        ttdProductDetailPageWeb.clickDetailButton(packageNumber);
        Assertion.assertThat("Verify price text in package detail in PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "package detail"), is(true));

        step("Go to TTD ticket quantity page");
        ttdProductDetailPageWeb.closeSection();
        ttdProductDetailPageWeb.chooseTicket(packageNumber);

        step("Check TTD ticket quantity page");
        Assertion.assertThat("Verify price text of price tier in ticket quantity page", ttdSelectedPackageDetailPageWeb.verifyDisplayedCurrency(currency, "price tier"), is(true));

        step("Go to TTD booking form page");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(1);
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Check TTD booking form page");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Verify price text in booking form page", ttdBookingFormPageWeb.verifyDisplayedCurrency(currency, "booking page"), is(true));
        Assertion.assertThat("Verify price text of price detail in booking form page", ttdBookingFormPageWeb.verifyDisplayedCurrency(currency, "price detail"), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Loyalty")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void VerifyLoyaltyBenefit() {
        Map<String, String> map = getParam("VerifyLoyaltyBenefit");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String category = map.get("category");
        String Filter = map.get("Filter");
        int ticketQty = Integer.parseInt(map.get("ticketQty"));
        String productName = map.get("productName");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdPackageDetailPageWeb<?> ttdPackageDetailPageWeb = TtdPackageDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR", username);

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open SRP Category: " + category);
        ttdLandingPage.openProductByCategory(category);

        step("Verify loyalty banner on SRP");
        Assertion.assertThat("Verify loyalty badge on TTD SRP", ttdSearchResultPage.verifyDisplayedLoyaltyBanner(), is(true));

        step("Open filter");
        ttdSearchResultPage.clickFilter();

        step("Choose filter");
        ttdSearchResultPage.removeAllFilter();
        ttdSearchResultPage.selectGeneralFilter(Filter);

        step("Submit filter");
        ttdSearchResultPage.submitFilter();

        step("select Product with loyalty badge");
        ttdSearchResultPage.selectLoyaltyProduct(productName);

        step("Verify loyalty badge details on PDP");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.clickMorePackages();
        Assertion.assertThat("Verify loyalty badge inside details on TTD PDP", ttdPackageDetailPageWeb.verifyAndClickLoyaltyDetails(), is(true));

        step("select Ticket with loyalty badge");
        Assertion.assertThat("Verify loyalty badge on TTD PDP", ttdPackageDetailPageWeb.verifyAndClickLoyaltyBadge(), is(true));
        ttdProductDetailPageWeb.chooseTicketWithLoyalty();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking ");
        Assertion.assertThat("Verify price detail breakdown", ttdBookingFormPageWeb.priceBreakDown("IDR"), is(true));
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();

        step("Select payment method");
        paymentPage.selectPaymentMethod("BCA Virtual Account");

        step("Submit payment");
        paymentPage.clickPayButton();
        paymentPage.checkNotifyPreviousOrderHasNotBeenCompleted();

        step("Go to order detail page");
        paymentPage.seeOrderList();
        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            orderListPage.openActiveTicketViaDeeplink(orderID);
        } else {
            orderListPage.openOrderWaitingPayment(orderID);
        }
        step("Verify price detail");
        Assertion.assertThat("Verify price detail breakdown", orderDetailPage.verifyTTDLoyaltyDeduction(), is(true));
    }

    @Author(names = {"Akbar", "Yazid", "Mridul"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyDecimalWhenLessThan05InTDLanding() throws ExecutionException, InterruptedException {
        Map<String, String> map = getParam("verifyDecimalWhenLessThan05InTDLanding");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is non-login mode");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Change currency to '" + currency + "'");
        homePage.changeCurrency(currency, "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Search the target product that are having less than 0.5");
        List<TTDLandingPagePOJO.Data> ttdLandingPagePOJO = ttdPage.getAllLandingPageContent(language, currency);
        TTDLandingPagePOJO.Card productCardLessThan05Price = ttdPage.getProductCardByPriceDecimal(ttdLandingPagePOJO, "<", 0.5, language, currency);

        step("Go to the target product card");
        ttdLandingPage.goToProductCard(productCardLessThan05Price);
        Assertion.assertThat("Verify product card '" + productCardLessThan05Price.productName + "' price text in TTD Home Page", ttdLandingPage.isProductCardPriceVisible(productCardLessThan05Price, CoreFunctions.normalizeNonIdrPrice(true, productCardLessThan05Price.rateInfo.formattedPrice)), is(true));
    }

    @Author(names = {"Akbar", "Yazid", "Mridul"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyDecimalWhenEqualOrMoreThan05InTDLanding() throws ExecutionException, InterruptedException {
        Map<String, String> map = getParam("verifyDecimalWhenEqualOrMoreThan05InTDLanding");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is non-login mode");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Change currency to '" + currency + "'");
        homePage.changeCurrency(currency, "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Search the target product that are having equal or more than 0.5");
        List<TTDLandingPagePOJO.Data> ttdLandingPagePOJO = ttdPage.getAllLandingPageContent(language, currency);
        TTDLandingPagePOJO.Card productCardEqualOrMoreThan05Price = ttdPage.getProductCardByPriceDecimal(ttdLandingPagePOJO, ">=", 0.5, language, currency);

        step("Go to the target product card");
        ttdLandingPage.goToProductCard(productCardEqualOrMoreThan05Price);
        Assertion.assertThat("Verify product card '" + productCardEqualOrMoreThan05Price.productName + "' price text in TTD Home Page", ttdLandingPage.isProductCardPriceVisible(productCardEqualOrMoreThan05Price, CoreFunctions.normalizeNonIdrPrice(true, productCardEqualOrMoreThan05Price.rateInfo.formattedPrice)), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkSortByLowestPriceNonIDRGuest() {
        Map<String, String> map = getParam("checkSortByLowestPriceNonIDRGuest");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String productCategory = map.get("productCategory");
        int totalActivity = Integer.parseInt(map.get("totalActivity"));

        Session<WebDriver> session = sessionThreadLocal.get();
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Change currency to '" + currency + "'");
        homePage.changeCurrency(currency, "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Open category '" + productCategory + "'");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Click sort button on search result page");
        ttdSearchResultPage.clickSortByButton("Lowest Price/Harga Terendah");

        step("Assert SRP sorted by the lowest price");
        String[] ProductNamesOrderAPI = ttdPage.getTTDSRPProductNameList(productCategory, "all", "lowest price", language, currency);
        String[] ProductNamesOrderAPP = ttdSearchResultPage.getProductList(totalActivity);
        String[] ProductOrderAPIAdjusted = Arrays.copyOfRange(ProductNamesOrderAPI, 0, totalActivity);
        Assertion.assertThat("Assert SRP product list order on UI is sorted by the lowest price based on API response", ProductNamesOrderAPP, arrayContaining(ProductOrderAPIAdjusted));
    }

    @Author(names = {"Akbar"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyPartnerPageWhenUsingNonIDRLogin() {
        Map<String, String> map = getParam("verifyPartnerPageWhenUsingNonIDRLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String currency = map.get("currency");
        String product = map.get("product");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TTDPartnerPage<WebDriver> ttdPartnerPage = TTDPartnerPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Change currency to '" + currency + "'");
        homePage.changeCurrency(currency, username);

        step("Open TTD Landing Page");
        if (Configs.ENVIRONMENT == Environment.PROD) homePage.openVerticalPage("To Do");
        else homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Click searchbar");
        ttdLandingPage.clickSearchBar();

        step("Input product name '" + product + "'");
        ttdSearchPage.inputSearchKeyword(product);

        step("Select recommended activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Check TTD PDP currency");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Verify price text of display price in PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "display price"), is(true));
        Assertion.assertThat("Verify price text of package list in PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "package list"), is(true));

        step("Check TTD Partner Page");
        ttdProductDetailPageWeb.clickProductPartner();
        webViewPage.switchToNative();
        boolean isPageModuleAvailable = ttdPartnerPage.isPageModuleDisplayed();

        if (isPageModuleAvailable) {
            Assertion.assertThat("Verify price text of page module product cards in partner page", ttdPartnerPage.verifyDisplayedCurrency(currency, "page module"), is(true));
        }

        Assertion.assertThat("Verify price text of product cards in partner page", ttdPartnerPage.verifyDisplayedCurrency(currency, "partner product"), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "MultiCurrency")
    @Vertical(name = "TTD")
    @Test
    public void testBookingFlowNonIDRLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testBookingFlowNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String product = param.get("product");
        String currency = param.get("currency");
        String countryName = param.get("countryName");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        BaseTest.paramsThreadLocal.set(param);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to " + currency);
        homePage.changeCurrency(currency, username);
        accountPage.chooseCountry(countryName);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", "", currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(1);

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(1, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Get Price on Booking Form");
        String priceBookingForm = ttdBookingFormPageWeb.getPrice();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        Assertion.assertThat("Verify total price is in " + currency, paymentPage.getTotalPriceCurrency(), equalTo(currency));

        step("Fill CC Info");
        String priceOnPaymentPage = paymentPage.getTotalPriceFirstPage();
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Click pay button");
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();
        Boolean isPaymentSuccessful = paymentPage.isPaymentSuccessful();

        step("Verify Step");
        Assertion.assertThat("Is Price match with previous pages", priceOnPaymentPage, is(priceBookingForm));
        Assertion.assertThat("Asserting if the payment is successful ", isPaymentSuccessful, is(true));
    }

    @Author(names = {"Prerna", "Mridul Gupta"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPriceRoundingBelowTenNonIDRGuest() throws ExecutionException, InterruptedException {
        Map<String, String> map = getParam("checkPriceRoundingBelowTenNonIDRGuest");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String productCount = map.get("productCount");
        String productCategory = map.get("productCategory");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Change currency to '" + currency + "'");
        homePage.changeCurrency(currency, "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Open category '"+productCategory+"'");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Back to Homepage");
        homePage.goToHomeDeeplink();
        language = "IDN";

        step("Change language to \"" + language + "\"");
        homePage.openAccount();
        accountPage.selectLanguage(language);
        settingPage.backToHome();

        step("Open TTD Landing Page");
        language = homePage.getLanguageApp();
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("User will see \".\" for thousand and \",\" for decimal when user language is ID and currency is Non-IDR");
        Assertion.assertThat("thousand price should contains \".\"", ttdLandingPage.findPrice(THOUSAND, currency.toUpperCase()), containsString("."));
        ttdLandingPage.backToHomePage();
        homePage.openVerticalPageDeeplink("To Do", language, currency);
        Assertion.assertThat("decimal price should contains \",\"", ttdLandingPage.findPrice(DECIMAL, currency.toUpperCase()), containsString(","));

        step("Back to Homepage");
        homePage.goToHomeDeeplink();

        language = "ENG";
        step("Change language to \"" + language + "\"");
        homePage.openAccount();
        accountPage.selectLanguage(language);
        settingPage.backToHome();

        step("Open TTD Landing Page");
        language = homePage.getLanguageApp();
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("User will see \".\" for decimal and \",\" for thousand when user language is EN and currency is Non-IDR");
        Assertion.assertThat("decimal price should contains \".\"", ttdLandingPage.findPrice(DECIMAL, currency.toUpperCase()), containsString("."));
        ttdLandingPage.backToHomePage();
        homePage.openVerticalPageDeeplink("To Do", language, currency);
        Assertion.assertThat("thousand price should contains \",\"", ttdLandingPage.findPrice(THOUSAND, currency.toUpperCase()), containsString(","));

        step("Open TTD Landing Page");
        homePage.goToHomeDeeplink();
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Search the target product that are having less than 10");
        List<GETLandingPageForYouSectionPOJO.Data> getLandingPageForYouSectionPOJO = ttdPage.getAllLandingPageForYouSectionContent(language, currency);
        HashMap<String, String> productWithPriceBelowTen = ttdPage.getProductByPriceDecimalForYouSection(getLandingPageForYouSectionPOJO, "<", 10);

        step("Go to the target product card");
        String productName = productWithPriceBelowTen.get("productName");
        String productPrice = productWithPriceBelowTen.get("finalPrice");
        Assertion.assertThat("Product with price below 10 is found " + productName + " actual Price " + productPrice, ttdLandingPage.getUIPrice(productName, currency), containsString(productPrice));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "MultiCurrency")
    @Vertical(name = "TTD")
    @Test
    public void verifyEVoucherDetailsNonIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyEVoucherDetailsNonIDR");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDApi<?> ttdApi = new TTDApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        DefaultAppsPage<?> defaultAppsPage = DefaultAppsPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String productUrlName = param.get("productUrlName");
        boolean refundable = true;
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String contactFullName = randomName() + " autorder";
        String contactSalutation = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestEmailAddress = randomEmail();
        String guestSalutation = salutationList[0];

        step("Goto home");
        homePage.gotoHome();

        step("Clear browsers default preference");
        defaultAppsPage.clearBrowserDefaultPreferenceOnLambdaTest();

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Get api access token");
        Unm<?> unm = new Unm<>(session);
        String apiAccessToken = unm.getAccessToken(username, password, "B2C");

        step("check order present in order");
        MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrder(apiAccessToken);
        String bookedOrderID = "";
        activeOrderData = activeOrderData.filterByCurrency(currency);
        if (activeOrderData.isEmpty || activeOrderData.ttd.isEmpty()) {
            step("Create new TTD order");
            bookedOrderID = ttdApi.createIssuedOrderApi(username, password, currency, productUrlName, refundable,
                    contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, cardNumber,
                    expiry, cvv, cardName);
            Assertion.assertThat("TTD Booking via API is successful", bookedOrderID, not(blankOrNullString()));
        } else {
            bookedOrderID = activeOrderData.ttd.get(0).orderId;
        }

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open the order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);

        step("Click eTiket button");
        orderDetailPage.clickEticketFromHeader();

        step("Verify eVoucher contains " + currency + " currency");
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("currency on the E-Voucher is adjusted based on the selected currency", actualPdfContent, containsString(currency));
    }

    @Author(names = {"Zia"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testVerifyReceiptNonIdrLogin() {
        Map<String, String> param = getParam("testVerifyReceiptNonIdrLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        String product = param.get("product");
        int ticketQty = Integer.parseInt(param.get("ticket_qty"));
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Change currency to " + currency);
        homePage.changeCurrency(currency, username);

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", "", currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicketRandom();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();

        step("Input and submit CC information");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Submit the payment method");
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();

        step("Assert if the payment is a success");
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open the order list page");
        paymentPage.seeOrderList();

        step("Open the order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        if (Configs.PLATFORM == Platform.ANDROID) {
            step("Check receipt");
            orderDetailPage.openReceipt();
            String actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Receipt contains " + currency, actualPdfContent, containsString(currency));
            Assertion.assertThat("Receipt not contains IDR", actualPdfContent, not(containsString("IDR")));
            orderDetailPage.clickBackFromPdf();
        }
    }

    @Author(names = {"Yazid"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testBookingSeatMapProductNonIdrLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testBookingSeatMapProductNonIdrLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String product = param.get("product");
        String packageName = param.get("packageName");
        int ticketQty = Integer.parseInt(param.get("ticket_qty"));
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("User choose currency as non-IDR: " + currency);
        homePage.changeCurrency(currency, username);

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", "", currency);

        step("User select a Seatmap product: " + product);
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket: " + packageName + " package");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Set total ticket: " + ticketQty);
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Continue to select seat randomly");
        ttdBookingFormPageWeb.continueToPaymentPage();
        String seatNumber = ttdBookingFormPageWeb.selectSeat("random");

        step("Submit booking");
        ttdBookingFormPageWeb.clickContinuePaymentFooterButton();
        ttdBookingFormPageWeb.checkPriceNotifChange();
        webViewPage.switchToNative();

        step("Redirected to the payment page");
        String orderID = paymentPage.checkOrderID();

        step("Input and submit CC information");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Submit the payment method");
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();
        Assertion.assertThat("User will able to book seat map product", paymentPage.isThankYouPageShown(), is(true));

        step("Open the order list page");
        paymentPage.seeOrderList();

        step("Open the order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Open e-ticket file");
        orderDetailPage.clickEticketFromHeader();
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("will get the e-voucher with the selected seat on it", actualPdfContent, containsString(seatNumber));
    }

    @Author(names = {"Zia"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testVerifyNumericSeparatorPDPNonIdrLogin() {
        Map<String, String> param = getParam("testVerifyNumericSeparatorPDPNonIdrLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        String product = param.get("product");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency to " + currency);
        homePage.changeCurrency(currency, username);

        step("Get Language App to IDN");
        String lang = homePage.getLanguageApp();
        if (!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.openAccount();
            accountPage.selectLanguage("IDN");
            settingPage.backToHome();
        }

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", "id", currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Verify price PDP IDN");
        webViewPage.switchToWebViewUrl("to-do");
        String totalPrice = ttdProductDetailPageWeb.getFooterFinalPriceText();
        Assertion.assertThat("Verify total price matches separator type IDN", CoreFunctions.verifySeparatorRegex(Collections.singletonList(totalPrice), "IDN", currency, 2), is(true));

        List<String> packagePrice = ttdProductDetailPageWeb.getPackagesListPrice();
        Assertion.assertThat("Verify package price matches separator type IDN", CoreFunctions.verifySeparatorRegex(packagePrice, "IDN", currency, 2), is(true));
        webViewPage.switchToNative();

        step("Go to home page");
        homePage.goToHomeDeeplink();

        step("Get Language App to ENG");
        homePage.openAccount();
        accountPage.selectLanguage("ENG");
        settingPage.backToHome();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", "en", currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Verify price PDP ENG");
        webViewPage.switchToWebViewUrl("to-do");
        totalPrice = ttdProductDetailPageWeb.getFinalPrice(1);
        Assertion.assertThat("Verify total price matches separator type type ENG", CoreFunctions.verifySeparatorRegex(Collections.singletonList(totalPrice), "ENG", currency, 2), is(true));

        packagePrice = ttdProductDetailPageWeb.getPackagesListPrice();
        Assertion.assertThat("Verify package price matches separator type ENG", CoreFunctions.verifySeparatorRegex(packagePrice, "ENG", currency, 2), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Banner")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkLandingPageModuleNonIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkLandingPageModuleNonIDR");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to " + currency);
        homePage.changeCurrency(currency, username);

        step("Open TTD landing page from Vertical icon");
        homePage.openVerticalPageDeeplink("To Do", "id", currency);
        Assertion.assertThat("Check if the currency is displayed correct on the landing page", ttdLandingPage.verifyDisplayedCurrency(currency), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "BookingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void verifyBookingPagePriceDetailDiscount() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyBookingPagePriceDetailDiscount");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String tier = map.get("tier");
        String product = map.get("product");
        String packageName = map.get("packageName");
        int ticketQty = Integer.parseInt(map.get("ticketQty"));
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");
        String productCategory = map.get("productCategory");
        int totalSwipe = Integer.parseInt(map.get("totalSwipe"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to " + currency);
        homePage.changeCurrency(currency, username);

        step("Open TTD landing page from Vertical icon");
        if (Configs.ENVIRONMENT == Environment.PROD) homePage.openVerticalPage("To Do");
        else homePage.openVerticalPageDeeplink("To Do", "", currency);

        step("Open category '"+productCategory+"'");
        ttdLandingPage.openProductByCategory(productCategory);

        step("check loyalty benefit on SRP");
        Assertion.assertThat("Loyalty section on SRP", ttdSearchResultPage.verifyDisplayedLoyaltyBanner(), is(true));
        Assertion.assertThat("Loyalty icon or campaign label on SRP Card", ttdSearchResultPage.verifyLoyaltyIconOrCampaignOnSRPCard(tier), is(true));

        step("check currency is non IDR on SRP");
        Assertion.assertThat("Correct currency on SRP", ttdSearchResultPage.verifyDisplayedCurrencyProductCard(currency), is(true));

        step("choose product");
        ttdSearchResultPage.chooseProduct(product, totalSwipe);

        step("check loyalty benefit on PDP");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.findPackage(packageName);
        Assertion.assertThat("Loyalty section on PDP", ttdProductDetailPageWeb.verifyLoyaltyPackageExists(), is(true));

        step("check currency is non IDR on PDP");
        Assertion.assertThat("Correct currency on PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "package list"), is(true));

        step("check loyalty benefit on bottomsheet detail");
        ttdProductDetailPageWeb.clickDetailButton(packageName);
        Assertion.assertThat("Loyalty or campaign label on package detail", ttdProductDetailPageWeb.verifyLoyaltyOrCampaignInPackageDetails(tier), is(true));

        step("check non IDR format on PDP");
        Assertion.assertThat("PDP Currency is correct", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "package detail"), is(true));

        step("book package");
        ttdProductDetailPageWeb.clickBookButtonFooter();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("open price detail and get discount price");
        String totalPriceBookingForm = ttdBookingFormPageWeb.getPrice();
        String bookingPagePriceDetail = ttdBookingFormPageWeb.getDiscountPrice();
        Assertion.assertThat("Discounted Price is correct", CoreFunctions.convertPrice(bookingPagePriceDetail), is(not(0.0)));

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("Open payment method");
        String orderID = paymentPage.checkOrderID();
        String totalPricePaymentPage = paymentPage.getTotalPricePayment();
        String paymentPagePriceDetail = paymentPage.getDiscountPrice();
        paymentPage.closePriceDetail();
        Assertion.assertThat("Discounted Price is correct", CoreFunctions.convertPrice(paymentPagePriceDetail), is(not(0.0)));
        Assertion.assertThat("Total Price is the same", totalPricePaymentPage, is(totalPriceBookingForm));

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select Credit Card as Payment Method");
        paymentPage.selectPaymentMethod("Credit Card");

        step("Fill Credit Card Details");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Go to the order detail page");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Check the Order Passes content without QR Code");
        orderDetailPage.openReceipt();
        String receiptContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Total price is listed in receipt", receiptContent, containsString(totalPricePaymentPage));
    }

    @Author(names = {"Yazid"})
    @Module(name = "TTD")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test
    public void TTD_completeBookingFlowNonIDRLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("TTD_completeBookingFlowNonIDRLogin");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");
        String packageName = map.get("packageName");
        int ticketQty = Integer.parseInt(map.get("ticketQty"));
        String countryName = map.get("countryName");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Choose country");
        accountPage.chooseCountry(countryName);

        step("Set currency to non IDR: " + currency);
        homePage.changeCurrency(currency, username);
        step("Expected currency: " + currency);
        homePage.openVerticalPageDeeplink("To Do", language, currency);
        Assertion.assertThat("Price currency on the landing page should be " + currency, ttdLandingPage.verifyDisplayedCurrency(currency), is(true));
        step("Landing page currency verified: " + currency);

        step("Search and choose activity");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);
        ttdSearchPage.selectRecommendedActivity(product);

        step("Choose package and set ticket quantity");
        webViewPage.switchToWebViewUrl("to-do");
        String totalPriceProductDetailPage = ttdProductDetailPageWeb.getFooterFinalPriceText();
        step("PDP footer price: " + totalPriceProductDetailPage);
        Assertion.assertThat("Price currency on the product detail page should be " + currency, totalPriceProductDetailPage.toLowerCase(), containsString(currency.toLowerCase()));
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(packageName);
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Fill booking form and go to payment page");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();
        Assertion.assertThat("Price currency on the booking form page should be " + currency, ttdBookingFormPageWeb.verifyDisplayedCurrency(currency, "booking page"), is(true));
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("Pay with credit card and capture order ID before payment");
        String orderIdBeforePayment = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open order detail and validate order ID after payment");
        orderListPage.openActiveTicketViaDeeplink(orderIdBeforePayment);
        String orderIdAfterPayment = orderDetailPage.getOrderID();
        Assertion.assertThat("Order ID before payment and after payment should be equal", orderIdAfterPayment, is(orderIdBeforePayment));
    }

    @Author(names = {"Prerna", "Aditi", "Mridul Gupta"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkPriceRoundingAboveTenNonIDRGuest() throws ExecutionException, InterruptedException {
        Map<String, String> map = getParam("checkPriceRoundingAboveTenNonIDRGuest");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String productCount = map.get("productCount");
        String productCategory = map.get("productCategory");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Change currency to '" + currency + "'");
        homePage.changeCurrency(currency, "");

        step("Open TTD Landing Page");
        homePage.goToHomeDeeplink();
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Search the target product that are having more than 10");
        List<GETLandingPageForYouSectionPOJO.Data> getLandingPageForYouSectionPOJO = ttdPage.getAllLandingPageForYouSectionContent(language, currency);
        HashMap<String, String> productWithPriceAboveOrEqualToTen = ttdPage.getProductByPriceDecimalForYouSection(getLandingPageForYouSectionPOJO, ">=", 10);

        step("Go to the target product card");
        String productName = productWithPriceAboveOrEqualToTen.get("productName");
        String productPrice = productWithPriceAboveOrEqualToTen.get("finalPrice");
        Assertion.assertThat("Product with price above or equal to 10 is found "+productName+" actual Price "+productPrice, ttdLandingPage.getUIPrice(productName, currency), containsString(roundOffPrice(productPrice)));
    }

    @Author(names = {"Kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "MultiCurrency")
    @Vertical(name = "TTD")
    @Test
    public void verifyTTDOrderCurrencyNonIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyTTDOrderCurrencyNonIDR");
        BaseTest.paramsThreadLocal.set(param);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDApi<?> ttdApi = new TTDApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String productUrlName = param.get("productUrlName");
        boolean refundable = false;
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String contactFullName = randomName() + " autorder";
        String contactSalutation = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestEmailAddress = randomEmail();
        String guestSalutation = salutationList[0];

        step("Navigate to the home page");
        homePage.gotoHome();

        step("Log out from the current session");
        loginPage.loginAsGuest();

        step("input login credentials");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency(currency, username);

        step("Create a new TTD order");
        String bookedOrderID = ttdApi.createIssuedOrderApi(username, password, currency, productUrlName, refundable,
                contactFullName, contactSalutation, guestFullName, guestEmailAddress, guestSalutation, cardNumber,
                expiry, cvv, cardName);
        Assertion.assertThat("TTD Booking via API is successful", bookedOrderID, not(blankOrNullString()));

        step("Navigate to the order page");
        homePage.openMyOrderPage();

        step("Locate the issued order");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);

        step("Verify order currency matches " + currency);
        Assertion.assertThat("The currency on the order matches the selected currency",
                orderDetailPage.checkPriceDetailIsIn(currency), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyGimmickPrice2DigitNonIDRGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        Map<String, String> map = getParam("verifyGimmickPrice2DigitNonIDRGuest");
        int productCount = Integer.parseInt(map.get("productCount"));
        String currency = map.get("currency");
        String productCategory = map.get("productCategory");
        BaseTest.paramsThreadLocal.set(map);

        step("Open app");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();
        String language = homePage.getLanguageApp();

        step("Change currency to '" + currency + "'");
        homePage.changeCurrency(currency, "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        String productCategoryTitle = ttdLandingPage.openProductByCategory(productCategory);

        step("Sort SRP by lowest price");
        ttdSearchResultPage.clickSortByButton("Lowest Price/Harga Terendah");

        step("Get SRP product list from API sorted by lowest price");
        GETProductListTTDSRPPOJO productDatafromAPI = ttdPage.getProductListTTDSRP(productCategoryTitle, "", "lowest price", language, currency);

        step("Assert SRP is displaying correct 2-digit price data and format");
        int currencyDecimalDigit = 2;
        if (Configs.PLATFORM == Platform.IOS)
            Assertion.assertThat("Assert SRP is displaying correct price data and format", ttdSearchResultPage.verifyDisplayedPriceAmountProductCard(currencyDecimalDigit, productCount, productDatafromAPI, currency), is(true));
        else
            Assertion.assertThat("Assert SRP is displaying correct price data and format", ttdSearchResultPage.verifyDisplayedPriceAmountProductCard(currencyDecimalDigit, productCount, productDatafromAPI), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyGimmickPrice0DigitNonIDRGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        Map<String, String> map = getParam("verifyGimmickPrice0DigitNonIDRGuest");
        int productCount = Integer.parseInt(map.get("productCount"));
        String currency = map.get("currency");
        String productCategory = map.get("productCategory");
        BaseTest.paramsThreadLocal.set(map);

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Change currency to '" + currency + "'");
        homePage.changeCurrency(currency, "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        String productCategoryTitle = ttdLandingPage.openProductByCategory(productCategory);

        step("Assert SRP sorted by the lowest price");
        GETProductListTTDSRPPOJO productDatafromAPI = ttdPage.getProductListTTDSRP(productCategoryTitle, "", "", language, currency);

        step("Assert SRP sorted by the lowest price");
        int currencyDecimalDigit = 0;
        if (Configs.PLATFORM == Platform.IOS)
            Assertion.assertThat("Assert SRP is displaying correct price data and format", ttdSearchResultPage.verifyDisplayedPriceAmountProductCard(currencyDecimalDigit, productCount, productDatafromAPI, currency), is(true));
        else
            Assertion.assertThat("Assert SRP is displaying correct price data and format", ttdSearchResultPage.verifyDisplayedPriceAmountProductCard(currencyDecimalDigit, productCount, productDatafromAPI), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkCurrencyCampaignPDP() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkCurrencyCampaignPDP");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to " + currency);
        homePage.changeCurrency(currency, username);
        String lang = homePage.getLanguageApp();

        step("Open TTD landing page from Vertical icon");
        homePage.openVerticalPageDeeplink("To Do",lang, currency);

        step("Open product with discount");
        if (product == null || product.isEmpty()) {
            ttdLandingPage.openProductWithDiscount();
        } else {
            step("Search the product");
            ttdLandingPage.clickSearchBar();
            ttdSearchPage.inputSearchKeyword(product);

            step("select the activity");
            ttdSearchPage.selectRecommendedActivity(product);
        }

        step("Verify footer have discount campaign");
        webViewPage.switchToWebViewUrl("to-do");
        String crossedPrice = ttdProductDetailPageWeb.getCrossPrice(1);
        String finalPrice = ttdProductDetailPageWeb.getFinalPrice(1);
        List<String> prices = new ArrayList<>();
        prices.add(crossedPrice);
        prices.add(finalPrice);
        Assertion.assertThat("Verify footer price matches separator type " + lang, CoreFunctions.verifySeparatorRegex(prices, lang, currency, 2), is(true));
        Assertion.assertThat("Cross price is more than final price", CoreFunctions.convertPriceSafely(crossedPrice), greaterThan(CoreFunctions.convertPriceSafely(finalPrice)));
    }

    @Author(names = {"Zia"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkCurrencyLoyaltyPDP() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkCurrencyLoyaltyPDP");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to " + currency);
        homePage.changeCurrency(currency, username);
        String lang = homePage.getLanguageApp();

        step("Open TTD landing page from Vertical icon");
        homePage.openVerticalPageDeeplink("To Do", lang, currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Verify footer have discount campaign");
        webViewPage.switchToWebViewUrl("to-do");
        String crossedPrice = ttdProductDetailPageWeb.getFooterCrossPriceText();
        String finalPrice = ttdProductDetailPageWeb.getFooterFinalPriceText();
        List<String> prices = new ArrayList<>();
        prices.add(crossedPrice);
        prices.add(finalPrice);
        Assertion.assertThat("Verify footer price matches separator type " + lang + " decimal 0", CoreFunctions.verifySeparatorRegex(prices, lang, currency, 0), is(true));
        Assertion.assertThat("Cross price is more than final price", CoreFunctions.convertPriceSafely(crossedPrice), greaterThan(CoreFunctions.convertPriceSafely(finalPrice)));
        ttdProductDetailPageWeb.clickTabSection("Benefit", "Benefits", "left");
        Assertion.assertThat("Verify loyalty section exist ", ttdProductDetailPageWeb.verifyLoyaltySectionExists(), is(true));
    }

    @Author(names = {"Dendi"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void testVerifyEarnPoint() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyEarnPoint");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String product = param.get("product");
        String packageName = param.get("packageName");
        int ticketQty = Integer.parseInt(param.get("ticket_qty"));
        String accountId = param.get("accountId");
        String usernameAdmin = param.get("usernameAdmin");
        String passwordAdmin = param.get("passwordAdmin");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        UlpPage<WebDriver> ulpPage = UlpPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("User input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Change currency to \"" + currency + "\"");
        homePage.changeCurrency(currency, username);

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Get tiket points");
        double tiketPointsOnPDP = ttdBookingFormPageWeb.getEarnedPoints();

        step("Continue to select seat randomly");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();

        step("Input and submit CC information");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");

        step("Submit the payment method");
        paymentPage.clickPayButton();
        paymentPage.seeOrderList();

        step("Check order earn point present in dashboard");
        JSONArray customerCareLoyaltyPointBlibliResponse = ulpPage.getLoyaltyData(accountId, usernameAdmin, passwordAdmin);
        log("TEST : " + customerCareLoyaltyPointBlibliResponse.toString());

        step("Check if order ID have same earn point in either pending or earned data");
        Assertion.assertThat("Check if order ID have same earn point in either pending or earned data", ulpPage.checkIfOrderIdPointEarningExistInDashboard(customerCareLoyaltyPointBlibliResponse, orderID, (int) tiketPointsOnPDP), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkPriceDetailBookingFormNonIdr() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPriceDetailBookingFormNonIdr");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticketQty"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to " + currency);
        homePage.changeCurrency(currency, username);

        step("Open TTD landing page from Vertical icon");
        homePage.openVerticalPageDeeplink("To Do", "", currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicketRandom();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);
        String totalPrice = ttdSelectedPackageDetailPageWeb.getTotalPrice();

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Verify price detail");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("verify total price is correct as selected", ttdBookingFormPageWeb.getPrice(), equalTo(totalPrice));
        Assertion.assertThat("Verify price breakdown", ttdBookingFormPageWeb.priceBreakDown(currency), is(true));
        Assertion.assertThat("is currency correct", ttdBookingFormPageWeb.verifyDisplayedCurrency(currency, "price detail"), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkPDPSellingPriceShowCorrectFormatNonIDR2DigitLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPDPSellingPriceShowCorrectFormatNonIDR2DigitLogin");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to " + currency);
        homePage.changeCurrency(currency, username);
        String lang = homePage.getLanguageApp();

        step("Open TTD landing page from Vertical icon");
        homePage.openVerticalPageDeeplink("To Do", "", currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Verify selling price");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("PDP Currency is correct", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "display price"), is(true));
        List<String> displayedPrice = Collections.singletonList(ttdProductDetailPageWeb.getFooterFinalPriceText());
        Assertion.assertThat("is currency correct", CoreFunctions.verifySeparatorRegex(displayedPrice, lang, currency, 2), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkPDPSellingPriceShowCorrectFormatNonIDR0DigitLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPDPSellingPriceShowCorrectFormatNonIDR0DigitLogin");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);
        String lang = homePage.getLanguageApp();

        step("Set currency to " + currency);
        homePage.changeCurrency(currency, username);

        step("Open TTD landing page from Vertical icon");
        homePage.openVerticalPageDeeplink("To Do", "", currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Verify selling price");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("PDP Currency is correct", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "display price"), is(true));
        List<String> displayedPrice = Collections.singletonList(ttdProductDetailPageWeb.getFooterFinalPriceText());
        Assertion.assertThat("is currency correct", CoreFunctions.verifySeparatorRegex(displayedPrice, lang, currency, 0), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyTiketPointsNotShowingNonIDRLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyTiketPointsNotShowingNonIDRLogin");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");

        BaseTest.paramsThreadLocal.set(map);

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Change currency to 'IDR'");
        homePage.changeCurrency(currency, username);

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Get the product name");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName();

        step("Select ticket");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket();

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(1, 0);

        step("Check tiket points not showing on ticket quantity page");
        boolean isTiketPointsDisplayed1 = ttdSelectedPackageDetailPageWeb.isTiketPointDisplayed();

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Fill booking form");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Check tiket points not showing on booking form page");
        boolean isTiketPointsDisplayed2 = ttdBookingFormPageWeb.isTotalPaymentTiketPointDisplayed();
        ttdBookingFormPageWeb.openPriceDetail();
        boolean isTiketPointsDisplayed3 = ttdBookingFormPageWeb.isPriceDetailTiketPointDisplayed();

        step("Check tiket points not showing on payment page");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();
        boolean isTiketPointsDisplayed4 = paymentPage.isEarnTiketPointsDisplayed();

        step("Assertion step");
        Assertion.assertThat("Tiket points not showed on ticket quantity page", isTiketPointsDisplayed1, is(false));
        Assertion.assertThat("Tiket points not showed on total payment booking form page", isTiketPointsDisplayed2, is(false));
        Assertion.assertThat("Tiket points not showed on price detail booking form page", isTiketPointsDisplayed3, is(false));
        Assertion.assertThat("Tiket points not showed on payment page", isTiketPointsDisplayed4, is(false));
    }

    @Author(names = {"kartik"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void verifyPriceTierFormatNonIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyPriceTierFormatNonIDR");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");
        int ticket_qty = Integer.parseInt(map.get("ticket_qty"));
        int guestTierIndex = Integer.parseInt(map.get("guestTierIndex"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to " + currency);
        homePage.changeCurrency(currency, username);
        String lang = homePage.getLanguageApp();

        step("Open TTD landing page from Vertical icon");
        homePage.openVerticalPageDeeplink("To Do", lang, currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(2);

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticket_qty, guestTierIndex);
        Map<String, List<String>> guestTypePriceMap = ttdSelectedPackageDetailPageWeb.getGuestTypePriceMap();

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Open booking form price detail");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.openPriceDetail();
        Map<String, List<String>> tierPriceMap = ttdBookingFormPageWeb.guestTypePriceTierMap();

        step("Verify price tier format");
        Assertion.assertThat("Verify price tier format", ttdBookingFormPageWeb.compareMaps(tierPriceMap, guestTypePriceMap), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkCurrencyPDPNonIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkCurrencyPDPNonIDR");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to " + currency);
        homePage.changeCurrency(currency, username);
        String lang = homePage.getLanguageApp();

        step("Open TTD landing page from Vertical icon");
        homePage.openVerticalPageDeeplink("To Do", lang, currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Check TTD PDP currency");
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Verify price text of display price in PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "display price"), is(true));
        Assertion.assertThat("Verify price text of package list in PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "package list"), is(true));

        step("choose ticket");
        ttdProductDetailPageWeb.goToPackagesList();

        ttdProductDetailPageWeb.clickDetailButton(1);
        Assertion.assertThat("Verify price text in package detail in PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "package detail"), is(true));
        ttdProductDetailPageWeb.closeSection();

        step("Assert price tier format");
        ttdProductDetailPageWeb.chooseTicket(1);
        Assertion.assertThat("Verify price text of selected product card for each tier ticket and total price", ttdSelectedPackageDetailPageWeb.verifyDisplayedCurrency(currency, "price tier"), is(true));
        Assertion.assertThat("Verify price text of pdp recommendation product cards in PDP", ttdProductDetailPageWeb.verifyDisplayedCurrency(currency, "pdp recommendation"), is(true));
    }


    @Author(names = {"Kartik"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void testEnableLocationLandingPage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        Map<String, String> map = getParam("testEnableLocationLandingPage");
        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Get the Explore Text before location is active");
        String beforeLocationActive = ttdLandingPage.getExploreText();

        step("Allow location from the TTD Landing Page");
        ttdLandingPage.clickEnableLocation();

        step("Get the Explore Text after location is active");
        ttdSearchResultPage.clickBack();
        String afterLocationActive = ttdLandingPage.getExploreText();

        Assertion.assertThat("Explore text after location turn on is '" + afterLocationActive + "' not See all activities nearby", afterLocationActive, not(beforeLocationActive));
    }

    @Author(names = {"Akbar"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void removeRecentlyViewedLogin() {
        Map<String, String> map = getParam("removeRecentlyViewedLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String slugProductName1 = map.get("slugProductName1");
        String slugProductName2 = map.get("slugProductName2");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Add 1 product to recent view by opening the PDP via deeplink");
        ttdSearchResultPage.openPdpDeeplink(slugProductName1);
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.getProductName(slugProductName1);
        webViewPage.switchToNative();
        ttdSearchResultPage.openPdpDeeplink(slugProductName2);
        webViewPage.switchToWebViewUrl("to-do");
        String expectedProductName = ttdProductDetailPageWeb.getProductName(slugProductName2);
        webViewPage.switchToNative();
        homePage.goToHomeDeeplink();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");
        Assertion.assertThat("Recently viewed section should appear", ttdLandingPage.isRecentlyViewedDisplayed(), is(true));

        step("Check if the product is in the recently viewed section");
        Assertion.assertThat("First recently viewed product is correct", ttdLandingPage.getFirstProductNameRecentlyViewed().toLowerCase(), is(expectedProductName.toLowerCase()));

        step("Click x button on the product card in recently viewed section");
        ttdLandingPage.removeFirstRecentlyViewedProduct();

        step("Verify step");
        Assertion.assertThat("Product name correctly removed", ttdLandingPage.getFirstProductNameRecentlyViewed().toLowerCase(), not(expectedProductName.toLowerCase()));
    }

    @Author(names = {"kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void testSelectSeatMapLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testSelectSeatMapLogin");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");
        String packageName = map.get("packageName");
        int ticket_qty = Integer.parseInt(map.get("ticket_qty"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Set currency to " + currency);
        homePage.changeCurrency(currency, username);
        String lang = homePage.getLanguageApp();

        step("Open TTD landing page from Vertical icon");
        homePage.openVerticalPageDeeplink("To Do", lang, currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.clickMorePackages();
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticket_qty);
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Submit package detail");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("select seat map");
        ttdBookingFormPageWeb.continueToPaymentPage();
        String seatNumber = ttdBookingFormPageWeb.selectSeat("random");
        ttdBookingFormPageWeb.clickContinuePaymentFooterButton();
        webViewPage.switchToNative();

        step("Submit Sakuku as payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");
        paymentPage.clickPayButton();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Check seat details in order details");
        orderListPage.openActiveTicketViaDeeplink(orderID);

        step("Open e-ticket file");
        orderDetailPage.clickEticketFromHeader();
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("will get the e-voucher with the selected seat on it", actualPdfContent, containsString(String.valueOf(seatNumber)));
    }

    @Author(names = {"kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void testSelectSeatMapTimeSlotLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testSelectSeatMapTimeSlotLogin");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");
        int ticket_qty = Integer.parseInt(map.get("ticket_qty"));
        String packageName = map.get("packageName");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to " + currency);
        homePage.changeCurrency(currency, username);
        String lang = homePage.getLanguageApp();

        step("Open TTD landing page from Vertical icon");
        homePage.openVerticalPageDeeplink("To Do", lang, currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Set total ticket");
        String timeSlot = ttdSelectedPackageDetailPageWeb.chooseTimeSlot();
        Assertion.assertThat("A time slot must be selected for the e-voucher.", timeSlot.isEmpty(), is(false));
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticket_qty);
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Submit package detail");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("select seat map");
        ttdBookingFormPageWeb.continueToPaymentPage();
        String seatNumber = ttdBookingFormPageWeb.selectSeat("random");
        ttdBookingFormPageWeb.clickContinuePaymentFooterButton();
        webViewPage.switchToNative();

        step("Input and submit CC information");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Credit Card");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Submit the payment method");
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Check seat details in order details");
        orderListPage.openActiveTicketViaDeeplink(orderID);

        step("Open e-ticket file");
        orderDetailPage.clickEticketFromHeader();
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("The e-voucher should contain the selected seat number: " + seatNumber, actualPdfContent, containsString(seatNumber));
        Assertion.assertThat("The e-voucher should contain the timeslot: " , actualPdfContent, containsString("Time slot"));
    }

    @Author(names = {"Mridul"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkSeatMapManualSeatingGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkSeatMapManualSeatingGuest");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String product = map.get("product");
        String packageName = map.get("packageName");
        int ticketQty = Integer.parseInt(map.get("ticket_qty"));
        String paymentMethod = map.get("paymentMethod");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();
        homePage.changeCurrency(currency, "");
        String language = homePage.getLanguageApp();

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Search and select product: " + product);
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select package: " + packageName);
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.clickBuyTicket();
        ttdProductDetailPageWeb.chooseTicket(packageName);

        step("Set total ticket and book");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty);
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(false);
        ttdBookingFormPageWeb.fillGuestDetail();
        ttdBookingFormPageWeb.continueToPaymentPage();

        Assertion.assertThat("Should be redirected to webview select seat page", ttdBookingFormPageWeb.isSelectSeatPageDisplayed(), is(true));

        step("Select seat and pay until issued");
        String seatNumber = ttdBookingFormPageWeb.selectSeat("random");
        ttdBookingFormPageWeb.clickContinuePaymentFooterButton();
        webViewPage.switchToNative();

        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickPayButton();
        Assertion.assertThat("Succeed to book seating package", paymentPage.isThankYouPageShown(), is(true));

        step("Open e-ticket and verify seat number");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        orderDetailPage.clickEticketFromHeader();
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("e-voucher should consist of seats number", actualPdfContent, containsString(seatNumber));
    }

    @Author(names = {"Akbar"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkFloatingButtonAndScrollBehaviorTTDLandingPageLogin() {
        Map<String, String> map = getParam("checkFloatingButtonAndScrollBehaviorTTDLandingPageLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Scroll until SRP");
        ttdLandingPage.scrollUntilSRP();
        Assertion.assertThat("Scrolled down until SRP", ttdSearchResultPage.isSearchResultPageShown(), is(true));

        step("Go To Home");
        ttdSearchResultPage.clickBack();
        ttdLandingPage.backToHomePage();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Scroll down until floating button is shown");
        Assertion.assertThat("Scroll down until floating button shown", ttdLandingPage.scrollUpUntilFloatingButtonShown(), is(true));
        ttdLandingPage.clickFloatingButton();
        Assertion.assertThat("SRP is shown after clicked floating button", ttdSearchResultPage.isSearchResultPageShown(), is(true));
    }

    @SneakyThrows
    @Author(names = {"kartik", "Aditi"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void testSelectSeatMapAutomaticLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testSelectSeatMapAutomaticLogin");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");
        int ticket_qty = Integer.parseInt(map.get("ticket_qty"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to " + currency);
        homePage.changeCurrency(currency, username);
        String lang = homePage.getLanguageApp();

        step("Open TTD landing page from Vertical icon");
        homePage.openVerticalPageDeeplink("To Do", lang, currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(5);

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticket_qty);
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Submit package detail");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("select seat map");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("make payment");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Check seat details in order details");
        String seatNumber = ttdPage.getSeatNoFromApiResponse(orderID);
        orderListPage.openActiveTicketViaDeeplink(orderID);

        step("Open e-ticket file");
        orderDetailPage.clickEticketFromHeader();
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("will get the e-voucher with the selected seat on it", actualPdfContent, containsString(String.valueOf(seatNumber)));
    }

    @Author(names = {"Akbar", "Aditi"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkGroupIconTTDLandingPage() {
        Map<String, String> map = getParam("checkGroupIconTTDLandingPage");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Make user as login");
        loginPage.loginWith(loginType, username, password);

        step("Open TTD Landing Page");
        homePage.changeCurrency("IDR", username);
        String language = homePage.getLanguageApp();
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Get group menu data");
        GETLandingPageGroupPOJO getLandingPageGroupPOJO = ttdPage.getLandingPageGroupIconData(language);
        List<Integer> priorityList = getLandingPageGroupPOJO.data.page.menuGroups.stream().map((e) -> e.priority).toList();
        String highestPriorityGroupMenuName = getLandingPageGroupPOJO.data.page.menuGroups.get(findIndexOfHighestInteger(priorityList)).translations.get(0).name;
        highestPriorityGroupMenuName = highestPriorityGroupMenuName.toLowerCase();
        Assertion.assertThat("Highest priority group should be on the left", ttdLandingPage.getFirstGroupMenuChipText(), containsString(highestPriorityGroupMenuName));

        step("Open activity group menu");
        ttdLandingPage.openGroupMenus("activities");

        step("Click random activity menu");
        List<GETLandingPageGroupPOJO.Menu> listOfActivitiesGroupMenu = getLandingPageGroupPOJO.data.page.menuGroups.get(0).menus;
        int randomNumber1 = CoreFunctions.generateRandomInt(1, listOfActivitiesGroupMenu.size());
        ttdLandingPage.clickMenuByName(listOfActivitiesGroupMenu.get(randomNumber1-1).translations.get(0).name);

        step("Verify clicked menu icon on SRP");
        Assertion.assertThat("The SRP already aligned with the menu settings", ttdLandingPage.verifyClickedMenuIcon("activity", listOfActivitiesGroupMenu.get(randomNumber1-1), language), is(true));
        ttdSearchResultPage.clickBack();

        step("Click 'See All Activities' menu icon");
        ttdLandingPage.clickMenuByIndex(listOfActivitiesGroupMenu.size()+1);
        Assertion.assertThat("The bottomsheet of category and sub-category already correct", ttdLandingPage.verifySeeAllGroupMenu("category"), is(true));
        ttdLandingPage.clickBottomsheetCrossButton();

        step("Open destination group menu");
        ttdLandingPage.openGroupMenus("destination");

        step("Click random destination menu");
        List<GETLandingPageGroupPOJO.Menu> listOfDestinationGroupMenu = getLandingPageGroupPOJO.data.page.menuGroups.get(1).menus;
        int randomNumber2 = CoreFunctions.generateRandomInt(1, listOfDestinationGroupMenu.size());
        ttdLandingPage.clickDestinationMenuByName(listOfDestinationGroupMenu.get(randomNumber2-1).translations.get(0).name);

        step("Verify clicked menu icon on destination page/SRP and go back to landing page");
        Assertion.assertThat("UI changes after selecting the destination", ttdLandingPage.checkDestinationUpdated(), is(true));
        Assertion.assertThat("TTD Landing page is shown", ttdLandingPage.isLandingPageValid(), is(false));

    }


    @Author(names = {"Prerna"})
    @Module(name = "Filter")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkSortByHighestPrice() {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        Map<String, String> map = getParam("checkSortByHighestPrice");
        int totalActivity = Integer.parseInt(map.get("totalActivity"));
        String productCategory = map.get("productCategory");
        BaseTest.paramsThreadLocal.set(map);

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("change currency");
        homePage.changeCurrency("IDR", "");

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Get SRP Data from API");
        GETProductListTTDSRPPOJO productDatafromAPI = ttdPage.getProductListTTDSRP(productCategory, "", "highest price", language, "IDR");

        step("Click sort button on search result page");
        ttdSearchResultPage.clickSortByButton("Highest Price/Harga Tertinggi");

        step("Assert SRP sorted by the highest price");
        int currencyDecimalDigit = 0;
        Assertion.assertThat("Assert SRP is displaying correct price data and format", ttdSearchResultPage.verifyDisplayedPriceAmountProductCard(currencyDecimalDigit, totalActivity, productDatafromAPI), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "Filter")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkSortByHighestPriceLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);

        Map<String, String> map = getParam("checkSortByHighestPriceLogin");
        int totalActivity = Integer.parseInt(map.get("totalActivity"));
        String productCategory = map.get("productCategory");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        BaseTest.paramsThreadLocal.set(map);

        step("Open app");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("change currency");
        homePage.changeCurrency("IDR", username);

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Get SRP Data from API");
        GETProductListTTDSRPPOJO productDatafromAPI = ttdPage.getProductListTTDSRP(productCategory, "", "highest price", language, "IDR");

        step("Click sort button on search result page");
        ttdSearchResultPage.clickSortByButton("Highest Price/Harga Tertinggi");

        step("Assert SRP sorted by the highest price");
        int currencyDecimalDigit = 0;
        Assertion.assertThat("Assert SRP is displaying correct price data and format", ttdSearchResultPage.verifyDisplayedPriceAmountProductCard(currencyDecimalDigit, totalActivity, productDatafromAPI), is(true));
    }

    @Author(names = {"kartik"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkLandingPagePromoSectionGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkLandingPagePromoSectionGuest");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TTDPartnerPage<WebDriver> ttdPartnerPage = TTDPartnerPage.getInstance(session);

        String currency = map.get("currency");
        String product = map.get("product");

        step("Go to home page");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();
        String language = homePage.getLanguageApp();

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);
        Assertion.assertThat("Promo section should be displayed on the TTD landing page", ttdLandingPage.verifyPromoSection(), is(true));

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Promo section should be displayed on the TTD PDP", ttdProductDetailPageWeb.verifyPromoBenefitsSection(), is(true));

        step("Open the TTD Partner Page");
        ttdProductDetailPageWeb.clickProductPartner();
        webViewPage.switchToNative();
        ttdPartnerPage.isPageModuleDisplayed();

        step("Verify promo section on partner page");
        Assertion.assertThat("Promo section showing on partner page", ttdPartnerPage.isPromoSectionDisplayed(), is(true));
    }

    @Author(names = {"kartik"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkLandingPagePromoWithTransactionHistory() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkLandingPagePromoWithTransactionHistory");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user Login with "+ username);
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");
        Assertion.assertThat("Promo section should not  be displayed on the TTD landing page With Transaction History User", ttdLandingPage.verifyPromoSection(), is(false));
    }

    @Author(names = {"Kartik"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkPromoSectionPdpGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPromoSectionPdpGuest");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String currency = map.get("currency");
        String product = map.get("product");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("Get App language");
        String lang = homePage.getLanguageApp();

        step("Open TTD landing page from Vertical icon");
        homePage.openVerticalPageDeeplink("To Do", lang, currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Promo section should be displayed on the TTD PDP", ttdProductDetailPageWeb.verifyPromoBenefitsSection(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkPromoOnPDPWithTransactionHistory() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPromoOnPDPWithTransactionHistory");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String product = map.get("product");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Open app");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);
        webViewPage.switchToWebViewUrl("to-do");
        Assertion.assertThat("Promo section should be displayed on the TTD PDP  with transaction history User", ttdProductDetailPageWeb.verifyPromoBenefitsSection(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkPromoOnPDPNoTransactionHistory() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPromoOnPDPNoTransactionHistory");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String product = map.get("product");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Open app");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);
        webViewPage.switchToNative();
        Assertion.assertThat("Promo section should be displayed on the TTD PDP with no transaction history User ", ttdProductDetailPageWeb.verifyPromoBenefitsSection(), is(true));
    }

    @Author(names = {"kartik"})
    @Module(name = "LandingPage")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkLandingPagePromoNoTransactionHistory() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkLandingPagePromoNoTransactionHistory");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user Login with "+ username);
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");
        Assertion.assertThat("Promo section should be displayed on the TTD landing page When user has no transaction history", ttdLandingPage.verifyPromoSection(), is(false));
    }

    @Author(names = {"Zia", "Aditi"})
    @Module(name = "AdditionalCase")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyPromoSectionPartnerWithTransactionHistory() {
        Map<String, String> map = getParam("verifyPromoSectionPartnerWithTransactionHistory");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String product = map.get("product");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDPartnerPage<WebDriver> ttdPartnerPage = TTDPartnerPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", "", "");

        step("Click searchbar");
        ttdLandingPage.clickSearchBar();

        step("Input product name " + product);
        ttdSearchPage.inputSearchKeyword(product);
        ttdSearchPage.selectRecommendedActivity(product);
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.clickProductPartner();
        webViewPage.switchToNative();

        step("Verify promo section on partner page");
        Assertion.assertThat("Promo section not showing on partner page", ttdPartnerPage.isPromoSectionDisplayed(), is(false));
    }

    @Author(names = {"Zia", "Aditi"})
    @Module(name = "AdditionalCase")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyPromoSectionPartnerWithNoTransactionHistory() {
        Map<String, String> map = getParam("verifyPromoSectionPartnerWithNoTransactionHistory");
        BaseTest.paramsThreadLocal.set(map);

        String product = map.get("product");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDPartnerPage<WebDriver> ttdPartnerPage = TTDPartnerPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("user login as guest");
        loginPage.loginAsGuest();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", "", "");

        step("Search the product: " + product);
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);
        ttdSearchPage.selectRecommendedActivity(product);

        step("Open the TTD Partner Page");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.clickProductPartner();
        webViewPage.switchToNative();
        ttdPartnerPage.isPageModuleDisplayed();

        step("Verify promo section on partner page");
        Assertion.assertThat("Promo section showing on partner page", ttdPartnerPage.isPromoSectionDisplayed(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "AdditionalCase")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void verifyPromoSectionPartnerWithNoTransactionHistoryLogin() {
        Map<String, String> map = getParam("verifyPromoSectionPartnerWithNoTransactionHistoryLogin");
        BaseTest.paramsThreadLocal.set(map);

        String product = map.get("product");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDPartnerPage<WebDriver> ttdPartnerPage = TTDPartnerPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("user login with account no trx");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", "", "");

        step("Search the product: " + product);
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);
        ttdSearchPage.selectRecommendedActivity(product);

        step("Open the TTD Partner Page");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.clickProductPartner();
        webViewPage.switchToNative();
        ttdPartnerPage.isPageModuleDisplayed();

        step("Verify promo section on partner page");
        Assertion.assertThat("Promo section showing on partner page", ttdPartnerPage.isPromoSectionDisplayed(), is(true));
    }

    @Author(names = {"kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void checkTTDBundlingFunctionality() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkTTDBundlingFunctionality");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticketQty"));
        String currency  = map.get("currency");

        step("Go to home page");
        homePage.gotoHome();

        step("Make user Login with " + username);
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Change currency to \"" + currency + "\"");
        homePage.changeCurrency(currency, username);

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(3);

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        String selectedDate = ttdSelectedPackageDetailPageWeb.getSelectedDate();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Add bundling data from booking form page");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.addBundlingItems();

        step("Completing booking form data");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillBundlingGuestDetail();

        step("Continue to select seat randomly");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();

        step("Make payment");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("ATM");
        paymentPage.clickPayButton();

        step("Go to the order list");
        paymentPage.seeOrderList();

        step("Check the order details");
        orderListPage.openActiveTicketViaDeeplink(orderID);
        orderDetailPage.clickEticketFromHeader();

        Assertion.assertThat("E-ticket should be displayed on the order detail page", orderDetailPage.verifyEticketSectionWebView(), is(true));
        Assertion.assertThat("order details ID component should be visible on order detail page", orderDetailPage.verifyTTDOrderDetailIDComponent(), is(true));
        String orderDetailDate = orderDetailPage.getTTDValidityPeriod();
        Assertion.assertThat("Package detail Data should be aligned with the booking data", orderDetailDate, is(containsString(selectedDate)));
        Assertion.assertThat("Manage Order section should be present", orderDetailPage.verifyManageOrderSection(orderID), is(true));
    }

    @SneakyThrows
    @Author(names = {"Kartik"})
    @Module(name = "CompleteBooking")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test()
    public void completeBookingForMasterDataLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingForMasterDataLogin");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("productName");

        int ticket_qty = Integer.parseInt(map.get("ticket_qty"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Go to home page");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);

        step("Set currency to " + currency);
        homePage.changeCurrency(currency, username);
        String lang = homePage.getLanguageApp();

        step("Open TTD landing page from Vertical icon");
        homePage.openVerticalPageDeeplink("To Do", lang, currency);

        step("Search the master data: "+ product);
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(product);

        step("select the activity");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.chooseTicket(1);

        step("Set total ticket");
        ttdSelectedPackageDetailPageWeb.fillAvailableSection();
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticket_qty);
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Submit package detail");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("select seat map");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("make payment");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("User is on payment page",orderID,is(notNullValue()));
    }

    @Author(names = {"Aditi"})
    @Module(name = "SearchSuggestion")
    @Tribe(name = "Search")
    @Vertical(name = "TTD")
    @Test
    public void checkShareProductButton() throws InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkShareProductButton");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

        String product = map.get("product");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Click Search Bar on TTD Landing Page");
        ttdLandingPage.clickSearchBar();

        step("Do Search on TTD Search Page");
        ttdSearchPage.inputSearchKeyword(product);

        step("Choose One of The Search Result");
        ttdSearchPage.selectRecommendedActivity(product);

        step("Check that the correct product is opened");
        webViewPage.switchToWebViewUrl("to-do");
        String expectedProductName = ttdProductDetailPageWeb.getProductName();
        Assertion.assertThat("Successfully to redirected to the correct PDP", product, is(expectedProductName));

        step("Check that the Share Product Button is present");
        String url = ttdProductDetailPageWeb.checkShareProductButton();

        step("Paste the URL in a new tab");
        boolean result = ttdProductDetailPageWeb.openCopiedLink(url);
        Assertion.assertThat("Selected product PDP is opened on new tab", result, is(true));

    }

    @Author(names = {"Alden"})
    @Module(name = "MyOrder")
    @Tribe(name = "Experience")
    @Vertical(name = "TTD")
    @Test
    public void checkUserRedeemOrReservePasses() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkUserRedeemOrReservePasses");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RedeemReservationActivityPage<WebDriver> redeemReservationActivityPage = RedeemReservationActivityPage.getInstance(session);
        RedeemReservationBookingDetailPage<WebDriver> redeemReservationDetailPage = RedeemReservationBookingDetailPage.getInstance(session);
        RedeemReservationContactDetailPage<WebDriver> redeemReservationContactDetailPage = RedeemReservationContactDetailPage.getInstance(session);
        PassesVoucherPage<WebDriver> passesVoucherPage = PassesVoucherPage.getInstance(session);
        ManagePassesPage<WebDriver> managePassesPage = ManagePassesPage.getInstance(session);


        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String product = map.get("product");
        int ticketQty = Integer.parseInt(map.get("ticketQty"));

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Open PDP page via deeplink");
        ttdSearchResultPage.openPdpDeeplink(product);

        step("Select ticket");
        webViewPage.switchToWebViewUrl("to-do");
        ttdProductDetailPageWeb.goToPackagesList();
        ttdProductDetailPageWeb.selectFirstPackage();
        ttdProductDetailPageWeb.clickBookButtonFooter();

        step("Select ticket quantity");
        ttdSelectedPackageDetailPageWeb.setTicketQty(ticketQty, 0);

        step("Submit package detail");
        ttdSelectedPackageDetailPageWeb.clickBookButton();
        webViewPage.switchToNative();

        step("Completing booking form data");
        webViewPage.switchToWebViewUrl("to-do");
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();

        step("Submit booking");
        ttdBookingFormPageWeb.continueToPaymentPage();
        webViewPage.switchToNative();

        step("Submit the payment method");
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");
        paymentPage.clickPayButton();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Go to the order detail page");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Check the Order Passes content without QR Code");
        orderDetailPage.clickEticketFromHeader();
        Assertion.assertThat("QR Code is not present", orderDetailPage.verifyEticketQRCode(), is(false));

        step("Choose Activity");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        orderDetailPage.clickRedeemButton();
        redeemReservationActivityPage.selectActivity();

        step("fill in activity details");
        redeemReservationDetailPage.completeDetailForm();
        redeemReservationDetailPage.clickReserveButtonFooter();

        step("fill in contact details");
        redeemReservationContactDetailPage.fillGuestDetail();
        redeemReservationContactDetailPage.swipeToReserve();

        step("check for QR code");
        Assertion.assertThat("QR code is shown", passesVoucherPage.checkQrCode(), is(true));
    }

    @Author(names = {"Alden"})
    @Vertical(name = "TTD")
    @Tribe(name = "Experience")
    @Module(name = "SRP")
    @Test
    public void verifyBoostingLabelProductCardSRPLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        Map<String, String> map = getParam("verifyBoostingLabelProductCardSRPLogin");

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String productCategory = map.get("productCategory");

        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Open attractions category");
        ttdLandingPage.openProductByCategory(productCategory);

        step("check the product cards");
        Assertion.assertThat("boosting labels are correct", ttdSearchResultPage.checkBoostingLabels(language), is(true));
    }

    @Author(names = {"Alden"})
    @Vertical(name = "TTD")
    @Tribe(name = "Experience")
    @Module(name = "LandingPage")
    @Test
    public void verifyNewHomeDestinationLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDExplorePage<WebDriver> ttdExplorePage = TTDExplorePage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);


        Map<String, String> map = getParam("verifyNewHomeDestinationLogin");

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String reloadChipText = map.get("reloadChipText");
        String reloadChipSection = map.get("reloadChipSection");


        BaseTest.paramsThreadLocal.set(map);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", language, "IDR");

        step("Check tabbing");
        ttdLandingPage.checkTabbing();

        step("Check for correct location chips");
        Assertion.assertThat("Location chip is showing", ttdLandingPage.verifyLocationChip(), is(true));
        Assertion.assertThat("SRP is showing all locations", ttdLandingPage.checkLocationChip().toLowerCase(), anyOf(is("all locations"), is("semua lokasi")));

        step("Check for new destinations search");
        ttdLandingPage.clickLocationChip();
        Assertion.assertThat("New destinations display is correct", ttdLandingPage.verifySeeAllGroupMenu("destination"), is(true));
        ttdLandingPage.clickBottomsheetCrossButton();

        step("Check for CTAs");
        ttdLandingPage.openSpecialOffers();
        Assertion.assertThat("CTA redirects correctly to search result page", ttdLandingPage.checkCTA(), is(true));
        ttdSearchResultPage.clickBack();

        step("Check for reload button behaviour");
        ttdLandingPage.checkReloadButton(reloadChipText, reloadChipSection);
    }

    @Author(names = {"Mridul"})
    @Module(name = "SRP")
    @Tribe(name = "Search")
    @Vertical(name = "TTD")
    @Test
    public void checkHighlightedFilterChipSRP() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkHighlightedFilterChipSRP");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String productCategory = map.get("productCategory");
        String currency = "IDR";

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open TTD Landing Page");
        homePage.openVerticalPageDeeplink("To Do", "", currency);

        step("Open any product page");
        ttdLandingPage.openProductByCategory(productCategory);

        step("Click Filter Button on SRP");
        ttdSearchResultPage.clickFilter();

        step("Select the Loyalty badge ");
        ttdSearchResultPage.selectGeneralFilter("Benefit");
        ttdSearchResultPage.submitFilter();

        step("Assertions");
        Assertion.assertThat("Is Filter Applied", ttdSearchResultPage.isFilterActivated("Benefit"), is(true));
        Assertion.assertThat("SRP is filtered based on highlighted filter chip", ttdSearchResultPage.isCartContainsLoyaltyBadge(), is(true));
        int apiCount = ttdPage.getProductCount(username,password,true, productCategory, language, currency);
        Assertion.assertThat("SRP product count is equal to API product count", ttdSearchResultPage.getProductCountOnSrp(), is(apiCount));
    }


    @Author(names = {"Harshit"})
    @Module(name = "MultiCurrency")
    @Tribe(name = "TTD")
    @Vertical(name = "TTD")
    @Test()
    public void verifyPackageBelowOneUsdOpensPdp() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyPackageBelowOneUsdOpensPdp");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDSearchResultPage<WebDriver> ttdSearchResultPage = TTDSearchResultPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);

       String loginType = map.get("loginType");
       String username = map.get("username");
       String password = map.get("password");
        String currency = map.get("currency");
        String keyword = map.get("search_input");
        int totalActivity = Integer.parseInt(map.get("totalActivity"));

        step("Go to home page");
        homePage.gotoHome();

       step("Make user Login with " + username);
       loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Change currency to '" + currency + "'");
        homePage.changeCurrency(currency, username);
        homePage.waitForHomePageToLoad();

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", language, currency);

        step("Search for '" + keyword + "' on TTD landing page");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(keyword);
        ttdSearchPage.selectRecommendedDestination(keyword);
        
        Assertion.assertThat("User should be redirected to the SRP for '" + keyword + "'", ttdSearchResultPage.isSearchResultPageShown(), is(true));

        step("Find a package with price less than 1 " + currency);
        String packageBelowOneUsd = ttdSearchResultPage.getProductBelowPrice(1.0, totalActivity);
        Assertion.assertThat("A package with price less than 1 " + currency + " should be available on the SRP", packageBelowOneUsd, is(not(emptyOrNullString())));

        step("Select the package and verify the package detail page is opened");
        ttdSearchResultPage.selectProduct(packageBelowOneUsd);

        webViewPage.switchToWebViewUrl("to-do");

        Assertion.assertThat("Package detail page should be opened successfully", ttdProductDetailPageWeb.getProductName(), is(not(emptyOrNullString())));

        double pdpPrice = convertPriceSafely(ttdProductDetailPageWeb.getFooterFinalPriceText());
        Assertion.assertThat("PDP price should be less than 1 USD", pdpPrice, lessThan(1.0));
        
    }

}