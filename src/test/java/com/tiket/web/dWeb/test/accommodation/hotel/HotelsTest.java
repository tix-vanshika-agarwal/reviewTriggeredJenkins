package com.tiket.web.dWeb.test.accommodation.hotel;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.apiPage.HotelApi;
import com.tiket.apiPage.ExtranetHotelAuthApi;
import com.tiket.pojo.accommodation.hotel.HotelAutoCompleteResponse;
import com.tiket.pojo.accommodation.hotel.payathotel.PayAtHotelRoom;
import com.tiket.pojo.room.SelectRoomResponse;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.dashboard.page.hotel.extranet.Navbar;
import com.tiket.web.dashboard.page.hotel.extranet.SideMenu;
import com.tiket.web.dashboard.page.hotel.extranet.PropertyPage;
import com.tiket.web.dashboard.page.hotel.extranet.ExtranetPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.ApproveActivationPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.LoginPage;
import com.tiket.web.dashboard.page.hotel.demand.FailedCheckinDashboardPage;
import com.tiket.web.dashboard.page.hotel.demand.FailedIssuedDashboardPage;
import com.tiket.web.dashboard.page.hotel.demand.SearchBookingDashboardPage;
import com.tiket.web.dashboard.page.hotel.demand.ViewOrderDashboardPage;
import com.tiket.web.page.accommodation.hotels.roomDetailsPage.RoomDetailsPageWeb;
import com.tiket.web.dWeb.page.accommodation.hotels.*;
import com.tiket.web.page.accommodation.hotels.SRP.HotelSearchResultPage;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageDWeb;
import com.tiket.web.page.accommodation.hotels.hotelRoomListPage.HotelRoomListPageWeb;
import com.tiket.web.page.accommodation.hotels.reschedulePage.HotelReschedulePageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailDweb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import com.tiket.web.page.accommodation.hotels.hotelLandingPage.HotelLandingPageWeb;
import com.tiket.web.page.common.calendar.CalendarPageWeb;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.platform.sphinx.refund.RefundDetailWeb;
import com.tiket.web.page.platform.sphinx.refund.RefundPageWeb;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.wishlist.WishlistLandingWeb;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPage;

public class HotelsTest extends BaseTest {

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySameHotelFromSrpToPdpLoginWithEmail() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySameHotelFromSrpToPdpLoginWithEmail");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.setStayDate(dateRange, dateType);

        step("submit search Button");
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency.equals("IDR"), is(true));

        String expectedHotelName = searchResultPage.getHotelName();
        String[] expectedLocation = searchResultPage.getHotelLocation();

        step("Select hotel Card ");
        searchResultPage.selectHotelCard();

        step("Go Inside PDP");
        detailPage.switchToNewTab();

        Assertion.assertThat("Hotel name on the search page and detail page should be equal", detailPage.getHotelNameDetail(), is(expectedHotelName));
        Assertion.assertThat("Hotel Area on the search page and detail page should be equal", detailPage.getHotelAreaDetail(), is(expectedLocation[0].trim()));
        Assertion.assertThat("Hotel City on the search page and detail page should be equal", detailPage.getHotelCityDetail(), is(expectedLocation[1].trim()));
        Assertion.assertThat("Hotel Region on the search page and detail page should be equal", detailPage.getHotelRegionDetail(), is(location));
        if (detailPage.isRoomsAvailable()){
            Assertion.assertThat("Hotel Price on the Detail page is visible", !detailPage.getHotelPriceDetail().isEmpty(), is(true));
        }else{
            Assertion.assertThat("If  room is fully booked respective text is visible on Hotel PDP", detailPage.getBookingFullDetail(), containsString("fully booked"));
        }
    }

    @Author(names = "Harshit")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyDetailsInSRPIsDisplayedLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyDetailsInSRPIsDisplayedLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency.equals("IDR"), is(true));

        String expectedHotelName = searchResultPage.getHotelName();
        String[] expectedLocation= searchResultPage.getHotelLocation();
        String expectedPrice= searchResultPage.getHotelPrice();
        String SearchBarLocation=searchResultPage.getLocationNameFromInput();

        Assertion.assertThat("Hotel Card on SRP is visible ", searchResultPage.isHotelCardVisible(), is(true));
        Assertion.assertThat("Hotel's Name is visible on Hotel Card", !expectedHotelName.isEmpty(), is(true));
        Assertion.assertThat("Hotel's Area is visible on Hotel Card", !expectedLocation[0].trim().isEmpty(), is(true));
        Assertion.assertThat("Hotel's City is visible on Hotel Card", !expectedLocation[1].trim().isEmpty(), is(true));
        Assertion.assertThat("Hotel's Price is visible on Hotel Card", !expectedPrice.isEmpty(), is(true));
        Assertion.assertThat("Hotel's Benefits is visible on Hotel Card", searchResultPage.isHotelBenefitsVisible(), is(true));
        Assertion.assertThat("Hotel's Image is visible on Hotel Card", searchResultPage.isHotelImageVisible(), is(true));
        Assertion.assertThat("MAP is visible on SRP", searchResultPage.isMapVisible(), is(true));
        Assertion.assertThat("Searched Text entered by user is matching on SRP's searchbar", location, is(SearchBarLocation));
    }

    @Author(names = "Harshit")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyDetailsInSRPIsChangedLogin() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("verifyDetailsInSRPIsChangedLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String newLocation = param.get("newLocation");
        int noOfAdults = Integer.parseInt(param.get("Adult"));
        int noOfRoom = Integer.parseInt(param.get("Room"));

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User opens home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User selects IDR currency");
        homePage.selectCurrency("IDR");

        step("User opens hotel landing page");
        homePage.openVerticalPageViaURL("Hotel");

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
        searchResultPage.setTotalRoom(noOfRoom);
        searchResultPage.clickDone();

        step("Set date");
        searchResultPage.setStayDate("range date", "10-15");

        step("Click search button to update SRP results");
        searchResultPage.clickSearchOnSRP();

        step("SRP is refreshed");

        String SearchBarLocation = searchResultPage.getLocationNameFromInput();
        String RoomAndGuestDetails = searchResultPage.getRoomAndGuestInfoFromInput();

        Assertion.assertThat("Hotel card on SRP is visible", searchResultPage.isHotelCardVisible(), is(true));
        Assertion.assertThat("Searched text entered by user matches SRP's search bar", newLocation, is(SearchBarLocation));

        String[] roomGuestDetailsArray = RoomAndGuestDetails.split(", ");

        int roomsFromSRP = Integer.parseInt(roomGuestDetailsArray[0].split(" ")[0]);
        int adultsFromSRP = Integer.parseInt(roomGuestDetailsArray[1].split(" ")[0]);

        Assertion.assertThat("Number of updated rooms should match on SRP",roomsFromSRP,is(noOfRoom));
        Assertion.assertThat("Number of updated adults should match on SRP", adultsFromSRP, is(noOfAdults));
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void VerifyHotelInfoAndInteractionInPDPLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("VerifyHotelInfoAndInteractionInPDPLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);
        int noOfAdults = Integer.parseInt(param.get("Adult"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);
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

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(noOfAdults);
        landingPage.clickDone();

        step("Set date");
        landingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        Assertion.assertThat("Verify User is redirected to PDP",detailPage.isPdpLoaded(),is(true));
        Assertion.assertThat("Hotel's Image is visible on Hotel Detail Page", detailPage.isHotelImageVisible(), is(true));
        Assertion.assertThat("Hotel name on the  detail page should be visible", !detailPage.getHotelNameDetail().isEmpty(), is(true));
        Assertion.assertThat("Hotel's Area is visible on Hotel PDP", !detailPage.getHotelAreaDetail().isEmpty(), is(true));
        Assertion.assertThat("Hotel's City is visible on Hotel PDP", !detailPage.getHotelCityDetail().isEmpty(), is(true));
        Assertion.assertThat("Highlights Section is visible on Hotel PDP", detailPage.isHighLightsVisible(), is(true));
        Assertion.assertThat("Popular Facilities Section is visible on Hotel PDP", detailPage.isPopularFacilitiesVisible(), is(true));
        Assertion.assertThat("Review Section is visible on Hotel PDP", detailPage.isReviewsVisible(), is(true));
        Assertion.assertThat("Map Section is visible on Hotel PDP", detailPage.isPdpMapVisible(), is(true));
        Assertion.assertThat("Accommodation Policies Section is visible on Hotel PDP", detailPage.isAccommodationPoliciesVisible(), is(true));
        Assertion.assertThat("FAQs Section is visible on Hotel PDP", detailPage.isFAQVisible(), is(true));

        if(detailPage.isRoomsAvailable()){
            Assertion.assertThat("Hotel's Price is visible on Hotel PDP", !detailPage.getHotelPriceDetail().isEmpty(), is(true));
            Assertion.assertThat("See Rooms Button  is visible on Hotel PDP", detailPage.isSeeRoomVisible(), is(true));

            step("Rooms are available hence click on See Rooms Button");
            detailPage.seeHotelRoom();

            step("Checking Recommended Room");
            Assertion.assertThat("Recommended room should be visible on room list", roomListPage.isRecommendedRoomVisible(), is(true));
            roomListPage.recommendedRoomForGuest(noOfAdults);

            step("Checking room List");
            Assertion.assertThat("Room lists section should be visible",roomListPage.isRoomListSectionVisible(),is(true));
            Assertion.assertThat("Room's Image  should be visible",roomListPage.isRoomImageVisible(),is(true));
        }else {
            Assertion.assertThat("If room is fully booked respective text is visible on Hotel PDP", !detailPage.getBookingFullDetail().isEmpty(), is(true));
        }
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void VerifyRoomOverviewRoomListLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("VerifyRoomOverviewRoomListLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");
        int noOfAdults = Integer.parseInt(param.get("Adult"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

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

        Assertion.assertThat("Verify User is redirected to PDP",detailPage.isPdpLoaded(),is(true));
        Assertion.assertThat("Hotel name on the Landing and detail page should be equal", detailPage.getHotelNameDetail(), containsString(location));

        if(detailPage.isRoomsAvailable()){
            Assertion.assertThat("Hotel's Price is visible on Hotel PDP", !detailPage.getHotelPriceDetail().isEmpty(), is(true));
            Assertion.assertThat("See Rooms Button  is visible on Hotel PDP", detailPage.isSeeRoomVisible(), is(true));

            step("Rooms are available hence click on See Rooms Button");
            detailPage.seeHotelRoom();

            step("Checking room List");
            roomListPage.scrollToRoomListSection();
            Assertion.assertThat("Room lists section should be visible",roomListPage.isRoomListSectionVisible(),is(true));
            Assertion.assertThat("Room Image should be visible",roomListPage.isRoomImageVisible(),is(true));

            step("checking individuals rooms visibile on roomlist");
            roomListPage.roomListDetails(2);
        }else {
            Assertion.assertThat("If room is fully booked respective text is visible on Hotel PDP", !detailPage.getBookingFullDetail().isEmpty(), is(true));
        }
    }

    @Author(names = "Harshit")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyFlexibleStayContentOnSRPLogin() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("verifyFlexibleStayContentOnSRPLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
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
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("close Pop up");
        searchResultPage.clickPopUp();

        Assertion.assertThat("SRP is loaded ", searchResultPage.isHotelCardVisible(), is(true));

        step("Scroll to bottom to see Flexible Stay");
        searchResultPage.scrollToFlexibleStay();

        Assertion.assertThat("Flexible stay dates card is visible", searchResultPage.isFlexibleStayVisible(), is(true));
        Assertion.assertThat("Hotel's Image on Flexible stay card is visible", searchResultPage.isFlexibleStayImageVisible(), is(true));
        Assertion.assertThat("Verifying Heading on Flexible Stay Date is correct based on selected Language", searchResultPage.getFlexibleStayHeading(), either(containsString("Do you have flexible stay dates")).or(containsString("Tanggal nginepmu fleksibel")));
        Assertion.assertThat("Hotel name on Flexible Stay Card is visible", !searchResultPage.getFlexibleStayHotelName().isEmpty(), is(true));
        Assertion.assertThat("(See and adjust you dates) text is present at bottom of Hotel card based on selected Language", searchResultPage.getFlexibleStayText(), either(containsString("See and adjust your date")).or(containsString("Lihat dan atur tanggal")));

        step("Click on Flexible Stay Card");
        searchResultPage.clickOnFlexibleStayCard();
        detailPage.switchToNewTab();

        Assertion.assertThat("Verify if user is redirected to PDP ",!detailPage.getHotelNameDetail().isEmpty(),is(true));
    }

    @Author(names = "Mridul")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelSRPDeeplinkAndFlexibleStayLoginWeb() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyHotelSRPDeeplinkAndFlexibleStayLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String locationId = param.get("locationId");
        String locationBahasa=param.get("locationBahasa");
        String locationType = param.get("locationType");
        int rooms = Integer.parseInt(param.get("rooms"));
        int adults = Integer.parseInt(param.get("adults"));
        int daysToCheckIn = Integer.parseInt(param.get("daysToCheckIn"));
        int stayDuration = Integer.parseInt(param.get("stayDuration"));
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);

        String checkInDate = CoreFunctions.addToDateYYYY_MM_DD(CoreFunctions.getCurrentDateYYYY_MM_DD(), daysToCheckIn);
        String checkOutDate = CoreFunctions.addToDateYYYY_MM_DD(checkInDate, stayDuration);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        String lang = homePage.getLanguage();
        String locationName = lang.equals("en") ? location : locationBahasa;

        step("Step 1 - Land on SRP via deeplink for location: " + location);
        searchResultPage.openHotelSrpViaDeeplink(location, locationId, locationType, rooms, adults, checkInDate, checkOutDate);

        step("Close coachmark pop up if visible");
        searchResultPage.clickPopUp();

        Assertion.assertThat("Hotel SRP is loaded after deeplink navigation", searchResultPage.isHotelCardVisible(), is(true));

        step("Step 2 - Verify search bar reflects deeplink-searched location and dates");
        String searchBarLocation = searchResultPage.getLocationNameFromInput();
        String roomAndGuestText = searchResultPage.getRoomAndGuestInfoFromInput();
        String currentUrl = searchResultPage.getCurrentUrl();

        Assertion.assertThat("Assertion 1 - URL contains expected check-in date", currentUrl, containsString("checkin=" + checkInDate));
        Assertion.assertThat("Assertion 1 - URL contains expected check-out date", currentUrl, containsString("checkout=" + checkOutDate));
        Assertion.assertThat("Assertion 1 - URL contains expected room count", currentUrl, containsString("room=" + rooms));
        Assertion.assertThat("Assertion 1 - URL contains expected adult count", currentUrl, containsString("adult=" + adults));
        Assertion.assertThat("Assertion 1 - Search bar shows deeplink-searched location", searchBarLocation, containsStringIgnoringCase(location));
        Assertion.assertThat("Assertion 1 - Room and guest input shows configured rooms", roomAndGuestText, containsString(String.valueOf(rooms)));
        Assertion.assertThat("Assertion 1 - Room and guest input shows configured adults", roomAndGuestText, containsString(String.valueOf(adults)));

        step("Step 3 - Verify hotel cards belong to the searched area, region or city");
        Assertion.assertThat("Assertion 2 - Hotel cards on SRP belong to the searched location",
                searchResultPage.rightHotel(locationName), is(true));

        step("Step 4 - Scroll to the sold-out intercept on SRP");
        searchResultPage.scrollToFlexibleStay();

        Assertion.assertThat("Assertion 3 - Sold-out intercept (flexible stay dates) is visible",
                searchResultPage.isFlexibleStayVisible(), is(true));
        Assertion.assertThat("Assertion 3 - Sold-out intercept heading matches 'Do you have flexible stay dates' in selected language",
                searchResultPage.getFlexibleStayHeading(),
                either(containsString("Do you have flexible stay dates")).or(containsString("Tanggal nginepmu fleksibel")));
        Assertion.assertThat("Assertion 3 - Hotel image is visible on the sold-out intercept",
                searchResultPage.isFlexibleStayImageVisible(), is(true));
        Assertion.assertThat("Assertion 3 - Hotel name is visible on the sold-out intercept",
                !searchResultPage.getFlexibleStayHotelName().isEmpty(), is(true));

        step("Step 5 - Verify hotel cards inside sold-out intercept show no price");
        Assertion.assertThat("Assertion 4 - Sold-out intercept contains at least one hotel card",
                searchResultPage.getFlexibleStayHotelCardCount(), greaterThan(0));
        Assertion.assertThat("Assertion 4 - No room/price available for hotel cards inside sold-out intercept",
                searchResultPage.isFlexibleStayPriceVisible(), is(false));
    }

    @Author(names = "Harshit")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void completeBookingMoWithoutAddOnsLoginHotel() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("completeBookingMoWithoutAddOnsLoginHotel");
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
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");


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

        step("User Open hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

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
    

        step("Go to payment page");
    
        bookingFormPage.continueTo2NdPartThenPaymentPage();

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
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySoRoomListLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySoRoomListLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        int noOfRooms = Integer.parseInt(param.get("rooms"));
        int guests = Integer.parseInt(param.get("guests"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        detailPage.seeHotelRoom();

        step("User update number of room & guest on roomList");
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(guests);
        roomListPage.setTotalRoom(noOfRooms);

        roomListPage.clickGuestDoneButton();
        roomListPage.clickRoomListChangeButton();

        roomListPage.scrollToRoomListSection();

        step("Checking room List");
        Assertion.assertThat("Room lists section should be visible",roomListPage.isRoomListSectionVisible(),is(true));
        Assertion.assertThat("Room Image should be visible",roomListPage.isRoomImageVisible(),is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();

        Assertion.assertThat(" User redirected to Booking Form when user click Book button on room card",bookingFormPage.isContactForm(),is(true));

        step("Go back to previous Page");
        detailPage.goToPreviousPage();

        step("Click on See other rooms");
        bookingFormPage.clickSeeOtherRooms();

        step("CLick on Room card to open Room Detail Page");
        roomListPage.openRoomDetail();

        Assertion.assertThat("Verify if Room Detail Page Opened",roomListPage.isRoomDetailPage(), is(true));
        Assertion.assertThat("Image is visible on Room Detail",roomListPage.isImageOnRoomDetail(), is(true));

        step("Click on book button on room Detail");
        roomListPage.clickBookRoomDetail();
        Assertion.assertThat(" User redirected to Booking Form when user click Book button on room Detail Page",bookingFormPage.isContactForm(),is(true));
    }

    @Author(names = "Harshit")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void VerifyChooseAddOnsLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("VerifyChooseAddOnsLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String dateType=param.get("dateType");
        String dateRange=param.get("dateRange");


        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

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

        String pdpHotelName=detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Hotel name on the search and detail page should be equal",pdpHotelName , containsString(location));

        step("Rooms are available hence click on See Rooms Button");
        detailPage.seeHotelRoom();

        step("Scroll to room list section");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list is visible",roomListPage.isRoomListSectionVisible(), is(true));
        Assertion.assertThat("Rooms Image should be visible",roomListPage.isRoomImageVisible(),is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();

        Assertion.assertThat("Contact form should be visible on Booking Form page",bookingFormPage.isContactForm(),is(true));

        Assertion.assertThat("Guest Stay details form should be visible on Booking Form page",bookingFormPage.isGuestStayForm(),is(true));

        bookingFormPage.continueToNextPage();
        int oldPrice=bookingFormPage.getTotalPrice();

        step("User select addOns and insurance");
        bookingFormPage.chooseAddOnsAndInsurance(true);

        int newPrice=bookingFormPage.getTotalPrice();

        Assertion.assertThat("Price should be increased when user select AddOns and Insurance", newPrice, greaterThan(oldPrice));
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRecommendedRoomLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyRecommendedRoomLogin");
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

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
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
        Assertion.assertThat("Hotel's Price is visible on Hotel PDP", !detailPage.getHotelPriceDetail().isEmpty(), is(true));

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
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySearchByHotelName(){
        Map<String, String> param = getParam("verifySearchByHotelName");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String hotel = param.get("hotel");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(hotel);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String HotelName=detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP  is same",HotelName,containsString(hotel));

        step("Go back to LP to search hotel name in SRP");
        detailPage.goToPreviousPage();

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("search stay location to open SRP");
        landingPage.searchStayLocation(location);
        landingPage.clickSubmit();

        searchResultPage.clickPopUp();
        Assertion.assertThat("Srp is Loaded",searchResultPage.isHotelCardVisible(),is(true));

        step("Search Hotel's name on SRP");
        searchResultPage.UpdateStayLocation(hotel);

        step("Click search button to go inside PDP");
        searchResultPage.clickSearchOnSRP();

        String HotelName2=detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify User is redirected to PDP",detailPage.isPdpLoaded(),is(true));
        Assertion.assertThat("Verify Hotel name on entered on SRP and PDP  is same",HotelName2,containsString(hotel));

    }

    @Author(names = "Harshit")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyAbilityToAddSplReqLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyAbilityToAddSplReqLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String fullName = CoreFunctions.randomName();
        String splReq=param.get("splReq");
        String customReq=param.get("customReq");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

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
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room lists section should be visible",roomListPage.isRoomListSectionVisible(),is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("User enter Stay details and add special Request ");
        Assertion.assertThat("Stay details form should be visible on Booking Form page",bookingFormPage.isGuestStayForm(),is(true));
        bookingFormPage.fillGuestDetails(1,fullName,true,true,splReq);

        bookingFormPage.enterSpecialRequest(customReq);
        Assertion.assertThat("Verify if Special Request is added successfully",bookingFormPage.getSpecialReqText(),containsString(customReq));
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingSoNativeVendor() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyBookingSoNativeVendor");
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
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
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

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("Reset room count to 1");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(1);
        landingPage.clickDone();

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String HotelName=detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP  is same",HotelName,containsString(location));

        roomListPage.scrollToRoomListSection();

        String vendorName=roomListPage.getVendorNameFromColor();

        Assertion.assertThat("Verify vendor is Native i.e Tiket",vendorName,containsString("TIKET"));

        step("Click Book Button");
        roomListPage.clickBookButton();
        hotelBookingFormPage.skipRoomChangePopup();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page",bookingFormPage.isContactForm(),is(true));

        step("User enter Stay details ");
        Assertion.assertThat("Guest Stay details form should be visible on Booking Form page",bookingFormPage.isGuestStayForm(),is(true));

        step("Go to payment page");
        bookingFormPage.continueToNextPage();
        bookingFormPage.continueToPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("contact name should be available on order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("No of Rooms should be correct",orderDetailPage.getTotalRoom(), is(1));
    }

    @Author(names = {"Harshit", "Vipul Kumar"})
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyMultiOrderNativeDiffRatePlan() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyMultiOrderNativeDiffRatePlan");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod=param.get("paymentMethod");
        String fullName = CoreFunctions.randomName();
        int noOfRooms = Integer.parseInt(param.get("rooms"));
        int guests = Integer.parseInt(param.get("guests"));

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String HotelName=detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP  is same",HotelName,containsString(location));

        roomListPage.scrollToRoomListSection();

        step("User update number of room & guest on roomList");
        roomListPage.clickRoomNGuestButton();
        roomListPage.setTotalGuest(noOfRooms);
        roomListPage.setTotalRoom(guests);

        roomListPage.clickGuestDoneButton();
        roomListPage.clickRoomListChangeButton();

        String vendorName=roomListPage.getVendorNameFromColor();
        Assertion.assertThat("Verify vendor is Native i.e Tiket",vendorName,containsString("TIKET"));

        step("Choose first room");
        roomListPage.firstBookRoom();

        step("Click second room with different rate plan ");
        roomListPage.secondBookRoom();

        roomListPage.clickMultiBookButton();
        bookingFormPage.dismissRatePlanChangePopupsIfPresent();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page",bookingFormPage.isContactForm(),is(true));

        step("User enter Stay details ");
        Assertion.assertThat("Guest Stay details form should be visible on Booking Form page",bookingFormPage.isGuestStayForm(),is(true));
        bookingFormPage.fillGuestDifferentRatePlan(fullName);

        step("Go to payment page");
        bookingFormPage.continueToNextPage();
        bookingFormPage.continueToPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("contact name should be available on order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));
    }

    @Author(names = "Harshit")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookingIDR() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookingIDR");
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
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        searchResultPage.clickPopUp();

        String searchPageCurrency = homePage.getCurrentCurrency();
        Assertion.assertThat("Verify currency in  Search Result page is set to IDR", searchPageCurrency.equals("IDR"), is(true));
        Assertion.assertThat("Srp is Loaded",searchResultPage.isHotelCardVisible(),is(true));

        step("Select hotel Card ");
        searchResultPage.selectHotel();

        step("Go Inside PDP");
        detailPage.switchToNewTab();

        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));

        roomListPage.scrollToRoomListSection();
        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go to payment page");
        bookingFormPage.continueToNextPage();
        bookingFormPage.continueToPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("User complete payment");
        paymentPage.clickOnPaymentButton();

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Go to Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("contact name should be available on order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyGuestDetailsandSplReqLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyGuestDetailsandSplReqLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String splReq=param.get("splReq");
        String name = param.get("name");
        int room = Integer.parseInt(param.get("room"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPage<?> hotelBookingFormPage = new HotelBookingFormPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");
        homePage.selectEnglishLanguage();

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalRoom(room);
        landingPage.clickDone();

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));

        step("Checking room List");
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room lists section should be visible",roomListPage.isRoomListSectionVisible(),is(true));

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("User enter Stay details and add special Request ");
        hotelBookingFormPage.skipRoomChangePopup();
        Assertion.assertThat("Stay details form should be visible on Booking Form page",bookingFormPage.isGuestStayForm(),is(true));
        bookingFormPage.fillGuestDetails(1,"",false,true,"Connecting Room");
        Assertion.assertThat("Verify Guest name is added Successfully ",bookingFormPage.getGuestName(),containsString(name));

        bookingFormPage.enterSpecialRequest(splReq);
        Assertion.assertThat("Verify if Custom Special Request is added successfully",bookingFormPage.getSpecialReqText(),containsString(splReq));
        Assertion.assertThat("Verify if Other Special Request is added successfully",bookingFormPage.getSpecialReqText(),containsString("Connecting Room"));
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookingNonIDRCC() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookingNonIDRCC");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cardCvv = param.get("cardCvv");
        String cardHolderName = param.get("cardHolderName");
        String currency=param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);
        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String HotelName=detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP  is same",HotelName,containsString(location));

        roomListPage.scrollToRoomListSection();
        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go to payment page");
        bookingFormPage.continueToPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("Scroll down to the payment Group");

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cardCvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);

        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("contact name should be available on order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), is(email));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyCompleteBookingNonIDRAddOnsCC() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyCompleteBookingNonIDRAddOnsCC");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String cardNumber = param.get("cardNumber");
        String cardMonth = param.get("cardMonth");
        String cardCvv = param.get("cardCvv");
        String cardHolderName = param.get("cardHolderName");
        String currency=param.get("currency");

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        HotelBookingFormPage<?> hotelBookingFormPage = new HotelBookingFormPage<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);
        step("User select IDR currency");
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);
        step("Set date");
        landingPage.setStayDate("range date", "20-21");
        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String HotelName=detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP  is same",HotelName,containsString(location));

        roomListPage.scrollToRoomListSection();
        step("Click Book Button");
        roomListPage.clickBookButton();
        hotelBookingFormPage.skipRoomChangePopup();

        step("User input contact detail");
        Assertion.assertThat("Contact form should be visible on Booking Form page",bookingFormPage.isContactForm(),is(true));

        step("User enter Stay details ");
        Assertion.assertThat("Guest Stay details form should be visible on Booking Form page",bookingFormPage.isGuestStayForm(),is(true));
        bookingFormPage.fillGuestDetails(1,"",false,false,"");

        bookingFormPage.continueToNextPage();
        int oldPrice=bookingFormPage.getTotalPrice();

        step("User select addOns and insurance");
        bookingFormPage.chooseAddOnsAndInsurance(false);

        int newPrice=bookingFormPage.getTotalPrice();

        Assertion.assertThat("Price should be increased when user select AddOns and Insurance", newPrice, greaterThan(oldPrice));

        step("Go to payment page");
        hotelBookingFormPage.skipRoomChangePopup();
        bookingFormPage.continueToPaymentPage();

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));
        step("Scroll down to the payment Group");

        step("Input the card details");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardMonth);
        paymentPage.inputCardCvv(cardCvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Issued Order");
        orderListPage.openActiveTicketViaURL(orderID);

        orderDetailPage.skipCoachmark();

        Assertion.assertThat("e-Ticket on the order detail should be published", orderDetailPage.verifyIssuedETicket(), is(true));
        Assertion.assertThat("contact name should be available on order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), is(true));
        Assertion.assertThat("email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), is(email));
    }

    @Author(names = "Harshit")
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifySimilarPropertiesLogin() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifySimilarPropertiesLogin");
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
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");

        Assertion.assertThat("Hotel name is same as User searched on LP", detailPage.getHotelNameDetail(), containsString(location));
        roomListPage.scrollToRoomListSection();

        step("Scroll to bottom");
        Assertion.assertThat("Verify Similar properties section is visible",roomListPage.isSimilarPropertiesVisible(),is(true));
        Assertion.assertThat("Verify Header for Similar Properties",roomListPage.getSimilarPropertiesHeader(),containsString("Check out other accommodations"));
        Assertion.assertThat("Verify Similar Hotels card are visible",roomListPage.isSimilarHotelcard(),is(true));

        step("Click on Similar Hotel card to open PDP in new Tab");
        roomListPage.clickSimilarHotel();
        detailPage.switchToNewTab();

        Assertion.assertThat("Verify if PDP Opened",detailPage.isPdpLoaded(),is(true));
    }

    @Author(names = {"Harshit"})
    @Module(name = "OrderPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "verifySpecialRequestSoDataProvider")
    public void verifySpecialRequestSo(Map<String, String> param) throws FailedLoginException, InterruptedException, ExecutionException {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelReviseBookingPage<?> reviseBookingPage=new HotelReviseBookingPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp=param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = generateRandomDate(param.get("startDate"));
        String hotelName=param.get("hotelName");
        int adult=Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));;
        int room = Integer.parseInt(param.get("room"));;
        String currency="IDR";
        String newReq=param.get("newReq");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        orderCreationPage.createHotelSakukuIssuedOrderApi(username, password, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle,currency);

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(orderCreationPage.getOrderId());

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);



        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");

        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),is(bookedOrderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),is(true));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(hotelName), is(true));

        step("Click on Edit booking");
        orderDetailPage.clickEditBooking();
        orderDetailPage.clickEditPopUpSplReq();

        step("Add Other request");
        reviseBookingPage.addOtherRequest(newReq);
        reviseBookingPage.clickAgreeButton();
        reviseBookingPage.inputOTPCode("1234");
        Assertion.assertThat("Success message should be displayed ", reviseBookingPage.getSuccessText(), containsString("The special request has been changed"));
        reviseBookingPage.clickSeeDetails();
        Assertion.assertThat("Verify Order Detail Page is Opened", orderListPage.getCurrentUrl(),allOf(containsString("tiket.com/"), containsString("/yourorder/")));
        Assertion.assertThat("Verify OrderId is same",bookedOrderID,is(orderDetailPage.getOrderId()));
        Assertion.assertThat("Verify New Special Request is Added ",orderDetailPage.getSpecialReq(),containsString(newReq));
    }
    @DataProvider(name = "verifySpecialRequestSoDataProvider")
    public Object[][] verifySpecialRequestSoDataProvider() {return getObjectParams("verifySpecialRequestSo");}

    @Author(names = {"Harshit"})
    @Module(name = "OrderPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test(dataProvider = "testFullRefundCcHotelDataProvider")
    public void testFullRefundCcHotel(Map<String, String> param) throws FailedLoginException, InterruptedException {
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp=param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = generateRandomDate(param.get("startDate"));
        String hotelName=param.get("hotelName");
        int adult=Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));;
        int room = Integer.parseInt(param.get("room"));;
        String currency="IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order");
        boolean result = hotelApi.createIssuedOrderApi(
            username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, true,
                false, "", "en",
                true);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(hotelApi.getOrderId());

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(username, password, otp, retryCounter);

        homePage.selectCurrency("IDR");

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(),is(bookedOrderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),is(true));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));
        Assertion.assertThat("Refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), is(true));

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();

        step("Click to contuniue");
        refundPage.continueToRefund();

        step("Select random refund reason");
        refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear("5");
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotal();
        List<String> amounts = new ArrayList<>(Arrays.asList(refundReceive, refundFee, refundTotal));
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive)+CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        amounts.clear();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));

        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Continue to next page");
        refundPage.clickButton("Continue");
        refundPage.waitRefundSecondPageLoaded();
        Assertion.assertThat("Order ID on 2nd refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(bookedOrderID));
        Assertion.assertThat("Refund progress banner same as refund step", refundPage.getActiveProgress(), is(2));

        step("Verify refund method and amount");
        String refundMethodSection = refundPage.getRefundMethod();
        Assertion.assertThat("Refund method is correct", refundMethodSection, not(emptyOrNullString()));
        Assertion.assertThat("Refund amount is same as prev page", refundTotal, is(refundPage.getRefundTotal()));

        step("Check Refund TnC");
        Assertion.assertThat("Submit refund button is disabled", refundPage.isDisabledSubmitButton(), is(true));
        refundPage.clickChecklistTnc();
        refundPage.clickButton("Submit");

       step("Submit refund");
        refundPage.clickButton("Process Refund");
        refundPage.clickConfirmationRefundPopup();
        refundPage.waitForSubmitRefundLoader();

        step("Go to refund detail page");
        refundPage.SuccessSubmitRefundPopup();

        step("Verify Refund Detail");
        refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(), not(emptyOrNullString()));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(), not(emptyOrNullString()));

        step("Verify Refund Method and Amount");
        for (String method : refundDetailPage.getRefundMethod()) {
            Assertion.assertThat("Refund method not empty", method, not(emptyOrNullString()));
        }

        step("Verify product section");
        Assertion.assertThat("Product detail shown", refundDetailPage.isOrderDetailSection(), is(true));

        step("Verify back button to order list");
        refundDetailPage.clickBackButton();
        Assertion.assertThat("Current page is order list", orderListPage.getCurrentUrl(), containsString("https://preprod.tiket.com/en-sg/yourorder"));
    }

    @DataProvider(name = "testFullRefundCcHotelDataProvider")
    public Object[][] testFullRefundCcHotelDataProvider() {return getObjectParams("testFullRefundCcHotel");}

    @Author(names = "Ketan Singh, Shubh Sharma")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRoomDetailSlidingImage(){
        Map<String, String> param = getParam("verifyRoomDetailSlidingImage");
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

        step("Go Inside PDP");
        Assertion.assertThat("PDP is loaded", detailPage.isPdpLoaded(), is(true));
        detailPage.seeHotelRoom();

        step("Ensure room list section is visible");
        String firstImagePath = "";
        String secondImagePath = "";
        roomListPage.scrollToRoomListBookSection();
        if(Configs.PLATFORM.equals(Platform.AMWEB) || Configs.PLATFORM.equals(Platform.IMWEB)){
            firstImagePath = roomListPage.savePath(1);
            secondImagePath = roomListPage.savePath(2);
        }
        Assertion.assertThat("Room List is visible", roomListPage.isRoomListSectionVisible(), is(true));

        step("Click on room name");
        String sliderText = roomListPage.getImageCount();
        roomListPage.clickRoomCard();

        step("Save first image path then click on next and check");
        String[] parts = sliderText.split("/");
        int count = Integer.parseInt(parts[1].trim());
        if(Configs.PLATFORM.equals(Platform.AMWEB) || Configs.PLATFORM.equals(Platform.IMWEB)){
            roomListPage.clickRoomImage();
            roomListPage.clickRoomImageInPopUp();
        }
        if(count > 1) {
            if(Configs.PLATFORM.equals(Platform.DWEB)){
                firstImagePath = roomListPage.savePath(1);
            }
            roomListPage.clickNext(count);
            if(Configs.PLATFORM.equals(Platform.DWEB)){
                secondImagePath = roomListPage.savePath(2);
            }
            Assertion.assertThat("Slider working properly", firstImagePath.equals(secondImagePath), is(false));
        }
    }

    @Author(names = "Venkat")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void testNearbyLepus() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("testNearbyLepus");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String currency = param.get("currency");

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User opens home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, 1);

        step("User selects IDR currency");
        homePage.selectCurrency("IDR");

        step("User opens hotel landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location");
        landingPage.searchStayLocation(location);

        step("Set date");
        landingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("Submit search button");
        landingPage.clickSubmit();

        step("Close pop-up");
        searchResultPage.clickPopUp();

        step("Verify Nearby Lepus section is visible");
        Assertion.assertThat("Nearby Lepus section is visible", searchResultPage.isNearbyLepusVisible(currency), is(true));
    }

    @Author(names = "Akshi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPromoOnBookingForm() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyPromoOnBookingForm");
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
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("Reset guests and rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(1);
        landingPage.setTotalRoom(1);
        landingPage.clickDone();

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        detailPage.seeHotelRoom();

        roomListPage.scrollToRoomListSection();
        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Go to Booking Form 2");
        bookingFormPage.continueToNextPage();
        Assertion.assertThat("Check promo section present",bookingFormPage.isPromoSectionAvailable(),is(true));
        Assertion.assertThat("Check most suitable promo present",bookingFormPage.checkMostSuitablePromo(),is(true));
        bookingFormPage.addPromo();
        Assertion.assertThat("Check most suitable promo Applied",bookingFormPage.checkPromoApplied(),is(true));
    }
    @Author(names = "Akshi")
    @Module(name = "BookingPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPromoTextOnBookingForm() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyPromoTextOnBookingForm");
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
        HotelRoomListPage<?> roomListPage=new HotelRoomListPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        step("User open home page");
        homePage.goToHomePage();

        step("User open as logged in user");
        loginPage.loginIfNotWithSameEmail(email, password, otp, retryCounter);

        step("User select IDR currency");
        homePage.selectCurrency("IDR");

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        Assertion.assertThat("Landing Page is Opened", landingPage.isLandingPageVisible(), is(true));

        step("Search stay hotel");
        landingPage.searchStayLocation(location);

        step("submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        detailPage.seeHotelRoom();

        roomListPage.scrollToRoomListSection();
        step("Click Book Button");
        roomListPage.clickBookButton();
        String roomPrice=roomListPage.getRoomPrice();
        Assertion.assertThat("Check price is same as RoomList",bookingFormPage.checkBookingPrice(roomPrice),is(true));

        step("Go to Booking Form 2");
        bookingFormPage.continueToNextPage();
        Assertion.assertThat("Check promo section present",bookingFormPage.isPromoSectionAvailable(),is(true));
        bookingFormPage.clickPromoSection();
        bookingFormPage.clickToAddPromo();
        Assertion.assertThat("Check most suitable promo text not displayed",bookingFormPage.checkMostSuitablePromoNotApplied(),is(false));
    }

    @Author(names = "Akshi")
    @Module(name = "DetailPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPromoOnPDP() throws FailedLoginException, InterruptedException {
        Map<String, String> param = getParam("verifyPromoOnPDP");
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
        homePage.openVerticalPageViaURL("Hotel");
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

    @Author(names = "Pankaj Singh Kushwaha")
    @Module(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Vertical(name = "Accommodation")
    @Test
    public void ACC_verifyManagerOrderReceiptAndPaidAmountAfterSuccessfulPayment()
            throws FailedLoginException, InterruptedException {

        Map<String, String> param = getParam("ACC_verifyManagerOrderReceiptAndPaidAmountAfterSuccessfulPayment");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        String username = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String location = param.get("location");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Open home page");
        homePage.goToHomePage();

        step("Login user");
        loginPage.loginAsGuest();
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Select currency");
        homePage.selectCurrency(currency);

        step("Open hotel page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing page visible",
                landingPage.isLandingPageVisible(), is(true));

        step("Search hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("Verify PDP");
        Assertion.assertThat("PDP loaded",
                detailPage.isPdpLoaded(), is(true));
        detailPage.seeHotelRoom();

        step("Select refundable room");
        roomListPage.clickFreeRefundRescheduleFilter();
        Assertion.assertThat("Refund filter applied",
                roomListPage.isFreeRefundRescheduleFilterSelected(), is(true));

        step("Capture price from PDP");
        String afterTaxPriceFromPDP = roomListPage.getAfterTaxPrice();

        step("Book room");
        roomListPage.clickBookButton();

        step("Go to payment page");
        bookingFormPage.continueTo2NdPartThenPaymentPage();

        step("Verify payment page loaded");
        Assertion.assertThat("Payment page should be loaded",
                paymentPage.isPaymentPageLoaded(), is(true));

        step("Capture Order ID");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Order ID should not be empty",
                orderID != null && !orderID.isEmpty(), is(true));

        step("Capture payment price");
        String paymentPrice = paymentPage.getFinalPrice();

        step("Verify price matches PDP");
        Assertion.assertThat("Payment price should match PDP",
                paymentPrice,
                containsString(afterTaxPriceFromPDP.replaceAll("[^0-9]", "")));

        step("Select payment method");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentGroup);
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Click Pay");
        paymentPage.clickOnPaymentButton();

        step("Verify payment success");
        Assertion.assertThat("Payment success page should be visible",
                paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Open order detail via URL");
        orderListPage.openActiveTicketViaURL(orderID);

        step("Handle coachmark");
        orderDetailPage.skipCoachmark();

        step("click Manage Order button");
        orderDetailPage.clickManageOrder();

        step("Handle coachmark");
        orderDetailPage.skipCoachmark();

        step("Capture order detail amount");
        String orderDetailAmount = orderDetailPage.getFinalPrice();

        step("Verify Receipt Button");
        orderDetailPage.verifyReceiptButton();

        step("Validate paid amount consistency");
        Long paymentAmount = Long.parseLong(paymentPrice.replaceAll("[^0-9]", ""));
        Long orderAmount = Long.parseLong(orderDetailAmount.replaceAll("[^0-9]", ""));

        Assertion.assertThat(
                "Order detail amount should match payment amount",
                orderAmount,
                is(paymentAmount)
        );
    }


    @Author(names = {"Manan Kumar", "Vipul Kumar"})
    @Module(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Vertical(name = "Accommodation")
    @Test
    public void ACC_verifyHotelRescheduleAfterFreeRefundRescheduleFilter() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("ACC_verifyHotelRescheduleAfterFreeRefundRescheduleFilter");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePage = HotelReschedulePageWeb.getInstance(session);
        RoomDetailsPageWeb<?> roomDetailsPage = RoomDetailsPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPageWeb = PaymentPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderDetailWeb<?> orderDetailWeb = new OrderDetailDweb<>(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);

        String username = param.get("email");
        String password = param.get("password");
        String currency = param.get("currency");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        boolean isRefundable = Boolean.parseBoolean(param.get("isRefundable"));
        int rescheduleDuration = Integer.parseInt(param.get("rescheduleDuration"));

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = getRandomObject(salutationList);

        step("Create refundable hotel issued order via API");
        boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, isRefundable,
                false, "", "en",
                true);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        String orderID = String.valueOf(hotelApi.getOrderId());

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("User select " + currency + " currency");
        homePage.selectCurrency(currency);

        step("Open order detail for issued order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details before reschedule");
        Assertion.assertThat("Wait for E-ticket to issued: " + orderID, orderDetailWeb.verifyIssuedETicket(orderID),is(true));
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(), is(true));
        Assertion.assertThat("Refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));
        Assertion.assertThat("Refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), is(true));
        Assertion.assertThat("Reschedule button should be displayed", orderDetailPage.isRescheduleButtonDisplayed(), is(true));
        String totalPaymentInitialOrder = orderDetailPage.getVerticalTotalPrice();

        step("User click Reschedule");
        orderDetailPage.clickRescheduleButton();

        step("Reschedule flow: room selection page is loaded");
        Assertion.assertThat("Reschedule page is loaded", hotelReschedulePage.isInReschedulePage(), is(true));

        step("Choose new date range for the reschedule");
        hotelReschedulePage.changeNightDuration(rescheduleDuration);

        step("Open room with cashback and verify balance");
        hotelReschedulePage.openRoomWithCashback();
        roomDetailsPage.clickOnPriceDetails();
        Assertion.assertThat("Current balance should be equal with initial total price", roomDetailsPage.getCurrentBalance(true), is(totalPaymentInitialOrder));
        roomDetailsPage.closePriceDetails();

        step("Book the room with cashback");
        roomDetailsPage.getRoomNameDetails();
        roomDetailsPage.clickBookButton();

        step("Submit reschedule booking form");
        hotelReschedulePage.submitRescheduleBookingForm();

        step("check order id on payment page");
        String rescheduleOrderID = paymentPageWeb.getOrderId();

        step("Pay with refund deposit using current balance");
        paymentPageWeb.selectRefund();
        paymentPageWeb.clickOnPaymentButton();

        step("Submit refund request for cashback");
        refundDetailPage.submitRefundCashBackFreePayment();

        step("Verify the reschedule request is submitted");
        Assertion.assertThat("Reschedule request submitted should be visible", paymentPageWeb.isRescheduleRequestSubmittedVisible(), is(true));

        step("Navigate to the details page of the rescheduled order");
        orderListPage.openActiveTicketViaURL(rescheduleOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify voucher (e-ticket) and receipt after reschedule payment");
        Assertion.assertThat("Wait for E-ticket to issued: ", orderDetailWeb.verifyIssuedETicket(orderID),is(true));
        Assertion.assertThat("Reschedule card notification should be present", orderDetailPage.isRescheduleCardDisplayed(), is(true));
        Assertion.assertThat("Order ID should be showing as expected", orderDetailPage.getOrderId(), is(rescheduleOrderID));
    }

    @Author(names = "Pankaj Singh Kushwaha")
    @Module(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Vertical(name = "Accommodation")
    @Test
    public void ACC_verifyPaymentPageIDRAndPaymentMethods() throws FailedLoginException, InterruptedException{
        Map<String, String> param = getParam("ACC_verifyPaymentPageIDRAndPaymentMethods");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        String username = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String currency = param.get("currency");
        String location = param.get("location");
        String dateType = param.get("dateType");
        String dateRange = param.get("dateRange");
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        step("Open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Select currency");
        homePage.selectCurrency(currency);

        step("Open hotel page");
        homePage.openVerticalPageViaURL("Hotel");
        Assertion.assertThat("Landing page visible", landingPage.isLandingPageVisible(), is(true));

        step("Search hotel");
        landingPage.searchStayLocation(location);
        landingPage.setStayDate(dateType, dateRange);
        landingPage.clickSubmit();

        step("Verify PDP");
        Assertion.assertThat("PDP loaded", detailPage.isPdpLoaded(), is(true));
        detailPage.seeHotelRoom();

        step("Select refundable room");
        roomListPage.clickFreeRefundRescheduleFilter();
        Assertion.assertThat("Refund filter applied", roomListPage.isFreeRefundRescheduleFilterSelected(), is(true));

        step("Capture price");
        String afterTaxPriceFromPDP = roomListPage.getAfterTaxPrice();

        step("Book room");
        roomListPage.clickBookButton();

        step("Go to payment page");
        bookingFormPage.selectFirstContactSalutation();
        bookingFormPage.continueTo2NdPartThenPaymentPage();
        bookingFormPage.selectPayment();

        step("Verify payment page loaded");
        Assertion.assertThat(
                "Payment page should be loaded",
                paymentPage.isPaymentPageLoaded(),
                is(true)
        );

        step("Verify Order ID");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Order ID visible", !orderID.isEmpty(), is(true));

        step("Verify all prices are in IDR");
        Assertion.assertThat(
                "All prices should be in IDR",
                paymentPage.verifyTotalPaymentPriceInIDR(),
                is(true)
        );

        step("Verify price matches PDP");
        String paymentPrice = paymentPage.getFinalPrice();
        Assertion.assertThat(
                "Payment price should match PDP",
                paymentPrice,
                containsString(afterTaxPriceFromPDP.replaceAll("[^0-9]", ""))
        );

        step("Open payment methods");
        paymentPage.clickOnSelectPaymentMethod();

        step("Select payment method");
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Verify selected payment method");
        Assertion.assertThat(
                "Selected payment method should match",
                paymentPage.getSelectedPayment(),
                containsString(paymentMethod)
        );

        step("Click Pay");
        paymentPage.clickOnPaymentButton();

        step("Verify payment success");
        Assertion.assertThat(
                "Payment success page should be visible",
                paymentPage.isPaymentSuccessfulVisible(),
                is(true)
        );

        step("Go to order list");
        paymentPage.clickSeeOrderListButton();
    }

    @Author(names = {"Abhishek Sharma", "Vipul Kumar"})
    @Module(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Vertical(name = "Accommodation")
    @Test
    public void ACC_testVerifyRefundProcess() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("ACC_testVerifyRefundProcess");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);

        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);

        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);
        RefundDetailWeb<?> refundDetailPage = RefundDetailWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int room = Integer.parseInt(param.get("room"));
        String currency = "IDR";
        String paymentGroup = param.get("paymentGroup");
        String paymentMethod = param.get("paymentMethod");

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User select " + currency + " currency");
        homePage.goToHomePage();
        homePage.selectCurrency(currency);

        step("User Open Hotel Landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay Hotel Name");
        landingPage.searchStayLocation(hotelName);

        step("Set date");
        landingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("Set Guest : " + adult + " Adults, " + room + " Rooms");
        landingPage.clickRoomAndGuestButton();
        landingPage.setTotalGuest(adult);
        landingPage.setTotalRoom(room);
        landingPage.clickDone();

        step("Submit search Button");
        landingPage.clickSubmit();

        step("Go Inside PDP");
        String hotelNameDetail =detailPage.getHotelNameDetail();
        Assertion.assertThat("Verify if PDP is Opened", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify Hotel name on entered on LP and PDP  is same",hotelNameDetail,containsString(hotelName));
        detailPage.seeHotelRoom();

        step("Select the Refund and Reschedule allowed filter chip");
        roomListPage.clickFreeRefundRescheduleFilter();
        Assertion.assertThat("Free Refund & Reschedule filter chip should be visible", roomListPage.isFreeRefundRescheduleFilterSelected(), is(true));

        step("Check that the cards now visible have the 100% Refundable label");
        Assertion.assertThat("At least one room should have 100% refund label", roomListPage.isFullRefundLabelVisible(), is(true));
        String afterTaxPriceFromPDP = roomListPage.getAfterTaxPrice().replaceAll("[^0-9]", "");

        step("Click Book Button");
        roomListPage.clickBookButton();

        step("Verify payment amount on Booking Details Page");
        String bookingPrice = bookingFormPage.getBookingPrice().replaceAll("[^0-9]", "");
        Assertion.assertThat("Booking Details Page total should match price from PDP", bookingPrice, is(afterTaxPriceFromPDP));

        step("Verify payment amount on Complementary Items Page");
        String totalPrice = String.valueOf(bookingFormPage.getTotalPrice()).replaceAll("[^0-9]", "");
        Assertion.assertThat("Complementary Items Page total should match price from PDP", totalPrice, is(afterTaxPriceFromPDP));

        step("Go to payment page");
        bookingFormPage.continueTo2NdPartThenPaymentPage();

        step("Continue to payment page");
        bookingFormPage.selectPayment();

        step("Verify payment amount on payment page");
        paymentPage.isPaymentPageLoaded();
        String finalPrice = paymentPage.getFinalPrice().replaceAll("[^0-9]", "");
        Assertion.assertThat("Payment page total should match price from PDP", finalPrice, is(afterTaxPriceFromPDP));

        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Verify order ID is displayed in payment page", !orderID.isEmpty(), is(true));

        step("User select payment method ");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.selectPaymentMethod(paymentMethod);

        step("Click on Pay button");
        paymentPage.clickOnPaymentButton();

        step("Verify the payment successful page is visible or not");
        Assertion.assertThat("Verify the payment successful page is visible", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("User try to open see order list");
        paymentPage.clickSeeOrderListButton();

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");
        Assertion.assertThat("orderID should be visible and should be same as from payment page", orderDetailPage.getOrderId(), is(orderID));
        Assertion.assertThat("Verify E-ticket issued", orderDetailPage.verifyIssuedETicket(),is(true));
        Assertion.assertThat("E-ticket card Displayed", orderDetailPage.isETicketCardDisplayed(),is(true));
        Assertion.assertThat("Email should be correctly displayed on order detail page", orderDetailPage.getContactEmail(orderID), is(email));
        Assertion.assertThat("Contact name should be available on order detail page", !orderDetailPage.getContactName(orderID).isEmpty(), is(true));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));
        Assertion.assertThat("Refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), is(true));
        String orderDetailFinalPrice = orderDetailPage.getFinalPrice().replaceAll("[^0-9]", "");
        Assertion.assertThat("Order Detail page total should match price from PDP", orderDetailFinalPrice, is(afterTaxPriceFromPDP));

        step("Open refund page");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear(); 

        step("Click to continue to refund");
        refundPage.continueToRefund();

        step("Select random refund reason");
        refundPage.selectRandomRefundReason();

        step("Verify refund estimation section");
        refundPage.waitForEstimationAmountAppear("15");
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotal();
        Assertion.assertThat("Refund Total amount is correct", CoreFunctions.removeText(refundReceive)+CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown");
        refundPage.openRefundBreakdown();
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Verify refund total matches the booking after-tax price (minus bank refund fee)");
        long bankRefundFee = Long.parseLong(param.getOrDefault("bankRefundFee", "5000"));
        String expectedRefundAfterBankFee = String.valueOf(Long.parseLong(afterTaxPriceFromPDP) - bankRefundFee);
        String refundTotalPrice = refundPage.getRefundTotalAndCurrency().replaceAll("[^0-9]", "");
        Assertion.assertThat("Refund total should match price from PDP minus bank refund fee", refundTotalPrice, is(expectedRefundAfterBankFee));

        step("Continue to next page");
        refundPage.clickButton("Continue");
        refundPage.waitRefundSecondPageLoaded();
        Assertion.assertThat("Order ID on 2nd refund page is correct", refundPage.getOrderIdOnRefundBanner(), containsString(orderID));

        step("Verify refund method and amount");
        String refundMethodSection = refundPage.getRefundMethod();
        Assertion.assertThat("Refund method is correct", refundMethodSection, not(emptyOrNullString()));
        Assertion.assertThat("Refund amount is same as prev page", refundTotal, is(refundPage.getRefundTotal()));

        step("Check Refund TnC");
        Assertion.assertThat("Submit refund button is disabled", refundPage.isDisabledSubmitButton(), is(true));
        refundPage.clickChecklistTnc();
        refundPage.clickButton("Submit");

        step("Submit refund");
        refundPage.clickButton("Process Refund");
        refundPage.clickConfirmationRefundPopup();
        refundPage.waitForSubmitRefundLoader();

        step("Go to refund detail page");
        refundPage.SuccessSubmitRefundPopup();

        step("Verify Refund Detail");
        refundDetailPage.countRefundProgressTracker();
        Assertion.assertThat("Refund detail status not empty or null string", refundDetailPage.getRefundStatus(), not(emptyOrNullString()));
        Assertion.assertThat("Refund Id not empty or null string", refundDetailPage.getRefundId(), not(emptyOrNullString()));
        String refundDetailPageTotal = refundDetailPage.getTotalRefund().replaceAll("[^0-9]", "");
        Assertion.assertThat("Refund Detail Total should match total price from PDP minus bank refund fee", refundDetailPageTotal, is(expectedRefundAfterBankFee));

        step("Verify Refund Method and Amount");
        for (String method : refundDetailPage.getRefundMethod()) {
            Assertion.assertThat("Refund method not empty", method, not(emptyOrNullString()));
        }

        step("Verify product section");
        Assertion.assertThat("Product detail shown", refundDetailPage.isOrderDetailSection(), is(true));

        step("Verify back button to order list");
        refundDetailPage.goToYourOrders();

        step("Verify the refunded order is no longer in your order page");
        Assertion.assertThat("The refunded order is no longer in your order page",
                refundDetailPage.isOrderIdPresentinYourOrder(orderID),
                is(false));
    }

    @Author(names = {"Hafiz", "Mridul", "Vipul Kumar"})
    @Module(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Vertical(name = "Accommodation")
    @Test
    public void ACC_verifyRescheduleRefundFee() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("ACC_verifyRescheduleRefundFee");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelReschedulePageWeb<?> hotelReschedulePage = HotelReschedulePageWeb.getInstance(session);
        RoomDetailsPageWeb<?> roomDetailsPage = RoomDetailsPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        RefundPageWeb<?> refundPage = RefundPageWeb.getInstance(session);

        String username = param.get("username");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));
        String startDate = generateRandomDate(param.get("startDate"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        int rescheduleDuration = Integer.parseInt(param.get("rescheduleDuration"));
        String currency = "IDR";
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = salutationList[0];

        step("Create new hotel issued order with refund and reschedule policy");
        boolean result = hotelApi.createIssuedOrderApi(
                username, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle, currency, true,
                false, "", "en",
                true);
        Assertion.assertThat("Hotel Booking via API is successful", result, is(true));

        step("Get data from the issued order");
        String bookedOrderID = String.valueOf(hotelApi.getOrderId());

        step("User open home page");
        homePage.goToHomePage();

        step("Login as " + username + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        homePage.selectCurrency(currency);

        step("Find the issued order");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify hotel order details");
        Assertion.assertThat("Order ID is displayed correctly", orderDetailPage.getOrderId(), is(bookedOrderID));
        Assertion.assertThat("Hotel Name is displayed correctly", orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("Refund and reschedule info section should be verified", orderDetailPage.verifyRefundAndRescheduleInfo(), is(true));
        Assertion.assertThat("Refund button should be displayed", orderDetailPage.isRefundButtonDisplayed(), is(true));

        step("Open refund page on initial issued order and verify refund fee");
        orderDetailPage.clickRefundButton();
        refundPage.waitForContentLoaderDisappear();
        refundPage.continueToRefund();
        refundPage.selectRandomRefundReason();
        refundPage.waitForEstimationAmountAppear("15");
        String refundReceive = refundPage.getRefundYoullReceive();
        String refundFee = refundPage.getRefundFee();
        String refundTotal = refundPage.getRefundTotal();
        Assertion.assertThat("Refund fee should be visible", refundFee, not(emptyOrNullString()));
        Assertion.assertThat("Refund total amount is correct (refund receive + fee = total)", CoreFunctions.removeText(refundReceive) + CoreFunctions.removeText(refundFee), is(CoreFunctions.removeText(refundTotal)));

        step("Verify refund amount breakdown with service fee deduction");
        refundPage.openRefundBreakdown();
        List<String> amounts = new ArrayList<>();
        amounts.addAll(refundPage.getRefundAmountBreakdown(currency));
        if (refundPage.getRefundFeeBreakdown(currency).isPresent()) amounts.addAll(refundPage.getRefundFeeBreakdown(currency).get());
        amounts.addAll(refundPage.getRefundTotalBreakdown(currency));
        Assertion.assertThat("Refund breakdown rows should be present", amounts.isEmpty(), is(false));
        for (String amount : amounts) {
            Assertion.assertThat("Refund breakdown row amount should not be empty", amount, not(emptyOrNullString()));
        }
        Assertion.assertThat("Total breakdown refund same as estimation", refundPage.getRefundTotalBreakdownSum(currency), is(String.valueOf(CoreFunctions.removeText(refundTotal))));
        refundPage.closeRefundBreakdown();

        step("Navigate back to order page to proceed with reschedule");
        orderListPage.openActiveTicketViaURL(bookedOrderID);
        orderDetailPage.skipCoachmark();

        step("Get initial total price before reschedule");
        String totalPaymentInitialOrder = orderDetailPage.getVerticalTotalPrice();

        step("Click reschedule button");
        orderDetailPage.clickRescheduleButton();

        step("Choose new dates for reschedule");
        String newStartDate = hotelReschedulePage.changeNightDuration(rescheduleDuration);

        step("Open room with additional price");
        hotelReschedulePage.openRoomWithAdditionalPrice();

        step("Verify old balance is deducted with reschedule service fee");
        roomDetailsPage.clickOnPriceDetails();
        Assertion.assertThat("Current balance should be equal with initial total price", roomDetailsPage.getCurrentBalance(true), is(totalPaymentInitialOrder));

        step("Verify reschedule service fee reduces the additional price appropriately");
        String initialTotalPrice = roomDetailsPage.getInitialTotalPrice(currency);
        String priceCalculation = summarizePrices(initialTotalPrice, roomDetailsPage.getCurrentBalance(false), currency);
        String additionalTotalPrice = roomDetailsPage.getAdditionalTotalPrice(currency);
        Assertion.assertThat("Additional total price should reflect reduction from reschedule service fee", additionalTotalPrice, is(priceCalculation));
        roomDetailsPage.closePriceDetails();

        step("Book the room and submit reschedule booking form");
        String newRoomName = roomDetailsPage.getRoomNameDetails();
        roomDetailsPage.clickBookButton();
        hotelReschedulePage.submitRescheduleBookingForm();

        step("Get reschedule order ID from payment page");
        String rescheduleOrderID = paymentPage.getOrderId();

        step("Select payment method and complete payment");
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroupSakuku("Sakuku");
        paymentPage.selectPaymentMethod("Sakuku");
        paymentPage.clickOnPaymentButton();
        Assertion.assertThat("Payment for reschedule should be successful", paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Navigate to rescheduled order");
        orderListPage.openActiveTicketViaURL(rescheduleOrderID);
        orderDetailPage.skipCoachmark();

        step("Verify rescheduled order details");
        Assertion.assertThat("Rescheduled order ID should be displayed correctly", orderDetailPage.getOrderId(), is(rescheduleOrderID));
        Assertion.assertThat("Start date should be updated to new reschedule date", orderDetailPage.getStartDate(), is(newStartDate));
        Assertion.assertThat("Hotel Name should be showing as expected: " + hotelName, orderDetailPage.isHotelNameDisplayed(hotelName), is(true));
        Assertion.assertThat("New room name should be showing as expected: " + newRoomName, orderDetailPage.isRoomNameDisplayed(newRoomName), is(true));
    }

    /**
     * PAH cancellation flow on dWeb:
     * <ol>
     *   <li>Pick a hotel that has Pay at Hotel availability from {@code HotelApi#findPayAtHotelRoom}
     *       (same source as the Appium reference {@code ACC_createHotelBookingPayAtHotelLogin}) and
     *       complete the booking end-to-end.</li>
     *   <li>Open the resulting order detail page, assert that Manage Order exposes <b>only</b>
     *       the Cancel order action (no Refund / Reschedule) and perform the cancellation.</li>
     *   <li>After cancellation, assert the Cancel order button is gone and only
     *       <b>Delete order</b> + <b>Make similar order</b> are available.</li>
     * </ol>
     * Required test data keys: {@code email, password, otp, retryCounter, currency,
     * dateRange, numberOfNights, numberOfRooms, numberOfAdults, paymentGroup, paymentMethod,
     * cardNumber, cardExpiry, cardCvv, cardHolderName}. Optional keys: {@code TwoCTwoP} (set to
     * {@code true} when the card triggers the 2C2P OTP challenge) and {@code cardOtp}
     * (the OTP value to submit on the 2C2P confirmation page).
     * Uses dWeb-only helpers (web hotel URL from the PAH summary) so it is skipped by default
     * on platforms other than DWEB via the {@link Platform} check.
     */
    @Author(names = {"Yazid", "Vipul Kumar"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Module(name = "Accommodation")
    @Test
    public void ACC_createHotelBookingPayAtHotelCancelLogin() throws Exception {
        Map<String, String> param = getParam("ACC_createHotelBookingPayAtHotelCancelLogin");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String currency = param.get("currency") != null ? param.get("currency") : "IDR";
        String dateRange = param.get("dateRange");
        int totalNight = Integer.parseInt(param.get("numberOfNights"));
        int totalRoom = Integer.parseInt(param.get("numberOfRooms"));
        int totalAdult = Integer.parseInt(param.get("numberOfAdults"));
        String cardNumber = param.get("cardNumber");
        String cardExpiry = param.get("cardExpiry");
        String cardCvv = param.get("cardCvv");
        String cardHolderName = param.get("cardHolderName");

        step("User opens home page");
        homePage.goToHomePage();

        step("Login as " + email + " via session injection");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User selects currency");
        homePage.selectCurrency(currency);

        step("Find a Pay at Hotel PDP and open it via the PAH summary URL");
        PayAtHotelRoom selectedPah = detailPage.openFirstAvailablePayAtHotelPdp(
                dateRange, totalNight, totalRoom, totalAdult);
        Assertion.assertThat(
                detailPage.getLastPayAtHotelPdpOpenFailureDetail() != null
                        ? detailPage.getLastPayAtHotelPdpOpenFailureDetail()
                        : "Pay at Hotel is available - one of the PAH Hotel items opened a PDP with an available room.",
                selectedPah, is(notNullValue()));

        step("See hotel rooms");
        detailPage.seeHotelRoom();
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list should be visible", roomListPage.isRoomListSectionVisible(), is(true));

        step("Book the Pay at Hotel rate plan");
        roomListPage.clickBookButtonOfPAHRoom();
        roomListPage.dismissRatePlanChangePopupsIfPresent();

        step("Fill primary guest details");
        bookingFormPage.selectFirstContactSalutation();
        bookingFormPage.fillGuestDetails(1, randomName() + " autoguest", true, false, "");

        step("Continue to payment page");
        bookingFormPage.continueToPaymentPage();

        step("Capture order ID from payment page");
        String orderID = paymentPage.getOrderId();
        Assertion.assertThat("Order ID should be generated for the PAH booking",
                orderID != null && !orderID.isEmpty(), is(true));

        step("Input credit/debit card details for the PAH guarantee");
        paymentPage.inputCardNumber(cardNumber);
        paymentPage.inputCardMonth(cardExpiry);
        paymentPage.inputCardCvv(cardCvv);
        paymentPage.inputCardHolderName(cardHolderName);

        step("Submit the payment");
        paymentPage.clickOnPaymentButton();

        step("Verify payment success");
        Assertion.assertThat("Payment should be successful for the PAH booking",
                paymentPage.isPaymentSuccessfulVisible(), is(true));

        step("Open the order detail page");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        step("Assert Manage Order section exposes only Cancel order for this PAH booking");
        Assertion.assertThat(
                "Cancel order button should be visible under Manage Order for PAH order " + orderID,
                orderDetailPage.isCancelOrderButtonVisible(), is(true));

        step("Cancel the PAH order via Manage Order > Cancel order");
        orderDetailPage.clickCancelOrderButton();

        step("Reopen the order detail page to re-read its state after cancellation");
        orderListPage.openActiveTicketViaURL(orderID);
        orderDetailPage.skipCoachmark();

        step("Verify cancellation was successful");
        Assertion.assertThat(
                "\"You have canceled this order\" banner should be displayed after a successful cancellation",
                orderDetailPage.isCanceledOrderNotificationBannerDisplayed(), is(true));

        step("Assert post-cancellation actions: only Delete order and Make similar order remain");
        Assertion.assertThat(
                "Cancel order button should be HIDDEN after the PAH order is canceled",
                orderDetailPage.isCancelOrderButtonVisible(), is(false));
        Assertion.assertThat(
                "Delete order button should be visible after the PAH order is canceled",
                orderDetailPage.isDeleteOrderButtonVisible(), is(true));
        Assertion.assertThat(
                "Make similar order button should be visible after the PAH order is canceled",
                orderDetailPage.isMakeSimilarOrderButtonVisible(), is(true));
    }

    @Author(names = {"Mridul"})
    @Module(name = "Pricing")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelTixPointsCashbackPriceLoginWeb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("verifyHotelTixPointsCashbackPriceLoginWeb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String email = param.get("username");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String currency = param.get("currency") != null ? param.get("currency") : "IDR";
        int totalRoom = Integer.parseInt(param.get("total_room"));
        int totalAdult = Integer.parseInt(param.get("total_adult"));
        int totalNight = Integer.parseInt(param.get("total_night"));
        String rangeAddDate = param.get("rangeAddDate");

        String[] dates = CoreFunctions.generateCheckInOutDate(rangeAddDate, totalNight);
        String checkInDate = dates[0];

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPageWeb<?> roomListPage = HotelRoomListPageWeb.getInstance(session);
        RoomDetailsPageWeb<?> roomDetailPage = RoomDetailsPageWeb.getInstance(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);

        step("Open home page and log in");
        homePage.goToHomePage();
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        homePage.selectCurrency(currency);

        step("Resolve hotel slug for " + hotelName);
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(email, password, hotelName, "HOTEL");
        int selectedIndex = hotelApi.findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        String country = hotelAutoCompleteResponse.data.contents.get(selectedIndex).country.id;
        String pdpSlug = country.toLowerCase() + "/" + hotelId;

        step("Open hotel PDP via deeplink with " + totalRoom + " room(s) and " + totalAdult + " adult(s)");
        String pdpDeepLink = buildPdpUrlWithDate(pdpSlug, checkInDate, totalNight, totalAdult, totalRoom);
        detailPage.openByUrl(pdpDeepLink);
        detailPage.clickPopUp();
        Assertion.assertThat("PDP should be loaded", detailPage.isPdpLoaded(), is(true));

        step("Open PDP and go to the Room List");
        detailPage.seeHotelRoom();
        String totalCashback = detailPage.getTotalCashback();
        double totalCashbackValue = CoreFunctions.convertPrice(totalCashback);

        step("Fetch tixpoints v2 room response and derive expected after-cashback price");
        SelectRoomResponse selectRoomResponse = hotelApi.getHotelRoomDataV2(
                hotelName, email, password, totalNight, totalRoom, totalAdult, checkInDate, currency);
        SelectRoomResponse.RateInfo rateInfo = selectRoomResponse.data.rooms.get(0).ratePlans.get(0).rateInfo;
        SelectRoomResponse.TixPointsV2 tixPointsV2 = rateInfo.tixPointsV2;
        Assertion.assertThat("API should expose a tixPointsV2 object on rateInfo", tixPointsV2, is(notNullValue()));
        Assertion.assertThat("API should advertise the displayed price as already after cashback (isPriceAfterCashback=true)",
                rateInfo.isPriceAfterCashback, is(true));

        double baseTotal = tixPointsV2.baseTotal;
        double cashbackTotal = tixPointsV2.cashbackTotal;
        // The PDP room list shows the after-cashback price EXCLUDING taxes & fees,
        // so derive the expected value from priceSummary.totalWithoutTax (not total).
        double expectedNetTotal = rateInfo.priceSummary.totalWithoutTax - (baseTotal + cashbackTotal);
        step("priceSummary.totalWithoutTax=" + rateInfo.priceSummary.totalWithoutTax
                + ", tixPointsV2.baseTotal=" + baseTotal
                + ", tixPointsV2.cashbackTotal=" + cashbackTotal
                + ", expectedNetTotal=" + expectedNetTotal);
        Assertion.assertThat("The cashback should match this formula, Total Cashback = Total base + cashback",totalCashbackValue,is(baseTotal+cashbackTotal));

        step("On Room List (PDP): verify price = priceSummary.totalWithoutTax - (tixPointsV2.baseTotal + cashbackTotal)");
        String pdpPriceText = roomListPage.getPriceAfterCashback();
        double pdpPrice = CoreFunctions.convertPrice(pdpPriceText);
        Assertion.assertThat("Price should equal priceSummary.totalWithoutTax - (tixPointsV2.baseTotal + cashbackTotal) (expected " + expectedNetTotal + ", got " + pdpPrice + ")", Math.abs(pdpPrice - expectedNetTotal), is(lessThanOrEqualTo(1.0)));

        step("Open Room Detail and verify 'you will be paying this amount' is shown in green");
        roomListPage.openRoomDetail();
        roomDetailPage.clickOnPriceDetails();
        Assertion.assertThat("You will be paying this amount' nudge should be shown in green on Room Detail", roomDetailPage.isPayingThisAmountTextInGreen(), is(true));
        roomDetailPage.closePriceDetails();

        step("Proceed to the Booking Form and verify the same green nudge is shown");
        roomDetailPage.clickBookButton();
        Assertion.assertThat("You will be paying this amount' nudge should be shown in green on Booking Form", bookingFormPage.isPayingThisAmountTextInGreen(), is(true));
    }

    /**
     * STR:
     * <ol>
     *   <li>Keep the stay dates as today (check-in) or a future date.</li>
     *   <li>Search a region/city and submit to land on the SRP.</li>
     *   <li>Scroll past the first few property cards until the "Hotels near your selected area"
     *       (Nearby Lepus) section is rendered.</li>
     * </ol>
     * Asserts:
     * <ul>
     *   <li>The "Hotels near your selected area" section is visible with at least one property card
     *       showing a price.</li>
     *   <li>Clicking the first property card redirects the user to a hotel PDP.</li>
     * </ul>
     */
    @Author(names = "Mridul")
    @Module(name = "SearchPage")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelsNearSelectedAreaCardRedirectionToPDPDweb() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("verifyHotelsNearSelectedAreaCardRedirectionToPDPDweb");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String currency = param.get("currency");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        int daysToDepartureDate = Integer.parseInt(param.get("daysToDepartureDate"));
        int daysToReturnDate = Integer.parseInt(param.get("daysToReturnDate"));
        String currentDate = CoreFunctions.getCurrentLongDate();
        String departureDate = CoreFunctions.addToLongDate(currentDate, daysToDepartureDate);
        String returnDate = CoreFunctions.addToLongDate(departureDate, daysToReturnDate);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        CalendarPageWeb<?> calendarPage = CalendarPageWeb.getInstance(session);

        step("User opens home page");
        homePage.goToHomePage();

        step("User opens as logged in user");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("User selects currency: " + currency);
        homePage.selectCurrency(currency);

        step("User opens hotel landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location: " + location);
        landingPage.searchStayLocation(location);

        step("Set stay dates (today / future): " + departureDate + " to " + returnDate);
        landingPage.clickOnCalendarWidgetToOpen();
        calendarPage.selectDate(departureDate, returnDate);

        step("Submit search and land on SRP");
        landingPage.clickSubmit();
        searchResultPage.clickPopUp();

        step("Scroll through property cards until 'Hotels near your selected area' section is visible");
        boolean isNearbyLepusVisible = searchResultPage.isNearbyLepusVisible(currency);
        Assertion.assertThat("User should see the 'Hotels near your selected area' section with property cards and prices", isNearbyLepusVisible, is(true));

        step("Click the first property card in the 'Hotels near your selected area' section");
        String selectedHotelName = searchResultPage.clickFirstNearbyLepusHotelCard();
        Assertion.assertThat("Selected hotel name should not be empty", selectedHotelName, not(emptyOrNullString()));

        step("Verify redirection to the PDP");
        detailPage.switchToNewTab();
        Assertion.assertThat("User should be redirected to the hotel PDP after clicking the card", detailPage.isPdpLoaded(), is(true));
        Assertion.assertThat("Verify the hotel name on PDP ",detailPage.getHotelNameDetail(), containsString(selectedHotelName));
    }

    @Author(names = {"Mridul"})
    @Module(name = "Wishlist")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyAddRemoveWishlistFromHotelSrpAndPdp() throws FailedLoginException, InterruptedException, ExecutionException {
        Map<String, String> param = getParam("verifyAddRemoveWishlistFromHotelSrpAndPdp");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String location = param.get("location");
        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");
        int retryCounter = Integer.parseInt(param.get("retryCounter"));

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        HotelLandingPageWeb<?> landingPage = HotelLandingPageWeb.getInstance(session);
        HotelSearchResultPage<?> searchResultPage = new HotelSearchResultPage<>(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        WishlistLandingWeb<?> wishlistPage = WishlistLandingWeb.getInstance(session);

        step("User opens home page (not logged in)");
        homePage.goToHomePage();
        loginPage.logoutViaUrl();

        step("User opens hotel landing page");
        homePage.openVerticalPageViaURL("Hotel");

        step("Search stay location: " + location);
        landingPage.searchStayLocation(location);

        step("Submit search and land on SRP");
        landingPage.clickSubmit();
        searchResultPage.clickPopUp();

        step("Click heart icon on hotel card as guest user");
        String hotelNameOnSrp = searchResultPage.getHotelName();
        searchResultPage.clickHeartIconOnHotelCard(hotelNameOnSrp);
        Assertion.assertThat("Guest user clicking the wishlist icon should be redirected to the login form", loginPage.isLoginPageOpened() || loginPage.isLoginV2PageOpened(), is(true));

        step("Login as " + email);
        loginPage.loginIntoWebFromDifferentPage(email, password, otp, retryCounter);

        step("Re-open hotel SRP as logged-in user");
        searchResultPage.clickPopUp();
        Assertion.assertThat("SRP is visible", searchResultPage.isHotelCardVisible(), is(true));

        step("Check heart icon on first hotel card is wishlist");
        Assertion.assertThat("Heart icon on SRP card should turn red (active) after adding hotel to wishlist", searchResultPage.isHeartIconActiveOnHotelCard(hotelNameOnSrp), is(true));

        step("Open PDP for the same hotel from SRP");
        searchResultPage.selectHotelCard();
        detailPage.switchToNewTab();
        Assertion.assertThat("Hotel name on PDP should match the hotel selected from SRP", detailPage.getHotelNameDetail(), is(hotelNameOnSrp));
        Assertion.assertThat("Heart icon on PDP should be red (active) — wishlist state synced from SRP", detailPage.isHeartIconColouredRed(), is(true));

        step("Click heart icon on PDP to remove hotel from wishlist");
        wishlistPage.clickWishlistHeartIcon();
        Assertion.assertThat("Heart icon on PDP should turn gray (inactive) after removing from wishlist", detailPage.isHeartIconColouredGrey(), is(true));

        step("Close PDP tab and switch back to SRP");
        detailPage.backToSrp();
        Assertion.assertThat("User is on SRP page", searchResultPage.getCurrentUrl(), containsString("/hotel/search"));
        Assertion.assertThat("Heart icon on SRP card should be gray (inactive) — removal reflected from PDP", searchResultPage.isHeartIconActiveOnHotelCard(hotelNameOnSrp), is(false));
    }

    @Author(names = {"Abhishek Sharma", "Bikash kumar jha"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "ViewOrder")
    @Test
    public void verifyEditBookingChangeTravelDate(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyEditBookingChangeTravelDate");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);

        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        SearchBookingDashboardPage<?> searchBookingDashboardPage = new SearchBookingDashboardPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String startDate = generateRandomDate(param.get("dateRange"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = Optional.ofNullable(param.get("currency")).orElse("IDR");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create a issued hotel order via API");
        Boolean apiResult = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, false, false, "",
                "en", false);
        Assertion.assertThat("Hotel booking via API is successful", apiResult, is(true));

        step("Get data from the issued order");
        String issuedOrderId = String.valueOf(hotelApi.getOrderId());

        if (issuedOrderId == null || issuedOrderId.isEmpty() || "0".equals(issuedOrderId)) {
            Assertion.skip("Issued order ID is invalid after API booking; skipping UI verification.");
        }

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Check Member Admin successful");
        boolean isLoggedInMemberAdmin = loginMemberAdmin.isUserLoggedIn(adminEmail,adminPassword);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", isLoggedInMemberAdmin, is(true));

        step("Now to verify using method 1 Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(param.get("adminOrderDetailUrl"));
        Assertion.assertThat("Is Search Order Detail Box Loaded",viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(issuedOrderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(issuedOrderId));
        Assertion.assertThat("Itinerary Id is Present",viewOrderDashboardPage.isItineraryIdLoaded(),is(true));
        Assertion.assertThat("Date purchased is Present",viewOrderDashboardPage.isDatePurchasedLoaded(),is(true));
        Assertion.assertThat("Product Type is Present",viewOrderDashboardPage.isProductTypeLoaded(),is(true));
        Assertion.assertThat("Order Detail is Present",viewOrderDashboardPage.isOrderDetailLoaded(),is(true));
        Assertion.assertThat("Passenger Detail is Present",viewOrderDashboardPage.isPassengerDetailLoaded(),is(true));
        Assertion.assertThat("Order Status is Present",viewOrderDashboardPage.isOrderStatusLoaded(),is(true));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));

        step("Open Edit Booking Page");
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded",viewOrderDashboardPage.isMoreActionBoxLoaded(),is(true));
        Assertion.assertThat("Edit Booking is visible",viewOrderDashboardPage.isEditBookingVisible(),containsString("Edit Booking"));
        viewOrderDashboardPage.clickEditBookingButton();
        Assertion.assertThat("Edit Booking Page is Visible",viewOrderDashboardPage.isEditBookingPageLoaded(),is(true));

        step("Change Travel Date and verify");
        LocalDate checkInDate = viewOrderDashboardPage.changeTravelDateSelectCheckInDate();
        int k = generateRandomInt(param.get("newCheckInDateIncreaseRange"));
        LocalDate updatedCheckInDate = viewOrderDashboardPage.changeTravelDateUpdateCheckInDate(k, checkInDate);
        LocalDate updatedCheckOutDate = viewOrderDashboardPage.changeTravelDateSelectCheckOutDate();
        long daysBetween = ChronoUnit.DAYS.between(updatedCheckInDate, updatedCheckOutDate);

        Assertion.assertThat("Date gap verification Expected offset: %s days Calculated offset: %d days".formatted(param.get("night"), daysBetween),
                daysBetween, is(Long.parseLong(param.get("night"))));

        step("Select Random Reason");
        viewOrderDashboardPage.changeTravelDateSelectRandomReason();

        step("Fill Description");
        viewOrderDashboardPage.changeTravelDateFillDescription(generateRandomString(10));

        step("Click and verify checkbox");
        boolean checkIfChangeTravelDateCheckBoxClickableOrNot = viewOrderDashboardPage.checkIfChangeTravelDateCheckBoxClickableOrNot();
        Assertion.assertThat("Checkbox is clickable", checkIfChangeTravelDateCheckBoxClickableOrNot, is(true));
        viewOrderDashboardPage.clickChangeTravelDateCheckBox();

        step("Click and verify submit button");
        boolean clickChangeTravelDateSubmitButton = viewOrderDashboardPage.clickChangeTravelDateSubmitButton();
        Assertion.assertThat("Submit button is clicked", clickChangeTravelDateSubmitButton, is(true));

        step("Verify popup appears");
        boolean isPopUpLoaded = viewOrderDashboardPage.isModalLoadedWhenChangeTravelDate();
        Assertion.assertThat("Verify popup is loaded", isPopUpLoaded, is(true));

        String modalTitle = viewOrderDashboardPage.getModalTitleWhenChangeTravelDate();
        String modalDescription = viewOrderDashboardPage.getModalDescriptionWhenChangeTravelDate();
        Assertion.assertThat("Verify Modal Title", modalTitle, equalTo("The travel date have been changed"));
        Assertion.assertThat("Verify Modal Description", modalDescription, equalTo("The new travel date will be updated on the order details."));

        boolean isCloseButtonClickableWhenChangeTravelDate = viewOrderDashboardPage.isCloseButtonClickableWhenChangeTravelDate();
        boolean isOkButtonClickableWhenChangeTravelDate = viewOrderDashboardPage.isOkButtonClickableWhenChangeTravelDate();

        Assertion.assertThat("Verify Close Button is clickable", isCloseButtonClickableWhenChangeTravelDate, is(true));
        Assertion.assertThat("Verify Ok Button is clickable", isOkButtonClickableWhenChangeTravelDate, is(true));
        viewOrderDashboardPage.clickOkButtonWhenChangeTravelDate();

        LocalDate finalCheckInDate = viewOrderDashboardPage.changeTravelDateSelectCheckInDate();
        LocalDate finalCheckOutDate = viewOrderDashboardPage.changeTravelDateSelectCheckOutDate();
        step("Verify travel date is updated : " + finalCheckInDate + " - " + finalCheckOutDate);
        Assertion.assertThat("Verify travel date is updated",
                updatedCheckInDate.equals(finalCheckInDate) && updatedCheckOutDate.equals(finalCheckOutDate)
                , is(true));


        step("Now to verify using method 2 go to Search booking dashboard page");
        loginMemberAdmin.goToDashboard(param.get("adminSearchBookingUrl"));

        step("Input Order Id");
        searchBookingDashboardPage.setInputOrderId(issuedOrderId);

        step("Click Search");
        searchBookingDashboardPage.clickSearch();

        step("Verify Booking details are shown");
        Assertion.assertThat("Order Detail Box is Loaded",searchBookingDashboardPage.isOrderDetailTableLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",searchBookingDashboardPage.areColumnValuesNotEmpty("Order ID"), is(true));
        Assertion.assertThat("Order Detail Id is Present",searchBookingDashboardPage.areColumnValuesNotEmpty("Order Detail ID"), is(true));
        Assertion.assertThat("Itinerary Id is Present",searchBookingDashboardPage.areColumnValuesNotEmpty("Itinerary ID"),is(true));
        Assertion.assertThat("Status is Present",searchBookingDashboardPage.areColumnValuesNotEmpty("Status"),is(true));
        Assertion.assertThat("Order Status is Present",searchBookingDashboardPage.areColumnValuesNotEmpty("Order Status"),is(true));
        Assertion.assertThat("Action column is Present",searchBookingDashboardPage.isActionIconLoaded(),is(true));

        step("Click Action button");
        searchBookingDashboardPage.clickActionIcon();

        step("Click reschedule button in dropdown");
        searchBookingDashboardPage.clickRescheduleButton();

        step("Check reschedule popup is loaded");
        boolean checkReschedulePopupIsLoaded = searchBookingDashboardPage.checkReschedulePopupIsLoadedAndVerifyHeading("Reschedule");
        Assertion.assertThat("Check reschedule popup is loaded and verify heading",
                checkReschedulePopupIsLoaded, is(true));

        step("Change Travel Date and verify");
        checkInDate = searchBookingDashboardPage.changeTravelDateSelectCheckInDate();
        k = generateRandomInt(param.get("newCheckInDateIncreaseRange"));
        updatedCheckInDate = searchBookingDashboardPage.changeTravelDateUpdateCheckInDate(k, checkInDate);
        updatedCheckOutDate = searchBookingDashboardPage.changeTravelDateSelectCheckOutDate();
        daysBetween = ChronoUnit.DAYS.between(updatedCheckInDate, updatedCheckOutDate);

        Assertion.assertThat("Date gap verification Expected offset: %s days Calculated offset: %d days".formatted(param.get("night"), daysBetween),
                daysBetween, is(Long.parseLong(param.get("night"))));

        step("Select Random Reason");
        searchBookingDashboardPage.changeTravelDateSelectRandomReason();

        step("Fill Description");
        searchBookingDashboardPage.changeTravelDateFillDescription(generateRandomString(10));

        step("Click and verify checkbox");
        checkIfChangeTravelDateCheckBoxClickableOrNot = searchBookingDashboardPage.checkIfChangeTravelDateCheckBoxClickableOrNot();
        Assertion.assertThat("Checkbox is clickable", checkIfChangeTravelDateCheckBoxClickableOrNot, is(true));
        searchBookingDashboardPage.clickChangeTravelDateCheckBox();

        step("Click and verify submit button");
        clickChangeTravelDateSubmitButton = searchBookingDashboardPage.clickChangeTravelDateSubmitButton();
        Assertion.assertThat("Submit button is clicked", clickChangeTravelDateSubmitButton, is(true));

        step("Verify popup appears");
        boolean isModalLoadedWhenChangeTravelDate = searchBookingDashboardPage.isModalLoadedWhenChangeTravelDate();
        Assertion.assertThat("Verify popup is loaded", isModalLoadedWhenChangeTravelDate, is(true));

        modalTitle = searchBookingDashboardPage.getModalTitleWhenChangeTravelDate();
        modalDescription = searchBookingDashboardPage.getModalDescriptionWhenChangeTravelDate();
        Assertion.assertThat("Verify Modal Title", modalTitle, equalTo("Submit Reschedule Request"));
        Assertion.assertThat("Verify Modal Description", modalDescription, equalTo("Please ensure the new stay dates are correct. Once submitted, they cannot be changed."));

        boolean isPopupCancelButtonClickableWhenChangeTravelDate = searchBookingDashboardPage.isPopupCancelButtonClickableWhenChangeTravelDate();
        boolean isPopupSubmitButtonClickableWhenChangeTravelDate = searchBookingDashboardPage.isPopupSubmitButtonClickableWhenChangeTravelDate();

        Assertion.assertThat("Verify Close Button is clickable", isPopupCancelButtonClickableWhenChangeTravelDate, is(true));
        Assertion.assertThat("Verify Ok Button is clickable", isPopupSubmitButtonClickableWhenChangeTravelDate, is(true));
        searchBookingDashboardPage.clickPopupSubmitButtonWhenChangeTravelDate();

        String updatedCheckInCheckOutDate = searchBookingDashboardPage.customFormatDate(updatedCheckInDate) + " — " + searchBookingDashboardPage.customFormatDate(updatedCheckOutDate);

        step("Verify travel date is updated: " + updatedCheckInCheckOutDate);
        loginMemberAdmin.goToDashboard(param.get("adminSearchBookingUrl"));
        searchBookingDashboardPage.setInputOrderId(issuedOrderId);
        searchBookingDashboardPage.clickSearch();

        Assertion.assertThat("Check-in & Check-out Date column visible and non-empty",
                searchBookingDashboardPage.areColumnValuesNotEmpty("Check-in & Check-out Date"),
                is(true));

        boolean checkInCheckOutDate = searchBookingDashboardPage.getTableValueByHeading("Check-in & Check-out Date");
        Assertion.assertThat("Verify Check-in & Check-out Date column is present", checkInCheckOutDate, is(true));
        boolean checkInCheckOutRow = searchBookingDashboardPage.getTableValueByRow();
        Assertion.assertThat("Verify Date is updated", checkInCheckOutRow, is(true));
   }
  
  
    @Author(names = {"Mridul", "Manan Kumar"})
    @Module(name = "SEO")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyHotelSeoPageSidePanelAndFaq() throws InterruptedException, FailedLoginException {
        Map<String, String> param = getParam("verifyHotelSeoPageSidePanelAndFaq");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        String username = param.get("username");
        String password = param.get("password");
        String regionSlug = param.get("regionSlug");
        String regionName = param.get("regionName");
        String citySlug = param.get("citySlug");
        String cityName = param.get("cityName");
        String areaSlug = param.get("areaSlug");
        String areaName = param.get("areaName");
        String lang = param.get("lang");

        HotelSeoPage<?> seoPage = HotelSeoPage.getInstance(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("Open home page");
        homePage.goToHomePage();

        step("Login using cookies for user: " + username);
        loginPage.loginWithSpecificAccountViaCookie(username, password);

        step("Select site language: " + lang);
        homePage.selectLanguage(lang);

        step("Open region SEO page: " + regionSlug);
        seoPage.openSeoPage(regionSlug);

        Assertion.assertThat("Page title should be correct",seoPage.getPageHeading(),containsString(regionName));
        Assertion.assertThat("SEO side panel should be visible on the region page", seoPage.isSidePanelVisible(), is(true));
        Assertion.assertThat("'Hotels in " + regionName + "' (cities list) panel should be visible on the region SEO page", seoPage.isHotelsInSectionVisible(regionName), is(true));
        Assertion.assertThat("Region's 'Hotels in " + regionName + "' panel should expose child city links", seoPage.getHotelsInSectionLinkCount(regionName), greaterThan(0));
        Assertion.assertThat("Landmarks side panel should be visible on the region SEO page", seoPage.isLandmarksSectionVisible(), is(true));
        Assertion.assertThat("Star-rating side panel should be visible on the region SEO page", seoPage.isStarRatingSectionVisible(), is(true));
        Assertion.assertThat("FAQ section should be visible on the region SEO page", seoPage.isFaqVisible(), is(true));
        Assertion.assertThat("FAQ should contain at least one question on the region SEO page", seoPage.getFaqItemCount(), greaterThan(0));
        
        step("FAQ questions on the region SEO page (lang=" + lang + ", expected name='" + regionName + "'): " + seoPage.getFaqItemTexts());
        Assertion.assertThat("Prod-Bug: FAQ region name should be displayed in the '" + lang + "' language matching the selected site language on the region SEO page",
                seoPage.isLocationNameShownInFaq(regionName), is(true));

        step("Click first city link under 'Hotels in " + regionName + "' panel and verify redirection to a city SEO page");
        String regionPanelUrlBefore = seoPage.getUrl();
        String clickedCityText = seoPage.clickFirstLinkInHotelsInSection(regionName);
        Assertion.assertThat("Page heading after redirection should reflect the clicked city link text", seoPage.getPageHeading(), containsStringIgnoringCase(clickedCityText));
        String urlAfterRegionCityClick = seoPage.getUrl();
        Assertion.assertThat("Clicking a city link in the region's side panel should navigate away from the region page", urlAfterRegionCityClick, not(equalTo(regionPanelUrlBefore)));
        Assertion.assertThat("After clicking the city link the user should still be on a /hotel/ SEO page", urlAfterRegionCityClick, containsString("/hotel/"));

        step("Open city SEO page: " + citySlug);
        seoPage.openSeoPage(citySlug);

        Assertion.assertThat("Page title should be correct",seoPage.getPageHeading(),containsString(cityName));
        Assertion.assertThat("SEO side panel should be visible on the city page", seoPage.isSidePanelVisible(), is(true));
        Assertion.assertThat("'Hotels in " + cityName + "' (areas list) panel should be visible on the city SEO page", seoPage.isHotelsInSectionVisible(cityName), is(true));
        Assertion.assertThat("City's 'Hotels in " + cityName + "' panel should expose child area links", seoPage.getHotelsInSectionLinkCount(cityName), greaterThan(0));
        Assertion.assertThat("Landmarks side panel should be visible on the city SEO page", seoPage.isLandmarksSectionVisible(), is(true));
        Assertion.assertThat("Star-rating side panel should be visible on the city SEO page", seoPage.isStarRatingSectionVisible(), is(true));
        Assertion.assertThat("FAQ section should be visible on the city SEO page", seoPage.isFaqVisible(), is(true));
        
        step("FAQ questions on the city SEO page (lang=" + lang + ", expected name='" + cityName + "'): " + seoPage.getFaqItemTexts());
        Assertion.assertThat("Prod-Bug: FAQ city name should be displayed in the '" + lang + "' language matching the selected site language on the city SEO page",
                seoPage.isLocationNameShownInFaq(cityName), is(true));

        step("Click first area link under 'Hotels in " + cityName + "' panel and verify redirection to an area SEO page");
        String cityPanelUrlBefore = seoPage.getUrl();
        String clickedAreaText = seoPage.clickFirstLinkInHotelsInSection(cityName);
        Assertion.assertThat("Page heading after redirection should reflect the clicked area link text", seoPage.getPageHeading(), containsStringIgnoringCase(clickedAreaText));
        String urlAfterCityAreaClick = seoPage.getUrl();
        Assertion.assertThat("Clicking an area link in the city's side panel should navigate away from the city page", urlAfterCityAreaClick, not(equalTo(cityPanelUrlBefore)));
        Assertion.assertThat("After clicking the area link the user should still be on a /hotel/ SEO page", urlAfterCityAreaClick, containsString("/hotel/"));

        step("Open area SEO page: " + areaSlug);
        seoPage.openSeoPage(areaSlug);

        Assertion.assertThat("Page title should be correct",seoPage.getPageHeading(),containsString(areaName));
        Assertion.assertThat("SEO side panel should be visible on the area page", seoPage.isSidePanelVisible(), is(true));
        Assertion.assertThat("'Hotels in " + cityName + "' (areas list) panel should NOT be visible on the city SEO page", seoPage.isHotelsInSectionVisible(cityName), is(false));
        Assertion.assertThat("Landmarks side panel should be visible on the area SEO page", seoPage.isLandmarksSectionVisible(), is(true));
        Assertion.assertThat("Star-rating side panel should be visible on the area SEO page", seoPage.isStarRatingSectionVisible(), is(true));
        Assertion.assertThat("FAQ section should be visible on the area SEO page", seoPage.isFaqVisible(), is(true));
        
        step("FAQ questions on the area SEO page (lang=" + lang + ", expected name='" + areaName + "'): " + seoPage.getFaqItemTexts());
        Assertion.assertThat("Prod-Bug: FAQ area name should be displayed in the '" + lang + "' language matching the selected site language on the area SEO page",
                seoPage.isLocationNameShownInFaq(areaName), is(true));

        step("Click first star-rating link and verify redirection stays inside a /hotel/ SEO page");
        String areaPanelUrlBefore = seoPage.getUrl();
        String clickedStarText = seoPage.clickFirstStarRatingLink();
        clickedStarText = clickedStarText.replace('-',' ');
        Assertion.assertThat("Page heading after redirection should reflect the clicked star-rating link text", seoPage.getPageHeading().toLowerCase(), containsStringIgnoringCase(clickedStarText.toLowerCase()));
        String urlAfterStarClick = seoPage.getUrl();
        Assertion.assertThat("Star-rating link should navigate away from the area SEO page", urlAfterStarClick, not(equalTo(areaPanelUrlBefore)));
        Assertion.assertThat("Star-rating side-panel link should redirect to a /hotel/ SEO URL", urlAfterStarClick, containsString("/hotel/"));
        Assertion.assertThat("Star-rating side-panel link should redirect to a /star/ SEO URL" , urlAfterStarClick,containsString("star"));
        String firstCardStarRating = seoPage.getFirstSeoCardStarRating()+"";
        Assertion.assertThat("First hotel card on the star-rating SEO page should show a valid star rating as we clicked ", clickedStarText.contains(firstCardStarRating), is(true));

        step("Re-open area SEO page and click first landmark link to verify redirection");
        seoPage.openSeoPage(areaSlug);
        String beforeLandmarkClick = seoPage.getUrl();
        String clickedLandmarkText = seoPage.clickFirstLandmarkLink();
        Assertion.assertThat("Page heading after redirection should reflect the clicked landmark link text", seoPage.getPageHeading().toLowerCase(), containsStringIgnoringCase(clickedLandmarkText.toLowerCase()));
        String urlAfterLandmarkClick = seoPage.getUrl();
        Assertion.assertThat("Landmark link should navigate away from the area SEO page", urlAfterLandmarkClick, not(equalTo(beforeLandmarkClick)));
        Assertion.assertThat("Landmark side-panel link should redirect to a /hotel/ SEO URL", urlAfterLandmarkClick, containsString("/hotel/"));
        Assertion.assertThat("Landmark side-panel link should redirect to a /Landmark/ SEO URL" , urlAfterLandmarkClick,containsString("landmark"));
    }

    /**
     * Asserts the captured popup text contains the expected fixed phrase in either English or Indonesian
     * (case-insensitive). Price amounts vary per run, so only the static phrase is matched.
     */
    private static void assertPopupTextContains(String description, String actualText, String englishPhrase, String indonesianPhrase) {
        String normalized = actualText == null ? "" : actualText.toLowerCase();
        boolean matched = normalized.contains(englishPhrase.toLowerCase()) || normalized.contains(indonesianPhrase.toLowerCase());
        Assertion.assertThat(description + " | actual popup text: '" + actualText + "'", matched, is(true));
    }

    /**
     * Builds the full PDP deep link from a hotel slug, using the environment base URL and the
     * data-driven stay date so the PDP and the allotment API always use the same date.
     */
    private static String buildPdpUrlWithDate(String slug, String checkin, int night, int adult, int room) {
        String baseUrl = switch (Configs.ENVIRONMENT) {
            case GK -> "https://gatotkaca.tiket.com/";
            case PROD -> "https://tiket.com/";
            default -> "https://preprod.tiket.com/";
        };
        String checkout = LocalDate.parse(checkin).plusDays(night).toString();
        return baseUrl + "hotel/" + slug + "?checkin=" + checkin + "&checkout=" + checkout
                + "&night=" + night + "&adult=" + adult + "&room=" + room;
    }

    @Author(names = {"Mridul", "Vipul Kumar"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "rates-and-availability")
    @Test
    public void verifyAllotmentRateChangePopupsDuringBookingFlow() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyAllotmentRateChangePopupsDuringBookingFlow");
        BaseTest.paramsThreadLocal.set(param);

        // Consumer booking-flow data
        String pdpSlug = param.get("pdpSlug");
        String email = param.get("email");
        String password = param.get("password");
        String dateRange = param.get("dateRange");
        int guests = Integer.parseInt(param.get("guests"));
        int rooms = Integer.parseInt(param.get("rooms"));
        String fullName = param.get("fullName");
        String currency = param.get("currency");
        String phone = "81" + CoreFunctions.randomInt(9);
        int night = Integer.parseInt(param.get("night"));

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

        String date = generateRandomDate(dateRange);
        String pdpDeepLink = buildPdpUrlWithDate(pdpSlug, date, night, guests, rooms);

        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        HotelBookingFormPageDWeb<?> bookingFormPageDWeb = (HotelBookingFormPageDWeb<?>) bookingFormPage;
        PaymentPage<?> paymentPage = new PaymentPage<>(session);

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
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, date,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price1, false,
                newCancellationCharge, newCancellationNoShow);

        step("User opens home page and logs in");
        homePage.goToHomePage();
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        homePage.selectCurrency(currency);

        step("Open hotel PDP via deep link");
        detailPage.openByUrl(pdpDeepLink);
        detailPage.clickPopUp();
        Assertion.assertThat("PDP should be loaded", detailPage.isPdpLoaded(), is(true));

        step("Go to room list and wait so the loaded rate is cached");
        detailPage.seeHotelRoom();
        roomListPage.scrollToRoomListSection();
        Assertion.assertThat("Room list section should be visible", roomListPage.isRoomListSectionVisible(), is(true));
        String roomListPrice = roomListPage.getRoomPrice();

        step("Change allotment rate from 100 to 200 (cancellation policy still false)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, date,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price2, false,
                newCancellationCharge, newCancellationNoShow);

        step("Book a room to open booking form page 1");
        detailPage.clickPopUp();
        roomListPage.clickBookButton();

        step("Verify the price-change snackbar text appears on booking form page 1");
        String bf1SnackbarText = bookingFormPage.detectAndDismissPriceChangeSnackbar();
        assertPopupTextContains("Price-change snackbar should appear on booking form page 1 after rate changed 100 -> 200",
                bf1SnackbarText, "the total price has changed", "total harga berubah");
        String bookingFormPrice = bookingFormPage.getBookingPrice();

        step("Fill contact and guest details on booking form page 1");
        bookingFormPage.fillContactDetail(fullName, phone, email);
        bookingFormPage.fillGuestDetails(guests, fullName, true, false, "");

        step("Continue to booking form page 2");
        bookingFormPage.continueToNextPage();

        step("Change allotment rate from 200 to 300 (cancellation policy still false)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, date,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price3, false,
                newCancellationCharge, newCancellationNoShow);

        step("Continue to payment and verify the price-update popup text appears");
        String payment1PopupText = bookingFormPageDWeb.continueToPaymentPageDetectingRatePlanPopup();
        assertPopupTextContains("Price-update popup should appear when going to payment after rate changed 200 -> 300",
                payment1PopupText, "The price has been updated", "Harga telah diperbarui");
        Assertion.assertThat("Payment page should load after dismissing the popup", paymentPage.isPaymentPageLoaded(), is(true));

        step("Verify the price increases across room list, booking form and payment page");
        String paymentPrice = paymentPage.getFinalPrice();
        assertPriceIncreasing("Phase 1", roomListPrice, bookingFormPrice, paymentPrice);

        // ============================ PHASE 2: cancellation policy = true ============================
        step("Go back to home page");
        homePage.goToHomePage();

        step("Set allotment rate to 100 with cancellation policy = true (refundable, charge " + newCancellationCharge + "%)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, date,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price1, true,
                newCancellationCharge, newCancellationNoShow);

        step("Reopen the same hotel PDP via deep link");
        detailPage.openByUrl(pdpDeepLink);

        step("Go to room list");
        Assertion.assertThat("PDP should be loaded in phase 2", detailPage.isPdpLoaded(), is(true));
        detailPage.seeHotelRoom();
        roomListPage.scrollToRoomListSection();
        String roomListPrice2 = roomListPage.getRoomPrice();

        step("Change allotment rate from 100 to 200 (cancellation policy still true)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, date,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price2, true,
                newCancellationCharge, newCancellationNoShow);


        step("Book a room and verify the cancellation-policy change dialog text appears before booking form");
        detailPage.clickPopUp();
        roomListPage.clickBookButton();
        String bf1DialogText = bookingFormPage.detectAndDismissPriceChangeSnackbar();
        assertPopupTextContains(
                "Price change notification should appear before booking form in phase 2", bf1DialogText,
                "The total price has changed",
                "Total harga telah berubah");
        String bookingFormPrice2 = bookingFormPage.getBookingPrice();

        step("Fill contact and guest details on booking form page 1");
        bookingFormPage.fillContactDetail(fullName, phone, email);
        bookingFormPage.fillGuestDetails(guests, fullName, true, false, "");

        step("Update allotment rate to 300 with cancellation charges (cancellation policy = true)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, date,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price3, true,
                newCancellationCharge, newCancellationNoShow);

        step("Continue to booking form page 2");
        bookingFormPage.continueToNextPage();

        step("Continue to payment and verify the price-update popup text appears (phase 2)");
        String payment2PopupText = bookingFormPageDWeb.continueToPaymentPageDetectingRatePlanPopup();
        assertPopupTextContains("Price-update popup should appear when going to payment in phase 2",
                payment2PopupText, "The price has been updated", "Harga telah diperbarui");
        Assertion.assertThat("Payment page should load after dismissing the popup in phase 2",
                paymentPage.isPaymentPageLoaded(), is(true));

        step("Verify the price increases across room list, booking form and payment page (phase 2)");
        String paymentPrice2 = paymentPage.getFinalPrice();
        assertPriceIncreasing("Phase 2", roomListPrice2, bookingFormPrice2, paymentPrice2);

        // -- flow 3
        step("Go back to home page");
        homePage.goToHomePage();

        step("Set allotment rate to 100 with cancellation policy = true (refundable, charge " + newCancellationCharge + "%)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, date,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price1, true,
                newCancellationCharge, newCancellationNoShow);

        step("Reopen the same hotel PDP via deep link");
        detailPage.openByUrl(pdpDeepLink);

        step("Go to room list");
        Assertion.assertThat("PDP should be loaded in phase 2", detailPage.isPdpLoaded(), is(true));
        detailPage.seeHotelRoom();
        roomListPage.scrollToRoomListSection();
        String roomListPrice3old = roomListPage.getRoomPrice();
        Thread.sleep(3000);

        step("Change allotment rate from 100 to 200 (cancellation policy still true)");
        hotelApi.updateHotelAllotmentApi(apiToken, apiCookie, apiUser, apiAccountId, hotelId, date,
                allotmentId, roomId, roomName, roomRateId, ratePlanId, price2, true,
                newCancellationCharge, newCancellationNoShow);


        step("Book a room and verify the cancellation-policy change dialog text appears before booking form");
        detailPage.clickPopUp();
        roomListPage.clickBookButton();
        String bf2DialogText = bookingFormPage.getPriceChangeSnackbarText();

        assertPopupTextContains("Cancellation-policy change dialog should appear before booking form in phase 2",
                bf2DialogText, "The total price has changed", "Total harga telah berubah");

        step("Use 'See other rooms' to go back to the room list and verify the price increased");
        detailPage.openByUrl(pdpDeepLink);
        detailPage.seeHotelRoom();
        roomListPage.scrollToRoomListSection();
        String roomListPriceAfterRateIncrease = roomListPage.getRoomPrice();
        Assertion.assertThat("Room list price after rate increase (" + parsePriceToNumber(roomListPriceAfterRateIncrease)
                        + ") should be greater than the initial room list price (" + parsePriceToNumber(roomListPrice3old) + ")",
                parsePriceToNumber(roomListPriceAfterRateIncrease) > parsePriceToNumber(roomListPrice3old), is(true));
    }
    @Author(names = {"Abhishek Sharma"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Demand")
    @Module(name = "FailedCheckinDashboard")
    @Test
    public void verifyFailedCheckInDashboardSearchAndValidation(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyFailedCheckInDashboardSearchAndValidation");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        FailedCheckinDashboardPage<?> failedCheckinDashboardPage = new FailedCheckinDashboardPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String startDate = generateRandomDate(param.get("dateRange"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = Optional.ofNullable(param.get("currency")).orElse("IDR");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create a issued hotel order via API");
        Boolean apiResult = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, false, false, "",
                "en", false);
        Assertion.assertThat("Hotel booking via API is successful", apiResult, is(true));

        step("Get data from the issued order");
        String issuedOrderId = String.valueOf(hotelApi.getOrderId());
        if (issuedOrderId == null || issuedOrderId.isEmpty() || "0".equals(issuedOrderId)) {
            Assertion.skip("Issued order ID is invalid after API booking; skipping UI verification.");
        }

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Check Member Admin successful");
        boolean isLoggedInMemberAdmin = loginMemberAdmin.isUserLoggedIn(adminEmail,adminPassword);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", isLoggedInMemberAdmin, is(true));

        step("Now go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(param.get("adminOrderDetailUrl"));
        Assertion.assertThat("Is Search Order Detail Box Loaded",viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(issuedOrderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(issuedOrderId));
        Assertion.assertThat("Itinerary Id is Present",viewOrderDashboardPage.isItineraryIdLoaded(),is(true));
        Assertion.assertThat("Date purchased is Present",viewOrderDashboardPage.isDatePurchasedLoaded(),is(true));
        Assertion.assertThat("Product Type is Present",viewOrderDashboardPage.isProductTypeLoaded(),is(true));
        Assertion.assertThat("Order Detail is Present",viewOrderDashboardPage.isOrderDetailLoaded(),is(true));
        Assertion.assertThat("Passenger Detail is Present",viewOrderDashboardPage.isPassengerDetailLoaded(),is(true));
        Assertion.assertThat("Order Status is Present",viewOrderDashboardPage.isOrderStatusLoaded(),is(true));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));

        step("Get data from table");
        String itineraryId = viewOrderDashboardPage.getItinid();

        step("Report hotel with order id: " + issuedOrderId + " order as failed-checkin");
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded",viewOrderDashboardPage.isMoreActionBoxLoaded(),is(true));
        Assertion.assertThat("Report Failed Checkin is visible",viewOrderDashboardPage.isReportFailedCheckinVisible(),containsString("Report Failed Checkin"));
        viewOrderDashboardPage.clickFailedCheckin();
        Assertion.assertThat("Report Failed Checkin is Visible",viewOrderDashboardPage.isReportFailedCheckInPageLoaded(),is(true));

        step("Select failed checkin category");
        viewOrderDashboardPage.clickCategoryDropdown();
        viewOrderDashboardPage.selectFailedCheckinCategory();

        step("Select failed checkin problem");
        viewOrderDashboardPage.clickCheckinProblemDropdown();
        viewOrderDashboardPage.SelectCheckinProblem();

        step("Select failed checkin reported from");
        viewOrderDashboardPage.clickReportedDropdown();
        viewOrderDashboardPage.selectReportedCategory();

        step("Select failed checkin root cause");
        viewOrderDashboardPage.clickRootCauseDropdown();
        viewOrderDashboardPage.selectRootCause();

        step("Click failed checkin continue button");
        viewOrderDashboardPage.clickContinueFailed();

        step("Now go to Hotel Failed checkin Dashboard");
        loginMemberAdmin.goToDashboard(param.get("failedCheckinDashboardUrl"));

        step("Go to Need CS tab");
        failedCheckinDashboardPage.switchFailedCheckinApprovalTab("Need CS");

        boolean isOrderIdFilterEditable = failedCheckinDashboardPage.isOrderIdFilterEditable();
        boolean isItineraryIdFilterEditable = failedCheckinDashboardPage.isItineraryIdFilterEditable();
        Assertion.assertThat("Verify Order id field is editable", isOrderIdFilterEditable, is(true));
        Assertion.assertThat("Verify Itinerary id field is editable", isItineraryIdFilterEditable, is(true));

        step("Now enter alphabets in Order id input field and search and verify");
        failedCheckinDashboardPage.setOrderIdInput(generateRandomString(5));
        failedCheckinDashboardPage.clickSearchButton();
        boolean verifyNoDataAvailable = failedCheckinDashboardPage.verifyNoDataAvailable();
        Assertion.assertThat("Verify data not available for alphabet Order id", verifyNoDataAvailable, is(true));
        failedCheckinDashboardPage.setOrderIdInput("");

        step("Now enter alphabets in Itinerary id input field and search and verify");
        failedCheckinDashboardPage.setItineraryIdInput(generateRandomString(5));
        failedCheckinDashboardPage.clickSearchButton();
        verifyNoDataAvailable = failedCheckinDashboardPage.verifyNoDataAvailable();
        Assertion.assertThat("Verify data not available for alphabet Itinerary id", verifyNoDataAvailable, is(true));
        failedCheckinDashboardPage.setItineraryIdInput("");

        step("Now enter incorrect value in Order id input field and search and verify");
        failedCheckinDashboardPage.setOrderIdInput(String.valueOf(generateRandomInt(5)));
        failedCheckinDashboardPage.clickSearchButton();
        verifyNoDataAvailable = failedCheckinDashboardPage.verifyNoDataAvailable();
        Assertion.assertThat("Verify data not available for incorrect Order id", verifyNoDataAvailable, is(true));
        failedCheckinDashboardPage.setOrderIdInput("");

        step("Now enter incorrect value in Itinerary id input field and search and verify");
        failedCheckinDashboardPage.setItineraryIdInput(String.valueOf(generateRandomInt(5)));
        failedCheckinDashboardPage.clickSearchButton();
        verifyNoDataAvailable = failedCheckinDashboardPage.verifyNoDataAvailable();
        Assertion.assertThat("Verify data not available for incorrect Itinerary id", verifyNoDataAvailable, is(true));
        failedCheckinDashboardPage.setItineraryIdInput("");

        step("Now enter correct value in Order id input field and search and verify");
        failedCheckinDashboardPage.setOrderIdInput(issuedOrderId);
        failedCheckinDashboardPage.clickSearchButton();
        String tableOrderId = failedCheckinDashboardPage.getOrderIdTable();
        Assertion.assertThat("Verify data is available for correct Order id", tableOrderId, equalTo(issuedOrderId));
        failedCheckinDashboardPage.setOrderIdInput("");

        step("Now enter correct value in Itinerary id input field and search and verify");
        failedCheckinDashboardPage.setItineraryIdInput(itineraryId);
        failedCheckinDashboardPage.clickSearchButton();
        String tableItineraryId = failedCheckinDashboardPage.getItineraryIdTable();
        Assertion.assertThat("Verify data is available for correct Itinerary id", tableItineraryId, equalTo(itineraryId));
        failedCheckinDashboardPage.setItineraryIdInput("");

        step("Go to Waiting for Approval tab");
        failedCheckinDashboardPage.switchFailedCheckinApprovalTab("Waiting for Approval");
        isOrderIdFilterEditable = failedCheckinDashboardPage.isOrderIdFilterEditable();
        isItineraryIdFilterEditable = failedCheckinDashboardPage.isItineraryIdFilterEditable();
        Assertion.assertThat("Verify Order id field is editable", isOrderIdFilterEditable, is(true));
        Assertion.assertThat("Verify Itinerary id field is editable", isItineraryIdFilterEditable, is(true));

        step("Go to Waiting MM Approval tab");
        failedCheckinDashboardPage.switchFailedCheckinApprovalTab("Waiting MM Approval");
        isOrderIdFilterEditable = failedCheckinDashboardPage.isOrderIdFilterEditable();
        isItineraryIdFilterEditable = failedCheckinDashboardPage.isItineraryIdFilterEditable();
        Assertion.assertThat("Verify Order id field is editable", isOrderIdFilterEditable, is(true));
        Assertion.assertThat("Verify Itinerary id field is editable", isItineraryIdFilterEditable, is(true));
    }
  
    @Author(names = {"Abhishek Sharma","Bikash kumar jha"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Demand")
    @Module(name = "FailedCheckinDashboard")
    @Test
    public void verifyFailedCheckInDashboardBehavior(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyFailedCheckInDashboardBehavior");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        FailedCheckinDashboardPage<?> failedCheckinDashboardPage = new FailedCheckinDashboardPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String startDate = generateRandomDate(param.get("dateRange"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = Optional.ofNullable(param.get("currency")).orElse("IDR");
        String otherSolutionSuccessPageTitle = param.get("otherSolutionSuccessPageTitle");
        String otherSolutionSuccessPageDesc = param.get("otherSolutionSuccessPageDesc");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create a issued hotel order via API");
        Boolean apiResult = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, false, false, "",
                "en", false);
        Assertion.assertThat("Hotel booking via API is successful", apiResult, is(true));

        step("Get data from the issued order");
        String issuedOrderId = String.valueOf(hotelApi.getOrderId());
        int issuedOrderIdInt = hotelApi.getOrderId();
        if (issuedOrderIdInt <= 0) {
            Assertion.skip("Issued order ID is invalid after API booking; skipping UI verification.");
        }

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Check Member Admin successful");
        boolean isLoggedInMemberAdmin = loginMemberAdmin.isUserLoggedIn(adminEmail,adminPassword);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", isLoggedInMemberAdmin, is(true));

        step("Now go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(param.get("adminOrderDetailUrl"));
        Assertion.assertThat("Is Search Order Detail Box Loaded",viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(issuedOrderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(issuedOrderId));
        Assertion.assertThat("Itinerary Id is Present",viewOrderDashboardPage.isItineraryIdLoaded(),is(true));
        Assertion.assertThat("Date purchased is Present",viewOrderDashboardPage.isDatePurchasedLoaded(),is(true));
        Assertion.assertThat("Product Type is Present",viewOrderDashboardPage.isProductTypeLoaded(),is(true));
        Assertion.assertThat("Order Detail is Present",viewOrderDashboardPage.isOrderDetailLoaded(),is(true));
        Assertion.assertThat("Passenger Detail is Present",viewOrderDashboardPage.isPassengerDetailLoaded(),is(true));
        Assertion.assertThat("Order Status is Present",viewOrderDashboardPage.isOrderStatusLoaded(),is(true));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
        Assertion.assertThat("More Action button is clickable",viewOrderDashboardPage.isActionButtonClickable(),is(true));

        step("Get data from table");
        String itineraryId = viewOrderDashboardPage.getItinid();

        step("Click More action button");
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
        viewOrderDashboardPage.clickMoreActionButton();

        Assertion.assertThat("More Action Box is Loaded",viewOrderDashboardPage.isMoreActionBoxLoaded(),is(true));
        Assertion.assertThat("Report Failed Checkin is visible",viewOrderDashboardPage.isReportFailedCheckinVisible(),containsString("Report Failed Checkin"));
        viewOrderDashboardPage.clickFailedCheckin();
        Assertion.assertThat("Report Failed Checkin is Visible",viewOrderDashboardPage.isReportFailedCheckInPageLoaded(),is(true));

        step("Check Popup Is Displayed");
        Assertion.assertThat("Confirmation Popup Box Is Loaded",failedCheckinDashboardPage.isConfirmationPopupBoxLoaded(),is(true));
        Assertion.assertThat("Category Option Is Visible",failedCheckinDashboardPage.isCategoryVisible(),is(true));
        Assertion.assertThat("CheckIn Option is Visible",failedCheckinDashboardPage.isCheckInProblemVisible(),is(true));
        Assertion.assertThat("Reported From Option is Visible",failedCheckinDashboardPage.isReportedFromVisible(),is(true));
        Assertion.assertThat("Root Cause Option is Visible",failedCheckinDashboardPage.isRootCauseVisible(),is(true));

        step("Check Dropdown List For Category");
        failedCheckinDashboardPage.selectCategory();
        Assertion.assertThat("Category Dropdown Is Visible",failedCheckinDashboardPage.isCategoryDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstCategoryOption();
        Assertion.assertThat("First Category Option is selected and visible",failedCheckinDashboardPage.isFirstCategoryOptionVisible(),containsString("Barcode Voucher Sudah Digunakan atau Tidak Berlaku"));

        step("Check Dropdown List For CheckIn-Problem");
        failedCheckinDashboardPage.selectCheckInProblem();
        Assertion.assertThat("CheckIn Problem Dropdown Is Visible",failedCheckinDashboardPage.isCheckInProblemDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstCheckInProblemOption();
        Assertion.assertThat("First CheckIn Problem Option is selected and visible",failedCheckinDashboardPage.isFirstCheckInProblemOptionVisible(),containsString("Hotel Full Book"));

        step("Check Dropdown List For Reported From");
        failedCheckinDashboardPage.selectReportedFrom();
        Assertion.assertThat("Reported From Dropdown Is Visible",failedCheckinDashboardPage.isReportedFromDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstReportedFromOption();
        Assertion.assertThat("First Reported From Option is selected and visible",failedCheckinDashboardPage.isFirstReportedFromOptionVisible(),containsString("Customer"));

        step("Check Dropdown List For Root Cause");
        failedCheckinDashboardPage.selectRootCause();
        Assertion.assertThat("Root Cause Dropdown Is Visible",failedCheckinDashboardPage.isRootCauseDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstRootCauseOption();
        Assertion.assertThat("First Root Cause Option is selected and visible",failedCheckinDashboardPage.isFirstRootCauseOptionVisible(),containsString("CM tidak connect ke Extranet"));

        step("Click failed checkin continue button");
        failedCheckinDashboardPage.clickYesContinueButton();

        step("Now go to Hotel Failed checkin Dashboard");
        loginMemberAdmin.goToDashboard(param.get("failedCheckinDashboardUrl"));

        boolean isDefaultTabNeedCS = failedCheckinDashboardPage.isDefaultTabNeedCS();
        Assertion.assertThat("Verify default tab is Need CS", isDefaultTabNeedCS, is(true));

        step("Now enter correct value in Order id input field and search and verify");
        failedCheckinDashboardPage.setOrderIdInput(issuedOrderId);

        step("click search button");
        failedCheckinDashboardPage.clickSearchButton();

        step("Verify data for Order id: " + issuedOrderId);
        Assertion.assertThat("Verify correct Order id is present", failedCheckinDashboardPage.getOrderIdTable(), equalTo(issuedOrderId));
        Assertion.assertThat("Verify correct Itinerary id is present", failedCheckinDashboardPage.getItineraryIdTable(), equalTo(itineraryId));
        Assertion.assertThat("Verify correct Ticket status is present", failedCheckinDashboardPage.getTicketStatusTable(), equalTo("Need CS"));

        step("Now click more action button");
        Assertion.assertThat("Verify click more action button is clickable", failedCheckinDashboardPage.isMoreActionButtonClickable(), is(true));
        failedCheckinDashboardPage.clickMoreActionButton();

        step("Click and Verify Other Solution Button");
        Assertion.assertThat("Verify Other Solution button is clickable", failedCheckinDashboardPage.isOtherSolutionButtonClickable(), is(true));
        failedCheckinDashboardPage.clickOtherSolutionButton();

        step("Check Other Solution Popup is Displayed");
        Assertion.assertThat("Confirmation Popup Box Is Loaded",failedCheckinDashboardPage.isConfirmationPopupBoxLoadedCS(),is(true));
        Assertion.assertThat("Why should be removed? Option Is Visible",failedCheckinDashboardPage.isWhyShouldBeRemovedVisible(),is(true));
        Assertion.assertThat("Reason Option is Visible",failedCheckinDashboardPage.isReasonVisible(),is(true));
        Assertion.assertThat("Description Text Area Option is Visible",failedCheckinDashboardPage.isDescriptionVisible(),is(true));

        step("Check Dropdown List For Why should be removed?");
        failedCheckinDashboardPage.selectWhyShouldBeRemoved();
        Assertion.assertThat("Why should be removed? Dropdown Is Visible",failedCheckinDashboardPage.isOtherSolutionDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstDropdownOption();
        Assertion.assertThat("First Why should be removed? Option is selected and visible",failedCheckinDashboardPage.isFirstWhyShouldBeRemovedOptionVisible(),containsString("REFUNDED"));

        step("Check Dropdown List For Reason");
        failedCheckinDashboardPage.selectReason();
        Assertion.assertThat("Reason Dropdown Is Visible",failedCheckinDashboardPage.isOtherSolutionDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstDropdownOption();
        Assertion.assertThat("First Reason Option is selected and visible",failedCheckinDashboardPage.isFirstReasonOptionVisible(),containsString("ISSUED"));

        step("Fill random description");
        failedCheckinDashboardPage.fillOtherSolutionDescription(generateRandomString(10));

        step("Click failed checkin continue button");
        failedCheckinDashboardPage.clickOtherSolutionPopupYesContinueButton();

        step("Verify Other Solution success page");
        Assertion.assertThat("Verify Other Solution success page appears", failedCheckinDashboardPage.isOtherSolutionSuccessPageAppears(), is(true));
        Assertion.assertThat("Verify Other Solution success page title", failedCheckinDashboardPage.isOtherSolutionSuccessTitleAppears(), containsString(otherSolutionSuccessPageTitle));
        Assertion.assertThat("Verify Other Solution success page description", failedCheckinDashboardPage.isOtherSolutionSuccessDescriptionAppears(), containsString(otherSolutionSuccessPageDesc));
    }
  
    @Author(names = {"Abhishek Sharma","Bikash kumar jha"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Demand")
    @Module(name = "ViewOrder")
    @Test
    public void verifyViewOrderUIElementsFunctionality() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyViewOrderUIElementsFunctionality");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        FailedCheckinDashboardPage<?> failedCheckinDashboardPage = new FailedCheckinDashboardPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String startDate = generateRandomDate(param.get("dateRange"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = Optional.ofNullable(param.get("currency")).orElse("IDR");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create a issued hotel order via API");
        Boolean apiResult = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, false, false, "",
                "en", false);
        Assertion.assertThat("Hotel booking via API is successful", apiResult, is(true));

        step("Get data from the issued order");
        int issuedOrderIdInt = hotelApi.getOrderId();
        String issuedOrderId = String.valueOf(issuedOrderIdInt);
        if (issuedOrderIdInt <= 0) {
            Assertion.skip("Issued order ID is invalid after API booking; skipping UI verification.");
        }

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Check Member Admin successful");
        boolean isLoggedInMemberAdmin = loginMemberAdmin.isUserLoggedIn(adminEmail,adminPassword);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", isLoggedInMemberAdmin, is(true));

        step("Now go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(param.get("adminOrderDetailUrl"));
        Assertion.assertThat("Is Search Order Detail Box Loaded",viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        Assertion.assertThat("Verify Source Dropdown is clickable", viewOrderDashboardPage.isSourceDropdownClickable(), is(true));
        viewOrderDashboardPage.clickSourceDropdown();

        Assertion.assertThat("Verify source Order ID is selectable", viewOrderDashboardPage.isSourceOrderIDSelectable(), is(true));
        Assertion.assertThat("Verify source Itinerary ID is selectable", viewOrderDashboardPage.isSourceItineraryIDSelectable(), is(true));
        Assertion.assertThat("Verify source Vendor Itinerary ID is selectable", viewOrderDashboardPage.isSourceVendorItineraryIDSelectable(), is(true));

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(issuedOrderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Verify and click Reset Button");
        Assertion.assertThat("Verify Reset Button clickable", viewOrderDashboardPage.isResetButtonClickable(), is(true));
        viewOrderDashboardPage.clickResetButton();
        Assertion.assertThat("Verify source input is reset", viewOrderDashboardPage.getSourceDropdownText(), containsString("Select Source"));
        Assertion.assertThat("Verify source id input field is reset", viewOrderDashboardPage.getSourceInputFieldText(), equalTo(""));
        Assertion.assertThat("Verify Search button is disabled", viewOrderDashboardPage.isSearchButtonDisabled(), is(true));
        step("Click Source Dropdown");
        Assertion.assertThat("Verify Source Dropdown is clickable", viewOrderDashboardPage.isSourceDropdownClickable(), is(true));
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(issuedOrderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(issuedOrderId));
        Assertion.assertThat("Itinerary Id is Present",viewOrderDashboardPage.isItineraryIdLoaded(),is(true));
        Assertion.assertThat("Date purchased is Present",viewOrderDashboardPage.isDatePurchasedLoaded(),is(true));
        Assertion.assertThat("Product Type is Present",viewOrderDashboardPage.isProductTypeLoaded(),is(true));
        Assertion.assertThat("Order Detail is Present",viewOrderDashboardPage.isOrderDetailLoaded(),is(true));
        Assertion.assertThat("Passenger Detail is Present",viewOrderDashboardPage.isPassengerDetailLoaded(),is(true));
        Assertion.assertThat("Order Status is Present",viewOrderDashboardPage.isOrderStatusLoaded(),is(true));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));

        step("Reporting hotel with order id: " + issuedOrderId + " order as failed-checkin");
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
        viewOrderDashboardPage.clickMoreActionButton();
        Assertion.assertThat("More Action Box is Loaded",viewOrderDashboardPage.isMoreActionBoxLoaded(),is(true));
        Assertion.assertThat("Report Failed Checkin is visible",viewOrderDashboardPage.isReportFailedCheckinVisible(),containsString("Report Failed Checkin"));
        viewOrderDashboardPage.clickFailedCheckin();
        Assertion.assertThat("Report Failed Checkin popup is Visible",viewOrderDashboardPage.isReportFailedCheckInPageLoaded(),is(true));

        step("Click failed checkin continue button (without entering reasons)");
        viewOrderDashboardPage.clickContinueFailed();
        Assertion.assertThat("Verify category input field error message", viewOrderDashboardPage.getFailedCheckinErrorMessageByInputName("Category"), containsString("This field is required"));
        Assertion.assertThat("Verify checkInProblem input field error message", viewOrderDashboardPage.getFailedCheckinErrorMessageByInputName("Check-in problem"), containsString("This field is required"));
        Assertion.assertThat("Verify reportedFrom input field error message", viewOrderDashboardPage.getFailedCheckinErrorMessageByInputName("Reported from"), containsString("This field is required"));

        step("Select failed checkin category");
        viewOrderDashboardPage.clickCategoryDropdown();
        viewOrderDashboardPage.selectFailedCheckinCategory();

        step("Select failed checkin problem");
        viewOrderDashboardPage.clickCheckinProblemDropdown();
        viewOrderDashboardPage.SelectCheckinProblem();

        step("Select failed checkin reported from");
        viewOrderDashboardPage.clickReportedDropdown();
        viewOrderDashboardPage.selectReportedCategory();

        step("Select failed checkin root cause");
        viewOrderDashboardPage.clickRootCauseDropdown();
        viewOrderDashboardPage.selectRootCause();

        step("Click failed checkin continue button");
        viewOrderDashboardPage.clickContinueFailed();

        step("Now go to Hotel Failed checkin Dashboard");
        loginMemberAdmin.goToDashboard(param.get("failedCheckinDashboardUrl"));

        boolean isDefaultTabNeedCS = failedCheckinDashboardPage.isDefaultTabNeedCS();
        Assertion.assertThat("Verify default tab is Need CS", isDefaultTabNeedCS, is(true));

        step("Now enter correct value in Order id input field and search and verify");
        failedCheckinDashboardPage.setOrderIdInput(issuedOrderId);

        step("click search button");
        failedCheckinDashboardPage.clickSearchButton();

        step("Verify data for Order id: " + issuedOrderId);
        Assertion.assertThat("Verify correct Order id is present", failedCheckinDashboardPage.getOrderIdTable(), equalTo(issuedOrderId));
        Assertion.assertThat("Verify correct Ticket status is present", failedCheckinDashboardPage.getTicketStatusTable(), equalTo("Need CS"));
    }
  
    @Author(names = {"Abhishek Sharma","Bikash kumar jha"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Demand")
    @Module(name = "ViewOrder")
    @Test
    public void verifyViewOrderSearchFunctionality(){
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyViewOrderSearchFunctionality");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String startDate = generateRandomDate(param.get("dateRange"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = Optional.ofNullable(param.get("currency")).orElse("IDR");

        String[] salutationList = { "Mr", "Mrs", "Ms" };
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create a issued hotel order via API");
        Boolean apiResult = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, false, false, "",
                "en", false);
        Assertion.assertThat("Hotel booking via API is successful", apiResult, is(true));

        step("Get data from the issued order");
        String issuedOrderId = String.valueOf(hotelApi.getOrderId());
        if (issuedOrderId == null || issuedOrderId.isEmpty() || "0".equals(issuedOrderId)) {
            Assertion.skip("Issued order ID is invalid after API booking; skipping UI verification.");
        }

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Check Member Admin successful");
        boolean isLoggedInMemberAdmin = loginMemberAdmin.isUserLoggedIn(adminEmail,adminPassword);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", isLoggedInMemberAdmin, is(true));

        step("Now go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(param.get("adminOrderDetailUrl"));
        Assertion.assertThat("Is Search Order Detail Box Loaded",viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Select source Order ID and input wrong Order ID");
        viewOrderDashboardPage.clickSourceDropdown();
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));
        viewOrderDashboardPage.SetInputOrderId(String.valueOf(generateRandomInt(10)));
        viewOrderDashboardPage.clickSearch();
        Assertion.assertThat("Verify for error", viewOrderDashboardPage.getNoDataAvailableErrorMessage(), containsStringIgnoringCase("Booking not found. Please enter another Order ID/Itinerary ID/Vendor Itinerary ID."));

        step("Select source Itinerary ID and input wrong Itinerary ID");
        viewOrderDashboardPage.clickResetButton();
        viewOrderDashboardPage.clickSourceDropdown();
        viewOrderDashboardPage.selectSourceItineraryID();
        Assertion.assertThat("Source Itinerary ID is selected", viewOrderDashboardPage.checkSelectedSource("Itinerary ID"), is(true));
        viewOrderDashboardPage.SetInputItineraryId(String.valueOf(generateRandomInt(10)));
        viewOrderDashboardPage.clickSearch();
        Assertion.assertThat("Verify for error", viewOrderDashboardPage.getNoDataAvailableErrorMessage(), containsStringIgnoringCase("Booking not found. Please enter another Order ID/Itinerary ID/Vendor Itinerary ID."));

        step("Select source Vendor Itinerary ID and input wrong Vendor Itinerary ID");
        viewOrderDashboardPage.clickResetButton();
        viewOrderDashboardPage.clickSourceDropdown();
        viewOrderDashboardPage.selectSourceVendorItineraryID();
        Assertion.assertThat("Source Vendor Itinerary ID is selected", viewOrderDashboardPage.checkSelectedSource("Vendor Itinerary ID"), is(true));
        viewOrderDashboardPage.SetInputVendorItineraryId(String.valueOf(generateRandomInt(10)));
        viewOrderDashboardPage.clickSearch();
        Assertion.assertThat("Verify for error", viewOrderDashboardPage.getNoDataAvailableErrorMessage(), containsStringIgnoringCase("Booking not found. Please enter another Order ID/Itinerary ID/Vendor Itinerary ID."));

        step("Select source Order ID and input correct Order ID");
        viewOrderDashboardPage.clickResetButton();
        viewOrderDashboardPage.clickSourceDropdown();
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));
        viewOrderDashboardPage.SetInputOrderId(issuedOrderId);
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(issuedOrderId));
        Assertion.assertThat("Itinerary Id is Present",viewOrderDashboardPage.isItineraryIdLoaded(),is(true));
        Assertion.assertThat("Date purchased is Present",viewOrderDashboardPage.isDatePurchasedLoaded(),is(true));
        Assertion.assertThat("Product Type is Present",viewOrderDashboardPage.isProductTypeLoaded(),is(true));
        Assertion.assertThat("Order Detail is Present",viewOrderDashboardPage.isOrderDetailLoaded(),is(true));
        Assertion.assertThat("Passenger Detail is Present",viewOrderDashboardPage.isPassengerDetailLoaded(),is(true));
        Assertion.assertThat("Order Status is Present",viewOrderDashboardPage.isOrderStatusLoaded(),is(true));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));

        step("Get data from table");
        String itineraryId = viewOrderDashboardPage.getItinid();
        String vendorItineraryId = viewOrderDashboardPage.getItinid();

        step("Select source Itinerary ID and input correct Itinerary ID");
        viewOrderDashboardPage.clickResetButton();
        viewOrderDashboardPage.clickSourceDropdown();
        viewOrderDashboardPage.selectSourceItineraryID();
        Assertion.assertThat("Source Itinerary ID is selected", viewOrderDashboardPage.checkSelectedSource("Itinerary ID"), is(true));
        viewOrderDashboardPage.SetInputItineraryId(itineraryId);
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(issuedOrderId));
        Assertion.assertThat("Itinerary Id is Present",viewOrderDashboardPage.getItinid(),containsString(itineraryId));
        Assertion.assertThat("Date purchased is Present",viewOrderDashboardPage.isDatePurchasedLoaded(),is(true));
        Assertion.assertThat("Product Type is Present",viewOrderDashboardPage.isProductTypeLoaded(),is(true));
        Assertion.assertThat("Order Detail is Present",viewOrderDashboardPage.isOrderDetailLoaded(),is(true));
        Assertion.assertThat("Passenger Detail is Present",viewOrderDashboardPage.isPassengerDetailLoaded(),is(true));
        Assertion.assertThat("Order Status is Present",viewOrderDashboardPage.isOrderStatusLoaded(),is(true));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));

        step("Select source Vendor Itinerary ID and input correct Vendor Itinerary ID");
        viewOrderDashboardPage.clickResetButton();
        viewOrderDashboardPage.clickSourceDropdown();
        viewOrderDashboardPage.selectSourceVendorItineraryID();
        Assertion.assertThat("Source Vendor Itinerary ID is selected", viewOrderDashboardPage.checkSelectedSource("Vendor Itinerary ID"), is(true));
        viewOrderDashboardPage.SetInputVendorItineraryId(vendorItineraryId);
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(issuedOrderId));
        Assertion.assertThat("Itinerary Id is Present",viewOrderDashboardPage.getItinid(),containsString(vendorItineraryId));
        Assertion.assertThat("Date purchased is Present",viewOrderDashboardPage.isDatePurchasedLoaded(),is(true));
        Assertion.assertThat("Product Type is Present",viewOrderDashboardPage.isProductTypeLoaded(),is(true));
        Assertion.assertThat("Order Detail is Present",viewOrderDashboardPage.isOrderDetailLoaded(),is(true));
        Assertion.assertThat("Passenger Detail is Present",viewOrderDashboardPage.isPassengerDetailLoaded(),is(true));
        Assertion.assertThat("Order Status is Present",viewOrderDashboardPage.isOrderStatusLoaded(),is(true));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
    }

    @Author(names = {"Abhishek Sharma"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Demand")
    @Module(name = "FailedCheckinDashboard")
    @Test
    public void verifyFailedCheckInApprovalFlowAndRedirection() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyFailedCheckInApprovalFlowAndRedirection");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        ViewOrderDashboardPage<?> viewOrderDashboardPage = new ViewOrderDashboardPage<>(session);
        FailedCheckinDashboardPage<?> failedCheckinDashboardPage = new FailedCheckinDashboardPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String startDate = generateRandomDate(param.get("dateRange"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = Optional.ofNullable(param.get("currency")).orElse("IDR");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create a issued hotel order via API");
        Boolean apiResult = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, false, false, "",
                "en", false);
        Assertion.assertThat("Hotel booking via API is successful", apiResult, is(true));

        step("Get data from the issued order");
        String issuedOrderId = String.valueOf(hotelApi.getOrderId());
        int issuedOrderIdInt = hotelApi.getOrderId();
        if (issuedOrderIdInt <= 0) {
            Assertion.skip("Issued order ID is invalid after API booking; skipping UI verification.");
        }

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Check Member Admin successful");
        boolean isLoggedInMemberAdmin = loginMemberAdmin.isUserLoggedIn(adminEmail, adminPassword);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", isLoggedInMemberAdmin, is(true));

        step("Now go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(param.get("adminOrderDetailUrl"));
        Assertion.assertThat("Is Search Order Detail Box Loaded",viewOrderDashboardPage.isSearchOrderDetailBoxLoaded(), is(true));

        step("Click Source Dropdown");
        viewOrderDashboardPage.clickSourceDropdown();

        step("Select Source Order ID");
        viewOrderDashboardPage.selectSourceOrderID();
        Assertion.assertThat("Source Order ID is selected", viewOrderDashboardPage.checkSelectedSource("Order ID"), is(true));

        step("Input Order Id");
        viewOrderDashboardPage.setOrderIdInput(issuedOrderId);

        step("Click Search");
        viewOrderDashboardPage.clickSearch();

        step("Every Order Details Should be Visible");
        Assertion.assertThat("Order Detail Box is Loaded",viewOrderDashboardPage.isOrderDetailBoxLoaded(),is(true));
        Assertion.assertThat("Order Id is Present",viewOrderDashboardPage.getOrderId(),containsString(issuedOrderId));
        Assertion.assertThat("Itinerary Id is Present",viewOrderDashboardPage.isItineraryIdLoaded(),is(true));
        Assertion.assertThat("Date purchased is Present",viewOrderDashboardPage.isDatePurchasedLoaded(),is(true));
        Assertion.assertThat("Product Type is Present",viewOrderDashboardPage.isProductTypeLoaded(),is(true));
        Assertion.assertThat("Order Detail is Present",viewOrderDashboardPage.isOrderDetailLoaded(),is(true));
        Assertion.assertThat("Passenger Detail is Present",viewOrderDashboardPage.isPassengerDetailLoaded(),is(true));
        Assertion.assertThat("Order Status is Present",viewOrderDashboardPage.isOrderStatusLoaded(),is(true));
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
        Assertion.assertThat("More Action button is clickable",viewOrderDashboardPage.isActionButtonClickable(),is(true));

        step("Get data from table");
        String itineraryId = viewOrderDashboardPage.getItinid();

        step("Click More action button");
        Assertion.assertThat("Action column is Present",viewOrderDashboardPage.isActionPaneLoaded(),is(true));
        viewOrderDashboardPage.clickMoreActionButton();

        Assertion.assertThat("More Action Box is Loaded",viewOrderDashboardPage.isMoreActionBoxLoaded(),is(true));
        Assertion.assertThat("Report Failed Checkin is visible",viewOrderDashboardPage.isReportFailedCheckinVisible(),containsString("Report Failed Checkin"));
        viewOrderDashboardPage.clickFailedCheckin();
        Assertion.assertThat("Report Failed Checkin is Visible",viewOrderDashboardPage.isReportFailedCheckInPageLoaded(),is(true));

        step("Check Popup Is Displayed");
        Assertion.assertThat("Confirmation Popup Box Is Loaded",failedCheckinDashboardPage.isConfirmationPopupBoxLoaded(),is(true));
        Assertion.assertThat("Category Option Is Visible",failedCheckinDashboardPage.isCategoryVisible(),is(true));
        Assertion.assertThat("CheckIn Option is Visible",failedCheckinDashboardPage.isCheckInProblemVisible(),is(true));
        Assertion.assertThat("Reported From Option is Visible",failedCheckinDashboardPage.isReportedFromVisible(),is(true));
        Assertion.assertThat("Root Cause Option is Visible",failedCheckinDashboardPage.isRootCauseVisible(),is(true));

        step("Check Dropdown List For Category");
        failedCheckinDashboardPage.selectCategory();
        Assertion.assertThat("Category Dropdown Is Visible",failedCheckinDashboardPage.isCategoryDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstCategoryOption();
        Assertion.assertThat("First Category Option is selected and visible",failedCheckinDashboardPage.isFirstCategoryOptionVisible(),containsString("Barcode Voucher Sudah Digunakan atau Tidak Berlaku"));

        step("Check Dropdown List For CheckIn-Problem");
        failedCheckinDashboardPage.selectCheckInProblem();
        Assertion.assertThat("CheckIn Problem Dropdown Is Visible",failedCheckinDashboardPage.isCheckInProblemDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstCheckInProblemOption();
        Assertion.assertThat("First CheckIn Problem Option is selected and visible",failedCheckinDashboardPage.isFirstCheckInProblemOptionVisible(),containsString("Hotel Full Book"));

        step("Check Dropdown List For Reported From");
        failedCheckinDashboardPage.selectReportedFrom();
        Assertion.assertThat("Reported From Dropdown Is Visible",failedCheckinDashboardPage.isReportedFromDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstReportedFromOption();
        Assertion.assertThat("First Reported From Option is selected and visible",failedCheckinDashboardPage.isFirstReportedFromOptionVisible(),containsString("Customer"));

        step("Check Dropdown List For Root Cause");
        failedCheckinDashboardPage.selectRootCause();
        Assertion.assertThat("Root Cause Dropdown Is Visible",failedCheckinDashboardPage.isRootCauseDropdownVisible(),is(true));
        failedCheckinDashboardPage.selectFirstRootCauseOption();
        Assertion.assertThat("First Root Cause Option is selected and visible",failedCheckinDashboardPage.isFirstRootCauseOptionVisible(),containsString("CM tidak connect ke Extranet"));

        step("Click failed checkin continue button");
        failedCheckinDashboardPage.clickYesContinueButton();

        step("Now go to Hotel Failed checkin Dashboard");
        loginMemberAdmin.goToDashboard(param.get("failedCheckinDashboardUrl"));
        Assertion.assertThat("Verify default tab is Need CS", failedCheckinDashboardPage.isDefaultTabNeedCS(), is(true));

        step("Now enter Order id input");
        failedCheckinDashboardPage.setOrderIdInput(issuedOrderId);

        step("click search button");
        failedCheckinDashboardPage.clickSearchButton();

        step("Verify data for Order id: " + issuedOrderId);
        Assertion.assertThat("Verify correct Order id is present", failedCheckinDashboardPage.getOrderIdTable(), equalTo(issuedOrderId));
        Assertion.assertThat("Verify correct Itinerary id is present", failedCheckinDashboardPage.getItineraryIdTable(), equalTo(itineraryId));
        Assertion.assertThat("Verify correct Ticket status is present", failedCheckinDashboardPage.getTicketStatusTable(), equalTo("Need CS"));
        String checkInCheckOutDateTable = failedCheckinDashboardPage.getCheckInCheckOutDateTable(true);

        step("Click and Verify more action button");
        Assertion.assertThat("Verify click more action button is clickable", failedCheckinDashboardPage.isMoreActionButtonClickable(), is(true));
        failedCheckinDashboardPage.clickMoreActionButton();

        step("Click and Verify Relocation Button");
        Assertion.assertThat("Verify Relocation button is clickable", failedCheckinDashboardPage.isRelocationButtonClickable(), is(true));
        failedCheckinDashboardPage.clickRelocationButton();

        step("Verify Relocation page is opened");
        Assertion.assertThat("Verify Relocation page is opened", failedCheckinDashboardPage.isRelocationPageOpened(), is(true));
        Assertion.assertThat("Verify Relocation page Order ID", failedCheckinDashboardPage.getRelocationOrderId(), containsString(issuedOrderId));
        Assertion.assertThat("Verify Relocation page Itinerary ID", failedCheckinDashboardPage.getRelocationItineraryId(), containsString(itineraryId));
        Assertion.assertThat("Verify Relocation page Order Status", failedCheckinDashboardPage.getRelocationOrderIssuedStatus(), containsString("FAILED"));

        step("Select New Hotel Randomly");
        String newHotelName = failedCheckinDashboardPage.selectRelocationHotel();

        step("Verify Relocation Preview Page details");
        Assertion.assertThat("Verify Previous Details Visible", failedCheckinDashboardPage.isMainCardContainerVisiblePreviewPage("Previous Details"), is(true));
        Assertion.assertThat("Verify New Details Visible", failedCheckinDashboardPage.isMainCardContainerVisiblePreviewPage("New Details"), is(true));
        Assertion.assertThat("Verify Previous Details Hotel Name", failedCheckinDashboardPage.getHotelNamePreviewPage("Previous Details"), containsString(param.get("hotelName")));
        Assertion.assertThat("Verify New Details Hotel Name", failedCheckinDashboardPage.getHotelNamePreviewPage("New Details"), containsString(newHotelName));
        Assertion.assertThat("Verify Previous Details Check-in Check-out date range", failedCheckinDashboardPage.getDateRangePreviousDetailPreviewPage(), containsString(checkInCheckOutDateTable));
        Assertion.assertThat("Verify New Details Check-in Check-out date range", failedCheckinDashboardPage.getDateRangeNewDetailPreviewPage(), containsString(checkInCheckOutDateTable));
        Assertion.assertThat("Verify Previous Details Rooms", failedCheckinDashboardPage.getRoomDetailPreviousDetailPreviewPage(), containsString(param.get("room")));
        Assertion.assertThat("Verify New Details Rooms", failedCheckinDashboardPage.getRoomDetailNewDetailPreviewPage(), containsString(param.get("room")));
        Assertion.assertThat("Verify Previous Details Nights", failedCheckinDashboardPage.getNightDetailPreviousDetailPreviewPage(), containsString(param.get("night")));
        Assertion.assertThat("Verify New Details Nights", failedCheckinDashboardPage.getNightDetailNewDetailPreviewPage(), containsString(param.get("night")));
        Assertion.assertThat("Verify Previous Details Guests", failedCheckinDashboardPage.getGuestDetailPreviousDetailPreviewPage(), containsString(param.get("adult")));
        Assertion.assertThat("Verify New Details Guests", failedCheckinDashboardPage.getGuestDetailNewDetailPreviewPage(), containsString(param.get("adult")));
        String totalPrice = failedCheckinDashboardPage.getTotalPricePreviewPage();

        step("Click Continue booking");
        failedCheckinDashboardPage.clickRebookingContinueBookingButton();

        step("Verify Relocation pop appears and its content");
        Assertion.assertThat("Verify popup appears", failedCheckinDashboardPage.isRebookingPopupAppears(), is(true));
        Assertion.assertThat("Verify popup heading", failedCheckinDashboardPage.rebookingPopupHeading(), containsString(param.get("popupTitle")));
        Assertion.assertThat("Verify popup description", failedCheckinDashboardPage.rebookingPopupDescription(), containsString(param.get("popupDescription")));

        step("Click Continue booking on popup");
        failedCheckinDashboardPage.clickRebookingPopupContinueBookingButton();

        step("Verify Relocation success page appears and its content");
        Assertion.assertThat("Verify Relocation success page appears", failedCheckinDashboardPage.isRebookingSuccessPageAppears(), is(true));
        Assertion.assertThat("Verify Relocation success page heading", failedCheckinDashboardPage.rebookingSuccessPageHeading(), containsString(param.get("successPageTitle")));
        Assertion.assertThat("Verify Relocation success page description", failedCheckinDashboardPage.rebookingSuccessPageDescription(), containsString(param.get("successPageDescription")));

        step("Now go to Hotel Failed checkin Dashboard");
        loginMemberAdmin.goToDashboard(param.get("failedCheckinDashboardUrl"));

        step("Go to Waiting for Approval tab");
        failedCheckinDashboardPage.switchFailedCheckinApprovalTab("Waiting for Approval");
        Assertion.assertThat("Verify Order id field is editable", failedCheckinDashboardPage.isOrderIdFilterEditable(), is(true));
        Assertion.assertThat("Verify Itinerary id field is editable", failedCheckinDashboardPage.isItineraryIdFilterEditable(), is(true));

        step("Now enter Order id input and search");
        failedCheckinDashboardPage.setOrderIdInput(issuedOrderId);
        failedCheckinDashboardPage.clickSearchButton();

        step("Verify data for Order id: " + issuedOrderId);
        Assertion.assertThat("Verify correct Order id is present", failedCheckinDashboardPage.getOrderIdTable(), equalTo(issuedOrderId));
        Assertion.assertThat("Verify correct Itinerary id is present", failedCheckinDashboardPage.getItineraryIdTable(), equalTo(itineraryId));
        Assertion.assertThat("Verify correct Ticket status is present", failedCheckinDashboardPage.getTicketStatusTable(), containsString("Waiting for approval"));

        step("Now verify and click Preview action button");
        Assertion.assertThat("Verify click more action button is clickable", failedCheckinDashboardPage.isApprovalPreviewActionButtonClickable(), is(true));
        failedCheckinDashboardPage.clickApprovalPreviewActionButton();

        step("Now Verify Preview Details");
        Assertion.assertThat("Verify Previous Details Visible", failedCheckinDashboardPage.isMainCardContainerVisiblePreviewPage("Previous Details"), is(true));
        Assertion.assertThat("Verify New Details Visible", failedCheckinDashboardPage.isMainCardContainerVisiblePreviewPage("New Details"), is(true));
        Assertion.assertThat("Verify Previous Details Hotel Name", failedCheckinDashboardPage.getHotelNamePreviewPage("Previous Details"), containsString(param.get("hotelName")));
        Assertion.assertThat("Verify New Details Hotel Name", failedCheckinDashboardPage.getHotelNamePreviewPage("New Details"), containsString(newHotelName));
        Assertion.assertThat("Verify Previous Details Check-in Check-out date range", failedCheckinDashboardPage.getDateRangePreviousDetailPreviewPage(), containsString(checkInCheckOutDateTable));
        Assertion.assertThat("Verify New Details Check-in Check-out date range", failedCheckinDashboardPage.getDateRangeNewDetailPreviewPage(), containsString(checkInCheckOutDateTable));
        Assertion.assertThat("Verify Previous Details Rooms", failedCheckinDashboardPage.getRoomDetailPreviousDetailPreviewPage(), containsString(param.get("room")));
        Assertion.assertThat("Verify New Details Rooms", failedCheckinDashboardPage.getRoomDetailNewDetailPreviewPage(), containsString(param.get("room")));
        Assertion.assertThat("Verify Previous Details Nights", failedCheckinDashboardPage.getNightDetailPreviousDetailPreviewPage(), containsString(param.get("night")));
        Assertion.assertThat("Verify New Details Nights", failedCheckinDashboardPage.getNightDetailNewDetailPreviewPage(), containsString(param.get("night")));
        Assertion.assertThat("Verify Previous Details Guests", failedCheckinDashboardPage.getGuestDetailPreviousDetailPreviewPage(), containsString(param.get("adult")));
        Assertion.assertThat("Verify New Details Guests", failedCheckinDashboardPage.getGuestDetailNewDetailPreviewPage(), containsString(param.get("adult")));
        Assertion.assertThat("Verify Total Price amount", failedCheckinDashboardPage.getTotalPricePreviewPage(), equalTo(totalPrice));

        step("Now click Approve button");
        failedCheckinDashboardPage.clickApprovePreviewPageButton();

        step("Verify Approval pop appears and its content");
        Assertion.assertThat("Verify popup appears", failedCheckinDashboardPage.isRebookingPopupAppears(), is(true));
        Assertion.assertThat("Verify popup heading", failedCheckinDashboardPage.rebookingPopupHeading(), containsString(param.get("popupTitle")));
        Assertion.assertThat("Verify popup description", failedCheckinDashboardPage.rebookingPopupDescription(), containsString(param.get("popupDescription")));

        step("Click Continue booking on Approval popup");
        failedCheckinDashboardPage.clickRebookingPopupContinueBookingButton();

        step("Verify success page appears and its content");
        Assertion.assertThat("Verify Success Page appears", failedCheckinDashboardPage.isRebookingSuccessPageAppears(), is(true));
        Assertion.assertThat("Verify Success Page heading", failedCheckinDashboardPage.rebookingSuccessPageHeading(), containsString(param.get("successPageTitle")));
        Assertion.assertThat("Verify Success Page description", failedCheckinDashboardPage.rebookingSuccessPageDescription(), containsString(param.get("successPageDescription")));

        step("Now go to Hotel Failed checkin Dashboard");
        loginMemberAdmin.goToDashboard(param.get("failedCheckinDashboardUrl"));

        step("Go to Waiting for Approval tab");
        failedCheckinDashboardPage.switchFailedCheckinApprovalTab("Waiting for Approval");

        step("Now enter Order id input and search");
        failedCheckinDashboardPage.setOrderIdInput(issuedOrderId);
        failedCheckinDashboardPage.clickSearchButton();

        step("Verify No data Available for Order id: " + issuedOrderId);
        Assertion.assertThat("Verify No data Available", failedCheckinDashboardPage.getNoDataAvailableErrorMessage(), containsString("No Data Available"));
    }

    @Author(names = {"Abhishek Sharma"})
    @Module(name = "Failed Issued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyOrderIdInputAndSearchBehaviorFailedIssued() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyOrderIdInputAndSearchBehaviorFailedIssued");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String startDate = generateRandomDate(param.get("dateRange"));
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = Optional.ofNullable(param.get("currency")).orElse("IDR");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        String randomString = generateRandomString(5);
        String randomInt = String.valueOf(generateRandomInt(5));
        String randomAlphaNumericString = randomString + randomInt;

        step("Create a Failed issued hotel order via API");
        Boolean apiResult = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, false, false, "",
                "en", false);
        Assertion.assertThat("Hotel booking via API is successful", apiResult, is(true));

        step("Get data from the Failed issued order");
        String orderId = String.valueOf(hotelApi.getOrderId());
        if (hotelApi.getOrderId() <= 0) {
            Assertion.skip("Issued order ID is invalid after API booking; skipping UI verification.");
        }

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(adminEmail, adminPassword);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", loginMemberAdmin.isUserLoggedIn(adminEmail, adminPassword), is(true));

        step("Now go to Hotel Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(param.get("failedIssuedDashboardUrl"));
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded", failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Now Verify Order ID Input");
        Assertion.assertThat("Verify Order ID Input is clickable", failedIssuedDashboardPage.isOrderIdInputClickable(), is(true));
        Assertion.assertThat("Verify Order ID Input is selectable", failedIssuedDashboardPage.isOrderIdInputSelectableAndEditable(), is(true));

        step("Now Enter Alphanumeric Input to Order ID");
        failedIssuedDashboardPage.inputOrderId(randomAlphaNumericString);
        Assertion.assertThat("Verify Order ID Input Text contains only numeric part", failedIssuedDashboardPage.getOrderIdInputValue(), equalTo(randomInt));
        failedIssuedDashboardPage.clickSearch();
        Assertion.assertThat("Verify No Data Message", failedIssuedDashboardPage.getTableNoDataMessage(), containsString("No Data"));

        step("Now Enter Alphabet Input to Order ID");
        failedIssuedDashboardPage.inputOrderId(randomString);
        Assertion.assertThat("Verify Order ID Input Text is empty", failedIssuedDashboardPage.getOrderIdInputValue(), equalTo(""));

        step("Now Enter Random Input to Order ID");
        failedIssuedDashboardPage.inputOrderId(randomInt);
        Assertion.assertThat("Verify Order ID Input Text contains random input", failedIssuedDashboardPage.getOrderIdInputValue(), equalTo(randomInt));
        failedIssuedDashboardPage.clickSearch();
        Assertion.assertThat("Verify No Data Message", failedIssuedDashboardPage.getTableNoDataMessage(), containsString("No Data"));

        step("Now Enter Correct Input to Order ID");
        failedIssuedDashboardPage.inputOrderId(orderId);
        Assertion.assertThat("Verify Order ID Input Text contains only numeric part", failedIssuedDashboardPage.getOrderIdInputValue(), equalTo(orderId));
        failedIssuedDashboardPage.clickSearch();

        step("Verify Data");
        Assertion.assertThat("Verify Order Id is Present", failedIssuedDashboardPage.isOrderIdBoxLoaded(), containsString(orderId));
        Assertion.assertThat("Verify Order Issued status is Present", failedIssuedDashboardPage.isOrderStatusLoaded(), containsString("FAILED"));

    }

    @Author(names = {"Abhishek Sharma"})
    @Module(name = "Failed Issued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyBookingAndCheckInDateFilterFailedIssued() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyBookingAndCheckInDateFilterFailedIssued");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String todayDateD_MMMM_YYYY = getTodayDateD_MMMM_YYYY();
        String todaysDate = convertDateFormat(todayDateD_MMMM_YYYY, "d MMMM yyyy", "MM/dd/yyyy");
        String prevDate = convertDateFormat(getRandomDateD_MMMM_YYYY(1), "d MMMM yyyy", "MM/dd/yyyy");
        String randomStartDate = getRandomDateD_MMMM_YYYY(-1);
        String startDate = convertDateFormat(randomStartDate, "d MMMM yyyy", "MM/dd/yyyy");
        String hotelName = param.get("hotelName");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = Optional.ofNullable(param.get("currency")).orElse("IDR");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];
        String formattedStartDate = convertDateFormat(randomStartDate);

        step("Create a Failed issued hotel order via API");
        Boolean apiResult = hotelApi.createIssuedOrderApi(
                email, password, formattedStartDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, false, false, "",
                "en", false);
        Assertion.assertThat("Hotel booking via API is successful", apiResult, is(true));

        step("Get data from the Failed issued order");
        String orderId = String.valueOf(hotelApi.getOrderId());
        if (hotelApi.getOrderId() <= 0) {
            Assertion.skip("Issued order ID is invalid after API booking; skipping UI verification.");
        }

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(adminEmail, adminPassword);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", loginMemberAdmin.isUserLoggedIn(adminEmail, adminPassword), is(true));

        step("Now go to Hotel Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(param.get("failedIssuedDashboardUrl"));
        Assertion.assertThat("Is Failed Issued Dashboard Page Loaded", failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Now Verify Date Input Fields");
        Assertion.assertThat("Verify CheckIn Date Input is clickable", failedIssuedDashboardPage.isCheckInInputClickable(), is(true));
        Assertion.assertThat("Verify CheckIn Date Input is selectable", failedIssuedDashboardPage.isCheckInInputSelectableAndEditable(), is(true));
        Assertion.assertThat("Verify Booking Date Input is clickable", failedIssuedDashboardPage.isBookingInputClickable(), is(true));
        Assertion.assertThat("Verify Booking Date Input is selectable", failedIssuedDashboardPage.isBookingInputSelectableAndEditable(), is(true));

        step("Select & Clear Booking and CheckIn data and verify");
        failedIssuedDashboardPage.setBookingDate(todaysDate, todaysDate);
        failedIssuedDashboardPage.setCheckInDate(startDate, startDate);
        Assertion.assertThat("Verify Booking date range", failedIssuedDashboardPage.getBookingDate(), containsString(todaysDate + " - " + todaysDate));
        Assertion.assertThat("Verify CheckIn date range", failedIssuedDashboardPage.getCheckInDate(), containsString(startDate + " - " + startDate));

        failedIssuedDashboardPage.clearBookingDate();
        failedIssuedDashboardPage.clearCheckInDate();
        Assertion.assertThat("Verify Booking date range cleared", failedIssuedDashboardPage.getBookingDate(), equalTo(" - "));
        Assertion.assertThat("Verify CheckIn date range cleared", failedIssuedDashboardPage.getCheckInDate(), equalTo(" - "));

        step("Now enter Future date in Booking date");
        failedIssuedDashboardPage.setBookingDate(startDate, startDate);
        failedIssuedDashboardPage.clearCheckInDate();
        failedIssuedDashboardPage.clickSearch();
        Assertion.assertThat("Verify No Data Message", failedIssuedDashboardPage.getTableNoDataMessage(), containsString("No Data"));

        step("Now enter Current month date in Booking date and previous month date in CheckIn date");
        failedIssuedDashboardPage.setBookingDate(todaysDate, todaysDate);
        failedIssuedDashboardPage.setCheckInDate(prevDate, prevDate);
        failedIssuedDashboardPage.clickSearch();
        Assertion.assertThat("Verify No Data Message", failedIssuedDashboardPage.getTableNoDataMessage(), containsString("No Data"));

        step("Now Enter Future date in CheckIn Date Input");
        failedIssuedDashboardPage.clearBookingDate();
        failedIssuedDashboardPage.setCheckInDate(startDate, startDate);
        failedIssuedDashboardPage.clickSearch();

        step("Verify Data");
        List<String> checkInDates = failedIssuedDashboardPage.getTableValuesByHeading("Checkin");
        Assertion.assertThat("CheckIn date column size should be atleast one", checkInDates.size(), greaterThan(0));

        for (int i = 0; i < checkInDates.size(); i++) {
            Assertion.assertThat("Verify %d Table Checkin date is within range".formatted(i + 1),
                    failedIssuedDashboardPage.areDatesWithInRange(startDate, startDate, checkInDates.get(i)), is(true));
        }

        step("Now Enter Correct Booking and CheckIn Date Input");
        failedIssuedDashboardPage.setBookingDate(todaysDate, todaysDate);
        failedIssuedDashboardPage.setCheckInDate(startDate, startDate);
        failedIssuedDashboardPage.clickSearch();

        step("Verify Data");
        Assertion.assertThat("Verify Order Id is Present", failedIssuedDashboardPage.getTableValuesByHeading("Order ID").toString(), containsString(orderId));
        Assertion.assertThat("Verify Order Issued status is Present", failedIssuedDashboardPage.getTableValuesByHeading("Order Issued").toString(), containsString("FAILED"));

        step("Verify Data");
        List<String> correctDataBookingDates = failedIssuedDashboardPage.getTableValuesByHeading("Booking Date");
        List<String> correctDataCheckInDates = failedIssuedDashboardPage.getTableValuesByHeading("Checkin");
        Assertion.assertThat("Booking date column size should be atleast one", correctDataBookingDates.size(), greaterThan(0));
        Assertion.assertThat("CheckIn date column size should be atleast one", correctDataCheckInDates.size(), greaterThan(0));

        for (int i = 0; i < correctDataBookingDates.size(); i++) {
            Assertion.assertThat("Verify %d Table Booking dates are within range".formatted(i + 1),
                    failedIssuedDashboardPage.areDatesWithInRange(todaysDate, todaysDate, correctDataBookingDates.get(i)), is(true));
        }

        for (int i = 0; i < correctDataCheckInDates.size(); i++) {
            Assertion.assertThat("Verify %d Table Checkin date is within range".formatted(i + 1),
                    failedIssuedDashboardPage.areDatesWithInRange(startDate, startDate, correctDataCheckInDates.get(i)), is(true));
        }
    }
  
    @Author(names = {"Abhishek Sharma"})
    @Module(name = "Failed Issued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRelocationSearchAndDataDisplayFailedIssued() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRelocationSearchAndDataDisplayFailedIssued");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String startDate = generateRandomDate(param.get("dateRange"));
        String hotelName = param.get("hotelName");
        boolean isRefundable = Boolean.parseBoolean(param.get("isRefundable"));
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = Optional.ofNullable(param.get("currency")).orElse("IDR");
        String noResultsMessage = param.get("noResultsMessage");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create a Failed issued hotel order via API");
        Boolean apiResult = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, isRefundable, false, "",
                "en", false);
        Assertion.assertThat("Hotel booking via API is successful", apiResult, is(true));

        step("Get data from the Failed issued order");
        String orderId = String.valueOf(hotelApi.getOrderId());
        if (hotelApi.getOrderId() <= 0) {
            Assertion.skip("Issued order ID is invalid after API booking; skipping UI verification.");
        }

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(adminEmail, adminPassword);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("Verify User is logged in", loginMemberAdmin.isUserLoggedIn(adminEmail, adminPassword), is(true));

        step("Now go to Hotel Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(param.get("failedIssuedDashboardUrl"));
        Assertion.assertThat("Verify Is Failed Issued Dashboard Page Loaded", failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Now Enter Order ID and click search");
        failedIssuedDashboardPage.inputOrderId(orderId);
        Assertion.assertThat("Verify Order ID Input Text contains only numeric part", failedIssuedDashboardPage.getOrderIdInputValue(), equalTo(orderId));
        failedIssuedDashboardPage.clickSearch();

        step("Verify Table Data");
        Assertion.assertThat("Verify Order Id is Present", failedIssuedDashboardPage.isOrderIdBoxLoaded(), containsString(orderId));
        Assertion.assertThat("Verify Order Issued status is Present", failedIssuedDashboardPage.isOrderStatusLoaded(), containsString("FAILED"));
        Assertion.assertThat("Verify Action is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        Assertion.assertThat("Verify Relocation Button is Present",failedIssuedDashboardPage.isRelocationButtonClickable(),is(true));

        step("Get Data from table");
        String paymentOption = failedIssuedDashboardPage.getTableValueByHeading("Payment Option");
        String source = failedIssuedDashboardPage.getTableValueByHeading("Source");
        String itineraryId = failedIssuedDashboardPage.getTableValueByHeading("Itinerary ID");
        String totalPrice = failedIssuedDashboardPage.getTableValueByHeading("Total Price");

        step("Click Relocation button");
        Assertion.assertThat("Verify Relocation button is loaded", failedIssuedDashboardPage.isRelocationButtonLoaded(), is(true));
        Assertion.assertThat("Verify Relocation button is clickable", failedIssuedDashboardPage.isRelocationButtonClickable(), is(true));
        failedIssuedDashboardPage.clickRelocationButton();

        step("Check Hotels are Present");
        String hotelName1 = failedIssuedDashboardPage.getRelocationHotelNameByIndex(1);
        String hotelName2 = failedIssuedDashboardPage.getRelocationHotelNameByIndex(2);
        String halfHotelName1 = hotelName1.substring(0, hotelName1.length() / 2);
        Assertion.assertThat("Hotel list is present", failedIssuedDashboardPage.isRelocationHotelListDisplayed(), is(true));
        Assertion.assertThat("Verify search button is clickable", failedIssuedDashboardPage.isRelocationSearchButtonClickable(), is(true));
        Assertion.assertThat("Verify destination search bar is editable", failedIssuedDashboardPage.isRelocationSearchInputEditable(), is(true));
        Assertion.assertThat("Verify Order Id on relocate page", failedIssuedDashboardPage.getRelocationPageValueByHeading("Order ID"), containsString(orderId));
        Assertion.assertThat("Verify Itinerary ID on relocate page", failedIssuedDashboardPage.getRelocationPageValueByHeading("Itinerary ID"), containsString(itineraryId));
        Assertion.assertThat("Verify Payment Options on relocate page", failedIssuedDashboardPage.getRelocationPageValueByHeading("Payment Option").replaceAll("[^a-zA-Z0-9]", ""), containsStringIgnoringCase(paymentOption.replaceAll("[^a-zA-Z0-9]", "")));
        Assertion.assertThat("Verify Source on relocate page", failedIssuedDashboardPage.getRelocationPageValueByHeading("Source"), containsString(source));
        Assertion.assertThat("Verify Order Issued Status on relocate page", failedIssuedDashboardPage.getRelocationPageValueByHeading("Order Issued"), containsStringIgnoringCase("FAILED"));
        Assertion.assertThat("Verify Hotel Name on relocate page", failedIssuedDashboardPage.getRelocationPageValueByHeading("Hotel Name"), containsString(hotelName));
        Assertion.assertThat("Verify Hotel Total Price on relocate page", failedIssuedDashboardPage.getRelocationPageValueByHeading("Total Price"), containsString(totalPrice));

        step("Enter the destination as Numerics and search");
        failedIssuedDashboardPage.setRelocationHotelName(String.valueOf(generateRandomInt(5)));
        failedIssuedDashboardPage.clickRelocationSearch();
        Assertion.assertThat("Verify Empty search results", failedIssuedDashboardPage.getRelocationEmptySearchResult(), containsStringIgnoringCase(noResultsMessage));

        step("Enter the destination as half input: " + halfHotelName1);
        failedIssuedDashboardPage.setRelocationHotelName(halfHotelName1);
        failedIssuedDashboardPage.clickRelocationSearch();
        Assertion.assertThat("Verify Searched hotel names contain complete Hotel name", failedIssuedDashboardPage.getRelocationAllHotelName().toString(), containsStringIgnoringCase(hotelName1));

        step("Enter the destination as input: " + hotelName1);
        failedIssuedDashboardPage.setRelocationHotelName(hotelName1);
        failedIssuedDashboardPage.clickRelocationSearch();
        Assertion.assertThat("Verify Searched hotel name contain complete Hotel name", failedIssuedDashboardPage.getRelocationAllHotelName().toString(), containsStringIgnoringCase(hotelName1));

        step("Click Select button for hotel: " + hotelName1);
        failedIssuedDashboardPage.selectRelocationHotelByHotelName(hotelName1);

        step("Enter the destination as input: " + hotelName2);
        failedIssuedDashboardPage.setRelocationHotelName(hotelName2);
        failedIssuedDashboardPage.clickRelocationSearch();
        Assertion.assertThat("Verify Searched hotel name contain Hotel name", failedIssuedDashboardPage.getRelocationAllHotelName().toString(), containsStringIgnoringCase(hotelName2));

        step("Enter the destination as input: " + hotelName1);
        failedIssuedDashboardPage.setRelocationHotelName(hotelName1);
        failedIssuedDashboardPage.clickRelocationSearch();
        Assertion.assertThat("Verify Searched hotel name contain complete Hotel name", failedIssuedDashboardPage.getRelocationAllHotelName().toString(), containsStringIgnoringCase(hotelName1));

        step("Click Select button for hotel: " + hotelName1);
        failedIssuedDashboardPage.selectRelocationHotelByHotelName(hotelName1);

        step("Verify Rooms are Present");
        Assertion.assertThat("Verify Room is shown",failedIssuedDashboardPage.isRoomLoaded(),is(true));
        Assertion.assertThat("Verify Room Details is shown",failedIssuedDashboardPage.isRoomImageLoaded(),is(true));
        Assertion.assertThat("Verify Room Name is shown",failedIssuedDashboardPage.isRoomNameLoaded(),is(true));
        Assertion.assertThat("Verify Room Price is shown",failedIssuedDashboardPage.isRoomPriceLoaded(),is(true));
        Assertion.assertThat("Verify Select button is clickable", failedIssuedDashboardPage.isSelectButtonClickable(),is(true));

        step("Click Back Arrow button");
        Assertion.assertThat("Verify Back Arrow button is clickable", failedIssuedDashboardPage.isRelocationRoomPageBackArrowButtonClickable(), is(true));
        failedIssuedDashboardPage.clickRelocationRoomPageBackArrowButton();

        step("Verify Data");
        Assertion.assertThat("Verify Hotel list is present", failedIssuedDashboardPage.isRelocationHotelListDisplayed(), is(true));
    }


    @Author(names = {"Abhishek Sharma"})
    @Module(name = "Failed Issued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyRelocationFlowBehaviorFailedIssued() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyRelocationFlowBehaviorFailedIssued");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String startDate = generateRandomDate(param.get("dateRange"));
        String hotelName = param.get("hotelName");
        boolean isRefundable = Boolean.parseBoolean(param.get("isRefundable"));
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = Optional.ofNullable(param.get("currency")).orElse("IDR");
        String successMessage = param.get("successMessage");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create a Failed issued hotel order via API");
        Boolean apiResult = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, isRefundable, false, "",
                "en", false);
        Assertion.assertThat("Hotel booking via API is successful", apiResult, is(true));

        step("Get data from the Failed issued order");
        int orderIdInt = hotelApi.getOrderId();
        if (orderIdInt <= 0) {
            Assertion.skip("Issued order ID is invalid after API booking; skipping UI verification.");
        }

        String orderId = String.valueOf(orderIdInt);

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(adminEmail, adminPassword);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("Verify User is logged in", loginMemberAdmin.isUserLoggedIn(adminEmail, adminPassword), is(true));

        step("Now go to Hotel Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(param.get("failedIssuedDashboardUrl"));
        Assertion.assertThat("Verify Is Failed Issued Dashboard Page Loaded", failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Now Enter Order ID and click search");
        failedIssuedDashboardPage.inputOrderId(orderId);
        Assertion.assertThat("Verify Order ID Input Text contains only numeric part", failedIssuedDashboardPage.getOrderIdInputValue(), equalTo(orderId));
        failedIssuedDashboardPage.clickSearch();

        step("Verify Table Data");
        Assertion.assertThat("Verify Order Id is Present", failedIssuedDashboardPage.isOrderIdBoxLoaded(), containsString(orderId));
        Assertion.assertThat("Verify Order Issued status is Present", failedIssuedDashboardPage.isOrderStatusLoaded(), containsString("FAILED"));
        Assertion.assertThat("Verify Action is Present",failedIssuedDashboardPage.isActionColumnLoaded(),is(true));
        Assertion.assertThat("Verify Relocation Button is Present",failedIssuedDashboardPage.isRelocationButtonClickable(),is(true));

        step("Get Data from table");
        String paymentOption = failedIssuedDashboardPage.getTableValueByHeading("Payment Option");
        String source = failedIssuedDashboardPage.getTableValueByHeading("Source");
        String itineraryId = failedIssuedDashboardPage.getTableValueByHeading("Itinerary ID");
        String checkInDate = failedIssuedDashboardPage.getTableValueByHeading("Checkin").replace("\u2011", "-");
        String checkOutDate = failedIssuedDashboardPage.getTableValueByHeading("Checkout").replace("\u2011", "-");
        String checkInCheckOutDateTable = checkInDate + " - " + checkOutDate;
        String totalPrice = failedIssuedDashboardPage.getTableValueByHeading("Total Price");

        step("Click Relocation button");
        Assertion.assertThat("Verify Relocation button is loaded", failedIssuedDashboardPage.isRelocationButtonLoaded(), is(true));
        Assertion.assertThat("Verify Relocation button is clickable", failedIssuedDashboardPage.isRelocationButtonClickable(), is(true));
        failedIssuedDashboardPage.clickRelocationButton();

        step("Check Hotels are Present");
        Assertion.assertThat("Hotel list is present", failedIssuedDashboardPage.isRelocationHotelListDisplayed(), is(true));
        Assertion.assertThat("Verify search button is clickable", failedIssuedDashboardPage.isRelocationSearchButtonClickable(), is(true));
        Assertion.assertThat("Verify destination search bar is editable", failedIssuedDashboardPage.isRelocationSearchInputEditable(), is(true));
        Assertion.assertThat("Verify Order Id on relocate page", failedIssuedDashboardPage.getRelocationPageValueByHeading("Order ID"), containsString(orderId));
        Assertion.assertThat("Verify Itinerary ID on relocate page", failedIssuedDashboardPage.getRelocationPageValueByHeading("Itinerary ID"), containsString(itineraryId));
        Assertion.assertThat("Verify Payment Options on relocate page", failedIssuedDashboardPage.getRelocationPageValueByHeading("Payment Option").replaceAll("[^a-zA-Z0-9]", ""), containsStringIgnoringCase(paymentOption.replaceAll("[^a-zA-Z0-9]", "")));
        Assertion.assertThat("Verify Source on relocate page", failedIssuedDashboardPage.getRelocationPageValueByHeading("Source"), containsString(source));
        Assertion.assertThat("Verify Order Issued Status on relocate page", failedIssuedDashboardPage.getRelocationPageValueByHeading("Order Issued"), containsStringIgnoringCase("FAILED"));
        Assertion.assertThat("Verify Hotel Name on relocate page", failedIssuedDashboardPage.getRelocationPageValueByHeading("Hotel Name"), containsString(hotelName));
        Assertion.assertThat("Verify Hotel Total Price on relocate page", failedIssuedDashboardPage.getRelocationPageValueByHeading("Total Price"), containsString(totalPrice));

        String newHotelName = failedIssuedDashboardPage.getRelocationHotelNameByIndex(1);

        step("Click Select button for hotel: " + newHotelName);
        Assertion.assertThat("Verify Hotel is selectable", failedIssuedDashboardPage.isRelocationHotelByHotelNameSelectable(newHotelName), is(true));
        failedIssuedDashboardPage.selectRelocationHotelByHotelName(newHotelName);

        step("Verify Filter Chips are present");
        List<String> filterChips = failedIssuedDashboardPage.getAllFilterChipsRelocationRoomPage();
        for (String filterChip: filterChips){
            Assertion.assertThat("Verify Filter chip: %s is selectable and select".formatted(filterChip), failedIssuedDashboardPage.isFilterChipSelectableRelocationRoomPage(filterChip), is(true));
            failedIssuedDashboardPage.selectFilterChipRelocationRoomPage(filterChip);
            Assertion.assertThat("Verify Filter chip: %s is selected and unselect".formatted(filterChip), failedIssuedDashboardPage.isFilterChipSelectedRelocationRoomPage(filterChip), is(true));

List<WebElement> roomCards = failedIssuedDashboardPage.getAllRoomCardElements();
roomCards.removeIf(roomCard -> !roomCard.isDisplayed());
Assertion.assertThat("Verify Rooms are available after selecting filter chip: " + filterChip, roomCards.size(), greaterThan(0));
for (int i = 0; i < roomCards.size(); i++){
    Assertion.assertThat("Verify Room card %d matches filter chip".formatted(i + 1), failedIssuedDashboardPage.verifyRoomMatchesActiveFilters(roomCards.get(i), List.of(filterChip)), is(true));
}

            failedIssuedDashboardPage.unselectFilterChipRelocationRoomPage(filterChip);
            Assertion.assertThat("Verify Filter chip: %s is unselected".formatted(filterChip), failedIssuedDashboardPage.isFilterChipUnselectedRelocationRoomPage(filterChip), is(true));
        }

        step("Verify Rooms are Present");
        Assertion.assertThat("Verify Room is shown",failedIssuedDashboardPage.isRoomLoaded(),is(true));
        Assertion.assertThat("Verify Room Details is shown",failedIssuedDashboardPage.isRoomImageLoaded(),is(true));
        Assertion.assertThat("Verify Room Name is shown",failedIssuedDashboardPage.isRoomNameLoaded(),is(true));
        Assertion.assertThat("Verify Room Price is shown",failedIssuedDashboardPage.isRoomPriceLoaded(),is(true));
        Assertion.assertThat("Verify Select button is clickable", failedIssuedDashboardPage.isSelectButtonClickable(),is(true));

        step("Select room");
        failedIssuedDashboardPage.clickSelectRoom();

        if (failedIssuedDashboardPage.isCheckBoxLoaded()) {
            step("click checkbox accepting terms and condition");
            failedIssuedDashboardPage.clickCheckBoxButton();
        }

        step("Verify details");
        Assertion.assertThat("Verify Previous Details Visible", failedIssuedDashboardPage.isMainCardContainerVisiblePreviewPage("Previous Details"), is(true));
        Assertion.assertThat("Verify New Details Visible", failedIssuedDashboardPage.isMainCardContainerVisiblePreviewPage("New Details"), is(true));
        Assertion.assertThat("Verify Previous Details Hotel Name", failedIssuedDashboardPage.getHotelNamePreviewPage("Previous Details"), containsString(hotelName));
        Assertion.assertThat("Verify New Details Hotel Name", failedIssuedDashboardPage.getHotelNamePreviewPage("New Details"), containsString(newHotelName));
        Assertion.assertThat("Verify Previous Details Check-in Check-out date range", failedIssuedDashboardPage.getDateRangePreviousDetailPreviewPage(), containsString(checkInCheckOutDateTable));
        Assertion.assertThat("Verify New Details Check-in Check-out date range", failedIssuedDashboardPage.getDateRangeNewDetailPreviewPage(), containsString(checkInCheckOutDateTable));

        step("Verify Refund Reschedule detail");
        Assertion.assertThat("Verify Refund Reschedule detail is present",failedIssuedDashboardPage.isRefundRescheduleDetailsAppear(),is(true));
        failedIssuedDashboardPage.clickRefundRescheduleDetails();
        Assertion.assertThat("Verify Modal Refund Reschedule detail is present",failedIssuedDashboardPage.isModalRefundRescheduleDetailsAppear(),is(true));
        failedIssuedDashboardPage.clickModalCloseButton();
        Assertion.assertThat("Verify Modal is closed",failedIssuedDashboardPage.isModalDisappeared(),is(true));

        step("Click Continue Booking Button");
        Assertion.assertThat("Continue Booking is Clickable",failedIssuedDashboardPage.isContinueBookingButtonClickable(),is(true));
        failedIssuedDashboardPage.clickContinueBookingButton();
        Assertion.assertThat("PopUp is Loaded",failedIssuedDashboardPage.isRebookPopUpLoaded(),is(true));

        step("Click Check Again Button on popup");
        Assertion.assertThat("Check Again is Clickable",failedIssuedDashboardPage.isCheckAgainButtonClickable(),is(true));
        failedIssuedDashboardPage.clickCheckAgainButton();
        Assertion.assertThat("Continue booking modal is disappeared", failedIssuedDashboardPage.isDialogBoxDisappeared(), is(true));

        step("Click Continue Booking Button Again");
        Assertion.assertThat("Continue Booking is Clickable",failedIssuedDashboardPage.isContinueBookingButtonClickable(),is(true));
        failedIssuedDashboardPage.clickContinueBookingButton();
        Assertion.assertThat("PopUp is Loaded",failedIssuedDashboardPage.isRebookPopUpLoaded(),is(true));

        step("Click Continue Booking button on popup");
        Assertion.assertThat("Continue Booking button is clickable",failedIssuedDashboardPage.isContinueBookingRebookButtonClickable(),is(true));
        failedIssuedDashboardPage.clickContinueBookingRebookButton();

        step("Now Success page appears");
        Assertion.assertThat("Verify Success Page Appears", failedIssuedDashboardPage.isRebookingSuccessPageLoaded(), is(true));
        Assertion.assertThat("Verify Success Page Message", failedIssuedDashboardPage.getRebookingSuccessPageMessage(), containsStringIgnoringCase(successMessage));
    }

    @Author(names = {"Nasha"})
    @Module(name = "Failed Issued")
    @Tribe(name = "Demand")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyPrebookPageDetailsAndFilterFunctionalityInRebookFlow() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyPrebookPageDetailsAndFilterFunctionalityInRebookFlow");
        BaseTest.paramsThreadLocal.set(param);

        HotelApi<WebDriver> hotelApi = new HotelApi<>(session);
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        FailedIssuedDashboardPage<?> failedIssuedDashboardPage = new FailedIssuedDashboardPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String adminEmail = param.get("adminEmail");
        String adminPassword = param.get("adminPassword");
        String startDate = generateRandomDate(param.get("dateRange"));
        String hotelName = param.get("hotelName");
        boolean isRefundable = Boolean.parseBoolean(param.get("isRefundable"));
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String currency = Optional.ofNullable(param.get("currency")).orElse("IDR");
        String failedIssuedDashboardUrl = param.get("failedIssuedDashboardUrl");

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Create a Failed issued hotel order via API");
        Boolean apiResult = hotelApi.createIssuedOrderApi(
                email, password, startDate, hotelName, adult, night, room,
                orderFullName, orderNameTitle, guestFullName, guestTitle,
                currency, isRefundable, false, "",
                "en", false);
        Assertion.assertThat("Hotel booking via API is successful", apiResult, is(true));

        step("Get data from the Failed issued order");
        String orderId = String.valueOf(hotelApi.getOrderId());
        if (hotelApi.getOrderId() <= 0) {
            Assertion.skip("Issued order ID is invalid after API booking; skipping UI verification.");
        }

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Login via cookie injection");
        loginMemberAdmin.loginViaCookie(adminEmail, adminPassword);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("Verify User is logged in", loginMemberAdmin.isUserLoggedIn(adminEmail, adminPassword), is(true));

        step("Now go to Hotel Failed Issued Dashboard");
        loginMemberAdmin.goToDashboard(failedIssuedDashboardUrl);
        Assertion.assertThat("Verify Is Failed Issued Dashboard Page Loaded", failedIssuedDashboardPage.isFailedIssuedDashboardPageLoaded(), is(true));

        step("Search with failed issued orderId");
        failedIssuedDashboardPage.inputOrderId(orderId);
        failedIssuedDashboardPage.clickSearch();
        Assertion.assertThat("Verify Order ID is same with the searched orderId", failedIssuedDashboardPage.isOrderIdBoxLoaded(), containsString(orderId));
        Assertion.assertThat("Verify Order status should be failed", failedIssuedDashboardPage.isOrderStatusLoaded(), containsString("FAILED"));

        step("Click Rebooking button");
        failedIssuedDashboardPage.clickRebookingButton();
        Assertion.assertThat("Verify Order ID is correct", failedIssuedDashboardPage.isFieldValueCorrect("Order ID", orderId), is(true));
        Assertion.assertThat("Verify Guest details (title and name) is correct", failedIssuedDashboardPage.isFieldValueCorrect("Guest Name", guestTitle + ". " + guestFullName), is(true));
        Assertion.assertThat("Verify Payment status is correct", failedIssuedDashboardPage.isFieldValueCorrect("Payment Status", "PAID"), is(true));
        Assertion.assertThat("Verify Rooms are available",failedIssuedDashboardPage.isRoomLoaded(),is(true));
        Assertion.assertThat("Verify Filter chips are present",failedIssuedDashboardPage.areFilterChipsPresent(),is(true));
        Assertion.assertThat("Verify Hotel name is correct", failedIssuedDashboardPage.isFieldValueCorrect("Hotel Name", hotelName), is(true));

        int totalInitialRoomsCount = failedIssuedDashboardPage.getTotalVisibleRoomCardsCount();
        if (totalInitialRoomsCount > 3) {
            Assertion.assertThat("Verify See more rooms button is present", failedIssuedDashboardPage.verifySeeMoreRoomsButtonIfApplicable(), is(true));
        }
    
        step("Check single filter chip.");
        failedIssuedDashboardPage.clickFilterChip("Breakfast");
        step("Verify Room list should be following filter.");
        List<String> activeFiltersSingle = failedIssuedDashboardPage.getSelectedFilterChipNames();
        List<WebElement> visibleRoomsSingle = failedIssuedDashboardPage.getAllRoomCardElements();
        Assertion.assertThat("Active filter should be 'Breakfast'", activeFiltersSingle, hasItem("Breakfast"));
        Assertion.assertThat("Active filters count should be 1", activeFiltersSingle.size(), is(1));
        for (WebElement roomElement : visibleRoomsSingle) {
            Assertion.assertThat("Room card must comply with active single filter rules", failedIssuedDashboardPage.verifyRoomMatchesActiveFilters(roomElement, activeFiltersSingle), is(true));
        }
        step("Select multiple filter chips.");
        failedIssuedDashboardPage.clickFilterChip("Free Wifi");
        step("Verify: Room list should be following filter.");
        List<String> activeFiltersMultiple = failedIssuedDashboardPage.getSelectedFilterChipNames();
        List<WebElement> visibleRoomsMultiple = failedIssuedDashboardPage.getAllRoomCardElements();
        Assertion.assertThat("Active filters should include 'Breakfast'", activeFiltersMultiple, hasItem("Breakfast"));
        Assertion.assertThat("Active filters should include 'Free Wifi'", activeFiltersMultiple, hasItem("Free Wifi"));
        Assertion.assertThat("Active filters count should be 2", activeFiltersMultiple.size(), is(2));
        for (WebElement roomElement : visibleRoomsMultiple) {
            Assertion.assertThat("Room card must comply with active multiple filter rules",  failedIssuedDashboardPage.verifyRoomMatchesActiveFilters(roomElement, activeFiltersMultiple), is(true));
        }
        step("Select all filter chips");
        List<String> allFilters = List.of("All inclusive meals", "Breakfast", "Dinner", "Free Refund & Reschedule", "Lunch", "Free Wifi");
        for (String filter : allFilters) {
            if (!failedIssuedDashboardPage.getSelectedFilterChipNames().contains(filter)) {
                failedIssuedDashboardPage.clickFilterChip(filter);
            }
        }
        step("Verify All filter chips selected");
        Assertion.assertThat("All filter chips should be marked selected", failedIssuedDashboardPage.getSelectedFilterChipNames().size(), is(allFilters.size()));

        step("Unselect all filter");
        for (String filter : allFilters) {
            if (failedIssuedDashboardPage.getSelectedFilterChipNames().contains(filter)) {
                failedIssuedDashboardPage.clickFilterChip(filter);
            }
        }
        step("Verify All rooms are displayed");
        int finalRoomsCount = failedIssuedDashboardPage.getTotalVisibleRoomCardsCount();
        Assertion.assertThat("Room count should match initial count before filtering", finalRoomsCount, is(totalInitialRoomsCount));

        step("Verify All filter chips are unselected");
        Assertion.assertThat("No filter chips should be marked selected", failedIssuedDashboardPage.getSelectedFilterChipNames().size(), is(0));
       
        if (failedIssuedDashboardPage.isSeeMoreRoomsButtonPresent()) {
            step("Click on see more rooms button");
            String buttonText = failedIssuedDashboardPage.getSeeMoreRoomsButtonText();
            int expectedExtraRooms = Integer.parseInt(buttonText.replaceAll("[^0-9]", ""));
            int expectedTotalExpandedRooms = totalInitialRoomsCount + expectedExtraRooms;
            failedIssuedDashboardPage.clickSeeMoreRoomsButton();
            step("number of rooms should be correct and should match");
            int actualExpandedRoomsCount = failedIssuedDashboardPage.getTotalVisibleRoomCardsCount();
            Assertion.assertThat("Expanded room count should match the expected total", actualExpandedRoomsCount, is(expectedTotalExpandedRooms));

            step("Verify Hide button shown at bottom.");
            Assertion.assertThat("Hide button should be visible after expanding rooms", failedIssuedDashboardPage.isHideButtonPresent(), is(true));

            step("Click on hide button.");
            failedIssuedDashboardPage.clickHideButton();

            step("Verify rooms should be hidden again.");
            int actualCollapsedRoomsCount = failedIssuedDashboardPage.getTotalVisibleRoomCardsCount();
            Assertion.assertThat("Room count should collapse back to the initial visible count", actualCollapsedRoomsCount, is(totalInitialRoomsCount));

            step("Verify See more room button displayed again.");
            Assertion.assertThat("See More Rooms button should be visible again after hiding extra rooms", failedIssuedDashboardPage.isSeeMoreRoomsButtonPresent(), is(true));
        }
    }

    @Author(names = {"Manan Kumar"})
    @Vertical(name = "Accommodation")
    @Tribe(name = "Hotel")
    @Module(name = "rates-and-availability")
    @Test
    public void verifyV3RoomReflectsStaticContentUpdateImmediately() throws Exception {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyV3RoomReflectsStaticContentUpdateImmediately");
        BaseTest.paramsThreadLocal.set(param);

        // Panel (Extranet dashboard) data
        String dashboardUsername = param.get("dashboardUsername");
        String dashboardPassword = param.get("dashboardPassword");
        String extranetHotelId = param.get("extranetHotelId");
        String newOccupancy = param.get("newOccupancy");

        // B2C consumer data (used to verify the change on the PDP)
        String b2cEmail = param.get("b2cEmail");
        String b2cPassword = param.get("b2cPassword");

        LoginPage<?> mmLoginPage = new LoginPage<>(session);
        ApproveActivationPage<?> approveActivationPage = new ApproveActivationPage<>(session);
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        PropertyPage<?> propertyPage = new PropertyPage<>(session);
        ExtranetPage<?> extranetPage = new ExtranetPage<>(session);
        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);

        step("Log in to the B2C consumer account sets the tiket.com session cookie used by the PDP tab later");
        homePage.goToHomePage();
        loginPage.loginWithSpecificAccountViaCookie(b2cEmail, b2cPassword);

        step("Member login (admin/member/login only — no MM dashboard URL)");
        mmLoginPage.goToDashboardMemberLoginPage();
        boolean onMemberLoginPage = mmLoginPage.isDashboardMemberLoginPageLoaded();
        if (onMemberLoginPage) {
            mmLoginPage.loginToDashboardMember(dashboardUsername, dashboardPassword);
            Assertion.assertThat("Login to dashboard member successful", mmLoginPage.isLoginToDashboardMemberSuccessful(dashboardUsername), is(true));
        } else {
            Assertion.assertThat("User has been logged in to Member Dashboard", mmLoginPage.isLoginToDashboardMemberSuccessful(dashboardUsername), is(true));
        }

        step("Open approve-activation page and search hotel: " + extranetHotelId);
        approveActivationPage.open();
        approveActivationPage.searchByHotelId(extranetHotelId);

        step("Open the found hotel — must redirect to Extranet");
        approveActivationPage.openHotelAndSwitchToExtranet(extranetHotelId);

        step("Verify the exact same hotel is opened on Extranet. Expected: " + extranetHotelId);
        navbar.skipAndCloseAnyPopUp();
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(extranetHotelId);
        Assertion.assertThat("Hotel on Extranet navbar. Expected: " + extranetHotelId
                + " Actual: " + navbar.getCurrentHotelIdFromHeader().orElse("no match"), hotelMatch, is(true));

        step("Open Property then Room Data and edit the target room");
        sideMenu.clickPropertyMenu();
        sideMenu.clickRoomDataMenu();
        Assertion.assertThat("Room data page should be loaded", propertyPage.isRoomDataPageLoaded(), is(true));

        step("Click on the Edit Button");
        propertyPage.clickFirstEditButton();
        Assertion.assertThat("Edit room page should be loaded", propertyPage.isEditRoomPageLoaded(), is(true));

        step("Capture the current occupancy and change it to " + newOccupancy);
        String oldOccupancy = propertyPage.getOccupancyValue();
        step("Old occupancy is : " + oldOccupancy);
        propertyPage.selectMaxOccupancy(newOccupancy);

        step("CLick on Edit Room Button to submit");
        propertyPage.clickEditRoomSubmitButton();
        propertyPage.waitForSaveSnackbarToAppear();

        try {
            step("Directly Open PDP by click on  'See your property page on tiket.com' and verify the occupancy change on the consumer.");
            extranetPage.clickSeePropertyPageLinkAndSwitchToPDPTab();
            Assertion.assertThat("PDP should load on the B2C tab", extranetPage.isHotelPDPLoaded(), is(true));

            step("Click on see room");
            extranetPage.clickSeeRoom();
            HotelRoomListPageWeb<?> roomListPageWeb = HotelRoomListPageWeb.getInstance(session);
            String roomCapacityText = roomListPageWeb.getRoomCapacityTextByIndex(1);
            Assertion.assertThat("Room capacity on the consumer PDP should immediately reflect the new occupancy (" + newOccupancy + ")",
                    roomCapacityText, containsString(newOccupancy));
            extranetPage.closePDPTabAndReturnToExtranet();

            step("Reopen the room's edit page directly and verify the new occupancy persisted immediately");
            propertyPage.clickFirstEditButton();
            Assertion.assertThat("Occupancy should show the updated value", propertyPage.getOccupancyValue(), is(newOccupancy));
        } finally {
            step("Revert occupancy back to its original value (" + oldOccupancy + ") to leave test data unchanged");
            propertyPage.selectMaxOccupancy(oldOccupancy);
            propertyPage.clickEditRoomSubmitButton();
        }
    }
}
