package com.tiket.api.refund;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

/**
 * User-facing e-refund status/progress call:
 * {@code GET /ms-gateway/tix-refund/external/refunds/{requestHash}/track}.
 *
 * <p>Returns the live refund request (the same object shape used across the refund service) keyed by the
 * {@code requestHash} returned when the e-refund was created. The fields this flow polls are
 * {@code data.lastStatus} (to know when the refund is force-submit-ready, i.e. {@code on_process*}) and
 * {@code data.orderRefundStatus} (which becomes {@code refunded} once the refund settles). Authenticated with
 * the <b>user</b> access token.
 */
public record GetRefundTrackApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String requestHash
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://gatotkaca.tiket.com/ms-gateway", "https://preprod.tiket.com/ms-gateway");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/refund/RefundExternal_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-refund/external/refunds/" + inputs.requestHash + "/track"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
