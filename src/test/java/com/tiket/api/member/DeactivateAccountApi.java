package com.tiket.api.member;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record DeactivateAccountApi(DeactivateAccountApi.Inputs inputs) implements BaseApi {
    public record Inputs(
            String authorization,
            long userAccountId,
            String emailRequester,
            String note
    ) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://member-core-v2-be-svc.preprod-platform-cluster.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/member/MemberVerificationByAdmin_header.properties");
        headers.put("Authorization", inputs.authorization);

        JSONObject body = getStaticJson("src/test/resources/api/member/DeactivateAccount_body.json");
        body.put("accountId", inputs.userAccountId);
        body.put("emailRequester", inputs.emailRequester);
        body.put("note", inputs.note);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-member-core/v2/account/status")).setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.put(request);
    }
}
