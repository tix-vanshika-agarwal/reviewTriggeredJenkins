package com.tiket.apiPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.pojo.paymentDweb.OrderDetailResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static org.hamcrest.Matchers.is;

/**
 * Books, pays for and issues a B2C round trip on dWeb, and hands back an order that is ISSUED and
 * therefore reschedulable and refundable. Each leg names its own airline and supplier, so the order
 * can be a plain round trip on one booking code or a virtual interline pair on two.
 * <p>
 * Issuance goes through the mock-success stub: the ISSUED integrators of the named suppliers are
 * pointed at it for the length of the booking (see {@link FlightIssuedSupplierMock}) and put back
 * before this returns, so no ticket number has to be written by hand and no airline is ever asked
 * to issue. The hold ends once the order is ISSUED, which is all a test needs if it only reads the
 * reschedule funnel — a test that completes a reschedule issues again and needs its own hold.
 * </p>
 * <p>
 * An order the caller cannot use is dropped before it is paid for wherever the platform allows it,
 * so nothing unusable is walked into issuance.
 * </p>
 */
public class FlightIssuedOrderFactory<T extends WebDriver> extends BasePage<T> {

    /** Shortest name part airlines accept on a booking. */
    private static final int MIN_NAME_PART_LENGTH = 3;

    private static final int THREE_DS_TIMEOUT_SECONDS = 30;

    /** Most Sakuku takes in one payment. */
    private static final long SAKUKU_LIMIT = 10_000_000L;

    private static final long PAID_TIMEOUT_SECONDS = 120;

    public FlightIssuedOrderFactory(Session<T> session) {
        super(session);
    }

    /**
     * Each leg names its own airline and supplier, so a round trip can be flown by one airline on a
     * single booking code or by two on a booking code each.
     *
     * @param airlineDepart         airline display name, as the search reads it
     * @param airlineReturn         airline for the return leg; blank means the same as the departure
     * @param airlineCodeDepart     two-letter code of the resolved departure flight, read by the
     *                              mock-issued gate
     * @param supplierCodeDepart    the supplier whose ISSUED integrators may be pointed at the mock
     *                              for the departure leg. The order is only used when the legs are
     *                              served by the named suppliers, since every other supplier mapped
     *                              to the airline is left alone
     * @param supplierCodeReturn    supplier for the return leg; blank means the same as the
     *                              departure, which only holds when both legs are on one airline
     * @param returnStreamType      which return-leg stream the return flight is booked from, blank
     *                              for {@link FlightApi#RETURN_STREAM}. Only a case that needs a
     *                              Round-trip Deals fare on the order itself asks for
     *                              {@link FlightApi#RETURN_STREAM_ROUND_TRIP_DEALS} — a single
     *                              booking code says nothing about the fare being one
     * @param expectedBookingCodes  how many booking codes the order must have — 1 for a plain round
     *                              trip, 2 for a virtual interline pair, 0 to accept any
     * @param card                  the card the payment page is filled with when Sakuku cannot
     *                              settle the order. Null for a rupiah order under
     *                              {@link #SAKUKU_LIMIT}, which is paid over the API
     */
    public record Inputs(String username, String password, String origin, String destination,
                         String departureDate, String returnDate, String tripType, int adult,
                         int child, int infant, String airlineDepart, String airlineReturn,
                         String airlineCodeDepart, String airlineCodeReturn, String phoneNumber,
                         String supplierCodeDepart, String supplierCodeReturn,
                         String returnStreamType, int expectedBookingCodes, Card card) {

        /**
         * A round trip on one airline, one supplier and one booking code, on a plain return fare.
         */
        public static Inputs singleBookingCode(String username, String password, String origin,
                                               String destination, String departureDate,
                                               String returnDate, String tripType, int adult,
                                               int child, int infant, String airline,
                                               String airlineCodeDepart, String airlineCodeReturn,
                                               String phoneNumber, String supplierCode, Card card) {
            return new Inputs(username, password, origin, destination, departureDate, returnDate,
                    tripType, adult, child, infant, airline, airline, airlineCodeDepart,
                    airlineCodeReturn, phoneNumber, supplierCode, supplierCode,
                    FlightApi.RETURN_STREAM, 1, card);
        }

        public static Inputs forPaymentOnly(String username, String password, Card card) {
            return new Inputs(username, password, "", "", "", "", "", 0, 0, 0, "", "", "", "", "",
                    "", "", "", 0, card);
        }

        String resolvedReturnStreamType() {
            return isBlank(returnStreamType) ? FlightApi.RETURN_STREAM : returnStreamType;
        }

        String resolvedAirlineReturn() {
            return isBlank(airlineReturn) ? airlineDepart : airlineReturn;
        }

        /**
         * The supplier the return leg may be issued through. A leg on a different airline is served
         * by a different supplier, so leaving it unnamed would book a leg whose ISSUED integrator
         * was never pointed at the mock.
         */
        String resolvedSupplierCodeReturn() {
            if (!isBlank(supplierCodeReturn)) {
                return supplierCodeReturn;
            }
            if (!resolvedAirlineReturn().equalsIgnoreCase(airlineDepart)) {
                Assertion.fail("The return leg is on " + resolvedAirlineReturn() + " rather than "
                        + airlineDepart + ", so the test data has to name supplierCodeReturn as well. "
                        + "Only named suppliers are pointed at the issuance mock.");
            }
            return supplierCodeDepart;
        }

        private static boolean isBlank(String value) {
            return value == null || value.isBlank();
        }
    }

    /** Card details as the sheet carries them: {@code cardNumber}, {@code cardMonth}, {@code cvv}, {@code cardHolderName}. */
    public record Card(String number, String month, String cvv, String holderName) {

        boolean isBlank() {
            return isEmpty(number) || isEmpty(month) || isEmpty(cvv) || isEmpty(holderName);
        }

        private static boolean isEmpty(String value) {
            return value == null || value.isBlank();
        }
    }

    /** An issued order, the passenger it was booked for, and how many booking codes it holds. */
    public record IssuedOrder(String orderId, String passengerName, int bookingCodeCount) {
    }

    public IssuedOrder createIssuedRoundTrip(Inputs inputs) {
        FlightApi<T> flightApi = new FlightApi<>(session);

        if (inputs.supplierCodeDepart() == null || inputs.supplierCodeDepart().isBlank()) {
            Assertion.fail("No supplierCodeDepart in the test data. A case using this flow has to name "
                    + "the supplier whose ISSUED integrator may be pointed at the issuance mock.");
        }

        // A round trip on one airline names the same supplier twice, and it is held once.
        Set<String> pinnedSuppliers =
                new LinkedHashSet<>(List.of(inputs.supplierCodeDepart(), inputs.resolvedSupplierCodeReturn()));

        String passengerName = randomPassengerName();
        FlightIssuedSupplierMock<T> issuedSupplierMock = new FlightIssuedSupplierMock<>(session);
        try {
            logger.step("Point the ISSUED integrators of supplier(s) " + pinnedSuppliers
                    + " at the issuance mock");
            issuedSupplierMock.hold(pinnedSuppliers);

            logger.step("Validate all suppliers are mock issued by airline, skipped if real issued "
                    + "supplier exist");
            // Booking picks among every supplier mapped to the airline by priority, not just the one
            // pointed at the mock above, and the serving supplier is only known once the order exists.
            boolean departMocked = flightApi.areAllSuppliersMockIssuedByAirline(
                    inputs.airlineCodeDepart(), inputs.origin(), inputs.destination());
            boolean returnMocked = flightApi.areAllSuppliersMockIssuedByAirline(
                    inputs.airlineCodeReturn(), inputs.destination(), inputs.origin());
            if (!departMocked || !returnMocked) {
                Assertion.skip("Skipping: supplier is NOT mock-issued for " + inputs.origin() + "-"
                        + inputs.destination());
            }

            logger.step("Create the round-trip order via API for passenger " + passengerName
                    + ", departing on " + inputs.airlineDepart() + " and returning on "
                    + inputs.resolvedAirlineReturn() + " out of the "
                    + inputs.resolvedReturnStreamType() + " stream");
            String orderId = flightApi.createOrderFlightB2CRoundTrip(
                    inputs.username(), inputs.password(),
                    inputs.origin(), inputs.destination(), inputs.departureDate(), inputs.returnDate(),
                    inputs.tripType(), inputs.adult(), inputs.child(), inputs.infant(),
                    inputs.airlineDepart(), inputs.resolvedAirlineReturn(),
                    inputs.resolvedReturnStreamType(),
                    new String[]{passengerName}, inputs.phoneNumber()
            );
            if (orderId == null) {
                Assertion.skip("Flight Booking via API failed. Skipping the subsequent steps.");
            }

            logger.step("Check the booking record before paying, so an order the case cannot use is "
                    + "dropped before it can be paid for and issued");
            int bookingCodeCount = flightApi.getBookingCodeCount(orderId, inputs.username());
            boolean checkedBeforePayment = bookingCodeCount > 0;
            if (checkedBeforePayment) {
                requireBookingCodeCount(orderId, bookingCodeCount, inputs.expectedBookingCodes());
                requireServedByPinnedSuppliers(flightApi, orderId, inputs, pinnedSuppliers);
            } else {
                // The supplier books before payment for most fares, but not all, so an order with no
                // record yet is checked once it is paid rather than dropped here.
                logger.log("Order " + orderId + " has no booking record before payment, so it is "
                        + "checked after paying");
            }

            logger.step("Pay the order");
            payOrder(inputs, orderId);

            if (!checkedBeforePayment) {
                logger.step("Check the booking record, now that the paid order has one");
                bookingCodeCount = flightApi.getBookingCodeCount(orderId, inputs.username());
                requireBookingCodeCount(orderId, bookingCodeCount, inputs.expectedBookingCodes());
                requireServedByPinnedSuppliers(flightApi, orderId, inputs, pinnedSuppliers);
            }

            logger.step("Wait for the order to be issued through the mock, which makes it "
                    + "reschedulable");
            if (!flightApi.waitForOrderIssued(orderId, inputs.username())) {
                Assertion.fail("Order " + orderId + " never reached ISSUED even though supplier(s) "
                        + pinnedSuppliers + " were pointed at the issuance mock");
            }

            return new IssuedOrder(orderId, passengerName, bookingCodeCount);
        } finally {
            // Restored only once issuance is proven, or a live integrator is pointed at an airline
            // while the order is still issuing.
            logger.step("Put every held ISSUED integrator back to the URL it had");
            issuedSupplierMock.release();
        }
    }

    public record MultiCityInputs(String username, String password, List<String[]> routes,
                                  List<String> departureDates, List<String> airlineCodes, int adult,
                                  int child, int infant, String cabinClass, String phoneNumber) {
    }

    public FlightApi.MultiCityOrder createIssuedMultiCity(MultiCityInputs inputs) {
        FlightApi<T> flightApi = new FlightApi<>(session);
        FlightIssuedSupplierMock<T> issuedSupplierMock = new FlightIssuedSupplierMock<>(session);
        Set<String> pinnedSuppliers = new LinkedHashSet<>();
        try {
            logger.step("Create the multi-city order via API across " + inputs.routes().size() + " leg(s)");
            FlightApi.MultiCityOrder order = flightApi.createMultiCityOrderWaitingForPayment(
                    inputs.username(), inputs.password(), inputs.routes(), inputs.departureDates(),
                    inputs.airlineCodes(), inputs.adult(), inputs.child(), inputs.infant(),
                    inputs.cabinClass(), inputs.phoneNumber(), supplierCodes -> {
                        pinnedSuppliers.addAll(supplierCodes);
                        issuedSupplierMock.hold(supplierCodes);
                    });
            if (order == null) {
                Assertion.skip("Multi-city booking via API failed. Skipping the subsequent steps.");
            }

            logger.step("Pay the order with Sakuku");
            payOrder(Inputs.forPaymentOnly(inputs.username(), inputs.password(), null), order.orderId());

            logger.step("Wait for the order to be issued through the mock");
            if (!flightApi.waitForOrderIssued(order.orderId(), inputs.username())) {
                Assertion.skip("Order " + order.orderId() + " never reached ISSUED even though " + "supplier(s) " + pinnedSuppliers + " were pointed at the issuance mock");
            }

            return order;
        } finally {
            logger.step("Put every held ISSUED integrator back to the URL it had");
            issuedSupplierMock.release();
        }
    }

    /**
     * Sakuku settles over the API in seconds and never opens the payment page, so it spares the
     * prerequisite the 3DS step and the price-change dialog the payment page can raise. It only
     * takes rupiah, and only up to {@link #SAKUKU_LIMIT}; anything else is paid by card.
     */
    @SneakyThrows
    private void payOrder(Inputs inputs, String orderId) {
        PaymentApi<T> paymentApi = new PaymentApi<>(session);
        OrderDetailResponse.Data order = paymentApi.getOrderDetails(orderId).data;
        long total = order.getTotalAmountAsLong();

        if (!"IDR".equalsIgnoreCase(order.totalAmountCurrency) || total >= SAKUKU_LIMIT) {
            logger.log("Order " + orderId + " comes to " + total + " " + order.totalAmountCurrency
                    + ", which Sakuku does not take, so it is paid by card on the payment page");
            payWithCard(inputs, orderId, order.orderHash);
            return;
        }

        String accessToken = new Unm<T>(session).getAccessToken(inputs.username(), inputs.password(), "B2C");
        String referenceId = paymentApi.submitSakukuPayment(accessToken, Long.parseLong(orderId), order.orderHash);

        String paymentStatus = paymentApi.paidCheckerStatus(PAID_TIMEOUT_SECONDS,
                referenceId == null || referenceId.isBlank() ? orderId : referenceId);
        if (!"SUCCESS".equalsIgnoreCase(paymentStatus)) {
            Assertion.fail("Order " + orderId + " was submitted to Sakuku but is still "
                    + (paymentStatus == null || paymentStatus.isBlank() ? "unpaid" : paymentStatus)
                    + " after " + PAID_TIMEOUT_SECONDS + " seconds");
        }
    }

    private void payWithCard(Inputs inputs, String orderId, String orderHash) {
        Card card = inputs.card();
        if (card == null || card.isBlank()) {
            Assertion.fail("Order " + orderId + " has to be paid by card, but the test data names no "
                    + "cardNumber, cardMonth, cvv and cardHolderName");
        }

        PaymentPageWeb<T> paymentPage = PaymentPageWeb.getInstance(session);
        paymentPage.goToPaymentPage(orderId, orderHash);
        paymentPage.selectCreditDebitCardTileRadio();
        paymentPage.inputCardNumber(card.number());
        paymentPage.inputCardMonth(card.month());
        paymentPage.inputCardCvv(card.cvv());
        paymentPage.inputCardHolderName(card.holderName());
        paymentPage.clickOnPaymentButton();
        paymentPage.handle3DSPage(THREE_DS_TIMEOUT_SECONDS);
        Assertion.assertThat("Payment successful page is visible",
                paymentPage.isPaymentSuccessfulVisible(), is(true));
        paymentPage.waitAfterPayment();
    }

    /** One booking code is a plain round trip, two is a virtual interline pair. */
    private static void requireBookingCodeCount(String orderId, int bookingCodeCount, int expected) {
        if (expected > 0 && bookingCodeCount != expected) {
            Assertion.skip("Skipping: order " + orderId + " has " + bookingCodeCount + " booking "
                    + "codes, this case needs exactly " + expected);
        }
    }

    /**
     * Every leg has to have been served by a supplier this run pointed at the mock. A leg served by
     * any other supplier is a leg that will not issue through the mock, however the rest of the
     * order looks.
     */
    private static void requireServedByPinnedSuppliers(FlightApi<?> flightApi, String orderId,
                                                       Inputs inputs, Set<String> pinnedSuppliers) {
        Set<String> servingSuppliers = flightApi.getIssuanceSupplierCodes(orderId, inputs.username());
        // An -ENC suffix marks an encrypted fare and names the same supplier.
        List<String> unpinned = servingSuppliers.stream()
                .map(code -> code.replaceAll("-ENC$", ""))
                .filter(code -> pinnedSuppliers.stream().noneMatch(code::equalsIgnoreCase))
                .toList();
        if (servingSuppliers.isEmpty() || !unpinned.isEmpty()) {
            Assertion.skip("Skipping: order " + orderId + " was served by " + servingSuppliers
                    + ", which is not covered by the supplier(s) this run pointed at the issuance "
                    + "mock, " + pinnedSuppliers);
        }
    }

    /**
     * A fresh passenger per order, so repeated runs do not book the same person over and over and
     * draw a duplicate-booking or fraud flag.
     */
    private static String randomPassengerName() {
        return namePart(CoreFunctions::randomNameWithoutTest) + " "
                + namePart(CoreFunctions::randomLastName);
    }

    /** Letters only and long enough for an airline to accept it. */
    private static String namePart(Supplier<String> generator) {
        String part = generator.get().replaceAll("[^A-Za-z]", "");
        while (part.length() < MIN_NAME_PART_LENGTH) {
            part = part + generator.get().replaceAll("[^A-Za-z]", "");
        }
        return part;
    }
}
