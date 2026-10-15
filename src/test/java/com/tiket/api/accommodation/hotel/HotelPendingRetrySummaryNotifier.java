package com.tiket.api.accommodation.hotel;

import com.tiket.io.api.ApiTools;
import com.tiket.logging.ILogger;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.Configs;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

// PREPROD: cron after web payment success, Your Orders, or HotelApi paid-checker SUCCESS (full API book+pay).
public final class HotelPendingRetrySummaryNotifier {

    private static final ConcurrentHashMap<String, Long> LAST_HIT_MS = new ConcurrentHashMap<>();
    private static final long DEDUP_MS = 15_000L;

    private HotelPendingRetrySummaryNotifier() {}

    public static void afterWebPaymentSuccess(Session<?> session, ApiTools apiTools, ILogger logger) {
        if (!isCronEnabled()) {
            return;
        }
        tryHitAfterDedupe(session.sessionId(), apiTools, logger);
    }

    /** Same cron/dedupe as {@link #afterWebPaymentSuccess}; use when payment is confirmed via API (e.g. paid checker), not the browser. */
    public static void afterApiPaymentSuccess(Session<?> session, ApiTools apiTools, ILogger logger) {
        afterWebPaymentSuccess(session, apiTools, logger);
    }

    public static void afterNavigateToYourOrders(Session<?> session, ApiTools apiTools, ILogger logger) {
        if (!isCronEnabled()) {
            return;
        }
        WebDriver driver = session.driver();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(25))
                    .until(d -> {
                        String u = d.getCurrentUrl();
                        return u != null && (u.contains("your-order") || u.contains("yourorder"));
                    });
        } catch (TimeoutException e) {
            logger.log("Skipped hotel pending-retry-summary cron: your-order URL not reached in time");
            return;
        }
        tryHitAfterDedupe(session.sessionId(), apiTools, logger);
    }

    private static boolean isCronEnabled() {
        if (Configs.ENVIRONMENT != Environment.PREPROD) {
            return false;
        }
        String url = Configs.CONFIG.getProperty("hotel.pending.retry.summary.url");
        return url != null && !url.isBlank();
    }

    private static void tryHitAfterDedupe(String dedupeKey, ApiTools apiTools, ILogger logger) {
        long now = System.currentTimeMillis();
        Long prev = LAST_HIT_MS.get(dedupeKey);
        if (prev != null && now - prev < DEDUP_MS) {
            return;
        }
        LAST_HIT_MS.put(dedupeKey, now);

        String url = Configs.CONFIG.getProperty("hotel.pending.retry.summary.url");
        HotelPendingRetrySummaryCronApi api = new HotelPendingRetrySummaryCronApi(
                new HotelPendingRetrySummaryCronApi.Inputs(url));
        try {
            ApiResponse response = api.hitApi(apiTools);
            logger.log("Hotel pending-retry-summary cron HTTP " + response.getStatusCode());
        } catch (Exception e) {
            logger.log("Hotel pending-retry-summary cron failed (ignored): " + e.getMessage());
        }
    }
}
