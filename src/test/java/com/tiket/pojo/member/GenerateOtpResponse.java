package com.tiket.pojo.member;

public class GenerateOtpResponse {
    public String code;
    public String message;
    public Data data;

    public static class Data{
        public String trxId;
        public String nextAvailableRequest;
        public String waitResend;
    }
}
