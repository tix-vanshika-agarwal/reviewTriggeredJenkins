package com.tiket.page.ttd.videorentalpage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class VideoRentalPage <T extends WebDriver> extends BasePage<T> {

    public VideoRentalPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> VideoRentalPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new VideoRentalPageAndroid<>(session);}
            case IOS -> {return new VideoRentalPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void clickPlayVideo();
    public abstract boolean isPlayButtonDisplayed();
    public abstract boolean isVideoControlExisted();
}
