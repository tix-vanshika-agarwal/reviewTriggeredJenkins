package com.tiket.web.page.common.mission;

import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mission microsite page (e.g. {@code /en-id/mission/stay-with-benefits}).
 *
 * <p>The stamp card renders a summary such as {@code "0/15 stamps"} where the first number is the
 * count of stamps already collected by the user and the second is the total required for the
 * mission. This page object exposes helpers to open a mission by its slug and to read the collected
 * stamp count so tests can assert whether a transaction did (or did not) grant a mission stamp / GBV.
 */
public class MissionPageWeb<T extends WebDriver> extends WebPage<T> {

    public MissionPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> MissionPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new MissionPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new MissionPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final String STAGING_BASE_URL = "https://gatotkaca.tiket.com";
    private static final String PREPROD_BASE_URL = "https://preprod.tiket.com";
    private static final String PROD_BASE_URL = "https://www.tiket.com";
    private static final String MISSION_PATH = "/en-id/mission/";

    /** Reads allowed for the mission page's stamp count to catch up with the mission API. */
    private static final int STAMP_COUNT_SYNC_ATTEMPTS = 6;

    /** Pause between stamp count reads; the page is refreshed before each re-read. */
    private static final int STAMP_COUNT_SYNC_INTERVAL_SECONDS = 5;

    /**
     * Remembers the last mission opened and how it was opened, so {@link #refreshMissionPage()} can reload
     * the microsite the right way for the current platform: dWeb re-navigates the browser to the mission
     * URL, while the app opens the mission inside an internal webview (no reload affordance) and must
     * re-fire the deeplink and switch contexts.
     */
    private String lastMissionSlug;

    /** How {@link #refreshMissionPage()} should reload the mission; set by the last open call. */
    private OpenMode lastOpenMode;

    /** How the mission microsite was last opened, which determines how {@link #refreshMissionPage()} reloads it. */
    private enum OpenMode {
        /** dWeb: opened by navigating the browser to the mission URL (reload = re-navigate to the URL). */
        URL,
        /** App: opened by firing the mission deeplink into an internal webview (reload = re-fire the deeplink). */
        DEEPLINK
    }

    // First number token (with optional decimal/thousands separators) followed by its optional magnitude
    // suffix in a GBV "spent" label, e.g. "564rb", "0.5M", "500.000". Applied to lower-cased text.
    private static final Pattern GBV_AMOUNT_PATTERN = Pattern.compile("(\\d[\\d.,]*)\\s*([a-z]*)");

    // The UI shows GBV spend abbreviated to ~3 significant digits (e.g. "564rb", "1.2jt"), so an exact
    // match against the raw API rupiah value is impossible. Accept a relative band around the API value:
    // wide enough to absorb the precision the UI drops, tight enough to still reject an order-of-magnitude
    // mismatch (e.g. a UI "50K" against an API-credited ~500K).
    private static final double GBV_UI_MATCH_TOLERANCE = 0.15;

    // Stamp count span lives in the "styles_stamp_achieved" container and reads "X/Y stamps" (EN) or
    // "X/Y stempel" (ID), e.g. "0/15 stamps". The sibling note "(Each stamp is received after trip)"
    // / "(Setiap stempel...)" has no "/", so scoping to the container's span that contains "/" picks
    // the count for any numbers, in any language, without depending on the wording.
    private static final Locator STAMP_SUMMARY = new Locator("STAMP_SUMMARY",
            new By.ByXPath("//div[contains(@class,'styles_stamp_achieved')]/span[contains(.,'/')]"));

    // The mission microsite shows ContentLoader placeholder skeletons while data is loading.
    private static final Locator LOADING_PLACEHOLDER = new Locator("LOADING_PLACEHOLDER",
            new By.ByCssSelector("[class*='ContentLoader_wave']"));

    // GBV (Gross Booking Value) progress bar container, present for spend-based missions instead of the
    // stamp summary. Identified by its language-agnostic test id.
    private static final Locator GBV_PROGRESS = new Locator("GBV_PROGRESS",
            new By.ByCssSelector("[data-testid='click-tooltip-gbv']"));

    // The "spent so far" label inside the GBV progress card, e.g. "IDR 564K spent" (EN) /
    // "IDR 564rb sudah dibelanjakan" (ID). Scoped to the "spent" sub-container (styles_wrapper_gbv_info_spent)
    // so it never matches the sibling "left"/"lagi" amount, and picks the span carrying the amount by
    // requiring it to contain at least one digit — currency-agnostic, so it works for any configured
    // currency (IDR, USD, SGD, ...) rather than only IDR.
    private static final Locator GBV_SPENT_TEXT = new Locator("GBV_SPENT_TEXT",
            new By.ByXPath("//div[contains(@class,'wrapper_gbv_info_spent')]"
                    + "//span[string-length(translate(normalize-space(.), '0123456789', '')) < string-length(normalize-space(.))]"));

    // Bottom "Swipe to join" / "Geser untuk ikutan" CTA shown only while the user has NOT joined the
    // mission. Detected by its (language-agnostic) ButtonSlider structure rather than its label.
    private static final Locator JOIN_MISSION_CTA = new Locator("JOIN_MISSION_CTA",
            new By.ByXPath("//div[contains(@class,'styles_cta_button')]//div[contains(@class,'ButtonSlider_confirmation_text')]"));

    // Bottom claim CTA shown once the mission is complete (all stamps collected). It lives in the
    // same styles_cta_button container as the join slider but is a plain <button> (not a slider), so
    // we target the button inside the CTA container that is NOT a ButtonSlider. Best-effort: the
    // reward popup often auto-opens on a completed mission, in which case this CTA is not needed.
    private static final Locator CLAIM_REWARD_CTA = new Locator("CLAIM_REWARD_CTA",
            new By.ByXPath("//div[contains(@class,'styles_cta_button')]//button[not(.//div[contains(@class,'ButtonSlider')])]"));

    // XPath fragment that uniquely identifies the reward popup (gift-voucher modal): a styles_content
    // section that holds BOTH the close button and the gift-voucher card, independent of language.
    // The on-page mission card reuses the same styles_content / GiftVoucher* classes, so every
    // popup-scoped locator below is anchored to this fragment to avoid matching the on-page card
    // (which would otherwise let Selenium read a wrong reward amount or click the wrong button).
    private static final String REWARD_POPUP_SECTION_XPATH =
            "//section[contains(@class,'styles_content')]"
                    + "[.//*[@data-testid='close-button'] and .//div[contains(@class,'GiftVoucher_gift_voucher')]]";

    // Reward popup (gift-voucher modal) shown when a completed mission has a reward to claim.
    private static final Locator REWARD_POPUP = new Locator("REWARD_POPUP",
            new By.ByXPath(REWARD_POPUP_SECTION_XPATH));

    // Reward amount inside the popup, e.g. "250,000 Points" (EN) / "250.000 Points" (ID). Scoped to
    // the popup section because GiftVoucherTitle_title also matches the on-page reward card.
    private static final Locator REWARD_AMOUNT = new Locator("REWARD_AMOUNT",
            new By.ByXPath(REWARD_POPUP_SECTION_XPATH + "//*[contains(@class,'GiftVoucherTitle_title')]"));

    // Reward type/name label inside the popup, e.g. "Blibli Tiket Points".
    private static final Locator REWARD_TITLE = new Locator("REWARD_TITLE",
            new By.ByXPath(REWARD_POPUP_SECTION_XPATH
                    + "//*[contains(@class,'styles_wrapper_title')]//*[contains(@class,'Text_variant_highEmphasis')]"));

    // Primary "Claim" / "Klaim" button inside the reward popup that redeems the reward.
    private static final Locator REWARD_CLAIM_BUTTON = new Locator("REWARD_CLAIM_BUTTON",
            new By.ByXPath(REWARD_POPUP_SECTION_XPATH + "//button[contains(@class,'Button_variant_primary')]"));

    // Post-claim success state: after a successful claim the reward popup transitions to a confirmation
    // modal that keeps the same section structure (close-button + gift-voucher card) but whose primary
    // CTA changes from "Claim"/"Klaim" to a "use reward" action - confirmed labels "Use points" (EN) /
    // "Pakai poinnya" (ID); gift-voucher variants read "Use ..."/"Pakai ..."/"Gunakan ...". A failed
    // claim leaves the original "Claim" CTA, so this success CTA appearing is a reliable on-screen
    // confirmation the claim was accepted (for both points and gift-voucher rewards). Matched ONLY via
    // the use-reward CTA, scoped to the reward popup section: a celebration animation
    // (styles_lottie_wrapper) is deliberately NOT used because it can render while the Claim CTA is still
    // active, which would false-pass before the actual post-claim transition.
    private static final Locator REWARD_CLAIMED_CONFIRMATION = new Locator("REWARD_CLAIMED_CONFIRMATION",
            new By.ByXPath(REWARD_POPUP_SECTION_XPATH + "//button[.//*[contains(text(),'Pakai') or contains(text(),'Use') or contains(text(),'Gunakan')]]"));

    // --- Locators for Mission Center List Page ---
    private static final Locator MISSION_CENTER_TITLE = new Locator("MISSION_CENTER_TITLE",
            new By.ByXPath("//span[contains(@class, 'Text_size_h1') and text()='Mission Center']"));

    private static final Locator MISSION_CENTER_SUBTITLE = new Locator("MISSION_CENTER_SUBTITLE",
            new By.ByXPath("//span[text()='Complete missions, get rewards.' or text()='Selesaikan misi, raih hadiahnya.']"));

    private static final Locator YOUR_PROGRESS_TAB = new Locator("YOUR_PROGRESS_TAB",
            new By.ByXPath("//*[text()='Your Progress' or text()='Progresmu']"));

    private static final Locator OTHER_MISSIONS_TAB = new Locator("OTHER_MISSIONS_TAB",
            new By.ByXPath("//*[text()='Other Missions' or text()='Misi Lainnya']"));

    private static final Locator HISTORY_TAB = new Locator("HISTORY_TAB",
            new By.ByXPath("//*[text()='History' or text()='Riwayat']"));

    private static final Locator EMPTY_STATE_HEADING = new Locator("EMPTY_STATE_HEADING",
            new By.ByXPath("//*[text()=\"You haven't joined any mission\" or text()='Kamu belum ikutan misi apa pun' or text()='No completed missions yet' or text()='Belum ada misi yang selesai']"));

    private static final Locator CHANGE_LANGUAGE = new Locator("CHANGE_LANGUAGE",
            new By.ByXPath("//button[contains(., 'EN |') or contains(., 'ID |')]"));

    private static final Locator BAHASA_INDONESIA_OPTION = new Locator("BAHASA_INDONESIA_OPTION",
            new By.ByXPath("//label[contains(., 'Bahasa Indonesia')]"));

    private static final Locator ENGLISH_OPTION = new Locator("ENGLISH_OPTION",
            new By.ByXPath("//label[contains(., 'English')]"));

    private static final Locator SAVE_LANGUAGE_BTN = new Locator("SAVE_LANGUAGE_BTN",
            new By.ByXPath("//button[contains(., 'Save') or contains(., 'Simpan')]"));

    private static final Locator BROWSE_MISSIONS_BTN = new Locator("BROWSE_MISSIONS_BTN",
            new By.ByXPath("//button[contains(., 'Browse missions') or contains(., 'Jelajahi misi')]"));

    private static final Locator ACTIVE_MISSION_CARD_BTN = new Locator("ACTIVE_MISSION_CARD_BTN",
            new By.ByXPath("(//button[contains(., 'Join mission here') or contains(., 'Ikuti misi di sini')])[1]"));

    private static final Locator SUCCESS_MODAL_TITLE = new Locator("SUCCESS_MODAL_TITLE",
            new By.ByXPath("//*[contains(@class, 'dialog_title')]"));

    private static final Locator SEE_THE_MISSION_BTN = new Locator("SEE_THE_MISSION_BTN",
            new By.ByXPath("//button[contains(@class, 'dialog_button_secondary') and (contains(., 'See the mission') or contains(., 'Lihat misinya'))]"));

    private static final Locator FIRST_ACTIVE_MISSION_TITLE = new Locator("FIRST_ACTIVE_MISSION_TITLE",
            new By.ByXPath("(//div[contains(@class, 'reward_title')]//span)[1]"));

    private static final Locator UI_BACK_ARROW_BTN = new Locator("UI_BACK_ARROW_BTN",
            new By.ByXPath("//div[contains(@class, 'back_nav')]"));

    private static final Locator FIRST_ACTIVE_MISSION_CARD_BODY = new Locator("FIRST_ACTIVE_MISSION_CARD_BODY",
            new By.ByXPath("(//button[contains(., 'Join mission here') or contains(., 'Ikuti misi di sini')]/ancestor::div[contains(@class, 'styles_mission_card')][1]//span[contains(@class, 'styles_mission_title')])[1]"));

    private static final Locator MISSION_HISTORY_CTA = new Locator("MISSION_HISTORY_CTA",
            new By.ByXPath("//div[contains(@class, 'styles_header_navigation')]//div[contains(@class, 'MobileRightColumn_menu_icons')]//div[contains(@class, 'styles_button')][.//*[local-name()='svg'][@viewBox='0 0 24 25']]"));

    private static final Locator JOIN_MISSION_DIALOG = new Locator("JOIN_MISSION_DIALOG",
            new By.ByXPath("//div[contains(@class, 'Dialog_body')]//section[contains(@class, 'Figure_figure')][.//h2[contains(., 'join a mission') or contains(., 'ikutan misi')]]"));

    private static final Locator JOIN_MISSION_DIALOG_TITLE = new Locator("JOIN_MISSION_DIALOG_TITLE",
            new By.ByXPath("//div[contains(@class, 'Dialog_body')]//h2[contains(@class, 'Figure_title') and (contains(., 'join a mission') or contains(., 'ikutan misi'))]"));

    private static final Locator JOIN_MISSION_DIALOG_DESC = new Locator("JOIN_MISSION_DIALOG_DESC",
            new By.ByXPath("//div[contains(@class, 'Dialog_body')]//p[contains(@class, 'Figure_description') and (contains(., 'Mission History') or contains(., 'Riwayat Misi'))]"));

    private static final Locator JOIN_MISSION_DIALOG_CTA = new Locator("JOIN_MISSION_DIALOG_CTA",
            new By.ByXPath("//div[contains(@class, 'Dialog_body')]//div[contains(@class, 'Figure_footer')]//button[contains(., 'Join mission') or contains(., 'Ikutan misi')]"));

    private static final Locator ONBOARDING_POPUP_CLOSE = new Locator("ONBOARDING_POPUP_CLOSE",
            new By.ByXPath("//div[contains(@class, 'Dialog_body')]//*[@data-testid='close-button']"));

    private static final Locator HISTORY_MISSION_CARD = new Locator("HISTORY_MISSION_CARD",
            new By.ByXPath("(//div[contains(@class, 'styles_mission_card')]//span[contains(@class, 'styles_mission_title')])[1]"));

    private static final Locator MISSION_HISTORY_TITLE = new Locator("MISSION_HISTORY_TITLE",
            new By.ByXPath("//span[contains(@class, 'page_title') and (text()='Mission History' or text()='Riwayat Misi')]"));

    private static final Locator MISSION_HISTORY_ORDER_ID = new Locator("MISSION_HISTORY_ORDER_ID",
            new By.ByXPath("(//div[contains(@class, 'styles_card_wrapper')]//span[contains(@class, 'styles_order_text')])[1]"));

    private static final Locator MISSION_HISTORY_ORDER_NEXT_ICON = new Locator("MISSION_HISTORY_ORDER_NEXT_ICON",
            new By.ByXPath("(//div[contains(@class, 'styles_card_wrapper')]//div[contains(@class, 'styles_next_icon_stamp')])[1]"));

    /**
     * @return {@code true} if the mission is complete and the bottom "claim reward" CTA is shown.
     */
    public boolean isClaimRewardAvailable() {
        boolean present = driverTools.waitForElementToAppear(CLAIM_REWARD_CTA, 20).isPresent();
        logger.log("Claim reward CTA present (mission complete)? " + present);
        return present;
    }

    /**
     * Clicks the bottom "claim reward" CTA on a completed mission to surface the reward popup.
     */
    public void clickClaimReward() {
        logger.log("Clicking the claim reward CTA on the completed mission");
        driverTools.click(CLAIM_REWARD_CTA);
        driverTools.waitForWebToLoad();
    }

    /**
     * @return {@code true} once the reward (gift-voucher) popup is visible, waiting up to
     *         {@code timeoutSeconds} for it (it can auto-open shortly after a completed mission loads).
     */
    public boolean isRewardReceivedPopupVisible(int timeoutSeconds) {
        boolean visible = driverTools.waitForElementToAppear(REWARD_POPUP, timeoutSeconds).isPresent();
        if (!visible) {
            logPageSourceForDebug("REWARD_POPUP not visible");
        }
        return visible;
    }

    /**
     * @return {@code true} once the reward (gift-voucher) popup is visible (default 30s wait).
     */
    public boolean isRewardReceivedPopupVisible() {
        return isRewardReceivedPopupVisible(30);
    }

    /**
     * @return the raw reward amount text shown on the reward popup, e.g. {@code "250,000 Points"} (EN)
     *         or {@code "250.000 Points"} (ID).
     */
    public String getRewardAmountText() {
        return driverTools.getText(driverTools.waitForElementToAppear(REWARD_AMOUNT, 30).orElseThrow());
    }

    /**
     * @return the reward type/name shown on the popup, e.g. {@code "Blibli Tiket Points"}.
     */
    public String getRewardTitleText() {
        return driverTools.getText(driverTools.waitForElementToAppear(REWARD_TITLE, 30).orElseThrow());
    }

    /**
     * Best-effort variant of {@link #getRewardTitleText()} for logging only: the title is non-critical,
     * so a missing element returns a placeholder instead of failing the test.
     *
     * @return the reward title, or {@code "(title not shown)"} if it is not present.
     */
    public String getRewardTitleTextSafely() {
        try {
            return getRewardTitleText();
        } catch (RuntimeException ignored) {
            return "(title not shown)";
        }
    }

    /**
     * @return the numeric reward points shown on the UI (digits only of {@link #getRewardAmountText()}),
     *         e.g. {@code 250000} for {@code "250,000 Points"} or {@code "250.000 Points"}.
     */
    public int getRewardPointsValue() {
        String text = getRewardAmountText();
        String digits = text.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) {
            throw new IllegalStateException("Unable to parse reward points from UI text: '" + text + "'");
        }
        return Integer.parseInt(digits);
    }

    /**
     * @return {@code true} if the visible reward is a points reward (amount/title mentions points/poin),
     *         {@code false} if it appears to be a non-points reward (e.g. a gift voucher / GV).
     */
    public boolean isPointsReward() {
        String amount = getRewardAmountText().toLowerCase();
        String title = "";
        try {
            title = getRewardTitleText().toLowerCase();
        } catch (RuntimeException ignored) {
            // title is optional for the points decision
        }
        return amount.contains("point") || amount.contains("poin")
                || title.contains("point") || title.contains("poin");
    }

    /**
     * Clicks the primary "Claim" / "Klaim" button inside the reward popup to redeem the reward.
     */
    public void clickClaimInRewardPopup() {
        logger.log("Clicking the 'Claim'/'Klaim' button in the reward popup");
        driverTools.click(REWARD_CLAIM_BUTTON);
        driverTools.waitForWebToLoad();
    }

    /**
     * UI-level confirmation that a claim was accepted: after clicking Claim, the reward popup transitions
     * to its success state - a celebration animation appears and the primary CTA changes from
     * "Claim"/"Klaim" to a "use reward" action ("Pakai .." / "Use .." / "Gunakan .."). A failed claim
     * leaves the popup on the original "Claim" CTA, so the appearance of this success state is a reliable
     * on-screen signal the claim succeeded (for both points and gift-voucher rewards).
     *
     * @param timeoutSeconds how long to wait for the post-claim success state to appear
     * @return {@code true} if the post-claim success state appeared within the timeout
     */
    public boolean isRewardClaimAcceptedOnUi(int timeoutSeconds) {
        boolean claimedShown = driverTools.waitForElementToAppear(REWARD_CLAIMED_CONFIRMATION, timeoutSeconds)
                .isPresent();
        logger.log("Reward popup post-claim success state visible (UI claim accepted)? " + claimedShown);
        if (!claimedShown) {
            logPageSourceForDebug("REWARD_CLAIMED_CONFIRMATION not visible after clicking Claim");
        }
        return claimedShown;
    }

    public void openMission(String missionSlug) {
        this.lastMissionSlug = missionSlug;
        this.lastOpenMode = OpenMode.URL;
        driverTools.maximizeWindow();
        String url = getBaseUrl() + MISSION_PATH + missionSlug;
        logger.log("Opening mission page via URL: " + url);
        driverTools.get(url);
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToDisappear(LOADING_PLACEHOLDER, 30);
    }

    /**
     * Opens the mission page via a deeplink in the native app. The mission microsite loads inside
     * the app's internal webview; the caller must switch to webview context after this call to
     * interact with mission page elements.
     */
    public void openMissionDeeplink(String missionSlug) {
        this.lastMissionSlug = missionSlug;
        this.lastOpenMode = OpenMode.DEEPLINK;
        String path = MISSION_PATH + missionSlug;
        Deeplink missionDeeplink = new Deeplink("Mission",
                "tiket://m.gatotkaca.tiket.com" + path,
                "tiket://m.preprod.tiket.com" + path,
                "tiket://m.tiket.com" + path);
        logger.log("Opening mission page via deeplink for slug: " + missionSlug);
        driverTools.deeplink(missionDeeplink);
    }

    public boolean isStampSummaryVisible() {
        boolean visible = driverTools.waitForElementToAppear(STAMP_SUMMARY, 30).isPresent();
        if (!visible) {
            logPageSourceForDebug("STAMP_SUMMARY not visible");
        }
        return visible;
    }

    /** Logs a short debug context when an expected mission UI state is missing. */
    private void logPageSourceForDebug(String context) {
        logger.log("[MISSION DEBUG] " + context);
    }

    /**
     * @return {@code true} if the mission still shows the join CTA ("Swipe to join"), i.e. the current
     *         user has NOT joined the mission yet and needs to enroll.
     */
    public boolean isJoinMissionRequired() {
        boolean present = driverTools.waitForElementToAppear(JOIN_MISSION_CTA, 10).isPresent();
        logger.log("Join mission CTA present (join required)? " + present);
        return present;
    }

    /**
     * Reloads the mission microsite so the latest join / stamp / GBV status is reflected, then waits
     * for it to finish loading.
     *
     * <p><b>Why not a browser-style reload?</b> The mission microsite runs inside the app's internal
     * webview, which has no reload affordance — {@code driver.navigate().refresh()} is unreliable there
     * (unlike on real web, where it maps to a page reload). The only dependable way to force a fresh
     * render is to re-open the mission from native: switch to native context, fire the mission deeplink
     * again, dismiss the currency/country popup that can reappear after a deeplink, then switch back
     * into the webview and wait for the content to settle.
     *
     * <p>On dWeb the mission is opened via a plain URL ({@link #openMission(String)}), so a refresh simply
     * re-navigates the browser to the same URL. The deeplink/context-switch path below applies only to the
     * app, where the mission runs inside an internal webview with no reload affordance.
     *
     * <p>Relies on the slug (and open mode) remembered by the most recent {@link #openMission(String)} or
     * {@link #openMissionDeeplink(String)} call.
     */
    public void refreshMissionPage() {
        if (lastMissionSlug == null) {
            throw new IllegalStateException(
                    "refreshMissionPage() was called before openMission(...) / openMissionDeeplink(...) — "
                            + "no mission slug is available to reload the microsite.");
        }
        if (lastOpenMode == OpenMode.URL) {
            logger.log("Refreshing the mission page by re-opening its URL (dWeb has no webview to switch): "
                    + "slug=" + lastMissionSlug);
            openMission(lastMissionSlug);
            return;
        }
        logger.log("Refreshing the mission page by re-firing its deeplink (webview has no reload): slug="
                + lastMissionSlug);
        WebViewPage<T> webViewPage = WebViewPage.getInstance(session);
        webViewPage.switchToNative();
        openMissionDeeplink(lastMissionSlug);
        HomePage.getInstance(session).closeCurrencyCountryPopup();
        webViewPage.switchToWebViewUrl("mission");
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToDisappear(LOADING_PLACEHOLDER, 30);
    }

    /**
     * @return the raw stamp summary text, e.g. {@code "0/15 stamps"}.
     */
    public String getStampSummaryText() {
        return driverTools.getText(driverTools.waitForElementToAppear(STAMP_SUMMARY, 30).orElseThrow());
    }

    /**
     * @return the number of stamps the user has already collected (the numerator of "X/Y stamps").
     */
    public int getCollectedStampCount() {
        return parseStampPart(0);
    }

    /**
     * Re-reads the collected stamp count, refreshing the mission page between attempts, until it equals
     * {@code expectedCount} or the retry budget is exhausted. The mission microsite can lag the mission
     * detail API, so a single read right after the API confirms a stamp can be stale; polling here keeps
     * the UI-vs-API assertion from failing on that propagation delay alone.
     *
     * @param expectedCount   the collected-stamp count the UI is expected to converge to (e.g. the value
     *                        already confirmed via the mission detail API)
     * @param maxAttempts     maximum number of reads (≥ 1)
     * @param intervalSeconds delay between attempts (a refresh is issued before each re-read)
     * @return the last collected stamp count read (equal to {@code expectedCount} if it converged in time)
     */
    public int waitForCollectedStampCount(int expectedCount, int maxAttempts, int intervalSeconds) {
        int collected = getCollectedStampCount();
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            collected = getCollectedStampCount();
            logger.log("Mission UI collected-stamp check " + attempt + "/" + maxAttempts + ": collected="
                    + collected + " (expected " + expectedCount + ")");
            if (collected == expectedCount) {
                return collected;
            }
            if (attempt < maxAttempts) {
                try {
                    Thread.sleep(intervalSeconds * 1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                refreshMissionPage();
            }
        }
        return collected;
    }

    /**
     * {@link #waitForCollectedStampCount(int, int, int)} with the page's default sync budget of
     * {@link #STAMP_COUNT_SYNC_ATTEMPTS} reads, {@link #STAMP_COUNT_SYNC_INTERVAL_SECONDS}s apart.
     */
    public int waitForCollectedStampCount(int expectedCount) {
        return waitForCollectedStampCount(expectedCount, STAMP_COUNT_SYNC_ATTEMPTS, STAMP_COUNT_SYNC_INTERVAL_SECONDS);
    }

    /**
     * @return the total number of stamps required by the mission (the denominator of "X/Y stamps").
     */
    public int getTotalStampCount() {
        return parseStampPart(1);
    }

    /**
     * @return {@code true} if the GBV (Gross Booking Value) progress bar is shown, i.e. this is a
     *         spend-based mission tracking an amount spent rather than collected stamps.
     */
    public boolean isGbvProgressVisible() {
        boolean visible = driverTools.waitForElementToAppear(GBV_PROGRESS, 30).isPresent();
        if (!visible) {
            logPageSourceForDebug("GBV_PROGRESS not visible");
        }
        return visible;
    }

    /**
     * @return the raw "spent so far" text on the GBV progress card, e.g. {@code "IDR 564K spent"} (EN)
     *         or {@code "IDR 564rb sudah dibelanjakan"} (ID).
     */
    public String getGbvSpentText() {
        return driverTools.getText(driverTools.waitForElementToAppear(GBV_SPENT_TEXT, 30).orElseThrow());
    }

    /**
     * @return the GBV "spent so far" amount from the UI as an <b>approximate absolute</b> rupiah value,
     *         expanding any magnitude suffix (e.g. {@code "564rb"}/{@code "564K"} → {@code 564000},
     *         {@code "0,5jt"}/{@code "0.5M"} → {@code 500000}, {@code "500.000"} → {@code 500000}). The UI
     *         abbreviates and rounds large amounts, so this is an approximation suited to a tolerance-band
     *         comparison against the API, not an exact figure. Returns {@code 0} when nothing is spent yet.
     */
    public long getGbvSpentAbsolute() {
        return parseGbvSpentToAbsolute(getGbvSpentText());
    }

    /**
     * Converts a GBV "spent" UI label into its approximate absolute rupiah value.
     *
     * <ul>
     *   <li>With a magnitude suffix (K/rb/ribu → thousand, M/jt/juta → million, B/mlr/miliar → billion)
     *       the numeric part is read with its separator as a decimal point and multiplied by the unit,
     *       e.g. {@code "0,5jt"} → {@code 500000}, {@code "1.2M"} → {@code 1200000}.</li>
     *   <li>Without a suffix, separators are treated as thousands groupings and stripped,
     *       e.g. {@code "500.000"} → {@code 500000}, {@code "0"} → {@code 0}.</li>
     * </ul>
     *
     * @return the absolute value, or {@code 0} when no numeric amount can be parsed
     */
    private long parseGbvSpentToAbsolute(String uiText) {
        if (uiText == null) {
            return 0L;
        }
        Matcher matcher = GBV_AMOUNT_PATTERN.matcher(uiText.toLowerCase());
        if (!matcher.find()) {
            return 0L;
        }
        String numberToken = matcher.group(1);
        long unit = gbvMagnitudeUnit(matcher.group(2));
        if (unit > 1L) {
            // A magnitude suffix is present, so a single separator is a decimal point (e.g. "0,5jt").
            String normalized = numberToken.replace(',', '.');
            int firstDot = normalized.indexOf('.');
            if (firstDot >= 0) {
                // Keep only the first separator as the decimal point; drop any further grouping dots.
                normalized = normalized.substring(0, firstDot + 1)
                        + normalized.substring(firstDot + 1).replace(".", "");
            }
            return Math.round(Double.parseDouble(normalized) * unit);
        }
        // No magnitude suffix: any separators are thousands groupings.
        String digits = numberToken.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? 0L : Long.parseLong(digits);
    }

    /**
     * Maps a magnitude suffix that immediately follows the amount to its multiplier. Handles English
     * (K/M/B) and Indonesian (rb/ribu, jt/juta, mlr/miliar) abbreviations. Billion tokens are checked
     * before million so {@code "miliar"} is not mistaken for {@code "m"} (million). Returns {@code 1}
     * for an empty/unknown suffix (e.g. a trailing word such as {@code "spent"}).
     */
    private long gbvMagnitudeUnit(String suffix) {
        if (suffix == null || suffix.isEmpty()) {
            return 1L;
        }
        String s = suffix.toLowerCase();
        if (s.startsWith("rb") || s.startsWith("ribu") || s.startsWith("k")) {
            return 1_000L;
        }
        if (s.startsWith("miliar") || s.startsWith("mlr") || s.startsWith("bn") || s.equals("b")) {
            return 1_000_000_000L;
        }
        if (s.startsWith("jt") || s.startsWith("juta") || s.startsWith("m")) {
            return 1_000_000L;
        }
        return 1L;
    }

    /**
     * Polls the GBV "spent so far" UI value, refreshing the mission page between attempts, until it is
     * <b>consistent with the absolute spend reported by the mission-detail API</b>.
     *
     * <p>This is stronger than a bare "greater than baseline" check, which passes on any non-zero value,
     * so a stale or wrong UI amount can slip through even when the API credited a different figure. The UI
     * abbreviates and rounds large amounts, so an exact equality against the raw API rupiah value is
     * impossible. Instead this expands the UI's abbreviated figure to an approximate absolute value (via
     * {@link #getGbvSpentAbsolute()}) and accepts it only when it falls within a relative tolerance band
     * ({@link #GBV_UI_MATCH_TOLERANCE}) of {@code apiAbsoluteSpend}. That absorbs the precision the UI
     * drops while still rejecting an order-of-magnitude mismatch (e.g. a UI "50K" against ~500K). It also
     * requires a strictly positive UI value so a "0 spent" read never counts as a match.
     *
     * @param apiAbsoluteSpend the exact spend reported by the mission-detail API (must be {@code > 0})
     * @param maxAttempts      maximum number of reads (≥ 1)
     * @param intervalSeconds  delay between attempts (a refresh is issued before each re-read)
     * @return {@code true} once the UI value falls within the tolerance band of the API value, else
     *         {@code false} when the retry budget is exhausted
     */
    public boolean waitForGbvSpentMatchingApi(long apiAbsoluteSpend, int maxAttempts, int intervalSeconds) {
        double tolerance = Math.abs(apiAbsoluteSpend) * GBV_UI_MATCH_TOLERANCE;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            long uiAbsolute = getGbvSpentAbsolute();
            boolean matches = uiAbsolute > 0 && Math.abs(uiAbsolute - apiAbsoluteSpend) <= tolerance;
            logger.log("Mission UI GBV-spent vs API check " + attempt + "/" + maxAttempts + ": uiAbsolute="
                    + uiAbsolute + ", api=" + apiAbsoluteSpend + ", tolerance=±" + Math.round(tolerance)
                    + " -> matches=" + matches);
            if (matches) {
                return true;
            }
            if (attempt < maxAttempts) {
                try {
                    Thread.sleep(intervalSeconds * 1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                refreshMissionPage();
            }
        }
        return false;
    }

    private int parseStampPart(int index) {
        String summary = getStampSummaryText();
        String digits = summary.replaceAll("[^0-9/]", "");
        String[] parts = digits.split("/");
        if (parts.length <= index || parts[index].isEmpty()) {
            throw new IllegalStateException("Unable to parse stamp summary text: '" + summary + "'");
        }
        return Integer.parseInt(parts[index]);
    }

    private String getBaseUrl() {
        return switch (Configs.ENVIRONMENT) {
            case GK -> STAGING_BASE_URL;
            case PREPROD -> PREPROD_BASE_URL;
            case PROD -> PROD_BASE_URL;
            default -> throw new NotImplementedException("Unsupported environment: " + Configs.ENVIRONMENT);
        };
    }

    /**
     * Verify Mission Center List Page.
     */
    public void openMissionCenterList() {
        this.lastOpenMode = OpenMode.URL;
        driverTools.maximizeWindow();
        String url = getBaseUrl() + MISSION_PATH;
        logger.log("Opening mission list page via URL: " + url);
        driverTools.get(url);
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToDisappear(LOADING_PLACEHOLDER, 15);
    }

    public boolean isOnMissionCenterListPage() {
        driverTools.waitForWebToLoad();
        String currentUrl = driverTools.getCurrentURL();
        logger.log("Current URL: " + currentUrl);

        boolean isUrlCorrect = currentUrl != null && currentUrl.contains("mission");

        logger.log("Verifying Mission Center title and subtitle visibility...");
        boolean isTitleVisible = driverTools.waitForElementToAppear(MISSION_CENTER_TITLE, 10).isPresent();
        boolean isSubtitleVisible = driverTools.waitForElementToAppear(MISSION_CENTER_SUBTITLE, 10).isPresent();

        if (!isTitleVisible || !isSubtitleVisible || !isUrlCorrect) {
            logPageSourceForDebug("Mission Center validation failed. See boolean status above.");
        }

        return isUrlCorrect && isTitleVisible && isSubtitleVisible;
    }

    public void clickYourProgressTab() {
        logger.log("Clicking 'Your Progress' (Progresmu) tab");
        driverTools.click(YOUR_PROGRESS_TAB);
        driverTools.waitForWebToLoad();
    }

    public void clickHistoryTab() {
        logger.log("Clicking 'History' (Riwayat) tab");
        driverTools.click(HISTORY_TAB);
        driverTools.waitForWebToLoad();
    }

    public void clickOtherMissionsTab() {
        logger.log("Clicking 'Other Missions' (Misi Lainnya) tab");
        driverTools.click(OTHER_MISSIONS_TAB);
        driverTools.waitForWebToLoad();
    }

    public boolean isAnyActiveMissionAvailable() {
        logger.log("Checking if there are active missions available in the 'Other Missions' tab");
        return driverTools.waitForElementToAppear(ACTIVE_MISSION_CARD_BTN, 15).isPresent();
    }

    public String clickFirstActiveMission() {
        logger.log("Scrolling to, capturing title, and clicking the first available active mission card");

        // 1. Safely locate the button and scroll it to the center of the screen (avoids sticky
        // header/footer overlays intercepting the click)
        WebElement btn = driverTools.waitForElementToAppear(ACTIVE_MISSION_CARD_BTN, 15).orElseThrow();
        driverTools.scrollElementToCenter(btn);

        // 2. Capture the title
        String missionTitle = driverTools.getText(driverTools.waitForElementToAppear(FIRST_ACTIVE_MISSION_TITLE, 15).orElseThrow());
        logger.log("Captured Mission Title: " + missionTitle);

        // 3. Pause to let the UI animations/React state settle
        driverTools.dummyWait(2);

        // 4. RE-FETCH the button just in case React refreshed the DOM during the sleep
        logger.log("Clicking the 'Join' button");
        try {
            // Re-fetching inside the try block ensures we don't click a stale element
            driverTools.click(ACTIVE_MISSION_CARD_BTN);
        } catch (Exception e) {
            logger.log("Standard click intercepted, attempting JavaScript click...");
            driverTools.clickWithJavascriptExecutor(ACTIVE_MISSION_CARD_BTN);
        }

        driverTools.waitForWebToLoad();

        return missionTitle;
    }

    public boolean isOnMissionDetailPage() {
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToDisappear(LOADING_PLACEHOLDER, 15);

        String currentUrl = driverTools.getCurrentURL();
        logger.log("Current URL after clicking mission card: " + currentUrl);

        return currentUrl != null && currentUrl.contains("/mission/") && !currentUrl.endsWith("/mission");
    }

    public String getEmptyStateText() {
        logger.log("Reading empty state text from the page");
        driverTools.scrollToElement(EMPTY_STATE_HEADING);
        return driverTools.getText(driverTools.waitForElementToAppear(EMPTY_STATE_HEADING, 10).orElseThrow());
    }

    public void clickBrowseMissionsButton() {
        logger.log("Clicking the 'Browse missions' (Jelajahi misi) button");
        driverTools.scrollToElement(BROWSE_MISSIONS_BTN);
        driverTools.click(BROWSE_MISSIONS_BTN);
        driverTools.waitForWebToLoad();
    }

    public void switchLanguageToIndonesian() {
        logger.log("Switching language to Bahasa Indonesia via header menu");

        driverTools.click(CHANGE_LANGUAGE);
        driverTools.click(BAHASA_INDONESIA_OPTION);
        driverTools.click(SAVE_LANGUAGE_BTN);

        // Wait for the browser to trigger the reload and fetch the ID locale
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToDisappear(LOADING_PLACEHOLDER, 15);
    }

    /**
     * Puts the page into English via the header language picker. No-op when the page is already in
     * English, which matters because the site persists the visitor's last locale across tests sharing
     * a session - a run can land on this page already in Bahasa Indonesia (e.g. a prior test switched
     * it) and an English-only assertion would fail on an otherwise healthy page without this call.
     */
    public void switchLanguageToEnglish() {
        if (isEnglishLanguageActive()) {
            logger.log("Page is already in English; skipping the language switch");
            return;
        }
        logger.log("Switching language to English via header menu");

        driverTools.click(CHANGE_LANGUAGE);
        driverTools.click(ENGLISH_OPTION);
        driverTools.click(SAVE_LANGUAGE_BTN);

        // Wait for the browser to trigger the reload and fetch the EN locale
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToDisappear(LOADING_PLACEHOLDER, 15);
    }

    /**
     * @return {@code true} if the header language button ("EN | IDR" / "ID | IDR") shows English.
     */
    private boolean isEnglishLanguageActive() {
        String label = driverTools.getText(driverTools.waitForElementToAppear(CHANGE_LANGUAGE, 15).orElseThrow());
        boolean active = label != null && label.trim().toUpperCase().startsWith("EN");
        logger.log("Active language read from header ('" + label + "'): " + (active ? "English" : "Indonesian"));
        return active;
    }

    /**
     * New Method For New User Joined Mission From List
     */
    public boolean isSuccessEnrollModalVisible() {
        logger.log("Verifying 'Your mission is officially on!' modal appears");

        driverTools.dummyWait(2);

        return driverTools.waitForElementToAppear(SUCCESS_MODAL_TITLE, 20).isPresent();
    }

    public void clickSeeTheMissionToCloseModal() {
        logger.log("Clicking 'See the mission' to close the success modal");

        driverTools.dummyWait(2);

        driverTools.click(SEE_THE_MISSION_BTN);
        driverTools.waitForWebToLoad();
    }

    public void navigateBackToListPage() {
        logger.log("Clicking the UI back arrow to return to List Page");
        driverTools.waitForElementToAppear(UI_BACK_ARROW_BTN, 20);
        driverTools.click(UI_BACK_ARROW_BTN);
        driverTools.waitForWebToLoad();
    }

    /**
     * @param missionIdentifier The exact title of the mission or the URL slug
     * @return boolean true if the exact enrolled card is found
     */
    public boolean isSpecificEnrolledMissionCardPresent(String missionIdentifier) {
        logger.log("Checking if the specific enrolled mission '" + missionIdentifier + "' is present in 'Your Progress'");

        //Wait 3 seconds to allow the 'Your Progress' tab to fetch data
        driverTools.dummyWait(3);

        logger.log("Scrolling down slightly to render lazy-loaded elements");
        try {
            driverTools.scroll(1, 400);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String dynamicXpath = String.format(
                "(//*[contains(., '%s')]/ancestor::div[contains(@class, 'mission') or contains(@class, 'card') or contains(@class, 'container')][1]//button)[1]",
                missionIdentifier);

        Locator specificEnrolledCardBtn = new Locator("SPECIFIC_ENROLLED_CARD_BTN_" + missionIdentifier,
                new By.ByXPath(dynamicXpath));

        boolean isPresent = driverTools.waitForElementToAppear(specificEnrolledCardBtn, 15).isPresent();

        if (isPresent) {
            driverTools.scrollToElement(specificEnrolledCardBtn);
        } else {
            logger.log("Failed to find the enrolled mission card for: " + missionIdentifier);
        }

        return isPresent;
    }

    public void clickFirstActiveMissionCard() {
        logger.log("Clicking the body of the first active mission card, not its join button");
        WebElement card = driverTools.waitForElementToAppear(FIRST_ACTIVE_MISSION_CARD_BODY, 15).orElseThrow();
        driverTools.scrollElementToCenter(card);
        driverTools.dummyWait(2);
        try {
            driverTools.click(FIRST_ACTIVE_MISSION_CARD_BODY);
        } catch (Exception e) {
            logger.log("Standard click intercepted, attempting JavaScript click...");
            driverTools.clickWithJavascriptExecutor(FIRST_ACTIVE_MISSION_CARD_BODY);
        }
        driverTools.waitForWebToLoad();
    }

    public void dismissOnboardingPopupIfPresent() {
        if (driverTools.waitForElementToPresenceSafe(ONBOARDING_POPUP_CLOSE, 3).isEmpty()) {
            logger.log("No onboarding popup appeared on the mission detail page; continuing");
            return;
        }
        logger.log("An onboarding popup appeared on the mission detail page; closing it");
        try {
            driverTools.click(ONBOARDING_POPUP_CLOSE);
        } catch (Exception e) {
            logger.log("Close click intercepted, attempting JavaScript click...");
            driverTools.clickWithJavascriptExecutor(ONBOARDING_POPUP_CLOSE);
        }
        driverTools.waitForWebToLoad();
    }

    public void clickMissionHistoryCta() {
        logger.log("Clicking the mission history CTA in the mission detail header");
        driverTools.waitForElementToAppear(MISSION_HISTORY_CTA, 15).orElseThrow();
        try {
            driverTools.click(MISSION_HISTORY_CTA);
        } catch (Exception e) {
            logger.log("Standard click intercepted, attempting JavaScript click...");
            driverTools.clickWithJavascriptExecutor(MISSION_HISTORY_CTA);
        }
        driverTools.waitForWebToLoad();
    }

    public boolean isJoinMissionDialogVisible() {
        logger.log("Verifying the enrollment bottom sheet to join a mission is displayed");
        return driverTools.waitForElementToAppear(JOIN_MISSION_DIALOG, 10).isPresent();
    }

    public boolean isJoinMissionDialogTitleDisplayed() {
        logger.log("Verifying the enrollment bottom sheet title invites the user to join a mission");
        return driverTools.waitForElementToAppear(JOIN_MISSION_DIALOG_TITLE, 10).isPresent();
    }

    public boolean isJoinMissionDialogDescriptionDisplayed() {
        logger.log("Verifying the enrollment bottom sheet description refers to the Mission History");
        return driverTools.waitForElementToAppear(JOIN_MISSION_DIALOG_DESC, 10).isPresent();
    }

    public boolean isJoinMissionDialogCtaDisplayed() {
        return driverTools.isElementDisplayed(JOIN_MISSION_DIALOG_CTA);
    }

    public void clickFirstHistoryMissionCard() {
        logger.log("Clicking the first mission card in the History tab");
        WebElement card = driverTools.waitForElementToAppear(HISTORY_MISSION_CARD, 20).orElseThrow(() -> { Assertion.skip("No mission card in the History tab - the account has not completed any mission"); return new IllegalStateException("unreachable"); });
        driverTools.scrollElementToCenter(card);
        driverTools.dummyWait(2);
        try {
            driverTools.click(HISTORY_MISSION_CARD);
        } catch (Exception e) {
            logger.log("Standard click intercepted, attempting JavaScript click...");
            driverTools.clickWithJavascriptExecutor(HISTORY_MISSION_CARD);
        }
        driverTools.waitForWebToLoad();
    }

    public boolean isOnMissionHistoryPage() {
        driverTools.waitForElementToDisappear(LOADING_PLACEHOLDER, 15);
        return driverTools.waitForElementToAppear(MISSION_HISTORY_TITLE, 20).isPresent();
    }

    public String getFirstMissionHistoryOrderId() {
        WebElement orderText = driverTools.waitForElementToAppear(MISSION_HISTORY_ORDER_ID, 20).orElseThrow(() -> { Assertion.skip("No order card on the Mission History page - the account has no mission order history"); return new IllegalStateException("unreachable"); });
        String orderId = driverTools.getText(orderText).replaceAll("[^0-9]", "");
        logger.log("Order ID read from the first mission history card: " + orderId);
        return orderId;
    }

    public void clickFirstMissionHistoryOrderCard() {
        logger.log("Clicking the first mission history order card");
        driverTools.scrollToElement(MISSION_HISTORY_ORDER_NEXT_ICON);
        try {
            driverTools.click(MISSION_HISTORY_ORDER_NEXT_ICON);
        } catch (Exception e) {
            logger.log("Standard click intercepted, attempting JavaScript click...");
            driverTools.clickWithJavascriptExecutor(MISSION_HISTORY_ORDER_NEXT_ICON);
        }
        driverTools.waitForWebToLoad();
    }

    public String waitForYourOrderDetailUrl() {
        driverTools.waitForUrlToContainAnyOf(30, "/yourorder/detail");
        driverTools.waitForWebToLoad();
        String url = driverTools.getCurrentURL();
        logger.log("Landed on URL after clicking the mission history order card: " + url);
        return url;
    }
}
