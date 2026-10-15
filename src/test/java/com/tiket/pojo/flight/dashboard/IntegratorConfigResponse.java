package com.tiket.pojo.flight.dashboard;

import java.util.List;

public class IntegratorConfigResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data {
        public List<Content> content;
    }

    public static class Content {
        public String id;
        public String action;
        public String distributionType;
        public String supplierCode;
        public String url;
    }
}
