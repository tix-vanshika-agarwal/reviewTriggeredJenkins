package com.tiket.api.b2b;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.b2b.corporate.EmployeeData;
import com.tiket.pojo.b2b.corporate.PatchTravelPolicyBodyFlightApi;
import com.tiket.pojo.b2b.corporate.PatchTravelPolicyBodyHotelApi;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record PatchTravelPolicyApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            EmployeeData employeeData,
            String accessToken,
            long accountId,
            int policyID,
            int id,
            String type,
            PatchTravelPolicyBodyFlightApi patchTravelPolicyBodyFlightApi,
            PatchTravelPolicyBodyHotelApi patchTravelPolicyBodyHotelApi
    ){ }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/TravelPolicy/PatchTravelPolicyApi_headers.properties");
        headers.put("X-Corporate-Id",inputs.employeeData.data.corporateID);
        headers.put("X-Corporate-Employee-ID",inputs.employeeData.data.employeeID);
        headers.put("x-account-id",inputs.accountId);

        JSONObject bodyFlight = getStaticJson("src/test/resources/api/b2b/TravelPolicy/PatchTravelPolicyApiFlight_body.json");
        JSONObject bodyHotel = getStaticJson("src/test/resources/api/b2b/TravelPolicy/PatchTravelPolicyApiHotel_body.json");
        Gson gson = new Gson();
        PatchTravelPolicyBodyFlightApi patchTravelPolicyBodyFlightApi = gson.fromJson(bodyFlight.toString(),PatchTravelPolicyBodyFlightApi.class);
        PatchTravelPolicyBodyHotelApi patchTravelPolicyBodyHotelApi = gson.fromJson(bodyHotel.toString(),PatchTravelPolicyBodyHotelApi.class);

        if (inputs.type.equals("flight")) {
            patchTravelPolicyBodyFlightApi.details = inputs.patchTravelPolicyBodyFlightApi.details;
        } else if (inputs.type.equals("hotel")) {
            patchTravelPolicyBodyHotelApi.details = inputs.patchTravelPolicyBodyHotelApi.details;
        }

        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-corporate/corporate-dashboard/travel-policy/"+inputs.policyID+"/details/"+inputs.id))
                .setHeaderParams(headers)
                .setBody(inputs.type.equals("flight") ? gson.toJson(patchTravelPolicyBodyFlightApi,PatchTravelPolicyBodyFlightApi.class): gson.toJson(patchTravelPolicyBodyHotelApi,PatchTravelPolicyBodyHotelApi.class))
                .build();
        return apiTools.patch(request);
    }
}
