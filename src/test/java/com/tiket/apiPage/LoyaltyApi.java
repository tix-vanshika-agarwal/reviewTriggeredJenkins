package com.tiket.apiPage;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tiket.api.loyalty.*;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.loyalty.*;
import com.tiket.pojo.member.AccountData;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class LoyaltyApi <T extends WebDriver> extends BasePage<T> {

    /** {@code X-Account-Id} of a signed-out visitor, which the mission backend answers un-segmented. */
    private static final String ANONYMOUS_ACCOUNT_ID = "0";

    /** {@code X-Username} placeholder that pairs with {@link #ANONYMOUS_ACCOUNT_ID}; no account is resolved. */
    private static final String ANONYMOUS_USERNAME = "username";

    /** Sends allowed for a setup request (join, segment mapping) before a 5xx or transport failure is final. */
    private static final int SETUP_REQUEST_ATTEMPTS = 3;

    /** Pause between setup request attempts. */
    private static final long SETUP_REQUEST_BACKOFF_MILLIS = 3000;

    /** Reads allowed for the mission eligibility API to reflect a segment mapping or a join. */
    private static final int ELIGIBILITY_POLL_ATTEMPTS = 5;

    /** Pause between mission eligibility reads. */
    private static final long ELIGIBILITY_POLL_INTERVAL_MILLIS = 3000;

    public LoyaltyApi(Session<T> session) {
        super(session);
    }

    public Map<String,Integer> accountIdMap = new HashMap<>();

    /** Account ids already resolved on this instance, keyed by email, so one flow looks each account up once. */
    private final Map<String, Long> resolvedAccountIds = new HashMap<>();

    /**
     * Joins the given loyalty mission for the given user via the mission backend microservice. The
     * user is identified by {@code X-Account-Id} + {@code X-Username}; the account id is resolved
     * from the username (email), so no browser session/cookie is required. A 5xx or transport failure
     * is re-sent up to {@link #SETUP_REQUEST_ATTEMPTS} times; a 4xx is final.
     *
     * @param missionSlug e.g. {@code "stay-with-benefits"}
     * @param username    the account email/username to enroll
     */
    public void joinMission(String missionSlug, String username) {
        String accountId = String.valueOf(resolveAccountId(username));
        logger.log("Joining mission '" + missionSlug + "' via API for accountId=" + accountId
                + " username=" + username);

        JoinMissionApi api = new JoinMissionApi(new JoinMissionApi.Inputs(missionSlug, accountId, username));
        ApiResponse response = sendWithRetryOnServerError(() -> api.hitApi(apiTools), "Join mission");
        Assertion.assertThat("Join mission API should succeed (200/201)",
                response.getStatusCode(), anyOf(is(200), is(201)));
    }

    /**
     * Fast-forwards a user's mission progress to the target {@code count} of stamps by calling the
     * mission backend tools endpoint, so a freshly-joined account can immediately complete a mission
     * (and unlock its reward) without performing {@code count} real eligible transactions.
     *
     * <p>Auth model of the tools endpoint:
     * <ul>
     *   <li>{@code X-Challenge} / {@code X-Challenge-Signature} are a global, long-lived (~1 year)
     *       signed token issued by the backend (kept in the test data sheet; refresh on expiry).</li>
     *   <li>{@code X-Username} must be a <b>whitelisted caller</b> (an internal user allowed to use the
     *       tools endpoint). It is NOT the account being injected; a non-whitelisted username returns
     *       {@code 401 "username is not in whitelist"}.</li>
     *   <li>The request body {@code accountId} is the <b>target</b> account — this can be a brand-new
     *       account created per run.</li>
     * </ul>
     *
     * @param targetUsername      the email of the account to inject progress for (its accountId is
     *                            resolved from this email and used as the body {@code accountId})
     * @param missionId           the mission id (Mongo ObjectId), e.g. {@code "6889c05b4307dde5498dd709"}
     * @param count               the number of stamps to set (e.g. the mission's total to complete it)
     * @param vertical            the vertical the progress belongs to, e.g. {@code "HOTEL"}
     * @param challenge           the {@code X-Challenge} value from the backend
     * @param challengeSignature  the {@code X-Challenge-Signature} value from the backend
     * @param whitelistUsername   a whitelisted caller email used for the {@code X-Username} header
     * @return the resolved target account id of {@code targetUsername}
     */
    public long injectMissionStamp(String targetUsername, String missionId, int count, String vertical,
                                   String challenge, String challengeSignature, String whitelistUsername) {
        AccountData.Data accountData = resolveAccountData(targetUsername);
        long accountId = accountData.accountId;
        logger.log("Injecting mission stamps via API: targetAccountId=" + accountId + " (" + targetUsername
                + "), caller(X-Username)=" + whitelistUsername + ", missionId=" + missionId
                + ", count=" + count + ", vertical=" + vertical);

        InjectMissionStampApi api = new InjectMissionStampApi(new InjectMissionStampApi.Inputs(
                accountId, 0, count, missionId, "ACTIVE", vertical, challenge, challengeSignature, whitelistUsername));
        ApiResponse response = api.hitApi(apiTools);
        logger.log("Inject mission stamp API response: " + response.getStatusCode() + " - " + response.getBody());
        Assertion.assertThat(
                "Inject mission stamp API should succeed (200/201). A 401 'username is not in whitelist' means "
                        + "the X-Username caller (" + whitelistUsername + ") is not whitelisted; any other 401/403 "
                        + "usually means the X-Challenge / X-Challenge-Signature in the test data sheet have expired "
                        + "and must be refreshed.",
                response.getStatusCode(), anyOf(is(200), is(201)));
        return accountId;
    }

    /**
     * Reads a user's live mission status from the mission detail API
     * ({@code GET /tix-mission/api/mission/{slug}/detail}). This is the source of truth for whether a
     * mission's progress/reward has changed (e.g. after an eligible order or an injected stamp), so it
     * can replace fixed waits with a deterministic poll.
     *
     * @param missionSlug e.g. {@code "stay-with-benefits"}
     * @param username    the account email whose progress to read
     * @return the parsed, validated mission detail (mission data is guaranteed present)
     * @throws IllegalStateException if the API returns a non-2xx status or an unsuccessful payload, so an
     *                               API failure surfaces instead of silently reading a 0 stamp count
     */
    public MissionDetailResponse getMissionDetail(String missionSlug, String username) {
        AccountData.Data accountData = resolveAccountData(username);
        return getMissionDetail(missionSlug, String.valueOf(accountData.accountId), username);
    }

    private MissionDetailResponse getMissionDetail(String missionSlug, String accountId, String username) {
        MissionDetailApi api = new MissionDetailApi(new MissionDetailApi.Inputs(missionSlug, accountId, username));
        ApiResponse response = api.hitApi(apiTools);
        int statusCode = response.getStatusCode();
        logger.log("Mission detail API (" + missionSlug + ", accountId=" + accountId + ") response: " + statusCode);
        if (statusCode < 200 || statusCode >= 300) {
            throw new IllegalStateException("Mission detail API (" + missionSlug + ", accountId=" + accountId
                    + ") failed with HTTP " + statusCode + " - cannot read mission progress.");
        }
        MissionDetailResponse detail = new Gson().fromJson(response.getBody(), MissionDetailResponse.class);
        if (detail == null || detail.code == null || !detail.code.toUpperCase().contains("SUCCESS")
                || detail.data == null || detail.data.mission == null) {
            throw new IllegalStateException("Mission detail API (" + missionSlug + ", accountId=" + accountId
                    + ") returned an unexpected payload (code=" + (detail != null ? detail.code : "null")
                    + ") - cannot read mission progress.");
        }
        return detail;
    }

    /**
     * Maps a user into a dynamic-segment mission's segment via
     * {@code POST /tix-mission/api/dynamic-segment}, which makes the user eligible to join it. The
     * mapping is one-shot per account and mission, so this is meant for a fresh account. A 5xx or
     * transport failure is re-sent; a {@code 409} (already mapped) counts as mapped, because it is
     * what a re-send returns after an earlier attempt landed without its response arriving.
     *
     * @param missionId the mission id (Mongo ObjectId), e.g. {@code "6ab4f0ffca1a51dff03a07f1"}
     * @param username  the account email to map
     */
    public void mapUserToMissionDynamicSegment(String missionId, String username) {
        long accountId = resolveAccountId(username);
        logger.log("Mapping accountId=" + accountId + " (" + username + ") to the dynamic segment of mission "
                + missionId);

        MapMissionDynamicSegmentApi api = new MapMissionDynamicSegmentApi(
                new MapMissionDynamicSegmentApi.Inputs(accountId, missionId, username));
        ApiResponse response = sendWithRetryOnServerError(() -> api.hitApi(apiTools), "Dynamic segment mapping");
        String reason = switch (response.getStatusCode()) {
            case 200, 201 -> "User is mapped to the mission segment";
            case 409 -> "User is already mapped to the mission segment (409)";
            case 400 -> "Mission " + missionId + " has dynamic segment disabled (400)";
            default -> "Dynamic segment mapping failed (" + response.getStatusCode() + ")";
        };
        Assertion.assertThat(reason, response.getStatusCode(), anyOf(is(200), is(201), is(409)));
    }

    /**
     * Re-reads a user's mission eligibility until {@code data.user.userStatus} equals
     * {@code expectedUserStatus} or the retry budget runs out. The mission backend can lag a segment
     * mapping or a join by a few seconds, so a single read right after either can still show the
     * previous status.
     *
     * @param missionId          the mission id (Mongo ObjectId)
     * @param username           the account email whose eligibility to read
     * @param expectedUserStatus the status to wait for, e.g. {@link MissionEligibilityResponse#USER_STATUS_INACTIVE}
     * @return the last eligibility read, whose status equals {@code expectedUserStatus} if it converged
     */
    public MissionEligibilityResponse waitForMissionUserStatus(String missionId, String username,
                                                               String expectedUserStatus) {
        return waitForMissionEligibility(missionId, username,
                eligibility -> expectedUserStatus.equalsIgnoreCase(eligibility.getUserStatus()),
                "userStatus " + expectedUserStatus);
    }

    /**
     * Re-reads a user's mission eligibility until {@code data.user.currentProgress} equals
     * {@code expectedProgress} or the retry budget runs out, e.g. to wait for a free stamp to be
     * credited after joining.
     *
     * @param missionId        the mission id (Mongo ObjectId)
     * @param username         the account email whose progress to read
     * @param expectedProgress the collected stamp count to wait for
     * @return the last eligibility read, whose progress equals {@code expectedProgress} if it converged
     */
    public MissionEligibilityResponse waitForMissionProgress(String missionId, String username, int expectedProgress) {
        return waitForMissionEligibility(missionId, username,
                eligibility -> eligibility.getCurrentProgress() == expectedProgress,
                "currentProgress " + expectedProgress);
    }

    /**
     * Polls {@code GET /tix-mission/api/mission-eligibility/{missionId}} until {@code condition} holds or
     * {@link #ELIGIBILITY_POLL_ATTEMPTS} reads have been made. A failed read (non-2xx or unexpected
     * payload) uses up one attempt instead of ending the poll, and is rethrown only on the last attempt.
     *
     * @return the last successful read, which satisfies {@code condition} if the poll converged
     */
    private MissionEligibilityResponse waitForMissionEligibility(String missionId, String username,
                                                                 Predicate<MissionEligibilityResponse> condition,
                                                                 String conditionDescription) {
        long accountId = resolveAccountId(username);
        MissionEligibilityResponse eligibility = null;
        for (int attempt = 1; attempt <= ELIGIBILITY_POLL_ATTEMPTS; attempt++) {
            try {
                eligibility = getMissionEligibility(missionId, accountId, username);
                logger.log("Mission eligibility check " + attempt + "/" + ELIGIBILITY_POLL_ATTEMPTS + ": userStatus="
                        + eligibility.getUserStatus() + ", currentProgress=" + eligibility.getCurrentProgress()
                        + " (expected " + conditionDescription + ")");
                if (condition.test(eligibility)) {
                    return eligibility;
                }
            } catch (IllegalStateException e) {
                if (attempt == ELIGIBILITY_POLL_ATTEMPTS) {
                    throw e;
                }
                logger.log("Mission eligibility read " + attempt + "/" + ELIGIBILITY_POLL_ATTEMPTS
                        + " failed: " + e.getMessage() + " Retrying...");
            }
            if (attempt < ELIGIBILITY_POLL_ATTEMPTS) {
                sleepQuietly(ELIGIBILITY_POLL_INTERVAL_MILLIS);
            }
        }
        return eligibility;
    }

    /**
     * Sends a setup request, re-sending it after {@link #SETUP_REQUEST_BACKOFF_MILLIS} when the backend
     * answers 5xx or the call throws, up to {@link #SETUP_REQUEST_ATTEMPTS} sends. A response below 500
     * is returned at once: a 4xx describes the request itself, so sending it again would not change it.
     *
     * @return the first response below 500, or the last response if every attempt answered 5xx
     */
    private ApiResponse sendWithRetryOnServerError(Supplier<ApiResponse> request, String description) {
        for (int attempt = 1; ; attempt++) {
            try {
                ApiResponse response = request.get();
                logger.log(description + " API response (attempt " + attempt + "/" + SETUP_REQUEST_ATTEMPTS + "): "
                        + response.getStatusCode() + " - " + response.getBody());
                if (response.getStatusCode() < 500 || attempt == SETUP_REQUEST_ATTEMPTS) {
                    return response;
                }
            } catch (RuntimeException e) {
                if (attempt == SETUP_REQUEST_ATTEMPTS) {
                    throw e;
                }
                logger.log(description + " API call failed (attempt " + attempt + "/" + SETUP_REQUEST_ATTEMPTS
                        + "): " + e.getMessage());
            }
            logger.log("Retrying " + description + " API in " + SETUP_REQUEST_BACKOFF_MILLIS / 1000 + "s...");
            sleepQuietly(SETUP_REQUEST_BACKOFF_MILLIS);
        }
    }

    private MissionEligibilityResponse getMissionEligibility(String missionId, long accountId, String username) {
        MissionEligibilityApi api = new MissionEligibilityApi(
                new MissionEligibilityApi.Inputs(missionId, accountId, username));
        ApiResponse response = api.hitApi(apiTools);
        int statusCode = response.getStatusCode();
        logger.log("Mission eligibility API (" + missionId + ", accountId=" + accountId + ") response: "
                + statusCode + " - " + response.getBody());
        if (statusCode < 200 || statusCode >= 300) {
            throw new IllegalStateException("Mission eligibility API (" + missionId + ", accountId=" + accountId
                    + ") failed with HTTP " + statusCode + " - cannot read the user's mission status.");
        }
        MissionEligibilityResponse eligibility = new Gson().fromJson(response.getBody(), MissionEligibilityResponse.class);
        if (eligibility == null || eligibility.code == null || !eligibility.code.toUpperCase().contains("SUCCESS")
                || eligibility.data == null || eligibility.data.user == null) {
            throw new IllegalStateException("Mission eligibility API (" + missionId + ", accountId=" + accountId
                    + ") returned an unexpected payload (code=" + (eligibility != null ? eligibility.code : "null")
                    + ") - cannot read the user's mission status.");
        }
        return eligibility;
    }

    /**
     * Reads the missions the consumer Mission List Page ({@code /id-sg/mission}) presents, in the order it
     * presents them, from {@code GET /tix-mission/api/mission-list}.
     *
     * <p>Called as the anonymous caller so the result is the un-segmented view: every mission that is live,
     * rather than the subset a particular account is targeted by. That keeps the list stable across runs and
     * independent of any one test account's segments and already-joined missions.
     *
     * <p>Retries the HTTP call three times with a 3s backoff so a transient 5xx does not fail the read.
     *
     * @return the slugs in the "Other Missions" tab, in the order the Mission List Page lists them
     * @throws IllegalStateException if the API returns a non-2xx status or an unsuccessful payload, so an
     *                               API failure surfaces instead of silently comparing against an empty list
     */
    public List<String> getLiveMissionSlugs() {
        return fetchMissionListWithRetry().getAvailableSlugs();
    }

    /** Mobile and desktop banner URLs the consumer Mission List Page serves from {@code data.banner.imageUrl}. */
    public record MissionBannerImages(String mobileUrl, String desktopUrl) {
    }

    /**
     * Reads the page-level mobile and desktop banner URLs from {@code GET /tix-mission/api/mission-list}.
     * This is what the consumer Mission List Page actually renders, so it is the source of truth for
     * whether a Mission Center header-image edit was published.
     */
    public MissionBannerImages getMissionBannerImages() {
        return readMissionBannerImages(fetchMissionListWithRetry());
    }

    /**
     * Re-reads {@link #getMissionBannerImages()} a bounded number of times. Use this for negative
     * assertions (Cancel must not publish) so a delayed publish cannot slip through a single early
     * read and false-pass.
     *
     * @return {@code expected} when every check matched; otherwise the first mismatched payload so
     *         the caller can assert on the real URLs
     */
    public MissionBannerImages waitForMissionBannerImagesToRemain(MissionBannerImages expected) {
        final int checks = 3;
        final int intervalSeconds = 3;
        MissionBannerImages lastSeen = expected;
        for (int attempt = 1; attempt <= checks; attempt++) {
            lastSeen = getMissionBannerImages();
            if (!expected.equals(lastSeen)) {
                logger.log("Mission List Page banners changed (check " + attempt + "/" + checks
                        + ", live=" + lastSeen + ")");
                return lastSeen;
            }
            logger.log("Mission List Page banners still match on check " + attempt + "/" + checks);
            if (attempt < checks) {
                sleepQuietly(intervalSeconds * 1000L);
            }
        }
        return lastSeen;
    }

    /**
     * Polls {@link #getLiveMissionSlugs()} until the consumer list matches the Mission Center sequence
     * narrowed to those live missions. Saving on the admin panel is eventually consistent with the
     * mission-list read, so a single shot can still see the previous order.
     *
     * @return the last list read — the matching one on success, or the final attempt so the caller
     *         can assert on the real mismatch
     */
    public List<String> waitForLiveMissionsToFollowConfiguredOrder(List<String> configuredOrder) {
        final int maxAttempts = 6;
        final int intervalSeconds = 3;
        List<String> liveSlugs = List.of();
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            liveSlugs = getLiveMissionSlugs();
            if (liveMissionsFollowConfiguredOrder(configuredOrder, liveSlugs)) {
                logger.log("Mission List Page matches configured order on attempt " + attempt
                        + "/" + maxAttempts);
                return liveSlugs;
            }
            logger.log("Mission List Page order not yet matching configured sequence (attempt "
                    + attempt + "/" + maxAttempts + ", live=" + liveSlugs + "); retrying in "
                    + intervalSeconds + "s...");
            if (attempt < maxAttempts) {
                sleepQuietly(intervalSeconds * 1000L);
            }
        }
        return liveSlugs;
    }

    private MissionListResponse fetchMissionListWithRetry() {
        final int maxRetries = 3;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return fetchMissionListResponse();
            } catch (RuntimeException e) {
                if (attempt == maxRetries) {
                    throw e;
                }
                logger.log("Attempt " + attempt + " failed: " + e.getMessage() + ". Retrying...");
                sleepQuietly(3000);
            }
        }
        throw new IllegalStateException("Mission list API failed after " + maxRetries + " attempts.");
    }

    private MissionListResponse fetchMissionListResponse() {
        MissionListApi api = new MissionListApi(
                new MissionListApi.Inputs(ANONYMOUS_ACCOUNT_ID, ANONYMOUS_USERNAME));
        ApiResponse response = api.hitApi(apiTools);
        int statusCode = response.getStatusCode();
        logger.log("Mission list API response: " + statusCode);
        if (statusCode < 200 || statusCode >= 300) {
            throw new IllegalStateException("Mission list API failed with HTTP " + statusCode
                    + " - cannot read the missions the Mission List Page shows.");
        }
        MissionListResponse missionList = new Gson().fromJson(response.getBody(), MissionListResponse.class);
        if (missionList == null || missionList.code == null
                || !missionList.code.toUpperCase().contains("SUCCESS")
                || missionList.data == null || missionList.data.tabs == null) {
            throw new IllegalStateException("Mission list API returned an unexpected payload (code="
                    + (missionList != null ? missionList.code : "null")
                    + ") - cannot read the missions the Mission List Page shows.");
        }
        List<String> slugs = missionList.getAvailableSlugs();
        logger.log("Mission List Page shows " + slugs.size() + " live mission(s): " + slugs);
        return missionList;
    }

    private MissionBannerImages readMissionBannerImages(MissionListResponse missionList) {
        MissionListResponse.ImageUrl imageUrl = missionList.data.banner == null
                ? null
                : missionList.data.banner.imageUrl;
        if (imageUrl == null || isBlank(imageUrl.mobile) || isBlank(imageUrl.desktop)) {
            throw new IllegalStateException("Mission list API returned no mobile/desktop banner URL"
                    + " - cannot verify the Mission List Page header images.");
        }
        logger.log("Mission List Page banner mobile=" + imageUrl.mobile + " desktop=" + imageUrl.desktop);
        return new MissionBannerImages(imageUrl.mobile, imageUrl.desktop);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static boolean liveMissionsFollowConfiguredOrder(List<String> configuredOrder, List<String> liveSlugs) {
        if (configuredOrder == null || liveSlugs == null || liveSlugs.isEmpty()) {
            return false;
        }
        List<String> expected = new ArrayList<>(configuredOrder);
        expected.retainAll(liveSlugs);
        return liveSlugs.equals(expected);
    }

    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting between mission API calls", e);
        }
    }

    /**
     * Resolves account data for an email, retrying briefly to absorb member-service propagation lag
     * right after registration (so callers never NPE on a transiently-null lookup).
     *
     * @throws IllegalStateException if the account cannot be resolved within the retry budget
     */
    private long resolveAccountId(String username) {
        return resolvedAccountIds.computeIfAbsent(username, email -> resolveAccountData(email).accountId);
    }

    private AccountData.Data resolveAccountData(String username) {
        MemberApi<?> memberApi = new MemberApi<>(session);
        final int maxAttempts = 5;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            AccountData.Data accountData = memberApi.getAccountDataByEmail(username);
            if (accountData != null) {
                return accountData;
            }
            logger.log("Account data not yet available for '" + username + "' (attempt " + attempt + "/"
                    + maxAttempts + ") - likely member-service propagation lag after registration; retrying...");
            if (attempt < maxAttempts) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        throw new IllegalStateException("Could not resolve account data for username '" + username + "' after "
                + maxAttempts + " attempts - the account may not have propagated to the member service yet.");
    }

    /**
     * Polls the mission detail API until {@code condition} holds for the mission, or the retry budget is
     * exhausted. This is the generic building block for status checks: pass any predicate to wait for a
     * single mission to reach a specific state (reward claimable, a given reward status, a stamp count,
     * etc.) and reuse it across cases.
     *
     * @param missionSlug          the mission slug to poll
     * @param username             the account email whose progress to read
     * @param condition            the state to wait for (evaluated against the latest detail response)
     * @param conditionDescription a short label for logging, e.g. {@code "reward claimable"}
     * @param maxAttempts          maximum number of polls
     * @param intervalSeconds      delay between polls
     * @return the last {@link MissionDetailResponse} read (the one that satisfied {@code condition}, or
     *         the final attempt's response if it never did)
     */
    public MissionDetailResponse waitForMissionState(String missionSlug, String username,
                                                     Predicate<MissionDetailResponse> condition,
                                                     String conditionDescription, int maxAttempts,
                                                     int intervalSeconds) {
        AccountData.Data accountData = resolveAccountData(username);
        String accountId = String.valueOf(accountData.accountId);
        MissionDetailResponse detail = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            detail = getMissionDetail(missionSlug, accountId, username);
            if (detail != null && condition.test(detail)) {
                logger.log("Mission '" + missionSlug + "' reached state [" + conditionDescription
                        + "] on attempt " + attempt + "/" + maxAttempts);
                return detail;
            }
            logger.log("Mission '" + missionSlug + "' not yet [" + conditionDescription + "] (attempt " + attempt
                    + "/" + maxAttempts + ", stamps=" + (detail != null
                        ? detail.getCompletedStampCount() + "/" + detail.getTotalStampCount() : "n/a")
                    + ", rewardStatus=" + (detail != null ? detail.getRewardStatus() : "n/a")
                    + ", cta=" + (detail != null ? detail.getCtaType() : "n/a") + "); retrying in "
                    + intervalSeconds + "s...");
            if (attempt < maxAttempts) {
                try {
                    Thread.sleep(intervalSeconds * 1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        return detail;
    }

    /**
     * @return the total number of stamps the mission currently requires (the size of {@code userProgress}
     *         in the mission detail API). Read this at the start of a run to drive how many stamps to
     *         inject, instead of hard-coding the count.
     */
    public int getMissionTotalStamps(String missionSlug, String username) {
        return getMissionDetail(missionSlug, username).getTotalStampCount();
    }

    /**
     * Waits until <b>every</b> stamp of the mission is marked COMPLETE (collected == required), polling
     * the mission detail API. This makes a completion flow truly dynamic: read the required count with
     * {@link #getMissionTotalStamps}, inject that many, then wait here for all stamps to flip to COMPLETE
     * rather than guessing with a fixed wait.
     *
     * @param expectedTotal the required stamp count read earlier (guards against a partial detail payload)
     * @return {@code true} if all {@code expectedTotal} stamps reached COMPLETE within the budget
     */
    public boolean waitForAllStampsComplete(String missionSlug, String username, int expectedTotal,
                                            int maxAttempts, int intervalSeconds) {
        MissionDetailResponse detail = waitForMissionState(missionSlug, username,
                d -> d.getTotalStampCount() == expectedTotal && d.areAllStampsComplete(),
                "all " + expectedTotal + " stamps complete", maxAttempts, intervalSeconds);
        return detail != null && detail.getTotalStampCount() == expectedTotal && detail.areAllStampsComplete();
    }

    /**
     * Waits until the mission's reward is claimable (mission complete: CTA {@code CLAIM_REWARD} or
     * reward status {@code COMPLETED}). Use this in place of a fixed {@code Thread.sleep} after
     * completing a mission.
     *
     * @return {@code true} if the reward became claimable within the budget
     */
    public boolean waitForMissionRewardClaimable(String missionSlug, String username, int maxAttempts,
                                                 int intervalSeconds) {
        MissionDetailResponse detail = waitForMissionState(missionSlug, username,
                MissionDetailResponse::isRewardClaimable, "reward claimable", maxAttempts, intervalSeconds);
        return detail != null && detail.isRewardClaimable();
    }

    /**
     * Waits until the mission's reward reaches {@code expectedStatus} (e.g. {@code "COMPLETED"},
     * {@code "CLAIMED"}). Reusable for any case that asserts a specific reward status transition.
     *
     * @return {@code true} if the reward reached {@code expectedStatus} within the budget
     */
    public boolean waitForMissionRewardStatus(String missionSlug, String username, String expectedStatus,
                                              int maxAttempts, int intervalSeconds) {
        MissionDetailResponse detail = waitForMissionState(missionSlug, username,
                d -> d.hasRewardStatus(expectedStatus), "reward status=" + expectedStatus, maxAttempts,
                intervalSeconds);
        return detail != null && detail.hasRewardStatus(expectedStatus);
    }

    /**
     * @return the current reward status of the mission for the given user (e.g. {@code "ACTIVE"},
     *         {@code "COMPLETED"}), or {@code null} if unavailable.
     */
    public String getMissionRewardStatus(String missionSlug, String username) {
        return getMissionDetail(missionSlug, username).getRewardStatus();
    }

    /**
     * Polls the mission detail API until the collected-stamp count rises ABOVE {@code baselineCount}
     * (i.e. an eligible transaction granted a stamp). Use this for positive checks so the assertion does
     * not run before the mission engine has asynchronously applied the stamp.
     *
     * @param baselineCount the stamp count captured before the eligible transaction
     * @return the collected-stamp count once it exceeds {@code baselineCount}; if it never does within the
     *         budget, the last count seen (so the caller can assert on the actual value)
     */
    public int waitForStampCountAbove(String missionSlug, String username, int baselineCount, int maxAttempts,
                                      int intervalSeconds) {
        MissionDetailResponse detail = waitForMissionState(missionSlug, username,
                d -> d.getCompletedStampCount() > baselineCount, "collected stamps > " + baselineCount,
                maxAttempts, intervalSeconds);
        return detail != null ? detail.getCompletedStampCount() : -1;
    }

    /**
     * @return the mission's accumulated GBV (Gross Booking Value) spend for the given user, i.e. the
     *         eligible amount the mission engine has credited (i.e. {@code gbvProgress.totalSpend},
     *         e.g. {@code 563546}). {@code 0} if the user has no progress yet.
     */
    public long getMissionGbvProgress(String missionSlug, String username) {
        return getMissionDetail(missionSlug, username).getGbvSpend();
    }

    /**
     * Polls the mission detail API until the GBV (Gross Booking Value) spend rises ABOVE
     * {@code baselineProgress} (i.e. an eligible transaction's spend was credited to the mission). Use
     * this for positive checks so the assertion does not run before the mission engine has asynchronously
     * applied the booking value.
     *
     * @param baselineProgress the GBV spend captured before the eligible transaction
     * @return the GBV spend once it exceeds {@code baselineProgress}; if it never does within the
     *         budget, the last value seen (so the caller can assert on the actual value)
     * @throws IllegalStateException if {@code maxAttempts < 1}, so no read was ever performed
     */
    public long waitForGbvProgressAbove(String missionSlug, String username, long baselineProgress,
                                        int maxAttempts, int intervalSeconds) {
        MissionDetailResponse detail = waitForMissionState(missionSlug, username,
                d -> d.getGbvSpend() > baselineProgress, "GBV spend > " + baselineProgress,
                maxAttempts, intervalSeconds);
        // waitForMissionState only returns null when the loop never ran (maxAttempts < 1); getMissionDetail
        // throws on any API failure, so a real timeout still yields the last-read detail. Return that
        // detail's spend (often the unchanged baseline) so the caller's assertion fails on the real value.
        if (detail == null) {
            throw new IllegalStateException("waitForGbvProgressAbove requires maxAttempts >= 1 (was "
                    + maxAttempts + ")");
        }
        return detail.getGbvSpend();
    }

    /**
     * Confirms a mission's collected-stamp count stays at {@code expectedCount} for a bounded window,
     * re-reading the mission detail API {@code checks} times. Use this for negative assertions (e.g.
     * "Pay at Hotel must NOT grant a stamp") so a delayed/asynchronous stamp increment cannot slip
     * through a single early read and false-pass the test.
     *
     * @param expectedCount   the collected-stamp count that must not change (the baseline)
     * @param checks          how many times to re-read (≥ 1)
     * @param intervalSeconds delay between checks
     * @return {@code true} if the count equalled {@code expectedCount} on every check; {@code false} as
     *         soon as it deviates
     */
    public boolean isStampCountStableAt(String missionSlug, String username, int expectedCount, int checks,
                                        int intervalSeconds) {
        for (int attempt = 1; attempt <= checks; attempt++) {
            int collected = getMissionDetail(missionSlug, username).getCompletedStampCount();
            logger.log("Stamp-stability check " + attempt + "/" + checks + " for mission '" + missionSlug
                    + "': collected=" + collected + " (expected " + expectedCount + ")");
            if (collected != expectedCount) {
                return false;
            }
            if (attempt < checks) {
                try {
                    Thread.sleep(intervalSeconds * 1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    // Not all scheduled re-reads ran, so stability is unconfirmed. Returning false (rather
                    // than true) prevents a negative assertion from false-passing before a delayed stamp
                    // increment could have been observed.
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return the loyalty core point-history for the given account id (most recent first).
     */
    public PointHistoryResponse getPointHistory(long accountId) {
        PointHistoryApi api = new PointHistoryApi(new PointHistoryApi.Inputs(accountId));
        ApiResponse response = api.hitApi(apiTools);
        logger.log("Point history API response (accountId=" + accountId + "): " + response.getStatusCode());
        return new Gson().fromJson(response.getBody(), PointHistoryResponse.class);
    }

    /**
     * Polls the point-history API and returns the <i>actual</i> earned point amount credited for the
     * claimed reward, so callers can assert the credited amount equals what the UI showed (instead of
     * just a true/false "reflected" check). Reward crediting can lag a few seconds behind the UI claim.
     *
     * @param accountId     the account that claimed the reward
     * @param expectedPoint the point amount shown on the UI reward popup (the value we want to match)
     * @return the matching earned point amount once it appears in the history; if no matching entry is
     *         found within the retry budget, returns the latest earned-reward amount seen (or {@code -1}
     *         if the account has no earned-reward entry at all) so the assertion can report the mismatch.
     */
    public int getCreditedRewardPoint(long accountId, int expectedPoint) {
        final int maxAttempts = 6;
        int latestEarnedSeen = -1;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            PointHistoryResponse history = null;
            try {
                history = getPointHistory(accountId);
            } catch (Exception e) {
                logger.log("Point history API call failed on attempt " + attempt + "/" + maxAttempts + " (" + e.getClass().getSimpleName() + ": " + e.getMessage() + "); will retry.");
            }
            if (history != null && history.data != null && history.data.content != null) {
                for (PointHistoryResponse.Content c : history.data.content) {
                    boolean reward = isRewardTypeEntry(c);
                    boolean earned = "Earned".equalsIgnoreCase(c.pointActivityType)
                            || "Earned".equalsIgnoreCase(c.pointStatus);
                    // Require a REWARD-type earned entry (mirrors getLatestEarnedRewardPoint) so an unrelated
                    // earned line that happens to carry the same point value cannot satisfy the poll before
                    // the mission reward claim is actually credited.
                    if (!(reward && earned)) {
                        continue;
                    }
                    if (latestEarnedSeen == -1) {
                        latestEarnedSeen = c.point;
                    }
                    if (c.point == expectedPoint) {
                        logger.log("Found credited reward point in history: point=" + c.point
                                + ", orderName=" + c.orderName + ", type=" + c.type
                                + ", pointStatus=" + c.pointStatus);
                        return c.point;
                    }
                }
            }
            if (attempt < maxAttempts) {
                logger.log("Reward point " + expectedPoint + " not yet reflected in point history (attempt "
                        + attempt + "/" + maxAttempts + "); retrying in 5s...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        return latestEarnedSeen;
    }

    /**
     * @return the point value of the latest <i>earned reward</i> entry in the account's point history,
     *         or {@code null} if none is found. Useful when the reward amount is not read from the UI.
     */
    public Integer getLatestEarnedRewardPoint(long accountId) {
        PointHistoryResponse history = getPointHistory(accountId);
        if (history == null || history.data == null || history.data.content == null) {
            return null;
        }
        for (PointHistoryResponse.Content c : history.data.content) {
            boolean reward = isRewardTypeEntry(c);
            boolean earned = "Earned".equalsIgnoreCase(c.pointActivityType);
            if (reward && earned && c.point > 0) {
                return c.point;
            }
        }
        return null;
    }

    /**
     * @return {@code true} if the point-history entry is a reward credit. The loyalty core types reward
     *         rows as {@code REWARD} or {@code MISSION_REWARDS} (mission reward claims), so we match any
     *         {@code type}/{@code orderType} that contains {@code "REWARD"} rather than an exact value.
     */
    private static boolean isRewardTypeEntry(PointHistoryResponse.Content c) {
        return containsReward(c.type) || containsReward(c.orderType);
    }

    private static boolean containsReward(String value) {
        return value != null && value.toUpperCase().contains("REWARD");
    }

    public void injectTiketPoint(int pdAccountId, int pdPoint, String pdProcessType, int pdRatio, String username)
            throws InterruptedException, ExecutionException {
        AddActivePointsApi.Inputs inputs = new AddActivePointsApi.Inputs(pdAccountId, pdPoint, pdProcessType, pdRatio,
                username);
        AddActivePointsApi api = new AddActivePointsApi(inputs);
        ApiResponse apiResponse = api.hitApi(apiTools);
        logger.log("response : " + apiResponse);
        Assertion.assertThat("Inject api success", apiResponse.getStatusCode(), is(200));
    }

    /**
     * This method injects Tiket points based on the provided conditions.
     * If the total points are greater than the required points and the condition is "half",
     * it deletes the extra points before injecting new points.
     *
     * @param pdPoint        The required points to be injected.
     * @param pdProcessType  The process type for the points.
     * @param pdRatio        The ratio for the points.
     * @param username       The username of the account.
     * @param totalPoint     The total available points as a string.
     * @param condition      The condition to check before injecting points.
     * @return               The total points after injection or deletion.
     */
    @SneakyThrows
    public String injectTiketPointsBasedOnCondition(
            int pdPoint,
             String pdProcessType,
             int pdRatio,
             String username,
             String totalPoint,
             String condition
    ){

        MemberApi<?> memberApi = new MemberApi<>(session);
        AccountData.Data accountData = memberApi.getAccountDataByEmail(username);
        int pdAccountId = (int)accountData.accountId;
        accountIdMap.put(username, pdAccountId);
        logger.log("accountId: " + pdAccountId + " for username: " + username);


        totalPoint = totalPoint.replaceAll("[^0-9]", "");
        if (totalPoint.isBlank()) {
            totalPoint = "0";
        }
        String accountUsername = accountData.accountUsername != null ? accountData.accountUsername : username;
        int totalPointInt;
        try {
            totalPointInt = readActiveTiketPoints(pdAccountId, accountUsername);
        } catch (Exception e) {
            logger.log("Falling back to UI tiket points after API read failed: " + e.getMessage());
            totalPointInt = Integer.parseInt(totalPoint);
        }
        if (totalPointInt > pdPoint && condition.equalsIgnoreCase("half")) {

            String dashboardCookieHeader = memberApi.getAdminDashboardCookieHeader();
            deleteTiketPoint(username,pdProcessType,totalPointInt - pdPoint,dashboardCookieHeader);
            totalPointInt = readActiveTiketPoints(pdAccountId, accountUsername);
        }
        if (totalPointInt < pdPoint) {
            logger.log("total tiket points available : " + totalPoint);
            logger.log("total tiket point required : " + pdPoint);

            int newPdPoint = pdPoint - totalPointInt;
            injectTiketPoint(pdAccountId, newPdPoint, pdProcessType, pdRatio, username);
            return String.valueOf(pdPoint);
        }
        canBeBookForPointsBasedOnCondition(pdPoint,totalPointInt,condition);
        return String.valueOf(totalPointInt);
    }

    private int readActiveTiketPoints(long accountId, String username) {
        ApiResponse response = new ActiveTiketPointsApi(new ActiveTiketPointsApi.Inputs(accountId, username)).hitApi(apiTools);
        ActiveTiketPointsResponse parsed = new Gson().fromJson(response.getBody(), ActiveTiketPointsResponse.class);
        if (parsed != null && parsed.data != null && parsed.data.trim().matches("-?\\d+")) {
            logger.log("active tiket points after adjust: " + parsed.data);
            return Integer.parseInt(parsed.data.trim());
        }
        throw new IllegalStateException("Could not read active tiket points for accountId=" + accountId
                + " status=" + response.getStatusCode() + " body=" + response.getBody());
    }

    @SneakyThrows
    public void deleteTiketPoint(
            String emailOfUserWHosePointsToBeDeleted,
            String pdProcessType,
            int extraPoint,
            String accessToken
    ) {
        ActivePointDetailsApi.Inputs inputs = new ActivePointDetailsApi.Inputs(pdProcessType, accessToken, emailOfUserWHosePointsToBeDeleted);
        ActivePointDetailsApiResponse activePointDetailsApiResponse = getActivePointDetails(inputs);
        long pointId = searchPointIdToBeDeleted(extraPoint,activePointDetailsApiResponse);
        logger.log("pointId found: " + pointId + ", extraPoint left: " + extraPoint);

        if(pointId==-1){
            //delete smaller points until the extraPoint is satisfied
            for(int i=0;i<3;i++){
                Map<String,Long> value= searchPointIdToBeDeletedSmallerPoint(extraPoint,activePointDetailsApiResponse);
                pointId = value.get("pointId");
                extraPoint = value.get("pointLeft").intValue();
                logger.log("pointId to be deleted: " + pointId + ", extraPoint left: " + extraPoint);

                if(pointId == -1){
                    logger.log("No suitable pointId found for extraPoint: " + extraPoint);
                    break;
                }
                commonDeletionStepTiketPoint(accessToken, pointId, pdProcessType, emailOfUserWHosePointsToBeDeleted);
                Thread.sleep(3000);
                if(extraPoint <= 0){
                    logger.log("Extra points satisfied, breaking the loop.");
                    break;
                }
                activePointDetailsApiResponse = getActivePointDetails(inputs);
            }
        }else{
            commonDeletionStepTiketPoint(accessToken, pointId, pdProcessType, emailOfUserWHosePointsToBeDeleted);
        }
    }

    private void commonDeletionStepTiketPoint(String accessToken,long pointId, String pdProcessType, String emailOfUserWHosePointsToBeDeleted) throws ExecutionException, InterruptedException {
        RequestDeleteTiketPointsApi.Inputs deleteInputs = new RequestDeleteTiketPointsApi.Inputs(accessToken, pointId, pdProcessType, emailOfUserWHosePointsToBeDeleted);
        RequestDeleteTiketPointsApiResponse requestDeleteTiketPointsApiResponse = requestDeleteTiketPoints(deleteInputs);
        long pointIdToBeDeleted = requestDeleteTiketPointsApiResponse.data.pointId;
        long amountToBeDeleted = requestDeleteTiketPointsApiResponse.data.amount;
        logger.log("pointId to be deleted: " + pointIdToBeDeleted + ", type: " + requestDeleteTiketPointsApiResponse.data.type);

        TiketPointsApprovalListApi.Inputs approvalListInputs = new TiketPointsApprovalListApi.Inputs(accessToken, emailOfUserWHosePointsToBeDeleted);
        TiketPointsApprovalListApiResponse tiketPointsApprovalListApiResponse = getTiketPointsApprovalList(approvalListInputs);

        long approvalId = approvalIdToBeDeleted(tiketPointsApprovalListApiResponse,pointIdToBeDeleted,requestDeleteTiketPointsApiResponse.data.type);;

        ApproveTiketPointApi.Inputs approveInputs = new ApproveTiketPointApi.Inputs(approvalId,accessToken);
        ApproveTiketPointApiResponse approveTiketPointApiResponse = approveTiketPoint(approveInputs);
        logger.log("successfully deleted tiket point with approvalId: " + approveTiketPointApiResponse.message);
        Assertion.assertThat("Tiket point deletion successful with point : {" + amountToBeDeleted + " }", approveTiketPointApiResponse.message, is("SUCCESS"));
    }

    private ActivePointDetailsApiResponse getActivePointDetails(ActivePointDetailsApi.Inputs inputs) throws ExecutionException, InterruptedException {
        logger.log("getActivePointDetails with inputs: " + inputs.processType() + ", username=" + inputs.username());
        ActivePointDetailsApi activePointDetailsApi = new ActivePointDetailsApi(inputs);
        ApiResponse apiResponse = activePointDetailsApi.hitApi(apiTools);
        Gson gson = new Gson();
        ActivePointDetailsApiResponse parsed = gson.fromJson(apiResponse.getBody(), ActivePointDetailsApiResponse.class);
        if (parsed == null || parsed.data == null) {
            throw new IllegalStateException("point-detail API failed: status=" + apiResponse.getStatusCode()
                    + " body=" + apiResponse.getBody());
        }
        return parsed;
    }

    private RequestDeleteTiketPointsApiResponse requestDeleteTiketPoints(RequestDeleteTiketPointsApi.Inputs deleteInputs){
        logger.log("requestDeleteTiketPoints pointId=" + deleteInputs.pointId() + ", username=" + deleteInputs.username());
        RequestDeleteTiketPointsApi requestDeleteTiketPointsApi = new RequestDeleteTiketPointsApi(deleteInputs);
        ApiResponse apiResponse = requestDeleteTiketPointsApi.hitApi(apiTools);
        Gson gson = new Gson();
        RequestDeleteTiketPointsApiResponse parsed = gson.fromJson(apiResponse.getBody(), RequestDeleteTiketPointsApiResponse.class);
        if (parsed == null || parsed.data == null) {
            throw new IllegalStateException("request-delete API failed: status=" + apiResponse.getStatusCode()
                    + " body=" + apiResponse.getBody());
        }
        return parsed;
    }

    private TiketPointsApprovalListApiResponse getTiketPointsApprovalList(TiketPointsApprovalListApi.Inputs inputs) throws ExecutionException, InterruptedException {
        logger.log("getTiketPointsApprovalList username=" + inputs.username());
        TiketPointsApprovalListApi tiketPointsApprovalListApi = new TiketPointsApprovalListApi(inputs);
        ApiResponse apiResponse = tiketPointsApprovalListApi.hitApi(apiTools);
        logger.log("API Response: " + apiResponse.getBody());
        Gson gson = new Gson();
        TiketPointsApprovalListApiResponse parsed = gson.fromJson(apiResponse.getBody(), TiketPointsApprovalListApiResponse.class);
        if (parsed == null || parsed.data == null) {
            throw new IllegalStateException("approval-list API failed: status=" + apiResponse.getStatusCode()
                    + " body=" + apiResponse.getBody());
        }
        return parsed;
    }

    private ApproveTiketPointApiResponse approveTiketPoint(ApproveTiketPointApi.Inputs inputs) throws ExecutionException, InterruptedException {
        ApproveTiketPointApi approveTiketPointApi = new ApproveTiketPointApi(inputs);
        ApiResponse apiResponse;
        logger.log("approveTiketPoint with inputs: " + inputs);
        try {
            apiResponse = approveTiketPointApi.hitApi(apiTools);
        }catch (Exception e){
            logger.log("Error while approving tiket point: " + e.getMessage());
            logger.log("Retrying to approve tiket point...");
            apiResponse = approveTiketPointApi.hitApi(apiTools);
        }
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), ApproveTiketPointApiResponse.class);
    }

    private long searchPointIdToBeDeleted(int extraPoint,ActivePointDetailsApiResponse activePointDetailsApiResponse){
        // using the extraPoint to find suitable pointId to be deleted
        logger.log("searchPointIdToBeDeleted with extraPoint: " + extraPoint);
        for (ActivePointDetailsApiResponse.PointDetail data : activePointDetailsApiResponse.data) {
            if (data.deleteAction && data.pointLeft >= extraPoint) {
                logger.log("searchPointIdToBeDeleted: Found pointId: " + data.pointId + " with pointLeft: " + data.pointLeft);
                return data.pointId;
            }
        }
        return -1; // return -1 if no suitable pointId found
    }

    private Map<String,Long> searchPointIdToBeDeletedSmallerPoint(int extraPoint, ActivePointDetailsApiResponse activePointDetailsApiResponse){
        // Sort the data list by pointLeft in descending order
        List<ActivePointDetailsApiResponse.PointDetail> sortedList = new ArrayList<>(activePointDetailsApiResponse.data);
        sortedList.sort((a, b) -> Long.compare(b.pointLeft, a.pointLeft));
        // using the extraPoint to find suitable pointId to be deleted
        for (ActivePointDetailsApiResponse.PointDetail data : sortedList) {
            if (data.deleteAction) {
                logger.log("searchPointIdToBeDeletedSmallerPoint: Found pointId: " + data.pointId + " with pointLeft: " + data.pointLeft);
                return Map.of(
                        "pointId", data.pointId,
                        "pointLeft", extraPoint - data.pointLeft
                );
            }
        }
        logger.log("No suitable pointId found for extraPoint: " + extraPoint);
        return Map.of(
                "pointId", (long) -1,
                "pointLeft", (long) extraPoint
        );
    }

    private long approvalIdToBeDeleted(TiketPointsApprovalListApiResponse tiketPointsApprovalListApiResponse,long pointId,String type) {
        // Logic to find the approvalId based on pointId and type
        logger.log("approvalIdToBeDeleted: Searching for approvalId with pointId: " + pointId + " and type: " + type);
        for (TiketPointsApprovalListApiResponse.Data data : tiketPointsApprovalListApiResponse.data) {
            if (data.pointId != null && data.pointId == pointId && data.type.equals(type)) {
                logger.log("approvalIdToBeDeleted: Found approvalId: " + data.id + " for pointId: " + pointId + " and type: " + type);
                return data.id;
            }
        }
        throw new RuntimeException("No suitable approvalId found for pointId: " + pointId + " and type: " + type);
    }

    @SneakyThrows
    public GetActivePointApiResponse.Data getActivePointsData(String searchBy,String username,String accessToken) {
        GetActivePointApi.Inputs inputs = new GetActivePointApi.Inputs(searchBy,username,accessToken);
        GetActivePointApi getActivePointApi = new GetActivePointApi(inputs);
        ApiResponse apiResponse = getActivePointApi.hitApi(apiTools);
        Gson gson = new Gson();
        GetActivePointApiResponse getActivePointApiResponse = gson.fromJson(apiResponse.getBody(), GetActivePointApiResponse.class);
        logger.log("Active Points Data: " + getActivePointApiResponse);
        return getActivePointApiResponse.data;
    }

    public void verifyOrderIdNotInPointHistory(String orderId, long accountId) {
        PointHistoryApi pointHistoryApi = new PointHistoryApi(new PointHistoryApi.Inputs(Math.toIntExact(accountId)));
        ApiResponse response = pointHistoryApi.hitApi(apiTools);
        Assertion.assertThat("Point history API call should succeed", response.getStatusCode(), is(200));

        JsonObject pointHistoryResponse = new Gson().fromJson(response.getBody(), JsonObject.class);
        JsonArray pointHistoryContent = pointHistoryResponse.getAsJsonObject("data").getAsJsonArray("content");
        for (JsonElement jsonElement : pointHistoryContent) {
            JsonObject item = jsonElement.getAsJsonObject();
            if (item.has("orderId") && !item.get("orderId").isJsonNull()
                    && orderId.equals(item.get("orderId").getAsString())) {
                Assertion.fail("Order id " + orderId + " should not be present in point history but was found");
            }
        }
        logger.log("Verified order id " + orderId + " is not present in point history for accountId: " + accountId);
    }

    public void verifyOrderIdInPointHistoryWithType(String orderId, long accountId, String expectedType, int retryCount)
            throws InterruptedException {
        JsonObject orderIdData = null;
        for (int attempt = 1; attempt <= retryCount; attempt++) {
            PointHistoryApi pointHistoryApi = new PointHistoryApi(new PointHistoryApi.Inputs(Math.toIntExact(accountId)));
            ApiResponse response = pointHistoryApi.hitApi(apiTools);
            Assertion.assertThat("Point history API call should succeed", response.getStatusCode(), is(200));

            JsonObject pointHistoryResponse = new Gson().fromJson(response.getBody(), JsonObject.class);
            JsonArray pointHistoryContent = pointHistoryResponse.getAsJsonObject("data").getAsJsonArray("content");
            for (JsonElement jsonElement : pointHistoryContent) {
                JsonObject item = jsonElement.getAsJsonObject();
                if (item.has("orderId") && !item.get("orderId").isJsonNull()
                        && orderId.equals(item.get("orderId").getAsString())) {
                    orderIdData = item;
                    break;
                }
            }
            if (orderIdData != null) {
                break;
            }
            if (attempt < retryCount) {
                logger.log("Order id " + orderId + " not found in point history yet, retrying... (" + attempt + "/" + retryCount + ")");
                TimeUnit.SECONDS.sleep(5);
            }
        }

        Assertion.assertThat("Order id should be present in point history", orderIdData, notNullValue());
        String type = orderIdData.has("type") && !orderIdData.get("type").isJsonNull()
                ? orderIdData.get("type").getAsString()
                : "";
        Assertion.assertThat("Point history type should be " + expectedType, type, is(expectedType));
        logger.log("Verified order id " + orderId + " is present in point history with type: " + type);
    }

    public void verifyRefundPointsInPointHistory(String orderId, long accountId, int expectedPromoPoints, int retryCount)
            throws InterruptedException {
        String expectedOrderIdFragment = "TIK00002#" + orderId;
        String expectedOrderTransaction = "Product transaction (Refund)";

        List<JsonObject> refundEntries = new ArrayList<>();
        JsonObject negativeEntry = null;
        JsonObject positiveEntry = null;

        for (int attempt = 1; attempt <= retryCount; attempt++) {
            refundEntries.clear();
            negativeEntry = null;
            positiveEntry = null;

            PointHistoryApi pointHistoryApi = new PointHistoryApi(new PointHistoryApi.Inputs(Math.toIntExact(accountId)));
            ApiResponse response = pointHistoryApi.hitApi(apiTools);
            logger.log("API response - " + response);
            Assertion.assertThat("Point history API call should succeed", response.getStatusCode(), is(200));

            JsonObject pointHistoryResponse = new Gson().fromJson(response.getBody(), JsonObject.class);
            JsonArray pointHistoryContent = pointHistoryResponse.getAsJsonObject("data").getAsJsonArray("content");
            for (JsonElement jsonElement : pointHistoryContent) {
                JsonObject item = jsonElement.getAsJsonObject();
                if (!item.has("orderId") || item.get("orderId").isJsonNull()) {
                    continue;
                }

                String entryOrderId = item.get("orderId").getAsString();
                if (!entryOrderId.contains(expectedOrderIdFragment)) {
                    continue;
                }

                if (!isRefundOrderTransaction(item, expectedOrderTransaction)) {
                    continue;
                }

                refundEntries.add(item);
                int point = item.get("point").getAsInt();
                if (point == -expectedPromoPoints) {
                    negativeEntry = item;
                } else if (point == expectedPromoPoints) {
                    positiveEntry = item;
                }
            }

            if (negativeEntry != null && positiveEntry != null) {
                break;
            }
            if (attempt < retryCount) {
                logger.log("Refund point history entries not ready yet, retrying... (" + attempt + "/" + retryCount + ")");
                Thread.sleep(3000);
            }
        }

        Assertion.assertThat("Negative refund point entry should be present", negativeEntry, notNullValue());

        String negativeOrderId = negativeEntry.get("orderId").getAsString();
        Assertion.assertThat("Refund orderId should contain payment order id", negativeOrderId, containsString(orderId));
        Assertion.assertThat("Refund orderId should contain TIK00002#orderId format", negativeOrderId, containsString(expectedOrderIdFragment));
        Assertion.assertThat("Refund pointStatus should be Redeemed", getJsonString(negativeEntry, "pointStatus"), is("Redeemed"));
        Assertion.assertThat("Refund order transaction should match", isRefundOrderTransaction(negativeEntry, expectedOrderTransaction), is(true));
        Assertion.assertThat("Negative refund point should match promo points", negativeEntry.get("point").getAsInt(), is(-expectedPromoPoints));

        logger.log("Verified refund point history for orderId " + orderId
                + ", negative=" + (-expectedPromoPoints));
    }

    private boolean isRefundOrderTransaction(JsonObject item, String expectedOrderTransaction) {
        String orderName = getJsonString(item, "orderName");
        String orderTransaction = getJsonString(item, "orderTransaction");
        return expectedOrderTransaction.equalsIgnoreCase(orderName)
                || expectedOrderTransaction.equalsIgnoreCase(orderTransaction)
                || "Product Transaction (Refund)".equalsIgnoreCase(orderName)
                || "Product Transaction (Refund)".equalsIgnoreCase(orderTransaction);
    }

    private String getJsonString(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull()) {
            return jsonObject.get(key).getAsString();
        }
        return "";
    }

    public void canBeBookForPointsBasedOnCondition(int totalPrice,int totalPoint,String condition){
        logger.log("total price : " + totalPrice);
        logger.log("total tiket points available : " + totalPoint);
        if(condition.contains("half")){
            Assertion.assertThat("is tiket point less then the total price",totalPoint,lessThanOrEqualTo(totalPrice));
        }else {
            Assertion.assertThat("is tiket point greater then or equal to the total price",totalPoint,greaterThanOrEqualTo(totalPrice));
        }
    }

    public long getAccountId(String username) {
        AccountData.Data accountData = resolveAccountData(username);
        return accountData.accountId;
    }

}
