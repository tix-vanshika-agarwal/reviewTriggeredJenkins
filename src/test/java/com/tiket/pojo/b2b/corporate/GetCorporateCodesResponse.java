package com.tiket.pojo.b2b.corporate;

import java.util.List;
import java.util.Map;

public class GetCorporateCodesResponse {
    public boolean success;
    public List<Data> data;
    public Object metadata;

    public static class Data {
        public int corporateID;
        public Map<String, CodeData> corporateCodes;
        public String productType;
        public String airlineName;
        public String airlineCode;
        public boolean isCHAEnabled;
    }

    public static class CodeData {
        public int id;
        public String code;
        public boolean isActive;
    }
}
