package com.tiket.api.discount;

import com.google.gson.Gson;
import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.discount.GiftVoucherCreateBody;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record GiftVoucherCreateApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String username,
            String idempotentKey,
            String apiKey,
            int discountAmount,
            int cashbackAmount,
            int maximumVoucherCashbackAmount,
            int discountPercentage,
            int maximumVoucherAmount,
            int minimumTransactionAmount,
            String currency,
            String rangeAddDate,
            String discountScheme,
            String giftVoucherAdjustmentCode,
            List<String> userEmails,
            List<String> validityRestriction
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/discount/GiftVoucherCreate_headers.properties");
        headers.put("X-Username", inputs.username);
        headers.put("X-IDEMPOTENT-KEY", inputs.idempotentKey);
        headers.put("X-AUTH-API-KEY", inputs.apiKey);

        JSONObject jsonObject = getStaticJson("src/test/resources/api/discount/GiftVoucherCreate_body.json");
        Gson gson = new Gson();
        GiftVoucherCreateBody giftVoucherCreateBody = gson.fromJson(jsonObject.toString(), GiftVoucherCreateBody.class);
        giftVoucherCreateBody.discountAmount = inputs.discountAmount;
        giftVoucherCreateBody.discountPercentage = inputs.discountPercentage;
        giftVoucherCreateBody.maximumVoucherAmount = inputs.maximumVoucherAmount;
        giftVoucherCreateBody.minimumTransactionAmount = inputs.minimumTransactionAmount;
        giftVoucherCreateBody.userEmails = inputs.userEmails;
        giftVoucherCreateBody.currency = inputs.currency;
        String untilDate = CoreFunctions.generateRandomDate(inputs.rangeAddDate);
        giftVoucherCreateBody.validUntilDate = CoreFunctions.convertDateFormat(untilDate, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");
        giftVoucherCreateBody.validityRestriction = inputs.validityRestriction;
        giftVoucherCreateBody.cashbackAmount = inputs.cashbackAmount;
        giftVoucherCreateBody.maximumVoucherCashbackAmount = inputs.maximumVoucherCashbackAmount;
        giftVoucherCreateBody.discountScheme = inputs.discountScheme;
        giftVoucherCreateBody.giftVoucherAdjustmentCode = inputs.giftVoucherAdjustmentCode;

        UriHelper uriHelper = new UriHelper("https://gift-voucher-be-svc.test-misc-cluster.tiket.com","https://gift-voucher-be-svc.preprod-misc-cluster.tiket.com");
        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("tix-gift-voucher/api/v3/generate"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(giftVoucherCreateBody, GiftVoucherCreateBody.class))
                .build();

        return apiTools.post(apiRequest);
    }
}
