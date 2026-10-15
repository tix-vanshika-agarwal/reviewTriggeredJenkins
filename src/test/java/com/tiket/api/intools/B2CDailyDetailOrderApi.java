package com.tiket.api.intools;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public record B2CDailyDetailOrderApi(Inputs inputs)  implements BaseApi{
    public record Inputs(

            String vertical,
            String softblockId,
            String searchDateBy,
            String startDate,
            String endDate

    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/intools/B2CDailyDetailApiHeaders.properties");
        UriHelper uriHelper = new UriHelper("https://deposit-be-http-svc.test-platform-cluster.tiket.com","https://deposit-be-http-svc.preprod-platform-cluster.tiket.com");
        ApiRequest request = new ApiRequest.Builder()

                .setUri(uriHelper.withEndpoint("/tix-deposit/report/"
                        + URLEncoder.encode(inputs.vertical, StandardCharsets.UTF_8) +"/"
                        + URLEncoder.encode(inputs.softblockId, StandardCharsets.UTF_8)+"/daily?searchDateBy="
                        + URLEncoder.encode(inputs.searchDateBy, StandardCharsets.UTF_8)+"&startSearchDateBy="
                        + URLEncoder.encode(inputs.startDate, StandardCharsets.UTF_8)+"&endSearchDateBy="
                        + URLEncoder.encode(inputs.endDate, StandardCharsets.UTF_8)))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }


}