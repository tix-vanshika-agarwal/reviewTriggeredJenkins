package com.tiket.io.network;

import com.google.gson.Gson;
import com.tiket.api.lambdatest.GETLambdaTestTunnelsApi;
import com.tiket.io.api.ApiTools;
import com.tiket.logging.Log4JLogger;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.lambdatest.LambdaTestTunnelsResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LambdaTestTunnelValidator {

    private static final Logger logger = LogManager.getLogger(LambdaTestTunnelValidator.class);
    private static final Gson gson = new Gson();

    /**
     * Fetches all active tunnels from LambdaTest API
     * @return LambdaTestTunnelsResponse or null if fetch failed
     */
    private static LambdaTestTunnelsResponse fetchActiveTunnels() {
        try {
            // Get credentials from capabilities
            LambdaTestCredentials.Credentials credentials = LambdaTestCredentials.getCredentials();
            
            if (credentials.username() == null || credentials.accessKey() == null) {
                logger.error("LambdaTest credentials not found in capabilities");
                return null;
            }
            
            // Log credentials info (mask sensitive data)
            logger.info("Using access key from capabilities: " + "************" + 
                       credentials.accessKey().substring(Math.max(0, credentials.accessKey().length() - 4)));
            
            // Create API inputs and hit the API
            GETLambdaTestTunnelsApi.Inputs inputs = new GETLambdaTestTunnelsApi.Inputs(
                credentials.username(), 
                credentials.accessKey()
            );
            
            GETLambdaTestTunnelsApi api = new GETLambdaTestTunnelsApi(inputs);
            ApiTools apiTools = new ApiTools(new Log4JLogger());
            
            ApiResponse response = api.hitApi(apiTools);
            
            logger.info("LambdaTest API Response Status: " + response.getStatusCode());
            logger.debug("LambdaTest API Response Body: " + response.getBody());
            
            if (response.getStatusCode() != 200) {
                logger.error("Failed to fetch LambdaTest tunnels. Status code: " + response.getStatusCode());
                return null;
            }
            
            // Parse response using POJO
            LambdaTestTunnelsResponse tunnelsResponse = gson.fromJson(
                response.getBody(), 
                LambdaTestTunnelsResponse.class
            );
            
            if (!"success".equalsIgnoreCase(tunnelsResponse.status)) {
                logger.error("LambdaTest API returned non-success status: " + tunnelsResponse.status);
                return null;
            }
            
            if (tunnelsResponse.data == null || tunnelsResponse.data.isEmpty()) {
                logger.warn("No tunnels found in LambdaTest response");
                return null;
            }
            
            return tunnelsResponse;
            
        } catch (Exception e) {
            logger.error("Error fetching LambdaTest tunnels: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Checks if a specific tunnel is running in the tunnels list
     * @param tunnelName The tunnel name to check
     * @param tunnelsResponse The response containing all tunnels
     * @return true if tunnel is running, false otherwise
     */
    private static boolean checkTunnelStatus(String tunnelName, LambdaTestTunnelsResponse tunnelsResponse) {
        for (LambdaTestTunnelsResponse.TunnelData tunnel : tunnelsResponse.data) {
            logger.debug("Found tunnel: " + tunnel.tunnel_name + " with status: " + tunnel.status_ind);
            
            if (tunnelName.equals(tunnel.tunnel_name)) {
                if ("running".equalsIgnoreCase(tunnel.status_ind)) {
                    logger.info("Required tunnel '" + tunnelName + "' is running");
                    logger.info("Tunnel Details - ID: " + tunnel.tunnel_id + 
                              ", Owner: " + tunnel.email + 
                              ", Started: " + tunnel.start_timestamp);
                    return true;
                } else {
                    logger.error("Required tunnel '" + tunnelName + 
                               "' exists but status is: " + tunnel.status_ind);
                    return false;
                }
            }
        }
        
        // Tunnel not found
        logger.error("Required tunnel '" + tunnelName + "' not found in active tunnels");
        logger.info("Available tunnels:");
        for (LambdaTestTunnelsResponse.TunnelData tunnel : tunnelsResponse.data) {
            logger.info("  - " + tunnel.tunnel_name + " (" + tunnel.status_ind + ")");
        }
        return false;
    }

    /**
     * Validates if the required LambdaTest tunnel is running
     * Gets tunnel name from capabilities automatically (mandatory)
     * @return true if tunnel is running, false otherwise
     * @throws RuntimeException if tunnel name is not found in capabilities
     */
    public static boolean isTunnelRunning() {
        try {
            // Get tunnel name from capabilities (mandatory)
            String requiredTunnelName = LambdaTestCredentials.getTunnelName();
            if (requiredTunnelName == null || requiredTunnelName.isEmpty()) {
                String errorMsg = "Tunnel name not found in capabilities (lt:options -> tunnelName). " +
                                "Please configure tunnelName in your test data sheet capabilities.";
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }
            
            logger.info("Checking if LambdaTest tunnel '" + requiredTunnelName + "' is running...");
            
            // Fetch all active tunnels
            LambdaTestTunnelsResponse tunnelsResponse = fetchActiveTunnels();
            if (tunnelsResponse == null) {
                return false;
            }
            
            // Check if required tunnel is running
            return checkTunnelStatus(requiredTunnelName, tunnelsResponse);
            
        } catch (Exception e) {
            logger.error("Error validating LambdaTest tunnel: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Validates if a specific tunnel is running
     * @param tunnelName The specific tunnel name to validate
     * @return true if tunnel is running, false otherwise
     */
    public static boolean isTunnelRunning(String tunnelName) {
        try {
            logger.info("Checking if specific LambdaTest tunnel '" + tunnelName + "' is running...");
            
            // Fetch all active tunnels
            LambdaTestTunnelsResponse tunnelsResponse = fetchActiveTunnels();
            if (tunnelsResponse == null) {
                return false;
            }
            
            // Check if specific tunnel is running
            return checkTunnelStatus(tunnelName, tunnelsResponse);
            
        } catch (Exception e) {
            logger.error("Error validating LambdaTest tunnel: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Validates tunnel and throws exception if not running (strict mode)
     * Automatically gets tunnel name from capabilities (mandatory)
     * @throws RuntimeException if tunnel name not in capabilities or tunnel is not running
     */
    public static void validateTunnelOrThrow() {
        // This will throw if tunnel name not found in capabilities
        if (!isTunnelRunning()) {
            String tunnelName = LambdaTestCredentials.getTunnelName();
            String errorMsg = "Required LambdaTest tunnel '" + tunnelName + 
                            "' is not running. Please start the tunnel before running tests.";
            logger.fatal(errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }
}

