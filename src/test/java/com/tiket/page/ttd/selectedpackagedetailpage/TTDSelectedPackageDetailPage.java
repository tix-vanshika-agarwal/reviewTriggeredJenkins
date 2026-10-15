package com.tiket.page.ttd.selectedpackagedetailpage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public class TTDSelectedPackageDetailPage<T extends WebDriver> extends BasePage<T> {

    public TTDSelectedPackageDetailPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TTDSelectedPackageDetailPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new TTDSelectedPackageDetailPageAndroid<>(session); }
            case IOS -> { return new TTDSelectedPackageDetailPageIOS<>(session); }
            default -> {throw new NotImplementedException("Not implemented");
            }
        }
    }

    public boolean isLoginPopupDisplayed() {
        //Usage only for Tiket Live that is already depreceated
        return false;
    }

    public void clickLoginButtonPopup() {
        //Usage only for Tiket Live that is already depreceated
    }

}
