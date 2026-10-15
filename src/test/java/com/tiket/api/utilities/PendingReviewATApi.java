package com.tiket.api.utilities;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record PendingReviewATApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            long accountId,
            String userFullName,
            String orderId,
            String orderDetailId,
            String username,
            String lang,
            String email,
            String automationIdentifier,
            String showDate,
            String expiryDate
    ){}
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/utilities/PendingReview_headers.properties");
        headers.put("username", inputs.username);
        JSONObject body = getStaticJson("src/test/resources/api/utilities/pendingReviewAT_body.json");
        body.put("userFullName", inputs.userFullName);
        body.put("orderId", inputs.orderId);
        body.put("orderDetailId", inputs.orderDetailId);
        body.put("username", inputs.username);
        body.put("lang", inputs.lang);
        body.put("accountId", inputs.accountId);
        body.put("inventoryName",inputs.automationIdentifier);
        body.getJSONObject("additionalInfo").put("pickupPlace", inputs.automationIdentifier);
        body.put("showDate", inputs.showDate);
        body.put("expiryDate", inputs.expiryDate);

        String journeyStartDate = CoreFunctions.addToDateYYYY_MM_DD(CoreFunctions.getCurrentDateYYYY_MM_DD(), -3) + body.getString("journeyStartDate");
        String journeyEndDate = CoreFunctions.addToDateYYYY_MM_DD(CoreFunctions.getCurrentDateYYYY_MM_DD(), -2) + body.getString("journeyStartDate");
        body.put("journeyStartDate",journeyStartDate);
        body.put("journeyEndDate",journeyEndDate);

        UriHelper helper = new UriHelper("https://review-core-be-svc.test-platform-cluster.tiket.com","https://review-core-be-svc.preprod-hotel-cluster.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-review-core/tools/reviewOrderSave"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();
        return apiTools.post(request);
    }
}

