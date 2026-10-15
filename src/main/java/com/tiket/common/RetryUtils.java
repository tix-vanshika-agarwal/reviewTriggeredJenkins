package com.tiket.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Shared retry utilities (e.g. exponential backoff) for operations that may transiently fail.
 */
public final class RetryUtils {

    private static final Logger logger = LogManager.getLogger(RetryUtils.class);

    private RetryUtils() {
    }

    /**
     * Retries an action with exponential backoff when the failure matches the given predicate.
     * Delay between attempts: baseDelayMs, 2*baseDelayMs, 4*baseDelayMs, ...
     *
     * @param maxRetries    maximum number of attempts (including the first)
     * @param baseDelayMs   delay before first retry; subsequent delays double each time
     * @param action        the operation to run (e.g. driver creation)
     * @param isRetryable   only retries when this predicate returns true for the thrown exception
     * @param operationName name used in log messages and thrown exceptions
     * @param <T>           return type of the action
     * @return the result of the action on success
     * @throws RuntimeException if the action never succeeds, or if a non-retryable exception is thrown
     */
    public static <T> T retryWithExponentialBackoff(
            int maxRetries,
            long baseDelayMs,
            Supplier<T> action,
            Predicate<RuntimeException> isRetryable,
            String operationName) {
        if (maxRetries < 1) {
            throw new IllegalArgumentException("maxRetries must be >= 1, got: " + maxRetries);
        }
        RuntimeException lastException = null;
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                return action.get();
            } catch (RuntimeException e) {
                lastException = e;
                if (!isRetryable.test(e)) {
                    throw e;
                }
                if (attempt < maxRetries - 1) {
                    long delayMs = baseDelayMs * (1L << attempt);
                    logger.warn("{} failed on attempt {} of {}. Retrying in {}ms...",
                            operationName, attempt + 1, maxRetries, delayMs);
                    sleepOrThrow(delayMs, operationName);
                } else {
                    logger.error("{} failed after {} attempts", operationName, maxRetries);
                    throw new RuntimeException(operationName + " failed after " + maxRetries + " retries", e);
                }
            }
        }
        throw new RuntimeException(operationName + " failed", lastException);
    }

    public static <T> T retryUntil(
            int maxAttempts,
            long delayMs,
            Supplier<T> action,
            Predicate<T> isSuccessful,
            String operationName) {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be >= 1, got: " + maxAttempts);
        }
        T result = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            result = action.get();
            if (isSuccessful.test(result)) {
                if (attempt > 1) {
                    logger.info("{} succeeded on attempt {} of {}", operationName, attempt, maxAttempts);
                }
                return result;
            }
            if (attempt < maxAttempts) {
                logger.warn("{} not ready on attempt {} of {}. Retrying in {}ms...",
                        operationName, attempt, maxAttempts, delayMs);
                sleepOrThrow(delayMs, operationName);
            } else {
                logger.error("{} did not succeed after {} attempts", operationName, maxAttempts);
            }
        }
        return result;
    }

    /**
     * Predicate that returns true when the exception message indicates a ChromeDriver "free port" failure.
     * Use with {@link #retryWithExponentialBackoff} for driver creation.
     */
    public static boolean isChromeDriverPortConflict(RuntimeException e) {
        return e.getMessage() != null && e.getMessage().contains("Unable to find a free port");
    }

    /**
     * Returns true if the throwable (or its cause chain) indicates ChromeDriver "free port" exhaustion
     * after retries. Callers can use this to take an early exit and fail the build fast instead of
     * letting the process run until a crash (e.g. exit code 143).
     */
    public static boolean isFreePortExhaustedFailure(Throwable t) {
        while (t != null) {
            String msg = t.getMessage();
            if (msg != null
                    && (msg.contains("Unable to find a free port")
                    || msg.contains("ChromeDriver creation (free port) failed after"))) {
                return true;
            }
            t = t.getCause();
        }
        return false;
    }

    private static void sleepOrThrow(long delayMs, String operationName) {
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while waiting to retry " + operationName, ie);
        }
    }
}
