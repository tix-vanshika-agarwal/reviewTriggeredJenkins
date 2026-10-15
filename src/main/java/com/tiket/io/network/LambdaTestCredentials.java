package com.tiket.io.network;

import com.tiket.capabilities.CapabilitiesHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

public class LambdaTestCredentials {

    private static final Logger logger = LogManager.getLogger(LambdaTestCredentials.class);

    /**
     * Extracts LambdaTest username from capabilities
     * @return username from lt:options or null if not found
     */
    @SuppressWarnings("unchecked")
    public static String getUsername() {
        try {
            CapabilitiesHandler handler = new CapabilitiesHandler();
            JSONArray configs = handler.getConfigs();
            JSONObject config = handler.getConfig(configs);
            DesiredCapabilities capabilities = handler.getCapabilities(config);
            
            Map<String, String> ltOptions = (Map<String, String>) capabilities.getCapability("lt:options");
            if (ltOptions != null && ltOptions.containsKey("user")) {
                String username = ltOptions.get("user");
                logger.info("LambdaTest username from capabilities: " + username);
                return username;
            }
            logger.warn("LambdaTest user not found in lt:options");
            return null;
        } catch (Exception e) {
            logger.error("Error extracting LambdaTest username from capabilities: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Extracts LambdaTest access key from capabilities
     * @return accessKey from lt:options or null if not found
     */
    public static String getAccessKey() {
        try {
            CapabilitiesHandler handler = new CapabilitiesHandler();
            JSONArray configs = handler.getConfigs();
            JSONObject config = handler.getConfig(configs);
            
            // Get accessKey using existing method
            String accessKey = handler.getAccessKey(config);
            
            if (accessKey != null && !accessKey.isEmpty()) {
                logger.info("LambdaTest access key found from capabilities");
                return accessKey;
            }
            
            logger.warn("LambdaTest accessKey not found in lt:options");
            return null;
        } catch (Exception e) {
            logger.error("Error extracting LambdaTest accessKey from capabilities: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Extracts tunnel name from capabilities
     * @return tunnel name from lt:options or null if not found
     */
    @SuppressWarnings("unchecked")
    public static String getTunnelName() {
        try {
            CapabilitiesHandler handler = new CapabilitiesHandler();
            JSONArray configs = handler.getConfigs();
            JSONObject config = handler.getConfig(configs);
            DesiredCapabilities capabilities = handler.getCapabilities(config);
            
            Map<String, String> ltOptions = (Map<String, String>) capabilities.getCapability("lt:options");
            if (ltOptions != null && ltOptions.containsKey("tunnelName")) {
                String tunnelName = ltOptions.get("tunnelName");
                logger.info("LambdaTest tunnel name from capabilities: " + tunnelName);
                return tunnelName;
            }
            logger.warn("LambdaTest tunnelName not found in lt:options");
            return null;
        } catch (Exception e) {
            logger.error("Error extracting LambdaTest tunnel name from capabilities: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Checks if tunnel is enabled in capabilities
     * @return true if tunnel is enabled ("true" or true), false otherwise
     */
    @SuppressWarnings("unchecked")
    public static boolean isTunnelEnabled() {
        try {
            CapabilitiesHandler handler = new CapabilitiesHandler();
            JSONArray configs = handler.getConfigs();
            JSONObject config = handler.getConfig(configs);
            DesiredCapabilities capabilities = handler.getCapabilities(config);
            
            Map<String, Object> ltOptions = (Map<String, Object>) capabilities.getCapability("lt:options");
            if (ltOptions != null && ltOptions.containsKey("tunnel")) {
                Object tunnelValue = ltOptions.get("tunnel");
                
                // Handle both string "true" and boolean true
                if (tunnelValue instanceof String) {
                    return "true".equalsIgnoreCase((String) tunnelValue);
                } else if (tunnelValue instanceof Boolean) {
                    return (Boolean) tunnelValue;
                }
            }
            
            logger.debug("Tunnel flag not found in lt:options or set to false");
            return false;
        } catch (Exception e) {
            logger.error("Error checking if tunnel is enabled: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Gets credentials as a record for easy usage
     */
    public record Credentials(String username, String accessKey, String tunnelName) {}

    public static Credentials getCredentials() {
        return new Credentials(getUsername(), getAccessKey(), getTunnelName());
    }
}

