package com.tiket.pojo.loyalty;

public class OtpGenerationResponse {

    public String code;
    public String message;
    public Object errors;
    public String serverTime;
    public Data data;

    public static class Data {
        public String trxId;
        public String nextAvailableRequest;
        public int waitResend;
        public int waitMaxResend;
        public Object maxAttempts;
        public String maxAttemptLabel;
        public String maskedAccount;
    }
}
