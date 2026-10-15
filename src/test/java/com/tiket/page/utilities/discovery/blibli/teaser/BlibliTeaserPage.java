package com.tiket.page.utilities.discovery.blibli.teaser;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class BlibliTeaserPage <T extends WebDriver> extends BasePage<T> {

    public BlibliTeaserPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> BlibliTeaserPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new BlibliTeaserPageAndroid<>(session);}
            case IOS -> {return new BlibliTeaserPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void clickMatchAccount(String matchType); // FIXME: Implementation pending
    public abstract void verifyRedirectBlibliWebview(); // FIXME: Implementation pending
    public abstract void verifyRedirectConsentPage(); // FIXME: Implementation pending
    public abstract void loginUsingBlibliAccount(String accountType);// FIXME: Implementation pending
    public abstract void verifyEmailMatchedWithTiketAccount();// FIXME: Implementation pending
    public abstract void goBackToHomePage(); // FIXME: Implementation pending




}
