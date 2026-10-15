package com.tiket.web.page.monitoring;

import com.tiket.apiPage.GetGrafanaSnapApi;
import com.tiket.model.Session;
import com.tiket.postprocessing.GrafanaEmailSender;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Map;

public class GrafanaPage<T extends WebDriver> extends BasePage<T> {
    public GrafanaPage(Session<T> session) {
        super(session);
    }

    public String getGrafanaSnapPath(
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
        GetGrafanaSnapApi<?> grafanaSnapApi = new GetGrafanaSnapApi<>(session);
        String path = grafanaSnapApi.captureGrafanaSnapshot(
                dashboardId,
                verticalName,
                orgId,
                panelId,
                metricType,
                platform,
                from,
                to,
                timezone,
                width,
                height,
                theme
        );
        logger.log("Grafana snapshot saved at: " + path);
        return path;
    }

    /**
     * Captures Grafana snapshots for all platforms and metrics, then sends email report
     *
     * @param dashboardId Dashboard ID
     * @param verticalName Vertical name (e.g., "accommodation")
     * @param orgId Organization ID
     * @param panelIds Map of platform-metric combinations to panel IDs
     * @param timezone Timezone for snapshots and dashboard links (e.g., "Asia/Singapore")
     * @param width Screenshot width in pixels
     * @param height Screenshot height in pixels
     * @param theme Render theme: "light" (day mode) or "dark". Sourced from {@code GrafanaSnapConfig}
     *              (resolution chain: {@code theme.<vertical>} -> {@code theme} -> default).
     * @param from Grafana time-range start (e.g. "now-7d"). Sourced from {@code GrafanaSnapConfig}.
     * @param to Grafana time-range end (e.g. "now-2d"). Sourced from {@code GrafanaSnapConfig}.
     * @param reportPeriodLabel Human-readable label shown in the email body's "Report Period".
     *                          Auto-derived from from/to by {@code GrafanaSnapConfig} unless overridden.
     * @return List of captured snapshot paths
     */
    public ArrayList<String> captureAndSendGrafanaReport(
            String dashboardId,
            String verticalName,
            int orgId,
            Map<String, Map<String, Integer>> panelIds,
            String timezone,
            int width,
            int height,
            String theme,
            String from,
            String to,
            String reportPeriodLabel
    ) {
        // Initialize
        logger.step("Capture Grafana Monitoring Snapshots - " + capitalizeFirst(verticalName));
        logger.log("Dashboard ID: " + dashboardId);
        logger.log("Organization ID: " + orgId);
        logger.log("Time Range: " + from + " -> " + to
                + (reportPeriodLabel == null || reportPeriodLabel.isBlank() ? "" : "  (" + reportPeriodLabel + ")"));
        logger.log("Timezone: " + timezone);
        logger.log("Screenshot Size: " + width + "x" + height);
        logger.log("Theme: " + (theme == null || theme.isBlank() ? "(grafana default)" : theme));

        String[] metricTypes = {"ExecutionStatus", "ExecutionTime", "Screens"};
        String[] platforms = {"Dweb", "Android", "iOS"};
        ArrayList<String> paths = new ArrayList<>();

        // Capture all snapshots by platform
        for (String platform : platforms) {
            logger.step("Capture " + platform + " Platform Metrics");

            for (String metricType : metricTypes) {
                logger.step("Capture " + metricType + " for " + platform);

                Integer panelId = panelIds.get(platform).get(metricType);
                if (panelId == null) {
                    logger.warn("Panel ID not found for " + platform + " - " + metricType);
                    continue;
                }

                logger.log("Panel ID: " + panelId);
                logger.log("Metric Type: " + metricType);
                logger.log("Platform: " + platform);

                String path = getGrafanaSnapPath(
                        dashboardId,
                        verticalName,
                        orgId,
                        panelId,
                        metricType,
                        platform,
                        from,
                        to,
                        timezone,
                        width,
                        height,
                        theme
                );

                if (path != null) {
                    paths.add(path);
                    logger.log("Snapshot captured successfully");
                    logger.log("File: " + extractFileName(path));
                } else {
                    logger.warn("Failed to capture snapshot");
                }
            }
        }

        // Summary
        logger.step("Grafana Snapshot Capture Summary");
        logger.log("Total Snapshots Captured: " + paths.size() + " / " + (platforms.length * metricTypes.length));
        logger.log("Storage Location: screenshots/grafana/");
        logger.log("Vertical: " + capitalizeFirst(verticalName));

        // List all captured files
        if (!paths.isEmpty()) {
            logger.log("Captured Files:");
            for (int i = 0; i < paths.size(); i++) {
                logger.log((i + 1) + ". " + extractFileName(paths.get(i)));
            }
        }

        // Send email if enabled (pass timezone, theme, from/to and period label so the dashboard
        // links, rendering theme and "Report Period" stay in sync with what was captured).
        sendGrafanaEmailReport(paths, verticalName, platforms, metricTypes,
                dashboardId, orgId, panelIds, timezone, from, to, reportPeriodLabel, theme);

        return paths;
    }

    /**
     * Sends Grafana monitoring report via email if enabled
     */
    private void sendGrafanaEmailReport(ArrayList<String> paths, String verticalName,
                                        String[] platforms, String[] metricTypes,
                                        String dashboardId, int orgId,
                                        Map<String, Map<String, Integer>> panelIds,
                                        String timezone,
                                        String from, String to, String reportPeriodLabel,
                                        String theme) {
        logger.step("Send Grafana Monitoring Report via Email");

        // Check if Grafana email sending is enabled
        String emailConfig = Configs.PROPERTIES_DATA.get("send.grafana.email");
        logger.log("Email Configuration (send.grafana.email): " +
                (emailConfig == null ? "not set (enabled by default)" : emailConfig));

        if ("false".equalsIgnoreCase(emailConfig)) {
            logger.log("Grafana email sending is disabled");
            logger.log("To enable: set send.grafana.email=true in configuration");
            return;
        }

        if (paths.isEmpty()) {
            logger.warn("No snapshots available to send via email");
            return;
        }

        boolean emailSentSuccessfully = false;

        String resolvedRecipients = GrafanaEmailSender.resolveRecipients(verticalName);
        String verticalKeySuffix = (verticalName == null || verticalName.isEmpty())
                ? "<vertical>" : verticalName.toLowerCase();

        if (resolvedRecipients == null || resolvedRecipients.isEmpty()) {
            logger.fail("No recipients configured - email NOT sent");
            logger.log("Configure 'recipients' (global) and/or 'recipients." + verticalKeySuffix
                    + "' in properties");
            logger.log("Images NOT deleted due to email skip (available for debugging)");
            return;
        }

        try {
            logger.log("Preparing email with " + paths.size() + " embedded images...");
            logger.log("Vertical: " + capitalizeFirst(verticalName));
            logger.log("Recipients (global + recipients." + verticalKeySuffix + "): " + resolvedRecipients);
            logger.log("Timezone: " + timezone);
            logger.log("Time Range: " + from + " -> " + to
                    + (reportPeriodLabel == null || reportPeriodLabel.isBlank() ? "" : "  (" + reportPeriodLabel + ")"));
            logger.log("Theme: " + (theme == null || theme.isBlank() ? "(grafana profile default)" : theme));
            logger.log("Each image is clickable and links to the live Grafana dashboard");

            GrafanaEmailSender.sendGrafanaEmail(paths, verticalName, platforms, metricTypes,
                    dashboardId, orgId, panelIds, timezone, resolvedRecipients,
                    from, to, reportPeriodLabel, theme);

            logger.log("Email sent successfully!");
            logger.log("Subject: Grafana Monitoring Report - " + capitalizeFirst(verticalName));
            logger.log("Attached Images: " + paths.size());
            
            emailSentSuccessfully = true;

        } catch (Exception e) {
            logger.fail("Failed to send Grafana email");
            logger.log("Error: " + e.getMessage());
            logger.log("Images NOT deleted due to email failure (available for debugging)");
            e.printStackTrace();
        }

        // Cleanup images after successful email send (if enabled)
        if (emailSentSuccessfully) {
            String cleanupConfig = Configs.PROPERTIES_DATA.get("cleanup.grafana.images");
            if ("true".equalsIgnoreCase(cleanupConfig)) {
                deleteGrafanaImages(paths);
            } else {
                logger.log("Image cleanup disabled (cleanup.grafana.images=false)");
                logger.log("Images preserved in: screenshots/grafana/");
            }
        }
    }

    /**
     * Deletes Grafana snapshot images from disk after email is sent
     * Images are already embedded in Extent Report and Email, so safe to delete
     */
    private void deleteGrafanaImages(ArrayList<String> imagePaths) {
        if (imagePaths == null || imagePaths.isEmpty()) {
            return;
        }
        
        logger.step("Cleanup Grafana Snapshots from Disk");
        logger.log("Note: Images are preserved in Extent Report (base64) and Email (inline)");
        
        int deletedCount = 0;
        int failedCount = 0;
        
        for (String path : imagePaths) {
            try {
                java.io.File file = new java.io.File(path);
                if (file.exists()) {
                    if (file.delete()) {
                        deletedCount++;
                        logger.log("Deleted: " + extractFileName(path));
                    } else {
                        failedCount++;
                        logger.warn("Failed to delete: " + extractFileName(path));
                    }
                } else {
                    logger.warn("File not found: " + extractFileName(path));
                }
            } catch (Exception e) {
                failedCount++;
                logger.warn("Error deleting " + extractFileName(path) + ": " + e.getMessage());
            }
        }
        
        logger.log("Cleanup Summary: " + deletedCount + " deleted, " + failedCount + " failed");
        
        if (deletedCount > 0) {
            logger.log("Disk space freed: ~" + (deletedCount * 100) + "KB - " + (deletedCount * 200) + "KB (estimated)");
        }
    }

    /**
     * Helper method to extract filename from full path
     */
    private String extractFileName(String path) {
        if (path == null || path.isEmpty()) {
            return "";
        }
        int lastSeparator = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        return lastSeparator >= 0 ? path.substring(lastSeparator + 1) : path;
    }

    /**
     * Helper method to capitalize first letter
     */
    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
