package com.tiket.page.account.reviewpage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class ReviewPage<T extends WebDriver> extends BasePage<T> {
    public ReviewPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> ReviewPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new ReviewPageAndroid<>(session);}
            case IOS -> {return new ReviewPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator REVIEW_PAGE_TITLE_WEB_VIEW = new Locator("REVIEW_PAGE_TITLE_WEB_VIEW", AppiumBy.xpath("//*[contains(@class,'review_title') and (contains(text(),'Your Review') or contains(text(),'Review Kamu'))]"));

    public boolean isReviewPageOpen(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("myreview");
        String getUrlOfPage = driverTools.getCurrentURL();
        boolean isUrlContainsReviewPage = getUrlOfPage.toLowerCase().contains("myreview");
        logger.log("does url contains myreview" + isUrlContainsReviewPage);

        boolean isReviewPageTitleDisplayed = driverTools.isElementDisplayed(REVIEW_PAGE_TITLE_WEB_VIEW);
        logger.log("is review page title displayed " + isReviewPageTitleDisplayed);
        driverTools.switchToNative();
        return isUrlContainsReviewPage && isReviewPageTitleDisplayed;
    }
}
