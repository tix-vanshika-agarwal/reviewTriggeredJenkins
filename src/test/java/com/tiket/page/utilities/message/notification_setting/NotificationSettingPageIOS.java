package com.tiket.page.utilities.message.notification_setting;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class NotificationSettingPageIOS <T extends WebDriver> extends NotificationSettingPage<T>{

    public NotificationSettingPageIOS(Session<T> session) {
        super(session);
    }
    @Override
    public void toggleNotification(String notifType, String notifChannel){} // FIXME: Implementation pending
    @Override
    public void verifyNotificationPage(){} // FIXME: Implementation pending
    @Override
    public void clickBackButton(){} // FIXME: Implementation pending
}
