package com.tiket.api.accommodation.hotel;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public record PublishFinishHotelOrderApi(Inputs inputs) implements BaseApi {

    public record Inputs(String orderId, String orderDetailId) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        String endpointUrl = Configs.CONFIG.getProperty("hotel.publish.finish.order.url");
        Map<String, Object> headers = getStaticMap(
                "src/test/resources/api/accommodation/hotel/PublishFinishHotelOrderApi_headers.properties");

        JSONObject body = getStaticJson("src/test/resources/api/accommodation/hotel/PublishFinishHotelOrderApi_body.json");
        JSONObject order = new JSONObject();
        order.put("orderDetailId", Long.parseLong(inputs.orderDetailId()));
        order.put("orderId", Long.parseLong(inputs.orderId()));
        body.put("orders", new JSONArray().put(order));

        ApiRequest request = new ApiRequest.Builder()
                .setUri(endpointUrl)
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
