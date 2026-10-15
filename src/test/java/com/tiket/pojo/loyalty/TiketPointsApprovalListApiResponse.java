package com.tiket.pojo.loyalty;

import java.util.List;

public class TiketPointsApprovalListApiResponse {
    public String code;
    public String message;
    public Object errors;
    public List<Data> data;
    public String serverTime;

    public static class Data {
        public Integer id;
        public String type;
        public String status;
        public String referenceId;
        public Long orderId;
        public Long pointId;
        public Integer accountId;
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
