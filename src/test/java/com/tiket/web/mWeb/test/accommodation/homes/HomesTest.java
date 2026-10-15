package com.tiket.web.mWeb.test.accommodation.homes;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelDetailPage;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelRoomListPage;
import com.tiket.web.page.accommodation.homes.LandingPage.HomesLandingPageWeb;
import com.tiket.web.page.accommodation.homes.homesSearchResultsPage.HomesSearchResultPageWeb;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.PDP.PDPPageWeb;
import com.tiket.web.page.accommodation.hotels.SRP.SRPPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelRoomListPage.HotelRoomListPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;

import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.is;

import java.util.Map;
import java.util.concurrent.ExecutionException;


public class HomesTest extends WebTest{
    @Author(names = "Shubh")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecentlySearchedNHALogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRecentlySearchedNHALogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);
        PDPPageWeb<?> hotelPDP = PDPPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Homes");
        hotelLP.closeContinueOnWebPopup();

        step("User search hotel by location");
        hotelLP.searchStayLocation(location);

        step("User hit search button");
        hotelLP.clickSubmit();

        step("User go to SRP");
        hotelSRP.clickPopUp();

        step("User click hotel card to go to PDP and go back to SRP");
        hotelPDP.pdpSrpBackAndForth(2);

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Homes");

        step("User close the recently viewed hotels");
        hotelLP.clickClearAll();
        Assertion.assertThat("Booking Form Present",hotelLP.isRecentlySearchedVisible(), Matchers.is(false));

    }
    @Author(names = "Pathakoti Venkat")
    @Module(name = "SearchPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySRPMasterTagNonLoginHomes() {
        Map<String, String> param = getParam("verifySRPMasterTagNonLoginHomes");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");
        hotelLP.closeContinueOnWebPopup();

        step("User search hotel by location");
        hotelLP.searchStayLocation(location);

        step("User hit search button");
        hotelLP.clickSubmit();

        step("User go to SRP");
        hotelSRP.clickPopUp();

        step("User click on Master Tag");
        Assertion.assertThat("Master Tag  on SRP is visible ", hotelSRP.isMasterTagVisible(), is(true));
        hotelSRP.clickFirstMasterTag();
        Assertion.assertThat("Master tag is active", hotelSRP.isFirstMasterTagActive(), is(true));
    }

    @Author(names = "Pathakoti Venkat")
    @Module(name = "SearchPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void MasterTagLoginNHA() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("MasterTagLoginNHA");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");
        hotelLP.closeContinueOnWebPopup();

        step("User search hotel by location");
        hotelLP.searchStayLocation(location);

        step("User hit search button");
        hotelLP.clickSubmit();

        step("User go to SRP");
        hotelSRP.clickPopUp();

        step("User click on Master Tag");
        Assertion.assertThat("Master Tag  on SRP is visible ", hotelSRP.isMasterTagVisible(), is(true));
        hotelSRP.clickFirstMasterTag();
        Assertion.assertThat("Master tag is active", hotelSRP.isFirstMasterTagActive(), is(true));
    }

    @Author(names = "Sunny")
    @Module(name = "SearchPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecentlyViewedNHANonLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRecentlyViewedNHANonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        int pdpCount = Integer.parseInt(param.get("pdpCount"));
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);
        PDPPageWeb<?> hotelPDP = PDPPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Homes");
        hotelLP.closeContinueOnWebPopup();

        step("User search home by location");
        hotelLP.searchStayLocation(location);

        step("User hit search button");
        hotelLP.clickSubmit();

        step("User go to SRP");
        hotelSRP.clickPopUp();

        step("User click homes card to go to PDP and go back to SRP");
        hotelPDP.pdpSrpBackAndForth(pdpCount);

        step("User Open homes Landing page");
        homePage.openVerticalPageViaURL("Homes");

        step("User close the recently viewed homes");
        hotelLP.clickClearAll();
        Assertion.assertThat("No recently viewed homes available",hotelLP.isRecentlySearchedVisible(), Matchers.is(false));
    }

    @Author(names = "Sunny")
    @Module(name = "SearchPage")
    @Tribe(name = "NHA")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecentlyViewedNHALogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRecentlyViewedNHALogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String location = param.get("location");
        int pdpCount = Integer.parseInt(param.get("pdpCount"));

        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);
        PDPPageWeb<?> hotelPDP = PDPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open Homes Landing page");
        homePage.openVerticalPageViaURL("Homes");
        hotelLP.closeContinueOnWebPopup();

        step("User search home by location");
        hotelLP.searchStayLocation(location);

        step("User hit search button");
        hotelLP.clickSubmit();

        step("User go to SRP");
        hotelSRP.clickPopUp();

        step("User click home card to go to PDP and go back to SRP");
        hotelPDP.pdpSrpBackAndForth(pdpCount);

        step("User Open homes Landing page");
        homePage.openVerticalPageViaURL("Homes");

        step("User close the recently viewed homes");
        hotelLP.clickClearAll();
        Assertion.assertThat("No recently viewed homes available",hotelLP.isRecentlySearchedVisible(), Matchers.is(false));
    }

    @Author(names = "Akshi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookNonIDRNHA() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookNonIDRNHA");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod=param.get("paymentMethod");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HomesLandingPageWeb<?> landingPage = HomesLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select SGD currency");
        homePage.selectCurrency("SGD");

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to SGD", searchPageCurrency.equals("SGD"), Is.is(true));
        homePage.openVerticalPageViaURL("Homes");

        step("Search stay homes");
        landingPage.searchStayLocation(location);
        landingPage.clickSubmit();

        step("submit search Button");
        searchResultPage.clickPopUp();
        Assertion.assertThat("Srp is Loaded",searchResultPage.isHotelCardVisible(), Is.is(true));

        step("Sort by lowest");
        searchResultPage.clickSortButton();
        searchResultPage.clickSortByLowest();
        Assertion.assertThat("Sorted by lowest", searchResultPage.checkSorting(), Is.is(true));

        step("Check price filter");
        searchResultPage.clickPriceFilter();
        Assertion.assertThat("Price Filter Currency", searchResultPage.checkPriceFilter("SGD"), Is.is(true));
        searchResultPage.clickPriceFilterSave();

        step("User clicks Star filter chips");
        searchResultPage.clickStarFilter();
        searchResultPage.selectStar();
        searchResultPage.clickStarSave();

        if(!detailPage.isPdpLoaded()){
            step("Select hotel Card ");
            searchResultPage.selectHotel();
        }
        step("Go Inside PDP");
        detailPage.switchToNewTab();
        detailPage.clickPopUp();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), Is.is(true));
        roomListPage.scrollToRoomListSection();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        step("check order id on payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), Is.is(true));
        Assertion.assertThat("contact name should be available on order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), Is.is(orderID));
    }

    @Author(names = "Shubh")
    @Module(name = "RoomListPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySoldOutNoNextAvailDatesNHANonLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySoldOutNoNextAvailDatesNHANonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");
        int guest = Integer.parseInt(param.get("guest"));
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        PDPPageWeb<?> hotelPDP = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User Open Homes Landing page");
        homePage.openVerticalPageViaURL("Homes");
        hotelLP.closeContinueOnWebPopup();

        step("User search home by location");
        hotelLP.searchStayLocation(location);
        hotelLP.clickRoomAndGuestButton();
        hotelLP.setTotalGuest(guest);
        hotelLP.clickDone();

        step("User hit search button");
        hotelLP.clickSubmit();

        step("PDP Loaded");
        Assertion.assertThat("PDP is loaded", hotelPDP.isPdpLoaded(), Is.is(true));

        step("Check Fully booked");
        Assertion.assertThat("Is All rooms Sold Out", hotelPDP.isAllRoomsFullyBooked(), Is.is(true));

        step("Adjust date button is pressed");
        hotelPDP.clickChangeDate();
        hotelPDP.clickCalenderPopUp();
        hotelPDP.clickDate();

        step("user change the dates");
        hotelPDP.clickOpenDate();
        hotelPDP.setStayDatePDP(dateType, dateRange);
        hotelPDP.clickNightSave();
        hotelPDP.clickSearchButton();
        Assertion.assertThat("PDP is loaded again", hotelPDP.isPdpLoaded(), Is.is(true));
    }

    @Author(names = "Bikash")
    @Module(name = "SearchPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserApplyAccommodationFilterInHomesSRP() {
        Map<String, String> param = getParam("verifyUserApplyAccommodationFilterInHomesSRP");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HomesLandingPageWeb<?> homesLP = HomesLandingPageWeb.getInstance(session);
        HomesSearchResultPageWeb<?> homesSRP = HomesSearchResultPageWeb.getInstance(session);

        String location = param.get ("location");

        step("User open Home Page");
        homePage.goToHomePage();

        step("User open Homes Landing Page");
        homePage.openVerticalPageViaURL("Homes");

        step("User search Homes by location");
        homesLP.searchStayLocation(location);

        step("User click submit button");
        homesLP.clickSubmit();

        step("User apply accommodation filters");
        homesSRP.clickAccommodationFilter();
        homesSRP.selectAccommodationFilter();

        step("Submit filter");
        homesSRP.clickAccommodationFilterSave();

        step("Verify accommodation filter on first 5 hotel cards");
        homesSRP.verifyAccommodationFilterForFiveHotelCards();
    }
    @Author(names = "Shubh")
    @Module(name = "RoomListPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySoldOutNoNextAvailDatesNHALogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySoldOutNoNextAvailDatesNHALogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");
        int guest = Integer.parseInt(param.get("guest"));
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        PDPPageWeb<?> hotelPDP = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open Homes Landing page");
        homePage.openVerticalPageViaURL("Homes");
        hotelLP.closeContinueOnWebPopup();

        step("User search home by location");
        hotelLP.searchStayLocation(location);
        hotelLP.clickRoomAndGuestButton();
        hotelLP.setTotalGuest(guest);
        hotelLP.clickDone();

        step("User hit search button");
        hotelLP.clickSubmit();

        step("PDP Loaded");
        Assertion.assertThat("PDP is loaded", hotelPDP.isPdpLoaded(), Is.is(true));

        step("Check Fully booked");
        Assertion.assertThat("Is All rooms Sold Out", hotelPDP.isAllRoomsFullyBooked(), Is.is(true));

        step("Adjust date button is pressed");
        hotelPDP.clickChangeDate();
        hotelPDP.clickCalenderPopUp();
        hotelPDP.clickDate();

        step("user change the dates");
        hotelPDP.clickOpenDate();
        hotelPDP.setStayDatePDP(dateType, dateRange);
        hotelPDP.clickNightSave();
        hotelPDP.clickSearchButton();
        Assertion.assertThat("PDP is loaded again", hotelPDP.isPdpLoaded(), Is.is(true));
    }

    @Author(names = "venkat pathakoti")
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySRPBanner() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySRPBanner");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search Button");
        landingPage.clickSubmit();
        Assertion.assertThat("Hotel Card on SRP is visible ", searchResultPage.isHotelCardVisible(), is(true));

        step("Scroll to find banner");
        searchResultPage.scrollToFindSrpBanner();
        Assertion.assertThat("Banner is visible", searchResultPage.isBannerVisible(), is(true));

        step("Click on Preferred Partner Banner");
        searchResultPage.clickPreferredPartnerBanner();

        Assertion.assertThat("Assert that sheet pops up", searchResultPage.isBottomSheetTitleVisible(), is(true));
        Assertion.assertThat("Assert that benefits is visible", searchResultPage.isBottomBenefitsVisible(), is(true));
        Assertion.assertThat("Close and assert that bottom sheet no longer displayed", searchResultPage.isCloseButtonWorks(), is(true));
    }

    @Author(names = "Akshi")
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPromoOnPDPHomes() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyPromoOnPDPHomes");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Homes");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        Assertion.assertThat("Verify User is redirected to PDP",detailPage.isPdpLoaded(),is(true));
        Assertion.assertThat("Check if promo List present",detailPage.isPromoListLoaded(),is(true));

        step("Click on button to copy the promo code");
        detailPage.clickPromoCopyButton();
        Assertion.assertThat("Promo is copied",detailPage.isPromoCopied(),is(true));

        detailPage.scrollToRightOfPromo();

        step("Open Promo section");
        detailPage.clickSeeAllPromo();
        Assertion.assertThat("Check if promo section is present",detailPage.isPromoSectionLoaded(),is(true));

        step("Click on copy button to copy the promo code");
        detailPage.clickPromoCopyButtonOnSection();
        Assertion.assertThat("Promo is copied",detailPage.isPromoCopiedOnPromoSection(),is(true));
    }

    @Author(names = "Akshi")
    @Module(name = "Landing Page")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySeeMoreNHALP() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySeeMoreNHALP");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HomesLandingPageWeb<?> landingPage = HomesLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page loaded",landingPage.isLandingPageLoaded(),is(true));
        Assertion.assertThat("See More Button is present",landingPage.checkSeeMoreButton(),is(true));

        step("Click on See More Button");
        landingPage.clickSeeMoreButton();

        Assertion.assertThat("Check 1st module present",landingPage.checkFirstModule(),is(true));

        step("Scroll to Bottom");
        landingPage.scrollToBottom();

        Assertion.assertThat("Check SEE MORE button not displayed",landingPage.checkSeeMoreButton(),is(false));

        Assertion.assertThat("Back to Top button present",landingPage.checkBackToTopButton(),is(true));

        step("Click on Back to Top Button");
        landingPage.clickBackToTopButton();

        Assertion.assertThat("Check landing page moved to top",landingPage.checkIfScrolledUp(),is(true));
    }
}
