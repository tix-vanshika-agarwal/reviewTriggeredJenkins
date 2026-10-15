package com.tiket.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record TestDataElement(
        String title,
        String description,
        String platform,
        boolean shouldRun,
        boolean gk,
        boolean prod,
        boolean preprod,
        boolean sanity,
        boolean regression,
        String tribe,
        String module,
        List<Map<String, String>> gkParams,
        List<Map<String, String>> prodParams,
        List<Map<String, String>> preprodParams
) {
    public static final Map<String, List<Map<String, String>>> titleParams = new HashMap<>();
}
