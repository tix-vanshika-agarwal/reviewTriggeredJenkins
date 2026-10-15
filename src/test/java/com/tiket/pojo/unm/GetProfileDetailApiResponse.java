package com.tiket.pojo.unm;

import java.util.List;

public class GetProfileDetailApiResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data {
        public long userId;
        public String name;
        public String email;
        public String phoneCountryCode;
        public String phoneNationalNumber;
        public String dob;
        public String preferredLanguage;
        public String country;
        public String gender;
        public boolean isEmailVerified;
        public boolean isPhoneVerified;
        public boolean isPasswordExist;
        public List<String> accessCodes;

        public String getFormattedDob() {
            if (dob == null || dob.isEmpty()) return "";
            try {
                java.time.LocalDate date = java.time.LocalDate.parse(dob);
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("d MMMM yyyy");
                return date.format(formatter);
            } catch (Exception e) {
                return dob;
            }
        }
    }
}
