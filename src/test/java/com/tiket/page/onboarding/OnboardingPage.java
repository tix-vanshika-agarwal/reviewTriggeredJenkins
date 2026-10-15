package com.tiket.page.onboarding;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

/**
 * First-run onboarding screen ("Let's start your journey!") that lets the user pick the preferred
 * language, currency and country before entering the app. Only shown on a fresh install.
 */
public abstract class OnboardingPage<T extends WebDriver> extends BasePage<T> {

    public OnboardingPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> OnboardingPage<T> getInstance(Session<T> session) {
        return switch (Configs.PLATFORM) {
            case ANDROID -> new OnboardingPageAndroid<>(session);
            case IOS -> new OnboardingPageIOS<>(session);
            default -> throw new NotImplementedException("Not implemented");
        };
    }

    /** @return true when the onboarding language/currency screen is on screen. */
    public abstract boolean isOnboardingPageDisplayed();

    /**
     * @param seconds max time to wait for the onboarding screen to appear.
     * @return true when the onboarding language/currency screen is on screen within the given timeout.
     * Use a short timeout for cheap probes (e.g. the pooled-session fresh-install gate) so an
     * already-onboarded driver does not block for the full default wait.
     */
    public abstract boolean isOnboardingPageDisplayed(int seconds);

    /** Opens the currency selector on the onboarding screen and selects the given currency (e.g. IDR). */
    public abstract void changeCurrency(String currency);

    /** @return the currency currently selected on the onboarding screen (e.g. SGD, IDR). */
    public abstract String getSelectedCurrency();

    /** Finishes onboarding by confirming and skipping the remaining onboarding/notification steps. */
    public abstract void finishOnboardingBySkipping();
}
