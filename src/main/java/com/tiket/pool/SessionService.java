package com.tiket.pool;

import com.tiket.appium.DriverTools;
import com.tiket.core.CoreFunctions;
import com.tiket.logging.Log4JLogger;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.service.BundleID;
import com.tiket.service.PackageName;
import com.tiket.testbase.Configs;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.ios.IOSDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.Objects;

public record SessionService<T extends WebDriver>(Session<T> session) implements Validate {

    private static final Logger logger = LogManager.getLogger(SessionService.class);

    public SessionService {
        Objects.requireNonNull(session);
    }

    public T getConnection() {
        return session.driver();
    }

    @Override
    public boolean isValid() {
        WebDriver driver = session.driver();
        if (driver == null) {
            return false;
        }
        DriverTools<WebDriver> driverTools = new DriverTools<>(driver, new Log4JLogger());
        return !insufficientTimeLeft()
                && isBrowserStackSOTimeoutDetected(driver)
                && isSessionTimeoutDetected(driverTools);
    }

    public boolean insufficientTimeLeft() {
        long timeLeft = ((1000L * 60 * session.timeLimitMinutes()) - (System.currentTimeMillis() - session.createdAt()));
        return timeLeft < 0;
    }

    public boolean isBrowserStackSOTimeoutDetected(WebDriver driver) {
        if(Configs.PLATFORM == Platform.IOS) {
            IOSDriver iosDriver = (IOSDriver) driver;
            try {
                ApplicationState applicationState = iosDriver.queryAppState(BundleID.getID());
                return appInCorrectState(applicationState);
            } catch (Exception e) {
                logger.error(e);
                logger.error("Exception occurred, session will be renewed");
                return false;
            }
        } else if(Configs.PLATFORM == Platform.ANDROID) {
            AndroidDriver androidDriver = (AndroidDriver) driver;
            try {
                ApplicationState applicationState = androidDriver.queryAppState(PackageName.get());
                return appInCorrectState(applicationState);
            } catch (Exception e) {
                logger.error(e);
                logger.error("Exception occurred, session will be renewed");
                return false;
            }
        }
        return true;
    }

    public boolean isSessionTimeoutDetected(DriverTools<WebDriver> driverTools) {
      boolean isProdAndroid = Configs.ENVIRONMENT == Environment.PROD && Configs.PLATFORM == Platform.ANDROID;
      if(Configs.ENVIRONMENT == Environment.GK || Configs.ENVIRONMENT == Environment.PREPROD || isProdAndroid) {
            String source;
            try {
                source = driverTools.getPageSource();
            } catch (Exception e) {
                logger.error(e);
                logger.error("Exception occurred, session will be renewed");
                return false;
            }
            String[] tokens = {
                    "(403)",
                    "Your session is expired",
                    "Sesimu sudah habis",
                    "You have an internet connection issue",
                    "Ada masalah dengan koneksi internetmu",
                    "You don't have access to see this",
                    "Kamu nggak punya akses untuk melihat ini"
            };
            boolean result = CoreFunctions.containsAny(source, tokens);
            if(result) {
                logger.error("Issue in session detected, session will be renewed");
                return false;
            }
        }
        return true;
    }

    private boolean appInCorrectState(ApplicationState applicationState) {
        logger.info("Current application state: " + applicationState);
        if(applicationState == ApplicationState.NOT_INSTALLED || applicationState == ApplicationState.NOT_RUNNING || applicationState == ApplicationState.RUNNING_IN_BACKGROUND_SUSPENDED) {
            logger.error("SO timeout detected, session will be renewed");
            return false;
        }
        return true;
    }
}
