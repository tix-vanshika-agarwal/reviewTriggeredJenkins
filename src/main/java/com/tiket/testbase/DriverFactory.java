package com.tiket.testbase;

import com.tiket.capabilities.CapabilitiesHandler;
import com.tiket.common.RetryUtils;
import com.tiket.io.network.BrowserStackLocal;
import com.tiket.mitmproxy.MitmProxy;
import com.tiket.model.DeviceFarm;
import com.tiket.model.Platform;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    public static RemoteWebDriver getDriver(Platform platform) {
        Connection con = getConnection();
        return switch (platform) {
            case ANDROID, AMWEB -> getAndroidDriver(con);
            case IOS, IMWEB -> getIosDriver(con);
            case DWEB -> getChromeDriver();
        };
    }

    private static AndroidDriver getAndroidDriver(Connection con) {
        if (Configs.DEVICE_FARM == DeviceFarm.LAMBDA_TEST) {
            AndroidDriver driver = new AndroidDriver(con.url, con.capabilities);
            HashMap<String, Object> settings = new HashMap<>();
            settings.put("enforceXPath1", true);
            driver.setSettings(settings);
            return driver;
        } else {
            return new AndroidDriver(con.url, con.capabilities);
        }
    }

    private static IOSDriver getIosDriver(Connection con) {
        if (Configs.DEVICE_FARM == DeviceFarm.LAMBDA_TEST) {
            IOSDriver driver = new IOSDriver(con.url, con.capabilities);
            HashMap<String, Object> settings = new HashMap<>();
            settings.put("enforceXPath1", true);
            driver.setSettings(settings);
            driver.setSetting("acceptAlertButtonSelector",
                    "**/XCUIElementTypeButton[`label == 'Allow While Using App'`]");
            return driver;
        } else {
            return new IOSDriver(con.url, con.capabilities);
        }
    }

    public static RemoteWebDriver getDriver() {
        return getDriver(Configs.PLATFORM);
    }

    private static RemoteWebDriver getChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions option = new ChromeOptions();
        option.addArguments("--no-sandbox");
        option.addArguments("--disable-dev-shm-usage"); // should be enabled for Jenkins
        option.addArguments("--remote-allow-origins=*");
        option.addArguments("--start-maximized");
        // option.addArguments("--incognito");
        option.setCapability(ChromeOptions.CAPABILITY, option);

        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 1);

        // set ExperimentalOption - prefs
        option.setExperimentalOption("prefs", prefs);
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        option.setCapability("goog:loggingPrefs", logPrefs);

        if (Configs.PROXY_USE) {
            String proxyAddress = "localhost:" + MitmProxy.PROXY_PORT;
            Proxy seleniumProxy = new Proxy();
            seleniumProxy.setHttpProxy(proxyAddress);
            seleniumProxy.setSslProxy(proxyAddress);
            option.setProxy(seleniumProxy);
            option.setAcceptInsecureCerts(true);
            option.setPageLoadStrategy(PageLoadStrategy.EAGER); // or NORMAL -> EAGER/NONE
            option.addArguments("--ignore-certificate-errors");
            logger.info("Chrome proxy configured for mitmproxy at {}", proxyAddress);
        } else {
//            option.addArguments("--headless=new"); // should be enabled for Jenkins, except for analytics tests
        }

        return RetryUtils.retryWithExponentialBackoff(
                5,
                100L,
                () -> new ChromeDriver(option),
                RetryUtils::isChromeDriverPortConflict,
                "ChromeDriver creation (free port)"
        );
    }

    /**
     * Creates a Selenium RemoteWebDriver using the given connection URL and capabilities.
     */
    private static RemoteWebDriver getRemoteWebDriver(Connection con) {
        return new RemoteWebDriver(con.url, con.capabilities);
    }

    private static Connection getConnection() {
        CapabilitiesHandler handler = new CapabilitiesHandler();
        JSONArray configs = handler.getConfigs();
        JSONObject config = handler.getConfig(configs);
        DesiredCapabilities capabilities = handler.getCapabilities(config);
        String accessKey = handler.getAccessKey(config);
        URL url = handler.getUrl(config);
        if (Configs.DEVICE_FARM == DeviceFarm.BROWSER_STACK) {
            BrowserStackLocal.start(capabilities, accessKey);
        }
        logger.debug("Connection URL: " + url);
        logger.debug("Desired Capabilities: " + capabilities);

        return new Connection(url, capabilities);
    }

    private record Connection(URL url, DesiredCapabilities capabilities) {
    }
}