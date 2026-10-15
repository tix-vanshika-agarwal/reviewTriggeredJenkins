package com.tiket.api.paymentDweb;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import org.json.JSONObject;

import java.util.Map;

public record VAStagingHitPaymentApi(Input inputs) implements BaseApi {
  public record Input(String bankCd, String amount, String referenceNo, String token, String vacctNo,
                      String paymentId) {
    // Backward-compatible constructor for existing callers
    public Input(String bankCd, String amount, String referenceNo, String token) {
      this(bankCd, amount, referenceNo, token, "1420201001354992", "633d5d363ffac137626da9b5");
    }
  }

  @Override
  public ApiResponse hitApi(ApiTools apiTools) {
    Map<String, Object> headers =
      getStaticMap("src/test/resources/api/paymentDweb/VAStagingGenerateTokenApiHeaders.properties");
    JSONObject body = new JSONObject();
    body.put("tXid", "TIKETMOCK0000000000000000000000");
    body.put("referenceNo", inputs.referenceNo);
    body.put("amt", inputs.amount);
    body.put("merchantToken", inputs.token);
    body.put("status", "0");
    body.put("vacctNo", inputs.vacctNo);
    body.put("bankCd", inputs.bankCd);
    body.put("paymentId", inputs.paymentId);

    UriHelper baseUrl = new UriHelper(
      "http://payment-callback-be-svc.test-payment-cluster.tiket.com",
      "",
      "http://payment-callback-be-svc.preprod-payment-cluster.tiket.com"
    );
    ApiRequest request = new ApiRequest.Builder().setUri(baseUrl.withEndpoint(
        "/tix-payment-callback/nicepay/paymentNotification?tXid=TIKETMOCK0000000000000000000000&referenceNo="
          + inputs.referenceNo + "&amt=" + inputs.amount + "&merchantToken=" + inputs.token + "&status=0&vacctNo="
          + inputs.vacctNo + "&bankCd=" + inputs.bankCd + "&paymentId=" + inputs.paymentId))
      .setBody(body.toString())
      .setHeaderParams(headers)
      .build();
    return apiTools.post(request);
  }
}