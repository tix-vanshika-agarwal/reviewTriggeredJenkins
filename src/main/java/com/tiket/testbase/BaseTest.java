package com.tiket.testbase;

import com.aventstack.extentreports.ExtentTest;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.dbclient.TiketMysqlDBManager;
import com.tiket.logging.ILogger;
import com.tiket.model.Session;
import com.tiket.pool.Pool;
import com.tiket.pool.SessionPool;
import com.tiket.report.ExtentReportManager;
import com.tiket.testng.TestListener;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class BaseTest {

    public static final int POOL_SIZE = Configs.SESSIONS;
    public static final int TIME_LIMIT_MINUTES = Double.valueOf(Configs.PROPERTIES_DATA.get("session.timelimit")).intValue();
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    public static final ThreadLocal<ILogger> mainLogger = new ThreadLocal<>();
    public static final ThreadLocal<Map<String, String>> paramsThreadLocal = new ThreadLocal<>();
    public static final ThreadLocal<Session<WebDriver>> sessionThreadLocal = new ThreadLocal<>();
    private static Pool<Session<WebDriver>> pool;

    @BeforeSuite
    public void beforeSuite(ITestContext context) {
        Runtime.getRuntime().addShutdownHook(new Thread("Shutdown-thread") {
            @Override
            public void run() {
                logger.info("Shutdown hook triggered. Closing resources...");
                ExtentReportManager.flushReports(true);
                logger.info("Extent reports flushed.");
                if (pool != null) {
                    pool.drain();
                    logger.info("Session pool closed.");
                }
            }
        });
        Supplier<Session<WebDriver>> sessionSupplier = new SessionSupplier(TIME_LIMIT_MINUTES);
        pool = new SessionPool(POOL_SIZE, sessionSupplier);
        pool.fill();
    }

    @BeforeMethod
    public void beforeMethod(ITestContext context, Method method) {
        sessionThreadLocal.set(pool.take());
        context.setAttribute("session", sessionThreadLocal.get());
        context.setAttribute("mainLogger", mainLogger);
        logger.info("Using session: " + sessionThreadLocal.get());
        System.setProperty("setVerticalName",method.getAnnotation(Vertical.class).name().toLowerCase(Locale.ROOT));

    }

    @AfterMethod
    public void afterMethod(ITestResult result, ITestContext context) {
        ExtentTest test = TestListener.extentTestThreadLocal.get();
        if(result.wasRetried()) {
            log("Retrying test: " + result.getMethod().getMethodName() + ", Retry count: " + result.getMethod().getCurrentInvocationCount());
            TestListener.extent.removeTest(test);
        } else {
            log("Test finished: " + result.getMethod().getMethodName());
            setAnnotations(result, test);
        }
        Session<WebDriver> session = sessionThreadLocal.get();
        try {
            if (session != null) {
                if (pool != null) {
                    logger.info("Returning session: " + session);
                    pool.put(session);
                } else {
                    logger.warn("Session pool is null in afterMethod; quitting driver directly instead of returning to pool.");
                    if (session.driver() != null) {
                        try {
                            session.driver().quit();
                        } catch (Exception qe) {
                            logger.error("Failed to quit driver when pool is null in afterMethod", qe);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error returning session to pool", e);
            if (session != null && session.driver() != null) {
                try {
                    session.driver().quit();
                } catch (Exception qe) {
                    logger.error("Failed to quit driver on afterMethod error", qe);
                }
            }
        } finally {
            sessionThreadLocal.remove();
        }
        TestListener.extentTestThreadLocal.remove();
    }

    @AfterSuite
    public void afterSuite() {
        TiketMysqlDBManager.closeDataSourceConnection();
        if (pool != null) {
            pool.drain();
        }
        com.tiket.report.TribeReportView.reset();
    }

    private void setAnnotations(ITestResult result, ExtentTest test) {
        setAuthorAnnotation(result, test);
        setVerticalAnnotation(result, test);
        collectTribeResult(result, test);
    }

    private void collectTribeResult(ITestResult result, ExtentTest test) {
        try {
            Method method = result.getMethod().getConstructorOrMethod().getMethod();
            Tribe tribe = method.getAnnotation(Tribe.class);
            if (tribe == null) {
                tribe = result.getTestClass().getRealClass().getAnnotation(Tribe.class);
            }
            if (tribe == null || tribe.name().isBlank()) {
                return;
            }
            com.tiket.report.TribeReportView.record(
                    tribe.name(),
                    test.getModel().getId(),
                    test.getModel().getName(),
                    statusLabel(result.getStatus()),
                    result.getStartMillis());
        } catch (Exception e) {
            logger.warn(ExceptionUtils.getStackTrace(e));
        }
    }

    private static String statusLabel(int status) {
        return switch (status) {
            case ITestResult.FAILURE -> "fail";
            case ITestResult.SKIP -> "skip";
            default -> "pass";
        };
    }

    private void setAuthorAnnotation(ITestResult result, ExtentTest test) {
        try {
            String[] authors = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Author.class).names();
            test.assignAuthor(authors);
        } catch (Exception e) {
            logger.warn(ExceptionUtils.getStackTrace(e));
        }
    }

    private void setVerticalAnnotation(ITestResult result, ExtentTest test) {
        try {
            String module = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Module.class).name();
            test.assignCategory(module);
        } catch (Exception e) {
            logger.warn(ExceptionUtils.getStackTrace(e));
        }
    }

    protected void sleep(int seconds) {
        try {
            Thread.sleep(1000L * seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void sleep() {
        sleep(10);
    }

    protected void log(String msg) {
        mainLogger.get().log(msg);
    }

    protected void step(String step) {
        mainLogger.get().step(step);
    }

    /**
     * Renews the current thread's session by quitting the existing driver and creating a brand-new
     * driver session (which reinstalls/relaunches the app fresh on the device farm), then swaps it
     * into the thread-local so subsequent page objects use it. Intended for tests that require a
     * genuine fresh install (e.g. onboarding). The renewed session carries a new sessionId, so
     * per-session state such as HomePage FLAG_MAP naturally resets and onboarding runs again.
     * The renewed session is returned to the pool by {@link #afterMethod}.
     *
     * <p>Callers must only invoke this when the current session is NOT already a fresh install,
     * otherwise a fresh session would be discarded needlessly.
     */
    public static Session<WebDriver> renewSessionForFreshInstall() {
        Session<WebDriver> old = sessionThreadLocal.get();
        String previousSessionId = old != null ? old.sessionId() : "none";
        if (old != null && old.driver() != null) {
            try {
                old.driver().quit();
            } catch (Exception e) {
                logger.error("Failed to quit existing driver while renewing session for fresh install", e);
            }
        }
        Session<WebDriver> fresh = new SessionSupplier(TIME_LIMIT_MINUTES).get();
        sessionThreadLocal.set(fresh);
        String msg = "Renewed session for fresh install. Now running on session: " + fresh
                + " (previous session was: " + previousSessionId + ")";
        logger.info(msg);
        // Surface the new session id in the Extent report timeline; the report header's "running with
        // Session" line is emitted in onTestStart (before renewal) and therefore shows the old session.
        ILogger reportLogger = mainLogger.get();
        if (reportLogger != null) {
            reportLogger.log(msg);
        }
        return fresh;
    }
}
