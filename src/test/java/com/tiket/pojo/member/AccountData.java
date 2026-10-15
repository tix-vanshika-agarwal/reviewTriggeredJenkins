package com.tiket.pojo.member;

import java.util.List;

public class AccountData {
    public String code;
    public String message;
    public String errors;
    public Data data;
    public String serverTime;

    public static class Data {
        public long accountId;
        public String accountLastLogin;
        public String accountPhoneCode;
        public String accountPhoneNumber;
        public String accountUri;
        public String accountUsername;
        public Object autoRegister;
        public String createdTimestamp;
        public int guest;
        public String lang;
        public Object newsLetter;
        public Object tixDeliveryAddress;
        public Object tixDeliveryCity;
        public Object tixDeliveryPostalCode;
        public String updatedTimestamp;
        public int verifiedEmail;
        public int verifiedPhoneNumber;
        public String loginMedia;
        public boolean canChangePassword;
        public boolean hasPin;
        public long tixPoints;
        public List<Object> missionRewards;
        public String rewardsProgress;
        public String completionPercentage;
        public int totalRewards;
        public boolean showRewardCompletionNotification;
        public int profileId;
        public String firstName;
        public String lastName;
        public String accountBirthdate;
        public String accountNationalityCode;
        public String accountGender;
        public String accountSalutationName;
        public String identityNumber;
        public String loyaltyStatus;
        public String loyaltyLevel;
        public int isActive;
        public String currency;
        public long unmUserId;
        public Object facebookRegist;
        public Object googleRegist;
        public Settings settings;
        public boolean isB2bCorp;
    }

    public static class Settings {
        public String THREEDS;
        public String BIOMETRIC;
    }
}
