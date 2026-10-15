package com.tiket.pojo.auth;

import com.google.gson.annotations.SerializedName;

public class AdminLoginResponse {
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
        @SerializedName("accessToken")
        public String accessToken;

        @SerializedName("refreshToken")
        public String refreshToken;

        @SerializedName("accountUsername")
        public String accountUsername;

        @SerializedName("accountFirstName")
        public String accountFirstName;

        @SerializedName("accountLastName")
        public String accountLastName;

        @SerializedName("mfaTrxId")
        public String mfaTrxId;
        
        @SerializedName("methodOptions")
        public String[] methodOptions;
        
        @SerializedName("adminType")
        public String adminType;
        
        @SerializedName("accountId")
        public Long accountId;
        
        @SerializedName("email")
        public String email;
    }
    
    /**
     * Checks if MFA is required based on the response code
     * @return true if MFA is required, false otherwise
     */
    public boolean isMfaRequired() {
        return "LOGIN_NEED_MFA".equals(code);
    }
    
    /**
     * Checks if login was successful without MFA
     * @return true if login succeeded, false otherwise
     */
    public boolean isSuccess() {
        return "SUCCESS".equals(code) || "OK".equals(code);
    }
}

