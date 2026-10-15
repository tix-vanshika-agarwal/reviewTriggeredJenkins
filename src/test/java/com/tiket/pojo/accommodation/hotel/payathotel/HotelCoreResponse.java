package com.tiket.pojo.accommodation.hotel.payathotel;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Core BE response for {@code GET /tix-hotel-core/hotel} (Pay at Hotel summary flow).
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelCoreResponse {

    private String code;
    private String message;
    private Data data;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private List<Content> content;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        @JsonAlias("public_id")
        private String publicId;
        private String name;
        private Location location;
        @JsonAlias("hotel_chain")
        private HotelChain hotelChain;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        private Country country;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Country {
        private String id;
        private String name;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HotelChain {
        @JsonAlias("accommodation_type")
        private String accommodationType;
    }
}
