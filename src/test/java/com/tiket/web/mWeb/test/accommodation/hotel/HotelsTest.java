package com.tiket.web.mWeb.test.accommodation.hotel;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelDetailPage;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPage;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.LandingPage.LandingPageWeb;
import com.tiket.web.page.accommodation.hotels.PDP.PDPPageWeb;
import com.tiket.web.page.accommodation.hotels.SRP.HotelSearchResultPage;
import com.tiket.web.page.accommodation.hotels.SRP.SRPPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelRoomListPage.HotelRoomListPage;
import com.tiket.web.page.accommodation.hotels.hotelRoomListPage.HotelRoomListPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.accommodation.homes.homesRoomListPage.HomesRoomListPageWeb;

import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.*;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class HotelsTest extends WebTest {

    @Author(names = "Deeon")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySRPMasterTag() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySRPMasterTag");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String location = param.get("location");

        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
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
        Assertion.assertThat("Master tag is active", hotelSRP.isFirstMasterTagActive(), Is.is(true));
    }

    @Author(names = "Deeon")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySRPPopularDestinationLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySRPPopularDestinationLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        hotelLP.closeContinueOnWebPopup();

        step("User search hotel by popular destination");
        hotelLP.clickSearchForm();
        String intendedPopularDestination = hotelLP.getFirstPopularDestinationText();
        hotelLP.clickFirstPopularDestination();

        step("Set date");
        hotelLP.setStayDate(dateType, dateRange);

        step("User hit search button");
        hotelLP.clickSubmit();

        step("User go to SRP");
        hotelSRP.clickPopUp();

        Assertion.assertThat("SRP is Loaded with hotels found", hotelSRP.isHotelCardVisible(), Is.is(true));
        Assertion.assertThat("SRP display hotels based on selected popular destination", hotelSRP.isHotelLocationExists(intendedPopularDestination), Is.is(true));

        hotelSRP.openAdvancedFilterMenu();
        String intendedPopularPlace = hotelSRP.getFirstPopularPlaceText();
        hotelSRP.applyPopularPlaceFilter();

        Assertion.assertThat("SRP is Loaded with hotels found", hotelSRP.isHotelCardVisible(), Is.is(true));
        Assertion.assertThat("SRP display hotels based on selected popular destination", hotelSRP.isHotelLocationExists(intendedPopularPlace), Is.is(true));
    }

    @Author(names = "Deeon")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecentlyViewedHotel() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRecentlyViewedHotel");
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

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        hotelLP.closeContinueOnWebPopup();

        step("User search hotel by location");
        hotelLP.searchStayLocation(location);

        step("User hit search button");
        hotelLP.clickSubmit();

        step("User go to SRP");
        hotelSRP.clickPopUp();

        step("User click hotel card to go to PDP and go back to SRP");
        hotelPDP.pdpSrpBackAndForth(pdpCount);

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("User close the recently viewed hotels");
        hotelLP.closeRecentlyViewedItem(pdpCount);
    }

    @Author(names = "Akshi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkSalutionNotPresent() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkSalutionNotPresent");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotelName= param.get("hotelName");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage= HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage =new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage=HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPage<?> bookingFormPage=new HotelBookingFormPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.switchToNewTab();
        Assertion.assertThat("Verify User is redirected to PDP",detailPage.isPdpLoaded(),is(true));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        Assertion.assertThat("Room list is visible",roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible",roomListPage.isRoomImageVisible(),is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go Inside Booking Form and check if salutation present");
        Assertion.assertThat("Salutation not present",bookingFormPage.checkSalutation(),is(true));

        step("Check for salutation message");
        bookingFormPage.continueToNextPage();

        Assertion.assertThat("Check if salutation message displayed",bookingFormPage.checkMessage(),is(true));
    }

    @Author(names = "Akshi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRoomRecommendationAvailable() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRoomRecommendationAvailable");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotelName= param.get("hotelName");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage= HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage =new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.switchToNewTab();
        Assertion.assertThat("Verify User is redirected to PDP",detailPage.isPdpLoaded(),is(true));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        Assertion.assertThat("Recommended room available",roomListPage.isRecommendedRoomVisible(), is(true));
    }

    @Author(names = "Akshi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserCanSeeRoomList() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyUserCanSeeRoomList");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotelName= param.get("hotelName");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage= HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage =new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.switchToNewTab();
        Assertion.assertThat("Verify User is redirected to PDP",detailPage.isPdpLoaded(),is(true));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        Assertion.assertThat("RoomList Available",roomListPage.roomAvailable(), is(true));
    }

    @Author(names = "Akshi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySearchedWith1Adult1Night() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySearchedWith1Adult1Night");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotelName= param.get("hotelName");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");
        int noOfAdults = Integer.parseInt(param.get("Adult"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage= HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage =new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.switchToNewTab();
        Assertion.assertThat("Verify User is redirected to PDP",detailPage.isPdpLoaded(),is(true));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        Assertion.assertThat("Number of guests is 1",roomListPage.getNumberOfGuest(), is("1 Guest"));
        Assertion.assertThat("Number of Nights if 1",roomListPage.getNightNumber(),is(true));
    }

    @Author(names = "Akshi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserSeeSORoomListRoomDetail() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyUserSeeSORoomListRoomDetail");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotelName= param.get("hotelName");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage= HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage =PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage=HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPage<?> bookingFormPage=new HotelBookingFormPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.switchToNewTab();
        Assertion.assertThat("Verify User is redirected to PDP",detailPage.isPdpLoaded(),is(true));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        Assertion.assertThat("Room list is visible",roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible",roomListPage.isRoomImageVisible(),is(true));

        String room_name=roomListPage.getRoomName();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go Inside Booking Form");
        Assertion.assertThat("Hotel name matched",bookingFormPage.getBookingHotelName(),is(hotelName));
        Assertion.assertThat("Room name is same",bookingFormPage.getBookingRoomName(),is(room_name));

        step("Go back to RoomList");
        bookingFormPage.moveToRoomList();

        step("Click on room detail");
        roomListPage.openRoomDetail();

        String room_name_detail=roomListPage.getRoomNameFromDetail();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go inside Booking Form");
        Assertion.assertThat("Hotel name matched",bookingFormPage.getBookingHotelName(),is(hotelName));
        Assertion.assertThat("Room name is same",bookingFormPage.getBookingRoomName(),is(room_name_detail));
    }

    @Author(names = "Dixita")
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void recommendedhotelsshowing() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("recommendedhotelsshowing");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotel = param.get("hotel");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        com.tiket.web.dWeb.page.accommodation.hotels.HotelRoomListPage<?> roomListPage=new com.tiket.web.dWeb.page.accommodation.hotels.HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(hotel);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        String pdpHotelName=detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel name on the search and detail page should be equal",pdpHotelName , containsString(hotel));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        Assertion.assertThat("Room list is visible",roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible",roomListPage.isRoomImageVisible(),is(true));

        step("See if recommended hotels are shown");
        Assertion.assertThat("Recommended hotels are shown",roomListPage.isSimilarPropertiesVisible(),is(true));
    }

    @Author(names = "Dixita")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void addsplreqonbookingform() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("addsplreqonbookingform");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String fullName = param.get("fullName");
        String splReq=param.get("splReq");
        String customReq=param.get("customReq");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage=HotelRoomListPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        HotelBookingFormPage<?> hotelBookingFormPage = new HotelBookingFormPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));

        step("Checking room List");
        Assertion.assertThat("Room lists section should be visible",roomListPage.isRoomListSectionVisible(),is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();
        hotelBookingFormPage.skipRoomChangePopup();

        step("User enter Stay details and add special Request ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page",bookingFormPage.isGuestStayForm(),is(true));
        bookingFormPage.fillGuestDetails(1,fullName,true,true,splReq);

        bookingFormPage.enterSpecialRequest(customReq);
        Assertion.assertThat("Verify if Special Request is added successfully",bookingFormPage.getSpecialReqText(),containsString(customReq));
    }

    @Author(names ={"Akshi"})
    @Module(name = "HotelLandingPage")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void checkRecentlySearchNearMEActiveLangID() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkRecentlySearchNearMEActiveLangID");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelSearchResultPage<?> hotelSearchResultPage=new HotelSearchResultPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Select Bahasa Language");
        homePage.selectBahasaLanguage();
        homePage.goback();

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.clickNearMeLocation();

        step("submit search Button");
        landingPage.clickSubmit();

        step("Check Recently Searched");
        hotelSearchResultPage.moveToLastPage();
        Assertion.assertThat("Recently Search Has near me in Bahasa", homePage.checkRecentlySearchIsNearMe(), is("Dekat saya"));

        step("Change Language");
        homePage.selectEnglishLanguage();
        homePage.goback();

        step("Check Recently Searched");
        Assertion.assertThat("Recently Search Has near me in Eng", homePage.checkRecentlySearchIsNearMe(), is("Near me"));
    }

    @Author(names ={"Akshi"})
    @Module(name = "HotelLandingPage")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void checkRecentlySearchNearMEActiveLangEN() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkRecentlySearchNearMEActiveLangEN");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelSearchResultPage<?> hotelSearchResultPage=new HotelSearchResultPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Select English Language");
        homePage.selectEnglishLanguage();
        homePage.goback();

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.clickNearMeLocation();

        step("submit search Button");
        landingPage.clickSubmit();

        step("Check Recently Searched");
        hotelSearchResultPage.moveToLastPage();
        Assertion.assertThat("Recently Search Has near me in English", homePage.checkRecentlySearchIsNearMe(), is("Near me"));

        step("Change Language");
        homePage.selectBahasaLanguage();
        homePage.goback();

        step("Check Recently Searched");
        Assertion.assertThat("Recently Search Has near me in Bahasa", homePage.checkRecentlySearchIsNearMe(), is("Dekat saya"));
    }

    @Author(names = "Akshi")
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void sortByLowestPrice() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("sortByLowestPrice");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPAge=SRPPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Select sort by lowest Price");
        searchResultPAge.clickSortButton();
        searchResultPAge.clickSortByLowest();

        step("Check if sorted");
        Assertion.assertThat("Sorting is correct",searchResultPAge.checkSorting(),is(true));
    }
  
    @Author(names ="Akshi")
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void changeLocationOnSRP() throws FailedLoginException, InterruptedException{
        Map<String, String> param = getParam("changeLocationOnSRP");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String region = param.get("region");
        String area = param.get("area");
        String POI = param.get("POI");
        String city= param.get("city");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPAge=SRPPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.clickNearMeLocation();
        landingPage.searchStayLocation(region);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Change region to area");
        searchResultPAge.changeLocation(area);
        Assertion.assertThat("Check region is changed to area",searchResultPAge.checkLocation(area),is(true));

        step("Change to city");
        searchResultPAge.changeLocation(city);
        Assertion.assertThat("Check changed to city",searchResultPAge.checkLocation(city),is(true));

        step("Change to region");
        searchResultPAge.changeLocation(region);
        Assertion.assertThat("Check to region",searchResultPAge.checkLocation(region),is(true));

        step("Change to POI");
        searchResultPAge.changeLocation(POI);
        Assertion.assertThat("Change to POI",searchResultPAge.checkLocation(POI),is(true));

        step("Change to near me");
        searchResultPAge.changeLocationToNearMe();
        Assertion.assertThat("Check near me",searchResultPAge.checkLocation("Near you") || searchResultPAge.checkLocation("Di dekat kamu"),is(true));
    }
  
    @Author(names = "Shubh Sharma")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void roomDetailImageGallery(){
        Map<String, String> param = getParam("roomDetailImageGallery");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);

        homePage.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType,dateRange);
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        step("Go Inside PDP");
        Assertion.assertThat("PDP is loaded", detailPage.isPdpLoaded(), is(true));
        detailPage.seeHotelRoom();

        step("Ensure room list section is visible");
        Assertion.assertThat("Room List is visible", roomListPage.isRoomListSectionVisible(), is(true));

        step("Click on thumbnail image");
        roomListPage.clickRoomImageInPDP();
        int imageCount = roomListPage.getOtherCount();
        roomListPage.clickRoomMainImage();

        step("Check sliding and Image count");
        int sliderCount = roomListPage.getSliderTotalCount();
        Assertion.assertThat("Image count same", imageCount, is(sliderCount));

        int currCount = roomListPage.getSliderCurrCount();
        String currImage = roomListPage.savePath(currCount);
        step("Swipe Next");
        roomListPage.slideImageRight(imageCount);

        step("Swipe Prev");
        roomListPage.slideImageLeft(imageCount);
        int lastCount = roomListPage.getSliderCurrCount();
        String lastImage = roomListPage.savePath(lastCount);

        step("Check First and Last Image");
        Assertion.assertThat("First and Last Image Same : ", currImage.equals(lastImage), is(true));
    }
    @Author(names = {"Dixita"})
    @Module(name = "SearchResultPage")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void filterByStarSRPLogin() throws FailedLoginException, InterruptedException  {
        Map<String, String> param = getParam("filterByStarSRPLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        int noOfHotels = Integer.parseInt(param.get("noOfHotels"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage=SRPPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();

        step("Click filter button");
        searchResultPage.clickStarFilter();
        searchResultPage.selectStar();

        step("Submit filter");
        searchResultPage.clickStarSave();

        double[] hotelStars = searchResultPage.checkStarFilter(noOfHotels);

        for (double stars : hotelStars) {
            step("Stars are: "+stars);
            Assertion.assertThat("Hotel star is within the filter range (4–5)", stars >= 4.0 && stars <= 5.0, is(true));
        }
    }
    @Author(names = {"Dixita"})
    @Module(name = "SearchResultPage")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void filterByPriceSRPLogin() throws FailedLoginException, InterruptedException  {
        Map<String, String> param = getParam("filterByPriceSRPLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<WebDriver> session = sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage=SRPPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();

        step("Click filter button");
        searchResultPage.clickPriceFilter();
        searchResultPage.selectPriceFilter();

        step("Submit filter");
        searchResultPage.clickPriceFilterSave();

        List<String> results = searchResultPage.checkPriceFilter();

        for (String result : results) {
            boolean valid = result.endsWith("true");
            Assertion.assertThat("Hotel price validation → " + result, valid, is(true));
        }
    }

    @Author(names = "Akshi")
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRoomDetailBook() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRoomDetailBook");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotelName = param.get("hotelName");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage=HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay location");
        landingPage.searchStayLocation(hotelName);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        detailPage.clickSeeRoomButton();

        step("Go to room Detail");
        roomListPage.openRoomDetail();
        String roomPrice=roomListPage.getFinalPrice();
        String roomName=roomListPage.getRoomNameFromDetail();

        step("Click book button");
        roomListPage.clickBookButton();

        step("Check Prebook Page loaded");
        Assertion.assertThat("Verify booking form loaded",bookingFormPage.isContactForm(),is(true));
        String bookPrice=bookingFormPage.getBookingPrice();
        String bookingHotelName=bookingFormPage.getBookingHotelName();
        Assertion.assertThat("Verify prices are same", HotelBookingFormPageWeb.checkPriceSameFromRoomDetail(roomPrice,bookPrice),is(true));
        Assertion.assertThat("Check hotelName is same",bookingHotelName.equals(hotelName),is(true));
        Assertion.assertThat("Check roomName",bookingFormPage.getBookingRoomName().equals(roomName),is(true));
    }
    @Author(names = "Akshi")
    @Module(name = "RoomListPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkPriceOnBookingForm() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkPriceOnBookingForm");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);

        step("Open home page and login");
        homePage.goToHomePage();

        step("Navigate to Hotel vertical and search for stay");
        homePage.selectCurrency("IDR");
        homePage.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType,dateRange);
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        step("Select hotel Card ");
        searchResultPage.selectHotelCard();

        step("Go Inside PDP");
        detailPage.switchToNewTab();
        detailPage.isPdpLoaded();
        detailPage.seeHotelRoom();

        step("Ensure room list section is visible");
        Assertion.assertThat("Room List is visible", roomListPage.isRoomListSectionVisible(), Matchers.is(true));

        step("Click select on Room List");
        roomListPage.clickBookRoomButton();

        step("Click book button");
        Assertion.assertThat("Redirected to Booking Form", bookingFormPage.isContactForm(), Matchers.is(true));
        Assertion.assertThat("Hotel Price on the Booking page is visible", !bookingFormPage.getHotelPriceDetail().isEmpty(), Matchers.is(true));

        step("Click Chevron Button");
        roomListPage.clickChevronButton();
        Assertion.assertThat("Chevron Page is loaded", bookingFormPage.isChevronButtonLoaded(), Matchers.is(true));

        step("Close price detail section");
        bookingFormPage.closePriceModal();

        step("Check booking form present");
        Assertion.assertThat("Booking Form Present",bookingFormPage.isContactForm(), Matchers.is(true));
    }
    @Author(names = "Shubh")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecentlySearchedHotelLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRecentlySearchedHotelLogin");
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
        homePage.openVerticalPageViaURL("Hotel");
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
        homePage.openVerticalPageViaURL("Hotel");

        step("User close the recently viewed hotels");
        hotelLP.clickClearAll();
        Assertion.assertThat("No recently viewed hotels available",hotelLP.isRecentlySearchedVisible(), Matchers.is(false));

    }

    @Author(names = "Akshi")
    @Module(name = "PDP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyMapsOnPDP() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyMapsOnPDP");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelSearchResultPage<?> hotelSearchResultPage=new HotelSearchResultPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("User select Hotel On SRP");
        hotelSearchResultPage.selectHotelCard();

        step("Go to Maps");
        detailPage.goToMaps();
        Assertion.assertThat("Check Maps",detailPage.checkMaps(),is(true));
    }

    @Author(names = "Sunny")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecentlyViewedHotelNonLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRecentlyViewedHotelNonLogin");
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
        homePage.openVerticalPageViaURL("Hotel");
        hotelLP.closeContinueOnWebPopup();

        step("User search hotel by location");
        hotelLP.searchStayLocation(location);

        step("User hit search button");
        hotelLP.clickSubmit();

        step("User go to SRP");
        hotelSRP.clickPopUp();

        step("User click hotel card to go to PDP and go back to SRP");
        hotelPDP.pdpSrpBackAndForth(pdpCount);

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("User close the recently viewed hotels");
        hotelLP.clickClearAll();
        Assertion.assertThat("No recently viewed hotels available",hotelLP.isRecentlySearchedVisible(), Matchers.is(false));
    }

    @Author(names ="Akshi")
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void changeLocationOnSRPLogin() throws FailedLoginException, InterruptedException{
        Map<String, String> param = getParam("changeLocationOnSRPLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String region = param.get("region");
        String area = param.get("area");
        String POI = param.get("POI");
        String city= param.get("city");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPAge=SRPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.clickNearMeLocation();
        landingPage.searchStayLocation(region);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Change region to area");
        searchResultPAge.changeLocation(area);
        Assertion.assertThat("Check region is changed to area",searchResultPAge.checkLocation(area),is(true));

        step("Change to city");
        searchResultPAge.changeLocation(city);
        Assertion.assertThat("Check changed to city",searchResultPAge.checkLocation(city),is(true));

        step("Change to region");
        searchResultPAge.changeLocation(region);
        Assertion.assertThat("Check to region",searchResultPAge.checkLocation(region),is(true));

        step("Change to POI");
        searchResultPAge.changeLocation(POI);
        Assertion.assertThat("Change to POI",searchResultPAge.checkLocation(POI),is(true));

        step("Change to near me");
        searchResultPAge.changeLocationToNearMe();
        Assertion.assertThat("Check near me",searchResultPAge.checkLocation("Near you") || searchResultPAge.checkLocation("Di dekat kamu"),is(true));
    }

    @Author(names = "Bikash")
    @Module(name = "SRP")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyMainCard() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyMainCard");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        String location = param.get("location");
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        SRPPageWeb<?> srpPageWeb = SRPPageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage =new HotelDetailPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Verifying the details on SRP");
        String hotelNameSRP = srpPageWeb.getSRPHotelName();
        Assertion.assertThat("Hotel name is found in SRP or not for the cart", srpPageWeb.isHotelNameVisible(), Is.is(true));
        Assertion.assertThat("Hotel price is found in SRP or not for the cart", srpPageWeb.isHotelPriceVisible(), Is.is(true));
        Assertion.assertThat("Hotel benifits are found in SRP or not for the cart", srpPageWeb.isHotelBenefitsVisible(), Is.is(true));
        Assertion.assertThat("Hotel tixPoint are present in SRP or not for the cart", srpPageWeb.isSRPHotelTixPoint(), Is.is(true));
        Assertion.assertThat("Hotel stars are present in SRP or not for the cart", srpPageWeb.isHotelStarVisible(), Is.is(true));

        step("Select hotel Card ");
        srpPageWeb.selectHotelCard();

        step("Go and click the PDP");
        detailPage.switchToNewTab();
        Assertion.assertThat("Verify User is redirected to PDP",detailPage.isPdpLoaded(),is(true));
        String hotelNamePDP = detailPage.getHotelNameDetail();
        Assertion.assertThat("Hotel name on SRP and PDP should match", hotelNameSRP, Is.is(hotelNamePDP));
   }

    @Author(names = "Dixita")
    @Module(name = "RoomListPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkPriceDetailOnPaymentPageLogin() throws FailedLoginException, InterruptedException{
        Map<String, String> param = getParam("checkPriceDetailOnPaymentPageLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        int noOfRooms = Integer.parseInt(param.get("rooms"));
        int guests = Integer.parseInt(param.get("guests"));
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String salutation = param.get("salutation");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("Navigate to Hotel vertical and search for stay");
        homePage.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType,dateRange);
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("PDP is loaded", detailPage.isPdpLoaded(), Matchers.is(true));
        detailPage.seeHotelRoom();

        step("Set guest and room count");
        roomListPage.clickRoomListChangeButton();
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(guests);
        roomListPage.setTotalRoom(noOfRooms);
        roomListPage.clickGuestDoneAndSubmitButton();

        step("Ensure room list section is visible");
        roomListPage.scrollToRoomListBookSection();
        Assertion.assertThat("Room List is visible", roomListPage.isRoomListSectionVisible(), Matchers.is(true));

        step("Click select on Room List");
        roomListPage.clickBookRoomButton();

        step("Click book button");
        bookingFormPage.fillSalutations(salutation);
        bookingFormPage.fillGuestDetailsCombinedRandom(guests);

        step("Click Continue Payment Button");
        bookingFormPage.continueToPaymentPage();
        Assertion.assertThat("Hotel Price on the Booking page is visible", !bookingFormPage.getHotelPriceDetail().isEmpty(), Matchers.is(true));
        roomListPage.clickChevronButton();
        Assertion.assertThat("Chevron Page is loaded", bookingFormPage.isChevronButtonLoaded(), Matchers.is(true));

    }

    @Author(names = "Sunny")
    @Module(name = "RoomList")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserApplyFilterInRoomList() throws FailedLoginException, InterruptedException{
        Map<String, String> param = getParam("verifyUserApplyFilterInRoomList");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HomesRoomListPageWeb<?> homesRoomList = HomesRoomListPageWeb.getInstance(session);

        String location = param.get ("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType,dateRange);

        step("Submit search");
        landingPage.clickSubmit();

        step("User click See Rooms button");
        homesRoomList.clickSeeRooms();

        step("User click filter on Room List");
        homesRoomList.clickFilter();

        step("Verify filter applied");
        Assertion.assertThat("Room filters are applied", homesRoomList.isFilterRoomListApplied(), is(true));
    }

    @Author(names = "Bikash Jha")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserApplyFilterAndSortingInSRP() throws FailedLoginException, InterruptedException{
        Map<String, String> param = getParam("verifyUserApplyFilterAndSortingInSRP");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("User open Home Page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open Hotel Landing Page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", hotelLP.isLandingPageVisible(), Is.is(true));
        hotelLP.closeContinueOnWebPopup();

        step("User search hotel by location");
        hotelLP.searchStayLocation(location);

        step("User hit search button");
        hotelLP.clickSubmit();

        Assertion.assertThat("SRP is Loaded", hotelSRP.isHotelCardVisible(), Is.is(true));

        step("User apply Popular filters");
        hotelSRP.applyPopularFilters();
        Assertion.assertThat("Popular filters are applied", hotelSRP.isPopularFilterApplied(), Is.is(true));

        step("Select sort by lowest Price");
        hotelSRP.clickSortButton();
        hotelSRP.clickSortByLowest();

        step("Check if sorted");
        Assertion.assertThat("Sorting is correct",hotelSRP.checkSorting(),is(true));
    }

    @Author(names = "Alden")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingHotelTwoChildren() throws Exception
    {
        Map<String, String> param = getParam("completeBookingHotelTwoChildren");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String hotel = param.get("hotel");
        int noOfGuest = Integer.parseInt(param.get("noOfGuest"));
        int noOfChild = Integer.parseInt(param.get("noOfChild"));
        int noOfRoom = Integer.parseInt(param.get("noOfRoom"));
        String childAges = param.get("childAges");


        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPage<?> bookingFormPage = new HotelBookingFormPage<>(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("Open the homepage");
        homePage.goToHomePage();

        step("Log in using an existing user account");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Go to the Hotel section and search for the target hotel");
        homePage.selectCurrency("IDR");
        homePage.openVerticalPageViaURL("Hotel");

        step("Configure guests and rooms for the search");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfGuest);
        landingPage.setTotalRoom(noOfRoom);
        landingPage.setTotalChildGuest(noOfChild);
        landingPage.setChildrenAges(childAges);
        landingPage.clickDone();

        step("Submit the search request");
        landingPage.searchStayLocation(hotel);
        landingPage.clickSubmit();

        step("Access the Property Detail Page (PDP)");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));

        step("Select 'See Rooms' to view available rooms");
        detailPage.seeHotelRoom();

        step("Select 'Book' to proceed with booking the room");
        roomListPage.clickBookButton();
        roomListPage.clickBookButtonConfirmation();

        step("Enter the Booking Form page and verify hotel name");
        Assertion.assertThat("Hotel name matches", bookingFormPage.getBookingHotelName(), Is.is(hotel));

        step("Continue to the payment page");
        bookingFormPage.continueToPaymentPage();

        step("Fetch the order ID from the payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed on the payment page", !orderID.isEmpty(), Is.is(true));

        step("Enter credit choose payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        step("Finish the payment process by clicking the payment button");
        paymentPage.clickOnPaymentButton();

        step("View your orders by opening the order list");
        paymentPage.clickSeeOrderListButton();

        step("Navigate to the details page of the issued e-ticket order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), Is.is(true));
        Assertion.assertThat("Contact name should be available on the order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("Email should be correctly displayed on the order detail page", orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("Order ID should be visible and match the ID from the payment page", orderDetailPage.getOrderId(), Is.is(orderID));
    }

    @Author(names = "Alden")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingHotelOneChildrenNonIDR() throws Exception {
        Map<String, String> param = getParam("completeBookingHotelOneChildrenNonIDR");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String currency = param.get("currency");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String hotel = param.get("hotel");
        int noOfGuest = Integer.parseInt(param.get("noOfGuest"));
        int noOfChild = Integer.parseInt(param.get("noOfChild"));
        int noOfRoom = Integer.parseInt(param.get("noOfRoom"));
        String vendor = param.get("vendor");
        String cardHolderName = param.get("cardHolderName");
        String cardNumber = param.get("cardNumber");
        String cvv = param.get("cardCvv");
        String cardMonth = param.get("cardMonth");

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPage<?> bookingFormPage = new HotelBookingFormPage<>(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("Open the homepage");
        homePage.goToHomePage();

        step("Log in using an existing user account");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Go to the Hotel section and search for the target hotel");
        homePage.selectCurrency(currency);
        homePage.openVerticalPageViaURL("Hotel");

        step("Configure guests and rooms for the search");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfGuest);
        landingPage.setTotalRoom(noOfRoom);
        landingPage.setTotalChildGuest(noOfChild);
        landingPage.clickDone();

        step("Submit the search request");
        landingPage.searchStayLocation(hotel);
        landingPage.clickSubmit();

        step("Access the Property Detail Page (PDP)");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));

        step("Select 'See Rooms' to view available rooms");
        detailPage.seeHotelRoom();

        step("Search for desired vendor HotelBeds with max 10 retries and book");
        String desiredVendor = vendor;
        int maxTries = 10;
        String vendorName = roomListPage.searchForDesiredVendor(desiredVendor, maxTries);
        Assertion.assertThat("Verify vendor is HotelBeds", vendorName, containsString(desiredVendor));

        step("Select 'Book' to proceed with booking the room");
        roomListPage.clickBookButton();

        step("Enter the Booking Form page and verify hotel name");
        Assertion.assertThat("Hotel name matches", bookingFormPage.getBookingHotelName(), Is.is(hotel));

        step("Continue to the payment page");
        bookingFormPage.continueToPaymentPage();

        step("Fetch the order ID from the payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed on the payment page", !orderID.isEmpty(), Is.is(true));

        step("Enter credit card details for payment");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Finish the payment process by clicking the payment button");
        paymentPage.clickOnPaymentButton();
        paymentPage.handle3DSPage(30);

        step("View your orders by opening the order list");
        paymentPage.clickSeeOrderListButton();

        step("Navigate to the details page of the issued e-ticket order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), Is.is(true));
        Assertion.assertThat("Contact name should be available on the order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("Email should be correctly displayed on the order detail page", orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("Order ID should be visible and match the ID from the payment page", orderDetailPage.getOrderId(), Is.is(orderID));

        step("Verify total rooms and children count");
        int actualTotalRooms = orderDetailPage.getTotalRoom();
        Assertion.assertThat("No of Rooms should be correct. Expected: " + noOfRoom + " Actual: " + actualTotalRooms, actualTotalRooms, is(noOfRoom));

        int actualTotalChildren = orderDetailPage.getTotalChild(orderID);
        Assertion.assertThat("No of Children should be correct. Expected: " + noOfChild + " Actual: " + actualTotalChildren, actualTotalChildren, is(noOfChild));
    }

    @Author(names = "Dixita")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkDetailOnBookingPageLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkDetailOnBookingPageLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String password=param.get("password");
        String email=param.get("email");
        String splReq=param.get("splReq");
        String customReq=param.get("customReq");
        String otp = param.get("otp");
        String fullName = param.get("fullName");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("Open home page and login");
        homePage.goToHomePage();
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Navigate to Hotel vertical and search for stay");
        homePage.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(location);
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        step("Go Inside PDP");
        Assertion.assertThat("PDP is loaded", detailPage.isPdpLoaded(), is(true));
        detailPage.seeHotelRoom();

        step("Ensure room list section is visible");
        roomListPage.scrollToRoomListBookSection();
        Assertion.assertThat("Room List is visible", roomListPage.isRoomListSectionVisible(), is(true));

        step("Click select on Room List");
        roomListPage.clickBookRoomButton();

        step("Click book button");
        Assertion.assertThat("Redirected to Booking Form", bookingFormPage.isContactForm(), is(true));

        step("User enter special Request ");
        bookingFormPage.fillGuestDetails(1,fullName,true,true,splReq);
        bookingFormPage.enterSpecialRequest(customReq);
        Assertion.assertThat("Verify if Special Request is added successfully",bookingFormPage.getSpecialReqText(),containsString(customReq));
        Assertion.assertThat("Hotel Price on the Booking page is visible", !bookingFormPage.getHotelPriceDetail().isEmpty(), is(true));

        step("Click Chevron Button");
        bookingFormPage.clickHotelChevron();
        Assertion.assertThat("Chevron Page is loaded", bookingFormPage.isChevronButtonLoadedInfo(), is(true));
    }

    @Author(names = "Dixita")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void checkDetailOnBookingPageNonLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("checkDetailOnBookingPageNonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String fullName=param.get("fullName");
        String phoneNumber=param.get("phoneNumber");
        String email=param.get("email");
        String splReq=param.get("splReq");
        String customReq=param.get("customReq");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);

        step("Open home page and login");
        homePage.goToHomePage();

        step("Navigate to Hotel vertical and search for stay");
        homePage.openVerticalPageViaURL("Hotel");
        landingPage.searchStayLocation(location);
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        step("Go Inside PDP");
        Assertion.assertThat("PDP is loaded", detailPage.isPdpLoaded(), is(true));
        detailPage.seeHotelRoom();

        step("Ensure room list section is visible");
        roomListPage.scrollToRoomListBookSection();
        Assertion.assertThat("Room List is visible", roomListPage.isRoomListSectionVisible(), is(true));

        step("Click select on Room List");
        roomListPage.clickBookRoomButton();

        step("Click book button");
        Assertion.assertThat("Redirected to Booking Form", bookingFormPage.isContactForm(), is(true));
        bookingFormPage.fillContactDetail(fullName,phoneNumber,email);

        step("User enter special Request ");
        bookingFormPage.fillGuestDetails(1,fullName,true,true,splReq);
        bookingFormPage.enterSpecialRequest(customReq);
        Assertion.assertThat("Verify if Special Request is added successfully",bookingFormPage.getSpecialReqText(),containsString(customReq));
        Assertion.assertThat("Hotel Price on the Booking page is visible", !bookingFormPage.getHotelPriceDetail().isEmpty(), is(true));

        step("Click Chevron Button");
        bookingFormPage.clickHotelChevron();
        Assertion.assertThat("Chevron Page is loaded", bookingFormPage.isChevronButtonLoadedInfo(), is(true));

    }

    @Author(names = "Akshi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingWithInsurance() throws FailedLoginException{
        Map<String, String> param = getParam("verifyBookingWithInsurance");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");
        String guestName=param.get("guestName");
        String guestNumber=param.get("guestNumber");
        String guestMail=param.get("guestMail");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage=HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency.equals("IDR"), Is.is(true));
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), Is.is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.switchToNewTab();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        String pdpPrice=detailPage.getRoomPrice();

        detailPage.clickSeeRoomButton();
        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go to Booking Form Page");
        bookingFormPage.enterGuestDetails(guestName,guestNumber,guestMail);
        bookingFormPage.setEnterGuestName(guestName);
        String priceOnBF1=bookingFormPage.getBookingPrice();
        Assertion.assertThat("Price on booking Page is correct",bookingFormPage.isTotalPriceCorrect(priceOnBF1,pdpPrice),is(true));
        bookingFormPage.continueToNextPage();

        step("Add insurance");
        String insurancePrice=bookingFormPage.getInsurancePrice();
        bookingFormPage.addInsurance();
        bookingFormPage.openPriceModal();
        Assertion.assertThat("Price added for insurance in Price details",bookingFormPage.checkInsurancePrice(insurancePrice),is(true));
        Assertion.assertThat("Check price after insurance is correct",bookingFormPage.checkPriceAfterInsurance(priceOnBF1,insurancePrice),is(true));
        int totalPriceAfterInsurance=bookingFormPage.getTotalPrice();
        bookingFormPage.continueToPaymentPage();

        step("Go to Payment Page");
        Assertion.assertThat("Total Price is including insurance",paymentPage.checkFinalPrice(totalPriceAfterInsurance),is(true));

        step("Enter credit choose payment method");
        paymentPage.selectPaymentMethod("Sakuku");

        paymentPage.openPriceSummary();
        paymentPage.openPriceDetails();
        Assertion.assertThat("Check InsurancePrice on Payment Page",paymentPage.checkInsurancePrice(insurancePrice),is(true));

        step("Finish the payment process by clicking the payment button");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment Done",paymentPage.isPaymentSuccessfulVisible(),is(true));
    }

    @Author(names = "Akshi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingWithPromo() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyBookingWithPromo");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String code=param.get("code");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelRoomListPage<?>roomListPage=new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?>bookingFormPage=HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();
        detailPage.switchToNewTab();
        step("Go Inside PDP");

        Assertion.assertThat("Verify User is redirected to PDP",detailPage.isPdpLoaded(),is(true));
        detailPage.seeHotelRoom();

        roomListPage.scrollToRoomListSection();
        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go to Booking Form 2");
        String price=bookingFormPage.getBookingPrice();
        bookingFormPage.continueToNextPage();
        Assertion.assertThat("Check promo section present",bookingFormPage.isPromoSectionAvailable(),is(true));
        bookingFormPage.clickCheckOtherPromoCodes();
        bookingFormPage.addPromoWithCode(code);
        bookingFormPage.openDiscountModal();
        Assertion.assertThat("Total Price is correct",bookingFormPage.checkPriceAfterPromo(price),is(true));
        int totalPriceAfterPromo=bookingFormPage.getTotalPrice();

        step("Go to Payment Page");
        bookingFormPage.continueToPaymentPage();
        Assertion.assertThat("Payment page loaded",!(paymentPage.getOrderId().isEmpty()),is(true));
        Assertion.assertThat("Total Price is including promo",paymentPage.checkFinalPrice(totalPriceAfterPromo),is(true));

        step("Enter credit choose payment method");
        Assertion.assertThat("Check promo applied",paymentPage.checkPromo(code),is(true));

        step("Finish the payment process by clicking the payment button");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment Done",paymentPage.isPaymentSuccessfulVisible(),is(true));
    }
}