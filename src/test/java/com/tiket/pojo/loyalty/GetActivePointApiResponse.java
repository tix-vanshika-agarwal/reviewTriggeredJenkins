package com.tiket.pojo.loyalty;

public class GetActivePointApiResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Data {
        public int id;
        public String username;
        public int activePoint;
        public int cumulativePoint;
        public int productUnlocked;
        public String levelName;
        public String levelDisplayName;
        public String levelUpdatedAt;
        public String levelExpiredAt;
        public Object productHistory;
        public int totalPointExpireSoon;
        public String pointExpireSoonAt;
        public String urlInfoPage;
        public String firstName;
        public String lastName;
        public String salutation;
        public String email;
        public String phoneNumber;
        public boolean emailVerified;
        public boolean phoneVerified;
        public String address;
        public String levelIcon;
    }
}
