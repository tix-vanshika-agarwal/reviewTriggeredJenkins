package com.tiket.api.dashboard.eiffel.lounge;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record LoungeResetPasswordUserApi(LoungeResetPasswordUserApi.Inputs inputs) implements BaseApi {
    public record Inputs(
            String email
    ){}

    static class ResetPasswordBody {
        String email;
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/dashboard/eiffel/lounge/LoungeResetPasswordUserApi_headers.properties");

        JSONObject jsonObject = getStaticJson("src/test/resources/api/dashboard/eiffel/lounge/LoungeResetPasswordUserApi_body.json");
        Gson gson = new Gson();
        ResetPasswordBody resetPasswordBody = gson.fromJson(jsonObject.toString(), ResetPasswordBody.class);
        resetPasswordBody.email = inputs.email;

        UriHelper uriHelper = new UriHelper("https://loyalty-lounge-be-svc.preprod-platform-cluster.tiket.com");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/tix-loyalty-lounge/users/forget-password"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(resetPasswordBody, ResetPasswordBody.class))
                .build();

        return apiTools.post(request);
    }
}
