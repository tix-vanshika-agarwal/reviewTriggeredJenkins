package com.tiket.api.carTransfer;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.simple.JSONObject;

import java.util.Map;

public record CarTransferAdminConfirmOrderApi(Inputs inputs) implements BaseApi{

    public record Inputs(String dashboardToken,String adminName,String adminPhone,String orderId) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
         JSONObject payload = new JSONObject();
        Map<String, Object> headers = getStaticMap("src/test/resources/api/carTransfer/CarTransferAdminConfirmOrder_headers.properties");
        headers.put("Authorization", "Bearer "+inputs.dashboardToken);
        payload.put("adminName", inputs.adminName);
        payload.put("adminPhone", inputs.adminPhone);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-airport-transfer-order/v1/orders/"+inputs.orderId+"/confirmation"))
                .setBody(payload.toJSONString())
                .setHeaderParams(headers)
                .build();

        return apiTools.post(request);
    }
    
}
