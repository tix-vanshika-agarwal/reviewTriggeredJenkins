package com.tiket.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Adds a per-run file appender so the email attachment only contains logs of the current execution.
 * Uses a separate directory because log/test-automation.log is owned by the daily RollingAppender.
 */
public class ExecutionLogManager {

    public static final String EXECUTION_LOG_LOCATION = "log/execution/test-automation.log";
    private static final String APPENDER_NAME = "ExecutionFileAppender";
    // Keep in sync with appender layout pattern in log4j2.properties
    private static final String LAYOUT_PATTERN =
            "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] [%logger{36}] [%X{caseId}] [%c-%M] - %msg%n";

    private static volatile boolean initialized;
    private static File executionLogFile;

    private ExecutionLogManager() {
    }

    public static void init() {
        synchronized (ExecutionLogManager.class) {
            if (initialized) {
                return;
            }

            executionLogFile = new File(EXECUTION_LOG_LOCATION).getAbsoluteFile();
            resetLogFile(executionLogFile);
            attachAppender(executionLogFile.getAbsolutePath());
            initialized = true;
        }
    }

    public static void flush() {
        if (!initialized) {
            return;
        }

        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Appender appender = context.getConfiguration().getAppender(APPENDER_NAME);
        if (appender instanceof FileAppender fileAppender) {
            fileAppender.getManager().flush();
        }
    }

    public static File getExecutionLogFile() {
        return executionLogFile;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    private static void resetLogFile(File logFile) {
        File logDir = logFile.getParentFile();
        if (logDir != null && !logDir.exists() && !logDir.mkdirs()) {
            throw new IllegalStateException("Failed to create log directory: " + logDir.getPath());
        }

        try {
            if (logFile.exists()) {
                Files.delete(logFile.toPath());
            }
            Files.createFile(logFile.toPath());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to reset execution log file: " + logFile.getPath(), e);
        }
    }

    private static void attachAppender(String absoluteLogPath) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();

        PatternLayout layout = PatternLayout.newBuilder()
                .withPattern(LAYOUT_PATTERN)
                .withConfiguration(config)
                .build();

        FileAppender appender = FileAppender.newBuilder()
                .setName(APPENDER_NAME)
                .withFileName(absoluteLogPath)
                .withAppend(true)
                .withImmediateFlush(true)
                .setLayout(layout)
                .setConfiguration(config)
                .build();
        appender.start();

        config.addAppender(appender);
        LoggerConfig rootConfig = config.getRootLogger();
        rootConfig.addAppender(appender, null, null);
        context.updateLoggers();
    }
}
