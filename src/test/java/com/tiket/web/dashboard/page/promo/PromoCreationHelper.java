package com.tiket.web.dashboard.page.promo;

import com.google.gson.Gson;
import com.tiket.api.discount.PromoCodeDataApi;
import com.tiket.api.discount.PromoCodeDataUpdateStatusApi;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.discount.PromoCodeDataResponse;
import com.tiket.pojo.discount.PromoCodeDataUpdateStatusResponse;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PromoCreationHelper<T extends WebDriver> extends BasePage<T> {
    public PromoCreationHelper(Session<T> session) {
        super(session);
    }

    public PromoCodeDataResponse createPromoApi(
        String username,
        String accountId,
        String tags,
        String quotaLevel,
        String quotaPromoCodeLevel,
        String code,
        String campaignName,
        String startDate,
        String endDate,
        String createdDate,
        String updatedDate,
        String budgetId) throws ExecutionException, InterruptedException {
        PromoCodeDataApi.Inputs inputs = new PromoCodeDataApi.Inputs(
            username,
            accountId,
            tags,
            quotaLevel,
            quotaPromoCodeLevel,
            code,
            campaignName,
            startDate,
            endDate,
            createdDate,
            updatedDate,
            null,
            null,
            null,
            null,
            budgetId
        );

        PromoCodeDataApi api = new PromoCodeDataApi(inputs);

        ApiResponse apiResponse = api.hitApi(apiTools);

        Gson gson = new Gson();
        System.out.println("API Response -> " + apiResponse.getBody());
        return gson.fromJson(apiResponse.getBody(), PromoCodeDataResponse.class);
    }

    public PromoCodeDataResponse createCreditCardPromoApi(
        String username,
        String accountId,
        String tags,
        String quotaLevel,
        String quotaPromoCodeLevel,
        String code,
        String campaignName,
        String startDate,
        String endDate,
        String createdDate,
        String updatedDate,
        String budgetId) throws ExecutionException, InterruptedException {
        PromoCodeDataApi.Inputs inputs = new PromoCodeDataApi.Inputs(
            username,
            accountId,
            tags,
            quotaLevel,
            quotaPromoCodeLevel,
            code,
            campaignName,
            startDate,
            endDate,
            createdDate,
            updatedDate,
            "CREDITCARD",
            List.of("411111"),
            List.of("41111100-41111199", "411111-411111"),
            "CARD_NUMBER",
            budgetId
        );

        PromoCodeDataApi api = new PromoCodeDataApi(inputs);

        ApiResponse apiResponse = api.hitApi(apiTools);

        Gson gson = new Gson();
        System.out.println("Credit Card Promo API Response -> " + apiResponse.getBody());
        return gson.fromJson(apiResponse.getBody(), PromoCodeDataResponse.class);
    }

    public PromoCodeDataUpdateStatusResponse approvePromoApi(
        String username,
        String accountId,
        String id
    ) throws ExecutionException, InterruptedException {
        PromoCodeDataUpdateStatusApi.Inputs inputs = new PromoCodeDataUpdateStatusApi.Inputs(
            username,
            accountId,
            "APPROVED",
            id
        );
        PromoCodeDataUpdateStatusApi api = new PromoCodeDataUpdateStatusApi(inputs);

        ApiResponse apiResponse = api.hitApi(apiTools);

        Gson gson = new Gson();
        return gson.fromJson(apiResponse.getBody(), PromoCodeDataUpdateStatusResponse.class);
    }
}
