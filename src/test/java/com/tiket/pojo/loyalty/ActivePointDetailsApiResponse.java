package com.tiket.pojo.loyalty;

import java.util.List;

public class ActivePointDetailsApiResponse {
    public String code;
    public String message;
    public Object errors;
    public List<PointDetail> data;
    public String serverTime;

    public static class PointDetail {
        public long pointId;
        public String detailId;
        public String username;
        public String processType;
        public String status;
        public long pointAmount;
        public long pointLeft;
        public boolean deleteAction;
    }
}
