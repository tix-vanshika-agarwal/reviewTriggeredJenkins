package com.tiket.pojo.extranet.login;

import com.google.gson.annotations.SerializedName;

public class ExtranetAdminLoginResponse {
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
        
        // MFA fields (when MFA is required)
        @SerializedName("mfaTrxId")
        public String mfaTrxId;
        
        @SerializedName("methodOptions")
        public String[] methodOptions;
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
        return "SUCCESS".equals(code);
    }
}

