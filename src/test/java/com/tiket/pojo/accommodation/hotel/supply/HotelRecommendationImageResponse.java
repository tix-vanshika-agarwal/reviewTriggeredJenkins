package com.tiket.pojo.accommodation.hotel.supply;

import com.google.gson.JsonElement;

import java.util.List;

/** Response of GET /tix-hotel-core/hotel/{hotelId}/recommendation-image. */
public class HotelRecommendationImageResponse {

    public String code;
    public String message;
    public Data data;

    public static class Data {
        public List<Image> imageIncludeList;
        public List<Image> imageExcludeList;
    }

    public static class Image {
        /** The {@code image_raw} document id. */
        public String id;
        public String url;
        public JsonElement overallScore;
        /** {@code null} (or JSON null) while the image is still "Not scored yet". */
        public JsonElement quality;
    }
}
