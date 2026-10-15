package com.tiket.pojo.intools;

import java.util.List;

public class SummaryReportList {

    public Data data;

    public class Data {
        public List<VerticalMonthlySummary> vertical_monthly_summary;

        public class VerticalMonthlySummary {
            public String entity;

            public String currency;
            public Long total_vendor;
            public Double total_revenue;
            public Double total_quantity_sold;
            public Double total_margin;
            public Double total_base_price;
            public Double total_tiket_point_value;
            public Double total_gift_voucher;
            public Double total_promo_code;
            public Double total_platform_fee;
            public Double total_direct_cost;
            public Double total_profit;

        }


    }

}