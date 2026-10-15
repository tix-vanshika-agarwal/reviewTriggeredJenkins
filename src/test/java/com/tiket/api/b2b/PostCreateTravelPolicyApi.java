package com.tiket.api.b2b;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.b2b.corporate.EmployeeData;
import com.tiket.pojo.b2b.corporate.PostCreateTravelPolicyApiBodyAll;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record PostCreateTravelPolicyApi(Inputs inputs) implements BaseApi {
    public record Inputs(
        EmployeeData employeeData,
        String accessToken,
        long accountId,
        String policyName,
        String type,
        PostCreateTravelPolicyApiBodyAll.Flight flight,
        PostCreateTravelPolicyApiBodyAll.Hotel hotel
    ){ }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/TravelPolicy/PostCreateTravelPolicy_headers.properties");
        headers.put("cookie","session_access_token="+inputs.accessToken+";");
        headers.put("x-account-id",inputs.accountId);
        headers.put("x-corporate-id",inputs.employeeData.data.corporateID);


        JSONObject body = getStaticJson("src/test/resources/api/b2b/TravelPolicy/PostCreateTravelPolicy_body.json");
        Gson gson = new Gson();
        PostCreateTravelPolicyApiBodyAll postCreateTravelPolicyApiBodyAll = gson.fromJson(body.toString(), PostCreateTravelPolicyApiBodyAll.class);
        postCreateTravelPolicyApiBodyAll.name = inputs.policyName;
        if (inputs.type.equals("flight")) {
            postCreateTravelPolicyApiBodyAll.details.flight = inputs.flight;
        } else if (inputs.type.equals("hotel")) {
            postCreateTravelPolicyApiBodyAll.details.hotel = inputs.hotel;
        }

        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");
        
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-corporate/corporate-dashboard/travel-policy"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(postCreateTravelPolicyApiBodyAll, PostCreateTravelPolicyApiBodyAll.class))
                .build();
        return apiTools.post(request);
    }
}
