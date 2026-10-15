package com.tiket.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentKlovReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.tiket.core.CoreFunctions;
import com.tiket.logging.Log4JLogger;
import com.tiket.testbase.Configs;
import org.apache.logging.log4j.LogManager;
import org.testng.ITestResult;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ExtentReportManager {

    private static final ExtentReports extentReports = new ExtentReports();
    private static volatile boolean isInitialized = false;
    private static volatile String klovReportName;
    private static volatile ExtentSparkReporter sparkReporter;
    private static final AtomicLong extentInvocationSequence = new AtomicLong();
    
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(Log4JLogger.class);
    
    public synchronized static ExtentReports getReports() {
        if (!isInitialized) {
            attachReporter();
            logger.info("Config.DB_USE is : " + Configs.DB_USE);
            final boolean useKlov = Configs.DB_USE && CoreFunctions.isJenkinsSystem();
            if (useKlov) {
                attachKlovReporter();
            } else {
                logger.info("Currently, only Jenkins runs with dbFlag can attach data to Klov.");
            }
            setupSystemInfo();
            if (useKlov) {
                materializeKlovBuild();
            }
            isInitialized = true;
        }
        return extentReports;
    }

    /**
     * Force the Klov reporter to create its {@code Build} document in MongoDB
     * on this single initialization thread, before TestNG starts running
     * {@code @Test} methods in parallel.
     *
     * <p>{@link ExtentKlovReporter} creates the Build lazily inside its test
     * observer ({@code onNext}) the first time an {@link ExtentTest} is created.
     * An empty {@code flush()} is a no-op because the reporter returns early when
     * the test list is empty. With {@code parallel=methods}, many worker threads
     * can pass the {@code initiated} guard before {@code compareAndSet} completes,
     * each calling {@code setupReport()} and inserting its own empty Build — one
     * launch gets all tests, the rest stay empty in Klov.
     *
     * <p>Creating a bootstrap test here triggers {@code setupReport()} once on the
     * init thread and sets {@code initiated=true} before any parallel worker runs.
     */
    private static void materializeKlovBuild() {
        try {
            ExtentTest bootstrap = extentReports.createTest("__klov_bootstrap__");
            bootstrap.info("Materializing Klov build before parallel execution");
            extentReports.flush();
            extentReports.removeTest(bootstrap);
            logger.info("Klov build materialized via bootstrap test (prevents duplicate launches under parallel execution)");
        } catch (Exception e) {
            logger.warn("Failed to materialize Klov build during init: {}", e.getMessage());
        }
    }

    private static void attachReporter() {
        String extentReportName = "./extent-reports/extent-report.html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(extentReportName);
        String platform = systemPropertyOrDash("platform");
        String environment = systemPropertyOrDash("env");
        configureSparkReporter(reporter,
                "TIKET Automation — " + platform + " / " + environment,
                "Test execution report");
        sparkReporter = reporter;
        extentReports.attachReporter(reporter);
    }

    /**
     * Spark defaults aligned with common enterprise reporting: UTF-8, readable theme, ISO-style
     * timestamps, execution timeline, base64 thumbnails, and offline (self-contained) HTML so
     * reports open without external CDN access (air-gapped / locked-down networks).
     */
    private static void configureSparkReporter(ExtentSparkReporter reporter, String documentTitle, String reportName) {
        reporter.config().setEncoding("utf-8");
        reporter.config().setDocumentTitle(documentTitle);
        reporter.config().setReportName(reportName);
        reporter.config().setTheme(Theme.STANDARD);
        reporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        reporter.config().setTimelineEnabled(true);
        reporter.config().thumbnailForBase64(true);
        reporter.config().setOfflineMode(true);
    }

    private static void setupSystemInfo() {
        extentReports.setSystemInfo("Platform", systemPropertyOrDash("platform"));
        extentReports.setSystemInfo("Environment", systemPropertyOrDash("env"));
        extentReports.setSystemInfo("Test type", systemPropertyOrDash("testtype"));
        extentReports.setSystemInfo("Threads", systemPropertyOrDash("threads"));
        extentReports.setSystemInfo("Sessions", systemPropertyOrDash("sessions"));
        extentReports.setSystemInfo("Test data", systemPropertyOrDash("testdata"));
        extentReports.setSystemInfo("Tribe", systemPropertyOrDash("tribe"));
        extentReports.setSystemInfo("Module", systemPropertyOrDash("module"));
        extentReports.setSystemInfo("Test name filter", systemPropertyOrDash("testname"));
        extentReports.setSystemInfo("Device farm", String.valueOf(Configs.DEVICE_FARM));
        extentReports.setSystemInfo("DB reporting", String.valueOf(Configs.DB_USE));
        extentReports.setSystemInfo("Java", systemPropertyOrDash("java.version"));
        extentReports.setSystemInfo("OS", osSummary());
        extentReports.setSystemInfo("Host", hostNameForReport());
        extentReports.setSystemInfo("CI build", firstNonBlankEnv("BUILD_NUMBER", "GITHUB_RUN_NUMBER"));
        extentReports.setSystemInfo("CI job", firstNonBlankEnv("JOB_NAME", "GITHUB_WORKFLOW"));
        extentReports.setSystemInfo("CI URL", firstNonBlankEnv("BUILD_URL", "GITHUB_SERVER_URL"));
    }

    private static String systemPropertyOrDash(String key) {
        String v = System.getProperty(key);
        return (v == null || v.isBlank()) ? "—" : v;
    }

    private static String osSummary() {
        String name = System.getProperty("os.name", "—");
        String arch = System.getProperty("os.arch", "");
        return arch.isBlank() ? name : name + " (" + arch + ")";
    }

    private static String hostNameForReport() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            String fromEnv = firstNonBlankEnv("HOSTNAME", "COMPUTERNAME");
            return fromEnv.isBlank() ? "—" : fromEnv;
        }
    }

    private static String firstNonBlankEnv(String... keys) {
        for (String k : keys) {
            String v = System.getenv(k);
            if (v != null && !v.isBlank()) {
                return v;
            }
        }
        return "—";
    }

    private static Map<String, String> getKlovConfigSafely() {
        try {
            return CoreFunctions.getKlovAndMongoData();
        } catch (Exception e) {
            logger.error("Failed to get Klov configuration: " + e.getMessage(), e);
            return null;
        }
    }

    private static void attachKlovReporter() {
        logger.info("Attaching KlovReporter to the extent report");

        String projectName = CoreFunctions.getProjectName();
        Map<String, String> klovConfig = getKlovConfigSafely();

        // Check if configuration is available
        if (klovConfig == null) {
            logger.warn("Klov configuration not available. Skipping Klov reporter.");
            return;
        }

        // Validate required fields
        if (!klovConfig.containsKey("klovServer") || 
            !klovConfig.containsKey("mongoHost") || 
            !klovConfig.containsKey("mongoPort")) {
            logger.warn("Incomplete Klov configuration. Skipping Klov reporter.");
            return;
        }

        String klovServer = klovConfig.get("klovServer");
        String mongoHost = klovConfig.get("mongoHost");
        int mongoPort = Integer.parseInt(klovConfig.get("mongoPort"));

        if (!isMongoReachable(mongoHost, mongoPort, 2000)) {
            logger.warn("MongoDB not reachable at {}:{}. Skipping Klov.", mongoHost, mongoPort);
            return;
        }

        try {
            ExtentKlovReporter klov = new ExtentKlovReporter(projectName);
            if (CoreFunctions.isJenkinsSystem()) {
                klovReportName = System.getenv("BUILD_URL") + System.currentTimeMillis();
                logger.info("Using Jenkins build URL as Klov report name: " + klovReportName);
                klov.setReportName(klovReportName);
            } else {
                klovReportName = "Local Execution - " + System.currentTimeMillis();
                logger.info("Using local execution name for Klov report: " + klovReportName);
                klov.setReportName(klovReportName);
            }
            klov.initKlovServerConnection(klovServer)
                    .initMongoDbConnection(mongoHost, mongoPort);

            extentReports.attachReporter(klov);
            logger.info("Successfully attached  KlovReporter to the extent report");
        } catch (Exception e) {
            logger.warn("Failed to attach KlovReporter due to: {}", e.getMessage());
        }
    }

    private static boolean isMongoReachable(String host, int port, int timeoutMillis) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeoutMillis);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static String displayNameForExtent(ITestResult result) {
        String className = result.getTestClass().getRealClass().getName();
        String method = result.getMethod().getMethodName();
        int paramHash = Arrays.deepHashCode(result.getParameters());
        long nano = System.nanoTime();
        long seq = extentInvocationSequence.incrementAndGet();
        String name = className + "#" + method + "@" + paramHash + "@" + nano + "@" + seq;
        logger.info("Generated display name for extent report: {}", name);
        return name;
    }

    /**
     * Klov build name for the current JVM run ({@code report.name} in Mongo).
     * Used by {@link KlovReportStatsNormalizer} after suite flush.
     */
    public static String getKlovReportName() {
        return klovReportName;
    }

    public synchronized static void applyTribeView() {
        try {
            if (sparkReporter != null && TribeReportView.hasData()) {
                sparkReporter.config().setJs(TribeReportView.buildJs());
                logger.info("Applied Tribe view to Spark report");
            }
        } catch (Exception e) {
            logger.warn("Failed to apply Tribe view to Spark report", e);
        }
    }

    public synchronized static void flushReports(boolean safeToFlush) {
        if (!safeToFlush) {
            return;
        }
        if (isInitialized) {
            applyTribeView();
            extentReports.flush();
            logger.info("Flushed extent reports");
        } else {
            logger.warn("Attempted to flush extent reports before initialization");
        }
    }
}
