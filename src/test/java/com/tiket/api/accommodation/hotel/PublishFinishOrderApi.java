package com.tiket.api.accommodation.hotel;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

/**
 * Triggers the hotel "publish finish order" (a.k.a. finished order) endpoint that the loyalty
 * mission engine consumes to evaluate whether a transaction should grant a mission stamp / GBV.
 *
 * <p>Mirrors:
 * <pre>
 * curl -L 'https://hotel-cart-be-svc.preprod-hotel-cluster.tiket.com/tix-hotel-cart-dashboard/hotel/publish-finish-order/direct/order-detail-id'
 *   -H 'storeId: TIKETCOM' -H 'channelId: WEB' -H 'requestId: 23123123'
 *   -H 'serviceId: swagger' -H 'username: venn' -H 'Content-Type: application/json'
 *   -d '{"orders":[{"orderDetailId":0,"orderId":0}]}'
 * </pre>
 */
public record PublishFinishOrderApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            long orderId,
            long orderDetailId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap(
                "src/test/resources/api/accommodation/hotel/PublishFinishOrder_headers.properties");

        JSONObject body = getStaticJson(
                "src/test/resources/api/accommodation/hotel/PublishFinishOrder_body.json");
        body.getJSONArray("orders").getJSONObject(0).put("orderId", inputs.orderId);
        body.getJSONArray("orders").getJSONObject(0).put("orderDetailId", inputs.orderDetailId);

        UriHelper helper = new UriHelper(
                "https://hotel-cart-be-svc.test-hotel-cluster.tiket.com",
                "https://hotel-cart-be-svc.preprod-hotel-cluster.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(
                        "/tix-hotel-cart-dashboard/hotel/publish-finish-order/direct/order-detail-id"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
