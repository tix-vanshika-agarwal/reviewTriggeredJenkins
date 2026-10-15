package com.tiket.api.b2b;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.b2b.corporate.EmployeeUpdateBody;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record PutUpdateEmployeeApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            int employeeId,
            long accountId,
            int corporateId,
            int corporateRoleId,
            String cookie
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        // Load headers from static properties
        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/Employee/PutUpdateEmployeeApi_headers.properties");

        // Inject dynamic values
        headers.put("x-account-id", inputs.accountId);
        headers.put("x-corporate-id", inputs.corporateId);
        headers.put("cookie", inputs.cookie);

        // Prepare body
        EmployeeUpdateBody body = new EmployeeUpdateBody();
        body.employeeID = inputs.employeeId;
        body.corporateRoleID = inputs.corporateRoleId;

        // Set URI
        UriHelper uriHelper = new UriHelper("https://corporate.preprod.tiket.com", "https://corporate.preprod.tiket.com");
        String endpoint = "/corporate-gateway/tix-corporate/corporate-dashboard/employees/" + inputs.employeeId;

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .setBody(new Gson().toJson(body))
                .build();

        return apiTools.put(request);
    }
}
