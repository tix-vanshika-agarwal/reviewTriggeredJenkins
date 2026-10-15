package com.tiket.api.intools;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record SummaryReportDetailApi (Inputs inputs)  implements BaseApi{
    public record Inputs(
            String flightType,
            String entity2,
            String vendorCurrency,
            String year,
            String months,
            String page,
            String limit


    ) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/intools/MonthlyReportDetail_headers.properties");
        UriHelper uriHelper = new UriHelper("https://deposit-be-http-svc.test-platform-cluster.tiket.com","https://deposit-be-http-svc.preprod-platform-cluster.tiket.com");
        ApiRequest request = new ApiRequest.Builder()

                .setUri(uriHelper.withEndpoint("/tix-deposit/vertical/flight/monthly-summary-report/vendor-summary?flightType="+inputs.flightType+"&entity="+inputs.entity2+"&vendorCurrency="+inputs.vendorCurrency+"&year="+inputs.year+"&month="+inputs.months+"&page="+inputs.page+"&limit="+inputs.limit))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }


}