package com.tiket.api.discovery;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public record PopupOrderApi(Inputs inputs) implements BaseApi {

    public record Inputs(String cookieHeader, String popupId, String orderGroupId) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/discovery/PopupOrderApi_headers.properties");
        headers.put("cookie", inputs.cookieHeader());

        JSONObject order = new JSONObject().put("priorities", new JSONArray().put(inputs.popupId()));
        JSONObject body = new JSONObject()
                .put("id", inputs.orderGroupId())
                .put("orders", new JSONArray().put(order));

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-promotion-page/admin/popups/order"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
