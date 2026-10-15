package com.tiket.api.paymentDweb;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record MandiriTransferApi(Input inputs) implements BaseApi{
    public record Input(
        String referenceId,
        int amount
    ) {}
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/paymentDweb/MandiriTransferApiHeaders.properties");
        JSONObject body = new JSONObject();
        body.put("transactionID","null");
        body.put("transactionDate","2024-02-21");
        body.put("transactionTime","14:25:00.+0700");
        body.put("ticketNumber",inputs.referenceId);
        body.put("transactionReference",inputs.referenceId);
        body.put("transactionCode","1234");
        body.put("transactionType","C");
        body.put("fromAccount","null");
        body.put("toAccount","1170008089989");
        body.put("amount",inputs.amount);
        body.put("currency","IDR");
        body.put("branch","MANDIRI-MOCK");
        body.put("remark1","null");
        body.put("remark2","null");
        body.put("remark3","null");
        body.put("remark4","null");

        UriHelper helper = new UriHelper("http://mandiri-statement-be-svc.test-payment-cluster.tiket.com","http://mandiri-statement-be-svc.preprod-payment-cluster.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-mandiri-statement/statement"))
                .setBody(body.toString())
                .setHeaderParams(headers)
                .build();
        return apiTools.post(request);
    }
}