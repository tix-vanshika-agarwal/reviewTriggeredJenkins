package com.tiket.api.ttd;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.ttd.KlookVoucher;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record POSTKlookVoucherApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String agent_order_id,
            String order_id
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/TTDSupplier/KlookVoucher_headers.properties");

        JSONObject jsonObject = getStaticJson("src/test/resources/api/ttd/TTDSupplier/KlookVoucher_body.json");
        Gson gson = new Gson();
        KlookVoucher klookVoucher = gson.fromJson(jsonObject.toString(), KlookVoucher.class);
        klookVoucher.meta.agent_order_id = inputs.agent_order_id;
        klookVoucher.meta.order_id = inputs.order_id;

        UriHelper helper = new UriHelper("https://events-v2-supplier-svc.test-to-do-cluster.tiket.com","https://events-v2-supplier-svc.preprod-to-do-cluster.tiket.com");

        ApiRequest apiRequest = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-events-v2-supplier/webhook-handler/klook/voucherReady"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(klookVoucher, KlookVoucher.class))
                .build();

        return apiTools.post(apiRequest);
    }
}
