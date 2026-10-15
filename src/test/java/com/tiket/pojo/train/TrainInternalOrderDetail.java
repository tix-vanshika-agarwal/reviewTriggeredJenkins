package com.tiket.pojo.train;

import java.math.BigDecimal;

public class TrainInternalOrderDetail {
    public String code;
    public Data data;

    public class Data{
        public String orderId;
        public String orderHash; 
        public String orderStatus;
        public BigDecimal price;
    }
}
