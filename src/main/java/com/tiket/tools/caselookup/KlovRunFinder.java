package com.tiket.tools.caselookup;

import com.mongodb.MongoExecutionTimeoutException;
import com.mongodb.MongoSocketException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tiket.tools.caselookup.LookupModels.CaseMeta;
import com.tiket.tools.caselookup.LookupModels.KlovTarget;
import com.tiket.tools.caselookup.LookupModels.LookupArgs;
import com.tiket.tools.caselookup.LookupModels.TestRun;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Projections.include;

final class KlovRunFinder {

    private static final long DAY_PAUSE_MS = 100L;

    /**
     * The API Klov holds far more documents than the app one, and its query planner
     * needs more than a couple of seconds to settle on a plan for a day slice. Three
     * seconds made every API lookup fail as if the server were down. Still bounded, so
     * a sick Klov cannot hang the job.
     */
    private static final long QUERY_TIMEOUT_MS = timeoutMs();

    private static long timeoutMs() {
        String configured = System.getProperty("caselookup.queryTimeoutMs");
        if (configured != null && !configured.isBlank()) {
            try {
                return Math.max(1000L, Long.parseLong(configured.trim()));
            } catch (NumberFormatException e) {
                System.err.println("Ignoring bad caselookup.queryTimeoutMs: " + configured);
            }
        }
        return 20_000L;
    }

    /**
     * Shared suite whose Klov project holds runs for cases tagged with some other
     * vertical in TestRail. A case can execute here instead of, or as well as, its own
     * vertical's suite.
     */
    private static final String SHARED_SUITE = "CriticalPathTests";

    /** How many of the method names that did run to name when nothing matched. */
    private static final int METHOD_SAMPLE = 15;

    private final Properties config;
    private final Map<ObjectId, String> reportNameById = new HashMap<>();
    private final Map<String, List<KlovTarget>> targetsByDisplayId = new LinkedHashMap<>();
    private final Map<ObjectId, KlovTarget> targetByProjectId = new HashMap<>();
    /**
     * The name Klov actually stores, which differs from the name we asked for whenever
     * the case-insensitive or prefix fallback matched. Links are built from this, since
     * the name we guessed would not resolve.
     */
    private final Map<ObjectId, String> projectNameById = new HashMap<>();
    /** One full project listing per Klov host, reused by every group on that host. */
    private final Map<String, List<Document>> projectListByUri = new HashMap<>();
    private final Map<String, String> errorByDisplayId = new HashMap<>();
    /** Cases whose primary/derived project was missing but an alias is configured, so the
     *  "no project" verdict waits until every one of the case's targets has been tried. */
    private final Map<String, String> pendingMissingPrimary = new LinkedHashMap<>();
    /** Cases for which at least one of their candidate targets resolved to a real project. */
    private final Set<String> resolvedProjectDisplayIds = new HashSet<>();
    private final Map<String, List<String>> methodKeysByDisplayId = new HashMap<>();
    private final Map<String, List<TestRun>> runsByMethodKey = new LinkedHashMap<>();
    private final Map<String, Set<String>> unmatchedByMethodKey = new LinkedHashMap<>();

    KlovRunFinder(Properties config) {
        this.config = config;
    }

    String reportName(ObjectId reportId) {
        return reportId == null ? null : reportNameById.get(reportId);
    }

    /** Every Klov project this case was searched in, primary first. */
    List<KlovTarget> targetsFor(String displayId) {
        return targetsByDisplayId.getOrDefault(displayId, List.of());
    }

    KlovTarget targetFor(String displayId) {
        List<KlovTarget> targets = targetsFor(displayId);
        return targets.isEmpty() ? null : targets.get(0);
    }

    /**
     * Which target a run came from. Candidate projects can live on different Klov
     * hosts, so a run's report link has to be built from its own project's server
     * rather than from whichever target happened to be searched first.
     */
    KlovTarget targetForProject(ObjectId projectId) {
        return projectId == null ? null : targetByProjectId.get(projectId);
    }

    /** The project name as Klov stores it, which is what a launch URL needs. */
    String projectNameFor(ObjectId projectId) {
        return projectId == null ? null : projectNameById.get(projectId);
    }

    /**
     * Why a lookup returned nothing, or {@code null} when Klov answered normally.
     * Without this an unreachable Mongo is indistinguishable from a case that
     * genuinely never ran.
     */
    String error(String displayId) {
        return errorByDisplayId.get(displayId);
    }

    /**
     * Every run of the method behind this case, including runs attributed to a sibling
     * scenario. Lets the report say "the endpoint ran, this scenario did not match"
     * instead of the misleading "never ran".
     */
    List<TestRun> methodRuns(String displayId) {
        List<TestRun> all = new ArrayList<>();
        for (String key : methodKeysByDisplayId.getOrDefault(displayId, List.of())) {
            all.addAll(runsByMethodKey.getOrDefault(key, List.of()));
        }
        return all;
    }

    /** Scenario names Klov reported that no TestRail title in the group claimed. */
    List<String> unmatchedScenarios(String displayId) {
        Set<String> all = new LinkedHashSet<>();
        for (String key : methodKeysByDisplayId.getOrDefault(displayId, List.of())) {
            all.addAll(unmatchedByMethodKey.getOrDefault(key, Set.of()));
        }
        return List.copyOf(all);
    }

    Map<String, List<TestRun>> findRuns(List<CaseMeta> cases, LookupArgs args) {
        Map<String, List<TestRun>> byDisplayId = new LinkedHashMap<>();
        Map<KlovTarget, List<CaseMeta>> groups = new LinkedHashMap<>();
        for (CaseMeta meta : cases) {
            if (!meta.canQueryKlov()) {
                byDisplayId.put(meta.displayId(), List.of());
                continue;
            }
            if (meta.api() && !supportsApi(args.env())) {
                byDisplayId.put(meta.displayId(), List.of());
                errorByDisplayId.put(meta.displayId(), "API Klov has no " + args.env().toUpperCase(Locale.ROOT)
                        + " instance; use gk or preprod");
                continue;
            }
            List<KlovTarget> targets;
            try {
                targets = targetsFor(meta, args);
            } catch (RuntimeException e) {
                byDisplayId.put(meta.displayId(), List.of());
                errorByDisplayId.put(meta.displayId(), e.getMessage());
                continue;
            }
            targetsByDisplayId.put(meta.displayId(), targets);
            for (KlovTarget target : targets) {
                groups.computeIfAbsent(target, key -> new ArrayList<>()).add(meta);
            }
        }
        for (Map.Entry<KlovTarget, List<CaseMeta>> group : groups.entrySet()) {
            try {
                queryGroup(group.getKey(), group.getValue(), args, byDisplayId);
            } catch (Exception e) {
                // A slow query and a dead server need different responses, so they get
                // different messages rather than a blanket "unreachable".
                String reason = isQueryTimeout(e) ? "Klov query timed out" : "Klov unreachable";
                System.err.println("[klov] " + reason + " for project " + group.getKey().projectName()
                        + ": " + e.getMessage());
                for (CaseMeta meta : group.getValue()) {
                    // Only the case's own vertical failing makes the lookup untrustworthy.
                    // The shared suite being down must not turn an honest "never ran"
                    // into an error for every case on the platform.
                    if (isPrimary(meta, group.getKey())) {
                        errorByDisplayId.putIfAbsent(meta.displayId(), reason);
                    }
                }
            }
        }
        // Now that every candidate target for every case has been tried, a case whose
        // derived primary project was missing is only "fine, use the alias" when some
        // other target actually resolved to a real project; otherwise every candidate
        // was a miss and it is a genuine "no Klov project" error.
        for (Map.Entry<String, String> entry : pendingMissingPrimary.entrySet()) {
            if (!resolvedProjectDisplayIds.contains(entry.getKey())) {
                errorByDisplayId.putIfAbsent(entry.getKey(), "No Klov project: " + entry.getValue());
            }
        }
        // One candidate project being absent or unreachable stops mattering the moment
        // another candidate produced runs, so a successful lookup clears the complaint.
        for (Map.Entry<String, List<TestRun>> entry : byDisplayId.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                errorByDisplayId.remove(entry.getKey());
            }
        }
        return byDisplayId;
    }

    /**
     * A case can run under its own vertical's suite and under the shared
     * {@value #SHARED_SUITE} suite, which is a separate Klov project on a separate
     * host. Searching only the vertical reported "never ran in range" for cases that
     * had in fact just run, so both are searched and the newest run across them wins.
     */
    List<KlovTarget> targetsFor(CaseMeta meta, LookupArgs args) {
        List<KlovTarget> targets = new ArrayList<>();
        KlovTarget primary = targetFor(meta, args);
        targets.add(primary);
        if (args.klovProject() != null && !args.klovProject().isBlank()) {
            return targets;
        }
        for (String alias : aliases(meta)) {
            KlovTarget aliased = new KlovTarget(primary.mongoUri(), primary.klovServer(),
                    projectName(meta, alias, envLabel(args.env())));
            if (!targets.contains(aliased)) {
                targets.add(aliased);
            }
        }
        if (meta.api() || SHARED_SUITE.equalsIgnoreCase(meta.vertical().trim())) {
            return targets;
        }
        KlovTarget shared = sharedTarget(meta, args);
        if (shared != null && !targets.contains(shared)) {
            targets.add(shared);
        }
        return targets;
    }

    /**
     * TestRail's vertical does not always name the Klov project. The accommodation panel
     * reports to {@code PANEL AC-Panel-Regression PREPROD}, and no rule derives "AC" from
     * "Accommodation", so the mapping has to be stated in config rather than computed.
     * Aliases are searched in addition to the derived name, because a case can run under
     * either, and a name that matches no project costs one cheap lookup and is then
     * ignored rather than reported as a failure.
     */
    private List<String> aliases(CaseMeta meta) {
        String vertical = meta.vertical().trim();
        // The API projects are their own namespace, named with no prefix, environment or
        // platform. Keying them apart stops "ACCOMODATION - Client Facing" being wrapped
        // into a nonsense "PANEL ACCOMODATION - Client Facing PREPROD", and stops a
        // regression suite name being tried against the API Klov.
        String key = meta.api()
                ? "caselookup.klovAlias.api." + vertical
                : "caselookup.klovAlias." + vertical;
        String raw = System.getProperty(key);
        if (isBlank(raw)) {
            raw = config.getProperty(key);
        }
        if (isBlank(raw)) {
            return List.of();
        }
        List<String> out = new ArrayList<>();
        for (String part : raw.split(",")) {
            String alias = part.trim();
            if (!alias.isEmpty() && !alias.equalsIgnoreCase(vertical)) {
                out.add(alias);
            }
        }
        return out;
    }

    /**
     * The shared suite keeps the platform and environment of the case but replaces the
     * vertical, and it lives on the web Klov host even for app platforms, which is the
     * same routing {@link #isWebConfig} already applies to that vertical.
     */
    private KlovTarget sharedTarget(CaseMeta meta, LookupArgs args) {
        String envSuffix = envSuffix(args.env());
        String server = config.getProperty("klovServerDweb" + envSuffix);
        String host = config.getProperty("klovMongoDbDweb" + envSuffix);
        String port = config.getProperty("klovMongoDbPortDweb" + envSuffix);
        if (isBlank(server) || isBlank(host) || isBlank(port)) {
            return null;
        }
        String mongoUri = "mongodb://" + host.trim() + ":" + port.trim()
                + "/?connectTimeoutMS=3000&serverSelectionTimeoutMS=5000";
        return new KlovTarget(mongoUri, trimSlash(server.trim()),
                projectName(meta, SHARED_SUITE, envLabel(args.env())));
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /** True when this target is the case's own vertical rather than a shared suite. */
    private boolean isPrimary(CaseMeta meta, KlovTarget target) {
        List<KlovTarget> targets = targetsFor(meta.displayId());
        return targets.isEmpty() || target.equals(targets.get(0));
    }

    KlovTarget targetFor(CaseMeta meta, LookupArgs args) {
        String envSuffix = envSuffix(args.env());
        String envLabel = envLabel(args.env());
        String configType = meta.api() ? "Api" : (isWebConfig(meta) ? "Dweb" : "App");
        String server = required(config.getProperty("klovServer" + configType + envSuffix), configType, envSuffix);
        String host = required(config.getProperty("klovMongoDb" + configType + envSuffix), configType, envSuffix);
        String port = required(config.getProperty("klovMongoDbPort" + configType + envSuffix), configType, envSuffix);
        String mongoUri = "mongodb://" + host + ":" + port + "/?connectTimeoutMS=3000&serverSelectionTimeoutMS=5000";
        String project = args.klovProject() != null && !args.klovProject().isBlank()
                ? args.klovProject().trim()
                : projectName(meta, envLabel);
        return new KlovTarget(mongoUri, trimSlash(server), project);
    }

    private void queryGroup(KlovTarget target, List<CaseMeta> cases, LookupArgs args,
                            Map<String, List<TestRun>> byDisplayId) {
        Map<String, List<CaseMeta>> byMethod = new LinkedHashMap<>();
        for (CaseMeta meta : cases) {
            String methodKey = testRailMethodKey(target, meta.method());
            byMethod.computeIfAbsent(methodKey, key -> new ArrayList<>()).add(meta);
            // A case is queried once per candidate project, so these accumulate rather
            // than replace; overwriting here would discard the other project's runs.
            methodKeysByDisplayId.computeIfAbsent(meta.displayId(), key -> new ArrayList<>()).add(methodKey);
            runsByMethodKey.computeIfAbsent(methodKey, key -> new ArrayList<>());
            byDisplayId.computeIfAbsent(meta.displayId(), key -> new ArrayList<>());
        }
        try (MongoClient mongo = MongoClients.create(target.mongoUri())) {
            MongoDatabase db = mongo.getDatabase("klov");
            // An alias or the shared suite not existing is the normal case, not news.
            // Only the case's own vertical missing deserves the full explanation.
            boolean primary = cases.stream().anyMatch(meta -> isPrimary(meta, target));
            Document project = findProject(db, target, primary);
            if (project == null || !project.containsKey("_id")) {
                for (CaseMeta meta : cases) {
                    // The shared suite has no project for every platform and env, and
                    // that absence is normal rather than a failed lookup.
                    if (!isPrimary(meta, target)) {
                        continue;
                    }
                    if (aliases(meta).isEmpty()) {
                        errorByDisplayId.putIfAbsent(meta.displayId(), "No Klov project: " + target.projectName());
                    } else {
                        // The derived name here can legitimately not exist (e.g. web/panel
                        // verticals whose real Klov project only lives under the alias),
                        // but it can also be a genuine miss if the alias is wrong too. Wait
                        // until every candidate target for this case has been tried before
                        // deciding, instead of trusting that a configured alias means one
                        // will actually resolve.
                        pendingMissingPrimary.putIfAbsent(meta.displayId(), target.projectName());
                    }
                }
                return;
            }
            for (CaseMeta meta : cases) {
                resolvedProjectDisplayIds.add(meta.displayId());
            }
            ObjectId projectId = project.getObjectId("_id");
            targetByProjectId.put(projectId, target);
            if (project.getString("name") != null && !project.getString("name").isBlank()) {
                projectNameById.put(projectId, project.getString("name"));
            }
            System.err.println("[klov] project \"" + project.getString("name") + "\": querying "
                    + byMethod.keySet().size() + " method(s) for " + cases.size() + " case(s)");
            MongoCollection<Document> tests = db.getCollection("test");
            Map<ObjectId, ObjectId> reportIds = new HashMap<>();
            // Every method name seen in range, matched or not. Matching is exact, so
            // this is the evidence that separates "did not run" from "ran under a
            // different name", which otherwise look identical in the output.
            Set<String> seenMethods = new LinkedHashSet<>();
            for (LocalDate day = args.from(); !day.isAfter(args.to()); day = day.plusDays(1)) {
                Date start = Date.from(day.atStartOfDay(CaseRunLookup.ZONE).toInstant());
                Date end = Date.from(day.plusDays(1).atStartOfDay(CaseRunLookup.ZONE).toInstant());
                // parent:null matches both an explicit null and a missing field, and
                // unlike an $exists:false branch it stays indexable. The $or form made
                // the API Klov planner give up before it chose a plan.
                Bson filter = and(
                        eq("project", projectId),
                        gte("startTime", start),
                        lt("startTime", end),
                        eq("parent", null)
                );
                FindIterable<Document> docs = tests.find(filter)
                        .projection(include("name", "status", "startTime", "duration", "report", "parent"))
                        .maxTime(QUERY_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                for (Document doc : docs) {
                    if (doc.get("parent") != null) {
                        continue;
                    }
                    ParsedName parsed = parseName(doc.getString("name"));
                    if (parsed == null) {
                        continue;
                    }
                    seenMethods.add(parsed.method());
                    // parseName has already shed one retry suffix, so this side must not
                    // shed another or "foo_1_2" would reduce further here than it does on
                    // the TestRail side and stop matching.
                    String methodKey = target.projectName() + "::" + parsed.method().toLowerCase(Locale.ROOT);
                    List<CaseMeta> matches = byMethod.get(methodKey);
                    if (matches == null) {
                        continue;
                    }
                    TestRun run = toRun(doc, projectId, parsed.scenario());
                    if (run.reportId() != null) {
                        reportIds.putIfAbsent(run.reportId(), run.reportId());
                    }
                    runsByMethodKey.get(methodKey).add(run);
                    assign(run, parsed, matches, methodKey, byDisplayId);
                }
                sleepQuietly();
            }
            loadReportNames(db, reportIds.keySet().stream().toList());
            reportIfEmpty(tests, projectId, cases, byDisplayId, seenMethods);
        }
    }

    /**
     * An app or web run belongs to every case sharing the method, which is how this has
     * always worked. An API run names one scenario out of many, so it must land on
     * exactly one case; broadcasting it would credit twenty scenarios for one execution.
     * That holds even when the name carries no readable scenario, hence the check on the
     * shape of the name rather than on the scenario text.
     */
    private void assign(TestRun run, ParsedName parsed, List<CaseMeta> matches, String methodKey,
                        Map<String, List<TestRun>> byDisplayId) {
        if (!parsed.scenarioAware()) {
            for (CaseMeta meta : matches) {
                byDisplayId.get(meta.displayId()).add(run);
            }
            return;
        }
        Set<String> unmatched = unmatchedByMethodKey.computeIfAbsent(methodKey, key -> new LinkedHashSet<>());
        if (parsed.scenario() == null || parsed.scenario().isBlank()) {
            unmatched.add("(run with no scenario name)");
            return;
        }
        CaseMeta best = ScenarioMatcher.best(parsed.scenario(), matches);
        if (best != null) {
            byDisplayId.get(best.displayId()).add(run);
        } else {
            unmatched.add(parsed.scenario());
        }
    }

    /**
     * The API framework names its Klov project after the vertical alone, so the exact
     * name can drift (a "- Client Facing" suffix, different capitalisation). One cheap
     * read of the project list beats reporting a silent zero.
     */
    private Document findProject(MongoDatabase db, KlovTarget target, boolean verbose) {
        String wanted = target.projectName();
        MongoCollection<Document> projects = db.getCollection("project");
        Document exact = projects.find(eq("name", wanted))
                .maxTime(QUERY_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .first();
        if (exact != null) {
            return exact;
        }
        // Searching two projects per case means a platform with no shared-suite project
        // would otherwise re-scan the whole collection on every lookup.
        List<Document> all = projectListByUri.computeIfAbsent(target.mongoUri(), uri -> {
            List<Document> found = new ArrayList<>();
            for (Document doc : projects.find().projection(include("name"))
                    .maxTime(QUERY_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                found.add(doc);
            }
            return found;
        });
        for (Document doc : all) {
            if (wanted.equalsIgnoreCase(doc.getString("name"))) {
                System.err.println("[klov] project matched case-insensitively: " + doc.getString("name"));
                return doc;
            }
        }
        // A prefix match exists so the API framework's "Flight" finds "Flight - Client
        // Facing". Accepting the first of several would silently report another suite's
        // runs as this case's, so ambiguity is an absent project instead.
        String prefix = wanted.toLowerCase(Locale.ROOT) + " ";
        List<Document> prefixed = all.stream()
                .filter(doc -> doc.getString("name") != null
                        && doc.getString("name").toLowerCase(Locale.ROOT).startsWith(prefix))
                .toList();
        if (prefixed.size() == 1) {
            System.err.println("[klov] project matched by prefix: " + prefixed.get(0).getString("name"));
            return prefixed.get(0);
        }
        if (prefixed.size() > 1) {
            System.err.println("[klov] '" + wanted + "' is ambiguous, it prefixes "
                    + prefixed.stream().map(doc -> doc.getString("name")).sorted().toList()
                    + ". Name one with -Dklovproject=...");
            return null;
        }
        if (!verbose) {
            System.err.println("[klov] no project named '" + wanted + "', skipping it");
            return null;
        }
        System.err.println("[klov] no project named '" + wanted + "'");
        List<String> near = sameFamily(wanted, all);
        if (!near.isEmpty()) {
            System.err.println("[klov] same platform and environment: " + near);
            System.err.println("[klov] if one of those is this vertical, add"
                    + " caselookup.klovAlias.<vertical>=<segment> to config.properties");
        }
        System.err.println("[klov] all projects: "
                + all.stream().map(doc -> doc.getString("name")).sorted().toList());
        return null;
    }

    /**
     * The web and panel suites are named after an abbreviated code rather than the
     * vertical ("AC-Panel-Regression" for Accommodation), so the project we want is
     * invisible in a list of fifty. Narrowing to the same prefix, environment and
     * platform leaves a handful of candidates, which is short enough to read.
     */
    private static List<String> sameFamily(String wanted, List<Document> all) {
        String[] parts = wanted.split(" ");
        if (parts.length < 3) {
            return List.of();
        }
        return all.stream()
                .map(doc -> doc.getString("name"))
                .filter(name -> name != null && name.startsWith(parts[0] + " "))
                // Everything after the vertical segment: the env, and the platform when
                // the family has one.
                .filter(name -> {
                    for (int i = 2; i < parts.length; i++) {
                        if (!name.endsWith(" " + parts[i]) && !name.contains(" " + parts[i] + " ")) {
                            return false;
                        }
                    }
                    return true;
                })
                .sorted()
                .toList();
    }

    private void reportIfEmpty(MongoCollection<Document> tests, ObjectId projectId, List<CaseMeta> cases,
                               Map<String, List<TestRun>> byDisplayId, Set<String> seenMethods) {
        boolean anyHit = cases.stream().anyMatch(meta -> !byDisplayId.get(meta.displayId()).isEmpty());
        if (anyHit) {
            return;
        }
        Set<String> wanted = new LinkedHashSet<>();
        for (CaseMeta meta : cases) {
            wanted.add(meta.method());
        }
        System.err.println("[klov] nothing matched in range. Looked for method(s): " + String.join(", ", wanted));
        if (!seenMethods.isEmpty()) {
            // The suite ran, just not under this name. Matching is exact, so printing
            // what did run is the only way to see that a name differs rather than
            // guessing from a silent "never ran".
            List<String> sample = seenMethods.stream().sorted().limit(METHOD_SAMPLE).toList();
            System.err.println("[klov] but " + seenMethods.size() + " other method(s) did run in range: "
                    + String.join(", ", sample)
                    + (seenMethods.size() > METHOD_SAMPLE ? ", ..." : ""));
            return;
        }
        Bson rootFilter = and(eq("project", projectId), eq("parent", null));
        Document sample = tests.find(rootFilter)
                .projection(include("name", "startTime", "parent", "status"))
                .sort(new Document("startTime", -1))
                .maxTime(QUERY_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .first();
        if (sample == null) {
            System.err.println("[klov] this project holds no runs at all");
            return;
        }
        // The raw Klov name carries hash and nanosecond suffixes that swamp the log, so
        // only the parsed method and the date are worth printing here.
        ParsedName parsed = parseName(sample.getString("name"));
        System.err.println("[klov] nothing ran in range at all. Newest run in this project: "
                + (parsed == null ? "unrecognised name" : parsed.method())
                + " on " + CaseRunLookup.formatRunTime(sample.getDate("startTime")));
    }

    private void loadReportNames(MongoDatabase db, List<ObjectId> reportIds) {
        if (reportIds.isEmpty()) {
            return;
        }
        for (Document doc : db.getCollection("report")
                .find(in("_id", reportIds))
                .projection(include("name"))
                .maxTime(QUERY_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
            reportNameById.put(doc.getObjectId("_id"), doc.getString("name"));
        }
    }

    private static TestRun toRun(Document doc, ObjectId projectId, String scenario) {
        Date start = doc.getDate("startTime");
        Instant instant = start == null ? Instant.EPOCH : start.toInstant();
        Number duration = (Number) doc.get("duration");
        Object report = doc.get("report");
        ObjectId reportId = report instanceof ObjectId id ? id : null;
        return new TestRun(
                instant,
                doc.getString("status"),
                duration == null ? 0L : duration.longValue(),
                reportId,
                doc.getObjectId("_id"),
                projectId,
                scenario
        );
    }

    /**
     * The method, plus the scenario for API runs. {@code scenarioAware} records that the
     * name came from the API framework even when the scenario itself could not be read.
     */
    record ParsedName(String method, String scenario, boolean scenarioAware) {
    }

    /**
     * Two naming schemes reach Klov. This framework writes
     * {@code class#method@hash@nano@seq}; the API framework writes
     * {@code method<h6>Scenario Name : ...</h6>}.
     */
    static ParsedName parseName(String name) {
        if (name == null || name.isBlank() || name.startsWith("<")) {
            return null;
        }
        int markup = name.indexOf("<h6>");
        if (markup > 0) {
            String method = name.substring(0, markup).trim();
            if (method.isEmpty() || method.contains("<")) {
                return null;
            }
            return new ParsedName(stripNumericSuffix(method), scenarioFrom(name.substring(markup)), true);
        }
        if (name.contains("<")) {
            return null;
        }
        int hashIdx = name.indexOf('#');
        if (hashIdx >= 0) {
            String afterHash = name.substring(hashIdx + 1);
            int atIdx = afterHash.indexOf('@');
            return new ParsedName(
                    stripNumericSuffix(atIdx > 0 ? afterHash.substring(0, atIdx) : afterHash), null, false);
        }
        int lastDot = name.lastIndexOf('.');
        String method = lastDot >= 0 && lastDot < name.length() - 1 ? name.substring(lastDot + 1) : name;
        return new ParsedName(stripNumericSuffix(method), null, false);
    }

    static String extractMethodName(String name) {
        ParsedName parsed = parseName(name);
        return parsed == null ? null : parsed.method();
    }

    private static String scenarioFrom(String markup) {
        String text = markup.replaceAll("<[^>]+>", " ")
                .replace("&nbsp;", " ")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .trim();
        int label = text.toLowerCase(Locale.ROOT).indexOf("scenario name");
        if (label >= 0) {
            text = text.substring(label + "scenario name".length()).replaceFirst("^\\s*:\\s*", "");
        }
        text = text.replaceAll("\\s+", " ").trim();
        return "null".equals(text) ? "" : text;
    }

    /**
     * Klov appends {@code _<n>} to a retried test, and {@link #parseName} removes it. The
     * TestRail side was left raw, so a method genuinely written as {@code ..._2} produced
     * a key no Klov run could ever match and reported a silent "never ran". Both sides
     * now shed exactly one such suffix.
     */
    private static String testRailMethodKey(KlovTarget target, String method) {
        return target.projectName() + "::" + stripNumericSuffix(method.trim()).toLowerCase(Locale.ROOT);
    }

    private static String stripNumericSuffix(String name) {
        int lastUnderscore = name.lastIndexOf('_');
        if (lastUnderscore <= 0 || lastUnderscore == name.length() - 1) {
            return name;
        }
        String suffix = name.substring(lastUnderscore + 1);
        if (suffix.chars().allMatch(Character::isDigit)) {
            return name.substring(0, lastUnderscore);
        }
        return name;
    }

    static String jenkinsUrl(String reportName) {
        if (reportName == null || reportName.isBlank() || reportName.toLowerCase(Locale.ROOT).startsWith("local")) {
            return "-";
        }
        String url = reportName.replaceFirst("\\d{10,}$", "");
        return url.isBlank() ? "-" : url;
    }

    /**
     * Klov addresses a launch by project <em>name</em>, as in
     * {@code /projects/APP%20CriticalPathTests%20PREPROD%20ANDROID/launches/<id>/tests}.
     * The older {@code /#/projects/<objectId>/builds/<objectId>} hash route this used to
     * build resolves to nothing on the servers we run, so every link in the report was
     * dead while still looking plausible.
     */
    static String klovUrl(String klovServer, String projectName, ObjectId reportId) {
        if (klovServer == null || klovServer.isBlank() || projectName == null || projectName.isBlank()
                || reportId == null) {
            return "-";
        }
        // URLEncoder is form encoding, so a space arrives as "+", which a path segment
        // reads literally rather than as a space.
        String encoded = URLEncoder.encode(projectName.trim(), StandardCharsets.UTF_8).replace("+", "%20");
        return trimDefaultPort(trimSlash(klovServer)) + "/projects/" + encoded
                + "/launches/" + reportId.toHexString() + "/tests";
    }

    /** The config carries an explicit :80, which is noise in a link people paste around. */
    private static String trimDefaultPort(String url) {
        if (url.startsWith("http://") && url.endsWith(":80")) {
            return url.substring(0, url.length() - ":80".length());
        }
        if (url.startsWith("https://") && url.endsWith(":443")) {
            return url.substring(0, url.length() - ":443".length());
        }
        return url;
    }

    static String projectName(CaseMeta meta, String envLabel) {
        return projectName(meta, meta.vertical().trim(), envLabel);
    }

    /**
     * The same prefix and suffix rules with the vertical segment supplied, so the shared
     * suite and the configured aliases build their names one way instead of three.
     */
    static String projectName(CaseMeta meta, String vertical, String envLabel) {
        if (meta.api()) {
            // The API framework sets the Klov project to the vertical alone, with no
            // APP/WEB/PANEL prefix and no env or platform suffix.
            return vertical;
        }
        if (meta.panel()) {
            return String.format("PANEL %s %s", vertical, envLabel);
        }
        String platform = meta.platformCode();
        if (platform != null && (platform.equals("DWEB") || platform.equals("AMWEB") || platform.equals("IMWEB"))) {
            return String.format("WEB %s %s %s", vertical, envLabel, platform);
        }
        return String.format("APP %s %s %s", vertical, envLabel, platform);
    }

    private static boolean isWebConfig(CaseMeta meta) {
        String vertical = meta.vertical();
        boolean monitoring = vertical.equalsIgnoreCase("Monitoring") || vertical.equalsIgnoreCase("CriticalPathTests");
        String platform = meta.platformCode();
        boolean web = platform != null && (platform.contains("WEB") || platform.equals("DWEB") || platform.equals("AMWEB") || platform.equals("IMWEB"));
        return monitoring || web || meta.panel();
    }

    /**
     * Only a server-side maxTimeMS expiry counts as a slow query. A server-selection
     * timeout also says "timed out" but means the host is unreachable, which is a
     * different thing to go and fix, so it must not be lumped in here.
     */
    private static boolean isQueryTimeout(Throwable error) {
        for (Throwable cause = error; cause != null; cause = cause.getCause()) {
            if (cause instanceof MongoExecutionTimeoutException) {
                return true;
            }
            if (cause instanceof MongoTimeoutException || cause instanceof MongoSocketException) {
                return false;
            }
            String message = cause.getMessage();
            if (message != null && message.toLowerCase(Locale.ROOT).contains("exceeded time limit")) {
                return true;
            }
        }
        return false;
    }

    /** The API Klov has gk and preprod only; its prod slot is empty. */
    private static boolean supportsApi(String env) {
        String lower = env.toLowerCase(Locale.ROOT);
        return lower.equals("gk") || lower.equals("preprod");
    }

    private static String envSuffix(String env) {
        return switch (env.toLowerCase(Locale.ROOT)) {
            case "gk" -> "GK";
            case "prod", "preprod" -> "PreProd";
            default -> throw new IllegalArgumentException("Unsupported env: " + env);
        };
    }

    private static String envLabel(String env) {
        return switch (env.toLowerCase(Locale.ROOT)) {
            case "gk" -> "GK";
            case "prod" -> "PROD";
            case "preprod" -> "PREPROD";
            default -> throw new IllegalArgumentException("Unsupported env: " + env);
        };
    }

    private static String required(String value, String configType, String envSuffix) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing klov*" + configType + envSuffix + " host in config.properties");
        }
        return value.trim();
    }

    private static String trimSlash(String value) {
        if (value == null) {
            return "";
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private static void sleepQuietly() {
        try {
            Thread.sleep(DAY_PAUSE_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
