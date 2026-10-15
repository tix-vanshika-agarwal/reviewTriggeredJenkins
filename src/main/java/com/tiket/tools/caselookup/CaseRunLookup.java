package com.tiket.tools.caselookup;

import com.tiket.io.local.PropertiesReader;
import com.tiket.tools.caselookup.LookupModels.CaseMeta;
import com.tiket.tools.caselookup.LookupModels.CaseResult;
import com.tiket.tools.caselookup.LookupModels.KlovTarget;
import com.tiket.tools.caselookup.LookupModels.LookupArgs;
import com.tiket.tools.caselookup.LookupModels.LookupRow;
import com.tiket.tools.caselookup.LookupModels.Outcome;
import com.tiket.tools.caselookup.LookupModels.RunView;
import com.tiket.tools.caselookup.LookupModels.TestRun;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public final class CaseRunLookup {

    static final ZoneId ZONE = ZoneId.of("Asia/Jakarta");
    private static final DateTimeFormatter RUN_FORMAT =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.ENGLISH);
    private static final DateTimeFormatter FILE_STAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
    /** Set once the config file is found, so output lands beside the repo, not the cwd. */
    private static Path outputDir = Path.of("target", "case-lookup");

    private CaseRunLookup() {
    }

    public static void main(String[] args) {
        // Spark renders through Freemarker, whose default logger dumps a DEBUG line per
        // template lookup. Same reason the framework's mvn commands pass this flag.
        System.setProperty("org.freemarker.loggerLibrary", "none");
        try {
            Properties config = loadProperties("config.properties", true);
            Properties credentials = loadProperties("credential.properties", false);
            LookupArgs lookupArgs = LookupArgs.parse(args, config);
            TestRailCaseClient testRail = new TestRailCaseClient(config, credentials);
            testRail.setProjectIdOverride(lookupArgs.projectId());
            testRail.setProjectIdFallback(lookupArgs.configProjectId());
            List<CaseMeta> cases = collectCases(testRail, lookupArgs);
            KlovRunFinder klov = new KlovRunFinder(config);
            Map<String, List<TestRun>> runsById = klov.findRuns(cases, lookupArgs);
            List<CaseResult> results = new ArrayList<>();
            for (CaseMeta meta : cases) {
                results.add(toResult(meta, runsById.getOrDefault(meta.displayId(), List.of()), klov));
            }
            Path csv = writeCsv(results);
            Path html = LookupExtentReport.write(results, lookupArgs, testRail.warnings(), outputDir);
            printSummary(results, lookupArgs, csv, html);
        } catch (Exception e) {
            System.err.println("Case run lookup failed: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    /**
     * The Jenkins console is where people actually read this, and raw tabs are useless
     * there: a 90 character title next to a one character dash collapses the columns
     * into a smear. So the console gets a padded table, and the tab separated block
     * stays underneath for pasting into a sheet.
     */
    private static void printSummary(List<CaseResult> results, LookupArgs args, Path csv, Path html) {
        // DAYS.between, not Period.getDays(), which returns only the day-of-month part
        // and so reports a month long range as one day.
        long days = ChronoUnit.DAYS.between(args.from(), args.to()) + 1L;
        System.out.println();
        System.out.println("=".repeat(112));
        System.out.println("  TestRail case run lookup");
        System.out.println("=".repeat(112));
        System.out.println("  Range     " + args.from() + " to " + args.to()
                + "  (" + days + (days == 1 ? " day, " : " days, ") + args.env() + ")");
        System.out.println("  Cases     " + results.size());
        System.out.println("  Outcome   " + outcomeSummary(results));
        System.out.println("  CSV       " + csv.toAbsolutePath());
        System.out.println("  HTML      " + html.toAbsolutePath());
        System.out.println();
        printTable(results);
        System.out.println();
        System.out.println("-".repeat(112));
        System.out.println("  Tab separated, for pasting into a spreadsheet");
        System.out.println("-".repeat(112));
        System.out.println(String.join("\t", LookupRow.HEADER));
        for (CaseResult result : results) {
            System.out.println(String.join("\t", sanitize(result.row().cells())));
        }
    }

    private static String outcomeSummary(List<CaseResult> results) {
        Map<Outcome, Integer> counts = new EnumMap<>(Outcome.class);
        for (CaseResult result : results) {
            counts.merge(result.outcome(), 1, Integer::sum);
        }
        List<String> parts = new ArrayList<>();
        for (Map.Entry<Outcome, Integer> entry : counts.entrySet()) {
            parts.add(entry.getValue() + " " + describe(entry.getKey()));
        }
        return parts.isEmpty() ? "nothing to report" : String.join(", ", parts);
    }

    private static String describe(Outcome outcome) {
        return switch (outcome) {
            case RAN -> "ran";
            case NEVER_RAN -> "never ran in range";
            case SCENARIO_UNMATCHED -> "method ran, scenario unmatched";
            case SKIPPED -> "skipped";
            case ERROR -> "could not be looked up";
        };
    }

    /** Pads every column to its widest value so the table lines up in a plain log. */
    private static void printTable(List<CaseResult> results) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"TestRail", "Status", "Last run", "Duration", "Pass rate", "Method", "Title"});
        for (CaseResult result : results) {
            LookupRow row = result.row();
            rows.add(new String[]{
                    cell(row.testRail()),
                    statusLabel(result),
                    lastRunCell(result),
                    cell(row.duration()),
                    cell(row.passRate()),
                    clip(row.method(), 38),
                    clip(row.title(), 46)
            });
        }
        int columns = rows.get(0).length;
        int[] widths = new int[columns];
        for (String[] row : rows) {
            for (int i = 0; i < columns; i++) {
                widths[i] = Math.max(widths[i], row[i].length());
            }
        }
        for (int r = 0; r < rows.size(); r++) {
            System.out.println(padRow(rows.get(r), widths));
            if (r == 0) {
                String[] rules = new String[columns];
                for (int i = 0; i < columns; i++) {
                    rules[i] = "-".repeat(widths[i]);
                }
                System.out.println(padRow(rules, widths));
            }
        }
    }

    private static String padRow(String[] row, int[] widths) {
        StringBuilder line = new StringBuilder("  ");
        for (int i = 0; i < row.length; i++) {
            line.append(row[i]);
            if (i < row.length - 1) {
                line.append(" ".repeat(widths[i] - row[i].length() + 2));
            }
        }
        return line.toString().stripTrailing();
    }

    private static String statusLabel(CaseResult result) {
        return switch (result.outcome()) {
            case RAN -> cell(result.row().status()).toUpperCase(Locale.ROOT);
            case NEVER_RAN -> "NEVER RAN";
            case SCENARIO_UNMATCHED -> "UNMATCHED";
            case SKIPPED -> "SKIPPED";
            case ERROR -> "ERROR";
        };
    }

    /** Anything that did not run carries its reason here, which is worth the width. */
    private static String lastRunCell(CaseResult result) {
        return result.outcome() == Outcome.NEVER_RAN ? "-" : cell(result.row().lastRun());
    }

    private static String cell(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }
        return value.replace('\t', ' ').replace('\n', ' ').replace('\r', ' ').trim();
    }

    private static String clip(String value, int max) {
        String text = cell(value);
        return text.length() <= max ? text : text.substring(0, Math.max(1, max - 3)) + "...";
    }

    /** Shared so Klov diagnostics print dates in the same shape as the table. */
    static String formatRunTime(Date when) {
        return when == null ? "-" : RUN_FORMAT.format(ZonedDateTime.ofInstant(when.toInstant(), ZONE));
    }

    /**
     * Finds the properties file without depending on the working directory, so the tool
     * survives being launched from a module folder or a Jenkins step that changed
     * directory. On-disk copies win over the packaged one, because the Jenkins jobs
     * rewrite {@code credential.properties} in place before the build runs.
     */
    private static Properties loadProperties(String fileName, boolean required) {
        Path file = locate(fileName);
        if (file != null) {
            return PropertiesReader.read(file.toString());
        }
        try (InputStream packaged = CaseRunLookup.class.getResourceAsStream("/" + fileName)) {
            if (packaged != null) {
                Properties properties = new Properties();
                properties.load(packaged);
                return properties;
            }
        } catch (IOException e) {
            System.err.println("Could not read packaged " + fileName + ": " + e.getMessage());
        }
        if (required) {
            throw new IllegalStateException("Cannot find " + fileName
                    + ". Run from the repository root or pass -Dcaselookup.configDir=/path/to/resources");
        }
        return new Properties();
    }

    private static Path locate(String fileName) {
        String configured = System.getProperty("caselookup.configDir");
        if (configured != null && !configured.isBlank()) {
            Path candidate = Path.of(configured.trim()).resolve(fileName);
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }
        Path dir = Path.of("").toAbsolutePath();
        for (int depth = 0; depth < 4 && dir != null; depth++, dir = dir.getParent()) {
            Path direct = dir.resolve(fileName);
            if (Files.isRegularFile(direct)) {
                return direct;
            }
            Path resources = dir.resolve("src").resolve("main").resolve("resources").resolve(fileName);
            if (Files.isRegularFile(resources)) {
                anchorOutput(dir);
                return resources;
            }
        }
        return null;
    }

    /** Keeps target/case-lookup with the repo even when launched from a subfolder. */
    private static void anchorOutput(Path repoRoot) {
        outputDir = repoRoot.resolve("target").resolve("case-lookup");
    }

    /**
     * Turns the requested ids, and any suite plus group the caller named, into the full
     * set of TestRail cases to look up. API cases arrive here as a single scenario and
     * leave as their whole endpoint group.
     */
    private static List<CaseMeta> collectCases(TestRailCaseClient testRail, LookupArgs args) {
        List<CaseMeta> seeds = new ArrayList<>();
        for (String id : args.ids()) {
            seeds.add(testRail.withPlatformOverride(testRail.fetch(id), args.platformOverride()));
        }
        if (args.groupId() != null) {
            if (args.suiteId() == null) {
                throw new IllegalArgumentException("--group-id needs --suite-id (or caselookup.testrail.apiSuiteId)");
            }
            seeds.addAll(testRail.fetchGroup(args.suiteId(), args.groupId()));
        }
        List<CaseMeta> cases = testRail.expandApiGroups(seeds);
        for (CaseMeta meta : cases) {
            if (meta.requested() || cases.size() <= 5) {
                System.err.println("TestRail " + meta.displayId() + " title=" + meta.title()
                        + " method=" + meta.method()
                        + " vertical=" + meta.vertical() + " platform=" + meta.platformLabel()
                        + (meta.skipReason() == null ? "" : " skip=" + meta.skipReason()));
            }
        }
        long expanded = cases.size() - seeds.size();
        if (expanded > 0) {
            System.err.println("Expanded " + expanded + " sibling API scenario(s) from the same TestRail section");
        }
        return cases;
    }

    private static CaseResult toResult(CaseMeta meta, List<TestRun> runs, KlovRunFinder klov) {
        String method = blankToDash(meta.method());
        String title = blankToDash(meta.title());
        List<KlovTarget> targets = klov.targetsFor(meta.displayId());
        KlovTarget target = targets.isEmpty() ? null : targets.get(0);
        // Until we know where the runs came from, name every project searched, so a
        // "never ran" result says which projects were actually looked in.
        String project = projectNames(targets);
        List<String> unmatched = klov.unmatchedScenarios(meta.displayId());
        if (!meta.canQueryKlov()) {
            LookupRow row = new LookupRow(meta.displayId(), title, method, meta.skipReason(), "-", "-", "-", "-", "-", "-");
            return new CaseResult(meta, row, project, List.of(), Outcome.SKIPPED, List.of(), unmatched);
        }
        String klovError = klov.error(meta.displayId());
        if (klovError != null) {
            LookupRow row = new LookupRow(meta.displayId(), title, method, klovError, "-", "-", "-", "-", "-", "-");
            return new CaseResult(meta, row, project, List.of(), Outcome.ERROR, List.of(), unmatched);
        }
        List<RunView> methodViews = toViews(klov.methodRuns(meta.displayId()), target, klov);
        if (runs == null || runs.isEmpty()) {
            // The endpoint running while this scenario matched nothing is a different
            // story from the endpoint never running, and the fix differs too.
            if (!methodViews.isEmpty()) {
                LookupRow row = new LookupRow(meta.displayId(), title, method,
                        "Scenario not matched (method ran " + methodViews.get(0).when() + ")",
                        "-", "-", "-", "-", methodViews.get(0).jenkins(), methodViews.get(0).klov());
                return new CaseResult(meta, row, project, List.of(), Outcome.SCENARIO_UNMATCHED, methodViews, unmatched);
            }
            // Not 0%. A case with no runs has no pass rate, and printing a number
            // invites reading it as a failing case rather than an absent one.
            LookupRow row = new LookupRow(meta.displayId(), title, method, "Never ran in range", "-", "-", "-", "-", "-", "-");
            return new CaseResult(meta, row, project, List.of(), Outcome.NEVER_RAN, methodViews, unmatched);
        }
        List<RunView> views = toViews(runs, target, klov);
        // Now that runs exist, report the one project they came from rather than the
        // full candidate list, so the reader knows which suite executed the case.
        TestRun newest = runs.stream().max(Comparator.comparing(TestRun::start)).orElse(null);
        KlovTarget owner = newest == null ? null : klov.targetForProject(newest.projectId());
        if (owner != null) {
            project = owner.projectName();
        }
        RunView last = views.get(0);
        RunView lastPass = views.stream().filter(RunView::passed).findFirst().orElse(null);
        // A skipped run says nothing about the case, and counting it as a failure made a
        // suite that only ever skipped read as 0%, the same number "never ran" shows.
        long decided = views.stream().filter(CaseRunLookup::decisive).count();
        long passCount = views.stream().filter(RunView::passed).count();
        String passRate = decided == 0
                ? "-"
                : Math.round(100.0 * passCount / decided) + "%";
        LookupRow row = new LookupRow(
                meta.displayId(),
                title,
                method,
                last.when(),
                last.status(),
                last.duration(),
                lastPass == null ? "-" : lastPass.when(),
                passRate,
                last.jenkins(),
                last.klov()
        );
        return new CaseResult(meta, row, project, views, Outcome.RAN, methodViews, unmatched);
    }

    /** A run that actually decided something, so skip and warning are excluded. */
    static boolean decisive(RunView view) {
        String status = view.status() == null ? "" : view.status().trim().toLowerCase(Locale.ROOT);
        return status.startsWith("pass") || status.equals("success")
                || status.startsWith("fail") || status.startsWith("error");
    }

    private static List<RunView> toViews(List<TestRun> runs, KlovTarget fallback, KlovRunFinder klov) {
        if (runs == null || runs.isEmpty()) {
            return List.of();
        }
        return runs.stream()
                .sorted(Comparator.comparing(TestRun::start).reversed())
                .map(run -> new RunView(
                        formatRun(run.start().atZone(ZONE)),
                        blankToDash(run.status()),
                        formatDuration(run.durationMs()),
                        KlovRunFinder.jenkinsUrl(klov.reportName(run.reportId())),
                        klovLink(run, fallback, klov),
                        blankToDash(run.scenario()),
                        run.passed()))
                .toList();
    }

    /**
     * Candidate projects can sit on different Klov hosts, so a run's link has to be
     * built from its own project's server, not from whichever was searched first.
     */
    private static String klovLink(TestRun run, KlovTarget fallback, KlovRunFinder klov) {
        KlovTarget owner = klov.targetForProject(run.projectId());
        KlovTarget source = owner != null ? owner : fallback;
        if (source == null) {
            return "-";
        }
        // Prefer the name Klov stores. The name we asked for only matches the URL when
        // no case-insensitive or prefix fallback was involved.
        String resolved = klov.projectNameFor(run.projectId());
        return KlovRunFinder.klovUrl(source.klovServer(),
                resolved != null ? resolved : source.projectName(), run.reportId());
    }

    private static String projectNames(List<KlovTarget> targets) {
        List<String> names = new ArrayList<>();
        for (KlovTarget target : targets) {
            if (!names.contains(target.projectName())) {
                names.add(target.projectName());
            }
        }
        return names.isEmpty() ? "-" : String.join(", ", names);
    }

    private static Path writeCsv(List<CaseResult> results) throws IOException {
        Files.createDirectories(outputDir);
        String stamp = FILE_STAMP.format(ZonedDateTime.now(ZONE));
        Path latest = outputDir.resolve("case-lookup.csv");
        Path archived = outputDir.resolve("case-lookup-" + stamp + ".csv");
        StringBuilder body = new StringBuilder();
        // The file is named .csv, so it has to be comma separated. It used to hold tabs,
        // which every spreadsheet opened as a single column. The tab separated copy for
        // pasting lives in the console output instead.
        body.append(csvLine(LookupRow.HEADER)).append("\r\n");
        for (CaseResult result : results) {
            body.append(csvLine(sanitize(result.row().cells()))).append("\r\n");
        }
        byte[] bytes = body.toString().getBytes(StandardCharsets.UTF_8);
        Files.write(latest, bytes);
        Files.write(archived, bytes);
        return latest;
    }

    /**
     * Every field is quoted rather than only the ones that need it. Titles carry commas
     * and quotes freely, and unconditional quoting is both valid RFC 4180 and one fewer
     * rule to get wrong.
     */
    private static String csvLine(String[] cells) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < cells.length; i++) {
            if (i > 0) {
                line.append(',');
            }
            String cell = cells[i] == null ? "-" : cells[i];
            line.append('"').append(cell.replace("\"", "\"\"")).append('"');
        }
        return line.toString();
    }

    private static String[] sanitize(String[] cells) {
        String[] out = new String[cells.length];
        for (int i = 0; i < cells.length; i++) {
            String cell = cells[i] == null
                    ? "-"
                    : cells[i].replace('\t', ' ').replace('\n', ' ').replace('\r', ' ');
            out[i] = neutralizeFormula(cell);
        }
        return out;
    }

    /**
     * TestRail titles and Klov scenario names land straight in a spreadsheet, where a
     * leading =, + or @ is read as a formula. The apostrophe that blocks it is visible
     * when a sheet imports a file rather than when a cell is typed, so it is only added
     * where a formula is actually possible: a leading - is left alone for "- Baggage"
     * and "-5", and neutralised for anything else, including arithmetic-looking payloads
     * such as "-1+1" or "-1/HYPERLINK(...)" whose second character happens to be a digit.
     */
    private static String neutralizeFormula(String cell) {
        if (cell.length() < 2 || "-".equals(cell)) {
            return cell;
        }
        char first = cell.charAt(0);
        if (first == '=' || first == '+' || first == '@') {
            return "'" + cell;
        }
        if (first != '-') {
            return cell;
        }
        if (cell.charAt(1) == ' ') {
            return cell;
        }
        // Only a plain negative number is safe to leave unprefixed.
        boolean plainNumber = cell.substring(1).matches("\\d+(\\.\\d+)?");
        return plainNumber ? cell : "'" + cell;
    }

    private static String formatRun(ZonedDateTime time) {
        return RUN_FORMAT.format(time);
    }

    private static String formatDuration(long durationMs) {
        if (durationMs <= 0) {
            return "-";
        }
        long minutes = durationMs / 60_000;
        long seconds = (durationMs % 60_000) / 1000;
        if (minutes > 0) {
            return minutes + "m " + seconds + "s";
        }
        if (seconds > 0) {
            return seconds + "s";
        }
        return durationMs + "ms";
    }

    private static String blankToDash(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}
