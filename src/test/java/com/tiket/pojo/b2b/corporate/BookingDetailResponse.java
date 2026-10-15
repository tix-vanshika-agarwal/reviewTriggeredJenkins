package com.tiket.pojo.b2b.corporate;

import java.util.List;
import java.util.Map;

public class BookingDetailResponse {
    public static class OrderResponse {
        private boolean success;
        public List<OrderData> data;
        private Metadata metadata;
    }

    public static class OrderData {
        private String approvalEmail;
        private String approvalNotes;
        private String employeeDivision;
        private String employeeEmail;
        private String employeeName;
        private OrderDetail orderDetail;
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
        private String InterimID;
    }

    public static class OrderDetail {
        private Long orderDetailId;
        private Long departureOrderDetailId;
        private String departureCity;
        private String departureCityName;
        private String departurePort;
        private String departureTerminal;
        private String departureStationCode;
        private String departureDate;
        private String departureTime;
        private String arrivalCity;
        private String arrivalCityName;
        private String arrivalPort;
        private String arrivalTerminal;
        private String arrivalStationCode;
        private String returnDate;
        private String returnTime;
        private Boolean roundTrip;
        private String orderName;
        private String orderNameDetail;
        private Integer countAdult;
        private Integer countChild;
        private Integer countInfant;
        private Integer countElderly;
        private Integer guest;
        private Integer quantity;
        private Integer duration;
        private Integer adultTotal;
        private Integer childTotal;
        private Integer infantTotal;
        private List<FerryDetail> ferryDetail;
        private List<BusDetail> busDetail;
        private List<TrainDetail> trainDetail;
        private HotelDetail hotelDetail;
    }

    public static class FerryDetail {
        private String status;
        private String trip;
        private String ticketTypeCode;
        private String ticketTypeGroupType;
        private String ferryOperatorDisplayName;
    }

    public static class BusDetail {
        private String orderName;
        private String orderNameDetail;
        private String status;
        private long orderDetailId;
        private String busDepartureCityName;
        private String busDepartureTerminal;
        private String busArrivalCityName;
        private String busArrivalTerminal;
        private String busTrip;
        private String busServiceClass;
        private String busName;
        private int quantity;
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
    }

    public static class HotelDetail {
        private String typeRoom;
        private int roomNumber;
        private String status;
        private String specialRequest;
        private int starRating;
    }

    public static class Traveler {
        private String salutation;
        private String fullName;
        private Vertical vertical;
    }

    public static class Vertical {
        private String busSeat;
    }

    public static class TravelPolicyDetail {
        private long orderID;
        private boolean complyTravelPolicy;
        private Policy policy;
    }

    public static class Policy {
        private long id;
        private long travelPolicyID;
        private String productType;
        private Object config;
    }

    public static class EmployeeGroups {
        private long divisionID;
        private long groupID;
        private String divisionName;
        private String groupName;
    }

    public static class Metadata {
        private int page;
        private int size;
        private int count;
        private int totalCount;
        private Map<String, Object> extInfo;
    }
}