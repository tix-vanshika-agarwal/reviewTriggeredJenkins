package com.tiket.api.loyalty;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record SubmitReviewApi(Inputs inputs) implements BaseApi {

    public record Inputs(String token,String reviewToken,String accountId) {
    }
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/SubmitReview_headers.properties");
        headers.put("Authorization", inputs.token);

        JSONObject body = getStaticJson("src/test/resources/api/loyalty/SubmitReview_body.json");
        body.put("token", inputs.reviewToken);
        System.out.println("DEBUG : " + body.toString());

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-review-core/review/submit"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
    
}
