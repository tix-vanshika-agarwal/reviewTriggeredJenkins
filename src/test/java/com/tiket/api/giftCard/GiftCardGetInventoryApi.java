package com.tiket.api.giftCard;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record GiftCardGetInventoryApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String currency
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/giftCard/GiftCardGetInventory_headers.properties");
        String currency;
        if (inputs.currency.isEmpty())
            currency = "IDR";
        else
            currency = inputs.currency.toUpperCase();
        headers.put("currency", currency);


        UriHelper uriHelper = new UriHelper("https://gift-card-be-svc.test-misc-cluster.tiket.com", "https://gift-card-be-svc.preprod-misc-cluster.tiket.com");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("tix-gift-card/gift-card/v1/inventories/search"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
