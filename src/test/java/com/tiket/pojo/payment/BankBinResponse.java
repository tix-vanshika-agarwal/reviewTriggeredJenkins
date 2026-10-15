package com.tiket.pojo.payment;

import java.util.List;

public class BankBinResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data {
        public String id;
        public String bin;
        public String issuer;
        public String issuerAlias;
        public String country;
        public String cardType;
        public Boolean whiteLabel;
        public Boolean eligibleToUse;
        public String eligibleMessage;
        public String cardIcon;
        public String cardScheme;
        public Boolean disableSaveCard;
        public boolean disablePromo;
        public String cardName;
        public AvsCheck avsCheck;
        public Long binMin;
        public Long binMax;
        public String binType;
    }

    public static class AvsCheck {
        public String type;
        public List<String> checkFieldType;
    }
}