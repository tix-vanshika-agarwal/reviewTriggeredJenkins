package com.tiket.api.payment;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record SubmitTiketPointsApi(Inputs inputs) implements BaseApi {
    
    public record Inputs(
        String referenceId,
        String orderHash
    ) {}
    
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper uriHelper = new UriHelper("https://payment-core-be-svc.test-payment-cluster.tiket.com","https://payment-core-be-svc.preprod-payment-cluster.tiket.com");
        Map<String, Object> headers = getStaticMap("src/test/resources/api/payment/SubmitTiketPointsApi_headers.properties");
        JSONObject body = getStaticJson("src/test/resources/api/payment/SubmitTiketPointsApi_body.json");

        body.put("referenceId", inputs.referenceId);
        body.put("orderHash", inputs.orderHash);

        ApiRequest request = new ApiRequest.Builder()
            .setUri(uriHelper.withEndpoint("tix-payment-core/payment/detail/TIXPOINT"))
            .setHeaderParams(headers)
            .setBody(body.toString())
            .build();
        
        return apiTools.put(request);
    }
}
