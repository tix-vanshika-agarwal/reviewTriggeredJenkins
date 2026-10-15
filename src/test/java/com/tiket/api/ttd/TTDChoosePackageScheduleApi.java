package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @param useCookieAuth when {@code true}, the access token is sent as the {@code session_access_token} cookie
 *                      instead of the {@code Authorization} header (e.g. for B2B corporate accounts)
 */
public record TTDChoosePackageScheduleApi(Inputs inputs, boolean useCookieAuth) implements BaseApi {

    /** Defaults to sending the access token in the {@code Authorization} header. */
    public TTDChoosePackageScheduleApi(Inputs inputs) {
        this(inputs, false);
    }

    public record Inputs(
            String accessToken,
            String productID,
            String packageCode,
            int dateRange,
            int monthRange,
            String startDate,
            String endDate
    ) {
        public Inputs(String accessToken, String productID, String packageCode, int dateRange, int monthRange) {
            this(accessToken, productID, packageCode, dateRange, monthRange, null, null);
        }

        public Inputs(String accessToken, String productID, String packageCode, int dateRange, int monthRange, String startDate) {
            this(accessToken, productID, packageCode, dateRange, monthRange, startDate, null);
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/TTDChoosePackageSchedule_headers.properties");
        if (useCookieAuth) {
            headers.put("cookie", "session_access_token=" + inputs.accessToken + ";");
        } else {
            headers.put("Authorization", inputs.accessToken);
        }

        DateFormat dateFormatNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat dateFormatLast = new SimpleDateFormat("yyyy-MM-dd 23:59:59");

        dateFormatNow.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        dateFormatLast.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));

        Date date = new Date();
        System.out.println(dateFormatNow.format(date));

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("productId", inputs.productID);
        queryParams.put("packageCode", inputs.packageCode);

        if (inputs.startDate != null && !inputs.startDate.isBlank()) {
            DateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");
            parseFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
            Date start = parseFormat.parse(inputs.startDate);
            queryParams.put("dateTimeFrom", dateFormatNow.format(start));
            Date end = (inputs.endDate != null && !inputs.endDate.isBlank())
                    ? parseFormat.parse(inputs.endDate)
                    : start;
            queryParams.put("dateTimeTo", dateFormatLast.format(end));
        } else {
            queryParams.put("dateTimeFrom", dateFormatNow.format(date));

            if(date.getDate() + inputs.dateRange > 28) {
                date.setDate(date.getDate() - 25);

                if(date.getMonth() + inputs.monthRange > 12) {
                    date.setYear(date.getYear()+1);
                    date.setMonth(1);
                } else {
                    date.setMonth(date.getMonth()+inputs.monthRange);
                }
            } else {
                date.setDate(date.getDate() + 3);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, 1);
            String updatedDate = dateFormatLast.format(calendar.getTime());
            queryParams.put("dateTimeTo", updatedDate);
        }

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-events-v2-inventory/v1/productSchedules"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
