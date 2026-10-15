package com.tiket.postprocessing;

import com.tiket.testbase.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Centralized manager for screenshot cleanup operations.
 * Handles cleanup of test screenshots, summary screenshots, and Grafana images
 * with proper edge case handling and synchronization.
 */
public class ScreenshotCleanupManager {

    private static final Logger logger = LogManager.getLogger(ScreenshotCleanupManager.class);
    
    // Track screenshots that need cleanup after TestRail upload
    private static final ConcurrentHashMap<String, TestRailUploadStatus> testRailPendingScreenshots = new ConcurrentHashMap<>();
    
    // Track summary screenshot for email cleanup
    private static volatile String summaryScreenshotPath = null;
    private static final AtomicBoolean emailSent = new AtomicBoolean(false);
    
    // Track all test screenshots for extent report cleanup
    private static final List<String> allTestScreenshots = new ArrayList<>();
    private static final AtomicBoolean extentReportGenerated = new AtomicBoolean(false);
    
    // Track cumulative storage saved
    private static final AtomicLong totalStorageSaved = new AtomicLong(0);

    /**
     * Status tracking for TestRail screenshot uploads
     */
    private static class TestRailUploadStatus {
        final String screenshotPath;
        volatile boolean uploadComplete = false;
        volatile boolean uploadSuccess = false;

        TestRailUploadStatus(String screenshotPath) {
            this.screenshotPath = screenshotPath;
        }
    }

    /**
     * Register a test screenshot that was added to extent report and may be uploaded to TestRail.
     * 
     * @param screenshotPath Path to the screenshot file
     * @param willUploadToTestRail Whether this screenshot will be uploaded to TestRail
     */
    public static synchronized void registerTestScreenshot(String screenshotPath, boolean willUploadToTestRail) {
        if (screenshotPath == null || screenshotPath.isEmpty()) {
            return;
        }
        
        // Add to list for potential extent report cleanup
        if (!allTestScreenshots.contains(screenshotPath)) {
            allTestScreenshots.add(screenshotPath);
            logger.debug("Registered test screenshot: " + screenshotPath);
        }
        
        // Track for TestRail upload if needed
        if (willUploadToTestRail) {
            testRailPendingScreenshots.put(screenshotPath, new TestRailUploadStatus(screenshotPath));
            logger.debug("Screenshot registered for TestRail upload: " + screenshotPath);
        }
    }

    /**
     * Mark TestRail upload as complete for a screenshot.
     * 
     * @param screenshotPath Path to the screenshot file
     * @param success Whether the upload was successful
     */
    public static void markTestRailUploadComplete(String screenshotPath, boolean success) {
        if (screenshotPath == null) {
            return;
        }
        
        TestRailUploadStatus status = testRailPendingScreenshots.get(screenshotPath);
        if (status != null) {
            status.uploadComplete = true;
            status.uploadSuccess = success;
            logger.debug("TestRail upload marked complete for: " + screenshotPath + ", success: " + success);
            
            // Cleanup if configured and upload complete
            attemptTestRailScreenshotCleanup(screenshotPath, status);
        }
    }

    /**
     * Attempt to cleanup a screenshot after TestRail upload completes.
     */
    private static void attemptTestRailScreenshotCleanup(String screenshotPath, TestRailUploadStatus status) {
        String cleanupConfig = Configs.PROPERTIES_DATA.get("cleanup.testrail.screenshots");
        
        // Default to cleanup=true unless explicitly disabled
        if ("false".equalsIgnoreCase(cleanupConfig)) {
            logger.debug("TestRail screenshot cleanup disabled (cleanup.testrail.screenshots=false)");
            return;
        }
        
        if (!status.uploadComplete) {
            logger.debug("TestRail upload not complete yet for: " + screenshotPath);
            return;
        }
        
        // Cleanup even if upload failed - image is already in extent report
        boolean deleted = deleteScreenshotSafely(screenshotPath, "TestRail Upload Complete");
        if (deleted) {
            testRailPendingScreenshots.remove(screenshotPath);
            allTestScreenshots.remove(screenshotPath);
        }
    }

    /**
     * Register the summary screenshot for cleanup after email is sent.
     * 
     * @param screenshotPath Path to the summary screenshot file
     */
    public static synchronized void registerSummaryScreenshot(String screenshotPath) {
        if (screenshotPath != null && !screenshotPath.isEmpty()) {
            summaryScreenshotPath = screenshotPath;
            emailSent.set(false);
            logger.debug("Registered summary screenshot: " + screenshotPath);
        }
    }

    /**
     * Mark email as sent successfully and cleanup summary screenshot if configured.
     * 
     * @param success Whether email was sent successfully
     */
    public static void markEmailSent(boolean success) {
        emailSent.set(success);
        logger.debug("Email marked as sent: " + success);
        
        if (success) {
            attemptSummaryScreenshotCleanup();
        }
    }

    /**
     * Attempt to cleanup the summary screenshot after email is sent.
     */
    private static void attemptSummaryScreenshotCleanup() {
        String cleanupConfig = Configs.PROPERTIES_DATA.get("cleanup.summary.screenshots");
        
        // Default to cleanup=true unless explicitly disabled
        if ("false".equalsIgnoreCase(cleanupConfig)) {
            logger.debug("Summary screenshot cleanup disabled (cleanup.summary.screenshots=false)");
            return;
        }
        
        if (!emailSent.get()) {
            logger.debug("Email not sent yet, keeping summary screenshot");
            return;
        }
        
        if (summaryScreenshotPath == null || summaryScreenshotPath.isEmpty()) {
            logger.debug("No summary screenshot registered for cleanup");
            return;
        }
        
        boolean deleted = deleteScreenshotSafely(summaryScreenshotPath, "Email Sent Successfully");
        if (deleted) {
            summaryScreenshotPath = null;
        }
    }

    /**
     * Mark extent report as generated and cleanup all test screenshots if configured.
     * This should be called AFTER the extent report is fully flushed and finalized.
     */
    public static void markExtentReportGenerated() {
        extentReportGenerated.set(true);
        logger.debug("Extent report marked as generated");
        attemptExtentReportScreenshotsCleanup();
    }

    /**
     * Attempt to cleanup all test screenshots after extent report is generated.
     * Only cleans up screenshots that are not pending TestRail upload.
     */
    private static void attemptExtentReportScreenshotsCleanup() {
        String cleanupConfig = Configs.PROPERTIES_DATA.get("cleanup.extent.screenshots");
        
        // Default to cleanup=true unless explicitly disabled
        if ("false".equalsIgnoreCase(cleanupConfig)) {
            logger.debug("Extent report screenshot cleanup disabled (cleanup.extent.screenshots=false)");
            return;
        }
        
        if (!extentReportGenerated.get()) {
            logger.debug("Extent report not generated yet, keeping screenshots");
            return;
        }
        
        synchronized (allTestScreenshots) {
            if (allTestScreenshots.isEmpty()) {
                logger.debug("No test screenshots to cleanup");
                return;
            }
            
            logger.info("Starting cleanup of " + allTestScreenshots.size() + " test screenshots after extent report generation");
            
            List<String> toCleanup = new ArrayList<>();
            int pendingTestRail = 0;
            
            // Identify screenshots that can be cleaned up
            for (String screenshotPath : allTestScreenshots) {
                // Check if this screenshot is still pending TestRail upload
                TestRailUploadStatus status = testRailPendingScreenshots.get(screenshotPath);
                if (status != null && !status.uploadComplete) {
                    pendingTestRail++;
                    logger.debug("Skipping cleanup - pending TestRail upload: " + extractFileName(screenshotPath));
                    continue;
                }
                toCleanup.add(screenshotPath);
            }
            
            // Cleanup eligible screenshots and track total storage freed
            int deletedCount = 0;
            int failedCount = 0;
            long totalBytesFreed = 0;
            
            for (String screenshotPath : toCleanup) {
                File file = new File(screenshotPath);
                long fileSize = file.exists() ? file.length() : 0;
                
                if (deleteScreenshotSafely(screenshotPath, "Extent Report Generated")) {
                    deletedCount++;
                    totalBytesFreed += fileSize;
                    allTestScreenshots.remove(screenshotPath);
                    testRailPendingScreenshots.remove(screenshotPath);
                } else {
                    failedCount++;
                }
            }
            
            String totalStorageFreed = formatFileSize(totalBytesFreed);
            logger.info("Test screenshots cleanup summary: " + deletedCount + " deleted, " + 
                       failedCount + " failed, " + pendingTestRail + " pending TestRail upload" +
                       " - Total storage freed: " + totalStorageFreed);
        }
    }

    /**
     * Cleanup Grafana screenshots after email is sent successfully.
     * Note: This method exists but is not currently used. Grafana cleanup is handled in GrafanaPage.java
     * 
     * @param imagePaths List of Grafana image paths
     * @param emailSuccess Whether email was sent successfully
     */
    public static void cleanupGrafanaScreenshots(List<String> imagePaths, boolean emailSuccess) {
        if (imagePaths == null || imagePaths.isEmpty()) {
            logger.debug("No Grafana images to cleanup");
            return;
        }
        
        String cleanupConfig = Configs.PROPERTIES_DATA.get("cleanup.grafana.screenshots");
        
        // Default to cleanup=true unless explicitly disabled
        if ("false".equalsIgnoreCase(cleanupConfig)) {
            logger.debug("Grafana screenshot cleanup disabled (cleanup.grafana.screenshots=false)");
            return;
        }
        
        if (!emailSuccess) {
            logger.warn("Grafana email not sent successfully, keeping screenshots for retry");
            return;
        }
        
        logger.info("Cleaning up " + imagePaths.size() + " Grafana screenshots after successful email");
        
        int deletedCount = 0;
        int failedCount = 0;
        
        for (String path : imagePaths) {
            if (deleteScreenshotSafely(path, "Grafana Email Sent")) {
                deletedCount++;
            } else {
                failedCount++;
            }
        }
        
        logger.info("Grafana screenshots cleanup summary: " + deletedCount + " deleted, " + failedCount + " failed");
    }

    /**
     * Safely delete a screenshot file with proper error handling.
     * 
     * @param screenshotPath Path to the screenshot file
     * @param reason Reason for deletion (for logging)
     * @return true if deleted successfully, false otherwise
     */
    private static boolean deleteScreenshotSafely(String screenshotPath, String reason) {
        if (screenshotPath == null || screenshotPath.isEmpty()) {
            return false;
        }
        
        try {
            File file = new File(screenshotPath);
            
            if (!file.exists()) {
                logger.debug("File already deleted or not found: " + extractFileName(screenshotPath));
                return true; // Consider it a success if already gone
            }
            
            // Additional safety checks
            if (file.isDirectory()) {
                logger.error("Cannot delete - path is a directory: " + screenshotPath);
                return false;
            }
            
            if (!file.canWrite()) {
                logger.error("Cannot delete - file is not writable: " + extractFileName(screenshotPath));
                return false;
            }
            
            // Get file size before deletion for storage tracking
            long fileSizeBytes = file.length();
            String fileSizeFormatted = formatFileSize(fileSizeBytes);
            
            // Attempt deletion
            boolean deleted = file.delete();
            
            if (deleted) {
                totalStorageSaved.addAndGet(fileSizeBytes); // Track cumulative storage saved
                logger.debug("Deleted screenshot [" + reason + "]: " + extractFileName(screenshotPath) + 
                            " - Storage freed: " + fileSizeFormatted);
                return true;
            } else {
                logger.warn("Failed to delete screenshot [" + reason + "]: " + extractFileName(screenshotPath));
                return false;
            }
            
        } catch (SecurityException se) {
            logger.error("Security exception deleting screenshot: " + extractFileName(screenshotPath) + " - " + se.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Error deleting screenshot: " + extractFileName(screenshotPath) + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * Force cleanup all remaining screenshots (emergency cleanup).
     * This should only be called at the very end of the test suite.
     */
    public static synchronized void forceCleanupAll() {
        logger.info("Force cleanup initiated for all remaining screenshots");
        
        // Cleanup summary screenshot
        if (summaryScreenshotPath != null) {
            deleteScreenshotSafely(summaryScreenshotPath, "Force Cleanup");
            summaryScreenshotPath = null;
        }
        
        // Cleanup all test screenshots
        synchronized (allTestScreenshots) {
            for (String path : allTestScreenshots) {
                deleteScreenshotSafely(path, "Force Cleanup");
            }
            allTestScreenshots.clear();
        }
        
        // Clear tracking maps
        testRailPendingScreenshots.clear();
        
        logger.info("Force cleanup completed");
    }

    /**
     * Extract filename from full path for logging.
     */
    private static String extractFileName(String path) {
        if (path == null) return "null";
        int lastSlash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        return lastSlash >= 0 ? path.substring(lastSlash + 1) : path;
    }

    /**
     * Format file size in human-readable format (B, KB, MB)
     * 
     * @param bytes File size in bytes
     * @return Formatted string (e.g., "2.5 MB", "150 KB")
     */
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            double kb = bytes / 1024.0;
            return String.format("%.2f KB", kb);
        } else {
            double mb = bytes / (1024.0 * 1024.0);
            return String.format("%.2f MB", mb);
        }
    }

    /**
     * Reset all tracking state (useful for testing).
     */
    public static synchronized void reset() {
        allTestScreenshots.clear();
        testRailPendingScreenshots.clear();
        summaryScreenshotPath = null;
        emailSent.set(false);
        extentReportGenerated.set(false);
        totalStorageSaved.set(0);
        logger.debug("ScreenshotCleanupManager reset completed");
    }

    /**
     * Get statistics for monitoring/debugging.
     */
    public static String getStatistics() {
        String totalSaved = formatFileSize(totalStorageSaved.get());
        return String.format(
            "ScreenshotCleanupManager Stats - Test Screenshots: %d, Pending TestRail: %d, " +
            "Summary Screenshot: %s, Email Sent: %s, Extent Report Generated: %s, " +
            "Total Storage Saved: %s",
            allTestScreenshots.size(),
            testRailPendingScreenshots.size(),
            summaryScreenshotPath != null ? "registered" : "none",
            emailSent.get(),
            extentReportGenerated.get(),
            totalSaved
        );
    }
}

