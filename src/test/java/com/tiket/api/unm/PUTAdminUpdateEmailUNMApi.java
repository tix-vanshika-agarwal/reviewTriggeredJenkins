package com.tiket.api.unm;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

/**
 * Admin (UNM dashboard-be) endpoint that updates a user's email. Setting a brand-new, unique
 * email leaves it in an <b>unverified</b> state, which is how a logged-in account is forced into
 * the "email not verified" scenario for the account-center email-verification flow.
 *
 * <p>{@code X-User-Id} is the admin user id performing the change (not the target user).
 * {@code unmId} in the path is the target user's UNM id; {@code newEmail} must be unique.
 */
public record PUTAdminUpdateEmailUNMApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            long unmId,
            String newEmail,
            String note,
            String password
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/unm/PUTAdminUpdateEmailUNMApi_headers.properties");

        JSONObject body = new JSONObject();
        body.put("newEmail", inputs.newEmail);
        body.put("note", inputs.note);
        body.put("password", inputs.password);

        UriHelper helper = new UriHelper("https://sandbox.eph.bliblitiket.tools");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/gks-unm-go-dashboard-be/api/v1/admin/users/" + inputs.unmId + "/update-email"))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.put(request);
    }
}
