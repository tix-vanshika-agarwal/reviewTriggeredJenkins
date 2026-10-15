package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Same endpoint as {@link TTDChoosePackageScheduleApi}, but fetches the schedules of one specific visit date
 * instead of a date range computed from today.
 *
 * @param useCookieAuth when {@code true}, the access token is sent as the {@code session_access_token} cookie
 *                      instead of the {@code Authorization} header (e.g. for B2B corporate accounts)
 */
public record TTDChoosePackageScheduleByDateApi(Inputs inputs, boolean useCookieAuth) implements BaseApi {

    /** Defaults to sending the access token in the {@code Authorization} header. */
    public TTDChoosePackageScheduleByDateApi(Inputs inputs) {
        this(inputs, false);
    }

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * @param date visit date in {@code yyyy-MM-dd} format, e.g. {@code 2026-10-15}
     */
    public record Inputs(
            String accessToken,
            String productID,
            String packageCode,
            String date
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/TTDChoosePackageSchedule_headers.properties");
        if (useCookieAuth) {
            headers.put("cookie", "session_access_token=" + inputs.accessToken + ";");
        } else {
            headers.put("Authorization", inputs.accessToken);
        }

        String date = validateDate(inputs.date);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("productId", inputs.productID);
        queryParams.put("packageCode", inputs.packageCode);
        queryParams.put("dateTimeFrom", date + " 00:00:00");
        queryParams.put("dateTimeTo", date + " 23:59:59");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-events-v2-inventory/v1/productSchedules"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }

    private static String validateDate(String date) {
        try {
            return LocalDate.parse(date.trim(), DATE_FORMAT).format(DATE_FORMAT);
        } catch (NullPointerException | DateTimeParseException e) {
            throw new IllegalArgumentException("TTD schedule date must be in yyyy-MM-dd format, but was: " + date, e);
        }
    }
}
