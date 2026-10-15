package com.tiket.analytics.core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class AnalyticsFunctions {

    public static String decodeMixpanelRequestBody(String raw) {
        if (raw == null || raw.isBlank()) {
            return raw == null ? "" : raw;
        }

        String body = decodeUrlEncoding(raw).trim();
        String payload = body.startsWith("data=") ? body.substring(5) : body;

        if (payload.startsWith("[")) {
            return payload;
        }

        try {
            return new String(Base64.getDecoder().decode(payload), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return payload;
        }
    }

    private static String decodeUrlEncoding(String url) {
        try {
            String prevURL = "";
            String decodeURL = url;
            while (!prevURL.equals(decodeURL)) {
                prevURL = decodeURL;
                decodeURL = decodeURL.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                decodeURL = decodeURL.replaceAll("\\+", "%2B");
                decodeURL = URLDecoder.decode(decodeURL, StandardCharsets.UTF_8);
            }
            return decodeURL;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean haveEventInBody(String body, String eventType) {
        String events = getEventsFromBody(body);
        if (events.isBlank()) return false;
        JSONArray array = new JSONArray(events);
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            if (!o.has("event")) {
                continue; // skip engage/$set people updates
            }
            if (eventType.equals(o.getString("event"))) {
                return true;
            }
        }
        return false;
    }

    public static boolean haveFilteredEventInBody(String body, String eventType, String value) {
        String events = getEventsFromBody(body);
        if (events.isBlank()) return false;
        JSONArray array = new JSONArray(events);
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            if (!o.has("event")) {
                continue;
            }
            if (eventType.equals(o.getString("event"))) {
                JSONObject eventProperties = o.getJSONObject("properties");
                for (String key : eventProperties.keySet()) {
                    Object obj = eventProperties.get(key);
                    if (value.equals(String.valueOf(obj))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean haveEventPropertyInBody(String body, String eventType, String screenName, String property) {
        boolean result = false;
        String events = getEventsFromBody(body);
        if (events.isBlank()) return false;
        JSONArray array = new JSONArray(events);
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            if (!o.has("event")) {
                continue;
            }
            if (eventType.equals(o.getString("event"))) {
                JSONObject eventProperties = o.getJSONObject("properties");
                if (screenName.equals(eventProperties.getString("screenName"))) {
                    if (!eventProperties.optString(property).isEmpty()) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static boolean haveUserPropertyInBody(String body, String property) {
        boolean result = false;
        String events = getEventsFromBody(body);
        if (events.isBlank()) return false;
        JSONArray array = new JSONArray(events);
        for (int i = 0; i < array.length(); i++) {
            JSONObject event = array.getJSONObject(i);
            if (event.has("properties")) {
                JSONObject userProperties = event.getJSONObject("properties");
                if (userProperties.has(property)) {
                    if (!userProperties.optString(property).isEmpty()) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static boolean haveEventPropertyWithValue(String body, String eventType, String property, String value) {
        boolean result = false;
        String events = getEventsFromBody(body);
        if (events.isBlank()) return false;
        JSONArray array = new JSONArray(events);
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            if (!o.has("event")) {
                continue;
            }
            if (eventType.equals(o.getString("event"))) {
                JSONObject eventProperties = o.getJSONObject("properties");
                if (eventProperties.has(property)) {
                    String propertyValueText = String.valueOf(eventProperties.get(property));
                    String[] propertyValueArr = propertyValueText.split(";");
                    String propertyValue = propertyValueArr[0].trim();
                    if (value.equals(propertyValue)) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static boolean filteredEventHaveEventPropertyWithValue(String body, String eventType, String screenName, String property, String value) {
        boolean result = false;
        String events = getEventsFromBody(body);
        if (events.isBlank()) return false;
        JSONArray array = new JSONArray(events);
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            if (!o.has("event")) {
                continue;
            }
            if (eventType.equals(o.getString("event"))) {
                JSONObject eventProperties = o.getJSONObject("properties");
                if (!eventProperties.has("screenName")) {
                    continue;
                }
                if (screenName.equals(String.valueOf(eventProperties.get("screenName")))) {
                    if (eventProperties.has(property)) {
                        String propertyValueText = String.valueOf(eventProperties.get(property));
                        String[] propertyValueArr = propertyValueText.split(";");
                        String propertyValue = propertyValueArr[0].trim();
                        if (value.equals(propertyValue)) {
                            result = true;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    // Generic counterparts of haveFilteredEventInBody/filteredEventHaveEventPropertyWithValue/haveEventPropertyInBody
    // for trackers that key events on a combination of properties other than "screenName" alone (e.g. a component
    // fires the same event+sectionName for every sub-action, so sectionName+eventAction together identify one).
    private static boolean matchesAllFilters(JSONObject eventProperties, Map<String, String> filters) {
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            if (!eventProperties.has(filter.getKey())) {
                return false;
            }
            if (!filter.getValue().equals(String.valueOf(eventProperties.get(filter.getKey())))) {
                return false;
            }
        }
        return true;
    }

    public static boolean haveFilteredEventInBodyByProperties(String body, String eventType, Map<String, String> filters) {
        String events = getEventsFromBody(body);
        if (events.isBlank()) return false;
        JSONArray array = new JSONArray(events);
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            if (!o.has("event")) {
                continue;
            }
            if (!eventType.equals(o.getString("event"))) {
                continue;
            }
            JSONObject eventProperties = o.getJSONObject("properties");
            if (matchesAllFilters(eventProperties, filters)) {
                return true;
            }
        }
        return false;
    }

    public static boolean filteredByPropertiesHaveEventPropertyWithValue(String body, String eventType, Map<String, String> filters, String property, String value) {
        String events = getEventsFromBody(body);
        if (events.isBlank()) return false;
        JSONArray array = new JSONArray(events);
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            if (!o.has("event")) {
                continue;
            }
            if (!eventType.equals(o.getString("event"))) {
                continue;
            }
            JSONObject eventProperties = o.getJSONObject("properties");
            if (!matchesAllFilters(eventProperties, filters)) {
                continue;
            }
            if (!eventProperties.has(property)) {
                continue;
            }
            String propertyValueText = String.valueOf(eventProperties.get(property));
            String propertyValue = propertyValueText.split(";")[0].trim();
            if (value.equals(propertyValue)) {
                return true;
            }
        }
        return false;
    }

    public static boolean haveUserPropertyWithValue(String body, String property, String value) {
        boolean result = false;
        String events = getEventsFromBody(body);
        if (events.isBlank()) return false;
        JSONArray array = new JSONArray(events);
        for (int i = 0; i < array.length(); i++) {
            JSONObject event = array.getJSONObject(i);
            if (event.has("properties")) {
                JSONObject userProperties = event.getJSONObject("properties");
                if (userProperties.has(property)) {
                    if (value.equals(userProperties.getString(property))) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static String getEventsFromBody(String body) {
        if (body == null) return "";

        body = decodeMixpanelRequestBody(body).trim();

        int start = body.indexOf('[');
        if (start == -1) return ""; // if no entry bracket, return empty string

        String jsonArray = body.substring(start);

        int end = jsonArray.lastIndexOf(']');
        if (end == -1) return ""; // if no closing bracket, return empty string

        jsonArray = jsonArray.substring(0, end + 1);

        try {
            new org.json.JSONArray(jsonArray); // validate JSON array
            return jsonArray;
        } catch (org.json.JSONException e) { // if it's invalid JSON, return empty string
            return "";
        }
    }
}