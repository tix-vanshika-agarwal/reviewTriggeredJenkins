package com.tiket.page.utilities.message.chat;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class ChatBoxPage<T extends WebDriver> extends BasePage<T> {
    public ChatBoxPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> ChatBoxPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new ChatBoxPageAndroid<>(session);}
            case IOS -> {return new ChatBoxPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract boolean verifyChatBox(String query, String orderID);
}
