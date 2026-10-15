package com.tiket.api.calendar;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public record CalendarHolidayApi(Inputs inputs) implements BaseApi {

    private static final String HEADERS_PATH = "src/test/resources/api/utilities/Holiday_Calendar/headers.properties";
    private static final String QUERY_PARAMS_PATH = "src/test/resources/api/utilities/Holiday_Calendar/queryParams.properties";

    public record Inputs(
            String accessToken,
            Date startDate,
            Date endDate
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap(HEADERS_PATH);
        String deviceId = CoreFunctions.generateDeviceId();
        String requestId = UUID.randomUUID().toString();

        headers.put("deviceid", deviceId);
        headers.put("x-correlation-id", requestId);
        headers.put("x-request-id", requestId);

        if (inputs.accessToken() != null && !inputs.accessToken().isBlank()) {
            String token = inputs.accessToken().replace("Bearer ", "");
            headers.put("Cookie", "device_id=" + deviceId + "; session_access_token=" + token + ";");
        } else {
            headers.put("Cookie", "device_id=" + deviceId + ";");
        }

        Map<String, Object> queryParams = getStaticMap(QUERY_PARAMS_PATH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        queryParams.put("startDate", sdf.format(inputs.startDate()));
        queryParams.put("endDate", sdf.format(inputs.endDate()));

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/ms-gateway/tix-calendar/holiday"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();
        return apiTools.get(request);
    }
}
