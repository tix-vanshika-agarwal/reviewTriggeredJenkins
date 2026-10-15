package com.tiket.api.train;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.train.TrainOrderBody;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record TrainOrderApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            String cartId,
            String secret,
            String name,
            String phone,
            String email,
            String IdNumber,
            boolean useV6
    ) {
        /**
         * Defaults {@code useV6} to {@code false} (the previous {@code tix-train-trx/carts} URL) so
         * existing callers are unaffected. Pass the full constructor with {@code useV6 = true} to use
         * the {@code tix-train-trx/v6/train/carts} URL.
         */
        public Inputs(String accessToken, String cartId, String secret, String name, String phone, String email, String IdNumber) {
            this(accessToken, cartId, secret, name, phone, email, IdNumber, false);
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/train/TrainAddToCart_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/train/TrainOrder_queryParams.properties");
        queryParams.put("secret",inputs.secret);

        JSONObject body = getStaticJson("src/test/resources/api/train/TrainOrder_body.json");

        Gson gson = new Gson();
        TrainOrderBody orderBody = gson.fromJson(body.toString(), TrainOrderBody.class);

        orderBody.cartId = inputs.cartId;
        orderBody.secret = inputs.secret;
        orderBody.contact.emailAddress = inputs.email;
        orderBody.contact.phone = inputs.phone;
        orderBody.contact.fullName = inputs.name;
        orderBody.passengers.get(0).fullName = inputs.name;
        orderBody.passengers.get(0).identityNumber = inputs.IdNumber;

        String endpoint = inputs.useV6
                ? "ms-gateway/tix-train-trx/v6/train/carts/" + inputs.cartId
                : "ms-gateway/tix-train-trx/carts/" + inputs.cartId;

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint(endpoint))
                .setQueryParams(queryParams)
                .setBody(gson.toJson(orderBody, TrainOrderBody.class))
                .setHeaderParams(headers)
                .build();
        return apiTools.post(request);
    }

}
