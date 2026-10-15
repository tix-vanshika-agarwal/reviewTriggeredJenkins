package com.tiket.api.carTransfer;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.cartransfer.CheckOutBodyAT;
import com.tiket.pojo.member.SmartProfileResponse;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.Map;

public record CarTransferCheckOutApi(Inputs inputs) implements BaseApi {
    /**
     * Inputs for the CarTransferCheckOutApi.
     *
     * @param accessToken        The access token for authentication.
     * @param cartId             The ID of the cart to check out.
     * @param smartProfileData   The smart profile data for the user.
     * @param detailName         The name of the detail to be added to the checkout.
     * @param flightNumber       The flight number associated with the checkout.
     */
    public record Inputs(
            String accessToken,
            String cartId,
            SmartProfileResponse.Data smartProfileData,
            String detailName,
            String flightNumber
    ) { }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/carTransfer/CarTransferCheckOut_headers.properties");
        headers.put("Authorization", inputs.accessToken);
        headers.put("referer", uriHelper.getUri() + "/airport-transfer/booking");
        headers.put("origin", uriHelper.getUri());

        Gson gson = new Gson();

        JSONObject body = getStaticJson("src/test/resources/api/carTransfer/CarTransferCheckOut_body.json");

        CheckOutBodyAT checkOutBodyAT = gson.fromJson(body.toString(), CheckOutBodyAT.class);
        checkOutBodyAT.cartId=inputs.cartId;

        //fallback to MR if salutation code is empty
        String title = inputs.smartProfileData.accountSalutationName != null ? inputs.smartProfileData.accountSalutationName : "MR";
        checkOutBodyAT.contactPerson.profileId=inputs.smartProfileData.id;
        checkOutBodyAT.contactPerson.title = title;
        checkOutBodyAT.contactPerson.accountId=inputs.smartProfileData.accountId;
        checkOutBodyAT.contactPerson.email=inputs.smartProfileData.accountEmail;
        checkOutBodyAT.contactPerson.firstName=inputs.smartProfileData.accountFirstName;
        checkOutBodyAT.contactPerson.lastName=inputs.smartProfileData.accountLastName;
        checkOutBodyAT.contactPerson.phoneCode=inputs.smartProfileData.accountPhoneCode;
        checkOutBodyAT.contactPerson.phoneNumber=inputs.smartProfileData.accountPhone;

        checkOutBodyAT.details.add(new CheckOutBodyAT.Detail());
        checkOutBodyAT.details.get(0).name = inputs.detailName;
        checkOutBodyAT.details.get(0).value = inputs.flightNumber;

        checkOutBodyAT.passengers.get(0).profileId=inputs.smartProfileData.id;
        checkOutBodyAT.passengers.get(0).phoneCode=inputs.smartProfileData.accountPhoneCode;
        checkOutBodyAT.passengers.get(0).phoneNumber = inputs.smartProfileData.accountPhone;
        checkOutBodyAT.passengers.get(0).title = title;
        checkOutBodyAT.passengers.get(0).firstName=inputs.smartProfileData.accountFirstName;
        checkOutBodyAT.passengers.get(0).lastName=inputs.smartProfileData.accountLastName;

        checkOutBodyAT.totalUnit = 1;
        checkOutBodyAT.flightInfo.flightNumber = inputs.flightNumber;

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-airport-transfer-order/v4/checkouts"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(checkOutBodyAT, CheckOutBodyAT.class))
                .build();

        return apiTools.post(request);
    }
}
