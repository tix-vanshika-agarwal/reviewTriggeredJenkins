package com.tiket.api.loyalty;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record AddActivePointsApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            int pdAccountId,
            int pdPoint,
            String pdProcessType,
            int pdRatio,
            String username
            ) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/AddActivePoint_headers.properties");
        headers.put("X-Username", inputs.username);

        JSONObject body = getStaticJson("src/test/resources/api/loyalty/AddActivePoint_body.json");
        body.put("pdAccountId", inputs.pdAccountId);
        body.put("pdPoint", inputs.pdPoint);
        body.put("pdProcessType", inputs.pdProcessType);
        body.put("pdRatio", inputs.pdRatio);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-loyalty-core/add-active-point")).setHeaderParams(headers)
                .setBody(body.toString())
                .build();
        ;

        return apiTools.post(request);
    }
}
