package com.tiket.api.auth;

import com.tiket.model.api.ApiResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses admin dashboard login responses: legacy GraphQL (gql-dashboard) or REST member-core v3,
 * and extracts {@code session_access_token} from {@code Set-Cookie} headers.
 */
public final class AdminDashboardLoginResponseParser {

    private AdminDashboardLoginResponseParser() {}

    private static final Pattern MAX_AGE_PATTERN = Pattern.compile("Max-Age=(\\d+)", Pattern.CASE_INSENSITIVE);

    /** A single {@code Set-Cookie} entry: {@code name=value} plus its {@code Max-Age} in millis. */
    public record ParsedCookie(String name, String value, long maxAgeMillis) {}

    /**
     * Parses every {@code Set-Cookie} header on {@code response} into {@link ParsedCookie}s so the full auth
     * cookie set can be replayed into a browser.
     *
     * @param defaultMaxAgeMillis lifetime applied to a cookie that carries no {@code Max-Age} attribute
     */
    public static List<ParsedCookie> parseSetCookies(ApiResponse response, long defaultMaxAgeMillis) {
        if (response == null) {
            return new ArrayList<>();
        }
        return parseSetCookies(response.getHeaderParams(), defaultMaxAgeMillis);
    }

    public static List<ParsedCookie> parseSetCookies(Map<String, ArrayList<Object>> headers, long defaultMaxAgeMillis) {
        List<ParsedCookie> parsed = new ArrayList<>();
        if (headers == null) {
            return parsed;
        }
        ArrayList<Object> rawCookies = headers.get("Set-Cookie");
        if (rawCookies == null) {
            rawCookies = headers.get("set-cookie");
        }
        if (rawCookies == null) {
            return parsed;
        }
        for (Object raw : rawCookies) {
            ParsedCookie cookie = parseCookie(raw.toString(), defaultMaxAgeMillis);
            if (cookie != null) {
                parsed.add(cookie);
            }
        }
        return parsed;
    }

    /** Parses one raw {@code Set-Cookie} value; returns {@code null} when it has no usable {@code name=value}. */
    public static ParsedCookie parseCookie(String rawCookie, long defaultMaxAgeMillis) {
        if (rawCookie == null || rawCookie.isBlank()) {
            return null;
        }
        String nameValue = rawCookie.split(";", 2)[0].trim();
        int eq = nameValue.indexOf('=');
        if (eq <= 0) {
            return null;
        }
        String name = nameValue.substring(0, eq).trim();
        String value = nameValue.substring(eq + 1).trim();
        if (value.isEmpty()) {
            return null;
        }
        long maxAgeMillis = defaultMaxAgeMillis;
        Matcher matcher = MAX_AGE_PATTERN.matcher(rawCookie);
        if (matcher.find()) {
            maxAgeMillis = Long.parseLong(matcher.group(1)) * 1000L;
        }
        return new ParsedCookie(name, value, maxAgeMillis);
    }

    public static Optional<String> extractSessionAccessToken(Map<String, ArrayList<Object>> headers) {
        return extractCookieValue(headers, "session_access_token");
    }

    public static Optional<String> extractCookieValue(Map<String, ArrayList<Object>> headers, String cookieName) {
        ArrayList<Object> cookies = headers.get("Set-Cookie");
        if (cookies == null) {
            cookies = headers.get("set-cookie");
        }
        if (cookies == null || cookies.isEmpty()) {
            return Optional.empty();
        }
        String prefix = cookieName + "=";
        return cookies.stream()
                .map(Object::toString)
                .filter(s -> s.contains(prefix))
                .findFirst()
                .map(s -> parseCookieValue(s, cookieName));
    }

    public static String resolveCookieValueOrNull(ApiResponse response, String cookieName) {
        if (response == null || response.getHeaderParams() == null) {
            return null;
        }
        return extractCookieValue(response.getHeaderParams(), cookieName).orElse(null);
    }

    private static String parseTokenFromCookieFragment(String raw) {
        return parseCookieValue(raw, "session_access_token");
    }

    private static String parseCookieValue(String raw, String cookieName) {
        String prefix = cookieName + "=";
        int idx = raw.indexOf(prefix);
        if (idx < 0) {
            return null;
        }
        int start = idx + prefix.length();
        int end = raw.indexOf(';', start);
        if (end == -1) {
            end = raw.length();
        }
        return raw.substring(start, end);
    }

    /**
     * True if response body indicates success (GraphQL {@code loginUser.message} or REST {@code status.message}).
     */
    public static boolean isLoginSuccessBody(String body) {
        if (body == null || body.isBlank()) {
            return false;
        }
        String trimmed = body.trim();
        try {
            if (trimmed.startsWith("[")) {
                String msg = new JSONArray(trimmed).getJSONObject(0).getJSONObject("data")
                        .getJSONObject("loginUser").get("message").toString();
                return "SUCCESS".equals(msg);
            }
            JSONObject json = new JSONObject(trimmed);
            JSONObject status = json.optJSONObject("status");
            if (status != null && "SUCCESS".equalsIgnoreCase(status.optString("message"))) {
                return true;
            }
            return "SUCCESS".equalsIgnoreCase(json.optString("message"));
        } catch (Exception e) {
            return false;
        }
    }

    public static String resolveSessionTokenOrNull(ApiResponse response) {
        Optional<String> fromCookie = extractSessionAccessToken(response.getHeaderParams());
        if (fromCookie.isPresent() && fromCookie.get() != null && !fromCookie.get().isBlank()) {
            return fromCookie.get();
        }
        if (!isLoginSuccessBody(response.getBody())) {
            return null;
        }
        Map<String, ArrayList<Object>> header = response.getHeaderParams();
        String key = header.containsKey("Set-Cookie") ? "Set-Cookie" : "set-cookie";
        ArrayList<Object> listSetCookies = header.get(key);
        if (listSetCookies == null || listSetCookies.isEmpty()) {
            return null;
        }
        String raw = listSetCookies.get(0).toString().contains("session_access_token")
                ? listSetCookies.get(0).toString()
                : (listSetCookies.size() > 1 ? listSetCookies.get(1).toString() : "");
        if (!raw.contains("session_access_token")) {
            return null;
        }
        return parseTokenFromCookieFragment(raw);
    }

    public static String buildDashboardCookieHeader(String aat, String art, String sessionAccessToken,
                                                    String sessionRefreshToken) {
        StringBuilder cookie = new StringBuilder();
        appendCookie(cookie, "aat", aat);
        appendCookie(cookie, "art", art);
        appendCookie(cookie, "session_access_token", sessionAccessToken);
        appendCookie(cookie, "session_refresh_token", sessionRefreshToken);
        return cookie.toString();
    }

    public static String toDashboardCookieHeader(String accessTokenOrCookie) {
        if (accessTokenOrCookie == null || accessTokenOrCookie.isBlank()) {
            return "";
        }
        if (accessTokenOrCookie.contains("session_access_token=") || accessTokenOrCookie.contains("aat=")) {
            return accessTokenOrCookie;
        }
        String token = accessTokenOrCookie.trim();
        if (token.regionMatches(true, 0, "Bearer ", 0, 7)) {
            token = token.substring(7).trim();
        }
        return "session_access_token=" + token;
    }

    public static void applyDashboardAuth(Map<String, Object> headers, String accessTokenOrCookie) {
        headers.put("Cookie", toDashboardCookieHeader(accessTokenOrCookie));
        String bearer = toStaffBearerAuthorization(accessTokenOrCookie);
        if (bearer != null) {
            headers.put("Authorization", bearer);
        } else {
            headers.remove("Authorization");
        }
    }

    private static String toStaffBearerAuthorization(String accessTokenOrCookie) {
        if (accessTokenOrCookie == null || accessTokenOrCookie.isBlank()) {
            return null;
        }
        String raw = accessTokenOrCookie.trim();
        if (raw.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return raw;
        }
        String aat = cookiePairValue(raw, "aat");
        if (aat != null) {
            return "Bearer " + aat;
        }
        String session = cookiePairValue(raw, "session_access_token");
        if (session != null) {
            return "Bearer " + session;
        }
        if (!raw.contains("=") && !raw.contains(";")) {
            return "Bearer " + raw;
        }
        return null;
    }

    private static String cookiePairValue(String cookieHeader, String name) {
        String prefix = name + "=";
        for (String part : cookieHeader.split(";")) {
            String trimmed = part.trim();
            if (trimmed.startsWith(prefix)) {
                String value = trimmed.substring(prefix.length()).trim();
                return value.isEmpty() ? null : value;
            }
        }
        return null;
    }

    private static void appendCookie(StringBuilder cookie, String name, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        if (cookie.length() > 0) {
            cookie.append("; ");
        }
        cookie.append(name).append('=').append(value);
    }
}
