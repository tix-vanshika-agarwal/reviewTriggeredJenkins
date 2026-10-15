package com.tiket.web.dashboard.test.hotel.extranet;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.extranet.ChildrenRatePlanPage;
import com.tiket.web.dashboard.page.hotel.extranet.Navbar;
import com.tiket.web.dashboard.page.hotel.extranet.SideMenu;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.is;

public class ChildrenRatePlanTest extends BaseTest {

    @Author(names = {"Vipul Kumar"})
    @Module(name = "RatesAndAvailabilityMenu")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserCanCreateActivateEditAndDeleteChildrenRatePlan() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        ChildrenRatePlanPage<?> childrenRatePlanPage = new ChildrenRatePlanPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);
        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserCanCreateActivateEditAndDeleteChildrenRatePlan");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelId = param.get("hotelId");
        String oldRatePlanName = param.get("oldRatePlanName");
        String newRatePlanName = param.get("newRatePlanName");
        String ratePlanName = param.getOrDefault("ratePlanName", oldRatePlanName);
        String price = param.get("price");
        String newPrice = param.get("newPrice");
        String name = oldRatePlanName;
        String newName = newRatePlanName;

        step("Login to extranet with automation credential");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Verify correct hotel from testdata");
        navbar.changeHotel(hotelId);
        boolean hotelMatch = navbar.verifyHotelIdOnNavbar(hotelId);
        Assertion.assertThat("Navbar shows correct hotel from testdata. Expected: " + hotelId + " Actual: " + (hotelMatch ? hotelId : "no match"), hotelMatch, is(true));

        step("Go to children rate plan page (Rates & Availability Menu)");
        sideMenu.clickRatesAndAvailabilityMenu();
        sideMenu.clickChildrenRatePlanMenu();
        Assertion.assertThat("Children rate plan page is loaded", childrenRatePlanPage.isChildrenRatePlanPageLoaded(), is(true));
        childrenRatePlanPage.dismissChildrenRatePlanLandingCoachmark();

        step("Delete existing children rate plan if any (by create name) and verify deleted");
        if (childrenRatePlanPage.isChildrenRatePlanDisplayed(name)) {
            childrenRatePlanPage.clickSettingByChildrenRatePlanName(name);
            childrenRatePlanPage.deleteChildrenRatePlan(name);
            Assertion.assertThat("Delete confirm modal is closed after delete", childrenRatePlanPage.isDeleteChildrenRatePlanConfirmModalDisplayed(), is(false));
            Assertion.assertThat("Children rate plan '" + name + "' is removed from list", childrenRatePlanPage.isChildrenRatePlanDisplayed(name), is(false));
        }

        step("Verify user able to create Children rate plan (Rates & Availability)");
        childrenRatePlanPage.clickAddChildrenRatePlanButton();
        Assertion.assertThat("Add children rate plan form is displayed", childrenRatePlanPage.isAddChildrenRatePlanFormPageLoaded(), is(true));
        childrenRatePlanPage.skipAddChildrenRatePlanLandingCoachmark();
        childrenRatePlanPage.fillOutAllChildrenRatePlanNameInput(name);
        childrenRatePlanPage.selectRatePlan(ratePlanName);
        boolean chipMatches = childrenRatePlanPage.isRatePlanChipTextMatching(ratePlanName);
        Assertion.assertThat("Selected rate plan chip shows '" + ratePlanName + "'. Expected: " + ratePlanName + " Actual: " + (chipMatches ? ratePlanName : "no match"), chipMatches, is(true));
        childrenRatePlanPage.enterRate(price);
        childrenRatePlanPage.clickAddChildrenRatePlanSubmitButton();
        childrenRatePlanPage.clickSaveChildrenRatePlanConfirmButton();
        Assertion.assertThat("Children rate plan '" + name + "' is visible in list", childrenRatePlanPage.isChildrenRatePlanDisplayed(name), is(true));
        childrenRatePlanPage.skipChildrenRatePlanLandingCoachmark();

        step("Verify user able to activate Children rate plan (Rates & Availability)");
        childrenRatePlanPage.clickChildrenRatePlanToggleByName(name, true);
        Assertion.assertThat("Success snackbar appears after activate", childrenRatePlanPage.isSuccessSnackbarAppeared(), is(true));
        Assertion.assertThat("Success snackbar is dismissed", childrenRatePlanPage.isSuccessSnackbarDisappeared(), is(true));
        Assertion.assertThat("Children rate plan '" + name + "' status is active", childrenRatePlanPage.isChildrenRatePlanActiveByName(name), is(true));

        step("Verify user able to deactivate Children rate plan (Rates & Availability)");
        childrenRatePlanPage.clickChildrenRatePlanToggleByName(name, false);
        Assertion.assertThat("Success snackbar appears after deactivate", childrenRatePlanPage.isSuccessSnackbarAppeared(), is(true));
        Assertion.assertThat("Success snackbar is dismissed", childrenRatePlanPage.isSuccessSnackbarDisappeared(), is(true));
        Assertion.assertThat("Children rate plan '" + name + "' status is inactive", childrenRatePlanPage.isChildrenRatePlanActiveByName(name), is(false));

        step("Activate children rate plan again (back to active)");
        childrenRatePlanPage.clickChildrenRatePlanToggleByName(name, true);
        Assertion.assertThat("Success snackbar appears after activate", childrenRatePlanPage.isSuccessSnackbarAppeared(), is(true));
        Assertion.assertThat("Success snackbar is dismissed", childrenRatePlanPage.isSuccessSnackbarDisappeared(), is(true));
        Assertion.assertThat("Children rate plan '" + name + "' status is active", childrenRatePlanPage.isChildrenRatePlanActiveByName(name), is(true));

        step("Verify user able to edit Children rate plan (Rates & Availability)");
        childrenRatePlanPage.clickSettingByChildrenRatePlanName(name);
        childrenRatePlanPage.clickEditChildrenRatePlan(name);
        Assertion.assertThat("Edit form (add children rate plan form) is displayed", childrenRatePlanPage.isAddChildrenRatePlanFormPageLoaded(), is(true));
        childrenRatePlanPage.skipAddChildrenRatePlanLandingCoachmark();
        childrenRatePlanPage.fillOutAllChildrenRatePlanNameInput(newName);
        childrenRatePlanPage.enterRate(newPrice);
        childrenRatePlanPage.clickAddChildrenRatePlanSubmitButton();
        childrenRatePlanPage.clickSaveChildrenRatePlanConfirmButton();
        Assertion.assertThat("Children rate plan list page is loaded after edit", childrenRatePlanPage.isChildrenRatePlanPageLoaded(), is(true));
        Assertion.assertThat("Row with new name '" + newName + "' is visible after edit", childrenRatePlanPage.isChildrenRatePlanDisplayedBySettingName(newName), is(true));

        step("Verify user able to delete Children rate plan (Rates & Availability)");
        childrenRatePlanPage.clickSettingByChildrenRatePlanName(newName);
        childrenRatePlanPage.deleteChildrenRatePlan(newName);
        Assertion.assertThat("Delete confirm modal is closed after delete", childrenRatePlanPage.isDeleteChildrenRatePlanConfirmModalDisplayed(), is(false));
        Assertion.assertThat("Children rate plan '" + newName + "' is removed from list", childrenRatePlanPage.isChildrenRatePlanDisplayedBySettingName(newName), is(false));
    }
}