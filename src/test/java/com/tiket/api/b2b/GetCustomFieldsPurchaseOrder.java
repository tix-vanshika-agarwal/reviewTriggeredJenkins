package com.tiket.api.b2b;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

public record GetCustomFieldsPurchaseOrder(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String orderId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        // Load static headers from properties file
        var headers = getStaticMap("src/test/resources/api/b2b/TravelPlans/GetCustomFieldsPurchaseOrder_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com","https://corporate.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/corporate-orders/"+ inputs.orderId+"/custom-fields"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
