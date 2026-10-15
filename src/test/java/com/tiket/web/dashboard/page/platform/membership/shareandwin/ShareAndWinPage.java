package com.tiket.web.dashboard.page.platform.membership.shareandwin;

import com.tiket.dbclient.TiketMysqlDBManager;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ShareAndWinPage<T extends WebDriver> extends BasePage<T> {

    private static final String SHARE_AND_WIN_PATH = "/admin/member/shareandwin";

    /** DB config key (dbSheet tribe) and table used to tear down test campaigns the UI cannot delete. */
    private static final String DB_TRIBE = "Membership";
    private static final String CAMPAIGN_TABLE = "tix_member_core.sharewin_campaigns";

    private static final Locator SHARE_AND_WIN_HEADING = new Locator("SHARE_AND_WIN_HEADING",
            By.xpath("//h2[text()='Share & Win Campaign']"));
    private static final Locator SEARCH_CAMPAIGN_SLUG = new Locator("SEARCH_CAMPAIGN_SLUG",
            By.id("search-campaign-slug"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON",
            By.xpath("//button[text()='Search']"));
    private static final Locator PREVIEW_EN_LINK = new Locator("PREVIEW_EN_LINK",
            By.xpath("//a[contains(@href,'sharewin-preview') and contains(@href,'lang=en')]"));

    /**
     * Slug search on the dashboard is a partial match ("create" also returns "create-abc123"), and the
     * list is sorted by last update, so the wanted campaign is rarely the first row. Every row read and
     * row action is therefore scoped to the row whose Campaign Slug cell equals the slug exactly.
     */
    private static final String CAMPAIGN_ROW_XPATH = "//tbody/tr[td[3][normalize-space()='%s']]";
    private static final Locator CREATE_CAMPAIGN_LINK = new Locator("CREATE_CAMPAIGN_LINK",
            By.xpath("//a[contains(@href,'/admin/member/shareandwin/create')]"));

    private static final Locator CREATE_CAMPAIGN_HEADING = new Locator("CREATE_CAMPAIGN_HEADING",
            By.xpath("//h2[text()='Create New Campaign']"));
    private static final Locator CAMPAIGN_NAME_INPUT = new Locator("CAMPAIGN_NAME_INPUT",
            By.id("campaign-name"));
    private static final Locator CAMPAIGN_SLUG_INPUT = new Locator("CAMPAIGN_SLUG_INPUT",
            By.id("campaign-slug"));
    private static final Locator NEXT_PAGE_BUTTON = new Locator("NEXT_PAGE_BUTTON",
            By.xpath("//p[text()='Next page']/parent::div"));
    private static final Locator SAVE_CAMPAIGN_BUTTON = new Locator("SAVE_CAMPAIGN_BUTTON",
            By.xpath("//button[text()='Save the campaign']"));
    private static final Locator SUCCESS_TOAST = new Locator("SUCCESS_TOAST",
            By.xpath("//*[contains(text(),'Successfully save the campaign')]"));
    private static final Locator VALIDATION_ERROR = new Locator("VALIDATION_ERROR",
            By.xpath("//*[contains(text(),'already exist')]"));

    private static final Locator FRIENDS_MILESTONES_HERO_BANNER = new Locator("FRIENDS_MILESTONES_HERO_BANNER",
            By.xpath("//*[text()='Hero Banner']"));

    private static final Locator GENERAL_CASE_SECTION_FRIEND_3 = new Locator("GENERAL_CASE_SECTION_FRIEND_3",
            By.xpath("(//*[text()='General Case (applies for both achieve & not achieve)'])[2]"));
    private static final Locator GENERAL_CASE_SECTION_FRIEND_5 = new Locator("GENERAL_CASE_SECTION_FRIEND_5",
            By.xpath("(//*[text()='General Case (applies for both achieve & not achieve)'])[3]"));

    private static final Locator PROMO_CODE_DATA_ID_FRIEND_1 = promoCodeDataIdLocator(1);
    private static final Locator PROMO_CODE_DATA_ID_FRIEND_3 = promoCodeDataIdLocator(2);
    private static final Locator PROMO_CODE_DATA_ID_FRIEND_5 = promoCodeDataIdLocator(3);

    private static final Locator PUSH_NOTIF_URL_LINK = urlLinkLocator(1);
    private static final Locator SOCIAL_MEDIA_URL_LINK_TO_APPS = new Locator("SOCIAL_MEDIA_URL_LINK_TO_APPS",
            By.id("-url-link-to-apps"));
    private static final Locator SOCIAL_MEDIA_SHAREABLE_LINK = new Locator("SOCIAL_MEDIA_SHAREABLE_LINK",
            By.id("shareable-link"));

    private static final Locator[] REWARD_COMM_SECTIONS = {
            rewardCommSectionLocator(1), rewardCommSectionLocator(3), rewardCommSectionLocator(5)
    };
    private static final Locator[] REWARD_COMM_URL_LINKS = {
            urlLinkLocator(2), urlLinkLocator(3), urlLinkLocator(4)
    };
    private static final Locator[] REWARD_COMM_EMAIL_BTN_URLS = {
            emailLinkBtnUrlLocator(1), emailLinkBtnUrlLocator(2), emailLinkBtnUrlLocator(3)
    };
    private static final Locator[] REWARD_COMM_EMAIL_TEXT_URLS = {
            emailBtnTextUrlLocator(1), emailBtnTextUrlLocator(2), emailBtnTextUrlLocator(3)
    };

    private static Locator campaignRowLocator(String slug) {
        return new Locator("CAMPAIGN_ROW[" + slug + "]",
                By.xpath(String.format(CAMPAIGN_ROW_XPATH, slug)));
    }

    private static Locator campaignNameCellLocator(String slug) {
        return new Locator("CAMPAIGN_NAME_CELL[" + slug + "]",
                By.xpath(String.format(CAMPAIGN_ROW_XPATH + "/td[2]", slug)));
    }

    private static Locator campaignSlugCellLocator(String slug) {
        return new Locator("CAMPAIGN_SLUG_CELL[" + slug + "]",
                By.xpath(String.format(CAMPAIGN_ROW_XPATH + "/td[3]", slug)));
    }

    private static Locator rowActionLocator(String slug, String action) {
        return new Locator("ACTION_" + action.toUpperCase() + "[" + slug + "]",
                By.xpath(String.format(CAMPAIGN_ROW_XPATH + "//*[@data-testid='action-%s']", slug, action)));
    }

    private static Locator promoCodeDataIdLocator(int position) {
        return new Locator("PROMO_CODE_DATA_ID_" + position,
                By.xpath(String.format("(//input[@id='promo-code-data-id'])[%d]", position)));
    }

    private static Locator urlLinkLocator(int position) {
        return new Locator("URL_LINK_" + position,
                By.xpath(String.format("(//input[@id='url-link'])[%d]", position)));
    }

    private static Locator emailLinkBtnUrlLocator(int position) {
        return new Locator("EMAIL_LINK_BTN_URL_" + position,
                By.xpath(String.format("(//input[@id='email-link-button-url'])[%d]", position)));
    }

    private static Locator emailBtnTextUrlLocator(int position) {
        return new Locator("EMAIL_BTN_TEXT_URL_" + position,
                By.xpath(String.format("(//input[@id='email-button-text-url'])[%d]", position)));
    }

    private static Locator rewardCommSectionLocator(int friendNumber) {
        return new Locator("REWARD_COMM_SECTION_" + friendNumber,
                By.xpath(String.format(
                        "//div[contains(text(),'Reward Communication') and ./div[text()='%d']]", friendNumber)));
    }

    public ShareAndWinPage(Session<T> session) {
        super(session);
    }


    public void goToShareAndWinDashboard(String email, String password) {
        MemberLoginPage<T> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(email, password);
        memberLoginPage.goToPage(SHARE_AND_WIN_PATH);
        driverTools.waitForWebToLoad();
    }

    public boolean isShareAndWinPageLoaded() {
        driverTools.waitForElementToAppear(SHARE_AND_WIN_HEADING, 15);
        return driverTools.isElementDisplayed(SHARE_AND_WIN_HEADING);
    }

    public boolean isCampaignFormLoaded() {
        driverTools.waitForElementToAppear(CREATE_CAMPAIGN_HEADING, 15);
        return driverTools.isElementDisplayed(CREATE_CAMPAIGN_HEADING);
    }


    public void searchCampaignBySlug(String slug) {
        driverTools.waitForElementToAppear(SEARCH_CAMPAIGN_SLUG, 10);
        driverTools.scrollElementToCenter(SEARCH_CAMPAIGN_SLUG);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(SEARCH_CAMPAIGN_SLUG, slug);
        driverTools.click(SEARCH_BUTTON);
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToAppear(campaignRowLocator(slug), 10);
    }

    public void searchAndVerifyCampaignBySlug(String slug) {
        driverTools.waitForElementToAppear(SHARE_AND_WIN_HEADING, 15);
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            searchCampaignBySlug(slug);
            try {
                if (driverTools.isElementDisplayed(campaignRowLocator(slug))) {
                    return;
                }
                logger.log("Row for slug '" + slug + "' not present on attempt " + (i + 1) + ", searching again...");
            } catch (org.openqa.selenium.TimeoutException | org.openqa.selenium.StaleElementReferenceException e) {
                logger.log("Stale element reading campaign row on attempt " + (i + 1) + ", retrying...");
            }
            driverTools.waitForWebToLoad();
        }
        throw new AssertionError("Campaign with slug '" + slug + "' not found after " + maxRetries + " search attempts");
    }

    /**
     * Tears down a test campaign directly in the DB by its slug ({@code code} column). The Share & Win
     * dashboard has no UI delete (MVP: only one campaign can be active), so created/duplicated campaigns
     * are removed here to keep the environment clean. The {@code isActive = 0} guard guarantees an
     * active/seed campaign can never be deleted, and any failure is logged (not thrown) so a cleanup
     * issue never masks the actual test result.
     */
    public void deleteCampaignBySlugFromDb(String slug) {
        if (slug == null || slug.isBlank()) {
            return;
        }
        try {
            int deleted = TiketMysqlDBManager.getInstance(DB_TRIBE).executeUpdate(
                    "DELETE FROM " + CAMPAIGN_TABLE + " WHERE code = ? AND isActive = 0", slug);
            logger.log("[Cleanup] Deleted " + deleted + " Share & Win campaign row(s) with code=" + slug);
        } catch (Exception e) {
            logger.log("[Cleanup] Could not delete Share & Win campaign with code=" + slug + " : " + e.getMessage());
        }
    }

    public String getCampaignName(String slug) {
        return getStableText(campaignNameCellLocator(slug));
    }

    public String getCampaignSlug(String slug) {
        return getStableText(campaignSlugCellLocator(slug));
    }

    public void clickPreviewIcon(String slug) {
        clickRowAction(slug, "preview");
    }

    public String clickPreviewEnLinkAndGetNewTabUrl() {
        driverTools.waitForElementToAppear(PREVIEW_EN_LINK, 10);

        String originalWindow = driver.getWindowHandle();
        int handleCountBefore = driver.getWindowHandles().size();

        driverTools.click(PREVIEW_EN_LINK);
        driverTools.waitForNewTabAndSwitchToLatest(handleCountBefore);
        driverTools.waitForWebToLoad();

        String previewUrl = driverTools.getCurrentURL();
        driverTools.closeCurrentTab();
        driver.switchTo().window(originalWindow);
        return previewUrl;
    }

    public void clickDuplicateIcon(String slug) {
        clickRowAction(slug, "duplicate");
    }

    public void clickEditIcon(String slug) {
        clickRowAction(slug, "edit");
        driverTools.waitForWebToLoad();
    }

    private void clickRowAction(String slug, String action) {
        Locator actionIcon = rowActionLocator(slug, action);
        driverTools.waitForElementToAppear(actionIcon, 10);
        driverTools.scrollElementToCenter(actionIcon);
        driverTools.click(actionIcon);
    }

    public void clickCreateCampaign() {
        driverTools.waitForElementToAppear(CREATE_CAMPAIGN_LINK, 10);
        driverTools.click(CREATE_CAMPAIGN_LINK);
        driverTools.waitForWebToLoad();
    }

    public void fillCampaignDetails(String name, String slug) {
        driverTools.refreshWebpage();
        driverTools.waitForWebToLoad();
        fillInputWithRetry(CAMPAIGN_NAME_INPUT, name);
        fillInputWithRetry(CAMPAIGN_SLUG_INPUT, slug);
    }

    public void updateCampaignDetails(String name, String slug) {
        fillInputWithRetry(CAMPAIGN_NAME_INPUT, name);
        fillInputWithRetry(CAMPAIGN_SLUG_INPUT, slug);
    }

    /**
     * One-shot edit flow: finds the campaign by slug, opens the editor, applies the new
     * name/slug, advances to the last wizard page, and saves. Handy for edit/restore steps.
     */
    public void editCampaign(String searchSlug, String newName, String newSlug) {
        searchAndVerifyCampaignBySlug(searchSlug);
        clickEditIcon(searchSlug);
        updateCampaignDetails(newName, newSlug);
        navigateToLastPage();
        clickSaveCampaign();
    }

    public void navigateToRewardsPage() {
        clickNextAndWaitFor(FRIENDS_MILESTONES_HERO_BANNER);
        clickNextAndWaitFor(PROMO_CODE_DATA_ID_FRIEND_1);
    }

    public void navigateToLastPage() {
        int maxClicks = 5;
        for (int i = 0; i < maxClicks; i++) {
            driverTools.waitForWebToLoad();

            if (driverTools.waitForElementToAppear(SAVE_CAMPAIGN_BUTTON, 3).isPresent()) {
                return;
            }
            if (driverTools.isElementDisplayed(VALIDATION_ERROR)) {
                throw new RuntimeException("Validation error on duplicate form: campaign name/slug already exists");
            }

            driverTools.waitForElementToAppear(NEXT_PAGE_BUTTON, 10);
            driverTools.clickWithJavascriptExecutor(NEXT_PAGE_BUTTON);
        }
        throw new RuntimeException("Could not reach the last wizard page after " + maxClicks + " Next page clicks");
    }

    public void clickSaveCampaign() {
        driverTools.waitForElementToAppear(SAVE_CAMPAIGN_BUTTON, 15);
        driverTools.waitForElementToBeClickable(SAVE_CAMPAIGN_BUTTON, 10)
                .orElseThrow(() -> new RuntimeException("Save the campaign button is not clickable"));
        driverTools.click(SAVE_CAMPAIGN_BUTTON);
        driverTools.waitForWebToLoad();
    }

    public boolean isSuccessToastDisplayed() {
        driverTools.waitForElementToAppear(SUCCESS_TOAST, 15);
        return driverTools.isElementDisplayed(SUCCESS_TOAST);
    }

    public void fillPromoCodeDataIds(String promoIdFriend1, String promoIdFriend3, String promoIdFriend5) {
        fillInputWithRetry(PROMO_CODE_DATA_ID_FRIEND_1, promoIdFriend1);

        expandSectionAndWaitForInput(GENERAL_CASE_SECTION_FRIEND_3, PROMO_CODE_DATA_ID_FRIEND_3);
        fillInputWithRetry(PROMO_CODE_DATA_ID_FRIEND_3, promoIdFriend3);

        expandSectionAndWaitForInput(GENERAL_CASE_SECTION_FRIEND_5, PROMO_CODE_DATA_ID_FRIEND_5);
        fillInputWithRetry(PROMO_CODE_DATA_ID_FRIEND_5, promoIdFriend5);
    }

    /**
     * Base tiket.com URL for the active environment, used to build campaign share/notification links.
     */
    public String getBaseUrl() {
        return switch (Configs.ENVIRONMENT) {
            case GK -> "https://gatotkaca.tiket.com";
            case PREPROD -> "https://preprod.tiket.com";
            case PROD -> "https://tiket.com";
            default -> throw new IllegalStateException("Unsupported environment: " + Configs.ENVIRONMENT);
        };
    }

    public void fillPushNotificationUrlLink(String url) {
        fillInputWithRetry(PUSH_NOTIF_URL_LINK, url);
    }

    public void fillSocialMediaContent(String urlLinkToApps, String shareableLink) {
        fillInputWithRetry(SOCIAL_MEDIA_URL_LINK_TO_APPS, urlLinkToApps);
        fillInputWithRetry(SOCIAL_MEDIA_SHAREABLE_LINK, shareableLink);
    }

    public void fillRewardCommunicationUrls(String urlLink, String emailLinkBtnUrl, String emailBtnTextUrl) {
        for (int i = 0; i < REWARD_COMM_SECTIONS.length; i++) {
            expandSectionAndWaitForInput(REWARD_COMM_SECTIONS[i], REWARD_COMM_URL_LINKS[i]);
            fillInputWithRetry(REWARD_COMM_URL_LINKS[i], urlLink);
            fillInputWithRetry(REWARD_COMM_EMAIL_BTN_URLS[i], emailLinkBtnUrl);
            fillInputWithRetry(REWARD_COMM_EMAIL_TEXT_URLS[i], emailBtnTextUrl);
        }
    }

    private String getStableText(Locator locator) {
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            driverTools.waitForWebToLoad();
            driverTools.waitForElementToAppear(locator, 10);
            try {
                return driverTools.getText(locator).orElse("");
            } catch (org.openqa.selenium.TimeoutException | org.openqa.selenium.StaleElementReferenceException e) {
                if (i == maxRetries - 1) throw e;
                logger.log("Stale element on getText for " + locator.name() + ", retry " + (i + 1));
            }
        }
        return "";
    }

    private void clickNextAndWaitFor(Locator expectedElement) {
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            driverTools.waitForWebToLoad();
            if (driverTools.isElementDisplayed(VALIDATION_ERROR)) {
                throw new RuntimeException("Validation error detected: campaign name/slug already exists");
            }
            driverTools.waitForElementToAppear(NEXT_PAGE_BUTTON, 10);
            try {
                driverTools.clickWithJavascriptExecutor(NEXT_PAGE_BUTTON);
                driverTools.waitForWebToLoad();
                if (driverTools.waitForElementToAppear(expectedElement, 15).isPresent()) {
                    return;
                }
                logger.log("Expected element " + expectedElement.name()
                        + " not found after Next click, attempt " + (i + 1) + ", retrying...");
            } catch (org.openqa.selenium.StaleElementReferenceException
                     | org.openqa.selenium.ElementClickInterceptedException e) {
                if (i == maxRetries - 1) throw e;
                logger.log("Click Next page failed on attempt " + (i + 1) + ", retrying...");
            }
        }
        throw new RuntimeException("Failed to navigate to page with element: " + expectedElement.name());
    }

    private void expandSectionAndWaitForInput(Locator sectionHeader, Locator expectedInput) {
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            driverTools.waitForElementToAppear(sectionHeader, 10);
            try {
                driverTools.scrollElementToCenter(sectionHeader);
                driverTools.clickWithJavascriptExecutor(sectionHeader);
                if (driverTools.waitForElementToAppear(expectedInput, 10).isPresent()) {
                    return;
                }
                logger.log("Input " + expectedInput.name() + " not visible after expanding "
                        + sectionHeader.name() + ", attempt " + (i + 1));
            } catch (org.openqa.selenium.StaleElementReferenceException
                     | org.openqa.selenium.ElementClickInterceptedException e) {
                if (i == maxRetries - 1) throw e;
                logger.log("Expand section " + sectionHeader.name() + " failed on attempt " + (i + 1) + ", retrying...");
            }
        }
        throw new RuntimeException("Failed to expand section " + sectionHeader.name()
                + " and reveal " + expectedInput.name());
    }

    private void fillInputWithRetry(Locator locator, String value) {
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            driverTools.waitForWebToLoad();
            if (driverTools.waitForElementToAppear(locator, 15).isEmpty()) {
                logger.log("Element " + locator.name() + " not found on attempt " + (i + 1) + ", retrying...");
                continue;
            }
            try {
                driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(locator, value);
                return;
            } catch (org.openqa.selenium.StaleElementReferenceException
                     | org.openqa.selenium.ElementNotInteractableException
                     | java.util.NoSuchElementException
                     | DriverToolsException e) {
                if (i == maxRetries - 1) throw e;
                logger.log("Fill " + locator.name() + " failed on attempt " + (i + 1) + ", retrying...");
            }
        }
        throw new org.openqa.selenium.NoSuchElementException(
                "Element " + locator.name() + " not found after " + maxRetries + " attempts");
    }
}
