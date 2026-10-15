package com.tiket.web.dWeb.test.accommodation.home;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.ExtranetHotelApi;
import com.tiket.apiPage.ExtranetHotelAuthApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Platform;
import com.tiket.pojo.extranet.login.ExtranetAdminLoginMfaApplyResponse;
import com.tiket.io.api.ApiTools;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.dWeb.page.accommodation.homes.HomesPDP;
import com.tiket.web.dWeb.page.accommodation.homes.HomesSearchResultPage;
import com.tiket.web.dWeb.page.accommodation.homes.HomesSeoPage;
import com.tiket.web.dWeb.page.accommodation.hotels.*;
import com.tiket.web.page.accommodation.hotels.SRP.HotelSearchResultPage;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.LandingPage.LandingPageWeb;
import com.tiket.web.page.accommodation.hotels.PDP.PDPPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelRoomListPage.HotelRoomListPageWeb;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.testng.annotations.Test;
import javax.security.auth.login.FailedLoginException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class HomesTest extends BaseTest {
    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySameHomeFromSrpToPdpGuest() {
        Map<String, String> param = getParam("verifySameHomeFromSrpToPdpGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        HomesSearchResultPage<?> searchResultPageNHA = new HomesSearchResultPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        String expectedHotelName = searchResultPageNHA.getHotelName();
        String expectedPrice = searchResultPageNHA.getHotelPrice();
        String[] expectedLocation = searchResultPageNHA.getHotelLocation();

        step("Select NHA Card ");
        searchResultPage.selectHotelCard();

        step("Go Inside PDP");
        detailPage.switchToNewTab();

        boolean roomAvailable = detailPage.isRoomsAvailable();

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("NHA name on the search and detail page should be equal", detailPage.getHotelNameDetail(), is(expectedHotelName));
        Assertion.assertThat("NHA Area on the search and detail page should be equal", detailPage.getHotelAreaDetail(), is(expectedLocation[0].trim()));
        Assertion.assertThat("NHA City on the search and detail page should be equal", detailPage.getHotelCityDetail(), is(expectedLocation[1].trim()));
        Assertion.assertThat("NHA Region on the search and detail page should be equal", detailPage.getHotelRegionDetail(), is(location));
        if (roomAvailable) {
            Assertion.assertThat("NHA Price on the Detail page is visible", !detailPage.getHotelPriceDetail().isEmpty(), is(true));
        } else {
            Assertion.assertThat("If room is fully booked respective text is visible on NHA PDP", detailPage.getBookingFullDetail(), containsString(" fully booked"));
        }
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserRedirectToPdpGuest() {
        Map<String, String> param = getParam("verifyUserRedirectToPdpGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotel = param.get("hotel");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int totalGuest = Integer.parseInt(param.get("totalGuest"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(hotel);

        step("Set guests");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(totalGuest);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("NHA's Image is visible on NHA Card", detailPage.isHotelImageVisible(), is(true));
        Assertion.assertThat("NHA name is same as User searched on LP", detailPage.getHotelNameDetail(), containsString(hotel));
        Assertion.assertThat("NHA's Area is visible on NHA PDP", !detailPage.getHotelAreaDetail().isEmpty(), is(true));
        Assertion.assertThat("NHA's City is visible on NHA PDP", !detailPage.getHotelCityDetail().isEmpty(), is(true));
        Assertion.assertThat("NHA's Region is visible on NHA PDP", !detailPage.getHotelRegionDetail().isEmpty(), is(true));
        if (detailPage.isRoomsAvailable()) {
            Assertion.assertThat("NHA's Price is visible on NHA PDP", !detailPage.getHotelPriceDetail().isEmpty(), is(true));
        } else {
            Assertion.assertThat("If room is fully booked respective text is visible on NHA PDP", !detailPage.getBookingFullDetail().isEmpty(), is(true));
        }
    }

    @Author(names = "Harshit")
    @Module(name = "SearchPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyDetailsInSRPIsDisplayedGuest() {
        Map<String, String> param = getParam("verifyDetailsInSRPIsDisplayedGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        HomesSearchResultPage<?> searchResultPageNHA = new HomesSearchResultPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        String expectedHotelName = searchResultPage.getHotelName();
        String[] expectedLocation = searchResultPageNHA.getHotelLocation();

        String expectedPrice = searchResultPage.getHotelPrice();
        String SearchBarLocation = searchResultPage.getLocationNameFromInput();

        Assertion.assertThat("NHA Card on SRP is visible ", searchResultPage.isHotelCardVisible(), is(true));
        Assertion.assertThat("NHA's name is visible on NHA Card ", !expectedHotelName.isEmpty(), is(true));
        Assertion.assertThat("NHA's Area is visible on NHA Card", !expectedLocation[0].trim().isEmpty(), is(true));
        Assertion.assertThat("NHA's City is visible on NHA Card", !expectedLocation[1].trim().isEmpty(), is(true));
        Assertion.assertThat("NHA's Price is visible on NHA Card", !expectedPrice.isEmpty(), is(true));
        Assertion.assertThat("NHA's Benefits is visible on NHA Card", searchResultPage.isHotelBenefitsVisible(), is(true));
        Assertion.assertThat("NHA's Image is visible on NHA Card", searchResultPage.isHotelImageVisible(), is(true));
        Assertion.assertThat("MAP is visible on SRP", searchResultPage.isMapVisible(), is(true));
        Assertion.assertThat("Searched Text entered by user  is matching on SRP's searchbar", location, is(SearchBarLocation));
    }

    @Author(names = "Harshit")
    @Module(name = "SearchPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyDetailsInSRPIsChangedGuest() throws InterruptedException {
        Map<String, String> param = getParam("verifyDetailsInSRPIsChangedGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String newLocation = param.get("newLocation");
        int noOfAdults = Integer.parseInt(param.get("Adult"));
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");


        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        HomesSearchResultPage<?> searchResultPageNHA = new HomesSearchResultPage<>(session);

        step("User opens home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User selects IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search button");
        landingPage.clickSubmit();

        step("Close pop-up");
        searchResultPage.clickPopUp();

        step("Input new location");
        searchResultPage.UpdateStayLocation(newLocation);

        step("Update guests ");
        searchResultPage.clickRoomAndGuestButton();
        searchResultPageNHA.setTotalGuest(noOfAdults);
        searchResultPage.clickDone();

        step("Set date");
        searchResultPage.setStayDate(dateType, dateRange);

        step("Click search button to update SRP results");
        searchResultPage.clickSearchOnSRP();

        step("SRP is refreshed");

        String SearchBarLocation = searchResultPage.getLocationNameFromInput();
        String RoomAndGuestDetails = searchResultPage.getRoomAndGuestInfoFromInput();

        Assertion.assertThat("NHA card on SRP is visible", searchResultPage.isHotelCardVisible(), is(true));
        Assertion.assertThat("Updated Searched text entered by user matches SRP's search bar", newLocation, is(SearchBarLocation));

        int adultsFromSRP = Integer.parseInt(RoomAndGuestDetails.split(" ")[0]);
        Assertion.assertThat("Number of updated adults should match on SRP", adultsFromSRP, is(noOfAdults));
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void VerifyHomeInfoAndInteractionInPDPGuest() {
        Map<String, String> param = getParam("VerifyHomeInfoAndInteractionInPDPGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int noOfAdults = Integer.parseInt(param.get("Adult"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set guests ");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("NHA's Image is visible on NHA Detail Page", detailPage.isHotelImageVisible(), is(true));
        Assertion.assertThat("NHA name on the  detail page should be visible", !detailPage.getHotelNameDetail().isEmpty(), is(true));
        Assertion.assertThat("NHA's Area is visible on NHA PDP", !detailPage.getHotelAreaDetail().isEmpty(), is(true));
        Assertion.assertThat("NHA's City is visible on NHA PDP", !detailPage.getHotelCityDetail().isEmpty(), is(true));
        Assertion.assertThat("Highlights Section is visible on NHA PDP", detailPage.isHighLightsVisible(), is(true));
        Assertion.assertThat("Popular Facilities Section is visible on NHA PDP", detailPage.isPopularFacilitiesVisible(), is(true));
        Assertion.assertThat("Review Section is visible on NHA PDP", detailPage.isReviewsVisible(), is(true));
        Assertion.assertThat("Map Section is visible on NHA PDP", detailPage.isPdpMapVisible(), is(true));
        Assertion.assertThat("Accommodation Policies Section is visible on NHA PDP", detailPage.isAccommodationPoliciesVisible(), is(true));
        Assertion.assertThat("FAQs Section is visible on NHA PDP", detailPage.isFAQVisible(), is(true));

        if (detailPage.isRoomsAvailable()) {
            Assertion.assertThat("Hotel's Price is visible on Hotel PDP", !detailPage.getHotelPriceDetail().isEmpty(), is(true));
            Assertion.assertThat("See Rooms Button  is visible on Hotel PDP", detailPage.isSeeRoomVisible(), is(true));

            step("Rooms are available hence click on See Rooms Button");
            detailPage.seeHotelRoom();

            step("Checking Recommended Room");
            Assertion.assertThat("Recommended room should be visible on room list", roomListPage.isRecommendedRoomVisible(), is(true));
            roomListPage.recommendedRoomForGuest(noOfAdults);

            step("Checking room List");
            Assertion.assertThat("Room lists section should be visible", roomListPage.isRoomListSectionVisible(), is(true));
            Assertion.assertThat("Room's Image should be visible", roomListPage.isRoomImageVisible(), is(true));
        } else {
            Assertion.assertThat("If room is fully booked respective text is visible on Hotel PDP", !detailPage.getBookingFullDetail().isEmpty(), is(true));
        }
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void VerifyRoomOverviewRoomListGuest() {
        Map<String, String> param = getParam("VerifyRoomOverviewRoomListGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int noOfAdults = Integer.parseInt(param.get("Adult"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set guests ");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("NHA's Image is visible on NHA PDP", detailPage.isHotelImageVisible(), is(true));
        Assertion.assertThat("NHA name on the search and detail page should be equal", detailPage.getHotelNameDetail(), is(location));

        if (detailPage.isRoomsAvailable()) {
            Assertion.assertThat("NHA's Price is visible on NHA PDP", !detailPage.getHotelPriceDetail().isEmpty(), is(true));
            Assertion.assertThat("See Rooms Button  is visible on NHA PDP", detailPage.isSeeRoomVisible(), is(true));

            step("Rooms are available hence click on See Rooms Button");
            detailPage.seeHotelRoom();

            step("Checking room List");
            roomListPage.scrollToRoomListSection();
            Assertion.assertThat("Room lists section should be visible", roomListPage.isRoomListSectionVisible(), is(true));
            Assertion.assertThat("Room Image should be visible", roomListPage.isRoomImageVisible(), is(true));

            step("checking individuals rooms and their details is visibile on roomlist");
            roomListPage.roomListDetails(2);
        } else {
            Assertion.assertThat("If room is fully booked respective text is visible on Hotel PDP", !detailPage.getBookingFullDetail().isEmpty(), is(true));
        }
    }

    @Author(names = "Harshit")
    @Module(name = "SearchPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyLocationSearchRedirectionToSRPGuest() {
        Map<String, String> param = getParam("verifyLocationSearchRedirectionToSRPGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        String expectedHotelName = searchResultPage.getHotelName();
        String SearchBarLocation = searchResultPage.getLocationNameFromInput();

        Assertion.assertThat("SRP is loaded ", searchResultPage.isHotelCardVisible(), is(true));
        Assertion.assertThat("NHA's name is visible on NHA Card ", !expectedHotelName.isEmpty(), is(true));
        Assertion.assertThat("MAP is visible on SRP", searchResultPage.isMapVisible(), is(true));
        Assertion.assertThat("Searched Text entered by user  is matching on SRP's searchbar", location, is(SearchBarLocation));
    }

    @Author(names = "Harshit")
    @Module(name = "SearchPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyFlexibleStayContentOnSRPGuest() throws InterruptedException {
        Map<String, String> param = getParam("verifyFlexibleStayContentOnSRPGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        Assertion.assertThat("SRP is loaded ", searchResultPage.isHotelCardVisible(), is(true));

        step("Scroll to down to see Flexible Stay");
        searchResultPage.scrollToFlexibleStay();

        Assertion.assertThat("Flexible stay dates card is visible", searchResultPage.isFlexibleStayVisible(), is(true));
        Assertion.assertThat("NHA's Image on Flexible stay card is visible", searchResultPage.isFlexibleStayImageVisible(), is(true));
        Assertion.assertThat("Verifying Heading on Flexible Stay Date is correct based on selected Language ", searchResultPage.getFlexibleStayHeading(), either(containsString("Do you have flexible stay dates")).or(containsString("Tanggal nginepmu fleksibel")));
        Assertion.assertThat("NHA name on Flexible Stay Card is visible", !searchResultPage.getFlexibleStayHotelName().isEmpty(), is(true));
        Assertion.assertThat("(See and adjust you dates) text is present at bottom of NHA card based on selected Language", searchResultPage.getFlexibleStayText(), either(containsString("See and adjust your date")).or(containsString("Lihat dan atur tanggal")));

        step("Click on Flexible Stay Card");
        searchResultPage.clickOnFlexibleStayCard();
        detailPage.switchToNewTab();

        Assertion.assertThat("Verify if user is redirected to PDP ", !detailPage.getHotelNameDetail().isEmpty(), is(true));
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPDPRedirectionOnHomeSearchPageGuest() {
        Map<String, String> param = getParam("verifyPDPRedirectionOnHomeSearchPageGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String hotel = param.get("hotel");
        String location = param.get("location");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int noOfAdults = Integer.parseInt(param.get("Adult"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set guests ");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside SRP");
        searchResultPage.clickPopUp();

        step("Search Villa name on Srp");
        searchResultPage.UpdateStayLocation(hotel);

        step("Click search button to go inside PDP");
        searchResultPage.clickSearchOnSRP();

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("NHA's Image is visible on NHA PDP", detailPage.isHotelImageVisible(), is(true));
        Assertion.assertThat("NHA name is same as User searched on LP", detailPage.getHotelNameDetail(), containsString(hotel));
        Assertion.assertThat("NHA's Region is visible on NHA PDP", !detailPage.getHotelRegionDetail().isEmpty(), is(true));

        if (detailPage.isRoomsAvailable()) {
            Assertion.assertThat("NHA's Price is visible on NHA PDP", !detailPage.getHotelPriceDetail().isEmpty(), is(true));
        } else {
            Assertion.assertThat("If room is fully booked respective text is visible on NHA PDP", !detailPage.getBookingFullDetail().isEmpty(), is(true));
        }
    }

    @Author(names = "Harshit")
    @Module(name = "BookingPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingSoWithAddOnsGuest() throws InterruptedException {
        Map<String, String> param = getParam("completeBookingSoWithAddOnsGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int noOfAdults = Integer.parseInt(param.get("Adult"));
        String splReq = param.get("splReq");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        String pdpNhaName = detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("NHA name on the search and detail page should be equal", pdpNhaName, is(location));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list is visible", roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible", roomListPage.isRoomImageVisible(), is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));
        bookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("User enter Stay details ");
        Assertion.assertThat("Guest Stay details form should be visible on Booking Form page", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDetails(1, fullName, true, true, splReq);

        bookingFormPage.continueToNextPage();
        int oldPrice = bookingFormPage.getTotalPrice();

        step("User select addOns and insurance");
        bookingFormPage.chooseAddOnsAndInsurance(true);

        int newPrice = bookingFormPage.getTotalPrice();

        Assertion.assertThat("Price should be increased when user select AddOns and Insurance", newPrice, greaterThan(oldPrice));
        Assertion.assertThat("NHA name on Booking form should be correct ", bookingFormPage.getBookingHotelName(), containsString(pdpNhaName));

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));


        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User open order list page");
        paymentPage.clickSeeOrderListButton();

        Assertion.assertThat("Verify web page will redirect to login page for guest user", loginPage.isLoginPageOpened(), is(true));
    }

    @Author(names = "Harshit")
    @Module(name = "BookingPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingMoWithoutAddOnsLoginNHA() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("completeBookingMoWithoutAddOnsLoginNHA");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String fullName = CoreFunctions.randomName();
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        int noOfRooms = Integer.parseInt(param.get("rooms"));
        int guests = Integer.parseInt(param.get("guests"));
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");


        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open Home Landing page");
        homePage.openVerticalPageViaURL("Homes");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel name on the search and detail page should be equal", detailPage.getHotelNameDetail(), containsString(location));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list is visible", roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible", roomListPage.isRoomImageVisible(), is(true));

        step("User update number of room & guest on roomList");
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(guests);
        roomListPage.setTotalRoom(noOfRooms);

        roomListPage.clickGuestDoneButton();
        roomListPage.clickRoomListChangeButton();

        step("Click Book Button");
        roomListPage.firstBookRoom();

        step("Select Multiple Room");
        roomListPage.selectMultipleRoomWithSameRatePlan(noOfRooms);

        roomListPage.clickMultiBookButton();

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDetails(2, fullName, true, false, "");

        int bookingPrice = bookingFormPage.getTotalPrice();
        bookingFormPage.continueToNextPage();

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        paymentPage.clickSeeOrderListButton();

        step("User open order detail page");
        orderListPage.openActiveTicketViaURL(orderID);

        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("contact name should be available on order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));

        String priceFromOrderDetail = orderDetailPage.getVerticalTotalPrice();
        int orderPrice = Integer.parseInt(priceFromOrderDetail.split(" ")[1].replaceAll("\\D", ""));

        Assertion.assertThat("Price should be visible and should be same as from payment page", orderPrice, is(bookingPrice));
        Assertion.assertThat("No of Rooms should be correct", orderDetailPage.getTotalRoom(), is(noOfRooms));
    }

    @Author(names = "Harshit")
    @Module(name = "RoomListPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRoomDetailsGuest() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRoomDetailsGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open Homes Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();

        step("CLick on Room card to open Room Detail Page");
        roomListPage.openRoomDetail();

        Assertion.assertThat("Verify if Room Detail Page Opened", roomListPage.isRoomDetailPage(), is(true));
        Assertion.assertThat("Image is visible on Room Detail", roomListPage.isImageOnRoomDetail(), is(true));
        Assertion.assertThat("Price is visible on Room Detail", roomListPage.isPriceOnRoomDetail(), is(true));
        step("Verifying All the sections of Room Detail Page");
        roomListPage.roomDetailsVisible();
    }

    @Author(names = "Harshit")
    @Module(name = "RoomListPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySoRoomListGuest() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySoRoomListGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();

        step("Checking room List");
        Assertion.assertThat("Room lists section should be visible", roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Room Image should be visible", roomListPage.isRoomImageVisible(), is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();

        Assertion.assertThat(" User redirected to Booking Form when user click Book button on room card", bookingFormPage.isContactForm(), is(true));

        step("Go back to previous Page");
        detailPage.goToPreviousPage();

        step("Click on See other rooms");
        bookingFormPage.clickSeeOtherRooms();

        step("CLick on Room card to open Room Detail Page");
        roomListPage.openRoomDetail();

        Assertion.assertThat("Verify if Room Detail Page Opened", roomListPage.isRoomDetailPage(), is(true));
        Assertion.assertThat("Image is visible on Room Detail", roomListPage.isImageOnRoomDetail(), is(true));

        step("Click on book button on room Detail");
        roomListPage.clickBookRoomDetail();
        Assertion.assertThat(" User redirected to Booking Form when user click Book button on room Detail Page", bookingFormPage.isContactForm(), is(true));
    }

    @Author(names = "Harshit")
    @Module(name = "BookingPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void VerifyChooseAddOnsGuest() throws InterruptedException {
        Map<String, String> param = getParam("VerifyChooseAddOnsGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");


        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();
        homePage.isHomePageOpened();

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(1);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        String pdpNhaName = detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("NHA name on the search and detail page should be equal", pdpNhaName, is(location));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list is visible", roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible", roomListPage.isRoomImageVisible(), is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));
        bookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("User enter Stay details ");
        Assertion.assertThat("Guest Stay details form should be visible on Booking Form page", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDetails(1, fullName, true, false, "");

        bookingFormPage.continueToNextPage();
        int oldPrice = bookingFormPage.getTotalPrice();

        step("User select addOns and insurance");
        bookingFormPage.chooseAddOnsAndInsurance(true);

        int newPrice = bookingFormPage.getTotalPrice();

        Assertion.assertThat("Price should be increased when user select AddOns and Insurance", newPrice, greaterThan(oldPrice));
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecommendedRoomGuest() {
        Map<String, String> param = getParam("verifyRecommendedRoomGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int guests = Integer.parseInt(param.get("guests"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(guests);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("NHA's Price is visible on NHA PDP", !detailPage.getHotelPriceDetail().isEmpty(), is(true));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Checking Recommended Room");
        Assertion.assertThat("Recommended room should be visible on room list", roomListPage.isRecommendedRoomVisible(), is(true));
        roomListPage.recommendedRoomForGuest(guests);
        Assertion.assertThat("On Recommended room , total-room-night-summary should be visible", roomListPage.isTotalRoomNightRecommended(), is(true));
        Assertion.assertThat("On Recommended room total price should be visible", roomListPage.isRecommendedTotalPrice(), is(true));
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyAbilityToAddSplReqGuest() throws InterruptedException {
        Map<String, String> param = getParam("verifyAbilityToAddSplReqGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String fullName = CoreFunctions.randomName();
        String splReq = param.get("splReq");
        String customReq = param.get("customReq");
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));

        step("Checking room List");
        Assertion.assertThat("Room lists section should be visible", roomListPage.isRoomListSectionVisible(), is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));
        bookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("User enter Stay details and add special Request ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDetails(1, fullName, true, true, splReq);

        bookingFormPage.enterSpecialRequest(customReq);
        Assertion.assertThat("Verify if Special Request is added successfully", bookingFormPage.getSpecialReqText(), containsString(customReq));
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySimilarPropertiesGuest() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySimilarPropertiesGuest");
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
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Homes");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        Assertion.assertThat("Hotel name is same as User searched on LP", detailPage.getHotelNameDetail(), containsString(location));
        roomListPage.scrollToRoomListSection();

        step("Scroll to bottom");
        Assertion.assertThat("Verify Similar properties section is visible", roomListPage.isSimilarPropertiesVisible(), is(true));
        Assertion.assertThat("Verify Header for Similar Properties", roomListPage.getSimilarPropertiesHeader(), containsString("Similar places around " + location));
        Assertion.assertThat("Verify Similar Homes card are visible", roomListPage.isSimilarHotelcard(), is(true));

        step("Click on Similar Hotel card to open PDP in new Tab");
        roomListPage.clickSimilarHotel();
        detailPage.switchToNewTab();

        Assertion.assertThat("Verify if PDP Opened", detailPage.isPdpLoaded(), is(true));
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyGuestDetailsandSplReqGuest() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyGuestDetailsandSplReqGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String fullName = CoreFunctions.randomName();
        String phoneNumber = "91" + CoreFunctions.randomInt(8);
        String email = CoreFunctions.randomEmail();
        String splReq = param.get("splReq");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));

        step("Checking room List");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room lists section should be visible", roomListPage.isRoomListSectionVisible(), is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page", bookingFormPage.isContactForm(), is(true));
        bookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("User enter Stay details and add special Request ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDetails(1, fullName, true, true, "Connecting Room");
        Assertion.assertThat("Verify Guest name is added Successfully ", bookingFormPage.getGuestName(), containsString(fullName));

        bookingFormPage.enterSpecialRequest(splReq);
        Assertion.assertThat("Verify if Custom Special Request is added successfully",bookingFormPage.getSpecialReqText(),containsString(splReq));
        Assertion.assertThat("Verify if Other Special Request is added successfully",bookingFormPage.getSpecialReqText(),containsString("Connecting Room"));
    }


    @Author(names = "Prabhleen")
    @Module(name = "Homes Landing Page")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void deeplinkHomesPDP() {
        Map<String, String> param = getParam("deeplinkHomesPDP");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomesPDP<?> homesPDP = new HomesPDP<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);

        String hotel_name = param.get("Hotel_name");

        step("User open home page");
        homePage.goToHomePage();

        step("Open Deeplink");
        homesPDP.openHomesPDPlink();
        String getHomesName = homesPDP.getHomesName();
        Assertion.assertThat("Property name Page is visible", getHomesName, is(hotel_name));

    }

    @Author(names = "Dixita")
    @Module(name = "BookingPage")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void correctredirectiontobookingform() throws InterruptedException {
        Map<String, String> param = getParam("correctredirectiontobookingform");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        HomesSearchResultPage<?> searchResultPageNHA = new HomesSearchResultPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        String hotelName = searchResultPageNHA.getHotelName();

        step("Select NHA Card ");
        searchResultPage.selectHotelCard();

        step("Go Inside PDP");
        detailPage.switchToNewTab();
        Assertion.assertThat("Verify User is redirected to PDP",detailPage.isPdpLoaded(),is(true));

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list is visible",roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible",roomListPage.isRoomImageVisible(),is(true));

        String room_name=roomListPage.getRoomName();

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go Inside Booking Form");
        Assertion.assertThat("Hotel name matched",bookingFormPage.getBookingHotelName(),is(hotelName));
        Assertion.assertThat("Room name is same",bookingFormPage.getBookingRoomName(),is(room_name));
    }

    @Author(names = "Prabhleen")
    @Module(name = "ProductDetailPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void VerifyAvailableDates() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("VerifyAvailableDates");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        String publicHotelId = param.get("publicHotelId");
        String day = param.get("day");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User change language");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("homes");

        step("Dismiss 'Install app' pop up");
        landingPage.dismissInstallAppPopUp();

        step("Go to Hotel which has next available dates");
        pdpPageWeb.goToHomeViaDeeplink(location, publicHotelId, day);

        step("Verify hotel details elements are visible");
        Assertion.assertThat("Hotel name should be visible", pdpPageWeb.isHotelNameVisible(), Is.is(true));
        Assertion.assertThat("Next available date night should be visible", pdpPageWeb.isNextAvailableDateNightVisible(), Is.is(true));
        Assertion.assertThat("Next available date range should be visible", pdpPageWeb.isNextAvailableDateRangeVisible(), Is.is(true));
        Assertion.assertThat("Next available date price should be visible", pdpPageWeb.isNextAvailableDatePriceVisible(), Is.is(true));
        Assertion.assertThat("Next available date total price text should be visible", pdpPageWeb.isNextAvailableDateTotalPriceTextVisible(), Is.is(true));
        Assertion.assertThat("Fully booked title should be visible", pdpPageWeb.isFullyBookedTitleVisible(), Is.is(true));
        Assertion.assertThat("Best price ribbon on PDP should be visible", pdpPageWeb.isBestPriceRibbonPDPVisible(), Is.is(true));
        Assertion.assertThat("Best price ribbon on room list should be visible", pdpPageWeb.isBestPriceRibbonRoomListVisible(), Is.is(true));
        Assertion.assertThat("Next available date no room text should be visible", pdpPageWeb.isNextAvailableDateNoRoomTextVisible(), Is.is(true));

        step("Dismiss 'Install app' pop up");
        landingPage.dismissInstallAppPopUp();

        step("Click next available date");
        pdpPageWeb.clickNextAvailableDate();

        step("Verify see room button is visible");
        pdpPageWeb.clickSeeRoomButton();

        step("Verify room list section is visible");
        Assertion.assertThat("Room List is visible", roomListPage.isRoomImageVisible(), Matchers.is(true));
    }




    @Author(names = "Dixita")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void multiorderplan() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("multiorderplan");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        int noOfRooms = Integer.parseInt(param.get("noOfRooms"));
        int guests = Integer.parseInt(param.get("guests"));
        String fullName = CoreFunctions.randomName();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Login as guest");
        loginPage.loginAsGuest();

        step("Select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search location");
        landingPage.searchStayLocation(location);

        step("Submit search");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        detailPage.switchToNewTab();
        Assertion.assertThat("Verify User is redirected to PDP",detailPage.isPdpLoaded(),is(true));

        step("Scroll to room list");

        step("Update room and guest");
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalRoom(noOfRooms);
        roomListPage.setTotalGuest(guests);
        roomListPage.clickGuestDoneButton();

        step("Click Change Room List button");
        roomListPage.clickRoomListChangeButton();

        step("Select first room");
        roomListPage.firstBookRoom();

        step("Select second room with different rate plan");
        roomListPage.secondBookRoom();

        step("Click Multi-Book button");
        roomListPage.clickMultiBookButton();

        step("Verify contact form is visible");
        Assertion.assertThat("Booking form is visible", bookingFormPage.isContactForm(), is(true));

        step("Fill Guest details for multiple rooms");
        Assertion.assertThat("Guest stay form is visible", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDifferentRatePlan(fullName);

        step("Verify rooms have different rate plans");
        bookingFormPage.openPriceModal();
        int roomPrice1 = bookingFormPage.getIndividualPrice(1);
        int roomPrice2 = bookingFormPage.getIndividualPrice(2);
        bookingFormPage.closePriceModal();
        Assertion.assertThat("Room prices are different (different rate plans)", roomPrice1, not(roomPrice2));
    }

    @Author(names = "Akshi")
    @Module(name = "SearchResultPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyDetailsOnSRPAreCorrect() {
        Map<String, String> param = getParam("verifyDetailsOnSRPAreCorrect");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int noOfAdults = Integer.parseInt(param.get("Adult"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set guests ");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside SRP");
        Assertion.assertThat("Location is same",searchResultPage.getLocation(),is(location));
        Assertion.assertThat("Right hotel is visible",searchResultPage.rightHotel(location),is(true));
    }


    @Author(names = "Dixita")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void changedDetailsOnSRP() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("changedDetailsOnSRP");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String newLocation = param.get("newLocation");
        int noOfAdults = Integer.parseInt(param.get("noOfAdults"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Submit search button");
        landingPage.clickSubmit();

        step("Close pop-up");
        searchResultPage.clickPopUp();

        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  search Result page is set to IDR", searchPageCurrency.equals("IDR"), is(true));

        step("Input new location");
        searchResultPage.UpdateStayLocation(newLocation);

        step("Update guests and rooms");
        searchResultPage.clickRoomAndGuestButton();
        searchResultPage.setTotalGuest(noOfAdults);
        searchResultPage.clickDone();

        step("Click search button to update SRP results");
        searchResultPage.clickSearchOnSRP();

        step("SRP is refreshed");

        String SearchBarLocation = searchResultPage.getLocationNameFromInput();

        Assertion.assertThat("Hotel card on SRP is visible", searchResultPage.isHotelCardVisible(), is(true));
        Assertion.assertThat("Searched text entered by user matches SRP's search bar", newLocation, is(SearchBarLocation));
    }

    @Author(names = {"Mridul"})
    @Module(name = "SEO")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHomesSeoPageDetails() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyHomesSeoPageDetails");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String propertyTypes = param.get("propertyTypes");
        String username = param.get("username");
        String password = param.get("password");
        int cardCountToCheck = CoreFunctions.getIntFromString(param.getOrDefault("cardCount", "3")).orElse(3);

        List<String> propertyList = CoreFunctions.splitText(propertyTypes, "\\|").stream()
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .collect(java.util.stream.Collectors.toList());

        HomesSeoPage<?> seoPage = HomesSeoPage.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("Go to home page");
        homePage.goToHomePage();

        step("Login using cookie");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        for (String propertyName : propertyList) {
            String propertySlug = seoPage.toSlug(propertyName);

            // Matcher for property name that accepts "Istana" if checking "Palace"
            org.hamcrest.Matcher<String> propertyMatcher = "Palace".equalsIgnoreCase(propertyName)
                    ? anyOf(containsStringIgnoringCase("Palace"), containsStringIgnoringCase("Istana"))
                    : containsStringIgnoringCase(propertyName);

            step("Open homes SEO page for property type: " + propertyName + " (" + propertySlug + ")");
            seoPage.openSeoPageByProperty(propertyName);
            Assertion.assertThat("[" + propertyName + "] SEO page should open on a /homes/ URL", seoPage.getUrl(), containsString("/homes/" + propertySlug));

            step("[" + propertyName + "] Assertion 1 - verify breadcrumb shows 'Homes' link and active '" + propertyName + "' label");
            Assertion.assertThat("[" + propertyName + "] Breadcrumb should contain the 'Homes' parent link", seoPage.getBreadcrumbHomesLinkText(), containsStringIgnoringCase("Homes"));
            Assertion.assertThat("[" + propertyName + "] Breadcrumb active label should be the property type", seoPage.getBreadcrumbActiveLabel(), propertyMatcher);
            Assertion.assertThat("[" + propertyName + "] Breadcrumb trail should include both 'Homes' and the property type", seoPage.getBreadcrumbText(),
                    allOf(containsStringIgnoringCase("Homes"), propertyMatcher));

            step("[" + propertyName + "] Assertion 2 - verify page title contains property type");
            Assertion.assertThat("[" + propertyName + "] Page title should contain the property type and tiket.com", seoPage.getPageTitle(),
                    allOf(propertyMatcher, containsStringIgnoringCase("tiket.com")));

            step("[" + propertyName + "] Assertion 3 - verify result count is greater than 0 and references the property type");
            String hotelCountText = seoPage.getHotelCountText();
            Assertion.assertThat("[" + propertyName + "] Result count text should reference the property type", hotelCountText, propertyMatcher);
            Assertion.assertThat("[" + propertyName + "] Result count should be greater than 0", seoPage.getHotelCount(), greaterThan(0));

            step("[" + propertyName + "] Assertion 4 - verify '" + propertyName + " in Popular Locations' side panel");
            Assertion.assertThat("[" + propertyName + "] '" + propertyName + " in Popular Locations' side panel should be visible", seoPage.isPopularLocationsPanelVisible(propertyName), is(true));
            String popularLocationsHeading = seoPage.getPopularLocationsHeading(propertyName);

            Assertion.assertThat("[" + propertyName + "] Popular Locations heading should reference the property type",
                    popularLocationsHeading,
                    allOf(
                            propertyMatcher,
                            anyOf(
                                    containsStringIgnoringCase("Popular Locations"),
                                    containsStringIgnoringCase("Lokasi Populer")
                            )
                    ));
            Assertion.assertThat("[" + propertyName + "] Popular Locations panel should expose location links", seoPage.getPopularLocationsLinkCount(propertyName), greaterThan(0));

            step("[" + propertyName + "] Assertion 5 - verify first " + cardCountToCheck + " SEO cards carry the '" + propertyName + "' property label");
            List<String> cardLabels = seoPage.getSeoCardPropertyLabels(cardCountToCheck);
            Assertion.assertThat("[" + propertyName + "] At least one SEO card should be present", cardLabels.size(), greaterThan(0));
            Assertion.assertThat("[" + propertyName + "] Every checked SEO card should show the '" + propertyName + "' property label", cardLabels,
                    everyItem(propertyMatcher));
        }
    }
}
