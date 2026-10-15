package com.tiket.postprocessing;

import com.tiket.logging.ILogger;
import com.tiket.logging.Log4JLogger;
import com.tiket.model.TestDataElement;
import com.tiket.testbase.Configs;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.stream.Collectors;

public class HtmlProcessor {

    private static final Map<String, List<TestDataElement>> testDataMap = preprocessTestData();
    private static final ILogger logger = new Log4JLogger();

    private static Map<String, List<TestDataElement>> preprocessTestData() {
        return Configs.TEST_DATA_ELEMENTS.stream()
                .collect(Collectors.groupingBy(
                        tde -> tde.module().replaceAll(" ", "").toLowerCase()
                ));
    }

    // Nested class to store Test Result information
    private record TestResultInfo(String tribeName, String moduleName, int passed, int failed, int skipped, int others, Object percentage) {
        public String getFormattedPercentage() {
            if (percentage == null) {
                return "0%";
            }
            // Convert to string, remove decimals if present, and add "%" symbol
            String percentageStr = percentage.toString();
            return percentageStr.contains(".") ? percentageStr.split("\\.")[0] + "%" : percentageStr;
        }
    }

    public static String getModuleNameContainer() {
        // STEP 1 - SAFE RETRY GET PAGE SOURCE
        String source = null;
        Exception lastEx = null;

        for (int i = 0; i < 3; i++) { // retry 3x
            try {
                source = ReportProcessor.processReportSource();
                if (source != null && !source.isEmpty()) break;
            } catch (Exception e) {
                lastEx = e;
            }
            try { Thread.sleep(1000); } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }

        // Fallback: if still no source, return empty table
        if (source == null || source.isEmpty()) {
            String msg = "[HtmlProcessor] Failed to load Extent Report source after retries.";
            System.err.println(msg);
            logger.log(msg);
            return "<tr><td align=\"center\" style=\"padding: 20px;\">" +
                    "<div style=\"border: 1px solid #ffcccc; background: #ffeeee; padding: 15px; border-radius: 5px; color: #cc0000; font-size: 14px;\">" +
                    "Table unavailable (failed to load).</div></td></tr>";
        }

        // STEP 2 - PARSE WITH JSoup SAFELY
        Document document;
        try {
            document = Jsoup.parse(source);
        } catch (Exception e) {
            String msg = "[HtmlProcessor] Error parsing HTML: " + e.getMessage();
            System.err.println(msg);
            logger.log(msg);
            return "<tr><td align=\"center\" style=\"padding: 20px;\">" +
                    "<div style=\"border: 1px solid #ffcccc; background: #ffeeee; padding: 15px; border-radius: 5px; color: #cc0000; font-size: 14px;\">" +
                    "Error reading table.</div></td></tr>";
        }

        Elements rows;
        try {
            rows = document.select("div.category-container div.card-body tbody tr");
        } catch (Exception e) {
            String msg = "[HtmlProcessor] Table structure not found.";
            System.err.println(msg);
            logger.log(msg);
            return "<tr><td align=\"center\" style=\"padding: 20px;\">" +
                    "<div style=\"border: 1px solid #fff8e1; background: #fff8e1; padding: 15px; border-radius: 5px; color: #7a5d00; font-size: 14px;\">" +
                    "Table data is currently unavailable. Detailed information can be found in the Jenkins and Extent Report links.</div></td></tr>";
        }

        // If table empty, fallback
        if (rows == null || rows.isEmpty()) {
            return "<tr><td align=\"center\" style=\"padding: 20px;\">" +
                    "<div style=\"border: 1px solid #fff8e1; background: #fff8e1; padding: 15px; border-radius: 5px; color: #7a5d00; font-size: 14px;\">" +
                    "Table data is currently unavailable. Detailed information can be found in the Jenkins and Extent Report links.</div></td></tr>";
        }

        // STEP 3 - EXISTING LOGIC (SAFE WRAPPED)
        Map<String, List<TestResultInfo>> tribeMap = new HashMap<>();
        int grandTotalPassed = 0, grandTotalFailed = 0, grandTotalSkipped = 0, grandTotalOthers = 0, grandTotalModules = 0;

        try {

            for (Element row : rows) {
                List<String> cells = row.select("td").eachText();
                if (cells.size() < 6) continue; // Ensure enough columns

                String moduleName = cells.get(0);

                // Defensive parsing
                int passed = safeInt(cells.get(1));
                int failed = safeInt(cells.get(2));
                int skipped = safeInt(cells.get(3));
                int others = safeInt(cells.get(4));

                grandTotalPassed += passed;
                grandTotalFailed += failed;
                grandTotalSkipped += skipped;
                grandTotalOthers += others;
                grandTotalModules++;

                // Module key normalization
                String lookupKey = moduleName.replaceAll(" ", "").toLowerCase();
                List<TestDataElement> testDataElements = testDataMap.get(lookupKey);
                TestDataElement testDataElement =
                        (testDataElements != null && !testDataElements.isEmpty())
                                ? testDataElements.get(0)
                                : null;

                String tribeName =
                        (testDataElement != null && !testDataElement.tribe().isEmpty())
                                ? testDataElement.tribe()
                                : "Unknown Tribe";

                TestResultInfo testResultInfo =
                        new TestResultInfo(tribeName, moduleName, passed, failed, skipped, others, cells.get(5));

                tribeMap.computeIfAbsent(tribeName, k -> new ArrayList<>()).add(testResultInfo);
            }

        } catch (Exception e) {
            String msg = "[HtmlProcessor] Error reading table rows: " + e.getMessage();
            System.err.println(msg);
            logger.log(msg);
            return "<tr><td align=\"center\" style=\"padding: 20px;\">" +
                    "<div style=\"border: 1px solid #ffcccc; background: #ffeeee; padding: 15px; border-radius: 5px; color: #cc0000; font-size: 14px;\">" +
                    "Error processing table.</div></td></tr>";
        }

        // STEP 4 - EXISTING HTML BUILDER (UNTOUCHED)
        StringBuilder tableBuilder = new StringBuilder();

        tribeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {

                    String tribe = entry.getKey();
                    List<TestResultInfo> infos = entry.getValue();

                    int tribePassed = infos.stream().mapToInt(TestResultInfo::passed).sum();
                    int tribeFailed = infos.stream().mapToInt(TestResultInfo::failed).sum();
                    int tribeSkipped = infos.stream().mapToInt(TestResultInfo::skipped).sum();
                    int tribeOthers = infos.stream().mapToInt(TestResultInfo::others).sum();

                    int totalTests = tribePassed + tribeFailed + tribeSkipped + tribeOthers;
                    int passPercentage = (totalTests > 0)
                            ? (int) Math.floor(((double) tribePassed / totalTests) * 100)
                            : 0;

                    // Tribe header
                    tableBuilder.append("<tr><td style='padding:10px;'><b style=\"font-size:15px;color:#345c72\">Tribe Name: ")
                            .append(tribe).append("</b>");

                    // Table
                    tableBuilder.append("<table width='100%' border='1' cellspacing='0' cellpadding='5' ")
                            .append("style='border-collapse:collapse;color:#333;margin-bottom:20px;'>")
                            .append("<tr style='background-color:#003355;color:white;'>")
                            .append("<th>Module Name</th><th>Passed</th><th>Failed</th><th>Skipped</th><th>Others</th><th>Passed %</th></tr>");

                    infos.forEach(info -> tableBuilder.append("<tr style='text-align:center'>")
                            .append("<td>").append(info.moduleName()).append("</td>")
                            .append("<td>").append(info.passed()).append("</td>")
                            .append("<td>").append(info.failed()).append("</td>")
                            .append("<td>").append(info.skipped()).append("</td>")
                            .append("<td>").append(info.others()).append("</td>")
                            .append("<td>").append(info.getFormattedPercentage()).append("</td>")
                            .append("</tr>")
                    );

                    // Summary row
                    tableBuilder.append("<tr style='font-weight:bold;text-align:center'>")
                            .append("<td style='background-color:#003355;color:white;'>Total</td>")
                            .append("<td>").append(tribePassed).append("</td>")
                            .append("<td>").append(tribeFailed).append("</td>")
                            .append("<td>").append(tribeSkipped).append("</td>")
                            .append("<td>").append(tribeOthers).append("</td>")
                            .append("<td>").append(passPercentage).append("%</td>")
                            .append("</tr></table></td></tr>");
                });

        // Return rendered table
        return tableBuilder.toString();
    }

    // Helper: safe integer parsing
    private static int safeInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }
}
