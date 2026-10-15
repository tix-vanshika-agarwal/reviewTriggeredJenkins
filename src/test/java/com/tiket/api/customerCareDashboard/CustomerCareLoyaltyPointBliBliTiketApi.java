package com.tiket.api.customerCareDashboard;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public record CustomerCareLoyaltyPointBliBliTiketApi(Inputs inputs) implements BaseApi {
    
    public record Inputs(String dashboardToken,String accountId){}

    @Override
    @SneakyThrows
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://gql-dashboard-gatotkaca.tiket.com","https://dash-graphql-be-svc.preprod-platform-cluster.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/customerCareDashboard/loyaltyPoint/loyaltyPoint_headers.properties");
        headers.put("Authorization", "Bearer "+inputs.dashboardToken);

        JSONArray payload = getStaticJsonArray("src/test/resources/api/customerCareDashboard/loyaltyPoint/loyaltyPoint_body.json");
        JSONObject variables1 = payload.getJSONObject(0).getJSONObject("variables");
        JSONObject variables2 = payload.getJSONObject(1).getJSONObject("variables");
        variables1.put("accountId", inputs.accountId);
        variables2.put("accountId", inputs.accountId);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/"))
                .setHeaderParams(headers)
                .setBody(payload.toString())
                .build();

        return apiTools.post(request);
    }
}