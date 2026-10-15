package com.tiket.api.loyalty;

import com.tiket.api.auth.AdminDashboardLoginResponseParser;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record ApproveTiketPointApi (Inputs inputs) implements BaseApi {
    public record Inputs(
            long id,
            String accessToken
    ){}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/ApproveTiketPointApi_headers.properties");
        AdminDashboardLoginResponseParser.applyDashboardAuth(headers, inputs.accessToken);
        headers.put("origin",uriHelper.getUri());
        headers.put("referer", uriHelper.getUri() + "/admin/care/loyalty/approval-point");

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/loyalty/ApproveTiketPointApi_queryParams.properties");
        queryParams.put("approvalStatus", "true");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-loyalty-core/support/pending-tasks/"+inputs.id))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.put(request);
    }
}
