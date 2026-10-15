package com.tiket.api.b2b;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GetTravelPlanDetailApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String email,
            long accountId,
            int corporateRoleId,
            int corporateId,
            int travelPlanId
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com","https://corporate.preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/TravelPlans/GetTravelPlanSearchApi_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("email",inputs.email);
        headers.put("x-account-id", inputs.accountId);
        headers.put("x-corporate-accessrole-id", inputs.corporateRoleId);
        headers.put("x-corporate-id", inputs.corporateId);
        headers.put("referer",uriHelper.getUri() + "/travel-plan/my-requests/"+inputs.travelPlanId);


        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/travel-plans/"+inputs.travelPlanId+"?approverView=false"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
