package com.tiket.page.common.webview;

import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.android.AndroidDriver;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public abstract class WebViewPage<T extends WebDriver> extends BasePage<T> {

    public WebViewPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> WebViewPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new WebViewPageAndroid<>(session); }
            case IOS -> { return new WebViewPageIOS<>(session); }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public void switchToWebViewUrl(String targetUrl) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl(targetUrl);
        logger.log("Current URL: " + driverTools.getCurrentURL());
    }

    public void switchToNative() {
        driverTools.switchToNative();
    }

    @SneakyThrows
    public boolean waitForWebview(int seconds) {
        if(Configs.PLATFORM == Platform.ANDROID) {
            boolean isWebview;
            int count = 0;
            do {
                count++;
                Thread.sleep(1000);
                String currentPackage = ((AndroidDriver) driver).getCurrentPackage();
                logger.log("Current package: " + currentPackage);
                Set<String> contextHandles = ((AndroidDriver) driver).getContextHandles();
                logger.log("Context handle size: " + contextHandles.size());
                assert currentPackage != null;
                isWebview = !currentPackage.contains("tiket");
                logger.log("Is on Webview? " + isWebview);
            } while (!isWebview && count < seconds);
            return isWebview;
        }
        throw new RuntimeException(Configs.PLATFORM + " not supported for this method.");   // For iOS to be implemented later
    }

    @SneakyThrows
    public boolean waitForNative(int seconds) {
        if(Configs.PLATFORM == Platform.ANDROID) {
            boolean isNative;
            int count = 0;
            do {
                count++;
                Thread.sleep(1000);
                String currentPackage = ((AndroidDriver) driver).getCurrentPackage();
                logger.log("Current package: " + currentPackage);
                Set<String> contextHandles = ((AndroidDriver) driver).getContextHandles();
                logger.log("Context handle size: " + contextHandles.size());
                assert currentPackage != null;
                isNative = currentPackage.contains("tiket");
                logger.log("Is on Native? " + isNative);
            } while (!isNative && count < seconds);
            return isNative;
        }
        throw new RuntimeException(Configs.PLATFORM + " not supported for this method.");   // For iOS to be implemented later
    }
}
