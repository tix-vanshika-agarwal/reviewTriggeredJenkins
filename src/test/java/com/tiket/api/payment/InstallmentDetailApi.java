package com.tiket.api.payment;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public record InstallmentDetailApi(Inputs inputs) implements BaseApi {

    private static final String ENDPOINT = "ms-gateway/tix-payment-core/installment/detail";
    private static final String BODY_PATH = "src/test/resources/api/payment/InstallmentDetailApi_body.json";
    private static final String HEADERS_PATH = "src/test/resources/api/payment/InstallmentDetailApi_headers.properties";

    public record Inputs(
            long referenceId,
            String cardNumber,
            String accessToken,
            String orderHash,
            String currency,
            String countryCode,
            String lang
    ) {
        public Inputs(long referenceId, String cardNumber, String accessToken, String orderHash) {
            this(referenceId, cardNumber, accessToken, orderHash, "IDR", "sg", "en");
        }
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = new HashMap<>(getStaticMap(HEADERS_PATH));
        String deviceId = CoreFunctions.generateDeviceId();

        headers.put("deviceid", deviceId);
        headers.put("currency", inputs.currency());
        headers.put("X-Currency", inputs.currency());
        headers.put("countrycode", inputs.countryCode());
        headers.put("X-Country-Code", inputs.countryCode());
        headers.put("X-Country-Id", inputs.countryCode());
        headers.put("cf-ipcountry", inputs.countryCode().toUpperCase());
        headers.put("lang", inputs.lang());
        headers.put("Accept-Language", inputs.lang());
        headers.put("Referer", uriHelper.withEndpoint(inputs.lang() + "-" + inputs.countryCode()
                + "/payment?order_id=" + inputs.referenceId()
                + "&order_hash=" + inputs.orderHash()
                + "&pagePreventInitialized=true"));

        if (inputs.accessToken() != null && !inputs.accessToken().isBlank()) {
            String token = inputs.accessToken().replace("Bearer ", "");
            headers.put("Cookie", "session_access_token=" + token + "; device_id=" + deviceId + ";");
        }

        JSONObject body = getStaticJson(BODY_PATH);
        body.put("referenceId", inputs.referenceId());
        body.put("cardNumber", inputs.cardNumber());

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("version", "v4");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint(ENDPOINT))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
