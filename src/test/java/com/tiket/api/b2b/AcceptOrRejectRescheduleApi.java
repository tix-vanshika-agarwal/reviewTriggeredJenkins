package com.tiket.api.b2b;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record AcceptOrRejectRescheduleApi (Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            int rescheduleId,
            Boolean isApproved,
            String note
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/AcceptOrRejectReschedule/AcceptOrRejectRescheduleApi_headers.properties");
        headers.put("cookie", "session_access_token=" + inputs.accessToken + ";");

        JSONObject body = getStaticJson("src/test/resources/api/b2b/AcceptOrRejectReschedule/AcceptOrRejectRescheduleApi_body.json");
        body.put("isApproved", inputs.isApproved);
        body.put("note", inputs.note);

        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com","https://corporate.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/reschedule/" + inputs.rescheduleId))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.patch(request);
    }
}