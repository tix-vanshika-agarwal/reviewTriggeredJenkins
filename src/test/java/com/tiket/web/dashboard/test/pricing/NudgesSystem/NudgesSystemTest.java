package com.tiket.web.dashboard.test.pricing.NudgesSystem;

import com.tiket.core.CoreFunctions;
import com.tiket.testbase.BaseTest;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberPage;
import com.tiket.web.dashboard.page.member_dashboard.PricingDashboardHome;
import com.tiket.web.dashboard.page.platform.pricing.PricingDashboardPageWeb;
import com.tiket.web.dashboard.page.pricingDashboard.NudgesSystem.NudgesSystemPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.CoreMatchers.is;


public class NudgesSystemTest extends BaseTest {

    @Author(names = {"Manan Kumar"})
    @Module(name = "Nudges System")
    @Tribe(name = "Pricing")
    @Vertical(name = "Pricing")
    @Test
    public void testEditNudgeRulePrefilledSteps() {
        Map<String, String> map = getParam("testEditNudgeRulePrefilledSteps");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        NudgesSystemPageWeb<?> nudgesSystemPage = new NudgesSystemPageWeb<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb =  PricingDashboardPageWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");

        step("User Log in to Panel dashboard");
        memberLoginPage.gotoMemberLoginPage();
        memberLoginPage.adminLogin(email, password);

        step("User navigates to Pricing Dashboard");
        pricingDashboardPageWeb.goToPage();

        step("User navigates to Nudges System Page");
        nudgesSystemPage.goToNudgesSystemPage();

        step("User verifies Nudges System Page is loaded");
        Assertion.assertThat("Nudges System page is loaded", nudgesSystemPage.isNudgesSystemPageLoaded(), is(true));

        step("User clicks on Settings button to open dropdown");
        nudgesSystemPage.clickSettingsButton();

        step("User clicks on Edit button from dropdown");
        nudgesSystemPage.clickEditButton();

        step("User verifies General Rule section is pre-filled");
        Assertion.assertThat("General Rule section is pre-filled", nudgesSystemPage.verifyGeneralRuleIsPreFilled(), is(true));

        step("User verifies Targeting Action section is pre-filled");
        Assertion.assertThat("Targeting Action section is pre-filled", nudgesSystemPage.verifyTargetActionIsPreFilled(), is(true));

        step("User verifies Condition section is pre-filled");
        Assertion.assertThat("Condition section is pre-filled", nudgesSystemPage.verifyConditionIsPreFilled(), is(true));

        step("User verifies Schedule section is pre-filled");
        Assertion.assertThat("Schedule section is pre-filled", nudgesSystemPage.verifyScheduleIsPreFilled(), is(true));

        step("User verifies Nudges Preview section is pre-filled");
        Assertion.assertThat("Nudges Preview section is pre-filled", nudgesSystemPage.verifyNudgesPreviewIsPreFilled(), is(true));

        step("Log out");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Nudges System")
    @Tribe(name = "Pricing")
    @Vertical(name = "Pricing")
    @Test
    public void testNudgesSystemMenuDisplayed() {
        Map<String, String> map = getParam("testNudgesSystemMenuDisplayed");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        NudgesSystemPageWeb<?> nudgesSystemPage = new NudgesSystemPageWeb<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb =  PricingDashboardPageWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");

        step("User Log in to Panel dashboard");
        memberLoginPage.adminLoginViaCookie(email, password);

        step("User navigates to Pricing Dashboard");
        pricingDashboardPageWeb.goToPage();

        step("User navigates to Nudges System Page");
        nudgesSystemPage.goToNudgesSystemPage();

        step("User verifies Nudges System menu is displayed on the Pricing Dashboard");
        Assertion.assertThat("Nudges System menu is displayed", nudgesSystemPage.isNudgesSystemPageLoaded(), is(true));

        step("Log out");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Nudges System")
    @Tribe(name = "Pricing")
    @Vertical(name = "Pricing")
    @Test
    public void testNudgesRulesCanDeleteOnlyScheduledPausedOrExpired() {
        Map<String, String> map = getParam("testNudgesRulesCanDeleteOnlyScheduledPausedOrExpired");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        NudgesSystemPageWeb<?> nudgesSystemPage = new NudgesSystemPageWeb<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb =  PricingDashboardPageWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");
        String vertical = map.get("vertical");
        String nudgeName = "aut_" + CoreFunctions.randomName();
        String priority = map.get("priority");
        String funnel = map.get("funnel");
        String message = map.get("message");
        String adjustValue = map.get("adjustValue");

        step("User Log in to Panel dashboard");
        memberLoginPage.adminLoginViaCookie(email, password);

        step("User navigates to Pricing Dashboard");
        pricingDashboardPageWeb.goToPage();

        step("User navigates to Nudges System Page");
        nudgesSystemPage.goToNudgesSystemPage();

        step("User Clicks on Add Nudge button");
        nudgesSystemPage.clickAddNudgeButton();

        step("User Fill General Rule details");
        nudgesSystemPage.fillGeneralRuleSection(vertical, nudgeName, priority);

        step("User fill Targeting Action details");
        nudgesSystemPage.fillTargetActionSection(funnel, message);

        step("User fill Conditions details");
        nudgesSystemPage.fillConditionSection(adjustValue);

        step("User navigates to Summary section");
        nudgesSystemPage.goToSummarySection();

        step("User Save the Nudge Rule");
        nudgesSystemPage.clickSaveButton();

        step("user Search for the created Nudge Rule");
        nudgesSystemPage.searchNewNudgeRule(nudgeName, vertical);

        step("User verifies that New Nudge Rule is created and displayed in the list");
        Assertion.assertThat("New Nudge Rule is created and displayed in the list", nudgesSystemPage.verifyNudgeName(nudgeName), is(true));

        step("User Clicks on the Setting Button");
        nudgesSystemPage.clickSettingsButton();

        step("User Clicks on Delete Button");
        nudgesSystemPage.clickDeleteButton();

        step("user again Search for the created Nudge Rule");
        nudgesSystemPage.searchNewNudgeRule(nudgeName, vertical);

        step("User verifies that New Nudge Rule is deleted and removed from the list");
        Assertion.assertThat("New Nudge Rule is deleted and removed from the list", nudgesSystemPage.verifyNudgeRuleIsDeleted(), is(true));

        step("Log out");
        loginMemberAdmin.logout();
    }

    @Author(names = {"Manan Kumar"})
    @Module(name = "Nudges System")
    @Tribe(name = "Pricing")
    @Vertical(name = "Pricing")
    @Test
    public void testVerifyNewNudgeRuleIsCreatedSuccessfully() {
        Map<String, String> map = getParam("testVerifyNewNudgeRuleIsCreatedSuccessfully");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        NudgesSystemPageWeb<?> nudgesSystemPage = new NudgesSystemPageWeb<>(session);
        PricingDashboardPageWeb<?> pricingDashboardPageWeb =  PricingDashboardPageWeb.getInstance(session);

        String email = map.get("email");
        String password = map.get("password");
        String vertical = map.get("vertical");
        String nudgeName = "aut_" + CoreFunctions.randomName();
        String priority = map.get("priority");
        String funnel = map.get("funnel");
        String message = map.get("message");
        String adjustValue = map.get("adjustValue");

        step("User Log in to Panel dashboard");
        memberLoginPage.adminLoginViaCookie(email, password);

        step("User navigates to Pricing Dashboard");
        pricingDashboardPageWeb.goToPage();

        step("User navigates to Nudges System Page");
        nudgesSystemPage.goToNudgesSystemPage();

        step("User Clicks on Add Nudge button");
        nudgesSystemPage.clickAddNudgeButton();

        step("User Fill General Rule details");
        nudgesSystemPage.fillGeneralRuleSection(vertical, nudgeName, priority);

        step("User fill Targeting Action details");
        nudgesSystemPage.fillTargetActionSection(funnel, message);

        step("User fill Conditions details");
        nudgesSystemPage.fillConditionSection(adjustValue);

        step("User navigates to Summary section");
        nudgesSystemPage.goToSummarySection();

        step("User Save the Nudge Rule");
        nudgesSystemPage.clickSaveButton();

        step("user Search for the created Nudge Rule");
        nudgesSystemPage.searchNewNudgeRule(nudgeName, vertical);

        step("User verifies that New Nudge Rule is created and displayed in the list");
        Assertion.assertThat("New Nudge Rule is created and displayed in the list", nudgesSystemPage.verifyNudgeName(nudgeName), is(true));

        step("Restore to Precondition - Delete the created Nudge Rule");
        nudgesSystemPage.clickSettingsButton();
        nudgesSystemPage.clickDeleteButton();
        nudgesSystemPage.searchNewNudgeRule(nudgeName, vertical);

        step("User verifies that New Nudge Rule is deleted and removed from the list");
        Assertion.assertThat("New Nudge Rule is deleted and removed from the list", nudgesSystemPage.verifyNudgeRuleIsDeleted(), is(true));

        step("Log out");
        loginMemberAdmin.logout();
    }
}
