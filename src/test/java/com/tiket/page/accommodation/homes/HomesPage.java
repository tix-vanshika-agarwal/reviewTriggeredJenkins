package com.tiket.page.accommodation.homes;

import com.google.gson.Gson;
import com.tiket.api.accommodation.homes.HomesAutocompleteApi;
import com.tiket.api.accommodation.homes.HomesLandingPageApi;
import com.tiket.api.accommodation.homes.HomesLandingPageModuleApi;
import com.tiket.api.accommodation.homes.HomesLandingPagePriceAPI;
import com.tiket.api.accommodation.hotel.HotelAutocompleteApi;
import com.tiket.api.accommodation.hotel.booking.SubmitBookingApiV2;
import com.tiket.api.accommodation.hotel.prebook.PreBookGeneratingCartApi;
import com.tiket.api.accommodation.hotel.room.SelectRoomApi;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.order.CompleteOrderListApi;
import com.tiket.api.payment.SubmitPaymentApi;
import com.tiket.apiPage.HotelApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.payment.PaymentPage;
import com.tiket.pojo.accommodation.homes.HomesData;
import com.tiket.pojo.accommodation.homes.HomesLandingPageModuleData;
import com.tiket.pojo.accommodation.homes.HomesLandingPageModuleResponse;
import com.tiket.pojo.accommodation.homes.HomesNHAResponse;
import com.tiket.pojo.accommodation.hotel.HotelAutoCompleteResponse;
import com.tiket.pojo.accommodation.hotel.SubmitBookingResponse;
import com.tiket.pojo.order.CompleteOrderListResponse;
import com.tiket.pojo.room.SelectRoomResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.*;

public abstract class HomesPage <T extends WebDriver> extends BasePage<T> {

    public HomesPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HomesPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {
                return new HomesPageIOS<>(session);
            }
            case ANDROID -> {
                return new HomesPageAndroid<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }


    String hotelRoomName;
    int orderId;
    BigInteger grandTotal;
    String nativeHotelColor = "#0064D2";
    public List<String> guestNames;

    @SneakyThrows
    public HomesData getContentLandingPageUsingPojo(boolean login, String username, String password, String language, String section) {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        accessToken = hotelApi.setLoginStatus(accessToken, login, username, password);
        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        HomesLandingPageApi.Inputs homesLandingPageApiInputs = new HomesLandingPageApi.Inputs(login, accessToken, username, userAgent, getLanguage(language), section);
        HomesData data = homesLandingPage(homesLandingPageApiInputs);
        logger.log("response-> " + data.toString());

        return data;
    }

    @SneakyThrows
    private HomesData homesLandingPage(HomesLandingPageApi.Inputs inputs) {
        HomesLandingPageApi homesLandingPageApi = new HomesLandingPageApi(inputs);
        ApiResponse apiResponse = homesLandingPageApi.hitApi(apiTools);
        Gson gson = new Gson();
        HomesNHAResponse homesNHAResponse = gson.fromJson(apiResponse.getBody(), HomesNHAResponse.class);

        ArrayList<HomesNHAResponse.List> listArray = homesNHAResponse.data.list;
        int totalData = listArray.size();
        int index = 0;
        for (int i = 0; i < totalData; i++) {
            logger.log("index loop -> " + i);
            HomesNHAResponse.List listData = listArray.get(i);
            String templateCode = listData.templateCode;

            if (templateCode.equals(inputs.templateCode())) {
                boolean hasLoginOnly = listData.content.loginOnly;
                logger.log("has loginOnly-> " + hasLoginOnly);

                boolean remainingTime = true;
                if (templateCode.equals("TIMED_PROMO"))
                    remainingTime = listData.content.remainingTime != 0;
                logger.log("has remainingTime-> " + remainingTime);

                if (inputs.login() && remainingTime) {
                    if (hasLoginOnly) {
                        String loginOnly = String.valueOf(listData.content.loginOnly);
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

                } else if (!remainingTime) {
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
        }

        HomesNHAResponse.List list = listArray.get(index);
        HomesNHAResponse.Content content = listArray.get(index).content;
        HomesData data = new HomesData();
        switch (inputs.templateCode()) {
            case "HERO_BANNER":
                data = heroBannerData(content);
                break;
            case "TIMED_PROMO":
                data = timedPromoData(list);
                break;
            case "MULTIPLE_HOTEL_ID":
                data = getMultipleHotelId(content);
                break;
            case "MINI_CARD_LIST":
                data = miniCardData(content);
                break;
            case "LANDSCAPE_BANNER":
                data = landscapeBannerData(content);
                break;
            case "THREE_POINTS_BANNER":
                data = threePointsBannerData(content);
                break;
            case "SIMILAR_PROPERTIES":
                // FIXME: Pending implementation
                break;
        }
        return data;
    }

    private HomesData miniCardData(HomesNHAResponse.Content content) {
        HomesData data = new HomesData();

        ArrayList<String> listData = new ArrayList<>();
        for (HomesNHAResponse.List it : content.list) {
            listData.add(it.title);
        }

        data.title = content.title;
        data.list = content.list;
        data.contentListTitle = listData;
        return data;
    }

    private HomesData getMultipleHotelId(HomesNHAResponse.Content content) {
        HomesData data = new HomesData();

        ArrayList<HomesData.ContentData> listData = new ArrayList<>();
        for (HomesNHAResponse.List it : content.list) {
            HomesData.ContentData newList = new HomesData.ContentData();
            newList.location = it.location;
            logger.log("location -> " + newList.location);
            newList.buttonText = it.buttonText;
            newList.redirectTarget = it.redirectTarget;
            newList.redirectLocation = it.redirect.location;
            logger.log("redirect location -> " + newList.redirectLocation);
            listData.add(newList);
        }
        data.contentDataList = listData;
        data.title = content.title;
        data.subTitle = content.subTitle;
        logger.log("title -> " + data.title);
        logger.log("subtitle -> " + data.subTitle);
        return data;
    }

    private HomesData heroBannerData(HomesNHAResponse.Content content) {
        HomesData data = new HomesData();
        ArrayList<String> moduleData = new ArrayList<>();
        for (HomesNHAResponse.Module module : content.modules) {
            moduleData.add(module.title);
        }

        data.title = content.title;
        data.modules = moduleData;
        return data;
    }

    private HomesData landscapeBannerData(HomesNHAResponse.Content content) {
        HomesData data = new HomesData();

        ArrayList<String> listData = new ArrayList<>();
        for (HomesNHAResponse.List list : content.list) {
            listData.add(list.title);
        }

        data.title = content.title;
        data.subTitle = content.subTitle;
        data.contentListTitle = listData;
        return data;
    }

    private HomesData threePointsBannerData(HomesNHAResponse.Content content) {
        HomesData data = new HomesData();

        ArrayList<HomesData.ListOfTitleAndSubTitle> listTitleAndSubtitle = new ArrayList<>();
        for (HomesNHAResponse.List list : content.list) {
            HomesData.ListOfTitleAndSubTitle newObj = new HomesData.ListOfTitleAndSubTitle();

            newObj.title = list.title;
            newObj.subTitle = list.subTitle;
            logger.log("title from api -> " + newObj.title);
            logger.log("sub-title from api -> " + newObj.subTitle);

            listTitleAndSubtitle.add(newObj);
        }

        data.title = content.title;
        data.subTitle = content.subTitle;
        data.showButton = content.showButton;
        data.buttonText = content.buttonText;
        data.listOfTitleAndSubTitles = listTitleAndSubtitle;

        HomesData.ContentData contentData = new HomesData.ContentData();
        contentData.redirectTarget = content.redirectTarget;
        contentData.redirectLocation = content.redirect.location;
        data.contentData = contentData;

        return data;
    }

    private HomesData timedPromoData(HomesNHAResponse.List list) {
        HomesData data = new HomesData();

        int totalList = list.content.list.size();
        ;
        logger.log("totalList-> " + totalList);
        int index;
        if (totalList > 4) totalList = 4;
        if (totalList > 1) {
            index = generateRandomInt(1, totalList);
        } else {
            index = 1;
        }
        logger.log("selected timed promo module index-> " + index);
        HomesNHAResponse.List listInsideContentElement = list.content.list.get(index - 1);
        HomesData.ContentListData contentListData = new HomesData.ContentListData();
        contentListData.name = listInsideContentElement.name;
        contentListData.buttonText = listInsideContentElement.buttonText;
        contentListData.location = listInsideContentElement.location;
        contentListData.redirectTarget = listInsideContentElement.redirectTarget;
        contentListData.locationInsideListRedirect = listInsideContentElement.redirect.location;
        contentListData.locationTypeInsideListRedirect = listInsideContentElement.redirect.locationType;

        data.contentListData = contentListData;
        data.landingPageId = list.id;
        data.templateCode = list.templateCode;
        data.subsidyLabel = list.content.subsidyLabel;
        data.index = index;

        return data;
    }

    @SneakyThrows
    public HomesLandingPageModuleData getHomesLandingPageModule(boolean login, String username, String password, String language, String landingPageId, String landingPageName, String templateCode) {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        HotelApi<?> hotelApi = new HotelApi<>(session);
        accessToken = hotelApi.setLoginStatus(accessToken, login, username, password);
        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        HomesLandingPageModuleApi.Inputs homesLandingPageModuleApiInputs = new HomesLandingPageModuleApi.Inputs(accessToken, username, userAgent, getLanguage(language), landingPageId, landingPageName);
        HomesLandingPageModuleData data = homesLandingPageModule(homesLandingPageModuleApiInputs, templateCode);
        logger.log("homesModule-> " + data.homesList.size());
        return data;
    }

    @SneakyThrows
    private HomesLandingPageModuleData homesLandingPageModule(HomesLandingPageModuleApi.Inputs inputs, String templateCode) {
        HomesLandingPageModuleApi homesLandingPageModuleApi = new HomesLandingPageModuleApi(inputs);
        ApiResponse apiResponse = homesLandingPageModuleApi.hitApi(apiTools);

        Gson gson = new Gson();
        HomesLandingPageModuleResponse homesLandingPageModuleResponse = gson.fromJson(apiResponse.getBody(), HomesLandingPageModuleResponse.class);

        HomesLandingPageModuleData data = new HomesLandingPageModuleData();
        HomesLandingPageModuleResponse.Chips chips = homesLandingPageModuleResponse.data.chips;

        switch (templateCode) {
            case "TIMED_PROMO": {
                data = getTimedPromoHomes(chips);
            }
        }

        return data;
    }

    private HomesLandingPageModuleData getTimedPromoHomes(HomesLandingPageModuleResponse.Chips chips) {
        ArrayList<String> homesData = new ArrayList<>();
        for (HomesLandingPageModuleResponse.Hotel hotel : chips.hotels) {
            String homesName = hotel.name;
            logger.log("homes name api -> " + homesName);
            homesData.add(homesName);
        }
        HomesLandingPageModuleData data = new HomesLandingPageModuleData();
        data.homesList = homesData;
        return data;
    }

    private String getMonth(String rangeAddDate, String language) {
        String date = generateRandomDate(rangeAddDate, "yyyy-M-d");
        String[] split = date.split("-");
        int month = Integer.parseInt(split[1]);
        String monthName = getMonthName(month, getLanguage(language));
        if (monthName.length() > 3) monthName = monthName.substring(0, 3);
        return split[2] + " " + monthName;
    }

    @SneakyThrows
    public String expectedDateRoom(String language) {
        String checkIn = getMonth("1-1", language);
        String checkOut = getMonth("2-2", language);
        String guest = "1 Tamu";
        if (getLanguage(language).equals("en")) {
            guest = "1 Guest";
        }
        logger.log("expectedDateRoomGuest-> " + checkIn + " - " + checkOut + " • " + guest);
        return checkIn + " - " + checkOut + " • " + guest;
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
            String lang,
            String currency
    ) {
        try {
            Unm<T> unm = new Unm<>(session);
            String accessToken = unm.getAccessToken(username, password, "B2C");
            logger.log("accessToken-> " + accessToken);

            HomesAutocompleteApi.Inputs hotelAutocompleteApiInputs = new HomesAutocompleteApi.Inputs(accessToken, username, query);
            String hotelId = hotelAutoComplete(hotelAutocompleteApiInputs);
            logger.log("hotelId-> " + hotelId);

            SelectRoomApi.Inputs selectRoomApiInputs = new SelectRoomApi.Inputs(accessToken, username, adult, hotelId, night, room, startDate, lang, "");
            SelectRoomResponse.RatePlan selectedRoom = selectRoom(selectRoomApiInputs);
            String roomName = selectRoomName(selectRoomApiInputs);
            hotelRoomName = roomName;
            int maxRoomOccupancy = selectedRoom.maxAdultOccupancy;
            int maxChildOccupancy = selectedRoom.maxChildOccupancy;
            logger.log("hotelRoomName-> " + hotelRoomName);

            PreBookGeneratingCartApi.Inputs preBookCartApiInputs = new PreBookGeneratingCartApi.Inputs(accessToken, username, adult, hotelId, night, selectedRoom.rateCode, room, selectedRoom.roomId, startDate,maxRoomOccupancy,maxChildOccupancy,currency);
            HotelApi<?> hotelApi = new HotelApi<>(session);
            String cartID = hotelApi.prebook(preBookCartApiInputs);
            logger.log("cartID-> " + cartID);

            SubmitBookingApiV2.Inputs submitBookingApiInputs = new SubmitBookingApiV2.Inputs(accessToken, username, cartID, username, fullName, nameTitle, guestFullName, guestTitle, room, "", false, "", lang, hotelId, startDate);
            SubmitBookingResponse submitBookingResponse = submitBooking(submitBookingApiInputs);
            orderId = submitBookingResponse.data.orderId;
            logger.log("orderId-> " + orderId);

            SubmitPaymentApi.Inputs submitPaymentApiInputs = new SubmitPaymentApi.Inputs(submitBookingResponse.data.orderHash, orderId);
            JSONObject paymentResponse = submitPayment(submitPaymentApiInputs);
            grandTotal = paymentResponse.getJSONObject("sidebarPayment").getBigInteger("grandTotal");
            logger.log("grandTotal-> " + grandTotal);

            String paymentChecker = PaymentPage.getInstance(session).paymentChecker();
            return paymentChecker.equalsIgnoreCase("success");
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Product Booking via API failed. Skipping the subsequent steps.");
        }
        return false;
    }

    private boolean CompleteOrderList(CompleteOrderListApi.Inputs inputs, long orderId) throws ExecutionException, InterruptedException {
        logger.log("*********initial order id**********" + orderId);
        Thread.sleep(1000);
        CompleteOrderListApi CompleteOrderListApi = new CompleteOrderListApi(inputs);
        ApiResponse apiResponse = CompleteOrderListApi.hitApi(apiTools);
        CompleteOrderListResponse response = new Gson().fromJson(apiResponse.getBody(), CompleteOrderListResponse.class);
        logger.log("*********Response after**********" + apiResponse.getBody());
        for (int i = 0; i < response.data.views.size() - 1; i++) {
            logger.log("*********Response 1st loop**********" + response.data.views.get(i).id);
            if ("activeCards".equals(response.data.views.get(i).id)) {
                int sizeOfCards = response.data.views.get(i).activeCards.cards.size();
                int j = 0;
                do {
                    String cardOrderIdString = response.data.views.get(i).activeCards.cards.get(j).orderData.orderId;
                    logger.log("*********Response Order Id************" + cardOrderIdString);

                    long cardOrderId = Long.parseLong(cardOrderIdString);
                    if (orderId == cardOrderId) {
                        return true;
                    }
                    j++;
                }
                while (j < sizeOfCards - 1);
            } else if (i == response.data.views.size() - 1) {
                return false;
            }
        }
        return false;
    }

    private String getToken() throws ExecutionException, InterruptedException {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer " + accessToken;
    }

    private HotelAutoCompleteResponse hotelAutoComplete(HotelAutocompleteApi.Inputs inputs) throws ExecutionException, InterruptedException {
        HotelAutocompleteApi hotelAutocompleteApi = new HotelAutocompleteApi(inputs);
        ApiResponse apiResponse = hotelAutocompleteApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), HotelAutoCompleteResponse.class);
    }

    private String hotelAutoComplete(HomesAutocompleteApi.Inputs inputs) throws ExecutionException, InterruptedException {
        HomesAutocompleteApi hotelAutocompleteApi = new HomesAutocompleteApi(inputs);
        ApiResponse hotelAutoCompleteResponse = hotelAutocompleteApi.hitApi(apiTools);
        JSONObject hotelAutoCompleteResponseObject = new JSONObject(hotelAutoCompleteResponse.getBody());

        JSONArray dataArray = hotelAutoCompleteResponseObject.getJSONObject("data").getJSONArray("contents");
        JSONObject data = (JSONObject) dataArray.get(0);
        return data.getString("publicId");
    }

    private SelectRoomResponse.RatePlan selectRoom(SelectRoomApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
        ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);

        return selectRoomResponse.data.rooms.stream()
                .filter(roomList -> roomList.ratePlans.get(0).availableRoom >= inputs.room())
                .findFirst()
                .orElseThrow()
                .ratePlans
                .stream()
                .findFirst()
                .orElseThrow();
    }

    private String selectRoomName(SelectRoomApi.Inputs inputs) throws ExecutionException, InterruptedException {
        SelectRoomApi selectRoomApi = new SelectRoomApi(inputs);
        ApiResponse selectRoomApiResponse = selectRoomApi.hitApi(apiTools);
        Gson gson = new Gson();
        SelectRoomResponse selectRoomResponse = gson.fromJson(selectRoomApiResponse.getBody(), SelectRoomResponse.class);

        return selectRoomResponse.data.rooms.stream()
                .filter(roomList -> roomList.ratePlans.get(0).availableRoom >= inputs.room())
                .findFirst()
                .orElseThrow()
                .groupName;
    }

    private SubmitBookingResponse submitBooking(SubmitBookingApiV2.Inputs inputs) throws ExecutionException, InterruptedException {
        SubmitBookingApiV2 submitBookingApi = new SubmitBookingApiV2(inputs);
        ApiResponse submitBookingApiResponse = submitBookingApi.hitApi(apiTools);
        Gson gson = new Gson();
        return gson.fromJson(submitBookingApiResponse.getBody(), SubmitBookingResponse.class);
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

    public int getOrderId() {
        logger.log("orderId-> " + orderId);
        return orderId;
    }

    public String getHotelRoomName() {
        logger.log("hotelRoomName-> " + hotelRoomName);
        return hotelRoomName;
    }

    public String issuedOrderData(String data) {
        HashMap<String, String> issuedOrder = new HashMap<>();
        issuedOrder.put("roomName", hotelRoomName);
        issuedOrder.put("orderId", String.valueOf(orderId));
        issuedOrder.put("totalPrice", String.valueOf(grandTotal));
        return issuedOrder.get(data);
    }

    @SneakyThrows
    private Map<String, String> getHomesLandingPagePriceData(String language, String latitude, String longitude,String ProductName,String currency) {
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        String userAgent = CoreFunctions.userAgentValue();
        logger.log("userAgent-> " + userAgent);

        HomesLandingPagePriceAPI.Inputs homesLandingPagePriceAPIInputs = new HomesLandingPagePriceAPI.Inputs(accessToken, userAgent, getLanguage(language), latitude, longitude,currency);
        HomesLandingPagePriceAPI homesLandingPagePriceAPI = new HomesLandingPagePriceAPI(homesLandingPagePriceAPIInputs);
        ApiResponse apiResponse = homesLandingPagePriceAPI.hitApi(apiTools);
        JSONObject response = new JSONObject(apiResponse.getBody());
        Map<String, String> result = new HashMap<>();
        int dataSize = response.getJSONArray("data").length();
        logger.log("dataSize-----"+dataSize);
        for(int i = 0; i < dataSize-1; i++) {
            logger.log("i-----"+i);
            int cardSize = 0;
            try {
                cardSize = response.getJSONArray("data").getJSONObject(i).getJSONArray("dynamicContents").getJSONObject(0).getJSONArray("cards").length();
                logger.log("cardSize-----"+cardSize);
            } catch (Exception e) {
                continue;
            }

            for (int j = 0; j < cardSize; j++) {
                String productName = response.getJSONArray("data").getJSONObject(i)
                        .getJSONArray("dynamicContents").getJSONObject(0)
                        .getJSONArray("cards").getJSONObject(j).getString("productName");
                logger.log("Product Name API-> " + productName);
                if (productName.equals(ProductName) ) {
                    String basePrice =  String.valueOf(response.getJSONArray("data").getJSONObject(i)
                            .getJSONArray("dynamicContents").getJSONObject(0)
                            .getJSONArray("cards").getJSONObject(j).getJSONObject("rateInfo").getInt("basePrice"));

                    String discountedPrice = String.valueOf(response.getJSONArray("data").getJSONObject(i)
                            .getJSONArray("dynamicContents").getJSONObject(0)
                            .getJSONArray("cards").getJSONObject(j).getJSONObject("rateInfo").getInt("discountedPrice"));

                    result.put("basePrice", basePrice);
                    result.put("discountedPrice", discountedPrice);
                    result.put("productName", productName);
                    break;
                }
            }
        }
        return result;
    }

    public Map<String, String> verifyHomesLandingPagePriceData(String language, String latitude, String longitude, String productName,String currency) {
        Map<String, String> priceData = getHomesLandingPagePriceData(language, latitude, longitude,productName,currency);
        logger.log("Base Price API-> " + priceData.get("basePrice"));
        logger.log("Discounted Price API-> " + priceData.get("discountedPrice"));
        logger.log("Product Name API-> " + priceData.get("productName"));
        return priceData;
    }
}