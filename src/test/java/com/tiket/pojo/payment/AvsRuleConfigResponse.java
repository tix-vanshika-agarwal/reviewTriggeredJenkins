package com.tiket.pojo.payment;

import java.util.List;

public class AvsRuleConfigResponse {
    public AvsCheck avsCheck;
    public boolean avsCheckEnable;
    public List<String> bins;
    public List<String> cardSchemes;
    public List<String> cardTypes;
    public String createdBy;
    public long createdDate;
    public List<String> currency;
    public String id;
    public int isDeleted;
    public List<String> issuerCountries;
    public String ruleName;
    public String storeId;
    public String updatedBy;
    public long updatedDate;
    public int version;

    public static class AvsCheck {
        public String type;
        public List<String> checkFieldType;
    }
}
