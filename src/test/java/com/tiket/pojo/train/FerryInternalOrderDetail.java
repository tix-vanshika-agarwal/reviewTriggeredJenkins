package com.tiket.pojo.train;

import java.math.BigDecimal;

public class FerryInternalOrderDetail {
      public String code;
    public Data data;

    public class Data{
        public String orderId;
        public boolean orderHash; 
        public String orderStatus;
        public BigDecimal price;
        public BigDecimal totalAmount;
    }
}
