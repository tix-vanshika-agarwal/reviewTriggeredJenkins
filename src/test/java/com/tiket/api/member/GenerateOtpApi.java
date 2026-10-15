package com.tiket.api.member;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.simple.JSONObject;

import java.util.Map;

public record GenerateOtpApi(Inputs inputs) implements BaseApi {

    public record Inputs(String otpType,String accessToken,String recipient){}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/member/generateOtp_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        JSONObject recipientData = new JSONObject();
        recipientData.put("recipient", inputs.recipient);
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-members-core/otp/v2/generate/"+inputs.otpType))
                .setHeaderParams(headers)
                .setBody(recipientData.toJSONString())
                .build();

        return apiTools.post(request);
    }
    
    
}
