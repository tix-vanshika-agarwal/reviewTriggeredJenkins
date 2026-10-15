package com.tiket.web.dashboard.test.pricing_dashboard;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberPage;
import com.tiket.web.dashboard.page.member_dashboard.PricingDashboardHome;
import com.tiket.web.dashboard.page.pricing_dashboard.PricingDashboardPageWeb;

import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class PricingDashboardTest extends BaseTest {
    @Author(names = {"Swapan"})
    @Module(name = "Pricing")
    @Tribe(name = "Pricing")
    @Vertical(name = "Pricing")
    @Test
    public void testPricingDashboardLogin() {
        Map<String, String> map = getParam("testPricingDashboardLogin");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        MemberPage<?> memberPage = new MemberPage<>(session);
        PricingDashboardHome<?> pricingDashboardHome = new PricingDashboardHome<>(session);
        String username = map.get("username");
        String password = map.get("password");

        step("Go to Member Login Page");
        memberLoginPage.gotoMemberLoginPage();

        step("Input Username");
        memberLoginPage.inputLogin(username, password);

        step("Click Login Button");
        memberLoginPage.clickOnButton();
        boolean isMemberDashboardLoaded = memberPage.isMemberDashboardLoaded();

        step("Click to open Pricing Dashboard");
        memberPage.goToPricingDashboard(username, password);

        Assertion.assertThat("Member page is loaded", isMemberDashboardLoaded, is(true));
        Assertion.assertThat("Pricing Dashboard is loaded", pricingDashboardHome.isPricingDashboardLoaded(), is(true));
    }

    @Author(names = {"Ankit Raj"})
    @Module(name = "Nudges System")
    @Tribe(name = "Pricing Dashboard")
    @Vertical(name = "Pricing")
    @Test
    public void testCreateRuleForBookingFormWithAllTimeScheduledAndOngoingStatus() {
        Map<String, String> map = getParam("testCreateRuleForBookingFormWithAllTimeScheduledAndOngoingStatus");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        PricingDashboardPageWeb<?> pricingDashboardPageWeb = new PricingDashboardPageWeb<>(session);
        LoginMemberAdmin<?> memberLoginAdmin = new LoginMemberAdmin<>(session);
        String email = map.get("email");
        String password = map.get("password");
        String vertical = map.get("vertical");
        String nudgeName = "Nudge" + System.currentTimeMillis();
        String layer = map.get("layer");
        String priority = String.valueOf((int)(Math.random() * 1000));
        String campaignId = "Campaign" + System.currentTimeMillis();
        String message = "Message" + System.currentTimeMillis();
        String icon = map.get("icon");
        String iconColor = map.get("iconColor");
        String style = map.get("style");
        String color = map.get("color");
        String size = map.get("size");
        String storeId = map.get("storeId");

        step("Login via cookie injection");
        memberLoginAdmin.loginViaCookie(email, password);

        step("Click to open Pricing Dashboard");
        memberLoginAdmin.goToPricingDashboard();

        step("Go to Nudges System");
        pricingDashboardPageWeb.clickNudgesSystemMenu();

        step("Click on add nudge");
        pricingDashboardPageWeb.clickAddNudgeButton();

        step("Select Vertical Distribution Channel");
        pricingDashboardPageWeb.selectVerticalDistributionChannel(vertical);

        step("Input Nudge Name");
        pricingDashboardPageWeb.inputNudgeName(nudgeName);

        step("Select Layer");
        pricingDashboardPageWeb.selectLayer(layer);

        step("Input Priority");
        pricingDashboardPageWeb.inputPriority(priority);

        step("Click on Android Platform");
        pricingDashboardPageWeb.clickAndroidPlatform();

        step("Click Target & Action button to proceed");
        pricingDashboardPageWeb.clickTargetAndActionButton();

        step("Fill the Target & Action form");
        pricingDashboardPageWeb.fillTargetAndActionForm();

        step("Fill the Style form");
        pricingDashboardPageWeb.fillStyleForm(icon, iconColor, style, color, size);

        step("Input Campaign ID");
        pricingDashboardPageWeb.inputCampaignId(campaignId);

        step("Enter message");
        pricingDashboardPageWeb.enterMessage(message);

        step("click Conditions & Schedule button");
        pricingDashboardPageWeb.clickConditionsAndScheduleButton();

        step("Fill Conditions & Schedule form");
        pricingDashboardPageWeb.fillConditionsAndScheduleForm(storeId);

        step("click Summary button");
        pricingDashboardPageWeb.clickSummaryButton();

        step("Verify Status is On Going");
        boolean isOnGoingStatusChecked = pricingDashboardPageWeb.isOnGoingStatusChecked();
        Assertion.assertThat("On Going Status is checked", isOnGoingStatusChecked, is(true));

        step("Verify Schedule is All Time");
        boolean isAllTimeChecked = pricingDashboardPageWeb.isAllTimeChecked();
        Assertion.assertThat("All Time is checked", isAllTimeChecked, is(true));

        step("click Save button");
        pricingDashboardPageWeb.clickSaveButton();

        step("Click on Save Confirm Modal Yes button");
        pricingDashboardPageWeb.clickSaveConfirmModalYesButton();

        step("Select Vertical Distribution Channel");
        pricingDashboardPageWeb.selectVerticalDistributionChannel(vertical);

        step("Input Nudge Name");
        pricingDashboardPageWeb.inputSearchNudgeName(nudgeName);
        Assertion.assertThat("Nudge Name is displayed", pricingDashboardPageWeb.isNudgeNameDisplayed(nudgeName), is(true));

        step("Clear Search Nudge Name");
        pricingDashboardPageWeb.clearSearchNudgeName(nudgeName);

        step("Click on Settings button");
        pricingDashboardPageWeb.clickSettingsButton();

        step("Click on Edit button");
        pricingDashboardPageWeb.clickEditButton();

        step("Verify Status is On Going");
        isOnGoingStatusChecked = pricingDashboardPageWeb.isOnGoingStatusChecked();
        Assertion.assertThat("On Going Status is checked", isOnGoingStatusChecked, is(true));

        step("Verify Schedule is All Time");
        isAllTimeChecked = pricingDashboardPageWeb.isAllTimeChecked();
        Assertion.assertThat("All Time is checked", isAllTimeChecked, is(true));

        step("Click on Edit Four button");
        pricingDashboardPageWeb.clickEditFourButton();

        step("Click on status button");
        pricingDashboardPageWeb.clickOnPausedStatus();

        step("click Summary button");
        pricingDashboardPageWeb.clickSummaryButton();

        step("Verify Status is Paused");
        boolean isPausedStatusChecked = pricingDashboardPageWeb.isPausedStatusChecked();
        Assertion.assertThat("Paused Status is checked", isPausedStatusChecked, is(true));

        step("Verify Schedule is All Time");
        isAllTimeChecked = pricingDashboardPageWeb.isAllTimeChecked();
        Assertion.assertThat("All Time is checked", isAllTimeChecked, is(true));

        step("click Save button");
        pricingDashboardPageWeb.clickSaveButton();

        step("Click on Save Confirm Modal Yes button");
        pricingDashboardPageWeb.clickSaveConfirmModalYesButton();

        step("Click on Settings button");
        pricingDashboardPageWeb.clickSettingsButton();

        step("Click on Delete button");
        pricingDashboardPageWeb.clickDeleteButton();

        step("Click on Delete Confirm Modal Yes button");
        pricingDashboardPageWeb.clickDeleteConfirmModalYesButton();

        step("Input Nudge Name");
        pricingDashboardPageWeb.inputSearchNudgeName(nudgeName);
        Assertion.assertThat("Nudge Name is displayed", pricingDashboardPageWeb.isNudgeNameDisplayed(nudgeName), is(false));

        step("Log out");
        memberLoginAdmin.logout();
    }

    @Author(names = {"Ankit Raj"})
    @Module(name = "Nudges System")
    @Tribe(name = "Pricing Dashboard")
    @Vertical(name = "Pricing")
    @Test
    public void testCreateRuleForBookingFormWithAllTimeScheduledAndPausedStatus() {
        Map<String, String> map = getParam("testCreateRuleForBookingFormWithAllTimeScheduledAndPausedStatus");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        PricingDashboardPageWeb<?> pricingDashboardPageWeb = new PricingDashboardPageWeb<>(session);
        LoginMemberAdmin<?> memberLoginAdmin = new LoginMemberAdmin<>(session);
        String email = map.get("email");
        String password = map.get("password");
        String vertical = map.get("vertical");
        String nudgeName = "Nudge" + System.currentTimeMillis();
        String layer = map.get("layer");
        String priority = String.valueOf((int)(Math.random() * 1000));
        String campaignId = "Campaign" + System.currentTimeMillis();
        String message = "Message" + System.currentTimeMillis();
        String icon = map.get("icon");
        String iconColor = map.get("iconColor");
        String style = map.get("style");
        String color = map.get("color");
        String size = map.get("size");
        String storeId = map.get("storeId");

        step("Login via cookie injection");
        memberLoginAdmin.loginViaCookie(email, password);

        step("Click to open Pricing Dashboard");
        memberLoginAdmin.goToPricingDashboard();

        step("Go to Nudges System");
        pricingDashboardPageWeb.clickNudgesSystemMenu();

        step("Click on add nudge");
        pricingDashboardPageWeb.clickAddNudgeButton();

        step("Select Vertical Distribution Channel");
        pricingDashboardPageWeb.selectVerticalDistributionChannel(vertical);

        step("Input Nudge Name");
        pricingDashboardPageWeb.inputNudgeName(nudgeName);

        step("Select Layer");
        pricingDashboardPageWeb.selectLayer(layer);

        step("Input Priority");
        pricingDashboardPageWeb.inputPriority(priority);

        step("Click on Android Platform");
        pricingDashboardPageWeb.clickAndroidPlatform();

        step("Click Target & Action button to proceed");
        pricingDashboardPageWeb.clickTargetAndActionButton();

        step("Fill the Target & Action form");
        pricingDashboardPageWeb.fillTargetAndActionForm();

        step("Fill the Style form");
        pricingDashboardPageWeb.fillStyleForm(icon, iconColor, style, color, size);

        step("Input Campaign ID");
        pricingDashboardPageWeb.inputCampaignId(campaignId);

        step("Enter message");
        pricingDashboardPageWeb.enterMessage(message);

        step("click Conditions & Schedule button");
        pricingDashboardPageWeb.clickConditionsAndScheduleButton();

        step("Fill Conditions & Schedule form");
        pricingDashboardPageWeb.fillConditionsAndScheduleFormWithPausedStatus(storeId);

        step("click Summary button");
        pricingDashboardPageWeb.clickSummaryButton();

        step("Verify Status is Paused");
        boolean isPausedStatusChecked = pricingDashboardPageWeb.isPausedStatusChecked();
        Assertion.assertThat("Paused Status is checked", isPausedStatusChecked, is(true));

        step("Verify Schedule is All Time");
        boolean isAllTimeChecked = pricingDashboardPageWeb.isAllTimeChecked();
        Assertion.assertThat("All Time is checked", isAllTimeChecked, is(true));

        step("click Save button");
        pricingDashboardPageWeb.clickSaveButton();

        step("Click on Save Confirm Modal Yes button");
        pricingDashboardPageWeb.clickSaveConfirmModalYesButton();

        step("Select Vertical Distribution Channel");
        pricingDashboardPageWeb.selectVerticalDistributionChannel(vertical);

        step("Input Nudge Name");
        pricingDashboardPageWeb.inputSearchNudgeName(nudgeName);
        Assertion.assertThat("Nudge Name is displayed", pricingDashboardPageWeb.isNudgeNameDisplayed(nudgeName), is(true));

        step("Click on Settings button");
        pricingDashboardPageWeb.clickSettingsButton();

        step("Click on Delete button");
        pricingDashboardPageWeb.clickDeleteButton();

        step("Click on Delete Confirm Modal Yes button");
        pricingDashboardPageWeb.clickDeleteConfirmModalYesButton();

        step("Input Nudge Name");
        pricingDashboardPageWeb.inputSearchNudgeName(nudgeName);
        Assertion.assertThat("Nudge Name is not present", pricingDashboardPageWeb.isNudgeNameDisplayed(nudgeName), is(false));

        step("Log out");
        memberLoginAdmin.logout();
    }

    @Author(names = {"Ankit Raj"})
    @Module(name = "Nudges System")
    @Tribe(name = "Pricing Dashboard")
    @Vertical(name = "Pricing")
    @Test
    public void testCreateRuleForBookingFormWithScheduledAndPausedStatus() {
        Map<String, String> map = getParam("testCreateRuleForBookingFormWithScheduledAndPausedStatus");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        PricingDashboardPageWeb<?> pricingDashboardPageWeb = new PricingDashboardPageWeb<>(session);
        LoginMemberAdmin<?> memberLoginAdmin = new LoginMemberAdmin<>(session);
        String email = map.get("email");
        String password = map.get("password");
        String vertical = map.get("vertical");
        String nudgeName = "Nudge" + System.currentTimeMillis();
        String layer = map.get("layer");
        String priority = String.valueOf((int)(Math.random() * 1000));
        String campaignId = "Campaign" + System.currentTimeMillis();
        String message = "Message" + System.currentTimeMillis();
        String icon = map.get("icon");
        String iconColor = map.get("iconColor");
        String style = map.get("style");
        String color = map.get("color");
        String size = map.get("size");
        String storeId = map.get("storeId");

        step("Login via cookie injection");
        memberLoginAdmin.loginViaCookie(email, password);

        step("Click to open Pricing Dashboard");
        memberLoginAdmin.goToPricingDashboard();

        step("Go to Nudges System");
        pricingDashboardPageWeb.clickNudgesSystemMenu();

        step("Click on add nudge");
        pricingDashboardPageWeb.clickAddNudgeButton();

        step("Select Vertical Distribution Channel");
        pricingDashboardPageWeb.selectVerticalDistributionChannel(vertical);

        step("Input Nudge Name");
        pricingDashboardPageWeb.inputNudgeName(nudgeName);

        step("Select Layer");
        pricingDashboardPageWeb.selectLayer(layer);

        step("Input Priority");
        pricingDashboardPageWeb.inputPriority(priority);

        step("Click on Android Platform");
        pricingDashboardPageWeb.clickAndroidPlatform();

        step("Click Target & Action button to proceed");
        pricingDashboardPageWeb.clickTargetAndActionButton();

        step("Fill the Target & Action form");
        pricingDashboardPageWeb.fillTargetAndActionForm();

        step("Fill the Style form");
        pricingDashboardPageWeb.fillStyleForm(icon, iconColor, style, color, size);

        step("Input Campaign ID");
        pricingDashboardPageWeb.inputCampaignId(campaignId);

        step("Enter message");
        pricingDashboardPageWeb.enterMessage(message);

        step("click Conditions & Schedule button");
        pricingDashboardPageWeb.clickConditionsAndScheduleButton();

        step("Click on Schedule button");
        pricingDashboardPageWeb.clickOnSchedule();

        step("Select Today");
        pricingDashboardPageWeb.selectToday();

        step("Input Dates");
        pricingDashboardPageWeb.selectLastDayOfMonth();

        step("Fill Conditions & Schedule form");
        pricingDashboardPageWeb.fillConditionsAndScheduleFormWithPausedStatus(storeId);

        step("click Summary button");
        pricingDashboardPageWeb.clickSummaryButton();

        step("Verify Status is Paused");
        boolean isPausedStatusChecked = pricingDashboardPageWeb.isPausedStatusChecked();
        Assertion.assertThat("Paused Status is checked", isPausedStatusChecked, is(true));

        step("Verify Schedule is Scheduled");
        boolean isAllTimeChecked = pricingDashboardPageWeb.isAllTimeChecked();
        Assertion.assertThat("Verify Schedule is Scheduled", isAllTimeChecked, is(false));

        step("click Save button");
        pricingDashboardPageWeb.clickSaveButton();

        step("Click on Save Confirm Modal Yes button");
        pricingDashboardPageWeb.clickSaveConfirmModalYesButton();

        step("Select Vertical Distribution Channel");
        pricingDashboardPageWeb.selectVerticalDistributionChannel(vertical);

        step("Input Nudge Name");
        pricingDashboardPageWeb.inputSearchNudgeName(nudgeName);
        Assertion.assertThat("Nudge Name is displayed", pricingDashboardPageWeb.isNudgeNameDisplayed(nudgeName), is(true));

        step("Click on Settings button");
        pricingDashboardPageWeb.clickSettingsButton();

        step("Click on Delete button");
        pricingDashboardPageWeb.clickDeleteButton();

        step("Click on Delete Confirm Modal Yes button");
        pricingDashboardPageWeb.clickDeleteConfirmModalYesButton();

        step("Input Nudge Name");
        pricingDashboardPageWeb.inputSearchNudgeName(nudgeName);
        Assertion.assertThat("Nudge Name is not present", pricingDashboardPageWeb.isNudgeNameDisplayed(nudgeName), is(false));

        step("Log out");
        memberLoginAdmin.logout();
    }

    @Author(names = {"Ankit Raj"})
    @Module(name = "Nudges System")
    @Tribe(name = "Pricing Dashboard")
    @Vertical(name = "Pricing")
    @Test
    public void testCreateRuleForBookingFormWithScheduledAndOngoingStatus() {
        Map<String, String> map = getParam("testCreateRuleForBookingFormWithScheduledAndOngoingStatus");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        PricingDashboardPageWeb<?> pricingDashboardPageWeb = new PricingDashboardPageWeb<>(session);
        LoginMemberAdmin<?> memberLoginAdmin = new LoginMemberAdmin<>(session);
        String email = map.get("email");
        String password = map.get("password");
        String vertical = map.get("vertical");
        String nudgeName = "Nudge" + System.currentTimeMillis();
        String layer = map.get("layer");
        String priority = String.valueOf((int)(Math.random() * 1000));
        String campaignId = "Campaign" + System.currentTimeMillis();
        String message = "Message" + System.currentTimeMillis();
        String icon = map.get("icon");
        String iconColor = map.get("iconColor");
        String style = map.get("style");
        String color = map.get("color");
        String size = map.get("size");
        String storeId = map.get("storeId");

        step("Login via cookie injection");
        memberLoginAdmin.loginViaCookie(email, password);

        step("Click to open Pricing Dashboard");
        memberLoginAdmin.goToPricingDashboard();

        step("Go to Nudges System");
        pricingDashboardPageWeb.clickNudgesSystemMenu();

        step("Click on add nudge");
        pricingDashboardPageWeb.clickAddNudgeButton();

        step("Select Vertical Distribution Channel");
        pricingDashboardPageWeb.selectVerticalDistributionChannel(vertical);

        step("Input Nudge Name");
        pricingDashboardPageWeb.inputNudgeName(nudgeName);

        step("Select Layer");
        pricingDashboardPageWeb.selectLayer(layer);

        step("Input Priority");
        pricingDashboardPageWeb.inputPriority(priority);

        step("Click on Android Platform");
        pricingDashboardPageWeb.clickAndroidPlatform();

        step("Click Target & Action button to proceed");
        pricingDashboardPageWeb.clickTargetAndActionButton();

        step("Fill the Target & Action form");
        pricingDashboardPageWeb.fillTargetAndActionForm();

        step("Fill the Style form");
        pricingDashboardPageWeb.fillStyleForm(icon, iconColor, style, color, size);

        step("Input Campaign ID");
        pricingDashboardPageWeb.inputCampaignId(campaignId);

        step("Enter message");
        pricingDashboardPageWeb.enterMessage(message);

        step("click Conditions & Schedule button");
        pricingDashboardPageWeb.clickConditionsAndScheduleButton();

        step("Click on Schedule button");
        pricingDashboardPageWeb.clickOnSchedule();

        step("Select Today");
        pricingDashboardPageWeb.selectToday();

        step("Input Dates");
        pricingDashboardPageWeb.selectLastDayOfMonth();

        step("Fill Conditions & Schedule form");
        pricingDashboardPageWeb.fillConditionsAndScheduleForm(storeId);

        step("click Summary button");
        pricingDashboardPageWeb.clickSummaryButton();

        step("Verify Status is On Going");
        boolean isOnGoingStatusChecked = pricingDashboardPageWeb.isOnGoingStatusChecked();
        Assertion.assertThat("On Going Status is checked", isOnGoingStatusChecked, is(true));

        step("Verify Schedule is Scheduled");
        boolean isAllTimeChecked = pricingDashboardPageWeb.isAllTimeChecked();
        Assertion.assertThat("Verify Schedule is Scheduled", isAllTimeChecked, is(false));

        step("click Save button");
        pricingDashboardPageWeb.clickSaveButton();

        step("Click on Save Confirm Modal Yes button");
        pricingDashboardPageWeb.clickSaveConfirmModalYesButton();

        step("Select Vertical Distribution Channel");
        pricingDashboardPageWeb.selectVerticalDistributionChannel(vertical);

        step("Input Nudge Name");
        pricingDashboardPageWeb.inputSearchNudgeName(nudgeName);
        Assertion.assertThat("Nudge Name is displayed", pricingDashboardPageWeb.isNudgeNameDisplayed(nudgeName), is(true));

        step("Clear Search Nudge Name");
        pricingDashboardPageWeb.clearSearchNudgeName(nudgeName);

        step("Click on Settings button");
        pricingDashboardPageWeb.clickSettingsButton();

        step("Click on Edit button");
        pricingDashboardPageWeb.clickEditButton();

        step("Verify Status is On Going");
        isOnGoingStatusChecked = pricingDashboardPageWeb.isOnGoingStatusChecked();
        Assertion.assertThat("On Going Status is checked", isOnGoingStatusChecked, is(true));

        step("Verify Schedule is Scheduled");
        isAllTimeChecked = pricingDashboardPageWeb.isAllTimeChecked();
        Assertion.assertThat("Verify Schedule is Scheduled", isAllTimeChecked, is(false));

        step("Click on Edit Four button");
        pricingDashboardPageWeb.clickEditFourButton();

        step("Click on status button");
        pricingDashboardPageWeb.clickOnPausedStatus();

        step("click Summary button");
        pricingDashboardPageWeb.clickSummaryButton();

        step("Verify Status is Paused");
        boolean isPausedStatusChecked = pricingDashboardPageWeb.isPausedStatusChecked();
        Assertion.assertThat("Paused Status is checked", isPausedStatusChecked, is(true));

        step("Verify Schedule is Scheduled");
        isAllTimeChecked = pricingDashboardPageWeb.isAllTimeChecked();
        Assertion.assertThat("Verify Schedule is Scheduled", isAllTimeChecked, is(false));

        step("click Save button");
        pricingDashboardPageWeb.clickSaveButton();

        step("Click on Save Confirm Modal Yes button");
        pricingDashboardPageWeb.clickSaveConfirmModalYesButton();

        step("Click on Settings button");
        pricingDashboardPageWeb.clickSettingsButton();

        step("Click on Delete button");
        pricingDashboardPageWeb.clickDeleteButton();

        step("Click on Delete Confirm Modal Yes button");
        pricingDashboardPageWeb.clickDeleteConfirmModalYesButton();

        step("Input Nudge Name");
        pricingDashboardPageWeb.inputSearchNudgeName(nudgeName);
        Assertion.assertThat("Nudge Name is not present", pricingDashboardPageWeb.isNudgeNameDisplayed(nudgeName), is(false));

        step("Log out");
        memberLoginAdmin.logout();
    }
}