package com.tiket.postprocessing;

import com.tiket.model.TestDataElement;
import com.tiket.testbase.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Fallback email report processor.
 *
 * <p>Triggered when the primary {@link ReportProcessor#processReport()} flow fails
 * to render the extent report through a headless browser (e.g. ChromeDriver port
 * conflicts, missing Chrome binary, headless rendering errors). This processor
 * parses {@code extent-reports/extent-report.html} directly from disk using JSoup,
 * reconstructs the same tribe-wise summary table the regular email contains, and
 * sends an email carrying the same data without requiring Selenium or a screenshot.</p>
 */
public class FallbackEmailReportProcessor {

    private static final Logger logger = LogManager.getLogger(FallbackEmailReportProcessor.class);

    private static final int MAX_RETRY = 3;
    private static final long BASE_BACKOFF_MS = 1500L;

    private FallbackEmailReportProcessor() {
        // utility
    }

    /**
     * Generate and send the fallback email report.
     *
     * @return {@code true} if an email was sent successfully, {@code false} otherwise.
     */
    public static boolean processReport() {
        File extentFile = new File(ReportProcessor.EXTENT_REPORT_LOCATION);

        Exception lastError = null;
        for (int attempt = 1; attempt <= MAX_RETRY; attempt++) {
            try {
                logger.info("Fallback email report attempt {}/{}", attempt, MAX_RETRY);

                ParsedReport parsed = parseExtentReport(extentFile);
                String tableSummary = renderTribeTable(parsed.tribeMap);
                String resultBlockHtml = renderExecutionSummaryBlock(parsed.totalPassed,
                        parsed.totalFailed, parsed.totalSkipped, parsed.totalOthers);

                EmailProcessor.processFallbackEmail(resultBlockHtml, tableSummary);

                logger.info("Fallback email sent successfully on attempt {}", attempt);
                return true;
            } catch (Exception e) {
                lastError = e;
                logger.warn("Fallback email attempt {} failed: {}", attempt, e.getMessage(), e);
                sleepQuietly(BASE_BACKOFF_MS * attempt);
            }
        }

        logger.error("Fallback email report failed after {} attempts. Last error: {}",
                MAX_RETRY, lastError != null ? lastError.getMessage() : "unknown");
        return false;
    }

    /**
     * Parse the extent report file from disk and aggregate module rows by tribe.
     * If the file is missing or the table cannot be located, an empty result is
     * returned so the email can still be sent with placeholder content.
     */
    private static ParsedReport parseExtentReport(File extentFile) {
        ParsedReport result = new ParsedReport();

        if (extentFile == null || !extentFile.exists()) {
            logger.warn("Extent report file not found at {}", extentFile);
            return result;
        }

        Document document;
        try {
            document = Jsoup.parse(extentFile, "UTF-8");
        } catch (Exception e) {
            logger.warn("Failed to parse extent report file: {}", e.getMessage());
            return result;
        }

        Elements rows = document.select("div.category-container div.card-body tbody tr");
        if (rows == null || rows.isEmpty()) {
            logger.warn("Extent report contains no module rows");
            return result;
        }

        Map<String, List<TestDataElement>> testDataByModule = Configs.TEST_DATA_ELEMENTS.stream()
                .collect(Collectors.groupingBy(tde -> normalizeKey(tde.module())));

        for (Element row : rows) {
            List<String> cells = row.select("td").eachText();
            if (cells.size() < 6) {
                continue;
            }

            String moduleName = cells.get(0);
            int passed = safeInt(cells.get(1));
            int failed = safeInt(cells.get(2));
            int skipped = safeInt(cells.get(3));
            int others = safeInt(cells.get(4));
            String percentage = cells.get(5);

            result.totalPassed += passed;
            result.totalFailed += failed;
            result.totalSkipped += skipped;
            result.totalOthers += others;

            String tribe = resolveTribe(moduleName, testDataByModule);

            result.tribeMap
                    .computeIfAbsent(tribe, k -> new ArrayList<>())
                    .add(new TestResultInfo(tribe, moduleName, passed, failed, skipped, others, percentage));
        }

        return result;
    }

    private static String resolveTribe(String moduleName, Map<String, List<TestDataElement>> testDataByModule) {
        List<TestDataElement> matches = testDataByModule.get(normalizeKey(moduleName));
        if (matches == null || matches.isEmpty()) {
            return "Unknown Tribe";
        }
        String tribe = matches.get(0).tribe();
        return (tribe == null || tribe.isEmpty()) ? "Unknown Tribe" : tribe;
    }

    /**
     * Render the tribe-wise summary table HTML. Returns an "unavailable" placeholder
     * row when no data is available so the surrounding email template renders cleanly.
     */
    private static String renderTribeTable(Map<String, List<TestResultInfo>> tribeMap) {
        if (tribeMap.isEmpty()) {
            return "<tr><td align=\"center\" style=\"padding: 20px;\">"
                    + "<div style=\"border: 1px solid #ffcccc; background: #ffeeee; padding: 15px; "
                    + "border-radius: 5px; color: #cc0000; font-size: 14px;\">"
                    + "Tribe wise summary unavailable (extent report could not be parsed)."
                    + "</div></td></tr>";
        }

        StringBuilder sb = new StringBuilder();
        tribeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String tribe = entry.getKey();
                    List<TestResultInfo> infos = entry.getValue();

                    int tribePassed = infos.stream().mapToInt(TestResultInfo::passed).sum();
                    int tribeFailed = infos.stream().mapToInt(TestResultInfo::failed).sum();
                    int tribeSkipped = infos.stream().mapToInt(TestResultInfo::skipped).sum();
                    int tribeOthers = infos.stream().mapToInt(TestResultInfo::others).sum();

                    int total = tribePassed + tribeFailed + tribeSkipped + tribeOthers;
                    int passPercentage = total > 0
                            ? (int) Math.floor(((double) tribePassed / total) * 100)
                            : 0;

                    sb.append("<tr><td style='padding:10px;'><b style=\"font-size:15px;color:#345c72\">Tribe Name: ")
                            .append(tribe).append("</b>");

                    sb.append("<table width='100%' border='1' cellspacing='0' cellpadding='5' ")
                            .append("style='border-collapse:collapse;color:#333;margin-bottom:20px;'>")
                            .append("<tr style='background-color:#003355;color:white;'>")
                            .append("<th>Module Name</th><th>Passed</th><th>Failed</th><th>Skipped</th>")
                            .append("<th>Others</th><th>Passed %</th></tr>");

                    infos.forEach(info -> sb.append("<tr style='text-align:center'>")
                            .append("<td>").append(info.moduleName()).append("</td>")
                            .append("<td>").append(info.passed()).append("</td>")
                            .append("<td>").append(info.failed()).append("</td>")
                            .append("<td>").append(info.skipped()).append("</td>")
                            .append("<td>").append(info.others()).append("</td>")
                            .append("<td>").append(formatPercentage(info.percentage())).append("</td>")
                            .append("</tr>"));

                    sb.append("<tr style='font-weight:bold;text-align:center'>")
                            .append("<td style='background-color:#003355;color:white;'>Total</td>")
                            .append("<td>").append(tribePassed).append("</td>")
                            .append("<td>").append(tribeFailed).append("</td>")
                            .append("<td>").append(tribeSkipped).append("</td>")
                            .append("<td>").append(tribeOthers).append("</td>")
                            .append("<td>").append(passPercentage).append("%</td>")
                            .append("</tr></table></td></tr>");
                });
        return sb.toString();
    }

    /**
     * Build a styled HTML block that replaces the dashboard screenshot in the
     * fallback email. The block surfaces the same headline numbers (passed,
     * failed, skipped, totals, pass %) the screenshot would have shown.
     */
    private static String renderExecutionSummaryBlock(int passed, int failed, int skipped, int others) {
        int total = passed + failed + skipped + others;
        int passPct = total > 0 ? (int) Math.floor(((double) passed / total) * 100) : 0;

        return "<div style=\"border:1px solid #cccccc; padding:18px; border-radius:6px; background:#ffffff; "
                + "max-width:520px; margin:0 auto; text-align:left; font-size:14px; color:#333;\">"
                + "<div style=\"font-size:16px; color:#345C72; font-weight:bold; margin-bottom:10px;\">"
                + "Test Execution Summary</div>"
                + "<table width=\"100%\" cellpadding=\"6\" cellspacing=\"0\" "
                + "style=\"border-collapse:collapse; font-size:14px;\">"
                + summaryRow("Total Tests", String.valueOf(total), "#333")
                + summaryRow("Passed", String.valueOf(passed), "#0a8a3a")
                + summaryRow("Failed", String.valueOf(failed), "#c0392b")
                + summaryRow("Skipped", String.valueOf(skipped), "#b9770e")
                + summaryRow("Others", String.valueOf(others), "#555")
                + summaryRow("Pass %", passPct + "%", "#345C72")
                + "</table>"
                + "<div style=\"margin-top:12px; padding:10px; border-radius:4px; background:#fff8e1; "
                + "color:#7a5d00; font-size:12px;\">"
                + "Note: dashboard screenshot was unavailable. This summary is generated as a fallback "
                + "directly from the extent report."
                + "</div>"
                + "</div>";
    }

    private static String summaryRow(String label, String value, String color) {
        return "<tr>"
                + "<td style=\"border-bottom:1px solid #eee; padding:6px 8px;\"><b style=\"color:" + color + ";\">"
                + label + "</b></td>"
                + "<td align=\"right\" style=\"border-bottom:1px solid #eee; padding:6px 8px;\">"
                + value + "</td>"
                + "</tr>";
    }

    private static String formatPercentage(String raw) {
        if (raw == null || raw.isEmpty()) {
            return "0%";
        }
        String trimmed = raw.trim();
        if (trimmed.contains(".")) {
            trimmed = trimmed.split("\\.")[0];
        }
        return trimmed.endsWith("%") ? trimmed : trimmed + "%";
    }

    private static String normalizeKey(String value) {
        return value == null ? "" : value.replaceAll(" ", "").toLowerCase();
    }

    private static int safeInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private static void sleepQuietly(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Aggregated view of the extent report parsed from disk.
     */
    private static final class ParsedReport {
        final Map<String, List<TestResultInfo>> tribeMap = new HashMap<>();
        int totalPassed;
        int totalFailed;
        int totalSkipped;
        int totalOthers;
    }

    private record TestResultInfo(String tribe,
                                  String moduleName,
                                  int passed,
                                  int failed,
                                  int skipped,
                                  int others,
                                  String percentage) {
    }
}
