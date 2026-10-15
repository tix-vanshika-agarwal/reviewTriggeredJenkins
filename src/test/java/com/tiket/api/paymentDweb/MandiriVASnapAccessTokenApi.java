package com.tiket.api.paymentDweb;

import java.util.Map;

import org.json.JSONObject;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

public record MandiriVASnapAccessTokenApi(Input inputs) implements BaseApi {

  private static final String GK_URL = "https://api-gateway-payment-partner-svc.preprod-payment-cluster.tiket.com";
  private static final String PREPROD_URL = "https://api-gateway-payment-partner-svc.preprod-payment-cluster.tiket.com";
  private static final String ENDPOINT = "/payment-gateway-partner/tix-bank-transfer/mandiri/openapi/v1.0/access-token/b2b";

  public record Input() {}

  @Override
  public ApiResponse hitApi(ApiTools apiTools) {
    Map<String, Object> headers = getStaticMap("src/test/resources/api/paymentDweb/MandiriVASnapAccessTokenApiHeaders.properties");
    JSONObject body = new JSONObject();
    body.put("grantType", "client_credentials");

    UriHelper helper = new UriHelper(GK_URL, PREPROD_URL);

    ApiRequest request = new ApiRequest.Builder()
      .setUri(helper.withEndpoint(ENDPOINT))
      .setBody(body.toString())
      .setHeaderParams(headers)
      .build();
    return apiTools.post(request);
  }
}
