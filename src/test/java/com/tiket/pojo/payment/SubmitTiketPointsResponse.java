package com.tiket.pojo.payment;

import java.math.BigDecimal;
import java.util.List;

public class SubmitTiketPointsResponse {
    public String code;
    public String message;
    public Data data;

    public static class Data {
        public SidebarPayment sidebarPayment;
    }

    public static class SidebarPayment {
        public String referenceId;
        public BigDecimal grandTotal;
        public BigDecimal subTotal;
        public List<OrderDetail> orderDetail;
    }

    public static class OrderDetail {
        public String referenceDetailId;
        public String productType;
    }
}
