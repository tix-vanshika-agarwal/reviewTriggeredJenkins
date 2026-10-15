package com.tiket.pojo.extranet.login;

import com.google.gson.annotations.SerializedName;

public class ExtranetAdminLoginMfaGenerateResponse {
    @SerializedName("code")
    public String code;
    
    @SerializedName("message")
    public String message;
    
    @SerializedName("errors")
    public Object errors;
    
    @SerializedName("data")
    public Data data;
    
    @SerializedName("serverTime")
    public String serverTime;
    
    public static class Data {
        @SerializedName("otp")
        public Otp otp;
        
        @SerializedName("mfa")
        public Mfa mfa;
    }
    
    public static class Otp {
        @SerializedName("trxId")
        public String trxId;
        
        @SerializedName("expired")
        public String expired;
        
        @SerializedName("nextAvailableRequest")
        public String nextAvailableRequest;
        
        @SerializedName("exist")
        public Object exist;
        
        @SerializedName("maskedAccount")
        public String maskedAccount;
        
        @SerializedName("waitResend")
        public Integer waitResend;
        
        @SerializedName("waitMaxResend")
        public Integer waitMaxResend;
        
        @SerializedName("maxAttempts")
        public Object maxAttempts;
        
        @SerializedName("maxAttemptLabel")
        public String maxAttemptLabel;
    }
    
    public static class Mfa {
        @SerializedName("method")
        public String method;
        
        @SerializedName("methodOptions")
        public String[] methodOptions;
    }
    
    /**
     * Checks if the OTP generation was successful
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return "SUCCESS".equals(code);
    }
}

