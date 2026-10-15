package com.tiket.page.ttd.partnerPage;

import com.tiket.model.Session;
import com.tiket.pojo.ttd.TTDPartnerPageModules;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class TTDPartnerPage<T extends WebDriver> extends BasePage<T> {

    public TTDPartnerPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TTDPartnerPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new TTDPartnerPageAndroid<>(session);}
            case IOS -> {return new TTDPartnerPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract boolean verifyPartnerPage(String keyword);
    public abstract boolean verifyBannerGrid(TTDPartnerPageModules.Datum data);
    public abstract boolean clickAndVerifyBannerGridRedirection(TTDPartnerPageModules.Datum data);
    public abstract boolean verifyImageWithChips(TTDPartnerPageModules.Datum data);
    public abstract boolean clickAndVerifyScrollImageWithChipsRedirection(TTDPartnerPageModules.Datum data);
    public abstract boolean clickAndVerifyScrollBannerCarouselRedirection(TTDPartnerPageModules.Datum data);
    public abstract void backToTTDPartnerPage();
    public abstract boolean isChipsScrollable(TTDPartnerPageModules.Datum data);
    public abstract boolean clickSeeAllBtn(TTDPartnerPageModules.Datum data);
    public abstract boolean verifyBannerCarousel(TTDPartnerPageModules.Datum data);
    public abstract boolean verifyDisplayedCurrency(String currency, String section);
    public abstract void clickBack();
    public abstract boolean isPageModuleDisplayed();
    public abstract boolean isPromoSectionDisplayed();
}
