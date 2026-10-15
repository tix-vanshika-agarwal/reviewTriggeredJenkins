package com.tiket.testng;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.logging.ExtentLogger;
import com.tiket.logging.ILogger;
import com.tiket.logging.Log4JLogger;
import com.tiket.logging.MainLogger;
import com.tiket.model.Session;
import com.tiket.postprocessing.MainProcessor;
import com.tiket.postprocessing.ReportProcessor;
import com.tiket.report.ExtentReportManager;
import com.tiket.report.TestCountTracker;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);
    public static final ThreadLocal<Long> timestamp = new ThreadLocal<>();

    public static ExtentReports extent = ExtentReportManager.getReports();
    public static ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        timestamp.set(System.currentTimeMillis());
        try {
            ITestContext context = result.getTestContext();
            Session<WebDriver> session = (Session) context.getAttribute("session");
            ThreadLocal<ILogger> mainLogger = (ThreadLocal<ILogger>) context.getAttribute("mainLogger");
            ExtentTest test = extent.createTest(ExtentReportManager.displayNameForExtent(result));
            extentTestThreadLocal.set(test);

            mainLogger.set(new MainLogger(new ExtentLogger(test), new Log4JLogger()));

            mainLogger.get().log("Test start: " + result.getMethod().getMethodName());
            logger.info("Test start: " + result.getMethod().getMethodName());

            String msg = String.format("Test (%s) running with Session: %s", result.getMethod().getMethodName(), session);
            test.info(msg);
            logger.info(msg);

            TestCountTracker.incrementTestsStarted(result.getMethod().getMethodName());
            MainProcessor.onTestStart();
        } catch (Exception e) {
            logger.error("Error in onTestStart for: " + result.getMethod().getMethodName(), e);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ITestListener.super.onTestSuccess(result);


        ExtentTest test = extentTestThreadLocal.get();
        test.log(Status.PASS, "Test success: " + result.getMethod().getMethodName());
        logger.info("Test success: " + result.getMethod().getMethodName());

        TestCountTracker.incrementTestsCompleted(result.getMethod().getMethodName(), "PASS");
        MainProcessor.onTestSuccess(result, test);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ITestListener.super.onTestFailure(result);

        ExtentTest test = extentTestThreadLocal.get();

        test.log(Status.FAIL, "Test failed: " + result.getMethod().getMethodName());
        logger.fatal("Test failed: " + result.getMethod().getMethodName());

        TestCountTracker.incrementTestsCompleted(result.getMethod().getMethodName(), "FAIL");
        MainProcessor.onTestFailure(result, test);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);

        ExtentTest test = extentTestThreadLocal.get();

        test.log(Status.SKIP, "Test skipped: " + result.getMethod().getMethodName());
        logger.error("Test Skipped: " + result.getMethod().getMethodName());

        TestCountTracker.incrementTestsCompleted(result.getMethod().getMethodName(), "SKIP");
        MainProcessor.onTestSkipped(result, test);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);


        ExtentTest test = extentTestThreadLocal.get();
        test.log(Status.FAIL, "Test failed intermittently: " + result.getMethod().getMethodName());
        logger.fatal("Test failed intermittently: " + result.getMethod().getMethodName());

        MainProcessor.onTestFailedButWithinSuccessPercentage(extent);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);

        ExtentTest test = extentTestThreadLocal.get();

        test.log(Status.FAIL, "Test failed with timeout: " + result.getMethod().getMethodName());
        logger.fatal("Test failed with timeout: " + result.getMethod().getMethodName());

        MainProcessor.onTestFailedWithTimeout(result, test);
    }

    @Override
    public void onStart(ITestContext context) {
        MainProcessor.onStart(context);
        ITestListener.super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);

        TestCountTracker.logSummary();

        MainProcessor.onFinish(context, extent);
        processReport();
    }

    @SneakyThrows
    private void processReport() {
        if (!Configs.SEND_EMAIL)
            return;

        String summaryResultImagePath = null;
        try {
            summaryResultImagePath = ReportProcessor.processReport();
        } catch (Exception e) {
            logger.warn("Error processing report before sending email", e);
        }

        if (summaryResultImagePath != null && !summaryResultImagePath.isEmpty()) {
            final int retries = 2;
            for (int i = 0; i < retries; i++) {
                try {
                    MainProcessor.processEmailWithCleanup(summaryResultImagePath);
                    return; // Exit after successful email processing
                } catch (Exception e) {
                    logger.warn("Error sending email, attempt {}", i + 1, e);
                }
                if (i < retries - 1) {
                    Thread.sleep(10000);
                }
            }
            logger.warn("Failed to process report and send email after 2 attempts, using fallback email method");
        } else {
            logger.warn("No summary screenshot available, skipping dashboard screenshot email");
        }

        boolean fallbackSent = MainProcessor.processFallbackEmailWithCleanup();
        if (fallbackSent) {
            logger.info("Fallback email sent successfully (no dashboard screenshot)");
        } else {
            logger.error("Fallback email could not be sent - no email dispatched for this run");
        }
    }
}
