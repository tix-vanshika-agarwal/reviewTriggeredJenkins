package com.tiket.web.test.accommodation.homes;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.ExtranetHotelApi;
import com.tiket.apiPage.ExtranetHotelAuthApi;
import com.tiket.apiPage.HotelApi;
import com.tiket.bq.OutboundNotificationVerifier;
import com.tiket.core.CoreFunctions;
import com.tiket.dbclient.bigquery.NotificationChannel;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.pojo.accommodation.hotel.HotelAutoCompleteResponse;
import com.tiket.pojo.accommodation.hotel.SearchHotelResponse;
import com.tiket.pojo.extranet.login.ExtranetAdminLoginMfaApplyResponse;
import com.tiket.pojo.room.SelectRoomResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.web.dWeb.page.accommodation.homes.HomesPDP;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelRoomListPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.accommodation.homes.LandingPage.HomesLandingPageWeb;
import com.tiket.web.dWeb.page.accommodation.homes.HomesSearchResultPage;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelDetailPage;
import com.tiket.web.page.accommodation.homes.homesRoomListPage.HomesRoomListPageWeb;
import com.tiket.web.page.accommodation.homes.homesSearchResultsPage.HomesSearchResultPageMWeb;
import com.tiket.web.page.accommodation.homes.homesSearchResultsPage.HomesSearchResultPageWeb;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.LandingPage.LandingPageWeb;
import com.tiket.web.page.accommodation.hotels.PDP.PDPPageWeb;
import com.tiket.web.page.accommodation.hotels.SRP.HotelSearchResultPage;
import com.tiket.web.page.accommodation.hotels.SRP.SRPPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelRoomListPage.HotelRoomListPageWeb;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.common.eTicket.ETicketWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import org.hamcrest.core.Is;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class HomesTest extends BaseTest{
    @Author(names = "Dixita")
    @Module(name = "Homes Landing Page")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void deeplinkhomePDP() {
        Map<String, String> param = getParam("deeplinkhomePDP");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomesPDP<?> homesPDP = new HomesPDP<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);

        String hotel_name = param.get("hotel_name");

        step("User open home page");
        homePage.goToHomePage();

        step("Open Deeplink");
        homesPDP.openHomesPDPlink();
        String getHomesName = homesPDP.getHomesName();
        Assertion.assertThat("Property name Page is visible", getHomesName, is(hotel_name));

    }

    @Author(names = "Yazid")
    @Module(name = "SRP")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyAfterTaxTotalPriceHomes() throws InterruptedException {
        Map<String, String> param = getParam("verifyAfterTaxTotalPriceHomes");
        BaseTest.paramsThreadLocal.set(param);
        String location = param.get("location");
        String locationType = param.get("locationType");
        String currency = param.get("currency");
        String rangeAddDate = param.get("rangeAddDate");
        int adult = 1; int room = 1; int night = 2;
        String[] dates = generateCheckInOutDate(rangeAddDate, night);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];
        String accommodationType = "homes";

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        SRPPageWeb<?> srpPageWeb = SRPPageWeb.getInstance(session);
        PDPPageWeb<?> pdpPageWeb = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> hotelRoomListPageWeb = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> hotelBookingFormPageWeb = HotelBookingFormPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User is on non-login mode");
        loginPage.loginAsGuest();

        step("User select currency: " + currency);
        homePage.selectCurrency(currency);
        String language = homePage.getLanguage();
        String separator = language.equals("en") ? "," : ".";

        step("Open SRP page via URL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData("", "", location, locationType);
        int index = hotelApi.findHotelIndex(hotelAutoCompleteResponse , location);
        String locationId = hotelAutoCompleteResponse.data.contents.get(index).publicId;
        srpPageWeb.openSRPViaUrl(accommodationType, room, adult, locationId, locationType, location, checkInDate, checkOutDate);

        step("Put toggle on for total price and include tax filter");
        srpPageWeb.setPriceTaxFilter(true, true);

        step("User check SRP price");
        SearchHotelResponse searchHotelResponse = hotelApi.getSearchHotelData(accommodationType, locationId, locationType, "", "", night, room, adult, checkInDate, currency);
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) srpPageWeb.saveFilters();
        srpPageWeb.checkSrpTotalPrice(searchHotelResponse, currency, separator, true);
        Assertion.assertThat("Price in SRP page label is total price", srpPageWeb.isLabelTotalPrice(), is(true));

        step("User check PDP");
        String selectedHotelName = srpPageWeb.getSRPHotelName();
        SelectRoomResponse selectRoomResponse = hotelApi.getHotelRoomData(selectedHotelName, "", "", night, room, adult, checkInDate, currency);
        srpPageWeb.selectHotelName(selectedHotelName);
        pdpPageWeb.switchToNewTab();
        pdpPageWeb.checkPdpTotalPrice(selectRoomResponse, selectedHotelName, currency, separator, true);
        Assertion.assertThat("Price in PDP page label is total price", pdpPageWeb.isLabelTotalPrice(), is(true));

        step("User check SO Room List");
        hotelRoomListPageWeb.checkRoomListTotalPrice(selectRoomResponse, selectedHotelName, currency, separator, true);
        Assertion.assertThat("Price in Room List page label is total price", hotelRoomListPageWeb.isLabelTotalPrice(), is(true));

        step("User check Booking Form price footer page 1");
        String selectedPrice = hotelRoomListPageWeb.getRoomListPrice();
        int targetPrice = (int) removeText(selectedPrice);
        String[] bookingPrices = hotelApi.getPrebookPrice(accommodationType, selectedHotelName, "", "", night, room, adult, checkInDate, currency, separator, targetPrice, true, true);
        hotelRoomListPageWeb.clickBookButton(selectedPrice);
        hotelBookingFormPageWeb.checkBookingPagePrice(bookingPrices);
        Assertion.assertThat("Price in Booking Form page label is total price", hotelBookingFormPageWeb.isLabelTotalPrice(), is(true));

        step("User check MO Room List");
        adult = 2; room = 2;
        selectRoomResponse = hotelApi.getHotelRoomData(selectedHotelName, "", "", night, room, adult, checkInDate, currency);
        srpPageWeb.openSRPViaUrl(accommodationType, room, adult, locationId, locationType, location, checkInDate, checkOutDate);
        hotelRoomListPageWeb.checkRoomListTotalPrice(selectRoomResponse, selectedHotelName, currency, separator, true);
        Assertion.assertThat("Price in Room List page label is total price", hotelRoomListPageWeb.isLabelRoomNightPrice(), is(true));
    }

    @Author(names = "Caroline")
    @Module(name = "SearchPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserApplyFilterInHomesSRP() {
        Map<String, String> param = getParam("verifyUserApplyFilterInHomesSRP");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HomesLandingPageWeb<?> homesLP = HomesLandingPageWeb.getInstance(session);
        HomesSearchResultPageWeb<?> homesSRP = HomesSearchResultPageWeb.getInstance(session);

        String location = param.get ("location");
        int star =  Integer.parseInt(param.get("star"));
        String facilityName = param.get("facilityName");

        step("User open Home Page");
        homePage.goToHomePage();

        step("User open Homes Landing Page");
        homePage.openVerticalPageViaURL("Homes");

        step("User search Homes by location");
        homesLP.searchStayLocation(location);

        step("User click submit button");
        homesLP.clickSubmit();

        step("User apply quick filters");
        homesSRP.applyQuickFilters(star);

        step("User apply advance filters");
        homesSRP.applyAdvancedFilters();
        Assertion.assertThat("Advanced filters are applied", homesSRP.isAdvancedFilterApplied(), is(true));
        Assertion.assertThat("Star rating filter are applied", homesSRP.isStarRatingFilterAppliedToHotelList(star), is(true));
        Assertion.assertThat("Facility filter are applied", homesSRP.isFacilityFilterAppliedToHotelList(), is(true));
    }

    @Author(names = "Raihan")
    @Module(name = "SearchPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyApplyQuickAdvancedFilterAndSortingNHANonLoginUser() {
        Map<String, String> param = getParam("verifyApplyQuickAdvancedFilterAndSortingNHANonLoginUser");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HomesLandingPageWeb<?> homesLP = HomesLandingPageWeb.getInstance(session);
        HomesSearchResultPageWeb<?> homesSRP = HomesSearchResultPageWeb.getInstance(session);

        String location = param.get ("location");
        int star =  Integer.parseInt(param.get("star"));

        step("User open Home Page");
        homePage.goToHomePage();

        step("User open Homes Landing Page");
        homePage.openVerticalPageViaURL("Homes");

        step("User search Homes by location");
        homesLP.searchStayLocation(location);

        step("User click submit button");
        homesLP.clickSubmit();

        step("User apply quick filters");
        homesSRP.applyQuickFilters(star);
        Assertion.assertThat("Star rating filter are applied", homesSRP.isFilterByStarRatingApplied(star), is(true));

        step("User apply advance filters");
        homesSRP.applyAdvancedFilters();
        Assertion.assertThat("Advanced filters are applied", homesSRP.isAdvancedFilterApplied(), is(true));
        Assertion.assertThat("Facility filter are applied", homesSRP.isFacilityFilterAppliedToHotelList(), is(true));
    }

    @Author(names = "Deeon")
    @Module(name = "DetailPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyNonLoginNHAChangeDetailsFromRoomList() {
        Map<String, String> param = getParam("verifyNonLoginNHAChangeDetailsFromRoomList");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        int newGuestCount = Integer.parseInt(param.get("newGuestCount"));
        int newChildCount = Integer.parseInt(param.get("newChildCount"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);
        PDPPageWeb<?> hotelPDP = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> hotelRoomListPage = HotelRoomListPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User Open Hotels Landing page");
        homePage.openVerticalPageViaURL("Homes");

        step("Search stay location");
        hotelLP.searchStayLocation(location);

        step("Submit search Button");
        hotelLP.clickSubmit();

        hotelSRP.clickHotelCard(1, false);

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", hotelPDP.isPdpLoaded(), Is.is(true));

        step("User is inside PDP and change details from room list");
        hotelPDP.seeHotelRoom();
        hotelRoomListPage.clickRoomNGuestButton();
        hotelRoomListPage.setTotalGuest(newGuestCount);
        hotelRoomListPage.setTotalChild(newChildCount);
        hotelRoomListPage.clickGuestDoneButton();
        hotelRoomListPage.clickRoomListChangeButton();
        Assertion.assertThat("Verify PDP is reloaded after changed details", hotelPDP.isPdpLoaded(), Is.is(true));

        hotelPDP.seeHotelRoom();
        Assertion.assertThat("Room guest count correctly displayed based on new details", hotelRoomListPage.checkRoomCardGuestCount(newGuestCount), Is.is(true));
        Assertion.assertThat("Room child count correctly displayed based on new details", hotelRoomListPage.checkRoomCardChildCount(newChildCount), Is.is(true));
    }

    @Author(names = "Caroline")
    @Module(name = "RoomList")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserApplyFilterInHomesRoomList() {
        Map<String, String> param = getParam("verifyUserApplyFilterInHomesRoomList");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HomesLandingPageWeb<?> homesLP = HomesLandingPageWeb.getInstance(session);
        HomesRoomListPageWeb<?> homesRoomList = HomesRoomListPageWeb.getInstance(session);

        String location = param.get ("location");

        step("User open Home Page");
        homePage.goToHomePage();

        step("User open Homes Landing Page");
        homePage.openVerticalPageViaURL("Homes");

        step("User search Homes by location");
        homesLP.searchStayLocation(location);

        step("User click Submit button");
        homesLP.clickSubmit();

        step("User click See Rooms button");
        homesRoomList.clickSeeRooms();

        step("User click filter on Room List");
        homesRoomList.clickFilter();
        String selectedFilter = homesRoomList.getSelectedFilter();

        step("Verify filter applied");
        Assertion.assertThat("Room filters are applied", homesRoomList.isFilterSelected(selectedFilter), is(true));
    }

    @Author(names = "Bikash")
    @Module(name = "LandingPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecentlySearchedNHANonLogin(){
        Map<String, String> param = getParam("verifyRecentlySearchedNHANonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HomesLandingPageWeb<?> homesLandingPage = HomesLandingPageWeb.getInstance(session);
        SRPPageWeb<?> homesSRP = SRPPageWeb.getInstance(session);

        String location = param.get("location");
        String hotelName = param.get("hotel");

        step("User open Home Page");
        homePage.openVerticalPageViaURL("Homes");

        step("User should see the Recently Searched section");
        if(homesSRP.isRecentlySearchedSectionAppeared()){
            homesSRP.clearAllRecentlySearched();
            Assertion.assertThat("Recently Searched section is cleared", homesSRP.isRecentlySearchedSectionDisappeared(), is(true));
        }
        step("User search Homes by location");
        homesLandingPage.searchStayLocation(location);

        step("User click Submit button");
        homesLandingPage.clickSubmit();

        step("User back to Homes Landing Page");
        homePage.openVerticalPageViaURL("Homes");

        step("User searches Home by Home name");
        homesLandingPage.searchStayLocation(hotelName);
        homesLandingPage.clickSubmit();

        step("User back to Homes Landing Page");
        homePage.openVerticalPageViaURL("Homes");
        
        step("User check the Recently Searched section");
        Assertion.assertThat("Recently Searched section is displayed", homesSRP.isRecentlySearchedSectionAppeared(),is(true)); 

        step("User clear the Recently Searched section");
        homesSRP.clearAllRecentlySearched();
        Assertion.assertThat("Recently Searched section is cleared", homesSRP.isRecentlySearchedSectionDisappeared(), is(true));
    }

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

    @Author(names = "Akshi")
    @Module(name = "LandingPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyNHASearchForm() {
        Map<String, String> param = getParam("verifyNHASearchForm");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HomesLandingPageWeb<?> homesLP = HomesLandingPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPAge=SRPPageWeb.getInstance(session);

        String location=param.get("location");

        step("User open Home Page");
        homePage.goToHomePage();

        step("User open Homes Landing Page");
        homePage.openVerticalPageViaURL("Homes");

        step("User search Homes by location");
        Assertion.assertThat("Search Form present",homesLP.isSearchFormPresent(),is(true));
        Assertion.assertThat("Date Form Present",homesLP.isDateFormPresent(),is(true));
        Assertion.assertThat("Guest Form Present",homesLP.isGuestFormPresent(),is(true));
        Assertion.assertThat("Submit Button Present",homesLP.isSubmitButtonPresent(),is(true));
        Assertion.assertThat("Near me Button Present",homesLP.isNearMeButtonPresent(),is(true));

        step("User click Destination Form");
        Assertion.assertThat("Destination drawer present",homesLP.isDestinationDrawerPresent(),is(true));
        homesLP.isCloseBottomDrawer();
        step("User click Date Form");
        Assertion.assertThat("Date drawer Present",homesLP.isDatePickerDrawerPresent(),is(true));
        homesLP.isCloseBottomDrawer();
        step("User Click Guest Form");
        Assertion.assertThat("Guest drawer Present",homesLP.isGuestFormDrawerPresent(),is(true));
        homesLP.isCloseBottomDrawer();

        step("Click Near ME");
        hotelLP.clickNearMeLocation();
        Assertion.assertThat("Near Me Present",homesLP.isNearMePresent(),is(true));

        step("Add location in SRP  form");
        homesLP.searchStayLocation(location);
        homesLP.clickSubmit();
        Assertion.assertThat("Check searched correct  location",searchResultPAge.checkLocation(location),is(true));
    }
    @Author(names = "Bikash")
    @Module(name = "LandingPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyNHADetailsOnMap(){
        Map<String, String> param = getParam("verifyNHADetailsOnMap");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);

        HomesLandingPageWeb<?> homesLandingPage = HomesLandingPageWeb.getInstance(session);
        SRPPageWeb<?> homesSRP = SRPPageWeb.getInstance(session);
        String location = param.get("location");

        step("User open Home Page");
        homePage.openVerticalPageViaURL("Homes");

        step("User search Homes by location");
        homesLandingPage.searchStayLocation(location);
        homesLandingPage.clickSubmit();

        step("User click on map");
        homesSRP.clickMap();

        step("click on Hotel chips");
        homesSRP.clickChip();

        step("check the hotel name of selected chip");
        Assertion.assertThat("Verify if user is able to see Hotel name",homesSRP.isHotelNameMapVisible(), is(true));
        Assertion.assertThat("Hotel's star is visible on Hotel Card",homesSRP.isHotelStarRatingMapVisible(), is(true));
        Assertion.assertThat("Hotel's price is visible on Hotel Card or not",homesSRP.isHotelPriceMapVisible(), is(true));
       }

    @Author(names = "Prabhleen")
    @Module(name = "PDP")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRoomRecommendationShownWithoutTotalRoomWhenCapacityMatches() throws FailedLoginException, InterruptedException{
        Map<String, String> param = getParam("verifyRoomRecommendationShownWithoutTotalRoomWhenCapacityMatches");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        // Page object initialization
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HomesLandingPageWeb<?> homesLP = HomesLandingPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        PDPPageWeb<?> pdpPage = PDPPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPAge=SRPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        // Extracting data from test sheet
        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String hotelName = param.get("hotelName");
        String checkIn = param.get("checkIn");
        String checkOut = param.get("checkOut");
        String adults = param.get("adults");
        String children = param.get("children");

        step("User open Home Page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User open Homes Landing Page");
        homePage.openVerticalPageViaURL("Homes");

        step("Verify Homes Landing Page is loaded");
        Assertion.assertThat("Landing page is visible", homesLP.isHeroBannerVisible(), Is.is(true));

        step("Add hotel in SRP  form");
        homesLP.searchStayLocation(hotelName);
        homesLP.clickSubmit();

        step("Verify PDP page is loaded");
        Assertion.assertThat("PDP page loaded successfully", pdpPage.isPdpLoaded(), Is.is(true));

        step("User clicks on 'See Room' button");
        pdpPage.clickSeeRoomButton();

        step("Verify recommended room section is visible on room list");
        Assertion.assertThat("Room recommendation section is visible", pdpPage.isRoomRecommendationCardVisible(), Is.is(true));

        step("Verify 'Book Room' button is visible in recommended room section");
        Assertion.assertThat("Book Room button is visible", pdpPage.isBookRoomButtonVisible(), Is.is(true));

        step("User clicks on 'Book Room' button");
        pdpPage.clickBookRoomButton();

        step("Verify room total and detail price are visible");
        Assertion.assertThat("Room total detail price is visible", pdpPage.isRoomDetailTotalRoomVisible(), Is.is(true));
    }

    @Author(names = "Akshi")
    @Module(name = "LandingPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyNHASearchFormLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyNHASearchFormLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HomesLandingPageWeb<?> homesLP = HomesLandingPageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPAge=SRPPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        String location=param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        step("User open Home Page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User open Homes Landing Page");
        homePage.openVerticalPageViaURL("Homes");

        step("User search Homes by location");
        Assertion.assertThat("Search Form present",homesLP.isSearchFormPresent(),is(true));
        Assertion.assertThat("Date Form Present",homesLP.isDateFormPresent(),is(true));
        Assertion.assertThat("Guest Form Present",homesLP.isGuestFormPresent(),is(true));
        Assertion.assertThat("Submit Button Present",homesLP.isSubmitButtonPresent(),is(true));
        Assertion.assertThat("Near me Button Present",homesLP.isNearMeButtonPresent(),is(true));

        step("User click Destination Form");
        Assertion.assertThat("Destination drawer present",homesLP.isDestinationDrawerPresent(),is(true));
        homesLP.isCloseBottomDrawer();
        step("User click Date Form");
        Assertion.assertThat("Date drawer Present",homesLP.isDatePickerDrawerPresent(),is(true));
        homesLP.isCloseBottomDrawer();
        step("User Click Guest Form");
        Assertion.assertThat("Guest drawer Present",homesLP.isGuestFormDrawerPresent(),is(true));
        homesLP.isCloseBottomDrawer();

        step("Click Near ME");
        hotelLP.clickNearMeLocation();
        Assertion.assertThat("Near Me Present",homesLP.isNearMePresent(),is(true));

        step("Add location in SRP  form");
        homesLP.searchStayLocation(location);
        homesLP.clickSubmit();
        Assertion.assertThat("Check searched correct  location",searchResultPAge.checkLocation(location),is(true));
    }

    @Author(names = "Prabhleen")
    @Module(name = "LandingPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHeroBannerLandingPage() {
        Map<String, String> param = getParam("verifyHeroBannerLandingPage");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HomesLandingPageWeb<?> homesLandingPage = HomesLandingPageWeb.getInstance(session);

        step("User opens the Homes Landing Page");
        homePage.openVerticalPageViaURL("Homes");

        step("User checks the hero banner section");
        Assertion.assertThat("Hero banner section is displayed", homesLandingPage.isHeroBannerVisible(), is(true));

        step("Verify hero banner title is visible");
        Assertion.assertThat("Hero banner title is displayed", homesLandingPage.isHeroBannerTitleVisible(), is(true));

        step("Verify hero banner text writing is visible");
        Assertion.assertThat("Hero banner text writing is displayed", homesLandingPage.isHeroBannerTextVisible(), is(true));
    }

    @Author(names = "Raihan")
    @Module(name = "LandingPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecentlySearchAndViewedNHANonLogin() {
        Map<String, String> param = getParam("verifyRecentlySearchAndViewedNHANonLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HomesLandingPageWeb<?> homesLP = HomesLandingPageWeb.getInstance(session);
        HomesRoomListPageWeb<?> homesRoomList = HomesRoomListPageWeb.getInstance(session);
        SRPPageWeb<?> searchResultPage = SRPPageWeb.getInstance(session);
        HomesSearchResultPageWeb<?> homesSearchResultPageWeb = HomesSearchResultPageMWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);

        String location = param.get ("location");
        String location2 = param.get ("location2");
        String propertyName = param.get ("propertyName");
        String pageModuleId = param.get ("pageModuleId");

        step("User open Home Page");
        homePage.goToHomePage();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User open Homes Landing Page");
        homePage.openVerticalPageViaURL("Homes");

        step("Check recent search from page module to SRP");
        homesLP.clickPageModulePrimaryButton(pageModuleId);
        Assertion.assertThat("Srp is Loaded",searchResultPage.isHotelCardVisible(), Is.is(true));
        String srpLocation = homesSearchResultPageWeb.getLocationHeaderLabel();
        String srpDate = homesSearchResultPageWeb.getDateHeaderLabel();
        String srpGuest = homesSearchResultPageWeb.getGuestHeaderLabel();
        homesSearchResultPageWeb.clickBack();
        Assertion.assertThat("Recently searched is showing correct destination", homesLP.isSearchHistoryDestinationLabelDisplayed(srpLocation, 1), is(true));
        Assertion.assertThat("Recently searched is showing correct no of guest", homesLP.isSearchHistoryGuestLabelDisplayed(srpGuest, 1), is(true));
        homesLP.clearAllRecentList();

        step("Check recent search from page module to PDP");
        homesLP.clickPageModuleProductCardByName(pageModuleId, propertyName);
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        String pdpPropertyName = homesRoomList.getPropertyNameLabel();
        homesSearchResultPageWeb.clickBack();
        Assertion.assertThat("Recently searched is showing correct destination", homesLP.isSearchHistoryDestinationLabelDisplayed(pdpPropertyName, 1), is(true));
        homesLP.clearAllRecentList();

        step("Check recent search from search location on landing page");
        homesLP.searchStayLocation(location);
        homesLP.clickSubmit();
        Assertion.assertThat("Srp is Loaded",searchResultPage.isHotelCardVisible(), Is.is(true));
        homesSearchResultPageWeb.clickBack();
        Assertion.assertThat("Recently searched is showing correct destination", homesLP.isSearchHistoryDestinationLabelDisplayed(location, 1), is(true));
        homesLP.clearAllRecentList();

        step("Check recent search from search property name on landing page");
        homesLP.searchStayLocation(propertyName);
        homesLP.clickSubmit();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        homesSearchResultPageWeb.clickBack();
        Assertion.assertThat("Recently searched is showing correct destination", homesLP.isSearchHistoryDestinationLabelDisplayed(propertyName, 1), is(true));
        homesLP.clearAllRecentList();

        step("Check recent search from search location on SRP");
        homesLP.searchStayLocation(location);
        homesLP.clickSubmit();
        Assertion.assertThat("Srp is Loaded",searchResultPage.isHotelCardVisible(), Is.is(true));
        homesSearchResultPageWeb.updateStayLocation(location2);
        homesSearchResultPageWeb.clickSearchOnSRP();
        Assertion.assertThat("Srp is Loaded",searchResultPage.isHotelCardVisible(), Is.is(true));
        homesSearchResultPageWeb.clickBack();
        homesSearchResultPageWeb.clickBack();
        Assertion.assertThat("Recently searched is showing correct destination", homesLP.isSearchHistoryDestinationLabelDisplayed(location2, 1), is(true));
        homesLP.clearAllRecentList();

        step("Check recent search from search property name on SRP");
        homesLP.searchStayLocation(location);
        homesLP.clickSubmit();
        Assertion.assertThat("Srp is Loaded",searchResultPage.isHotelCardVisible(), Is.is(true));
        homesSearchResultPageWeb.updateStayLocation(propertyName);
        homesSearchResultPageWeb.clickSearchOnSRP();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));
        homesSearchResultPageWeb.clickBack();
        homesSearchResultPageWeb.clickBack();
        Assertion.assertThat("Recently searched is showing correct destination", homesLP.isSearchHistoryDestinationLabelDisplayed(propertyName, 1), is(true));
        homesLP.clearAllRecentList();
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "Booking")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingMoWithAddOnsNonLoginHome() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("completeBookingMoWithAddOnsNonLoginHome");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String fullName1 = CoreFunctions.randomName();
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String phoneNumber = param.get("phoneNumber");
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
        ETicketWeb<?> eTicketPage = ETicketWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.goToHomePage();
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open NHA Landing page");
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

        step("Update room and guest");
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalRoom(noOfRooms);
        roomListPage.setTotalGuest(guests);
        roomListPage.clickGuestDoneButton();

        step("Click Change Room List button");
        roomListPage.clickRoomListChangeButton();

        step("Select first room");
        roomListPage.clickBookButton();

        step("Select second room with different rate plan");
        roomListPage.clickBookButton();

        step("Click Multi-Book button");
        roomListPage.clickMultiBookButton();

        step("Fill contact detail");
        bookingFormPage.fillContactDetail(fullName1, phoneNumber, email);

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDetailsCombinedRandom(2);
        int bookingPrice = bookingFormPage.getTotalPrice();

        step("Special request");
        String specialRequest = "Need extra pillows";
        bookingFormPage.enterSpecialRequestMO(specialRequest, noOfRooms);
        Assertion.assertThat("Verify if Special Request is added successfully", bookingFormPage.getSpecialReqText(), containsString(specialRequest));
        bookingFormPage.continueToNextPage();

        step("User select addOns and insurance");
        HashMap<String,String> addOnDetails = bookingFormPage.chooseAddOnsMultiRoom(noOfRooms);

        step("Verify total price is correct with add ons");
        int finalPrice = bookingFormPage.getTotalPrice();
        Assertion.assertThat("Final price should be greater than booking price after selecting add ons", finalPrice, greaterThan(bookingPrice));
        Assertion.assertThat("Room details and addon are there in Price break down ", bookingFormPage.isRoomAndAddOnPresent(addOnDetails) , is(true));
        bookingFormPage.continueToPaymentPage();

        step("Go to payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();
        paymentPage.clickSeeOrderListButton();

        step("Login to open the order list page");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User open order detail page");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));

        step("Verify Special request and AddOn in order detail page");
        Assertion.assertThat("Special request should be visible in order detail page", orderDetailPage.getSpecialReq(), containsString(specialRequest));
        String addOnText = orderDetailPage.getAddOnText();
        Assertion.assertThat("Add-ons should be visible in order detail page", !addOnText.isEmpty(), is(true));

        step("Verify Special request and AddOn in E-Ticket");
        orderDetailPage.openETicket();
        String pdfContent;
        if (Configs.PLATFORM == Platform.DWEB) {
            String url = orderDetailPage.switchTabAndgetURL();
            pdfContent = eTicketPage.pdfContent(url);
        } else {
            pdfContent = orderDetailPage.getPDFContentFile();
        }
        Assertion.assertThat("Special request should be visible in E-Ticket", pdfContent, containsString(specialRequest));
        Assertion.assertThat("Add-ons should be visible in E-Ticket", pdfContent, containsString(addOnText));
    }

    @Author(names = "Aditi")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserPriceCombinationInSRP() {
        Map<String, String> param = getParam("verifyUserPriceCombinationInSRP");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> hotelLP = HotelLandingPageWeb.getInstance(session);
        HomesLandingPageWeb<?> homesLP = HomesLandingPageWeb.getInstance(session);
        SRPPageWeb<?> hotelSRP = SRPPageWeb.getInstance(session);
        HomesSearchResultPageWeb<?> homesSearchResultPageWeb = HomesSearchResultPageMWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        String location = param.get("location");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        step("User open Home Page");
        homePage.goToHomePage();
        String currency = homePage.getCurrentCurrency();

        step("User open Homes Landing Page");
        homePage.openVerticalPageViaURL("Homes");
        hotelLP.closeContinueOnWebPopup();

        step("User search hotel by location");
        homesLP.searchStayLocation(location);

        step("Set date");
        hotelLP.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("User hit search button");
        homesLP.clickSubmit();
        Assertion.assertThat("SRP is Loaded", hotelSRP.isHotelCardVisible(), is(true));

        step("Calculate the number of days between check-in and check-out");
        int duration = homesSearchResultPageWeb.getNumberOfDays();

        step("Get accomodation price before applying filter");
        int priceBeforeFilter = homesSearchResultPageWeb.getFirstNHAPrice(currency);

        step("User open filters");
        hotelSRP.openFilterMenu();

        step("Select total price toggle");
        homesSearchResultPageWeb.selectTotalPriceToggle();

        step("Save filter");
        hotelSRP.saveFilters();

        step("Get accomodation price after applying filter");
        int priceAfterFilter = homesSearchResultPageWeb.getFirstNHAPrice(currency);

        step("Verify price combination based on booking duration");
        homesSearchResultPageWeb.verifyPriceCombination(priceBeforeFilter, priceAfterFilter, duration);
    }

    @Author(names = "Mridul Gupta")
    @Module(name = "DetailPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecommendedRoomLoginCapacityLessThanSearchCriteriaHomes() throws FailedLoginException, InterruptedException{
        Map<String, String> param = getParam("verifyRecommendedRoomLoginCapacityLessThanSearchCriteriaHomes");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");
        int guests = Integer.parseInt(param.get("guests"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User Open Homes Landing page");
        homePage.openVerticalPageViaURL("Homes");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(guests);
        landingPage.clickDone();

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel's Price is visible on Hotel PDP", !detailPage.getHotelPriceDetail().isEmpty(), is(true));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Checking Recommended Room");
        Assertion.assertThat("Recommended room should be visible on room list", roomListPage.isRecommendedRoomMWEBVisible(), is(true));
        Assertion.assertThat("On Recommended room, total-room-night-summary should be visible", roomListPage.isTotalRoomNightRecommended(), is(true));
        Assertion.assertThat("On Recommended room, total price should be visible", roomListPage.isRecommendedTotalPrice(), is(true));
        Assertion.assertThat("On Recommended room, total room should be visible", roomListPage.isRecommendedTotalRoomIcon(), is(true));
        Assertion.assertThat("On Recommended room, total room should be 2", roomListPage.getRecommendedTotalRoom(), is("2"));
        Assertion.assertThat("On Recommended room, total room should be Book 2 Rooms", roomListPage.getRecommendedTotalRoomBookButtonRoomList(), either(containsString("Book 2 Rooms")).or(containsString("Pesan 2 Kamar")));

        step("Select room recommendation Card ");
        roomListPage.selectRoomRecommendationCard();

        Assertion.assertThat("On Recommended room, total room should be Book 2 Rooms", roomListPage.getRecommendedTotalRoomBookButtonRoomDetail(), either(containsString("Book 2 Rooms")).or(containsString("Pesan 2 Kamar")));

    }

    @Author(names = "Aditi")
    @Module(name = "BookingPage")
    @Tribe(name = "Homes")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingNHAGuest() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("completeBookingNHAGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        String fullName = CoreFunctions.randomName();
        String phoneNumber = CoreFunctions.randomPhone();
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String currency = param.get("currency");
        String filterChip = param.get("filterChip");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PDPPageWeb<?> pdpPage = PDPPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        ETicketWeb<?> eTicketPage = ETicketWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open home page as guest");
        loginPage.loginAsGuest();

        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User Open NHA Landing page");
        homePage.openVerticalPageViaURL("Homes");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateType, dateRange);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", pdpPage.isPdpLoaded(), is(true));
        Assertion.assertThat("NHA name on the detail page should contain the location", pdpPage.getHotelNameDetail(), containsString(location));

        step("Verify Main Image hotel is visible");
        Assertion.assertThat("Main hotel image is visible on PDP", pdpPage.isHotelImageVisible(), is(true));

        step("User clicks on hotel image to open gallery");
        pdpPage.clickHotelImage();

        step("Verify gallery is opened with title and images");
        Assertion.assertThat("Gallery is opened after clicking on hotel image", pdpPage.isGalleryOpened(), is(true));
        Assertion.assertThat("Gallery title is visible", pdpPage.isGalleryTitleVisible(), is(true));
        Assertion.assertThat("Gallery images are visible", pdpPage.isGalleryImagesVisible(), is(true));

        step("User closes the gallery by clicking cross button");
        pdpPage.closeGallery();

        step("Verify gallery is closed");
        Assertion.assertThat("Gallery is closed after clicking cross button", pdpPage.isGalleryClosed(), is(true));

        step("Verify NHA Star rating is visible");
        Assertion.assertThat("NHA Star rating is visible on PDP", pdpPage.isStarRatingVisible(), is(true));

        step("Verify NHA Property type (category) is visible");
        Assertion.assertThat("NHA Property type (Villa/Apartment etc.) is visible on PDP", pdpPage.isPropertyTypeVisible(), is(true));

        step("Verify NHA Location is visible");
        Assertion.assertThat("NHA Location is visible on PDP", pdpPage.isHotelLocationVisible(), is(true));

        step("Verify Hotel review by Tiket is visible");
        pdpPage.scrollToReviewSection();
        Assertion.assertThat("Hotel review section is visible on PDP", pdpPage.isHotelReviewVisible(), is(true));

        step("Verify Popular Facilities is visible");
        Assertion.assertThat("Popular Facilities section is visible on PDP", pdpPage.isHotelFacilitiesVisible(), is(true));

        step("Verify Location or Maps is visible");
        Assertion.assertThat("Location/Maps section is visible on PDP", pdpPage.isPdpMapVisible(), is(true));

        step("Verify Accommodation Rules/About is visible");
        Assertion.assertThat("Accommodation Rules/About section is visible on PDP", pdpPage.isAccommodationPoliciesVisible(), is(true));

        step("Verify Hotel price is visible on PDP");
        Assertion.assertThat("Hotel price is visible on PDP", pdpPage.isHotelPriceVisible(), is(true));

        step("Verify See Rooms button is visible");
        Assertion.assertThat("See Rooms button is visible on PDP", pdpPage.isSeeRoomVisible(), is(true));

        step("Click on See Rooms button");
        pdpPage.clickSeeRoomsButton();

        step("Verify room list filter chips are visible");
        Assertion.assertThat("Room list filter chips are visible", pdpPage.isRoomListFilterChipsVisible(), is(true));

        step("Click on a filter chip");
        pdpPage.clickRoomListFilterChip(filterChip);

        step("Verify rooms have loaded after selecting the filter chip");
        Assertion.assertThat("Room recommendation card is visible after applying filter", pdpPage.isRoomRecommendationCardVisible(), is(true));

        step("Fetch room name from card");
        String roomNameFromCard = pdpPage.getRoomNameFromCard();
        Assertion.assertThat("Room name should not be empty", roomNameFromCard.isEmpty(), is(false));

        step("Click on room inner card chevron to open room detail page");
        pdpPage.clickRoomInnerCardChevron();

        step("Verify Room Detail page is opened");
        Assertion.assertThat("Room detail page is opened", pdpPage.isRoomDetailPageOpened(), is(true));

        step("Verify Room Name is visible on room detail page");
        Assertion.assertThat("Room name is visible on room detail page", pdpPage.isRoomDetailNameVisible(), is(true));

        step("Verify Room Name matches the room that was clicked from PDP");
        String roomNameOnDetailPage = pdpPage.getRoomDetailNames();
        Assertion.assertThat("Room name on detail page should match room name from card", roomNameOnDetailPage, is(roomNameFromCard));

        step("Verify Room Size information is visible on room detail page");
        Assertion.assertThat("Room size information is visible on room detail page", pdpPage.isRoomDetailSizeVisible(), is(true));

        step("Verify Bed Type is visible on room detail page");
        Assertion.assertThat("Bed type is visible on room detail page", pdpPage.isRoomDetailBedTypeVisible(), is(true));

        step("Verify Check-in Procedure is visible on room detail page");
        Assertion.assertThat("Check-in procedure is visible on room detail page", pdpPage.isRoomDetailCheckinProcedureVisible(), is(true));

        step("Click on price detail chevron to open price breakdown");
        pdpPage.clickPriceDetailChevron();

        step("Verify Price Details page is opened");
        Assertion.assertThat("Price details page is opened", pdpPage.isPriceDetailsPageOpened(), is(true));
        Assertion.assertThat("Price details title is visible", pdpPage.isPriceDetailsTitleVisible(), is(true));

        step("Validate price breakdown calculation");
        pdpPage.validatePriceBreakdownCalculation(currency);

        step("Click on Book button in price details page");
        pdpPage.clickPriceDetailsBookButton();

        step("Verify Booking page is opened");
        String bookingPageRoomName = bookingFormPage.getBookingRoomName();
        Assertion.assertThat("Room name on booking page should match room name from card", bookingPageRoomName, is(roomNameFromCard));

        step("Click on Stay Details chevron");
        bookingFormPage.clickStayDetailsChevron();

        step("Verify Stay Details bottom sheet is opened");
        Assertion.assertThat("Stay Details bottom sheet is opened", bookingFormPage.isStayDetailsBottomSheetOpened(), is(true));

        step("Verify Hotel name is visible in Stay Details");
        Assertion.assertThat("Hotel name is visible in Stay Details", bookingFormPage.isStayDetailsHotelNameVisible(), is(true));
        String stayDetailsHotelName = bookingFormPage.getStayDetailsHotelName();
        Assertion.assertThat("Hotel name in Stay Details should contain location", stayDetailsHotelName, containsString(location));

        step("Verify Room name is visible in Stay Details");
        Assertion.assertThat("Room name is visible in Stay Details", bookingFormPage.isStayDetailsRoomNameVisible(), is(true));

        step("Verify Room count is visible in Stay Details");
        Assertion.assertThat("Room count is visible in Stay Details", bookingFormPage.isStayDetailsRoomCountVisible(), is(true));

        step("Verify Night count is visible in Stay Details");
        Assertion.assertThat("Night count is visible in Stay Details", bookingFormPage.isStayDetailsNightCountVisible(), is(true));

        step("Verify Guest count is visible in Stay Details");
        Assertion.assertThat("Guest count is visible in Stay Details", bookingFormPage.isStayDetailsGuestCountVisible(), is(true));

        step("Verify Cancellation policy is visible in Stay Details");
        Assertion.assertThat("Cancellation policy is visible in Stay Details", bookingFormPage.isStayDetailsCancellationPolicyVisible(), is(true));

        step("Close the Stay Details bottom sheet by clicking cross button");
        bookingFormPage.closeBottomSheet();

        step("Verify bottom sheet is closed");
        Assertion.assertThat("Stay Details bottom sheet is closed", bookingFormPage.isBottomSheetClosed(), is(true));

        step("Fill contact detail");
        bookingFormPage.fillContactDetail(fullName, phoneNumber, email);

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDetailsCombinedRandom(1);

        step("Click on Special Request button");
        bookingFormPage.clickSpecialRequestFormButton();

        step("Verify Special Request form is opened");
        Assertion.assertThat("Special Request form is opened", bookingFormPage.isSpecialRequestFormOpened(), is(true));

        step("Click on special request chips");
        bookingFormPage.clickFirstSpecialRequestChip();
        bookingFormPage.clickSecondSpecialRequestChip();

        step("Fill other requests in the text area");
        String otherRequestsText = "Need extra pillows";
        bookingFormPage.fillOtherRequestsTextarea(otherRequestsText);

        step("Click on Save button to save special requests");
        bookingFormPage.clickSpecialRequestSaveFormButton();
        Assertion.assertThat("Verify if Special Request is added successfully", bookingFormPage.getSpecialReqText(), containsString(otherRequestsText));

        step("Click on Continue button");
        bookingFormPage.clickContinueButton();

        step("Verify Price Summary is visible on booking form");
        Assertion.assertThat("Price Summary is visible on booking form", bookingFormPage.isPriceSummaryVisible(), is(true));

        step("Validate price breakdown calculation on booking form");
        bookingFormPage.validatePriceSummaryCalculation(currency);

        step("Continue to payment page");
        bookingFormPage.selectPayment();

        step("Go to payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();
        paymentPage.clickSeeOrderListButton();

        step("Login to open the order list page");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User open order detail page");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));

        step("Verify Special request in order detail page");
        Assertion.assertThat("Special request should be visible in order detail page", orderDetailPage.getSpecialReq(), containsString(otherRequestsText));
    }

    @Author(names = "Alden, Aditi")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingHomesEmailSMSVerification() throws Exception
    {
        Map<String, String> param = getParam("completeBookingHomesEmailSMSVerification");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String home = param.get("hotel");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("Open the homepage");
        homePage.goToHomePage();

        step("Log in using an existing user account");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("Go to the Homes section and search for the target property");
        homePage.selectCurrency("IDR");
        homePage.openVerticalPageViaURL("Homes");

        step("Submit the search request");
        landingPage.searchStayLocation(home);
        landingPage.clickSubmit();

        step("Access the Property Detail Page (PDP)");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));

        step("Select 'See Rooms' to view available rooms");
        detailPage.seeHotelRoom();

        step("Select 'Book' to proceed with booking the room");
        roomListPage.clickBookButton();

        step("Enter the Booking Form page and verify property name");
        Assertion.assertThat("Property name matches", bookingFormPage.getBookingHotelName(), Is.is(home));

        step("Continue to the payment page");
        bookingFormPage.clickContinueButton();
        bookingFormPage.selectPayment();

        step("Fetch the order ID from the payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed on the payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Finish the payment process by clicking the payment button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("View your orders by opening the order list");
        paymentPage.clickSeeOrderListButton();

        step("Navigate to the details page of the issued e-ticket order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), Is.is(true));
        Assertion.assertThat("Contact name should be available on the order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), Is.is(true));
        Assertion.assertThat("Email should be correctly displayed on the order detail page", orderDetailPage.getContactEmail(orderID), Is.is(email));
        Assertion.assertThat("Order ID should be visible and match the ID from the payment page", orderDetailPage.getOrderId(), Is.is(orderID));

        step("Check the status of the order");
        Assertion.assertThat("New order status is correct", orderDetailPage.getOrderStatus(orderID), is("ETICKET_ISSUED"));

        step("Click on Receipt button");
        orderDetailPage.clickReceiptButton();
        orderDetailPage.openReceipt();

        step("Verify orderID on receipt");
        orderDetailPage.verifyOrderIDOnReceipt(orderID);

        step("check user received notification on email");
        OutboundNotificationVerifier.verify(email, orderID, NotificationChannel.EMAIL);
    }

    @Author(names = "Alden, Aditi")
    @Module(name = "CompleteBooking")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingHomesEmailSMSVerificationGuest() throws Exception
    {
        Map<String, String> param = getParam("completeBookingHomesEmailSMSVerificationGuest");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String home = param.get("hotel");
        String email = param.get("email");
        String phone = CoreFunctions.randomPhone();
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String password = param.get("password");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LandingPageWeb<?> landingPage = LandingPageWeb.getInstance(session);
        PDPPageWeb<?> detailPage = PDPPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("Open the homepage");
        homePage.goToHomePage();
        
        step("Go to the Homes section and search for the target property");
        homePage.selectCurrency("IDR");
        homePage.openVerticalPageViaURL("Homes");

        step("Submit the search request");
        landingPage.searchStayLocation(home);
        landingPage.clickSubmit();

        step("Access the Property Detail Page (PDP)");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), Is.is(true));

        step("Select 'See Rooms' to view available rooms");
        detailPage.seeHotelRoom();

        step("Select 'Book' to proceed with booking the room");
        roomListPage.clickBookButton();

        step("Enter the Booking Form page and verify property name");
        Assertion.assertThat("Property name matches", bookingFormPage.getBookingHotelName(), Is.is(home));

        step("Fill in guest details in booking form");
        bookingFormPage.fillContactDetail(randomName(), phone, email);
        bookingFormPage.fillGuestDetails(1, randomName(), true, false, "");

        step("Continue to the payment page");
        bookingFormPage.clickContinueButton();
        bookingFormPage.selectPayment();

        step("Fetch the order ID from the payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed on the payment page", !orderID.isEmpty(), Is.is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.clickOnSeeAll();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Finish the payment process by clicking the payment button");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        Assertion.assertThat("See order list button appeared", paymentPage.isSeeOrderListButtonDisplayed(), is(true));

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderListPage.loginFromOrderDetailPage(email, password, otp, retryCounter);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(),
                is(true));
        Assertion.assertThat("orderID should be visible and should be same as from payment page",
                orderDetailPage.getOrderId(), is(orderID));

        step("Check the status of the order");
        Assertion.assertThat("New order status is correct", orderDetailPage.getOrderStatus(orderID), is("ETICKET_ISSUED"));

        step("Click on Receipt button");
        orderDetailPage.clickReceiptButton();
        orderDetailPage.openReceipt();

        step("Verify orderID on receipt");
        if (Configs.PLATFORM == Platform.IMWEB){
            String pdfContent = orderDetailPage.getPDFContentFile();
            // Remove all whitespace/newlines from PDF content to handle split text
            String normalizedPdfContent = pdfContent.replaceAll("\\s+", "");
            Assertion.assertThat("Receipt Order ID should match expected", normalizedPdfContent, containsString(orderID));

        } else if (Configs.PLATFORM == Platform.AMWEB)
        {
            orderDetailPage.verifyOrderIDOnReceipt(orderID);
        }

        step("check user received notification on email");
        OutboundNotificationVerifier.verify(email, orderID, NotificationChannel.EMAIL);

    }

    @Author(names = "Aditi")
    @Module(name = "BookingPage")
    @Tribe(name = "Home")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySingleRatePlanMultipleRoomsExtranet() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifySingleRatePlanMultipleRoomsExtranet");
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
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);
        int hotelId = Integer.parseInt(param.get("hotelId"));
        String dashboardUsername = param.get("dashboardUsername");
        String dashboardPassword = param.get("dashboardPassword");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelRoomListPage<?> roomListPages = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        ExtranetHotelApi<?> extranetHotelApi = new ExtranetHotelApi<>(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Home Landing page");
        homePage.openVerticalPageViaURL("Homes");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel name on the search and detail page should be equal", detailPage.getHotelNameDetail(), containsString(location));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("User update number of room & guest on roomList");
        roomListPage.updateRoomAndGuest(guests, noOfRooms);

        step("Select Multiple Room");
        roomListPages.secondBookRoom();
        roomListPages.firstBookRoom();
        roomListPage.clickMultiBook();

        step("User enter Stay details ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page", bookingFormPage.isGuestStayForm(), is(true));
        bookingFormPage.fillGuestDetails(guests, fullName, true, false, "");

        step("Go to payment page");
        bookingFormPage.continueToNextPage();
        int bookingPrice = bookingFormPage.getTotalPrice();
        bookingFormPage.selectPayment();

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
        Assertion.assertThat("No of Rooms should be correct", orderDetailPage.getRoomTypeCount(), is(noOfRooms));

        step("Verify multi-room booking exists on hotel dashboard");
        String departureDateISO = CoreFunctions.convertDateFormat(departureDate);
        String returnDateISO = CoreFunctions.convertDateFormat(returnDate);

        ExtranetHotelAuthApi<?> authApi = new ExtranetHotelAuthApi<>(session);
        ExtranetAdminLoginMfaApplyResponse.Data authData = authApi.getAccessToken(dashboardUsername, dashboardPassword);
        String accessToken = authData.accessToken;
        Long dashboardAccountId = authData.accountId;

        ExtranetHotelApi.MultiRoomBookingDetails multiRoomBookingDetails = extranetHotelApi.getMultiRoomBookingDetails(
                hotelId, orderID, departureDateISO, returnDateISO, dashboardUsername, accessToken, dashboardAccountId);

        Assertion.assertThat("Multi-room booking should be found on dashboard", multiRoomBookingDetails.found(), is(true));
        Assertion.assertThat("Number of room entries should match noOfRooms", multiRoomBookingDetails.totalBookingEntries(), is(noOfRooms));

        int expectedOrderId = Integer.parseInt(orderID);
        Assertion.assertThat("All room entries should have same orderId as payment page",
                multiRoomBookingDetails.allEntriesHaveSameOrderId(expectedOrderId), is(true));

        // Validate each room entry individually
        for (int i = 0; i < multiRoomBookingDetails.totalBookingEntries(); i++) {
            ExtranetHotelApi.RoomBookingEntry roomEntry = multiRoomBookingDetails.getRoomEntry(i);

            step("Validating Room Entry " + (i + 1) + " (Itinerary ID: " + roomEntry.itineraryId() + ")");

            Assertion.assertThat("Room " + (i + 1) + " - Order ID should match",
                    String.valueOf(roomEntry.orderId()), is(orderID));
            Assertion.assertThat("Room " + (i + 1) + " - Status should be 'issued'",
                    roomEntry.status(), is("issued"));
            Assertion.assertThat("Room " + (i + 1) + " - Check-in date should match",
                    roomEntry.checkinDate(), is(departureDateISO));
            Assertion.assertThat("Room " + (i + 1) + " - Check-out date should match",
                    roomEntry.checkoutDate(), is(returnDateISO));
            Assertion.assertThat("Room " + (i + 1) + " - Duration should match",
                    roomEntry.duration(), is(daysToReturnDate));
            Assertion.assertThat("Room " + (i + 1) + " - Adult count should be valid",
                    roomEntry.adult(), greaterThan(0));
            Assertion.assertThat("Room " + (i + 1) + " - Total room should be 1",
                    roomEntry.totalRoom(), is(1));
            Assertion.assertThat("Room " + (i + 1) + " - Currency should be: IDR",
                    roomEntry.currency(), is("IDR"));
            Assertion.assertThat("Room " + (i + 1) + " - Guest should not be empty",
                    roomEntry.guests().isEmpty(), is(false));
            Assertion.assertThat("Room " + (i + 1) + " - Room name should not be null",
                    roomEntry.roomName(), notNullValue());
        }

        Assertion.assertThat("Total rooms across all entries should match noOfRooms",
                multiRoomBookingDetails.totalRooms(), is(noOfRooms));
        Assertion.assertThat("Total guests should match expected",
                multiRoomBookingDetails.totalAdults(), is(guests));

    }
}
