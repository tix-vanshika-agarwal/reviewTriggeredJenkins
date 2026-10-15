package com.tiket.io.network;

import com.browserstack.local.Local;
import com.tiket.model.Platform;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

public class BrowserStackLocal {

    private static final Logger logger = LogManager.getLogger(BrowserStackLocal.class);
    private static final Local bsLocal = new Local();
    private static boolean isBrowserStackLocalRunning = false;
    private static boolean shutdownDone = false;

    @SneakyThrows
    public static void start(DesiredCapabilities capabilities, String accessKey) {
        logger.debug("Starting browserstack local session");



        if(Configs.FORCE_DEVICE_FARM_BINARY_SHUTDOWN && !shutdownDone) {
            assert accessKey != null;
            Map<String, String> bsLocalArgs = Map.of(
                    "key", accessKey,
                    "forcelocal", "true"
            );
            logger.debug("Force stop bs local");
            stop(bsLocalArgs);
            shutdownDone = true;
        }

        if(!(Platform.ANDROID == Configs.PLATFORM || Platform.IOS == Configs.PLATFORM)) {
            logger.debug("Browserstack local not required for dWeb and mWeb");
            return;
        }

        if (isBsLocalUpAndRunning()) {
            logger.debug("Browserstack local session already up and running");
            return;
        }

        Map<String, String> bstack = (Map<String, String>) capabilities.getCapability("bstack:options");
        if (bstack == null || bstack.get("local") == null || !bstack.get("local").equalsIgnoreCase("true")) {
            logger.debug("Browserstack local session not required");
            return;
        }

        if (isBrowserStackLocalRunning) {
            logger.debug("Browserstack session already started");
            return;
        }
        
        assert accessKey != null;
        Map<String, String> bsLocalArgs = Map.of(
                "key", accessKey,
                "forcelocal", "true"
        );
        bsLocal.start(bsLocalArgs);
        for (int i = 0; i < 20; i++) {
            if(!bsLocal.isRunning()) {
                Thread.sleep(1000);
            }
        }
        assert isBsLocalUpAndRunning();
        isBrowserStackLocalRunning = true;
        logger.debug("Started browserstack local session");
    }

    @SneakyThrows
    public static void stop(Map<String, String> bsLocalArgs) {
        logger.debug("bslocal running: " + bsLocal.isRunning());
        logger.debug("Stopping browserstack local session");
        bsLocal.stop(bsLocalArgs);
        isBrowserStackLocalRunning = false;
        logger.debug("bslocal running: " + bsLocal.isRunning());
        logger.debug("Stopped browserstack local session");
    }

    private static String browserStackLocalResponse() {

        logger.debug("Hitting browser stack local check url");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://bs-local.com:45691/check"))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();

        String response;

        try {
            response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .get();
            logger.debug("Got response: " + response);
        } catch (Exception e) {
            logger.debug(ExceptionUtils.getStackTrace(e));
            response = "";
        }

        return response;
    }

    private static boolean isBsLocalUpAndRunning() {
        String response = browserStackLocalResponse();
        logger.info("BS local response: " + response);
        return response.contains("Up and running");
    }
}
