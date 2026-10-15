package com.tiket.web.test.platform.eiffel.loyalty;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.HotelApi;
import com.tiket.apiPage.LoyaltyApi;
import com.tiket.apiPage.MemberApi;
import com.tiket.apiPage.OrderApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Session;
import com.tiket.pojo.accommodation.hotel.payathotel.PayAtHotelRoom;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.WebTest;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelDetailPage;
import com.tiket.web.dWeb.page.accommodation.hotels.HotelRoomListPage;
import com.tiket.web.dWeb.page.payment.PaymentPage;
import com.tiket.web.page.accommodation.hotels.BookingForm.HotelBookingFormPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import com.tiket.web.page.common.mission.MissionPageWeb;
import com.tiket.web.page.common.rewards_page.RewardsPageWeb;
import com.tiket.web.page.order.OrderList.OrderListWeb;
import com.tiket.web.page.platform.eiffel.loyalty.LoyaltyDependencyPageWeb;
import com.tiket.web.page.platform.eiffel.loyalty.LoyaltyDependencyPageWeb.ProductToRefund;
import com.tiket.web.page.platform.eiffel.loyalty.LoyaltyDependencyPageWeb.RefundOrder;
import com.tiket.web.page.platform.eiffel.loyalty.LoyaltyPointPageWeb;
import com.tiket.web.page.platform.payment.PaymentPageWeb;

import org.hamcrest.core.Is;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import org.eclipse.jgit.util.StringUtils;

import static com.tiket.core.CoreFunctions.generateRandomDate;
import static com.tiket.core.CoreFunctions.getRandomObject;
import static com.tiket.core.CoreFunctions.randomName;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class LoyaltyRewardsTest extends WebTest {

    @Author(names = {"Mridul"})
    @Module(name = "Rewards")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Eiffel")
    @Test
    public void openRewardsPageAsGuestUserWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("openRewardsPageAsGuestUserWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        RewardsPageWeb<?> rewardsPage = RewardsPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Given a non-login user opens the app");
        homePage.goToHomePage();

        step("Set session as non-login (guest)");
        loginPage.loginAsGuest();

        step("When user clicks on rewards page");
        rewardsPage.openRewardsPage();

        step("Then user should be redirected to tiket's rewards page");
        Assertion.assertThat("User should be on the rewards page", rewardsPage.isOnRewardsPage(), is(true));

        step("When user clicks on the login button on the rewards page");
        rewardsPage.clickLoginButton();

        step("And user inputs all the required login fields");
        boolean loginAccepted = loginPage.loginIntoWebFromDifferentPage(email, password, otp, 1);
        Assertion.assertThat("Login credentials should be accepted", loginAccepted, is(true));

        step("Then user should be successfully logged in");
        Assertion.assertThat("User should be successfully logged in and points are visible", rewardsPage.isPointsBalanceVisible(), is(true));
    }

    @Author(names = {"Mridul"})
    @Module(name = "Rewards")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Eiffel")
    @Test
    public void openRewardsPageAsLoginUserWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> param = getParam("openRewardsPageAsLoginUserWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        RewardsPageWeb<?> rewardsPage = RewardsPageWeb.getInstance(session);

        String email = param.get("email");
        String password = param.get("password");
        String otp = param.get("otp");

        step("Given user is already logged in");
        homePage.goToHomePage();
        loginPage.loginWithSpecificAccountViaCookie(email, password);

        step("When user clicks on rewards page");
        rewardsPage.openRewardsPage();

        step("Then user should be redirected to the ULP (rewards) page");
        Assertion.assertThat("User should be on the rewards page", rewardsPage.isOnRewardsPage(), is(true));

        step("And user can see their points");
        Assertion.assertThat("User points balance should be visible", rewardsPage.isPointsBalanceVisible(), is(true));

        step("And user can scroll to the bottom of the page");
        rewardsPage.scrollToBottomOfRewardsPage();
    }

    @Author(names = {"Ashish"})
    @Module(name = "Rewards")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Eiffel")
    @Test
    public void verifyNoMissionStampForPayAtHotelWeb() throws Exception {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyNoMissionStampForPayAtHotelWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        HotelDetailPage<?> detailPage = new HotelDetailPage<>(session);
        HotelRoomListPage<?> roomListPage = new HotelRoomListPage<>(session);
        HotelBookingFormPageWeb<?> bookingFormPage = HotelBookingFormPageWeb.getInstance(session);
        PaymentPage<?> paymentPage = new PaymentPage<>(session);
        MissionPageWeb<?> missionPage = new MissionPageWeb<>(session);
        OrderApi<?> orderApi = new OrderApi<>(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        // Check-in date is driven by availability: the PAH finder scans the configured dateRange
        // window (set a wide range like "1-60" in the sheet) and picks whichever date actually has
        // Pay at Hotel availability (it returns the earliest available PAH date per hotel).
        String dateRange = param.get("dateRange");
        String username = param.get("username");
        String password = param.get("password");
        int totalNight = Integer.parseInt(param.get("numberOfNights"));
        int totalRoom = Integer.parseInt(param.get("numberOfRooms"));
        int totalAdult = Integer.parseInt(param.get("numberOfAdults"));
        String cardNumber = param.get("cardNumber");
        String cardExpiry = param.get("cardExpiry");
        String cardCvv = param.get("cardCvv");
        String cardHolderName = param.get("cardHolderName");
        String missionSlug = param.get("missionSlug") != null ? param.get("missionSlug") : "stay-with-benefits";

        try
        {
            step("User opens home page");
            homePage.goToHomePage();

            step("Login as the new account " + username + " via session injection");
            loginPage.loginWithSpecificAccountViaCookie(username, password);

            step("User selects IDR currency (required to join the mission)");
            homePage.selectCurrency("IDR");

            step("Open the mission page and check whether the user still needs to join");
            missionPage.openMission(missionSlug);
            if (missionPage.isJoinMissionRequired()) {
                step("User has NOT joined yet - join the mission via API for " + username);
                loyaltyApi.joinMission(missionSlug, username);

                step("Refresh the mission page and verify the join status changed (join CTA gone)");
                missionPage.refreshMissionPage();
                Assertion.assertThat(
                        "After joining the mission via API and refreshing, the 'Swipe to join' CTA must be gone "
                                + "(the user is now enrolled in the mission).",
                        missionPage.isJoinMissionRequired(), is(false));
            } else {
                step("User has already joined the mission - continuing without re-joining");
            }

            step("Read the baseline mission stamp count for the joined account");
            Assertion.assertThat(
                    "Mission stamp summary (e.g. '0/15 stamps') should be visible on the mission page after "
                            + "the user has joined the mission",
                    missionPage.isStampSummaryVisible(), is(true));
            int stampsBefore = missionPage.getCollectedStampCount();
            int totalStamps = missionPage.getTotalStampCount();
            String summaryBefore = missionPage.getStampSummaryText();
            step("BASELINE mission stamps (before any Pay at Hotel booking): '" + summaryBefore
                    + "' -> collected=" + stampsBefore + ", total=" + totalStamps
                    + ". This is the reference value the post-booking count must match.");

            step("Find a Pay at Hotel PDP and open it via the PAH summary URL");
            PayAtHotelRoom selectedPah = detailPage.openFirstAvailablePayAtHotelPdp(
                    dateRange, totalNight, totalRoom, totalAdult);
            Assertion.assertThat(
                    detailPage.getLastPayAtHotelPdpOpenFailureDetail() != null
                            ? detailPage.getLastPayAtHotelPdpOpenFailureDetail()
                            : "Pay at Hotel is available - one of the PAH Hotel items opened a PDP with an available room.",
                    selectedPah, is(notNullValue()));

            step("See hotel rooms");
            detailPage.seeHotelRoom();
            roomListPage.scrollToRoomListSection();
            Assertion.assertThat("Room list should be visible", roomListPage.isRoomListSectionVisible(), is(true));

            step("Book the Pay at Hotel rate plan");
            roomListPage.clickBookButtonOfPAHRoom();
            roomListPage.dismissRatePlanChangePopupsIfPresent();

            step("Fill primary guest details");
            bookingFormPage.fillGuestDetails(1, randomName() + " autoguest", true, false, "");

            step("Continue to payment page");
            roomListPage.dismissRatePlanChangePopupsIfPresent();
            bookingFormPage.continueToPaymentPage();

            step("Capture order ID from payment page");
            String orderID = paymentPage.getOrderId();
            Assertion.assertThat("Order ID should be generated for the PAH booking",
                    orderID != null && !orderID.isEmpty(), is(true));

            step("Input credit/debit card details for the PAH guarantee");
            paymentPage.inputCardNumber(cardNumber);
            paymentPage.inputCardMonth(cardExpiry);
            paymentPage.inputCardCvv(cardCvv);
            paymentPage.inputCardHolderName(cardHolderName);

            step("Submit the payment to issue the order");
            paymentPage.clickOnPaymentButton();

            step("Wait for the order to reach issued status (fail fast if it never does, instead of "
                    + "silently continuing with an unissued order)");
            Assertion.assertThat(
                    "Order " + orderID + " must reach an issued status (ETICKET_ISSUED/PROCESSING_ETICKET) "
                            + "before the mission is evaluated - otherwise the publish-finish/mission checks "
                            + "would fail misleadingly instead of surfacing the real payment/issuance failure.",
                    orderListPage.waitForPaymentSuccessful(orderID, 6), is(true));

            step("Verify the order is successfully issued");
            Assertion.assertThat("Payment should be successful for the PAH booking",
                    paymentPage.isPaymentSuccessfulVisible(), is(true));

            step("Resolve the hotel orderDetailId for orderId " + orderID);
            long orderId = Long.parseLong(Objects.requireNonNull(orderID).replaceAll("[^0-9]", ""));
            long orderDetailId = orderApi.getHotelOrderDetailId(orderID);

            //add sleep to wait for order to be processed by mission engine
            step("Wait for 5 seconds to allow mission engine to process the order");
            sleep(5);

            step("Trigger the finished order endpoint for the PAH transaction");
            orderApi.publishFinishOrder(orderId, orderDetailId);

            step("Wait for the order to reach FINISHED - this is the event the mission engine consumes");
            Assertion.assertThat(
                    "Order " + orderID + " must reach FINISHED status (polled via the internal order detail "
                            + "API) before we evaluate the mission - otherwise the mission engine has not even "
                            + "seen the transaction yet.",
                    orderApi.waitForOrderFinished(orderID, 60, 5), is(true));

            step("Cross-check via the mission detail API that the finished Pay at Hotel order grants NO stamp - "
                    + "poll a bounded settle window so a delayed/async stamp increment cannot slip through a "
                    + "single early read");
            Assertion.assertThat(
                    "Pay at Hotel must NOT grant a mission stamp/GBV: the mission detail API collected-stamp "
                            + "count for " + username + " must stay at the baseline (" + stampsBefore + ") across "
                            + "the whole post-FINISHED settle window. Any increase means a stamp/GBV was wrongly "
                            + "awarded for a Pay at Hotel transaction.",
                    loyaltyApi.isStampCountStableAt(missionSlug, username, stampsBefore, 6, 10), is(true));

            step("Re-read the mission stamp count on the UI after the PAH order is issued & finished "
                    + "(expected still " + stampsBefore + "/total)");
            missionPage.openMission(missionSlug);
            Assertion.assertThat(
                    "Mission stamp summary should still be visible on the mission page after the PAH order",
                    missionPage.isStampSummaryVisible(), is(true));
            int stampsAfter = missionPage.getCollectedStampCount();
            String summaryAfter = missionPage.getStampSummaryText();
            step("AFTER PAH booking + finished order: '" + summaryAfter + "' -> collected=" + stampsAfter
                    + ", total=" + totalStamps);

            step("Verify Pay at Hotel granted NO mission stamp / GBV");
            Assertion.assertThat(
                    "PAY AT HOTEL MUST NOT GRANT A MISSION STAMP/GBV. After completing a Pay at Hotel booking, "
                            + "issuing the order and triggering the publish-finish-order event, the collected "
                            + "mission stamp count must stay EXACTLY THE SAME as the baseline (it must not "
                            + "increase). Baseline before booking = '" + summaryBefore + "' (collected="
                            + stampsBefore + "/" + totalStamps + "); after the PAH transaction = '" + summaryAfter
                            + "' (collected=" + stampsAfter + "/" + totalStamps + "). A higher count means a "
                            + "stamp/GBV was wrongly awarded for a Pay at Hotel transaction.",
                    stampsAfter, is(stampsBefore));
        }catch (Exception e){
            throw new Exception("Error during test execution: " + e.getMessage(), e);
        }finally {
            step("logout so same account won't be affected by other test runs");
            loginPage.logoutViaUrl();
        }
    }

    @Author(names = {"Mridul"})
    @Module(name = "PendingPoints")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Eiffel")
    @Test
    public void testPendingEarningPointsAfterTtdOrderWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testPendingEarningPointsAfterTtdOrderWeb");
        BaseTest.paramsThreadLocal.set(sheetData);

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        LoyaltyPointPageWeb<?> loyaltyPointPageWeb = LoyaltyPointPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);
        RewardsPageWeb<?> rewardsPage = RewardsPageWeb.getInstance(session);

        String email = sheetData.get("email");
        String password = sheetData.get("password");
        String productUrl = sheetData.get("productUrl");
        String productPackages = sheetData.get("productPackages");
        int dateRange = Integer.parseInt(sheetData.get("dateRange"));
        int monthRange = Integer.parseInt(sheetData.get("monthRange"));
        String paymentMethod = sheetData.get("paymentMethod");
        int issuedWaitAttempts = Integer.parseInt(sheetData.get("issuedWaitAttempts"));
        int pendingPointWaitAttempts = Integer.parseInt(sheetData.get("pendingPointWaitAttempts"));
        int pendingPointWaitIntervalSec = Integer.parseInt(sheetData.get("pendingPointWaitIntervalSec"));
        String expectedOrderType = sheetData.get("expectedOrderType");
        String expectedPointStatus = sheetData.get("expectedPointStatus");
        String guestFullName = randomName() + " " + randomName();
        String guestTitle = "Mr";

        step("Given user is logged in on dWeb");
        homePageWeb.goToHomePage();
        loginPageWeb.loginWithSpecificAccountViaCookie(email, password);

        step("When user books Todo product " + productUrl + " via API, then pays with " + paymentMethod
                + " on the payment page UI");
        Map<String, String> bookingData = loyaltyPointPageWeb.bookTTDViaApiAndPayOnUi(
                email, password, productUrl, productPackages,
                guestFullName, email, guestTitle,
                dateRange, monthRange, paymentMethod);
        String orderId = bookingData.get("orderId");
        String earningPointsOnPaymentPage = bookingData.get("earningPoints");
        int expectedPendingPoints = Integer.parseInt(earningPointsOnPaymentPage);

        step("And user captured earning points from payment page before paying with Sakuku");
        Assertion.assertThat("Earning points on payment page should be greater than 0",
                expectedPendingPoints, greaterThan(0));

        step("Wait until Todo order is issued");
        orderListPage.waitForPaymentSuccessful(orderId, issuedWaitAttempts);

        step("Then pending-list API should contain orderId, orderType, pointStatus and matching points");
        loyaltyPointPageWeb.verifyPendingEarningPoints(
                orderId,
                expectedPendingPoints,
                expectedOrderType,
                expectedPointStatus,
                pendingPointWaitAttempts,
                pendingPointWaitIntervalSec);

        step("And pending point history page should be accessible");
        rewardsPage.openPendingPointHistoryPage();
        Assertion.assertThat("Pending point history page should be loaded", rewardsPage.isPendingPointHistoryPageLoaded(), is(true));
    }

    @Author(names = {"Mridul"})
    @Module(name = "PendingPoints")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Eiffel")
    @Test
    public void testNoPendingEarningPointsWhenOrderFinishedWithPromoWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> sheetData = getParam("testNoPendingEarningPointsWhenOrderFinishedWithPromoWeb");
        BaseTest.paramsThreadLocal.set(sheetData);

        HomePageWeb<?> homePageWeb = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPageWeb = LoginPageWeb.getInstance(session);
        LoyaltyPointPageWeb<?> loyaltyPointPageWeb = LoyaltyPointPageWeb.getInstance(session);
        OrderListWeb<?> orderListPage = OrderListWeb.getInstance(session);

        String email = sheetData.get("email");
        String password = sheetData.get("password");
        String productUrl = sheetData.get("productUrl");
        String productPackages = sheetData.get("productPackages");
        int dateRange = Integer.parseInt(sheetData.get("dateRange"));
        int monthRange = Integer.parseInt(sheetData.get("monthRange"));
        String promoCode = sheetData.get("promoCode");
        String paymentMethod = sheetData.get("paymentMethod");
        int issuedWaitAttempts = Integer.parseInt(sheetData.get("issuedWaitAttempts"));
        int pendingPointWaitAttempts = Integer.parseInt(sheetData.get("pendingPointWaitAttempts"));
        int pendingPointWaitIntervalSec = Integer.parseInt(sheetData.get("pendingPointWaitIntervalSec"));
        String guestFullName = randomName() + " " + randomName();
        String guestTitle = "Mr";

        step("Given user is logged in on dWeb");
        homePageWeb.goToHomePage();
        loginPageWeb.loginWithSpecificAccountViaCookie(email, password);

        step("When user books Todo product " + productUrl + " via API, then applies promo " + promoCode
                + " and pays with " + paymentMethod + " on the payment page UI (the ToDo search/PDP/booking-form "
                + "UI is not under test here, so driving it would only add flakiness)");
        Map<String, String> bookingData = loyaltyPointPageWeb.bookTTDViaApiAndPayWithPromoOnUi(
                email, password, productUrl, productPackages,
                guestFullName, email, guestTitle,
                dateRange, monthRange, promoCode, paymentMethod);
        String orderId = bookingData.get("orderId");

        step("Wait until Todo order is issued");
        orderListPage.waitForPaymentSuccessful(orderId, issuedWaitAttempts);

        step("Then orderId should not have real pending earning points in pending-list API (pending point = 1 is tier upgrade/GBV reporting and is acceptable)");
        loyaltyPointPageWeb.verifyOrderIdNotInPendingEarningPoints(orderId, pendingPointWaitAttempts, pendingPointWaitIntervalSec);

    }

    @Author(names = {"Ashish"})
    @Module(name = "Rewards")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Eiffel")
    @Test
    public void verifyClaimMissionRewardsWeb() throws Exception {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyClaimMissionRewardsWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        MissionPageWeb<?> missionPage = new MissionPageWeb<>(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        Unm<?> unm = new Unm<>(session);

        String missionSlug = param.get("missionSlug") != null ? param.get("missionSlug") : "stay-with-benefits";
        String missionId = param.get("missionId");
        String vertical = param.get("vertical") != null ? param.get("vertical") : "HOTEL";
        String challenge = param.get("challenge");
        String challengeSignature = param.get("challengeSignature");
        String whitelistUsername = param.get("whitelistUsername");

        RegisterNewAccountData accountData = null;
        try {
            // ---------- GIVEN: a fresh account enrolled in the mission ----------
            step("Create a fresh account via API so the mission starts from zero stamps");
            accountData = unm.registerNewAccountViaApiUNM();
            String username = accountData.email;
            String password = accountData.password;

            step("Enroll the new account in mission '" + missionSlug + "' via API");
            loyaltyApi.joinMission(missionSlug, username);

            step("Log in as " + username + " and set IDR currency");
            homePage.goToHomePage();
            loginPage.loginWithSpecificAccountViaCookie(username, password);
            homePage.selectCurrency("IDR");

            step("Open the mission page and confirm enrollment");
            missionPage.openMission(missionSlug);
            missionPage.refreshMissionPage();
            Assertion.assertThat(
                    "The 'Swipe to join' CTA must be gone after enrolling - it confirms the account joined "
                            + "mission '" + missionSlug + "'.",
                    missionPage.isJoinMissionRequired(), is(false));

            step("Read how many stamps the mission requires from the mission detail API (drives the run "
                    + "dynamically - no hard-coded count)");
            int totalStamps = loyaltyApi.getMissionTotalStamps(missionSlug, username);
            Assertion.assertThat(
                    "A joinable mission must declare at least one required stamp.",
                    totalStamps, greaterThan(0));

            step("Confirm the mission page renders the same required stamp count");
            Assertion.assertThat(
                    "The stamp summary (e.g. '0/15 stamps') must be visible once the account has joined.",
                    missionPage.isStampSummaryVisible(), is(true));
            Assertion.assertThat(
                    "The total stamps shown on the UI must match the mission detail API (" + totalStamps + ").",
                    missionPage.getTotalStampCount(), is(totalStamps));

            // ---------- WHEN: complete the mission by injecting all stamps ----------
            step("Inject all " + totalStamps + " stamps via the mission tools API to complete the mission");
            long accountId = loyaltyApi.injectMissionStamp(username, missionId, totalStamps, vertical,
                    challenge, challengeSignature, whitelistUsername);

            step("Poll the mission detail API until ALL " + totalStamps + " stamps are marked COMPLETE "
                    + "(deterministic, replaces a fixed wait)");
            Assertion.assertThat(
                    "After injecting " + totalStamps + " stamps, every stamp in the mission detail API "
                            + "(userProgress) must reach COMPLETE status (collected == required = " + totalStamps
                            + ") within the wait budget - this confirms the mission engine processed all the "
                            + "injected progress.",
                    loyaltyApi.waitForAllStampsComplete(missionSlug, username, totalStamps, 12, 5), is(true));

            step("Poll until the reward becomes claimable (mission detail API exposes CLAIM_REWARD / "
                    + "reward.status=COMPLETED) - this bridges the gap between stamps completing and the "
                    + "claim popup appearing on the UI");
            Assertion.assertThat(
                    "Once all stamps are complete, the mission detail API must expose the reward as claimable "
                            + "(cta=CLAIM_REWARD / reward.status=COMPLETED) before the claim popup can be shown "
                            + "on the UI.",
                    loyaltyApi.waitForMissionRewardClaimable(missionSlug, username, 12, 5), is(true));

            step("Open the completed mission and surface the reward popup");
            missionPage.openMission(missionSlug);
            boolean rewardPopupShown = missionPage.isRewardReceivedPopupVisible(20);
            if (!rewardPopupShown && missionPage.isClaimRewardAvailable()) {
                step("Reward popup did not auto-open - clicking the claim CTA to open it");
                missionPage.clickClaimReward();
                rewardPopupShown = missionPage.isRewardReceivedPopupVisible(20);
            }
            Assertion.assertThat(
                    "The reward popup must appear after all " + totalStamps + " stamps are injected - it only "
                            + "shows once the mission is complete and a reward is claimable.",
                    rewardPopupShown, is(true));

            // ---------- THEN: claim the reward and verify the granted amount ----------
            String rewardTitle = missionPage.getRewardTitleTextSafely();
            String rewardAmountText = missionPage.getRewardAmountText();
            boolean isPointsReward = missionPage.isPointsReward();
            // Capture the points amount while the popup is still open - clicking claim closes the modal,
            // so the popup locators are gone afterwards.
            int uiPoints = isPointsReward ? missionPage.getRewardPointsValue() : 0;
            step("Reward on popup -> title='" + rewardTitle + "', amount='" + rewardAmountText
                    + "', type=" + (isPointsReward ? "POINTS" : "GIFT VOUCHER"));

            step("Claim the reward from the popup");
            missionPage.clickClaimInRewardPopup();

            step("Verify on the UI that the claim was accepted (reward popup advances past the claimable state)");
            Assertion.assertThat(
                    "After clicking Claim, the reward popup's claim button must disappear (popup closes / "
                            + "transitions). If it stays visible, the claim was not accepted on the UI.",
                    missionPage.isRewardClaimAcceptedOnUi(20), is(true));

            step("Verify the mission reward transitions to CLAIMED via the mission detail API");
            Assertion.assertThat(
                    "After clicking Claim, the mission reward status must become CLAIMED. If the popup stays "
                            + "visible but the API never reports CLAIMED, the claim did not actually succeed "
                            + "(guards against a false pass, especially for non-points rewards).",
                    loyaltyApi.waitForMissionRewardStatus(missionSlug, username, "CLAIMED", 12, 5), is(true));

            if (isPointsReward) {
                step("Verify the credited points in the point-history API match the " + uiPoints
                        + " points shown on the popup");
                int creditedPoints = loyaltyApi.getCreditedRewardPoint(accountId, uiPoints);
                Assertion.assertThat(
                        "The points credited in the loyalty point-history API (accountId=" + accountId + ") must "
                                + "equal the " + uiPoints + " points shown on the reward popup. A different value "
                                + "means the claim credited the wrong amount; -1 means nothing was credited.",
                        creditedPoints, is(uiPoints));
            } else {
                step("Verify the gift-voucher reward shows a concrete amount on the popup");
                Assertion.assertThat(
                        "A claimed gift-voucher reward must display a non-empty amount/value on the popup "
                                + "(actual='" + rewardAmountText + "').",
                        rewardAmountText, is(not(emptyOrNullString())));
            }
        } finally {
            step("Cleanup: log out and delete the temporary account");
            homePage.logoutAndDeleteTemporaryAccount(accountData);
        }
    }

    /**
     * The mission engine only grants a stamp for a mission-eligible product. A standard prepaid hotel
     * booking (paid via Sakuku) is eligible (unlike Pay at Hotel), so finishing such an order must
     * increase the collected stamp count. The eligible hotel order is created via API (search -&gt;
     * select native room -&gt; prebook -&gt; submit booking -&gt; pay via Sakuku) instead of the slow,
     * flaky UI booking flow.
     */
    @Author(names = {"Ashish"})
    @Module(name = "Rewards")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Eiffel")
    @Test
    public void verifyMissionStampIncreaseForEligibleHotelWeb() throws Exception {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Map<String, String> param = getParam("verifyMissionStampIncreaseForEligibleHotelWeb");
        BaseTest.paramsThreadLocal.set(param);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        MissionPageWeb<?> missionPage = new MissionPageWeb<>(session);
        HotelApi<?> hotelApi = new HotelApi<>(session);
        OrderApi<?> orderApi = new OrderApi<>(session);
        LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
        Unm<?> unm = new Unm<>(session);

        String missionSlug = param.get("missionSlug") != null ? param.get("missionSlug") : "stay-with-benefits";
        String hotelName = param.get("hotelName");
        String currency = param.get("currency") != null ? param.get("currency") : "IDR";
        String startDate = generateRandomDate(param.get("rangeAddDate") != null ? param.get("rangeAddDate") : "7-8");
        int adult = param.get("numberOfAdults") != null ? Integer.parseInt(param.get("numberOfAdults")) : 1;
        int night = param.get("numberOfNights") != null ? Integer.parseInt(param.get("numberOfNights")) : 1;
        int room = param.get("numberOfRooms") != null ? Integer.parseInt(param.get("numberOfRooms")) : 1;
        boolean isRefundable = param.get("isRefundable") != null ? Boolean.parseBoolean(param.get("isRefundable")) : true;
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autoguest";
        String guestTitle = getRandomObject(salutationList);

        RegisterNewAccountData accountData = null;
        try {
            step("Create a brand new account via API (fresh account so we start from zero stamps)");
            accountData = unm.registerNewAccountViaApiUNM();
            String username = accountData.email;
            String password = accountData.password;

            step("User opens home page");
            homePage.goToHomePage();

            step("Login as the freshly created account " + username + " via session injection");
            loginPage.loginWithSpecificAccountViaCookie(username, password);

            step("New account has NOT joined yet - join the mission via API for " + username);
            loyaltyApi.joinMission(missionSlug, username);

            step("User selects IDR currency (required to join the mission)");
            homePage.selectCurrency("IDR");

            step("Open the mission page and join the mission for the new account");
            missionPage.openMission(missionSlug);

            step("Refresh the mission page and verify the join status changed (join CTA gone)");
            missionPage.refreshMissionPage();
            Assertion.assertThat(
                    "After joining the mission via API and refreshing, the 'Swipe to join' CTA must be gone "
                            + "(the user is now enrolled in the mission).",
                    missionPage.isJoinMissionRequired(), is(false));

            step("Read the baseline mission stamp count right after joining (expected NO stamp yet)");
            Assertion.assertThat(
                    "Mission stamp summary (e.g. '0/15 stamps') should be visible on the mission page after the "
                            + "user has joined the mission",
                    missionPage.isStampSummaryVisible(), is(true));
            int stampsBefore = missionPage.getCollectedStampCount();
            int totalStamps = missionPage.getTotalStampCount();
            String summaryBefore = missionPage.getStampSummaryText();
            step("BASELINE mission stamps right after joining (before any eligible booking): '" + summaryBefore
                    + "' -> collected=" + stampsBefore + ", total=" + totalStamps
                    + ". This is the reference value the post-order count must exceed.");
            Assertion.assertThat(
                    "A freshly joined account must have NO mission stamp yet. Right after joining the mission '"
                            + missionSlug + "', the collected stamp count must be 0 (summary='" + summaryBefore
                            + "'). A non-zero baseline means the account is not fresh or a stamp was granted "
                            + "before any eligible order was completed.",
                    stampsBefore, is(0));

            step("Cross-check the baseline against the mission detail API (source of truth)");
            Assertion.assertThat(
                    "The mission detail API total stamps must match the UI (" + totalStamps + ").",
                    loyaltyApi.getMissionTotalStamps(missionSlug, username), is(totalStamps));
            Assertion.assertThat(
                    "The mission detail API must also report 0 collected stamps for the freshly joined account.",
                    loyaltyApi.getMissionDetail(missionSlug, username).getCompletedStampCount(), is(stampsBefore));

            step("Book a standard (non Pay at Hotel) mission-eligible hotel order for '" + hotelName
                    + "' via API (autocomplete -> select native room -> prebook -> submit booking -> pay via "
                    + "Sakuku). Using the API instead of the UI booking flow avoids the slow, flaky UI steps.");
            boolean bookingIssued = hotelApi.createIssuedOrderApi(
                    username, password, startDate, hotelName, adult, night, room,
                    orderFullName, orderNameTitle, guestFullName, guestTitle, currency, isRefundable,
                    false, "", "en", true);
            Assertion.assertThat(
                    "The mission-eligible hotel order must be created and issued via API (standard native "
                            + "prepaid room paid via Sakuku) before the mission is evaluated - otherwise the "
                            + "publish-finish/mission stamp checks would fail misleadingly instead of surfacing "
                            + "the real booking/issuance failure.",
                    bookingIssued, is(true));

            step("Capture the issued order ID from the API booking");
            int rawOrderId = hotelApi.getOrderId();
            Assertion.assertThat("A valid (non-zero) order ID must be generated for the eligible hotel booking",
                    rawOrderId, is(greaterThan(0)));
            String orderID = String.valueOf(rawOrderId);

            step("Resolve the hotel orderDetailId for orderId " + orderID);
            long orderId = rawOrderId;
            long orderDetailId = orderApi.getHotelOrderDetailId(orderID);

            //add sleep to wait for order to be processed by mission engine
            step("Wait for 5 seconds to allow mission engine to process the order");
            sleep(5);

            step("Trigger the publish-finish-order endpoint so the order is FINISHED (not just issued)");
            orderApi.publishFinishOrder(orderId, orderDetailId);

            step("Wait for the order to reach FINISHED - the event the mission engine consumes");
            Assertion.assertThat(
                    "Order " + orderID + " should reach FINISHED status (via the internal order detail API) so "
                            + "the mission engine can evaluate the eligible transaction.",
                    orderApi.waitForOrderFinished(orderID, 60, 6), is(true));

            step("Poll the mission detail API until the eligible order grants a stamp (collected stamps rise "
                    + "above the baseline of " + stampsBefore + ") - deterministic, replaces a fixed wait");
            int apiStampsAfter = loyaltyApi.waitForStampCountAbove(missionSlug, username, stampsBefore, 12, 5);
            Assertion.assertThat(
                    "FINISHING A MISSION-ELIGIBLE HOTEL ORDER MUST GRANT A MISSION STAMP. After completing a "
                            + "standard hotel booking paid via Sakuku, issuing the order and triggering the "
                            + "publish-finish-order event, the mission detail API collected-stamp count must be "
                            + "GREATER THAN the baseline (" + stampsBefore + "/" + totalStamps + "). apiCollected="
                            + apiStampsAfter + ". If it did not increase, no stamp was awarded for an eligible "
                            + "transaction.",
                    apiStampsAfter, is(greaterThan(stampsBefore)));

            step("Cross-check the mission UI reflects the increased stamp count");
            step("Re-read the mission stamp count after the eligible hotel order is finished (expected to increase)");
            homePage.goToHomePage();
            missionPage.openMission(missionSlug);
            Assertion.assertThat(
                    "Mission stamp summary should still be visible on the mission page after the eligible order",
                    missionPage.isStampSummaryVisible(), is(true));
            // The mission microsite can lag the mission detail API, so poll/refresh the UI until it catches
            // up to the API-confirmed count instead of reading it once (which can be stale).
            int uiStampsAfter = missionPage.waitForCollectedStampCount(apiStampsAfter, 6, 5);
            step("AFTER eligible hotel booking + finished order: UI collected=" + uiStampsAfter + ", API collected="
                    + apiStampsAfter + ", total=" + totalStamps);
            Assertion.assertThat(
                    "The mission UI collected-stamp count must match the mission detail API after the eligible "
                            + "order (UI=" + uiStampsAfter + ", API=" + apiStampsAfter + ").",
                    uiStampsAfter, is(apiStampsAfter));
        } finally {
            step("Cleanup: log out and delete the temporary account so it does not pollute future runs");
            homePage.logoutAndDeleteTemporaryAccount(accountData);
        }
    }

    @Author(names = { "Aditi" })
    @Module(name = "Loyalty")
    @Tribe(name = "Loyalty")
    @Vertical(name = "Platform")
    @Test
    public void testRefundHotelNegativePoints() throws FailedLoginException, InterruptedException, ExecutionException {
            Map<String, String> param = getParam("testRefundHotelNegativePoints");
            BaseTest.paramsThreadLocal.set(param);
            Session<?> session = BaseTest.sessionThreadLocal.get();

            HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
            LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
            PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
            LoyaltyPointPageWeb<?> loyaltyPage = LoyaltyPointPageWeb.getInstance(session);
            LoyaltyDependencyPageWeb<?> loyaltyDependencyPage = LoyaltyDependencyPageWeb.getInstance(session);
            LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
            MemberApi<?> memberApi = new MemberApi<>(session);

            String email = param.get("email");
            String password = param.get("password");
            int retryCounter = Integer.parseInt(param.get("retryCounter"));
            RefundOrder refundOrder = new RefundOrder(
                            ProductToRefund.from(param.get("productToRefund")),
                            param.get("productUrl"),
                            param.get("productPackage"),
                            param.get("minDaysToVisit"),
                            param.get("hotelName"),
                            param.get("dateRange"),
                            param.get("adult"),
                            param.get("night"),
                            param.get("room"),
                            param.get("currency"));
            String paymentGroup = param.get("paymentGroup");
            String paymentMethod = param.get("paymentMethod");
            String promoCode = param.get("promoCode");
            String reasonId = param.get("reasonId");
            String adminEmail = param.get("adminEmail");
            String adminPassword = param.get("adminPassword");

            if (StringUtils.isEmptyOrNull(promoCode)
                            || StringUtils.isEmptyOrNull(adminEmail)
                            || StringUtils.isEmptyOrNull(adminPassword)) {
                    Assertion.skip("Missing required test data for testRefundHotelNegativePoints "
                                    + "(promoCode/adminEmail/adminPassword must be provided); skipping as a test-data issue.");
            }

            step("Create a waiting-payment " + refundOrder.product() + " order to refund");
            Map<String, String> orderData = loyaltyDependencyPage.bookOrderToRefund(email, password, refundOrder);
            String orderId = orderData.get("orderId");
            Assertion.assertThat("Success to create waiting payment order", orderId, Is.is(not(emptyOrNullString())));

            step("User open home page");
            homePage.goToHomePage();

            step("User open as logged in user");
            loginPage.loginWithSpecificAccountViaCookie(email, password);
            step("User select currency: " + refundOrder.displayCurrency());
            homePage.selectCurrency(refundOrder.displayCurrency());

            step("Go to payment page directly via URL");
            loyaltyPage.goToPaymentPage(orderId, orderData.get("orderHash"));

            step("Verify order id is not present in loyalty point history");
            long accountId = !StringUtils.isEmptyOrNull(param.get("accountId"))
                            ? Long.parseLong(param.get("accountId"))
                            : memberApi.getAccountDataByEmail(email).accountId;
            loyaltyApi.verifyOrderIdNotInPointHistory(orderId, accountId);

            step("Click in promo suggestion");
            loyaltyDependencyPage.clickOnSeePromosVoucher();

            step("input text in promo box");
            loyaltyDependencyPage.inputPromoCodeAndApply(promoCode);

            step("Capture promo points from applied promo banner");
            Assertion.assertThat("Promo banner should be visible on payment page",
                            loyaltyDependencyPage.isPromoBannerVisible(), is(true));
            int expectedPromoPoints = loyaltyDependencyPage.getAppliedPromoPoints();
            step("Noted expected promo points after payment: " + expectedPromoPoints);

            step("Input the card details");
            paymentPage.clickOnSelectPaymentMethod();
            paymentPage.clickOnSeeAll();
            loyaltyDependencyPage.selectPaymentMethodInDrawer(paymentGroup, paymentMethod);

            step("Finish the payment process by clicking the payment button");
            paymentPage.clickOnPaymentButton();
            paymentPage.handle3DSPage(30);

            step("Verify the payment successful page is visible or not");
            Assertion.assertThat("Verify the payment successful page is visible",
                            paymentPage.isPaymentSuccessfulVisible(), is(true));

            step("Verify order id is present in loyalty point history with INSTANT_POINT_CASHBACK type");
            loyaltyApi.verifyOrderIdInPointHistoryWithType(orderId, accountId, "INSTANT_POINT_CASHBACK", retryCounter);

            step("Verify the " + refundOrder.product() + " order is issued, then complete the refund end-to-end via the "
                            + "customer e-refund flow and verify orderRefundStatus is 'refunded'");
            loyaltyDependencyPage.verifyOrderAndCompleteRefund(refundOrder, orderId, reasonId,
                            email, password, adminEmail, adminPassword);

            step("Verify refund negative and positive points in loyalty point history");
            loyaltyApi.verifyRefundPointsInPointHistory(orderId, accountId, expectedPromoPoints, retryCounter);

    }
}
