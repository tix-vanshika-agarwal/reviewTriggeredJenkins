package com.tiket.page.common.error_msg;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;


public abstract class ErrorMessagePage<T extends WebDriver> extends BasePage<T> {

    public ErrorMessagePage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> ErrorMessagePage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new ErrorMessagePageAndroid<>(session);}
            case IOS -> {return new ErrorMessagePageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract boolean isErrorDisplayed();
    public abstract String getMessageTitle();
    public abstract String getMessageContent();
    public abstract boolean isButtonDisplayed();
    public abstract void clickButton();
}
