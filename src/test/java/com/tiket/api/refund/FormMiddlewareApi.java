package com.tiket.api.refund;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.refund.FormMiddlewareApiBody;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record FormMiddlewareApi(Inputs inputs) implements BaseApi {
    
    public record Inputs(
        BigDecimal amountFromVendor,
        boolean isForceToBankTransfer,
        List<String> items,
        long orderDetailId,
        long orderId,
        String reasonId,
        String bankAccountName,
        String bankAccountNumber,
        String bankCodeTiketIdentifier,
        String remark,
        String usernameAdmin
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/refund/FormMiddlewareApi_headers.properties");
        headers.put("X-Username", inputs.usernameAdmin);

        JSONObject body = getStaticJson("src/test/resources/api/refund/FormMiddlewareApi_body.json");
        Gson gson = new Gson();
        FormMiddlewareApiBody formMiddlewareApiBody = gson.fromJson(body.toString(), FormMiddlewareApiBody.class);
        formMiddlewareApiBody.amountFromVendor = inputs.amountFromVendor;
        formMiddlewareApiBody.isForceToBankTransfer = inputs.isForceToBankTransfer;
        formMiddlewareApiBody.items = inputs.items;
        formMiddlewareApiBody.orderDetailId = inputs.orderDetailId;
        formMiddlewareApiBody.orderId = inputs.orderId;
        formMiddlewareApiBody.reasonId = inputs.reasonId;
        formMiddlewareApiBody.remark = inputs.remark;
        formMiddlewareApiBody.receiver.bankAccountName = inputs.bankAccountName;
        formMiddlewareApiBody.receiver.bankAccountNumber = inputs.bankAccountNumber;
        formMiddlewareApiBody.receiver.bankCodeTiketIdentifier = inputs.bankCodeTiketIdentifier;

        UriHelper uriHelper = new UriHelper("https://refund-v4-be-svc.test-payment-cluster.tiket.com","https://refund-v4-be-svc.preprod-payment-cluster.tiket.com");
        ApiRequest apiRequest = new ApiRequest.Builder()
            .setUri(uriHelper.withEndpoint("tix-refund/dashboard/refunds/form-middleware"))
            .setHeaderParams(headers)
            .setBody(gson.toJson(formMiddlewareApiBody, FormMiddlewareApiBody.class))
            .build();

        return apiTools.post(apiRequest);
    }
}
