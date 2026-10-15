package com.tiket.pojo.member;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SmartProfileResponse {
    public String code;
    public ArrayList<SmartProfileResponse.Data> data;
    public BigDecimal serverTime;
    public String message;
    public String errors;

    public static class Data{
        public int id;
        public int accountId;
        public boolean isPrimary;
        public String accountSalutationName;
        public String accountFirstName;
        public String accountLastName;
        public String accountGender;
        public String accountPhone;
        public String accountPhoneCode;
        public String accountBirthdate;
        public String accountNationalityCode;
        public String accountEmail;
        public String emergencyContactName;
        public String emergencyContactNumber;
        public String emergencyCountryPhoneCode;
        public String identityType;
        public String identityNumber;
        public String identityFullname;
        public String passportNumber;
        public String passportExpiryDate;
        public String passportIssuedDate;
        public String passportIssuingCountry;
        public String createdTimestamp;
        /**
         * Bound as a {@code String} because the profile service returns the age either as a bare
         * number or as a formatted label; Gson reads both into a string, but only one into a number.
         */
        // Age bracket returned as a label, e.g. "Adult".
        public String ages;
        public boolean isNew;
        public String accountSalutationCode;
    }
}

