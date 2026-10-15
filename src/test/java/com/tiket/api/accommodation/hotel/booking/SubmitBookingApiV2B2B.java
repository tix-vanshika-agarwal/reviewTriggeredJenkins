package com.tiket.api.accommodation.hotel.booking;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.accommodation.hotel.SubmitBookingV2B2BBody;
import com.tiket.pojo.b2b.corporate.PostInterimDetailsApiResponse;
import com.tiket.pojo.member.AccountData;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tiket.core.CoreFunctions.stripBearerPrefix;

public record SubmitBookingApiV2B2B(Inputs inputs) implements BaseApi {

    /**
     * @param accessToken the B2B access token, sent as the {@code session_access_token} cookie;
     *                    a {@code "Bearer "} prefix is dropped so callers can pass either shape
     */
    public record Inputs(
            String accessToken,
            String currency,
            AccountData accountData,
            String guestFullName,
            String guestTitle,
            String hotelId,
            String cartId,
            String corporateInterimID,
            PostInterimDetailsApiResponse postInterimDetailsApiResponse
    ) {
        public static List<String> guestNames = new ArrayList<>();
        public List<String> getGuestNames() {
            return guestNames;
        }

        public void addGuestName(String guestFullName) {
            guestNames.add(guestFullName);
        }
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/booking/SubmitBookingV2B2B_headers.properties");
        headers.put("cookie","session_access_token="+stripBearerPrefix(inputs.accessToken)+";");
        headers.put("currency", inputs.currency);

        SubmitBookingV2B2BBody submitBookingV2B2BBody = getBody();
        Gson gson = new Gson();
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-hotel-cart/hotel/v2/book"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(submitBookingV2B2BBody, SubmitBookingV2B2BBody.class))
                .build();

        return apiTools.post(request);
    }

    private SubmitBookingV2B2BBody getBody() {
        JSONObject body = getStaticJson("src/test/resources/api/accommodation/hotel/booking/SubmitBookingV2B2B_body.json");
        Gson gson = new Gson();

        SubmitBookingV2B2BBody submitBookingV2B2BBody = gson.fromJson(body.toString(), SubmitBookingV2B2BBody.class);
        AccountData.Data accountData = inputs.accountData.data;
        submitBookingV2B2BBody.contactPerson.email = accountData.accountUsername;
        submitBookingV2B2BBody.contactPerson.fullName = accountData.firstName + " " + accountData.lastName;
        submitBookingV2B2BBody.contactPerson.title = "Mr";
        submitBookingV2B2BBody.contactPerson.profileId = accountData.profileId;
        submitBookingV2B2BBody.contactPerson.areaCode = accountData.accountPhoneCode;
        submitBookingV2B2BBody.contactPerson.phone = accountData.accountPhoneNumber;
        submitBookingV2B2BBody.contactPerson.countryId = "indonesia";

        submitBookingV2B2BBody.hotel.get(0).hotelId = inputs.hotelId;
        submitBookingV2B2BBody.hotel.get(0).books.get(0).cartId = inputs.cartId;
        SubmitBookingV2B2BBody.GuestDetail guestDetail = new SubmitBookingV2B2BBody.GuestDetail();
        guestDetail.nationalityCode = "ID";
        guestDetail.profileId = accountData.profileId;
        guestDetail.fullName = inputs.guestFullName;
        inputs.addGuestName(inputs.guestFullName);
        guestDetail.title = inputs.guestTitle;
        submitBookingV2B2BBody.hotel.get(0).books.get(0).rooms.get(0).guestDetail = guestDetail;

        SubmitBookingV2B2BBody.B2bInterimPageData b2bInterimPageData = new SubmitBookingV2B2BBody.B2bInterimPageData();
        b2bInterimPageData.corporateInterimID = inputs.corporateInterimID;
        b2bInterimPageData.serviceFee = inputs.postInterimDetailsApiResponse.data.corporateInformation.serviceFee;
        b2bInterimPageData.serviceFeeType = inputs.postInterimDetailsApiResponse.data.corporateInformation.serviceFeeType;
        b2bInterimPageData.transactionalStatus = inputs.postInterimDetailsApiResponse.data.corporateInformation.transactionalStatus;
        submitBookingV2B2BBody.b2bInterimPageData = b2bInterimPageData;
        return submitBookingV2B2BBody;
    }
}
