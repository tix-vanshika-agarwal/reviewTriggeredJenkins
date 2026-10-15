package com.tiket.api.b2b;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record PostCorporateDashboardSettingsApi(Inputs inputs) implements BaseApi {

    public record Config(String name, Object value) {}

    public record Inputs(
            String accessToken,
            List<Config> configList
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        UriHelper helper = new UriHelper("https://corporate.preprod.tiket.com");

        Map<String, Object> headers = new HashMap<>();
        headers.put("accept", "*/*");
        headers.put("content-type", "application/json");
        headers.put("lang", "id");
        headers.put("origin", "https://corporate.preprod.tiket.com");
        headers.put("referer", "https://corporate.preprod.tiket.com/settings");
        headers.put("serviceid", "corporate-external");
        headers.put("x-account-id", "4916");
        headers.put("x-audience", "tiket.com");
        headers.put("x-cookie-session-v2", "true");
        headers.put("x-corporate-accessrole-id", "1");
        headers.put("x-corporate-id", "2431");
        headers.put("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");
        headers.put("cookie", "session_access_token=" + inputs.accessToken + ";");

        Gson gson = new Gson();
        Map<String, Object> body = new HashMap<>();
        body.put("config", inputs.configList);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/corporate-dashboard/settings"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(body))
                .build();

        return apiTools.post(request);
    }
}
