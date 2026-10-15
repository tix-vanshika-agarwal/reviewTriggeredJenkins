package com.tiket.pojo.b2b.corporate;

import java.util.List;

/**
 * Same as {@link BookingDetailResponse}, except for flight multi city orders where
 * {@code $.data[*].orderDetail} is an array (one item per route) instead of an object.
 */
public class BookingDetailMultiCityResponse {
    public static class OrderResponse {
        private boolean success;
        public List<OrderData> data;
        private BookingDetailResponse.Metadata metadata;
    }

    public static class OrderData {
        private String approvalEmail;
        private String approvalNotes;
        private String employeeDivision;
        private String employeeEmail;
        private String employeeName;
        public List<BookingDetailResponse.OrderDetail> orderDetail;
        public long orderID;
        private String orderHash;
        private long referenceOrderID;
        private String referenceTable;
        private String paymentExpiredDate;
        private String paymentStatus;
        public String paymentType;
        private String orderStatus;
        public String productType;
        private String subProductType;
        private String totalPrice;
        private List<BookingDetailResponse.Traveler> travelers;
        private String orderNotes;
        private String purchaseDate;
        private List<Object> refundDetail;
        private List<Object> rescheduleDetail;
        private boolean approvable;
        private BookingDetailResponse.TravelPolicyDetail travelPolicyDetail;
        private BookingDetailResponse.EmployeeGroups employeeGroups;
        private long travelPlanID;
        private String travelPlanName;
        private String InterimID;
    }
}
