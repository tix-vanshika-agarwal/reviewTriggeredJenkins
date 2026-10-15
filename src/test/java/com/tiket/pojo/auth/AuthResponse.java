package com.tiket.pojo.auth;

public class AuthResponse {
    public String code;
    public Data data;
    public String serverTime;
    public String message;
    public String errors;

    public static class Data{
        public String accessToken;
        public String accountFirstName;
        public long accountId;
        public String accountLastName;
        public String verificationToken;
        public String verificationStatus;
        public boolean isNeed2FA;
        public String accountPhoneCode;
        public String accountPhoneNumber;
        public String accountUsername;
        public boolean isSessionCreated;
        public String refreshToken;
        public String twoFactorOTPRecipient;
        public boolean verifiedEmail;
        public boolean verifiedPhoneNumber;
        public boolean isOfferBiometric;
        public String lang;
        public String currency;
    }
}
