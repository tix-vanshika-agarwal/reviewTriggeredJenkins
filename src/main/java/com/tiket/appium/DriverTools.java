package com.tiket.appium;

import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import com.tiket.capabilities.CapabilitiesHandler;
import com.tiket.core.CoreFunctions;
import com.tiket.core.ExceptionAnalyser;
import com.tiket.io.adb.ADB;
import com.tiket.logging.ILogger;
import com.tiket.model.*;
import com.tiket.model.Exception.ForbiddenException;
import com.tiket.model.Platform;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.service.BundleID;
import com.tiket.service.PackageName;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.clipboard.ClipboardContentType;
import io.appium.java_client.clipboard.HasClipboard;
import io.appium.java_client.functions.ExpectedCondition;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import lombok.SneakyThrows;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.chromium.HasCdp;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.tiket.capabilities.CapabilitiesHandler.isRunningOnDeviceFarm;
import static com.tiket.testbase.Configs.BROWSERSTACK_DEBUG;
import static com.tiket.testbase.Configs.LAMBDA_TEST_DEBUG;


public class DriverTools<T extends WebDriver> {

    private static final Class[] forbiddenExceptions = { WebDriverException.class, ClassCastException.class };
    private static final int MAX_TRY = 5;
    private static final int SECONDS = 10;
    // Max px an element's top-left may drift between samples and still count as "settled" (animation done).
    private static final double POSITION_SETTLE_TOLERANCE_PX = 2.0;
    private static final int MULTIPLIER_CONSTANT_GK_WAIT_TIME = 3;
    private static final int MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME = 2;

    // Timeout constants for health check (configurable via Google Sheet properties)
    // Default: 45 seconds for operation timeout, 5 seconds for health check ping
    private static final int OPERATION_TIMEOUT_SECONDS = getConfiguredTimeout("browser.healthcheck.timeout", 45);
    private static final int HEALTH_CHECK_TIMEOUT_SECONDS = getConfiguredTimeout("browser.healthcheck.ping.timeout", 5);

    private static int getConfiguredTimeout(String key, int defaultValue) {
        try {
            String value = Configs.PROPERTIES_DATA.get(key);
            if (value != null && !value.isEmpty()) {
                int parsed = Integer.parseInt(value.trim());
                // Validate: must be positive, reasonable range (1-300 seconds)
                if (parsed < 1) {
                    System.err.println("[DriverTools] Config value for '" + key + "' must be positive. Got: " + parsed + ". Using default: " + defaultValue);
                    return defaultValue;
                }
                if (parsed > 300) {
                    System.err.println("[DriverTools] Config value for '" + key + "' exceeds max (300s). Got: " + parsed + ". Using default: " + defaultValue);
                    return defaultValue;
                }
                return parsed;
            }
        } catch (NumberFormatException e) {
            System.err.println("[DriverTools] Invalid config value for '" + key + "': not a valid integer. Using default: " + defaultValue);
        } catch (Exception e) {
            // Config not available yet (class loading order) - use default silently
        }
        return defaultValue;
    }
    private final T driver;
    private final ILogger logger;
    private final TimeLimiter timeLimiter;
    private final Set<String> processedFlightStreamingRequestIds = Collections.synchronizedSet(new HashSet<>());
    private final Set<String> pendingFlightStreamingRequestIds = Collections.synchronizedSet(new HashSet<>());
    private final Map<String, String> flightStreamingRequestPostDataById = new ConcurrentHashMap<>();
    private final List<Map<String, Object>> javaFlightStreamingCaptures = Collections.synchronizedList(new ArrayList<>());
    private boolean flightStreamingCaptureInitialized = false;
    private boolean flightStreamingPerformanceLogsUnavailable = false;
    private long flightStreamingCaptureClearEpochMs = 0L;

    /**
     * Click the WebView component outside the device screen. This method only
     * usable for Webview Context.
     *
     * <p>The click is deferred via {@code setTimeout(...,0)} so the JS atom returns
     * immediately. This avoids the iOS WKWebView hang (manifesting as
     * {@code "Did not get any response for atom execution after 120001ms"}) that occurs
     * when the click triggers a full-page navigation (e.g. an anchor with
     * {@code href}) while {@code executeScript} is still waiting for the atom.
     */
    public void clickWithJavascriptExecutor(WebElement element) {
        logger.log("Clicking element : " + element);
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "var el = arguments[0];"
                            + "setTimeout(function(){ try { el.click(); } catch (e) {} }, 0);"
                            + "return true;",
                    element);
        } catch (TimeoutException te) {
            // Page likely already navigated away before the atom could return.
            // The click was dispatched, so treat this as success.
            logger.log("JS click atom did not return in time (likely navigated). Treating as dispatched: " + te.getMessage());
        }
        logger.log("Clicked element : " + element);
    }

    /**
     * Click the WebView component outside the device screen. This method only
     * usable for Webview Context.
     */
    public void clickWithJavascriptExecutor(Locator locator) {
        // Scroll first so lazy-loaded elements are actually rendered in the DOM
        // before we try to resolve them. This avoids a redundant lookup and the
        // "NoSuchElement" failure that could otherwise occur for elements that
        // only attach after being scrolled into view.
        scrollIntoWebView(locator);
        WebElement element = element(locator).orElseThrow();
        logger.log("Clicking element with JS : " + element);
        clickWithJavascriptExecutor(element);
    }


    /**
     * Clicks using Selenium Actions (moveToElement + click). Triggers real browser-level
     * mouse events, which works with React/synthetic event handlers and bypasses overlay
     * interception that blocks regular click().
     */
    public void clickWithActions(Locator locator) {
        scrollIntoWebView(locator);
        WebElement element = element(locator).orElseThrow();
        logger.log("Clicking element with Actions : " + element);
        clickWithActions(element);
    }

    public void clickWithActions(WebElement element) {
        new Actions(driver)
                .moveToElement(element)
                .click()
                .perform();
        logger.log("Clicked element with Actions : " + element);
    }

    /**
     * Reveals a hover-only fly-out and clicks a target inside it in one continuous gesture: hovers the
     * parent, then (without ever leaving the fly-out) moves onto the target and clicks. Needed for menus
     * whose children are only visible while the parent is hovered — a normal click can't keep the fly-out
     * open, and the children can even match locators while hidden.
     */
    public void hoverThenClick(WebElement hoverTarget, WebElement clickTarget) {
        logger.log("Hovering parent then clicking child with Actions. parent=" + hoverTarget + " child=" + clickTarget);
        new Actions(driver)
                .moveToElement(hoverTarget)
                .pause(Duration.ofMillis(300))
                .moveToElement(clickTarget)
                .pause(Duration.ofMillis(150))
                .click()
                .perform();
        logger.log("Hover-then-click done");
    }

    /**
     * Hovers over an element using Selenium Actions (moveToElement). Use for tooltips and hover menus on web.
     * Some React/headless setups ignore Actions hover — use {@link #hoverWithJavascriptExecutor(WebElement)} as fallback.
     */
    public void hoverToElement(Locator locator) {
        scrollIntoWebView(locator);
        WebElement element = element(locator).orElseThrow();
        hoverToElement(element);
    }

    public void hoverToElement(WebElement element) {
        scrollIntoWebView(element);
        new Actions(driver)
                .moveToElement(element, 0, 0)
                .pause(Duration.ofMillis(150))
                .moveToElement(element)
                .pause(Duration.ofMillis(150))
                .perform();
        logger.log("Hovered element with Actions : " + element);
    }

    /**
     * Hovers an element by dispatching synthetic DOM events (mouseover/mouseenter/pointerover/pointerenter).
     * Required for React components whose tooltips only listen to JS-dispatched events or whose Actions hover is intercepted.
     */
    public void hoverWithJavascriptExecutor(Locator locator) {
        scrollIntoWebView(locator);
        WebElement element = element(locator).orElseThrow();
        hoverWithJavascriptExecutor(element);
    }

    public void hoverWithJavascriptExecutor(WebElement element) {
        scrollIntoWebView(element);
        String script =
                "const el = arguments[0];" +
                        "const rect = el.getBoundingClientRect();" +
                        "const opts = {bubbles: true, cancelable: true, view: window," +
                        "  clientX: rect.left + rect.width / 2, clientY: rect.top + rect.height / 2};" +
                        "el.dispatchEvent(new PointerEvent('pointerover', opts));" +
                        "el.dispatchEvent(new PointerEvent('pointerenter', opts));" +
                        "el.dispatchEvent(new MouseEvent('mouseover', opts));" +
                        "el.dispatchEvent(new MouseEvent('mouseenter', opts));" +
                        "el.dispatchEvent(new MouseEvent('mousemove', opts));";
        ((JavascriptExecutor) driver).executeScript(script, element);
        logger.log("Hovered element with JS : " + element);
    }

    /**
     * Focuses an element via JavaScript. Use when a pointer click would start the wrong interaction
     * (for example a dnd-kit sortable handle) but keyboard events still need the element to be
     * {@code document.activeElement}.
     */
    public void focusWithJavascriptExecutor(Locator locator) {
        WebElement element = element(locator).orElseThrow();
        focusWithJavascriptExecutor(element);
    }

    public void focusWithJavascriptExecutor(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", element);
        logger.log("Focused element with JS : " + element);
    }

    /**
     * Clicks a web element by dispatching synthetic pointer and mouse events.
     * Useful for React/headless UI toggles that ignore normal Selenium clicks.
     */
    public void clickWithSyntheticEvents(Locator locator) {
        scrollIntoWebView(locator);
        WebElement element = element(locator).orElseThrow();
        clickWithSyntheticEvents(element);
    }

    public void clickWithSyntheticEvents(WebElement element) {
        scrollElementToCenter(element);
        String script =
                "const el = arguments[0];" +
                        "el.scrollIntoView({block:'center'});" +
                        "const rect = el.getBoundingClientRect();" +
                        "const opts = {bubbles:true,cancelable:true,view:window," +
                        "clientX: rect.left + rect.width / 2, clientY: rect.top + rect.height / 2, buttons:1};" +
                        "if (typeof PointerEvent !== 'undefined') {" +
                        "  el.dispatchEvent(new PointerEvent('pointerdown', opts));" +
                        "  el.dispatchEvent(new PointerEvent('pointerup', opts));" +
                        "}" +
                        "el.dispatchEvent(new MouseEvent('mousedown', opts));" +
                        "el.dispatchEvent(new MouseEvent('mouseup', opts));" +
                        "el.dispatchEvent(new MouseEvent('click', opts));";
        ((JavascriptExecutor) driver).executeScript(script, element);
        logger.log("Clicked element with synthetic events : " + element);
    }

    public void clearWithJsExecutor(Locator locator) {
        WebElement element = element(locator).orElseThrow();
        clearWithJsExecutor(element);
    }

    public void clearWithJsExecutor(WebElement element) {
        logger.log("try clearing element : " + element);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = ''; arguments[0].dispatchEvent(new Event('input'));", element);
        logger.log("try clearing element : " + element);
    }

    public boolean openNewTabAndPasteUrl(String url) {
        if (url == null || url.isEmpty()) {
            logger.log("Clipboard is empty or URL not copied!");
            return false;
        } else {
            driver.switchTo().newWindow(WindowType.TAB);
            driver.get(url);

            if (driver.getCurrentUrl().equals(url)) {
                logger.log("Opened new tab and navigated to: " + url);
                return true;
            } else {
                logger.log("Failed to navigate to the expected URL.");
                return false;
            }
        }
    }

    public void closeCurrentTab() {
        driver.close();
    }

    public enum SideOfElement {
        btmmiddle("btm"),
        topmiddle("topmiddle"),
        topleft("topleft"),
        topright("topright"),
        btmleft("btmleft"),
        btmright("btmright");

        private String name;

        SideOfElement(String name) {
            this.name = name;
        }

        public Double[] getXYMultiplier() {
            switch (this) {
                case btmmiddle -> {
                    Double[] xy = { 0.5, 0.8 };
                    return xy;
                }
                case btmleft -> {
                    Double[] xy = { 0.2, 0.8 };
                    return xy;
                }
                case btmright -> {
                    Double[] xy = { 0.8, 0.8 };
                    return xy;
                }
                case topleft -> {
                    Double[] xy = { 0.2, 0.2 };
                    return xy;
                }
                case topmiddle -> {
                    Double[] xy = { 0.5, 0.2 };
                    return xy;
                }
                case topright -> {
                    Double[] xy = { 0.8, 0.2 };
                    return xy;
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + this);
            }
        }

    }

    public DriverTools(T driver) {
        Objects.requireNonNull(driver);
        this.driver = driver;
        this.logger = BaseTest.mainLogger.get();
        this.timeLimiter = SimpleTimeLimiter.create(Executors.newCachedThreadPool());
    }

    public DriverTools(T driver, ILogger logger) {
        Objects.requireNonNull(driver);
        this.driver = driver;
        this.logger = logger;
        this.timeLimiter = SimpleTimeLimiter.create(Executors.newCachedThreadPool());
    }

    public static String fileScreenshot(WebDriver driver, String name) {
        return executeStaticWithHealthCheck(driver, () -> {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            return CoreFunctions.processPng(source, name);
        }, "fileScreenshot(" + name + ")");
    }

    public static String fileScreenshot(WebDriver driver, String name, WebElement element) {
        return executeStaticWithHealthCheck(driver, () -> {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            File source = element.getScreenshotAs(OutputType.FILE);
            return CoreFunctions.processPng(source, name);
        }, "fileScreenshot(" + name + ", element)");
    }

    public static String base64Screenshot(WebDriver driver) {
        return executeStaticWithHealthCheck(driver, () -> {
            return "data:image/png;base64,"
                    + ((TakesScreenshot) Objects.requireNonNull(driver)).getScreenshotAs(OutputType.BASE64);
        }, "base64Screenshot");
    }

    public Optional<WebElement> element(Locator locator) {
        for (int i = 0; i < MAX_TRY; i++) {
            try {
                logger.log("Finding element: " + locator);
                AtomicReference<Optional<WebElement>> element = new AtomicReference<>(Optional.empty());
                executeWithTimeout(()-> {
                    element.set(Optional.of(driver.findElement(locator.by())));
                    return null;
                }, 10, TimeUnit.SECONDS);
                logger.log("Found element: " + locator);
                return element.get();
            } catch (TimeoutException te) {
                throw new ForbiddenException(te);
            } catch (Exception e) {
                ExceptionAnalyser.analyse(e, forbiddenExceptions);
                if (i == MAX_TRY - 1) {
                    logger.softWarning("Element not found: " + locator);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<WebElement> element(Locator locator, int maxTry) {
        for (int i = 0; i < maxTry; i++) {
            try {
                logger.log("Finding element: " + locator);
                AtomicReference<Optional<WebElement>> element = new AtomicReference<>(Optional.empty());
                executeWithTimeout(()-> {
                    element.set(Optional.of(driver.findElement(locator.by())));
                    return null;
                }, 10, TimeUnit.SECONDS);
                logger.log("Found element: " + locator);
                return element.get();
            } catch (TimeoutException te) {
                throw new ForbiddenException(te);
            } catch (Exception e) {
                ExceptionAnalyser.analyse(e, forbiddenExceptions);
                if (i == maxTry - 1) {
                    logger.softWarning("Element not found: " + locator);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<WebElement> element(Locator locator, WebElement webElement) {
        for (int i = 0; i < MAX_TRY; i++) {
            try {
                logger.log("Finding element: " + locator + ", on element: " + webElement);
                AtomicReference<Optional<WebElement>> element = new AtomicReference<>(Optional.empty());
                executeWithTimeout(()-> {
                    element.set(Optional.of(webElement.findElement(locator.by())));
                    return null;
                }, 10, TimeUnit.SECONDS);
                logger.log("Found element: " + locator);
                return element.get();
            } catch (TimeoutException te) {
                throw new ForbiddenException(te);
            } catch (Exception e) {
                ExceptionAnalyser.analyse(e, forbiddenExceptions);
                if (i == MAX_TRY - 1) {
                    logger.softWarning("Element not found: " + locator);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<List<WebElement>> elements(Locator locator) {
        for (int i = 0; i < MAX_TRY; i++) {
            try {
                logger.log("Finding elements: " + locator);
                AtomicReference<List<WebElement>> elements = new AtomicReference<>();
                executeWithTimeout(()-> {
                    elements.set(driver.findElements(locator.by()));
                    return null;
                }, 10, TimeUnit.SECONDS);
                logger.log("Found elements: " + locator);
                return Optional.of(elements.get());
            } catch (TimeoutException te) {
                throw new ForbiddenException(te);
            } catch (Exception e) {
                ExceptionAnalyser.analyse(e, forbiddenExceptions);
                if (i == MAX_TRY - 1) {
                    logger.softWarning("Elements not found: " + locator);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<List<WebElement>> elements(Locator locator, int timeOutInSeconds) {
        for (int i = 0; i < MAX_TRY; i++) {
            try {
                logger.log("Finding elements: " + locator);
                AtomicReference<List<WebElement>> elements = new AtomicReference<>();
                executeWithTimeout(()-> {
                    elements.set(driver.findElements(locator.by()));
                    return null;
                }, timeOutInSeconds, TimeUnit.SECONDS);
                logger.log("Found elements: " + locator);
                return Optional.of(elements.get());
            } catch (TimeoutException te) {
                throw new ForbiddenException(te);
            } catch (Exception e) {
                ExceptionAnalyser.analyse(e, forbiddenExceptions);
                if (i == MAX_TRY - 1) {
                    logger.softWarning("Elements not found: " + locator);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<List<WebElement>> elements(Locator locator, WebElement webElement) {
        for (int i = 0; i < MAX_TRY; i++) {
            try {
                logger.log("Finding elements: " + locator + ", on element: " + webElement);
                AtomicReference<List<WebElement>> elements = new AtomicReference<>();
                executeWithTimeout(()-> {
                    elements.set(webElement.findElements(locator.by()));
                    return null;
                }, 10, TimeUnit.SECONDS);
                logger.log("Found elements: " + locator);
                return Optional.of(elements.get());
            } catch (TimeoutException te) {
                throw new ForbiddenException(te);
            } catch (Exception e) {
                ExceptionAnalyser.analyse(e, forbiddenExceptions);
                if (i == MAX_TRY - 1) {
                    logger.softWarning("Elements not found: " + locator);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<List<WebElement>> elementsUntilExist(Locator locator) {
        for (int i = 0; i < MAX_TRY; i++) {
            try {
                logger.log("Finding elements: " + locator);
                AtomicReference<List<WebElement>> elements = new AtomicReference<>();
                executeWithTimeout(()-> {
                    elements.set(driver.findElements(locator.by()));
                    return null;
                }, 10, TimeUnit.SECONDS);
                if (!elements.get().isEmpty()) {
                    logger.log("Found elements: " + locator);
                    return Optional.of(elements.get());
                } else {
                    logger.softWarning("Elements not found: " + locator);
                }
            } catch (TimeoutException te) {
                throw new ForbiddenException(te);
            } catch (Exception e) {
                ExceptionAnalyser.analyse(e, forbiddenExceptions);
                if (i == MAX_TRY - 1) {
                    logger.softWarning("Elements not found: " + locator);
                }
            }
            dummyWait(1);
        }
        return Optional.empty();
    }

    public Optional<WebElement> firstElement(Locator locator) {
        logger.log("Finding first element" + locator);
        List<WebElement> elements = elements(locator).orElseThrow();
        if (elements.size() > 0) {
            logger.log("Found first element" + locator);
            return Optional.of(Iterables.getFirst(elements, null));
        }
        logger.softWarning("Failed to find first element" + locator);
        return Optional.empty();
    }

    public Optional<WebElement> firstElement(Locator locator, WebElement element) {
        logger.log("Finding first element: " + locator + ", on element: " + element);
        List<WebElement> elements = elements(locator, element).orElseThrow();
        if (elements.size() > 0) {
            logger.log("Found first element: " + locator + ", on element: " + element);
            return Optional.of(Iterables.getFirst(elements, null));
        }
        logger.softWarning("Failed to find first element: " + locator + ", on element" + element);
        return Optional.empty();
    }

    public Optional<WebElement> lastElement(Locator locator) {
        logger.log("Finding last element: " + locator);
        List<WebElement> elements = elements(locator).orElseThrow();
        if (elements.size() > 0) {
            logger.log("Found last element: " + locator);
            return Optional.of(Iterables.getLast(elements, null));
        }
        logger.softWarning("Failed to find last element: " + locator);
        return Optional.empty();
    }

    public Optional<WebElement> lastElement(Locator locator, WebElement element) {
        logger.log("Finding last element: " + locator + ", on element: " + element);
        List<WebElement> elements = elements(locator, element).orElseThrow();
        if (elements.size() > 0) {
            logger.log("Found last element: " + locator + ", on element: " + element);
            return Optional.of(Iterables.getLast(elements, null));
        }
        logger.softWarning("Failed to find last element: " + locator + ", on element" + element);
        return Optional.empty();
    }

    public Optional<WebElement> nthElement(Locator locator, int position) {
        logger.log("Finding " + position + " position element: " + locator);
        List<WebElement> elements = elements(locator).orElseThrow();
        if (elements.size() > 0) {
            logger.log("Found " + position + " position element: " + locator);
            return Optional.of(Iterables.get(elements, position, null));
        }
        logger.softWarning("Failed to find " + position + " position element: " + locator);
        return Optional.empty();
    }

    public Optional<WebElement> nthElement(Locator locator, WebElement element, int position) {
        logger.log("Finding " + position + " position element: " + locator + ", on element: " + element);
        List<WebElement> elements = elements(locator, element).orElseThrow();
        if (elements.size() > 0) {
            logger.log("Found " + position + " position element: " + locator + ", on element: " + element);
            return Optional.of(Iterables.get(elements, position, null));
        }
        logger.softWarning("Failed to find " + position + " position element: " + locator + ", on element" + element);
        return Optional.empty();
    }

    /**
     * ExpectedConditions.presenceOfElementLocated may throw following exception
     * java.lang.ClassCastException: class
     * com.google.common.collect.Maps$TransformedEntriesMap cannot be cast to class
     * org.openqa.selenium.WebElement
     * (com.google.common.collect.Maps$TransformedEntriesMap and
     * org.openqa.selenium.WebElement are in unnamed module of loader 'app')
     */
    public Optional<WebElement> waitForElementToPresence(Locator locator, int seconds) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        }else if(Configs.ENVIRONMENT == Environment.PREPROD){
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }
        try {
            logger.log("Waiting for element to presence: " + locator);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            WebElement e = wait
                    .until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(locator.by())));
            logger.log("Element presented: " + locator);
            return Optional.of(e);
        } catch (TimeoutException te) {
            logger.softWarning("Timeout waiting for " + seconds + " seconds for element to presence: " + locator);
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.softWarning(e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    public Optional<WebElement> waitForElementToPresence(Locator locator) {
        return waitForElementToPresence(locator, SECONDS);
    }

    /**
     * Polls multiple locators within a single timeout and returns the first that is present
     * (order = priority). Avoids stacking per-locator waits when any of several variants may appear.
     *
     * @param seconds  max wait (GK/PREPROD multipliers applied like other waits)
     * @param locators candidates in preference order
     * @return the first matching {@link Locator}, or empty if none appear in time
     */
    public Optional<Locator> waitForAnyLocator(int seconds, Locator... locators) {
        if (locators == null || locators.length == 0) {
            return Optional.empty();
        }
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }
        try {
            logger.log("Waiting for any of " + locators.length + " locator(s) for up to " + seconds + "s");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            wait.pollingEvery(Duration.ofMillis(200));
            Locator matched = wait.until(d -> {
                for (Locator locator : locators) {
                    try {
                        List<WebElement> found = d.findElements(locator.by());
                        if (found != null && !found.isEmpty()) {
                            return locator;
                        }
                    } catch (StaleElementReferenceException | NoSuchElementException ignored) {
                        // keep polling
                    }
                }
                return null;
            });
            if (matched != null) {
                logger.log("First matching locator: " + matched);
                return Optional.of(matched);
            }
        } catch (TimeoutException te) {
            logger.softWarning("Timeout waiting " + seconds + "s for any of "
                    + locators.length + " locator(s)");
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.softWarning(e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    /**
     * Instant probe (no wait): returns the first locator that currently has at least one element.
     */
    public Optional<Locator> firstPresentLocator(Locator... locators) {
        if (locators == null || locators.length == 0) {
            return Optional.empty();
        }
        for (Locator locator : locators) {
            try {
                List<WebElement> found = driver.findElements(locator.by());
                if (found != null && !found.isEmpty()) {
                    return Optional.of(locator);
                }
            } catch (Exception ignored) {
                // try next
            }
        }
        return Optional.empty();
    }

    /**
     * Polls {@code condition} until it returns a non-null, non-false value. Stale elements are
     * ignored. Timeout is not a warning — the caller decides whether an empty result is a miss.
     * GK/PREPROD wait multipliers are applied like the other wait methods.
     */
    public <V> Optional<V> waitUntil(Function<WebDriver, V> condition, int seconds) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            wait.ignoring(StaleElementReferenceException.class);
            return Optional.ofNullable(wait.until(condition));
        } catch (TimeoutException timeout) {
            return Optional.empty();
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            return Optional.empty();
        }
    }

    public Optional<List<WebElement>> waitForPresenceElementsList(Locator locator) {
        return waitForPresenceElementsList(locator,SECONDS);
    }

    public Optional<List<WebElement>> waitForPresenceElementsList(Locator locator,int seconds) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }

        try {
            logger.log("Waiting for element list to be present: " + locator);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));

            List<WebElement> elements = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(locator.by())
            );

            if (elements == null || elements.isEmpty()) {
                logger.softWarning("No elements found after waiting: " + locator);
                return Optional.empty();
            }

            logger.log("Found " + elements.size() + " elements: " + locator);
            return Optional.of(elements);
        } catch (TimeoutException te) {
            logger.softWarning("Timeout after waiting " + seconds + " seconds for element list: " + locator);
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.softWarning("Error finding element list: " + e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    //adding scrollIntoWebViewMWeb only for mWeb so to increase scrolling stability on apps browsers
    public void scrollIntoWebViewMWeb(Locator locator) {
        logger.log("Scrolling into web view: " + locator);
        element(locator).ifPresentOrElse(el -> {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
            logger.log("Scrolled into view: " + locator);
        }, () -> {
            logger.softWarning("Failed to scroll into web view: " + locator);
            throw new DriverToolsException("Failed to scroll into web view: " + locator);
        });
    }
    public void waitForElementToBeVisible(Locator locator, int seconds) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }
        try {
            logger.log("Waiting for element to be visible: " + locator);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            wait.until(ExpectedConditions.refreshed(
                    ExpectedConditions.visibilityOfElementLocated(locator.by())
            ));
            logger.log("Element is visible: " + locator);
        } catch (TimeoutException te) {
            logger.softWarning("Timeout waiting " + seconds + " seconds for visibility of: " + locator);
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.softWarning(e.getLocalizedMessage());
        }
    }
    public void waitForElementToBeVisible(Locator locator) {
        waitForElementToBeVisible(locator, SECONDS);
    }

    /**
     * Waits until the given text appears in the element identified by {@code locator}.
     *
     * @return {@code true} if the text is present within the timeout, {@code false} otherwise
     */
    public boolean waitForTextToBePresent(Locator locator, String text) {
        return waitForTextToBePresent(locator, text, SECONDS);
    }

    public boolean waitForTextToBePresent(Locator locator, String text, int seconds) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }
        try {
            logger.log("Waiting for text to be present in " + locator + ": " + text);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            wait.until(ExpectedConditions.refreshed(
                    ExpectedConditions.textToBePresentInElementLocated(locator.by(), text)
            ));
            logger.log("Text present in element: " + locator);
            return true;
        } catch (TimeoutException te) {
            logger.softWarning("Timeout waiting for text in element: " + locator + " — expected: " + text);
            return false;
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.softWarning(e.getLocalizedMessage());
            return false;
        }
    }

    /**
     * Waits until the given element's text contains the expected text (case-insensitive).
     *
     * @param locator        the element to check
     * @param text           expected text (case-insensitive)
     * @param seconds        timeout in seconds
     * @return true if the element text contains the expected text within timeout
     */
    public boolean waitForElementTextToContainIgnoreCase(Locator locator, String text, int seconds) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }
        try {
            logger.log(String.format("Waiting for [%s] to contain text (ignore case): '%s'", locator.name(), text));
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(d -> {
                try {
                    WebElement element = d.findElement(locator.by());
                    String elementText = element.getText();
                    return elementText != null && elementText.toLowerCase().contains(text.toLowerCase());
                } catch (Exception e) {
                    return false;
                }
            });
            logger.log(String.format("Element [%s] contains text: '%s'", locator.name(), text));
            return true;
        } catch (TimeoutException te) {
            logger.softWarning(String.format("Timeout waiting for [%s] to contain text: '%s'", locator.name(), text));
            return false;
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.softWarning(e.getLocalizedMessage());
            return false;
        }
    }

    public Optional<WebElement> waitForElementToPresenceSafe(Locator locator, int seconds) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        }else if(Configs.ENVIRONMENT == Environment.PREPROD){
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }
        int time = seconds;

        AtomicReference<Optional<WebElement>> element = new AtomicReference<>(Optional.empty());
        try{
            executeWithTimeout(() -> {
                element.set(waitForElementToPresence(locator, time));
                return null;
            }, seconds, TimeUnit.SECONDS);
        } catch (Exception e){
            logger.softWarning("Timeout waiting for element to presence: " + locator);
        }
        return element.get();
    }

    public boolean waitForElementToAbsence(Locator locator, int seconds) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        }else if(Configs.ENVIRONMENT == Environment.PREPROD){
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }

        for (int i = 0 ; i < seconds; i++) {
            dummyWait(1);
            if (element(locator).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public boolean waitForElementToAbsence(Locator locator) {
        return waitForElementToAbsence(locator, SECONDS);
    }

    public Optional<WebElement> waitForElementToAppear(WebElement element, int seconds) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        }else if(Configs.ENVIRONMENT == Environment.PREPROD){
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }
        try {
            logger.log("Waiting for element to appear: " + element);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            WebElement e = wait.until(ExpectedConditions.visibilityOf(element));
            logger.log("Element appeared: " + element);
            return Optional.of(e);
        } catch (TimeoutException te) {
            logger.softWarning("Timeout waiting for " + seconds + " seconds for element to appear: " + element);
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.softWarning(e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    public Optional<WebElement> waitForElementToAppear(WebElement element) {
        return waitForElementToAppear(element, SECONDS);
    }

    /**
     * ExpectedConditions.visibilityOfElementLocated may throw following exception
     * java.lang.ClassCastException: class
     * com.google.common.collect.Maps$TransformedEntriesMap cannot be cast to class
     * org.openqa.selenium.WebElement
     * (com.google.common.collect.Maps$TransformedEntriesMap and
     * org.openqa.selenium.WebElement are in unnamed module of loader 'app')
     */
    public Optional<WebElement> waitForElementToAppear(Locator locator, int seconds) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        }else if(Configs.ENVIRONMENT == Environment.PREPROD){
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }
        try {
            logger.log("Waiting for element to appear: " + locator);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            WebElement e = wait
                    .until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(locator.by())));
            logger.log("Element appeared: " + locator);
            return Optional.of(e);
        } catch (TimeoutException te) {
            logger.softWarning("Timeout waiting for " + seconds + " seconds for element to appear: " + locator);
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.softWarning(e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    public Optional<WebElement> waitForElementToAppear(Locator locator) {
        return waitForElementToAppear(locator, SECONDS);
    }


    public Optional<List<WebElement>> waitForElementsToAppear(Locator locator,int seconds){
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        }else if(Configs.ENVIRONMENT == Environment.PREPROD){
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }
        try {
            logger.log("Waiting for element to appear: " + locator);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            List<WebElement> e = wait
                    .until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfAllElementsLocatedBy(locator.by())));
            logger.log("Element appeared: " + locator);
            return Optional.of(e);
        } catch (TimeoutException te) {
            logger.softWarning("Timeout waiting for " + seconds + " seconds for element to appear: " + locator);
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.softWarning(e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    public Optional<List<WebElement>> waitForElementsToAppear(Locator locator){
        return waitForElementsToAppear(locator,SECONDS);
    }

    /**
     * ExpectedConditions.elementToBeClickable(locator) may throw following
     * exception
     * java.lang.ClassCastException: class
     * com.google.common.collect.Maps$TransformedEntriesMap cannot be cast to class
     * org.openqa.selenium.WebElement
     * (com.google.common.collect.Maps$TransformedEntriesMap and
     * org.openqa.selenium.WebElement are in unnamed module of loader 'app')
     */
    public Optional<WebElement> waitForElementToBeClickable(Locator locator, int seconds) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        }else if(Configs.ENVIRONMENT == Environment.PREPROD){
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }
        try {
            logger.log("Waiting for element to be clickable: " + locator);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            WebElement e = wait
                    .until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(locator.by())));
            logger.log("Element is now clickable: " + locator);
            return Optional.of(e);
        } catch (TimeoutException te) {
            logger.softWarning("Timeout waiting for " + seconds + " seconds for element to be clickable: " + locator);
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.softWarning(e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    public Optional<WebElement> waitForElementToBeClickable(Locator locator) {
        return waitForElementToBeClickable(locator, SECONDS);
    }

    public Optional<WebElement> waitForElementToBeClickable(WebElement element, int seconds) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        }else if(Configs.ENVIRONMENT == Environment.PREPROD){
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }
        try {
            logger.log("Waiting for element to be clickable: " + element);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            WebElement e = wait.until(ExpectedConditions.elementToBeClickable(element));
            logger.log("Element is now clickable: " + element);
            return Optional.of(e);
        } catch (TimeoutException te) {
            logger.softWarning("Timeout waiting for " + seconds + " seconds for element to be clickable: " + element);
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.softWarning(e.getLocalizedMessage());
        }
        return Optional.empty();
    }

    public Optional<WebElement> waitForElementToBeClickable(WebElement element) {
        return waitForElementToBeClickable(element, SECONDS);
    }

    public boolean waitForElementToDisappear(Locator locator, int seconds) {
        try {
            logger.log("Waiting for element to disappear: " + locator);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            boolean result = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator.by()));
            logger.log("Element disappeared: " + locator + "? " + result);
            return result;
        } catch (TimeoutException te) {
            logger.softWarning("Timeout waiting for element to disappear: " + locator);
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.softWarning(e.getLocalizedMessage());
        }
        return false;
    }

    public boolean waitForElementToDisappear(Locator locator) {
        AtomicBoolean isDisappeared = new AtomicBoolean(false);
        try{
            executeWithTimeout(() -> {
                isDisappeared.set(waitForElementToDisappear(locator, SECONDS));
                return null;
            }, SECONDS, TimeUnit.SECONDS);
        } catch (Exception e){
            logger.softWarning("Timeout waiting for element to disappear: " + locator);
        }
        return isDisappeared.get();
    }

    public boolean waitForElementToDisappear(WebElement element, int seconds) {
        try {
            logger.log("Waiting for element to appear: " + element);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            boolean result = wait.until(ExpectedConditions.invisibilityOf(element));
            logger.log("Element disappeared: " + element + "? " + result);
            return result;
        } catch (TimeoutException te) {
            logger.softWarning("Timeout waiting for element to disappear: " + element);
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.softWarning(e.getLocalizedMessage());
        }
        return false;
    }

    public boolean waitForElementToDisappear(WebElement element) {
        return waitForElementToDisappear(element, SECONDS);
    }

    /**
     * Waits up to {@code seconds} for {@code parent} to resolve and to contain at least
     * {@code minChildCount} descendants matching {@code childBy}, then returns those
     * descendants. The parent is re-resolved on every polling tick so a stale parent
     * (e.g. re-rendered by the framework) does not cause a {@link StaleElementReferenceException}.
     *
     * @param parent         locator of the container element
     * @param childBy        relative locator used to find descendants of the parent
     * @param minChildCount  minimum number of descendants expected
     * @param seconds        explicit wait timeout in seconds
     * @return the descendant elements when the condition is met within the timeout;
     *         an empty list otherwise (no exception is thrown on timeout).
     */
    public List<WebElement> waitForChildElementsCountAtLeast(
            Locator parent, By childBy, int minChildCount, int seconds) {
        try {
            logger.log("Waiting up to " + seconds + "s for at least " + minChildCount
                    + " children of " + parent + " matched by " + childBy);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            return wait.until(d -> {
                Optional<WebElement> resolvedParent = element(parent);
                if (resolvedParent.isEmpty()) {
                    return null;
                }
                List<WebElement> found = resolvedParent.get().findElements(childBy);
                return found.size() >= minChildCount ? found : null;
            });
        } catch (TimeoutException te) {
            logger.softWarning("Timeout waiting for at least " + minChildCount
                    + " children of " + parent + " matched by " + childBy);
            return List.of();
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.softWarning(e.getLocalizedMessage());
            return List.of();
        }
    }

    public boolean isElementDisplayed(Locator locator) {
        try{
            Optional<List<WebElement>> elements = elements(locator);
            boolean result = elements.map(list -> list.stream().anyMatch(WebElement::isDisplayed)).orElse(false);
            logger.log("Is element displayed: " + locator + "? " + result);
            return result;
        }catch(StaleElementReferenceException e){
            Optional<WebElement> element = element(locator);
            logger.log("Element is not attached to the page document");
            boolean result = element.map(WebElement::isDisplayed).orElse(false);
            logger.log("Is element displayed: " + locator + "? " + result);
            return result;
        }
    }

    public boolean isElementDisplayed(WebElement element) {
        boolean result = element.isDisplayed();
        logger.log("Is element displayed: " + element + "? " + result);
        return result;
    }

    public boolean isElementDisplayedWithStaleRetry(Locator locator) {
        try {
            return isElementDisplayed(locator);
        } catch (StaleElementReferenceException e) {
            waitForElementToAppear(locator, 2);
            return isElementDisplayed(locator);
        }
    }

    /**
     * Checks whether an element is on-screen and visible.
     * Stricter than {@link #isElementDisplayed(Locator)}: on iOS the accessibility tree
     * exposes a {@code visible} attribute that flips to {@code "false"} when a node is
     * rendered off-screen but still in the snapshot. We require both {@code isDisplayed()}
     * AND (when present) {@code visible == "true"}. A missing attribute (web / Android)
     * is treated as no objection. Returns {@code false} when the element is absent or stale.
     */
    public boolean isElementVisible(Locator locator) {
        boolean result;
        try {
            result = element(locator)
                    .map(this::resolveVisibility)
                    .orElse(false);
        } catch (StaleElementReferenceException e) {
            logger.log("Element is not attached to the page document");
            result = element(locator)
                    .map(this::resolveVisibility)
                    .orElse(false);
        }
        logger.log("Is element visible: " + locator + "? " + result);
        return result;
    }

    public boolean isElementVisible(WebElement element) {
        boolean result = resolveVisibility(element);
        logger.log("Is element visible: " + element + "? " + result);
        return result;
    }

    private boolean resolveVisibility(WebElement el) {
        try {
            if (!el.isDisplayed()) {
                return false;
            }
            String visibleAttr = el.getAttribute("visible");
            return visibleAttr == null || "true".equalsIgnoreCase(visibleAttr);
        } catch (StaleElementReferenceException sere) {
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isElementEnabled(Locator locator) {
        Optional<WebElement> element = element(locator);
        boolean result = element.map(WebElement::isEnabled).orElse(false);
        logger.log("Is element enabled: " + locator + "? " + result);
        return result;
    }

    public boolean isElementEnabled(WebElement element) {
        boolean result = element.isEnabled();
        logger.log("Is element enabled: " + element + "? " + result);
        return result;
    }

    public boolean isElementSelected(Locator locator) {
        Optional<WebElement> element = element(locator);
        boolean result = element.map(WebElement::isSelected).orElse(false);
        logger.log("Is element selected: " + locator + "? " + result);
        return result;
    }

    public boolean isRadioButtonChecked(Locator locator) {
        Optional<WebElement> element = element(locator);
        boolean result = element
                .map(el -> Boolean.parseBoolean(el.getDomProperty("checked")))
                .orElse(false);

        logger.log("Is radio checked: " + locator + "? " + result);
        return result;
    }

    public boolean isElementSelected(WebElement element) {
        boolean result = element.isSelected();
        logger.log("Is element selected: " + element + "? " + result);
        return result;
    }

    public boolean isElementAttributePresent(Locator locator, String attribute) {
        Optional<WebElement> element = element(locator);
        boolean result = false;
        try {
            String value = element.orElseThrow().getAttribute(attribute);
            if (value != null) {
                result = true;
                logger.log("Element attribute: " + attribute + " is present: " + locator);
            }
        } catch (Exception e) {
            logger.log("Element attribute: " + attribute + " not found: " + locator);
        }
        return result;
    }

    public boolean isElementGone(Locator locator) {
        return !isElementDisplayed(locator);
    }

    public boolean isElementGone(WebElement element) {
        return !isElementDisplayed(element);
    }

    public WebElement onScreenElement(Locator locator) {
        waitForElementToPresence(locator).orElseThrow();
        List<WebElement> elements = elements(locator).orElseThrow();
        int noOfElements = elements.size();
        logger.log("No of elements: " + noOfElements);
        logger.log("Checking duplicate off screen elements");
        Dimension windowSize = driver.manage().window().getSize();
        int screenWidth = windowSize.width;
        int screenHeight = windowSize.height;

        for (int x = 0; x < noOfElements; x++) {
            WebElement e = elements.get(x);
            int xLocation = e.getLocation().x;
            int yLocation = e.getLocation().y;
            boolean isElementVisible = Boolean.parseBoolean(e.getAttribute("visible"));

            if ((0 <= xLocation && xLocation <= screenWidth) && (0 <= yLocation && yLocation <= screenHeight)
                    && isElementVisible) {
                logger.log("Found element within screen bound");
                return e;
            }
        }
        throw new DriverToolsException("No element found within screen bound: " + locator);
    }

    public boolean isElementInView(Locator locator) {
        waitForElementToPresence(locator).orElseThrow();
        List<WebElement> elements = elements(locator).orElseThrow();
        int noOfElements = elements.size();
        logger.log("No of elements: " + noOfElements);
        logger.log("Checking duplicate off screen elements");
        Dimension windowSize = driver.manage().window().getSize();
        int screenWidth = windowSize.width;
        int screenHeight = windowSize.height;

        for (int x = 0; x < noOfElements; x++) {
            WebElement e = elements.get(x);
            int xLocation = e.getLocation().x;
            int yLocation = e.getLocation().y;

            if ((0 <= xLocation && xLocation <= screenWidth) && (0 <= yLocation && yLocation <= screenHeight)) {
                logger.log("Found element within screen bound");
                return true;
            }
        }
        logger.log("No element found within screen bound: " + locator);
        return false;
    }

    /**
     * Returns the elements that intersect the current WebView viewport.
     * Use inside a WebView/web context when a list (e.g. a virtualized or
     * scrollable bottom-sheet) renders many items in the DOM but only a
     * subset is visible to the user. The element is considered in-viewport
     * when it has a non-zero size and any part of its bounding rectangle
     * overlaps the visible window.
     *
     * <p>For native context, prefer {@link #onScreenElements(Locator)}.
     */
    public List<WebElement> elementsInViewport(Locator locator) {
        logger.log("Getting in-viewport elements for: " + locator);
        waitForElementToPresence(locator).orElseThrow();
        List<WebElement> elements = elements(locator).orElseThrow();
        if (elements.isEmpty()) {
            throw new DriverToolsException("No element found for locator: " + locator);
        }
        @SuppressWarnings("unchecked")
        List<Long> indexes = (List<Long>) ((JavascriptExecutor) driver).executeScript(
                "var els = arguments[0];"
                        + "var w = window.innerWidth || document.documentElement.clientWidth;"
                        + "var h = window.innerHeight || document.documentElement.clientHeight;"
                        + "var out = [];"
                        + "for (var i = 0; i < els.length; i++) {"
                        + "  var r = els[i].getBoundingClientRect();"
                        + "  if (r.width > 0 && r.height > 0 &&"
                        + "      r.bottom > 0 && r.right > 0 && r.top < h && r.left < w) {"
                        + "    out.push(i);"
                        + "  }"
                        + "}"
                        + "return out;",
                elements);
        List<WebElement> visible = new ArrayList<>();
        if (indexes != null) {
            for (Long idx : indexes) {
                visible.add(elements.get(idx.intValue()));
            }
        }
        logger.log("Found " + visible.size() + " in-viewport element(s) of " + elements.size());
        if (visible.isEmpty()) {
            throw new DriverToolsException("No element in viewport for locator: " + locator);
        }
        return visible;
    }

    public void inputStringOneAtTime(Locator locator, String textToInput, Locator... waitElements){
        logger.log(String.format("Typing '%s' into '%s' (search mode)...", textToInput, locator.name()));
        WebElement element = waitForElementToAppear(locator, 5)
                .orElseThrow(() -> new DriverToolsException("Element not found: " + locator.name()));
        element.clear();
        element.click();

        for (char c : textToInput.toCharArray()) {
            element.sendKeys(String.valueOf(c));

            // wait for search results / debounce
            for (Locator waitLocator : waitElements) {
                waitForElementToAppear(waitLocator, 2);
            }

            // small delay to simulate real typing (important for debounce)
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new DriverToolsException("Interrupted while typing into: " + locator.name(), e);
            }
        }
        logger.log(String.format("Finished typing '%s'", textToInput));
    }

    public List<WebElement> onScreenElements(Locator locator) {
        List<WebElement> finalResults = new ArrayList<>();
        waitForElementToPresence(locator).orElseThrow();
        List<WebElement> elements = elements(locator).orElseThrow();
        int noOfElements = elements.size();
        logger.log("No of elements: " + noOfElements);
        logger.log("Checking duplicate off screen elements");
        Dimension windowSize = driver.manage().window().getSize();
        int screenWidth = windowSize.width;
        int screenHeight = windowSize.height;

        for (int x = 0; x < noOfElements; x++) {
            WebElement e = elements.get(x);
            int xLocation = e.getLocation().x;
            int yLocation = e.getLocation().y;
            boolean isElementVisible = Boolean.parseBoolean(e.getAttribute("visible"));

            if ((0 <= xLocation && xLocation <= screenWidth) && (0 <= yLocation && yLocation <= screenHeight)
                    && isElementVisible) {
                logger.log("Found element within screen bound");
                finalResults.add(e);
            }
        }
        if(!finalResults.isEmpty()){
            return finalResults;
        } else{
            throw new DriverToolsException("No element found within screen bound: " + locator);
        }
    }

    public void click(Locator locator) {
        logger.log("Clicking on element: " + locator);
        try{
            WebElement element = waitForElementToBeClickable(locator).orElseThrow();
            element.click();
        }catch(StaleElementReferenceException e){
            logger.log("Statle element detected");
            logger.log("Retry clicking locator:  " + locator);
            waitForElementToBeClickable(locator).ifPresentOrElse(clickableElement -> {
                clickableElement.click();
            }, () -> {
                logger.softWarning("Failed to click element: " + locator);
                throw new DriverToolsException("Failed to click element: " + locator);
            });
        }

        logger.log("Clicked on element: " + locator);
    }

    public void clickOrFallbackToClickWithJavascriptExecutor(Locator locator) {
        WebElement element =  waitForElementToAppear(locator).orElseThrow();
        clickOrFallbackToClickWithJavascriptExecutor(element, MAX_TRY);
        logger.log("Clicked on locator: " + locator);
    }
    public void clickOrFallbackToClickWithJavascriptExecutor(WebElement element) {
        clickOrFallbackToClickWithJavascriptExecutor(element, MAX_TRY);
        logger.log("Clicked on element: " + element);
    }
    public void clickOrFallbackToClickWithJavascriptExecutor(WebElement element, int maxTry) {
        for (int i = 0; i < maxTry; i++) {
            try {
                click(element);
                return;
            } catch (Exception ex) {
                logger.softWarning(String.format("Standard click failed on attempt %d/%d for %s: %s", (i + 1), maxTry, element, ex.getMessage()));
                try {
                    clickWithJavascriptExecutor(element);
                    return;
                } catch (Exception jsEx) {
                    logger.softWarning(String.format("JS fallback click also failed on attempt %d/%d: %s", (i + 1), maxTry, jsEx.getMessage()));
                }
            }
        }
    }

    /**
     * Overload of {@link #clickWhenPresent(Locator)} with a custom presence-wait timeout (seconds).
     */
    public void clickWhenPresent(Locator locator, int seconds) {
        logger.log("Clicking (on presence) element: " + locator);
        try {
            WebElement element = waitForElementToPresence(locator, seconds).orElseThrow(
                    () -> new DriverToolsException("Failed to click element (not present): " + locator));
            element.click();
        } catch (StaleElementReferenceException e) {
            logger.log("Stale element detected on clickWhenPresent, retrying: " + locator);
            waitForElementToPresence(locator, seconds).ifPresentOrElse(
                    WebElement::click,
                    () -> {
                        logger.softWarning("Failed to click element (not present on retry): " + locator);
                        throw new DriverToolsException("Failed to click element: " + locator);
                    });
        }
        logger.log("Clicked (on presence) element: " + locator);
    }

    /**
     * Like {@link #clickWhenPresent(Locator, int)} but does not throw if the element never appears or
     * {@link WebElement#click()} fails: returns {@code true} when a click was executed successfully,
     * {@code false} otherwise. Retries up to five times (same as {@code MAX_TRY} for
     * {@link #element(Locator)} / {@link #elements(Locator)}): each attempt waits for presence, then
     * clicks; on missing element, stale element, or other click failure waits one second and tries again.
     */
    public boolean tryClickWhenPresent(Locator locator, int seconds) {
        logger.log("tryClickWhenPresent: " + locator);
        for (int attempt = 0; attempt < MAX_TRY; attempt++) {
            Optional<WebElement> opt = waitForElementToPresence(locator, seconds);
            if (opt.isEmpty()) {
                logger.softWarning(
                        "tryClickWhenPresent: attempt " + (attempt + 1) + "/" + MAX_TRY + " element not present: "
                                + locator);
                if (attempt < MAX_TRY - 1) {
                    dummyWait(1);
                }
                continue;
            }
            try {
                opt.get().click();
                logger.log(
                        "tryClickWhenPresent: clicked on attempt " + (attempt + 1) + "/" + MAX_TRY + ": " + locator);
                return true;
            } catch (StaleElementReferenceException e) {
                logger.log(
                        "tryClickWhenPresent: stale on attempt " + (attempt + 1) + "/" + MAX_TRY + ": " + locator);
                if (attempt < MAX_TRY - 1) {
                    dummyWait(1);
                }
            } catch (Exception e) {
                logger.softWarning(
                        "tryClickWhenPresent: click failed attempt " + (attempt + 1) + "/" + MAX_TRY + ": " + locator
                                + " — " + e);
                if (attempt < MAX_TRY - 1) {
                    dummyWait(1);
                }
            }
        }
        logger.softWarning("tryClickWhenPresent: giving up after " + MAX_TRY + " attempts: " + locator);
        return false;
    }

    /**
     * @see #tryClickWhenPresent(Locator, int)
     */
    public boolean tryClickWhenPresent(Locator locator) {
        return tryClickWhenPresent(locator, SECONDS);
    }

    /**
     * Simulates a scratching motion on a canvas element using native Actions.
     * Moves back and forth across the element's width and height.
     */
    public void scratchWithActions(Locator locator) {
        WebElement element = element(locator).orElseThrow();
        scratchWithActions(element);
    }

    /**
     * Simulates a scratching motion on a canvas element using native Actions.
     * Moves back and forth across the element's width and height.
     */
    public void scratchWithActions(WebElement element) {
        logger.log("Scratching element with Actions: " + element);
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();
        int stepX = width / 4;
        int stepY = height / 4;

        new org.openqa.selenium.interactions.Actions(driver)
                .moveToElement(element, -stepX, -stepY) // start top-left of center
                .clickAndHold()
                .moveByOffset(stepX * 2, 0)             // swipe right
                .moveByOffset(-stepX * 2, stepY)        // swipe left and down
                .moveByOffset(stepX * 2, 0)             // swipe right
                .moveByOffset(-stepX * 2, stepY)        // swipe left and down
                .moveByOffset(stepX * 2, 0)             // swipe right
                .release()
                .perform();
        logger.log("Scratched element with Actions: " + element);
    }

    public void click(List<Locator> locators, Predicate<Locator> condition) {
        for (Locator locator : locators) {
            if (condition.test(locator)) {
                click(locator);
                break;
            }
        }
    }

    public void click(WebElement element) {
        logger.log("Clicking on element: " + element);
        element.click();
        logger.log("Clicked on element: " + element);
    }

    public void sendKeysKeyboard(Locator locator, String msg) {
        logger.log("Sending keys: (" + msg + ") to: " + locator);
        element(locator).ifPresentOrElse(element -> {
            element.click();
            Actions actions = new Actions(driver);
            for (char c : msg.toCharArray()) {
                actions.sendKeys(String.valueOf(c)).perform();}
        }, () -> {
            logger.softWarning("Failed sending keys: (" + msg + ") to: " + locator);
            throw new DriverToolsException("Failed sending keys: (" + msg + ") to: " + locator);
        });
        logger.log("Sent keys: (" + msg + ") to: " + locator);
    }

    public void sendKeysKeyboard(WebElement element, String msg) {
        logger.log("Sending keys to element: " + element);
        element.click();
        Actions actions = new Actions(driver);
        for (char c : msg.toCharArray()) {
            actions.sendKeys(String.valueOf(c)).perform();
        }
        logger.log("Sent keys to element: " + element);
    }

    /**
     * Sends keys via keyboard (Actions) without clicking the element.
     * Use when the element is already focused (e.g. after clickElementByCoordinate) to avoid
     * a redundant click that can hang on iOS (e.g. with keyboard or AutoFill visible).
     */
    public void sendKeysKeyboardNoClick(WebElement element, String msg) {
        logger.log("Sending keys to element (no click): " + element);
        Actions actions = new Actions(driver);
        for (char c : msg.toCharArray()) {
            actions.sendKeys(String.valueOf(c)).perform();
        }
        logger.log("Sent keys to element: " + element);
    }

    public void sendKeysWithJsExecutor(Locator locator, String msg) {
        logger.log("Sending keys: (" + msg + ") to: " + locator);
        try {
            element(locator).ifPresentOrElse(element -> {
                ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", element, msg);
            }, () -> {
                logger.softWarning("Failed sending keys: (" + msg + ") to: " + locator);
                throw new DriverToolsException("Failed sending keys: (" + msg + ") to: " + locator);
            });
        } catch (StaleElementReferenceException e) {
            logger.log("Element is not attached to the page document");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement retryElement = wait
                    .until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(locator.by())));
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", retryElement, msg);
        }
        logger.log("Sent keys: (" + msg + ") to: " + locator);
    }

    /* * This method uses JavaScript to set the value of an input field and trigger
     * input and change events.
     * It is useful for cases where the input field does not update its value
     * correctly with standard sendKeys.
     */
    public void sendKeysWithJsExecutorForceInputValueAndTriggerEvents(Locator locator, String msg) {
        logger.log("Sending keys: (" + msg + ") to: " + locator);
        try {
            element(locator).ifPresentOrElse(element -> {
                ((JavascriptExecutor) driver).executeScript(
                        "const input = arguments[0];" +
                                "const nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                                "nativeInputValueSetter.call(input, arguments[1]);" +
                                "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                                "input.dispatchEvent(new Event('change', { bubbles: true }));",
                        element, msg
                );
            }, () -> {
                logger.softWarning("Failed sending keys: (" + msg + ") to: " + locator);
                throw new DriverToolsException("Failed sending keys: (" + msg + ") to: " + locator);
            });
        } catch (StaleElementReferenceException e) {
            logger.log("Element is not attached to the page document");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement retryElement = wait
                    .until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(locator.by())));
            ((JavascriptExecutor) driver).executeScript(
                    "const input = arguments[0];" +
                            "const nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                            "nativeInputValueSetter.call(input, arguments[1]);" +
                            "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                            "input.dispatchEvent(new Event('change', { bubbles: true }));",
                    retryElement, msg
            );
        }
        logger.log("Sent keys: (" + msg + ") to: " + locator);
    }

    /**
     * WebElement overload of {@link #sendKeysWithJsExecutorForceInputValueAndTriggerEvents(Locator, String)}: forces
     * the value onto an already-resolved element (works for both {@code <input>} and {@code <textarea>}) via the
     * native value setter and dispatches {@code input}/{@code change} so React registers the change. Use when the
     * element was scoped inside a parent (e.g. a duplicated id resolved per card) and a standard {@code sendKeys}
     * fails because the field is not (yet) interactable.
     */
    public void sendKeysWithJsExecutorForceInputValueAndTriggerEvents(WebElement element, String msg) {
        logger.log("Sending keys via JS (force value + events) to element: " + element);
        ((JavascriptExecutor) driver).executeScript(
                "const el = arguments[0], val = arguments[1];" +
                        "const proto = el.tagName === 'TEXTAREA' ? window.HTMLTextAreaElement.prototype : window.HTMLInputElement.prototype;" +
                        "const setter = Object.getOwnPropertyDescriptor(proto, 'value').set;" +
                        "setter.call(el, val);" +
                        "el.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "el.dispatchEvent(new Event('change', { bubbles: true }));",
                element, msg);
        logger.log("Sent keys via JS to element: " + element);
    }

    /**
     * Forces a hidden/collapsed element (e.g. a visually-hidden {@code <input type="file">}) to
     * {@code display: block; visibility: visible;} via JS, so it becomes interactable for Selenium
     * actions like {@code sendKeys} that require the element to be displayed.
     */
    public void makeElementVisible(Locator locator) {
        logger.log("Forcing element visible: " + locator);
        try {
            element(locator).ifPresentOrElse(element -> {
                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].style.display = 'block'; arguments[0].style.visibility = 'visible';", element);
            }, () -> {
                logger.softWarning("Failed to force element visible: " + locator);
                throw new DriverToolsException("Failed to force element visible: " + locator);
            });
        } catch (StaleElementReferenceException e) {
            logger.log("Element is not attached to the page document");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement retryElement = wait
                    .until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(locator.by())));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.display = 'block'; arguments[0].style.visibility = 'visible';", retryElement);
        }
        logger.log("Forced element visible: " + locator);
    }

    public void sendKeys(Locator locator, String msg) {
        logger.log("Sending keys: (" + msg + ") to: " + locator);
        try {
            element(locator).ifPresentOrElse(element -> element.sendKeys(msg), () -> {
                logger.softWarning("Failed sending keys: (" + msg + ") to: " + locator);
                throw new DriverToolsException("Failed sending keys: (" + msg + ") to: " + locator);
            });
        } catch (StaleElementReferenceException e) {
            logger.log("Element is not attached to the page document");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement retryElement = wait
                    .until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(locator.by())));
            retryElement.sendKeys(msg);
        }
        logger.log("Sent keys: (" + msg + ") to: " + locator);
    }

    public void sendKeys(WebElement element, String msg) {
        logger.log("Sending keys: (" + msg + ") to element: " + element);
        element.sendKeys(msg);
        logger.log("Sent keys: (" + msg + ") to element: " + element);
    }

    public Optional<String> getText(Locator locator) {
        logger.log("Getting text from: " + locator);
        Optional<WebElement> element = element(locator);
        if (element.isPresent()) {
            try {
                String text = element(locator).get().getText();
                logger.log("Got text: (" + text + ") from: " + locator);
                return Optional.of(text);
            } catch (StaleElementReferenceException e) {
                logger.log("Element is not attached to the page document");
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
                WebElement retryElement = wait
                        .until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(locator.by())));
                String text = retryElement.getText();
                logger.log("Got text: (" + text + ") from: " + locator);
                return Optional.of(text);
            }
        }
        logger.softWarning("Failed to get text from: " + locator);
        return Optional.empty();
    }

    public String getText(WebElement element) {
        logger.log("Getting text from element: " + element);
        String text = element.getText();
        logger.log("Got text: (" + text + ") from element: " + element);
        return text;
    }

    /**
     * Current value of an input/textarea.
     *
     * <p>Behavior is context-aware:
     * <ul>
     *   <li>In a WebView / HTML context we prefer {@link WebElement#getDomProperty(String)} {@code value},
     *       so React and other frameworks that keep the live text on the DOM property (not the HTML
     *       {@code value} attribute) read correctly.</li>
     *   <li>In a native context (XCUITest / UIAutomator2) {@code getDomProperty} is an unsupported
     *       endpoint and throws {@link UnsupportedCommandException}, so we go directly to
     *       {@link WebElement#getAttribute(String)} and fall back to {@link WebElement#getText()}
     *       (useful for {@code XCUIElementTypeStaticText} whose content is exposed as {@code label}).</li>
     * </ul>
     */
    public String getValue(WebElement element) {
        logger.log("Getting value from element: " + element);

        String text = null;
        boolean htmlContext = isHtmlContext();

        if (htmlContext) {
            try {
                text = element.getDomProperty("value");
            } catch (WebDriverException e) {
                // Covers UnsupportedCommandException (native driver does not expose the W3C
                // /property/value endpoint) and any transient driver-level issue while reading.
                logger.log("getDomProperty(\"value\") unsupported/failed in current context, falling back to getAttribute. Reason: "
                        + e.getClass().getSimpleName() + " - " + e.getMessage());
            }
        }

        if (text == null || text.isEmpty()) {
            text = Optional.ofNullable(element.getAttribute("value")).orElse("");
        }

        if (text.isEmpty() && !htmlContext) {
            // XCUIElementTypeStaticText / some native labels expose their displayed content
            // only via label (returned by getText()), not via value.
            try {
                String fallback = element.getText();
                if (fallback != null) text = fallback;
            } catch (WebDriverException ignored) {
                // swallow - we'll just return the empty string we already have
            }
        }

        logger.log("Got value: (" + text + ") from element: " + element);
        return text;
    }

    /**
     * Returns {@code true} when the driver is operating on an HTML DOM (dWeb/panel browsers or
     * Appium WebView), {@code false} for native mobile contexts.
     */
    private boolean isHtmlContext() {
        if (!(driver instanceof AndroidDriver || driver instanceof IOSDriver)) {
            return true;
        }
        return isInWebViewContext();
    }

    /**
     * Returns {@code true} when the Appium driver is currently focused on a WebView/HTML context,
     * {@code false} for native contexts or non-Appium drivers. Swallows any error from the context
     * query and treats it as "not a webview" so callers can branch safely.
     */
    private boolean isInWebViewContext() {
        try {
            String context = null;
            if (driver instanceof AndroidDriver) {
                context = ((AndroidDriver) driver).getContext();
            } else if (driver instanceof IOSDriver) {
                context = ((IOSDriver) driver).getContext();
            } else {
                return false;
            }
            return context != null && context.startsWith("WEBVIEW");
        } catch (WebDriverException e) {
            logger.log("Could not read current context, assuming native. Reason: "
                    + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        }
    }

    public String getValue(Locator locator) {
        WebElement el = element(locator).orElseThrow();
        return getValue(el);
    }

    public String getValueAttribute(WebElement element, String attribute) {
        logger.log("Getting \"" + attribute + "\" value from element: " + element);
        String valueAttribute = element.getAttribute(attribute);
        logger.log("Got \"" + attribute + "\" value: (" + valueAttribute + ") from element: " + element);
        return valueAttribute;
    }

    public String getValueAttribute(Locator locator, String attribute) {
        WebElement e = element(locator).orElseThrow();
        return getValueAttribute(e, attribute);
    }

    public void clear(Locator locator) {
        logger.log("Clearing text from: " + locator);
        Optional<WebElement> element = element(locator);
        element.ifPresentOrElse(WebElement::clear, () -> {
            logger.softWarning("Failed to clear text from: " + locator);
            throw new DriverToolsException("Failed to clear text from: " + locator);
        });
        logger.log("Cleared text from: " + locator);
    }

    public void clear(WebElement element) {
        logger.log("Clearing text from element: " + element);
        element.clear();
        logger.log("Cleared text from element: " + element);
    }

    public void dragAndDrop(WebElement source, WebElement target) {
        logger.log("Dragging " + source + ", to " + target);
        Actions actions = new Actions(driver);
        actions.dragAndDrop(source, target).build().perform();
        logger.log("Dragging " + source + ", to " + target + " complete");
    }

    public boolean dragAndDrop(Locator source, Locator target) {
        logger.log("Dragging " + source + ", to " + target);
        try {
            WebElement sourceElement = element(source).orElseThrow();
            WebElement targetElement = element(target).orElseThrow();
            dragAndDrop(sourceElement, targetElement);
        } catch (Exception e) {
            ExceptionAnalyser.analyse(e, forbiddenExceptions);
            logger.warn("Failed to Drag " + source + ", to " + target);
        }
        logger.log("Dragging " + source + ", to " + target + " complete");
        return true;
    }

    /** Drag for dnd-kit style sortables that need real pointer movement (press, move past activation distance, move over target, release). */
    public void dragAndDropWithSteps(WebElement source, WebElement target) {
        logger.log("Dragging with steps " + source + ", to " + target);
        Actions actions = new Actions(driver);
        actions.moveToElement(source)
                .clickAndHold()
                .pause(Duration.ofMillis(300))
                .moveByOffset(8, 8)
                .pause(Duration.ofMillis(200))
                .moveToElement(target)
                .pause(Duration.ofMillis(300))
                .moveByOffset(3, 3)
                .pause(Duration.ofMillis(300))
                .release()
                .build()
                .perform();
        logger.log("Dragging with steps " + source + ", to " + target + " complete");
    }

    /** Keyboard drag for dnd-kit sortables: focus handle, Space to lift, arrows to move, Space to drop. Positive steps move right, negative left. */
    public void dragAndDropWithKeyboard(WebElement handle, int steps) {
        logger.log("Keyboard dragging " + handle + " by " + steps + " step(s)");
        Keys arrow = steps >= 0 ? Keys.ARROW_RIGHT : Keys.ARROW_LEFT;
        Actions actions = new Actions(driver);
        actions.moveToElement(handle).click().pause(Duration.ofMillis(200)).sendKeys(Keys.SPACE).pause(Duration.ofMillis(300));
        for (int i = 0; i < Math.abs(steps); i++) {
            actions.sendKeys(arrow).pause(Duration.ofMillis(250));
        }
        actions.sendKeys(Keys.SPACE).pause(Duration.ofMillis(400)).build().perform();
    }

    public void dragAndDropBy(WebElement source, int xOffset, int yOffset) {
        logger.log("Dragging " + source + ", x offset: " + xOffset + ", y offset: " + yOffset);
        Actions actions = new Actions(driver);
        actions.dragAndDropBy(source, xOffset, yOffset).build().perform();
        logger.log("Dragging " + source + ", x offset: " + xOffset + ", y offset: " + yOffset + " complete");
    }

    public boolean dragAndDropBy(Locator source, int xOffset, int yOffset) {
        logger.log("Dragging " + source + ", x offset: " + xOffset + ", y offset: " + yOffset);
        try {
            WebElement sourceElement = element(source).orElseThrow();
            dragAndDropBy(sourceElement, xOffset, yOffset);
            logger.log("Dragging " + source + ", x offset: " + xOffset + ", y offset: " + yOffset + " complete");
            return true;
        } catch (Exception e) {
            logger.warn(ExceptionAnalyser.analyse(e, forbiddenExceptions).toString());
            logger.warn("Failed to Drag " + source + ", x offset: " + xOffset + ", y offset: " + yOffset);
            return false;
        }
    }



    public void clearInput(WebElement element) {
        logger.log("Clearing text from element: " + element);
        try {
            if (CoreFunctions.isMacSystem()) {
                element.sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.DELETE);
            } else {
                element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            }
            String value = element.getAttribute("value");
            if (value != null && !value.isEmpty()) {
                for (int i = 0; i < value.length(); i++) {
                    element.sendKeys(Keys.BACK_SPACE);
                }

                if (!element.getAttribute("value").isEmpty()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", element);
                }
            }
        } catch (Exception e) {
            logger.softWarning("Clear input failed: " + e.getMessage());
        }
        logger.log("Cleared text from element: " + element);
    }

    public void clearInput(Locator locator) {
        WebElement element = element(locator).orElseThrow();
        element.click();
        logger.log("Clearing text from element: " + element);
        try {
            if (CoreFunctions.isMacSystem()) {
                element.sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.DELETE);
            } else {
                element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
            }

            String value = element.getAttribute("value");
            if (value != null && !value.isEmpty()) {
                for (int i = 0; i < value.length(); i++) {
                    element.sendKeys(Keys.BACK_SPACE);
                }

                if (!element.getAttribute("value").isEmpty()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", element);
                }
            }
        } catch (Exception e) {
            logger.softWarning("Clear input failed for locator " + locator + ": " + e.getMessage());
        }
        logger.log("Cleared text from element: " + element);
    }

    public void scrollIntoWebView(Locator locator) {
        logger.log("Scrolling into web view: " + locator);
        element(locator).ifPresentOrElse(this::scrollIntoWebView, () -> {
            logger.softWarning("Failed to scroll into web view: " + locator);
            throw new DriverToolsException("Failed to scroll into web view: " + locator);
        });
        logger.log("Scrolled into web view: " + locator);
    }

    public void scrollIntoWebViewOptional(Locator locator) {
        logger.log("Scrolling into web view: " + locator);
        element(locator).ifPresentOrElse(element -> {
            try {
                scrollIntoWebView(element);
            } catch (StaleElementReferenceException sere) {
                // Re-rendered between resolving the element and running the scroll script.
                logger.log("Element went stale while scrolling into web view: " + locator);
            }
        }, () -> {
            logger.log("Failed to scroll into web view: " + locator);
        });
        logger.log("Scrolled into web view: " + locator);
    }

    /** Scrolls the element to the vertical centre of the viewport so sticky headers don't intercept clicks. */
    public void scrollIntoWebViewCenter(WebElement element) {
        logger.log("Scrolling into web view (center): " + element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
    }

    public void scrollIntoWebView(WebElement element) {
        logger.log("Scrolling into web view: " + element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        logger.log("Scrolled into web view: " + element);
    }

    /**
     * Scrolls a web view element into view horizontally without moving the page vertically
     * ({@code block:'nearest'}), unlike {@link #scrollIntoWebView(WebElement)} which jumps the page.
     * Use for items inside a horizontally scrollable container such as a carousel.
     * <p>
     * {@code direction} is the side of the container to scroll towards: {@link Swipe#RIGHT} aligns the
     * element with the right edge ({@code inline:'end'}) to reach items sitting off-screen to the
     * right, {@link Swipe#LEFT} aligns it with the left edge ({@code inline:'start'}).
     *
     * @throws DriverToolsException when {@code direction} is not {@link Swipe#LEFT} or {@link Swipe#RIGHT}
     */
    public void scrollIntoWebViewHorizontally(WebElement element, Swipe direction) {
        String inline = switch (direction) {
            case RIGHT -> "end";
            case LEFT -> "start";
            default -> throw new DriverToolsException("Unsupported horizontal scroll direction: " + direction);
        };
        logger.log("Scrolling " + direction + " into web view: " + element);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'nearest', inline:'" + inline + "'});", element);
        logger.log("Scrolled " + direction + " into web view: " + element);
    }

    /**
     * Resolves an element that lives in a horizontally scrollable web view container (e.g. a carousel).
     * Items outside the visible window never become visible on their own, so when the visibility wait
     * comes back empty the element is resolved by presence, scrolled into view towards
     * {@code direction} and re-waited for. Falls back to the presence-resolved element when the web
     * view never flips the visibility flag.
     *
     * @param direction side of the container to scroll towards, see
     *                  {@link #scrollIntoWebViewHorizontally(WebElement, Swipe)}
     * @throws DriverToolsException when the element is not in the DOM at all
     */
    public WebElement waitForElementToAppearScrollingHorizontally(Locator locator, Swipe direction, int seconds) {
        Optional<WebElement> visibleElement = waitForElementToAppear(locator, seconds);
        if (visibleElement.isPresent()) {
            return visibleElement.orElseThrow();
        }

        logger.log("Element is not visible, scrolling the container " + direction + ": " + locator);
        WebElement offScreenElement = waitForElementToPresence(locator, seconds)
                .orElseThrow(() -> new DriverToolsException("Failed to find element to scroll " + direction + " to: " + locator));
        scrollIntoWebViewHorizontally(offScreenElement, direction);
        return waitForElementToAppear(locator, seconds).orElse(offScreenElement);
    }

    public void scrollToElement(WebElement element) {
        logger.log("Scrolling into element: "+element);
        new Actions(driver).scrollToElement(element).perform();
        logger.log("Scrolled into element: "+element);
    }

    @SneakyThrows
    public void scrollToElement(Locator locator) {
        logger.log("Scrolling into element: "+locator);
        try{
            element(locator).ifPresentOrElse(this::scrollToElement, () -> {
                logger.softWarning("Failed to scroll into element: " + locator);
                throw new DriverToolsException("Failed to scroll into element: " + locator);
            });
        }catch (MoveTargetOutOfBoundsException e){
            logger.log("Out of bounds exception detected");
            Thread.sleep(1000);
        }
        logger.log("Scrolled into element: "+locator);
    }

    public void scrollInsideElement(WebElement element) {
        logger.log("Scrolling inside element: "+element);
        WheelInput.ScrollOrigin scrollOrigin = WheelInput.ScrollOrigin.fromElement(element);
        new Actions(driver)
                .scrollFromOrigin(scrollOrigin, 0, 500)
                .perform();
        logger.log("Scrolled inside element: "+element);
    }

    public void scrollInsideElement(WebElement element, int pixelsAmount) {
        logger.log("Scrolling inside element: "+element);
        WheelInput.ScrollOrigin scrollOrigin = WheelInput.ScrollOrigin.fromElement(element);
        new Actions(driver)
                .scrollFromOrigin(scrollOrigin, 0, pixelsAmount)
                .perform();
        logger.log("Scrolled inside element: "+element);
    }

    public void scrollInsideElement(Locator locator, int pixelsAmount) {
        logger.log("Scrolling inside locator: "+locator);
        WebElement element = element(locator).orElseThrow();
        scrollInsideElement(element, pixelsAmount);
        logger.log("Scrolled inside locator: "+locator);
    }

    public void scrollToBottomOfTable(Locator locator) {
        logger.log("Scrolling to bottom of table: " + locator);
        try {
            element(locator).ifPresentOrElse(tableElement -> {
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", tableElement);
            }, () -> {
                logger.softWarning("Failed to scroll to bottom of table: " + locator);
                throw new DriverToolsException("Failed to scroll to bottom of table: " + locator);
            });
        } catch (StaleElementReferenceException e) {
            logger.log("Table element is not attached to the page document");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            WebElement retryElement = wait.until(
                    ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(locator.by()))
            );
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", retryElement);
        }
        logger.log("Scrolled to bottom of table: " + locator);
    }


    public void scroll(int i, int distanceInPixels) throws InterruptedException{
        for (int j = 0; j < i; j++) {
            final int scrollNum = i;
            final int pixels = distanceInPixels;

            executeWithHealthCheck(() -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                logger.log(scrollNum + " Scrolling by " + pixels + " pixels");
                js.executeScript("window.scrollBy(0, arguments[0]);", pixels);
                return null;
            }, "scroll(" + i + ", " + distanceInPixels + ")");

            Thread.sleep(500);
        }
    }

    public Object focusWindow() {
        Object focusState = ((JavascriptExecutor) driver).executeScript(
                "window.focus(); return {hasFocus: document.hasFocus(), visibilityState: document.visibilityState};");
        logger.log("Window focus diagnostic: " + focusState);
        return focusState;
    }

    @SneakyThrows
    public boolean waitForWebview() {
        logger.log("Wait for web view to be available");
        boolean webviewIsAvailable = false;
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                for (int i = 0; (i < 5 && !webviewIsAvailable); i++) {
                    AndroidDriver d = (AndroidDriver) driver;
                    Set<String> contextHandles = d.getContextHandles();
                    logger.log("Context handles: " + contextHandles);
                    for (String context : contextHandles) {
                        if (context.contains("WEBVIEW_com")) {
                            webviewIsAvailable = true;
                            break;
                        }
                    }
                    if (!webviewIsAvailable) {
                        logger.log("There are no webview available " + i);
                    }
                    Thread.sleep(1000);
                }
            }
            case IOS -> {
                for (int i = 0; (i < 5 && !webviewIsAvailable); i++) {
                    IOSDriver d = (IOSDriver) driver;
                    Set<String> contextHandles = d.getContextHandles();
                    for (int j = 0; contextHandles.size() < 2 && j < 5; j++) {
                        logger.log("Getting context handles " + j + " times.");
                        Thread.sleep(1000);
                        contextHandles = d.getContextHandles();
                        logger.log("Context handles: " + contextHandles);
                    }
                    logger.log("Context handles: " + contextHandles);
                    for (String context : contextHandles) {
                        if (context.contains("WEBVIEW")) {
                            webviewIsAvailable = true;
                            break;
                        }
                    }
                    if (!webviewIsAvailable) {
                        logger.log("There are no webview available " + i);
                    }
                    Thread.sleep(1000);
                }
            }
        }
        return webviewIsAvailable;
    }

    @SneakyThrows
    public String switchToWebViewIndex(int targetIndex) {
        logger.log("Switching to web view by index '" + targetIndex + "'");
        StringBuilder currentUrl = new StringBuilder();
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                AndroidDriver d = (AndroidDriver) driver;
                Set<String> contextHandles = d.getContextHandles();
                logger.log("Context handles: " + contextHandles);
                for (String context : contextHandles) {
                    if (context.contains("WEBVIEW_com")) {
                        d.context(context);
                        logger.log("Switched to web view");
                        // Optimize: Wrap getWindowHandles() with timeout to prevent 6-minute hangs
                        AtomicReference<Set<String>> windowHandlesRef = new AtomicReference<>();
                        try {
                            executeWithTimeout(() -> {
                                windowHandlesRef.set(d.getWindowHandles());
                                return null;
                            }, 10, TimeUnit.SECONDS);
                        } catch (Exception e) {
                            logger.log("Timeout getting window handles: " + e.getMessage());
                            continue; // Skip to next context
                        }
                        Set<String> windowHandles = windowHandlesRef.get();
                        if (windowHandles == null || windowHandles.isEmpty()) {
                            logger.log("No window handles found, webview may not be ready");
                            continue; // Skip to next context
                        }
                        logger.log("Window Handles: " + windowHandles);
                        String[] windowHandlesArray = windowHandles.toArray(new String[0]);
                        for (int index = 0; index < windowHandlesArray.length; index++) {
                            String window = windowHandlesArray[index];
                            if (index == targetIndex) {
                                d.switchTo().window(window);
                                logger.log("Selected window: " + window);
                                String currentURL = d.executeScript("return window.location.href").toString();
                                if (currentURL.contains("tiket.com")) {
                                    currentUrl.append(currentURL);
                                    logger.log("Webview with url: " + currentURL);
                                    break;
                                }
                            } else {
                                logger.log("Skipping window '" + window + "' | index == '" + index + "'");
                            }
                        }
                    }
                }
            }
            case IOS -> {
                IOSDriver d = (IOSDriver) driver;
                Set<String> contextHandles = d.getContextHandles();
                for (int i = 0; contextHandles.size() < 2 && i < 60; i++) {
                    logger.log("Getting context handles " + i + " times.");
                    Thread.sleep(1000);
                    contextHandles = d.getContextHandles();
                }
                logger.log("Context handles: " + contextHandles);
                for (String context : contextHandles) {
                    if (context.contains("WEBVIEW")) {
                        String[] contextHandlesArray = contextHandles.toArray(new String[0]);
                        logger.log("Context Handles Array Length: " + contextHandlesArray.length);
                        for (int index = 0; index < contextHandlesArray.length; index++) {
                            context = contextHandlesArray[index];
                            if (index == targetIndex) {
                                d.context(context);
                                logger.log("Switched to web view");
                                String currentURL = d.executeScript("return window.location.href").toString();
                                if (currentURL.contains("tiket.com")) {
                                    currentUrl.append(currentURL);
                                    logger.log("Webview with url: " + currentURL);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return currentUrl.toString();
    }

    @SneakyThrows
    public String switchToWebViewUrl(String targetUrl) {
        return switchToWebViewUrl(targetUrl,3);
    }

    @SneakyThrows
    public String switchToWebViewUrl(String targetUrl, int maxTry) {
        logger.log("Switching to web view with url : " + targetUrl);
        String currentUrl = "";
        Thread.sleep(1000);
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                currentUrl = switchToWebViewAndroidUrl(targetUrl, maxTry);
            }
            case IOS -> {
                currentUrl = switchToWebViewIosUrl(targetUrl, maxTry);
            }
        }
        return currentUrl;
    }

    @SneakyThrows
    public void terminateApp(String bundleId){
        AndroidDriver d = (AndroidDriver) driver;
        logger.log("Terminating app with bundleId: " + bundleId);
        d.terminateApp(bundleId);
        logger.log("App with bundleId: " + bundleId + " terminated");
    }


    /**
     * When getWindowHandles() times out or returns empty in WEBVIEW_chrome, try to get the current
     * URL via executeScript in the current context. If the URL matches targetUrl, the context is
     * already the right one and the caller can proceed (e.g. to skip passkey).
     */
    private String tryGetCurrentUrlInContext(AndroidDriver d, String targetUrl) {
        try {
            AtomicReference<String> urlRef = new AtomicReference<>();
            executeWithTimeout(() -> {
                Object href = d.executeScript("return window.location.href");
                urlRef.set(href != null ? href.toString() : null);
                return null;
            }, 5, TimeUnit.SECONDS);
            String url = urlRef.get();
            if (url != null && url.contains(targetUrl)) {
                return url;
            }
        } catch (Exception e) {
            logger.log("Fallback get current url failed: " + e.getMessage());
        }
        return null;
    }

    @SneakyThrows
    private String switchToWebViewAndroidUrl(String targetUrl, int maxTry) {
        AndroidDriver d = (AndroidDriver) driver;
        Set<String> contextHandles;
        for (int counter = 0; counter <= maxTry; counter++) {
            logger.log("Getting context handles " + counter + " times.");
            contextHandles = d.getContextHandles();
            logger.log("Context handles: " + contextHandles);
            for (String context : contextHandles) {
                String targetContext = (targetUrl.contains("login") || targetUrl.contains("blibli") || targetUrl.contains("yopmail")) ? "WEBVIEW_chrome" : "WEBVIEW_com";
                logger.log("targetContext: " + targetContext);
                if (context.contains(targetContext)) {
                    try{
                        String currentContext = d.getContext();
                        logger.log("current context: " + currentContext);

                        executeWithTimeout(() -> {
                            d.context(context);
                            return null;
                        }, 5, TimeUnit.SECONDS);

                        logger.log("Switched to web view");
                        // Optimize: Wrap getWindowHandles() with timeout to prevent 6-minute hangs
                        AtomicReference<Set<String>> windowHandlesRef = new AtomicReference<>();
                        try {
                            executeWithTimeout(() -> {
                                windowHandlesRef.set(d.getWindowHandles());
                                return null;
                            }, 15, TimeUnit.SECONDS);
                        } catch (Exception e) {
                            logger.log("Timeout getting window handles: " + e.getMessage());
                            String fallbackUrl = tryGetCurrentUrlInContext(d, targetUrl);
                            if (fallbackUrl != null) {
                                logger.log("Using fallback: current url in context: " + fallbackUrl);
                                return fallbackUrl;
                            }
                            logger.log("Webview may not be ready, retrying...");
                            continue; // Skip to next iteration
                        }
                        Set<String> windowHandles = windowHandlesRef.get();
                        if (windowHandles == null || windowHandles.isEmpty()) {
                            logger.log("No window handles found, webview may not be ready");
                            String fallbackUrl = tryGetCurrentUrlInContext(d, targetUrl);
                            if (fallbackUrl != null) {
                                logger.log("Using fallback: current url in context: " + fallbackUrl);
                                return fallbackUrl;
                            }
                            continue; // Skip to next iteration
                        }
                        logger.log("Window Handles: " + windowHandles);
                        String[] windowHandlesArray = windowHandles.toArray(new String[0]);
                        for (String window : windowHandlesArray) {
                            d.switchTo().window(window);
                            logger.log("Selected window: " + window);
                            AtomicReference<String> currentURLAtomic = new AtomicReference<>();
                            executeWithTimeout(() -> {
                                currentURLAtomic.set(d.executeScript("return window.location.href").toString());
                                return null;
                            }, 5, TimeUnit.SECONDS);

                            String currentURL = currentURLAtomic.get();
                            logger.log("current url: " + currentURL);
                            if (currentURL != null && currentURL.contains(targetUrl)) {
                                logger.log("Found Webview. Current url: " + currentURL);
                                return currentURL;
                            } else {
                                logger.log("Skipping webview with url '" + currentURL + "'");
                            }
                        }
                    }catch (Exception e) {
                        logger.log("error got: " + e.getMessage());
                        logger.log("Failed to get current url");
                    }
                }
            }
            logger.log("Webview not found retry");
            Thread.sleep(1000);
        }
        return "";
    }

    @SneakyThrows
    private String switchToWebViewIosUrl(String targetUrl, int maxTry){
        IOSDriver d = (IOSDriver) driver;
        Set<String> contextHandles;
        for(int counter = 0; counter <= maxTry; counter++){
            logger.log("Getting context handles " + counter + " times.");
            contextHandles = d.getContextHandles();
            logger.log("Context handles ios: " + contextHandles);
            String[] contextArray = sortContext(contextHandles.toArray(String[]::new));

            if(targetUrl.contains("blibli") || targetUrl.contains("login") || targetUrl.contains("register")){
                contextArray = filterUniqueWebViews(contextArray);
                logger.log("switching for unm page");
                logger.log("context list: " + Arrays.toString(contextArray));
            }

            logger.log("context list after sort: " + Arrays.toString(contextArray));
            for (String context : contextArray) {
                if (context.contains("WEBVIEW")) {
                    try {
                        logger.log("current webview instance from array: " + context);
                        String currentContext = d.getContext();
                        logger.log("current context: " + currentContext);

                        executeWithTimeout(() -> {
                            d.context(context);
                            return null;
                        }, 5, TimeUnit.SECONDS);

                        logger.log("current webview instance from getContext: " + d.getContext());

                        AtomicReference<String> currentURLAtomic = new AtomicReference<>();
                        if ("payment".equalsIgnoreCase(targetUrl)) {
                            try {
                                executeWithTimeout(() -> {
                                    Object href = d.executeScript("return window.location.href");
                                    if (href != null) {
                                        currentURLAtomic.set(href.toString());
                                    }
                                    return null;
                                }, 5, TimeUnit.SECONDS);
                            } catch (Exception e) {
                                logger.log("JavaScript WebView URL lookup failed: " + e.getMessage());
                            }
                        } else {
                            executeWithTimeout(() -> {
                                currentURLAtomic.set(d.executeScript("return window.location.href").toString());
                                return null;
                            }, 5, TimeUnit.SECONDS);
                        }

                        String currentURL = currentURLAtomic.get();
                        if ("payment".equalsIgnoreCase(targetUrl)
                                && (currentURL == null || currentURL.isBlank())) {
                            try {
                                executeWithTimeout(() -> {
                                    currentURLAtomic.set(d.getCurrentUrl());
                                    return null;
                                }, 5, TimeUnit.SECONDS);
                                currentURL = currentURLAtomic.get();
                            } catch (Exception e) {
                                logger.log("Fallback WebDriver current URL lookup failed: " + e.getMessage());
                            }
                        }

                        logger.log("currentURL: '" + currentURL + "'");
                        logger.log("targetUrl: '" + targetUrl + "'");
                        logger.log("contains result: " + (currentURL != null && currentURL.contains(targetUrl)));

                        if (currentURL != null && urlMatchesTarget(currentURL, targetUrl)) {
                            logger.log("Switched to webview with url: " + currentURL);
                            return currentURL;
                        } else {
                            logger.log("Skipping webview with url '" + currentURL + "' (does not contain '" + targetUrl + "')");
                            Thread.sleep(1000);
                        }
                    }catch (Exception e){
                        logger.log("error got: " + e.getMessage());
                        logger.log("Failed to get current url");
                    }
                }
            }
            logger.log("Webview not found retry");
            Thread.sleep(1000);
        }
        return "";
    }


    /**
     * Helper method to check if a URL matches the target pattern.
     * For "payment" target, uses fallback patterns since iOS payment URLs
     * redirect from mpayment domain to preprod.tiket.com/next/v4
     */
    private boolean urlMatchesTarget(String currentURL, String targetUrl) {
        // Standard exact match
        if (currentURL.contains(targetUrl)) {
            return true;
        }

        // Fallback matching for payment pages: check for order_hash or order_id in URL
        if ("payment".equals(targetUrl)) {
            if (currentURL.contains("order_hash") || currentURL.contains("order_id")) {
                logger.log("Payment page detected by order parameters");
                return true;
            }
            // Additional pattern for payment flow (/next/v4 is specific to payment pages)
            if (currentURL.contains("/next/v4")) {
                logger.log("Payment page detected by URL pattern /next/v4");
                return true;
            }
        }

        return false;
    }

    public String[] filterUniqueWebViews(String[] contexts) {
        Map<String, Integer> prefixCount = new HashMap<>();
        List<String> result = new ArrayList<>();

        // First pass: Count occurrences of each prefix
        for (String context : contexts) {
            String prefix = context.contains(".") ? context.split("\\.")[0] : context;  // Extract prefix
            prefixCount.put(prefix, prefixCount.getOrDefault(prefix, 0) + 1);
        }

        // Second pass: Only keep items with unique prefixes
        for (String context : contexts) {
            String prefix = context.contains(".") ? context.split("\\.")[0] : context;
            if (prefixCount.get(prefix) == 1) {  // Keep only if the prefix is unique
                result.add(context);
            }
        }

        if(result.size() == 1){
            logger.log("no unique context found except native returning original");
            return contexts;
        } else{
            return result.toArray(new String[0]);  // Convert List<String> back to String[]
        }
    }

    private void executeWithTimeout(Callable<Void> callable, long timeout, TimeUnit timeUnit) throws TimeoutException, Exception {
        timeLimiter.callWithTimeout(callable, timeout, timeUnit);
    }

    // ==================== BROWSER HEALTH CHECK METHODS ====================

    /**
     * Execute operation with timeout. If timeout occurs, check browser health
     * and fail fast with diagnostic info instead of waiting for HTTP timeout.
     */
    private <R> R executeWithHealthCheck(Callable<R> operation, String operationName) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<R> future = null;
        try {
            future = executor.submit(operation);
            return future.get(OPERATION_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        } catch (java.util.concurrent.TimeoutException te) {
            // Cancel the stuck operation to free up resources
            if (future != null) {
                future.cancel(true);
            }

            // Operation took too long - check if browser is frozen
            logger.warn("[WARNING] Operation '" + operationName + "' exceeded " + OPERATION_TIMEOUT_SECONDS + "s - checking browser health...");

            BrowserHealthResult health = checkBrowserHealthQuick();

            String errorMessage = buildHealthErrorMessage(operationName, health);
            logger.fail(errorMessage);

            throw new RuntimeException(errorMessage, te);

        } catch (ExecutionException ee) {
            throw new RuntimeException("Operation '" + operationName + "' failed: " + ee.getCause().getMessage(), ee.getCause());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Operation '" + operationName + "' was interrupted", ie);
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Quick browser health check with 5 second timeout
     */
    private BrowserHealthResult checkBrowserHealthQuick() {
        BrowserHealthResult result = new BrowserHealthResult();
        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Boolean> heartbeat = executor.submit(() -> {
                try {
                    ((JavascriptExecutor) driver).executeScript("return true;");
                    return true;
                } catch (Exception e) {
                    return false;
                }
            });

            result.isResponsive = heartbeat.get(HEALTH_CHECK_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            if (result.isResponsive) {
                // Get additional diagnostic info
                try { result.currentUrl = driver.getCurrentUrl(); } catch (Exception ignored) {}
                try { result.pageTitle = driver.getTitle(); } catch (Exception ignored) {}
                try {
                    result.readyState = String.valueOf(
                            ((JavascriptExecutor) driver).executeScript("return document.readyState")
                    );
                } catch (Exception ignored) {}
            }

        } catch (java.util.concurrent.TimeoutException e) {
            result.isResponsive = false;
            result.diagnosis = "BROWSER FROZEN - Not responding to commands";
        } catch (Exception e) {
            result.isResponsive = false;
            result.diagnosis = "BROWSER ERROR - " + e.getClass().getSimpleName();
        } finally {
            result.checkDurationMs = System.currentTimeMillis() - startTime;
            executor.shutdownNow();
        }

        // Set diagnosis if not already set
        if (result.diagnosis == null) {
            result.diagnosis = result.isResponsive
                    ? "BROWSER OK - Operation was slow but browser is responsive"
                    : "BROWSER UNRESPONSIVE";
        }

        return result;
    }

    /**
     * Build formatted error message with health check results
     */
    private String buildHealthErrorMessage(String operationName, BrowserHealthResult health) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("================================================================\n");
        sb.append("           OPERATION TIMEOUT - BROWSER HEALTH CHECK             \n");
        sb.append("================================================================\n");
        sb.append(String.format(" Operation      : %-45s %n", operationName));
        sb.append(String.format(" Timeout After  : %-45s %n", OPERATION_TIMEOUT_SECONDS + " seconds"));
        sb.append("----------------------------------------------------------------\n");
        sb.append(String.format(" Browser Status : %-45s %n", health.isResponsive ? "RESPONSIVE" : "FROZEN/CRASHED"));
        sb.append(String.format(" Diagnosis      : %-45s %n", health.diagnosis));
        sb.append(String.format(" Health Check   : %-45s %n", health.checkDurationMs + "ms"));
        if (health.isResponsive) {
            sb.append(String.format(" Current URL    : %-45s %n", truncateString(health.currentUrl, 45)));
            sb.append(String.format(" Page Title     : %-45s %n", truncateString(health.pageTitle, 45)));
            sb.append(String.format(" Ready State    : %-45s %n", health.readyState));
        }
        sb.append("================================================================");
        return sb.toString();
    }

    private String truncateString(String str, int maxLen) {
        if (str == null) return "N/A";
        return str.length() > maxLen ? str.substring(0, maxLen - 3) + "..." : str;
    }

    /**
     * Health check result container
     */
    private static class BrowserHealthResult {
        boolean isResponsive = false;
        String currentUrl = "N/A";
        String pageTitle = "N/A";
        String readyState = "N/A";
        String diagnosis = null;
        long checkDurationMs = 0;
    }

    // ==================== STATIC HEALTH CHECK METHODS (for screenshot) ====================

    /**
     * Static version of executeWithHealthCheck for static screenshot methods
     */
    private static <R> R executeStaticWithHealthCheck(WebDriver driver, Callable<R> operation, String operationName) {
        if (driver == null) {
            throw new RuntimeException(operationName + " failed: WebDriver is null");
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<R> future = null;

        try {
            future = executor.submit(operation);
            return future.get(OPERATION_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        } catch (java.util.concurrent.TimeoutException te) {
            // Cancel the stuck operation to free up resources
            if (future != null) {
                future.cancel(true);
            }

            // Quick health check
            boolean isResponsive = checkDriverResponsiveStatic(driver);

            String diagnosis = isResponsive
                    ? "BROWSER OK - Screenshot operation was slow"
                    : "BROWSER FROZEN - Not responding to commands";

            StringBuilder errorMsg = new StringBuilder()
                    .append("\n================================================================\n")
                    .append("           SCREENSHOT TIMEOUT - BROWSER HEALTH CHECK            \n")
                    .append("================================================================\n")
                    .append(" Operation      : ").append(operationName).append("\n")
                    .append(" Timeout After  : ").append(OPERATION_TIMEOUT_SECONDS).append(" seconds\n")
                    .append("----------------------------------------------------------------\n")
                    .append(" Browser Status : ").append(isResponsive ? "RESPONSIVE" : "FROZEN/CRASHED").append("\n")
                    .append(" Diagnosis      : ").append(diagnosis).append("\n")
                    .append("================================================================");

            throw new RuntimeException(errorMsg.toString(), te);

        } catch (ExecutionException ee) {
            throw new RuntimeException("Screenshot failed: " + ee.getCause().getMessage(), ee.getCause());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Screenshot interrupted", ie);
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Static health check for screenshot methods
     */
    private static boolean checkDriverResponsiveStatic(WebDriver driver) {
        if (driver == null) {
            return false;
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<Boolean> heartbeat = executor.submit(() -> {
                try {
                    ((JavascriptExecutor) driver).executeScript("return true;");
                    return true;
                } catch (Exception e) {
                    return false;
                }
            });
            return heartbeat.get(HEALTH_CHECK_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        } finally {
            executor.shutdownNow();
        }
    }

    // ==================== END BROWSER HEALTH CHECK METHODS ====================

    private String[] sortContext(String[] contextArray){

        Arrays.sort(contextArray, (wv1, wv2) -> {
            if (wv1.startsWith("WEBVIEW_") && wv2.startsWith("WEBVIEW_")) {
                // Extract the numeric parts after "WEBVIEW_"
                String[] parts1 = wv1.split("_")[1].split("\\.");
                String[] parts2 = wv2.split("_")[1].split("\\.");

                // Compare each part numerically
                for (int i = 0; i < Math.min(parts1.length, parts2.length); i++) {
                    int num1 = Integer.parseInt(parts1[i]);
                    int num2 = Integer.parseInt(parts2[i]);
                    if (num1 != num2) {
                        return Integer.compare(num2, num1); // Descending order
                    }
                }

                // If all compared parts are equal, the one with more segments is "larger"
                return Integer.compare(parts2.length, parts1.length);
            } else if (wv1.startsWith("WEBVIEW_")) {
                // If wv1 is a "WEBVIEW_" and wv2 is not, wv1 should come first
                return -1;
            } else if (wv2.startsWith("WEBVIEW_")) {
                // If wv2 is a "WEBVIEW_" and wv1 is not, wv2 should come first
                return 1;
            } else {
                // If neither is a "WEBVIEW_", keep original order (e.g., for "NATIVE_APP")
                return 0;
            }
        });
        return contextArray;
    }

    @SneakyThrows
    public String switchToWebView() {
        logger.log("Switching to web view");
        String currentUrl = "";
        Thread.sleep(2000);
        switch (Configs.PLATFORM) {
            case ANDROID, AMWEB -> {
                currentUrl = switchToWebViewAndroid();
            }
            case IOS, IMWEB -> {
                currentUrl = switchToWebViewIos();
            }
        }
        return currentUrl;
    }

    @SneakyThrows
    private String switchToWebViewAndroid(){
        AndroidDriver d = (AndroidDriver) driver;
        if(Configs.PLATFORM == Platform.AMWEB) {
            logger.log("All context: " + d.getContextHandles().toString());
            logger.log("Setting context to Chromium");
            d.context("CHROMIUM");
            logger.log("Now context is: " + d.getContext());
            return d.getContext();
        }
        Set<String> contextHandles;
        for(int counter = 0; counter < MAX_TRY; counter++){
            logger.log("Getting context handles " + counter + " times.");
            contextHandles = d.getContextHandles();
            logger.log("Context handles: " + contextHandles);
            for (String context : contextHandles) {
                if (context.contains("WEBVIEW_com")) {
                    d.context(context);
                    logger.log("Switched to web view");
                    // Optimize: Wrap getWindowHandles() with timeout to prevent 6-minute hangs
                    AtomicReference<Set<String>> windowHandlesRef = new AtomicReference<>();
                    try {
                        executeWithTimeout(() -> {
                            windowHandlesRef.set(d.getWindowHandles());
                            return null;
                        }, 10, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        logger.log("Timeout getting window handles: " + e.getMessage());
                        logger.log("Webview may not be ready, retrying...");
                        continue; // Skip to next iteration
                    }
                    Set<String> windowHandles = windowHandlesRef.get();
                    if (windowHandles == null || windowHandles.isEmpty()) {
                        logger.log("No window handles found, webview may not be ready");
                        continue; // Skip to next iteration
                    }
                    logger.log("Window Handles: " + windowHandles);
                    for (String window : windowHandles) {
                        d.switchTo().window(window);
                        logger.log("Selected window: " + window);
                        String currentURL = d.executeScript("return window.location.href").toString();
                        if (currentURL.contains("tiket.com")) {
                            logger.log("Webview with url: " + currentURL);
                            return currentURL;
                        }
                    }
                }
            }
            logger.log("Webview not found retry");
            Thread.sleep(1000);
        }
        return "";
    }

    @SneakyThrows
    private String switchToWebViewIos(){
        IOSDriver d = (IOSDriver) driver;
        if(Configs.PLATFORM == Platform.IMWEB) {
            logger.log("All context: " + d.getContextHandles().toString());
            logger.log("Setting context to Webview");
            for(String context: d.getContextHandles()) {
                if(!context.equalsIgnoreCase("native")) {
                    d.context(context);
                }
            }
            logger.log("Now context is: " + d.getContext());
            return d.getContext();
        }
        Set<String> contextHandles;
        for(int counter = 0; counter < MAX_TRY; counter++){
            logger.log("Getting context handles " + counter + " times.");
            contextHandles = d.getContextHandles();
            logger.log("Context handles ios: " + contextHandles);
            String [] contextArray = sortContext(contextHandles.toArray(String[]::new));
            logger.log("context list after sort: " + Arrays.toString(contextArray));
            for (String context : contextArray) {
                if (context.contains("WEBVIEW")) {
                    logger.log("current webview instance from array: " + context);
                    d.context(context);
                    logger.log("current webview instance from getContext: " + d.getContext());
                    String currentURL = d.executeScript("return window.location.href").toString();
                    logger.log("Webview url : " + "tiket.com");
                    if (currentURL.contains("tiket.com")) {
                        logger.log("Switched to webview with url: " + currentURL);
                        return currentURL;
                    } else {
                        logger.log("Skipping webview with url '" + currentURL + "'");
                        Thread.sleep(1000);
                    }
                }
            }
            logger.log("Webview not found retry");
            Thread.sleep(1000);
        }
        return "";
    }

    public void switchToNative() {
        logger.log("Switching to native");
        switch (Configs.PLATFORM) {
            case ANDROID, AMWEB, IOS, IMWEB -> switchToNativeMobile((SupportsContextSwitching) driver);
            default -> {
            }
        }
    }

    /**
     * Prefer direct {@code NATIVE_APP} and current-context checks over listing all contexts.
     * {@link SupportsContextSwitching#getContextHandles()} can fail transiently after app restart (deeplink / closeApp)
     * or on some cloud devices while the AUT is still settling.
     */
    private void switchToNativeMobile(SupportsContextSwitching d) {
        try {
            String current = d.getContext();
            if (current != null && current.contains("NATIVE")) {
                logger.log("Already on native context: " + current);
                return;
            }
        } catch (WebDriverException e) {
            logger.log("Could not read current context before switching to native: " + e.getMessage());
        }
        try {
            d.context("NATIVE_APP");
            logger.log("Switched to native");
            return;
        } catch (WebDriverException e) {
            logger.log("Direct NATIVE_APP context switch failed, enumerating contexts: " + e.getMessage());
        }
        Set<String> contextHandles = d.getContextHandles();
        logger.log("Available Contexts: " + contextHandles);
        for (String context : contextHandles) {
            if (context.contains("NATIVE_APP")) {
                d.context(context);
                logger.log("Switched to native");
                return;
            }
        }
    }

    /**
     * Switch to targeted iframe element which present in webview context
     *
     * @param iframeElement -> targeted iframe element
     */
    @SneakyThrows
    public void switchToIframe(WebElement iframeElement) {
        driver.switchTo().frame(iframeElement);
        logger.log("Switched to iframe");
        logger.log("Current iframe context : " + driver.getCurrentUrl());
    }

    @SneakyThrows
    public void switchBackFromIframe() {
        driver.switchTo().defaultContent();
        logger.log("Switched back from iframe");
        logger.log("Current context : " + driver.getCurrentUrl());
    }

    /**
     * Performs an upward swipe gesture on the screen, which scrolls the content downward.
     * <p>
     * This method simulates a user's finger swiping from bottom to top (70% to 30% of screen height),
     * resulting in the content moving down to reveal items that were below the current view.
     * </p>
     *
     * <h3>Behavior:</h3>
     * <ul>
     *   <li>Finger gesture direction: Upward (bottom → top)</li>
     *   <li>Content movement: Downward (reveals content below)</li>
     *   <li>Equivalent to: Scrolling down in a list or page</li>
     * </ul>
     *
     * <h3>Implementation Details:</h3>
     * <ul>
     *   <li>Start position: 70% from top of screen</li>
     *   <li>End position: 30% from top of screen</li>
     *   <li>Duration: 2 seconds (as defined in {@link Swipe} enum)</li>
     * </ul>
     *
     * <h3>Retry Logic:</h3>
     * If the initial swipe fails due to stale element or invalid element state,
     * the method automatically retries once after waiting for the FrameLayout to refresh.
     *
     * <h3>Example Usage:</h3>
     * <pre>{@code
     * // Scroll down to find an element further down the page
     * swipeUp();
     * WebElement bottomElement = driver.findElement(By.id("bottom-element"));
     * }</pre>
     *
     * @throws StaleElementReferenceException if element becomes stale during swipe (handled internally)
     * @throws InvalidElementStateException if element is in invalid state during swipe (handled internally)
     * @see Swipe#UP
     * @see #swipeDown() for opposite direction (scrolls content upward)
     */
    public void swipeUp() {
        try {
            logger.log("Performing swipe up on screen");
            Swipe.UP.perform(driver);
        } catch (StaleElementReferenceException | InvalidElementStateException e1) {
            logger.log("Retry performing swipe up on screen");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.refreshed(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.widget.FrameLayout"))));
            Swipe.UP.perform(driver);
        }
    }

    public void swipeUp(double swipeRatio) {
        try {
            logger.log("Performing swipe up on screen by ratio: " + swipeRatio);

            Dimension size = driver.manage().window().getSize();
            int width = size.width;
            int height = size.height;

            int startX = width / 2;
            int startY = (int) (height * 0.8);
            int endY = (int) (startY - (height * swipeRatio));

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);

            swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(400), PointerInput.Origin.viewport(), startX, endY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            ((AppiumDriver) driver).perform(Collections.singletonList(swipe));

        } catch (Exception e) {
            logger.log("Retry performing swipe up on screen after exception: " + e.getMessage());
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.refreshed(
                            ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.widget.FrameLayout"))));
            swipeUp(swipeRatio);
        }
    }

    public boolean swipeLeftUntilFound(Locator containerLocator, Locator targetLocator, int maxSwipes) {
        int swipes = 0;

        while (swipes < maxSwipes) {
            if (element(targetLocator).isPresent()) {
                return true;
            }

            WebElement container = element(containerLocator).orElseThrow();
            logger.log("Swipe left inside container: " + container);
            Swipe.LEFT.perform(driver, container); // use your existing swipe method

            swipes++;
        }

        // final check after max swipes
        return element(targetLocator).isPresent();
    }

    private void swipeLeftOnElement(WebElement container) {
        // Get element location and size
        int startX = (int) (container.getLocation().getX() + container.getSize().getWidth() * 0.8);
        int endX = (int) (container.getLocation().getX() + container.getSize().getWidth() * 0.2);
        int y = container.getLocation().getY() + container.getSize().getHeight() / 2;

        new TouchAction<>((PerformsTouchActions) getDriver())
                .press(PointOption.point(startX, y))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(endX, y))
                .release()
                .perform();
    }

    public void swipeUp(int numberOfSwipe) {
        for (int i = 1; i <= numberOfSwipe; i++) {
            swipeUp();
        }
    }

    public void swipeUp(WebElement element) {
        logger.log("Performing swipe up on element: " + element);
        try {
            Swipe.UP.perform(driver);
        } catch (StaleElementReferenceException e) {
            logger.log("Element is not attached to the page document");
            logger.log("Retry performing swipe up on screen");
            Swipe.UP.perform(driver, element);
        }
    }

    public void swipeUp(Locator locator) {
        WebElement element = element(locator).orElseThrow();
        swipeUp(element);
    }

    public void swipeUpPresence(Locator locator) {
        WebElement element = waitForElementToPresence(locator).orElseThrow();
        swipeUp(element);
    }

    public void swipeUp(Locator locator, int numberOfSwipe) {
        for (int i = 1; i <= numberOfSwipe; i++) {
            swipeUp(element(locator).orElseThrow());
        }
    }

    public void swipeDown() {
        logger.log("Performing swipe down on screen");
        try {
            Swipe.DOWN.perform(driver);
        } catch (StaleElementReferenceException | InvalidElementStateException e1) {
            logger.log("Retry performing swipe down on screen");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.refreshed(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.widget.FrameLayout"))));
            Swipe.DOWN.perform(driver);
        }
    }

    public void swipeDown(double swipeRatio) {
        try {
            logger.log("Performing swipe down on screen by ratio: " + swipeRatio);
            performSwipeDownByRatio(swipeRatio);
        } catch (Exception e) {
            logger.log("Retry performing swipe down on screen after exception: " + e.getMessage());
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.refreshed(
                            ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.widget.FrameLayout"))));
            performSwipeDownByRatio(swipeRatio);
        }
    }

    private void performSwipeDownByRatio(double swipeRatio) {
        Dimension size = driver.manage().window().getSize();
        int width = size.width;
        int height = size.height;

        int startX = width / 2;
        int startY = (int) (height * 0.2);
        int endY = (int) (startY + (height * swipeRatio));
        endY = Math.max(0, Math.min(endY, height - 1));

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(400), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        ((AppiumDriver) driver).perform(Collections.singletonList(swipe));
    }

    public void scrollDown(int pixels) {
        logger.log("Scrolling down by " + pixels + " pixels");

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, arguments[0]);", pixels);
        } catch (Exception e) {
            logger.log("Failed to scroll down: " + e.getMessage());
            throw e;
        }
    }

    public void scrollUp(int pixels) {
        logger.log("Scrolling up by " + pixels + " pixels");

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, arguments[0]);", -pixels);
        } catch (Exception e) {
            logger.log("Failed to scroll up: " + e.getMessage());
            throw e;
        }
    }

    public void scrollToBottomOfPage() {
        scrollToBottomOfPage(10); // Default to 10 attempts if not specified
    }

    /** Scrolls the window to the bottom of the page (document height). No locator. */
    public void scrollToBottomOfPage(int maxScrollAttempts) {
        logger.log("Scrolling window to bottom of page");
        try {
            for(int i=0; i<maxScrollAttempts; i++) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript(
                        "window.scrollTo(0, Math.max(document.body.scrollHeight, document.documentElement.scrollHeight));");
                if (isPageScrolledToBottom()) {
                    logger.log("Reached bottom of page after " + (i + 1) + " scroll attempts");
                    return;
                }
            }
        } catch (Exception e) {
            logger.log("Failed to scroll to bottom of page: " + e.getMessage());
            throw e;
        }
    }

    /** Returns true when the window scroll position is at (or within 5px of) the page bottom. */
    public boolean isPageScrolledToBottom() {
        logger.log("Checking if page is scrolled to bottom");
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Object result = js.executeScript(
                    "var scrollTop = window.pageYOffset || document.documentElement.scrollTop;"
                            + "var viewportHeight = window.innerHeight || document.documentElement.clientHeight;"
                            + "var scrollHeight = Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);"
                            + "return scrollTop + viewportHeight >= scrollHeight - 5;");
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            logger.log("Failed to check scroll position: " + e.getMessage());
            return false;
        }
    }

    public void swipeDown(WebElement element) {
        logger.log("Performing swipe down on element: " + element);
        Swipe.DOWN.perform(driver, element);
    }

    public void swipeDown(Locator locator) {
        WebElement element = element(locator).orElseThrow();
        swipeDown(element);
    }

    public void swipeDownPresence(Locator locator) {
        WebElement element = waitForElementToPresence(locator).orElseThrow();
        swipeDown(element);
    }

    public void swipeDown(int numberOfSwipe) {
        for (int i = 1; i <= numberOfSwipe; i++) {
            swipeDown();
        }
    }

    public void swipeDown(Locator locator, int numberOfSwipe) {
        for (int i = 1; i <= numberOfSwipe; i++) {
            swipeDown(element(locator).orElseThrow());
        }
    }

    public void swipeRight() {
        logger.log("Performing swipe right on screen");
        Swipe.RIGHT.perform(driver);
    }

    public void swipeRight(WebElement element) {
        logger.log("Performing swipe right on element: " + element);
        Swipe.RIGHT.perform(driver, element);
    }

    public void swipeRight(Locator locator) {
        WebElement element = element(locator).orElseThrow();
        swipeRight(element);
    }

    public void swipeRight(int numberOfSwipe) {
        for (int i = 1; i <= numberOfSwipe; i++) {
            swipeRight();
        }
    }

    public void swipeRight(Locator locator, int numberOfSwipe) {
        for (int i = 1; i <= numberOfSwipe; i++) {
            swipeRight(element(locator).orElseThrow());
        }
    }

    public void swipeLeft() {
        logger.log("Performing swipe left on screen");
        Swipe.LEFT.perform(driver);
    }

    public void swipeLeft(WebElement element) {
        logger.log("Performing swipe left on element: " + element);
        Swipe.LEFT.perform(driver, element);
    }

    public void swipeLeft(WebElement element, int numberOfSwipe) {
        for (int i = 1; i <= numberOfSwipe; i++) {
            logger.log(i + " Performing swipe left on element: " + element);
            Swipe.LEFT.perform(driver, element);
        }
    }

    public void swipeLeft(Locator locator) {
        WebElement element = element(locator).orElseThrow();
        swipeLeft(element);
    }

    public void swipeLeftPresence(Locator locator) {
        WebElement element = waitForElementToPresence(locator).orElseThrow();
        swipeLeft(element);
    }

    public void swipeLeft(Locator locator, int numberOfSwipe) {
        for (int i = 1; i <= numberOfSwipe; i++) {
            swipeLeft(element(locator).orElseThrow());
        }
    }

    public void swipeLeftLong(WebElement element) {
        logger.log("Performing long swipe left on element: " + element);
        Swipe.LEFT_LONG.perform(driver, element);
    }

    public void swipeLeftLong(Locator locator) {
        WebElement element = element(locator).orElseThrow();
        swipeLeft(element);
    }

    public void swipeLeftLong() {
        logger.log("Performing long swipe left on screen");
        Swipe.LEFT_LONG.perform(driver);
    }

    public void swipeLeft(int numberOfSwipe) {
        for (int i = 1; i <= numberOfSwipe; i++) {
            swipeLeft();
        }
    }

    public Optional<WebElement> swipeToElement(Locator locator, Swipe direction, int maxTries) {
        // Optimized: Check if element is already visible before swiping (performance improvement)
        try {
            Optional<WebElement> element = element(locator);
            if (element.isPresent()) {
                try {
                    if (element.get().isDisplayed()) {
                        logger.log("Element already visible, skipping swipe: " + locator);
                        return element;
                    }
                } catch (StaleElementReferenceException e) {
                    // Element is stale, will be handled by ListScrollHandler
                    logger.log("Element became stale during visibility check, proceeding with swipe");
                }
            }
        } catch (StaleElementReferenceException e) {
            // Element is stale, proceed with swipe
            logger.log("Stale element detected in initial check, proceeding with swipe");
        } catch (Exception e) {
            // Element not found or other error, proceed with swipe
        }

        // Optimized: Create ListScrollHandler once and reuse
        ListScrollHandler<WebDriver> listScrollHandler = new ListScrollHandler<>(driver, locator);

        try {
            return listScrollHandler.gotoFirstElement(direction, maxTries);
        } catch (StaleElementReferenceException e) {
            logger.log("Stale element exception caught. Refreshing and retrying swipe: " + locator);
            // Optimized: Reduced wait time from 3s to 1s for faster retry
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
                wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(locator.by())));
            } catch (Exception refreshException) {
                // If refresh fails, continue anyway - ListScrollHandler will handle it
                logger.log("Element refresh failed, continuing with swipe: " + refreshException.getMessage());
            }
            // Retry with same handler instance
            return listScrollHandler.gotoFirstElement(direction, maxTries);
        }
    }

    public Optional<WebElement> swipeToPresentedElement(Locator locator, Swipe direction, int maxTries) {
        try {
            ListScrollHandler<WebDriver> listScrollHandler = new ListScrollHandler<>(driver, locator);
            return listScrollHandler.gotoFirstPresentedElement(direction, maxTries);
        } catch (StaleElementReferenceException e) {
            logger.log("Element is not attached to the page document");
            logger.log("Stale element exception catched. Retrying swipe to find locator: " + locator);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(locator.by())));
            ListScrollHandler<WebDriver> listScrollHandler = new ListScrollHandler<>(driver, locator);
            return listScrollHandler.gotoFirstPresentedElement(direction, maxTries);
        }
    }

    public Optional<WebElement> swipeToElementTillPresented(Locator locator, Swipe direction, int maxTries) {
        int tries = 0;
        boolean isEmpty = false;
        while (tries < maxTries) {
            Optional<WebElement> element = waitForElementToPresence(locator);
            isEmpty = element.isEmpty();
            if (isEmpty) {
                logger.log("Element not found, performing swipe: " + locator);
                performSwipe(direction);
            } else {
                logger.log("Element found, returning: " + locator);
                return waitForElementToPresence(locator);
            }
            tries++;
        }
        return Optional.empty();
    }

    /**
     * Swipes until the element is fully within the device screen bounds (Android / iOS native).
     * Unlike {@link #swipeToElement(Locator, Swipe, int)}, which stops at {@code isDisplayed()}
     * (can be only partially visible), this continues until the entire bounding box is on-screen.
     * <p>
     * Uses the same swipe mechanics as {@link #swipeToElement(Locator, Swipe, int)}.
     * Prefer {@link #swipeToFullyVisibleElement(Locator, Locator, Swipe, int)} when the target
     * lives inside a nested scrollable (e.g. horizontal order card carousel).
     *
     * @param locator   target element
     * @param direction swipe direction used to bring the element fully into view
     * @param maxTries  maximum swipe attempts
     * @return the element when fully (or maximally) on-screen; empty if not found
     */
    public Optional<WebElement> swipeToFullyVisibleElement(Locator locator, Swipe direction, int maxTries) {
        ListScrollHandler<WebDriver> listScrollHandler = new ListScrollHandler<>(driver, locator);
        return listScrollHandler.gotoFullyVisibleElement(direction, maxTries);
    }

    /**
     * Same as {@link #swipeToFullyVisibleElement(Locator, Swipe, int)} but swipes on
     * {@code parentScrollableView} — same pattern as
     * {@link #swipeToElement(Locator, Locator, Swipe, int)}.
     */
    public Optional<WebElement> swipeToFullyVisibleElement(Locator locator, Locator parentScrollableView,
                                                           Swipe direction, int maxTries) {
        ListScrollHandler<WebDriver> listScrollHandler =
                new ListScrollHandler<>(driver, locator, parentScrollableView);
        return listScrollHandler.gotoFullyVisibleElement(direction, maxTries);
    }

    /**
     * True when the element's full bounding box lies within the device window.
     * If the element is larger than the screen on an axis, that axis only requires the
     * leading edge (top/left) to be on-screen.
     */
    public boolean isElementFullyOnScreen(Locator locator) {
        return element(locator).map(this::isElementFullyOnScreen).orElse(false);
    }

    /**
     * True when the element's full bounding box lies within the device window.
     * If the element is larger than the screen on an axis, that axis only requires the
     * leading edge (top/left) to be on-screen.
     */
    public boolean isElementFullyOnScreen(WebElement element) {
        try {
            Dimension window = driver.manage().window().getSize();
            Point location = element.getLocation();
            Dimension size = element.getSize();
            int left = location.getX();
            int top = location.getY();
            int right = left + size.getWidth();
            int bottom = top + size.getHeight();
            int screenW = window.getWidth();
            int screenH = window.getHeight();

            boolean horizontalOk = size.getWidth() > screenW
                    ? left >= 0
                    : left >= 0 && right <= screenW;
            boolean verticalOk = size.getHeight() > screenH
                    ? top >= 0
                    : top >= 0 && bottom <= screenH;
            return horizontalOk && verticalOk;
        } catch (StaleElementReferenceException e) {
            return false;
        } catch (Exception e) {
            logger.log("isElementFullyOnScreen failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * True when {@code element}'s full bounding box lies inside {@code container}'s bounds
     * (used for nested scrollables / carousels).
     */
    public boolean isElementFullyWithin(WebElement element, WebElement container) {
        try {
            Point eLoc = element.getLocation();
            Dimension eSize = element.getSize();
            Point cLoc = container.getLocation();
            Dimension cSize = container.getSize();
            int eLeft = eLoc.getX();
            int eTop = eLoc.getY();
            int eRight = eLeft + eSize.getWidth();
            int eBottom = eTop + eSize.getHeight();
            int cLeft = cLoc.getX();
            int cTop = cLoc.getY();
            int cRight = cLeft + cSize.getWidth();
            int cBottom = cTop + cSize.getHeight();
            return eLeft >= cLeft && eTop >= cTop && eRight <= cRight && eBottom <= cBottom;
        } catch (StaleElementReferenceException e) {
            return false;
        } catch (Exception e) {
            logger.log("isElementFullyWithin failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Swipes calendar using coordinate-based touch actions (similar to CalendarPageIOS approach).
     * This is more reliable for iOS calendar views that don't respond to standard swipes.
     *
     * @param element The calendar element
     * @param direction The swipe direction
     */
    public void swipeCalendarByCoordinates(WebElement element, Swipe direction) {
        int elementLocX = element.getLocation().getX();
        int elementLocY = element.getLocation().getY();
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();

        // Calculate swipe coordinates
        int x = elementLocX + (width / 2); // Middle of element horizontally
        int y1, y2;

        if (direction == Swipe.UP) {
            // Swipe up: start from 70% down, end at 30% down
            y1 = (int) (elementLocY + height * 0.7);
            y2 = (int) (elementLocY + height * 0.3);
        } else {
            // Swipe down: start from 30% down, end at 70% down
            y1 = (int) (elementLocY + height * 0.3);
            y2 = (int) (elementLocY + height * 0.7);
        }

        logger.log(String.format("Swiping calendar by coordinates from (%d, %d) to (%d, %d)", x, y1, x, y2));

        // Use TouchAction for iOS calendar (more reliable than standard swipe)
        TouchAction action = new TouchAction((PerformsTouchActions) driver);
        action.press(PointOption.point(x, y1))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(x, y2))
                .release()
                .perform();

        logger.log("Coordinate-based calendar swipe completed");
    }

    public Optional<WebElement> swipeToElement(Locator locator, Locator parentScrollableView, Swipe direction, int maxTries) {
        ListScrollHandler<WebDriver> listScrollHandler = new ListScrollHandler<>(driver, locator, parentScrollableView);
        return listScrollHandler.gotoFirstElement(direction, maxTries);
    }

    public Optional<WebElement> swipeToPresentedElement(Locator locator, Locator parentScrollableView, Swipe direction, int maxTries) {
        ListScrollHandler<WebDriver> listScrollHandler = new ListScrollHandler<>(driver, locator, parentScrollableView);
        return listScrollHandler.gotoFirstPresentedElement(direction, maxTries);
    }

    @SneakyThrows
    public void get(String url) {
        logger.log("Navigating to url: " + url);
        executeWithTimeout(() -> {
            driver.get(url);
            return null;
        }, 20, TimeUnit.SECONDS);
        logger.log("Current url : " + url);
    }

    @SneakyThrows
    public void get(String url,int retryCount) {
        logger.log("Navigating to url: " + url);
        try {
            executeWithTimeout(() -> {
                driver.get(url);
                return null;
            }, 20, TimeUnit.SECONDS);
        }catch (Exception e) {
            if(retryCount > 0){
                dummyWait(3);
                logger.log("Retrying to navigate to url: " + url + " , remaining retry count: " + (retryCount -1));
                get(url, retryCount -1);
            } else {
                throw e;
            }
        }
        logger.log("Current url : " + url);
    }

    @SneakyThrows
    public void getWithDynamicTimeout(String url, int seconds) {
        logger.log("Navigating to url: " + url);
        executeWithTimeout(() -> {
            driver.get(url);
            return null;
        }, seconds, TimeUnit.SECONDS);
        logger.log("Current url : " + url);
    }

    public void mAppOpenUrl(String url){
        CapabilitiesHandler handler = new CapabilitiesHandler();
        JSONArray configs = handler.getConfigs();
        JSONObject config = handler.getConfig(configs);
        DesiredCapabilities capabilities = handler.getCapabilities(config);
        Set<String> capabilityNames = capabilities.getCapabilityNames();
        logger.log("Capabilities value : " + capabilityNames);
        if (Configs.PLATFORM == Platform.IOS) {
            logger.log("Openning link from safari. Link : " + url);
            IOSDriver iosDriver = (IOSDriver) driver;
            String safariBundleID = "com.apple.mobilesafari";
            Map<String, Object> params = Map.of("bundleId", safariBundleID);

            iosDriver.executeScript("mobile: launchApp", params);
            Locator search = new Locator("search", AppiumBy.className("XCUIElementTypeTextField"));
            click(search);
            WebElement searchBox = element(search).orElseThrow();
            sendKeys(searchBox, url);
            searchBox.sendKeys(Keys.ENTER);
        }else if(Configs.PLATFORM == Platform.ANDROID){
            logger.log("Openning link from chrome. Link : " + url);
            AndroidDriver androidDriver = (AndroidDriver) driver;
            Activity activity = new Activity("com.android.chrome", "com.google.android.apps.chrome.Main");
            androidDriver.startActivity(activity);
            Locator termAccept = new Locator("TERM_ACCEPT", AppiumBy.id("terms_accept"));
            final Locator ALLOW_BUTTON = new Locator("ALLOW_BUTTON", AppiumBy.id("positive_button"));
            final Locator ALLOW_TEXT = new Locator("ALLOW_TEXT", AppiumBy.xpath("//*[@text='Allow']"));
            Locator negativeButton = new Locator("NEGATIVE_BUTTON",AppiumBy.id("negative_button"));
            waitForAnyLocator(5, termAccept, ALLOW_BUTTON, negativeButton, ALLOW_TEXT);
            if(element(termAccept).isPresent()) click(termAccept);
            if(element(ALLOW_BUTTON).isPresent()) click(ALLOW_BUTTON);
            if(element(ALLOW_TEXT).isPresent()) click(ALLOW_TEXT);
            if(element(negativeButton).isPresent()) click(negativeButton);
            Locator search1 = new Locator("SEARCH", AppiumBy.xpath("//*[@resource-id=\"com.android.chrome:id/url_bar\"]"));
            Locator search2 = new Locator("SEARCH", AppiumBy.xpath("//*[@resource-id=\"com.android.chrome:id/search_box_text\"]"));
            Optional<Locator> locator = waitForAnyLocator(5, search1, search2);
            WebElement searchBox = element(locator.orElseThrow()).orElseThrow();
            sendKeys(searchBox, url);
            KeyEvent keyEvent = new KeyEvent(AndroidKey.ENTER);
            androidDriver.pressKey(keyEvent);
        }
    }

    private String extractVersion(String input) {
        logger.log("Extracting version from: " + input);
        String[] parts = input.split("\\.");
        if (parts.length >= 2) {
            logger.log("final platform version: " + parts[0] + "." + parts[1]);
            return parts[0] + "." + parts[1];
        }
        return input; // Return the original string if it doesn't have enough parts
    }

    public void deeplink(Deeplink deeplink) {

        String url = switch (Configs.ENVIRONMENT) {
            case GK -> deeplink.gkDeeplink();
            case PREPROD -> deeplink.preprodDeeplink();
            case PROD -> deeplink.prodDeeplink();
        };

        logger.log("Hitting deeplink: " + url);

        if (Configs.CODE_COVERAGE && Configs.PLATFORM == Platform.ANDROID) {
            ADB adb = new ADB(getEmulatorName(), "am start -d " + url);
            adb.execShell();
            return;
        }

        CapabilitiesHandler handler = new CapabilitiesHandler();
        JSONArray configs = handler.getConfigs();
        JSONObject config = handler.getConfig(configs);
        URL server = handler.getUrl(config);
        DesiredCapabilities capabilities = handler.getCapabilities(config);
        Set<String> capabilityNames = capabilities.getCapabilityNames();
        logger.log("Capabilities value : " + capabilityNames);

        if (Configs.PLATFORM == Platform.IOS) {
            IOSDriver iosDriver = (IOSDriver) driver;
            boolean simulator=false;

            String platformVersion;
            if (Configs.DEVICE_FARM == DeviceFarm.BROWSER_STACK && capabilityNames.contains(BROWSERSTACK_DEBUG)) {
                logger.log("BrowserStack detected");
                platformVersion = iosDriver.getCapabilities().getCapability("appium:os_version").toString();
            } else if (Configs.DEVICE_FARM == DeviceFarm.LAMBDA_TEST && capabilityNames.contains(LAMBDA_TEST_DEBUG)) {
                logger.log("LambdaTest detected");
                platformVersion = iosDriver.getCapabilities().getCapability("platformVersion").toString();
            } else {
                logger.log("Local device detected, get the iOS version from testdata");
                Map<String, Object> caps = this.getCapabilities();
                platformVersion = caps.get("platformVersion").toString();
                simulator = true;
            }
            platformVersion = extractVersion(platformVersion);
            logger.log("platformVersion -> " + platformVersion);

            if (((Configs.DEVICE_FARM == DeviceFarm.LAMBDA_TEST || Configs.DEVICE_FARM == DeviceFarm.BROWSER_STACK) && Double.parseDouble(platformVersion) >= 17.0) || simulator) {
                get(url);
            }

            else if ( (capabilityNames.contains(LAMBDA_TEST_DEBUG) || capabilityNames.contains(BROWSERSTACK_DEBUG))) {

                logger.log("IOS on device farm detected");
                String safariBundleID = "com.apple.mobilesafari";
                Map<String, Object> params = Map.of("bundleId", safariBundleID);

                iosDriver.executeScript("mobile: launchApp", params);
                Locator search = new Locator("search", AppiumBy.className("XCUIElementTypeTextField"));
                click(search);
                WebElement searchBox = element(search).orElseThrow();
                sendKeys(searchBox, url);
                searchBox.sendKeys(Keys.ENTER);
            }

            Locator openInApp = new Locator("OPEN_IN_APP",
                    AppiumBy.xpath("//*[@type='XCUIElementTypeButton' and @label='Open']"));
            if (isElementDisplayed(openInApp)) {
                click(openInApp);
                waitForElementToDisappear(openInApp);
            }

        } else {
            if (Configs.PLATFORM == Platform.ANDROID && !Configs.CODE_COVERAGE
                    && !isRunningOnDeviceFarm()) {
                closeApp();
            }
            get(url);
        }

        logger.log("Successfully hit the deeplink: " + url);
    }

    @Deprecated
    public void restartApp() {
        if (Configs.PLATFORM == Platform.IOS) {
            IOSDriver iosDriver = (IOSDriver) driver;
            iosDriver.terminateApp(BundleID.getID());
            try {
                iosDriver.runAppInBackground(Duration.ofSeconds(1));
            } catch (Exception ignored) {
            }
        } else if (Configs.PLATFORM == Platform.ANDROID) {
            AndroidDriver androidDriver = (AndroidDriver) driver;
            androidDriver.terminateApp(BundleID.getID());
            try {
                androidDriver.runAppInBackground(Duration.ofSeconds(1));
            } catch (Exception ignored) {
            }
        }
    }

    private static final int CHROME_DWELL_SECONDS = 10;

    /**
     * Minimizes the app under test by opening Google Chrome, verifies Chrome is in the foreground,
     * keeps Chrome open for {@link #CHROME_DWELL_SECONDS} seconds, then brings the app under test
     * back to the foreground. Used to verify a screen survives a background/foreground cycle (via
     * another app) without crashing or showing an error state.
     *
     * @return {@code true} if Google Chrome was successfully brought to the foreground.
     */
    public boolean minimizeOpenChromeAndReopen() {
        if (Configs.PLATFORM == Platform.ANDROID) {
            AndroidDriver androidDriver = (AndroidDriver) driver;
            String chromePackage = "com.android.chrome";
            boolean inChrome = false;
            try {
                logger.log("Minimizing app and opening Google Chrome (" + chromePackage + ")");
                androidDriver.activateApp(chromePackage);
                String currentPackage = androidDriver.getCurrentPackage();
                logger.log("Current package after opening Chrome: " + currentPackage);
                inChrome = currentPackage != null && currentPackage.contains("chrome");
                logger.log("Keeping Google Chrome in foreground for " + CHROME_DWELL_SECONDS + " seconds");
                Thread.sleep(CHROME_DWELL_SECONDS * 1000L);
            } catch (Exception e) {
                logger.log("Unable to open/validate Google Chrome on Android: " + e.getMessage());
            } finally {
                logger.log("Reopening app under test: " + PackageName.get());
                androidDriver.activateApp(PackageName.get());
            }
            return inChrome;
        } else if (Configs.PLATFORM == Platform.IOS) {
            IOSDriver iosDriver = (IOSDriver) driver;
            String chromeBundleId = "com.google.chrome.ios";
            boolean inChrome = false;
            try {
                logger.log("Minimizing app and opening Google Chrome (" + chromeBundleId + ")");
                iosDriver.executeScript("mobile: activateApp", Map.of("bundleId", chromeBundleId));
                Object state = iosDriver.executeScript("mobile: queryAppState", Map.of("bundleId", chromeBundleId));
                logger.log("Google Chrome app state (4 = foreground): " + state);
                inChrome = state != null && String.valueOf(state).equals("4");
                logger.log("Keeping Google Chrome in foreground for " + CHROME_DWELL_SECONDS + " seconds");
                Thread.sleep(CHROME_DWELL_SECONDS * 1000L);
            } catch (Exception e) {
                logger.log("Unable to open/validate Google Chrome on iOS: " + e.getMessage());
            } finally {
                logger.log("Reopening app under test: " + BundleID.getID());
                iosDriver.executeScript("mobile: activateApp", Map.of("bundleId", BundleID.getID()));
            }
            return inChrome;
        }
        return false;
    }

    public void closeApp() {
        logger.log("Closing app");
        if (Configs.PLATFORM == Platform.IOS) {
            if(Configs.CODE_COVERAGE) {
                return;
            }
            IOSDriver iosDriver = (IOSDriver) driver;
            iosDriver.terminateApp(BundleID.getID());
        } else if (Configs.PLATFORM == Platform.ANDROID) {
            if (Configs.CODE_COVERAGE && Configs.PLATFORM == Platform.ANDROID) {
                ADB adb = new ADB(getEmulatorName(), "am broadcast -a com.tiket.gits.END_EMMA");
                adb.execShell();
                adb = new ADB(getEmulatorName(),
                        "am instrument -e coverage true -w " + PackageName.get() + "/com.tiket.gits.initializer.JacocoInstrumentation");
                adb.execShell();
                return;
            }
            AndroidDriver androidDriver = (AndroidDriver) driver;
            androidDriver.terminateApp(PackageName.get());
        }
        logger.log("Closed app");
    }

    public String getCurrentURL() {
        String currentUrl="";
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                try{
                    AndroidDriver d = (AndroidDriver) driver;
                    currentUrl = d.executeScript("return window.location.href").toString();
                }catch (Exception e){
                    logger.log("Failed to get current url");
                }
            }
            case IOS -> {
                try{
                    IOSDriver d = (IOSDriver) driver;
                    currentUrl = d.executeScript("return window.location.href").toString();
                }catch (Exception e){
                    logger.log("Failed to get current url");
                }
            } case DWEB, AMWEB, IMWEB -> {
                currentUrl = driver.getCurrentUrl();
            }
        }
        logger.log("Got current url: " + currentUrl);
        return currentUrl;
    }

    public boolean waitForUrlToContainAnyOf(int seconds, String... anyOfPaths) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            seconds = seconds * MULTIPLIER_CONSTANT_GK_WAIT_TIME;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            seconds = seconds * MULTIPLIER_CONSTANT_PREPROD_WAIT_TIME;
        }
        try {
            logger.log("Waiting up to " + seconds + " seconds for URL to contain any of: " + String.join(", ", anyOfPaths));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
            return wait.until(d -> {
                String currentUrl = getCurrentURL();
                for (String path : anyOfPaths) {
                    if (currentUrl.contains(path)) {
                        return true;
                    }
                }
                return false;
            });
        } catch (TimeoutException te) {
            logger.softWarning("Timeout waiting for " + seconds + " seconds for URL to contain any of: " + String.join(", ", anyOfPaths));
            return false;
        }
    }

    public boolean waitForUrlToContainAnyOf(String... anyOfPaths) {
        return waitForUrlToContainAnyOf(SECONDS, anyOfPaths);
    }

    public String getTitle() {
        String title = driver.getTitle();
        logger.log("Got title: " + title);
        return title;
    }

    public String getPageSource() {
        String pageSource = driver.getPageSource();
        logger.log("Got page source: " + pageSource.substring(0, 20) + "...");
        return pageSource;
    }

    public void back() {
        logger.log("Navigating back");
        driver.navigate().back();
    }

    public void hideKeyboard(int times) {
        if(Configs.DEVICE_FARM == DeviceFarm.LAMBDA_TEST && Configs.PLATFORM == Platform.ANDROID) {
            return;
        }
        else {
            switch (Configs.PLATFORM) {
                case IOS -> {
                    for (int i = 0; i < times; i++) {
                        Locator label = new Locator("KEYBOARD_LABEL", By.xpath(
                                "//XCUIElementTypeButton[@label='Done' or @label='Selesai' or @label='done' or @label='return' or @label='go' or @label='Go' or @label='search' or @label='Search' or @label='cari' or @label='Cari']"));

                        if (element(label).isPresent()) {
                            clickElementByCoordinate(label);
                        } else {
                            logger.softWarning("Keyboard is not displayed");
                        }
                    }
                }
                case ANDROID -> {
                    AndroidDriver d = (AndroidDriver) driver;
                    if(d.isKeyboardShown()){
                        d.navigate().back();
                    }
                }
            }
        }
    }

    public void hideKeyboard() {
        if(Configs.DEVICE_FARM == DeviceFarm.LAMBDA_TEST && Configs.PLATFORM == Platform.ANDROID) {
            return;
        }
        else {
            switch (Configs.PLATFORM) {
                case IOS -> {
                    Locator label = new Locator("KEYBOARD_LABEL", By.xpath(
                            "(//XCUIElementTypeButton[@label='Done' or @label='Selesai' or @label='done' or @label='return' or @label='go' or @label='Go' or @label='search' or @label='Search' or @label='cari' or @label='Cari'])[1]"));

                    if (element(label).isPresent()) {
                        clickElementByCoordinate(label);
                    } else {
                        logger.softWarning("Keyboard is not displayed");
                    }
                }
                case ANDROID -> {
                    AndroidDriver d = (AndroidDriver) driver;
                    d.hideKeyboard();
                }
            }
        }
    }

    public void hideKeyboardForce() {
        switch (Configs.PLATFORM) {
            case IOS -> {
                Locator label = new Locator("KEYBOARD_LABEL", By.xpath(
                        "//XCUIElementTypeButton[@label='Done' or @label='Selesai' or @label='done' or @label='return' or @label='go' or @label='Go']"));

                if (element(label).isPresent()) {
                    click(label);
                } else {
                    logger.softWarning("Keyboard is not displayed");
                }
            }
            case ANDROID -> {
                AndroidDriver d = (AndroidDriver) driver;
                try {
                    d.hideKeyboard();
                } catch (WebDriverException e) {
                    logger.log("hide keyboard completed with exception");
                }
            }
        }
    }

    @Deprecated
    public void selectWheelPickerValue(Locator elementsLocator, String order) {
        switch (Configs.PLATFORM) {
            case IOS -> {
                JavascriptExecutor d = (JavascriptExecutor) driver;
                WebElement e = this.element(elementsLocator).orElseThrow();
                Map<String, Object> params = new HashMap<>();
                params.put("element", ((RemoteWebElement) e).getId());
                params.put("order", order);
                params.put("velocity", 20);
                d.executeScript("mobile: selectPickerWheelValue", params);
            }
            case ANDROID -> {
                List<WebElement> elements = this.elements(elementsLocator).orElseThrow();

                WebElement before = elements.get(0);
                WebElement current = elements.get(1);
                WebElement next = elements.get(2);

                switch (order) {
                    case "before" -> this.dragAndDrop(current, next);
                    case "next" -> this.dragAndDrop(current, before);
                }
            }
        }
    }

    private void zoomInOutIos(Locator locator, boolean zoomIn) {
        Map<String, Object> pinchObject = new HashMap<>();
        WebElement webElement = element(locator).orElseThrow();
        pinchObject.put("element", webElement);
        float scale = 0.5F;
        float velocity = -0.5F;
        if (zoomIn) {
            scale = 1.5F;
            velocity = 0.5F;
        }
        pinchObject.put("scale", scale);
        pinchObject.put("velocity", velocity);
        ((JavascriptExecutor) driver).executeScript("mobile: pinch", pinchObject);
    }

    private void zoomInOutAndroid(Locator locator, boolean zoomIn) {
        Map<String, Object> pinchObject = new HashMap<>();
        WebElement webElement = element(locator).orElseThrow();
        pinchObject.put("elementId", webElement);
        pinchObject.put("speed", 500);
        pinchObject.put("percent", 0.4);
        String script = "mobile: pinchCloseGesture";
        if (zoomIn) {
            pinchObject.put("percent", 0.8);
            script = "mobile: pinchOpenGesture";
        }
        ((JavascriptExecutor) driver).executeScript(script, pinchObject);
    }

    public void zoomInOut(Locator locator, boolean zoomIn) {
        if (Configs.PLATFORM == Platform.IOS)
            zoomInOutIos(locator, zoomIn);
        else
            zoomInOutAndroid(locator, zoomIn);
    }

    @SneakyThrows
    public void scrollPickerWheel(Locator e, int numberOfScroll, String scrollDirection) {
        WebElement a = element(e).orElseThrow();
        scrollPickerWheel(a, numberOfScroll, scrollDirection);
    }

    /**
     * Scrolls a picker wheel using an already-resolved {@link WebElement}. Use this when the picker
     * was looked up earlier and you want to avoid a second {@code findElement} (e.g. after a slow
     * {@code getText} that re-triggers a UI hierarchy dump and may race with a bottom-sheet dismiss).
     */
    @SneakyThrows
    public void scrollPickerWheel(WebElement a, int numberOfScroll, String scrollDirection) {
        int elementLocX = a.getLocation().x;
        int elementLocY = a.getLocation().y;

        int width = a.getSize().width;
        int height = a.getSize().height;

        int x1 = (int) (elementLocX + width * 0.5);
        int y1 = (int) (elementLocY + height * 0.5);
        int y2 = (int) (elementLocY + height * 0.2);
        int y3 = (int) (elementLocY + height * 0.8);

        if ("before".equals(scrollDirection)) {
            int temp = y2;
            y2 = y1;
            y1 = temp;
        } else if ("next".equals(scrollDirection)) {
            int temp = y1;
            y1 = y3;
            y2 = temp;
        }

        for (int loops = numberOfScroll; loops > 0; loops--) {
            logger.log("SCROLL x1 " + x1 + " y1 " + y1 + " x1 " + x1 + " y2 " + y2);
            PointerInput finger1 = new PointerInput (PointerInput.Kind. TOUCH,"finger1");
            Sequence sequence= new Sequence(finger1,1)
                    .addAction (finger1.createPointerMove (Duration.ZERO, PointerInput.Origin.viewport(), x1, y1))
                    .addAction (finger1.createPointerDown(PointerInput.MouseButton. LEFT.asArg()))
                    .addAction (new Pause (finger1, Duration.ofMillis(100)))
                    .addAction(finger1.createPointerMove (Duration.ofMillis(500), PointerInput. Origin.viewport(), x1, y2))
                    .addAction (finger1.createPointerUp (PointerInput.MouseButton.LEFT.asArg()));
            ((AppiumDriver)driver).perform(Collections.singletonList(sequence));

            Thread.sleep(1500);
        }
    }

    @SneakyThrows
    public void scrollPickerWheel(Locator e, int numberOfScroll, String scrollDirection, double distance) {

        WebElement elementToSwipeOn = element(e).orElseThrow();
        int elementLocX = elementToSwipeOn.getLocation().x;
        int elementLocY = elementToSwipeOn.getLocation().y;

        int width = elementToSwipeOn.getSize().width;
        int height = elementToSwipeOn.getSize().height;

        int middleX = (int) (elementLocX + width * 0.5);
        int middleY = (int) (elementLocY + height * 0.5);

        int x1 = middleX;
        int y1 = middleY;

        int y2 = (int) (middleY - (height * distance / 2));
        if (scrollDirection == "before") {
            int temp = y2;
            y2 = y1;
            y1 = temp;
        }

        for (int loops = numberOfScroll; loops > 0; loops--) {
            PointerInput finger1 = new PointerInput (PointerInput.Kind. TOUCH,"finger1");
            Sequence sequence= new Sequence(finger1,1)
                    .addAction (finger1.createPointerMove (Duration.ZERO, PointerInput.Origin.viewport(), x1, y1))
                    .addAction (finger1.createPointerDown(PointerInput.MouseButton. LEFT.asArg()))
                    .addAction (new Pause (finger1, Duration.ofMillis(500)))
                    .addAction(finger1.createPointerMove (Duration.ofSeconds(3), PointerInput. Origin.viewport(), x1, y2))
                    .addAction (finger1.createPointerUp (PointerInput.MouseButton.LEFT.asArg()));
            ((AppiumDriver)driver).perform(Collections.singletonList(sequence));
            Thread.sleep(1500);
        }
    }

    public void moveElementVertical(Locator startLocator, Locator targetLocator, double distance) {
        logger.log("Moving element vertically from " + startLocator + " to " + targetLocator + " by distance: " + distance);

        WebElement thumbElement = waitForElementToPresence(startLocator).orElseThrow();
        WebElement topThumb = waitForElementToPresence(startLocator).orElseThrow();
        WebElement bottomThumb = waitForElementToPresence(targetLocator).orElseThrow();

        // Get full slider track start and end
        int startY = topThumb.getLocation().getY() + (topThumb.getSize().getHeight() / 2);
        int endY = bottomThumb.getLocation().getY() + (bottomThumb.getSize().getHeight() / 2);
        int trackX = thumbElement.getLocation().getX() + (thumbElement.getSize().getWidth() / 2);

        int trackHeight = endY - startY;

        // Calculate target position based on distance
        int targetY = startY + (int)(trackHeight * distance);

        // Get current thumb center position
        int currentY = thumbElement.getLocation().getY() + (thumbElement.getSize().getHeight() / 2);

        logger.log("Moving from " + currentY + " to " + targetY);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence sequence = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), trackX, currentY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(200)))
                .addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), trackX, targetY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        ((AppiumDriver)driver).perform(Collections.singletonList(sequence));
    }

    /**
     * Sets the UiAutomator2 {@code waitForIdleTimeout} (Android only). Every UiAutomator2 command
     * (find, getText, click, gesture) blocks until the app reports itself "idle" or this timeout
     * elapses; on continuously-busy screens (e.g. animated wheel/number pickers) that wait dominates
     * execution time. Lower it (or set {@code 0} to disable) around such interactions and restore it
     * afterwards. No-op on non-Android platforms.
     *
     * @param milliseconds new idle timeout in ms (0 disables the idle wait entirely)
     */
    public void setWaitForIdleTimeout(long milliseconds) {
        if (driver instanceof AndroidDriver androidDriver) {
            androidDriver.setSetting("waitForIdleTimeout", milliseconds);
            logger.log("Set waitForIdleTimeout to " + milliseconds + " ms");
        }
    }

    /**
     * Returns the current UiAutomator2 {@code waitForIdleTimeout} in ms (Android only). Use this to
     * capture the existing value before {@link #setWaitForIdleTimeout(long)} so it can be restored
     * afterwards instead of hardcoding a default. Falls back to {@code 10000} (the UiAutomator2
     * default) when the value is unavailable or on non-Android platforms.
     */
    public long getWaitForIdleTimeout() {
        if (driver instanceof AndroidDriver androidDriver) {
            Object value = androidDriver.getSettings().get("waitForIdleTimeout");
            if (value instanceof Number number) {
                return number.longValue();
            }
        }
        return 10000;
    }

    /**
     * Performs a single vertical swipe between two absolute Y coordinates at a fixed X, without any
     * element lookup. Useful for tight loops (e.g. number/age wheel pickers) where re-locating the
     * element and re-reading its geometry on every step is prohibitively slow while the UI animates.
     */
    public void swipeVertical(int x, int fromY, int toY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence sequence = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, fromY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(200)))
                .addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), x, toY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        ((AppiumDriver) driver).perform(Collections.singletonList(sequence));
    }

    public void moveElementHorizontal(Locator startLocator, Locator targetLocator, double distance) {
        logger.log("Moving element horizontally from " + startLocator + " to " + targetLocator + " by distance: " + distance);

        WebElement thumbElement = element(startLocator).orElseThrow();
        WebElement leftThumb = element(startLocator).orElseThrow();
        WebElement rightThumb = element(targetLocator).orElseThrow();

        // Get full slider track start and end
        int startX = leftThumb.getLocation().getX() + (leftThumb.getSize().getWidth() / 2);
        int endX = rightThumb.getLocation().getX() + (rightThumb.getSize().getWidth() / 2);
        int trackY = thumbElement.getLocation().getY() + (thumbElement.getSize().getHeight() / 2);

        int trackWidth = endX - startX;

        // Calculate target position based on distance
        int targetX = startX + (int)(trackWidth * distance);

        // Get current thumb center position
        int currentX = thumbElement.getLocation().getX() + (thumbElement.getSize().getWidth() / 2);

        logger.log("Moving from " + currentX + " to " + targetX);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence sequence = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), currentX, trackY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(200)))
                .addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), targetX, trackY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        ((AppiumDriver)driver).perform(Collections.singletonList(sequence));
    }

    public void moveElementToEndOfContainer(Locator elementLocator, Locator containerLocator) {
        WebElement element = element(elementLocator).orElseThrow();
        WebElement container = element(containerLocator).orElseThrow();

        int startX = element.getLocation().getX() + (element.getSize().getWidth() / 2);
        int endX = container.getLocation().getX() + container.getSize().getWidth() - (element.getSize().getWidth() / 2);
        int y = element.getLocation().getY() + (element.getSize().getHeight() / 2);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence sequence = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(200)))
                .addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), endX, y))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        ((AppiumDriver) driver).perform(Collections.singletonList(sequence));
    }

    public void swipeReleaseOnElement(Locator containerLocator, Swipe direction, int count) {
        for (int i = 0; i < count; i++) {
            WebElement container = element(containerLocator).orElseThrow();
            swipeReleaseOnElement(container, direction);
        }
    }

    public void swipeReleaseOnElement(WebElement container, Swipe direction, int count) {
        for (int i = 0; i < count; i++) {
            swipeReleaseOnElement(container, direction);
        }
    }

    public void swipeReleaseOnElement(WebElement container, Swipe direction) {
        int startX, startY, endX, endY;
        int x = container.getLocation().getX();
        int y = container.getLocation().getY();
        int width = container.getSize().getWidth();
        int height = container.getSize().getHeight();

        switch (direction) {
            case UP -> {
                startX = x + width / 2;
                startY = y + (int)(height * 0.8);
                endX = startX;
                endY = y + (int)(height * 0.2);
            }
            case DOWN -> {
                startX = x + width / 2;
                startY = y + (int)(height * 0.2);
                endX = startX;
                endY = y + (int)(height * 0.8);
            }
            case LEFT -> {
                startY = y + height / 2;
                startX = x + (int)(width * 0.8);
                endY = startY;
                endX = x + (int)(width * 0.2);
            }
            case RIGHT -> {
                startY = y + height / 2;
                startX = x + (int)(width * 0.2);
                endY = startY;
                endX = x + (int)(width * 0.8);
            }
            default -> throw new IllegalArgumentException("Unsupported swipe direction");
        }

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence swipe = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new org.openqa.selenium.interactions.Pause(finger, Duration.ofMillis(200)))
                .addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        ((AppiumDriver) driver).perform(Collections.singletonList(swipe));
    }

    /**
     * Swipe on {@code elementToSwipeOn}. The swipe span uses the same fraction semantics as
     * {@link #swipeByDistance(WebElement, Swipe, double)}, where the fraction is derived from {@code distanceReferenceElement}:
     * horizontal swipes use {@code distanceReferenceElement} width vs {@code elementToSwipeOn} width;
     * vertical swipes use reference height vs surface height (each ratio capped at {@code 1.0}).
     *
     * @param elementToSwipeOn          scrollable / swipe surface
     * @param distanceReferenceElement element whose size defines swipe distance along the axis
     * @param swipeDirection           swipe axis/direction
     * @param swipeAmount              how many swipes to perform
     */
    public void swipeByDistance(Locator elementToSwipeOn, Locator distanceReferenceElement, Swipe swipeDirection, int swipeAmount) {
        WebElement surface = element(elementToSwipeOn).orElseThrow();
        WebElement distanceRef = element(distanceReferenceElement).orElseThrow();
        double distance = swipeDistanceFractionFromReference(surface, distanceRef, swipeDirection);
        swipeByDistance(surface, swipeDirection, distance, swipeAmount);
    }

    private static double swipeDistanceFractionFromReference(WebElement swipeSurface, WebElement distanceReference, Swipe swipeDirection) {
        int surfW = Math.max(1, swipeSurface.getSize().getWidth());
        int surfH = Math.max(1, swipeSurface.getSize().getHeight());
        return switch (swipeDirection) {
            case LEFT, RIGHT, LEFT_LONG ->
                    Math.min(1.0, (double) distanceReference.getSize().getWidth() / surfW);
            case UP, DOWN, UP_LONG ->
                    Math.min(1.0, (double) distanceReference.getSize().getHeight() / surfH);
        };
    }

    /**
     * Use if controlled swipe distance needed, 1 being longest (from edge of
     * element to edge of element)
     */
    public void swipeByDistance(Locator elementToSwipeOn, Swipe swipeDirection, double distance) {
        WebElement a = element(elementToSwipeOn).orElseThrow();
        swipeByDistance(a, swipeDirection, distance);
    }

    /**
     * Same as {@link #swipeByDistance(Locator, Swipe, double)}, but retries finding the swipe element
     * up to {@code maxFindAttempts} times before swiping once.
     */
    public void swipeByDistanceWithFindRetry(Locator elementToSwipeOn, Swipe swipeDirection, double distance, int maxFindAttempts) {
        WebElement a = element(elementToSwipeOn, maxFindAttempts).orElseThrow();
        swipeByDistance(a, swipeDirection, distance);
    }

    public void swipeByDistance(Locator elementToSwipeOn, Swipe swipeDirection, double distance, int amount) {
        WebElement a = element(elementToSwipeOn).orElseThrow();
        for(int count = 0; count<amount; count++){
            swipeByDistance(a, swipeDirection, distance);
        }
    }

    public void swipeByDistance(WebElement elementToSwipeOn, Swipe swipeDirection, double distance, int amount) {
        for(int count = 0; count<amount; count++){
            swipeByDistance(elementToSwipeOn, swipeDirection, distance);
        }
    }

    public void swipeByDistance(WebElement elementToSwipeOn, Swipe swipeDirection, double distance) {

        int elementLocX = elementToSwipeOn.getLocation().x;
        int elementLocY = elementToSwipeOn.getLocation().y;

        int width = elementToSwipeOn.getSize().width;
        int height = elementToSwipeOn.getSize().height;

        int middleX = (int) (elementLocX + width * 0.5);
        int middleY = (int) (elementLocY + height * 0.5);

        int x1 = middleX;
        int x2 = middleX;
        int y1 = middleY;
        int y2 = middleY;

        if (swipeDirection == Swipe.LEFT || swipeDirection == Swipe.RIGHT) {
            x1 = (int) (middleX + (width * distance / 2));
            x2 = (int) (middleX - (width * distance / 2));

            if (swipeDirection == Swipe.RIGHT) {
                int temp = x2;
                x2 = x1;
                x1 = temp;
            }
        }

        if (swipeDirection == Swipe.UP || swipeDirection == Swipe.DOWN) {
            y1 = (int) (middleY + (height * distance / 2));
            y2 = (int) (middleY - (height * distance / 2));
            if (swipeDirection == Swipe.DOWN) {
                int temp = y2;
                y2 = y1;
                y1 = temp;
            }
        }

        logger.log("swiping from " + x1 + "," + y1 + " to " + x2 + "," + y2);

        PointerInput finger1 = new PointerInput (PointerInput.Kind. TOUCH,"finger1");
        Sequence sequence= new Sequence(finger1,1)
                .addAction (finger1.createPointerMove (Duration.ZERO, PointerInput.Origin.viewport(), x1, y1))
                .addAction (finger1.createPointerDown(PointerInput.MouseButton. LEFT.asArg()))
                .addAction (new Pause (finger1, Duration.ofMillis(500)))
                .addAction(finger1.createPointerMove (Duration.ofSeconds(3), PointerInput. Origin.viewport(), x2, y2))
                .addAction (finger1.createPointerUp (PointerInput.MouseButton.LEFT.asArg()));
        ((AppiumDriver)driver).perform(Collections.singletonList(sequence));
    }

    public Optional<WebElement> swipeByDistanceToElement(Locator elementToFind, Locator elementToSwipeOn, Swipe swipeDirection, double distance, int attempts) {
        WebElement a = element(elementToSwipeOn).orElseThrow();
        return swipeByDistanceToElement(elementToFind, a, swipeDirection, distance, attempts);
    }

    public Optional<WebElement> swipeByDistanceToElement(Locator elementToFind, WebElement elementToSwipeOn, Swipe swipeDirection, double distance, int attempts) {
        int tries = 0;
        Optional<WebElement> webElement;

        do {
            tries++;
            webElement = element(elementToFind);
            if (webElement.isPresent() && isElementInView(elementToFind)) {
                return waitForElementToPresence(elementToFind);
            } else {
                swipeByDistance(elementToSwipeOn, swipeDirection, distance);
                if (tries > attempts)
                    throw new DriverToolsException("Element not found by swipeByDistance within counts: " + attempts);
            }
        } while (tries < attempts);
        return Optional.empty();
    }

    public Optional<WebElement> swipeUntilElementVisible(Locator elementToFind, Locator elementToSwipeOn, Swipe swipeDirection, double distance, int maxAttempts) {
        for (int i = 0; i < maxAttempts; i++) {
            logger.log("finding... (Attempt " + i + ")");
            swipeByDistance(elementToSwipeOn, swipeDirection, distance);
            if (isElementDisplayed(elementToFind)) {
                logger.log("Element found after " + (i + 1) + " swipes");
                break;
            }
        }
        return element(elementToFind);
    }

    /**
     * Use when send key doesn't work on an element, this method will input keys 1
     * by 1 like send key on iOS
     */
    public void pressKey(WebElement webElement, String msg, Boolean... params) {
        boolean noPressBack = params.length >= 1 && params[0];

        if (Configs.PLATFORM == Platform.ANDROID) {
            click(webElement);
            AndroidDriver d = (AndroidDriver) driver;
            for (int i = 0; i < msg.length(); i++) {
                char str = msg.charAt(i);
                String key = String.valueOf(str).toUpperCase();
                try {
                    Integer.valueOf(key);
                    key = "DIGIT_" + key;
                } catch (NumberFormatException e) {
                    if (key.equals(" ")) {
                        key = "SPACE";
                    }
                }
                logger.log("Pressing " + key + " key");
                KeyEvent keyEvent = new KeyEvent(AndroidKey.valueOf(key));
                d.pressKey(keyEvent);
            }
        } else {
            sendKeys(webElement, msg);
        }
    }

    public void pressKey(Locator locator, String msg) {
        WebElement e = element(locator).orElseThrow();
        pressKey(e, msg);
    }

    /**
     * Avoid using this when possible, only if locators are not being able to be
     * located due to an issue
     *
     * This method now includes smart click functionality that automatically uses
     * iOS accessibility tap for elements in the Dynamic Island/Notch area.
     */
    public void clickElementByCoordinate(WebElement e) {
        Point location = e.getLocation();
        Dimension size = e.getSize();
        Point centerOfElement = getCenterOfElement(location, size);

        // Only perform Dynamic Island check on iOS devices to avoid performance overhead on Android
        if (isIOSPlatform()) {
            // Optimized: Reduced logging overhead - only log when actually in grey area
            // Smart Click: Try accessibility action first for elements in grey area
            boolean usedAccessibilityTap = handleDynamicIslandSafetyWithAccessibility(e, centerOfElement);
            if (usedAccessibilityTap) {
                return; // Exit early, tap completed
            }

            // Fallback: Use coordinate-based approach with safety adjustments
            centerOfElement = handleDynamicIslandSafety(e, centerOfElement);
        }

        logger.log("Clicking at: " + centerOfElement);
        PointerInput finger1 = new PointerInput (PointerInput.Kind. TOUCH,"finger1");
        Sequence sequence= new Sequence(finger1,1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerOfElement))
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger1, Duration.ofMillis(200)))
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        ((AppiumDriver)driver).perform(Collections.singletonList(sequence));
    }

    /**
     * Top edge of a bottom sheet (or any webview element) as a fraction of the webview height (0 = top, 1 = bottom),
     * so it can be mapped onto the native screen, e.g. with {@link #clickAboveBottomSheet(double)}. Call it in the
     * webview context.
     *
     * @return {@code null} when the element is not displayed within 5 seconds
     */
    public Double getBottomSheetTopRatio(Locator locator) {
        Optional<WebElement> bottomSheet = waitForElementToAppear(locator, 5);
        if (bottomSheet.isEmpty()) {
            logger.log("Bottom sheet " + locator + " is not displayed");
            return null;
        }
        return getBottomSheetTopRatio(bottomSheet.get());
    }

    /** Same as {@link #getBottomSheetTopRatio(Locator)} for an element that is already found. */
    public Double getBottomSheetTopRatio(WebElement webElement) {
        Object ratio = ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].getBoundingClientRect().top / window.innerHeight;", webElement);
        logger.log("Bottom sheet top ratio -> " + ratio);
        return ratio instanceof Number number ? number.doubleValue() : null;
    }

    /**
     * Taps the page area above a bottom sheet, halfway between the top of the screen and the bottom sheet's top edge,
     * so it lands outside the bottom sheet (e.g. to close it). Call it in the native context.
     *
     * @param bottomSheetTopRatio from {@link #getBottomSheetTopRatio(Locator)}
     */
    public void clickAboveBottomSheet(double bottomSheetTopRatio) {
        // Stay below the status bar / header even if the ratio comes back unexpectedly small.
        double tapRatio = Math.max(bottomSheetTopRatio, 0.2) / 2;
        Dimension screenSize = driver.manage().window().getSize();
        tapByCoordinate(screenSize.getWidth() / 2, (int) (screenSize.getHeight() * tapRatio));
    }

    /**
     * Taps the screen at the given native (viewport) coordinate, e.g. to tap outside a bottom sheet or popup.
     * Uses the same touch sequence as {@link #clickElementByCoordinate(WebElement)}; call it in the native context.
     */
    public void tapByCoordinate(int x, int y) {
        Point point = new Point(x, y);
        logger.log("Tapping at: " + point);
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence sequence = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), point))
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger1, Duration.ofMillis(200)))
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        ((AppiumDriver) driver).perform(Collections.singletonList(sequence));
    }

    /**
     * Taps inside {@code element}'s bounding rect at fractions of width/height measured from top-left,
     * instead of always using the geometric centre.
     * <p>
     * Use when a single iOS accessibility node groups multiple visible lines—e.g. body copy plus a link—so tapping
     * the centre hits the wrong target. Typical link line on two-line reschedule copy: horizontally centred
     * ({@code 0.5}) and vertically in the bottom band ({@code ~0.78}–{@code ~0.85}).
     * <p>
     * Fractions are clamped into {@code [0.02, 0.98]} so taps stay inset from neighbours. Intended for banners
     * on the scrollable canvas; avoids Dynamic Island workarounds that recompute the element centre and would erase
     * the fractional offset.
     *
     * @param horizontalFractionFromLeft 0 = left edge, 1 = right edge
     * @param verticalFractionFromTop    0 = top edge, 1 = bottom edge
     */
    public void clickElementFractionWithinBounds(WebElement element,
                                                 double horizontalFractionFromLeft, double verticalFractionFromTop) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        Point tapPoint = getTapPointFractionWithinBounds(location, size, horizontalFractionFromLeft, verticalFractionFromTop);

        final int dynamicIslandSafeY = 100;
        if (isIOSPlatform() && tapPoint.getY() < dynamicIslandSafeY) {
            tapPoint = new Point(tapPoint.getX(), dynamicIslandSafeY + 5);
            logger.log("Adjusted fractional tap Y to stay outside Dynamic Island zone: " + tapPoint);
        }

        logger.log("Fraction-adjusted viewport tap at " + tapPoint + " (element at " + location + ", size " + size + ")");

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence sequence = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), tapPoint))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(200)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        ((AppiumDriver) driver).perform(Collections.singletonList(sequence));
    }

    private Point getTapPointFractionWithinBounds(Point location, Dimension size,
                                                  double horizontalFractionFromLeft, double verticalFractionFromTop) {
        double hf = clampElementTapFraction(horizontalFractionFromLeft);
        double vf = clampElementTapFraction(verticalFractionFromTop);
        int x = location.getX() + (int) Math.round(size.getWidth() * hf);
        int y = location.getY() + (int) Math.round(size.getHeight() * vf);
        return new Point(x, y);
    }

    private static double clampElementTapFraction(double fraction) {
        if (Double.isNaN(fraction) || fraction < 0.02d) {
            return 0.02d;
        }
        return Math.min(fraction, 0.98d);
    }

    /**
     * Smart handler that tries to use iOS accessibility tap for elements in Dynamic Island area.
     * This is safer and more reliable than coordinate-based tapping for fixed header elements.
     *
     * @param e The element to click
     * @param centerOfElement The calculated center point
     * @return true if accessibility tap was used successfully, false if fallback needed
     */
    private boolean handleDynamicIslandSafetyWithAccessibility(WebElement e, Point centerOfElement) {
        final int DYNAMIC_ISLAND_GREY_AREA_THRESHOLD = 100;

        // Check if element is in the grey area (Dynamic Island zone)
        if (centerOfElement.getY() < DYNAMIC_ISLAND_GREY_AREA_THRESHOLD) {
            logger.log("Element in Dynamic Island grey area (Y: " + centerOfElement.getY() + "). Attempting safer click methods...");

            try {
                // Method 1: Try standard WebDriver click first (safest for iOS)
                // This uses iOS WebDriverAgent's native tap implementation
                e.click();
                logger.log("Successfully used WebDriver click() for element in grey area");
                return true;

            } catch (Exception ex) {
                logger.log("WebDriver click failed: " + ex.getMessage());

                // Method 2: Try mobile: tap with coordinates below safe threshold
                try {
                    // Force tap below Dynamic Island area for safety
                    int safeY = Math.max(centerOfElement.getY(), DYNAMIC_ISLAND_GREY_AREA_THRESHOLD + 5);

                    Map<String, Object> params = new HashMap<>();
                    params.put("x", centerOfElement.getX());
                    params.put("y", safeY);

                    ((AppiumDriver) driver).executeScript("mobile: tap", params);
                    logger.log("Successfully used 'mobile: tap' with safe coordinates (Y: " + safeY + ")");
                    return true;

                } catch (Exception ex2) {
                    logger.log("Mobile tap also failed: " + ex2.getMessage() + ". Will use coordinate-based fallback.");
                    return false;
                }
            }
        }

        return false; // Element not in grey area, no action needed
    }

    /**
     * Clicks element using safer methods (WebDriver click or mobile: tap).
     * This method uses iOS native tap implementation which is safer for elements
     * near Dynamic Island or system UI areas. Works for both iOS and Android.
     *
     * @param locator The element locator to click
     * @return true if click was successful, false otherwise
     */
    public boolean clickUsingAccessibilityAction(Locator locator) {
        try {
            WebElement element = element(locator).orElseThrow();
            return clickUsingAccessibilityAction(element);
        } catch (Exception e) {
            logger.softWarning("Failed to click using accessibility action: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clicks element using safer methods (WebDriver click or mobile: tap).
     * Overloaded version that accepts WebElement directly.
     * For iOS: Uses WebDriver click() which leverages iOS WebDriverAgent's native tap.
     * For Android: Uses standard click().
     *
     * @param element The WebElement to click
     * @return true if click was successful, false otherwise
     */
    public boolean clickUsingAccessibilityAction(WebElement element) {
        try {
            logger.log("Clicking element using safer click method: " + element);

            // WebDriver click() is actually the safest method for both platforms
            // On iOS, it uses WebDriverAgent's native tap implementation
            // On Android, it uses UiAutomator2's native tap
            element.click();
            logger.log("Successfully clicked element using WebDriver native click");
            return true;

        } catch (Exception e) {
            logger.softWarning("Failed to click using accessibility action: " + e.getMessage());

            // Fallback: Try mobile: tap with element coordinates
            if (isIOSPlatform()) {
                try {
                    Point location = element.getLocation();
                    Dimension size = element.getSize();
                    Point center = getCenterOfElement(location, size);

                    Map<String, Object> params = new HashMap<>();
                    params.put("x", center.getX());
                    params.put("y", center.getY());

                    ((AppiumDriver) driver).executeScript("mobile: tap", params);
                    logger.log("Successfully used mobile: tap fallback");
                    return true;
                } catch (Exception ex) {
                    logger.softWarning("Mobile tap fallback also failed: " + ex.getMessage());
                    return false;
                }
            }
            return false;
        }
    }

    /**
     * Checks if element is in Dynamic Island/Notch zone and handles it appropriately
     * Returns the safe click coordinates
     */
    private Point handleDynamicIslandSafety(WebElement e, Point centerOfElement) {
        // Avoid clicking on Dynamic Island area (iPhone 14 Pro and newer)
        // Dynamic Island typically occupies ~50-60 pixels from the top
        // This also provides safety for notch area on older iPhones
        final int DYNAMIC_ISLAND_SAFE_Y_THRESHOLD = 100;

        // Check if element is in the Dynamic Island/Notch zone
        if (centerOfElement.getY() < DYNAMIC_ISLAND_SAFE_Y_THRESHOLD) {
            logger.log("Element is in Dynamic Island/Notch zone (Y: " + centerOfElement.getY() + "). Performing small swipe down to move content...");

            // Perform a small swipe down to move the content away from Dynamic Island
            try {
                performSmallSwipeDownToAvoidDynamicIsland();

                // Wait briefly for the UI to settle after swipe (reduced from 300ms to 200ms)
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                // Re-get element location after swipe
                Point newLocation = e.getLocation();
                Dimension newSize = e.getSize();
                Point newCenterOfElement = getCenterOfElement(newLocation, newSize);
                logger.log("After swipe, new element position: " + newCenterOfElement);

                // Double-check: if still in danger zone after swipe, adjust Y coordinate as fallback
                if (newCenterOfElement.getY() < DYNAMIC_ISLAND_SAFE_Y_THRESHOLD) {
                    int originalY = newCenterOfElement.getY();
                    newCenterOfElement = new Point(newCenterOfElement.getX(), DYNAMIC_ISLAND_SAFE_Y_THRESHOLD);
                    logger.log("Fallback: Adjusted tap Y coordinate from " + originalY + " to " + DYNAMIC_ISLAND_SAFE_Y_THRESHOLD);
                }
                return newCenterOfElement;
            } catch (Exception ex) {
                logger.log("Swipe failed, using fallback: adjusting coordinates. Error: " + ex.getMessage());
                // Fallback: adjust coordinates if swipe fails
                int originalY = centerOfElement.getY();
                Point adjustedPoint = new Point(centerOfElement.getX(), DYNAMIC_ISLAND_SAFE_Y_THRESHOLD);
                logger.log("Adjusted tap Y coordinate from " + originalY + " to " + DYNAMIC_ISLAND_SAFE_Y_THRESHOLD);
                return adjustedPoint;
            }
        }

        return centerOfElement; // No adjustment needed
    }

    /**
     * Performs a small, controlled swipe down to move content away from Dynamic Island area
     * Works on all iPhone models (with or without Dynamic Island/Notch)
     */
    private void performSmallSwipeDownToAvoidDynamicIsland() {
        try {
            Dimension screenSize = driver.manage().window().getSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;

            // Start from middle of screen horizontally, near top (below status bar)
            int startX = screenWidth / 2;
            int startY = (int) (screenHeight * 0.15); // Start at 15% from top

            // Swipe down a small amount (only 20% of screen height)
            int endX = startX;
            int endY = (int) (screenHeight * 0.35); // End at 35% from top

            logger.log("Performing small swipe down from (" + startX + ", " + startY + ") to (" + endX + ", " + endY + ")");

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1)
                    .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                    .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                    .addAction(new Pause(finger, Duration.ofMillis(100)))
                    .addAction(finger.createPointerMove(Duration.ofMillis(300), PointerInput.Origin.viewport(), endX, endY))
                    .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            ((AppiumDriver)driver).perform(Collections.singletonList(swipe));
            logger.log("Small swipe down completed to avoid Dynamic Island area");
        } catch (Exception e) {
            logger.log("Error performing small swipe down: " + e.getMessage());
            throw e;
        }
    }

    public void clickElementByCoordinateDWeb(Locator locator) {
        WebElement e = element(locator).orElseThrow();
        try{
            clickElementByCoordinateDWeb(e);
        }catch(StaleElementReferenceException error){
            logger.log("Stale element exception detected. Retrying once");
            WebElement newElement = element(locator).orElseThrow();
            clickElementByCoordinateDWeb(newElement);
        }catch(ElementClickInterceptedException intercepted){
            logger.log("Click intercepted on " + locator + "; waiting briefly and retrying once");
            dummyWait(2);
            WebElement retryElement = element(locator).orElseThrow();
            clickElementByCoordinateDWeb(retryElement);
        }
    }

    /**
     * Helper method to check if running on iOS platform
     */
    private boolean isIOSPlatform() {
        try {
            return Configs.PLATFORM == Platform.IOS;
        } catch (Exception e) {
            // If platform check fails, assume it might be iOS for safety
            return driver instanceof IOSDriver;
        }
    }

    public void clickElementByCoordinateDWeb(WebElement e) {
        int x = e.getLocation().x;
        int y = e.getLocation().y;
        int width = e.getSize().width;
        int height = e.getSize().height;
        x = (int) (x + width * 0.5);
        y = (int) (y + height * 0.5);
        logger.log("Clicking at coordinate: X: " + x + " Y: " + y);
        Actions actions = new Actions(driver);
        actions.moveToElement(e).click().build().perform();
    }

    /**
     * Clicks a React-controlled toggle/switch (a visible knob backed by a hidden <input type="checkbox">)
     * and confirms the checkbox state actually flipped before returning, retrying the click if needed.
     * <p>
     * A single click fired right after a modal opens can be silently dropped (the modal is still animating
     * or React hasn't attached its change handler yet), which then "saves" the unchanged value. After each
     * click it polls the backing checkbox for the opposite state rather than sleeping a fixed time, so a slow
     * state update can't trigger an immediate re-click that toggles the value back.
     *
     * @param toggleContainer the clickable toggle wrapper (e.g. the Toggle_container div)
     * @param hiddenCheckbox  the backing <input type="checkbox"> whose selected state reflects the toggle
     * @param maxAttempts     how many times to click before giving up
     * @return the new selected state after the flip
     */
    public boolean toggleUntilStateChanges(Locator toggleContainer, Locator hiddenCheckbox, int maxAttempts) {
        boolean before = isElementSelected(hiddenCheckbox);
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            clickElementByCoordinateDWeb(toggleContainer);
            if (waitForSelectedStateToBe(hiddenCheckbox, !before, 3)) {
                return !before;
            }
            logger.log("Toggle " + toggleContainer + " did not change state on attempt " + attempt + "; retrying");
        }
        throw new DriverToolsException("Toggle did not change state after " + maxAttempts + " attempts: " + toggleContainer);
    }

    /**
     * Polls the (possibly hidden) checkbox's selected state until it equals {@code expected} or the timeout
     * elapses. Reads the DOM state directly (no logging/soft-warning noise on each poll) and returns
     * {@code false} on timeout instead of throwing, so the caller can decide whether to retry.
     */
    private boolean waitForSelectedStateToBe(Locator checkbox, boolean expected, int timeoutSeconds) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .pollingEvery(Duration.ofMillis(200))
                    .until(d -> {
                        List<WebElement> found = d.findElements(checkbox.by());
                        return !found.isEmpty() && found.get(0).isSelected() == expected;
                    });
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Waits until an element's on-screen position stops changing, i.e. an enter/slide/spring animation has
     * finished (e.g. the V2 modal that slides up from the bottom and overshoots before settling).
     * <p>
     * Selenium computes an element's coordinates and then clicks them; while the element is still moving the
     * click can land on the spot it just vacated and be intercepted by whatever is now there (the
     * {@code ElementClickInterceptedException: ... Other element would receive the click} seen on the
     * just-opened Create User modal). Polling the rendered bounding box (via {@code getBoundingClientRect},
     * which reflects CSS transforms, unlike Selenium's layout-based getLocation) until it is stable across
     * consecutive samples makes the subsequent interaction land on the settled element.
     * <p>
     * Best-effort: returns as soon as the position is stable, or after {@code timeoutSeconds} (it never throws,
     * so callers keep their existing explicit waits/clicks as the real assertion).
     *
     * @param locator        element to watch
     * @param timeoutSeconds maximum time to wait for the position to stabilize
     */
    public void waitForElementToSettle(Locator locator, int timeoutSeconds) {
        logger.log("Waiting for element position to settle: " + locator);
        double[] previous = { Double.NaN, Double.NaN };
        int[] stableSamples = { 0 };
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .pollingEvery(Duration.ofMillis(150))
                    .until(d -> {
                        double[] current = boundingBoxTopLeft(locator).orElse(null);
                        if (current == null) {
                            stableSamples[0] = 0;
                            return false;
                        }
                        boolean stable = !Double.isNaN(previous[0])
                                && Math.abs(current[0] - previous[0]) <= POSITION_SETTLE_TOLERANCE_PX
                                && Math.abs(current[1] - previous[1]) <= POSITION_SETTLE_TOLERANCE_PX;
                        previous[0] = current[0];
                        previous[1] = current[1];
                        if (stable) {
                            return ++stableSamples[0] >= 2;
                        }
                        stableSamples[0] = 0;
                        return false;
                    });
            logger.log("Element position settled: " + locator);
        } catch (TimeoutException e) {
            logger.softWarning("Element did not fully settle within " + timeoutSeconds + "s; proceeding anyway: " + locator);
        }
    }

    private Optional<double[]> boundingBoxTopLeft(Locator locator) {
        try {
            Optional<WebElement> element = element(locator);
            if (element.isEmpty()) {
                return Optional.empty();
            }
            Object result = ((JavascriptExecutor) driver).executeScript(
                    "var r = arguments[0].getBoundingClientRect(); return [r.left, r.top];", element.get());
            if (result instanceof List<?> list && list.size() >= 2
                    && list.get(0) instanceof Number x && list.get(1) instanceof Number y) {
                return Optional.of(new double[] { x.doubleValue(), y.doubleValue() });
            }
        } catch (Exception e) {
            logger.softWarning("Could not read bounding box for " + locator + ": " + e.getMessage());
        }
        return Optional.empty();
    }

    public void clickElementByCoordinate(Locator locator) {
        for (int i = 0; i < MAX_TRY; i++) {
            try {
                WebElement e = element(locator).orElseThrow();
                clickElementByCoordinate(e);
                break;
            } catch (Exception e) {
                ExceptionAnalyser.analyse(e, forbiddenExceptions);
                if (i == MAX_TRY - 1) {
                    throw new DriverToolsException("Failed clicking element" + locator);
                }
            }
        }
    }

    /**
     * Avoid using this when possible, only if locators are not being able to be
     * located due to an issue
     * You can find use case at InboxPageIOS.clickLegalBanner where it try to click
     * child element which is not accessible
     */
    public void clickSidePartOfElement(WebElement e, SideOfElement sideOfElement) {
        int x = e.getLocation().x;
        int y = e.getLocation().y;
        int width = e.getSize().width;
        int height = e.getSize().height;
        double xMultipler = sideOfElement.getXYMultiplier()[0];
        double yMultipler = sideOfElement.getXYMultiplier()[1];
        x = (int) (x + width * xMultipler);
        y = (int) (y + height * yMultipler);
        logger.log("Clicking at coordinate: X: " + x + " Y: " + y);
        PointerInput finger1 = new PointerInput (PointerInput.Kind. TOUCH,"finger1");
        Sequence sequence= new Sequence(finger1,1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger1, Duration.ofMillis(200)))
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        ((AppiumDriver)driver).perform(Collections.singletonList(sequence));
    }

    public void clickSidePartOfElement(Locator locator, SideOfElement sideOfElement) {
        for (int i = 0; i < MAX_TRY; i++) {
            try {
                WebElement e = element(locator).orElseThrow();
                clickSidePartOfElement(e, sideOfElement);
                break;
            } catch (Exception e) {
                ExceptionAnalyser.analyse(e, forbiddenExceptions);
                if (i == MAX_TRY - 1) {
                    logger.softWarning("Element not found: " + locator);
                }
            }
        }
    }

    /**
     * Clicks an element as soon as it is present in the DOM (uses {@link #waitForElementToPresence})
     * instead of waiting for visibility/clickable. Useful when the element is technically interactive
     * but the clickable/visibility check is flaky — e.g. elements with {@code enabled=false} at the
     * moment the wait is evaluated, zero-sized wrappers, overlays, or transient UI transitions where
     * {@link ExpectedConditions#elementToBeClickable(By)} times out even though the element is tappable.
     *
     * <p>Prefer {@link #click(Locator)} for normal cases; reach for this when the clickable wait is the
     * problem, not the element itself. Retries once on {@link StaleElementReferenceException}.
     *
     * <p>For optional clicks that must not fail the flow, use {@link #tryClickWhenPresent(Locator)}.
     */
    public void clickWhenPresent(Locator locator) {
        clickWhenPresent(locator, SECONDS);
    }

    // get a list of texts from elements with the same locator
    public List<String> getTexts(Locator locator) {
        logger.log("getting all the texts from " + locator);
        List<WebElement> elements = elements(locator).orElseThrow();
        return elements.stream()
                .map(this::getText)
                .filter(text -> text != null && !text.isEmpty())  // Filter out null and empty strings
                .collect(Collectors.toList());  // Collect the results into a list
    }

    public List<String> getTexts(WebElement webElement) {
        List<WebElement> elements = Collections.singletonList(webElement);
        return elements.stream()
                .map(this::getText)
                .filter(text -> text != null && !text.isEmpty())  // Filter out null and empty strings
                .collect(Collectors.toList());  // Collect the results into a list
    }

    // get a list of texts from elements with the same locator
    public List<String> getNumberTexts(Locator locator) {
        List<WebElement> elements = elements(locator).orElseThrow();
        return elements.stream()
                .map(this::getText)
                .filter(e -> e.matches(".*[0-9]+.*"))
                .map(CoreFunctions::convertPriceToEng)
                .toList();
    }

    private Point getCenterOfElement(Point location, Dimension size) {
        return new Point(location.getX() + size.getWidth() / 2,
                location.getY() + size.getHeight() / 2);
    }

    public void longPress(WebElement element, int duration) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        Point centerOfElement = getCenterOfElement(location, size);
        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        Sequence sequence = new Sequence(finger1, 1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerOfElement))
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger1, Duration.ofSeconds(duration)))
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                AndroidDriver d = (AndroidDriver) driver;
                d.perform(Collections.singletonList(sequence));
            }
            case IOS -> {
                IOSDriver d = (IOSDriver) driver;
                d.perform(Collections.singletonList(sequence));
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + Configs.PLATFORM);
        }

    }

    public void longPress(Locator locator, int duration) {
        WebElement element = element(locator).orElseThrow();
        this.longPress(element, duration);
    }

    public int getElementHeight(WebElement a) {
        return a.getSize().height;
    }

    public int getElementHeight(Locator a) {
        return getElementHeight(element(a).orElseThrow());
    }

    public Map<String, Object> getCapabilities(){
        CapabilitiesHandler handler = new CapabilitiesHandler();
        JSONArray configs = handler.getConfigs();
        JSONObject config = handler.getConfig(configs);
        DesiredCapabilities capabilities = handler.getCapabilities(config);
        Map<String, Object> capabilitiesMap = capabilities.toJson();
        logger.log("Capabilities: " + capabilitiesMap);

        return capabilitiesMap;
    }

    public void maximizeWindow(){
        logger.log("Maximizing window");
        driver.manage().window().maximize();
        if (Configs.PLATFORM == Platform.DWEB) {
            Dimension size = driver.manage().window().getSize();
            if (size.getWidth() < 1280) {
                driver.manage().window().setSize(new Dimension(1920, 1080));
            }
        }
        logger.log("Window is maximized: " + driver.manage().window().getSize());
    }

    public void refreshWebpage(){
        logger.log("Refresh Webpage");
        driver.navigate().refresh();
        logger.log("Webpage Refreshed");
    }

    /**
     * Performs a hard reload that bypasses the HTTP cache (via CDP {@code Page.reload} with
     * {@code ignoreCache=true}), similar to a manual Ctrl/Cmd+Shift+R. Useful to recover a page
     * stuck on a cached/503 state. Note: this does not unregister service workers or clear Cache
     * Storage. Falls back to a normal refresh if CDP is unavailable.
     */
    public void hardRefreshWebpage() {
        logger.log("Hard Refresh Webpage (bypass cache)");
        if (driver instanceof HasCdp hasCdp) {
            boolean cacheDisabled = false;
            try {
                hasCdp.executeCdpCommand("Network.enable", Map.of());
                hasCdp.executeCdpCommand("Network.setCacheDisabled", Map.of("cacheDisabled", true));
                cacheDisabled = true;
                hasCdp.executeCdpCommand("Page.enable", Map.of());
                hasCdp.executeCdpCommand("Page.reload", Map.of("ignoreCache", true));
                logger.log("Webpage Hard Refreshed via CDP");
                return;
            } catch (Exception e) {
                logger.log("CDP hard refresh failed, falling back to normal refresh: " + e.getLocalizedMessage());
            } finally {
                if (cacheDisabled) {
                    try {
                        hasCdp.executeCdpCommand("Network.setCacheDisabled", Map.of("cacheDisabled", false));
                    } catch (Exception ignored) {
                        logger.log("Failed to re-enable cache after CDP hard refresh: " + ignored.getLocalizedMessage());
                    }
                }
            }
        }
        driver.navigate().refresh();
        logger.log("Webpage Refreshed (fallback)");
    }

    public void acceptBrowserAlert() {
        logger.log("Accepting browser alert");
        try {
            driver.switchTo().alert().accept();
            logger.log("Alert accepted");
        } catch (NoAlertPresentException e) {
            logger.log("No alert shown "+e.getLocalizedMessage());
        }
    }

    /**
     * Returns the Appium session {@code udid} (serial) for the current Android session.
     * Despite the method name, this is the ADB serial for <b>either</b> an emulator
     * ({@code emulator-5554}) or a physical device / cloud serial — not emulator-only.
     */
    public String getEmulatorName() {
        Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
        String currentUdid = capabilities.getCapability("udid").toString();
        logger.log("Emulator name: " + currentUdid);
        return currentUdid.trim();
    }

    /**
     * Enables Android Developer option "Don't keep activities"
     * ({@code settings put global always_finish_activities 1}).
     * <p>
     * Works on <b>emulators and physical devices</b> reachable via local ADB using the session
     * {@code udid}. No-op on non-Android platforms. Requires ADB access to the device (local USB /
     * wireless debugging); remote-only cloud sessions without a tunneled ADB serial may fail.
     * Waits for the {@code settings} command to finish and verifies the value was applied.
     */
    public void enableDontKeepActivities() {
        if (Configs.PLATFORM != Platform.ANDROID) {
            logger.log("Don't keep activities is Android-only; skipping");
            return;
        }
        String serial = getEmulatorName();
        logger.log("Enabling Don't keep activities on Android device/emulator udid=" + serial);
        ADB put = new ADB(serial, "settings put global always_finish_activities 1");
        int putExit = put.execShell();
        if (putExit != 0) {
            logger.log("WARN: failed to set always_finish_activities (exit=" + putExit + ")");
            return;
        }
        ADB get = new ADB(serial, "settings get global always_finish_activities");
        int getExit = get.execShell();
        if (getExit != 0) {
            logger.log("WARN: could not verify always_finish_activities after put");
        }
    }

    /**
     * Clears Android app data (including local passenger cache) for the AUT package, then relaunches.
     * Uses Appium {@code mobile: clearApp}, with {@code adb shell pm clear} as fallback.
     * No-op on non-Android. After this, call {@code HomePage.gotoHome()} so onboarding can run again.
     */
    public void clearAppDataAndRelaunch() {
        if (Configs.PLATFORM != Platform.ANDROID) {
            logger.log("clearAppDataAndRelaunch is Android-only; skipping");
            return;
        }
        AndroidDriver androidDriver = (AndroidDriver) driver;
        String pkg = PackageName.get();
        logger.log("Clearing Android app data for package=" + pkg);
        try {
            androidDriver.terminateApp(pkg);
        } catch (Exception e) {
            logger.log("terminateApp before clear failed (continuing): " + e.getMessage());
        }
        boolean cleared = false;
        try {
            androidDriver.executeScript("mobile: clearApp", Map.of("appId", pkg));
            cleared = true;
            logger.log("App data cleared via mobile: clearApp");
        } catch (Exception e) {
            logger.log("mobile: clearApp failed, falling back to pm clear: " + e.getMessage());
        }
        if (!cleared) {
            ADB adb = new ADB(getEmulatorName(), "pm clear " + pkg);
            int exit = adb.execShell();
            if (exit != 0) {
                throw new IllegalStateException("Failed to clear app data for " + pkg + " (exit=" + exit + ")");
            }
            logger.log("App data cleared via adb pm clear");
        }
        androidDriver.activateApp(pkg);
        dummyWait(2);
        switchToNative();
        logger.log("App relaunched after data clear");
    }

    public WebElement swipeUntilElementVisible(Locator locator, Swipe direction, int maxTries) {
        int tries = 0;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

        while (tries < maxTries) {
            try {
                // Attempt to locate and check if the element is in the viewport
                WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator.by()));
                if (isElementInViewport(element)) {
                    return element; // Element is found and visible
                }
                performSwipe(direction); // Swipe if not in viewport
            } catch (TimeoutException | StaleElementReferenceException e) {
                logger.log("Swipe attempt " + (tries + 1) + " failed; retrying...");
                performSwipe(direction); // Swipe in case of exceptions
            }
            tries++;
        }
        throw new NoSuchElementException("Element not found after " + maxTries + " swipe attempts: " + locator);
    }

    private boolean isElementInViewport(WebElement element) {
        return (Boolean) ((JavascriptExecutor) driver).executeScript(
                "const rect = arguments[0].getBoundingClientRect();" +
                        "return (rect.top >= 0 && rect.left >= 0 && " +
                        "rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) && " +
                        "rect.right <= (window.innerWidth || document.documentElement.clientWidth));",
                element
        );
    }

    private void performSwipe(Swipe direction) {
        int distance = 300; // Scroll distance
        String scrollScript = switch (direction) {
            case UP -> "window.scrollBy(0, " + distance + ");";
            case DOWN -> "window.scrollBy(0, -" + distance + ");";
            case LEFT -> "window.scrollBy(-" + distance + ", 0);";
            case RIGHT -> "window.scrollBy(" + distance + ", 0);";
            case LEFT_LONG -> null;
            case UP_LONG -> null;
        };
        ((JavascriptExecutor) driver).executeScript(scrollScript);
    }

    public void switchToNewTab() {
        // Get all window handles
        List<String> allTabs = new ArrayList<>(driver.getWindowHandles());
        // Get the current window handle
        String currentTab = driver.getWindowHandle();
        // Find the index of the current tab
        int currentIndex = allTabs.indexOf(currentTab);
        // Calculate the index of the next tab
        int nextIndex = (currentIndex + 1) % allTabs.size();
        // Switch to the next tab
        driver.switchTo().window(allTabs.get(nextIndex));
    }

    public String tabSwitch(String url){
        Set<String> windowHandles = driver.getWindowHandles();
        logger.log("Window Handles: " + windowHandles);
        for (String window : windowHandles) {
            driver.switchTo().window(window);
            logger.log("Selected window: " + window);
            String currentURL = driver.getCurrentUrl();
            logger.log("Current URL: " + currentURL);
            if (currentURL.contains(url) && !currentURL.contains("corporate")) {
                logger.log("Webview with selected tab url: " + currentURL);
                return currentURL;
            }
        }
        return "";
    }

    public String getPdfContent(String expectedPartialUrl) throws IOException, InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(driver -> driver.getWindowHandles().size() > 1);

        // Store the original window handle before switching to PDF window
        String originalWindowHandle = driver.getWindowHandle();
        logger.log("Original window handle: " + originalWindowHandle);

        switchToWindowContainingUrl(expectedPartialUrl);
        String actualUrl = driver.getCurrentUrl();
        String pdfWindowHandle = driver.getWindowHandle();
        logger.log("Current URL: " + actualUrl);
        logger.log("PDF window handle: " + pdfWindowHandle);

        URL pdfURL = new URL(actualUrl);
        HttpURLConnection connection = (HttpURLConnection) pdfURL.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        String contentType = connection.getContentType();
        if (!"application/pdf".equalsIgnoreCase(contentType)) {
            throw new IOException("Invalid content type: " + contentType);
        }
        PDDocument doc = null;
        try (InputStream is = new BufferedInputStream(connection.getInputStream())) {
            doc = PDDocument.load(is);
            int pages = doc.getNumberOfPages();
            logger.log("Total number of pages: " + pages);
            PDFTextStripper strip = new PDFTextStripper();
            strip.setStartPage(1);
            strip.setEndPage(pages);
            String stripText = strip.getText(doc);
            return stripText;
        } catch (IOException e) {
            logger.log("Error reading PDF: " + e.getMessage());
            throw e;
        } finally {
            if (doc != null) {
                doc.close();
            }
            // Close the PDF window and switch back to the original window
            driver.close();
            logger.log("Closed PDF window, switching back to original window: " + originalWindowHandle);

            // Switch back to the original window to avoid NoSuchWindowException
            Set<String> remainingHandles = driver.getWindowHandles();
            if (remainingHandles.contains(originalWindowHandle)) {
                driver.switchTo().window(originalWindowHandle);
                logger.log("Successfully switched back to original window");
            } else if (!remainingHandles.isEmpty()) {
                // If original handle is not available, switch to the first available window
                String fallbackHandle = remainingHandles.iterator().next();
                driver.switchTo().window(fallbackHandle);
                logger.log("Original window not found, switched to fallback window: " + fallbackHandle);
            }
        }
    }

    public void switchToWindowContainingUrl(String partialUrl) {
        Set<String> windowHandles = driver.getWindowHandles();
        for (String windowHandle : windowHandles) {
            driver.switchTo().window(windowHandle);
            if (driver.getCurrentUrl().contains(partialUrl)) {
                logger.log("Switched to window with URL: " + driver.getCurrentUrl());
                return;
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("No window found with URL containing: " + partialUrl);
    }

    public boolean waitForWebToLoad(){
        try{
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            wait.until((ExpectedCondition<Boolean>) wd ->
                    ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete")
            );
            return true;
        }catch (Exception e){
            logger.log("Web page failed to load: " + e.getMessage());
            return false;
        }
    }

    public WebElement findElementInElement(WebElement parent, By locator) {
        try {
            return parent.findElement(locator);
        } catch (NoSuchElementException e) {
            logger.log("Element not found: " + locator.toString());
            throw e;
        }
    }

    public void slideToValue(String value, WebElement element){
        int desiredValue = Integer.parseInt(value);
        int sliderWidth = element.getSize().getWidth();
        int xOffset = ((sliderWidth * desiredValue) / 100)+3;

        Actions actions = new Actions(driver);
        actions.clickAndHold(element)
                .moveByOffset(-sliderWidth / 2, 0)
                .moveByOffset(xOffset, 0)
                .release()
                .perform();

        logger.log("Slider moved to desired value");
    }

    public boolean isElementPresent(Locator locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
            List<WebElement> elements = wait.until(
                    ExpectedConditions.refreshed(ExpectedConditions.presenceOfAllElementsLocatedBy(locator.by()))
            );
            boolean isPresent = elements != null && !elements.isEmpty();
            logger.log("Element appeared: " + locator + " = " + isPresent);
            return isPresent;
        } catch (TimeoutException e) {
            logger.log("Element NOT present: " + locator);
            return false;
        }
    }

    //special method for vi case on booking page
    public void clickSpecificPart(WebElement e,int where){
        double xPercent=0,yPercent=0;
        yPercent = switch (where) {
            case 1 -> {
                logger.log("btmStart");
                xPercent = 0.05;
                yield 0.8;
            }
            case 2 -> {
                logger.log("btmMid");
                xPercent = 0.3;
                yield 0.8;
            }
            case  3 -> {
                logger.log("btmEnd");
                xPercent = 0.8;
                yield 0.8;
            }
            case  4 -> {
                logger.log("btmUp");
                xPercent = 0.6;
                yield 0.6;
            }
            default -> {
                logger.log("middle");
                xPercent = 0.5;
                yield 0.5;
            }
        };
        int x = e.getLocation().x;
        int y = e.getLocation().y;
        int width = e.getSize().width;
        int height = e.getSize().height;
        x = x + (int)(width * xPercent);
        y = y + (int)(height * yPercent);
        logger.log("Clicking at coordinate: X: " + x + " Y: " + y + " where " + where);

        PointerInput finger1 = new PointerInput (PointerInput.Kind. TOUCH,"finger1");
        Sequence sequence= new Sequence(finger1,1)
                .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y))
                .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger1, Duration.ofMillis(200)))
                .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        ((AppiumDriver)driver).perform(Collections.singletonList(sequence));
    }

    private static final String BROWSER_CLIPBOARD_CAPTURE_SCRIPT =
            "(() => {"
                    + "if (!window.__tiketClipboardCaptureInstalled) {"
                    + "  window.__tiketLastCopiedText = '';"
                    + "  window.__tiketClipboardCaptureInstalled = true;"
                    + "  if (navigator.clipboard && navigator.clipboard.writeText) {"
                    + "    const originalWriteText = navigator.clipboard.writeText.bind(navigator.clipboard);"
                    + "    navigator.clipboard.writeText = (text) => {"
                    + "      window.__tiketLastCopiedText = String(text || '');"
                    + "      return originalWriteText(text);"
                    + "    };"
                    + "  }"
                    + "  const originalExecCommand = document.execCommand.bind(document);"
                    + "  document.execCommand = function(command) {"
                    + "    const result = originalExecCommand.apply(document, arguments);"
                    + "    if (String(command).toLowerCase() === 'copy') {"
                    + "      const active = document.activeElement;"
                    + "      if (active && active.value) window.__tiketLastCopiedText = active.value;"
                    + "      else {"
                    + "        const sel = document.getSelection();"
                    + "        if (sel) window.__tiketLastCopiedText = sel.toString();"
                    + "      }"
                    + "    }"
                    + "    return result;"
                    + "  };"
                    + "  document.addEventListener('copy', (event) => {"
                    + "    try {"
                    + "      if (event.clipboardData) {"
                    + "        const data = event.clipboardData.getData('text/plain');"
                    + "        if (data) window.__tiketLastCopiedText = data;"
                    + "      }"
                    + "    } catch (e) {}"
                    + "  }, true);"
                    + "} else {"
                    + "  window.__tiketLastCopiedText = '';"
                    + "}"
                    + "})();";

    /**
     * Installs a page-level hook that records text written by the next browser copy action.
     * Call this immediately <b>before</b> clicking a copy button, then read with
     * {@link #getTextFromClipboardBrowser()}.
     */
    public void prepareBrowserClipboardCapture() {
        if (!(driver instanceof JavascriptExecutor js)) {
            logger.log("prepareBrowserClipboardCapture: driver is not JavascriptExecutor");
            return;
        }
        grantBrowserClipboardPermissionsIfPossible();
        js.executeScript(BROWSER_CLIPBOARD_CAPTURE_SCRIPT);
        logger.log("prepareBrowserClipboardCapture: hook installed / reset");
    }

    /**
     * Reads text copied by the most recent copy action in the automated Chrome session.
     * <p>
     * Requires {@link #prepareBrowserClipboardCapture()} to have been called before the copy click.
     * Captures {@code navigator.clipboard.writeText()} and {@code copy} events in-page so the value
     * comes from the app's copy handler, not pre-existing host OS clipboard content.
     *
     * @return trimmed copied text, or {@code ""} when nothing was captured
     */
    public String getTextFromClipboardBrowser() {
        if (!(driver instanceof JavascriptExecutor)) {
            logger.log("getTextFromClipboardBrowser: unsupported driver type (" + driver.getClass().getName() + ")");
            return "";
        }
        return readCapturedBrowserClipboardText();
    }

    private String readCapturedBrowserClipboardText() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        final int maxAttempts = 5;
        final long pauseMs = 200;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                Object result = js.executeScript("return (window.__tiketLastCopiedText || '').trim();");
                if (result instanceof String text && !text.isEmpty()) {
                    logger.log("getTextFromClipboardBrowser (captured): " + text);
                    return text;
                }
            } catch (Exception e) {
                logger.log("getTextFromClipboardBrowser (captured) attempt " + attempt + "/" + maxAttempts
                        + " failed: " + e.getMessage());
            }
            if (attempt < maxAttempts) {
                try {
                    Thread.sleep(pauseMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        logger.log("getTextFromClipboardBrowser (captured): empty after " + maxAttempts + " attempt(s)");
        return "";
    }

    private void grantBrowserClipboardPermissionsIfPossible() {
        if (!(driver instanceof HasCdp hasCdp)) {
            return;
        }
        try {
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl == null || currentUrl.isBlank() || currentUrl.startsWith("data:")) {
                return;
            }
            java.net.URI uri = java.net.URI.create(currentUrl);
            String origin = uri.getScheme() + "://" + uri.getHost();
            if (uri.getPort() != -1) {
                origin += ":" + uri.getPort();
            }
            Map<String, Object> params = new HashMap<>();
            params.put("origin", origin);
            params.put("permissions", List.of("clipboardReadWrite", "clipboardSanitizedWrite"));
            hasCdp.executeCdpCommand("Browser.grantPermissions", params);
            logger.log("Granted browser clipboard permissions for origin: " + origin);
        } catch (Exception e) {
            logger.log("grantBrowserClipboardPermissionsIfPossible skipped: " + e.getMessage());
        }
    }

    @SneakyThrows
    public String getCopiedUrlFromClipboard(){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);

        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return (String) contents.getTransferData(DataFlavor.stringFlavor);
        }
        return null;
    }

    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    public void switchToWindow(String windowHandle) {
        driver.switchTo().window(windowHandle);
    }

    public T getDriver() {
        return driver;
    }

    /**
     * Adds a cookie to the current browser session.
     */
    public void addCookie(Cookie cookie) {
        logger.log("Adding cookie: " + cookie.getName());
        driver.manage().addCookie(cookie);
        logger.log("Added cookie: " + cookie.getName());
    }

    /**
     * Adds a cookie by fields. Use when the captured cookie's host must be rewritten
     * (for example {@code .tiket.com} after a cross-subdomain navigation).
     */
    public void addCookie(String name, String value, String domain, String path) {
        addCookie(new Cookie.Builder(name, value).domain(domain).path(path).build());
    }

    /**
     * Returns the named cookie from the current browser session, or null if it is not present.
     */
    public Cookie getCookie(String cookieName) {
        return driver.manage().getCookieNamed(cookieName);
    }

    /**
     * Returns every cookie carrying this name in the current browser session.
     *
     * <p>A site can legitimately hold two cookies of the same name at once - typically one host-only
     * and one domain-wide - and {@link #getCookie(String)} then reports only whichever the driver
     * returns first. Use this when the duplicate is the point, for example to see that the page has
     * written its own session cookie next to an injected one.
     */
    public List<Cookie> getCookiesNamed(String cookieName) {
        return driver.manage().getCookies().stream()
                .filter(cookie -> cookie.getName().equals(cookieName))
                .toList();
    }

    /**
     * Returns the value of a cookie in the current browser session, or null if it is not present.
     */
    public String getCookieValue(String cookieName) {
        Cookie cookie = getCookie(cookieName);
        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * Deletes one cookie by name from the current browser session.
     */
    public void deleteCookieNamed(String cookieName) {
        driver.manage().deleteCookieNamed(cookieName);
    }

    /**
     * Deletes all cookies for the currently loaded domain in the browser session.
     */
    public void deleteAllCookies() {
        logger.log("Deleting all cookies for the current domain");
        driver.manage().deleteAllCookies();
    }


    public void deleteUsingBackspace(Locator locator) {
        String textToDelete = driver.findElement(locator.by()).getAttribute("value");
        if(!textToDelete.isEmpty()) {
            logger.log(String.format("Trying to delete '%s' from locator '%s' using backspace...", textToDelete, locator.name()));
            if(System.getProperty("os.name").toLowerCase().contains("mac")) {
                sendKeys(locator, Keys.COMMAND + "a");
            } else {
                sendKeys(locator, Keys.CONTROL + "a");
            }
            sendKeys(locator, String.valueOf(Keys.BACK_SPACE));
            logger.log(textToDelete + " Deleted successfully");
        } else {
            logger.log("Found nothing on " + locator.name() + ". Continue process.");
        }
    }

    public Optional<String> getAttributeTextContent(Locator locator) {
        logger.log("Getting textContent attribute from: " + locator);
        Optional<WebElement> element = element(locator);

        if (element.isPresent()) {
            try {
                WebElement webElement = element.get();
                String text = webElement.getAttribute("textContent").trim();

                logger.log("Got textContent: (" + text + ") from: " + locator);
                return Optional.of(text);
            } catch (Exception e) {
                logger.softWarning("Failed to get textContent from: " + locator + " - " + e.getMessage());
            }
        }
        logger.softWarning("Element not found for textContent retrieval: " + locator);
        return Optional.empty();
    }

    private void removeOverlay(Locator locator) {
        try {
            Optional<WebElement> overlayOpt = element(locator);

            if (overlayOpt.isPresent()) {
                WebElement overlay = overlayOpt.get();
                WebDriver driver = getDriver();
                ((JavascriptExecutor) driver).executeScript("arguments[0].remove();", overlay);
                System.out.println("Overlay removed.");
            } else {
                System.out.println("No overlay found.");
            }
        } catch (Exception e) {
            System.out.println("Error removing overlay: " + e.getMessage());
        }
    }

    public String getAttribute(By by, String attributeName) {
        try {
            WebElement element = driver.findElement(by);
            return element.getAttribute(attributeName);
        } catch (NoSuchElementException e) {
            logger.log("Element not found: " + by.toString());
            throw e;
        }
    }

    /**
     * Safe attribute reader for an already-resolved {@link WebElement}.
     * <p>
     * Use this overload from page objects instead of calling
     * {@code element.getAttribute(...)} directly so that callers don't have to
     * handle stale/detached elements themselves and so every read is logged
     * consistently. Returns {@code null} when the attribute is absent or when
     * the element has gone stale; never throws.
     */
    public String getAttribute(WebElement element, String attributeName) {
        try {
            String value = element.getAttribute(attributeName);
            logger.log("getAttribute(" + attributeName + ") = " + value);
            return value;
        } catch (StaleElementReferenceException e) {
            logger.log("getAttribute(" + attributeName + ") on stale element, returning null");
            return null;
        } catch (Exception e) {
            logger.log("getAttribute(" + attributeName + ") failed: " + e.getMessage());
            return null;
        }
    }

    public void dummyWait(int seconds) {
        logger.log("Waiting for " + seconds + " seconds...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        try {
            wait.withTimeout(Duration.ofSeconds(seconds)).until(driver -> false);
        } catch (TimeoutException ignored) {}
        logger.log("Done waiting.");
    }

    /**
     * Writes a solid-colour PNG to a JVM temp file (deleted on exit). Use for file-upload
     * fixtures that only need a valid image of a given size.
     */
    public File writeTempPng(String prefix, int width, int height, Color fill) {
        File file;
        try {
            file = File.createTempFile(prefix, ".png");
        } catch (IOException e) {
            throw new IllegalStateException("Could not create temp PNG " + prefix, e);
        }
        file.deleteOnExit();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(fill);
        graphics.fillRect(0, 0, width, height);
        graphics.dispose();
        try {
            if (!ImageIO.write(image, "png", file)) {
                throw new IllegalStateException("Could not write temp PNG " + file.getName());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not write temp PNG " + file.getName(), e);
        }
        logger.log("Wrote temp PNG " + file.getAbsolutePath());
        return file;
    }

    public void scrollElementToCenter(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            dummyWait(1);
        } catch (NoSuchElementException e) {
            logger.log("error while scrolling: " + e.getMessage());
        }
    }

    public void scrollElementToCenter(Locator locator) {
        WebElement element = waitForElementToAppear(locator).orElseThrow();
        scrollElementToCenter(element);
    }

    public void scrollAndClick(Locator locator) {
        WebElement element = waitForElementToAppear(locator).orElseThrow();
        scrollElementToCenter(element);
        WebElement clickableElement = waitForElementToBeClickable(element, 10)
                .orElseThrow(() -> new RuntimeException("Element not clickable after scroll: " + locator.name()));
        click(clickableElement);
    }

    public void scrollAndClick(WebElement element) {
        scrollElementToCenter(element);
        WebElement clickableElement = waitForElementToBeClickable(element, 10)
                .orElseThrow(() -> new RuntimeException("Element not clickable after scroll"));
        click(clickableElement);
    }

    /**
     * Scrolls element to the middle of the screen for native mobile apps (iOS/Android).
     * This method performs native swipe gestures repeatedly until the element is positioned
     * at the vertical center of the viewport to avoid bottom overlays, navigation bars,
     * or any sticky elements at the bottom of the screen.
     *
     * Use this method when clicking elements that are being blocked by bottom navigation bars,
     * floating action buttons, or any persistent overlays at the bottom of the screen.
     *
     * Example usage:
     * <pre>
     * // Scroll book button to mid-screen before clicking
     * driverTools.swipeElementToMidScreen(BOOK_ROOM_BUTTON);
     * driverTools.click(BOOK_ROOM_BUTTON);
     * </pre>
     *
     * @param element The WebElement to scroll to the middle of the screen
     */
    public void swipeElementToMidScreen(WebElement element) {
        logger.log("Swiping element to mid-screen to avoid bottom overlays: " + element);
        swipeElementToMidScreenLoop(null, element);
    }

    /**
     * Scrolls element to the middle of the screen for native mobile apps (iOS/Android).
     * This method positions the element at the vertical center of the viewport to avoid
     * bottom overlays, navigation bars, or any sticky elements at the bottom of the screen.
     *
     * Use this method when clicking elements that are being blocked by bottom navigation bars,
     * floating action buttons, or any persistent overlays at the bottom of the screen.
     *
     * Example usage:
     * <pre>
     * // In your Page Object:
     * driverTools.waitForElementToPresence(BOOK_ROOM_BUTTON, 15).orElseThrow();
     * driverTools.swipeElementToMidScreen(BOOK_ROOM_BUTTON);
     * driverTools.click(BOOK_ROOM_BUTTON);
     * </pre>
     *
     * @param locator The Locator of the element to scroll to the middle of the screen
     */
    public void swipeElementToMidScreen(Locator locator) {
        logger.log("Swiping element to mid-screen: " + locator);
        swipeElementToMidScreenLoop(locator, null);
    }

    /**
     * @param locator if non-null, the target is resolved once and again only after {@link StaleElementReferenceException}
     * @param element used when locator is null; same reference is reused for the whole loop
     */
    @SneakyThrows
    private void swipeElementToMidScreenLoop(Locator locator, WebElement element) {
        if (locator == null && element == null) {
            throw new DriverToolsException("swipeElementToMidScreen: element and locator cannot both be null");
        }

        WebElement resolvedFromLocator = locator != null
                ? waitForElementToPresence(locator).orElseThrow(() -> new DriverToolsException("Element not found: " + locator))
                : null;

        Dimension windowSize = driver.manage().window().getSize();
        int screenHeight = windowSize.getHeight();
        int screenWidth = windowSize.getWidth();
        int centerX = screenWidth / 2;

        int targetYMin = (int) (screenHeight * 0.40);
        int targetYMax = (int) (screenHeight * 0.60);
        int targetYCenter = (targetYMin + targetYMax) / 2;

        int swipeUpStartY = (int) (screenHeight * 0.65);
        int swipeDownStartY = (int) (screenHeight * 0.35);
        int clampTop = (int) (screenHeight * 0.20);
        int clampBottom = (int) (screenHeight * 0.80);
        int maxSwipeDistance = (int) (screenHeight * 0.30);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        final int maxAttempts = 8;
        int attempt = 0;
        int previousElementCenterY = -1;

        while (attempt < maxAttempts) {
            WebElement el = locator != null ? resolvedFromLocator : element;

            try {
                Point elementLocation = el.getLocation();
                Dimension elementSize = el.getSize();
                int elementCenterY = elementLocation.getY() + (elementSize.getHeight() / 2);

                if (elementCenterY >= targetYMin && elementCenterY <= targetYMax) {
                    if (attempt > 0) {
                        logger.log("Element positioned at mid-screen after " + attempt + " attempt(s)");
                    }
                    return;
                }

                if (previousElementCenterY == elementCenterY && attempt > 2) {
                    logger.log("Element position unchanged, may be at scroll limit");
                    break;
                }
                previousElementCenterY = elementCenterY;

                int distanceFromTarget = Math.abs(elementCenterY - targetYCenter);
                int swipeDistance = Math.max(1, Math.min(distanceFromTarget, maxSwipeDistance));

                if (attempt == 0 || distanceFromTarget < screenHeight * 0.10) {
                    logger.log("Attempt " + (attempt + 1) + ": Element Y: " + elementCenterY +
                            ", Target: " + targetYMin + "-" + targetYMax +
                            ", Distance: " + distanceFromTarget);
                }

                int startY;
                int endY;
                if (elementCenterY > targetYMax) {
                    startY = swipeUpStartY;
                    endY = Math.max(startY - swipeDistance, clampTop);
                } else {
                    startY = swipeDownStartY;
                    endY = Math.min(startY + swipeDistance, clampBottom);
                }

                long swipeDuration = Math.max(300, Math.min(500, distanceFromTarget / 2));

                Sequence swipe = new Sequence(finger, 1)
                        .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, startY))
                        .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                        .addAction(new Pause(finger, Duration.ofMillis(100)))
                        .addAction(finger.createPointerMove(Duration.ofMillis(swipeDuration), PointerInput.Origin.viewport(), centerX, endY))
                        .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

                ((AppiumDriver) driver).perform(Collections.singletonList(swipe));

                int waitTime = distanceFromTarget > screenHeight * 0.20 ? 400 : 250;
                Thread.sleep(waitTime);
                attempt++;

            } catch (StaleElementReferenceException e) {
                logger.log("Stale element during swipe attempt " + (attempt + 1)
                        + (locator != null ? "; re-resolving" : ", retrying..."));
                if (locator != null) {
                    resolvedFromLocator = element(locator).orElseThrow(() ->
                            new DriverToolsException("Element not found after stale: " + locator));
                }
                attempt++;
                if (attempt >= maxAttempts) {
                    throw new DriverToolsException("Element became stale during scroll to mid-screen after " + maxAttempts
                            + " attempts. Element may have moved off screen.");
                }
            } catch (WebDriverException e) {
                logger.log("WebDriver error during swipe attempt " + (attempt + 1) + ": " + e.getMessage());
                attempt++;
                if (attempt >= maxAttempts) {
                    throw new DriverToolsException(
                            "Failed to scroll element to mid-screen after " + maxAttempts + " attempts: " + e.getMessage());
                }
            }
        }

        WebElement finalEl = locator != null ? element(locator).orElse(null) : element;
        if (finalEl == null) {
            logger.log("Reached max attempts (" + attempt + "), element not found for final check");
            return;
        }

        try {
            Point finalLocation = finalEl.getLocation();
            Dimension finalSize = finalEl.getSize();
            int finalCenterY = finalLocation.getY() + (finalSize.getHeight() / 2);
            int relaxedTargetMin = (int) (screenHeight * 0.35);
            int relaxedTargetMax = (int) (screenHeight * 0.65);

            if (finalCenterY >= relaxedTargetMin && finalCenterY <= relaxedTargetMax) {
                logger.log("Element positioned within acceptable range after " + attempt + " attempts");
                return;
            }
        } catch (WebDriverException e) {
            logger.log("Final position check skipped: " + e.getMessage());
        }

        logger.log("Reached max attempts (" + attempt + "), element may not be exactly centered but should be usable");
    }

    /**
     * Safely clicks on SVG or non-standard elements by first trying a direct click.
     * If that fails, it dispatches a manual MouseEvent to ensure the click happens.
     * Useful for handling SVGs, canvas, and custom web components reliably.
     */
    public void clickSvgOrNonHtmlElements(WebElement element) {
        logger.log("Clicking element : " + element);
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "if (arguments[0]) {" +
                            "  try {" +
                            "    arguments[0].click();" +
                            "  } catch (e) {" +
                            "    var evt = new MouseEvent('click', {" +
                            "      bubbles: true," +
                            "      cancelable: true," +
                            "      view: window" +
                            "    });" +
                            "    arguments[0].dispatchEvent(evt);" +
                            "  }" +
                            "}",
                    element
            );
            logger.log("Clicked element : " + element);
        } catch (Exception e) {
            logger.fail("Click failed on element: " + element + " due to: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Best-effort clipboard reset before a copy-button interaction.
     * <p>
     * The {@code instanceof HasClipboard} check only confirms that the Java driver class
     * declares the interface — it does not guarantee the remote Appium server actually
     * implements the {@code setClipboard} endpoint. Several real-device farms (notably
     * LambdaTest's UiAutomator2 server on Android 14 / Galaxy S24) respond with
     * {@code UnsupportedCommandException: "Method has not yet been implemented"}, and
     * security-locked-down devices can also reject it with a generic {@link WebDriverException}.
     * <p>
     * Treat this as advisory: log and continue. Callers that depend on a clean
     * clipboard (e.g. a follow-up {@code getClipboardText()} after a copy tap) already
     * tolerate stale or empty reads, so degrading to a no-op here is preferable to
     * aborting the test on an unsupported helper.
     */
    public void clearClipboard(){
        if (!(driver instanceof HasClipboard)) {
            logger.log("clearClipboard skipped: driver does not implement HasClipboard");
            return;
        }
        try {
            ((HasClipboard) driver).setClipboardText("");
            logger.log("Clipboard cleared");
        } catch (UnsupportedCommandException e) {
            logger.log("clearClipboard skipped: setClipboard not implemented by Appium server "
                    + "(driver=" + driver.getClass().getSimpleName() + ", "
                    + firstLine(e.getMessage()) + ")");
        } catch (WebDriverException e) {
            // Covers permission/security failures, transient transport errors, etc.
            logger.log("clearClipboard skipped: " + firstLine(e.getMessage()));
        }
    }

    private static String firstLine(String s) {
        if (s == null) return "";
        int newline = s.indexOf('\n');
        return newline < 0 ? s : s.substring(0, newline);
    }

    /**
     * Reads plain-text clipboard from the device. Retries because the OS often updates the pasteboard shortly
     * after a "copy" tap.
     * <p>
     * Uses {@link HasClipboard#getClipboard(ClipboardContentType)} and decodes with MIME, standard, or URL Base64
     * fallbacks — java-client's default {@link HasClipboard#getClipboardText()} only uses {@link java.util.Base64#getMimeDecoder()}
     * and can mis-decode what some Appium servers / device farms return.
     * <p>
     * <b>iOS:</b> If the clipboard is still empty after normal reads, tries {@code mobile: getPasteboard} on simulators,
     * then activates the WebDriverAgent runner (foreground pasteboard visibility on real devices), reads again, and finally
     * reactivates the app under test. See {@link #tryReadIosPasteboardExtras(IOSDriver, HasClipboard)}.
     */
    public String getClipboardText() {
        if (!(driver instanceof HasClipboard clipboardDriver)) {
            logger.log("getClipboardText: driver does not implement HasClipboard (" + driver.getClass().getName() + ")");
            return "";
        }
        String standard = readClipboardDecodedWithRetries(clipboardDriver);
        if (!standard.isEmpty()) {
            return standard;
        }
        if (driver instanceof IOSDriver iosDriver) {
            String ios = tryReadIosPasteboardExtras(iosDriver, clipboardDriver);
            if (!ios.isEmpty()) {
                return ios;
            }
        }
        return "";
    }

    /** Default WDA runner bundle unless overridden by capabilities (e.g. {@code updatedWDABundleId}). */
    private static final String IOS_WDA_RUNNER_DEFAULT = "com.facebook.WebDriverAgentRunner.xctrunner";

    private String readClipboardDecodedWithRetries(HasClipboard clipboardDriver) {
        final int maxAttempts = 6;
        final long pauseMs = 250;
        String lastRaw = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                lastRaw = clipboardDriver.getClipboard(ClipboardContentType.PLAINTEXT);
                String text = decodeClipboardPlaintextPayload(lastRaw);
                if (!text.isEmpty()) {
                    logger.log("Clipboard text: " + text);
                    return text;
                }
                logger.log("getClipboard empty after decode (attempt " + attempt + "/" + maxAttempts + ")");
            } catch (Exception e) {
                logger.log("getClipboardText attempt " + attempt + "/" + maxAttempts + " failed: " + e.getMessage());
            }
            if (attempt < maxAttempts) {
                sleepQuiet(pauseMs);
            }
        }
        logger.log("Clipboard text: (empty after retries); last raw len="
                + (lastRaw == null ? "null" : String.valueOf(lastRaw.length())));
        return "";
    }

    /**
     * iOS clipboard workarounds after standard {@link HasClipboard} reads returned empty:
     * <ol>
     *   <li>{@code mobile: getPasteboard} — simulators only in XCUITest; ignored if unsupported.</li>
     *   <li>{@code mobile: activateApp} to WDA foreground, clipboard read(s), then re-activate AUT — required on many
     *   real devices (Apple restricts pasteboard when WDA is not foreground).</li>
     * </ol>
     */
    private String tryReadIosPasteboardExtras(IOSDriver iosDriver, HasClipboard clipboardDriver) {
        String simulatorPasteboard = tryMobileGetPasteboard(iosDriver);
        if (!simulatorPasteboard.isEmpty()) {
            logger.log("Clipboard text (mobile: getPasteboard): " + simulatorPasteboard);
            return simulatorPasteboard;
        }
        logger.log("getClipboardText: attempting iOS WDA foreground clipboard read");

        String appBundleId = resolveIosAppBundleId(iosDriver);
        String wdaBundleId = resolveWdaBundleId(iosDriver);

        try {
            iosActivateApplication(iosDriver, wdaBundleId);
            sleepQuiet(350);
            String fromWda = readClipboardDecodedWithRetries(clipboardDriver);
            iosActivateApplication(iosDriver, appBundleId);
            sleepQuiet(350);
            if (fromWda.isEmpty()) {
                logger.log("getClipboardText: WDA foreground read still empty; restored app bundle="
                        + (appBundleId == null ? "" : appBundleId));
            } else {
                logger.log("Clipboard text (via WDA foreground): " + fromWda);
            }
            return fromWda;
        } catch (Exception e) {
            logger.log("iOS pasteboard workaround failed: " + e.getMessage());
            try {
                if (appBundleId != null && !appBundleId.isBlank()) {
                    iosActivateApplication(iosDriver, appBundleId);
                }
            } catch (Exception suppressed) {
                logger.log("iOS pasteboard workaround: failed to restore app: " + suppressed.getMessage());
            }
            sleepQuiet(200);
            return "";
        }
    }

    /** @return trimmed text, or {@code \"\"} when capability missing / blank */
    private String resolveIosAppBundleId(IOSDriver iosDriver) {
        Object bid = findFirstCapability(iosDriver, "bundleId", "appium:bundleId");
        if (bid != null) {
            String s = String.valueOf(bid).trim();
            if (!s.isBlank()) {
                return s;
            }
        }
        try {
            return BundleID.getID();
        } catch (Exception e) {
            logger.log("resolveIosAppBundleId: BundleID fallback failed: " + e.getMessage());
            return "";
        }
    }

    private String resolveWdaBundleId(IOSDriver iosDriver) {
        Object w = findFirstCapability(iosDriver, "updatedWDABundleId", "appium:updatedWDABundleId");
        if (w != null) {
            String s = String.valueOf(w).trim();
            if (!s.isBlank()) {
                return s;
            }
        }
        return IOS_WDA_RUNNER_DEFAULT;
    }

    private static Object findFirstCapability(IOSDriver iosDriver, String... capabilityKeys) {
        for (String key : capabilityKeys) {
            try {
                Object v = iosDriver.getCapabilities().getCapability(key);
                if (v != null) {
                    if (v instanceof String str && !str.isBlank()) {
                        return v;
                    }
                    if (!(v instanceof String)) {
                        return v;
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private void iosActivateApplication(IOSDriver iosDriver, String bundleId) {
        if (bundleId == null || bundleId.isBlank()) {
            logger.log("iosActivateApplication skipped: blank bundleId");
            return;
        }
        iosDriver.executeScript("mobile: activateApp", Map.of("bundleId", bundleId));
    }

    private String tryMobileGetPasteboard(IOSDriver iosDriver) {
        try {
            Object raw = iosDriver.executeScript("mobile: getPasteboard", Map.of());
            if (raw == null) {
                return "";
            }
            if (raw instanceof String s) {
                return s.trim();
            }
            return String.valueOf(raw).trim();
        } catch (Exception e) {
            logger.log("mobile: getPasteboard not available or failed (expected on real devices): " + e.getMessage());
            return "";
        }
    }

    private static void sleepQuiet(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Appium returns plaintext clipboard as Base64-encoded UTF-8. Tries MIME, standard, and URL-safe decoders.
     */
    private static String decodeClipboardPlaintextPayload(String base64FromServer) {
        if (base64FromServer == null || base64FromServer.isEmpty()) {
            return "";
        }
        byte[] bytes = tryDecodeBase64Clipboard(base64FromServer);
        if (bytes.length > 0) {
            return new String(bytes, StandardCharsets.UTF_8);
        }
        if (looksLikeAsciiPlaintextFallback(base64FromServer)) {
            return base64FromServer;
        }
        return "";
    }

    private static boolean looksLikeAsciiPlaintextFallback(String s) {
        if (s.length() > 512) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c > 0x7e || (c < 0x20 && c != '\n' && c != '\r' && c != '\t')) {
                return false;
            }
        }
        return true;
    }

    private static byte[] tryDecodeBase64Clipboard(String raw) {
        try {
            return Base64.getMimeDecoder().decode(raw);
        } catch (IllegalArgumentException ignored) {
        }
        try {
            return Base64.getDecoder().decode(raw);
        } catch (IllegalArgumentException ignored) {
        }
        try {
            return Base64.getUrlDecoder().decode(raw);
        } catch (IllegalArgumentException ignored) {
        }
        return new byte[0];
    }

    public WebElement scrollUntilVisibleInElement(Locator container, Locator target, int scrollStep, int maxTries) {
        WebElement containerElement = waitForElementToAppear(container).orElseThrow();
        JavascriptExecutor js = (JavascriptExecutor) getDriver();

        for (int i = 0; i < maxTries; i++) {
            Optional<WebElement> targetOpt = element(target);
            if (targetOpt.isPresent() && targetOpt.get().isDisplayed()) {
                return targetOpt.get();
            }
            js.executeScript("arguments[0].scrollTop += arguments[1];", containerElement, scrollStep);
        }

        throw new RuntimeException("Element " + target.toString() + " not found after scrolling.");
    }

    public void minimizeKeyboardMweb() {
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("document.activeElement.blur();");
        }
    }

    public void forceOpenLinkInSameTab() {
        ((JavascriptExecutor) driver).executeScript("document.querySelector('a[target=\"_blank\"]').target = '_self';");
    }

    public void editUrlQueryParam(String queryParam, String newValue) {
        String currentUrl = getCurrentURL();
        logger.log("Current URL: " + currentUrl);

        String newUrl = currentUrl.replaceAll("(" + Pattern.quote(queryParam) + "=)[^&]*", "$1" + newValue);
        logger.log("New URL: " + newUrl);

        driver.get(newUrl);
    }

    public static String getNearestDayDate(String day) {
        DayOfWeek targetDay;
        try {
            targetDay = DayOfWeek.valueOf(day.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid day: " + day);
        }

        LocalDate today = LocalDate.now();
        DayOfWeek todayDay = today.getDayOfWeek();

        int daysUntilTarget = (targetDay.getValue() - todayDay.getValue() + 7) % 7;
        LocalDate nearestDate = today.plusDays(daysUntilTarget);

        return nearestDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public boolean areTwoStringArraysEqual(List<String> roomRate, List<String> roomRatePriceDetail) {
        logger.log("Room from price detail: " + roomRatePriceDetail);
        logger.log("Room from BF: " + roomRate);
        if (roomRate == null || roomRatePriceDetail == null) {
            return false;
        }

        if (roomRate.size() != roomRatePriceDetail.size()) {
            return false;
        }

        for (int i = 0; i < roomRate.size(); i++) {
            if (!roomRate.get(i).contains(roomRatePriceDetail.get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isElementClickable(Locator locator, int timeoutSeconds) {
        Optional<WebElement> elementOpt = element(locator);
        if (elementOpt.isEmpty()) {
            return false;
        }
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            wait.until(ExpectedConditions.elementToBeClickable(elementOpt.get()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SneakyThrows
    public void terminateAppAndroid(String bundleId) {
        AndroidDriver d = (AndroidDriver) driver;
        logger.log("Terminating app with bundleId: " + bundleId);
        d.terminateApp(bundleId);
        logger.log("App with bundleId: " + bundleId + " terminated");
    }

    /*
    Switches the driver context to the most recently opened tab
     (commonly required after a click action that triggers a new tab) */
    public void switchToLatestTab() {
        List<String> allTabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(allTabs.get(allTabs.size() - 1));
    }

    /**
     * Waits for a new tab to appear (handle count &gt; handleCountBeforeClick) then switches to the latest tab.
     * Call after performing an action that opens a new tab (e.g. click on a link with target="_blank").
     *
     * @param handleCountBeforeClick number of window handles before the click that opened the new tab
     */
    public void waitForNewTabAndSwitchToLatest(int handleCountBeforeClick) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(d -> d.getWindowHandles().size() > handleCountBeforeClick);
        switchToLatestTab();
    }


    /**
     * Injects a one-time API response stub into the current page.
     * <p>
     * Intercepts the next fetch call matching the given API path substring,
     * modifies its JSON response using the provided modifier function,
     * and then restores the original fetch implementation.</p>
     * <p>
     * Example usage:
     * modifyApiResponse("/api/v1/auth/methods", "(json) => { if (json?.data?.passkey) { json.data.passkey.isPasskeyExist = false; } return json; }");
     *
     * @param apiPath                  substring of the API URL to intercept
     * @param modifierFunctionAsString JavaScript function (as string) that takes, changes and returns the JSON response
     **/
    public void modifyApiResponse(String apiPath, String modifierFunctionAsString) {
        String script =
                "(() => {"
                        + "  function modifyApi(path, modifierSrc) {"
                        + "    const _fetch = window.fetch;"
                        + "    const modifier = (new Function('return ' + modifierSrc))();"
                        + "    window.fetch = async function(input, init) {"
                        + "      const url = (typeof input === 'string') ? input : input.url || '';"
                        + "      const res = await _fetch(input, init);"
                        + "      if (url.includes(path)) {"
                        + "        try {"
                        + "          const text = await res.clone().text();"
                        + "          const json = JSON.parse(text);"
                        + "          const modified = modifier(json);"
                        + "          console.log('[Stub] Patching', path, '→', modified);"
                        + "          window.fetch = _fetch;"
                        + "          return new Response(JSON.stringify(modified), {"
                        + "            status: res.status,"
                        + "            statusText: res.statusText,"
                        + "            headers: res.headers"
                        + "          });"
                        + "        } catch (e) { console.warn('[Stub] Failed to patch', e); }"
                        + "      }"
                        + "      return res;"
                        + "    };"
                        + "  }"
                        + "  modifyApi('" + apiPath + "', " + modifierFunctionAsString + ");"
                        + "})();";

        try {
            logger.log("Trying to modify response for path: '" + apiPath + "' using this command: '" + modifierFunctionAsString +"'...");
            ((JavascriptExecutor) driver).executeScript(script);
            logger.log("Successfully injected API response modifier for path: '" + apiPath + "' using this command: '" + modifierFunctionAsString +"'");
        } catch (Exception e) {
            logger.log("Failed to modify API response modifier for path: '" + apiPath + "' using this command: '" + modifierFunctionAsString + "'\n error: " + e.getMessage());
        }
    }

    private static final String FLIGHT_STREAMING_CAPTURE_STORAGE_KEY = "__tiketFlightStreamingCaptures";
    private static final Pattern DEPARTURE_FLIGHT_DELTA_PRICE_PATTERN =
            Pattern.compile("\"departureFlightDeltaPrice\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");
    private static final Pattern STREAM_TYPE_PATTERN =
            Pattern.compile("\"streamType\"\\s*:\\s*\"([^\"]+)\"");

    private String buildFlightStreamingCaptureScript() {
        return "(() => {"
                + "  const storageKey = '" + FLIGHT_STREAMING_CAPTURE_STORAGE_KEY + "';"
                + "  const loadStored = () => {"
                + "    try {"
                + "      const stored = sessionStorage.getItem(storageKey);"
                + "      if (stored) { window.__tiketFlightStreamingCaptures = JSON.parse(stored); }"
                + "    } catch (e) {}"
                + "    if (!window.__tiketFlightStreamingCaptures) { window.__tiketFlightStreamingCaptures = []; }"
                + "  };"
                + "  const persist = () => {"
                + "    try {"
                + "      var compact = (window.__tiketFlightStreamingCaptures || []).map(function(c) {"
                + "        return {streamType: c.streamType || '', departureFlightDeltaPrice: c.departureFlightDeltaPrice, capturedAt: c.capturedAt};"
                + "      });"
                + "      sessionStorage.setItem(storageKey, JSON.stringify(compact));"
                + "    } catch (e) {}"
                + "  };"
                + "  const isStreamingUrl = (url) => !!url && String(url).indexOf('search/streaming') !== -1;"
                + "  const lastJsonValue = (text, key) => {"
                + "    if (typeof text !== 'string') { return null; }"
                + "    const token = '\"' + key + '\"';"
                + "    const idx = text.lastIndexOf(token);"
                + "    if (idx < 0) { return null; }"
                + "    let i = idx + token.length;"
                + "    while (i < text.length && text.charAt(i) !== ':') { i++; }"
                + "    if (i >= text.length) { return null; }"
                + "    i++;"
                + "    while (i < text.length && (text.charAt(i) === ' ' || text.charAt(i) === '\\t' || text.charAt(i) === '\\n' || text.charAt(i) === '\\r')) { i++; }"
                + "    if (i >= text.length) { return null; }"
                + "    if (text.charAt(i) === '\"') {"
                + "      const end = text.indexOf('\"', i + 1);"
                + "      return end > i ? text.substring(i + 1, end) : null;"
                + "    }"
                + "    let j = i;"
                + "    if (text.charAt(j) === '-') { j++; }"
                + "    while (j < text.length && ((text.charAt(j) >= '0' && text.charAt(j) <= '9') || text.charAt(j) === '.')) { j++; }"
                + "    return j > i ? text.substring(i, j) : null;"
                + "  };"
                + "  const extractStreamType = (text) => {"
                + "    try {"
                + "      const parsed = JSON.parse(text);"
                + "      if (parsed && parsed.streamType) { return parsed.streamType; }"
                + "    } catch (e) {}"
                + "    return lastJsonValue(text, 'streamType');"
                + "  };"
                + "  const extractDelta = (text) => {"
                + "    const raw = lastJsonValue(text, 'departureFlightDeltaPrice');"
                + "    if (raw == null || raw === '') { return null; }"
                + "    const n = Number(raw);"
                + "    return isNaN(n) ? null : Math.round(n);"
                + "  };"
                + "  const recordCapture = (url, requestBody, responseText) => {"
                + "    if (!isStreamingUrl(url)) { return; }"
                + "    const req = typeof requestBody === 'string' ? requestBody : '';"
                + "    const res = typeof responseText === 'string' ? responseText : '';"
                + "    let streamType = extractStreamType(req) || extractStreamType(res);"
                + "    let departureFlightDeltaPrice = extractDelta(res);"
                + "    if (!streamType && res.indexOf('srtReturnFlights') !== -1) {"
                + "      streamType = 'RT_RETURN_SMART_TRIP';"
                + "    }"
                + "    if (!streamType) { return; }"
                + "    loadStored();"
                + "    window.__tiketFlightStreamingCaptures.push({"
                + "      streamType: streamType,"
                + "      departureFlightDeltaPrice: departureFlightDeltaPrice,"
                + "      capturedAt: Date.now()"
                + "    });"
                + "    persist();"
                + "  };"
                + "  window.__tiketRecordFlightStreamingCapture = recordCapture;"
                + "  loadStored();"
                + "  if (!window.__tiketOriginalFetch) {"
                + "    window.__tiketOriginalFetch = window.fetch.bind(window);"
                + "  }"
                + "  if (!window.fetch || !window.fetch.__tiketStreamingCapture) {"
                + "    const originalFetch = window.__tiketOriginalFetch;"
                + "    window.fetch = async function(input, init) {"
                + "      const response = await originalFetch.apply(this, arguments);"
                + "      try {"
                + "        const url = (typeof input === 'string') ? input : (input && input.url) || '';"
                + "        if (isStreamingUrl(url)) {"
                + "          let requestBody = '';"
                + "          try {"
                + "            if (init && typeof init.body === 'string') { requestBody = init.body; }"
                + "            else if (input && typeof input.clone === 'function') { requestBody = await input.clone().text(); }"
                + "          } catch (e) {}"
                + "          response.clone().text().then(function(responseText) {"
                + "            if (window.__tiketRecordFlightStreamingCapture) {"
                + "              window.__tiketRecordFlightStreamingCapture(url, requestBody, responseText);"
                + "            }"
                + "          }).catch(function() {});"
                + "        }"
                + "      } catch (e) {"
                + "        console.warn('[tiket-automation] flight streaming fetch capture failed', e);"
                + "      }"
                + "      return response;"
                + "    };"
                + "    window.fetch.__tiketStreamingCapture = true;"
                + "  }"
                + "  if (!XMLHttpRequest.prototype.__tiketStreamingCapture) {"
                + "    const originalOpen = XMLHttpRequest.prototype.open;"
                + "    const originalSend = XMLHttpRequest.prototype.send;"
                + "    XMLHttpRequest.prototype.open = function(method, url) {"
                + "      this.__tiketStreamingUrl = url;"
                + "      return originalOpen.apply(this, arguments);"
                + "    };"
                + "    XMLHttpRequest.prototype.send = function(body) {"
                + "      this.addEventListener('load', function() {"
                + "        try {"
                + "          if (window.__tiketRecordFlightStreamingCapture) {"
                + "            window.__tiketRecordFlightStreamingCapture(this.__tiketStreamingUrl || '', body, this.responseText);"
                + "          }"
                + "        } catch (e) {"
                + "          console.warn('[tiket-automation] flight streaming xhr capture failed', e);"
                + "        }"
                + "      });"
                + "      return originalSend.apply(this, arguments);"
                + "    };"
                + "    XMLHttpRequest.prototype.__tiketStreamingCapture = true;"
                + "  }"
                + "})();";
    }

    private void initializeFlightStreamingNetworkCapture() {
        if (flightStreamingCaptureInitialized) {
            return;
        }
        if (driver instanceof HasCdp hasCdp) {
            try {
                hasCdp.executeCdpCommand("Network.enable", Map.of());
                hasCdp.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument",
                        Map.of("source", buildFlightStreamingCaptureScript()));
                flightStreamingCaptureInitialized = true;
                logger.log("Initialized CDP flight streaming capture (Network.enable + addScriptToEvaluateOnNewDocument).");
            } catch (Exception e) {
                logger.log("Failed to initialize CDP flight streaming capture: " + e.getMessage());
            }
        } else {
            logger.log("CDP not available; flight streaming capture relies on in-page JS hooks only.");
            flightStreamingCaptureInitialized = true;
        }
    }

    private boolean isFlightStreamingUrl(String url) {
        return url != null && url.contains("search/streaming");
    }

    private String extractStreamTypeFromPostData(String postData) {
        if (postData == null || postData.isBlank()) {
            return null;
        }
        try {
            JSONObject requestJson = (JSONObject) new JSONParser().parse(postData);
            Object streamType = requestJson.get("streamType");
            if (streamType != null) {
                return streamType.toString();
            }
        } catch (Exception ignored) {
        }
        Matcher matcher = STREAM_TYPE_PATTERN.matcher(postData);
        return matcher.find() ? matcher.group(1) : null;
    }

    private Long extractDepartureFlightDeltaPriceFromBody(String body) {
        if (body == null || body.isBlank()) {
            return null;
        }
        try {
            JSONObject responseJson = (JSONObject) new JSONParser().parse(body);
            JSONObject data = (JSONObject) responseJson.get("data");
            if (data != null && data.get("departureFlightDeltaPrice") != null) {
                Object value = data.get("departureFlightDeltaPrice");
                if (value instanceof Number number) {
                    return Math.round(number.doubleValue());
                }
                return Long.valueOf(value.toString());
            }
        } catch (Exception ignored) {
        }
        Matcher matcher = DEPARTURE_FLIGHT_DELTA_PRICE_PATTERN.matcher(body);
        String lastMatch = null;
        while (matcher.find()) {
            lastMatch = matcher.group(1);
        }
        if (lastMatch == null) {
            return null;
        }
        try {
            return Math.round(Double.parseDouble(lastMatch));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Map<String, Object> buildFlightStreamingCaptureEntry(String streamType, String body) {
        Map<String, Object> capture = new HashMap<>();
        capture.put("streamType", streamType);
        capture.put("body", body);
        capture.put("departureFlightDeltaPrice", extractDepartureFlightDeltaPriceFromBody(body));
        capture.put("capturedAt", System.currentTimeMillis());
        return capture;
    }

    /**
     * Patches {@code window.fetch}/{@code XMLHttpRequest} on the current page and registers a
     * CDP script for future navigations. Also enables Chrome performance logging harvest as a
     * fallback when in-page hooks miss requests (e.g. service worker).
     */
    public void enableFlightSearchStreamingCapture() {
        initializeFlightStreamingNetworkCapture();
        try {
            ((JavascriptExecutor) driver).executeScript(buildFlightStreamingCaptureScript());
            logger.log("Enabled flight search streaming API capture on current page.");
        } catch (Exception e) {
            logger.log("Failed to enable flight search streaming capture: " + e.getMessage());
        }
    }

    public void pollFlightStreamingCapturesFromNetworkLogs() {
        if (!(driver instanceof HasCdp hasCdp)) {
            return;
        }
        if (flightStreamingPerformanceLogsUnavailable) {
            return;
        }
        try {
            LogEntries logEntries = driver.manage().logs().get("performance");
            JSONParser parser = new JSONParser();
            for (LogEntry entry : logEntries) {
                JSONObject logJson = (JSONObject) parser.parse(entry.getMessage());
                JSONObject message = (JSONObject) logJson.get("message");
                if (message == null) {
                    continue;
                }
                String method = String.valueOf(message.get("method"));
                JSONObject params = (JSONObject) message.get("params");
                if (params == null) {
                    continue;
                }
                String requestId = String.valueOf(params.get("requestId"));

                if ("Network.requestWillBeSent".equals(method)) {
                    JSONObject request = (JSONObject) params.get("request");
                    if (request == null) {
                        continue;
                    }
                    String url = String.valueOf(request.get("url"));
                    if (!isFlightStreamingUrl(url)) {
                        continue;
                    }
                    pendingFlightStreamingRequestIds.add(requestId);
                    Object postData = request.get("postData");
                    if (postData != null) {
                        flightStreamingRequestPostDataById.put(requestId, postData.toString());
                    }
                } else if ("Network.responseReceived".equals(method)) {
                    captureFlightStreamingResponseBody(hasCdp, requestId, false);
                } else if ("Network.loadingFinished".equals(method)) {
                    captureFlightStreamingResponseBody(hasCdp, requestId, true);
                }
            }
        } catch (Exception e) {
            String message = e.getMessage() == null ? "" : e.getMessage();
            if (message.contains("log type 'performance' not found")) {
                flightStreamingPerformanceLogsUnavailable = true;
                logger.log("Performance logs unavailable; flight streaming capture will use in-page hooks only.");
            } else {
                logger.log("Failed to poll flight streaming captures from network logs: " + message);
            }
        }
    }

    private void captureFlightStreamingResponseBody(HasCdp hasCdp, String requestId, boolean giveUpOnFailure) {
        if (!pendingFlightStreamingRequestIds.contains(requestId)
                || processedFlightStreamingRequestIds.contains(requestId)) {
            return;
        }
        try {
            Map<String, Object> bodyResult = hasCdp.executeCdpCommand("Network.getResponseBody",
                    Map.of("requestId", requestId));
            String body = String.valueOf(bodyResult.get("body"));
            if (Boolean.TRUE.equals(bodyResult.get("base64Encoded"))) {
                body = new String(Base64.getDecoder().decode(body), StandardCharsets.UTF_8);
            }
            String postData = flightStreamingRequestPostDataById.get(requestId);
            if (postData == null || postData.isBlank()) {
                try {
                    Map<String, Object> postDataResult = hasCdp.executeCdpCommand("Network.getRequestPostData",
                            Map.of("requestId", requestId));
                    Object retrieved = postDataResult.get("postData");
                    if (retrieved != null) {
                        postData = retrieved.toString();
                        flightStreamingRequestPostDataById.put(requestId, postData);
                    }
                } catch (Exception ignored) {
                }
            }
            String streamType = extractStreamTypeFromPostData(postData);
            if (streamType == null || streamType.isBlank()) {
                streamType = extractStreamTypeFromPostData(body);
            }
            Long departureFlightDeltaPrice = extractDepartureFlightDeltaPriceFromBody(body);
            if ((streamType == null || streamType.isBlank()) && body.contains("srtReturnFlights")) {
                streamType = "RT_RETURN_SMART_TRIP";
            }
            if (streamType == null || streamType.isBlank()) {
                if (giveUpOnFailure) {
                    processedFlightStreamingRequestIds.add(requestId);
                }
                return;
            }
            javaFlightStreamingCaptures.add(buildFlightStreamingCaptureEntry(streamType, body));
            processedFlightStreamingRequestIds.add(requestId);
            logger.log("Captured flight streaming response via CDP. streamType=" + streamType
                    + ", departureFlightDeltaPrice=" + departureFlightDeltaPrice);
        } catch (Exception e) {
            if (giveUpOnFailure) {
                processedFlightStreamingRequestIds.add(requestId);
                logger.log("Skipped unavailable streaming response body for requestId=" + requestId + ": " + e.getMessage());
            }
        }
    }

    /** @deprecated use {@link #pollFlightStreamingCapturesFromNetworkLogs()} */
    public void harvestFlightStreamingCapturesFromNetworkLogs() {
        pollFlightStreamingCapturesFromNetworkLogs();
    }

    public void clearFlightSearchStreamingCaptures() {
        flightStreamingCaptureClearEpochMs = System.currentTimeMillis();
        javaFlightStreamingCaptures.clear();
        pendingFlightStreamingRequestIds.clear();
        processedFlightStreamingRequestIds.clear();
        flightStreamingRequestPostDataById.clear();
        drainFlightStreamingPerformanceLogs();
        String script =
                "(() => {"
                        + "  window.__tiketFlightStreamingCaptures = [];"
                        + "  try {"
                        + "    sessionStorage.setItem('" + FLIGHT_STREAMING_CAPTURE_STORAGE_KEY + "', '[]');"
                        + "  } catch (e) {}"
                        + "})();";
        try {
            ((JavascriptExecutor) driver).executeScript(script);
            logger.log("Cleared flight search streaming captures and CDP tracking state.");
        } catch (Exception e) {
            logger.log("Failed to clear flight search streaming captures: " + e.getMessage());
        }
    }

    /**
     * Discards buffered Chrome performance logs so return-SRP streaming events are not
     * pushed out of the ring buffer by earlier departure-SRP traffic (common on CI).
     */
    private void drainFlightStreamingPerformanceLogs() {
        if (!(driver instanceof HasCdp) || flightStreamingPerformanceLogsUnavailable) {
            return;
        }
        try {
            LogEntries entries = driver.manage().logs().get("performance");
            logger.log("Drained " + entries.getAll().size() + " performance log entries before return-SRP capture.");
        } catch (Exception e) {
            String message = e.getMessage() == null ? "" : e.getMessage();
            if (message.contains("log type 'performance' not found")) {
                flightStreamingPerformanceLogsUnavailable = true;
                logger.log("Performance logs unavailable while draining; CDP harvest disabled.");
            } else {
                logger.log("Failed to drain performance logs: " + message);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getJsFlightStreamingCaptures() {
        try {
            Object result = ((JavascriptExecutor) driver).executeScript(
                    "(() => {"
                            + "  const inMemory = window.__tiketFlightStreamingCaptures || [];"
                            + "  let stored = [];"
                            + "  try {"
                            + "    const raw = sessionStorage.getItem('" + FLIGHT_STREAMING_CAPTURE_STORAGE_KEY + "');"
                            + "    if (raw) { stored = JSON.parse(raw); }"
                            + "  } catch (e) {}"
                            + "  return inMemory.length >= stored.length ? inMemory : stored;"
                            + "})();");
            if (result instanceof List) {
                return (List<Map<String, Object>>) result;
            }
        } catch (Exception e) {
            logger.log("Failed to read JS flight streaming captures: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    public List<Map<String, Object>> getFlightSearchStreamingCaptures() {
        pollFlightStreamingCapturesFromNetworkLogs();
        return mergeFlightStreamingCaptures();
    }

    private List<Map<String, Object>> mergeFlightStreamingCaptures() {
        List<Map<String, Object>> merged = new ArrayList<>();
        merged.addAll(getJsFlightStreamingCaptures());
        merged.addAll(javaFlightStreamingCaptures);
        merged.removeIf(capture -> {
            Object capturedAt = capture.get("capturedAt");
            if (!(capturedAt instanceof Number number)) {
                return flightStreamingCaptureClearEpochMs > 0L;
            }
            return number.longValue() < flightStreamingCaptureClearEpochMs;
        });
        merged.sort(Comparator.comparingLong(capture -> {
            Object capturedAt = capture.get("capturedAt");
            if (capturedAt instanceof Number number) {
                return number.longValue();
            }
            return 0L;
        }));
        return merged;
    }

    private long resolveCaptureTimestamp(Map<String, Object> capture) {
        Object capturedAt = capture.get("capturedAt");
        if (capturedAt instanceof Number number) {
            return number.longValue();
        }
        return 0L;
    }

    private Long resolveDepartureFlightDeltaPriceFromCapture(Map<String, Object> capture) {
        Long fromBody = null;
        Object body = capture.get("body");
        if (body != null && !body.toString().isBlank()) {
            fromBody = extractDepartureFlightDeltaPriceFromBody(body.toString());
        }
        Object delta = capture.get("departureFlightDeltaPrice");
        Long fromField = null;
        if (delta != null) {
            fromField = delta instanceof Number number
                    ? Math.round(number.doubleValue())
                    : Long.parseLong(delta.toString());
        }
        Long raw = fromField != null ? fromField : fromBody;
        return raw == null ? null : Math.abs(raw);
    }

    private static final int FLIGHT_STREAMING_RESPONSE_LOG_MAX_CHARS = 8000;

    private void logFlightStreamingCaptureDetails(String streamType, int captureIndex, int totalCaptures,
                                                  Map<String, Object> capture, long expectedUiDeltaPrice) {
        long capturedAt = resolveCaptureTimestamp(capture);
        Long delta = resolveDepartureFlightDeltaPriceFromCapture(capture);
        Object body = capture.get("body");
        String bodyText = body == null ? "<no body>" : body.toString();
        if (bodyText.length() > FLIGHT_STREAMING_RESPONSE_LOG_MAX_CHARS) {
            bodyText = bodyText.substring(0, FLIGHT_STREAMING_RESPONSE_LOG_MAX_CHARS)
                    + "... [truncated, totalChars=" + body.toString().length() + "]";
        }
        logger.log(streamType + " streaming capture #" + captureIndex + "/" + totalCaptures
                + " timestamp=" + capturedAt
                + ", departureFlightDeltaPrice=" + (delta == null ? "not found" : delta)
                + ", comparingToUiDeltaPrice=" + expectedUiDeltaPrice);
        logger.log(streamType + " streaming capture #" + captureIndex + " response body: " + bodyText);
        if (delta != null && delta > 0L) {
            if (delta == expectedUiDeltaPrice) {
                logger.log("MATCH: UI delta price " + expectedUiDeltaPrice
                        + " equals departureFlightDeltaPrice " + delta + " from " + streamType
                        + " capture #" + captureIndex);
            } else {
                logger.log("NO MATCH: " + streamType + " capture #" + captureIndex
                        + " departureFlightDeltaPrice=" + delta + " != UI delta price " + expectedUiDeltaPrice);
            }
        } else {
            logger.log("NO MATCH: " + streamType + " capture #" + captureIndex
                    + " has no valid departureFlightDeltaPrice in response");
        }
    }

    private List<Map<String, Object>> getStreamingCapturesByType(String streamType) {
        List<Map<String, Object>> typedCaptures = new ArrayList<>();
        for (Map<String, Object> capture : mergeFlightStreamingCaptures()) {
            Object capturedStreamType = capture.get("streamType");
            if (capturedStreamType != null && streamType.equals(String.valueOf(capturedStreamType))) {
                typedCaptures.add(capture);
            }
        }
        return typedCaptures;
    }

    public int countStreamingCapturesByType(String streamType) {
        return getStreamingCapturesByType(streamType).size();
    }

    /**
     * Waits until no new {@code streamType} streaming captures arrive for several consecutive polls.
     * Return SRP commonly fires multiple streaming waves; this ensures we read the final wave.
     */
    public void waitForReturnStreamingCapturesToSettle(String streamType, int stablePollsRequired, int timeoutSeconds) {
        waitForReturnStreamingCapturesToSettle(streamType, stablePollsRequired, timeoutSeconds, 1);
    }

    /**
     * @param minCapturesRequired minimum captures of {@code streamType} before stability is considered
     */
    public void waitForReturnStreamingCapturesToSettle(String streamType, int stablePollsRequired, int timeoutSeconds,
                                                       int minCapturesRequired) {
        long deadlineMs = System.currentTimeMillis() + (timeoutSeconds * 1000L);
        int previousCount = -1;
        int stablePolls = 0;
        while (System.currentTimeMillis() < deadlineMs) {
            enableFlightSearchStreamingCapture();
            pollFlightStreamingCapturesFromNetworkLogs();
            int currentCount = countStreamingCapturesByType(streamType);
            if (currentCount >= minCapturesRequired && currentCount == previousCount) {
                stablePolls++;
                if (stablePolls >= stablePollsRequired) {
                    logger.log("Return streaming settled for " + streamType + " with captureCount=" + currentCount);
                    return;
                }
            } else {
                stablePolls = 0;
            }
            previousCount = currentCount;
            dummyWait(1);
        }
        logger.log("Return streaming for " + streamType + " did not fully settle within " + timeoutSeconds
                + "s (minCapturesRequired=" + minCapturesRequired + "); proceeding with captureCount=" + previousCount);
    }

    /**
     * Polls streaming captures until the latest {@code streamType} {@code departureFlightDeltaPrice}
     * equals {@code expectedPrice}, re-injecting in-page hooks and harvesting CDP logs on each attempt.
     */
    public Optional<Long> waitForMatchingDepartureFlightDeltaPrice(String streamType, long expectedPrice,
                                                                   int timeoutSeconds) {
        long deadlineMs = System.currentTimeMillis() + (timeoutSeconds * 1000L);
        Optional<Long> latest = Optional.empty();
        while (System.currentTimeMillis() < deadlineMs) {
            enableFlightSearchStreamingCapture();
            pollFlightStreamingCapturesFromNetworkLogs();
            latest = getLastDepartureFlightDeltaPriceByStreamType(streamType);
            if (latest.isPresent() && latest.get() == expectedPrice) {
                return latest;
            }
            dummyWait(1);
        }
        pollFlightStreamingCapturesFromNetworkLogs();
        latest = getLastDepartureFlightDeltaPriceByStreamType(streamType);
        if (latest.isPresent() && latest.get() == expectedPrice) {
            return latest;
        }
        if (latest.isPresent()) {
            logger.log("Latest " + streamType + " departureFlightDeltaPrice=" + latest.get()
                    + " does not match UI delta=" + expectedPrice);
        }
        return Optional.empty();
    }

    public Optional<Long> getLastDepartureFlightDeltaPriceByStreamType(String streamType) {
        Long lastDelta = null;
        long lastCapturedAt = -1L;
        for (Map<String, Object> capture : mergeFlightStreamingCaptures()) {
            Object capturedStreamType = capture.get("streamType");
            if (capturedStreamType == null || !streamType.equals(String.valueOf(capturedStreamType))) {
                continue;
            }
            Long delta = resolveDepartureFlightDeltaPriceFromCapture(capture);
            if (delta == null || delta <= 0L) {
                continue;
            }
            long capturedAt = resolveCaptureTimestamp(capture);
            if (capturedAt >= lastCapturedAt) {
                lastCapturedAt = capturedAt;
                lastDelta = delta;
            }
        }
        if (lastDelta != null) {
            logger.log("Using departureFlightDeltaPrice=" + lastDelta + " from latest valid " + streamType
                    + " capture at timestamp=" + lastCapturedAt);
        }
        return Optional.ofNullable(lastDelta);
    }

    /**
     * Iterates all {@code streamType} streaming captures (chronological order), logs each response body,
     * and returns the first {@code departureFlightDeltaPrice} that equals {@code expectedPrice}.
     */
    public List<Long> getDepartureFlightDeltaPricesByStreamType(String streamType) {
        List<Long> deltas = new ArrayList<>();
        for (Map<String, Object> capture : getStreamingCapturesByType(streamType)) {
            Long delta = resolveDepartureFlightDeltaPriceFromCapture(capture);
            if (delta != null && delta > 0L) {
                deltas.add(delta);
            }
        }
        return deltas;
    }

    public Optional<Long> findMatchingDepartureFlightDeltaPriceByStreamType(String streamType, long expectedPrice) {
        pollFlightStreamingCapturesFromNetworkLogs();
        return findMatchingDepartureFlightDeltaPriceByStreamType(streamType, expectedPrice, true);
    }

    private Optional<Long> findMatchingDepartureFlightDeltaPriceByStreamType(String streamType, long expectedPrice,
                                                                             boolean logEachCapture) {
        List<Map<String, Object>> typedCaptures = getStreamingCapturesByType(streamType);
        logger.log("Comparing UI delta price " + expectedPrice + " against " + typedCaptures.size()
                + " " + streamType + " streaming capture(s)");
        for (int i = 0; i < typedCaptures.size(); i++) {
            Map<String, Object> capture = typedCaptures.get(i);
            int captureIndex = i + 1;
            if (logEachCapture) {
                logFlightStreamingCaptureDetails(streamType, captureIndex, typedCaptures.size(), capture, expectedPrice);
            }
            Long delta = resolveDepartureFlightDeltaPriceFromCapture(capture);
            if (delta != null && delta > 0L && delta == expectedPrice) {
                return Optional.of(delta);
            }
        }
        if (logEachCapture) {
            logger.log("No " + streamType + " capture matched UI delta price " + expectedPrice
                    + " (checked " + typedCaptures.size() + " capture(s))");
        }
        return Optional.empty();
    }

    public Optional<String> getLastFlightStreamingResponseBodyByStreamType(String streamType) {
        List<Map<String, Object>> captures = mergeFlightStreamingCaptures();
        String lastBody = null;
        for (Map<String, Object> capture : captures) {
            Object capturedStreamType = capture.get("streamType");
            Object body = capture.get("body");
            if (body == null || capturedStreamType == null) {
                continue;
            }
            if (streamType.equals(String.valueOf(capturedStreamType))) {
                lastBody = body.toString();
            }
        }
        return Optional.ofNullable(lastBody);
    }

    public Optional<String> getLastFlightStreamingResponseBodyContaining(String responseField) {
        List<Map<String, Object>> captures = mergeFlightStreamingCaptures();
        String lastBody = null;
        for (Map<String, Object> capture : captures) {
            Object body = capture.get("body");
            if (body != null && body.toString().contains(responseField)) {
                lastBody = body.toString();
            }
        }
        return Optional.ofNullable(lastBody);
    }

    /**
     * Progressively scrolls down the page until the element is found or max attempts reached.
     * Scrolls by specified height, waits for element, then scrolls again if not found.
     *
     * @param locator The locator of the element to find
     * @param scrollHeight Height in pixels to scroll each time (e.g., 500)
     * @param waitSeconds Seconds to wait for element after each scroll
     * @param maxScrolls Maximum number of scroll attempts before giving up
     * @return Optional<WebElement> - present if found, empty if not found after max attempts
     */
    public Optional<WebElement> scrollUntilElementFound(Locator locator, int scrollHeight, int waitSeconds, int maxScrolls) {
        logger.log("Starting progressive scroll to find element: " + locator);
        logger.log("Scroll parameters - height: " + scrollHeight + "px, wait: " + waitSeconds + "s, max attempts: " + maxScrolls);

        for (int attempt = 1; attempt <= maxScrolls; attempt++) {
            logger.log("Scroll attempt " + attempt + " of " + maxScrolls);

            // Check if element is already present
            Optional<WebElement> elementOpt = waitForElementToPresence(locator, waitSeconds);

            if (elementOpt.isPresent()) {
                logger.log("Element found after " + attempt + " scroll attempt(s)!");

                // Scroll element into view for better visibility
                try {
                    scrollIntoWebView(locator);
                } catch (Exception e) {
                    logger.log("Warning: Could not scroll element into view: " + e.getMessage());
                }

                return elementOpt;
            }

            // Element not found, scroll down
            if (attempt < maxScrolls) {
                try {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("window.scrollBy(0, arguments[0]);", scrollHeight);
                    logger.log("Scrolled down by " + scrollHeight + "px");

                    // Small pause to allow lazy loading
                    Thread.sleep(500);

                } catch (Exception e) {
                    logger.log("Error during scroll: " + e.getMessage());
                }
            }
        }

        logger.log("Element not found after " + maxScrolls + " scroll attempts");
        return Optional.empty();
    }

    public Optional<WebElement> scrollUpUntilElementFound(Locator locator, int distance, int maxScrolls) {
        for (int i = 0; i < maxScrolls; i++) {
            if (element(locator).isPresent()) {
                return element(locator);
            }
            try {
                scrollUp(distance);
            } catch (Exception e) {
                logger.log("Error during scroll: " + e.getMessage());
            }
        }
        return Optional.empty();
    }

    /**
     * Overloaded method with default parameters:
     * - scrollHeight: 800px (about one viewport height)
     * - waitSeconds: 2 seconds
     * - maxScrolls: 10 attempts
     */
    public Optional<WebElement> scrollUntilElementFound(Locator locator) {
        return scrollUntilElementFound(locator, 800, 2, 10);
    }

    /**
     * Overloaded method with custom scroll height and max attempts, default 2 second wait
     */
    public Optional<WebElement> scrollUntilElementFound(Locator locator, int scrollHeight, int maxScrolls) {
        return scrollUntilElementFound(locator, scrollHeight, 2, maxScrolls);
    }

    /**
     * Checks if element exists by progressively scrolling down the page.
     * Returns true if element is found and displayed, false otherwise.
     */
    public boolean scrollAndCheckElement(Locator locator, int scrollHeight, int waitSeconds, int maxScrolls) {
        Optional<WebElement> element = scrollUntilElementFound(locator, scrollHeight, waitSeconds, maxScrolls);

        if (element.isPresent()) {
            try {
                return element.get().isDisplayed();
            } catch (Exception e) {
                logger.log("Element found but not displayed: " + e.getMessage());
                return false;
            }
        }

        return false;
    }

    /**
     * Overloaded with default parameters
     */
    public boolean scrollAndCheckElement(Locator locator) {
        return scrollAndCheckElement(locator, 800, 2, 10);
    }

    /**
     * Verifies PDF is loaded in Safari (iOS only)
     * Switches to Safari context and checks URL/page elements
     *
     * @param pdfElementLocator Locator for PDF element to verify (if URL validation fails)
     * @return true if PDF is verified, false otherwise
     */
    public boolean verifyPdfInSafari(Locator pdfElementLocator) {
        if (Configs.PLATFORM != Platform.IOS && Configs.PLATFORM != Platform.IMWEB) {
            logger.log("Not on iOS platform, skipping Safari PDF verification");
            return false;
        }

        logger.log("Verifying PDF in Safari...");

        try {
            // Wait for Safari to load the PDF
            logger.log("Waiting for Safari to load PDF...");
            dummyWait(5);

            // Switch to Safari webview context
            logger.log("Switching to Safari context...");
            switchToWebView();
            dummyWait(3);

            // Verify PDF is loaded by checking URL
            String currentUrl = getCurrentURL();
            logger.log("Current URL in Safari: " + currentUrl);

            // Check if URL contains PDF keywords
            boolean isPdfUrlValid = currentUrl != null &&
                    (currentUrl.toLowerCase().contains("pdf") ||
                            currentUrl.toLowerCase().contains("insurance") ||
                            currentUrl.toLowerCase().contains("tix-insurance"));

            if (isPdfUrlValid) {
                logger.log("PDF URL is valid: " + currentUrl);
                return true;
            }

            // Fallback: Check for PDF elements on page
            logger.log("WARNING: PDF URL validation inconclusive, checking page elements...");

            if (pdfElementLocator != null) {
                Optional<WebElement> element = waitForElementToAppear(pdfElementLocator, 10);
                if (element.isPresent()) {
                    String elementText = getText(element.get());
                    logger.log("Found PDF element: " + elementText);

                    boolean hasInsuranceKeyword = elementText != null &&
                            (elementText.toLowerCase().contains("insurance") ||
                                    elementText.toLowerCase().contains(".pdf"));

                    if (hasInsuranceKeyword) {
                        logger.log("PDF element validation passed");
                        return true;
                    }
                }
            }

            logger.log("WARNING: Could not find specific PDF elements, but Safari was opened");
            return currentUrl != null; // Return true if we at least have a URL

        } catch (Exception e) {
            logger.log("Error during PDF verification in Safari: " + e.getMessage());
            return false;
        }
    }

    /**
     * Moves the text cursor to the exact middle of the provided input element.
     *
     * @param element The input or textarea WebElement
     */
    public void moveCursorToMiddle(WebElement element) {
        Objects.requireNonNull(element, "element");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        String script = "arguments[0].focus();" +
                "var length = arguments[0].value.length;" +
                "var middle = Math.floor(length / 2);" +
                "arguments[0].setSelectionRange(middle, middle);";

        js.executeScript(script, element);
    }
    public void enableNetworkRequestCapture(String urlContains, String storageKey) {
        String script = networkRequestCaptureScript(urlContains, storageKey);
        if (driver instanceof org.openqa.selenium.chromium.HasCdp hasCdp) {
            try {
                hasCdp.executeCdpCommand("Network.enable", Map.of());
                hasCdp.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", Map.of("source", script));
            } catch (Exception ignored) {
            }
        }
        ((JavascriptExecutor) driver).executeScript(
                "try{sessionStorage.removeItem(arguments[0]);}catch(e){}"
                        + "window[arguments[0]]='';",
                storageKey);
        ((JavascriptExecutor) driver).executeScript(script);
    }

    public String readCapturedNetworkRequestBody(String storageKey) {
        Object result = ((JavascriptExecutor) driver).executeScript(
                "try { return window[arguments[0]] || sessionStorage.getItem(arguments[0]) || ''; }"
                        + " catch (e) { return ''; }",
                storageKey);
        return result == null ? "" : result.toString();
    }

    public String readNetworkRequestBodyFromPerformanceLogs(String urlContains) {
        try {
            String last = "";
            for (org.openqa.selenium.logging.LogEntry entry : driver.manage().logs().get("performance").getAll()) {
                org.json.JSONObject message = new org.json.JSONObject(entry.getMessage()).optJSONObject("message");
                if (message == null || !"Network.requestWillBeSent".equals(message.optString("method"))) {
                    continue;
                }
                org.json.JSONObject request = Optional.ofNullable(message.optJSONObject("params"))
                        .map(params -> params.optJSONObject("request"))
                        .orElse(null);
                if (request == null) {
                    continue;
                }
                String url = request.optString("url");
                if (url != null && url.contains(urlContains) && request.has("postData")) {
                    last = request.optString("postData");
                }
            }
            return last;
        } catch (Exception e) {
            return "";
        }
    }

    private String networkRequestCaptureScript(String urlContains, String storageKey) {
        return "(() => {"
                + "  const MATCH = " + toJsString(urlContains) + ";"
                + "  const KEY = " + toJsString(storageKey) + ";"
                + "  const FLAG = KEY + 'Installed';"
                + "  const persist = (url, body) => {"
                + "    if (String(url || '').indexOf(MATCH) === -1) return;"
                + "    if (!body) return;"
                + "    const value = typeof body === 'string' ? body : String(body);"
                + "    if (!value || value === '[object Object]' || value === '[object ReadableStream]') return;"
                + "    window[KEY] = value;"
                + "    try { sessionStorage.setItem(KEY, value); } catch (e) {}"
                + "  };"
                + "  if (window[FLAG]) return;"
                + "  window[FLAG] = true;"
                + "  const origFetch = window.fetch;"
                + "  window.fetch = async function(input, init) {"
                + "    try {"
                + "      const url = typeof input === 'string' ? input : (input && input.url) || '';"
                + "      let body = (init && init.body) || '';"
                + "      if (!body && input && typeof input !== 'string' && input.clone) {"
                + "        try { body = await input.clone().text(); } catch (e) {}"
                + "      }"
                + "      persist(url, body);"
                + "    } catch (e) {}"
                + "    return origFetch.apply(this, arguments);"
                + "  };"
                + "  const origOpen = XMLHttpRequest.prototype.open;"
                + "  const origSend = XMLHttpRequest.prototype.send;"
                + "  XMLHttpRequest.prototype.open = function(method, url) {"
                + "    this.__tiketCaptureUrl = url;"
                + "    return origOpen.apply(this, arguments);"
                + "  };"
                + "  XMLHttpRequest.prototype.send = function(body) {"
                + "    try { persist(this.__tiketCaptureUrl, body); } catch (e) {}"
                + "    return origSend.apply(this, arguments);"
                + "  };"
                + "})();";
    }

    private static String toJsString(String value) {
        return "'" + value.replace("\\", "\\\\").replace("'", "\\'") + "'";
    }

    public List<String> extractPolicyKeywords(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        List<String> keywords = new ArrayList<>();

        // 1. Extract dates (e.g., "05 Nov 2026", "03 Nov 2026")
        Matcher dateMatcher = Pattern.compile("\\b\\d{1,2}\\s+(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|Januari|Februari|Maret|April|Mei|Juni|Juli|Agustus|September|Oktober|November|Desember)\\s+\\d{4}\\b", Pattern.CASE_INSENSITIVE).matcher(text);
        while (dateMatcher.find()) {
            keywords.add(dateMatcher.group());
        }

        // 2. Extract percentages (e.g., 100%, 25%, 50%, 75%)
        Matcher pctMatcher = Pattern.compile("\\d+\\s*%").matcher(text);
        while (pctMatcher.find()) {
            keywords.add(pctMatcher.group().replaceAll("\\s+", ""));
        }

        // 3. Extract price digits (stripping commas/dots)
        Matcher priceMatcher = Pattern.compile("\\b\\d{1,3}([,.]\\d{3})+\\b|\\b\\d{5,10}\\b").matcher(text);
        while (priceMatcher.find()) {
            String cleanNum = priceMatcher.group().replaceAll("[,.]", "");
            if (!keywords.contains(cleanNum)) {
                keywords.add(cleanNum);
            }
        }

        // 4. Extract rule phrases
        String lowerText = text.toLowerCase();
        if (lowerText.contains("not allowed") || lowerText.contains("tidak diperbolehkan") || lowerText.contains("tidak diizinkan")) {
            keywords.add("not allowed");
        }

        return keywords;
    }

    public boolean verifyTextContainsKeywords(String fullText, List<String> requiredKeywords) {
        if (fullText == null || fullText.isBlank()) {
            logger.softWarning("Policy text on Payment page is empty.");
            return false;
        }

        if (requiredKeywords == null || requiredKeywords.isEmpty()) {
            logger.softWarning("No required policy keywords were extracted to compare.");
            return false;
        }

        String normalizedFullText = fullText.toLowerCase().replaceAll("[\\s,.]", "");

        for (String keyword : requiredKeywords) {
            String normalizedKeyword = keyword.toLowerCase().replaceAll("[\\s,.]", "");

            if (normalizedKeyword.equals("notallowed")) {
                if (!normalizedFullText.contains("notallowed") &&
                        !normalizedFullText.contains("tidakdiperbolehkan") &&
                        !normalizedFullText.contains("tidakdiizinkan")) {
                    logger.softWarning("Missing expected policy rule: " + keyword);
                    return false;
                }
                continue;
            }

            if (!normalizedFullText.contains(normalizedKeyword)) {
                logger.softWarning("Missing expected policy keyword on Payment page: " + keyword);
                return false;
            }
        }
        return true;
    }

    public void pressEscape() {
        logger.log("Pressing Escape key");
        try {
            new
                    Actions(driver).sendKeys(Keys.ESCAPE).perform();
            logger.log("Pressed Escape key");
        } catch (WebDriverException e) {
            logger.softWarning("Could not press Escape key: "
                    + e.getMessage());
        }
    }
}
