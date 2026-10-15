package com.tiket.api.intools;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public record DeleteOrderIdApi(Inputs inputs) implements BaseApi {

    public record Inputs(String softblockId, String orderId) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/intools/DeleteOrderId.properties");

        UriHelper uriHelper = new UriHelper(
                "https://deposit-be-http-svc.test-platform-cluster.tiket.com",
                "https://deposit-be-http-svc.preprod-platform-cluster.tiket.com"
        );

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/tix-deposit/transaction/"
                        + URLEncoder.encode(inputs.softblockId, StandardCharsets.UTF_8) + "/"
                        + URLEncoder.encode(inputs.orderId, StandardCharsets.UTF_8) + "?deleteBy=orderId"))
                .setHeaderParams(headers)
                .build();

        return apiTools.delete(request);
    }

}