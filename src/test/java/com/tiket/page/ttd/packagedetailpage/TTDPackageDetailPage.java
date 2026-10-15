package com.tiket.page.ttd.packagedetailpage;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class TTDPackageDetailPage<T extends WebDriver> extends BasePage<T> {

    public TTDPackageDetailPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TTDPackageDetailPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> { return new TTDPackageDetailPageAndroid<>(session); }
            case IOS -> { return new TTDPackageDetailPageIOS<>(session); }
            default -> {throw new NotImplementedException("Not implemented");
            }
        }
    }
}
