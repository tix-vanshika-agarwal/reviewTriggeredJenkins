package com.tiket.api.b2b;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record AcceptBookingDashboardApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken1,
            String accessToken2,
            String orderID
            ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/AcceptBookingDashboard_headers.properties");
        headers.put("authorization", "Bearer "+ inputs.accessToken1);
        headers.put("token", inputs.accessToken2);

        JSONObject body = getStaticJson("src/test/resources/api/b2b/AcceptBookingDashboard_Body.json");

        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com","https://corporate.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/corporate-orders/"+inputs.orderID))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.patch(request);
    }

}
