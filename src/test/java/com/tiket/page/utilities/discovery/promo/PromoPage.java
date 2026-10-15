package com.tiket.page.utilities.discovery.promo;

import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.utilities.PromoListApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.utilities.PromoListResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public abstract class PromoPage <T extends WebDriver> extends BasePage<T> {

    public PromoPage(Session<T> session) {
        super(session);
    }

    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//button[contains(@class,'back_button')]"));
    private static final Locator SHARE_BUTTON = new Locator("SHARE_BUTTON", AppiumBy.xpath("//div[contains(@class,'MobileRightColumn_menu_icons')]"));
    private static final Locator PROMO_DETAIL_BANNER_IMAGE = new Locator("PROMO_DETAIL_BANNER_IMAGE", AppiumBy.xpath("//img[contains(@class,'promo_detail_banner')]"));

    public static <T extends WebDriver> PromoPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new PromoPageAndroid<>(session);}
            case IOS -> {return new PromoPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void verifyOnPromoLandingPage(); // FIXME: Implementation pending
    public abstract void verifyOnPromoListPage(); // FIXME: Implementation pending
    public abstract void verifyPromoDetail(); // FIXME: Implementation pending
    public abstract void clickPromoBanner(int index);

    public boolean isPromoDetailPageValid(String bannerUrl) {
        driverTools.waitForWebview();
        // Webviews from earlier tests stay alive, so switch by the expected promo url instead of
        // taking whichever tiket.com window comes first.
        String pageUrl = driverTools.switchToWebViewUrl("promo");
        if (pageUrl.isEmpty()) {
            pageUrl = driverTools.switchToWebView();
        }
        driverTools.waitForElementToAppear(BACK_BUTTON);
        boolean isBackButtonDisplayed = driverTools.waitForElementToAppear(BACK_BUTTON,7).isPresent();
        boolean isShareButtonDisplayed = driverTools.waitForElementToAppear(SHARE_BUTTON,7).isPresent();
        boolean isBannerImageDisplayed = driverTools.waitForElementToAppear(PROMO_DETAIL_BANNER_IMAGE,7).isPresent();
        logger.log("pageUrl : " + pageUrl);
        logger.log("bannerUrl : " + bannerUrl);
        Assertion.assertThat("Verify back button present", isBackButtonDisplayed, is(true));
        Assertion.assertThat("Verify share button present", isShareButtonDisplayed, is(true));
        Assertion.assertThat("Verify banner image present", isBannerImageDisplayed, is(true));
        Assertion.assertThat("Verify promo detail is valid", pageUrl, containsString(bannerUrl));
        driverTools.switchToNative();

        return true;
    }

    private String getToken() throws ExecutionException, InterruptedException {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
    }

    @SneakyThrows
    public String getUrlPromoBannerAPI(boolean needLogin, String username, String password, int index, String language) {
        Gson gson = new Gson();

        String accessToken;
        if(needLogin){
            Unm<T> unm = new Unm<>(session);
            accessToken = unm.getAccessToken(username, password,"B2C");
            logger.log("access token: " + accessToken);
        }else {
            accessToken = getToken();
            logger.log("accessToken-> " + accessToken);
        }

        PromoListApi.Inputs input = new PromoListApi.Inputs(accessToken, language);
        PromoListApi promoListApi = new PromoListApi(input);
        ApiResponse res = promoListApi.hitApi(apiTools);
        System.out.println("Response api : " + res.getBody());

        PromoListResponse pojoResponse = gson.fromJson(res.getBody(), PromoListResponse.class);

        return pojoResponse.data.contents.get(index-1).slug.split(";")[0].split("\\?")[0];
    }

    public abstract boolean isPromoListPageValid();
    public abstract boolean isFlightTabDisplayedOnPromoPage();
    public abstract void clickBack();
}
