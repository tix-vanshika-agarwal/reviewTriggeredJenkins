package com.tiket.pojo.order;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tiket.page.refund.RefundPage;
import com.tiket.page.refund.RefundPage.CheckRefundAbleStatus2;
import com.tiket.page.reschedule.ReschedulePage;
import com.tiket.page.reschedule.ReschedulePage.CheckRescheduleAble2;
import com.tiket.testbase.Assertion;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MapVerticalWithOrderId {
    public boolean isEmpty = true;
    public TitleAndOrderId firstOrder;
    public List<TitleAndOrderId> hotel = new ArrayList<>();
    public List<TitleAndOrderId> homes = new ArrayList<>();
    public List<TitleAndOrderId> carRental = new ArrayList<>();
    public List<TitleAndOrderId> airportTransfer = new ArrayList<>();
    public List<TitleAndOrderId> train = new ArrayList<>();
    public List<TitleAndOrderId> ttd = new ArrayList<>();
    public List<TitleAndOrderId> flight = new ArrayList<>();
    public List<TitleAndOrderId> ferry = new ArrayList<>();
    public List<TitleAndOrderId> bus = new ArrayList<>();

    public static class OrderDetailHotel extends TitleAndOrderId{
        //add other fields if needed
        public HotelInternalOrderDetail hotelInternalOrderDetail;
        public OrderDetailHotel(TitleAndOrderId baseOrder) {
            copyFields(baseOrder, this);
            Gson gson = new Gson();
            this.hotelInternalOrderDetail = gson.fromJson(internalOrderDetailJSONObject, HotelInternalOrderDetail.class);
        }

        public int getRescheduleAbleRoom(ReschedulePage<WebDriver> reschedulePage){
            CheckRescheduleAble2 filterFunction = reschedulePage.checkRescheduleableStatus2(hotelInternalOrderDetail.data.contact.email);
            int foundIndex = -1;
            String foundOrderDetailId = "";
            List<String> orderDetailIds = new ArrayList<>();
            for(HotelInternalOrderDetail.OrderDetails d : hotelInternalOrderDetail.data.orderDetails){
                if(filterFunction.check(this.orderId,String.valueOf(d.orderDetailId))){
                    foundOrderDetailId = String.valueOf(d.orderDetailId);
                }
                orderDetailIds.add(String.valueOf(d.orderDetailId));
            }
            Collections.sort(orderDetailIds);
            foundIndex = orderDetailIds.indexOf(foundOrderDetailId);
            return foundIndex;
        }

        public int getRefundAbleRoom(RefundPage<WebDriver> refundPage){
            CheckRefundAbleStatus2 filterFunction = refundPage.checkRefundAbleStatus2(hotelInternalOrderDetail.data.contact.email,String.valueOf(Integer.valueOf(hotelInternalOrderDetail.data.contact.accountId)));
            int foundIndex = -1;
            String foundOrderDetailId = "";
            List<String> orderDetailIds = new ArrayList<>();
            for(HotelInternalOrderDetail.OrderDetails d : hotelInternalOrderDetail.data.orderDetails){
                if(filterFunction.check(this.orderId,String.valueOf(d.orderDetailId))){
                    foundOrderDetailId = String.valueOf(d.orderDetailId);
                }
                orderDetailIds.add(String.valueOf(d.orderDetailId));
            }
            Collections.sort(orderDetailIds);
            foundIndex = orderDetailIds.indexOf(foundOrderDetailId);
            return foundIndex;
        }
    }
    
    public static class TitleAndOrderIdAirportTransfer extends TitleAndOrderId{
        public String fleetName;
        public String originName;
        public String destinationName;
        public String salutation;
        public String guestName;
        public String phoneNumber;

        public TitleAndOrderIdAirportTransfer(TitleAndOrderId baseOrder) {
            copyFields(baseOrder, this);
            Gson gson = new Gson();
            // Parse JSON string to JsonObject
            JsonObject jsonObject = gson.fromJson(internalOrderDetailJSONObject, JsonObject.class);
            // Extract values
            this.fleetName = jsonObject.getAsJsonObject("data")
                                            .getAsJsonArray("orderDetails").get(0)
                                            .getAsJsonObject().getAsJsonObject("vertical")
                                            .getAsJsonObject("fleet").get("fleetName").getAsString();
            this.phoneNumber = jsonObject.getAsJsonObject("data")
                                            .getAsJsonObject("contact")
                                            .get("phoneNumber").getAsString();
            this.guestName = jsonObject.getAsJsonObject("data")
                                        .getAsJsonArray("travelers").get(0)
                                        .getAsJsonObject().get("fullName").getAsString();
            this.salutation = jsonObject.getAsJsonObject("data")
                                        .getAsJsonArray("travelers").get(0)
                                        .getAsJsonObject().get("salutation").getAsString();
            String[] splittedOrderNameDetail = this.orderNameDetail.split(" - ");
            this.originName = splittedOrderNameDetail[0];
            this.destinationName = splittedOrderNameDetail[1];
        }
    }

    public static class TitleAndOrderId{
        public String orderStatus;
        public String orderName;
        public String orderNameDetail;
        public String title;
        public String orderId;
        public String orderHash;
        public String orderDetailId;
        public String subtitle;
        public String userLang;
        public String verticalName;
        public String currency;
        public String paymentSource;
        public String totalPrice;
        public String internalOrderDetailJSONObject;
        public String subOrderType;
        public ArrayList<GeneralVerticalInternalOrderDetail.OrderDetail> orderDetails;

        public <T extends TitleAndOrderId> T getDetailedVerticalData(Class<T> clazz){
            try {
                return clazz.getDeclaredConstructor(TitleAndOrderId.class).newInstance(this);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e ) {
                //Ignore exception
                Assertion.skip("Failed in parsing vertical order detail data. Exception : " + ExceptionUtils.getStackTrace(e));
            }
            return null;
        }

        protected void copyFields(Object source, Object destination) {
            Field[] fields = source.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    Field destField = destination.getClass().getField(field.getName());
                    field.setAccessible(true);
                    destField.setAccessible(true);
                    destField.set(destination, field.get(source));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    Assertion.skip("Failed in parsing vertical order data. Exception : " + ExceptionUtils.getStackTrace(e));
                }
            }
        }

        public String normalizeVerticalName(){
            switch (verticalName) {
                case "tixhotel":
                    return "hotel";
                case "event":
                    return "ttd";
                default:
                    return verticalName;
            }
        }

        public boolean checkIfMultiOrder(){
            JSONObject jsonData = new JSONObject(internalOrderDetailJSONObject);
            JSONObject ordeData = jsonData.optJSONObject("data");
            JSONArray orderAttributes = ordeData.optJSONArray("orderAttributes");
            if (orderAttributes == null || orderAttributes.isEmpty()) {
                return false;
            }

            for (int i = 0; i < orderAttributes.length(); i++) {
                JSONObject attribute = orderAttributes.optJSONObject(i);
                if (attribute != null) {
                    String key = attribute.optString("key");
                    String value = attribute.optString("value");
                    if ("isMultiOrder".equals(key) && "true".equals(value)) {
                        return true;
                    }
                }
            }
    
            return false;
        }
    }

    public MapVerticalWithOrderId filterByCurrency(String currency) {
        this.hotel.removeIf(order -> !order.currency.equalsIgnoreCase(currency));
        this.homes.removeIf(order -> !order.currency.equalsIgnoreCase(currency));
        this.carRental.removeIf(order -> !order.currency.equalsIgnoreCase(currency));
        this.airportTransfer.removeIf(order -> !order.currency.equalsIgnoreCase(currency));
        this.ferry.removeIf(order -> !order.currency.equalsIgnoreCase(currency));
        this.train.removeIf(order -> !order.currency.equalsIgnoreCase(currency));
        this.bus.removeIf(order -> !order.currency.equalsIgnoreCase(currency));
        this.flight.removeIf(order -> !order.currency.equalsIgnoreCase(currency));
        this.ttd.removeIf(order -> !order.currency.equalsIgnoreCase(currency));

        return this;
    }
}
