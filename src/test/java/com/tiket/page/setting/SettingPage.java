package com.tiket.page.setting;

import com.tiket.model.Deeplink;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class SettingPage<T extends WebDriver> extends BasePage<T> {
    public SettingPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> SettingPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new SettingPageAndroid<>(session);}
            case IOS -> {return new SettingPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract boolean isOnSettingPage();
    public abstract boolean isOnLoginSettingPage();
    public abstract void logOut();
    public abstract void setLocationSwitch(String action);
    public abstract boolean isLocationOn();
    public abstract void openSecuritySettingPage();
    public abstract void closeSecuritySettingPage();
    public abstract boolean isSecurityPageOpen();
    public abstract void setNewPin(String pin);
    public abstract void changePin(String pin); // FIXME: Implementation pending
    public abstract void forgotPin(String pin); // FIXME: Implementation pending
    public abstract void switchDeviceTrust(boolean trustCondition);
    public abstract boolean verifyStatusDeviceTrust(boolean trustCondition);
    public abstract boolean verifyNotifDeviceTrust(boolean trustCondition);
    public abstract void setVerificationStep();
    public abstract void setTrustDevicePopupYes();
    public abstract void openNotificationSettingPage();
    public abstract void backToHome();
    public abstract void openSavePassengersData();
    public abstract boolean isNewPINSet();
    public abstract void openDeviceManagement();
    public abstract boolean verifyDeviceManagementPage();
    public abstract void userTurnOnSingleVerification();
    public abstract void userTurnOnDoubleVerification();
    public abstract boolean isDoubleVerificationON();
    public abstract boolean isSingleVerificationON();
    public abstract String checkCurrentCurrency();
    public abstract void changeCurrency(String inputCurrency);
    public abstract void changeCurrencyIfOtherCurrencyIsSelected(String currency);
    public abstract void changeCurrencyIfOtherCurrencyIsSelectedDeeplink(String currency);
    /**
     * Selects the given currency in an already-open currency bottom sheet (search + pick + dismiss).
     * The caller is responsible for opening the sheet first (via deeplink, settings, or onboarding).
     */
    public abstract void selectCurrencyFromOpenBottomSheet(String currency);
    public abstract boolean isOnNotificationMenu();
    public abstract void clickBackBtn();
    public abstract void clickBackBtnOnNotificationMenu();
    public abstract String getNotificationSettingLanguage();
    public abstract boolean isNotificationSettingNewVersion();
    public abstract boolean verifyAllToggle(String[] settings, String language);
    public abstract void clickDeleteAccount();
    public abstract void fillDeleteAccountForm();
    public abstract void clickSubmitDelete();
    public abstract boolean isSessionExpired();
    public abstract void openAccountSetting();
    public abstract boolean isChangeDetailSnackbarDisplayed(String snackbarType);
    public abstract boolean isNewEmailShowing(String emailToCheck);
    public abstract boolean isEmailVerified();
    public abstract boolean toggleNotifAll(String[] settings, boolean status, String language);
    public abstract void clickAccountCenter();
    public abstract void openChangeCurrencyOverlayUsingDeeplink();
    public abstract boolean isLoginWithCorrectCredentials(String username);
}