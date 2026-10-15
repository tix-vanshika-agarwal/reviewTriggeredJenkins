package com.tiket.pojo.auth;


public class ProfileDetailResponse {
    public String code;
    public String message;
    public String errors;
    public ProfileDetailData data;
    public String serverTime;

    public static class ProfileDetailData {
        public long accountId;
        public long profileId;
        public String firstName;
        public String lastName;
        public String salutation;
        public String dateOfBirth;
        public String phoneNumber;
        public String phoneCode;
    }
}
