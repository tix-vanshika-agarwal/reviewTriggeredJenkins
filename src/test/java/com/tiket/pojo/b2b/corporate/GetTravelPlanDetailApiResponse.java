package com.tiket.pojo.b2b.corporate;

import java.util.List;

public class GetTravelPlanDetailApiResponse {
    public boolean success;
    public Data data;
    public Object metadata;

    public static class Data {
        public int travelPlanID;
        public String travelPlanNumber;
        public String name;
        public String startDate;
        public String endDate;
        public String status;
        public RequestorDetails requestorDetails;
        public String notes;
        public List<Approver> approvers;
        public Config config;
        public List<TravelPlanPojo.CustomFields> customFields;
    }

    public static class customFields {
        public int id;
        public String fieldType;
        public String fieldName;
        public List<String> value;
        public File file;

        public static class File {
            public String id;
            public String name;
            public String urlPath;
            public int size;
            public String signedURL;
        }
}

    public static class RequestorDetails {
        public String requestedAt;
        public String name;
        public String travelPolicyID;
        public String travelPolicyName;
    }

    public static class Approver {
        public int approverEmployeeID;
        public String lastActionAt;
        public String action;
        public int id;
        public String name;
        public String email;
        public String divisionName;
        public String role;
        public String reference;
        public String groupName;
        public String corporateRoleName;
        public String status;
    }

    public static class Config {
        public List<Hotel> hotels;
        public List<Flight> flights;
        public List<Car> cars;
        public List<Train> trains;
        public List<Whoosh> whooshs;
    }

    public static class Hotel {
        public String publicId;
        public String location;
        public String startDate;
        public String endDate;
        public int nights;
        public String locationType;
        public int orderID;
        public String orderStatus;
        public String orderHash;
        public String grandTotal;
        public String paymentExpiredAt;
        public String paymentStatus;
        public boolean isRefunded;
        public boolean isRefundRequested;
        public int travelPlanDetailID;
        public String hotelName;
        public int rating;
        public String checkInTime;
        public String checkOutTime;
        public int orderDetailID;
    }

    public static class Flight {
        public String departureCode;
        public String departureName;
        public String departureType;
        public String arrivalCode;
        public String arrivalName;
        public String arrivalType;
        public boolean isRoundTrip;
        public String departureDate;
        public String returnDate;
        public int orderID;
        public String orderStatus;
        public String orderHash;
        public String grandTotal;
        public String paymentExpiredAt;
        public String paymentStatus;
        public boolean isRefunded;
        public boolean isRefundRequested;
        public String departureFlightName;
        public String arrivalFlightName;
        public String departureAirlineCode;
        public String arrivalAirlineCode;
        public int travelPlanDetailID;
        public String departureIconURL;
        public String arrivalIconURL;
        public int totalTransit;
        public String departureTime;
        public String arrivalTime;
        public int departureOrderDetailID;
        public int arrivalOrderDetailID;
        public String refundType;
    }

    public static class Car {
        public String pickupDate;
        public String pickupTime;
        public String regionalID;
        public String regionalName;
        public boolean withDriver;
        public int totalDays;
        public int orderID;
        public String orderStatus;
        public String orderHash;
        public String grandTotal;
        public String paymentExpiredAt;
        public String paymentStatus;
        public boolean isRefunded;
        public boolean isRefundRequested;
        public int travelPlanDetailID;
        public String carName;
        public String vendorName;
        public String dropOffDate;
        public String dropOffTime;
        public int orderDetailID;
    }

    public static class Train {
        public String departureCode;
        public String departureName;
        public String departureType;
        public String arrivalCode;
        public String arrivalName;
        public String arrivalType;
        public boolean isRoundTrip;
        public String departureDate;
        public String returnDate;
        public int orderID;
        public String orderStatus;
        public String orderHash;
        public String grandTotal;
        public String paymentExpiredAt;
        public String paymentStatus;
        public boolean isRefunded;
        public boolean isRefundRequested;
        public int travelPlanDetailID;
        public String departureTrainName;
        public String arrivalTrainName;
        public String departureIconURL;
        public String arrivalIconURL;
        public String departureClass;
        public String arrivalClass;
        public String departureSubClass;
        public String arrivalSubClass;
        public int departureOrderDetailID;
        public int arrivalOrderDetailID;
        public String refundType;
    }

    public static class Whoosh {
        public String departureCode;
        public String departureName;
        public String departureType;
        public String arrivalCode;
        public String arrivalName;
        public String arrivalType;
        public boolean isRoundTrip;
        public String departureDate;
        public String returnDate;
        public int orderID;
        public String orderStatus;
        public String orderHash;
        public String grandTotal;
        public String paymentExpiredAt;
        public String paymentStatus;
        public boolean isRefunded;
        public boolean isRefundRequested;
        public int travelPlanDetailID;
        public String departureTrainName;
        public String arrivalTrainName;
        public String departureIconURL;
        public String arrivalIconURL;
        public String departureClass;
        public String arrivalClass;
        public String departureSubClass;
        public String arrivalSubClass;
        public int departureOrderDetailID;
        public int arrivalOrderDetailID;
        public String refundType;
    }
}
