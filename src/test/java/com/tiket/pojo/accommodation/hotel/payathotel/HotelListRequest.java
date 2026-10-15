package com.tiket.pojo.accommodation.hotel.payathotel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Price engine {@code POST /price/v2/hotel-list} body (Pay at Hotel summary flow).
 * Matches {@code JsonInclude.NON_NULL}: do not set {@code rateKeyMapping} when unused.
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HotelListRequest {

    private Map<String, HotelIdEntry> hotelIds;
    private String startDate;
    private boolean usePriceDrop;
    private int numberOfNights;
    private int numberOfRooms;
    private int numberOfAdults;
    private int packageRate;
    private List<Integer> childrenAge;
    private String localTimezone;
    private boolean showAlternate;
    private boolean useSmartMapping;
    private RateKeyMapping rateKeyMapping;
    private boolean searchOnly;
    private boolean excludeAltProduct;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class HotelIdEntry {
        private List<VendorData> vendorDataV2;
        private HotelDetail hotelDetail;
        private Map<String, List<String>> allowToSearch;
    }

    @Getter
    @Setter
    public static class VendorData {
        private String vendor;
        private List<String> externalIds;
        private Object activeRoomDetails;
    }

    @Getter
    @Setter
    public static class HotelDetail {
        private String countryId;
        private String regionId;
        private String cityId;
        private String areaId;
        private String hotelTimeZone;
        private int starRating;
    }

    @Getter
    @Setter
    public static class RateKeyMapping {
        private String rateKey;
        private String promotionTypes;
        private String ratePlanTypes;
    }
}
