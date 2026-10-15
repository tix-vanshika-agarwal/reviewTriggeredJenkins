package com.tiket.pojo.auth;

import com.google.gson.annotations.SerializedName;

public class AdminLoginMfaApplyResponse {
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
        @SerializedName("accessToken")
        public String accessToken;
        
        @SerializedName("refreshToken")
        public String refreshToken;
        
        @SerializedName("accountId")
        public Long accountId;
        
        @SerializedName("email")
        public String email;
        
        @SerializedName("aat")
        public String aat;
        
        @SerializedName("art")
        public String art;
    }
}

