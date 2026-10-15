package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.ferry.AddToCartFerryApi;
import com.tiket.api.ferry.SearchConfigFerryApi;
import com.tiket.api.ferry.SearchScheduleFerryApi;
import com.tiket.api.ferry.SubmitBookingFerryApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.ferry.AddToCartFerryResponse;
import com.tiket.pojo.ferry.FerrySearchConfigResponse;
import com.tiket.pojo.ferry.SearchScheduleFerryResponse;
import com.tiket.pojo.ferry.SubmitBookingFerryResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

public class FerryApi <T extends WebDriver> extends BasePage<T> {
    // Thread-safe cache for storing available ferry dates
    private static final ConcurrentHashMap<String, String> ferryDateCache = new ConcurrentHashMap<>();
    
    public FerryApi(Session<T> session) { super(session); }

    @SneakyThrows
    public int createWaitingPaymentOrderFerry(
            String username,
            String password,
            String origin,
            String destination,
            String departureDate,
            String departureTime,
            String vehicleType,
            int adult,
            int child,
            int infant,
            int elderly) {
        try {
            Unm<T> unm = new Unm<>(session);
            LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
            String accessToken = authData.accessToken;

            // search for ferry
            SearchScheduleFerryApi.Input searchQuery = new SearchScheduleFerryApi.Input(accessToken, origin, destination,
                    departureDate, departureTime, vehicleType, adult, child, infant, elderly);
            SearchScheduleFerryResponse searchResponse = searchFerry(searchQuery);
            SearchScheduleFerryResponse.Schedule selectedSchedule = this.filterSchedule(searchResponse.data);
            BigDecimal grandTotal = selectedSchedule.price.finalPrice;
            logger.log("grand total without service fee: " + grandTotal);
            String scheduleId = selectedSchedule.id;
            // add to cart ferry
            AddToCartFerryApi.Input cartInput = new AddToCartFerryApi.Input(accessToken, scheduleId, adult, child, infant,
                    elderly);
            AddToCartFerryResponse addToCartResponse = addToCart(cartInput);
            Thread.sleep(2000);
            String cartId = addToCartResponse.data.cartId;
            String secretId = addToCartResponse.data.secret;
            boolean licensePlate = addToCartResponse.data.schedules.get(0).vehicleType.isLicensePlateRequired;
            // submit booking
            SubmitBookingFerryApi.Input submitBookingInput = new SubmitBookingFerryApi.Input(authData.accountUsername, accessToken, licensePlate, cartId, secretId, adult, child, infant);
            SubmitBookingFerryResponse responseSubmitBooking = submitBooking(submitBookingInput);
            int orderId = Integer.parseInt(responseSubmitBooking.data.order.orderId);

            if (responseSubmitBooking.code.equals("SUCCESS"))
                return orderId;
            else {
                Assertion.skip("Ferry Booking via API failed. Skipping the subsequent steps.");
            }
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Ferry Booking via API failed. Skipping the subsequent steps.");
        }
        return 0;
    }

    private SearchScheduleFerryResponse searchFerry(SearchScheduleFerryApi.Input input) {
        SearchScheduleFerryApi searchScheduleFerryApi = new SearchScheduleFerryApi(input);
        ApiResponse searchScheduleFerryResponse = searchScheduleFerryApi.hitApi(apiTools);
        logger.log("DEBUG raw search result : " + searchScheduleFerryResponse.getBody());
        Gson gson = new Gson();
        return gson.fromJson(searchScheduleFerryResponse.getBody(),
                SearchScheduleFerryResponse.class);
    }

    private SearchScheduleFerryResponse.Schedule filterSchedule(SearchScheduleFerryResponse.Data searchResultData) {
        return searchResultData.schedules.stream().filter(data -> data.availableQuota > 0).findFirst().orElseThrow();
    }

    private AddToCartFerryResponse addToCart(AddToCartFerryApi.Input input)
            throws InterruptedException, ExecutionException {
        AddToCartFerryApi addToCartFerryApi = new AddToCartFerryApi(input);
        ApiResponse addToCartFerryResponse = addToCartFerryApi.hitApi(apiTools);
        Gson gson = new Gson();
        AddToCartFerryResponse responsePojo = gson.fromJson(addToCartFerryResponse.getBody(),
                AddToCartFerryResponse.class);
        return responsePojo;
    }

    private SubmitBookingFerryResponse submitBooking(SubmitBookingFerryApi.Input bookingData)
            throws InterruptedException, ExecutionException {
        SubmitBookingFerryApi submitBookingFerryApi = new SubmitBookingFerryApi(bookingData);
        ApiResponse submitBookingFerryApiResponse = submitBookingFerryApi.hitApi(apiTools);
        logger.log("debug : " + submitBookingFerryApiResponse.getBody());
        Gson gson = new Gson();
        return gson.fromJson(submitBookingFerryApiResponse.getBody(),
                SubmitBookingFerryResponse.class);
    }

    private String generateCacheKey(String origin, String destination, String vehicleType) {
        return String.format("%s|%s|%s", origin, destination, vehicleType);
    }

    /**
     * Converts date from yyyy-MM-dd format to d MMMM yyyy format
     * @param dateInYyyyMmDd Date in yyyy-MM-dd format (e.g., "2025-11-30")
     * @return Date in d MMMM yyyy format (e.g., "30 November 2025")
     */
    private String convertToDisplayFormat(String dateInYyyyMmDd) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        LocalDate date = LocalDate.parse(dateInYyyyMmDd, inputFormatter);
        return date.format(outputFormatter);
    }

    private List<String> generateEndOfMonthDates() {
        // Get the last day of the current month using CoreFunctions
        String lastDayOfMonthStr = CoreFunctions.getEndOfCurrentMonth("yyyy-MM-dd");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate lastDayOfMonth = LocalDate.parse(lastDayOfMonthStr, dateFormatter);
        
        // Generate 4 dates: last day, last day - 1, last day - 2, last day - 3
        List<String> datesToCheck = new ArrayList<>();
        datesToCheck.add(lastDayOfMonth.format(dateFormatter));
        datesToCheck.add(lastDayOfMonth.minusDays(1).format(dateFormatter));
        datesToCheck.add(lastDayOfMonth.minusDays(2).format(dateFormatter));
        datesToCheck.add(lastDayOfMonth.minusDays(3).format(dateFormatter));
        
        return datesToCheck;
    }

    private FerrySearchConfigResponse getFerrySearchConfig(String accessToken) {
        SearchConfigFerryApi.Input input = new SearchConfigFerryApi.Input(accessToken);
        SearchConfigFerryApi searchConfigFerryApi = new SearchConfigFerryApi(input);
        ApiResponse searchConfigResponse = searchConfigFerryApi.hitApi(apiTools);
        logger.log("DEBUG ferry search config: " + searchConfigResponse.getBody());
        Gson gson = new Gson();
        return gson.fromJson(searchConfigResponse.getBody(), FerrySearchConfigResponse.class);
    }

    private String getPortIdByName(FerrySearchConfigResponse configResponse, String portName) {
        return configResponse.data.ports.stream()
                .filter(port -> port.name.equalsIgnoreCase(portName))
                .map(port -> port.portId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Port not found: " + portName));
    }

    private String getVehicleTypeIdByName(FerrySearchConfigResponse configResponse, String vehicleTypeName) {
        return configResponse.data.vehicleTypes.stream()
                .filter(vt -> vt.name.equalsIgnoreCase(vehicleTypeName))
                .map(vt -> vt.id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Vehicle type not found: " + vehicleTypeName));
    }

    /**
     * Retrieves the available ferry date for the specified route and vehicle type.
     * <p>
     * This method performs the following logic:
     * <ul>
     *   <li>If the environment is <b>PROD</b>, returns the last date of the current month without making any API calls.</li>
     *   <li>Otherwise, attempts to retrieve the available date from a thread-safe cache based on origin, destination, and vehicle type.</li>
     *   <li>If not cached, performs an API search to determine the available date and caches the result for future calls.</li>
     * </ul>
     * </p>
     *
     * @param origin       The name of the origin port.
     * @param destination  The name of the destination port.
     * @param vehicleType  The type of vehicle for the ferry booking.
     * @param adult        Number of adult passengers.
     * @param child        Number of child passengers.
     * @param infant       Number of infant passengers.
     * @param elderly      Number of elderly passengers.
     * @return The available ferry date as a formatted string (e.g., "d MMMM yyyy").
     *         In PROD environment, this is always the last date of the current month.
     *         Otherwise, it is the earliest available date found via API or cache.
     * @throws Exception if an error occurs during API search or data processing.
     */
    @SneakyThrows
    public String getFerryAvailableDate(String origin, String destination, String vehicleType, int adult, int child, int infant, int elderly) {
        // Check environment - if PROD, return last date of month without API call
        if (Configs.ENVIRONMENT == Environment.PROD) {
            String lastDayOfMonth = CoreFunctions.getEndOfCurrentMonth("d MMMM yyyy");
            logger.log("Environment is PROD. Returning last date of month without API call: " + lastDayOfMonth);
            return lastDayOfMonth;
        }
        
        logger.log("Environment is " + Configs.ENVIRONMENT + ". Proceeding with API search.");
        
        // Generate cache key (only based on route and vehicle type, not passenger counts)
        String cacheKey = generateCacheKey(origin, destination, vehicleType);
        
        // Check if date is already cached
        if (ferryDateCache.containsKey(cacheKey)) {
            String cachedDate = ferryDateCache.get(cacheKey);
            logger.log("Returning cached ferry date: " + cachedDate + " for key: " + cacheKey);
            return cachedDate;
        }
        
        logger.log("Cache miss. Fetching available ferry date for key: " + cacheKey);
        
        LoginApi<?> loginApi = new LoginApi<>(session);
        String accessToken = loginApi.getAllToken().data.accessToken;

        // Get ferry search configuration
        FerrySearchConfigResponse configResponse = getFerrySearchConfig(accessToken);

        accessToken = "Bearer " + accessToken;
        
        // Get port IDs and vehicle type ID by name
        String originPortId = getPortIdByName(configResponse, origin);
        String destinationPortId = getPortIdByName(configResponse, destination);
        String vehicleTypeId = getVehicleTypeIdByName(configResponse, vehicleType);
        
        logger.log("Origin Port: " + origin + " -> ID: " + originPortId);
        logger.log("Destination Port: " + destination + " -> ID: " + destinationPortId);
        logger.log("Vehicle Type: " + vehicleType + " -> ID: " + vehicleTypeId);

        // Generate dates to check
        List<String> datesToCheck = generateEndOfMonthDates();
        
        // Default search parameters
        String departureTime = "10:00";
        
        // Calculate total passengers
        int totalPassengers = adult + child + infant + elderly;
        logger.log("Total passengers: " + totalPassengers);
        
        // Loop through each date to find availability
        for (String formattedDate : datesToCheck) {
            logger.log("Checking ferry availability for date: " + formattedDate);
            
            try {
                // Search for ferry schedules
                SearchScheduleFerryApi.Input searchQuery = new SearchScheduleFerryApi.Input(
                        accessToken, originPortId, destinationPortId, formattedDate, departureTime, 
                        vehicleTypeId, adult, child, infant, elderly);
                SearchScheduleFerryResponse searchResponse = searchFerry(searchQuery);

                // Check if there are any schedules with sufficient quota for all passengers
                if (searchResponse.data != null && searchResponse.data.schedules != null && !searchResponse.data.schedules.isEmpty()) {
                    boolean hasAvailability = searchResponse.data.schedules.stream()
                            .anyMatch(schedule -> schedule.availableQuota >= totalPassengers);
                    
                    if (hasAvailability) {
                        logger.log("Found available ferry schedule for date: " + formattedDate + " with sufficient quota for " + totalPassengers + " passengers");
                        // Convert to display format before caching and returning
                        String displayDate = convertToDisplayFormat(formattedDate);
                        ferryDateCache.put(cacheKey, displayDate);
                        logger.log("Cached ferry date: " + displayDate + " for key: " + cacheKey);
                        return displayDate;
                    } else {
                        logger.log("No sufficient quota for " + totalPassengers + " passengers on date: " + formattedDate);
                    }
                } else {
                    logger.log("No schedules found for date: " + formattedDate);
                }
            } catch (Exception e) {
                logger.log("Error checking date " + formattedDate + ": " + e.getMessage());
            }
        }
        
        // If no available date found, return last date of month as fallback
        String fallbackDate = CoreFunctions.getEndOfCurrentMonth("d MMMM yyyy");
        logger.log("No available ferry dates found. Returning fallback date: " + fallbackDate);
        return fallbackDate;
    }
}
