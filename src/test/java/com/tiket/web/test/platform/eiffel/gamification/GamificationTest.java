package com.tiket.web.test.platform.eiffel.gamification;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.GamificationApi;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.platform.eiffel.gamification.GamificationPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static com.tiket.web.page.platform.eiffel.gamification.GamificationPageWeb.TOKENS_NEEDED_TO_BID;
import static com.tiket.web.page.platform.eiffel.gamification.GamificationPageWeb.TOKENS_TO_TRADE;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

public class GamificationTest extends WebTest {

    @Author(names = {"Yanuar"})
    @Module(name = "Gamification")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void verifyBtmSnatchProductWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyBtmSnatchProductWeb");
        BaseTest.paramsThreadLocal.set(param);

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GamificationPageWeb<?> gamificationPage = GamificationPageWeb.getInstance(session);
        GamificationApi<?> gamificationApi = new GamificationApi<>(session);

        String email = param.get("email");
        String password = param.get("password");
        String campaignId = param.get("campaignId");
        String campaignTitle = param.get("campaignTitle");
        String sessionId = param.get("sessionId");
        long otherAccountId = Long.parseLong(param.get("otherAccountId"));

        step("Given a logged-in user");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("When the user opens Berburu Tiket Murah");
        gamificationPage.openGamePageAsPlayer(email, password);

        step("Then the user has a token to bid with");
        int tokensBeforeBid = gamificationPage.ensureTokensToBidWith(TOKENS_NEEDED_TO_BID);
        Assertion.assertThat("The account has tokens to bid with. A bid costs one, so an account that has spent "
                        + "them all cannot snatch anything and this run would have nothing to verify.",
                tokensBeforeBid, greaterThanOrEqualTo(TOKENS_NEEDED_TO_BID));

        step("And another player is holding the treasure, so it is there to be snatched");
        gamificationApi.snatchTreasureAs(otherAccountId, sessionId);

        step("And the hunt shows the campaigns that are still running");
        Assertion.assertThat("The hunt lists active campaigns",
                gamificationPage.isActiveCampaignListDisplayed(), is(true));

        step("When the user opens the campaign '" + campaignTitle + "'");
        gamificationPage.openCampaign(campaignTitle);

        step("Then the campaign is open with its treasure up for grabs");
        Assertion.assertThat("The chosen campaign is opened",
                gamificationPage.waitForCampaignPage(campaignId), is(true));
        Assertion.assertThat("The campaign page is the one that was picked",
                gamificationPage.getCampaignTitle(), is(campaignTitle));
        Assertion.assertThat("The treasure is held by another player",
                gamificationPage.waitForTreasureToBeHeldByAnotherPlayer(), is(true));
        Assertion.assertThat("The snatch button is offering a bid",
                gamificationPage.isSnatchTreasureButtonReady(), is(true));

        step("When the user spends a token to snatch the treasure");
        int tokenCost = gamificationPage.getSnatchTokenCost();
        gamificationPage.snatchTreasure();

        step("Then the user takes the treasure and is charged for the bid");
        boolean holdingTreasure = gamificationPage.isHoldingTreasure();
        Assertion.assertThat("The user is now holding the treasure (holder shown: "
                        + gamificationPage.getTreasureHolderName() + ")",
                holdingTreasure, is(true));
        Assertion.assertThat("The treasure is no longer held by another player",
                gamificationPage.isTreasureHeldByAnotherPlayer(), is(false));
        Assertion.assertThat("The bid costs the user " + tokenCost + " of their " + tokensBeforeBid + " tokens",
                gamificationPage.waitForTokenBalanceToChangeFrom(tokensBeforeBid), is(tokensBeforeBid - tokenCost));
        Assertion.assertThat("The holder cannot bid against themselves",
                gamificationPage.isSnatchTreasureButtonReady(), is(false));
    }

    @Author(names = {"Yanuar"})
    @Module(name = "Gamification")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void verifyBtmExchangePointsForTokensWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyBtmExchangePointsForTokensWeb");
        BaseTest.paramsThreadLocal.set(param);

        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        GamificationPageWeb<?> gamificationPage = GamificationPageWeb.getInstance(session);
        GamificationApi<?> gamificationApi = new GamificationApi<>(session);

        String email = param.get("email");
        String password = param.get("password");
        long accountId = Long.parseLong(param.get("accountId"));
        int minimumPoints = Integer.parseInt(param.get("minimumPoints"));

        step("Given a player holding Blibli Tiket Points");
        int pointsBeforeTrade = gamificationApi.topUpLoyaltyPointsTo(accountId, email, minimumPoints);

        step("And that player is logged in");
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("When the player opens Berburu Tiket Murah");
        gamificationPage.openGamePageAsPlayer(email, password);
        int tokensBeforeTrade = gamificationPage.getTokenBalance();

        step("And opens 'Add Tokens' from beside their token counter");
        gamificationPage.openAddTokensSheet();
        Assertion.assertThat("The '+' beside the token counter opens the 'Add Tokens' sheet",
                gamificationPage.isAddTokensSheetDisplayed(), is(true));

        step("And asks to trade points rather than buy a voucher");
        Assertion.assertThat("The sheet shows the points trade", gamificationPage.openTradePointsTab(), is(true));
        Assertion.assertThat("The trade credits the player with the points the loyalty service holds for them",
                gamificationPage.getPointsBalanceInSheet(), is(pointsBeforeTrade));

        step("When the player trades their points for " + TOKENS_TO_TRADE + " token");
        int pointsPerToken = gamificationPage.getPointsPerToken();
        gamificationPage.setTokensToTrade(TOKENS_TO_TRADE);
        int tradeCost = gamificationPage.getTradeCostInPoints();
        Assertion.assertThat("The trade is priced at the rate the sheet advertises, " + pointsPerToken
                        + " points a token", tradeCost, is(pointsPerToken * TOKENS_TO_TRADE));
        Assertion.assertThat("The player holds enough points to make the trade. Points are topped up to "
                        + minimumPoints + " before the hunt is opened, so falling short here means that "
                        + "top-up did not take.",
                pointsBeforeTrade, greaterThanOrEqualTo(tradeCost));
        gamificationPage.tradePointsForTokens();

        step("Then the tokens are credited and the points are spent");
        Assertion.assertThat("The trade adds " + TOKENS_TO_TRADE + " token to the player's " + tokensBeforeTrade,
                gamificationPage.waitForTokenBalanceToChangeFrom(tokensBeforeTrade),
                is(tokensBeforeTrade + TOKENS_TO_TRADE));
        Assertion.assertThat("The trade takes " + tradeCost + " of the player's " + pointsBeforeTrade + " points",
                gamificationPage.waitForPointsBalanceInSheetToChangeFrom(pointsBeforeTrade),
                is(pointsBeforeTrade - tradeCost));
        Assertion.assertThat("The points are gone from the loyalty service too, not just off the sheet",
                gamificationApi.getLoyaltyPoints(accountId, email), is(pointsBeforeTrade - tradeCost));
    }
}
