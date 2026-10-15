package com.tiket.pojo.cartransfer;

import java.math.BigDecimal;

public class ChooseAutoCompleteResponseAT {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Data{
        public String placeId;
        public String name;
        public BigDecimal latitude;
        public BigDecimal longitude;
    }
}
