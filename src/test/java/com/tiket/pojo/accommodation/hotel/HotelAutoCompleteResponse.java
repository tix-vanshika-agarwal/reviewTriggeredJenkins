package com.tiket.pojo.accommodation.hotel;

import java.util.List;

public class HotelAutoCompleteResponse {

    public String code;
    public String message;
    public Data data;

    public static class Data {
        public List<Content> contents;

        public static class Content {
            public Boolean popular;
            public String type;
            public String label;
            public String publicId;
            public String name;
            public String location;
            public Country country;
            public String latitude;
            public String longitude;
        }
    }

    public static class Country {
        public String id;
        public String name;
    }
}
