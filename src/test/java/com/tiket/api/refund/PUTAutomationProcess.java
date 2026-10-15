package com.tiket.api.refund;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record PUTAutomationProcess(Inputs inputs) implements BaseApi {
    
    public record Inputs(
        String automationCommand,
        long refundRequestId
    ) {}

    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper uriHelper = new UriHelper("https://refund-v4-be-svc.test-payment-cluster.tiket.com","https://refund-v4-be-svc.preprod-payment-cluster.tiket.com");
        Map<String, Object> headers = getStaticMap("src/test/resources/api/refund/PUTAutomationProcess_headers.properties");
        JSONObject body = getStaticJson("src/test/resources/api/refund/PUTAutomationProcess_body.json");
        
        body.put("automationCommand", inputs.automationCommand);
        body.put("refundRequestId", inputs.refundRequestId);

        ApiRequest request = new ApiRequest.Builder()
            .setUri(uriHelper.withEndpoint("tix-refund/svc/refunds/automation/process"))
            .setHeaderParams(headers)
            .setBody(body.toString())
            .build();

        return apiTools.put(request);
    }
}
