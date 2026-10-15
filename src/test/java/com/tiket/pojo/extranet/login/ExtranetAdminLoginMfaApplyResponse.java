package com.tiket.pojo.extranet.login;

import com.google.gson.annotations.SerializedName;

public class ExtranetAdminLoginMfaApplyResponse {
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
        
        @SerializedName("accountId")
        public Long accountId;
        
        @SerializedName("accountUsername")
        public String accountUsername;
        
        @SerializedName("accountFirstName")
        public String accountFirstName;
        
        @SerializedName("accountLastName")
        public String accountLastName;
        
        @SerializedName("accountPhoneCode")
        public String accountPhoneCode;
        
        @SerializedName("accountPhoneNumber")
        public String accountPhoneNumber;
        
        @SerializedName("currency")
        public String currency;
        
        @SerializedName("lang")
        public String lang;
        
        @SerializedName("uniqueId")
        public String uniqueId;
        
        @SerializedName("verifiedEmail")
        public Boolean verifiedEmail;
        
        @SerializedName("verifiedPhoneNumber")
        public Boolean verifiedPhoneNumber;
        
        @SerializedName("passwordInfo")
        public Object passwordInfo;
        
        @SerializedName("sessionExpirationCode")
        public Object sessionExpirationCode;
    }
    
    /**
     * Checks if the MFA apply was successful
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return "SUCCESS".equals(code);
    }
}

