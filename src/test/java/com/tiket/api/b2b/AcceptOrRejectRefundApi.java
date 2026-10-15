package com.tiket.api.b2b;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record AcceptOrRejectRefundApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String refundId,
            Boolean isApproved
            ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/ApproveOrRejectRefundContent/ApproveOrRejectRefundHeader.properties");
        headers.put("Authorization", "Bearer "+ inputs.accessToken);
        String corpBase = CoreFunctions.getCorporateDashboardBaseUrl();
        headers.put("authority", corpBase.replaceFirst("^https?://", ""));
        headers.put("origin", corpBase);
        headers.put("referer", corpBase + "/refund-list");

        JSONObject body = getStaticJson("src/test/resources/api/b2b/ApproveOrRejectRefundContent/ApproveOrRejectRefund_Body.json");
        body.put("isApproved", inputs.isApproved);

        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com","https://corporate.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/refund/"+inputs.refundId))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.patch(request);
    }
}
