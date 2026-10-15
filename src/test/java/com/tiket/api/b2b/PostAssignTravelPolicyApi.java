package com.tiket.api.b2b;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.b2b.corporate.EmployeeData;
import com.tiket.pojo.b2b.corporate.PostAssignTravelPolicyBody;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public record PostAssignTravelPolicyApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            EmployeeData employeeDataAdmin,
            EmployeeData employeeDataUser,
            long accountIdAdmin,
            int policyId
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/TravelPolicy/PostAssignTravelPolicyApi_headers.properties");
        headers.put("x-account-id", inputs.accountIdAdmin);
        headers.put("x-corporate-id", inputs.employeeDataAdmin.data.corporateID);
        headers.put("X-Corporate-Employee-ID", inputs.employeeDataUser.data.employeeID);


        JSONObject body = getStaticJson("src/test/resources/api/b2b/TravelPolicy/PostAssignTravelPolicyApi_body.json");
        Gson gson = new Gson();
        PostAssignTravelPolicyBody postAssignTravelPolicyBody = gson.fromJson(body.toString(), PostAssignTravelPolicyBody.class);
        ArrayList<Integer> employeeIds = new ArrayList<>();
        employeeIds.add(inputs.employeeDataUser.data.employeeID);
        postAssignTravelPolicyBody.employeeIDs = employeeIds;

        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-corporate/corporate-dashboard/travel-policy/" + inputs.policyId + "/assign"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(postAssignTravelPolicyBody, PostAssignTravelPolicyBody.class))
                .build();
        return apiTools.post(request);
    }
}
