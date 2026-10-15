package com.tiket.pojo.accommodation.master_tag;

public class AccommodationMasterTagResponse {

    public String code;
    public String message;
    public Data[] data;

    public static class Data {
        public String keyword;
        public String alias;
        public String name;
        public String iconUrl;
        public String subtitle;
        public String url;
        public Boolean isPreferredPartner;
    }

    public String toString() {
        String res = "[";

        for (Data datum: data) {
            res += datum.name + ", ";
        }

        return res + "]";
    }
}
