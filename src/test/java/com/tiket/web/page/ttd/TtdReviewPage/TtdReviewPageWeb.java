package com.tiket.web.page.ttd.TtdReviewPage;

import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TtdReviewPageWeb<T extends WebDriver> extends WebPage<T> {

    public TtdReviewPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TtdReviewPageWeb<T> getInstance(Session<T> session) {
        return new TtdReviewPageWeb<>(session);
    }

    private static final Locator USER_RATING = new Locator("USER_RATING", AppiumBy.xpath("//*[contains(@class,'ReviewSummary_rating_score')]"));
    private static final Locator PAGE_TITLE = new Locator("PAGE_TITLE", AppiumBy.xpath("//*[contains(@class,'ModifiedHeaderMobile_content_slot')]/span"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//*[contains(@class,'ModifiedHeaderMobile_left_slot')]"));

    public boolean reviewPageDisplayed() {
        boolean reviewPageElements = driverTools.waitForElementToAppear(USER_RATING).isPresent();
        return reviewPageElements;
    }

    public String getPageTitle(){
        WebElement productElement = driverTools.waitForElementToPresence(PAGE_TITLE).orElseThrow();
        String pageTitle = driverTools.getText(productElement);
        logger.log("page title on the review page-> " + pageTitle);
        return pageTitle;
    }

    public  void clickBackButton(){
        driverTools.waitForElementToAppear(BACK_BUTTON);
        if (Configs.PLATFORM == Platform.ANDROID) {
            driverTools.back();
        } else {
            driverTools.click(BACK_BUTTON);
        }
    }
}
