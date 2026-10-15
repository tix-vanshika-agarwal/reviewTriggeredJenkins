package com.tiket.pojo.ttd;

public class KlookVoucher {

    public String event_id;
    public long event_time;
    public String event_type;
    public Meta meta;

    public static class Meta {
        public String agent_id;
        public String agent_order_id;
        public String order_id;
        public String voucher_url;
        public String status;
        public int redemption_type;
        public int file_type;
    }
}