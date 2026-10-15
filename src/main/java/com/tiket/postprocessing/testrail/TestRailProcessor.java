package com.tiket.postprocessing.testrail;

import com.tiket.io.CachedPool;
import com.tiket.postprocessing.ScreenshotCleanupManager;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.ITestResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TestRailProcessor {

    private static final Logger logger = LogManager.getLogger(TestRailProcessor.class);
    private static APIClient client;

    public static CompletableFuture<Object> sendResult(ITestResult result, String runID, String caseId, String screenshotPath) {

        logger.info("sending result");
        APIClient client = getClient();

        Map<String, Object> data = getTestResultData(result);
        data.put("custom_reusable", 1);
        data.put("id", runID);
        data.put("test_id", caseId);
        logger.info("data -> " + data);
        String addResultUri = "add_result_for_case/" + runID + "/" + caseId;
        logger.info("addResultUri -> " + addResultUri);
        CompletableFuture<Object> postResult = CompletableFuture.supplyAsync(() -> post(client, addResultUri, data), CachedPool.POOL);

        if(screenshotPath != null) {
            postResult.thenApply(r -> ((JSONObject) r).get("id"))
                    .thenApply(String::valueOf)
                    .thenAcceptAsync(id -> {
                        Object uploadResult = post(client, "add_attachment_to_result/" + id, screenshotPath);
                        // Mark upload as complete for cleanup tracking
                        boolean success = uploadResult != null;
                        ScreenshotCleanupManager.markTestRailUploadComplete(screenshotPath, success);
                        if (success) {
                            logger.info("TestRail screenshot upload successful: " + screenshotPath);
                        } else {
                            logger.warn("TestRail screenshot upload failed: " + screenshotPath);
                        }
                    }, CachedPool.POOL)
                    .exceptionally(ex -> {
                        // Handle upload failure
                        logger.error("Exception during TestRail screenshot upload: " + ex.getMessage());
                        ScreenshotCleanupManager.markTestRailUploadComplete(screenshotPath, false);
                        return null;
                    });
        }

        logger.info("sent result");
        return postResult;
    }

    private static Object post(APIClient client, String uri, Object data) {
        try {
            return client.sendPost(uri, data);
        } catch (IOException e) {
            logger.error(e);
            logger.error("Failed api call to: " + uri);
            return null;
        }
    }

    private static Object get(APIClient client, String uri) {
        try {
            return client.sendGet(uri);
        } catch (IOException e) {
            logger.error(e);
            logger.error("Failed api call to: " + uri);
            return null;
        }
    }

    private static APIClient getClient() {
        if (client == null) {
            client = new APIClient(Configs.PROPERTIES_DATA.get("testrail.url"));
            client.setUser(Configs.CREDENTIALS.getProperty("testrail.user"));
            client.setPassword(Configs.CREDENTIALS.getProperty("testrail.password"));
        }
        return client;
    }

    private static Map<String, Object> getTestResultData(ITestResult result) {

        Map<String, Object> data = new HashMap<>();

        switch (result.getStatus()) {
            case ITestResult.SUCCESS -> data.put("status_id", 1);
            case ITestResult.FAILURE -> data.put("status_id", 5);
            case ITestResult.SKIP -> data.put("status_id", 4);
        }

        if (result.isSuccess()) {
            data.put("comment", "Test passed: " + result.getMethod().getConstructorOrMethod().getMethod().getName());
        } else {
            data.put("comment", ExceptionUtils.getStackTrace(result.getThrowable()));
        }

        return data;
    }
}
