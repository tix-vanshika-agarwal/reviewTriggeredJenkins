package com.tiket.apiPage;

import com.aventstack.extentreports.ExtentTest;
import com.tiket.api.monitoring.GrafanaSnapGetApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testng.TestListener;
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class GetGrafanaSnapApi <T extends WebDriver> extends BasePage<T> {
    public GetGrafanaSnapApi(Session<T> session) {
        super(session);
    }
    /**
     * Captures Grafana dashboard screenshot and saves it to the screenshots/grafana directory.
     *
     * @param dashboardId Dashboard ID from Grafana (e.g., "adg8nfm")
     * @param verticalName Vertical name (e.g., "accommodation", "flight", "train")
     * @param orgId Organization ID
     * @param panelId Panel ID
     * @param metricType Metric type: "ExecutionStatus", "ExecutionTime", or "Screens"
     * @param platform Platform type: "Dweb", "Android", or "iOS"
     * @param from Time range start (e.g., "now-24h")
     * @param to Time range end (e.g., "now")
     * @param timezone Timezone (e.g., "Asia/Singapore")
     * @param width Screenshot width in pixels (default: 1400)
     * @param height Screenshot height in pixels (default: 550)
     * @param theme Render theme: "light" (day mode) or "dark". Pass null/blank to use Grafana default.
     * @return The file path where the screenshot was saved, or null if failed
     */
    @SneakyThrows
    public String captureGrafanaSnapshot(
            String dashboardId,
            String verticalName,
            int orgId,
            int panelId,
            String metricType,
            String platform,
            String from,
            String to,
            String timezone,
            int width,
            int height,
            String theme
    ) {
        try {
            // Create API inputs
            GrafanaSnapGetApi.Inputs inputs = new GrafanaSnapGetApi.Inputs(
                    dashboardId,
                    verticalName,
                    orgId,
                    panelId,
                    from,
                    to,
                    timezone,
                    width,
                    height,
                    theme
            );

            // Hit the API and get binary response (image bytes)
            GrafanaSnapGetApi grafanaSnapGetApi = new GrafanaSnapGetApi(inputs);
            byte[] imageBytes = grafanaSnapGetApi.getBinaryResponse(apiTools);

            logger.log("Response -> Image bytes received: " + imageBytes.length + " bytes");

            // Sanity check: response MUST start with the PNG magic bytes (89 50 4E 47 0D 0A 1A 0A).
            // If we get HTML/JSON instead it almost always means one of:
            //   1. Grafana Image Renderer plugin is NOT installed on the Grafana server
            //   2. Bearer token is invalid/expired and Grafana is serving the login SPA HTML
            //   3. The dashboard/panel is restricted for this token's permissions
            // Failing fast here prevents broken-image emails and HTML files saved as ".png".
            if (!isPng(imageBytes)) {
                String preview = previewBody(imageBytes);
                logger.fail("Grafana did NOT return a PNG for panel " + panelId
                        + " (" + platform + " - " + metricType + ")");
                logger.log("Response size: " + imageBytes.length + " bytes");
                logger.log("Likely cause: image-renderer plugin missing OR invalid/expired Grafana token");
                logger.log("Response preview (first 200 chars): " + preview);
                return null;
            }

            // Build filename: verticalName_metricType_platform_timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = String.format("%s_%s_%s_%s",
                    verticalName,
                    metricType,
                    platform,
                    timestamp);

            // Save to screenshots/grafana/ directory
            String savedPath = CoreFunctions.processPng(imageBytes, filename, "grafana");

            logger.log("Grafana snapshot saved: " + savedPath);

            // Automatically add image to Extent Report
            addImageToExtentReport(savedPath, platform, metricType);

            return savedPath;

        } catch (Exception e) {
            logger.log("Error capturing Grafana snapshot: " + ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    /**
     * Adds the captured Grafana image to Extent Report automatically
     */
    private void addImageToExtentReport(String imagePath, String platform, String metricType) {
        try {
            // Check if we're in a test context

            ExtentTest test = TestListener.extentTestThreadLocal.get();

            // Convert image to base64
            String base64Image = convertImageToBase64(imagePath);

            // Add to extent report with descriptive label
            String label = String.format("Grafana: %s - %s", platform, metricType);
            test.info(label, test.addScreenCaptureFromBase64String(base64Image).getModel().getMedia().get(0));

            logger.log("Added to Extent Report: " + platform + " - " + metricType);
        } catch (Exception e) {
            logger.log("Could not add image to Extent Report: " + e.getMessage());
        }
    }

    /**
     * Returns true if the given bytes start with the PNG magic signature.
     * PNG header: 89 50 4E 47 0D 0A 1A 0A
     */
    private static boolean isPng(byte[] bytes) {
        if (bytes == null || bytes.length < 8) {
            return false;
        }
        return (bytes[0] & 0xFF) == 0x89
                && (bytes[1] & 0xFF) == 0x50
                && (bytes[2] & 0xFF) == 0x4E
                && (bytes[3] & 0xFF) == 0x47
                && (bytes[4] & 0xFF) == 0x0D
                && (bytes[5] & 0xFF) == 0x0A
                && (bytes[6] & 0xFF) == 0x1A
                && (bytes[7] & 0xFF) == 0x0A;
    }

    /**
     * Returns a short, single-line preview of the response body for logging.
     * Used to help diagnose when Grafana returns HTML/JSON instead of PNG.
     */
    private static String previewBody(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "<empty body>";
        }
        int len = Math.min(bytes.length, 200);
        String s = new String(bytes, 0, len, java.nio.charset.StandardCharsets.UTF_8);
        return s.replaceAll("\\s+", " ").trim();
    }

    /**
     * Convert image file to base64 string for Extent Report
     */
    private String convertImageToBase64(String imagePath) throws IOException {
        String optimized = CoreFunctions.encodeImageFileForExtentReport(new File(imagePath));
        if (optimized != null) {
            return optimized;
        }
        File file = new File(imagePath);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(fileContent);
    }

    /**
     * Overloaded method with default width, height and "light" (day mode) theme.
     */
    @SneakyThrows
    public String captureGrafanaSnapshot(
            String dashboardId,
            String verticalName,
            int orgId,
            int panelId,
            String metricType,
            String platform,
            String from,
            String to,
            String timezone
    ) {
        return captureGrafanaSnapshot(
                dashboardId,
                verticalName,
                orgId,
                panelId,
                metricType,
                platform,
                from,
                to,
                timezone,
                1400,
                550,
                "light"
        );
    }

    /**
     * Convenience method for 24-hour snapshot in day mode (light theme).
     */
    @SneakyThrows
    public String captureGrafanaSnapshot24h(
            String dashboardId,
            String verticalName,
            int orgId,
            int panelId,
            String metricType,
            String platform
    ) {
        return captureGrafanaSnapshot(
                dashboardId,
                verticalName,
                orgId,
                panelId,
                metricType,
                platform,
                "now-24h",
                "now",
                "Asia/Singapore"
        );
    }
}
