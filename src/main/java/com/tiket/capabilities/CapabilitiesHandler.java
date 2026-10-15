package com.tiket.capabilities;

import com.tiket.model.DeviceFarm;
import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import static com.tiket.testbase.Configs.BROWSERSTACK_DEBUG;
import static com.tiket.testbase.Configs.LAMBDA_TEST_DEBUG;

public class CapabilitiesHandler {

    private static final Logger logger = LogManager.getLogger(CapabilitiesHandler.class);
    private static int configIndex = 0;

    public JSONObject getConfig(JSONArray configs) {
        return (JSONObject) configs.get(getAndUpdateConfigIndex(configs.size()));
    }

    private synchronized int getAndUpdateConfigIndex(int size) {
        var i = configIndex;
        configIndex++;
        configIndex = configIndex % size;
        return i;
    }

    public DesiredCapabilities getCapabilities(JSONObject config) {

        DesiredCapabilities capabilities = new DesiredCapabilities();
        JSONArray envs = (JSONArray) config.get("environments");

        Map<String, Object> envCapabilities = (Map) envs.get(0);
        Map<String, Object> commonCapabilities = (Map) config.get("capabilities");

        envCapabilities.forEach(capabilities::setCapability);
        commonCapabilities.forEach(capabilities::setCapability);

        String app = System.getProperty("BROWSERSTACK_APP_ID");
        if (app != null && !app.isBlank()) {
            capabilities.setCapability("app", app);
        }

        logger.debug("capabilities: " + capabilities);

        return capabilities;
    }

    public JSONArray getConfigs() {
        JSONParser parser = new JSONParser();
        try {
            logger.info("capability name: " + getCapabilityName());
            String s = Configs.PROPERTIES_DATA.get(getCapabilityName());
            logger.info("properties data: "+s);
            return (JSONArray) parser.parse(s);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    public String getUserName(JSONObject config) {
        String username = System.getProperty("BROWSERSTACK_USERNAME");
        if (username == null) {
            username = (String) config.get("username");
        }
        return username;
    }

    public String getAccessKey(JSONObject config) {
        String accessKey = System.getProperty("BROWSERSTACK_ACCESS_KEY");
        String cloudOptions = Configs.LAMBDA_TEST_DEBUG;
        if (accessKey == null) {
            Map<String, Object> capabilities = (Map<String, Object>) config.get("capabilities");
            if(Configs.DEVICE_FARM == DeviceFarm.BROWSER_STACK) {
                cloudOptions = Configs.BROWSERSTACK_DEBUG;
            }
            Map<String, String> options = (Map<String, String>) capabilities.get(cloudOptions);
            if(options != null) {
                accessKey = options.get("accessKey");
            }
        }
        return accessKey;
    }

    public URL getUrl(JSONObject config) {
        try {
            return new URL("http://" + config.get("server") + "/wd/hub");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static boolean isRunningOnDeviceFarm(){
        CapabilitiesHandler handler = new CapabilitiesHandler();
        JSONArray configs = handler.getConfigs();
        JSONObject config = handler.getConfig(configs);
        DesiredCapabilities capabilities = handler.getCapabilities(config);
        Set<String> capabilityNames = capabilities.getCapabilityNames();
        boolean runningOnLambdaTest = capabilityNames.contains(LAMBDA_TEST_DEBUG);
        boolean runningOnBrowserStack = capabilityNames.contains(BROWSERSTACK_DEBUG);
        return runningOnLambdaTest || runningOnBrowserStack;
    }

    private String getCapabilityName() {
        if(Configs.CODE_COVERAGE && Configs.PLATFORM == Platform.ANDROID) {
            if(Configs.ENVIRONMENT == Environment.PROD) {
                return "capability.coverage.prod";
            } else {
                return "capability.coverage.gk";
            }
        } else if(Configs.CODE_COVERAGE && Configs.PLATFORM == Platform.IOS) {
            if(Configs.ENVIRONMENT == Environment.PROD) {
                return "capability.ios.coverage.prod";
            } else {
                return "capability.ios.coverage.gk";
            }
        } else if (Configs.PLATFORM == Platform.ANDROID && Configs.ENVIRONMENT == Environment.GK) {
            return "capability.android.gk";
        } else if (Configs.PLATFORM == Platform.ANDROID && Configs.ENVIRONMENT == Environment.PREPROD) {
            return "capability.android.preprod";
        } else if (Configs.PLATFORM == Platform.ANDROID && Configs.ENVIRONMENT == Environment.PROD) {
            return "capability.android.prod";
        } else if (Configs.PLATFORM == Platform.IOS && Configs.ENVIRONMENT == Environment.GK) {
            return "capability.ios.gk";
        } else if (Configs.PLATFORM == Platform.IOS && Configs.ENVIRONMENT == Environment.PREPROD) {
            return "capability.ios.preprod";
        } else if (Configs.PLATFORM == Platform.DWEB) {
            return "capability.dweb";
        } else if (Configs.PLATFORM == Platform.AMWEB) {
            return "capability.amweb";
        } else if (Configs.PLATFORM == Platform.IMWEB) {
            return "capability.imweb";
        } else {
            return "capability.ios.prod";
        }
    }
}