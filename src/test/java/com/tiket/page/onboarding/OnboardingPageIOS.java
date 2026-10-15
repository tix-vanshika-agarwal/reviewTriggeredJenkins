package com.tiket.page.onboarding;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.home.HomePage;
import com.tiket.page.home.HomePageIOS;
import com.tiket.page.setting.SettingPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class OnboardingPageIOS<T extends WebDriver> extends OnboardingPage<T> {

    private static final Locator ONBOARDING_TITLE = new Locator("ONBOARDING_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'start your journey') or contains(@name,'perjalanan')]"));
    private static final Locator CURRENCY_LABEL = new Locator("CURRENCY_LABEL", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Currency' or @name='Mata Uang']"));
    private static final Locator CURRENCY_ROW_VALUE = new Locator("CURRENCY_ROW_VALUE", AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Currency' or @name='Mata Uang']/following-sibling::XCUIElementTypeStaticText[1]"));

    public OnboardingPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public boolean isOnboardingPageDisplayed() {
        return isOnboardingPageDisplayed(20);
    }

    @Override
    public boolean isOnboardingPageDisplayed(int seconds) {
        return driverTools.waitForElementToAppear(ONBOARDING_TITLE, seconds).isPresent();
    }

    @Override
    public void changeCurrency(String currency) {
        // The onboarding currency selector is the same bottom sheet used in settings, so reuse it.
        driverTools.waitForElementToAppear(CURRENCY_LABEL, 10).orElseThrow();
        driverTools.click(CURRENCY_LABEL);
        SettingPage.getInstance(session).selectCurrencyFromOpenBottomSheet(currency);
    }

    @Override
    public String getSelectedCurrency() {
        return driverTools.waitForElementToAppear(CURRENCY_ROW_VALUE, 10).orElseThrow().getText();
    }

    @Override
    public void finishOnboardingBySkipping() {
        // Reuse the proven onboarding-dismissal flow (regional continue + permission alerts + skip
        // carousel), but WITHOUT terminating/re-launching the app, so the Welcome Rewards sheet survives.
        HomePage<T> homePage = HomePage.getInstance(session);
        ((HomePageIOS<T>) homePage).completeFreshInstallOnboarding();

        // Flag onboarding done for this pooled session ONLY when we truly left the onboarding flow, i.e. the
        // "start your journey" screen actually goes away. waitForElementToDisappear tolerates slow-device
        // transitions (waits up to 10s) and is reliable even when the Welcome Rewards sheet overlays home
        // (where a home-icon check would be unreliable). This avoids both flagging a driver as onboarded
        // while it is still stuck in onboarding AND failing to flag it after a genuine completion.
        if (driverTools.waitForElementToDisappear(ONBOARDING_TITLE, 10)) {
            homePage.markOnboardingComplete();
        }
    }
}
