package com.tiket.pojo.ttd;

import java.math.BigInteger;
import java.util.ArrayList;

public class TTDGetPartners {
    public String code;
    public String message;
    public Object errors;
    public ArrayList<Datum> data;
    public BigInteger serverTime;

    public static class Datum{
        public String partnerId;
        public String partnerName;
    }
}
