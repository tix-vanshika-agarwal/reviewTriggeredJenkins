package com.tiket.pojo.auth;

import com.google.gson.annotations.SerializedName;

public class AdminLoginMfaGenerateResponse {
    @SerializedName("status")
    public Status status;
    
    @SerializedName("data")
    public Data data;
    
    public static class Status {
        @SerializedName("code")
        public String code;
        
        @SerializedName("message")
        public String message;
    }
    
    public static class Data {
        @SerializedName("otpTrxId")
        public String otpTrxId;
        
        @SerializedName("message")
        public String message;
    }
}

