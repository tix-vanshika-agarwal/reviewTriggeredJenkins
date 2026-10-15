package com.tiket.appium;

import com.tiket.model.Locator;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TimeoutException;
import java.util.List;
import java.util.stream.Collectors;

public final class PopupObserver {

    private static final Logger logger = Logger.getLogger(PopupObserver.class.getName());

    private PopupObserver() {
    }

    public static void inject(WebDriver driver, List<Locator> popupLocatorsXpathOnly) {

        List<String> xpaths = popupLocatorsXpathOnly.stream().map(PopupObserver::extractXPath).collect(Collectors.toList());

        logger.info("Injecting PopupObserver with the following XPaths: " + xpaths);

        // Clicks are deferred via setTimeout(..., 0) so executeScript returns immediately.
        // A synchronous element.click() that triggers navigation leaves ChromeDriver waiting
        // for the atom result until script timeout (see DriverTools.clickWithJavascriptExecutor).
        String script = """
                    (function(xpaths) {

                     window.__popupSelectors = xpaths;

                     function closePopups() {

                         for (const xpath of (window.__popupSelectors || [])) {

                             const element = document.evaluate(
                                 xpath,
                                 document,
                                 null,
                                 XPathResult.FIRST_ORDERED_NODE_TYPE,
                                 null
                             ).singleNodeValue;

                             if (element) {
                                 try {
                                     setTimeout(function() {
                                         try { element.click(); } catch (e) {}
                                     }, 0);
                                 } catch (e) {}
                             }
                         }
                     }

                     function scheduleClosePopups() {
                         if (window.__popupCloseScheduled) {
                             return;
                         }
                         window.__popupCloseScheduled = true;
                         setTimeout(function() {
                             window.__popupCloseScheduled = false;
                             closePopups();
                         }, 50);
                     }

                     function installObserver() {

                         if (!document.body) {
                             requestAnimationFrame(installObserver);
                             return;
                         }

                         if (window.__popupObserver) {
                             try {
                                 window.__popupObserver.disconnect();
                             } catch (e) {}
                         }

                         closePopups();

                         const observer = new MutationObserver(scheduleClosePopups);

                         observer.observe(document.body, {
                             childList: true,
                             subtree: true
                         });

                         window.__popupObserver = observer;
                     }

                     installObserver();
                     return true;

                 })(arguments[0]);
                """;

        try {
            ((JavascriptExecutor) driver).executeScript(script, xpaths);
            logger.info("PopupObserver injected successfully.");
        } catch (TimeoutException te) {
            // Page likely navigated away before the atom could return; treat as dispatched.
            logger.warn("PopupObserver inject timed out (likely navigation). Treating as dispatched: " + te.getMessage());
        }
    }

    private static String extractXPath(Locator locator) {

        String locatorString = locator.by().toString();

        if (!locatorString.startsWith("By.xpath: ")) {
            throw new IllegalArgumentException("PopupObserver currently supports only By.xpath locators. Found: " + locatorString);
        }

        return locatorString.substring("By.xpath: ".length());
    }
}
