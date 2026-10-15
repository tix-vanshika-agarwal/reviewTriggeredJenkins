package com.tiket.pojo.accommodation.hotel.supply;

import java.util.List;

/** Response of GET /tix-hotel-core/hotel. */
public class HotelByVendorResponse {

    public String code;
    public String message;
    public String errors;
    public Data data;
    public Long serverTime;

    public static class Data {
        public List<Content> content;
        public int totalPages;
        public int totalElements;
        public int size;
        public int number;
        public int numberOfElements;
        public boolean first;
        public boolean last;
        public boolean empty;
    }

    public static class Content {
        public String id;
        public String hotelId;
        public String publicId;
        public String name;
        public Integer isActive;
    }
}
