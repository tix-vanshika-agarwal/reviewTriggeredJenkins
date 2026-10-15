package com.tiket.pojo.lounge;

public class LoungeResetPasswordApiResponse {
    public String code;
    public String message;
    public String errors;
    public ResetPasswordData data;

    public static class ResetPasswordData {
        public String exists;
        public String expired;
        public String maskedAccount;
        public String nextAvailableRequest;
        public String trxId;
    }
}
