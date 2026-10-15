package com.tiket.api.accommodation.hotel.booking;

import com.google.gson.Gson;
import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.accommodation.hotel.SubmitBookingMultiRoomBody;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tiket.core.CoreFunctions.*;

public record SubmitBookingMultiRoomApi(Inputs inputs) implements BaseApi {

    /**
     * Plain class (not a record) so that each booking can keep its generated guest names
     * on a regular instance field. Each {@code new Inputs(...)} owns its own
     * {@code guestNames} list, so parallel tests (which each construct their own
     * {@link Inputs}) cannot see each other's data without any thread-local plumbing.
     */
    public static final class Inputs {
        public final String authorization;
        public final String currency;
        public final String contactEmail;
        public final String hotelId;
        public final int rooms;
        public final int qtyEachRoom;
        public final List<String> cartId;
        public final boolean isSpecialRequestEnable;
        public final String prefixSpecialRequest;
        public final int addonsAmount;
        public final String addonsId;
        public final String checkInDate;

        private final List<String> guestNames = new ArrayList<>();

        public Inputs(
                String authorization,
                String currency,
                String contactEmail,
                String hotelId,
                int rooms,
                int qtyEachRoom,
                List<String> cartId,
                boolean isSpecialRequestEnable,
                String prefixSpecialRequest,
                int addonsAmount,
                String addonsId,
                String checkInDate
        ) {
            this.authorization = authorization;
            this.currency = currency;
            this.contactEmail = contactEmail;
            this.hotelId = hotelId;
            this.rooms = rooms;
            this.qtyEachRoom = qtyEachRoom;
            this.cartId = cartId;
            this.isSpecialRequestEnable = isSpecialRequestEnable;
            this.prefixSpecialRequest = prefixSpecialRequest;
            this.addonsAmount = addonsAmount;
            this.addonsId = addonsId;
            this.checkInDate = checkInDate;
        }

        public Inputs(
                String authorization,
                String currency,
                String contactEmail,
                String hotelId,
                int rooms,
                int qtyEachRoom,
                List<String> cartId,
                boolean isSpecialRequestEnable,
                String prefixSpecialRequest
        ) {
            this(authorization, currency, contactEmail, hotelId, rooms, qtyEachRoom, cartId,
                    isSpecialRequestEnable, prefixSpecialRequest, 0, "0", "0");
        }

        /**
         * @return an immutable snapshot of the names collected for this specific booking call.
         */
        public List<String> getGuestNames() {
            return List.copyOf(guestNames);
        }

        public void addGuestName(String guestFullName) {
            guestNames.add(guestFullName);
        }
    }

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/accommodation/hotel/booking/SubmitBookingMultiRoom_headers.properties");
        headers.put("Authorization", inputs.authorization);
        headers.put("currency", inputs.currency);

        SubmitBookingMultiRoomBody submitBookingMultiRoomBody = getBody();
        Gson gson = new Gson();
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("ms-gateway/tix-hotel-cart/hotel/v2/book"))
                .setHeaderParams(headers)
                .setBody(gson.toJson(submitBookingMultiRoomBody, SubmitBookingMultiRoomBody.class))
                .build();

        return apiTools.post(request);
    }

    private SubmitBookingMultiRoomBody getBody() {
        JSONObject body = getStaticJson("src/test/resources/api/accommodation/hotel/booking/SubmitBookingMultiRoom_body.json");
        Gson gson = new Gson();

        SubmitBookingMultiRoomBody submitBookingMultiRoomBody = gson.fromJson(body.toString(), SubmitBookingMultiRoomBody.class);
        submitBookingMultiRoomBody.contactPerson.email = inputs.contactEmail;
        submitBookingMultiRoomBody.contactPerson.fullName = randomName() + " autguest";
        submitBookingMultiRoomBody.contactPerson.title = capitalize(randomSalutation());
        submitBookingMultiRoomBody.hotel.get(0).hotelId = inputs.hotelId;

        ArrayList<SubmitBookingMultiRoomBody.Book> bookArrayList = new ArrayList<>();
        for (int i = 0; i < inputs().rooms; i++) {
            SubmitBookingMultiRoomBody.Book bookData = new SubmitBookingMultiRoomBody.Book();
            bookData.bookingAddOnRequests = new ArrayList<>();
            String cartData = inputs.cartId.get(i);
            bookData.cartId = cartData;
            bookData.childAges = new ArrayList<>();
            if (inputs.isSpecialRequestEnable) bookData.specialRequest = inputs.prefixSpecialRequest + cartData;

            if (inputs.addonsAmount > 0) {
                SubmitBookingMultiRoomBody.BookingAddOnRequests bookingAddOnRequests = new SubmitBookingMultiRoomBody.BookingAddOnRequests();
                bookingAddOnRequests.addons = new ArrayList<>();
                SubmitBookingMultiRoomBody.Addons addons = new SubmitBookingMultiRoomBody.Addons();
                addons.amount = inputs.addonsAmount;
                addons.id = inputs.addonsId;
                bookingAddOnRequests.addons.add(addons);
                bookingAddOnRequests.date = inputs.checkInDate;
                bookData.bookingAddOnRequests.add(bookingAddOnRequests);
            }

            ArrayList<SubmitBookingMultiRoomBody.Room> roomArrayList = new ArrayList<>();
            for (int j = 0; j < inputs().qtyEachRoom; j++) {
                SubmitBookingMultiRoomBody.GuestDetail guestDetailData = new SubmitBookingMultiRoomBody.GuestDetail();
                SubmitBookingMultiRoomBody.Room roomData = new SubmitBookingMultiRoomBody.Room();
                String guestFullName = randomName() + " autguest";
                guestDetailData.fullName = guestFullName;
                guestDetailData.title = capitalize(randomSalutation());
                roomData.guestDetail = guestDetailData;
                roomArrayList.add(roomData);
                inputs.addGuestName(guestFullName);
            }

            bookData.rooms = roomArrayList;
            bookArrayList.add(bookData);
        }

        submitBookingMultiRoomBody.hotel.get(0).books = bookArrayList;
        return submitBookingMultiRoomBody;
    }
}
