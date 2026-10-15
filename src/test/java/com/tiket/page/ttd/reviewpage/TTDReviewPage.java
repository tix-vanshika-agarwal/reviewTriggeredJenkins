package com.tiket.page.ttd.reviewpage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class TTDReviewPage <T extends WebDriver> extends BasePage<T> {

    public TTDReviewPage(Session<T> session) { super(session); }

    public static <T extends WebDriver> TTDReviewPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new TTDReviewPageAndroid<>(session);}
            case IOS -> {return new TTDReviewPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }
}
