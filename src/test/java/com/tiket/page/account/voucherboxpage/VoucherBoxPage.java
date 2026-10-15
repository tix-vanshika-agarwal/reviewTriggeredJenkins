package com.tiket.page.account.voucherboxpage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.utilities.HomePageModule;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;

public class VoucherBoxPage <T extends WebDriver> extends BasePage<T> {
    public VoucherBoxPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> VoucherBoxPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new VoucherBoxPageAndroid<>(session);}
            case IOS -> {return new VoucherBoxPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator VOUCHER_BOX_PAGE_TITLE = new Locator("VOUCHER_BOX_PAGE_TITLE", AppiumBy.xpath("//*[contains(@class,'page_title') and contains(text(),'Voucher Box')]"));
    private static final Locator BACK_BTN = new Locator("BACK_BTN", AppiumBy.xpath("//*[contains(@class,'back_button')]"));
    private static final Locator VOUCHER_PAGE_SORT_BY_NEWEST_BUTTON = new Locator("VOUCHER_PAGE_SORT_BY_NEWEST_BUTTON", AppiumBy.xpath("//*[contains(@class,'Chip_text') and (contains(text(),'Terbaru') or contains(text(),'Newest'))]"));
    private static final Locator VOUCHER_PAGE_SORT_BY_END_DATE_BTN = new Locator("VOUCHER_PAGE_SORT_BY_END_DATE_BTN", AppiumBy.xpath("//*[contains(@class,'Chip_text') and (contains(text(),'Tanggal Berakhir') or contains(text(),'End date'))]"));

    // Webviews from earlier tests stay alive, so switching by context alone lands on the wrong window.
    private static final String VOUCHER_BOX_URL_PATH = "voucher-box";

    /** Falls back to the first available webview when no window matches the voucher box url. */
    private void switchToVoucherBoxWebView() {
        if (driverTools.switchToWebViewUrl(VOUCHER_BOX_URL_PATH).isEmpty()) {
            driverTools.switchToWebView();
        }
    }

    public boolean isVoucherPageOpen() {
        driverTools.waitForWebview();
        switchToVoucherBoxWebView();
        Boolean isVoucherBoxTitleVisible = driverTools.waitForElementToAppear(VOUCHER_BOX_PAGE_TITLE,15).isPresent();
        Assertion.assertThat("Verify voucher box appear",isVoucherBoxTitleVisible,is(true));

        driverTools.switchToNative();
        return true;
    }

    private Locator getTitleOfVouchers(String title){
        return new Locator("TITLE_VOUCHER", AppiumBy.xpath("//*[contains(@class,'VoucherCodeCard_subtitle') and contains(text(),'"+title+"')]"));
    }

    public boolean checkRandomVoucherPresence(ArrayList<HomePageModule.DynamicContent> dynamicContents){
        int index = CoreFunctions.generateRandomInt(0,dynamicContents.size()-1);
        String titleOfVoucher = dynamicContents.get(index).title;
        switchToVoucherBoxWebView();
        boolean isVoucherWithGivenTitlePresent = driverTools.isElementDisplayed(getTitleOfVouchers(titleOfVoucher));
        logger.log("is random voucher title present on voucher box page " + isVoucherWithGivenTitlePresent);
        driverTools.switchToNative();
        return isVoucherWithGivenTitlePresent;
    }

    public void clickBackBtnVoucherBoxPage(){
        switchToVoucherBoxWebView();
        driverTools.click(BACK_BTN);
        driverTools.switchToNative();
    }
}
