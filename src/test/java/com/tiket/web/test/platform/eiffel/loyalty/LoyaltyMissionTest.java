package com.tiket.web.test.platform.eiffel.loyalty;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.core.CoreFunctions;
import com.tiket.apiPage.LoyaltyApi;
import com.tiket.apiPage.Unm;
import com.tiket.web.WebTest;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.mission.MissionPageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.pojo.loyalty.MissionEligibilityResponse;
import com.tiket.pojo.member.RegisterNewAccountData;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.pojo.loyalty.MissionEligibilityResponse.USER_STATUS_INACTIVE;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class LoyaltyMissionTest extends WebTest {

    @Author(names = {"Arini"})
    @Module(name = "Mission")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Eiffel")
    @Test
    public void verifyMissionCenterListPageAsGuestUserWeb() {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyMissionCenterListPageAsGuestUserWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        MissionPageWeb<?> missionPage = MissionPageWeb.getInstance(session);

        step("Given a non-login user opens the app");
        homePage.goToHomePage();

        step("When user open mission center list page");
        missionPage.openMissionCenterList();

        step("Then user should be redirected to mission center list page");
        Assertion.assertThat("User should be on the mission center page", missionPage.isOnMissionCenterListPage(), is(true));

        step("And the page is set to English so the English copy can be verified");
        missionPage.switchLanguageToEnglish();

        step("And user verifies the empty state on 'Your Progress' tab");
        missionPage.clickYourProgressTab();
        Assertion.assertThat("Verify English empty state for Progress tab", missionPage.getEmptyStateText(), is("You haven't joined any mission"));

        step("When user clicks 'Browse missions' from Your Progress tab");
        missionPage.clickBrowseMissionsButton();

        step("And user verifies the empty state on 'History' tab");
        missionPage.clickHistoryTab();
        Assertion.assertThat("Verify English empty state for History tab", missionPage.getEmptyStateText(), is("No completed missions yet"));

        step("When user clicks 'Browse missions' from History tab");
        missionPage.clickBrowseMissionsButton();

        step("Then user should see active missions available in the 'Other Missions' tab");
        Assertion.assertThat("Active missions should be visible in the list", missionPage.isAnyActiveMissionAvailable(), is(true));

        step("When user clicks the 'Join mission here' button on an active mission");
        missionPage.clickFirstActiveMission();

        step("Then user should be redirected to the mission detail page");
        Assertion.assertThat("User should be on the mission detail page", missionPage.isOnMissionDetailPage(), is(true));

        step("Verify mission center list page using Bahasa Indonesia");
        missionPage.openMissionCenterList();
        missionPage.switchLanguageToIndonesian();

        step("Then user should be redirected to mission center list page");
        Assertion.assertThat("User should be on the mission center page", missionPage.isOnMissionCenterListPage(), is(true));

        step("Then user verifies the localized empty state on 'Progresmu' tab");
        missionPage.clickYourProgressTab();
        Assertion.assertThat("Verify Indonesian empty state for Progress tab", missionPage.getEmptyStateText(), is("Kamu belum ikutan misi apa pun"));

        step("And user verifies the localized empty state on 'Riwayat' tab");
        missionPage.clickHistoryTab();
        Assertion.assertThat("Verify Indonesian empty state for History tab", missionPage.getEmptyStateText(), is("Belum ada misi yang selesai"));

        step("Then user verifies the localized on 'Misi Lainnya' tab");
        missionPage.clickOtherMissionsTab();

        step("Then user should see active missions available in the 'Misi Lainnya' tab");
        Assertion.assertThat("Active missions should be visible in the list", missionPage.isAnyActiveMissionAvailable(), is(true));

        step("When user clicks the 'Ikuti misi di sini' button on an active mission");
        missionPage.clickFirstActiveMission();

        step("Then user should be redirected to the mission detail page");
        Assertion.assertThat("User should be on the mission detail page", missionPage.isOnMissionDetailPage(), is(true));
    }

    @Author(names = {"Arini"})
    @Module(name = "Mission")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Eiffel")
    @Test
    public void verifyNewUserJoinMissionFromListWeb() throws Exception {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyNewUserJoinMissionFromListWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        MissionPageWeb<?> missionPage = new MissionPageWeb<>(session);
        Unm<?> unm = new Unm<>(session);

        RegisterNewAccountData accountData = null;

        try {
            step("1. Create a fresh account via API and Log in");
            accountData = unm.registerNewAccountViaApiUNM();
            homePage.goToHomePage();
            loginPage.loginWithSpecificAccountViaCookie(accountData.email, accountData.password);

            homePage.selectCurrency("IDR");

            step("2. Open the Mission Center List Page");
            missionPage.openMissionCenterList();
            Assertion.assertThat("User should be on the mission center list page", missionPage.isOnMissionCenterListPage(), is(true));

            step("3. Navigate to 'Other Missions', verify active missions, and join the first one");
            missionPage.clickOtherMissionsTab();
            Assertion.assertThat("Active missions should be visible to a new user", missionPage.isAnyActiveMissionAvailable(), is(true));

            String enrolledMissionTitle = missionPage.clickFirstActiveMission();

            step("4. On Mission Detail page, verify Success Modal and dismiss it");
            Assertion.assertThat("Success Enroll modal should be visible", missionPage.isSuccessEnrollModalVisible(), is(true));
            missionPage.clickSeeTheMissionToCloseModal();

            step("5. Return to List Page and verify the mission moved to 'Your Progress'");
            missionPage.navigateBackToListPage();
            missionPage.clickYourProgressTab();
            Assertion.assertThat("The mission '" + enrolledMissionTitle + "' should appear with 'Check your progress' button",
                    missionPage.isSpecificEnrolledMissionCardPresent(enrolledMissionTitle), is(true));

        } finally {
            step("Cleanup: log out and delete the temporary account");
            if (accountData != null) {
                homePage.logoutAndDeleteTemporaryAccount(accountData);
            }
        }
    }

    @Author(names = {"Yanuar"})
    @Module(name = "Mission")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Eiffel")
    @Test
    public void verifyFreeStampClaimedWhenJoinMissionDirectlyFromBEWeb() throws Exception {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyFreeStampClaimedWhenJoinMissionDirectlyFromBEWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        MissionPageWeb<?> missionPage = MissionPageWeb.getInstance(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        Unm<?> unm = new Unm<>(session);

        String missionSlug = param.get("missionSlug");
        String missionId = param.get("missionId");
        int expectedFreeStamps = Integer.parseInt(param.get("expectedFreeStamps"));

        RegisterNewAccountData accountData = null;

        try {
            step("Given a fresh account is created via API");
            accountData = unm.registerNewAccountViaApiUNM();

            step("And the account is mapped to the segment of the free stamp mission '" + missionSlug + "'");
            loyaltyApi.mapUserToMissionDynamicSegment(missionId, accountData.email);

            step("And the mission eligibility API shows the user is eligible but has not joined yet");
            MissionEligibilityResponse eligibilityBeforeJoin =
                    loyaltyApi.waitForMissionUserStatus(missionId, accountData.email, USER_STATUS_INACTIVE);
            Assertion.assertThat("A user mapped to the mission segment who has not joined yet should be INACTIVE",
                    eligibilityBeforeJoin.getUserStatus(), is(USER_STATUS_INACTIVE));

            step("When the user joins the mission via the backend join API");
            loyaltyApi.joinMission(missionSlug, accountData.email);

            step("Then the mission eligibility API credits the free stamp to the user");
            MissionEligibilityResponse eligibilityAfterJoin =
                    loyaltyApi.waitForMissionProgress(missionId, accountData.email, expectedFreeStamps);
            Assertion.assertThat("Joining the mission should credit the free stamp to the user's progress",
                    eligibilityAfterJoin.getCurrentProgress(), is(expectedFreeStamps));
            Assertion.assertThat("A user who has joined the mission should no longer be INACTIVE",
                    eligibilityAfterJoin.getUserStatus(), not(is(USER_STATUS_INACTIVE)));

            step("When the user logs in on the web");
            loginPage.loginWithSpecificAccountViaCookie(accountData.email, accountData.password);
            Assertion.assertThat("The user should be logged in before opening the mission page",
                    loginPage.checkIfLoggedIn(), is(true));
            homePage.selectCurrency("IDR");

            step("And the user opens the mission page");
            missionPage.openMission(missionSlug);

            step("Then the user should have joined the mission");
            Assertion.assertThat("The stamp summary should be visible for a user who has joined the mission",
                    missionPage.isStampSummaryVisible(), is(true));
            Assertion.assertThat("The join CTA should not be shown for a user who has joined the mission",
                    missionPage.isJoinMissionRequired(), is(false));

            step("And the free stamp is shown as collected on the mission page");
            int collectedStamps = missionPage.waitForCollectedStampCount(expectedFreeStamps);
            Assertion.assertThat("The mission page should show the free stamp as collected ('"
                            + missionPage.getStampSummaryText() + "')",
                    collectedStamps, is(expectedFreeStamps));
        } finally {
            step("Cleanup: log out and delete the temporary account");
            if (accountData != null) {
                homePage.logoutAndDeleteTemporaryAccount(accountData);
            }
        }
    }

    @Author(names = {"Nasha"})
    @Module(name = "Mission")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Eiffel")
    @Test
    public void verifyClickMissionHistoryCtaWhenUserNotJoinedMission() throws Exception {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyClickMissionHistoryCtaWhenUserNotJoinedMission");
        BaseTest.paramsThreadLocal.set(param);

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        MissionPageWeb<?> missionPage = MissionPageWeb.getInstance(session);

        step("Given the user logs in with the account from the test data");
        loginPage.loginWithSpecificAccountViaCookie(param.get("email"), param.get("password"));
        Assertion.assertThat("The user should be logged in before opening the mission page", loginPage.checkIfLoggedIn(), is(true));

        step("When the user opens the mission center list page");
        missionPage.openMissionCenterList();
        Assertion.assertThat("User should be on the mission center list page", missionPage.isOnMissionCenterListPage(), is(true));

        step("And the user opens the 'Other Missions' tab with at least one mission not joined yet");
        missionPage.clickOtherMissionsTab();
        Assertion.assertThat("Active missions should be visible in the list", missionPage.isAnyActiveMissionAvailable(), is(true));

        step("When the user clicks the mission card body that has 'Join mission here' button");
        missionPage.clickFirstActiveMissionCard();
        Assertion.assertThat("User should be on the mission detail page", missionPage.isOnMissionDetailPage(), is(true));
        missionPage.dismissOnboardingPopupIfPresent();

        step("When the user clicks the mission history CTA in the header");
        missionPage.clickMissionHistoryCta();

        step("Then the user should be navigated to the Mission History page");
        Assertion.assertThat("User should be navigated to the Mission History page", missionPage.isOnMissionHistoryPage(), is(true));

        step("And the enrollment bottom sheet to join a mission is displayed");
        Assertion.assertThat("The enrollment bottom sheet should be displayed for a user who has not joined a mission", missionPage.isJoinMissionDialogVisible(), is(true));
        Assertion.assertThat("The enrollment bottom sheet title is visible", missionPage.isJoinMissionDialogTitleDisplayed(), is(true));
        Assertion.assertThat("The enrollment bottom sheet description is visible", missionPage.isJoinMissionDialogDescriptionDisplayed(), is(true));
        Assertion.assertThat("The enrollment bottom sheet should have the join mission button", missionPage.isJoinMissionDialogCtaDisplayed(), is(true));
    }

    @Author(names = {"Nasha"})
    @Module(name = "Mission")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Eiffel")
    @Test
    public void verifyMissionOrderHistoryRedirectsToYourOrder() throws Exception {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyMissionOrderHistoryRedirectsToYourOrder");
        BaseTest.paramsThreadLocal.set(param);

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        MissionPageWeb<?> missionPage = MissionPageWeb.getInstance(session);

        step("Given a user who has joined a mission and has mission order history is logged in");
        loginPage.loginWithSpecificAccountViaCookie(param.get("email"), param.get("password"));
        Assertion.assertThat("The user should be logged in before opening the mission page", loginPage.checkIfLoggedIn(), is(true));

        step("When the user opens the mission center list page");
        missionPage.openMissionCenterList();
        Assertion.assertThat("User should be on the mission center list page", missionPage.isOnMissionCenterListPage(), is(true));

        step("And the user opens the History tab");
        missionPage.clickHistoryTab();

        step("And the user opens the first mission in the History tab");
        missionPage.clickFirstHistoryMissionCard();
        Assertion.assertThat("User should be on the mission detail page", missionPage.isOnMissionDetailPage(), is(true));

        missionPage.dismissOnboardingPopupIfPresent();

        step("When the user opens the mission order history");
        missionPage.clickMissionHistoryCta();
        Assertion.assertThat("User should be on the Mission History page", missionPage.isOnMissionHistoryPage(), is(true));

        step("And the user clicks the first mission order history card");
        String orderId = missionPage.getFirstMissionHistoryOrderId();
        missionPage.clickFirstMissionHistoryOrderCard();

        step("Then the user is redirected to the your order page of that same order");
        String yourOrderUrl = missionPage.waitForYourOrderDetailUrl();
        Assertion.assertThat("Clicking the mission order history card should open the your order detail page", yourOrderUrl, containsString("/yourorder/detail"));
        Assertion.assertThat("The your order page should open the order shown on the mission history card", CoreFunctions.getQueryParameterValue(yourOrderUrl, "orderId"), is(orderId));
    }
}
