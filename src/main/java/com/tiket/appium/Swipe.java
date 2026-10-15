package com.tiket.appium;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.*;

public enum Swipe {

    UP, DOWN, RIGHT, LEFT,LEFT_LONG, UP_LONG;

    final int duration;
    final float height_modifier_1, height_modifier_2, height_modifier_3, height_modifier_4, width_modifier_1, width_modifier_2,width_modifier_3,width_modifier_4;

    Swipe() {
        this.duration = 2;
        this.height_modifier_1 = 0.70f;
        this.height_modifier_2 = 0.30f;
        this.height_modifier_3 = 0.95f;
        this.height_modifier_4 = 0.05f;
        this.width_modifier_1 = 0.70f;
        this.width_modifier_2 = 0.30f;
        this.width_modifier_3 = 0.95f;
        this.width_modifier_4 = 0.05f;
    }

    public <T extends WebDriver, E extends WebElement> void perform(T driver, E element) {
        // Use the center of the parent/scrollable — top-left (getLocation) often misses the
        // hittable scroll area for LEFT/RIGHT swipes on nested carousels.
        int start_x = element.getLocation().getX() + element.getSize().getWidth() / 2;
        int start_y = element.getLocation().getY() + element.getSize().getHeight() / 2;
        performSwitch(driver, start_x, start_y);
    }

    public <T extends WebDriver> void perform(T driver) {
        Dimension windowSize = getWindowSizeWithTimeout(driver);
        int width = windowSize.getWidth();
        int height = windowSize.getHeight();
        int start_x = width / 2;
        int start_y = height / 2;
        performSwitch(driver, start_x, start_y);
    }

    /**
     * Gets window size with timeout protection to prevent hanging on unresponsive drivers.
     */
    private <T extends WebDriver> Dimension getWindowSizeWithTimeout(T driver) {
        ExecutorService executor = null;
        try {
            // Use a reasonable timeout (10 seconds) for getSize() operation
            executor = Executors.newSingleThreadExecutor();
            Future<Dimension> future = executor.submit(() ->
                driver.manage().window().getSize()
            );
            Dimension result = future.get(10, TimeUnit.SECONDS);
            return result;
        } catch (TimeoutException e) {
            throw new WebDriverException("Timeout getting window size after 10 seconds. Driver may be unresponsive.", e);
        } catch (Exception e) {
            // If executor fails, fallback to direct call
            try {
                return driver.manage().window().getSize();
            } catch (Exception ex) {
                throw new WebDriverException("Failed to get window size: " + ex.getMessage(), ex);
            }
        } finally {
            // Ensure executor is properly shut down
            if (executor != null) {
                executor.shutdownNow();
                try {
                    // Wait a short time for shutdown, but don't block indefinitely
                    if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                        executor.shutdownNow();
                    }
                } catch (InterruptedException ie) {
                    executor.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private <T extends WebDriver> void performSwitch(T driver, int start_x, int start_y) {
        Dimension windowSize = getWindowSizeWithTimeout(driver);
        int width = windowSize.getWidth();
        int height = windowSize.getHeight();
        int x1 = (int) (width * width_modifier_1);
        int x2 = (int) (width * width_modifier_2);
        int y1 = (int) (height * height_modifier_1);
        int y2 = (int) (height * height_modifier_2);
        int far_x1 = (int) (width * width_modifier_3);
        int far_x2 = (int) (width * width_modifier_4);
        int far_y1 = (int) (height * height_modifier_3);
        int far_y2 = (int) (height * height_modifier_4);
        PointerInput finger1 = new PointerInput (PointerInput.Kind. TOUCH,"finger1");
        Sequence sequence= new Sequence(finger1,1);
        switch (this) {
            case UP -> {
                sequence.addAction (finger1.createPointerMove (Duration.ZERO, PointerInput.Origin.viewport(), start_x, y1))
                        .addAction (finger1.createPointerDown(PointerInput.MouseButton. LEFT.asArg()))
                        .addAction (new Pause(finger1, Duration.ofMillis(200)))
                        .addAction(finger1.createPointerMove (Duration.ofSeconds(duration), PointerInput. Origin.viewport(), start_x, y2))
                        .addAction (finger1.createPointerUp (PointerInput.MouseButton.LEFT.asArg()));
                ((AppiumDriver)driver).perform(Collections.singletonList(sequence));
            }

            case DOWN -> {
                sequence.addAction (finger1.createPointerMove (Duration.ZERO, PointerInput.Origin.viewport(), start_x, y2))
                        .addAction (finger1.createPointerDown(PointerInput.MouseButton. LEFT.asArg()))
                        .addAction (new Pause(finger1, Duration.ofMillis(200)))
                        .addAction(finger1.createPointerMove (Duration.ofSeconds(duration), PointerInput. Origin.viewport(), start_x, y1))
                        .addAction (finger1.createPointerUp (PointerInput.MouseButton.LEFT.asArg()));
                ((AppiumDriver)driver).perform(Collections.singletonList(sequence));
            }

            case LEFT -> {
                sequence.addAction (finger1.createPointerMove (Duration.ZERO, PointerInput.Origin.viewport(), x1, start_y))
                        .addAction (finger1.createPointerDown(PointerInput.MouseButton. LEFT.asArg()))
                        .addAction (new Pause(finger1, Duration.ofMillis(200)))
                        .addAction(finger1.createPointerMove (Duration.ofSeconds(duration), PointerInput. Origin.viewport(), x2, start_y))
                        .addAction (finger1.createPointerUp (PointerInput.MouseButton.LEFT.asArg()));
                ((AppiumDriver)driver).perform(Collections.singletonList(sequence));
            }

            case RIGHT -> {
                sequence.addAction (finger1.createPointerMove (Duration.ZERO, PointerInput.Origin.viewport(), x2, start_y))
                        .addAction (finger1.createPointerDown(PointerInput.MouseButton. LEFT.asArg()))
                        .addAction (new Pause(finger1, Duration.ofMillis(200)))
                        .addAction(finger1.createPointerMove (Duration.ofSeconds(duration), PointerInput. Origin.viewport(), x1, start_y))
                        .addAction (finger1.createPointerUp (PointerInput.MouseButton.LEFT.asArg()));
                ((AppiumDriver)driver).perform(Collections.singletonList(sequence));
            }

            case LEFT_LONG -> {
                sequence.addAction (finger1.createPointerMove (Duration.ZERO, PointerInput.Origin.viewport(), far_x1, start_y))
                        .addAction (finger1.createPointerDown(PointerInput.MouseButton. LEFT.asArg()))
                        .addAction (new Pause(finger1, Duration.ofMillis(200)))
                        .addAction(finger1.createPointerMove (Duration.ofSeconds(duration), PointerInput. Origin.viewport(), far_x2, start_y))
                        .addAction (finger1.createPointerUp (PointerInput.MouseButton.LEFT.asArg()));
                ((AppiumDriver)driver).perform(Collections.singletonList(sequence));
            }

            case UP_LONG -> {
                sequence.addAction (finger1.createPointerMove (Duration.ZERO, PointerInput.Origin.viewport(), start_x, far_y1))
                        .addAction (finger1.createPointerDown(PointerInput.MouseButton. LEFT.asArg()))
                        .addAction (new Pause(finger1, Duration.ofMillis(200)))
                        .addAction(finger1.createPointerMove (Duration.ofSeconds(duration), PointerInput. Origin.viewport(), start_x, far_y2))
                        .addAction (finger1.createPointerUp (PointerInput.MouseButton.LEFT.asArg()));
                ((AppiumDriver)driver).perform(Collections.singletonList(sequence));
            }
        }
    }
}