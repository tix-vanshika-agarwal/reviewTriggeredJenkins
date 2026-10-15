package com.tiket.web.test.prodmonitoring.sendgrafanasnap;

import com.tiket.testbase.Configs;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Centralized configuration for the Grafana monitoring snapshot/email flow.
 *
 * <h3>Resolution order (theme / from / to)</h3>
 * For these three values the resolver tries the keys below in order and returns the first
 * non-blank match:
 *
 * <ol>
 *   <li>Per-vertical key, e.g. {@code grafana.theme.accommodation}</li>
 *   <li>Global key, e.g. {@code grafana.theme}</li>
 *   <li>Hard-coded {@code DEFAULT_*} constant in this class (last resort)</li>
 * </ol>
 *
 * Mirrors the {@code recipients} / {@code recipients.<vertical>} pattern used by
 * {@code GrafanaEmailSender}, except these are <strong>single values</strong> (no union):
 * the per-vertical override <em>replaces</em> the global value.
 *
 * <h3>Resolution order (report period label)</h3>
 * The label is special — it is normally <strong>auto-derived</strong> from the resolved
 * {@code from} / {@code to} so you only ever maintain the time window in one place:
 *
 * <ol>
 *   <li>Per-vertical key {@code grafana.report.period.label.<vertical>}</li>
 *   <li>Global key {@code grafana.report.period.label}</li>
 *   <li><strong>Auto-derived</strong> from {@link #resolveFrom(String)} / {@link #resolveTo(String)}
 *       (e.g. {@code now-7d} + {@code now-2d} -> {@code "Last 7 days (excluding last 2 days)"})</li>
 *   <li>Raw {@code "From <from> to <to>"} string (only when auto-derivation fails)</li>
 * </ol>
 *
 * <h3>Property keys</h3>
 *
 * <pre>
 * # Theme (single value)
 * grafana.theme=light
 * grafana.theme.accommodation=light
 * grafana.theme.flight=dark
 *
 * # Time window
 * grafana.from=now-7d
 * grafana.to=now-2d
 * grafana.from.flight=now-24h     # per-vertical override
 * grafana.to.flight=now
 *
 * # Report period label (optional - auto-derived from from/to when blank/missing)
 * grafana.report.period.label=Custom override label
 * grafana.report.period.label.flight=Last 24 hours
 * </pre>
 *
 * <h3>Adding a new vertical</h3>
 * Zero code changes. Add the per-vertical keys (e.g. {@code grafana.theme.train=light}) - the resolver
 * builds the lookup dynamically from {@code verticalName.toLowerCase()}.
 *
 * <h3>Edge cases handled</h3>
 * <ul>
 *   <li>{@code null} or empty {@code verticalName} -> falls through to the global key.</li>
 *   <li>Property present but blank/whitespace -> treated as missing.</li>
 *   <li>Vertical name in any case (e.g. {@code "Accommodation"}) -> lower-cased before lookup.</li>
 *   <li>Auto-derive: same-unit windows ({@code now-7d}/{@code now-2d}) produce the
 *       "Last X (excluding Y)" form; {@code to=now} produces "Last X".</li>
 *   <li>Auto-derive: mixed-unit / absolute-timestamp / unparseable values -> raw fallback
 *       {@code "From <from> to <to>"}.</li>
 *   <li>Auto-derive handles plural/singular ({@code "1 day"} vs {@code "2 days"}).</li>
 *   <li>Auto-derive accepts Grafana rounding suffixes like {@code now-1d/d} (suffix ignored
 *       in the label).</li>
 * </ul>
 */
public final class GrafanaSnapConfig {

    // -- Property keys (global) -----------------------------------------------------------
    // All keys are namespaced under "grafana." so they cannot collide with unrelated
    // subsystems that might use generic names like "theme".
    private static final String KEY_THEME = "grafana.theme";
    private static final String KEY_FROM = "grafana.from";
    private static final String KEY_TO = "grafana.to";
    private static final String KEY_REPORT_PERIOD_LABEL = "grafana.report.period.label";

    // -- Defaults (final fallback when neither per-vertical nor global property is set) ---
    public static final String DEFAULT_THEME = "light";
    public static final String DEFAULT_FROM = "now-7d";
    public static final String DEFAULT_TO = "now-2d";

    // -- Relative-time parsing for auto-derived labels -----------------------------------
    /**
     * Matches Grafana relative-time expressions:
     *   now                  -> amount=null, unit=null
     *   now-7d               -> amount=7,   unit=d
     *   now-1d/d             -> amount=1,   unit=d (rounding suffix ignored)
     *   now/d                -> amount=null,unit=null (rounding-only, treated like "now")
     */
    private static final Pattern RELATIVE_TIME = Pattern.compile(
            "^now(?:-(\\d+)([smhdwMy]))?(?:/[smhdwMy])?$");

    private GrafanaSnapConfig() {
    }

    /**
     * Resolves the render theme for a given vertical.
     * Lookup order: {@code grafana.theme.<vertical>} -> {@code grafana.theme} -> {@link #DEFAULT_THEME}.
     */
    public static String resolveTheme(String verticalName) {
        return resolveSingle(KEY_THEME, verticalName, DEFAULT_THEME);
    }

    /**
     * Resolves the snapshot/dashboard time-range start for a given vertical.
     * Lookup order: {@code grafana.from.<vertical>} -> {@code grafana.from} -> {@link #DEFAULT_FROM}.
     */
    public static String resolveFrom(String verticalName) {
        return resolveSingle(KEY_FROM, verticalName, DEFAULT_FROM);
    }

    public static String resolveFrom() {

        LocalDate yesterday = LocalDate.now(ZoneOffset.UTC).minusDays(1);

        ZonedDateTime from = yesterday.atStartOfDay(ZoneOffset.UTC);

        return from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
    }

    /**
     * Resolves the snapshot/dashboard time-range end for a given vertical.
     * Lookup order: {@code grafana.to.<vertical>} -> {@code grafana.to} -> {@link #DEFAULT_TO}.
     */
    public static String resolveTo(String verticalName) {
        return resolveSingle(KEY_TO, verticalName, DEFAULT_TO);
    }

    public static String resolveTo() {

        LocalDate yesterday = LocalDate.now(ZoneOffset.UTC).minusDays(1);

        ZonedDateTime to = yesterday
                .plusDays(1)
                .atStartOfDay(ZoneOffset.UTC)
                .minusSeconds(1)
                .withZoneSameInstant(ZoneOffset.UTC);

        return to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX"));
    }

    /**
     * Resolves the email-body "Report Period" label for a given vertical.
     *
     * <p>Lookup order:
     * <ol>
     *   <li>{@code grafana.report.period.label.<vertical>}</li>
     *   <li>{@code grafana.report.period.label}</li>
     *   <li>Auto-derived from {@link #resolveFrom(String)} / {@link #resolveTo(String)}</li>
     *   <li>Raw {@code "From <from> to <to>"} (only if auto-derive fails)</li>
     * </ol>
     */
    public static String resolveReportPeriodLabel(String verticalName) {
        // 1. Explicit property override (per-vertical or global)
        String explicit = lookupPropertyOnly(KEY_REPORT_PERIOD_LABEL, verticalName);
        if (explicit != null) {
            return explicit;
        }

        // 2. Auto-derive from the resolved from/to
        String from = resolveFrom(verticalName);
        String to = resolveTo(verticalName);
        String derived = deriveReportPeriodLabel(from, to);
        if (derived != null) {
            return derived;
        }

        // 3. Raw fallback (when from/to use mixed units or absolute timestamps)
        return "From " + from + " to " + to;
    }

    /**
     * Builds a human-readable "Report Period" label from a Grafana relative-time pair.
     * Returns {@code null} when the values cannot be parsed (caller should fall back).
     *
     * Examples:
     *   from="now-7d",  to="now-2d"  -> "Last 7 days (excluding last 2 days)"
     *   from="now-7d",  to="now"     -> "Last 7 days"
     *   from="now-24h", to="now"     -> "Last 24 hours"
     *   from="now-1d",  to="now"     -> "Last 1 day"
     *   from="now-1h",  to="now-30m" -> null  (mixed units; caller falls back to raw)
     *   from="1745625600000", to=... -> null  (absolute timestamps; caller falls back to raw)
     */
    static String deriveReportPeriodLabel(String from, String to) {
        if (from == null || to == null) {
            return null;
        }
        Matcher fm = RELATIVE_TIME.matcher(from.trim());
        Matcher tm = RELATIVE_TIME.matcher(to.trim());
        if (!fm.matches() || !tm.matches()) {
            return null;
        }

        long fromAmount = parseAmount(fm.group(1));
        String fromUnit = fm.group(2);
        long toAmount = parseAmount(tm.group(1));
        String toUnit = tm.group(2);

        // "now" as the start makes no sense as a report range start
        if (fromAmount == 0L) {
            return null;
        }

        String fromWord = pluralizeUnit(fromAmount, fromUnit);

        // "Last 7 days" (to == "now")
        if (toAmount == 0L) {
            return "Last " + fromAmount + " " + fromWord;
        }

        // Same unit on both sides -> "Last X <unit> (excluding last Y <unit>)"
        if (fromUnit != null && fromUnit.equals(toUnit)) {
            // Sanity: from must be older than to (greater amount-back-from-now)
            if (toAmount >= fromAmount) {
                return null;
            }
            String toWord = pluralizeUnit(toAmount, toUnit);
            return "Last " + fromAmount + " " + fromWord
                    + " (excluding last " + toAmount + " " + toWord + ")";
        }

        // Mixed units -> let caller fall back to raw "From X to Y"
        return null;
    }

    private static long parseAmount(String captured) {
        if (captured == null || captured.isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(captured);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private static String pluralizeUnit(long amount, String unit) {
        if (unit == null) {
            return "";
        }
        String word;
        switch (unit) {
            case "s": word = "second"; break;
            case "m": word = "minute"; break;
            case "h": word = "hour"; break;
            case "d": word = "day"; break;
            case "w": word = "week"; break;
            case "M": word = "month"; break;
            case "y": word = "year"; break;
            default: return unit;
        }
        return amount == 1L ? word : word + "s";
    }

    /**
     * Generic single-value resolver: per-vertical key wins, falls back to global key,
     * then to the supplied default. Treats null/blank as "not set".
     */
    private static String resolveSingle(String baseKey, String verticalName, String defaultValue) {
        String value = lookupPropertyOnly(baseKey, verticalName);
        return value != null ? value : defaultValue;
    }

    /**
     * Same lookup chain as {@link #resolveSingle(String, String, String)} but returns
     * {@code null} (instead of a default) when neither property is set. Used by
     * {@link #resolveReportPeriodLabel(String)} which has its own derivation step.
     */
    private static String lookupPropertyOnly(String baseKey, String verticalName) {
        // Reject whitespace-only verticalName too, otherwise we'd build keys like "theme."
        // and accidentally hit unrelated properties.
        String normalizedVertical = (verticalName == null) ? null : verticalName.trim().toLowerCase();
        if (normalizedVertical != null && !normalizedVertical.isEmpty()) {
            String verticalKey = baseKey + "." + normalizedVertical;
            String verticalValue = Configs.PROPERTIES_DATA.get(verticalKey);
            if (verticalValue != null && !verticalValue.isBlank()) {
                return verticalValue.trim();
            }
        }
        String globalValue = Configs.PROPERTIES_DATA.get(baseKey);
        if (globalValue != null && !globalValue.isBlank()) {
            return globalValue.trim();
        }
        return null;
    }
}
