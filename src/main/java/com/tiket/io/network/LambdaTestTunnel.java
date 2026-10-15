package com.tiket.io.network;

import com.lambdatest.tunnel.Tunnel;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

public class LambdaTestTunnel {

    private static final Logger logger = LogManager.getLogger(LambdaTestTunnel.class);
    private static Tunnel tunnel;

    @SneakyThrows
    public static void start(DesiredCapabilities capabilities, String accessKey) {

        Map<String, String> ltOptions = (Map<String, String>) capabilities.getCapability("lt:options");
        if (ltOptions == null || ltOptions.get("tunnel") == null || !ltOptions.get("tunnel").equalsIgnoreCase("true")) {
            logger.debug("Tunnel not required");
            return;
        }
        String user = ltOptions.get("user");
        String tunnelName = ltOptions.get("tunnelName");
        assert user != null;
        assert tunnelName != null;
        assert accessKey != null;
        HashMap<String, String> tunnelArgs = new HashMap<String, String>();
        tunnelArgs.put("user", user);
        tunnelArgs.put("key", accessKey);
        tunnelArgs.put("tunnelName", tunnelName);
        tunnelArgs.put("mitm", "true");
        tunnel = new Tunnel();
        tunnel.start(tunnelArgs);
        Thread.sleep(30_000);
    }

    @SneakyThrows
    public static void stopTunnel() {
        tunnel.stopTunnel();
    }
}
