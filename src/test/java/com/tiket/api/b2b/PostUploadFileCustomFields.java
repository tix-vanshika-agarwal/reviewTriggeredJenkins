package com.tiket.api.b2b;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import okhttp3.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public record PostUploadFileCustomFields(Inputs inputs) implements BaseApi {
    public record Inputs(
            String accessToken,
            long accountId,
            int corporateId,
            int accessRoleId
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/b2b/TravelPlans/PostUploadFile_headers.properties");
        headers.put("x-account-id", inputs.accountId);
        headers.put("x-corporate-id", inputs.corporateId);
        headers.put("x-corporate-accessrole-id", inputs.accessRoleId);
        headers.put("Authorization", inputs.accessToken);

        File file = new File("src/test/resources/b2b/image.png");
        Map<String, Object> multiParts = new HashMap<>();
        multiParts.put("file", file); // file field
        multiParts.put("type", "custom_field"); // string field

        UriHelper helper = new UriHelper("https://corporate-gatotkaca.tiket.com", "https://corporate.preprod.tiket.com");
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/corporate-gateway/tix-corporate/uploader/upload"))
                .setHeaderParams(headers)
                .setMultiParts(multiParts)
                .build();

        return apiTools.post(request);
    }
}
