package com.tiket.api.paymentDweb;

import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

public record MandiriVASnapPaymentApi(Input inputs) implements BaseApi {
  private static final String BASE_URL = "https://api-gateway-payment-partner-svc.preprod-payment-cluster.tiket.com";
  private static final String ENDPOINT = "/payment-gateway-partner/tix-bank-transfer/mandiri/openapi/v1.0/transfer-va/payment";
  private static final String BODY_PATH = "src/test/resources/api/paymentDweb/MandiriVASnapPaymentApi_body.json";

  public record Input(
    String accessToken,
    String customerNo,
    String virtualAccountNo,
    long paymentAmount,
    String virtualAccountName,
    String subCompany,
    String orderId
  ) {}

  @Override
  public ApiResponse hitApi(ApiTools apiTools) {
    Map<String, Object> headers = getStaticMap("src/test/resources/api/paymentDweb/MandiriVAInternalPaymentApiHeaders.properties");
    headers.put("Authorization", "Bearer " + inputs.accessToken());
    headers.put("X-EXTERNAL-ID", UUID.randomUUID().toString());

    String trxDateTime = String.valueOf(headers.get("X-TIMESTAMP"));
    String amountValue = inputs.paymentAmount() + ".00";

    JSONObject body = getStaticJson(BODY_PATH);

    body.put("partnerServiceId", body.getString("partnerServiceId").trim());
    body.put("customerNo", inputs.customerNo());
    body.put("virtualAccountNo", inputs.virtualAccountNo());
    body.put("virtualAccountName", inputs.virtualAccountName());
    body.put("paymentRequestId", inputs.orderId());
    body.put("hashedSourceAccountNo", inputs.orderId());
    body.getJSONObject("paidAmount").put("value", amountValue);
    body.getJSONObject("totalAmount").put("value", amountValue);
    body.put("paidBills", amountValue);
    body.put("trxDateTime", trxDateTime);
    body.put("subCompany", inputs.subCompany());

    UriHelper helper = new UriHelper(BASE_URL);

    ApiRequest request = new ApiRequest.Builder()
      .setUri(helper.withEndpoint(ENDPOINT))
      .setBody(body.toString())
      .setHeaderParams(headers)
      .build();
    return apiTools.post(request);
  }
}
