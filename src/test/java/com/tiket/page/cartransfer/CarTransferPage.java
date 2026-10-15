package com.tiket.page.cartransfer;

import com.google.gson.Gson;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardApi;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.carTransfer.*;
import com.tiket.api.payment.SubmitCreditCardApi;
import com.tiket.apiPage.SmartProfileApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.order.orderdetail.OrderDetailPage;
import com.tiket.page.payment.PaymentPage;
import com.tiket.pojo.cartransfer.*;
import com.tiket.pojo.member.SmartProfileResponse;
import com.tiket.pojo.payment.SubmitCreditCardResponse;
import com.tiket.pojo.train.TrainInternalOrderDetail;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CarTransferPage <T extends WebDriver> extends BasePage<T> {

    public CarTransferPage (Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CarTransferPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {return new CarTransferPageIOS<>(session);}
            case ANDROID -> {return new CarTransferPageAndroid<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }
}
