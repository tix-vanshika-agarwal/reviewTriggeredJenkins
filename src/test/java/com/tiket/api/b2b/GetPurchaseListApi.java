package com.tiket.api.b2b;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GetPurchaseListApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken2,
            String orderID,
            int employeeID,
            int corporateID,
            int accessRoleId
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com","https://corporate.preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/GETPurchaseListContent/GETPurchaseListContent_header.properties");
        headers.put("Authorization", "Bearer " + inputs.accessToken2);
        headers.put("x-corporate-id", inputs.corporateID);
        headers.put("x-employee-id", inputs.employeeID);


        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/corporate-orders/"+inputs.orderID+"/purchase-detail"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
