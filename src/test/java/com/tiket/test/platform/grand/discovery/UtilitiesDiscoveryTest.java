package com.tiket.test.platform.grand.discovery;

import com.tiket.apiPage.HotelApi;

import com.costumeMatcher.CaseInsensitiveSubstringMatcher;
import com.google.gson.JsonSyntaxException;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.FlightApi;
import com.tiket.apiPage.OrderApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
import com.tiket.page.account.AccountPage;
import com.tiket.page.account.voucherboxpage.VoucherBoxPage;
import com.tiket.page.carrental.bookingpage.CarRentalBookingPage;
import com.tiket.page.carrental.reviewbookingpage.CarRentalReviewBookingPage;
import com.tiket.page.carrental.searchpage.SearchPageCarRental;
import com.tiket.page.carrental.searchresultpage.SearchResultPageCarRental;
import com.tiket.page.common.calendar.CalendarPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.flight.FlightPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.train.TrainPage;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.page.ttd.TTDPage;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.page.ttd.partnerPage.TTDPartnerPage;
import com.tiket.page.ttd.searchpage.TTDSearchPage;
import com.tiket.page.utilities.discovery.blibli.affiliate.BlibliAffiliatePage;
import com.tiket.page.utilities.discovery.promo.PromoPage;
import com.tiket.page.utilities.message.inbox.InboxPage;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.ttd.TTDPartnerPageModules;
import com.tiket.pojo.utilities.*;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.core.CoreFunctions.getRandomObject;
import static com.tiket.core.CoreFunctions.randomName;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class UtilitiesDiscoveryTest extends BaseTest {

    @Author(names = {"Yazid"})
    @Module(name = "ImageChip")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyImageWithChipModuleNonLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        Map<String, String> param = getParam("testVerifyImageWithChipModuleNonLogin");
        BaseTest.paramsThreadLocal.set(param);
        boolean needLogin = false;
        String username = "";
        String password = "";
        String templateCode = "IMAGE_WITH_CHIPS";

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("user open app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency("IDR","");

        step("Get language");
        String language = homePage.getLanguageApp();

        step("Get the data from the API");
        SectionModulePOJO apiData = (SectionModulePOJO) homePage.getModuleSection(needLogin, username, password, language, templateCode, "ASYNC","IDR");
        String titleSection = apiData.titleSection;
        boolean subTitleSection = !apiData.subTitleSection.equals("");
        String productTypeApi = apiData.chipsItem.get(0).productType;
        boolean titleChipsApi = apiData.chipsItem.get(0).titleChips != null;
        boolean buttonTextApi = apiData.chipsItem.get(0).buttonText != null;

        step("User go to the " + templateCode + " section");
        homePage.swipeToCard(titleSection);
        if (subTitleSection) {
            Assertion.assertThat("Subtitle on the App and API should be equal", homePage.getTheSubtitle(titleSection), is(equalTo(apiData.subTitleSection)));
        }

        step("User swipe the inventory list");
        String firstInventoryName = homePage.getInventoryName(titleSection);
        boolean isSwipe;
        isSwipe = homePage.swipeInventoryList(titleSection);
        Assertion.assertThat("Data should be different on the before and after swipe", homePage.compareBeforeAfterSwipe(titleSection, "inventory", firstInventoryName, isSwipe), is(true));

        step("User select the inventory");
        String currentInventoryName = homePage.selectInventory(titleSection, "random", productTypeApi);
        Assertion.assertThat("Redirection item should be equal selected inventory", currentInventoryName, is(equalTo(homePage.getInventoryRedirection(productTypeApi))));

        String selectedChipsText = "";
        if (titleChipsApi) {
            step("User swipe the chips button");
            isSwipe = homePage.swipeChipsButton(titleSection);
            Assertion.assertThat("Data should be different on the before and after swipe", homePage.compareBeforeAfterSwipe(titleSection, "chips", "", isSwipe), is(true));

            step("User click the chips button");
            selectedChipsText = homePage.selectChipsButton(titleSection, "random");
            Assertion.assertThat("Selected chips text should be there on the API", homePage.checkChipsButtonTextApi(apiData, selectedChipsText), is(true));
        }

        if (buttonTextApi) {
            step("User can see the CTA button");
            String ctaButtonTextApi = homePage.getCtaButtonTextApi(apiData, selectedChipsText);
            Assertion.assertThat("The CTA button text should equal with the API", homePage.getCtaButtonTextApp(titleSection), is(equalTo(ctaButtonTextApi)));

            step("User click the CTA button");
            homePage.clickCtaButton(titleSection);
            Assertion.assertThat("Home page should be disappear", homePage.isOnHomePage(), is(false));
        }
    }

    @Author(names = {"Yazid"})
    @Module(name = "ImageChip")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyImageWithChipModuleLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        Map<String, String> param = getParam("testVerifyImageWithChipModuleLogin");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        boolean needLogin = true;
        String templateCode = "IMAGE_WITH_CHIPS";

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed) and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");
        accountPage.chooseCountry("Indonesia");

        step("Get language");
        String language = homePage.getLanguageApp();

        step("Get the data from the API");
        SectionModulePOJO apiData = (SectionModulePOJO) homePage.getModuleSection(needLogin, username, password, language, templateCode, "ASYNC","IDR");
        String titleSection = apiData.titleSection;
        boolean subTitleSection = !apiData.subTitleSection.equals("");
        String productTypeApi = apiData.chipsItem.get(0).productType;
        boolean titleChipsApi = apiData.chipsItem.get(0).titleChips != null;
        boolean buttonTextApi = apiData.chipsItem.get(0).buttonText != null;

        step("User go to the " + templateCode + " section");
        homePage.swipeToCard(titleSection);
        if (subTitleSection) {
            Assertion.assertThat("Subtitle on the App and API should be equal", homePage.getTheSubtitle(titleSection), is(equalTo(apiData.subTitleSection)));
        }

        step("User swipe the inventory list");
        String firstInventoryName = homePage.getInventoryName(titleSection);
        boolean isSwipe;
        isSwipe = homePage.swipeInventoryList(titleSection);
        Assertion.assertThat("Data should be different on the before and after swipe", homePage.compareBeforeAfterSwipe(titleSection, "inventory", firstInventoryName, isSwipe), is(true));

        step("User select the inventory");
        String currentInventoryName = homePage.selectInventory(titleSection, "random", productTypeApi);
        Assertion.assertThat("Redirection item should be equal selected inventory", currentInventoryName, is(equalTo(homePage.getInventoryRedirection(productTypeApi))));

        String selectedChipsText = "";
        if (titleChipsApi) {
            step("User swipe the chips button");
            isSwipe = homePage.swipeChipsButton(titleSection);
            Assertion.assertThat("Data should be different on the before and after swipe", homePage.compareBeforeAfterSwipe(titleSection, "chips", "", isSwipe), is(true));

            step("User click the chips button");
            selectedChipsText = homePage.selectChipsButton(titleSection, "random");
            Assertion.assertThat("Selected chips text should be there on the API", homePage.checkChipsButtonTextApi(apiData, selectedChipsText), is(true));
        }

        if (buttonTextApi) {
            step("User can see the CTA button");
            String ctaButtonTextApi = homePage.getCtaButtonTextApi(apiData, selectedChipsText);
            Assertion.assertThat("The CTA button text should equal with the API", homePage.getCtaButtonTextApp(titleSection), is(equalTo(ctaButtonTextApi)));

            step("User click the CTA button");
            homePage.clickCtaButton(titleSection);
            Assertion.assertThat("Home page should be disappear", homePage.isOnHomePage(), is(false));
        }
    }

    @Author(names = {"Yazid"})
    @Module(name = "BannerGrid")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyBannerGridModuleNonLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        Map<String, String> param = getParam("testVerifyBannerGridModuleNonLogin");
        BaseTest.paramsThreadLocal.set(param);
        boolean needLogin = false;
        String username = "";
        String password = "";
        String templateCode = "BANNER_GRID";
        String productType = "WEBVIEW";
        String processType = "SYNC";

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("user open app as guest");
        loginPage.loginAsGuest();

        step("Get language");
        String language = homePage.getLanguageApp();

        step("Get the data from the API");
        SectionModulePOJO apiData = (SectionModulePOJO) homePage.getModuleSection(needLogin, username, password, language, templateCode, processType,"IDR");
        String titleSection = apiData.titleSection;
        boolean subTitleSection = !apiData.subTitleSection.equals("");
        boolean titleChipsApi = apiData.chipsItem.get(0).titleChips != null;
        boolean buttonTextApi = apiData.chipsItem.get(0).buttonText != null;

        step("User go to the " + templateCode + " section");
        homePage.swipeToCard(titleSection);
        if (subTitleSection) {
            Assertion.assertThat("Subtitle on the App and API should be equal", homePage.getTheSubtitle(titleSection), is(equalTo(apiData.subTitleSection)));
        }

        step("User select the inventory");
        String indexElement = homePage.selectInventory(titleSection, "random", productType);
        String expectedUrl = apiData.chipsItem.get(0).contentsItem.get(Integer.parseInt(indexElement)).clickUrl;
        Assertion.assertThat("Go to the correct redirection URL", homePage.getAppUrl(), is(containsString(CoreFunctions.removeUrlProtocol(expectedUrl))));
        homePage.clickBackBtn();

        boolean isSwipe;
        String selectedChipsText = "";
        if (titleChipsApi) {
            step("User swipe the chips button");
            isSwipe = homePage.swipeChipsButton(titleSection);
            Assertion.assertThat("Data should be different on the before and after swipe", homePage.compareBeforeAfterSwipe(titleSection, "chips", "", isSwipe), is(true));

            step("User click the chips button");
            selectedChipsText = homePage.selectChipsButton(titleSection, "random");
            Assertion.assertThat("Selected chips text should be there on the API", homePage.checkChipsButtonTextApi(apiData, selectedChipsText), is(true));
        }

        if (buttonTextApi) {
            step("User can see the CTA button");
            String ctaButtonTextApi = homePage.getCtaButtonTextApi(apiData, selectedChipsText);
            Assertion.assertThat("The CTA button text should equal with the API", homePage.getCtaButtonTextApp(titleSection), is(equalTo(ctaButtonTextApi)));

            step("User click the CTA button");
            homePage.clickCtaButton(titleSection);
            Assertion.assertThat("Home page should be disappear", homePage.isOnHomePage(), is(false));
        }
    }

    @Author(names = {"Yazid"})
    @Module(name = "BannerGrid")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyBannerGridModuleLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<?> accountPage = AccountPage.getInstance(session);

        Map<String, String> param = getParam("testVerifyBannerGridModuleLogin");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        boolean needLogin = true;
        String templateCode = "BANNER_GRID";
        String productType = "WEBVIEW";
        String processType = "SYNC";

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");
        accountPage.chooseCountry("Indonesia");

        step("Get language");
        String language = homePage.getLanguageApp();

        step("Get the data from the API");
        SectionModulePOJO apiData = (SectionModulePOJO) homePage.getModuleSection(needLogin, username, password, language, templateCode, processType,"IDR");
        String titleSection = apiData.titleSection;
        boolean subTitleSection = !apiData.subTitleSection.equals("");
        boolean titleChipsApi = apiData.chipsItem.get(0).titleChips != null;
        boolean buttonTextApi = apiData.chipsItem.get(0).buttonText != null;

        step("User go to the " + templateCode + " section");
        homePage.swipeToCard(titleSection);
        if (subTitleSection) {
            Assertion.assertThat("Subtitle on the App and API should be equal", homePage.getTheSubtitle(titleSection), is(equalTo(apiData.subTitleSection)));
        }

        step("User select the inventory");
        String indexElement = homePage.selectInventory(titleSection, "random", productType);
        String expectedUrl = apiData.chipsItem.get(0).contentsItem.get(Integer.parseInt(indexElement)).clickUrl;
        Assertion.assertThat("Go to the correct redirection URL", homePage.getAppUrl(), is(containsString(CoreFunctions.removeUrlProtocol(expectedUrl))));
        homePage.clickBackBtn();

        boolean isSwipe;
        String selectedChipsText = "";
        if (titleChipsApi) {
            step("User swipe the chips button");
            isSwipe = homePage.swipeChipsButton(titleSection);
            Assertion.assertThat("Data should be different on the before and after swipe", homePage.compareBeforeAfterSwipe(titleSection, "chips", "", isSwipe), is(true));

            step("User click the chips button");
            selectedChipsText = homePage.selectChipsButton(titleSection, "random");
            Assertion.assertThat("Selected chips text should be there on the API", homePage.checkChipsButtonTextApi(apiData, selectedChipsText), is(true));
        }

        if (buttonTextApi) {
            step("User can see the CTA button");
            String ctaButtonTextApi = homePage.getCtaButtonTextApi(apiData, selectedChipsText);
            Assertion.assertThat("The CTA button text should equal with the API", homePage.getCtaButtonTextApp(titleSection), is(equalTo(ctaButtonTextApi)));

            step("User click the CTA button");
            homePage.clickCtaButton(titleSection);
            Assertion.assertThat("Home page should be disappear", homePage.isOnHomePage(), is(false));
        }
    }

    @Author(names = {"Yazid"})
    @Module(name = "InfoPages")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyInfoPagesNonLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        Map<String, String> param = getParam("testVerifyInfoPagesNonLogin");
        BaseTest.paramsThreadLocal.set(param);
        boolean needLogin = false;
        String username = "";
        String password = "";

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("user open app as guest");
        loginPage.loginAsGuest();

        step("Get language");
        homePage.changeLanguageUsingDeeplink("IDN");
        String language = homePage.getLanguageApp();

        step("Get the data from the API");
        InfoPagesPOJO apiData = (InfoPagesPOJO) homePage.getModuleSection(needLogin, username, password, language, "INFO_PAGES", "SYNC","IDR");
        String cardType = apiData.titleSection;

        step("User go to the info pages section");
        homePage.swipeToCard(cardType);

        step("User select random item of the info pages");
        String selectedTab = homePage.selectInfoPagesTab("random");
        String expectedUrl = homePage.selectInfoPagesItem(apiData.chipsItem, selectedTab);

        Assertion.assertThat("Info page content is displayed", homePage.isInfoPagesDisplayed(), is(true));
        Assertion.assertThat("Go to the correct redirection URL", homePage.getAppUrl(), is(containsString(CoreFunctions.removeUrlProtocol(expectedUrl))));
    }

    @Author(names = {"Yazid"})
    @Module(name = "InfoPages")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyInfoPagesLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<?> accountPage = AccountPage.getInstance(session);

        Map<String, String> param = getParam("testVerifyInfoPagesLogin");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        boolean needLogin = true;

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        accountPage.chooseCountry("Indonesia");
        homePage.changeCurrency("IDR",username);

        step("Get language");
        String language = homePage.getLanguageApp();

        step("Get the data from the API");
        InfoPagesPOJO apiData = (InfoPagesPOJO) homePage.getModuleSection(needLogin, username, password, language, "INFO_PAGES", "SYNC","IDR");
        String cardType = apiData.titleSection;

        step("User go to the info pages section");
        homePage.swipeToCard(cardType);

        step("User select random item of the info pages");
        String selectedTab = homePage.selectInfoPagesTab("policy");
        String expectedUrl = homePage.selectInfoPagesItem(apiData.chipsItem, selectedTab);

        Assertion.assertThat("Info page content is displayed", homePage.isInfoPagesDisplayed(), is(true));
        Assertion.assertThat("Go to the correct redirection URL", homePage.getAppUrl(), is(containsString(CoreFunctions.removeUrlProtocol(expectedUrl))));
    }

    @Author(names = { "Prerna" })
    @Module(name = "HomeIcon")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyHomeIconLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<?> accountPage = AccountPage.getInstance(session);

        Map<String, String> testProperties = getParam("testVerifyHomeIconLogin");

        BaseTest.paramsThreadLocal.set(testProperties);

        String loginType = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String currency = testProperties.get("currency");

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User login into their account");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.gotoHome();
        homePage.changeCurrency(currency,username);
        accountPage.chooseCountry("Indonesia");

        step("user verify product icon on home page");
        Assertion.assertThat("verify product icon in home page", homePage.verifyIconsProduct(), is(true));
    }

    @Author(names = { "Dendi" })
    @Module(name = "HomeIcon")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyHomeIconNonLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<?> accountPage = AccountPage.getInstance(session);

        Map<String, String> testProperties = getParam("testVerifyHomeIconNonLogin");
        BaseTest.paramsThreadLocal.set(testProperties);

        String currency = testProperties.get("currency");

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("user open app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");
        accountPage.chooseCountry("Indonesia");

        step("user verify product icon on home page");
        Assertion.assertThat("verify product icon in home page", homePage.verifyIconsProduct(), is(true));
    }

    @Author(names = { "Dendi" })
    @Module(name = "FlashSale")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testFlashSaleInventoryLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        Map<String, String> testProperties = getParam("testFlashSaleInventoryLogin");
        String loginType = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(testProperties);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed) and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");
        accountPage.chooseCountry("Indonesia");

        step("Get langguage");
        String lang = homePage.getLanguageApp();

        step("Request data for availaible promo modules through api");
        String flashSaleSectionname = homePage.getHomeModule(username, password, "FLASH_SALE",lang,"IDR");

        step("User search for flash sale in home page");
        homePage.swipeToCard(flashSaleSectionname);

        step("Verify flash sale section");
        Assertion.assertThat("Flash sale count down present", homePage.isFlashSaleCountDownPresent(), is(true));
        Assertion.assertThat("Is price and image of flash sale inventory present",
                homePage.isFlashSaleInventoryPricePresent("IDR"), is(true));
        Assertion.assertThat("Is flash sale inventory swipe able", homePage.isFlashListSaleSwipeAble(flashSaleSectionname), is(true));

        step("User click on card");
        String flashSaleInveentoryName = homePage.clickOneOfFlashSaleInventory();

        step("User redirect to detail page");
        Assertion.assertThat("Is redirected to correct hotel",
                homePage.verifyRedirectFlashSale(flashSaleInveentoryName), is(true));
    }

    @Author(names = { "Dendi" })
    @Module(name = "FlashSale")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testFlashSaleInventoryNonLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        Map<String, String> testProperties = getParam("testFlashSaleInventoryNonLogin");
        String username = testProperties.get("username");
        String password = testProperties.get("password");

        BaseTest.paramsThreadLocal.set(testProperties);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User open app as guest");
        loginPage.loginAsGuest();

        step("Change currency to IDR");
        homePage.changeCurrency("IDR","");

        step("Get langguage");
        String lang = homePage.getLanguageApp();

        step("Request data for availaible promo modules through api");
        String flashSaleSectionname = homePage.getHomeModule(username, password, "FLASH_SALE",lang,"IDR");

        step("User search for flash sale in home page");
        homePage.swipeToCard(flashSaleSectionname);

        step("User verify flash sale section");
        Assertion.assertThat("Flash sale count down present", homePage.isFlashSaleCountDownPresent(), is(true));
        Assertion.assertThat("Is price and image of flash sale inventory present",
                homePage.isFlashSaleInventoryPricePresent("IDR"), is(true));
        Assertion.assertThat("Is flash sale inventory swipe able", homePage.isFlashListSaleSwipeAble(flashSaleSectionname), is(true));

        step("User click on card");
        String flashSaleInveentoryName = homePage.clickOneOfFlashSaleInventory();

        step("User redirect to detail page");
        Assertion.assertThat("Is redirected to correct hotel",
                homePage.verifyRedirectFlashSale(flashSaleInveentoryName), is(true));
    }

    @Author(names = { "Dendi" })
    @Module(name = "HeroBanner")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testClickHeroBannerNonLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testClickHeroBannerNonLogin");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Log out");
        loginPage.loginAsGuest();

        step("User click hero banner in home page");
        homePage.clickHeroBanner();

        step("user should be on tiket homepage");
        Assertion.assertThat("Verify user still in homepage", homePage.isOnHomePage(), is(true));
    }

    @Author(names = { "Dendi" })
    @Module(name = "HeroBanner")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testClickHeroBannerLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testClickHeroBannerLogin");
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        BaseTest.paramsThreadLocal.set(param);

        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("User click hero banner in home page");
        homePage.clickHeroBanner();

        step("user should be on tiket homepage");
        Assertion.assertThat("Verify user still in homepage", homePage.isOnHomePage(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PartnerPageBannerGridBannerCanBeClicked")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test(dataProvider="testPartnerPageBannerGridBannerCanBeClickedLoginDataProvider")
    public void testPartnerPageBannerGridBannerCanBeClickedLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDPartnerPage<WebDriver> ttdPartnerPage = TTDPartnerPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String keyword = param.get("keyword");
        String templateCode = param.get("templateCode");
        String appVersion = param.get("appVersion");

        step("Go to home page");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Make user as guest");
        loginPage.loginWith(loginType,username,password);

        step("get language");
        String langCode = homePage.getLanguageId();

        step("get the title for banner grid through api");
        TTDPartnerPageModules.Datum data = ttdPage.getPartnerPageGridBannerTitleLogin(username,password,keyword,templateCode,langCode,appVersion);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", "ID","IDR");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(keyword);

        step("find and open partner page");
        ttdSearchPage.findAndClickPartnerPage(keyword);

        step("verify partner page");
        Assertion.assertThat("are we on correct partner page",ttdPartnerPage.verifyPartnerPage(keyword),is(true));

        step("verify partner page grid banner");
        Assertion.assertThat("is grid banner correct on partner's page",ttdPartnerPage.verifyBannerGrid(data),is(true));

        step("verify partner page grid banner is clickable");
        Assertion.assertThat("is grid banner clickable",ttdPartnerPage.clickAndVerifyBannerGridRedirection(data),is(true));

    }

    @Author(names = { "Dendi" })
    @Module(name = "GlobalSearch")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testGlobalSearchWithoutKeywordGuest() throws JsonSyntaxException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testGlobalSearchWithoutKeywordGuest");
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        BaseTest.paramsThreadLocal.set(param);

        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("get current app lang");
        String currentAppLang = homePage.getLanguageApp();

        step("Get global search data");
        GSLandingResponse res = homePage.getGSLandingDataNonLogin(currentAppLang);

        step("Open global search");
        homePage.clickGlobalSearch();

        step("Verify global search info");
        Assertion.assertThat("Verify global search info", homePage.verifyGlobalSearchInfo(res), is(true));

        step("Reopen global search page");
        homePage.gotoHome();
        homePage.clickGlobalSearch();

        step("Verify global search holiday inspiration");
        Assertion.assertThat("Verify global search holiday inspiration", homePage.verifyGLobalSearchHolidayInspiration(res),is(true));
    }

    @Author(names = { "Dendi" })
    @Module(name = "Setting")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testOpenSettingOthers(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testOpenSettingOthers");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open account page");
        homePage.openAccount();

        step("Open setting page");
        accountPage.openSettings();

        step("Verify setting page is open");
        Assertion.assertThat("Verify settings page is open", settingPage.isOnLoginSettingPage(), is(true));
    }

    @Author(names = { "Dendi" })
    @Module(name = "GlobalSearch")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testGlobalSearchWithoutKeywordLogin() throws JsonSyntaxException, InterruptedException, ExecutionException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testGlobalSearchWithoutKeywordLogin");
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        String loginType = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        BaseTest.paramsThreadLocal.set(testProperties);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Get current langguage");
        String currentAppLang = homePage.getLanguageApp();

        step("Get global search data");
        GSLandingResponse res = homePage.getGsLandingResponseLogin(currentAppLang,username,password);

        step("Open global search");
        homePage.clickGlobalSearch();

        step("Verify global search info");
        Assertion.assertThat("Verify global search info", homePage.verifyGlobalSearchInfo(res), is(true));

        step("Reopen global search page");
        homePage.gotoHome();
        homePage.clickGlobalSearch();

        step("Verify global search holiday inspiration");
        Assertion.assertThat("Verify global search holiday inspiration", homePage.verifyGLobalSearchHolidayInspiration(res),is(true));
    }

    @Author(names = { "Dendi" })
    @Module(name = "GlobalSearch")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testGlobalSearchWithTwoLettersKeywordLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testGlobalSearchWithTwoLettersKeywordLogin");
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        String loginType = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String keyword = "ho";
        String expectedWarningKeywordEn = testProperties.get("expectedWarningKeywordEn");
        String expectedWarningKeywordId = testProperties.get("expectedWarningKeywordId");
        BaseTest.paramsThreadLocal.set(testProperties);

        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();
        String currentAppLang = homePage.getLanguageApp();

        step("User open app as login user");
        loginPage.loginWith(loginType, username, password);

        step("Open global search from home page");
        homePage.clickGlobalSearch();

        step("Enter keyword in global search bar");
        homePage.enterKeywordToGlobalSearchBar(keyword);

        step("Verify warning keyword appear");
        Assertion.assertThat("verify gs warning appear", homePage.verifyGlobalSearcWarningMinimunChar(expectedWarningKeywordId, expectedWarningKeywordEn, currentAppLang), is(true));

    }

    @Author(names = { "Dendi" })
    @Module(name = "GlobalSearch")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testGlobalSearchWithTwoLettersKeywordGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testGlobalSearchWithTwoLettersKeywordGuest");
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        String keyword = "ho";
        String expectedWarningKeywordEn = testProperties.get("expectedWarningKeywordEn");
        String expectedWarningKeywordId = testProperties.get("expectedWarningKeywordId");
        BaseTest.paramsThreadLocal.set(testProperties);

        step("user go back to home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Log out");
        loginPage.loginAsGuest();
        String currentAppLang = homePage.getLanguageApp();

        step("Open global search from home page");
        homePage.clickGlobalSearch();

        step("Enter keyword in global search bar");
        homePage.enterKeywordToGlobalSearchBar(keyword);

        step("Verify warning keyword appear");
        Assertion.assertThat("verify gs warning appear", homePage.verifyGlobalSearcWarningMinimunChar(expectedWarningKeywordId, expectedWarningKeywordEn, currentAppLang), is(true));
    }

    @Author(names = { "Yazid","Dendi" })
    @Module(name = "GlobalSearch")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testGlobalSearchWithThreeLettersKeywordLogin() {
        Map<String, String> testProperties = getParam("testGlobalSearchWithThreeLettersKeywordLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String keyword = testProperties.get("keyword");;

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("Open global search from home page");
        homePage.clickHomeIcon();
        homePage.clickGlobalSearch();

        step("Enter a valid keyword in global search bar");
        homePage.enterKeywordToGlobalSearchBar(keyword);

        Assertion.assertThat("Suggestion list items are shown", homePage.isSuggestionItemListsShown(), is(true));
        Assertion.assertThat("Suggestion item title should contain the search keyword", homePage.getSuggestionItemListTitle(), hasItems(CaseInsensitiveSubstringMatcher.containsIgnoringCase(keyword.toLowerCase())));
    }

    @Author(names = { "Yazid","Dendi" })
    @Module(name = "GlobalSearch")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testGlobalSearchWithThreeLettersKeywordGuest() {
        Map<String, String> testProperties = getParam("testGlobalSearchWithThreeLettersKeywordGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        String keyword = testProperties.get("keyword");;

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Log out");
        loginPage.loginAsGuest();

        step("Open global search from home page");
        homePage.clickGlobalSearch();

        step("Enter a valid keyword in global search bar");
        homePage.enterKeywordToGlobalSearchBar(keyword);

        Assertion.assertThat("Suggestion list items are shown", homePage.isSuggestionItemListsShown(), is(true));
        Assertion.assertThat("Suggestion item title should contain the search keyword", homePage.getSuggestionItemListTitle(), hasItems(CaseInsensitiveSubstringMatcher.containsIgnoringCase(keyword.toLowerCase())));
    }

    @Author(names = {"Yazid"})
    @Module(name = "CardMember")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyCardMember() {
        Map<String, String> param = getParam("testVerifyCardMember");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String phoneNumber = param.get("phoneNumber");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Wait for app to launch");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User open app as login user");
        loginPage.loginWith(loginType, phoneNumber, "");

        step("go to the card member");
        homePage.swipeToCard("Lengkapi Detail Akunmu/Complete Your Details");

        step("click the complete now button");
        homePage.clickMemberCompleteNow();

        Assertion.assertThat("Manage account detail page is opened", accountPage.isAccountDetailOpened(), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "PromoPage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testCheckPromoListDetailContentNonLogin(){
        Map<String, String> param = getParam("testCheckPromoListDetailContentNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PromoPage<WebDriver> promoPage = PromoPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();
        String lang = homePage.getLanguageApp();

        step("Get promo banner url");
        boolean needLogin = false;
        int bannerIndex = 1;
        String bannerUrl = promoPage.getUrlPromoBannerAPI(needLogin, "-", "-", bannerIndex, lang);

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();
        accountPage.chooseCountry("Indonesia");
        homePage.changeCurrency("IDR","");

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
        }

        step("User go to promo landing page");
        homePage.clickPromoChip();

        step("Click first promo banner");
        promoPage.clickPromoBanner(bannerIndex);

        step("Verify Step");
        Assertion.assertThat("Promo detail page content is displayed", promoPage.isPromoDetailPageValid(bannerUrl), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "PromoPage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testCheckPromoListDetailContentLogin(){
        Map<String, String> param = getParam("testCheckPromoListDetailContentLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PromoPage<WebDriver> promoPage = PromoPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = "IDR";

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        loginPage.loginWith(loginType,username, password);
        homePage.changeCurrency(currency,username);
        accountPage.chooseCountry("Indonesia");

        homePage.gotoHome();
        String lang = homePage.getLanguageApp();

        step("Get promo banenr url");
        boolean needLogin = true;
        int bannerIndex = 1;
        String bannerUrl = promoPage.getUrlPromoBannerAPI(needLogin, username, password, bannerIndex, lang);

        if(!lang.equalsIgnoreCase("en")) {
            step("Change Language to 'ENG'");
            homePage.changeLanguageUsingDeeplink("ENG");
        }

        step("User go to promo landing page");
        homePage.clickPromoChip();

        step("Click first promo banner");
        promoPage.clickPromoBanner(bannerIndex);

        step("Verify Step");
        Assertion.assertThat("Promo detail page content is displayed and valid", promoPage.isPromoDetailPageValid(bannerUrl), is(true));
    }

    @Author(names = {"Akbar", "Yazid"})
    @Module(name = "CalendarPage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyCalendarHolidayNonLogin() {
        Map<String, String> param = getParam("testVerifyCalendarHolidayNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        CalendarPage<WebDriver> calendarPage = CalendarPage.getInstance(session);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User open app as guest user");
        loginPage.loginAsGuest();

        step("Change currency to idr");
        homePage.changeCurrency("IDR","");

        step("Get holiday data from API");
        HolidayResponse holidayResponse = calendarPage.getHolidayData(false, "-", "-", new Date(LocalDate.now().getYear()-1900, Calendar.JANUARY,1));

        step("Set language to 'ID'");
        homePage.openAccount();
        accountPage.selectLanguage("ID");
        settingPage.backToHome();

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","ID","IDR");

        step("Open calendar page");
        trainSearchPage.openCalendar();

        step("Verify holiday on 'ID' language");
        Assertion.assertThat("Holiday is valid", calendarPage.isHolidayValid(holidayResponse, "ID"), is(true));

        step("User goto home");
        homePage.gotoHome();

        step("Set language to 'ENG'");
        homePage.openAccount();
        accountPage.selectLanguage("ENG");
        settingPage.backToHome();

        step("Open train page");
        homePage.openVerticalPage("Trains");

        step("Open calendar page");
        trainSearchPage.openCalendar();

        step("Verify holiday on 'EN' language");
        Assertion.assertThat("Holiday is valid", calendarPage.isHolidayValid(holidayResponse, "EN"), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "CalendarPage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyCalendarHolidayLogin() {
        Map<String, String> param = getParam("testVerifyCalendarHolidayLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        TrainSearchPage<WebDriver> trainSearchPage = TrainSearchPage.getInstance(session);
        CalendarPage<WebDriver> calendarPage = CalendarPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("Get holiday data from API");
        HolidayResponse holidayResponse = calendarPage.getHolidayData(true, username, password, new Date(LocalDate.now().getYear()-1900, LocalDate.now().getMonthValue() - 1,1));

        step("Set language to 'ID'");
        homePage.changeLanguageUsingDeeplink("IDN");

        step("Open train page");
        homePage.openVerticalPageDeeplink("Trains","ID","IDR");
        trainSearchPage.skipPopup();

        step("Open calendar page");
        trainSearchPage.openCalendar();

        step("Verify holiday on 'ID' language");
        Assertion.assertThat("Holiday is valid", calendarPage.isHolidayValid(holidayResponse, "ID"), is(true));

        step("User goto home");
        homePage.gotoHome();

        step("Set language to 'ENG'");
        homePage.openAccount();
        accountPage.selectLanguage("ENG");
        settingPage.backToHome();

        step("Open train page");
        homePage.openVerticalPage("Trains");

        step("Open calendar page");
        trainSearchPage.openCalendar();

        step("Verify holiday on 'EN' language");
        Assertion.assertThat("Holiday is valid", calendarPage.isHolidayValid(holidayResponse, "EN"), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PartnerPageBannerGridBannerCanBeClicked")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test(dataProvider="testPartnerPageBannerGridBannerCanBeClickedGuestDataProvider")
    public void testPartnerPageBannerGridBannerCanBeClickedGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDPartnerPage<WebDriver> ttdPartnerPage = TTDPartnerPage.getInstance(session);

        String keyword = param.get("keyword");
        String templateCode = param.get("templateCode");
        String appVersion = param.get("appVersion");

        step("Go to home page");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("get language");
        String langCode = homePage.getLanguageId();

        step("get the title for banner grid through api");
        TTDPartnerPageModules.Datum data = ttdPage.getPartnerPageGridBannerTitle(keyword,templateCode,langCode,appVersion);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", langCode, "");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(keyword);

        step("find and open partner page");
        ttdSearchPage.findAndClickPartnerPage(keyword);

        step("verify partner page");
        Assertion.assertThat("are we on correct partner page",ttdPartnerPage.verifyPartnerPage(keyword),is(true));

        step("verify partner page grid banner");
        Assertion.assertThat("is grid banner correct on partner's page",ttdPartnerPage.verifyBannerGrid(data),is(true));

        step("verify partner page grid banner is clickable");
        Assertion.assertThat("is grid banner clickable",ttdPartnerPage.clickAndVerifyBannerGridRedirection(data),is(true));

    }

    @Author(names = { "Dendi" })
    @Module(name = "Setting")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testOpenSettingGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testOpenSettingGuest");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Log out");
        loginPage.loginAsGuest();

        step("Open account page");
        homePage.openAccount();

        step("Open setting page");
        accountPage.openSettings();

        step("Verify setting page is open");
        Assertion.assertThat("Verify settings page is open", settingPage.isOnSettingPage(), is(true));
    }

    @Author(names = { "Prerna"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testLocationBannerHomePageLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLocationBannerHomePageLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        homePage.gotoHome();

        step("Check user is on Home Page");
        accountPage.chooseCountry("Indonesia");
        homePage.gotoHome();
        boolean onHomePage = homePage.isOnHomePage();

        step("Verify Location Banner Section");
        boolean locationBannerIsValid = homePage.verifyLocationBanner();

        step("Click Allow Location");
        homePage.clickAllowLocationFromBanner();

        step("Check Banner should disappear");
        boolean isLocationBannerDisappear = homePage.isLocationBannerDisappear();

        step("Assertions");
        Assertion.assertThat("User is on home Page", onHomePage, is(true));
        Assertion.assertThat("Location Banner Is Valid", locationBannerIsValid, is(true));
        Assertion.assertThat("After allowing location from location banner, is the banner disappear", isLocationBannerDisappear, is(true));

    }

    @Author(names = { "Prerna"})
    @Module(name = "HomePageWeb")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testLocationBannerHomePage(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLocationBannerHomePage");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User open app as guest ");
        loginPage.loginAsGuest();

        step("Check user is on Home Page");
        boolean onHomePage = homePage.isOnHomePage();

        step("Verify Location Banner Section");
        boolean locationBannerIsValid = homePage.verifyLocationBanner();

        step("Click Allow Location");
        homePage.clickAllowLocationFromBanner();

        step("Check Banner should disappear");
        boolean isLocationBannerDisappear = homePage.isLocationBannerDisappear();

        step("Assertions");
        Assertion.assertThat("User is on home Page", onHomePage, is(true));
        Assertion.assertThat("Location Banner Is Valid", locationBannerIsValid, is(true));
        Assertion.assertThat("After allowing location from location banner, is the banner disappear", isLocationBannerDisappear, is(true));

    }

    @Author(names = {"Dendi"})
    @Module(name = "ActivatePinCard")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testActivatePinCard(){
        Map<String, String> param = getParam("testActivatePinCard");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Log out");
        loginPage.loginAsGuest();

        step("User open app as login user");
        loginPage.loginWith(loginType, username, password);

        step("user click activate pin card");
        homePage.clickActivatePinCard();

        step("Verify security setting page open");
        Assertion.assertThat("Verify security page is open", settingPage.isSecurityPageOpen(), is(true));
    }
    @Author(names = { "Dendi" })
    @Module(name = "FlashSale")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testFlashSeeAllButtonLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testFlashSeeAllButtonLogin");
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        String loginType = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SearchResultPage<WebDriver> srpHotel = SearchResultPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed) and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");
        accountPage.chooseCountry("Indonesia");

        step("Get langguage");
        String lang = homePage.getLanguageApp();

        step("Request data for availaible promo modules through api");
        String flashSaleSectionname = homePage.getHomeModule(username, password, "FLASH_SALE",lang,"IDR");

        step("User search for flash sale in home page");
        homePage.swipeToCard(flashSaleSectionname);

        step("User click flash sale see all cta button");
        homePage.clickSeeAllFlashSale(flashSaleSectionname);

        step("Verify flash sale Then user will redirect to list page hotel");
        Assertion.assertThat("Verify redirect to hotel srp",  srpHotel.isHeaderSearchResultShown(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PartnerPageImageWithChipsInventoryCanBeClicked")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test(dataProvider="testPartnerPageImageWithChipsInventoryCanBeClickedLoginDataProvider")
    public void testPartnerPageImageWithChipsInventoryCanBeClickedLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDPartnerPage<WebDriver> ttdPartnerPage = TTDPartnerPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String keyword = param.get("keyword");
        String templateCode = param.get("templateCode");
        String appVersion = param.get("appVersion");

        step("Go to home page");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("login in using credentials");
        loginPage.loginWith(loginType,username,password);

        step("get language");
        String langCode = homePage.getLanguageId();

        step("get the title for banner grid through api");
        TTDPartnerPageModules.Datum data = ttdPage.getPartnerPageGridBannerTitleLogin(username,password,keyword,templateCode,langCode,appVersion);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do",langCode,"");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(keyword);

        step("find and open partner page");
        ttdSearchPage.findAndClickPartnerPage(keyword);

        step("verify partner page");
        Assertion.assertThat("are we on correct partner page",ttdPartnerPage.verifyPartnerPage(keyword),is(true));

        step("verify partner page image with chips");
        Assertion.assertThat("is image with chips correct on partner's page",ttdPartnerPage.verifyImageWithChips(data),is(true));

        step("verify partner page grid banner is clickable and scrollable");
        Assertion.assertThat("is grid banner clickable",ttdPartnerPage.clickAndVerifyScrollImageWithChipsRedirection(data),is(true));

        step("back to partner page");
        ttdPartnerPage.backToTTDPartnerPage();

        step("verify partner page see all btn");
        Assertion.assertThat("is image with chips see all btn correct on partner's page",ttdPartnerPage.clickSeeAllBtn(data),is(true));

        step("verify partner page chips scrollable");
        Assertion.assertThat("is image with chips correct on partner's page",ttdPartnerPage.isChipsScrollable(data),is(true));
    }

    @Author(names = {"Akbar", "Yazid"})
    @Module(name = "PromoPage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testCheckPromoListNonLogin(){
        Map<String, String> param = getParam("testCheckPromoListNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PromoPage<WebDriver> promoPage = PromoPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();
        accountPage.chooseCountry("Indonesia");

        homePage.gotoHome();
        String language = homePage.getLanguageApp().toUpperCase();

        step("Change currency");
        homePage.changeCurrency("IDR","");

        step("Request data for promo module section using api");
        String[] sectionTitle = homePage.getPromoSection(false, "-", "-", language,"IDR");
        Assertion.assertThat("Promo section is there", sectionTitle[0], is(not("")));

        step("Swiping to target section '" + sectionTitle[0] + "'");
        homePage.swipeToCard(sectionTitle[0].trim());
        homePage.clickCtaButton(sectionTitle);

        step("Verify Step");
        Assertion.assertThat("Promo list page is displayed", promoPage.isPromoListPageValid(), is(true));
    }

    @Author(names = {"Akbar", "Yazid"})
    @Module(name = "PromoPage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testCheckPromoListLogin(){
        Map<String, String> param = getParam("testCheckPromoListLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        PromoPage<WebDriver> promoPage = PromoPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed) and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");
        accountPage.chooseCountry("Indonesia");
        String language = homePage.getLanguageApp().toUpperCase();
        homePage.gotoHome();

        step("Request data for promo module section using api");
        String[] sectionTitle = homePage.getPromoSection(true, username, password, language,"IDR");
        Assertion.assertThat("Promo section is there", sectionTitle[0], is(not("")));

        step("Swiping to target section '" + sectionTitle[0] + "'");
        homePage.swipeToCard(sectionTitle[0].trim());
        homePage.clickCtaButton(sectionTitle);

        step("Verify Step");
        Assertion.assertThat("Promo list page is displayed", promoPage.isPromoListPageValid(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "verifyOpenInbox")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test(dataProvider="testVerifyOpenInboxLoginDataProvider")
    public void testVerifyOpenInboxLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        InboxPage<WebDriver> inboxPage = InboxPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Go to home page");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login in using credentials");
        loginPage.loginWith(loginType,username,password);

        step("Open inbox");
        homePage.clickInboxIcon();

        step("verify we are on inbox page");
        Assertion.assertThat("are we on inbox page",inboxPage.verifyWeAreOnInboxPage(),is(true));
    }

    @Author(names = { "Dendi" })
    @Module(name = "FlashSale")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testFlashSeeAllButtonGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> testProperties = getParam("testFlashSeeAllButtonGuest");
        BaseTest.paramsThreadLocal.set(testProperties);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SearchResultPage<WebDriver> srpHotel = SearchResultPage.getInstance(session);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Non login user");
        loginPage.loginAsGuest();

        step("Get langguage");
        String lang = homePage.getLanguageApp();

        step("Request data for availaible promo modules through api");
        String flashSaleSectionname = homePage.getHomeModuleNonLogin( "FLASH_SALE",lang,"IDR");

        step("User search for flash sale in home page");
        homePage.swipeToCard(flashSaleSectionname);

        step("User click flash sale see all cta button");
        homePage.clickSeeAllFlashSale(flashSaleSectionname);

        step("Verify flash sale Then user will redirect to list page hotel");
        Assertion.assertThat("Verify redirect to hotel srp",  srpHotel.isHeaderSearchResultShown(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "verifyPromoListHomePage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void verifyHeaderSliderPromoListHomePageLogin(){
        Map<String, String> param = getParam("verifyHeaderSliderPromoListHomePageLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = "IDR";

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency(currency,"");
        accountPage.chooseCountry("Indonesia");

        step("Check Header on Home Page");
        homePage.goToHomeDeeplink();
        boolean headerOnHomePage = homePage.isOnHomePage();

        step("Click Promo Button");
        homePage.clickPromoChip();

        Assertion.assertThat("Verify Header on Home Page", headerOnHomePage, is(true));
        Assertion.assertThat("Verify Promo Page", homePage.isOnPromoPage(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "verifyPromoListHomePage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void verifyHeaderSliderPromoListHomePage(){
        Map<String, String> param = getParam("verifyHeaderSliderPromoListHomePage");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("USer enter app as guest");
        loginPage.loginAsGuest();

        step("Change currency to IDR");
        homePage.changeCurrency("IDR","");

        step("Check Header on Home Page");
        boolean headerOnHomePage = homePage.isOnHomePage();

        step("Click Promo Button");
        homePage.clickPromoChip();

        Assertion.assertThat("Verify Header on Home Page", headerOnHomePage, is(true));
        Assertion.assertThat("Verify Promo Page", homePage.isOnPromoPage(), is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "PartnerPageImageWithChipsInventoryCanBeClicked")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test(dataProvider="testPartnerPageImageWithChipsInventoryCanBeClickedGuestDataProvider")
    public void testPartnerPageImageWithChipsInventoryCanBeClickedGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDPartnerPage<WebDriver> ttdPartnerPage = TTDPartnerPage.getInstance(session);

        String keyword = param.get("keyword");
        String templateCode = param.get("templateCode");
        String appVersion = param.get("appVersion");

        step("Go to home page");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Make user as guest");
        loginPage.loginAsGuest();

        step("get language");
        String langCode = homePage.getLanguageId();

        step("get the title for banner grid through api");
        TTDPartnerPageModules.Datum data = ttdPage.getPartnerPageGridBannerTitle(keyword,templateCode,langCode,appVersion);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", langCode, "");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(keyword);

        step("find and open partner page");
        ttdSearchPage.findAndClickPartnerPage(keyword);

        step("verify partner page");
        Assertion.assertThat("are we on correct partner page",ttdPartnerPage.verifyPartnerPage(keyword),is(true));

        step("verify partner page image with chips");
        Assertion.assertThat("is image with chips correct on partner's page",ttdPartnerPage.verifyImageWithChips(data),is(true));

        step("verify partner page grid banner is clickable and scrollable");
        Assertion.assertThat("is grid banner clickable",ttdPartnerPage.clickAndVerifyScrollImageWithChipsRedirection(data),is(true));

        step("back to partner page");
        ttdPartnerPage.backToTTDPartnerPage();

        step("verify partner page see all btn");
        Assertion.assertThat("is image with chips see all btn correct on partner's page",ttdPartnerPage.clickSeeAllBtn(data),is(true));

        step("verify partner page chips scrollable");
        Assertion.assertThat("is image with chips correct on partner's page",ttdPartnerPage.isChipsScrollable(data),is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "PartnerPageBannerCarouselInventoryCanBeClicked")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test(dataProvider="testPartnerPageBannerCarouselInventoryCanBeClickedLoginDataProvider")
    public void testPartnerPageBannerCarouselInventoryCanBeClickedLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDPartnerPage<WebDriver> ttdPartnerPage = TTDPartnerPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String keyword = param.get("keyword");
        String templateCode = param.get("templateCode");
        String appVersion = param.get("appVersion");

        step("Go to home page");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("login in using credentials");
        loginPage.loginWith(loginType,username,password);

        step("get language");
        String langCode = homePage.getLanguageId();

        step("get the title for banner grid through api");
        TTDPartnerPageModules.Datum data = ttdPage.getPartnerPageGridBannerTitleLogin(username,password,keyword,templateCode,langCode,appVersion);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", langCode, "");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(keyword);

        step("find and open partner page");
        ttdSearchPage.findAndClickPartnerPage(keyword);

        step("verify partner page");
        Assertion.assertThat("are we on correct partner page",ttdPartnerPage.verifyPartnerPage(keyword),is(true));

        step("verify partner page Banner Carousel");
        Assertion.assertThat("is Banner Carousel correct on partner's page",ttdPartnerPage.verifyBannerCarousel(data),is(true));

        step("verify partner page grid banner is clickable and scrollable");
        Assertion.assertThat("is grid banner clickable",ttdPartnerPage.clickAndVerifyScrollBannerCarouselRedirection(data),is(true));

        step("Back to TTD Partner Page");
        searchResultPage.clickBackButton();

        step("verify partner page chips scrollable");
        Assertion.assertThat("is banner Carousel chips scrollable",ttdPartnerPage.isChipsScrollable(data),is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "PartnerPageBannerCarouselInventoryCanBeClicked")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test(dataProvider="testPartnerPageBannerCarouselInventoryCanBeClickedDataProvider")
    public void testPartnerPageBannerCarouselInventoryCanBeClicked(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        TTDPage<WebDriver> ttdPage = TTDPage.getInstance(session);
        TTDLandingPage<WebDriver> ttdLandingPage = TTDLandingPage.getInstance(session);
        TTDSearchPage<WebDriver> ttdSearchPage = TTDSearchPage.getInstance(session);
        TTDPartnerPage<WebDriver> ttdPartnerPage = TTDPartnerPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        String keyword = param.get("keyword");
        String templateCode = param.get("templateCode");
        String appVersion = param.get("appVersion");

        step("Go to home page");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as Guest");
        loginPage.loginAsGuest();

        step("get language");
        String langCode = homePage.getLanguageId();

        step("get the title for banner grid through api");
        TTDPartnerPageModules.Datum data = ttdPage.getPartnerPageGridBannerTitle(keyword,templateCode,langCode,appVersion);

        step("Open TTD landing page");
        homePage.openVerticalPageDeeplink("To Do", langCode, "");

        step("Search the product");
        ttdLandingPage.clickSearchBar();
        ttdSearchPage.inputSearchKeyword(keyword);

        step("find and open partner page");
        ttdSearchPage.findAndClickPartnerPage(keyword);

        step("verify partner page");
        Assertion.assertThat("are we on correct partner page",ttdPartnerPage.verifyPartnerPage(keyword),is(true));

        step("verify partner page Banner Carousel");
        Assertion.assertThat("is Banner Carousel correct on partner's page",ttdPartnerPage.verifyBannerCarousel(data),is(true));

        step("verify partner page grid banner is clickable and scrollable");
        Assertion.assertThat("is grid banner clickable",ttdPartnerPage.clickAndVerifyScrollBannerCarouselRedirection(data),is(true));

        step("Back to TTD Partner Page");
        searchResultPage.clickBackButton();

        step("verify partner page chips scrollable");
        Assertion.assertThat("is banner Carousel chips scrollable",ttdPartnerPage.isChipsScrollable(data),is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "PageModule")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyThePriceUsingCurrencyIDRNonLogin(){
        Map<String, String> param = getParam("testVerifyThePriceUsingCurrencyIDRNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User open app as guest user");
        loginPage.loginAsGuest();
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
            lang = homePage.getLanguageApp();
        }

        step("Change currency");
        homePage.changeCurrency("IDR","");

        step("User go to selected page module");
        boolean needLogin = false;
        String[] targetModulesTemplate = new String[] {"IMAGE_WITH_CHIPS","HIGHLIGHT_DISCOVER_PRODUCT","FLASH_SALE"};
        String[] targetModuleNames = homePage.getHomeModules(needLogin, "-", "-", targetModulesTemplate,lang,"IDR");

        step("Verify Step");
        Assertion.assertThat("Each page module is showing IDR Currency", homePage.isCurrencyOnPageModuleCorrect(targetModuleNames,"IDR"), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "PageModule")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyThePriceUsingCurrencyIDRLogin(){
        Map<String, String> param = getParam("testVerifyThePriceUsingCurrencyIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed) and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");
        accountPage.chooseCountry("Indonesia");
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("en")) {
            step("Change Language to 'ENG'");
            homePage.changeLanguageUsingDeeplink("ENG");
            lang = homePage.getLanguageApp();
        }

        step("User go to selected page module");
        boolean needLogin = true;
        String[] targetModulesTemplate = new String[] {"IMAGE_WITH_CHIPS","HIGHLIGHT_DISCOVER_PRODUCT","FLASH_SALE"};
        String[] targetModuleNames = homePage.getHomeModules(needLogin, username, password, targetModulesTemplate,lang,"IDR");

        step("Verify Step");
        Assertion.assertThat("Each page module is showing IDR Currency", homePage.isCurrencyOnPageModuleCorrect(targetModuleNames,"IDR"), is(true));
    }

    @Author(names = {"Ashish", "Prerna"})
    @Module(name = "VerifyCardDefaultChipsModule")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test(dataProvider="testVerifyCardDefaultChipsModuleLoginDataProvider")
    public void testVerifyCardDefaultChipsModuleLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<?> accountPage = AccountPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String templateCode = param.get("templateCode");

        step("Go to home page");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");
        accountPage.chooseCountry("Indonesia");

        step("Get language");
        String langCode = homePage.getLanguageId();

        step("Get title from api for card with default chips");
        HomePageModule.PageModules pageModules = homePage.getHomeModuleLogin(username, password, templateCode,langCode,"IDR");

        step("Swipe to card with default chips");
        homePage.swipeToCard(pageModules.moduleDetail.heading.title);

        step("Verify card default chips");
        Assertion.assertThat("is card default chips correct",homePage.verifyCardDefaultChips(pageModules),is(true));

        step("Verify card and chips scrollable and chips clickable");
        Assertion.assertThat("card and chips scrollable and chips clickable",homePage.VerifyScrollCardDefaultChips(pageModules),is(true));

        step("Click first card");
        Assertion.assertThat("is redirected to blog page",homePage.verifyClickCard(pageModules),is(true));

        step("Back to home page");
        homePage.clickBackBtn();

        step("Click see all btn (CLICK ME)");
        Assertion.assertThat("is redirected to home page",homePage.clickSeeAllBtn(pageModules),is(true));
    }

    @Author(names = {"Ashish", "Prerna"})
    @Module(name = "VerifyCardDefaultChipsModule")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test(dataProvider="testVerifyCardDefaultChipsModuleGuestDataProvider")
    public void testVerifyCardDefaultChipsModuleGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String templateCode = param.get("templateCode");

        step("Go to home page");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Get language");
        String langCode = homePage.getLanguageId();

        step("Get title from api for card with default chips");
        HomePageModule.PageModules pageModules = homePage.getHomeModuleGuest(templateCode,langCode,"IDR");

        step("Swipe to card with default chips");
        homePage.swipeToCard(pageModules.moduleDetail.heading.title);

        step("Verify card default chips");
        Assertion.assertThat("is card default chips correct",homePage.verifyCardDefaultChips(pageModules),is(true));

        step("Verify card and chips scrollable and chips clickable");
        Assertion.assertThat("card and chips scrollable and chips clickable",homePage.VerifyScrollCardDefaultChips(pageModules),is(true));

        step("Click first card");
        Assertion.assertThat("is redirected to blog page",homePage.verifyClickCard(pageModules),is(true));

        step("Back to home page");
        homePage.clickBackBtn();

        step("Click see all btn (CLICK ME)");
        Assertion.assertThat("is redirected to home page",homePage.clickSeeAllBtn(pageModules),is(true));
    }

    @DataProvider(name="testPartnerPageBannerGridBannerCanBeClickedLoginDataProvider")
    public Object[][] testPartnerPageBannerGridBannerCanBeClickedLoginDataProvider(){
        return getObjectParams("testPartnerPageBannerGridBannerCanBeClickedLogin");
    }

    @DataProvider(name="testPartnerPageBannerGridBannerCanBeClickedGuestDataProvider")
    public Object[][] testPartnerPageBannerGridBannerCanBeClickedGuestDataProvider(){
        return getObjectParams("testPartnerPageBannerGridBannerCanBeClickedGuest");
    }

    @DataProvider(name="testPartnerPageImageWithChipsInventoryCanBeClickedLoginDataProvider")
    public Object[][] testPartnerPageImageWithChipsInventoryCanBeClickedLoginDataProvider(){
        return getObjectParams("testPartnerPageImageWithChipsInventoryCanBeClickedLogin");
    }
    @DataProvider(name="testPartnerPageImageWithChipsInventoryCanBeClickedGuestDataProvider")
    public Object[][] testPartnerPageImageWithChipsInventoryCanBeClickedGuestDataProvider(){
        return getObjectParams("testPartnerPageImageWithChipsInventoryCanBeClickedGuest");
    }

    @DataProvider(name="testVerifyOpenInboxLoginDataProvider")
    public Object[][] testVerifyOpenInboxLoginDataProvider(){
        return getObjectParams("testVerifyOpenInboxLogin");
    }

    @DataProvider(name="testPartnerPageBannerCarouselInventoryCanBeClickedLoginDataProvider")
    public Object[][] testPartnerPageBannerCarouselInventoryCanBeClickedLoginDataProvider(){
        return getObjectParams("testPartnerPageBannerCarouselInventoryCanBeClickedLogin");
    }

    @DataProvider(name="testPartnerPageBannerCarouselInventoryCanBeClickedDataProvider")
    public Object[][] testPartnerPageBannerCarouselInventoryCanBeClickedDataProvider(){
        return getObjectParams("testPartnerPageBannerCarouselInventoryCanBeClicked");
    }

    @DataProvider(name="testVerifyCardDefaultChipsModuleLoginDataProvider")
    public Object[][] testVerifyCardDefaultChipsModuleLoginDataProvider(){
        return getObjectParams("testVerifyCardDefaultChipsModuleLogin");
    }

    @DataProvider(name="testVerifyCardDefaultChipsModuleGuestDataProvider")
    public Object[][] testVerifyCardDefaultChipsModuleGuestDataProvider(){
        return getObjectParams("testVerifyCardDefaultChipsModuleGuest");
    }

    @Author(names = {"Kartik"})
    @Module(name = "GlobalSearch")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testGlobalSearchVerifyVerticaKeyword() throws JsonSyntaxException, InterruptedException, ExecutionException, ParseException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testGlobalSearchVerifyVerticaKeyword");
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        String loginType = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String VerticalName = testProperties.get("VerticalName");

        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();
        String currentAppLang = homePage.getLanguageId();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        homePage.changeLanguageUsingDeeplink("ENG");

        homePage.gotoHome();

        step("Open global search");
        homePage.clickGlobalSearch();

        step("Get global search data");
        GSLandingResponse res = homePage.getGsLandingResponseLogin(currentAppLang,username,password);

        step("Verify global search info");
        Assertion.assertThat("Verify global search info", homePage.verifyGlobalSearchInfo(res), is(true));

        step("Input search keyword in searchbar");
        homePage.inputSearchKeyword(VerticalName);

        step("Get GS Article section Data");
        GSSearchPageResponse response = homePage.getGlobalSearchResultResponse(currentAppLang, VerticalName);

        step("Verify global search vertical keyword");
        Assertion.assertThat("Verify global search vertical info", homePage.verifyGlobalSearchVerticalSection(VerticalName), is(true));

        step("verify global search article seciton");
        homePage.inputSearchKeyword(VerticalName);
        Assertion.assertThat("Verify global search Article info", homePage.verifyGlobalSearchArticleSection(response), is(true));
    }

    @Author(names = {"Akbar","Ashish"})
    @Vertical(name = "Platform")
    @Tribe(name = "Discovery")
    @Module(name = "FinanceBar")
    @Test
    public void verifyFinanceBarUsingIDRCurrencyNonLogin(){
        Map<String, String> param = getParam("verifyFinanceBarUsingIDRCurrencyNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR","");
        homePage.refreshHomePage(1);

        step("Check the running text on finance bar");
        boolean isRunningTextValid = homePage.checkRunningTextFinanceBar();

        step("Click the finance bar");
        homePage.clickFinanceBar();

        step("Verify Step");
        Assertion.assertThat("Running Text on Finance Bar is valid", isRunningTextValid, is(true));
        Assertion.assertThat("Redirected to login page", loginPage.isOnLoginPage(), is(true));
    }

    @Author(names = {"Aditi"})
    @Vertical(name = "Platform")
    @Tribe(name = "Discovery")
    @Module(name = "FinanceBar")
    @Test
    public void testFinanceBarGuestLoginRedirectFromRunningText(){
        Map<String, String> param = getParam("testFinanceBarGuestLoginRedirectFromRunningText");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Logout via deeplink to ensure guest state");
        homePage.logoutViaDeeplink();

        step("Change currency to 'IDR'");
        homePage.changeCurrency("IDR","");
        homePage.refreshHomePage(1);

        String[] financeBarSections = {"Blibli Tiket Rewards", "PayLater", "BlibliPay"};
        for (String sectionTitle : financeBarSections) {
            step("Click finance bar running text: " + sectionTitle);
            homePage.clickFinanceBarRunningText(sectionTitle);

            step("Verify redirected to login page");
            Assertion.assertThat("Redirected to login page after clicking " + sectionTitle, loginPage.isOnLoginPage(), is(true));

            step("Login from finance bar flow");
            loginPage.loginFromDifferentPages(username, password);

            step("Verify redirected to targeted page after login for " + sectionTitle);
            boolean isOnTargetPage;
            if (sectionTitle.contains("Blibli Tiket Rewards")) {
                isOnTargetPage = homePage.isOnBlibliTiketRewardsPage();
            } else if (sectionTitle.contains("PayLater")) {
                isOnTargetPage = homePage.isOnPaylaterUspPage();
            } else {
                isOnTargetPage = homePage.isOnBlipayDashboard();
            }
            Assertion.assertThat("Redirected to " + sectionTitle + " page after login", isOnTargetPage, is(true));

            step("Logout after verifying " + sectionTitle + " page");
            homePage.logoutViaDeeplink();

            step("Go to home page by clicking home button");
            homePage.clickHomeIcon();
            Assertion.assertThat("Home page is displayed", homePage.isOnHomePage(), is(true));

            step("Change currency to 'IDR'");
            homePage.changeCurrency("IDR","");
            homePage.refreshHomePage(1);
        }
    }

    @Author(names = {"Aditi"})
    @Vertical(name = "Platform")
    @Tribe(name = "Discovery")
    @Module(name = "FinanceBar")
    @Test
    public void verifyFinanceBarContentByCurrency() {
        Map<String, String> param = getParam("verifyFinanceBarContentByCurrency");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String nonIdrCurrency = param.get("currency");
        String tier = "Tier";

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");
        homePage.refreshHomePage(1);

        step("Verify IDR login details show tier, PayLater, and BlibliPay");
        Assertion.assertThat(
                "IDR login details should show loyalty tier, PayLater, and BlibliPay",
                homePage.areIdrLoginDetailsShown(tier), is(true));

        step("Change currency to " + nonIdrCurrency);
        homePage.changeCurrency(nonIdrCurrency, username);
        homePage.refreshHomePage(1);

        step("Change language to English and verify non-IDR finance bar content");
        homePage.changeLanguageUsingDeeplink("ENG");
        homePage.refreshHomePage(1);
        boolean isNonIdrFinanceBarValidEn = homePage.checkRunningTextFinanceBarForNonIdrCurrency(nonIdrCurrency, "ENG");
        Assertion.assertThat(
                "Finance bar should show English running text for selected non-IDR currency without PayLater and BlibliPay",
                isNonIdrFinanceBarValidEn, is(true));

        step("Change language to Bahasa Indonesia and verify non-IDR finance bar content");
        homePage.changeLanguageUsingDeeplink("IDN");
        homePage.refreshHomePage(1);
        boolean isNonIdrFinanceBarValidId = homePage.checkRunningTextFinanceBarForNonIdrCurrency(nonIdrCurrency, "IDN");
        Assertion.assertThat(
                "Finance bar should show Bahasa running text for selected non-IDR currency without PayLater and BlibliPay",
                isNonIdrFinanceBarValidId, is(true));
    }

    @Author(names = {"Prerna","Ashish"})
    @Vertical(name = "Platform")
    @Tribe(name = "Discovery")
    @Module(name = "FinanceBar")
    @Test
    public void verifyFinanceBarUsingNonIDRCurrencyLogin(){
        Map<String, String> param = getParam("verifyFinanceBarUsingNonIDRCurrencyLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);
        homePage.refreshHomePage(1);

        step("Click the finance bar");
        Assertion.assertThat("is tool tip message appears on finance bar", homePage.isToolTipMessageAppears(currency), is(true));
    }

    @Author(names = {"Akbar"})
    @Vertical(name = "Platform")
    @Tribe(name = "Discovery")
    @Module(name = "HomePageModule")
    @Test
    public void verifyBannerCarouselV2NonLogin(){
        Map<String, String> param = getParam("verifyBannerCarouselV2NonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User open app as a guest user");
        loginPage.loginAsGuest();
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
            lang = homePage.getLanguageApp();
        }

        step("Change Currency to 'IDR'");
        homePage.changeCurrency("IDR","");

        step("Get BANNER_CAROUSEL_V2 with chips + with CTA button");
        boolean needLogin = false;
        String username = "-";
        String password = "-";
        HomePageModule homePageModuleResponse = homePage.getHomeModuleResponse(needLogin,username,password,lang,"IDR");

        boolean withChips = true;
        boolean withCTAButton = true;
        boolean withSubtitle = true;
        boolean withSeeAllLink = true;
        HomePageModule.PageModules targetPageModule = homePage.getFirstBannerCarouselV2(homePageModuleResponse, withChips, withCTAButton, withSubtitle, withSeeAllLink);
        Assertion.assertThat("PageModule is available", targetPageModule.moduleDetail.heading.title != null, is(true));

        step("Swipe to target page module");
        String pageModuleTitle = targetPageModule.moduleDetail.heading.title;
        homePage.swipeToCard(pageModuleTitle);

        step("Verify UI Step");
        boolean isTitleHeadingAppear = homePage.isBannerCarouselV2ComponentValid(pageModuleTitle,"TITLE_HEADING");
        boolean isSubtitleHeadingAppear = homePage.isBannerCarouselV2ComponentValid(pageModuleTitle,"SUBTITLE_HEADING");
        boolean isChipListAppearScrollableClickable = homePage.isBannerCarouselV2ComponentValid(pageModuleTitle,"CHIP_LIST");
        boolean isImageBannerListAppearScrollableClickable = homePage.isBannerCarouselV2ComponentValid(pageModuleTitle,"IMAGE_BANNER_LIST");
        boolean isSeeAllLinkAppearClickable = homePage.isBannerCarouselV2ComponentValid(pageModuleTitle,"SEE_ALL_LINK");
        boolean isCTAButtonWithTextAppearClickable = homePage.isBannerCarouselV2ComponentValid(pageModuleTitle,"CTA_BUTTON_WITH_TEXT");

        step("Click other than selected chips");
        String byType = "index";
        homePage.clickPageModuleChip(pageModuleTitle, byType, "1");
        boolean isNewImageBannerListAppear = homePage.isBannerCarouselV2ComponentValid(pageModuleTitle,"IMAGE_BANNER_LIST");
        homePage.clickPageModuleChip(pageModuleTitle, byType, "0");

        step("Click First Banner on '"+pageModuleTitle+"'");
        int clickedBannerIndex = 1;
        int clickedChipsIndex = 0;
        homePage.clickBannerCarouselV2BannerByIndex(targetPageModule, clickedBannerIndex);

        step("Verify Step");
        Assertion.assertThat("Title Heading appeared", isTitleHeadingAppear, is(true));
        Assertion.assertThat("Subtitle Heading appeared", isSubtitleHeadingAppear, is(true));
        Assertion.assertThat("Chip List appeared, scrollable, and clickable", isChipListAppearScrollableClickable, is(true));
        Assertion.assertThat("Image Banner List appeared, scrollable, and clickable", isImageBannerListAppearScrollableClickable, is(true));
        Assertion.assertThat("See All Link appeared and clickable", isSeeAllLinkAppearClickable, is(true));
        Assertion.assertThat("CTA Button with Text appeared and clickable", isCTAButtonWithTextAppearClickable, is(true));

        Assertion.assertThat("Image Banner List appeared after clicked other chip", isNewImageBannerListAppear, is(true));

        Assertion.assertThat("Redirected to correct page", homePage.isBannerRedirectdValid(targetPageModule, clickedChipsIndex, clickedBannerIndex), is(true));
    }

    @Author(names = {"Akbar"})
    @Vertical(name = "Platform")
    @Tribe(name = "Discovery")
    @Module(name = "HomePageModule")
    @Test
    public void verifyBannerCarouselV2Login(){
        Map<String, String> param = getParam("verifyBannerCarouselV2Login");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed) and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");
        accountPage.chooseCountry("Indonesia");
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("en")) {
            step("Change Language to 'ENG'");
            homePage.changeLanguageUsingDeeplink("ENG");
            lang = homePage.getLanguageApp();
        }

        step("Get BANNER_CAROUSEL_V2 with chips + with CTA button");
        boolean needLogin = true;
        HomePageModule homePageModuleResponse = homePage.getHomeModuleResponse(needLogin,username,password,lang,"IDR");

        boolean withChips = true;
        boolean withCTAButton = true;
        boolean withSubtitle = true;
        boolean withSeeAllLink = true;
        HomePageModule.PageModules targetPageModule = homePage.getFirstBannerCarouselV2(homePageModuleResponse, withChips, withCTAButton, withSubtitle, withSeeAllLink);
        Assertion.assertThat("PageModule is available", targetPageModule.moduleDetail.heading.title != null, is(true));

        step("Swipe to target page module");
        String pageModuleTitle = targetPageModule.moduleDetail.heading.title;
        homePage.swipeToCard(pageModuleTitle);

        step("Verify UI Step");
        boolean isTitleHeadingAppear = homePage.isBannerCarouselV2ComponentValid(pageModuleTitle,"TITLE_HEADING");
        boolean isSubtitleHeadingAppear = homePage.isBannerCarouselV2ComponentValid(pageModuleTitle,"SUBTITLE_HEADING");
        boolean isChipListAppearScrollableClickable = homePage.isBannerCarouselV2ComponentValid(pageModuleTitle,"CHIP_LIST");
        boolean isImageBannerListAppearScrollableClickable = homePage.isBannerCarouselV2ComponentValid(pageModuleTitle,"IMAGE_BANNER_LIST");
        boolean isSeeAllLinkAppearClickable = homePage.isBannerCarouselV2ComponentValid(pageModuleTitle,"SEE_ALL_LINK");
        boolean isCTAButtonWithTextAppearClickable = homePage.isBannerCarouselV2ComponentValid(pageModuleTitle,"CTA_BUTTON_WITH_TEXT");

        step("Click other than selected chips");
        String byType = "index";
        homePage.clickPageModuleChip(pageModuleTitle, byType, "1");
        boolean isNewImageBannerListAppear = homePage.isBannerCarouselV2ComponentValid(pageModuleTitle,"IMAGE_BANNER_LIST");
        homePage.clickPageModuleChip(pageModuleTitle, byType, "0");

        step("Click First Banner on '"+pageModuleTitle+"'");
        int clickedBannerIndex = 1;
        int clickedChipsIndex = 0;
        homePage.clickBannerCarouselV2BannerByIndex(targetPageModule, clickedBannerIndex);

        step("Verify Step");
        Assertion.assertThat("Title Heading appeared", isTitleHeadingAppear, is(true));
        Assertion.assertThat("Subtitle Heading appeared", isSubtitleHeadingAppear, is(true));
        Assertion.assertThat("Chip List appeared, scrollable, and clickable", isChipListAppearScrollableClickable, is(true));
        Assertion.assertThat("Image Banner List appeared, scrollable, and clickable", isImageBannerListAppearScrollableClickable, is(true));
        Assertion.assertThat("See All Link appeared and clickable", isSeeAllLinkAppearClickable, is(true));
        Assertion.assertThat("CTA Button with Text appeared and clickable", isCTAButtonWithTextAppearClickable, is(true));

        Assertion.assertThat("Image Banner List appeared after clicked other chip", isNewImageBannerListAppear, is(true));

        Assertion.assertThat("Redirected to correct page", homePage.isBannerRedirectdValid(targetPageModule, clickedChipsIndex, clickedBannerIndex), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "SeasonalBanner")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifySeasonalBannerGuest() {
        Map<String, String> param = getParam("testVerifySeasonalBannerGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String templateCode = "SEASONAL";

        step("Go to home page");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Get language");
        String langCode = homePage.getLanguageId();

        step("Get title from api for card with default chips");
        HomePageModule.PageModules pageModules = homePage.getHomeModuleGuest(templateCode,langCode,"IDR");

        step("Swipe to Seasonal Banner");
        homePage.swipeToCard(pageModules.dynamicContents.get(0).contentTitle);

        step("Verify Seasonal Banner");
        boolean isSeasonalBannerCorrect = homePage.verifySeasonalBanner(pageModules);

        step("Check redirection of seasonal Banner Button");
        boolean isRedirectionCorrect = homePage.verifySeasonalBannerClick(pageModules);

        step("Check If banner is Scrollable");
        boolean isBannerScrollable = homePage.verifySeasonalBannerScrollable(pageModules);

        step("Assertions");
        Assertion.assertThat("Seasonal Banner is Valid", isSeasonalBannerCorrect, is(true));
        Assertion.assertThat("Redirection of Button Valid ? ", isRedirectionCorrect, is(true));
        Assertion.assertThat("is Banner Scrollable", isBannerScrollable, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "SeasonalBanner")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifySeasonalBannerLogin() {
        Map<String, String> param = getParam("testVerifySeasonalBannerLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);


        String templateCode = "SEASONAL";
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed) and change currency to IDR");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");

        step("Get language");
        homePage.gotoHome();
        String langCode = homePage.getLanguageId();

        step("Get title from api for card with default chips");
        HomePageModule.PageModules pageModules = homePage.getHomeModuleGuest(templateCode,langCode,"IDR");

        step("Swipe to Seasonal Banner");
        homePage.swipeToCard(pageModules.dynamicContents.get(0).contentTitle);

        step("Verify Seasonal Banner");
        boolean isSeasonalBannerCorrect = homePage.verifySeasonalBanner(pageModules);

        step("Check redirection of seasonal Banner Button");
        boolean isRedirectionCorrect = homePage.verifySeasonalBannerClick(pageModules);

        step("Check If banner is Scrollable");
        boolean isBannerScrollable = homePage.verifySeasonalBannerScrollable(pageModules);

        step("Assertions");
        Assertion.assertThat("Seasonal Banner is Valid", isSeasonalBannerCorrect, is(true));
        Assertion.assertThat("Redirection of Button Valid ? ", isRedirectionCorrect, is(true));
        Assertion.assertThat("is Banner Scrollable", isBannerScrollable, is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "GlobalSearch")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testGlobalSearchVerifyVerticaKeywordGuest() throws JsonSyntaxException, InterruptedException, ExecutionException, ParseException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testGlobalSearchVerifyVerticaKeywordGuest");
        BaseTest.paramsThreadLocal.set(testProperties);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        String VerticalName = testProperties.get("VerticalName");

        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();
        String currentAppLang = homePage.getLanguageId();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Open global search");
        homePage.clickGlobalSearch();

        step("Get global search data");
        GSLandingResponse res = homePage.getGSLandingDataNonLogin(currentAppLang);

        step("Verify global search info");
        Assertion.assertThat("Verify global search info", homePage.verifyGlobalSearchInfo(res), is(true));

        step("Input search keyword in searchbar");
        homePage.inputSearchKeyword(VerticalName);

        step("Get GS Article section Data");
        GSSearchPageResponse response = homePage.getGlobalSearchResultResponse(currentAppLang, VerticalName);

        step("Verify global search vertical keyword");
        Assertion.assertThat("Verify global search vertical info", homePage.verifyGlobalSearchVerticalSection(VerticalName), is(true));

        step("verify global search article seciton");
        homePage.inputSearchKeyword(VerticalName);
        Assertion.assertThat("Verify global search Article info", homePage.verifyGlobalSearchArticleSection(response), is(true));
    }

    @Author(names = {"Prerna","Ashish"})
    @Vertical(name = "Platform")
    @Tribe(name = "Discovery")
    @Module(name = "FinanceBar")
    @Test
    public void verifyFinanceBarUsingNonIDRCurrencyNonLogin(){
        Map<String, String> param = getParam("verifyFinanceBarUsingNonIDRCurrencyNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Change currency to 'USD'");
        homePage.changeCurrency("USD","");

        step("Check the Login text on finance bar");
        boolean isLoginTextValid = homePage.checkLoginTextFinanceBar();

        step("Click the finance bar");
        homePage.clickFinanceBar();

        step("Verify Step");
        Assertion.assertThat("Login Text on Finance Bar is valid", isLoginTextValid, is(true));
        Assertion.assertThat("Redirected to login page", loginPage.isOnLoginPage(), is(true));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Platform")
    @Tribe(name = "Discovery")
    @Module(name = "HeaderAndSliderScroll")
    @Test
    public void headerAndSliderScrollUpGuest(){
        Map<String, String> param = getParam("headerAndSliderScrollUpGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User open app as a guest user");
        loginPage.loginAsGuest();

        step("Loop click down arrow");
        Assertion.assertThat("Go down using arrow", homePage.scrollUntilBottom(15), is(true));
        Assertion.assertThat("Go back to top", homePage.scrollBackToTop(), is(true));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Platform")
    @Tribe(name = "Discovery")
    @Module(name = "HeaderAndSliderScroll")
    @Test
    public void headerAndSliderScrollUpLogin(){
        Map<String, String> param = getParam("headerAndSliderScrollUpLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User open app as login user");
        loginPage.loginWith(loginType, username, password);

        step("Loop click down arrow");
        Assertion.assertThat("Verify down arrow", homePage.scrollUntilBottom(15), is(true));
        Assertion.assertThat("Back to top of page", homePage.scrollBackToTop(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "PageModule")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyThePriceUsingCurrencyNonIDRLogin(){
        Map<String, String> param = getParam("testVerifyThePriceUsingCurrencyNonIDRLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String currency = param.get("currency");

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);
        accountPage.chooseCountry("Indonesia");
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("en")) {
            step("Change Language to 'ENG'");
            homePage.changeLanguageUsingDeeplink("ENG");
            lang = homePage.getLanguageApp();
        }

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User go to selected page module");
        boolean needLogin = true;
        String[] targetModulesTemplate = new String[] {"IMAGE_WITH_CHIPS","HIGHLIGHT_DISCOVER_PRODUCT","FLASH_SALE"};
        String[] targetModuleNames = homePage.getHomeModules(needLogin, username, password, targetModulesTemplate,lang,currency);

        step("Verify Step");
        Assertion.assertThat("Each page module is showing "+currency+" Currency", homePage.isCurrencyOnPageModuleCorrect(targetModuleNames,currency), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "PageModule")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyThePriceUsingCurrencyNonIDRNonLogin(){
        Map<String, String> param = getParam("testVerifyThePriceUsingCurrencyNonIDRNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String currency = param.get("currency");

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User login as guest");
        loginPage.loginAsGuest();
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
            lang = homePage.getLanguageApp();
        }

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("User go to selected page module");
        boolean needLogin = false;
        String[] targetModulesTemplate = new String[] {"IMAGE_WITH_CHIPS","HIGHLIGHT_DISCOVER_PRODUCT","FLASH_SALE"};
        String[] targetModuleNames = homePage.getHomeModules(needLogin, "-", "-", targetModulesTemplate,lang,currency);

        step("Verify Step");
        Assertion.assertThat("Each page module is showing "+currency+" Currency", homePage.isCurrencyOnPageModuleCorrect(targetModuleNames,currency), is(true));
    }

    @Author(names = { "Kartik" })
    @Module(name = "PageModule")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testHighlightDiscoverModuleLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testHighlightDiscoverModuleLogin");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<?> accountPage = AccountPage.getInstance(session);

        String loginType = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String currency = testProperties.get("currency");


        BaseTest.paramsThreadLocal.set(testProperties);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        accountPage.chooseCountry("Indonesia");
        homePage.changeCurrency(currency,username);

        step("Get language");
        String lang = homePage.getLanguageApp();

        step("Request data for available promo modules through api");
        String testHighlightDiscoverModule = homePage.getHomeModuleSection(username, password, "HIGHLIGHT_DISCOVER_PRODUCT", lang, "", currency);

        step("check the HighLight Discover section on UI");
        boolean HighLightDiscoverModule = homePage.checkHighLightDiscoverModule(testHighlightDiscoverModule);
        boolean HighLightDiscoverProductName = homePage.checkHighLightDiscoverModuleProduct(testHighlightDiscoverModule);
        boolean HighLightDiscoverLocationName = homePage.checkHighLightDiscoverModuleLocation(testHighlightDiscoverModule);
        boolean HighLightDiscoverPrice = homePage.checkHighLightDiscoverModulePrice(testHighlightDiscoverModule);

        Assertion.assertThat("Verify HighLight Discover section on UI", HighLightDiscoverModule, is(true));
        Assertion.assertThat("Verify HighLight Discover section product Name on UI", HighLightDiscoverProductName, is(true));
        Assertion.assertThat("Verify HighLight Discover section product Location on UI", HighLightDiscoverLocationName, is(true));
        Assertion.assertThat("Verify HighLight Discover section product Price on UI", HighLightDiscoverPrice, is(true));
        Assertion.assertThat("Verify HighLight Discover image clickable and info card redirect and scroll", homePage.verifyDiscoverModuleProductRedirection(testHighlightDiscoverModule), is(true));
    }

    @Author(names = { "Kartik" })
    @Module(name = "PageModule")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testHighlightDiscoverModuleGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testHighlightDiscoverModuleGuest");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String currency = testProperties.get("currency");
        BaseTest.paramsThreadLocal.set(testProperties);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User open app as login user");
        loginPage.loginAsGuest();

        step("Get language");
        String lang = homePage.getLanguageApp();

        step("Request data for available promo modules through api");
        String testHighlightDiscoverModule = homePage.getHomeModuleSection(username, password, "HIGHLIGHT_DISCOVER_PRODUCT", lang, "",currency);

        step("check the HighLight Discover section on UI");
        boolean HighLightDiscoverModule = homePage.checkHighLightDiscoverModule(testHighlightDiscoverModule);
        boolean HighLightDiscoverReview = homePage. checkHighLightDiscoverModuleReview(testHighlightDiscoverModule);
        boolean HighLightDiscoverProductName = homePage.checkHighLightDiscoverModuleProduct(testHighlightDiscoverModule);
        boolean HighLightDiscoverLocationName = homePage.checkHighLightDiscoverModuleLocation(testHighlightDiscoverModule);
        boolean HighLightDiscoverPrice = homePage.checkHighLightDiscoverModulePrice(testHighlightDiscoverModule);

        Assertion.assertThat("Verify HighLight Discover section on UI Title , label, description, icon", HighLightDiscoverModule, is(true));
        Assertion.assertThat("Verify HighLight Discover section product Name on UI", HighLightDiscoverProductName, is(true));
        Assertion.assertThat("Verify HighLight Discover section product Location on UI", HighLightDiscoverLocationName, is(true));
        Assertion.assertThat("Verify HighLight Discover section product Price on UI", HighLightDiscoverPrice, is(true));
        Assertion.assertThat("Verify HighLight Discover section product Review on UI", HighLightDiscoverReview, is(true));
        Assertion.assertThat("Verify HighLight Discover image CLICKABLE and info card redirect and SWIPE", homePage.verifyDiscoverModuleProductRedirection(testHighlightDiscoverModule), is(true));
        Assertion.assertThat("Verify HighLight Discover section product Rating on UI", homePage.checkHighLightDiscoverModuleRating(testHighlightDiscoverModule), is(true));

    }

    @Author(names = {"Prerna","Mridul Gupta"})
    @Module(name = "Finance Bar")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testLoginVerifyFinanceBarIDR(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testLoginVerifyFinanceBarIDR");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        String loginType = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");

        String currency = "IDR";

        BaseTest.paramsThreadLocal.set(testProperties);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User open app as login user");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Change currency "+currency);
        homePage.changeCurrency(currency,username);

        step("Check the Finance Bar sections are appearing");
        Assertion.assertThat("Verify finance bar paylater section appear", homePage.isPayLaterSectionAppear(), is(true));
        Assertion.assertThat("Verify finance bar Blipay section appear", homePage.isBlipaySectionAppear(), is(true));

        step("Verify redirection on blibli tiket point section");
        Assertion.assertThat("Verify redirection on blibli tiket point section", homePage.verifyTiketPointRedirection(), is(true));

        step("Verify redirection on paylater section");
        Assertion.assertThat("Verify redirection on paylater section", homePage.verifyPayLaterRedirection(), is(true));

        step("Verify redirection on blipay section");
        Assertion.assertThat("Verify redirection on blipay section", homePage.verifyBlipayRedirection(), is(true));
    }

    @Author(names = { "Zia" })
    @Module(name = "PageModule")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testHighlightDiscoverArticleModuleLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testHighlightDiscoverArticleModuleLogin");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String currency = testProperties.get("currency");

        BaseTest.paramsThreadLocal.set(testProperties);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);
        accountPage.chooseCountry("Indonesia");
        String lang = homePage.getLanguageApp();

        if (!lang.equalsIgnoreCase("en")) {
            step("Change Language to 'ENG'");
            homePage.changeLanguageUsingDeeplink("ENG");
            lang = homePage.getLanguageApp();
        }

        step("Get the data from the API");
        HighlightDiscoverPOJO highlightArticleSection = homePage.getHighlightDiscoverArticleSection(true, username, password, lang,currency);
        String moduleTitle = highlightArticleSection.moduleTitle;

        step("Swipe to the HighLight Discover section on UI");
        homePage.swipeToCard(moduleTitle);

        step("Verify module is correct");
        Assertion.assertThat("Verify HighLight Discover Article Title, Description, Icon", homePage.verifyHighlightDiscoverArticleTitle(highlightArticleSection), is(true));
        Assertion.assertThat("Verify HighLight Discover Article is swipeable", homePage.verifySwipeHighlightDiscoverArticle(highlightArticleSection), is(true));
        Assertion.assertThat("Verify HighLight Discover Article section text correct and button/image is clickable", homePage.verifyClickHighlightDiscoverArticle(highlightArticleSection), is(true));
    }

    @Author(names = { "Zia" })
    @Module(name = "PageModule")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testHighlightDiscoverArticleModuleGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testHighlightDiscoverArticleModuleGuest");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        BaseTest.paramsThreadLocal.set(testProperties);

        String currency = testProperties.get("currency");

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User login as guest");
        loginPage.loginAsGuest();
        homePage.changeCurrency(currency,"");
        String lang = homePage.getLanguageApp();

        if(!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.changeLanguageUsingDeeplink("IDN");
            lang = homePage.getLanguageApp();
        }

        step("Get the data from the API");
        HighlightDiscoverPOJO highlightArticleSection = homePage.getHighlightDiscoverArticleSection(false, "", "", lang,currency);
        String moduleTitle = highlightArticleSection.moduleTitle;

        step("Swipe to the HighLight Discover section on UI");
        homePage.swipeToCard(moduleTitle);

        step("Verify module is correct");
        Assertion.assertThat("Verify HighLight Discover Article Title, Description, Icon", homePage.verifyHighlightDiscoverArticleTitle(highlightArticleSection), is(true));
        Assertion.assertThat("Verify HighLight Discover Article is swipeable", homePage.verifySwipeHighlightDiscoverArticle(highlightArticleSection), is(true));
        Assertion.assertThat("Verify HighLight Discover Article section text correct and button/image is clickable", homePage.verifyClickHighlightDiscoverArticle(highlightArticleSection), is(true));
    }

    @Author(names = { "Ashish" })
    @Module(name = "Notification")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testUserClickNotificationMenu() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> testProperties = getParam("testUserClickNotificationMenu");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String loginType = testProperties.get("loginType");
        String username = testProperties.get("username");
        String password = testProperties.get("password");
        String currency = testProperties.get("currency");

        BaseTest.paramsThreadLocal.set(testProperties);

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("User open app as login user");
        loginPage.loginWith(loginType, username, password);

        step("Change currency "+currency);
        homePage.changeCurrency(currency,username);

        step("open account page");
        homePage.openAccount();

        step("open notification page");
        settingPage.openNotificationSettingPage();

        step("verify we are on menu notification settings");
        Assertion.assertThat("is on notification settings menu",settingPage.isOnNotificationMenu(),is(true));

    }

    @Author(names = {"Ashish"})
    @Module(name = "UTIL - DISCOVERY")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyTheContinuePaymentModuleHomePage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyTheContinuePaymentModuleHomePage");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<?> accountPage = AccountPage.getInstance(session);
        SearchPageCarRental<WebDriver> carRentalSearchPage = SearchPageCarRental.getInstance(session);
        SearchResultPageCarRental<WebDriver> carRentalSearchResultPage = SearchResultPageCarRental.getInstance(session);
        CarRentalReviewBookingPage<WebDriver> carRentalReviewBookingPage = CarRentalReviewBookingPage.getInstance(session);
        CarRentalBookingPage<WebDriver> carRentalBookingPage = CarRentalBookingPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        String Location = param.get("location");
        String pickupLocation = param.get("pickupLocation");
        String carRentalDuration = param.get("carRentalDuration");
        String carRentalPackage = param.get("carRentalPackage");
        String dateRental = CoreFunctions.getCurrentLongDate();

        String currency = param.get("currency");

        step("user open app");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);
        accountPage.chooseCountry("Indonesia");

        step("Open Car Landing Page");
        homePage.openVerticalPageDeeplink("Car Rentals","",currency);
        carRentalSearchPage.closeInformationPopUp();
        if (Configs.PLATFORM == Platform.IOS) {
            carRentalSearchPage.allowLocationAccess();
        }

        step("Select car rental type");
        carRentalSearchPage.chooseDriver("with_driver");

        step("Select car rental date");
        carRentalSearchPage.selectDate(CoreFunctions.addToLongDate(dateRental,7));

        step("Input car rental location");
        carRentalSearchPage.chooseLocation(Location);

        step("Searh car rental");
        carRentalSearchPage.searchCar();

        step("choose car");
        carRentalSearchResultPage.chooseCar();

        step("continue to booking form from review page");
        carRentalReviewBookingPage.continueFromReviewDetailCar();
        if (Configs.PLATFORM == Platform.IOS) {
            carRentalSearchPage.allowLocationAccess();
        }

        step("pick up location and set drop-off");
        carRentalBookingPage.choosePickUpLocationAndToggleForDropOff(pickupLocation);

        step("continue to complete booking");
        carRentalBookingPage.continueToCompleteBookingPage();
        String productName = carRentalBookingPage.getVehicleNameFromBookingSummary();
        String totalPrice = carRentalBookingPage.getTotalPriceFromBookingSummary();

        step("continue to payment");
        carRentalBookingPage.setContinueToPayment();

        step("get order id");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("user get order id",!orderID.isEmpty(),is(true));

        step("go to home page");
        homePage.gotoHome();

        step("verify continue payment section");
        homePage.verifyContinueToPaymentSection(productName, totalPrice);

        step("click continue to payment btn and check redirection");
        homePage.clickContinueToPayment();
        String orderId = paymentPage.checkOrderID();
        Assertion.assertThat("is open correct payment order",orderId.contains(orderID),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "UTIL - DISCOVERY")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyTheClaimGvModule() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyTheClaimGvModule");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        VoucherBoxPage<WebDriver> voucherBoxPage = VoucherBoxPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        String loginMethod = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String templateCode = param.get("templateCode");
        String currency = param.get("currency");

        step("user open app");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        loginPage.loginWith(loginMethod,username, password);
        homePage.changeCurrency(currency,username);

        step("Change the Nation to Indonesia");
        homePage.openAccount();
        accountPage.chooseCountry("Indonesia");
        homePage.gotoHome();

        step("Get language");
        String langCode = homePage.getLanguageId();

        step("Get title from api for card with default chips");
        HomePageModule.PageModules pageModules = homePage.getHomeModuleLoginGVClaim(username, password, templateCode,langCode,"IDR");

        step("find GV Claim on home page CTA BTN");
        homePage.swipeToCtaBtn(pageModules.moduleDetail.cta.title);

        step("verify GV Claim section");
        homePage.verifyClaimGVSection(pageModules);

        step("click the voucher");
        homePage.clickGVVoucher(pageModules.dynamicContents);
        Assertion.assertThat("are we redirect to the voucher box page",voucherBoxPage.isVoucherPageOpen(),is(true));
        Assertion.assertThat("is one of the voucher present :", voucherBoxPage.checkRandomVoucherPresence(pageModules.dynamicContents),is(true));

        step("back to home page");
        voucherBoxPage.clickBackBtnVoucherBoxPage();

        step("click cta btn");
        homePage.clickGVCtaBtn(pageModules.moduleDetail.cta.title);
        Assertion.assertThat("are we redirect to the voucher box page",voucherBoxPage.isVoucherPageOpen(),is(true));
    }

    @Author(names = { "Akbar","Mridul" })
    @Module(name = "PageModule")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void verifyUpcomingBookingModuleLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyUpcomingBookingModuleLogin");
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        Unm<WebDriver> unm = new Unm<>(session);

        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String currency = "IDR";

        String hotelName = param.get("hotelName");
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int adult = Integer.parseInt(param.get("adult"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get api access token");
        String apiAccessToken = unm.getAccessToken(username,password,"B2C");

        step("check order present in order");
        String bookedOrderID = "",bookedOrderID2 = "";
        MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrderByVertical("HOTEL", apiAccessToken);
        List<MapVerticalWithOrderId.TitleAndOrderId> filteredHotelOrder = orderListPage.filterOrderByStatus(activeOrderData.hotel,"ETICKET_ISSUED");
        filteredHotelOrder = orderListPage.filterUpcomingBooking(filteredHotelOrder, "HOTEL");

        if(filteredHotelOrder.size() < 2) {
            step("Create hotel issued order for 1 passenger and 1 day via API");
            bookedOrderID = hotelApi.createWaitingPaymentOrderApi(
                    username, password, startDate, hotelName, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle,
                    currency,"id");
            Assertion.assertThat("Success to create waiting payment order", bookedOrderID, is(not(emptyOrNullString())));
            orderListPage.waitForPaymentSuccessful(bookedOrderID,10);

            if(filteredHotelOrder.isEmpty()) {
                step("Create hotel issued order for 1 passenger and 1 day via API");
                startDate = addToDateYYYY_MM_DD(startDate,2);
                bookedOrderID2 = hotelApi.createWaitingPaymentOrderApi(
                        username, password, startDate, hotelName, adult, night, room,
                        orderFullName, orderNameTitle, guestFullName, guestTitle,
                        currency,"id");
                Assertion.assertThat("Success to create waiting payment order", bookedOrderID2, is(not(emptyOrNullString())));
                orderListPage.waitForPaymentSuccessful(bookedOrderID2,10);
            }

            bookedOrderID = String.valueOf(hotelApi.getOrderId());
        } else {
            bookedOrderID = orderListPage.sortByEarliestDeparture(filteredHotelOrder).get(0).orderId;
        }
        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Back to homepage");
        homePage.goToHomeDeeplink();

        step("Go to homepage and swipe to upcoming booking module");
        homePage.swipeToCard("upcoming booking/Pesananmu Berikutnya");

        step("Click see all on upcoming booking module");
        homePage.clickSeeAllUpcomingBooking();

        step("Verify upcoming booking module 1");
        Assertion.assertThat("Upcoming module countdown departure is displayed",homePage.isUpcomingBookingCountdownDisplayed(),is(true));
        Assertion.assertThat("Upcoming module hotel name is displayed",homePage.isUpcomingBookingHotelNameDisplayed(),is(true));
        Assertion.assertThat("Upcoming module hotel checking details is displayed",homePage.checkInDetails(),is(true));

        step("Go back to home page");
        Assertion.assertThat("See all link is appears when having more than 1 order ", homePage.countOfSeeAll(),is(true));
        Assertion.assertThat("Title cross-sell appears ",homePage.isTitleCrossSellAppears(),is(true));

        step("Click upcoming booking module");
        homePage.clickUpcomingBooking();
        
        Assertion.assertThat("Redirected to order detail page",orderDetailPage.getOrderID(),not(emptyOrNullString()));
        homePage.goToHomeDeeplink();
        homePage.swipeToCard("upcoming booking");

        step("When clicking the vertical cross-sell");
        homePage.clickCrossSellCard();
        Assertion.assertThat("Redirected to vertical landing page",homePage.verifyVertical("Flight"),is(true));
    }

    @Author(names = {"Mridul Gupta"})
    @Module(name = "onlineCheckIn")
    @Tribe(name = "Flight")
    @Vertical(name = "Flight")
    @Test
    public void testUpcomingBookingHomePageFlightOrder() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testUpcomingBookingHomePageFlightOrder");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        FlightPage<WebDriver> flightPage = FlightPage.getInstance(session);
        FlightApi<WebDriver> flightApi = new FlightApi<>(session);

        String tripType = param.get("tripType");
        String originAirport = param.get("originAirport");
        String destinationAirport = param.get("destinationAirport");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int adultPassengers = Integer.parseInt(param.get("adult"));
        int childPassengers = Integer.parseInt(param.get("child"));
        int infantPassengers = Integer.parseInt(param.get("infant"));
        String airline = param.get("airline");
        String phoneNumber = param.get("phoneNumber");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        step("Get api access token");
        String apiAccessToken = unm.getAccessToken(username, password,"B2C");

        step("check order present in order");
        MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrder(apiAccessToken);
        if(!activeOrderData.isEmpty && !activeOrderData.flight.isEmpty()){
            Assertion.assertThat("Flight order is already present", activeOrderData.flight.get(0).orderId, is(not(emptyOrNullString())));
        }else {
            step("set date and get depart date in YYYY-MM-DD format");
            String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
            String departureDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToDepartureDate);

            step("complete flight booking via API");
            String[] passengerNames = {CoreFunctions.randomNameWithoutTest() + " Testing"};
            String orderID = flightPage.createOrderFlightB2CDomestic(username, password, originAirport, destinationAirport, departureDate, tripType, adultPassengers, childPassengers, infantPassengers, airline, passengerNames, phoneNumber);
            Assertion.assertThat("Flight order created", orderID, not(""));

            step("get booking code from API");
            String bookingCode = flightApi.getBookingCode(orderID, username);

            step("set Tiket Number on flight dashboard and ticket will be eticket issued");
            Boolean isTicketIssued = flightApi.flightEticketIssuedRequest(orderID, bookingCode, username, passengerNames[0], 15);
            Assertion.assertThat("is Eticket Issued", isTicketIssued, is(true));
        }

        step("Goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink and change currency to IDR");
        homePage.loginWithSpecificAccountViaDeeplink(username, password, "IDR");

        step("Change the Nation to Indonesia");
        homePage.openAccount();
        accountPage.chooseCountry("Indonesia");
        homePage.gotoHome();

        step("verify upcoming booking module appear on home page");
        homePage.refreshHomePage(2);
        homePage.swipeToCard("Upcoming Booking/Pesananmu Berikutnya");

        if(!homePage.isFlightUpcomingBookingDisplayed()){
            step("open upcoming booking module");
            homePage.clickSeeAllUpcomingBooking();
        }

        step("Assertion upcoming Flight booking module appear on home page");
        Assertion.assertThat("Upcoming Flight booking module countdown departure is displayed",homePage.isFlightUpcomingBookingDisplayed(),is(true));
    }

    @Author(names = {"Areeb"})
    @Module(name = "UTIL - DISCOVERY")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testAffiliatorUserRedirectedToTargetPageViaAffiliateIcon() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testAffiliatorUserRedirectedToTargetPageViaAffiliateIcon");
        BaseTest.paramsThreadLocal.set(param);

        var homePage = HomePage.getInstance(session);
        var loginPage = LoginPage.getInstance(session);
        var accountPage = AccountPage.getInstance(session);
        var blibliAffiliatePage = BlibliAffiliatePage.getInstance(session);
        var webviewPage = WebViewPage.getInstance(session);

        String currency = param.get("currency");
        String username = param.get("username");
        String password = param.get("password");

        step("user open app");
        homePage.gotoHome();

        step("Login as " + username + " and change currency to " + currency);
        loginPage.loginAsGuest();
        loginPage.loginWith("email", username, password);
        homePage.changeCurrency(currency, username);
        accountPage.chooseCountry("Indonesia");

        step("Click on Affiliate Icon");
        homePage.gotoHome();
        homePage.closePromoPopup();
        homePage.clickHomeIcon();
        homePage.verticalsPage.openVerticalContainingName("Affiliate");

        step("Verify redirection to target page");
        webviewPage.switchToWebViewUrl("affiliate");
        Assertion.assertThat("User is redirected to target page via affiliate icon", blibliAffiliatePage.checkAffiliateHomePageForAffiliateUser(), is(true));
        webviewPage.switchToNative();
    }

    @Author(names = {"Mridul"})
    @Module(name = "Homepage")
    @Tribe(name = "Grand")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyDynamicWidgetContentAndInfoRedirectionLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        Map<String, String> param = getParam("testVerifyDynamicWidgetContentAndInfoRedirectionLogin");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String preferredWidgetTitle = param.get("widgetTitle");
        String templateCode = "DYNAMIC_WIDGET";
        String currency = "IDR";

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);
        accountPage.chooseCountry("Indonesia");

        step("Get the " + templateCode + " heading from the home module API, preferring '" + preferredWidgetTitle + "'");
        String language = homePage.getLanguageApp();
        String widgetTitle = homePage.getModuleHeadingTitle(true, username, password, language, currency,
                templateCode, preferredWidgetTitle);
        Assertion.assertThat("Dynamic widget title should not be null or empty", widgetTitle, is(not(emptyOrNullString())));

        step("Scroll the home page down to the '" + widgetTitle + "' dynamic widget");
        homePage.goToHomeDeeplink();
        homePage.swipeToDynamicWidget(widgetTitle);

        step("Verify the widget heading is present");
        Assertion.assertThat("Dynamic widget heading should not be null or empty",
                homePage.getDynamicWidgetTitle(widgetTitle), is(not(emptyOrNullString())));

        step("Verify the widget sub title is present");
        Assertion.assertThat("Dynamic widget sub title should not be null or empty",
                homePage.getDynamicWidgetSubtitle(widgetTitle), is(not(emptyOrNullString())));

        step("Verify the widget picture is displayed");
        Assertion.assertThat("Dynamic widget picture should be displayed",
                homePage.isDynamicWidgetImageDisplayed(widgetTitle), is(true));

        step("User clicks the info button on the widget");
        homePage.clickDynamicWidgetInfoButton(widgetTitle);

        step("Verify user is redirected to the info content");
        Assertion.assertThat("Info content should be displayed after clicking the info button",
                homePage.isDynamicWidgetInfoDetailDisplayed(), is(true));
        Assertion.assertThat("Home page should no longer be shown", homePage.isOnHomePage(), is(false));
    }

    @Author(names = {"Mridul"})
    @Module(name = "PageModule")
    @Tribe(name = "Discovery")
    @Vertical(name = "Platform")
    @Test
    public void testVerifyAccommodationRecommendationPriceOnCurrencySwitchLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        Map<String, String> param = getParam("testVerifyAccommodationRecommendationPriceOnCurrencySwitchLogin");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String templateCode = "IMAGE_WITH_CHIPS";
        // The accommodation recommendation module is the one whose chips sell hotels; its heading is
        // campaign copy and changes per environment, so it is never hardcoded here.
        String chipProductType = "TIXHOTEL";
        // Tiebreaker only, matched as a prefix so the model version bump (_v8 -> _v9) is harmless.
        String variantNamePrefix = "neoPolaris";
        String baseCurrency = "IDR";
        String switchedCurrency = "NZD";

        step("User goto home");
        homePage.gotoHome();
        homePage.clickHomeIcon();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + baseCurrency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, baseCurrency);
        accountPage.chooseCountry("Indonesia");

        step("Get the accommodation recommendation heading from the home module API");
        String language = homePage.getLanguageApp();
        String titleSection = homePage.getModuleHeadingByChipProductType(true, username, password, language,
                baseCurrency, templateCode, chipProductType, variantNamePrefix);
        Assertion.assertThat("Recommendation section heading should not be null or empty", titleSection, is(not(emptyOrNullString())));

        step("Open the landing page in " + baseCurrency + " and scroll to the '" + titleSection + "' section");
        homePage.goToHomeDeeplink();
        homePage.swipeToCard(titleSection);

        step("Record the product name and its rate in " + baseCurrency);
        // Every later visit reads the price of this very product, so the amounts stay comparable
        // even though the carousel holds several cards.
        String productName = homePage.getSectionInventoryName(titleSection);
        Assertion.assertThat("Recommendation product name should not be null or empty", productName, is(not(emptyOrNullString())));
        String baseCurrencyPrice = homePage.getInventoryPrice(titleSection, productName);
        Assertion.assertThat("Rate of '" + productName + "' should be shown in " + baseCurrency, homePage.getCurrencyFromPrice(baseCurrencyPrice), is(equalTo(baseCurrency)));

        step("Reopen the landing page with currency " + switchedCurrency + " and record the rate");
        homePage.changeCurrency(switchedCurrency, username);
        homePage.goToHomeDeeplink();
        homePage.swipeToCard(titleSection);
        String switchedCurrencyPrice = homePage.getInventoryPrice(titleSection, productName);
        Assertion.assertThat("Rate of '" + productName + "' should be shown in " + switchedCurrency,
                homePage.getCurrencyFromPrice(switchedCurrencyPrice), is(equalTo(switchedCurrency)));

        step("Verify the " + baseCurrency + " and " + switchedCurrency + " rates are not the same");
        Assertion.assertThat("Rate of '" + productName + "' in " + switchedCurrency + " should differ from the "
                + baseCurrency + " rate", switchedCurrencyPrice, is(not(equalTo(baseCurrencyPrice))));

        step("Reopen the landing page back in " + baseCurrency + " and match with the recorded rate");
        homePage.changeCurrency(baseCurrency, username);
        homePage.goToHomeDeeplink();
        homePage.swipeToCard(titleSection);
        String baseCurrencyPriceAgain = homePage.getInventoryPrice(titleSection, productName);
        Assertion.assertThat("Rate of '" + productName + "' should be shown in " + baseCurrency + " again",
                homePage.getCurrencyFromPrice(baseCurrencyPriceAgain), is(equalTo(baseCurrency)));
        Assertion.assertThat("Rate of '" + productName + "' should match the " + baseCurrency + " rate recorded "
                + "before the currency switch", baseCurrencyPriceAgain, is(equalTo(baseCurrencyPrice)));

        step("Reopen the landing page in " + switchedCurrency + " again and match with the recorded rate");
        homePage.changeCurrency(switchedCurrency, username);
        homePage.goToHomeDeeplink();
        homePage.swipeToCard(titleSection);
        String switchedCurrencyPriceAgain = homePage.getInventoryPrice(titleSection, productName);
        Assertion.assertThat("Rate of '" + productName + "' should be shown in " + switchedCurrency + " again",
                homePage.getCurrencyFromPrice(switchedCurrencyPriceAgain), is(equalTo(switchedCurrency)));
        Assertion.assertThat("Rate of '" + productName + "' should match the " + switchedCurrency + " rate recorded "
                + "on the first switch", switchedCurrencyPriceAgain, is(equalTo(switchedCurrencyPrice)));
    }
}
