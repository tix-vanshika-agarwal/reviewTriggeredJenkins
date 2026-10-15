package com.tiket.api.paymentDweb;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record VAStagingGenerateTokenApi(Input inputs) implements BaseApi{
    public record Input(
        String bankCd,
        String amount
    ) {}
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/paymentDweb/VAStagingGenerateTokenApiHeaders.properties");
        JSONObject body = new JSONObject();
        body.put("bankCd",inputs.bankCd);
        body.put("amount",inputs.amount);

        UriHelper baseUrl = new UriHelper("http://192.168.69.199:3001", "", "http://10.6.2.33:3001");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(baseUrl.withEndpoint("/tiket/sdet/generateNicepayVaToken"))
                .setBody(body.toString())
                .setHeaderParams(headers)
                .build();
        return apiTools.post(request);
    }
}
