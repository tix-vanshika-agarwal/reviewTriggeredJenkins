package com.tiket.apiPage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tiket.api.accommodation.AccommodationLocationDetailApi;
import com.tiket.api.accommodation.AccommodationMasterTagApi;
import com.tiket.api.accommodation.hotel.ChangeHotelSessionApi;
import com.tiket.api.accommodation.hotel.HotelAllotmentsApi;
import com.tiket.api.accommodation.hotel.HotelAutocompleteApi;
import com.tiket.api.accommodation.hotel.HotelLoyaltyApi;
import com.tiket.api.accommodation.hotel.HotelPendingRetrySummaryNotifier;
import com.tiket.api.accommodation.hotel.HotelSearchApi;
import com.tiket.api.accommodation.hotel.HotelSearchApiByName;
import com.tiket.api.accommodation.hotel.getCityNameApi;
import com.tiket.api.accommodation.hotel.landingpage.HotelLandingPageApi;
import com.tiket.api.accommodation.hotel.landingpage.HotelMasterContentApi;
import com.tiket.api.accommodation.hotel.landingpage.LandingPageModuleApi;
import com.tiket.api.accommodation.hotel.supply.UnmappedHotelsSearchApi;
import com.tiket.api.accommodation.hotel.booking.*;
import com.tiket.api.accommodation.hotel.prebook.PreBookGeneratingCartApi;
import com.tiket.api.accommodation.hotel.prebook.PreBookMultiRoomApi;
import com.tiket.api.accommodation.hotel.room.SelectRoomApi;
import com.tiket.api.auth.PayExpressApiWithInputs;
import com.tiket.api.b2b.PostInterimDetailsApi;
import com.tiket.api.b2b.PostInterimTransactionsApi;
import com.tiket.api.discount.ApplyDiscountApi;
import com.tiket.api.loyalty.ActiveTiketPointsApi;
import com.tiket.api.loyalty.ApplyTiketPointsApi;
import com.tiket.api.loyalty.OtpGenerationApi;
import com.tiket.api.order.OrderListApi;
import com.tiket.api.payment.PaidCheckerApi;
import com.tiket.api.payment.SubmitCreditCardApi;
import com.tiket.api.payment.SubmitPaymentApi;
import com.tiket.api.payment.SubmitPaymentApiB2B;
import com.tiket.api.payment.SubmitPaymentPutApiB2B;
import com.tiket.api.payment.SubmitSakukuApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.HotelPriceKind;
import com.tiket.model.HotelPriceScope;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.accommodation.hotel.reviewpage.ReviewPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.corporate.login.CorporateLoginPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.pojo.accommodation.hotel.*;
import com.tiket.pojo.accommodation.hotel.payathotel.HotelCoreResponse;
import com.tiket.pojo.accommodation.hotel.payathotel.HotelListRequest;
import com.tiket.pojo.accommodation.hotel.payathotel.HotelListResponse;
import com.tiket.pojo.accommodation.hotel.payathotel.PayAtHotelRoom;
import com.tiket.pojo.b2b.corporate.EmployeeData;
import com.tiket.pojo.b2b.corporate.PostInterimDetailsApiHotel;
import com.tiket.pojo.b2b.corporate.PostInterimDetailsApiResponse;
import com.tiket.pojo.b2b.corporate.PostInterimTransactionsApiBodyHotel;
import com.tiket.pojo.accommodation.master_tag.AccommodationMasterTagResponse;
import com.tiket.pojo.discount.DiscountResponse;
import com.tiket.pojo.loyalty.ActiveTiketPointsResponse;
import com.tiket.pojo.loyalty.ApplyTiketPointsResponse;
import com.tiket.pojo.loyalty.OtpGenerationResponse;
import com.tiket.pojo.member.AccountData;
import com.tiket.pojo.order.HotelInternalOrderDetail;
import com.tiket.pojo.order.OrderListResponse;
import com.tiket.pojo.payment.PaidCheckerResponse;
import com.tiket.pojo.payment.SubmitCreditCardResponse;
import com.tiket.pojo.payment.SubmitSakukuResponse;
import com.tiket.pojo.room.SelectRoomResponse;
import com.tiket.pojo.room.SelectRoomResponseV2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.tiket.core.CoreFunctions.*;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public class HotelApi<T extends WebDriver> extends BasePage<T> {

    private static final DateTimeFormatter PAH_DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final ObjectMapper PAH_OBJECT_MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    /**
     * PAH report tables: route output through {@code logger} only.
     * {@code Log4JLogger} handles console output; {@code ExtentLogger} writes to the HTML report.
     */
    private void pahReportLine(String line) {
        logger.log(line);
    }

    private void pahReportPrintf(String format, Object... args) {
        logger.log(String.format(Locale.US, format, args));
    }

    public HotelApi(Session<T> session) {
        super(session);
    }

    public String hotelRoomName;
    int orderId;
    String orderHash;
    BigInteger grandTotal;
    Double totalDiscount;
    Double grandTotalInDouble;
    String totalTiketPoints;
    @Setter
    String nativeHotelColor = "#0064D2";
    String hotelName;
    public List<String> guestNames;
    public String addonsName;
    String orderName;
    @Getter
    /**
     * The list of room names for the current hotel order.
     * <p>
     * This list is populated after calling {@link #createOrderMultiRoomApi} and contains the names of each room included in the multi-room order.
     * Callers can use this list to verify or display the names of the rooms booked.
     */
    public List<String> roomNames = new ArrayList<>();
    /**
     * The list of total prices for each room in the current hotel order.
     * <p>
     * This list is populated after calling {@link #createOrderMultiRoomApi} and contains the price summary totals for each room in the multi-room order.
     * Callers can use this list to verify the pricing details of each room booked.
     */
    public List<Double> roomPriceSummaryTotals = new ArrayList<>();
    public List<String> cardIdArray = new ArrayList<>();
    @Getter
    public String orderSpecialRequest;
    /**
     * Map of room names to their corresponding guest names.
     * <p>
     * This map is populated after calling multi-room booking methods and contains
     * the mapping of each room name to the list of guests staying in that room.
     */
    public Map<String, List<String>> roomNameToGuestNamesMap = new LinkedHashMap<>();

    public Map<String,List<String>> getRoomNameToGuestNamesMap() {
        logger.log("========== Room Name to Guest Names Mapping ==========");
        if (roomNameToGuestNamesMap.isEmpty()) {
            logger.log("Map is empty - no rooms have been booked yet");
        } else {
            logger.log("Total rooms booked: " + roomNameToGuestNamesMap.size());
            int roomNumber = 1;
            for (Map.Entry<String, List<String>> entry : roomNameToGuestNamesMap.entrySet()) {
                logger.log("Room " + roomNumber + ": " + entry.getKey());
                logger.log("  Guests (" + entry.getValue().size() + "): " + entry.getValue());
                roomNumber++;
            }
        }
        logger.log("====================================================");
        return roomNameToGuestNamesMap;
    }

    private SubmitBookingResponse submitBooking(SubmitBookingApiV2.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitBookingResponse response = submitBookingWithoutValidation(inputs);

        // Same fail-fast contract as submitBookingV2B2B - reject responses where
        //   1. code != SUCCESS         (e.g. THIRD_PARTY_ERROR)
        //   2. code == SUCCESS but data.orderId == 0 (the slot was lost)
        // so callers don't silently propagate orderId=0 downstream. The raw body is already
        // in the log line emitted by submitBookingWithoutValidation.
        if (response == null
                || response.data == null
                || !"SUCCESS".equalsIgnoreCase(response.code)
                || response.data.orderId == 0) {
            String code = response != null ? response.code : "UNKNOWN";
            String message = response != null ? response.message : "no response body";
            throw new RuntimeException(
                    "submitBooking failed [code=" + code + ", message=" + message + "]");
        }
        return response;
    }

    /**
     * Parses the SubmitBooking response without asserting on {@code code} / {@code data.orderId}.
     * <p>
     * Used by the booking flows that do their own failure handling - they either wrap the whole
     * flow in a {@code try/catch} that skips the test, or assert on the returned order data at
     * the call site - so a tolerated non-{@code SUCCESS} response keeps behaving as it did
     * before these flows were consolidated here. New flows should call {@link #submitBooking}.
     */
    private SubmitBookingResponse submitBookingWithoutValidation(SubmitBookingApiV2.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitBookingApiV2 submitBookingApi = new SubmitBookingApiV2(inputs);
        ApiResponse submitBookingApiResponse = submitBookingApi.hitApi(apiTools);
        String body = submitBookingApiResponse.getBody().toString();
        logger.log("Hotel response : " + body);
        Gson gson = new Gson();
        return gson.fromJson(body, SubmitBookingResponse.class);
    }

    private HotelAutoCompleteResponse hotelAutoComplete(HotelAutocompleteApi.Inputs inputs) throws ExecutionException, InterruptedException {
        HotelAutocompleteApi hotelAutocompleteApi = new HotelAutocompleteApi(inputs);
        ApiResponse apiResponse = hotelAutocompleteApi.hitApi(apiTools);
        JSONObject jsonObject = new JSONObject(apiResponse.getBody());
        logger.log("hotelAutoCompleteResponse-> " + jsonObject);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), HotelAutoCompleteResponse.class);
    }

    public SelectRoomResponse.RatePlan selectRoom(SelectRoomApi.Inputs inputs) throws InterruptedException {
        return selectRoom(inputs, false);
    }

    public SelectRoomResponse.RatePlan selectRoom(SelectRoomApi.Inputs inputs, boolean selectCheapest) throws InterruptedException {
        for(int attempt = 0; attempt<=3; attempt++){
            SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
            ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
            logger.log("Select Room Response : "+selectRoomApiResponse.getBody());
            Gson gson = new Gson();
            SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
            if(!selectRoomResponse.data.rooms.isEmpty()){
                logger.log("Room item color" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).itemColor);
                logger.log("Available rooms" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).availableRoom);
                int totalRoomList = selectRoomResponse.data.rooms.get(0).ratePlans.size();
                int index = -1;
                for(int i = 0; i < totalRoomList; i++){
                    logger.log("Room "+i+" Cancellation policies not null? " + (selectRoomResponse.data.rooms.get(0).ratePlans.get(i).cancellationPolicies.policies != null));
                    if(selectRoomResponse.data.rooms.get(0).ratePlans.get(i).cancellationPolicies.policies != null){
                        index = i;
                        break;
                    }
                }
                if(index == -1){
                    logger.log("No room with a cancellation policy found; falling back to the first room (index 0)");
                    index = 0;
                }
                int finalIndex = index;
                logger.log("Cancellation policies not null?" + (selectRoomResponse.data.rooms.get(0).ratePlans.get(index).cancellationPolicies.policies != null));

                var filteredRooms = selectRoomResponse.data.rooms.stream()
                        .filter(roomList -> roomList.ratePlans.get(finalIndex).availableRoom >= inputs.room())
                        .filter(roomList -> roomList.ratePlans.get(finalIndex).cancellationPolicies.policies != null)
                        .filter(roomList -> Objects.equals(roomList.ratePlans.get(finalIndex).itemColor, nativeHotelColor));

                SelectRoomResponse.Room chosenRoom = selectCheapest
                        ? filteredRooms.min(Comparator.comparing(r -> {
                            SelectRoomResponse.RatePlan rp = r.ratePlans.get(finalIndex);
                            if (rp.rateInfo == null || rp.rateInfo.price == null) return Integer.MAX_VALUE;
                            return rp.rateInfo.price.totalRateWithTax;
                        })).orElseThrow()
                        : filteredRooms.findFirst().orElseThrow();

                return chosenRoom.ratePlans.stream()
                        .findFirst()
                        .orElseThrow();
            }
            Thread.sleep(10000);
        }
        throw new RuntimeException("Hotel Room not found");
    }

    public String selectRoomName(SelectRoomApi.Inputs inputs) throws  InterruptedException {
        for(int attempt = 0; attempt<=3; attempt++){
            SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
            ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
            logger.log("Select Room Response : "+selectRoomApiResponse.getBody());
            Gson gson = new Gson();
            SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
            if(!selectRoomResponse.data.rooms.isEmpty()){
                logger.log("Room item color" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).itemColor);
                logger.log("Available rooms" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).availableRoom);
                int totalRoomList = selectRoomResponse.data.rooms.get(0).ratePlans.size();
                int index = -1;
                for(int i = 0; i < totalRoomList; i++){
                    logger.log("Room "+i+" Cancellation policies not null? " + (selectRoomResponse.data.rooms.get(0).ratePlans.get(i).cancellationPolicies.policies != null));
                    if(selectRoomResponse.data.rooms.get(0).ratePlans.get(i).cancellationPolicies.policies != null){
                        index = i;
                        break;
                    }
                }
                if(index == -1){
                    logger.log("No room with a cancellation policy found; falling back to the first room (index 0)");
                    index = 0;
                }
                int finalIndex = index;
                logger.log("Cancellation policies not null?" + (selectRoomResponse.data.rooms.get(0).ratePlans.get(index).cancellationPolicies.policies != null));
                return selectRoomResponse.data.rooms.stream()
                        .filter(roomList -> roomList.ratePlans.get(finalIndex).availableRoom >= inputs.room())
                        .filter(roomList -> roomList.ratePlans.get(finalIndex).cancellationPolicies.policies != null)
                        .filter(roomList -> Objects.equals(roomList.ratePlans.get(finalIndex).itemColor, nativeHotelColor))
                        .findFirst()
                        .orElseThrow()
                        .groupName;
            }
            Thread.sleep(10000);
        }
        throw new RuntimeException("Hotel Room not found");
    }

    public SelectRoomResponse.RatePlan selectRoomWithoutCancellationPolicy(SelectRoomApi.Inputs inputs) throws InterruptedException {
        for (int attempt = 0; attempt <= 3; attempt++) {
            SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
            ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
            logger.log("Select Room Response : " + selectRoomApiResponse.getBody());
            Gson gson = new Gson();
            SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
            if (!selectRoomResponse.data.rooms.isEmpty()) {
                SelectRoomResponse.Room chosenRoom = selectRoomResponse.data.rooms.stream()
                        .filter(roomList -> roomList.ratePlans.get(0).availableRoom >= inputs.room())
                        .filter(roomList -> Objects.equals(roomList.ratePlans.get(0).itemColor, nativeHotelColor))
                        .findFirst()
                        .orElseThrow();
                return chosenRoom.ratePlans.get(0);
            }
            Thread.sleep(10000);
        }
        throw new RuntimeException("Hotel Room not found");
    }

    public String selectRoomNameWithoutCancellationPolicy(SelectRoomApi.Inputs inputs) throws InterruptedException {
        for (int attempt = 0; attempt <= 3; attempt++) {
            SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
            ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
            logger.log("Select Room Response : " + selectRoomApiResponse.getBody());
            Gson gson = new Gson();
            SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
            if (!selectRoomResponse.data.rooms.isEmpty()) {
                return selectRoomResponse.data.rooms.stream()
                        .filter(roomList -> roomList.ratePlans.get(0).availableRoom >= inputs.room())
                        .filter(roomList -> Objects.equals(roomList.ratePlans.get(0).itemColor, nativeHotelColor))
                        .findFirst()
                        .orElseThrow()
                        .groupName;
            }
            Thread.sleep(10000);
        }
        throw new RuntimeException("Hotel Room not found");
    }

    /**
     * Strategy for picking one room out of the select-room response.
     * All three options apply to the same isRefundable filter; only the comparator differs.
     */
    public enum RoomSelectionStrategy {
        /** Pick the room with the lowest {@code totalRateWithTax} on the matching rate plan. */
        CHEAPEST,
        /** Pick the first matching room in API response order. */
        FIRST,
        /** Pick the (room, ratePlan) pair with the highest {@code totalRateWithTax} across all rooms. */
        MOST_EXPENSIVE
    }

    /**
     * Bundles the rate plan and group name of the same chosen B2B room so callers don't
     * have to make two separate API calls (which previously caused the rate plan and
     * group name to refer to different rooms when one path used {@code min}/{@code max}
     * but the other used {@code findFirst()} for the name lookup).
     */
    public record SelectedRoomB2B(SelectRoomResponse.RatePlan ratePlan, String groupName) {}

    /**
     * Picks one B2B room ({@link RoomSelectionStrategy#CHEAPEST cheapest},
     * {@link RoomSelectionStrategy#FIRST first}, or
     * {@link RoomSelectionStrategy#MOST_EXPENSIVE most expensive}) and returns both its
     * rate plan and group name in a single shot. Single source of truth for the B2B
     * select-room flow: one API call, one filter chain, one chosen room — guarantees
     * the rate plan and group name always belong to the same room regardless of strategy.
     *
     * @param inputs       select-room API inputs
     * @param strategy     room selection strategy (see {@link RoomSelectionStrategy})
     * @param isRefundable filter for rate plans with ({@code true}) or without
     *                     ({@code false}) cancellation policies
     */
    public SelectedRoomB2B selectRoomAndNameB2B(SelectRoomApi.Inputs inputs, RoomSelectionStrategy strategy, boolean isRefundable) throws InterruptedException {
        for (int attempt = 0; attempt <= 3; attempt++) {
            SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
            ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
            logger.log("Select Room Response : " + selectRoomApiResponse.getBody());
            Gson gson = new Gson();
            SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
            if (!selectRoomResponse.data.rooms.isEmpty()) {
                SelectedRoomB2B selected = strategy == RoomSelectionStrategy.MOST_EXPENSIVE
                        ? pickMostExpensiveRoomB2B(selectRoomResponse, inputs, isRefundable)
                        : pickRoomByIndexB2B(selectRoomResponse, inputs, strategy, isRefundable);
                logger.log("Selected B2B room: groupName=" + selected.groupName()
                        + " (strategy=" + strategy + ", isRefundable=" + isRefundable + ")");
                return selected;
            }
            Thread.sleep(10000);
        }
        throw new RuntimeException("Hotel Room not found");
    }

    /**
     * CHEAPEST / FIRST path: locks the rate plan column to the first index whose policies
     * match {@code isRefundable}, filters rooms on availability + isRefundable + nativeHotelColor,
     * then picks by min(price) or findFirst.
     */
    private SelectedRoomB2B pickRoomByIndexB2B(SelectRoomResponse response, SelectRoomApi.Inputs inputs, RoomSelectionStrategy strategy, boolean isRefundable) {
        logger.log("Room item color" + response.data.rooms.get(0).ratePlans.get(0).itemColor);
        logger.log("Available rooms" + response.data.rooms.get(0).ratePlans.get(0).availableRoom);
        int totalRoomList = response.data.rooms.get(0).ratePlans.size();
        int index = -1;
        for (int i = 0; i < totalRoomList; i++) {
            boolean policiesPresent = response.data.rooms.get(0).ratePlans.get(i).cancellationPolicies.policies != null;
            logger.log("Room " + i + " Cancellation policies present? " + policiesPresent);
            if (isRefundable == policiesPresent) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new RuntimeException("Hotel Room with " + (isRefundable ? "refundable" : "non-refundable") + " policy not found");
        }
        int finalIndex = index;
        logger.log("Selected rate plan index: " + index + " (isRefundable=" + isRefundable + ")");

        var filteredRooms = response.data.rooms.stream()
                .filter(roomList -> roomList.ratePlans.get(finalIndex).availableRoom >= inputs.room())
                .filter(roomList -> isRefundable
                        ? roomList.ratePlans.get(finalIndex).cancellationPolicies.policies != null
                        : roomList.ratePlans.get(finalIndex).cancellationPolicies.policies == null)
                .filter(roomList -> Objects.equals(roomList.ratePlans.get(finalIndex).itemColor, nativeHotelColor));

        SelectRoomResponse.Room chosenRoom = strategy == RoomSelectionStrategy.CHEAPEST
                ? filteredRooms.min(Comparator.comparing(r -> {
                    SelectRoomResponse.RatePlan rp = r.ratePlans.get(finalIndex);
                    if (rp.rateInfo == null || rp.rateInfo.price == null) return Integer.MAX_VALUE;
                    return rp.rateInfo.price.totalRateWithTax;
                })).orElseThrow()
                : filteredRooms.findFirst().orElseThrow();

        return new SelectedRoomB2B(chosenRoom.ratePlans.get(finalIndex), chosenRoom.groupName);
    }

    /**
     * MOST_EXPENSIVE path: flat-maps over (room, ratePlan) pairs, filters by availability
     * and isRefundable, then picks the pair with the highest price. Tracks the parent room
     * so the returned groupName always matches the chosen rate plan (closes the same
     * room/name divergence the bot flagged for the cheapest path).
     *
     * <p>Preserves the legacy single-room corner case: when only one room is available
     * the comparison is meaningless, so we return its first rate plan with a soft warning.
     */
    private SelectedRoomB2B pickMostExpensiveRoomB2B(SelectRoomResponse response, SelectRoomApi.Inputs inputs, boolean isRefundable) {
        if (response.data.rooms.size() == 1) {
            logger.softWarning("Only one room available, can't get the expansive room");
            SelectRoomResponse.Room only = response.data.rooms.get(0);
            // Honor isRefundable even on the fallback path: pick the first rate plan
            // whose policies match — picking the literal first rate plan can hand back
            // a non-refundable plan when the caller asked for refundable, which would
            // surface much later as a misleading reschedule/refund failure.
            SelectRoomResponse.RatePlan rp = only.ratePlans.stream()
                    .filter(plan -> isRefundable
                            ? plan.cancellationPolicies.policies != null
                            : plan.cancellationPolicies.policies == null)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "Only one room available and it has no "
                                    + (isRefundable ? "refundable" : "non-refundable")
                                    + " rate plan"));
            return new SelectedRoomB2B(rp, only.groupName);
        }
        record RoomRatePlanPair(SelectRoomResponse.Room room, SelectRoomResponse.RatePlan ratePlan) {}
        RoomRatePlanPair winner = response.data.rooms.stream()
                .flatMap(room -> room.ratePlans.stream().map(rp -> new RoomRatePlanPair(room, rp)))
                .filter(p -> p.ratePlan().availableRoom >= inputs.room())
                .filter(p -> isRefundable
                        ? p.ratePlan().cancellationPolicies.policies != null
                        : p.ratePlan().cancellationPolicies.policies == null)
                // Same null-safe price access as the CHEAPEST path: a rate plan with
                // null rateInfo / price sorts as if it had the lowest possible price
                // so it never wins the max comparison (and never NPEs the comparator).
                .max(Comparator.comparingDouble(p -> {
                    SelectRoomResponse.RatePlan rp = p.ratePlan();
                    if (rp.rateInfo == null || rp.rateInfo.price == null) return Double.NEGATIVE_INFINITY;
                    return rp.rateInfo.price.totalRateWithTax;
                }))
                .orElseThrow(() -> new RuntimeException(
                        "Hotel Room with " + (isRefundable ? "refundable" : "non-refundable")
                                + " policy not found"));
        return new SelectedRoomB2B(winner.ratePlan(), winner.room().groupName);
    }

    /** Guards the cost of multi-room flows that pay for real, since they book several rooms at once. */
    private static final int MULTI_ROOM_PRICE_CAP = 5_000_000;

    private static final String FREE_CANCELLATION = "FREE_CANCELLATION";
    private static final String CANCELLATION_WITH_FEE = "CANCELLATION_WITH_FEE";
    private static final String NO_CANCELLATION = "NO_CANCELLATION";

    private record RoomRatePlanSelection(SelectRoomResponse.Room room, SelectRoomResponse.RatePlan ratePlan) {}

    private boolean matchesCancellationPolicyType(SelectRoomResponse.RatePlan ratePlan, boolean isRefundable) {
        if (ratePlan.cancellationPolicies == null || ratePlan.cancellationPolicies.type == null) {
            return false;
        }
        String type = ratePlan.cancellationPolicies.type;
        return isRefundable
                ? FREE_CANCELLATION.equals(type) || CANCELLATION_WITH_FEE.equals(type)
                : NO_CANCELLATION.equals(type);
    }

    private boolean matchesRatePlanSelection(SelectRoomResponse.RatePlan ratePlan, SelectRoomApi.Inputs inputs,
                                             boolean isRefundable, boolean isNative) {
        return matchesRatePlanSelection(ratePlan, inputs.room(), isRefundable, isNative);
    }

    /**
     * @param requiredRooms how many units of this rate plan the booking needs; one room per rate plan
     *                      for a multi-room booking, the whole room count for a single-room booking
     */
    private boolean matchesRatePlanSelection(SelectRoomResponse.RatePlan ratePlan, int requiredRooms,
                                             boolean isRefundable, boolean isNative) {
        if (ratePlan.availableRoom < requiredRooms) {
            return false;
        }
        if (isNative && !Objects.equals(ratePlan.itemColor, nativeHotelColor)) {
            return false;
        }
        return matchesCancellationPolicyType(ratePlan, isRefundable);
    }

    private Optional<RoomRatePlanSelection> findFirstMatchingRatePlan(SelectRoomResponse response,
                                                                      SelectRoomApi.Inputs inputs,
                                                                      boolean isRefundable,
                                                                      boolean isNative) {
        return findMatchingRatePlan(response, inputs, isRefundable, isNative, RoomSelectionStrategy.FIRST);
    }

    private Optional<RoomRatePlanSelection> findMatchingRatePlan(SelectRoomResponse response,
                                                                 SelectRoomApi.Inputs inputs,
                                                                 boolean isRefundable,
                                                                 boolean isNative,
                                                                 RoomSelectionStrategy strategy) {
        var matches = response.data.rooms.stream()
                .flatMap(room -> room.ratePlans.stream()
                        .filter(ratePlan -> matchesRatePlanSelection(ratePlan, inputs, isRefundable, isNative))
                        .map(ratePlan -> new RoomRatePlanSelection(room, ratePlan)));
        return switch (strategy) {
            case FIRST -> matches.findFirst();
            case CHEAPEST -> matches.min(byTotalRateWithTax(Double.MAX_VALUE));
            case MOST_EXPENSIVE -> matches.max(byTotalRateWithTax(Double.NEGATIVE_INFINITY));
        };
    }

    /**
     * Collects the {@code count} cheapest rate plans that satisfy every booking precondition
     * {@link #selectRoom(SelectRoomApi.Inputs, boolean, boolean, RoomSelectionStrategy) selectRoom}
     * enforces - one available unit each, the native supplier when {@code isNative}, and a
     * cancellation policy matching {@code isRefundable} - and stay within {@code priceCap}. Each
     * returned selection is a distinct rate plan, so a multi-room booking sends one validated
     * {@code roomId}/{@code rateCode} pair per room instead of a price-filtered guess.
     *
     * @return the matching selections cheapest first, which is shorter than {@code count} when too
     *         few rate plans qualify
     */
    private List<RoomRatePlanSelection> findCheapestMatchingRatePlans(SelectRoomResponse response,
                                                                      int count,
                                                                      boolean isRefundable,
                                                                      boolean isNative,
                                                                      int priceCap) {
        return response.data.rooms.stream()
                .filter(room -> room.ratePlans != null)
                .flatMap(room -> room.ratePlans.stream()
                        .filter(Objects::nonNull)
                        .filter(ratePlan -> matchesRatePlanSelection(ratePlan, 1, isRefundable, isNative))
                        .filter(ratePlan -> ratePlan.rateInfo != null
                                && ratePlan.rateInfo.price != null
                                && ratePlan.rateInfo.price.totalRateWithTax <= priceCap)
                        .map(ratePlan -> new RoomRatePlanSelection(room, ratePlan)))
                .sorted(byTotalRateWithTax(Double.MAX_VALUE))
                .limit(count)
                .toList();
    }

    /**
     * A rate plan whose price is absent is sorted to {@code missingPrice}, so pass the extreme that
     * loses the comparison being made ({@code MAX_VALUE} for min, {@code NEGATIVE_INFINITY} for max).
     */
    private static Comparator<RoomRatePlanSelection> byTotalRateWithTax(double missingPrice) {
        return Comparator.comparingDouble(selection -> {
            SelectRoomResponse.RatePlan ratePlan = selection.ratePlan();
            if (ratePlan.rateInfo == null || ratePlan.rateInfo.price == null) return missingPrice;
            return ratePlan.rateInfo.price.totalRateWithTax;
        });
    }

    private SelectRoomResponse.RatePlan selectRoom(SelectRoomApi.Inputs inputs, boolean isRefundable, boolean isNative) throws InterruptedException {
        return selectRoom(inputs, isRefundable, isNative, RoomSelectionStrategy.FIRST);
    }

    /**
     * Picks one rate plan that satisfies every booking precondition - enough {@code availableRoom} for
     * the requested room count, the native supplier when {@code isNative}, and a cancellation policy
     * matching {@code isRefundable} - and returns that exact rate plan, so its {@code roomId} and
     * {@code rateCode} are a consistent pair that prebook and submit-book will accept.
     *
     * @param strategy which of the matching rate plans wins when more than one qualifies
     */
    private SelectRoomResponse.RatePlan selectRoom(SelectRoomApi.Inputs inputs, boolean isRefundable, boolean isNative,
                                                   RoomSelectionStrategy strategy) throws InterruptedException {
        for (int attempt = 0; attempt <= 3; attempt++) {
            SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
            ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
            Gson gson = new Gson();
            SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
            if (selectRoomResponse == null || selectRoomResponse.data == null || selectRoomResponse.data.rooms == null) {
                logger.log("selectRoom attempt " + attempt + " - data/rooms is null. Response body: " + selectRoomApiResponse.getBody());
                Thread.sleep(10000);
                continue;
            }
            if (!selectRoomResponse.data.rooms.isEmpty()) {
                logger.log("Room item color" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).itemColor);
                logger.log("Available rooms: " + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).availableRoom);

                return findMatchingRatePlan(selectRoomResponse, inputs, isRefundable, isNative, strategy)
                        .map(RoomRatePlanSelection::ratePlan)
                        .orElseThrow(() -> new RuntimeException(
                                "Hotel Room with " + (isRefundable ? "refundable" : "non-refundable")
                                        + " cancellation policy not found"));
            }
            Thread.sleep(10000);
        }
        throw new RuntimeException("Hotel Room not found");
    }

    private String selectRoomName(SelectRoomApi.Inputs inputs, boolean isRefundable, boolean isNative) throws InterruptedException {
        for (int attempt = 0; attempt <= 3; attempt++) {
            SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
            ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
            Gson gson = new Gson();
            SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
            if (!selectRoomResponse.data.rooms.isEmpty()) {
                logger.log("Room item color" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).itemColor);
                logger.log("Available rooms: " + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).availableRoom);

                return findFirstMatchingRatePlan(selectRoomResponse, inputs, isRefundable, isNative)
                        .map(selection -> selection.room().groupName)
                        .orElseThrow(() -> new RuntimeException(
                                "Hotel Room with " + (isRefundable ? "refundable" : "non-refundable")
                                        + " cancellation policy not found"));
            }
            Thread.sleep(10000);
        }
        throw new RuntimeException("Hotel Room not found");
    }

    private SelectRoomResponse.RatePlan findRoomFromPrice(SelectRoomResponse selectRoomResponse, int targetPrice, boolean isTax, boolean isTotalPrice) {
        int priceToCheck;
        for (SelectRoomResponse.Room room : selectRoomResponse.data.rooms) {
            for (SelectRoomResponse.RatePlan ratePlan : room.ratePlans) {
                if (isTotalPrice) {
                    priceToCheck = isTax ? ratePlan.rateInfo.price.totalRateWithTax : ratePlan.rateInfo.price.totalRateWithoutTax;
                } else {
                    priceToCheck = isTax ? ratePlan.rateInfo.price.totalBaseRateWithTax : ratePlan.rateInfo.price.totalBaseRateWithoutTax;
                }
                if (priceToCheck == targetPrice && ratePlan.availableRoom > 0) {
                    return ratePlan;
                }
            }
        }
        Gson gson = new Gson();
        String selectRoomResponseString = gson.toJson(selectRoomResponse);
        logger.log("room list on findRoomFromPrice() -> " + selectRoomResponseString);
        throw new RuntimeException("Hotel Room with targetPrice: " + targetPrice + " is not found on the API");
    }

    public String prebook(PreBookGeneratingCartApi.Inputs inputs) {
        PreBookGeneratingCartApi preBookGeneratingCartApi = new PreBookGeneratingCartApi(inputs);
        ApiResponse preBookCartApiResponse = preBookGeneratingCartApi.hitApi(apiTools);
        Gson gson = new Gson();
        PreBookGeneratingCartResponse generatingCartResponse = gson.fromJson(preBookCartApiResponse.getBody(),PreBookGeneratingCartResponse.class);
        String cartId = generatingCartResponse.data.hotels.get(0).detail.rooms.get(0).cartId;
        logger.log("cart id: " + cartId);
        return cartId;
    }

    public PreBookGeneratingCartResponse prebookResponse(PreBookGeneratingCartApi.Inputs inputs) {
        PreBookGeneratingCartApi preBookGeneratingCartApi = new PreBookGeneratingCartApi(inputs);
        ApiResponse preBookCartApiResponse = preBookGeneratingCartApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(preBookCartApiResponse.getBody(),PreBookGeneratingCartResponse.class);
    }

    private SubmitSakukuResponse submitSakuku(SubmitSakukuApi.Inputs inputs) throws InterruptedException, ExecutionException {
        SubmitSakukuApi submitSakukuApi = new SubmitSakukuApi(inputs);
        ApiResponse apiResponse = submitSakukuApi.hitApi(apiTools);
        Gson gson = new Gson();
        logger.log("response submit Sakuku : " + apiResponse.getBody());
        return gson.fromJson(apiResponse.getBody(), SubmitSakukuResponse.class);
    }

    private String paidCheckerStatus(long duration) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        long waitTime = duration * 1000;
        boolean paymentSuccessful = false;
        String paymentStatus = null;

        while (System.currentTimeMillis() - startTime < waitTime && !paymentSuccessful) {
            PaidCheckerApi.Inputs paidCheckerApiInputs = new PaidCheckerApi.Inputs(String.valueOf(orderId));
            paymentStatus = hitPaidChecker(paidCheckerApiInputs, 1);

            if ("SUCCESS".equalsIgnoreCase(paymentStatus)) {
                paymentSuccessful = true;
                HotelPendingRetrySummaryNotifier.afterApiPaymentSuccess(session, apiTools, logger);
            } else {
                Thread.sleep(5000);
            }
        }
        return paymentStatus;
    }

    private String hitPaidChecker(PaidCheckerApi.Inputs inputs, int retry) throws InterruptedException, ExecutionException {
        boolean paid = false;
        String status = "";
        for (int i = 0; i < retry; i++) {
            PaidCheckerApi paidCheckerApi = new PaidCheckerApi(inputs);
            ApiResponse apiResponse = paidCheckerApi.hitApi(apiTools);
            Gson gson = new Gson();
            PaidCheckerResponse paidCheckerResponse = gson.fromJson(apiResponse.getBody(), PaidCheckerResponse.class);
            paid = paidCheckerResponse.data.paid;
            if (paid) {
                status = paidCheckerResponse.data.paymentStatus;
                break;
            }
            Thread.sleep(500);
        }
        return status;
    }

    @SneakyThrows
    public String[] getPrebookPrice(String accommodationType, String hotelName, String username, String password, int night, int room, int adult, String startDate, String currency, String separator, int targetPrice, boolean isTax, boolean isTotalPrice) {
        String authorization = getToken(generateDeviceId());
        logger.log("authorization-> " + authorization);

        if(!username.isEmpty()) authorization = setLoginStatus(authorization, true, username, password);

        if (accommodationType.equalsIgnoreCase("homes")) accommodationType = "NHA";
        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, username, hotelName, accommodationType.toUpperCase());
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(authorization, username, adult, hotelId, night, room, startDate,"id", currency);
        SelectRoomApi selectRoomApi = new SelectRoomApi(selectRoomApiInputs);
        ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
        SelectRoomResponse.RatePlan selectedRoom = findRoomFromPrice(selectRoomResponse, targetPrice, isTax, isTotalPrice);

        PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(authorization, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate, selectedRoom.maxAdultOccupancy, selectedRoom.maxChildOccupancy, currency);
        PreBookGeneratingCartResponse prebookData = prebookResponse(preBookCartApiInputs);
        String finalPrice;
        String basePrice;
        if (isTax) {
            finalPrice = currency.toUpperCase() + " " + thousandSeparator(String.valueOf((int)prebookData.data.hotels.get(0).detail.rooms.get(0).rateInfo.price.totalRateWithTax), separator);
            basePrice = currency.toUpperCase() + " " + thousandSeparator(String.valueOf((int)prebookData.data.hotels.get(0).detail.rooms.get(0).rateInfo.price.totalBaseRateWithTax), separator);
        } else {
            finalPrice =  currency.toUpperCase() + " " + thousandSeparator(String.valueOf((int)prebookData.data.hotels.get(0).detail.rooms.get(0).rateInfo.price.totalRateWithoutTax), separator);
            basePrice = currency.toUpperCase() + " " + thousandSeparator(String.valueOf((int)prebookData.data.hotels.get(0).detail.rooms.get(0).rateInfo.price.totalBaseRateWithoutTax), separator);
        }

        return new String[]{finalPrice, basePrice};
    }

    @SneakyThrows
    public Boolean createIssuedOrderApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency,
            boolean isRefundable,
            boolean isSpecialRequestEnable,
            String prefixSpecialRequest,
            String language, boolean isNative) {
        try {
            Unm<T> unm = new Unm<>(session);
            String accessToken = unm.getAccessToken(username, password, "B2C");
            logger.log("accessToken-> " + accessToken);
            logger.log("query " + query);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);

            String hotelId = null;
            hotelName = null;
            for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
                if (query.toLowerCase().contains(content.name.toLowerCase())) {
                    hotelId = content.publicId;
                    hotelName = content.name;
                    break;
                }
            }

            logger.log("hotelId-> " + hotelId);
            logger.log("hotel name->" + hotelName);
            logger.log("Date: " + startDate);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,language, currency);
            SelectRoomResponse.RatePlan selectedRoom;

            selectedRoom = selectRoom(selectRoomApiInputs, isRefundable, isNative);
            hotelRoomName = selectRoomName(selectRoomApiInputs, isRefundable, isNative);

            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName srp -> " +  hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            String cartID = prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, currency, isSpecialRequestEnable, prefixSpecialRequest, language, hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();
            orderId = submitBookingResponse.data.orderId;
            orderHash = submitBookingResponse.data.orderHash;
            logger.log("orderId-> " + orderId);
            logger.log("orderHash-> " + orderHash);
            this.orderSpecialRequest = submitBookingApiInputs.getSpecialRequest();

            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, orderId);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!"SUCCESS".equalsIgnoreCase(submitSakukuResponse.message)) Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> " + grandTotal);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");

            OrderApi<?> orderApi = new OrderApi<>(session);
            HotelInternalOrderDetail hotelOrderDetail = orderApi.getHotelInternalOrderData(String.valueOf(orderId));
            orderName = orderApi.getHotelOrderNameFromInternalOrder(hotelOrderDetail,String.valueOf(orderId));
            logger.log("order name-> " + orderName);

            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) {
                return false;
            }
            logger.log("Waiting for 30 seconds after payment is done");
            driverTools.dummyWait(30);
            return true;
        } catch(Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
        return false;
    }

    @SneakyThrows
    public Boolean createOrderMultiRoomApi(
            String username,
            String password,
            String hotelName,
            int adult,
            int night,
            int room,
            String startDate,
            int qtyEachRoom,
            String currency,
            boolean isSpecialRequestEnable,
            String prefixSpecialRequest) {
        try {
            Unm<T> unm = new Unm<>(session);
            String authorization = unm.getAccessToken(username, password, "B2C");
            logger.log("authorization-> " + authorization);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, username, hotelName, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            int selectedIndex = findHotelIndex(hotelAutoCompleteResponse, hotelName);
            String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
            logger.log("hotelId-> " + hotelId);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(
                    authorization, username, adult, hotelId, night, room, startDate, "id", currency);
            SelectRoomResponse selectRoomResponse = findRoom(selectRoomApiInputs);

            String[] roomIdArray = new String[room];
            String[] rateCodeArray = new String[room];
            int[] maxOccupancyArray = new int[room];

            int totalRoomList = selectRoomResponse.data.rooms.size();
            logger.log("totalRoomList -> " + totalRoomList);

            final int PRICE_CAP = 5_000_000;
            List<int[]> eligible = new ArrayList<>();

            for (int rIdx = 0; rIdx < totalRoomList; rIdx++) {
                SelectRoomResponse.Room roomObj = selectRoomResponse.data.rooms.get(rIdx);
                if (roomObj.ratePlans == null || roomObj.ratePlans.isEmpty()) continue;

                for (int rpIdx = 0; rpIdx < roomObj.ratePlans.size(); rpIdx++) {
                    SelectRoomResponse.RatePlan rp = roomObj.ratePlans.get(rpIdx);
                    if (rp == null || rp.rateInfo == null || rp.rateInfo.price == null) continue;

                    int price = rp.rateInfo.price.totalRateWithTax;
                    if (price <= PRICE_CAP) {
                        eligible.add(new int[]{rIdx, rpIdx});
                    }
                }
            }

            logger.log("eligibleUnderCap -> " + eligible.size());

            if (eligible.size() < room) {
                throw new IllegalStateException(
                        "Not enough rooms under price cap " + PRICE_CAP + " (need " + room + ", found " + eligible.size() + ")"
                );
            }

            for (int i = 0; i < room; i++) {
                int[] pick = eligible.get(i);
                int roomIndex = pick[0];
                int ratePlanIndex = pick[1];

                SelectRoomResponse.RatePlan chosen = selectRoomResponse.data.rooms
                        .get(roomIndex).ratePlans.get(ratePlanIndex);

                roomIdArray[i] = chosen.roomId;
                rateCodeArray[i] = chosen.rateCode;
                maxOccupancyArray[i] = chosen.maxAdultOccupancy;

                logger.log("Chosen[" + i + "] -> roomIdx=" + roomIndex
                        + ", ratePlanIdx=" + ratePlanIndex
                        + ", totalRateWithTax=" + chosen.rateInfo.price.totalRateWithTax);
            }

            logger.log("roomIdArray-> " + Arrays.toString(roomIdArray));
            logger.log("rateCodeArray-> " + Arrays.toString(rateCodeArray));
            logger.log("maxOccupancyArray-> " + Arrays.toString(maxOccupancyArray));

            PreBookMultiRoomApi.Inputs preBookMultiRoomApiInputs = new PreBookMultiRoomApi.Inputs(authorization, adult, hotelId, night, startDate, maxOccupancyArray, qtyEachRoom, rateCodeArray, roomIdArray);
            PreBookMultiRoomResponse preBookMultiRoomResponse = preBookMultiRoom(preBookMultiRoomApiInputs);
            Gson gson = new Gson();

            String jsonString = gson.toJson(preBookMultiRoomResponse);
            logger.log("preBookMultiRoomResponse-> " + jsonString);
            roomNames.clear();
            roomPriceSummaryTotals.clear();
            cardIdArray.clear();
            for (int i = 0; i < room; i++) {
                cardIdArray.add(preBookMultiRoomResponse.data.hotels.get(0).detail.rooms.get(i).cartId);
                roomNames.add(preBookMultiRoomResponse.data.hotels.get(0).detail.rooms.get(i).roomName);
                roomPriceSummaryTotals.add(preBookMultiRoomResponse.data.hotels.get(0).detail.rooms.get(i).rateInfo.priceSummary.total);
            }
            logger.log("cardIdArray-> " + cardIdArray);
            logger.log("roomNames-> " + roomNames);
            logger.log("roomPriceSummaryTotals-> " + roomPriceSummaryTotals);

            SubmitBookingMultiRoomApi.Inputs submitBookingMultiRoomApiInputs = new SubmitBookingMultiRoomApi.Inputs(authorization, currency, username, hotelId, room, qtyEachRoom, cardIdArray, isSpecialRequestEnable, prefixSpecialRequest);
            SubmitBookingMultiRoomResponse submitBookingMultiRoomResponse = submitBookingMultiRoom(submitBookingMultiRoomApiInputs);
            guestNames = submitBookingMultiRoomApiInputs.getGuestNames();
            orderId = submitBookingMultiRoomResponse.data.orderId;
            orderHash = submitBookingMultiRoomResponse.data.orderHash;
            logger.log("orderId-> " + orderId);
            logger.log("orderHash -> " + orderHash);
            logger.log("guestNames -> " + guestNames);

            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(authorization, orderHash, orderId);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!"SUCCESS".equalsIgnoreCase(submitSakukuResponse.message)) Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> " + grandTotal);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");

            OrderApi<?> orderApi = new OrderApi<>(session);
            HotelInternalOrderDetail hotelOrderDetail = orderApi.getHotelInternalOrderData(String.valueOf(orderId));
            orderName = orderApi.getHotelOrderNameFromInternalOrder(hotelOrderDetail,String.valueOf(orderId));
            logger.log("order name-> " + orderName);

            return paymentStatus.equalsIgnoreCase("SUCCESS");
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
        return false;
    }

    @SneakyThrows
    public Boolean createOrderMultiRoomApiDifferentRooms(
            String username,
            String password,
            String hotelName,
            int adult,
            int night,
            int room,
            String startDate,
            int qtyEachRoom,
            String currency
    ) {
        try {
            if (room < 2) {
                throw new IllegalArgumentException("This method requires at least 2 rooms to ensure different room types");
            }

            Unm<T> unm = new Unm<>(session);
            String authorization = unm.getAccessToken(username, password, "B2C");
            logger.log("authorization-> " + authorization);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, username, hotelName, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            int selectedIndex = findHotelIndex(hotelAutoCompleteResponse, hotelName);
            String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
            logger.log("hotelId-> " + hotelId);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(
                    authorization, username, adult, hotelId, night, room, startDate, "id", currency);
            SelectRoomResponse selectRoomResponse = findRoom(selectRoomApiInputs);

            String[] roomIdArray = new String[room];
            String[] rateCodeArray = new String[room];
            int[] maxOccupancyArray = new int[room];

            int totalRoomList = selectRoomResponse.data.rooms.size();
            logger.log("totalRoomList -> " + totalRoomList);

            final int PRICE_CAP = 5_000_000;

            // Map to store eligible rooms grouped by roomIndex
            Map<Integer, List<int[]>> eligibleByRoomIndex = new LinkedHashMap<>();

            for (int rIdx = 0; rIdx < totalRoomList; rIdx++) {
                SelectRoomResponse.Room roomObj = selectRoomResponse.data.rooms.get(rIdx);
                if (roomObj.ratePlans == null || roomObj.ratePlans.isEmpty()) continue;

                for (int rpIdx = 0; rpIdx < roomObj.ratePlans.size(); rpIdx++) {
                    SelectRoomResponse.RatePlan rp = roomObj.ratePlans.get(rpIdx);
                    if (rp == null || rp.rateInfo == null || rp.rateInfo.price == null) continue;

                    int price = rp.rateInfo.price.totalRateWithTax;
                    if (price <= PRICE_CAP) {
                        eligibleByRoomIndex.computeIfAbsent(rIdx, k -> new ArrayList<>())
                                .add(new int[]{rIdx, rpIdx});
                    }
                }
            }

            logger.log("Number of different room types under price cap -> " + eligibleByRoomIndex.size());

            if (eligibleByRoomIndex.size() < room) {
                throw new IllegalStateException(
                        "Not enough different room types under price cap " + PRICE_CAP +
                        " (need " + room + " different types, found " + eligibleByRoomIndex.size() + ")"
                );
            }

            // Select rooms ensuring different roomIndex for each
            int roomCounter = 0;
            for (Map.Entry<Integer, List<int[]>> entry : eligibleByRoomIndex.entrySet()) {
                if (roomCounter >= room) break;

                // Pick the first rate plan for this room type
                int[] pick = entry.getValue().get(0);
                int roomIndex = pick[0];
                int ratePlanIndex = pick[1];

                SelectRoomResponse.RatePlan chosen = selectRoomResponse.data.rooms
                        .get(roomIndex).ratePlans.get(ratePlanIndex);

                roomIdArray[roomCounter] = chosen.roomId;
                rateCodeArray[roomCounter] = chosen.rateCode;
                maxOccupancyArray[roomCounter] = chosen.maxAdultOccupancy;

                logger.log("Chosen[" + roomCounter + "] -> roomIdx=" + roomIndex
                        + ", ratePlanIdx=" + ratePlanIndex
                        + ", groupName=" + selectRoomResponse.data.rooms.get(roomIndex).groupName
                        + ", totalRateWithTax=" + chosen.rateInfo.price.totalRateWithTax);

                roomCounter++;
            }

            logger.log("roomIdArray-> " + Arrays.toString(roomIdArray));
            logger.log("rateCodeArray-> " + Arrays.toString(rateCodeArray));
            logger.log("maxOccupancyArray-> " + Arrays.toString(maxOccupancyArray));

            PreBookMultiRoomApi.Inputs preBookMultiRoomApiInputs = new PreBookMultiRoomApi.Inputs(authorization, adult, hotelId, night, startDate, maxOccupancyArray, qtyEachRoom, rateCodeArray, roomIdArray);
            PreBookMultiRoomResponse preBookMultiRoomResponse = preBookMultiRoom(preBookMultiRoomApiInputs);
            Gson gson = new Gson();

            String jsonString = gson.toJson(preBookMultiRoomResponse);
            logger.log("preBookMultiRoomResponse-> " + jsonString);
            roomNames.clear();
            roomPriceSummaryTotals.clear();
            cardIdArray.clear();
            for (int i = 0; i < room; i++) {
                cardIdArray.add(preBookMultiRoomResponse.data.hotels.get(0).detail.rooms.get(i).cartId);
                roomNames.add(preBookMultiRoomResponse.data.hotels.get(0).detail.rooms.get(i).roomName);
                roomPriceSummaryTotals.add(preBookMultiRoomResponse.data.hotels.get(0).detail.rooms.get(i).rateInfo.priceSummary.total);
            }
            logger.log("cardIdArray-> " + cardIdArray);
            logger.log("roomNames-> " + roomNames);
            logger.log("roomPriceSummaryTotals-> " + roomPriceSummaryTotals);

            SubmitBookingMultiRoomApi.Inputs submitBookingMultiRoomApiInputs = new SubmitBookingMultiRoomApi.Inputs(authorization, currency, username, hotelId, room, qtyEachRoom, cardIdArray, false,"");
            SubmitBookingMultiRoomResponse submitBookingMultiRoomResponse = submitBookingMultiRoom(submitBookingMultiRoomApiInputs);
            orderId = submitBookingMultiRoomResponse.data.orderId;
            orderHash = submitBookingMultiRoomResponse.data.orderHash;
            guestNames = submitBookingMultiRoomApiInputs.getGuestNames();
            logger.log("orderId-> " + orderId);
            logger.log("orderHash -> " + orderHash);
            logger.log("guestNames -> " + guestNames);

            // Map room names to guest names
            roomNameToGuestNamesMap.clear();
            int guestNameIndex = 0;
            for (int i = 0; i < room; i++) {
                String roomName = roomNames.get(i);
                List<String> guestsInRoom = new ArrayList<>();

                // Each room can have multiple guests based on qtyEachRoom
                for (int j = 0; j < qtyEachRoom; j++) {
                    if (guestNameIndex < guestNames.size()) {
                        guestsInRoom.add(guestNames.get(guestNameIndex));
                        guestNameIndex++;
                    }
                }

                roomNameToGuestNamesMap.put(roomName, guestsInRoom);
            }
            logger.log("roomNameToGuestNamesMap-> " + roomNameToGuestNamesMap);

            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(authorization, orderHash, orderId);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!"SUCCESS".equalsIgnoreCase(submitSakukuResponse.message)) Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> " + grandTotal);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");

            OrderApi<?> orderApi = new OrderApi<>(session);
            HotelInternalOrderDetail hotelOrderDetail = orderApi.getHotelInternalOrderData(String.valueOf(orderId));
            orderName = orderApi.getHotelOrderNameFromInternalOrder(hotelOrderDetail,String.valueOf(orderId));
            logger.log("order name-> " + orderName);

            return paymentStatus.equalsIgnoreCase("SUCCESS");
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
        return false;
    }

    @SneakyThrows
    public String createWaitingPaymentMultiRoomApi(
            String username,
            String password,
            String hotelName,
            int adult,
            int night,
            int room,
            String startDate,
            int qtyEachRoom,
            String currency,
            boolean isSpecialRequestEnable,
            String prefixSpecialRequest
    ) {
        try {
            Unm<T> unm = new Unm<>(session);
            String authorization = unm.getAccessToken(username, password, "B2C");
            logger.log("authorization-> " + authorization);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, username, hotelName, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            int selectedIndex = findHotelIndex(hotelAutoCompleteResponse, hotelName);
            String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
            logger.log("hotelId-> " + hotelId);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(
                    authorization, username, adult, hotelId, night, room, startDate, "id", currency);
            SelectRoomResponse selectRoomResponse = findRoom(selectRoomApiInputs);

            String[] roomIdArray = new String[room];
            String[] rateCodeArray = new String[room];
            int[] maxOccupancyArray = new int[room];

            int totalRoomList = selectRoomResponse.data.rooms.size();
            logger.log("totalRoomList -> " + totalRoomList);

            final int PRICE_CAP = 5_000_000;
            List<int[]> eligible = new ArrayList<>();

            for (int rIdx = 0; rIdx < totalRoomList; rIdx++) {
                SelectRoomResponse.Room roomObj = selectRoomResponse.data.rooms.get(rIdx);
                if (roomObj.ratePlans == null || roomObj.ratePlans.isEmpty()) continue;

                for (int rpIdx = 0; rpIdx < roomObj.ratePlans.size(); rpIdx++) {
                    SelectRoomResponse.RatePlan rp = roomObj.ratePlans.get(rpIdx);
                    if (rp == null || rp.rateInfo == null || rp.rateInfo.price == null) continue;

                    int price = rp.rateInfo.price.totalRateWithTax;
                    if (price <= PRICE_CAP) {
                        eligible.add(new int[]{rIdx, rpIdx});
                    }
                }
            }

            logger.log("eligibleUnderCap -> " + eligible.size());

            if (eligible.size() < room) {
                throw new IllegalStateException(
                        "Not enough rooms under price cap " + PRICE_CAP + " (need " + room + ", found " + eligible.size() + ")"
                );
            }

            for (int i = 0; i < room; i++) {
                int[] pick = eligible.get(i);
                int roomIndex = pick[0];
                int ratePlanIndex = pick[1];

                SelectRoomResponse.RatePlan chosen = selectRoomResponse.data.rooms
                        .get(roomIndex).ratePlans.get(ratePlanIndex);

                roomIdArray[i] = chosen.roomId;
                rateCodeArray[i] = chosen.rateCode;
                maxOccupancyArray[i] = chosen.maxAdultOccupancy;

                logger.log("Chosen[" + i + "] -> roomIdx=" + roomIndex
                        + ", ratePlanIdx=" + ratePlanIndex
                        + ", totalRateWithTax=" + chosen.rateInfo.price.totalRateWithTax);
            }

            logger.log("roomIdArray-> " + Arrays.toString(roomIdArray));
            logger.log("rateCodeArray-> " + Arrays.toString(rateCodeArray));
            logger.log("maxOccupancyArray-> " + Arrays.toString(maxOccupancyArray));

            PreBookMultiRoomApi.Inputs preBookMultiRoomApiInputs = new PreBookMultiRoomApi.Inputs(authorization, adult, hotelId, night, startDate, maxOccupancyArray, qtyEachRoom, rateCodeArray, roomIdArray);
            PreBookMultiRoomResponse preBookMultiRoomResponse = preBookMultiRoom(preBookMultiRoomApiInputs);
            Gson gson = new Gson();

            String jsonString = gson.toJson(preBookMultiRoomResponse);
            logger.log("preBookMultiRoomResponse-> " + jsonString);
            roomNames.clear();
            roomPriceSummaryTotals.clear();
            cardIdArray.clear();
            for (int i = 0; i < room; i++) {
                cardIdArray.add(preBookMultiRoomResponse.data.hotels.get(0).detail.rooms.get(i).cartId);
                roomNames.add(preBookMultiRoomResponse.data.hotels.get(0).detail.rooms.get(i).roomName);
                roomPriceSummaryTotals.add(preBookMultiRoomResponse.data.hotels.get(0).detail.rooms.get(i).rateInfo.priceSummary.total);
            }
            logger.log("cardIdArray-> " + cardIdArray);
            logger.log("roomNames-> " + roomNames);
            logger.log("roomPriceSummaryTotals-> " + roomPriceSummaryTotals);

            SubmitBookingMultiRoomApi.Inputs submitBookingMultiRoomApiInputs = new SubmitBookingMultiRoomApi.Inputs(authorization, currency, username, hotelId, room, qtyEachRoom, cardIdArray, isSpecialRequestEnable, prefixSpecialRequest);
            SubmitBookingMultiRoomResponse submitBookingMultiRoomResponse = submitBookingMultiRoom(submitBookingMultiRoomApiInputs);
            orderId = submitBookingMultiRoomResponse.data.orderId;
            orderHash = submitBookingMultiRoomResponse.data.orderHash;
            guestNames = submitBookingMultiRoomApiInputs.getGuestNames();
            logger.log("orderId-> " + orderId);
            logger.log("orderHash -> " + orderHash);
            logger.log("guestNames -> " + guestNames);

            // Map room names to guest names
            roomNameToGuestNamesMap.clear();
            int guestNameIndex = 0;
            for (int i = 0; i < room; i++) {
                String roomName = roomNames.get(i);
                List<String> guestsInRoom = new ArrayList<>();

                // Each room can have multiple guests based on qtyEachRoom
                for (int j = 0; j < qtyEachRoom; j++) {
                    if (guestNameIndex < guestNames.size()) {
                        guestsInRoom.add(guestNames.get(guestNameIndex));
                        guestNameIndex++;
                    }
                }

                roomNameToGuestNamesMap.put(roomName, guestsInRoom);
            }
            logger.log("roomNameToGuestNamesMap-> " + roomNameToGuestNamesMap);

            // NOTE: No payment step - order remains in WAITING_PAYMENT status
            logger.log("Order created with WAITING_PAYMENT status. Order ID: " + orderId);

            return String.valueOf(orderId);
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
        return null;
    }

    public SelectRoomResponse findRoom(SelectRoomApi.Inputs inputs) {
        SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
        ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
    }

    public PreBookMultiRoomResponse preBookMultiRoom(PreBookMultiRoomApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PreBookMultiRoomApi preBookMultiRoomApi = new PreBookMultiRoomApi(inputs);
        ApiResponse apiResponse = preBookMultiRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), PreBookMultiRoomResponse.class);
    }

    public SubmitBookingMultiRoomResponse submitBookingMultiRoom(SubmitBookingMultiRoomApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitBookingMultiRoomApi submitBookingMultiRoomApi = new SubmitBookingMultiRoomApi(inputs);
        ApiResponse apiResponse = submitBookingMultiRoomApi.hitApi(apiTools);
        String body = apiResponse.getBody().toString();
        logger.log("Hotel multi-room response : " + body);
        Gson gson = new Gson();
        SubmitBookingMultiRoomResponse response = gson.fromJson(body, SubmitBookingMultiRoomResponse.class);

        // Same fail-fast contract as submitBookingV2 - reject responses where
        //   1. code != SUCCESS         (e.g. THIRD_PARTY_ERROR)
        //   2. code == SUCCESS but data.orderId == 0 with a `popup` payload
        //      (e.g. ROOM_UNAVAILABLE_SINGLE_ORDER when a room sold out)
        // so callers don't silently propagate orderId=0 downstream.
        if (response == null
                || response.data == null
                || !"SUCCESS".equalsIgnoreCase(response.code)
                || response.data.orderId == 0) {
            String code = response != null ? response.code : "UNKNOWN";
            String message = response != null ? response.message : "no response body";
            String popupInfo = "";
            if (response != null && response.data != null && response.data.popup != null) {
                SubmitBookingMultiRoomResponse.Popup p = response.data.popup;
                popupInfo = " | popup[type=" + p.type + ", title=" + p.title + ", body=" + p.body + "]";
            }
            throw new RuntimeException(
                    "submitBookingMultiRoom failed [code=" + code + ", message=" + message + "]"
                            + popupInfo + ": " + body);
        }
        return response;
    }

    @SneakyThrows
    public HotelAutoCompleteResponse.Data.Content getHotelDetail(
            String query
    ) {
        try {
            String accessToken = getToken(generateDeviceId());
            logger.log("accessToken-> " + accessToken);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, "", query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);

            String hotelId = null;
            hotelName = null;
            for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
                if (query.equalsIgnoreCase(content.name)) {
                    logger.log("hotelId-> " + hotelId);
                    logger.log("hotel name->" + hotelName);
                    hotelId = content.publicId;
                    hotelName = content.name;
                    return content;
                }
            }
        }catch (Exception e){
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            return null;
        }
        return null;
    }

    @SneakyThrows
    public HotelAutoCompleteResponse getAutocompleteData(String username, String password, String query, String accommodationTypes) {
        String accessToken = getToken(generateDeviceId());
        logger.log("accessToken-> " + accessToken);

        boolean loginStatus;
        if (username.isEmpty() || password.isEmpty()) {
            loginStatus = false;
        } else {
            Unm<T> unm = new Unm<>(session);
            accessToken = unm.getAccessToken(username, password, "B2C");
            logger.log("accessToken-> " + accessToken);
            loginStatus = true;
        }
        logger.log("loginStatus-> " + username + ", " + loginStatus);

        if (accommodationTypes.equalsIgnoreCase("homes")) accommodationTypes = "NHA";
        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, accommodationTypes);
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        logger.log("getAutocompleteData -> " + hotelAutoCompleteResponse.message);
        return hotelAutoCompleteResponse;
    }

    public record ResolvedPdpUrl(String pdpUrl, String propertyName, String publicId, String countryId) {}

    private static final double MAX_PROPERTY_COORDINATE_DISTANCE_METERS = 1000;

    /**
     * Tries each property name (up to {@code maxAttempts}) against hotel autocomplete and returns the first
     * entry with a valid {@code publicId}, {@code country.id}, and coordinates within {@code 1000m} of the
     * table Long Lat value, formatted as {@code https://preprod.tiket.com/hotel/{country}/{publicId}}.
     */
    @SneakyThrows
    public ResolvedPdpUrl resolveExistingPdpUrlFromPropertyNames(
            String username,
            String password,
            List<String> propertyNames,
            Map<String, String> tableLongLatByPropertyName,
            String accommodationType,
            int maxAttempts) {
        int attempts = Math.min(maxAttempts, propertyNames.size());
        for (int i = 0; i < attempts; i++) {
            String propertyName = propertyNames.get(i);
            logger.log(String.format("Resolving PDP URL for property name [%d/%d]: %s", i + 1, attempts, propertyName));

            String tableLongLat = tableLongLatByPropertyName.get(propertyName);
            if (tableLongLat == null || tableLongLat.isBlank()) {
                logger.log("No Long Lat value found in table for: " + propertyName);
                continue;
            }

            HotelAutoCompleteResponse hotelAutoCompleteResponse =
                    getAutocompleteData(username, password, propertyName, accommodationType);
            if (hotelAutoCompleteResponse == null
                    || hotelAutoCompleteResponse.data == null
                    || hotelAutoCompleteResponse.data.contents == null
                    || hotelAutoCompleteResponse.data.contents.isEmpty()) {
                logger.log("Autocomplete returned no contents for: " + propertyName);
                continue;
            }

            HotelAutoCompleteResponse.Data.Content matchedContent = findAutocompleteContentWithValidPdpData(
                    hotelAutoCompleteResponse, propertyName);
            if (matchedContent == null) {
                logger.log("No valid publicId/country.id found for: " + propertyName);
                continue;
            }

            if (!hasValidAutocompleteCoordinates(matchedContent)) {
                logger.log("Autocomplete content has no valid latitude/longitude for: " + propertyName);
                continue;
            }

            double[] tableCoordinates = CoreFunctions.parseTableLongLat(tableLongLat);
            double apiLatitude = Double.parseDouble(matchedContent.latitude.trim());
            double apiLongitude = Double.parseDouble(matchedContent.longitude.trim());
            double distanceInMeters = CoreFunctions.calculateDistanceInMeters(
                    tableCoordinates[0], tableCoordinates[1], apiLatitude, apiLongitude);
            logger.log(String.format(
                    "Coordinate distance for %s: %.2fm (table: %s, api: %s, %s)",
                    propertyName,
                    distanceInMeters,
                    tableLongLat,
                    matchedContent.latitude,
                    matchedContent.longitude));
            if (distanceInMeters > MAX_PROPERTY_COORDINATE_DISTANCE_METERS) {
                logger.log(String.format(
                        "Skipping %s because coordinate distance %.2fm exceeds %dm",
                        propertyName, distanceInMeters, (int) MAX_PROPERTY_COORDINATE_DISTANCE_METERS));
                continue;
            }

            String pdpUrl = String.format("%s/%s/%s",
                    getHotelPdpBaseUrl(),
                    matchedContent.country.id.toLowerCase(Locale.ROOT),
                    matchedContent.publicId);
            logger.log("Resolved PDP URL: " + pdpUrl);
            return new ResolvedPdpUrl(pdpUrl, propertyName, matchedContent.publicId, matchedContent.country.id);
        }
        throw new IllegalStateException(
                "Could not resolve a valid PDP URL within "
                        + (int) MAX_PROPERTY_COORDINATE_DISTANCE_METERS
                        + "m from table coordinates after "
                        + attempts
                        + " attempt(s)");
    }

    public record ResolvedUnmappedProperty(String vendorName, String vendorPropertyId, String propertyName, String pdpUrl) {}

    private static final Map<String, String> UNMAPPED_HOTELS_VENDOR_NAME_TO_API_PARAM = Map.of(
            "Booking.com", "BCOM",
            "Hotelbedsss", "HOTELBEDS",
            "Expedia Rapid", "EXPEDIA_RAPID",
            "Agoda", "AGODA");

    /**
     * Converts a test-data vendor name (e.g. {@code Booking.com}) to the unmapped-hotels API param
     * (e.g. {@code BCOM}).
     */
    public String toUnmappedHotelsApiVendorName(String testDataVendorName) {
        String trimmedVendorName = testDataVendorName.trim().replaceAll(",+$", "");
        for (Map.Entry<String, String> entry : UNMAPPED_HOTELS_VENDOR_NAME_TO_API_PARAM.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(trimmedVendorName)) {
                return entry.getValue();
            }
        }
        throw new IllegalArgumentException("Unsupported vendor name for unmapped hotels API: " + testDataVendorName);
    }

    @SneakyThrows
    public UnmappedHotelsSearchResponse searchUnmappedHotels(String vendorName) {
        return searchUnmappedHotels(vendorName, 0, 10000);
    }

    @SneakyThrows
    public UnmappedHotelsSearchResponse searchUnmappedHotels(String vendorName, int page, int limit) {
        UnmappedHotelsSearchApi unmappedHotelsSearchApi =
                new UnmappedHotelsSearchApi(new UnmappedHotelsSearchApi.Inputs(vendorName, page, limit));
        ApiResponse apiResponse = unmappedHotelsSearchApi.hitApi(apiTools);
        logger.log("searchUnmappedHotels -> " + apiResponse.getBody());
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), UnmappedHotelsSearchResponse.class);
    }

    /**
     * Tries each vendor name (slash-separated, e.g. {@code Booking.com/Hotelbeds/Expedia Rapid}) until the first
     * valid unmapped property is resolved via {@link #resolveVendorPropertyFromUnmappedHotels}.
     */
    @SneakyThrows
    public ResolvedUnmappedProperty resolveVendorPropertyFromVendorNames(
            String vendorNames,
            String username,
            String password,
            String accommodationType) {
        for (String name : vendorNames.split("/")) {
            String trimmedVendorName = name.trim().replaceAll(",+$", "");
            if (trimmedVendorName.isEmpty()) {
                continue;
            }
            try {
                String apiVendorName = toUnmappedHotelsApiVendorName(trimmedVendorName);
                UnmappedHotelsSearchResponse unmappedHotels = searchUnmappedHotels(apiVendorName);
                return resolveVendorPropertyFromUnmappedHotels(
                        unmappedHotels, trimmedVendorName, username, password, accommodationType);
            } catch (IllegalArgumentException | IllegalStateException e) {
                logger.log("No valid unmapped property resolved for vendor: " + trimmedVendorName + " (" + e.getMessage() + ")");
            }
        }
        throw new IllegalStateException("Could not resolve a valid property from vendors: " + vendorNames);
    }

    public record ResolvedUnmappedPropertyWithLogin(
            String vendorName, String vendorPropertyId, String propertyName, String pdpUrl, String email) {}

    /**
     * Tries each API vendor name (slash-separated, e.g. {@code BCOM/HOTELBEDS/EXPEDIA_RAPID/AGODA}) until the first
     * valid unmapped property is resolved. Returns the login email at the same index as the matched vendor.
     */
    @SneakyThrows
    public ResolvedUnmappedPropertyWithLogin resolveVendorPropertyAndEmail(
            String apiVendorNames,
            String emails,
            String username,
            String password,
            String accommodationType) {
        String[] vendorList = apiVendorNames.split("/");
        String[] emailList = emails.split("/");
        for (int i = 0; i < vendorList.length; i++) {
            String apiVendorName = vendorList[i].trim().replaceAll(",+$", "");
            if (apiVendorName.isEmpty()) {
                continue;
            }
            try {
                UnmappedHotelsSearchResponse unmappedHotels = searchUnmappedHotels(apiVendorName);
                ResolvedUnmappedProperty property = resolveVendorPropertyFromUnmappedHotels(
                        unmappedHotels, apiVendorName, username, password, accommodationType);
                if (i >= emailList.length) {
                    throw new IllegalStateException(
                            "No email configured at index " + i + " for vendor: " + apiVendorName);
                }
                String email = emailList[i].trim().replaceAll(",+$", "");
                logger.log(String.format(
                        "Resolved unmapped property with login - email: %s, vendorName: %s, vendorPropertyId: %s, propertyName: %s",
                        email,
                        property.vendorName(),
                        property.vendorPropertyId(),
                        property.propertyName()));
                return new ResolvedUnmappedPropertyWithLogin(
                        property.vendorName(),
                        property.vendorPropertyId(),
                        property.propertyName(),
                        property.pdpUrl(),
                        email);
            } catch (IllegalStateException e) {
                logger.log("No valid unmapped property resolved for API vendor: " + apiVendorName);
            }
        }
        throw new IllegalStateException(
                "Could not resolve a valid property and email from vendors: " + apiVendorNames);
    }

    /**
     * Tries each unmapped hotel (up to the API content size or {@link UnmappedHotelsSearchApi.Inputs} limit,
     * whichever is smaller) against hotel autocomplete and returns the first entry with a valid PDP URL whose
     * coordinates are within {@code 1000m} of the unmapped hotel coordinates.
     */
    @SneakyThrows
    public ResolvedUnmappedProperty resolveVendorPropertyFromUnmappedHotels(
            UnmappedHotelsSearchResponse unmappedHotelsSearchResponse,
            String vendorName,
            String username,
            String password,
            String accommodationType) {
        if (unmappedHotelsSearchResponse == null
                || unmappedHotelsSearchResponse.data == null
                || unmappedHotelsSearchResponse.data.content == null
                || unmappedHotelsSearchResponse.data.content.isEmpty()) {
            throw new IllegalStateException("No unmapped hotels found in search response");
        }

        int unmappedHotelsLimit = new UnmappedHotelsSearchApi.Inputs("").limit();
        int attempts = Math.min(unmappedHotelsSearchResponse.data.content.size(), unmappedHotelsLimit);
        for (int i = 0; i < attempts; i++) {
            UnmappedHotelsSearchResponse.Content unmappedHotel = unmappedHotelsSearchResponse.data.content.get(i);
            String propertyName = unmappedHotel.hotelName;
            String vendorPropertyId = unmappedHotel.hotelId;
            logger.log(String.format(
                    "Resolving vendor property [%d/%d]: %s (%s)",
                    i + 1, attempts, propertyName, vendorPropertyId));

            HotelAutoCompleteResponse hotelAutoCompleteResponse =
                    getAutocompleteData(username, password, propertyName, accommodationType);
            if (hotelAutoCompleteResponse == null
                    || hotelAutoCompleteResponse.data == null
                    || hotelAutoCompleteResponse.data.contents == null
                    || hotelAutoCompleteResponse.data.contents.isEmpty()) {
                logger.log("Autocomplete returned no contents for: " + propertyName);
                continue;
            }

            HotelAutoCompleteResponse.Data.Content matchedContent = findAutocompleteContentWithValidPdpData(
                    hotelAutoCompleteResponse, propertyName);
            if (matchedContent == null) {
                logger.log("No valid publicId/country.id found for: " + propertyName);
                continue;
            }

            if (!hasValidAutocompleteCoordinates(matchedContent)) {
                logger.log("Autocomplete content has no valid latitude/longitude for: " + propertyName);
                continue;
            }

            double unmappedLatitude = unmappedHotel.latitude;
            double unmappedLongitude = unmappedHotel.longitude;
            double apiLatitude = Double.parseDouble(matchedContent.latitude.trim());
            double apiLongitude = Double.parseDouble(matchedContent.longitude.trim());
            double distanceInMeters = CoreFunctions.calculateDistanceInMeters(
                    unmappedLatitude, unmappedLongitude, apiLatitude, apiLongitude);
            logger.log(String.format(
                    "Coordinate distance for %s: %.2fm (unmapped: %s, %s, api: %s, %s)",
                    propertyName,
                    distanceInMeters,
                    unmappedLatitude,
                    unmappedLongitude,
                    matchedContent.latitude,
                    matchedContent.longitude));
            if (distanceInMeters > MAX_PROPERTY_COORDINATE_DISTANCE_METERS) {
                logger.log(String.format(
                        "Skipping %s because coordinate distance %.2fm exceeds %dm",
                        propertyName, distanceInMeters, (int) MAX_PROPERTY_COORDINATE_DISTANCE_METERS));
                continue;
            }

            String pdpUrl = String.format("%s/%s/%s",
                    getHotelPdpBaseUrl(),
                    matchedContent.country.id.toLowerCase(Locale.ROOT),
                    matchedContent.publicId);
            logger.log("Resolved PDP URL: " + pdpUrl);
            logger.log(String.format(
                    "Resolved unmapped property - vendorName: %s, vendorPropertyId: %s, propertyName: %s",
                    vendorName, vendorPropertyId, propertyName));
            return new ResolvedUnmappedProperty(vendorName, vendorPropertyId, propertyName, pdpUrl);
        }
        throw new IllegalStateException(
                "Could not resolve a vendor property within "
                        + (int) MAX_PROPERTY_COORDINATE_DISTANCE_METERS
                        + "m from unmapped hotel coordinates after "
                        + attempts
                        + " attempt(s)");
    }

    private HotelAutoCompleteResponse.Data.Content findAutocompleteContentWithValidPdpData(
            HotelAutoCompleteResponse hotelAutoCompleteResponse, String propertyName) {
        for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
            if (propertyName.equalsIgnoreCase(content.name) && hasValidPdpAutocompleteContent(content)) {
                return content;
            }
        }
        int index = findHotelIndex(hotelAutoCompleteResponse, propertyName);
        HotelAutoCompleteResponse.Data.Content content = hotelAutoCompleteResponse.data.contents.get(index);
        if (propertyName.equalsIgnoreCase(content.name) && hasValidPdpAutocompleteContent(content)) {
            return content;
        }
        return null;
    }

    private boolean hasValidPdpAutocompleteContent(HotelAutoCompleteResponse.Data.Content content) {
        return content != null
                && content.publicId != null && !content.publicId.isBlank()
                && content.country != null
                && content.country.id != null && !content.country.id.isBlank();
    }

    private boolean hasValidAutocompleteCoordinates(HotelAutoCompleteResponse.Data.Content content) {
        return content.latitude != null && !content.latitude.isBlank()
                && content.longitude != null && !content.longitude.isBlank();
    }

    private String getHotelPdpBaseUrl() {
        return switch (Configs.ENVIRONMENT) {
            case GK -> "https://gatotkaca.tiket.com/hotel";
            case PROD -> "https://tiket.com/hotel";
            default -> "https://preprod.tiket.com/hotel";
        };
    }

    @SneakyThrows
    public SearchHotelResponse getSearchHotelData(String accommodationType, String locationId, String locationType, String username, String password, int night, int room, int adult, String startDate, String currency) {
        String accessToken = getToken(generateDeviceId());
        logger.log("accessToken-> " + accessToken);

        if ("NHA".equalsIgnoreCase(accommodationType) || "HOMES".equalsIgnoreCase(accommodationType)) {
            accommodationType = "nha";
        } else {
            accommodationType = "hotel";
        }
        if(!username.isEmpty()){
            accessToken = setLoginStatus(accessToken, true, username, password);
        }
        String userAgent = userAgentValue();
        logger.log("userAgent-> " + userAgent);

        HotelSearchApi.Inputs hotelSearchApiInputs = new HotelSearchApi.Inputs(accessToken, username, adult, night, room, locationType, locationId, startDate, "", accommodationType, userAgent, currency);
        return customSearchHotel(hotelSearchApiInputs);
    }

    @SneakyThrows
    public SearchHotelResponse customSearchHotel(HotelSearchApi.Inputs inputs) {
        HotelSearchApi hotelSearchApi = new HotelSearchApi(inputs);
        ApiResponse hotelSearchApiResponse = hotelSearchApi.hitApi(apiTools);
        logger.log("hotelSearchApiResponse-> " + hotelSearchApiResponse.getBody());
        Gson gson = new Gson();
        return gson.fromJson(hotelSearchApiResponse.getBody(), SearchHotelResponse.class);
    }

    /**
     * Updates a hotel room rate / allotment on the rates-and-availability dashboard.
     *
     * @param updateCancellationPolicy true  -> apply default refundable cancellation policy (15% charge & no-show, 1 day before);
     *                                 false -> send null cancellation values (non-refundable).
     * @param rate                     the room rate amount to set.
     */
    @SneakyThrows
    public ApiResponse updateHotelAllotmentApi(HotelAllotmentsApi.Inputs inputs) {
        HotelAllotmentsApi hotelAllotmentsApi = new HotelAllotmentsApi(inputs);
        ApiResponse response = hotelAllotmentsApi.hitApi(apiTools);
        logger.log("updateHotelAllotmentApi response-> " + response.getBody());
        return response;
    }

    /**
     * Builds the allotment payload from the supplied parameters, hits the rates-and-availability
     * dashboard allotments API and asserts it succeeds (HTTP 200).
     *
     * @param rate                      the room rate amount to set.
     * @param updateCancellationPolicy  true  -> apply refundable cancellation policy using the supplied charge & no-show percentages (1 day before);
     *                                  false -> send null cancellation values (non-refundable).
     * @param cancellationChargeValue   cancellation charge percentage (used only when updateCancellationPolicy is true).
     * @param cancellationNoShowValue   cancellation no-show percentage (used only when updateCancellationPolicy is true).
     */
    @SneakyThrows
    public ApiResponse updateHotelAllotmentApi(String authorization, String cookie, String username, String accountId, String hotelId, String date,
                                               long allotmentId, long roomId, String roomName, long roomRateId,
                                               int ratePlanId, int rate, boolean updateCancellationPolicy,
                                               int cancellationChargeValue, int cancellationNoShowValue) {
        HotelAllotmentsApi.Inputs inputs = new HotelAllotmentsApi.Inputs(
                authorization, cookie, username, accountId, hotelId, date, allotmentId, roomId, roomName, roomRateId,
                ratePlanId, rate, updateCancellationPolicy, cancellationChargeValue, cancellationNoShowValue);
        ApiResponse response = updateHotelAllotmentApi(inputs);
        Assertion.assertThat(
                "Allotment update API (rate=" + rate + ", updateCancellationPolicy=" + updateCancellationPolicy + ") should return HTTP 200",
                response.getStatusCode(), is(200));
        return response;
    }

    /**
     * Binds the given hotel to the current rates-and-availability dashboard session.
     * Must be called after the Extranet admin login and before any hotel-scoped write
     * (e.g. {@link #updateHotelAllotmentApi}); otherwise those calls return HTTP 401.
     *
     * @param authorization the fresh Extranet access token (with "Bearer " prefix).
     * @param hotelId       the hotel to activate for the session.
     */
    @SneakyThrows
    public ApiResponse changeHotelSession(String authorization, String hotelId) {
        ChangeHotelSessionApi changeHotelSessionApi = new ChangeHotelSessionApi(
                new ChangeHotelSessionApi.Inputs(authorization, hotelId));
        ApiResponse response = changeHotelSessionApi.hitApi(apiTools);
        logger.log("changeHotelSession response-> " + response.getBody());
        Assertion.assertThat(
                "Change-hotel session API (hotelId=" + hotelId + ") should return HTTP 200",
                response.getStatusCode(), is(200));
        return response;
    }

    @SneakyThrows
    public String setLoginStatus(String accessToken, boolean login, String username, String password) {
        if (login) {
            logger.log("loginStatus-> should be logged in");
            Unm<T> unm = new Unm<>(session);
            accessToken = unm.getAccessToken(username, password, "B2C");
            logger.log("accessToken-> " + accessToken);
        } else {
            logger.log("loginStatus-> should be not logged in");
        }
        return accessToken;
    }

    // ---------- Unified hotel price API (enum-based) ----------
    /*
     * USAGE EXAMPLES (ABT = Before Tax, AAT = After Tax):
     *
     * 1) SRP/PDP/SO Room List – AAT, /room/night: crossed = baseRateWithTax, final = rateWithTax
     *    getSrpHotelPrice(true, HotelPriceKind.CROSSED, HotelPriceScope.PER_ROOM_NIGHT, searchHotelResponse);
     *    getSrpHotelPrice(true, HotelPriceKind.FINAL,   HotelPriceScope.PER_ROOM_NIGHT, searchHotelResponse);
     *    getHotelPrice(true, HotelPriceKind.CROSSED, HotelPriceScope.PER_ROOM_NIGHT, selectRoomResponse);
     *    getHotelPrice(true, HotelPriceKind.FINAL,   HotelPriceScope.PER_ROOM_NIGHT, selectRoomResponse);
     *
     * 2) LP/SRP/PDP/SO Room List – ABT, Total Price: crossed = totalBaseRateWithoutTax, final = totalRateWithoutTax
     *    getSrpHotelPrice(false, HotelPriceKind.CROSSED, HotelPriceScope.TOTAL, searchHotelResponse);
     *    getSrpHotelPrice(false, HotelPriceKind.FINAL,   HotelPriceScope.TOTAL, searchHotelResponse);
     *    getSrpHotelPrice(false, HotelPriceKind.AFTER_TAX, HotelPriceScope.TOTAL, searchHotelResponse); // totalRateWithoutTax
     *
     * 3) MO Room List – always per room/night (even with Total Price filter)
     *    getHotelPrice(isTax, HotelPriceKind.CROSSED, HotelPriceScope.PER_ROOM_NIGHT, selectRoomResponse);
     *    getHotelPrice(isTax, HotelPriceKind.FINAL,   HotelPriceScope.PER_ROOM_NIGHT, selectRoomResponse);
     *
     * 4) Booking Form price footer – always total, always with tax
     *    getHotelPrice(true, HotelPriceKind.CROSSED, HotelPriceScope.TOTAL, selectRoomResponse);
     *    getHotelPrice(true, HotelPriceKind.FINAL,   HotelPriceScope.TOTAL, selectRoomResponse);
     *
     * 5) After-tax line on SRP/PDP/Room List (e.g. ABT + /room/night): rateWithTax
     *    getSrpHotelPrice(false, HotelPriceKind.AFTER_TAX, HotelPriceScope.PER_ROOM_NIGHT, searchHotelResponse);
     *    getHotelPrice(false, HotelPriceKind.AFTER_TAX, HotelPriceScope.PER_ROOM_NIGHT, selectRoomResponse);
     */

    /**
     * Returns the price value from SRP (SearchHotelResponse) for the given kind, scope, and tax.
     *
     * @param isTax   true = AAT (after tax), false = ABT (before tax)
     * @param kind    CROSSED, FINAL, or AFTER_TAX
     * @param scope   PER_ROOM_NIGHT or TOTAL
     * @param searchHotelResponse SRP response (first content is used)
     * @return the numeric price
     */
    @SneakyThrows
    public double getSrpHotelPrice(boolean isTax, HotelPriceKind kind, HotelPriceScope scope,
                                   SearchHotelResponse searchHotelResponse) {
        SearchHotelResponse.Content content = searchHotelResponse.data.contents.get(0);
        SearchHotelResponse.Price p = content.rateInfo.price;
        logger.log("getSrpHotelPrice: isTax=" + isTax + ", kind=" + kind + ", scope=" + scope + " -> " + content.hotelName);
        logSrpPriceFields(p);
        double price = selectPrice(isTax, kind, scope, p);
        logger.log(" -> " + price);
        return price;
    }

    /**
     * Returns the price value from room list / PDP / booking (SelectRoomResponse) for the given kind, scope, and tax.
     *
     * @param isTax   true = AAT (after tax), false = ABT (before tax)
     * @param kind    CROSSED, FINAL, or AFTER_TAX
     * @param scope   PER_ROOM_NIGHT or TOTAL (use TOTAL for Booking Form footer)
     * @param selectRoomResponse room/rate plan response (first room, first rate plan used)
     * @return the numeric price
     */
    @SneakyThrows
    public double getHotelPrice(boolean isTax, HotelPriceKind kind, HotelPriceScope scope,
                                SelectRoomResponse selectRoomResponse) {
        SelectRoomResponse.Price p = selectRoomResponse.data.rooms.get(0).ratePlans.get(0).rateInfo.price;
        logger.log("getHotelPrice: isTax=" + isTax + ", kind=" + kind + ", scope=" + scope);
        logRoomPriceFields(p);
        double price = selectPrice(isTax, kind, scope, p);
        logger.log(" -> " + price);
        return price;
    }

    private double selectPrice(boolean isTax, HotelPriceKind kind, HotelPriceScope scope,
                               SearchHotelResponse.Price p) {
        boolean useTotal = (scope == HotelPriceScope.TOTAL);
        switch (kind) {
            case CROSSED:
                return isTax
                        ? (useTotal ? p.totalBaseRateWithTax : p.baseRateWithTax)
                        : (useTotal ? p.totalBaseRateWithoutTax : p.baseRateWithoutTax);
            case FINAL:
                return isTax
                        ? (useTotal ? p.totalRateWithTax : p.rateWithTax)
                        : (useTotal ? p.totalRateWithoutTax : p.rateWithoutTax);
            case AFTER_TAX:
                if (useTotal) return isTax ? p.totalRateWithTax : p.totalRateWithoutTax;
                return p.rateWithTax;
            default:
                throw new IllegalArgumentException("HotelPriceKind: " + kind);
        }
    }

    private double selectPrice(boolean isTax, HotelPriceKind kind, HotelPriceScope scope,
                               SelectRoomResponse.Price p) {
        boolean useTotal = (scope == HotelPriceScope.TOTAL);
        switch (kind) {
            case CROSSED:
                return isTax
                        ? (useTotal ? p.totalBaseRateWithTax : p.baseRateWithTax)
                        : (useTotal ? p.totalBaseRateWithoutTax : p.baseRateWithoutTax);
            case FINAL:
                return isTax
                        ? (useTotal ? p.totalRateWithTax : p.rateWithTax)
                        : (useTotal ? p.totalRateWithoutTax : p.rateWithoutTax);
            case AFTER_TAX:
                if (useTotal) return isTax ? p.totalRateWithTax : p.totalRateWithoutTax;
                return p.rateWithTax;
            default:
                throw new IllegalArgumentException("HotelPriceKind: " + kind);
        }
    }

    private void logSrpPriceFields(SearchHotelResponse.Price p) {
        logger.log("\"price\": { "
                + "\"baseRateWithTax\": " + p.baseRateWithTax + ", "
                + "\"rateWithTax\": " + p.rateWithTax + ", "
                + "\"totalBaseRateWithTax\": " + p.totalBaseRateWithTax + ", "
                + "\"totalRateWithTax\": " + p.totalRateWithTax + ", "
                + "\"baseRateWithoutTax\": " + p.baseRateWithoutTax + ", "
                + "\"rateWithoutTax\": " + p.rateWithoutTax + ", "
                + "\"totalRateWithoutTax\": " + p.totalRateWithoutTax + ", "
                + "\"totalBaseRateWithoutTax\": " + p.totalBaseRateWithoutTax
                + " }");
    }

    private void logRoomPriceFields(SelectRoomResponse.Price p) {
        logger.log("\"price\": { "
                + "\"baseRateWithTax\": " + p.baseRateWithTax + ", "
                + "\"rateWithTax\": " + p.rateWithTax + ", "
                + "\"totalBaseRateWithTax\": " + p.totalBaseRateWithTax + ", "
                + "\"totalRateWithTax\": " + p.totalRateWithTax + ", "
                + "\"baseRateWithoutTax\": " + p.baseRateWithoutTax + ", "
                + "\"rateWithoutTax\": " + p.rateWithoutTax + ", "
                + "\"totalRateWithoutTax\": " + p.totalRateWithoutTax + ", "
                + "\"totalBaseRateWithoutTax\": " + p.totalBaseRateWithoutTax
                + " }");
    }

    // ---------- Legacy methods (delegate to enum-based API) ----------

    @SneakyThrows
    public double getSrpHotelTotalPriceWithTax(boolean isTax, SearchHotelResponse searchHotelResponse) {
        return getSrpHotelPrice(isTax, HotelPriceKind.FINAL, HotelPriceScope.TOTAL, searchHotelResponse);
    }

    @SneakyThrows
    public double getSrpHotelPriceWithTax(boolean isTax, SearchHotelResponse searchHotelResponse) {
        return getSrpHotelPrice(isTax, HotelPriceKind.FINAL, HotelPriceScope.PER_ROOM_NIGHT, searchHotelResponse);
    }

    public int findSrpHotelContentIndex(SearchHotelResponse searchHotelResponse, String hotelName) {
        String normalizedQuery = normalizeHotelNameForLookup(hotelName);
        for (int i = 0; i < searchHotelResponse.data.contents.size(); i++) {
            String contentName = searchHotelResponse.data.contents.get(i).hotelName;
            String normalizedContentName = normalizeHotelNameForLookup(contentName);
            if (normalizedContentName.equalsIgnoreCase(normalizedQuery)) {
                Assertion.assertThat(
                        "SRP hotel name should match lookup query",
                        normalizedContentName,
                        equalToIgnoringCase(normalizedQuery));
                return i;
            }
        }
        List<String> actualHotelNames = new ArrayList<>();
        for (SearchHotelResponse.Content content : searchHotelResponse.data.contents) {
            actualHotelNames.add(normalizeHotelNameForLookup(content.hotelName));
        }
        Assertion.assertThat(
                "Hotel '" + hotelName + "' not found in SRP search response",
                actualHotelNames,
                hasItem(equalToIgnoringCase(normalizedQuery)));
        return -1;
    }

    private String normalizeHotelNameForLookup(String hotelName) {
        return normalizeNonBreakingSpaceText(hotelName).replace("_", "");
    }

    @SneakyThrows
    public double getSrpHotelTotalPriceWithTax(boolean isTax, SearchHotelResponse searchHotelResponse, String hotelName) {
        int index = findSrpHotelContentIndex(searchHotelResponse, hotelName);
        SearchHotelResponse.Content content = searchHotelResponse.data.contents.get(index);
        SearchHotelResponse.Price price = content.rateInfo.price;
        logger.log("getSrpHotelTotalPriceWithTax: hotel=" + hotelName + ", isTax=" + isTax + ", scope=TOTAL");
        logSrpPriceFields(price);
        double amount = isTax ? price.totalRateWithTax : price.totalRateWithoutTax;
        logger.log(" -> " + amount);
        return amount;
    }

    @SneakyThrows
    public double getSrpHotelPriceWithTax(boolean isTax, SearchHotelResponse searchHotelResponse, String hotelName) {
        int index = findSrpHotelContentIndex(searchHotelResponse, hotelName);
        SearchHotelResponse.Content content = searchHotelResponse.data.contents.get(index);
        SearchHotelResponse.Price price = content.rateInfo.price;
        logger.log("getSrpHotelPriceWithTax: hotel=" + hotelName + ", isTax=" + isTax + ", scope=PER_ROOM_NIGHT");
        logSrpPriceFields(price);
        double amount = isTax ? price.rateWithTax : price.rateWithoutTax;
        logger.log(" -> " + amount);
        return amount;
    }

    @SneakyThrows
    public double getSrpHotelCrossedTotalPriceWithTax(boolean isTax, SearchHotelResponse searchHotelResponse) {
        return getSrpHotelPrice(isTax, HotelPriceKind.CROSSED, HotelPriceScope.TOTAL, searchHotelResponse);
    }

    @SneakyThrows
    public double getSrpHotelCrossedPriceWithTax(boolean isTax, SearchHotelResponse searchHotelResponse) {
        return getSrpHotelPrice(isTax, HotelPriceKind.CROSSED, HotelPriceScope.PER_ROOM_NIGHT, searchHotelResponse);
    }

    @SneakyThrows
    public SelectRoomResponse getHotelRoomData(String hotelName, String username, String password, int night, int room, int adult, String startDate, String currency) {
        String authorization = getToken(generateDeviceId());
        logger.log("authorization-> " + authorization);

        if(!username.isEmpty()){
            authorization = setLoginStatus(authorization, true, username, password);
        }

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, username, hotelName, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        int selectedIndex = findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(authorization, username, adult, hotelId, night, room, startDate,"id", currency);
        SelectRoomApi selectRoomApi = new SelectRoomApi(selectRoomApiInputs);
        ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
    }

    @SneakyThrows
    public SelectRoomResponse getHotelRoomDataV2(String hotelName, String username, String password, int night, int room, int adult, String startDate, String currency) {
        String authorization = getToken(generateDeviceId());
        logger.log("authorization-> " + authorization);

        if (!username.isEmpty()) {
            authorization = setLoginStatus(authorization, true, username, password);
        }

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, "", hotelName, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(authorization, username, adult, hotelId, night, room, startDate, "id", currency);
        SelectRoomApi selectRoomApi = new SelectRoomApi(selectRoomApiInputs);
        ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
        Gson gson = new Gson();

        logger.log(selectRoomApiResponse.getBody());
        return gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
    }

    @SneakyThrows
    public double getHotelTotalPriceWithTax(boolean isTax, SelectRoomResponse selectRoomResponse) {
        return getHotelPrice(isTax, HotelPriceKind.FINAL, HotelPriceScope.TOTAL, selectRoomResponse);
    }

    @SneakyThrows
    public double getHotelTotalCrossedPriceWithTax(boolean isTax, SelectRoomResponse selectRoomResponse) {
        return getHotelPrice(isTax, HotelPriceKind.CROSSED, HotelPriceScope.TOTAL, selectRoomResponse);
    }

    /**
     * This method is used to get a token.
     *
     * @return String Returns the access token.
     * @throws Exception
     */
    private String getToken(String deviceId) throws Exception {
        PayExpressApiWithInputs.Inputs inputs = new PayExpressApiWithInputs.Inputs(deviceId);
        PayExpressApiWithInputs payExpressApi = new PayExpressApiWithInputs(inputs);
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
    }

    public int getOrderId() {
        logger.log("orderId-> " + orderId);
        return orderId;
    }

    private SelectRoomResponse.RatePlan selectRoomMostExpansive(SelectRoomApi.Inputs inputs) throws InterruptedException {
        Gson gson = new Gson();
        for(int attempt = 0; attempt<=3; attempt++){
            SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
            ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
            logger.log("Select Room Response : "+selectRoomApiResponse.getBody());
            SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
            if(!selectRoomResponse.data.rooms.isEmpty()){
                if (selectRoomResponse.data.rooms.size() == 1) {
                    logger.softWarning("Only one room available, can't get the expansive room");
                    return selectRoomResponse.data.rooms.get(0).ratePlans.stream().findFirst().orElseThrow();
                }
                return selectRoomResponse.data.rooms.stream()
                        .flatMap(roomList -> roomList.ratePlans.stream())
                        .filter(room -> room.availableRoom >= inputs.room() && room.cancellationPolicies.policies != null)
                        .max(Comparator.comparingDouble(room -> room.rateInfo.price.totalRateWithTax))
                        .orElseThrow();
            }
            Thread.sleep(10000);
        }
        throw new RuntimeException("Hotel Room not found");
    }

    private PreBookGeneratingCartResponse prebookGetData(PreBookGeneratingCartApi.Inputs inputs) {
        PreBookGeneratingCartApi preBookGeneratingCartApi = new PreBookGeneratingCartApi(inputs);
        ApiResponse preBookCartApiResponse = preBookGeneratingCartApi.hitApi(apiTools);
        Gson gson = new Gson();
        PreBookGeneratingCartResponse generatingCartResponse = gson.fromJson(preBookCartApiResponse.getBody(),PreBookGeneratingCartResponse.class);
        String cartId = generatingCartResponse.data.hotels.get(0).detail.rooms.get(0).cartId;
        logger.log("cart id: " + cartId);
        return generatingCartResponse;
    }

    private PostInterimDetailsApiHotel getBodyPostInterimDetailsApiHotel(PreBookGeneratingCartResponse preBookGeneratingCartResponse,int night, String notes){
        PostInterimDetailsApiHotel postInterimDetailsApiHotel = new PostInterimDetailsApiHotel();
        postInterimDetailsApiHotel.notes = notes;
        postInterimDetailsApiHotel.productType = "hotel";
        postInterimDetailsApiHotel.verticalCartID = preBookGeneratingCartResponse.data.hotels.get(0).detail.rooms.get(0).cartId;
        postInterimDetailsApiHotel.bookingCriteria = new PostInterimDetailsApiHotel.BookingCriteria();
        postInterimDetailsApiHotel.bookingCriteria.hotel = new PostInterimDetailsApiHotel.Hotel();
        postInterimDetailsApiHotel.bookingCriteria.hotel.cartId = preBookGeneratingCartResponse.data.hotels.get(0).detail.rooms.get(0).cartId;
        postInterimDetailsApiHotel.bookingCriteria.hotel.areaId = preBookGeneratingCartResponse.data.hotels.get(0).detail.area.id;
        postInterimDetailsApiHotel.bookingCriteria.hotel.cityId = preBookGeneratingCartResponse.data.hotels.get(0).detail.city.id;
        postInterimDetailsApiHotel.bookingCriteria.hotel.regionId = preBookGeneratingCartResponse.data.hotels.get(0).detail.region.id;
        postInterimDetailsApiHotel.bookingCriteria.hotel.countryId = preBookGeneratingCartResponse.data.hotels.get(0).detail.country.id;
        postInterimDetailsApiHotel.bookingCriteria.hotel.publicId = preBookGeneratingCartResponse.data.hotels.get(0).detail.hotelId;
        postInterimDetailsApiHotel.bookingCriteria.hotel.nights = Integer.toString(night);
        postInterimDetailsApiHotel.bookingCriteria.hotel.startDate = preBookGeneratingCartResponse.data.hotels.get(0).startDate;
        postInterimDetailsApiHotel.bookingCriteria.hotel.endDate = preBookGeneratingCartResponse.data.hotels.get(0).endDate;
        return postInterimDetailsApiHotel;
    }

    private PostInterimDetailsApiResponse postInterimDetail(PostInterimDetailsApi.Inputs inputs) {
        PostInterimDetailsApi postInterimDetailsApi = new PostInterimDetailsApi(inputs);
        ApiResponse postInterimDetailsApiResponse = postInterimDetailsApi.hitApi(apiTools);
        Gson gson = new Gson();
        PostInterimDetailsApiResponse postInterimDetailsApiResponse1 = gson.fromJson(postInterimDetailsApiResponse.getBody(),PostInterimDetailsApiResponse.class);
        logger.log("postInterimDetailsApiResponse1 -> " + postInterimDetailsApiResponse1.code);
        return postInterimDetailsApiResponse1;
    }

    private PostInterimTransactionsApiBodyHotel getBodyPostInterimTransactionsApiBodyHotel(PostInterimDetailsApiHotel postInterimDetailsApiHotel, String notes){
        PostInterimTransactionsApiBodyHotel postInterimTransactionsApiBodyHotel = new PostInterimTransactionsApiBodyHotel();
        postInterimTransactionsApiBodyHotel.notes = notes;
        postInterimTransactionsApiBodyHotel.productType = postInterimDetailsApiHotel.productType;
        postInterimTransactionsApiBodyHotel.travelPlanDetailID = "0";
        postInterimTransactionsApiBodyHotel.verticalCartID = postInterimDetailsApiHotel.verticalCartID;
        postInterimTransactionsApiBodyHotel.bookingCriteria = new PostInterimTransactionsApiBodyHotel.BookingCriteria();
        postInterimTransactionsApiBodyHotel.bookingCriteria.hotel = new PostInterimTransactionsApiBodyHotel.Hotel();
        postInterimTransactionsApiBodyHotel.bookingCriteria.hotel.cartId = postInterimDetailsApiHotel.bookingCriteria.hotel.cartId;
        postInterimTransactionsApiBodyHotel.bookingCriteria.hotel.areaId = postInterimDetailsApiHotel.bookingCriteria.hotel.areaId;
        postInterimTransactionsApiBodyHotel.bookingCriteria.hotel.cityId = postInterimDetailsApiHotel.bookingCriteria.hotel.cityId;
        postInterimTransactionsApiBodyHotel.bookingCriteria.hotel.regionId = postInterimDetailsApiHotel.bookingCriteria.hotel.regionId;
        postInterimTransactionsApiBodyHotel.bookingCriteria.hotel.countryId = postInterimDetailsApiHotel.bookingCriteria.hotel.countryId;
        postInterimTransactionsApiBodyHotel.bookingCriteria.hotel.publicId = postInterimDetailsApiHotel.bookingCriteria.hotel.publicId;
        postInterimTransactionsApiBodyHotel.bookingCriteria.hotel.nights = postInterimDetailsApiHotel.bookingCriteria.hotel.nights;
        postInterimTransactionsApiBodyHotel.bookingCriteria.hotel.startDate = postInterimDetailsApiHotel.bookingCriteria.hotel.startDate;
        postInterimTransactionsApiBodyHotel.bookingCriteria.hotel.endDate = postInterimDetailsApiHotel.bookingCriteria.hotel.endDate;

        return postInterimTransactionsApiBodyHotel;
    }

    private SubmitBookingV2Response submitBookingV2B2B(SubmitBookingApiV2B2B.Inputs inputs) {
        SubmitBookingApiV2B2B submitBookingApi = new SubmitBookingApiV2B2B(inputs);
        ApiResponse submitBookingApiResponse = submitBookingApi.hitApi(apiTools);
        logger.log("Hotel response : " + submitBookingApiResponse.getBody());
        Gson gson = new Gson();
        return gson.fromJson(submitBookingApiResponse.getBody(), SubmitBookingV2Response.class);
    }

    private String postInterimTransactions(PostInterimTransactionsApi.Inputs inputs) {
        PostInterimTransactionsApi postInterimTransactionsApi = new PostInterimTransactionsApi(inputs);
        ApiResponse apiResponse = postInterimTransactionsApi.hitApi(apiTools);
        JSONObject object = new JSONObject(apiResponse.getBody());
        logger.log("res-> " + object);
        return object.getJSONObject("data").getString("corporateInterimID");
    }

    private String submitPaymentB2B(SubmitPaymentApiB2B.Inputs inputs) {
        SubmitPaymentApiB2B submitPaymentApi = new SubmitPaymentApiB2B(inputs);
        ApiResponse apiResponse = submitPaymentApi.hitApi(apiTools);

        JSONObject response = new JSONObject(apiResponse.getBody());
        logger.log("Payment api response -> " +response);
        return response.getString("code");
    }

    private String submitPaymentB2BPUT(SubmitPaymentPutApiB2B.Inputs inputs) {
        SubmitPaymentPutApiB2B submitPaymentApi = new SubmitPaymentPutApiB2B(inputs);
        ApiResponse apiResponse = submitPaymentApi.hitApi(apiTools);

        JSONObject response = new JSONObject(apiResponse.getBody());
        logger.log("Payment api response -> " +response);
        return response.getString("code");
    }

    @SneakyThrows
    public String createIssuedOrderApiB2B(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            String currency,
            String guestFullName,
            String guestTitle,
            EmployeeData employeeData,
            boolean selectRoomMostExpansive,
            String notes) {

        Unm<?> unm = new Unm<>(session);
        MemberApi<?> memberApi = new MemberApi<>(session);
        String accessToken = unm.getAccessToken(username,password,"B2B");
        String auth = "Bearer " + accessToken;

        AccountData accountData = memberApi.getAccountData(accessToken);
        logger.log("accessToken for dashboard request->" + auth);

        logger.log("query " + query);
        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(auth, username, query, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        String hotelId=null;
        hotelName=null;
        for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
            if (query.toLowerCase().contains(content.name.toLowerCase())) {
                hotelId = content.publicId;
                hotelName = content.name;
                break;
            }
        }

        logger.log("hotelId-> " + hotelId);
        logger.log("hotel name->" + hotelName);
        logger.log("Date: "+startDate);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(auth, username, adult, hotelId, night, room, startDate,"id", "");
        SelectRoomResponse.RatePlan selectedRoom;
        if (selectRoomMostExpansive) {
            selectedRoom = selectRoomMostExpansive(selectRoomApiInputs);
        } else {
            selectedRoom = selectRoom(selectRoomApiInputs, true, true);
        }
        hotelRoomName = selectRoomName(selectRoomApiInputs, true, true);
        int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
        int maxChildOccupancy = selectedRoom.maxChildOccupancy;
        logger.log("hotelRoomName-> " + hotelRoomName);

        PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(auth, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
        PreBookGeneratingCartResponse preBookGeneratingCartResponse = prebookGetData(preBookCartApiInputs);
        String cartID = preBookGeneratingCartResponse.data.hotels.get(0).detail.rooms.get(0).cartId;
        logger.log("cartID-> " + cartID);

        PostInterimDetailsApiHotel postInterimDetailsApiHotel = getBodyPostInterimDetailsApiHotel(preBookGeneratingCartResponse,night, notes);
        PostInterimDetailsApi.Inputs postInterimDetailsApi = new PostInterimDetailsApi.Inputs(auth,postInterimDetailsApiHotel,null);
        PostInterimDetailsApiResponse postInterimDetailsApiResponse = postInterimDetail(postInterimDetailsApi);

        PostInterimTransactionsApiBodyHotel postInterimTransactionsApiBodyHotel = getBodyPostInterimTransactionsApiBodyHotel(postInterimDetailsApiHotel, notes);
        PostInterimTransactionsApi.Inputs postInterimTransactionsApiInputs = new PostInterimTransactionsApi.Inputs(auth,postInterimTransactionsApiBodyHotel,null);
        String corporateInterimID = postInterimTransactions(postInterimTransactionsApiInputs);
        logger.log("corporateInterimID-> " + corporateInterimID);

        SubmitBookingApiV2B2B.Inputs submitBookingApiInputs = new SubmitBookingApiV2B2B.Inputs(accessToken,currency,accountData,guestFullName,guestTitle,hotelId,cartID,corporateInterimID,postInterimDetailsApiResponse);
        SubmitBookingV2Response submitBookingResponse = submitBookingV2B2B(submitBookingApiInputs);
        guestNames = submitBookingApiInputs.getGuestNames();
        orderId = submitBookingResponse.data.orderId;
        logger.log("orderId-> " + orderId);

        SubmitPaymentApiB2B.Inputs submitPaymentApiInputs = new SubmitPaymentApiB2B.Inputs(auth, submitBookingResponse.data.orderHash, orderId, employeeData.data.employeeID,employeeData.data.corporateID);
        String paymentResponse = submitPaymentB2B(submitPaymentApiInputs);
        logger.log("paymentStatus"+ paymentResponse);

        SubmitPaymentPutApiB2B.Inputs submitPaymentPutApiInputs = new SubmitPaymentPutApiB2B.Inputs(auth,submitBookingResponse.data.orderHash, orderId, employeeData.data.employeeID,employeeData.data.corporateID, "");
        String paymentResponsePut = submitPaymentB2BPUT(submitPaymentPutApiInputs);
        logger.log("paymentStatus"+ paymentResponsePut);

        if(paymentResponsePut.contains("CORPORATE_BILLING_ERROR")){
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
        return String.valueOf(orderId);
    }

    public int findHotelIndex(HotelAutoCompleteResponse hotelAutoCompleteResponse, String query) {
        int hotelIndex = 0;
        int totalContents = hotelAutoCompleteResponse.data.contents.size();
        for (int i = 0; i < totalContents; i++) {
            String contentName = hotelAutoCompleteResponse.data.contents.get(i).name;
            logger.log("contentName-> " + contentName);
            if (query.equalsIgnoreCase(contentName)) {
                hotelIndex = i;
                logger.log("hotelIndex-> " + hotelIndex);
                break;
            }
        }
        return hotelIndex;
    }

    public int findHotelIndex(HotelAutoCompleteResponse hotelAutoCompleteResponse, String[] queries) {
        int hotelIndex = 0;
        int totalContents = hotelAutoCompleteResponse.data.contents.size();
        for (int i = 0; i < totalContents; i++) {
            String contentName = hotelAutoCompleteResponse.data.contents.get(i).name;
            logger.log("contentName-> " + contentName);
            if (Arrays.stream(queries).anyMatch(query -> query.trim().equalsIgnoreCase(contentName))) {
                hotelIndex = i;
                logger.log("hotelIndex-> " + hotelIndex);
                break;
            }
        }
        return hotelIndex;
    }

    public int findHotelIndexPublicId(HotelAutoCompleteResponse hotelAutoCompleteResponse, String publicId) {
        int hotelIndex = 0;
        int totalContents = hotelAutoCompleteResponse.data.contents.size();
        for (int i = 0; i < totalContents; i++) {
            String contentPublicId = hotelAutoCompleteResponse.data.contents.get(i).publicId;
            logger.log("contentPublicId-> " + contentPublicId);
            if (publicId.equalsIgnoreCase(contentPublicId)) {
                hotelIndex = i;
                logger.log("hotelIndex-> " + hotelIndex);
                break;
            }
        }
        return hotelIndex;
    }

    @SneakyThrows
    public String createWaitingPaymentOrderApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            String currency,
            int addonsAmount,
            String accommodationTypes,
            String language
    ) throws ExecutionException, InterruptedException {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        logger.log("accessToken-> " + accessToken);

        MemberApi<T> memberApi = new MemberApi<>(session);
        AccountData.Data accountData = memberApi.getAccountDataByEmail(username);
        String unmUserId = String.valueOf(accountData.unmUserId);
        logger.log("unmUserId-> " + unmUserId);

        if (accommodationTypes.equalsIgnoreCase("homes")) accommodationTypes = "NHA";
        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, accommodationTypes.toUpperCase());
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        int selectedIndex = findHotelIndex(hotelAutoCompleteResponse, query);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate, language, currency);
        boolean isNative = nativeHotelColor != null;
        SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs, true, isNative);
        hotelRoomName = selectRoomName(selectRoomApiInputs, true, isNative);
        int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
        int maxChildOccupancy = selectedRoom.maxChildOccupancy;
        logger.log("hotelRoomName-> " + hotelRoomName);

        PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
        PreBookGeneratingCartResponse preBookGeneratingCartResponse = prebookGetData(preBookCartApiInputs);
        String cartID = preBookGeneratingCartResponse.data.hotels.get(0).detail.rooms.get(0).cartId;
        logger.log("cartID-> " + cartID);
        String addonsId;
        if (addonsAmount > 0) {
            logger.log("addonsAmount-> " + addonsAmount);
            addonsId = String.valueOf(preBookGeneratingCartResponse.data.hotels.get(0).addOnCategoryGroups.get(0).addOns.get(0).id);
            addonsName = preBookGeneratingCartResponse.data.hotels.get(0).addOnCategoryGroups.get(0).addOns.get(0).name;
            logger.log("addonsId-> " + addonsId);
            logger.log("addonsName-> " + addonsName);
        } else {
            logger.log("No need addons");
            addonsId = "0";
            startDate = "0";
        }

        SubmitBookingApiV2.Inputs submitBookingApiV2Inputs = new SubmitBookingApiV2.Inputs(accessToken, unmUserId, currency, username, hotelId, cartID, addonsAmount, addonsId, startDate);
        SubmitBookingV2Response submitBookingV2Response = submitBookingV2(submitBookingApiV2Inputs);
        orderId = submitBookingV2Response.data.orderId;
        orderHash = submitBookingV2Response.data.orderHash;
        logger.log("orderId-> " + orderId);
        logger.log("orderHash-> " + orderHash);
        return String.valueOf(orderId);
    }

    public String createWaitingPaymentOrderApiByHotelId(
            String username,
            String password,
            String startDate,
            String hotelId,
            int adult,
            int night,
            int room,
            String currency,
            int addonsAmount,
            String language
    ) throws ExecutionException, InterruptedException {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        logger.log("accessToken-> " + accessToken);

        MemberApi<T> memberApi = new MemberApi<>(session);
        AccountData.Data accountData = memberApi.getAccountDataByEmail(username);
        String unmUserId = String.valueOf(accountData.unmUserId);
        logger.log("unmUserId-> " + unmUserId);
        logger.log("hotelId (static)-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate, language, currency);
        boolean isNative = nativeHotelColor != null;
        SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs, true, isNative);
        hotelRoomName = selectRoomName(selectRoomApiInputs, true, isNative);
        int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
        int maxChildOccupancy = selectedRoom.maxChildOccupancy;
        logger.log("hotelRoomName-> " + hotelRoomName);

        SubmitBookingV2Response submitBookingV2Response = null;
        RuntimeException lastError = null;
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                logger.log("submitBookingV2 attempt " + attempt);
                PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate, maxRoomOccupancy, maxChildOccupancy, currency);
                PreBookGeneratingCartResponse preBookGeneratingCartResponse = prebookGetData(preBookCartApiInputs);
                String cartID = preBookGeneratingCartResponse.data.hotels.get(0).detail.rooms.get(0).cartId;
                logger.log("cartID-> " + cartID);
                String addonsId;
                String effectiveStartDate = startDate;
                if (addonsAmount > 0) {
                    logger.log("addonsAmount-> " + addonsAmount);
                    addonsId = String.valueOf(preBookGeneratingCartResponse.data.hotels.get(0).addOnCategoryGroups.get(0).addOns.get(0).id);
                    addonsName = preBookGeneratingCartResponse.data.hotels.get(0).addOnCategoryGroups.get(0).addOns.get(0).name;
                    logger.log("addonsId-> " + addonsId);
                    logger.log("addonsName-> " + addonsName);
                } else {
                    logger.log("No need addons");
                    addonsId = "0";
                    effectiveStartDate = "0";
                }
                SubmitBookingApiV2.Inputs submitBookingApiV2Inputs = new SubmitBookingApiV2.Inputs(accessToken, unmUserId, currency, username, hotelId, cartID, addonsAmount, addonsId, effectiveStartDate);
                submitBookingV2Response = submitBookingV2(submitBookingApiV2Inputs);
                lastError = null;
                break;
            } catch (RuntimeException e) {
                lastError = e;
                logger.log("submitBookingV2 attempt " + attempt + " failed: " + e.getMessage() + " — retrying...");
                Thread.sleep(3000);
            }
        }
        if (lastError != null) throw lastError;
        if (submitBookingV2Response == null) throw new RuntimeException("submitBookingV2Response is null after retry");

        orderId = submitBookingV2Response.data.orderId;
        orderHash = submitBookingV2Response.data.orderHash;
        logger.log("orderId-> " + orderId);
        logger.log("orderHash-> " + orderHash);
        return String.valueOf(orderId);
    }

    public String getOrderHash() {
        logger.log("orderHash-> " + orderHash);
        return orderHash;
    }

    /**
     * Creates a fully ISSUED hotel order with optional add-ons and/or special-request,
     * for either a single-room or multi-room booking. The {@code isMultiRoom} flag
     * selects the booking shape; when {@code true} the flow uses the multi-room
     * prebook + multi-room submit-book endpoint, and the same add-on / special-request
     * inputs are applied to every book entry.
     *
     * <p>The cheapest available room (under the framework price cap for multi-room)
     * is picked automatically. After {@code submitBooking*}, the order is paid via
     * Sakuku and {@code paidCheckerStatus} is polled until {@code SUCCESS}, so the
     * returned order is in the issued/paid state and the e-ticket should be
     * available shortly after.</p>
     *
     * <p>Pass {@code addonsAmount = 0} to skip add-ons. Pass
     * {@code isSpecialRequestEnable = false} to skip the special-request line.
     * Both can be combined in a single call.</p>
     *
     * @param qtyEachRoom guests per room; only used when {@code isMultiRoom == true}
     * @param addonsAmount quantity of the first available add-on; {@code 0} disables add-ons
     * @param accommodationTypes "HOTEL" or "homes" (auto-mapped to "NHA")
     * @param isMultiRoom {@code true} for multi-room V2 flow, {@code false} for single-room V2 flow
     * @return the created {@code orderId} as a String (after payment is confirmed)
     */
    @SneakyThrows
    public String createIssuedOrderWithExtrasApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            int qtyEachRoom,
            String currency,
            int addonsAmount,
            boolean isSpecialRequestEnable,
            String prefixSpecialRequest,
            String accommodationTypes,
            String language,
            boolean isMultiRoom
    ) throws ExecutionException, InterruptedException {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        logger.log("accessToken-> " + accessToken);

        MemberApi<T> memberApi = new MemberApi<>(session);
        AccountData.Data accountData = memberApi.getAccountDataByEmail(username);
        String unmUserId = String.valueOf(accountData.unmUserId);
        logger.log("unmUserId-> " + unmUserId);

        String normalizedAccommodationTypes = accommodationTypes;
        if (normalizedAccommodationTypes.equalsIgnoreCase("homes")) normalizedAccommodationTypes = "NHA";

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(
                accessToken, username, query, normalizedAccommodationTypes.toUpperCase());
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        int selectedIndex = findHotelIndex(hotelAutoCompleteResponse, query);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        logger.log("hotelId-> " + hotelId);
        logger.log("isMultiRoom-> " + isMultiRoom);
        logger.log("addonsAmount-> " + addonsAmount);
        logger.log("isSpecialRequestEnable-> " + isSpecialRequestEnable);

        if (!isMultiRoom) {
            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(
                    accessToken, username, adult, hotelId, night, room, startDate, language, currency);
            boolean isNative = nativeHotelColor != null;
            SelectRoomResponse.RatePlan selectedRoom = selectRoom(
                    selectRoomApiInputs, true, isNative, RoomSelectionStrategy.CHEAPEST);
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("Selected cheapest room | totalRateWithTax=" + (selectedRoom.rateInfo != null && selectedRoom.rateInfo.price != null
                    ? selectedRoom.rateInfo.price.totalRateWithTax : "n/a"));

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(
                    accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room,
                    selectedRoom.roomId, startDate, maxRoomOccupancy, maxChildOccupancy, currency);
            PreBookGeneratingCartResponse preBookGeneratingCartResponse = prebookGetData(preBookCartApiInputs);
            String cartID = preBookGeneratingCartResponse.data.hotels.get(0).detail.rooms.get(0).cartId;
            hotelRoomName = preBookGeneratingCartResponse.data.hotels.get(0).detail.rooms.get(0).roomName;
            logger.log("cartID-> " + cartID);
            logger.log("hotelRoomName (from prebook)-> " + hotelRoomName);

            String addonsId = "0";
            String addonsCheckInDate = "0";
            if (addonsAmount > 0) {
                addonsId = String.valueOf(preBookGeneratingCartResponse.data.hotels.get(0).addOnCategoryGroups.get(0).addOns.get(0).id);
                addonsName = preBookGeneratingCartResponse.data.hotels.get(0).addOnCategoryGroups.get(0).addOns.get(0).name;
                addonsCheckInDate = startDate;
                logger.log("addonsId-> " + addonsId);
                logger.log("addonsName-> " + addonsName);
            } else {
                logger.log("No add-ons requested");
            }

            SubmitBookingApiV2.Inputs submitBookingApiV2Inputs = new SubmitBookingApiV2.Inputs(
                    accessToken, unmUserId, currency, username, hotelId, cartID,
                    addonsAmount, addonsId, addonsCheckInDate,
                    isSpecialRequestEnable, prefixSpecialRequest);
            SubmitBookingV2Response submitBookingV2Response = submitBookingV2(submitBookingApiV2Inputs);
            orderId = submitBookingV2Response.data.orderId;
            orderHash = submitBookingV2Response.data.orderHash;
            guestNames = submitBookingApiV2Inputs.getGuestNames();
            if (isSpecialRequestEnable) this.orderSpecialRequest = prefixSpecialRequest + cartID;
            logger.log("orderId-> " + orderId);
            logger.log("orderHash-> " + orderHash);
            logger.log("guestNames-> " + guestNames);

            paySakukuAndWaitPaid(accessToken);
            logger.log("Order created and paid (ETICKET_ISSUED). Order ID: " + orderId);
            return String.valueOf(orderId);
        }

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(
                accessToken, username, adult, hotelId, night, room, startDate, language, currency);
        SelectRoomResponse selectRoomResponse = findRoom(selectRoomApiInputs);
        if (selectRoomResponse == null || selectRoomResponse.data == null || selectRoomResponse.data.rooms == null) {
            throw new IllegalStateException("Select room returned no room list for hotelId " + hotelId);
        }

        String[] roomIdArray = new String[room];
        String[] rateCodeArray = new String[room];
        int[] maxOccupancyArray = new int[room];

        logger.log("totalRoomList -> " + selectRoomResponse.data.rooms.size());

        boolean isNative = nativeHotelColor != null;
        List<RoomRatePlanSelection> picks = findCheapestMatchingRatePlans(
                selectRoomResponse, room, true, isNative, MULTI_ROOM_PRICE_CAP);
        if (picks.size() < room) {
            throw new IllegalStateException("Not enough bookable rooms (need " + room + ", found " + picks.size()
                    + ") that have an available unit, "
                    + (isNative ? "come from the native supplier, " : "")
                    + "allow cancellation and cost at most " + MULTI_ROOM_PRICE_CAP);
        }
        for (int i = 0; i < room; i++) {
            SelectRoomResponse.RatePlan chosen = picks.get(i).ratePlan();
            roomIdArray[i] = chosen.roomId;
            rateCodeArray[i] = chosen.rateCode;
            maxOccupancyArray[i] = chosen.maxAdultOccupancy;
            logger.log("Cheapest[" + i + "] -> roomName=" + picks.get(i).room().groupName
                    + ", rateCode=" + chosen.rateCode
                    + ", availableRoom=" + chosen.availableRoom
                    + ", totalRateWithTax=" + chosen.rateInfo.price.totalRateWithTax);
        }

        PreBookMultiRoomApi.Inputs preBookMultiRoomApiInputs = new PreBookMultiRoomApi.Inputs(
                accessToken, adult, hotelId, night, startDate, maxOccupancyArray,
                qtyEachRoom, rateCodeArray, roomIdArray);
        PreBookMultiRoomResponse preBookMultiRoomResponse = preBookMultiRoom(preBookMultiRoomApiInputs);

        roomNames.clear();
        roomPriceSummaryTotals.clear();
        cardIdArray.clear();
        for (int i = 0; i < room; i++) {
            cardIdArray.add(preBookMultiRoomResponse.data.hotels.get(0).detail.rooms.get(i).cartId);
            roomNames.add(preBookMultiRoomResponse.data.hotels.get(0).detail.rooms.get(i).roomName);
            roomPriceSummaryTotals.add(preBookMultiRoomResponse.data.hotels.get(0).detail.rooms.get(i).rateInfo.priceSummary.total);
        }
        logger.log("cardIdArray-> " + cardIdArray);
        logger.log("roomNames-> " + roomNames);

        String addonsId = "0";
        String addonsCheckInDate = "0";
        if (addonsAmount > 0) {
            PreBookMultiRoomResponse.Hotel hotelPrebook = preBookMultiRoomResponse.data.hotels.get(0);
            if (hotelPrebook.addOnCategoryGroups == null
                    || hotelPrebook.addOnCategoryGroups.isEmpty()
                    || hotelPrebook.addOnCategoryGroups.get(0).addOns == null
                    || hotelPrebook.addOnCategoryGroups.get(0).addOns.isEmpty()) {
                throw new IllegalStateException("addonsAmount > 0 but no add-ons available on multi-room prebook response");
            }
            PreBookMultiRoomResponse.AddOnCategoryGroup.AddOn firstAddOn =
                    hotelPrebook.addOnCategoryGroups.get(0).addOns.get(0);
            addonsId = String.valueOf(firstAddOn.id);
            addonsName = firstAddOn.name;
            addonsCheckInDate = startDate;
            logger.log("addonsId-> " + addonsId);
            logger.log("addonsName-> " + addonsName);
        } else {
            logger.log("No add-ons requested");
        }

        SubmitBookingMultiRoomApi.Inputs submitBookingMultiRoomApiInputs = new SubmitBookingMultiRoomApi.Inputs(
                accessToken, currency, username, hotelId, room, qtyEachRoom, cardIdArray,
                isSpecialRequestEnable, prefixSpecialRequest,
                addonsAmount, addonsId, addonsCheckInDate);
        SubmitBookingMultiRoomResponse submitBookingMultiRoomResponse = submitBookingMultiRoom(submitBookingMultiRoomApiInputs);
        orderId = submitBookingMultiRoomResponse.data.orderId;
        orderHash = submitBookingMultiRoomResponse.data.orderHash;
        guestNames = submitBookingMultiRoomApiInputs.getGuestNames();
        if (isSpecialRequestEnable && !cardIdArray.isEmpty()) {
            this.orderSpecialRequest = prefixSpecialRequest + cardIdArray.get(0);
        }
        logger.log("orderId-> " + orderId);
        logger.log("orderHash -> " + orderHash);

        roomNameToGuestNamesMap.clear();
        int guestNameIndex = 0;
        for (int i = 0; i < room; i++) {
            String roomName = roomNames.get(i);
            List<String> guestsInRoom = new ArrayList<>();
            for (int j = 0; j < qtyEachRoom; j++) {
                if (guestNameIndex < guestNames.size()) {
                    guestsInRoom.add(guestNames.get(guestNameIndex));
                    guestNameIndex++;
                }
            }
            // When multiple rooms share the same display name (common when the cheapest pick
            // returns several units of the same room type), disambiguate the map key with a
            // " #N" suffix so each booked room keeps its own guest list. The first occurrence
            // is stored under the raw room name so callers that match on a single room name
            // still work; subsequent collisions become "<roomName> #2", "<roomName> #3", ...
            String key = roomName;
            int suffix = 2;
            while (roomNameToGuestNamesMap.containsKey(key)) {
                key = roomName + " #" + suffix++;
            }
            roomNameToGuestNamesMap.put(key, guestsInRoom);
        }

        paySakukuAndWaitPaid(accessToken);
        logger.log("Multi-room order created and paid (ETICKET_ISSUED). Order ID: " + orderId);
        return String.valueOf(orderId);
    }

    /**
     * Pays the currently-created order (identified by {@code this.orderId} and
     * {@code this.orderHash}) via Sakuku, then polls {@code paidCheckerStatus} for up
     * to two minutes. The test is skipped (not failed) when Sakuku payment cannot
     * be initiated or the payment never settles, mirroring the existing
     * {@code createIssuedOrderApi} contract.
     */
    private void paySakukuAndWaitPaid(String accessToken) throws ExecutionException, InterruptedException {
        if (orderHash == null || orderHash.isBlank()) {
            Assertion.skip("Sakuku payment cannot start because orderHash is missing. Skipping the subsequent steps.");
            return;
        }
        SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, orderId);
        SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
        if (!"SUCCESS".equalsIgnoreCase(submitSakukuResponse.message)) {
            Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            return;
        }
        grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
        logger.log("Grand Total -> " + grandTotal);

        String paymentStatus = paidCheckerStatus(120);
        if (!paymentStatus.equalsIgnoreCase("SUCCESS")) {
            Assertion.skip("Order not paid. Skipping the subsequent steps.");
        }
    }

    private SubmitBookingV2Response submitBookingV2(SubmitBookingApiV2.Inputs inputs) {
        SubmitBookingApiV2 submitBookingApiV2 = new SubmitBookingApiV2(inputs);
        ApiResponse submitBookingApiResponse = submitBookingApiV2.hitApi(apiTools);
        Gson gson = new Gson();
        String body = submitBookingApiResponse.getBody().toString();
        logger.log("submitBookingApiResponse-> " + body);

        SubmitBookingV2Response response = gson.fromJson(body, SubmitBookingV2Response.class);

        // Fail fast on backend errors so callers can react instead of silently
        // proceeding with orderId=0. Two known failure shapes:
        //   1. code != SUCCESS         (e.g. THIRD_PARTY_ERROR)
        //   2. code == SUCCESS but data.orderId == 0 with a `popup` payload
        //      (e.g. ROOM_UNAVAILABLE_SINGLE_ORDER when the room sold out)
        if (response == null
                || response.data == null
                || !"SUCCESS".equalsIgnoreCase(response.code)
                || response.data.orderId == 0) {
            String code = response != null ? response.code : "UNKNOWN";
            String message = response != null ? response.message : "no response body";
            String popupInfo = "";
            if (response != null && response.data != null && response.data.popup != null) {
                SubmitBookingV2Response.Popup p = response.data.popup;
                popupInfo = " | popup[type=" + p.type + ", title=" + p.title + ", body=" + p.body + "]";
            }
            throw new RuntimeException(
                    "submitBookingV2 failed [code=" + code + ", message=" + message + "]"
                            + popupInfo + ": " + body);
        }
        return response;
    }

    private static final Map<String, CachedDashboardToken> dashboardTokenCache = new ConcurrentHashMap<>();
    private record CachedDashboardToken(String token, int accountId, long expiry) {}

    public List<String> getGuestNameList(){
        logger.log("guest names list: " + guestNames);
        return guestNames;
    }

    @SneakyThrows
    public Boolean createIssuedOrderApiWithProtection(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,

            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency
    ) {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
        try {
            Unm<T> unm = new Unm<>(session);
            String accessToken = unm.getAccessToken(username, password, "B2C");
            logger.log("accessToken-> " + accessToken);
            logger.log("query " + query);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);

            String hotelId=null;
            hotelName=null;
            for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
                if (query.toLowerCase().contains(content.name.toLowerCase())) {
                    hotelId = content.publicId;
                    hotelName = content.name;
                    break;
                }
            }

            logger.log("hotelId-> " + hotelId);
            logger.log("hotel name->" + hotelName);

            logger.log("Date: "+startDate);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"en", "");
            SelectRoomResponse.RatePlan selectedRoom = selectRoomWithoutCancellationPolicy(selectRoomApiInputs);
            hotelRoomName = selectRoomNameWithoutCancellationPolicy(selectRoomApiInputs);
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName srp -> " +  hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            String cartID = prebook(preBookCartApiInputs);
            HashMap <String, String> insurancesCredential = prebookInsuranceDetails(preBookCartApiInputs);
            String insurances = insurancesCredential.get("insuranceCode");
            String insuranceCredentials = insurancesCredential.get("insuranceCredentials");
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingWithProtectionInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, "", false, "", "id", insurances, insuranceCredentials, hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingWithProtectionInputs);
            guestNames = submitBookingWithProtectionInputs.getGuestNames();
            orderId = submitBookingResponse.data.orderId;
            orderHash = submitBookingResponse.data.orderHash;
            logger.log("orderId-> " + orderId);
            logger.log("orderHash-> " + orderHash);

            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, orderId);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!"SUCCESS".equalsIgnoreCase(submitSakukuResponse.message)) Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> " + grandTotal);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");

            OrderApi<?> orderApi = new OrderApi<>(session);
            HotelInternalOrderDetail hotelOrderDetail = orderApi.getHotelInternalOrderData(String.valueOf(orderId));
            orderName = orderApi.getHotelOrderNameFromInternalOrder(hotelOrderDetail,String.valueOf(orderId));
            logger.log("order name-> " + orderName );

            return paymentStatus.equalsIgnoreCase("SUCCESS");
        }
        catch(Exception e){
            logger.log("Attempt " + attempt + "/" + maxAttempts + " failed: " + ExceptionUtils.getStackTrace(e));
            if (attempt == maxAttempts) {
                Assertion.skip("Hotel Booking via API failed after " + maxAttempts + " attempts. Skipping the subsequent steps.");
            } else {
                logger.log("Retrying hotel booking via API (attempt " + (attempt + 1) + "/" + maxAttempts + ")");
            }
        }
        }
        return false;
    }

    public HashMap<String, String> prebookInsuranceDetails(PreBookGeneratingCartApi.Inputs inputs) throws ExecutionException, InterruptedException {
        PreBookGeneratingCartApi preBookGeneratingCartApi = new PreBookGeneratingCartApi(inputs);
        ApiResponse preBookCartApiResponse = preBookGeneratingCartApi.hitApi(apiTools);
        Gson gson = new Gson();
        PreBookGeneratingCartResponse generatingCartResponse = gson.fromJson(preBookCartApiResponse.getBody(),PreBookGeneratingCartResponse.class);
        String insuranceCode = generatingCartResponse.data.insuranceV2.get(0).list.get(1).productCode;
        String insuranceCredentials = generatingCartResponse.data.insuranceV2.get(0).list.get(1).credentialCode;
        logger.log("insuranceCode-> " + insuranceCode + ", insuranceCredentials-> " + insuranceCredentials);
        HashMap<String, String> insuranceCredential = new HashMap<>();
        insuranceCredential.put("insuranceCode", insuranceCode);
        insuranceCredential.put("insuranceCredentials", insuranceCredentials);
        return insuranceCredential;
    }

    /**
     * Prints the full PayAtHotelMain-style output sections:
     * banner, mock check report, PAH availability report, and PAH hotel summary.
     *
     * @return the rows shown under {@code === PAH Hotel Summary ===} as structured POJOs.
     *         When mocked vendors are detected, the availability check filters the expected
     *         vendor combinations to those mocked vendors only instead of short-circuiting.
     *         Empty when no hotel had PAH in range after the applicable vendor filtering.
     *         Downstream tests can use this to navigate to the PDP URL, pick the first
     *         available hotel, assert vendor coverage, etc.
     */
    public List<PayAtHotelRoom> findPayAtHotelRoom(
            String dateRange,
            String numberOfNights,
            String numberOfRooms,
            String numberOfAdults) throws Exception {
        String environmentKey = pahEnvironmentKeyFromMaven();
        Objects.requireNonNull(dateRange, "dateRange");
        Objects.requireNonNull(numberOfNights, "numberOfNights");
        Objects.requireNonNull(numberOfRooms, "numberOfRooms");
        Objects.requireNonNull(numberOfAdults, "numberOfAdults");
        Map<String, Object> testData = loadPayAtHotelTemplateTestData();
        testData.put("numberOfNights", Integer.parseInt(numberOfNights.trim()));
        testData.put("numberOfRooms", Integer.parseInt(numberOfRooms.trim()));
        testData.put("numberOfAdults", Integer.parseInt(numberOfAdults.trim()));
        String[] rangeParts = dateRange.split("-");
        int dateFrom = Integer.parseInt(rangeParts[0].trim());
        int dateTo = Integer.parseInt(rangeParts[1].trim());

        printPahBanner(environmentKey, dateFrom, dateTo);
        Map<String, Map<String, String[]>> mockReportByVendor = buildPahMockReport(environmentKey, testData);
        printPahMockReport(mockReportByVendor);

        List<String> mockedVendors = pahMockedVendors(mockReportByVendor, testData);
        if (!mockedVendors.isEmpty()) {
            pahReportLine("");
            pahReportLine("Mocked vendor(s) detected: " + String.join(", ", mockedVendors)
                    + ". Availability check will focus on these vendor(s) only.");
        }

        List<String[]> expectedCombos = payAtHotelExpectedCombos(testData, mockedVendors);
        Map<String, HotelCoreResponse.Content> hotelDetails = fetchPahHotelDetailsFromCore(environmentKey, expectedCombos);

        Map<String, String> earliestPahDates = printPahAvailabilityAcrossDateRange(
                environmentKey, testData, expectedCombos, hotelDetails, dateFrom, dateTo);

        List<PayAtHotelRoom> summaryRows = buildPahSummaryRows(earliestPahDates, hotelDetails, environmentKey);
        printPahSummaryReport(summaryRows);
        return summaryRows;
    }

    /** PAH clusters match Maven {@code -Denv}: {@code gk} vs {@code preprod}; prod is not supported. */
    private static String pahEnvironmentKeyFromMaven() {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            throw new SkipException("PAH summary generator targets GK/preprod hotel clusters only.");
        }
        return Configs.ENVIRONMENT == Environment.GK ? "gk" : "preprod";
    }

    private Map<String, Object> loadPayAtHotelTemplateTestData() throws Exception {
        try (var in = HotelApi.class.getResourceAsStream("/payAtHotel/testData.json")) {
            if (in == null) {
                throw new IllegalStateException("Classpath resource /payAtHotel/testData.json not found");
            }
            Gson gson = new Gson();
            return gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8),
                    new TypeToken<Map<String, Object>>() {
                    }.getType());
        }
    }

    @SuppressWarnings("unchecked")
    private List<String[]> payAtHotelExpectedCombos(Map<String, Object> testData, List<String> vendorFilter) {
        List<String[]> combos = new ArrayList<>();
        List<Map<String, Object>> vendorArray = (List<Map<String, Object>>) testData.get("vendorV2Array");
        Set<String> allowed = (vendorFilter == null || vendorFilter.isEmpty())
                ? null
                : new HashSet<>(vendorFilter);
        for (Map<String, Object> v : vendorArray) {
            String vendor = (String) v.get("vendor");
            if (allowed != null && !allowed.contains(vendor)) {
                continue;
            }
            List<String> externalIds = (List<String>) v.get("externalIds");
            for (String externalId : externalIds) {
                combos.add(new String[]{externalId, vendor});
            }
        }
        return combos;
    }

    private Map<String, HotelCoreResponse.Content> fetchPahHotelDetailsFromCore(String env, List<String[]> expectedCombos) {
        String baseUrl = pahCoreBaseUrl(env);
        Map<String, HotelCoreResponse.Content> hotelDetails = new LinkedHashMap<>();
        for (String[] combo : expectedCombos) {
            String key = combo[0] + "|" + combo[1];
            HotelCoreResponse.Content content = getPahHotelCoreContent(baseUrl, combo[1], combo[0]);
            if (content != null) {
                hotelDetails.put(key, content);
            }
        }
        return hotelDetails;
    }

    private HotelCoreResponse.Content getPahHotelCoreContent(String baseUrl, String vendor, String externalId) {
        try {
            Map<String, Object> queryParams = new LinkedHashMap<>();
            queryParams.put("isDeleted", 0);
            queryParams.put("vendorName", vendor);
            queryParams.put("vendorExternalId", externalId);
            queryParams.put("page", 0);
            queryParams.put("limit", 20);
            queryParams.put("sort", "updatedDate,asc");

            Map<String, String> headers = new LinkedHashMap<>();
            headers.put("accept", "*/*");
            headers.put("storeId", "TIKETCOM");
            headers.put("channelId", "WEB");
            headers.put("requestId", "b2903f11-f5bc-424d-964a-eaeca8b905b1");
            headers.put("serviceId", "LOGIN");
            headers.put("X-Account-Id", "0");
            headers.put("username", "support@tiket.com");
            headers.put("lang", "en");

            Response raw = RestAssured.given()
                    .baseUri(baseUrl)
                    .queryParams(queryParams)
                    .headers(headers)
                    .get("/hotel");
            int status = raw.getStatusCode();
            String rawBody = raw.getBody().asString();
            if (status != 200) {
                String snippet = rawBody != null && rawBody.length() > 600 ? rawBody.substring(0, 600) + "..." : rawBody;
                logger.log("[WARN] PAH core BE HTTP " + status + " for " + vendor + "/" + externalId + ": " + snippet);
                return null;
            }
            // Same as tiketRakesh-Utilities PahCheckService.fetchHotelDetails: wrapper { data: { content: [...] } }.
            HotelCoreResponse response = raw.as(HotelCoreResponse.class);
            if (response == null || response.getData() == null || response.getData().getContent() == null
                    || response.getData().getContent().isEmpty()) {
                String snippet = rawBody != null && rawBody.length() > 400 ? rawBody.substring(0, 400) + "..." : rawBody;
                logger.log("[WARN] PAH core BE returned no content for " + vendor + "/" + externalId + ". Body prefix: " + snippet);
                return null;
            }
            return response.getData().getContent().get(0);
        } catch (Exception e) {
            logger.log("[WARN] PAH core BE request failed for " + vendor + "/" + externalId + ": " + e.getMessage());
            return null;
        }
    }

    private static String pahCoreBaseUrl(String environment) {
        return "gk".equals(environment)
                ? "https://hotel-core-be-svc.test-hotel-cluster.tiket.com/tix-hotel-core"
                : "https://hotel-core-be-svc.preprod-hotel-cluster.tiket.com/tix-hotel-core";
    }

    private static String pahPriceBaseUrl(String environment) {
        return "gk".equals(environment)
                ? "https://hotel-price-engine-svc.test-hotel-cluster.tiket.com/tix-hotel-price-engine"
                : "https://hotel-price-engine-svc.preprod-hotel-cluster.tiket.com/tix-hotel-price-engine";
    }

    private static String pahIntegratorBaseUrl(String environment) {
        return "gk".equals(environment)
                ? "https://hotel-integrator-framework-svc.test-hotel-cluster.tiket.com/tix-hotel-integrator-framework"
                : "https://hotel-integrator-framework-svc.preprod-hotel-cluster.tiket.com/tix-hotel-integrator-framework";
    }

    private Map<String, String> printPahAvailabilityAcrossDateRange(
            String env,
            Map<String, Object> testData,
            List<String[]> expectedCombos,
            Map<String, HotelCoreResponse.Content> hotelDetails,
            int dateFrom,
            int dateTo) {

        LocalDate today = LocalDate.now();
        Map<String, String> earliestPahDate = new LinkedHashMap<>();
        int[] rowCounter = {1};

        printPahReportHeader();

        for (int offset = dateFrom; offset <= dateTo; offset++) {
            String startDate = today.plusDays(offset).format(PAH_DATE_FMT);
            HotelListRequest requestBody = buildPahHotelListRequest(testData, startDate);
            HotelListResponse response = postPahHotelList(env, requestBody);
            if (response == null) {
                printPahSkippedRows(expectedCombos, hotelDetails, startDate, rowCounter);
                continue;
            }

            Map<String, Boolean> seen = evaluatePahAvailability(response);
            for (String[] combo : expectedCombos) {
                String key = combo[0] + "|" + combo[1];
                HotelCoreResponse.Content content = hotelDetails.get(key);
                String hotelName = content != null ? content.getName() : "N/A";
                boolean pahAvailable = Boolean.TRUE.equals(seen.get(key));
                String url = (pahAvailable && content != null) ? buildPahHotelUrl(env, content, startDate) : "-";
                if (pahAvailable && content == null) {
                    logger.log("[PAH] Price engine reports PAH for " + key
                            + " but Core BE returned no hotel — URL stays \"-\".");
                }
                printPahAvailabilityRow(rowCounter[0]++, combo[0], combo[1], hotelName,
                        String.valueOf(pahAvailable), startDate, url);

                if (pahAvailable) {
                    earliestPahDate.putIfAbsent(key, startDate);
                }
            }
        }
        printPahReportFooter();
        return earliestPahDate;
    }

    private static Map<String, Boolean> evaluatePahAvailability(HotelListResponse response) {
        Map<String, Boolean> seen = new LinkedHashMap<>();
        if (response == null || response.getData() == null || response.getData().getHotels() == null) {
            return seen;
        }
        for (HotelListResponse.Hotel hotel : response.getData().getHotels()) {
            if (hotel.getHotelRoom() == null) {
                continue;
            }
            for (HotelListResponse.HotelRoom room : hotel.getHotelRoom()) {
                String key = room.getHotelExternalId() + "|" + room.getVendor();
                boolean pahRoom = room.getRoomBenefits() != null
                        && room.getRoomBenefits().isPayAtHotel()
                        && "pay_at_hotel".equals(room.getPaymentOption());
                seen.merge(key, pahRoom, (existing, incoming) -> existing || incoming);
            }
        }
        return seen;
    }

    private HotelListResponse postPahHotelList(String env, HotelListRequest body) {
        try {
            Map<String, String> headers = new LinkedHashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("X-Store-Id", "TIKETCOM");
            headers.put("X-Channel-Id", "DESKTOP");
            headers.put("X-Currency", "SGD");
            headers.put("X-Request-Id", "findingPAH");
            headers.put("X-Service-Id", "gateway");
            headers.put("X-Username", "qaapps@tiket.com");
            headers.put("Accept-Language", "en");
            headers.put("login", "1");
            headers.put("X-Loyalty-Level", "LV4");
            headers.put("X-Country-Id", "en");
            headers.put("X-Account-Id", "123456789");
            headers.put("X-Reseller-Id", "qaapps");
            headers.put("version", "2");

            String jsonBody = PAH_OBJECT_MAPPER.writeValueAsString(body);
            Response raw = RestAssured.given()
                    .baseUri(pahPriceBaseUrl(env))
                    .contentType(ContentType.JSON)
                    .headers(headers)
                    .body(jsonBody)
                    .post("/price/v2/hotel-list");
            int status = raw.getStatusCode();
            String rawBody = raw.getBody().asString();
            if (status != 200) {
                String snippet = rawBody.length() > 900 ? rawBody.substring(0, 900) + "..." : rawBody;
                logger.log("[WARN] PAH hotel-list HTTP " + status + ": " + snippet);
                return null;
            }
            HotelListResponse parsed = raw.as(HotelListResponse.class);
            if (parsed != null && parsed.getData() != null && parsed.getData().getHotels() == null) {
                String snippet = rawBody.length() > 600 ? rawBody.substring(0, 600) + "..." : rawBody;
                logger.log("[WARN] PAH hotel-list 200 OK but data.hotels is null (check JSON shape vs DTO). Body prefix: "
                        + snippet);
            }
            return parsed;
        } catch (Exception e) {
            logger.log("[WARN] PAH hotel-list request failed: " + e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private HotelListRequest buildPahHotelListRequest(Map<String, Object> testData, String startDate) {
        List<Map<String, Object>> vendorRaw = (List<Map<String, Object>>) testData.get("vendorV2Array");
        List<HotelListRequest.VendorData> vendorList = new ArrayList<>();
        for (Map<String, Object> v : vendorRaw) {
            HotelListRequest.VendorData vd = new HotelListRequest.VendorData();
            vd.setVendor((String) v.get("vendor"));
            vd.setExternalIds((List<String>) v.get("externalIds"));
            vd.setActiveRoomDetails(v.get("activeRoomDetails"));
            vendorList.add(vd);
        }

        HotelListRequest.HotelDetail hotelDetail = new HotelListRequest.HotelDetail();
        hotelDetail.setCountryId("indonesia");
        hotelDetail.setRegionId("jakarta-108001534490276204");
        hotelDetail.setCityId("west-jakarta-108001534490276536");
        hotelDetail.setAreaId("grogol-petamburan-108001534490276846");
        hotelDetail.setHotelTimeZone("+07:00");
        hotelDetail.setStarRating(4);

        HotelListRequest.HotelIdEntry entry = new HotelListRequest.HotelIdEntry();
        entry.setVendorDataV2(vendorList);
        entry.setHotelDetail(hotelDetail);
        entry.setAllowToSearch(new LinkedHashMap<>());

        Map<String, HotelListRequest.HotelIdEntry> hotelIds = new LinkedHashMap<>();
        hotelIds.put("randomHotelId", entry);

        HotelListRequest request = new HotelListRequest();
        request.setHotelIds(hotelIds);
        request.setStartDate(startDate);
        request.setUsePriceDrop(true);
        request.setNumberOfNights(pahAsInt(testData.get("numberOfNights")));
        request.setNumberOfRooms(pahAsInt(testData.get("numberOfRooms")));
        request.setNumberOfAdults(pahAsInt(testData.get("numberOfAdults")));
        request.setPackageRate(0);
        request.setChildrenAge(pahToIntList(testData.get("childrenAge")));
        request.setLocalTimezone("+05:30");
        request.setShowAlternate(true);
        request.setUseSmartMapping(true);
        request.setSearchOnly(Boolean.TRUE.equals(testData.get("searchOnly")));
        request.setExcludeAltProduct(false);
        // rateKeyMapping left null so Jackson NON_NULL omits it (same as utilities JsonInclude.NON_NULL).
        return request;
    }

    private static int pahAsInt(Object o) {
        if (o instanceof Number n) {
            return n.intValue();
        }
        return Integer.parseInt(String.valueOf(o));
    }

    private static List<Integer> pahToIntList(Object raw) {
        if (raw == null) {
            return Collections.emptyList();
        }
        List<?> list = (List<?>) raw;
        List<Integer> out = new ArrayList<>();
        for (Object x : list) {
            if (x instanceof Number n) {
                out.add(n.intValue());
            }
        }
        return out;
    }

    private void printPahBanner(String environmentKey, int dateFrom, int dateTo) {
        String environmentLabel = "gk".equals(environmentKey)
                ? "GK (Gatotkaca/Staging)"
                : "Preprod (Sandbox)";
        pahReportLine("=== Pay At Hotel Finder ===");
        pahReportLine("Environment : " + environmentLabel);
        pahReportLine("Date Range  : today +" + dateFrom + " to +" + dateTo + " days");
    }

    @SuppressWarnings("unchecked")
    private List<String> pahVendorsFromTestData(Map<String, Object> testData) {
        Object raw = testData.get("vendorArray");
        if (!(raw instanceof List<?> list)) {
            return List.of();
        }
        List<String> vendors = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof Map<?, ?> map) {
                Object v = ((Map<String, Object>) map).get("vendor");
                if (v != null && !String.valueOf(v).isBlank()) {
                    vendors.add(String.valueOf(v));
                }
            }
        }
        return vendors;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, String[]>> buildPahMockReport(String environmentKey, Map<String, Object> testData) {
        String sysparVendorMappingId = getPahSysparId(testData, environmentKey, "vendorMapping");
        String sysparServiceConfigId = getPahSysparId(testData, environmentKey, "serviceConfig");
        if (sysparVendorMappingId.isBlank() || sysparServiceConfigId.isBlank()) {
            return new LinkedHashMap<>();
        }

        Map<String, Object> vendorMapping = getPahSysparById(environmentKey, sysparVendorMappingId);
        Map<String, Object> serviceConfig = getPahSysparById(environmentKey, sysparServiceConfigId);
        if (vendorMapping.isEmpty() || serviceConfig.isEmpty()) {
            return new LinkedHashMap<>();
        }

        String vendorRoot = String.valueOf(testData.getOrDefault("vendorRoot", ""));
        String serviceRoot = String.valueOf(testData.getOrDefault("serviceRoot", ""));
        List<String> vendors = pahVendorsFromTestData(testData);

        List<String> operations = (List<String>) testData.getOrDefault("operations", List.of());
        Set<String> sandboxUrls = new HashSet<>((List<String>) testData.getOrDefault("sandboxUrls", List.of()));

        Map<String, Map<String, String[]>> reportByVendor = new LinkedHashMap<>();
        for (String vendor : vendors) {
            Map<String, String[]> report = new LinkedHashMap<>();
            for (String operation : operations) {
                String clientName = traversePahMap(vendorMapping, vendorRoot + "." + vendor + "." + operation + ".clientName");
                String baseUrl = traversePahMap(serviceConfig, serviceRoot + "." + clientName + ".baseUrl");
                boolean isMock = !sandboxUrls.contains(baseUrl.trim());
                report.put(operation, new String[]{clientName, baseUrl, String.valueOf(isMock)});
            }
            reportByVendor.put(vendor, report);
        }
        return reportByVendor;
    }

    private void printPahMockReport(Map<String, Map<String, String[]>> reportByVendor) {
        pahReportLine("");
        pahReportLine("=== Mock Check Report ===");
        if (reportByVendor.isEmpty()) {
            pahReportLine("(no vendors resolved from testData.vendorArray)");
            return;
        }
        for (Map.Entry<String, Map<String, String[]>> vendorEntry : reportByVendor.entrySet()) {
            pahReportLine("");
            pahReportLine("Vendor: " + vendorEntry.getKey());
            pahReportPrintf("%-5s | %-20s | %-35s | %-55s | %-6s%n",
                    "No", "Operation", "Client Name", "Base URL", "isMock");
            pahReportLine("-".repeat(130));
            int i = 1;
            for (Map.Entry<String, String[]> entry : vendorEntry.getValue().entrySet()) {
                String[] v = entry.getValue();
                pahReportPrintf("%-5d | %-20s | %-35s | %-55s | %-6s%n",
                        i++, entry.getKey(), v[0], v[1], v[2]);
            }
            pahReportLine("-".repeat(130));
        }
    }

    /**
     * Returns the vendors (in {@code vendorArray} order) that have at least one critical
     * operation pointing to a non-sandbox (mocked) base URL. Empty when no vendor is mocked.
     */
    @SuppressWarnings("unchecked")
    private List<String> pahMockedVendors(
            Map<String, Map<String, String[]>> reportByVendor, Map<String, Object> testData) {
        Set<String> criticalOperations = new HashSet<>(
                (List<String>) testData.getOrDefault("criticalOperations", List.of()));
        List<String> mocked = new ArrayList<>();
        for (Map.Entry<String, Map<String, String[]>> entry : reportByVendor.entrySet()) {
            boolean vendorMocked = entry.getValue().entrySet().stream()
                    .filter(e -> criticalOperations.contains(e.getKey()))
                    .anyMatch(e -> "true".equals(e.getValue()[2]));
            if (vendorMocked) {
                mocked.add(entry.getKey());
            }
        }
        return mocked;
    }

    private String getPahSysparId(Map<String, Object> testData, String environmentKey, String idType) {
        Object raw = testData.get("sysparIds");
        if (!(raw instanceof Map<?, ?> idsMap)) {
            return "";
        }
        Object envIds = idsMap.get(environmentKey);
        if (!(envIds instanceof Map<?, ?> envMap)) {
            return "";
        }
        Object id = envMap.get(idType);
        return id != null ? String.valueOf(id) : "";
    }

    private Map<String, Object> getPahSysparById(String environmentKey, String sysparId) {
        try {
            Map<String, String> headers = new LinkedHashMap<>();
            headers.put("accept", "*/*");
            headers.put("storeId", "TIKETCOM");
            headers.put("channelId", "WEB");
            headers.put("requestId", "56389940-c335-40a3-8137-170be21e845a");
            headers.put("serviceId", "gateway");
            headers.put("username", "USERNAME");
            headers.put("lang", "en");
            headers.put("user-agent", "Chrome");
            headers.put("x-forwarded-host", "192.168.1.1");
            headers.put("true-client-ip", "192.168.1.1");
            headers.put("customerSessionId", "d41d8cd98f00b204e9800998ecf8427e");
            headers.put("currency", "IDR");
            headers.put("isVerifiedPhoneNumber", "false");

            String escapedJson = RestAssured.given()
                    .baseUri(pahIntegratorBaseUrl(environmentKey))
                    .headers(headers)
                    .get("/system-parameter/" + sysparId)
                    .jsonPath()
                    .getString("data.value");

            if (escapedJson == null || escapedJson.isBlank()) {
                return Collections.emptyMap();
            }
            Gson gson = new Gson();
            return gson.fromJson(escapedJson, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (Exception e) {
            logger.log("[WARN] PAH syspar request failed for id " + sysparId + ": " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    @SuppressWarnings("unchecked")
    private static String traversePahMap(Map<String, Object> map, String path) {
        Object current = map;
        for (String key : path.split("\\.")) {
            if (current instanceof Map<?, ?> currentMap) {
                current = ((Map<String, Object>) currentMap).get(key);
            } else {
                return "N/A";
            }
        }
        return current != null ? current.toString() : "N/A";
    }

    private void printPahReportHeader() {
        pahReportLine("");
        pahReportLine("=== PAH Availability Report ===");
        pahReportPrintf("%-5s | %-15s | %-12s | %-40s | %-15s | %-12s | %s%n",
                "No", "External ID", "Vendor", "Hotel Name", "PAH Available", "Date", "URL");
        pahReportLine("-".repeat(200));
    }

    private void printPahAvailabilityRow(
            int rowNo, String externalId, String vendor, String hotelName, String pahAvailable, String date, String url) {
        pahReportPrintf("%-5d | %-15s | %-12s | %-40s | %-15s | %-12s | %s%n",
                rowNo, externalId, vendor, hotelName, pahAvailable, date, url);
    }

    private void printPahSkippedRows(
            List<String[]> expectedCombos,
            Map<String, HotelCoreResponse.Content> hotelDetails,
            String startDate,
            int[] rowCounter) {
        for (String[] combo : expectedCombos) {
            String key = combo[0] + "|" + combo[1];
            HotelCoreResponse.Content content = hotelDetails.get(key);
            String hotelName = content != null ? content.getName() : "N/A";
            printPahAvailabilityRow(rowCounter[0]++, combo[0], combo[1], hotelName, "SKIPPED", startDate, "-");
        }
    }

    private void printPahReportFooter() {
        pahReportLine("-".repeat(200));
    }

    private List<PayAtHotelRoom> buildPahSummaryRows(
            Map<String, String> earliestPahDate,
            Map<String, HotelCoreResponse.Content> hotelDetails,
            String environmentKey) {

        List<PayAtHotelRoom> summaryReport = new ArrayList<>();
        for (Map.Entry<String, String> entry : earliestPahDate.entrySet()) {
            HotelCoreResponse.Content c = hotelDetails.get(entry.getKey());
            if (c == null) {
                continue;
            }
            // Key format set in printPahAvailabilityAcrossDateRange: externalId|vendor
            String[] parts = entry.getKey().split("\\|");
            String externalId = parts.length > 0 ? parts[0] : "";
            String vendor = parts.length > 1 ? parts[1] : "";
            String earliestDate = entry.getValue();
            String url = buildPahHotelUrl(environmentKey, c, earliestDate);
            summaryReport.add(PayAtHotelRoom.builder()
                    .vendor(vendor)
                    .externalId(externalId)
                    .publicId(c.getPublicId())
                    .hotelName(c.getName())
                    .countryId(extractPahCountryId(c))
                    .accommodationType(extractPahAccommodationType(c))
                    .earliestPahDate(earliestDate)
                    .url(url)
                    .build());
        }
        return summaryReport;
    }

    private static String buildPahHotelUrl(String env, HotelCoreResponse.Content content, String checkinDate) {
        return pahBaseWebUrl(env) + "/en-sg/" + extractPahAccommodationType(content)
                + "/" + extractPahCountryId(content) + "/" + content.getPublicId()
                + "?checkin=" + checkinDate;
    }

    private static String extractPahAccommodationType(HotelCoreResponse.Content content) {
        String accommodationType = content.getHotelChain() != null ? content.getHotelChain().getAccommodationType() : null;
        boolean isHotel = accommodationType == null || "hotel".equalsIgnoreCase(accommodationType);
        return isHotel ? "hotel" : "homes";
    }

    private static String extractPahCountryId(HotelCoreResponse.Content content) {
        if (content.getLocation() != null
                && content.getLocation().getCountry() != null
                && content.getLocation().getCountry().getId() != null) {
            return content.getLocation().getCountry().getId();
        }
        return "";
    }

    private static String pahBaseWebUrl(String environment) {
        return "gk".equals(environment)
                ? "https://gatotkaca.tiket.com"
                : "https://preprod.tiket.com";
    }

    private void printPahSummaryReport(List<PayAtHotelRoom> summaryReport) {
        StringWriter sw = new StringWriter();
        try (Formatter fmt = new Formatter(sw, Locale.US)) {
            fmt.format("%n=== PAH Hotel Summary ===%n");
            fmt.format("%-5s | %-25s | %-45s | %-80s%n",
                    "No", "Vendor (External ID)", "Hotel Name", "URL");
            fmt.format("%s%n", "-".repeat(162));
            int i = 1;
            for (PayAtHotelRoom row : summaryReport) {
                fmt.format("%-5d | %-25s | %-45s | %-80s%n",
                        i++, row.vendorExternalIdLabel(), row.getHotelName(), row.getUrl());
            }
            fmt.format("%s%n", "-".repeat(162));
            fmt.format("%nNote: When opening the PDP links above, please ensure you are logged in with a Diamond (LV4) account.%n");
            fmt.format("      If Pay at Hotel availability is not shown on the page, it may be because you are not logged in%n");
            fmt.format("      or your account is not on the Diamond loyalty tier.%n");
        }
        logger.log(sw.toString());
    }

    public String getHotelRoomName() {
        logger.log("hotelRoomName-> " + hotelRoomName);
        return hotelRoomName;
    }

    public String getHotelName() {
        logger.log("hotelName-> " + hotelName);
        return hotelName;
    }

    public BigInteger getGrandTotal() {
        logger.log("grandTotal-> " + grandTotal);
        return grandTotal;
    }

    public Double getGrandToalInDouble() {
        logger.log("grandTotal-> " + grandTotalInDouble);
        return grandTotalInDouble;
    }

    public Double getTotalDiscount() {
        logger.log("totalDiscount-> " + totalDiscount);
        return totalDiscount;
    }

    public String getTotalTiketPoints() {
        logger.log("totalTiketPoints-> " + totalTiketPoints);
        return totalTiketPoints;
    }

    public String getOrderName() {
        logger.log("Hotel order name-> " + orderName);
        return orderName;
    }

    @SneakyThrows
    public MasterContentPOJO getMasterContent(boolean login, String username, String password, String language, String templateCode) {
        String accessToken = getToken(generateDeviceId());
        logger.log("accessToken-> " + accessToken);

        accessToken = setLoginStatus(accessToken, login, username, password);
        String userAgent = userAgentValue();
        logger.log("userAgent-> " + userAgent);

        HotelMasterContentApi.Inputs hotelMasterContentApiInputs = new HotelMasterContentApi.Inputs(login, accessToken, username, userAgent, getLanguage(language), templateCode);
        MasterContentPOJO.MasterContentData masterContent = hotelMasterContent(hotelMasterContentApiInputs);
        logger.log("masterContent-> " + masterContent);

        MasterContentPOJO details = new MasterContentPOJO();
        details.accessToken = accessToken;
        details.userAgent = userAgent;
        details.contentList = masterContent;
        return details;
    }

    @SneakyThrows
    public String getCityName(String countryCode) {
        String accessToken = getToken(generateDeviceId());
        logger.log("accessToken-> " + accessToken);

        getCityNameApi api = new getCityNameApi(new getCityNameApi.Inputs(accessToken, countryCode));
        ApiResponse apiResponse = api.hitApi(apiTools);

        String responseBody = apiResponse.getBody();
        logger.log("getCityName responseBody-> " + responseBody);

        if (responseBody == null || responseBody.isEmpty()) {
            throw new RuntimeException("API returned empty body");
        }
        responseBody = responseBody.trim();

        Pattern pattern = Pattern.compile("\"text\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(responseBody);

        int count = 0;
        int maxCount = 10;
        while (matcher.find() && count < maxCount) {
            String text = matcher.group(1).trim();
            if (text.split("\\s+").length >= 2) {
                logger.log("First city text with >= 2 words: " + text);
                return text;
            }
            count ++;
        }
        throw new RuntimeException("No city present with the given criteria");
    }

    @SneakyThrows
    public LandingPageModulePOJO getLandingPageModule(boolean login, String username, String password, String language, String templateCode,MasterContentPOJO details) {
        MasterContentPOJO.MasterContentData masterContent = details.contentList;
        LandingPageModuleApi.Inputs landingPageModuleApiInputs = new LandingPageModuleApi.Inputs((String) details.accessToken, username, details.userAgent,
                getLanguage(language), templateCode, masterContent.id);
        LandingPageModulePOJO landingPageModule = landingPageModule(landingPageModuleApiInputs);
        logger.log("landingPageModule-> " + landingPageModule);

        return landingPageModule;
    }

    private MasterContentPOJO.MasterContentData hotelMasterContent(HotelMasterContentApi.Inputs inputs) throws ExecutionException, InterruptedException {
        HotelMasterContentApi hotelMasterContentApi = new HotelMasterContentApi(inputs);
        ApiResponse apiResponse = hotelMasterContentApi.hitApi(apiTools);
        JSONObject response = new JSONObject(apiResponse.getBody());
        JSONArray list = response.getJSONArray("data");

        int totalData = list.length();
        int index = 0;
        for (int i = 0; i < totalData; i++) {
            logger.log("masterContent index-> " + i);
            JSONObject listData = list.getJSONObject(i);
            String templateCode = String.valueOf(listData.get("templateCode"));

            if (templateCode.equals(inputs.templateCode())) {
                boolean hasLoginOnly = listData.has("loginOnly");
                logger.log("has loginOnly-> " + hasLoginOnly);

                if (inputs.login()) {
                    if (hasLoginOnly) {
                        String loginOnly = String.valueOf(listData.get("loginOnly"));
                        logger.log("loginOnly-> " + loginOnly);

                        if (loginOnly.equals("true")) {
                            logger.log("\"" + inputs.templateCode() + "\" and loginOnly is true at the " + i + " index");
                        } else {
                            logger.fail("There is a \"loginOnly\" key but the value is \"" + loginOnly + "\", the value should be \"true\"");
                        }

                    } else {
                        logger.log("\"" + inputs.templateCode() + "\" at the " + i + " index");
                    }
                    index = i;
                    break;

                } else if (hasLoginOnly) {
                    continue;

                } else {
                    logger.log("\"" + inputs.templateCode() + "\" at the " + i + " index");
                    index = i;
                    break;
                }

            } else {
                logger.softWarning("There is no templateCode for \"" + inputs.templateCode() + "\" value with has loginOnly \"" + inputs.login() + "\" at the \"" + i + "\" index");
                if (i == totalData - 1) {
                    logger.fail("There is no templateCode for \"" + inputs.templateCode() + "\" value at the response");
                    i++;
                }
            }
            index = i;
        }

        Gson gson = new Gson();
        MasterContentPOJO.MasterContentData masterContentData = gson.fromJson(list.getJSONObject(index).toString(), MasterContentPOJO.MasterContentData.class);

        return masterContentData;
    }

    private LandingPageModulePOJO landingPageModule(LandingPageModuleApi.Inputs inputs) throws ExecutionException, InterruptedException {
        LandingPageModuleApi landingPageModuleApi = new LandingPageModuleApi(inputs);
        ApiResponse apiResponse = landingPageModuleApi.hitApi(apiTools);

        Gson gson = new Gson();
        LandingPageModulePOJO landingPageModule = gson.fromJson(apiResponse.getBody(), LandingPageModulePOJO.class);

        return landingPageModule;
    }

    @SneakyThrows
    public TimedPromoPOJO getTimedPromoLocation(LandingPageModulePOJO landingPageModule) {
        int totalList = landingPageModule.data.list.size();
        logger.log("totalList-> " + totalList);
        int index;
        if (totalList > 1) {
            index = generateRandomInt(1, totalList);
        } else {
            index = 1;
        }
        logger.log("selected timed promo index-> " + index);

        LandingPageModulePOJO.List listData = landingPageModule.data.list.get(index-1);
        String location = listData.location;
        String buttonText = listData.buttonText;
        String redirectTarget = listData.redirectTarget;

        LandingPageModulePOJO.Redirect locationDetail = listData.redirect;
        String publicId = locationDetail.location;
        String locationType = locationDetail.locationType;

        ArrayList<String> hotelData = new ArrayList<>();
        ArrayList<LandingPageModulePOJO.Hotel> listHotel = listData.hotels;
        for (int i = 0; i < listHotel.size(); i++) {
            String hotelName = String.valueOf(listHotel.get(i).name);
            hotelData.add(hotelName);
        }

        TimedPromoPOJO data = new TimedPromoPOJO();
        data.index = index;
        data.location = location;
        data.buttonText = buttonText;
        data.redirectTarget = redirectTarget;
        data.publicId = publicId;
        data.locationType = locationType;
        data.hotelList = hotelData;

        logger.log("timed promo data-> " + data.toString());
        return data;
    }

    @SneakyThrows
    public String getLocationName(String publicId, String locationType, String language) {
        String accessToken = getToken(generateDeviceId());
        logger.log("accessToken-> " + accessToken);

        AccommodationLocationDetailApi.Inputs accommodationLocationDetailApiInputs = new AccommodationLocationDetailApi.Inputs(publicId, accessToken, locationType, getLanguage(language));
        JSONObject locationName = locationFromPublicId(accommodationLocationDetailApiInputs)
                .getJSONArray("hits")
                .getJSONObject(0)
                .getJSONObject("_source")
                .getJSONObject("name");

        String name;
        if (getLanguage(language).equals("en")) {
            name = String.valueOf(locationName.get("en"));
        } else {
            name = String.valueOf(locationName.get("id"));
        }
        logger.log("location name from ES-> " + name);

        return name;
    }

    private JSONObject locationFromPublicId(AccommodationLocationDetailApi.Inputs inputs) throws ExecutionException, InterruptedException {
        AccommodationLocationDetailApi accommodationLocationDetailApi = new AccommodationLocationDetailApi(inputs);
        ApiResponse apiResponse = accommodationLocationDetailApi.hitApi(apiTools);
        return new JSONObject(apiResponse.getBody()).getJSONObject("hits");
    }

    /**
     * This method is used to asynchronously fetch search data related to a specific location and location type.
     * It uses the CompletableFuture's supplyAsync method to run the getAutocompleteData method asynchronously.
     * If an exception occurs during the execution of the getAutocompleteData method, it is logged and null is returned.
     *
     * @param username The username of the user. This is a String.
     * @param password The password of the user. This is a String.
     * @param location The location for which the search data is to be fetched. This is a String.
     * @param locationType The type of the location for which the search data is to be fetched. This is a String.
     * @return A CompletableFuture that will be completed with the result of the getAutocompleteData method or null if an exception occurs.
     */
    public CompletableFuture<HotelAutoCompleteResponse> getSearchDataAsync(String username, String password, String location, String locationType) {
        return CompletableFuture.supplyAsync(() -> getAutocompleteData(username, password, location, locationType))
                .exceptionally(ex -> {
                    logger.exception(ex);
                    return null;
                });
    }

    @SneakyThrows
    public JSONObject getMasterTagList(boolean login, String username, String password, String query, String accommodationType, String language, int totalMasterTag) {
        String separator = "/";
        String[] splitData = splitLanguageData(query, separator);
        String queryId = splitData[0];
        String queryEn = splitData[1];

        String accessToken = getToken(generateDeviceId());
        logger.log("accessToken-> " + accessToken);
        accessToken = setLoginStatus(accessToken, login, username, password);

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, queryId, accommodationType);
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        String publicId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        String locationType = hotelAutoCompleteResponse.data.contents.get(0).type;
        logger.log("publicId-> " + publicId);
        logger.log("locationType-> " + locationType);

        AccommodationMasterTagApi.Inputs accommodationMasterTagApiInputs = new AccommodationMasterTagApi.Inputs(accessToken, username, getLanguage(language), accommodationType, publicId, locationType );
        AccommodationMasterTagResponse accommodationMasterTagResponse = accommodationMasterTagApi(accommodationMasterTagApiInputs);
        JSONObject data = new JSONObject();
        data.put("accessToken", accessToken);
        data.put("publicId", publicId);
        data.put("locationType", locationType);
        data.put("data", accommodationMasterTagResponse);
        logger.log("data-> " + data);
        logger.log("masterTagList ->" + accommodationMasterTagResponse.toString());

        return data;
    }

    public HashMap<String, String> getMasterTagDetails(JSONObject data) {
        AccommodationMasterTagResponse array = (AccommodationMasterTagResponse) data.get("data");
        int totalData = array.data.length;
        logger.log("total master tag details: totalData-> " + totalData);
        int masterTagIndex;
        if (totalData > 1) {
            masterTagIndex = generateRandomInt(0, totalData-1);
        } else {
            masterTagIndex = 0;
        }
        HashMap<String, String> groupFilterDetails = new HashMap<>();
        groupFilterDetails.put("name", String.valueOf(array.data[masterTagIndex].name));
        groupFilterDetails.put("subtitle", String.valueOf(array.data[masterTagIndex].subtitle));
        groupFilterDetails.put("keyword", String.valueOf(array.data[masterTagIndex].keyword));

        logger.log("groupFilterName-> " + groupFilterDetails.get("name"));
        logger.log("groupFilterSubtitle-> " + groupFilterDetails.get("subtitle"));
        logger.log("groupFilterKeyword-> " + groupFilterDetails.get("keyword"));
        return groupFilterDetails;
    }

    private AccommodationMasterTagResponse accommodationMasterTagApi(AccommodationMasterTagApi.Inputs inputs) throws ExecutionException, InterruptedException {
        AccommodationMasterTagApi accommodationMasterTagApi = new AccommodationMasterTagApi(inputs);
        ApiResponse apiResponse = accommodationMasterTagApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), AccommodationMasterTagResponse.class);
    }

    @SneakyThrows
    public List<String> getHotelList(String accessToken, String username, int adult, int night, int room, String type, String publicId, String startDate, String groupFilterKeyword, int totalCard, String accommodationType, String currency) {
        if (accommodationType.equals("NHA")) {
            accommodationType = "nha";
        } else {
            accommodationType = "hotel";
        }

        String userAgent = userAgentValue();
        logger.log("userAgent-> " + userAgent);

        List<String> hotelName = new ArrayList<>();
        HotelSearchApi.Inputs hotelSearchApiInputs = new HotelSearchApi.Inputs(accessToken, username, adult, night, room, type, publicId, startDate, groupFilterKeyword, accommodationType, userAgent, currency);
        SearchHotelResponse searchHotelResponse = customSearchHotel(hotelSearchApiInputs);
        if (!searchHotelResponse.data.contents.isEmpty()) {
            if (totalCard > searchHotelResponse.data.contents.size())
                totalCard = searchHotelResponse.data.contents.size();
            for (int i = 0; i < totalCard; i++) {
                String contentName = searchHotelResponse.data.contents.get(i).hotelName;
                logger.log(i + " contentName-> " + contentName);
                hotelName.add(contentName);
            }
        }
        logger.log("hotelNameList-> " + hotelName);
        return hotelName;
    }

    @SneakyThrows
    public ArrayList<String> getHotelNameAds(String username, String password, int adult, int night, int room, String type, String publicId, String startDate, String groupFilterKeyword, String accommodationType, String currency) {
Unm<T> unm = new Unm<>(session);
String accessToken = unm.getAccessToken(username, password, "B2C");
        HotelSearchApi.Inputs hotelSearchApiInputs = new HotelSearchApi.Inputs(
                accessToken, username, adult, night, room, type, publicId, startDate, "", accommodationType, userAgentValue(), currency
        );
        
        SearchHotelResponse searchHotelResponse = customSearchHotel(hotelSearchApiInputs);
    
        // 1. Null Safety: Prevent crashes if the API response is null or empty
        if (searchHotelResponse == null || searchHotelResponse.data == null || searchHotelResponse.data.contents == null) {
            Assertion.skip("Skip the test case: Hotel search response is null or empty");
            return new ArrayList<>(); 
        }
    
        // 2. Stream API: Cleaner filtering and mapping without standard for-loops
        ArrayList<String> hotelNameAds = searchHotelResponse.data.contents.stream()
                .filter(content -> content.labels != null && content.labels.ads != null) // Keep only hotels with ads
                .map(content -> content.hotelName)                                       // Extract the hotel name
                .collect(Collectors.toCollection(ArrayList::new));                       // Collect directly to ArrayList
    
        // 3. Fixed Bug: Now we check if the ENTIRE list has zero ads, not just index 0
        if (hotelNameAds.isEmpty()) {
            Assertion.skip("Skip the test case due to can't find any hotels with ads");
        }
    
        return hotelNameAds;
    }

    @SneakyThrows
    public ArrayList<String> getHotelListLandingPage(LandingPageModulePOJO hotelModule) {
        ArrayList<String> hotelData = new ArrayList<>();
        ArrayList<LandingPageModulePOJO.List> listArray = hotelModule.data.list;
        ArrayList<LandingPageModulePOJO.Hotel> listHotels = listArray.get(0).hotels;
        for (int i = 0; i < listHotels.size(); i++) {
            String hotelsName = String.valueOf(listHotels.get(i).name);
            hotelData.add(hotelsName);
        }

        return hotelData;
    }

    @SneakyThrows
    public Boolean createOrderTixPointsApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,

            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency
    ) {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        logger.log("accessToken-> " + accessToken);

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"id", "");
        SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs);
        String roomName = selectRoomName(selectRoomApiInputs);
        hotelRoomName = roomName;
        int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
        int maxChildOccupancy = selectedRoom.maxChildOccupancy;
        logger.log("hotelRoomName-> " + hotelRoomName);

        PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
        String cartID = prebook(preBookCartApiInputs);
        logger.log("cartID-> " + cartID);

        SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, "",false, "", "id", hotelId, startDate);
        SubmitBookingResponse submitBookingResponse = submitBookingWithoutValidation(submitBookingApiInputs);
        orderId = submitBookingResponse.data.orderId;
        logger.log("orderId-> " + orderId);

        ActiveTiketPointsApi.Inputs tiketPointsApiInputs = new ActiveTiketPointsApi.Inputs(accessToken);
        ActiveTiketPointsResponse activeTiketPointsResponse = getActivePoint(tiketPointsApiInputs);
        totalTiketPoints = activeTiketPointsResponse.data;
        logger.log("totalTiketPoints-> " + totalTiketPoints);

        OtpGenerationApi.Inputs otpGenerationApiInputs = new OtpGenerationApi.Inputs(accessToken);
        OtpGenerationResponse otpGenerationResponse = generateOtp(otpGenerationApiInputs);
        String trxId = otpGenerationResponse.data.trxId;
        logger.log("trxId-> " + trxId);

        ApplyTiketPointsApi.Inputs applyTiketPointsApiInputs = new ApplyTiketPointsApi.Inputs(accessToken, orderId, trxId);
        ApplyTiketPointsResponse applyTiketPointsResponse = applyTiketPoint(applyTiketPointsApiInputs);
        boolean tiketPointsStatus = orderId == applyTiketPointsResponse.data.orderId;
        logger.log("tiketPointsStatus-> " + tiketPointsStatus);

        SubmitPaymentApi.Inputs submitPaymentApiInputs = new SubmitPaymentApi.Inputs(submitBookingResponse.data.orderHash, orderId);
        JSONObject paymentResponse = submitPayment(submitPaymentApiInputs);
        grandTotal = paymentResponse.getJSONObject("sidebarPayment").getBigInteger("grandTotal");
        logger.log("grandTotal-> " + grandTotal);

        OrderListApi.Inputs orderListApiInputs = new OrderListApi.Inputs(username, accessToken);
        return orderList(orderListApiInputs, orderId);
    }

    @SneakyThrows
    public boolean checkOrderStatus(String username, String password, long orderId) {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        logger.log("accessToken-> " + accessToken);

        OrderListApi.Inputs orderListApiInputs = new OrderListApi.Inputs(username, accessToken);
        return orderList(orderListApiInputs, orderId);
    }

    private boolean orderList(OrderListApi.Inputs inputs, long orderId) throws ExecutionException, InterruptedException {
        logger.log("*********initial order id**********"+orderId);
        OrderListApi orderListApi = new OrderListApi(inputs);
        String expectedStatus = "ETICKET_ISSUED";
        String status = null;
        int count = 0;
        do {
            ApiResponse apiResponse = orderListApi.hitApi(apiTools);
            OrderListResponse response = new Gson().fromJson(apiResponse.getBody(), OrderListResponse.class);
            status = response.data.content.stream()
                    .filter(content -> content.orderId == orderId)
                    .findFirst()
                    .orElseThrow()
                    .orderStatus;
            count++;
            Thread.sleep(1000);
        } while(!expectedStatus.equals(status) && count < 180);

        return expectedStatus.equals(status);
    }

    @SneakyThrows
    public String getExclusiveNudges(String username, String password, String query, int adult, String startDate,int night, int room, String lang, String nudgesType)  {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        logger.log("accessToken-> " + accessToken);
        logger.log("query " + query);

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);

        String hotelId=null;
        hotelName=null;
        for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
            if (query.toLowerCase().contains(content.name.toLowerCase())) {
                hotelId = content.publicId;
                hotelName = content.name;
                break;
            }
        }

        logger.log("hotelId-> " + hotelId);
        logger.log("hotel name->" + hotelName);

        logger.log("Date: "+startDate);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate, lang, "");
        SelectRoomResponse.RatePlan selectedRoom = selectAnyRoom(selectRoomApiInputs);
        String roomName = selectRoomName(selectRoomApiInputs);
        hotelRoomName = roomName;
        String nudgesTitle;
        if(nudgesType.equals("room")){
            nudgesTitle = selectedRoom.ratePlanNudge.title;
        } else if (nudgesType.equals("facility")) {
            throw new NotImplementedException("this nudges type is no longer supported");
        } else{
            throw new NotImplementedException("not implemented nudges type");
        }

        logger.log("nudges title -> " + nudgesTitle);
        return nudgesTitle;
    }

    private SelectRoomResponseV2.Room selectRoomV2(SelectRoomApi.Inputs inputs) throws ExecutionException, InterruptedException {
        for(int attempt = 0; attempt<=3; attempt++){
            SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
            ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
            logger.log("Select Room Response : "+selectRoomApiResponse.getBody());
            Gson gson = new Gson();
            SelectRoomResponseV2 selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponseV2.class);
            if(!selectRoomResponse.data.roomList.isEmpty()){
                logger.log("Room item color" + selectRoomResponse.data.roomList.get(0).rooms.get(0).itemColor);
                logger.log("Available rooms" + selectRoomResponse.data.roomList.get(0).rooms.get(0).availableRoom);
                logger.log("Cancellation policies not null?" + (selectRoomResponse.data.roomList.get(0).rooms.get(0).cancellationPoliciesV3.policies != null));

                return selectRoomResponse.data.roomList.stream()
                        .filter(roomList -> roomList.rooms.get(0).availableRoom >= inputs.room())
                        .filter(roomList -> roomList.rooms.get(0).cancellationPoliciesV3.policies != null)
                        .filter(roomList -> Objects.equals(roomList.rooms.get(0).itemColor, nativeHotelColor))
                        .findFirst()
                        .orElseThrow()
                        .rooms
                        .stream()
                        .findFirst()
                        .orElseThrow();
            }
            Thread.sleep(10000);
        }
        throw new RuntimeException("Hotel Room not found");
    }

    private SelectRoomResponse.RatePlan selectAnyRoom(SelectRoomApi.Inputs inputs) throws ExecutionException, InterruptedException {
        for(int attempt = 0; attempt<=3; attempt++){
            SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
            ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
            Gson gson = new Gson();
            SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);
            if(!selectRoomResponse.data.rooms.isEmpty()){
                logger.log("Room item color" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).itemColor);
                logger.log("Available rooms" + selectRoomResponse.data.rooms.get(0).ratePlans.get(0).availableRoom);
                logger.log("Cancellation policies not null?" + (selectRoomResponse.data.rooms.get(0).ratePlans.get(0).cancellationPolicies.policies != null));

                return selectRoomResponse.data.rooms.stream()
                        .filter(roomList -> roomList.ratePlans.get(0).availableRoom >= inputs.room())
                        .findFirst()
                        .orElseThrow()
                        .ratePlans
                        .stream()
                        .findFirst()
                        .orElseThrow();
            }
        }
        throw new RuntimeException("Hotel Room not found");
    }

    /**
     * Skips the test (with diagnostic context) when the B2B hotel SubmitBooking response is not usable.
     * <p>
     * The backend returns HTTP 200 with {@code code:"FAILED"} and {@code data.orderId:0} on booking
     * failure - and on harder failures it can return an entirely null body, leaving
     * {@code submitBookingResponse} or {@code submitBookingResponse.data} null. Since {@code orderId}
     * is a primitive long downstream, Gson deserializes the absent/zero field as 0, which silently
     * propagates as the string {@code "0"} through subsequent payment + corporate approval calls
     * (e.g. {@code PATCH /corporate-orders/0 -> DATA_NOT_FOUND}). Fail fast here instead.
     * <p>
     * <b>Callers MUST invoke this method BEFORE dereferencing {@code submitBookingResponse.data}.</b>
     * The check tolerates a null response and a null {@code data}; if either is null the test is
     * skipped here and the caller never reaches the dereference.
     *
     * @return the validated, non-zero {@code orderId} extracted from the response, so the caller
     *         can read it without re-dereferencing {@code .data} (which keeps the call-site safe
     *         against future refactors that might forget the assertion). Return type matches the
     *         POJO field type ({@code int}) so the call-site doesn't need a narrowing cast.
     */
    private int assertSubmitBookingSucceeded(SubmitBookingV2Response submitBookingResponse) {
        String code = submitBookingResponse == null ? "null" : submitBookingResponse.code;
        String message = submitBookingResponse == null ? "null" : submitBookingResponse.message;
        int orderId = (submitBookingResponse != null && submitBookingResponse.data != null)
                ? submitBookingResponse.data.orderId
                : 0;
        boolean codeOk = submitBookingResponse != null && "SUCCESS".equalsIgnoreCase(code);
        boolean orderIdOk = submitBookingResponse != null
                && submitBookingResponse.data != null
                && orderId > 0;
        if (!codeOk || !orderIdOk) {
            String reason = String.format(
                    "Hotel B2B SubmitBooking failed -> code=%s, message=%s, orderId=%d. "
                            + "Skipping subsequent steps (payment + approval) to avoid cascading failures.",
                    code, message, orderId);
            logger.log(reason);
            Assertion.skip(reason);
        }
        return orderId;
    }

    /**
     * Skips the test when the B2B corporate-billing payment legs report an unrecoverable status.
     * Previously only {@code CORPORATE_BILLING_ERROR} was caught, so codes like
     * {@code DATA_NOT_EXIST} (which occur when the upstream booking already failed with orderId=0)
     * leaked through and corrupted downstream corporate-approval calls.
     * <p>
     * The two callers ({@link #submitPaymentB2B} / {@link #submitPaymentB2BPUT}) return only the
     * top-level JSON {@code code} field, never the full body, so an exact-match check is both safe
     * and correct. The earlier {@code contains()} approach risked false positives against codes
     * like {@code "PARTIAL_FAILED"} or future variants whose names embed the substring "FAILED",
     * which would have skipped tests on otherwise-valid responses.
     */
    private void assertB2BPaymentSucceeded(String postResponseCode, String putResponseCode) {
        String post = postResponseCode == null ? "" : postResponseCode.trim();
        String put = putResponseCode == null ? "" : putResponseCode.trim();
        if (isB2BPaymentFailureCode(post) || isB2BPaymentFailureCode(put)) {
            String reason = String.format(
                    "Hotel B2B corporate-billing payment failed -> POST=%s, PUT=%s. Skipping subsequent steps.",
                    post, put);
            logger.log(reason);
            Assertion.skip(reason);
        }
    }

    /**
     * The corporate-billing payment endpoints reply with a small, well-known set of code values
     * in the top-level {@code code} JSON field. Anything that exactly matches a known failure
     * code below is treated as unrecoverable; anything else (including {@code "SUCCESS"} and
     * any new pending/intermediate code that may be added later) is left for the caller to
     * interpret. Comparison is case-insensitive against the exact code, NOT a substring match,
     * so codes such as {@code "NOT_FAILED"} or messages containing the word "failed" cannot
     * trip this filter.
     */
    private static boolean isB2BPaymentFailureCode(String code) {
        return "FAILED".equalsIgnoreCase(code)
                || "CORPORATE_BILLING_ERROR".equalsIgnoreCase(code)
                || "DATA_NOT_EXIST".equalsIgnoreCase(code);
    }

    private SubmitCreditCardResponse submitCreditCardResponse(SubmitCreditCardApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitCreditCardApi submitCreditCardApi = new SubmitCreditCardApi(inputs);
        ApiResponse submitCreditCard = submitCreditCardApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(submitCreditCard.getBody(), SubmitCreditCardResponse.class);
    }

    private JSONObject submitPayment(SubmitPaymentApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitPaymentApi submitPaymentApi = new SubmitPaymentApi(inputs);
        ApiResponse apiResponse = submitPaymentApi.hitApi(apiTools);

        JSONArray responseArray = new JSONArray(apiResponse.getBody());
        return responseArray.getJSONObject(0)
                .getJSONObject("data")
                .getJSONObject("paymentSubmit")
                .getJSONObject("data");
    }

    public ActiveTiketPointsResponse getActivePoint(ActiveTiketPointsApi.Inputs inputs) throws ExecutionException, InterruptedException {
        ActiveTiketPointsApi tiketPointsApi = new ActiveTiketPointsApi(inputs);
        ApiResponse tiketPointsApiResponse = tiketPointsApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(tiketPointsApiResponse.getBody(), ActiveTiketPointsResponse.class);
    }

    public OtpGenerationResponse generateOtp(OtpGenerationApi.Inputs inputs) throws ExecutionException, InterruptedException {
        OtpGenerationApi otpGenerationApi = new OtpGenerationApi(inputs);
        ApiResponse otpGenerationApiResponse = otpGenerationApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(otpGenerationApiResponse.getBody(), OtpGenerationResponse.class);
    }

    public ApplyTiketPointsResponse applyTiketPoint(ApplyTiketPointsApi.Inputs inputs) throws ExecutionException, InterruptedException {
        ApplyTiketPointsApi applyTiketPointsApi = new ApplyTiketPointsApi(inputs);
        ApiResponse applyTiketPointsApiResponse = applyTiketPointsApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(applyTiketPointsApiResponse.getBody(), ApplyTiketPointsResponse.class);
    }

    public DiscountResponse applyDiscount(ApplyDiscountApi.Inputs inputs) throws ExecutionException, InterruptedException {
        ApplyDiscountApi applyDiscountApi = new ApplyDiscountApi(inputs);
        ApiResponse apiResponse = applyDiscountApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), DiscountResponse.class);
    }

    @SneakyThrows
    public String getBlackoutDate(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            String blackoutDateType
    ) {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        logger.log("accessToken-> " + accessToken);

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        int selectedIndex = findHotelIndex(hotelAutoCompleteResponse, query);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"id", "");
        SelectRoomResponseV2.Room selectedRoom = selectRoomV2(selectRoomApiInputs);
        hotelRoomName = selectedRoom.roomName;
        String hotelExternalId = selectedRoom.xInfo.hotelExternalId;
        logger.log("hotelRoomName-> " + hotelRoomName);
        logger.log("hotelExternalId-> " + hotelExternalId);

        HotelLoyaltyApi.Inputs hotelLoyaltyApiInputs = new HotelLoyaltyApi.Inputs(hotelExternalId);
        HotelLoyaltyResponse hotelLoyaltyResponse = hotelLoyalty(hotelLoyaltyApiInputs);
        List<String> blackoutDates = hotelLoyaltyResponse.data.blackoutDates;
        logger.log("available blackout dates:" + blackoutDates.toString());

        String date = null;
        for(String dates : blackoutDates) {
            logger.log("checking blackout date: " + dates);
            String[] dateList = dates.split(" - ");
            if (dateList[0].equalsIgnoreCase(getCurrentDateYYYY_MM_DD())) {
                logger.log("Blackout date is today, skipping");
                continue;
            }
            if(blackoutDateType.equals("single")){
                if (Objects.equals(dateList[0], dateList[1])){
                    date = dateList[0];
                    break;
                }
            } else{
                if (!Objects.equals(dateList[0], dateList[1])){
                    date = dates;
                    break;
                }
            }
        }
        logger.log("got blackout date of type "+blackoutDateType+" with date "+date);
        return date;
    }

    private HotelLoyaltyResponse hotelLoyalty(HotelLoyaltyApi.Inputs inputs) throws ExecutionException, InterruptedException {
        HotelLoyaltyApi hotelLoyaltyApi = new HotelLoyaltyApi(inputs);
        ApiResponse apiResponse = hotelLoyaltyApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), HotelLoyaltyResponse.class);
    }

    public String[] getMultipleSquareCardTitleList (LandingPageModulePOJO moduleApi) {
        List<String> resultList = new ArrayList<>();
        ArrayList<LandingPageModulePOJO.List> contentList = moduleApi.data.list;

        for (int i = 0; i < contentList.size(); i++) {
            resultList.add(contentList.get(i).titleContent);
        }

        String[] result = resultList.toArray(new String[0]);
        logger.log("titleContentList -> " + Arrays.toString(result));

        return result;
    }

    @SneakyThrows
    public boolean getSalutationFlag(String startDate, String hotelName) throws ExecutionException, InterruptedException {
        int night = 1;
        int room = 1;
        int adult = 1;
        String authorization = getToken(generateDeviceId());
        logger.log("authorization-> " + authorization);

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, "", hotelName, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        int selectedIndex = findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(authorization, "", adult, hotelId, night, room, startDate,"id", "");
        SelectRoomResponse selectRoomResponse = findRoom(selectRoomApiInputs);
        String[] roomIdArray = new String[room];
        String[] rateCodeArray = new String[room];
        int[] maxOccupancyArray = new int[room];
        int totalRoomList = selectRoomResponse.data.rooms.size();
        logger.log("totalRoomList -> " + totalRoomList);
        int roomIndex = 0;
        int roomTypeIndex = 0;
        for (int i = 0; i < room; i++) {
            roomIdArray[i] = selectRoomResponse.data.rooms.get(roomIndex).ratePlans.get(roomTypeIndex).roomId;
            rateCodeArray[i] = selectRoomResponse.data.rooms.get(roomIndex).ratePlans.get(roomTypeIndex).rateCode;
            maxOccupancyArray[i] = selectRoomResponse.data.rooms.get(roomIndex).ratePlans.get(roomTypeIndex).maxAdultOccupancy;
            if (roomTypeIndex == (selectRoomResponse.data.rooms.get(roomIndex).ratePlans.size()-1) && (roomIndex+1) < totalRoomList) {
                roomIndex++;
                roomTypeIndex = 0;
            }else if((roomIndex+1) == totalRoomList){
                roomTypeIndex = 0;
            }else{
                roomTypeIndex++;
            }
        }
        logger.log("roomIdArray-> " + Arrays.toString(roomIdArray));
        logger.log("rateCodeArray-> " + Arrays.toString(rateCodeArray));
        logger.log("maxOccupancyArray-> " + Arrays.toString(maxOccupancyArray));

        PreBookMultiRoomApi.Inputs preBookMultiRoomApiInputs = new PreBookMultiRoomApi.Inputs(authorization, adult, hotelId, night, startDate, maxOccupancyArray, 1, rateCodeArray, roomIdArray);
        PreBookMultiRoomApi preBookMultiRoomApi = new PreBookMultiRoomApi(preBookMultiRoomApiInputs);
        ApiResponse apiResponse = preBookMultiRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        PreBookMultiRoomResponse preBookMultiRoomResponse = gson.fromJson(apiResponse.getBody(), PreBookMultiRoomResponse.class);
        boolean isSalutationOn = preBookMultiRoomResponse.isSalutationEnabled;
        logger.log("isSalutationOn-> " + isSalutationOn);
        return isSalutationOn;
    }

    @SneakyThrows
    public double getBaseHotelPriceWithTax(boolean isTax, SelectRoomResponse selectRoomResponse) {
        double price = getHotelPrice(isTax, HotelPriceKind.FINAL, HotelPriceScope.PER_ROOM_NIGHT, selectRoomResponse);
        logger.log("getBaseHotelPriceWithTax: \"" + isTax + "\" -> " + price);
        return price;
    }

    @SneakyThrows
    public double getBaseHotelCrossedPriceWithTax(boolean isTax, SelectRoomResponse selectRoomResponse) {
        double price = getHotelPrice(isTax, HotelPriceKind.CROSSED, HotelPriceScope.PER_ROOM_NIGHT, selectRoomResponse);
        logger.log("getBaseHotelCrossedPriceWithTax: \"" + isTax + "\" -> " + price);
        return price;
    }

    @SneakyThrows
    public double getHotelPriceWithTax(String hotelName,String username, String password, int night, int room, int adult, String startDate) {
        String authorization = getToken(generateDeviceId());
        logger.log("authorization-> " + authorization);

        if(!username.equals("")){
            authorization = setLoginStatus(authorization, true, username, password);
        }

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, "", hotelName, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        logger.log("hotelAutoCompleteResponse-> " + hotelAutoCompleteResponse.data.toString());
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(authorization, username, adult, hotelId, night, room, startDate,"id", "");
        SelectRoomResponse selectRoomResponse = findRoom(selectRoomApiInputs);
        double price = getHotelPrice(true, HotelPriceKind.FINAL, HotelPriceScope.TOTAL, selectRoomResponse);
        logger.log(hotelName+ " price with tax-> " + price);
        return  price;
    }

    @SneakyThrows
    public double getHotelCrossedPriceWithTax(String hotelName,String username, String password, int night, int room, int adult, String startDate) {
        String authorization = getToken(generateDeviceId());
        logger.log("authorization-> " + authorization);

        if(!username.equals("")){
            authorization = setLoginStatus(authorization, true, username, password);
        }

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, "", hotelName, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(authorization, username, adult, hotelId, night, room, startDate,"id", "");
        SelectRoomResponse selectRoomResponse = findRoom(selectRoomApiInputs);
        double price = getHotelPrice(true, HotelPriceKind.CROSSED, HotelPriceScope.TOTAL, selectRoomResponse);
        logger.log(hotelName+ " crossed price with tax-> " + price);
        return  price;
    }

    @SneakyThrows
    public double getBaseHotelPriceWithTax(String hotelName,String username, String password, int night, int room, int adult, String startDate) {
        String authorization = getToken(generateDeviceId());
        logger.log("authorization-> " + authorization);

        if(!username.equals("")){
            authorization = setLoginStatus(authorization, true, username, password);
        }

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, "", hotelName, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        int selectedIndex = findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(authorization, username, adult, hotelId, night, room, startDate,"id", "");
        SelectRoomResponse selectRoomResponse = findRoom(selectRoomApiInputs);
        double price = getHotelPrice(true, HotelPriceKind.FINAL, HotelPriceScope.PER_ROOM_NIGHT, selectRoomResponse);
        logger.log(hotelName+ " price with tax-> " + price);
        return  price;
    }

    @SneakyThrows
    public double getBaseHotelPriceWithoutTax(String hotelName,String username, String password, int night, int room, int adult, String startDate) {
        String authorization = getToken(generateDeviceId());
        logger.log("authorization-> " + authorization);

        if(!username.equals("")){
            authorization = setLoginStatus(authorization, true, username, password);
        }

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, "", hotelName, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        int selectedIndex = findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(authorization, username, adult, hotelId, night, room, startDate,"id", "");
        SelectRoomResponse selectRoomResponse = findRoom(selectRoomApiInputs);
        double price = getHotelPrice(false, HotelPriceKind.FINAL, HotelPriceScope.PER_ROOM_NIGHT, selectRoomResponse);
        logger.log(hotelName+ " price without tax-> " + price);
        return  price;
    }

    @SneakyThrows
    public double getBaseHotelCrossedPriceWithTax(String hotelName,String username, String password, int night, int room, int adult, String startDate) {
        String authorization = getToken(generateDeviceId());
        logger.log("authorization-> " + authorization);

        if(!username.equals("")){
            authorization = setLoginStatus(authorization, true, username, password);
        }

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, "", hotelName, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        int selectedIndex = findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(authorization, username, adult, hotelId, night, room, startDate,"id", "");
        SelectRoomResponse selectRoomResponse = findRoom(selectRoomApiInputs);
        double price = getHotelPrice(true, HotelPriceKind.CROSSED, HotelPriceScope.PER_ROOM_NIGHT, selectRoomResponse);
        logger.log(hotelName+ " crossed price with tax-> " + price);
        return  price;
    }

    @SneakyThrows
    public double getBaseHotelCrossedPriceWithoutTax(String hotelName,String username, String password, int night, int room, int adult, String startDate) {
        String authorization = getToken(generateDeviceId());
        logger.log("authorization-> " + authorization);

        if(!username.equals("")){
            authorization = setLoginStatus(authorization, true, username, password);
        }

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, "", hotelName, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        int selectedIndex = findHotelIndex(hotelAutoCompleteResponse, hotelName);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(selectedIndex).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(authorization, username, adult, hotelId, night, room, startDate,"id", "");
        SelectRoomResponse selectRoomResponse = findRoom(selectRoomApiInputs);
        double price = getHotelPrice(false, HotelPriceKind.CROSSED, HotelPriceScope.PER_ROOM_NIGHT, selectRoomResponse);
        logger.log(hotelName+ " crossed price without tax-> " + price);
        return  price;
    }

    @SneakyThrows
    public Map<String, String> getHotelListNHA(String accessToken, String username, int adult, int night, int room, String type, String publicId, String date, int totalCard, String accommodationType,String password) {
        if ("NHA".equals(accommodationType)) {
            accommodationType = "NHA_SEARCH";
        } else {
            accommodationType = "NORMAL_SEARCH";
        }
        if(!username.equals("")){
            accessToken = setLoginStatus(accessToken, true, username, password);
        }
        String userAgent = userAgentValue();
        logger.log("userAgent-> " + userAgent);

        Map<String, String> hotelDataMap = new HashMap<>();
        HotelSearchApiByName.Inputs hotelSearchApiInputs = new HotelSearchApiByName.Inputs(accessToken, username, adult, night, room, type, publicId,date, accommodationType, userAgent);
        SearchHotelResponse searchHotelResponse = customSearchHotelByName(hotelSearchApiInputs);
        if (!searchHotelResponse.data.contents.isEmpty()) {
            for (int i = 0; i < totalCard; i++) {
                String contentName = searchHotelResponse.data.contents.get(i).hotelName;
                String baseRateWithoutTax = String.valueOf(searchHotelResponse.data.contents.get(i).rateInfo.price.baseRateWithoutTax);
                String totalRateWithoutTax = String.valueOf(searchHotelResponse.data.contents.get(i).rateInfo.price.totalRateWithoutTax);
                String totalRateWithTax = String.valueOf(searchHotelResponse.data.contents.get(i).rateInfo.price.totalRateWithTax);
                logger.log(i + " contentName-> " + contentName + ", baseRateWithoutTax-> " + baseRateWithoutTax + ", totalRateWithoutTax-> " + totalRateWithoutTax + ", totalRateWithTax-> " + totalRateWithTax);
                if (!baseRateWithoutTax.equals(totalRateWithoutTax)) {
                    hotelDataMap.put("hotelName", contentName);
                    hotelDataMap.put("baseRateWithoutTax", baseRateWithoutTax);
                    hotelDataMap.put("totalRateWithoutTax", totalRateWithoutTax);
                    hotelDataMap.put("totalRateWithTax", totalRateWithTax);
                    break;
                }
            }
        }
        logger.log("hotelDataMap-> " + hotelDataMap);
        return hotelDataMap;
    }

    private SearchHotelResponse customSearchHotelByName(HotelSearchApiByName.Inputs inputs) throws ExecutionException, InterruptedException {
        HotelSearchApiByName hotelSearchApiByName = new HotelSearchApiByName(inputs);
        ApiResponse hotelSearchApiResponse = hotelSearchApiByName.hitApi(apiTools);

        String responseBody = hotelSearchApiResponse.getBody();
;

        if (responseBody == null || !responseBody.trim().startsWith("{")) {
            logger.log("Invalid or empty response: " + responseBody);
            throw new JSONException("Invalid response received from the API");
        }

        Gson gson = new Gson();
        return gson.fromJson(responseBody, SearchHotelResponse.class);
    }

    @SneakyThrows
    public Map<String, String> getRoomListPagePriceData(String username, String password, String query, String accomType, String language, int adult, int night, int room, String startDate) throws ExecutionException, InterruptedException {
        String authorization = getToken(generateDeviceId());
        logger.log("authorization-> " + authorization);
        if(!username.isEmpty()){
            authorization = setLoginStatus(authorization, true, username, password);
        }

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, username, query, accomType);
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        logger.log("hotelId-> " + hotelId);
        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(authorization, username, adult, hotelId, night, room, startDate, language, "");

        SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs);
        logger.log("selectedRoom-> " + selectedRoom.toString());
        hotelRoomName = selectRoomName(selectRoomApiInputs);;
        logger.log("hotelRoomName-> " + hotelRoomName);
        String totalBaseRateWithoutTax = String.valueOf(selectedRoom.rateInfo.price.totalBaseRateWithoutTax);
        String totalRateWithoutTax = String.valueOf(selectedRoom.rateInfo.price.totalRateWithoutTax);
        String totalRateWithTax = String.valueOf(selectedRoom.rateInfo.price.totalRateWithTax);
        String roomName = hotelRoomName;
        logger.log("totalBaseRateWithoutTax-> " + totalBaseRateWithoutTax + ", totalRateWithoutTax-> " + totalRateWithoutTax + ", totalRateWithTax-> " + totalRateWithTax + ", roomName-> " + roomName);

        Map<String, String> prices = new HashMap<>();
        prices.put("totalBaseRateWithoutTax", totalBaseRateWithoutTax);
        prices.put("totalRateWithoutTax", totalRateWithoutTax);
        prices.put("totalRateWithTax", totalRateWithTax);
        return prices;
    }

    @SneakyThrows
    public HotelLandingPageResponse getLandingPageData(String tribe, String username, String password, String currency, String language, boolean withTax){
        String authorization = getToken(generateDeviceId());
        logger.log("authorization-> " + authorization);
        if(!username.equals("")){
            authorization = setLoginStatus(authorization, true, username, password);
        }
        HotelLandingPageApi.Inputs hotelLandingPageApiInputs = new HotelLandingPageApi.Inputs(tribe, authorization, currency, language, withTax);
        HotelLandingPageApi hotelLandingPageApi = new HotelLandingPageApi(hotelLandingPageApiInputs);
        ApiResponse apiResponse = hotelLandingPageApi.hitApi(apiTools);
        Assertion.assertThat("API response is successful", apiResponse.getStatusCode(), is(200));
        
        Gson gson = new Gson();
        HotelLandingPageResponse hotelLandingPageResponse = gson.fromJson(apiResponse.getBody(), HotelLandingPageResponse.class);
        logger.log("hotelLandingPageResponse-> " + hotelLandingPageResponse.toString());
        return hotelLandingPageResponse;
    }

    @SneakyThrows
    public HashMap<String,String> getHotelAfterTaxInLandingPage(String tribe,String username,String password,String currency, String language, boolean withTax){
        String authorization = getToken(generateDeviceId());
        logger.log("authorization-> " + authorization);
        if(!username.equals("")){
            authorization = setLoginStatus(authorization, true, username, password);
        }
        HotelLandingPageApi.Inputs hotelLandingPageApiInputs = new HotelLandingPageApi.Inputs(tribe,authorization ,currency, language,withTax);
        HotelLandingPageApi hotelLandingPageApi = new HotelLandingPageApi(hotelLandingPageApiInputs);
        boolean apiSuccess=false;
        for(int tries = 0; tries<5 && !apiSuccess; tries++){
            try{
                ApiResponse apiResponse = hotelLandingPageApi.hitApi(apiTools);
                Gson gson = new Gson();
                HotelLandingPageResponse hotelLandingPageResponse = gson.fromJson(apiResponse.getBody(), HotelLandingPageResponse.class);
                HashMap<String,String> firstRateInfoDetails = getFirstRateInfoDetails(hotelLandingPageResponse);
                logger.log("firstRateInfoDetails-> " + firstRateInfoDetails);
                return firstRateInfoDetails;
            }catch (Exception e){
                logger.log("Error in api call, retrying");
                Thread.sleep(500);
            }
        }
        throw new RuntimeException("Error in api call");
    }

    public static HashMap<String,String> getFirstRateInfoDetails(HotelLandingPageResponse response) {
        HashMap<String,String> rateInfoDetails = new HashMap<>();
        for (HotelLandingPageResponse.Data data : response.data) {
            if(data.templateCode.equals("FLASH_SALE")){
                continue;
            }
            for (HotelLandingPageResponse.DynamicContent dynamicContent : data.dynamicContents) {
                if(dynamicContent.cards == null){
                    continue;
                }
                for(HotelLandingPageResponse.Card card : dynamicContent.cards)
                    if (card.rateInfo != null) {
                        rateInfoDetails.put("productName", card.productName);
                        rateInfoDetails.put("basePrice", String.valueOf(card.rateInfo.basePrice));
                        rateInfoDetails.put("discountedPrice", String.valueOf(card.rateInfo.discountedPrice));
                        return rateInfoDetails;
                    }
            }
        }
        throw new RuntimeException("No rate info found");
    }

    @SneakyThrows
    public HashMap<String, String> getHotelBeforeTaxInLandingPage(String tribe, String username, String password, String currency, String language, boolean withTax) {
        String authorization = getToken(generateDeviceId());
        logger.log("authorization-> " + authorization);
        if(!username.isEmpty()){
            authorization = setLoginStatus(authorization, true, username, password);
        }
        HotelLandingPageApi.Inputs hotelLandingPageApiInputs = new HotelLandingPageApi.Inputs(tribe, authorization, currency, language, withTax);
        HotelLandingPageApi hotelLandingPageApi = new HotelLandingPageApi(hotelLandingPageApiInputs);
        boolean apiSuccess = false;
        for (int tries = 0; tries < 5 && !apiSuccess; tries++) {
            try {
                ApiResponse apiResponse = hotelLandingPageApi.hitApi(apiTools);
                Gson gson = new Gson();
                HotelLandingPageResponse hotelLandingPageResponse = gson.fromJson(apiResponse.getBody(), HotelLandingPageResponse.class);
                JSONObject response = new JSONObject(apiResponse.getBody());
                logger.log("api response -> " + response.toString());
                HashMap<String, String> firstRateInfoDetails = getFirstRateInfoDetails(hotelLandingPageResponse);
                logger.log("firstRateInfoDetails-> " + firstRateInfoDetails);
                HashMap<String, String> abtDetails = new HashMap<>();
                abtDetails.put("productName", firstRateInfoDetails.get("productName"));
                abtDetails.put("discountedPrice", firstRateInfoDetails.get("basePrice"));
                abtDetails.put("basePrice", firstRateInfoDetails.get("discountedPrice"));
                logger.log("productName-> " + firstRateInfoDetails.get("productName") + ", amountBeforeTax-> " + firstRateInfoDetails.get("basePrice") + ", discountedAmount-> " + firstRateInfoDetails.get("discountedPrice"));
                return abtDetails;
            } catch (Exception e) {
                logger.log("Error in api call, retrying");
                Thread.sleep(500);
            }
        }
        throw new RuntimeException("Error in api call");
    }

    @SneakyThrows
    public String createWaitingPaymentOrderApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency,
            String language
    ) {
        return createWaitingPaymentOrderApi(
                username, password, startDate, query, adult, night, room,
                fullName, nameTitle, guestFullName, guestTitle,
                currency, language, "HOTEL");
    }

    /**
     * Creates a waiting-payment order via Android-style {@code /hotel/v2/book}
     * (Bearer auth, {@code currency}/{@code X-Currency}, checkIn + guest payload).
     *
     * @param accommodationType {@code HOTEL} or {@code NHA}/{@code homes}
     */
    @SneakyThrows
    public String createWaitingPaymentOrderApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency,
            String language,
            String accommodationType
    ) {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        logger.log("accessToken-> " + accessToken);

        String resolvedAccommodationType = accommodationType == null || accommodationType.isBlank()
                ? "HOTEL"
                : accommodationType;
        if (resolvedAccommodationType.equalsIgnoreCase("homes")) {
            resolvedAccommodationType = "NHA";
        }
        String resolvedLanguage = (language == null || language.isBlank()) ? "id" : language.toLowerCase();
        String resolvedCurrency = (currency == null || currency.isBlank()) ? "IDR" : currency.toUpperCase();

        try {
            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(
                    accessToken, username, query, resolvedAccommodationType.toUpperCase());
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            String hotelId = null;
            for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
                if (query.toLowerCase().contains(content.name.toLowerCase())
                        || content.name.toLowerCase().contains(query.toLowerCase())) {
                    hotelId = content.publicId;
                    break;
                }
            }
            if (hotelId == null && hotelAutoCompleteResponse.data.contents != null
                    && !hotelAutoCompleteResponse.data.contents.isEmpty()) {
                hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
            }
            logger.log("hotelId-> " + hotelId + " accommodationType-> " + resolvedAccommodationType);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(
                    accessToken, username, adult, hotelId, night, room, startDate, resolvedLanguage, resolvedCurrency);
            SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs, true);
            String roomName = selectRoomName(selectRoomApiInputs);
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName-> " + hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(
                    accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId,
                    startDate, maxRoomOccupancy, maxChildOccupancy, resolvedCurrency);
            String cartID = prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(
                    accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room,
                    resolvedCurrency, false, "", resolvedLanguage, hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBookingWithoutValidation(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();
            orderId = submitBookingResponse.data.orderId;
            grandTotal = BigInteger.valueOf(submitBookingResponse.data.price.intValue());
            logger.log("orderId-> " + orderId);

            OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
            HotelInternalOrderDetail hotelOrderDetail = orderDetailPage.getHotelInternalOrderData(String.valueOf(orderId));
            orderName = orderDetailPage.getHotelOrderNameFromInternalOrder(hotelOrderDetail, String.valueOf(orderId));
            logger.log("order name-> " + orderName);

            return String.valueOf(orderId);
        } catch (Exception e) {
            logger.log("Exception occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Skipping test due to exception: " + e.getMessage());
            return null;
        }
    }

    @SneakyThrows
    public String createIssuedOrderApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,

            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,

            String currency,
            String cardNumber,
            String expiry,
            String cvv,
            String cardName
    ) {
        String[] expirySplit = expiry.split("/");
        String cardExpiredMonth = expirySplit[0];
        String cardExpiredYear = expirySplit[1];

        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        logger.log("accessToken-> " + accessToken);

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"id", currency);
        SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs, true);
        String roomName = selectRoomName(selectRoomApiInputs);
        hotelRoomName = roomName;
        int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
        int maxChildOccupancy = selectedRoom.maxChildOccupancy;
        logger.log("hotelRoomName-> " + hotelRoomName);

        PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
        String cartID = prebook(preBookCartApiInputs);
        logger.log("cartID-> " + cartID);

        SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, currency,false, "", "id", hotelId, startDate);
        SubmitBookingResponse submitBookingResponse = submitBookingWithoutValidation(submitBookingApiInputs);
        guestNames = submitBookingApiInputs.getGuestNames();
        orderId = submitBookingResponse.data.orderId;
        grandTotal = BigInteger.valueOf(submitBookingResponse.data.price.intValue());
        grandTotalInDouble =  submitBookingResponse.data.price.doubleValue();
        logger.log("orderId-> " + orderId);

        OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelInternalOrderDetail hotelOrderDetail = orderDetailPage.getHotelInternalOrderData(String.valueOf(orderId));
        orderName = orderDetailPage.getHotelOrderNameFromInternalOrder(hotelOrderDetail,String.valueOf(orderId));
        logger.log("order name-> " + orderName );

        SubmitCreditCardApi.Inputs submitCreditCardApiInputs = new SubmitCreditCardApi.Inputs(accessToken, submitBookingResponse.data.orderHash, orderId, cardNumber, cardName, cardExpiredMonth, cardExpiredYear, cvv);
        SubmitCreditCardResponse submitCreditCardResponse = submitCreditCardResponse(submitCreditCardApiInputs);
        if(submitCreditCardResponse.data.thirdPartyResponse.data.acsUrl!=null && !submitCreditCardResponse.data.thirdPartyResponse.data.acsUrl.isEmpty()){
            PaymentPage<T> paymentPage = PaymentPage.getInstance(session);
            logger.log("acsUrl-> " + submitCreditCardResponse.data.thirdPartyResponse.data.acsUrl);
            paymentPage.openUrl(submitCreditCardResponse.data.thirdPartyResponse.data.acsUrl);
        }
        return String.valueOf(orderId);
    }

    public String createIssuedOrderWithSpecialRequestApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,

            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency,
            String cardNumber,
            String expiry,
            String cvv,
            String cardName
    ) throws ExecutionException, InterruptedException {
        try{
            String[] expirySplit = expiry.split("/");
            String cardExpiredMonth = expirySplit[0];
            String cardExpiredYear = expirySplit[1];
            Unm<T> unm = new Unm<>(session);
            String accessToken = unm.getAccessToken(username, password, "B2C");
            logger.log("accessToken-> " + accessToken);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            String hotelId=null;
            hotelName=null;
            for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
                if (query.toLowerCase().contains(content.name.toLowerCase())) {
                    hotelId = content.publicId;
                    hotelName = content.name;
                    break;
                }
            }

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"id", currency);
            SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs, true);
            String roomName = selectRoomName(selectRoomApiInputs);
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName-> " + hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            String cartID = prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, currency,true, "Test Special request of user " + username + " ", "id", hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBookingWithoutValidation(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();
            orderId = submitBookingResponse.data.orderId;
            logger.log("orderId-> " + orderId);
            this.orderSpecialRequest = submitBookingApiInputs.getSpecialRequest();

            SubmitCreditCardApi.Inputs submitCreditCardApiInputs = new SubmitCreditCardApi.Inputs(accessToken, submitBookingResponse.data.orderHash, orderId, cardNumber, cardName, cardExpiredMonth, cardExpiredYear, cvv);
            SubmitCreditCardResponse submitCreditCardResponse = submitCreditCardResponse(submitCreditCardApiInputs);

            OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
            HotelInternalOrderDetail hotelOrderDetail = orderDetailPage.getHotelInternalOrderData(String.valueOf(orderId));
            orderName = orderDetailPage.getHotelOrderNameFromInternalOrder(hotelOrderDetail,String.valueOf(orderId));
            logger.log("order name-> " + orderName );

            if(!currency.equalsIgnoreCase("IDR")){
                PaymentPage<T> paymentPage = PaymentPage.getInstance(session);
                String acsUrl = submitCreditCardResponse.data.thirdPartyResponse.data.acsUrl;
                logger.log("acsUrl-> " + acsUrl);
                paymentPage.openUrl(acsUrl);
            }
            return String.valueOf(orderId);
        }catch(Exception e){
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
                Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
        return "";
    }

    @SneakyThrows
    public String createIssuedOrderApiB2B(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            String currency,
            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            EmployeeData employeeData,
            boolean selectRoomMostExpansive) {
        return createIssuedOrderApiB2B(username, password, startDate, query, adult, night, room, currency,
                fullName, nameTitle, guestFullName, guestTitle, employeeData, selectRoomMostExpansive, true);
    }

    public String createIssuedOrderApiB2B(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            String currency,
            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            EmployeeData employeeData,
            boolean selectRoomMostExpansive,
            boolean isRefundable) {
        try {
            CorporateLoginPage<T> corporateLoginPage = CorporateLoginPage.getInstance(session);
            CorporateApi<T> corporateApi = new CorporateApi<>(session);
            String accessToken = corporateLoginPage.getAccessTokenB2BCorporate(username,password);
            String auth = "Bearer " + accessToken;
            AccountData accountData = corporateApi.getAccountData(accessToken);
            logger.log("accessToken for dashboard request->" + auth);

            logger.log("query " + query);
            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(auth, username, query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            String hotelId=null;
            hotelName=null;
            for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
                if (query.toLowerCase().contains(content.name.toLowerCase())) {
                    hotelId = content.publicId;
                    hotelName = content.name;
                    break;
                }
            }

            logger.log("hotelId-> " + hotelId);
            logger.log("hotel name->" + hotelName);

            logger.log("Date: "+startDate);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(auth, username, adult, hotelId, night, room, startDate,"id", "");
            SelectRoomResponse.RatePlan selectedRoom;
            String roomName;
            HotelApi.RoomSelectionStrategy strategy = selectRoomMostExpansive
                    ? HotelApi.RoomSelectionStrategy.MOST_EXPENSIVE
                    : HotelApi.RoomSelectionStrategy.CHEAPEST;
            HotelApi.SelectedRoomB2B selectedB2B = selectRoomAndNameB2B(selectRoomApiInputs, strategy, isRefundable);
            selectedRoom = selectedB2B.ratePlan();
            roomName = selectedB2B.groupName();
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName-> " + hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(auth, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            PreBookGeneratingCartResponse preBookGeneratingCartResponse = prebookGetData(preBookCartApiInputs);
            String cartID = preBookGeneratingCartResponse.data.hotels.get(0).detail.rooms.get(0).cartId;
            logger.log("cartID-> " + cartID);

            PostInterimDetailsApiHotel postInterimDetailsApiHotel = getBodyPostInterimDetailsApiHotel(preBookGeneratingCartResponse,night, null);
            PostInterimDetailsApi.Inputs postInterimDetailsApi = new PostInterimDetailsApi.Inputs(auth,postInterimDetailsApiHotel,null);
            PostInterimDetailsApiResponse postInterimDetailsApiResponse = postInterimDetail(postInterimDetailsApi);

            PostInterimTransactionsApiBodyHotel postInterimTransactionsApiBodyHotel = getBodyPostInterimTransactionsApiBodyHotel(postInterimDetailsApiHotel, null);
            PostInterimTransactionsApi.Inputs postInterimTransactionsApiInputs = new PostInterimTransactionsApi.Inputs(auth,postInterimTransactionsApiBodyHotel,null);
            String corporateInterimID = postInterimTransactions(postInterimTransactionsApiInputs);
            logger.log("corporateInterimID-> " + corporateInterimID);

            SubmitBookingApiV2B2B.Inputs submitBookingApiInputs = new SubmitBookingApiV2B2B.Inputs(accessToken,currency,accountData,guestFullName,guestTitle,hotelId,cartID,corporateInterimID,postInterimDetailsApiResponse);
            SubmitBookingV2Response submitBookingResponse = submitBookingV2B2B(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();

            // Validate response BEFORE any .data dereference: a null response or null
            // data would otherwise NPE here and bypass the controlled skip below.
            orderId = assertSubmitBookingSucceeded(submitBookingResponse);
            logger.log("orderId-> " + orderId);

            SubmitPaymentApiB2B.Inputs submitPaymentApiInputs = new SubmitPaymentApiB2B.Inputs(auth, submitBookingResponse.data.orderHash, orderId, employeeData.data.employeeID,employeeData.data.corporateID);
            String paymentResponse = submitPaymentB2B(submitPaymentApiInputs);
            logger.log("paymentStatus"+ paymentResponse);

            SubmitPaymentPutApiB2B.Inputs submitPaymentPutApiInputs = new SubmitPaymentPutApiB2B.Inputs(auth,submitBookingResponse.data.orderHash, orderId, employeeData.data.employeeID,employeeData.data.corporateID, "testing only");
            String paymentResponsePut = submitPaymentB2BPUT(submitPaymentPutApiInputs);
            logger.log("paymentStatus"+ paymentResponsePut);

            assertB2BPaymentSucceeded(paymentResponse, paymentResponsePut);
            return String.valueOf(orderId);
        }
        catch(SkipException e){
            throw e;
        }
        catch(Exception e){
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
                Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
            }
        return "";
    }

    @SneakyThrows
    public String createWaitForPaymentApiB2B(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            String currency,
            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle
    ) {
        try {
            CorporateLoginPage<T> corporateLoginPage = CorporateLoginPage.getInstance(session);
            CorporateApi<T> corporateApi = new CorporateApi<>(session);
            String accessToken = corporateLoginPage.getAccessTokenB2BCorporate(username,password);
            String auth = "Bearer " + accessToken;
            AccountData accountData = corporateApi.getAccountData(accessToken);
            logger.log("accessToken for dashboard request->" + auth);

            logger.log("query " + query);
            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(auth, username, query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
            String hotelId=null;
            hotelName=null;
            for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
                if (query.toLowerCase().contains(content.name.toLowerCase())) {
                    hotelId = content.publicId;
                    hotelName = content.name;
                    break;
                }
            }

            logger.log("hotelId-> " + hotelId);
            logger.log("hotel name->" + hotelName);

            logger.log("Date: "+startDate);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(auth, username, adult, hotelId, night, room, startDate,"id", currency);
            SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs, true);
            String roomName = selectRoomName(selectRoomApiInputs);
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName-> " + hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(auth, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            PreBookGeneratingCartResponse preBookGeneratingCartResponse = prebookGetData(preBookCartApiInputs);
            String cartID = preBookGeneratingCartResponse.data.hotels.get(0).detail.rooms.get(0).cartId;
            logger.log("cartID-> " + cartID);

            PostInterimDetailsApiHotel postInterimDetailsApiHotel = getBodyPostInterimDetailsApiHotel(preBookGeneratingCartResponse,night, null);
            PostInterimDetailsApi.Inputs postInterimDetailsApi = new PostInterimDetailsApi.Inputs(auth,postInterimDetailsApiHotel,null);
            PostInterimDetailsApiResponse postInterimDetailsApiResponse = postInterimDetail(postInterimDetailsApi);

            PostInterimTransactionsApiBodyHotel postInterimTransactionsApiBodyHotel = getBodyPostInterimTransactionsApiBodyHotel(postInterimDetailsApiHotel, null);
            PostInterimTransactionsApi.Inputs postInterimTransactionsApiInputs = new PostInterimTransactionsApi.Inputs(auth,postInterimTransactionsApiBodyHotel,null);
            String corporateInterimID = postInterimTransactions(postInterimTransactionsApiInputs);
            logger.log("corporateInterimID-> " + corporateInterimID);

            SubmitBookingApiV2B2B.Inputs submitBookingApiInputs = new SubmitBookingApiV2B2B.Inputs(accessToken,currency,accountData,guestFullName,guestTitle,hotelId,cartID,corporateInterimID,postInterimDetailsApiResponse);
            SubmitBookingV2Response submitBookingResponse = submitBookingV2B2B(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();

            // Validate response BEFORE any .data dereference: a null response or null
            // data would otherwise NPE here and bypass the controlled skip below.
            orderId = assertSubmitBookingSucceeded(submitBookingResponse);
            logger.log("orderId-> " + orderId);
            return String.valueOf(orderId);
        }
        catch(SkipException e){
            throw e;
        }
        catch(Exception e){
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
        return "";
    }

    @SneakyThrows
    public Boolean createIssuedOrderApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,

            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency
    ) {
        try {
            Unm<T> unm = new Unm<>(session);
            String accessToken = unm.getAccessToken(username, password, "B2C");
            logger.log("accessToken-> " + accessToken);
            logger.log("query " + query);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);

            String hotelId=null;
            hotelName=null;
            for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
                if (query.toLowerCase().contains(content.name.toLowerCase())) {
                    hotelId = content.publicId;
                    hotelName = content.name;
                    break;
                }
            }

            logger.log("hotelId-> " + hotelId);
            logger.log("hotel name->" + hotelName);

            logger.log("Date: "+startDate);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"id", "");
            SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs, true);
            hotelRoomName = selectRoomName(selectRoomApiInputs);
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName srp -> " +  hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            String cartID = prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, "", false, "", "id", hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBookingWithoutValidation(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();
            orderId = submitBookingResponse.data.orderId;
            orderHash = submitBookingResponse.data.orderHash;
            logger.log("orderId-> " + orderId);
            logger.log("orderHash-> " + orderHash);

            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, orderId);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!submitSakukuResponse.message.equals("SUCCESS")) Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> " + grandTotal);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");
            
            OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
            HotelInternalOrderDetail hotelOrderDetail = orderDetailPage.getHotelInternalOrderData(String.valueOf(orderId));
            orderName = orderDetailPage.getHotelOrderNameFromInternalOrder(hotelOrderDetail,String.valueOf(orderId));
            logger.log("order name-> " + orderName );

            return paymentStatus.equalsIgnoreCase("SUCCESS");
        }
        catch(Exception e){
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
                Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
            }
        return false;
    }

    @SneakyThrows
    public Boolean createIssuedOrderApi(
            String language,
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,

            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String currency,
            boolean selectRoomMostExpansive) {
        try {
            Unm<T> unm = new Unm<>(session);
            String accessToken = unm.getAccessToken(username, password, "B2C");
            logger.log("accessToken-> " + accessToken);
            logger.log("query " + query);

            HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);

            String hotelId=null;
            hotelName=null;
            for (HotelAutoCompleteResponse.Data.Content content : hotelAutoCompleteResponse.data.contents) {
                if (query.toLowerCase().contains(content.name.toLowerCase())) {
                    hotelId = content.publicId;
                    hotelName = content.name;
                    break;
                }
            }

            logger.log("hotelId-> " + hotelId);
            logger.log("hotel name->" + hotelName);

            logger.log("Date: "+startDate);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,language, "");
            SelectRoomResponse.RatePlan selectedRoom;
            if (selectRoomMostExpansive) {
                selectedRoom = selectRoomMostExpansive(selectRoomApiInputs);
            } else {
                selectedRoom = selectRoom(selectRoomApiInputs, true);
            }
            String roomName = selectRoomName(selectRoomApiInputs);
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName srp -> " +  hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            String cartID = prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, currency, false, "", language, hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBookingWithoutValidation(submitBookingApiInputs);
            guestNames = submitBookingApiInputs.getGuestNames();
            orderId = submitBookingResponse.data.orderId;
            orderHash = submitBookingResponse.data.orderHash;
            logger.log("orderId-> " + orderId);
            logger.log("orderHash-> " + orderHash);

            SubmitSakukuApi.Inputs submitSakukuApiInputs = new SubmitSakukuApi.Inputs(accessToken, orderHash, orderId);
            SubmitSakukuResponse submitSakukuResponse = submitSakuku(submitSakukuApiInputs);
            if (!submitSakukuResponse.message.equals("SUCCESS")) Assertion.skip("Sakuku payment via API is failed. Skipping the subsequent steps.");
            grandTotal = submitSakukuResponse.data.sidebarPayment.grandTotal.toBigInteger();
            logger.log("Grand Total -> " + grandTotal);

            String paymentStatus = paidCheckerStatus(120);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");

            OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
            HotelInternalOrderDetail hotelOrderDetail = orderDetailPage.getHotelInternalOrderData(String.valueOf(orderId));
            orderName = orderDetailPage.getHotelOrderNameFromInternalOrder(hotelOrderDetail,String.valueOf(orderId));
            logger.log("order name-> " + orderName );

            return paymentStatus.equalsIgnoreCase("SUCCESS");
        }
        catch(Exception e){
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Hotel Booking via API failed. Skipping the subsequent steps.");
        }
        return false;
    }

    @SneakyThrows
    public Boolean createOrderDiscountApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            String fullName,
            String nameTitle,
            String guestFullName,
            String guestTitle,
            String discountCode,
            String currency
    ) {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password, "B2C");
        logger.log("accessToken-> " + accessToken);

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(accessToken, username, query, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate,"id", "");
        SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs, true);
        String roomName = selectRoomName(selectRoomApiInputs);
        hotelRoomName = roomName;
        int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
        int maxChildOccupancy = selectedRoom.maxChildOccupancy;
        logger.log("hotelRoomName-> " + hotelRoomName);

        PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
        String cartID = prebook(preBookCartApiInputs);
        logger.log("cartID-> " + cartID);

        SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, "", false, "", "id", hotelId, startDate);
        SubmitBookingResponse submitBookingResponse = submitBookingWithoutValidation(submitBookingApiInputs);
        orderId = submitBookingResponse.data.orderId;
        String orderHash = submitBookingResponse.data.orderHash;
        grandTotal = BigInteger.valueOf(submitBookingResponse.data.price.intValue());
        logger.log("orderId-> " + orderId);

        ApplyDiscountApi.Inputs applyDiscountApiInputs = new ApplyDiscountApi.Inputs(accessToken, String.valueOf(orderId), orderHash, discountCode, "TIXHOTEL", grandTotal, "");
        DiscountResponse discountResponse = applyDiscount(applyDiscountApiInputs);
        totalDiscount = discountResponse.data.totalDiscount;
        logger.log("totalDiscount-> " + totalDiscount);

        SubmitPaymentApi.Inputs submitPaymentApiInputs = new SubmitPaymentApi.Inputs(orderHash, orderId);
        JSONObject paymentResponse = submitPayment(submitPaymentApiInputs);
        grandTotal = paymentResponse.getJSONObject("sidebarPayment").getBigInteger("grandTotal");
        logger.log("grandTotal-> " + grandTotal);

        String paymentChecker = PaymentPage.getInstance(session).paymentChecker();
        return paymentChecker.equalsIgnoreCase("success");
    }

    @SneakyThrows
    public String createOrderMultiRoomApi(
            String username,
            String password,
            String hotelName,
            int adult,
            int night,
            int room,
            String startDate,
            int qtyEachRoom,
            String currency,
            String cardNumber,
            String expiry,
            String cvv,
            String cardName
    ) {
        String[] expirySplit = expiry.split("/");
        String cardExpiredMonth = expirySplit[0];
        String cardExpiredYear = expirySplit[1];

        Unm<T> unm = new Unm<>(session);
        String authorization = unm.getAccessToken(username, password, "B2C");
        logger.log("authorization-> " + authorization);

        HotelAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HotelAutocompleteApi.Inputs(authorization, username, hotelName, "HOTEL");
        HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelAutoComplete(hotelAutocompleteApiInputs);
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        logger.log("hotelId-> " + hotelId);

        SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(authorization, username, adult, hotelId, night, room, startDate,"id", currency);
        SelectRoomResponse selectRoomResponse = findRoom(selectRoomApiInputs);
        String[] roomIdArray = new String[room];
        String[] rateCodeArray = new String[room];
        int[] maxOccupancyArray = new int[room];
        int totalRoomList = selectRoomResponse.data.rooms.size();
        logger.log("totalRoomList -> " + totalRoomList);
        int roomIndex = 0;
        int roomTypeIndex = 0;
        for (int i = 0; i < room; i++) {
            roomIdArray[i] = selectRoomResponse.data.rooms.get(roomIndex).ratePlans.get(roomTypeIndex).roomId;
            rateCodeArray[i] = selectRoomResponse.data.rooms.get(roomIndex).ratePlans.get(roomTypeIndex).rateCode;
            maxOccupancyArray[i] = selectRoomResponse.data.rooms.get(roomIndex).ratePlans.get(roomTypeIndex).maxAdultOccupancy;
            if (roomTypeIndex == (selectRoomResponse.data.rooms.get(roomIndex).ratePlans.size()-1) && (roomIndex+1) < totalRoomList) {
                roomIndex++;
                roomTypeIndex = 0;
            }else if((roomIndex+1) == totalRoomList){
                roomTypeIndex = 0;
            }else{
                roomTypeIndex++;
            }
        }
        logger.log("roomIdArray-> " + Arrays.toString(roomIdArray));
        logger.log("rateCodeArray-> " + Arrays.toString(rateCodeArray));
        logger.log("maxOccupancyArray-> " + Arrays.toString(maxOccupancyArray));

        PreBookMultiRoomApi.Inputs preBookMultiRoomApiInputs = new PreBookMultiRoomApi.Inputs(authorization, adult, hotelId, night, startDate, maxOccupancyArray, qtyEachRoom, rateCodeArray, roomIdArray);
        PreBookMultiRoomResponse preBookMultiRoomResponse = preBookMultiRoom(preBookMultiRoomApiInputs);
        Gson gson = new Gson();

        // Converting POJO to JSON string
        String jsonString = gson.toJson(preBookMultiRoomResponse);
        logger.log("preBookMultiRoomResponse-> " + jsonString);
        List<String> cardIdArray = new ArrayList<>();
        for (int i = 0; i < room; i++) {
            cardIdArray.add(preBookMultiRoomResponse.data.hotels.get(0).detail.rooms.get(i).cartId);
        }
        logger.log("cardIdArray-> " + cardIdArray);

        SubmitBookingMultiRoomApi.Inputs submitBookingMultiRoomApiInputs = new SubmitBookingMultiRoomApi.Inputs(authorization, currency, username, hotelId, room, qtyEachRoom, cardIdArray, false, "");
        SubmitBookingMultiRoomResponse submitBookingMultiRoomResponse = submitBookingMultiRoom(submitBookingMultiRoomApiInputs);
        orderId = submitBookingMultiRoomResponse.data.orderId;
        logger.log("orderId-> " + orderId);

        SubmitCreditCardApi.Inputs submitCreditCardApiInputs = new SubmitCreditCardApi.Inputs(authorization, submitBookingMultiRoomResponse.data.orderHash, orderId, cardNumber, cardName, cardExpiredMonth, cardExpiredYear, cvv);
        SubmitCreditCardResponse submitCreditCardResponse = submitCreditCardResponse(submitCreditCardApiInputs);

        PaymentPage<T> paymentPage = PaymentPage.getInstance(session);
        if(submitCreditCardResponse.data.thirdPartyResponse.data.acsUrl!=null || !submitCreditCardResponse.data.thirdPartyResponse.data.acsUrl.equals("")){
            logger.log("acsUrl-> " + submitCreditCardResponse.data.thirdPartyResponse.data.acsUrl);
            paymentPage.openUrl(submitCreditCardResponse.data.thirdPartyResponse.data.acsUrl);
        }
        logger.log("grandTotal-> " + grandTotal);

        OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
        HotelInternalOrderDetail hotelOrderDetail = orderDetailPage.getHotelInternalOrderData(String.valueOf(orderId));
        orderName = orderDetailPage.getHotelOrderNameFromInternalOrder(hotelOrderDetail,String.valueOf(orderId));
        logger.log("order name-> " + orderName);

        return orderName;
    }

    @SneakyThrows
    public String getReviewTopic(String hotelName,String lang){
        String accessToken = getToken(generateDeviceId());
        logger.log("accessToken-> " + accessToken);
        HotelAutoCompleteResponse hotelAutoCompleteResponse = getAutocompleteData("", "", hotelName, "HOTEL");
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        ReviewPage<T> reviewPage = ReviewPage.getInstance(this.session);
        return reviewPage.getReviewTopic(accessToken,"TIXHOTEL",hotelId,lang);
    }

    @SneakyThrows
    public String getReviewTopicPublicId(String publicId, String lang){
        String accessToken = getToken(generateDeviceId());
        logger.log("accessToken-> " + accessToken);
        ReviewPage<T> reviewPage = ReviewPage.getInstance(this.session);
        return reviewPage.getReviewTopic(accessToken,"TIXHOTEL",publicId,lang);
    }

    @SneakyThrows
    public List<String> getReviewFromTopic(String hotelName,String lang, String topic){
        String accessToken = getToken(generateDeviceId());
        logger.log("accessToken-> " + accessToken);
        HotelAutoCompleteResponse hotelAutoCompleteResponse = getAutocompleteData("", "", hotelName, "HOTEL");
        String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
        ReviewPage<T> reviewPage = ReviewPage.getInstance(this.session);
        return reviewPage.getReviewFromTopic(accessToken,"TIXHOTEL",hotelId,lang,topic);
    }

    @SneakyThrows
    public List<String> getReviewFromTopicPublicId(String publicId,String lang, String topic){
        String accessToken = getToken(generateDeviceId());
        logger.log("accessToken-> " + accessToken);
        ReviewPage<T> reviewPage = ReviewPage.getInstance(this.session);
        return reviewPage.getReviewFromTopic(accessToken,"TIXHOTEL",publicId,lang,topic);
    }

    /**
     * Builds the auth Cookie header from the extranet dashboard tokens: the access token is sent as
     * {@code aat} / {@code session_access_token} and the refresh token as {@code art} / {@code session_refresh_token}.
     */
    public String buildAuthCookie(String bearerAccessToken, String refreshToken) {
        String rawAccessToken = bearerAccessToken == null ? "" : bearerAccessToken.replaceFirst("^Bearer ", "");
        String art = refreshToken == null ? "" : refreshToken;
        return "userlang=en"
                + "; aat=" + rawAccessToken
                + "; art=" + art
                + "; session_access_token=" + rawAccessToken
                + "; session_refresh_token=" + art
                + "; usercurrency=IDR"
                + "; tiket_currency=IDR"
                + "; app_is_desktop=true";
    }
}
