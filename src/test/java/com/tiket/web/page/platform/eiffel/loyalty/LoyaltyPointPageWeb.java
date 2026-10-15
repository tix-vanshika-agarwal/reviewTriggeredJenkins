package com.tiket.web.page.platform.eiffel.loyalty;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tiket.api.bus.BusCartsApi;
import com.tiket.api.bus.BusCheckoutApi;
import com.tiket.api.bus.BusSearchApi;
import com.tiket.api.bus.BusSeatLayoutApi;
import com.tiket.api.carrental.*;
import com.tiket.api.loyalty.ActiveTiketPointsApi;
import com.tiket.api.loyalty.AddActivePointsApi;
import com.tiket.api.loyalty.PendingPointListApi;
import com.tiket.api.loyalty.PointHistoryApi;
import com.tiket.api.member.GetAccountDataApi;
import com.tiket.api.order.InternalOrderDetailApi;
import com.tiket.api.train.TrainCartApi;
import com.tiket.api.train.TrainOrderApi;
import com.tiket.api.train.TrainOrderV7Api;
import com.tiket.api.train.TrainSearchApi;
import com.tiket.api.ttd.*;
import com.tiket.apiPage.FerryApi;
import com.tiket.apiPage.HotelApi;
import com.tiket.apiPage.LoyaltyApi;
import com.tiket.apiPage.TTDApi;
import com.tiket.apiPage.Unm;
import com.tiket.common.RetryUtils;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Exception.ApiEnvironmentException;
import com.tiket.model.Exception.ApiToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.bus.BusCartsResponse;
import com.tiket.pojo.bus.BusCheckoutResponse;
import com.tiket.pojo.bus.BusSearchResponse;
import com.tiket.pojo.bus.BusSeatLayoutResponse;
import com.tiket.pojo.carrental.*;
import com.tiket.pojo.loyalty.ActiveTiketPointsResponse;
import com.tiket.pojo.member.AccountData;
import com.tiket.pojo.train.TrainCartResponse;
import com.tiket.pojo.train.TrainOrderResponse;
import com.tiket.pojo.train.TrainSearchResponse;
import com.tiket.pojo.ttd.TtdCheckOrderResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;
import com.tiket.web.page.flight.landingpage.FlightLandingPageWeb;
import com.tiket.web.page.flight.searchresultpage.FlightSearchResultPageWeb;

import com.tiket.web.page.platform.payment.PaymentPageWeb;
import com.tiket.web.page.ttd.TtdBookingFormPage.TtdBookingFormPageWeb;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.SkipException;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;

public class LoyaltyPointPageWeb<T extends WebDriver> extends WebPage<T> {
    private static final int TIER_UPGRADE_PENDING_POINT = 1;
    private static final int POINT_HISTORY_MAX_ATTEMPTS = 10;
    private static final int POINT_HISTORY_RETRY_DELAY_SECONDS = 5;
    /**
     * Minimum TTD order total (IDR) for promo flows; promos such as OLDGENALLCB reject orders whose price after
     * discount is not above IDR 10,000, so a single cheap ticket is not enough.
     */
    private static final long PROMO_MIN_ORDER_TOTAL = 50_000;
    /**
     * Retry policy for the ToDo booking calls. Only transient environment errors are retried on create-order, so a
     * request the backend actually rejected is never re-sent and cannot leave a duplicate order behind.
     */
    private static final int TTD_API_RETRY_ATTEMPTS = 3;
    private static final long TTD_API_RETRY_BASE_DELAY_MS = 2_000L;
    /** Contact phone sent on a ToDo order when the UNM login data carries none. */
    private static final String FALLBACK_CONTACT_PHONE_CODE = "+62";
    private static final String FALLBACK_CONTACT_PHONE_NUMBER = "81296319528";
    /** Guest nationality sent on every API-created ToDo order. */
    private static final String TTD_GUEST_NATIONALITY = "Indonesia";

    public LoyaltyPointPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> LoyaltyPointPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new LoyaltyPointPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new LoyaltyPointPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator LOADING_TO_PAYMENT = new Locator("LOADING_TO_PAYMENT", By.xpath("//div[contains(@class,'LoadingWithIllustration_content')]"));
    private static final Locator NEW_ORDER_SUMMARY_SECTION = new Locator("NEW_ORDER_SUMMARY_SECTION", By.xpath("//div[contains(@class,'NewOrderSummarySection_container')]"));
    private static final Locator GENERAL_PRICE_BOOKING_FORM = new Locator("GENERAL_PRICE_BOOKING_FORM", By.xpath("//div[contains(@class,'TotalPrice_price')]"));
    private static final Locator RELOAD_BUTTON = new Locator("RELOAD_BUTTON", By.xpath("//*[contains(@class,'Button_variant_primary') and contains(text(), 'Reload')]"));
    private static final Locator TIKET_POINT_RADIO = new Locator("TIKET_POINT_RADIO", By.xpath("//input[@data-testid='btnApplyTiketPoints']"));

    public void bookFlight(String origin, String destination, int departureDaysFromNow, String airline, String paymentType) {
        try {
            FlightLandingPageWeb<?> flightLandingPage = FlightLandingPageWeb.getInstance(session);
            int departureDaysAddition = CoreFunctions.generateRandomInt(0, 14);
            departureDaysFromNow += departureDaysAddition;

            String currentDate = CoreFunctions.getCurrentLongDate();
            String departureDate = CoreFunctions.addToLongDate(currentDate, departureDaysFromNow);

            flightLandingPage.selectOrigin(origin);
            flightLandingPage.selectDestination(destination);
            flightLandingPage.selectDate(departureDate);
            flightLandingPage.searchFlight();

            FlightSearchResultPageWeb<?> flightSearchResultPage = FlightSearchResultPageWeb.getInstance(session);
            FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
            flightSearchResultPage.selectFlightByAirline(airline);
            flightSearchResultPage.selectTicket(1);
            flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();

            flightBookingFormPage.waitForBookingPageToLoad();
            flightBookingFormPage.fillOutSalutation("mr");
            flightBookingFormPage.clickSameContactToggle();

            totalPriceFinal = Integer.parseInt(flightBookingFormPage.getPriceText().replaceAll("\\D", ""));
            loyaltyPointsChecker(totalPriceFinal, paymentType);

            flightBookingFormPage.goToPaymentPageFromBookingFormPage();
            flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
            driverTools.waitForElementToAppear(LOADING_TO_PAYMENT, 5).ifPresent(driverTools::waitForElementToDisappear);
            flightBookingFormPage.clickGotItInBaggagePopup();
            driverTools.waitForElementToAppear(NEW_ORDER_SUMMARY_SECTION, 5);

        } catch (Exception e) {
            logger.exception(new SkipException("Failed to book flight, skipping test. \n error: " + e.getLocalizedMessage()));
            Assertion.skip("Failed to book flight, skipping test. \n error: " + e.getLocalizedMessage());
        }
    }

    public void loyaltyPointsChecker(int price, String paymentType) {
        int currentPoints = getCurrentLoyaltyPoints();

        logger.log("totalPrice: " + price);
        logger.log("currentPoints: " + currentPoints);

        this.paymentType = paymentType;
        String DEFAULT_PLATFORM_FEE = "25000";
        switch (paymentType) {
            case "full" -> {
                String platformFee = sheetData.get("platformFee") != null ? sheetData.get("platformFee") : DEFAULT_PLATFORM_FEE;
                price += Integer.parseInt(platformFee);
                if (currentPoints < price) {
                    logger.softWarning("current points is lower than total price. Injecting points");
                    addActivePoints((price - currentPoints));
                }
            }
            case "half" -> {
                if(currentPoints > price) {
                    logger.softWarning("current points is higher than half of total price. Deleting points");
                    String accessToken = "Bearer " + driver.manage().getCookieNamed("session_access_token").getValue();
                    String pdProcessType = sheetData.get("deletionType");
                    String email = sheetData.get("email");
                    int extraPoints = currentPoints - (price/2);
                    LoyaltyApi<?> loyaltyApi = new LoyaltyApi<>(session);
                    try {
                        int iteration = 0;
                        while (currentPoints > (price/2) || iteration < 3) {
                            iteration++;
                            logger.log("Start iteration: " + iteration);
                            loyaltyApi.deleteTiketPoint(email, pdProcessType, extraPoints, accessToken);
                            currentPoints = getCurrentLoyaltyPoints();
                        }
                    } catch (Exception e) {
                        logger.log("Failed message: " + e.getMessage());
                        logger.log(Arrays.toString(e.getStackTrace()));
                        Assertion.skip("Failed deleting points. Skipping the test.");
                    }
                }
                if (currentPoints == 0 || currentPoints < (price / 2)) {
                    logger.softWarning("current points is 0 or lower than half of total price. Injecting points");
                    addActivePoints((price / 2) - currentPoints);
                }
            }
            default -> throw new IllegalArgumentException("Unsupported payment type: " + paymentType);
        }
    }

    /**
     * Creates a payment-ready Train order entirely via API (search -&gt; cart -&gt; v6 order -&gt; v7
     * checkout) and returns {@code orderId}, {@code orderHash} and {@code orderPrice}. The cart/order
     * step is retried because a freshly created cart needs a short settle time before it is orderable
     * and the cart API exposes no pollable "ready" state; the v7 checkout is retried for the same
     * transient-failure reason.
     */
    public Map<String, String> bookTrain(
            String email,
            String password,
            String name,
            String phone,
            String idNumber,
            String originCity,
            String destinationCity,
            String startDate,
            String tripType,
            int adultCount,
            int infantCount
    ) throws ExecutionException, InterruptedException {
        Unm<T> unm = new Unm<>(session);
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
        String accessToken = authData.accessToken;
        String refreshToken = authData.refreshToken;

        TrainSearchApi.Inputs inputSearch = new TrainSearchApi.Inputs(originCity, destinationCity, startDate, tripType, adultCount, infantCount, accessToken, refreshToken);
        TrainSearchApi trainSearchApi = new TrainSearchApi(inputSearch);
        Gson gson = new Gson();

        int maxSearchAttempts = 3;
        TrainSearchResponse searchResponse = null;
        String lastSearchFailure = null;
        for (int attempt = 1; attempt <= maxSearchAttempts; attempt++) {
            try {
                ApiResponse trainSearchApiResponse = trainSearchApi.hitApi(apiTools);
                TrainSearchResponse parsed = gson.fromJson(trainSearchApiResponse.getBody(), TrainSearchResponse.class);
                if (parsed != null && parsed.data != null && parsed.data.departJourneys != null) {
                    searchResponse = parsed;
                    break;
                }
                lastSearchFailure = "[" + trainSearchApiResponse.getStatusCode() + "] " + trainSearchApiResponse.getBody();
                logger.softWarning("Train search attempt " + attempt + "/" + maxSearchAttempts
                        + " returned no journey data: " + lastSearchFailure);
            } catch (ApiEnvironmentException e) {
                lastSearchFailure = e.getMessage();
                logger.softWarning("Train search attempt " + attempt + "/" + maxSearchAttempts
                        + " hit a transient environment error: " + lastSearchFailure);
            }
            if (attempt < maxSearchAttempts) {
                TimeUnit.SECONDS.sleep(5);
            }
        }

        if (searchResponse == null) {
            Assertion.skip("Skipping Train test: train search did not return usable journey data for "
                    + originCity + " -> " + destinationCity + " on " + startDate + " after " + maxSearchAttempts
                    + " attempts (" + lastSearchFailure + "). This is a Train-side availability/backend issue, "
                    + "not an Eiffel test defect.");
        }
        if (searchResponse.data.departJourneys.count == 0
                || searchResponse.data.departJourneys.journeys == null
                || searchResponse.data.departJourneys.journeys.isEmpty()) {
            Assertion.skip("Skipping Train test: train search returned no journeys for "
                    + originCity + " -> " + destinationCity + " on " + startDate
                    + ". This is a Train-side availability/data issue (try another date or stations), "
                    + "not an Eiffel test defect.");
        }
        String trainJourneyCode = searchResponse.data.departJourneys.journeys.get(0).id;
        logger.log("Available journeys: " + trainJourneyCode);

        int maxOrderAttempts = 3;
        String cartId = null;
        String cartSecret = null;
        String lastOrderResponseBody = null;
        for (int attempt = 1; attempt <= maxOrderAttempts; attempt++) {
            try {
                TrainCartApi.Inputs inputCart = new TrainCartApi.Inputs(accessToken, trainJourneyCode, adultCount, infantCount);
                ApiResponse trainCartApiResponse = new TrainCartApi(inputCart).hitApi(apiTools);
                TrainCartResponse cartResponse = gson.fromJson(trainCartApiResponse.getBody(), TrainCartResponse.class);
                if (cartResponse == null || cartResponse.data == null
                        || cartResponse.data.id == null || cartResponse.data.id.isBlank()
                        || cartResponse.data.secret == null || cartResponse.data.secret.isBlank()) {
                    logger.softWarning("Train cart attempt " + attempt + "/" + maxOrderAttempts
                            + " returned no usable cart: " + trainCartApiResponse.getBody());
                    if (attempt < maxOrderAttempts) {
                        TimeUnit.SECONDS.sleep(5);
                    }
                    continue;
                }
                String id = cartResponse.data.id;
                String secret = cartResponse.data.secret;
                logger.log("Cart ID: " + id + ", Secret: " + secret + ", State: " + cartResponse.data.state);
                TimeUnit.SECONDS.sleep(5);

                TrainOrderApi.Inputs inputBook = new TrainOrderApi.Inputs(accessToken, id, secret, name, phone, email, idNumber, true);
                ApiResponse trainOrderApiResponse = new TrainOrderApi(inputBook).hitApi(apiTools);
                lastOrderResponseBody = trainOrderApiResponse.getBody();
                logger.log("Train order response (attempt " + attempt + "/" + maxOrderAttempts + ") ["
                        + trainOrderApiResponse.getStatusCode() + "]: " + lastOrderResponseBody);

                TrainOrderResponse res = gson.fromJson(lastOrderResponseBody, TrainOrderResponse.class);
                if (res != null && res.data != null && res.data.order != null) {
                    cartId = id;
                    cartSecret = secret;
                    break;
                }
                logger.softWarning("Train order attempt " + attempt + "/" + maxOrderAttempts + " returned no order.");
            } catch (ApiEnvironmentException e) {
                lastOrderResponseBody = e.getMessage();
                logger.softWarning("Train cart/order attempt " + attempt + "/" + maxOrderAttempts
                        + " hit a transient environment error: " + e.getMessage());
            }
            if (attempt < maxOrderAttempts) {
                TimeUnit.SECONDS.sleep(5);
            }
        }

        if (cartId == null) {
            Assertion.skip("Skipping Train test: could not create a v6 train order after " + maxOrderAttempts
                    + " attempts (cart/order stage). Train API reason: "
                    + describeTrainOrderError(lastOrderResponseBody)
                    + ". This is a Train-side availability/backend issue, not an Eiffel test defect.");
        }

        int maxCheckoutAttempts = 3;
        TrainOrderResponse checkout = null;
        String lastCheckoutResponseBody = null;
        for (int attempt = 1; attempt <= maxCheckoutAttempts; attempt++) {
            try {
                ApiResponse checkoutResponse = new TrainOrderV7Api(
                        new TrainOrderV7Api.Inputs(accessToken, cartId, cartSecret)).hitApi(apiTools);
                lastCheckoutResponseBody = checkoutResponse.getBody();
                logger.log("Train checkout (v7) response (attempt " + attempt + "/" + maxCheckoutAttempts + ") ["
                        + checkoutResponse.getStatusCode() + "]: " + lastCheckoutResponseBody);

                TrainOrderResponse parsed = gson.fromJson(lastCheckoutResponseBody, TrainOrderResponse.class);
                if (parsed != null && parsed.data != null && parsed.data.order != null
                        && parsed.data.order.id != 0 && parsed.data.price != null
                        && parsed.data.price.newPrice != null) {
                    checkout = parsed;
                    break;
                }
                logger.softWarning("Train checkout (v7) attempt " + attempt + "/" + maxCheckoutAttempts
                        + " did not return a valid monolith order.");
            } catch (ApiEnvironmentException e) {
                lastCheckoutResponseBody = e.getMessage();
                logger.softWarning("Train checkout (v7) attempt " + attempt + "/" + maxCheckoutAttempts
                        + " hit a transient environment error: " + e.getMessage());
            }
            if (attempt < maxCheckoutAttempts) {
                TimeUnit.SECONDS.sleep(3);
            }
        }

        if (checkout == null) {
            Assertion.skip("Skipping Train test: v7 checkout did not return a valid monolith order after "
                    + maxCheckoutAttempts + " attempts. Train API reason: "
                    + describeTrainOrderError(lastCheckoutResponseBody)
                    + ". This is a Train-side/backend issue, not an Eiffel test defect.");
        }

        String orderId = String.valueOf(checkout.data.order.id);
        String orderHash = checkout.data.order.hash;
        String orderPrice = checkout.data.price.newPrice.toBigInteger().toString();

        logger.log("Train order checked out -> state: " + checkout.data.state
                + ", orderId: " + orderId + ", orderHash: " + orderHash + ", price: " + orderPrice);

        return Map.of(
                "orderHash", orderHash,
                "orderPrice", orderPrice,
                "orderId", orderId
        );
    }

    /**
     * Builds a human-readable reason from a Train order/checkout response body. The Train APIs return
     * HTTP 200 {@code SUCCESS} even when the order is refused, carrying the real reason inside
     * {@code data.errorData} (e.g. {@code NO_AVAILABLE_SEATS} plus a localized title). Surfacing that
     * lets a skipped run explain <em>why</em> the Train team's API refused the order instead of dumping
     * the raw payload. Falls back to the raw body when no {@code errorData} is present.
     */
    private String describeTrainOrderError(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return "no order response (cart never became orderable)";
        }
        try {
            JSONObject data = new JSONObject(responseBody).optJSONObject("data");
            if (data != null && !data.isNull("errorData")) {
                JSONObject errorData = data.getJSONObject("errorData");
                String errorCode = errorData.optString("errorCode", "UNKNOWN");
                String title = errorData.isNull("message")
                        ? ""
                        : errorData.getJSONObject("message").optString("title", "");
                return errorCode + (title.isBlank() ? "" : " - \"" + title + "\"");
            }
        } catch (JSONException e) {
            logger.softWarning("Could not parse Train order error body: " + e.getMessage());
        }
        return responseBody;
    }

    public Map<String, String> bookBus(
            String email,
            String password,
            String originId,
            String destinationId,
            String departureDate,
            String profileId
    ) {
        Unm<T> unm = new Unm<>(session);
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
        String accessToken = authData.accessToken;
        logger.log("access token : " + accessToken.substring(0, 4) + "****");

        logger.log("loginStatus-> " + email + ", " + authData.accountUsername.equalsIgnoreCase(email));
        String fullName = authData.accountFirstName + " " + authData.accountLastName;
        String phone = authData.accountPhoneNumber;
        int adult = 1;

        BusSearchApi.Inputs busSearchApiInputs = new BusSearchApi.Inputs(accessToken, originId, destinationId, departureDate, adult);
        BusSearchApi busSearchApi = new BusSearchApi(busSearchApiInputs);
        ApiResponse apiResponse = busSearchApi.hitApi(apiTools);
        Gson gson = new Gson();
        BusSearchResponse busSearchResponse = gson.fromJson(apiResponse.getBody(), BusSearchResponse.class);
        logger.log("BusSearchApi-> " + busSearchResponse.message);
        String scheduleId = busSearchResponse.data.schedules.get(0).id;
        String scheduleHash = busSearchResponse.data.schedules.get(0).hash;

        BusSeatLayoutApi.Inputs busSeatLayoutApiInputs = new BusSeatLayoutApi.Inputs(accessToken, scheduleId, adult);
        BusSeatLayoutApi busSeatLayoutApi = new BusSeatLayoutApi(busSeatLayoutApiInputs);
        ApiResponse seatRes = busSeatLayoutApi.hitApi(apiTools);
        BusSeatLayoutResponse busSeatLayoutResponse = gson.fromJson(seatRes.getBody(), BusSeatLayoutResponse.class);
        logger.log("BusSeatLayoutApi-> " + busSeatLayoutResponse.message);

        String seatName = busSeatLayoutResponse.data.seatMap.get(0).rows.stream()
                .filter(rows -> rows.available)
                .findFirst()
                .orElseThrow()
                .name;

        BusCartsApi.Inputs busCartsApiInputs = new BusCartsApi.Inputs(accessToken, scheduleId, scheduleHash, seatName, adult);
        BusCartsApi busCartsApi = new BusCartsApi(busCartsApiInputs);
        ApiResponse cartRes = busCartsApi.hitApi(apiTools);
        BusCartsResponse busCartsResponse = gson.fromJson(cartRes.getBody(), BusCartsResponse.class);
        logger.log("BusCartsApi-> " + busCartsResponse.message);

        String cartId = busCartsResponse.data.cartId;
        String cartSecret = busCartsResponse.data.secret;
        int totalPrice = busCartsResponse.data.priceDetail.totalPrice;

        BusCheckoutApi.Inputs busCheckoutApiInputs = new BusCheckoutApi.Inputs(accessToken, cartId, cartSecret, fullName, phone, email, profileId, totalPrice, seatName);
        BusCheckoutApi busCheckoutApi = new BusCheckoutApi(busCheckoutApiInputs);
        ApiResponse checkOutRes = busCheckoutApi.hitApi(apiTools);
        BusCheckoutResponse busCheckoutResponse = null;
        try {
            busCheckoutResponse = gson.fromJson(checkOutRes.getBody(), BusCheckoutResponse.class);
            logger.log("BusCheckoutApi-> " + busCheckoutResponse.message);
            return Map.of(
                    "orderId", busCheckoutResponse.data.order.orderId,
                    "orderHash", busCheckoutResponse.data.order.orderHash,
                    "totalPrice", String.valueOf(busCheckoutResponse.data.totalFare)
            );
        } catch (Exception e) {
            logger.softWarning("Failure response:\n" + checkOutRes.getBody());
        }
        return null;
    }

    public void payWithTixPoints(int currentPoints, int pricePayment) {
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        if (this.paymentType.equalsIgnoreCase("full") && currentPoints < pricePayment) {
            Assertion.fail("Current points after inject is lower than total price after inject. " +
                    "currentPointsAfterInject: " + currentPoints +
                    ", totalPriceAfterInject: " + pricePayment);
        } else if (this.paymentType.equalsIgnoreCase("half") && (currentPoints == 0 || currentPoints > pricePayment)) {
            Assertion.fail("Current points is 0 or bigger than final price, but payment type is half. " +
                    "currentPointsAfterInject: " + currentPoints);
        }
        priceFromPaymentPage = paymentPage.getFinalPrice();
        paymentPage.clickPayWithTiketPoints();
        driverTools.waitForElementToAppear(RELOAD_BUTTON, 5).ifPresent(element -> {
            driverTools.click(element);
            driverTools.waitForElementToDisappear(element, 3);
            paymentPage.clickPayWithTiketPoints();
        });

        if(this.paymentType.equalsIgnoreCase("half")) {
            paymentPage.clickOnSelectPaymentMethod();
            paymentPage.goToPaymentMethodsGroup("Instant Payment");
            paymentPage.selectPaymentMethod("Sakuku");
        }

        paymentPage.clickOnPaymentButton();
    }

    public Map<String, String> bookTTDViaUI(String productName, String packageName, int ticketQty) {
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        ttdLandingPageWeb.goToTtdLandingPage();
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);
        selectTtdPackage(ttdProductDetailPageWeb, packageName);
        ttdProductDetailPageWeb.setTicketQty(ticketQty, 0);
        ttdProductDetailPageWeb.clickBookButton();
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();
        ttdBookingFormPageWeb.continueToPaymentPage();
        if (!paymentPage.isPaymentPageLoaded()) {
            Assertion.fail("Payment page did not load after TTD UI booking.");
        }

        String orderId = paymentPage.getOrderId();
        int totalPrice = getFinalPriceValue(paymentPage);
        return Map.of("orderId", orderId, "totalPrice", String.valueOf(totalPrice));
    }

    public Map<String, String> bookTTDAndPayViaUI(
            String productName,
            String packageName,
            int ticketQty,
            String paymentMethodGroup,
            String paymentMethod
    ) {
        TtdLandingPageWeb<?> ttdLandingPageWeb = TtdLandingPageWeb.getInstance(session);
        TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
        TtdBookingFormPageWeb<?> ttdBookingFormPageWeb = TtdBookingFormPageWeb.getInstance(session);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        ttdLandingPageWeb.goToTtdLandingPage();
        ttdLandingPageWeb.inputSearchKeyword(productName);
        ttdLandingPageWeb.selectRecommendedActivity(productName);
        selectTtdPackage(ttdProductDetailPageWeb, packageName);
        ttdProductDetailPageWeb.setTicketQty(ticketQty, 0);
        ttdProductDetailPageWeb.clickBookButton();
        ttdBookingFormPageWeb.fillContactOrderDetail(true);
        ttdBookingFormPageWeb.fillGuestDetail();
        ttdBookingFormPageWeb.continueToPaymentPage();
        if (!paymentPage.isPaymentPageLoaded()) {
            Assertion.fail("Payment page did not load after TTD UI booking.");
        }

        String orderId = paymentPage.getOrderId();
        String earningPoints = paymentPage.isEarningPointsVisibleOnPaymentPage()
                ? paymentPage.getEarningPointsFromPaymentPage()
                : "0";

        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentMethodGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();

        return Map.of("orderId", orderId, "earningPoints", earningPoints);
    }

    public Map<String, String> bookTTDAndPayWithPromoViaUI(
            String productName,
            String packageName,
            int ticketQty,
            String promoCode,
            String paymentMethodGroup,
            String paymentMethod
    ) {
        Map<String, String> bookingData = bookTTDViaUI(productName, packageName, ticketQty);
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);

        paymentPage.applyAnyPromo(promoCode);
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentMethodGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();

        return Map.of("orderId", bookingData.get("orderId"));
    }

    /**
     * Creates a ToDo (TTD) order via API, then opens that order's payment page on the UI to apply a promo
     * and pay with the given payment method.
     * <p>This is the hybrid counterpart of {@link #bookTTDAndPayWithPromoViaUI}. The ToDo search / PDP /
     * booking-form UI is not what such a test asserts on, and driving it is the flakiest part of the run, so
     * the order itself is created through {@link #bookTTD}. The promo is still applied on the payment page
     * UI, which keeps {@code promoCode:any} working (the promo drawer picks the first eligible promo) and
     * keeps the promo application itself under test.
     * <p>The order books enough tickets to reach {@link #PROMO_MIN_ORDER_TOTAL}, so a cheap price tier does not
     * leave the order below the promo's minimum price after discount.
     * <p>The caller must already be logged in on the browser as {@code username}: the payment page is opened
     * with the live browser session.
     *
     * @param paymentMethod the payment method label as it appears in the payment-method drawer; defaults to
     *                      {@code Sakuku} when blank.
     * @return the created order's {@code orderId}, {@code orderHash} and {@code totalPrice} (before discount).
     */
    @SneakyThrows
    public Map<String, String> bookTTDViaApiAndPayWithPromoOnUi(
            String username,
            String password,
            String productUrlName,
            String productPackages,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation,
            int dateRange,
            int monthRange,
            String promoCode,
            String paymentMethod
    ) {
        Map<String, String> orderData = bookTTD(username, password, productUrlName, productPackages,
                guestFullName, guestEmailAddress, guestSalutation,
                dateRange, monthRange, false, PROMO_MIN_ORDER_TOTAL);
        String orderId = orderData.get("orderId");
        String orderHash = orderData.get("orderHash");

        goToPaymentPage(orderId, orderHash);

        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        paymentPage.applyAnyPromo(promoCode);

        // Pick the method straight out of the "see all payment methods" drawer instead of first scrolling to
        // its group: the group heading is localized ("Instant Payment" / "Pembayaran Instan"), so a
        // sheet-driven group name breaks whenever the payment page renders in the other language, while the
        // method name itself ("Sakuku") is the same in both.
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.selectPaymentMethod(
                paymentMethod == null || paymentMethod.isBlank() ? "Sakuku" : paymentMethod);
        paymentPage.clickOnPaymentButton();

        return Map.of("orderId", orderId,
                "orderHash", orderHash,
                "totalPrice", orderData.get("totalPrice"));
    }

    /**
     * Creates a ToDo (TTD) order via API, then opens that order's payment page on the UI to read the earning
     * points and pay with the given payment method.
     * <p>This is the hybrid counterpart of {@link #bookTTDAndPayViaUI}, used by pending-earning-point tests:
     * those assert on the loyalty pending-list API, not on the ToDo search / PDP / booking-form UI, and
     * driving that UI is the flakiest part of the run, so the order itself is created through
     * {@link #bookTTD}. Reading the earning points and paying still happen on the payment page UI, because
     * the points shown there are what the pending-point assertion compares against.
     * <p>The caller must already be logged in on the browser as {@code username}: the payment page is opened
     * with the live browser session.
     *
     * @param paymentMethod the payment method label as it appears in the payment-method drawer; defaults to
     *                      {@code Sakuku} when blank.
     * @return the created order's {@code orderId}, {@code orderHash}, {@code totalPrice} and the
     *         {@code earningPoints} shown on the payment page before paying.
     */
    @SneakyThrows
    public Map<String, String> bookTTDViaApiAndPayOnUi(
            String username,
            String password,
            String productUrlName,
            String productPackages,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation,
            int dateRange,
            int monthRange,
            String paymentMethod
    ) {
        Map<String, String> orderData = bookTTD(username, password, productUrlName, productPackages,
                guestFullName, guestEmailAddress, guestSalutation,
                dateRange, monthRange, false);
        String orderId = orderData.get("orderId");
        String orderHash = orderData.get("orderHash");

        goToPaymentPage(orderId, orderHash);

        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        String earningPoints = paymentPage.isEarningPointsVisibleOnPaymentPage()
                ? paymentPage.getEarningPointsFromPaymentPage()
                : "0";

        // Pick the method straight out of the "see all payment methods" drawer instead of first scrolling to
        // its group: the group heading is localized ("Instant Payment" / "Pembayaran Instan"), so a
        // sheet-driven group name breaks whenever the payment page renders in the other language, while the
        // method name itself ("Sakuku") is the same in both.
        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.selectPaymentMethod(
                paymentMethod == null || paymentMethod.isBlank() ? "Sakuku" : paymentMethod);
        paymentPage.clickOnPaymentButton();

        return Map.of("orderId", orderId,
                "orderHash", orderHash,
                "totalPrice", orderData.get("totalPrice"),
                "earningPoints", earningPoints);
    }

    private void selectTtdPackage(TtdProductDetailPageWeb<?> ttdProductDetailPageWeb, String packageName) {
        if (packageName == null || packageName.isBlank()
                || packageName.equalsIgnoreCase("first")
                || packageName.equalsIgnoreCase("firstAvailable")) {
            ttdProductDetailPageWeb.chooseTicket(1);
        } else if (packageName.matches("\\d+")) {
            ttdProductDetailPageWeb.chooseTicket(Integer.parseInt(packageName));
        } else {
            ttdProductDetailPageWeb.chooseTicket(packageName);
        }
    }

    public void ensurePointsExceedPrice(int price) {
        int currentPoints = getCurrentLoyaltyPoints();
        if (currentPoints <= price) {
            logger.softWarning("Current points are not higher than order price. Injecting excess points.");
            addActivePoints((price - currentPoints) + 10000);
        }
    }

    /**
     * {@link PaymentPageWeb#getFinalPrice()} already returns a normalized numeric string (e.g. "97500.0"),
     * because it runs {@link CoreFunctions#convertPriceSafely(String)} internally. Running convertPriceSafely
     * on that result a second time double-converts it: the '.' decimal separator is treated as an Indonesian
     * thousands separator and stripped, adding a spurious trailing zero (97500 -> 975000). Parse the
     * already-normalized string directly instead. Kept on the loyalty page so the payment page is untouched.
     */
    private int getFinalPriceValue(PaymentPageWeb<?> paymentPage) {
        return (int) Double.parseDouble(paymentPage.getFinalPrice());
    }

    public void payWithTixPointsCapped(int totalPrice, int pointsCapPercentage, String paymentMethodGroup, String paymentMethod) {
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        this.paymentType = "half";

        int priceBeforePoints = getFinalPriceValue(paymentPage);
        priceFromPaymentPage = paymentPage.getFinalPrice();
        driverTools.waitForElementToPresence(TIKET_POINT_RADIO)
                .orElseThrow(() -> new AssertionError(
                        "Tiket-point toggle (btnApplyTiketPoints) never appeared on the payment page."));
        driverTools.clickWithJavascriptExecutor(TIKET_POINT_RADIO);
        driverTools.waitForElementToAppear(RELOAD_BUTTON, 5).ifPresent(element -> {
            driverTools.click(element);
            driverTools.waitForElementToDisappear(element, 3);
            driverTools.clickWithJavascriptExecutor(TIKET_POINT_RADIO);
        });

        Assertion.assertThat("User cannot pay full order price with tiket points", paymentPage.isOrderFree(), is(false));

        int priceAfterPoints = getFinalPriceValue(paymentPage);
        int pointsUsed = priceBeforePoints - priceAfterPoints;
        int maxUsablePoints = (totalPrice * pointsCapPercentage) / 100;
        logger.log("priceBeforePoints: " + priceBeforePoints + ", priceAfterPoints: " + priceAfterPoints
                + ", pointsUsed: " + pointsUsed + ", maxUsablePoints: " + maxUsablePoints);

        Assertion.assertThat("Tiket points used should not exceed configurable cap percentage",
                pointsUsed, lessThanOrEqualTo(maxUsablePoints + 1));
        Assertion.assertThat("Remaining price should require another payment method",
                priceAfterPoints, greaterThan(0));

        paymentPage.clickOnSelectPaymentMethod();
        paymentPage.goToPaymentMethodsGroup(paymentMethodGroup);
        paymentPage.selectPaymentMethod(paymentMethod);
        paymentPage.clickOnPaymentButton();
    }

    public boolean isPaymentSuccessful() {
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        return paymentPage.isPaymentSuccessfulVisible();
    }

    /**
     * Reads the account's current active tiket points. The browser {@code session_access_token} cookie
     * is a web session JWT that carries only standard claims (no {@code accountid}/{@code email}), so
     * the account identity is resolved via the member API (same pattern as {@link #addActivePoints})
     * rather than by decoding the token locally.
     */
    public int getCurrentLoyaltyPoints() {
        accessToken = driverTools.getCookieValue("session_access_token");
        if (accessToken == null) {
            throw new ApiToolsException("No session_access_token cookie: the browser is not logged in.");
        }
        // A guest token carries no accountid/email, so the member API answers with an empty payload and
        // the failure would otherwise read as a backend problem instead of a lost login.
        if (!Unm.isMemberToken(accessToken)) {
            throw new ApiToolsException("session_access_token is a guest token: the member session was lost before reading points.");
        }
        Gson gson = new Gson();
        AccountData accountData = gson.fromJson(
                new GetAccountDataApi(new GetAccountDataApi.Inputs("Bearer " + accessToken)).hitApi(apiTools).getBody(),
                AccountData.class);
        if (accountData == null || accountData.data == null) {
            throw new ApiToolsException("Unable to resolve account data for active points read.");
        }
        this.accountId = accountData.data.accountId;
        ActiveTiketPointsApi activeTiketPointsApi = new ActiveTiketPointsApi(
                new ActiveTiketPointsApi.Inputs(accountData.data.accountId, accountData.data.accountUsername)
        );
        int maxAttempts = 3;
        String lastFailure = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                ApiResponse response = activeTiketPointsApi.hitApi(apiTools);
                String body = response.getBody();
                ActiveTiketPointsResponse activeTiketPointsResponse =
                        gson.fromJson(body, ActiveTiketPointsResponse.class);
                if (activeTiketPointsResponse != null && activeTiketPointsResponse.data != null
                        && activeTiketPointsResponse.data.trim().matches("-?\\d+")) {
                    return Integer.parseInt(activeTiketPointsResponse.data.trim());
                }
                lastFailure = "[" + response.getStatusCode() + "] " + body;
                logger.softWarning("Active points read attempt " + attempt + "/" + maxAttempts
                        + " returned no numeric points: " + lastFailure);
            } catch (ApiEnvironmentException e) {
                lastFailure = e.getMessage();
                logger.softWarning("Active points read attempt " + attempt + "/" + maxAttempts
                        + " hit a transient environment error: " + lastFailure);
            }
            if (attempt < maxAttempts) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ApiToolsException("Interrupted while retrying active points read", e);
                }
            }
        }
        throw new ApiToolsException("Failed to read active tiket points after " + maxAttempts
                + " attempts. Last failure: " + lastFailure);
    }

    private void addActivePoints(int points) {
        GetAccountDataApi getAccountDataApi = new GetAccountDataApi(
                new GetAccountDataApi.Inputs("Bearer " + accessToken)
        );

        AccountData accountData = new Gson().fromJson(
                getAccountDataApi.hitApi(apiTools).getBody(),
                AccountData.class
        );

        accountId = accountData.data.accountId;
        AddActivePointsApi addActivePointsApi = new AddActivePointsApi(new AddActivePointsApi.Inputs(
                Math.toIntExact(accountId),
                points,
                "REWARD",
                1,
                accountData.data.accountUsername
        ));

        ApiResponse response = addActivePointsApi.hitApi(apiTools);
        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Failed to add active points:\n" + response.getBody());
        }
    }

    public void verifyLoyaltyPointsHistory(String orderId) {
        if (accountId == null) {
            GetAccountDataApi getAccountDataApi = new GetAccountDataApi(
                    new GetAccountDataApi.Inputs("Bearer " + accessToken)
            );

            AccountData accountData = new Gson().fromJson(
                    getAccountDataApi.hitApi(apiTools).getBody(),
                    AccountData.class
            );
            accountId = accountData.data.accountId;
        }

        JsonObject orderIdData = findOrderInPointHistory(orderId);
        if (orderIdData == null) {
            Assertion.fail("Order id " + orderId + " was not found in the loyalty point history after "
                    + POINT_HISTORY_MAX_ATTEMPTS + " attempts (~"
                    + (POINT_HISTORY_MAX_ATTEMPTS * POINT_HISTORY_RETRY_DELAY_SECONDS) + "s).");
        }
        pointHistoryData.put("orderId", getSafeString(orderIdData, "orderId"));
        pointHistoryData.put("point", orderIdData.get("point").getAsInt());
        pointHistoryData.put("pointStatus", getSafeString(orderIdData, "pointStatus"));
        logger.log(pointHistoryData.toString());
    }

    /**
     * Point history is written asynchronously after the payment succeeds, so the first read can
     * legitimately miss the order. Poll until the order shows up or the attempts run out.
     */
    private JsonObject findOrderInPointHistory(String orderId) {
        PointHistoryApi pointHistoryApi = new PointHistoryApi(new PointHistoryApi.Inputs(Math.toIntExact(accountId)));
        Gson gson = new Gson();

        for (int attempt = 1; attempt <= POINT_HISTORY_MAX_ATTEMPTS; attempt++) {
            ApiResponse response = pointHistoryApi.hitApi(apiTools);
            JsonObject pointHistoryResponse = gson.fromJson(response.getBody(), JsonObject.class);
            JsonObject data = pointHistoryResponse == null ? null : pointHistoryResponse.getAsJsonObject("data");
            JsonArray pointHistoryContent = data == null ? null : data.getAsJsonArray("content");

            if (pointHistoryContent != null) {
                for (JsonElement jsonElement : pointHistoryContent) {
                    JsonObject item = jsonElement.getAsJsonObject();
                    if (item.has("orderId") && !item.get("orderId").isJsonNull()
                            && item.get("orderId").getAsString().equals(orderId)) {
                        return item;
                    }
                }
            }

            logger.softWarning("Point history attempt " + attempt + "/" + POINT_HISTORY_MAX_ATTEMPTS
                    + " does not contain order id " + orderId + " yet");

            if (attempt < POINT_HISTORY_MAX_ATTEMPTS) {
                try {
                    TimeUnit.SECONDS.sleep(POINT_HISTORY_RETRY_DELAY_SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ApiToolsException("Interrupted while waiting for point history", e);
                }
            }
        }
        return null;
    }

    public void verifyPaymentAndLoyaltyPointsHistory(String orderId, int price) {
        boolean isSuccess = isPaymentSuccessful();
        Assertion.assertThat("Verify pay using full points is successful", isSuccess, is(true));

        verifyLoyaltyPointsHistory(orderId);
        Assertion.assertThat("Verify order id is found in points history", pointHistoryData.get("orderId"), is(orderId));
        Assertion.assertThat("Verify pointStatus is Redeemed", pointHistoryData.get("pointStatus"), is("Redeemed"));

        int point = (int) pointHistoryData.get("point");
        if (!"half".equalsIgnoreCase(this.paymentType)) {
            Assertion.assertThat("Verify point is the same as price but negative", point, is(-price));
        } else {
            Assertion.assertThat("Verify point is negative", point, lessThan(0));
            Assertion.assertThat("Verify absolute point reduced is lower than totalPrice", Math.abs(point), lessThan(price));
        }
    }

    private String getSafeString(JsonObject jsonObject, String key) {
        if(jsonObject.has(key) && !jsonObject.get(key).isJsonNull()) {
            return jsonObject.get(key).getAsString();
        }
        return "";
    }

    public void goToPaymentPage(String orderId, String orderHash) {
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        paymentPage.goToPaymentPage(orderId, orderHash);
        if (!paymentPage.isPaymentPageLoaded()) {
            Assertion.fail("Payment page did not load successfully. Please check the order ID and hash.");
        }
    }

    /**
     * TTD API booking flow. The order is created through {@code v2/orders} ({@link TTDCreateOrderV2Api}) because
     * the legacy {@code v3/orders} endpoint now rejects every request with "orders: must not be null". The v2
     * payload has no insurance add-on, so {@code insurance=true} is unsupported.
     */
    public Map<String, String> bookTTD(
            String username,
            String password,
            String productUrlName,
            String productPackages,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation,
            int dateRange,
            int monthRange,
            boolean insurance
    ) {
        return bookTTD(username, password, productUrlName, productPackages,
                guestFullName, guestEmailAddress, guestSalutation, dateRange, monthRange, insurance, 0);
    }

    /**
     * Same as {@link #bookTTD(String, String, String, String, String, String, String, int, int, boolean)},
     * but books as many tickets of the selected price tier as needed for the order total to reach
     * {@code minOrderTotal}. Use it when the order must clear a promo's minimum price: a single ticket of a cheap
     * tier (e.g. CHILD at IDR 16,000) can fall below a promo's "price after promo must be above 10,000" rule.
     *
     * @param minOrderTotal minimum order total in IDR; {@code 0} or less books a single ticket.
     */
    @SneakyThrows
    public Map<String, String> bookTTD(
            String username,
            String password,
            String productUrlName,
            String productPackages,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation,
            int dateRange,
            int monthRange,
            boolean insurance,
            long minOrderTotal
    ) {
        Unm<T> unm = new Unm<>(session);
        LoginDataUnmResponse.Data loginData = unm.unmLoginApi(username, password);
        String accessToken = loginData.accessToken;
        logger.log("accessToken-> " + accessToken);

        TTDSelectProductApi.Inputs ttdProductSelectApiInputs = new TTDSelectProductApi.Inputs(accessToken, productUrlName);
        JSONObject productData = ttdSelectProductData(ttdProductSelectApiInputs);
        String selectedProductID = productData.getString("id");
        String productName = productData.getJSONArray("translations").getJSONObject(0).getString("title");
        logger.log("selectedProductID-> " + selectedProductID);
        logger.log("selectedProductName-> " + productName);

        productPackages = resolvePackageCode(productData, accessToken, selectedProductID, productPackages);
        logger.log("PackageID-> " + productPackages);

        TTDChoosePackageScheduleApi.Inputs ttdChoosePackageScheduleApiInputs = new TTDChoosePackageScheduleApi.Inputs(accessToken, selectedProductID, productPackages, dateRange, monthRange);
        String selectedPackageScheduleID = ttdChoosePackageScheduleApi(ttdChoosePackageScheduleApiInputs);
        logger.log("PackageScheduleID-> " + selectedPackageScheduleID);

        return createTTDOrder(loginData, productData, productPackages, selectedPackageScheduleID,
                guestFullName, guestEmailAddress, guestSalutation, insurance, minOrderTotal);
    }

    /**
     * Books one ticket of a <b>refundable</b> ToDo package through the API, with a visit date at least
     * {@code minDaysFromToday} days after today (Asia/Jakarta). Use it when a test refunds the order later: most
     * ToDo packages are not refundable, and a refund is only accepted for a visit date far enough ahead.
     * <p>The package is matched by its {@code code} or its English name (e.g. {@code "Refundable Rates"}) and must be
     * flagged {@code isRefundable}; package names are not unique, so a same-named non-refundable package is ignored.
     * The case is skipped if no such package, or no schedule from that date onwards, exists.
     *
     * @param packageName      package code or English package name
     * @param minDaysFromToday earliest visit date, in days after today
     * @return the created order's {@code orderId}, {@code orderHash} and {@code totalPrice}
     */
    @SneakyThrows
    public Map<String, String> bookRefundableTTD(
            String username,
            String password,
            String productUrlName,
            String packageName,
            String guestFullName,
            String guestSalutation,
            int minDaysFromToday
    ) {
        Unm<T> unm = new Unm<>(session);
        LoginDataUnmResponse.Data loginData = unm.unmLoginApi(username, password);
        String accessToken = loginData.accessToken;

        JSONObject productData = ttdSelectProductData(new TTDSelectProductApi.Inputs(accessToken, productUrlName));
        String productId = productData.getString("id");
        String packageCode = resolveRefundablePackageCode(productData, productUrlName, packageName);
        logger.log("Refundable package '" + packageName + "' -> code " + packageCode + " (product " + productId + ")");

        LocalDate earliestVisitDate = LocalDate.now(ZoneId.of("Asia/Jakarta")).plusDays(minDaysFromToday);
        String scheduleId = ttdChooseScheduleOnOrAfter(accessToken, productId, packageCode, earliestVisitDate);

        return createTTDOrder(loginData, productData, packageCode, scheduleId,
                guestFullName, username, guestSalutation, false, 0);
    }

    /**
     * Returns the {@code code} of the package matching {@code packageName} (by code or English name) that is
     * flagged {@code isRefundable}; skips the case when the product has none.
     */
    private String resolveRefundablePackageCode(JSONObject productData, String productUrlName, String packageName) {
        String wanted = packageName == null ? "" : packageName.trim();
        List<String> refundablePackages = new ArrayList<>();
        for (Object item : productData.getJSONArray("packages")) {
            if (!(item instanceof JSONObject packageJson) || !packageJson.optBoolean("isRefundable", false)) {
                continue;
            }
            String code = packageJson.optString("code");
            List<String> names = new ArrayList<>();
            JSONArray translations = packageJson.optJSONArray("translations");
            for (int i = 0; translations != null && i < translations.length(); i++) {
                names.add(translations.getJSONObject(i).optString("name"));
            }
            refundablePackages.add(code + " " + names);
            if (code.equalsIgnoreCase(wanted) || names.stream().anyMatch(wanted::equalsIgnoreCase)) {
                return code;
            }
        }
        Assertion.skip("ToDo product '" + productUrlName + "' has no refundable package '" + packageName
                + "'. Refundable packages: " + refundablePackages + ". Skipping as a test-data issue.");
        return null;
    }

    /**
     * Picks the earliest schedule of the package whose visit {@code date} is on or after {@code earliestVisitDate}
     * (searching one year ahead); skips the case when there is none.
     */
    @SneakyThrows
    private String ttdChooseScheduleOnOrAfter(String accessToken, String productId, String packageCode,
                                              LocalDate earliestVisitDate) {
        TTDChoosePackageScheduleApi.Inputs inputs = new TTDChoosePackageScheduleApi.Inputs(accessToken, productId,
                packageCode, 0, 0, earliestVisitDate.toString(), earliestVisitDate.plusYears(1).toString());
        String responseBody = RetryUtils.retryWithExponentialBackoff(TTD_API_RETRY_ATTEMPTS, TTD_API_RETRY_BASE_DELAY_MS,
                () -> {
                    String body = new TTDChoosePackageScheduleApi(inputs).hitApi(apiTools).getBody();
                    new JSONObject(body);
                    return body;
                },
                e -> e instanceof ApiEnvironmentException || e instanceof JSONException,
                "Read ToDo schedules of package " + packageCode);
        JSONArray schedules = new JSONObject(responseBody).optJSONArray("data");
        JSONObject earliest = null;
        LocalDate earliestDate = null;
        for (int i = 0; schedules != null && i < schedules.length(); i++) {
            JSONObject schedule = schedules.getJSONObject(i);
            String date = schedule.optString("date", "");
            if (date.isBlank()) {
                continue;
            }
            LocalDate visitDate = LocalDate.parse(date);
            if (!visitDate.isBefore(earliestVisitDate) && (earliestDate == null || visitDate.isBefore(earliestDate))) {
                earliest = schedule;
                earliestDate = visitDate;
            }
        }
        if (earliest == null) {
            Assertion.skip("No schedule on or after " + earliestVisitDate + " for ToDo package " + packageCode
                    + " of product " + productId + ". Response: " + responseBody);
        }
        logger.log("Selected schedule " + earliest.getString("id") + " with visit date " + earliestDate
                + " (earliest allowed " + earliestVisitDate + ")");
        return earliest.getString("id");
    }

    /**
     * Creates the ToDo order for an already-resolved package and schedule (random price tier, as many tickets as
     * {@code minOrderTotal} needs) and waits for its core order id and hash.
     */
    @SneakyThrows
    private Map<String, String> createTTDOrder(
            LoginDataUnmResponse.Data loginData,
            JSONObject productData,
            String productPackages,
            String selectedPackageScheduleID,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation,
            boolean insurance,
            long minOrderTotal
    ) {
        String accessToken = loginData.accessToken;
        List<String> availablePriceTier = ttdCheckAvailablePriceTier(productData, productPackages);
        String selectedPriceTiers = randomSelectPriceTier(availablePriceTier);
        logger.log("selectedPriceTiers-> " + selectedPriceTiers);

        if (insurance) {
            throw new NotImplementedException("TTD insurance add-on is not supported by the v2/orders create-order API");
        }

        JSONObject selectedPackageDetail = ttdFindPackage(productData, productPackages);
        long ticketPrice = TTDApi.extractFinalPrice(selectedPackageDetail, selectedPriceTiers);
        int quantity = ticketPrice > 0 && minOrderTotal > ticketPrice
                ? (int) Math.ceil((double) minOrderTotal / ticketPrice)
                : 1;
        logger.log("ticketPrice-> " + ticketPrice + ", quantity-> " + quantity);
        String[] guestName = guestFullName.trim().split("\\s+", 2);
        String countryCode = loginData.accountPhoneCode == null || loginData.accountPhoneCode.isBlank()
                ? FALLBACK_CONTACT_PHONE_CODE : loginData.accountPhoneCode;
        String phoneNumber = loginData.accountPhoneNumber == null || loginData.accountPhoneNumber.isBlank()
                ? FALLBACK_CONTACT_PHONE_NUMBER : loginData.accountPhoneNumber;
        TTDCreateOrderV2Api.Inputs ttdCreateOrderApiInputs = new TTDCreateOrderV2Api.Inputs(
                accessToken, selectedPackageScheduleID, guestSalutation,
                guestName[0], guestName.length > 1 ? guestName[1] : "",
                guestEmailAddress, countryCode, phoneNumber, TTD_GUEST_NATIONALITY,
                loginData.accountId, selectedPriceTiers, quantity, "IDR",
                ticketPrice,
                TTDApi.extractRequiredQuestionnaireAnswers(selectedPackageDetail),
                loginData.refreshToken, loginData.uniqueId);
        String createOrderID = ttdCreateOrderApi(ttdCreateOrderApiInputs);
        logger.log("createOrderID-> " + createOrderID);

        TTDCheckOrderIDHashApi.Inputs ttdCheckOrderIDHashApiInputs = new TTDCheckOrderIDHashApi.Inputs(accessToken, createOrderID);
        TtdCheckOrderResponse createdOrderIDHash = ttdGetOrderData(ttdCheckOrderIDHashApiInputs);
        if (createdOrderIDHash == null || createdOrderIDHash.coreOrderId == null
                || createdOrderIDHash.coreOrderHash == null) {
            Assertion.skip("Skipping Todo/TTD test: order " + createOrderID
                    + " never produced a core order (coreOrderId="
                    + (createdOrderIDHash == null ? "n/a" : createdOrderIDHash.coreOrderId)
                    + ", coreOrderHash="
                    + (createdOrderIDHash == null ? "n/a" : createdOrderIDHash.coreOrderHash)
                    + "). The TTD/core-order backend did not finish creating the order in time — "
                    + "this is a TTD-side/backend issue, not an Eiffel test defect.");
        }
        long orderId = Long.parseLong(createdOrderIDHash.coreOrderId);
        String orderHash = createdOrderIDHash.coreOrderHash;
        BigInteger grandTotal = createdOrderIDHash.totalAmount;
        logger.log("OrderID-> " + orderId);
        logger.log("OrderHash-> " + orderHash);
        logger.log("Grand total-> " + grandTotal);

        return Map.of("orderId", String.valueOf(orderId),
                "orderHash", orderHash,
                "totalPrice", String.valueOf(grandTotal));
    }

    /**
     * Fetches the TTD select-product catalog once, with retry, and returns its {@code data} object so the
     * booking flow can derive product id, package codes, price tiers and insurance from a single response
     * instead of re-hitting the endpoint for each step (previously up to four calls per booking). Fails with a
     * clear {@link ApiToolsException} rather than a raw {@link JSONException} when the product data cannot be read.
     */
    private JSONObject ttdSelectProductData(TTDSelectProductApi.Inputs inputs)
            throws ExecutionException, InterruptedException {
        TTDSelectProductApi ttdSelectProductApi = new TTDSelectProductApi(inputs);
        int maxAttempts = 3;
        String lastBody = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                ApiResponse response = ttdSelectProductApi.hitApi(apiTools);
                lastBody = response.getBody();
                JSONObject body = new JSONObject(lastBody);
                if (!body.isNull("data")) {
                    JSONObject data = body.getJSONObject("data");
                    if (data.has("id") && data.has("packages") && !data.getJSONArray("packages").isEmpty()) {
                        return data;
                    }
                }
                logger.softWarning("Select-product read attempt " + attempt + "/" + maxAttempts
                        + " returned no usable product data: " + lastBody);
            } catch (ApiEnvironmentException e) {
                lastBody = e.getMessage();
                logger.softWarning("Select-product read attempt " + attempt + "/" + maxAttempts
                        + " hit a transient environment error: " + e.getMessage());
            } catch (JSONException e) {
                logger.softWarning("Select-product parse attempt " + attempt + "/" + maxAttempts
                        + " failed: " + e.getMessage());
            }
            if (attempt < maxAttempts) {
                TimeUnit.SECONDS.sleep(2);
            }
        }
        throw new ApiToolsException("Failed to read TTD product data after " + maxAttempts
                + " attempts. Last response: " + lastBody);
    }

    private String ttdChoosePackageApi(TTDChoosePackageApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDChoosePackageApi ttdChoosePackageApi = new TTDChoosePackageApi(inputs);
        ApiResponse ttdChoosePackageResponse = ttdChoosePackageApi.hitApi(apiTools);
        JSONObject ttdChoosePackageResponseObject = new JSONObject(ttdChoosePackageResponse.getBody());
        JSONArray data = ttdChoosePackageResponseObject.getJSONArray("data").getJSONObject(0).getJSONArray("packageCodes");

        return data.getString(0);
    }

    /**
     * Resolves which ToDo package to order. When {@code requestedPackageCode} is provided and exists in the
     * product's package catalog, that exact package is ordered (so a test can pin e.g. package code "6").
     * Otherwise the first currently-available package is used, preserving the historic behaviour.
     */
    private String resolvePackageCode(JSONObject productData, String accessToken,
                                      String selectedProductID, String requestedPackageCode)
            throws ExecutionException, InterruptedException {
        if (requestedPackageCode != null && !requestedPackageCode.isBlank()) {
            List<String> productPackageCodes = ttdListPackageCodes(productData);
            for (String code : productPackageCodes) {
                if (code.equalsIgnoreCase(requestedPackageCode.trim())) {
                    return code;
                }
            }
            logger.softWarning("Requested ToDo package code '" + requestedPackageCode + "' not found in product "
                    + "package catalog " + productPackageCodes + "; falling back to the first available package.");
        }
        return ttdChoosePackageApi(new TTDChoosePackageApi.Inputs(accessToken, selectedProductID));
    }

    /**
     * Lists every package {@code code} advertised in an already-fetched product {@code data} object, regardless
     * of schedule window. Used by {@link #resolvePackageCode} to validate a pinned package code.
     */
    private List<String> ttdListPackageCodes(JSONObject productData) {
        JSONArray packages = productData.getJSONArray("packages");
        List<String> codes = new ArrayList<>();
        for (int i = 0; i < packages.length(); i++) {
            codes.add(packages.getJSONObject(i).getString("code"));
        }
        return codes;
    }

    private String ttdChoosePackageScheduleApi(TTDChoosePackageScheduleApi.Inputs inputs) throws ExecutionException, InterruptedException {
        TTDChoosePackageScheduleApi ttdChoosePackageScheduleApi = new TTDChoosePackageScheduleApi(inputs);
        ApiResponse ttdChoosePackageScheduleResponse = ttdChoosePackageScheduleApi.hitApi(apiTools);
        JSONObject ttdChoosePackageScheduleResponseObject = new JSONObject(ttdChoosePackageScheduleResponse.getBody());
        logger.log("Choose package schedule response : " + ttdChoosePackageScheduleResponseObject.toString());
        JSONObject data = ttdChoosePackageScheduleResponseObject.getJSONArray("data").getJSONObject(0);

        return data.getString("id");
    }

    private List<String> ttdCheckAvailablePriceTier(JSONObject productData, String packageCode) {
        JSONArray packagesData = productData.getJSONArray("packages");
        List<String> availablePriceTiers = new ArrayList<>();

        for (Object packageDetail : packagesData) {
            if (packageDetail instanceof JSONObject) {
                JSONObject packageDetail2 = (JSONObject) packageDetail;

                if (packageDetail2.getString("code").equalsIgnoreCase(packageCode)) {
                    JSONArray availablePriceTier = packageDetail2.getJSONArray("priceTiers");

                    availablePriceTier.forEach(item -> {
                        JSONObject obj = (JSONObject) item;
                        availablePriceTiers.add(obj.getString("code"));
                    });

                    break;
                }
            }
        }

        return availablePriceTiers;
    }

    private String randomSelectPriceTier(List<String> availablePriceTier) {
        int size = availablePriceTier.size();

        return availablePriceTier.get(CoreFunctions.generateRandomInt(0, size - 1));
    }

    /**
     * Creates the ToDo order. On non-PROD environments the public API gateway is bypassed (direct service call)
     * to dodge Cloudflare rate-limiting; on PROD there is no direct-service endpoint, so the order is created
     * through the gateway. See {@link TTDCreateOrderV2Api} for the gateway-bypass rationale.
     */
    private String ttdCreateOrderApi(TTDCreateOrderV2Api.Inputs inputs) {
        boolean hitGateway = Configs.ENVIRONMENT == Environment.PROD;
        TTDCreateOrderV2Api ttdCreateOrderApi = new TTDCreateOrderV2Api(inputs, hitGateway);
        ApiResponse ttdCreateOrderResponse = RetryUtils.retryWithExponentialBackoff(
                TTD_API_RETRY_ATTEMPTS, TTD_API_RETRY_BASE_DELAY_MS,
                () -> ttdCreateOrderApi.hitApi(apiTools),
                ApiEnvironmentException.class::isInstance,
                "Create ToDo order");
        JSONObject data = new JSONObject(ttdCreateOrderResponse.getBody()).getJSONObject("data");
        return data.getString("id");
    }

    /** Returns the {@code packages[]} entry of the select-product data whose code matches {@code packageCode}. */
    private JSONObject ttdFindPackage(JSONObject productData, String packageCode) {
        for (Object packageDetail : productData.getJSONArray("packages")) {
            if (packageDetail instanceof JSONObject packageJson
                    && packageJson.getString("code").equalsIgnoreCase(packageCode)) {
                return packageJson;
            }
        }
        return null;
    }

    /**
     * Polls the TTD check-order endpoint until the asynchronous core order is created (i.e.
     * {@code coreOrderId}/{@code coreOrderHash} become non-null), waiting between attempts so the
     * backend has time to settle. When the core order never materialises within the budget, the last
     * (incomplete) payload is returned so the caller can raise an informative skip instead of blowing
     * up on a {@code null} {@code coreOrderId}.
     */
    private TtdCheckOrderResponse ttdGetOrderData(TTDCheckOrderIDHashApi.Inputs inputs) throws ExecutionException, InterruptedException {
        Gson gson = new Gson();
        TTDCheckOrderIDHashApi ttdCheckOrderIDHashApi = new TTDCheckOrderIDHashApi(inputs);
        int maxAttempts = 30;
        JSONObject data = new JSONObject();

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                ApiResponse ttdCheckOrderIDHashResponse = ttdCheckOrderIDHashApi.hitApi(apiTools);
                JSONObject ttdCheckOrderIDHashResponseObject = new JSONObject(ttdCheckOrderIDHashResponse.getBody());
                data = ttdCheckOrderIDHashResponseObject.getJSONObject("data");

                if (!data.isNull("coreOrderId") && !data.isNull("coreOrderHash")) {
                    logger.log("core response (attempt " + attempt + "/" + maxAttempts + ") : \n" + data);
                    return gson.fromJson(data.toString(), TtdCheckOrderResponse.class);
                }
                logger.softWarning("TTD core order not ready yet (attempt " + attempt + "/" + maxAttempts
                        + "): coreOrderId/coreOrderHash still null.");
            } catch (ApiEnvironmentException e) {
                logger.softWarning("TTD check-order attempt " + attempt + "/" + maxAttempts
                        + " hit a transient environment error: " + e.getMessage());
            }
            if (attempt < maxAttempts) {
                TimeUnit.SECONDS.sleep(5);
            }
        }

        logger.softWarning("TTD core order was not created after " + maxAttempts
                + " attempts. Last response: " + data);
        return gson.fromJson(data.toString(), TtdCheckOrderResponse.class);
    }

    /** Car Rental order-via-API flow. */
    @SneakyThrows
    public Map<String, String> bookCarRental(
            String email,
            String phoneNumber,
            String password,
            String driverOption,
            String location,
            String startDate,
            String pickupTime,
            String fullName,
            String nameTitle,
            int rentalDuration
    ) {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(email, password,"B2C");
        logger.log("access token: " + accessToken);

        CarRentalAutoCompleteApi.Inputs carRentalAutoCompleteApiInputs = new CarRentalAutoCompleteApi.Inputs(accessToken,location, driverOption);
        String regionalId = carRentalAutoComplete(carRentalAutoCompleteApiInputs).get(0);
        logger.log("regionalId -> " + regionalId);
        String placeId = carRentalAutoComplete(carRentalAutoCompleteApiInputs).get(1);
        logger.log("placeId -> " + placeId);

        CarRentalSearchLocationApi.Inputs carRentalSearchLocationInputs = new CarRentalSearchLocationApi.Inputs(accessToken,placeId);
        String latitude = carRentalSearchLocation(carRentalSearchLocationInputs).get(0);
        logger.log("latitude -> " + latitude);
        String longitude = carRentalSearchLocation(carRentalSearchLocationInputs).get(1);
        logger.log("longitude -> " + longitude);

        CarRentalGetLocationIdApi.Inputs carRentalGetLocationIdInputs = new CarRentalGetLocationIdApi.Inputs(regionalId, latitude, longitude, driverOption,accessToken);
        String locationId = carRentalGetLocationId(carRentalGetLocationIdInputs);
        logger.log("locationId -> " + locationId);

        StringBuilder pickup = new StringBuilder(pickupTime);
        pickup.setCharAt(2, '-');
        String pickupTimeUpdate = pickup.toString();

        SelectCarRentalApi.Inputs selectCarRentalApiInputs = new SelectCarRentalApi.Inputs(accessToken,driverOption, regionalId, startDate, pickupTimeUpdate, latitude, longitude);
        CarRentalData carRentalData = selectCarRental(selectCarRentalApiInputs);
        logger.log("carRegionalId -> " + carRentalData.getCarRegionalId());
        logger.log("carName -> " + carRentalData.getCarName());
        logger.log("carRentalLocation -> " + carRentalData.getCarRentalLocation());
        logger.log("carType -> " + carRentalData.getCarType());

        SubmitBookingApi.Inputs submitBookingApiInputs = new SubmitBookingApi.Inputs(accessToken, carRentalData.getCarRegionalId(), email, phoneNumber, driverOption, location, locationId, startDate, pickupTime, carRentalData.getCarType(), fullName, nameTitle,rentalDuration);
        SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
        String orderId = submitBookingResponse.data.orderId;
        logger.log("orderId-> " + orderId);

        JSONObject orderResponse = getOrderData(String.valueOf(orderId));
        logger.log("orderResponse -> " + orderResponse.toString());
        String totalAmount = String.valueOf(orderResponse.getJSONObject("data").getInt("totalAmount"));

        if(totalAmount.equals("0")){
            totalAmount = String.valueOf(orderResponse.getJSONObject("data").getInt("price"));
        }

        return Map.of("orderId", String.valueOf(orderId),
                "totalPrice", totalAmount, "orderHash", submitBookingResponse.data.orderHash);
    }

    private List<String> carRentalAutoComplete(CarRentalAutoCompleteApi.Inputs inputs) throws ExecutionException, InterruptedException {
        CarRentalAutoCompleteApi carRentalAutoCompleteApi = new CarRentalAutoCompleteApi(inputs);
        ApiResponse carRentalAutoCompleteResponse = carRentalAutoCompleteApi.hitApi(apiTools);
        Gson gson = new Gson();
        CarRentalAutoCompleteResponse response = gson.fromJson(carRentalAutoCompleteResponse.getBody(), CarRentalAutoCompleteResponse.class);
        String regionalId = response.data.get(0).regionalId;
        String placeId = response.data.get(0).placeId;
        return Arrays.asList(regionalId, placeId);
    }

    private List<String> carRentalSearchLocation(CarRentalSearchLocationApi.Inputs inputs) throws ExecutionException, InterruptedException {
        CarRentalSearchLocationApi carRentalSearchLocationApi = new CarRentalSearchLocationApi(inputs);
        ApiResponse carRentalSearchLocationResponse = carRentalSearchLocationApi.hitApi(apiTools);
        Gson gson = new Gson();
        CarRentalSearchLocationResponse response = gson.fromJson(carRentalSearchLocationResponse.getBody(), CarRentalSearchLocationResponse.class);
        String latitude = response.data.latitude;
        String longitude = response.data.longitude;
        return Arrays.asList(latitude, longitude);
    }

    private String carRentalGetLocationId(CarRentalGetLocationIdApi.Inputs inputs) throws ExecutionException, InterruptedException {
        CarRentalGetLocationIdApi carRentalGetLocationIdApi = new CarRentalGetLocationIdApi(inputs);
        ApiResponse carRentalGetLocationIdResponse = carRentalGetLocationIdApi.hitApi(apiTools);
        Gson gson = new Gson();
        logger.log("DEBUG : " + carRentalGetLocationIdResponse.getBody());
        CarRentalGetLocationIdResponse response = gson.fromJson(carRentalGetLocationIdResponse.getBody(), CarRentalGetLocationIdResponse.class);
        return response.data.zone.id;
    }

    private CarRentalData selectCarRental(SelectCarRentalApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SelectCarRentalApi selectCarRentalApi = new SelectCarRentalApi(inputs);
        ApiResponse selectCarRentalResponse = selectCarRentalApi.hitApi(apiTools);
        Gson gson = new Gson();
        SelectCarRentalResponse response = gson.fromJson(selectCarRentalResponse.getBody(), SelectCarRentalResponse.class);
        logger.log("response car rental : " + selectCarRentalResponse.getBody());

        String carRegionalId = response.data.fleets.get(0).carRegionalId;
        String carName = response.data.fleets.get(0).name;
        String carRentalLocation = response.data.regionName;
        String carType = response.data.fleets.get(0).type;

        return new CarRentalData(carRegionalId, carName, carRentalLocation, carType);
    }

    private SubmitBookingResponse submitBooking(SubmitBookingApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitBookingApi submitBookingApi = new SubmitBookingApi(inputs);
        ApiResponse submitBookingApiResponse = submitBookingApi.hitApi(apiTools);
        logger.log("Api response : \n" + submitBookingApiResponse.getBody());
        Gson gson = new Gson();
        return gson.fromJson(submitBookingApiResponse.getBody(), SubmitBookingResponse.class);
    }

    public JSONObject getOrderData(String orderID) throws InterruptedException, ExecutionException {
        InternalOrderDetailApi.Inputs input = new InternalOrderDetailApi.Inputs(orderID);
        InternalOrderDetailApi internalOrderDetailApi = new InternalOrderDetailApi(input);
        ApiResponse response = internalOrderDetailApi.hitApi(apiTools);
        return new JSONObject(response.getBody());
    }

    /** Hotel booking-via-API flow. */
    @SneakyThrows
    public Map<String, String> bookHotelApi(
            String username,
            String password,
            String startDate,
            String query,
            int adult,
            int night,
            int room,
            String currency,
            String language
    ) {
        String orderId = null;
        try {
            HotelApi<?> hotelApi = new HotelApi<>(session);
            hotelApi.setNativeHotelColor(null);
            orderId = hotelApi.createWaitingPaymentOrderApi(username, password, startDate, query, adult, night, room, currency, 0, "hotel", language);
        } catch (Exception e) {
            logger.softWarning("StackTrace:\n" + Arrays.toString(e.getStackTrace()));
            Assertion.skip("Failed booking hotel via API. \nError: " + e.getMessage() );
        }

        JSONObject orderResponse = getOrderData(String.valueOf(orderId));
        logger.log("orderResponse -> " + orderResponse.toString().replaceAll("<[^>]*>", ""));
        String totalAmount = String.valueOf(orderResponse.getJSONObject("data").getInt("totalAmount"));
        String orderHash = String.valueOf(orderResponse.getJSONObject("data").getString("orderHash"));
        if(totalAmount.equals("0")){
            totalAmount = String.valueOf(orderResponse.getJSONObject("data").getInt("price"));
        }

        return Map.of("orderId", String.valueOf(orderId),
                "orderHash", orderHash,
                "totalPrice", totalAmount);
    }

    public Map<String, String> bookFerryApi(String username,
                                            String password,
                                            String origin,
                                            String destination,
                                            String departureDate,
                                            String departureTime,
                                            String vehicleType,
                                            int adult,
                                            int child,
                                            int infant,
                                            int elderly) throws ExecutionException, InterruptedException {
        FerryApi<T> ferryApi = new FerryApi<>(session);
        int orderId = ferryApi.createWaitingPaymentOrderFerry(username,
                password,
                origin,
                destination,
                departureDate,
                departureTime,
                vehicleType,
                adult,
                child,
                infant,
                elderly);

        JSONObject orderResponse = getOrderData(String.valueOf(orderId));
        logger.log("orderResponse -> " + orderResponse.toString().replaceAll("<[^>]*>", ""));
        String totalAmount = String.valueOf(orderResponse.getJSONObject("data").getInt("totalAmount"));
        String orderHash = String.valueOf(orderResponse.getJSONObject("data").getString("orderHash"));
        if(totalAmount.equals("0")){
            totalAmount = String.valueOf(orderResponse.getJSONObject("data").getInt("price"));
        }

        return Map.of("orderId", String.valueOf(orderId),
                "orderHash", orderHash,
                "totalPrice", totalAmount);
    }

    public long resolveAccountId() {
        if (accountId == null) {
            accessToken = driver.manage().getCookieNamed("session_access_token").getValue();
            GetAccountDataApi getAccountDataApi = new GetAccountDataApi(
                    new GetAccountDataApi.Inputs("Bearer " + accessToken)
            );
            AccountData accountData = new Gson().fromJson(
                    getAccountDataApi.hitApi(apiTools).getBody(),
                    AccountData.class
            );
            accountId = accountData.data.accountId;
            logger.log("Resolved accountId: " + accountId);
        }
        return accountId;
    }

    @SneakyThrows
    public void verifyPendingEarningPoints(
            String orderId,
            int expectedPoints,
            String expectedOrderType,
            String expectedPointStatus,
            int maxAttempts,
            int sleepSeconds
    ) {
        long resolvedAccountId = resolveAccountId();
        Gson gson = new Gson();

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            PendingPointListApi pendingPointListApi = new PendingPointListApi(
                    new PendingPointListApi.Inputs(resolvedAccountId));
            ApiResponse response = pendingPointListApi.hitApi(apiTools);
            logger.log("Pending point list response (attempt " + (attempt + 1) + "): " + response.getBody());

            JsonObject pendingResponse = gson.fromJson(response.getBody(), JsonObject.class);
            JsonArray content = pendingResponse.getAsJsonObject("data").getAsJsonArray("content");
            for (JsonElement jsonElement : content) {
                JsonObject item = jsonElement.getAsJsonObject();
                if (!orderId.equals(getSafeString(item, "orderId"))) {
                    continue;
                }

                int pendingPoint = item.get("point").getAsInt();
                String responseOrderType = getSafeString(item, "orderType");
                String responsePointStatus = getSafeString(item, "pointStatus");

                logger.log("Pending point entry for order " + orderId
                        + " -> point: " + pendingPoint
                        + ", orderType: " + responseOrderType
                        + ", pointStatus: " + responsePointStatus);

                Assertion.assertThat("Pending earning points should match payment page",
                        pendingPoint, is(expectedPoints));
                Assertion.assertThat("Pending list orderType should match expected",
                        responseOrderType, equalToIgnoringCase(expectedOrderType));
                Assertion.assertThat("Earned list pointStatus should match expected",
                        responsePointStatus, equalToIgnoringCase(expectedPointStatus));
                return;
            }
            Thread.sleep(sleepSeconds * 1000L);
        }
        Assertion.fail("OrderId " + orderId + " not found in pending point list response after "
                + maxAttempts + " attempts");
    }

    @SneakyThrows
    public void verifyPendingEarningPoint(
            long resolvedAccountId,
            String orderId,
            int expectedPoints,
            String expectedOrderType,
            String expectedPointStatus,
            int maxAttempts,
            int sleepSeconds
    ) {

        Gson gson = new Gson();

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            PendingPointListApi pendingPointListApi = new PendingPointListApi(
                    new PendingPointListApi.Inputs(resolvedAccountId));
            ApiResponse response = pendingPointListApi.hitApi(apiTools);
            logger.log("Pending point list response (attempt " + (attempt + 1) + "): " + response.getBody());

            JsonObject pendingResponse = gson.fromJson(response.getBody(), JsonObject.class);
            JsonArray content = pendingResponse.getAsJsonObject("data").getAsJsonArray("content");
            for (JsonElement jsonElement : content) {
                JsonObject item = jsonElement.getAsJsonObject();
                if (!orderId.equals(getSafeString(item, "orderId"))) {
                    continue;
                }

                int pendingPoint = item.get("point").getAsInt();
                String responseOrderType = getSafeString(item, "orderType");
                String responsePointStatus = getSafeString(item, "pointStatus");

                logger.log("Pending point entry for order " + orderId
                        + " -> point: " + pendingPoint
                        + ", orderType: " + responseOrderType
                        + ", pointStatus: " + responsePointStatus);

                Assertion.assertThat("Pending earning points should match payment page",
                        pendingPoint, is(expectedPoints));
                Assertion.assertThat("Pending list orderType should match expected",
                        responseOrderType, equalToIgnoringCase(expectedOrderType));
                Assertion.assertThat("Earned list pointStatus should match expected",
                        responsePointStatus, equalToIgnoringCase(expectedPointStatus));
                return;
            }
            Thread.sleep(sleepSeconds * 1000L);
        }
        Assertion.fail("OrderId " + orderId + " not found in pending point list response after "
                + maxAttempts + " attempts");
    }

    @SneakyThrows
    public void verifyOrderIdNotInPendingEarningPoints(String orderId, int maxAttempts, int sleepSeconds) {
        long resolvedAccountId = resolveAccountId();
        Gson gson = new Gson();

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            PendingPointListApi pendingPointListApi = new PendingPointListApi(
                    new PendingPointListApi.Inputs(resolvedAccountId));
            ApiResponse response = pendingPointListApi.hitApi(apiTools);
            logger.log("Pending point list response (attempt " + (attempt + 1) + "): " + response.getBody());

            JsonObject pendingResponse = gson.fromJson(response.getBody(), JsonObject.class);
            JsonArray content = pendingResponse.getAsJsonObject("data").getAsJsonArray("content");
            for (JsonElement jsonElement : content) {
                JsonObject item = jsonElement.getAsJsonObject();
                if (!orderId.equals(getSafeString(item, "orderId"))) {
                    continue;
                }

                int pendingPoint = item.get("point").getAsInt();
                if (pendingPoint == TIER_UPGRADE_PENDING_POINT) {
                    logger.log("OrderId " + orderId + " has pending point = 1 — tier upgrade/GBV reporting only, "
                            + "not real earning points");
                    return;
                }

                Assertion.fail("OrderId " + orderId + " has real pending earning points: " + pendingPoint);
            }
            Thread.sleep(sleepSeconds * 1000L);
        }
        logger.log("Verified orderId " + orderId + " is not in pending point list with real earning points");
    }

    Long accountId = null;
    String accessToken;
    int totalPriceFinal = 0;
    String paymentType = null;
    Map<String, Object> pointHistoryData = new HashMap<>();
    @Setter
    @Getter
    Map<String, String> sheetData = new HashMap<>();
    @Getter
    String priceFromPaymentPage = null;
}
