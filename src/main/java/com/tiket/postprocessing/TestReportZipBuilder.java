package com.tiket.postprocessing;

import com.tiket.io.local.PropertiesReader;
import com.tiket.logging.ExecutionLogManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Builds test-report.zip (extent report + spark assets + execution log) within the configured attachment size limit.
 * The spark folder is required because the Spark reporter runs in offline mode and loads its CSS/JS from it.
 * Spark JS files are inlined into the zipped HTML because Gmail rejects archives containing .js files (552-5.7.0).
 * Fallback order when too large: extent + truncated log, extent only, truncated log only, no attachment.
 */
public class TestReportZipBuilder {

    private static final Logger logger = LogManager.getLogger(TestReportZipBuilder.class);
    private static final Properties EMAIL_PROPERTIES =
            PropertiesReader.read("src/main/resources/email.properties");

    static final String TEST_REPORT_ZIP_NAME = "test-report.zip";
    private static final String SPARK_ASSETS_DIR_NAME = "spark";

    private static final long ONE_MB = 1024L * 1024;
    private static final long ATTACHMENT_BUFFER_BYTES = ONE_MB;
    private static final long DEFAULT_ATTACHMENT_MAX_MB = 15L;
    private static final String LOG_TRUNCATION_MARKER =
            "...[earlier log entries truncated due to size limit]...\n";

    private TestReportZipBuilder() {
    }

    public static File buildSafely() {
        try {
            return build();
        } catch (Exception e) {
            logger.warn("Unable to build test report zip; continuing without attachment", e);
            return null;
        }
    }

    public static File build() throws IOException {
        ExecutionLogManager.flush();

        long maxZipSizeBytes = getMaxZipSizeBytes();
        long maxLogSourceBytes = maxZipSizeBytes * 2 / 5;
        long fallbackLogSourceBytes = Math.max(maxZipSizeBytes / 10, ONE_MB);

        File zipTarget = getTestReportZipFile();
        File extentFile = new File(ReportProcessor.EXTENT_REPORT_LOCATION).getAbsoluteFile();
        File logFile = ExecutionLogManager.getExecutionLogFile();

        List<File> tempFiles = new ArrayList<>();
        try {
            List<ZipSource> extentSources = collectExtentSources(extentFile, tempFiles);
            List<ZipSource> sources = new ArrayList<>(extentSources);
            ZipSource logSource = collectLogSource(logFile, tempFiles, maxLogSourceBytes);
            if (logSource != null) {
                sources.add(logSource);
            }
            if (sources.isEmpty()) {
                return null;
            }

            File zip = writeZip(zipTarget, sources);
            if (zip.length() <= maxZipSizeBytes) {
                return zip;
            }

            logger.warn("Test report zip exceeds {}MB ({} bytes). Retrying with smaller content.",
                    maxZipSizeBytes / ONE_MB, zip.length());
            deleteQuietly(zip);

            if (!extentSources.isEmpty()) {
                zip = writeZip(zipTarget, extentSources);
                if (zip.length() <= maxZipSizeBytes) {
                    logger.warn("Attached extent report only. Log file excluded to stay within {}MB.",
                            maxZipSizeBytes / ONE_MB);
                    return zip;
                }
                deleteQuietly(zip);
            }

            if (logFile != null && logFile.isFile() && logFile.length() > 0) {
                File smallerLog = truncateLogFile(logFile, fallbackLogSourceBytes, tempFiles);
                zip = writeZip(zipTarget, List.of(new ZipSource(smallerLog, logFile.getName())));
                if (zip.length() <= maxZipSizeBytes) {
                    logger.warn("Attached truncated log only. Extent report excluded to stay within {}MB.",
                            maxZipSizeBytes / ONE_MB);
                    return zip;
                }
                deleteQuietly(zip);
            }

            logger.warn(
                    "Unable to build test report zip within {}MB. Email will be sent without attachment.",
                    maxZipSizeBytes / ONE_MB);
            return null;
        } finally {
            for (File tempFile : tempFiles) {
                Files.deleteIfExists(tempFile.toPath());
            }
        }
    }

    static long getMaxZipSizeBytes() {
        long providerMaxBytes = getProviderMaxAttachmentBytes();
        long effectiveMax = providerMaxBytes - ATTACHMENT_BUFFER_BYTES;
        return Math.max(effectiveMax, ONE_MB);
    }

    private static long getProviderMaxAttachmentBytes() {
        String configuredMaxMb = EMAIL_PROPERTIES.getProperty("attachment.maxSizeMb");
        if (configuredMaxMb == null || configuredMaxMb.isBlank()) {
            return DEFAULT_ATTACHMENT_MAX_MB * ONE_MB;
        }

        try {
            long maxMb = Long.parseLong(configuredMaxMb.trim());
            if (maxMb <= 0) {
                logger.warn("Invalid attachment.maxSizeMb value '{}'. Using default {}MB.",
                        configuredMaxMb, DEFAULT_ATTACHMENT_MAX_MB);
                return DEFAULT_ATTACHMENT_MAX_MB * ONE_MB;
            }
            return maxMb * ONE_MB;
        } catch (NumberFormatException e) {
            logger.warn("Invalid attachment.maxSizeMb value '{}'. Using default {}MB.",
                    configuredMaxMb, DEFAULT_ATTACHMENT_MAX_MB);
            return DEFAULT_ATTACHMENT_MAX_MB * ONE_MB;
        }
    }

    static File getTestReportZipFile() {
        return new File(new File(ReportProcessor.EXTENT_REPORT_LOCATION).getParentFile(), TEST_REPORT_ZIP_NAME);
    }

    private static List<ZipSource> collectExtentSources(File extentFile, List<File> tempFiles) throws IOException {
        List<ZipSource> sources = new ArrayList<>();
        if (!extentFile.isFile()) {
            return sources;
        }

        Path reportDir = extentFile.getParentFile().toPath();
        Path sparkDir = reportDir.resolve(SPARK_ASSETS_DIR_NAME);
        if (!Files.isDirectory(sparkDir)) {
            logger.warn("Spark assets folder not found: {}. Extent report in zip may render without styles.", sparkDir);
            sources.add(new ZipSource(extentFile, extentFile.getName()));
            return sources;
        }

        List<Path> sparkFiles;
        try (Stream<Path> paths = Files.walk(sparkDir)) {
            sparkFiles = paths.filter(Files::isRegularFile).sorted().toList();
        }

        List<Path> scripts = sparkFiles.stream().filter(TestReportZipBuilder::isScript).toList();
        File html = scripts.isEmpty() ? extentFile : inlineScripts(extentFile, reportDir, scripts, tempFiles);
        sources.add(new ZipSource(html, extentFile.getName()));

        for (Path path : sparkFiles) {
            if (!isScript(path)) {
                sources.add(new ZipSource(path.toFile(), toEntryName(reportDir, path)));
            }
        }
        return sources;
    }

    private static boolean isScript(Path path) {
        return path.getFileName().toString().toLowerCase().endsWith(".js");
    }

    private static String toEntryName(Path baseDir, Path path) {
        return baseDir.relativize(path).toString().replace(File.separatorChar, '/');
    }

    /**
     * Writes a copy of the extent HTML with {@code <script src="spark/x.js"></script>} replaced by the inline script,
     * so the zip carries no .js files. The original report on disk is left untouched.
     */
    private static File inlineScripts(File extentFile, Path reportDir, List<Path> scripts, List<File> tempFiles)
            throws IOException {
        String html = Files.readString(extentFile.toPath(), StandardCharsets.UTF_8);
        for (Path script : scripts) {
            String scriptTag = "<script src=\"" + toEntryName(reportDir, script) + "\"></script>";
            if (!html.contains(scriptTag)) {
                logger.warn("Script tag not found in extent report, skipping inline: {}", scriptTag);
                continue;
            }
            String content = Files.readString(script, StandardCharsets.UTF_8);
            html = html.replace(scriptTag, "<script>\n" + content + "\n</script>");
        }

        File inlinedHtml = File.createTempFile("extent-report-inlined-", ".html");
        tempFiles.add(inlinedHtml);
        Files.writeString(inlinedHtml.toPath(), html, StandardCharsets.UTF_8);
        return inlinedHtml;
    }

    private static ZipSource collectLogSource(File logFile, List<File> tempFiles, long maxLogSourceBytes)
            throws IOException {
        if (logFile != null && logFile.isFile() && Files.size(logFile.toPath()) > 0) {
            return new ZipSource(truncateLogFile(logFile, maxLogSourceBytes, tempFiles), logFile.getName());
        }

        if (ExecutionLogManager.isInitialized()) {
            logger.warn("Execution log file is missing or empty: {}",
                    logFile != null ? logFile.getAbsolutePath() : ExecutionLogManager.EXECUTION_LOG_LOCATION);
        }
        return null;
    }

    private static File truncateLogFile(File logFile, long maxBytes, List<File> tempFiles) throws IOException {
        if (logFile.length() <= maxBytes) {
            return logFile;
        }

        File truncatedFile = File.createTempFile("test-automation-truncated-", ".log");
        tempFiles.add(truncatedFile);

        try (RandomAccessFile source = new RandomAccessFile(logFile, "r");
             FileOutputStream output = new FileOutputStream(truncatedFile)) {
            long startOffset = logFile.length() - maxBytes;
            source.seek(startOffset);
            // Skip the partial line at the cut point
            source.readLine();

            output.write(LOG_TRUNCATION_MARKER.getBytes(StandardCharsets.UTF_8));

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = source.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }

        return truncatedFile;
    }

    private static File writeZip(File zipFile, List<ZipSource> sources) throws IOException {
        File parent = zipFile.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Failed to create zip directory: " + parent.getPath());
        }

        try (FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
             ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
            zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);

            for (ZipSource source : sources) {
                addFileToZip(zipOutputStream, source);
            }
        }

        return zipFile;
    }

    private static void addFileToZip(ZipOutputStream zipOutputStream, ZipSource source) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(source.file())) {
            ZipEntry zipEntry = new ZipEntry(source.entryName());
            zipOutputStream.putNextEntry(zipEntry);

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, bytesRead);
            }

            zipOutputStream.closeEntry();
        }
    }

    private static void deleteQuietly(File file) {
        if (file != null && file.exists() && !file.delete()) {
            logger.warn("Failed to delete temporary zip file: {}", file.getPath());
        }
    }

    private record ZipSource(File file, String entryName) {
    }
}
