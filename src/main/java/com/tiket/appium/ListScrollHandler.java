package com.tiket.appium;

import com.tiket.model.Locator;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ListScrollHandler<T extends WebDriver> {

    private final T driver;
    private final DriverTools<T> driverTools;
    private final Locator firstElementLocator;
    private Locator parentScrollableView;

    public ListScrollHandler(T driver, Locator firstElementLocator, Locator parentScrollableView) {
        this.driver = driver;
        this.driverTools = new DriverTools<>(driver);
        this.firstElementLocator = firstElementLocator;
        this.parentScrollableView = parentScrollableView;
    }

    public ListScrollHandler(T driver, Locator firstElementLocator) {
        this.driver = driver;
        this.driverTools = new DriverTools<>(driver);
        this.firstElementLocator = firstElementLocator;
    }

    public Optional<WebElement> gotoFirstElement(Swipe swipe, int maxTries) {
        int tries = 0;
        
        // Optimized: Use while loop instead of do-while to avoid unnecessary first swipe
        while (tries < maxTries) {
            tries++;
            
            // Optimized: Find element once and reuse the result
            Optional<WebElement> webElement = driverTools.element(firstElementLocator);
            
            // Optimized: Check if element is present AND displayed using the cached element
            if (webElement.isPresent()) {
                try {
                    // Use the cached element to check display status (no redundant find)
                    if (webElement.get().isDisplayed()) {
                        // Element found and displayed - return it immediately (no redundant waitForElementToPresence)
                        return webElement;
                    }
                } catch (Exception e) {
                    // Element might be stale, continue to swipe
                }
            }
            
            // Only swipe if element not found or not displayed
            performSwipe(swipe);
        }
        
        // Final check before giving up
        Optional<WebElement> finalElement = driverTools.element(firstElementLocator);
        if (finalElement.isPresent()) {
            try {
                if (finalElement.get().isDisplayed()) {
                    return finalElement;
                }
            } catch (Exception e) {
                // Element stale or not displayed
            }
        }

        return Optional.empty();
    }

    public Optional<WebElement> gotoFirstPresentedElement(Swipe swipe, int maxTries) {
        int tries = 0;
        
        // Optimized: Use while loop and cache the result
        while (tries < maxTries) {
            tries++;
            
            // Optimized: Cache the result to avoid calling waitForElementToPresence twice
            Optional<WebElement> webElement = driverTools.waitForElementToPresence(firstElementLocator);
            
            if (webElement.isPresent()) {
                // Return immediately - no redundant call
                return webElement;
            }
            
            performSwipe(swipe);
        }
        
        return Optional.empty();
    }

    /**
     * Swipes (optionally on {@code parentScrollableView}) until the target element's
     * full bounds are on-screen. Uses the same swipe mechanics as {@link #gotoFirstElement}.
     */
    public Optional<WebElement> gotoFullyVisibleElement(Swipe swipe, int maxTries) {
        int tries = 0;

        while (tries < maxTries) {
            tries++;
            Optional<WebElement> webElement = driverTools.element(firstElementLocator);
            if (webElement.isPresent()) {
                try {
                    WebElement el = webElement.get();
                    if (isTargetFullyVisible(el)) {
                        return webElement;
                    }
                } catch (Exception e) {
                    // stale / missing — keep swiping
                }
            }
            performSwipe(swipe);
        }

        Optional<WebElement> finalElement = driverTools.element(firstElementLocator);
        if (finalElement.isPresent() && isTargetFullyVisible(finalElement.get())) {
            return finalElement;
        }
        return finalElement.isPresent() ? finalElement : Optional.empty();
    }

    /**
     * When a parent scrollable is set, require the target to sit fully inside the parent
     * bounds (nested carousel clip). Otherwise require full device-window visibility.
     */
    private boolean isTargetFullyVisible(WebElement target) {
        if (parentScrollableView != null) {
            Optional<WebElement> parent = driverTools.element(parentScrollableView);
            if (parent.isPresent()) {
                return driverTools.isElementFullyWithin(target, parent.get())
                        && driverTools.isElementFullyOnScreen(target);
            }
        }
        return driverTools.isElementFullyOnScreen(target);
    }

    private void performSwipe(Swipe swipe) {
        if (parentScrollableView != null) {
            Optional<WebElement> e = driverTools.element(parentScrollableView);
            if (e.isPresent()) {
                swipe.perform(driver, e.get());
                return;
            }
            // Parent missing → still swipe on screen so the gesture is never a silent no-op
        }
        swipe.perform(driver);
    }

    /**
     * UiAutomator2-native scroll; W3C pointer moves often miss nested scrollers on real devices.
     */
    private void performAndroidScrollGesture(WebElement element, Swipe swipe) {
        String direction = switch (swipe) {
            case UP, UP_LONG -> "up";
            case DOWN -> "down";
            case LEFT, LEFT_LONG -> "left";
            case RIGHT -> "right";
        };
        double percent = (swipe == Swipe.UP_LONG || swipe == Swipe.LEFT_LONG) ? 0.95 : 0.75;
        Map<String, Object> args = new HashMap<>();
        args.put("elementId", ((RemoteWebElement) element).getId());
        args.put("direction", direction);
        args.put("percent", percent);
        ((JavascriptExecutor) driver).executeScript("mobile: scrollGesture", args);
    }
}
