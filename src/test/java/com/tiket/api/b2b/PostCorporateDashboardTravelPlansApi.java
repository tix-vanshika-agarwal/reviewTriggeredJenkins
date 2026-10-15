package com.tiket.api.b2b;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.b2b.corporate.PostCreateTravelPlanBody;
import com.tiket.pojo.b2b.corporate.PostCreateTravelPlanMultiCityBody;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record PostCorporateDashboardTravelPlansApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String email,
            String name,
            String startDate,
            String endDate,
            /** {@link PostCreateTravelPlanBody.Configs} or {@link PostCreateTravelPlanMultiCityBody.Configs} (multi-city flight). */
            Object configs,
            List<Integer> approverIDs,
            long accountId,
            int corporateId,
            int accessRoleId,
            java.util.ArrayList<PostCreateTravelPlanBody.customFields> customFields // New field for customFields
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        // Load static headers from properties file
        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/TravelPlans/PostCorporateDashboardTravelPlansApi_headers.properties");
        headers.put("x-account-id", inputs.accountId);
        headers.put("x-corporate-id", inputs.corporateId);
        headers.put("x-corporate-accessrole-id", inputs.accessRoleId);
        headers.put("Authorization", inputs.accessToken);
        headers.put("email", inputs.email);

        // Construct body
        Map<String, Object> body = new HashMap<>();
        body.put("name", inputs.name);
        body.put("startDate", inputs.startDate);
        body.put("endDate", inputs.endDate);
        body.put("configs", inputs.configs);
        body.put("approverIDs", inputs.approverIDs);
        body.put("customFields", inputs.customFields);  // Empty list for now

        Gson gson = new Gson();
        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com","https://corporate.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/travel-plans"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(body))
                .build();

        return apiTools.post(request);
    }
}
