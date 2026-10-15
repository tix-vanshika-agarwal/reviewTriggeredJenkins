package com.tiket.api.loyalty;

import com.tiket.api.auth.AdminDashboardLoginResponseParser;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record ActivePointDetailsApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String processType,
            String accessToken,
            String username
    ){}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/ActivePointDetailsApi_headers.properties");
        AdminDashboardLoginResponseParser.applyDashboardAuth(headers, inputs.accessToken);
        headers.put("X-Username", inputs.username);
        headers.put("referer", uriHelper.getUri() + "/admin/care/loyalty/bliblitiketpoint");

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/loyalty/ActivePointDetailsApi_queryParams.properties");
        queryParams.put("processType", inputs.processType);
        queryParams.put("username", inputs.username);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-loyalty-core/support/point-detail"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
