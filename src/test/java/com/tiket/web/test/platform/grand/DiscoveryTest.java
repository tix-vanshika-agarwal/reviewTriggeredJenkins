package com.tiket.web.test.platform.grand;

import com.tiket.annotation.Author;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.dashboard.page.platform.discovery.PopupSettingPage;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.platform.eiffel.membership.AccountPageWeb;
import com.tiket.web.page.platform.grand.PromoPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import com.tiket.annotation.Module;
import javax.security.auth.login.FailedLoginException;
import java.util.Map;
import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class DiscoveryTest extends BaseTest {

    @Author(names = { "DendiH" , "Yoan" })
    @Vertical(name = "Grand")
    @Tribe(name = "Discovery")
    @Module(name = "PromoPage")
    @Test
    public void checkPromoListNonLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPromoListNonLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        PromoPageWeb<?> promoPageWeb = PromoPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Logout via URL");
        loginPage.logoutViaUrl();

        step("Click See All Promo Button");
        homePage.clickPromoListHeaderEntrypoint();

        step("Check the promo list page is appearing successfully or not");
        Assertion.assertThat("Promo list page is visible", promoPageWeb.isPromoPageVisible(), is(true));

        step("Verify login button in voucher box is appeared");
        Assertion.assertThat("Login button in voucher box should be appeared", promoPageWeb.isLoginButtoninPromoPageAppear(), is(true));

        step("Verify promo banner is showing");
        Assertion.assertThat("Promo banner is showing", promoPageWeb.isPromoBannerVisible(), is(true));

    }

    @Author(names = {"Mridul"})
    @Vertical(name = "Grand")
    @Tribe(name = "Discovery")
    @Module(name = "PromoPage")
    @Test
    public void checkGlobalSearchWithoutKeyword() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkGlobalSearchWithoutKeyword");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);

        step("Given user open homepage app");
        homePageWeb.goToHomePage();

        step("Change country to Indonesia");
        homePageWeb.changeCountryToIndonesia();

        step("Click on search input");
        homePageWeb.clickSearchColumn();

        step("Check GS_Info section appears");
        Assertion.assertThat("All GS_INFO assertions", homePageWeb.verifyContentGS_Info(), is(true));

        step("Check Popular section appears");
        Assertion.assertThat("All Popular section assertions", homePageWeb.verifyContentPopular(), is(true));

        step("Check Inspired section appears");
        Assertion.assertThat("All Inspired section assertions", homePageWeb.verifyContentInspired(), is(true));

    }

    @Author(names = {"sumit"})
    @Tribe(name = "Discovery")
    @Module(name = "HomePage")
    @Vertical(name = "Grand")
    @Test
    public void testLoginGlobalSearchWithVerticalKeywordWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginGlobalSearchWithVerticalKeywordWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        homePage.setSheetData(param);
        String email = param.get("email");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        String password = param.get("password");
        String keywords = param.get("keywords");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Go to tiket homepage");
        homePage.goToHomePage();

        step("Change country to Indonesia");
        homePage.changeCountryToIndonesia();

        step("User click global search");
        homePage.clickSearchColumn();

        step("User input keywords " + keywords + " global search");
        homePage.inputVerticalKeywords();

        step("User verify vertical icon appears redirect correctly");
        homePage.verifyVerticalIconText(keywords);
        homePage.verticalRedirectionFromGS(keywords);
    }

    @Author(names = {"Ankit, Fandy, Shailja"})
    @Vertical(name = "Grand")
    @Tribe(name = "Discovery")
    @Module(name = "HomePage")
    @Test
    public void checkHomeIconLogin() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkHomeIconLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String vertical1 = param.get("vertical1");
        String vertical2 = param.get("vertical2");
        String vertical3 = param.get("vertical3");
        String vertical4 = param.get("vertical4");
        String vertical5 = param.get("vertical5");

        step("Go to home");
        homePageWeb.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPageWeb.loginWithSpecificAccountViaCookie(username, password);

        step("Ensure country is Indonesia");
        homePageWeb.changeCountryToIndonesia();

        step("Ensure language is Bahasa");
        if (!"id".equalsIgnoreCase(homePageWeb.getLanguage())) {
            homePageWeb.selectBahasaLanguage();
        }

        step("Change currency");
        homePageWeb.selectCurrency("IDR");

        step("Go to Homepage again if mweb");
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            homePageWeb.goToHomePage();
        }

        step("Check Vertical Icon are Appeared");
        boolean isVerticalIconAppeared = homePageWeb.verifyHomeIconImage();
        Assertion.assertThat("Verify All Verticals Icons are Appeared", isVerticalIconAppeared, is(true));
        homePageWeb.goToHomePage();

        step("Check Vertical Icon Title are Appeared");
        boolean isVerticalTitleAppeared = homePageWeb.verifyHomeIconTitle();
        Assertion.assertThat("Verify All Verticals Icon Titles are Appeared", isVerticalTitleAppeared, is(true));
        homePageWeb.goToHomePage();

        step("Verify if the vertical are directed correctly ");
        Assertion.assertThat(vertical1 + " vertical is directed correctly", homePageWeb.verifyVerticalIcons(vertical1), is(true));
        Assertion.assertThat(vertical2 + " vertical is directed correctly", homePageWeb.verifyVerticalIcons(vertical2), is(true));
        Assertion.assertThat(vertical3 + " vertical is directed correctly", homePageWeb.verifyVerticalIcons(vertical3), is(true));
        Assertion.assertThat(vertical4 + " vertical is directed correctly", homePageWeb.verifyVerticalIcons(vertical4), is(true));
        Assertion.assertThat(vertical5 + " vertical is directed correctly", homePageWeb.verifyVerticalIcons(vertical5), is(true));
    }

    @Author(names = {"Ankit"})
    @Vertical(name = "Grand")
    @Tribe(name = "Discovery")
    @Module(name = "HomePage")
    @Test
    public void checkFlashSaleLogin() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkFlashSaleLogin");
        BaseTest.paramsThreadLocal.set(param);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        homePage.setSheetData(param);
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String password = param.get("password");
        String otp = param.get("otp");
        String email = param.get("email");

        step("Go to home");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Change country to Indonesia");
        homePage.changeCountryToIndonesia();

        step("check the FlashSale");
        homePage.flashSaleCheck();

        step("Check the Flash Sale product is clickable and redirected to Hotel page");
        Assertion.assertThat("Flash Sale product redirects to hotel page", homePage.isRedirectedToUrlContaining("/hotel", "/homes"), is(true));

    }

    @Author(names = { "Salimah", "Fandy"})
    @Module(name = "Homepage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testClickSeeAllFlashSale() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testClickSeeAllFlashSale");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        homePage.setSheetData(param);
        String username = param.get("username");
        String password = param.get("password");

        step("User open as logged in user");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Go to tiket homepage");
        homePage.goToHomePage();

        step("Change country to Indonesia");
        homePage.changeCountryToIndonesia();

        step("Verify successfully click");
        homePage.clickSeeAllFlashSale();
    }

    @Author(names = {"Fandy","Dendi", "Yoan"})
    @Vertical(name = "Grand")
    @Tribe(name = "Discovery")
    @Module(name = "HomePage")
    @Test
    public void testLoginPriceUsingCurrencyIDRWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginPriceUsingCurrencyIDRWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String country = param.get("country");
        String currency = param.get("currency");
        String language = param.get("language");
        String iwcModuleId = param.get("IWCModuleId");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Go to tiket homepage");
        homePage.goToHomePage();

        step("Change country, currency, and language");
        homePage.selectCurrencyLanguageAndCountry(country,currency,language);

        step("Verify Image with Chips Contains Currency IDR");
        homePage.verifyIWCCurrencyIDR(iwcModuleId);

        step("Verify Highlight Discovery Product Contains Currency IDR");
        homePage.verifyHDPCurrencyIDR();

        step("Verify Flash Sale Product Contains Currency IDR");
        homePage.verifyFlashSaleCurrencyIDR();
    }

    @Author(names = {"Mridul"})
    @Vertical(name = "Grand")
    @Tribe(name = "Discovery")
    @Module(name = "PromoPage")
    @Test
    public void checkGlobalSearchWithKeyword() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkGlobalSearchWithKeyword");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);

        step("Given user open homepage app");
        homePageWeb.goToHomePage();

        step("Check GS_Info section appears");
        Assertion.assertThat("All GS_INFO assertions", homePageWeb.verifyContentGS_Info(), is(true));

        step("Enter the Vertical Name \"HOTEL\"");
        homePageWeb.enterVerticalName("Hotel");

        step("Check Vertical section appears");
        Assertion.assertThat("All Vertical Section assertions", homePageWeb.verifyVerticalSection(), is(true));

        step("Check Inspired section appears");
        Assertion.assertThat("All Article Section assertions", homePageWeb.verifyArticleSection(), is(true));
    }

    @Author(names = { "Ankit" , "Yoan" })
    @Module(name = "HomePage")
    @Tribe(name = "discovery")
    @Vertical(name = "Platform")
    @Test
    public void loginPromoListWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("loginPromoListWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PromoPageWeb<?> promoPage = PromoPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();

        step("Go to tiket home page");
        homePage.goToHomePage();

        step("Access promo list from header entry point");
        homePage.clickPromoListHeaderEntrypoint();

        step("Check the promo list page is appearing successfully or not");
        Assertion.assertThat("Promo list page is visible", promoPage.isPromoPageVisible(), is(true));

    }

    @Author(names = "Dendi")
    @Module(name = "HomePage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void loginVerifyImageWithChipsWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("loginVerifyImageWithChipsWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String country = param.get("country");
        String currency = param.get("currency");
        String language = param.get("language");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username);
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Select language, country, and currency if not in expected setting");
        homePage.selectCurrencyLanguageAndCountry(country,currency,language);

        step("go to tiket home page");
        homePage.goToHomePage();

        step("find image with chips section");
        String id = param.get("ModuleId");
        homePage.verifyIWCModuleVisible(id);

        step("verify header title");
        boolean isHeaderTitleVisible = homePage.verifyIWCHeaderTitle();
        Assertion.assertThat("Verify Header Title is Visible", isHeaderTitleVisible, is(true));

        step("verify header subtitle");
        boolean isSubtitleHeaderVisible = homePage.verifyIWCHeaderSubtitle();
        Assertion.assertThat("Verify Header subtitle is Visible", isSubtitleHeaderVisible, is(true));

        step("verify the chips are appeared and clickable");
        boolean areChipsClickable = homePage.verifyChipsIWC();
        Assertion.assertThat("Verify chips are clickable", areChipsClickable, is(true));

        step("verify inventory are appeared and clickable");
        boolean areInventoriesClickable = homePage.verifyIWCInventories();
        Assertion.assertThat("Verify Inventories are Clickable", areInventoriesClickable, is(true));

        step("verify iwc image are appeared");
        boolean areIWCImageVisible = homePage.verifyIWCIMG();
        Assertion.assertThat("Verify iwc image are appeared", areIWCImageVisible, is(true));

        step("verify iwc inventory name are appeared and clickable");
        boolean areIWCInventoriesNameVisible = homePage.verifyIWCInventoriesName();
        Assertion.assertThat("Verify iwc image are appeared", areIWCInventoriesNameVisible, is(true));

        step("verify iwc price are appeared");
        boolean areIWCPriceVisible = homePage.verifyIWCPrice();
        Assertion.assertThat("Verify iwc price are appeared", areIWCPriceVisible, is(true));

        step("verify iwc strike price are appeared");
        boolean areIWCStrikePriceVisible = homePage.verifyIWCStrikePrice();
        Assertion.assertThat("Verify iwc strike price are appeared", areIWCStrikePriceVisible, is(true));

        step("verify see all button in IWC are appeared and have url");
        boolean areIWCSeeAllButtonVisibleAndHaveURL = homePage.verifyIWCSeeAllButton();
        Assertion.assertThat("verify see all button in IWC are appeared and have url", areIWCSeeAllButtonVisibleAndHaveURL, is(true));

        step("click inventory in IWC section");
        String expectedIWCName = homePage.getIWCInventoryNameAndClick();

        step("verify the inventory title are same with PDP title");
        String actualIWCPDPName = homePage.getIWCPDPTitle();
        Assertion.assertThat("verify the inventory title are same with PDP title", actualIWCPDPName, is(actualIWCPDPName));
    }

    @Author(names = {"Fandy"})
    @Tribe(name = "Discovery")
    @Module(name = "HomePage")
    @Vertical(name = "Grand")
    @Test
    public void testNonLoginGlobalSearchWithVerticalKeywordWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testNonLoginGlobalSearchWithVerticalKeywordWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        homePageWeb.setSheetData(param);

        step("Given user open homepage");
        homePageWeb.goToHomePage();

        step("Open GS Page and Assert GS Info");
        homePageWeb.enterVerticalName("Hotel");
        Assertion.assertThat("Global Search Info Assertion", homePageWeb.verifyContentGS_Info(), is(true));

        step("Open GS Page and Assert Vertical Section");
        homePageWeb.goToHomePage();
        homePageWeb.enterVerticalName("Hotel");
        Assertion.assertThat("Vertical Section Assertion", homePageWeb.verifyVerticalSection(), is(true));

        step("Open GS Page and Assert Article Section");
        homePageWeb.goToHomePage();
        homePageWeb.enterVerticalName("Hotel");
        Assertion.assertThat("All Article Section assertions", homePageWeb.verifyArticleSection(), is(true));
    }

    @Author(names = {"Fandy", "Dendi", "Yoan"})
    @Module(name = "HomePage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testLoginPriceUsingCurrencyNonIDRWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginPriceUsingCurrencyNonIDRWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String country = param.get("country");
        String currency = param.get("currency");
        String language = param.get("language");

        step("User open home page");
        homePage.goToHomePage();

        step("Login in using {" + username + "} which has preference currency Non IDR");
        loginPage.loginAsGuest();
        loginPage.loginIfNotWithSameEmail(username, password, otp, 1);

        step("Go to tiket homepage");
        homePage.goToHomePage();

        step("Change country and currency to Non IDR");
        homePage.selectCurrencyLanguageAndCountry(country,currency,language);

        step("verify IWC Module is in non IDR");
        homePage.verifyModuleCurrency("IWC", param.get("IWCModuleId"), currency);

        step("verify Flash Sale v2 Module is in non IDR");
        homePage.verifyModuleCurrency("Flash Sale v2", param.get("FSv2ModuleId"), currency);

        step("verify HDP Module is in non IDR");
        homePage.verifyModuleCurrency("HDP", param.get("HDPModuleId"), currency);
    }

    @Author(names = "Ankit")
    @Module(name = "HomePage")
    @Tribe(name = "discovery")
    @Vertical(name = "Platform")
    @Test
    public void testLoginBannerCarousel() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginBannerCarousel");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("go to tiket home page");
        homePage.goToHomePage();

        step("check the Banner Carousel");
        Assertion.assertThat("check the Banner Carousel", homePage.checkBannerCarousel(), is(true));

        step("check the Banner Carousel Title");
        Assertion.assertThat("check the Banner Carousel Title", homePage.checkBannerCarouselTitle(), is(true));

        step("check the Banner Carousel SubTitle");
        Assertion.assertThat("check the Banner Carousel SubTitle", homePage.checkBannerCarouselSubTitle(), is(true));

        step("check the Banner list Check");
        Assertion.assertThat("check the Banner list Check", homePage.checkBannerList(param.get("ModuleId")), is(true));

        step("check the Banner Title inside Banner");
        Assertion.assertThat("check the Banner Title inside Banner", homePage.checkBannerCarouselTitleInside(param.get("ModuleId")), is(true));

        step("check the Banner SubTitle inside Banner");
        Assertion.assertThat("check the Banner SubTitle inside Banner", homePage.checkBannerCarouselSubTitleInside(param.get("ModuleId")), is(true));

        step("check the Banner Scroll Check and clickable");
        Assertion.assertThat("check the Banner Title inside Banner", homePage.checkBannerScroll(param.get("ModuleId")), is(true));

        step("check the Banner Click See ALL");
        homePage.clickSeeAllBannerCarousel(param.get("ModuleId"));

        step("check the banner Carousel click SeeAll redirection ");
        Assertion.assertThat("check the banner Carousel click SeeAll redirection", homePage.bannerCarouselRedirectionCheck(), is(true));

    }

    @Author(names = {"Fandy"})
    @Tribe(name = "Discovery")
    @Module(name = "HomePage")
    @Vertical(name = "Grand")
    @Test
    public void testLoginGlobalSearchWith3LetterKeywordWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testLoginGlobalSearchWith3LetterKeywordWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        homePage.setSheetData(param);

        String email = param.get("email");
        String password = param.get("password");
        String keywords = param.get("keywords");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("Go to tiket homepage");
        homePage.goToHomePage();

        step("User click global search");
        homePage.clickSearchColumn();

        step("User input 3 keywords " + keywords + " global search");
        homePage.enterVerticalName(keywords);

        step("User verify search result appears accordingly");
        homePage.verifyGS_Top_Result(keywords);
        homePage.verifyGS_Info_Result(keywords);
    }

    @Author(names = {"Fandy,Ankit"})
    @Vertical(name = "Grand")
    @Tribe(name = "Discovery")
    @Module(name = "HomePage")
    @Test
    public void checkHomeIconNonLogin() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkHomeIconNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);

        String vertical1 = param.get("vertical1");
        String vertical2 = param.get("vertical2");
        String vertical3 = param.get("vertical3");
        String vertical4 = param.get("vertical4");
        String vertical5 = param.get("vertical5");

        step("Go to home");
        homePageWeb.goToHomePage();

        step("Change currency");
        homePageWeb.selectCurrency("IDR");

        step("Go to Homepage again if mweb");
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            homePageWeb.goToHomePage();
        }

        step("Check Vertical Icon are Appeared");
        boolean isVerticalIconAppeared = homePageWeb.verifyHomeIconImage();
        Assertion.assertThat("Verify All Verticals Icons are Appeared", isVerticalIconAppeared, is(true));

        step("Check Vertical Icon Title are Appeared");
        boolean isVerticalTitleAppeared = homePageWeb.verifyHomeIconTitle();
        Assertion.assertThat("Verify All Verticals Icon Titles are Appeared", isVerticalTitleAppeared, is(true));

        step("Verify if the vertical are directed correctly ");
        Assertion.assertThat(vertical1 + " vertical is directed correctly", homePageWeb.verifyVerticalIcons(vertical1), is(true));
        Assertion.assertThat(vertical2 + " vertical is directed correctly", homePageWeb.verifyVerticalIcons(vertical2), is(true));
        Assertion.assertThat(vertical3 + " vertical is directed correctly", homePageWeb.verifyVerticalIcons(vertical3), is(true));
        Assertion.assertThat(vertical4 + " vertical is directed correctly", homePageWeb.verifyVerticalIcons(vertical4), is(true));
        Assertion.assertThat(vertical5 + " vertical is directed correctly", homePageWeb.verifyVerticalIcons(vertical5), is(true));
    }

    @Author(names = {"Ankit"})
    @Module(name = "Homepage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void TestLoginBannerGrid() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("TestLoginBannerGrid");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("go to tiket home page");
        homePage.goToHomePage();

        step("set to country indonesia");
        homePage.changeCountryToIndonesia();

        step("Verify all contents of Banner Grid");
        Assertion.assertThat("Banner grid content verification failed", homePage.VerifyContentForBannerGrid(param.get("ModuleId")), is(true));
    }

    @Author(names = {"Dendi", "Yoan"})
    @Module(name = "HomePage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void loginVerifyImageWithChipsModuleWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("loginVerifyImageWithChipsModuleWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("go to tiket home page");
        homePage.goToHomePage();

        step("find image with chips section");
        String id = param.get("ModuleId");
        homePage.verifyIWCModuleVisible(id);

        step("verify header title");
        boolean isHeaderTitleVisible = homePage.verifyIWCHeaderTitle();
        Assertion.assertThat("Verify Header Title is Visible", isHeaderTitleVisible, is(true));

        step("verify header subtitle");
        boolean isSubtitleHeaderVisible = homePage.verifyIWCHeaderSubtitle();
        Assertion.assertThat("Verify Header subtitle is Visible", isSubtitleHeaderVisible, is(true));

        step("verify the chips are appeared and clickable");
        boolean areChipsClickable = homePage.verifyChipsIWC();
        Assertion.assertThat("Verify chips are clickable", areChipsClickable, is(true));

        step("Verify inventories appear and are clickable");
        boolean areInventoriesClickable = homePage.verifyIWCInventories();
        Assertion.assertThat("Verify inventories appear and are clickable", areInventoriesClickable, is(true));

        step("verify iwc image are appeared");
        boolean areIWCImageVisible = homePage.verifyIWCIMG();
        Assertion.assertThat("Verify iwc image are appeared", areIWCImageVisible, is(true));

        step("verify iwc inventory name are appeared and clickable");
        boolean areIWCInventoriesNameVisible = homePage.verifyIWCInventoriesName();
        Assertion.assertThat("Verify iwc image are appeared", areIWCInventoriesNameVisible, is(true));

        step("verify iwc price are appeared");
        boolean areIWCPriceVisible = homePage.verifyIWCPrice();
        Assertion.assertThat("Verify iwc price are appeared", areIWCPriceVisible, is(true));

        step("verify iwc strike price are appeared");
        boolean areIWCStrikePriceVisible = homePage.verifyIWCStrikePrice();
        Assertion.assertThat("Verify iwc strike price are appeared", areIWCStrikePriceVisible, is(true));

        step("verify see all button in IWC are appeared and have url");
        boolean areIWCSeeAllButtonVisibleAndHaveURL = homePage.verifyIWCSeeAllButton();
        Assertion.assertThat("verify see all button in IWC are appeared and have url", areIWCSeeAllButtonVisibleAndHaveURL, is(true));

        step("click inventory in IWC section");
        String expectedIWCName = homePage.getIWCInventoryNameAndClick();

        step("verify the inventory title are same with PDP title");
        String actualIWCPDPName = homePage.getIWCPDPTitle();
        Assertion.assertThat("verify the inventory title are same with PDP title", expectedIWCName, is(actualIWCPDPName));
    }

    @Author(names = {"Fandy"})
    @Module(name = "HomePage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void loginVerifyBannerCarouselV2ModuleWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("loginVerifyBannerCarouselV2ModuleWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Go to tiket home page");
        homePage.goToHomePage();

        step("Find banner carousel section");
        String moduleId = param.get("ModuleId");
        homePage.verifyBannerCarouselModuleVisible(moduleId);

        step("Verify header banner carousel title");
        boolean isHeaderTitleVisible = homePage.verifyBannerCarouselHeaderTitle(moduleId);
        Assertion.assertThat("Verify Header Title is Visible", isHeaderTitleVisible, is(true));

        step("Verify header banner carousel subtitle");
        boolean isSubtitleHeaderVisible = homePage.verifyBannerCarouselHeaderSubtitle(moduleId);
        Assertion.assertThat("Verify Header subtitle is Visible", isSubtitleHeaderVisible, is(true));

        step("Verify the chips banner carousel are appeared and clickable");
        boolean areChipsClickable = homePage.verifyChipsBannerCarousel(moduleId);
        Assertion.assertThat("Verify chips are clickable", areChipsClickable, is(true));

        step("Verify banner list are appeared and clickable");
        boolean areBannerClickable = homePage.verifyBannerCarouselList(moduleId);
        Assertion.assertThat("Verify Banner List are Clickable", areBannerClickable, is(true));

        step("Verify banner images are appeared");
        boolean areBannerCarouselImageVisible = homePage.verifyBannerCarouselImage(moduleId);
        Assertion.assertThat("Verify banner images are appeared", areBannerCarouselImageVisible, is(true));

        step("Verify banner carousel title cards are appeared and clickable");
        boolean areBannerCarouselTitleCardVisible = homePage.verifyBannerCarouselTitleCards(moduleId);
        Assertion.assertThat("Verify banner carousel title are appeared", areBannerCarouselTitleCardVisible, is(true));

        step("Verify banner carousel subtitle cards are appeared and clickable");
        boolean areBannerCarouselSubtitleCardVisible = homePage.verifyBannerCarouselSubtitleCards(moduleId);
        Assertion.assertThat("Verify banner carousel subtitle are appeared", areBannerCarouselSubtitleCardVisible, is(true));

        step("Verify see all button in Banner Carousel V2 are appeared and have url");
        boolean areSeeAllButtonVisibleAndHaveURL = homePage.verifyBannerCarouselV2SeeAllButton(moduleId);
        Assertion.assertThat("verify see all button in BCv2 are appeared and have url", areSeeAllButtonVisibleAndHaveURL, is(true));

        step("Click another chips");
        homePage.clickAnotherBCChips(moduleId);

        step("Retrieve URL from the card & Click");
        String expectedBannerCarouselURL = homePage.getBannerCarouselURLAndClick(moduleId);

        step("Verify redirection after click");
        String actualBannerCarouselURL = homePage.bannerCarouselRedirectUrl();
        Assertion.assertThat("Verify the banner carousel URL are same with redirection page URL ", expectedBannerCarouselURL, containsString(actualBannerCarouselURL));
    }

    @Author(names = {"Fandy"})
    @Module(name = "HomePage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void loginVerifyBannerGridModuleWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("loginVerifyBannerGridModuleWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String moduleId = param.get("moduleId");

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Go to tiket home page");
        homePage.goToHomePage();

        step("Verify Banner Grid Visible");
        homePage.verifyBannerGridModuleVisible(moduleId);

        step("Verify Banner Grid Title Header");
        boolean isBannerGridTitleHeaderVisible = homePage.verifyBannerGridTitleHeader(moduleId);
        Assertion.assertThat("Verify header title visible", isBannerGridTitleHeaderVisible, is(true));

        step("Verify Banner Grid Subtitle Header");
        boolean isBannerGridSubtitleHeaderVisible = homePage.verifyBannerGridSubtitleHeader(moduleId);
        Assertion.assertThat("Verify header subtitle visible", isBannerGridSubtitleHeaderVisible, is(true));

        step("Verify Banner Grid Chips are Appeared & Clickable");
        boolean isBannerGridChipsVisible = homePage.verifyBannerGridChips(moduleId);
        Assertion.assertThat("Verify chips are visible and clickable", isBannerGridChipsVisible, is(true));

        step("Verify Banner Grid List");
        boolean isBannerGridListVisibleAndClickable = homePage.verifyBannerGridList(moduleId);
        Assertion.assertThat("Verify list of banner grid is visible and clickable", isBannerGridListVisibleAndClickable, is(true));

        step("Verify Banner Grid Title Card");
        boolean isBannerGridCardTitleVisible = homePage.verifyBannerGridCardsTitle(moduleId);
        Assertion.assertThat("Verify banner grid card title is visible", isBannerGridCardTitleVisible, is(true));

        step("Verify Banner Grid Subtitle Card");
        boolean isBannerGridCardSubtitleVisible = homePage.verifyBannerGridCardsSubtitle(moduleId);
        Assertion.assertThat("Verify banner grid card subtitle is visible", isBannerGridCardSubtitleVisible, is(true));

        step("Verify Banner Grid See All Button");
        boolean isBannerGridSeeAllButtonVisibleAndClickable = homePage.verifyBannerGridSeeAllButton(moduleId);
        Assertion.assertThat("Verify banner grid see all button are visible & clickable", isBannerGridSeeAllButtonVisibleAndClickable, is(true));

        step("Click another chips");
        homePage.clickAnotherBannerGridChips(moduleId);

        step("Retrieve URL from Banner Grid Card & Click");
        String expectedBannerGridURL = homePage.getBannerGridURLAndClick(moduleId);

        step("Verify redirection after click");
        String actualBannerGridURL = homePage.bannerCarouselRedirectUrl(); //BannerCarouselMethod can be reused
        Assertion.assertThat("Verify the banner grid URL are same with redirection page URL", actualBannerGridURL, containsString(expectedBannerGridURL));
    }

    @Author(names = {"Fandy"})
    @Module(name = "HomePage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void loginVerifyHDPWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("loginVerifyHDPWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("Go to tiket home page");
        homePage.goToHomePage();

        step("Login as " + username);
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        step("Change country to Indonesia");
        homePage.changeCountryToIndonesia();

        step("Go to tiket home page");
        homePage.goToHomePage();

        step("Close the pop up if it appears");
        homePage.closeModalPopUpIfAppears();

        step("Verify Highlight Discovery Product Section Appear");
        String moduleId = param.get("ModuleId");
        homePage.verifyHDPVisible(moduleId);

        step("Verify HDP Label is Appearing");
        boolean isHeaderLabelVisible = homePage.verifyHDPHeaderLabel(moduleId);
        Assertion.assertThat("Verify Header Label is Visible", isHeaderLabelVisible, is(true));

        step("Verify HDP Title is Appearing");
        boolean isHeaderTitleVisible = homePage.verifyHDPHeaderTitle(moduleId);
        Assertion.assertThat("Verify Header Title is Visible", isHeaderTitleVisible, is(true));

        step("Verify HDP Subtitle is Appearing");
        boolean isHeaderSubtitleVisible = homePage.verifyHDPHeaderSubtitle(moduleId);
        Assertion.assertThat("Verify Header Title is Visible", isHeaderSubtitleVisible, is(true));

        step("Verify HDP Image is Appearing");
        boolean isHDPImageAppearing = homePage.verifyHDPProductImage(moduleId);
        Assertion.assertThat("Verify Product Image is Visible", isHDPImageAppearing, is(true));

        step("Verify HDP Inventory are appears and clickable");
        boolean areHDPInventoriesVisible = homePage.verifyHDPInventories(moduleId);
        Assertion.assertThat("Verify HDP Inventories are appears & clickable", areHDPInventoriesVisible, is(true));

        step("Verify HDP Inventory name is appeared clickable");
        boolean areHDPInventoriesNameVisible = homePage.verifyHDPInventoryName(moduleId);
        Assertion.assertThat("Verify HDP inventory name are visible", areHDPInventoriesNameVisible, is(true));

        step("Verify HDP Inventory Location are appears and clickable");
        boolean areHDPInventoriesLocationVisible = homePage.verifyHDPInventoryLocation(moduleId);
        Assertion.assertThat("Verify HDP inventory location are visible", areHDPInventoriesLocationVisible, is(true));

        step("Verify HDP Inventory price are appears");
        boolean areHDPPriceVisible = homePage.verifyHDPPrice(moduleId);
        Assertion.assertThat("Verify HDP price are appeared", areHDPPriceVisible, is(true));

        step("Verify HDP Strike price are appears");
        boolean areHDPStrikePriceVisible = homePage.verifyHDPStrikePrice(moduleId);
        Assertion.assertThat("Verify HDP strike price are appeared", areHDPStrikePriceVisible, is(true));

    }

    @Author(names = {"Sumit"})
    @Vertical(name = "Grand")
    @Tribe(name = "Discovery")
    @Module(name = "Platform")
    @Test
    public void checkPromoListDetailNonLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkPromoListDetailNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        PromoPageWeb<?> promoPageWeb = PromoPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("Go to home as non login");
        homePageWeb.goToHomePage();

        step("Logout via URL");
        loginPage.logoutViaUrl();

        step("Change currency");
        homePageWeb.selectCurrency("IDR");

        step("Select english language if not in english");
        homePageWeb.selectEnglishLanguageOnly();

        step("go to tiket home page");
        homePageWeb.goToHomePage();

        step("Access promo list from header entry point");
        homePageWeb.clickPromoListHeaderEntrypoint();

        step("Check the promo list page is appearing successfully or not");
        Assertion.assertThat("Promo Page Should be Opened", promoPageWeb.isPromoPageVisible(), is(true));

        step("check the promo detail");
        promoPageWeb.clickPromoDetailPage();

        step("Check the promo detail page is appearing successfully or not");
        Assertion.assertThat("Promo detail page is visible", promoPageWeb.checkPromoDetailPage(), is(true));
    }

    @Author(names = { "Sumit" , "Yoan"})
    @Vertical(name = "Grand")
    @Tribe(name = "Discovery")
    @Module(name = "Platform")
    @Test
    public void checkPromoListDetailLogin() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkPromoListDetailLogin");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        PromoPageWeb<?> promoPageWeb = PromoPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String country = param.get("country");
        String currency = param.get("currency");
        String language = param.get("language");

        step("Go to home");
        homePageWeb.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("go to tiket home page");
        homePageWeb.goToHomePage();

        step("go to tiket home page");
        homePageWeb.goToHomePage();

        step("Change currency, language, and country if not in expected setting");
        homePageWeb.selectCurrencyLanguageAndCountry(country,currency,language);


        step("Access promo list from header entry point");
        homePageWeb.clickPromoListHeaderEntrypoint();

        step("Check the promo list page is appearing successfully or not");
        Assertion.assertThat("Promo list page is visible", promoPageWeb.isPromoPageVisible(), is(true));

        step("check the promo detail");
        promoPageWeb.clickPromoDetailPage();

        step("Check the promo detail page is appearing successfully or not");
        Assertion.assertThat("Promo detail page is visible", promoPageWeb.checkPromoDetailPage(), is(true));
    }

    @Author(names = {"Ankit"})
    @Module(name = "Homepage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void ClickSeeAllFlashSale() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("ClickSeeAllFlashSale");
        BaseTest.paramsThreadLocal.set(param);

        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String password = param.get("password");
        String otp = param.get("otp");
        String email = param.get("email");

        step("Go to home");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Go to tiket homepage");
        homePage.goToHomePage();

        step("Verify successfully click");
        homePage.clickSeeAllFlashSale();
    }

    @Author(names = {"Fandy"})
    @Module(name = "Calendar")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testVerifyCalendarHolidayWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testVerifyCalendarHolidayWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        String currentLang = param.get("currentLang");
        String langToBeChanged = param.get("langToBeChanged");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginAsGuest();
        loginPage.loginIntoWeb(username, password, otp, 2);

        step("Go to tiket homepage");
        homePage.goToHomePage();

        step("Ensure country is Indonesia");
        homePage.changeCountryToIndonesia();

        step("Get current language");
        String lang = homePage.getLanguage();

        if (!"id".equals(lang)) {
            homePage.selectBahasaLanguage();
        }
        Assertion.assertThat("Current language is ID", homePage.getLanguage(), is(currentLang));

        step("Click one of product icon");
        homePage.clickOneOfProductIcon();

        step("Click date picker");
        homePage.clickDatePicker();

        step("verify description holiday is display");
        Assertion.assertThat("is description holiday display", homePage.isDescriptionHolidayDisplay(), is(true));

        step("close calendar popup");
        homePage.closeCalendar();

        step("Change language to ENG");
        homePage.selectEnglishLanguage();
        Assertion.assertThat("Changed language is EN", homePage.getLanguage(), is(langToBeChanged));

        step("Click date picker");
        homePage.clickDatePicker();

        step("Verify description holiday is display");
        Assertion.assertThat("is description holiday display", homePage.isDescriptionHolidayDisplay(), is(true));
    }

    @Author(names = {"Fandy"})
    @Module(name = "HomePage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testVerifyClaimedGVModuleWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testVerifyClaimedGVModuleWeb");
        BaseTest.paramsThreadLocal.set(sheetData);

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);

        String moduleId = sheetData.get("moduleId");
        homePageWeb.setSheetData(sheetData);

        String username = sheetData.get("username");
        String password = sheetData.get("password");
        String otp = sheetData.get("otp");
        String startDate = generateRandomDate(sheetData.get("rangeAddDate"));
        String query = sheetData.get("hotelName");
        int adult = Integer.parseInt(sheetData.get("adult"));
        int night = Integer.parseInt(sheetData.get("night"));
        int room = Integer.parseInt(sheetData.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];
        String currency = sheetData.get("currency");

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi( username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Login as " + username + " via session injection");
        loginPageWeb.loginWithSpecificAccountViaCookie(username, password);
        String curr = accountPageWeb.getCurrentCurrency();
        if(!curr.equals("IDR")) {
            accountPageWeb.changeCurrency("IDR");
        }

        step("And user go to Home page");
        homePageWeb.goToHomePage();

        step("Verify GV section appears");
        homePageWeb.verifyGVSectionVisible(moduleId);

        step("Verify GV Header Title Appears");
        boolean areGvTitleHeaderAppear = homePageWeb.verifyGVSectionHeaderTitle(moduleId);
        Assertion.assertThat("Verify Header Title Appear", areGvTitleHeaderAppear, is(true));

        step("Verify GV Header Subtitle Appears");
        boolean areGvSubtitleHeaderAppear = homePageWeb.verifyGVSectionHeaderSubtitle(moduleId);
        Assertion.assertThat("Verify Header Subtitle Appear", areGvSubtitleHeaderAppear, is(true));

        step("Verify GV Module Icon Appears");
        boolean areGvIconAppear = homePageWeb.verifyGVIcon(moduleId);
        Assertion.assertThat("Verify GV Icon Appear", areGvIconAppear, is(true));

        step("Verify GV Card Appears");
        boolean areGvCardAppear = homePageWeb.verifyGVCardAppearing(moduleId);
        Assertion.assertThat("Verify GV Cards Carousel Appear", areGvCardAppear, is(true));

        step("Verify GV Vertical Appears");
        boolean areGvCardVerticalAppear = homePageWeb.verifyGVCardVertical(moduleId);
        Assertion.assertThat("Verify GV Card Vertical Appear", areGvCardVerticalAppear, is(true));

        step("Verify GV Card Expiry Date Appears");
        boolean areGvCardExpiryDateAppear = homePageWeb.verifyGVCardExpiryDate(moduleId);
        Assertion.assertThat("Verify GV Card Expiry Date Appear", areGvCardExpiryDateAppear, is(true));

        step("Verify GV Card Claimable Appears");
        boolean areGvCardAppearClaimable = homePageWeb.verifyGVCardClaimable(moduleId);
        Assertion.assertThat("Verify GV Card are Claimable", areGvCardAppearClaimable, is(true));

        step("Verify GV Card Voucher See All Appears");
        boolean areGvCardSeeAllAppearClickable = homePageWeb.verifyGVSectionSeeAllButton(moduleId);
        Assertion.assertThat("Verify GV Card are Claimable", areGvCardSeeAllAppearClickable, is(true));
    }

    @Author(names = { "Abhishek Sharma" })
    @Module(name = "Homepage")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testAddAndEditPopupList() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testAddAndEditPopupList");
        BaseTest.paramsThreadLocal.set(param);

        PopupSettingPage<?> popupSettingPage = new PopupSettingPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String popUpName = generateRandomString(5) + " Popup Name Testing";
        String updatedPopUpName = generateRandomString(5) + " Updated Popup Name Testing";
        String popUpType = param.get("popUpType");
        String popUpCurrency = param.get("popUpCurrency");
        String popUpAvailablePlatform = param.get("popUpAvailablePlatform");
        String screenNameOrPageLink = param.get("screenNameOrPageLink");
        String filePath = param.get("filePath");
        String imageType = param.get("imageType");
        String imageRatio = param.get("imageRatio");
        String imageCTAUrl = param.get("imageCTAUrl");

        step("Login via cookie injection");
        popupSettingPage.goToHomePage(username, password);

        step("Click GET IN button");
        popupSettingPage.clickGetInButton();

        step("Verify home page loaded");
        Assertion.assertThat("Verify home page loaded", popupSettingPage.isHomePageLoaded(), is(true));

        step("Go to Pop up setting page");
        popupSettingPage.clickPopupSettingPageSideMenu();
        Assertion.assertThat("Verify Popup Setting page loaded", popupSettingPage.isPopupSettingPageLoaded(), is(true));

        step("Click Add Popup button");
        popupSettingPage.clickAddPopupButton();
        Assertion.assertThat("Verify New Pop Up page visible", popupSettingPage.isNewPopupPageVisible(), is(true));

        step("Enter popup name: " + popUpName);
        popupSettingPage.setPopupName(popUpName);

        step("Select popup type: " + popUpType);
        popupSettingPage.setPopUpTypeInput(popUpType);

        step("Select popup currency: " + popUpCurrency);
        popupSettingPage.setPopUpCurrencyInput(popUpCurrency);

        step("Select popup show users options: Login and Logout");
        popupSettingPage.selectPopupShowLoginCheckbox();
        popupSettingPage.selectPopupShowLogoutCheckbox();

        step("Select popup available platform: " + popUpAvailablePlatform);
        popupSettingPage.selectPopupAvailablePlatformOptionCheckbox(popUpAvailablePlatform);

        step("Enter popup Screen Name or Page Link: " + screenNameOrPageLink);
        popupSettingPage.selectPopupScreenNameOrPageLinkInput(screenNameOrPageLink);

        step("Click Save and go to Content button");
        popupSettingPage.clickSaveAndGoToContentButton();

        step("Select Image Type: " + imageType);
        popupSettingPage.selectImageTypeInput(imageType);

        step("Select Image Ratio: " + imageRatio);
        popupSettingPage.selectImageRatioInput(imageRatio);

        step("Upload CTA Image for EN/ID");
        popupSettingPage.uploadCTAImage(filePath);

        step("Set CTA Image Url for EN/ID: " + imageCTAUrl);
        popupSettingPage.setCTAImageUrl(imageCTAUrl);

        step("Click Submit button");
        popupSettingPage.clickSubmitButton();
        Assertion.assertThat("Verify success snackbar appears", popupSettingPage.isSnackbarSuccessAppear(), is(true));

        step("Return to Pop Up List after popup creation");
        popupSettingPage.goBackToPopupList();
        Assertion.assertThat("Verify Popup Setting page loaded after popup creation", popupSettingPage.isPopupSettingPageLoaded(), is(true));

        step("Search by Popup name: " + popUpName);
        popupSettingPage.setSearchPopupName(popUpName);
        popupSettingPage.clickSearchButton();

        step("Verify searched data");
        Assertion.assertThat("Verify searched popup name", popupSettingPage.getTableValueByHeading("Pop Up Name"), containsString(popUpName));

        step("Click Edit General button");
        popupSettingPage.clickEditGeneralButton();

        step("Enter updated popup name: " + updatedPopUpName);
        popupSettingPage.setPopupName(updatedPopUpName);

        step("Click Save and go to Content button");
        popupSettingPage.clickSaveAndGoToContentButton();

        step("Click Submit button");
        popupSettingPage.clickSubmitButton();
        Assertion.assertThat("Verify success snackbar appears", popupSettingPage.isSnackbarSuccessAppear(), is(true));

        step("Return to dashboard after popup update");
        popupSettingPage.goBackToPopupList();
        Assertion.assertThat("Verify Popup Setting page loaded after popup update", popupSettingPage.isPopupSettingPageLoaded(), is(true));

        step("Search by Updated Popup name: " + updatedPopUpName);
        popupSettingPage.setSearchPopupName(updatedPopUpName);
        popupSettingPage.clickSearchButton();

        step("Verify searched data");
        Assertion.assertThat("Verify searched popup name is same as updated", popupSettingPage.getTableValueByHeading("Pop Up Name"), containsString(updatedPopUpName));

        step("Now Archive the Popup");
        popupSettingPage.clickArchiveButton();

        step("Verify Archive Popup Modal and click Archive");
        Assertion.assertThat("Verify Archive popup modal appears", popupSettingPage.isArchivePopupAppear(), is(true));
        Assertion.assertThat("Verify Archive popup modal button clickable", popupSettingPage.isArchivePopupButtonClickable(), is(true));
        popupSettingPage.clickArchivePopupModalButton();
        Assertion.assertThat("Verify success snackbar appears", popupSettingPage.isSnackbarSuccessAppear(), is(true));

        step("Search by Updated Popup name: " + updatedPopUpName);
        popupSettingPage.setSearchPopupName(updatedPopUpName);
        popupSettingPage.clickSearchButton();

        step("Verify popup data is removed");
        Assertion.assertThat("Verify No pop-up to display", popupSettingPage.isTableDataNotAppear(), is(true));
    }

    @Author(names = { "Fandy" })
    @Module(name = "PopUp")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testPopUpAddMultipleConditionUTM() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPopUpAddMultipleConditionUTM");
        BaseTest.paramsThreadLocal.set(param);

        PopupSettingPage<?> popupSettingPage = new PopupSettingPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String popUpName = generateRandomString(5) + "Popup UTM Condition Testing";
        String popUpType = param.get("popUpType");
        String popUpCurrency = param.get("popUpCurrency");
        String popUpAvailablePlatform = param.get("popUpAvailablePlatform");
        String screenNameOrPageLink = param.get("screenNameOrPageLink");
        String filePath = param.get("filePath");
        String imageType = param.get("imageType");
        String imageRatio = param.get("imageRatio");
        String imageCTAUrl = param.get("imageCTAUrl");
        String utmConditionOrValue1 = generateRandomString(5) + "_utm1";
        String utmConditionOrValue2 = generateRandomString(5) + "_utm2";
        String utmConditionAndValue = generateRandomString(5) + "_utm3";

        step("Login via cookie injection");
        popupSettingPage.goToHomePage(username, password);
        Assertion.assertThat("Verify home page loaded", popupSettingPage.isHomePageLoaded(), is(true));

        step("Go to Pop up setting page");
        popupSettingPage.clickGetInButton();
        popupSettingPage.clickPopupSettingPageSideMenu();
        Assertion.assertThat("Verify Popup Setting page loaded", popupSettingPage.isPopupSettingPageLoaded(), is(true));

        step("Click Add Popup button");
        popupSettingPage.clickAddPopupButton();
        Assertion.assertThat("Verify New Pop Up page visible", popupSettingPage.isNewPopupPageVisible(), is(true));

        step("Enter popup name: " + popUpName);
        popupSettingPage.setPopupName(popUpName);

        step("Select popup type: " + popUpType);
        popupSettingPage.setPopUpTypeInput(popUpType);

        step("Select popup currency: " + popUpCurrency);
        popupSettingPage.setPopUpCurrencyInput(popUpCurrency);

        step("Select popup show users options: Login and Logout");
        popupSettingPage.selectPopupShowLoginCheckbox();
        popupSettingPage.selectPopupShowLogoutCheckbox();

        step("Select popup available platform: " + popUpAvailablePlatform);
        popupSettingPage.selectPopupAvailablePlatformOptionCheckbox(popUpAvailablePlatform);

        step("Enter popup Screen Name or Page Link: " + screenNameOrPageLink);
        popupSettingPage.selectPopupScreenNameOrPageLinkInput(screenNameOrPageLink);

        step("Click +Condition button");
        popupSettingPage.clickAddConditionButton();

        step("Select condition type: UTM/Traffic Source");
        popupSettingPage.selectConditionTypeUtmTrafficSource();

        step("Click +AND to add first UTM condition criteria");
        popupSettingPage.clickAddAndCriteriaButton();

        step("Select UTM condition key for first criteria");
        popupSettingPage.selectConditionCriteriaKey(1);

        step("Enter first OR value: " + utmConditionOrValue1);
        popupSettingPage.setConditionValue(1, utmConditionOrValue1);

        step("Click + Condition criteria OR to add second value under same UTM key");
        popupSettingPage.clickAddOrValueButton();

        step("Select UTM condition key for OR value");
        popupSettingPage.selectConditionCriteriaKey(2);

        step("Enter second OR value: " + utmConditionOrValue2);
        popupSettingPage.setConditionValue(2, utmConditionOrValue2);

        step("Verify both OR values are retained under the same UTM condition key");
        Assertion.assertThat("Verify first OR value is retained", popupSettingPage.getConditionValue(1), containsString(utmConditionOrValue1));
        Assertion.assertThat("Verify second OR value is retained", popupSettingPage.getConditionValue(2), containsString(utmConditionOrValue2));

        step("Click +AND to add a separate AND-joined UTM condition");
        popupSettingPage.clickAddAndCriteriaButton();

        step("Select UTM condition key for AND criteria");
        popupSettingPage.selectConditionCriteriaKey(3);

        step("Enter AND condition value: " + utmConditionAndValue);
        popupSettingPage.setConditionValue(3, utmConditionAndValue);

        step("Verify AND value is set and OR values remain unaffected");
        Assertion.assertThat("Verify AND value is set", popupSettingPage.getConditionValue(3), containsString(utmConditionAndValue));
        Assertion.assertThat("Verify first OR value still retained", popupSettingPage.getConditionValue(1), containsString(utmConditionOrValue1));
        Assertion.assertThat("Verify second OR value still retained", popupSettingPage.getConditionValue(2), containsString(utmConditionOrValue2));

        step("Click Save and go to Content button");
        popupSettingPage.clickSaveAndGoToContentButton();

        step("Select Image Type: " + imageType);
        popupSettingPage.selectImageTypeInput(imageType);

        step("Select Image Ratio: " + imageRatio);
        popupSettingPage.selectImageRatioInput(imageRatio);

        step("Upload CTA Image for EN/ID");
        popupSettingPage.uploadCTAImage(filePath);

        step("Set CTA Image Url for EN/ID: " + imageCTAUrl);
        popupSettingPage.setCTAImageUrl(imageCTAUrl);

        step("Click Submit button");
        popupSettingPage.clickSubmitButton();
        Assertion.assertThat("Verify success snackbar appears", popupSettingPage.isSnackbarSuccessAppear(), is(true));

        step("Go to Pop up setting page");
        popupSettingPage.clickPopupSettingPageSideMenu();
        Assertion.assertThat("Verify Popup Setting page loaded", popupSettingPage.isPopupSettingPageLoaded(), is(true));

        step("Search by Popup name: " + popUpName);
        popupSettingPage.setSearchPopupName(popUpName);
        popupSettingPage.clickSearchButton();

        step("Verify searched data");
        Assertion.assertThat("Verify searched popup name", popupSettingPage.getTableValueByHeading("Pop Up Name"), containsString(popUpName));

        step("Now Archive the Popup");
        popupSettingPage.clickArchiveButton();

        step("Verify Archive Popup Modal and click Archive");
        Assertion.assertThat("Verify Archive popup modal appears", popupSettingPage.isArchivePopupAppear(), is(true));
        Assertion.assertThat("Verify Archive popup modal button clickable", popupSettingPage.isArchivePopupButtonClickable(), is(true));
        popupSettingPage.clickArchivePopupModalButton();
        Assertion.assertThat("Verify success snackbar appears", popupSettingPage.isSnackbarSuccessAppear(), is(true));

        step("Search by Popup name: " + popUpName);
        popupSettingPage.setSearchPopupName(popUpName);
        popupSettingPage.clickSearchButton();

        step("Verify popup data is removed");
        Assertion.assertThat("Verify No pop-up to display", popupSettingPage.isTableDataNotAppear(), is(true));
    }

    @Author(names = { "Fandy" })
    @Module(name = "PopUp")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testPopUpAllPagesScreenName() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPopUpAllPagesScreenName");
        BaseTest.paramsThreadLocal.set(param);

        PopupSettingPage<?> popupSettingPage = new PopupSettingPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String popUpName = generateRandomString(5) + " Automation Popup All Pages Test";
        String popUpType = param.get("popUpType");
        String popUpCurrency = param.get("popUpCurrency");
        String popUpAvailablePlatform = param.get("popUpAvailablePlatform");
        String screenNameOrPageLink = param.get("screenNameOrPageLink");
        String filePath = param.get("filePath");
        String imageType = param.get("imageType");
        String imageRatio = param.get("imageRatio");
        String imageCTAUrl = param.get("imageCTAUrl");
        String popUpOrderGroupId = param.get("popUpOrderGroupId");
        String utmConditionOrValue1 = generateRandomString(5) + "_utm1";

        step("Login admin using: " + username);
        popupSettingPage.goToHomePage(username, password);
        Assertion.assertThat("Verify home page loaded", popupSettingPage.isHomePageLoaded(), is(true));

        step("Go to Pop up setting page");
        popupSettingPage.clickGetInButton();
        popupSettingPage.clickPopupSettingPageSideMenu();
        Assertion.assertThat("Verify Popup Setting page loaded", popupSettingPage.isPopupSettingPageLoaded(), is(true));

        step("Click Add Popup button");
        popupSettingPage.clickAddPopupButton();
        Assertion.assertThat("Verify New Pop Up page visible", popupSettingPage.isNewPopupPageVisible(), is(true));

        step("Enter popup name: " + popUpName);
        popupSettingPage.setPopupName(popUpName);

        step("Select popup type: " + popUpType);
        popupSettingPage.setPopUpTypeInput(popUpType);

        step("Select popup currency: " + popUpCurrency);
        popupSettingPage.setPopUpCurrencyInput(popUpCurrency);

        step("Select popup show users options: Login and Logout");
        popupSettingPage.selectPopupShowLoginCheckbox();
        popupSettingPage.selectPopupShowLogoutCheckbox();

        step("Select popup available platform: " + popUpAvailablePlatform);
        popupSettingPage.selectPopupAvailablePlatformOptionCheckbox(popUpAvailablePlatform);

        step("Enter popup Screen Name or Page Link: " + screenNameOrPageLink);
        popupSettingPage.selectPopupScreenNameOrPageLinkInput(screenNameOrPageLink);

        step("Click +Condition button");
        popupSettingPage.clickAddConditionButton();

        step("Select condition type: UTM/Traffic Source");
        popupSettingPage.selectConditionTypeUtmTrafficSource();

        step("Click +AND to add first UTM condition criteria");
        popupSettingPage.clickAddAndCriteriaButton();

        step("Select UTM condition key for first criteria");
        popupSettingPage.selectConditionCriteriaKey(1);

        step("Enter first OR value: " + utmConditionOrValue1);
        popupSettingPage.setConditionValue(1, utmConditionOrValue1);

        step("Remove second condition due to not needed");
        popupSettingPage.removeSecondConditionValue();

        step("Click Save and go to Content button");
        popupSettingPage.clickSaveAndGoToContentButton();

        step("Select Image Type: " + imageType);
        popupSettingPage.selectImageTypeInput(imageType);

        step("Select Image Ratio: " + imageRatio);
        popupSettingPage.selectImageRatioInput(imageRatio);

        step("Upload CTA Image for EN/ID");
        popupSettingPage.uploadCTAImage(filePath);

        step("Set CTA Image Url for EN/ID: " + imageCTAUrl);
        popupSettingPage.setCTAImageUrl(imageCTAUrl);

        step("Get created popup id from URL");
        String popupId = popupSettingPage.getPopupIdFromUrl();

        step("Click Submit button");
        popupSettingPage.clickSubmitButton();
        Assertion.assertThat("Verify success snackbar appears", popupSettingPage.isSnackbarSuccessAppear(), is(true));

        step("Set popup order to first priority so it is the first popup to appear");
        popupSettingPage.setPopupOrderFirst(popupId, popUpOrderGroupId);

        step("Verify popup is visible to end user via /popups/multi for UTM source: " + utmConditionOrValue1);
        Assertion.assertThat("Verify popup exists in end user /popups/multi response",
                popupSettingPage.isPopupVisibleForEndUser(popupId, utmConditionOrValue1, "dynamicPage", "/"), is(true));

        step("Go to Pop up setting page");
        popupSettingPage.clickPopupSettingPageSideMenu();
        Assertion.assertThat("Verify Popup Setting page loaded", popupSettingPage.isPopupSettingPageLoaded(), is(true));

        step("Search by Popup name: " + popUpName);
        popupSettingPage.setSearchPopupName(popUpName);
        popupSettingPage.clickSearchButton();

        step("Verify searched data");
        Assertion.assertThat("Verify searched popup name", popupSettingPage.getTableValueByHeading("Pop Up Name"), containsString(popUpName));

        step("Now Archive the Popup");
        popupSettingPage.clickArchiveButton();

        step("Verify Archive Popup Modal and click Archive");
        Assertion.assertThat("Verify Archive popup modal appears", popupSettingPage.isArchivePopupAppear(), is(true));
        Assertion.assertThat("Verify Archive popup modal button clickable", popupSettingPage.isArchivePopupButtonClickable(), is(true));
        popupSettingPage.clickArchivePopupModalButton();
        Assertion.assertThat("Verify success snackbar appears", popupSettingPage.isSnackbarSuccessAppear(), is(true));

        step("Search by Popup name: " + popUpName);
        popupSettingPage.setSearchPopupName(popUpName);
        popupSettingPage.clickSearchButton();

        step("Verify popup data is removed");
        Assertion.assertThat("Verify No pop-up to display", popupSettingPage.isTableDataNotAppear(), is(true));
    }

    @Author(names = {"Ankit"})
    @Vertical(name = "Grand")
    @Tribe(name = "Discovery")
    @Module(name = "HomePage")
    @Test
    public void verifyInstallAppQrPopupTitleLanguage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyInstallAppQrPopupTitleLanguage");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        String country = map.get("country");
        String currency = map.get("currency");
        String languageEn = map.get("languageEn");
        String expectedBannerTextEn = map.get("expectedBannerTextEn");
        String expectedQrTitleEn = map.get("expectedQrTitleEn");
        String languageId = map.get("languageId");
        String expectedBannerTextId = map.get("expectedBannerTextId");
        String expectedQrTitleId = map.get("expectedQrTitleId");

        step("Given user opens the desktop homepage");
        homePage.goToHomePage();

        step("Logout via URL so the test runs as non login");
        loginPage.logoutViaUrl();
        homePage.goToHomePage();
        Assertion.assertThat("User should be logged out", loginPage.checkIfLoggedIn(), is(false));

        step("Set country, currency, and language to " + country + ", " + currency + ", " + languageEn);
        homePage.selectCurrencyLanguageAndCountry(country, currency, languageEn);

        step("Swipe a little up so the install QR banner appears");
        homePage.scrollLittleToShowInstallBanner();

        step("Verify install banner sentence matches English");
        Assertion.assertThat("Install banner sentence should match English",
                homePage.getInstallAppBannerText(), containsString(expectedBannerTextEn));

        step("Click Install on the floating banner");
        homePage.clickInstallAppBanner();

        step("Verify QR scanner popup opens after clicking Install");
        Assertion.assertThat("QR scanner popup should be displayed after clicking Install",
                homePage.isQrPopupDisplayed(), is(true));

        step("Verify QR popup title language matches account language English");
        Assertion.assertThat("QR popup title language matches account language English",
                homePage.getQrPopupTitle(), containsString(expectedQrTitleEn));

        step("Close QR popup");
        homePage.closeQrPopup();

        step("Renew WebDriver session (fresh install) so the QR banner can appear again");
        session = renewSessionForFreshInstall();
        homePage = HomePageWeb.getInstance(session);

        step("Open homepage and set country, currency, and language to " + country + ", " + currency + ", " + languageId);
        homePage.goToHomePage();
        homePage.selectCurrencyLanguageAndCountry(country, currency, languageId);

        step("Swipe a little up so the install QR banner appears");
        homePage.scrollLittleToShowInstallBanner();

        step("Verify install banner sentence matches Bahasa");
        Assertion.assertThat("Install banner sentence should match Bahasa",
                homePage.getInstallAppBannerText(), containsString(expectedBannerTextId));

        step("Click Instal on the floating banner");
        homePage.clickInstallAppBanner();

        step("Verify QR scanner popup opens after clicking Instal");
        Assertion.assertThat("QR scanner popup should be displayed after clicking Instal",
                homePage.isQrPopupDisplayed(), is(true));

        step("Verify QR popup title language matches account language Bahasa");
        Assertion.assertThat("QR popup title language matches account language Bahasa",
                homePage.getQrPopupTitle(), containsString(expectedQrTitleId));

        step("Close QR popup");
        homePage.closeQrPopup();
    }

    @Author(names = {"Ankit"})
    @Vertical(name = "Grand")
    @Tribe(name = "Discovery")
    @Module(name = "HomePage")
    @Test
    public void verifyBrokenPage404AndInvalidUrlRedirect() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyBrokenPage404AndInvalidUrlRedirect");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);

        String brokenPath = map.getOrDefault("brokenPath", "en-id/broken");
        String invalidPath = map.getOrDefault("invalidPath", "asdadadada");

        step("Given user opens a broken page URL: " + brokenPath);
        homePage.openWebUrlPath(brokenPath);

        step("Then the 404 'This page could not be found.' message should be displayed");
        Assertion.assertThat("Broken URL should show the 404 not-found message",
                homePage.isPageNotFoundMessageDisplayed(), is(true));

        step("When user opens an invalid URL: " + invalidPath);
        homePage.openWebUrlPath(invalidPath);

        step("Then user should be redirected to the homepage (verified via homepage greeting UI)");
        Assertion.assertThat("Invalid URL should redirect to the tiket homepage (greeting UI displayed)",
                homePage.isHomePageOpened(), is(true));
    }
  
    @Author(names = { "Fandy" })
    @Module(name = "PopUp")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testPopUpFloatingHiddenOnPriorityDropdownList() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("testPopUpFloatingHiddenOnPriorityDropdownList");
        BaseTest.paramsThreadLocal.set(param);

        PopupSettingPage<?> popupSettingPage = new PopupSettingPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String popUpName = "Floating Automation_" + generateRandomString(5);
        String popUpType = param.get("popUpType");
        String popUpCurrency = param.get("popUpCurrency");
        String popUpAvailablePlatform = param.get("popUpAvailablePlatform");
        String screenNameOrPageLink = param.get("screenNameOrPageLink");
        String filePath = param.get("filePath");
        String floatingTitle = param.get("floatingTitle");
        String floatingCtaButtonText = param.get("floatingCtaButtonText");
        String floatingCtaButtonUrl = param.get("floatingCtaButtonUrl");

        step("Login admin using: " + username);
        popupSettingPage.goToHomePage(username, password);
        Assertion.assertThat("Verify home page loaded", popupSettingPage.isHomePageLoaded(), is(true));

        step("Go to Pop up setting page");
        popupSettingPage.clickGetInButton();
        popupSettingPage.clickPopupSettingPageSideMenu();
        Assertion.assertThat("Verify Popup Setting page loaded", popupSettingPage.isPopupSettingPageLoaded(), is(true));

        step("Click Add Popup button");
        popupSettingPage.clickAddPopupButton();
        Assertion.assertThat("Verify New Pop Up page visible", popupSettingPage.isNewPopupPageVisible(), is(true));

        step("Enter popup name: " + popUpName);
        popupSettingPage.setPopupName(popUpName);

        step("Select popup type: " + popUpType);
        popupSettingPage.setPopUpTypeInput(popUpType);

        step("Select popup currency: " + popUpCurrency);
        popupSettingPage.setPopUpCurrencyInput(popUpCurrency);

        step("Select popup show users options: Login and Logout");
        popupSettingPage.selectPopupShowLoginCheckbox();
        popupSettingPage.selectPopupShowLogoutCheckbox();

        step("Select popup available platform: " + popUpAvailablePlatform);
        popupSettingPage.selectPopupAvailablePlatformOptionCheckbox(popUpAvailablePlatform);

        step("Enter popup Screen Name or Page Link: " + screenNameOrPageLink);
        popupSettingPage.selectPopupScreenNameOrPageLinkInput(screenNameOrPageLink);

        step("Click Save and go to Content button");
        popupSettingPage.clickSaveAndGoToContentButton();

        step("Set floating position: Bottom");
        popupSettingPage.selectFloatingPositionBottom();

        step("Upload image icon");
        popupSettingPage.uploadFloatingIcon(filePath);

        step("Click & Fill Title: " + floatingTitle);
        popupSettingPage.setFloatingTitle(floatingTitle);

        step("Click & Fill CTA Button Text: " + floatingCtaButtonText);
        popupSettingPage.setFloatingCTAButtonText(floatingCtaButtonText);

        step("Click & Fill CTA Button URL: " + floatingCtaButtonUrl);
        popupSettingPage.setFloatingCTAButtonUrl(floatingCtaButtonUrl);

        step("Click Submit button");
        popupSettingPage.clickSubmitButton();
        Assertion.assertThat("Verify success snackbar appears", popupSettingPage.isSnackbarSuccessAppear(), is(true));

        step("Go to Pop up setting page");
        popupSettingPage.clickPopupSettingPageSideMenu();
        Assertion.assertThat("Verify Popup Setting page loaded", popupSettingPage.isPopupSettingPageLoaded(), is(true));

        step("Click Set Order & Priority button");
        popupSettingPage.clickSetOrderPriorityButton();
        Assertion.assertThat("Verify Pop up / Position title appears", popupSettingPage.isPopupPositionSettingTitleVisible(), is(true));

        step("Click Priority dropdown");
        popupSettingPage.clickPriorityDropdown();

        step("Verify top dropdown option is not the created floating popup: " + popUpName);
        Assertion.assertThat("Verify floating popup is hidden from priority dropdown top option", popupSettingPage.isTopPriorityDropdownOptionEqualToPopupName(popUpName), is(false));

        step("Go to Pop up setting page");
        popupSettingPage.clickPopupSettingPageSideMenu();
        Assertion.assertThat("Verify Popup Setting page loaded", popupSettingPage.isPopupSettingPageLoaded(), is(true));

        step("Search by Popup name: " + popUpName);
        popupSettingPage.setSearchPopupName(popUpName);
        popupSettingPage.clickSearchButton();

        step("Verify searched data");
        Assertion.assertThat("Verify searched popup name", popupSettingPage.getTableValueByHeading("Pop Up Name"), containsString(popUpName));

        step("Now Archive the Popup");
        popupSettingPage.clickArchiveButton();

        step("Verify Archive Popup Modal and click Archive");
        Assertion.assertThat("Verify Archive popup modal appears", popupSettingPage.isArchivePopupAppear(), is(true));
        Assertion.assertThat("Verify Archive popup modal button clickable", popupSettingPage.isArchivePopupButtonClickable(), is(true));
        popupSettingPage.clickArchivePopupModalButton();
        Assertion.assertThat("Verify success snackbar appears", popupSettingPage.isSnackbarSuccessAppear(), is(true));

        step("Search by Popup name: " + popUpName);
        popupSettingPage.setSearchPopupName(popUpName);
        popupSettingPage.clickSearchButton();

        step("Verify popup data is removed");
        Assertion.assertThat("Verify No pop-up to display", popupSettingPage.isTableDataNotAppear(), is(true));
    }
}