package com.tiket.pojo.flight;

public class FlightCartId {
    public class Data{
        public String cartId;
    }
    
    public class Root{
        public String code;
        public String message;
        public Object errors;
        public Data data;
    }

    public Data data;

    
}
