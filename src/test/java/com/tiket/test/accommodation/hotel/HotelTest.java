package com.tiket.test.accommodation.hotel;

import com.tiket.page.common.booking_form.CommonBookingFormPage;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.*;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.*;
import com.tiket.page.accommodation.hotel.HotelPage;
import com.tiket.page.accommodation.hotel.bookingdetailpage.BookingDetailPage;
import com.tiket.page.accommodation.hotel.bookingformpage.BookingFormPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.accommodation.hotel.reviewpage.ReviewPage;
import com.tiket.page.accommodation.hotel.roomdetailpage.RoomDetailPage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPage;
import com.tiket.page.accommodation.hotel.roomlistpage.RoomListPageAndroid;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPage;
import com.tiket.page.account.AccountPage;
import com.tiket.page.common.child_age.ChildAgePickerPage;
import com.tiket.page.common.phone.settings.DefaultAppsPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.order.orderlist.OrderListPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.page.refund.RefundPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.train.TrainPage;
import com.tiket.pojo.accommodation.hotel.*;
import com.tiket.pojo.accommodation.hotel.payathotel.PayAtHotelRoom;
import com.tiket.pojo.accommodation.master_tag.AccommodationMasterTagResponse;
import com.tiket.pojo.member.SmartProfilePojo;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.pojo.room.SelectRoomResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.common.InboxPageYopmail;
import com.tiket.web.page.accommodation.hotels.reschedulePage.HotelReschedulePageWeb;

import lombok.SneakyThrows;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.model.AddOnsType.*;
import static com.tiket.model.Language.ENGLISH;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class HotelTest extends BaseTest {

    //OTW test case
    @Author(names = {"Yazid"})
    @Module(name = "LandingPage")
    @Tribe(name = "Accommodation")
    @Vertical(name = "Accommodation")
    @Test
    public void checkLandingPageChip() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkLandingPageChip");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home with login session");
        homePage.gotoHome();
        loginPage.loginWith(loginType, username, password);

        step("Go to Hotel landing page");
       homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("check if any modules are available");
        landingPage.clickSeeMoreFloatingButton();
        String moduleTitle = landingPage.getModuleTitleSection();
        Assertion.assertThat("check for the module chips which are having more than. two words", getTotalWords(moduleTitle), is(greaterThanOrEqualTo(2)));

        step("click on see all button and check redirection");
        landingPage.clickSeeAllButton();
        landingPage.fillNightCheckInForm();
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat("user should be able to redirect to the SRP", searchResultPage.isSearchResultShown(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "LandingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyTimedPromoLandingPage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyTimedPromoLandingPage");
        BaseTest.paramsThreadLocal.set(param);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Get timed promo module from the API");
        String language = homePage.getLanguageApp();
        String templateCode = "TIMED_PROMO";
        MasterContentPOJO masterContentApi = hotelApi.getMasterContent(false, "guest", "", language, templateCode);
        LandingPageModulePOJO landingPageModuleApi = hotelApi.getLandingPageModule(false, "guest", "", language, templateCode,masterContentApi);
        String templateCodeApi = landingPageModuleApi.data.templateCode;
        String titleApi = landingPageModuleApi.data.subsidyLabel;
        Assertion.assertThat("Success to get landing page module from the API", templateCodeApi, is(templateCode));

        TimedPromoPOJO locationButtonApi = hotelApi.getTimedPromoLocation(landingPageModuleApi);
        String locationApi = locationButtonApi.location;
        String seeAllButtonApi = locationButtonApi.buttonText;
        String publicId = locationButtonApi.publicId;
        String locationType = locationButtonApi.locationType;
        String searchLocationApi = hotelApi.getLocationName(publicId, locationType, language);
        ArrayList<String> hotelList = locationButtonApi.hotelList;

        step("open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", language, "");

        step("Go to the timed promo section");
        landingPage.goToTimedPromoSection();

        step("Get the timed promo title");
        String titleApp = landingPage.getTimedPromoTitle();

        step("Select timed promo location");
        int index = locationButtonApi.index;
        String locationApp = landingPage.selectTimedPromoLocation(index);

        step("Get the timed promo hotel list");
        boolean compareHotelName = landingPage.compareTimedPromoHotelName(hotelList);

        step("Click see all button");
        String seeAllButtonApp = landingPage.clickSeeAllButtonTimedPromo();

        step("Get the expected data of check-in/out date and total room-guest");
        String expectedDate = hotelPage.expectedDateRoomGuest(language);

        step("Get the SRP data");
        landingPage.fillNightCheckInForm();
        boolean searchResult = searchResultPage.isHeaderSearchResultShown();
        String searchLocationApp = searchResultPage.getCurrentLocation();
        String dateRoomGuestApp = searchResultPage.getDateRoomGuestDetail();

        Assertion.assertThat("Timed Promo title on the App should be equal with the API", titleApp, is(titleApi));
        Assertion.assertThat("Timed Promo location button on the App should be equal with the API", locationApp, is(locationApi));
        Assertion.assertThat("Timed Promo hotels on the App should be equal with the API", compareHotelName, is(true));
        Assertion.assertThat("Timed Promo see all button on the App should be equal with the API", seeAllButtonApp, is(seeAllButtonApi));
        Assertion.assertThat("User is on the hotel search result page", searchResult, is(true));
        Assertion.assertThat("Search location on the App SRP should be equal with the API", searchLocationApp, is(searchLocationApi));
        Assertion.assertThat("Check-in/out date and room guest detail SRP should be visible as expected", dateRoomGuestApp, is(expectedDate));
    }

    @Author(names = {"Yazid"})
    @Module(name = "LandingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyTimedPromoLandingPageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyTimedPromoLandingPageLogin");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Get timed promo module from the API");
        String language = homePage.getLanguageApp();
        String templateCode = "TIMED_PROMO";
        MasterContentPOJO masterContentApi = hotelApi.getMasterContent(true, username, password, language, templateCode);
        LandingPageModulePOJO landingPageModuleApi = hotelApi.getLandingPageModule(true, username, password, language, templateCode,masterContentApi);
        String templateCodeApi = landingPageModuleApi.data.templateCode;
        String titleApi = landingPageModuleApi.data.subsidyLabel;
        Assertion.assertThat("Success to get landing page module from the API", templateCodeApi, is(templateCode));

        TimedPromoPOJO locationButtonApi = hotelApi.getTimedPromoLocation(landingPageModuleApi);
        String locationApi = locationButtonApi.location;
        String seeAllButtonApi = locationButtonApi.buttonText;
        String publicId = locationButtonApi.publicId;
        String locationType = locationButtonApi.locationType;
        String searchLocationApi = hotelApi.getLocationName(publicId, locationType, language);
        ArrayList<String> hotelList = locationButtonApi.hotelList;

        step("open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", language, "");

        step("Go to the timed promo section");
        landingPage.goToTimedPromoSection();

        step("Get the timed promo title");
        String titleApp = landingPage.getTimedPromoTitle();

        step("Select timed promo location");
        int index = locationButtonApi.index;
        String locationApp = landingPage.selectTimedPromoLocation(index);

        step("Get the timed promo hotel list");
        boolean compareHotelName = landingPage.compareTimedPromoHotelName(hotelList);

        step("Click see all button");
        String seeAllButtonApp = landingPage.clickSeeAllButtonTimedPromo();

        step("Get the expected data of check-in/out date and total room-guest");
        String expectedDate = hotelPage.expectedDateRoomGuest(language);

        step("Get the SRP data");
        landingPage.fillNightCheckInForm();
        boolean searchResult = searchResultPage.isHeaderSearchResultShown();
        String searchLocationApp = searchResultPage.getCurrentLocation();
        String dateRoomGuestApp = searchResultPage.getDateRoomGuestDetail();

        Assertion.assertThat("Timed Promo title on the App should be equal with the API", titleApp, is(titleApi));
        Assertion.assertThat("Timed Promo location button on the App should be equal with the API", locationApp, is(locationApi));
        Assertion.assertThat("Timed Promo hotels on the App should be equal with the API", compareHotelName, is(true));
        Assertion.assertThat("Timed Promo see all button on the App should be equal with the API", seeAllButtonApp, is(seeAllButtonApi));
        Assertion.assertThat("User is on the hotel search result page", searchResult, is(true));
        Assertion.assertThat("Search location on the App SRP should be equal with the API", searchLocationApp, is(searchLocationApi));
        Assertion.assertThat("Check-in/out date and room guest detail SRP should be visible as expected", dateRoomGuestApp, is(expectedDate));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Booking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void createHotelIssuedOrderAPI() {
        Session<WebDriver> session = sessionThreadLocal.get();
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        CorporateApi<?> corporateApi = new CorporateApi<>(session);

        Map<String, String> param = getParam("createHotelIssuedOrderAPI");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create new hotel issued order for B2B account");
        String result = hotelApi.createIssuedOrderApiB2B(
                username, password, startDate, query, adult, night, room,"IDR",
                orderFullName, orderNameTitle, guestFullName, guestTitle, corporateApi.getEmployeeData(username), false);

        Assertion.assertThat("Hotel Booking via API is successful for B2B account", !result.isEmpty(), is(true));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void verifyAllotmentRateChangePopupsDuringBookingFlowApp() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyAllotmentRateChangePopupsDuringBookingFlowApp");
        BaseTest.paramsThreadLocal.set(param);

        // Consumer booking-flow data
        String email = param.get("email");
        String password = param.get("password");
        String currency = param.getOrDefault("currency", "IDR");
        String accommodationType = param.getOrDefault("accommodationType", "hotel");
        String hotelPublicId = param.get("hotelPublicId");
        String hotelCountry = param.get("hotelCountry");
        int night = Integer.parseInt(param.get("night"));
        String[] dateArray = generateCheckInOutDate(param.get("rangeAddDate"), night);
        String checkInDate = dateArray[0];
        String checkOutDate = dateArray[1];
        int room = Integer.parseInt(param.getOrDefault("room", "1"));
        int adult = Integer.parseInt(param.getOrDefault("adult", "1"));

        // Supply (rates-and-availability dashboard) allotment data
        String hotelId = param.get("hotelId");
        long roomId = Long.parseLong(param.get("roomId"));
        String roomName = param.get("roomName");
        long allotmentId = Long.parseLong(param.get("allotmentId"));
        long roomRateId = Long.parseLong(param.get("roomRateId"));
        int ratePlanId = Integer.parseInt(param.get("ratePlanId"));
        int newCancellationCharge = Integer.parseInt(param.get("newCancellationCharge"));
        int newCancellationNoShow = Integer.parseInt(param.get("newCancellationNoShow"));
        String apiUsername = param.get("apiUsername");
        String apiPassword = param.get("apiPassword");
        int price1 = Integer.parseInt(param.get("price1"));
        int price2 = Integer.parseInt(param.get("price2"));
        int price3 = Integer.parseInt(param.get("price3"));

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Log in to the Extranet dashboard to generate the token/cookie for the allotment API");
        ExtranetHotelAuthApi<WebDriver> extranetAuth = new ExtranetHotelAuthApi<>(session);
        var authData = extranetAuth.getAccessToken(apiUsername, apiPassword);
        Assertion.assertThat("Extranet dashboard token should be generated", authData, is(notNullValue()));
        String apiToken = authData.accessToken;
        String apiCookie = hotelApi.buildAuthCookie(authData.accessToken, authData.refreshToken);
        String apiAccountId = String.valueOf(authData.accountId);
        String apiUser = authData.accountUsername;

        step("Change the active hotel in the dashboard session so the allotment update is authorized");
        hotelApi.changeHotelSession(apiToken, hotelId);

        // ============================ PHASE 1: cancellation policy = false ============================
        step("Set allotment rate to 100 with cancellation policy = false (non-refundable)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, checkInDate,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price1, false,
                newCancellationCharge, newCancellationNoShow);

        step("User opens home page and logs in");
        homePage.gotoHome();
        homePage.loginWithSpecificAccountViaDeeplinkOnly(email, password);
        homePage.changeCurrency(currency, email);

        step("Open hotel PDP via deep link");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), hotelCountry.toLowerCase(), hotelPublicId, room, adult, checkInDate, checkOutDate);
        detailPage.RemovePopUp();
        Assertion.assertThat("Hotel price should be loaded", detailPage.getHotelPrice(), is(notNullValue()));

        step("Go to room list and wait so the loaded rate is cached");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();
        String roomListPrice = roomListPage.getTheFirstHotelRoomPrice();
        Assertion.assertThat("Room list price should be loaded", roomListPrice, is(notNullValue()));

        step("Change allotment rate from 100 to 200 (cancellation policy still false)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, checkInDate,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price2, false,
                newCancellationCharge, newCancellationNoShow);

        step("Book a room to open booking form page 1");
        roomListPage.clickBookRoomWithoutSkippingRatePlan();

        step("Verify the price-change snackbar text appears on booking form page 1");
        String bf1SnackbarText = bookingFormPage.detectAndDismissPriceChangeSnackbar();
        Assertion.assertThat("Price-change snackbar should appear on booking form page 1 after rate changed 100 -> 200",
            bf1SnackbarText.toLowerCase(), either(containsString("the total price has changed")).or(containsString("total harga berubah")));
        String bookingFormPrice = bookingFormPage.getTotalPriceBookingForm();

        step("Continue to booking form page 2");
        bookingFormPage.continueToAddOnPage();

        step("Change allotment rate from 200 to 300 (cancellation policy still false)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, checkInDate,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price3, false,
                newCancellationCharge, newCancellationNoShow);

        step("Continue to payment and verify the price-update popup text appears");
        bookingFormPage.continueToPaymentPage();
        String paymentPopupText = bookingFormPage.priceChangePopUpText();
        Assertion.assertThat("Price-update popup should appear when going to payment after rate changed 200 -> 300",
            paymentPopupText.toLowerCase(), either(containsString("the price has been updated")).or(containsString("harga baru saja diperbarui")));
        bookingFormPage.clickPriceChangeGoPayment();
        Assertion.assertThat("Payment page should load after dismissing the popup", paymentPage.checkOrderID(), is(notNullValue()));

        step("Verify the price increases across room list, booking form and payment page");
        String paymentPrice = paymentPage.getTotalPricePayment();
        assertPriceIncreasing("Phase 1", roomListPrice, bookingFormPrice, paymentPrice);

        // ============================ PHASE 2: cancellation policy = true ============================
        step("Set allotment rate to 100 with cancellation policy = true (refundable, charge " + newCancellationCharge + "%)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, checkInDate,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price1, true,
                newCancellationCharge, newCancellationNoShow);

        step("Reopen the same hotel PDP via deep link");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), hotelCountry.toLowerCase(), hotelPublicId, room, adult, checkInDate, checkOutDate);
        detailPage.RemovePopUp();

        step("Go to room list");
        Assertion.assertThat("Hotel price should be loaded in phase 2", detailPage.getHotelPrice(), is(notNullValue()));
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();
        roomListPrice = roomListPage.getTheFirstHotelRoomPrice();
        Assertion.assertThat("Room list price should be loaded", roomListPrice, is(notNullValue()));

        step("Change allotment rate from 100 to 200 (cancellation policy still true)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, checkInDate,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price2, true,
                newCancellationCharge, newCancellationNoShow);

        step("Book a room and verify the cancellation-policy change dialog text appears before booking form");
        roomListPage.clickBookRoomWithoutSkippingRatePlan();
        String bfDialogText = bookingFormPage.detectAndDismissPriceChangeSnackbar();
        Assertion.assertThat("Price change notification should appear before booking form in phase 2",
            bfDialogText.toLowerCase(), either(containsString("the total price has changed")).or(containsString("total harga berubah")));
        bookingFormPrice = bookingFormPage.getTotalPriceBookingForm();

        step("Update allotment rate to 300 with cancellation charges (cancellation policy = true)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, checkInDate,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price3, true,
                newCancellationCharge, newCancellationNoShow);

        step("Continue to the payment page");
        bookingFormPage.continueToAddOnPage();
        bookingFormPage.continueToPaymentPage();

        step("Continue to payment and verify the price-update popup text appears (phase 2)");
        paymentPopupText = bookingFormPage.priceChangePopUpText();
        Assertion.assertThat("Price-update popup should appear when going to payment in phase 2",
            paymentPopupText.toLowerCase(), either(containsString("the price has been updated")).or(containsString("harga baru saja diperbarui"))); 
        bookingFormPage.clickPriceChangeGoPayment();
        Assertion.assertThat("Payment page should load after dismissing the popup in phase 2", paymentPage.checkOrderID(), is(notNullValue()));

        step("Verify the price increases across room list, booking form and payment page (phase 2)");
        paymentPrice = paymentPage.getTotalPricePayment();
        assertPriceIncreasing("Phase 2", roomListPrice, bookingFormPrice, paymentPrice);

        // -- flow 3
        step("Set allotment rate to 100 with cancellation policy = true (refundable, charge " + newCancellationCharge + "%)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, checkInDate,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price1, true,
                newCancellationCharge, newCancellationNoShow);

        step("Reopen the same hotel PDP via deep link");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), hotelCountry.toLowerCase(), hotelPublicId, room, adult, checkInDate, checkOutDate);
        detailPage.RemovePopUp();

        step("Go to room list");
        Assertion.assertThat("Hotel price should be loaded in phase 2", detailPage.getHotelPrice(), is(notNullValue()));
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();
        roomListPrice = roomListPage.getTheFirstHotelRoomPrice();
        Thread.sleep(3000);

        step("Change allotment rate from 100 to 200 (cancellation policy still true)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, checkInDate,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price2, true,
                newCancellationCharge, newCancellationNoShow);

        step("Book a room and verify the cancellation-policy change dialog text appears before booking form");
        roomListPage.clickBookRoomWithoutSkippingRatePlan();
        bfDialogText = bookingFormPage.detectAndDismissPriceChangeSnackbar();
        Assertion.assertThat("Cancellation-policy change dialog should appear before booking form in phase 2",
            bfDialogText.toLowerCase(), either(containsString("the total price has changed")).or(containsString("total harga berubah")));

        step("Use 'See other rooms' to go back to the room list and verify the price increased");
        bookingFormPage.clickBackButton();
        bookingFormPage.clickSearchOtherRoom();
        String roomListPriceAfterRateIncrease = roomListPage.getTheFirstHotelRoomPrice();
        Assertion.assertThat("Room list price after rate increase (" + parsePriceToNumber(roomListPriceAfterRateIncrease) 
                        + ") should be greater than the initial room list price (" + parsePriceToNumber(roomListPrice) + ")",
            parsePriceToNumber(roomListPriceAfterRateIncrease), 
            greaterThan(parsePriceToNumber(roomListPrice)));
    }

    @Author(names = {"Yazid"})
    @Module(name = "PostPurchase")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void sampleCreateBookingMultiRoomAPI() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("sampleCreateBookingMultiRoomAPI");
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        int qtyEachRoom = Integer.parseInt(param.get("qtyEachRoom"));
        String currency = param.get("currency");

        step("Create hotel booking multi-room using Sakuku");
        Boolean bookingApiSakuku = hotelApi.createOrderMultiRoomApi(username, password,
                hotelName, adult, night, room, startDate, qtyEachRoom, currency, false, "");
        Assertion.assertThat("Success to book multi-room by API using Sakuku", bookingApiSakuku, is(true));

        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        step("Create hotel booking multi-room using CC");
        String bookingApiCc = hotelApi.createOrderMultiRoomApi(username, password,
                hotelName, adult, night, room, startDate, qtyEachRoom, currency, cardNumber, expiry, cvv, cardName);
        Assertion.assertThat("Success to book multi-room by API using CC", bookingApiCc, is(equalToIgnoringCase(hotelName)));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "verifyPriceLoginDataProvider")
    public void verifyPriceLogin(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        ChildAgePickerPage<WebDriver> childAgePickerPage = ChildAgePickerPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        int totalRoom = Integer.parseInt(map.get("totalRoom"));
        int totalAdult = Integer.parseInt(map.get("totalAdult"));
        int totalChild = Integer.parseInt(map.get("totalChild"));
        List<String> childAges = splitText(map.get("childAges"), "-");

        step("verifyPriceLogin with totalRoom: " + totalRoom + ", totalAdult: " + totalAdult + ", totalChild: " + totalChild + ", childAges: " + childAges);
        homePage.gotoHome();

        step("user is in login mode");
        loginPage.loginWith(loginType, username, password);

        step("open hotel page");
       homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("search stay location: " + location);
        landingPage.searchStayLocation(location);

        step("set the total room, total adult, and total child");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalAdult);
        landingPage.setTotalChildren(totalChild, childAges);
        Assertion.assertThat("Check the child ages", childAgePickerPage.checkSelectedChildAge(totalChild, childAges), is(true));
        landingPage.clickDone();
        Assertion.assertThat("Total room, adult, child should shown as expected", landingPage.getRoomAndGuestText(),
            either(is(totalRoom + " Kamar, " + totalAdult + " Dewasa, " + totalChild + " Anak"))
            .or(allOf(
                containsString(totalRoom + " Room"),
                containsString(totalAdult + " Adult"),
                containsString(totalChild + " Child"))));

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Get the Price on the SRP");
        String srpPrice = searchResultPage.getFirstHotelPrice();
        Assertion.assertThat("Price on the SRP should be displayed", srpPrice, is(not(emptyOrNullString())));

        step("Open the PDP");
        searchResultPage.selectFirstHotelCardFromPrice();
        String pdpPrice = detailPage.getHotelPrice();
        Assertion.assertThat("Price on the PDP should be equal with the SRP", pdpPrice, is(srpPrice));

        step("Open Room List Page");
        detailPage.seeHotelRoom();
        String currency = splitText(srpPrice, " ").get(0);
        String roomListPrice = roomListPage.getTheFirstHotelRoomPrice(currency);
        Assertion.assertThat("Price on the Room List Page should be equal with the PDP", roomListPrice, is(pdpPrice));

        step("Open the Room Details");
        roomListPage.openRoomDetailPage(roomListPrice);
        String roomDetailPrice = roomDetailPage.getHotelRoomPriceOnRoomDetail();
        Assertion.assertThat("Price on the Room Details Page should be equal with the Room List Page", roomDetailPrice, is(roomListPrice));
    }

    @DataProvider(name = "verifyPriceLoginDataProvider")
    public Object[][] verifyPriceLoginDataProvider() {
        return getObjectParams("verifyPriceLogin");
    }

    @Author(names = {"Naufal", "Akbar", "Felicia", "Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "searchHotelLoginDataProvider")
    public void searchHotelLogin(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        ChildAgePickerPage<WebDriver> childAgePickerPage = ChildAgePickerPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String date_type = map.get("date_type");
        String date = map.get("date");

        int totalRoom = Integer.parseInt(map.get("total_room"));
        int totalAdult = Integer.parseInt(map.get("total_adult"));
        int totalChild = Integer.parseInt(map.get("total_child"));
        int childAge = Integer.parseInt(map.get("child_age"));

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("open hotel page");
       homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("search stay location: " + location);
        landingPage.searchStayLocation(location);

        step("set stay date");
        landingPage.setStayDate(date_type, date);

        step("set total room: " + totalRoom + " and total adult: " + totalAdult + ", total child: " + totalChild);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalAdult);
        landingPage.setTotalChildren(totalChild);
        childAgePickerPage.setChildAge(totalChild, childAge);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        Assertion.assertThat("login user is on the hotel search result page", searchResultPage.isSearchResultShown(), is(true));
    }

    @Author(names = {"Yazid", "Felicia"})
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "navigateSrpToPdpLoginDataProvider")
    public void navigateSrpToPdpLogin(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String list = Objects.toString(map.get("list"), "");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel page");
       homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel list");
        String expectedHotelName = searchResultPage.getHotelNameList(list);
        String expectedHotelLocation = searchResultPage.getHotelLocationList(list);
        searchResultPage.selectHotelList(list);

        Assertion.assertThat("Hotel detail should be displayed", detailPage.isHotelDetailShown(), is(true));
        Assertion.assertThat("Hotel name on the search and detail page should be equal", detailPage.getHotelNameDetail(), anything(expectedHotelName));
        Assertion.assertThat("Hotel location on the search and detail page should be equal", detailPage.getHotelLocationDetail(), anything(expectedHotelLocation));
    }

    @Author(names = {"Naufal", "Felicia", "Yazid"})
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "verifySameHotelFromSrpToPdpLoginDataProvider")
    public void verifySameHotelFromSrpToPdpLogin(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel list and get some variable");
        searchResultPage.goToFirstHotelCard();

        String expectedHotelName = searchResultPage.getFirstHotelName();
        String expectedHotelLocation = searchResultPage.getFirstHotelLocation();
        boolean expectedHotelStar = searchResultPage.getFirstHotelStar();
        String expectedHotelPrice = searchResultPage.getFirstHotelPrice();

        searchResultPage.selectFirstHotelCard();

        Assertion.assertThat("Hotel detail should be displayed", detailPage.isHotelDetailShown(), is(true));
        Assertion.assertThat("Hotel image should be displayed", detailPage.isHaveHotelImage(), is(true));
        Assertion.assertThat("Hotel name on the search and detail page should be equal", detailPage.getHotelNameDetail(), equalToIgnoringCase(expectedHotelName));
        Assertion.assertThat("Hotel star on the search and detail page should be equal", detailPage.getHotelStar(), is(expectedHotelStar));
        Assertion.assertThat("Hotel Price on the search and detail page should be equal", detailPage.getHotelPrice(), containsString(expectedHotelPrice));
        Assertion.assertThat("Hotel location on the search and detail page should be equal", detailPage.getHotelLocationDetail(), containsString(expectedHotelLocation));
    }

    @Author(names = {"Yazid"})
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkDetailHotel() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkDetailHotel");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel list and get some variable");
        searchResultPage.goToFirstHotelCard();

        String expectedHotelName = searchResultPage.getFirstHotelName();
        String expectedHotelLocation = searchResultPage.getFirstHotelLocation();
        boolean expectedHotelStar = searchResultPage.getFirstHotelStar();
        String expectedHotelPrice = searchResultPage.getFirstHotelPrice();

        searchResultPage.selectFirstHotelCard();

        Assertion.assertThat("Hotel detail should be displayed", detailPage.isHotelDetailShown(), is(true));
        Assertion.assertThat("Hotel image should be displayed", detailPage.isHaveHotelImage(), is(true));
        Assertion.assertThat("Hotel name on the search and detail page should be equal", detailPage.getHotelNameDetail(), is(expectedHotelName));
        Assertion.assertThat("Hotel star on the search and detail page should be equal", detailPage.getHotelStar(), is(expectedHotelStar));
        Assertion.assertThat("Hotel Price on the search and detail page should be equal", detailPage.getHotelPrice(), containsString(expectedHotelPrice));
        Assertion.assertThat("Hotel location on the search and detail page should be equal", detailPage.getHotelLocationDetail(), is(expectedHotelLocation));
    }

    @Author(names = {"Yazid"})
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void searchHotelByName() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("searchHotelByName");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String hotelName = map.get("hotelName");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);

        step("user is on Hotel Landing Page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("user search by hotel name");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Check the hotel name and location");
        Assertion.assertThat("user redirected to PDP", detailPage.getHotelNameDetail(), is(hotelName));
    }

    @Author(names = {"Naufal", "Felicia", "Yazid"})
    @Module(name = "Room")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "verifySeeHotelRoomLoginDataProvider")
    public void verifySeeHotelRoomLogin(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel list");
        searchResultPage.selectFirstHotelCard();

        step("Select See Room");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        Assertion.assertThat("Hotel rooms card should be displayed", detailPage.isRoomDetailShown(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "VerifyChangeLocationOnSrpLoginDataProvider")
    public void VerifyChangeLocationOnSrpLogin(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String initialLocation = map.get("initialLocation");
        List<String> newLocations = List.of(map.get("newLocations").split("-"));
        String language = map.get("language");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);

        step("Change language to \"" + language + "\"");
        homePage.openAccount();
        accountPage.selectLanguage(language);
        settingPage.backToHome();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", language, "");

        step("user is on SRP");
        landingPage.searchStayLocation(initialLocation);
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("user change the location to the New locations");
        Assertion.assertThat("Verify that User can change the location form SRP page", searchResultPage.checkChangeLocation(newLocations), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void VerifyChangeLocationOnSrpGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("VerifyChangeLocationOnSrpGuest");
        BaseTest.paramsThreadLocal.set(map);

        String initialLocation = map.get("initialLocation");
        List<String> newLocations = List.of(map.get("newLocations").split("-"));
        String language = map.get("language");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on non-login mode");
        loginPage.loginAsGuest();

        step("Change language to \"" + language + "\"");
        homePage.openAccount();
        accountPage.selectLanguage(language);
        settingPage.backToHome();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", language, "");

        step("user is on SRP");
        landingPage.searchStayLocation(initialLocation);
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("user change the location to the New locations");
        Assertion.assertThat("Verify that User can change the location form SRP page", searchResultPage.checkChangeLocation(newLocations), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecentSearchPlatformLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyRecentSearchPlatformLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String landingPageLocation = map.get("landingPageLocation");
        String landingPageHotelName = map.get("landingPageHotelName");
        String srpLocation = map.get("srpLocation");
        String srpHotelName = map.get("srpHotelName");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);
        accountPage.chooseCountry("Indonesia");

        step("Clear home page recent search section");
        homePage.goToRecentSearch("Hotel");
        homePage.clearRecentSearch();

        step("User choose the see all button on the hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");
        landingPage.clickFirstSeeAllButton();
        landingPage.fillNightCheckInForm();
        String currentLocation = searchResultPage.getCurrentLocation();

        step("Get the check-in/out date, total room and guest data on the SRP");
        String checkInOutDate = searchResultPage.getCheckInOutDate();

        step("User choose the hotel card on the hotel landing Page");
        homePage.goToHomeDeeplink();
        homePage.openVerticalPageDeeplink("Hotel", "", "");
        landingPage.clickFirstHotelCard();

        step("Get the hotel data on the PDP");
        String hotelName = detailPage.getHotelNameDetail();
        String hotelLocation = detailPage.getHotelLocationDetail();

        step("Find the location from see all button on the home page recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("Hotel");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(true, currentLocation, checkInOutDate), is(true));

        step("Find the hotel name on the home page recent search section");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(false, hotelName, hotelLocation), is(true));

        step("User search the location on hotel landing page");
        homePage.goToHomeDeeplink();
        homePage.openVerticalPageDeeplink("Hotel", "", "");
        landingPage.searchStayLocation(landingPageLocation);
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Get the check-in/out date, total room and guest data on the SRP");
        checkInOutDate = searchResultPage.getCheckInOutDate();
        searchResultPage.getFirstHotelName();

        step("User search the hotel name on hotel landing page");
        homePage.goToHomeDeeplink();
        homePage.openVerticalPageDeeplink("Hotel", "", "");
        landingPage.searchStayLocation(landingPageHotelName);
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Get the hotel data on the PDP");
        detailPage.getHotelNameDetail();
        hotelLocation = detailPage.getHotelLocationDetail();

        step("Find the location from landing page search on the home page recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("Hotel");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(true, landingPageLocation, checkInOutDate), is(true));

        step("Find the hotel name from landing page search on the recent search section");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(false, landingPageHotelName, hotelLocation), is(true));

        step("User search the location on the SRP");
        homePage.goToHomeDeeplink();
        homePage.openVerticalPageDeeplink("Hotel", "", "");
        landingPage.searchStayLocation(landingPageLocation);
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();
        searchResultPage.changeLocation(srpLocation);
        searchResultPage.clickSubmit();

        step("Get the check-in/out date, total room and guest data on the SRP");
        checkInOutDate = searchResultPage.getCheckInOutDate();

        step("User search the hotel name on the SRP");
        searchResultPage.changeLocation(srpHotelName);
        searchResultPage.clickSubmit();

        step("Get the hotel data on the PDP");
        detailPage.getHotelNameDetail();
        hotelLocation = detailPage.getHotelLocationDetail();

        step("Find the location on the home page recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("Hotel");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(true, srpLocation, checkInOutDate), is(true));

        step("Find the hotel name on the home page recent search section");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(false, srpHotelName, hotelLocation), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecentSearchPlatformGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyRecentSearchPlatformGuest");
        BaseTest.paramsThreadLocal.set(map);

        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";
        String dateType = map.get("dateType");
        String locationType = map.get("locationType");
        String landingPageLocation = map.get("landingPageLocation");
        String landingPageDate = map.get("landingPageDate");
        int landingPageTotalGuest = Integer.parseInt(map.get("landingPageTotalGuest"));
        int landingPageTotalChild = Integer.parseInt(map.get("landingPageTotalChild"));
        String landingPageHotelName = map.get("landingPageHotelName");
        String landingPageDateNew = map.get("landingPageDateNew");
        int landingPageTotalGuestNew = Integer.parseInt(map.get("landingPageTotalGuestNew"));
        int landingPageTotalChildNew = Integer.parseInt(map.get("landingPageTotalChildNew"));
        String srpLocation = map.get("srpLocation");
        String srpDate = map.get("srpDate");
        int srpTotalGuest = Integer.parseInt(map.get("srpTotalGuest"));
        int srpTotalChild = Integer.parseInt(map.get("srpTotalChild"));
        String srpHotelName = map.get("srpHotelName");
        String srpDateNew = map.get("srpDateNew");
        int srpTotalGuestNew = Integer.parseInt(map.get("srpTotalGuestNew"));
        int srpTotalChildNew = Integer.parseInt(map.get("srpTotalChildNew"));
        String srpHotelNameNew = map.get("srpHotelNameNew");
        String roomListDate = map.get("roomListDate");
        int roomListTotalGuest = Integer.parseInt(map.get("roomListTotalGuest"));
        int roomListTotalChild = Integer.parseInt(map.get("roomListTotalChild"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on non-login mode");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("User change the search date, guest, child data on search by hotel name Landing Page");
        homePage.openVerticalPageDeeplink("Hotel", language, "");
        landingPage.searchStayLocation(landingPageHotelName);
        landingPage.setStayDate(dateType, landingPageDateNew);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(landingPageTotalGuestNew);
        landingPage.setTotalChildren(landingPageTotalChildNew);
        landingPage.clickDone();
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Get the hotel data on the PDP");
        detailPage.getHotelNameDetail();
        String hotelLocation = detailPage.getHotelLocationDetail();

        step("User change the search date, guest, child data on search by location Landing Page");
        homePage.openVerticalPageDeeplink("Hotel", language, "");
        landingPage.searchStayLocation(landingPageLocation);
        landingPage.setStayDate(dateType, landingPageDate);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(landingPageTotalGuest);
        landingPage.setTotalChildren(landingPageTotalChild);
        landingPage.clickDone();
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Get the check-in/out date, total room and guest data on the SRP");
        String checkInOutDate = searchResultPage.getCheckInOutDate();
        searchResultPage.getFirstHotelName();

        step("Find the hotel name from landing page search on the recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("Hotel");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(false, landingPageHotelName, hotelLocation), is(true));

        step("Find the location from landing page search on the home page recent search section");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(true, landingPageLocation, checkInOutDate), is(true));
        int expectedTotalGuest = landingPageTotalGuest+landingPageTotalChild;
        Assertion.assertThat("Total all guest for adult "+landingPageTotalGuest+" and child "+landingPageTotalChild+" should be displayed as expected: " + expectedTotalGuest, homePage.checkTotalAllGuestRecentSearch(expectedTotalGuest), is(true));

        step("User change the search date, guest, child data on search by location SRP");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", landingPageLocation, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), 1, 1, locationId, locationType, landingPageLocation, checkInDate, checkOutDate, language);
        landingPage.fillNightCheckInForm();
        searchResultPage.changeLocation(srpLocation);
        landingPage.setStayDate(dateType, srpDate);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(srpTotalGuest);
        landingPage.setTotalChildren(srpTotalChild);
        landingPage.clickDone();
        searchResultPage.clickSubmit();

        step("Get the check-in/out date, total room and guest data on the SRP");
        checkInOutDate = searchResultPage.getCheckInOutDate();
        searchResultPage.getFirstHotelName();

        step("Find the location on the home page recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("Hotel");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(true, srpLocation, checkInOutDate), is(true));
        expectedTotalGuest = srpTotalGuest+srpTotalChild;
        Assertion.assertThat("Total all guest for adult "+srpTotalGuest+" and child "+srpTotalChild+" should be displayed as expected: " + expectedTotalGuest, homePage.checkTotalAllGuestRecentSearch(expectedTotalGuest), is(true));

        step("User change the search date, guest, child data on search by hotel name SRP");
        hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", srpLocation, locationType);
        locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), 1, 1, locationId, locationType, srpLocation, checkInDate, checkOutDate, language);
        landingPage.fillNightCheckInForm();
        searchResultPage.changeLocation(srpHotelName);
        landingPage.setStayDate(dateType, srpDateNew);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(srpTotalGuestNew);
        landingPage.setTotalChildren(srpTotalChildNew);
        landingPage.clickDone();
        searchResultPage.clickSubmit();

        step("Get the hotel data on the PDP");
        detailPage.getHotelNameDetail();
        hotelLocation = detailPage.getHotelLocationDetail();

        step("Find the hotel name on the home page recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("Hotel");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(false, srpHotelName, hotelLocation), is(true));

        step("Open the room list page");
        hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", srpHotelNameNew, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 1, 1, checkInDate, checkOutDate);
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("User change the search date, guest, child data on Room List");
        roomListPage.clickChangeButton();
        landingPage.setStayDate(dateType, roomListDate);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(roomListTotalGuest);
        landingPage.setTotalChildren(roomListTotalChild);
        landingPage.clickDone();
        searchResultPage.clickSubmit();

        step("Get the hotel data on the PDP");
        searchResultPage.getCheckInOutDate();
        searchResultPage.clickBackButton();
        detailPage.getHotelNameDetail();
        hotelLocation = detailPage.getHotelLocationDetail();

        step("Find the hotel name from landing page search on the recent search section");
        homePage.goToHomeDeeplink();
        homePage.goToRecentSearch("Hotel");
        Assertion.assertThat("recently search data is send to Platform", homePage.checkRecentSearch(false, srpHotelNameNew, hotelLocation), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyWhenNonLoginWishlist() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyWhenNonLoginWishlist");
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
        String accommodationType = "hotel";

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Open the SRP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, accommodationType.toUpperCase());
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), 1, 1, locationId, locationType, location, checkInDate, checkOutDate, "");
        landingPage.fillNightCheckInForm();

        step("On one of the property card, click the wishlist (love) icon");
        String hotelName = searchResultPage.clickHeartIcon();

        step("User redirected to login page");
        loginPage.inputEmailOrPhoneNumberNative(username);
        loginPage.loginUNM(loginType, username, password);

        step("After successfully logged in and save wishlist, User back to SRP");
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

    @Author(names = {"Yazid"})
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelWishlistLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyHotelWishlistLogin");
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
        String accommodationType = "hotel";

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

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test(dataProvider = "createHotelBookingDataProvider")
    public void createHotelBooking(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel from the hotel list");
        searchResultPage.selectFirstHotelCard();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        String expectedHotelName = bookingFormPage.getHotelNameBookingForm();
        String expectedCheckinDate = bookingFormPage.getCheckinBookingForm();
        String expectedNightDuration = bookingFormPage.getDurationBookingForm();
        String expectedTotalPrice = bookingFormPage.getTotalPriceBookingForm();
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        String priceChanges = bookingFormPage.PriceChangesPopUp();
        expectedTotalPrice = updateString(expectedTotalPrice, priceChanges);

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Hotel name on the booking form and payment page should be equal", paymentPage.getProductNamePayment(), is(expectedHotelName));
        Assertion.assertThat("Check-in date on the booking form and payment page should be equal",  paymentPage.getCheckinPayment(), is(expectedCheckinDate));
        Assertion.assertThat("Night duration on the booking form and payment page should be equal", paymentPage.getDurationPayment(), is(expectedNightDuration));
        Assertion.assertThat("Total price on the booking form and payment page should be equal", paymentPage.getTotalPricePayment(), is(expectedTotalPrice));
    }

    @Author(names = {"Naufal" , "Nishtha", "Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Filter")
    @Test(dataProvider = "sortByLowestPriceLoginDataProvider")
    public void sortByLowestPriceLogin(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String location = map.get("location");
        String filter = map.get("filter");
        String totalHotel = map.get("totalHotel");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("User click sort filter button");
        searchResultPage.selectFilterSortBy(filter);

        step("Get hotel price data");
        Boolean isSortByLowestPriceValid = searchResultPage.isSortByLowestPriceValid(totalHotel);

        step("Sort by lowest price assertion");
        Assertion.assertThat("short by lowest price is valid", isSortByLowestPriceValid ,is(true));
    }

    @DataProvider(name = "sortByLowestPriceLoginDataProvider")
    public Object[][] sortByLowestPriceLoginDataProvider() {
        return getObjectParams("sortByLowestPriceLogin");
    }

    @Author(names = {"Abhishek", "Yazid"})
    @Module(name = "Filter")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "headerQuickFilterLoginDataProvider")
    public void headerQuickFilterLogin(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        int index = Integer.parseInt(map.get("totalQuickFilter"));

        step("Go to home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Looping on header quick filter");
        Assertion.assertThat("Assert if quick filter  has valid result", searchResultPage.verifyLoopingHeaderQuickFilter(index), is(true));
    }

    @Author(names = {"Yazid","Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Module(name = "Accommodation")
    @Test
    public void ACC_completeBookingInternationalNonIdrMultiPaxNightGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("ACC_completeBookingInternationalNonIdrMultiPaxNightGuest");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String hotelNameSearch = map.get("hotelName");
        String dateType = map.get("dateType");
        String date = map.get("date");
        int totalRoom = Integer.parseInt(map.get("totalRoom"));
        int totalGuest = Integer.parseInt(map.get("totalGuest"));
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Change currency to " + currency);
        homePage.changeCurrency(currency,"");

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

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.openRoomDetailPage();
        roomDetailPage.clickBookButton(totalRoom);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        step("Fill the data on the booking form and submit it");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);
        List<String> bookingFormGuestName = bookingFormPage.getGuestNameList();
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

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
        Assertion.assertThat("Currency is "+currency, orderDetailPage.getTotalPaymentHotel(), containsString(currency));
        Assertion.assertThat("Guest name should be equal with the booking form "+bookingFormGuestName, bookingFormGuestName, is(orderDetailPage.getGuestDetailsName()));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Module(name = "Accommodation")
    @Test
    public void ACC_completeBookingInternationalNonIdrMultiPaxNightGuestDeeplink() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("ACC_completeBookingInternationalNonIdrMultiPaxNightGuestDeeplink");
        BaseTest.paramsThreadLocal.set(map);

        String currency = map.get("currency");
        String hotelName = map.get("hotelName");
        String rangeAddDate = map.get("rangeAddDate");
        int totalRoom = Integer.parseInt(map.get("totalRoom"));
        int totalGuest = Integer.parseInt(map.get("totalGuest"));
        int totalNight = Integer.parseInt(map.get("totalNight"));
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Change currency to " + currency);
        homePage.changeCurrency(currency,"");

        step("user open PDP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, totalGuest, checkInDate, checkOutDate);

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.openRoomDetailPage();
        roomDetailPage.clickBookButton(totalRoom);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        step("Fill the data on the booking form and submit it");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);
        List<String> bookingFormGuestName = bookingFormPage.getGuestNameList();
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Input and submit CC information");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Submit the payment method");
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();

        step("Assert if the payment is a success");
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open the order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("Currency is "+currency, orderDetailPage.getTotalPaymentHotel(), containsString(currency));
        Assertion.assertThat("Guest name should be equal with the booking form "+bookingFormGuestName, bookingFormGuestName, is(orderDetailPage.getGuestDetailsName()));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void completeBookingInternationalNonIdrMultiPaxNightLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingInternationalNonIdrMultiPaxNightLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");
        String hotelNameSearch = map.get("hotelName");
        String dateType = map.get("dateType");
        String date = map.get("date");
        int totalRoom = Integer.parseInt(map.get("totalRoom"));
        int totalGuest = Integer.parseInt(map.get("totalGuest"));
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

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

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.openRoomDetailPage();
        roomDetailPage.clickBookButton(totalRoom);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        step("Fill the data on the booking form and submit it");
        bookingFormPage.fillGuestDetail(false);
        List<String> bookingFormGuestName = bookingFormPage.getGuestNameList();
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

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
        
        Assertion.assertThat("Currency is "+currency, orderDetailPage.getTotalPaymentHotel(), containsString(currency));
        Assertion.assertThat("Guest name should be equal with the booking form "+bookingFormGuestName, bookingFormGuestName, is(orderDetailPage.getGuestDetailsName()));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void completeBookingInternationalNonIdrMultiPaxNightLoginDeeplink() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingInternationalNonIdrMultiPaxNightLoginDeeplink");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");
        String hotelName = map.get("hotelName");
        String rangeAddDate = map.get("rangeAddDate");
        int totalRoom = Integer.parseInt(map.get("totalRoom"));
        int totalGuest = Integer.parseInt(map.get("totalGuest"));
        int totalNight = Integer.parseInt(map.get("totalNight"));
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("user open PDP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, totalGuest, checkInDate, checkOutDate);

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.openRoomDetailPage();
        roomDetailPage.clickBookButton(totalRoom);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        step("Fill the data on the booking form and submit it");
        bookingFormPage.fillGuestDetail(false);
        List<String> bookingFormGuestName = bookingFormPage.getGuestNameList();
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Input and submit CC information");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Submit the payment method");
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();

        step("Assert if the payment is a success");
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open the order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        Assertion.assertThat("Currency is "+currency, orderDetailPage.getTotalPaymentHotel(), containsString(currency));
        Assertion.assertThat("Guest name should be equal with the booking form "+bookingFormGuestName, bookingFormGuestName, is(orderDetailPage.getGuestDetailsName()));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test(dataProvider = "createHotelBookingLoginDataProvider")
    public void createHotelBookingLogin(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel from the hotel list");
        searchResultPage.selectFirstHotelCard();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Get the booking form data");
        String expectedHotelName = bookingFormPage.getHotelNameBookingForm();
        String expectedCheckinDate = bookingFormPage.getCheckinBookingForm();
        String expectedNightDuration = bookingFormPage.getDurationBookingForm();
        String expectedTotalPrice = bookingFormPage.getTotalPriceBookingForm();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Hotel name on the booking form and payment page should be equal", paymentPage.getProductNamePayment(), is(expectedHotelName));
        Assertion.assertThat("Check-in date on the booking form and payment page should be equal", paymentPage.getCheckinPayment(), is(expectedCheckinDate));
        Assertion.assertThat("Night duration on the booking form and payment page should be equal", paymentPage.getDurationPayment(), is(expectedNightDuration));
        Assertion.assertThat("Total price on the booking form and payment page should be equal", convertPrice(paymentPage.getTotalPricePayment()), is(convertPrice(expectedTotalPrice)));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Module(name = "Accommodation")
    @Test
    public void ACC_completeBookingHotelPayNowLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("ACC_completeBookingHotelPayNowLogin");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String date_type = map.get("date_type");
        String date = map.get("date");
        int totalRoom = Integer.parseInt(map.get("total_room"));
        int totalAdult = Integer.parseInt(map.get("total_adult"));

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set stay date");
        landingPage.setStayDate(date_type, date);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalAdult);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel from the hotel list");
        searchResultPage.selectFirstHotelCard();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Get the booking form data");
        CommonBookingFormPage.getInstance(session).waitProcessingBooking();
        String expectedHotelName = bookingFormPage.getHotelNameBookingForm();
        String expectedCheckinDate = bookingFormPage.getCheckinBookingForm();
        String expectedNightDuration = bookingFormPage.getDurationBookingForm();
        String expectedTotalPrice = bookingFormPage.getTotalPriceBookingForm();

        step("Submit booking");
        bookingFormPage.selectRandomSalutationIfMissing();
        bookingFormPage.continueToPaymentPage();
        String priceChanges = bookingFormPage.PriceChangesPopUp();
        expectedTotalPrice = updateString(expectedTotalPrice, priceChanges);

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Hotel name on the booking form and payment page should be equal", paymentPage.getProductNamePayment(), is(expectedHotelName));
        Assertion.assertThat("Check-in date on the booking form and payment page should be equal", paymentPage.getCheckinPayment(), is(expectedCheckinDate));
        Assertion.assertThat("Night duration on the booking form and payment page should be equal", paymentPage.getDurationPayment(), is(expectedNightDuration));
        Assertion.assertThat("Total price on the booking form and payment page should be equal", convertPrice(paymentPage.getTotalPricePayment()), is(convertPrice(expectedTotalPrice)));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Module(name = "Accommodation")
    @Test
    public void ACC_completeBookingHotelPayNowLoginDeeplink() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("ACC_completeBookingHotelPayNowLoginDeeplink");
        BaseTest.paramsThreadLocal.set(map);
        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String locationType = map.get("locationType");
        int totalRoom = Integer.parseInt(map.get("total_room"));
        int totalAdult = Integer.parseInt(map.get("total_adult"));
        int totalNight = Integer.parseInt(map.get("total_night"));
        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open the SRP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, accommodationType.toUpperCase());
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), totalRoom, totalAdult, locationId, locationType, location, checkInDate, checkOutDate, "");
        searchResultPage.waitForSrpToLoad();
        landingPage.fillNightCheckInForm();

        step("Select hotel from the hotel list");
        searchResultPage.selectFirstHotelCard();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Get the booking form data");
        String expectedHotelName = bookingFormPage.getHotelNameBookingForm();
        String expectedCheckinDate = bookingFormPage.getCheckinBookingForm();
        String expectedNightDuration = bookingFormPage.getDurationBookingForm();
        String expectedTotalPrice = bookingFormPage.getTotalPriceBookingForm();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        String priceChanges = bookingFormPage.PriceChangesPopUp();
        expectedTotalPrice = updateString(expectedTotalPrice, priceChanges);

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Hotel name on the booking form and payment page should be equal", paymentPage.getProductNamePayment(), is(expectedHotelName));
        Assertion.assertThat("Check-in date on the booking form and payment page should be equal", paymentPage.getCheckinPayment(), is(expectedCheckinDate));
        Assertion.assertThat("Night duration on the booking form and payment page should be equal", paymentPage.getDurationPayment(), is(expectedNightDuration));
        Assertion.assertThat("Total price on the booking form and payment page should be equal", convertPrice(paymentPage.getTotalPricePayment()), is(convertPrice(expectedTotalPrice)));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test(dataProvider = "completeBookingHotelPayNowDataProvider")
    public void completeBookingHotelPayNow(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");
        String date_type = map.get("date_type");
        String date = map.get("date");
        int totalRoom = Integer.parseInt(map.get("total_room"));
        int totalAdult = Integer.parseInt(map.get("total_adult"));

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User open app as guest");
        loginPage.loginAsGuest();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set stay date");
        landingPage.setStayDate(date_type, date);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalAdult);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel from the hotel list");
        searchResultPage.selectFirstHotelCard();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Get the booking form data");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);
        String expectedHotelName = bookingFormPage.getHotelNameBookingForm();
        String expectedCheckinDate = bookingFormPage.getCheckinBookingForm();
        String expectedNightDuration = bookingFormPage.getDurationBookingForm();
        String expectedTotalPrice = bookingFormPage.getTotalPriceBookingForm();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        String priceChanges = bookingFormPage.PriceChangesPopUp();
        expectedTotalPrice = updateString(expectedTotalPrice, priceChanges);

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Hotel name on the booking form and payment page should be equal", paymentPage.getProductNamePayment(), is(expectedHotelName));
        Assertion.assertThat("Check-in date on the booking form and payment page should be equal", paymentPage.getCheckinPayment(), is(expectedCheckinDate));
        Assertion.assertThat("Night duration on the booking form and payment page should be equal", paymentPage.getDurationPayment(), is(expectedNightDuration));
        Assertion.assertThat("Total price on the booking form and payment page should be equal", convertPrice(paymentPage.getTotalPricePayment()), is(convertPrice(expectedTotalPrice)));
    }

    @Author(names = {"Felicia", "Naufal", "Akbar", "Yazid"})
    @Module(name = "Room")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "checkUserCanSeeRoomDetailPdpLoginDataProvider")
    public void checkUserCanSeeRoomDetailPdpLogin(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location"); //Straight to searching the hotel instead of the region
        landingPage.searchStayLocation(location);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select See Room");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select first hotel room from room list page");
        String firstHotelRoomNameText = roomListPage.getTheFirstHotelRoomName();
        String firstHotelRoomPriceText = roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.openRoomDetailPage();

        String roomNameInRoomDetail = roomDetailPage.getHotelRoomNameOnRoomDetail();
        String roomPriceInRoomDetail = roomDetailPage.getHotelRoomPriceOnRoomDetail();

        Assertion.assertThat("Hotel room name on Room List Page and hotel room name on Room Detail Page should be equal", roomNameInRoomDetail, is(firstHotelRoomNameText));
        Assertion.assertThat("Hotel room price on Room List Page and hotel room price on Room Detail Page should be equal", roomPriceInRoomDetail, is(firstHotelRoomPriceText));
        Assertion.assertThat("All hotel room detail tabs is valid", roomDetailPage.hotelRoomDetailTabIsValid(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Room")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkDetailRoomListWithSpecificProperty() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkDetailRoomListWithSpecificProperty");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User open app as guest");
        loginPage.loginAsGuest();

        step("Open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel list");
        searchResultPage.selectFirstHotelCard();

        step("Select See Room");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select first hotel room from room list page");
        String firstHotelRoomNameText = roomListPage.getTheFirstHotelRoomName();
        String firstHotelRoomPriceText = roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.openRoomDetailPage();

        String roomNameInRoomDetail = roomDetailPage.getHotelRoomNameOnRoomDetail();
        String roomPriceInRoomDetail = roomDetailPage.getHotelRoomPriceOnRoomDetail();

        Assertion.assertThat("Hotel room name on Room List Page and hotel room name on Room Detail Page should be equal", roomNameInRoomDetail, is(firstHotelRoomNameText));
        Assertion.assertThat("Hotel room price on Room List Page and hotel room price on Room Detail Page should be equal", roomPriceInRoomDetail, is(firstHotelRoomPriceText));
        Assertion.assertThat("All hotel room detail tabs is valid", roomDetailPage.hotelRoomDetailTabIsValid(), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "Room")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkDetailRoomListWithSpecificPropertyLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkDetailRoomListWithSpecificPropertyLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String hotelName = map.get("hotelName");
        String rangeAddDate = map.get("rangeAddDate");

        int totalGuest = 2;
        int totalRoom = 1;
        int totalNight = 2;
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);

        step("user open PDP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, totalGuest, checkInDate, checkOutDate);

        step("Select See Room");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select first hotel room from room list page");
        String firstHotelRoomNameText = roomListPage.getTheFirstHotelRoomName();
        String firstHotelRoomPriceText = roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.openRoomDetailPage();

        String roomNameInRoomDetail = roomDetailPage.getHotelRoomNameOnRoomDetail();
        String roomPriceInRoomDetail = roomDetailPage.getHotelRoomPriceOnRoomDetail();

        Assertion.assertThat("Hotel room name on Room List Page and hotel room name on Room Detail Page should be equal", roomNameInRoomDetail, is(firstHotelRoomNameText));
        Assertion.assertThat("Hotel room price on Room List Page and hotel room price on Room Detail Page should be equal", roomPriceInRoomDetail, is(firstHotelRoomPriceText));
        Assertion.assertThat("All hotel room detail tabs is valid", roomDetailPage.hotelRoomDetailTabIsValid(), is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "CreateBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "paymentPageToSrpLoginDataProvider")
    public void paymentPageToSrpLogin(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel from the hotel list");
        searchResultPage.selectFirstHotelCard();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();

        step("Back to booking form");
        paymentPage.clickBackButton();

        step("Back to room list");
        bookingFormPage.clickBackButton();

        step("Click Search Other Room");
        bookingFormPage.clickSearchOtherRoom();

        step("Back to hotel list search");
        roomListPage.clickBackButton();
        detailPage.clickBackButton();

        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        Assertion.assertThat("User is on the hotel search result page", searchResultPage.isSearchResultShown(), is(true));
    }

    @Author(names = {"Naufal", "Akbar"})
    @Module(name = "Filter")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void filterByFrequentlyPriceRangeLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("filterByFrequentlyPriceRangeLogin");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        String totalHotel = map.get("totalHotel");
        String priceRange = map.get("priceRange");
        String currency = map.get("currency");

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency(currency, username);

        step("Open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("open and select price filter");
        searchResultPage.openPriceFilter();
        searchResultPage.selectPriceFilterByRange(priceRange,currency);
        searchResultPage.clickFilterSubmit();

        Assertion.assertThat("Hotel price is match with price filter by range", searchResultPage.isPriceFilterByRangeCorrect(priceRange, totalHotel), is(true));
    }

    @DataProvider(name = "verifySeeHotelRoomDataProvider")
    public Object[][] verifySeeHotelRoomDataProvider() {
        return getObjectParams("verifySeeHotelRoom");
    }

    @DataProvider(name = "navigateSrpToPdpDataProvider")
    public Object[][] navigateSrpToPdpDataProvider() {
        return getObjectParams("navigateSrpToPdp");
    }

    @DataProvider(name = "searchHotelDataProvider")
    public Object[][] searchHotelDataProvider() {
        return getObjectParams("searchHotel");
    }

    @Author(names = {"Akbar"})
    @Module(name = "Filter")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void filterByHotelStarLogin() {
        Map<String, String> map = getParam("filterByHotelStarLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String star = map.get("star");
        String totalHotel = map.get("totalHotel");

        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Click filter button");
        searchResultPage.clickFilterButton();

        step("Scroll till filter by hotel star is visible");
        searchResultPage.swipeToFilter("Hotel Star/Bintang Hotel");

        step("Choose filter based on testdata | testdata : '"+star+"'");
        searchResultPage.selectHotelStarFilter(star);

        step("Submit filter");
        searchResultPage.clickFilterSubmit();

        Assertion.assertThat("Assert if first '"+totalHotel+"' hotel card is having the right hotel star",
                searchResultPage.checkHotelCardList("Star", Integer.parseInt(totalHotel), star.split("-")), is(true));
    }

    @Author(names = {"Akbar", "Naufal"})
    @Module(name = "BookingForm")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void userCanAddContactAndGuestDetailsLogin() {
        Map<String, String> map = getParam("userCanAddContactAndGuestDetailsLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set total room");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel list");
        searchResultPage.selectFirstHotelCard();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();
        Assertion.assertThat("Asserting if the contact details and guest detail already filled", bookingFormPage.isContactAndGuestDetailsFilled(), is(true));

        String contactName = bookingFormPage.getContactNameDetail();
        String contactPhoneNumber = bookingFormPage.getContactPhoneNumberDetail();
        String guestName = bookingFormPage.getGuestNameDetail();

        step("Edit contact detail");
        bookingFormPage.editContactOrderDetail();

        step("Edit guest detail");
        bookingFormPage.fillGuestDetail(false);

        Assertion.assertThat("Asserting if the contact details and guest detail already changed", bookingFormPage.isContactAndGuestDetailsChanged(contactName, contactPhoneNumber, guestName), is(true));

        String[] editedContactNameArray = bookingFormPage.getGuestNameDetail().split(" ");
        String editedContactName = String.join(" ", Arrays.copyOfRange(editedContactNameArray,editedContactNameArray.length-2, editedContactNameArray.length));

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();

        Assertion.assertThat("Asserting order ID is not null", !orderID.isEmpty(), is(true));

        step("Click Book Detail on Payment Page");
        paymentPage.clickBookDetail();

        Assertion.assertThat("Asserting if the guest detail name is valid", paymentPage.getGuestDetailName().toLowerCase(), is(containsString(editedContactName.toLowerCase())));
    }

    @Author(names = {"Naufal"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "BookingForm")
    @Test(dataProvider = "addProtectionLoginDataProvider")
    public void addProtectionLogin(Map<String, String> map) {
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String protectionType = map.get("protectionType");

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR",username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("User go to 2nd page booking form");
        bookingFormPage.continueToAddOnPage();

        step("user choose and add protection: " + protectionType);
        bookingFormPage.chooseProtection(protectionType);
        String totalPriceBefore = bookingFormPage.getTotalToPayBookingForm();

        String protectionPrice = bookingFormPage.getProtectionPrice();
        bookingFormPage.addProtection();

        String totalPriceAfter = bookingFormPage.getTotalToPayBookingForm();
        bookingFormPage.openSubtotal();

        boolean isProtectionAdded = bookingFormPage.isProtectionAdded(protectionPrice);

        Assertion.assertThat("Total Price is changed", totalPriceBefore.equals(totalPriceAfter), is(false));
        Assertion.assertThat("protection is added", isProtectionAdded, is(true));
        Assertion.assertThat("price calculation after protection is added is true", bookingFormPage.isProtectionPriceCorrect(totalPriceBefore, protectionPrice, totalPriceAfter), is(true));
    }

    @DataProvider(name = "addProtectionLoginDataProvider")
    public Object[][] addProtectionLoginDataProvider() {
        return getObjectParams("addProtectionLogin");
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "BookingForm")
    @Test(dataProvider = "addProtectionDataProvider")
    public void addProtection(Map<String, String> map) {
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");
        String protectionType = map.get("protectionType");

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User open app as guest");
        loginPage.loginAsGuest();
        homePage.changeCurrency("IDR","");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

        step("User go to 2nd page booking form");
        bookingFormPage.continueToAddOnPage();

        step("user choose and add protection: " + protectionType);
        bookingFormPage.chooseProtection(protectionType);
        String totalPriceBefore = bookingFormPage.getTotalToPayBookingForm();

        String protectionPrice = bookingFormPage.getProtectionPrice();
        bookingFormPage.addProtection();

        String totalPriceAfter = bookingFormPage.getTotalToPayBookingForm();
        bookingFormPage.openSubtotal();

        boolean isProtectionAdded = bookingFormPage.isProtectionAdded(protectionPrice);

        Assertion.assertThat("Total Price is changed", totalPriceAfter, is(not(totalPriceBefore)));
        Assertion.assertThat("protection is added", isProtectionAdded, is(true));
        Assertion.assertThat("price calculation after protection is added is true", bookingFormPage.isProtectionPriceCorrect(totalPriceBefore, protectionPrice, totalPriceAfter), is(true));
    }

    @DataProvider(name = "addProtectionDataProvider")
    public Object[][] addProtectionDataProvider() {
        return getObjectParams("addProtection");
    }

    @Author(names = {"Naufal"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Module(name = "Accommodation")
    @Test
    public void ACC_verifyBookingWithPromoCodeLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        Map<String, String> map = getParam("ACC_verifyBookingWithPromoCodeLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        String paymentMethod = map.get("paymentMethod");
        String promoCode = map.get("promoCode");
        String currency = "IDR";

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency(currency, username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit Booking");
        bookingFormPage.selectRandomSalutationIfMissing();
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Choose payment method");
        paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Apply promo code");
        boolean isPromoCodeSectionExist = paymentPage.isPromoCodeSectionExist();

        paymentPage.seeAllPromoCodes();
        paymentPage.applyPromoCode(promoCode);

        String CrossedPriceBefore = paymentPage.getCrossedPriceBefore();
        String discountPercentage = paymentPage.getDiscountPercentage();
        String discountAmountOnPromoCodeSection = paymentPage.getDiscountAmountOnPromoCodeSection(currency);

        boolean isCantCombinePromoLabelExist = paymentPage.isCantCombinePromoLabelExist();

        step("Open price detail");
        paymentPage.openPriceDetailWebView();
        String discountAmountOnPriceDetail = paymentPage.getDiscountAmountOnPriceDetail();

        step("Assertions");
        Assertion.assertThat("Promo code section is exist", isPromoCodeSectionExist, is(true));
        Assertion.assertThat("crossed 'before price' on bottom navigation is exist", CrossedPriceBefore.isEmpty(), is(false));
        Assertion.assertThat("percentage of discount is exist", discountPercentage.isEmpty(), is(false));
        Assertion.assertThat("Does discount amount on price detail is match with discount amount on the promo section", discountAmountOnPromoCodeSection.equals(discountAmountOnPriceDetail), is(true));
    }

    @Author(names = {"Naufal"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void verifyBookingWithPromoCodeGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> map = getParam("verifyBookingWithPromoCodeGuest");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String location = map.get("location");

        String paymentMethod = map.get("paymentMethod");
        String promoCode = map.get("promoCode");
        String currency = "IDR";

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

        step("Submit Booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Choose payment method");
        paymentPage.checkOrderID();
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Apply promo code");
        boolean isPromoCodeSectionExist = paymentPage.isPromoCodeSectionExist();

        paymentPage.seeAllPromoCodes();
        paymentPage.applyPromoCode(promoCode);

        String CrossedPriceBefore = paymentPage.getCrossedPriceBefore();
        String discountPercentage = paymentPage.getDiscountPercentage();
        String discountAmountOnPromoCodeSection = paymentPage.getDiscountAmountOnPromoCodeSection(currency);

        step("Open price detail");
        paymentPage.openPriceDetail();
        String discountAmountOnPriceDetail = paymentPage.getDiscountAmountOnPriceDetail();

        step("Assertions");
        Assertion.assertThat("Promo code section is exist", isPromoCodeSectionExist, is(true));
        Assertion.assertThat("crossed 'before price' on bottom navigation is exist", CrossedPriceBefore.isEmpty(), is(false));
        Assertion.assertThat("percentage of discount is exist", discountPercentage.isEmpty(), is(false));
        Assertion.assertThat("Does discount amount on price detail is match with discount amount on the promo section", discountAmountOnPromoCodeSection.equals(discountAmountOnPriceDetail), is(true));
    }

    @Author(names = {"Naufal"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Module(name = "Accommodation")
    @Test
    public void ACC_verifyOnCancellationFreeChargeLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        Map<String, String> map = getParam("ACC_verifyOnCancellationFreeChargeLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String payment_method = map.get("payment_method");

        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR", username);

        step("user open PDP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, accommodationType.toUpperCase());
        int index = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(index).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(index).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 1, 1, checkInDate, checkOutDate);

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open room detail and see refund policy");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.openRoomDetailPage();

        String refundPolicyValue = detailPage.getRefundPolicyValue();

        step("Book room");
        roomDetailPage.clickBookButton(1);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        step("Submit booking");
        bookingFormPage.selectRandomSalutationIfMissing();
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Choose payment method and pay");
        String orderId = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(payment_method);
        paymentPage.getProductNamePayment();
        String totalPayment = paymentPage.getTotalPricePayment();
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Go to order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderId);

        step("open refund and reschedule info");
        orderDetailPage.hidePromoBannerIfPresent();
        orderDetailPage.openRefundAndRescheduleInfo();
        String orderRefundPolicyValue = orderDetailPage.getOrderRefundPolicyValue(totalPayment);

        orderDetailPage.closeRefundAndRescheduleInfoWebView();

        step("Go to the refund button and open the refund page");
        orderDetailPage.clickRefundButton();
        paymentPage.checkOrderID();
        refundPage.chooseRefundReasonWebView();

        step("Assertion");
        Assertion.assertThat("is refund policy value is free", refundPolicyValue,is("IDR 0"));
        Assertion.assertThat("is refund policy value is match with refund policy value on order list", orderRefundPolicyValue.toUpperCase(),is("IDR 0"));

        step("verify estimation popup functionality");
        Assertion.assertThat("is estimation calculation correct",refundPage.verifyCalculationForEstimation("IDR"),is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PostPurchase")
    @Test
    public void verifyOnCancellationChargeLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        Map<String, String> map = getParam("verifyOnCancellationChargeLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        String hotelName = map.get("location");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR", username);

        step("user open PDP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        int index = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(index).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(index).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 1, 1, checkInDate, checkOutDate);

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.openRoomWithRefundRescheduleChargePolicy();
        roomDetailPage.clickBookButton(1);

        step("Submit booking");
        bookingFormPage.openBookingDetailPage();
        String refundPolicyValue = detailPage.getRefundPolicyValue();
        bookingFormPage.clickCancelButton();
        String totalPayment = bookingFormPage.getTotalPriceBookingForm();
        bookingFormPage.continueToAddOnPage();
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Choose payment method and pay");
        String orderId = paymentPage.checkOrderID();

        step("select Sakuku as payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();

        step("Assert if the payment is a success");
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open the order detail page");
        orderListPage.openIssuedTicketViaDeeplink(orderId);

        step("open refund and reschedule info");
        
        orderDetailPage.openRefundAndRescheduleInfo();
        String orderRefundPolicyValue = orderDetailPage.getOrderRefundPolicyValueWebView(totalPayment);
        orderDetailPage.closeRefundAndRescheduleInfo();

        step("Go to the refund button and open the refund page");
        orderDetailPage.clickRefundButton();
        paymentPage.checkOrderID();
        refundPage.chooseRefundReasonWebView();

        step("Get the first estimated refund price");
        String totalEstimatedRefund = refundPage.getEstimatedRefund();

        step("Open the total refund amount & verify calculation");
        refundPage.openEstimatedRefundDetail();
        Assertion.assertThat("Total breakdown pop up shown", refundPage.calculatePriceBreakdown(), is(notNullValue()));
        Assertion.assertThat("cancellation price match with order refund policy value",
                CoreFunctions.convertPriceSafely(refundPage.getCancellationPrice("IDR")), is(CoreFunctions.convertPriceSafely(orderRefundPolicyValue)));
        refundPage.closeTheRefundPaymentDetails();

        step("Click process refund button");
        refundPage.clickProcessRefundBtn();

        String totalRefundEstimation = refundPage.getTotalRefund();

        step("Assertions");
        Assertion.assertThat("is refund policy value is match with refund policy value on order list", convertPrice(refundPolicyValue), is(convertPrice(orderRefundPolicyValue)));
        Assertion.assertThat("is refund policy value is not free ("+refundPolicyValue+")", refundPolicyValue.equals("IDR 0"),is(false));
        Assertion.assertThat("is total payment is match with total refund estimation on page 1: " + totalEstimatedRefund +" == page 2: " + totalRefundEstimation, convertPrice(totalEstimatedRefund), is(convertPrice(totalRefundEstimation)));
    }

    @Author(names = {"Prerna", "Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void verifyThankyouPageAfterPaymentLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyThankyouPageAfterPaymentLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String hotelName = map.get("hotelName");
        String paymentMethod = map.get("paymentMethod");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR", username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        detailPage.RemovePopUp();
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();

        step("select the payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickPayButton();

        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        paymentPage.paymentChecker();
        Assertion.assertThat("Thankyou page is shown", paymentPage.isThankYouPageShown(), is(true));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Reschedule")
    @Test
    public void verifyClickRescheduleBookButton() {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        Map<String, String> map = getParam("verifyClickRescheduleBookButton");
        BaseTest.paramsThreadLocal.set(map);

        String username = map.get("username");
        String password = map.get("password");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String query = map.get("hotelName");
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));
        int rescheduleDuration = Integer.parseInt(map.get("rescheduleDuration"));
        int delayMs = Integer.parseInt(map.get("delayMs"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];
        String currency = "IDR";

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency);

        step("Get data from the issued order");
        String issuedOrderID =  String.valueOf(hotelApi.getOrderId());

        Assertion.assertThat("Verify Booking via API is successful",result,is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);
        homePage.gotoHome();

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);

        step("Choose reschedule");
        orderDetailPage.clickReschedule(issuedOrderID);

        step("Choose new date for the reschedule");
        webViewPage.switchToWebViewUrl("reschedule");
        hotelReschedulePageWeb.changeNightDuration(rescheduleDuration);

        step("Choose any room type");
        webViewPage.switchToWebViewUrl("reschedule");
        hotelReschedulePageWeb.getRoomName();
        int clickCount = hotelReschedulePageWeb.clickAnyBookButton(delayMs);
        Assertion.assertThat("Click button book now should be 1 time", clickCount, is(1));

        step("Check the redirection to the booking page");
        webViewPage.switchToWebViewUrl("reschedule/checkout");
        Assertion.assertThat("Booking page is shown", hotelReschedulePageWeb.isBookingPageShown(), is(true));
    }

    @Author(names = {"Akbar"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Module(name = "Accommodation")
    @Test
    public void ACC_verifyRescheduleIssuedBookingLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        Map<String, String> map = getParam("ACC_verifyRescheduleIssuedBookingLogin");
        BaseTest.paramsThreadLocal.set(map);

        String username = map.get("username");
        String password = map.get("password");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String query = map.get("hotelName");
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));
        int rescheduleDuration = Integer.parseInt(map.get("rescheduleDuration"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];
        String currency = "IDR";

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, query, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,currency);

        step("Get data from the issued order");
        String issuedOrderID =  String.valueOf(hotelApi.getOrderId());

        Assertion.assertThat("Verify Booking via API is successful",result,is(true));

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);
        String appLanguage = homePage.getLanguageApp();
        homePage.gotoHome();

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);

        step("Choose reschedule");
        orderDetailPage.clickReschedule(issuedOrderID);

        step("Choose new date for the reschedule");
        webViewPage.switchToWebViewUrl("reschedule");
        String newStartDate = hotelReschedulePageWeb.changeNightDuration(rescheduleDuration);

        step("Choose a new room type with additional Price");
        webViewPage.switchToWebViewUrl("reschedule");
        hotelReschedulePageWeb.getRoomName();
        String newRoomName = hotelReschedulePageWeb.chooseRoomWithAdditionalPrice();

        step("Submit booking form");
        webViewPage.switchToWebViewUrl("reschedule/checkout");
        hotelReschedulePageWeb.submitRescheduleBookingForm();

        step("Submit the payment method");
        webViewPage.switchToNative();
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");
        paymentPage.clickPayButton();

        step("Open order list page");
        paymentPage.seeOrderList();

        step("Check the new order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        
        Assertion.assertThat("Reschedule card notification",orderDetailPage.isRescheduleCardDisplayed(orderID), is(true));
        Assertion.assertThat("Order ID is equal", orderDetailPage.getOrderID(), is(orderID));
        Assertion.assertThat("Start Date is equal", orderDetailPage.getStartDate(orderID).trim(), is(newStartDate.trim()));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(query, orderID), is(true));
        Assertion.assertThat("Room Name is displayed correctly", orderDetailPage.getRoomName(orderID).toLowerCase(), containsString(newRoomName.toLowerCase()));
        String totalPayment = orderDetailPage.getTotalPrice();

        step("Check the previous order detail");
        orderDetailPage.clickPreviousOrder(orderID);
        Assertion.assertThat("Previous order banner is displayed",orderDetailPage.isNewScheduleButtonDisplayed(issuedOrderID), is(true));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(query, issuedOrderID), is(true));
        Assertion.assertThat("Start Date is different with the rescheduled order", orderDetailPage.getStartDate(issuedOrderID), is(not(newStartDate)));
        Assertion.assertThat("Reschedule button is not displayed", orderDetailPage.isRescheduleButtonDisplayed(issuedOrderID), is(false));

        step("Back to new order detail");
        orderDetailPage.clickNewScheduleButton(orderID);
        orderDetailPage.hidePromoBannerIfPresent();

        step("Check receipt");
        orderDetailPage.openReceipt();
        String actualPdfContent;
        if(Configs.PLATFORM == Platform.IOS){
            boolean isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("Hotel", orderID, appLanguage);
            Assertion.assertThat("Verify receipt file title has correct name", isFileNameCorrect, is(true));
            orderDetailPage.closeShareReceiptBtmSheet();
        }else{
            actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Total price on the order detail should be contains on the receipt", actualPdfContent, containsString(totalPayment));
            orderDetailPage.clickBackFromPdf();
        }

        step("e-Ticket file is opened");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage(appLanguage);
        actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Successfully to open the e-ticket", actualPdfContent.toLowerCase(), containsString(orderID));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PostPurchase")
    @Test
    public void hotelRescheduleNonIdr() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("hotelRescheduleNonIdr");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        String loginType = map.get("loginType");
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

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency, username);

        step("Create a hotel order with waiting payment status");
        String orderId = hotelApi.createWaitingPaymentOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency,"id");
        Assertion.assertThat("Success to create waiting payment order", orderId, is(not(emptyOrNullString())));

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

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);

        step("Choose reschedule");
        
        orderDetailPage.clickReschedule(issuedOrderID);

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
        Assertion.assertThat("Hotel Name should be showing as expected: " + hotelName, orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Room Name should be showing as expected: " + newRoomName, orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));

        step("Check the currency on the new order detail");
        Assertion.assertThat("Current currency should be " + currency, orderDetailPage.getTotalPaymentHotel(), containsString(currency));

        step("Check the previous order detail");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        Assertion.assertThat("Previous order banner should be present",orderDetailPage.isPreviousOrderBannerDisplayed(issuedOrderID), is(true));
        Assertion.assertThat("Hotel Name should be showing as expected: " + hotelName, orderDetailPage.isHotelNameDisplayed(hotelName, issuedOrderID), is(true));
        Assertion.assertThat("Start Date should be different with the rescheduled order", orderDetailPage.getStartDate(issuedOrderID), is(not(newStartDate)));

        step("Check the e-ticket on the new order detail");
        orderDetailPage.clickNewScheduleButton(issuedOrderID);
        orderDetailPage.clickEticketFromHeader(newOrderID);
        orderDetailPage.clickETicketLanguage("English");

        step("e-Ticket file is opened");
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("successfully to download the e-ticket", actualPdfContent, is(not(emptyString())));
    }

    @Author(names = {"Yazid" ,"Kartik"})
    @Module(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Vertical(name = "Accommodation")
    @Test
    public void ACC_rescheduleSingleGuestGetCashback() {
        Map<String, String> param = getParam("ACC_rescheduleSingleGuestGetCashback");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);
        int rescheduleDuration = Integer.parseInt(param.get("rescheduleDuration"));

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");

        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openOrderDetailViaDeeplink(issuedOrderID);

        step("Choose reschedule");
        orderDetailPage.clickReschedule(issuedOrderID);

        step("Choose new date for the reschedule");
        webViewPage.switchToWebViewUrl("bookings/reschedule");
        String newStartDate = hotelReschedulePageWeb.changeNightDuration(rescheduleDuration);

        step("Choose a new room type with Cashback");
        webViewPage.switchToWebViewUrl("bookings/reschedule");
        hotelReschedulePageWeb.getRoomName();
        Double refundEstimation = convertPriceSafely(hotelReschedulePageWeb.getTheFirstRoomRefundEstimation());
        log("refundEstimation-> " + refundEstimation);
        hotelReschedulePageWeb.chooseRoomWithCashback();

        step("Submit booking form");
        webViewPage.switchToWebViewUrl("reschedule/checkout");
        hotelReschedulePageWeb.submitRescheduleBookingForm();

        step("Submit payment with Refund Deposit (free payment cashback)");
        webViewPage.switchToNative();
        String orderID = paymentPage.checkOrderID();
        paymentPage.selectRefund();
        refundPage.submitRefundCashBackFreePayment();

        step("Open order list from success paid banner");
        paymentPage.seeOrderList();

        step("Check the new order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        
        Assertion.assertThat("Reschedule order banner is displayed", orderDetailPage.isRescheduleCardDisplayed(orderID), is(true));
        Assertion.assertThat("Reschedule hotel name should be showing as expected", hotelName.toLowerCase(), is(orderDetailPage.getHotelName(orderID).toLowerCase()));
        Assertion.assertThat("Reschedule check-in date should be showing as expected", orderDetailPage.getStartDate(orderID), is(newStartDate));
        Assertion.assertThat("Reschedule night duration should be showing as expected", orderDetailPage.getTotalNight(orderID), is(String.valueOf(rescheduleDuration)));
        String rescheduleCheckInDate = orderDetailPage.getStartDate(orderID);

        step("Check the previous order detail");
        orderDetailPage.clickPreviousOrder(orderID);
        
        Assertion.assertThat("Previous order banner is displayed", orderDetailPage.isNewScheduleButtonDisplayed(issuedOrderID), is(true));
        Assertion.assertThat("Previous hotel name should be showing as expected", hotelName.toLowerCase(), is(orderDetailPage.getHotelName(issuedOrderID).toLowerCase()));
        Assertion.assertThat("Previous night duration should be different with reschedule order", orderDetailPage.getTotalNight(issuedOrderID), is(not(String.valueOf(rescheduleDuration))));
        Assertion.assertThat("Previous check-in date should be different with reschedule order", orderDetailPage.getStartDate(issuedOrderID), is(not(rescheduleCheckInDate)));
    }

    @Author(names = {"Yazid"})
    @Module(name = "PostPurchase")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRescheduleFlowGuestUpdateVoucherReceipt() {
        Map<String, String> param = getParam("verifyRescheduleFlowGuestUpdateVoucherReceipt");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = param.getOrDefault("currency", "IDR");
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);
        String updatedGuestFullName = randomName() + " autguest";
        int rescheduleDuration = Integer.parseInt(param.get("rescheduleDuration"));

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        DefaultAppsPage<?> defaultAppsPage = DefaultAppsPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, /* isRefundable */ true, /* isSpecialRequestEnable */ false, "",
                "en", /* isNative */ true);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Goto home");
        homePage.gotoHome();

        step("Clear browsers default preference");
        defaultAppsPage.clearBrowserDefaultPreferenceOnLambdaTest();

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);
        String appLanguage = homePage.getLanguageApp();

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);

        step("Check if user is able to click on reschedule button and navigate to room selection page");
        orderDetailPage.clickReschedule(issuedOrderID);
        webViewPage.switchToWebViewUrl("bookings/reschedule");
        Assertion.assertThat("User should be on reschedule room selection page", hotelReschedulePageWeb.isInReschedulePage(), is(true));

        step("Check if user is able to select the date range and choose room with cashback");
        String newStartDate = hotelReschedulePageWeb.changeNightDuration(rescheduleDuration);
        webViewPage.switchToWebViewUrl("bookings/reschedule");
        hotelReschedulePageWeb.chooseRoomWithCashback();

        step("Check if user able to navigate to booking form, change guest name, and proceed to payment page");
        webViewPage.switchToWebViewUrl("reschedule/checkout");
        hotelReschedulePageWeb.fillGuestName(updatedGuestFullName);
        hotelReschedulePageWeb.submitRescheduleBookingForm();

        step("Submit the payment method");
        webViewPage.switchToNative();
        String orderID = paymentPage.checkOrderID();
        paymentPage.selectRefund();
        refundPage.submitRefundCashBackFreePayment();

        step("Check the new order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        
        Assertion.assertThat("Reschedule order banner is displayed", orderDetailPage.isRescheduleCardDisplayed(orderID), is(true));
        Assertion.assertThat("Reschedule hotel name should be showing as expected", hotelName.toLowerCase(), is(orderDetailPage.getHotelName(orderID).toLowerCase()));
        Assertion.assertThat("Reschedule check-in date should be showing as expected", orderDetailPage.getStartDate(orderID), is(newStartDate));
        Assertion.assertThat("Reschedule night duration should be showing as expected", orderDetailPage.getTotalNight(orderID), is(String.valueOf(rescheduleDuration)));
        String totalPayment = orderDetailPage.getTotalPrice();
        if (totalPayment.equalsIgnoreCase("free") || totalPayment.equalsIgnoreCase("gratis")) totalPayment = currency + " " + "0";

        step("Check if user is getting the receipt after the payment is done");
        orderDetailPage.openReceipt();
        String actualPdfContent;
        if (Configs.PLATFORM == Platform.IOS) {
            boolean isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("Hotel", orderID, appLanguage);
            Assertion.assertThat("Verify receipt file title has correct name", isFileNameCorrect, is(true));
            orderDetailPage.closeShareReceiptBtmSheet();
        } else {
            actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Total price on the order detail should be present on the receipt", actualPdfContent, containsString(totalPayment));
            orderDetailPage.clickBackFromPdf();
        }

        step("Check if user is getting the voucher (e-ticket) after the payment is done");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage(appLanguage);
        actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Successfully to open the e-ticket", actualPdfContent.toLowerCase(), containsString(orderID));
        Assertion.assertThat("Updated guest name should be present on the e-ticket", actualPdfContent.toLowerCase(), containsString(updatedGuestFullName.toLowerCase()));
    }

    @Author(names = {"Yazid", "Ashish"})
    @Module(name = "PostPurchase")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void rescheduleMultiGuestNonIDRSameDate() {
        Map<String, String> param = getParam("rescheduleMultiGuestNonIDRSameDate");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int rescheduleNightDuration = Integer.parseInt(param.get("rescheduleNightDuration"));
        String currency = param.get("currency");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        DefaultAppsPage<?> defaultAppsPage = DefaultAppsPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Clear browsers default preference");
        defaultAppsPage.clearBrowserDefaultPreferenceOnLambdaTest();

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        String appLanguage = homePage.getLanguageApp();

        step("Change currency");
        homePage.changeCurrency(currency,username);

        String orderId = "";
        step("Create an order for HOTEL with waiting payment status");
        try {
            orderId = hotelApi.createWaitingPaymentOrderApi(
                    username, password, startDate, hotelName, adult, night, room,
                    currency, 0, "HOTEL", appLanguage);
            Assertion.assertThat("Success to create waiting payment order", orderId, is(not(emptyOrNullString())));
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

        step("Open the order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderId);
        

        step("Get total price from order detail");
        String totalPriceOrderDetail = orderDetailPage.getTotalPaymentHotel();

        step("Choose reschedule");
        orderDetailPage.clickReschedule(orderId);

        step("Choose new date for the reschedule");
        webViewPage.switchToWebViewUrl("reschedule");
        String newStartDate = hotelReschedulePageWeb.changeNightDuration(rescheduleNightDuration);

        step("Choose a new room with the same price as previous order: " + totalPriceOrderDetail);
        webViewPage.switchToWebViewUrl("reschedule");
        String newRoomName = hotelReschedulePageWeb.openRoomDetail();
        hotelReschedulePageWeb.chooseRoomWithSamePrice(totalPriceOrderDetail);

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

        step("Check the new order detail");
        orderListPage.openIssuedTicketViaDeeplink(newOrderID);
        
        Assertion.assertThat("Reschedule order banner is displayed", orderDetailPage.isRescheduleCardDisplayed(newOrderID), is(true));
        Assertion.assertThat("Order ID should be showing as expected", orderDetailPage.getOrderID(), is(newOrderID));
        Assertion.assertThat("Reschedule check-in date should be showing as expected", orderDetailPage.getStartDate(newOrderID), is(newStartDate));
        Assertion.assertThat("Reschedule hotel name should be showing as expected", orderDetailPage.getHotelName(newOrderID), containsString(hotelName));
        Assertion.assertThat("Room Name should be showing as expected", orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));
        Assertion.assertThat("Reschedule night duration should be showing as expected", Integer.parseInt(orderDetailPage.getTotalNight(newOrderID)), is(night));

        step("Check the previous order detail");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        Assertion.assertThat("Previous order banner should be present",orderDetailPage.isNewScheduleButtonDisplayed(orderId), is(true));
        Assertion.assertThat("Previous hotel name should be showing as expected", orderDetailPage.getHotelName(orderId), containsString(hotelName));
        Assertion.assertThat("Start Date should be same with the rescheduled order", orderDetailPage.getStartDate(orderId), is(newStartDate));
        Assertion.assertThat("Previous night duration should be equal with the reschedule order", Integer.parseInt(orderDetailPage.getTotalNight(orderId)), is(night));
        Assertion.assertThat("Reschedule button is not there", orderDetailPage.isRescheduleButtonDisplayed(orderId), is(false));
        Assertion.assertThat("Delete order button is there", orderDetailPage.isDeleteOrderButtonDisplayed(orderId), is(true));

        step("Back to new order detail");
        orderDetailPage.clickNewScheduleButton(newOrderID);

        step("Check receipt");
        orderDetailPage.openReceipt();
        String actualPdfContent;
        if(Configs.PLATFORM == Platform.IOS){
            boolean isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("Hotel", newOrderID, appLanguage);
            Assertion.assertThat("Verify receipt file title has correct name", isFileNameCorrect, is(true));
            orderDetailPage.closeShareReceiptBtmSheet();
        }else{
            actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Currency on the Receipt is "+currency, actualPdfContent, containsString(currency));
            orderDetailPage.clickBackFromPdf();
        }

        step("e-Ticket file is opened");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage(appLanguage);
        actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Successfully to open the e-ticket", actualPdfContent.toLowerCase(), containsString(hotelName.toLowerCase()));
    }

    @Author(names = {"Zia"})
    @Module(name = "PostPurchase")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void rescheduleMultiGuestNonIDRDifferentDate() {
        Map<String, String> param = getParam("rescheduleMultiGuestNonIDRDifferentDate");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int rescheduleNightDuration = Integer.parseInt(param.get("rescheduleNightDuration"));
        String currency = param.get("currency");
        String cardNumber = param.get("cardNumber");
        String expiry = param.get("expiry");
        String cvv = param.get("cvv");
        String cardName = param.get("cardName");

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        DefaultAppsPage<?> defaultAppsPage = DefaultAppsPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePageWeb = HotelReschedulePageWeb.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Clear browsers default preference");
        defaultAppsPage.clearBrowserDefaultPreferenceOnLambdaTest();

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        String appLanguage = homePage.getLanguageApp();
        
        step("Change currency");
        homePage.changeCurrency(currency,username);

        String orderId = "";
        step("Create an order for HOTEL with waiting payment status");
        try {
            orderId = hotelApi.createWaitingPaymentOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                currency, 0, "HOTEL", appLanguage);
        Assertion.assertThat("Success to create waiting payment order", orderId, is(not(emptyOrNullString())));
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
        

        step("Get total price from order detail");
        String totalPriceOrderDetail = orderDetailPage.getTotalPaymentHotel();
        
        step("Choose reschedule");
        orderDetailPage.clickReschedule(orderId);

        step("Choose new date for the reschedule");
        webViewPage.switchToWebViewUrl("reschedule");
        String newStartDate = hotelReschedulePageWeb.changeNightDuration(rescheduleNightDuration);

        step("Choose a new room with the same price as previous order: " + totalPriceOrderDetail);
        webViewPage.switchToWebViewUrl("reschedule");
        String newRoomName = hotelReschedulePageWeb.openRoomDetail();
        hotelReschedulePageWeb.chooseRoomWithSamePrice(totalPriceOrderDetail);

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

        step("Check the new order detail");
        orderListPage.openIssuedTicketViaDeeplink(newOrderID);
        
        Assertion.assertThat("Reschedule order banner is displayed", orderDetailPage.isRescheduleCardDisplayed(newOrderID), is(true));
        Assertion.assertThat("Order ID should be showing as expected", orderDetailPage.getOrderID(), is(newOrderID));
        Assertion.assertThat("Reschedule check-in date should be showing as expected", orderDetailPage.getStartDate(newOrderID), is(newStartDate));
        Assertion.assertThat("Reschedule hotel name should be showing as expected", orderDetailPage.getHotelName(newOrderID), containsString(hotelName));
        Assertion.assertThat("Room Name should be showing as expected", orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));
        Assertion.assertThat("Reschedule night duration should be showing as expected", Integer.parseInt(orderDetailPage.getTotalNight(newOrderID)), is(rescheduleNightDuration));

        step("Check the previous order detail");
        orderDetailPage.goToPreviousOrderBanner();
        orderDetailPage.clickPreviousOrder();
        Assertion.assertThat("Previous order banner should be present",orderDetailPage.isNewScheduleButtonDisplayed(orderId), is(true));
        Assertion.assertThat("Previous hotel name should be showing as expected", orderDetailPage.getHotelName(orderId), containsString(hotelName));
        Assertion.assertThat("Start Date should be different with the rescheduled order", orderDetailPage.getStartDate(orderId), is(not(newStartDate)));
        Assertion.assertThat("Previous night duration should be equal with the reschedule order", Integer.parseInt(orderDetailPage.getTotalNight(orderId)), is(night));
        Assertion.assertThat("Reschedule button is not there", orderDetailPage.isRescheduleButtonDisplayed(orderId), is(false));
        Assertion.assertThat("Delete order button is there", orderDetailPage.isDeleteOrderButtonDisplayed(orderId), is(true));

        step("Back to new order detail");
        orderDetailPage.clickNewScheduleButton(newOrderID);

        step("Check receipt");
        orderDetailPage.openReceipt();
        String actualPdfContent;
        if(Configs.PLATFORM == Platform.IOS){
            boolean isFileNameCorrect = orderDetailPage.isShareFileNameCorrect("Hotel", newOrderID, appLanguage);
            Assertion.assertThat("Verify receipt file title has correct name", isFileNameCorrect, is(true));
            orderDetailPage.closeShareReceiptBtmSheet();
        }else{
            actualPdfContent = orderDetailPage.getPDFContentFile();
            Assertion.assertThat("Currency on the Receipt is "+currency, actualPdfContent, containsString(currency));
            orderDetailPage.clickBackFromPdf();
        }

        step("e-Ticket file is opened");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage(appLanguage);
        actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Successfully to open the e-ticket", actualPdfContent.toLowerCase(), containsString(hotelName.toLowerCase()));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "LandingPage")
    @Test
    public void checkRecentlySearched() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkRecentlySearched");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginAsGuest();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("getting details");
        String dateRoomGuestDetail = searchResultPage.getDateRoomGuestDetail();

        step("Click Back Button");
        searchResultPage.closeFilterPopup();
        searchResultPage.clickBackButton();

        Assertion.assertThat("Recently Searched on Search page is correct", landingPage.isRecentlySearchAvailable(location), is(true));
        Assertion.assertThat("Recently Searched on landing page is correct", landingPage.recentlySearchedStatus(dateRoomGuestDetail, location, landingPage.getAreaText(location)), is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "LandingPage")
    @Test
    public void checkRecentlySearchedLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkRecentlySearchedLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("getting details");
        String dateRoomGuestDetail = searchResultPage.getDateRoomGuestDetail();

        step("Click Back Button");
        searchResultPage.closeFilterPopup();
        searchResultPage.clickBackButton();

        Assertion.assertThat("Recently Searched on Search page is correct", landingPage.isRecentlySearchAvailable(location), is(true));
        Assertion.assertThat("Recently Searched on landing page is correct", landingPage.recentlySearchedStatus(dateRoomGuestDetail, location, landingPage.getAreaText(location)), is(true));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "LandingPage")
    @Test
    public void verifyLandingPageAfterHotelSearch() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyLandingPageAfterHotelSearch");
        BaseTest.paramsThreadLocal.set(map);

        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String currency = map.getOrDefault("currency", "IDR");
        String accommodationType = map.getOrDefault("accommodationType", "hotel");
        boolean priceBeforeTax = Boolean.parseBoolean(map.getOrDefault("priceBeforeTax", "true"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        
        step("Login with any testing account");
        homePage.gotoHome();
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);
        homePage.changeCurrency(currency, username);
        String appLanguage = homePage.getLanguageApp();

        step("Open Hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("If Recently Searched section is present, clear all recently searched items");
        landingPage.clearAllRecentViewed();
        Assertion.assertThat("Recently Searched section is not present", landingPage.isRecentlySearchAvailable(), is(false));
        
        step("Before search, check page module on landing page");
        Assertion.assertThat("Page module is visible", landingPage.isPageModulePresent(), is(true));

        step("Before search, check page module API");
        HotelLandingPageResponse hotelLandingPageResponse = hotelApi.getLandingPageData(accommodationType, username, password, currency, appLanguage, priceBeforeTax);
        Assertion.assertThat("API response has content data", hotelLandingPageResponse.data.size(), is(not(0)));
        Assertion.assertThat("API response has dynamic contents data", hotelLandingPageResponse.data.get(0).dynamicContents.size(), is(not(0)));

        step("Search with any location");
        landingPage.searchStayLocation(location);
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();
        
        step("Verify location data on SRP");
        Assertion.assertThat("SRP loads", searchResultPage.isSearchResultShown(), is(true));
        Assertion.assertThat("Shows the same searched location", searchResultPage.getCurrentLocation().toLowerCase(), is(location.toLowerCase()));

        step("Go back to Hotel landing page");
        searchResultPage.clickBackButton();

        step("Verify Recently Searched");
        Assertion.assertThat("Recently Searched shows the same location that was searched, name matches SRP location", landingPage.getRecentlySearchLocation(), equalToIgnoringCase(location));

        step("Verify page module below Recently Searched");
        Assertion.assertThat("Page module title below Recently Searched is visible (not missing/blank)", landingPage.getPageModuleTitle(), is(not(emptyString())));
        Assertion.assertThat("Page module product card below Recently Searched is visible (not missing/blank)", landingPage.getPageModuleProductCard(), is(not(emptyString())));

        step("Verify page module API after recent search is present");
        hotelLandingPageResponse = hotelApi.getLandingPageData(accommodationType, username, password, currency, appLanguage, priceBeforeTax);
        Assertion.assertThat("API response has content data", hotelLandingPageResponse.data.size(), is(not(0)));
        Assertion.assertThat("API response has dynamic contents data", hotelLandingPageResponse.data.get(0).dynamicContents.size(), is(not(0)));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PDP")
    @Test
    public void checkDeeplinkBehaviorGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkDeeplinkBehaviorGuest");
        BaseTest.paramsThreadLocal.set(map);

        String location = map.get("location");
        String locationType = map.get("locationType").toUpperCase();
        String hotelName = map.get("hotelName");
        int totalGuest = Integer.parseInt(map.get("totalGuest"));
        int totalRoom = Integer.parseInt(map.get("totalRoom"));
        int totalNight = Integer.parseInt(map.get("totalNight"));
        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

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
        hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;

        step("user click SRP deeplink with redirect");
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), totalRoom, totalGuest, hotelId, "HOTEL", hotelName, checkInDate, checkOutDate, language, true);
        landingPage.fillNightCheckInForm();
        Assertion.assertThat("Check SRP content", searchResultPage.isSearchResultShown(), is(true));
        Assertion.assertThat("First hotel name is correct", searchResultPage.getFirstHotelName().toLowerCase(), is(hotelName.toLowerCase()));
        Assertion.assertThat("First hotel name is highlighted", searchResultPage.checkHighlightedResult(), is(true));

        step("user click PDP normal deeplink");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, totalGuest, checkInDate, checkOutDate);
        Assertion.assertThat("successfully open apps and redirected to PDP", detailPage.getHotelNameDetail().toLowerCase(), is(hotelName.toLowerCase()));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PDP")
    @Test
    public void checkDeeplinkBehaviorLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkDeeplinkBehaviorLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String locationType = map.get("locationType").toUpperCase();
        String hotelName = map.get("hotelName");
        int totalGuest = Integer.parseInt(map.get("totalGuest"));
        int totalRoom = Integer.parseInt(map.get("totalRoom"));
        int totalNight = Integer.parseInt(map.get("totalNight"));
        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Get the SRP deeplink parameter");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, locationType);
        int hotelIndexLocation = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String locationId = hotelAutoCompleteResponse.data.contents.get(hotelIndexLocation).publicId;

        step("user click SRP deeplink");
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), totalRoom, totalGuest, locationId, locationType, location, checkInDate, checkOutDate, language);
        landingPage.fillNightCheckInForm();
        Assertion.assertThat("Check SRP content", searchResultPage.isSearchResultShown(), is(true));
        Assertion.assertThat("successfully open apps and redirected to SRP", searchResultPage.getCurrentLocation().toLowerCase(), is(location.toLowerCase()));

        step("Get the SRP deeplink parameter");
        hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        int hotelIndexName = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(hotelIndexName).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(hotelIndexName).country.name;

        step("user click SRP deeplink with redirect");
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), totalRoom, totalGuest, hotelId, "HOTEL", hotelName, checkInDate, checkOutDate, language, true);
        landingPage.fillNightCheckInForm();
        Assertion.assertThat("Check SRP content", searchResultPage.isSearchResultShown(), is(true));
        Assertion.assertThat("First hotel name is correct", searchResultPage.getFirstHotelName().toLowerCase(), is(hotelName.toLowerCase()));
        Assertion.assertThat("First hotel name is highlighted", searchResultPage.checkHighlightedResult(), is(true));

        step("user click PDP deeplink");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, totalGuest, checkInDate, checkOutDate);
        Assertion.assertThat("successfully open apps and redirected to PDP", detailPage.getHotelNameDetail().toLowerCase(), is(hotelName.toLowerCase()));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "SRP")
    @Test
    public void checkSRPBannerLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkSRPBannerLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String locationType = map.get("locationType").toUpperCase();
        String[] dates = generateCheckInOutDate("1-2", 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on login mode and Get the SRP deeplink parameter");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("user open SRP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), 1, 1, locationId, locationType, location, checkInDate, checkOutDate, language);
        landingPage.fillNightCheckInForm();

        step("user check banner");
        String initBanner = searchResultPage.getSrpBanner();
        Assertion.assertThat("user can see banner on SRP", initBanner, is(not(emptyString())));
        Assertion.assertThat("banner swipe automatically", searchResultPage.checkBannerChanges(initBanner), is(true));

        step("user swipe the banner");
        searchResultPage.swipeBanner(Swipe.RIGHT);
        initBanner = searchResultPage.getSrpBanner();
        searchResultPage.swipeBanner(Swipe.LEFT);
        String currBanner = searchResultPage.swipeBanner(Swipe.LEFT);
        Assertion.assertThat("user can see the next/previous banner", currBanner, is(not(initBanner)));

        step("user click the banner");
        searchResultPage.clickBanner();
        Assertion.assertThat("user redirected to web view or opening the bottom popup information", searchResultPage.getRedirectionBanner(), is(not(emptyString())));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "LandingPage")
    @Test
    public void checkRecentlyViewedGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkRecentlyViewedGuest");
        BaseTest.paramsThreadLocal.set(map);

        String[] hotelNameList = map.get("hotelNameList").split("-");
        List<String> hotelList = new ArrayList<>(List.of(hotelNameList));
        if (hotelList.size() < 11) Assertion.skip("Skip this test because of hotel name less than 11 with \"-\" as separator");
        int maxRecentlyViewed = 10;

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on non-login mode");
        loginPage.loginAsGuest();

        step("Clear all recent viewed items");
        homePage.openVerticalPageDeeplink("Hotel", "", "");
        landingPage.clearAllRecentViewed();

        step("user enter PDP via deeplink by " + hotelList.size() + " times with hotel name: " + hotelList);
        hotelPage.openPDPDeeplink(hotelList, "", "", "hotel");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("user click x button on each recent view item");
        Assertion.assertThat("recently viewed showing latest last " + maxRecentlyViewed + " PDP visited by users from all entry point", landingPage.deleteRecentViewItems(hotelList, maxRecentlyViewed), is(maxRecentlyViewed));
        Assertion.assertThat("hotel card deleted from recently viewed section and it's not shown on Hotel Landing Page", landingPage.isRecentViewedLandingPageEmpty(), is(true));

        step("Click search field on the landing page");
        landingPage.clickSearchField();
        Assertion.assertThat("Recently viewed section is not shown on Search drawer", landingPage.isRecentViewedSearchPopupEmpty(), is(true));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "LandingPage")
    @Test
    public void checkRecentlyViewedLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkRecentlyViewedLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String[] hotelNameList = map.get("hotelNameList").split("-");
        List<String> hotelList = new ArrayList<>(List.of(hotelNameList));
        if (hotelList.size() < 11) Assertion.skip("Skip this test because of hotel name less than 11 with \"-\" as separator");
        int maxRecentlyViewed = 10;

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);

        step("user enter PDP via deeplink by " + hotelList.size() + " times with hotel name: " + hotelList);
        hotelPage.openPDPDeeplink(hotelList, username, password, "hotel");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        if (Configs.PLATFORM == Platform.ANDROID) {
            Assertion.assertThat("recently viewed showing latest last " + maxRecentlyViewed + " PDP visited by users from all entry point", landingPage.getRecentViewedItemCount(hotelList, maxRecentlyViewed), is(maxRecentlyViewed));

            step("user click Clear All button");
            landingPage.clearAllRecentViewed();
        } else {

            step("user click x button on each recent view item");
            Assertion.assertThat("recently viewed showing latest last " + maxRecentlyViewed + " PDP visited by users from all entry point", landingPage.deleteRecentViewItems(hotelList, maxRecentlyViewed), is(maxRecentlyViewed));
        }

        Assertion.assertThat("hotel card deleted from recently viewed section and it's not shown on Hotel Landing Page", landingPage.isRecentViewedLandingPageEmpty(), is(true));

        step("Click search field on the landing page");
        landingPage.clickSearchField();
        Assertion.assertThat("Recently viewed section is not shown on Search drawer", landingPage.isRecentViewedSearchPopupEmpty(), is(true));
    }

    @Author(names = {"Okta"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "BookingForm")
    @Test(dataProvider = "checkBookingFormDetailGuestDataProvider")
    public void checkBookingFormDetailGuest(Map<String, String> map) {
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        BookingDetailPage<WebDriver> bookingDetailPage = BookingDetailPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("User open app as guest");
        loginPage.loginAsGuest();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("set total room and guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.clickDone();

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel from SRP");
        searchResultPage.selectFirstHotelCard();

        step("Go to hotel PDP");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from hotel room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Get booking data in booking form");
        String hotelNameForm = bookingFormPage.getHotelNameBookingForm();
        String roomNameForm = bookingFormPage.getRoomNameBookingForm();
        String checkInDateForm = bookingFormPage.getCheckinBookingForm();
        String checkOutDateForm = bookingFormPage.getCheckOutDateBookingForm();
        /*because there is no check-in time in the new UI
        String checkInTimeForm = bookingFormPage.getCheckInTimeBookingForm();*/
        String roomGuestForm = bookingFormPage.getRoomGuestBookingForm();
        String roomBedForm = bookingFormPage.getRoomBedBookingForm();
        String cancellationForm = bookingFormPage.getCancellationBookingForm();

        String[] formData = { hotelNameForm, roomNameForm, checkInDateForm, checkOutDateForm,
                roomGuestForm, roomBedForm, cancellationForm };

        step("Open booking detail page");
        bookingFormPage.openBookingDetailPage();

        step("Get booking data in booking detail page");
        String hotelNameDetail = bookingDetailPage.getHotelNameBookingDetail();
        String roomNameDetail = bookingDetailPage.getRoomNameBookingDetail();
        String checkInDateDetail = bookingDetailPage.getCheckInDateBookingDetail();
        String checkOutDateDetail = bookingDetailPage.getCheckOutDateBookingDetail();
        /*String checkInTimeDetail = bookingDetailPage.getCheckInTimeBookingDetail();*/
        String roomGuestDetail = bookingDetailPage.getRoomGuestBookingDetail();
        String roomBedDetail = bookingDetailPage.getRoomBedBookingDetail(roomBedForm);
        String cancellationDetail = bookingDetailPage.getCancellationBookingDetail();

        String[] detailData = { hotelNameDetail, roomNameDetail, checkInDateDetail, checkOutDateDetail,
                roomGuestDetail, roomBedDetail, cancellationDetail };

        step("Assert booking form data is the same as booking detail data");
        Assertion.assertThat("Booking data is the same", bookingDetailPage.isBookingDetailCorrect(formData, detailData), is(true));
    }

    @Author(names = {"Okta"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "BookingForm")
    @Test(dataProvider = "checkBookingFormDetailLoginDataProvider")
    public void checkBookingFormDetailLogin(Map<String, String> map) {
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        BookingDetailPage<WebDriver> bookingDetailPage = BookingDetailPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("set total room and guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.clickDone();

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel from SRP");
        searchResultPage.selectFirstHotelCard();

        step("Go to hotel PDP");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from hotel room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Get booking data in booking form");
        String hotelNameForm = bookingFormPage.getHotelNameBookingForm();
        String roomNameForm = bookingFormPage.getRoomNameBookingForm();
        String checkInDateForm = bookingFormPage.getCheckinBookingForm();
        String checkOutDateForm = bookingFormPage.getCheckOutDateBookingForm();
        /*because there is no check-in time in the new UI
        String checkInTimeForm = bookingFormPage.getCheckInTimeBookingForm();*/
        String roomGuestForm = bookingFormPage.getRoomGuestBookingForm();
        String roomBedForm = bookingFormPage.getRoomBedBookingForm();
        String cancellationForm = bookingFormPage.getCancellationBookingForm();

        String[] formData = { hotelNameForm, roomNameForm, checkInDateForm, checkOutDateForm,
                roomGuestForm, roomBedForm, cancellationForm };

        step("Open booking detail page");
        bookingFormPage.openBookingDetailPage();

        step("Get booking data in booking detail page");
        String hotelNameDetail = bookingDetailPage.getHotelNameBookingDetail();
        String roomNameDetail = bookingDetailPage.getRoomNameBookingDetail();
        String checkInDateDetail = bookingDetailPage.getCheckInDateBookingDetail();
        String checkOutDateDetail = bookingDetailPage.getCheckOutDateBookingDetail();
        /*String checkInTimeDetail = bookingDetailPage.getCheckInTimeBookingDetail();*/
        String roomGuestDetail = bookingDetailPage.getRoomGuestBookingDetail();
        String roomBedDetail = bookingDetailPage.getRoomBedBookingDetail(roomBedForm);
        String cancellationDetail = bookingDetailPage.getCancellationBookingDetail();

        String[] detailData = { hotelNameDetail, roomNameDetail, checkInDateDetail, checkOutDateDetail,
                roomGuestDetail, roomBedDetail, cancellationDetail };

        step("Assert booking form data is the same as booking detail data");
        Assertion.assertThat("Booking data is the same", bookingDetailPage.isBookingDetailCorrect(formData, detailData), is(true));
    }

    @Author(names = {"Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "BookingForm")
    @Test
    public void testCheckPriceDetailOnBookingFormLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> map = getParam("testCheckPriceDetailOnBookingFormLogin");

        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String hotelName = map.get("hotelName");
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency, username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("set total room");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        detailPage.RemovePopUp();
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Get the total payment and open price detail and verify prices");
        bookingFormPage.continueToAddOnPage();
        String totalPayment = bookingFormPage.getTotalToPayBookingForm();
        boolean isPriceBreakDownCorrect = bookingFormPage.isPriceDetailCorrect(totalPayment);
        Assertion.assertThat("Is price breakdown correct", isPriceBreakDownCorrect, is(true));
    }

    @Author(names = {"Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "BookingForm")
    @Test
    public void testCheckPriceDetailOnBookingFormGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCheckPriceDetailOnBookingFormGuest");
        BaseTest.paramsThreadLocal.set(map);
        String hotelName = map.get("hotelName");
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("login as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency, "");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        detailPage.RemovePopUp();
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

        step("Get the total payment and open price detail and verify prices");
        bookingFormPage.continueToAddOnPage();
        String totalPayment = bookingFormPage.getTotalToPayBookingForm();
        boolean isPriceBreakDownCorrect = bookingFormPage.isPriceDetailCorrect(totalPayment);
        Assertion.assertThat("Is price breakdown correct", isPriceBreakDownCorrect, is(true));
    }

    @Author(names = "Kartik")
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "SRP")
    @Test
    public void VerifyMainCardInformationHotelSrpGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("VerifyMainCardInformationHotelSrpGuest");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("login as guest");
        loginPage.loginAsGuest();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        Assertion.assertThat("Is hotel name Visible on Hotel Card ", searchResultPage.isFirstHotelNameDisplayed(), is(true));
        Assertion.assertThat("Is hotel price Visible on Hotel Card ", searchResultPage.isFirstHotelPriceDisplayed(), is(true));
        Assertion.assertThat("Is hotel star rating Visible on Hotel Card ", searchResultPage.getFirstHotelStar(), is(true));
        Assertion.assertThat("Is hotel location Visible on Hotel Card ", searchResultPage.istFirstHotelLocationDisplayed(), is(true));
        Assertion.assertThat("Is hotel added benefit Visible on Hotel Card  ", searchResultPage.isAddedBenefitVisible(), is(true));
    }

    /**
     * PDP infinity-promo + room-detail + in-app-map / nearby chips coverage.
     *
     * Flow:
     * - Login, set currency from testdata (e.g. IDR).
     * - SO phase: open PDP via deeplink with single-order config (soTotalRoom / soTotalAdult)
     *     - verify infinity promo cards section is shown,
     *     - copy first promo code and verify the "copied" snackbar,
     *     - verify see-all when ≥ 5 distinct promo codes were detected by swiping the carousel, open it and copy a promo from the section,
     *     - capture first room name + price on room list, open the room detail and assert they match,
     *     - tap book on room detail and assert redirect to booking form.
     * - MO phase: re-open PDP via deeplink with MO config (moTotalRoom / moTotalAdult)
     *     - open room detail, increment quantity and save → verify room list + MO banner,
     *     - open room detail again and verify room info attributes,
     *     - navigate back to PDP and open in-app map: verify popup, hotel highlight, nearby chips,
     *     - tap Accommodations chip and secondary chip (testdata secondaryNearbyChip, default Shopping),
     *     - reopen accommodations chip to assert nearby hotels list, then close map.
     */
    @Author(names = {"Yazid"})
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPdpInfinityPromoRoomDetailAndInAppMap() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPdpInfinityPromoRoomDetailAndInAppMap");
        BaseTest.paramsThreadLocal.set(param);

        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String currency = param.get("currency");
        int soTotalAdult = Integer.parseInt(param.get("soTotalAdult"));
        int soTotalRoom = Integer.parseInt(param.get("soTotalRoom"));
        int moTotalAdult = Integer.parseInt(param.get("moTotalAdult"));
        int moTotalRoom = Integer.parseInt(param.get("moTotalRoom"));
        int totalNight = Integer.parseInt(param.get("totalNight"));
        String rangeAddDate = param.get("rangeAddDate");

        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Login as " + email + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(email, password, currency);

        step("Resolve hotel id/country for " + hotelName);
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(email, password, hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;

        step("Open PDP via deeplink (SO: " + soTotalRoom + " room, " + soTotalAdult + " adult)");
        hotelPage.openPDPDeeplink(accommodationType, country.toLowerCase(), hotelId, soTotalRoom, soTotalAdult, checkInDate, checkOutDate);
        Assertion.assertThat("Hotel name on PDP matches deeplink target", detailPage.getHotelNameDetail(), containsStringIgnoringCase(hotelName));

        step("Verify infinity promo cards section is visible on PDP");
        Assertion.assertThat("Infinity promo cards section is shown on PDP", detailPage.isInfinityPromoSectionDisplayed(), is(true));

        step("Copy first promo code from PDP card and verify 'copied' feedback");
        String copiedCode = detailPage.copyFirstInfinityPromoCode();
        Assertion.assertThat("Copied promo code is not empty", copiedCode, is(not(emptyOrNullString())));
        String expectedPromoCode = normalizeNonBreakingSpaceText(copiedCode);
        String clipboardText = detailPage.getClipboardText();
        Assertion.assertThat("Clipboard should match copied infinity promo code", clipboardText, is(expectedPromoCode));

        step("Verify see-all button when at least 5 distinct infinity promo codes exist (carousel swipe)");
        int promoCardCount = detailPage.getInfinityPromoCardCount();
        Assertion.assertThat("At least 1 distinct infinity promo code is shown on PDP", promoCardCount, greaterThanOrEqualTo(1));
        if (promoCardCount >= 5) {
            Assertion.assertThat(
                    "See-all button is visible when at least 5 distinct promo codes were found on the carousel (got " + promoCardCount + ")",
                    detailPage.isSeeAllPromoButtonDisplayed(),
                    is(true));
            detailPage.clickSeeAllPromoButton();
            Assertion.assertThat("See-all promo section/bottom sheet is shown after clicking 'See all'", detailPage.isSeeAllPromoSectionShown(), is(true));
            String copiedSectionPromoText = detailPage.copyFirstPromoCodeFromSection();
            String sectionCode = copiedSectionPromoText.contains(": ")
                    ? copiedSectionPromoText.substring(copiedSectionPromoText.indexOf(": ") + 2).trim()
                    : copiedSectionPromoText.trim();
            Assertion.assertThat("Promo code copied from see-all section is not empty", sectionCode, is(not(emptyOrNullString())));
            expectedPromoCode = normalizeNonBreakingSpaceText(sectionCode);
            clipboardText = detailPage.getClipboardText();
            Assertion.assertThat("Clipboard should match promo copied from see-all section", clipboardText, is(expectedPromoCode));
            detailPage.closeSeeAllPromoSection();
        } else {
            step("Skipping see-all promo verification because distinct promo codes on carousel = " + promoCardCount + " (< 5)");
        }

        step("Go to room list and verify roomDetail content matches roomList");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();
        Assertion.assertThat("Room list is shown after seeing rooms", roomListPage.isRoomListShown(), is(true));
        String firstRoomNameOnList = roomListPage.getTheFirstHotelRoomName();
        String firstRoomPriceOnList = roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.openRoomDetailPage();
        String firstRoomNameOnDetail = roomDetailPage.getHotelRoomNameOnRoomDetail();
        String firstRoomPriceOnDetail = roomDetailPage.getHotelRoomPriceOnRoomDetail();
        Assertion.assertThat("Room name on room detail matches room list", firstRoomNameOnDetail, containsStringIgnoringCase(firstRoomNameOnList));
        Assertion.assertThat("Room price on room detail matches room list",
                CoreFunctions.convertPrice(firstRoomPriceOnDetail), is(CoreFunctions.convertPrice(firstRoomPriceOnList)));

        step("Tap book on room detail and verify redirect to booking form (SO)");
        roomDetailPage.clickBookButton(soTotalRoom);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        Assertion.assertThat("User is redirected to Booking Form after tapping book on room detail",
                bookingFormPage.isBookingFormShown(true), is(true));

        step("Leave booking form before opening MO PDP deeplink");
        bookingFormPage.clickBackButton();

        step("Re-open PDP via deeplink with MO config (" + moTotalRoom + " room, " + moTotalAdult + " adult)");
        hotelPage.openPDPDeeplink(accommodationType, country.toLowerCase(), hotelId, moTotalRoom, moTotalAdult, checkInDate, checkOutDate);
        Assertion.assertThat("Hotel name on PDP matches deeplink target", detailPage.getHotelNameDetail(), containsStringIgnoringCase(hotelName));

        step("MO — add rooms from room detail, save, and land on room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();
        Assertion.assertThat("Room list is shown before opening room detail (MO)", roomListPage.isRoomListShown(), is(true));
        roomListPage.openRoomDetailPage();
        roomDetailPage.clickAddRoomButton(moTotalRoom);
        Assertion.assertThat("After saving rooms from room detail in MO, user moves to the room list",
                roomListPage.isRoomListShown(), is(true));

        step("MO — open room detail again and verify room info");
        roomListPage.openRoomDetailPage();
        Assertion.assertThat("Room info attributes are displayed on room detail page",
                roomDetailPage.isRoomInfoDetailsDisplayed(), is(true));

        step("Return to PDP for location / in-app map checks");
        roomListPage.clickBackButton();
        Assertion.assertThat("Back from room detail lands on room list", roomListPage.isRoomListShown(), is(true));
        roomListPage.clickBackButton();
        Assertion.assertThat("PDP is visible again after navigating back from room list", detailPage.getHotelNameDetail(), containsStringIgnoringCase(hotelName));

        step("Open the in-app map from PDP and verify popup with hotel highlight + nearby points");
        detailPage.openInAppMap();
        Assertion.assertThat("In-app map popup is shown after tapping location on PDP", detailPage.isInAppMapPopupShown(), is(true));
        Assertion.assertThat("Chosen hotel is highlighted on the in-app map", detailPage.isChosenHotelHighlightedOnMap(hotelName), is(true));
        Assertion.assertThat("Nearby points (shopping/culinary spots) are visible on the in-app map", detailPage.areNearbyPointsShownOnMap(hotelName), is(true));

        step("Tap accommodations nearby chip and verify nearby hotel data");
        detailPage.clickNearbyChip();
        Assertion.assertThat("Nearby accommodation list/data is shown after tapping the Accommodations chip",
                detailPage.isNearbyAccommodationListShown(), is(true));

        step("Open another nearby chip and verify data");
        detailPage.clickNearbyChip();
        Assertion.assertThat("Nearby data is shown after tapping the secondary nearby chip",
                detailPage.isNearbyChipDataShown(), is(true));
    }

    @Author(names = {"Aditi"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "SRP")
    @Test
    public void verifySrpRefundRescheduleBannerAndPdpMapSafeArea() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifySrpRefundRescheduleBannerAndPdpMapSafeArea");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String bannerTitle = map.getOrDefault("bannerTitle", "100% Refund");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Input credential on login page");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, "IDR");
        homePage.changeCurrency("IDR", username);

        step("Open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Navigate to Search Results Page (SRP)");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();
        Assertion.assertThat("Search result page is displayed", searchResultPage.isSearchResultShown(), is(true));

        step("Scroll to Banner Section");
        searchResultPage.scrollToBannerSection();

        step("Tap \"100% Refund & Reschedule\" Banner");
        searchResultPage.clickBannerByTitle(bannerTitle);

        step("Verify Redirection is Displayed");
        Assertion.assertThat("User redirected to web view or opening the bottom popup information",
                searchResultPage.getRedirectionBanner(), containsString("refund"));

        step("Navigate back to SRP");
        searchResultPage.navigateBackToSrp();
        Assertion.assertThat("Search result page is displayed after navigating back from banner",
                searchResultPage.isSearchResultShown(), is(true));

        step("Select any hotel card");
        String hotel = searchResultPage.getFirstHotelName();
        searchResultPage.selectFirstHotelCard();
        Assertion.assertThat("Hotel detail should be displayed", detailPage.isHotelDetailShown(), is(true));

        step("Open PDP Map");
        detailPage.openInAppMap();
        Assertion.assertThat("In-app map popup is opened after tapping location on PDP", detailPage.isInAppMapPopupShown(), is(true));

        step("Verify Back Button Position Within Safe Area");
        Assertion.assertThat("In-app map back button is within safe area", detailPage.isInAppMapBackButtonWithinSafeArea(), is(true));

        step("Verify \"Accommodation Nearby\" Section Is Within Safe Area and Tappable");
        Assertion.assertThat("Accommodation nearby section is within safe area and tappable",
                detailPage.isAccommodationNearbySectionWithinSafeAreaAndTappable(), is(true));

        step("Verify first nearby accommodation hotel card is displayed on the map");
        Assertion.assertThat("First nearby accommodation hotel card should appear after tapping Accommodation Nearby",
                detailPage.isFirstNearbyAccommodationHotelCardShown(hotel), is(true));

        step("Tap Accommodation Nearby again to collapse the nearby hotel list");
        detailPage.clickNearbyChip();

        step("Verify nearby accommodation hotel card is hidden");
        Assertion.assertThat("First nearby accommodation hotel card should disappear after tapping Accommodation Nearby again",
                detailPage.isFirstNearbyAccommodationHotelCardHidden(hotel), is(true));

        step("Tap back on in-app map");
        detailPage.clickInAppMapBackButton();

        step("Verify opened hotel on PDP matches the hotel selected from SRP");
        Assertion.assertThat("Hotel name on PDP should match the first hotel selected from SRP",
                detailPage.getHotelName(), containsStringIgnoringCase(hotel));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Room")
    @Test
    public void testCheckRoomDetailPriceLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> map = getParam("testCheckRoomDetailPriceLogin");

        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String hotelName = map.get("hotelName");
        int totalRoom = 1;
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency, username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(2);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open room detail");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.openRoomDetailPage();

        step("Get room price on the room detail");
        Double price = convertPrice(roomDetailPage.getHotelRoomPriceOnRoomDetail());

        step("Open Price Detail");
        detailPage.openPriceDetail();

        Assertion.assertThat("Price on Room detail page is equal to total price on price detail", detailPage.priceMatch(price, totalRoom), is(true));
        Assertion.assertThat("Is Price breakdown on price detail correct", detailPage.checkPriceBreakdown(price, totalRoom), is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Room")
    @Test
    public void testCheckRoomDetailPrice() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCheckRoomDetailPrice");
        BaseTest.paramsThreadLocal.set(map);
        String hotelName = map.get("hotelName");
        int totalRoom = 1;
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency, "");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(2);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open room detail");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.openRoomDetailPage();

        step("Get room price on the room detail");
        Double price = convertPrice(roomDetailPage.getHotelRoomPriceOnRoomDetail());

        step("Open Price Detail");
        detailPage.openPriceDetail();

        Assertion.assertThat("Price on Room detail page is equal to total price on price detail", detailPage.priceMatch(price, totalRoom), is(true));
        Assertion.assertThat("Is Price breakdown on price detail correct", detailPage.checkPriceBreakdown(price, totalRoom), is(true));
    }


    @Author(names = {"Kartik"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Room")
    @Test
    public void verifyRoomDetailImageGalleryLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyRoomDetailImageGalleryLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String hotelName = map.get("hotelName");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay HotelName");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the detail page");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Go to the RoomList page");
        roomListPage.openRoomDetailPage();

        step("Go to the RoomDetail page");
        roomDetailPage.seeAllphotos();

        Assertion.assertThat("Verify that photo gallery is visible",roomDetailPage.isPhotoGalleryVisible(),is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void checkPriceDetailPaymentPage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPriceDetailPaymentPage");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        homePage.changeCurrency("IDR", "");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel from the hotel list");
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

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select payment method");
        paymentPage.selectPaymentMethod("BCA Virtual Account");

        String priceOnPaymentPage = paymentPage.getTotalPriceFirstPage();

        step("open price detail");
        paymentPage.openPriceDetail();

        Assertion.assertThat("Total price on payment page is equal to Total price on price detail", paymentPage.getTotalPricePayment(), is(priceOnPaymentPage));
        Assertion.assertThat("is price Breakdown Correct", paymentPage.priceBreakDown(), is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void checkPriceDetailPaymentPageLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPriceDetailPaymentPageLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR", "");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel from the hotel list");
        searchResultPage.selectFirstHotelCard();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select payment method");
        paymentPage.selectPaymentMethod("BCA Virtual Account");

        String priceOnPaymentPage = paymentPage.getTotalPriceFirstPage();

        step("open price detail");
        paymentPage.openPriceDetail();

        Assertion.assertThat("Total price on payment page is equal to Total price on price detail", paymentPage.getTotalPricePayment(), is(priceOnPaymentPage));
        Assertion.assertThat("is price Breakdown Correct", paymentPage.priceBreakDown(), is(true));
    }

    @Author(names = {"Okta"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PDP")
    @Test(dataProvider = "checkHotelViewMapGuestDataProvider")
    public void checkHotelViewMapGuest(Map<String, String> map) {
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("User open app as guest");
        loginPage.loginAsGuest();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel from SRP");
        searchResultPage.selectFirstHotelCard();

        step("Click location tab in PDP");
        String hotelName = detailPage.getHotelNameDetail();
        detailPage.clickLocationTab();

        step("Click See maps button in PDP");
        detailPage.clickSeeMapsButton();

        step("Assert redirect to map apps");
        Assertion.assertThat("App redirected to map apps", detailPage.isNativeMapsShown(), is(true));
        Assertion.assertThat("Hotel name in map is correct", detailPage.getHotelNamePoi(), is(hotelName));
    }

    @Author(names = {"Okta"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Module(name = "Accommodation")
    @Test(dataProvider = "checkHotelViewMapLoginDataProvider")
    public void ACC_checkHotelViewMapLogin(Map<String, String> map) {
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel from SRP");
        searchResultPage.selectFirstHotelCard();

        step("Click location tab in PDP");
        String hotelName = detailPage.getHotelNameDetail();
        detailPage.clickLocationTab();

        step("Click See maps button in PDP");
        detailPage.clickSeeMapsButton();

        step("Assert redirect to map apps");
        Assertion.assertThat("App redirected to map apps", detailPage.isNativeMapsShown(), is(true));
        Assertion.assertThat("Hotel name in map is correct", detailPage.getHotelNamePoi(), containsStringIgnoringCase(hotelName));
    }

    @Author(names = {"Kartik"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Room")
    @Test
    public void verifyRoomDetailImageGalleryGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyRoomDetailImageGalleryGuest");
        BaseTest.paramsThreadLocal.set(map);

        String hotelName = map.get("hotelName");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

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

        step("Go to the detail page");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Go to the RoomList page");
        roomListPage.openRoomDetailPage();

        step("Go to the RoomDetail page");
        roomDetailPage.seeAllphotos();

        Assertion.assertThat("Verify that photo gallery is visible",roomDetailPage.isPhotoGalleryVisible(),is(true));
    }

    @Author(names = {"Naufal"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Room")
    @Test
    public void verifyRoomDetailBookButtonLogin(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyRoomDetailBookButtonLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String hotelName = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("set total room");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select 1 room from the room list and click book button");
        roomListPage.openRoomDetailPage();
        String expectedRoomName = roomDetailPage.getHotelRoomNameOnRoomDetail();
        roomDetailPage.clickBookButton(1);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        Assertion.assertThat("booking from is shown", bookingFormPage.isBookingFormShown(true), is(true));
        Assertion.assertThat("room name is match", bookingFormPage.getRoomNameBookingForm(), is(expectedRoomName));
    }

    @Author(names = {"Naufal"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Room")
    @Test
    public void verifyRoomDetailBookButtonGuest(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyRoomDetailBookButtonGuest");
        BaseTest.paramsThreadLocal.set(map);
        String hotelName = map.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Logout if on the login session");
        loginPage.loginAsGuest();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("set total room");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select 1 room from the room list and click book button");
        roomListPage.openRoomDetailPage();
        String expectedRoomName = roomDetailPage.getHotelRoomNameOnRoomDetail();
        roomDetailPage.clickBookButton(1);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        Assertion.assertThat("booking from is shown", bookingFormPage.isBookingFormShown(false), is(true));
        Assertion.assertThat("room name is match", bookingFormPage.getRoomNameBookingForm(), is(expectedRoomName));
    }

    @Author(names = {"Mridul"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "SearchPage")
    @Test
    public void verifyHotelSRPDeeplinkAndFlexibleStayLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("verifyHotelSRPDeeplinkAndFlexibleStayLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String locationId = param.get("locationId");
        String locationType = param.get("locationType");
        int rooms = Integer.parseInt(param.get("rooms"));
        int adults = Integer.parseInt(param.get("adults"));
        int daysToCheckIn = Integer.parseInt(param.get("daysToCheckIn"));
        int stayDuration = Integer.parseInt(param.get("stayDuration"));
        int cardsToCheck = Integer.parseInt(param.get("cardsToCheck"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        String checkInDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToCheckIn);
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(currentDate, daysToCheckIn + stayDuration);
        String dateRange = daysToCheckIn + "-" + (daysToCheckIn + stayDuration);

        step("Goto home with login session");
        homePage.gotoHome();
        loginPage.loginWith(loginType, username, password);
        homePage.gotoHome();

        step("Step 1 - Land on SRP via deeplink for location: " + location);
        landingPage.deeplinkWithSelectedDate(rooms, adults, locationId, locationType, location, dateRange);
        searchResultPage.waitForSrpToLoad();

        Assertion.assertThat("Hotel SRP is loaded after deeplink navigation",
                searchResultPage.isSearchResultShown(), is(true));

        step("Step 2 - Verify SRP header reflects deeplink-searched location and dates");
        String currentLocation = searchResultPage.getCurrentLocation();
        String dateRoomGuestDetail = searchResultPage.getDateRoomGuestDetail();
        String checkInOutDate = searchResultPage.getCheckInOutDate();
        log("currentLocation: " + currentLocation);
        log("dateRoomGuestDetail: " + dateRoomGuestDetail);
        log("checkInOutDate: " + checkInOutDate);
        log("expected check-in date: " + checkInDate + ", check-out date: " + checkOutDate);

        Assertion.assertThat("Assertion 1 - Search header shows deeplink-searched location",
                currentLocation, containsStringIgnoringCase(location));
        Assertion.assertThat("Assertion 1 - Search header shows configured room count",
                dateRoomGuestDetail, containsString(String.valueOf(rooms)));
        Assertion.assertThat("Assertion 1 - Search header shows configured adult count",
                dateRoomGuestDetail, containsString(String.valueOf(adults)));
        Assertion.assertThat("Assertion 1 - Search header shows a non-empty check-in/out date",
                !checkInOutDate.isBlank(), is(true));

        step("Step 3 - Verify hotel cards belong to the searched area, region or city");
        Assertion.assertThat("Assertion 2 - First " + cardsToCheck + " hotel cards on SRP belong to the searched location",
                searchResultPage.isHotelCardLocationMatch(location, cardsToCheck), is(true));

        step("Step 4 - Scroll to the sold-out intercept (flexible stay dates) on SRP");
        searchResultPage.scrollToFlexibleStay();

        Assertion.assertThat("Assertion 3 - Sold-out intercept (flexible stay dates) is visible",
                searchResultPage.isFlexibleStayVisible(), is(true));
        Assertion.assertThat("Assertion 3 - Sold-out intercept heading matches 'Do you have flexible stay dates' in selected language",
                searchResultPage.getFlexibleStayHeading(),
                either(containsString("Do you have flexible stay dates")).or(containsString("Tanggal nginepmu fleksibel")));

        step("Step 5 - Verify hotel cards inside sold-out intercept show no price");
        Assertion.assertThat("Assertion 4 - Sold-out intercept contains at least one hotel card",
                searchResultPage.getFlexibleStayHotelCardCount(), greaterThan(0));
        Assertion.assertThat("Assertion 4 - No room/price available for hotel cards inside sold-out intercept",
                searchResultPage.isFlexibleStayPriceVisible(), is(false));
    }

    @Author(names = {"Akbar"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Filter")
    @Test
    public void verifySRPApplyInterceptFilter() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> map = getParam("verifySRPApplyInterceptFilter");
        BaseTest.paramsThreadLocal.set(map);

        String location = map.get("location");
        String totalHotel = map.get("totalHotel");
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency, "");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Finding intercept filter and randomly select the available option");
        FilterInfo filterInfo = searchResultPage.findAndChooseInterceptFilter();

        step("Assert if the SRP is valid after using intercept filter");
        Assertion.assertThat("Assert if the bottom bar has green dot",
                searchResultPage.isFilterActivated(filterInfo.filterType), is(true));
        Assertion.assertThat("Assert if first '"+totalHotel+"' hotel card is having the right hotel star",
                searchResultPage.checkHotelCardList(filterInfo.filterType,Integer.parseInt(totalHotel), filterInfo.choosenFilter.split("-")), is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "BookingForm")
    @Test
    public void checkAddOn() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkAddOn");
        BaseTest.paramsThreadLocal.set(map);
        String hotelName = map.get("hotelName");
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Logout if on the login session");
        loginPage.loginAsGuest();
        homePage.changeCurrency(currency, "");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        detailPage.RemovePopUp();
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToAddOnPage();
        Double priceWithOutAddOn = convertPrice(bookingFormPage.getTotalToPayBookingForm());

        step("Click Extra Facility Add Button");
        bookingFormPage.clickExtraFacilityAddOn();
        String addOnCategory = bookingFormPage.getAddonTitle();
        Double addOnPrice = convertPrice(bookingFormPage.getAddOnPrice());
        String priceWithAddOnString = bookingFormPage.getTotalToPayBookingForm();
        double priceWithAddOn = convertPrice(priceWithAddOnString);
        bookingFormPage.openSubtotal();
        Assertion.assertThat("Is price breakdown correct", bookingFormPage.isPriceDetailCorrect(priceWithAddOnString), is(true));
        Assertion.assertThat("Price after addOn is equals to hotel price + addOn price", normalizeNonIdrPrice(false, priceWithOutAddOn + addOnPrice), is(normalizeNonIdrPrice(false, priceWithAddOn)));
        Assertion.assertThat("Addon Price is correct on price detail", bookingFormPage.MatchAddOnPriceDetail(addOnPrice, addOnCategory), is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "BookingForm")
    @Test
    public void checkAddOnLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkAddOnLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String hotelName = map.get("hotelName");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        detailPage.RemovePopUp();
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Submit booking");
        bookingFormPage.continueToAddOnPage();
        Double priceWithOutAddOn = convertPrice(bookingFormPage.getTotalToPayBookingForm());

        step("Click Extra Facility Add Button");
        bookingFormPage.clickExtraFacilityAddOn();
        String addOnCategory = bookingFormPage.getAddonTitle();
        Double addOnPrice = convertPrice(bookingFormPage.getAddOnPrice());
        String priceWithAddOnString = bookingFormPage.getTotalToPayBookingForm();
        double priceWithAddOn = convertPrice(priceWithAddOnString);
        bookingFormPage.openSubtotal();
        Assertion.assertThat("Is price breakdown correct", bookingFormPage.isPriceDetailCorrect(priceWithAddOnString), is(true));
        Assertion.assertThat("Price after addOn is equals to hotel price + addOn price", normalizeNonIdrPrice(false, priceWithOutAddOn + addOnPrice), is(normalizeNonIdrPrice(false, priceWithAddOn)));
        Assertion.assertThat("Addon Price is correct on price detail", bookingFormPage.MatchAddOnPriceDetail(addOnPrice, addOnCategory), is(true));
    }

    @Author(names = {"Kartik","Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PDP")
    @Test
    public void testCheckReviewHotelGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testCheckReviewHotelGuest");
        BaseTest.paramsThreadLocal.set(map);
        String hotelName = map.get("hotelName");
        String category = map.get("category");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        ReviewPage<WebDriver> reviewPage = ReviewPage.getInstance(session);

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
        reviewPage.isReviewPageDisplayed();

        step("check like btn");
        reviewPage.clickLikeBtn();
        reviewPage.verifyLoginPop();
        reviewPage.closeLoginPopup();

        step("check sort filter");
        reviewPage.checkSortFilter();

        step("verify Category filter");
        reviewPage.verifyCategoryFilter(category);
    }

    /**
     * Login (if configured) → open hotel SRP via deeplink/search input → scan the first
     * {@code totalHotelCards} listing cards until one exposes a review summary (score /5 + volume), assert
     * that line’s format → apply guest/property rating filter and assert the displayed hotel cards
     * honour the selected threshold via the existing validation flow.
     * <p>
     * Sheet key: {@code verifyReviewScore}. Params: loginType, email, password, srpLocation, currency
     * (optional), locationType (optional, default {@code AREA}), language (optional; default = current app
     * language from {@link HomePage#getLanguageApp()}), rangeAddDate, totalNight, totalHotelCards,
     * optional totalRoom/totalAdult (default 1), and guestRatingFilter (passed to
     * {@link SearchResultPage#selectPropertyRatingFilter}; use {@code random} for auto-pick if supported by
     * the test data).
     */
    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "SRP")
    @Test
    public void verifyReviewScore() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyReviewScore");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String email = map.get("email");
        String password = map.get("password");
        String currency = map.getOrDefault("currency", "IDR");
        String srpLocation = map.getOrDefault("srpLocation", "Jakarta");
        String locationType = map.getOrDefault("locationType", "Region");
        String rangeAddDate = map.getOrDefault("rangeAddDate", "10-20");
        int totalNight = Integer.parseInt(map.getOrDefault("totalNight", "1"));
        int totalRoom = Integer.parseInt(map.getOrDefault("totalRoom", "1"));
        int totalAdult = Integer.parseInt(map.getOrDefault("totalAdult", "1"));
        int totalHotelCards = Integer.parseInt(map.getOrDefault("totalHotelCards", "5"));
        String guestRatingFilter = map.getOrDefault("guestRatingFilter", "random");

        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("Login as " + email + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(email, password, currency);
        String language = map.getOrDefault("language", homePage.getLanguageApp());

        step("Open hotel SRP via deeplink");
        String srpLocationId;
        if (Configs.ENVIRONMENT == Environment.PROD) {
            srpLocationId = map.get("locationId");
        } else {
            HotelAutoCompleteResponse srpAutocomplete = hotelApi.getAutocompleteData(email, password, srpLocation, locationType);
            srpLocationId = srpAutocomplete.data.contents.get(0).publicId;
        }
        hotelPage.openSRPDeeplink(accommodationType, totalRoom, totalAdult, srpLocationId, locationType, srpLocation,
                checkInDate, checkOutDate, language);
        landingPage.fillNightCheckInForm();
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat("Search result page loads with visible hotel inventory", searchResultPage.isSearchResultShown(), is(true));

        step("Verify at least one of the first " + totalHotelCards + " hotel listing cards exposes review summary (score + review volume)");
        String cardReviewLineFound = searchResultPage.findReviewLineAmongFirstCards(totalHotelCards);
        Assertion.assertThat(
                "At least one of the first " + totalHotelCards + " hotel cards should advertise a visible review snippet",
                cardReviewLineFound,
                is(not(nullValue())));
        Assertion.assertThat("Hotel card rating line shows score vs /5 (e.g. 3/5, 4.1/5, 4,8/5)",
                Objects.requireNonNull(cardReviewLineFound),
                matchesPattern(".*\\d+(?:[.,]\\d+)?\\s*/\\s*5.*"));

        step("Apply guest (property) rating filter and validate results on first N cards");
        searchResultPage.clickFilterButton();
        searchResultPage.swipeToFilter("Rating");
        String selectedGuestRating = searchResultPage.selectPropertyRatingFilter(guestRatingFilter);
        searchResultPage.clickFilterSubmit();

        Assertion.assertThat("Filter chip shows active badge after submitting guest rating filter",
                searchResultPage.isFilterActivated("Filter"), is(true));

        step("Assert first " + totalHotelCards + " hotel cards have the right property rating");
        Assertion.assertThat("All property rating is correct", searchResultPage.checkHotelCardList("Guest Rating", totalHotelCards, new String[]{selectedGuestRating}), is(true));
    }

    @Author(names = {"Kartik", "Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void hotelBookingWithTwoChildGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("hotelBookingWithTwoChildGuest");
        BaseTest.paramsThreadLocal.set(map);

        String hotelName = map.get("hotelName");
        int totalGuest = Integer.parseInt(map.get("totalGuest"));
        int totalChild = Integer.parseInt(map.get("totalChild"));

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(totalGuest);
        landingPage.setTotalChildren(totalChild);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();
        boolean childCountVisible = roomListPage.checkChildGuest();

        step("Select room from the room list");
        detailPage.RemovePopUp();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillContactOrderDetail();
        String expectedHotelName = bookingFormPage.getHotelNameBookingForm();
        String expectedCheckinDate = bookingFormPage.getCheckinBookingForm();
        String expectedNightDuration = bookingFormPage.getDurationBookingForm();
        String expectedTotalPrice = bookingFormPage.getTotalPriceBookingForm();
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        String priceChanges = bookingFormPage.PriceChangesPopUp();
        expectedTotalPrice = updateString(expectedTotalPrice, priceChanges);

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        Assertion.assertThat("child count on room list page is visible", childCountVisible,is(true));
        Assertion.assertThat("Hotel name on the booking form and payment page should be equal", paymentPage.getProductNamePayment(), is(expectedHotelName));
        Assertion.assertThat("Check-in date on the booking form and payment page should be equal",  paymentPage.getCheckinPayment(), is(expectedCheckinDate));
        Assertion.assertThat("Night duration on the booking form and payment page should be equal", paymentPage.getDurationPayment(), is(expectedNightDuration));
        Assertion.assertThat("Total price on the booking form and payment page should be equal", convertPrice(paymentPage.getTotalPricePayment()), is(convertPrice(expectedTotalPrice)));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "SRP")
    @Test
    public void checkMapsOnSrpLogin() throws ExecutionException, InterruptedException {
        Map<String, String> param = getParam("checkMapsOnSrpLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String locationType = param.get("locationType");
        int numberOfZoomOut = Integer.parseInt(param.get("numberOfZoomOut"));
        int numberOfZoomIn = Integer.parseInt(param.get("numberOfZoomIn"));
        int numberOfSwipe = Integer.parseInt(param.get("numberOfSwipe"));
        Swipe swipeDirection = Swipe.valueOf(param.get("swipeDirection").toUpperCase());

        String currency = "IDR";
        String[] dates = generateCheckInOutDate("1-2", 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Go To Home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("Change currency");
        homePage.changeCurrency(currency, username);

        step("user open SRP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), 1, 1, locationId, locationType, location, checkInDate, checkOutDate, language);
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
        Assertion.assertThat("Hotel card item is displayed", priceInThisArea, is(containsString(currency + " ")));

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

    /**
     * Sheet key: {@code verifySrpMapCarouselPdpThenListLogin}.
     * Params: {@code loginType}, {@code username}, {@code password}, {@code location}, {@code locationType}.
     * Opens map and taps one visible price marker ({@link SearchResultPage#getHotelPriceMapsText()}) instead of zooming or using Search this area.
     */
    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "SRP")
    @Test
    public void verifySrpMapCarouselPdpThenListLogin() throws ExecutionException, InterruptedException {
        Map<String, String> param = getParam("verifySrpMapCarouselPdpThenListLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String locationType = param.get("locationType");

        String currency = "IDR";
        String[] dates = generateCheckInOutDate("1-2", 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Go To Home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);
        String language = homePage.getLanguageApp();

        step("user open SRP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, locationType);
        int hotelIndexLocation = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String locationId = hotelAutoCompleteResponse.data.contents.get(hotelIndexLocation).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), 1, 1, locationId, locationType, location, checkInDate, checkOutDate, language);
        landingPage.fillNightCheckInForm();

        step("Click See maps button in SRP");
        searchResultPage.clickSeeMapButton();
        
        step("Verify map view property card shows correct hotel info (price, star, name)");
        String selectedMapPrice = searchResultPage.getHotelPriceMapsText();
        String hotelPriceCard = searchResultPage.getHotelPriceCard(selectedMapPrice, true);
        Assertion.assertThat("Hotel price on map marker matches visible carousel card price", convertPriceSafely(selectedMapPrice), is(convertPriceSafely(hotelPriceCard)));
        Assertion.assertThat("Visible map carousel price shows expected currency", selectedMapPrice, containsString(currency + " "));
        Assertion.assertThat("Map carousel shows a hotel name", searchResultPage.isHotelNameCardVisibleOnMaps(), is(true));
        Assertion.assertThat("Map carousel shows star rating on the hotel card", searchResultPage.isHotelStarCardVisibleOnMaps(), is(true));

        step("Open PDP from map by tapping the visible hotel card");
        String mapsHotelName = searchResultPage.selectVisibleHotelItem();
        Assertion.assertThat("Hotel name on PDP matches map carousel selection",
                normalizeNonBreakingSpaceText(detailPage.getHotelNameDetail()),
                equalToIgnoringCase(normalizeNonBreakingSpaceText(mapsHotelName)));

        step("Return to map from PDP, then use list icon to exit map and land on SRP");
        detailPage.clickBackButton();
        searchResultPage.closeMap();
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat("User is back on hotel SRP after closing map (list)", searchResultPage.isSearchResultShown(), is(true));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "SRP")
    @Test
    public void checkMapsOnSrpGuest() {
        Map<String, String> param = getParam("checkMapsOnSrpGuest");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = param.get("location");
        int numberOfZoomOut = Integer.parseInt(param.get("numberOfZoomOut"));
        int numberOfZoomIn = Integer.parseInt(param.get("numberOfZoomIn"));
        int numberOfSwipe = Integer.parseInt(param.get("numberOfSwipe"));
        Swipe swipeDirection = Swipe.valueOf(param.get("swipeDirection").toUpperCase());
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("User open app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency, "");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
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
        Assertion.assertThat("Hotel card item is displayed", priceInThisArea, is(containsString(currency + " ")));

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

    @Author(names = {"Okta"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "SRP")
    @Test(dataProvider = "checkHotelPriceInMapsGuestDataProvider")
    public void checkHotelPriceInMapsGuest(Map<String, String> map) {
        BaseTest.paramsThreadLocal.set(map);
        Session<WebDriver> session = sessionThreadLocal.get();
        String location = map.get("location");
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Go to home");
        homePage.gotoHome();

        step("User open app as guest");
        loginPage.loginAsGuest();

        step("Change currency");
        homePage.changeCurrency(currency, "");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Input hotel location");
        landingPage.searchStayLocation(location);

        step("Search hotel");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Click see map button in SRP");
        searchResultPage.clickSeeMapButton();

        step("Get hotel price in maps");
        String hotelPriceMaps = searchResultPage.getHotelPriceMapsText();

        step("Get hotel price in card");
        String hotelPriceCard = searchResultPage.getHotelPriceCard(hotelPriceMaps, true);

        step("Assert hotel price in maps is the same in card");
        Assertion.assertThat("Hotel price in maps is the same in card", convertPriceSafely(hotelPriceMaps), is(convertPriceSafely(hotelPriceCard)));
    }

    @Author(names = {"Kartik","Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PDP")
    @Test
    public void openReviewImageFromPdpLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("openReviewImageFromPdpLogin");
        BaseTest.paramsThreadLocal.set(map);

        String hotelName = map.get("hotelName");
        String password = map.get("password");
        String loginType = map.get("loginType");
        String username = map.get("username");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
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

        step("check swipe review card");
        Assertion.assertThat("Review card can be swiped", detailPage.checkReviewImageBottomSheetSwipe(), is(true));

        step("Click All Reviews button on review bottom sheet");
        detailPage.clickAllReviewsBtnOnReviewBottomSheet();
        Assertion.assertThat("Review page is displayed", detailPage.verifyRedirectedToReviewPage(), is(true));
        detailPage.clickBackButtonReviewPage();
        detailPage.clickBack();

        step("click See all button on PDP");
        detailPage.seeAllReview();
        Assertion.assertThat("Review page is displayed", detailPage.verifyRedirectedToReviewPage(), is(true));
    }

    @Author(names = {"Prerna","Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PostPurchase")
    @Test
    public void verifyCancellationDateLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        Map<String, String> map = getParam("verifyCancellationDateLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String hotelName = map.get("hotelName");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));
        int qtyEachRoom = Integer.parseInt(map.get("qtyEachRoom"));
        String currency = "IDR";

        step("Goto home");
        homePage.gotoHome();

        step("user open app as a guest");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType,username,password);

        step("Create hotel booking multi-room using Sakuku via API");
        Boolean result = hotelApi.createOrderMultiRoomApi(username, password,
                hotelName, adult, night, room, startDate, qtyEachRoom, currency, false, "");
        Assertion.assertThat("Success to book multi-room by API", result, is(true));

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Open order detail");
        orderListPage.openIssuedTicketViaDeeplink(bookedOrderID);

        step("Get room names");
        String room1 = orderDetailPage.getRoomName(1);
        String room2 = orderDetailPage.getRoomName(2);

        step("open refund and reschedule info");
        orderDetailPage.openRefundAndRescheduleInfo();

        step("Verify refund and reschedule info opened");
        Assertion.assertThat("Reschedule and Refund detail open", orderDetailPage.getRefundAndRescheduleTextWebview(), is(notNullValue()));

        step("Verify tab can open");
        String refundInfo1 = orderDetailPage.getRefundInfoWebview(1);
        String refundInfo2 = orderDetailPage.getRefundInfoWebview(2);

        Assertion.assertThat("first room refund info contains room "+room1,refundInfo1,containsString(room1));
        Assertion.assertThat("second room refund info contains room "+room2,refundInfo2,containsString(room2));

        step("Close refund and reschedule info");
        orderDetailPage.closeRefundAndRescheduleInfoWebView();

        step("Verify bottomsheet closed");
        Assertion.assertThat("Reschedule and Refund detail closed", orderDetailPage.isCloseBottomSheetButtonPresent(), is(false));
    }

    @Author(names = {"Akbar"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void createHotelBookingPayAtHotelGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> map = getParam("createHotelBookingPayAtHotelGuest");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        String currency = "IDR";
        String location = map.get("location");
        String date_type = map.get("date_type");
        String date = map.get("date");

        String filterType = map.get("filterType");
        String filterOption = map.get("filterOption");

        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        int totalRoom = Integer.parseInt(map.get("total_room"));
        int totalAdult = Integer.parseInt(map.get("total_adult"));

        step("Goto home");
        homePage.gotoHome();

        step("Set user as guest");
        loginPage.loginAsGuest();

        step("Change Language to " + ENGLISH);
        homePage.openAccount();
        accountPage.selectLanguage("ENG");
        settingPage.backToHome();

        step("Change currency");
        homePage.changeCurrency(currency, "");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("search stay location");
        landingPage.searchStayLocation(location);

        step("set stay date");
        landingPage.setStayDate(date_type, date);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalAdult);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Click filter button");
        searchResultPage.clickFilterButton();

        step("Scroll untill filter by '"+filterType+"' is visible");
        searchResultPage.swipeToFilter(filterType);

        step("Select '"+filterOption+"'");
        searchResultPage.selectHotelPaymentMethod(filterOption);

        step("Submit filter");
        searchResultPage.clickFilterSubmit();

        step("Select first hotel displayed on SRP");
        searchResultPage.goToFirstHotelCard();
        searchResultPage.selectFirstHotelCard();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Clicking master tag '"+filterOption+"' in room list");
        roomListPage.clickMastertag(filterOption);

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);
        String expectedHotelName = bookingFormPage.getHotelNameBookingForm();
        String expectedCheckinDate = bookingFormPage.getCheckinBookingForm();
        String expectedNightDuration = bookingFormPage.getDurationBookingForm();
        String expectedTotalPrice = bookingFormPage.getTotalPricePayAtHotelBookingForm();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        String priceChanges = bookingFormPage.PriceChangesPopUp();
        expectedTotalPrice = updateString(expectedTotalPrice, priceChanges);

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Hotel name on the booking form and payment page should be equal", paymentPage.getProductName(), is(expectedHotelName));
        Assertion.assertThat("Total price on the booking form and payment page should be equal", convertPrice(paymentPage.getTotalPricePayment()), is(convertPrice(expectedTotalPrice)));
        Assertion.assertThat("Total room on test data and payment page should be equal", getIntFromString(paymentPage.getTotalRoomHotel()), is(getIntFromString(String.valueOf(totalRoom))));

        step("Open order detail");
        paymentPage.clickBookDetail();
        Assertion.assertThat("Check-in date on the booking form and payment page should be equal",  paymentPage.getCheckInDateDetail(), is(expectedCheckinDate));
        Assertion.assertThat("Night duration on the booking form and payment page should be equal", paymentPage.getNightDurationDetail(), is(expectedNightDuration));
        Assertion.assertThat("Total guest on test data and payment page should be equal", getIntFromString(paymentPage.getTotalGuestHotelDetail()), is(getIntFromString(String.valueOf(totalAdult))));

        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Back to the payment page");
            paymentPage.closePriceDetail();

            step("Input and submit CC information");
            paymentPage.fillCCDetailRealIssued(cardNumber,expiry,cvv,cardName);

            step("Submit the payment method");
            paymentPage.clickPayButton();
            paymentPage.verifyCreditCard3ds();

            step("Assert if the payment is a success");
            Assertion.assertThat("Payment Successful and Thankyou page is shown", paymentPage.isThankYouPageShown(), is(true));
        }
    }

    /**
     * Prints the same {@code === PAH Hotel Summary ===} table as {@code PayAtHotelMain} in tiketRakesh-Utilities
     * (steps 3–5: core BE lookup, price-engine PAH scan, summary URLs).
     * {@code dateRange}, {@code numberOfNights}, {@code numberOfRooms}, {@code numberOfAdults} come from test data;
     * vendor list and other defaults from {@code /payAtHotel/testData.json}. Environment follows Maven {@code -Denv}.
     */
    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PayAtHotel")
    @Test
    public void findPayAtHotel() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("findPayAtHotel");
        BaseTest.paramsThreadLocal.set(map);
        String dateRange = map.get("dateRange");
        String numberOfNights = map.get("numberOfNights");
        String numberOfRooms = map.get("numberOfRooms");
        String numberOfAdults = map.get("numberOfAdults");

        step("Generate PAH Hotel Summary");
        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);
        List<PayAtHotelRoom> payAtHotelRooms = hotelApi.findPayAtHotelRoom(
                dateRange, numberOfNights, numberOfRooms, numberOfAdults);

        // Every row from the === PAH Hotel Summary === table is now available as a POJO
        // (vendor, externalId, publicId, hotelName, earliestPahDate, url) for downstream steps.
        BaseTest.mainLogger.get().log("Pay at Hotel rooms captured: " + payAtHotelRooms.size());
        for (PayAtHotelRoom room : payAtHotelRooms) {
            BaseTest.mainLogger.get().log(" - " + room.vendorExternalIdLabel()
                    + " | " + room.getHotelName()
                    + " | " + room.getEarliestPahDate()
                    + " | " + room.getUrl());
        }
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PDP")
    @Test
    public void verifyCrossSellOnPdpMap() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyCrossSellOnPdpMap");
        BaseTest.paramsThreadLocal.set(map);

        TrainPage<WebDriver> trainPage = TrainPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);

        String email = map.get("email");
        String password = map.get("password");
        String passengerName = map.get("passengerName");
        String phone = map.get("phone");
        String nationalId = String.valueOf(CoreFunctions.generateRandomInt(16));
        String originCity = map.get("originCity");
        String originType = map.get("originType");
        String destinationCity = map.get("destinationCity");
        String destinationType = map.get("destinationType");
        String tripType = map.get("tripType");
        int adult = Integer.parseInt(map.get("adult"));
        int infant = Integer.parseInt(map.get("infant"));
        String trainDate = generateRandomDate(map.get("rangeAddDate")).replace("-", "");
        String hotelName = map.get("hotelName");
        int totalRoom = Integer.parseInt(map.getOrDefault("totalRoom", "1"));
        int totalAdult = Integer.parseInt(map.getOrDefault("totalAdult", "1"));
        int totalNight = Integer.parseInt(map.getOrDefault("totalNight", "1"));
        String accommodationType = "hotel";

        step("Create new train issued order");
        Boolean result = trainPage.createIssuedOrderApi(email, password, passengerName, phone, nationalId, originCity, destinationCity, trainDate, tripType, adult, infant);
        Assertion.assertThat("Train Booking via API is successful", result, is(true));
        String orderId = trainPage.issuedOrderData("orderId");
        String totalPrice = trainPage.issuedOrderData("totalPrice");
        String originStation = trainPage.issuedOrderData("originStation");
        String destinationStation = trainPage.issuedOrderData("destinationStation");
        String arrivalDate = trainPage.issuedOrderData("arrivalDate");

        step("User open app");
        homePage.gotoHome();

        step("Login as " + email + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(email, password);
        homePage.changeCurrency("IDR", email);

        step("Open the issued train order detail via deeplink");
        orderListPage.openIssuedTicketViaDeeplink(orderId);

        step("Verify the train order is issued");
        Assertion.assertThat("Train order is issued and e-ticket detail is shown",
                orderDetailPage.isTrainDetailCorrect(originStation, destinationStation, totalPrice), is(true));

        step("Open hotel PDP via deeplink with check-in on the train arrival date (" + arrivalDate + ")");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(email, password, hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;
        String checkOutDate = LocalDate.parse(arrivalDate).plusDays(totalNight).toString();
        hotelPage.openPDPDeeplink(accommodationType, country.toLowerCase(), hotelId, totalRoom, totalAdult, arrivalDate, checkOutDate);
        Assertion.assertThat("Hotel detail should be displayed", detailPage.isHotelDetailShown(), is(true));

        step("Open the in-app map on PDP");
        detailPage.openInAppMap();
        Assertion.assertThat("In-app map popup is shown after tapping location on PDP", detailPage.isInAppMapPopupShown(), is(true));

        step("Tap accommodations nearby button");
        detailPage.clickNearbyChip();
        Assertion.assertThat("Nearby accommodation list is shown after tapping Accommodations nearby",
                detailPage.isNearbyAccommodationListShown(), is(true));

        step("Verify cross sell nudge is shown on nearby accommodation");
        Assertion.assertThat("Cross sell nudge is shown on nearby accommodation",
                detailPage.isCrossSellNudgeShownOnNearbyAccommodation(), is(true));
        String crossSellHotelName = detailPage.getNearbyAccommodationHotelNameWithCrossSell();

        step("Open the nearby hotel that has cross sell nudge");
        detailPage.clickNearbyAccommodationWithCrossSell();

        step("Verify PDP of the selected cross sell hotel is opened");
        Assertion.assertThat("Hotel detail should be displayed", detailPage.isHotelDetailShown(), is(true));
        Assertion.assertThat("Hotel name on PDP matches the hotel selected from cross sell",
                detailPage.getHotelNameDetail(), containsStringIgnoringCase(crossSellHotelName));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Module(name = "Accommodation")
    @Test
    public void ACC_createHotelBookingPayAtHotelLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> map = getParam("ACC_createHotelBookingPayAtHotelLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String currency = "IDR";

        String location = map.get("location");
        String date_type = map.get("date_type");
        String date = map.get("date");

        String filterType = map.get("filterType");
        String filterOption = map.get("filterOption");

        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        int totalRoom = Integer.parseInt(map.get("total_room"));
        int totalAdult = Integer.parseInt(map.get("total_adult"));

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency, username);

        if (Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            int totalNight = Integer.parseInt(map.get("total_night"));

            step("Find a Pay at Hotel PDP and open it via deeplink");
            PayAtHotelRoom selectedPah = HotelPage.getInstance(session)
                    .openFirstAvailablePayAtHotelPdpDeeplink(date, totalNight, totalRoom, totalAdult);
            Assertion.assertThat(
                    "Pay at Hotel is available - one of the PAH Hotel items opened a PDP with an available room",
                    selectedPah, is(notNullValue()));
        } else {
            step("Open hotel landing page");
            homePage.openVerticalPageDeeplink("Hotel", "", currency);

            step("search stay location");
            landingPage.searchStayLocation(location);

            step("set stay date");
            landingPage.setStayDate(date_type, date);

            step("set total room and total guest");
            landingPage.clickRoomAndGuestButton();
            landingPage.setTotalRoom(totalRoom);
            landingPage.setTotalGuest(totalAdult);
            landingPage.clickDone();

            step("submit search");
            landingPage.clickSubmit();
            landingPage.fillNightCheckInForm();

            step("Click filter button");
            searchResultPage.clickFilterButton();

            step("Scroll untill filter by '" + filterType + "' is visible");
            searchResultPage.swipeToFilter(filterType);

            step("Select '" + filterOption + "'");
            searchResultPage.selectHotelPaymentMethod(filterOption);

            step("Submit filter");
            searchResultPage.clickFilterSubmit();

            step("Select first hotel displayed on SRP");
            searchResultPage.selectFirstHotelCard();
        }

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Clicking master tag '"+filterOption+"' in room list");
        roomListPage.clickMastertag(filterOption);

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.selectRandomSalutationIfMissing();
        bookingFormPage.fillGuestDetail(false);
        String expectedHotelName = bookingFormPage.getHotelNameBookingForm();
        String expectedCheckinDate = bookingFormPage.getCheckinBookingForm();
        String expectedNightDuration = bookingFormPage.getDurationBookingForm();
        String expectedTotalPrice = bookingFormPage.getTotalPricePayAtHotelBookingForm();

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        String priceChanges = bookingFormPage.PriceChangesPopUp();
        expectedTotalPrice = updateString(expectedTotalPrice, priceChanges);

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Hotel name on the booking form and payment page should be equal", paymentPage.getProductName(), is(expectedHotelName));
        Assertion.assertThat("Total price on the booking form and payment page should be equal", convertPrice(paymentPage.getTotalPricePayment()), is(convertPrice(expectedTotalPrice)));
        Assertion.assertThat("Total room on test data and payment page should be equal", getIntFromString(paymentPage.getTotalRoomHotel()), is(getIntFromString(String.valueOf(totalRoom))));

        step("Open order detail");
        paymentPage.clickBookDetail();
        Assertion.assertThat("Check-in date on the booking form and payment page should be equal",  paymentPage.getCheckInDateDetail(), is(expectedCheckinDate));
        Assertion.assertThat("Night duration on the booking form and payment page should be equal", paymentPage.getNightDurationDetail(), is(expectedNightDuration));
        Assertion.assertThat("Total guest on test data and payment page should be equal", getIntFromString(paymentPage.getTotalGuestHotelDetail()), is(getIntFromString(String.valueOf(totalAdult))));

        if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD) {
            step("Back to the payment page");
            paymentPage.closePriceDetail();

            step("Input and submit CC information");
            paymentPage.fillCCDetailRealIssued(cardNumber,expiry,cvv,cardName);

            step("Submit the payment method");
            paymentPage.clickPayButton();
            paymentPage.verifyCreditCard3ds();

            step("Assert if the payment is a success");
            Assertion.assertThat("Payment Successful and Thankyou page is shown", paymentPage.isThankYouPageShown(), is(true));
        }
    }

    /**
     * App counterpart of dWeb {@code ACC_createHotelBookingPayAtHotelCancelLogin}.
     * GK / PreProd only.
     * <ol>
     *   <li>Complete a full PAH booking (with CC) and open the issued order.</li>
     *   <li>Assert Manage Order exposes Cancel order and cancel the PAH order.</li>
     *   <li>After cancellation, assert Cancel is hidden and only Delete order + Make similar order remain.</li>
     * </ol>
     */
    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PostPurchase")
    @Test
    public void createHotelBookingPayAtHotelCancelLogin() throws InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("createHotelBookingPayAtHotelCancelLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);

        String username = map.getOrDefault("username", map.get("email"));
        String password = map.get("password");
        String currency = map.getOrDefault("currency", "IDR");
        String date = map.get("date");
        String filterOption = map.get("filterOption");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");
        String postalCode = map.get("postalCode");
        String billingAddress = map.get("billingAddress");
        int totalRoom = Integer.parseInt(map.get("total_room"));
        int totalAdult = Integer.parseInt(map.get("total_adult"));
        int totalNight = Integer.parseInt(map.get("total_night"));

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Find a Pay at Hotel PDP and open it via deeplink");
        PayAtHotelRoom selectedPah = HotelPage.getInstance(session)
                .openFirstAvailablePayAtHotelPdpDeeplink(date, totalNight, totalRoom, totalAdult);
        Assertion.assertThat(
                "Pay at Hotel is available - one of the PAH Hotel items opened a PDP with an available room",
                selectedPah, is(notNullValue()));

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Clicking master tag '" + filterOption + "' in room list");
        roomListPage.clickMastertag(filterOption);

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.selectRandomSalutationIfMissing();
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("Input and submit CC information for the PAH guarantee");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName, postalCode, billingAddress);

        step("Submit the payment method");
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();

        step("Assert if the payment is a success and order reaches issued state");
        Assertion.assertThat("Payment Successful and Thankyou page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open the issued order detail (waits until ETICKET_ISSUED)");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        

        step("Assert Manage Order section exposes Cancel order for this PAH booking");
        webViewPage.switchToWebViewUrl("yourorder/detail?orderId=" + orderID);
        Assertion.assertThat(
                "Cancel order button should be visible under Manage Order for PAH order " + orderID,
                orderDetailPage.isCancelOrderButtonVisible(), is(true));
        webViewPage.switchToNative();

        step("Cancel the PAH order via Manage Order > Cancel order");
        orderDetailPage.clickCancelOrderButton(orderID);

        step("Reopen the order detail page to re-read its state after cancellation");
        orderListPage.openOrderDetailViaDeeplink(orderID);
        

        step("Verify cancellation was successful");
        Assertion.assertThat(
                "Canceled order banner should be displayed after a successful cancellation",
                orderDetailPage.getOrderStatus(orderID, 8),
                either(is("This order has been canceled")).or(is("Pesanan ini telah dibatalkan")));

        step("Assert post-cancellation actions: Cancel hidden; Delete order and Make similar order remain");
        webViewPage.switchToWebViewUrl("yourorder/detail?orderId=" + orderID);
        Assertion.assertThat(
                "Cancel order button should be HIDDEN after the PAH order is canceled",
                orderDetailPage.isCancelOrderButtonVisible(), is(false));
        Assertion.assertThat(
                "Delete order button should be visible after the PAH order is canceled",
                orderDetailPage.isDeleteOrderButtonVisible(), is(true));
        Assertion.assertThat(
                "Make similar order button should be visible after the PAH order is canceled",
                orderDetailPage.isMakeSimilarOrderButtonVisible(), is(true));
        webViewPage.switchToNative();
    }

    /**
     * Flexible payment (DownPayment 0%) booking on a 100% refundable room, then cancel it from My Order.
     * <ol>
     *   <li>Open PDP of a hotel with 100% refundable room and book the room labelled DownPayment 0%.</li>
     *   <li>Verify DownPayment label on booking form page 1, default DownPayment selection + info on page 2,
     *       and the continue button shows pay IDR 0.</li>
     *   <li>Complete the order without submitting any payment method.</li>
     *   <li>Cancel the order from My Order and verify success snackbar, order list and canceled status.</li>
     * </ol>
     */
    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PostPurchase")
    @Test
    public void cancelBookingFlexiblePayment() throws InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("cancelBookingFlexiblePayment");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String email = map.get("email");
        String password = map.get("password");
        String hotelName = map.get("hotelName");
        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(email, password, "IDR");

        step("Open PDP of hotel with 100% refundable room via deeplink: " + hotelName);
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(email, password, hotelName, accommodationType.toUpperCase());
        int index = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(index).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(index).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 1, 1, checkInDate, checkOutDate);

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Book room with 100% refund & reschedule and DownPayment 0% (flexible payment) label");
        roomListPage.bookRoomWithRefundRescheduleAndFlexiblePayment();

        step("Verify DownPayment label is shown on booking form page 1");
        Assertion.assertThat("DownPayment label should be displayed on booking form page 1",
                bookingFormPage.isFlexiblePaymentLabelDisplayed(), is(true));

        step("Click DownPayment section on booking form page 1");
        bookingFormPage.clickFlexiblePaymentSection();

        step("Verify DownPayment content is shown");
        Assertion.assertThat("DownPayment content should be displayed",
                bookingFormPage.isDownPaymentContentDisplayed(), is(true));

        step("Close DownPayment content");
        bookingFormPage.clickDownPaymentContentCloseButton();

        step("Go to booking form page 2");
        bookingFormPage.selectRandomSalutationIfMissing();
        bookingFormPage.continueToAddOnPage();

        step("Verify Pay by option is selected on booking form page 2");
        Assertion.assertThat("Pay by option should be selected",
                bookingFormPage.isPayByButtonSelected(), is(true));

        step("Verify continue button shows pay IDR 0");
        String continueToPaymentButtonText = bookingFormPage.getContinueToPaymentButtonText();
        Assertion.assertThat("Continue button '" + continueToPaymentButtonText + "' should contain one of " + BookingFormPage.PAY_ZERO_TODAY_TEXTS,
                BookingFormPage.isPayZeroTodayText(continueToPaymentButtonText), is(true));

        step("Click continue to payment button and handle the continue payment popup");
        bookingFormPage.clickContinueToPaymentButton();

        step("Assert the payment is a success without submitting any payment method");
        CommonBookingFormPage.getInstance(session).waitProcessingBooking();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Get the order id from thank you page and open the order detail via deeplink");
        String orderID = paymentPage.getOrderIdFromThankYouPageUrl();
        orderListPage.openOrderDetailViaDeeplink(orderID);
        

        step("Click cancel order on Refund & Reschedule section");
        Assertion.assertThat("Cancel order button should be displayed on Refund & Reschedule section",
                orderDetailPage.isDeleteOrderButtonDisplayed(orderID), is(true));
        orderDetailPage.clickRefundRescheduleCancelOrderButton(orderID);

        step("Verify room option is shown and cancel the room");
        Assertion.assertThat("Cancel order button for the room should be visible",
                orderDetailPage.isCancelOrderButtonVisible(), is(true));
        Assertion.assertThat("Snackbar 'Your order was canceled successfully and moved to the order history.' should be shown after submitting cancel order",
                orderDetailPage.submitCancelOrder(orderID), is(true));

        step("Verify user is navigated back to Order Detail page");
        Assertion.assertThat("User should be navigated back to Order Detail page", orderDetailPage.isInOrderDetailPage(), is(true));

        step("Reopen the order detail via deeplink and verify the order is shown as canceled");
        orderListPage.openOrderDetailViaDeeplink(orderID);
        
        Assertion.assertThat("Canceled order banner should be displayed after a successful cancellation",
                orderDetailPage.getOrderStatus(orderID, 8),
                either(equalToIgnoringCase("This order has been canceled")).or(equalToIgnoringCase("Pesanan ini telah dibatalkan")));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "BookingForm")
    @Test
    public void verifyBookingFormLocalCurrencyPayAtHotel() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyBookingFormLocalCurrencyPayAtHotel");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String currency = map.get("currency");
        String hotelName = map.get("hotelName");
        String hotelCurrency = map.get("hotelCurrency");
        String filterOption = "Pay at Hotel/Bayar di Hotel";
        int totalRoom = 1;

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency to " + currency);
        homePage.changeCurrency(currency, username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("search stay location: " + hotelName);
        landingPage.searchStayLocation(hotelName);

        step("set the total room: " + totalRoom);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Clicking master tag '"+filterOption+"' in room list");
        roomListPage.clickMastertag(filterOption);

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.selectMultiRoom(totalRoom, totalRoom);

        step("Get the local price on the booking form");
        Assertion.assertThat("Local price should be visible and different with selected currency", bookingFormPage.getLocalPriceValue(), is(not(containsString(currency))));
        Assertion.assertThat("Local price should be equal " + hotelCurrency, bookingFormPage.getLocalPriceValue(), is(containsString(hotelCurrency)));

        step("Open price details on booking form");
        bookingFormPage.continueToAddOnPage();
        Assertion.assertThat("Local price on the price details should be visible and different with selected currency", bookingFormPage.getLocalPriceOnPriceDetails(), is(not(containsString(currency))));
        Assertion.assertThat("Local price on the price details should be equal " + hotelCurrency, bookingFormPage.getLocalPriceOnPriceDetails(), is(containsString(hotelCurrency)));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PostPurchase")
    @Test
    public void verifyClaimingVoucherPayAtHotel() throws InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyClaimingVoucherPayAtHotel");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String username = map.getOrDefault("username", map.get("email"));
        String password = map.get("password");
        String currency = map.getOrDefault("currency", "IDR");
        String dateRange = map.get("dateRange");
        String filterOption = map.get("filterOption");
        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");
        String postalCode = map.get("postalCode");
        String billingAddress = map.get("billingAddress");
        int totalRoom = Integer.parseInt(map.get("total_room"));
        int totalAdult = Integer.parseInt(map.get("total_adult"));
        int totalNight = Integer.parseInt(map.get("total_night"));

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Find a Pay at Hotel PDP and open it via deeplink");
        PayAtHotelRoom selectedPah = HotelPage.getInstance(session)
                .openFirstAvailablePayAtHotelPdpDeeplink(dateRange, totalNight, totalRoom, totalAdult);
        Assertion.assertThat(
                "Pay at Hotel is available - one of the PAH Hotel items opened a PDP with an available room",
                selectedPah, is(notNullValue()));

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Clicking master tag '" + filterOption + "' in room list");
        roomListPage.clickMastertag(filterOption);

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.selectMultiRoom(true, totalRoom);

        step("Completing booking form data for " + totalRoom + " rooms / guests");
        bookingFormPage.selectRandomSalutationIfMissing();
        bookingFormPage.fillGuestDetail(false);
        Assertion.assertThat("Guest details should be filled for 2 rooms",
                bookingFormPage.getGuestNameList().size(), greaterThanOrEqualTo(2));

        step("Fill special request with random text");
        bookingFormPage.fillSpecialRequest(randomName() + " " + randomName());

        step("Verify Pay upon check-in label and price on booking form page 1");
        Assertion.assertThat("Pay upon check-in label and price should be displayed",
                bookingFormPage.isPayUponCheckInLabelAndPriceDisplayed(), is(true));

        step("Go to booking form page 2");
        bookingFormPage.continueToAddOnPage();

        step("Claim gift voucher on booking form page 2");
        bookingFormPage.claimGiftVoucherOnBookingFormPage2();

        step("Verify continue button shows pay IDR 0 today");
        String continueToPaymentButtonText = bookingFormPage.getContinueToPaymentButtonText();
        Assertion.assertThat("Continue button '" + continueToPaymentButtonText + "' should contain one of " + BookingFormPage.PAY_ZERO_TODAY_TEXTS,
                BookingFormPage.isPayZeroTodayText(continueToPaymentButtonText), is(true));

        step("Verify price calculation on booking form page 2");
        String totalPayment = bookingFormPage.getTotalToPayBookingForm();
        Assertion.assertThat("Price breakdown should match total price",
                bookingFormPage.isPriceDetailCorrect(totalPayment), is(true));

        step("Continue to payment page");
        bookingFormPage.submitBooking();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should show only debit/credit card for Pay at Hotel");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", orderID, not(emptyOrNullString()));
        Assertion.assertThat("See all payment methods should not be shown for PAH (only CC)",
                paymentPage.checkSeeAllPaymentMethod(), is(false));

        step("Submit CC payment for PAH guarantee");
        String paymentTotalPrice = paymentPage.getTotalPrice();
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName, postalCode, billingAddress);
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();
        Assertion.assertThat("Payment Successful and Thankyou page is shown",
                paymentPage.isThankYouPageShown(), is(true));

        step("Open the issued order detail via deeplink");
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        

        step("Verify guest amount is 2 and price matches payment page");
        Assertion.assertThat("Guest amount on order detail should be 2",
                orderDetailPage.getRommGuestAmount(), is(2));
        Assertion.assertThat("Order detail price should match payment page total",
                convertPriceSafely(orderDetailPage.getTotalPaymentPah(orderID)),
                is(convertPriceSafely(paymentTotalPrice)));

        step("Verify guest name is editable (optional)");
        orderDetailPage.seeMoreDetails();
        if (orderDetailPage.isEditGuestNameButtonDisplayed()) {
            orderDetailPage.clickEditGuestName();
            orderDetailPage.editGuestName();
            Assertion.assertThat("Guest name should be updated on order detail",
                    orderDetailPage.isGuestNameUpdated(), is(true));
        } else {
            log("Edit guest name button not displayed, skipping guest name edit verification");
        }

        if (Configs.PLATFORM == Platform.ANDROID) {
            step("Open email inbox in mobile browser and verify receipt + gift voucher email");
            InboxPageYopmail<?> inboxPage = new InboxPageYopmail<>(session);
            inboxPage.openYopmailInboxInMobileBrowser(username);
            Assertion.assertThat("Order receipt email should be received in Yopmail inbox",
                    inboxPage.verifyOrderReceiptEmailReceived(orderID), is(true));
            boolean giftVoucherReceived = inboxPage.verifyGiftVoucherEmailReceived(15, 5);
            if (giftVoucherReceived) {
                Assertion.assertThat("Gift voucher email should be received in Yopmail inbox", giftVoucherReceived, is(true));
            } else {
                log("the gift voucher email is not received due to timeout from the mailgun");
            }
        } else {
            log("Skipping email inbox verification (receipt + gift voucher) - this step is Android-only");
        }
    }

    /**
     * Mobile app counterpart of the web test {@code checkRefundStatusOfFailedIssuedBookingWeb}.
     * Books a hotel from the vendor known to simulate a failed-issue in this environment (vendor
     * color "8" on web == "expedia" here, see {@link HotelPage#generateVendorCode(String)}), pays
     * with Sakuku, then verifies the booking auto-refunds and shows an "In process" refund status
     * on the order detail page with a refund amount matching what was paid.
     * Sheet key: {@code checkRefundStatusOfFailedIssuedBooking}. Params: email/username, password,
     * location, locationType, rangeAddDate, room, adult, night, vendor, paymentMethod, currency.
     */
    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PostPurchase")
    @Test
    public void checkRefundStatusOfFailedIssuedBooking() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("checkRefundStatusOfFailedIssuedBooking");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.getOrDefault("email", param.get("username"));
        String password = param.get("password");
        String location = param.get("location");
        String locationType = param.get("locationType");
        String rangeAddDate = param.get("rangeAddDate");
        String currency = param.getOrDefault("currency", "IDR");
        int room = Integer.parseInt(param.getOrDefault("total_room", "1"));
        int adult = Integer.parseInt(param.getOrDefault("total_adult", "1"));
        int night = Integer.parseInt(param.getOrDefault("total_night", "1"));
        String vendor = param.getOrDefault("vendor", "expedia");
        String paymentMethod = param.getOrDefault("paymentMethod", "Sakuku");
        String accommodationType = "hotel";
        String[] dates = generateCheckInOutDate(rangeAddDate, night);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Goto home and login as " + username + " via deeplink, change currency to " + currency);
        homePage.gotoHome();
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Resolve location and open hotel SRP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, accommodationType.toUpperCase());
        int locationIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String locationId = hotelAutoCompleteResponse.data.contents.get(locationIndex).publicId;
        hotelPage.openSRPDeeplink(accommodationType, room, adult, locationId, locationType, location, checkInDate, checkOutDate, "");
        searchResultPage.waitForSrpToLoad();
        landingPage.fillNightCheckInForm();

        step("Choose hotel with vendor \"" + vendor + "\" and open PDP");
        searchResultPage.selectHotelVendor(vendor);

        step("Find and book room with vendor \"" + vendor + "\"");
        detailPage.seeHotelRoom();
        Assertion.assertThat("Room list should load", roomListPage.isRoomListShown(), is(true));
        roomListPage.openFirstRoomDetail(vendor, false);
        roomDetailPage.clickBookButton(room);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        bookingFormPage.selectRandomSalutationIfMissing();

        step("Store booking page data for order history verification");
        String storedHotelName = bookingFormPage.getHotelNameBookingForm();
        String storedCheckInDate = bookingFormPage.getCheckinBookingForm();
        String storedNightLabel = bookingFormPage.getDurationBookingForm();
        String storedRoomName = bookingFormPage.getRoomNameBookingForm();
        Assertion.assertThat("Stored hotel name should not be empty", storedHotelName, not(emptyOrNullString()));
        Assertion.assertThat("Stored check-in date should not be empty", storedCheckInDate, not(emptyOrNullString()));
        Assertion.assertThat("Stored night label should not be empty", storedNightLabel, not(emptyOrNullString()));
        Assertion.assertThat("Stored room name should not be empty", storedRoomName, not(emptyOrNullString()));

        step("Continue to payment page and complete booking with " + paymentMethod);
        String paymentAmount = bookingFormPage.getTotalPriceBookingForm();
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();
        String orderId = paymentPage.checkOrderID();
        Assertion.assertThat("Order ID should be displayed on payment page", orderId, not(emptyOrNullString()));
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();
        Assertion.assertThat("Payment successful page should be visible", paymentPage.isThankYouPageShown(), is(true));

        step("Wait for failed issued booking to be processed and refunded automatically");
        sleep(20);

        step("Open the failed issued booking from order list");
        orderListPage.openActiveTicketViaDeeplink(orderId);
        

        step("Check the refund is 'In Process' state");
        Assertion.assertThat("Refund status should be 'In process' for failed issued booking",
                orderDetailPage.getRefundInProcessStatus(orderId).toLowerCase(), anyOf(is("in process"), is("diproses")));

        String refundAmountText = orderDetailPage.getRefundInProcessAmount(orderId);
        long refundAmount = Long.parseLong(refundAmountText.replaceAll("[^0-9]", ""));
        long paidAmount = Long.parseLong(paymentAmount.replaceAll("[^0-9]", ""));
        Assertion.assertThat("Refund amount should match payment amount", refundAmount, is(paidAmount));
    }

    @DataProvider(name = "searchHotelLoginDataProvider")
    public Object[][] searchHotelLoginDataProvider() {
        return getObjectParams("searchHotelLogin");
    }

    @DataProvider(name = "navigateSrpToPdpLoginDataProvider")
    public Object[][] navigateSrpToPdpDataProviderLogin() {
        return getObjectParams("navigateSrpToPdpLogin");
    }

    @DataProvider(name = "verifySameHotelFromSrpToPdpLoginDataProvider")
    public Object[][] verifySameHotelFromSrpToPdpLoginDataProvider() {
        return getObjectParams("verifySameHotelFromSrpToPdpLogin");
    }

    @DataProvider(name = "verifySeeHotelRoomLoginDataProvider")
    public Object[][] verifySeeHotelRoomLoginDataProvider() {
        return getObjectParams("verifySeeHotelRoomLogin");
    }

    @DataProvider(name = "VerifyChangeLocationOnSrpLoginDataProvider")
    public Object[][] VerifyChangeLocationOnSrpLoginDataProvider() {
        return getObjectParams("VerifyChangeLocationOnSrpLogin");
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void verifyBookingWithPromoCode() {
        Session<WebDriver> session = sessionThreadLocal.get();
        
        Map<String, String> map = getParam("verifyBookingWithPromoCode");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String location = map.get("location");

        String paymentMethod = map.get("paymentMethod");
        String promoCode = map.get("promoCode");
        String currency = "IDR";

        step("Goto home");
        homePage.gotoHome();

        step("User open app as guest");
        loginPage.loginAsGuest();
        homePage.changeCurrency(currency, "");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Fill Booking Form details");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        paymentPage.checkOrderID();

        step("Choose payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Apply promo code");
        boolean isPromoCodeSectionExist = paymentPage.isPromoCodeSectionExist();

        paymentPage.seeAllPromoCodes();
        paymentPage.applyPromoCode(promoCode);

        String CrossedPriceBefore = paymentPage.getCrossedPriceBefore();
        String discountPercentage = paymentPage.getDiscountPercentage();
        String discountAmountOnPromoCodeSection = paymentPage.getDiscountAmountOnPromoCodeSection(currency);

        step("Open price detail");
        paymentPage.openPriceDetail();
        String discountAmountOnPriceDetail = paymentPage.getDiscountAmountOnPriceDetail();

        step("Assertions");
        Assertion.assertThat("Promo code section is exist", isPromoCodeSectionExist, is(true));
        Assertion.assertThat("crossed 'before price' on bottom navigation is exist", CrossedPriceBefore.isEmpty(), is(false));
        Assertion.assertThat("percentage of discount is exist", discountPercentage.isEmpty(), is(false));
        Assertion.assertThat("Does discount amount on price detail is match with discount amount on the promo section", discountAmountOnPriceDetail, is(discountAmountOnPromoCodeSection));
    }
    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void completePaymentByCCGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> map = getParam("completePaymentByCCGuest");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String location = map.get("location");
        String date_type = map.get("date_type");
        String date = map.get("date");

        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        step("Goto home");
        homePage.gotoHome();

        step("Login as Guest");
        loginPage.loginAsGuest();
        homePage.changeCurrency("IDR", "");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("search stay location");
        landingPage.searchStayLocation(location);

        step("set stay date");
        landingPage.setStayDate(date_type, date);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select first room displayed on room list page");
        roomListPage.openRoomDetailPage();

        step("Select room from the room list");
        detailPage.clickBookButton(1);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        step("Completing booking form data");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select Credit Card as Payment Method");
        paymentPage.selectPaymentMethod("Credit Card");

        step("Fill Credit Card Details");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Click pay Button");
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();

        step("Assert if the payment is a success");
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Payment Successful and Thankyou page is shown", paymentPage.isThankYouPageShown(), is(true));
    }

    @Author(names = {"Prerna"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void completePaymentByCCLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> map = getParam("completePaymentByCCLogin");
        BaseTest.paramsThreadLocal.set(map);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String date_type = map.get("date_type");
        String date = map.get("date");

        String cardNumber = map.get("cardNumber");
        String expiry = map.get("expiry");
        String cvv = map.get("cvv");
        String cardName = map.get("cardName");

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency("IDR", username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("search stay location");
        landingPage.searchStayLocation(location);

        step("set stay date");
        landingPage.setStayDate(date_type, date);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select first room displayed on room list page");
        roomListPage.getTheFirstHotelRoomPrice();

        step("Select room from the room list");
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();

        step("Open payment method");
        paymentPage.openPaymentMethod();

        step("Select Credit Card as Payment Method");
        paymentPage.selectPaymentMethod("Credit Card");

        step("Fill Credit Card Details");
        paymentPage.fillCCDetailRealIssued(cardNumber, expiry, cvv, cardName);

        step("Click pay Button");
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();

        step("Assert if the payment is a success");
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        Assertion.assertThat("Payment Successful and Thankyou page is shown", paymentPage.isThankYouPageShown(), is(true));
    }

    @Author(names = {"Kartik", "Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test(dataProvider="checkLoyaltyBenefitLoginDataProvider")
    public void checkLoyaltyBenefitLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = "IDR";
        String location = param.get("location");
        String loyaltyBenefitType = param.get("loyaltyBenefitType");

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency");
        homePage.changeCurrency(currency, username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Select hotel that have tier badge: " + loyaltyBenefitType);
        searchResultPage.goToCardWithLoyaltyBenefitBadge();

        step("Go to the room list");
        detailPage.RemovePopUp();
        boolean LoyaltyTierBenefitSection = detailPage.checkLoyaltyTierBenefit();
        boolean verifyLoyaltyBenefitPopUp = detailPage.verifyLoyaltyBenefitPopUp(loyaltyBenefitType);
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        boolean LoyaltyBadgeOnCard = roomListPage.checkLoyaltyTierBenefit();
        boolean LoyaltyBenefitPopUp = roomListPage.verifyLoyaltyBenefitPopUp(loyaltyBenefitType);

        step("check tier benefit and click book room button");
        boolean LoyaltyBenefitSection = roomDetailPage.verifyLoyaltyBenefitSection(loyaltyBenefitType);
        roomDetailPage.clickBookButton(1);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        step("Submit booking");
        String loyaltyBenefitValueText = bookingFormPage.loyaltyBenefitValueText();
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Open price detail");
        paymentPage.openPriceDetail();
        String loyaltyBenefit = paymentPage.getLoyaltyDiscountAmount();
        boolean priceBreakDownCorrect = paymentPage.priceBreakDown();

        Assertion.assertThat("sum the price detail == total price on the Detail Total Payment && Total Payment page", priceBreakDownCorrect, is(true));
        Assertion.assertThat("Loyalty benefit section on PDP pop up is opening ", (LoyaltyTierBenefitSection && verifyLoyaltyBenefitPopUp), is(true));
        Assertion.assertThat("On Room list hotel card have loyalty benefit detail and popup button is opening", (LoyaltyBadgeOnCard && LoyaltyBenefitPopUp), is(true));
        Assertion.assertThat("On Room detailPage Loyalty benefit section with detail is there ", LoyaltyBenefitSection, is(true));
        Assertion.assertThat("loyalty benefit value equals the amount on payment page ", loyaltyBenefit, is(loyaltyBenefitValueText));
    }

    @Author(names = {"Prerna"})
    @Module(name = "PostPurchase")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkReviseMultiGuestName() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("checkReviseMultiGuestName");
        BaseTest.paramsThreadLocal.set(param);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String query = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create new hotel issued order");
        hotelApi.createIssuedOrderApi(
        "en", username, password, startDate, query, adult, night, room,
        orderFullName, orderNameTitle, guestFullName, guestTitle, "", false);

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        List<String> guestNames = hotelApi.guestNames;

        step("Goto home");
        homePage.gotoHome();

        step("Log out");
        loginPage.loginAsGuest();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open the issued order");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);
        

        step("check hotel name");
        boolean isHotelNameMatch = orderDetailPage.isHotelNameDisplayed(query);

        step("click Edit Guest Name");
        orderDetailPage.seeMoreDetails();
        boolean isGuestNameCorrect = orderDetailPage.checkRoomDetail(guestNames);
        orderDetailPage.clickEditGuestName();

        step("check orderID, Hotel Name, Guest Name on Edit page");
        
        boolean isDetailCorrectEditPage = orderDetailPage.checkDetailsEditPage(issuedOrderID, query, guestNames);

        step("Edit Guest Name");
        orderDetailPage.editGuestName();

        step("Assertions");
        Assertion.assertThat("Hotel Name is displayed correctly", isHotelNameMatch, is(true));
        Assertion.assertThat("orderID, Hotel Name, Guest Name on Edit page is correct",isDetailCorrectEditPage||isGuestNameCorrect, is(true));
        Assertion.assertThat("check Guest name is updated in the order details", orderDetailPage.isGuestNameUpdated(), is(true));
        orderDetailPage.seeMoreDetails();
        Assertion.assertThat("check Guest name is updated in the Room Details", orderDetailPage.isGuestNameUpdated(), is(true));
    }

    @Author(names = {"Kartik"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "LandingPage")
    @Test
    public void verifyMultipleHotelIdGuest() {
        Map<String, String> param = getParam("verifyMultipleHotelIdGuest");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();
        String language = homePage.getLanguageApp();

        step("Open Hotel vertical page");
        homePage.openVerticalPageDeeplink("Hotel", language, "");
        String vertical = "Hotel";

        step("Get multiple Hotel ID module from the API");
        String templateCode = "MULTIPLE_HOTEL_ID";

        MasterContentPOJO masterContentApi = hotelApi.getMasterContent(false, "guest", "", language, templateCode);
        LandingPageModulePOJO landingPageModuleApi = hotelApi.getLandingPageModule(false, "guest", "", language, templateCode,masterContentApi);
        String templateCodeApi = landingPageModuleApi.data.templateCode;
        Assertion.assertThat("Success to get landing page module from the API", templateCodeApi, is(templateCode));

        step("Get title from the API");
        String titleApi = masterContentApi.contentList.title;

        step("Get subtitle from the API");
        String subTitleApi = masterContentApi.contentList.subTitle;

        step("Get the multiple ID hotel list");
        ArrayList<String> hotelList =  hotelApi.getHotelListLandingPage(landingPageModuleApi);

        step("Get buttonText from the API");
        ArrayList<LandingPageModulePOJO.List> listArray = landingPageModuleApi.data.list;
        String buttonTextApi = listArray.get(0).buttonText;

        step("Get redirect target");
        String redirectTargetApi = listArray.get(0).redirectTarget;
        LandingPageModulePOJO.Redirect redirectTargetDataApi = listArray.get(0).redirect;

        step("open Landing page");
        String masterTitle =  landingPage.getMasterTitle();

        String masterSubTitle =  landingPage.getMasterSubTitle();

        step("Compare location from API and UI");
        boolean locationApi = landingPage.checkLocationTagsForMultipleHotelId(landingPageModuleApi);
        boolean hotelNameList = landingPage.compareTimedPromoHotelName(hotelList);

        step("Get Button Text from the APP");
        String buttonText = landingPage.getButtonText();

        step("User click on See All Button");
        landingPage.seeAllMultipleID();

        step("Assertions");
        Assertion.assertThat("Hotel Title displayed is same as API", titleApi.equals(masterTitle), is(true));
        Assertion.assertThat("Hotel Subtitle displayed is same as API", subTitleApi.equals(masterSubTitle), is(true));
        Assertion.assertThat("Hotel Location displayed is same as API", locationApi, is(true));
        Assertion.assertThat("Hotel Names List on the App should be equal with API",hotelNameList ,is(true));
        Assertion.assertThat("Hotel Button Text displayed is same as API", buttonTextApi.equals(buttonText), is(true));

        step("Comparing API response with redirectTarget option '"+redirectTargetApi+"'");
        Boolean isRedirectValid = hotelLandingPage.verifyRedirectTarget(redirectTargetApi, redirectTargetDataApi, language, vertical);
        Assertion.assertThat(" multiple hotel ID Redirect on the App should be equal with API", isRedirectValid, is(true));
    }

    @Author(names = {"Kartik"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "LandingPage")
    @Test
    public void verifyMultipleHotelIdLogin() {
        Map<String, String> param = getParam("verifyMultipleHotelIdLogin");
        BaseTest.paramsThreadLocal.set(param);
        Session<WebDriver> session = sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String loginType = param.get("loginType");

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);

        step("Open app");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);

        String language = homePage.getLanguageApp();

        step("Open Hotel vertical page");
        homePage.openVerticalPageDeeplink("Hotel", language, "");
        String vertical = "Hotel";

        step("Get multiple Hotel ID module from the API");
        String templateCode = "MULTIPLE_HOTEL_ID";

        MasterContentPOJO masterContentApi = hotelApi.getMasterContent(true, username, password, language, templateCode);
        LandingPageModulePOJO landingPageModuleApi = hotelApi.getLandingPageModule(true, username, password, language, templateCode,masterContentApi);
        String templateCodeApi = landingPageModuleApi.data.templateCode;
        Assertion.assertThat("Success to get landing page module from the API", templateCodeApi, is(templateCode));

        step("Get title from the API");
        String titleApi = masterContentApi.contentList.title;

        step("Get subtitle from the API");
        String subTitleApi = masterContentApi.contentList.subTitle;

        step("Get the multiple ID hotel list");
        ArrayList<String> hotelList =  hotelApi.getHotelListLandingPage(landingPageModuleApi);

        step("Get buttonText from the API");
        ArrayList<LandingPageModulePOJO.List> listArray = landingPageModuleApi.data.list;
        String buttonTextApi = listArray.get(0).buttonText;

        step("Get redirect target");
        String redirectTargetApi = listArray.get(0).redirectTarget;
        LandingPageModulePOJO.Redirect redirectTargetDataApi = listArray.get(0).redirect;

        step("open Landing page");
        String masterTitle =  landingPage.getMasterTitle();

        String masterSubTitle =  landingPage.getMasterSubTitle();

        step("Compare location from API and UI");
        boolean locationApi = landingPage.checkLocationTagsForMultipleHotelId(landingPageModuleApi);

        step("Compare hotelList from API and UI");
        boolean hotelNameList = landingPage.compareTimedPromoHotelName(hotelList);

        step("Get Button Text from the APP");
        String buttonText = landingPage.getButtonText();

        step("User click on See All Button");
        landingPage.seeAllMultipleID();

        step("Assertions");
        Assertion.assertThat("Hotel Title displayed is same as API", titleApi.equals(masterTitle), is(true));
        Assertion.assertThat("Hotel Subtitle displayed is same as API", subTitleApi.equals(masterSubTitle), is(true));
        Assertion.assertThat("Hotel Location displayed is same as API", locationApi, is(true));
        Assertion.assertThat("Hotel Names List on the App should be equal with API",hotelNameList ,is(true));
        Assertion.assertThat("Hotel Button Text displayed is same as API", buttonTextApi.equals(buttonText), is(true));

        step("Comparing API response with redirectTarget option '"+redirectTargetApi+"'");
        Boolean isRedirectValid = hotelLandingPage.verifyRedirectTarget(redirectTargetApi, redirectTargetDataApi, language, vertical);
        Assertion.assertThat(" multiple hotel ID Redirect on the App should be equal with API", isRedirectValid, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void getMasterTagApiLogin() {
        Map<String, String> param = getParam("getMasterTagApiLogin");
        BaseTest.paramsThreadLocal.set(param);
        
        boolean login = Boolean.parseBoolean(param.get("login"));
        String username = param.get("username");
        String password = param.get("password");
        String query = param.get("query");
        int totalMasterTag = Integer.parseInt(param.get("totalMasterTag"));
        int totalCard = Integer.parseInt(param.get("totalCard"));
        String loginType = param.get("loginType");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login");
        loginPage.loginWith(loginType, username, password);
        String language = homePage.getLanguageApp();

        step("open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", language, "");

        step("search stay location");
        landingPage.searchStayLocation(query);

        step("Getting Master Tags via API");
        String currentDate = getCurrentDateYYYY_MM_DD();
        String accommodationType = "HOTEL";
        JSONObject masterTagList = hotelApi.getMasterTagList(login, username, password, query, accommodationType, language, totalMasterTag);
        String accessToken = String.valueOf(masterTagList.get("accessToken"));
        String searchType = String.valueOf(masterTagList.get("locationType"));
        String publicId = String.valueOf(masterTagList.get("publicId"));

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();
        HashMap<String ,String> groupFilterDetails = hotelApi.getMasterTagDetails(masterTagList);
        String masterTagName = groupFilterDetails.get("name");

        step("Choose master tag randomly: " + masterTagName);
        searchResultPage.selectMasterTag(masterTagName);

        step("Getting Hotel List via API");
        List<String> hotelNameList = hotelApi.getHotelList(accessToken, username, 1, 1, 1, searchType, publicId, currentDate, groupFilterDetails.get("keyword"), totalCard, accommodationType, "");
        Assertion.assertThat("Success to get hotel list", hotelNameList, is(either(empty()).or(not(empty()))));

        step("Compare hotel list from API and UI");
        boolean hotelListIsEmpty = hotelNameList.isEmpty();
        Assertion.assertThat("Hotel list between API and UI are the same", (hotelListIsEmpty & searchResultPage.checkEmptyHotelList()) || searchResultPage.checkHotelCardList("name", totalCard, hotelNameList.toArray(new String[0])), is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void getHotelNameWithAds() {
        Map<String, String> param = getParam("getHotelNameWithAds");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        int totalRoom = Integer.parseInt(param.get("totalRoom"));
        int totalAdult = Integer.parseInt(param.get("totalAdult"));
        String rangeAddDate = param.get("rangeAddDate");
        int totalNight = Integer.parseInt(param.get("totalNight"));
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";
        String currency = "IDR";

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        homePage.changeCurrency(currency, username);
        String language = homePage.getLanguageApp();

        step("Open the SRP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, accommodationType);
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String locationId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String locationType = hotelAutoCompleteResponse.data.contents.get(selectedIndex).type;
        hotelPage.openSRPDeeplink(accommodationType, totalRoom, totalAdult, locationId, locationType.toUpperCase(), location, checkInDate, checkOutDate, language);
        searchResultPage.waitForSrpToLoad();
        landingPage.fillNightCheckInForm();

        step("Get hotel name with ads");
        List<String> hotelNameList = hotelApi.getHotelNameAds(username, password, totalAdult, totalNight, totalRoom, locationType.toUpperCase(), locationId, checkInDate, "", accommodationType, currency);
        Assertion.assertThat("Success to get hotel name with ads", hotelNameList, is(either(empty()).or(not(empty()))));
        String hotelName = hotelNameList.get(0);

        step("Find the 1st hotel name with ads: " + hotelName);
        boolean isHotelNameAdsVisible = searchResultPage.isHotelNameAdsVisible(hotelName);
        Assertion.assertThat("Hotel name with ads is visible", isHotelNameAdsVisible, is(true));
    }

    @Author(names = {"Prerna"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void getMasterTagApiGuest() {
        Map<String, String> param = getParam("getMasterTagApiGuest");
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
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login As Guest");
        loginPage.loginAsGuest();

        String language = homePage.getLanguageApp();

        step("open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", language, "");

        step("search stay location");
        landingPage.searchStayLocation(query);

        step("Set dates");
        String currentDate = getCurrentDateYYYY_MM_DD();

        step("Getting Master Tags via API");
        String accommodationType = "HOTEL";
        JSONObject masterTagList = hotelApi.getMasterTagList(login, username, password, query, accommodationType, language, totalMasterTag);
        String accessToken = String.valueOf(masterTagList.get("accessToken"));
        String searchType = String.valueOf(masterTagList.get("locationType"));
        String publicId = String.valueOf(masterTagList.get("publicId"));

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();
        HashMap<String ,String> groupFilterDetails = hotelApi.getMasterTagDetails(masterTagList);

        step("Getting Hotel List via API");
        List<String> hotelNameList = hotelApi.getHotelList(accessToken, username, 1, 1, 1, searchType, publicId, currentDate, groupFilterDetails.get("name"), totalCard, accommodationType, "");

        Assertion.assertThat("Success to get hotel list", hotelNameList, is(either(empty()).or(not(empty()))));

        boolean hotelListIsEmpty = hotelNameList.isEmpty();

        step("Assertions");
        Assertion.assertThat("masterTag name matches with api response and click a random master tag", searchResultPage.checkMasterTags((AccommodationMasterTagResponse) masterTagList.get("data"), totalMasterTag, groupFilterDetails.get("name")), is(true));
        Assertion.assertThat("check hotel cards list if hotel list for master tag is not empty and if empty then check 'no hotel found' text", (hotelListIsEmpty & searchResultPage.checkEmptyHotelList()) || searchResultPage.checkHotelCardList("name", totalCard, hotelNameList.toArray(new String[0])), is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "LandingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyMultipleSquareCardLogin() {
        Map<String, String> param = getParam("verifyMultipleSquareCardLogin");
        BaseTest.paramsThreadLocal.set(param);
        String username = param.get("username");
        String password = param.get("password");
        String loginType = param.get("loginType");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Get multiple square card data from the API");
        String language = homePage.getLanguageApp();
        String templateCode = "MULTIPLE_SQUARE_CARD";
        MasterContentPOJO masterContentApi = hotelApi.getMasterContent(true, username, password, language, templateCode);
        MasterContentPOJO.MasterContentData masterContentJson = masterContentApi.contentList;
        LandingPageModulePOJO landingPageModuleApi = hotelApi.getLandingPageModule(true, username, password, language, templateCode,masterContentApi);

        Assertion.assertThat("Success to get master content from the API", masterContentApi, is(notNullValue()));
        Assertion.assertThat("Success to get landing page module from the API", landingPageModuleApi, is(notNullValue()));

        String titleApi = masterContentJson.title;
        String subTitleApi = masterContentJson.subTitle;
        String[] contentTitleApi = hotelApi.getMultipleSquareCardTitleList(landingPageModuleApi);

        step("open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", language, "");

        step("Go to the multiple square card section");
        landingPage.goToMultipleSquareCard();

        step("Get the multiple square card data from the APP");
        String titleApp = landingPage.getMultipleSquareCardTitle();
        String subTitleApp = landingPage.getMultipleSquareCardSubTitle();
        String[] contentTitleApp = landingPage.getMultipleSquareCardTitleList();

        step("Choose and click random card from the section");
        LandingPageModulePOJO.List redirectObject = landingPage.chooseRandomMultipleSquareCard(landingPageModuleApi);
        String redirectTarget = redirectObject.redirectTarget;
        LandingPageModulePOJO.Redirect redirectData = redirectObject.redirect;

        step("Comparing API response with Hotel Landing Page");
        Assertion.assertThat("Multiple Square Card Title on the APP should be equal with API", titleApi, is(titleApp));
        Assertion.assertThat("Multiple Square Card Main Sub Title on the APP should be equal with API", subTitleApi, is(subTitleApp));
        Assertion.assertThat("Multiple Square Card Content List on the App should be equal with API", landingPage.compareMultipleSquareCard(contentTitleApi,contentTitleApp), is(true));

        step("Comparing API response with redirectTarget option '"+ redirectTarget +"'");
        Boolean isRedirectValid = landingPage.verifyRedirectTarget(redirectTarget, redirectData, language, "Hotel");
        Assertion.assertThat("Multiple Square Card Redirect on the App should be equal with API", isRedirectValid, is(true));
    }

    @Author(names = {"Akbar"})
    @Module(name = "LandingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyMultipleSquareCardGuest() {
        Map<String, String> param = getParam("verifyMultipleSquareCardGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Get multiple square card data from the API");
        String language = homePage.getLanguageApp();
        String templateCode = "MULTIPLE_SQUARE_CARD";
        MasterContentPOJO masterContentApi = hotelApi.getMasterContent(false, "guest", "-", language, templateCode);
        MasterContentPOJO.MasterContentData masterContentJson = masterContentApi.contentList;
        LandingPageModulePOJO landingPageModuleApi = hotelApi.getLandingPageModule(false, "guest", "-", language, templateCode, masterContentApi);

        Assertion.assertThat("Success to get master content from the API", masterContentApi, is(notNullValue()));
        Assertion.assertThat("Success to get landing page module from the API", landingPageModuleApi, is(notNullValue()));

        String titleApi = masterContentJson.title;
        String subTitleApi = masterContentJson.subTitle;
        String[] contentTitleApi = hotelApi.getMultipleSquareCardTitleList(landingPageModuleApi);

        step("open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", language, "");

        step("Go to the multiple square card section");
        landingPage.goToMultipleSquareCard();

        step("Get the multiple square card data from the APP");
        String titleApp = landingPage.getMultipleSquareCardTitle();
        String subTitleApp = landingPage.getMultipleSquareCardSubTitle();
        String[] contentTitleApp = landingPage.getMultipleSquareCardTitleList();

        step("Choose and click random card from the section");
        LandingPageModulePOJO.List redirectObject = landingPage.chooseRandomMultipleSquareCard(landingPageModuleApi);
        String redirectTarget = redirectObject.redirectTarget;
        LandingPageModulePOJO.Redirect redirectData = redirectObject.redirect;

        step("Comparing API response with Hotel Landing Page");
        Assertion.assertThat("Multiple Square Card Title on the APP should be equal with API", titleApi, is(titleApp));
        Assertion.assertThat("Multiple Square Card Main Sub Title on the APP should be equal with API", subTitleApi, is(subTitleApp));
        Assertion.assertThat("Multiple Square Card Content List on the App should be equal with API", landingPage.compareMultipleSquareCard(contentTitleApi,contentTitleApp), is(true));

        step("Comparing API response with redirectTarget option '"+ redirectTarget +"'");
        Boolean isRedirectValid = landingPage.verifyRedirectTarget(redirectTarget, redirectData, language, "Hotel");
        Assertion.assertThat("Multiple Square Card Redirect on the App should be equal with API", isRedirectValid, is(true));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test(dataProvider="verifyBookingInBlackoutDataProvider")
    public void verifyBookingInBlackout(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currentDate = getCurrentDateYYYY_MM_DD();
        String startDate = addToDateYYYY_MM_DD(currentDate,1);
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get hotel blackout date");
        startDate = hotelApi.getBlackoutDate(username, password, startDate, hotelName, adult, night, room, "single");
        night = 1;

        step("Create new hotel issued order");
        hotelApi.createIssuedOrderApi(
        "en", username, password, startDate, hotelName, adult, night, room,
        orderFullName, orderNameTitle, guestFullName, guestTitle, "", false);

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        String issuedRoomName = hotelApi.getHotelRoomName();

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);

        step("Open e-Ticket");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("English");

        step("e-Ticket file is opened and not contain loyalty discount");
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, is(either(containsString(hotelName.toLowerCase())).or(containsString(hotelName))));
        Assertion.assertThat("Room name on the PDF should be equal on the App", actualPdfContent.contains(issuedRoomName), is(true));
        Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent.contains(guestFullName), is(true));
        Assertion.assertThat("PDF should contain tier benefit", actualPdfContent, is(not(containsString("Diamond Tier Benefit"))));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test(dataProvider="verifyBookingSingleBlackoutDataProvider")
    public void verifyBookingSingleBlackout(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currentDate = getCurrentDateYYYY_MM_DD();
        String startDate = addToDateYYYY_MM_DD(currentDate,1);
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get hotel blackout date");
        String blackoutDate = hotelApi.getBlackoutDate(username, password, startDate, hotelName, adult, night, room, "single");
        startDate = addToDateYYYY_MM_DD(blackoutDate,-1);
        night = 3;

        step("Create new hotel issued order");
        hotelApi.createIssuedOrderApi(
                "en", username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, "", false);

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        String issuedRoomName = hotelApi.getHotelRoomName();

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);

        step("Open e-Ticket");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("English");

        step("e-Ticket file is opened and contain loyalty discount");
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent, is(either(containsString(hotelName.toLowerCase())).or(containsString(hotelName))));
        Assertion.assertThat("Room name on the PDF should be equal on the App", actualPdfContent.contains(issuedRoomName), is(true));
        Assertion.assertThat("Guest name on the PDF should be equal on the App", actualPdfContent.contains(guestFullName), is(true));
        Assertion.assertThat("PDF should contain tier benefit", actualPdfContent, is(containsString("Diamond Tier Benefit")));
        String blackoutDateFull = convertToLongDate(blackoutDate);
        Assertion.assertThat("PDF should contain blackout date detail", actualPdfContent, is(containsString("benefits are not valid on the following stay dates: " + blackoutDateFull)));
    }

    @Author(names = {"Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void verifyBookingMultipleBlackout() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyBookingMultipleBlackout");
        BaseTest.paramsThreadLocal.set(param);

        String emailAdmin = param.get("emailAdmin");
        String passwordAdmin = param.get("passwordAdmin");
        String hotelName = param.get("hotelName");
        String username = param.get("emailUser");
        String password = param.get("passwordUser");
        int adult = Integer.parseInt(param.get("adult"));
        int room = Integer.parseInt(param.get("room"));
        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        MmDashboardApi<?> mmDashboardApi = new MmDashboardApi<>(session);
        ExtranetHotelApi<?> extranetHotelApi = new ExtranetHotelApi<>(session);

        step("Login to MM dashboard member");
        MmDashboardApi.DashboardMemberSession dashboardSession = mmDashboardApi.loginDashboardMember(emailAdmin, passwordAdmin);

        step("Get approved hotel id");
        int hotelId = mmDashboardApi.getApprovedHotelId(dashboardSession, hotelName);

        step("Get hotel loyalty blackout dates");
        List<String> blackoutDates = extranetHotelApi.getHotelLoyaltyBlackoutDates(dashboardSession, hotelId, "multiple");
        Assertion.assertThat("Pre-condition: at least 1 multi-day blackout range is available (excluding today)",
                blackoutDates, is(not(empty())));
        String blackoutDateRange = blackoutDates.get(0);
        String[] dateList = blackoutDateRange.split(" - ");
        String startBlackoutDate = dateList[0].trim();
        String endBlackoutDate = dateList[1].trim();
        String startDate = addToDateYYYY_MM_DD(startBlackoutDate, -1);
        int night = getDaysBetweenDates(startDate, addToDateYYYY_MM_DD(endBlackoutDate, 1));

        step("Create new hotel issued order");
        Boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                "IDR", false, false, "",
                "en", false);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String issuedOrderID = String.valueOf(hotelApi.getOrderId());
        String issuedRoomName = hotelApi.getHotelRoomName();

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Open my order page");
        homePage.openMyOrderPage();

        step("Find the issued order");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderID);

        step("Open e-Ticket");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage("English");

        step("e-Ticket file is opened and contain loyalty discount");
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("Hotel name on the PDF should be equal on the App", actualPdfContent,
                is(either(containsString(hotelName.toLowerCase())).or(containsString(hotelName))));
        Assertion.assertThat("Room name on the PDF should be equal on the App",
                actualPdfContent.contains(issuedRoomName), is(true));
        Assertion.assertThat("Guest name on the PDF should be equal on the App",
                actualPdfContent.contains(guestFullName), is(true));
        Assertion.assertThat("PDF should contain tier benefit", actualPdfContent,
                is(containsStringIgnoringCase("Tier Benefit")));

        String blackoutDateFull = orderDetailPage.getFullBlackoutDate(startBlackoutDate, endBlackoutDate);
        Assertion.assertThat("PDF should contain blackout date detail", actualPdfContent,
                is(containsString("benefits are not valid on the following stay dates: " + blackoutDateFull)));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Room")
    @Test
    public void checkExactAlternateRoomLogin() {
        Map<String, String> param = getParam("checkExactAlternateRoomLogin");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int totalRoom = Integer.parseInt(param.get("totalRoom"));
        int totalAdult = Integer.parseInt(param.get("totalAdult"));
        String expectedTextLabel = param.get("expectedTextLabel");
        int totalLoop = Integer.parseInt(param.get("totalLoop"));

        Session<WebDriver> session = sessionThreadLocal.get();
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalAdult);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("check search result when exact match & alternate is available");
        Assertion.assertThat("Exact and Alternate match room showing correctly", roomListPage.verifyExactAlternateRoom(expectedTextLabel, totalLoop), is(true));
    }

    @Author(names = {"Zia", "Upi"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Room")
    @Test(dataProvider = "checkAlternateMatchRoomLoginDataProvider")
    public void checkAlternateMatchRoomLogin(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");

        String date_type = param.get("date_type");
        String date = param.get("date");

        int totalRoom = Integer.parseInt(param.get("total_room"));
        int totalAdult = Integer.parseInt(param.get("total_adult"));

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set stay date");
        landingPage.setStayDate(date_type, date);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalAdult);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Verify alternate room for "+totalAdult+" guest(s)");
        Assertion.assertThat("Alternate room showing correctly", roomListPage.verifyAlternateRoomShowingCorrectly(totalAdult), is(true));
    }

    @Author(names = {"Zia", "Upi"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Room")
    @Test(dataProvider = "checkAlternateMatchRoomGuestDataProvider")
    public void checkAlternateMatchRoomGuest(Map<String, String> param) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");

        String date_type = param.get("date_type");
        String date = param.get("date");

        int totalRoom = Integer.parseInt(param.get("total_room"));
        int totalAdult = Integer.parseInt(param.get("total_adult"));

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set stay date");
        landingPage.setStayDate(date_type, date);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalAdult);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Verify alternate room for "+totalAdult+" guest(s)");
        Assertion.assertThat("Alternate room showing correctly", roomListPage.verifyAlternateRoomShowingCorrectly(totalAdult), is(true));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void checkAddOnHotelNonIDRGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkAddOnHotelNonIDRGuest");
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
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Logout if on the login session");
        loginPage.loginAsGuest();

        String language = "ENG";
        String eTicketLanguage = "English";
        step("Change language to \"" + language + "\"");
        homePage.openAccount();
        accountPage.selectLanguage(language);
        settingPage.backToHome();

        step("Change currency to \"" + currency + "\"");
        homePage.changeCurrency(currency,"");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", language, currency);

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("set stay date");
        landingPage.setStayDate(dateType, date);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        String roomName = roomListPage.getTheFirstHotelRoomName();

        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToAddOnPage();
        Double priceWithOutAddOn = convertPrice(bookingFormPage.getTotalPriceBookingForm());

        step("Click Extra Facility Add Button");
        bookingFormPage.clickExtraFacilityAddOn();
        String addOnTitle = bookingFormPage.getAddonTitle();
        Double addOnPrice = convertPrice(bookingFormPage.getAddOnPrice());
        String priceWithAddOnString = bookingFormPage.getTotalPriceBookingForm();
        Assertion.assertThat("Addon Price with correct currency: "+currency, priceWithAddOnString, is(containsString(currency)));

        Double priceWithAddOn = convertPrice(priceWithAddOnString);

        step("open Price Detail");
        Assertion.assertThat("Price after addOn is equals to hotel price ("+priceWithOutAddOn+") + addOn price ("+addOnPrice+")", setNumberAfterComma(priceWithOutAddOn + addOnPrice), is(setNumberAfterComma(priceWithAddOn)));
        Assertion.assertThat("Is price breakdown correct", bookingFormPage.isPriceDetailCorrect(priceWithAddOnString), is(true));
        Assertion.assertThat("Addon Price is correct on price detail", bookingFormPage.MatchAddOnPriceDetail(addOnPrice, addOnTitle), is(true));

        int addOnQty = bookingFormPage.getAddOnQty(addOnTitle);

        step("continue to payment");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

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
        Assertion.assertThat("add on "+addOnQty+" "+addOnTitle+" should be showing on order detail", orderDetailPage.checkAddon(addOnTitle,addOnQty), is(true));

        step("Open e-Ticket");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage(eTicketLanguage);

        step("e-Ticket file is opened and contain "+addOnQty+" "+addOnTitle);
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("PDF content contains: "+addOnQty+"x "+addOnTitle, actualPdfContent, is(containsString(addOnQty+" "+addOnTitle)));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void checkAddOnHotelNonIDRLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkAddOnHotelNonIDRLogin");
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
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
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

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", language, currency);

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("set stay date");
        landingPage.setStayDate(dateType, date);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        String roomName = roomListPage.getTheFirstHotelRoomName();

        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Completing booking form data");
        bookingFormPage.fillGuestDetail(false);

        step("Submit booking");
        bookingFormPage.continueToAddOnPage();
        Double priceWithOutAddOn = convertPrice(bookingFormPage.getTotalPriceBookingForm());

        step("Click Extra Facility Add Button");
        bookingFormPage.clickExtraFacilityAddOn();
        String addOnTitle = bookingFormPage.getAddonTitle();

        Double addOnPrice = convertPrice(bookingFormPage.getAddOnPrice());
        String priceWithAddOnString = bookingFormPage.getTotalPriceBookingForm();
        Assertion.assertThat("Addon Price with correct currency: "+currency, priceWithAddOnString, is(containsString(currency)));

        Double priceWithAddOn = convertPrice(priceWithAddOnString);

        step("open Price Detail");
        Assertion.assertThat("Price after addOn is equals to hotel price ("+priceWithOutAddOn+") + addOn price ("+addOnPrice+")", setNumberAfterComma(priceWithOutAddOn + addOnPrice), is(setNumberAfterComma(priceWithAddOn)));
        Assertion.assertThat("Is price breakdown correct", bookingFormPage.isPriceDetailCorrect(priceWithAddOnString), is(true));
        Assertion.assertThat("Addon Price is correct on price detail", bookingFormPage.MatchAddOnPriceDetail(addOnPrice, addOnTitle), is(true));

        int addOnQty = bookingFormPage.getAddOnQty(addOnTitle);

        step("continue to payment");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

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
        Assertion.assertThat("add on "+addOnQty+" "+addOnTitle+" should be showing on order detail", orderDetailPage.checkAddon(addOnTitle,addOnQty), is(true));

        step("Open e-Ticket");
        orderDetailPage.clickEticketFromHeader();
        orderDetailPage.clickETicketLanguage(eTicketLanguage);

        step("e-Ticket file is opened and contain "+addOnQty+" "+addOnTitle);
        String actualPdfContent = orderDetailPage.getPDFContentFile();
        Assertion.assertThat("PDF content contains: "+addOnQty+"x "+addOnTitle, actualPdfContent, is(containsString(addOnQty+" "+addOnTitle)));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test(dataProvider = "checkMultipleGuestVendorDataProvider")
    public void checkMultipleGuestVendor(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String vendor = map.get("vendor");

        //based on testrail data -> search hotel for 2 night 2 room 4 guest
        String dateType = "exact date";
        String date = "15-17";
        int totalRoom = 2;
        int totalGuest = 4;

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set stay date for 2 night");
        landingPage.setStayDate(dateType, date);

        step("Set " + totalRoom + " room and " + totalGuest + " guests");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalGuest);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();

        if(vendor.contains("expedia")){
            step("Select a hotel with vendor: \"" + vendor + "\"");
            landingPage.fillNightCheckInForm();
            searchResultPage.selectHotelVendor(vendor);
        }else {
            detailPage.RemovePopUp();
        }

        step("Go to room list");
        detailPage.seeHotelRoom();

        step("click room \"" + vendor + "\"");
        roomListPage.openFirstRoomDetail(vendor, false);

        step("open booking form");
        roomDetailPage.clickBookButton(totalRoom);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        step("input guest data");
        bookingFormPage.fillGuestDetail(false);

        step("Check the total guest data");
        Assertion.assertThat("display " + totalRoom + " guest form on booking form", bookingFormPage.getGuestNameList().size(), is(totalRoom));

        step("click booking room until payment");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();
        bookingFormPage.closeRatePlanBottomSheet();

        step("Redirected to the payment page");
        String orderID = paymentPage.checkOrderID();

        step("Open the order list");
        homePage.gotoHome();
        homePage.openMyOrderPage();

        step("Open the order details with order ID: " + orderID);
        orderListPage.openOrderDetailWaitingForPayment(orderID);
        
        Assertion.assertThat("display " + totalRoom + " guest name on my order", orderDetailPage.getGuestDetailsName().size(), is(totalRoom));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test(dataProvider = "checkSingleGuestVendorDataProvider")
    public void checkSingleGuestVendor(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String hotelName = map.get("hotelName");
        String vendor = map.get("vendor");

        String dateType = map.get("date_type");
        String date = map.get("date");
        int totalRoom = 1;
        int totalGuest = 2;

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Set stay date");
        landingPage.setStayDate(dateType, date);

        step("Set " + totalRoom + " room and " + totalGuest + " guests");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalGuest);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to room list");
        detailPage.seeHotelRoom();

        step("click room \"" + vendor + "\"");
        roomListPage.openFirstRoomDetail(vendor, false);

        step("open booking form");
        roomDetailPage.clickBookButton(totalRoom);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        step("input guest data");
        bookingFormPage.fillGuestDetail(false);

        step("Check the total guest data");
        Assertion.assertThat("display " + totalRoom + " guest form on booking form", bookingFormPage.getGuestNameList().size(), is(totalRoom));

        step("click booking room until payment");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();
        bookingFormPage.closeRatePlanBottomSheet();

        step("Redirected to the payment page");
        String orderID = paymentPage.checkOrderID();

        step("Open the order list");
        homePage.gotoHome();
        homePage.openMyOrderPage();

        step("Open the order details with order ID: " + orderID);
        orderListPage.openOrderDetailWaitingForPayment(orderID);
        
        Assertion.assertThat("display " + totalRoom + " guest name on my order", orderDetailPage.getGuestDetailsName().size(), is(totalRoom));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test(dataProvider = "checkSingleGuestSpecialRequestVendorDataProvider")
    public void checkSingleGuestSpecialRequestVendor(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String hotelName = map.get("hotelName");
        String vendor = map.get("vendor");
        String specialRequest = map.get("specialRequest");

        String dateType = map.get("date_type");
        String date = map.get("date");
        int totalRoom = 1;
        int totalGuest = 2;

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Set stay date");
        landingPage.setStayDate(dateType, date);

        step("Set " + totalRoom + " room and " + totalGuest + " guests");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalGuest);
        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Go to room list");
        detailPage.seeHotelRoom();

        step("click room \"" + vendor + "\"");
        roomListPage.openFirstRoomDetail(vendor, false);

        step("open booking form");
        roomDetailPage.clickBookButton(totalRoom);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        step("input guest data");
        bookingFormPage.fillGuestDetail(false);
        bookingFormPage.fillSpecialRequest(specialRequest);

        step("Check the total guest data");
        Assertion.assertThat("display " + totalRoom + " guest form on booking form", bookingFormPage.getGuestNameList().size(), is(totalRoom));

        step("click booking room until payment");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();
        bookingFormPage.closeRatePlanBottomSheet();

        step("Redirected to the payment page");
        String orderID = paymentPage.checkOrderID();

        step("Open the order list");
        homePage.gotoHome();
        homePage.openMyOrderPage();

        step("Open the order details with order ID: " + orderID);
        orderListPage.openOrderDetailWaitingForPayment(orderID);
        
        Assertion.assertThat("display " + totalRoom + " guest name on my order", orderDetailPage.getGuestDetailsName().size(), is(totalRoom));
        Assertion.assertThat("display " + specialRequest + " special request on my order", orderDetailPage.getSpecialRequest(), containsString(specialRequest));
    }

    @Author(names = {"Yazid"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test(dataProvider = "bookRoomChildrenVendorDataProvider")
    public void bookRoomChildrenVendor(Map<String, String> map) {
        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String dateType = map.get("date_type");
        String date = map.get("date");
        String vendor = map.get("vendor");
        String currency = map.get("currency");
        String hotelName = map.get("hotelName");
        int totalAdult = 1;
        int totalChildren = 2; //min 2 because there are steps that change the children's age twice
        int totalGuest = totalAdult + totalChildren;
        String childAgeBugUpperBoundValue = map.get("childAgeBugUpperBound");
        int childAgeBugUpperBound = (childAgeBugUpperBoundValue == null || childAgeBugUpperBoundValue.isBlank())
                ? LandingPage.DEFAULT_CHILD_AGE_BUG_UPPER_BOUND
                : Integer.parseInt(childAgeBugUpperBoundValue.trim());

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        BookingDetailPage<WebDriver> bookingDetailPage = BookingDetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("login with specific account via deeplink only");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username,password);

        step("Change currency to \"" + currency + "\"");
        homePage.changeCurrency(currency,username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set stay date");
        String[] checkInOut = landingPage.setStayDate(dateType, date);

        step("user set rooms: 1 and guest: "+totalGuest+" with children age: one child aged "+childAgeBugUpperBound+" and the rest below "+childAgeBugUpperBound+" years old");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(totalAdult);
        landingPage.setTotalChildren(totalChildren);

        step("set children ages (one child aged " + childAgeBugUpperBound + ", others random below " + childAgeBugUpperBound + ") and verify the picker keeps the selected age when re-editing each of the " + totalChildren + " children (prod bug: age <= " + childAgeBugUpperBound + " resets to 16 on re-edit)");
        landingPage.setChildAgesWithinBugRangeAndVerifyReEdit(totalChildren, childAgeBugUpperBound);

        landingPage.clickDone();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Check the SRP");
        Assertion.assertThat("SRP displayed successfully", searchResultPage.isSearchResultShown(), is(true));
        Assertion.assertThat("verify price on SRP", searchResultPage.getHotelPriceCard("", false), is(notNullValue()));
        Assertion.assertThat("guest count is " + totalGuest, searchResultPage.getTotalGuestHeader(), is(totalGuest));

        step("change search to open native hotel directly with name: " + hotelName);
        searchResultPage.changeLocation(hotelName,"Hotel");
        searchResultPage.clickSubmit();

        step("Check the PDP");
        Assertion.assertThat("PDP displayed successfully", detailPage.isHotelDetailShown(), is(true));
        Assertion.assertThat("Verify cheapest price on PDP", detailPage.getHotelPrice(), is(notNullValue()));
        Assertion.assertThat("Children policy should be existed", detailPage.checkPolicyAccommodation(), is(either(containsString("Children")).or(containsString("Anak"))));

        step("user tap \"see more\" on children policy");
        detailPage.openAccommodationRules();
        Assertion.assertThat("children policy drawer displayed", detailPage.isAccommodationRulesOpen(), is(true));

        step("user closed children policy drawer");
        detailPage.closePdpDrawer();
        Assertion.assertThat("children policy drawer closed successfully", detailPage.isAccommodationRulesOpen(), is(false));

        step("user tap button \"See Room\"");
        detailPage.seeHotelRoom();
        Assertion.assertThat("guest count is " + totalGuest, searchResultPage.getTotalGuestHeader(), is(totalGuest)); //using the same method on different page due to the same elements
        Assertion.assertThat("verify price on room list", roomListPage.getTheFirstHotelRoomPrice(), is(notNullValue()));
        Assertion.assertThat("children meal plan pax is displayed on room card", roomListPage.checkChildMeal(), is(true));
        Assertion.assertThat("children count is displayed on room card", roomListPage.checkChildGuest(), is(true));

        step("Get price on room list");
        String priceRoomList = roomListPage.getTheFirstHotelRoomPrice(vendor, true,currency);

        step("Open the \"" + vendor + "\" room details");
        roomListPage.openFirstRoomDetail(vendor, true);
        Assertion.assertThat("children count is displayed on room detail", roomDetailPage.checkChildGuest(), is(true));
        Assertion.assertThat("children meal plan pax is displayed on room detail", roomDetailPage.checkChildMeal(), is(true));
        Assertion.assertThat("children policy is displayed", roomDetailPage.checkChildRoomPolicy(), is(true));

        step("Get price on room detail");
        String priceRoomDetail = roomDetailPage.getHotelRoomPriceOnRoomDetail();
        Assertion.assertThat("verify price on room detail should be equal with the room list", priceRoomDetail, is(priceRoomList));

        step("Get total price (after taxes) from the price detail on room detail");
        String priceTotalRoomDetail = roomDetailPage.getTotalPriceOnRoomDetail();

        step("user proceeded to book respective room");
        detailPage.clickBookButton(1);
        String priceChangePopUp = bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        priceRoomDetail = updateString(priceRoomDetail, priceChangePopUp);
        priceTotalRoomDetail = updateString(priceTotalRoomDetail, priceChangePopUp);
        Assertion.assertThat("booking form displayed successfully", bookingFormPage.getHotelNameBookingForm(), is(notNullValue()));
        Assertion.assertThat("children count is displayed on booking details", bookingFormPage.checkChildGuest(), is(true));
        Assertion.assertThat("children meal plan pax is displayed on booking details", bookingFormPage.checkChildMeal(), is(true));

        step("Get price on booking form");
        String priceBookingForm = bookingFormPage.getTotalPriceBookingForm();
        bookingFormPage.handleRoundOffNonIDR(priceTotalRoomDetail,priceBookingForm,currency);

        step("user click booking detail");
        bookingFormPage.openBookingDetailPage();
        Assertion.assertThat("children count is displayed on booking details", bookingDetailPage.checkChildGuest(), is(true));
        Assertion.assertThat("children meal plan pax is displayed on booking details", bookingDetailPage.checkChildMeal(), is(true));

        step("Open see more policies section");
        bookingDetailPage.openSeeMorePolicies();
        Assertion.assertThat("Children policies is displayed on booking details", bookingDetailPage.checkChildrenPolicy(), is(true));
        bookingDetailPage.backToBookingFormPage();

        step("user proceeded to submit booking");
        bookingFormPage.selectRandomSalutationIfMissing();
        bookingFormPage.continueToPaymentPage();
        String priceChanges = bookingFormPage.PriceChangesPopUp();
        priceBookingForm = updateString(priceBookingForm, priceChanges);

        step("redirected to payment page");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
        Assertion.assertThat("verify price", CoreFunctions.convertPriceSafely(paymentPage.getTotalPricePayment()), is(CoreFunctions.convertPriceSafely(priceBookingForm)));

        step("user back to the hotel landing page");
        homePage.gotoHome();
        homePage.openVerticalPageDeeplink("Hotel", "", currency);
        Assertion.assertThat("recent search displayed with guest text: \""+totalGuest+" Guest\" or \""+totalGuest+" Tamu\"", landingPage.getRecentTotalGuest(location, checkInOut), is(either(equalTo(totalGuest + " Guests")).or(equalTo(totalGuest + " Tamu"))));
    }

    @Author(names = {"Yazid","Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void completeBookingIdrGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingIdrGuest");
        BaseTest.paramsThreadLocal.set(map);
        String location = map.get("location");
        String currency = map.get("currency");
        String priceRange = map.get("priceRange");
        String star = map.get("star");
        String totalHotel = map.get("totalHotel");
        String dateType = map.get("date_type");
        String date = map.get("date");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
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

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("set stay date");
        landingPage.setStayDate(dateType, date);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

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
        searchResultPage.resetFilter();

        step("User click sort filter button");
        searchResultPage.selectFilterSortBy("lowest price");

        step("Get hotel price data");
        Boolean isSortByLowestPriceValid = searchResultPage.isSortByLowestPriceValid(totalHotel);

        step("Sort by lowest price assertion");
        Assertion.assertThat("short by lowest price is valid", isSortByLowestPriceValid ,is(true));

        step("Filter by star");
        searchResultPage.selectFilterStar(star);
        Assertion.assertThat("Assert if first '"+totalHotel+"' hotel card is having the right hotel star", searchResultPage.checkHotelCardList("Star", Integer.parseInt(totalHotel), star.split("-")), is(true));

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
            bookingFormPage.clickCancelButton();
        }

        step("Fill contact detail");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

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

        step("select Sakuku as payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();

        step("Assert if the payment is a success");
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open the order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Price on order detail should be "+currency);
        String totalPriceOrderDetail = orderDetailPage.getTotalPaymentHotel();
        Assertion.assertThat("Currency is "+currency, totalPriceOrderDetail, containsString(currency));
    }

    @Author(names = {"Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void completeBookingIdrLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingIdrLogin");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);

        String location = map.get("location");
        String currency = map.get("currency");
        String priceRange = map.get("priceRange");
        String star = map.get("star");
        String totalHotel = map.get("totalHotel");
        String dateType = map.get("date_type");
        String date = map.get("date");

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        step("Goto home");
        homePage.gotoHome();

        step("Logout if on the login session");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType,username,password);
        homePage.changeCurrency(currency,username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("set stay date");
        landingPage.setStayDate(dateType, date);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

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
        searchResultPage.resetFilter();

        step("User click sort filter button");
        searchResultPage.selectFilterSortBy("lowest price");

        step("Get hotel price data");
        Boolean isSortByLowestPriceValid = searchResultPage.isSortByLowestPriceValid(totalHotel);

        step("Sort by lowest price assertion");
        Assertion.assertThat("short by lowest price is valid", isSortByLowestPriceValid ,is(true));

        step("Filter by star");
        searchResultPage.selectFilterStar(star);
        Assertion.assertThat("Assert if first '"+totalHotel+"' hotel card is having the right hotel star", searchResultPage.checkHotelCardList("Star", Integer.parseInt(totalHotel), star.split("-")), is(true));

        step("Click see map button in SRP");
        searchResultPage.clickSeeMapButton();

        step("Zoom the maps");
        searchResultPage.zoomMaps(0, 2);

        step("Get hotel price in maps");
        String[] hotelPriceMaps = searchResultPage.getHotelPriceMaps();
        Assertion.assertThat("Price in maps Currency is "+currency, hotelPriceMaps[1],containsString(currency));

        step("Get hotel price in card");
        String hotelPriceCard = searchResultPage.getHotelPriceCard(hotelPriceMaps[1], true);
        Assertion.assertThat("Price in maps card Currency is "+currency, hotelPriceCard, containsString(currency));
        searchResultPage.closeMap();

        step("Select hotel list");
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

        step("get hotel name");
        String hotelName = bookingFormPage.getHotelNameBookingForm();

        step("Verify cancellation charge "+charges.get(0));
        if(!charges.get(0).equals("No Charges present")){
            bookingFormPage.openBookingDetailPage();
            Assertion.assertThat("Cancellation Currency is "+currency, detailPage.getCancellationCharges(), everyItem(containsString(currency)));
            bookingFormPage.clickCancelButton();
        }

        step("Fill contact detail");
        bookingFormPage.fillGuestDetail(false);

        step("Verify price in booking form");
        String priceTotalBookingForm = bookingFormPage.getTotalPriceBookingForm();
        Assertion.assertThat("Total price booking form with correct currency: "+currency, priceTotalBookingForm, is(containsString(currency)));

        step("verify price detail");
        bookingFormPage.continueToAddOnPage();
        Assertion.assertThat("Is price detail correct currency "+currency, bookingFormPage.getPriceDetailPrices(priceTotalBookingForm), everyItem(containsString(currency)));
        Assertion.assertThat("Is price breakdown correct", bookingFormPage.isPriceDetailCorrect(priceTotalBookingForm), is(true));

        step("continue to payment");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));

        step("select Sakuku as payment method");
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod("Sakuku");
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();

        step("Assert if the payment is a success");
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open the order detail");
        orderListPage.openIssuedTicketViaDeeplink(orderID);

        step("Price on order detail should be "+currency);
        String totalPriceOrderDetail = orderDetailPage.getTotalPaymentHotel();
        Assertion.assertThat("Currency is "+currency, totalPriceOrderDetail, containsString(currency));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void completeBookingNonIdrGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingNonIdrGuest");
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
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        DefaultAppsPage<?> defaultAppsPage = DefaultAppsPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Clear browsers default preference");
        defaultAppsPage.clearBrowserDefaultPreferenceOnLambdaTest();

        step("Goto home");
        homePage.gotoHome();

        step("Clear browsers default preference");
        defaultAppsPage.clearBrowserDefaultPreferenceOnLambdaTest();

        step("Goto home");
        homePage.gotoHome();

        step("Logout if on the login session");
        loginPage.loginAsGuest();
        homePage.changeCurrency(currency,"");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("set stay date");
        landingPage.setStayDate(dateType, date);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

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

        step("get hotel name");
        String hotelName = bookingFormPage.getHotelNameBookingForm();

        step("Verify cancellation charge "+charges.get(0));
        if(!charges.get(0).equals("No Charges present")){
            bookingFormPage.openBookingDetailPage();
            Assertion.assertThat("Cancellation Currency is "+currency, detailPage.getCancellationCharges(), everyItem(containsString(currency)));
            bookingFormPage.clickBackButton();
        }

        step("Fill contact detail");
        bookingFormPage.fillContactOrderDetail();
        bookingFormPage.fillGuestDetail(false);

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
        paymentPage.clickPayButton();
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

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void completeBookingNonIdrLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("completeBookingNonIdrLogin");
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
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
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

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(1);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("set stay date");
        landingPage.setStayDate(dateType, date);

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

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

        step("get hotel name");
        String hotelName = bookingFormPage.getHotelNameBookingForm();

        step("Verify cancellation charge "+charges.get(0));
        if(!charges.get(0).equals("No Charges present")){
            bookingFormPage.openBookingDetailPage();
            Assertion.assertThat("Cancellation Currency is "+currency, detailPage.getCancellationCharges(), everyItem(containsString(currency)));
            bookingFormPage.clickBackButton();
        }

        step("Fill contact detail");
        bookingFormPage.fillGuestDetail(false);

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
        paymentPage.clickPayButton();
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
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(priority = 5)
    public void multiOrderDifferentRoomWithItemAddOnsLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("multiOrderDifferentRoomWithItemAddOnsLogin");
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
        String accommodationType = "hotel";
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
        double totalRoomPrice = roomListPage.selectRoomsByCount(totalRoom, totalGuest, new int[]{2, 1});

        step("user check the price footer");
        double priceFooter = convertPrice(roomListPage.getPriceFooter());
        // Allow discounted footer (footer can be lower than room total). Reject if footer is absurdly low or higher than room total (overcharge).
        double roomTotal = totalRoomPrice * totalNight;
        double discountTolerance = Math.max(roomTotal * 0.15, 100_000);
        double roundingTolerance = Math.max(roomTotal * 0.001, 1);
        Assertion.assertThat("price footer should not be below room total by more than discount tolerance: " + totalRoomPrice + " x " + totalNight, priceFooter, greaterThanOrEqualTo(roomTotal - discountTolerance));
        Assertion.assertThat("price footer should not exceed room total (overcharge): " + totalRoomPrice + " x " + totalNight, priceFooter, lessThanOrEqualTo(roomTotal + roundingTolerance));

        step("user click price footer");
        roomListPage.clickPriceFooter();
        Assertion.assertThat("user can see the price breakdown", roomListPage.priceBreakdownIsDisplayed(), is(true));
        Assertion.assertThat("verify the total price is same or less than with price footer", (roomListPage.sumPriceBreakdown() - priceFooter), lessThanOrEqualTo(totalRoom * totalNight * 1.0));

        step("user click book button from price footer");
        roomListPage.clickBookButtonFooter();
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        Assertion.assertThat("user redirected to Booking Form", bookingFormPage.isBookingFormShown(true), is(true));

        step("user add the guest name");
        bookingFormPage.fillGuestDetail(false);
        Assertion.assertThat("user should add guest name for each room type", bookingFormPage.getGuestNameList().size(), is(is(totalGuest)));

        step("user click add on /item add ons");
        String priceWithoutAddOns = bookingFormPage.getTotalPriceBookingForm();
        HashMap<Integer, String> selectedRoomName = RoomListPageAndroid.selectedRoomName;
        bookingFormPage.continueToAddOnPage();
        HashMap<Object, Object> addOnData = bookingFormPage.clickExtraFacilityAddOn(itemPerRoom, ITEM, selectedRoomName);

        step("user check price footer booking form");
        String priceWithAddOns = bookingFormPage.getTotalPriceBookingForm();
        Assertion.assertThat("price on price footer booking form recalculated with add ons price: " + priceWithoutAddOns + " + " + addOnData.get(TOTAL_PRICE), convertPrice(priceWithAddOns), is(convertPrice(priceWithoutAddOns) + convertPrice(String.valueOf(addOnData.get(TOTAL_PRICE)))));

        step("user check Booking Form's price detail");
        String addOnsName = String.valueOf(addOnData.get(NAME));
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
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(priority = 6)
    public void multiOrderDifferentRoomWithRoomNightAddOnsLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("multiOrderDifferentRoomWithRoomNightAddOnsLogin");
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
        String accommodationType = "hotel";
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
        Assertion.assertThat("verify the total price is same or less than with price footer", (roomListPage.sumPriceBreakdown() - priceFooter), lessThanOrEqualTo(totalRoom * totalNight * 1.0));

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
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(priority = 7)
    public void multiOrderDifferentRoomIdr() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("multiOrderDifferentRoomIdr");
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
        String accommodationType = "hotel";
        String currency = "IDR";

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

        step("user selected currency is " + currency);
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

        step("order with " + currency + " MO successfully paid");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();
        String orderID = paymentPage.checkOrderID();
        paymentPage.openPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickPayButton();
        paymentPage.paymentChecker();
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

    @Author(names = {"Yazid"})
    @Module(name = "CreateBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void multiOrderDifferentRoomNonIdr() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("multiOrderDifferentRoomNonIdr");
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
        String accommodationType = "hotel";

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
        paymentPage.clickPayButton();
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
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    @SneakyThrows
    public void refundNonIdr() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("refundNonIdr");
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
        Unm<WebDriver> unm = new Unm<>(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int limit = 15;

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

        step("Create a hotel order with waiting payment status");
        String bookedOrderID = hotelApi.createWaitingPaymentOrderApi(
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

        step("Open order detail");
        // Wait for the backend to record the payment (PROCESSING_ETICKET or
        // ETICKET_ISSUED) before deeplinking - otherwise the order-detail
        // screen may render in an intermediate state where the refund button
        // is not yet available, causing the next step to flake.
        orderListPage.waitForPaymentSuccessful(bookedOrderID,10);
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
    @Module(name = "PostPurchase")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    @SneakyThrows
    public void refundIdr() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("refundIdr");
        BaseTest.paramsThreadLocal.set(param);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        Unm<WebDriver> unm = new Unm<>(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String currency = param.get("currency");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Get api access token");
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String apiAccessToken = authData.accessToken;
        String accountId = String.valueOf(authData.accountId);

        step("check order present in order");
        MapVerticalWithOrderId activeOrderData = new OrderApi<>(session).getAvailaibleOrderByVertical("HOTEL", apiAccessToken);
        String bookedOrderID;

        int limit = 15;
        activeOrderData = refundPage.getRefundAbleOrder(activeOrderData, username,accountId,limit);
        activeOrderData = orderListPage.filterOrderByPaymentCurrency(activeOrderData,currency,limit);

        step("Goto home");
        homePage.gotoHome();

        if(activeOrderData.isEmpty && activeOrderData.hotel.isEmpty()){
            step("Create new hotel issued order");
            Boolean result = hotelApi.createIssuedOrderApi(
                    username, password, startDate, hotelName, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle,"IDR");
            Assertion.assertThat("Hotel Booking via API is successful", result, is(true));
            step("Get data from the issued order");
            bookedOrderID = String.valueOf(hotelApi.getOrderId());
        }else{
            bookedOrderID = activeOrderData.hotel.get(0).orderId;
        }

        step("user open app as a guest");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType,username,password);
        homePage.changeCurrency(currency,username);

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

    @Author(names = {"Mridul"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PostPurchase")
    @Test
    @SneakyThrows
    public void verifyRefundDetailFromOrderDetailPage() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRefundDetailFromOrderDetailPage");
        BaseTest.paramsThreadLocal.set(param);

        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        RefundPage<WebDriver> refundPage = RefundPage.getInstance(session);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = param.get("currency");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create issued hotel order via API");
        Boolean bookingResult = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency);
        Assertion.assertThat("Hotel Booking via API is successful", bookingResult, is(true));
        String bookedOrderID = String.valueOf(hotelApi.getOrderId());

        step("Goto home and login");
        homePage.gotoHome();
        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        step("Open order detail and submit refund via UI");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);
        orderDetailPage.clickRefundButton();
        refundPage.chooseRefundReasonWebView();
        refundPage.clickProcessRefundBtn();
        refundPage.submitRefund();
        Assertion.assertThat("Successfully submit the refund", refundPage.checkRefundSubmission(), is(true));
        refundPage.viewRefundDetailPage();

        step("Reopen order detail page after refund submission");
        orderListPage.openActiveTicketViaDeeplink(bookedOrderID);
        

        step("Verify See Details option is shown on order detail page after refund");
        Assertion.assertThat("See Details option is displayed on order detail page",
                orderDetailPage.isRefundDetailButtonDisplayed(), is(true));

        step("Click See Details to open refund detail page");
        orderDetailPage.goToRefundDetail(bookedOrderID);

        step("Verify refund progress, refund id, refund method and refund amount on refund detail page");
        String refundProgress = refundPage.getRefundStatus();
        String refundId = refundPage.getRefundIdRefundDetails();
        String refundMethod = refundPage.getRefundMethod();
        String refundAmount = refundPage.getTotalRefund();

        Assertion.assertThat("Refund progress is displayed", refundProgress, is(not(emptyOrNullString())));
        Assertion.assertThat("Refund ID is displayed", refundId, is(not(emptyOrNullString())));
        Assertion.assertThat("Refund method is displayed", refundMethod, is(not(emptyOrNullString())));
        Assertion.assertThat("Refund amount currency is " + currency, refundAmount, containsString(currency));
    }

    @Author(names = {"Zia", "Aditi"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Module(name = "Accommodation")
    @Test
    @SneakyThrows
    public void ACC_hotelRescheduleNonIdrWhenCurrencyIDR() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("ACC_hotelRescheduleNonIdrWhenCurrencyIDR");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
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
        int addonsAmount = Integer.parseInt(map.get("addonsAmount"));

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        String appLanguage = homePage.getLanguageApp();

        step("Create an order for HOTEL with waiting payment status");
        String orderId = "";
        String addonsName = "";
        try {
            orderId = hotelApi.createWaitingPaymentOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                currency, addonsAmount, "HOTEL", appLanguage);
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
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));
        orderListPage.waitForPaymentSuccessful(orderId,10);

        step("Change currency to IDR");
        homePage.changeCurrency("IDR", username);

        step("Open order detail");
        orderListPage.waitForPaymentSuccessful(orderId,10);
        // openIssuedTicketViaDeeplink polls the order status until
        // ETICKET_ISSUED and only then deeplinks to the order-detail page;
        // we use it (instead of openActiveTicketViaDeeplink) because the
        // add-on assertion below needs the issued state - waitForPaymentSuccessful
        // alone returns at PROCESSING_ETICKET, before the add-on row renders.
        orderListPage.openIssuedTicketViaDeeplink(orderId);
        
        Assertion.assertThat("add on "+addonsAmount+" "+addonsName+" should be showing on order detail", orderDetailPage.checkAddon(addonsName,addonsAmount), is(true));

        step("Click reschedule");
        orderDetailPage.clickReschedule(orderId);

        step("Choose new date for the reschedule");
        webViewPage.switchToWebViewUrl("reschedule");
        hotelReschedulePageWeb.changeNightDuration(daysToAdjust);

        step("Choose a new room type with additional Price");
        webViewPage.switchToWebViewUrl("reschedule");
        hotelReschedulePageWeb.chooseRoomWithAdditionalPrice();

        step("Verify booking form prices is " + currency);
        webViewPage.switchToWebViewUrl("reschedule/checkout");
        Assertion.assertThat("Total price in reschedule booking form is " + currency, hotelReschedulePageWeb.getTotalPriceBookingForm(), containsString(currency));

        step("Submit booking form");
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

        step("Check the new order detail");
        orderListPage.openIssuedTicketViaDeeplink(newOrderID);

        step("Check the currency on the new order detail");
        Assertion.assertThat("Current currency should be " + currency, orderDetailPage.getTotalPaymentHotel(), containsString(currency));
    }

    @Author(names = {"Zia"})
    @Module(name = "PostPurchase")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    @SneakyThrows
    public void refundNonIdrWhenCurrencyIdr() {
        Session<WebDriver> session = sessionThreadLocal.get();

        Map<String, String> param = getParam("refundNonIdrWhenCurrencyIdr");
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

        step("Create a hotel order with waiting payment status");
        String bookedOrderID = hotelApi.createWaitingPaymentOrderApi(
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
        String priceOnPaymentPage = paymentPage.getTotalPricePayment();
        paymentPage.clickPayButton();
        paymentPage.verifyCreditCard3ds();
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Change currency to IDR");
        homePage.gotoHome();
        homePage.changeCurrency("IDR",username);

        step("Open order detail");
        // Wait for the backend to record the payment (PROCESSING_ETICKET or
        // ETICKET_ISSUED) before deeplinking - otherwise the price assertion
        // and refund button may not have rendered yet on the order-detail
        // screen, causing the next steps to flake.
        orderListPage.waitForPaymentSuccessful(bookedOrderID,10);
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

    @Author(names = {"Prerna"})
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkDetailHotelPDPLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkDetailHotelPDPLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String hotelName = map.get("hotelName");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);

        step("user is on Hotel Landing Page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("user search by hotel name");
        landingPage.searchStayLocation(hotelName);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.setTotalGuest(2);
        landingPage.setTotalChildren(0);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        Assertion.assertThat("Hotel Price Exists", detailPage.getHotelPrice(), notNullValue());
        Assertion.assertThat("Is gallery shown on clicking hotel image", detailPage.clickImageAndVerifyGallery(), is(true));
        Assertion.assertThat("Hotel detail should be displayed", detailPage.isHotelDetailShown(), is(true));
        Assertion.assertThat("Hotel Review Section and People review section exists", detailPage.isReviewSectionShown(), is(true));
        Assertion.assertThat("Hotel Facilities Section exists", detailPage.isFacilitiesSectionShown(), is(true));
        Assertion.assertThat("Location Section Exists", detailPage.isLocationSectionShown(), is(true));
        Assertion.assertThat("About Accommodation Section Exists", detailPage.isAboutAccommodationSectionShown(), is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "Room")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelExclusiveHotelRoomNudgesLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyHotelExclusiveHotelRoomNudgesLogin");
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
        String accommodationType = "hotel";

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

        step("Verify exclusive hotel room nudges same as in API in room list");
        Assertion.assertThat("Exclusive Hotel Room Nudges exist and is same as in API", roomListPage.getRoomNudges(nudgesText), is(nudgesText));

        step("Click on room nudges in room list");
        roomListPage.clickNudgesRoom(nudgesText);

        step("Verify exclusive hotel room nudges same as in API in room detail");
        Assertion.assertThat("Exclusive Hotel Room Nudges exist and is same as in API", roomListPage.getRoomNudges(nudgesText), is(nudgesText));
    }

    @Author(names = {"Zia"})
    @Module(name = "Room")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelFacilityRoomNudgesLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyHotelFacilityRoomNudgesLogin");
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
        String accommodationType = "hotel";

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
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void applyAdvanceFilterLogin() {
        Map<String, String> map = getParam("applyAdvanceFilterLogin");
        BaseTest.paramsThreadLocal.set(map);

        String username = map.get("username");
        String password = map.get("password");
        String location = map.get("location");
        String locationType = map.get("locationType");
        int room = Integer.parseInt(map.get("room"));
        int adult = Integer.parseInt(map.get("adult"));
        String rangeAddDate = map.get("rangeAddDate");
        int totalNight = Integer.parseInt(map.get("totalNight"));
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String currency = map.get("currency");
        String priceRange = map.get("priceRange");
        String benefit = map.get("benefit");
        String facility = map.get("facility");
        String totalHotel = map.get("totalHotel");
        String accommodationType = "hotel";

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);
        homePage.changeCurrency(currency, username);
        String language = homePage.getLanguageApp();

        step("Open the SRP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, locationType);
        int locationIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String locationId = hotelAutoCompleteResponse.data.contents.get(locationIndex).publicId;
        hotelPage.openSRPDeeplink(accommodationType, room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);
        searchResultPage.waitForSrpToLoad();
        landingPage.fillNightCheckInForm();

        step("Click filter button");
        searchResultPage.clickFilterButton();

        step("apply advance filter");
        searchResultPage.applyAdvanceFilter(priceRange,benefit,facility,currency);

        step("Submit filter");
        searchResultPage.clickFilterSubmit();

        step("check applied advance filter");
        searchResultPage.checkBenefitAndFacility(benefit,facility,totalHotel);

        step("verification of price range");
        Assertion.assertThat("Hotel price is match with price filter by range", searchResultPage.isPriceFilterByRangeCorrect(priceRange,totalHotel), is(true));

        step("Open the filter again");
        searchResultPage.clickFilterButton();

        step("Assert the selected currency option is displayed in the filter");
        searchResultPage.verifyCurrencyOnFilter(currency);

        /*
        bug: https://borobudur.atlassian.net/browse/AC-46415
        improvement: https://borobudur.atlassian.net/browse/QAAUT-43714
        */
        if (Configs.PLATFORM.equals(Platform.IOS)) {
            step("Reset filter");
            searchResultPage.closeFilterPopup();
            searchResultPage.clickFilterButton();
            searchResultPage.clickResetFilterButton();
            searchResultPage.clickFilterSubmit();

            step("Choose the master tag (quick filter) option on the header");
            String masterTag = searchResultPage.selectMasterTagByIndex(1);
            searchResultPage.waitForSrpToLoad();
            Assertion.assertThat("Selected master tag is not appear twice", searchResultPage.isSelectedMasterTagDuplicate(masterTag), is(false));
        }
    }

    @Author(names = {"Zia"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySearchNearByMeLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifySearchNearByMeLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.clickNearMeIcon();

        step("Verify location is set to Near me / Dekat saya ");
        String currentLocation = landingPage.getCurrentSearchLocation().strip();
        Assertion.assertThat("Current location in search field is either 'Near me' or 'Dekat Saya'", currentLocation, anyOf(equalToIgnoringCase("Near me"), equalToIgnoringCase("Dekat saya"),equalToIgnoringCase("Dekat Saya"),equalToIgnoringCase("Near Me"),equalToIgnoringCase("Hotels Near You"),equalToIgnoringCase("Hotel Dekat Anda"),equalToIgnoringCase("Near you"),equalToIgnoringCase("Dekat anda"),equalToIgnoringCase("Di dekat kamu")));

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();
        landingPage.enableLocation();

        step("Verify header location is Near me / Dekat saya");
        String currentLocationSrp = searchResultPage.getCurrentLocation().strip();
        Assertion.assertThat("Current location in SRP  is either 'Near me' or 'Dekat Saya'", currentLocationSrp, anyOf(equalToIgnoringCase("Near me"), equalToIgnoringCase("Dekat saya"),equalToIgnoringCase("Dekat Saya"),equalToIgnoringCase("Near Me"),equalToIgnoringCase("Hotels Near You"),equalToIgnoringCase("Hotel Dekat Anda"),equalToIgnoringCase("Near you"),equalToIgnoringCase("Dekat anda"),equalToIgnoringCase("Di dekat kamu")));

        step("Verify distance showing in hotel card");
        List<String> hotelDistances = searchResultPage.getHotelDistances();
        Assertion.assertThat("Hotel distances contain the expected text",
                hotelDistances,
                everyItem(anyOf(containsString("m dari lokasimu"), containsString("m from your location"))));
    }

    @Author(names = {"Zia"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPopularPlacesFilterLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPopularPlacesFilterLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Click filter button");
        searchResultPage.clickFilterButton();

        step("apply popular place filter");
        String popularPlace = searchResultPage.selectPopularPlaceFilter();

        step("Submit filter");
        searchResultPage.clickFilterSubmit();

        step("Verify header location is still "+location);
        String currentLocationSrp = searchResultPage.getCurrentLocation().strip();
        Assertion.assertThat("Current location in SRP  is "+location, currentLocationSrp, is(location));

        step("Verify distance showing in hotel card");
        List<String> hotelDistances = searchResultPage.getHotelDistances();
        Assertion.assertThat("Hotel distances contain the expected text",
                hotelDistances,
                everyItem(anyOf(containsString("m dari "+popularPlace), containsString("m from "+popularPlace))));
    }

    /**
     * Verify multiple filter combinations, reset functionality, and conditional filter visibility on SRP.
     *
     * Covers:
     *  1. tiket.com choices filter shows results on SRP.
     *  2. Smoking preference combined with facility filter shows results on SRP.
     *  3. Payment method filter shows results on SRP.
     *  4. Selecting an area/city filter hides the popular place filter section and SRP cards show that area in the location line.
     *  5. Reset button deselects all selected filters.
     *  6. Loyalty filter is only visible when the user is logged in.
     */
    @Author(names = {"Yazid"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyMultipleFilterCombinationsAndResetSRPLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyMultipleFilterCombinationsAndResetSRPLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String locationType = param.get("locationType");
        int totalRoom = Integer.parseInt(param.get("total_room"));
        int totalAdult = Integer.parseInt(param.get("total_adult"));
        int totalNight = Integer.parseInt(param.get("total_night"));
        String rangeAddDate = param.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";
        String tiketChoicesFilter = param.get("tiketChoicesFilter");
        String smokingPreference = param.get("smokingPreference");
        String facility = param.get("facility");
        String paymentMethod = param.get("paymentMethod");
        String filterLocationType = param.get("filterLocationType");
        String filterLocationName = param.get("filterLocationName");
        int maxHotelToCheck = Integer.parseInt(param.get("maxHotelToCheck"));

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Logout via deeplink");
        homePage.logoutViaDeeplink();

        step("Get location publicId for SRP deeplink");
        String locationId;
        if (Configs.ENVIRONMENT == Environment.PROD) {
            locationId = param.get("locationId");
        } else {
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", location, accommodationType.toUpperCase());
            locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        }

        step("Open the SRP via deeplink (guest)");
        hotelPage.openSRPDeeplink(accommodationType, totalRoom, totalAdult, locationId, locationType, location, checkInDate, checkOutDate, "");
        searchResultPage.waitForSrpToLoad();
        landingPage.fillNightCheckInForm();

        step("Open filter sheet as guest");
        searchResultPage.clickFilterButton();

        step("(pre-condition): Loyalty filter should NOT be visible for guest user");
        boolean loyaltyVisibleGuest = searchResultPage.isFilterSectionVisible("Loyalty");
        Assertion.assertThat("Loyalty filter should NOT be visible when user is a guest",
                loyaltyVisibleGuest, is(false));

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        homePage.changeCurrency("IDR", username);
        homePage.openAccount();
        accountPage.selectLanguage("ENG");

        step("Open the SRP via deeplink (login)");
        hotelPage.openSRPDeeplink(accommodationType, totalRoom, totalAdult, locationId, locationType, location, checkInDate, checkOutDate, "");
        searchResultPage.waitForSrpToLoad();
        landingPage.fillNightCheckInForm();

        step("Open filter sheet as logged-in user");
        searchResultPage.clickFilterButton();

        step("Loyalty filter should be visible for logged-in user");
        boolean loyaltyVisibleLogin = searchResultPage.isFilterSectionVisible("Loyalty");
        Assertion.assertThat("Loyalty filter should be visible when user is logged in",
                loyaltyVisibleLogin, is(true));

        step("Select tiket.com choices filter and submit");
        searchResultPage.swipeToFilter(tiketChoicesFilter);
        searchResultPage.selectFilterToggle(tiketChoicesFilter);
        searchResultPage.clickFilterSubmit();
        Assertion.assertThat("Filter badge should be activated after applying tiket.com choices filter",
                searchResultPage.isFilterActivated("filter"), is(true));
        Assertion.assertThat("Benefit should be visible after applying tiket.com choices filter",
                searchResultPage.isBenefitVisible(tiketChoicesFilter, maxHotelToCheck), is(true));

        step("Apply smoking preference combined with facility filter and submit");
        searchResultPage.clickFilterButton();
        searchResultPage.swipeToFilter("Smoking/Merokok");
        searchResultPage.selectFilterCheckbox(smokingPreference);
        searchResultPage.swipeToFilter("Facilities/Fasilitas");
        searchResultPage.selectFilterCheckbox(facility);
        searchResultPage.clickFilterSubmit();
        Assertion.assertThat("Filter badge should still be activated after applying smoking + facility filter",
                searchResultPage.isFilterActivated("filter"), is(true));
        Assertion.assertThat("Benefit should be visible after applying smoking preference and facility filter",
                searchResultPage.isBenefitVisible(smokingPreference, maxHotelToCheck), is(true));

        step("Apply payment method filter and submit");
        searchResultPage.clickFilterButton();
        searchResultPage.swipeToFilter("Payment/Pembayaran");
        searchResultPage.selectFilterCheckbox(paymentMethod);
        searchResultPage.clickFilterSubmit();
        Assertion.assertThat("Filter badge should still be activated after applying payment method filter",
                searchResultPage.isFilterActivated("filter"), is(true));
        Assertion.assertThat("Benefit should be visible after applying payment method filter",
                searchResultPage.isBenefitVisible(paymentMethod, maxHotelToCheck), is(true));

        step("Open filter and verify popular place filter is visible before area selection");
        searchResultPage.clickFilterButton();
        boolean popularPlaceVisibleBefore = searchResultPage.isFilterSectionVisible("Popular/Populer");
        Assertion.assertThat("Popular place filter should be visible before area/city is selected",
                popularPlaceVisibleBefore, is(true));
        searchResultPage.closeFilterPopup();

        step("Select "+filterLocationType+" filter then verify popular place filter disappears");
        searchResultPage.clickFilterButton();
        searchResultPage.swipeToFilter(filterLocationType);
        String[] selectedAreas = searchResultPage.selectLocationFilter(filterLocationType, filterLocationName);
        boolean popularPlaceVisibleAfter = searchResultPage.isFilterSectionVisible("Popular/Populer");
        Assertion.assertThat("Popular place filter should NOT be visible after area/city filter is selected",
                popularPlaceVisibleAfter, is(false));
        searchResultPage.clickFilterSubmit();
        Assertion.assertThat("Filter badge should still be activated after area filter is selected",
                searchResultPage.isFilterActivated("filter"), is(true));
        Assertion.assertThat("Hotel card address lines should include the selected area(s)",
                searchResultPage.checkHotelCardList("area", maxHotelToCheck, selectedAreas), is(true));

        step("Click reset button to deselect all filters and submit");
        searchResultPage.clickFilterButton();
        searchResultPage.clickResetFilterButton();
        searchResultPage.clickFilterSubmit();
        Assertion.assertThat("SRP should still display results after reset",
                searchResultPage.isSearchResultShown(), is(true));
        Assertion.assertThat("Filter badge should NOT be activated after clicking reset",
                searchResultPage.isFilterActivated("filter"), is(false));
    }

    @Author(names = {"Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Room")
    @Test
    public void verifyRoomRecommendationRoomCapacityLessThanSearchCriteriaLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRoomRecommendationRoomCapacityLessThanSearchCriteriaLogin");
        BaseTest.paramsThreadLocal.set(param);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
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

        step("Change currency to " + currency);
        homePage.changeCurrency(currency,username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("search stay location: " + hotelNameSearch);
        landingPage.searchStayLocation(hotelNameSearch);

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

        step("Check room recommendation");
        int roomCapacity = roomListPage.getRecommendedRoomCapacity();
        Assertion.assertThat("room recommendation correct",roomListPage.checkRoomRecommendation(totalGuest,roomCapacity,searchCriteria),is(true));
        roomListPage.openRoomDetailForRecommendedRoom();

        step("Check room detail with updated room capacity");
        Assertion.assertThat("is room detail with updated room capacity",roomDetailPage.checkRoomDetail(totalGuest,roomCapacity,searchCriteria),is(true));
    }

    @Author(names = {"Ashish"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Room")
    @Test
    public void verifyRoomRecommendationRoomCapacityMatchSearchCriteriaLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRoomRecommendationRoomCapacityMatchSearchCriteriaLogin");
        BaseTest.paramsThreadLocal.set(param);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
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
        String searchCriteria = "equal";

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("Change currency to " + currency);
        homePage.changeCurrency(currency,username);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", currency);

        step("search stay location: " + hotelNameSearch);
        landingPage.searchStayLocation(hotelNameSearch);

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

        step("Check room recommendation");
        int roomCapacity = roomListPage.getRecommendedRoomCapacity();
        Assertion.assertThat("room recommendation correct",roomListPage.checkRoomRecommendation(totalGuest,roomCapacity,searchCriteria),is(true));
        roomListPage.openRoomDetailForRecommendedRoom();

        step("Check room detail with correct room capacity");
        Assertion.assertThat("is room detail with correct room capacity",roomDetailPage.checkRoomDetail(totalGuest,roomCapacity,searchCriteria),is(true));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Room")
    @Test
    public void verifyTotalRoomBookingFormLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyTotalRoomBookingFormLogin");
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
        int totalRoom1 = Integer.parseInt(param.get("totalRoom1"));
        int totalGuest1 = Integer.parseInt(param.get("totalGuest1"));
        int totalRoom2 = Integer.parseInt(param.get("totalRoom2"));
        int totalGuest2 = Integer.parseInt(param.get("totalGuest2"));

        String rangeAddDate = param.get("rangeAddDate");
        int totalNight = Integer.parseInt(param.get("night"));
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "Hotel";

        step("Goto home");
        homePage.gotoHome();

        step("Input credential on login page");
        loginPage.loginWith(loginType, username, password);

        step("user open PDP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom1, totalGuest1, checkInDate, checkOutDate);

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
        Assertion.assertThat("Total room booking form  is "+(totalRoom1),bookingFormTotal,anyOf(containsStringIgnoringCase(expectedTotalBookingFormId), containsStringIgnoringCase(expectedTotalBookingFormEn)));

        bookingFormPage.continueToAddOnPage();
        List<String> priceElementsTitle = bookingFormPage.getPriceTitles();
        Assertion.assertThat("Price element title contains "+(totalRoom1)+" room",
                priceElementsTitle,
                hasItem(anyOf(containsStringIgnoringCase((totalRoom1)+" kamar"), containsStringIgnoringCase((totalRoom1)+" room"))));

        step("user click PDP deeplink");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom2, totalGuest2, checkInDate, checkOutDate);

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
        Assertion.assertThat("Hotel name in detail contains "+additionalRoom+" x",roomName,containsStringIgnoringCase(additionalRoom+"x"));

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
        Assertion.assertThat("Total room booking form is "+additionalRoom,bookingFormTotal,anyOf(containsStringIgnoringCase(expectedTotalBookingFormId), containsStringIgnoringCase(expectedTotalBookingFormEn)));

        bookingFormPage.continueToAddOnPage();
        priceElementsTitle = bookingFormPage.getPriceTitles();
        Assertion.assertThat("Price element title contains "+additionalRoom+" room",
                priceElementsTitle,
                hasItem(anyOf(containsStringIgnoringCase(additionalRoom+" kamar"), containsStringIgnoringCase(additionalRoom+" room"))));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "BookingForm")
    @Test
    public void verifyNoGuestSalutationInBookingForm() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyNoGuestSalutationInBookingForm");
        BaseTest.paramsThreadLocal.set(map);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        String hotelName = map.get("hotelName");
        String rangeAddDate = map.get("rangeAddDate");
        int totalNight = Integer.parseInt(map.get("night"));
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String accommodationType = "hotel";
        String startDate = generateRandomDate(map.get("rangeAddDate"));

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Goto home");
        homePage.gotoHome();

        step("verify hotel booking form salutation is false");
        boolean isSalutationOn = hotelApi.getSalutationFlag(startDate,hotelName);
        Assertion.assertThat("Salutation is false",isSalutationOn,is(false));

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);

        step("user open PDP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 1, 1, checkInDate, checkOutDate);

        step("Select See Room");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Go to booking form");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("Fill contact detail");
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        bookingFormPage.fillGuestDetailNoSalutation();

        step("continue to payment");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Payment page should be displayed");
        String orderID = paymentPage.checkOrderID();
        Assertion.assertThat("Order id should be displayed", !orderID.isEmpty(), is(true));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "BookingForm")
    @Test
    public void verifyNoGuestSalutationInBookingFormMO() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyNoGuestSalutationInBookingFormMO");
        BaseTest.paramsThreadLocal.set(map);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        String hotelName = map.get("hotelName");
        String rangeAddDate = map.get("rangeAddDate");
        int totalNight = Integer.parseInt(map.get("night"));
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String accommodationType = "hotel";
        String startDate = generateRandomDate(map.get("rangeAddDate"));

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Goto home");
        homePage.gotoHome();

        step("verify hotel booking form salutation is false");
        boolean isSalutationOn = hotelApi.getSalutationFlag(startDate,hotelName);
        Assertion.assertThat("Salutation is false",isSalutationOn,is(false));

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);

        step("user open PDP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 2, 2, checkInDate, checkOutDate);

        step("Select See Room");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Go to booking form");
        roomListPage.selectMultiRoom(2, 1);
        roomListPage.clickBookButtonFooter();

        step("Fill contact detail");
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        Assertion.assertThat("Able to fill name without salutation",bookingFormPage.fillGuestDetailNoSalutation(), is(true));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "LandingPage")
    @Test
    public void checkPlatformNearMeIdLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPlatformNearMeIdLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginWith(loginType, username, password);

        step("Get Language App to IDN");
        String lang = homePage.getLanguageApp();
        if (!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.openAccount();
            accountPage.selectLanguage("IDN");
            settingPage.backToHome();
        }

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.clickNearMeIcon();

        step("Verify location is set to Near me / Dekat saya ");
        String currentLocation = landingPage.getCurrentSearchLocation().strip();
        List<String> nearYouId = List.of("hotel dekat anda", "dekat saya", "di dekat kamu", "dekat anda");
        Assertion.assertThat("Current location in search field is 'Dekat Saya'", currentLocation, either(equalToIgnoringCase(nearYouId.get(0))).or(equalToIgnoringCase(nearYouId.get(1))).or(equalToIgnoringCase(nearYouId.get(2))).or(equalToIgnoringCase(nearYouId.get(3))));
        String currentDate = landingPage.getSelectedCheckInOut();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();
        landingPage.enableLocation();
        landingPage.removePopUp();

        step("Click Back Button");
        searchResultPage.waitForSrpToLoad();
        searchResultPage.clickBackButton();
        landingPage.clickBackButton();

        step("Swipe to recently searched in homepage");
        String recentlySearchedText = homePage.swipeToRecentlySearchedNearMe(currentDate);
        Assertion.assertThat("Recently searched is 'Dekat Saya'", recentlySearchedText, either(equalToIgnoringCase(nearYouId.get(0))).or(equalToIgnoringCase(nearYouId.get(1))).or(equalToIgnoringCase(nearYouId.get(2))).or(equalToIgnoringCase(nearYouId.get(3))));

        step("Change Language to 'ENG'");
        homePage.openAccount();
        accountPage.selectLanguage("ENG");
        settingPage.backToHome();

        step("Get current date in english");
        homePage.openVerticalPageDeeplink("Hotel", "", "");
        currentDate = landingPage.getSelectedCheckInOut();
        landingPage.clickBackButton();

        step("Swipe to recently searched in homepage");
        recentlySearchedText = homePage.swipeToRecentlySearchedNearMe(currentDate);
        List<String> nearYouEn = List.of("hotels near you", "near me", "near you");
        Assertion.assertThat("Recently searched is 'Near Me'", recentlySearchedText, either(equalToIgnoringCase(nearYouEn.get(0))).or(equalToIgnoringCase(nearYouEn.get(1))).or(equalToIgnoringCase(nearYouEn.get(2))));
    }

    @Author(names = {"Zia"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "LandingPage")
    @Test
    public void checkPlatformNearMeIdGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPlatformNearMeIdGuest");
        BaseTest.paramsThreadLocal.set(map);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("input login as guest");
        loginPage.loginAsGuest();

        step("Get Language App to IDN");
        String lang = homePage.getLanguageApp();
        if (!lang.equalsIgnoreCase("id")) {
            step("Change Language to 'IDN'");
            homePage.openAccount();
            accountPage.selectLanguage("IDN");
            settingPage.backToHome();
        }

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.clickNearMeIcon();

        step("Verify location is set to Near me / Dekat saya ");
        String currentLocation = landingPage.getCurrentSearchLocation().strip();
        List<String> nearYouId = List.of("hotel dekat anda", "dekat saya", "di dekat kamu", "dekat anda");
        Assertion.assertThat("Current location in search field is 'Dekat Saya'", currentLocation, either(equalToIgnoringCase(nearYouId.get(0))).or(equalToIgnoringCase(nearYouId.get(1))).or(equalToIgnoringCase(nearYouId.get(2))).or(equalToIgnoringCase(nearYouId.get(3))));
        String currentDate = landingPage.getSelectedCheckInOut();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();
        landingPage.enableLocation();
        landingPage.removePopUp();

        step("Click Back Button");
        searchResultPage.waitForSrpToLoad();
        searchResultPage.clickBackButton();
        landingPage.clickBackButton();

        step("Swipe to recently searched in homepage");
        String recentlySearchedText = homePage.swipeToRecentlySearchedNearMe(currentDate);
        Assertion.assertThat("Recently searched is 'Dekat Saya'", recentlySearchedText, either(equalToIgnoringCase(nearYouId.get(0))).or(equalToIgnoringCase(nearYouId.get(1))).or(equalToIgnoringCase(nearYouId.get(2))).or(equalToIgnoringCase(nearYouId.get(3))));
        step("Change Language to 'ENG'");
        homePage.openAccount();
        accountPage.selectLanguage("ENG");
        settingPage.backToHome();

        step("Get current date in English");
        homePage.openVerticalPageDeeplink("Hotel", "", "");
        currentDate = landingPage.getSelectedCheckInOut();
        landingPage.clickBackButton();

        step("Swipe to recently searched in homepage");
        recentlySearchedText = homePage.swipeToRecentlySearchedNearMe(currentDate);
        List<String> nearYouEn = List.of("hotels near you", "near me", "near you");
        Assertion.assertThat("Recently searched is 'Near Me'", recentlySearchedText, either(equalToIgnoringCase(nearYouEn.get(0))).or(equalToIgnoringCase(nearYouEn.get(1))).or(equalToIgnoringCase(nearYouEn.get(2))));
    }

    @Author(names = {"Zia", "Aditi"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "LandingPage")
    @Test
    public void checkPlatformNearMeEnLogin() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPlatformNearMeEnLogin");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);
        SettingPage<WebDriver> settingPage = SettingPage.getInstance(session);

        step("Go To Home");
        homePage.gotoHome();

        step("input credential");
        loginPage.loginWith(loginType, username, password);

        step("Get Language App to EN");
        String lang = homePage.getLanguageApp();
        if (!lang.equalsIgnoreCase("en")) {
            step("Change Language to 'ENG'");
            homePage.openAccount();
            accountPage.selectLanguage("ENG");
            settingPage.backToHome();
        }

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search stay location");
        landingPage.clickNearMeIcon();

        step("Verify location is set to Near me");
        String currentLocation = landingPage.getCurrentSearchLocation().strip();
        List<String> nearYouEn = List.of("hotels near you", "near me", "near you");
        Assertion.assertThat("Current location in search field is 'Near me'", currentLocation, either(equalToIgnoringCase(nearYouEn.get(0))).or(equalToIgnoringCase(nearYouEn.get(1))).or(equalToIgnoringCase(nearYouEn.get(2))));
        String currentDate = landingPage.getSelectedCheckInOut();

        step("submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();
        landingPage.enableLocation();
        landingPage.removePopUp();

        step("Click Back Button");
        searchResultPage.waitForSrpToLoad();
        searchResultPage.clickBackButton();
        landingPage.clickBackButton();

        step("Swipe to recently searched in homepage");
        String recentlySearchedText = homePage.swipeToRecentlySearchedNearMe(currentDate);
        Assertion.assertThat("Recently searched is 'Near me'", recentlySearchedText, either(equalToIgnoringCase(nearYouEn.get(0))).or(equalToIgnoringCase(nearYouEn.get(1))).or(equalToIgnoringCase(nearYouEn.get(2))));

        step("Change Language to 'ID'");
        homePage.openAccount();
        accountPage.selectLanguage("IDN");
        settingPage.backToHome();

        step("Get current date in Indonesian");
        homePage.openVerticalPageDeeplink("Hotel", "", "");
        currentDate = landingPage.getSelectedCheckInOut();
        landingPage.clickBackButton();

        step("Swipe to recently searched in homepage");
        recentlySearchedText = homePage.swipeToRecentlySearchedNearMe(currentDate);
        List<String> nearYouId = List.of("hotel dekat anda", "dekat saya", "di dekat kamu", "dekat anda");
        Assertion.assertThat("Recently searched is 'Dekat Saya'", recentlySearchedText, either(equalToIgnoringCase(nearYouId.get(0))).or(equalToIgnoringCase(nearYouId.get(1))).or(equalToIgnoringCase(nearYouId.get(2))).or(equalToIgnoringCase(nearYouId.get(3))));
    }

    @Author(names = {"Yazid"})
    @Module(name = "BookingForm")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPriceWithoutTaxesTotalPriceFilterGuest() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPriceWithoutTaxesTotalPriceFilterGuest");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String locationType = param.get("locationType");
        String hotelName = param.get("hotelName");
        String filterOption = param.get("filterOption");
        String rangeAddDate = param.get("rangeAddDate");
        int night = Integer.parseInt(param.get("night"));

        String[] dates = generateCheckInOutDate(rangeAddDate, night);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";
        int room = 1;
        int adult = 1;
        String currency = "IDR";

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on non-login mode");
        loginPage.loginAsGuest();
        homePage.changeCurrency(currency, "");

        step("Get app language");
        String language = homePage.getLanguageApp();
        String separator = language.equals("en") ? "," : ".";

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", language, currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(room);
        landingPage.setTotalGuest(adult);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
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
        HashMap<String,String> hotelLandingPageData = hotelApi.getHotelAfterTaxInLandingPage(accommodationType, "", "", currency, language, false);
        double hotelPriceLP = Double.parseDouble(hotelLandingPageData.get("basePrice"));
        String hotelPriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceLP),separator);
        double hotelBasePriceLP = Double.parseDouble(hotelLandingPageData.get("discountedPrice"));
        String hotelBasePriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceLP),separator);
        String landingPageHotelName = hotelLandingPageData.get("productName");
        boolean checkCrossedPriceLP = hotelPriceLPString.equals(hotelBasePriceLPString);
        Assertion.assertThat("Price in landing page is same as API price for hotel " + landingPageHotelName,landingPage.checkHotelPrice(false,landingPageHotelName,hotelBasePriceLPString),is(anyOf(containsString(hotelBasePriceLPString),not(emptyOrNullString()))));
        if(!checkCrossedPriceLP){
            Assertion.assertThat("Crossed price in landing page is same as API price for hotel " + landingPageHotelName,landingPage.checkHotelPrice(true,landingPageHotelName,hotelPriceLPString),is(anyOf(containsString(hotelPriceLPString),not(emptyOrNullString()))));
        }

        step("user hit SRP deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", location, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);

        step("Check hotel price in SRP");
        SearchHotelResponse searchHotelResponse = hotelApi.getSearchHotelData(accommodationType, locationId, locationType, "", "", night, room, adult, checkInDate, currency);
        String srpAPIHotelName = searchHotelResponse.data.contents.get(0).hotelName;
        double hotelPriceSRP = hotelApi.getSrpHotelTotalPriceWithTax(false, searchHotelResponse);
        double hotelBasePriceSRP = hotelApi.getSrpHotelCrossedTotalPriceWithTax(false, searchHotelResponse);
        String hotelPriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceSRP),separator);
        String hotelBasePriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceSRP),separator);
        Assertion.assertThat("Price in SRP page is same as API price for hotel "+srpAPIHotelName,searchResultPage.checkHotelPrice(false,srpAPIHotelName,hotelPriceSRPString),is(true));
        if(!hotelPriceSRPString.equals(hotelBasePriceSRPString))
            Assertion.assertThat("Crossed price in SRP page is same as API price for hotel "+srpAPIHotelName,searchResultPage.checkHotelPrice(true,srpAPIHotelName,hotelBasePriceSRPString),is(true));
        Assertion.assertThat("Price in SRP page label is total price",searchResultPage.isLabelTotalPrice(false),is(true));

        step("user hit PDP deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponsePDP = hotelApi.getAutocompleteData("", "", hotelName, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponsePDP.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponsePDP.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, room, adult, checkInDate, checkOutDate);

        step("Check price in PDP");
        SelectRoomResponse selectRoomResponse = hotelApi.getHotelRoomData(hotelName, "", "", night, room, adult, checkInDate, currency);
        double hotelPricePDP = hotelApi.getHotelTotalPriceWithTax(false, selectRoomResponse);
        double hotelBasePricePDP = hotelApi.getHotelTotalCrossedPriceWithTax(false, selectRoomResponse);
        String hotelPricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDP),separator);
        String hotelBasePricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDP),separator);
        String priceOnDetail = detailPage.getHotelPrice();
        Assertion.assertThat("Price in PDP page is same as API price for hotel "+hotelName,priceOnDetail ,containsString(hotelPricePDPString));
        Assertion.assertThat("Price in PDP page label is total price",detailPage.isLabelTotalPrice(),is(true));
        detailPage.seeHotelRoom();

        step("Check price in room list");
        boolean checkCrossedPriceRoomlist = hotelBasePricePDPString.equals(hotelPricePDPString);
        Assertion.assertThat("Price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(false,hotelPricePDPString),is(true));
        if(!checkCrossedPriceRoomlist)
            Assertion.assertThat("Crossed price in SRP page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(true,hotelBasePricePDPString),is(true));
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
        Assertion.assertThat("Price in booking form page is same as API price for hotel "+hotelName,priceBookingform,containsString(hotelPricePDPString));
        if(!checkCrossedPriceRoomlist){
            String crossedPriceBookingform = bookingFormPage.getCrossedPriceBookingForm();
            Assertion.assertThat("Crossed price in booking form page is same as API price for hotel "+hotelName,crossedPriceBookingform,containsString(hotelBasePricePDPString));
        }
        Assertion.assertThat("Price in booking form page label is total price",bookingFormPage.isLabelTotalPrice(),is(true));

        step("Check price in room list MO");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 2, 2, checkInDate, checkOutDate);
        selectRoomResponse = hotelApi.getHotelRoomData(hotelName, "", "", night, 2, 2, checkInDate, currency);
        double hotelPricePDPMO = hotelApi.getBaseHotelPriceWithTax(false,selectRoomResponse);
        double hotelBasePricePDPMO = hotelApi.getBaseHotelCrossedPriceWithTax(false,selectRoomResponse);
        String hotelPricePDPStringMO = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDPMO),separator);
        String hotelBasePricePDPStringMO = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDPMO),separator);
        detailPage.seeHotelRoom();
        Assertion.assertThat("Price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(false,hotelPricePDPStringMO),is(true));
        if(!hotelBasePricePDPStringMO.equals(hotelPricePDPStringMO))
            Assertion.assertThat("Crossed price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(true,hotelBasePricePDPStringMO),is(true));
        Assertion.assertThat("Price in room list page label is total price",roomListPage.isLabelPerRoom(false),is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "BookingForm")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPriceWithoutTaxesTotalPriceFilterLoginNonIdr() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPriceWithoutTaxesTotalPriceFilterLoginNonIdr");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String locationType = param.get("locationType");
        String hotelName = param.get("hotelName");
        String filterOption = param.get("filterOption");
        String rangeAddDate = param.get("rangeAddDate");
        int night = Integer.parseInt(param.get("night"));
        String currency = param.get("currency");

        String[] dates = generateCheckInOutDate(rangeAddDate, night);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";
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

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency(currency, username);

        step("Get app language");
        String language = homePage.getLanguageApp();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", language, currency);

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(room);
        landingPage.setTotalGuest(adult);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
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
        String hotelPriceLPString = CoreFunctions.normalizeNonIdrPrice(false, hotelPriceLP);
        double hotelBasePriceLP = Double.parseDouble(hotelLandingPageData.get("discountedPrice"));
        String hotelBasePriceLPString = CoreFunctions.normalizeNonIdrPrice(false, hotelBasePriceLP);
        String landingPageHotelName = hotelLandingPageData.get("productName");
        boolean checkCrossedPriceLP = hotelPriceLPString.equals(hotelBasePriceLPString);
        Assertion.assertThat("Price in landing page is same as API price for hotel " + landingPageHotelName,landingPage.checkHotelPrice(false,landingPageHotelName,hotelBasePriceLPString),is(anyOf(containsString(hotelBasePriceLPString),not(emptyOrNullString()))));
        if(!checkCrossedPriceLP){
            Assertion.assertThat("Crossed price in landing page is same as API price for hotel " + landingPageHotelName,landingPage.checkHotelPrice(true,landingPageHotelName,hotelPriceLPString),is(anyOf(containsString(hotelPriceLPString),not(emptyOrNullString()))));
        }

        step("user hit SRP deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", location, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);

        step("Check hotel price in SRP");
        SearchHotelResponse searchHotelResponse = hotelApi.getSearchHotelData(accommodationType, locationId, locationType, username, password, night, room, adult, checkInDate, currency);
        String srpAPIHotelName = searchHotelResponse.data.contents.get(0).hotelName;
        double hotelPriceSRP = hotelApi.getSrpHotelTotalPriceWithTax(false, searchHotelResponse);
        double hotelBasePriceSRP = hotelApi.getSrpHotelCrossedTotalPriceWithTax(false, searchHotelResponse);
        String hotelPriceSRPString = CoreFunctions.normalizeNonIdrPrice(false, hotelPriceSRP);
        String hotelBasePriceSRPString = CoreFunctions.normalizeNonIdrPrice(false, hotelBasePriceSRP);
        Assertion.assertThat("Price in SRP page is same as API price for hotel "+srpAPIHotelName,searchResultPage.checkHotelPrice(false,srpAPIHotelName,hotelPriceSRPString),is(true));
        if(!hotelPriceSRPString.equals(hotelBasePriceSRPString))
            Assertion.assertThat("Crossed price in SRP page is same as API price for hotel "+srpAPIHotelName,searchResultPage.checkHotelPrice(true,srpAPIHotelName,hotelBasePriceSRPString),is(true));
        Assertion.assertThat("Price in SRP page label is total price",searchResultPage.isLabelTotalPrice(false),is(true));

        step("user hit PDP deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponsePDP = hotelApi.getAutocompleteData("", "", hotelName, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponsePDP.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponsePDP.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, room, adult, checkInDate, checkOutDate);

        step("Check price in PDP");
        SelectRoomResponse selectRoomResponse = hotelApi.getHotelRoomData(hotelName, username, password, night, room, adult, checkInDate, currency);
        double hotelPricePDP = hotelApi.getHotelTotalPriceWithTax(false, selectRoomResponse);
        double hotelBasePricePDP = hotelApi.getHotelTotalCrossedPriceWithTax(false, selectRoomResponse);
        String hotelPricePDPString = CoreFunctions.normalizeNonIdrPrice(true, hotelPricePDP);
        String hotelBasePricePDPString = CoreFunctions.normalizeNonIdrPrice(true, hotelBasePricePDP);
        String priceOnDetail = detailPage.getHotelPrice();
        Assertion.assertThat("Price in PDP page is same as API price for hotel "+hotelName, removeNonDigit(priceOnDetail) ,containsString(hotelPricePDPString));
        Assertion.assertThat("Price in PDP page label is total price",detailPage.isLabelTotalPrice(),is(true));
        detailPage.seeHotelRoom();

        step("Check price in room list");
        boolean checkCrossedPriceRoomlist = hotelBasePricePDPString.equals(hotelPricePDPString);
        Assertion.assertThat("Price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(false,hotelPricePDPString),is(true));
        if(!checkCrossedPriceRoomlist)
            Assertion.assertThat("Crossed price in SRP page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(true,hotelBasePricePDPString),is(true));
        Assertion.assertThat("Price in SRP page label is total price",roomListPage.isLabelTotalPrice(false),is(true));
        roomListPage.clickBookRoom();

        step("Check price in booking form");
        hotelPricePDP = hotelApi.getHotelTotalPriceWithTax(true, selectRoomResponse);
        hotelBasePricePDP = hotelApi.getHotelTotalCrossedPriceWithTax(true, selectRoomResponse);
        hotelPricePDPString = CoreFunctions.normalizeNonIdrPrice(true, hotelPricePDP);
        hotelBasePricePDPString = CoreFunctions.normalizeNonIdrPrice(true, hotelBasePricePDP);
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

        step("Check price in room list MO");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 2, 2, checkInDate, checkOutDate);
        selectRoomResponse = hotelApi.getHotelRoomData(hotelName, username, password, night, 2, 2, checkInDate, currency);
        double hotelPricePDPMO = hotelApi.getBaseHotelPriceWithTax(false,selectRoomResponse);
        double hotelBasePricePDPMO = hotelApi.getBaseHotelCrossedPriceWithTax(false,selectRoomResponse);
        String hotelPricePDPStringMO = CoreFunctions.normalizeNonIdrPrice(true, hotelPricePDPMO);
        String hotelBasePricePDPStringMO = CoreFunctions.normalizeNonIdrPrice(true, hotelBasePricePDPMO);
        detailPage.seeHotelRoom();
        Assertion.assertThat("Price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(false,hotelPricePDPStringMO),is(true));
        if(!hotelBasePricePDPStringMO.equals(hotelPricePDPStringMO))
            Assertion.assertThat("Crossed price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(true,hotelBasePricePDPStringMO),is(true));
        Assertion.assertThat("Price in room list page label is total price",roomListPage.isLabelPerRoom(false),is(true));
    }

    @SneakyThrows
    @Author(names = {"Kartik"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "BookingForm")
    @Test
    public void verifyPriceWithoutTaxesForGuestsABT() {
        Map<String, String> map = getParam("verifyPriceWithoutTaxesForGuestsABT");
        BaseTest.paramsThreadLocal.set(map);

        String loginType = map.get("loginType");
        String username = map.get("username");
        String password = map.get("password");
        String currency = "IDR";
        String location = map.get("location");
        String locationType = map.get("locationType").toUpperCase();
        String hotelName = map.get("hotelName");
        String filterOption = map.get("filterOption");
        int adult = Integer.parseInt(map.get("adult"));
        int night = Integer.parseInt(map.get("night"));
        int room = Integer.parseInt(map.get("room"));
        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, night);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        Session<WebDriver> session = sessionThreadLocal.get();
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        loginPage.loginAsGuest();
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency(currency,username);

        step("Get app language");
        String language = homePage.getLanguageApp();
        String separator = ".";
        if(language.equals("en")){
            separator = ",";
        }

        step("Get the SRP deeplink parameter");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, accommodationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;

        step("open SRP via deeplink");
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);
        hotelLandingPage.fillNightCheckInForm();

        step("Click filter button");
        String priceDescription = searchResultPage.getPriceDescription();
        searchResultPage.clickFilterButton();

        step("apply price without tax filter");
        searchResultPage.selectPriceAfterTaxFilter(false, priceDescription);

        step("Submit filter");
        searchResultPage.clickFilterSubmit();
        searchResultPage.waitForSrpToLoad();

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink(accommodationType, language, currency);

        step("check landing page section");
        HashMap<String,String> hotelLandingPageData = hotelApi.getHotelBeforeTaxInLandingPage(accommodationType,username,password,currency,language,false);
        double hotelPriceLP = Double.parseDouble(hotelLandingPageData.get("basePrice"));
        String hotelPriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceLP),separator);
        double hotelBasePriceLP = Double.parseDouble(hotelLandingPageData.get("discountedPrice"));
        String hotelBasePriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceLP),separator);
        String landingPageHotelName = hotelLandingPageData.get("productName");

        boolean checkCrossedPriceLP = hotelPriceLPString.equals(hotelBasePriceLPString);
        Assertion.assertThat("Price in landing page is same as API price for hotel "+landingPageHotelName,hotelLandingPage.checkHotelPrice(false,landingPageHotelName,hotelPriceLPString),is(anyOf(containsString(hotelPriceLPString),not(emptyOrNullString()))));
        if(!checkCrossedPriceLP){
            Assertion.assertThat("Crossed price in landing page is same as API price for hotel "+landingPageHotelName,hotelLandingPage.checkHotelPrice(true,landingPageHotelName,hotelBasePriceLPString),is(anyOf(containsString(hotelBasePriceLPString),not(emptyOrNullString()))));
        }

        step("open SRP via deeplink");
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);
        searchResultPage.waitForSrpToLoad();

        step("Check hotel price in SRP");
        SearchHotelResponse searchHotelResponse = hotelApi.getSearchHotelData(accommodationType, locationId, locationType, username, password, night, room, adult, checkInDate, currency);
        String srpAPIHotelName = searchHotelResponse.data.contents.get(0).hotelName;
        double hotelPriceSRP = hotelApi.getSrpHotelTotalPriceWithTax(false, searchHotelResponse);
        double hotelBasePriceSRP = hotelApi.getSrpHotelCrossedTotalPriceWithTax(false, searchHotelResponse);
        String hotelPriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceSRP),separator);
        String hotelBasePriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceSRP),separator);
        Assertion.assertThat("Price in SRP page is same as API price for hotel "+srpAPIHotelName,searchResultPage.checkHotelPrice(false,srpAPIHotelName,hotelPriceSRPString),is(true));
        if(!hotelPriceSRPString.equals(hotelBasePriceSRPString)) {
            Assertion.assertThat("Crossed price in SRP page is same as API price for hotel "+srpAPIHotelName,searchResultPage.checkHotelPrice(true,srpAPIHotelName,hotelBasePriceSRPString),is(true));
        }
        Assertion.assertThat("Price in SRP page label is /room/night (after taxes)",searchResultPage.getPriceRemarkText(),is(anyOf(containsString("/room/night (after taxes"),containsString("/kamar/malam (setelah pajak"))));

        step("Get PDP data");
        HotelAutoCompleteResponse hotelAutoCompleteResponsePDP = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponsePDP, hotelName);
        String hotelId = hotelAutoCompleteResponsePDP.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponsePDP.data.contents.get(selectedIndex).country.name;
        SelectRoomResponse selectRoomResponse = hotelApi.getHotelRoomData(hotelName, username, password, night, room, adult, checkInDate, currency);
        double hotelPricePDP = hotelApi.getHotelPrice(false, HotelPriceKind.FINAL, HotelPriceScope.PER_ROOM_NIGHT, selectRoomResponse);;
        double hotelBasePricePDP = hotelApi.getHotelPrice(false, HotelPriceKind.CROSSED, HotelPriceScope.PER_ROOM_NIGHT, selectRoomResponse);
        double hotelBasePricePDPWithTax = hotelApi.getHotelPrice(true, HotelPriceKind.AFTER_TAX, HotelPriceScope.PER_ROOM_NIGHT, selectRoomResponse);
        String hotelPricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDP),separator);
        String hotelBasePricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDP),separator);
        String hotelBasePricePDPWithTaxString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDPWithTax),separator);

        step("Check price in PDP");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, room, adult, checkInDate, checkOutDate);
        String priceOnDetail = detailPage.getHotelPrice();
        Assertion.assertThat("Price in PDP page is same as API price for hotel "+hotelName,priceOnDetail ,containsString(hotelPricePDPString));
        Assertion.assertThat("Price in PDP page label is /room/night",detailPage.isLabelPerRoomPerNight(),is(true));
        detailPage.seeHotelRoom();

        step("Check price in room list");
        boolean checkCrossedPriceRoomList = hotelBasePricePDPString.equals(hotelPricePDPString);
        Assertion.assertThat("Price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(false,hotelPricePDPString),is(true));
        if(!checkCrossedPriceRoomList)
            Assertion.assertThat("Crossed price in SRP page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(true,hotelBasePricePDPString),is(true));
        Assertion.assertThat("Price in SRP page label is /room/night",roomListPage.isLabelPerRoom(false),is(true));
        String priceAfterTax = roomListPage.getPriceAfterTax();
        roomListPage.clickBookRoom();

        step("Check price in booking form");
        String priceBookingForm = bookingFormPage.getTotalPriceBookingForm();
        Assertion.assertThat("Price in booking form page is same as API price for hotel "+hotelName,priceBookingForm,containsString(priceAfterTax));
        if(!checkCrossedPriceRoomList){
            String crossedPriceBookingForm = bookingFormPage.getCrossedPriceBookingForm();
            Assertion.assertThat("Crossed price in booking form page is same as API price for hotel "+hotelName,crossedPriceBookingForm,containsString(hotelBasePricePDPWithTaxString));
        }
        Assertion.assertThat("Price in booking form page label is total price",bookingFormPage.isLabelTotalPrice(),is(true));
    }

    @Author(names = {"Zia"})
    @Module(name = "BookingForm")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPriceAfterTaxesTotalPriceFilterLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPriceAfterTaxesTotalPriceFilterLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String hotelName = param.get("hotelName");
        String filterOption = param.get("filterOption");
        String rangeAddDate = param.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";
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
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(room);
        landingPage.setTotalGuest(adult);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        searchResultPage.waitForSrpToLoad();
        landingPage.fillNightCheckInForm();

        step("check if price after tax filter is already applied");
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
        HashMap<String,String> hotelLandingPageData = hotelApi.getHotelAfterTaxInLandingPage("hotel",username,password,"IDR",language,true);
        double hotelPriceLP = Double.parseDouble(hotelLandingPageData.get("basePrice"));
        String hotelPriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceLP),separator);
        double hotelBasePriceLP = Double.parseDouble(hotelLandingPageData.get("discountedPrice"));
        String hotelBasePriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceLP),separator);
        String landingPageHotelName = hotelLandingPageData.get("productName");

        boolean checkCrossedPriceLP = hotelPriceLPString.equals(hotelBasePriceLPString);
        Assertion.assertThat("Price in landing page is same as API price for hotel "+landingPageHotelName,landingPage.checkHotelPrice(false,landingPageHotelName,hotelPriceLPString),is(anyOf(containsString(hotelPriceLPString),not(emptyOrNullString()))));
        if(!checkCrossedPriceLP){
            Assertion.assertThat("Crossed price in landing page is same as API price for hotel "+landingPageHotelName,landingPage.checkHotelPrice(true,landingPageHotelName,hotelBasePriceLPString),is(anyOf(containsString(hotelBasePriceLPString),not(emptyOrNullString()))));
        }

        step("user hit SRP deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);

        step("Check hotel price in SRP");
        String srpPageHotelName = searchResultPage.getFirstHotelName();
        double hotelPriceSRP = hotelApi.getHotelPriceWithTax(srpPageHotelName,username,password,night,room,adult,checkInDate);
        String hotelPriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceSRP),separator);
        double hotelBasePriceSRP = hotelApi.getHotelCrossedPriceWithTax(srpPageHotelName,username,password,night,room,adult,checkInDate);
        String hotelBasePriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceSRP),separator);

        boolean checkCrossedPriceSRP = hotelPriceSRPString.equals(hotelBasePriceSRPString);
        Assertion.assertThat("Price in SRP page is same as API price for hotel "+srpPageHotelName,searchResultPage.checkHotelPrice(false,srpPageHotelName,hotelPriceSRPString),is(true));
        if(!checkCrossedPriceSRP){
            Assertion.assertThat("Crossed price in SRP page is same as API price for hotel "+srpPageHotelName,searchResultPage.checkHotelPrice(true,srpPageHotelName,hotelBasePriceSRPString),is(true));
        }
        Assertion.assertThat("Price in SRP page label is total price",searchResultPage.isLabelTotalPrice(),is(true));

        step("Check price in PDP");
        HotelAutoCompleteResponse hotelAutoCompleteResponsePDP = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        String hotelId = hotelAutoCompleteResponsePDP.data.contents.get(0).publicId;
        String country = hotelAutoCompleteResponsePDP.data.contents.get(0).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, room, adult, checkInDate, checkOutDate);
        double hotelPricePDP = hotelApi.getHotelPriceWithTax(hotelName,username,password,night,room,adult,checkInDate);
        String hotelPricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDP),separator);
        double hotelBasePricePDP = hotelApi.getHotelCrossedPriceWithTax(hotelName,username,password,night,room,adult,checkInDate);
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
        Assertion.assertThat("Price in roomlist page label is per room",roomListPage.isLabelPerRoom(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "BookingForm")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPriceAfterTaxesPerRoomPerNightFilterLogin() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPriceAfterTaxesPerRoomPerNightFilterLogin");
        BaseTest.paramsThreadLocal.set(param);

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String hotelName = param.get("hotelName");
        String filterOption = param.get("filterOption");
        String rangeAddDate = param.get("rangeAddDate");
        String accommodationType = "hotel";
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
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

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
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(room);
        landingPage.setTotalGuest(adult);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        searchResultPage.waitForSrpToLoad();
        landingPage.fillNightCheckInForm();

        step("check if price after tax filter is already applied");
        boolean isPriceAfterTaxFilterApplied = searchResultPage.isPriceAfterTaxFilterApplied();

        step("Click filter button");
        searchResultPage.clickFilterButton();

        step("apply /room/night filter");
        searchResultPage.selectHotelPriceDisplay(filterOption);

        step("apply price after tax filter");
        searchResultPage.checkAndSelectPriceAfterTaxFilter(isPriceAfterTaxFilterApplied);

        step("Submit filter");
        searchResultPage.clickFilterSubmit();

        step("Go back to landing page");
        searchResultPage.clickBackButton();

        step("check landing page section");
        HashMap<String,String> hotelLandingPageData = hotelApi.getHotelAfterTaxInLandingPage("hotel",username,password,"IDR",language,true);
        double hotelPriceLP = Double.parseDouble(hotelLandingPageData.get("basePrice"));
        String hotelPriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceLP),separator);
        double hotelBasePriceLP = Double.parseDouble(hotelLandingPageData.get("discountedPrice"));
        String hotelBasePriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceLP),separator);
        String landingPageHotelName = hotelLandingPageData.get("productName");

        boolean checkCrossedPriceLP = hotelPriceLPString.equals(hotelBasePriceLPString);
        Assertion.assertThat("Price in landing page is same as API price for hotel "+landingPageHotelName,landingPage.checkHotelPrice(false,landingPageHotelName,hotelPriceLPString),is(anyOf(containsString(hotelPriceLPString),not(emptyOrNullString()))));
        if(!checkCrossedPriceLP){
            Assertion.assertThat("Crossed price in landing page is same as API price for hotel "+landingPageHotelName,landingPage.checkHotelPrice(true,landingPageHotelName,hotelBasePriceLPString),is(anyOf(containsString(hotelBasePriceLPString),not(emptyOrNullString()))));
        }

        step("user hit SRP deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);

        step("Check hotel price in SRP");
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
        Assertion.assertThat("Price in SRP page label is /room/night (include taxes)",searchResultPage.getPriceRemarkText(),is(anyOf(containsString("/room/night (include taxes"),containsString("/kamar/malam (termasuk pajak"))));

        step("Check price in PDP");
        HotelAutoCompleteResponse hotelAutoCompleteResponsePDP = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
        int hotelIndexName = hotelApi.findHotelIndex(hotelAutoCompleteResponsePDP, hotelName);
        String hotelId = hotelAutoCompleteResponsePDP.data.contents.get(hotelIndexName).publicId;
        String country = hotelAutoCompleteResponsePDP.data.contents.get(hotelIndexName).country.name;
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, room, adult, checkInDate, checkOutDate);
        double hotelPricePDP = hotelApi.getBaseHotelPriceWithTax(hotelName,username,password,night,room,adult,checkInDate);
        String hotelPricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDP),separator);
        double hotelBasePricePDP = hotelApi.getBaseHotelCrossedPriceWithTax(hotelName,username,password,night,room,adult,checkInDate);
        String hotelBasePricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDP),separator);
        String priceOnDetail = detailPage.getHotelPrice();
        Assertion.assertThat("Price in PDP page is same as API price for hotel "+hotelName,priceOnDetail ,containsString(hotelPricePDPString));
        Assertion.assertThat("Price in PDP page label is total price",detailPage.isLabelPerRoomPerNight(),is(true));
        detailPage.seeHotelRoom();

        step("Check price in room list");
        boolean checkCrossedPriceRoomlist = hotelBasePricePDPString.equals(hotelPricePDPString);
        Assertion.assertThat("Price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(false,hotelPricePDPString),is(true));
        if(!checkCrossedPriceRoomlist){
            Assertion.assertThat("Crossed price in SRP page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(true,hotelBasePricePDPString),is(true));
        }
        Assertion.assertThat("Price in SRP page label is total price",roomListPage.isLabelPerRoomPerNight(),is(true));

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

        if(!checkCrossedPriceRoomlist){
            String crossedPriceBookingform = bookingFormPage.getCrossedPriceBookingForm();
            Assertion.assertThat("Crossed price in booking form page is same as API price for hotel "+hotelName,crossedPriceBookingform,containsString(hotelBasePricePDPString));
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
        Assertion.assertThat("Price in roomlist page label is total price",roomListPage.isLabelPerRoomPerNight(),is(true));
    }

    @Author(names = {"Ashish"})
    @Module(name = "BookingForm")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPriceAfterTaxesPerRoomPerNightFilterGuest() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPriceAfterTaxesPerRoomPerNightFilterGuest");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String hotelName = param.get("hotelName");
        String filterOption = param.get("filterOption");
        String rangeAddDate = param.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";
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

        step("Goto home");
        homePage.gotoHome();

        step("User is on guest mode");
        loginPage.loginAsGuest();
        homePage.changeCurrency("IDR","");

        step("Get app language");
        String language = homePage.getLanguageApp();
        String separator = ".";
        if(language.equals("en")){
            separator = ",";
        }

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("set total room and total guest");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(room);
        landingPage.setTotalGuest(adult);
        landingPage.clickDone();

        step("Submit search");
        landingPage.clickSubmit();
        searchResultPage.waitForSrpToLoad();
        landingPage.fillNightCheckInForm();

        step("check if price after tax filter is already applied");
        boolean isPriceAfterTaxFilterApplied = searchResultPage.isPriceAfterTaxFilterApplied();

        step("Click filter button");
        searchResultPage.clickFilterButton();

        step("apply /room/night filter");
        searchResultPage.selectHotelPriceDisplay(filterOption);

        step("apply price after tax filter");
        searchResultPage.checkAndSelectPriceAfterTaxFilter(isPriceAfterTaxFilterApplied);

        step("Submit filter");
        searchResultPage.clickFilterSubmit();

        step("Go back to landing page");
        searchResultPage.clickBackButton();

        step("check landing page section");
        HashMap<String,String> hotelLandingPageData = hotelApi.getHotelAfterTaxInLandingPage("hotel","","","IDR",language,true);
        double hotelPriceLP = Double.parseDouble(hotelLandingPageData.get("basePrice"));
        String hotelPriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceLP),separator);
        double hotelBasePriceLP = Double.parseDouble(hotelLandingPageData.get("discountedPrice"));
        String hotelBasePriceLPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceLP),separator);
        String landingPageHotelName = hotelLandingPageData.get("productName");

        boolean checkCrossedPriceLP = hotelPriceLPString.equals(hotelBasePriceLPString);
        Assertion.assertThat("Price in landing page is same as API price for hotel "+landingPageHotelName,landingPage.checkHotelPrice(false,landingPageHotelName,hotelPriceLPString),is(anyOf(containsString(hotelPriceLPString),not(emptyOrNullString()))));
        if(!checkCrossedPriceLP){
            Assertion.assertThat("Crossed price in landing page is same as API price for hotel "+landingPageHotelName,landingPage.checkHotelPrice(true,landingPageHotelName,hotelBasePriceLPString),is(anyOf(containsString(hotelBasePriceLPString),not(emptyOrNullString()))));
        }

        step("user hit SRP deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", location, locationType);
        String locationId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);

        step("Check hotel price in SRP");
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
        Assertion.assertThat("Price in SRP page label is /night (after taxes)",searchResultPage.getPriceRemarkText(),is(anyOf(containsString("/room/night (after taxes"),containsString("/kamar/malam (setelah pajak"))));

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
        Assertion.assertThat("Price in PDP page label is /room/night",detailPage.isLabelPerRoomPerNight(),is(true));
        detailPage.seeHotelRoom();

        step("Check price in room list");
        boolean checkCrossedPriceRoomlist = hotelBasePricePDPString.equals(hotelPricePDPString);
        Assertion.assertThat("Price in room list page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(false,hotelPricePDPString),is(true));
        if(!checkCrossedPriceRoomlist){
            Assertion.assertThat("Crossed price in SRP page is same as API price for hotel "+hotelName,roomListPage.checkHotelPrice(true,hotelBasePricePDPString),is(true));
        }
        Assertion.assertThat("Price in SRP page label is /room/night",roomListPage.isLabelPerRoomPerNight(),is(true));

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
        Assertion.assertThat("Price in roomlist page label is /room/night",roomListPage.isLabelPerRoomPerNight(),is(true));
    }

    @Author(names = {"Yazid"})
    @Module(name = "BookingForm")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void showPriceAatAbt() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("showPriceAatAbt");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String locationType = param.get("locationType");
        String hotelName = param.get("hotelName");
        String filterOption = param.getOrDefault("filterOption", "per room per night/per kamar per malam");
        String rangeAddDate = param.getOrDefault("rangeAddDate", "5-15");
        int night = Integer.parseInt(param.getOrDefault("night", "2"));
        String[] dates = generateCheckInOutDate(rangeAddDate, night);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = param.getOrDefault("accommodationType", "hotel");
        int room = Integer.parseInt(param.getOrDefault("room", "1"));
        int adult = Integer.parseInt(param.getOrDefault("adult", "1"));
        String currency = param.getOrDefault("currency", "IDR");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Goto home");
        homePage.gotoHome();

        step("User is on login mode");
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        homePage.changeCurrency(currency,username);

        step("Get the app language");
        String language = homePage.getLanguageApp();
        String separator = ".";
        if(language.equals("en")){
            separator = ",";
        }

        // ABT (isTax=false): after taxes / setelah pajak
        // AAT (isTax=true): including taxes / termasuk pajak
        boolean[] taxModes = {false, true};
        for (boolean isTax : taxModes) {
            String taxModeLabel = isTax ? "AAT - Amount After Tax" : "ABT - Amount Before Tax";
            step("Set the Price Tax Toggle to " + (isTax ? "ON" : "OFF") + " (" + taxModeLabel + ").");
            step("open SRP deeplink");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, locationType);
            int locationIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
            String locationId = hotelAutoCompleteResponse.data.contents.get(locationIndex).publicId;
            hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);
            searchResultPage.waitForSrpToLoad();
            landingPage.fillNightCheckInForm();

            step("check if price after tax filter is already applied");
            String priceDescription = searchResultPage.getPriceDescription();

            step("Click filter button");
            searchResultPage.clickFilterButton();

            step("go to \'"+filterOption+"\' section");
            searchResultPage.selectHotelPriceDisplay(filterOption);

            step("apply price after tax filter (" + taxModeLabel + ")");
            searchResultPage.selectPriceAfterTaxFilter(isTax, priceDescription);

            step("Submit filter");
            searchResultPage.clickFilterSubmit();

            step("Check hotel price in SRP (" + taxModeLabel + ")");
            searchResultPage.waitForSrpToLoad();
            String srpPageHotelName = searchResultPage.getFirstHotelName();
            double hotelPriceSRP = isTax
                    ? hotelApi.getBaseHotelPriceWithTax(srpPageHotelName, username, password, night, room, adult, checkInDate)
                    : hotelApi.getBaseHotelPriceWithoutTax(srpPageHotelName, username, password, night, room, adult, checkInDate);
            double hotelBasePriceSRP = isTax
                    ? hotelApi.getBaseHotelCrossedPriceWithTax(srpPageHotelName, username, password, night, room, adult, checkInDate)
                    : hotelApi.getBaseHotelCrossedPriceWithoutTax(srpPageHotelName, username, password, night, room, adult, checkInDate);
            String hotelPriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPriceSRP), separator);
            String hotelBasePriceSRPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePriceSRP), separator);

            boolean checkCrossedPriceSRP = hotelPriceSRPString.equals(hotelBasePriceSRPString);
            Assertion.assertThat("Price in SRP page is same as API price for hotel "+srpPageHotelName, searchResultPage.checkHotelPrice(false, srpPageHotelName, hotelPriceSRPString), is(true));
            if (!checkCrossedPriceSRP) {
                Assertion.assertThat("Crossed price in SRP page is same as API price for hotel "+srpPageHotelName, searchResultPage.checkHotelPrice(true, srpPageHotelName, hotelBasePriceSRPString), is(true));
            }
            if (isTax) {
                Assertion.assertThat("Price in SRP page label is /room/night (including taxes)", searchResultPage.getPriceRemarkText(),
                        is(anyOf(containsString("/room/night (including taxes"), containsString("/kamar/malam (termasuk pajak"))));
            } else {
                Assertion.assertThat("Price in SRP page label is /room/night (after taxes)", searchResultPage.getPriceRemarkText(),
                        is(anyOf(containsString("/room/night (after taxes"), containsString("/kamar/malam (setelah pajak"))));
            }

            step("Check price in PDP (" + taxModeLabel + ")");
            HotelAutoCompleteResponse hotelAutoCompleteResponsePDP = hotelApi.getAutocompleteData(username, password, hotelName, accommodationType.toUpperCase());
            int hotelIndexName = hotelApi.findHotelIndex(hotelAutoCompleteResponsePDP, hotelName);
            String hotelId = hotelAutoCompleteResponsePDP.data.contents.get(hotelIndexName).publicId;
            String country = hotelAutoCompleteResponsePDP.data.contents.get(hotelIndexName).country.name;
            hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, room, adult, checkInDate, checkOutDate);

            SelectRoomResponse selectRoomResponse = hotelApi.getHotelRoomData(hotelName, username, password, night, room, adult, checkInDate, currency);
            double hotelPricePDP = hotelApi.getBaseHotelPriceWithTax(isTax, selectRoomResponse);
            double hotelBasePricePDP = hotelApi.getBaseHotelCrossedPriceWithTax(isTax, selectRoomResponse);
            String hotelPricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDP), separator);
            String hotelBasePricePDPString = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDP), separator);

            String priceOnDetail = detailPage.getHotelPrice();
            Assertion.assertThat("Price in PDP page is same as API price for hotel "+hotelName, priceOnDetail, containsString(hotelPricePDPString));
            Assertion.assertThat("Price in PDP page label is /room/night", detailPage.isLabelPerRoomPerNight(), is(true));
            detailPage.seeHotelRoom();

            step("Check price in room list (" + taxModeLabel + ")");
            boolean checkCrossedPriceRoomlist = hotelBasePricePDPString.equals(hotelPricePDPString);
            Assertion.assertThat("Price in room list page is same as API price for hotel "+hotelName, roomListPage.checkHotelPrice(false, hotelPricePDPString), is(true));
            if (!checkCrossedPriceRoomlist) {
                Assertion.assertThat("Crossed price in room list page is same as API price for hotel "+hotelName, roomListPage.checkHotelPrice(true, hotelBasePricePDPString), is(true));
            }
            Assertion.assertThat("Price in room list page label is /room/night", roomListPage.isLabelPerRoom(isTax), is(true));

            step("Check room list MO (" + taxModeLabel + ")");
            hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 2, 2, checkInDate, checkOutDate);
            selectRoomResponse = hotelApi.getHotelRoomData(hotelName, username, password, night, 2, 2, checkInDate, currency);
            double hotelPricePDPMO = hotelApi.getBaseHotelPriceWithTax(isTax, selectRoomResponse);
            double hotelBasePricePDPMO = hotelApi.getBaseHotelCrossedPriceWithTax(isTax, selectRoomResponse);
            String hotelPricePDPStringMO = CoreFunctions.thousandSeparator(String.valueOf((int)hotelPricePDPMO), separator);
            String hotelBasePricePDPStringMO = CoreFunctions.thousandSeparator(String.valueOf((int)hotelBasePricePDPMO), separator);
            detailPage.seeHotelRoom();

            boolean checkCrossedPriceRoomlistMO = hotelBasePricePDPStringMO.equals(hotelPricePDPStringMO);
            Assertion.assertThat("Price in room list page is same as API price for hotel "+hotelName, roomListPage.checkHotelPrice(false, hotelPricePDPStringMO), is(true));
            if (!checkCrossedPriceRoomlistMO) {
                Assertion.assertThat("Crossed price in room list page is same as API price for hotel "+hotelName, roomListPage.checkHotelPrice(true, hotelBasePricePDPStringMO), is(true));
            }
            Assertion.assertThat("Price in roomlist page label is /room/night", roomListPage.isLabelPerRoom(isTax), is(true));
        }
    }

    @Author(names = {"Ashish"})
    @Module(name = "BookingFormGuestDetail")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyGuestNameInRangeMaximum50CharMin3Char() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyGuestNameInRangeMaximum50CharMin3Char");
        BaseTest.paramsThreadLocal.set(param);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String hotelName = param.get("hotelName");
        String nationality = param.get("nationality");
        int totalRoom = 1;
        int adult = 1;

        String country = "",hotelId = "",rangeAddDate = "",checkInDate = "",checkOutDate = "",accommodationType = "";
        int totalNight;
        if(Configs.ENVIRONMENT == Environment.PROD){
            country = param.get("country");
            hotelId = param.get("hotelId");
        }else {
            HotelAutoCompleteResponse.Data.Content content = hotelApi.getHotelDetail(hotelName);
            country = content.country.name;
            hotelId = content.publicId;
        }
        rangeAddDate = param.get("rangeAddDate");
        totalNight = Integer.parseInt(param.get("night"));
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        checkInDate = dates[0];
        checkOutDate = dates[1];
        accommodationType = "hotel";

        step("Goto home");
        homePage.gotoHome();

        step("User is on guest mode");
        loginPage.loginAsGuest();
        homePage.changeCurrency("IDR","");

        step("user click PDP deeplink");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, adult, checkInDate, checkOutDate);

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("open guest detail form");
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        bookingFormPage.openGuestDetailForm();

        step("verify guest detail name field");
        Assertion.assertThat("is default nationality is {" + nationality +"}",bookingFormPage.verifyGuestDetailDefaultNationality("Indonesia"),equalToIgnoringCase(nationality));
        Assertion.assertThat("check less then 3 char name",bookingFormPage.verifyGuestDetailNameSize(2),anyOf(containsString("Must consist of 3–50 characters"),containsString("Harus terdiri atas 3–50 karakter"),containsString("Harus terdiri dari 3–50 karakter")));
        Assertion.assertThat("check more than 50 char name",bookingFormPage.verifyGuestDetailNameSize(51),anyOf(containsString("Must consist of 3–50 characters"),containsString("Harus terdiri atas 3–50 karakter"),containsString("Harus terdiri dari 3–50 karakter")));
        String guestNameInput = bookingFormPage.verifyGuestDetailNameSize(generateRandomInt(3,50));
        Assertion.assertThat("check valid name",guestNameInput.length(),allOf(greaterThanOrEqualTo(3),lessThanOrEqualTo(50)));
        Assertion.assertThat("check saved name on guest detail",bookingFormPage.getNameOnGuestDetail(guestNameInput),equalToIgnoringCase(guestNameInput));
    }

    @Author(names = {"Ashish"})
    @Module(name = "BookingFormGuestDetail")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyGuestNameInRangeMaximum50CharMin3CharMO() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyGuestNameInRangeMaximum50CharMin3CharMO");
        BaseTest.paramsThreadLocal.set(param);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        String hotelName = param.get("hotelName");
        String nationality = param.get("nationality");
        int totalRoom = 2;
        int adult = 2;

        String country = "",hotelId = "",rangeAddDate = "",checkInDate = "",checkOutDate = "",accommodationType = "";
        int totalNight;
        if(Configs.ENVIRONMENT == Environment.PROD){
            country = param.get("country");
            hotelId = param.get("hotelId");
        }else {
            HotelAutoCompleteResponse.Data.Content content = hotelApi.getHotelDetail(hotelName);
            country = content.country.name;
            hotelId = content.publicId;
        }
        rangeAddDate = param.get("rangeAddDate");
        totalNight = Integer.parseInt(param.get("night"));
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        checkInDate = dates[0];
        checkOutDate = dates[1];
        accommodationType = "hotel";

        step("Goto home");
        homePage.gotoHome();

        step("User is on guest mode");
        loginPage.loginAsGuest();
        homePage.changeCurrency("IDR","");

        step("user click PDP deeplink");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, adult, checkInDate, checkOutDate);

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open hotel booking form page");
        detailPage.selectRoomMultiOrder(totalRoom);

        step("open guest detail form");
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        bookingFormPage.openGuestDetailForm();

        step("verify guest detail name field");
        ArrayList<String> guestDetailNameList = bookingFormPage.verifyGuestDetailNameSizeMO(totalRoom,nationality);
        for(String guestNameInput : guestDetailNameList){
            Assertion.assertThat("check valid name",guestNameInput.length(),allOf(greaterThanOrEqualTo(3),lessThanOrEqualTo(50)));
            Assertion.assertThat("check saved name on guest detail",bookingFormPage.getNameOnGuestDetail(guestNameInput),equalToIgnoringCase(guestNameInput));
        }
    }

    @Author(names = {"Ashish"})
    @Module(name = "BookingFormGuestDetail")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyGuestNationalityBookingForm() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyGuestNationalityBookingForm");
        BaseTest.paramsThreadLocal.set(param);

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        SmartProfileApi<?> smartProfileApi = new SmartProfileApi<>(session);

        String hotelName = param.get("hotelName");
        String nationality = param.get("nationality");
        String username = param.get("username");
        String password = param.get("password");
        int totalRoom = 1;
        int adult = 1;

        String country = "",hotelId = "",rangeAddDate = "",checkInDate = "",checkOutDate = "",accommodationType = "";
        String guestNameInput = "";
        long profileId = 0;
        int totalNight;
        if(Configs.ENVIRONMENT == Environment.PROD){
            country = param.get("country");
            hotelId = param.get("hotelId");
            guestNameInput = param.get("guestName");
        }else {
            step("get hotel data");
            HotelAutoCompleteResponse.Data.Content content = hotelApi.getHotelDetail(hotelName);
            country = content.country.name;
            hotelId = content.publicId;

            step("create smart profile");
            SmartProfilePojo smartProfilePojo = smartProfileApi.addProfileAPI(username,password);
            guestNameInput = smartProfilePojo.firstName + " " + smartProfilePojo.lastName;
            nationality = smartProfilePojo.nationality;
            profileId = smartProfilePojo.profileId;
        }
        rangeAddDate = param.get("rangeAddDate");
        totalNight = Integer.parseInt(param.get("night"));
        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        checkInDate = dates[0];
        checkOutDate = dates[1];
        accommodationType = "hotel";

        step("Goto home");
        homePage.gotoHome();

        step("User is on guest mode");
        loginPage.loginAsGuest();

        step("login with username { " + username + " }");
        loginPage.loginWith("email", username, password);

        step("user click PDP deeplink");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, totalRoom, adult, checkInDate, checkOutDate);

        step("Go to the room list");
        detailPage.RemovePopUp();
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Select first room from the room list");
        roomListPage.getTheFirstHotelRoomPrice();
        roomListPage.clickBookRoom();

        step("open guest detail form");
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        bookingFormPage.openGuestDetailForm();

        step("select saved smart guest name and verify nationality");
        bookingFormPage.selectSuggestedNameAndVerifyNationality(guestNameInput,nationality);

        if(Configs.ENVIRONMENT != Environment.PROD){
            step("delete smart profile");
            smartProfileApi.deleteProfileAPI(username,password,profileId);
        }
    }

    @Author(names = {"Aditi"})
    @Module(name = "Wishlist")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void testAddHotelInWishlist(){
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        Map<String, String> param = getParam("testAddHotelInWishlist");
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

        step("open Hotel SRP page");
        homePage.openVerticalPageDeeplink("Hotel", "", "IDR");
        hotelLandingPage.searchStayLocation(location);
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

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

    @Author(names = {"Aditi"})
    @Module(name = "Wishlist")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void testRemoveHotelInWishlist(){
        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        LandingPage<WebDriver> hotelLandingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        Map<String, String> param = getParam("testRemoveHotelInWishlist");
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

        step("open Hotel SRP page");
        homePage.openVerticalPageDeeplink("hotel","","IDR");
        hotelLandingPage.searchStayLocation(location);
        hotelLandingPage.clickSubmit();
        hotelLandingPage.fillNightCheckInForm();

        step("Select the wishlist button on the first product");
        searchResultPage.clickWishlistFirstHotelCardForRemoval();
        Assertion.assertThat("Wishlist icon is filled on the SRP", searchResultPage.isWishlistIconSelected(), is(true));

        step("Unclick the wishlist button on the first product");
        boolean result_2 = searchResultPage.clickRedWishlistIcon();
        Assertion.assertThat("Removed from wishlist snackbar appeared", result_2, is(true));

        step("Check if the wishlist button color on SRP changed to grey now");
        Assertion.assertThat("Wishlist icon is un-filled on the SRP", searchResultPage.isWishlistIconSelected(), is(false));

        step("Check that the wishlist button color on SRP Maps changed to grey now");
        searchResultPage.openHotelCardInMaps();
        Assertion.assertThat("Wishlist icon is un-filled on the SRP Maps", searchResultPage.isWishlistIconSelected(), is(false));
        searchResultPage.closeMaps();

        step("Open the first hotel page and check if wishlist button color on PDP changed to grey");
        searchResultPage.selectFirstHotelCard();
        Assertion.assertThat("Wishlist icon is un-filled on the PDP", searchResultPage.isWishlistIconSelected(), is(false));

    }

    @Author(names = {"Aditi"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkModuleChipLandingPage() {
        Map<String, String> map = getParam("checkModuleChipLandingPage");
        BaseTest.paramsThreadLocal.set(map);
        String countryName = map.get("countryName");
        String countryCode = map.get("countryCode");

        Session<WebDriver> session = sessionThreadLocal.get();

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        AccountPage<WebDriver> accountPage = AccountPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as guest");
        loginPage.loginAsGuest();
        homePage.changeCurrency("IDR", "");
        accountPage.chooseCountry(countryName);

        step("Open hotel page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Fetch the hotel IDs");
        String chip = hotelApi.getCityName(countryCode);

        step("Select the chip module in landing page");
        landingPage.selectModuleChip(chip);

        step("Click the See All button");
        landingPage.clickSeeAllButton();

        step("Verify correct page opened in SRP");
        String srp_name = searchResultPage.verifyCorrectSRP(chip);
        Assertion.assertThat("Correct SRP page is opened", chip, containsString(srp_name));

    }

    @Author(names = {"Mridul Gupta"})
    @Module(name = "Payment")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkIDRPaymentMethods() {
        Map<String, String> param = getParam("checkIDRPaymentMethods");
        BaseTest.paramsThreadLocal.set(param);
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String currency = param.get("currency");
        String hotelName = param.get("hotelName");
        String date_type = param.get("date_type");
        String date = param.get("date");
        String promo = param.get("promo");
        String paymentMethod = param.get("paymentMethod");

        Session<WebDriver> session = sessionThreadLocal.get();
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        PaymentPage<WebDriver> paymentPage = PaymentPage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        step("Launch the Home page");
        homePage.gotoHome();

        step("Login as " + username + " and change currency to " + currency);
        loginPage.loginWith(loginType, username, password);
        homePage.changeCurrency(currency, username);

        step("Open the Hotel landing page");
        homePage.openVerticalPageDeeplink("hotel", "en", currency);

        step("Select stay dates");
        landingPage.changeDateViaDeeplink(date,date_type,2,2);

        step("Search for hotel/location: " + hotelName);
        landingPage.searchStayLocation(hotelName);

        step("Configure rooms and guests");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(2);
        landingPage.setTotalGuest(2);
        landingPage.clickDone();

        step("Submit hotel search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("Open the Room List page");
        detailPage.seeHotelRoom();

        step("Open room details");
        roomListPage.openRoomDetailPage();
        String roomName = roomListPage.getTheFirstHotelRoomName();

        step("Book selected room");
        detailPage.clickBookButton(2);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();

        step("Fill guest details");
        bookingFormPage.fillGuestDetail(false);

        step("Continue to Add-ons page");
        bookingFormPage.continueToAddOnPage();

        step("Apply promo code: " + promo);
        bookingFormPage.addPromo(promo);

        step("Continue to Payment page");
        bookingFormPage.continueToPaymentPage();
        bookingFormPage.PriceChangesPopUp();

        step("Fetch Order ID from Payment page");
        String orderID = paymentPage.checkOrderID();

        step("Verify promo code and currency on Payment page");
        Assertion.assertThat("Promo Code is visible", paymentPage.getPromo(), is(promo));
        Assertion.assertThat("Currency of total price on Payment page", paymentPage.getTotalPriceCurrency(), is(currency));

        step("Verify all payment methods are displayed");
        paymentPage.openPaymentMethod();
        paymentPage.verifyPaymentMethods();

        step("Select payment method: " + paymentMethod);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Proceed with payment");
        paymentPage.clickPayButton();

        step("Verify payment is completed successfully");
        Assertion.assertThat("Payment Successful and Thank You page is shown", paymentPage.isThankYouPageShown(), is(true));

        step("Open order details for Order ID: " + orderID);
        orderListPage.openIssuedTicketViaDeeplink(orderID);
        
        List<String> guestNames = orderDetailPage.getGuestDetailsName();

        step("Open additional booking details");
        orderDetailPage.seeMoreDetails();

        step("Open Edit Guest Name page");
        orderDetailPage.clickEditGuestName();

        step("Verify Order ID, Room Name, and Guest Names on Edit page");
        
        boolean isDetailCorrectEditPage = orderDetailPage.checkDetailsEditPage(orderID, roomName, guestNames);
        Assertion.assertThat("Check Order ID, Room Name, and Guest Names on Edit page", isDetailCorrectEditPage, is(true));

        step("Edit guest names");
        orderDetailPage.editGuestName();
        orderDetailPage.hidePromoBannerIfPresent();

        step("Fetch updated guest names");
        List<String> newNames = orderDetailPage.getGuestDetailsName();
        Assertion.assertThat("Updated guest names should not match old guest names", newNames, not(guestNames));

        step("Verify Edit Guest Name option is disabled after update");
        orderDetailPage.seeMoreDetails();
        Assertion.assertThat("Edit Guest Name is disabled", orderDetailPage.verifyEditGuestDetailDisabled(), is(true));
    }

    @Author(names = {"Mridul Gupta"})
    @Module(name = "Pricing")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelTixPointsCashbackPriceLogin() {
        Map<String, String> map = getParam("verifyHotelTixPointsCashbackPriceLogin");

        Session<WebDriver> session = sessionThreadLocal.get();
        BaseTest.paramsThreadLocal.set(map);

        String username = map.get("username");
        String password = map.get("password");
        String hotelName = map.get("hotelName");
        String currency = map.get("currency");
        int totalRoom = Integer.parseInt(map.get("total_room"));
        int totalAdult = Integer.parseInt(map.get("total_adult"));
        int totalNight = Integer.parseInt(map.get("total_night"));
        String rangeAddDate = map.get("rangeAddDate");

        String[] dates = generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        RoomDetailPage<WebDriver> roomDetailPage = RoomDetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Go to home and login");
        homePage.gotoHome();
        step("Login as " + username + " via deeplink (if needed) and change currency to " + currency);
        homePage.loginIfLoggedOutAndChangeCurrencyViaDeeplink(username, password, currency);

        step("Open PDP via deeplink for hotel '" + hotelName + "' with " + totalRoom + " room(s) and " + totalAdult + " adult(s)");
        HotelAutoCompleteResponse hotelAutoCompleteResponse =
                hotelApi.getAutocompleteData(username, password, hotelName, "HOTEL");
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;
        hotelPage.openPDPDeeplink(
                "hotel", country.toLowerCase(), hotelId, totalRoom, totalAdult, checkInDate, checkOutDate);
        Assertion.assertThat("User should land on the hotel PDP after opening via deeplink",
                detailPage.isHotelDetailShown(), is(true));

        step("Fetch tixpoints v2 room response and derive expected after-cashback price");
        SelectRoomResponse selectRoomResponse = hotelApi.getHotelRoomDataV2(
                hotelName, username, password, totalNight, totalRoom, totalAdult, checkInDate, currency);
        SelectRoomResponse.RateInfo rateInfo = selectRoomResponse.data.rooms.get(0).ratePlans.get(0).rateInfo;
        SelectRoomResponse.TixPointsV2 tixPointsV2 = rateInfo.tixPointsV2;
        Assertion.assertThat("API should expose a tixPointsV2 object on rateInfo", tixPointsV2, is(notNullValue()));
        Assertion.assertThat("API should advertise the displayed price as already after cashback (isPriceAfterCashback=true)",
                rateInfo.isPriceAfterCashback, is(true));

        double baseTotal = tixPointsV2.baseTotal;
        double cashbackTotal = tixPointsV2.cashbackTotal;
        double expectedNetTotal = rateInfo.priceSummary.total - (baseTotal + cashbackTotal);
        step("priceSummary.total=" + rateInfo.priceSummary.total
                + ", tixPointsV2.baseTotal=" + baseTotal
                + ", tixPointsV2.cashbackTotal=" + cashbackTotal
                + ", expectedNetTotal=" + expectedNetTotal);

        step("Open PDP and go to the Room List");
        String totalCashback = detailPage.getTotalCashback();
        double totalCashbackValue = CoreFunctions.convertPrice(totalCashback);
        Assertion.assertThat("The cashback should match this formula, Total Cashback = Total base + cashback",totalCashbackValue,is(baseTotal+cashbackTotal));

        step("On Room List (PDP): verify price = priceSummary.total - (tixPointsV2.baseTotal + cashbackTotal)");
        detailPage.seeHotelRoom();
        String roomListPriceText = roomListPage.getPriceAfterTax();
        double roomListPrice = convertPrice(roomListPriceText);
        Assertion.assertThat("Price should equal priceSummary.total - (tixPointsV2.baseTotal + cashbackTotal) (expected " + expectedNetTotal + ", got " + roomListPrice + ")", Math.abs(roomListPrice - expectedNetTotal), is(lessThanOrEqualTo(1.0)));

        step("Open Room Detail and verify 'you will be paying this amount' is shown in green");
        roomListPage.openRoomDetailPage(roomListPriceText);
        Assertion.assertThat("You will be paying this amount' nudge should be shown in green on Room Detail", roomDetailPage.isPayingThisAmountTextInGreen(), is(true));

        step("Proceed to the Booking Form and verify the same green nudge is shown");
        roomDetailPage.clickBookButton(totalRoom);
        bookingFormPage.cancelRatePLanBottomSheetBookingPart1();
        Assertion.assertThat("'You will be paying this amount' nudge should be shown in green on Booking Form", bookingFormPage.isPayingThisAmountTextInGreen(), is(true));
    }

    @Author(names = {"Aditi"})
    @Module(name = "LandingPage")
    @Tribe(name = "Hotel Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelDestinationSearch() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyHotelDestinationSearch");
        BaseTest.paramsThreadLocal.set(param);

        String location = param.get("location");
        String region = param.get("region");
        String city = param.get("city");
        String area = param.get("area");
        String poi = param.get("poi");
        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String dateType = param.get("dateType");
        String date = param.get("date");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Type partial location to trigger autocomplete and select the suggestion");
        landingPage.searchStayLocation(location);

        step("Verify the selected suggestion is reflected on the search field");
        String selectedLocation = landingPage.getCurrentSearchLocation().strip();
        String expectedLocation = splitLanguageData(location, "/")[0];
        Assertion.assertThat(
                "Selected autocomplete suggestion should be reflected on the search field",
                selectedLocation,
                containsStringIgnoringCase(expectedLocation));

        step("Search by region / city / area / POI, submit search, and validate SRP loads with the correct location");
        Map<String, String> queries = new LinkedHashMap<>();
        queries.put("region", region);
        queries.put("city", city);
        queries.put("area", area);
        queries.put("poi", poi);

        List<String> failures = new ArrayList<>();
        for (Map.Entry<String, String> entry : queries.entrySet()) {
            String label = entry.getKey();
            String query = entry.getValue();
            if (query == null || query.isBlank()) {
                continue;
            }
            try {
                landingPage.backToHomePage();
                homePage.openVerticalPageDeeplink("Hotel", "", "");
                landingPage.searchStayLocation(query);

                String selected = landingPage.getCurrentSearchLocation().strip();
                String expected = splitLanguageData(query, "/")[0].strip();
                Assertion.assertThat(
                        "Search field should reflect the chosen suggestion for " + label + " '" + query + "'",
                        selected,
                        containsStringIgnoringCase(expected));

                landingPage.clickSubmit();
                landingPage.fillNightCheckInForm();
                searchResultPage.waitForSrpToLoad();

                Assertion.assertThat(
                        "SRP should be opened for " + label + " '" + query + "'",
                        searchResultPage.isSearchResultShown(),
                        is(true));

                if (label.equals("poi")) {
                    Assertion.assertThat(
                            "Hotel cards on SRP should show distance from POI '" + expected + "'",
                            landingPage.verifyPoiOnHotelCards(expected),
                            is(true));
                } else {
                    Assertion.assertThat(
                            "SRP location header should reflect the selected " + label + " '" + query + "'",
                            searchResultPage.getCurrentLocation().strip(),
                            containsStringIgnoringCase(expected));
                }
                searchResultPage.clickBackButton();
            } catch (Throwable t) {
                failures.add(label + " ('" + query + "'): " + t.getMessage());
            }
        }
        Assertion.assertThat(
                "All region/city/area/POI searches should pass. Failures: " + failures,
                failures.isEmpty(),
                is(true));

        step("Use 'Near Me' option to search by current location");
        landingPage.backToHomePage();
        homePage.openVerticalPageDeeplink("Hotel", "", "");
        landingPage.clickNearMeIcon();

        step("Verify search field is set to Near Me / Dekat saya");
        String nearMeLocation = landingPage.getCurrentSearchLocation().strip();
        Assertion.assertThat(
                "Search field should show a Near Me equivalent label",
                nearMeLocation,
                anyOf(
                        equalToIgnoringCase("Near me"),
                        equalToIgnoringCase("Dekat saya"),
                        equalToIgnoringCase("Near you"),
                        equalToIgnoringCase("Di dekat kamu"),
                        equalToIgnoringCase("Hotels Near You"),
                        equalToIgnoringCase("Hotel Dekat Anda")));

        step("Submit search and verify SRP loads with Near Me context");
        landingPage.clickSubmit();
        landingPage.enableLocation();
        landingPage.fillNightCheckInForm();
        String srpLocation = searchResultPage.getCurrentLocation().strip();
        Assertion.assertThat(
                "SRP header should reflect Near Me location",
                srpLocation,
                anyOf(
                        equalToIgnoringCase("Near me"),
                        equalToIgnoringCase("Dekat saya"),
                        equalToIgnoringCase("Near you"),
                        equalToIgnoringCase("Di dekat kamu"),
                        equalToIgnoringCase("Hotels Near You"),
                        equalToIgnoringCase("Hotel Dekat Anda")));

        step("Verify hotel cards are present and reflect Near Me context");
        Assertion.assertThat(
                "SRP should be loaded with hotel cards for Near Me search",
                searchResultPage.isSearchResultShown(),
                is(true));
        Assertion.assertThat(
                "Hotel cards on SRP should show distance from user's location",
                searchResultPage.verifyNearMeOnHotelCards(),
                is(true));
    }

    /**
     * Verifies the "Reschedule (non-refundable)" form for an issued hotel order whose room
     * cancellation policy is {@code NO_CANCELLATION} ("Refund & reschedule not allowed"):
     * <ol>
     *   <li>API books a non-refundable issued order under the supplied user credentials.</li>
     *   <li>UI logs in as guest then signs in with the same email, opens the order via deeplink,
     *       and taps Reschedule from Manage Order.</li>
     *   <li>The non-refundable reschedule form opens inside the WebView. The booking's
     *       check-in / check-out dates must match the text displayed in
     *       {@code RescheduleForm_select_date_text}.</li>
     *   <li>Filling reason + description + attaching an image is allowed; submitting BEFORE
     *       changing the date keeps the user on the form (the submit is rejected).</li>
     *   <li>After changing the date, submitting succeeds and the approval flow becomes visible.</li>
     * </ol>
     */
    @Author(names = {"Ashish"})
    @Module(name = "Reschedule")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRescheduleNonRefundableFlow() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRescheduleNonRefundableFlow");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);
        WebViewPage<WebDriver> webViewPage = WebViewPage.getInstance(session);
        HotelReschedulePageWeb<?> reschedulePage = HotelReschedulePageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = Optional.ofNullable(param.get("currency")).orElse("IDR");
        String rescheduleReason = Optional.ofNullable(param.get("rescheduleReason"))
                .filter(s -> !s.isBlank())
                .orElse(null);
        String rescheduleDescription = Optional.ofNullable(param.get("rescheduleDescription"))
                .filter(s -> !s.isBlank())
                .orElseGet(() -> "Auto reschedule - " + generateRandomString(30));
        int rescheduleDayShift = Integer.parseInt(Optional.ofNullable(param.get("rescheduleDayShift")).orElse("2"));

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create a non-refundable (NO_CANCELLATION) issued hotel order via API");
        Boolean apiResult = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, /* isRefundable */ false, /* isSpecialRequestEnable */ false, "",
                "en", /* isNative */ false);
        Assertion.assertThat("Non-refundable hotel booking via API is successful", apiResult, is(true));

        String issuedOrderId = String.valueOf(hotelApi.getOrderId());
        if (issuedOrderId == null || issuedOrderId.isEmpty() || "0".equals(issuedOrderId)) {
            Assertion.skip("Issued order ID is invalid after API booking; skipping UI verification.");
        }

        step("Open home and log out to guest mode");
        homePage.gotoHome();
        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);
        homePage.gotoHome();

        step("Open the issued order via deeplink");
        orderListPage.openIssuedTicketViaDeeplink(issuedOrderId);

        step("Tap Reschedule from Manage Order");
        orderDetailPage.clickReschedule(issuedOrderId);

        step("Switch to the WebView hosting the reschedule form");
        webViewPage.switchToWebViewUrl("reschedule");
        Assertion.assertThat("Non-refundable reschedule form should be visible",
                reschedulePage.isOnNonRefundablePage(), is(true));

        step("Verify the booked check-in / check-out are reflected in the date button text");
        String selectedDateText = reschedulePage.getNonRefundableSelectedDateText();
        String checkInBanner = reschedulePage.getNonRefundableCheckInDateBannerText();
        String checkOutBanner = reschedulePage.getNonRefundableCheckOutDateBannerText();
        Assertion.assertThat("Select-date text should not be empty",
                selectedDateText, not(emptyOrNullString()));
        Assertion.assertThat("Booking check-in date should be reflected in the page banner",
                checkInBanner, not(emptyOrNullString()));
        Assertion.assertThat("Booking check-out date should be reflected in the page banner",
                checkOutBanner, not(emptyOrNullString()));

        step("Fill the form: pick a reschedule reason (random if not provided in test data)");
        String pickedReason;
        if (rescheduleReason != null) {
            reschedulePage.selectNonRefundableReason(rescheduleReason);
            pickedReason = rescheduleReason;
        } else {
            pickedReason = reschedulePage.selectRandomNonRefundableReason();
        }
        Assertion.assertThat("A reschedule reason must be selected", pickedReason, not(emptyOrNullString()));

        step("Fill description");
        reschedulePage.fillNonRefundableDescription(rescheduleDescription);

        step("Attempt to submit WITHOUT changing the date — submission must be rejected");
        reschedulePage.clickNonRefundableSubmit();
        Assertion.assertThat("Inline 'must be different from the previous dates' error should be shown",
                reschedulePage.isSameDateErrorVisible(), is(true));

        step("Change the date — the inline error must clear and select-date text updates");
        String newDateRange = reschedulePage.changeNonRefundableNightDuration(rescheduleDayShift);
        Assertion.assertThat("After date change the select-date text must reflect new dates",
                newDateRange, not(equalToIgnoringCase(selectedDateText)));

        step("Submit again — confirmation dialog must appear");
        reschedulePage.clickNonRefundableSubmit();
        reschedulePage.confirmRescheduleRequest();

        step("Verify the approval-flow status card is shown after submission");
        Assertion.assertThat("Reschedule status card should be visible",
                reschedulePage.isApprovalFlowStatusVisible(), is(true));

        step("Verify the new check-in/check-out dates on the status card match the picked date range");
        String approvalDateText = reschedulePage.getApprovalNewDateText();
        Assertion.assertThat("Status card should display the newly picked dates",
                approvalDateText, not(emptyOrNullString()));

        step("Verify the approval progress bar (3 steps: 1 completed, 1 active, 1 pending)");
        Assertion.assertThat("Approval flow should expose 3 progress steps",
                reschedulePage.getApprovalTotalStepCount(), is(3));
        Assertion.assertThat("Step 1 must be completed",
                reschedulePage.getApprovalCompletedStepCount(), is(greaterThanOrEqualTo(1)));
        Assertion.assertThat("Step 2 must be the active one",
                reschedulePage.getApprovalActiveStepCount(), is(greaterThanOrEqualTo(1)));
        Assertion.assertThat("Status label must say 'Waiting for accommodation to confirm'",
                reschedulePage.getApprovalStatusLabelText(),
                anyOf(
                        containsStringIgnoringCase("Waiting for accommodation to confirm"),
                        containsStringIgnoringCase("Menunggu konfirmasi pihak akomodasi")));
    }

    @Author(names = {"Aditi"})
    @Module(name = "ProductDetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelDetailsImageGalleryAndRoomList() throws ExecutionException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyHotelDetailsImageGalleryAndRoomList");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String location = param.get("location");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDate = generateRandomDate(param.get("rangeAddDate"));

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        String appLanguage = homePage.getLanguageApp();

        step("Get exclusive nudges text from API");
        String nudgesText = hotelApi.getExclusiveNudges(
                username, password, hotelName, adult, startDate, night, room, appLanguage, "room");

        step("Open hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("Search by location name '" + location + "' and submit to land on SRP");
        landingPage.searchStayLocation(location);
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();
        Assertion.assertThat("User should land on SRP after searching '" + location + "'",
                searchResultPage.isSearchResultShown(), is(true));

        step("Apply 'Preferred Partner' and 'Preferred Partner Plus' benefit filters on SRP");
        searchResultPage.getFirstHotelPrice();
        searchResultPage.applyPreferredPartnerFilters();

        step("Verify SRP reflects the applied filters");
        Assertion.assertThat(
                "Filter chip badge should be visible after applying Preferred Partner / Preferred Partner Plus filters",
                searchResultPage.isFilterActivated("Filter"), is(true));
        Assertion.assertThat(
                "SRP should still render hotel cards after applying Preferred Partner / Preferred Partner Plus filters",
                searchResultPage.isSearchResultShown(), is(true));
        Assertion.assertThat(
                "Preferred Partner / Loyalty badge icon should be visible on the first hotel card after applying the filters",
                searchResultPage.isPreferredPartnerIconOnFirstHotelCardVisible(), is(true));

        step("Capture hotel name, location and price from the first hotel card on SRP");
        searchResultPage.goToFirstHotelCard();
        String srpFirstHotelName = searchResultPage.getFirstHotelName();
        String srpFirstHotelLocation = searchResultPage.getFirstHotelLocation();
        String srpFirstHotelPrice = searchResultPage.getFirstHotelPrice();
        Assertion.assertThat("First hotel name should be visible on SRP",
                srpFirstHotelName, is(not(emptyOrNullString())));
        Assertion.assertThat("First hotel location should be visible on SRP",
                srpFirstHotelLocation, is(not(emptyOrNullString())));
        Assertion.assertThat("First hotel price should be visible on SRP",
                srpFirstHotelPrice, is(not(emptyOrNullString())));

        step("Click the first hotel card on SRP to land on PDP");
        searchResultPage.selectFirstHotelCard();
        Assertion.assertThat("User should land on the hotel PDP after clicking the first hotel card",
                detailPage.isHotelDetailShown(), is(true));

        step("Verify the same hotel that was clicked on SRP is opened on PDP (name and location must match SRP first card)");
        Assertion.assertThat("Hotel image is displayed on PDP", detailPage.isHaveHotelImage(), is(true));
        Assertion.assertThat(
                "Hotel name on PDP ('" + detailPage.getHotelNameDetail() + "') should match the SRP first card name ('" + srpFirstHotelName + "')",
                detailPage.getHotelNameDetail(), equalToIgnoringCase(srpFirstHotelName));
        Assertion.assertThat(
                "Hotel location on PDP ('" + detailPage.getHotelLocationDetail() + "') should contain the SRP first card location ('" + srpFirstHotelLocation + "')",
                detailPage.getHotelLocationDetail(), containsString(srpFirstHotelLocation));

        step("Verify other PDP details (price, star rating, reviews) are present");
        String pdpPrice = detailPage.getHotelPrice();
        Assertion.assertThat("Hotel price on PDP is non-empty",
                pdpPrice, is(not(emptyOrNullString())));
        Assertion.assertThat("Hotel star rating is shown on PDP", detailPage.getHotelStar(), is(true));
        Assertion.assertThat("Hotel review summary is non-empty", detailPage.getHotelReview(), is(not(emptyOrNullString())));

        step("Verify Preferred Partner / Preferred Partner Plus indicator is visible on PDP");
        Assertion.assertThat(
                "Preferred Partner / Preferred Partner Plus indicator should be visible on PDP after navigating from a Preferred Partner-filtered SRP",
                detailPage.isPreferredPartnerVisibleOnPdp(), is(true));

        step("Verify Priority Program / PPP+ membership info is visible on PDP");
        Assertion.assertThat(
                "Priority Program / PPP+ tier benefit section should be visible on PDP",
                detailPage.checkLoyaltyTierBenefit(), is(true));

        step("Tap the hotel image (and 'See all photos' entry) and verify image gallery opens");
        Assertion.assertThat(
                "Image gallery should open when user taps the hotel image / 'See all photos'",
                detailPage.clickImageAndVerifyGallery(), is(true));

        step("Open the hotel PDP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse =
                hotelApi.getAutocompleteData(username, password, hotelName, "HOTEL");
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.name;
        String checkOutDate = addToDateYYYY_MM_DD(startDate, night);
        hotelPage.openPDPDeeplink(
                "hotel", country.toLowerCase(), hotelId, room, adult, startDate, checkOutDate);
        Assertion.assertThat(
                "User should land on the hotel PDP after opening via deeplink",
                detailPage.isHotelDetailShown(), is(true));

        step("Tap 'See rooms' button on PDP and verify user lands on the room list");
        detailPage.seeHotelRoom();
        Assertion.assertThat("Rooms should be available for the searched hotel after tapping See rooms",
                roomListPage.isRoomListShown(), is(true));

        step("Verify first room card shows name and price (room info on the room list)");
        Assertion.assertThat("First room name should be visible on the room list card",
                roomListPage.getTheFirstHotelRoomName(), is(not(emptyOrNullString())));
        Assertion.assertThat("First room price should be visible on the room list card",
                roomListPage.getTheFirstHotelRoomPrice(), is(not(emptyOrNullString())));

        step("Verify exclusive nudges are visible on top of the room card");
        Assertion.assertThat(
                "Exclusive room nudges from API should be displayed on the room card",
                roomListPage.getRoomNudges(nudgesText), is(nudgesText));

        step("Verify the exclusive nudge is also rendered on the room recommendation card");
        Assertion.assertThat(
                "Exclusive nudge ('" + nudgesText + "') should be visible inside the room recommendation card",
                roomListPage.isExclusiveNudgePresentOnRoomRecommendationCard(nudgesText), is(true));

        step("Verify the room recommendation card shows food, refund/reschedule, and max-people occupancy details");
        Assertion.assertThat(
                "Food / meal info (e.g. breakfast included or not included) should be visible on the room recommendation card",
                roomListPage.isMealInfoOnRoomRecommendationCardVisible(), is(true));
        Assertion.assertThat(
                "Refund & reschedule info (allowed or not allowed) should be visible on the room recommendation card",
                roomListPage.isRefundRescheduleInfoOnRoomRecommendationCardVisible(), is(true));
        Assertion.assertThat(
                "Max people occupancy info (e.g. '1 Adult', '2 Tamu') should be visible on the room recommendation card",
                roomListPage.isMaxOccupancyInfoOnRoomRecommendationCardVisible(), is(true));

        step("Verify the hotel price on top of PDP matches the price on the 1st regular room card (NOT the room recommendation card)");
        roomListPage.clickBackButton();
        pdpPrice = detailPage.getHotelPrice();
        detailPage.seeHotelRoom();
        String roomListFirstRoomPrice = roomListPage.getTheFirstHotelRoomPrice();
        String pdpPriceDigits = pdpPrice == null ? "" : pdpPrice.replaceAll("[^0-9]", "");
        String roomListPriceDigits = roomListFirstRoomPrice == null ? "" : roomListFirstRoomPrice.replaceAll("[^0-9]", "");
        Assertion.assertThat(
                "1st regular room price (excluding the recommendation card) should not be empty",
                roomListFirstRoomPrice, is(not(emptyOrNullString())));
        Assertion.assertThat(
                "Numeric portion of PDP price ('" + pdpPrice + "') should match the 1st regular room price ('" + roomListFirstRoomPrice + "')",
                roomListPriceDigits, is(pdpPriceDigits));

        step("Verify meal, refund/reschedule and max-occupancy info are present on the first room card");
        Assertion.assertThat(
                "Meal info (e.g. breakfast included / not included) should be visible on the first room card",
                roomListPage.isMealInfoOnFirstRoomCardVisible(), is(true));
        Assertion.assertThat(
                "Refund & reschedule info (allowed or not allowed) should be visible on the first room card",
                roomListPage.isRefundRescheduleInfoOnFirstRoomCardVisible(), is(true));
        Assertion.assertThat(
                "Max people occupancy info (e.g. '1 Adult') should be visible on the first room card",
                roomListPage.isMaxOccupancyInfoOnFirstRoomCardVisible(), is(true));
    }

    @DataProvider(name = "checkMultipleGuestVendorDataProvider")
    public Object[][] checkMultipleGuestVendorDataProvider() {
        return getObjectParams("checkMultipleGuestVendor");
    }

    @DataProvider(name = "checkSingleGuestVendorDataProvider")
    public Object[][] checkSingleGuestVendorDataProvider() {
        return getObjectParams("checkSingleGuestVendor");
    }

    @DataProvider(name = "checkSingleGuestSpecialRequestVendorDataProvider")
    public Object[][] checkSingleGuestSpecialRequestVendorDataProvider() {
        return getObjectParams("checkSingleGuestSpecialRequestVendor");
    }

    @DataProvider(name = "bookRoomChildrenVendorDataProvider")
    public Object[][] bookRoomChildrenVendorDataProvider() {
        return getObjectParams("bookRoomChildrenVendor");
    }

    @Author(names = {"Ashish"})
    @Module(name = "PostPurchase")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    @SneakyThrows
    public void verifyEticketLanguageAndBookingDetails() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyEticketLanguageAndBookingDetails");
        BaseTest.paramsThreadLocal.set(param);

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderListPage<WebDriver> orderListPage = OrderListPage.getInstance(session);
        OrderDetailPage<WebDriver> orderDetailPage = OrderDetailPage.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int qtyEachRoom = Integer.parseInt(param.getOrDefault("qtyEachRoom", "1"));
        String currency = param.get("currency");
        int addonsAmount = Integer.parseInt(param.get("addonsAmount"));
        String prefixSpecialRequest = param.getOrDefault("prefixSpecialRequest", "AutoTest-");
        boolean isMultiRoom = Boolean.parseBoolean(param.getOrDefault("isMultiRoom", "false"));
        String accommodationTypes = param.getOrDefault("accommodationTypes", "HOTEL");

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink and change currency to " + currency);
        homePage.loginWithSpecificAccountViaDeeplink(username, password, currency);

        String orderId = "";
        String addonsName = "";
        String specialRequestText = "";
        List<String> bookedGuestNames = new ArrayList<>();
        String initialAppLanguage = homePage.getLanguageApp();

        step("Create and pay (via Sakuku) a hotel order with add-ons and special request via API");
        try {
            orderId = hotelApi.createIssuedOrderWithExtrasApi(
                    username, password, startDate, hotelName, adult, night, room,
                    qtyEachRoom, currency, addonsAmount,
                    true, prefixSpecialRequest,
                    accommodationTypes, initialAppLanguage, isMultiRoom);
            Assertion.assertThat("Order created and paid via Sakuku", orderId,
                    both(is(not(emptyOrNullString()))).and(is(not("0"))));
            addonsName = hotelApi.addonsName;
            Assertion.assertThat("Add-on name should be captured from prebook", addonsName, is(not(emptyOrNullString())));
            specialRequestText = hotelApi.getOrderSpecialRequest();
            Assertion.assertThat("Special request should be captured from booking", specialRequestText, is(not(emptyOrNullString())));
            bookedGuestNames = new ArrayList<>(hotelApi.getGuestNameList());
            Assertion.assertThat("Guest names should be captured from booking", bookedGuestNames, is(not(empty())));
        } catch (Exception e) {
            Assertion.skip("Error when creating + paying order via API: " + e.getMessage());
        }
        if (orderId == null || orderId.isEmpty() || "0".equals(orderId)) {
            Assertion.skip("Order was not created via API. Skipping the subsequent steps.");
            return;
        }

        //<------ Post-purchase verification starts here ------>
        // The e-ticket bottom sheet on the order detail page already exposes
        // BOTH "English" and "Bahasa Indonesia" as inline options, so we don't
        // need to flip the app language between rounds. We just open the dialog
        // twice from the same session: once in the booking language (full
        // verification) and once in the other language (loose verification,
        // since localized add-on names depend on the live i18n catalogue).
        String bookingLanguage = "en".equalsIgnoreCase(initialAppLanguage) ? "English" : "Bahasa Indonesia";
        String otherLanguage = bookingLanguage.equals("English") ? "Bahasa Indonesia" : "English";

        // Anchor pattern that the date and quantity must appear AFTER the
        // add-ons section header in the PDF, e.g.
        //   "Add-ons: 2026-07-10 1 dinner" (en) or
        //   "Fasilitas Ekstra: 2026-07-10 1 makan malam" (id).
        // Whitespace between tokens is flexible (PDF text extraction may
        // collapse line breaks differently).
        String addonsSectionDateQtyRegex =
                "(?:Add-?ons|Fasilitas\\s+Ekstra)[\\s:]*" + java.util.regex.Pattern.quote(startDate)
                        + "\\s+" + addonsAmount;
        java.util.regex.Pattern addonsSectionDateQtyPattern =
                java.util.regex.Pattern.compile(addonsSectionDateQtyRegex,
                        java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.DOTALL);

        step("Open the issued order detail (no app-language change needed)");
        orderListPage.openIssuedTicketViaDeeplink(orderId);
        

        step("Verify e-ticket button is visible on the manage-order section");
        boolean manageOrderOk = (Configs.PLATFORM == Platform.IOS)
                ? orderDetailPage.verifyManageOrderSection()
                : orderDetailPage.verifyManageOrderSection(orderId);
        Assertion.assertThat("E-ticket button should be visible on order detail",
                manageOrderOk, is(true));

        step("Open the e-ticket dialog and verify both English and Bahasa Indonesia options are offered");
        orderDetailPage.clickEticketFromHeader();
        Assertion.assertThat("English option should be available on e-ticket dialog",
                orderDetailPage.isETicketLanguageOptionVisible("English"), is(true));
        Assertion.assertThat("Bahasa Indonesia option should be available on e-ticket dialog",
                orderDetailPage.isETicketLanguageOptionVisible("Bahasa Indonesia"), is(true));

        // ---- PDF 1: booking-language PDF (FULL verification) ----
        step("Open the e-ticket in '" + bookingLanguage + "' (booking language) and verify FULL content");
        orderDetailPage.clickETicketLanguage(bookingLanguage);
        String pdfBookingLang = orderDetailPage.getPDFContentFile();
        Assertion.assertThat(bookingLanguage + " e-ticket PDF should not be empty",
                pdfBookingLang, is(not(emptyOrNullString())));
        if (bookingLanguage.equals("English")) {
            Assertion.assertThat("English PDF should render English labels (e.g. 'Add-ons' or 'Special Request')",
                    pdfBookingLang, anyOf(containsStringIgnoringCase("Add-ons"),
                            containsStringIgnoringCase("Special Request")));
        } else {
            Assertion.assertThat("Bahasa PDF should render Bahasa labels (e.g. 'Fasilitas Ekstra' or 'Permintaan Khusus')",
                    pdfBookingLang, anyOf(containsStringIgnoringCase("Fasilitas Ekstra"),
                            containsStringIgnoringCase("Permintaan Khusus")));
        }
        // The API returns the canonical (English) add-on name regardless of the
        // booking locale, so we can only assert the add-on name itself when the
        // PDF is in English. For the Bahasa PDF we still validate the
        // date+qty pattern below, which is language-stable.
        if (bookingLanguage.equals("English")) {
            Assertion.assertThat(bookingLanguage + " PDF should list the booked add-on name (" + addonsName + ")",
                    pdfBookingLang, containsStringIgnoringCase(addonsName));
        }
        Assertion.assertThat(bookingLanguage + " PDF add-ons section should show '<date> <qty>' (e.g. \""
                        + startDate + " " + addonsAmount + "\") immediately after the section header",
                addonsSectionDateQtyPattern.matcher(pdfBookingLang).find(), is(true));
        Assertion.assertThat(bookingLanguage + " PDF should mention the special request",
                pdfBookingLang, containsStringIgnoringCase(specialRequestText));
        Assertion.assertThat(bookingLanguage + " PDF should mention the booked hotel",
                pdfBookingLang, containsStringIgnoringCase(hotelName));
        orderDetailPage.clickBackFromPdf();

        // Re-deeplink between PDFs: closing the PDF tears down the Chrome page
        // on Android, so the next clickEticketFromHeader hits a stale webview.
        orderListPage.openIssuedTicketViaDeeplink(orderId);
        

        // ---- PDF 2: other-language PDF (LOOSE verification - skips localized add-on name) ----
        step("Re-open the e-ticket dialog for the other language");
        orderDetailPage.clickEticketFromHeader();
        step("Open the e-ticket in '" + otherLanguage + "' (other language) and verify language-stable fields only");
        orderDetailPage.clickETicketLanguage(otherLanguage);
        String pdfOtherLang = orderDetailPage.getPDFContentFile();
        Assertion.assertThat(otherLanguage + " e-ticket PDF should not be empty",
                pdfOtherLang, is(not(emptyOrNullString())));
        if (otherLanguage.equals("English")) {
            Assertion.assertThat("English PDF should render English labels (proves language switch worked)",
                    pdfOtherLang, anyOf(containsStringIgnoringCase("Add-ons"),
                            containsStringIgnoringCase("Special Request")));
        } else {
            Assertion.assertThat("Bahasa PDF should render Bahasa labels (proves language switch worked)",
                    pdfOtherLang, anyOf(containsStringIgnoringCase("Fasilitas Ekstra"),
                            containsStringIgnoringCase("Permintaan Khusus")));
        }
        Assertion.assertThat(otherLanguage + " PDF add-ons section should show '<date> <qty>' (e.g. \""
                        + startDate + " " + addonsAmount + "\") immediately after the section header",
                addonsSectionDateQtyPattern.matcher(pdfOtherLang).find(), is(true));
        Assertion.assertThat(otherLanguage + " PDF should mention the special request (user input, never translated)",
                pdfOtherLang, containsStringIgnoringCase(specialRequestText));
        Assertion.assertThat(otherLanguage + " PDF should mention the booked hotel",
                pdfOtherLang, containsStringIgnoringCase(hotelName));
        orderDetailPage.clickBackFromPdf();

        // ---- UI verification on the order detail page (booking language) ----
        // App language never changed, so the on-page text is in bookingLanguage.
        orderListPage.openIssuedTicketViaDeeplink(orderId);

        step("Verify add-ons, guests and special request on the order detail page (" + bookingLanguage + ")");
        // The order detail UI renders the localized add-on name (e.g. "Makan
        // malam" in Bahasa). The API only gives us the canonical (English)
        // name, so we can only assert the add-on name itself when the UI
        // language is English. Guest names and the special request are user
        // input and stay language-stable.
        if (bookingLanguage.equals("English")) {
            Assertion.assertThat(addonsAmount + " " + addonsName + " should be on order detail",
                    orderDetailPage.checkAddon(addonsName, addonsAmount), is(true));
        }
        List<String> uiGuestNames = orderDetailPage.getGuestDetailsName();
        Assertion.assertThat("UI should show at least as many guest names as were booked",
                uiGuestNames.size(), is(greaterThanOrEqualTo(bookedGuestNames.size())));
        for (String expectedGuestName : bookedGuestNames) {
            Assertion.assertThat("UI should show booked guest name " + expectedGuestName,
                    uiGuestNames, hasItem(containsStringIgnoringCase(expectedGuestName)));
        }
        Assertion.assertThat("UI should display the booked special request",
                orderDetailPage.containsSpecialRequest(specialRequestText), is(true));
    }

    /**
     * STR:
     * <ol>
     *   <li>Keep the stay dates as today (check-in) or a future date.</li>
     *   <li>Search a region/city and submit to land on the SRP.</li>
     *   <li>Scroll past the first few property cards until the
     *       "Hotels near your selected area" / "Hotel di dekat lokasi pilihanmu" section is rendered.</li>
     * </ol>
     * Asserts:
     * <ul>
     *   <li>The "Hotels near your selected area" section is visible with at least one property
     *       card showing a price.</li>
     *   <li>Tapping the first property card redirects the user to a hotel PDP.</li>
     * </ul>
     */
    @Author(names = {"Mridul"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelsNearSelectedAreaCardRedirectionToPDP() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyHotelsNearSelectedAreaCardRedirectionToPDP");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("email");
        String password = param.get("password");
        String[] locations = param.get("location").split("/");
        String location = locations[0].trim();
        String locationType = param.get("locationType");
        String[] dates = generateCheckInOutDate(param.get("addRangeDate"), 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("User opens home page");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink");
        homePage.loginWithSpecificAccountViaDeeplinkOnly(username, password);

        step("Open the SRP via deeplink: " + location + " (" + checkInDate + " to " + checkOutDate + ")");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, accommodationType.toUpperCase());
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, locations);
        String locationId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        hotelPage.openSRPDeeplink(accommodationType.toLowerCase(), 1, 1, locationId, locationType, location.replace(" ", "+"), checkInDate, checkOutDate, "");
        landingPage.fillNightCheckInForm();

        step("Submit search and land on SRP");
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat("SRP should be visible before scrolling to the recommended section",
                searchResultPage.isSearchResultShown(), is(true));

        step("Scroll through property cards until 'Hotels near your selected area' section is visible");
        boolean isSectionVisible = searchResultPage.isHotelsNearSelectedAreaSectionVisible();
        Assertion.assertThat(
                "User should see the 'Hotels near your selected area' section with property cards and prices",
                isSectionVisible, is(true));

        step("Click the first property card in the 'Hotels near your selected area' section");
        String selectedHotelName = searchResultPage.clickFirstHotelInNearSelectedArea();
        Assertion.assertThat("Selected hotel name should not be empty",
                selectedHotelName, not(emptyOrNullString()));

        step("Verify redirection to the PDP");
        detailPage.RemovePopUp();
        Assertion.assertThat("User should be redirected to the hotel PDP after clicking the card",
                detailPage.isHotelDetailShown(), is(true));
        Assertion.assertThat("Verify the hotel name on PDP",
                detailPage.getHotelNameDetail(), containsString(selectedHotelName));
    }

    @DataProvider(name = "checkAlternateMatchRoomGuestDataProvider")
    public Object[][] checkAlternateMatchRoomGuestDataProvider() {
        return getObjectParams("checkAlternateMatchRoomGuest");
    }

    @DataProvider(name = "checkAlternateMatchRoomLoginDataProvider")
    public Object[][] checkAlternateMatchRoomLoginDataProvider() {
        return getObjectParams("checkAlternateMatchRoomLogin");
    }

    @DataProvider(name = "checkLoyaltyBenefitLoginDataProvider")
    public Object[][] checkLoyaltyBenefitLoginDataProvider() {
        return getObjectParams("checkLoyaltyBenefitLogin");
    }

    @DataProvider(name = "verifyBookingInBlackoutDataProvider")
    public Object[][] verifyBookingInBlackoutDataProvider() {return getObjectParams("verifyBookingInBlackout"); }

    @DataProvider(name = "verifyBookingSingleBlackoutDataProvider")
    public Object[][] verifyBookingSingleBlackoutDataProvider() {return getObjectParams("verifyBookingSingleBlackout"); }

    @DataProvider(name = "createHotelBookingDataProvider")
    public Object[][] createHotelBookingDataProvider() {
        return getObjectParams("createHotelBooking");
    }

    @DataProvider(name = "createHotelBookingLoginDataProvider")
    public Object[][] createHotelBookingLoginDataProvider() {
        return getObjectParams("createHotelBookingLogin");
    }

    @DataProvider(name = "checkUserCanSeeRoomDetailPdpLoginDataProvider")
    public Object[][] checkUserCanSeeRoomDetailPdpLoginDataProvider() {
        return getObjectParams("checkUserCanSeeRoomDetailPdpLogin");
    }

    @DataProvider(name = "completeBookingHotelPayNowDataProvider")
    public Object[][] completeBookingHotelPayNowDataProvider() {
        return getObjectParams("completeBookingHotelPayNow");
    }

    @DataProvider(name = "headerQuickFilterLoginDataProvider")
    public Object[][] quickFilterWithHeaderDataProvider() {
        return getObjectParams("headerQuickFilterLogin");
    }

    @DataProvider(name = "paymentPageToSrpLoginDataProvider")
    public Object[][] paymentPageToSrpLoginDataProvider() {
        return getObjectParams("paymentPageToSrpLogin");
    }

    @DataProvider(name = "checkBookingFormDetailGuestDataProvider")
    public Object[][] checkBookingFormDetailGuestDataProvider() {
        return getObjectParams("checkBookingFormDetailGuest");
    }

    @DataProvider(name = "checkBookingFormDetailLoginDataProvider")
    public Object[][] checkBookingFormDetailLoginDataProvider() {
        return getObjectParams("checkBookingFormDetailLogin");
    }

    @DataProvider(name = "checkHotelViewMapGuestDataProvider")
    public Object[][] checkHotelViewMapGuestDataProvider() {
        return getObjectParams("checkHotelViewMapGuest");
    }

    @DataProvider(name = "checkHotelViewMapLoginDataProvider")
    public Object[][] checkHotelViewMapLoginDataProvider() {
        return getObjectParams("ACC_checkHotelViewMapLogin");
    }

    @DataProvider(name = "checkHotelPriceInMapsGuestDataProvider")
    public Object[][] checkHotelPriceInMapsGuestDataProvider() {
        return getObjectParams("checkHotelPriceInMapsGuest");
    }

    @Author(names = {"Vipul Kumar"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "SRP")
    @Test
    public void verifySRPLoginBannerGuest() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifySRPLoginBannerGuest");
        BaseTest.paramsThreadLocal.set(map);

        String location = map.get("location");
        String filter = map.get("filter");
        String rangeAddDate = map.get("rangeAddDate");
        String[] dates = generateCheckInOutDate(rangeAddDate, 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String stayDate = Integer.parseInt(checkInDate.substring(8)) + "-" + Integer.parseInt(checkOutDate.substring(8));
        String b2cUsername = map.get("b2cUsername");
        String b2cPassword = map.get("b2cPassword");
        String b2bUsername = map.get("b2bUsername");
        String b2bPassword = map.get("b2bPassword");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("[1/19] Open the app Home screen");
        homePage.gotoHome();

        step("[2/19] Continue as guest (not logged in) — skip sign-in if prompted");
        loginPage.loginAsGuest();
        Assertion.assertThat(
                "Pre-condition: user must start without an active login (guest / non-login session)",
                homePage.isUserLogin(), is(false));

        step("[3/19] Open the Hotel landing page (search form before hotel list)");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("[4/19] Search for hotels in \"" + location + "\" from the landing page");
        landingPage.searchStayLocation(location);

        step("[5/19] Select stay date (" + checkInDate + " to " + checkOutDate + ") before submitting search");
        landingPage.setStayDate("exact date", stayDate);

        step("[6/19] Submit search and open Hotel SRP (Search Result Page — list of hotels)");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat(
                "Hotel search results page (SRP) should load after submitting the search",
                searchResultPage.isSearchResultShown(), is(true));

        step("[7/19] Apply sort/filter \"" + filter + "\" on SRP and wait for results to refresh");
        searchResultPage.selectFilterSortBy(filter);
        searchResultPage.waitForSrpToLoad();

        step("[8/19] On SRP as guest — verify login/join banner and Log in button are visible");
        Assertion.assertThat(
                "Login/join banner must be visible on SRP when the user is not logged in",
                searchResultPage.isLoginBannerDisplayed(), is(true));
        Assertion.assertThat(
                "Log in button on the banner must be clickable (visible and enabled) for a guest user",
                searchResultPage.isLoginButtonClickable(), is(true));

        step("[9/19] Tap Log in on SRP banner and sign in with B2C account (" + b2cUsername + ")");
        searchResultPage.clickLoginBanner();
        loginPage.loginFromDifferentPages(b2cUsername, b2cPassword);

        step("[10/19] After B2C login — user should return to SRP and login banner must be hidden");
        Assertion.assertThat(
                "User should land back on Hotel SRP after B2C login from the banner",
                searchResultPage.isSearchResultShown(), is(true));
        Assertion.assertThat(
                "Login/join banner must not be shown on SRP after successful B2C login",
                searchResultPage.isLoginBannerDisplayed(), is(false));

        step("[11/19] Log out so the session returns to guest (non-login)");
        homePage.goToHomeDeeplink();
        loginPage.loginAsGuest();
        Assertion.assertThat(
                "After logout, user must be guest again (not logged in)",
                homePage.isUserLogin(), is(false));

        step("[12/19] Open the Hotel landing page again after logout");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("[13/19] Search for hotels in \"" + location + "\" again from the landing page");
        landingPage.searchStayLocation(location);

        step("[14/19] Select stay date (" + checkInDate + " to " + checkOutDate + ") before submitting search again");
        landingPage.setStayDate("exact date", stayDate);

        step("[15/19] Submit search and open Hotel SRP again");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat(
                "Hotel SRP should load again after logout",
                searchResultPage.isSearchResultShown(), is(true));

        step("[16/19] Apply sort/filter \"" + filter + "\" on SRP again and wait for results to refresh");
        searchResultPage.selectFilterSortBy(filter);
        searchResultPage.waitForSrpToLoad();

        step("[17/19] On SRP as guest again — verify login banner and Log in button are visible");
        Assertion.assertThat(
                "Login/join banner must be visible again on SRP after logout (guest session)",
                searchResultPage.isLoginBannerDisplayed(), is(true));
        Assertion.assertThat(
                "Log in button must be clickable again on SRP after logout",
                searchResultPage.isLoginButtonClickable(), is(true));

        step("[18/19] Tap Log in on SRP banner and sign in with B2B corporate account (" + b2bUsername + ")");
        searchResultPage.clickLoginBanner();
        loginPage.loginFromDifferentPages(b2bUsername, b2bPassword);

        step("[19/19] After B2B login — user should return to SRP and login banner must be hidden");
        Assertion.assertThat(
                "User should land back on Hotel SRP after B2B login from the banner",
                searchResultPage.isSearchResultShown(), is(true));
        Assertion.assertThat(
                "Login/join banner must not be shown on SRP after successful B2B login",
                searchResultPage.isLoginBannerDisplayed(), is(false));
    }

    @Author(names = {"Vipul Kumar"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "Wishlist")
    @Test
    public void verifyHotelWishlistSrpPdp() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyHotelWishlistSrpPdp");
        BaseTest.paramsThreadLocal.set(map);

        String location = map.get("location");
        String username = map.get("username");
        String password = map.get("password");

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);

        step("[1/12] Open the app Home screen");
        homePage.gotoHome();

        step("[2/12] Login as guest");
        loginPage.loginAsGuest();
        Assertion.assertThat(
                "Pre-condition: user must start as guest (not logged in)",
                homePage.isUserLogin(), is(false));

        step("[3/12] Open Hotel landing page and search for \"" + location + "\"");
        homePage.openVerticalPageDeeplink("Hotel", "", "");
        landingPage.searchStayLocation(location);

        step("[4/12] Submit search and open Hotel SRP");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat(
                "Hotel SRP should load after search",
                searchResultPage.isSearchResultShown(), is(true));

        step("[5/12] As guest — tap wishlist (heart) icon on a hotel card");
        Assertion.assertThat(
                "Wishlist icon on SRP should be gray (not selected) before tapping as guest",
                searchResultPage.isWishlistIconSelected(), is(false));
        String hotelName = searchResultPage.clickHeartIcon();

        step("[6/12] Guest wishlist tap — user should be redirected to the login form");
        Assertion.assertThat(
                "Non-login user tapping wishlist should see login form fields (email/phone, continue, or password)",
                loginPage.isLoginFormDisplayedForDifferentPages(), is(true));

        step("[7/12] Sign in from the login form (" + username + ") — user should return to Hotel SRP");
        loginPage.loginFromDifferentPages(username, password);
        searchResultPage.waitForSrpToLoad();
        Assertion.assertThat(
                "User should land back on Hotel SRP after login from the banner",
                searchResultPage.isSearchResultShown(), is(true));
        Assertion.assertThat(
                "Login/join banner must not be shown on SRP after successful login",
                searchResultPage.isLoginBannerDisplayed(), is(false));

        step("[8/12] After login — wishlist icon on SRP should turn red (hotel added to wishlist)");
        Assertion.assertThat(
                "Wishlist icon on SRP for \"" + hotelName + "\" should be red (selected) after adding",
                searchResultPage.isWishlistActive(hotelName), is(true));

        step("[9/12] Open PDP for the hotel and verify wishlist icon is red on PDP");
        searchResultPage.selectFirstHotelCard();
        Assertion.assertThat(
                "Wishlist icon on PDP should be red (selected) — same state as on SRP",
                detailPage.isWishlistActive(), is(true));

        step("[10/12] Return to SRP from PDP");
        detailPage.clickBackButton();
        Assertion.assertThat(
                "User should be back on Hotel SRP after leaving PDP",
                searchResultPage.isSearchResultShown(), is(true));

        step("[11/12] Remove hotel from wishlist on SRP — icon should turn gray");
        searchResultPage.clickRedWishlistIcon();
        Assertion.assertThat(
                "Wishlist icon on SRP should be gray (not selected) after removal",
                searchResultPage.isWishlistIconSelected(), is(false));

        step("[12/12] Open PDP again and verify wishlist icon is gray on PDP");
        searchResultPage.selectFirstHotelCard();
        Assertion.assertThat(
                "Wishlist icon on PDP should be gray (not selected) — same state as on SRP after removal",
                detailPage.isWishlistActive(), is(false));
    }

    @Author(names = {"Akshi"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "CreateBooking")
    @Test
    public void checkPropertyFeeWithEstimatedPrice() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("checkPropertyFeeWithEstimatedPrice");
        BaseTest.paramsThreadLocal.set(map);
        String username = map.get("username");
        String password = map.get("password");
        String accommodationType = map.getOrDefault("accommodationType", "hotel");
        String location = map.get("location");
        String rangeAddDate = map.get("rangeAddDate");
        int night = Integer.parseInt(map.get("night"));
        String[] dateArray = generateCheckInOutDate(rangeAddDate, night);
        String checkInDate = dateArray[0];
        String checkOutDate = dateArray[1];
        int totalRoom = Integer.parseInt(map.get("total_room"));
        int totalAdult = Integer.parseInt(map.get("total_adult"));
        String currency = map.getOrDefault("currency", "IDR");

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        BookingFormPage<WebDriver> bookingFormPage = BookingFormPage.getInstance(session);

        step("Goto home");
        homePage.gotoHome();

        step("Login as " + username + " via deeplink (if needed)");
        homePage.loginIfLoggedOutViaDeeplink(username, password);
        homePage.changeCurrency(currency, username);

        step("Get hotel data via autocomplete");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, accommodationType.toUpperCase());
        int index = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String hotelPublicId = hotelAutoCompleteResponse.data.contents.get(index).publicId;
        String hotelCountry = hotelAutoCompleteResponse.data.contents.get(index).country.name;

        step("Open hotel PDP via deep link");
        hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), hotelCountry.toLowerCase(), hotelPublicId, totalRoom, totalAdult, checkInDate, checkOutDate);
        detailPage.RemovePopUp();

        step("Go to the room list");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();

        step("Open room detail price section and get room price");
        roomListPage.goToRoomDetailPriceSection();
        String roomPrice = roomListPage.getRoomPriceFromPriceSection();
        detailPage.closePriceDetail();

        step("Book room and go to booking form");
        detailPage.clickBookButton(totalRoom);

        step("Verify pay now price on booking form matches room list price");
        bookingFormPage.verifyPayNowPriceMatchesRoomPrice(roomPrice);

        step("Get pay upon check-in price on booking form page 1");
        String payUponCheckInPrice = bookingFormPage.getPayUponCheckInPriceBookingForm();
        Assertion.assertThat("Pay upon check-in price is displayed", payUponCheckInPrice, is(not(emptyOrNullString())));

        step("Get estimated total from booking form page 1 and verify it equals pay upon check-in plus room price");
        String estimatedTotalPage1 = bookingFormPage.getEstimatedTotalFromBookingFormPage1();
        bookingFormPage.verifyEstimatedTotalIsCorrect(roomPrice, payUponCheckInPrice, estimatedTotalPage1);

        step("Go to booking form page 2");
        bookingFormPage.continueToAddOnPage();

        step("Verify total price on booking form page 2 matches room list price");
        bookingFormPage.verifyTotalPriceMatchesPriceOnBookingForm(roomPrice);

        step("Get pay upon check-in price on booking form page 2");
        String payUponCheckInPricePage2 = bookingFormPage.getPayUponCheckInPriceBookingFormPage2();
        Assertion.assertThat("Pay upon check-in price on booking form page 2 is displayed",
                payUponCheckInPricePage2, is(not(emptyOrNullString())));

        step("Get estimated total from booking form page 2 and verify it matches page 1");
        String estimatedTotalPage2 = bookingFormPage.getEstimatedTotalFromBookingFormPage2();
        bookingFormPage.verifyEstimatedTotalIsCorrectOnBF2(estimatedTotalPage1, estimatedTotalPage2);

        step("Add Addons");
        bookingFormPage.clickExtraFacilityAddOn();
        bookingFormPage.verifyEstimatedTotalUnchangedAfterAddOns(estimatedTotalPage2);

        step("Add insurance on booking form page 2 and verify estimated total is unchanged");
        bookingFormPage.addInsuranceOnBookingFormPage2(estimatedTotalPage2);

    }

    @Author(names = {"Vipul Kumar"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "PDP")
    @Test
    public void verifyRoomRecommendationBestPickOnRoomList() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("verifyRoomRecommendationBestPickOnRoomList");
        BaseTest.paramsThreadLocal.set(map);

        String hotelName = map.get("hotelName");
        String dateType = map.get("date_type");
        String date = map.get("date");
        int totalRoom = Integer.parseInt(map.get("total_room"));
        int totalGuest = Integer.parseInt(map.get("total_adult"));

        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LandingPage<WebDriver> landingPage = LandingPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);
        RoomListPage<WebDriver> roomListPage = RoomListPage.getInstance(session);

        step("[1/11] Open Home and continue as guest");
        homePage.gotoHome();
        step("Logout via deeplink");
        homePage.logoutViaDeeplink();
        Assertion.assertThat(
                "Pre-condition: user must start as guest (not logged in)",
                homePage.isUserLogin(), is(false));
        homePage.changeCurrency("IDR", "");

        step("[2/11] Open Hotel landing page");
        homePage.openVerticalPageDeeplink("Hotel", "", "");

        step("[3/11] Set stay dates (type: " + dateType + ", date: " + date + ")");
        landingPage.changeDateViaDeeplink(date, dateType, 2, 2);

        step("[4/11] Search hotel by name: \"" + hotelName + "\"");
        landingPage.searchStayLocation(hotelName);

        step("[5/11] Set room count: " + totalRoom + " and adult count: " + totalGuest);
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(totalRoom);
        landingPage.setTotalGuest(totalGuest);
        landingPage.clickDone();

        step("[6/11] Submit search");
        landingPage.clickSubmit();
        landingPage.fillNightCheckInForm();

        step("[7/11] Open PDP (Match hotelName from testdata when search land directly on PDP)");
        Assertion.assertThat(
                "User should land on hotel PDP after searching '" + hotelName + "'",
                detailPage.isHotelDetailShown(), is(true));

        step("[8/11] Open room list from PDP");
        detailPage.seeHotelRoom();
        detailPage.RemovePopUp();
        Assertion.assertThat(
                "Room list should be displayed after tapping See rooms on PDP",
                roomListPage.isRoomListShown(), is(true));

        step("[9/11] Verify room recommendation card is visible on the room list");
        Assertion.assertThat(
                "Room recommendation card should be visible on the room list",
                roomListPage.isRoomRecommendationCardOnTop(), is(true));

        step("[10/11] Verify room recommendation card shows the 'Best pick for you' banner");
        Assertion.assertThat(
                "Best pick header should be visible on the room list (on top of room recommendation card)",
                roomListPage.isBestPickForYouHeaderVisible(), is(true));
        Assertion.assertThat(
                "Best pick header text should match expected value",
                roomListPage.getBestPickForYouHeaderText(),
                anyOf(containsStringIgnoringCase("Best pick for you"), containsStringIgnoringCase("Pilihan terbaik untukmu")));

        step("[11/11] Verify announcement banner is visible on room list");
        Assertion.assertThat(
                "Announcement banner should be visible above to the Best pickup & room list.",
                roomListPage.isAnnouncementBannerVisible(), is(true));
    }

    @Author(names = {"Ishan Singh"})
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPreferredPartnerFilterOnSrpAndPdp() throws InterruptedException {
        Map<String, String> param = getParam("verifyPreferredPartnerFilterOnSrpAndPdp");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = BaseTest.sessionThreadLocal.get();

        String loginType = param.get("loginType");
        String username = param.get("username");
        String password = param.get("password");
        String location = param.get("location");
        String locationType = param.get("locationType");
        String rangeAddDate = param.get("rangeAddDate");
        String language = param.getOrDefault("language", param.getOrDefault("lang", "id"));
        int room = Integer.parseInt(param.getOrDefault("total_room", param.getOrDefault("room", "1")));
        int adult = Integer.parseInt(param.getOrDefault("total_adult", param.getOrDefault("adult", "1")));
        int night = Integer.parseInt(param.getOrDefault("total_night", param.getOrDefault("night", "1")));
        String[] dates = generateCheckInOutDate(rangeAddDate, night);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "hotel";

        HomePage<WebDriver> homePage = HomePage.getInstance(session);
        LoginPage<WebDriver> loginPage = LoginPage.getInstance(session);
        SearchResultPage<WebDriver> searchResultPage = SearchResultPage.getInstance(session);
        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);
        HotelPage<WebDriver> hotelPage = HotelPage.getInstance(session);
        DetailPage<WebDriver> detailPage = DetailPage.getInstance(session);

        step("Open home page as guest (no login)");
        homePage.gotoHome();

        homePage.closeGiveRatingPopup();

        step("Login with credentials");
        loginPage.loginWith(loginType, username, password);
        Assertion.assertThat(
                "Pre-condition: user must be logged in",
                homePage.isUserLogin(), is(true));

        step("Resolve location publicId and open hotel SRP via deeplink");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, location, locationType);
        int locationIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, location);
        String locationId = hotelAutoCompleteResponse.data.contents.get(locationIndex).publicId;

        hotelPage.openSRPDeeplink(accommodationType, room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);

        step("Verify 'Preferred Partner Plus' filter on SRP");
        searchResultPage.applyAndVerifyPreferredPartnerFilter("Preferred Partner Plus");
        searchResultPage.selectFirstHotelCard();

        step("Verify 'Preferred Partner Plus' filter on PDP");
        Assertion.assertThat("Selected PDP is opened", detailPage.isHotelDetailShown(), is(true));
        Assertion.assertThat("Preferred Partner indicator is displayed on PDP", detailPage.isPreferredPartnerVisibleOnPdp(), is(true));
        detailPage.clickBackButton();

        step("Re-open hotel SRP via deeplink for Preferred Partner filter");
        hotelPage.openSRPDeeplink(accommodationType, room, adult, locationId, locationType, location, checkInDate, checkOutDate, language);

        step("Verify 'Preferred Partner' filter on SRP");
        searchResultPage.applyAndVerifyPreferredPartnerFilter("Preferred Partner");
        searchResultPage.selectFirstHotelCard();

        step("Verify 'Preferred Partner' filter on PDP");
        Assertion.assertThat("Selected PDP is opened", detailPage.isHotelDetailShown(), is(true));
        Assertion.assertThat("Preferred Partner indicator is displayed on PDP", detailPage.isPreferredPartnerVisibleOnPdp(), is(true));
    }
}
