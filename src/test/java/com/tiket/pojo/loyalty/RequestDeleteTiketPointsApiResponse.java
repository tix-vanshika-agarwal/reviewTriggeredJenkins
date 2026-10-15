package com.tiket.pojo.loyalty;

public class RequestDeleteTiketPointsApiResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Data {
        public Long id;
        public String type;
        public String status;
        public String referenceId;
        public Long orderId;
        public Long pointId;
        public Long accountId;
        public String username;
        public String partnerName;
        public String processType;
        public String expiredAt;
        public Integer amount;
        public String notes;
        public String requestedBy;
        public String processedBy;
        public String createdAt;
        public String updatedAt;
        public String activeAt;
    }
}
