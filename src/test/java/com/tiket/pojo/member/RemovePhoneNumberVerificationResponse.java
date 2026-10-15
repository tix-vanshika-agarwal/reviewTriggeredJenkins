package com.tiket.pojo.member;

import java.util.ArrayList;

public class RemovePhoneNumberVerificationResponse {
    public String code;
    public String message;
    public Object errors;
    public ArrayList<Datum> data;
    public long serverTime;

    public static class Datum{
        public String event;
        public String dateTime;
        public String note;
        public String emailRequester;
    }
}
