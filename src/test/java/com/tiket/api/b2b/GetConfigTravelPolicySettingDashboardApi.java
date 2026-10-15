package com.tiket.api.b2b;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.b2b.corporate.EmployeeData;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GetConfigTravelPolicySettingDashboardApi(Inputs inputs) implements BaseApi {
    public record Inputs(
        EmployeeData employeeData,
        long accountId
    ){ }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/TravelPolicy/GetConfigTravelPolicySettingDashboardApi_headers.properties");
        headers.put("X-Corporate-Id", inputs.employeeData.data.corporateID);
        headers.put("X-Account-Id", inputs.accountId);
        headers.put("x-Corporate-Employee-Id", inputs.employeeData.data.employeeID);

        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-corporate/corporate-dashboard/settings"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
