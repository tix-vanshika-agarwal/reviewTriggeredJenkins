package com.tiket.pojo.payment;

import java.util.List;

public class AvsRuleConfigListResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data {
        public List<AvsRuleConfigResponse> content;
        public boolean empty;
        public int totalElements;
    }
}
