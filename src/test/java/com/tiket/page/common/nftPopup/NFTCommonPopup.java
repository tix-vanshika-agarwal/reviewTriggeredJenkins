package com.tiket.page.common.nftPopup;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class NFTCommonPopup <T extends WebDriver> extends BasePage<T> {
    public NFTCommonPopup(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> NFTCommonPopup<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new NFTCommonPopupAndroid<>(session);}
            case IOS -> {return new NFTCommonPopupIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void clickCloseButton();
}
