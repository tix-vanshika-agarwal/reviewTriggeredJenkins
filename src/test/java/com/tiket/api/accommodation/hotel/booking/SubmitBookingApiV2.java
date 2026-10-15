package com.tiket.api.accommodation.hotel.booking;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.accommodation.hotel.SubmitBookingV2Body;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tiket.core.CoreFunctions.*;

public record SubmitBookingApiV2(Inputs inputs) implements BaseApi {

    /**
     * Plain class so each booking keeps its own guest-name list for parallel tests.
     */
    public static final class Inputs {
        public final String authorization;
        public final String username;
        public final String profileId;
        public final String currency;
        public final String language;
        public final String contactEmail;
        public final String contactFullName;
        public final String contactTitle;
        public final String hotelId;
        public final String cartId;
        public final int roomCount;
        public final String guestFullName;
        public final String guestTitle;
        public final int addonsAmount;
        public final String addonsId;
        public final String checkInDate;
        public final boolean isSpecialRequestEnable;
        public final String prefixSpecialRequest;
        public final String insurance;
        public final String insuranceCredential;

        private final List<String> guestNames = new ArrayList<>();

        private Inputs(
                String authorization,
                String username,
                String profileId,
                String currency,
                String language,
                String contactEmail,
                String contactFullName,
                String contactTitle,
                String hotelId,
                String cartId,
                int roomCount,
                String guestFullName,
                String guestTitle,
                int addonsAmount,
                String addonsId,
                String checkInDate,
                boolean isSpecialRequestEnable,
                String prefixSpecialRequest,
                String insurance,
                String insuranceCredential
        ) {
            this.authorization = authorization;
            this.username = username;
            this.profileId = profileId == null ? "" : profileId;
            this.currency = currency;
            this.language = language;
            this.contactEmail = contactEmail;
            this.contactFullName = contactFullName;
            this.contactTitle = contactTitle;
            this.hotelId = hotelId == null ? "" : hotelId;
            this.cartId = cartId;
            this.roomCount = Math.max(roomCount, 1);
            this.guestFullName = guestFullName;
            this.guestTitle = guestTitle;
            this.addonsAmount = addonsAmount;
            this.addonsId = addonsId;
            this.checkInDate = checkInDate == null ? "" : checkInDate;
            this.isSpecialRequestEnable = isSpecialRequestEnable;
            this.prefixSpecialRequest = prefixSpecialRequest == null ? "" : prefixSpecialRequest;
            this.insurance = insurance;
            this.insuranceCredential = insuranceCredential;
        }

        /** Add-ons booking flow (random contact/guest names). */
        public Inputs(
                String authorization,
                String profileId,
                String currency,
                String contactEmail,
                String hotelId,
                String cartId,
                int addonsAmount,
                String addonsId,
                String checkInDate
        ) {
            this(authorization, "", profileId, currency, "id", contactEmail, null, null,
                    hotelId, cartId, 1, null, null, addonsAmount, addonsId, checkInDate,
                    false, "", null, null);
        }

        /** Add-ons booking flow with optional special request. */
        public Inputs(
                String authorization,
                String profileId,
                String currency,
                String contactEmail,
                String hotelId,
                String cartId,
                int addonsAmount,
                String addonsId,
                String checkInDate,
                boolean isSpecialRequestEnable,
                String prefixSpecialRequest
        ) {
            this(authorization, "", profileId, currency, "id", contactEmail, null, null,
                    hotelId, cartId, 1, null, null, addonsAmount, addonsId, checkInDate,
                    isSpecialRequestEnable, prefixSpecialRequest, null, null);
        }

        /** Standard hotel booking flow. */
        public Inputs(
                String authorization,
                String username,
                String cartId,
                String contactEmail,
                String contactFullName,
                String contactTitle,
                String guestFullName,
                String guestTitle,
                int roomCount,
                String currency,
                boolean isSpecialRequestEnable,
                String prefixSpecialRequest,
                String language,
                String hotelId,
                String checkInDate
        ) {
            this(authorization, username, "", currency, language, contactEmail, contactFullName, contactTitle,
                    hotelId, cartId, roomCount, guestFullName, guestTitle, 0, "0", checkInDate,
                    isSpecialRequestEnable, prefixSpecialRequest, null, null);
        }

        /** Hotel booking with travel insurance. */
        public Inputs(
                String authorization,
                String username,
                String cartId,
                String contactEmail,
                String contactFullName,
                String contactTitle,
                String guestFullName,
                String guestTitle,
                int roomCount,
                String currency,
                boolean isSpecialRequestEnable,
                String prefixSpecialRequest,
                String language,
                String insurance,
                String insuranceCredential,
                String hotelId,
                String checkInDate
        ) {
            this(authorization, username, "", currency, language, contactEmail, contactFullName, contactTitle,
                    hotelId, cartId, roomCount, guestFullName, guestTitle, 0, "0", checkInDate,
                    isSpecialRequestEnable, prefixSpecialRequest, insurance, insuranceCredential);
        }

        public List<String> getGuestNames() {
            return List.copyOf(guestNames);
        }

        void addGuestName(String guestName) {
            guestNames.add(guestName);
        }

        public String getSpecialRequest() {
            return prefixSpecialRequest + cartId;
        }
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/booking/SubmitBookingV2_headers.properties");
        headers.put("Authorization", inputs.authorization);
        String resolvedCurrency = inputs.currency == null || inputs.currency.isBlank() ? "IDR" : inputs.currency.toUpperCase();
        headers.put("currency", resolvedCurrency);
        headers.put("X-Currency", resolvedCurrency);
        if (inputs.username != null && !inputs.username.isBlank()) {
            headers.put("Username", inputs.username);
        }
        if (inputs.language != null && !inputs.language.isBlank()) {
            headers.put("lang", inputs.language);
            headers.put("language", inputs.language);
            headers.put("Accept-Language", inputs.language);
        }

        Gson gson = new Gson();
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-hotel-cart/hotel/v2/book"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(buildBody(), SubmitBookingV2Body.class))
                .build();

        return apiTools.post(request);
    }

    private SubmitBookingV2Body buildBody() {
        JSONObject body = getStaticJson("src/test/resources/api/accommodation/hotel/booking/SubmitBookingV2_body.json");
        Gson gson = new Gson();
        SubmitBookingV2Body submitBookingV2Body = gson.fromJson(body.toString(), SubmitBookingV2Body.class);

        submitBookingV2Body.contactPerson.email = inputs.contactEmail;
        submitBookingV2Body.contactPerson.profileId = inputs.profileId;
        if (inputs.contactFullName != null && !inputs.contactFullName.isBlank()) {
            submitBookingV2Body.contactPerson.fullName = inputs.contactFullName;
            submitBookingV2Body.contactPerson.title = inputs.contactTitle;
        } else {
            submitBookingV2Body.contactPerson.fullName = randomName() + " autguest";
            submitBookingV2Body.contactPerson.title = capitalize(randomSalutation());
        }

        SubmitBookingV2Body.Hotel hotel = submitBookingV2Body.hotel.get(0);
        hotel.hotelId = inputs.hotelId;
        SubmitBookingV2Body.Books book = hotel.books.get(0);
        book.cartId = inputs.cartId;

        if (inputs.checkInDate != null && !inputs.checkInDate.isBlank() && !"0".equals(inputs.checkInDate)) {
            book.checkIn = withCheckInTime(inputs.checkInDate);
        }

        if (inputs.addonsAmount > 0) {
            book.bookingAddOnRequests = new ArrayList<>();
            SubmitBookingV2Body.BookingAddOnRequests bookingAddOnRequests = new SubmitBookingV2Body.BookingAddOnRequests();
            bookingAddOnRequests.addons = new ArrayList<>();
            SubmitBookingV2Body.Addons addons = new SubmitBookingV2Body.Addons();
            addons.amount = inputs.addonsAmount;
            addons.id = inputs.addonsId;
            bookingAddOnRequests.addons.add(addons);
            bookingAddOnRequests.date = inputs.checkInDate;
            book.bookingAddOnRequests.add(bookingAddOnRequests);
        } else {
            book.bookingAddOnRequests = new ArrayList<>();
        }

        if (inputs.isSpecialRequestEnable) {
            book.specialRequest = inputs.getSpecialRequest();
        }

        if (inputs.insurance != null && !inputs.insurance.isBlank()) {
            submitBookingV2Body.insurances = new ArrayList<>(List.of(inputs.insurance));
        } else {
            submitBookingV2Body.insurances = new ArrayList<>();
        }

        if (inputs.insuranceCredential != null && !inputs.insuranceCredential.isBlank()) {
            submitBookingV2Body.insurancesCredentials = new ArrayList<>(List.of(inputs.insuranceCredential));
        } else {
            submitBookingV2Body.insurancesCredentials = new ArrayList<>();
        }

        ArrayList<SubmitBookingV2Body.Room> rooms = new ArrayList<>();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        for (int i = 0; i < inputs.roomCount; i++) {
            SubmitBookingV2Body.Room room = new SubmitBookingV2Body.Room();
            SubmitBookingV2Body.GuestDetail guestDetail = new SubmitBookingV2Body.GuestDetail();
            String resolvedGuestName;
            String resolvedGuestTitle;
            if (inputs.guestFullName != null && !inputs.guestFullName.isBlank()) {
                resolvedGuestName = i == 0 ? inputs.guestFullName : randomName() + " autguest";
                resolvedGuestTitle = i == 0 ? inputs.guestTitle : getRandomObject(salutationList);
            } else {
                resolvedGuestName = randomName() + " autguest";
                resolvedGuestTitle = capitalize(randomSalutation());
            }
            guestDetail.fullName = resolvedGuestName;
            guestDetail.title = resolvedGuestTitle;
            guestDetail.profileId = "";
            room.guestDetail = guestDetail;
            room.nights = new ArrayList<>();
            rooms.add(room);
            inputs.addGuestName(resolvedGuestName);
        }
        book.rooms = rooms;
        return submitBookingV2Body;
    }

    private static final String DEFAULT_CHECK_IN_TIME = "15:00";

    /**
     * The book API expects {@code checkIn} as {@code yyyy-MM-dd HH:mm}. When only a date is
     * provided, append the default hotel check-in time so the request is not rejected (5xx).
     */
    private static String withCheckInTime(String checkInDate) {
        String trimmed = checkInDate.trim();
        if (trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return trimmed + " " + DEFAULT_CHECK_IN_TIME;
        }
        return trimmed;
    }
}
