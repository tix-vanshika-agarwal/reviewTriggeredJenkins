package com.tiket.pojo.unm;

public class LoginDataUnmResponse {
    public String code;
    public Data data;
    public String serverTime;
    public String message;
    public Object errors;

    public static class Data {
        public String accountFirstName;
        public String accountLastName;
        public long unmUserId;
        public String accountPhoneCode;
        public String accessToken;
        public String accountPhoneNumber;
        public long accountId;
        public String accessRoleName;
        public String accountUsername;
        public boolean verifiedPhoneNumber;
        public String currency;
        public boolean isB2BCorp;
        public String lang;
        public String uniqueId;
        public String refreshToken;
        public boolean verifiedEmail;
    }
}
