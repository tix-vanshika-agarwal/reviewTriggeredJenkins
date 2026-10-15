package com.tiket.web.page.platform.eiffel.loyalty;

import com.tiket.api.auth.AdminDashboardLoginResponseParser;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardDynamicApi;
import com.tiket.api.refund.GetRefundPreviewsApi;
import com.tiket.api.refund.GetRefundTrackApi;
import com.tiket.api.refund.PostERefundApi;
import com.tiket.api.refund.PostRefundEstimationApi;
import com.tiket.api.refund.PostRefundForceSubmitApi;
import com.tiket.apiPage.OrderApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.order.HotelInternalOrderDetail;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;

import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Orchestrates the customer e-refund "dependency" a loyalty test needs to trigger point reversal.
 * The refund is not the thing under test (loyalty points are), so the whole multi-step flow is encapsulated
 * here and driven entirely by API, replacing the flaky UI verification (which failed only because the order
 * detail renders the localized room name).
 *
 * <p>Full flow (mirrors the storefront "my-refund" e-refund journey captured from the UI):
 * user estimation ({@code /external/refunds/{orderId}/{orderDetailId}/estimation}) &rarr; user create e-refund
 * ({@code /external/refunds/e-refund}) &rarr; poll {@code /external/refunds/{requestHash}/track} until the
 * refund is force-submit-ready ({@code on_process*}) &rarr; admin finance force-submit
 * ({@code /dashboard/refunds/finance/force-submit}) &rarr; poll {@code /track} until {@code orderRefundStatus}
 * is {@code refunded}. The estimation/create/track calls use the <b>user</b> access token; only the finance
 * force-submit uses the admin token. For ToDo, the refund preview ({@code /external/refunds/previews}) is read
 * first to confirm refundability and pick the product-specific reason id. This handler is intentionally scoped to
 * the loyalty negative-points refund tests (hotel and ToDo).
 */
public class LoyaltyDependencyPageWeb<T extends WebDriver> extends WebPage<T> {

    /** Force-submit needs the e-refund to reach an {@code on_process*} state; the /track status is polled for ~60s. */
    private static final int ON_PROCESS_POLL_ATTEMPTS = 12;
    private static final int ON_PROCESS_POLL_INTERVAL_SEC = 5;
    /** How long to poll {@code /track} for the terminal {@code refunded} state after force-submit (~2 minutes; the finance refund can take a while to settle on shared preprod). */
    private static final int REFUNDED_POLL_ATTEMPTS = 24;
    private static final int REFUNDED_POLL_INTERVAL_SEC = 5;
    /** A ToDo refund is only accepted for a visit date at least this many days after today. */
    private static final int TTD_REFUND_MIN_DAYS_TO_VISIT = 7;
    /** Refund reason used for a hotel refund when the test data sets none. */
    private static final String DEFAULT_HOTEL_REASON_ID = "CHANGE_TRIP_PLAN";
    /** Storefront currency for a refund order whose test data sets none. */
    private static final String DEFAULT_DISPLAY_CURRENCY = "IDR";
    /** E-ticket issuance can lag payment success, so poll the order status before verifying it (~60s). */
    private static final int ISSUED_POLL_ATTEMPTS = 20;
    private static final int ISSUED_POLL_INTERVAL_SEC = 3;
    /** Transient-failure retry policy for one-shot precondition API calls (estimation, create, admin login, force-submit). */
    private static final int API_RETRY_ATTEMPTS = 3;
    private static final int API_RETRY_BACKOFF_SEC = 3;
    /** The applied-promo banner renders its earned-points message asynchronously, so poll its textContent (~40s). */
    private static final int PROMO_POINTS_POLL_ATTEMPTS = 20;
    private static final int PROMO_POINTS_POLL_INTERVAL_SEC = 2;
    private static final int PROMO_BANNER_WAIT_SEC = 30;

    /**
     * UI "dependency" locators: storefront elements owned by the payment/order teams whose stock page objects
     * are flaky or language-brittle. Kept here (not on the loyalty page) so those files stay untouched.
     */
    private static final Locator SEE_PROMOS_VOUCHER = new Locator("SEE_PROMOS_VOUCHER", By.xpath("//*[@data-testid='btnApplyUnapplyPromo']"));
    private static final Locator INPUT_PROMO_FIELD = new Locator("INPUT_PROMO_FIELD", By.xpath("//*[@name='promo-input']"));
    private static final Locator APPLY_PROMO_BUTTON = new Locator("APPLY_PROMO_BUTTON", By.xpath("//button[contains(@class,'PromoCodeInputForm_apply_button')]"));
    private static final Locator APPLIED_PROMO_CONTAINER = new Locator("APPLIED_PROMO_CONTAINER", By.cssSelector("[data-testid='promo-container']"));
    private static final Locator APPLIED_PROMO_TEXT = new Locator("APPLIED_PROMO_TEXT", By.xpath("//*[@data-testid='promo-container']//div[contains(@class,'AppliedPromoText_text_content')]//p"));
    private static final Locator REMOVE_PROMO_BUTTON = new Locator("REMOVE_PROMO_BUTTON", By.cssSelector("[data-testid='btnRemovePromo']"));

    public LoyaltyDependencyPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> LoyaltyDependencyPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new LoyaltyDependencyPageDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new LoyaltyDependencyPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    /** The product a refund test books and refunds, from the {@code productToRefund} test data. */
    public enum ProductToRefund {
        HOTEL, TTD;

        /**
         * Parses the test-data value (case-insensitive). A blank value means {@link #HOTEL}, so rows written before
         * ToDo refunds existed keep running; any other unknown value skips the case as a test-data issue.
         */
        public static ProductToRefund from(String value) {
            if (value == null || value.isBlank()) {
                return HOTEL;
            }
            try {
                return valueOf(value.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                Assertion.skip("Test data productToRefund must be HOTEL or TTD but was '" + value
                        + "'; skipping as a test-data issue.");
                return null;
            }
        }
    }

    /**
     * The order a refund test books, pays and refunds, as read from its test data. Only the fields of
     * {@link #product} are required:
     * <ul>
     *     <li>{@link ProductToRefund#TTD}: a refundable ToDo package ({@code productUrl}, {@code productPackage});
     *     {@code minDaysToVisit} can push the visit date beyond D+{@value #TTD_REFUND_MIN_DAYS_TO_VISIT}.</li>
     *     <li>{@link ProductToRefund#HOTEL}: {@code hotelName}, {@code dateRange}, {@code adult}, {@code night},
     *     {@code room} and {@code currency}.</li>
     * </ul>
     */
    public record RefundOrder(
            ProductToRefund product,
            String productUrl,
            String productPackage,
            String minDaysToVisit,
            String hotelName,
            String dateRange,
            String adult,
            String night,
            String room,
            String currency
    ) {
        /** Currency the storefront shows while paying the order; {@value #DEFAULT_DISPLAY_CURRENCY} when none is set. */
        public String displayCurrency() {
            return currency == null || currency.isBlank() ? DEFAULT_DISPLAY_CURRENCY : currency;
        }
    }

    /**
     * Creates the waiting-payment order a refund test pays for and later refunds, through the API. A ToDo order
     * gets a visit date at least D+{@value #TTD_REFUND_MIN_DAYS_TO_VISIT}, or D+{@code minDaysToVisit} when that is
     * later.
     *
     * @return the created order's {@code orderId} and {@code orderHash}
     */
    public Map<String, String> bookOrderToRefund(String email, String password, RefundOrder order) {
        LoyaltyPointPageWeb<T> loyaltyPointPage = LoyaltyPointPageWeb.getInstance(session);
        if (order.product() == ProductToRefund.TTD) {
            String productUrl = requireTestData(order.productUrl(), "productUrl");
            String productPackage = requireTestData(order.productPackage(), "productPackage");
            int minDays = ttdMinDaysToVisit(order.minDaysToVisit());
            logger.log("Booking refundable ToDo package '" + productPackage + "' of '" + productUrl
                    + "' with a visit date at least D+" + minDays);
            return loyaltyPointPage.bookRefundableTTD(email, password, productUrl, productPackage,
                    CoreFunctions.randomName() + " " + CoreFunctions.randomName(), "Mr", minDays);
        }
        return loyaltyPointPage.bookHotelApi(email, password,
                CoreFunctions.generateRandomDate(order.dateRange()),
                requireTestData(order.hotelName(), "hotelName"),
                Integer.parseInt(requireTestData(order.adult(), "adult")),
                Integer.parseInt(requireTestData(order.night(), "night")),
                Integer.parseInt(requireTestData(order.room(), "room")),
                order.currency(), "id");
    }

    /**
     * Verifies the paid order is issued and refundable, resolves its {@code orderDetailId}, then completes the
     * e-refund end-to-end until it is {@code refunded}. A ToDo refund takes its reason from the refund preview
     * ({@code reasonId} is only preferred); a hotel refund sends {@code reasonId} as is, defaulting to
     * {@value #DEFAULT_HOTEL_REASON_ID}. A hotel order's name is checked against {@link RefundOrder#hotelName()}.
     */
    public void verifyOrderAndCompleteRefund(RefundOrder order, String orderId, String reasonId, String email,
                                             String password, String adminUsername, String adminPassword) {
        if (order.product() == ProductToRefund.TTD) {
            long orderDetailId = verifyTtdOrderIssuedAndResolveOrderDetailId(orderId);
            submitAndCompleteTtdRefund(orderId, orderDetailId, reasonId, email, password, adminUsername, adminPassword);
            return;
        }
        long orderDetailId = verifyHotelOrderAndResolveOrderDetailId(orderId, order.hotelName());
        String hotelReasonId = reasonId == null || reasonId.isBlank() ? DEFAULT_HOTEL_REASON_ID : reasonId;
        submitAndCompleteRefund(orderId, orderDetailId, hotelReasonId, email, password, adminUsername, adminPassword);
    }

    /** The later of D+{@value #TTD_REFUND_MIN_DAYS_TO_VISIT} and the test data's optional {@code minDaysToVisit}. */
    private static int ttdMinDaysToVisit(String minDaysToVisit) {
        if (minDaysToVisit == null || minDaysToVisit.isBlank()) {
            return TTD_REFUND_MIN_DAYS_TO_VISIT;
        }
        return Math.max(TTD_REFUND_MIN_DAYS_TO_VISIT, Integer.parseInt(minDaysToVisit.trim()));
    }

    /** Returns the non-blank test-data value; skips the case as a test-data issue when it is missing. */
    private static String requireTestData(String value, String key) {
        if (value == null || value.isBlank()) {
            Assertion.skip("Missing required test data '" + key + "' for the refund order; skipping as a test-data issue.");
        }
        return value.trim();
    }

    /**
     * Submits a refund via the customer <b>e-refund</b> flow and drives it end-to-end via API, asserting
     * the refund reaches the {@code refunded} state. This mirrors what a user does on the storefront "my-refund"
     * page: get an estimation, create the e-refund, then (as the finance admin) force-submit it and track it to
     * completion.
     *
     * @param orderId       order id (as displayed, e.g. "1234567")
     * @param orderDetailId order-detail id resolved via the order API
     * @param reasonId      refund reason id (e.g. {@code "CHANGE_TRIP_PLAN"})
     * @param email         the booking user's email — used to obtain the user access token for the e-refund calls
     * @param password      the booking user's password
     * @param adminUsername admin account used to log in and finance force-submit
     * @param adminPassword admin password for {@code v3/admin/login}
     */
    @SneakyThrows
    public void submitAndCompleteRefund(
            String orderId,
            long orderDetailId,
            String reasonId,
            String email,
            String password,
            String adminUsername,
            String adminPassword
    ) {
        String step = "starting e-refund";
        try {
            long orderIdLong = Long.parseLong(orderId.replaceAll("[^0-9]", ""));

            step = "getting the user access token for " + email;
            Unm<T> unm = new Unm<>(session);
            String userToken = unm.getAccessToken(email, password, "B2C");
            Assertion.assertThat("User access token should not be empty", userToken, not(blankOrNullString()));

            step = "getting the e-refund estimation";
            JSONObject estimationData = getERefundEstimation(userToken, orderIdLong, orderDetailId, reasonId);
            JSONArray amountDetails = estimationData.getJSONArray("amountDetails");
            String paymentMethodKey = resolveRefundMethodKey(estimationData);
            logger.log("E-refund estimation resolved: paymentMethodKey=" + paymentMethodKey
                    + ", amountDetails=" + amountDetails.length() + " item(s)");

            step = "creating the e-refund request";
            JSONObject eRefundData = createERefund(userToken, orderIdLong, orderDetailId, reasonId, paymentMethodKey, amountDetails);
            long refundRequestId = eRefundData.getLong("id");
            String requestHash = eRefundData.getString("requestHash");
            int paymentMethodId = eRefundData.getJSONArray("paymentMethods").getJSONObject(0).getInt("id");
            logger.log("E-refund created: refundRequestId=" + refundRequestId + ", requestHash=" + requestHash
                    + ", paymentMethodId=" + paymentMethodId);

            step = "logging in as admin (v3/admin/login)";
            String adminToken = "Bearer " + getAdminAccessTokenDashboardAuth(adminUsername, adminPassword);

            step = "waiting for e-refund " + refundRequestId + " to become force-submit-ready";
            String lastStatus = pollTrackForForceSubmitReady(userToken, requestHash);

            if (lastStatus != null && lastStatus.contains("on_process")) {
                step = "force-submitting e-refund " + refundRequestId;
                logger.log("Force-submitting e-refund " + refundRequestId + " (status=" + lastStatus
                        + ", paymentMethodId=" + paymentMethodId + ")");
                final String forceSubmitStatus = lastStatus;
                ApiResponse forceSubmitResponse = hitWithRetry("Force-submit e-refund " + refundRequestId, () ->
                        new PostRefundForceSubmitApi(new PostRefundForceSubmitApi.Inputs(
                                adminToken, forceSubmitStatus, orderId, String.valueOf(refundRequestId), paymentMethodId))
                                .hitApi(apiTools));
                logger.log("Force-submit response: " + forceSubmitResponse.getBody());
            } else {
                logger.log("E-refund " + refundRequestId + " already past the on_process stage (status=" + lastStatus
                        + "); skipping force-submit.");
            }

            step = "verifying orderRefundStatus becomes 'refunded' for " + refundRequestId;
            verifyTrackReachesRefunded(userToken, requestHash);
        } catch (SkipException skip) {
            throw skip;
        } catch (AssertionError | Exception t) {
            logger.softWarning("Refund dependency failed while " + step + ". Full trace (kept so a genuine refund "
                    + "regression is not hidden by the skip below):\n" + ExceptionUtils.getStackTrace(t));
            Assertion.skip("Refund dependency failed while " + step + ": " + t.getClass().getSimpleName()
                    + (t.getMessage() != null ? " - " + t.getMessage() : "")
                    + ". Loyalty points cannot be verified without a completed refund, so skipping this case.");
        }
    }

    /**
     * ToDo variant of {@link #submitAndCompleteRefund}: reads the refund preview first, skips when the order detail
     * is not refundable, and uses {@code preferredReasonId} only when the preview offers it (otherwise the first
     * offered reason), because ToDo reason ids are product-specific (e.g. {@code refund-reason-supplier-...-S2-0}).
     * A failing preview call, token or order id fails the case instead of skipping it, so a real regression stays
     * visible.
     */
    public void submitAndCompleteTtdRefund(
            String orderId,
            long orderDetailId,
            String preferredReasonId,
            String email,
            String password,
            String adminUsername,
            String adminPassword
    ) {
        long orderIdLong = Long.parseLong(orderId.replaceAll("[^0-9]", ""));
        String userToken = new Unm<>(session).getAccessToken(email, password, "B2C");
        String reasonId = resolveRefundReasonId(userToken, orderIdLong, orderDetailId, preferredReasonId);
        submitAndCompleteRefund(orderId, orderDetailId, reasonId, email, password, adminUsername, adminPassword);
    }

    /**
     * Reads the refund preview: fails when the call is unsuccessful, skips when the order detail is not refundable
     * ({@code data.refundable.refundable.value}) or offers no reasons, and returns {@code preferredReasonId} if {@code data.reasons}
     * offers it, otherwise the first offered reason id.
     */
    private String resolveRefundReasonId(String userToken, long orderId, long orderDetailId, String preferredReasonId) {
        ApiResponse response = hitWithRetry("Get refund preview for order " + orderId, () ->
                new GetRefundPreviewsApi(new GetRefundPreviewsApi.Inputs(userToken, orderId, orderDetailId))
                        .hitApi(apiTools));
        JSONObject json = new JSONObject(response.getBody());
        JSONObject data = json.optJSONObject("data");
        if (!isSuccessBody(json) || data == null) {
            Assertion.fail("Refund preview failed for order " + orderId + ": " + response.getStatusCode() + " - "
                    + response.getBody());
        }
        JSONObject refundable = data.optJSONObject("refundable");
        JSONObject refundableFlag = refundable == null ? null : refundable.optJSONObject("refundable");
        if (refundableFlag == null || !refundableFlag.optBoolean("value", false)) {
            Assertion.skip("Order " + orderId + " / detail " + orderDetailId + " is not refundable per the refund "
                    + "preview: " + refundable);
        }
        JSONArray reasons = data.optJSONArray("reasons");
        if (reasons == null || reasons.isEmpty()) {
            Assertion.skip("Refund preview for order " + orderId + " offered no refund reasons. Body: " + response.getBody());
        }
        for (int i = 0; i < reasons.length(); i++) {
            String id = reasons.getJSONObject(i).optString("id");
            if (preferredReasonId != null && id.equals(preferredReasonId.trim())) {
                return id;
            }
        }
        JSONObject firstReason = reasons.getJSONObject(0);
        logger.log("Using refund reason '" + firstReason.optString("reason") + "' (" + firstReason.optString("id") + ")");
        return firstReason.getString("id");
    }

    /**
     * First e-refund step: fetches the refund estimation for the order/detail so the subsequent create call can
     * echo back the exact refundable {@code amountDetails} and the eligible refund method the UI would submit.
     * Skips (as a refund dependency issue) if the estimation is unsuccessful or exposes no amounts.
     *
     * @return the {@code data} object of a successful estimation response (guaranteed to contain a non-empty
     *         {@code amountDetails} array)
     */
    private JSONObject getERefundEstimation(String userToken, long orderId, long orderDetailId, String reasonId) {
        ApiResponse response = hitWithRetry("Get e-refund estimation for order " + orderId, () ->
                new PostRefundEstimationApi(new PostRefundEstimationApi.Inputs(
                        userToken, orderId, orderDetailId, reasonId, String.valueOf(System.currentTimeMillis())))
                        .hitApi(apiTools));
        JSONObject json = new JSONObject(response.getBody());
        if (!isSuccessBody(json) || json.isNull("data")) {
            Assertion.skip("E-refund estimation failed for order " + orderId + " (reason '" + reasonId + "'): "
                    + response.getStatusCode() + " - " + response.getBody());
        }
        JSONObject data = json.getJSONObject("data");
        if (!data.has("amountDetails") || data.getJSONArray("amountDetails").isEmpty()) {
            Assertion.skip("E-refund estimation returned no amountDetails for order " + orderId
                    + "; cannot build the e-refund request. Body: " + response.getBody());
        }
        return data;
    }

    /**
     * Resolves the refund method key (e.g. {@code "blipay"}) the e-refund request must submit, from the
     * estimation response's {@code refundMethod} / {@code groupedRefundMethods} list. Skips if none is exposed.
     */
    private String resolveRefundMethodKey(JSONObject estimationData) {
        for (String key : new String[]{"refundMethod", "groupedRefundMethods"}) {
            if (estimationData.has(key) && !estimationData.isNull(key)) {
                JSONArray methods = estimationData.getJSONArray(key);
                if (!methods.isEmpty()) {
                    String methodKey = methods.getJSONObject(0).optString("methodKey", "");
                    if (!methodKey.isBlank()) {
                        return methodKey;
                    }
                }
            }
        }
        Assertion.skip("E-refund estimation did not expose a refund method key; cannot choose a payment method.");
        return null;
    }

    /**
     * Second e-refund step: creates the e-refund request (HTTP 201) with the estimation's amounts and method.
     * Skips (as a refund dependency issue) on any non-success response or if the response is missing the
     * {@code requestHash}/payment method the later track + force-submit steps rely on.
     *
     * @return the {@code data} object of a successful create response (contains {@code id}, {@code requestHash}
     *         and a non-empty {@code paymentMethods} array)
     */
    private JSONObject createERefund(String userToken, long orderId, long orderDetailId, String reasonId,
                                     String paymentMethodKey, JSONArray amountDetails) {
        ApiResponse response = hitWithRetry("Create e-refund for order " + orderId, () ->
                new PostERefundApi(new PostERefundApi.Inputs(
                        userToken, orderId, orderDetailId, reasonId, paymentMethodKey, amountDetails, ""))
                        .hitApi(apiTools));
        int status = response.getStatusCode();
        JSONObject json = new JSONObject(response.getBody());
        if ((status != 200 && status != 201) || !isSuccessBody(json) || json.isNull("data")) {
            Assertion.skip("Create e-refund failed for order " + orderId + ": " + status + " - " + response.getBody());
        }
        JSONObject data = json.getJSONObject("data");
        boolean hasHash = data.has("requestHash") && !data.isNull("requestHash") && !data.getString("requestHash").isBlank();
        boolean hasPaymentMethod = data.has("paymentMethods") && !data.isNull("paymentMethods")
                && !data.getJSONArray("paymentMethods").isEmpty();
        if (!hasHash || !hasPaymentMethod) {
            Assertion.skip("Create e-refund response missing requestHash/paymentMethods for order " + orderId
                    + ". Body: " + response.getBody());
        }
        return data;
    }

    /**
     * Polls {@code /track} until the e-refund is force-submit-ready (its {@code lastStatus} reaches an
     * {@code on_process*} state). Returns the last status seen so the caller can decide whether to force-submit;
     * if the refund unexpectedly settles to {@code refunded} first, that status is returned and force-submit is
     * skipped. Skips (as a refund dependency issue) if it never becomes force-submit-ready.
     */
    @SneakyThrows
    private String pollTrackForForceSubmitReady(String userToken, String requestHash) {
        String lastSeen = null;
        for (int attempt = 1; attempt <= ON_PROCESS_POLL_ATTEMPTS; attempt++) {
            JSONObject data = getTrackDataOrNull(userToken, requestHash);
            if (data != null) {
                lastSeen = data.optString("lastStatus", null);
                String orderRefundStatus = data.optString("orderRefundStatus", "");
                if (lastSeen != null && lastSeen.contains("on_process")) {
                    return lastSeen;
                }
                if ("refunded".equalsIgnoreCase(orderRefundStatus)) {
                    return lastSeen;
                }
                logger.log("E-refund " + requestHash + " not force-submit-ready yet (attempt " + attempt + "/"
                        + ON_PROCESS_POLL_ATTEMPTS + ") -> lastStatus=" + lastSeen
                        + ", orderRefundStatus=" + orderRefundStatus);
            }
            if (attempt < ON_PROCESS_POLL_ATTEMPTS) {
                TimeUnit.SECONDS.sleep(ON_PROCESS_POLL_INTERVAL_SEC);
            }
        }
        Assertion.skip("E-refund " + requestHash + " never became force-submit-ready after " + ON_PROCESS_POLL_ATTEMPTS
                + " attempts (needs an 'on_process' status); last status was '" + lastSeen
                + "'. Not force-submitting to avoid leaving the refund stuck; skipping as a refund dependency issue.");
        return null;
    }

    /**
     * Waits (with retries) for the e-refund's order-level state to become {@code refunded} via {@code /track}.
     * This is the API replacement for the previous UI order-detail room-name check. If it never reaches
     * {@code refunded} the refund did not complete, which is a dependency problem, so the case is skipped.
     */
    @SneakyThrows
    private void verifyTrackReachesRefunded(String userToken, String requestHash) {
        String lastSeenStatus = null;
        for (int attempt = 1; attempt <= REFUNDED_POLL_ATTEMPTS; attempt++) {
            JSONObject data = getTrackDataOrNull(userToken, requestHash);
            if (data != null) {
                lastSeenStatus = data.optString("orderRefundStatus", null);
                logger.log("orderRefundStatus for e-refund " + requestHash + " (attempt " + attempt + "/"
                        + REFUNDED_POLL_ATTEMPTS + "): " + lastSeenStatus);
                if (lastSeenStatus != null && lastSeenStatus.equalsIgnoreCase("refunded")) {
                    logger.log("E-refund " + requestHash + " reached orderRefundStatus 'refunded'.");
                    return;
                }
            }
            if (attempt < REFUNDED_POLL_ATTEMPTS) {
                TimeUnit.SECONDS.sleep(REFUNDED_POLL_INTERVAL_SEC);
            }
        }
        Assertion.skip("E-refund " + requestHash + " did not reach orderRefundStatus 'refunded' after "
                + REFUNDED_POLL_ATTEMPTS + " attempts (last seen: '" + lastSeenStatus + "'). The refund did not "
                + "complete, so the loyalty point reversal cannot be verified; skipping as a refund dependency issue.");
    }

    /**
     * Fetches the e-refund {@code /track} payload and returns its {@code data} object, or {@code null} (with a
     * soft warning) if the response is not a successful, data-bearing body. A just-created {@code requestHash}
     * can momentarily 404 (eventual consistency) and shared preprod can return a transient 5xx; {@link ApiTools}
     * throws on any non-2xx, so those are caught here and reported as {@code null} to let the caller's poll loop
     * retry the next attempt rather than aborting the whole refund flow.
     */
    private JSONObject getTrackDataOrNull(String userToken, String requestHash) {
        try {
            ApiResponse response = new GetRefundTrackApi(new GetRefundTrackApi.Inputs(userToken, requestHash)).hitApi(apiTools);
            JSONObject json = new JSONObject(response.getBody());
            if (!isSuccessBody(json) || json.isNull("data")) {
                logger.softWarning("Refund track response had no data for " + requestHash + ": " + response.getBody());
                return null;
            }
            return json.getJSONObject("data");
        } catch (Exception e) {
            logger.softWarning("Transient error polling /track for " + requestHash + "; will retry next attempt: "
                    + e.getClass().getSimpleName() + " - " + e.getMessage());
            return null;
        }
    }

    /** @return {@code true} if the API envelope's {@code code} indicates success. */
    private static boolean isSuccessBody(JSONObject json) {
        return json.optString("code", "").toUpperCase().contains("SUCCESS");
    }

    private String getAdminAccessTokenDashboardAuth(String username, String password) {
        POSTGetAdminAccessTokenDashboardDynamicApi.Inputs inputs =
                new POSTGetAdminAccessTokenDashboardDynamicApi.Inputs(username, password);
        ApiResponse response = hitWithRetry("Admin login for " + username, () ->
                new POSTGetAdminAccessTokenDashboardDynamicApi(inputs).hitApi(apiTools));
        String accessToken = AdminDashboardLoginResponseParser.resolveSessionTokenOrNull(response);
        if (accessToken == null || accessToken.isBlank()) {
            Assertion.skip("Admin login via v3/admin/login failed for " + username + "; cannot complete refund.");
        }
        return accessToken;
    }

    /**
     * Runs a one-shot precondition API call with a short retry loop so a transient 5xx / network blip does not
     * skip the whole case. Rethrows the last error after {@link #API_RETRY_ATTEMPTS} attempts so the caller's
     * skip carries the real cause.
     */
    @SneakyThrows
    private ApiResponse hitWithRetry(String action, ApiCall call) {
        Exception lastError = null;
        for (int attempt = 1; attempt <= API_RETRY_ATTEMPTS; attempt++) {
            try {
                return call.execute();
            } catch (Exception e) {
                lastError = e;
                if (attempt >= API_RETRY_ATTEMPTS) {
                    break;
                }
                logger.log(action + " failed (attempt " + attempt + "/" + API_RETRY_ATTEMPTS + "): "
                        + e.getMessage() + ". Retrying in " + API_RETRY_BACKOFF_SEC + "s...");
                TimeUnit.SECONDS.sleep(API_RETRY_BACKOFF_SEC);
            }
        }
        throw lastError;
    }

    @FunctionalInterface
    private interface ApiCall {
        ApiResponse execute() throws Exception;
    }

    /**
     * API replacement for the flaky order-detail UI verification: fetches the internal order detail
     * ({@code /tix-order-go/internal/order/{orderId}/details}) <b>once</b> and asserts the hotel order is issued
     * ({@code orderStatus = ETICKET_ISSUED}), the hotel name matches what was booked, and the order is refundable
     * (which is what the UI "refund/reschedule info" and "refund button" checks really assert). Returns the hotel
     * {@code orderDetailId} resolved from the same response, so the caller does not need a second call to the same
     * endpoint. Uses {@link OrderApi} so the loyalty test no longer depends on the order team's page objects.
     *
     * <p>Polls briefly for {@code ETICKET_ISSUED} because e-ticket issuance can lag payment success; the poll
     * reuses the full order-detail payload so a settled order is verified and resolved from a single fetch.
     *
     * @param orderId           the order id
     * @param expectedHotelName the hotel name that was booked (from test data)
     * @return the hotel {@code orderDetailId} read from the same fetched order detail
     */
    @SneakyThrows
    public long verifyHotelOrderAndResolveOrderDetailId(String orderId, String expectedHotelName) {
        OrderApi<T> orderApi = new OrderApi<>(session);
        HotelInternalOrderDetail order = waitForOrderIssued(orderId,
                () -> orderApi.getHotelInternalOrderData(orderId),
                fetched -> fetched.data != null ? fetched.data.orderStatus : null);
        Assertion.assertThat("Hotel name in order detail should match booked hotel",
                orderApi.getHotelOrderNameFromInternalOrder(order, orderId), is(expectedHotelName));
        Assertion.assertThat("Hotel order should be refundable (refund/reschedule available)",
                isHotelOrderRefundable(order, orderId), is(true));
        return orderApi.getHotelOrderDetailId(order, orderId);
    }

    /**
     * Reads whether the hotel order detail is refundable from the internal order detail
     * ({@code data.orderDetails[].vertical.cancellationPolicyV3.refundable}).
     */
    private boolean isHotelOrderRefundable(HotelInternalOrderDetail response, String orderId) {
        if (response == null || response.data == null || response.data.orderDetails == null) {
            Assertion.skip("Internal order detail for orderID " + orderId + " has no orderDetails payload; "
                    + "skipping as a refund dependency issue rather than failing the loyalty case.");
        }
        for (HotelInternalOrderDetail.OrderDetails orderDetail : response.data.orderDetails) {
            if (orderDetail == null || orderDetail.type == null) {
                continue;
            }
            if ((orderDetail.type.equalsIgnoreCase("tixhotel") || orderDetail.type.equalsIgnoreCase("hotel_homes"))
                    && orderDetail.vertical != null && orderDetail.vertical.cancellationPolicyV3 != null) {
                return orderDetail.vertical.cancellationPolicyV3.refundable;
            }
        }
        Assertion.skip("Cannot determine hotel refundability (no cancellationPolicyV3) for orderID " + orderId
                + "; skipping as a refund dependency issue rather than failing the loyalty case.");
        return false;
    }

    /**
     * Waits for the ToDo order to be issued ({@code orderStatus = ETICKET_ISSUED}; issuance can lag payment success)
     * and returns its {@code event} {@code orderDetailId}. Refundability is checked later by the refund preview.
     *
     * @param orderId the order id
     * @return the ToDo order detail id
     */
    public long verifyTtdOrderIssuedAndResolveOrderDetailId(String orderId) {
        OrderApi<T> orderApi = new OrderApi<>(session);
        waitForOrderIssued(orderId, () -> orderApi.getOrderStatus(orderId), Function.identity());
        return orderApi.getOrderDetailIdByType(orderId, "event");
    }

    /**
     * Polls the order until its status is {@code ETICKET_ISSUED} (~{@value #ISSUED_POLL_ATTEMPTS} x
     * {@value #ISSUED_POLL_INTERVAL_SEC}s; issuance can lag payment success), treating a failed fetch as transient,
     * then asserts the order was issued.
     *
     * @param fetchOrder reads the order payload once
     * @param statusOf   extracts {@code orderStatus} from that payload
     * @return the last fetched payload, which carries the issued status
     */
    @SneakyThrows
    private <O> O waitForOrderIssued(String orderId, Callable<O> fetchOrder, Function<O, String> statusOf) {
        O order = null;
        String status = null;
        for (int attempt = 1; attempt <= ISSUED_POLL_ATTEMPTS; attempt++) {
            try {
                order = fetchOrder.call();
                status = statusOf.apply(order);
            } catch (Exception e) {
                status = null;
                logger.softWarning("Transient error fetching internal order detail for " + orderId
                        + " (attempt " + attempt + "/" + ISSUED_POLL_ATTEMPTS + "); will retry: "
                        + e.getClass().getSimpleName() + " - " + e.getMessage());
            }
            if ("ETICKET_ISSUED".equalsIgnoreCase(status)) {
                break;
            }
            logger.log("Order " + orderId + " not ETICKET_ISSUED yet (attempt " + attempt + "/"
                    + ISSUED_POLL_ATTEMPTS + ") -> " + status);
            if (attempt < ISSUED_POLL_ATTEMPTS) {
                TimeUnit.SECONDS.sleep(ISSUED_POLL_INTERVAL_SEC);
            }
        }
        Assertion.assertThat("Order " + orderId + " status should be ETICKET_ISSUED",
                status, is(equalToIgnoringCase("ETICKET_ISSUED")));
        return order;
    }

    public void clickOnSeePromosVoucher() {
        driverTools.waitForElementToAppear(SEE_PROMOS_VOUCHER).orElseThrow(() -> new NoSuchElementException("See promos voucher button not found"));
        driverTools.click(SEE_PROMOS_VOUCHER);
    }

    public void inputPromoCodeAndApply(String promoCode) {
        driverTools.waitForElementToAppear(INPUT_PROMO_FIELD).orElseThrow(() -> new NoSuchElementException("Input promo field not found"));
        driverTools.sendKeys(INPUT_PROMO_FIELD, promoCode);
        driverTools.click(APPLY_PROMO_BUTTON);
        driverTools.waitForElementToDisappear(APPLY_PROMO_BUTTON);
    }

    /**
     * The redesigned payment-page promo banner (data-testid="promo-container") only renders the earned-points
     * message (e.g. "Hore! Kamu bakal dapet 186.599 poin setelah pembayaran.") plus a remove-promo button; the
     * old PaymentPageWeb PromoDisplay_* / unapply-promo locators no longer exist and the promo code is no longer
     * displayed. These reads live here so the payment page (owned by another team) stays untouched.
     */
    public boolean isPromoBannerVisible() {
        driverTools.waitForElementToAppear(APPLIED_PROMO_CONTAINER, PROMO_BANNER_WAIT_SEC);
        boolean applied = driverTools.waitForElementToPresence(REMOVE_PROMO_BUTTON, PROMO_BANNER_WAIT_SEC).isPresent();
        String pointsText = readAppliedPromoPointsText();
        boolean hasPoints = java.util.regex.Pattern.compile("\\d").matcher(pointsText).find();
        logger.log("Promo banner visible check -> applied: " + applied + ", pointsText: '" + pointsText + "'");
        return applied && hasPoints;
    }

    /**
     * Polls the applied-promo banner's earned-points paragraph via textContent. The message renders
     * asynchronously a moment after the promo card (the container and remove button appear first), and in
     * headless Chrome the paragraph is intermittently reported as "not displayed" even when populated, so this
     * reads textContent rather than relying on Selenium visibility.
     */
    private String readAppliedPromoPointsText() {
        for (int attempt = 0; attempt < PROMO_POINTS_POLL_ATTEMPTS; attempt++) {
            String text = driverTools.getAttributeTextContent(APPLIED_PROMO_TEXT).orElse("");
            if (java.util.regex.Pattern.compile("\\d").matcher(text).find()) {
                return text;
            }
            try {
                TimeUnit.SECONDS.sleep(PROMO_POINTS_POLL_INTERVAL_SEC);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return driverTools.getAttributeTextContent(APPLIED_PROMO_TEXT).orElse("");
    }

    public int getAppliedPromoPoints() {
        String bannerText = readAppliedPromoPointsText();
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("[\\d.,]+").matcher(bannerText);
        if (!matcher.find()) {
            throw new NoSuchElementException("No promo points number found in applied promo banner text: " + bannerText);
        }
        int points = (int) CoreFunctions.convertPriceSafely(matcher.group());
        logger.log("Applied promo points from banner: " + points + " (text: " + bannerText + ")");
        return points;
    }

    private static Locator paymentMethodItem(String paymentMethod) {
        return new Locator("PAYMENT_METHOD_ITEM", By.xpath(String.format(
                "//li[.//span[contains(@class,'Text_text') and normalize-space()='%s']]", paymentMethod)));
    }

    /**
     * Selects a payment method from the redesigned payment drawer. The payment-team
     * PaymentPageDWeb.goToPaymentMethodsGroup() matched the group title by English text only ("Instant Payment"),
     * but the storefront renders the localized title ("Pembayaran Instan"), so the scroll-to-group step failed.
     * The method tile (e.g. "Sakuku") is a language-agnostic brand label, so this wrapper scrolls straight to the
     * method tile and JS-clicks it. Kept here so the payment-team file stays untouched.
     */
    public void selectPaymentMethodInDrawer(String paymentGroup, String paymentMethod) {
        logger.log("Selecting payment method '" + paymentMethod + "' (group hint: '" + paymentGroup + "')");
        org.openqa.selenium.WebElement methodTile = driverTools.waitForElementToPresence(paymentMethodItem(paymentMethod))
                .orElseThrow(() -> new NoSuchElementException("Payment method tile not found in drawer: " + paymentMethod));
        driverTools.scrollElementToCenter(methodTile);
        driverTools.clickWithJavascriptExecutor(methodTile);
    }
}
