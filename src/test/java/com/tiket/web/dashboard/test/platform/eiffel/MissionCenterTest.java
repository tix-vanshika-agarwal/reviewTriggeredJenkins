package com.tiket.web.dashboard.test.platform.eiffel;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.LoyaltyApi;
import com.tiket.apiPage.LoyaltyApi.MissionBannerImages;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.web.WebTest;
import com.tiket.web.dashboard.page.platform.eiffel.loyalty.MissionCenterPage;
import com.tiket.web.dashboard.page.platform.eiffel.loyalty.MissionCenterPage.HeaderImages;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static com.tiket.web.dashboard.page.platform.eiffel.loyalty.MissionCenterPage.EDIT_MODE_BANNER;
import static com.tiket.web.dashboard.page.platform.eiffel.loyalty.MissionCenterPage.SEQUENCE_COLUMNS;
import static com.tiket.web.dashboard.page.platform.eiffel.loyalty.MissionCenterPage.SEQUENCE_NOTE;
import static com.tiket.web.dashboard.page.platform.eiffel.loyalty.MissionCenterPage.SEQUENCE_TITLE;
import static org.hamcrest.core.Is.is;

public class MissionCenterTest extends WebTest {

    @Author(names = { "Yanuar" })
    @Module(name = "MissionConfig")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testMissionCenterSequenceReorderAppliedOnMissionList() {
        Session<?> session = WebTest.sessionThreadLocal.get();
        Map<String, String> testData = getParam("testMissionCenterSequenceReorderAppliedOnMissionList");
        WebTest.paramsThreadLocal.set(testData);

        String email = testData.get("email");
        String password = testData.get("password");
        int sourceOrder = Integer.parseInt(testData.get("sourceOrder"));
        int targetOrder = Integer.parseInt(testData.get("targetOrder"));
        String successMessage = testData.get("successMessage");

        MissionCenterPage<?> missionCenterPage = new MissionCenterPage<>(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);

        step("Given user opens the Mission Center page in the Loyalty admin panel");
        missionCenterPage.goToMissionCenterPage(email, password);

        step("Then the sequence is listed read-only under the Order, Header, Name and Slug columns");
        Assertion.assertThat("The sequence card is titled", missionCenterPage.getSequenceTitle(), is(SEQUENCE_TITLE));
        Assertion.assertThat("The sequence card explains what it manages", missionCenterPage.getSequenceNote(), is(SEQUENCE_NOTE));
        Assertion.assertThat("The sequence table has its four columns", missionCenterPage.getColumnHeaders(), is(SEQUENCE_COLUMNS));
        Assertion.assertThat("View mode offers no drag handle to reorder with", missionCenterPage.getVisibleDragHandleCount(), is(0));
        Assertion.assertThat("View mode hides the drag banner", missionCenterPage.isEditModeBannerDisplayed(), is(false));

        step("When user records the current sequence and clicks Edit");
        List<String> sequenceBeforeMove = missionCenterPage.getSequenceSlugs();
        missionCenterPage.clickEdit();

        step("Then edit mode adds the drag banner and a drag handle on every row");
        Assertion.assertThat("Edit mode reveals the drag banner", missionCenterPage.isEditModeBannerDisplayed(), is(true));
        Assertion.assertThat("The drag banner tells the user how to reorder", missionCenterPage.getEditModeBannerText(), is(EDIT_MODE_BANNER));
        Assertion.assertThat("Every mission row can be dragged", missionCenterPage.getVisibleDragHandleCount(), is(sequenceBeforeMove.size()));

        step("When user moves the mission at Order " + sourceOrder + " above the mission at Order " + targetOrder);
        missionCenterPage.moveMissionToOrder(sourceOrder, targetOrder);

        step("Then the moved mission lands at Order " + targetOrder + " and the rest of the sequence shifts with no gaps");
        missionCenterPage.verifySequenceAfterMove(sequenceBeforeMove, sourceOrder, targetOrder);

        step("When user saves the new sequence");
        missionCenterPage.clickSave();

        step("Then a success snackbar confirms the save and the sequence goes back to read-only");
        missionCenterPage.verifySuccessSnackbarDisplayed(successMessage);
        Assertion.assertThat("Saving returns the sequence to read-only", missionCenterPage.getVisibleDragHandleCount(), is(0));

        step("And the new sequence survives reloading the Mission Center page");
        missionCenterPage.reloadMissionCenterPage();
        missionCenterPage.verifySequenceAfterMove(sequenceBeforeMove, sourceOrder, targetOrder);

        step("And the Mission List Page lists the live missions in the configured order");
        List<String> liveMissionSlugs = loyaltyApi.waitForLiveMissionsToFollowConfiguredOrder(
                missionCenterPage.getSequenceSlugs());
        missionCenterPage.verifyLiveMissionsFollowConfiguredOrder(liveMissionSlugs);
    }

    @Author(names = { "Yanuar" })
    @Module(name = "MissionConfig")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testMissionCenterCancelDiscardsStagedHeaderImage() {
        Session<?> session = WebTest.sessionThreadLocal.get();
        Map<String, String> testData = getParam("testMissionCenterCancelDiscardsStagedHeaderImage");
        WebTest.paramsThreadLocal.set(testData);

        String email = testData.get("email");
        String password = testData.get("password");

        MissionCenterPage<?> missionCenterPage = new MissionCenterPage<>(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);

        step("Given user opens the Mission Center page in the Loyalty admin panel");
        missionCenterPage.goToMissionCenterPage(email, password);

        step("And the current banner filenames, sequence and live banner URLs are recorded");
        HeaderImages bannersBefore = missionCenterPage.getSavedHeaderImages();
        List<String> sequenceBefore = missionCenterPage.getSequenceSlugs();
        MissionBannerImages liveBannersBefore = loyaltyApi.getMissionBannerImages();

        step("When user clicks Edit and replaces the Desktop and Mobile image assets");
        missionCenterPage.clickEdit();
        missionCenterPage.replaceHeaderImagesWithStagedAssets();
        missionCenterPage.verifyStagedHeaderImagesDisplayed();

        step("And user clicks Cancel");
        missionCenterPage.clickCancel();

        step("Then the sequence returns to read-only and the staged header images are discarded");
        Assertion.assertThat("Cancel returns the page to read-only", missionCenterPage.getVisibleDragHandleCount(), is(0));
        missionCenterPage.verifyStagedHeaderImagesDiscarded();
        missionCenterPage.verifySavedHeaderImages(bannersBefore);
        Assertion.assertThat("Cancel does not change the mission sequence", missionCenterPage.getSequenceSlugs(), is(sequenceBefore));

        step("And the Mission List Page still serves the original mobile and desktop banners");
        Assertion.assertThat("Cancel does not publish a new mobile or desktop banner",
                loyaltyApi.waitForMissionBannerImagesToRemain(liveBannersBefore), is(liveBannersBefore));
    }
}
