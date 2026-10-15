package com.tiket.api.discount;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.discount.DiscountBody;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.Map;

public record ApplyDiscountApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String orderId,
            String orderHash,
            String discountCode,
            String productType,
            BigInteger totalPrice,
            String currency
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/discount/Discount_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        JSONObject jsonObject = getStaticJson("src/test/resources/api/discount/Discount_body.json");
        Gson gson = new Gson();
        DiscountBody discountBody = gson.fromJson(jsonObject.toString(), DiscountBody.class);
        discountBody.orderId = inputs.orderId;
        discountBody.orderHash = inputs.orderHash;
        discountBody.discountCode = inputs.discountCode;
        discountBody.productType = inputs.productType;
        discountBody.totalPrice = inputs.totalPrice;
        String currency = inputs.currency.toUpperCase();
        if (inputs.currency.isEmpty()) currency = "IDR";
        discountBody.currency = currency;

        UriHelper uriHelper = new UriHelper("https://mpayment-gatotkaca.tiket.com", "https://mpayment.tiket.com", "https://mpayment.preprod.tiket.com");
        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-discount-engine/discount/apply/v2"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(discountBody, DiscountBody.class))
                .build();

        return apiTools.post(apiRequest);
    }
}
