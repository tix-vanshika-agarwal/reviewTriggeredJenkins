package com.tiket.api.accommodation.hotel.landingpage;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Map;

public record HotelLandingPageApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String tribe,
            String accessToken,
            String currency,
            String language,
            boolean withTax
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/HotelLandingPage_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("currency", inputs.currency);
        headers.put("lang", inputs.language);

        String taxDisplay="abt";
        if(inputs.withTax){
            taxDisplay="aat";
        }

        String pageModuleCode="hotel_LP";
        String variant="hotel_LP_treatment";
        if(!inputs.tribe.equalsIgnoreCase("hotel")){
            variant="homes_landingPage";
            pageModuleCode="homes_landingPage";
        }


        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-home/v2/page-modules-full?pageModuleCode="+pageModuleCode+"&variant="+variant+"&taxDisplay="+taxDisplay+"&isNotificationActive=true&accommPriceBeforeTax="+!inputs.withTax))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
