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
import org.openqa.selenium.WebElement;

import javax.security.auth.login.FailedLoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Top Affiliates, the tiket CUAN affiliate leaderboard served under {@code /game}.
 * <p>
 * The page settles into one of two states. A registered affiliate is met by their own status card —
 * display name, tier, completed orders, the incentive they have earned — and a nudge saying how many
 * more orders the next incentive is worth. Everybody else gets the tagline and the "Join Affiliate"
 * button in its place. Both are followed by the same five tier sections, each with its badge, its
 * title, the incentive that tier pays and a table of affiliates against completed orders.
 * <p>
 * Split per platform the way {@link GamificationPageWeb} is, and for the same reason: the leaderboard
 * is one responsive bundle, so the production host and the window it is read at are all a subclass has
 * to name. The markup itself does not vary — at a phone's width the tier sections keep their
 * {@code data-testid}s and the cards their module classes — so every locator below is shared.
 */
public abstract class TiketCuanLeaderboardPageWeb<T extends WebDriver> extends WebPage<T> {

    /**
     * {@code test=true} is what the test data sheet's URL carries and what the case was written
     * against: it pins the leaderboard to the seeded affiliates rather than whatever the live feed
     * holds that day, so the counts a run reads stay the ones it can reason about.
     */
    private static final String LEADERBOARD_PATH = "game/tiketcuan-affiliate/leaderboard?test=true";

    private static final String STAGING_BASE_URL = "https://gatotkaca.tiket.com/";
    private static final String PREPROD_BASE_URL = "https://preprod.tiket.com/";

    /**
     * The tiers as the leaderboard stacks their sections, best-paying first. The programme's own ladder
     * rather than anything a run varies, so it is published here and read from the test.
     */
    public static final List<String> TIER_ORDER = List.of("Crystal", "Ruby", "Emerald", "Sapphire", "Amethyst");

    private static final int ELEMENT_TIMEOUT_SECONDS = 15;

    /**
     * How long the page is given to settle on one of its two halves. Shorter than the element budget
     * because it is spent once per attempt: a half that has not painted in this long is one that the
     * next load has a better chance with than the rest of this wait does.
     */
    private static final int PAGE_STATE_TIMEOUT_SECONDS = 10;

    /**
     * How many further times the leaderboard is asked for when it comes up not knowing the affiliate.
     * Two is enough for the render that missed the session; anything beyond that is not a rendering
     * blip.
     */
    private static final int GUEST_RENDER_RETRIES = 2;

    /**
     * The window for a look that expects to find nothing — the join CTA, once the affiliate has been
     * recognised. Giving that the full element budget would add dead waiting to a passing run.
     */
    private static final int ABSENCE_PROBE_SECONDS = 3;

    /** A number as the page writes it, grouping separators and all, so "1,200" is not read as 1. */
    private static final Pattern LEADING_COUNT = Pattern.compile("\\d[\\d.,]*");

    private static final String STATUS_CARD_XPATH = "//section[@data-testid='tiketcuan-user-status-card']";
    private static final String JOIN_CTA_XPATH = "//section[@data-testid='tiketcuan-join-affiliate-cta']";
    private static final String TIER_TESTID_PREFIX = "tiketcuan-tier-";
    private static final String TIER_SECTION_XPATH =
            "//section[starts-with(@data-testid,'" + TIER_TESTID_PREFIX + "')]";
    private static final String TIER_TITLE_XPATH = "//h2[contains(@class,'TierSection_name')]";
    private static final String ROW_XPATH = "//div[contains(@class,'RankingTable_row')]";
    private static final String ROW_NAME_XPATH = "//span[contains(@class,'RankingTable_name')]";

    /**
     * The completed-order count of a row, taken as the cell that is not the name: the two spans carry
     * only styling classes otherwise, and neither the row nor the cells are marked up as a table.
     */
    private static final String ROW_ORDER_COUNT_XPATH = "/span[not(contains(@class,'RankingTable_name'))]";

    private static final Locator USER_STATUS_CARD = new Locator("USER_STATUS_CARD",
            new By.ByXPath(STATUS_CARD_XPATH));
    private static final Locator STATUS_CARD_NAME = new Locator("STATUS_CARD_NAME",
            new By.ByXPath(STATUS_CARD_XPATH + "//span[contains(@class,'UserStatusCard_name')]"));
    private static final Locator STATUS_CARD_TIER = new Locator("STATUS_CARD_TIER",
            new By.ByXPath(STATUS_CARD_XPATH + "//div[contains(@class,'UserStatusCard_tier')]/span"));

    /**
     * The card's two stats sit in identical containers, so each is addressed by the icon beside it
     * rather than by its position: the orders count by the purchase-list icon, the incentive the
     * affiliate has earned by the loyalty-point one. Being image alts they read the same in every
     * locale, and they survive the two swapping places.
     */
    private static final Locator STATUS_CARD_ORDERS = new Locator("STATUS_CARD_ORDERS",
            new By.ByXPath(STATUS_CARD_XPATH + "//div[contains(@class,'UserStatusCard_stat')][.//img[@alt='purchase list']]/span"));
    private static final Locator STATUS_CARD_INCENTIVE = new Locator("STATUS_CARD_INCENTIVE",
            new By.ByXPath(STATUS_CARD_XPATH + "//div[contains(@class,'UserStatusCard_stat')][.//img[@alt='loyalty point']]/span"));

    private static final Locator PROGRESS_NUDGE = new Locator("PROGRESS_NUDGE",
            new By.ByXPath(STATUS_CARD_XPATH + "//div[contains(@class,'UserStatusCard_progress')]/span"));

    /**
     * The guest half of the page. Both are addressed by their own module's classes rather than by
     * their copy or the button's {@code aria-label}: these are asked where the answer is expected to
     * be "not there", and a locator that only matches the English wording would answer that way on an
     * Indonesian render too.
     */
    private static final Locator NON_PARTICIPANT_TAGLINE = new Locator("NON_PARTICIPANT_TAGLINE",
            new By.ByXPath(JOIN_CTA_XPATH + "//span[contains(@class,'JoinAffiliateCta_tagline')]"));
    private static final Locator JOIN_AFFILIATE_CTA = new Locator("JOIN_AFFILIATE_CTA",
            new By.ByXPath(JOIN_CTA_XPATH + "//button[contains(@class,'JoinAffiliateCta_cta')]"));

    private static final Locator TIER_SECTIONS = new Locator("TIER_SECTIONS",
            new By.ByXPath(TIER_SECTION_XPATH));

    /**
     * The parts of a tier section, and of a row within it. These are read inside an element that has
     * already been found, so each is relative: the leading {@code .} is what keeps a row's count
     * coming from that row rather than from the first row on the page.
     */
    private static final Locator TIER_TITLE = new Locator("TIER_TITLE", new By.ByXPath("." + TIER_TITLE_XPATH));
    private static final Locator TIER_BADGE = new Locator("TIER_BADGE",
            new By.ByXPath(".//img[contains(@class,'TierSection_icon')]"));
    private static final Locator TIER_INCENTIVE = new Locator("TIER_INCENTIVE",
            new By.ByXPath(".//span[contains(@class,'TierSection_incentive')]"));
    private static final Locator TIER_ROWS = new Locator("TIER_ROWS", new By.ByXPath("." + ROW_XPATH));
    private static final Locator ROW_NAME = new Locator("ROW_NAME", new By.ByXPath("." + ROW_NAME_XPATH));
    private static final Locator ROW_ORDER_COUNT = new Locator("ROW_ORDER_COUNT",
            new By.ByXPath("." + ROW_ORDER_COUNT_XPATH));

    public TiketCuanLeaderboardPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TiketCuanLeaderboardPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new TiketCuanLeaderboardPageDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new TiketCuanLeaderboardPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    /**
     * The host the leaderboard is served from in production, which the platforms spell differently. The
     * shared environments put it behind one host, so this is only asked for on a production run.
     */
    protected abstract String getProdBaseUrl();

    /**
     * Sizes the browser window for the layout the platform is read at, before the leaderboard is asked
     * for. Desktop web has this to do because a headless Chrome starts narrow enough to be served the
     * mobile layout; on mobile web the device's own viewport is the layout.
     */
    protected abstract void sizeWindowForLayout();

    /** The nudge under the status card: its copy, and the orders it says are still to go. */
    public record ProgressNudge(String text, int ordersToGo) {
    }

    /** A row of a tier's ranking table: the affiliate it lists, against the orders it ranks them by. */
    public record TierRow(String affiliateName, String completedOrders) {

        /** Whether the row is carrying an affiliate without a count, or a count without an affiliate. */
        public boolean isUnpaired() {
            return affiliateName.isBlank() || completedOrders.isBlank();
        }
    }

    /**
     * A tier section as the page rendered it. {@code name} is the tier as its {@code data-testid}
     * spells it, which is the identity that survives a change of display language; {@code title} is
     * the heading a reader sees.
     */
    public record TierSection(String name, String title, boolean badgeDisplayed, String incentive,
                              List<TierRow> rows) {
    }

    /**
     * The tier half of the leaderboard, read in one pass.
     * <p>
     * What fell short is reported as the tiers it happened in, so a test can state what has to hold of
     * every tier without walking them itself, and a failure names the tier rather than leaving it to be
     * found in the log.
     */
    public record TierBoard(List<TierSection> sections) {

        /** The tier headings in the order the page stacked their sections. */
        public List<String> titles() {
            return sections.stream().map(TierSection::title).toList();
        }

        /** The tiers that painted no badge icon. */
        public List<String> tiersMissingBadge() {
            return tiersWhere(section -> !section.badgeDisplayed());
        }

        /** The tiers that advertise no incentive. */
        public List<String> tiersMissingIncentive() {
            return tiersWhere(section -> section.incentive().isBlank());
        }

        /** The tiers whose ranking table came up with nobody in it. */
        public List<String> tiersRankingNobody() {
            return tiersWhere(section -> section.rows().isEmpty());
        }

        /** The tiers holding a row that lists an affiliate and a count but not both. */
        public List<String> tiersWithUnpairedRows() {
            return tiersWhere(section -> section.rows().stream().anyMatch(TierRow::isUnpaired));
        }

        /** How many affiliates each tier ranked, for the run log. */
        public String summary() {
            return sections.stream()
                    .map(section -> section.title() + ": " + section.rows().size() + " affiliates")
                    .collect(Collectors.joining(", "));
        }

        private List<String> tiersWhere(Predicate<TierSection> fellShort) {
            return sections.stream().filter(fellShort).map(TierSection::name).toList();
        }
    }

    /**
     * Opens the leaderboard with the affiliate recognised, planting the session again if it keeps
     * coming up as the guest half of the page, and reports whether it ended up knowing them.
     * <p>
     * The leaderboard shares its bundle with the rest of {@code /game}, which is served logged out
     * often enough that {@link GamificationPageWeb} carries the same recovery: asking for the page
     * again clears the render that missed the session, and dropping the cookies before logging in
     * again is what makes the second attempt a fresh session rather than a settle for the live one.
     * <p>
     * The verdict is returned rather than asserted so the test keeps its own "Then" — there is nothing
     * left for it to check once the leaderboard insists on treating the affiliate as a guest.
     */
    public boolean openLeaderboardAsAffiliate(String email, String password)
            throws FailedLoginException, InterruptedException {
        if (openLeaderboardUpToRetryLimit()) {
            return true;
        }

        logger.log("The leaderboard is still showing its guest half, so the session is planted again");
        LoginPageWeb<T> loginPage = LoginPageWeb.getInstance(session);
        loginPage.clearSessionCookies();
        loginPage.loginWithSpecificAccountViaCookie(email, password);
        return openLeaderboardUpToRetryLimit();
    }

    /** The account display name the status card is made out to. */
    public String getStatusCardName() {
        return readText(STATUS_CARD_NAME, "the display name on the status card");
    }

    /** The tier the status card puts the affiliate in, e.g. {@code "Ruby"}. */
    public String getStatusCardTier() {
        return readText(STATUS_CARD_TIER, "the tier on the status card");
    }

    /** Completed orders as the status card counts them, read from its "160 Orders" stat. */
    public int getStatusCardOrderCount() {
        String stat = readText(STATUS_CARD_ORDERS, "the completed-order count on the status card");
        return requireCount(parseCount(stat),
                "The status card's order stat reads '" + stat + "', which carries no number.");
    }

    /** The incentive the affiliate has already earned, as the card's badge advertises it. */
    public String getStatusCardIncentiveBadge() {
        return readText(STATUS_CARD_INCENTIVE, "the earned-incentive badge on the status card");
    }

    /**
     * The nudge under the card, read once: "You need 840 orders to get the next incentive!" together
     * with the 840 in it, which is the first number its copy carries in either language.
     */
    public ProgressNudge readProgressNudge() {
        String text = readText(PROGRESS_NUDGE, "the progress nudge under the status card");
        return new ProgressNudge(text, requireCount(parseLeadingCount(text),
                "The progress nudge reads '" + text + "', so it does not say how many more orders the "
                        + "next incentive needs."));
    }

    /** True while the page is still pitching the programme with its non-participant tagline. */
    public boolean isNonParticipantTaglineDisplayed() {
        return isVisibleWithin(NON_PARTICIPANT_TAGLINE);
    }

    /** True while the page is still offering the "Join Affiliate" button. */
    public boolean isJoinAffiliateCtaDisplayed() {
        return isVisibleWithin(JOIN_AFFILIATE_CTA);
    }

    /** Every tier section and what it carried, taken once the tables have stopped filling. */
    public TierBoard readTierBoard() {
        List<TierSection> sections = new ArrayList<>();
        for (WebElement section : waitForTierTablesToFill()) {
            TierSection tier = readTierSection(section);
            logger.log("Tier " + tier.name() + " -> title='" + tier.title() + "', badge="
                    + tier.badgeDisplayed() + ", incentive='" + tier.incentive() + "', rows=" + tier.rows());
            sections.add(tier);
        }
        return new TierBoard(List.copyOf(sections));
    }

    /**
     * Completed orders as the leaderboard itself counts them for {@code affiliateName}, read from
     * whichever tier table lists that affiliate.
     */
    public int getOrderCountInTierTable(String affiliateName) {
        String count = readText(affiliateRowOrderCount(affiliateName),
                "the completed-order count listed against '" + affiliateName + "' in its tier table");
        return requireCount(parseCount(count),
                "The tier table lists '" + count + "' against '" + affiliateName + "', which carries no number.");
    }

    /** The tier whose table lists {@code affiliateName}. */
    public String getTierContainingAffiliate(String affiliateName) {
        return readText(tierTitleContainingAffiliate(affiliateName),
                "the tier whose table lists '" + affiliateName + "'");
    }

    /** Opens the leaderboard, whichever half of the page it decides to serve. */
    private void openLeaderboard() {
        sizeWindowForLayout();
        String url = getLeaderboardUrl();
        logger.log("Opening the tiket CUAN leaderboard: " + url);
        driverTools.get(url);
        driverTools.waitForWebToLoad();
    }

    /**
     * Opens the leaderboard, asking for it again while it comes up as the guest half, and reports
     * whether it ended up recognising the affiliate. The page is loaded once and then at most
     * {@value #GUEST_RENDER_RETRIES} times more, so this gives up rather than waiting on a leaderboard
     * that will not have the affiliate.
     */
    private boolean openLeaderboardUpToRetryLimit() {
        openLeaderboard();
        for (int retry = 1; retry <= GUEST_RENDER_RETRIES; retry++) {
            if (isAffiliateRecognised()) {
                return true;
            }
            logger.log("The leaderboard came up as a guest; opening it again ("
                    + retry + " of " + GUEST_RENDER_RETRIES + ")");
            openLeaderboard();
        }
        return isAffiliateRecognised();
    }

    /**
     * Whether the page settled on the affiliate's own status card rather than the join CTA.
     * <p>
     * Both halves are waited for together so that a page which painted neither is reported as not
     * recognising the affiliate, rather than being read as a status card that is merely slow.
     */
    private boolean isAffiliateRecognised() {
        Optional<Locator> settled = driverTools.waitForAnyLocator(
                PAGE_STATE_TIMEOUT_SECONDS, USER_STATUS_CARD, JOIN_AFFILIATE_CTA);
        if (settled.isEmpty()) {
            logger.softWarning("The leaderboard painted neither a status card nor the join CTA on "
                    + driverTools.getCurrentURL() + ", so it is being treated as not knowing the affiliate.");
            return false;
        }
        return USER_STATUS_CARD.name().equals(settled.get().name());
    }

    /**
     * The tier sections, waited on until every one of them has rows in its table.
     * <p>
     * The wait is the point. The sections mount with their heading before the standings arrive, and a
     * plain look at a table that has not filled yet reads as a tier ranking nobody — which a check
     * comparing the affiliates it found against the counts it found would call a pass. Whatever is
     * mounted is returned when the wait runs out, so the shortfall is reported by the tier it is in.
     */
    private List<WebElement> waitForTierTablesToFill() {
        Optional<List<WebElement>> filled = driverTools.waitUntil(d -> {
            List<WebElement> sections = tierSections();
            if (sections.size() < TIER_ORDER.size()) {
                return null;
            }
            return sections.stream().allMatch(section -> !rowsIn(section).isEmpty()) ? sections : null;
        }, ELEMENT_TIMEOUT_SECONDS);

        if (filled.isPresent()) {
            return filled.get();
        }
        logger.softWarning("Not every tier had filled its ranking table after " + ELEMENT_TIMEOUT_SECONDS
                + "s on " + driverTools.getCurrentURL());
        return tierSections();
    }

    private List<WebElement> tierSections() {
        return driverTools.elements(TIER_SECTIONS).orElseGet(List::of);
    }

    private List<WebElement> rowsIn(WebElement section) {
        return driverTools.elements(TIER_ROWS, section).orElseGet(List::of);
    }

    private TierSection readTierSection(WebElement section) {
        boolean badgeDisplayed = driverTools.elements(TIER_BADGE, section).orElseGet(List::of).stream()
                .anyMatch(driverTools::isElementDisplayed);
        List<TierRow> rows = rowsIn(section).stream()
                .map(row -> new TierRow(textIn(row, ROW_NAME), textIn(row, ROW_ORDER_COUNT)))
                .toList();
        return new TierSection(tierNameOf(section), textIn(section, TIER_TITLE), badgeDisplayed,
                textIn(section, TIER_INCENTIVE), rows);
    }

    /** The tier a section is for, taken from the {@code data-testid} that is not translated. */
    private String tierNameOf(WebElement section) {
        String testId = Optional.ofNullable(driverTools.getAttribute(section, "data-testid")).orElse("");
        return testId.startsWith(TIER_TESTID_PREFIX) ? testId.substring(TIER_TESTID_PREFIX.length()) : testId;
    }

    /** The text {@code locator} carries inside {@code parent}, and {@code ""} where it carries none. */
    private String textIn(WebElement parent, Locator locator) {
        return driverTools.elements(locator, parent).orElseGet(List::of).stream()
                .findFirst()
                .map(driverTools::getText)
                .orElse("")
                .trim();
    }

    private static Locator affiliateRowOrderCount(String affiliateName) {
        return new Locator("AFFILIATE_ROW_ORDER_COUNT[" + affiliateName + "]",
                new By.ByXPath(ROW_XPATH + rowOfPredicate(affiliateName) + ROW_ORDER_COUNT_XPATH));
    }

    private static Locator tierTitleContainingAffiliate(String affiliateName) {
        return new Locator("TIER_OF_AFFILIATE[" + affiliateName + "]",
                new By.ByXPath(TIER_SECTION_XPATH + "[." + ROW_NAME_XPATH
                        + "[normalize-space()=" + asXpathLiteral(affiliateName) + "]]" + TIER_TITLE_XPATH));
    }

    private static String rowOfPredicate(String affiliateName) {
        return "[." + ROW_NAME_XPATH + "[normalize-space()=" + asXpathLiteral(affiliateName) + "]]";
    }

    /** Affiliate names are read off the page, so they may carry an apostrophe. */
    private static String asXpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        return "concat('" + value.replace("'", "',\"'\",'") + "')";
    }

    /**
     * The text an element settles on, failing with what was being read when it never carries any. The
     * leaderboard mounts its shell before the affiliate data lands, so a single read can catch a node
     * that exists but is still empty.
     */
    private String readText(Locator locator, String what) {
        Optional<String> text = driverTools.waitUntil(d -> {
            if (driverTools.firstPresentLocator(locator).isEmpty()) {
                return null;
            }
            String value = driverTools.getText(locator).orElse("");
            return value.isBlank() ? null : value.trim();
        }, ELEMENT_TIMEOUT_SECONDS);
        if (text.isEmpty()) {
            Assertion.fail("Could not read " + what + " on " + driverTools.getCurrentURL()
                    + ". The leaderboard never painted it (" + locator.name() + ").");
        }
        return text.orElse("");
    }

    /**
     * Whether {@code locator} is on show within the absence window. Timeout is not a failure here:
     * this is asked where absence is the expected answer, so {@code waitForElementToAppear} would hang
     * a warning off a passing run.
     */
    private boolean isVisibleWithin(Locator locator) {
        return driverTools.waitUntil(d -> driverTools.isElementDisplayed(locator), ABSENCE_PROBE_SECONDS)
                .orElse(false);
    }

    /**
     * The number that was read, failing with {@code failureMessage} when there was none.
     * {@link Assertion#fail(String)} throws, so the return below it is unreachable.
     */
    private static int requireCount(Optional<Integer> count, String failureMessage) {
        if (count.isEmpty()) {
            Assertion.fail(failureMessage);
        }
        return count.orElse(0);
    }

    /** Pulls the number out of a cell or stat such as {@code "1,200"} or {@code "160 Orders"}. */
    private static Optional<Integer> parseCount(String text) {
        String digits = Optional.ofNullable(text).orElse("").replaceAll("[^0-9]", "");
        return digits.isEmpty() ? Optional.empty() : Optional.of(Integer.parseInt(digits));
    }

    /**
     * The leading number of copy that holds more than one, such as the 840 of "You need 840 orders to
     * get the next incentive!" — where {@link #parseCount(String)} would run them together.
     */
    private static Optional<Integer> parseLeadingCount(String text) {
        Matcher number = LEADING_COUNT.matcher(Optional.ofNullable(text).orElse(""));
        return number.find() ? parseCount(number.group()) : Optional.empty();
    }

    private String getLeaderboardUrl() {
        String baseUrl = switch (Configs.ENVIRONMENT) {
            case GK -> STAGING_BASE_URL;
            case PREPROD -> PREPROD_BASE_URL;
            case PROD -> getProdBaseUrl();
        };
        return baseUrl + LEADERBOARD_PATH;
    }
}
