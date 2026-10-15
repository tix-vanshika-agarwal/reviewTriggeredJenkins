package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record GETDeleteCheckUserUNMApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            long unmId
    ) {
    }
    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/unm/GETDeleteCheckUserUNMApi_headers.properties");
        headers.put("X-User-Id",inputs.unmId);

        UriHelper helper = new UriHelper("https://service.bliblitiket.tools","https://sandbox.eph.bliblitiket.tools");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/deletion/check"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
