package com.tiket.pojo.accommodation.hotel.supply;

import java.util.List;

/**
 * Response of GET /tix-hotel-supply-dashboard/funnel/rooms.
 * Each mapped room exposes its {@code id} (used as the {@code roomIds} value for unmap) and its
 * current group via {@code masterRoomGroup.groupingKey}.
 */
public class RoomGroupingRoomsResponse {

    public String code;
    public String message;
    public Data data;

    public static class Data {
        public List<Content> content;
        public int number;
        public int size;
        public int totalElements;
        public boolean first;
        public boolean last;
        public int numberOfElements;
        public int totalPages;
        public boolean hasContent;
    }

    public static class Content {
        public String id;
        public String roomId;
        public String name;
        public MasterRoomGroup masterRoomGroup;
    }

    public static class MasterRoomGroup {
        public String id;
        public String groupingKey;
        public String groupingName;
    }
}
