package com.tiket.pojo.flight;

import java.util.ArrayList;
import java.util.Date;

public class FlightBookingCodeDetail {
    public Data data;
    public Content content;
    public class Content{
        public String id;
        public String orderId;
        public String orderDetailId;
        public String bookingCode;
        public String supplierCode;
        public Object airlineBookingCodes;
        public String departureDate;
        public Date bookingExpired;
        public Date orderTime;
        public String status;
        public String distributionType;
        public String flightSelect;
        public boolean allowSetBroken;
        public long bookingExpiredDate;
        public long orderTimeDate;
        public Notes notes;
        public String storeId;
        public boolean isAutomateReschedule;
    }
    
    public class Data{
        public ArrayList<Content> content;
        public Pageable pageable;
        public int totalElements;
        public int totalPages;
        public boolean last;
        public boolean first;
        public Sort sort;
        public int numberOfElements;
        public int size;
        public int number;
        public boolean empty;
    }
    
    public class Notes{
        public String storeId;
        public String ticketNumberIncompleteReason;
    }
    
    public class Pageable{
        public Sort sort;
        public int pageSize;
        public int pageNumber;
        public int offset;
        public boolean paged;
        public boolean unpaged;
    }
    
    public class Root{
        public String code;
        public String message;
        public Object errors;
        public Data data;
        public long serverTime;
    }
    
    public class Sort{
        public boolean sorted;
        public boolean unsorted;
        public boolean empty;
    }
    
    
}
