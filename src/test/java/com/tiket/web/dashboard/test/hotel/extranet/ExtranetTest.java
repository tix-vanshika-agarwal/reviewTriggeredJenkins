package com.tiket.web.dashboard.test.hotel.extranet;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.HotelApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.pojo.room.SelectRoomResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.extranet.*;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.ApproveActivationPage;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.LoginPage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.page.accommodation.hotels.PDP.PDPPageWeb;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.*;

public class ExtranetTest extends BaseTest {

    @Author(names = {"Vipul Kumar"})
    @Module(name = "ExtranetCommonCases")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyAllSideMenusAndSubmenusOpenSuccessfullyOnDomesticHotel() {
        Map<String, String> param = getParam("testVerifyAllSideMenusAndSubmenusOpenSuccessfullyOnDomesticHotel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        ApproveActivationPage<?> approveActivationPage = new ApproveActivationPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");

        LoginPage<?> mmLoginPage = new LoginPage<>(session);
        step("Member login (admin/member/login only — no MM dashboard URL)");
        mmLoginPage.goToDashboardMemberLoginPage();
        boolean onMemberLoginPage = mmLoginPage.isDashboardMemberLoginPageLoaded();
        if (onMemberLoginPage) {
            mmLoginPage.loginToDashboardMember(email, password);
            Assertion.assertThat("Login to dashboard member successful", mmLoginPage.isLoginToDashboardMemberSuccessful(email), is(true));
        } else {
            Assertion.assertThat("User has been logged in to Member Dashboard", mmLoginPage.isLoginToDashboardMemberSuccessful(email), is(true));
        }

        step("Open approve-activation page and search hotel: " + hotelId);
        approveActivationPage.open();
        approveActivationPage.searchByHotelId(hotelId);

        step("Open the found hotel — must redirect to Extranet");
        approveActivationPage.openHotelAndSwitchToExtranet(hotelId);

        step("Verify the exact same hotel is opened on Extranet. Expected: " + hotelId);
        navbar.skipAndCloseAnyPopUp();
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on Extranet navbar. Expected: " + hotelId + " Actual: " + navbar.getCurrentHotelIdFromHeader().orElse("no match"), hotelMatch, is(true));

        step("Verify all side menu items are displayed");
        for (SideMenu.SidebarMenu menu : SideMenu.SidebarMenu.values()) {
            boolean menuDisplayed = sideMenu.isMenuDisplayed(menu);
            Assertion.assertThat("Side menu displayed: " + menu, menuDisplayed, is(true));
        }

        step("Open each submenu and validate its page subject is present (rendered & not blocked)");
        for (SideMenu.SidebarSubmenu submenu : SideMenu.SidebarSubmenu.values()) {
            boolean subjectPresent;
            String failureDetail = "";
            try {
                subjectPresent = sideMenu.openSubmenuAndVerifySubject(submenu);
            } catch (Exception e) {
                subjectPresent = false;
                failureDetail = " | error: " + e;
                log("Error opening submenu " + submenu.getParent() + " > " + submenu + "\n" + ExceptionUtils.getStackTrace(e));
            }
            Assertion.assertThat("Submenu page subject present (rendered & not blocked): " + submenu.getParent() + " > " + submenu + failureDetail, subjectPresent, is(true));
        }

        step("Go back to Home before logout");
        sideMenu.clickMenu(SideMenu.SidebarMenu.HOME);

        step("Logout from Extranet");
        new AuthTest().testVerifyUserCanLogoutFromExtranet(session);
    }

    @Author(names = {"Vipul Kumar"})
    @Module(name = "ManageUsers")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToAddUserAsAdministrator() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        ManageUsersPage<?> manageUsersPage = new ManageUsersPage<>(session);
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToAddUserAsAdministrator");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String userEmail = param.get("userEmail");
        String userName = param.get("userName");
        String phoneNo = param.get("phoneNo");
        String pin = param.get("pin");
        String hotelId = param.get("hotelId");

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Go to Manage Users Page");
        sideMenu.clickManageUsersMenu();
        Assertion.assertThat("Manage Users Page is Loaded", manageUsersPage.isManageUsersPageLoaded(), is(true));

        step("If user already exists, delete first");
        manageUsersPage.enterFilterUsersName(userName);
        manageUsersPage.submitFilterManageUsers();
        if (manageUsersPage.isUserSearchResultFoundByName(userName)) {
            String existingUserName = manageUsersPage.getDisplayedUserName();
            String existingUserEmail = manageUsersPage.getDisplayedUserEmail();
            Assertion.assertThat("Existing user to delete - name. Expected: " + userName + " Actual: " + existingUserName, existingUserName, containsString(userName));
            Assertion.assertThat("Existing user to delete - email. Expected: " + userEmail + " Actual: " + existingUserEmail, existingUserEmail, containsString(userEmail));
            manageUsersPage.clickDeleteUserButton();
            Assertion.assertThat("Delete User PopUp is Loaded", manageUsersPage.isDeleteUserPopUpLoaded(), is(true));
            manageUsersPage.clickPopUpDeleteButton();
            Assertion.assertThat("Delete Success PopUp is Loaded", manageUsersPage.isDeleteSuccessPopUpLoaded(), is(true));
            manageUsersPage.clickBackToListButton();
            manageUsersPage.clickResetFilterButton();
            manageUsersPage.enterFilterUsersName(userName);
            manageUsersPage.submitFilterManageUsers();
            Assertion.assertThat("Existing user removed from list", manageUsersPage.isUserSearchResultFoundByName(userName), is(false));
        }

        step("Click Add New User Button");
        manageUsersPage.clickAddNewUserButton();
        Assertion.assertThat("Save User Page is Loaded", manageUsersPage.isSaveUserPageLoaded(), is(true));

        step("Fill Out Form");
        manageUsersPage.fillUserEmail(userEmail);
        manageUsersPage.fillUserName(userName);
        manageUsersPage.fillPhoneNo(phoneNo);

        step("Click on Administrator Role");
        manageUsersPage.clickAdministratorRadioButton();

        step("Click Allow Notification");
        manageUsersPage.clickYesRadioButton();

        step("Click Add new User");
        manageUsersPage.clickAddNewUserButtonAgain();
        Assertion.assertThat("Account PIN Popup is Loaded", manageUsersPage.isAccountPinPopupLoaded(), is(true));

        step("Enter Account Pin");
        manageUsersPage.fillAccountPin(pin);
        Assertion.assertThat("Successfully Added New User Popup is Loaded", manageUsersPage.isUserAddedPopupLoaded(), is(true));

        step("Click Go Back");
        manageUsersPage.clickGoBackButton();
        Assertion.assertThat("Manage Users Page is Loaded", manageUsersPage.isManageUsersPageLoaded(), is(true));

        step("Verify added user is present on the list");
        manageUsersPage.enterFilterUsersName(userName);
        manageUsersPage.submitFilterManageUsers();
        Assertion.assertThat("User is present on the list", manageUsersPage.isUserSearchResultFoundByName(userName), is(true));
        String displayedName = manageUsersPage.getDisplayedUserName();
        Assertion.assertThat("Added user name. Expected: " + userName + " Actual: " + displayedName, displayedName, containsString(userName));
        String displayedEmail = manageUsersPage.getDisplayedUserEmail();
        Assertion.assertThat("Added user email. Expected: " + userEmail + " Actual: " + displayedEmail, displayedEmail, containsString(userEmail));
        Assertion.assertThat("User is of Administrator Role", manageUsersPage.getUserRole(), containsString("Administrator"));

        step("Revert test data - verify correct user before delete");
        manageUsersPage.enterFilterUsersName(userName);
        manageUsersPage.submitFilterManageUsers();
        Assertion.assertThat("User to be deleted is found", manageUsersPage.isUserSearchResultFoundByName(userName), is(true));
        String userToDeleteName = manageUsersPage.getDisplayedUserName();
        Assertion.assertThat("User to delete - name. Expected: " + userName + " Actual: " + userToDeleteName, userToDeleteName, containsString(userName));
        String userToDeleteEmail = manageUsersPage.getDisplayedUserEmail();
        Assertion.assertThat("User to delete - email. Expected: " + userEmail + " Actual: " + userToDeleteEmail, userToDeleteEmail, containsString(userEmail));
        manageUsersPage.clickDeleteUserButton();
        Assertion.assertThat("Delete User PopUp is Loaded", manageUsersPage.isDeleteUserPopUpLoaded(), is(true));
        manageUsersPage.clickPopUpDeleteButton();
        Assertion.assertThat("Delete Success PopUp is Loaded", manageUsersPage.isDeleteSuccessPopUpLoaded(), is(true));
        manageUsersPage.clickBackToListButton();
        manageUsersPage.clickResetFilterButton();
        manageUsersPage.enterFilterUsersName(userName);
        manageUsersPage.submitFilterManageUsers();
        Assertion.assertThat("Deleted user is not present on the list", manageUsersPage.isUserSearchResultFoundByName(userName), is(false));
    }

    @Author(names = {"Vipul Kumar"})
    @Module(name = "ExtranetCommonCases")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserCanOpenHotelPDPViaHyperlink() {
        Map<String, String> param = getParam("testVerifyUserCanOpenHotelPDPViaHyperlink");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        ExtranetPage<?> extranetPage = new ExtranetPage<>(session);
        AuthTest authTest = new AuthTest();

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Get hotel name from extranet");
        String extranetHotelName = navbar.getDisplayedHotelName().orElseThrow(() -> new IllegalStateException("Hotel name not found on extranet navbar"));

        step("Click property PDP link (located by href) and switch to PDP tab");
        Assert.assertTrue(extranetPage.isSeePropertyPageLinkPresent(), "Property PDP link (by href) is present");
        extranetPage.clickSeePropertyPageLinkAndSwitchToPDPTab();

        step("Verify PDP loads and hotel name in new tab");
        Assertion.assertThat("Hotel PDP is loaded in new tab", extranetPage.isHotelPDPLoaded(), is(true));
        String pdpHotelName = extranetPage.getHotelNameOnPDP();
        Assertion.assertThat("Hotel name on PDP. Expected: " + extranetHotelName + " Actual: " + pdpHotelName, pdpHotelName, is(extranetHotelName));

        step("Close new tab and return to extranet");
        extranetPage.closePDPTabAndReturnToExtranet();
    }

    @Author(names = {"Vipul Kumar"})
    @Module(name = "ExtranetCommonCases")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserCanSwitchLanguage() {
        Map<String, String> param = getParam("testVerifyUserCanSwitchLanguage");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String language = param.get("language");
        String hotelId = param.get("hotelId");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        ExtranetPage<?> extranetPage = new ExtranetPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        AuthTest authTest = new AuthTest();

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Capture welcome text before language change");
        String welcomeBefore = navbar.getWelcomeAndHotelNameText().orElseThrow(() -> new IllegalStateException("Welcome and hotel name text not found"));

        step("Click on the Flag to change language");
        extranetPage.clickLanguageSelector();
        Assertion.assertThat("Language dropdown is visible", extranetPage.isLanguageDropdownVisible(), is(true));

        step("Switch language to " + language);
        extranetPage.selectLanguageByName(language);

        step("Verify welcome text before vs after language change");
        String welcomeAfter = navbar.getWelcomeAndHotelNameText().orElseThrow(() -> new IllegalStateException("Welcome and hotel name text not found"));
        String expectedWelcomePrefix = ExtranetPage.getExpectedWelcomePrefixForLanguage(language);
        boolean welcomeUpdated = welcomeAfter.startsWith(expectedWelcomePrefix + ", ") && welcomeAfter.length() > (expectedWelcomePrefix + ", ").length();
        Assertion.assertThat("Before: \"" + welcomeBefore + "\". After: \"" + welcomeAfter + "\". Expected after: " + expectedWelcomePrefix + ", [HotelName].", welcomeUpdated, is(true));

        step("Ensure sidebar menu items are present after language change");
        Assertion.assertThat("Sidebar HOME menu displayed", sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.HOME), is(true));
        Assertion.assertThat("Sidebar PROPERTY menu displayed", sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.PROPERTY), is(true));
        Assertion.assertThat("Sidebar RATES_AND_AVAILABILITY menu displayed", sideMenu.isMenuDisplayed(SideMenu.SidebarMenu.RATES_AND_AVAILABILITY), is(true));
    }

    @Author(names = {"Gaurav Bhatia", "Vipul Kumar"})
    @Module(name = "RatesAndAvailabilityMenu")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserCanSwitchPricingModelBetweenOccupancyAndRoomBased() {
        Map<String, String> param = getParam("testVerifyUserCanSwitchPricingModelBetweenOccupancyAndRoomBased");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");

        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        RatesAndAvailabilityPage<?> ratesAndAvailabilityPage = new RatesAndAvailabilityPage<>(session);
        AuthTest authTest = new AuthTest();

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Go to Rates & Availability > Pricing Model");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickPricingModelMenu();

        step("Verify Pricing Model page is loaded");
        Assertion.assertThat("Pricing Model page is loaded (heading and CHANGE button visible)", ratesAndAvailabilityPage.isPricingModelPageLoaded(), is(true));
        Assertion.assertThat("Room-Based and Occupancy-Based options are displayed", ratesAndAvailabilityPage.isPricingModelRoomBasedSectionDisplayed() && ratesAndAvailabilityPage.isPricingModelOccupancyBasedSectionDisplayed(), is(true));

        boolean roomBasedActiveFirst = ratesAndAvailabilityPage.isPricingModelRoomBasedCurrentlyActive();

        if (roomBasedActiveFirst) {
            // Case 1: Room-Based active → switch to Occupancy-Based → revert to Room-Based
            step("Room-Based is active: switch to Occupancy-Based, then revert to Room-Based");
            ratesAndAvailabilityPage.clickPricingModelChangeButton();
            ratesAndAvailabilityPage.clickPricingModelSecondRadio();
            ratesAndAvailabilityPage.clickPricingModelSaveButton();
            ratesAndAvailabilityPage.clickPricingModelYesContinue();
            Assertion.assertThat("Occupancy-Based is currently active after change", ratesAndAvailabilityPage.isPricingModelSecondOptionCurrentlyActive(), is(true));

            step("Revert to Room-Based");
            ratesAndAvailabilityPage.clickPricingModelChangeButton();
            ratesAndAvailabilityPage.clickPricingModelFirstRadio();
            ratesAndAvailabilityPage.clickPricingModelSaveButton();
            ratesAndAvailabilityPage.clickPricingModelYesContinue();
            Assertion.assertThat("Room-Based is active again after revert", ratesAndAvailabilityPage.isPricingModelRoomBasedCurrentlyActive(), is(true));
        } else {
            // Case 2: Occupancy-Based active → switch to Room-Based → revert to Occupancy-Based
            Assertion.assertThat("Occupancy-Based is currently active", ratesAndAvailabilityPage.isPricingModelSecondOptionCurrentlyActive(), is(true));
            step("Occupancy-Based is active: switch to Room-Based, then revert to Occupancy-Based");
            ratesAndAvailabilityPage.clickPricingModelChangeButton();
            ratesAndAvailabilityPage.clickPricingModelFirstRadio();
            ratesAndAvailabilityPage.clickPricingModelSaveButton();
            ratesAndAvailabilityPage.clickPricingModelYesContinue();
            Assertion.assertThat("Room-Based is currently active after change", ratesAndAvailabilityPage.isPricingModelRoomBasedCurrentlyActive(), is(true));

            step("Revert to Occupancy-Based");
            ratesAndAvailabilityPage.clickPricingModelChangeButton();
            ratesAndAvailabilityPage.clickPricingModelSecondRadio();
            ratesAndAvailabilityPage.clickPricingModelSaveButton();
            ratesAndAvailabilityPage.clickPricingModelYesContinue();
            Assertion.assertThat("Occupancy-Based is active again after revert", ratesAndAvailabilityPage.isPricingModelSecondOptionCurrentlyActive(), is(true));
        }
    }

    @Author(names = {"Vipul Kumar"})
    @Module(name = "Commission")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToChangePropertyAndAddOnsCommission() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        CommissionPage<?> commissionPage = new CommissionPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToChangePropertyAndAddOnsCommission");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");
        String newPropertyPercent = String.valueOf(generateRandomInt(21, 50));
        String newMealsPercent = String.valueOf(generateRandomInt(21, 50));
        String newRoomComplementsPercent = String.valueOf(generateRandomInt(21, 50));
        String newTransportsPercent = String.valueOf(generateRandomInt(21, 50));
        String newActivitiesPercent = String.valueOf(generateRandomInt(21, 50));

        LoginPage<?> mmLoginPage = new LoginPage<>(session);
        step("Member login (admin/member/login only — no MM dashboard URL)");
        mmLoginPage.goToDashboardMemberLoginPage();
        boolean onMemberLoginPage = mmLoginPage.isDashboardMemberLoginPageLoaded();
        if (onMemberLoginPage) {
            mmLoginPage.loginToDashboardMember(email, password);
            Assertion.assertThat("Login to dashboard member successful", mmLoginPage.isLoginToDashboardMemberSuccessful(email), is(true));
        } else {
            Assertion.assertThat("User has been logged in to Member Dashboard", mmLoginPage.isLoginToDashboardMemberSuccessful(email), is(true));
        }

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Go to Commission Page (Property commission)");
        sideMenu.clickFinanceMenu();
        sideMenu.clickCommissionMenu();
        Assertion.assertThat("Commission Page is Loaded", commissionPage.isCommissionPageLoaded(), is(true));

        step("Property commission: capture current %, submit testdata new value, verify request popup");
        commissionPage.clickRequestChangeButton();
        Assertion.assertThat("The Property Commission Form page is loaded", commissionPage.isPropertyCommissionFormPageLoaded(), is(true));
        String savedOriginalPropertyPercent = commissionPage.getPropertyCommissionFromFormInput();
        Assertion.assertThat("Current property commission from form. Expected: non-empty | Actual: \"" + savedOriginalPropertyPercent + "\"", !savedOriginalPropertyPercent.isEmpty(), is(true));
        commissionPage.enterCommission(newPropertyPercent);
        commissionPage.clickRequestChangeButtonAgain();
        Assertion.assertThat("Successfully request hotel commission change toast (new value)", commissionPage.isSuccessfullyRequestHotelCommissionChangeToastLoaded(), is(true));
        commissionPage.clickSeeRequestButton();
        Assertion.assertThat("Commission changes request Popup is present", commissionPage.isCommissionChangesRequestPopupLoaded(), is(true));
        Assertion.assertThat("Current Commission is present", commissionPage.isCurrentCommissionLoaded(), is(true));
        String proposedPropertyRowText = commissionPage.getProposedNewPropertyCommissionRowTextForMessage(newPropertyPercent);
        Assertion.assertThat("Proposed New Commission. Expected row contains: \"" + newPropertyPercent + "\" | Actual row text: \"" + proposedPropertyRowText + "\"", proposedPropertyRowText.contains(newPropertyPercent), is(true));
        step("Property commission: Approve request then Save without agreement");
        commissionPage.approvePropertyCommissionChangeRequestAndConfirmSaveWithoutAgreement();
        Assertion.assertThat("Success toast after property Approve & Save without agreement", commissionPage.isSuccessfullyRequestHotelCommissionChangeToastLoaded(), is(true));

        step("Property commission: restore captured original % and verify request popup");
        commissionPage.reloadBrowserPage();
        Assertion.assertThat("Commission Page loaded after refresh", commissionPage.isCommissionPageLoaded(), is(true));
        commissionPage.clickRequestChangeButton();
        Assertion.assertThat("Property commission form loaded for revert", commissionPage.isPropertyCommissionFormPageLoaded(), is(true));
        commissionPage.enterCommission(savedOriginalPropertyPercent);
        commissionPage.clickRequestChangeButtonAgain();
        Assertion.assertThat("Successfully request hotel commission change toast (revert)", commissionPage.isSuccessfullyRequestHotelCommissionChangeToastLoaded(), is(true));
        commissionPage.clickSeeRequestButton();
        Assertion.assertThat("Commission revert popup present", commissionPage.isCommissionChangesRequestPopupLoaded(), is(true));
        String proposedRevertRowText = commissionPage.getProposedNewPropertyCommissionRowTextForMessage(savedOriginalPropertyPercent);
        Assertion.assertThat("Proposed after revert. Expected row contains: \"" + savedOriginalPropertyPercent + "\" | Actual row text: \"" + proposedRevertRowText + "\"", proposedRevertRowText.contains(savedOriginalPropertyPercent), is(true));
        step("Property revert: Approve request then Save without agreement");
        commissionPage.approvePropertyCommissionChangeRequestAndConfirmSaveWithoutAgreement();
        Assertion.assertThat("Success toast after property revert Approve & Save without agreement", commissionPage.isSuccessfullyRequestHotelCommissionChangeToastLoaded(), is(true));

        step("Add-ons commission: capture current %, submit testdata new values, verify popup");
        commissionPage.reloadBrowserPage();
        Assertion.assertThat("Commission Page loaded", commissionPage.isCommissionPageLoaded(), is(true));
        commissionPage.clickAddOnsCommissionTab();
        Assertion.assertThat("Add Ons Commission Page is Loaded", commissionPage.isAddOnsCommissionPageLoaded(), is(true));
        commissionPage.clickAddOnsRequestChangeButton();
        Assertion.assertThat("AddOns commission form loaded", commissionPage.isAddOnsCommissionFormPageLoaded(), is(true));
        String savedOriginalMealsPercent = commissionPage.getMealsCommissionFromFormInput();
        String savedOriginalRoomComplementsPercent = commissionPage.getRoomComplementsCommissionFromFormInput();
        String savedOriginalTransportsPercent = commissionPage.getTransportsCommissionFromFormInput();
        String savedOriginalActivitiesPercent = commissionPage.getActivitiesCommissionFromFormInput();
        Assertion.assertThat("Add-ons Meals from form (non-empty). Expected: non-empty | Actual: \"" + savedOriginalMealsPercent + "\"", !savedOriginalMealsPercent.isEmpty(), is(true));
        Assertion.assertThat("Add-ons Room from form (non-empty). Expected: non-empty | Actual: \"" + savedOriginalRoomComplementsPercent + "\"", !savedOriginalRoomComplementsPercent.isEmpty(), is(true));
        Assertion.assertThat("Add-ons Transport from form (non-empty). Expected: non-empty | Actual: \"" + savedOriginalTransportsPercent + "\"", !savedOriginalTransportsPercent.isEmpty(), is(true));
        Assertion.assertThat("Add-ons Activities from form (non-empty). Expected: non-empty | Actual: \"" + savedOriginalActivitiesPercent + "\"", !savedOriginalActivitiesPercent.isEmpty(), is(true));
        commissionPage.enterMealsCommission(newMealsPercent);
        commissionPage.enterRoomComplementsCommission(newRoomComplementsPercent);
        commissionPage.enterTransportsCommission(newTransportsPercent);
        commissionPage.enterActivitiesCommission(newActivitiesPercent);
        commissionPage.clickSendRequestButton();
        Assertion.assertThat("Add-ons request toast (new values)", commissionPage.isSuccessfullyRequestHotelCommissionChangeToastLoaded(), is(true));
        commissionPage.clickSeeChangeButton();
        Assertion.assertThat("Add Ons request Popup present", commissionPage.isCommissionChangesRequestPopupLoaded(), is(true));
        Assertion.assertThat("Current Add Ons in popup", commissionPage.isCurrentAddOnsCommissionLoaded(), is(true));
        Assertion.assertThat("Proposed add-ons section visible", commissionPage.isProposedNewAddOnsCommissionLoaded(), is(true));
        String actualMealsProposed = commissionPage.getProposedMealsCommissionTextFromRequestPopup();
        Assertion.assertThat("Proposed Meal. Expected (contains): \"" + newMealsPercent + "\" | Actual: \"" + actualMealsProposed + "\"", actualMealsProposed, containsString(newMealsPercent));
        String actualRoomProposed = commissionPage.getProposedRoomComplementsCommissionTextFromRequestPopup();
        Assertion.assertThat("Proposed Room. Expected (contains): \"" + newRoomComplementsPercent + "\" | Actual: \"" + actualRoomProposed + "\"", actualRoomProposed, containsString(newRoomComplementsPercent));
        String actualTransportProposed = commissionPage.getProposedTransportsCommissionTextFromRequestPopup();
        Assertion.assertThat("Proposed Transport. Expected (contains): \"" + newTransportsPercent + "\" | Actual: \"" + actualTransportProposed + "\"", actualTransportProposed, containsString(newTransportsPercent));
        String actualActivitiesProposed = commissionPage.getProposedActivitiesCommissionTextFromRequestPopup();
        Assertion.assertThat("Proposed Activities. Expected (contains): \"" + newActivitiesPercent + "\" | Actual: \"" + actualActivitiesProposed + "\"", actualActivitiesProposed, containsString(newActivitiesPercent));
        step("Add-ons commission: Approve request");
        commissionPage.approveAddOnsCommissionChangeRequest();
        Assertion.assertThat("Success toast after add-ons Approve", commissionPage.isSuccessfullyRequestHotelCommissionChangeToastLoaded(), is(true));

        step("Add-ons commission: restore captured originals and verify popup");
        commissionPage.reloadBrowserPage();
        Assertion.assertThat("Commission Page loaded before add-ons revert", commissionPage.isCommissionPageLoaded(), is(true));
        commissionPage.clickAddOnsCommissionTab();
        Assertion.assertThat("Add Ons tab after refresh", commissionPage.isAddOnsCommissionPageLoaded(), is(true));
        commissionPage.clickAddOnsRequestChangeButton();
        Assertion.assertThat("AddOns form for revert", commissionPage.isAddOnsCommissionFormPageLoaded(), is(true));
        commissionPage.enterMealsCommission(savedOriginalMealsPercent);
        commissionPage.enterRoomComplementsCommission(savedOriginalRoomComplementsPercent);
        commissionPage.enterTransportsCommission(savedOriginalTransportsPercent);
        commissionPage.enterActivitiesCommission(savedOriginalActivitiesPercent);
        commissionPage.clickSendRequestButton();
        Assertion.assertThat("Add-ons request toast (revert)", commissionPage.isSuccessfullyRequestHotelCommissionChangeToastLoaded(), is(true));
        commissionPage.clickSeeChangeButton();
        Assertion.assertThat("Add-ons revert popup", commissionPage.isCommissionChangesRequestPopupLoaded(), is(true));
        String actualMealsRevert = commissionPage.getProposedMealsCommissionTextFromRequestPopup();
        Assertion.assertThat("Proposed Meal (revert). Expected (contains original): \"" + savedOriginalMealsPercent + "\" | Actual: \"" + actualMealsRevert + "\"", actualMealsRevert, containsString(savedOriginalMealsPercent));
        String actualRoomRevert = commissionPage.getProposedRoomComplementsCommissionTextFromRequestPopup();
        Assertion.assertThat("Proposed Room (revert). Expected (contains original): \"" + savedOriginalRoomComplementsPercent + "\" | Actual: \"" + actualRoomRevert + "\"", actualRoomRevert, containsString(savedOriginalRoomComplementsPercent));
        String actualTransportRevert = commissionPage.getProposedTransportsCommissionTextFromRequestPopup();
        Assertion.assertThat("Proposed Transport (revert). Expected (contains original): \"" + savedOriginalTransportsPercent + "\" | Actual: \"" + actualTransportRevert + "\"", actualTransportRevert, containsString(savedOriginalTransportsPercent));
        String actualActivitiesRevert = commissionPage.getProposedActivitiesCommissionTextFromRequestPopup();
        Assertion.assertThat("Proposed Activities (revert). Expected (contains original): \"" + savedOriginalActivitiesPercent + "\" | Actual: \"" + actualActivitiesRevert + "\"", actualActivitiesRevert, containsString(savedOriginalActivitiesPercent));
        step("Add-ons revert: Approve request");
        commissionPage.approveAddOnsCommissionChangeRequest();
        Assertion.assertThat("Success toast after add-ons revert Approve", commissionPage.isSuccessfullyRequestHotelCommissionChangeToastLoaded(), is(true));
    }

    @Author(names = {"Vipul Kumar"})
    @Module(name = "HotelCreation")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testHotelCreationNewPropertyWithAccommodationTypeHotel() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        HotelCreationPage<?> hotelCreationPage = new HotelCreationPage<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testHotelCreationNewPropertyWithAccommodationTypeHotel");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String propertyCategory = param.get("propertyCategory");
        String guestType = param.get("guestType");
        String propertyType = param.get("propertyType");
        String propertyName = param.get("propertyName") + " " + propertyType + " " + System.currentTimeMillis();
        String propertyNameAlias = param.get("propertyNameAlias");
        int starRating = Integer.parseInt(param.get("starRating"));
        boolean isUseChain = Boolean.parseBoolean(param.get("isUseChain"));
        String currency = param.get("currency");
        String address = param.get("address");
        String zipCode = param.get("zipCode");
        String contactRole = param.get("contactRole");
        String propertyPhoneNumber = param.get("propertyPhoneNumber");
        String companyName = param.get("companyName");
        String companyCountry = param.get("companyCountry");
        String otherPlatformName = param.get("otherPlatformName");
        int facilityIndex1 = Integer.parseInt(param.get("facilityIndex1"));
        int facilityIndex2 = Integer.parseInt(param.get("facilityIndex2"));
        int facilityIndex3 = Integer.parseInt(param.get("facilityIndex3"));
        String propertyPhotoCategory = param.get("propertyPhotoCategory");
        String genderType = param.get("genderType");
        boolean isMarriageRequired = Boolean.parseBoolean(param.get("isMarriageRequired"));
        boolean isDepositRequired = Boolean.parseBoolean(param.get("isDepositRequired"));
        boolean isAllAgeAllowed = Boolean.parseBoolean(param.get("isAllAgeAllowed"));
        boolean isBreakfastAvailable = Boolean.parseBoolean(param.get("isBreakfastAvailable"));
        boolean isSmokingAllowed = Boolean.parseBoolean(param.get("isSmokingAllowed"));
        boolean isAlcoholAllowed = Boolean.parseBoolean(param.get("isAlcoholAllowed"));
        boolean isPetAllowed = Boolean.parseBoolean(param.get("isPetAllowed"));
        String roomType = param.get("roomType");
        String roomName = param.get("roomName");
        String minSellingPrice = param.get("minSellingPrice");
        String maxOccupancy = param.get("maxOccupancy");
        boolean hasMultipleRoom = Boolean.parseBoolean(param.get("hasMultipleRoom"));
        String bedType = param.get("bedType");
        String noOfBed = param.get("noOfBed");
        String roomPhotoCategory = param.get("roomPhotoCategory");
        String paymentMethod = param.get("paymentMethod");
        String bankName = param.get("bankName");
        String branchName = param.get("branchName");
        String accountNumber = param.get("accountNumber");
        String accountHolderName = param.get("accountHolderName");
        String swiftCode = param.get("swiftCode");
        String channelSource = param.get("channelSource");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel and click add new hotel");
        navbar.clickProfile();
        navbar.clickChangeHotelMenu();
        navbar.clickAddNewHotel();
        Assertion.assertThat("Property category page is displayed", hotelCreationPage.isPropertyCategoryPageDisplayed(), is(true));

        step("Choose property category");
        hotelCreationPage.selectPropertyCategory(propertyCategory);
        hotelCreationPage.clickNextSubmitOnPropertyCategoryPage();
        Assertion.assertThat("Select category confirm modal is appeared", hotelCreationPage.isConfirmationPopupDialogAppeared(), is(true));
        hotelCreationPage.confirmPopupDialog();

        step("Fill in guest and property type form");
        Assertion.assertThat("Guest and property type form is displayed", hotelCreationPage.isGuestAndPropertyTypeFormDisplayed(), is(true));
        hotelCreationPage.selectGuestType(guestType);
        hotelCreationPage.searchAndSelectPropertyType(propertyType);
        hotelCreationPage.clickNextSubmit();

        step("Fill in general info form");
        Assertion.assertThat("General info form is displayed", hotelCreationPage.isGeneralInfoFormDisplayed(), is(true));
        hotelCreationPage.enterPropertyAddress(address);
        hotelCreationPage.enterPropertyZipCode(zipCode);
        hotelCreationPage.searchAndSelectMapLocation(address);
        hotelCreationPage.enterPropertyName(propertyName);
        hotelCreationPage.enterPropertyNameAlias(propertyNameAlias);
        hotelCreationPage.selectPropertyStarRating(starRating);
        hotelCreationPage.selectPropertyUseChainRadio(isUseChain);
        hotelCreationPage.searchAndSelectPropertyCurrency(currency);

        step("Verify general info form values");
        String actualAddress = hotelCreationPage.getEnteredPropertyAddress();
        Assertion.assertThat("Property address. Expected: " + address + " | Actual: " + actualAddress, actualAddress, is(equalTo(address)));
        String actualZipCode = hotelCreationPage.getEnteredPropertyZipCode();
        Assertion.assertThat("Property zip code. Expected: " + zipCode + " | Actual: " + actualZipCode, actualZipCode, is(equalTo(zipCode)));
        String actualPropertyName = hotelCreationPage.getEnteredPropertyName();
        Assertion.assertThat("Property name. Expected: " + propertyName + " | Actual: " + actualPropertyName, actualPropertyName, is(equalTo(propertyName)));
        String actualPropertyAlias = hotelCreationPage.getEnteredPropertyNameAlias();
        Assertion.assertThat("Property name alias. Expected: " + propertyNameAlias + " | Actual: " + actualPropertyAlias, actualPropertyAlias, is(equalTo(propertyNameAlias)));
        String actualCurrency = hotelCreationPage.getSelectedPropertyCurrency();
        Assertion.assertThat("Property currency. Expected (contains): " + currency + " | Actual: " + actualCurrency, actualCurrency, containsString(currency));

        hotelCreationPage.clickNextSubmit();

        step("Fill in contact detail form");
        Assertion.assertThat("Contact detail form is displayed", hotelCreationPage.isContactDetailFormDisplayed(), is(true));
        hotelCreationPage.selectContactDetailRole(contactRole);
        hotelCreationPage.enterContactDetailPropertyPhoneNumber(propertyPhoneNumber);

        step("Verify contact detail form values");
        String actualContactRole = hotelCreationPage.getSelectedContactDetailRole();
        Assertion.assertThat("Contact role. Expected (contains): " + contactRole + " | Actual: " + actualContactRole, actualContactRole, containsString(contactRole));
        String actualPropertyPhone = hotelCreationPage.getEnteredContactDetailPropertyPhone();
        Assertion.assertThat("Property phone. Expected (contains): " + propertyPhoneNumber + " | Actual: " + actualPropertyPhone, actualPropertyPhone, containsString(propertyPhoneNumber));

        hotelCreationPage.clickNextSubmit();

        step("Fill in partnership agreement form");
        Assertion.assertThat("Partnership agreement form is displayed", hotelCreationPage.isPartnershipAgreementFormDisplayed(), is(true));
        hotelCreationPage.copyPartnershipAgreementContact();
        hotelCreationPage.enterPartnershipAgreementCompanyAddress(address);
        hotelCreationPage.selectPartnershipAgreementCompanyCountry(companyCountry);
        hotelCreationPage.enterPartnershipAgreementCompanyName(companyName);
        hotelCreationPage.copyPartnershipAgreementContact();
        hotelCreationPage.checkPartnershipAgreement();
        hotelCreationPage.clickNextSubmit();
        hotelCreationPage.confirmPartnershipAgreement();

        step("Fill in other platform form");
        Assertion.assertThat("Other platform form is displayed", hotelCreationPage.isOtherPlatformFormDisplayed(), is(true));
        hotelCreationPage.checkOtherPlatformAvailability(otherPlatformName);
        hotelCreationPage.clickNextSubmit();

        step("Fill in property facility form");
        Assertion.assertThat("Property facility form is displayed", hotelCreationPage.isPropertyFacilityFormDisplayed(), is(true));
        hotelCreationPage.checkPropertyFacilityByIndex(facilityIndex1);
        hotelCreationPage.checkPropertyFacilityByIndex(facilityIndex2);
        hotelCreationPage.checkPropertyFacilityByIndex(facilityIndex3);
        hotelCreationPage.clickNextSubmit();

        step("Fill in upload property photo form");
        Assertion.assertThat("Upload property photo form is displayed", hotelCreationPage.isUploadPropertyPhotoFormDisplayed(), is(true));
        int photoIndex = 1;
        hotelCreationPage.selectPhotoCategoryByIndex(photoIndex, propertyPhotoCategory);
        hotelCreationPage.clickUploadPhotoByIndex(photoIndex);
        Assertion.assertThat("Upload property photo modal is appeared", hotelCreationPage.isUploadPhotoModalAppeared(), is(true));
        hotelCreationPage.uploadPhotoByUrl("sampleImage.jpg");
        hotelCreationPage.uploadPhotoByUrl("sampleImage.jpg");
        hotelCreationPage.uploadPhotoByUrl("sampleImage.jpg");
        hotelCreationPage.uploadPhotoByUrl("sampleImage.jpg");
        hotelCreationPage.refreshPage();
        Assertion.assertThat("Upload property photo form is displayed", hotelCreationPage.isUploadPropertyPhotoFormDisplayed(), is(true));
        hotelCreationPage.selectPhotoCategoryByIndex(photoIndex, propertyPhotoCategory);
        hotelCreationPage.clickUploadPhotoByIndex(photoIndex);
        Assertion.assertThat("Upload property photo modal is appeared", hotelCreationPage.isUploadPhotoModalAppeared(), is(true));
        hotelCreationPage.checkAllUploadedPhoto(hotelCreationPage.getUploadedPhotoOnModal());
        hotelCreationPage.clickAddUploadedPhoto();
        hotelCreationPage.setMainPhotoByIndex(photoIndex);
        hotelCreationPage.clickNextSubmit();

        step("Fill in general policy form");
        Assertion.assertThat("General policy form is displayed", hotelCreationPage.isGeneralPolicyFormDisplayed(), is(true));
        hotelCreationPage.selectGeneralPolicyGenderType(genderType);
        hotelCreationPage.selectGeneralPolicyByType("MARRIAGE_REGULATION", isMarriageRequired);
        hotelCreationPage.selectGeneralPolicyByType("PAY_DEPOSIT", isDepositRequired);
        hotelCreationPage.selectGeneralPolicyByType("ALLOW_ALL_AGE", isAllAgeAllowed);
        hotelCreationPage.selectGeneralPolicyByType("BREAKFAST", isBreakfastAvailable);
        hotelCreationPage.selectGeneralPolicyByType("ALLOW_SMOKING", isSmokingAllowed);
        hotelCreationPage.selectGeneralPolicyByType("ALLOW_ALCOHOL", isAlcoholAllowed);
        hotelCreationPage.selectGeneralPolicyByType("ALLOW_PETS", isPetAllowed);
        hotelCreationPage.clickNextSubmit();

        step("Fill in room listing form");
        Assertion.assertThat("Room listing form is displayed", hotelCreationPage.isRoomListingFormDisplayed(), is(true));
        hotelCreationPage.selectRoomType(roomType);
        hotelCreationPage.enterRoomName(roomName);
        hotelCreationPage.selectRoomSmokingPreference(isSmokingAllowed);
        hotelCreationPage.enterRoomMinimumSellingPrice(minSellingPrice);
        hotelCreationPage.enterRoomMaxOccupancy(maxOccupancy);

        hotelCreationPage.selectRoomHasMultipleBedroom(hasMultipleRoom);
        hotelCreationPage.enterRoomConfiguration(1, 0, bedType, noOfBed);
        hotelCreationPage.selectRoomFacility(facilityIndex1);
        hotelCreationPage.selectRoomFacility(facilityIndex2);
        hotelCreationPage.selectRoomFacility(facilityIndex3);
        hotelCreationPage.selectPhotoCategoryByIndex(photoIndex, roomPhotoCategory);
        hotelCreationPage.clickUploadPhotoByIndex(photoIndex);
        Assertion.assertThat("Upload property photo modal is appeared", hotelCreationPage.isUploadPhotoModalAppeared(), is(true));
        hotelCreationPage.checkUploadedPhotoByIndex(1);
        hotelCreationPage.clickAddUploadedPhoto();
        hotelCreationPage.setMainPhotoByIndex(photoIndex);
        hotelCreationPage.clickNextSubmit();
        Assertion.assertThat("Room listing review form is displayed", hotelCreationPage.isRoomListingReviewFormDisplayed(), is(true));
        hotelCreationPage.clickNextSubmit();

        step("Fill in payment detail form");
        Assertion.assertThat("Payment detail form is displayed", hotelCreationPage.isPaymentDetailFormDisplayed(), is(true));
        hotelCreationPage.selectPaymentMethod(paymentMethod);
        hotelCreationPage.selectBankName(bankName);
        hotelCreationPage.enterBankBranchName(branchName);
        hotelCreationPage.enterBankAccountNumber(accountNumber);
        hotelCreationPage.enterBankAccountHolderName(accountHolderName);
        hotelCreationPage.enterBankSwiftCode(swiftCode);
        hotelCreationPage.selectProvideItLater();
        hotelCreationPage.clickNextSubmit();

        step("Fill in registration review form");
        Assertion.assertThat("Registration review form is displayed", hotelCreationPage.isRegistrationReviewFormDisplayed(), is(true));
        hotelCreationPage.selectChannelSource(channelSource);
        hotelCreationPage.clickNextSubmit();

        step("Confirm registration");
        Assertion.assertThat("Registration confirmation modal is appeared", hotelCreationPage.isRegistrationConfirmModalAppeared(), is(true));
        hotelCreationPage.confirmSubmitRegistration();
        Assertion.assertThat("Registration success page is displayed", hotelCreationPage.isRegistrationSuccessPageDisplayed(), is(true));

        step("Go to extranet home page and verify newly registered hotel on navbar");
        hotelCreationPage.goToExtranetHomeFromRegistrationSuccessPage();

        String actualHotelNameOnNavbar = navbar.getDisplayedHotelName().orElse("");
        Assertion.assertThat(
                "Hotel name on header. Expected: " + propertyName + " | Actual: " + actualHotelNameOnNavbar,
                actualHotelNameOnNavbar, containsString(propertyName));
    }

    @Author(names = {"Vipul Kumar"})
    @Module(name = "PropertyGeneralInfo")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserCanUpdatePropertyGeneralInfo() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        ExtranetPage<?> extranetPage = new ExtranetPage<>(session);
        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserCanUpdatePropertyGeneralInfo");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");
        int targetStarRating = Integer.parseInt(param.get("starRating"));

        LoginPage<?> mmLoginPage = new LoginPage<>(session);
        step("Member login (admin/member/login only — no MM dashboard URL)");
        mmLoginPage.goToDashboardMemberLoginPage();
        boolean onMemberLoginPage = mmLoginPage.isDashboardMemberLoginPageLoaded();
        if (onMemberLoginPage) {
            mmLoginPage.loginToDashboardMember(email, password);
            Assertion.assertThat("Login to dashboard member successful", mmLoginPage.isLoginToDashboardMemberSuccessful(email), is(true));
        } else {
            Assertion.assertThat("User has been logged in to Member Dashboard", mmLoginPage.isLoginToDashboardMemberSuccessful(email), is(true));
        }

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Navigate to Property > Property General Info");
        sideMenu.clickPropertyMenu();
        sideMenu.clickPropertyGeneralInfoMenu();

        step("Verify Property General Info page is loaded");
        Assertion.assertThat("Property General Info page is loaded", extranetPage.isPropertyGeneralInfoPageLoaded(), is(true));

        step("Click REQUEST CHANGES button");
        extranetPage.clickPropertyGeneralInfoRequestChangesButton();

        String beforeUpdatePropertyName = extranetPage.getDisplayedPropertyNameFromInput();
        String beforeUpdateAlias = extranetPage.getDisplayedAliasFromInput();
        int beforeUpdateStarRating = extranetPage.getDisplayedStarRating();
        step(String.format("Capture current values from UI (Property Name: %s, Alias: %s, Star Rating: %d)",
                beforeUpdatePropertyName, beforeUpdateAlias, beforeUpdateStarRating));

        String afterUpdatePropertyName = beforeUpdatePropertyName + " Update";
        String afterUpdateAlias = beforeUpdateAlias + " Update";
        int afterUpdateStarRating = targetStarRating;

        step(String.format("Apply update values on the form (Property Name: %s, Alias: %s, Star Rating: %d)",
                afterUpdatePropertyName, afterUpdateAlias, afterUpdateStarRating));
        extranetPage.enterPropertyGeneralInfoPropertyName(afterUpdatePropertyName);
        extranetPage.enterPropertyGeneralInfoAlias(afterUpdateAlias);
        extranetPage.selectPropertyGeneralInfoStarRating(afterUpdateStarRating);

        step("Submit the updated changes via SAVE CHANGES");
        extranetPage.clickPropertyGeneralInfoSaveChangesButton();

        step("Verify success toast appears after update submit");
        Assertion.assertThat("Success toast visible after update submit",
                extranetPage.isPropertyGeneralInfoSuccessToastVisible(), is(true));

        step("Wait for the success toast to dismiss before next action");
        boolean updateSubmitToastDismissed = extranetPage.waitForPropertyGeneralInfoSuccessToastToDisappear();

        step("Verify the success toast dismissed after update submit");
        Assertion.assertThat("Success toast dismissed after update submit",
                updateSubmitToastDismissed, is(true));

        step("Click See Change link to open the requested change preview");
        extranetPage.clickPropertyGeneralInfoSeeChangeLink();

        step("Verify Changes modal is visible after update");
        Assertion.assertThat("Changes modal is visible after update", extranetPage.isPropertyGeneralInfoChangesModalVisible(), is(true));

        step("Verify Before: Property Name = " + beforeUpdatePropertyName);
        Assertion.assertThat("Before: Property Name", extranetPage.getPropertyGeneralInfoChangesModalBeforePropertyName(), is(beforeUpdatePropertyName));

        step("Verify Before: Aliases = " + beforeUpdateAlias);
        Assertion.assertThat("Before: Aliases", extranetPage.getPropertyGeneralInfoChangesModalBeforeAlias(), is(beforeUpdateAlias));

        step("Verify Before: Star Rating = " + beforeUpdateStarRating);
        Assertion.assertThat("Before: Star Rating", extranetPage.getPropertyGeneralInfoChangesModalBeforeStarRating(), is(beforeUpdateStarRating));

        step("Verify After: Property Name = " + afterUpdatePropertyName);
        Assertion.assertThat("After: Property Name", extranetPage.getPropertyGeneralInfoChangesModalAfterPropertyName(), is(afterUpdatePropertyName));

        step("Verify After: Aliases = " + afterUpdateAlias);
        Assertion.assertThat("After: Aliases", extranetPage.getPropertyGeneralInfoChangesModalAfterAlias(), is(afterUpdateAlias));

        step("Verify After: Star Rating = " + afterUpdateStarRating);
        Assertion.assertThat("After: Star Rating", extranetPage.getPropertyGeneralInfoChangesModalAfterStarRating(), is(afterUpdateStarRating));

        step("Confirm the requested changes via SAVE in the modal");
        extranetPage.clickPropertyGeneralInfoChangesModalSaveButton();

        step("Verify success toast appears after confirming update from modal");
        Assertion.assertThat("Success toast visible after confirming update from modal",
                extranetPage.isPropertyGeneralInfoSuccessToastVisible(), is(true));

        step("Wait for the success toast to dismiss before next action");
        boolean modalUpdateToastDismissed = extranetPage.waitForPropertyGeneralInfoSuccessToastToDisappear();

        step("Verify the success toast dismissed after confirming update from modal");
        Assertion.assertThat("Success toast dismissed after confirming update from modal",
                modalUpdateToastDismissed, is(true));

        String beforeRevertPropertyName = afterUpdatePropertyName;
        String beforeRevertAlias = afterUpdateAlias;
        int beforeRevertStarRating = afterUpdateStarRating;

        String afterRevertPropertyName = beforeUpdatePropertyName;
        String afterRevertAlias = beforeUpdateAlias;
        int afterRevertStarRating = beforeUpdateStarRating;

        step("Re-enter edit mode for revert via REQUEST CHANGES");
        extranetPage.clickPropertyGeneralInfoRequestChangesButton();

        step(String.format("Apply revert values on the form (Property Name: %s, Alias: %s, Star Rating: %d)",
                afterRevertPropertyName, afterRevertAlias, afterRevertStarRating));
        extranetPage.enterPropertyGeneralInfoPropertyName(afterRevertPropertyName);
        extranetPage.enterPropertyGeneralInfoAlias(afterRevertAlias);
        extranetPage.selectPropertyGeneralInfoStarRating(afterRevertStarRating);

        step("Submit the reverted changes via SAVE CHANGES");
        extranetPage.clickPropertyGeneralInfoSaveChangesButton();

        step("Verify success toast appears after revert submit");
        Assertion.assertThat("Success toast visible after revert submit",
                extranetPage.isPropertyGeneralInfoSuccessToastVisible(), is(true));

        step("Wait for the success toast to dismiss before next action");
        boolean revertSubmitToastDismissed = extranetPage.waitForPropertyGeneralInfoSuccessToastToDisappear();

        step("Verify the success toast dismissed after revert submit");
        Assertion.assertThat("Success toast dismissed after revert submit",
                revertSubmitToastDismissed, is(true));

        step("Click See Change link to open the revert preview");
        extranetPage.clickPropertyGeneralInfoSeeChangeLink();

        step("Verify Changes modal is visible after revert");
        Assertion.assertThat("Changes modal is visible after revert", extranetPage.isPropertyGeneralInfoChangesModalVisible(), is(true));

        step("Verify Before (revert): Property Name = " + beforeRevertPropertyName);
        Assertion.assertThat("Before: Property Name (revert)", extranetPage.getPropertyGeneralInfoChangesModalBeforePropertyName(), is(beforeRevertPropertyName));

        step("Verify Before (revert): Aliases = " + beforeRevertAlias);
        Assertion.assertThat("Before: Aliases (revert)", extranetPage.getPropertyGeneralInfoChangesModalBeforeAlias(), is(beforeRevertAlias));

        step("Verify Before (revert): Star Rating = " + beforeRevertStarRating);
        Assertion.assertThat("Before: Star Rating (revert)", extranetPage.getPropertyGeneralInfoChangesModalBeforeStarRating(), is(beforeRevertStarRating));

        step("Verify After (revert): Property Name = " + afterRevertPropertyName);
        Assertion.assertThat("After: Property Name (revert)", extranetPage.getPropertyGeneralInfoChangesModalAfterPropertyName(), is(afterRevertPropertyName));

        step("Verify After (revert): Aliases = " + afterRevertAlias);
        Assertion.assertThat("After: Aliases (revert)", extranetPage.getPropertyGeneralInfoChangesModalAfterAlias(), is(afterRevertAlias));

        step("Verify After (revert): Star Rating = " + afterRevertStarRating);
        Assertion.assertThat("After: Star Rating (revert)", extranetPage.getPropertyGeneralInfoChangesModalAfterStarRating(), is(afterRevertStarRating));

        step("Confirm the reverted changes via SAVE in the modal");
        extranetPage.clickPropertyGeneralInfoChangesModalSaveButton();

        step("Verify success toast appears after confirming revert from modal");
        Assertion.assertThat("Success toast visible after confirming revert from modal",
                extranetPage.isPropertyGeneralInfoSuccessToastVisible(), is(true));

        step("Wait for the success toast to dismiss");
        boolean modalRevertToastDismissed = extranetPage.waitForPropertyGeneralInfoSuccessToastToDisappear();

        step("Verify the success toast dismissed after confirming revert from modal");
        Assertion.assertThat("Success toast dismissed after confirming revert from modal",
                modalRevertToastDismissed, is(true));
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "ManageRatesAndAvailability")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToUpdateMultipleDatesOnCalendar() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        ManageRatesAndAvailabilityPage<?> manageRatesAndAvailabilityPage = new ManageRatesAndAvailabilityPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToUpdateMultipleDatesOnCalendar");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");
        String allotment = param.get("allotment");
        String minValidRate = param.get("rate");
        int startDateIndex = Integer.parseInt(param.get("startDateIndex"));
        int endDateIndex = Integer.parseInt(param.get("endDateIndex"));
        String expectedAllotmentTextEn = param.get("emptyAllotmentErrorEN");
        String expectedRateTextEn = param.get("emptyRateErrorEN");
        String expectedAllotmentTextId = param.get("emptyAllotmentErrorID");
        String expectedRateTextId = param.get("emptyRateErrorID");
        String expectedToastEN = param.get("expectedToastMessageEN");
        String expectedToastID = param.get("expectedToastMessageID");
        String value = param.get("value");
        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Go to Manage Rates and Availability page");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickManageRatesAndAvailabilityMenu();
        Assertion.assertThat("Manage Rates and Availability page is loaded", manageRatesAndAvailabilityPage.isManageRatesAndAvailabilityPageLoaded(), is(true));

        step("Verify the calendar page open successfully");
        manageRatesAndAvailabilityPage.verifyCalendarPageOpened();

        step("Get the current month");
        String oldMonth = manageRatesAndAvailabilityPage.getCurrentCalendarMonth();

        step("Navigate to the next month on the calendar");
        manageRatesAndAvailabilityPage.navigateToNextMonth(1);

        step("Get the month after navigating to next month on the calendar");
        String newMonth = manageRatesAndAvailabilityPage.getCurrentCalendarMonth();
        String assertionMessage = String.format("NewMonth changed after navigating. Current Month: [%s], Next Month: [%s]", oldMonth, newMonth);
        Assertion.assertThat(assertionMessage, !newMonth.equalsIgnoreCase(oldMonth), is(true));

        step("Select two dates from the calendar");
        manageRatesAndAvailabilityPage.dragSelectDatesByIndices(startDateIndex, endDateIndex);

        step("Verify that the exact selected date range indices are highlighted correctly on the calendar");
        Assertion.assertThat("The selected date cell range is highlighted on the active grid layout", manageRatesAndAvailabilityPage.areIndicesHighlighted(startDateIndex), is(true));
        Assertion.assertThat("The right drawer is loaded", manageRatesAndAvailabilityPage.isTheRightDrawerLoaded(), is(true));

        step("[Negative Scenario 1] Clear the allotment field and all rate fields for every available rate plan");
        manageRatesAndAvailabilityPage.clearGlobalAllotmentInput();
        manageRatesAndAvailabilityPage.clearAllRatePlanInputs();

        step("Click the SAVE button to attempt submission of empty configurations");
        manageRatesAndAvailabilityPage.clickDrawerSaveButton();

        step("Verify validation error messages appear for all mandatory empty fields");
        manageRatesAndAvailabilityPage.verifyAllotmentErrorMessage(expectedAllotmentTextEn, expectedAllotmentTextId);
        manageRatesAndAvailabilityPage.verifyRateErrorsOneByOne(expectedRateTextEn, expectedRateTextId, value);

        step("Verify no success toast/snackbar appears after invalid save attempt");
        manageRatesAndAvailabilityPage.verifyNoSuccessToastAppeared();

        step("[Negative Scenario 2] Enter 0 in the allotment field for every available rate plan");
        manageRatesAndAvailabilityPage.fillAllotmentForAllRatePlans("0");

        step("Enter 1 in the rate field for every available rate plan");
        manageRatesAndAvailabilityPage.fillRateForAllRatePlans("1");

        step("Click SAVE to attempt submission with allotment=0 and rate=1 (below threshold)");
        manageRatesAndAvailabilityPage.clickDrawerSaveButton();

        step("Verify that rate validation error messages appear only for the rate fields (rate=1 is below minimum threshold)");
        manageRatesAndAvailabilityPage.verifyRateErrorsOneByOne(expectedRateTextEn, expectedRateTextId, value);

        step("Verify that NO allotment validation error is displayed when allotment=0 is entered");
        manageRatesAndAvailabilityPage.verifyNoAllotmentErrorDisplayed();

        step("Verify no success toast after saving with rate below threshold");
        manageRatesAndAvailabilityPage.verifyNoSuccessToastAppeared();

        step("[Positive Scenario] Enter " + allotment + " in the allotment field for every available rate plan");
        manageRatesAndAvailabilityPage.fillAllotmentForAllRatePlans(allotment);

        step("Enter the minimum valid 7-digit rate value in the rate field for every available rate plan");
        manageRatesAndAvailabilityPage.fillRateForAllRatePlans(minValidRate);

        step("Click SAVE to submit the valid rate and availability adjustment");
        manageRatesAndAvailabilityPage.clickDrawerSaveButton();

        step("Verify that a confirmation popup appears after clicking SAVE with valid data");
        Assertion.assertThat("Confirmation popup is displayed after clicking SAVE with valid data", manageRatesAndAvailabilityPage.isConfirmationPopupDisplayed(), is(true));

        step("Verify that the confirmation popup displays the same selected dates for which the adjustment was made");
        String drawerStartDate = manageRatesAndAvailabilityPage.getRightDrawerStartDateValue();
        String drawerEndDate = manageRatesAndAvailabilityPage.getRightDrawerEndDateValue();
        String confirmationDateText = manageRatesAndAvailabilityPage.getConfirmationPopupDateText();
        Assertion.assertThat("Confirmation popup date text contains selected start date [" + drawerStartDate + "]. Actual text context: " + confirmationDateText, confirmationDateText.contains(drawerStartDate), is(true));
        Assertion.assertThat("Confirmation popup date text contains selected end date [" + drawerEndDate + "]. Actual text context: " + confirmationDateText, confirmationDateText.contains(drawerEndDate), is(true));

        step("Click YES, SAVE or YA,SIMPAN in the confirmation popup to confirm and persist the adjustment");
        manageRatesAndAvailabilityPage.clickConfirmationSaveButton();

        step("Verify that a success toast/snackbar appears after saving valid data");
        Assertion.assertThat("Success toast appeared after saving valid rate and availability adjustment", manageRatesAndAvailabilityPage.isSuccessToastAppeared(), is(true));
        manageRatesAndAvailabilityPage.verifySuccessToastMessage(expectedToastEN, expectedToastID);

        step("Wait until the success toast/snackbar disappears automatically");
        boolean toastDismissed = manageRatesAndAvailabilityPage.waitForSuccessToastToDisappear();
        Assertion.assertThat("Success toast dismissed after saving", toastDismissed, is(true));

        step("Verify that the calendar displays allotment value " + allotment + " for the selected dates after saving");
        manageRatesAndAvailabilityPage.verifyCalendarAllotmentForSelectedDates(startDateIndex, endDateIndex, allotment);

        step("Reopen the same selected dates from the calendar by repeating the drag-select operation");
        manageRatesAndAvailabilityPage.dragSelectDatesByIndices(startDateIndex, endDateIndex);

        step("Verify that the rate and availability adjustment panel (right drawer) opens for the same selected dates");
        Assertion.assertThat("Right drawer reopened successfully for the same selected dates", manageRatesAndAvailabilityPage.isTheRightDrawerLoaded(), is(true));

        step("Skip coachmark if it appears after reopening the drawer");
        manageRatesAndAvailabilityPage.skipRightDrawerCoachmark();

        step("Verify that the allotment value is displayed as 1000 in the drawer after reopening the saved dates");
        manageRatesAndAvailabilityPage.verifyDrawerAllotmentValue(allotment);

        step("Verify that each available rate plan displays the saved 7-digit minimum valid rate value in the rate field");
        manageRatesAndAvailabilityPage.verifyDrawerRateValuesForAllRatePlans(minValidRate);

        step("Verify that no validation error messages are displayed after reopening the saved dates");
        manageRatesAndAvailabilityPage.verifyNoValidationErrorsInDrawer();
    }

    @Author(names = {"Abhishek Sharma"})
    @Module(name = "ManageRatesAndAvailability")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserCanBulkUpdateRoomAllotment() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        BulkUpdatePage<?> bulkUpdatePage = new BulkUpdatePage<>(session);
        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserCanBulkUpdateRoomAllotment");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");
        String[] dateIndexes = {param.get("singleDateRange"), param.get("multiDateRange")};
        String[] values = {param.get("negativeValue"), param.get("zeroValue"), param.get("positiveValue")};
        String expectedModalDescDelimiter = param.get("expectedModalDescDelimiter");
        String[] expectedModalTitle = param.get("expectedModalTitle").split(Pattern.quote(param.get("separator")));
        String[] negativeValueErrorMessage = param.get("negativeValueErrorMessage").split(Pattern.quote(param.get("separator")));

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotelId from testData. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelIdMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel Id on navbar. Expected: " + hotelId + " Actual: " + (hotelIdMatch ? hotelId : "no match"), hotelIdMatch, is(true));

        step("Open Rates & Availability Menu");
        sideMenu.clickRatesAndAvailabilityMenu();

        step("Click Bulk Update Menu");
        sideMenu.clickBulkUpdateMenu();

        boolean isBulkUpdatePageLoaded = bulkUpdatePage.isBulkUpdatePageLoaded();
        Assertion.assertThat("Verify bulk update page loaded", isBulkUpdatePageLoaded, is(true));

        step("Click Allotment Tab");
        bulkUpdatePage.clickAllotmentTab();

        for (String dateIndex : dateIndexes) {
            String[] indexes = dateIndex.split("-");
            int startIndex = Integer.parseInt(indexes[0]);
            int endIndex = Integer.parseInt(indexes[1]);

            step("Selecting " + (startIndex == endIndex ? "Single" : "Multi") + " Date Range");
            String dateRange = bulkUpdatePage.selectDateRangeByIndex(startIndex, endIndex);
            String enDateRange = bulkUpdatePage.formatCalendarDateWithLang(dateRange);
            String idDateRange = bulkUpdatePage.toBahasaDateRange(enDateRange);

            // Modal renders in the UI language, so substitute the English date into the English text and the Bahasa date into the Bahasa text.
            String[] expectedModalDesc = param.get("expectedModalDesc").split(Pattern.quote(param.get("separator")));
            expectedModalDesc[0] = expectedModalDesc[0].replace(expectedModalDescDelimiter, enDateRange);
            expectedModalDesc[1] = expectedModalDesc[1].replace(expectedModalDescDelimiter, idDateRange);
            int alternateBit = 0;

            for (String value : values) {
                if (alternateBit == 0) {
                    step("Click on select room type input field");
                    bulkUpdatePage.clickRoomTypeInput();

                    step("Select All room types");
                    bulkUpdatePage.clickSelectAllRoomTypeCheckbox();

                    step("Click Apply button");
                    bulkUpdatePage.clickApplyRoomTypeButton();
                }
                alternateBit = 1 - alternateBit;

                step("Fill Allotment value for all room types: " + value);
                List<String> allotmentInputsFields = bulkUpdatePage.getAllAllotmentInputFields();
                bulkUpdatePage.fillOutAllAllotmentInputs(value);

                if (Integer.parseInt(value) < 0) {
                    step("For Allotment value = " + value + " check for errors");
                    List<String> allotmentInputsErrors = bulkUpdatePage.getAllotmentInputsErrors();
                    Assertion.assertThat("Verify error's size as same as room size", allotmentInputsErrors.size(), is(allotmentInputsFields.size()));
                    for (int i = 0; i < allotmentInputsErrors.size(); i++) {
                        Assertion.assertThat("Verify error message for room name: " + allotmentInputsFields.get(i), allotmentInputsErrors.get(i),
                                anyOf(containsString(negativeValueErrorMessage[0]), containsString(negativeValueErrorMessage[1])));
                    }

                    boolean isSaveChangesButtonClickable = bulkUpdatePage.isSaveChangesButtonClickable();
                    Assertion.assertThat("Save changes button is not clickable", isSaveChangesButtonClickable, is(false));
                } else {
                    step("Click save changes button");
                    boolean isSaveChangesButtonClickable = bulkUpdatePage.isSaveChangesButtonClickable();
                    Assertion.assertThat("Save changes button is clickable", isSaveChangesButtonClickable, is(true));
                    bulkUpdatePage.clickSaveChangesButton();

                    step("Verify modal appears");
                    boolean isModalAppear = bulkUpdatePage.isAllotmentChangesSaveChangesModalAppears();
                    Assertion.assertThat("Verify Modal appears", isModalAppear, is(true));

                    step("Verify modal title and description");
                    String modalTitle = bulkUpdatePage.allotmentChangesSaveChangesModalTitle();
                    String modalDesc = bulkUpdatePage.allotmentChangesSaveChangesModalDesc();

                    Assertion.assertThat("Verify modal title matches", modalTitle,
                            anyOf(containsString(expectedModalTitle[0]), containsString(expectedModalTitle[1])));
                    Assertion.assertThat("Verify modal description matches", modalDesc,
                            anyOf(containsString(expectedModalDesc[0]), containsString(expectedModalDesc[1])));

                    step("Click save changes confirmation modal button");
                    bulkUpdatePage.clickSaveChangesConfirmationButton();

                    step("Verify update preview modal and its content appears");
                    boolean isUpdatePreviewModalAppeared = bulkUpdatePage.isUpdatePreviewModalAppeared();
                    Assertion.assertThat("Verify update preview modal appears", isUpdatePreviewModalAppeared, is(true));

                    String updatePreviewModalDateRange = bulkUpdatePage.getUpdatePreviewModalDateRange();
                    Assertion.assertThat("Verify date range", updatePreviewModalDateRange, anyOf(containsString(enDateRange), containsString(idDateRange)));

                    step("Verify all allotment room names and values");
                    List<BulkUpdatePage.Pair<String, String>> allAllotmentRoomNamesAndValues = bulkUpdatePage.getAllAllotmentRoomNamesAndValues();
                    Assertion.assertThat("Verify Room names size matches", allAllotmentRoomNamesAndValues.size(), is(allotmentInputsFields.size()));
                    for (int i = 0; i < allAllotmentRoomNamesAndValues.size(); i++) {
                        String itemKey = allAllotmentRoomNamesAndValues.get(i).first();
                        String itemValue = allAllotmentRoomNamesAndValues.get(i).second();
                        Assertion.assertThat("Verify room name: " + allotmentInputsFields.get(i), itemKey, containsString(allotmentInputsFields.get(i)));
                        Assertion.assertThat("Verify room value: " + value, itemValue, containsString(value));
                    }

                    step("Click ok on update preview modal");
                    bulkUpdatePage.clickUpdatePreviewModalOKButton();

                    isUpdatePreviewModalAppeared = bulkUpdatePage.isUpdatePreviewModalAppeared();
                    Assertion.assertThat("Verify modal not appears now", isUpdatePreviewModalAppeared, is(false));
                }
            }
        }
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "ManageRatesAndAvailability")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToUpdateSingleDateOnCalendar() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        ManageRatesAndAvailabilityPage<?> manageRatesAndAvailabilityPage = new ManageRatesAndAvailabilityPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToUpdateSingleDateOnCalendar");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");
        String allotment = param.get("allotment");
        String minValidRate = param.get("rate");
        int startDateIndex = Integer.parseInt(param.get("startDateIndex"));
        int endDateIndex = Integer.parseInt(param.get("endDateIndex"));
        Assertion.assertThat("For single-date scenario, startDateIndex and endDateIndex must be equal", startDateIndex, is(endDateIndex));
        String expectedAllotmentTextEn = param.get("emptyAllotmentErrorEN");
        String expectedRateTextEn = param.get("emptyRateErrorEN");
        String expectedAllotmentTextId = param.get("emptyAllotmentErrorID");
        String expectedRateTextId = param.get("emptyRateErrorID");
        String expectedToastEN = param.get("expectedToastMessageEN");
        String expectedToastID = param.get("expectedToastMessageID");
        String value = param.get("value");
        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Go to Manage Rates and Availability page");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickManageRatesAndAvailabilityMenu();
        Assertion.assertThat("Manage Rates and Availability page is loaded", manageRatesAndAvailabilityPage.isManageRatesAndAvailabilityPageLoaded(), is(true));

        step("Verify the calendar page open successfully");
        manageRatesAndAvailabilityPage.verifyCalendarPageOpened();

        step("Get the current month");
        String oldMonth = manageRatesAndAvailabilityPage.getCurrentCalendarMonth();

        step("Navigate to the next month on the calendar");
        manageRatesAndAvailabilityPage.navigateToNextMonth(1);

        step("Get the month after navigating to next month on the calendar");
        String newMonth = manageRatesAndAvailabilityPage.getCurrentCalendarMonth();
        String assertionMessage = String.format("NewMonth changed after navigating. Current Month: [%s], Next Month: [%s]", oldMonth, newMonth);
        Assertion.assertThat(assertionMessage, !newMonth.equalsIgnoreCase(oldMonth), is(true));

        step("Select Single date from the calendar");
        manageRatesAndAvailabilityPage.dragSelectDatesByIndices(startDateIndex, endDateIndex);

        step("Verify that the exact selected date range indices are highlighted correctly on the calendar");
        Assertion.assertThat("The selected date cell range is highlighted on the active grid layout", manageRatesAndAvailabilityPage.areIndicesHighlighted(startDateIndex), is(true));
        Assertion.assertThat("The right drawer is loaded", manageRatesAndAvailabilityPage.isTheRightDrawerLoaded(), is(true));

        step("[Negative Scenario 1] Clear the allotment field and all rate fields for every available rate plan");
        manageRatesAndAvailabilityPage.clearGlobalAllotmentInput();
        manageRatesAndAvailabilityPage.clearAllRatePlanInputs();

        step("Click the SAVE button to attempt submission of empty configurations");
        manageRatesAndAvailabilityPage.clickDrawerSaveButton();

        step("Verify validation error messages appear for all mandatory empty fields");
        manageRatesAndAvailabilityPage.verifyAllotmentErrorMessage(expectedAllotmentTextEn, expectedAllotmentTextId);
        manageRatesAndAvailabilityPage.verifyRateErrorsOneByOne(expectedRateTextEn, expectedRateTextId, value);

        step("Verify no success toast/snackbar appears after invalid save attempt");
        manageRatesAndAvailabilityPage.verifyNoSuccessToastAppeared();

        step("[Negative Scenario 2] Enter 0 in the allotment field for every available rate plan");
        manageRatesAndAvailabilityPage.fillAllotmentForAllRatePlans("0");

        step("Enter 1 in the rate field for every available rate plan");
        manageRatesAndAvailabilityPage.fillRateForAllRatePlans("1");

        step("Click SAVE to attempt submission with allotment=0 and rate=1 (below threshold)");
        manageRatesAndAvailabilityPage.clickDrawerSaveButton();

        step("Verify that rate validation error messages appear only for the rate fields (rate=1 is below minimum threshold)");
        manageRatesAndAvailabilityPage.verifyRateErrorsOneByOne(expectedRateTextEn, expectedRateTextId, value);

        step("Verify that NO allotment validation error is displayed when allotment=0 is entered");
        manageRatesAndAvailabilityPage.verifyNoAllotmentErrorDisplayed();

        step("Verify no success toast after saving with rate below threshold");
        manageRatesAndAvailabilityPage.verifyNoSuccessToastAppeared();

        step("[Positive Scenario] Enter " + allotment + " in the allotment field for every available rate plan");
        manageRatesAndAvailabilityPage.fillAllotmentForAllRatePlans(allotment);

        step("Enter the minimum valid 7-digit rate value in the rate field for every available rate plan");
        manageRatesAndAvailabilityPage.fillRateForAllRatePlans(minValidRate);

        step("Click SAVE to submit the valid rate and availability adjustment");
        manageRatesAndAvailabilityPage.clickDrawerSaveButton();

        step("Verify that a confirmation popup appears after clicking SAVE with valid data");
        Assertion.assertThat("Confirmation popup is displayed after clicking SAVE with valid data", manageRatesAndAvailabilityPage.isConfirmationPopupDisplayed(), is(true));

        step("Verify that the confirmation popup displays the same selected dates for which the adjustment was made");
        String drawerStartDate = manageRatesAndAvailabilityPage.getRightDrawerStartDateValue();
        String drawerEndDate = manageRatesAndAvailabilityPage.getRightDrawerEndDateValue();
        String confirmationDateText = manageRatesAndAvailabilityPage.getConfirmationPopupDateText();
        Assertion.assertThat("Confirmation popup date text contains selected start date [" + drawerStartDate + "]. Actual text context: " + confirmationDateText, confirmationDateText.contains(drawerStartDate), is(true));
        Assertion.assertThat("Confirmation popup date text contains selected end date [" + drawerEndDate + "]. Actual text context: " + confirmationDateText, confirmationDateText.contains(drawerEndDate), is(true));
        Assertion.assertThat("Start Date and End Date must match for single date selected start date [" + drawerStartDate + "] selected End date [" + drawerEndDate + "]", drawerStartDate, equalTo(drawerEndDate));

        step("Click YES, SAVE or YA,SIMPAN in the confirmation popup to confirm and persist the adjustment");
        manageRatesAndAvailabilityPage.clickConfirmationSaveButton();

        step("Verify that a success toast/snackbar appears after saving valid data");
        Assertion.assertThat("Success toast appeared after saving valid rate and availability adjustment", manageRatesAndAvailabilityPage.isSuccessToastAppeared(), is(true));
        manageRatesAndAvailabilityPage.verifySuccessToastMessage(expectedToastEN, expectedToastID);

        step("Wait until the success toast/snackbar disappears automatically");
        boolean toastDismissed = manageRatesAndAvailabilityPage.waitForSuccessToastToDisappear();
        Assertion.assertThat("Success toast dismissed after saving", toastDismissed, is(true));

        step("Verify that the calendar displays allotment value " + allotment + " for the selected dates after saving");
        manageRatesAndAvailabilityPage.verifyCalendarAllotmentForSelectedDates(startDateIndex, endDateIndex, allotment);

        step("Reopen the same selected dates from the calendar by repeating the drag-select operation");
        manageRatesAndAvailabilityPage.dragSelectDatesByIndices(startDateIndex, endDateIndex);

        step("Verify that the rate and availability adjustment panel (right drawer) opens for the same selected dates");
        Assertion.assertThat("Right drawer reopened successfully for the same selected dates", manageRatesAndAvailabilityPage.isTheRightDrawerLoaded(), is(true));

        step("Skip coachmark if it appears after reopening the drawer");
        manageRatesAndAvailabilityPage.skipRightDrawerCoachmark();

        step("Verify that the allotment value is displayed as " + allotment + " in the drawer after reopening the saved dates");
        manageRatesAndAvailabilityPage.verifyDrawerAllotmentValue(allotment);

        step("Verify that each available rate plan displays the saved 7-digit minimum valid rate value in the rate field");
        manageRatesAndAvailabilityPage.verifyDrawerRateValuesForAllRatePlans(minValidRate);

        step("Verify that no validation error messages are displayed after reopening the saved dates");
        manageRatesAndAvailabilityPage.verifyNoValidationErrorsInDrawer();
    }

    @Author(names = {"Pankaj Singh Kushwaha","Bikash kumar jha"})
    @Module(name = "Booking")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserCanFilterBookingByItineraryID() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        SideMenu<?> sideMenu = new SideMenu<>(session);
        AuthTest authTest = new AuthTest();
        Navbar<?> navbar = new Navbar<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        BookingPage<?> bookingPage = new BookingPage<>(session);

        Map<String, String> param = getParam("testVerifyUserCanFilterBookingByItineraryID");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");
        String username = param.get("username");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDateRaw = param.get("startDate").trim();
        String startDate = startDateRaw.split("-").length >= 3 ? startDateRaw : generateRandomDate(startDateRaw);
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String guestFullName = randomName() + " automation";
        String guestTitle = salutationList[0];
        String currency = param.get("currency");
        step("Create a booking and get the itinerary ID");

        orderCreationPage.createOrderByHotelIdAndGetItineraryID(username, startDate, hotelId,
                adult, night, room, guestFullName, guestTitle, currency);

        String itineraryID = String.valueOf(orderCreationPage.getItineraryID());
        String checkInDate = String.valueOf(orderCreationPage.getCheckInDate());

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Go to the Booking Section");
        sideMenu.clickBookingMenu();

        step("Go to Search Booking Page");
        sideMenu.clickSearchBookingsMenu();
        Assertion.assertThat("Is Booking Open", bookingPage.isBookingListPageDisplayed(), is(true));

        step("Select the booking search option in dateof Field");
        bookingPage.selectBookingDateType();

        step("Fill the itinerary ID and click on search and verify the search result as per booking data");
        bookingPage.searchAndVerifyBookingRecord(itineraryID, guestFullName, checkInDate);
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "PropertyRoomData")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserCanAddActivateEditAndDeleteRooms() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        PropertyPage<?> propertyPage = new PropertyPage<>(session);
        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserCanAddActivateEditAndDeleteRooms");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");
        String roomName = param.get("roomName");
        String editedRoomName = param.get("editedRoomName");
        String roomType = param.get("roomType");
        String roomNumber = param.get("roomNumber");
        String roomFloorNumber = param.get("roomFloorNumber");
        String numberOfRooms = param.get("numberOfRooms");
        String minPrice = param.get("minPrice");
        String baseRoomSizeStr = param.get("avgRoomSize");
        int expectedBaseSize = Integer.parseInt(baseRoomSizeStr);
        String maxOcc = param.get("maxOcc");
        String maxAdult = param.get("maxAdults");
        String maxChildren = param.get("maxChildren");
        String minOcc = param.get("minOcc");
        String bedRoomCount = param.get("bedRoomCount");
        String extraBedQty = param.get("extraBedQty");
        String desiredBedType = param.get("bedType");
        String bedCount = param.get("bedCount");
        int numOfPhotos = Integer.parseInt(param.get("numPhotos"));
        String roomCreateEn = param.get("roomCreateEn");
        String roomCreateId = param.get("roomCreateId");
        roomCreateEn += " " + roomName + ".";
        roomCreateId += " " + roomName + ".";
        String editedRoomEn = param.get("roomEditEn");
        String editedRoomId = param.get("roomEditId");
        editedRoomEn += " " + editedRoomName + ".";
        editedRoomId += " " + editedRoomName + ".";
        String deleteEn = param.get("roomDeleteEn");
        String deleteId = param.get("roomDeleteId");

        step("Step 1: Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Step 2: Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Step 3: Click the Property Menu from the Side bar and Navigate to Room Data page");
        sideMenu.clickPropertyMenu();
        sideMenu.clickRoomDataMenu();

        step("Step 4: Verify we are navigated to Room Data page");
        Assertion.assertThat("Room Data page is Loaded", propertyPage.isRoomDataPageLoaded(), is(true));

        step("Step 5: Search the Room with Room Name  = " + roomName + " or " + editedRoomName + " If exist then delete and verify it");
        propertyPage.searchRoomName(editedRoomName);
        propertyPage.clickSearch();
        if (propertyPage.isRoomAppeared(editedRoomName)) {
            propertyPage.clickDeleteButton(editedRoomName);
            propertyPage.clickDeleteModalYesButton();
            propertyPage.verifySnackbarMessage(deleteEn, deleteId);
            Assertion.assertThat("Room Deleted Successfully Room Name = " + editedRoomName, !propertyPage.isRoomAppeared(editedRoomName), is(true));
        } else {
            Assertion.assertThat("No Room with Room name = " + editedRoomName + " Exist", !propertyPage.isRoomAppeared(editedRoomName), is(true));
        }

        propertyPage.searchRoomName(roomName);
        propertyPage.clickSearch();
        if (propertyPage.isRoomAppeared(roomName)) {
            propertyPage.clickDeleteButton(roomName);
            propertyPage.clickDeleteModalYesButton();
            propertyPage.verifySnackbarMessage(deleteEn, deleteId);
            Assertion.assertThat("Room Deleted Successfully Room Name = " + roomName, !propertyPage.isRoomAppeared(roomName), is(true));
        } else {
            Assertion.assertThat("No Room with Room name = " + roomName + " Exist", !propertyPage.isRoomAppeared(roomName), is(true));
        }

        step("Step 6: Click on Add new Room Button and verify that we have Navigate to Add New Room Page");
        propertyPage.clickAddNewRoomButton();
        Assertion.assertThat("Verify Add New Room Page is Loaded", propertyPage.isAddNewRoomPageLoaded(), is(true));

        step("Step 7: Without filling mandatory fields, and on click Create Room Button");
        propertyPage.clickCreateRoomButton();

        step("Step 8: Verify validation errors across localized language sets dynamically");
        propertyPage.verifyErrorWithoutFillingTheFields(param);

        step("Step 9: Enter the room name = " + roomName);
        propertyPage.enterRoomName(roomName);
        String actualFilledRoomName = propertyPage.getEnteredRoomName();
        Assertion.assertThat("Verify the filled Original Room Name matches our test data input exactly", actualFilledRoomName, equalTo(roomName));

        step("Step 10: Select Room Type variant [" + roomType + "] and confirm the value updates");
        propertyPage.selectRoomType(roomType);
        String actualSelectedType = propertyPage.getSelectedRoomType();
        Assertion.assertThat("Verify the selected room type reflects our test data requirements perfectly", actualSelectedType, equalTo(roomType));

        step("Step 11: Enter Floor Number as " + roomFloorNumber + " And Room Number as " + roomNumber);
        propertyPage.enterRoomFloorNumber(roomFloorNumber);
        propertyPage.enterRoomNumber(roomNumber);
        String actualFloorNumber = propertyPage.getEnteredRoomFloorNumber();
        Assertion.assertThat("Verify the filled Floor Number matches our test data input", actualFloorNumber, equalTo(roomFloorNumber));
        String actualRoomNumber = propertyPage.getEnteredRoomNumber();
        Assertion.assertThat("Verify the filled Room Number matches our test data input", actualRoomNumber, equalTo(roomNumber));

        step("Step 12: Enter the Description in English and Bahasa and verify field content");
        String expectedDescEn = "This is a comprehensive and long valid room description intended for the automation test in English.";
        String expectedDescId = "Ini adalah deskripsi kamar yang komprehensif, panjang, dan valid yang ditujukan untuk pengujian otomatisasi dalam bahasa Indonesia.";
        propertyPage.enterRoomDescriptionEn(expectedDescEn);
        String actualDescEn = propertyPage.getEnteredRoomDescriptionEn();
        Assertion.assertThat("Verify the filled English description matches our data input exactly", actualDescEn, equalTo(expectedDescEn));
        propertyPage.enterRoomDescriptionId(expectedDescId);
        String actualDescId = propertyPage.getEnteredRoomDescriptionId();
        Assertion.assertThat("Verify the filled Indonesian description matches our data input exactly", actualDescId, equalTo(expectedDescId));

        step("Step 13: Verify stepper button mechanics and fill target room total = " + numberOfRooms);
        propertyPage.clickNumberOfRoomsPlus();
        Assertion.assertThat("Verify clicking '+' increments counter to 1", propertyPage.getNumberOfRoomsValue(), equalTo(1));
        propertyPage.clickNumberOfRoomsMinus();
        Assertion.assertThat("Verify clicking '-' decrements counter back to 0", propertyPage.getNumberOfRoomsValue(), equalTo(0));
        propertyPage.setNumberOfRooms(Integer.parseInt(numberOfRooms));
        Assertion.assertThat("Verify final selected room value tier reflects test data parameters completely. Expected = " + numberOfRooms + " Actual = " + propertyPage.getNumberOfRoomsValue(), propertyPage.getNumberOfRoomsValue(), equalTo(Integer.parseInt(numberOfRooms)));

        step("Step 14: Activate Default Allotment Toggle and set an invalid value to trigger error");
        propertyPage.turnOnDefaultAllotmentToggle();
        Assertion.assertThat("Verify Default Allotment Toggle is ON", propertyPage.isDefaultAllotmentToggleActive(), is(true));
        int invalidAllotmentValue = Integer.parseInt(numberOfRooms) + 1;
        propertyPage.setDefaultAllotmentValue(invalidAllotmentValue);

        step("Step 15: Verify boundary validation error message displays correctly in either language");
        String expectedAllotmentErrEn = param.get("errAllotmentExceedEn") + " (" + numberOfRooms + ").";
        String expectedAllotmentErrId = param.get("errAllotmentExceedId") + " (" + numberOfRooms + ").";
        String actualAllotmentUIError = propertyPage.getActualAllotmentExceedErrorText();
        Assertion.assertThat(String.format("Verify allotment boundary threshold validation message.\nExpected EN: [%s]\nOR ID: [%s]\nActual UI Error: [%s]\n", expectedAllotmentErrEn, expectedAllotmentErrId, actualAllotmentUIError), actualAllotmentUIError, anyOf(containsString(expectedAllotmentErrEn), containsString(expectedAllotmentErrId)));

        step("Step 16: Correct the Default Allotment value to a valid number (" + numberOfRooms + ")");
        propertyPage.setDefaultAllotmentValue(Integer.parseInt(numberOfRooms));
        int finalAllotmentValue = propertyPage.getDefaultAllotmentValue();
        Assertion.assertThat(String.format("Verify Default Allotment field input reflects the targeted valid count. Expected: [%s], Actual: [%d]", numberOfRooms, finalAllotmentValue), finalAllotmentValue, equalTo(Integer.parseInt(numberOfRooms)));

        step("Step 17: Fill Price Threshold / Minimum Price as " + minPrice + " and verify");
        propertyPage.enterMinimumPrice(minPrice);
        String actualPriceValue = propertyPage.getEnteredMinimumPrice();
        Assertion.assertThat(String.format("Verify the filled Minimum Price value matches our test data configuration exactly. Expected: [%s], Actual: [%s]", minPrice, actualPriceValue), actualPriceValue, equalTo(minPrice));

        step("Step 18: Select Any View inside the Room View dropdown list and verify selection");
        propertyPage.selectRoomViewByPosition(2);
        String actualSelectedViewText = propertyPage.getSelectedRoomView();
        Assertion.assertThat("Verify that a room view option was selected and the input field value is not empty. Selected View = " + actualSelectedViewText, actualSelectedViewText.isEmpty(), equalTo(false));

        step("Step 19: Select 'Smoking and Non-smoking' policy radio option and verify state");
        propertyPage.selectSmokingAndNonSmokingPolicy();
        boolean isRadioChecked = propertyPage.isSmokingAndNonSmokingSelected();
        Assertion.assertThat("Verify that the 'Smoking and Non-smoking' radio choice is actively checked and selected inside the DOM", isRadioChecked, equalTo(true));

        step("Step 20: Verify the field increment (+) functionality updates counter value");
        propertyPage.clickAverageRoomSizePlus();
        Assertion.assertThat("Verify clicking '+' increases the average room size value by 1", propertyPage.getAverageRoomSizeValue(), equalTo(1));

        step("Step 21: Verify the field decrement (-) functionality drops counter back");
        propertyPage.clickAverageRoomSizeMinus();
        Assertion.assertThat("Verify clicking '-' drops the average room size value back down by 0", propertyPage.getAverageRoomSizeValue(), equalTo(0));

        step("Step 22: Fill 3-digit Average Room Size value (" + baseRoomSizeStr + " m2) and verify");
        propertyPage.enterAverageRoomSize(baseRoomSizeStr);
        Assertion.assertThat("Verify base room size matches our inputted text dataset numerically. Expected = " + expectedBaseSize + " Actual = " + propertyPage.getAverageRoomSizeValue(), propertyPage.getAverageRoomSizeValue(), equalTo(expectedBaseSize));

        step("Step 23: Increment Number of Bathroom to 1 and verify structural update");
        propertyPage.clickNumberOfBathroomsPlus();
        Assertion.assertThat("Verify bathroom quantitative tracker changed to 1", propertyPage.getNumberOfBathroomsValue(), equalTo(1));

        step("Step 24: Select 'Private' from the newly revealed Bathroom Type section and verify choice");
        propertyPage.selectBathroomTypePrivate();
        boolean isPrivateChecked = propertyPage.isBathroomTypePrivateSelected();
        Assertion.assertThat("Verify that the 'Private' bathroom radio option is actively checked", isPrivateChecked, equalTo(true));

        step("Step 25: Set Room Occupancy and verify");
        propertyPage.setOccupancyDetails(maxOcc, maxAdult, maxChildren);
        String actualMaxOcc = propertyPage.getOccupancyValue();
        Assertion.assertThat("Verify Max Occupancy. Expected = " + maxOcc + ", Actual = " + actualMaxOcc, actualMaxOcc, equalTo(maxOcc));
        String actualMaxAdults = propertyPage.getMaxAdultsValue();
        Assertion.assertThat("Verify Max Adults. Expected = " + maxAdult + ", Actual = " + actualMaxAdults, actualMaxAdults, equalTo(maxAdult));
        String actualMaxChildren = propertyPage.getMaxChildrenValue();
        Assertion.assertThat("Verify Max Children. Expected = " + maxChildren + ", Actual = " + actualMaxChildren, actualMaxChildren, equalTo(maxChildren));

        propertyPage.setMinOccupancy(minOcc);
        String actualMinOcc = propertyPage.getMinOccupancyValue();
        Assertion.assertThat("Verify Min Occupancy. Expected = " + minOcc + ", Actual = " + actualMinOcc, actualMinOcc, equalTo(minOcc));

        propertyPage.verifyAndSetBedroomQuantity(Integer.parseInt(bedRoomCount));
        String finalBedroomCount = propertyPage.getBedroomQuantityValue();
        Assertion.assertThat("Verify final bedroom count is set. Expected = " + bedRoomCount + " Actual = " + finalBedroomCount, finalBedroomCount, equalTo(bedRoomCount));

        step("Step 26: Verify bedroom sections generated and set bed types");
        propertyPage.verifyRenderedBedroomSectionsCount(Integer.parseInt(bedRoomCount));
        propertyPage.selectBedTypeForAllBedrooms(Integer.parseInt(bedRoomCount), desiredBedType, bedCount);

        step("Step 27: Select the Extra Bed Available checkbox and verify it with number of extra beds filled is " + extraBedQty);
        propertyPage.setExtraBedQuantity(extraBedQty);
        propertyPage.verifyExtraBedQuantity(extraBedQty);

        step("Step 28: Upload " + numOfPhotos + " Photo and select one of them as Main and Verify it");
        propertyPage.uploadPhotosAndSetMain(numOfPhotos);

        step("Step 29: Select Facilities for the room");
        propertyPage.clickAllFacilitiesWithLimit(5);

        step("Step 30: Click on the Create Room button and Verify that we are navigated to Room Data Page");
        propertyPage.clickCreateRoomButton();
        propertyPage.verifySnackbarMessage(roomCreateEn, roomCreateId);
        Assertion.assertThat("Verify Add New Room Page is Loaded", propertyPage.isRoomDataPageLoaded(), is(true));

        step("Step 31: Search the Room with the Room Name as " + roomName);
        propertyPage.searchRoomName(roomName);
        propertyPage.clickSearch();
        propertyPage.verifySearchResults(roomName);

        step("Step 32: Verify the default status toggle is off");
        Assertion.assertThat("Verify that the room status toggle is OFF by default for room: '" + roomName + "'", propertyPage.isStatusToggleOffByDefault(roomName), is(true));

        step("Step 33: Turn on the status toggle and Verify the modal appears");
        propertyPage.turnOnStatusToggle(roomName);
        propertyPage.verifyStatusModalAppear();

        step("Step 34: click on complete button and verify the we have Navigated to Room Data filling page");
        propertyPage.clickCompleteDataButtonInModal();
        Assertion.assertThat("Verify Room Data Filling Page is Loaded", propertyPage.isEditRoomPageLoaded(), is(true));

        step("step 35: Return back to the Room Data page and verify Room Data Page is loaded");
        propertyPage.navigateToPreviousPage();
        Assertion.assertThat("Room Data page is Loaded", propertyPage.isRoomDataPageLoaded(), is(true));

        step("Step 36: Again turn on the status toggle and click on Activate button and Verify the Status is Active");
        propertyPage.turnOnStatusToggle(roomName);
        propertyPage.verifyStatusModalAppear();
        propertyPage.clickActivateRoomButtonInModal();
        Assertion.assertThat("Verify that the room status toggle is On for room: '" + roomName + "'", !propertyPage.isStatusToggleOffByDefault(roomName), is(true));

        step("Step 37: Click on the edit button of Room " + roomName + " and verify we are navigated to Edit Room Data Page");
        propertyPage.clickEditButton(roomName);
        Assertion.assertThat("Verify Room Data Filling Page is Loaded", propertyPage.isEditRoomPageLoaded(), is(true));

        step("Step 38: Change the room name from " + roomName + " to " + editedRoomName + " And verify the room is name is changed");
        propertyPage.enterRoomName(editedRoomName);
        actualFilledRoomName = propertyPage.getEnteredRoomName();
        Assertion.assertThat("Verify the filled Room Name is changed Old Name = " + roomName + " new Room name = " + actualFilledRoomName, actualFilledRoomName, is(not(equalTo(roomName))));
        Assertion.assertThat("Verify the filled New Room Name matches with the Expected = " + editedRoomName + " Actual = " + actualFilledRoomName, actualFilledRoomName, equalTo(editedRoomName));

        step("Step 39: Click on the Edit button and verify the snackbar");
        propertyPage.clickEditRoomSubmitButton();
        propertyPage.verifySnackbarMessage(editedRoomEn, editedRoomId);

        step("Step 40: Verify the Room Data page is Loaded");
        Assertion.assertThat("Room Data page is Loaded", propertyPage.isRoomDataPageLoaded(), is(true));

        step("Step 41: Search the Room with edited Room Name = " + editedRoomName);
        propertyPage.searchRoomName(editedRoomName);
        propertyPage.clickSearch();
        propertyPage.verifySearchResults(editedRoomName);

        step("Step 42: Click on Delete Room Button");
        propertyPage.clickDeleteButton(editedRoomName);
        propertyPage.verifyDeleteModalAppears();

        step("Step 43: Click on No button and verify that Room = " + editedRoomName + " Still exist");
        propertyPage.clickDeleteModalNoButton();
        propertyPage.verifySearchResults(editedRoomName);

        step("Step 44: Again click on Delete Room Button");
        propertyPage.clickDeleteButton(editedRoomName);
        propertyPage.verifyDeleteModalAppears();

        step("Step 45: Click on Yes button and verify the snackbar appears");
        propertyPage.clickDeleteModalYesButton();
        propertyPage.verifySnackbarMessage(deleteEn, deleteId);

        step("Step 46: Verify the room successfully deleted");
        propertyPage.searchRoomName(editedRoomName);
        propertyPage.clickSearch();
        propertyPage.verifyRoomIsDeletedAndNoDataFound();
    }

    @Author(names = {"Pankaj Singh Kushwaha", "Vipul Kumar"})
    @Module(name = "ManageRatesAndAvailability")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserCanCreateCustomRatePlan() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        ManageRatesAndAvailabilityPage<?> manageRatesAndAvailabilityPage = new ManageRatesAndAvailabilityPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserCanCreateCustomRatePlan");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");
        String ratePlanErrEn = param.get("ratePlanErrEn");
        String ratePlanErrId = param.get("ratePlanErrId");
        String mealErrEn = param.get("mealErrEn");
        String mealErrId = param.get("mealErrId");
        String roomIdErrEn = param.get("roomIdErrEn");
        String roomIdErrId = param.get("roomIdErrId");
        String cancelErrEn = param.get("cancelErrEn");
        String cancelErrId = param.get("cancelErrId");
        String noShowErrEn = param.get("noShowErrEn");
        String noShowErrId = param.get("noShowErrId");
        String ratePlanName = param.get("ratePlanName");
        String editedRatePlanName = param.get("editRatePlan");
        String minNight = param.get("minNights");
        String maxNight = param.get("maxNights");
        String linkedRoomName = param.get("linkedRoom");
        String defaultRatePlan = param.get("defaultRatePlan");
        String statusEn = param.get("statusEn");
        String statusId = param.get("statusId");
        String emptyDataEn = param.get("emptyDataEn");
        String emptyDataId = param.get("emptyDataId");
        String validCancelDays = param.get("validCancelDays");
        String validCancelPct = param.get("validCancelPct");
        String validNoShowPct = param.get("validNoShowPct");
        String invalidAmount = param.get("invalidAmount");
        String validCancelAmount = param.get("validCancelAmount");
        String validNoShowAmount = param.get("validNoShowAmount");
        String amountErrorEn = param.get("amountErrorEn");
        String amountErrorId = param.get("amountErrorId");
        String opt1En = param.get("opt1En");
        String opt1Id = param.get("opt1Id");
        String opt2En = param.get("opt2En");
        String opt2Id = param.get("opt2Id");
        String summaryOpt1En = param.get("summaryOpt1En");
        String summaryOpt1Id = param.get("summaryOpt1Id");
        String summaryOpt2En = param.get("summaryOpt2En");
        String summaryOpt2Id = param.get("summaryOpt2Id");

        step("Step 1: Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Step 2: Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Step 3: Go to Rate Plan page and Verify the Rate Plan page is Loaded");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickRatePlanMenu();
        Assertion.assertThat("Verify Rate Plan page is loaded", manageRatesAndAvailabilityPage.isRatePlanPageLoaded(), is(true));

        step("Step 4: Search the Rate Plan Name =" + editedRatePlanName + " If it exist delete it");
        manageRatesAndAvailabilityPage.searchRatePlan(editedRatePlanName);
        manageRatesAndAvailabilityPage.deleteRatePlanIfExists(emptyDataEn, emptyDataId, editedRatePlanName);

        step("Step 5: Search the Rate Plan Name =" + ratePlanName + " If it exist delete it");
        manageRatesAndAvailabilityPage.searchRatePlan(ratePlanName);
        manageRatesAndAvailabilityPage.deleteRatePlanIfExists(emptyDataEn, emptyDataId, ratePlanName);

        step("Step 6: Click on Add New Rate Plan Button and Verify the Select Rate Plan Modal Appear");
        manageRatesAndAvailabilityPage.clickAddNewRatePlanButton();
        manageRatesAndAvailabilityPage.verifyRatePlanTypeModalAppears();

        step("Step 7: Choose the Custom Rate Plan option from the Rate Plan Modal and Verify Add New Rate Plan Page Loaded");
        manageRatesAndAvailabilityPage.clickCustomRatePlanOption();
        manageRatesAndAvailabilityPage.verifyAddNewRatePlanPageLoaded();

        step("Step 8: Without filling any Data field click on the Submit Add New Rate Plan Button");
        manageRatesAndAvailabilityPage.clickSubmitNewRatePlanButton();

        step("Step 9: Verify that all required field validation errors are displayed on the UI");
        manageRatesAndAvailabilityPage.verifyErrorMessage(ratePlanErrEn, ratePlanErrId);
        manageRatesAndAvailabilityPage.verifyErrorMessage(mealErrEn, mealErrId);
        manageRatesAndAvailabilityPage.verifyErrorMessage(roomIdErrEn, roomIdErrId);
        manageRatesAndAvailabilityPage.verifyErrorMessage(cancelErrEn, cancelErrId);
        manageRatesAndAvailabilityPage.verifyErrorMessage(noShowErrEn, noShowErrId);

        step("Step 10: Fill the Rate Plan Name as " + ratePlanName + " And Verify it");
        manageRatesAndAvailabilityPage.fillRatePlanName(ratePlanName);
        manageRatesAndAvailabilityPage.verifyRatePlanNameFilled(ratePlanName);

        step("Step 11: Fill Minimum Night = " + minNight + " And Maximum Night = " + maxNight);
        manageRatesAndAvailabilityPage.fillMinimumNights(minNight);
        manageRatesAndAvailabilityPage.fillMaximumNights(maxNight);
        manageRatesAndAvailabilityPage.verifyMinimumNightsFilled(minNight);
        manageRatesAndAvailabilityPage.verifyMaximumNightsFilled(maxNight);

        step("Step 12: Select no meal radio button and verify it is selected");
        manageRatesAndAvailabilityPage.selectNoMealOption();
        manageRatesAndAvailabilityPage.verifyNoMealOptionSelected();

        step("Step 13: Select the room checkbox");
        manageRatesAndAvailabilityPage.selectLinkedRoom(linkedRoomName);

        step("Step 14: Fill the Default Rate and verify it");
        manageRatesAndAvailabilityPage.fillLinkedDefaultRate(defaultRatePlan);
        manageRatesAndAvailabilityPage.verifyLinkedDefaultRateFilled(defaultRatePlan);

        step("Step 15: Click 'Remove' (Hapus) and verify 'Set default' Hyperlink appears");
        manageRatesAndAvailabilityPage.clickLinkedRemoveDefaultRate();
        manageRatesAndAvailabilityPage.verifySetDefaultAppearsForRoom(linkedRoomName);

        step("Step 16: Click on the Set Default Hyperlink and fill Default Rate = " + defaultRatePlan + " And verify it");
        manageRatesAndAvailabilityPage.clickSetDefaultHyperlink(linkedRoomName);
        manageRatesAndAvailabilityPage.fillLinkedDefaultRate(defaultRatePlan);
        manageRatesAndAvailabilityPage.verifyLinkedDefaultRateFilled(defaultRatePlan);

        step("Step 17: Select the Custom policy option");
        manageRatesAndAvailabilityPage.selectCustomPolicy();

        step("Step 18: Select 'Night' for both Cancellation Fee and No-show, and verify the dropdown options");
        manageRatesAndAvailabilityPage.selectCancelNightAndVerifyOptions(opt1En, opt1Id, opt2En, opt2Id);
        manageRatesAndAvailabilityPage.selectNoShowNightAndVerifyOptions(opt1En, opt1Id, opt2En, opt2Id);

        step("Step 19: Verify the summary policy text dynamically reflects the Night selection for Cancellation");
        manageRatesAndAvailabilityPage.verifySummaryPolicyDescriptionForNight(summaryOpt1En, summaryOpt1Id);

        step("Step 20: Verify the summary policy text dynamically reflects the Night selection for NoShow");
        manageRatesAndAvailabilityPage.verifySummaryPolicyDescriptionForNight(summaryOpt2En, summaryOpt2Id);

        step("Step 21: Select 'Percentage' and enter " + validCancelPct + " for Cancellation Fee and " + validNoShowPct + " for No-show");
        manageRatesAndAvailabilityPage.fillCustomPolicyFields(validCancelDays, validCancelPct, validNoShowPct);

        step("Step 22: Verify the summary policy text dynamically reflects the entered Percentage for Cancellation as "+validCancelPct);
        manageRatesAndAvailabilityPage.verifySummaryPolicyDescription(validCancelPct);

        step("Step 23: Verify the summary policy text dynamically reflects the entered Percentage for NoShow as "+validNoShowPct);
        manageRatesAndAvailabilityPage.verifySummaryPolicyDescription(validNoShowPct);

        step("Step 24: Select 'Amount/Nominal' radio buttons for both Cancellation Fee and No-show sections");
        manageRatesAndAvailabilityPage.clickCancellationFeeAmountRadio();
        manageRatesAndAvailabilityPage.clickNoShowAmountRadio();

        step("Step 25: Enter " + invalidAmount + " in both Amount fields to trigger validation errors");
        manageRatesAndAvailabilityPage.fillAmountFields(invalidAmount, invalidAmount);

        step("Step 26: Verify the error messages for invalid amount (" + invalidAmount + ") in English and Indonesian");
        manageRatesAndAvailabilityPage.verifyErrorMessage(amountErrorEn, amountErrorId);

        step("Step 27: Enter valid amounts: " + validCancelAmount + " for Cancellation Fee and " + validNoShowAmount + " for No-show");
        manageRatesAndAvailabilityPage.fillAmountFields(validCancelAmount, validNoShowAmount);

        step("Step 28: Verify the summary policy text dynamically reflects the entered Amounts for Cancellation Fee "+validCancelAmount);
        manageRatesAndAvailabilityPage.verifySummaryPolicyDescription(validCancelAmount);

        step("Step 29: Verify the summary policy text dynamically reflects the entered Amounts for NoShow "+validNoShowAmount);
        manageRatesAndAvailabilityPage.verifySummaryPolicyDescription(validNoShowAmount);

        step("Step 30: Select the Non-refundable Cancellation Policy");
        manageRatesAndAvailabilityPage.selectNonRefundableOption();
        manageRatesAndAvailabilityPage.verifyNonRefundableOptionSelected();

        step("Step 31: Click on the Add New Rate Plan Button");
        manageRatesAndAvailabilityPage.clickSubmitNewRatePlanButton();

        step("Step 32: Verify Rate Plan page is loaded.");
        Assertion.assertThat("Verify Rate Plan page is loaded", manageRatesAndAvailabilityPage.isRatePlanPageLoaded(), is(true));

        step("Step 33: Search for the Rate Plan using rate plan name = " + ratePlanName);
        manageRatesAndAvailabilityPage.searchRatePlan(ratePlanName);

        step("Step 34: Verify only single entry is present and name is matching with the search name");
        manageRatesAndAvailabilityPage.verifySingleEntryAndNameMatch(ratePlanName);

        step("Step 35: Verify the status toggle is off");
        manageRatesAndAvailabilityPage.verifyStatusToggleIsOff();

        step("Step 36: Turn on the status toggle and Verify the Toast message");
        manageRatesAndAvailabilityPage.turnOnOffStatusToggle();
        manageRatesAndAvailabilityPage.verifyToastMessage(statusEn, statusId);

        step("Step 37: Turn off the status toggle and Verify the Toast message");
        manageRatesAndAvailabilityPage.turnOnOffStatusToggle();
        manageRatesAndAvailabilityPage.verifyToastMessage(statusEn, statusId);

        step("Step 38: Click on the Edit button and Verify the Edit Rate Plan Page is Loaded.");
        manageRatesAndAvailabilityPage.clickRatePlanEditButton();
        manageRatesAndAvailabilityPage.verifyEditRatePlanPageLoaded();

        step("Step 39: Change the Rate Plan name from " + ratePlanName + " to " + editedRatePlanName + " And verify the rate plan name is changed");
        manageRatesAndAvailabilityPage.fillRatePlanName(editedRatePlanName);
        manageRatesAndAvailabilityPage.verifyRatePlanNameFilled(editedRatePlanName);

        step("Step 40: Click on the Edit Rate Plan Submit button");
        manageRatesAndAvailabilityPage.clickSubmitEditRatePlanButton();

        step("Step 41: Verify Rate Plan page is loaded.");
        Assertion.assertThat("Verify Rate Plan page is loaded", manageRatesAndAvailabilityPage.isRatePlanPageLoaded(), is(true));

        step("Step 42: Search for the Rate Plan using Edited Rate plan name = " + editedRatePlanName);
        manageRatesAndAvailabilityPage.searchRatePlan(editedRatePlanName);

        step("Step 43: Verify only single entry is present and name is matching with the search name");
        manageRatesAndAvailabilityPage.verifySingleEntryAndNameMatch(editedRatePlanName);

        step("Step 44: Click on the delete button and verify the Modal appears");
        manageRatesAndAvailabilityPage.clickRatePlanDeleteButton();
        manageRatesAndAvailabilityPage.verifyDeleteConfirmationModalAppears();

        step("Step 45: Click on the Cancel Button and Verify the rate plan still exist");
        manageRatesAndAvailabilityPage.clickCancelDelete();
        manageRatesAndAvailabilityPage.searchRatePlan(editedRatePlanName);
        manageRatesAndAvailabilityPage.verifySingleEntryAndNameMatch(editedRatePlanName);

        step("Step 46: Again click on the delete button and Verify the modal appears");
        manageRatesAndAvailabilityPage.clickRatePlanDeleteButton();
        manageRatesAndAvailabilityPage.verifyDeleteConfirmationModalAppears();

        step("Step 47: Click on the confirm delete button and verify Delete Success modal appears");
        manageRatesAndAvailabilityPage.clickConfirmDelete();
        manageRatesAndAvailabilityPage.verifyDeleteSuccessModalAppears();

        step("Step 48: Click on the Back to List Button");
        manageRatesAndAvailabilityPage.clickBackToList();

        step("Step 49: Search the Rate Plan using name = " + editedRatePlanName + " And verify No Data Found");
        manageRatesAndAvailabilityPage.searchRatePlan(editedRatePlanName);
        manageRatesAndAvailabilityPage.verifyNoDataFound(emptyDataEn, emptyDataId);

    }

    @Author(names = {"Abhishek Sharma", "Vipul Kumar"})
    @Module(name = "ManageRatesAndAvailability")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserCanBulkUpdateRoomRates() {
        Map<String, String> param = getParam("testVerifyUserCanBulkUpdateRoomRates");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        BulkUpdatePage<?> bulkUpdatePage = new BulkUpdatePage<>(session);
        AuthTest authTest = new AuthTest();

        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");
        String[] dateIndexes = {param.get("singleDateRange"), param.get("multiDateRange")};
        String[][] values = {{param.get("negativeValue"), param.get("zeroValue")}, {param.get("positiveValue")}};
        String expectedModalDescDelimiter = param.get("expectedModalDescDelimiter");
        String[] expectedModalTitle = param.get("expectedModalTitle").split(Pattern.quote(param.get("separator")));
        String[] negativeValueErrorMessage = param.get("negativeValueErrorMessage").split(Pattern.quote(param.get("separator")));

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotelId from testData. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelIdMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel Id on navbar. Expected: " + hotelId + " Actual: " + (hotelIdMatch ? hotelId : "no match"), hotelIdMatch, is(true));

        step("Open Rates & Availability Menu");
        sideMenu.clickRatesAndAvailabilityMenu();

        step("Click Bulk Update Menu");
        sideMenu.clickBulkUpdateMenu();

        boolean isBulkUpdatePageLoaded = bulkUpdatePage.isBulkUpdatePageLoaded();
        Assertion.assertThat("Verify bulk update page loaded", isBulkUpdatePageLoaded, is(true));

        for (String dateIndex : dateIndexes) {
            String[] indexes = dateIndex.split("-");
            int startIndex = Integer.parseInt(indexes[0]);
            int endIndex = Integer.parseInt(indexes[1]);

            step("Selecting " + (startIndex == endIndex ? "Single" : "Multi") + " Date Range");
            String dateRange = bulkUpdatePage.selectDateRangeByIndex(startIndex, endIndex);
            String enDateRange = bulkUpdatePage.formatCalendarDateWithLang(dateRange);
            String idDateRange = bulkUpdatePage.toBahasaDateRange(enDateRange);

            // Modal renders in the UI language, so substitute the English date into the English text and the Bahasa date into the Bahasa text.
            String[] expectedModalDesc = param.get("expectedModalDesc").split(Pattern.quote(param.get("separator")));
            expectedModalDesc[0] = expectedModalDesc[0].replace(expectedModalDescDelimiter, enDateRange);
            expectedModalDesc[1] = expectedModalDesc[1].replace(expectedModalDescDelimiter, idDateRange);
            int alternateBit = 0;
            List<String> allRoomTypes = new ArrayList<>();

            for (String value : values[startIndex == endIndex ? 0 : 1]) {
                if (alternateBit == 0) {
                    step("Click on select room type input field");
                    bulkUpdatePage.clickRoomTypeInput();

                    step("Select All room types");
                    allRoomTypes = bulkUpdatePage.getAllRoomTypesFromDropdown();
                    bulkUpdatePage.clickSelectAllRoomTypeCheckbox();

                    step("Click Apply button");
                    bulkUpdatePage.clickApplyRoomTypeButton();

                    step("Verify all room tags appears");
                    for (String roomType : allRoomTypes) {
                        Assertion.assertThat("Verify Tag name for " + roomType, bulkUpdatePage.getRoomTagName(roomType), containsString(roomType));
                    }
                }

                alternateBit = 1 - alternateBit;
                List<List<String>> roomNameRatePlanValue = new ArrayList<>();

                for (String roomType : allRoomTypes) {
                    step("Select First Rate plan for Room Name: %s (If present) with value: %s".formatted(roomType, value));
                    String ratePlan = bulkUpdatePage.selectFirstRatePlanWithValue(roomType, value);

                    if (ratePlan == null) {
                        step("No Rate plan selected");
                        Assertion.assertThat("Verify Rate value input field is disabled", bulkUpdatePage.isRateInputFieldDisabled(roomType), is(true));
                    } else {
                        step("Rate plan selected: " + ratePlan);
                        Assertion.assertThat("Verify Rate value input field is enabled", bulkUpdatePage.isRateInputFieldEnabled(roomType), is(true));

                        if (value != null && value.matches("\\d+")) {
                            roomNameRatePlanValue.add(List.of(roomType, ratePlan, value));
                            Assertion.assertThat("Verify No Error Message", bulkUpdatePage.getRatePlanErrorMessage(roomType), nullValue());
                        } else {
                            Assertion.assertThat("Verify Error Message", bulkUpdatePage.getRatePlanErrorMessage(roomType), anyOf(containsString(negativeValueErrorMessage[0]), containsString(negativeValueErrorMessage[1])));
                        }
                    }
                }

                if (value != null && value.matches("\\d+")) {
                    step("Click Save Changes button");
                    Assertion.assertThat("Save changes button is clickable", bulkUpdatePage.isSaveChangesButtonClickable(), is(true));
                    bulkUpdatePage.clickSaveChangesButton();

                    step("Verify modal details");
                    Assertion.assertThat("Verify Modal appears", bulkUpdatePage.isAllotmentChangesSaveChangesModalAppears(), is(true));
                    String modalTitle = bulkUpdatePage.allotmentChangesSaveChangesModalTitle();
                    String modalDesc = bulkUpdatePage.allotmentChangesSaveChangesModalDesc();

                    Assertion.assertThat("Verify modal title matches", modalTitle,
                            anyOf(containsString(expectedModalTitle[0]), containsString(expectedModalTitle[1])));
                    Assertion.assertThat("Verify modal description matches", modalDesc,
                            anyOf(containsString(expectedModalDesc[0]), containsString(expectedModalDesc[1])));

                    step("Click save changes confirmation modal button");
                    bulkUpdatePage.clickSaveChangesConfirmationButton();

                    step("Verify update preview modal and its content appears");
                    Assertion.assertThat("Verify update preview modal appears", bulkUpdatePage.isUpdatePreviewModalAppeared(), is(true));
                    Assertion.assertThat("Verify date range", bulkUpdatePage.getUpdatePreviewModalDateRange(), anyOf(containsString(enDateRange), containsString(idDateRange)));

                    step("Verify all room names and values");
                    List<List<String>> extractedRoomNameRatePlanValue = bulkUpdatePage.getModalAllRoomNameRatePlanValue(roomNameRatePlanValue);
                    Assertion.assertThat("Verify Room names size matches", extractedRoomNameRatePlanValue.size(), is(roomNameRatePlanValue.size()));

                    for (int i = 0; i < extractedRoomNameRatePlanValue.size(); i++) {
                        List<String> expected = roomNameRatePlanValue.get(i);
                        List<String> extracted = extractedRoomNameRatePlanValue.get(i);
                        Assertion.assertThat("Verify room name: " + expected.get(0), extracted.get(0), containsString(expected.get(0)));
                        Assertion.assertThat("Verify rate plan: " + expected.get(1), extracted.get(1), containsString(expected.get(1)));
                        Assertion.assertThat("Verify rate value: " + expected.get(2), extracted.get(2), containsString(expected.get(2)));
                    }

                    step("Click ok on update preview modal");
                    bulkUpdatePage.clickUpdatePreviewModalOKButton();
                    Assertion.assertThat("Verify modal disappears", bulkUpdatePage.isUpdatePreviewModalDisappeared(), is(true));
                }
            }
        }
    }

    @Author(names = {"Pankaj Singh Kushwaha", "Vipul Kumar"})
    @Module(name = "ManageRatesAndAvailability")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserCanBulkUpdateNonRefundablePolicy() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        AuthTest authTest = new AuthTest();
        BulkUpdatePage<?> bulkUpdatePage = new BulkUpdatePage<>(session);

        Map<String, String> param = getParam("verifyUserCanBulkUpdateNonRefundablePolicy");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");
        String[] dateIndexes = {param.get("singleDateRange"), param.get("multiDateRange")};
        String singleDate = dateIndexes[0];
        String multiDate = dateIndexes[1];
        String[] singleIndexes = singleDate.split("-");
        int singleStartIndex = Integer.parseInt(singleIndexes[0]);
        int singleEndIndex = Integer.parseInt(singleIndexes[1]);
        String[] multiIndexes = multiDate.split("-");
        int multiStartDay = Integer.parseInt(multiIndexes[0]);
        int multiEndDay = Integer.parseInt(multiIndexes[1]);
        String roomCount = param.get("roomCount");
        String expectedNonRefundPolicyEn = param.get("expectedNonRefundPolicyEn");
        String expectedNonRefundPolicyId = param.get("expectedNonRefundPolicyId");
        String expectedCustomPolicyEn = param.get("expectedCustomPolicyEn");
        String expectedCustomPolicyId = param.get("expectedCustomPolicyId");
        String daysErrorEn = param.get("daysErrorEn");
        String pctErrorEn = param.get("pctErrorEn");
        String daysErrorId = param.get("daysErrorId");
        String pctErrorId = param.get("pctErrorId");
        String noShowErrorEn = param.get("noShowErrorEn");
        String noShowErrorId = param.get("noShowErrorId");
        String invalidDaysErrorEn = param.get("invalidDaysErrorEn");
        String invalidDaysErrorId = param.get("invalidDaysErrorId");
        String invalidNoShowErrorEn = param.get("invalidNoShowErrorEn");
        String invalidNoShowErrorId = param.get("invalidNoShowErrorId");
        String invalidPctErrorEn = param.get("invalidPctErrorEn");
        String invalidPctErrorId = param.get("invalidPctErrorId");
        String invalidCancelDays = param.get("invalidCancelDays");
        String invalidPct = param.get("invalidPct");
        String invalidNoShowPct = param.get("invalidNoShowPct");
        String opt1En = param.get("opt1En");
        String opt1Id = param.get("opt1Id");
        String opt2En = param.get("opt2En");
        String opt2Id = param.get("opt2Id");
        String invalidSecCancelDays = param.get("invalidSecCancelDays");
        String validCancelDays = param.get("validCancelDays");
        String validPct = param.get("validPct");
        String validNoShowPct = param.get("validNoShowPct");
        String restrictionErrorEn = param.get("restrictionErrorEn");
        String restrictionErrorId = param.get("restrictionErrorId");
        String invalidMaxLosEn = param.get("invalidMaxLosEn");
        String invalidMaxLosId = param.get("invalidMaxLosId");
        String invalidMaxAdvEn = param.get("invalidMaxAdvEn");
        String invalidMaxAdvId = param.get("invalidMaxAdvId");
        String invalidMinLos = param.get("invalidMinLos");
        String invalidMaxLos = param.get("invalidMaxLos");
        String invalidMinAdv = param.get("invalidMinAdv");
        String invalidMaxAdv = param.get("invalidMaxAdv");
        String validMinLos = param.get("validMinLos");
        String validMaxLos = param.get("validMaxLos");
        String validMinAdv = param.get("validMinAdv");
        String validMaxAdv = param.get("validMaxAdv");
        String expectedLosLimits = param.get("expectedLosLimits");
        String expectedAdvLimits = param.get("expectedAdvLimits");
        String invalidCancellationAmt = param.get("invalidCancellationAmt");
        String invalidNoShowAmt = param.get("invalidNoShowAmt");
        String validCancellationAmt = param.get("validCancellationAmt");
        String validNoShowAmt = param.get("validNoShowAmt");
        String amtValidationEn = param.get("amtValidationEn");
        String amtValidationId = param.get("amtValidationId");

        step("Step 1: Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Step 2: Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Step 3: Go to Bulk Update Page and Verify the Bulk Update Page is Loaded");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickBulkUpdateMenu();
        Assertion.assertThat("Verify Rate Plan page is loaded", bulkUpdatePage.isBulkUpdatePageLoaded(), is(true));

        step("Step 4: click on the Cancellation Policy Tab");
        bulkUpdatePage.clickCancellationPolicyTab();

        step("Step 5: Select Single Date only");
        String dateRange = bulkUpdatePage.selectDateRangeByIndex(singleStartIndex, singleEndIndex);
        String currentDateRange = bulkUpdatePage.formatCalendarDateWithLang(dateRange);

        step("Step 6: Select room type and rate type, verifying at least one of each is displayed");
        Map<String, String> dynamicSelections = bulkUpdatePage.selectAndVerifyRoomAndRateType();
        String expectedRoom = dynamicSelections.get("roomName");
        String expectedRatePlan = dynamicSelections.get("ratePlanName");

        step("Step 7: Select the Non-Refundable cancellation policy");
        bulkUpdatePage.selectNonRefundablePolicy();

        step("Step 8: Click Save to confirm the rate plan and room selections");
        bulkUpdatePage.clickSavePolicyButton();

        step("Step 9: Verify the cancellation policy confirmation modal displays the correct data");
        bulkUpdatePage.verifyCancellationPolicyModal(roomCount + " room types", roomCount + " tipe kamar", currentDateRange);

        step("Step 10: Click 'Yes, save' to confirm the bulk update");
        bulkUpdatePage.clickYesSavePolicy();

        step("Step 11: Verify that the success confirmation pop-up appears with correct details");
        bulkUpdatePage.verifySuccessSummaryModal(currentDateRange, expectedNonRefundPolicyEn, expectedNonRefundPolicyId, expectedRoom, expectedRatePlan);

        step("Step 12: Click on Okay and verify that the pop-up disappears");
        bulkUpdatePage.clickSummaryOkAndVerifyDisappears();

        step("Step 13: Open the date picker and select a one-week range in the next month");
        dateRange = bulkUpdatePage.selectDateRangeInNextMonth(multiStartDay, multiEndDay);
        currentDateRange = bulkUpdatePage.formatCalendarDateWithLang(dateRange);

        step("Step 14: Select the Custom cancellation policy");
        bulkUpdatePage.selectCustomPolicy();

        step("Step 15: Click Save to trigger form validation on empty fields");
        bulkUpdatePage.clickSavePolicyButton();

        step("Step 16: Verify validation errors for empty Custom Policy fields");
        bulkUpdatePage.verifyCustomPolicyValidationErrors(daysErrorEn, daysErrorId, pctErrorEn, pctErrorId, noShowErrorEn, noShowErrorId);

        step("Step 17: Enter invalid data " + invalidCancelDays + " as cancel Days, " + invalidPct + " as percentage, " + invalidNoShowPct + " as No Show into the Custom Policy fields");
        bulkUpdatePage.fillCustomPolicyFields(invalidCancelDays, invalidPct, invalidNoShowPct);

        step("Step 18: Click Save to trigger form validation on invalid fields");
        bulkUpdatePage.clickSavePolicyButton();

        step("Step 19: Verify validation errors for Invalid Custom Policy fields");
        bulkUpdatePage.verifyCustomPolicyInvalidFieldValidationErrors(invalidPctErrorEn, invalidPctErrorId, invalidNoShowErrorEn, invalidNoShowErrorId);

        step("Step 20: Enter cancellation fee days as " + invalidSecCancelDays + " and verify the UI Error");
        bulkUpdatePage.enterRefundableDays(invalidSecCancelDays);
        bulkUpdatePage.verifyCancellationFeeUIError(invalidDaysErrorEn, invalidDaysErrorId);

        step("Step 21: Select Cancellation Fee Night option and verify dropdown options");
        bulkUpdatePage.selectCancelNightAndVerifyOptions(opt1En, opt1Id, opt2En, opt2Id);

        step("Step 22: Select No-show Night option and verify dropdown options");
        bulkUpdatePage.selectNoShowNightAndVerifyOptions(opt1En, opt1Id, opt2En, opt2Id);

        step("Step 23: Verify the summary policy text dynamically reflects the entered Cancellation Night (First night)");
        bulkUpdatePage.verifySummaryPolicyDescriptionForNight("First night", "Malam pertama");

        step("Step 24: Verify the summary policy text dynamically reflects the entered No-show Night (All nights)");
        bulkUpdatePage.verifySummaryPolicyDescriptionForNight("all nights", "semua malam");
        
        step("Step 25: Enter Valid data " + validCancelDays + " as cancel Days, " + validPct + " as percentage, " + validNoShowPct + " as No Show into the Custom Policy fields");
        bulkUpdatePage.fillCustomPolicyFields(validCancelDays, validPct, validNoShowPct);

        step("Step 26: Verify the summary policy text dynamically reflects the entered Cancellation Percentage = "+validPct);
        bulkUpdatePage.verifySummaryPolicyDescription(validPct);

        step("Step 27: Verify the summary policy text dynamically reflects the entered NoShow Percentage = "+validNoShowPct);
        bulkUpdatePage.verifySummaryPolicyDescription(validNoShowPct);

        step("Step 28: Click on Save Button");
        bulkUpdatePage.clickSavePolicyButton();

        step("Step 29: Verify the cancellation policy confirmation modal displays the correct data");
        bulkUpdatePage.verifyCancellationPolicyModal(roomCount + " room types", roomCount + " tipe kamar", currentDateRange);

        step("Step 30: Click 'Yes, save' to confirm the bulk update");
        bulkUpdatePage.clickYesSavePolicy();

        step("Step 31: Verify that the success confirmation pop-up appears with correct details");
        bulkUpdatePage.verifySuccessSummaryModal(currentDateRange, expectedCustomPolicyEn, expectedCustomPolicyId, expectedRoom, expectedRatePlan);

        step("Step 32: Click on Okay and verify that the pop-up disappears");
        bulkUpdatePage.clickSummaryOkAndVerifyDisappears();

        step("Step 33: Verify the info SVG icon is present next to the Amount/Nominal option");
        bulkUpdatePage.verifyAmountInfoIconPresent();

        step("Step 34: Select the Amount/Nominal radio button for both Cancellation Fee and No-show");
        bulkUpdatePage.clickCancellationFeeAmountRadio();
        bulkUpdatePage.clickNoShowAmountRadio();

        step("Step 35: Enter "+ invalidCancellationAmt+ " in both amount fields to trigger validation");
        bulkUpdatePage.fillAmountFields(invalidCancellationAmt, invalidNoShowAmt);

        step("Step 36: Verify the error messages for invalid amount (0) in English and Indonesian");
        bulkUpdatePage.verifyAmountValidationErrors(amtValidationEn, amtValidationId);

        step("Step 37: Enter valid amount for Cancellation = "+validCancellationAmt+" and No Show amount = "+validNoShowAmt+" in amount fields");
        bulkUpdatePage.fillAmountFields(validCancellationAmt, validNoShowAmt);

        step("Step 38: Verify the summary policy text dynamically reflects the entered Cancellation amount = "+validCancellationAmt);
        bulkUpdatePage.verifySummaryPolicyDescription(validCancellationAmt);

        step("Step 39: Verify the summary policy text dynamically reflects the entered NoShow amount = "+validNoShowAmt);
        bulkUpdatePage.verifySummaryPolicyDescription(validNoShowAmt);

        step("Step 40: Click on Save Button");
        bulkUpdatePage.clickSavePolicyButton();

        step("Step 41: Verify the cancellation policy confirmation modal displays the correct data");
        bulkUpdatePage.verifyCancellationPolicyModal(roomCount + " room types", roomCount + " tipe kamar", currentDateRange);

        step("Step 42: Click 'Yes, save' to confirm the bulk update");
        bulkUpdatePage.clickYesSavePolicy();

        step("Step 43: Verify that the success confirmation pop-up appears with correct details");
        bulkUpdatePage.verifySuccessSummaryModal(currentDateRange, expectedCustomPolicyEn, expectedCustomPolicyId, expectedRoom, expectedRatePlan);

        step("Step 44: Click on Okay and verify that the pop-up disappears");
        bulkUpdatePage.clickSummaryOkAndVerifyDisappears();

        step("Step 45: click on the Restrictions Tab");
        bulkUpdatePage.clickRestrictionsTab();

        step("Step 46: Open the date picker and select a one-week range in the next month");
        dateRange = bulkUpdatePage.selectDateRangeInNextMonth(multiStartDay, multiEndDay);
        currentDateRange = bulkUpdatePage.formatCalendarDateWithLang(dateRange);

        step("Step 47: Select room type and rate type, verifying at least one of each is displayed");
        dynamicSelections = bulkUpdatePage.selectAndVerifyRoomAndRateType();
        expectedRoom = dynamicSelections.get("roomName");
        expectedRatePlan = dynamicSelections.get("ratePlanName");

        step("Step 48: Turn on each restriction toggle");
        bulkUpdatePage.turnOnAllRestrictionToggles();

        step("Step 49: Verify that only Length of Stay and Advance Booking options are displayed for input");
        bulkUpdatePage.verifyOnlyLosAndAdvBookingInputsDisplayed();

        step("Step 50: Click Save to trigger restriction form validation on empty fields");
        bulkUpdatePage.clickSavePolicyButton();

        step("Step 51: Verify UI errors for empty restriction fields");
        bulkUpdatePage.verifyRestrictionValidationErrors(restrictionErrorEn, restrictionErrorId);

        step("Step 52: Enter limit restrictions (" + invalidMinLos + " for Min LOS, " + invalidMaxLos + " for Max LOS, " + invalidMinAdv + " for Min Adv, " + invalidMaxAdv + " for Max Adv)");
        bulkUpdatePage.fillRestrictionFields(invalidMinLos, invalidMaxLos, invalidMinAdv, invalidMaxAdv);

        step("Step 53: Verify the validation errors displayed for invalid Maximum limits");
        bulkUpdatePage.verifyMaxRestrictionErrors(invalidMaxLosEn, invalidMaxLosId, invalidMaxAdvEn, invalidMaxAdvId);

        step("Step 54: Enter limit restrictions (" + validMinLos + " for Min LOS, " + validMaxLos + " for Max LOS, " + validMinAdv + " for Min Adv, " + validMaxAdv + " for Max Adv)");
        bulkUpdatePage.fillRestrictionFields(validMinLos, validMaxLos, validMinAdv, validMaxAdv);

        step("Step 55: Click Save to trigger restriction form validation on empty fields");
        bulkUpdatePage.clickSavePolicyButton();

        step("Step 56: Verify the restriction confirmation modal displays the correct data");
        bulkUpdatePage.verifyRestrictionConfirmationModal(roomCount + " room types", roomCount + " tipe kamar", currentDateRange);

        step("Step 57: Click 'Yes, save' to confirm the restrictions bulk update");
        bulkUpdatePage.clickYesSaveRestriction();

        step("Step 58: Verify Another Pop-up appears and verify period date, restrictions policy, room, and rate plan details on the pop-up");
        bulkUpdatePage.verifyRestrictionSuccessSummaryModal(currentDateRange, expectedRoom, expectedRatePlan, expectedLosLimits, expectedAdvLimits);

        step("Step 59: Click on Okay and verify that the pop-up disappears");
        bulkUpdatePage.clickSummaryOkAndVerifyDisappears();

        step("Step 60: Turn off all restriction toggles");
        bulkUpdatePage.turnOffAllRestrictionToggles();

        step("Step 61: Click on Save");
        bulkUpdatePage.clickSavePolicyButton();

        step("Step 62: Verify that the confirmation pop-up appears with selected dates and total selected room/rate count");
        bulkUpdatePage.verifyRestrictionConfirmationModal(roomCount + " room types", roomCount + " tipe kamar", currentDateRange);

        step("Step 63: Click on Yes, Save");
        bulkUpdatePage.clickYesSaveRestriction();

        step("Step 64: Verify period date, room, and rate plan on the final success pop-up");
        bulkUpdatePage.verifyRestrictionSuccessSummaryModal(currentDateRange, expectedRoom, expectedRatePlan, "", "");

        step("Step 65: Click on Okay and verify that the pop-up disappears");
        bulkUpdatePage.clickSummaryOkAndVerifyDisappears();
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "ActivityLog")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserCanExportAndFilterActivityLogs() throws InterruptedException, IOException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        ActivityLogPage<?> activityLogPage = new ActivityLogPage<>(session);
        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("verifyUserCanExportAndFilterActivityLogs");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");

        step("Step 1: Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Step 2: Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Step 3: Navigate to the Activity Log Page from the left-side navigation panel and verify the Activity Log page is Loaded.");
        sideMenu.clickActivityLogsMenu();
        activityLogPage.verifyActivityLogPageLoaded();

        step("Step 4: Verify that all filter fields and buttons are displayed");
        activityLogPage.verifyFilterElementsAndButtonsDisplayed();

        step("Step 5: Verify default activities are retrieved, table is displayed, and columns match");
        activityLogPage.verifyActivityResultTableAndColumns();

        step("Step 6: Click on the Export to Excel hyperlink");
        activityLogPage.clickExportToExcelButton();

        step("Step 7: Verify that the Excel file is downloaded successfully and is in .xlsx format");
        activityLogPage.verifyExcelFileDownloaded();

        step("Step 8: Open the calendar for Start Date and verify only current month dates are selectable");
        activityLogPage.verifyStartDateCalendar();

        step("Step 9: Open the calendar for End Date and verify only current month dates are selectable");
        activityLogPage.verifyEndDateCalendar();

        step("Step 10: Verify Activity Type dropdown properties and first 5 options");
        activityLogPage.verifyActivityTypeDropdown();

        step("Step 11: Verify Form Type dropdown properties and options");
        activityLogPage.verifyFormTypeDropdown();

        step("Step 12: From the activity result table, copy the displayed User ID from the First row");
        String copiedUserId = activityLogPage.getFirstUserIdFromTable();

        step("Step 13: Paste the copied User ID into the Search User field, click GO, and verify the filtered data");
        activityLogPage.searchAndVerifyUser(copiedUserId);

        step("Step 14: Click on Reset filter button and verify the Search User input is empty");
        activityLogPage.clickResetFilterAndVerifySearchUserCleared();

        step("Step 15: Click on the Export to Excel hyperlink");
        activityLogPage.clickExportToExcelButton();

        step("Step 16: Verify that the Excel file is downloaded successfully and is in .xlsx format");
        activityLogPage.verifyExcelFileDownloaded();
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "ManageRatesAndAvailability")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyLogByItineraryIDShouldStartFromBookingDate() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        SideMenu<?> sideMenu = new SideMenu<>(session);
        AuthTest authTest = new AuthTest();
        Navbar<?> navbar = new Navbar<>(session);
        orderCreationPage<?> orderCreationPage = new orderCreationPage<>(session);
        ManageRatesAndAvailabilityPage<?> manageRatesAndAvailabilityPage = new ManageRatesAndAvailabilityPage<>(session);

        Map<String, String> param = getParam("verifyLogByItineraryIDShouldStartFromBookingDate");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");
        String username = param.get("username");
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));
        String startDateRaw = param.get("startDate").trim();
        String startDate = startDateRaw.split("-").length >= 3 ? startDateRaw : generateRandomDate(startDateRaw);
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String guestFullName = randomName() + " automation";
        String guestTitle = salutationList[0];
        String currency = param.get("currency");
        step("Create a booking and get the itinerary ID");

        orderCreationPage.createOrderByHotelIdAndGetItineraryID(username, startDate, hotelId,
                adult, night, room, guestFullName, guestTitle, currency);

        String itineraryID = String.valueOf(orderCreationPage.getItineraryID());
        String utcTime = orderCreationPage.getUtcTime();
        Assertion.assertThat("Verify UTC time format is not empty. UTC Format Time: "+utcTime, utcTime.isEmpty(), is(false));

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Go to Manage Rates and Availability page");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickManageRatesAndAvailabilityMenu();
        Assertion.assertThat("Manage Rates and Availability page is loaded", manageRatesAndAvailabilityPage.isManageRatesAndAvailabilityPageLoaded(), is(true));

        step("Click View Log and verify the log page loads in default state");
        manageRatesAndAvailabilityPage.clickViewLogAndVerifyPageLoaded();

        step("Click on Itinerary ID tab and search for the booked itinerary");
        manageRatesAndAvailabilityPage.searchByItineraryId(itineraryID);

        step("Verify the log entry displays the correct server time converted to UTC");
        manageRatesAndAvailabilityPage.verifyLogTimestampForItinerary(itineraryID, utcTime);

        step("Verify the Booking Summary displays correct guest name and booking time");
        manageRatesAndAvailabilityPage.verifyBookingSummaryDetails(guestFullName, utcTime);
    }

    @Author(names = {"Pankaj Singh Kushwaha", "Vipul Kumar"})
    @Module(name = "PromoAndCampaign")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyUserCanAddRemoveBlibliTiketRewards(){
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        PromoAndCampaignPage<?> promoAndCampaignPage = new PromoAndCampaignPage<>(session);
        ExtranetPage<?> extranetPage = new  ExtranetPage<>(session);
        PDPPageWeb<?> pdpPage = PDPPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("verifyUserCanAddRemoveBlibliTiketRewards");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String b2cEmail = param.get("b2cEmail");
        String b2cPassword = param.get("b2cPassword");
        String hotelId = param.get("hotelId");
        String removeMsgEn = param.get("removeMsgEn");
        String removeMsgId =  param.get("removeMsgId");
        String discountErrEn = param.get("discountErrEn");
        String discountErrId = param.get("discountErrId");
        String benefitErrEn = param.get("benefitErrEn");
        String benefitErrId = param.get("benefitErrId");
        String dateErrEn = param.get("dateErrEn");
        String dateErrId = param.get("dateErrId");
        String invalidMaxDis = param.get("invalidMaxDis");
        String invalidMinDis = param.get("invalidMinDis");
        String validDiscount = param.get("validDiscount");
        String addDiscount = param.get("addDiscount");
        String successEn = param.get("successEn");
        String successId = param.get("successId");
        String editMsgEn = param.get("editMsgEn");
        String editMsgId = param.get("editMsgId");
        String requestApproveMsgEn = param.get("requestApproveMsgEn");
        String requestApproveMsgId = param.get("requestApproveMsgId");


        LoginPage<?> mmLoginPage = new LoginPage<>(session);
        step("Member login (admin/member/login only — no MM dashboard URL)");
        mmLoginPage.goToDashboardMemberLoginPage();
        boolean onMemberLoginPage = mmLoginPage.isDashboardMemberLoginPageLoaded();
        if (onMemberLoginPage) {
            mmLoginPage.loginToDashboardMember(email, password);
            Assertion.assertThat("Login to dashboard member successful", mmLoginPage.isLoginToDashboardMemberSuccessful(email), is(true));
        } else {
            Assertion.assertThat("User has been logged in to Member Dashboard", mmLoginPage.isLoginToDashboardMemberSuccessful(email), is(true));
        }

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Open the promo and campaign side menu and select Blibli Tiket Reward and verify the Blibli Tiket Reward page is loaded");
        sideMenu.clickPromoAndCampaignMenu();
        sideMenu.clickBlibliTiketRewardsMenu();
        promoAndCampaignPage.verifyRewardsPageLoaded();

        step("Check current Blibli Tiket Rewards status and reset if necessary");
        boolean isReadyToJoin = promoAndCampaignPage.isJoinNowButtonPresent();

        if (!isReadyToJoin) {
            step("Property is enrolled. Removing property from rewards program");
            promoAndCampaignPage.removePropertyFromRewards();

            step("Verify the removal success snackbar");
            promoAndCampaignPage.verifySnackbar(removeMsgEn, removeMsgId);

            step("Verify the Join Now button reappears after removal");
            promoAndCampaignPage.verifyJoinNowButtonAppears();
        }

        step("Click on the Join Now button");
        promoAndCampaignPage.clickJoinNowButton();

        step("Verify the rewards setup form page is loaded");
        promoAndCampaignPage.verifyRewardsSetupPageLoaded();

        step("Click on add blackout dates");
        promoAndCampaignPage.clickAddBlackoutDates();

        step("Click submit Button without filling the fields.");
        promoAndCampaignPage.clickSubmitButton();

        step("Verify validation errors appear for all missing mandatory fields");
        promoAndCampaignPage.verifyUIValidationErrorMessage(discountErrEn, discountErrId);
        promoAndCampaignPage.verifyUIValidationErrorMessage(benefitErrEn, benefitErrId);
        promoAndCampaignPage.verifyUIValidationErrorMessage(dateErrEn, dateErrId);

        step("Enter discount value greater than 90% and verify error. Entered Value is: "+invalidMaxDis);
        promoAndCampaignPage.fillDiscount(invalidMaxDis);
        promoAndCampaignPage.verifyUIValidationErrorMessage("Maximum discount percentage is 90%.", "Maksimum persentase diskon 90%.");

        step("Enter discount value less than 10% and verify error. Entered Value is: "+invalidMinDis);
        promoAndCampaignPage.fillDiscount(invalidMinDis);
        promoAndCampaignPage.verifyUIValidationErrorMessage("Minimum discount percentage is 10%.", "Minimum persentase diskon 10%.");

        step("Enter valid discount value as 20% and verify it is accepted. Entered Value is: "+validDiscount);
        promoAndCampaignPage.fillDiscount(validDiscount);
        promoAndCampaignPage.verifyDiscountAccepted(validDiscount);

        step("Verify Extra Benefit options (Yes and No) are available");
        promoAndCampaignPage.verifyExtraBenefitOptionsAvailable();

        step("Select 'No' for Extra Benefit");
        promoAndCampaignPage.selectExtraBenefitNo();

        step("Select all rooms and all rate plans and verify it");
        promoAndCampaignPage.selectAllRoomsAndRatePlans();
        promoAndCampaignPage.verifyAllRoomsAndRatePlansSelected();

        step("Select today as the start date and 7 days from today as the end date");
        promoAndCampaignPage.selectBlackoutDateRangeNext7Days();

        step("Capture the selected blackout dates for future verification");
        List<String> blackoutDates = promoAndCampaignPage.getSelectedBlackoutDates();
        String blackoutStartDate = blackoutDates.get(0);
        String blackoutEndDate = blackoutDates.get(1);

        step("Submit the form and verify snackbar message");
        promoAndCampaignPage.clickSubmitButton();
        promoAndCampaignPage.verifySnackbar(successEn,successId);

        step("verify Blibli Tiket Rewards Page is loaded");
        promoAndCampaignPage.verifyRewardsPageLoaded();

        step("Verify the 'Join Now' button is removed");
        boolean isJoinNowBtnPresent = promoAndCampaignPage.isJoinNowButtonPresent();
        Assertion.assertThat("Join Now Button should not present",isJoinNowBtnPresent,is(false));

        step("Verify the summary page reflects the exact data we submitted");
        promoAndCampaignPage.verifySubmittedBenefitDetails(validDiscount, blackoutStartDate, blackoutEndDate);

        step("Verify the Remove from program hyperlink should be visible");
        Assertion.assertThat("Verify the Remove from program hyperlink should be visible", promoAndCampaignPage.isRemoveProgramLinkVisible(), is(true));

        step("Get hotel name from extranet");
        String extranetHotelName = navbar.getDisplayedHotelName().orElseThrow(() -> new IllegalStateException("Hotel name not found on extranet navbar"));

        step("Click property PDP link (located by href) and switch to PDP tab");
        Assert.assertTrue(extranetPage.isSeePropertyPageLinkPresent(), "Property PDP link (by href) is present");
        extranetPage.clickSeePropertyPageLinkAndSwitchToPDPTab();

        step("Verify PDP loads and hotel name in new tab");
        Assertion.assertThat("Hotel PDP is loaded in new tab", extranetPage.isHotelPDPLoaded(), is(true));
        String pdpHotelName = extranetPage.getHotelNameOnPDP();
        Assertion.assertThat("Hotel name on PDP. Expected: " + extranetHotelName + " Actual: " + pdpHotelName, pdpHotelName, is(extranetHotelName));

        step("Click on user avatar button and verify the tier is not Gold, Platinum, or Diamond");
        pdpPage.clickUserAvatarAndVerifyTierIsNotPremium();

        step("Click on See Rooms button");
        extranetPage.clickSeeRoom();

        step("Click on the search tab and select stay dates beginning 10 days out from today");
        pdpPage.selectDateAfterTenDays();

        step("Verify for a non-premium account that no discount or premium extra benefit fields are displayed");
        pdpPage.verifyPremiumBenefitsNotDisplayedForNonPremiumAccount();

        step("Close new tab and return to extranet");
        extranetPage.closePDPTabAndReturnToExtranet();

        step("Verify the response for the data outside of the blackout date range should display all benefits");
        String startDate = CoreFunctions.getJakartaDateYYYY_MM_DD(10);
        SelectRoomResponse response = hotelApi.getHotelRoomDataV2(extranetHotelName, b2cEmail, b2cPassword, 1, 1, 1, startDate, "IDR");
        Assertion.assertThat("Verify API status code is successful", response.code, is("SUCCESS"));
        promoAndCampaignPage.verifyCampaignDiscountApplied(response, validDiscount);

        step("Verify the response for the data within the blackout date range should not display all benefits");
        startDate = CoreFunctions.getJakartaDateYYYY_MM_DD(2);
        response = hotelApi.getHotelRoomDataV2(extranetHotelName, b2cEmail, b2cPassword, 1, 1, 1, startDate, "IDR");
        Assertion.assertThat("Verify API status code is successful", response.code, is("SUCCESS"));
        promoAndCampaignPage.verifyCampaignDiscountBlockedByBlackout(response);

        step("Click Manage benefit and verify Benefit edit page is Loaded");
        promoAndCampaignPage.clickManageBenefitAndVerifyFormOpens();
        promoAndCampaignPage.verifyBenefitEditPageLoaded(validDiscount);

        step("Change Extra Benefits to Yes, verify new fields appear, and Select all additional benefit options");
        promoAndCampaignPage.enableAndSelectAllExtraBenefits();

        step("In the Rooms & Rate Plans section, select specific room and rate plan options");
        promoAndCampaignPage.selectSpecificRoomsAndVerifyRatePlans();

        step("Extract room and benefit details from the form");
        String expectedRoomSummary = promoAndCampaignPage.getSelectedRoomSummaryText();
        List<String> expectedBenefitsList = promoAndCampaignPage.getSelectedExtraBenefitsTexts();

        step("Verify selected dates on UI - Previously selected blackout dates should be visible correctly");
        promoAndCampaignPage.verifyBlackoutDatesOnUI(blackoutStartDate, blackoutEndDate);

        step("Click submit, verify the confirmation modal appears, click on Yes, continue and verify the snackbar");
        promoAndCampaignPage.submitAndConfirmChanges();
        promoAndCampaignPage.verifySnackbar(editMsgEn,editMsgId);

        step("verify Blibli Tiket Rewards Page is loaded");
        promoAndCampaignPage.verifyRewardsPageLoaded();

        step("Verify the 'Join Now' button is removed");
        isJoinNowBtnPresent = promoAndCampaignPage.isJoinNowButtonPresent();
        Assertion.assertThat("Join Now Button should not present",isJoinNowBtnPresent,is(false));

        step("Verify that the 'See request' banner appears and click on it");
        promoAndCampaignPage.verifyAndClickSeeRequest();

        step("Verify all the dynamically filled details inside the request modal");
        promoAndCampaignPage.verifySubmittedRequestDetailsDynamic(expectedRoomSummary, expectedBenefitsList);

        step("Click on the Approve Button and Verify the Snackbar");
        promoAndCampaignPage.clickApproveButton();
        promoAndCampaignPage.verifySnackbar(requestApproveMsgEn, requestApproveMsgId);

        step("Verify the remove from program hyperlink is visible");
        Assertion.assertThat("Verify the Remove from program hyperlink should be visible", promoAndCampaignPage.isRemoveProgramLinkVisible(), is(true));

        step("Verify the final approved details on the summary page");
        promoAndCampaignPage.verifyApprovedSummaryDetails(validDiscount, expectedRoomSummary, expectedBenefitsList, blackoutStartDate, blackoutEndDate);

        step("Verify the response for the data within the blackout date range should not display all benefits");
        startDate = CoreFunctions.getJakartaDateYYYY_MM_DD(2);
        response = hotelApi.getHotelRoomDataV2(extranetHotelName, b2cEmail, b2cPassword, 1, 1, 1, startDate, "IDR");
        Assertion.assertThat("Verify API status code is successful", response.code, is("SUCCESS"));
        promoAndCampaignPage.verifyCampaignDiscountBlockedByBlackout(response);

        step("Validate API response for non-blackout dates (Benefits, loyaltyInfo, rateInfo, and extra benefits)");
        startDate = CoreFunctions.getJakartaDateYYYY_MM_DD(10);
        response = hotelApi.getHotelRoomDataV2(extranetHotelName, b2cEmail, b2cPassword, 1, 1, 1, startDate, "IDR");
        Assertion.assertThat("Verify API status code is successful", response.code, is("SUCCESS"));
        String expectedDiscount = String.valueOf(Integer.parseInt(validDiscount)+Integer.parseInt(addDiscount));
        promoAndCampaignPage.verifyCampaignAndExtraBenefitsForNonBlackoutDates(response, expectedDiscount);

        step("Verify the remove from program hyperlink is visible");
        Assertion.assertThat("Verify the Remove from program hyperlink should be visible", promoAndCampaignPage.isRemoveProgramLinkVisible(), is(true));

        step("Removing Benefit from rewards program");
        promoAndCampaignPage.removePropertyFromRewards();

        step("Verify the removal success snackbar");
        promoAndCampaignPage.verifySnackbar(removeMsgEn, removeMsgId);

        step("Verify the Join Now button reappears after removal");
        promoAndCampaignPage.verifyJoinNowButtonAppears();

        step("Verify the response, when the reward program is removed.");
        startDate = CoreFunctions.getJakartaDateYYYY_MM_DD(12);
        response = hotelApi.getHotelRoomDataV2(extranetHotelName, b2cEmail, b2cPassword, 1, 1, 1, startDate, "IDR");
        Assertion.assertThat("Verify API status code is successful", response.code, is("SUCCESS"));
        promoAndCampaignPage.verifyCampaignDiscountBlockedByBlackout(response);
    }

    @Author(names = {"Vipul Kumar"})
    @Module(name = "ExtranetCommonCases")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyAllSideMenusAndSubmenusOpenSuccessfullyOnInternationalHotel() {
        Map<String, String> param = getParam("testVerifyAllSideMenusAndSubmenusOpenSuccessfullyOnInternationalHotel");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        ApproveActivationPage<?> approveActivationPage = new ApproveActivationPage<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");

        LoginPage<?> mmLoginPage = new LoginPage<>(session);
        step("Member login (admin/member/login only — no MM dashboard URL)");
        mmLoginPage.goToDashboardMemberLoginPage();
        boolean onMemberLoginPage = mmLoginPage.isDashboardMemberLoginPageLoaded();
        if (onMemberLoginPage) {
            mmLoginPage.loginToDashboardMember(email, password);
            Assertion.assertThat("Login to dashboard member successful", mmLoginPage.isLoginToDashboardMemberSuccessful(email), is(true));
        } else {
            Assertion.assertThat("User has been logged in to Member Dashboard", mmLoginPage.isLoginToDashboardMemberSuccessful(email), is(true));
        }

        step("Open approve-activation page and search hotel: " + hotelId);
        approveActivationPage.open();
        approveActivationPage.searchByHotelId(hotelId);

        step("Open the found hotel — must redirect to Extranet");
        approveActivationPage.openHotelAndSwitchToExtranet(hotelId);

        step("Verify the exact same hotel is opened on Extranet. Expected: " + hotelId);
        navbar.skipAndCloseAnyPopUp();
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on Extranet navbar. Expected: " + hotelId + " Actual: " + navbar.getCurrentHotelIdFromHeader().orElse("no match"), hotelMatch, is(true));

        step("Verify all side menu items are displayed");
        for (SideMenu.SidebarMenu menu : SideMenu.SidebarMenu.values()) {
            boolean menuDisplayed = sideMenu.isMenuDisplayed(menu);
            Assertion.assertThat("Side menu displayed: " + menu, menuDisplayed, is(true));
        }

        step("Open each submenu and validate its page subject is present (rendered & not blocked)");
        for (SideMenu.SidebarSubmenu submenu : SideMenu.SidebarSubmenu.values()) {
            if (submenu.isDomesticOnly()) {
                continue;
            }
            boolean subjectPresent;
            String failureDetail = "";
            try {
                subjectPresent = sideMenu.openSubmenuAndVerifySubject(submenu);
            } catch (Exception e) {
                subjectPresent = false;
                failureDetail = " | error: " + e;
                log("Error opening submenu " + submenu.getParent() + " > " + submenu + "\n" + ExceptionUtils.getStackTrace(e));
            }
            Assertion.assertThat("Submenu page subject present (rendered & not blocked): " + submenu.getParent() + " > " + submenu + failureDetail, subjectPresent, is(true));
        }

        step("Go back to Home before logout");
        sideMenu.clickMenu(SideMenu.SidebarMenu.HOME);

        step("Logout from Extranet");
        new AuthTest().testVerifyUserCanLogoutFromExtranet(session);
    }

    @Author(names = {"Pankaj Singh Kushwaha"})
    @Module(name = "PromoAndCampaign")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserCanCreateActivateDeactivateAndDeleteEarlyBirdOffer() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        PromoAndCampaignPage<?> promoAndCampaignPage = new PromoAndCampaignPage<>(session);
        YourPromotionsPage<?> yourPromotionsPage = new YourPromotionsPage<>(session);
        AddNewPromoAndCampaignPage<?> addNewPromoAndCampaignPage = new AddNewPromoAndCampaignPage<>(session);
        EarlyBirdOfferPage<?> earlyBirdOfferPage = new EarlyBirdOfferPage<>(session);
        ExtranetPage<?> extranetPage = new ExtranetPage<>(session);
        PDPPageWeb<?> pdpPage = PDPPageWeb.getInstance(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserCanCreateActivateDeactivateAndDeleteEarlyBirdOffer");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String b2cEmail = param.get("b2cEmail");
        String b2cPassword = param.get("b2cPassword");
        String hotelId = param.get("hotelId");
        String pageTitleEn = param.get("pageTitleEn");
        String pageTitleId = param.get("pageTitleId");
        String pageSubtitleEn = param.get("pageSubtitleEn");
        String pageSubtitleId = param.get("pageSubtitleId");
        String promoNameRequiredErrorEn = param.get("promoNameRequiredErrorEn");
        String promoNameRequiredErrorId = param.get("promoNameRequiredErrorId");
        String discountPercentageRequiredErrorEn = param.get("discountPercentageRequiredErrorEn");
        String discountPercentageRequiredErrorId = param.get("discountPercentageRequiredErrorId");
        String minDaysRequiredErrorEn = param.get("minDaysRequiredErrorEn");
        String minDaysRequiredErrorId = param.get("minDaysRequiredErrorId");
        String selectPlatformRequiredErrorEn = param.get("selectPlatformRequiredErrorEn");
        String selectPlatformRequiredErrorId = param.get("selectPlatformRequiredErrorId");
        String selectRatePlanRequiredErrorEn = param.get("selectRatePlanRequiredErrorEn");
        String selectRatePlanRequiredErrorId = param.get("selectRatePlanRequiredErrorId");
        String startDateRequiredErrorEn = param.get("startDateRequiredErrorEn");
        String startDateRequiredErrorId = param.get("startDateRequiredErrorId");
        String endDateRequiredErrorEn = param.get("endDateRequiredErrorEn");
        String endDateRequiredErrorId = param.get("endDateRequiredErrorId");
        String endTimeGreaterThanStartTimeErrorEn = param.get("endTimeGreaterThanStartTimeErrorEn");
        String endTimeGreaterThanStartTimeErrorId = param.get("endTimeGreaterThanStartTimeErrorId");
        String dateRangeDaysTimeRequiredErrorEn = param.get("dateRangeDaysTimeRequiredErrorEn");
        String dateRangeDaysTimeRequiredErrorId = param.get("dateRangeDaysTimeRequiredErrorId");
        String percentageRequiredErrorEn = param.get("percentageRequiredErrorEn");
        String percentageRequiredErrorId = param.get("percentageRequiredErrorId");
        String earlyBirdPromoName = param.get("earlyBirdPromoName");
        String earlyBirdPromoNameEdited = earlyBirdPromoName + " Edited";
        String earlyBirdDiscountPercentage = param.get("earlyBirdDiscountPercentage");
        String earlyBirdMinDays = param.get("earlyBirdMinDays");
        String statusChangeSnackbarEn = param.get("statusChangeSnackbarEn");
        String statusChangeSnackbarId = param.get("statusChangeSnackbarId");
        String deletePromoSnackbarEn = param.get("deletePromoSnackbarEn");
        String deletePromoSnackbarId = param.get("deletePromoSnackbarId");
        String noSearchResultsTitleEn = param.get("noSearchResultsTitleEn");
        String noSearchResultsTitleId = param.get("noSearchResultsTitleId");

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Open the promo and campaign side menu and select Your Promotion List");
        sideMenu.clickPromoAndCampaignMenu();
        sideMenu.clickYourPromotionsMenu();
        Assertion.assertThat("Verify Promo List page is opened", yourPromotionsPage.isYourPromotionsPageLoaded(), is(true));

        step("Clean up any pre-existing Early Bird Offer promo (created or edited name) before creating a new one");
        yourPromotionsPage.deletePromoIfPresent(earlyBirdPromoNameEdited, noSearchResultsTitleEn, noSearchResultsTitleId);
        yourPromotionsPage.deletePromoIfPresent(earlyBirdPromoName, noSearchResultsTitleEn, noSearchResultsTitleId);

        step("Click Add a new promo button and verify the Promotions & Campaigns page is opened");
        yourPromotionsPage.clickAddNewPromoButton();
        Assertion.assertThat("Verify Promotions & Campaigns page is opened", addNewPromoAndCampaignPage.isPromotionsAndCampaignsPageLoaded(), is(true));

        step("Click Early Bird Offer card and verify the Early Bird Offer page is opened");
        addNewPromoAndCampaignPage.clickEarlyBirdOfferCard();
        Assertion.assertThat("Verify Early Bird Offer page is opened", earlyBirdOfferPage.isEarlyBirdOfferPageLoaded(), is(true));

        step("Verify the Early Bird Offer page title and subtitle in both languages");
        earlyBirdOfferPage.verifyEarlyBirdPageTitleAndSubtitle(pageTitleEn, pageTitleId, pageSubtitleEn, pageSubtitleId);

        step("Deselect all platforms");
        earlyBirdOfferPage.deselectAllPlatforms();

        step("Select Specific days and time in Booking Period");
        earlyBirdOfferPage.selectSpecificBookingPeriod();

        step("Select Specific dates in Stay Period");
        earlyBirdOfferPage.selectSpecificStayPeriod();

        step("Select Yes in Cancellation Policy");
        earlyBirdOfferPage.selectCancellationPolicyYes();

        step("Turn ON the Blackout Dates toggle");
        earlyBirdOfferPage.turnOnBlackoutDatesToggle();

        step("Click Review button without filling any required fields and verify all required validation errors are displayed");
        earlyBirdOfferPage.clickEarlyBirdReviewButton();
        promoAndCampaignPage.verifyUIValidationErrorMessage(promoNameRequiredErrorEn, promoNameRequiredErrorId);
        promoAndCampaignPage.verifyUIValidationErrorMessage(discountPercentageRequiredErrorEn, discountPercentageRequiredErrorId);
        promoAndCampaignPage.verifyUIValidationErrorMessage(minDaysRequiredErrorEn, minDaysRequiredErrorId);
        promoAndCampaignPage.verifyUIValidationErrorMessage(selectPlatformRequiredErrorEn, selectPlatformRequiredErrorId);
        promoAndCampaignPage.verifyUIValidationErrorMessage(selectRatePlanRequiredErrorEn, selectRatePlanRequiredErrorId);
        promoAndCampaignPage.verifyUIValidationErrorMessage(startDateRequiredErrorEn, startDateRequiredErrorId);
        promoAndCampaignPage.verifyUIValidationErrorMessage(endDateRequiredErrorEn, endDateRequiredErrorId);
        promoAndCampaignPage.verifyUIValidationErrorMessage(endTimeGreaterThanStartTimeErrorEn, endTimeGreaterThanStartTimeErrorId);
        promoAndCampaignPage.verifyUIValidationErrorMessage(dateRangeDaysTimeRequiredErrorEn, dateRangeDaysTimeRequiredErrorId);
        promoAndCampaignPage.verifyUIValidationErrorMessage(percentageRequiredErrorEn, percentageRequiredErrorId);

        step("Enter promo name, discount percentage and minimum days before check-in");
        earlyBirdOfferPage.fillOutPromoNameInput(earlyBirdPromoName);
        earlyBirdOfferPage.fillOutEarlyBirdDiscountPercentage(earlyBirdDiscountPercentage);
        earlyBirdOfferPage.fillOutEarlyBirdMinDays(earlyBirdMinDays);

        step("Verify the yellow information banner shows the entered Early Bird discount details");
        earlyBirdOfferPage.verifyEarlyBirdInfoBannerDisplayed(earlyBirdDiscountPercentage, earlyBirdMinDays);

        step("Select all platform options");
        earlyBirdOfferPage.selectAllPlatforms();

        step("Select the first room and its first rate plan");
        earlyBirdOfferPage.clickFirstRoomFirstRatePlanCheckbox();

        step("Booking Period: select start date today and end date 6 months from today");
        earlyBirdOfferPage.selectBookingPeriodStartDate(0);
        earlyBirdOfferPage.selectBookingPeriodEndDateMonthsFromToday(6);

        step("Booking Period: click Select All in the Select Days option");
        earlyBirdOfferPage.selectAllBookingPeriodDays();

        step("Booking Period: enter start time 00:00 and end time 23:59");
        earlyBirdOfferPage.fillOutBookingPeriodStartTime("00:00");
        earlyBirdOfferPage.fillOutBookingPeriodEndTime("23:59");
        earlyBirdOfferPage.verifyBookingPeriodTimesMatch("00:00", "23:59");

        step("Select Specific dates in Stay Period, matching the Booking Period date range");
        earlyBirdOfferPage.selectSpecificStayPeriod();
        earlyBirdOfferPage.selectStayPeriodStartDate(0);
        earlyBirdOfferPage.selectStayPeriodEndDateMonthsFromToday(6);

        step("Select Non-refundable, then Custom Policy, and verify the switch works both ways");
        earlyBirdOfferPage.selectNonRefundablePolicy();
        Assertion.assertThat("Verify Non-refundable policy is selected", earlyBirdOfferPage.isNonRefundablePolicySelected(), is(true));
        earlyBirdOfferPage.selectCustomPolicy();
        Assertion.assertThat("Verify Custom policy is selected", earlyBirdOfferPage.isCustomPolicySelected(), is(true));
        Assertion.assertThat("Verify Non-refundable policy is no longer selected", earlyBirdOfferPage.isNonRefundablePolicySelected(), is(false));

        step("Enter Cancellation Fee: 1 day, cancellation percentage 20%, no-show percentage 50%");
        earlyBirdOfferPage.fillOutCancellationDaysBefore("1");
        earlyBirdOfferPage.fillOutCancellationPercentage("20");
        earlyBirdOfferPage.fillOutNoShowPercentage("50");

        step("Verify the selected days and percentages appear in the cancellation policy summary banner");
        earlyBirdOfferPage.verifyCancellationPolicySummaryBanner("1", "20", "50");

        step("Turn ON the Combinable Promo toggle");
        earlyBirdOfferPage.turnOnCombinablePromoToggle();

        step("Select 15 random Blackout Dates");
        List<String> blackoutDates = earlyBirdOfferPage.selectRandomBlackoutDates(15);
        earlyBirdOfferPage.verifySelectedBlackoutDatesCount(15);

        step("Verify the selected dates area is scrollable when multiple dates are selected");
        earlyBirdOfferPage.verifyBlackoutDatesListIsScrollable();
        earlyBirdOfferPage.scrollSelectedBlackoutDatesListToBottom();

        step("Click Review and verify the promo name and the green Early Bird component are displayed in the review modal");
        earlyBirdOfferPage.clickEarlyBirdReviewButton();
        earlyBirdOfferPage.verifyPromoNameInReviewModal(earlyBirdPromoName);
        earlyBirdOfferPage.verifyEarlyBirdComponentInReviewModal(earlyBirdDiscountPercentage);

        step("Click Edit Promo and verify the user is returned to the same filled Early Bird Offer page");
        earlyBirdOfferPage.clickEditPromoInReviewModal();
        earlyBirdOfferPage.verifyEarlyBirdFormStillFilled(earlyBirdPromoName);

        step("Click Review again and verify the promo name and the green Early Bird component are still displayed correctly");
        earlyBirdOfferPage.clickEarlyBirdReviewButton();
        earlyBirdOfferPage.verifyPromoNameInReviewModal(earlyBirdPromoName);
        earlyBirdOfferPage.verifyEarlyBirdComponentInReviewModal(earlyBirdDiscountPercentage);

        step("Click Create and verify the promo is created successfully and the user is redirected to the Promo List page");
        earlyBirdOfferPage.clickCreatePromoInReviewModal();
        Assertion.assertThat("Verify user is redirected to the Promo List page", yourPromotionsPage.isYourPromotionsPageLoaded(), is(true));

        step("Search for the created promo by name");
        yourPromotionsPage.fillOutSearchPromoNameAndSearch(earlyBirdPromoName);

        step("Verify the created promo is present in the promo list");
        Assertion.assertThat("Verify the created promo is present in the list", yourPromotionsPage.isPromoPresentInList(earlyBirdPromoName), is(true));

        step("Verify the status toggle of the created promo is OFF by default");
        Assertion.assertThat("Verify status toggle is OFF by default", yourPromotionsPage.getPromoToggleState(earlyBirdPromoName), is(false));

        step("Turn ON the status toggle and verify the snackbar in both languages, then wait for it to dismiss");
        yourPromotionsPage.clickStatusToggle(earlyBirdPromoName);
        yourPromotionsPage.verifyStatusChangeSnackbarMessage(statusChangeSnackbarEn + " " + earlyBirdPromoName + ".", statusChangeSnackbarId + " " + earlyBirdPromoName + ".");
        yourPromotionsPage.waitForSnackbarToDismiss();

        step("Get hotel name from extranet for API verification");
        String extranetHotelName = navbar.getDisplayedHotelName().orElseThrow(() -> new IllegalStateException("Hotel name not found on extranet navbar"));

        step("Verify the Early Bird Offer promo is present in the room API response for a date outside the blackout range");
        String nonBlackoutStartDate = CoreFunctions.getJakartaDateYYYY_MM_DD(2);
        SelectRoomResponse response = hotelApi.getHotelRoomDataV2(extranetHotelName, b2cEmail, b2cPassword, 1, 1, 1, nonBlackoutStartDate, "IDR");
        Assertion.assertThat("Verify API status code is successful", response.code, is("SUCCESS"));
        promoAndCampaignPage.verifyEarlyBirdPromoApplied(response);

        step("Verify the Early Bird Offer promo is NOT present in the room API response for a date within the blackout range");
        String blackoutStartDate = blackoutDates.get(0);
        response = hotelApi.getHotelRoomDataV2(extranetHotelName, b2cEmail, b2cPassword, 1, 1, 1, blackoutStartDate, "IDR");
        Assertion.assertThat("Verify API status code is successful", response.code, is("SUCCESS"));
        promoAndCampaignPage.verifyEarlyBirdPromoNotApplied(response);

        step("Turn OFF the status toggle, verify the confirmation popup, and click Cancel the first time");
        yourPromotionsPage.clickStatusToggle(earlyBirdPromoName);
        Assertion.assertThat("Verify the deactivate confirmation popup is displayed with Cancel and Deactivate buttons", yourPromotionsPage.isDeactivateConfirmationPopupDisplayed(), is(true));
        yourPromotionsPage.clickCancelInDeactivateConfirmation();

        step("Verify the status toggle remains ON after Cancel");
        Assertion.assertThat("Verify status toggle remains ON after Cancel", yourPromotionsPage.getPromoToggleState(earlyBirdPromoName), is(true));

        step("Click the status toggle again and this time click Deactivate");
        yourPromotionsPage.clickStatusToggle(earlyBirdPromoName);
        yourPromotionsPage.clickDeactivateInDeactivateConfirmation();

        step("Verify the snackbar in both languages, wait for it to dismiss, and verify the status toggle is now OFF");
        yourPromotionsPage.verifyStatusChangeSnackbarMessage(statusChangeSnackbarEn + " " + earlyBirdPromoName + ".", statusChangeSnackbarId + " " + earlyBirdPromoName + ".");
        yourPromotionsPage.waitForSnackbarToDismiss();
        Assertion.assertThat("Verify status toggle is OFF", yourPromotionsPage.getPromoToggleState(earlyBirdPromoName), is(false));

        step("Verify the Early Bird Offer promo is NOT present in the room API response after deactivating the promo");
        response = hotelApi.getHotelRoomDataV2(extranetHotelName, b2cEmail, b2cPassword, 1, 1, 1, nonBlackoutStartDate, "IDR");
        Assertion.assertThat("Verify API status code is successful", response.code, is("SUCCESS"));
        promoAndCampaignPage.verifyEarlyBirdPromoNotApplied(response);

        step("Click the 3-dot action menu and click Edit to navigate to the Edit Early Bird Offer page");
        yourPromotionsPage.clickRowActionMenu(earlyBirdPromoName);
        yourPromotionsPage.clickEditMenuItem();
        Assertion.assertThat("Verify the Edit Early Bird Offer page is opened", earlyBirdOfferPage.isEarlyBirdOfferPageLoaded(), is(true));

        step("Edit only the promo name to " + earlyBirdPromoNameEdited + "fill out the early bird minimum days");
        earlyBirdOfferPage.fillOutPromoNameInput(earlyBirdPromoNameEdited);
        earlyBirdOfferPage.fillOutEarlyBirdMinDays(earlyBirdMinDays);

        step("Click Review Promo and verify the edited promo name and the green Early Bird component are displayed");
        earlyBirdOfferPage.clickEarlyBirdReviewButton();
        earlyBirdOfferPage.verifyPromoNameInReviewModal(earlyBirdPromoNameEdited);
        earlyBirdOfferPage.verifyEarlyBirdComponentInReviewModal(earlyBirdDiscountPercentage);

        step("Click Save Promo Changes and verify the user is redirected back to the Promo List page");
        earlyBirdOfferPage.clickCreatePromoInReviewModal();
        Assertion.assertThat("Verify user is redirected to the Promo List page after saving changes", yourPromotionsPage.isYourPromotionsPageLoaded(), is(true));

        step("Search for the edited promo by name and verify it is present in the list");
        yourPromotionsPage.fillOutSearchPromoNameAndSearch(earlyBirdPromoNameEdited);
        Assertion.assertThat("Verify the edited promo is present in the list", yourPromotionsPage.isPromoPresentInList(earlyBirdPromoNameEdited), is(true));

        step("Verify the status toggle of the edited promo is OFF");
        Assertion.assertThat("Verify status toggle is OFF", yourPromotionsPage.getPromoToggleState(earlyBirdPromoNameEdited), is(false));

        step("Click the three-dot menu of the edited promo and click Delete");
        yourPromotionsPage.clickRowActionMenu(earlyBirdPromoNameEdited);
        yourPromotionsPage.clickDeleteMenuItem();

        step("Verify the delete confirmation popup appears with Cancel and Delete buttons, then click Cancel first");
        Assertion.assertThat("Verify the delete confirmation popup is displayed with Cancel and Delete buttons", yourPromotionsPage.isDeleteConfirmationPopupDisplayed(), is(true));
        yourPromotionsPage.clickCancelInDeleteConfirmation();

        step("Verify the promo is not deleted and is still present in the promo list");
        Assertion.assertThat("Verify the promo is still present in the list after Cancel", yourPromotionsPage.isPromoPresentInList(earlyBirdPromoNameEdited), is(true));

        step("Click the three-dot menu again, click Delete, and this time click Delete in the confirmation popup");
        yourPromotionsPage.clickRowActionMenu(earlyBirdPromoNameEdited);
        yourPromotionsPage.clickDeleteMenuItem();
        yourPromotionsPage.clickDeleteInDeleteConfirmation();

        step("Verify the successful deletion snackbar appears and wait for it to dismiss");
        yourPromotionsPage.verifyStatusChangeSnackbarMessage(deletePromoSnackbarEn + " " + earlyBirdPromoNameEdited + ".", deletePromoSnackbarId + " " + earlyBirdPromoNameEdited + ".");
        yourPromotionsPage.waitForSnackbarToDismiss();

        step("Search for the deleted promo and verify the no search results message is displayed");
        yourPromotionsPage.fillOutSearchPromoNameAndSearch(earlyBirdPromoNameEdited);
        yourPromotionsPage.verifyNoSearchResultsMessage(noSearchResultsTitleEn, noSearchResultsTitleId);

        step("Verify the Early Bird Offer promo is NOT present in the room API response after deleting the promo");
        response = hotelApi.getHotelRoomDataV2(extranetHotelName, b2cEmail, b2cPassword, 1, 1, 1, nonBlackoutStartDate, "IDR");
        Assertion.assertThat("Verify API status code is successful", response.code, is("SUCCESS"));
        promoAndCampaignPage.verifyEarlyBirdPromoNotApplied(response);
    }
}