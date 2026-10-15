package com.tiket.api.payment;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.HashMap;
import java.util.Map;

public record PaymentGroupApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String orderHash,
            String referenceId,
            String platform,
            String accessToken,
            String currency,
            String countryCode,
            String lang
    ) {
        public Inputs(String orderHash, String referenceId, String platform, String accessToken, String currency, String countryCode) {
            this(orderHash, referenceId, platform, accessToken, currency, countryCode, "en");
        }
    }

    private static final Map<String, String> PLATFORM_USER_AGENTS = Map.of(
            "IOS", "tiketcom/ios-version (twh:20420882) - v4.75.0",
            "ANDROID", "tiketcom/android-version (twh:20296642) - v4.77.2-uat-HEAD",
            "MOBILE", "Mozilla/5.0 (iPhone; CPU iPhone OS 16_6 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.6 Mobile/15E148 Safari/604.1",
            "DESKTOP", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.0.0 Safari/537.36"
    );

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        String platformKey = inputs.platform().toUpperCase();

        Map<String, Object> headers = new HashMap<>();
        headers.put("User-Agent", PLATFORM_USER_AGENTS.get(platformKey));
        headers.put("channelId", platformKey);
        headers.put("X-Currency", inputs.currency());
        headers.put("currency", inputs.currency());
        headers.put("countryCode", inputs.countryCode());
        headers.put("X-Country-Code", inputs.countryCode());
        headers.put("X-Country-Id", inputs.countryCode());
        headers.put("cf-ipcountry", inputs.countryCode().toUpperCase());
        headers.put("lang", inputs.lang());
        headers.put("Accept-Language", inputs.lang());
        headers.put("Referer", uriHelper.withEndpoint(inputs.lang() + "-" + inputs.countryCode() + "/payment?order_id=" + inputs.referenceId() + "&order_hash=" + inputs.orderHash() + "&pagePreventInitialized=true"));
        if (inputs.accessToken() != null && !inputs.accessToken().isBlank()) {
            String token = inputs.accessToken().replace("Bearer ", "");
            headers.put("cookie", "session_access_token=" + token + ";");
        }

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("orderHash", inputs.orderHash());
        queryParams.put("referenceId", inputs.referenceId());

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-payment-core/payment/v4/payment-group"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
