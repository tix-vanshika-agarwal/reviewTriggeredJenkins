package com.tiket.api.paymentDweb;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record BCAVANotifyApi(Input inputs) implements BaseApi{
    public record Input(
        String CompanyCode,
        String CustomerNumber,
        String RequestID,
        String ChannelType,
        String TransactionDate,
        String accessToken,
        String CustomerName,
        String CurrencyCode,
        String PaidAmount,
        String TotalAmount
    ) {}
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/paymentDweb/BCAVAInquiryApiHeaders.properties");
        headers.put("Authorization","Bearer " + inputs.accessToken);

        JSONObject body = new JSONObject();
        body.put("CompanyCode",inputs.CompanyCode);
        body.put("CustomerNumber",inputs.CustomerNumber);
        body.put("RequestID",inputs.RequestID);
        body.put("ChannelType",inputs.ChannelType);
        body.put("CustomerName",inputs.CustomerName);
        body.put("CurrencyCode",inputs.CurrencyCode);
        body.put("PaidAmount",inputs.PaidAmount);
        body.put("TotalAmount",inputs.TotalAmount);
        body.put("SubCompany","000126001700000");
        body.put("TransactionDate",inputs.TransactionDate);
        body.put("Reference","12345");
        body.put("DetailBills","null");
        body.put("FlagAdvice","N");
        body.put("Additionaldata","");

        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-bank-transfer/bca/notif"))
                .setBody(body.toString())
                .setHeaderParams(headers)
                .build();
        return apiTools.post(request);
    }
}
