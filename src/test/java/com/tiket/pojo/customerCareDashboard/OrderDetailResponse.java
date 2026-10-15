package com.tiket.pojo.customerCareDashboard;

import java.util.List;

public class OrderDetailResponse {
    public Data1 data;

    public static class Data1 {
        public SearchOrder searchOrder;
    }

    public static class SearchOrder {
        public String code;
        public Data2 data;
    }

    public static class Data2 {
        public int totalPage;
        public int page;
        public List<SearchData> searchData;
    }

    public static class SearchData {
        public String orderId;
        public String orderDetailId;
        public String orderHash;
    }
}
