package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.carTransfer.*;
import com.tiket.api.payment.SubmitCreditCardApi;
import com.tiket.api.payment.SubmitSakukuApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.pojo.cartransfer.AddToCartResponseAT;
import com.tiket.pojo.cartransfer.CheckOutResponseAT;
import com.tiket.pojo.cartransfer.GetCatalogueIdResponseAT;
import com.tiket.pojo.cartransfer.SuggestionResponse;
import com.tiket.pojo.member.SmartProfileResponse;
import com.tiket.pojo.payment.SubmitCreditCardResponse;
import com.tiket.pojo.payment.SubmitSakukuResponse;
import com.tiket.pojo.train.TrainInternalOrderDetail;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class CarTransferApi <T extends WebDriver> extends BasePage<T> {
    public CarTransferApi(Session<T> session) {
        super(session);
    }

    public String orderId;
    @Getter
    public double grandTotal;
    @Getter
    String pickupPlace;
    @Getter
    String pickupAddress;
    @Getter
    String dropPlace;
    @Getter
    String dropAddress;
    @Getter
    String orderHash;
    @Getter
    String salutation;
    @Getter
    String guestName;
    @Getter
    String phoneNumber;
    @Getter
    String carType;
    @Getter
    String orderName;

    /**
     * This method is used to create an issued order via API using credit card payment method.
     * It logs in the user, retrieves the smart profile data, gets the car transfer suggestions,
     * adds the car transfer to the cart, checks out, and submits the credit card payment.
     * Finally, it confirms the order and retrieves the order details.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param origin The origin location for the car transfer.
     * @param destination The destination location for the car transfer.
     * @param type The type of car transfer (e.g., "OUT", "IN").
     * @param rangeDate The date range for the car transfer.
     * @param pickupTime The pickup time for the car transfer.(10/10) will be changed to 10:10
     * @param pax The number of passengers for the car transfer.
     * @param cardNumber The credit card number for payment.
     * @param cardHolderName The name on the credit card.
     * @param cardExpiredMonth The expiration month of the credit card.
     * @param cardExpiredYear The expiration year of the credit card.
     * @param cardCvvNumber The CVV number of the credit card.
     * @param adminName The name of the admin for order confirmation.
     * @param adminPhoneNumber The phone number of the admin for order confirmation.
     * @return The order ID of the created order. complete booking
     */
    @SneakyThrows
    public String createIssuedOrderApi(
            String username,
            String password,
            String origin,
            String destination,
            String type,
            String rangeDate,
            String pickupTime,
            int pax,
            String cardNumber,
            String cardHolderName,
            String cardExpiredMonth,
            String cardExpiredYear,
            String cardCvvNumber,
            String adminName,
            String adminPhoneNumber
    ) {
        try {
            Unm<T> unm = new Unm<>(session);
            SmartProfileApi<?> smartProfileApi = new SmartProfileApi<>(session);
            LoginDataUnmResponse.Data data = unm.unmLoginApi(username, password);
            String accessToken = data.accessToken;
            boolean loginStatus = data.accountUsername.equals(username);
            logger.log("loginStatus-> " + username + ", " + loginStatus);
            String departureDate = CoreFunctions.generateRandomDate(rangeDate);

            SmartProfileResponse.Data smartProfileData = smartProfileApi.getSmartProfileData(accessToken,data.accountFirstName);
            logger.log("getSmartProfileData-> " + (smartProfileData != null ? "Found" : "Not Found") + " profile with first name: " + data.accountFirstName);

            phoneNumber = smartProfileData.accountPhone;
            salutation = smartProfileData.accountSalutationCode;
            guestName = smartProfileData.accountFirstName + " " + smartProfileData.accountLastName;

            String locationTypeOrigin = "AIRPORT";
            CarTransferSuggestionApi.Inputs carTransferSuggestionApiInputsOrigin = new CarTransferSuggestionApi.Inputs(accessToken,locationTypeOrigin, type,"","", "");
            SuggestionResponse.Suggestion originSuggestion = getCarTransferSuggestion(carTransferSuggestionApiInputsOrigin, origin);
            logger.log("get originSuggestion-> " + (originSuggestion != null ? "Found" : "Not Found") + " for origin: " + origin);

            String locationTypeDestination = "LOCATION";
            String pos = originSuggestion.pin.lat + "," + originSuggestion.pin.lng;
            CarTransferSuggestionApi.Inputs carTransferSuggestionApiInputsDestination = new CarTransferSuggestionApi.Inputs(accessToken,locationTypeDestination, type,destination,pos, originSuggestion.airportIataCode);
            SuggestionResponse.Suggestion destinationSuggestion = getCarTransferSuggestion(carTransferSuggestionApiInputsDestination, destination);
            logger.log("get destinationSuggestion-> " + (destinationSuggestion != null ? "Found" : "Not Found") + " for destination: " + destination);

            CarTransferGetCatalogueIdApi.Inputs carTransferGetCatalogueApi = new CarTransferGetCatalogueIdApi.Inputs(accessToken,originSuggestion.locationId,destinationSuggestion.locationId,departureDate,pickupTime,type,pax);
            String catalogueItemId = CarTransferGetCatalogueId(carTransferGetCatalogueApi);
            logger.log("get catalogueItemId-> " + !catalogueItemId.isEmpty());

            CarTransferAddToCartApi.Inputs carTransferAddToCartApi = new CarTransferAddToCartApi.Inputs(accessToken,catalogueItemId);
            AddToCartResponseAT responseAddToCart = CarTransferAddToCart(carTransferAddToCartApi);
            String cartId = responseAddToCart.data.cartId;
            logger.log("get cartId-> " + !cartId.isEmpty());
            String detailName = "tripCode";
            String flightNumber = "GA " + CoreFunctions.generateRandomInt(3);

            CarTransferCheckOutApi.Inputs carTransferCheckOutApi = new CarTransferCheckOutApi.Inputs(accessToken,cartId,smartProfileData,detailName,flightNumber);
            CheckOutResponseAT checkOutResponseAT = CarTransferCheckOut(carTransferCheckOutApi);
            orderId= String.valueOf(checkOutResponseAT.data.orderID);
            logger.log("OrderId-> "+orderId);
            orderHash = checkOutResponseAT.data.orderHash;
            logger.log("OrderHash-> "+orderHash);

            SubmitCreditCardApi.Inputs submitPaymentApiInputs = new SubmitCreditCardApi.Inputs(accessToken, checkOutResponseAT.data.orderHash, checkOutResponseAT.data.orderID, cardNumber, cardHolderName, cardExpiredMonth, cardExpiredYear, cardCvvNumber);
            SubmitCreditCardResponse responseSubmitCc = submitCreditCardResponse(submitPaymentApiInputs);

            //hit acs url
            PaymentPage<T> paymentPage = PaymentPage.getInstance(session);
            String acsUrl = responseSubmitCc.data.thirdPartyResponse.data.acsUrl;
            logger.log("acsUrl-> " + acsUrl);
            paymentPage.openUrl(acsUrl);
            String completeOrderCode = confirmOrder(String.valueOf(checkOutResponseAT.data.orderID), adminName, adminPhoneNumber);
            logger.log("Is complete success: " + completeOrderCode);

            OrderDetailPage<T> orderDetailPage = OrderDetailPage.getInstance(session);
            TrainInternalOrderDetail trainOrderDetailResponse = orderDetailPage.getTrainInternalOrderData(String.valueOf(orderId));
            logger.log("grand total : "+trainOrderDetailResponse.data.price);
            //convert to double
            grandTotal = trainOrderDetailResponse.data.price.doubleValue();
            return orderId;
        }
        catch(Exception e){
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Airport transfer booking via API failed. Skipping the subsequent steps.");
        }
        return null;
    }


    /**
     * This method is used to create an issued order via API using Sakuku payment method.
     * It logs in the user, retrieves the smart profile data, gets the car transfer suggestions,
     * adds the car transfer to the cart, checks out, and submits the Sakuku payment.
     * Finally, it confirms the order and retrieves the order details.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param adminName The name of the admin for order confirmation.
     * @param adminPhoneNumber The phone number of the admin for order confirmation.
     * @param origin The origin location for the car transfer.
     * @param destination The destination location for the car transfer.
     * @param type The type of car transfer (e.g., "OUT", "IN").
     * @param rangeDate The date range for the car transfer.
     * @param pickupTime The pickup time for the car transfer.(10/10) will be changed to 10:10
     * @param pax The number of passengers for the car transfer.
     * @return The order ID of the created order. complete booking
     */
    @SneakyThrows
    public String createIssuedOrderApiPayUsingSakuku(
        String username,
        String password,
        String adminName,
        String adminPhoneNumber,
        String origin,
        String destination,
        String type,
        String rangeDate,
        String pickupTime,
        int pax
    ) {
    try {
        Unm<T> unm = new Unm<>(session);
        SmartProfileApi<?> smartProfileApi = new SmartProfileApi<>(session);
        LoginDataUnmResponse.Data data = unm.unmLoginApi(username, password);
        String accessToken = data.accessToken;
        boolean loginStatus = data.accountUsername.equals(username);
        logger.log("loginStatus-> " + username + ", " + loginStatus);
        String departureDate = CoreFunctions.generateRandomDate(rangeDate);
        if (pickupTime.contains("/")) {
            pickupTime = pickupTime.replace("/", ":");
        }

        SmartProfileResponse.Data smartProfileData = smartProfileApi.getSmartProfileData(accessToken, data.accountFirstName);
        logger.log("getSmartProfileData-> " + (smartProfileData != null ? "Found" : "Not Found") + " profile with first name: " + data.accountFirstName);

        if (smartProfileData == null) {
            Assertion.skip("Smart profile data not found for user: " + data.accountFirstName + ". Skipping subsequent steps.");
        }

        phoneNumber = smartProfileData.accountPhone;
        salutation = smartProfileData.accountSalutationCode;
        guestName = smartProfileData.accountFirstName + " " + smartProfileData.accountLastName;

        String locationTypeOrigin = "AIRPORT";
        CarTransferSuggestionApi.Inputs carTransferSuggestionApiInputsOrigin = new CarTransferSuggestionApi.Inputs(accessToken, locationTypeOrigin, type, "", "", "");
        SuggestionResponse.Suggestion originSuggestion = getCarTransferSuggestion(carTransferSuggestionApiInputsOrigin, origin);
        logger.log("get originSuggestion-> " + (originSuggestion != null ? "Found" : "Not Found") + " for origin: " + origin);
        if (originSuggestion == null) {
            Assertion.skip("Airport transfer origin suggestion not found for: " + origin);
        }

        String locationTypeDestination = "LOCATION";
        String pos = originSuggestion.pin.lat + "," + originSuggestion.pin.lng;
        CarTransferSuggestionApi.Inputs carTransferSuggestionApiInputsDestination = new CarTransferSuggestionApi.Inputs(accessToken, locationTypeDestination, type, destination, pos, originSuggestion.airportIataCode);
        SuggestionResponse.Suggestion destinationSuggestion = getCarTransferSuggestion(carTransferSuggestionApiInputsDestination, destination);
        logger.log("get destinationSuggestion-> " + (destinationSuggestion != null ? "Found" : "Not Found") + " for destination: " + destination);
        if (destinationSuggestion == null) {
            Assertion.skip("Airport transfer destination suggestion not found for: " + destination);
        }

        CarTransferGetCatalogueIdApi.Inputs carTransferGetCatalogueApi = new CarTransferGetCatalogueIdApi.Inputs(accessToken, originSuggestion.locationId, destinationSuggestion.locationId, departureDate, pickupTime, type, pax);
        String catalogueItemId = CarTransferGetCatalogueId(carTransferGetCatalogueApi);
        logger.log("get catalogueItemId-> " + !catalogueItemId.isEmpty());

        CarTransferAddToCartApi.Inputs carTransferAddToCartApi = new CarTransferAddToCartApi.Inputs(accessToken, catalogueItemId);
        AddToCartResponseAT responseAddToCart = CarTransferAddToCart(carTransferAddToCartApi);
        String cartId = responseAddToCart.data.cartId;
        logger.log("get cartId-> " + !cartId.isEmpty());
        String detailName = "tripCode";
        String flightNumber = "GA " + CoreFunctions.generateRandomInt(3);

        CarTransferCheckOutApi.Inputs carTransferCheckOutApi = new CarTransferCheckOutApi.Inputs(accessToken, cartId, smartProfileData, detailName, flightNumber);
        CheckOutResponseAT checkOutResponseAT = CarTransferCheckOut(carTransferCheckOutApi);
        orderId = String.valueOf(checkOutResponseAT.data.orderID);
        logger.log("OrderId-> " + orderId);
        orderHash = checkOutResponseAT.data.orderHash;
        logger.log("OrderHash-> " + orderHash);

        PaymentApi<?> paymentApi = new PaymentApi<>(session);
        String referenceId = paymentApi.submitSakukuPayment(accessToken, checkOutResponseAT.data.orderID, orderHash);
        grandTotal = paymentApi.grandTotal;
        logger.log("referenceId-> " + referenceId);

        String paymentStatus = paymentApi.paidCheckerStatus(120, orderId);
        if (!paymentStatus.equalsIgnoreCase("SUCCESS")) {
            Assertion.skip("Order not paid. Skipping the subsequent steps.");
        }

        return orderId;
    } catch (Exception e) {
        logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
        Assertion.skip("Airport transfer booking via API failed. Skipping the subsequent steps.");
    }
    return null;
    }

    @SneakyThrows
    public String createPaidOrderApiPayUsingSakuku(
            String username,
            String password,
            String origin,
            String destination,
            String type,
            String rangeDate,
            String pickupTime,
            int pax
    ) {
        try {
            Unm<T> unm = new Unm<>(session);
            SmartProfileApi<?> smartProfileApi = new SmartProfileApi<>(session);
            LoginDataUnmResponse.Data data = unm.unmLoginApi(username, password);
            String accessToken = data.accessToken;
            boolean loginStatus = data.accountUsername.equals(username);
            logger.log("loginStatus-> " + username + ", " + loginStatus);
            String departureDate = CoreFunctions.generateRandomDate(rangeDate);
            if(pickupTime.contains("/")){
                pickupTime = pickupTime.replace("/",":");
            }

            SmartProfileResponse.Data smartProfileData = smartProfileApi.getSmartProfileData(accessToken,data.accountFirstName);
            logger.log("getSmartProfileData-> " + (smartProfileData != null ? "Found" : "Not Found") + " profile with first name: " + data.accountFirstName);

            phoneNumber = smartProfileData.accountPhone;
            salutation = smartProfileData.accountSalutationCode;
            guestName = smartProfileData.accountFirstName + " " + smartProfileData.accountLastName;

            String locationTypeOrigin = "AIRPORT";
            CarTransferSuggestionApi.Inputs carTransferSuggestionApiInputsOrigin = new CarTransferSuggestionApi.Inputs(accessToken,locationTypeOrigin, type,"","", "");
            SuggestionResponse.Suggestion originSuggestion = getCarTransferSuggestion(carTransferSuggestionApiInputsOrigin, origin);
            logger.log("get originSuggestion-> " + (originSuggestion != null ? "Found" : "Not Found") + " for origin: " + origin);
            if (originSuggestion == null) {
                Assertion.skip("Airport transfer origin suggestion not found for: " + origin);
            }

            String locationTypeDestination = "LOCATION";
            String pos = originSuggestion.pin.lat + "," + originSuggestion.pin.lng;
            CarTransferSuggestionApi.Inputs carTransferSuggestionApiInputsDestination = new CarTransferSuggestionApi.Inputs(accessToken,locationTypeDestination, type,destination,pos, originSuggestion.airportIataCode);
            SuggestionResponse.Suggestion destinationSuggestion = getCarTransferSuggestion(carTransferSuggestionApiInputsDestination, destination);
            logger.log("get destinationSuggestion-> " + (destinationSuggestion != null ? "Found" : "Not Found") + " for destination: " + destination);
            if (destinationSuggestion == null) {
                Assertion.skip("Airport transfer destination suggestion not found for: " + destination);
            }

            CarTransferGetCatalogueIdApi.Inputs carTransferGetCatalogueApi = new CarTransferGetCatalogueIdApi.Inputs(accessToken,originSuggestion.locationId,destinationSuggestion.locationId,departureDate,pickupTime,type,pax);
            String catalogueItemId = CarTransferGetCatalogueId(carTransferGetCatalogueApi);
            logger.log("get catalogueItemId-> " + !catalogueItemId.isEmpty());

            CarTransferAddToCartApi.Inputs carTransferAddToCartApi = new CarTransferAddToCartApi.Inputs(accessToken,catalogueItemId);
            AddToCartResponseAT responseAddToCart = CarTransferAddToCart(carTransferAddToCartApi);
            String cartId = responseAddToCart.data.cartId;
            logger.log("get cartId-> " + !cartId.isEmpty());
            String detailName = "tripCode";
            String flightNumber = "GA " + CoreFunctions.generateRandomInt(3);

            CarTransferCheckOutApi.Inputs carTransferCheckOutApi = new CarTransferCheckOutApi.Inputs(accessToken,cartId,smartProfileData,detailName,flightNumber);
            CheckOutResponseAT checkOutResponseAT = CarTransferCheckOut(carTransferCheckOutApi);
            orderId= String.valueOf(checkOutResponseAT.data.orderID);
            logger.log("OrderId-> "+orderId);
            orderHash = checkOutResponseAT.data.orderHash;
            logger.log("OrderHash-> "+orderHash);

            PaymentApi<?> paymentApi = new PaymentApi<>(session);
            String referenceId = paymentApi.submitSakukuPayment(accessToken,checkOutResponseAT.data.orderID,orderHash);
            logger.log("referenceId-> " + referenceId);

            String paymentStatus = paymentApi.paidCheckerStatus(120,orderId);
            if (!paymentStatus.equalsIgnoreCase("SUCCESS")) Assertion.skip("Order not paid. Skipping the subsequent steps.");

            return orderId;
        }
        catch(Exception e){
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Airport transfer booking via API failed. Skipping the subsequent steps.");
        }
        return null;
    }

    @SneakyThrows
    public String createWaitingPaymentOrderApi(
            String username,
            String password,
            String origin,
            String destination,
            String type,
            String rangeDate,
            String pickupTime,
            int pax
    ) {
        return createWaitingPaymentOrderApi(username, password, origin, destination, type, rangeDate, pickupTime, pax, Optional.empty());
    }

    /**
     * This method is used to create a waiting payment order via API.
     * It logs in the user, retrieves the smart profile data, gets the car transfer suggestions,
     * adds the car transfer to the cart, checks out, and returns the order ID.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param origin The origin location for the car transfer.
     * @param destination The destination location for the car transfer.
     * @param type The type of car transfer (e.g., "OUT", "IN").
     * @param rangeDate The date range for the car transfer.
     * @param pickupTime The pickup time for the car transfer.(10/10) will be changed to 10:10
     * @param pax The number of passengers for the car transfer.
     * @return The order ID of the created order.
     */
    @SneakyThrows
    public String createWaitingPaymentOrderApi(
            String username,
            String password,
            String origin,
            String destination,
            String type,
            String rangeDate,
            String pickupTime,
            int pax,
            Optional<String> fleetName
    ){
        Unm<T> unm = new Unm<>(session);
        SmartProfileApi<?> smartProfileApi = new SmartProfileApi<>(session);
        LoginDataUnmResponse.Data data = unm.unmLoginApi(username, password);
        String accessToken = data.accessToken;
        boolean loginStatus = data.accountUsername.equals(username);
        logger.log("loginStatus-> " + username + ", " + loginStatus);
        String departureDate = CoreFunctions.generateRandomDate(rangeDate);

        SmartProfileResponse.Data smartProfileData = smartProfileApi.getSmartProfileData(accessToken,data.accountFirstName);
        logger.log("getSmartProfileData-> " + (smartProfileData != null ? "Found" : "Not Found") + " profile with first name: " + data.accountFirstName);

        String locationTypeOrigin = "AIRPORT";
        CarTransferSuggestionApi.Inputs carTransferSuggestionApiInputsOrigin = new CarTransferSuggestionApi.Inputs(accessToken,locationTypeOrigin, type,"","", "");
        SuggestionResponse.Suggestion originSuggestion = getCarTransferSuggestion(carTransferSuggestionApiInputsOrigin, origin);
        logger.log("get originSuggestion-> " + (originSuggestion != null ? "Found" : "Not Found") + " for origin: " + origin);

        String locationTypeDestination = "LOCATION";
        String pos = originSuggestion.pin.lat + "," + originSuggestion.pin.lng;
        CarTransferSuggestionApi.Inputs carTransferSuggestionApiInputsDestination = new CarTransferSuggestionApi.Inputs(accessToken,locationTypeDestination, type,destination,pos, originSuggestion.airportIataCode);
        SuggestionResponse.Suggestion destinationSuggestion = getCarTransferSuggestion(carTransferSuggestionApiInputsDestination, destination);
        logger.log("get destinationSuggestion-> " + (destinationSuggestion != null ? "Found" : "Not Found") + " for destination: " + destination);

        CarTransferGetCatalogueIdApi.Inputs carTransferGetCatalogueApi = new CarTransferGetCatalogueIdApi.Inputs(accessToken,originSuggestion.locationId,destinationSuggestion.locationId,departureDate,pickupTime,type,pax);
        String catalogueItemId = CarTransferGetCatalogueId(carTransferGetCatalogueApi, fleetName);
        logger.log("get catalogueItemId-> " + !catalogueItemId.isEmpty());

        CarTransferAddToCartApi.Inputs carTransferAddToCartApi = new CarTransferAddToCartApi.Inputs(accessToken,catalogueItemId);
        AddToCartResponseAT responseAddToCart = CarTransferAddToCart(carTransferAddToCartApi);
        String cartId = responseAddToCart.data.cartId;
        logger.log("get cartId-> " + !cartId.isEmpty());
        String detailName = "tripCode";
        String flightNumber = "GA " + CoreFunctions.generateRandomInt(3);


        CarTransferCheckOutApi.Inputs carTransferCheckOutApi = new CarTransferCheckOutApi.Inputs(accessToken,cartId,smartProfileData,detailName,flightNumber);
        CheckOutResponseAT checkOutResponseAT = CarTransferCheckOut(carTransferCheckOutApi);
        orderId= String.valueOf(checkOutResponseAT.data.orderID);
        logger.log("OrderId-> "+orderId);

        PaymentApi<?> paymentApi = new PaymentApi<>(session);
        grandTotal = paymentApi.getTotalPriceViaApi(orderId);
        return String.valueOf(orderId);
    }

    private SuggestionResponse.Suggestion getCarTransferSuggestion( CarTransferSuggestionApi.Inputs inputs,String location) throws Exception {
        CarTransferSuggestionApi carTransferSuggestionApi = new CarTransferSuggestionApi(inputs);
        ApiResponse suggestionResponse = carTransferSuggestionApi.hitApi(apiTools);
        Gson gson = new Gson();
        SuggestionResponse suggestionResponseData = gson.fromJson(suggestionResponse.getBody(), SuggestionResponse.class);
        if(suggestionResponseData.code.equals("SUCCESS") && suggestionResponseData.data != null && suggestionResponseData.data.suggestions != null && !suggestionResponseData.data.suggestions.isEmpty()) {
            logger.log("get car transfer suggestion for " + location + " with size: " + suggestionResponseData.data.suggestions.size());
            for(SuggestionResponse.Suggestion suggestion : suggestionResponseData.data.suggestions) {
                logger.log("Location: " + suggestion.name + ", ID: " + suggestion.locationId + ", Address: " + suggestion.address);
                if (suggestion.name.equalsIgnoreCase(location) || suggestion.airportIataCode.equalsIgnoreCase(location)) {
                    return suggestion;
                }
            }
        } else {
            throw new Exception("No suggestions found for " + location);
        }
        return null;
    }

    private String CarTransferGetCatalogueId(CarTransferGetCatalogueIdApi.Inputs inputs) {
        return CarTransferGetCatalogueId(inputs, Optional.empty());
    }

    private String CarTransferGetCatalogueId(CarTransferGetCatalogueIdApi.Inputs inputs, Optional<String> fleetName){
        CarTransferGetCatalogueIdApi carTransferGetCatalogueIdApi = new CarTransferGetCatalogueIdApi(inputs);
        ApiResponse carTransferGetCatalogueIdApiResponse;
        for (int i = 0; i < 3; i++) {
            carTransferGetCatalogueIdApiResponse = carTransferGetCatalogueIdApi.hitApi(apiTools);
            Gson gson = new Gson();
            GetCatalogueIdResponseAT getCatalogueIdResponseAT = null;
            try {
                getCatalogueIdResponseAT = gson.fromJson(carTransferGetCatalogueIdApiResponse.getBody(), GetCatalogueIdResponseAT.class);
            } catch (Exception e) {
                logger.log("Failed to parse GetCatalogueIdResponseAT: " + e.getMessage());
                continue;
            }
            if (getCatalogueIdResponseAT == null) {
                logger.log("Null response from API");
                continue;
            }
            if (!"SUCCESS".equals(getCatalogueIdResponseAT.code)) {
                logger.log("API response code not SUCCESS: " + getCatalogueIdResponseAT.code);
                continue;
            }
            if (getCatalogueIdResponseAT.data == null || getCatalogueIdResponseAT.data.items == null || getCatalogueIdResponseAT.data.items.isEmpty()) {
                logger.log("No items found in API response");
                continue;
            }
            boolean found = false;
            for (GetCatalogueIdResponseAT.Items items : getCatalogueIdResponseAT.data.items) {
                logger.log("carType: " + (items.carType != null ? items.carType.name : "null"));
                if (items.fleets == null || items.fleets.isEmpty()) {
                    logger.log("No fleets found for item");
                    continue;
                }
                for (GetCatalogueIdResponseAT.Fleet fleet : items.fleets) {
                    logger.log("fleetId: " + fleet.fleetId);
                    logger.log("businessName: " + fleet.businessName);
                    logger.log("businessLogoUrl: " + fleet.businessLogoUrl);
                    logger.log("name: " + fleet.name);
                    logger.log("description: " + fleet.description);
                    logger.log("initialPrice: " + fleet.initialPrice);
                    logger.log("finalPrice: " + fleet.finalPrice);
                    logger.log("carType: " + fleet.carType);
                    logger.log("catalogueItemId: " + fleet.catalogueItemId);
                    boolean check;
                    if(fleetName.isEmpty()) {
                        check = fleet.catalogueItemId != null && !fleet.catalogueItemId.isEmpty();
                    } else {
                        check = fleet.catalogueItemId != null && !fleet.catalogueItemId.isEmpty() && fleetName.get().equalsIgnoreCase(fleet.name);
                    }
                    if (check) {
                        return fleet.catalogueItemId;
                    }
                }
            }
        }
        Assertion.skip("No catalogue item found for the given inputs: " + inputs);
        return "";
    }

    private AddToCartResponseAT CarTransferAddToCart(CarTransferAddToCartApi.Inputs inputs) throws ExecutionException, InterruptedException{
        CarTransferAddToCartApi carTransferAddToCartApi = new CarTransferAddToCartApi(inputs);
        ApiResponse carTransferAddToCartApiResponse = carTransferAddToCartApi.hitApi(apiTools);
        Gson gson = new Gson();
        AddToCartResponseAT addToCartResponseAT = gson.fromJson(carTransferAddToCartApiResponse.getBody(), AddToCartResponseAT.class);
        logger.log("cartId: " + addToCartResponseAT.data.cartId);
        setDataFromAddToCart(addToCartResponseAT);
        return addToCartResponseAT;
    }

    private CheckOutResponseAT CarTransferCheckOut(CarTransferCheckOutApi.Inputs inputs)  throws ExecutionException, InterruptedException {
        CarTransferCheckOutApi carTransferCheckOutApi = new CarTransferCheckOutApi(inputs);
        ApiResponse carTransferCheckOutResponse = carTransferCheckOutApi.hitApi(apiTools);

        Gson gson = new Gson();
        CheckOutResponseAT checkOutResponse = gson.fromJson(carTransferCheckOutResponse.getBody(), CheckOutResponseAT.class);
        logger.log("orderId: " + checkOutResponse.data.orderID);
        return checkOutResponse;
    }

    private SubmitCreditCardResponse submitCreditCardResponse(SubmitCreditCardApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitCreditCardApi submitCreditCardApi = new SubmitCreditCardApi(inputs);
        ApiResponse submitCreditCard = submitCreditCardApi.hitApi(apiTools);
        Gson gson = new Gson();
        logger.log("response submit card: " + submitCreditCard.getBody());
        return gson.fromJson(submitCreditCard.getBody(), SubmitCreditCardResponse.class);
    }

    @SneakyThrows
    public String confirmOrder(String orderId,String adminName,String adminPhoneNumber){
        MemberApi<?> memberApi = new MemberApi<>(session);
        String dashboardToken = memberApi.getAdminAccessTokenDashboardAuth().replace("Bearer ", "");
        CarTransferAdminConfirmOrderApi.Inputs inputs = new CarTransferAdminConfirmOrderApi.Inputs(dashboardToken,adminName,adminPhoneNumber,orderId);
        CarTransferAdminConfirmOrderApi carRentalAdminConfirmOrderApi = new CarTransferAdminConfirmOrderApi(inputs);
        ApiResponse response = carRentalAdminConfirmOrderApi.hitApi(apiTools);
        logger.log("response confirm order: " + response.getBody());
        JSONObject data = new JSONObject(response.getBody());
        return data.getString("code");
    }

    private void setDataFromAddToCart(AddToCartResponseAT addToCartResponseAT){
        pickupPlace = addToCartResponseAT.data.origin.name;
        pickupAddress = addToCartResponseAT.data.origin.address;
        dropPlace = addToCartResponseAT.data.destination.name;
        dropAddress = addToCartResponseAT.data.destination.address;
        orderName = addToCartResponseAT.data.fleet.name;
        carType = addToCartResponseAT.data.fleet.name;
    }

}
