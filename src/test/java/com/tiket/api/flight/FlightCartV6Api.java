package com.tiket.api.flight;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.tiket.core.CoreFunctions.log;

public record FlightCartV6Api(Inputs inputs) implements BaseApi {
    public static final String DEFAULT_FULL_REFUND_CREDENTIAL_CODE = "tiket-basic-internal";

    public record CartDetail(
            String flightId,
            String supplierId,
            String fareBasisCode,
            boolean comboMC,
            boolean isFullRefund
    ) {}

    public record Inputs(
            List<CartDetail> cartDetails,
            String fullRefundCredentialCode,
            String accessToken
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightCart_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        String credentialCode = inputs.fullRefundCredentialCode == null || inputs.fullRefundCredentialCode.isBlank()
                ? DEFAULT_FULL_REFUND_CREDENTIAL_CODE
                : inputs.fullRefundCredentialCode;

        JSONArray cartDetails = new JSONArray();
        for (CartDetail detail : inputs.cartDetails) {
            cartDetails.put(new JSONObject()
                    .put("flightId", detail.flightId())
                    .put("supplierRequest", new JSONObject().put("supplierId", detail.supplierId()))
                    .put("isFullRefund", detail.isFullRefund())
                    .put("fullRefundCredentialCode", credentialCode)
                    .put("fareBasisCode", detail.fareBasisCode())
                    .put("comboMC", detail.comboMC()));
        }

        JSONObject body = getStaticJson("src/test/resources/api/flight/FlightCartV6_body.json");
        body.put("cartDetails", cartDetails);

        log("FlightCartV6Api body: " + body);
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-flight-cart/v6/cart"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
