package com.tiket.api.ttd;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @param useCookieAuth when {@code true}, the access token is sent as the {@code session_access_token} cookie
 *                      instead of the {@code Authorization} header (e.g. for B2B corporate accounts)
 */
public record TTDChoosePackageApi(Inputs inputs, boolean useCookieAuth) implements BaseApi {

    /** Defaults to sending the access token in the {@code Authorization} header. */
    public TTDChoosePackageApi(Inputs inputs) {
        this(inputs, false);
    }

    public record Inputs(
            String accessToken,
            String productID
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/TTDChoosePackage_headers.properties");
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

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("productId", inputs.productID);
        queryParams.put("dateTimeFrom", dateFormatNow.format(date));

        if(date.getDate() + 3 > 28) {
            date.setDate(date.getDate() - 25);

            if(date.getMonth() + 1 > 12) {
                date.setYear(date.getYear()+1);
                date.setMonth(1);
            } else {
                date.setMonth(date.getMonth()+1);
            }
        } else {
            date.setDate(date.getDate() + 3);
        }
        queryParams.put("dateTimeTo", dateFormatLast.format(date));


        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-events-v2-inventory/v1/productSchedules/availablePackages"))
                .setQueryParams(queryParams)
                .setHeaderParams(headers)
                .build();

        return apiTools.get(request);
    }
}
