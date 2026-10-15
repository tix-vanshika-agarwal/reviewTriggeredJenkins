package com.tiket.web.dashboard.test.hotel.extranet;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.extranet.Navbar;
import com.tiket.web.dashboard.page.hotel.extranet.RatePlanPage;
import com.tiket.web.dashboard.page.hotel.extranet.SideMenu;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class RatePlanTest extends BaseTest {
    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "RatePlan")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToCreateCustomRatePlan() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        RatePlanPage<?> ratePlanPage = new RatePlanPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();
        
        Map<String, String> param = getParam("testVerifyUserAbleToCreateCustomRatePlan");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String ratePlanName = param.get("ratePlanName");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to rate plan page");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickRatePlanMenu();
        Assertion.assertThat("Rate plan page is loaded", ratePlanPage.isRatePlanPageLoaded(), is(true));

        step("Click add new rate plan button");
        ratePlanPage.clickAddNewRatePlanButton();
        ratePlanPage.selectCustomRatePlanType();
        Assertion.assertThat("Create rate plan page is loaded", ratePlanPage.isCreateRatePlanPageLoaded(), is(true));
        
        step("Fill in the form");
        ratePlanPage.fillInRatePlanNameInput(ratePlanName);
        ratePlanPage.clickNoMealsOption();
        ratePlanPage.clickLinkedRoomByNumber(1);
        ratePlanPage.clickNonRefundableOption();
        ratePlanPage.clickAddNewRatePlanSubmitButton();
        Assertion.assertThat("Success toast is appeared", ratePlanPage.isSuccessToastAppeared(), is(true));

        step("Search rate plan");
        ratePlanPage.searchRatePlan(ratePlanName);
        Assertion.assertThat("The new rate plan is created", ratePlanPage.isRatePlanAppeared(ratePlanName), is(true));

        step("Revert data to be removed");
        ratePlanPage.deleteRatePlanByName(ratePlanName);
        Assertion.assertThat("The rate plan is deleted", ratePlanPage.isRatePlanAppeared(ratePlanName), is(false));
    }

    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "RatePlan")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToCreateWeeklyRatePlan() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        RatePlanPage<?> ratePlanPage = new RatePlanPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();
        
        Map<String, String> param = getParam("testVerifyUserAbleToCreateWeeklyRatePlan");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String ratePlanName = param.get("ratePlanName");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to rate plan page");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickRatePlanMenu();
        Assertion.assertThat("Rate plan page is loaded", ratePlanPage.isRatePlanPageLoaded(), is(true));

        step("Click add new rate plan button");
        ratePlanPage.clickAddNewRatePlanButton();
        ratePlanPage.selectWeeklyRatePlanType();
        Assertion.assertThat("Create rate plan page is loaded", ratePlanPage.isCreateRatePlanPageLoaded(), is(true));
        
        step("Fill in the form");
        ratePlanPage.fillInRatePlanNameInput(ratePlanName);
        ratePlanPage.selectCustomRatePlanParent();
        ratePlanPage.clickNoMealsOption();
        ratePlanPage.clickNonRefundableOption();
        ratePlanPage.clickAddNewRatePlanSubmitButton();
        Assertion.assertThat("Success toast is appeared", ratePlanPage.isSuccessToastAppeared(), is(true));

        step("Search rate plan");
        ratePlanPage.searchRatePlan(ratePlanName);
        Assertion.assertThat("The new rate plan is created", ratePlanPage.isRatePlanAppeared(ratePlanName), is(true));

        step("Revert data to be removed");
        ratePlanPage.deleteRatePlanByName(ratePlanName);
        Assertion.assertThat("The rate plan is deleted", ratePlanPage.isRatePlanAppeared(ratePlanName), is(false));
    }

    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "RatePlan")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToCreateMonthlyRatePlan() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        RatePlanPage<?> ratePlanPage = new RatePlanPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();
        
        Map<String, String> param = getParam("testVerifyUserAbleToCreateMonthlyRatePlan");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String ratePlanName = param.get("ratePlanName");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to rate plan page");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickRatePlanMenu();
        Assertion.assertThat("Rate plan page is loaded", ratePlanPage.isRatePlanPageLoaded(), is(true));

        step("Click add new rate plan button");
        ratePlanPage.clickAddNewRatePlanButton();
        ratePlanPage.selectMonthlyRatePlanType();
        Assertion.assertThat("Create rate plan page is loaded", ratePlanPage.isCreateRatePlanPageLoaded(), is(true));
        
        step("Fill in the form");
        ratePlanPage.fillInRatePlanNameInput(ratePlanName);
        ratePlanPage.selectCustomRatePlanParent();
        ratePlanPage.clickNoMealsOption();
        ratePlanPage.clickNonRefundableOption();
        ratePlanPage.clickAddNewRatePlanSubmitButton();
        Assertion.assertThat("Success toast is appeared", ratePlanPage.isSuccessToastAppeared(), is(true));

        step("Search rate plan");
        ratePlanPage.searchRatePlan(ratePlanName);
        Assertion.assertThat("The new rate plan is created", ratePlanPage.isRatePlanAppeared(ratePlanName), is(true));

        step("Revert data to be removed");
        ratePlanPage.deleteRatePlanByName(ratePlanName);
        Assertion.assertThat("The rate plan is deleted", ratePlanPage.isRatePlanAppeared(ratePlanName), is(false));
    }

    @Author(names = {"Fadhlan", "Vipul Kumar"})
    @Module(name = "RatePlan")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToEditRatePlan() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        RatePlanPage<?> ratePlanPage = new RatePlanPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();
        
        Map<String, String> param = getParam("testVerifyUserAbleToEditRatePlan");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String oldRatePlanName = param.get("oldRatePlanName");
        String newRatePlanName = param.get("newRatePlanName");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to rate plan page");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickRatePlanMenu();
        Assertion.assertThat("Rate plan page is loaded", ratePlanPage.isRatePlanPageLoaded(), is(true));

        step("Search rate plan");
        ratePlanPage.searchRatePlan(oldRatePlanName);
        Assertion.assertThat("Rate plan is appeared", ratePlanPage.isRatePlanAppeared(oldRatePlanName), is(true));

        step("Click edit rate plan button");
        ratePlanPage.clickEditButtonByRatePlanName(oldRatePlanName);
        Assertion.assertThat("Edit rate plan page is loaded", ratePlanPage.isCreateRatePlanPageLoaded(), is(true));
        
        step("Fill in the form");
        ratePlanPage.clearRatePlanNameInput();
        ratePlanPage.fillInRatePlanNameInput(newRatePlanName);
        ratePlanPage.clickEditRatePlanButton();
        Assertion.assertThat("Success toast is appeared", ratePlanPage.isSuccessToastAppeared(), is(true));

        step("Search rate plan");
        ratePlanPage.refreshPage();
        ratePlanPage.searchRatePlan(newRatePlanName);
        Assertion.assertThat("The rate plan is edited", ratePlanPage.isRatePlanAppeared(newRatePlanName), is(true));

        step("Revert test data");
        ratePlanPage.clickEditButtonByRatePlanName(newRatePlanName);
        Assertion.assertThat("Edit rate plan page is loaded", ratePlanPage.isCreateRatePlanPageLoaded(), is(true));
        ratePlanPage.clearRatePlanNameInput();
        ratePlanPage.fillInRatePlanNameInput(oldRatePlanName);
        ratePlanPage.clickEditRatePlanButton();
        Assertion.assertThat("Success toast is appeared", ratePlanPage.isSuccessToastAppeared(), is(true));

        step("Search rate plan");
        ratePlanPage.searchRatePlan(oldRatePlanName);
        Assertion.assertThat("The rate plan is edited", ratePlanPage.isRatePlanAppeared(oldRatePlanName), is(true));
    }

    @Author(names = {"Vipul Kumar"})
    @Module(name = "RatesAndAvailabilityMenu")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserCanActivateAndDeactivateRatePlan() throws InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        RatePlanPage<?> ratePlanPage = new RatePlanPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserCanActivateAndDeactivateRatePlan");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String ratePlanName = param.get("ratePlanName");
        String hotelId = param.get("hotelId");

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata");
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Navbar shows correct hotel from testdata. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Go to Rates and Availability menu");
        sideMenu.clickRatesAndAvailabilityMenu();

        step("Go to Rate plan submenu");
        sideMenu.clickRatePlanMenu();
        Assertion.assertThat("Rate plan page is loaded", ratePlanPage.isRatePlanPageLoaded(), is(true));

        step("Check rate plan '" + ratePlanName + "' is present");
        ratePlanPage.searchRatePlan(ratePlanName);
        Assertion.assertThat("Rate plan '" + ratePlanName + "' is present", ratePlanPage.isRatePlanAppeared(ratePlanName), is(true));

        boolean initiallyActive = ratePlanPage.isRatePlanActiveByName(ratePlanName);

        if (initiallyActive) {
            step("Deactivate rate plan '" + ratePlanName + "' and verify deactivated");
            boolean deactivated = ratePlanPage.clickAndVerifyToggleButtonByRatePlanName(ratePlanName, false);
            Assertion.assertThat("Rate plan '" + ratePlanName + "' is deactivated", deactivated, is(true));

            step("Activate rate plan '" + ratePlanName + "' and verify activated");
            boolean activated = ratePlanPage.clickAndVerifyToggleButtonByRatePlanName(ratePlanName, true);
            Assertion.assertThat("Rate plan '" + ratePlanName + "' is activated", activated, is(true));
        } else {
            step("Activate rate plan '" + ratePlanName + "' and verify activated");
            boolean activated = ratePlanPage.clickAndVerifyToggleButtonByRatePlanName(ratePlanName, true);
            Assertion.assertThat("Rate plan '" + ratePlanName + "' is activated", activated, is(true));

            step("Deactivate rate plan '" + ratePlanName + "' and verify deactivated");
            boolean deactivated = ratePlanPage.clickAndVerifyToggleButtonByRatePlanName(ratePlanName, false);
            Assertion.assertThat("Rate plan '" + ratePlanName + "' is deactivated", deactivated, is(true));

            step("Ensure rate plan '" + ratePlanName + "' is in active state at the end");
            boolean finalActive = ratePlanPage.clickAndVerifyToggleButtonByRatePlanName(ratePlanName, true);
            Assertion.assertThat("Rate plan '" + ratePlanName + "' status is active (final state)", finalActive, is(true));
        }
    }
}
