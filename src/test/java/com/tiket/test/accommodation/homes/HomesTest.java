package com.tiket.test.accommodation.homes;

import com.tiket.page.common.booking_form.CommonBookingFormPage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.HotelApi;
import com.tiket.apiPage.OrderApi;
import com.tiket.apiPage.Unm;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.page.accommodation.homes.HomesPage;
import com.tiket.page.accommodation.homes.homesdetailpage.HomesDetailPage;
import com.tiket.page.accommodation.homes.homeslandingpage.HomesLandingPage;
import com.tiket.page.accommodation.homes.homesroomdetailpage.HomesRoomDetailPage;
import com.tiket.page.accommodation.homes.homesroomlistpage.HomesRoomListPage;
import com.tiket.page.accommodation.homes.homessearchresultpage.HomesSearchResultPage;
import com.tiket.page.accommodation.hotel.HotelPage;
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.roomdetailpage.RoomDetailPage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPageAndroid;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
import com.tiket.page.account.AccountPage;
import com.tiket.page.common.phone.settings.DefaultAppsPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.refund.RefundPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.wishlist.landing.WishlistPage;
import com.tiket.pojo.accommodation.homes.HomesData;
import com.tiket.pojo.accommodation.homes.HomesLandingPageModuleData;
import com.tiket.pojo.accommodation.homes.HomesNHAResponse;
import com.tiket.pojo.accommodation.hotel.FilterInfo;
import com.tiket.pojo.accommodation.hotel.HotelAutoCompleteResponse;
import com.tiket.pojo.accommodation.hotel.SearchHotelResponse;
import com.tiket.pojo.accommodation.master_tag.AccommodationMasterTagResponse;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.room.SelectRoomResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.page.accommodation.hotels.reschedulePage.HotelReschedulePageWeb;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.model.AddOnsType.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class HomesTest extends BaseTest {

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Module(name = "Accommodation")
    @Test
    public void ACC_homesRescheduleNonIdr() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("ACC_homesRescheduleNonIdr");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        DefaultAppsPage<?> defaultAppsPage = DefaultAppsPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        String username = map.get("username");
        String password = map.get("password");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String hotelName = map.get("hotelName");
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night")); // should be less than 'rescheduleDuration' value
        int room = Integer.parseInt(map.get("room"));
        String currency = map.get("currency");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");
        int rescheduleDuration = Integer.parseInt(map.get("rescheduleDuration")); // should be greater than 'night' value
        int addonsAmount = Integer.parseInt(map.get("addonsAmount"));

        step("Goto home");
        homePage.gotoHome();

        step("Clear browsers default preference");
        defaultAppsPage.clearBrowserDefaultPreferenceOnLambdaTest();

        step("Goto home");
        homePage.gotoHome();

        step("Login via deeplink: " + username);
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Change currency to non-IDR booking currency { " + currency + " }");
        homePage.changeCurrency(currency, username);

        step("Create a homes (NHA) order with waiting payment status via Android book API");
        String orderId = "";
        String addonsName = "";
        try {
            orderId = hotelApi.createWaitingPaymentOrderApi(
                    username, password, startDate, hotelName, adult, night, room,
                    currency, addonsAmount, "NHA", "id");
            Assertion.assertThat("Success to create waiting payment order", orderId, both(is(not(emptyOrNullString()))).and(is(not("0"))));
            addonsName = hotelApi.addonsName;
            Assertion.assertThat("Addons should be included in the created order", addonsName, is(not(emptyOrNullString())));
        } catch (Exception e) {
            Assertion.skip("Error when creating waiting payment order: " + e.getMessage());
        } 

        step("Open the order page");
        homePage.openMyOrderPage();

        step("Complete order with the payment method");
        orderListPage.openOrderCompletePayment(orderId);
        paymentPage.checkOrderID();
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();

        step("Open order list from success paid banner");
        paymentPage.seeOrderList();

        step("Open the order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderId);
        
        Assertion.assertThat("add on "+addonsAmount+" "+addonsName+" should be showing on order detail", orderDetailPage.checkAddon(addonsName,addonsAmount), is(true));

        step("Choose reschedule");
        orderDetailPage.clickReschedule(orderId);

        step("Choose new date for the reschedule");
        webViewPage.switchToWebViewUrl("reschedule");
        String newStartDate = hotelReschedulePageWeb.changeNightDuration(rescheduleDuration);

        step("Choose a new room type with additional Price");
        webViewPage.switchToWebViewUrl("reschedule");
        String newRoomName = hotelReschedulePageWeb.chooseRoomWithAdditionalPrice();

        step("Submit booking form");
        webViewPage.switchToWebViewUrl("reschedule/checkout");
        hotelReschedulePageWeb.submitRescheduleBookingForm();

        step("Payment page should be displayed");
        webViewPage.switchToNative();
        String newOrderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !newOrderID.isEmpty(), is(true));

        step("Input and submit CC information");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName, newOrderID);

        step("Submit the payment method");
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(newOrderID), is(true));

        step("Open order list page");
        paymentPage.seeOrderList(newOrderID);

        step("Check the new order detail");
        orderListPage.openIssuedTicketViaDeeplink(newOrderID);
        
        Assertion.assertThat("Reschedule card notification should be present", orderDetailPage.isRescheduleCardDisplayed(newOrderID), is(true));
        Assertion.assertThat("Order ID should be showing as expected", orderDetailPage.getOrderID(), is(newOrderID));
        Assertion.assertThat("Start Date should be showing as expected", orderDetailPage.getStartDate(newOrderID), is(newStartDate));
        Assertion.assertThat("Hotel Name should be showing as expected: " + hotelName, orderDetailPage.getHotelName(newOrderID), containsString(hotelName));
        Assertion.assertThat("Room Name is displayed correctly", orderDetailPage.getRoomName(newOrderID).toLowerCase(), containsString(newRoomName.toLowerCase()));

        step("Check the currency on the new order detail");
        Assertion.assertThat("Current currency should be " + currency, orderDetailPage.getTotalPaymentHotel(), containsString(currency));

        step("Check the previous order detail");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        Assertion.assertThat("Previous order banner should be present",orderDetailPage.isNewScheduleButtonDisplayed(orderId), is(true));
        Assertion.assertThat("Hotel Name should be showing as expected: " + hotelName, orderDetailPage.isHotelNameDisplayed(hotelName, orderId), is(true));
        Assertion.assertThat("Start Date should be different with the rescheduled order", orderDetailPage.getStartDate(orderId), is(not(newStartDate)));

        step("Check the e-ticket on the new order detail");
        orderDetailPage.clickNewScheduleButton(orderId);
        orderDetailPage.hidePromoBannerIfPresent();
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("en");

        step("e-Ticket file is opened");
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("successfully to download the e-ticket", actualPdfContent, is(not(emptyString())));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "SRP")
    @Test
    public void checkMapsOnSrpGuestHomes() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkMapsOnSrpGuestHomes");
        BaseTest.paramsThreadLocal.set(param);
        String location = param.get("location");
        int numberOfZoomOut = Integer.parseInt(param.get("numberOfZoomOut"));
        int numberOfZoomIn = Integer.parseInt(param.get("numberOfZoomIn"));
        int numberOfSwipe = Integer.parseInt(param.get("numberOfSwipe"));
        Swipe swipeDirection = Swipe.valueOf(param.get("swipeDirection").toUpperCase());
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("User open app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Search hotel");
        homesLandingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Click See maps button in PDP");
        searchResultPage.clickSeeMapButton();

        step("Zoom the maps");
        searchResultPage.zoomMaps(numberOfZoomOut, numberOfZoomIn);

        step("Swipe the maps");
        searchResultPage.swipeMaps(swipeDirection, numberOfSwipe);

        step("Click Search This Area button");
        searchResultPage.searchThisArea();
        String priceInThisArea = searchResultPage.getVisibleHotelPriceMaps();
        Assertion.assertThat("Hotel card item is displayed", priceInThisArea, is(containsString("IDR ")));

        step("Click Search Previous button");
        searchResultPage.searchPrevious();
        String priceAfterPrevious = searchResultPage.getVisibleHotelPriceMaps();
        Assertion.assertThat("Price in this area and after click previous button are different", priceAfterPrevious, is(not(priceInThisArea)));

        step("Swipe the hotel card item");
        searchResultPage.swipeHotelCardItem();
        String priceAfterSwipe = searchResultPage.getVisibleHotelPriceMaps();
        Assertion.assertThat("Price after swiping and after click previous button are different", priceAfterSwipe, is(not(priceAfterPrevious)));

        step("Go to the hotel PDP");
        String mapsHotelName = searchResultPage.selectVisibleHotelItem();
        Assertion.assertThat("Hotel name on the PDP is equal with the selected on the Maps", detailPage.getHotelNameDetail(), is(mapsHotelName));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "SRP")
    @Test
    public void checkMapsOnSrpLoginHomes() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkMapsOnSrpLoginHomes");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        int numberOfZoomOut = Integer.parseInt(param.get("numberOfZoomOut"));
        int numberOfZoomIn = Integer.parseInt(param.get("numberOfZoomIn"));
        int numberOfSwipe = Integer.parseInt(param.get("numberOfSwipe"));
        Swipe swipeDirection = Swipe.valueOf(param.get("swipeDirection").toUpperCase());
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Search hotel");
        homesLandingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Click See maps button in PDP");
        searchResultPage.clickSeeMapButton();

        step("Zoom the maps");
        searchResultPage.zoomMaps(numberOfZoomOut, numberOfZoomIn);

        step("Swipe the maps");
        searchResultPage.swipeMaps(swipeDirection, numberOfSwipe);

        step("Click Search This Area button");
        searchResultPage.searchThisArea();
        String priceInThisArea = searchResultPage.getVisibleHotelPriceMaps();
        Assertion.assertThat("Hotel card item is displayed", priceInThisArea, is(containsString("IDR ")));

        step("Click Search Previous button");
        searchResultPage.searchPrevious();
        String priceAfterPrevious = searchResultPage.getVisibleHotelPriceMaps();
        Assertion.assertThat("Price in this area and after click previous button are different", priceAfterPrevious, is(not(priceInThisArea)));

        step("Swipe the hotel card item");
        searchResultPage.swipeHotelCardItem();
        String priceAfterSwipe = searchResultPage.getVisibleHotelPriceMaps();
        Assertion.assertThat("Price after swiping and after click previous button are different", priceAfterSwipe, is(not(priceAfterPrevious)));

        step("Go to the hotel PDP");
        String mapsHotelName = searchResultPage.selectVisibleHotelItem();
        Assertion.assertThat("Hotel name on the PDP is equal with the selected on the Maps", detailPage.getHotelNameDetail(), is(mapsHotelName));
    }

    @Author(names = {"Naufal", "Akbar"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifyLandscapeBannerLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyLandscapeBannerLogin");
        BaseTest.paramsThreadLocal.set(map);

        boolean login = Boolean.parseBoolean(map.get("login"));
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomesPage<WebDriver> homesPage = HomesPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginWith(loginType, username, password);

        step("Get landscape banner content from the API");
        String language = homePage.getLanguageApp();
        String section = "LANDSCAPE_BANNER";
        HomesData data = homesPage.getContentLandingPageUsingPojo(login, username, password, language, section);
        Assertion.assertThat("Success to get landscape banner content from the API", !data.title.isEmpty(), is(true));

        step("Get landscape banner title from the API");
        String landscapeBannerTitleApi = data.title;
        log("Landscape Banner Title Api : " + landscapeBannerTitleApi);

        step("Get landscape banner title, subtitle and length from the API");
        String landscapeBannerSubtitleApi = data.subTitle;
        ArrayList<String> landscapeBannerListApi = data.contentListTitle;
        int TotalLandscapeBannerApi = landscapeBannerListApi.size();

        log("Landscape Banner Subtitle Api : " + landscapeBannerSubtitleApi);
        log("Landscape Banner list Api : " + landscapeBannerListApi);
        log("Total Landscape Banner Api : " + TotalLandscapeBannerApi);

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", language, "");

        step("Get landscape banner title from the App");
        String landscapeBannerTitleApp = homesLandingPage.getLandscapeBannerTitle();
        String landscapeBannerSubtitleApp = homesLandingPage.getLandscapeBannerSubtitle();

        step("Compare landscape banner text caption between API and App");
        boolean compareTotalLandscapeBanner = homesLandingPage.compareTotalLandscapeBanner(TotalLandscapeBannerApi);

        Assertion.assertThat("Landscape Banner Title on the App should be equal with API", landscapeBannerTitleApp, equalTo(landscapeBannerTitleApi));
        Assertion.assertThat("Landscape Banner Subtitle on the App should be equal with API", landscapeBannerSubtitleApp, equalTo(landscapeBannerSubtitleApi));
        Assertion.assertThat("Total Landscape Banner on the App should be equal with API", compareTotalLandscapeBanner, is(true));
    }

    @Author(names = {"Naufal", "Akbar"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifyLandscapeBannerGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyLandscapeBannerGuest");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomesPage<WebDriver> homesPage = HomesPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Get landscape banner content from the API");
        String language = homePage.getLanguageApp();
        String section = "LANDSCAPE_BANNER";
        HomesData landscapeBannerTextApi = homesPage.getContentLandingPageUsingPojo(false, "guest", "-", language, section);
        Assertion.assertThat("Success to get landscape banner content from the API", landscapeBannerTextApi, is(notNullValue()));

        step("Get landscape banner title from the API");
        String landscapeBannerTitleApi = landscapeBannerTextApi.title;
        log("Landscape Banner Title Api : " + landscapeBannerTitleApi);

        step("Get landscape banner title, subtitle and length from the API");
        String landscapeBannerSubtitleApi = landscapeBannerTextApi.subTitle;
        ArrayList<String> landscapeBannerListApi = landscapeBannerTextApi.contentListTitle;
        int TotalLandscapeBannerApi = landscapeBannerListApi.size();

        log("Landscape Banner Subtitle Api : " + landscapeBannerSubtitleApi);
        log("Landscape Banner list Api : " + landscapeBannerListApi);
        log("Total Landscape Banner Api : " + TotalLandscapeBannerApi);

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", language, "");

        step("Get landscape banner title from the App");
        String landscapeBannerTitleApp = homesLandingPage.getLandscapeBannerTitle();
        String landscapeBannerSubtitleApp = homesLandingPage.getLandscapeBannerSubtitle();

        step("Compare landscape banner text caption between API and App");
        boolean compareTotalLandscapeBanner = homesLandingPage.compareTotalLandscapeBanner(TotalLandscapeBannerApi);

        Assertion.assertThat("Landscape Banner Title on the App should be equal with API", landscapeBannerTitleApp, equalTo(landscapeBannerTitleApi));
        Assertion.assertThat("Landscape Banner Subtitle on the App should be equal with API", landscapeBannerSubtitleApp, equalTo(landscapeBannerSubtitleApi));
        Assertion.assertThat("Total Landscape Banner on the App should be equal with API", compareTotalLandscapeBanner, is(true));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifyHeroBannerLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyHeroBannerLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        boolean login = Boolean.parseBoolean(map.get("login"));
        String username = map.get("username");
        String password = map.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomesPage<WebDriver> homesPage = HomesPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Get hero banner content from the API");
        String language = homePage.getLanguageApp();
        String templateCode = "HERO_BANNER";
        HomesData data = homesPage.getContentLandingPageUsingPojo(
                login, username, password, language, templateCode
        );
        Assertion.assertThat("Success to get hero banner content from the API", !data.title.isEmpty(), is(true));

        step("Get hero banner title from the API");
        String heroBannerTitleApi = data.title;

        step("Get hero banner module title list from the API");
        ArrayList<String> heroBannerModuleApi = data.modules;

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", language, "");

        step("Get hero banner title from the App");
        String heroBannerTitleApp = homesLandingPage.getHeroBannerTitle();

        step("Compare hero banner text caption between API and App");
        boolean compareTextCaption = homesLandingPage.compareModuleTitleCaption(heroBannerModuleApi);

        Assertion.assertThat("Hero Banner Title on the App should be equal with API", heroBannerTitleApp, equalTo(heroBannerTitleApi));
        Assertion.assertThat("Hero Banner Caption Text on the App should be equal with API", compareTextCaption, is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "LandingPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyTimedPromoHomesLandingPage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyTimedPromoHomesLandingPage");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomesPage<WebDriver> homesPage = HomesPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Get timed promo module from the API");
        String language = homePage.getLanguageApp();
        String templateCode = "TIMED_PROMO";
        HomesData data = homesPage.getContentLandingPageUsingPojo(false, "guest", "", language, templateCode);
        String templateCodeApi = data.templateCode;
        Assertion.assertThat("Success to get landing page module from the API", templateCodeApi, is(templateCode));

        String landingPageId = data.landingPageId;
        String titleApi = data.subsidyLabel;
        int indexList = data.index;

        HomesData.ContentListData listData = data.contentListData;
        String landingPageName = listData.name;
        String locationApi = listData.location;
        String seeAllButtonApi = listData.buttonText;

        String publicId = listData.locationInsideListRedirect;
        String locationType = listData.locationTypeInsideListRedirect;

        HomesLandingPageModuleData homesLandingPageModuleData = homesPage.getHomesLandingPageModule(false, "guest", "", language, landingPageId, landingPageName,templateCode);
        String searchLocationApi = hotelApi.getLocationName(publicId, locationType, language);

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", language, "");

        step("Go to the timed promo section");
        homesLandingPage.goToTimedPromoSection();

        step("Get the timed promo title");
        String titleApp = homesLandingPage.getTimedPromoTitle();

        step("Select timed promo location");
        String locationApp = homesLandingPage.selectTimedPromoLocation(indexList);

        step("Get the timed promo homes list");
        boolean compareHomesName = homesLandingPage.compareTimedPromoHomesName(homesLandingPageModuleData);

        step("Click see all button");
        String seeAllButtonApp = homesLandingPage.clickSeeAllButton();

        step("Get the expected data of check-in/out date and total room");
        String expectedDate = homesPage.expectedDateRoom(language);

        step("Get the SRP data");
        boolean searchResult = searchResultPage.isHeaderSearchResultShown();
        String searchLocationApp = searchResultPage.getCurrentLocation();
        String dateRoomGuestApp = searchResultPage.getDateRoomGuestDetail();

        Assertion.assertThat("Timed Promo title on the App should be equal with the API", titleApp, is(titleApi));
        Assertion.assertThat("Timed Promo location button on the App should be equal with the API", locationApp, is(locationApi));
        Assertion.assertThat("Timed Promo homes on the App should be equal with the API", compareHomesName, is(true));
        Assertion.assertThat("Timed Promo see all button on the App should be equal with the API", seeAllButtonApp, is(seeAllButtonApi));
        Assertion.assertThat("User is on the homes search result page", searchResult, is(true));
        Assertion.assertThat("Search location on the App SRP should be equal with the API", searchLocationApp, is(searchLocationApi));
        Assertion.assertThat("Check-in/out date and room guest detail SRP should be visible as expected", dateRoomGuestApp, is(expectedDate));
    }

    @Author(names = {"Yazid"})
    @Module(name = "LandingPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyTimedPromoHomesLandingPageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyTimedPromoHomesLandingPageLogin");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomesPage<WebDriver> homesPage = HomesPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Get timed promo module from the API");
        String language = homePage.getLanguageApp();
        String templateCode = "TIMED_PROMO";
        HomesData data = homesPage.getContentLandingPageUsingPojo(true, username, password, language, templateCode);
        String templateCodeApi = data.templateCode;
        Assertion.assertThat("Success to get landing page module from the API", templateCodeApi, is(templateCode));

        String landingPageId = data.landingPageId;
        String titleApi = data.subsidyLabel;
        int indexList = data.index;

        HomesData.ContentListData listData = data.contentListData;
        String landingPageName = listData.name;
        String locationApi = listData.location;
        String seeAllButtonApi = listData.buttonText;

        String publicId = listData.locationInsideListRedirect;
        String locationType = listData.locationTypeInsideListRedirect;

        HomesLandingPageModuleData homesLandingPageModuleData = homesPage.getHomesLandingPageModule(true, username, password, language, landingPageId, landingPageName,templateCode);
        String searchLocationApi = hotelApi.getLocationName(publicId, locationType, language);

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", language, "");

        step("Go to the timed promo section");
        homesLandingPage.goToTimedPromoSection();

        step("Get the timed promo title");
        String titleApp = homesLandingPage.getTimedPromoTitle();

        step("Select timed promo location");
        String locationApp = homesLandingPage.selectTimedPromoLocation(indexList);

        step("Get the timed promo homes list");
        boolean compareHomesName = homesLandingPage.compareTimedPromoHomesName(homesLandingPageModuleData);

        step("Click see all button");
        String seeAllButtonApp = homesLandingPage.clickSeeAllButton();

        step("Get the expected data of check-in/out date and total room");
        String expectedDate = homesPage.expectedDateRoom(language);

        step("Get the SRP data");
        landingPage.fillNightCheckInForm();
        boolean searchResult = searchResultPage.isHeaderSearchResultShown();
        String searchLocationApp = searchResultPage.getCurrentLocation();
        String dateRoomGuestApp = searchResultPage.getDateRoomGuestDetail();

        Assertion.assertThat("Timed Promo title on the App should be equal with the API", titleApp, is(titleApi));
        Assertion.assertThat("Timed Promo location button on the App should be equal with the API", locationApp, is(locationApi));
        Assertion.assertThat("Timed Promo homes on the App should be equal with the API", compareHomesName, is(true));
        Assertion.assertThat("Timed Promo see all button on the App should be equal with the API", seeAllButtonApp, is(seeAllButtonApi));
        Assertion.assertThat("User is on the homes search result page", searchResult, is(true));
        Assertion.assertThat("Search location on the App SRP should be equal with the API", searchLocationApp, is(searchLocationApi));
        Assertion.assertThat("Check-in/out date and room guest detail SRP should be visible as expected", dateRoomGuestApp, is(expectedDate));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "CreateBooking")
    @Test(dataProvider = "createHomesBookingDataProvider")
    public void createHomesBooking(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");
        int totalGuest = Integer.parseInt(map.get("totalGuest"));

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("set total guest: " + totalGuest);
        homesLandingPage.openTotalGuest();
        hotelLandingPage.setTotalGuest(totalGuest);
        homesLandingPage.clickSubmit();

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Select Homes from the Homes list");
        searchResultPage.selectFirstHotelCard();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);
        String expectedHotelName = bookingFormPage.getHotelNameBookingForm();
        String expectedCheckinDate = bookingFormPage.getCheckinBookingForm();
        String expectedNightDuration = bookingFormPage.getDurationBookingForm();
        String expectedTotalPrice = bookingFormPage.getTotalPriceBookingForm();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        String priceChanges = bookingFormPage.PriceChangesPopUp();
        expectedTotalPrice = CoreFunctions.updateString(expectedTotalPrice, priceChanges);

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Homes name on the booking form and payment page should be equal", paymentPage.getProductNamePayment(), is(expectedHotelName));
        Assertion.assertThat("Check-in date on the booking form and payment page should be equal",  paymentPage.getCheckinPayment(), is(expectedCheckinDate));
        Assertion.assertThat("Night duration on the booking form and payment page should be equal", paymentPage.getDurationPayment(), is(expectedNightDuration));
        Assertion.assertThat("Total price on the booking form and payment page should be equal", removeText(paymentPage.getTotalPricePayment()), is(removeText(expectedTotalPrice)));
    }

    @DataProvider(name = "createHomesBookingDataProvider")
    public Object[][] createHomesBookingDataProvider() {
        return getObjectParams("createHomesBooking");
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Module(name = "Accommodation")
    @Test(dataProvider = "createHomesBookingLoginDataProvider")
    public void ACC_createHomesBookingLogin(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        int totalGuest = Integer.parseInt(map.get("totalGuest"));
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        CommonBookingFormPage<WebDriver> commonBookingFormPage = CommonBookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Change currency");
        homePage.changeCurrency(currency,username);

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("set total guest: " + totalGuest);
        homesLandingPage.openTotalGuest();
        hotelLandingPage.setTotalGuest(totalGuest);
        homesLandingPage.clickSubmit();

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Select Homes from the Homes list");
        searchResultPage.selectFirstHotelCard();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        commonBookingFormPage.waitProcessingBooking();
        String expectedHotelName = bookingFormPage.getHotelNameBookingForm();
        String expectedCheckinDate = bookingFormPage.getCheckinBookingForm();
        String expectedNightDuration = bookingFormPage.getDurationBookingForm();
        String expectedTotalPrice = bookingFormPage.getTotalPriceBookingForm();
        bookingFormPage.selectRandomSalutationIfMissing();
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        String priceChanges = bookingFormPage.PriceChangesPopUp();
        expectedTotalPrice = CoreFunctions.updateString(expectedTotalPrice, priceChanges);

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Homes name on the booking form and payment page should be equal", paymentPage.getProductNamePayment(), is(expectedHotelName));
        Assertion.assertThat("Check-in date on the booking form and payment page should be equal", removeLeadingZeroFromDate(paymentPage.getCheckinPayment()), is(expectedCheckinDate));
        Assertion.assertThat("Night duration on the booking form and payment page should be equal", paymentPage.getDurationPayment(), is(expectedNightDuration));
        Assertion.assertThat("Total price on the booking form and payment page should be equal", convertPrice(paymentPage.getTotalPricePayment()), is(convertPrice(expectedTotalPrice)));
    }

    @DataProvider(name = "createHomesBookingLoginDataProvider")
    public Object[][] createHomesBookingLoginDataProvider() {
        return getObjectParams("ACC_createHomesBookingLogin");
    }

    @Author(names = {"Kartik"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "SRP")
    @Test
    public void verifyAllSrpComponents() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyAllSrpComponents");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        HomesSearchResultPage<WebDriver> homesSearchResultPage = HomesSearchResultPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        Assertion.assertThat("user is on the hotel search result page", searchResultPage.isSearchResultShown(), is(true));
    }

    @Author(names = {"Kartik", "Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "Filter")
    @Test
    public void verifyUserCanApplySortOnSrp() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyUserCanApplySortOnSrp");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");
        String filter = map.get("filter");
        String totalHotel = map.get("totalHotel");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("User click sort filter button");
        searchResultPage.selectFilterSortBy(filter);

        step("Get hotel price data");
        Boolean isSortByLowestPriceValid = searchResultPage.isSortByLowestPriceValid(totalHotel);

        step("Sort by filter assertion");
        Assertion.assertThat("short According to filter is valid", isSortByLowestPriceValid ,is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecentSearchPlatformHomesGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyRecentSearchPlatformHomesGuest");
        BaseTest.paramsThreadLocal.set(map);

        String landingPageLocation = map.get("landingPageLocation");
        String landingPageHomesName = map.get("landingPageHomesName");
        String srpLocation = map.get("srpLocation");
        String srpHomesName = map.get("srpHomesName");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on non-login mode");
        loginPage.loginAsGuest();

        step("User choose the see all button on the homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "");
        hotelLandingPage.clickFirstSeeAllButton();
        hotelLandingPage.fillNightCheckInForm();
        String currentLocation = searchResultPage.getCurrentLocation();

        step("Get the check-in/out date, total room and guest data on the SRP");
        String checkInOutDate = searchResultPage.getCheckInOutDate();
        searchResultPage.getFirstHotelName();

        step("User choose the homes card on the homes landing Page");
        homePage.goToHomeDeeplink();
        homePage.openVerticalPageDeeplink("Homes", "", "");
        hotelLandingPage.clickFirstHotelCard();

        step("Get the homes data on the PDP");
        String homesName = detailPage.getHotelNameDetail();
        String homesLocation = detailPage.getHotelLocationDetail();

        step("Find the location from see all button on the home page recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("Homes");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(true, currentLocation, checkInOutDate), is(true));

        step("Find the homes name on the home page recent search section");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(false, homesName, homesLocation), is(true));

        step("User search the location on homes landing page");
        homePage.goToHomeDeeplink();
        homePage.openVerticalPageDeeplink("Homes", "", "");
        homesLandingPage.searchStayLocation(landingPageLocation);
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Get the check-in/out date, total room and guest data on the SRP");
        checkInOutDate = searchResultPage.getCheckInOutDate();
        searchResultPage.getFirstHotelName();

        step("User search the homes name on homes landing page");
        homePage.goToHomeDeeplink();
        homePage.openVerticalPageDeeplink("Homes", "", "");
        homesLandingPage.searchStayLocation(landingPageHomesName);
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Get the homes data on the PDP");
        detailPage.getHotelNameDetail();
        homesLocation = detailPage.getHotelLocationDetail();

        step("Find the location from landing page search on the home page recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("Homes");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(true, landingPageLocation, checkInOutDate), is(true));

        step("Find the homes name from landing page search on the recent search section");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(false, landingPageHomesName, homesLocation), is(true));

        step("User search the location on the SRP");
        homePage.goToHomeDeeplink();
        homePage.openVerticalPageDeeplink("Homes", "", "");
        homesLandingPage.searchStayLocation(landingPageLocation);
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();
        searchResultPage.changeLocation(srpLocation);
        searchResultPage.clickSubmit();

        step("Get the check-in/out date, total room and guest data on the SRP");
        checkInOutDate = searchResultPage.getCheckInOutDate();

        step("User search the homes name on the SRP");
        searchResultPage.changeLocation(srpHomesName);
        searchResultPage.clickSubmit();

        step("Get the homes data on the PDP");
        detailPage.getHotelNameDetail();
        homesLocation = detailPage.getHotelLocationDetail();

        step("Find the location on the home page recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("Homes");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(true, srpLocation, checkInOutDate), is(true));

        step("Find the homes name on the home page recent search section");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(false, srpHomesName, homesLocation), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecentSearchPlatformHomesLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyRecentSearchPlatformHomesLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "homes";
        String dateType = map.get("dateType");
        String locationType = map.get("locationType");
        String landingPageLocation = map.get("landingPageLocation");
        String landingPageDate = map.get("landingPageDate");
        int landingPageTotalGuest = Integer.parseInt(map.get("landingPageTotalGuest"));
        int landingPageTotalChild = Integer.parseInt(map.get("landingPageTotalChild"));
        String landingPageHomesName = map.get("landingPageHomesName");
        String landingPageDateNew = map.get("landingPageDateNew");
        int landingPageTotalGuestNew = Integer.parseInt(map.get("landingPageTotalGuestNew"));
        int landingPageTotalChildNew = Integer.parseInt(map.get("landingPageTotalChildNew"));
        String srpLocation = map.get("srpLocation");
        String srpDate = map.get("srpDate");
        int srpTotalGuest = Integer.parseInt(map.get("srpTotalGuest"));
        int srpTotalChild = Integer.parseInt(map.get("srpTotalChild"));
        String srpHomesName = map.get("srpHomesName");
        String srpDateNew = map.get("srpDateNew");
        int srpTotalGuestNew = Integer.parseInt(map.get("srpTotalGuestNew"));
        int srpTotalChildNew = Integer.parseInt(map.get("srpTotalChildNew"));
        String srpHomesNameNew = map.get("srpHomesNameNew");
        String roomListDate = map.get("roomListDate");
        int roomListTotalGuest = Integer.parseInt(map.get("roomListTotalGuest"));
        int roomListTotalChild = Integer.parseInt(map.get("roomListTotalChild"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("User change the search date, guest, child data on search by location Landing Page");
        homePage.openVerticalPageDeeplink("Homes", language, "");
        homesLandingPage.searchStayLocation(landingPageLocation);
        landingPage.setStayDate(dateType, landingPageDateNew);
        homesLandingPage.openTotalGuest();
        landingPage.setTotalGuest(landingPageTotalGuestNew);
        landingPage.setTotalChildren(landingPageTotalChildNew);
        homesLandingPage.clickSubmit();
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Get the check-in/out date, total room and guest data on the SRP");
        String checkInOutDate = searchResultPage.getCheckInOutDate();
        searchResultPage.getFirstHotelName();

        step("Find the location from landing page search on the home page recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("homes");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(true, landingPageLocation, checkInOutDate), is(true));
        int expectedTotalGuest = landingPageTotalGuestNew+landingPageTotalChildNew;
        Assertion.assertThat("Total all guest for adult "+landingPageTotalGuestNew+" and child "+landingPageTotalChildNew+" should be displayed as expected: " + expectedTotalGuest, homePage.checkTotalAllGuestRecentSearch(expectedTotalGuest), is(true));

        step("User change the search date, guest, child data on search by homes name Landing Page");
        homePage.openVerticalPageDeeplink("Homes", language, "");
        homesLandingPage.searchStayLocation(landingPageHomesName);
        landingPage.setStayDate(dateType, landingPageDate);
        homesLandingPage.openTotalGuest();
        landingPage.setTotalGuest(landingPageTotalGuest);
        landingPage.setTotalChildren(landingPageTotalChild);
        homesLandingPage.clickSubmit();
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Get the homes data on the PDP");
        detailPage.getHotelNameDetail();
        String hotelLocation = detailPage.getHotelLocationDetail();

        step("Find the homes name from landing page search on the recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("homes");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(false, landingPageHomesName, hotelLocation), is(true));

        step("User change the search date, guest, child data on search by location SRP");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, landingPageLocation, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), 1, 1, locationId, locationType, landingPageLocation, checkInDate, checkOutDate, language);
        landingPage.fillNightCheckInForm();
        searchResultPage.changeLocation(srpLocation);
        landingPage.setStayDate(dateType, srpDate);
        homesLandingPage.openTotalGuest();
        landingPage.setTotalGuest(srpTotalGuest);
        landingPage.setTotalChildren(srpTotalChild);
        homesLandingPage.clickSubmit();
        searchResultPage.clickSubmit();

        step("Get the check-in/out date, total room and guest data on the SRP");
        checkInOutDate = searchResultPage.getCheckInOutDate();
        searchResultPage.getFirstHotelName();

        step("Find the location on the home page recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("Homes");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(true, srpLocation, checkInOutDate), is(true));
        expectedTotalGuest = srpTotalGuest+srpTotalChild;
        Assertion.assertThat("Total all guest for adult "+srpTotalGuest+" and child "+srpTotalChild+" should be displayed as expected: " + expectedTotalGuest, homePage.checkTotalAllGuestRecentSearch(expectedTotalGuest), is(true));

        step("User change the search date, guest, child data on search by homes name SRP");
        hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, srpLocation, locationType);
        locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), 1, 1, locationId, locationType, srpLocation, checkInDate, checkOutDate, language);
        landingPage.fillNightCheckInForm();
        searchResultPage.changeLocation(srpHomesName);
        landingPage.setStayDate(dateType, srpDateNew);
        homesLandingPage.openTotalGuest();
        landingPage.setTotalGuest(srpTotalGuestNew);
        landingPage.setTotalChildren(srpTotalChildNew);
        homesLandingPage.clickSubmit();
        searchResultPage.clickSubmit();

        step("Get the homes data on the PDP");
        detailPage.getHotelNameDetail();
        hotelLocation = detailPage.getHotelLocationDetail();

        step("Find the homes name on the home page recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("homes");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(false, srpHomesName, hotelLocation), is(true));

        step("Open the room list page");
        hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, srpHomesNameNew, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 1, 1, checkInDate, checkOutDate);
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("User change the search date, guest, child data on Room List");
        roomListPage.clickChangeButton();
        landingPage.setStayDate(dateType, roomListDate);
        homesLandingPage.openTotalGuest();
        landingPage.setTotalGuest(roomListTotalGuest);
        landingPage.setTotalChildren(roomListTotalChild);
        homesLandingPage.clickSubmit();
        searchResultPage.clickSubmit();

        step("Get the homes data on the PDP");
        searchResultPage.getCheckInOutDate();
        searchResultPage.clickBackButton();
        detailPage.getHotelNameDetail();
        hotelLocation = detailPage.getHotelLocationDetail();

        step("Find the homes name from landing page search on the recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("homes");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(false, srpHomesNameNew, hotelLocation), is(true));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifyRecentSearchHomesLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyRecentSearchHomesLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        int totalDestination = 5;

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Open search form");
        homesLandingPage.openSearchLocation();

        step("Select first popular destination");
        String firstDestination = homesLandingPage.chooseDestination(location);

        step("Submit the selected destination");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Back to the search form on the landing page");
        searchResultPage.clickBackButton();
        homesLandingPage.openSearchLocation();

        step("Select " + totalDestination + " popular destinations");
        homesLandingPage.chooseDestinationLoop(totalDestination, firstDestination);

        step("Choose the recent search");
        String selectedRecentSearch = homesLandingPage.chooseRecentSearch();

        step("Redirect to the SRP");
        String srpLocation = searchResultPage.getCurrentLocation();
        Assertion.assertThat("Selected recent search location should be contains the SRP location", selectedRecentSearch, is(containsString(srpLocation)));

        step("Back to the recent search on the landing page");
        searchResultPage.clickBackButton();
        homesLandingPage.clickSearchFieldText();

        step("Find the first selected destination");
        boolean actualStatus = homesLandingPage.findRecentSearch(firstDestination, totalDestination);
        Assertion.assertThat("Maximum recent search should be equal " + totalDestination, actualStatus, is(false));

        step("Delete the recent search");
        homesLandingPage.clickRecentSearchDeleteButton();
        Assertion.assertThat("Recent search section should be removed", homesLandingPage.isRecentSearchVisible(), is(false));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void checkRecentlySearchedHomesLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkRecentlySearchedHomesLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("getting details");
        String dateGuestDetail = searchResultPage.getDateRoomGuestDetail();

        step("Click Back Button");
        searchResultPage.clickBackButton();

        step("Click Search field");
        homesLandingPage.clickSearchFieldText();

        Assertion.assertThat("Recently Searched on Search page is correct", landingPage.isRecentlySearchAvailable(location), is(true));
        Assertion.assertThat("Recently Searched on landing page is correct", landingPage.recentlySearchedStatus(dateGuestDetail, location, landingPage.getAreaText(location)), is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void checkRecentlySearchedHomes() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkRecentlySearchedHomes");
        BaseTest.paramsThreadLocal.set(map);

        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("getting details");
        String dateGuestDetail = searchResultPage.getDateRoomGuestDetail();

        step("Click Back Button");
        searchResultPage.clickBackButton();

        step("Click Search field");
        homesLandingPage.clickSearchFieldText();

        Assertion.assertThat("Recently Searched on Search page is correct", landingPage.isRecentlySearchAvailable(location), is(true));
        Assertion.assertThat("Recently Searched on landing page is correct", landingPage.recentlySearchedStatus(dateGuestDetail, location, landingPage.getAreaText(location)), is(true));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "PDP")
    @Test
    public void homesCheckDeeplinkBehaviorGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("homesCheckDeeplinkBehaviorGuest");
        BaseTest.paramsThreadLocal.set(map);

        String location = map.get("location");
        String locationType = map.get("locationType").toUpperCase();
        String homesName = map.get("homesName");
        int totalGuest = Integer.parseInt(map.get("totalGuest"));
        int totalRoom = Integer.parseInt(map.get("totalRoom"));
        int totalNight = Integer.parseInt(map.get("totalNight"));
        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "homes";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on non-login mode");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Get the SRP deeplink parameter");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", location, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;

        step("user click SRP deeplink");
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), totalRoom, totalGuest, locationId, locationType, location, checkInDate, checkOutDate, language);
        landingPage.fillNightCheckInForm();
        Assertion.assertThat("Check SRP content", searchResultPage.isSearchResultShown(), is(true));
        Assertion.assertThat("successfully open apps and redirected to SRP", searchResultPage.getCurrentLocation().toLowerCase(), is(location.toLowerCase()));

        step("Get the SRP deeplink parameter");
        hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", homesName, accommodationType);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(0).country.name;

        step("user click SRP deeplink with redirect");
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), totalRoom, totalGuest, hotelId, "HOTEL", homesName, checkInDate, checkOutDate, language, true);
        landingPage.fillNightCheckInForm();
        Assertion.assertThat("Check SRP content", searchResultPage.isSearchResultShown(), is(true));
        Assertion.assertThat("First hotel name is correct", searchResultPage.getFirstHotelName().toLowerCase(), is(homesName.toLowerCase()));
        Assertion.assertThat("First hotel name is highlighted", searchResultPage.checkHighlightedResult(), is(true));

        step("user click PDP deeplink");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, totalGuest, checkInDate, checkOutDate);
        Assertion.assertThat("successfully open apps and redirected to PDP", detailPage.getHotelNameDetail().toLowerCase(), is(homesName.toLowerCase()));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "PDP")
    @Test
    public void homesCheckDeeplinkBehaviorLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("homesCheckDeeplinkBehaviorLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String locationType = map.get("locationType").toUpperCase();
        String homesName = map.get("homesName");
        int totalGuest = Integer.parseInt(map.get("totalGuest"));
        int totalRoom = Integer.parseInt(map.get("totalRoom"));
        int totalNight = Integer.parseInt(map.get("totalNight"));
        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "homes";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Get the SRP deeplink parameter");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;

        step("user click SRP deeplink");
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), totalRoom, totalGuest, locationId, locationType, location, checkInDate, checkOutDate, language);
        landingPage.fillNightCheckInForm();
        Assertion.assertThat("Check SRP content", searchResultPage.isSearchResultShown(), is(true));
        Assertion.assertThat("successfully open apps and redirected to SRP", searchResultPage.getCurrentLocation().toLowerCase(), is(location.toLowerCase()));

        step("Get the SRP deeplink parameter");
        hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, homesName, accommodationType);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(0).country.name;

        step("user click SRP deeplink with redirect");
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), totalRoom, totalGuest, hotelId, "HOTEL", homesName, checkInDate, checkOutDate, language, true);
        landingPage.fillNightCheckInForm();
        Assertion.assertThat("Check SRP content", searchResultPage.isSearchResultShown(), is(true));
        Assertion.assertThat("First hotel name is correct", searchResultPage.getFirstHotelName().toLowerCase(), is(homesName.toLowerCase()));
        Assertion.assertThat("First hotel name is highlighted", searchResultPage.checkHighlightedResult(), is(true));

        step("user click PDP deeplink");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, totalGuest, checkInDate, checkOutDate);
        Assertion.assertThat("successfully open apps and redirected to PDP", detailPage.getHotelNameDetail().toLowerCase(), is(homesName.toLowerCase()));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void homesCheckRecentlyViewedLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("homesCheckRecentlyViewedLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String[] hotelNameList = map.get("hotelNameList").split("-");
        List<String> hotelList = new ArrayList<>(List.of(hotelNameList));
        if (hotelList.size() < 11) Assertion.skip("Skip this test because of homes name less than 11 with \"-\" as separator");
        int maxRecentlyViewed = 10;

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);

        step("user enter PDP via deeplink by " + hotelList.size() + " times with homes name: " + hotelList);
        hotelPage.openPDPDeeplink(hotelList, username, password, "homes");

        step("Open homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Select recent view tab on the landing page");
        homesLandingPage.clickSearchFieldText();

        step("user click x button on each recent view item");
        Assertion.assertThat("recently viewed showing latest last " + maxRecentlyViewed + " PDP visited by users from all entry point", landingPage.deleteRecentViewItems(hotelList, maxRecentlyViewed), is(maxRecentlyViewed));
        Assertion.assertThat("homes card deleted from recently viewed section and it's not shown on Homes Landing Page", landingPage.isRecentViewedLandingPageEmpty(), is(true));

        step("Click search field on the landing page");
        landingPage.clickSearchField();
        Assertion.assertThat("Recently viewed section is not shown on Search drawer", landingPage.isRecentViewedSearchPopupEmpty(), is(true));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void homesCheckRecentlyViewedGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("homesCheckRecentlyViewedGuest");
        BaseTest.paramsThreadLocal.set(map);

        String[] hotelNameList = map.get("hotelNameList").split("-");
        List<String> hotelList = new ArrayList<>(List.of(hotelNameList));
        if (hotelList.size() < 11) Assertion.skip("Skip this test because of homes name less than 11 with \"-\" as separator");
        int maxRecentlyViewed = 10;

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on non-login mode");
        loginPage.loginAsGuest();

        step("user enter PDP via deeplink by " + hotelList.size() + " times with homes name: " + hotelList);
        hotelPage.openPDPDeeplink(hotelList, "", "", "homes");

        step("Open homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Select recent view tab on the landing page");
        homesLandingPage.clickSearchFieldText();

        if (Configs.PLATFORM == Platform.ANDROID) {
            Assertion.assertThat("recently viewed showing latest last " + maxRecentlyViewed + " PDP visited by users from all entry point", landingPage.getRecentViewedItemCount(hotelList, maxRecentlyViewed), is(maxRecentlyViewed));

            step("user click Clear All button");
            landingPage.clearAllRecentViewed();
        } else {

            step("user click x button on each recent view item");
            Assertion.assertThat("recently viewed showing latest last " + maxRecentlyViewed + " PDP visited by users from all entry point", landingPage.deleteRecentViewItems(hotelList, maxRecentlyViewed), is(maxRecentlyViewed));
        }

        Assertion.assertThat("homes card deleted from recently viewed section and it's not shown on Homes Landing Page", landingPage.isRecentViewedLandingPageEmpty(), is(true));

        step("Click search field on the landing page");
        landingPage.clickSearchField();
        Assertion.assertThat("Recently viewed section is not shown on Search drawer", landingPage.isRecentViewedSearchPopupEmpty(), is(true));
    }

    @Author(names = {"Okta"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifySearchFormLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifySearchFormLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Open search form");
        boolean searchForm = homesLandingPage.isSearchFormExist();

        step("Open search location");
        boolean searchLocation = homesLandingPage.isSearchLocationExist();

        step("Open guest form");
        boolean guestForm = homesLandingPage.isGuestFormExist();

        step("Open calendar");
        boolean calendar = homesLandingPage.isCalendarExist();

        boolean status = searchForm && searchLocation && guestForm && calendar;

        step("Verify all search forms are opened");
        Assertion.assertThat("Verify all search forms are opened", status, is(true));
    }

    @Author(names = {"Okta"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifySearchFormGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifySearchFormGuest");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Open search form");
        boolean searchForm = homesLandingPage.isSearchFormExist();

        step("Open search location");
        boolean searchLocation = homesLandingPage.isSearchLocationExist();

        step("Open guest form");
        boolean guestForm = homesLandingPage.isGuestFormExist();

        step("Open calendar");
        boolean calendar = homesLandingPage.isCalendarExist();

        boolean status = searchForm && searchLocation && guestForm && calendar;

        step("Verify all search forms are opened");
        Assertion.assertThat("Verify all search forms are opened", status, is(true));
    }

    @Author(names = {"Kartik", "Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "Filter")
    @Test
    public void verifyAccommodationTypesFromGeneralFilter() {
        Map<String, String> map = getParam("verifyAccommodationTypesFromGeneralFilter");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");
        String filterName = map.get("filterName");
        String totalHotel = map.get("totalHotel");
        String accomadationType  = map.get("accomadationType");
        String filterType  = map.get("filterType");

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        HomesSearchResultPage<WebDriver> homesSearchResultPage = HomesSearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Click filter button");
        searchResultPage.clickFilterButton();

        step("User select Home Accommodation Type from filter");
        searchResultPage.swipeToFilter(filterName);
        searchResultPage.clickSeeAllBtnFilter(filterName);
        homesSearchResultPage.selectAccomodationTypeFilter(accomadationType);
        searchResultPage.clickFilterSubmit();
        searchResultPage.clickFilterSubmit();

        step("filter button have green dot");
        Assertion.assertThat("filter button have green dot", searchResultPage.isFilterActivated("Filter"), is(true));

        step("is filter applied for n cards");
        Assertion.assertThat("is filter applied for n cards", searchResultPage.checkHotelCardList(filterType, Integer.parseInt(totalHotel), accomadationType.split("/")), is(true));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifySearchFormGuestDrawerLogin() {
        Map<String, String> map = getParam("verifySearchFormGuestDrawerLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        int totalGuest = Integer.parseInt(map.get("totalGuest"));
        int totalChild = Integer.parseInt(map.get("totalChild"));

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Open search form");
        homesLandingPage.clickSearchFieldText();

        step("Open guest form");
        homesLandingPage.openTotalGuest();

        step("set total guest is " + totalGuest + " and children is " + totalChild);
        landingPage.setTotalGuest(totalGuest);
        landingPage.setTotalChildren(totalChild);
        homesLandingPage.clickSubmit();

        step("get the total guest");
        int actualTotalGuest = Integer.parseInt(homesLandingPage.getTotalGuest());

        Assertion.assertThat("Total guest input should be equal total guest on the landing page", actualTotalGuest, is(totalGuest + totalChild));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifySearchFormDatePickerLogin() {
        Map<String, String> map = getParam("verifySearchFormDatePickerLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String date_type = map.get("date_type");
        String date = map.get("date");

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Open search form");
        homesLandingPage.clickSearchFieldText();

        step("set stay date");
        String[] checkInOut = landingPage.setStayDate(date_type, date);

        Assertion.assertThat("Check-in and check-out date should be equal to those selected on the calendar", landingPage.getSelectedCheckInOut().trim(), is(landingPage.getExpectedCheckInOut(checkInOut).trim()));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifySearchFormDestinationFormLogin() {
        Map<String, String> map = getParam("verifySearchFormDestinationFormLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String destination = map.get("destination");

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Open search form");
        homesLandingPage.openSearchLocation();

        step("Select " + destination + " as a popular destination");
        destination = homesLandingPage.chooseDestination(destination);

        Assertion.assertThat("Destination on search field should be equal with the selected popular destination", homesLandingPage.getSearchDestination(), is(containsString(destination)));
    }

    @Author(names = {"Okta"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "Filter")
    @Test
    public void filterByFrequentlyPriceRangeGuest() {
        Map<String, String> map = getParam("filterByFrequentlyPriceRangeGuest");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");
        String totalHotel = map.get("totalHotel");
        String priceRange = map.get("priceRange");
        String currency = "IDR";

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();
        homePage.changeCurrency(currency, "");

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Open price filter");
        searchResultPage.openPriceFilter();

        step("Select price on filter");
        searchResultPage.selectPriceFilterByRange(priceRange,currency);
        searchResultPage.clickFilterSubmit();

        step("Assert price and filter button have green dot");
        Assertion.assertThat("Price and filter button have green dot", searchResultPage.isFilterActivated("Price"), is(true));

        step("Assert price is match with price filter by range");
        Assertion.assertThat("Price is match with price filter by range", searchResultPage.isPriceFilterByRangeCorrect(priceRange, totalHotel), is(true));
    }

    @Author(names = {"Naufal"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "CreateBooking")
    @Test
    public void verifyNumberOfBedroomsFromGeneralFilterGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyNumberOfBedroomsFromGeneralFilterGuest");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        HomesSearchResultPage<WebDriver> homesSearchResultPage = HomesSearchResultPage.getInstance(session);

        // this test data format is '[insert number of Bed]-0', it's needed due to adjustment for final assertion method heckHotelCardList require to make param as an array
        String location = map.get("location");
        String totalBedroom = map.get("totalBedroom");
        String totalHomes = map.get("totalHomes");

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();

        step("Open homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("search stay location");
        homesLandingPage.searchStayLocation(location);

        step("submit search");
        homesLandingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Click bed filter button");
        homesSearchResultPage.clickBedroomsFilterButton();
        homesSearchResultPage.addTotalBedroom(totalBedroom.split("-")[0]);

        step("Submit filter");
        searchResultPage.clickFilterSubmit();

        step("Assertion");
        Assertion.assertThat("green icon is applied on general filter and bedrooms filter", searchResultPage.isFilterActivated("Bedrooms"), is(true));
        Assertion.assertThat("all product card should contains room more than or equal to " + totalBedroom, searchResultPage.checkHotelCardList("Bedrooms", Integer.parseInt(totalHomes), totalBedroom.split("-")), is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "Filter")
    @Test
    public void filterByBenefitsGuest() {
        Map<String, String> map = getParam("filterByBenefitsGuest");
        BaseTest.paramsThreadLocal.set(map);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        String location = map.get("location");
        String totalHotel = map.get("totalHotel");
        String benefitType = map.get("benefitType");
        String filterType = map.get("filterType");

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Open filter");
        searchResultPage.clickFilterButton();

        step("Select filter");
        searchResultPage.swipeToFilter(benefitType);
        searchResultPage.selectBenefit(benefitType);

        step("filter button have green dot");
        Assertion.assertThat("filter button have green dot", searchResultPage.isFilterActivated("Filter"), is(true));

        step("is filter applied for n cards");
        Assertion.assertThat("is filter applied for n cards", searchResultPage.checkHotelCardList(filterType, Integer.parseInt(totalHotel), benefitType.split("/")), is(true));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "Filter")
    @Test
    public void verifySRPApplyInterceptFilterHomes() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifySRPApplyInterceptFilterHomes");
        BaseTest.paramsThreadLocal.set(map);

        String location = map.get("location");
        String totalHotel = map.get("totalHotel");
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency, "");

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Submit search");
        homesLandingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Finding intercept filter and randomly select the available option");
        FilterInfo filterInfo = searchResultPage.findAndChooseInterceptFilter();

        step("Assert if the SRP is valid after using intercept filter");
        Assertion.assertThat("Assert if the bottom bar has green dot", searchResultPage.isFilterActivated(filterInfo.filterType), is(true));
        Assertion.assertThat("Assert if first '"+totalHotel+"' homes card is having price as expected", searchResultPage.checkHotelCardList(filterInfo.filterType,Integer.parseInt(totalHotel), filterInfo.choosenFilter.split("-")), is(true));
    }

    @Author(names = {"Yazid", "Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "Filter")
    @Test
    public void filterByFrequentlyPriceRangeGeneralFilter() {
        Map<String, String> map = getParam("filterByFrequentlyPriceRangeGeneralFilter");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");
        String filterType = map.get("filterType");
        String totalHotel = map.get("totalHotel");
        String priceRange = map.get("priceRange");
        String currency = "IDR";

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Click filter button");
        searchResultPage.clickFilterButton();

        step("Scroll until filter by " + filterType + " is visible");
        searchResultPage.swipeToFilter(filterType);

        step("Select price on filter");
        priceRange = searchResultPage.selectPriceFilterByRange(priceRange,currency);
        searchResultPage.clickFilterSubmit();

        step("Assert price and filter button have green dot");
        Assertion.assertThat("Price and filter button have green dot", searchResultPage.isFilterActivated("Price"), is(true));

        step("Assert price is match with price filter by range");
        Assertion.assertThat("Price is match with price filter by range", searchResultPage.isPriceFilterByRangeCorrect(priceRange, totalHotel), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "Filter")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void filterByAreasGuest() {
        Map<String, String> map = getParam("filterByAreasGuest");
        BaseTest.paramsThreadLocal.set(map);

        String area = map.get("area");
        String location = map.get("location");
        String filterType = map.get("filterType");
        String totalHotel = map.get("totalHotel");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Click filter button");
        searchResultPage.clickFilterButton();

        step("Scroll until filter by property rating is visible");
        searchResultPage.swipeToFilter(filterType);

        step("Click see all button on area filter");
        searchResultPage.clickSeeAllBtnFilter(filterType);

        step("Select area from testdata");
        String[] areaArray = searchResultPage.selectAreaFilter(area);

        step("Save area filter");
        searchResultPage.clickFilterSubmit();

        step("Submit filter");
        searchResultPage.clickFilterSubmit();

        step("Assert filter button have green dot");
        Assertion.assertThat("Filter button have green dot", searchResultPage.isFilterActivated("Filter"), is(true));

        step("Assert first " + totalHotel + " hotel cards have the right area");
        Assertion.assertThat("All hotel area is correct", searchResultPage.checkHotelCardList("area", Integer.parseInt(totalHotel), areaArray), is(true));
    }

    @Author(names = {"Okta"})
    @Module(name = "Filter")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void filterByPropertyRatingGuest() {
        Map<String, String> map = getParam("filterByPropertyRatingGuest");
        BaseTest.paramsThreadLocal.set(map);

        String rating = map.get("rating");
        String location = map.get("location");
        String filterType = map.get("filterType");
        String totalHotel = map.get("totalHotel");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Click filter button");
        searchResultPage.clickFilterButton();

        step("Scroll until filter by property rating is visible");
        searchResultPage.swipeToFilter(filterType);

        step("Select rating from testdata");
        String selectedRating = searchResultPage.selectPropertyRatingFilter(rating);

        step("Submit filter");
        searchResultPage.clickFilterSubmit();

        step("Assert filter button have green dot");
        Assertion.assertThat("Filter button have green dot", searchResultPage.isFilterActivated("Filter"), is(true));

        step("Assert first " + totalHotel + " hotel cards have the right property rating");
        Assertion.assertThat("All property rating is correct", searchResultPage.checkHotelCardList("property rating", Integer.parseInt(totalHotel), selectedRating.split("")), is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "Filter")
    @Test
    public void filterByFacilitiesGuest() {
        Map<String, String> map = getParam("filterByFacilitiesGuest");
        BaseTest.paramsThreadLocal.set(map);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        String location = map.get("location");
        String totalHotel = map.get("totalHotel");
        String filterType = map.get("filterType");
        String facilityType = map.get("facilityType");

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Open filter");
        searchResultPage.clickFilterButton();

        step("Select filter");
        searchResultPage.swipeToFilter(filterType);

        step("Click SEE ALL Button");
        searchResultPage.clickSeeAllBtnFilter(filterType);

        step("Select Facility ");
        searchResultPage.selectFacility(facilityType);

        step("Save facility filter");
        searchResultPage.clickFilterSubmit();

        step("Submit filter");
        searchResultPage.clickFilterSubmit();

        step("filter button have green dot");
        Assertion.assertThat("filter button have green dot", searchResultPage.isFilterActivated("Filter"), is(true));

        step("is filter applied for n cards");
        Assertion.assertThat("open card from SRP -> PDP -> check Facilities section", searchResultPage.checkHotelCardList("facility", Integer.parseInt(totalHotel), facilityType.split("_")), is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "SRP")
    @Test
    public void verifyAllSrpComponentsLogin() {
        Map<String, String> map = getParam("verifyAllSrpComponentsLogin");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        HomesSearchResultPage<WebDriver> homesSearchResultPage = HomesSearchResultPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        Assertion.assertThat("user is on the hotel search result page", homesSearchResultPage.verifySrpElements() && searchResultPage.isSearchResultShown(), is(true));
    }

    @Author(names = {"Kartik"})
    @Module(name = "Filter")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPriceRangeGeneralFilterGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyPriceRangeGeneralFilterGuest");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        String location = map.get("location");
        String totalHotel = map.get("totalHotel");
        String priceRange = map.get("priceRange");
        String filterType = map.get("filterType");
        String currency = "IDR";

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();
        homePage.changeCurrency(currency,"");

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("open and select price filter");
        searchResultPage.clickFilterButton();
        searchResultPage.selectPriceFilterByRange(priceRange,currency);
        searchResultPage.clickFilterSubmit();

        Assertion.assertThat("Green dot on applied filter is visible or not ",searchResultPage.isFilterActivated(filterType),is(true));
        Assertion.assertThat("Homes price is match with price filter by range", searchResultPage.isPriceFilterByRangeCorrect(priceRange, totalHotel), is(true));
    }

    @Author(names = {"Naufal"})
    @Module(name = "Filter")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPriceRangeFromPriceFilterGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyPriceRangeFromPriceFilterGuest");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        String location = map.get("location");
        String totalHotel = map.get("totalHotel");
        String priceRange = map.get("priceRange");
        String filterType = map.get("filterType");
        String currency = "IDR";

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency,"");

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("open and select price filter");
        searchResultPage.openPriceFilter();
        searchResultPage.selectPriceFilterByRange(priceRange, currency);
        searchResultPage.clickFilterSubmit();

        step("Assertion");
        Assertion.assertThat("Green dot on applied filter is visible or not ",searchResultPage.isFilterActivated(filterType),is(true));
        Assertion.assertThat("Homes price is match with price filter by range", searchResultPage.isPriceFilterByRangeCorrect(priceRange, totalHotel), is(true));
    }

    @Author(names = {"Okta"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifyViewMoreAndBackToTopButton() {
        Map<String, String> map = getParam("verifyViewMoreAndBackToTopButton");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Click View More button");
        boolean viewMore = homesLandingPage.clickViewMoreButton();

        step("Click Back To Top button");
        boolean backToTop = homesLandingPage.clickBackToTopButton();

        step("Verify buttons are working as expected");
        Assertion.assertThat("Verify buttons are working as expected", viewMore && backToTop, is(true));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifyViewMoreAndBackToTopButtonGuest() {
        Map<String, String> map = getParam("verifyViewMoreAndBackToTopButtonGuest");
        BaseTest.paramsThreadLocal.set(map);

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Click View More button");
        boolean viewMore = homesLandingPage.clickViewMoreButton();

        step("Click Back To Top button");
        boolean backToTop = homesLandingPage.clickBackToTopButton();

        step("Verify buttons are working as expected");
        Assertion.assertThat("Verify buttons are working as expected", viewMore && backToTop, is(true));
    }

    @Author(names = {"Kartik"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "Filter")
    @Test
    public void verifyBedroomsFromBedroomsFilterGuest() {
        Map<String, String> map = getParam("verifyBedroomsFromBedroomsFilterGuest");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");
        String totalHotel = map.get("totalHotel");
        String totalRoom  = map.get("totalRoom");

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        HomesSearchResultPage<WebDriver> homesSearchResultPage = HomesSearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Open Bedrooms filter");
        homesSearchResultPage.setBedroomsFilterHomes(totalRoom);
        searchResultPage.clickFilterSubmit();

        step("filter button have green dot");
        Assertion.assertThat("filter have green dot", searchResultPage.isFilterActivated("Bedrooms"), is(true));

        step("is filter applied for n cards");
        Assertion.assertThat("all product card should contains room more than or equal to " + totalRoom, searchResultPage.checkHotelCardList("Bedrooms", Integer.parseInt(totalHotel), totalRoom.split("-")), is(true));
    }

    @Author(names = {"Akbar","Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "Filter")
    @Test
    public void verifyPriceDisplayGuest() {
        Map<String, String> map = getParam("verifyPriceDisplayGuest");
        BaseTest.paramsThreadLocal.set(map);
        String currency = "IDR";
        String location = map.get("location");
        String dateType = map.get("date_type");
        String date = map.get("date");
        int totalGuest = Integer.parseInt(map.get("total_guest"));

        String filterType = map.get("filterType");
        String filterOption = map.get("filterOption");

        int totalHotel = Integer.parseInt(map.get("totalHotel"));
        int totalNight = Integer.parseInt(map.get("totalNight"));

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();
        homePage.changeCurrency(currency,"");

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Set stay date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set total guest");
        homesLandingPage.openTotalGuest();
        hotelLandingPage.setTotalGuest(totalGuest);
        homesLandingPage.clickSubmit();

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("reset filter");
        searchResultPage.getFirstHotelName();
        searchResultPage.resetFilter();

        step("Scan and get the data needed");
        String [] additionalData = new String[] {String.valueOf(totalNight)};
        String[] supportData = searchResultPage.getHotelCardList("Price",totalHotel, additionalData);

        step("Click filter button");
        searchResultPage.clickFilterButton();

        step("Scroll untill filter by '"+filterType+"' is visible");
        searchResultPage.swipeToFilter(filterType);

        step("Select '"+filterOption+"'");
        searchResultPage.selectHotelPriceDisplay(filterOption);

        step("Submit filter");
        searchResultPage.clickFilterSubmit();

        step("Assert bottom bar has green dot");
        Assertion.assertThat("Assert if the bottom bar has green dot", searchResultPage.isFilterActivated("Filter"), is(true));

        step("Assert price is as expected");
        Assertion.assertThat("Assert if first '"+totalHotel+"' hotel card is having the right price displayed", searchResultPage.checkHotelCardList("total_price",totalHotel, supportData), is(true));
    }

    @Author(names = {"Okta", "Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "CreateBooking")
    @Test
    public void completeBookingWithTierLogin() {
        Map<String, String> map = getParam("completeBookingWithTierLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        HomesSearchResultPage<WebDriver> homesSearchResultPage = HomesSearchResultPage.getInstance(session);
        HomesDetailPage<WebDriver> homesDetailPage = HomesDetailPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HomesRoomListPage<WebDriver> homesRoomListPage = HomesRoomListPage.getInstance(session);
        HomesRoomDetailPage<WebDriver> homesRoomDetailPage = HomesRoomDetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Set total guest");
        homesLandingPage.openTotalGuest();
        hotelLandingPage.setTotalGuest(1);
        homesLandingPage.clickSubmit();

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Select homes with loyalty badge in SRP");
        homesSearchResultPage.selectHomesWithLoyaltyBenefit();

        step("Check loyalty benefit in PDP");
        boolean loyaltyDetail = homesDetailPage.checkLoyaltyBenefit();

        step("Go to room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open room with loyalty benefit on the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        homesRoomListPage.selectRoomWithLoyaltyBenefit();

        step("Check loyalty benefit in room detail");
        boolean loyaltyRoomDetail = homesRoomDetailPage.checkLoyaltyBenefit();

        step("Select room and create booking");
        homesRoomDetailPage.selectRoom();

        step("Get price in booking form");
        String priceBookingForm = bookingFormPage.getTotalPriceBookingForm();

        step("Check loyalty benefit in booking form");
        String loyaltyBenefitValueText = bookingFormPage.loyaltyBenefitValueText();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Get price in payment page");
        paymentPage.checkOrderID();
        String pricePaymentPage = paymentPage.getTotalPricePayment();

        step("Open price detail");
        paymentPage.openPriceDetail();

        step("Get price in price detail");
        String priceDetail = paymentPage.getTotalPricePayment();

        step("Check loyalty benefit in price detail");
        boolean loyaltyPriceDetail = paymentPage.checkLoyaltyBenefitPriceDetail();

        Assertion.assertThat("Loyalty benefit exist on PDP", loyaltyDetail, is(true));
        Assertion.assertThat("Loyalty benefit exist on room detail", loyaltyRoomDetail, is(true));
        Assertion.assertThat("Loyalty benefit exist on booking form", loyaltyBenefitValueText, is(notNullValue()));
        Assertion.assertThat("Loyalty benefit exist on price detail", loyaltyPriceDetail, is(true));
        Assertion.assertThat("Price on the booking form should be equal on the payment page", priceBookingForm, is(pricePaymentPage));
        Assertion.assertThat("Calculation price on the payment page should correct", pricePaymentPage, is(priceDetail));
    }

    @Author(names = {"Akbar", "Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "Filter")
    @Test
    public void verifyCombinationFromGeneralFilterGuest() {
        Map<String, String> map = getParam("verifyCombinationFromGeneralFilterGuest");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");
        String dateType = map.get("date_type");
        String date = map.get("date");
        int totalGuest = Integer.parseInt(map.get("total_guest"));

        String[] filterTypes = map.get("filterTypes").split("-");
        String[] filterOptions = map.get("filterOptions").split("-");

        List<String> param = new ArrayList<>(Arrays.asList(filterTypes));
        param.add("separator");
        param.addAll(Arrays.asList(filterOptions));

        int totalHotel = Integer.parseInt(map.get("totalHotel"));

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("filterTypes : " + filterTypes[0]);

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();
        homePage.changeCurrency("IDR", "");

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", "", "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("Set stay date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Set total guest");
        homesLandingPage.openTotalGuest();
        hotelLandingPage.setTotalGuest(totalGuest);
        homesLandingPage.clickSubmit();

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Click filter button");
        searchResultPage.clickFilterButton();

        step("Select multiple filter based on testdata");
        searchResultPage.handleMultipleFilter(filterTypes, filterOptions);

        step("Submit filter");
        searchResultPage.clickFilterSubmit();

        Assertion.assertThat("Check the filter button has active dot", searchResultPage.isFilterActivated("filter"), is(true));
        Assertion.assertThat("Check the price filter button has active dot", searchResultPage.isFilterActivated("price"), is(true));
        Assertion.assertThat("Check the bedrooms filter button has active dot", searchResultPage.isFilterActivated("bedrooms"), is(true));
        Assertion.assertThat("Assert if first '"+totalHotel+"' hotel card is having the right attribute displayed",
                searchResultPage.checkHotelCardList("multiple",totalHotel, param.toArray(new String[0])), is(true));
    }

    @Author(names = {"Akbar"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifyThreePointsBannerGuest() {
        Map<String, String> map = getParam("verifyThreePointsBannerGuest");
        BaseTest.paramsThreadLocal.set(map);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HomesPage<WebDriver> homesPage = HomesPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Get three points banner content from the API");
        String language = homePage.getLanguageApp();
        String section = "THREE_POINTS_BANNER";
        HomesData data = homesPage.getContentLandingPageUsingPojo(false, "guest", "-", language, section);
        Assertion.assertThat("Success to get three points banner content from the API", !data.title.isEmpty(), is(true));

        String threePointBannerRedirectTargetApi = data.contentData.redirectTarget;
        String threePointBannerMainTitleApi = data.title;
        String threePointBannerSubTitleApi = data.subTitle;
        Boolean threePointBannerShowButtonApi = data.showButton;
        String threePointBannerButtonTextApi = data.buttonText;
        ArrayList<HomesData.ListOfTitleAndSubTitle> threePointBannerContentListApi = data.listOfTitleAndSubTitles;

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", language, "");
        String vertical = "Homes";

        step("Swipe to Three Points Banner Section");
        homesLandingPage.swipeToThreePointsBanner();

        step("Get three points banner data from the APP");
        String threePointBannerMainTitleApp = homesLandingPage.getThreePointsBannerAttribute("content.title");
        String threePointBannerSubTitleApp = homesLandingPage.getThreePointsBannerAttribute("content.subTitle");
        Boolean threePointBannerShowButtonApp = Boolean.parseBoolean(homesLandingPage.getThreePointsBannerAttribute("content.showButton"));
        String threePointBannerButtonTextApp = homesLandingPage.getThreePointsBannerAttribute("content.buttonText");
        ArrayList<HomesData.ListOfTitleAndSubTitle> threePointBannerContentListApp = homesLandingPage.getThreePointsBannerContentList();

        step("Click the button on three points main banner");
        homesLandingPage.clickThreePointBannersButton();

        step("Comparing API response with NHA Landing Page");
        Assertion.assertThat("Three Points Banner Main Title on the App should be equal with API", threePointBannerMainTitleApi, is(threePointBannerMainTitleApp));
        Assertion.assertThat("Three Points Banner Main Sub Title on the App should be equal with API", threePointBannerSubTitleApi, is(threePointBannerSubTitleApp));
        Assertion.assertThat("Three Points Banner Button on the App should be displayed", threePointBannerShowButtonApi, is(threePointBannerShowButtonApp));
        Assertion.assertThat("Three Points Banner Button Text on the App should be equal with API", threePointBannerButtonTextApi, is(threePointBannerButtonTextApp));
        Assertion.assertThat("Three Points Banner Content List on the App should be equal with API", homesLandingPage.compareThreeBannersContentList(threePointBannerContentListApi, threePointBannerContentListApp), is(true));

        step("Comparing API response with redirectTarget option '" + threePointBannerRedirectTargetApi + "'");
        Boolean isRedirectValid = hotelLandingPage.verifyRedirectTargetWithPojo(data.contentData,language, vertical);
        Assertion.assertThat("Three Points Banner Redirect on the App should be equal with API", isRedirectValid, is(true));
    }

    @Author(names = {"Akbar"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifyThreePointsBannerLogin() {
        Map<String, String> map = getParam("verifyThreePointsBannerLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        boolean login = Boolean.parseBoolean(map.get("login"));
        String username = map.get("username");
        String password = map.get("password");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomesPage<WebDriver> homesPage = HomesPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Get three points banner content from the API");
        String language = homePage.getLanguageApp();
        String section = "THREE_POINTS_BANNER";
        HomesData data = homesPage.getContentLandingPageUsingPojo(
                login, username, password, language, section
        );
        Assertion.assertThat("Success to get three points banner content from the API", !data.title.isEmpty(), is(true));

        String threePointBannerRedirectTargetApi = data.contentData.redirectTarget;
        String threePointBannerMainTitleApi = data.title;
        String threePointBannerSubTitleApi = data.subTitle;
        Boolean threePointBannerShowButtonApi = data.showButton;
        String threePointBannerButtonTextApi = data.buttonText;
        ArrayList<HomesData.ListOfTitleAndSubTitle> threePointBannerContentListApi = data.listOfTitleAndSubTitles;

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", language, "");
        String vertical = "Homes";

        step("Swipe to Three Points Banner Section");
        homesLandingPage.swipeToThreePointsBanner();

        step("Get three points banner data from the APP");
        String threePointBannerMainTitleApp = homesLandingPage.getThreePointsBannerAttribute("content.title");
        String threePointBannerSubTitleApp = homesLandingPage.getThreePointsBannerAttribute("content.subTitle");
        Boolean threePointBannerShowButtonApp = Boolean.parseBoolean(homesLandingPage.getThreePointsBannerAttribute("content.showButton"));
        String threePointBannerButtonTextApp = homesLandingPage.getThreePointsBannerAttribute("content.buttonText");
        ArrayList<HomesData.ListOfTitleAndSubTitle> threePointBannerContentListApp = homesLandingPage.getThreePointsBannerContentList();

        step("Click the button on three points main banner");
        homesLandingPage.clickThreePointBannersButton();

        step("Comparing API response with NHA Landing Page");
        Assertion.assertThat("Three Points Banner Main Title on the App should be equal with API", threePointBannerMainTitleApi, is(threePointBannerMainTitleApp));
        Assertion.assertThat("Three Points Banner Main Sub Title on the App should be equal with API", threePointBannerSubTitleApi, is(threePointBannerSubTitleApp));
        Assertion.assertThat("Three Points Banner Button on the App should be displayed", threePointBannerShowButtonApi, is(threePointBannerShowButtonApp));
        Assertion.assertThat("Three Points Banner Button Text on the App should be equal with API", threePointBannerButtonTextApi, is(threePointBannerButtonTextApp));
        Assertion.assertThat("Three Points Banner Content List on the App should be equal with API", homesLandingPage.compareThreeBannersContentList(threePointBannerContentListApi,threePointBannerContentListApp), is(true));

        step("Comparing API response with redirectTarget option '"+threePointBannerRedirectTargetApi+"'");
        Boolean isRedirectValid = hotelLandingPage.verifyRedirectTargetWithPojo(data.contentData, language, vertical);
        Assertion.assertThat("Three Points Banner Redirect on the App should be equal with API", isRedirectValid, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "SRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void homesGetMasterTagApiGuest() {
        Map<String, String> param = getParam("homesGetMasterTagApiGuest");
        BaseTest.paramsThreadLocal.set(param);
        boolean login = Boolean.parseBoolean(param.get("login"));
        String username = param.get("username");
        String password = param.get("password");
        String query = param.get("query");
        int totalMasterTag = Integer.parseInt(param.get("totalMasterTag"));
        int totalCard = Integer.parseInt(param.get("totalCard"));

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SearchResultPage<WebDriver> hotelSearchResultPage = SearchResultPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        String language = homePage.getLanguageApp();

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", language, "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(query);

        step("Set dates");
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();

        step("Getting Master Tags via API");
        String accommodationType = "NHA";
        JSONObject masterTagList = hotelApi.getMasterTagList(login, username, password, query, accommodationType, language, totalMasterTag);
        String accessToken = String.valueOf(masterTagList.get("accessToken"));
        String searchType = String.valueOf(masterTagList.get("locationType"));
        String publicId = String.valueOf(masterTagList.get("publicId"));

        step("submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();
        HashMap<String ,String> groupFilterDetails = hotelApi.getMasterTagDetails(masterTagList);

        step("Getting Hotel List via API");
        List<String> hotelNameList = hotelApi.getHotelList(accessToken, username, 1, 1, 1, searchType, publicId, currentDate, groupFilterDetails.get("name"), totalCard, accommodationType, "");

        Assertion.assertThat("Success to get hotel list", hotelNameList, is(either(empty()).or(not(empty()))));

        boolean hotelListIsEmpty = hotelNameList.isEmpty();

        step("Assertions");
        Assertion.assertThat("masterTag name matches with api response and click a random master tag", hotelSearchResultPage.checkMasterTags((AccommodationMasterTagResponse) masterTagList.get("data"), totalMasterTag, groupFilterDetails.get("name")), is(true));
        Assertion.assertThat("check hotel cards list if hotel list for master tag is not empty and if empty then check 'no hotel found' text", (hotelListIsEmpty & hotelSearchResultPage.checkEmptyHotelList()) || hotelSearchResultPage.checkHotelCardList("name", totalCard, hotelNameList.toArray(new String[0])), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "SRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void homesGetMasterTagApiLogin() {
        Map<String, String> param = getParam("homesGetMasterTagApiLogin");
        BaseTest.paramsThreadLocal.set(param);
        boolean login = Boolean.parseBoolean(param.get("login"));
        String username = param.get("username");
        String password = param.get("password");
        String loginType = param.get("loginType");
        String query = param.get("query");
        int totalMasterTag = Integer.parseInt(param.get("totalMasterTag"));
        int totalCard = Integer.parseInt(param.get("totalCard"));

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SearchResultPage<WebDriver> hotelSearchResultPage = SearchResultPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Open Homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", language, "");

        step("Search stay location");
        homesLandingPage.searchStayLocation(query);

        step("Getting Master Tags via API");
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String accommodationType = "NHA";
        JSONObject masterTagList = hotelApi.getMasterTagList(login, username, password, query, accommodationType, language, totalMasterTag);
        String accessToken = String.valueOf(masterTagList.get("accessToken"));
        String searchType = String.valueOf(masterTagList.get("locationType"));
        String publicId = String.valueOf(masterTagList.get("publicId"));

        step("submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();
        HashMap<String ,String> groupFilterDetails = hotelApi.getMasterTagDetails(masterTagList);
        String masterTagName = groupFilterDetails.get("name");

        step("Choose master tag randomly: " + masterTagName);
        hotelSearchResultPage.selectMasterTag(masterTagName);

        step("Getting Hotel List via API");
        List<String> hotelNameList = hotelApi.getHotelList(accessToken, username, 1, 1, 1, searchType, publicId, currentDate, groupFilterDetails.get("keyword"), totalCard, accommodationType, "");
        Assertion.assertThat("Success to get hotel list", hotelNameList, is(either(empty()).or(not(empty()))));

        step("Compare hotel list from API and UI");
        boolean hotelListIsEmpty = hotelNameList.isEmpty();
        Assertion.assertThat("check homes cards list if homes list for master tag is not empty and if empty then check 'no homes found' text", (hotelListIsEmpty & hotelSearchResultPage.checkEmptyHotelList()) || hotelSearchResultPage.checkHotelCardList("name", totalCard, hotelNameList.toArray(new String[0])), is(true));
    }

    @Author(names = {"Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void homesVerifyMultipleHotelIdLogin() {
        Map<String, String> param = getParam("homesVerifyMultipleHotelIdLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        boolean login = Boolean.parseBoolean(param.get("login"));
        String username = param.get("username");
        String password = param.get("password");
        String loginType = param.get("loginType");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HomesPage<WebDriver> homesPage = HomesPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);

        String language = homePage.getLanguageApp();

        step("Get multiple Hotel ID module from the API");
        String templateCode = "MULTIPLE_HOTEL_ID";

        HomesData data = homesPage.getContentLandingPageUsingPojo(
                login,username,password,language,templateCode
        );
        Assertion.assertThat("Success to get landing page module from the API", !data.title.isEmpty(), is(true));
        String expectedTitle = data.title;
        String expectedSubTitle = data.subTitle;

        step("Open homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", language, "");
        String vertical = "homes";

        step("swipe to the MULTIPLE_HOTEL_ID");
        homesLandingPage.swipeToTitleMultipleHotelId(expectedTitle);

        step("verify title, subtitle and locations from api with app");
        boolean isTitleAndSubTitleDisplayed = homesLandingPage.isTitleAndSubTitleDisplayedMultipleHotelId(expectedTitle,expectedSubTitle);
        boolean isLocationsCorrect = homesLandingPage.verifyLocationsMultipleHotelId(data.contentDataList);

        step("click random location");
        int index = CoreFunctions.generateRandomInt(0,data.contentDataList.size()-1);
        HomesData.ContentData list = data.contentDataList.get(index);
        homesLandingPage.clickLocationChipBtnMultipleHotelId(list.location);

        step("verify buttonText from api with app and click " + list.buttonText + "button");
        boolean isButtonTextMatch = homesLandingPage.verifyButtonTextMultipleHotelId(list.buttonText);

        step("Final assertion");
        Assertion.assertThat("Homes Title and SubTitle displayed is same as API", isTitleAndSubTitleDisplayed, is(true));
        Assertion.assertThat("Homes Location displayed is same as API", isLocationsCorrect, is(true));
        Assertion.assertThat("Homes Button Text displayed is same as API", isButtonTextMatch, is(true));

        step("Comparing API response with redirectTarget option '"+ list.redirectTarget +"'");
        boolean isRedirectValid = hotelLandingPage.verifyRedirectTargetWithPojo(list, language, vertical);
        Assertion.assertThat(" multiple hotel ID Redirect on the App should be equal with API", isRedirectValid, is(true));
    }

    @Author(names = {"Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void homesVerifyMultipleHotelIdGuest() {
        Map<String, String> param = getParam("homesVerifyMultipleHotelIdGuest");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        boolean login = Boolean.parseBoolean(param.get("login"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HomesPage<WebDriver> homesPage = HomesPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        String language = homePage.getLanguageApp();

        step("Get multiple Hotel ID module from the API");
        String templateCode = "MULTIPLE_HOTEL_ID";

        HomesData data = homesPage.getContentLandingPageUsingPojo(
                login,"guest","",language,templateCode
        );
        Assertion.assertThat("Success to get landing page module from the API", !data.title.isEmpty(), is(true));
        String expectedTitle = data.title;
        String expectedSubTitle = data.subTitle;

        step("Open homes vertical page");
        homePage.openVerticalPageDeeplink("Homes", language, "");
        String vertical = "homes";

        step("swipe to the MULTIPLE_HOTEL_ID");
        homesLandingPage.swipeToTitleMultipleHotelId(expectedTitle);

        step("verify title, subtitle and locations from api with app");
        boolean isTitleAndSubTitleDisplayed = homesLandingPage.isTitleAndSubTitleDisplayedMultipleHotelId(expectedTitle,expectedSubTitle);
        boolean isLocationsCorrect = homesLandingPage.verifyLocationsMultipleHotelId(data.contentDataList);

        step("click random location");
        int index = CoreFunctions.generateRandomInt(0,data.contentDataList.size()-1);
        HomesData.ContentData list = data.contentDataList.get(index);
        homesLandingPage.clickLocationChipBtnMultipleHotelId(list.location);

        step("verify buttonText from api with app and click " + list.buttonText + "button");
        boolean isButtonTextMatch = homesLandingPage.verifyButtonTextMultipleHotelId(list.buttonText);

        step("Final assertion");
        Assertion.assertThat("Homes Title and SubTitle displayed is same as API", isTitleAndSubTitleDisplayed, is(true));
        Assertion.assertThat("Homes Location displayed is same as API", isLocationsCorrect, is(true));
        Assertion.assertThat("Homes Button Text displayed is same as API", isButtonTextMatch, is(true));

        step("Comparing API response with redirectTarget option '"+ list.redirectTarget +"'");
        boolean isRedirectValid = hotelLandingPage.verifyRedirectTargetWithPojo(list, language, vertical);
        Assertion.assertThat(" multiple hotel ID Redirect on the App should be equal with API", isRedirectValid, is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifyMiniCardListLogin() {
        Map<String, String> map = getParam("verifyMiniCardListLogin");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();
        String loginType = map.get("loginType");
        boolean login = Boolean.parseBoolean(map.get("login"));
        String username = map.get("username");
        String password = map.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomesPage<WebDriver> homesPage = HomesPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Get multiple Hotel ID module from the API");
        String language = homePage.getLanguageApp();
        String templateCode = "MINI_CARD_LIST";

        HomesData data = homesPage.getContentLandingPageUsingPojo(
                login,username,password,language,templateCode
        );
        Assertion.assertThat("Success to get landing page module from the API", !data.title.isEmpty(), is(true));

        step("Get mini card title from the API");
        String miniCardTitleApi = data.title;
        log("miniCardTitleApi-> " + miniCardTitleApi);

        step("Open Homes landing page");
        String vertical = "Homes";
        homePage.openVerticalPageDeeplink("Homes", language, "");

        step("Get mini card section text");
        String miniCardTitleApp = homesLandingPage.getMiniCardTitle();

        step("Compare Title on Mini Card between API and App");
        boolean compareTitleMiniCards = homesLandingPage.compareMiniCardTitleList(data);

        step("Getting Redirect data from API ");
        ArrayList<HomesNHAResponse.List> listArray = data.list;
        int randomMiniCard = generateRandomInt(0, listArray.size()-1);

        String redirectTargetApi = listArray.get(randomMiniCard).redirectTarget;
        HomesData.ContentData redirectTargetDataApi = new HomesData.ContentData();
        redirectTargetDataApi.redirectTarget = listArray.get(randomMiniCard).redirectTarget;
        redirectTargetDataApi.redirect = listArray.get(randomMiniCard).redirect;

        step("Click Random mini card");
        homesLandingPage.clickMiniCard(randomMiniCard+1, listArray.get(randomMiniCard).title);

        step("Comparing API response with redirectTarget option '"+redirectTargetApi+"'");
        Boolean isRedirectValid = hotelLandingPage.verifyRedirectTargetWithPojo(redirectTargetDataApi, language, vertical);

        step("Assertions");
        Assertion.assertThat("Mini Card Title on the App should be equal with API", miniCardTitleApp, equalTo(miniCardTitleApi));
        Assertion.assertThat("Mini Card Title on each card on App should be equal with API", compareTitleMiniCards, is(true));
        Assertion.assertThat(" Redirect on the App should be equal with API", isRedirectValid, is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "LandingPage")
    @Test
    public void verifyMiniCardListGuest() {
        Map<String, String> map = getParam("verifyMiniCardListGuest");
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomesPage<WebDriver> homesPage = HomesPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Get multiple Hotel ID module from the API");
        String language = homePage.getLanguageApp();
        String templateCode = "MINI_CARD_LIST";

        HomesData data = homesPage.getContentLandingPageUsingPojo(false, "guest", "-", language, templateCode);
        Assertion.assertThat("Success to get landing page module from the API", !data.title.isEmpty(), is(true));

        step("Get mini card title from the API");
        String miniCardTitleApi = data.title;
        log("miniCardTitleApi-> " + miniCardTitleApi);

        step("Open Homes landing page");
        String vertical = "Homes";
        homePage.openVerticalPageDeeplink(vertical, language, "");

        step("Get mini card section text");
        String miniCardTitleApp = homesLandingPage.getMiniCardTitle();

        step("Compare Title on Mini Card between API and App");
        boolean compareTitleMiniCards = homesLandingPage.compareMiniCardTitleList(data);

        step("Getting Redirect data from API ");
        ArrayList<HomesNHAResponse.List> listArray = data.list;
        int randomMiniCard = generateRandomInt(0, listArray.size()-1);

        String redirectTargetApi = listArray.get(randomMiniCard).redirectTarget;
        HomesData.ContentData redirectTargetDataApi = new HomesData.ContentData();
        redirectTargetDataApi.redirectTarget = listArray.get(randomMiniCard).redirectTarget;
        redirectTargetDataApi.redirect = listArray.get(randomMiniCard).redirect;

        step("Click Random mini card");
        homesLandingPage.clickMiniCard(randomMiniCard+1, listArray.get(randomMiniCard).title);

        step("Comparing API response with redirectTarget option '"+redirectTargetApi+"'");
        Boolean isRedirectValid = hotelLandingPage.verifyRedirectTargetWithPojo(redirectTargetDataApi, language, vertical);

        step("Assertions");
        Assertion.assertThat("Mini Card Title on the App should be equal with API", miniCardTitleApp, equalTo(miniCardTitleApi));
        Assertion.assertThat("Mini Card Title on each card on App should be equal with API", compareTitleMiniCards, is(true));
        Assertion.assertThat(" Redirect on the App should be equal with API", isRedirectValid, is(true));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "CreateBooking")
    @Test
    public void checkAddOnHomesNonIDRGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkAddOnHomesNonIDRGuest");
        BaseTest.paramsThreadLocal.set(map);
        String hotelName = map.get("hotelName");
        String currency = map.get("currency");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");
        String dateType = map.get("date_type");
        String date = map.get("date");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        String language = "ENG";
        String eTicketLanguage = "English";
        step("Change language to \"" + language + "\"");
        homePage.openAccount();
        accountPage.selectLanguage(language);
        settingPage.backToHome();

        step("Change currency to \"" + currency + "\"");
        homePage.changeCurrency(currency,"");

        step("Open Homes landing page");
        language = homePage.getLanguageApp();
        homePage.openVerticalPageDeeplink("Homes", language, currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(hotelName);

        step("set total guest: 1");
        homesLandingPage.openTotalGuest();
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.setTotalChildren(0);
        homesLandingPage.clickSubmit();

        step("Set stay date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        String roomName = roomListPage.getTheFirstHotelRoomName();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToAddOnPage();
        Double priceWithOutAddOn = CoreFunctions.convertPrice(bookingFormPage.getTotalPriceBookingForm());

        step("Click Extra Facility Add Button");
        bookingFormPage.clickExtraFacilityAddOn();

        String addOnCategory = bookingFormPage.getAddonName();
        String addOnTitle = bookingFormPage.getAddonTitle();

        Double addOnPrice = CoreFunctions.convertPrice(bookingFormPage.getAddOnPrice());
        String priceWithAddOnString = bookingFormPage.getTotalPriceBookingForm();
        Assertion.assertThat("Addon Price with correct currency: "+currency, priceWithAddOnString, is(containsString(currency)));

        Double priceWithAddOn = CoreFunctions.convertPrice(priceWithAddOnString);

        step("open Price Detail");
        Assertion.assertThat("Price after addOn is equals to hotel price ("+priceWithOutAddOn+") + addOn price ("+addOnPrice+")", setNumberAfterComma(priceWithOutAddOn + addOnPrice), is(setNumberAfterComma(priceWithAddOn)));
        Assertion.assertThat("Is price breakdown correct", bookingFormPage.isPriceDetailCorrect(priceWithAddOnString), is(true));
        Assertion.assertThat("Addon Price is correct on price detail", bookingFormPage.MatchAddOnPriceDetail(addOnPrice, addOnCategory), is(true));

        int addOnQty = bookingFormPage.getAddOnQty(addOnCategory);

        step("continue to payment");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Input and submit CC information");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Submit the payment method");
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();

        step("Assert if the payment is a success");
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open the order list page");
        paymentPage.seeOrderList();

        step("Open the order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        
        Assertion.assertThat("add on "+addOnQty+" "+addOnTitle+" should be showing on order detail", orderDetailPage.checkAddon(addOnTitle,addOnQty), is(true));

        step("Open e-Ticket");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage(eTicketLanguage);

        step("e-Ticket file is opened and contain "+addOnQty+" "+addOnTitle);
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("PDF content contains: "+addOnQty+"x "+addOnTitle, actualPdfContent, is(containsString(addOnQty+" "+addOnTitle)));
        Assertion.assertThat("PDF content contains: "+addOnTitle+"("+addOnQty+" item)", actualPdfContent, is(containsString(addOnQty+" "+addOnTitle)));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "CreateBooking")
    @Test
    public void checkAddOnHomesNonIDRLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkAddOnHomesNonIDRLogin");
        BaseTest.paramsThreadLocal.set(map);
        String hotelName = map.get("hotelName");
        String currency = map.get("currency");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");
        String dateType = map.get("date_type");
        String date = map.get("date");

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        String language = "ENG";
        String eTicketLanguage = "English";
        step("Change language to \"" + language + "\"");
        homePage.openAccount();
        accountPage.selectLanguage(language);
        settingPage.backToHome();

        step("Change currency to \"" + currency + "\"");
        homePage.changeCurrency(currency,username);

        step("Open Homes landing page");
        language = homePage.getLanguageApp();
        homePage.openVerticalPageDeeplink("Homes", language, currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(hotelName);

        step("set total guest: 1");
        homesLandingPage.openTotalGuest();
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.setTotalChildren(0);
        homesLandingPage.clickSubmit();

        step("Set stay date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        String roomName = roomListPage.getTheFirstHotelRoomName();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToAddOnPage();
        Double priceWithOutAddOn = CoreFunctions.convertPrice(bookingFormPage.getTotalPriceBookingForm());

        step("Click Extra Facility Add Button");
        bookingFormPage.clickExtraFacilityAddOn();

        String addOnCategory = bookingFormPage.getAddonName();
        String addOnTitle = bookingFormPage.getAddonTitle();

        Double addOnPrice = CoreFunctions.convertPrice(bookingFormPage.getAddOnPrice());
        String priceWithAddOnString = bookingFormPage.getTotalPriceBookingForm();
        Assertion.assertThat("Addon Price with correct currency: "+currency, priceWithAddOnString, is(containsString(currency)));

        Double priceWithAddOn = CoreFunctions.convertPrice(priceWithAddOnString);

        step("open Price Detail");
        Assertion.assertThat("Price after addOn is equals to hotel price ("+priceWithOutAddOn+") + addOn price ("+addOnPrice+")", setNumberAfterComma(priceWithOutAddOn + addOnPrice), is(setNumberAfterComma(priceWithAddOn)));
        Assertion.assertThat("Is price breakdown correct", bookingFormPage.isPriceDetailCorrect(priceWithAddOnString), is(true));
        Assertion.assertThat("Addon Price is correct on price detail", bookingFormPage.MatchAddOnPriceDetail(addOnPrice, addOnCategory), is(true));

        int addOnQty = bookingFormPage.getAddOnQty(addOnCategory);

        step("continue to payment");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Input and submit CC information");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Submit the payment method");
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();

        step("Assert if the payment is a success");
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open the order list page");
        paymentPage.seeOrderList();

        step("Open the order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("add on "+addOnQty+" "+addOnTitle+" should be showing on order detail", orderDetailPage.checkAddon(addOnTitle,addOnQty), is(true));

        step("Open e-Ticket");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage(eTicketLanguage);

        step("e-Ticket file is opened and contain "+addOnQty+" "+addOnTitle);
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("PDF content contains: "+addOnQty+"x "+addOnTitle, actualPdfContent, is(containsString(addOnQty+" "+addOnTitle)));
        Assertion.assertThat("PDF content contains: "+addOnTitle+"("+addOnQty+" item)", actualPdfContent, is(containsString(addOnQty+" "+addOnTitle)));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "CreateBooking")
    @Test
    public void completeBookingNonIdrHomesGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingNonIdrHomesGuest");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");
        String currency = map.get("currency");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");
        String dateType = map.get("date_type");
        String date = map.get("date");

        String totalHotel = map.get("totalHotel");
        String priceRange = map.get("priceRange");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Logout if on the login session");
        loginPage.loginAsGuest();
        homePage.changeCurrency(currency,"");

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("set total guest: 1");
        homesLandingPage.openTotalGuest();
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.setTotalChildren(0);
        homesLandingPage.clickSubmit();

        step("Set stay date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Verify price in SRP");
        Assertion.assertThat("SRP Currency is "+currency, searchResultPage.getPrices(), everyItem(containsString(currency)));

        step("open filter");
        searchResultPage.openPriceFilter();

        step("Verify filter prices");
        Assertion.assertThat("Price filter Currency is "+currency, searchResultPage.getPricesInFilter(), everyItem(containsString(currency)));

        step("Submit and verify filter is activated");
        String selectedPriceRange = searchResultPage.selectPriceFilterByRange(priceRange,currency);
        searchResultPage.clickFilterSubmit();
        Assertion.assertThat("Hotel price is match with price filter by range", searchResultPage.isPriceFilterByRangeCorrect(selectedPriceRange, totalHotel), is(true));

        step("User click sort filter button");
        searchResultPage.selectFilterSortBy("lowest price");

        step("Get hotel price data");
        Boolean isSortByLowestPriceValid = searchResultPage.isSortByLowestPriceValid(totalHotel);

        step("Sort by lowest price assertion");
        Assertion.assertThat("short by lowest price is valid", isSortByLowestPriceValid ,is(true));

        step("Click see map button in SRP");
        searchResultPage.clickSeeMapButton();

        step("Get hotel price in maps");
        String[] hotelPriceMaps = searchResultPage.getHotelPriceMaps();
        Assertion.assertThat("Price in maps Currency is "+currency, hotelPriceMaps[1],containsString(currency));

        step("Get hotel price in card");
        String hotelPriceCard = searchResultPage.getHotelPriceCard(hotelPriceMaps[1], true);
        Assertion.assertThat("Price in maps card Currency is "+currency, hotelPriceCard, containsString(currency));
        searchResultPage.closeMap();

        step("Select hotel list");
        searchResultPage.resetFilter();
        searchResultPage.selectFirstHotelCard();

        step("Verify price in PDP");
        Assertion.assertThat("Price in PDP Currency is "+currency, detailPage.getHotelPrice(), containsString(currency));

        step("Go to the room list");
        detailPage.seeHotelRoom();
        Assertion.assertThat("Price in Room List Currency is "+currency, roomListPage.getPrices(), everyItem(containsString(currency)));

        step("move back to PDP and then to room list for selecting first room");
        roomListPage.clickBackButton();
        detailPage.seeHotelRoom();

        step("Open room detail");
        String roomName = roomListPage.getTheFirstHotelRoomName();
        roomListPage.openRoomDetailPage();

        step("Verify price in room detail");
        Assertion.assertThat("Price in Room Detail Currency is "+currency, roomDetailPage.getHotelRoomPriceOnRoomDetail(), containsString(currency));

        List<String> charges = detailPage.getCancellationCharges();
        if(!charges.get(0).equals("No Charges present")){
            Assertion.assertThat("Cancellation Currency is "+currency, charges, everyItem(containsString(currency)));
        }

        detailPage.openPriceDetail();
        Assertion.assertThat("Price in Room Detail Price Detail Currency is "+currency, detailPage.getPriceDetailPrices(), everyItem(containsString(currency)));
        detailPage.closePriceDetail();

        step("Go to booking form");
        detailPage.clickBookButton(1);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        step("Verify cancellation charge "+charges.get(0));
        if(!charges.get(0).equals("No Charges present")){
            bookingFormPage.openBookingDetailPage();
            Assertion.assertThat("Cancellation Currency is "+currency, detailPage.getCancellationCharges(), everyItem(containsString(currency)));
            bookingFormPage.clickBackButton();
        }

        step("Fill contact detail");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);
        String hotelName = bookingFormPage.getHotelNameBookingForm();

        step("Verify price in booking form");
        String priceTotalBookingForm = bookingFormPage.getTotalPriceBookingForm();
        Assertion.assertThat("Total price booking form with correct currency: "+currency, priceTotalBookingForm, is(containsString(currency)));

        step("open Price Detail");
        bookingFormPage.continueToAddOnPage();
        Assertion.assertThat("Is price detail correct currency "+currency, bookingFormPage.getPriceDetailPrices(priceTotalBookingForm), everyItem(containsString(currency)));
        Assertion.assertThat("Is price breakdown correct", bookingFormPage.isPriceDetailCorrect(priceTotalBookingForm), is(true));


        step("continue to payment");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Input and submit CC information");
        if (paymentPage.checkSeeAllPaymentMethod()) {
            paymentPage.openPaymentMethod();
            paymentPage.selectPaymentMethod("Credit Card");
        }
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Submit the payment method");
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();

        step("Assert if the payment is a success");
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open the order list page");
        paymentPage.seeOrderList();

        step("Open the order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Price on order detail should be "+currency);
        String totalPriceOrderDetail = orderDetailPage.getTotalPaymentHotel();
        Assertion.assertThat("Currency is "+currency, totalPriceOrderDetail, containsString(currency));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "CreateBooking")
    @Test
    public void completeBookingNonIdrHomesLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingNonIdrHomesLogin");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");
        String currency = map.get("currency");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");
        String dateType = map.get("date_type");
        String date = map.get("date");

        String totalHotel = map.get("totalHotel");
        String priceRange = map.get("priceRange");

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Logout if on the login session");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType,username,password);
        homePage.changeCurrency(currency,username);

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("set total guest: 1");
        homesLandingPage.openTotalGuest();
        hotelLandingPage.setTotalGuest(1);
        hotelLandingPage.setTotalChildren(0);
        homesLandingPage.clickSubmit();

        step("Set stay date");
        hotelLandingPage.setStayDate(dateType, date);

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Verify price in SRP");
        Assertion.assertThat("SRP Currency is "+currency, searchResultPage.getPrices(), everyItem(containsString(currency)));

        step("open filter");
        searchResultPage.openPriceFilter();

        step("Verify filter prices");
        Assertion.assertThat("Price filter Currency is "+currency, searchResultPage.getPricesInFilter(), everyItem(containsString(currency)));

        step("Submit and verify filter is activated");
        String selectedPriceRange = searchResultPage.selectPriceFilterByRange(priceRange,currency);
        searchResultPage.clickFilterSubmit();
        Assertion.assertThat("Hotel price is match with price filter by range", searchResultPage.isPriceFilterByRangeCorrect(selectedPriceRange, totalHotel), is(true));

        step("User click sort filter button");
        searchResultPage.selectFilterSortBy("lowest price");

        step("Get hotel price data");
        Boolean isSortByLowestPriceValid = searchResultPage.isSortByLowestPriceValid(totalHotel);

        step("Sort by lowest price assertion");
        Assertion.assertThat("short by lowest price is valid", isSortByLowestPriceValid ,is(true));

        step("Click see map button in SRP");
        searchResultPage.clickSeeMapButton();

        step("Get hotel price in maps");
        String[] hotelPriceMaps = searchResultPage.getHotelPriceMaps();
        Assertion.assertThat("Price in maps Currency is "+currency, hotelPriceMaps[1],containsString(currency));

        step("Get hotel price in card");
        String hotelPriceCard = searchResultPage.getHotelPriceCard(hotelPriceMaps[1], true);
        Assertion.assertThat("Price in maps card Currency is "+currency, hotelPriceCard, containsString(currency));
        searchResultPage.closeMap();

        step("Select hotel list");
        searchResultPage.resetFilter();
        searchResultPage.selectFirstHotelCard();

        step("Verify price in PDP");
        Assertion.assertThat("Price in PDP Currency is "+currency, detailPage.getHotelPrice(), containsString(currency));

        step("Go to the room list");
        detailPage.seeHotelRoom();
        Assertion.assertThat("Price in Room List Currency is "+currency, roomListPage.getPrices(), everyItem(containsString(currency)));

        step("Open room detail");
        String roomName = roomListPage.getTheFirstHotelRoomName();
        roomListPage.openRoomDetailPage();

        step("Verify price in room detail");
        Assertion.assertThat("Price in Room Detail Currency is "+currency, roomDetailPage.getHotelRoomPriceOnRoomDetail(), containsString(currency));

        List<String> charges = detailPage.getCancellationCharges();
        if(!charges.get(0).equals("No Charges present")){
            Assertion.assertThat("Cancellation Currency is "+currency, charges, everyItem(containsString(currency)));
        }

        detailPage.openPriceDetail();
        Assertion.assertThat("Price in Room Detail Price Detail Currency is "+currency, detailPage.getPriceDetailPrices(), everyItem(containsString(currency)));
        detailPage.closePriceDetail();

        step("Go to booking form");
        detailPage.clickBookButton(1);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        step("Verify cancellation charge "+charges.get(0));
        if(!charges.get(0).equals("No Charges present")){
            bookingFormPage.openBookingDetailPage();
            Assertion.assertThat("Cancellation Currency is "+currency, detailPage.getCancellationCharges(), everyItem(containsString(currency)));
            bookingFormPage.clickBackButton();
        }

        step("Fill contact detail");
        bookingFormPage.fillGuestDetail(false);
        String hotelName = bookingFormPage.getHotelNameBookingForm();

        step("Verify price in booking form");
        String priceTotalBookingForm = bookingFormPage.getTotalPriceBookingForm();
        Assertion.assertThat("Total price booking form with correct currency: "+currency, priceTotalBookingForm, is(containsString(currency)));

        step("open Price Detail");
        bookingFormPage.continueToAddOnPage();
        Assertion.assertThat("Is price detail correct currency "+currency, bookingFormPage.getPriceDetailPrices(priceTotalBookingForm), everyItem(containsString(currency)));
        Assertion.assertThat("Is price breakdown correct", bookingFormPage.isPriceDetailCorrect(priceTotalBookingForm), is(true));


        step("continue to payment");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Input and submit CC information");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Submit the payment method");
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();

        step("Assert if the payment is a success");
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open the order list page");
        paymentPage.seeOrderList();

        step("Open the order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Price on order detail should be "+currency);
        String totalPriceOrderDetail = orderDetailPage.getTotalPaymentHotel();
        Assertion.assertThat("Currency is "+currency, totalPriceOrderDetail, containsString(currency));

        if(Configs.PLATFORM == Platform.ANDROID){
            step("Check receipt");
            orderDetailPage.openReceipt();
            String actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Receipt contains "+currency, actualPdfContent, containsString(currency));
            Assertion.assertThat("Receipt not contains IDR", actualPdfContent, not(containsString("IDR")));
            Assertion.assertThat("Total price on the order detail should be contains on the receipt", actualPdfContent, containsString(totalPriceOrderDetail));
            orderDetailPage.clickBackFromPdf();
        }
    }

    @Author(names = {"Yazid"})
    @Module(name = "CreateBooking")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test(priority = 1)
    public void multiOrderDifferentRoomWithItemAddOnsLoginHomes() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("multiOrderDifferentRoomWithItemAddOnsLoginHomes");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int totalGuest = Integer.parseInt(param.get("totalGuest"));
        int totalRoom = Integer.parseInt(param.get("totalRoom"));
        int guestPerRoom = Integer.parseInt(param.get("guestPerRoom"));
        int totalNight = Integer.parseInt(param.get("totalNight"));
        String rangeAddDate = param.get("rangeAddDate");
        String paymentMethod = param.get("paymentMethod");

        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "homes";
        String currency = "IDR";
        int itemPerRoom = 1;

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency(currency, username);
        String appLanguage = homePage.getLanguageApp();

        step("user search for multi night and multi room");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, totalGuest, checkInDate, checkOutDate);

        step("user check Room List");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();
        Assertion.assertThat("user can see MO banner on the top of room list", roomListPage.moBannerIsDisplayed(), is(true));

        step("user click select button on multiple room type");
        double totalRoomPrice = roomListPage.selectMultiRoom(totalRoom, guestPerRoom);

        step("user check the price footer");
        double priceFooter = convertPrice(roomListPage.getPriceFooter());
        Assertion.assertThat("verify the price footer is showing the total price of selected room: " + totalRoomPrice + " x " + totalNight, priceFooter, greaterThanOrEqualTo(totalRoomPrice * totalNight));

        step("user click price footer");
        roomListPage.clickPriceFooter();
        Assertion.assertThat("user can see the price breakdown", roomListPage.priceBreakdownIsDisplayed(), is(true));
        Assertion.assertThat("verify the total price is same or less than with price footer", roomListPage.sumPriceBreakdown(), lessThanOrEqualTo(priceFooter));

        step("user click book button from price footer");
        roomListPage.clickBookButtonFooter();
        Assertion.assertThat("user redirected to Booking Form", bookingFormPage.isBookingFormShown(true), is(true));

        step("user add the guest name");
        bookingFormPage.fillGuestDetail(true);
        Assertion.assertThat("user should add guest name for each room type", bookingFormPage.getGuestNameList().size(), is(is(totalGuest)));

        step("user click add on /item add ons");
        bookingFormPage.continueToAddOnPage();
        String priceWithoutAddOns = bookingFormPage.getTotalPriceBookingForm();
        HashMap<Integer, String> selectedRoomName = RoomListPageAndroid.selectedRoomName;
        HashMap<Object, Object> addOnData = bookingFormPage.clickExtraFacilityAddOn(itemPerRoom, ITEM, selectedRoomName);

        step("user check price footer booking form");
        String priceWithAddOns = bookingFormPage.getTotalPriceBookingForm();
        Assertion.assertThat("price on price footer booking form recalculated with add ons price: " + priceWithoutAddOns + " + " + addOnData.get(TOTAL_PRICE), convertPrice(priceWithAddOns), is(convertPrice(priceWithoutAddOns) + convertPrice(String.valueOf(addOnData.get(TOTAL_PRICE)))));

        step("user check Booking Form's price detail");
        String addOnsName = String.valueOf(addOnData.get(NAME));
        // have improvement soon https://borobudur.atlassian.net/browse/AC-21523
        if (Configs.PLATFORM == Platform.IOS) addOnsName = String.valueOf(addOnData.get(CATEGORY));
        Assertion.assertThat("price detail showing add ons cost", bookingFormPage.checkAddOnPriceDetail((int) addOnData.get(TOTAL_ROOM_TYPE), addOnsName, String.valueOf(addOnData.get(SINGLE_PRICE)), itemPerRoom), is(true));
        Assertion.assertThat("Total price on the price detail should be equal with the price footer on the booking form", bookingFormPage.getTotalPriceInPriceDetail(), is(priceWithAddOns));

        step("order with add ons successfully paid");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();
        paymentPage.isThankYouPageShown();
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        
        Assertion.assertThat("user can see detail add ons inside order detail", orderDetailPage.checkAddon(String.valueOf(addOnData.get(NAME)), itemPerRoom, selectedRoomName), is(true));

        orderDetailPage.clickEticketFromHeader();
        String ticketLanguage = (appLanguage.equals("en")) ? "English" : "Bahasa Indonesia";
        orderDetailPage.clickETicketLanguage(ticketLanguage);
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("user can see detail add ons inside voucher", orderDetailPage.checkETicket(actualPdfContent, String.valueOf(addOnData.get(NAME)), itemPerRoom, selectedRoomName), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "CreateBooking")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test(priority = 2)
    public void multiOrderDifferentRoomWithRoomNightAddOnsLoginHomes() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("multiOrderDifferentRoomWithRoomNightAddOnsLoginHomes");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int totalGuest = Integer.parseInt(param.get("totalGuest"));
        int totalRoom = Integer.parseInt(param.get("totalRoom"));
        int guestPerRoom = Integer.parseInt(param.get("guestPerRoom"));
        int totalNight = Integer.parseInt(param.get("totalNight"));
        String rangeAddDate = param.get("rangeAddDate");
        String paymentMethod = param.get("paymentMethod");

        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "homes";
        String currency = "IDR";
        int itemPerRoom = 1;

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency(currency, username);
        String appLanguage = homePage.getLanguageApp();

        step("user search for multi night and multi room");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, totalGuest, checkInDate, checkOutDate);

        step("user check Room List");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();
        Assertion.assertThat("user can see MO banner on the top of room list", roomListPage.moBannerIsDisplayed(), is(true));

        step("user click select button on multiple room type");
        double totalRoomPrice = roomListPage.selectMultiRoom(totalRoom, guestPerRoom);

        step("user check the price footer");
        double priceFooter = convertPrice(roomListPage.getPriceFooter());
        Assertion.assertThat("verify the price footer is showing the total price of selected room: " + totalRoomPrice + " x " + totalNight, priceFooter, greaterThanOrEqualTo(totalRoomPrice * totalNight));

        step("user click price footer");
        roomListPage.clickPriceFooter();
        Assertion.assertThat("user can see the price breakdown", roomListPage.priceBreakdownIsDisplayed(), is(true));
        Assertion.assertThat("verify the total price is same or less than with price footer", roomListPage.sumPriceBreakdown(), lessThanOrEqualTo(priceFooter));

        step("user click book button from price footer");
        roomListPage.clickBookButtonFooter();
        Assertion.assertThat("user redirected to Booking Form", bookingFormPage.isBookingFormShown(true), is(true));

        step("user add the guest name");
        bookingFormPage.fillGuestDetail(true);
        Assertion.assertThat("user should add guest name for each room type", bookingFormPage.getGuestNameList().size(), is(is(totalGuest)));

        step("user click add on /room/night add ons");
        bookingFormPage.continueToAddOnPage();
        String priceWithoutAddOns = bookingFormPage.getTotalPriceBookingForm();
        HashMap<Integer, String> selectedRoomName = RoomListPageAndroid.selectedRoomName;
        HashMap<Object, Object> addOnData = bookingFormPage.clickExtraFacilityAddOn(itemPerRoom, ROOM, selectedRoomName);

        step("user check price footer booking form");
        String priceWithAddOns = bookingFormPage.getTotalPriceBookingForm();
        Assertion.assertThat("price on price footer booking form recalculated with add ons price: " + priceWithoutAddOns + " + " + addOnData.get(TOTAL_PRICE), convertPrice(priceWithAddOns), is(convertPrice(priceWithoutAddOns) + convertPrice(String.valueOf(addOnData.get(TOTAL_PRICE)))));

        step("user check Booking Form's price detail");
        String addOnsName = String.valueOf(addOnData.get(NAME));
        // have improvement soon https://borobudur.atlassian.net/browse/AC-21523
        if (Configs.PLATFORM == Platform.IOS) addOnsName = String.valueOf(addOnData.get(CATEGORY));
        Assertion.assertThat("price detail showing add ons cost", bookingFormPage.checkAddOnPriceDetail((int) addOnData.get(TOTAL_ROOM_TYPE), addOnsName, String.valueOf(addOnData.get(SINGLE_PRICE)), itemPerRoom), is(true));
        Assertion.assertThat("Total price on the price detail should be equal with the price footer on the booking form", bookingFormPage.getTotalPriceInPriceDetail(), is(priceWithAddOns));

        step("order with add ons successfully paid");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();
        paymentPage.isThankYouPageShown();
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        
        Assertion.assertThat("user can see detail add ons inside order detail", orderDetailPage.checkAddon(String.valueOf(addOnData.get(NAME)), itemPerRoom, selectedRoomName), is(true));

        orderDetailPage.clickEticketFromHeader();
        String ticketLanguage = (appLanguage.equals("en")) ? "English" : "Bahasa Indonesia";
        orderDetailPage.clickETicketLanguage(ticketLanguage);
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("user can see detail add ons inside voucher", orderDetailPage.checkETicket(actualPdfContent, String.valueOf(addOnData.get(NAME)), itemPerRoom, selectedRoomName), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "CreateBooking")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test(priority = 3)
    public void multiOrderDifferentRoomIdrHomes() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("multiOrderDifferentRoomIdrHomes");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int totalGuest = Integer.parseInt(param.get("totalGuest"));
        int totalRoom = Integer.parseInt(param.get("totalRoom"));
        int guestPerRoom = Integer.parseInt(param.get("guestPerRoom"));
        int totalNight = Integer.parseInt(param.get("totalNight"));
        String rangeAddDate = param.get("rangeAddDate");
        String paymentMethod = param.get("paymentMethod");

        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "homes";
        String currency = "IDR";

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);

        step("user selected currency is " + currency);
        homePage.changeCurrency(currency, username);

        step("user open apps with PDP Deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        int index = hotelApi.findHotelIndex(hotelAutoCompleteResponse , hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(index).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(index).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, totalGuest, checkInDate, checkOutDate);

        step("User select multiple room type");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();
        roomListPage.selectMultiRoom(totalRoom, guestPerRoom);
        roomListPage.clickBookButtonFooter();

        step("User fill the guest name");
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        bookingFormPage.fillGuestDetail(true);

        step("order with " + currency + " MO successfully paid");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();
        paymentPage.isThankYouPageShown();

        step("Open the order list page and wait for the order to be in issued state");
        orderListPage.waitForOrderToBeInIssuedState(orderID,20);
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        

        step("Get price on the order detail");
        String totalPriceOrderDetail = orderDetailPage.getTotalPaymentHotel();
        Assertion.assertThat("order detail showing price with selected currency", totalPriceOrderDetail, containsString(currency));

        if (Configs.PLATFORM == Platform.ANDROID) {
            step("Get price on the receipt");
            orderDetailPage.openReceipt();
            String actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("receipt showing price with selected currency", actualPdfContent, containsString(currency));
        } else if (Configs.PLATFORM == Platform.IOS) {
            step("Check receipt share button");
            Assertion.assertThat("Receipt button is working fine", orderDetailPage.isReceiptButtonWork(currency), is(true));
        }
    }

    @Author(names = {"Yazid"})
    @Module(name = "CreateBooking")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test(priority = 4)
    public void multiOrderDifferentRoomNonIdrHomes() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("multiOrderDifferentRoomNonIdrHomes");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int totalGuest = Integer.parseInt(param.get("totalGuest"));
        int totalRoom = Integer.parseInt(param.get("totalRoom"));
        int guestPerRoom = Integer.parseInt(param.get("guestPerRoom"));
        int totalNight = Integer.parseInt(param.get("totalNight"));
        String rangeAddDate = param.get("rangeAddDate");
        String currency = param.get("currency");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "homes";

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);

        step("user selected currency is non IDR: " + currency);
        homePage.changeCurrency(currency, username);

        step("user open apps with PDP Deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, totalGuest, checkInDate, checkOutDate);

        step("User select multiple room type");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();
        roomListPage.selectMultiRoom(totalRoom, guestPerRoom);
        roomListPage.clickBookButtonFooter();

        step("User fill the guest name");
        bookingFormPage.fillGuestDetail(true);

        step("order with non IDR: " + currency + " MO successfully paid");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();
        String orderID = paymentPage.checkOrderID();
        if (paymentPage.checkSeeAllPaymentMethod()) {
            paymentPage.openPaymentMethod();
            paymentPage.selectPaymentMethod("Credit Card");
        }
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        paymentPage.clickPayButtonCC();
        paymentPage.verifyCreditCard3ds();
        paymentPage.isThankYouPageShown();
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        

        step("Get price on the order detail");
        String totalPriceOrderDetail = orderDetailPage.getTotalPaymentHotel();
        Assertion.assertThat("order detail showing price with selected currency", totalPriceOrderDetail, containsString(currency));

        if (Configs.PLATFORM == Platform.ANDROID) {
            step("Get price on the receipt");
            orderDetailPage.openReceipt();
            String actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("receipt showing price with selected currency", actualPdfContent, containsString(currency));
        } else if (Configs.PLATFORM == Platform.IOS) {
            step("Check receipt share button");
            Assertion.assertThat("Receipt button is working fine", orderDetailPage.isReceiptButtonWork(currency), is(true));
        }
    }

    @Author(names = {"Zia"})
    @Module(name = "PostPurchase")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    @SneakyThrows
    public void refundNonIdrHomes() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("refundNonIdrHomes");
        BaseTest.paramsThreadLocal.set(param);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        String currency = param.get("currency");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType,username,password);
        homePage.changeCurrency(currency,username);

        step("Get api access token");
        Unm<?> unm = new Unm<>(session);
        LoginDataUnmResponse.Data data = unm.unmLoginApi(username, password);
        String apiAccessToken = data.accessToken;
        String accountId = String.valueOf(data.accountId);

        step("check order present in order");
        MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrderByVertical("HOMES", apiAccessToken);
        String bookedOrderID = "";

        step("filter refundable");
        int limit = 15;
        activeOrderData = refundPage.getRefundAbleOrder(activeOrderData, username,accountId,limit);

        step("filter currency");
        activeOrderData = orderListPage.filterOrderByPaymentCurrency(activeOrderData,currency,limit);

        if(activeOrderData.isEmpty && activeOrderData.homes.size()==0){
            step("Create a hotel order with waiting payment status");
            bookedOrderID = hotelApi.createWaitingPaymentOrderApi(
                    username, password, startDate, hotelName, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle,
                    currency,"id");
            Assertion.assertThat("Success to create waiting payment order", bookedOrderID, is(not(emptyOrNullString())));

            step("Open the order page");
            homePage.openMyOrderPage();

            step("Complete order with the payment method");
            orderListPage.openOrderCompletePayment(bookedOrderID);
            paymentPage.checkOrderID();
            paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
            paymentPage.clickPayButtonCC();
            paymentPage.verifyCreditCard3ds();
            Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        }else{
            bookedOrderID = activeOrderData.homes.get(0).orderId;
        }

        step("Open order detail");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Verify currency on refund estimation based on dates");
        Assertion.assertThat("Estimated date prices currency is " +currency,refundPage.getEstimationDatePrices(),everyItem(containsString(currency)));

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Verify total refund currency");
        Assertion.assertThat("Total refund currency is " +currency,refundPage.getTotalRefund(),containsString(currency));

        step("Open refund price details");
        refundPage.openEstimatedRefundDetail();
        Assertion.assertThat("Prices in refund total detail break down is " +currency,refundPage.getRefundDetailPrices(),everyItem(containsString(currency)));
        refundPage.closeTheRefundPaymentDetails();

        step("Submit refund");
        refundPage.submitRefund();

        step("check refund submit successfully");
        boolean isRefundSubmitSuccessfully = refundPage.checkRefundSubmission();
        Assertion.assertThat("Successfully submit the refund", isRefundSubmitSuccessfully, is(true));

        step("Go to the refund detail");
        refundPage.viewRefundDetailPage();

        step("Verify total refund currency");
        Assertion.assertThat("Total refund currency is " +currency,refundPage.getTotalRefund(),containsString(currency));

        step("Open refund price details");
        refundPage.openEstimatedRefundDetail();
        Assertion.assertThat("Prices in refund total detail break down is " +currency,refundPage.getRefundDetailPrices(),everyItem(containsString(currency)));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "PostPurchase")
    @Test
    @SneakyThrows
    public void homesRescheduleNonIdrWhenCurrencyIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("homesRescheduleNonIdrWhenCurrencyIDR");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        int daysToAdjust = Integer.parseInt(map.get("daysToAdjust"));
        String hotelName = map.get("hotelName");
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));
        String currency = map.get("currency");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Get api access token");
        Unm<?> unm = new Unm<>(session);
        LoginDataUnmResponse.Data data = unm.unmLoginApi(username, password);
        String apiAccessToken = data.accessToken;
        String accountId = String.valueOf(data.accountId);

        step("check order present in order");
        MapVerticalWithOrderId.TitleAndOrderId activeOrderData = orderListPage.findFirstOrderFilterByVertical(apiAccessToken, username,accountId, "HOMES", currency, true, true);
        String bookedOrderID = "";

        if (activeOrderData == null) {
            step("Create a hotel order with waiting payment status");
            bookedOrderID = hotelApi.createWaitingPaymentOrderApi(
                    username, password, startDate, hotelName, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle,
                    currency,"id");
            Assertion.assertThat("Success to create waiting payment order", bookedOrderID, is(not(emptyOrNullString())));

            step("Open the order page");
            homePage.openMyOrderPage();

            step("Complete order with the payment method");
            orderListPage.openOrderCompletePayment(bookedOrderID);
            paymentPage.checkOrderID();
            paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
            paymentPage.clickPayButton();
            paymentPage.verifyCreditCard3ds();
            Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));
            homePage.gotoHome();

        } else {
            bookedOrderID = activeOrderData.orderId;
        }

        step("Change currency to IDR");
        homePage.changeCurrency("IDR",username);

        step("Open order detail");
        orderListPage.openIssuedTicketViaDeeplink(bookedOrderID);

        step("Click reschedule");
        orderDetailPage.clickReschedule(bookedOrderID);

        step("Choose new date for the reschedule");
        webViewPage.switchToWebViewUrl("reschedule");
        String newStartDate = hotelReschedulePageWeb.changeNightDuration(daysToAdjust);

        step("Choose a new room type with additional Price");
        webViewPage.switchToWebViewUrl("reschedule");
        hotelReschedulePageWeb.getRoomName();
        String newRoomName = hotelReschedulePageWeb.chooseRoomWithAdditionalPrice();

        step("Submit booking form");
        webViewPage.switchToWebViewUrl("reschedule/checkout");
        hotelReschedulePageWeb.submitRescheduleBookingForm();

        step("Verify booking form prices is " + currency);
        webViewPage.switchToNative();
        String totalPrice = bookingFormPage.getTotalPriceBookingForm();
        boolean isCharged = !totalPrice.equalsIgnoreCase("free") && !totalPrice.equalsIgnoreCase("gratis");

        if(isCharged){
            Assertion.assertThat("Total price in reschedule booking form is " + currency, bookingFormPage.getTotalPriceBookingForm(), containsString(currency));
        }

        step("Payment page should be displayed");
        String newOrderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !newOrderID.isEmpty(), is(true));

        if(isCharged){
            step("Input and submit CC information");
            paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName, newOrderID);

            step("Submit the payment method");
            paymentPage.clickPayButton();
            paymentPage.verifyCreditCard3ds();
            Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(newOrderID), is(true));
        }else{
            step("complete reschedule");
            paymentPage.selectRefund();
            refundPage.submitRefundCashBackFreePayment();
        }

        step("Check the new order detail");
        orderListPage.openIssuedTicketViaDeeplink(newOrderID);

        step("Check the currency on the new order detail");
        Assertion.assertThat("Current currency should be " + currency, orderDetailPage.getTotalPaymentHotel(), containsString(currency));

        if (Configs.PLATFORM == Platform.ANDROID) {
            step("Check receipt");
            orderDetailPage.openReceipt();
            String actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Receipt contains " + currency, actualPdfContent, containsString(currency));
            Assertion.assertThat("Receipt not contains IDR", actualPdfContent, not(containsString("IDR")));
            orderDetailPage.clickBackFromPdf();
        }

        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("English");

        step("e-Ticket file is opened");
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("successfully to download the e-ticket", actualPdfContent, is(not(emptyString())));
    }

    @Author(names = {"Zia"})
    @Module(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Vertical(name = "Accommodation")
    @Test
    @SneakyThrows
    public void ACC_refundNonIdrWhenCurrencyIdrHomes() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("ACC_refundNonIdrWhenCurrencyIdrHomes");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        // Non-IDR booking currency (e.g. USD/SGD); refund must keep this after app switches to IDR
        String currency = param.get("currency");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a specific account");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Change currency to non-IDR booking currency { " + currency + " }");
        homePage.changeCurrency(currency, username);

        step("Create a homes (NHA) order with waiting payment status via Android book API");
        String bookedOrderID = hotelApi.createWaitingPaymentOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, "id", "NHA");
        Assertion.assertThat("Success to create waiting payment order", bookedOrderID, is(not(emptyOrNullString())));

        step("Open the order page");
        homePage.openMyOrderPage();

        step("Complete order with the payment method");
        orderListPage.openOrderCompletePayment(bookedOrderID);
        paymentPage.checkOrderID();
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);
        String priceOnPaymentPage = paymentPage.getTotalPricePayment();
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Change currency to IDR");
        homePage.gotoHome();
        homePage.changeCurrency("IDR", username);

        step("Open order detail");
        orderListPage.waitForPaymentSuccessful(bookedOrderID, 10);
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);
        

        step("Verify price on payment same as on order");
        String priceOnOrderPage = orderDetailPage.getTotalPaymentHotel();
        Assertion.assertThat("Price on order page is same as on payment page", priceOnPaymentPage, is(priceOnOrderPage));

        step("Open refund menu");
        orderDetailPage.clickRefundButton();

        step("Choose refund reason");
        refundPage.chooseRefundReasonWebView();

        step("Verify currency on refund estimation based on dates still "+currency);
        List<String> listEstimatedPrices = refundPage.getEstimationDatePrices();
        Assertion.assertThat("Estimated date prices currency is " +currency,listEstimatedPrices,everyItem(containsString(currency)));

        step("Verify estimated refund same as total payment");
        Assertion.assertThat("Estimated refund contains total price",listEstimatedPrices,hasItem(priceOnOrderPage));

        step("Check refund fee if available");
        if (refundPage.checkRefundFee()) {
            String totalEstimatedRefund = refundPage.getTotalRefund();
            Assertion.assertThat("Total estimated refund should be less than or equal to total payment", convertPriceSafely(totalEstimatedRefund), lessThanOrEqualTo(convertPriceSafely(priceOnOrderPage)));
            priceOnOrderPage = totalEstimatedRefund;
        }

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        step("Verify total refund currency still "+currency);
        String totalRefund = refundPage.getTotalRefund();
        Assertion.assertThat("Total refund currency is " +currency,totalRefund,containsString(currency));

        step("Refund amount same as price on order");
        Assertion.assertThat("Total refund is same as order",totalRefund,is(priceOnOrderPage));

        step("Verify refund price details still "+currency);
        refundPage.openEstimatedRefundDetail();
        Assertion.assertThat("Prices in refund total detail break down is " +currency,refundPage.getRefundDetailPrices(),everyItem(containsString(currency)));
        refundPage.closeTheRefundPaymentDetails();

        step("Submit refund");
        refundPage.submitRefund();

        step("check refund submit successfully");
        boolean isRefundSubmitSuccessfully = refundPage.checkRefundSubmission();
        Assertion.assertThat("Successfully submit the refund", isRefundSubmitSuccessfully, is(true));

        step("Go to the refund detail");
        refundPage.viewRefundDetailPage();

        step("Verify total refund currency still "+currency);
        String totalRefundSuccess = refundPage.getTotalRefund();
        Assertion.assertThat("Total refund currency is " +currency,totalRefundSuccess,containsString(currency));

        step("Verify total refund after success same as order");
        Assertion.assertThat("Total refund after success is same as order",totalRefundSuccess,is(priceOnOrderPage));

        step("Verify refund price details still "+currency);
        refundPage.openEstimatedRefundDetail();
        Assertion.assertThat("Prices in refund total detail break down is " +currency,refundPage.getRefundDetailPrices(),everyItem(containsString(currency)));
    }

    @Author(names = {"Zia"})
    @Module(name = "Room")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHomesExclusiveRoomNudgesLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyHomesExclusiveRoomNudgesLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String checkOutDate = addToDateYYYY_MM_DD(startDate, 1);

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String accommodationType = "HOMES";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);
        String appLanguage = homePage.getLanguageApp();

        step("Get nudges text from API");
        String nudgesText = hotelApi.getExclusiveNudges(username,password,hotelName,adult,startDate,night,room,appLanguage,"room");

        step("user open PDP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, room, adult, startDate, checkOutDate);

        step("Select See Room");
        detailPage.seeHotelRoom();

        step("Verify exclusive NHA room nudges same as in API in room list");
        Assertion.assertThat("Exclusive NHA Room Nudges exist and is same as in API", roomListPage.getRoomNudges(nudgesText), is(nudgesText));

        step("Click on room nudges in room list");
        roomListPage.clickNudgesRoom(nudgesText);

        step("Verify exclusive NHA room nudges same as in API in room detail");
        Assertion.assertThat("Exclusive NHA Room Nudges exist and is same as in API", roomListPage.getRoomNudges(nudgesText), is(nudgesText));
    }

    @Author(names = {"Zia"})
    @Module(name = "Room")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHomesFacilityRoomNudgesLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyHomesFacilityRoomNudgesLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String checkOutDate = addToDateYYYY_MM_DD(startDate, 1);

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String accommodationType = "HOMES";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);
        String appLanguage = homePage.getLanguageApp();

        step("Get nudges text from API");
        String nudgesText = hotelApi.getExclusiveNudges(username,password,hotelName,adult,startDate,night,room,appLanguage,"facility");

        step("user open PDP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, room, adult, startDate, checkOutDate);

        step("Select See Room");
        detailPage.seeHotelRoom();

        step("Verify facility room nudges same as in API in room list");
        Assertion.assertThat("Facility Room Nudges exist and is same as in API", roomListPage.getRoomNudges(nudgesText), is(nudgesText));

        step("Click on room nudges in room list");
        roomListPage.clickNudgesRoom(nudgesText);

        step("Verify facility nudges not showing in room detail");
        Assertion.assertThat("Facility nudges not showing in room detail", roomListPage.isNudgesPresent(nudgesText), is(false));
    }

    @Author(names = {"Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "Room")
    @Test
    public void verifyRoomRecommendationRoomCapacityLessThanSearchCriteriaNHALogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRoomRecommendationRoomCapacityLessThanSearchCriteriaNHALogin");
        BaseTest.paramsThreadLocal.set(param);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String hotelNameSearch = param.get("hotelName");
        int totalRoom = Integer.parseInt(param.get("totalRoom"));
        int totalGuest = Integer.parseInt(param.get("totalGuest"));
        String searchCriteria = "less";

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency to \"" + currency + "\"");
        homePage.changeCurrency(currency,username);

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(hotelNameSearch);

        step("set total guest: " + totalGuest);
        homesLandingPage.openTotalGuest();
        hotelLandingPage.setTotalGuest(totalGuest);
        hotelLandingPage.setTotalChildren(0);
        homesLandingPage.clickSubmit();

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Check room recommendation");
        int roomCapacity = roomListPage.getRecommendedRoomCapacity();
        Assertion.assertThat("room recommendation correct",roomListPage.checkRoomRecommendation(totalGuest,roomCapacity,searchCriteria),is(true));
        roomListPage.openRoomDetailForRecommendedRoom();

        step("Check room detail with updated room capacity");
        Assertion.assertThat("is room detail with updated room capacity",roomDetailPage.checkRoomDetail(totalGuest,roomCapacity,searchCriteria),is(true));
    }

    @Author(names = {"Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "Room")
    @Test
    public void verifyRoomRecommendationRoomCapacityMatchSearchCriteriaNHALogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRoomRecommendationRoomCapacityMatchSearchCriteriaNHALogin");
        BaseTest.paramsThreadLocal.set(param);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String hotelNameSearch = param.get("hotelName");
        int totalChild = Integer.parseInt(param.get("totalChild"));
        int totalAdult = Integer.parseInt(param.get("totalAdult"));
        String searchCriteria = "equal";

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency to \"" + currency + "\"");
        homePage.changeCurrency(currency,username);

        step("Open Homes landing page");
        homePage.openVerticalPageDeeplink("Homes", "", currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(hotelNameSearch);

        step("set total adult: " + totalAdult + " and total child: " + totalChild);
        homesLandingPage.openTotalGuest();
        hotelLandingPage.setTotalGuest(totalAdult);
        hotelLandingPage.setTotalChildren(totalChild);
        homesLandingPage.clickSubmit();

        step("Submit search");
        homesLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Check room recommendation");
        int roomCapacity = roomListPage.getRecommendedRoomCapacity();
        Assertion.assertThat("room recommendation correct",roomListPage.checkRoomRecommendation(totalAdult,roomCapacity,searchCriteria),is(true));
        roomListPage.openRoomDetailForRecommendedRoom();

        step("Check room detail with correct room capacity");
        Assertion.assertThat("is room detail with correct room capacity",roomDetailPage.checkRoomDetail(totalAdult,roomCapacity,searchCriteria),is(true));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "Room")
    @Test
    public void verifyTotalRoomBookingFormHomesLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyTotalRoomBookingFormHomesLogin");
        BaseTest.paramsThreadLocal.set(param);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int childAge = Integer.parseInt(param.get("childAge"));
        int totalRoom1 = Integer.parseInt(param.get("totalRoom1"));
        int totalGuest1 = Integer.parseInt(param.get("totalGuest1"));
        int totalRoom2 = Integer.parseInt(param.get("totalRoom2"));
        int totalGuest2 = Integer.parseInt(param.get("totalGuest2"));

        String rangeAddDate = param.get("rangeAddDate");
        int totalNight = Integer.parseInt(param.get("night"));
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "Homes";

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("user open PDP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom1, totalGuest1, checkInDate, checkOutDate, childAge);

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Check room recommendation");
        String totalRoomRecommendation = roomListPage.getRecommendedRoomAmount();
        String expectedTotalId = "(" + (totalRoom1) + " kamar,";
        String expectedTotalEn = "(" + (totalRoom1) + " room";
        Assertion.assertThat("Total room recommendation is "+(totalRoom1),totalRoomRecommendation,anyOf(containsStringIgnoringCase(expectedTotalId), containsStringIgnoringCase(expectedTotalEn)));

        step("Check room detail with updated room capacity");
        roomListPage.clickRecommendedRoom();

        String totalRoomDetail = roomListPage.getRecommendedRoomAmount();
        String expectedTotalDetailId = (totalRoom1) + " kamar,";
        String expectedTotalDetailEn = (totalRoom1) + " room";
        Assertion.assertThat("Total room in room detail is "+(totalRoom1),totalRoomDetail,anyOf(containsStringIgnoringCase(expectedTotalDetailId), containsStringIgnoringCase(expectedTotalDetailEn)));

        step("Check booking form detail");
        detailPage.clickBookButton(1);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        String bookingFormTotal = bookingFormPage.getHotelRoomNameBookingForm();
        String expectedTotalBookingFormId = (totalRoom1) + " Kamar";
        String expectedTotalBookingFormEn = (totalRoom1) + " Room";
        Assertion.assertThat("Total room in booking form is "+(totalRoom1),bookingFormTotal,anyOf(containsStringIgnoringCase(expectedTotalBookingFormId), containsStringIgnoringCase(expectedTotalBookingFormEn)));

        bookingFormPage.continueToAddOnPage();
        List<String> priceElementsTitle = bookingFormPage.getPriceTitles();
        Assertion.assertThat("Price element title contains " + (totalRoom1) + " room",
                priceElementsTitle,
                hasItem(anyOf(containsStringIgnoringCase((totalRoom1) + " kamar"), containsStringIgnoringCase((totalRoom1) + " room"))));


        step("user click PDP deeplink");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom2, totalGuest2, checkInDate, checkOutDate, childAge);

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Check room recommendation");
        String additionalRoomRecommendation = roomListPage.getRecommendationAdditionalRoom();
        int additionalRoom = CoreFunctions.getIntFromString(additionalRoomRecommendation).orElseThrow();
        Assertion.assertThat("Additional room recommendation is more than "+(totalRoom2),additionalRoom,greaterThan(totalRoom2));

        totalRoomRecommendation = roomListPage.getRecommendedRoomAmount();
        expectedTotalId = "(" + additionalRoom + " kamar,";
        expectedTotalEn = "(" + additionalRoom + " room";
        Assertion.assertThat("Total room recommendation is "+additionalRoom,totalRoomRecommendation,anyOf(containsStringIgnoringCase(expectedTotalId), containsStringIgnoringCase(expectedTotalEn)));

        step("Check room detail with updated room capacity");
        roomListPage.clickRecommendedRoom();
        String roomName = roomDetailPage.getRawHotelRoomNameOnRoomDetail();
        Assertion.assertThat("Hotel name in room detail contains "+additionalRoom+" x",roomName,containsStringIgnoringCase(additionalRoom+"x"));

        totalRoomDetail = roomListPage.getRecommendedRoomAmount();
        expectedTotalDetailId = additionalRoom + " kamar,";
        expectedTotalDetailEn = additionalRoom+ " room";
        Assertion.assertThat("Total room in room detail is "+additionalRoom,totalRoomDetail,anyOf(containsStringIgnoringCase(expectedTotalDetailId), containsStringIgnoringCase(expectedTotalDetailEn)));

        step("Check booking form detail");
        detailPage.clickBookButton(1);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        bookingFormTotal = bookingFormPage.getHotelRoomNameBookingForm();
        expectedTotalBookingFormId = additionalRoom + " Kamar";
        expectedTotalBookingFormEn = additionalRoom + " Room";
        Assertion.assertThat("Total room in booking form is "+additionalRoom,bookingFormTotal,anyOf(containsStringIgnoringCase(expectedTotalBookingFormId), containsStringIgnoringCase(expectedTotalBookingFormEn)));
        bookingFormPage.fillGuestDetail(false);
        bookingFormPage.continueToAddOnPage();
        priceElementsTitle = bookingFormPage.getPriceTitles();
        Assertion.assertThat("Price element title contains "+additionalRoom+" room",
                priceElementsTitle,
                hasItem(anyOf(containsStringIgnoringCase(additionalRoom+" kamar"), containsStringIgnoringCase(additionalRoom+" room"))));
    }

    @SneakyThrows
    @Author(names = {"Kartik"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "BookingForm")
    @Test
    public void verifyPriceWithoutTaxesForGuestsWithABT() {
        Map<String, String> map = getParam("verifyPriceWithoutTaxesForGuestsWithABT");
        BaseTest.paramsThreadLocal.set(map);

        String currency = "IDR";
        String location = map.get("location");
        String locationType = map.get("locationType").toUpperCase();
        String homesName = map.get("homesName");
        String filterOption = map.get("filterOption");
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));
        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, night);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "homes";

        Session<WebDriver> session = sessionThreadLocal.get();
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginAsGuest();
        homePage.changeCurrency(currency,"");

        step("Get app language");
        String language = homePage.getLanguageApp();
        String separator = ".";
        if(language.equals("en")){
            separator = ",";
        }

        step("Get the SRP deeplink parameter");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", location, accommodationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;

        step("open SRP via deeplink");
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);
        hotelLandingPage.fillNightCheckInForm();

        step("Click filter button");
        String priceDescription = searchResultPage.getPriceDescription();
        searchResultPage.clickFilterButton();

        step("apply total price filter");
        searchResultPage.selectHotelPriceDisplay(filterOption);

        step("apply price without tax filter");
        searchResultPage.selectPriceAfterTaxFilter(false, priceDescription);

        step("Submit filter");
        searchResultPage.clickFilterSubmit();
        searchResultPage.waitForSrpToLoad();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink(accommodationType, language, currency);

        step("check landing page section");
        HashMap<String,String> hotelLandingPageData = hotelApi.getHotelBeforeTaxInLandingPage(accommodationType,"","",currency,language,false);
        double hotelPriceLP = Double.parseDouble(hotelLandingPageData.get("basePrice"));
        String hotelPriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceLP),separator);
        double hotelBasePriceLP = Double.parseDouble(hotelLandingPageData.get("discountedPrice"));
        String hotelBasePriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceLP),separator);
        String landingPageHotelName = hotelLandingPageData.get("productName");

        boolean checkCrossedPriceLP = hotelPriceLPString.equals(hotelBasePriceLPString);
        Assertion.assertThat("Price in landing page is same as API price for homes "+landingPageHotelName,hotelLandingPage.checkHotelPrice(false,landingPageHotelName,hotelPriceLPString),is(anyOf(containsString(hotelPriceLPString),not(emptyOrNullString()))));
        if(!checkCrossedPriceLP){
            Assertion.assertThat("Crossed price in landing page is same as API price for homes "+landingPageHotelName,hotelLandingPage.checkHotelPrice(true,landingPageHotelName,hotelBasePriceLPString),is(anyOf(containsString(hotelBasePriceLPString),not(emptyOrNullString()))));
        }

        step("open SRP via deeplink");
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);
        searchResultPage.waitForSrpToLoad();

        step("Check hotel price in SRP");
        SearchHotelResponse searchHotelResponse = hotelApi.getSearchHotelData(accommodationType, locationId, locationType, "", "", night, room, adult, checkInDate, currency);
        String srpAPIHotelName = searchHotelResponse.data.contents.get(0).hotelName;
        double hotelPriceSRP = hotelApi.getSrpHotelTotalPriceWithTax(false, searchHotelResponse);
        double hotelBasePriceSRP = hotelApi.getSrpHotelCrossedTotalPriceWithTax(false, searchHotelResponse);
        String hotelPriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceSRP),separator);
        String hotelBasePriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceSRP),separator);
        Assertion.assertThat("Price in SRP page is same as API price for homes "+srpAPIHotelName,searchResultPage.checkHotelPrice(false,srpAPIHotelName,hotelPriceSRPString),is(true));
        if(!hotelPriceSRPString.equals(hotelBasePriceSRPString)) {
            Assertion.assertThat("Crossed price in SRP page is same as API price for homes "+srpAPIHotelName,searchResultPage.checkHotelPrice(true,srpAPIHotelName,hotelBasePriceSRPString),is(true));
        }
        Assertion.assertThat("Price in SRP page label is /night (include taxes)",searchResultPage.getPriceRemarkText(),is(anyOf(containsString("/night (include taxes"),containsString("/malam (termasuk pajak"))));

        step("Get PDP data");
        HotelAutoCompleteResponse hotelAutoCompleteResponsePDP = hotelApi.getAutocompleteData("", "", homesName, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponsePDP.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponsePDP.data.contents.get(0).country.name;
        SelectRoomResponse selectRoomResponse = hotelApi.getHotelRoomData(homesName, "", "", night, room, adult, checkInDate, currency);
        double hotelPricePDP = hotelApi.getHotelTotalPriceWithTax(false, selectRoomResponse);
        double hotelBasePricePDP = hotelApi.getHotelTotalCrossedPriceWithTax(false, selectRoomResponse);
        double hotelBasePricePDPWithTax = hotelApi.getBaseHotelCrossedPriceWithTax(true, selectRoomResponse);
        String hotelPricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDP),separator);
        String hotelBasePricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDP),separator);
        String hotelBasePricePDPWithTaxString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDPWithTax),separator);

        step("Check price in PDP");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, room, adult, checkInDate, checkOutDate);
        String priceOnDetail = detailPage.getHotelPrice();
        Assertion.assertThat("Price in PDP page is same as API price for homes "+homesName,priceOnDetail ,containsString(hotelPricePDPString));
        Assertion.assertThat("Price in PDP page label is /room/night",detailPage.isLabelPerRoomPerNight(),is(true));
        detailPage.seeHotelRoom();

        step("Check price in room list");
        boolean checkCrossedPriceRoomlist = hotelBasePricePDPString.equals(hotelPricePDPString);
        Assertion.assertThat("Price in room list page is same as API price for homes "+homesName,roomListPage.checkHotelPrice(false,hotelPricePDPString),is(true));
        if(!checkCrossedPriceRoomlist)
            Assertion.assertThat("Crossed price in SRP page is same as API price for homes "+homesName,roomListPage.checkHotelPrice(true,hotelBasePricePDPString),is(true));
        Assertion.assertThat("Price in SRP page label is /room/night",roomListPage.isLabelPerRoom(false),is(true));
        String priceAfterTax = roomListPage.getPriceAfterTax();
        roomListPage.clickBookRoom();

        step("Check price in booking form");
        String priceBookingform = bookingFormPage.getTotalPriceBookingForm();
        Assertion.assertThat("Price in booking form page is same as API price for homes "+homesName,priceBookingform,containsString(priceAfterTax));
        if(!checkCrossedPriceRoomlist){
            String crossedPriceBookingform = bookingFormPage.getCrossedPriceBookingForm();
            Assertion.assertThat("Crossed price in booking form page is same as API price for homes "+homesName,crossedPriceBookingform,containsString(hotelBasePricePDPWithTaxString));
        }
        Assertion.assertThat("Price in booking form page label is total price",bookingFormPage.isLabelTotalPrice(),is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "BookingForm")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void homesVerifyPriceWithoutTaxesTotalPriceFilterLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("homesVerifyPriceWithoutTaxesTotalPriceFilterLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String locationType = param.get("locationType");
        String homesName = param.get("homesName");
        String filterOption = param.get("filterOption");
        String rangeAddDate = param.get("rangeAddDate");
        int night = Integer.parseInt(param.get("night"));

        String[] dates = generateCheckInOutDate(rangeAddDate, night);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "homes";
        int room = 1;
        int adult = 1;
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency(currency, username);

        step("Get app language");
        String language = homePage.getLanguageApp();
        String separator = language.equals("en") ? "," : ".";

        step("Open homes landing page");
        homePage.openVerticalPageDeeplink("Homes", language, currency);

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("set total room and total guest");
        homesLandingPage.openTotalGuest();
        landingPage.setTotalGuest(adult);
        landingPage.setTotalChildren(0);
        homesLandingPage.clickSubmit();

        step("Submit search");
        homesLandingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Click filter button");
        String priceDescription = searchResultPage.getPriceDescription();
        searchResultPage.clickFilterButton();

        step("apply total price filter");
        searchResultPage.selectHotelPriceDisplay(filterOption);

        step("apply price without tax filter");
        searchResultPage.selectPriceAfterTaxFilter(false, priceDescription);

        step("Submit filter");
        searchResultPage.clickFilterSubmit();
        searchResultPage.waitForSrpToLoad();

        step("Go back to landing page");
        searchResultPage.clickBackButton();

        step("check landing page section");
        HashMap<String,String> hotelLandingPageData = hotelApi.getHotelAfterTaxInLandingPage(accommodationType, username, password, currency, language, false);
        double hotelPriceLP = Double.parseDouble(hotelLandingPageData.get("basePrice"));
        String hotelPriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceLP),separator);
        double hotelBasePriceLP = Double.parseDouble(hotelLandingPageData.get("discountedPrice"));
        String hotelBasePriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceLP),separator);
        String landingPageHotelName = hotelLandingPageData.get("productName");
        boolean checkCrossedPriceLP = hotelPriceLPString.equals(hotelBasePriceLPString);
        Assertion.assertThat("Price in landing page is same as API price for homes "+landingPageHotelName,landingPage.checkHotelPrice(false,landingPageHotelName,hotelBasePriceLPString),is(anyOf(containsString(hotelBasePriceLPString),not(emptyOrNullString()))));
        if(!checkCrossedPriceLP){
            Assertion.assertThat("Crossed price in landing page is same as API price for homes "+landingPageHotelName,landingPage.checkHotelPrice(true,landingPageHotelName,hotelPriceLPString),is(anyOf(containsString(hotelPriceLPString),not(emptyOrNullString()))));
        }

        step("user hit SRP deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);

        step("Check hotel price in SRP");
        SearchHotelResponse searchHotelResponse = hotelApi.getSearchHotelData(accommodationType, locationId, locationType, username, password, night, room, adult, checkInDate, currency);
        String srpAPIHotelName = searchHotelResponse.data.contents.get(0).hotelName;
        double hotelPriceSRP = hotelApi.getSrpHotelTotalPriceWithTax(false, searchHotelResponse);
        double hotelBasePriceSRP = hotelApi.getSrpHotelCrossedTotalPriceWithTax(false, searchHotelResponse);
        String hotelPriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceSRP),separator);
        String hotelBasePriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceSRP),separator);
        Assertion.assertThat("Price in SRP page is same as API price for homes "+srpAPIHotelName,searchResultPage.checkHotelPrice(false,srpAPIHotelName,hotelPriceSRPString),is(true));
        if(!hotelPriceSRPString.equals(hotelBasePriceSRPString))
            Assertion.assertThat("Crossed price in SRP page is same as API price for homes "+srpAPIHotelName,searchResultPage.checkHotelPrice(true,srpAPIHotelName,hotelBasePriceSRPString),is(true));
        Assertion.assertThat("Price in SRP page label is total price",searchResultPage.isLabelTotalPrice(false),is(true));

        step("user hit PDP deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponsePDP = hotelApi.getAutocompleteData(username, password, homesName, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponsePDP.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponsePDP.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, room, adult, checkInDate, checkOutDate);

        step("Check price in PDP");
        SelectRoomResponse selectRoomResponse = hotelApi.getHotelRoomData(homesName, username, password, night, room, adult, checkInDate, currency);
        double hotelPricePDP = hotelApi.getHotelTotalPriceWithTax(false, selectRoomResponse);
        double hotelBasePricePDP = hotelApi.getHotelTotalCrossedPriceWithTax(false, selectRoomResponse);
        String hotelPricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDP),separator);
        String hotelBasePricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDP),separator);
        String priceOnDetail = detailPage.getHotelPrice();
        Assertion.assertThat("Price in PDP page is same as API price for homes "+homesName,priceOnDetail ,containsString(hotelPricePDPString));
        Assertion.assertThat("Price in PDP page label is total price",detailPage.isLabelTotalPrice(),is(true));
        detailPage.seeHotelRoom();

        step("Check price in room list");
        boolean checkCrossedPriceRoomlist = hotelBasePricePDPString.equals(hotelPricePDPString);
        Assertion.assertThat("Price in room list page is same as API price for homes "+homesName,roomListPage.checkHotelPrice(false,hotelPricePDPString),is(true));
        if(!checkCrossedPriceRoomlist)
            Assertion.assertThat("Crossed price in SRP page is same as API price for homes "+homesName,roomListPage.checkHotelPrice(true,hotelBasePricePDPString),is(true));
        Assertion.assertThat("Price in SRP page label is total price",roomListPage.isLabelTotalPrice(false),is(true));
        roomListPage.clickBookRoom();

        step("Check price in booking form");
        hotelPricePDP = hotelApi.getHotelTotalPriceWithTax(true, selectRoomResponse);
        hotelBasePricePDP = hotelApi.getHotelTotalCrossedPriceWithTax(true, selectRoomResponse);
        hotelPricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDP),separator);
        hotelBasePricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDP),separator);
        String priceBookingform = bookingFormPage.getTotalPriceBookingForm();
        if(!priceBookingform.contains(hotelPricePDPString)){
            bookingFormPage.fillContactOrderDetail();
            bookingFormPage.fillGuestDetail(false);
            bookingFormPage.continueToAddOnPage();
            bookingFormPage.cancelProtection();
            priceBookingform = bookingFormPage.getTotalPriceBookingForm();
        }
        Assertion.assertThat("Price in booking form page is same as API price for homes "+homesName,priceBookingform,containsString(hotelPricePDPString));
        if(!checkCrossedPriceRoomlist){
            String crossedPriceBookingform = bookingFormPage.getCrossedPriceBookingForm();
            Assertion.assertThat("Crossed price in booking form page is same as API price for homes "+homesName,crossedPriceBookingform,containsString(hotelBasePricePDPString));
        }
        Assertion.assertThat("Price in booking form page label is total price",bookingFormPage.isLabelTotalPrice(),is(true));

        step("Check price in room list MO");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 2, 2, checkInDate, checkOutDate);
        selectRoomResponse = hotelApi.getHotelRoomData(homesName, username, password, night, 2, 2, checkInDate, currency);
        double hotelPricePDPMO = hotelApi.getBaseHotelPriceWithTax(false,selectRoomResponse);
        double hotelBasePricePDPMO = hotelApi.getBaseHotelCrossedPriceWithTax(false,selectRoomResponse);
        String hotelPricePDPStringMO = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDPMO),separator);
        String hotelBasePricePDPStringMO = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDPMO),separator);
        detailPage.seeHotelRoom();
        Assertion.assertThat("Price in room list page is same as API price for homes "+homesName,roomListPage.checkHotelPrice(false,hotelPricePDPStringMO),is(true));
        if(!hotelBasePricePDPStringMO.equals(hotelPricePDPStringMO))
            Assertion.assertThat("Crossed price in room list page is same as API price for homes "+homesName,roomListPage.checkHotelPrice(true,hotelBasePricePDPStringMO),is(true));
        Assertion.assertThat("Price in room list page label is total price",roomListPage.isLabelPerRoom(false),is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "BookingForm")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void homesVerifyPriceAfterTaxesTotalPriceFilterGuest() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("homesVerifyPriceAfterTaxesTotalPriceFilterGuest");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String hotelName = param.get("hotelName");
        String filterOption = param.get("filterOption");
        String rangeAddDate = param.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "homes";
        String locationType=param.get("locationType");
        int night = Integer.parseInt(param.get("night"));
        int room = 1;
        int adult = 1;

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginAsGuest();
        homePage.changeCurrency("IDR","");

        step("Get app language");
        String language = homePage.getLanguageApp();
        String separator = ".";
        if(language.equals("en")){
            separator = ",";
        }

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Homes", "", "IDR");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("set total room and total guest");
        homesLandingPage.openTotalGuest();
        landingPage.setTotalGuest(adult);
        landingPage.setTotalChildren(0);
        homesLandingPage.clickSubmit();

        step("Submit search");
        homesLandingPage.clickSubmit();
        searchResultPage.waitForSrpToLoad();
        landingPage.fillNightCheckInForm();

        step("Click filter button");
        boolean isPriceAfterTaxFilterApplied = searchResultPage.isPriceAfterTaxFilterApplied();
        searchResultPage.clickFilterButton();

        step("apply total price filter");
        searchResultPage.selectHotelPriceDisplay(filterOption);

        step("apply price after tax filter");
        searchResultPage.checkAndSelectPriceAfterTaxFilter(isPriceAfterTaxFilterApplied);

        step("Submit filter");
        searchResultPage.clickFilterSubmit();

        step("Go back to landing page");
        searchResultPage.clickBackButton();

        step("check landing page section");
        HashMap<String,String> hotelLandingPageData = hotelApi.getHotelAfterTaxInLandingPage("homes","","","IDR",language,true);
        double hotelPriceLP = Double.parseDouble(hotelLandingPageData.get("basePrice"));
        String hotelPriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceLP),separator);
        double hotelBasePriceLP = Double.parseDouble(hotelLandingPageData.get("discountedPrice"));
        String hotelBasePriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceLP),separator);
        String landingPageHotelName = hotelLandingPageData.get("productName");

        boolean checkCrossedPriceLP = hotelPriceLPString.equals(hotelBasePriceLPString);
        Assertion.assertThat("Price in landing page is same as API price for homes "+landingPageHotelName,landingPage.checkHotelPrice(false,landingPageHotelName,hotelPriceLPString),is(anyOf(containsString(hotelPriceLPString),not(emptyOrNullString()))));
        if(!checkCrossedPriceLP){
            Assertion.assertThat("Crossed price in landing page is same as API price for homes "+landingPageHotelName,landingPage.checkHotelPrice(true,landingPageHotelName,hotelBasePriceLPString),is(anyOf(containsString(hotelBasePriceLPString),not(emptyOrNullString()))));
        }

        step("user hit SRP deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", location, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);

        step("Check hotel price in SRP");
        searchResultPage.waitForSrpToLoad();
        String srpPageHotelName = searchResultPage.getFirstHotelName();
        double hotelPriceSRP = hotelApi.getHotelPriceWithTax(srpPageHotelName,"","",night,room,adult,checkInDate);
        String hotelPriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceSRP),separator);
        double hotelBasePriceSRP = hotelApi.getHotelCrossedPriceWithTax(srpPageHotelName,"","",night,room,adult,checkInDate);
        String hotelBasePriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceSRP),separator);

        boolean checkCrossedPriceSRP = hotelPriceSRPString.equals(hotelBasePriceSRPString);
        Assertion.assertThat("Price in SRP page is same as API price for hotel "+srpPageHotelName,searchResultPage.checkHotelPrice(false,srpPageHotelName,hotelPriceSRPString),is(true));
        if(!checkCrossedPriceSRP){
            Assertion.assertThat("Crossed price in SRP page is same as API price for hotel "+srpPageHotelName,searchResultPage.checkHotelPrice(true,srpPageHotelName,hotelBasePriceSRPString),is(true));
        }
        Assertion.assertThat("Price in SRP page label is total price",searchResultPage.isLabelTotalPrice(),is(true));

        step("Check price in PDP");
        HotelAutoCompleteResponse hotelAutoCompleteResponsePDP = hotelApi.getAutocompleteData("", "", hotelName, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponsePDP.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponsePDP.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, room, adult, checkInDate, checkOutDate);
        double hotelPricePDP = hotelApi.getHotelPriceWithTax(hotelName,"","",night,room,adult,checkInDate);
        String hotelPricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDP),separator);
        double hotelBasePricePDP = hotelApi.getHotelCrossedPriceWithTax(hotelName,"","",night,room,adult,checkInDate);
        String hotelBasePricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDP),separator);
        String priceOnDetail = detailPage.getHotelPrice();
        Assertion.assertThat("Price in PDP page is same as API price for hotel "+hotelName,priceOnDetail ,containsString(hotelPricePDPString));
        Assertion.assertThat("Price in PDP page label is total price",detailPage.isLabelTotalPrice(),is(true));
        detailPage.seeHotelRoom();

        step("Check price in room list");
        boolean checkCrossedPriceRoomlist = hotelBasePricePDPString.equals(hotelPricePDPString);
        Assertion.assertThat("Price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(false,hotelPricePDPString),is(true));
        if(!checkCrossedPriceRoomlist){
            Assertion.assertThat("Crossed price in SRP page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(true,hotelBasePricePDPString),is(true));
        }
        Assertion.assertThat("Price in SRP page label is total price",roomListPage.isLabelTotalPrice(),is(true));

        roomListPage.clickBookRoom();

        step("Check price in booking form");
        String priceBookingform = bookingFormPage.getTotalPriceBookingForm();

        if(!priceBookingform.contains(hotelPricePDPString)){
            bookingFormPage.fillContactOrderDetail();
            bookingFormPage.fillGuestDetail(false);
            bookingFormPage.continueToAddOnPage();
            bookingFormPage.cancelProtection();
            priceBookingform = bookingFormPage.getTotalPriceBookingForm();
        }

        Assertion.assertThat("Price in booking form page is same as API price for hotel "+hotelName,priceBookingform,containsString(hotelPricePDPString));

        if(!checkCrossedPriceRoomlist){
            String crossedPriceBookingform = bookingFormPage.getCrossedPriceBookingForm();
            Assertion.assertThat("Crossed price in booking form page is same as API price for hotel "+hotelName,crossedPriceBookingform,containsString(hotelBasePricePDPString));
        }
        Assertion.assertThat("Price in booking form page label is total price",bookingFormPage.isLabelTotalPrice(),is(true));

        step("Check room list MO");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 2, 2, checkInDate, checkOutDate);
        double hotelPricePDPMO = hotelApi.getBaseHotelPriceWithTax(hotelName,"","",night,2,2,checkInDate);
        String hotelPricePDPStringMO = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDPMO),separator);
        double  hotelBasePricePDPMO = hotelApi.getBaseHotelCrossedPriceWithTax(hotelName,"","",night,room,adult,checkInDate);
        String hotelBasePricePDPStringMO = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDPMO),separator);
        detailPage.seeHotelRoom();

        boolean checkCrossedPriceRoomlistMO = hotelBasePricePDPStringMO.equals(hotelPricePDPStringMO);
        Assertion.assertThat("Price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(false,hotelPricePDPStringMO),is(true));
        if(!checkCrossedPriceRoomlistMO){
            Assertion.assertThat("Crossed price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(true,hotelBasePricePDPStringMO),is(true));
        }
        Assertion.assertThat("Price in roomlist page label is per room",roomListPage.isLabelPerRoom(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "BookingForm")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void homesVerifyPriceAfterTaxesPerRoomPerNightFilterLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("homesVerifyPriceAfterTaxesPerRoomPerNightFilterLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String hotelName = param.get("hotelName");
        String filterOption = param.get("filterOption");
        String rangeAddDate = param.get("rangeAddDate");
        String accommodationType = "homes";
        String locationType=param.get("locationType");
        int night = Integer.parseInt(param.get("night"));
        String[] dates = generateCheckInOutDate(rangeAddDate, night);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        int room = 1;
        int adult = 1;

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR",username);

        step("Get app language");
        String language = homePage.getLanguageApp();
        String separator = ".";
        if(language.equals("en")){
            separator = ",";
        }

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Homes", language, "IDR");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);

        step("set total room and total guest");
        homesLandingPage.openTotalGuest();
        landingPage.setTotalGuest(adult);
        landingPage.setTotalChildren(0);
        homesLandingPage.clickSubmit();

        step("Submit search");
        homesLandingPage.clickSubmit();
        searchResultPage.waitForSrpToLoad();
        landingPage.fillNightCheckInForm();

        step("check if price after tax filter is already applied");
        boolean isPriceAfterTaxFilterApplied = searchResultPage.isPriceAfterTaxFilterApplied();

        step("Click filter button");
        searchResultPage.clickFilterButton();

        step("apply /night filter");
        searchResultPage.selectHotelPriceDisplay(filterOption);

        step("apply price after tax filter");
        searchResultPage.checkAndSelectPriceAfterTaxFilter(isPriceAfterTaxFilterApplied);

        step("Submit filter");
        searchResultPage.clickFilterSubmit();

        step("Go back to landing page");
        searchResultPage.clickBackButton();

        step("check landing page section");
        HashMap<String,String> hotelLandingPageData = hotelApi.getHotelAfterTaxInLandingPage("homes",username,password,"IDR",language,true);
        double hotelPriceLP = Double.parseDouble(hotelLandingPageData.get("basePrice"));
        String hotelPriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceLP),separator);
        double hotelBasePriceLP = Double.parseDouble(hotelLandingPageData.get("discountedPrice"));
        String hotelBasePriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceLP),separator);
        String landingPageHotelName = hotelLandingPageData.get("productName");

        boolean checkCrossedPriceLP = hotelPriceLPString.equals(hotelBasePriceLPString);
        Assertion.assertThat("Price in landing page is same as API price for homes "+landingPageHotelName,landingPage.checkHotelPrice(false,landingPageHotelName,hotelPriceLPString),is(anyOf(containsString(hotelPriceLPString),not(emptyOrNullString()))));
        if(!checkCrossedPriceLP){
            Assertion.assertThat("Crossed price in landing page is same as API price for homes "+landingPageHotelName,landingPage.checkHotelPrice(true,landingPageHotelName,hotelBasePriceLPString),is(anyOf(containsString(hotelBasePriceLPString),not(emptyOrNullString()))));
        }

        step("user hit SRP deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);

        step("Check hotel price in SRP");
        searchResultPage.waitForSrpToLoad();
        sleep(10);
        String srpPageHotelName = searchResultPage.getFirstHotelName();
        double hotelPriceSRP = hotelApi.getBaseHotelPriceWithTax(srpPageHotelName,username,password,night,room,adult,checkInDate);
        String hotelPriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceSRP),separator);
        double hotelBasePriceSRP = hotelApi.getBaseHotelCrossedPriceWithTax(srpPageHotelName,username,password,night,room,adult,checkInDate);
        String hotelBasePriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceSRP),separator);

        boolean checkCrossedPriceSRP = hotelPriceSRPString.equals(hotelBasePriceSRPString);
        Assertion.assertThat("Price in SRP page is same as API price for hotel "+srpPageHotelName,searchResultPage.checkHotelPrice(false,srpPageHotelName,hotelPriceSRPString),is(true));
        if(!checkCrossedPriceSRP){
            Assertion.assertThat("Crossed price in SRP page is same as API price for hotel "+srpPageHotelName,searchResultPage.checkHotelPrice(true,srpPageHotelName,hotelBasePriceSRPString),is(true));
        }
        Assertion.assertThat("Price in SRP page label is /night (include taxes)",searchResultPage.getPriceRemarkText(),is(anyOf(containsString("/night (include taxes"),containsString("/malam (termasuk pajak"))));


        step("Check price in PDP");
        HotelAutoCompleteResponse hotelAutoCompleteResponsePDP = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponsePDP.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponsePDP.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, room, adult, checkInDate, checkOutDate);
        double hotelPricePDP = hotelApi.getBaseHotelPriceWithTax(hotelName,username,password,night,room,adult,checkInDate);
        String hotelPricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDP),separator);
        double hotelBasePricePDP = hotelApi.getBaseHotelCrossedPriceWithTax(hotelName,username,password,night,room,adult,checkInDate);
        String hotelBasePricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDP),separator);
        String priceOnDetail = detailPage.getHotelPrice();
        Assertion.assertThat("Price in PDP page is same as API price for hotel "+hotelName,priceOnDetail ,containsString(hotelPricePDPString));
        Assertion.assertThat("Price in PDP page label is /room/night",detailPage.isLabelPerRoomPerNight(),is(true));
        detailPage.seeHotelRoom();

        step("Check price in room list");
        boolean checkCrossedPriceRoomList = hotelBasePricePDPString.equals(hotelPricePDPString);
        Assertion.assertThat("Price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(false,hotelPricePDPString),is(true));
        if(!checkCrossedPriceRoomList){
            Assertion.assertThat("Crossed price in SRP page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(true,hotelBasePricePDPString),is(true));
        }
        Assertion.assertThat("Price in SRP page label is /room/night",roomListPage.isLabelPerRoomPerNight(),is(true));

        roomListPage.clickBookRoom();

        step("Check price in booking form");
        String priceBookingform = bookingFormPage.getTotalPriceBookingForm();

        if(!priceBookingform.contains(hotelPricePDPString)){
            bookingFormPage.fillGuestDetail(false);
            bookingFormPage.continueToAddOnPage();
            bookingFormPage.cancelProtection();
            priceBookingform = bookingFormPage.getTotalPriceBookingForm();
        }

        Assertion.assertThat("Price in booking form page is same as API price for hotel "+hotelName,priceBookingform,containsString(hotelPricePDPString));

        if(!checkCrossedPriceRoomList){
            String crossedPriceBookingForm = bookingFormPage.getCrossedPriceBookingForm();
            Assertion.assertThat("Crossed price in booking form page is same as API price for hotel "+hotelName,crossedPriceBookingForm,containsString(hotelBasePricePDPString));
        }
        Assertion.assertThat("Price in booking form page label is total price",bookingFormPage.isLabelTotalPrice(),is(true));

        step("Check room list MO");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 2, 2, checkInDate, checkOutDate);
        double hotelPricePDPMO = hotelApi.getBaseHotelPriceWithTax(hotelName,username,password,night,2,2,checkInDate);
        String hotelPricePDPStringMO = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDPMO),separator);
        double  hotelBasePricePDPMO = hotelApi.getBaseHotelCrossedPriceWithTax(hotelName,username,password,night,room,adult,checkInDate);
        String hotelBasePricePDPStringMO = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDPMO),separator);
        detailPage.seeHotelRoom();

        boolean checkCrossedPriceRoomlistMO = hotelBasePricePDPStringMO.equals(hotelPricePDPStringMO);
        Assertion.assertThat("Price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(false,hotelPricePDPStringMO),is(true));
        if(!checkCrossedPriceRoomlistMO){
            Assertion.assertThat("Crossed price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(true,hotelBasePricePDPStringMO),is(true));
        }
        Assertion.assertThat("Price in room list page label is room/night",roomListPage.isLabelPerRoomPerNight(),is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHomesWishlistLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyHomesWishlistLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String locationType = map.get("locationType");
        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "homes";

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open the SRP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, accommodationType.toUpperCase());
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), 1, 1, locationId, locationType, location, checkInDate, checkOutDate, "");
        landingPage.fillNightCheckInForm();

        step("On one of the property card, click the wishlist (love) icon");
        String hotelName = searchResultPage.clickHeartIcon();

        step("After successfully save wishlist, check on the SRP, SRP Maps & PDP");
        Assertion.assertThat("the snackbar with a \"Manage\" text button should be visible for a few seconds", searchResultPage.isWishlistDialogShown(), either(equalToIgnoringCase("Manage")).or(equalToIgnoringCase("Atur")));

        step("Find the hotel: \""+hotelName+"\" on the SRP");
        Assertion.assertThat("Wishlist button color on SRP changed to red", searchResultPage.isWishlistActive(hotelName), is(true));

        step("Find the hotel: \""+hotelName+"\" on the SRP Maps");
        searchResultPage.clickSeeMapButton();
        Assertion.assertThat("Wishlist button color on SRP Maps changed to red", searchResultPage.isWishlistActiveOnMap(hotelName), is(true));

        step("Open the PDP from the SRP Maps");
        searchResultPage.chooseHotelCardOnMap(hotelName);
        Assertion.assertThat("Wishlist button color on PDP changed to red", detailPage.isWishlistActive(), is(true));
    }

    @Author(names = {"Aditi"})
    @Module(name = "Wishlist")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void testAddHomesInWishlist(){
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        WishlistPage<?> wishlistPage = new WishlistPage<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        Map<String, String> param = getParam("testAddHomesInWishlist");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");

        step("User goto home");
        homePage.gotoHome();

        step("User open app as login user " +username);
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR", username);

        step("open Homes SRP page");
        homePage.openVerticalPageDeeplink("homes","","IDR");

        step("Search stay location");
        homesLandingPage.searchStayLocation(location);
        homesLandingPage.clickSubmit();

        step("Select the wishlist button on the first product");
        boolean result = searchResultPage.clickWishlistFirstHotelCard();

        step("Check if snackbar appears");
        Assertion.assertThat("Snackbar appears with the Manage button", result, is(true));

        step("Check if the wishlist button color on SRP changed to red");
        Assertion.assertThat("Wishlist icon is filled on the SRP", searchResultPage.isWishlistIconSelected(), is(true));

        step("Check that the wishlist button color on SRP Maps changed to red");
        searchResultPage.openHotelCardInMaps();
        Assertion.assertThat("Wishlist icon is filled on the SRP Maps", searchResultPage.isWishlistIconSelected(), is(true));
        searchResultPage.closeMaps();

        step("Open the first hotel page and check if wishlist button color on PDP changed to red");
        searchResultPage.selectFirstHotelCard();
        Assertion.assertThat("Wishlist icon is filled on the PDP", searchResultPage.isWishlistIconSelected(), is(true));

        step("Unclick the wishlist button on the first product on the SRP");
        detailPage.clickBackButton();
        searchResultPage.clickRedWishlistIcon();

    }

    @Author(names = {"Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Homes")
    @Module(name = "PDP")
    @Test
    public void verifyHomesPdpCtaAndOccupancy() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyHomesPdpCtaAndOccupancy");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String currency = param.getOrDefault("currency", "IDR");
        String country = param.getOrDefault("country", "Indonesia");
        String wanderlustCategory = param.getOrDefault("wanderlustCategory", "Villa");
        int seeMoreLoop = Integer.parseInt(param.getOrDefault("seeMoreLoop", "2"));
        int childCount = Integer.parseInt(param.getOrDefault("childCount", "1"));
        String hotelNameSearch = param.get("hotelName");
        String dateType = param.get("dateType");
        String date = param.get("date");
        int totalRoom = Integer.parseInt(param.get("totalRoom"));
        int totalGuest = Integer.parseInt(param.get("totalGuest"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        HomesLandingPage<WebDriver> homesLandingPage = HomesLandingPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        HomesDetailPage<WebDriver> homesDetailPage = HomesDetailPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HomesRoomListPage<WebDriver> homesRoomListPage = HomesRoomListPage.getInstance(session);
        HomesRoomDetailPage<WebDriver> homesRoomDetailPage = HomesRoomDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username);
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("Change country to " + country);
        accountPage.chooseCountry(country);
        homePage.gotoHome();

        step("Change currency to " + currency);
        homePage.changeCurrency(currency, username);

        step("Go to NHA landing page");
        homePage.openVerticalPageDeeplink("Homes", "", currency);

        step("Verify See More / Back to Top button functionality by toggling it " + seeMoreLoop + " times");
        boolean fabFunctionalityCorrect = homesLandingPage.verifySeeMoreAndBackToTopFunctionality(seeMoreLoop);
        Assertion.assertThat("See More & Back to Top FAB should toggle correctly", fabFunctionalityCorrect, is(true));

        step("Swipe to 'Satisfy your wanderlust' section");
        homesLandingPage.swipeToSatisfyWanderlust();

        step("Click on '" + wanderlustCategory + "' in 'Satisfy your wanderlust' section");
        homesLandingPage.clickWanderlustCategory(wanderlustCategory);
        landingPage.fillNightCheckInForm();

        step("On the SRP, swipe to the 'Do you have flexible stay dates' section");
        HomesSearchResultPage<WebDriver> homesSearchResultPage = HomesSearchResultPage.getInstance(session);
        Assertion.assertThat("'Do you have flexible stay dates' section should be displayed on the SRP", homesSearchResultPage.isFlexibleStayDatesSectionShown(), is(true));

        step("Capture the hotel name and location of the first card in the 'Do you have flexible stay dates' section");
        String flexibleCardName = homesSearchResultPage.getFirstFlexibleStayHotelName();
        String flexibleCardLocation = homesSearchResultPage.getFirstFlexibleStayLocation();

        step("Click the first card in the 'Do you have flexible stay dates' section");
        homesSearchResultPage.selectFirstFlexibleStayCard();

        step("Verify the hotel name and location on the PDP match the selected card");
        Assertion.assertThat("Hotel name on PDP should match the selected 'flexible stay dates' card", detailPage.getHotelNameDetail(), equalToIgnoringCase(flexibleCardName));
        Assertion.assertThat("Location on PDP should match the selected 'flexible stay dates' card", homesDetailPage.isSameLocation(flexibleCardLocation, detailPage.getHotelLocationDetail()), is(true));

        step("Click on 'Similar Places' button on the PDP");
        Assertion.assertThat("'Similar places' button should be displayed on the PDP", homesDetailPage.isSimilarPlacesButtonShown(), is(true));
        homesDetailPage.clickSimilarPlacesButton();

        step("Capture the name and location of the first hotel in the 'Similar places' module");
        String similarCardName = homesDetailPage.getFirstSimilarPlaceName();
        String similarCardLocation = homesDetailPage.getFirstSimilarPlaceLocation();

        step("Select the hotel from the 'Similar places' module");
        homesDetailPage.selectFirstSimilarPlaceHotel();

        step("Verify the hotel name and location on the PDP match the selected 'Similar places' card");
        Assertion.assertThat("Hotel name on PDP should match the selected 'Similar places' card", detailPage.getHotelNameDetail(), equalToIgnoringCase(similarCardName));
        Assertion.assertThat("Location on PDP should match the selected 'Similar places' card", homesDetailPage.isSameLocation(similarCardLocation, detailPage.getHotelLocationDetail()), is(true));

        //go to landing page and search for the hotel to verify child occupancy
        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("search stay location: " + hotelNameSearch);
        landingPage.searchStayLocation(hotelNameSearch);

        step("set stay date");
        landingPage.setStayDate(dateType, date);

        step("set total room: " + totalRoom + " and total guest: " + totalGuest);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalGuest);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Change guest details: add " + childCount + " child");
        homesRoomListPage.addChildGuest(childCount);

        step("Swipe to and select the room showing " + childCount + " child occupancy");
        homesRoomListPage.openRoomWithChildOccupancy(childCount);

        step("On the room detail page, verify the child occupancy reflects " + childCount + " child");
        String childOccupancy = homesRoomDetailPage.getChildOccupancy(childCount);
        Assertion.assertThat("Child occupancy on room detail should reflect " + childCount + " child", childOccupancy,
                anyOf(containsString(childCount + " Child"), containsString(childCount + " Anak")));
    }
}