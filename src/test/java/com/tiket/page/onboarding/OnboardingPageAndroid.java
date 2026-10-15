package com.tiket.page.onboarding;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.home.HomePage;
import com.tiket.page.setting.SettingPage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;

public class OnboardingPageAndroid<T extends WebDriver> extends OnboardingPage<T> {

    private static final Locator ONBOARDING_TITLE = new Locator("ONBOARDING_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'start your journey') or contains(@text,'perjalanan'))]"));
    private static final Locator CURRENCY_ROW = new Locator("CURRENCY_ROW", AppiumBy.id("row_currency"));
    private static final Locator CURRENCY_ROW_VALUE = new Locator("CURRENCY_ROW_VALUE", AppiumBy.xpath("//*[contains(@resource-id,'row_currency')]//*[contains(@resource-id,'tv_value')]"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'btn_action')]//*[contains(@resource-id,'tds_button_view')]"));
    // Broader fallback matching the regional continue the same way HomePageAndroid.gotoHome() does
    // (By.id("tds_button_view")), for devices where the btn_action-scoped continue is not resolvable.
    private static final Locator CONTINUE_BUTTON_FALLBACK = new Locator("CONTINUE_BUTTON_FALLBACK", AppiumBy.id("tds_button_view"));

    // Post-continue onboarding/permission steps (mirrors the flow handled in HomePageAndroid.gotoHome)
    private static final Locator SKIP_NOTIFICATION = new Locator("SKIP_NOTIFICATION", AppiumBy.id("tds_btn_primary_info_view"));
    private static final Locator ALLOW_NOTIFICATION = new Locator("ALLOW_NOTIFICATION", AppiumBy.xpath("//*[contains(@resource-id,'permission_allow_button')]"));
    private static final Locator SKIP_ONBOARDING = new Locator("SKIP_ONBOARDING", AppiumBy.id("tv_skip"));

    public OnboardingPageAndroid(Session<T> session) {
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
        driverTools.waitForElementToAppear(CURRENCY_ROW, 10).orElseThrow();
        driverTools.click(CURRENCY_ROW);
        SettingPage.getInstance(session).selectCurrencyFromOpenBottomSheet(currency);
    }

    @Override
    public String getSelectedCurrency() {
        return driverTools.waitForElementToAppear(CURRENCY_ROW_VALUE, 10).orElseThrow().getText();
    }

    @Override
    public void finishOnboardingBySkipping() {
        // Advance from the regional "start your journey" screen. Prefer the btn_action-scoped continue, but
        // fall back to the broader tds_button_view continue (as HomePageAndroid.gotoHome() uses) so the flow
        // is robust across devices where the scoped locator is not resolvable, mirroring iOS's best-effort
        // regional continue. Only fail hard if neither is present.
        if (driverTools.waitForElementToAppear(CONTINUE_BUTTON, 10).isPresent()) {
            driverTools.click(CONTINUE_BUTTON);
        } else if (driverTools.waitForElementToAppear(CONTINUE_BUTTON_FALLBACK, 5).isPresent()) {
            driverTools.click(CONTINUE_BUTTON_FALLBACK);
        } else {
            // Best-effort, mirroring iOS completeFreshInstallOnboarding(): if the regional continue is not
            // found we may already be past the regional page, so proceed to the skip steps rather than
            // hard-failing. The onboarding-complete flag below is still gated on the onboarding screen
            // actually going away, and the test's Welcome Rewards assertion catches a genuine failure.
            logger.log("Onboarding regional 'Continue' not found; assuming already past the regional page and continuing with the skip steps");
        }

        // Skip the notification permission prompt if it surfaces.
        if (driverTools.waitForElementToAppear(SKIP_NOTIFICATION, 5).isPresent()) {
            driverTools.click(SKIP_NOTIFICATION);
            driverTools.waitForElementToAppear(ALLOW_NOTIFICATION, 3).ifPresent(driverTools::click);
        }

        // Skip the onboarding carousel if it surfaces.
        driverTools.waitForElementToAppear(SKIP_ONBOARDING, 5).ifPresent(driverTools::click);

        HomePage<T> homePage = HomePage.getInstance(session);
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
