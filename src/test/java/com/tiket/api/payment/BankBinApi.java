package com.tiket.api.payment; 

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record BankBinApi(Inputs inputs) implements BaseApi {
    
    public record Inputs(
            String bin,
            String currency,
            String deviceId,
            String lang,
            String countryCode
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://gatotkaca.tiket.com", "https://preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/payment/BankBinApi_headers.properties");
        
        headers.put("X-Currency", inputs.currency);
        headers.put("currency", inputs.currency);
        headers.put("deviceId", inputs.deviceId);
        headers.put("lang", inputs.lang);
        headers.put("countryCode", inputs.countryCode);
        headers.put("X-Country-Code", inputs.countryCode);
        headers.put("X-Country-Id", inputs.countryCode);

        ApiRequest request = new ApiRequest.Builder().setUri(helper.withEndpoint("ms-gateway/tix-payment-core/bankBin/bin/" + inputs.bin() + "?ts=" + System.currentTimeMillis())).setHeaderParams(headers).build();
                
        return apiTools.get(request);
    }
}