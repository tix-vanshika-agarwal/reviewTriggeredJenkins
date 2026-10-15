package com.tiket.tools.caselookup;

import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class LookupModels {

    private LookupModels() {
    }

    record LookupArgs(
            List<String> ids,
            int lastN,
            String env,
            LocalDate from,
            LocalDate to,
            String platformOverride,
            Integer suiteId,
            Integer groupId,
            Integer projectId,
            Integer configProjectId,
            String klovProject
    ) {
        /** {@code index.php?/cases/view/400092} */
        private static final Pattern CASE_URL = Pattern.compile("/cases/view/(\\d+)");
        /** {@code index.php?/suites/view/4710&...} */
        private static final Pattern SUITE_URL = Pattern.compile("/suites/view/(\\d+)");
        /** Only the real group_id, never the neighbouring group_by/group_order params. */
        private static final Pattern GROUP_PARAM = Pattern.compile("[?&]group_id=(\\d+)");
        private static final Pattern SECTION_PARAM = Pattern.compile("[?&]section_id=(\\d+)");

        /**
         * Klov is queried one day at a time, so the range is also the number of queries
         * per project. A month is the practical ceiling before the job gets slow and
         * the load on Klov stops being negligible.
         */
        static final int MAX_DAYS = 31;

        /** The environments Klov is configured for. Anything else has no host to query. */
        private static final List<String> ENVS = List.of("gk", "preprod", "prod");

        /**
         * Accepts both styles. The test jobs pass everything as {@code -Dname=value}, so
         * this tool reads the same way ({@code -Dids=C1,C2 -Denv=gk -Dlastn=7}), and the
         * {@code --flag value} form still works for running it by hand. A flag always
         * beats the matching system property.
         */
        static LookupArgs parse(String[] argv, Properties config) {
            List<String> ids = new ArrayList<>();
            Integer lastN = null;
            String env = null;
            LocalDate from = null;
            LocalDate to = null;
            String platform = null;
            String klovProject = null;
            // Slots 0/1 let a pasted suite URL contribute the suite and group ids.
            Integer[] fromUrl = new Integer[2];
            Integer suiteId = null;
            Integer groupId = null;
            Integer projectId = null;
            for (int i = 0; i < argv.length; i++) {
                String arg = argv[i];
                if (!arg.startsWith("--")) {
                    // A bare token is an id. Without this, "--ids C1, C2" would split on
                    // the space and reject "C2" as an unknown flag.
                    collectIds(arg, ids, fromUrl);
                    continue;
                }
                String value;
                if (arg.contains("=")) {
                    int eq = arg.indexOf('=');
                    value = arg.substring(eq + 1);
                    arg = arg.substring(0, eq);
                } else if (i + 1 < argv.length && !argv[i + 1].startsWith("--")) {
                    value = argv[++i];
                } else {
                    throw new IllegalArgumentException("Missing value for " + arg);
                }
                switch (arg) {
                    case "--ids" -> collectIds(value, ids, fromUrl);
                    case "--last-n" -> lastN = number(value, "--last-n");
                    case "--env" -> env = environment(value, "--env");
                    case "--from" -> from = date(value, "--from");
                    case "--to" -> to = date(value, "--to");
                    case "--platform" -> platform = value.trim();
                    case "--suite-id" -> suiteId = positive(value, "--suite-id");
                    case "--group-id", "--section-id" -> groupId = positive(value, "--group-id");
                    case "--project-id" -> projectId = positive(value, "--project-id");
                    case "--klov-project" -> klovProject = value.trim();
                    default -> throw new IllegalArgumentException("Unknown argument: " + arg);
                }
            }
            if (ids.isEmpty()) {
                collectIds(property("ids"), ids, fromUrl);
            }
            if (lastN == null) {
                String raw = property("lastn");
                lastN = raw == null ? 7 : number(raw, "-Dlastn");
            }
            if (env == null) {
                String raw = property("env");
                env = raw == null ? "preprod" : environment(raw, "-Denv");
            }
            if (from == null) {
                from = date(property("from"), "from");
            }
            if (to == null) {
                to = date(property("to"), "to");
            }
            if (platform == null) {
                platform = property("platform");
            }
            if (klovProject == null) {
                klovProject = property("klovproject");
            }
            if (suiteId == null) {
                suiteId = propertyInt("suiteid");
            }
            if (groupId == null) {
                groupId = propertyInt("groupid", "sectionid");
            }
            if (projectId == null) {
                projectId = propertyInt("projectid");
            }
            if (suiteId == null) {
                suiteId = fromUrl[0];
            }
            if (groupId == null) {
                groupId = fromUrl[1];
            }
            if (suiteId == null) {
                suiteId = configInt(config, "caselookup.testrail.apiSuiteId", "TESTRAIL_API_SUITE_ID");
            }
            Integer configProjectId = configInt(config, "caselookup.testrail.projectId", "TESTRAIL_PROJECT_ID");
            if (ids.isEmpty() && groupId == null) {
                throw new IllegalArgumentException(
                        "Pass TestRail ids with -Dids=C123,C456 (a case URL works too), "
                                + "or an API group with -Dsuiteid=4710 -Dgroupid=211326");
            }
            if (lastN > MAX_DAYS || lastN < 1) {
                // Silently clamping made "-Dlastn=20" report a 14 day window with no
                // hint that it had been changed.
                System.err.println("Clamping " + lastN + " days to the allowed 1 to " + MAX_DAYS);
                lastN = Math.min(Math.max(lastN, 1), MAX_DAYS);
            }
            if (from != null && to != null && from.isAfter(to)) {
                throw new IllegalArgumentException("--from must be on or before --to");
            }
            if (from == null && to == null) {
                to = LocalDate.now(CaseRunLookup.ZONE);
                from = to.minusDays(lastN - 1L);
            } else if (from == null) {
                from = to.minusDays(lastN - 1L);
            } else if (to == null) {
                to = LocalDate.now(CaseRunLookup.ZONE);
            }
            if (from.isAfter(to)) {
                // Only reachable when a lone --from/-Dfrom landed after today (or a lone
                // --to before the computed lastN start): the equal-both check above
                // already caught an explicit inverted pair.
                throw new IllegalArgumentException("--from must be on or before --to, got " + from + " to " + to);
            }
            long days = ChronoUnit.DAYS.between(from, to) + 1;
            if (days > MAX_DAYS) {
                throw new IllegalArgumentException("Date range cannot exceed " + MAX_DAYS + " days, got " + days);
            }
            return new LookupArgs(List.copyOf(ids), lastN, env, from, to, platform,
                    suiteId, groupId, projectId, configProjectId, klovProject);
        }

        /** True when the caller named a group but no individual case. */
        boolean groupOnly() {
            return ids.isEmpty() && groupId != null;
        }

        /**
         * Jenkins leaves an unset parameter as an empty string, so blank is treated the
         * same as absent rather than failing the run.
         */
        private static String property(String name) {
            String value = System.getProperty(name);
            return value == null || value.isBlank() ? null : value.trim();
        }

        private static Integer propertyInt(String... names) {
            for (String name : names) {
                String raw = property(name);
                if (raw != null) {
                    return positive(raw, "-D" + name);
                }
            }
            return null;
        }

        private static LocalDate date(String raw, String name) {
            if (raw == null) {
                return null;
            }
            try {
                return LocalDate.parse(raw);
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Expected yyyy-MM-dd for " + name + ", got: " + raw);
            }
        }

        private static void collectIds(String value, List<String> ids, Integer[] fromUrl) {
            if (value == null || value.isBlank()) {
                return;
            }
            for (String token : value.split("[,\\s]+")) {
                if (token.isBlank()) {
                    continue;
                }
                addToken(token.trim(), ids, fromUrl);
            }
        }

        private static void addToken(String token, List<String> ids, Integer[] fromUrl) {
            if (token.contains("/")) {
                boolean understood = false;
                Matcher caseMatch = CASE_URL.matcher(token);
                if (caseMatch.find()) {
                    ids.add("C" + caseMatch.group(1));
                    understood = true;
                }
                Matcher suiteMatch = SUITE_URL.matcher(token);
                if (suiteMatch.find()) {
                    fromUrl[0] = Integer.valueOf(suiteMatch.group(1));
                    understood = true;
                }
                // TestRail writes the section as group_id when the suite view is grouped
                // by section, and as section_id elsewhere. Both mean the same thing here.
                Matcher groupMatch = GROUP_PARAM.matcher(token);
                if (groupMatch.find()) {
                    fromUrl[1] = Integer.valueOf(groupMatch.group(1));
                    understood = true;
                }
                Matcher sectionMatch = SECTION_PARAM.matcher(token);
                if (sectionMatch.find()) {
                    fromUrl[1] = Integer.valueOf(sectionMatch.group(1));
                    understood = true;
                }
                if (!understood) {
                    throw new IllegalArgumentException("Cannot read a case or suite id from URL: " + token);
                }
                return;
            }
            String digits = token.toUpperCase(Locale.ROOT).startsWith("C") ? token.substring(1) : token;
            if (!digits.matches("\\d+")) {
                throw new IllegalArgumentException("Not a TestRail id or URL: " + token);
            }
            ids.add("C" + digits);
        }

        private static Integer positive(String value, String flag) {
            int parsed = number(value, flag);
            if (parsed <= 0) {
                throw new IllegalArgumentException(flag + " must be a positive number, got: " + value);
            }
            return parsed;
        }

        /**
         * Jenkins shows an uncaught {@code NumberFormatException} as a stack trace
         * reading only {@code For input string: "7 days"}, which says nothing about
         * which parameter was wrong.
         */
        private static int number(String raw, String name) {
            try {
                return Integer.parseInt(raw.trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Expected a whole number for " + name + ", got: " + raw);
            }
        }

        /**
         * Checked here rather than where the Klov host is resolved. Left to that point a
         * typo costs a TestRail round trip per id and then reports itself as a per-case
         * error, hiding the single real cause behind a table of failures.
         */
        private static String environment(String raw, String name) {
            String value = raw.trim().toLowerCase(Locale.ROOT);
            if (!ENVS.contains(value)) {
                throw new IllegalArgumentException("Expected one of " + ENVS + " for " + name + ", got: " + raw);
            }
            return value;
        }

        /** Jenkins sets these as -D or env vars; config.properties is the checked-in default. */
        private static Integer configInt(Properties config, String property, String envVar) {
            String raw = System.getProperty(property);
            if (raw == null || raw.isBlank()) {
                raw = System.getenv(envVar);
            }
            if ((raw == null || raw.isBlank()) && config != null) {
                raw = config.getProperty(property);
            }
            if (raw == null || raw.isBlank()) {
                return null;
            }
            try {
                return Integer.valueOf(raw.trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Not a number for " + property + ": " + raw);
            }
        }
    }

    record CaseMeta(
            String displayId,
            int numericId,
            String title,
            String method,
            String vertical,
            String platformLabel,
            String platformCode,
            boolean panel,
            boolean api,
            int suiteId,
            int sectionId,
            boolean requested,
            String skipReason
    ) {
        boolean canQueryKlov() {
            return skipReason == null || skipReason.isBlank();
        }

        /** A sibling scenario pulled in by group expansion rather than named by the caller. */
        CaseMeta asSibling() {
            return requested ? withRequested(false) : this;
        }

        CaseMeta withRequested(boolean value) {
            return new CaseMeta(displayId, numericId, title, method, vertical, platformLabel,
                    platformCode, panel, api, suiteId, sectionId, value, skipReason);
        }

        CaseMeta withSkipReason(String reason) {
            return new CaseMeta(displayId, numericId, title, method, vertical, platformLabel,
                    platformCode, panel, api, suiteId, sectionId, requested, reason);
        }

        boolean expandable() {
            return api && suiteId > 0 && sectionId > 0;
        }
    }

    record KlovTarget(String mongoUri, String klovServer, String projectName) {
    }

    record TestRun(
            Instant start,
            String status,
            long durationMs,
            ObjectId reportId,
            ObjectId testId,
            ObjectId projectId,
            String scenario
    ) {
        boolean passed() {
            if (status == null) {
                return false;
            }
            String s = status.trim().toLowerCase(Locale.ROOT);
            return s.startsWith("pass") || s.equals("success");
        }
    }

    /** One Klov run with every value already formatted for display. */
    record RunView(
            String when,
            String status,
            String duration,
            String jenkins,
            String klov,
            String scenario,
            boolean passed
    ) {
    }

    /** How a single lookup ended, so the report never has to infer it from a pass rate. */
    enum Outcome {
        RAN,
        NEVER_RAN,
        /** The endpoint's method ran, but no Klov scenario matched this case's title. */
        SCENARIO_UNMATCHED,
        /** TestRail did not give us enough to query Klov at all. */
        SKIPPED,
        ERROR
    }

    /** Everything the CSV and the Extent report need for a single TestRail id. */
    record CaseResult(
            CaseMeta meta,
            LookupRow row,
            String klovProject,
            List<RunView> runs,
            Outcome outcome,
            List<RunView> methodRuns,
            List<String> unmatchedScenarios
    ) {
        /**
         * API scenarios that share a method are reported together under this key. Keyed
         * by the TestRail suite/section rather than the resolved Klov project, because
         * that project can differ between sibling scenarios (one ran and resolved to its
         * actual project, another never ran and still lists every candidate), which would
         * otherwise split one endpoint's scenarios across multiple report entries.
         */
        String groupKey() {
            return meta.suiteId() + "/" + meta.sectionId();
        }

        boolean grouped() {
            return meta.api();
        }
    }

    record LookupRow(
            String testRail,
            String title,
            String method,
            String lastRun,
            String status,
            String duration,
            String lastPass,
            String passRate,
            String jenkins,
            String klov
    ) {
        static final String[] HEADER = {
                "TestRail", "Title", "Method", "Last run", "Status", "Duration",
                "Last pass", "Pass rate (range)", "Jenkins", "Klov"
        };

        String[] cells() {
            return new String[]{testRail, title, method, lastRun, status, duration, lastPass, passRate, jenkins, klov};
        }
    }
}
