package com.tiket.api.b2b;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.b2b.corporate.EmployeeData;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

public record PostPatchTravelPlanApprovalApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String email,
            int travelPlanId,
            long accountId,
            EmployeeData.Data employeeData
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        // Construct URL helper
        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com","https://corporate.preprod.tiket.com");

        // Load static headers
        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/TravelPlans/PostPatchTravelPlanApprovalApi_headers.properties");

        // Add dynamic headers
        headers.put("Authorization", inputs.accessToken);
        headers.put("x-account-id", inputs.accountId);
        headers.put("x-corporate-id", inputs.employeeData.corporateID);
        headers.put("x-corporate-accessrole-id", inputs.employeeData.accessRoleId);
        headers.put("email",inputs.email);

        // Create JSON body
        Map<String, Object> body = new HashMap<>();
        body.put("status", "approved");
        body.put("approverView",true);
        body.put("notes", "");

        Gson gson = new Gson();

        // Build the API request
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/travel-plans/" + inputs.travelPlanId))
                .setHeaderParams(headers)
                .setBody(gson.toJson(body))
                .build();

        // PATCH request
        return apiTools.patch(request);
    }
}
