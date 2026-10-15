package com.tiket.pojo.auth;

import java.math.BigInteger;
import java.util.ArrayList;

public class MfaMethodsResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public BigInteger serverTime;

    public static class Data{
        public String otpRecipientId;
        public ArrayList<Method> methods;
        public String token;
    }

    public static class Method{
        public String type;
        public String name;
        public boolean isPrimary;
        public boolean otpRequired;
        public String recipient;
        public ArrayList<String> subMethods;
    }
}
