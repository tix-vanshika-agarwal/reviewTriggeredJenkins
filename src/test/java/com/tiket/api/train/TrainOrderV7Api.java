package com.tiket.api.train;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Checks out a train order (tix-train-trx <b>v7</b>). This is the step after the v6
 * {@code carts/{cartId}} passenger/contact call: it creates the real numeric monolith order and
 * returns {@code order.id}, {@code order.hash}, {@code paymentUrl} and the final price, so the flow
 * can go straight to the payment page (no waiting-payment-list lookup required).
 *
 * <p>Insurances and voucherIds are sent as empty arrays (no add-ons selected), which is accepted by
 * the endpoint regardless of the offered insurance products for the journey/class.
 */
public record TrainOrderV7Api(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String cartId,
            String secret,
            // "train" or "whoosh", the product the cart was made for
            String product
    ) {
        public Inputs(String accessToken, String cartId, String secret) {
            this(accessToken, cartId, secret, "train");
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/train/TrainAddToCart_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        JSONObject body = new JSONObject();
        body.put("cartId", inputs.cartId);
        body.put("secret", inputs.secret);
        body.put("insurances", new JSONArray());
        body.put("voucherIds", new JSONArray());

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-train-trx/v7/" + inputs.product + "/orders"))
                .setBody(body.toString())
                .setHeaderParams(headers)
                .build();
        return apiTools.post(request);
    }
}
