package com.tiket.tools.caselookup;

import com.tiket.postprocessing.testrail.APIClient;
import com.tiket.tools.caselookup.LookupModels.CaseMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

final class TestRailCaseClient {

    static final String PLATFORM_API = "API";

    /** A mis-typed section must not turn into thousands of Klov rows. */
    private static final int MAX_GROUP_CASES = 200;
    private static final int PAGE_SIZE = 250;

    private final APIClient client;
    private final Map<String, Map<String, String>> dropdownLabels = new HashMap<>();
    private final Map<String, List<CaseMeta>> groupCache = new HashMap<>();
    private final Map<Integer, Integer> projectIdBySuite = new HashMap<>();
    private final List<String> warnings = new ArrayList<>();
    private Integer projectIdOverride;
    private Integer projectIdFallback;

    TestRailCaseClient(Properties config, Properties credentials) throws IOException {
        String url = firstNonBlank(
                System.getProperty("testrail.url"),
                System.getenv("TESTRAIL_BASE_URL"),
                config.getProperty("testrail.url")
        );
        if (url == null) {
            throw new IllegalStateException(
                    "TestRail URL missing. Set TESTRAIL_BASE_URL, -Dtestrail.url, or testrail.url in config.properties");
        }
        url = normalizeTestRailUrl(url);
        String user = firstNonBlank(
                System.getenv("TESTRAIL_USERNAME"),
                credentials.getProperty("testrail.user")
        );
        String password = firstNonBlank(
                System.getenv("TESTRAIL_API_KEY"),
                credentials.getProperty("testrail.password")
        );
        if (user == null || password == null) {
            throw new IllegalStateException("TestRail user/password missing in credential.properties or env");
        }
        this.client = new APIClient(url);
        this.client.setUser(user);
        this.client.setPassword(password);
        try {
            loadCaseFields();
        } catch (Exception e) {
            System.err.println("Could not load TestRail case fields (dropdown labels may be ids): " + e.getMessage());
        }
    }

    /** Only from --project-id: an explicit instruction wins over anything discovered. */
    void setProjectIdOverride(Integer projectId) {
        this.projectIdOverride = projectId;
    }

    /** From config, used only when TestRail cannot tell us which project owns a suite. */
    void setProjectIdFallback(Integer projectId) {
        this.projectIdFallback = projectId;
    }

    CaseMeta fetch(String rawId) {
        String displayId = rawId.toUpperCase(Locale.ROOT).startsWith("C") ? rawId.toUpperCase(Locale.ROOT) : "C" + rawId;
        int numericId = Integer.parseInt(displayId.substring(1));
        try {
            Object response = client.sendGet("get_case/" + numericId);
            if (!(response instanceof JSONObject caseJson)) {
                return skipped(displayId, numericId, "", 0, 0, true, "TestRail returned no case JSON");
            }
            return toMeta(caseJson, true);
        } catch (Exception e) {
            return skipped(displayId, numericId, "", 0, 0, true, "TestRail error: " + e.getMessage());
        }
    }

    /**
     * API automation runs one method per endpoint, and TestRail keeps every scenario for
     * that endpoint as a separate case in one section. Looking up a single API id would
     * therefore show one scenario out of twenty, so each API case is expanded into its
     * whole section. Results are cached per section, which keeps a batch of sibling ids
     * down to one {@code get_cases} call.
     */
    List<CaseMeta> expandApiGroups(List<CaseMeta> seeds) {
        Map<Integer, CaseMeta> byId = new LinkedHashMap<>();
        for (CaseMeta seed : seeds) {
            byId.putIfAbsent(seed.numericId(), seed);
        }
        for (CaseMeta seed : List.copyOf(byId.values())) {
            if (!seed.expandable()) {
                continue;
            }
            for (CaseMeta sibling : fetchGroup(seed.suiteId(), seed.sectionId())) {
                byId.putIfAbsent(sibling.numericId(), sibling);
            }
        }
        return new ArrayList<>(byId.values());
    }

    /** Anything that made a lookup less complete than it looks, shown on the report. */
    List<String> warnings() {
        return List.copyOf(warnings);
    }

    /** Every case in one TestRail section, i.e. every scenario of one API endpoint. */
    List<CaseMeta> fetchGroup(int suiteId, int sectionId) {
        String key = suiteId + "/" + sectionId;
        List<CaseMeta> cached = groupCache.get(key);
        if (cached != null) {
            return cached;
        }
        List<CaseMeta> group = new ArrayList<>();
        try {
            int projectId = projectIdFor(suiteId);
            int offset = 0;
            while (group.size() < MAX_GROUP_CASES) {
                Object response = client.sendGet("get_cases/" + projectId
                        + "&suite_id=" + suiteId + "&section_id=" + sectionId
                        + "&limit=" + PAGE_SIZE + "&offset=" + offset);
                JSONArray page = casesArray(response);
                if (page == null || page.isEmpty()) {
                    break;
                }
                for (Object entry : page) {
                    if (entry instanceof JSONObject caseJson && group.size() < MAX_GROUP_CASES) {
                        group.add(toMeta(caseJson, false));
                    }
                }
                if (page.size() < PAGE_SIZE) {
                    break;
                }
                offset += page.size();
            }
            if (group.size() >= MAX_GROUP_CASES) {
                warn("Section " + sectionId + " has at least " + MAX_GROUP_CASES
                        + " cases; only the first " + MAX_GROUP_CASES + " are shown");
            }
            if (group.isEmpty()) {
                warn("TestRail returned no cases for suite " + suiteId + " section " + sectionId);
            }
        } catch (Exception e) {
            // Losing the siblings is not the same as them never running, so this has to
            // reach the report rather than only the console.
            warn("Could not expand suite " + suiteId + " section " + sectionId + ": " + e.getMessage());
        }
        List<CaseMeta> result = List.copyOf(group);
        groupCache.put(key, result);
        return result;
    }

    private void warn(String message) {
        System.err.println(message);
        warnings.add(message);
    }

    /** TestRail 6.7+ wraps the list in an object; older versions return a bare array. */
    private static JSONArray casesArray(Object response) {
        if (response instanceof JSONArray array) {
            return array;
        }
        if (response instanceof JSONObject object && object.get("cases") instanceof JSONArray array) {
            return array;
        }
        return null;
    }

    /**
     * Asks TestRail which project owns the suite rather than assuming the configured
     * one, so a suite outside the default project still expands correctly.
     */
    private int projectIdFor(int suiteId) {
        if (projectIdOverride != null) {
            return projectIdOverride;
        }
        Integer cached = projectIdBySuite.get(suiteId);
        if (cached != null) {
            return cached;
        }
        try {
            Object response = client.sendGet("get_suite/" + suiteId);
            if (response instanceof JSONObject suite && suite.get("project_id") instanceof Number projectId) {
                projectIdBySuite.put(suiteId, projectId.intValue());
                return projectId.intValue();
            }
        } catch (Exception e) {
            System.err.println("Could not read suite " + suiteId + " from TestRail: " + e.getMessage());
        }
        if (projectIdFallback != null) {
            projectIdBySuite.put(suiteId, projectIdFallback);
            return projectIdFallback;
        }
        throw new IllegalStateException("Cannot resolve the TestRail project for suite " + suiteId
                + ". Pass --project-id or set caselookup.testrail.projectId.");
    }

    CaseMeta withPlatformOverride(CaseMeta meta, String platformOverride) {
        if (platformOverride == null || platformOverride.isBlank()) {
            return meta;
        }
        if (meta.skipReason() != null && !meta.skipReason().toLowerCase(Locale.ROOT).contains("platform")) {
            return meta;
        }
        return buildMeta(meta.displayId(), meta.numericId(), meta.title(),
                "-".equals(meta.method()) ? "" : meta.method(),
                meta.vertical(), platformOverride, platformOverride,
                meta.suiteId(), meta.sectionId(), meta.requested());
    }

    private CaseMeta toMeta(JSONObject caseJson, boolean requested) {
        int numericId = intValue(caseJson.get("id"));
        String displayId = "C" + numericId;
        String title = stringValue(caseJson.get("title"));
        int suiteId = intValue(caseJson.get("suite_id"));
        int sectionId = intValue(caseJson.get("section_id"));
        Map<String, Object> custom = customFields(caseJson);
        String vertical = resolveLabel("vertical", firstMatching(custom, "case_vertical", "vertical"));
        if (vertical.isBlank()) {
            vertical = resolveLabel("vertical", firstMatchingContains(custom, "vertical"));
        }
        String method = stringValue(firstMatching(custom,
                "case_code_testmethod_name", "testmethod", "test_method", "method_name", "automation_method"));
        if (method.isBlank()) {
            method = stringValue(firstMatchingContains(custom, "testmethod"));
        }
        if (method.isBlank()) {
            method = stringValue(firstMatchingContains(custom, "method"));
        }
        Object platformRaw = firstMatching(custom, "platform");
        String platformLabel = resolveLabel("platform", platformRaw);
        if (platformLabel.isBlank()) {
            platformLabel = stringValue(platformRaw);
        }
        if (vertical.isBlank() || method.isBlank() || platformLabel.isBlank()) {
            System.err.println(displayId + " incomplete in TestRail: vertical=" + vertical
                    + " method=" + method + " platform=" + platformLabel);
        }
        return buildMeta(displayId, numericId, title, method, vertical, platformLabel, null,
                suiteId, sectionId, requested);
    }

    private CaseMeta buildMeta(String displayId, int numericId, String title, String method,
                               String vertical, String platformLabel, String platformOverride,
                               int suiteId, int sectionId, boolean requested) {
        String effectivePlatform = platformOverride != null && !platformOverride.isBlank() ? platformOverride : platformLabel;
        String platformCode = toPlatformCode(effectivePlatform);
        boolean api = PLATFORM_API.equals(platformCode);
        if (method == null || method.isBlank()) {
            return skipped(displayId, numericId, title, suiteId, sectionId, requested, "Missing method in TestRail");
        }
        if (vertical == null || vertical.isBlank()) {
            return skipped(displayId, numericId, title, suiteId, sectionId, requested, "Missing vertical in TestRail");
        }
        boolean panel = containsAny(effectivePlatform, "panel");
        if (platformCode == null && !panel) {
            return new CaseMeta(displayId, numericId, title, method.trim(), vertical.trim(),
                    effectivePlatform, null, false, false, suiteId, sectionId, requested,
                    "Missing platform in TestRail");
        }
        return new CaseMeta(displayId, numericId, title, method.trim(), vertical.trim(),
                effectivePlatform, platformCode, panel, api, suiteId, sectionId, requested, null);
    }

    private static CaseMeta skipped(String displayId, int numericId, String title,
                                    int suiteId, int sectionId, boolean requested, String reason) {
        return new CaseMeta(displayId, numericId, title, "-", "", "", null, false, false,
                suiteId, sectionId, requested, reason);
    }

    @SuppressWarnings("unchecked")
    private void loadCaseFields() throws IOException {
        Object response = client.sendGet("get_case_fields");
        if (!(response instanceof JSONArray fields)) {
            return;
        }
        for (Object fieldObj : fields) {
            if (!(fieldObj instanceof JSONObject field)) {
                continue;
            }
            String systemName = stringValue(field.get("system_name")).toLowerCase(Locale.ROOT);
            String name = stringValue(field.get("name")).toLowerCase(Locale.ROOT);
            String key = systemName.isBlank() ? name : systemName;
            Map<String, String> items = parseDropdownItems(field);
            if (!items.isEmpty()) {
                dropdownLabels.put(key, items);
                if (key.contains("vertical")) {
                    dropdownLabels.put("vertical", items);
                }
                if (key.contains("platform")) {
                    dropdownLabels.put("platform", items);
                }
            }
        }
    }

    private String resolveLabel(String kind, Object raw) {
        if (raw == null) {
            return "";
        }
        String asString = stringValue(raw);
        Map<String, String> items = dropdownLabels.get(kind);
        if (items != null && items.containsKey(asString)) {
            return items.get(asString);
        }
        return asString;
    }

    private static Map<String, String> parseDropdownItems(JSONObject field) {
        Map<String, String> items = new HashMap<>();
        Object configs = field.get("configs");
        if (!(configs instanceof JSONArray configArray)) {
            return items;
        }
        for (Object cfg : configArray) {
            if (!(cfg instanceof JSONObject config)) {
                continue;
            }
            Object optionsObj = config.get("options");
            if (!(optionsObj instanceof JSONObject options)) {
                continue;
            }
            String itemsText = stringValue(options.get("items"));
            if (itemsText.isBlank()) {
                continue;
            }
            for (String line : itemsText.split("\\r?\\n")) {
                int comma = line.indexOf(',');
                if (comma <= 0) {
                    continue;
                }
                String id = line.substring(0, comma).trim();
                String label = line.substring(comma + 1).trim();
                if (!id.isEmpty() && !label.isEmpty()) {
                    items.put(id, label);
                }
            }
        }
        return items;
    }

    private static Map<String, Object> customFields(JSONObject caseJson) {
        Map<String, Object> custom = new HashMap<>();
        for (Object keyObj : caseJson.keySet()) {
            String key = String.valueOf(keyObj);
            if (key.startsWith("custom_")) {
                custom.put(key.substring("custom_".length()).toLowerCase(Locale.ROOT), caseJson.get(keyObj));
            }
        }
        return custom;
    }

    private static Object firstMatching(Map<String, Object> custom, String... names) {
        for (String name : names) {
            if (custom.containsKey(name)) {
                return custom.get(name);
            }
        }
        return null;
    }

    private static Object firstMatchingContains(Map<String, Object> custom, String fragment) {
        for (Map.Entry<String, Object> entry : custom.entrySet()) {
            if (entry.getKey().contains(fragment) && entry.getValue() != null && !stringValue(entry.getValue()).isBlank()) {
                return entry.getValue();
            }
        }
        return null;
    }

    static String toPlatformCode(String label) {
        if (label == null || label.isBlank()) {
            return null;
        }
        String lower = label.toLowerCase(Locale.ROOT);
        if (lower.contains("panel")) {
            return "DWEB";
        }
        // Exact match only: the neighbouring TestRail option "Backend (Other than API)"
        // is deliberately not an API case. Must come before the generic web/app checks,
        // since API runs live in their own framework and their own Klov instance.
        if (lower.equals("api")) {
            return PLATFORM_API;
        }
        if (lower.contains("amweb")) {
            return "AMWEB";
        }
        if (lower.contains("imweb")) {
            return "IMWEB";
        }
        if (lower.contains("dweb") || lower.contains("desktop") || (lower.contains("web") && !lower.contains("app"))) {
            return "DWEB";
        }
        if (lower.contains("android")) {
            return "ANDROID";
        }
        if (lower.contains("ios")) {
            return "IOS";
        }
        return null;
    }

    private static boolean containsAny(String value, String fragment) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(fragment);
    }

    private static int intValue(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value == null) {
            return 0;
        }
        try {
            return Integer.parseInt(String.valueOf(value).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    static String stringValue(Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof Number number) {
            if (number.doubleValue() == number.longValue()) {
                return String.valueOf(number.longValue());
            }
        }
        return stripHtml(String.valueOf(value).trim());
    }

    static String stripHtml(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.replaceAll("(?i)<br\\s*/?>", " ")
                .replaceAll("<[^>]+>", "")
                .replace("&nbsp;", " ")
                .replace("&amp;", "&")
                .trim();
    }

    private static String normalizeTestRailUrl(String url) {
        String normalized = url.trim();
        if (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        if (normalized.endsWith("/index.php")) {
            normalized = normalized.substring(0, normalized.length() - "/index.php".length());
        }
        int apiIdx = normalized.indexOf("/index.php?");
        if (apiIdx > 0) {
            normalized = normalized.substring(0, apiIdx);
        }
        return normalized;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }
}
