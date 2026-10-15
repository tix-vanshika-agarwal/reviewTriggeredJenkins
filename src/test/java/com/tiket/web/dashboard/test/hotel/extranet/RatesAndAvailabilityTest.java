package com.tiket.web.dashboard.test.hotel.extranet;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.extranet.Navbar;
import com.tiket.web.dashboard.page.hotel.extranet.RatesAndAvailabilityPage;
import com.tiket.web.dashboard.page.hotel.extranet.SideMenu;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class RatesAndAvailabilityTest extends BaseTest {

    @Author(names = {"Vipul Kumar"})
    @Module(name = "RatesAndAvailabilityMenu")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testUserCanAddActivateDeactivateAndDeleteSurcharge() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        RatesAndAvailabilityPage<?> ratesAndAvailabilityPage = new RatesAndAvailabilityPage<>(session);
        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testUserCanAddActivateDeactivateAndDeleteSurcharge");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Go to Rates and Availability menu");
        sideMenu.clickRatesAndAvailabilityMenu();

        step("Go to Surcharge submenu. Expected: Surcharge page loaded");
        sideMenu.clickSurchargeSubmenu();
        boolean surchargePageLoaded = ratesAndAvailabilityPage.isSurchargePageLoaded();
        Assertion.assertThat("Surcharge page is loaded. Expected: true Actual: " + surchargePageLoaded, surchargePageLoaded, is(true));

        String surchargeName = param.get("surchargeName");
        step("If surcharge with same name exists, delete it first: " + surchargeName);
        ratesAndAvailabilityPage.deleteSurchargeByNameIfPresent(surchargeName);

        step("Click Add New Surcharge button");
        ratesAndAvailabilityPage.clickAddNewSurchargeButton();

        step("Fill New Surcharge form. Expected: New surcharge form loaded");
        boolean formLoaded = ratesAndAvailabilityPage.isNewSurchargeFormLoaded();
        Assertion.assertThat("New Surcharge form is loaded. Expected: true Actual: " + formLoaded, formLoaded, is(true));
        ratesAndAvailabilityPage.fillSurchargeName(surchargeName);
        int startDaysFromToday = Integer.parseInt(param.get("stayDateStart").trim());
        int endDaysFromStart = Integer.parseInt(param.get("stayDateEnd").trim());
        ratesAndAvailabilityPage.fillStayDatesFromToday(startDaysFromToday, endDaysFromStart);
        ratesAndAvailabilityPage.selectSurchargeTypeRoom();
        String surchargePrice = param.get("surchargePrice");
        ratesAndAvailabilityPage.fillSurchargePrice(surchargePrice);
        ratesAndAvailabilityPage.selectFirstSurchargeInfo();

        step("Submit New Surcharge");
        ratesAndAvailabilityPage.clickNewSurchargeSubmit();

        step("Click Back to List on success modal");
        ratesAndAvailabilityPage.clickBackToListFromSuccessModal();

        step("Verify surcharge list page is loaded after create. Expected: list page visible");
        boolean listLoadedAfterCreate = ratesAndAvailabilityPage.isSurchargePageLoaded();
        Assertion.assertThat("Surcharge list page after create. Expected: true Actual: " + listLoadedAfterCreate, listLoadedAfterCreate, is(true));

        step("Validate surcharge name. Expected: " + surchargeName + " in list");
        ratesAndAvailabilityPage.searchSurcharge(surchargeName);
        boolean surchargePresent = ratesAndAvailabilityPage.isSurchargeDisplayed(surchargeName);
        Assertion.assertThat("Surcharge name in list. Expected: true (name: " + surchargeName + ") Actual: " + surchargePresent, surchargePresent, is(true));

        boolean initiallyActive = ratesAndAvailabilityPage.isSurchargeActiveByName(surchargeName);
        if (initiallyActive) {
            step("Turn surcharge OFF: " + surchargeName + ". Expected: toggle OFF");
            ratesAndAvailabilityPage.clickSurchargeToggle(surchargeName, false);
            boolean offAfterClick = ratesAndAvailabilityPage.isSurchargeActiveByName(surchargeName);
            Assertion.assertThat("Toggle state after turn OFF. Expected: false Actual: " + offAfterClick, offAfterClick, is(false));
            step("Turn surcharge ON: " + surchargeName + ". Expected: toggle ON");
            ratesAndAvailabilityPage.clickSurchargeToggle(surchargeName, true);
            boolean onAtEnd = ratesAndAvailabilityPage.isSurchargeActiveByName(surchargeName);
            Assertion.assertThat("Toggle state at end. Expected: true Actual: " + onAtEnd, onAtEnd, is(true));
        } else {
            step("Turn surcharge ON: " + surchargeName + ". Expected: toggle ON");
            ratesAndAvailabilityPage.clickSurchargeToggle(surchargeName, true);
            boolean onAfterClick = ratesAndAvailabilityPage.isSurchargeActiveByName(surchargeName);
            Assertion.assertThat("Toggle state after turn ON. Expected: true Actual: " + onAfterClick, onAfterClick, is(true));
            step("Turn surcharge OFF: " + surchargeName + ". Expected: toggle OFF");
            ratesAndAvailabilityPage.clickSurchargeToggle(surchargeName, false);
            boolean offAfterClick = ratesAndAvailabilityPage.isSurchargeActiveByName(surchargeName);
            Assertion.assertThat("Toggle state after turn OFF. Expected: false Actual: " + offAfterClick, offAfterClick, is(false));
            step("Turn surcharge ON: " + surchargeName + ". Expected: toggle ON");
            ratesAndAvailabilityPage.clickSurchargeToggle(surchargeName, true);
            boolean onAtEnd = ratesAndAvailabilityPage.isSurchargeActiveByName(surchargeName);
            Assertion.assertThat("Toggle state at end. Expected: true Actual: " + onAtEnd, onAtEnd, is(true));
        }

        step("Delete surcharge: " + surchargeName);
        ratesAndAvailabilityPage.clickDeleteSurcharge(surchargeName);
        ratesAndAvailabilityPage.confirmDeleteSurchargeIfPresent();

        step("Click Back to List on delete success modal");
        ratesAndAvailabilityPage.clickBackToListFromSuccessModal();

        step("Verify surcharge is deleted: " + surchargeName + ". Expected: not in list");
        ratesAndAvailabilityPage.searchSurcharge(surchargeName);
        boolean stillPresent = ratesAndAvailabilityPage.isSurchargeDisplayed(surchargeName);
        Assertion.assertThat("Surcharge deleted. Expected: false Actual: " + stillPresent, stillPresent, is(false));
    }

    @Author(names = {"Vipul Kumar"})
    @Module(name = "RatesAndAvailabilityMenu")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testEditCaseCopyEditToggleDeleteSurcharge() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        RatesAndAvailabilityPage<?> ratesAndAvailabilityPage = new RatesAndAvailabilityPage<>(session);
        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testEditCaseCopyEditToggleDeleteSurcharge");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");
        String existingSurchargeName = param.get("existingSurchargeName");
        String copiedSurchargeName = param.get("copiedSurchargeName");

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata. Expected: " + hotelId);
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Hotel on navbar. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Go to Rates and Availability menu");
        sideMenu.clickRatesAndAvailabilityMenu();

        step("Go to Surcharge submenu. Expected: Surcharge page loaded");
        sideMenu.clickSurchargeSubmenu();
        boolean surchargePageLoaded = ratesAndAvailabilityPage.isSurchargePageLoaded();
        Assertion.assertThat("Surcharge page is loaded. Expected: true Actual: " + surchargePageLoaded, surchargePageLoaded, is(true));

        step("If surcharge with same copied name exists, delete it first: " + copiedSurchargeName);
        ratesAndAvailabilityPage.deleteSurchargeByNameIfPresent(copiedSurchargeName);

        step("Verify existing surcharge is present. Expected: " + existingSurchargeName);
        ratesAndAvailabilityPage.searchSurcharge(existingSurchargeName);
        boolean existingPresent = ratesAndAvailabilityPage.isSurchargeDisplayed(existingSurchargeName);
        Assertion.assertThat("Existing surcharge present. Expected: true Actual: " + existingPresent, existingPresent, is(true));

        step("Copy surcharge: " + existingSurchargeName);
        ratesAndAvailabilityPage.clickCopySurcharge(existingSurchargeName);
        Assertion.assertThat("Copy form loaded. Expected: true", ratesAndAvailabilityPage.isNewSurchargeFormLoaded(), is(true));
        ratesAndAvailabilityPage.fillSurchargeName(copiedSurchargeName);
        int startDaysFromToday = Integer.parseInt(param.get("stayDateStart").trim());
        int endDaysFromStart = Integer.parseInt(param.get("stayDateEnd").trim());
        ratesAndAvailabilityPage.fillStayDatesFromToday(startDaysFromToday, endDaysFromStart);
        ratesAndAvailabilityPage.selectSurchargeTypeRoom();
        String surchargePrice = param.get("surchargePrice");
        if (surchargePrice != null && !surchargePrice.isBlank()) {
            ratesAndAvailabilityPage.fillSurchargePrice(surchargePrice);
        }
        ratesAndAvailabilityPage.selectFirstSurchargeInfo();
        ratesAndAvailabilityPage.clickNewSurchargeSubmit();
        ratesAndAvailabilityPage.clickBackToListFromSuccessModal();

        step("Edit case: toggle ON/OFF on copied surcharge: " + copiedSurchargeName);
        ratesAndAvailabilityPage.searchSurcharge(copiedSurchargeName);
        boolean copiedPresent = ratesAndAvailabilityPage.isSurchargeDisplayed(copiedSurchargeName);
        Assertion.assertThat("Copied surcharge in list. Expected: true Actual: " + copiedPresent, copiedPresent, is(true));
        boolean initiallyActive = ratesAndAvailabilityPage.isSurchargeActiveByName(copiedSurchargeName);
        if (initiallyActive) {
            step("Turn surcharge OFF: " + copiedSurchargeName);
            ratesAndAvailabilityPage.clickSurchargeToggle(copiedSurchargeName, false);
            boolean offAfterClick = ratesAndAvailabilityPage.isSurchargeActiveByName(copiedSurchargeName);
            Assertion.assertThat("Toggle OFF. Expected: false Actual: " + offAfterClick, offAfterClick, is(false));
            step("Turn surcharge ON: " + copiedSurchargeName);
            ratesAndAvailabilityPage.clickSurchargeToggle(copiedSurchargeName, true);
            boolean onAtEnd = ratesAndAvailabilityPage.isSurchargeActiveByName(copiedSurchargeName);
            Assertion.assertThat("Toggle ON at end. Expected: true Actual: " + onAtEnd, onAtEnd, is(true));
        } else {
            step("Turn surcharge ON: " + copiedSurchargeName);
            ratesAndAvailabilityPage.clickSurchargeToggle(copiedSurchargeName, true);
            boolean onAfterClick = ratesAndAvailabilityPage.isSurchargeActiveByName(copiedSurchargeName);
            Assertion.assertThat("Toggle ON. Expected: true Actual: " + onAfterClick, onAfterClick, is(true));
            step("Turn surcharge OFF: " + copiedSurchargeName);
            ratesAndAvailabilityPage.clickSurchargeToggle(copiedSurchargeName, false);
            boolean offAfterClick = ratesAndAvailabilityPage.isSurchargeActiveByName(copiedSurchargeName);
            Assertion.assertThat("Toggle OFF. Expected: false Actual: " + offAfterClick, offAfterClick, is(false));
            step("Turn surcharge ON: " + copiedSurchargeName);
            ratesAndAvailabilityPage.clickSurchargeToggle(copiedSurchargeName, true);
            boolean onAtEnd = ratesAndAvailabilityPage.isSurchargeActiveByName(copiedSurchargeName);
            Assertion.assertThat("Toggle ON at end. Expected: true Actual: " + onAtEnd, onAtEnd, is(true));
        }

        step("Delete copied surcharge: " + copiedSurchargeName);
        ratesAndAvailabilityPage.clickDeleteSurcharge(copiedSurchargeName);
        ratesAndAvailabilityPage.confirmDeleteSurchargeIfPresent();
        ratesAndAvailabilityPage.clickBackToListFromSuccessModal();

        step("Verify copied surcharge is deleted. Expected: not in list");
        ratesAndAvailabilityPage.searchSurcharge(copiedSurchargeName);
        boolean copiedStillPresent = ratesAndAvailabilityPage.isSurchargeDisplayed(copiedSurchargeName);
        Assertion.assertThat("Copied surcharge deleted. Expected: false Actual: " + copiedStillPresent, copiedStillPresent, is(false));
    }
}
