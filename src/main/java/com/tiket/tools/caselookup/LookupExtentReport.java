package com.tiket.tools.caselookup;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.tiket.tools.caselookup.LookupModels.CaseMeta;
import com.tiket.tools.caselookup.LookupModels.CaseResult;
import com.tiket.tools.caselookup.LookupModels.LookupArgs;
import com.tiket.tools.caselookup.LookupModels.LookupRow;
import com.tiket.tools.caselookup.LookupModels.Outcome;
import com.tiket.tools.caselookup.LookupModels.RunView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Renders lookup results as a Spark (Extent) report so the output looks like the
 * execution reports the team already reads: one "test" per TestRail id, passing
 * when its last automation run passed.
 *
 * <p>API cases are the exception. There, one automation method covers every scenario in
 * a TestRail section, so those cases are rendered as a single method entry with one
 * child node per scenario.
 */
final class LookupExtentReport {

    private static final String NONE = "-";

    private LookupExtentReport() {
    }

    static Path write(List<CaseResult> results, LookupArgs args, List<String> warnings, Path dir) throws IOException {
        Files.createDirectories(dir);
        Path html = dir.resolve("case-lookup.html");

        ExtentSparkReporter spark = new ExtentSparkReporter(html.toString());
        spark.config().setEncoding("utf-8");
        spark.config().setDocumentTitle("TestRail Case Run Lookup");
        // The range belongs in the header, which is the one thing visible from every
        // tab. Without it a screenshot of this report cannot be interpreted later.
        spark.config().setReportName("Last automation run per TestRail case | "
                + args.from() + " to " + args.to() + " | " + args.env().toUpperCase(Locale.ROOT));
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");
        spark.config().setTimelineEnabled(false);
        // Offline so the report renders without CDN access, matching ExtentReportManager.
        spark.config().setOfflineMode(true);

        Map<String, List<CaseResult>> groups = groupApiScenarios(results);

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Date range", args.from() + " to " + args.to());
        extent.setSystemInfo("Environment", args.env().toUpperCase(Locale.ROOT));
        extent.setSystemInfo("Time zone", CaseRunLookup.ZONE.getId());
        extent.setSystemInfo("TestRail cases", String.valueOf(results.size()));
        extent.setSystemInfo("Ran in range", String.valueOf(
                results.stream().filter(r -> r.outcome() == Outcome.RAN).count()));
        if (!groups.isEmpty()) {
            extent.setSystemInfo("API endpoints", String.valueOf(groups.size()));
        }
        extent.setSystemInfo("Source", "Klov MongoDB, root tests only");
        for (int i = 0; i < warnings.size(); i++) {
            extent.setSystemInfo("Warning " + (i + 1), esc(warnings.get(i)));
        }

        Set<String> rendered = new LinkedHashSet<>();
        for (CaseResult result : results) {
            if (result.grouped()) {
                if (rendered.add(result.groupKey())) {
                    addApiGroup(extent, groups.get(result.groupKey()), args);
                }
                continue;
            }
            addCase(extent.createTest(title(result)), result, args);
        }
        extent.flush();
        return html;
    }

    private static Map<String, List<CaseResult>> groupApiScenarios(List<CaseResult> results) {
        Map<String, List<CaseResult>> groups = new LinkedHashMap<>();
        for (CaseResult result : results) {
            if (result.grouped()) {
                groups.computeIfAbsent(result.groupKey(), key -> new ArrayList<>()).add(result);
            }
        }
        return groups;
    }

    /** One entry per API endpoint, with every scenario of that endpoint beneath it. */
    private static void addApiGroup(ExtentReports extent, List<CaseResult> scenarios, LookupArgs args) {
        CaseResult first = scenarios.get(0);
        CaseMeta meta = first.meta();
        ExtentTest parent = extent.createTest(
                esc(meta.method()) + " - " + scenarios.size() + " scenario" + (scenarios.size() == 1 ? "" : "s"));
        if (!meta.vertical().isBlank()) {
            parent.assignCategory(tag(meta.vertical()));
        }
        parent.assignDevice("API");

        List<RunView> methodRuns = first.methodRuns();
        parent.info(MarkupHelper.createTable(escape(groupSummary(scenarios, args))));
        if (!methodRuns.isEmpty()) {
            parent.info(MarkupHelper.createTable(escape(builds(methodRuns))));
        } else {
            parent.info(MarkupHelper.createLabel("This endpoint did not run in the selected range", ExtentColor.GREY));
        }
        List<String> unmatched = first.unmatchedScenarios();
        if (!unmatched.isEmpty()) {
            // Klov names scenarios from the test-data sheet, so drift from the TestRail
            // title shows up here rather than silently disappearing.
            parent.warning(MarkupHelper.createLabel(
                    "Klov scenarios with no matching TestRail title: " + unmatched.size(), ExtentColor.AMBER));
            parent.info(MarkupHelper.createTable(escape(unmatchedTable(unmatched))));
        }

        for (CaseResult scenario : scenarios) {
            addCase(parent.createNode(title(scenario)), scenario, args);
        }
    }

    private static String title(CaseResult result) {
        String base = esc(result.row().testRail()) + " - " + esc(result.row().title());
        return result.meta().requested() ? base + " [requested]" : base;
    }

    private static void addCase(ExtentTest test, CaseResult result, LookupArgs args) {
        CaseMeta meta = result.meta();
        LookupRow row = result.row();
        if (!meta.vertical().isBlank()) {
            test.assignCategory(tag(meta.vertical()));
        }
        if (meta.platformLabel() != null && !meta.platformLabel().isBlank()) {
            test.assignDevice(tag(meta.platformLabel()));
        }

        switch (result.outcome()) {
            case SKIPPED -> test.warning(MarkupHelper.createLabel("Not looked up: " + esc(meta.skipReason()), ExtentColor.AMBER));
            case ERROR -> test.warning(MarkupHelper.createLabel(esc(row.lastRun()), ExtentColor.AMBER));
            case NEVER_RAN -> test.skip(MarkupHelper.createLabel("Never ran in range", ExtentColor.GREY));
            case SCENARIO_UNMATCHED -> test.warning(MarkupHelper.createLabel(esc(row.lastRun()), ExtentColor.AMBER));
            case RAN -> {
                RunView last = result.runs().get(0);
                String label = "Last run " + last.status().toUpperCase(Locale.ROOT) + " on " + last.when();
                if (last.passed()) {
                    test.pass(MarkupHelper.createLabel(esc(label), ExtentColor.GREEN));
                } else {
                    test.fail(MarkupHelper.createLabel(esc(label), ExtentColor.RED));
                }
            }
            default -> throw new IllegalStateException("Unhandled outcome: " + result.outcome());
        }

        test.info(MarkupHelper.createTable(escape(summary(result, args))));
        if (result.outcome() != Outcome.RAN) {
            return;
        }
        test.info(MarkupHelper.createTable(escape(history(result.runs()))));
        RunView last = result.runs().get(0);
        if (!NONE.equals(last.jenkins())) {
            test.info(link("Jenkins build", last.jenkins()));
        }
        if (!NONE.equals(last.klov())) {
            test.info(link("Klov report", last.klov()));
        }
    }

    private static String[][] groupSummary(List<CaseResult> scenarios, LookupArgs args) {
        CaseResult first = scenarios.get(0);
        CaseMeta meta = first.meta();
        long ran = scenarios.stream().filter(r -> r.outcome() == Outcome.RAN).count();
        long passed = scenarios.stream()
                .filter(r -> r.outcome() == Outcome.RAN && r.runs().get(0).passed())
                .count();
        List<RunView> methodRuns = first.methodRuns();
        return new String[][]{
                {"Method", meta.method()},
                {"Vertical", dash(meta.vertical())},
                {"Platform", dash(meta.platformLabel())},
                {"Environment", args.env().toUpperCase(Locale.ROOT)},
                {"Klov project", dash(first.klovProject())},
                {"TestRail suite / section", meta.suiteId() + " / " + meta.sectionId()},
                {"Scenarios in section", String.valueOf(scenarios.size())},
                {"Scenarios with runs", ran + " of " + scenarios.size()},
                {"Passing on last run", String.valueOf(passed)},
                {"Endpoint last ran", methodRuns.isEmpty() ? NONE : methodRuns.get(0).when()},
                {"Scenario executions in range", String.valueOf(methodRuns.size())}
        };
    }

    /**
     * Collapses the method's scenario executions into the builds they came from. Failed
     * only counts runs that {@link CaseRunLookup#decisive} would count, so a skipped or
     * warning run does not read as a failure the way it doesn't in the pass rate either.
     */
    private static String[][] builds(List<RunView> methodRuns) {
        Map<String, String[]> byBuild = new LinkedHashMap<>();
        Map<String, int[]> counts = new LinkedHashMap<>();
        for (RunView run : methodRuns) {
            String key = NONE.equals(run.klov()) ? run.jenkins() + "|" + run.when() : run.klov();
            byBuild.putIfAbsent(key, new String[]{run.when(), run.jenkins(), run.klov()});
            int[] tally = counts.computeIfAbsent(key, ignored -> new int[3]);
            tally[0]++;
            if (run.passed()) {
                tally[1]++;
            } else if (CaseRunLookup.decisive(run)) {
                tally[2]++;
            }
        }
        String[][] table = new String[byBuild.size() + 1][];
        table[0] = new String[]{"Build started", "Scenarios", "Passed", "Failed", "Skipped", "Jenkins", "Klov"};
        int index = 1;
        for (Map.Entry<String, String[]> entry : byBuild.entrySet()) {
            int[] tally = counts.get(entry.getKey());
            String[] build = entry.getValue();
            table[index++] = new String[]{
                    build[0],
                    String.valueOf(tally[0]),
                    String.valueOf(tally[1]),
                    String.valueOf(tally[2]),
                    String.valueOf(tally[0] - tally[1] - tally[2]),
                    build[1],
                    build[2]
            };
        }
        return table;
    }

    private static String[][] unmatchedTable(List<String> unmatched) {
        String[][] table = new String[unmatched.size() + 1][];
        table[0] = new String[]{"Klov scenario name"};
        for (int i = 0; i < unmatched.size(); i++) {
            table[i + 1] = new String[]{unmatched.get(i)};
        }
        return table;
    }

    private static String[][] summary(CaseResult result, LookupArgs args) {
        CaseMeta meta = result.meta();
        LookupRow row = result.row();
        return new String[][]{
                {"TestRail", row.testRail()},
                {"Method", row.method()},
                {"Vertical", dash(meta.vertical())},
                {"Platform", dash(meta.platformLabel())},
                {"Environment", args.env().toUpperCase(Locale.ROOT)},
                // "Never ran" is meaningless without the window it was searched in, and
                // the dashboard's copy of this is on a tab nobody opens.
                {"Date range searched", args.from() + " to " + args.to()},
                {"Klov project", dash(result.klovProject())},
                {"Last run", row.lastRun()},
                {"Status", row.status()},
                {"Duration", row.duration()},
                {"Last pass", row.lastPass()},
                {"Pass rate (range)", row.passRate()},
                {"Runs in range", String.valueOf(result.runs().size())}
        };
    }

    private static String[][] history(List<RunView> runs) {
        boolean withScenario = runs.stream().anyMatch(run -> !NONE.equals(run.scenario()));
        String[][] table = new String[runs.size() + 1][];
        table[0] = withScenario
                ? new String[]{"When", "Status", "Duration", "Klov scenario"}
                : new String[]{"When", "Status", "Duration"};
        for (int i = 0; i < runs.size(); i++) {
            RunView run = runs.get(i);
            table[i + 1] = withScenario
                    ? new String[]{run.when(), run.status(), run.duration(), run.scenario()}
                    : new String[]{run.when(), run.status(), run.duration()};
        }
        return table;
    }

    /**
     * Klov scenario names legitimately contain markup (the API framework stores an
     * {@code <h6>} block in the test name), and Extent writes table cells straight into
     * the page, so every cell is escaped before it gets there.
     */
    private static String[][] escape(String[][] table) {
        String[][] escaped = new String[table.length][];
        for (int row = 0; row < table.length; row++) {
            escaped[row] = new String[table[row].length];
            for (int col = 0; col < table[row].length; col++) {
                escaped[row][col] = esc(table[row][col]);
            }
        }
        return escaped;
    }

    private static String esc(String value) {
        if (value == null || value.isBlank()) {
            return NONE;
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    /**
     * Extent writes categories and devices into the page without going through the table
     * escaping, so markup characters are dropped rather than encoded. These values come
     * from fixed TestRail dropdowns, so nothing legitimate is lost.
     */
    private static String tag(String value) {
        return value.replaceAll("[<>\"'&]", "").trim();
    }

    /**
     * Jenkins links are rebuilt from the Klov report name, which is data rather than a
     * validated URL. Only http and https become clickable; anything else is shown as
     * plain text so a stored "javascript:" value cannot run from the report.
     */
    private static String link(String label, String url) {
        String lower = url.toLowerCase(Locale.ROOT);
        if (!lower.startsWith("http://") && !lower.startsWith("https://")) {
            return esc(label) + ": " + esc(url);
        }
        return "<a href=\"" + esc(url) + "\" target=\"_blank\" rel=\"noopener noreferrer\">" + esc(label) + "</a>";
    }

    private static String dash(String value) {
        return value == null || value.isBlank() ? NONE : value;
    }
}
