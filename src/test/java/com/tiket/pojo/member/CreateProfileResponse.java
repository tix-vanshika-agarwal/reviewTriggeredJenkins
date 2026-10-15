package com.tiket.pojo.member;

import java.math.BigDecimal;

public class CreateProfileResponse {
    public String code;
    public BigDecimal serverTime;
    public String message;
    public String errors;
    public Data data;

    public static class Data {
        public int id;
        public int accountId;
        public boolean isPrimary;
        public String accountSalutationName;
        public String accountFirstName;
        public String accountLastName;
        public String ages;
    }
}
