package com.tiket.api.b2b;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.b2b.corporate.PostInterimDetailsApiFlight;
import com.tiket.pojo.b2b.corporate.PostInterimDetailsApiHotel;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record PostInterimDetailsApi(Inputs inputs) implements BaseApi {
    public record Inputs(
        String accessToken,
        PostInterimDetailsApiHotel postInterimDetailsApiHotel,
        PostInterimDetailsApiFlight postInterimDetailsApiFlight
    ){}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://m.gatotkaca.tiket.com","https://m.preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/InterimPage/PostInterimDetailsApi_headers.properties");
        headers.put("Authorization",inputs.accessToken);
        headers.put("origin", helper.getUri());

        Gson gson = new Gson();
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/ms-gateway/tix-corporate/interim/details"))
                .setHeaderParams(headers)
                .setBody(inputs.postInterimDetailsApiFlight==null? gson.toJson(inputs.postInterimDetailsApiHotel, PostInterimDetailsApiHotel.class):gson.toJson(inputs.postInterimDetailsApiFlight, PostInterimDetailsApiFlight.class))
                .build();
        return apiTools.post(request);
    }
}
