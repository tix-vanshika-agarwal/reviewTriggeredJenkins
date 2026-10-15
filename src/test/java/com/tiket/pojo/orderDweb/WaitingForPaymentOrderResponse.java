package com.tiket.pojo.orderDweb;

import java.util.List;

public class WaitingForPaymentOrderResponse {
    public Data data;

    public static class Data {
        public String title;
        public List<OrderItem> list;
    }

    public static class OrderItem {
        public String header;
        public Content content;
        public OrderData orderData;
    }

    public static class Content {
        public List<Product> products;
        public String totalPayment;
        public int timeleftSeconds;
    }

    public static class Product {
        public String iconUrl;
        public String description;
        public String orderType;
    }

    public static class OrderData {
        public String orderId;
        public String orderHash;
        public String orderDetailId;
    }
}
