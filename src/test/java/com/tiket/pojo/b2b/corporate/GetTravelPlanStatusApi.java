package com.tiket.pojo.b2b.corporate;

import java.util.List;

public class GetTravelPlanStatusApi {

    private boolean success;
    private List<Data> data;
    private Metadata metadata;

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    // Inner class for Data
    public static class Data {
        private String approvalEmail;
        private String approvalNotes;
        private String employeeDivision;
        private String employeeEmail;
        private String employeeName;
        private OrderDetail orderDetail;
        private long orderID;
        private String orderHash;
        private long referenceOrderID;
        private String referenceTable;
        private String paymentExpiredDate;
        private String paymentStatus;
        private String paymentType;
        private String orderStatus;
        private String productType;
        private String subProductType;
        private String totalPrice;
        private List<Traveler> travelers;
        private String orderNotes;
        private String purchaseDate;
        private List<Object> refundDetail;
        private List<Object> rescheduleDetail;
        private boolean approvable;
        private TravelPolicyDetail travelPolicyDetail;
        private EmployeeGroups employeeGroups;
        private long travelPlanID;
        private String travelPlanName;
        private String interimID;

        // Getter for orderID
        public long getOrderID() {
            return orderID;
        }

        // Getter for travelPlanName
        public String getTravelPlanName() {
            return travelPlanName;
        }

        // Getters for other fields...
        public String getApprovalEmail() {
            return approvalEmail;
        }

        public String getApprovalNotes() {
            return approvalNotes;
        }

        // Add other getters for fields as needed...
    }

    // Other nested classes...
    public static class OrderDetail {
        private long departureOrderDetailId;
        private long returnOrderDetailId;
        private String departureCity;
        private String departureStationCode;
        private String arrivalCity;
        private String arrivalStationCode;
        private String departureDate;
        private String departureTime;
        private String returnDate;
        private String returnTime;
        private int adultTotal;
        private int childTotal;
        private int infantTotal;
        private boolean roundTrip;
        private List<TrainDetail> trainDetail;

        // Getters and setters for all fields...
    }

    public static class TrainDetail {
        private String orderName;
        private String departureStation;
        private String departureDate;
        private String arrivalDate;
        private String arrivalStation;
        private String trainName;
        private String trainNumber;
        private String trainClass;
        private String trainSubClass;
        private String trainIcon;

        // Getters and setters for all fields...
    }

    public static class Traveler {
        private String salutation;
        private String fullName;
        private Vertical vertical;

        // Getters and setters for all fields...
    }

    public static class Vertical {
        private String busSeat;

        // Getters and setters for busSeat...
    }

    public static class TravelPolicyDetail {
        private long orderID;
        private boolean complyTravelPolicy;
        private Policy policy;

        // Getters and setters for all fields...
    }

    public static class Policy {
        private long id;
        private long travelPolicyID;
        private String productType;
        private Config config;

        // Getters and setters for all fields...
    }

    public static class Config {
        private boolean enableBooking;

        // Getters and setters for enableBooking...
    }

    public static class EmployeeGroups {
        private long divisionID;
        private long groupID;
        private String divisionName;
        private String groupName;

        // Getters and setters for all fields...
    }

    public static class Metadata {
        private int page;
        private int size;
        private int count;
        private int totalCount;
        private Object extInfo;

        // Getters and setters for all fields...
    }
}
