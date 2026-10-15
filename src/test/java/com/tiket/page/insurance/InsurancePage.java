package com.tiket.page.insurance;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.Matchers;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.core.Is.is;

public class InsurancePage<T extends WebDriver> extends BasePage<T> {
    public InsurancePage(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> InsurancePage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {return new InsurancePageIOS<>(session);}
            case ANDROID -> {return new InsurancePageAndroid<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator WELCOME_INSURANCE_PAGE_CLOSE_BTN = new Locator("WELCOME_INSURANCE_PAGE_CLOSE_BTN", AppiumBy.xpath("//*[contains(@class,'close_button')]"));
    private static final Locator INSURANCE_PAGE_TITLE = new Locator("INSURANCE_PAGE_TITLE", AppiumBy.xpath("//h1[(contains(text(),'Insurance') or contains(text(),'Asuransi'))]"));
    private static final Locator INSURANCE_TITLE_MY_HEADER = new Locator("INSURANCE_TITLE_MY_HEADER", AppiumBy.xpath("//*[contains(@class,'MyOrder_header')]"));
    private static final Locator INSURANCE_LOGIN_BTN = new Locator("INSURANCE_LOGIN_BTN", AppiumBy.xpath("//button[contains(@class,'NotLoggedIn_button')]"));
    private static final Locator MY_INSURANCE_BTN = new Locator("MY_INSURANCE_BTN", AppiumBy.xpath("//*[contains(@class,'InsuranceBar_title') and (contains(text(),'My Insurance') or contains(text(),'Asuransimu') or contains(text(),'Pusat Klaim') or contains(text(),'Claim Center'))]"));
    private static final Locator STANDALONE_INSURANCE_ADD_BTN = new Locator("STANDALONE_INSURANCE_ADD_BTN", AppiumBy.xpath("//button[contains(@data-testid,'add-protection') and not(contains(@class,'Button_state_disabled'))]"));


    public void removeWelcomePopup(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("insurance");
        driverTools.waitForElementToAppear(WELCOME_INSURANCE_PAGE_CLOSE_BTN).ifPresent(driverTools::click);
        driverTools.switchToNative();
    }

    public boolean isOnInsuranceLandingPage(boolean isLogin){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("insurance");
        String url = driverTools.getCurrentURL();
        Assertion.assertThat("is url contains insurance",url.toLowerCase(), Matchers.containsString("insurance"));

        boolean isLandingPageContainsCorrectTitle = driverTools.waitForElementToPresence(INSURANCE_PAGE_TITLE).isPresent();
        logger.log("is landing page contains correct title " + isLandingPageContainsCorrectTitle);
        Assertion.assertThat("is landing page contains correct title",isLandingPageContainsCorrectTitle,is(true));

        boolean isTitleLoginCorrect = driverTools.waitForElementToPresence(INSURANCE_TITLE_MY_HEADER).isPresent();
        logger.log("is page contains correct title expected for login " + isTitleLoginCorrect);
        Assertion.assertThat("is on landing page of Insurance Claim Landing Page with my order header",isTitleLoginCorrect,is(true));

        boolean isTitleNonLoginCorrect = driverTools.waitForElementToPresence(INSURANCE_LOGIN_BTN).isPresent();
        logger.log("is page contains login btn " + isTitleNonLoginCorrect);
        // isLogin -> true means there will not be login button and vice versa isLogin -> false means there will be login button
        Assertion.assertThat("is login btn present that signify guest condition ",isTitleNonLoginCorrect,is(!isLogin));
        driverTools.switchToNative();
        return isLandingPageContainsCorrectTitle && isTitleLoginCorrect ;
    }

    public void clickLoginBtn(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("insurance");
        driverTools.clickWithJavascriptExecutor(INSURANCE_LOGIN_BTN);
        driverTools.switchToNative();
    }

    public void openClaimCenterPage(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("insurance");
        driverTools.clickWithJavascriptExecutor(MY_INSURANCE_BTN);
        driverTools.switchToNative();
    }

    public boolean isStandAloneInsuranceAddBtnDisplayed(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("insurance");
        boolean isAddBtnDisabled = driverTools.waitForElementToPresence(STANDALONE_INSURANCE_ADD_BTN).isPresent();
        logger.log("is standalone insurance add btn displayed " + isAddBtnDisabled);
        driverTools.switchToNative();
        return isAddBtnDisabled;
    }
}
