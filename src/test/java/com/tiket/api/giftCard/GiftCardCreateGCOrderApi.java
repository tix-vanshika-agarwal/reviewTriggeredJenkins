package com.tiket.api.giftCard;

import com.google.gson.Gson;
import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.giftCard.GiftCardOrderBody;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public record GiftCardCreateGCOrderApi(Inputs inputs) implements BaseApi {

    private static final String ORDER_ENDPOINT = "krakend-gateway-promo/tix-gift-card/gift-card/v1/carts/";
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) "
            + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/152.0.0.0 Safari/537.36";
    private static final String DEFAULT_SEC_CH_UA = "\"Chromium\";v=\"152\", \"Not?A_Brand\";v=\"24\", \"Google Chrome\";v=\"152\"";

    public record Inputs(
        String cartId,
        String cartSecret,
        String scheduleDateTime,
        String profileId,
        String emailAddress,
        String fullName,
        String phone,
        String title,
        String currency,
        String senderName,
        String scheduleTimeZone,
        String cookieHeader,
        String accountId,
        String username,
        String referer
    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        if (inputs.cookieHeader == null || inputs.cookieHeader.isBlank()) {
            throw new IllegalStateException("Gift card order API requires a cookie header");
        }

        Gson gson = new Gson();
        UriHelper uriHelper = new UriHelper("https://gatotkaca.tiket.com", "https://preprod.tiket.com");
        Map<String, Object> headers = buildHeaders(uriHelper.getUri());
        JSONObject body = getStaticJson("src/test/resources/api/giftCard/GiftCardCreateGCOrder_body.json");
        GiftCardOrderBody giftCardOrderBody = gson.fromJson(body.toString(), GiftCardOrderBody.class);
        giftCardOrderBody.cartId = inputs.cartId;
        giftCardOrderBody.cartSecret = inputs.cartSecret;

        giftCardOrderBody.customer.profileId = inputs.profileId;
        giftCardOrderBody.customer.detail.emailAddress = inputs.emailAddress;
        giftCardOrderBody.customer.detail.fullName = inputs.fullName;
        giftCardOrderBody.customer.detail.phone = inputs.phone;
        giftCardOrderBody.customer.detail.title = inputs.title;

        GiftCardOrderBody.BookingDetail bookingDetail = giftCardOrderBody.bookingDetail.get(0);
        bookingDetail.scheduleDateTime = inputs.scheduleDateTime;
        bookingDetail.scheduleTimeZone = inputs.scheduleTimeZone;
        bookingDetail.senderName = inputs.senderName;

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint(ORDER_ENDPOINT + inputs.cartId + "/orders"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(giftCardOrderBody, GiftCardOrderBody.class))
                .build();

        return apiTools.post(request);
    }

    private Map<String, Object> buildHeaders(String origin) {
        Map<String, Object> headers = new LinkedHashMap<>();
        String currency = normalizeCurrency(inputs.currency);
        String countryCode = "id";
        String language = "en";
        String requestId = UUID.randomUUID().toString();
        String correlationId = CoreFunctions.generateRandomString(6);

        headers.put("accept", "*/*");
        headers.put("accept-language", language);
        headers.put("cf-ipcountry", "SG");
        headers.put("content-type", "application/json");
        headers.put("countrycode", countryCode);
        headers.put("currency", currency);
        headers.put("deviceid", "");
        headers.put("lang", language);
        headers.put("origin", origin);
        headers.put("priority", "u=1, i");
        headers.put("referer", inputs.referer == null || inputs.referer.isBlank() ? origin : inputs.referer);
        headers.put("sec-ch-ua", DEFAULT_SEC_CH_UA);
        headers.put("sec-ch-ua-mobile", "?0");
        headers.put("sec-ch-ua-platform", "\"macOS\"");
        headers.put("sec-fetch-dest", "empty");
        headers.put("sec-fetch-mode", "cors");
        headers.put("sec-fetch-site", "same-origin");
        headers.put("user-agent", DEFAULT_USER_AGENT);
        headers.put("x-audience", "tiket.com");
        headers.put("x-city-id", "");
        headers.put("x-cookie-session-v2", "true");
        headers.put("x-correlation-id", correlationId);
        headers.put("x-country-code", countryCode);
        headers.put("x-country-id", countryCode);
        headers.put("x-currency", currency);
        headers.put("x-region-id", "");
        headers.put("x-request-id", requestId);
        headers.put("username", inputs.username);
        headers.put("accountId", inputs.accountId);
        headers.put("cookie", inputs.cookieHeader);
        return headers;
    }

    private String normalizeCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            return "IDR";
        }
        return currency.toUpperCase();
    }

}
