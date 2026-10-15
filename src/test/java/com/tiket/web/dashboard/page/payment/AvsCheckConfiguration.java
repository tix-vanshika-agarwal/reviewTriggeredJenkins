package com.tiket.web.dashboard.page.payment;

import com.google.gson.Gson;
import com.tiket.model.Session;
import com.tiket.pojo.payment.AvsRuleConfigListResponse;
import com.tiket.pojo.payment.AvsRuleConfigResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.is;

public class AvsCheckConfiguration<T extends WebDriver> extends BasePage<T> {

    private static final Gson gson = new Gson();
    private static final String AVS_RULE_CONFIG_API_PATH = "/ms-gateway/tix-payment-dashboard/avs-rule-config";

    public AvsCheckConfiguration(Session<T> session) {
        super(session);
    }

    public void ensureAvsRuleEnabledForCard(String cardNumber, String avsRuleIdActive, String avsRuleIdInactive, String avsBin) {
        List<AvsRuleConfigResponse> rules = getAllAvsRules();
        String binToEnsure = resolveBin(cardNumber, avsBin);

        AvsRuleConfigResponse inactiveRule = findRuleById(rules, avsRuleIdInactive)
                .orElseThrow(() -> new IllegalStateException("Inactive AVS rule not found: " + avsRuleIdInactive));
        AvsRuleConfigResponse activeRule = findRuleById(rules, avsRuleIdActive)
                .orElseThrow(() -> new IllegalStateException("Active AVS rule not found: " + avsRuleIdActive));

        if (!needsRuleUpdate(activeRule, binToEnsure, cardNumber)) {
            logger.log("Active AVS rule already enabled and covers test card BIN: " + activeRule.ruleName);
            return;
        }

        if (!inactiveRule.avsCheckEnable) {
            logger.log("Active AVS rule needs update and inactive rule is disabled, enabling via PUT: " + inactiveRule.ruleName);
            enableRuleIfNeeded(inactiveRule, binToEnsure, cardNumber);
            return;
        }

        logger.log("Active AVS rule needs update, enabling via PUT: " + activeRule.ruleName);
        enableRuleIfNeeded(activeRule, binToEnsure, cardNumber);
    }

    public List<AvsRuleConfigResponse> getAllAvsRules() {
        JSONObject envelope = executeClientApi("GET", AVS_RULE_CONFIG_API_PATH + "?ruleName=&page=0&size=999&sortDirection=DESC", null);
        int httpStatus = envelope.optInt("status", -1);
        String body = envelope.optString("body", "");
        if (httpStatus != 200) {
            throw new IllegalStateException("AVS rule config GET returned HTTP " + httpStatus + " -> " + body);
        }

        AvsRuleConfigListResponse response = gson.fromJson(body, AvsRuleConfigListResponse.class);
        if (response.data == null || response.data.content == null) {
            throw new IllegalStateException("AVS rule config GET response has no content -> " + body);
        }
        return response.data.content;
    }

    private Optional<AvsRuleConfigResponse> findRuleById(List<AvsRuleConfigResponse> rules, String avsRuleId) {
        return rules.stream()
                .filter(rule -> avsRuleId.equals(rule.id))
                .findFirst();
    }

    private void enableRuleIfNeeded(AvsRuleConfigResponse rule, String binToEnsure, String cardNumber) {
        if (!needsRuleUpdate(rule, binToEnsure, cardNumber)) {
            logger.log("AVS rule already active and covers test card BIN: " + rule.ruleName);
            return;
        }

        logger.log("Updating AVS rule " + rule.ruleName + " to enable AVS and ensure BIN " + binToEnsure);
        enableRule(rule, binToEnsure);
    }

    private boolean needsRuleUpdate(AvsRuleConfigResponse rule, String binToEnsure, String cardNumber) {
        boolean binCovered = ruleCoversCard(rule, cardNumber);
        boolean binListed = rule.bins != null && rule.bins.contains(binToEnsure);
        return !rule.avsCheckEnable || !binCovered || !binListed;
    }

    private void enableRule(AvsRuleConfigResponse rule, String binToEnsure) {
        Set<String> bins = new LinkedHashSet<>();
        if (rule.bins != null) {
            bins.addAll(rule.bins);
        }
        bins.add(binToEnsure);

        JSONObject putBody = buildPutBody(rule, bins, true);
        JSONObject response = updateAvsRuleViaClientApi(rule.id, putBody.toString());

        Assertion.assertThat("AVS rule update API should return SUCCESS", response.optString("code"), is("SUCCESS"));
        Assertion.assertThat("AVS rule should be enabled after update",
                response.getJSONObject("data").getBoolean("avsCheckEnable"), is(true));
    }

    private boolean ruleCoversCard(AvsRuleConfigResponse rule, String cardNumber) {
        if (rule.bins == null || rule.bins.isEmpty()) {
            return false;
        }
        return rule.bins.stream().anyMatch(cardNumber::startsWith);
    }

    private String resolveBin(String cardNumber, String avsBin) {
        if (avsBin != null && !avsBin.isBlank()) {
            return avsBin;
        }
        if (cardNumber.length() >= 8) {
            return cardNumber.substring(0, 8);
        }
        return cardNumber.substring(0, Math.min(6, cardNumber.length()));
    }

    private JSONObject buildPutBody(AvsRuleConfigResponse rule, Set<String> bins, boolean avsCheckEnable) {
        JSONObject body = new JSONObject();
        body.put("id", rule.id);
        body.put("ruleName", rule.ruleName);
        body.put("avsCheckEnable", avsCheckEnable);
        body.put("bins", new JSONArray(new ArrayList<>(bins)));
        body.put("issuerCountries", rule.issuerCountries == null ? JSONObject.NULL : new JSONArray(rule.issuerCountries));
        body.put("cardTypes", rule.cardTypes == null ? JSONObject.NULL : new JSONArray(rule.cardTypes));
        body.put("cardSchemes", rule.cardSchemes == null ? JSONObject.NULL : new JSONArray(rule.cardSchemes));
        body.put("currency", rule.currency == null ? JSONObject.NULL : new JSONArray(rule.currency));

        JSONObject avsCheck = new JSONObject();
        if (rule.avsCheck != null) {
            avsCheck.put("type", rule.avsCheck.type);
            avsCheck.put("checkFieldType", new JSONArray(rule.avsCheck.checkFieldType));
        } else {
            avsCheck.put("type", "MANDATORY");
            avsCheck.put("checkFieldType", new JSONArray(List.of("postalCode", "address")));
        }
        body.put("avsCheck", avsCheck);
        return body;
    }

    private JSONObject updateAvsRuleViaClientApi(String ruleId, String jsonBody) {
        JSONObject envelope = executeClientApi("PUT", AVS_RULE_CONFIG_API_PATH + "/" + ruleId, jsonBody);
        int httpStatus = envelope.optInt("status", -1);
        String body = envelope.optString("body", "");
        if (httpStatus != 200) {
            throw new IllegalStateException("AVS rule config PUT returned HTTP " + httpStatus + " -> " + body);
        }
        return new JSONObject(body);
    }

    private JSONObject executeClientApi(String method, String path, String jsonBody) {
        String escapedBody = jsonBody == null ? "null" : JSONObject.quote(jsonBody);
        String script = "var xhr = new XMLHttpRequest();"
                + "xhr.open('" + method + "', '" + path + "', false);"
                + "xhr.setRequestHeader('accept', '*/*');"
                + "xhr.setRequestHeader('content-type', 'application/json;charset=UTF-8');"
                + "xhr.setRequestHeader('lang', 'en');"
                + "xhr.setRequestHeader('x-audience', 'tiket.com');"
                + "xhr.setRequestHeader('x-cookie-session-v2', 'true');"
                + "if (" + escapedBody + " !== null) { xhr.send(" + escapedBody + "); } else { xhr.send(null); }"
                + "return JSON.stringify({status: xhr.status, body: xhr.responseText});";

        String raw = String.valueOf(((JavascriptExecutor) driver).executeScript(script));
        logger.log("AVS rule config " + method + " " + path + " raw -> " + raw);
        return new JSONObject(raw);
    }
}
