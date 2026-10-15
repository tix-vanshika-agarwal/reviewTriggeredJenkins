package com.tiket.api.loyalty;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record GetReviewApi(Inputs inputs) implements BaseApi{

    public record Inputs(
            String productType,
            String inventoryId,
            String accessToken,
            String lang,
            String topic
    ) { }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/loyalty/Loyalty_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("lang",inputs.lang);
        String topic = "";
        if(!inputs.topic.isBlank()){
            topic = "&topic="+inputs.topic;
        }

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-review-core/verticalReview/internalReview?hideEmptyComment=1&inventoryId="+inputs.inventoryId+"&page=0&productType="+inputs.productType+"&reviewSubmitColumn=RATING_SUMMARY&searchType=INVENTORY&size=100&sortDirection=DESC"+topic))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

}