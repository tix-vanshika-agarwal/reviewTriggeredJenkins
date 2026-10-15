package com.tiket.pojo.accommodation.hotel.supply;

import com.google.gson.JsonElement;
import java.util.List;

/** Response of GET /tix-hotel-core/room-grouping/get-all-by-hotel-id. */
public class AllRoomGroupingsByHotelIdResponse {

    public String code;
    public String message;
    public String errors;
    public Data data;
    public Long serverTime;

    public static class Data {
        public CetusUserAction cetusUserAction;
        public List<RoomGrouping> roomGroupings;
    }

    public static class CetusUserAction {
        public Long displaySnackbarUntil;
        public Integer estimatedFinishDuration;
        public Long estimationEndDate;
        public Boolean allowSendToCetus;
        public Boolean isDataValid;
    }

    public static class RoomGrouping {
        public String id;
        public String hotelId;
        public String groupingKey;
        public LocalisedName groupingName;
        public JsonElement customGroupingName; // BE returns either a plain string or a localised object {en,id}
        public String tag;
        public Long updatedDate;
        public String lastUpdatedUser;
        public boolean verified;
        public int totalRoom;
        public int totalRoomVerified;
        public int totalRoomUnverified;
    }

    public static class LocalisedName {
        public String en;
        public String id;
    }
}
