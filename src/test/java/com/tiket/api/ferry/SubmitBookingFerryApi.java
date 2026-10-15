package com.tiket.api.ferry;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

public record SubmitBookingFerryApi(Input input) implements BaseApi{
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public record Input(
        String accountUsername,
        String accessToken,
        boolean licensePlate,
        String cartId,
        String secretId,
        int adult,
        int child,
        int infant
    ) {
    }

    private JSONArray generatePassengerData(int adult, int child,int infant){
        JSONArray arrPassenger = new JSONArray();
        JSONObject emptyObject = new JSONObject();
        for(int i = 0 ; i< adult ; i++){
            JSONObject objPasseger = new JSONObject();
            objPasseger.put("paxType", "ADULT");
                JSONObject vaccinveData = new JSONObject();
                vaccinveData.put("isSuccess", true);
                vaccinveData.put("vaccinationData", emptyObject);
                objPasseger.put("vaccineData",vaccinveData);
            objPasseger.put("title", "Mr");
            objPasseger.put("fullName", CoreFunctions.randomName());
            objPasseger.put("profileId", "");
            objPasseger.put("dateOfBirth", CoreFunctions.generateRandomBirthDateString(18,40, "1-10", dateFormat));
            objPasseger.put("identityType", "driverLicense");
            objPasseger.put("issuingCountry", "ID");
            objPasseger.put("originCity", "Kota Semarang");
            objPasseger.put("identityValue", "327" + CoreFunctions.generateRandomInt(11));

            arrPassenger.put(objPasseger);
        }

        for(int i = 0 ; i< child ; i++){
            JSONObject objPasseger = new JSONObject();
            objPasseger.put("paxType", "CHILD");
            objPasseger.put("title", "Mr");
            objPasseger.put("fullName", CoreFunctions.randomName());
            objPasseger.put("profileId", "");
            objPasseger.put("dateOfBirth", CoreFunctions.generateRandomBirthDateString(3,5, "1-10", dateFormat));
            objPasseger.put("identityType", "dateOfBirth");
            objPasseger.put("originCity", "Kota Semarang");
            JSONObject vaccinveData = new JSONObject();
            vaccinveData.put("isSuccess", true);
            vaccinveData.put("vaccinationData", emptyObject);
            objPasseger.put("vaccineData",vaccinveData);
            arrPassenger.put(objPasseger);
        }

        for(int i = 0 ; i< infant ; i++){
            JSONObject objPasseger = new JSONObject();
            objPasseger.put("paxType", "INFANT");
            objPasseger.put("title", "Mr");
            objPasseger.put("fullName", CoreFunctions.randomName());
            objPasseger.put("profileId", "");
            objPasseger.put("dateOfBirth", CoreFunctions.generateRandomBirthDateString(0,1, "1-10", dateFormat));
            objPasseger.put("identityType", "dateOfBirth");
            objPasseger.put("originCity", "Kota Semarang");
            JSONObject vaccinveData = new JSONObject();
            vaccinveData.put("isSuccess", true);
            vaccinveData.put("vaccinationData", emptyObject);
            objPasseger.put("vaccineData",vaccinveData);
            arrPassenger.put(objPasseger);
        }
        
        return arrPassenger;
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/ferry/SubmitBookingFerryHeaders.properties");
        headers.put("Authorization",input.accessToken);

        JSONObject payload = new JSONObject();
        JSONObject contact = new JSONObject();
        contact.put("areaCode", "+62");
        contact.put("phone", "81" + CoreFunctions.randomInt(9));
        contact.put("emailAddress", input.accountUsername);
        contact.put("fullName", CoreFunctions.randomName());
        contact.put("title", "mr");
        payload.put("contact", contact);
        payload.put("passengers", generatePassengerData(input.adult, input.child, input.infant));
        if(input.licensePlate) payload.put("licensePlate", "DR"+CoreFunctions.generateRandomInt(4)+"YQ");
        payload.put("latitude", -7.0234113);
        payload.put("longitude", 110.4665578);

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-ferry-demand/v1/carts/"+input.cartId+"?cartSecret="+input.secretId))
                .setBody(payload.toString())
                .setHeaderParams(headers)
                .build();

        return apiTools.post(request);
    }

}