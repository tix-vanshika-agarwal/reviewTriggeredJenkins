package com.tiket.api.auth;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;

/**
 * Dynamic-credentials admin login. Delegates to member-core REST
 * {@code POST /tix-member-core/v3/admin/login} (same contract as curl to
 * {@code member-core-v2-be-svc.*.tiket.com}).
 */
public record POSTGetAdminAccessTokenDashboardDynamicApi(Inputs inputs) implements BaseApi {

    public record Inputs(String username, String password) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        POSTAdminLoginApi.Inputs postInputs = new POSTAdminLoginApi.Inputs(
                inputs.username(), inputs.password(), "INTERNAL", null);
        return new POSTAdminLoginApi(postInputs).hitApi(apiTools);
    }
}
