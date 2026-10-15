package com.tiket.api.intools;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

public record SearchMutationApi() implements BaseApi {

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        JSONObject payload = new JSONObject();
        JSONArray paymentMethods = new JSONArray();
        JSONArray reconStatuses = new JSONArray();
        JSONArray statuses = new JSONArray();

        paymentMethods.add("BCA Virtual Account");
        reconStatuses.add("none");
        statuses.add("paid");

        payload.put("orderId", -1);
        payload.put("page", 1);
        payload.put("size", 10);
        payload.put("paymentMethods", paymentMethods);
        payload.put("reconStatuses", reconStatuses);
        payload.put("statuses", statuses);

        Map<String, Object> headers = getStaticMap("src/test/resources/api/intools/MonthlyReportDetail_headers.properties");
        UriHelper uriHelper = new UriHelper("https://finance-be-http-svc.test-platform-cluster.tiket.com","https://finance-be-http-svc.preprod-platform-cluster.tiket.com");
        ApiRequest request = new ApiRequest.Builder()

                .setUri(uriHelper.withEndpoint("/tix-finance-be/finance-dashboard/reconciliation/search/mutation"))
                .setHeaderParams(headers)
                .setBody(payload.toJSONString())
                .build();

        return apiTools.post(request);
    }
}
