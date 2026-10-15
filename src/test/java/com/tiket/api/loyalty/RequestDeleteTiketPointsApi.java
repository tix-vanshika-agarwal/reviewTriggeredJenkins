package com.tiket.api.loyalty;

import com.tiket.api.auth.AdminDashboardLoginResponseParser;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record RequestDeleteTiketPointsApi (Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            long pointId,
            String pdProcessType,
            String username
    ) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/RequestDeleteTiketPointsApi_headers.properties");
        AdminDashboardLoginResponseParser.applyDashboardAuth(headers, inputs.accessToken);
        headers.put("referer", uriHelper.getUri()+ "/admin/care/loyalty/delete-point?pointType="+inputs.pdProcessType+"&username="+inputs.username);
        headers.put("origin",uriHelper.getUri());

        JSONObject body = getStaticJson("src/test/resources/api/loyalty/RequestDeleteTiketPointsApi_body.json");
        body.put("type", "DELETE");
        body.put("pointId", inputs.pointId);
        body.remove("signal");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-loyalty-core/support/pending-tasks"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();
        return apiTools.post(request);
    }
}
