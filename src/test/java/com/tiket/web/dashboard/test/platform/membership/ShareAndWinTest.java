package com.tiket.web.dashboard.test.platform.membership;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.membership.shareandwin.ShareAndWinPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class ShareAndWinTest extends BaseTest {

    @Author(names = {"Yanuar"})
    @Module(name = "ShareAndWin")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void verifyAdminCanPreviewShareAndWinCampaign() {
        Map<String, String> param = getParam("verifyAdminCanPreviewShareAndWinCampaign");
        BaseTest.paramsThreadLocal.set(param);
        ShareAndWinPage<?> page = new ShareAndWinPage<>(BaseTest.sessionThreadLocal.get());
        String campaignSlug = param.get("campaignSlug");

        step("Given user is logged in and on the Share & Win dashboard");
        page.goToShareAndWinDashboard(param.get("adminEmail"), param.get("adminPassword"));
        Assertion.assertThat("Share & Win page is loaded",
                page.isShareAndWinPageLoaded(), is(true));

        step("When user searches for a campaign and clicks the preview icon");
        page.searchCampaignBySlug(campaignSlug);
        page.clickPreviewIcon(campaignSlug);

        step("And user clicks Preview EN link — a new tab opens with the preview");
        String previewUrl = page.clickPreviewEnLinkAndGetNewTabUrl();
        Assertion.assertThat("Preview URL contains 'sharewin-preview'",
                previewUrl, containsString("sharewin-preview"));
        Assertion.assertThat("Preview URL contains campaign slug",
                previewUrl, containsString(campaignSlug));
    }

    @Author(names = {"Yanuar"})
    @Module(name = "ShareAndWin")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void verifyAdminCanDuplicateShareAndWinCampaign() {
        Map<String, String> param = getParam("verifyAdminCanDuplicateShareAndWinCampaign");
        BaseTest.paramsThreadLocal.set(param);
        ShareAndWinPage<?> page = new ShareAndWinPage<>(BaseTest.sessionThreadLocal.get());
        String campaignSlug = param.get("campaignSlug");

        String uniqueSuffix = CoreFunctions.generateRandomString(10).toLowerCase();
        String newCampaignName = "auto dupe " + uniqueSuffix;
        String newCampaignSlug = "auto-dupe-" + uniqueSuffix;

        step("Given user is logged in and on the Share & Win dashboard");
        page.goToShareAndWinDashboard(param.get("adminEmail"), param.get("adminPassword"));
        Assertion.assertThat("Share & Win page is loaded",
                page.isShareAndWinPageLoaded(), is(true));

        try {
            step("When user duplicates an existing campaign");
            page.searchCampaignBySlug(campaignSlug);
            page.clickDuplicateIcon(campaignSlug);
            Assertion.assertThat("Create campaign page is loaded",
                    page.isCampaignFormLoaded(), is(true));

            step("And user changes the campaign name/slug, navigates to the last page, and saves");
            page.fillCampaignDetails(newCampaignName, newCampaignSlug);
            page.navigateToLastPage();
            page.clickSaveCampaign();

            step("Then the duplicated campaign appears in the list with correct details");
            Assertion.assertThat("Success toast is displayed",
                    page.isSuccessToastDisplayed(), is(true));
            Assertion.assertThat("Campaign list page is loaded",
                    page.isShareAndWinPageLoaded(), is(true));
            page.searchAndVerifyCampaignBySlug(newCampaignSlug);
            Assertion.assertThat("Campaign name matches",
                    page.getCampaignName(newCampaignSlug), is(newCampaignName));
            Assertion.assertThat("Campaign slug matches",
                    page.getCampaignSlug(newCampaignSlug), is(newCampaignSlug));
        } finally {
            step("Cleanup: remove the duplicated campaign from the database");
            page.deleteCampaignBySlugFromDb(newCampaignSlug);
        }
    }

    @Author(names = {"Yanuar"})
    @Module(name = "ShareAndWin")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void verifyAdminCanCreateNewShareAndWinCampaign() {
        Map<String, String> param = getParam("verifyAdminCanCreateNewShareAndWinCampaign");
        BaseTest.paramsThreadLocal.set(param);
        ShareAndWinPage<?> page = new ShareAndWinPage<>(BaseTest.sessionThreadLocal.get());

        String[] promoIdArray = param.get("promoIds").split("#");
        if (promoIdArray.length < 3) {
            throw new IllegalArgumentException(
                    "promoIds must contain 3 values separated by '#', got: " + param.get("promoIds"));
        }

        String timestamp = CoreFunctions.getCurrentDate("ddMMyy HH:mm");
        String campaignName = "Automation " + timestamp;
        String campaignSlug = "create-" + CoreFunctions.generateRandomString(10).toLowerCase();

        String baseUrl = page.getBaseUrl();
        String pushNotifUrl = baseUrl + "/share/" + campaignSlug;
        String shareableLink = baseUrl + "/share/" + campaignSlug + "?share_code=%s";
        String baseLinkUrl = baseUrl + "/";

        step("Given user is logged in and on the Share & Win dashboard");
        page.goToShareAndWinDashboard(param.get("adminEmail"), param.get("adminPassword"));
        Assertion.assertThat("Share & Win page is loaded",
                page.isShareAndWinPageLoaded(), is(true));

        try {
            step("When user clicks Create Campaign and fills in campaign name/slug");
            page.clickCreateCampaign();
            Assertion.assertThat("Create campaign page is loaded",
                    page.isCampaignFormLoaded(), is(true));
            page.fillCampaignDetails(campaignName, campaignSlug);

            step("And user navigates to Rewards page and fills promo code data IDs for friend 1, 3, and 5");
            page.navigateToRewardsPage();
            page.fillPromoCodeDataIds(promoIdArray[0], promoIdArray[1], promoIdArray[2]);

            step("And user navigates to Message Communication page and fills all URL fields");
            page.navigateToLastPage();
            page.fillPushNotificationUrlLink(pushNotifUrl);
            page.fillSocialMediaContent(baseLinkUrl, shareableLink);
            page.fillRewardCommunicationUrls(pushNotifUrl, baseLinkUrl, baseLinkUrl);

            step("When user saves the campaign");
            page.clickSaveCampaign();

            step("Then the new campaign appears in the list with correct details");
            Assertion.assertThat("Success toast is displayed",
                    page.isSuccessToastDisplayed(), is(true));
            Assertion.assertThat("Campaign list page is loaded",
                    page.isShareAndWinPageLoaded(), is(true));
            page.searchAndVerifyCampaignBySlug(campaignSlug);
            Assertion.assertThat("Campaign name matches",
                    page.getCampaignName(campaignSlug), is(campaignName));
            Assertion.assertThat("Campaign slug matches",
                    page.getCampaignSlug(campaignSlug), is(campaignSlug));
        } finally {
            step("Cleanup: remove the created campaign from the database");
            page.deleteCampaignBySlugFromDb(campaignSlug);
        }
    }

    @Author(names = {"Yanuar"})
    @Module(name = "ShareAndWin")
    @Tribe(name = "Membership")
    @Vertical(name = "Platform")
    @Test
    public void verifyAdminCanEditShareAndWinCampaign() {
        Map<String, String> param = getParam("verifyAdminCanEditShareAndWinCampaign");
        BaseTest.paramsThreadLocal.set(param);
        ShareAndWinPage<?> page = new ShareAndWinPage<>(BaseTest.sessionThreadLocal.get());
        String campaignSlug = param.get("campaignSlug");

        String timestamp = CoreFunctions.getCurrentDate("ddMMyy HH:mm");
        String updatedName = "updated " + timestamp;
        String updatedSlug = "updated-" + CoreFunctions.generateRandomString(10).toLowerCase();

        step("Given user is logged in and on the Share & Win dashboard");
        page.goToShareAndWinDashboard(param.get("adminEmail"), param.get("adminPassword"));
        Assertion.assertThat("Share & Win page is loaded",
                page.isShareAndWinPageLoaded(), is(true));

        step("When user searches for a campaign and clicks edit");
        page.searchAndVerifyCampaignBySlug(campaignSlug);
        String originalName = page.getCampaignName(campaignSlug);
        page.clickEditIcon(campaignSlug);
        Assertion.assertThat("Edit campaign page is loaded",
                page.isCampaignFormLoaded(), is(true));

        step("And user updates the campaign name/slug, navigates to the last page, and saves");
        page.updateCampaignDetails(updatedName, updatedSlug);
        page.navigateToLastPage();
        page.clickSaveCampaign();

        step("Then the updated campaign appears in the list with correct details");
        Assertion.assertThat("Success toast is displayed",
                page.isSuccessToastDisplayed(), is(true));
        Assertion.assertThat("Campaign list page is loaded",
                page.isShareAndWinPageLoaded(), is(true));
        page.searchAndVerifyCampaignBySlug(updatedSlug);
        Assertion.assertThat("Updated campaign name matches",
                page.getCampaignName(updatedSlug), is(updatedName));
        Assertion.assertThat("Updated campaign slug matches",
                page.getCampaignSlug(updatedSlug), is(updatedSlug));

        step("And user restores the original campaign name/slug for idempotency");
        page.editCampaign(updatedSlug, originalName, campaignSlug);
        Assertion.assertThat("Restore success toast is displayed",
                page.isSuccessToastDisplayed(), is(true));
    }
}
