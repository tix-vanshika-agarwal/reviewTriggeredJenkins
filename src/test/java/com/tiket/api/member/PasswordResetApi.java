package com.tiket.api.member;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.simple.JSONObject;

import java.util.Map;

public record PasswordResetApi(Inputs inputs) implements BaseApi {
    public record Inputs(String trxId,String accessToken,String password){}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
          Map<String, Object> headers = getStaticMap("src/test/resources/api/member/verifyOtp_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        JSONObject resetPasswordPayload = new JSONObject();
        resetPasswordPayload.put("otpToken", "1234");
        resetPasswordPayload.put("otpTrxId", inputs.trxId);
        resetPasswordPayload.put("password", inputs.password);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-member-core/v2/password/reset"))
                .setHeaderParams(headers)
                .setBody(resetPasswordPayload.toJSONString())
                .build();

        return apiTools.put(request);
    }
}
