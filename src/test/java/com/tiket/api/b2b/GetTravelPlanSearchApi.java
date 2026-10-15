package com.tiket.api.b2b;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public record GetTravelPlanSearchApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String email,
            long accountId,
            int corporateRoleId,
            int corporateId,
            String searchType,
            String startDate,
            String endDate,
            String productType,
            String planName
    ) {
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com","https://corporate.preprod.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/TravelPlans/GetTravelPlanSearchApi_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("email",inputs.email);
        headers.put("x-account-id", inputs.accountId);
        headers.put("x-corporate-accessrole-id", inputs.corporateRoleId);
        headers.put("x-corporate-id", inputs.corporateId);
        headers.put("referer",uriHelper.getUri() + "/travel-plan/my-requests");

        Map<String, Object> queryParams = getStringObjectMap();

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/travel-plans"))
                .setHeaderParams(headers)
                .setQueryParams(queryParams)
                .build();

        return apiTools.get(request);
    }

    private Map<String, Object> getStringObjectMap() {
        Map<String, Object> queryParams = getStaticMap("src/test/resources/api/b2b/TravelPlans/GetTravelPlanSearchApi_queryParam.properties");
        queryParams.put("status", inputs.searchType);
        queryParams.put("startDate", inputs.startDate);
        queryParams.put("endDate", inputs.endDate);
        queryParams.put("approverView", false);
        queryParams.put("search", inputs.planName);

        List<String> productTypes = Arrays.asList("flight", "hotel", "train","whoosh","car");

        if (inputs.productType.equalsIgnoreCase("all")) {
            queryParams.put("productTypes", productTypes);
        } else {
            if (productTypes.contains(inputs.productType)) {
                queryParams.put("productTypes", Arrays.asList(inputs.productType));
            } else {
                throw new IllegalArgumentException("Invalid product type: " + inputs.productType);
            }
        }
        return queryParams;
    }
}
