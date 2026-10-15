package com.tiket.api.auth;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONArray;

import java.util.Map;

public record POSTGetAdminAccessTokenDashboardApi() implements BaseApi {

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://gql-dashboard-gatotkaca.tiket.com","https://dash-graphql-be-svc.preprod-platform-cluster.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/POSTGetAdminAccessTokenDashboard_header.properties");

        JSONArray body = getStaticJsonArray("src/test/resources/api/auth/POSTGetAdminAccessTokenDashboard_body.json");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/")).setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
