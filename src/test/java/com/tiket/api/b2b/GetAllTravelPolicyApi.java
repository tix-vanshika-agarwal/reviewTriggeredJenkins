package com.tiket.api.b2b;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.b2b.corporate.EmployeeData;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GetAllTravelPolicyApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            EmployeeData employeeData,
            long accountId,
            String policyName,
            int size
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/TravelPolicy/GetAllTravelPolicyApi_headers.properties");
        headers.put("X-Corporate-Id",inputs.employeeData.data.corporateID);
        headers.put("X-Corporate-Employee-ID",inputs.employeeData.data.employeeID);
        headers.put("X-Account-ID",inputs.accountId);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/b2b/TravelPolicy/GetAllTravelPolicyApi_queryParam.properties");
        queryParams.put("size", inputs.size);
        queryParams.put("name", inputs.policyName);

        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-corporate/corporate-dashboard/travel-policy"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }
}
