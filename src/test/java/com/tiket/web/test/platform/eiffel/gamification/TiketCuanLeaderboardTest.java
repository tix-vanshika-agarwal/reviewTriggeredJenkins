package com.tiket.web.test.platform.eiffel.gamification;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.platform.eiffel.gamification.TiketCuanLeaderboardPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static com.tiket.web.page.platform.eiffel.gamification.TiketCuanLeaderboardPageWeb.TIER_ORDER;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class TiketCuanLeaderboardTest extends WebTest {

    @Author(names = {"Yanuar"})
    @Module(name = "Gamification")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void verifyCuanAffiliateStatusCardAndTierLeaderboardsWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyCuanAffiliateStatusCardAndTierLeaderboardsWeb");
        BaseTest.paramsThreadLocal.set(param);

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        TiketCuanLeaderboardPageWeb<?> leaderboardPage = TiketCuanLeaderboardPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");

        step("Given a registered affiliate is logged in");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("When the affiliate opens the tiket CUAN leaderboard");
        boolean affiliateRecognised = leaderboardPage.openLeaderboardAsAffiliate(email, password);

        step("Then the leaderboard greets them with their own participant status card");
        Assertion.assertThat("The affiliate is met by their status card. The leaderboard shows it only to an "
                        + "account the affiliate programme has registered, and it kept serving the guest half "
                        + "of the page through repeated loads and a freshly injected session — so either the "
                        + "session is not being carried onto the page or the account is no longer enrolled.",
                affiliateRecognised, is(true));

        String displayName = leaderboardPage.getStatusCardName();
        String cardTier = leaderboardPage.getStatusCardTier();
        int cardOrders = leaderboardPage.getStatusCardOrderCount();
        String incentiveBadge = leaderboardPage.getStatusCardIncentiveBadge();
        step("Status card -> name='" + displayName + "', tier='" + cardTier + "', completed orders="
                + cardOrders + ", earned incentive='" + incentiveBadge + "'");

        step("And the card is made out to the affiliate, with their tier, orders and earned incentive on it");
        Assertion.assertThat("The status card carries the account display name",
                displayName, is(not(emptyOrNullString())));
        Assertion.assertThat("The status card names the tier the affiliate is currently in",
                cardTier, is(not(emptyOrNullString())));
        Assertion.assertThat("The status card carries the incentive the affiliate has earned so far",
                incentiveBadge, is(not(emptyOrNullString())));

        step("And a nudge under the card says how many more orders the next incentive is worth");
        TiketCuanLeaderboardPageWeb.ProgressNudge nudge = leaderboardPage.readProgressNudge();
        step("Progress nudge -> '" + nudge.text() + "'");
        Assertion.assertThat("The nudge under the status card counts the orders still to go before the next "
                        + "incentive, so it has to name a number. It reads '" + nudge.text() + "'.",
                nudge.ordersToGo(), greaterThan(0));

        step("And the page has stopped pitching the programme at them");
        Assertion.assertThat("The non-participant tagline is gone. It sells the programme to accounts that "
                        + "have not joined, so showing it to a registered affiliate means the page has not "
                        + "recognised them.",
                leaderboardPage.isNonParticipantTaglineDisplayed(), is(false));
        Assertion.assertThat("The 'Join Affiliate' button is gone. An affiliate who has already joined must "
                        + "not be asked to join again.",
                leaderboardPage.isJoinAffiliateCtaDisplayed(), is(false));

        step("And the five tiers are stacked in order: " + String.join(", ", TIER_ORDER));
        TiketCuanLeaderboardPageWeb.TierBoard tierBoard = leaderboardPage.readTierBoard();
        step("Tier board -> " + tierBoard.summary());
        Assertion.assertThat("The leaderboard renders its tier sections best-paying first. A different order "
                        + "or a missing tier changes which leaderboard an affiliate reads themselves into.",
                tierBoard.titles(), contains(TIER_ORDER.toArray(new String[0])));

        step("And every tier shows its badge, the incentive it pays and a table of ranked affiliates");
        Assertion.assertThat("Every tier shows the badge icon that goes with it",
                tierBoard.tiersMissingBadge(), is(empty()));
        Assertion.assertThat("Every tier advertises the incentive it pays",
                tierBoard.tiersMissingIncentive(), is(empty()));
        Assertion.assertThat("Every tier ranks at least one affiliate. An empty table is either a tier the "
                        + "standings have dropped or a table that never finished filling, and both leave the "
                        + "leaderboard with nobody to read under that tier.",
                tierBoard.tiersRankingNobody(), is(empty()));
        Assertion.assertThat("Every affiliate a tier lists is set against a completed-order count, and every "
                        + "count against an affiliate — a row carrying one without the other is a row a "
                        + "reader cannot rank.",
                tierBoard.tiersWithUnpairedRows(), is(empty()));

        step("And the affiliate finds their own row in their own tier's table");
        Assertion.assertThat("'" + displayName + "' is listed in the " + cardTier + " table — the tier their "
                        + "status card puts them in. Being ranked under a different tier than the card claims "
                        + "means the two are reading different standings.",
                leaderboardPage.getTierContainingAffiliate(displayName), is(cardTier));

        step("And that row agrees with the count on their status card");
        Assertion.assertThat("The completed orders on the status card (" + cardOrders + ") are the same "
                        + "orders the " + cardTier + " table ranks '" + displayName + "' by. The card and the "
                        + "leaderboard are fed by the same standings, so a mismatch means one of them is stale.",
                leaderboardPage.getOrderCountInTierTable(displayName), is(cardOrders));
    }
}
