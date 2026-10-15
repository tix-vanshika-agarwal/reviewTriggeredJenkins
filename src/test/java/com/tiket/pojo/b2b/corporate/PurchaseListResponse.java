package com.tiket.pojo.b2b.corporate;

import java.util.ArrayList;
import java.util.Date;

public class PurchaseListResponse {

    public static class TravelerName {
        public String title;
        public String fullName;

    }

    public static class Data {
        public String typeRoom;
        public int roomNumber;
        public ArrayList<TravelerName> travelerNames;
        public Object specialRequests;
        public String status;
        public String orderNotes;
        public Date purchaseDate;

    }

    public Data data;
    public TravelerName travelerName;
}
