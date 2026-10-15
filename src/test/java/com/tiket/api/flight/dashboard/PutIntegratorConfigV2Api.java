package com.tiket.api.flight.dashboard;

import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record PutIntegratorConfigV2Api(Inputs inputs) implements BaseApi {
    /**
     * @param urlSuffix     appended to {@code bookingUrl} to break the endpoint; ignored when
     *                      {@code replacementUrl} is set
     * @param replacementUrl used verbatim as the new URL when set, for pointing a supplier at a
     *                      stub that lives on its own host
     */
    public record Inputs(String cookieHeader, String urlSuffix, String configId,
                         String action, String distributionType, String supplierCode, String bookingUrl,
                         String replacementUrl) {}


    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper uriHelper = new UriHelper("https://gatotkaca.tiket.com", "https://preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/dashboard/PutIntegratorConfigV2Api_headers.properties");
        if (inputs.cookieHeader != null && !inputs.cookieHeader.isBlank()) {
            headers.put("Cookie", inputs.cookieHeader);
        }


        JSONObject body = getStaticJson("src/test/resources/api/flight/dashboard/PutIntegratorConfigV2Api_body.json");
        body.put("action", inputs.action);
        body.put("distributionType", inputs.distributionType);
        body.put("supplierCode", inputs.supplierCode);
        if (inputs.replacementUrl != null && !inputs.replacementUrl.isBlank()) {
            // A stub has its own host, so neither the suffix nor the GK cluster rewrite applies.
            body.put("url", inputs.replacementUrl);
        } else {
            String baseUrl = (inputs.bookingUrl == null || inputs.bookingUrl.isBlank())
                    ? body.getString("url") : inputs.bookingUrl;

            if (Configs.ENVIRONMENT == Environment.GK) {
                baseUrl = baseUrl.replace("preprod-flight-cluster", "test-flight-cluster");
            }
            String suffix = inputs.urlSuffix == null ? "" : inputs.urlSuffix;
            body.put("url", baseUrl + suffix);
        }

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/tix-gateway-dashboard/integrator-config-v2/" + inputs.configId))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.put(request);
    }
}