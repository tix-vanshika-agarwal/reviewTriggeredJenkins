package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record POSTServiceTicketUNMApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String serviceTicket
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/auth/POSTServiceTicketUNMApi_headers.properties");

        JSONObject body = getStaticJson("src/test/resources/api/auth/POSTServiceTicketUNMApi_body.json");
        body.put("serviceTicket",inputs.serviceTicket);

        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://member-core-v2-be-svc.preprod-platform-cluster.tiket.com");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-member-core/v3/auth/unm/service-ticket"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }
}
