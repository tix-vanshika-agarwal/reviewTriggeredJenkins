package com.tiket.api.loyalty;

import com.tiket.api.auth.AdminDashboardLoginResponseParser;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record TiketPointsApprovalListApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String username
    ){}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/TiketPointsApperverListApi_headers.properties");
        AdminDashboardLoginResponseParser.applyDashboardAuth(headers, inputs.accessToken);
        headers.put("referer", uriHelper.getUri() + "/admin/care/loyalty/approval-point");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-loyalty-core/support/approval-list"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
