package com.tiket.api.member;

import com.google.gson.Gson;
import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.member.AddProfileBody;
import com.tiket.testbase.BaseApi;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.Map;

public record CreateProfileApi(Inputs inputs) implements BaseApi {

    public record Inputs(
            String accessToken,
            String firstName,
            String lastName,
            String salutation,
            String nationality,
            String passportNumber,
            String passportExpiryDate,
            String passportIssuedDate,
            String passportIssuingCountry
    ) {
        public Inputs(String accessToken, String firstName, String lastName, String salutation, String nationality) {
            this(accessToken, firstName, lastName, salutation, nationality, "", "", "", "");
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/member/smartProfile_headers.properties");
        headers.put("Authorization", inputs.accessToken);

        String bodyPath = "src/test/resources/api/member/CreateProfile_body.json";

        JSONObject body = getStaticJson(bodyPath);
        Gson gson = new Gson();
        AddProfileBody addProfileBody = gson.fromJson(body.toString(), AddProfileBody.class);

        addProfileBody.accountFirstName = inputs.firstName;
        addProfileBody.accountLastName = inputs.lastName;
        addProfileBody.accountBirthdate = CoreFunctions.generateRandomDate("1-1");
        addProfileBody.accountEmail = CoreFunctions.randomEmail();
        addProfileBody.accountPhone = "812"+CoreFunctions.randomInt(8);
        addProfileBody.accountSalutationName= inputs.salutation;
        String id = String.valueOf(CoreFunctions.randomInt(3));
        id = "327202200202"+id+"1";
        addProfileBody.identityNumber= id;
        addProfileBody.accountNationalityCode= inputs.nationality.isEmpty() ? "ID": inputs.nationality;
        if (inputs.passportNumber != null && !inputs.passportNumber.isBlank()) {
            addProfileBody.passportNumber = inputs.passportNumber;
            addProfileBody.passportExpiryDate = inputs.passportExpiryDate;
            addProfileBody.passportIssuedDate = inputs.passportIssuedDate;
            addProfileBody.passportIssuingCountry = inputs.passportIssuingCountry;
        }

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-profile-core/profiles"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(addProfileBody, AddProfileBody.class))
                .build();

        return apiTools.post(request);
    }
}