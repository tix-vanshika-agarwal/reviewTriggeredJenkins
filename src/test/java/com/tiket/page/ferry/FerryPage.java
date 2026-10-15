package com.tiket.page.ferry;

import com.google.gson.Gson;
import com.tiket.api.ferry.AddToCartFerryApi;
import com.tiket.api.ferry.SearchScheduleFerryApi;
import com.tiket.api.ferry.SubmitBookingFerryApi;
import com.tiket.api.payment.SubmitCreditCardApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.corporate.login.CorporateLoginPage;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.pojo.ferry.AddToCartFerryResponse;
import com.tiket.pojo.ferry.SearchScheduleFerryResponse;
import com.tiket.pojo.ferry.SubmitBookingFerryResponse;
import com.tiket.pojo.payment.SubmitCreditCardResponse;
import com.tiket.pojo.train.FerryInternalOrderDetail;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.equalTo;

public abstract class FerryPage<T extends WebDriver> extends BasePage<T> {

    public FerryPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FerryPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {
                return new FerryPageIOS<>(session);
            }
            case ANDROID -> {
                return new FerryPageAndroid<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    int orderId;
    BigDecimal grandTotal;
    private static final Gson gson = new Gson();

    private SearchScheduleFerryResponse searchFerry(SearchScheduleFerryApi.Input input)
            throws InterruptedException, ExecutionException {
        SearchScheduleFerryApi searchScheduleFerryApi = new SearchScheduleFerryApi(input);
        ApiResponse searchScheduleFerryResponse = searchScheduleFerryApi.hitApi(apiTools);
        logger.log("DEBUG raw search result : " + searchScheduleFerryResponse.getBody());
        SearchScheduleFerryResponse responsePojo = gson.fromJson(searchScheduleFerryResponse.getBody(),
                SearchScheduleFerryResponse.class);
        return responsePojo;
    }

    private SearchScheduleFerryResponse.Schedule filterSchedule(SearchScheduleFerryResponse.Data searchResultData) {
        return searchResultData.schedules.stream().filter(data -> data.availableQuota > 0).findFirst().orElseThrow();
    }

    private AddToCartFerryResponse addToCart(AddToCartFerryApi.Input input)
            throws InterruptedException, ExecutionException {
        AddToCartFerryApi addToCartFerryApi = new AddToCartFerryApi(input);
        ApiResponse addToCartFerryResponse = addToCartFerryApi.hitApi(apiTools);
        AddToCartFerryResponse responsePojo = gson.fromJson(addToCartFerryResponse.getBody(),
                AddToCartFerryResponse.class);
        return responsePojo;
    }

    private SubmitBookingFerryResponse submitBooking(SubmitBookingFerryApi.Input bookingData)
            throws InterruptedException, ExecutionException {
        SubmitBookingFerryApi submitBookingFerryApi = new SubmitBookingFerryApi(bookingData);
        ApiResponse submitBookingFerryApiResponse = submitBookingFerryApi.hitApi(apiTools);
        logger.log("debug : " + submitBookingFerryApiResponse.getBody());
        SubmitBookingFerryResponse responsePojo = gson.fromJson(submitBookingFerryApiResponse.getBody(),
                SubmitBookingFerryResponse.class);
        return responsePojo;
    }

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
            grandTotal = selectedSchedule.price.finalPrice;
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
            orderId = Integer.parseInt(responseSubmitBooking.data.order.orderId);

            //get ferry total price + service fee
            OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
            FerryInternalOrderDetail ferryInternalOrderDetial = orderDetailPage.getFerryInternalOrderDetail(String.valueOf(orderId));
            logger.log("grand total : "+ferryInternalOrderDetial.data.totalAmount);
            grandTotal = ferryInternalOrderDetial.data.totalAmount;

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

    @SneakyThrows
    public int createWaitingPaymentOrderFerryB2B(
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

            CorporateLoginPage<T> corporateLoginPage = CorporateLoginPage.getInstance(session);
            String loginAccessToken = "Bearer " + corporateLoginPage.getAccessTokenB2BCorporate(username,password);
            logger.log("accessToken for dashboard request->" + loginAccessToken);

            // search for ferry
            SearchScheduleFerryApi.Input searchQuery = new SearchScheduleFerryApi.Input(loginAccessToken, origin, destination,
                    departureDate, departureTime, vehicleType, adult, child, infant, elderly);
            SearchScheduleFerryResponse searchResponse = searchFerry(searchQuery);
            SearchScheduleFerryResponse.Schedule selectedSchedule = this.filterSchedule(searchResponse.data);
            grandTotal = selectedSchedule.price.finalPrice;
            logger.log("grand total without service fee: " + grandTotal);
            String scheduleId = selectedSchedule.id;
            // add to cart ferry
            AddToCartFerryApi.Input cartInput = new AddToCartFerryApi.Input(loginAccessToken, scheduleId, adult, child, infant,
                    elderly);
            AddToCartFerryResponse addToCartResponse = addToCart(cartInput);
            Thread.sleep(2000);
            String cartId = addToCartResponse.data.cartId;
            String secretId = addToCartResponse.data.secret;
            boolean licensePlate = addToCartResponse.data.schedules.get(0).vehicleType.isLicensePlateRequired;
            // submit booking
            SubmitBookingFerryApi.Input submitBookingInput = new SubmitBookingFerryApi.Input(username, loginAccessToken, licensePlate, cartId, secretId, adult, child, infant);
            SubmitBookingFerryResponse responseSubmitBooking = submitBooking(submitBookingInput);
            orderId = Integer.parseInt(responseSubmitBooking.data.order.orderId);

            //get ferry total price + service fee
            OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
            FerryInternalOrderDetail ferryInternalOrderDetial = orderDetailPage.getFerryInternalOrderDetail(String.valueOf(orderId));
            logger.log("grand total : "+ferryInternalOrderDetial.data.totalAmount);
            grandTotal = ferryInternalOrderDetial.data.totalAmount;

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

    public BigDecimal getTotalPrice(){
        return this.grandTotal;
    }


    @SneakyThrows
    public int createIssuedOrderFerry(
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
            int elderly,
            String cardNumber,
            String cardHolderName,
            String cardExpiredMonth,
            String cardExpiredYear,
            String cardCvvNumber
            ) {
        try {
            Unm<T> unm = new Unm<>(session);
            LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
            String accessToken = authData.accessToken;

            // search for ferry
            SearchScheduleFerryApi.Input searchQuery = new SearchScheduleFerryApi.Input(accessToken, origin, destination,
                    departureDate, departureTime, vehicleType, adult, child, infant, elderly);
            SearchScheduleFerryResponse searchResponse = searchFerry(searchQuery);
            SearchScheduleFerryResponse.Schedule selectedSchedule = this.filterSchedule(searchResponse.data);
            grandTotal = selectedSchedule.price.finalPrice;
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
            orderId = Integer.parseInt(responseSubmitBooking.data.order.orderId);

            SubmitCreditCardApi.Inputs submitPaymentApiInputs = new SubmitCreditCardApi.Inputs(accessToken, responseSubmitBooking.data.order.orderHash, orderId, cardNumber, cardHolderName, cardExpiredMonth, cardExpiredYear, cardCvvNumber);
            SubmitCreditCardResponse responseSubmitCc = submitCreditCardResponse(submitPaymentApiInputs);
            Assertion.assertThat("Verify response submit cc", responseSubmitCc.code, equalTo("SUCCESS"));
            if(responseSubmitCc.data.thirdPartyResponse.data.acsUrl!=null && !responseSubmitCc.data.thirdPartyResponse.data.acsUrl.isEmpty()){
                PaymentPage<T> paymentPage = PaymentPage.getInstance(session);
                logger.log("acsUrl-> " + responseSubmitCc.data.thirdPartyResponse.data.acsUrl);
                paymentPage.openUrl(responseSubmitCc.data.thirdPartyResponse.data.acsUrl);
            }
            //get ferry total price + service fee
            OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
            FerryInternalOrderDetail ferryInternalOrderDetial = orderDetailPage.getFerryInternalOrderDetail(String.valueOf(orderId));
            logger.log("grand total : "+ferryInternalOrderDetial.data.totalAmount);
            grandTotal = ferryInternalOrderDetial.data.totalAmount;


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

    private SubmitCreditCardResponse submitCreditCardResponse(SubmitCreditCardApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitCreditCardApi submitCreditCardApi = new SubmitCreditCardApi(inputs);
        ApiResponse submitCreditCard = submitCreditCardApi.hitApi(apiTools);
        Gson gson = new Gson();
        logger.log("response submit card: " + submitCreditCard.getBody());
        return gson.fromJson(submitCreditCard.getBody(), SubmitCreditCardResponse.class);
    }

}
