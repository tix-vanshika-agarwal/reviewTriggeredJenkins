package com.tiket.api.discovery;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.HashMap;
import java.util.Map;

public record CalendarHolidayApi(Inputs inputs) implements BaseApi {
    public record Inputs (String date) {}
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("storeId", "TIKETCOM");
        headers.put("requestId", "AUTOMATION_DASHBOARD_DISCOV");
        headers.put("channelId", "WEB");
        headers.put("username", "username");
        headers.put("serviceId", "gateway");

        UriHelper uriHelper = new UriHelper("https://calendar-be-svc.test-platform-cluster.tiket.com","https://calendar-be-svc.preprod-platform-cluster.tiket.com");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/tix-calendar/holiday?startDate="+ inputs.date + "&endDate=" + inputs.date + "&page=0&size=200"))
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
