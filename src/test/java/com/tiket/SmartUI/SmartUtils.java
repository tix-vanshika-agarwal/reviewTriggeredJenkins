package com.tiket.SmartUI;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

public class SmartUtils {
    private SmartUtils() {
        // Prevent instantiation
    }

    public static void takeSmartScreenshot(WebDriver driver, String name) {
        try {
            Map<String, Object> config = new HashMap<>();
            config.put("screenshotName", name);
            ((JavascriptExecutor) driver).executeScript("smartui.takeScreenshot", config);
        } catch (Exception e) {
            System.out.println("SmartUI screenshot skipped: " + e.getMessage());
        }
    }


    public static void takeSmartScreenshot(WebDriver driver, String name, boolean fullPage) {
        ((JavascriptExecutor) driver).executeScript(
                "smartui.takeScreenshot",
                Map.of("screenshotName", name, "fullPage", fullPage)
        );
    }
}
