package com.tiket.api.member;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record MemberVerificationByAdminApi(MemberVerificationByAdminApi.Inputs inputs) implements BaseApi {
    public record Inputs(
            String userAccountId,
            String authorization,
            String note,
            boolean verification
    ) {
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://members-core-be-svc.preprod-platform-cluster.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/member/MemberVerificationByAdmin_header.properties");
        headers.put("Authorization", inputs.authorization);

        JSONObject body = getStaticJson("src/test/resources/api/member/MemberVerificationByAdmin_body.json");
        body.put("accountId", inputs.userAccountId);
        body.put("note", inputs.note);
        body.put("isVerified",inputs.verification);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("tix-members-core/verification/admin")).setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.put(request);
    }
}
