package com.tiket.api.utilities;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record InboxApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            long accountId,
            String subtitle
    ){}
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/utilities/NewInbox_headers.properties");
        JSONObject body = getStaticJson("src/test/resources/api/utilities/NewInbox_body.json");
        body.put("subtitle", inputs.subtitle);
        body.put("accountId", inputs.accountId);

        UriHelper helper = new UriHelper("https://inbox-be-svc.test-platform-cluster.tiket.com","https://inbox-be-svc.preprod-platform-cluster.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-inbox/inbox"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();
        return apiTools.post(request);
    }
}

