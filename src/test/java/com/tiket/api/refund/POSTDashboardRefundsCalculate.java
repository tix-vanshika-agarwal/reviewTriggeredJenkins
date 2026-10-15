package com.tiket.api.refund;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public record POSTDashboardRefundsCalculate(Inputs inputs) implements BaseApi {

    public record Inputs(
            String orderId,
            String orderDetailId,
            String reasonId,
            String amount,
            String forceBank,
            List<Integer> items
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/refund/POSTDashboardRefundsCalculate_headers.properties");
        JSONObject body = getStaticJson("src/test/resources/api/refund/POSTDashboardRefundsCalculate_body.json");
        JSONArray itemsBody = new JSONArray(inputs.items);
        body.put("items", itemsBody);

        String endpoint = "/tix-refund/dashboard/refunds/"+inputs.orderId+"/"+inputs.orderDetailId+"/calculate?reasonId="+inputs.reasonId+"&reasonType=refund_reason&amountFromVendor="+inputs.amount+"&isForceToBankTransfer="+inputs.forceBank+"";
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint(endpoint))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }

}