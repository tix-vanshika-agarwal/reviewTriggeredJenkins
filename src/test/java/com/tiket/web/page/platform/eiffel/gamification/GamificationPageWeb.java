package com.tiket.web.page.platform.eiffel.gamification;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javax.security.auth.login.FailedLoginException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.is;

/**
 * Berburu Tiket Murah (BTM), the treasure-hunt gamification page served under {@code /game}.
 */
public abstract class GamificationPageWeb<T extends WebDriver> extends WebPage<T> {

    private static final String GAME_PATH = "game/berburu-tiket-murah";
    private static final String CAMPAIGN_PATH = "/campaign/";

    private static final String STAGING_BASE_URL = "https://gatotkaca.tiket.com/";
    private static final String PREPROD_BASE_URL = "https://preprod.tiket.com/";

    /** Marks the snatch button as refusing bids, which is how the current holder of a treasure sees it. */
    private static final String INACTIVE_BUTTON_CLASS = "RankList_inactive";

    /**
     * How many further times the hunt is asked for when it comes up logged out. Two is enough for the
     * render that missed the session; anything beyond that is not a rendering blip.
     */
    private static final int GUEST_PAGE_RETRIES = 2;

    /**
     * How many tokens a points trade is exercised with. One is enough to show the trade working, and it
     * keeps the run cheap: the quantity cannot be typed into the sheet, so every extra token is another
     * press of "+".
     */
    public static final int TOKENS_TO_TRADE = 1;

    /** A bid costs one token, so one is all a snatch has to have in hand before it starts. */
    public static final int TOKENS_NEEDED_TO_BID = 1;

    /**
     * Timeouts as this page counts them, and worth knowing they do not all land the same way: the
     * element helpers on {@code DriverTools} stretch whatever they are handed on GK and preprod, while
     * the waits built in this class take the number as it stands.
     */
    private static final int ELEMENT_TIMEOUT_SECONDS = 15;
    private static final int ROUTING_TIMEOUT_SECONDS = 20;
    private static final int BID_TIMEOUT_SECONDS = 20;
    private static final int TRADE_TIMEOUT_SECONDS = 20;
    private static final int PAGE_STATE_TIMEOUT_SECONDS = 10;

    /**
     * The window for a look that expects to find nothing — the snatched badge once the player has taken
     * the treasure back. Giving that the full element budget would add dead waiting to a passing run.
     */
    private static final int ABSENCE_PROBE_SECONDS = 3;

    /**
     * Kept shorter still because it is spent on every single page open: the token popup is usually not
     * there at all, it arrives with the page's own token request rather than late, and one that does
     * turn up afterwards is caught by the second look taken before a campaign is picked.
     */
    private static final int POPUP_PROBE_SECONDS = 2;

    /** A number as the game writes it, grouping separators and all, so "1.234" is not read as 1. */
    private static final Pattern LEADING_COUNT = Pattern.compile("\\d[\\d.,]*");

    private static final String SNATCH_BUTTON_XPATH = "//*[contains(@class,'snatch-button')]";
    private static final String ACTIVE_CAMPAIGN_CARD_XPATH =
            "//*[contains(@class,'CampaignCard_inventory_card') and contains(@class,'CampaignCard_active')]";
    private static final String TOKEN_COUNTER_XPATH = "//*[contains(@class,'HeaderPage_token_label_icon_container')]";
    private static final String ADD_TOKENS_SHEET_XPATH = "//*[contains(@class,'BlibliPointRedeemer_bottom_sheet')]";
    private static final String TRADE_POINTS_CONTENT_XPATH = "//*[@data-testid='contentRedeemPoint']";
    private static final String TRADE_CONFIRMATION_XPATH = "//*[contains(@class,'Dialog_pop_up')]";

    private static final Locator LOGIN_PROMPT_CARD = new Locator("LOGIN_PROMPT_CARD",
            new By.ByXPath("//*[contains(@class,'FallbackPage_card')]"));
    private static final Locator MY_TOKEN_CONTAINER = new Locator("MY_TOKEN_CONTAINER",
            new By.ByXPath("//*[contains(@class,'HeaderPage_my_token_container')]"));
    private static final Locator ACTIVE_CAMPAIGN_CARD = new Locator("ACTIVE_CAMPAIGN_CARD",
            new By.ByXPath(ACTIVE_CAMPAIGN_CARD_XPATH));
    private static final Locator ACTIVE_CAMPAIGN_LIST = new Locator("ACTIVE_CAMPAIGN_LIST",
            new By.ByXPath("//*[contains(@class,'CampaignCardList_cards_container') and contains(@class,'CampaignCardList_active')]"));

    /** The hunt landing page keeps the balance in its hero, the campaign page in its navbar. */
    private static final Locator GAME_TOKEN_BALANCE = new Locator("GAME_TOKEN_BALANCE",
            new By.ByXPath(TOKEN_COUNTER_XPATH + "//h3"));
    private static final Locator CAMPAIGN_TOKEN_BALANCE = new Locator("CAMPAIGN_TOKEN_BALANCE",
            new By.ByXPath("//*[contains(@class,'HeaderNavbar_token_container')]/span"));

    private static final Locator CAMPAIGN_TITLE = new Locator("CAMPAIGN_TITLE",
            new By.ByXPath("//*[contains(@class,'RankList_price_name_container')]//span"));
    private static final Locator TREASURE_HOLDER_NAME = new Locator("TREASURE_HOLDER_NAME",
            new By.ByXPath("//*[contains(@class,'RankList_champion_container')]//span[contains(@class,'Text_text')]"));

    private static final Locator SNATCH_TREASURE_BUTTON = new Locator("SNATCH_TREASURE_BUTTON",
            new By.ByXPath(SNATCH_BUTTON_XPATH));
    private static final Locator SNATCH_TREASURE_COST = new Locator("SNATCH_TREASURE_COST",
            new By.ByXPath(SNATCH_BUTTON_XPATH + "//h2"));

    /**
     * The "+" the hunt sets beside its token counter, which raises the "Add Tokens" sheet. It is a bare
     * icon — no button around it, no label on it — so it is taken as the counter's trailing svg.
     */
    private static final Locator ADD_TOKENS_BUTTON = new Locator("ADD_TOKENS_BUTTON",
            new By.ByXPath(TOKEN_COUNTER_XPATH + "/*[local-name()='svg'][last()]"));

    private static final Locator ADD_TOKENS_SHEET = new Locator("ADD_TOKENS_SHEET",
            new By.ByXPath(ADD_TOKENS_SHEET_XPATH));

    /**
     * "Add Tokens" offers two ways to come by tokens and opens on the second, buying a voucher, so the
     * points trade has to be asked for. Nothing in a tab's own markup says which one is showing — there
     * is no {@code aria-selected} and no active class — so the trade is addressed as the first of the
     * two, and {@link #TRADE_POINTS_CONTENT} is what confirms the tab that came up.
     */
    private static final Locator TRADE_POINTS_TAB = new Locator("TRADE_POINTS_TAB",
            new By.ByXPath("(//*[contains(@class,'BlibliPointRedeemer_tab_list')]"
                    + "//button[contains(@class,'Tabs_tab__')])[1]"));

    /** The points trade, matched only while it is the tab on show. */
    private static final Locator TRADE_POINTS_CONTENT = new Locator("TRADE_POINTS_CONTENT",
            new By.ByXPath("//*[contains(@class,'Tabs_tab_panel_active')]" + TRADE_POINTS_CONTENT_XPATH));

    /** Where the sheet advertises what a token costs, as "Trade 3 Blibli Tiket Points to get 1 token." */
    private static final Locator EXCHANGE_RATE_NOTE = new Locator("EXCHANGE_RATE_NOTE",
            new By.ByXPath(ADD_TOKENS_SHEET_XPATH + "//*[contains(@class,'NotificationBanner_content')]"));

    /** The trade lists what the player holds first and what they are trading for second. */
    private static final Locator SHEET_POINTS_BALANCE = new Locator("SHEET_POINTS_BALANCE",
            new By.ByXPath("(" + TRADE_POINTS_CONTENT_XPATH
                    + "//*[contains(@class,'BlibliPointRedeemer_point_value')])[1]"));

    private static final Locator TRADE_MORE_TOKENS_BUTTON = new Locator("TRADE_MORE_TOKENS_BUTTON",
            new By.ByXPath("(" + TRADE_POINTS_CONTENT_XPATH
                    + "//button[contains(@class,'QuantityEditor_operation_button')])[last()]"));

    private static final Locator TOKENS_TO_TRADE_FIELD = new Locator("TOKENS_TO_TRADE_FIELD",
            new By.ByXPath(TRADE_POINTS_CONTENT_XPATH
                    + "//input[contains(@class,'QuantityEditor_quantity_input')]"));

    /** Carries what the trade on offer costs, as "Trade 3 points"; absent until tokens are dialled up. */
    private static final Locator TRADE_POINTS_BUTTON = new Locator("TRADE_POINTS_BUTTON",
            new By.ByXPath("//*[@data-testid='btnConfirmRedeemPoint']"));

    /**
     * The dialog the trade button raises to have the spend agreed to. Its buttons carry no label in the
     * markup and only the cancelling one is marked ({@code Figure_secondary}), so agreeing is the other.
     */
    private static final Locator TRADE_CONFIRMATION = new Locator("TRADE_CONFIRMATION",
            new By.ByXPath(TRADE_CONFIRMATION_XPATH));
    private static final Locator TRADE_CONFIRMATION_ACCEPT = new Locator("TRADE_CONFIRMATION_ACCEPT",
            new By.ByXPath(TRADE_CONFIRMATION_XPATH + "//*[contains(@class,'Figure_footer')]"
                    + "/button[not(contains(@class,'Figure_secondary'))]"));

    /**
     * The bottom sheet BTM raises over the hunt to announce the tokens it hands out — once a day, and
     * again after a transaction. Everything inside it is server copy from {@code token/v1/popup}, so its
     * container is what identifies it; the game's other sheets (the win sheet, for one) carry their own
     * module's classes and are left alone by this.
     */
    private static final Locator DAILY_TOKEN_POPUP = new Locator("DAILY_TOKEN_POPUP",
            new By.ByXPath("//*[contains(@class,'ActionBottomSheet_bottom_sheet_container')]"));

    /**
     * The sheet's own close control, taken in preference to the popup's "OK, got it!" button: it is
     * addressed through a {@code data-testid} rather than a hashed class, and closing is all it can ever
     * do, whereas the primary button is configured by the same payload that carries the copy (which
     * offers {@code popupButtonAction} and {@code popupButtonUrl}, unused by the current bundle). It is
     * scoped to the sheet holding the popup so that another sheet's close button cannot be picked up.
     */
    private static final Locator DAILY_TOKEN_POPUP_CLOSE = new Locator("DAILY_TOKEN_POPUP_CLOSE",
            new By.ByXPath("//*[contains(@class,'BottomSheet_bottom_sheet__')]"
                    + "[.//*[contains(@class,'ActionBottomSheet_bottom_sheet_container')]]"
                    + "//*[@data-testid='bottom-sheet-header']/button"));

    /**
     * On a campaign page these two are mutually exclusive and say who is holding the treasure: the
     * champion badge is shown when it is the logged-in player, the snatched badge when it is somebody
     * else. Being image alts they read the same in every locale.
     */
    private static final Locator PRIZE_HOLDER_BADGE = new Locator("PRIZE_HOLDER_BADGE",
            new By.ByXPath("//img[@alt='champion']"));
    private static final Locator TREASURE_STOLEN_BADGE = new Locator("TREASURE_STOLEN_BADGE",
            new By.ByXPath("//img[@alt='treasure-snatched']"));

    public GamificationPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> GamificationPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new GamificationPageDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new GamificationPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    /**
     * The host BTM is served from in production, which is the one thing the platforms disagree on: the
     * game itself is a single responsive bundle and the shared environments put it behind one host.
     */
    protected abstract String getProdBaseUrl();

    private String getGameBaseUrl() {
        return switch (Configs.ENVIRONMENT) {
            case GK -> STAGING_BASE_URL;
            case PREPROD -> PREPROD_BASE_URL;
            case PROD -> getProdBaseUrl();
        };
    }

    public void openGamePage() {
        driverTools.get(getGameUrl());
        driverTools.waitForWebToLoad();
        dismissDailyTokenPopupIfShown();
    }

    /**
     * Opens the hunt with the player recognised, planting the session again if it stays logged out.
     * <p>
     * The hunt works out who the player is in the browser — the server sends nothing but an empty shell —
     * and a load whose auth check does not come back leaves "log in to join the hunt" up for good, no
     * matter how long the page is then waited on. The session is not what was lost when this happens:
     * BTM's own player-state call answers for the cookie the framework plants. Asking for the page again
     * is what clears it, which {@link #openGamePageUpToRetryLimit()} does first.
     * <p>
     * Planting the session again is the step past that, for the run where the browser's session really
     * has stopped being accepted. The cookies have to be dropped for that to happen at all: a session
     * login that finds a live cookie for the same account settles for it rather than injecting a new
     * one. Every caller is here to check what a joined player sees, so there is nothing left to check
     * once the hunt insists on treating them as a guest — hence an assertion rather than a warning.
     */
    public void openGamePageAsPlayer(String email, String password)
            throws FailedLoginException, InterruptedException {
        if (openGamePageUpToRetryLimit()) {
            return;
        }

        logger.log("The hunt is still logged out, so the session is planted again");
        LoginPageWeb<T> loginPage = LoginPageWeb.getInstance(session);
        loginPage.clearSessionCookies();
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        Assertion.assertThat("Berburu Tiket Murah recognises the logged-in player. It kept serving the "
                        + "'log in to join the hunt' page through repeated loads and a freshly injected "
                        + "session, so either the gateway has stopped accepting the session cookie or the "
                        + "hunt's own backend is down.",
                openGamePageUpToRetryLimit(), is(true));
    }

    /**
     * Opens the hunt, asking for it again while it is served logged out, and reports whether it ended up
     * recognising the player. The page is loaded once and then at most {@value #GUEST_PAGE_RETRIES} times
     * more, so this gives up rather than waiting on a hunt that will not have the player.
     * <p>
     * The verdict is returned rather than asserted so that {@link #openGamePageAsPlayer(String, String)}
     * can go on to plant the session again.
     */
    private boolean openGamePageUpToRetryLimit() {
        openGamePage();
        for (int retry = 1; retry <= GUEST_PAGE_RETRIES; retry++) {
            if (!isLoginPromptDisplayed()) {
                return true;
            }
            logger.log("The hunt came up logged out; opening it again (" + retry + " of " + GUEST_PAGE_RETRIES + ")");
            openGamePage();
        }
        return !isLoginPromptDisplayed();
    }

    /**
     * Closes the token popup when the hunt is showing it.
     * <p>
     * The game announces the tokens it grants with a bottom sheet, so this is there on the first run of
     * a day and after a transaction, and cannot be waited for otherwise. It covers the campaign cards,
     * which is enough to swallow a native click on one, so it is cleared both on arriving at the hunt
     * and again before a campaign is picked — the popup follows its own request and can land after the
     * hunt has settled. Being absent costs a short look, and leaving it up would show as a failure
     * further along with nothing pointing back here.
     * <p>
     * The sheet is looked for by whether it is <em>on show</em>, not by whether it is in the DOM: the
     * game mounts it closed, so a presence check reports a popup that no close button is reachable on,
     * and this is the first thing every test does. A popup that then refuses to close is a warning
     * rather than a failure for the same reason — it is in the way of the hunt, not part of it.
     */
    public void dismissDailyTokenPopupIfShown() {
        if (!isVisibleWithin(DAILY_TOKEN_POPUP, POPUP_PROBE_SECONDS)) {
            return;
        }
        logger.log("Closing the BTM token popup");
        driverTools.waitForElementToAppear(DAILY_TOKEN_POPUP_CLOSE, ABSENCE_PROBE_SECONDS)
                .ifPresentOrElse(driverTools::clickOrFallbackToClickWithJavascriptExecutor,
                        () -> logger.softWarning("The BTM token popup is up but its close button never "
                                + "became clickable, so it has been left covering the hunt."));
        if (!driverTools.waitForElementToDisappear(DAILY_TOKEN_POPUP, ELEMENT_TIMEOUT_SECONDS)) {
            logger.softWarning("The BTM token popup is still up after its close button was clicked.");
        }
    }

    /**
     * Whether the hunt came up logged out.
     * <p>
     * The guest card and the player's own token counter are the two states the shell settles into, and
     * both are waited on together: watching only for the guest card makes a slow auth check look exactly
     * like a recognised player, and {@link #openGamePageUpToRetryLimit()} would then hand a page that
     * has painted neither back to its caller as a success.
     */
    public boolean isLoginPromptDisplayed() {
        Optional<Locator> settled = driverTools.waitForAnyLocator(
                PAGE_STATE_TIMEOUT_SECONDS, LOGIN_PROMPT_CARD, MY_TOKEN_CONTAINER);
        if (settled.isEmpty()) {
            logger.softWarning("The hunt showed neither the guest card nor a token counter on "
                    + driverTools.getCurrentURL() + ", so it is being treated as logged out.");
            return true;
        }
        return LOGIN_PROMPT_CARD.name().equals(settled.get().name());
    }

    /** True once the hunt lists at least one campaign that is still open for bidding. */
    public boolean isActiveCampaignListDisplayed() {
        boolean listed = driverTools.waitForElementToAppear(ACTIVE_CAMPAIGN_LIST, ELEMENT_TIMEOUT_SECONDS).isPresent()
                && driverTools.waitForElementToAppear(ACTIVE_CAMPAIGN_CARD, ELEMENT_TIMEOUT_SECONDS).isPresent();
        if (!listed) {
            logger.softWarning("The hunt is not listing active campaigns on " + driverTools.getCurrentURL());
        }
        return listed;
    }

    /** Opens the campaign whose card carries {@code campaignTitle} from the list of active campaigns. */
    public void openCampaign(String campaignTitle) {
        dismissDailyTokenPopupIfShown();
        Locator card = activeCampaignCard(campaignTitle);
        driverTools.waitForElementToAppear(card, ELEMENT_TIMEOUT_SECONDS)
                .ifPresentOrElse(driverTools::clickOrFallbackToClickWithJavascriptExecutor,
                        () -> Assertion.fail("No active campaign named '" + campaignTitle + "' on the hunt. "
                                + "Its campaign may have ended, in which case the test data sheet needs a "
                                + "campaign that is still running."));
        driverTools.waitForWebToLoad();
    }

    /**
     * True once the game has routed onto the given campaign <em>and</em> that campaign's data has
     * painted. The route lands after the page reports itself loaded, so the URL is polled; the rank
     * list then fills in a moment later, so the title is waited on as well.
     * Without that second wait every read on this page races the first paint.
     */
    public boolean waitForCampaignPage(String campaignId) {
        boolean routed = driverTools.waitUntil(
                d -> Optional.ofNullable(d.getCurrentUrl()).orElse("").contains(CAMPAIGN_PATH + campaignId),
                ROUTING_TIMEOUT_SECONDS).orElse(false);
        logger.log("Current BTM URL: " + driverTools.getCurrentURL());
        if (!routed) {
            return false;
        }
        return waitForNonBlankText(CAMPAIGN_TITLE, ROUTING_TIMEOUT_SECONDS).isPresent();
    }

    public String getCampaignTitle() {
        return waitForNonBlankText(CAMPAIGN_TITLE, ROUTING_TIMEOUT_SECONDS).orElse("");
    }

    /** How many tokens the player has left, read from whichever counter the current page shows. */
    public int getTokenBalance() {
        int balance = requireCount(waitForTokenCounter(),
                "Could not read the BTM token balance on " + driverTools.getCurrentURL());
        logger.log("BTM token balance: " + balance);
        return balance;
    }

    /** How many tokens this campaign charges for one bid, as advertised on the snatch button. */
    public int getSnatchTokenCost() {
        return requireCount(
                waitForNonBlankText(SNATCH_TREASURE_COST, ELEMENT_TIMEOUT_SECONDS)
                        .flatMap(GamificationPageWeb::parseCount),
                "Could not read how many tokens a bid costs from the snatch button.");
    }

    /**
     * True when the snatch button is offering a bid. The game renders it for everyone but greys it out
     * for the player already holding the treasure, so the class is what separates the two.
     */
    public boolean isSnatchTreasureButtonReady() {
        return driverTools.waitForElementToAppear(SNATCH_TREASURE_BUTTON, ELEMENT_TIMEOUT_SECONDS)
                .map(button -> {
                    String classes = Optional.ofNullable(driverTools.getAttribute(button, "class")).orElse("");
                    logger.log("Snatch button classes: " + classes);
                    return !classes.contains(INACTIVE_BUTTON_CLASS);
                })
                .orElse(false);
    }

    public void snatchTreasure() {
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(SNATCH_TREASURE_BUTTON);
    }

    /** True once the campaign page shows the logged-in player as the one holding the treasure. */
    public boolean isHoldingTreasure() {
        return driverTools.waitForElementToAppear(PRIZE_HOLDER_BADGE, BID_TIMEOUT_SECONDS).isPresent();
    }

    /**
     * True while the treasure sits with somebody else, which is what leaves it open to be snatched.
     * Sampled over a short window because this is asked where the badge is expected to have gone.
     */
    public boolean isTreasureHeldByAnotherPlayer() {
        return isVisibleWithin(TREASURE_STOLEN_BADGE, ABSENCE_PROBE_SECONDS);
    }

    /**
     * The waiting form of {@link #isTreasureHeldByAnotherPlayer()}, for the precondition that another
     * player has taken the treasure: that is arranged over the API just before the page is opened, so
     * the badge can trail the campaign's first paint by a moment. The plain check keeps its short window
     * for asserting the badge is <em>gone</em>, where waiting would only burn the timeout.
     */
    public boolean waitForTreasureToBeHeldByAnotherPlayer() {
        return driverTools.waitForElementToAppear(TREASURE_STOLEN_BADGE, ELEMENT_TIMEOUT_SECONDS).isPresent();
    }

    /** The masked name of the current holder, for the report; empty when nobody is shown as holding it. */
    public String getTreasureHolderName() {
        return driverTools.getText(TREASURE_HOLDER_NAME).map(String::trim).orElse("");
    }

    /**
     * The balance the token counter settles on once a bid has been charged. The game accepts the bid
     * before it refreshes the counter, so the value is polled until it moves off {@code previousBalance}
     * instead of read once; on timeout the last value read is returned so the caller can report it.
     */
    public int waitForTokenBalanceToChangeFrom(int previousBalance) {
        boolean changed = driverTools.waitUntil(
                d -> !peekTokenBalance().orElse(previousBalance).equals(previousBalance),
                BID_TIMEOUT_SECONDS).orElse(false);
        if (!changed) {
            logger.softWarning("Token balance stayed at " + previousBalance + " for "
                    + BID_TIMEOUT_SECONDS + "s after bidding.");
        }
        return getTokenBalance();
    }

    /**
     * Makes sure the player has at least {@code minimumTokens} to bid with, trading Blibli Tiket Points
     * for any shortfall, and returns the balance the hunt settles on.
     * <p>
     * BTM hands out its free tokens once a day, so a test that spends one on every run passes on the
     * first run of a day and then fails until somebody tops the account up by hand. Trading is the
     * game's own way of coming by more, and it asks nothing of the test data sheet: whatever points the
     * account holds are what the trade draws on, and coming up short is reported as such.
     */
    public int ensureTokensToBidWith(int minimumTokens) {
        int tokens = getTokenBalance();
        if (tokens >= minimumTokens) {
            return tokens;
        }

        logger.log("The player holds " + tokens + " tokens, so " + (minimumTokens - tokens)
                + " are being traded for out of their Blibli Tiket Points");
        buyTokensWithPoints(minimumTokens - tokens);

        openGamePage();
        int toppedUp = getTokenBalance();
        if (toppedUp < minimumTokens) {
            Assertion.fail("The player holds " + toppedUp + " of the " + minimumTokens + " tokens a bid "
                    + "needs even after trading points for them. The trade itself went through, so this is "
                    + "either the hunt not having credited the tokens yet or the account having no Blibli "
                    + "Tiket Points left to trade.");
        }
        return toppedUp;
    }

    /** Walks the "Add Tokens" sheet through a points trade for {@code tokens} tokens. */
    private void buyTokensWithPoints(int tokens) {
        openAddTokensSheet();
        if (!isAddTokensSheetDisplayed()) {
            Assertion.fail("The '+' beside the token counter did not open the 'Add Tokens' sheet, so the "
                    + "tokens this run needs cannot be traded for.");
        }
        if (!openTradePointsTab()) {
            Assertion.fail("The 'Add Tokens' sheet would not show the points trade, so the tokens this run "
                    + "needs cannot be traded for.");
        }
        setTokensToTrade(tokens);
        tradePointsForTokens();
    }

    /**
     * Opens the sheet the hunt offers for coming by more tokens, from the "+" beside the token counter.
     * The token popup is cleared first because it covers the counter.
     */
    public void openAddTokensSheet() {
        dismissDailyTokenPopupIfShown();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(ADD_TOKENS_BUTTON);
    }

    public boolean isAddTokensSheetDisplayed() {
        return driverTools.waitForElementToAppear(ADD_TOKENS_SHEET, TRADE_TIMEOUT_SECONDS).isPresent();
    }

    /** Asks "Add Tokens" for the points trade, and reports whether that is the tab it came up on. */
    public boolean openTradePointsTab() {
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(TRADE_POINTS_TAB);
        return driverTools.waitForElementToAppear(TRADE_POINTS_CONTENT, TRADE_TIMEOUT_SECONDS).isPresent();
    }

    /** How many points the sheet says a token costs: the first number of its rate note. */
    public int getPointsPerToken() {
        String note = waitForNonBlankText(EXCHANGE_RATE_NOTE, TRADE_TIMEOUT_SECONDS).orElse("");
        logger.log("Add Tokens rate note: " + note);
        return requireCount(parseLeadingCount(note),
                "Could not read what a token costs from the Add Tokens sheet. Its rate note reads: '" + note + "'");
    }

    /** How many Blibli Tiket Points the sheet credits the player with. */
    public int getPointsBalanceInSheet() {
        return requireCount(
                waitForNonBlankText(SHEET_POINTS_BALANCE, TRADE_TIMEOUT_SECONDS)
                        .flatMap(GamificationPageWeb::parseCount),
                "Could not read the player's points from the Add Tokens sheet.");
    }

    /**
     * Dials the trade up to {@code tokens}. Pressing "+" is the only way to set it — the quantity field
     * is disabled and cannot be typed into — and the field is read back afterwards because the sheet,
     * not the browser, is what turns those presses into a quantity.
     */
    public void setTokensToTrade(int tokens) {
        for (int press = 1; press <= tokens; press++) {
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(TRADE_MORE_TOKENS_BUTTON);
        }
        if (!waitForTokensToTrade(tokens)) {
            Assertion.fail("The Add Tokens sheet is set to trade for " + peekTokensToTrade().orElse(null)
                    + " tokens rather than " + tokens + " after that many presses of '+'. The stepper stops "
                    + "itself at nothing to trade, so it most likely also stops at what the player's points "
                    + "can cover — check the points landed before the hunt was opened.");
        }
    }

    /** What the trade being offered costs, as its button advertises it. */
    public int getTradeCostInPoints() {
        return requireCount(
                waitForNonBlankText(TRADE_POINTS_BUTTON, TRADE_TIMEOUT_SECONDS)
                        .flatMap(GamificationPageWeb::parseCount),
                "Could not read what the trade costs from the Add Tokens sheet. Its button only "
                        + "appears once tokens have been dialled up.");
    }

    /** Trades the points, seeing off the confirmation the game asks for before it spends them. */
    public void tradePointsForTokens() {
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(TRADE_POINTS_BUTTON);
        driverTools.waitForElementToAppear(TRADE_CONFIRMATION_ACCEPT, TRADE_TIMEOUT_SECONDS)
                .ifPresentOrElse(driverTools::clickOrFallbackToClickWithJavascriptExecutor,
                        () -> Assertion.fail("BTM never asked for the trade to be confirmed, so nothing was "
                                + "spent. Its trade button was pressed but the confirmation dialog did not open."));
        if (!driverTools.waitForElementToDisappear(TRADE_CONFIRMATION, TRADE_TIMEOUT_SECONDS)) {
            logger.softWarning("The trade confirmation is still up after it was agreed to.");
        }
    }

    /**
     * The points balance the sheet settles on once a trade has been charged. Like the token counter the
     * sheet is repainted after the trade goes through, so the value is polled rather than read once; on
     * timeout the last value read is returned so the caller can report it.
     */
    public int waitForPointsBalanceInSheetToChangeFrom(int previousBalance) {
        boolean changed = driverTools.waitUntil(
                d -> !peekPointsBalanceInSheet().orElse(previousBalance).equals(previousBalance),
                TRADE_TIMEOUT_SECONDS).orElse(false);
        if (!changed) {
            logger.softWarning("The points on the Add Tokens sheet stayed at " + previousBalance + " for "
                    + TRADE_TIMEOUT_SECONDS + "s after the trade.");
        }
        return getPointsBalanceInSheet();
    }

    private static Locator activeCampaignCard(String campaignTitle) {
        return new Locator("ACTIVE_CAMPAIGN_CARD[" + campaignTitle + "]", new By.ByXPath(
                ACTIVE_CAMPAIGN_CARD_XPATH + "[.//p[normalize-space()=" + asXpathLiteral(campaignTitle) + "]]"));
    }

    /** Campaign titles come from the test data sheet, so they may carry an apostrophe. */
    private static String asXpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        return "concat('" + value.replace("'", "',\"'\",'") + "')";
    }

    /**
     * The text an element settles on, rather than whatever it holds on first paint. The game mounts the
     * campaign shell before its hunt data arrives, so titles and labels exist as empty nodes for a
     * moment; reading them once returns {@code ""} on runs quick enough to get there first.
     */
    private Optional<String> waitForNonBlankText(Locator locator, int seconds) {
        Optional<String> text = driverTools.waitUntil(d -> {
            if (driverTools.firstPresentLocator(locator).isEmpty()) {
                return null;
            }
            String value = driverTools.getText(locator).orElse("");
            return value.isBlank() ? null : value.trim();
        }, seconds);
        if (text.isEmpty()) {
            logger.softWarning(locator.name() + " had no text after " + seconds + "s on "
                    + driverTools.getCurrentURL());
        }
        return text;
    }

    /**
     * Waits for whichever token counter the current page shows to carry a number. Both counters paint
     * before the player's data lands, so a single read can catch an empty counter and mistake it for an
     * unreadable balance.
     */
    private Optional<Integer> waitForTokenCounter() {
        return driverTools.waitUntil(d -> peekTokenBalance().orElse(null), ELEMENT_TIMEOUT_SECONDS);
    }

    /**
     * Instant read of whichever token counter is mounted. {@code firstPresentLocator} is used so a
     * miss is a sample, not a 5× retry with a soft warning — this is polled while a bid settles.
     */
    private Optional<Integer> peekTokenBalance() {
        return driverTools.firstPresentLocator(CAMPAIGN_TOKEN_BALANCE, GAME_TOKEN_BALANCE)
                .flatMap(counter -> parseCount(driverTools.getText(counter).orElse("")));
    }

    private boolean waitForTokensToTrade(int tokens) {
        return driverTools.waitUntil(
                d -> peekTokensToTrade().filter(count -> count == tokens).isPresent(),
                TRADE_TIMEOUT_SECONDS).orElse(false);
    }

    /**
     * Instant read of the stepper / sheet points, as {@link #peekTokenBalance()} does, while the
     * sheet settles.
     */
    private Optional<Integer> peekTokensToTrade() {
        if (driverTools.firstPresentLocator(TOKENS_TO_TRADE_FIELD).isEmpty()) {
            return Optional.empty();
        }
        return parseCount(driverTools.getValue(TOKENS_TO_TRADE_FIELD));
    }

    private Optional<Integer> peekPointsBalanceInSheet() {
        if (driverTools.firstPresentLocator(SHEET_POINTS_BALANCE).isEmpty()) {
            return Optional.empty();
        }
        return parseCount(driverTools.getText(SHEET_POINTS_BALANCE).orElse(""));
    }

    /**
     * Whether {@code locator} is on show within {@code seconds}.
     * <p>
     * Timeout here is not a failure: this is also asked where absence is the expected answer, so
     * {@code waitForElementToAppear} would hang a warning off a passing run. Visibility rather than
     * presence because BTM mounts its bottom sheets and badges closed or hidden, so being in the DOM
     * says nothing about being in front of the player.
     */
    private boolean isVisibleWithin(Locator locator, int seconds) {
        return driverTools.waitUntil(d -> driverTools.isElementDisplayed(locator), seconds).orElse(false);
    }

    /**
     * The number that was read, failing with {@code failureMessage} when there was none.
     * <p>
     * {@link Assertion#fail(String)} throws, so the return below it is unreachable; keeping the shape in
     * one place is what stops every reader on this page from carrying its own dead line.
     */
    private static int requireCount(Optional<Integer> count, String failureMessage) {
        if (count.isEmpty()) {
            Assertion.fail(failureMessage);
        }
        return count.orElse(0);
    }

    /** Pulls the number out of counters and labels such as {@code "Use 1 token"}. */
    private static Optional<Integer> parseCount(String text) {
        String digits = Optional.ofNullable(text).orElse("").replaceAll("[^0-9]", "");
        return digits.isEmpty() ? Optional.empty() : Optional.of(Integer.parseInt(digits));
    }

    /**
     * The leading number of copy that holds more than one, such as the 3 of "Trade 3 Blibli Tiket Points
     * to get 1 token." — where {@link #parseCount(String)} would run the two together into 31.
     */
    private static Optional<Integer> parseLeadingCount(String text) {
        Matcher number = LEADING_COUNT.matcher(text);
        return number.find() ? parseCount(number.group()) : Optional.empty();
    }

    private String getGameUrl() {
        String baseUrl = getGameBaseUrl();
        return baseUrl.endsWith("/") ? baseUrl + GAME_PATH : baseUrl + "/" + GAME_PATH;
    }
}
