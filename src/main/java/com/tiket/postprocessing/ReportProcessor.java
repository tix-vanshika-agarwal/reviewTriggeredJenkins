
package com.tiket.postprocessing;

import com.tiket.appium.DriverTools;
import com.tiket.common.RetryUtils;
import com.tiket.core.CoreFunctions;
import com.tiket.logging.ILogger;
import com.tiket.logging.Log4JLogger;
import com.tiket.model.Locator;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class ReportProcessor {

    public static final String EXTENT_REPORT_LOCATION = "extent-reports/extent-report.html";
    private static final ILogger logger = new Log4JLogger();
    private static final Locator SUMMARY = new Locator("Summary Icon", By.id("nav-dashboard"));
    private static final Locator SUMMARY_CONTAINER = new Locator("Summary Container", By.xpath("//div[contains(@class,'dashboard-view')]//div[@class='row'][2]//div[contains(@class,'col-md-')][1]"));

    private static final int MAX_RETRY = 3;

    public static String processReport() {

        WebDriver driver = null;
        try {
            driver = getDriver();
            final WebDriver finalDriver = driver;
            DriverTools<WebDriver> driverTools = new DriverTools<>(driver, logger);

            File file = new File(EXTENT_REPORT_LOCATION);
            openExtentReport(driverTools, file);

            Boolean gotoSummaryResult = retry(() -> gotoSummary(driverTools), "Goto summary");
            if (Boolean.FALSE.equals(gotoSummaryResult) || gotoSummaryResult == null)
                return null;

            String summaryScreenshotName = "SummaryScreenshot";

            String result = retry(() ->
                            takeScreenshotSafely(finalDriver, driverTools, summaryScreenshotName, SUMMARY_CONTAINER),
                    "Take summary screenshot");

            if (result == null) {
                logger.log("Screenshot failed after retries - continuing WITHOUT screenshot.");
            } else {
                logger.log("Summary screenshot taken at: " + result);
                // Register summary screenshot for cleanup tracking
                ScreenshotCleanupManager.registerSummaryScreenshot(result);
            }

            return result;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.fail("Thread was interrupted in processReport: " + e.getMessage());
            return null;

        } finally {
            if (driver != null) {
                try { driver.quit(); } catch (Exception ignored) {}
            }
        }
    }

    public static String processReportSource() {
        WebDriver driver = null;
        try {
            driver = getDriver();
            DriverTools<WebDriver> driverTools = new DriverTools<>(driver, logger);

            File file = new File(EXTENT_REPORT_LOCATION);
            openExtentReport(driverTools, file);
            gotoSummary(driverTools);
            return driver.getPageSource();

        } catch (Exception e) {
            logger.fail("Failed to process report source: " + e.getMessage());
            return null;
        } finally {
            if (driver != null) {
                try { driver.quit(); } catch (Exception ignored) {}
            }
        }
    }

    private static void openExtentReport(DriverTools<WebDriver> driverTools, File file) {
        driverTools.get("file://" + file.getAbsolutePath());
    }

    @SneakyThrows
    private static boolean gotoSummary(DriverTools<WebDriver> driverTools) {
        try {
            driverTools.click(SUMMARY);

            // explicit wait using driverTools.element(locator)
            Optional<WebElement> maybe = waitForElementVisible(driverTools, SUMMARY_CONTAINER, 10);
            if (maybe.isPresent()) return true;
            logger.warn("SUMMARY_CONTAINER not visible after wait.");
            return false;

        } catch (Exception e) {
            logger.fail("gotoSummary failed: " + e.getMessage());
            return false;
        }
    }

    private static String takeScreenshotSafely(WebDriver driver,
                                               DriverTools<WebDriver> driverTools,
                                               String name,
                                               Locator locator) {
        try {
            Optional<WebElement> maybe = waitForElementVisible(driverTools, locator, 12);
            if (maybe.isEmpty()) {
                logger.fail("Timeout waiting for summary container (screenshot).");
                return null;
            }
            WebElement container = maybe.get();

            // If container has canvas => ensure chart is rendered
            try {
                WebElement canvas = container.findElement(By.tagName("canvas"));
                boolean rendered = waitForChartRendered(driver, canvas);

                if (!rendered) {
                    logger.fail("Chart canvas not rendered (size still zero).");
                    return null;
                }

                // Additional wait for ChartJS animation
                Thread.sleep(500);

            } catch (Exception ignore) {
                // If no canvas inside container, continue normally
                logger.log("Canvas chart unavailable during screenshot.");
            }

            return DriverTools.fileScreenshot(driver, name, container);
        } catch (TimeoutException te) {
            logger.fail("Timeout waiting for summary container: " + te.getMessage());
            return null;
        } catch (Exception e) {
            logger.fail("Screenshot error: " + e.getMessage());
            return null;
        }
    }

    private static boolean waitForChartRendered(WebDriver driver, WebElement canvas) {
        try {
            // Waiting for chart to have a visible size
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> {
                        int w = canvas.getSize().getWidth();
                        int h = canvas.getSize().getHeight();
                        return w > 30 && h > 30;  // chart already rendered
                    });
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits up to timeoutSeconds for driverTools.element(locator) to be present and displayed.
     * Polls every 500ms. Returns Optional.empty() on timeout.
     */
    private static Optional<WebElement> waitForElementVisible(DriverTools<WebDriver> driverTools,
                                                              Locator locator,
                                                              int timeoutSeconds) throws InterruptedException {
        Instant end = Instant.now().plusSeconds(timeoutSeconds);
        while (Instant.now().isBefore(end)) {
            try {
                Optional<WebElement> maybe = driverTools.element(locator);
                if (maybe.isPresent()) {
                    WebElement el = maybe.get();
                    try {
                        if (el.isDisplayed()) return Optional.of(el);
                    } catch (StaleElementReferenceException sere) {
                        // element got stale, continue polling
                    }
                }
            } catch (Exception ignore) {
                // driverTools.element might throw intermittently; swallow and retry
                logger.log("Exception in waitForElementVisible polling: " + ignore.getMessage());
            }
            Thread.sleep(500L);
        }
        return Optional.empty();
    }

    /**
     * Generic retry wrapper.
     */
    private static <T> T retry(SupplierWithException<T> action, String label) throws InterruptedException {
        Exception last = null;
        for (int i = 1; i <= MAX_RETRY; i++) {
            try {
                return action.get();
            } catch (Exception e) {
                last = e;
                logger.warn(label + " failed attempt " + i + ": " + e.getMessage());
                Thread.sleep(1500L * i);
            }
        }
        logger.log(label + " FAILED after retry. Root cause: " + (last != null ? last.getMessage() : "-"));
        return null;
    }

    private interface SupplierWithException<T> {
        T get() throws Exception;
    }

    private static String takeScreenshot(WebDriver driver, DriverTools<WebDriver> driverTools, String name, Locator locator) {
        WebElement container = driverTools.element(locator).orElseThrow();
        return DriverTools.fileScreenshot(driver, name, container);
    }

    private static WebDriver getDriver() {
        if (CoreFunctions.isJenkinsSystem()) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions option = new ChromeOptions();
            option.addArguments("--no-sandbox");
            option.addArguments("--headless"); // should be enabled for Jenkins
            option.addArguments("--disable-dev-shm-usage"); // should be enabled for Jenkins
            option.addArguments("--window-size=1920x1080");
            option.setCapability(ChromeOptions.CAPABILITY, option);

            return RetryUtils.retryWithExponentialBackoff(
                    5,
                    100L,
                    () -> new ChromeDriver(option),
                    RetryUtils::isChromeDriverPortConflict,
                    "ChromeDriver creation (free port)"
            );
        } else {
            System.setProperty("webdriver.http.factory", "jdk-http-client");
            return WebDriverManager.chromedriver().create();
        }
    }
}
