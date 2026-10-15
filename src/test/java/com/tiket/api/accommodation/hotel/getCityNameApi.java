package com.tiket.api.accommodation.hotel;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;

import lombok.SneakyThrows;

import java.util.Map;

public record getCityNameApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String countryCode
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        String os = Configs.PLATFORM.toString().toUpperCase();

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/master_tag/getCityName_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("X-Country-Code", inputs.countryCode);
        headers.put("Channelid", os);
        headers.put("Platform", os);

        String endpoint = "https://api.preprod.tiket.com/ms-gateway/tix-home/v2/page-modules-full?pageModuleCode=hotel_LP&variant=hotel_LP_treatment&taxDisplay=abt&isNotificationActive=false&accommPriceBeforeTax=true";

        ApiRequest request = new ApiRequest.Builder()
                .setUri(endpoint)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

}
