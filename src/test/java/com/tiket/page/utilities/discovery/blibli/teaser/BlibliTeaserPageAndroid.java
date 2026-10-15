package com.tiket.page.utilities.discovery.blibli.teaser;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class BlibliTeaserPageAndroid <T extends WebDriver> extends BlibliTeaserPage<T> {


    public BlibliTeaserPageAndroid(Session<T> session) {
        super(session);
    }
    @Override
    public void clickMatchAccount(String matchType){} // FIXME: Implementation pending
    @Override
    public void verifyRedirectBlibliWebview(){} // FIXME: Implementation pending
    @Override
    public void verifyRedirectConsentPage(){} // FIXME: Implementation pending
    @Override
    public void loginUsingBlibliAccount(String accountType){} // FIXME: Implementation pending
    @Override
    public void verifyEmailMatchedWithTiketAccount(){}// FIXME: Implementation pending
    @Override
    public void goBackToHomePage(){} // FIXME: Implementation pending

}
