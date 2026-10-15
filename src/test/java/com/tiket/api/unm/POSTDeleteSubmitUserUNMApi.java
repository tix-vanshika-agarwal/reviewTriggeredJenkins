package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record POSTDeleteSubmitUserUNMApi (Inputs inputs) implements BaseApi {
    public record Inputs(
            long unmId,
            String token
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/unm/POSTDeleteSubmitUserUNMApi_headers.properties");
        headers.put("X-User-Id",inputs.unmId);

        JSONObject body = getStaticJson("src/test/resources/api/unm/POSTDeleteSubmitUserUNMApi_body.json");
        body.put("token",inputs.token);

        UriHelper helper = new UriHelper("https://service.bliblitiket.tools","https://sandbox.eph.bliblitiket.tools");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-be/api/v1/deletion/submit"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
