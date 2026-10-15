package com.tiket.web.dashboard.test.accommodation;

import com.tiket.annotation.*;
import com.tiket.annotation.Module;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.dashboard.page.accommodation.internal_supplier.rebooking_dashboard.AccommodationRebookingDashboardPage;
import com.tiket.web.dashboard.page.accommodation.internal_supplier.rebooking_dashboard.AccommodationRebookingDashboardPageDWeb;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class AccommodationRebookingTest extends WebTest {

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Vertical(name = "Accommodation")
    @Test
    public void testAccommodationRebookingFlowEndToEnd() {
        Map<String, String> param = getParam("testAccommodationRebookingFlowEndToEnd");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AccommodationRebookingDashboardPage<?> accommodationRebookingPage = AccommodationRebookingDashboardPageDWeb.getInstance(session);
        
        String username = param.get("username");
        String password = param.get("password");
        
        step("The user logins to the Admin dashboard.");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(username, password);
        memberLoginPage.isAdminLogin();
        
        step("The user navigates to accommodation rebooking dashboard.");
        accommodationRebookingPage.goToAccommodationRebookingDashboard();
        
        step("The user selects first failed order from the list.");
        String selectedOrderId = accommodationRebookingPage.selectFailedOrder("first");

        Assertion.assertThat("user verifies selected order ID is not null", selectedOrderId, notNullValue());
        Assertion.assertThat("user verifies selected order ID is not empty", selectedOrderId.isEmpty(), is(false));

        step("The user clicks rebooking button.");
        accommodationRebookingPage.clickRebookingButton();

        step("The user checks room availability and selects room.");
        int availableRooms = accommodationRebookingPage.checkRoomAvailability();
        Assertion.assertThat("user verifies available rooms count is greater than 0", availableRooms > 0, is(true));
        accommodationRebookingPage.selectRoom(-1);

        step("The user clicks continue booking.");
        accommodationRebookingPage.clickContinueBooking();

        step("The user handles booking confirmation popup.");
        accommodationRebookingPage.handleBookingPopup("continue");

        step("The user verifies that success page is displayed.");
        boolean isSuccess = accommodationRebookingPage.isSuccessPageLoaded();
        Assertion.assertThat("user verifies success page is loaded", isSuccess, is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Vertical(name = "Accommodation")
    @Test
    public void testAccommodationRebookingWithHotelLogRedirection() {
        Map<String, String> param = getParam("testAccommodationRebookingWithHotelLogRedirection");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AccommodationRebookingDashboardPage<?> accommodationRebookingPage = AccommodationRebookingDashboardPageDWeb.getInstance(session);
        
        String username = param.get("username");
        String password = param.get("password");
        
        step("The user logins to the Admin dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);
        
        step("The user navigates to accommodation rebooking dashboard.");
        accommodationRebookingPage.goToAccommodationRebookingDashboard();
        
        step("The user selects failed order from the list.");
        String selectedOrderId = accommodationRebookingPage.selectFailedOrder("first");

        Assertion.assertThat("user verifies selected order ID is not null", selectedOrderId, notNullValue());
        Assertion.assertThat("user verifies selected order ID is not empty", selectedOrderId.isEmpty(), is(false));

        step("The user clicks rebooking button.");
        accommodationRebookingPage.clickRebookingButton();

        step("The user checks room availability and selects room.");
        int availableRooms = accommodationRebookingPage.checkRoomAvailability();
        Assertion.assertThat("user verifies available rooms count is greater than 0", availableRooms > 0, is(true));
        accommodationRebookingPage.selectRoom(-1);

        step("The user clicks continue booking.");
        accommodationRebookingPage.clickContinueBooking();

        step("The user handles booking confirmation popup.");
        accommodationRebookingPage.handleBookingPopup("continue");

        step("The user verifies success page is loaded.");
        boolean isSuccess = accommodationRebookingPage.isSuccessPageLoaded() || accommodationRebookingPage.isSuccessPageByURL();
        Assertion.assertThat("user verifies success page is loaded", isSuccess, is(true));

        step("The user clicks on 'Go to Hotel Log' button.");
        accommodationRebookingPage.clickGoToHotelLogButton();

        step("The user verifies that hotel log page is loaded successfully.");
        boolean isHotelLogLoaded = accommodationRebookingPage.isHotelLogPageLoaded();
        Assertion.assertThat("user verifies hotel log page is loaded", isHotelLogLoaded, is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Vertical(name = "Accommodation")
    @Test
    public void testAccommodationRebookingWithHotelLogActionItem() {
        Map<String, String> param = getParam("testAccommodationRebookingWithHotelLogActionItem");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AccommodationRebookingDashboardPage<?> accommodationRebookingPage = AccommodationRebookingDashboardPageDWeb.getInstance(session);
        
        String username = param.get("username");
        String password = param.get("password");
        
        step("The user logins to the Admin dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);
        
        step("The user navigates to accommodation rebooking dashboard.");
        accommodationRebookingPage.goToAccommodationRebookingDashboard();
        
        step("The user selects failed order from the list.");
        String selectedOrderId = accommodationRebookingPage.selectFailedOrder("first");
 
        Assertion.assertThat("user verifies selected order ID is not null", selectedOrderId, notNullValue());
        Assertion.assertThat("user verifies selected order ID is not empty", selectedOrderId.isEmpty(), is(false));

        step("The user clicks rebooking button.");
        accommodationRebookingPage.clickRebookingButton();

        step("The user checks room availability and selects room.");
        int availableRooms = accommodationRebookingPage.checkRoomAvailability();
        Assertion.assertThat("user verifies available rooms count is greater than 0", availableRooms > 0, is(true));
        accommodationRebookingPage.selectRoom(-1);

        step("The user clicks continue booking.");
        accommodationRebookingPage.clickContinueBooking();

        step("The user handles booking confirmation popup.");
        accommodationRebookingPage.handleBookingPopup("continue");

        step("The user verifies that success page is displayed.");
        boolean isSuccess = accommodationRebookingPage.isSuccessPageLoaded();
        Assertion.assertThat("user verifies success page is loaded", isSuccess, is(true));

        step("The user clicks on 'Go to Hotel Log' button.");
        accommodationRebookingPage.clickGoToHotelLogButton();

        step("The user verifies that hotel log page is loaded successfully.");
        boolean isHotelLogLoaded = accommodationRebookingPage.isHotelLogPageLoaded();
        Assertion.assertThat("user verifies hotel log page is loaded", isHotelLogLoaded, is(true));

        step("The user verifies that Show Log action item button is clickable.");
        boolean isShowLogClickable = accommodationRebookingPage.isShowLogButtonClickable();
        Assertion.assertThat("user verifies Show Log button is clickable", isShowLogClickable, is(true));

        step("The user clicks on Show Log action item button.");
        accommodationRebookingPage.clickShowLogActionItem();

        step("The user verifies that hotel log detail page is loaded.");
        boolean isDetailPageLoaded = accommodationRebookingPage.isHotelLogDetailPageLoaded();
        Assertion.assertThat("user verifies hotel log detail page is loaded", isDetailPageLoaded, is(true));
    }

    @Author(names = {"Ram Pratap Singh"})
    @Module(name = "Accommodation")
    @Tribe(name = "Accommodation")
    @Vertical(name = "Accommodation")
    @Test
    public void testAccommodationRebookingWithOrderIdFilter() {
        Map<String, String> param = getParam("testAccommodationRebookingWithOrderIdFilter");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        AccommodationRebookingDashboardPage<?> accommodationRebookingPage = AccommodationRebookingDashboardPageDWeb.getInstance(session);
        
        String username = param.get("username");
        String password = param.get("password");
        
        step("The user logins to the Admin dashboard via cookie injection.");
        memberLoginPage.adminLoginViaCookie(username, password);
        
        step("The user navigates to accommodation rebooking dashboard.");
        accommodationRebookingPage.goToAccommodationRebookingDashboard();
        
        step("The user selects failed order from the list.");
        String selectedOrderId = accommodationRebookingPage.selectFailedOrder("first");
 
        Assertion.assertThat("user verifies selected order ID is not null", selectedOrderId, notNullValue());
        Assertion.assertThat("user verifies selected order ID is not empty", selectedOrderId.isEmpty(), is(false));

        step("The user clicks rebooking button.");
        accommodationRebookingPage.clickRebookingButton();

        step("The user checks room availability and selects room.");
        int availableRooms = accommodationRebookingPage.checkRoomAvailability();
        Assertion.assertThat("user verifies available rooms count is greater than 0", availableRooms > 0, is(true));
        accommodationRebookingPage.selectRoom(-1);

        step("The user clicks continue booking.");
        accommodationRebookingPage.clickContinueBooking();

        step("The user handles booking confirmation popup.");
        accommodationRebookingPage.handleBookingPopup("continue");

        step("The user verifies that success page is displayed.");
        boolean isSuccess = accommodationRebookingPage.isSuccessPageLoaded();
        Assertion.assertThat("user verifies success page is loaded", isSuccess, is(true));

        step("The user clicks on 'Go to Hotel Log' button.");
        accommodationRebookingPage.clickGoToHotelLogButton();

        step("The user verifies that hotel log page is loaded successfully.");
        boolean isHotelLogLoaded = accommodationRebookingPage.isHotelLogPageLoaded();
        Assertion.assertThat("user verifies hotel log page is loaded", isHotelLogLoaded, is(true));

        step("The user searches for the order ID.");
        accommodationRebookingPage.goToAccommodationRebookingDashboard();
        accommodationRebookingPage.searchByOrderId(selectedOrderId);

        step("The user verifies that filter results show data for the searched order ID.");
        boolean isOrderFound = accommodationRebookingPage.isOrderIdInFilterResults(selectedOrderId);
        Assertion.assertThat("user verifies order ID appears in filter results", isOrderFound, is(true));

        step("The user verifies that filter results count is greater than 0.");
        int filterResultCount = accommodationRebookingPage.getFilterResultCount();
        Assertion.assertThat("user verifies filter results count is greater than 0", filterResultCount > 0, is(true));
    }
}
