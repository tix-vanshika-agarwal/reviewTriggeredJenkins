package com.tiket.io.adb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ADB {

    private Logger logger = LogManager.getLogger(ADB.class);
    private String deviceID;
    private String command;

    public ADB(String deviceID, String command) {
        this.deviceID = deviceID;
        this.command = command;
    }

    public ADB(String command) {
        this.command = command;
    }

    /**
     * Runs {@code adb [-s &lt;deviceId&gt;] shell &lt;command&gt;} and waits for completion.
     * Returns the process exit code, or {@code -1} if the process could not be started / timed out.
     */
    public int execShell() {
        List<String> cmd = new ArrayList<>();
        cmd.add("adb");
        if (deviceID != null && !deviceID.isBlank()) {
            cmd.add("-s");
            cmd.add(deviceID.trim());
        }
        cmd.add("shell");
        // Split shell payload on whitespace so ProcessBuilder does not tokenize incorrectly.
        for (String part : command.trim().split("\\s+")) {
            if (!part.isEmpty()) {
                cmd.add(part);
            }
        }
        return runAndWait(cmd, 30);
    }

    public void exec() {
        List<String> cmd = new ArrayList<>();
        cmd.add("adb");
        if (deviceID != null && !deviceID.isBlank()) {
            cmd.add("-s");
            cmd.add(deviceID.trim());
        }
        for (String part : command.trim().split("\\s+")) {
            if (!part.isEmpty()) {
                cmd.add(part);
            }
        }
        new Thread(() -> runAndWait(cmd, 30)).start();
    }

    private int runAndWait(List<String> cmd, int timeoutSeconds) {
        logger.info("Executing adb command: " + String.join(" ", cmd));
        try {
            Process process = new ProcessBuilder(cmd)
                    .redirectErrorStream(true)
                    .start();
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.isBlank()) {
                        output.append(line).append('\n');
                    }
                }
            }
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                logger.error("ADB command timed out after " + timeoutSeconds + "s: " + String.join(" ", cmd));
                return -1;
            }
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                logger.error("ADB command failed (exit=" + exitCode + "): " + String.join(" ", cmd)
                        + (output.isEmpty() ? "" : " | output: " + output.toString().trim()));
            } else if (!output.isEmpty()) {
                logger.info("ADB output: " + output.toString().trim());
            }
            return exitCode;
        } catch (IOException e) {
            logger.error("Failed to execute ADB command: " + String.join(" ", cmd) + " — " + e.getMessage());
            return -1;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("ADB command interrupted: " + String.join(" ", cmd));
            return -1;
        }
    }
}
