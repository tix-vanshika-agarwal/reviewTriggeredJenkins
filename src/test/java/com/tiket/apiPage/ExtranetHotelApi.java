package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.extranet.booking.GETHotelBookingDetailApi;
import com.tiket.api.extranet.booking.HotelBookingsSearchApi;
import com.tiket.api.accommodation.hotel.HotelLoyaltyApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.accommodation.hotel.HotelBookingsSearchResponse;
import com.tiket.pojo.accommodation.hotel.HotelLoyaltyResponse;
import com.tiket.pojo.extranet.booking.HotelBookingDetailResponse;
import com.tiket.pojo.extranet.login.ExtranetAdminLoginMfaApplyResponse;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExtranetHotelApi <T extends WebDriver> extends BasePage<T> {
    public ExtranetHotelApi(Session<T> session) {
        super(session);
    }

    public record DashboardBookingDetails(
            long itineraryId, int orderId, String status, String checkinDate, String checkoutDate,
            int duration, int adult, int totalRoom, List<String> guests, String promotion, HotelBookingsSearchResponse.AddOnDetails[] addOnDetails, String ratePlanName, boolean found) {
        public static DashboardBookingDetails notFound() {
            return new DashboardBookingDetails(0, 0, null, null, null, 0, 0, 0, List.of(), null, null, null, false);
        }
        public static List<DashboardBookingDetails> notFoundMulti() {
            List<DashboardBookingDetails> list = new ArrayList<>();
            list.add(new DashboardBookingDetails(0, 0, null, null, null, 0, 0, 0, List.of(), null, null, null,false));
            return list;
        }
    }


    public record RoomBookingEntry(
            long itineraryId,          // The unique ID for this room booking (id field)
            int orderId,               // The parent order ID
            String status,
            String checkinDate,
            String checkoutDate,
            int duration,
            int adult,
            int totalRoom,
            String roomName,
            String ratePlanName,
            double netPrice,
            String currency,
            List<String> guests
    ) {}


    public record MultiRoomBookingDetails(
            int orderId,
            String status,
            String checkinDate,
            String checkoutDate,
            int duration,
            int totalAdults,           // Sum of adults across all entries
            int totalRooms,            // Sum of totalRoom across all entries
            int totalBookingEntries,   // Number of entries in content[]
            List<String> allGuests,    // All guests from all entries
            List<String> roomNames,    // All room names booked
            List<Long> itineraryIds,   // All itinerary IDs
            List<RoomBookingEntry> roomEntries,  // Individual room entries for detailed validation
            boolean found
    ) {
        public static MultiRoomBookingDetails notFound() {
            return new MultiRoomBookingDetails(0, null, null, null, 0, 0, 0, 0, List.of(), List.of(), List.of(), List.of(), false);
        }


        public boolean allEntriesHaveSameOrderId(int expectedOrderId) {
            if (roomEntries == null || roomEntries.isEmpty()) {
                return false;
            }
            for (RoomBookingEntry entry : roomEntries) {
                if (entry.orderId() != expectedOrderId) {
                    return false;
                }
            }
            return true;
        }


        public RoomBookingEntry getRoomEntry(int index) {
            if (roomEntries == null || index < 0 || index >= roomEntries.size()) {
                return null;
            }
            return roomEntries.get(index);
        }
    }

    /**
     * Fetches dashboard booking details using username and password for authentication
     *
     * @param hotelId      The unique identifier of the hotel
     * @param orderId      The order ID to search for
     * @param startDate    The start date for the booking search
     * @param endDate      The end date for the booking search
     * @param username     The username for authentication
     * @param password     The password for authentication
     * @return DashboardBookingDetails containing the booking information (includes both orderId and itineraryId)
     */
    public DashboardBookingDetails getDashboardBookingDetails(int hotelId, String orderId, String startDate, String endDate,
                                                              String username, String password) {
        try {
            ExtranetHotelAuthApi<?> authApi = new ExtranetHotelAuthApi<>(session);
            ExtranetAdminLoginMfaApplyResponse.Data authData = authApi.getAccessToken(username, password);
            String accessToken = authData.accessToken;
            Long dashboardAccountId = authData.accountId;

            return getDashboardBookingDetails(hotelId, orderId, startDate, endDate, username, accessToken, dashboardAccountId);
        } catch (Exception e) {
            logger.log("Error during authentication for booking details: " + e.getMessage());
            return DashboardBookingDetails.notFound();
        }
    }

    /**
     * Fetches dashboard booking details using username and password for authentication
     *
     * @param hotelId      The unique identifier of the hotel
     * @param orderId      The order ID to search for
     * @param startDate    The start date for the booking search
     * @param endDate      The end date for the booking search
     * @param username     The username for authentication
     * @param password     The password for authentication
     * @return DashboardBookingDetails containing the booking information (includes both orderId and itineraryId)
     */
    public List<DashboardBookingDetails> getDashboardMultiBookingDetails(int hotelId, String orderId, String startDate, String endDate,
                                                              String username, String password) {
        try {
            ExtranetHotelAuthApi<?> authApi = new ExtranetHotelAuthApi<>(session);
            ExtranetAdminLoginMfaApplyResponse.Data authData = authApi.getAccessToken(username, password);
            String accessToken = authData.accessToken;
            Long dashboardAccountId = authData.accountId;

            return getDashboardMultiBookingDetails(hotelId, orderId, startDate, endDate, username, accessToken, dashboardAccountId);
        } catch (Exception e) {
            logger.log("Error during authentication for booking details: " + e.getMessage());
            return DashboardBookingDetails.notFoundMulti();
        }
    }

    /**
     * Fetches dashboard booking details using pre-authenticated access token
     *
     * @param hotelId             The unique identifier of the hotel
     * @param orderId             The order ID to search for
     * @param startDate           The start date for the booking search
     * @param endDate             The end date for the booking search
     * @param username            The username associated with the access token
     * @param accessToken         The pre-authenticated access token
     * @param dashboardAccountId  The account ID associated with the access token
     * @return DashboardBookingDetails containing the booking information (includes both orderId and itineraryId)
     */
    public DashboardBookingDetails getDashboardBookingDetails(int hotelId, String orderId, String startDate, String endDate,
                                                              String username, String accessToken, long dashboardAccountId) {
        return fetchDashboardBookingDetails(hotelId, orderId, startDate, endDate, username, accessToken, dashboardAccountId);
    }

    /**
     * Fetches multiple dashboard booking details using a pre-authenticated access token
     *
     * @param hotelId             The unique identifier of the hotel
     * @param orderId             The order ID to search for
     * @param startDate           The start date for the booking search
     * @param endDate             The end date for the booking search
     * @param username            The username associated with the access token
     * @param accessToken         The pre-authenticated access token
     * @param dashboardAccountId  The account ID associated with the access token
     * @return a list of {@link DashboardBookingDetails} containing booking information for all matched results
     */
    public List<DashboardBookingDetails> getDashboardMultiBookingDetails(int hotelId, String orderId, String startDate, String endDate,
                                                              String username, String accessToken, long dashboardAccountId) {
        return fetchDashboardMultiBookingDetails(hotelId, orderId, startDate, endDate, username, accessToken, dashboardAccountId);
    }

    /**
     * Private helper method that contains the common logic for fetching dashboard booking details
     * Note: Searches by orderId but returns both orderId and itineraryId in the response
     */
    private DashboardBookingDetails fetchDashboardBookingDetails(int hotelId, String orderId, String startDate, String endDate,
                                                                 String username, String accessToken, long dashboardAccountId) {
        try {
            logger.log("Fetching booking details from dashboard for orderId: " + orderId + ", hotelId: " + hotelId);

            HotelBookingsSearchApi.Inputs inputs = new HotelBookingsSearchApi.Inputs(
                    accessToken, hotelId, orderId, startDate, endDate, username, dashboardAccountId);

            HotelBookingsSearchApi api = new HotelBookingsSearchApi(inputs);
            ApiResponse response = api.hitApi(apiTools);

            int statusCode = response.getStatusCode();
            logger.log("Hotel Bookings Search API Response Status: " + statusCode);
            logger.log("Response body: " + response.getBody());

            if (statusCode != 200) {
                logger.log("Failed to fetch booking - Status code: " + statusCode);
                return DashboardBookingDetails.notFound();
            }

            Gson gson = new Gson();
            HotelBookingsSearchResponse responseBody = gson.fromJson(response.getBody(), HotelBookingsSearchResponse.class);

            if (responseBody.data == null || responseBody.data.content == null || responseBody.data.content.isEmpty()) {
                logger.log("No booking found in response");
                return DashboardBookingDetails.notFound();
            }

            HotelBookingsSearchResponse.BookingContent booking = responseBody.data.content.get(0);

            List<String> guestsList = new ArrayList<>();
            if (booking.guests != null) {
                for (String guest : booking.guests) {
                    guestsList.add(guest.trim());
                }
            }
            // booking.id is the itineraryId (e.g., 1866409), booking.orderId is the orderId (e.g., 1900101319)
            return new DashboardBookingDetails(booking.id, booking.orderId, booking.status, booking.checkinDate, 
                    booking.checkoutDate, booking.duration, booking.adult, booking.totalRoom, guestsList, booking.promotion, booking.addOnDetails, booking.ratePlanName, true);
        } catch (Exception e) {
            logger.log("Error fetching booking details: " + e.getMessage());
            return DashboardBookingDetails.notFound();
        }
    }

    private List<DashboardBookingDetails> fetchDashboardMultiBookingDetails(int hotelId, String orderId, String startDate, String endDate,
                                                                 String username, String accessToken, long dashboardAccountId) {
        try {
            logger.log("Fetching booking details from dashboard for orderId: " + orderId + ", hotelId: " + hotelId);

            HotelBookingsSearchApi.Inputs inputs = new HotelBookingsSearchApi.Inputs(
                    accessToken, hotelId, orderId, startDate, endDate, username, dashboardAccountId);

            HotelBookingsSearchApi api = new HotelBookingsSearchApi(inputs);
            ApiResponse response = api.hitApi(apiTools);

            int statusCode = response.getStatusCode();
            logger.log("Hotel Bookings Search API Response Status: " + statusCode);
            logger.log("Response body: " + response.getBody());

            if (statusCode != 200) {
                logger.log("Failed to fetch booking - Status code: " + statusCode);
                return DashboardBookingDetails.notFoundMulti();
            }

            Gson gson = new Gson();
            HotelBookingsSearchResponse responseBody = gson.fromJson(response.getBody(), HotelBookingsSearchResponse.class);

            if (responseBody.data == null || responseBody.data.content == null || responseBody.data.content.isEmpty()) {
                logger.log("No booking found in response");
                return DashboardBookingDetails.notFoundMulti();
            }

            List<HotelBookingsSearchResponse.BookingContent> bookings = responseBody.data.content;
            List<DashboardBookingDetails> list = new ArrayList<>();

            for (HotelBookingsSearchResponse.BookingContent booking : bookings) {
                List<String> guestsList = new ArrayList<>();
                if (booking.guests != null) {
                    for (String guest : booking.guests) {
                        guestsList.add(guest.trim());
                    }
                }
                // booking.id is the itineraryId (e.g., 1866409), booking.orderId is the orderId (e.g., 1900101319)
                list.add(new DashboardBookingDetails(booking.id, booking.orderId, booking.status, booking.checkinDate,
                        booking.checkoutDate, booking.duration, booking.adult, booking.totalRoom, guestsList, booking.promotion, booking.addOnDetails, booking.ratePlanName, true));
            }

            return list;
        } catch (Exception e) {
            logger.log("Error fetching booking details: " + e.getMessage());
            return DashboardBookingDetails.notFoundMulti();
        }
    }


    public MultiRoomBookingDetails getMultiRoomBookingDetails(int hotelId, String orderId, String startDate, String endDate,
                                                               String username, String accessToken, long dashboardAccountId) {
        return fetchMultiRoomBookingDetails(hotelId, orderId, startDate, endDate, username, accessToken, dashboardAccountId);
    }


    private MultiRoomBookingDetails fetchMultiRoomBookingDetails(int hotelId, String orderId, String startDate, String endDate,
                                                                  String username, String accessToken, long dashboardAccountId) {
        try {
            logger.log("Fetching multi-room booking details from dashboard for orderId: " + orderId + ", hotelId: " + hotelId);

            HotelBookingsSearchApi.Inputs inputs = new HotelBookingsSearchApi.Inputs(
                    accessToken, hotelId, orderId, startDate, endDate, username, dashboardAccountId);

            HotelBookingsSearchApi api = new HotelBookingsSearchApi(inputs);
            ApiResponse response = api.hitApi(apiTools);

            int statusCode = response.getStatusCode();
            logger.log("Hotel Bookings Search API Response Status: " + statusCode);
            logger.log("Response body: " + response.getBody());

            if (statusCode != 200) {
                logger.log("Failed to fetch multi-room booking - Status code: " + statusCode);
                return MultiRoomBookingDetails.notFound();
            }

            Gson gson = new Gson();
            HotelBookingsSearchResponse responseBody = gson.fromJson(response.getBody(), HotelBookingsSearchResponse.class);

            if (responseBody.data == null || responseBody.data.content == null || responseBody.data.content.isEmpty()) {
                logger.log("No booking found in response");
                return MultiRoomBookingDetails.notFound();
            }

            // Aggregate data from all booking entries and build individual room entries
            int totalRoomCount = 0;
            int totalAdultCount = 0;
            List<String> allGuests = new ArrayList<>();
            List<String> allRoomNames = new ArrayList<>();
            List<Long> allItineraryIds = new ArrayList<>();
            List<RoomBookingEntry> roomEntries = new ArrayList<>();

            for (HotelBookingsSearchResponse.BookingContent entry : responseBody.data.content) {
                totalRoomCount += entry.totalRoom;
                totalAdultCount += entry.adult;
                allItineraryIds.add(entry.id);

                // Collect guests for this entry
                List<String> entryGuests = new ArrayList<>();
                if (entry.guests != null) {
                    for (String guest : entry.guests) {
                        String trimmedGuest = guest.trim();
                        allGuests.add(trimmedGuest);
                        entryGuests.add(trimmedGuest);
                    }
                }

                if (entry.roomName != null) {
                    allRoomNames.add(entry.roomName);
                }

                // Create individual room entry for detailed validation
                RoomBookingEntry roomEntry = new RoomBookingEntry(
                        entry.id,
                        entry.orderId,
                        entry.status,
                        entry.checkinDate,
                        entry.checkoutDate,
                        entry.duration,
                        entry.adult,
                        entry.totalRoom,
                        entry.roomName,
                        entry.ratePlanName,
                        entry.netPrice,
                        entry.currency,
                        entryGuests
                );
                roomEntries.add(roomEntry);

                logger.log("Room entry " + roomEntries.size() + ": itineraryId=" + entry.id +
                           ", orderId=" + entry.orderId + ", roomName=" + entry.roomName +
                           ", status=" + entry.status);
            }

            // Use first entry for common fields (orderId, dates, status, etc.)
            HotelBookingsSearchResponse.BookingContent firstBooking = responseBody.data.content.get(0);
            int totalBookingEntries = responseBody.data.content.size();

            logger.log("Multi-room booking aggregated: " + totalBookingEntries + " entries, " +
                       totalRoomCount + " total rooms, " + totalAdultCount + " total adults");

            return new MultiRoomBookingDetails(
                    firstBooking.orderId,
                    firstBooking.status,
                    firstBooking.checkinDate,
                    firstBooking.checkoutDate,
                    firstBooking.duration,
                    totalAdultCount,
                    totalRoomCount,
                    totalBookingEntries,
                    allGuests,
                    allRoomNames,
                    allItineraryIds,
                    roomEntries,
                    true
            );
        } catch (Exception e) {
            logger.log("Error fetching multi-room booking details: " + e.getMessage());
            return MultiRoomBookingDetails.notFound();
        }
    }

    /**
     * Fetches detailed hotel booking information for a specific hotel and itinerary
     *
     * @param hotelId      The unique identifier of the hotel
     * @param itineraryId  The itinerary ID (booking ID) as a string
     * @param username     The username for authentication
     * @param accountId    The account ID for the user
     * @param currency     The currency code (e.g., "IDR", "USD")
     * @return HotelBookingDetailResponse containing the booking details, or null if the API call fails
     */
    public HotelBookingDetailResponse getHotelBookingDetail(int hotelId, long itineraryId, String username,
                                                            long accountId, String currency) {
        try {
            logger.log("Fetching hotel booking detail for hotelId: " + hotelId + ", itineraryId: " + itineraryId);

            GETHotelBookingDetailApi.Inputs inputs = new GETHotelBookingDetailApi.Inputs(
                    hotelId, itineraryId, username, accountId, currency);

            GETHotelBookingDetailApi api = new GETHotelBookingDetailApi(inputs);
            ApiResponse response = api.hitApi(apiTools);

            int statusCode = response.getStatusCode();
            logger.log("Hotel Booking Detail API Response Status: " + statusCode);
            logger.log("Response body: " + response.getBody());

            if (statusCode != 200) {
                logger.fail("Failed to fetch hotel booking detail - Status code: " + statusCode);
                return null;
            }

            Gson gson = new Gson();
            HotelBookingDetailResponse responseBody = gson.fromJson(response.getBody(), HotelBookingDetailResponse.class);

            if (responseBody.isSuccess()) {
                logger.log("Successfully fetched booking detail for itineraryId: " + responseBody.getItineraryId());
            } else {
                logger.log("API returned non-success response: " + responseBody.message);
            }

            return responseBody;

        } catch (Exception e) {
            logger.fail("Error fetching hotel booking detail: " + e.getMessage());
            return null;
        }
    }

    /**
     * Fetches loyalty blackout dates for the given hotel, filtered by {@code blackoutType}.
     * Uses the hotel-deals backend directly with the MM dashboard internal hotel id.
     *
     * @param blackoutType {@code "multiple"} for date ranges (e.g. {@code 2026-07-07 - 2026-07-08}),
     *                     {@code "single"} for single-day entries (e.g. {@code 2026-07-22 - 2026-07-22})
     */
    public List<String> getHotelLoyaltyBlackoutDates(MmDashboardApi.DashboardMemberSession session, int hotelId, String blackoutType) {
        HotelLoyaltyApi.Inputs inputs = new HotelLoyaltyApi.Inputs(String.valueOf(hotelId));
        HotelLoyaltyApi api = new HotelLoyaltyApi(inputs);
        ApiResponse apiResponse = api.hitApi(apiTools);
        logger.log("Hotel Loyalty API response: " + apiResponse.getBody());

        Gson gson = new Gson();
        HotelLoyaltyResponse response = gson.fromJson(apiResponse.getBody(), HotelLoyaltyResponse.class);
        if (response == null || !"SUCCESS".equalsIgnoreCase(response.code)) {
            throw new RuntimeException("Failed to fetch hotel loyalty blackout dates: " + apiResponse.getBody());
        }
        if (response.data == null || response.data.blackoutDates == null) {
            logger.log("No blackout dates found for hotelId: " + hotelId);
            return new ArrayList<>();
        }
        List<String> filteredBlackoutDates = filterBlackoutDatesByType(response.data.blackoutDates, blackoutType);
        logger.log("Blackout dates for hotelId " + hotelId + " (type=" + blackoutType + "): " + filteredBlackoutDates);
        return filteredBlackoutDates;
    }

    private List<String> filterBlackoutDatesByType(List<String> blackoutDates, String blackoutType) {
        boolean singleType = "single".equalsIgnoreCase(blackoutType);
        List<String> filtered = new ArrayList<>();
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        for (String dates : blackoutDates) {
            String[] dateList = dates.split(" - ");
            if (dateList.length < 2) {
                continue;
            }
            if (dateList[0].trim().equalsIgnoreCase(currentDate)) {
                continue;
            }
            boolean isSingleDay = Objects.equals(dateList[0].trim(), dateList[1].trim());
            if (singleType && isSingleDay) {
                filtered.add(dates);
            } else if (!singleType && !isSingleDay) {
                filtered.add(dates);
            }
        }
        return filtered;
    }


}
