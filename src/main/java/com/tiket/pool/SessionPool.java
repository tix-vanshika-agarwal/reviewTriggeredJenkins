package com.tiket.pool;

import com.tiket.common.RetryUtils;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Stream;

public record SessionPool(
        BlockingQueue<Session<WebDriver>> queue,
        Supplier<Session<WebDriver>> sessionSupplier,
        int capacity
) implements Pool<Session<WebDriver>> {

    private static final Logger logger = LogManager.getLogger(SessionPool.class);

    public SessionPool {
        Objects.requireNonNull(queue);
        Objects.requireNonNull(sessionSupplier);
    }

    public SessionPool(int capacity, Supplier<Session<WebDriver>> supplier) {
        this(new ArrayBlockingQueue<>(capacity), supplier, capacity);
        logger.info("SessionPool created with capacity: " + capacity);
    }

    @Override
    public Session<WebDriver> take() {
        Session<WebDriver> session = takeSecurely(queue);
        if (session == null && isWebPlatform()) {
            logger.warn("Session is null, renewing session for web platform");
            session = sessionSupplier.get();
        }
        return sanitizedSession(session);
    }

    @Override
    public void put(Session<WebDriver> session) {
        if (Configs.PLATFORM == Platform.IOS || Configs.PLATFORM == Platform.ANDROID) {
            putSecurely(queue, sanitizedSession(session));
        } else {
            putSecurely(queue, sanitizedSessionWithTimeout(session));
        }
    }

    @Override
    public void fill() {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Session pool capacity must be positive, got: " + capacity);
        }
        logger.info("Started filling Session Pool");
        ExecutorService threadPool = Executors.newFixedThreadPool(capacity);
        try {
            List<CompletableFuture<Void>> futures = Stream.iterate(sessionSupplier, s -> s)
                    .limit(capacity)
                    .peek(s -> {
                        if(Configs.PLATFORM == Platform.DWEB) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    })
                    .map(supplier -> CompletableFuture.<Session<WebDriver>>supplyAsync(supplier, threadPool)
                            .thenAccept(session -> {
                                if (session == null) {
                                    throw new CompletionException(new IllegalStateException("sessionSupplier returned null"));
                                }
                                putSecurely(queue, session);
                            })
                            .exceptionally(e -> {
                                Throwable cause = e instanceof CompletionException ? e.getCause() : e;
                                if (RetryUtils.isFreePortExhaustedFailure(cause != null ? cause : e)) {
                                    logger.error("ChromeDriver creation failed: no free port after retries. Failing fast.");
                                } else {
                                    logger.error("Issue in driver: " + e);
                                    e.printStackTrace();
                                }
                                throw e instanceof CompletionException ? (CompletionException) e : new CompletionException(cause != null ? cause : e);
                            }))
                    .toList();

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            for (CompletableFuture<Void> f : futures) {
                if (f.isCompletedExceptionally()) {
                    try {
                        f.get();
                    } catch (ExecutionException ex) {
                        Throwable cause = ex.getCause();
                        if (cause == null) {
                            throw new RuntimeException("Session creation failed", ex);
                        }
                        throw cause instanceof RuntimeException r ? r : new RuntimeException(cause);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(ie);
                    }
                }
            }
            logger.info("SessionPool filled");
        } finally {
            threadPool.shutdown();
        }
    }

    @Override
    public void drain() {
        logger.info("Started draining Session Pool");
        queue.forEach(session -> {
            if (session != null) {
                try {
                    session.driver().quit();
                } catch (Exception e) {
                    logger.error("Failed to quit driver");
                    logger.error(e);
                }
            } else {
                logger.warn("Session is null in queue, skipping");
            }
        });
        logger.info("Session Pool Drained");
    }

    private Session<WebDriver> sanitizedSession(Session<WebDriver> session) {
        if (session == null) return session;
        if (session.driver() == null && Configs.PLATFORM == Platform.DWEB) {
            Session<WebDriver> s = sessionSupplier.get();
            if (s == null) throw new IllegalStateException("sessionSupplier returned null");
            return s;
        }
        SessionService<WebDriver> sessionService = new SessionService<>(session);
        if (!sessionService.isValid()) {
            try {
                sessionService.getConnection().quit();
            } catch (Exception e) {
                // Driver may already be quit (e.g. by ExceptionAnalyser); still renew so we never return a dead session
                logger.debug("quit() threw (driver may already be quit), renewing session anyway: " + e.getMessage());
            }
            Session<WebDriver> renewed = sessionSupplier.get();
            if (renewed == null) throw new IllegalStateException("sessionSupplier returned null");
            return renewed;
        }
        return session;
    }

    private static boolean isWebPlatform() {
        return Configs.PLATFORM == Platform.DWEB
                || Configs.PLATFORM == Platform.AMWEB
                || Configs.PLATFORM == Platform.IMWEB;
    }

    /**
     * Non-blocking session sanitization with timeout for web platforms (DWEB, AMWEB, IMWEB).
     * If renewal times out, returns original session only when the background task has not quit it;
     * otherwise returns a new session to avoid returning a quit WebDriver.
     */
    private Session<WebDriver> sanitizedSessionWithTimeout(Session<WebDriver> session) {
        if (session == null) {
            if (isWebPlatform()) {
                Session<WebDriver> s = sessionSupplier.get();
                if (s == null) throw new IllegalStateException("sessionSupplier returned null");
                return s;
            }
            return session;
        }
        if (session.driver() == null) {
            if (isWebPlatform()) {
                Session<WebDriver> s = sessionSupplier.get();
                if (s == null) throw new IllegalStateException("sessionSupplier returned null");
                return s;
            }
            return session;
        }
        try {
            SessionService<WebDriver> sessionService = new SessionService<>(session);
            if (!sessionService.isValid()) {
                final Session<WebDriver> originalSession = session;
                final AtomicBoolean renewalCancelled = new AtomicBoolean(false);
                final AtomicBoolean driverQuitCalled = new AtomicBoolean(false);
                CompletableFuture<Session<WebDriver>> renewalFuture = CompletableFuture.supplyAsync(() -> {
                    if (renewalCancelled.get()) return originalSession;
                    try {
                        // Set true before quit so main thread knows we are committed. If we then see cancellation we must still quit.
                        driverQuitCalled.set(true);
                        if (renewalCancelled.get()) {
                            sessionService.getConnection().quit();
                            return originalSession;
                        }
                        sessionService.getConnection().quit();
                        if (renewalCancelled.get()) return originalSession;
                        Session<WebDriver> newSession = sessionSupplier.get();
                        // If main thread timed out and cancelled, quit the new driver to avoid resource leak
                        if (renewalCancelled.get()) {
                            try {
                                if (newSession != null && newSession.driver() != null) {
                                    newSession.driver().quit();
                                }
                            } catch (Exception ignored) {
                                logger.debug("Quit orphan driver after timeout", ignored);
                            }
                            return originalSession;
                        }
                        return newSession;
                    } catch (Exception e) {
                        logger.error("Failed to renew driver in async renewal", e);
                        // If we already quit the original driver, we must not return it (dead session); create new or rethrow
                        if (driverQuitCalled.get()) {
                            try {
                                Session<WebDriver> fallback = sessionSupplier.get();
                                if (fallback == null) {
                                    throw new IllegalStateException("sessionSupplier returned null after failed renewal");
                                }
                                return fallback;
                            } catch (Exception e2) {
                                logger.error("Could not create new session after failed renewal", e2);
                                throw new RuntimeException("Session renewal failed after quitting original driver", e);
                            }
                        }
                        return originalSession;
                    }
                });
                try {
                    Session<WebDriver> renewedSession = renewalFuture.get(10, TimeUnit.SECONDS);
                    logger.info("Session renewed successfully for web platform");
                    return renewedSession;
                } catch (TimeoutException e) {
                    logger.warn("Session renewal timed out after 10 seconds");
                    renewalCancelled.set(true);
                    // cancel(false): do not interrupt the background task. If we interrupt during
                    // sessionSupplier.get(), the supplier may have already created a new WebDriver
                    // but not yet returned it; we would never get a reference to quit it → resource leak.
                    renewalFuture.cancel(false);
                    // Never return originalSession after timeout: the background may set driverQuitCalled
                    // and quit the driver after we've already read driverQuitCalled, causing a race where
                    // the caller receives a session that is then quit → "session closed" errors.
                    Session<WebDriver> newSession = sessionSupplier.get();
                    if (newSession == null) {
                        throw new IllegalStateException("sessionSupplier returned null after renewal timeout");
                    }
                    return newSession;
                } catch (Exception e) {
                    logger.error("Exception during session renewal: " + e.getMessage(), e);
                    renewalCancelled.set(true);
                    renewalFuture.cancel(false);
                    // Same as timeout: never return originalSession; background may quit it after we check.
                    Session<WebDriver> newSession = sessionSupplier.get();
                    if (newSession == null) {
                        throw new IllegalStateException("sessionSupplier returned null after renewal exception");
                    }
                    return newSession;
                }
            }
        } catch (Exception e) {
            if (isWebPlatform()) {
                logger.warn("Exception in sanitizedSessionWithTimeout, renewing for web platform: " + e.getMessage());
                try {
                    if (session.driver() != null) session.driver().quit();
                } catch (Exception ignored) { }
                Session<WebDriver> s = sessionSupplier.get();
                if (s == null) throw new IllegalStateException("sessionSupplier returned null");
                return s;
            }
        }
        return session;
    }

    /** Takes from the queue, wrapping InterruptedException in RuntimeException. */
    private Session<WebDriver> takeSecurely(BlockingQueue<Session<WebDriver>> queue) {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    private void putSecurely(BlockingQueue<Session<WebDriver>> queue, Session<WebDriver> session) {
        try {
            queue.put(session);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }
}
