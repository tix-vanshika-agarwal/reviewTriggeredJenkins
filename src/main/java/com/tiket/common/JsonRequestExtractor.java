package com.tiket.common;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonRequestExtractor {

    private JsonRequestExtractor() {}

    public static String extractCheckoutTemplate(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONArray arr = root.optJSONArray("v:templateCode");
            if (arr == null || arr.isEmpty()) return null;

            return arr.getJSONObject(0).optString("body", null);
        } catch (Exception e) {
            return null;
        }
    }

    public static String extractIssuedTemplate(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONObject customArgs = root.optJSONObject("custom_args");
            if (customArgs == null) return null;

            return customArgs.optString("templateCode", null);
        } catch (Exception e) {
            return null;
        }
    }

    public static String extractETicketSubject(String json) {
        try {
            JSONObject root = new JSONObject(json);
            JSONArray subject = root.optJSONArray("subject");
            if (subject == null || subject.isEmpty()) return null;

            return subject.getJSONObject(0).optString("body", null);
        } catch (Exception e) {
            return null;
        }
    }
}
