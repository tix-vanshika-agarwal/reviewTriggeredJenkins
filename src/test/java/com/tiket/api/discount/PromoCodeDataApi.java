package com.tiket.api.discount;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.discount.PromoCodeDataBody;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record PromoCodeDataApi(Inputs inputs) implements BaseApi {

    private static final String BUDGET_ID_GK = "6936556fa115fb0020e8b2c2";
    private static final String BUDGET_ID_PREPROD = "693a67849d835de7a371390c";
    private static final String BUDGET_ID_PROD = "";

    public record Inputs(
        String username,
        String accountId,
        String tags,
        String quotaLevel,
        String quotaPromoCodeLevel,
        String promocode,
        String campaignName,
        String startDate,
        String endDate,
        String createdDate,
        String updatedDate,
        String paymentMethodName,
        List<String> binNumbers,
        List<String> binNumberListRaw,
        String validatedBy,
        String budgetId
    ) {
        public Inputs(
            String username,
            String accountId,
            String tags,
            String quotaLevel,
            String quotaPromoCodeLevel,
            String promocode,
            String campaignName,
            String startDate,
            String endDate,
            String createdDate,
            String updatedDate
        ) {
            this(username, accountId, tags, quotaLevel, quotaPromoCodeLevel, promocode,
                campaignName, startDate, endDate, createdDate, updatedDate, null, null, null, null, null);
        }
    }

    private String resolveBudgetId() {
        if (inputs.budgetId != null && !inputs.budgetId.isBlank()) {
            return inputs.budgetId;
        }
        return switch (Configs.ENVIRONMENT) {
            case PREPROD -> BUDGET_ID_PREPROD;
            case GK -> BUDGET_ID_GK;
            case PROD -> BUDGET_ID_PROD;
        };
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/discount/PromoCodeData_headers.properties");
        headers.put("X-Username", inputs.username);
        headers.put("X-Account-Id", inputs.accountId);

        JSONObject jsonObject = getStaticJson("src/test/resources/api/discount/PromoCodeData_body.json");
        Gson gson = new Gson();
        PromoCodeDataBody promoCodeDataBody = gson.fromJson(jsonObject.toString(), PromoCodeDataBody.class);

        promoCodeDataBody.tags = Collections.singletonList(inputs.tags);
        promoCodeDataBody.quotaLevel = inputs.quotaLevel;
        promoCodeDataBody.quotaPromoCodeLevel = inputs.quotaPromoCodeLevel;
        promoCodeDataBody.code = inputs.promocode;
        promoCodeDataBody.campaignName = inputs.campaignName;
        promoCodeDataBody.createdDate = inputs.createdDate;
        promoCodeDataBody.updatedDate = inputs.updatedDate;
        promoCodeDataBody.budgetId = resolveBudgetId();

        promoCodeDataBody.uuid = UUID.randomUUID().toString();
        if (promoCodeDataBody.ruleSets != null && !promoCodeDataBody.ruleSets.isEmpty()) {
            PromoCodeDataBody.RuleSet ruleSet = promoCodeDataBody.ruleSets.get(0);
            if (ruleSet.discountRules != null && !ruleSet.discountRules.isEmpty()) {
                ruleSet.discountRules.get(0).uuid = UUID.randomUUID().toString();
            }
        }

        for (PromoCodeDataBody.CampaignPeriod campaignPeriod : promoCodeDataBody.campaignPeriods) {
            campaignPeriod.startDate = inputs.startDate;
            campaignPeriod.endDate = inputs.endDate;
        }

        if (inputs.validatedBy != null && !inputs.validatedBy.isBlank()) {
            promoCodeDataBody.fraudPrevention.validatedBy = inputs.validatedBy;
            promoCodeDataBody.fraudPrevention.validatedByList = List.of(inputs.validatedBy);
        }

        if (inputs.paymentMethodName != null && !inputs.paymentMethodName.isBlank()) {
            PromoCodeDataBody.PaymentMethodObject paymentMethod = promoCodeDataBody.ruleSets.get(0)
                .discountRules.get(0).promoCodePriceRangeObject.paymentMethodObjectList.get(0);
            paymentMethod.paymentMethodName = inputs.paymentMethodName;
            if (inputs.binNumbers != null) {
                paymentMethod.binNumbers = inputs.binNumbers;
            }
            if (inputs.binNumberListRaw != null) {
                paymentMethod.binNumberListRaw = inputs.binNumberListRaw;
            }
        }

        UriHelper uriHelper = new UriHelper("https://promocode-be-svc.test-misc-cluster.tiket.com","https://promocode-be-svc.preprod-misc-cluster.tiket.com");
        ApiRequest apiRequest = new ApiRequest.Builder()
            .setUri(uriHelper.withEndpoint("tix-promocode/promoCodeData"))
            .setHeaderParams(headers)
            .setBody(gson.toJson(promoCodeDataBody, PromoCodeDataBody.class))
            .build();

        return apiTools.post(apiRequest);
    }
}
