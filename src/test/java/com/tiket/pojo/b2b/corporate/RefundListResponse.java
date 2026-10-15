package com.tiket.pojo.b2b.corporate;

import java.util.List;

public class RefundListResponse {
    public boolean success;
    public List<Data> data;
    public Object metadata;

    public static class Data {
        public String id;
        public Orders orders;
    }

    public static class Orders {
        public String orderID;
        public OrderDetail orderDetail;
    }

    public static class OrderDetail {
        public String orderDetailId;
    }
}
