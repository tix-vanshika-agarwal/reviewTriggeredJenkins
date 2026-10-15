package com.tiket.page.common.calendar;

import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.calendar.CalendarHolidayApi;
import com.tiket.apiPage.Unm;
import com.tiket.appium.Swipe;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.utilities.HolidayResponse;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public abstract class CalendarPage <T extends WebDriver> extends BasePage<T> {

    public CalendarPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CalendarPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new CalendarPageAndroid<>(session);}
            case IOS -> {return new CalendarPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void swipe(Swipe direction);
    public abstract boolean selectDate(String date);
    public abstract void clickNextMonthButton();
    private String getToken() throws ExecutionException, InterruptedException {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
    }

    @SneakyThrows
    public HolidayResponse getHolidayData(boolean isLogin, String username, String password, Date startDate) {
        Gson gson = new Gson();

        String accessToken = getToken();
        logger.log("accessToken non login-> " + accessToken);

        if(isLogin) {
            Unm<T> unm = new Unm<>(session);
            accessToken = unm.getAccessToken(username, password, "B2C");
            logger.log("accessToken login-> " + accessToken);
        }

        Date endDate = Date.from(
                startDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .plusMonths(1)
                        .minusDays(1)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
        );
        CalendarHolidayApi.Inputs input = new CalendarHolidayApi.Inputs(accessToken, startDate, endDate);
        CalendarHolidayApi calendarHolidayApi = new CalendarHolidayApi(input);
        ApiResponse res = calendarHolidayApi.hitApi(apiTools);
        logger.log("Response api : " + res.getBody());

        return gson.fromJson(res.getBody(), HolidayResponse.class);
    }

    public abstract boolean isHolidayValid(HolidayResponse holidayResponse, String id);
}
