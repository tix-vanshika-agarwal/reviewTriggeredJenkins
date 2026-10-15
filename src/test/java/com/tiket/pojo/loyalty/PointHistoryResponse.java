package com.tiket.pojo.loyalty;

import java.util.List;

/**
 * Response of the loyalty core point-history endpoint
 * {@code GET /tix-loyalty-core/admin-cs/ulp/point-history}.
 *
 * <p>Each {@link Content} row represents a single point movement (earned, redeemed, expired, …).
 * A mission reward that was successfully claimed surfaces here as a positive {@code point} entry
 * with {@code pointActivityType == "Earned"} / {@code type == "REWARD"}.
 */
public class PointHistoryResponse {
    public String code;
    public String message;
    public String errors;
    public Data data;
    public String serverTime;

    public static class Data {
        public List<Content> content;
        public int totalElements;
        public int totalPages;
    }

    public static class Content {
        public int point;
        public int statusPoint;
        public String expiredAt;
        public String activeAt;
        public String type;
        public String orderId;
        public String orderDate;
        public String notes;
        public String orderName;
        public String orderType;
        public Object additionalInfo;
        public String pointStatus;
        public String iconUrl;
        public String pointActivityType;
    }
}
