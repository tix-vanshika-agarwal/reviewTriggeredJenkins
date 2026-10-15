package com.tiket.api.carrental;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.simple.JSONObject;

import java.util.Map;

public record CarRentalAdminConfirmOrderApi(Inputs inputs) implements BaseApi {

    public record Inputs(String adminName,String adminPhone,String orderId,String dashboardToken){}

    @Override
    @SneakyThrows
    public ApiResponse hitApi(ApiTools apiTools) {
        JSONObject payload = new JSONObject();
        Map<String, Object> headers = getStaticMap("src/test/resources/api/carrental/CarRentalAdminConfirmOrder_headers.properties");
        headers.put("Authorization", "Bearer "+inputs.dashboardToken);
        payload.put("adminName", inputs.adminName);
        payload.put("adminPhone", inputs.adminPhone);
        payload.put("orderId", inputs.orderId);

        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-vendor/v1/orderCar/confirm-order"))
                .setBody(payload.toJSONString())
                .setHeaderParams(headers)
                .build();

        return apiTools.post(request);
    }
} 