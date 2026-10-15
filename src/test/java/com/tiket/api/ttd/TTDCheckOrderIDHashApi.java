package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

/**
 * @param useCookieAuth when {@code true}, the access token is sent as the {@code session_access_token} cookie
 *                      instead of the {@code Authorization} header (e.g. for B2B corporate accounts)
 */
public record TTDCheckOrderIDHashApi(Inputs inputs, boolean useCookieAuth) implements BaseApi {

    /** Defaults to sending the access token in the {@code Authorization} header. */
    public TTDCheckOrderIDHashApi(Inputs inputs) {
        this(inputs, false);
    }

    public record Inputs(
            String accessToken,
            String createOrderID
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/TTDCheckOrderIDHash_headers.properties");
        if (useCookieAuth) {
            headers.put("cookie", "session_access_token=" + inputs.accessToken + ";");
        } else {
            headers.put("Authorization", inputs.accessToken);
        }

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-events-v2-order/v1/orders/" + inputs.createOrderID))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
