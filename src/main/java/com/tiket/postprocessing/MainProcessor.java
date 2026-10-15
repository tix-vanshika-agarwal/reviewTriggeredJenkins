package com.tiket.postprocessing;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.tiket.appium.DriverTools;
import com.tiket.capabilities.CapabilitiesHandler;
import com.tiket.core.CoreFunctions;
import com.tiket.io.CachedPool;
import com.tiket.io.network.LambdaTestCredentials;
import com.tiket.io.network.LambdaTestTunnelValidator;
import com.tiket.logging.ExecutionLogManager;
import com.tiket.model.DeviceFarm;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.postprocessing.db.DBProcessor;
import com.tiket.postprocessing.testrail.TestRailProcessor;
import com.tiket.report.ExtentReportManager;
import com.tiket.report.MongoToInfluxUploader;
import com.tiket.service.RunID;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import com.tiket.testng.TestListener;
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
public class MainProcessor {

    private static final Logger logger = LogManager.getLogger(MainProcessor.class);
    private static final String RUN_ID = String.valueOf(Float.valueOf(RunID.getID()).intValue()); // Local excel sheet gives value in decimal string e.g., "2160.0"

    private static DBProcessor dbProcessor;
    private static final List<CompletableFuture<Object>> futureResponse = new ArrayList<>();

    public static void onStart(ITestContext context) {
        ExecutionLogManager.init();
//        dbProcessor = new DBProcessor();

        killZombieChromeProcesses();
        
        // Validate LambdaTest tunnel BEFORE creating driver sessions
        if (shouldValidateLambdaTestTunnel()) {
            validateLambdaTestTunnel();
        } else {
            logger.info("Suite Start - Skipping tunnel validation (Platform: " + Configs.PLATFORM + ", DeviceFarm: " + Configs.DEVICE_FARM + ")");
        }
    }

    public static void onTestStart() {
        // Tunnel validation already done at Suite level in onStart()
        // No need to validate again for each test
        logger.debug("Test starting - Tunnel already validated at Suite level");
    }

    public static void onTestSuccess(ITestResult result, ExtentTest test) {
        String caseID = getCaseID();
        test.info("TestRailID: " + caseID);
        uploadTestSuccess(result);
        flushPerTestIfSafe();
        CompletableFuture<Object> object = TestRailProcessor.sendResult(result, RUN_ID, caseID, null);
        futureResponse.add(object);
//        dbProcessor.sendResult(result, RUN_ID, caseID);
    }

    public static void onTestFailure(ITestResult result, ExtentTest test) {
        String screenshotPath = screenshot(result, test);
        String caseID = getCaseID();
        test.info("TestRailID: " + caseID);
        uploadTestFailure(result);
        flushPerTestIfSafe();
        
        // Register screenshot for cleanup tracking
        boolean willUploadToTestRail = screenshotPath != null && !caseID.equals("MissingTestID");
        ScreenshotCleanupManager.registerTestScreenshot(screenshotPath, willUploadToTestRail);
        
        CompletableFuture<Object> object = TestRailProcessor.sendResult(result, RUN_ID, caseID, screenshotPath);
        futureResponse.add(object);
//        dbProcessor.sendResult(result, RUN_ID, caseID);
    }

    public static void onTestSkipped(ITestResult result, ExtentTest test) {
        String screenshotPath = screenshot(result, test);
        String caseID = getCaseID();
        test.info("TestRailID: " + caseID);
        uploadTestFailure(result);
        flushPerTestIfSafe();
        
        // Register screenshot for cleanup tracking
        boolean willUploadToTestRail = screenshotPath != null && !caseID.equals("MissingTestID");
        ScreenshotCleanupManager.registerTestScreenshot(screenshotPath, willUploadToTestRail);
        
        CompletableFuture<Object> object = TestRailProcessor.sendResult(result, RUN_ID, caseID, screenshotPath);
        futureResponse.add(object);
//        dbProcessor.sendResult(result, RUN_ID, caseID);
    }

    public static void onFinish(ITestContext context, ExtentReports extentReports) {
        ExtentReportManager.flushReports(true);

        // Mark extent report as generated - this may trigger screenshot cleanup
        logger.info("Extent report finalized, marking for potential screenshot cleanup");
        ScreenshotCleanupManager.markExtentReportGenerated();
        
        // Wait for TestRail uploads to complete before potential cleanup
        getTestRailLogs();
        
        CachedPool.close(); // Not closing will keep the java process alive.
//        dbProcessor.close();
        logger.info("DB_USE: " + Configs.DB_USE + ", GRAFANA_USE: " + Configs.GRAFANA_USE);
        sendDataToInflux();
        
        // Log cleanup statistics for debugging
        logger.info(ScreenshotCleanupManager.getStatistics());
    }

    /**
     * Per-test flush gate.
     *
     * <p>When the Klov reporter is attached ({@link Configs#DB_USE} == true),
     * calling {@code extent.flush()} after every individual test is harmful:
     * <ul>
     *   <li>It pushes the entire report state to MongoDB on every test end,
     *       multiplying write load by N tests and forcing every parallel
     *       TestNG worker to serialize on the {@code synchronized} flush.</li>
     *   <li>Combined with {@code extent.removeTest(...)} on retries
     *       (see {@code BaseTest.afterMethod}), it leaves orphan/empty
     *       documents in the {@code klov.test} collection.</li>
     * </ul>
     * The {@code onFinish} flush already publishes the complete run.
     *
     * <p>When Klov is NOT attached, we keep the previous behaviour of
     * incrementally flushing the Spark HTML on local runs so devs can watch
     * the report grow during execution. Jenkins runs continue to skip it
     * (the workspace is sealed until the suite ends anyway).
     */
    private static void flushPerTestIfSafe() {
        if (Configs.DB_USE) {
            return;
        }
        ExtentReportManager.flushReports(!CoreFunctions.isJenkinsSystem());
    }

    public static void processEmailWithCleanup(String summaryResultImagePath) {
        if (summaryResultImagePath == null || summaryResultImagePath.isEmpty()) {
            logger.warn("processEmailWithCleanup called with null/empty screenshot path - skipping email");
            return;
        }
        
        try {
            EmailProcessor.processEmail(summaryResultImagePath);
            // Mark email as sent for cleanup tracking
            ScreenshotCleanupManager.markEmailSent(true);
        } catch (Exception e) {
            logger.error("Failed to send email", e);
            ScreenshotCleanupManager.markEmailSent(false);
            throw e;
        }
    }

    /**
     * Run the fallback email report flow when the primary screenshot-based flow
     * could not produce a dashboard image. The fallback parses the extent report
     * directly from disk (no Selenium) and sends an email with the same data.
     *
     * @return {@code true} when the fallback email was sent, {@code false} otherwise.
     */
    public static boolean processFallbackEmailWithCleanup() {
        boolean sent = false;
        try {
            sent = FallbackEmailReportProcessor.processReport();
        } catch (Exception e) {
            logger.error("Fallback email processing failed", e);
        } finally {
            ScreenshotCleanupManager.markEmailSent(sent);
        }
        return sent;
    }

    public static void onTestFailedButWithinSuccessPercentage(ExtentReports extentReports) {
        flushPerTestIfSafe();
    }

    public static void onTestFailedWithTimeout(ITestResult result, ExtentTest test) {
        String screenshotPath = screenshot(result, test);
        String caseID = getCaseID();
        test.info("TestRailID: " + caseID);
        flushPerTestIfSafe();
        
        // Register screenshot for cleanup tracking
        boolean willUploadToTestRail = screenshotPath != null && !caseID.equals("MissingTestID");
        ScreenshotCleanupManager.registerTestScreenshot(screenshotPath, willUploadToTestRail);
        
        CompletableFuture<Object> object = TestRailProcessor.sendResult(result, RUN_ID, caseID, screenshotPath);
        futureResponse.add(object);
    }

    private static String getCaseID() {
        String caseID;
        String missingID = "MissingTestID";
        try {
            caseID = BaseTest.paramsThreadLocal.get().getOrDefault("testID", missingID);
        } catch (NullPointerException npe) {
            caseID = missingID;
            logger.error(ExceptionUtils.getStackTrace(npe));
        } finally {
            BaseTest.paramsThreadLocal.remove();
        }
        return caseID;
    }

    public static String screenshot(ITestResult result, ExtentTest test) {
        Session<WebDriver> session = BaseTest.sessionThreadLocal.get();
        if (session == null) {
            logger.warn("Session is null, skipping screenshot (driver may already be quit)");
            test.log(Status.WARNING, "Skipped screenshot: session unavailable");
            return null;
        }
        String screenshotPath = null;
        try {
            WebDriver driver = session.driver();
            if (driver == null) {
                logger.warn("Driver is null, skipping screenshot");
                return null;
            }
            screenshotPath = DriverTools.fileScreenshot(driver, result.getMethod().getMethodName());
            BaseTest.mainLogger.get().log("Screenshot: " + screenshotPath);
            String base64ForExtent = CoreFunctions.encodeImageFileForExtentReport(new File(screenshotPath));
            if (base64ForExtent == null) {
                base64ForExtent = DriverTools.base64Screenshot(driver);
            }
            test.info("Screenshot", test.addScreenCaptureFromBase64String(base64ForExtent).getModel().getMedia().get(0));
        } catch (NoSuchSessionException e) {
            logger.warn("Driver session already closed, skipping screenshot: " + e.getMessage());
            test.log(Status.WARNING, "Skipped screenshot: session already closed");
        } catch (Exception e) {
            test.log(Status.WARNING, "Failed to capture screenshot");
            logger.error("Failed to capture screenshot");
        }
        return screenshotPath;
    }

    private static URL getURLServer(){
        CapabilitiesHandler handler = new CapabilitiesHandler();
        JSONArray configs = handler.getConfigs();
        JSONObject config = handler.getConfig(configs);
        URL server = handler.getUrl(config);
        return server;
    }

    private static WebDriver getDriver(){
        Session<WebDriver> session = BaseTest.sessionThreadLocal.get();
        return session == null ? null : session.driver();
    }

    private static void uploadTestFailure(ITestResult result){
        WebDriver driver = getDriver();
        if (driver == null) {
            logger.debug("No driver available for uploadTestFailure (session may already be closed), skipping");
            return;
        }
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        URL server =  getURLServer();
        boolean onBS = server.toString().contains("hub-cloud.browserstack.com");
        boolean onLT = server.toString().contains("hub.lambdatest.com");
        try{
            String set_comment = "Test failed: " + result.getMethod().getConstructorOrMethod().getMethod().getName()+"_"+TestListener.timestamp.get();
            if(onBS){
                jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"failed\",\"reason\": \""+set_comment+"\"}}");
            } else if (onLT) {
                jse.executeScript("lambda-hook: {\"action\": \"setTestStatus\",\"arguments\": {\"status\":\"failed\", \"remark\": \""+set_comment+"\"}} ");
            }
        }catch(Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }

    }

    private static void uploadTestSuccess(ITestResult result){
        WebDriver driver = getDriver();
        if (driver == null) {
            logger.debug("No driver available for uploadTestSuccess (session may already be closed), skipping");
            return;
        }
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        URL server =  getURLServer();
        boolean onBS = server.toString().contains("hub-cloud.browserstack.com");
        boolean onLT = server.toString().contains("hub.lambdatest.com");
        try{
            String set_comment = "Test passed: " + result.getMethod().getConstructorOrMethod().getMethod().getName()+"_"+TestListener.timestamp.get();
            if(onBS){
                jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"passed\",\"reason\": \""+set_comment+"\"}}");
            } else if (onLT) {
                jse.executeScript("lambda-hook: {\"action\": \"setTestStatus\",\"arguments\": {\"status\":\"passed\", \"remark\": \"" + set_comment + "\"}} ");
            }
        }catch(Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }
     
    }

    private static void getTestRailLogs() {
        for (CompletableFuture<Object> objectCompletableFuture : futureResponse) {
            while (!objectCompletableFuture.isDone()) {
                logger.info("Waiting response from TestRail");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                logger.info("Success Update TestRail -> " + objectCompletableFuture.get());
            } catch (InterruptedException | ExecutionException exception) {
                logger.info("Failed Update TestRail -> " + exception);
            }
        }
    }

    private static void sendDataToInflux() {
        logger.info("Attempting to send data to Influx...");
        if(Configs.DB_USE && Configs.GRAFANA_USE){
            logger.info("Calling attachGrafanaReporter...");
            MongoToInfluxUploader.attachGrafanaReporter();
        } else {
            logger.warn("Skipping Influx upload - DB_USE: " + Configs.DB_USE + ", GRAFANA_USE: " + Configs.GRAFANA_USE);
        }
    }

    /**
     * Validates LambdaTest tunnel availability and throws SkipException if validation fails
     */
    private static void validateLambdaTestTunnel() {
        logger.info("=================================");
        logger.info("Suite Start - Validating LambdaTest tunnel for " + Configs.PLATFORM + " platform...");
        try {
            String tunnelName = LambdaTestCredentials.getTunnelName();
            logger.info("Expected tunnel from capabilities: " + tunnelName);

            LambdaTestTunnelValidator.validateTunnelOrThrow();
            logger.info("LambdaTest tunnel validation successful - Suite can proceed");
            logger.info("=================================");
        } catch (Exception e) {
            logger.error("=================================");
            logger.error("LambdaTest tunnel validation failed at Suite level");
            logger.error("Error: " + e.getMessage());
            logger.error("=================================");
            // Skip entire suite if tunnel is not available
            String skipMsg = "Skipping entire test suite - LambdaTest tunnel not available: " + e.getMessage();
            logger.fatal(skipMsg);
            throw new SkipException(skipMsg);
        }
    }

    /**
     * Checks if tunnel validation should be performed
     * Validates tunnel for LambdaTest on mobile platforms (ANDROID, IOS, IMWEB, AMWEB)
     * DWEB/panel platform is excluded from tunnel validation
     * Also checks if tunnel is enabled in capabilities (lt:options -> tunnel: true)
     *
     * @return true if tunnel validation should be performed, false otherwise
     */
    private static boolean shouldValidateLambdaTestTunnel() {
        if (Configs.DEVICE_FARM != DeviceFarm.LAMBDA_TEST) {
            logger.debug("Not running on LambdaTest - skipping tunnel validation");
            return false;
        }

        // Check if it's a mobile platform (exclude DWEB/panel)
        boolean isMobilePlatform = Configs.PLATFORM == Platform.ANDROID ||
                Configs.PLATFORM == Platform.IOS ||
                Configs.PLATFORM == Platform.IMWEB ||
                Configs.PLATFORM == Platform.AMWEB;

        if (!isMobilePlatform) {
            logger.debug("Platform " + Configs.PLATFORM + " excluded from tunnel validation");
            return false;
        }

        // Check if tunnel is enabled in capabilities
        boolean tunnelEnabled = LambdaTestCredentials.isTunnelEnabled();
        if (!tunnelEnabled) {
            logger.info("Tunnel not enabled in capabilities (lt:options -> tunnel is not true) - skipping validation");
            return false;
        }

        logger.debug("All conditions met for tunnel validation: LambdaTest + Mobile Platform + Tunnel Enabled");
        return true;
    }

    @SneakyThrows
    private static void killZombieChromeProcesses() {
        logger.info("Attempting to kill zombie Chrome processes...");
        Process process = new ProcessBuilder("sh", "-c", "ps -eo pid=,ppid=,cmd= | grep -E 'chrome|chromedriver' | grep -v grep")
                .redirectErrorStream(true)
                .start();
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("chrome")) {
                        try {
                            String[] parts = line.trim().split("\\s+", 3);
                            if (parts.length < 2) {
                                logger.warn("Skipping malformed process line: " + line);
                                continue;
                            }

                            String pid = parts[0];
                            String ppid = parts[1];

                            if ("1".equals(ppid)) {
                                Process killProcess = new ProcessBuilder("kill", "-9", pid)
                                        .redirectErrorStream(true)
                                        .start();
                                if (!killProcess.waitFor(2, TimeUnit.SECONDS)) {
                                    killProcess.destroyForcibly();
                                    logger.warn("Timed out waiting for kill command to finish for PID: " + pid);
                                } else if (killProcess.exitValue() == 0) {
                                    logger.info("Killed zombie chrome PID: " + pid);
                                } else {
                                    logger.warn("Failed to kill zombie chrome PID: " + pid + " with exit code: " + killProcess.exitValue());
                                }
                            }
                        } catch (Exception e) {
                            logger.warn("Error processing line: " + line, e);
                        }
                    }
                }
            }
            if (!process.waitFor(10, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                logger.warn("Timed out waiting for ps process to finish; process was forcefully terminated");
            }
        } finally {
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }
}
