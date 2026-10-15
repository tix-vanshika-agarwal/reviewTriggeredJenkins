package com.tiket.page.reschedule;

import com.tiket.api.reschedule.GETRescheduleableStatus;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

public class ReschedulePage <T extends WebDriver> extends BasePage<T>{

    public ReschedulePage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> ReschedulePage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new ReschedulePageAndroid<>(session);}
            case IOS -> {return new ReschedulePageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public interface CheckRescheduleAble {
        boolean check(MapVerticalWithOrderId.TitleAndOrderId orderData);
    }
    
    public interface  CheckRescheduleAble2 {
        boolean check(String orderId,String orderDetailId);
        
    }

    public CheckRescheduleAble2 checkRescheduleableStatus2(String username){
        return (orderId,orderDetailId) -> {
            GETRescheduleableStatus.Inputs input = new GETRescheduleableStatus.Inputs(username,orderId,orderDetailId);
            GETRescheduleableStatus getReschedulableStatus = new GETRescheduleableStatus(input);
            ApiResponse response;
            try {
                response = getReschedulableStatus.hitApi(apiTools);
                JSONObject obj = new JSONObject(response.getBody());
                logger.log("object : " + obj.toString());
                return obj.getJSONObject("data").getJSONObject("reschedulable").getBoolean("value");
            } catch (Exception e) {
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            }
            return false;
        };
    }

    public CheckRescheduleAble checkRescheduleableStatus(String username){
        return (orderData) -> {
            GETRescheduleableStatus.Inputs input = new GETRescheduleableStatus.Inputs(username,orderData.orderId,orderData.orderDetailId);
            GETRescheduleableStatus getReschedulableStatus = new GETRescheduleableStatus(input);
            ApiResponse response;
            try {
                response = getReschedulableStatus.hitApi(apiTools);
                JSONObject obj = new JSONObject(response.getBody());
                logger.log("object : " + obj.toString());
                return obj.getJSONObject("data").getJSONObject("reschedulable").getBoolean("value");
            } catch (Exception e) {
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            }
            return false;
        };
    }

    public MapVerticalWithOrderId filterRescheduleAbleOrder(MapVerticalWithOrderId mapVerticalWithOrderId,String username){
        CheckRescheduleAble filterFunction = checkRescheduleableStatus(username);
        mapVerticalWithOrderId.carRental = mapVerticalWithOrderId.carRental.stream().filter(d -> filterFunction.check(d)).toList();
        //filter Todo
        mapVerticalWithOrderId.ttd = mapVerticalWithOrderId.ttd.stream().filter(d -> filterFunction.check(d)).toList();
        //filter Hotel
        mapVerticalWithOrderId.hotel = mapVerticalWithOrderId.hotel.stream().filter(d -> filterFunction.check(d)).toList();
        //filter Homes
        mapVerticalWithOrderId.homes = mapVerticalWithOrderId.homes.stream().filter(d -> filterFunction.check(d)).toList();
        //filter AT
        mapVerticalWithOrderId.airportTransfer = mapVerticalWithOrderId.airportTransfer.stream().filter(d -> filterFunction.check(d)).toList();
        logger.log("DEBUG: Length after filter" + mapVerticalWithOrderId.ttd.size());
        if(mapVerticalWithOrderId.airportTransfer.size()==0 && mapVerticalWithOrderId.hotel.size()==0 && mapVerticalWithOrderId.ttd.size()==0 && mapVerticalWithOrderId.carRental.size()==0 && mapVerticalWithOrderId.homes.size()==0){
            mapVerticalWithOrderId.isEmpty = true;
        };
        return mapVerticalWithOrderId;
    }
    
}
