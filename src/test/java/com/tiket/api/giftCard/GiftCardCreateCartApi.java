package com.tiket.api.giftCard;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.giftCard.GiftCardCartBody;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record GiftCardCreateCartApi(Inputs inputs) implements BaseApi {

    public record Inputs(
        String searchKey,
        String inventoryId,
        int voucherAmount,
        String currency,
        String profileId,
        String emailAddress,
        String fullName,
        String phone,
        String title
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/giftCard/GiftCardCreateCart_headers.properties");
        String currency;
        if (inputs.currency.isEmpty())
            currency = "IDR";
        else
            currency = inputs.currency.toUpperCase();
        headers.put("currency", currency);

        JSONObject body = getStaticJson("src/test/resources/api/giftCard/GiftCardCreateCart_body.json");
        Gson gson = new Gson();
        GiftCardCartBody giftCardCartBody = gson.fromJson(body.toString(), GiftCardCartBody.class);

        GiftCardCartBody.Passenger passenger = giftCardCartBody.passengers.get(0);
        passenger.profileId = inputs.profileId;
        passenger.detail.emailAddress = inputs.emailAddress;
        passenger.detail.fullName = inputs.fullName;
        passenger.detail.phone = inputs.phone;
        passenger.detail.title = inputs.title;

        GiftCardCartBody.Inventory inventory = giftCardCartBody.inventories.get(0);
        inventory.searchKey = inputs.searchKey;
        inventory.inventoryId = inputs.inventoryId;
        GiftCardCartBody.BookingItem bookingItem = inventory.bookingItems.get(0);
        bookingItem.voucherAmount = inputs.voucherAmount;

        UriHelper uriHelper = new UriHelper("https://gift-card-be-svc.test-misc-cluster.tiket.com", "https://gift-card-be-svc.preprod-misc-cluster.tiket.com");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("tix-gift-card/gift-card/v1/carts"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(giftCardCartBody, GiftCardCartBody.class))
                .build();

        return apiTools.post(request);
    }
}
