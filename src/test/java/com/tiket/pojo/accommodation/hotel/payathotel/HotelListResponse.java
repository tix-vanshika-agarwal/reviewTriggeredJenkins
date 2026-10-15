package com.tiket.pojo.accommodation.hotel.payathotel;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Price engine {@code POST /price/v2/hotel-list} response (Pay at Hotel summary flow).
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelListResponse {

    private String code;
    private String message;
    private Data data;
    private List<String> errors;
    private long serverTime;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private List<Hotel> hotels;
        private String currency;
        private int scale;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Hotel {
        private String hotelId;
        private HotelBenefits hotelBenefits;
        @JsonAlias("hotel_room")
        private List<HotelRoom> hotelRoom;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HotelBenefits {
        @JsonProperty("pay_at_hotel")
        private boolean payAtHotel;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HotelRoom {
        private String roomId;
        @JsonAlias("room_benefits")
        private RoomBenefits roomBenefits;
        @JsonAlias("payment_option")
        private String paymentOption;
        private String vendor;
        @JsonAlias("hotel_external_id")
        private String hotelExternalId;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RoomBenefits {
        @JsonProperty("pay_at_hotel")
        @JsonAlias("payAtHotel")
        private boolean payAtHotel;
    }
}
