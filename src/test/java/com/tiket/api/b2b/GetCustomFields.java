package com.tiket.api.b2b;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

public record GetCustomFields(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            long accountId,
            int corporateId,
            int accessRoleId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        // Load static headers from properties file
        var headers = getStaticMap("src/test/resources/api/b2b/TravelPlans/GetCustomFields_headers.properties");
        headers.put("x-account-id", inputs.accountId);
        headers.put("x-corporate-id", inputs.corporateId);
        headers.put("x-corporate-accessrole-id", inputs.accessRoleId);
        headers.put("Authorization", inputs.accessToken);

        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com","https://corporate.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/custom-fields?page=TRAVEL_PLAN_REQUEST"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
