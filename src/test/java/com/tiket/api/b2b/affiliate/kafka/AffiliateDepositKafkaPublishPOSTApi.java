package com.tiket.api.b2b.affiliate.kafka;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record AffiliateDepositKafkaPublishPOSTApi(Inputs input) implements BaseApi {

    public record Inputs(
            long orderId,
            long orderDetailId,
            int baseFare,
            int tiketCommissionValue,
            int publishedFareToReseller,
            int resellerSubsidyCampaignValue,
            int customerFinalPriceInVendorPreferredCurrency,
            int airlineCommission
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/Affiliate/AffiliateDepositKafkaPublishPOSTApi_headers.properties");

        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/b2b/Affiliate/AffiliateDepositKafkaPublishPOSTApi_queryParam.properties");

        JSONObject body = getStaticJson("src/test/resources/api/b2b/Affiliate/AffiliateDepositKafkaPublishPOSTApi_body.json");
        body.put("orderId", input.orderId);

        body.getJSONArray("details").getJSONObject(0).put("orderDetailId", input.orderDetailId);
        body.getJSONArray("details").getJSONObject(0).put("baseFare", input.baseFare);
        body.getJSONArray("details").getJSONObject(0).put("tiketCommissionValue", input.tiketCommissionValue);
        body.getJSONArray("details").getJSONObject(0).put("publishedFareToReseller", input.publishedFareToReseller);
        body.getJSONArray("details").getJSONObject(0).put("resellerSubsidyCampaignValue", input.resellerSubsidyCampaignValue);
        body.getJSONArray("details").getJSONObject(0).put("customerFinalPriceInVendorPreferredCurrency", input.customerFinalPriceInVendorPreferredCurrency);
        body.getJSONArray("details").getJSONObject(0).put("airlineCommission", input.airlineCommission);

        UriHelper helper = new UriHelper("https://deposit-be-http-svc.test-platform-cluster.tiket.com", "https://deposit-be-http-svc.preprod-platform-cluster.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-deposit/kafka/publish"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }

}
