package com.tiket.api.loyalty;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record PendingReviewApi(Inputs inputs) implements BaseApi{

    public record Inputs(String token) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
       Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/PendingReview_headers.properties");
        headers.put("Authorization", inputs.token);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-review-core/myReview/getPendingReview?page=0&size=5&sortDirection=DESC&reviewSubmitColumn=SHOW_DATE")).setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
    
} 