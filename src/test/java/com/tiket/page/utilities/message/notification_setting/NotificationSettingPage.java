package com.tiket.page.utilities.message.notification_setting;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class NotificationSettingPage <T extends WebDriver> extends BasePage<T> {

    public NotificationSettingPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> NotificationSettingPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new NotificationSettingPageAndroid<>(session);}
            case IOS -> {return new NotificationSettingPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void toggleNotification(String notifType, String notifChannel); // FIXME: Implementation pending
    public abstract void verifyNotificationPage(); // FIXME: Implementation pending
    public abstract void clickBackButton(); // FIXME: Implementation pending




}
