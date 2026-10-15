package com.tiket.web.page.platform.eiffel.membership;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.member.MainProfileData;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;

import javax.security.auth.login.FailedLoginException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class AccountPageWeb<T extends WebDriver> extends WebPage<T> {

    public AccountPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> AccountPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
        case DWEB -> {
            return new AccountPageDWeb<>(session);
        }
        case AMWEB, IMWEB, ANDROID, IOS -> {
            return new AccountPageMWeb<>(session);
        }
        default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    protected static final Locator ACCOUNT_SALUTATION = new Locator("ACCOUNT_SALUTATION", new By.ByXPath("(//input[@name='accountSalutationName'])[1]"));
    protected static final Locator ACCOUNT_FIRST_NAME = new Locator("ACCOUNT_FIRST_NAME", new By.ByXPath("//input[@id='account-first-name']"));
    protected static final Locator ACCOUNT_LAST_NAME = new Locator("ACCOUNT_LAST_NAME", new By.ByXPath("//input[@id='account-last-name']"));
    /**
     * Copy that identifies the mononym option, in both UI languages. The option is matched on its
     * wording rather than on a CSS-module class, because those class names carry a build hash that
     * changes on every frontend release.
     */
    private static final String NO_LAST_NAME_TEXT =
            "contains(normalize-space(.),'No last name/surname')"
                    + " or contains(normalize-space(.),'nama keluarga/nama belakang')";
    /** The clickable "No last name/surname" control on the profile form. */
    protected static final Locator NO_LAST_NAME_OPTION = new Locator("NO_LAST_NAME_OPTION",
            new By.ByXPath("//label[" + NO_LAST_NAME_TEXT + "]"));
    /** The checkbox behind that control, which is what carries the checked state. */
    protected static final Locator NO_LAST_NAME_CHECKBOX = new Locator("NO_LAST_NAME_CHECKBOX",
            new By.ByXPath("//label[" + NO_LAST_NAME_TEXT + "]//input[@type='checkbox']"));
    /** The nationality entry for a given country, matched on the country name the list renders. */
    private static final String COUNTRY_LIST_ITEM_XPATH = "//div[contains(@class,'CountryListSelection_list_item')]";
    /**
     * The call to action the account page renders instead of the member menu when the session is
     * not logged in. Matched on its copy in both UI languages.
     */
    private static final Locator GUEST_LOGIN_CTA = new Locator("GUEST_LOGIN_CTA", new By.ByXPath(
            "//*[normalize-space(text())='Log in or register' or normalize-space(text())='Log in atau daftar']"));
    /**
     * The header avatar a logged-in member gets. The class is the site-wide icon class rather than
     * an avatar-specific one, so its presence is treated as supporting evidence only - see
     * {@link #isGuestSession()} for why the call to action outranks it.
     */
    private static final Locator MEMBER_PROFILE_ICON = new Locator("MEMBER_PROFILE_ICON",
            new By.ByXPath("//div[contains(@class,'blink_icon')]"));
    /** How long the member view gets to resolve before the page is called a guest session. */
    private static final int MEMBER_VIEW_TIMEOUT = 10;
    /** How many times the account page is re-opened, re-logging in, before giving up. */
    private static final int MEMBER_PAGE_ATTEMPTS = 3;
    /** How long a React re-render gets to apply the mononym option's effect on Last Name. */
    private static final int FIELD_STATE_TIMEOUT = 5;
    protected static final Locator DATE = new Locator("DATE", new By.ByXPath("//input[@id='date-of-birth']"));
    protected static final Locator SAVE_DATE = new Locator("SAVE_DATE", new By.ByXPath("//footer[contains(@class,'Date')]//button"));
    /** The mandatory Sex field; it is read-only and opens a picker of radio options when clicked. */
    protected static final Locator ACCOUNT_SEX = new Locator("ACCOUNT_SEX", new By.ByXPath("//input[@id='account-gender']"));
    /**
     * The picker's option list. It stays mounted while the modal or bottom sheet plays its closing
     * animation, during which the overlay still covers the form, so its removal is what frees the form.
     */
    private static final Locator SEX_PICKER_LIST = new Locator("SEX_PICKER_LIST",
            new By.ByXPath("//div[contains(@class,'styles_gender_list__')]"));
    /**
     * The picker's Save control. dWeb renders the picker as a modal with a primary button, mWeb as a
     * bottom sheet with a header text button, so both are matched inside the section holding the list.
     */
    private static final Locator SEX_PICKER_SAVE = new Locator("SEX_PICKER_SAVE", new By.ByXPath(
            "//section[.//div[contains(@class,'styles_gender_list__')]]"
                    + "//button[contains(@class,'Button_variant_primary') or contains(@class,'BottomSheet_text_button')]"));
    /** Sex code that matches {@link #ACCOUNT_SALUTATION}, the first salutation option (Mr.). */
    protected static final String SEX_MATCHING_DEFAULT_SALUTATION = "M";
    protected static final Locator ACCOUNT_NATIONALITY_CODE = new Locator("ACCOUNT_NATIONALITY_CODE", new By.ByXPath("//input[@id='account-nationality-code']"));
    protected static final Locator SELECT_NATIONALITY = new Locator("SELECT_NATIONALITY", new By.ByXPath("//*[contains(@class,'CountryListSelection_list_item')]"));
    protected static final Locator NIN_CODE = new Locator("NIN_CODE", new By.ByXPath("//input[@id='identity-number']"));
    protected static final Locator ACCOUNT_EMAIL = new Locator("ACCOUNT_EMAIL", new By.ByXPath("//input[@id='account-email']"));
    protected static final Locator ACCOUNT_PHONE = new Locator("ACCOUNT_PHONE", new By.ByXPath("//input[@id='account-phone']"));
    protected static final Locator SAVE_PASSENGER = new Locator("SAVE_PASSENGER", new By.ByXPath("//div[contains(@class,'styles_save_passenger')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator DELETE_PASSENGER = new Locator("DELETE_PASSENGER", new By.ByXPath("//button[contains(@class,'Button_variant_danger')]"));
    private static final Locator YES_DELETE_PASSENGER = new Locator("YES_DELETE_PASSENGER", new By.ByXPath("//button[contains(@class,'Button_variant_primary') and contains(.,',')]"));
    protected static final Locator ERROR_MESSAGE = new Locator("ERROR_MESSAGE", new By.ByXPath("(//div[contains(@class,'styles_state_danger')])[1]"));
    protected static final Locator EMERGENCY_CONTACT = new Locator("EMERGENCY_CONTACT", By.id("emergency-contact-name"));
    protected static final Locator EMERGENCY_NUMBER = new Locator("EMERGENCY_NUMBER", By.id("emergency-country-phone-number"));
    protected static final Locator PASSENGER_INFO = new Locator("PASSENGER_INFO", new By.ByXPath("(//span[contains(@class, 'Text_size_b1') and contains(@class, 'Text_weight_bold')])[1]"));
    private static final Locator ACCOUNT_PAGE_NAVBAR_MWEB = new Locator("ACCOUNT_PAGE_NAVBAR_MWEB", new By.ByXPath("//a[@data-testid='nav-item' and contains(@href,'myaccount')]"));
    private static final Locator LAYOUT_MY_ACCOUNT_ELEMENT = new Locator("LAYOUT_MY_ACCOUNT_ELEMENT", new By.ByXPath("//div[contains(@class,'Layout_my_account')]"));
    /** Stories article path that the passport-renewal CTA must open. */
    public static final String PASSPORT_RENEWAL_ARTICLE_PATH = "cara-membuat-paspor-dan-visa-luar-negeri";
    private static final String PASSENGER_LIST_PATH = "/myaccount/passengers";
    private static final String PASSENGER_DETAIL_PATH = "/myaccount/passengers/detail";
    private static final String AUTH_COOKIE_DOMAIN = ".tiket.com";
    /** Cookies that carry the logged-in tiket session on a {@code .tiket.com} document. */
    private static final List<String> AUTH_COOKIE_NAMES = List.of(
            "session_access_token", "session_refresh_token", "_auto_session_email");
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final Locator PASSENGER_LIST_BANNER = new Locator("PASSENGER_LIST_BANNER",
            new By.ByXPath("//div[contains(@class,'styles_info_banner__')]"));
    private static final Locator PASSPORT_RENEWAL_BANNER = new Locator("PASSPORT_RENEWAL_BANNER",
            new By.ByXPath("//div[contains(@class,'styles_info_banner__') and contains(@class,'styles_info_warning_banner')]"));
    private static final Locator PASSPORT_RENEWAL_BANNER_TEXT = new Locator("PASSPORT_RENEWAL_BANNER_TEXT",
            new By.ByXPath("//div[contains(@class,'styles_info_warning_banner')]//*[contains(@class,'styles_info_banner_text')]"));
    private static final Locator PASSPORT_RENEWAL_WARNING_ICON = new Locator("PASSPORT_RENEWAL_WARNING_ICON",
            new By.ByXPath("//div[contains(@class,'styles_info_warning_banner')]//*[contains(@class,'styles_info_banner_icon')]"));
    private static final Locator PASSPORT_RENEWAL_SEE_HOW_LINK = new Locator("PASSPORT_RENEWAL_SEE_HOW_LINK",
            new By.ByXPath("//div[contains(@class,'styles_info_warning_banner')]//*[contains(@class,'styles_info_banner_cta')]"));
    private static final Locator ADD_PASSENGER_FALLBACK = new Locator("ADD_PASSENGER_FALLBACK",
            new By.ByXPath("//span[contains(@class,'Header_profile_add')]"));
    private static final Locator PASSPORT_DETAIL_CARD = new Locator("PASSPORT_DETAIL_CARD",
            By.id("passport-number"));
    private static final Locator PASSPORT_DETAIL_TITLE = new Locator("PASSPORT_DETAIL_TITLE",
            new By.ByXPath("//div[@id='passport-number']//*[contains(@class,'styles_box_title')]"));
    private static final Locator PASSPORT_EXPIRY_WARNING = new Locator("PASSPORT_EXPIRY_WARNING",
            new By.ByXPath("//div[@id='passport-number']//*[contains(@class,'Label_color_yellow')]"));
    private static final Locator PASSPORT_DETAIL_BANNER = new Locator("PASSPORT_DETAIL_BANNER",
            new By.ByXPath("//div[@id='passport-number']//*[contains(@class,'styles_box_info__')]"));
    private static final Locator PASSPORT_DETAIL_SEE_HOW = new Locator("PASSPORT_DETAIL_SEE_HOW",
            new By.ByXPath("//div[@id='passport-number']//*[contains(@class,'styles_box_info_cta')]"));

    /**
     * The passport-renewal copy the passenger list and the profile detail render for a UI
     * language. Each value is the fragment the assertion matches on, not the full sentence,
     * so wording changes around it do not break the test.
     */
    public record PassportRenewalCopy(String bannerCopy, String seeHowLabel, String passportSectionTitle,
                                      String expirySoonWarning) {

        private static final PassportRenewalCopy ENGLISH = new PassportRenewalCopy(
                "renew your passport", "See how", "Passport", "expir");
        private static final PassportRenewalCopy BAHASA = new PassportRenewalCopy(
                "perpanjang paspor", "Lihat caranya", "Paspor", "Masa berlaku segera habis");

        public static PassportRenewalCopy forLanguage(String language) {
            boolean bahasa = "id".equalsIgnoreCase(language) || "bahasa".equalsIgnoreCase(language);
            return bahasa ? BAHASA : ENGLISH;
        }
    }

    /**
     * What a caller needs to get back to the passenger list once the renewal article has taken
     * over the tab: the URL the CTA landed on, the list URL it came from, and the auth cookies
     * that were live at the moment of the click.
     */
    public record ArticleVisit(String url, String passengerListUrl, Set<Cookie> authCookies) {
    }

    public abstract String getRandomCurrency(String currentCurrency);

    public abstract String getCurrentCurrency();

    public abstract void changeCurrency(String currency);

    public abstract void goToSavePassengerData();

    public abstract void clickAddPassengerData();

    public abstract String getCurrentLanguage();

    public abstract void changeLanguage(String language);

    public abstract void addSmartProfile(String firstName, String lastName, String ninCode, String email, String pn) throws InterruptedException;

    public abstract void openAccountOwnerProfile();

    public abstract void editMainProfileSection(MainProfileData data);

    public abstract void editPassportSection(MainProfileData data);

    public abstract void editDriverLicenseSection(MainProfileData data);

    public abstract void clearFrequentFlyerSection();

    public abstract void addFrequentFlyerSection(MainProfileData data);

    public abstract void saveAccountOwnerProfile();

    public abstract String getSuccessSnackbarText();

    public abstract MainProfileData getAccountOwnerProfileData();

    protected Locator getCurrencyLocator(String currency) {
        return new Locator("CURRENCY", new By.ByXPath("//span[contains(text(),'" + currency + "')]/ancestor::label[contains(@class,'Radio_radio')]"));
    }

    private Locator smartProfileLocator(String name) {
        return new Locator("SMART_PROFILE", new By.ByXPath("//div[contains(text(),'" + name + "')]"));
    }

    public boolean verifySmartProfileByName(String firstName, String lastName) {
        driverTools.waitForElementToAppear(smartProfileLocator(firstName + " " + lastName));
        return driverTools.isElementDisplayed(smartProfileLocator(firstName + " " + lastName));
    }

    public void editSmartProfile(String firstName, String lastName, String newName) throws InterruptedException {
        driverTools.clickWithJavascriptExecutor(smartProfileLocator(firstName + " " + lastName));
        WebElement element = driverTools.waitForElementToAppear(ACCOUNT_FIRST_NAME, 10).orElseThrow();
        driverTools.clear(element);
        driverTools.sendKeys(element, newName);
        driverTools.scrollIntoWebView(SAVE_PASSENGER);
        driverTools.clickWithJavascriptExecutor(SAVE_PASSENGER);
    }

    public void deleteSmartProfile(String firstName, String lastName) {
        driverTools.clickWithJavascriptExecutor(smartProfileLocator(firstName + " " + lastName));
        driverTools.waitForElementToPresence(DELETE_PASSENGER);
        driverTools.clickWithJavascriptExecutor(DELETE_PASSENGER);
        driverTools.clickWithJavascriptExecutor(YES_DELETE_PASSENGER);
    }

    public void goToAccountPage() {
        driverTools.waitForElementToPresence(ACCOUNT_PAGE_NAVBAR_MWEB, 5);
        driverTools.click(ACCOUNT_PAGE_NAVBAR_MWEB);
    }

    public boolean isUserInMyAccount() {
        return driverTools.waitForElementToAppear(LAYOUT_MY_ACCOUNT_ELEMENT, 5).isPresent();
    }

    public void waitForPassengerList() {
        driverTools.waitForWebToLoad();
        driverTools.waitUntil(d -> isPassengerListUrl(driverTools.getCurrentURL()) ? true : null, 15)
                .orElseThrow(() -> new AssertionError(
                        "Saved-passenger list never opened, current URL: " + driverTools.getCurrentURL()));
        driverTools.waitForElementToAppear(PASSENGER_LIST_BANNER, 20)
                .or(() -> driverTools.waitForElementToAppear(ADD_PASSENGER_FALLBACK, 5))
                .orElseThrow(() -> new AssertionError(
                        "Saved-passenger list rendered neither a banner nor the add-passenger header"));
    }

    public boolean isPassportRenewalBannerDisplayed() {
        return driverTools.waitForElementToAppear(PASSPORT_RENEWAL_BANNER, 15).isPresent();
    }

    /**
     * Banners rendered in the list's banner slot, of any variant. The renewal banner replaces the
     * default one rather than joining it, so the count is what proves the takeover: asking whether
     * the default banner is absent would pass just as well once its class is renamed.
     */
    public int passengerListBannerCount() {
        return driverTools.elements(PASSENGER_LIST_BANNER).map(List::size).orElse(0);
    }

    public String getPassportRenewalBannerText() {
        return waitForNonBlankText(PASSPORT_RENEWAL_BANNER_TEXT, 15);
    }

    public boolean isPassportRenewalWarningIconDisplayed() {
        return driverTools.waitForElementToAppear(PASSPORT_RENEWAL_WARNING_ICON, 10).isPresent();
    }

    public String getSeeHowLinkText() {
        return waitForNonBlankText(PASSPORT_RENEWAL_SEE_HOW_LINK, 10);
    }

    public ArticleVisit openSeeHowArticle() {
        String passengerListUrl = passengerListUrlFromCurrentPage();
        Set<Cookie> authCookies = currentAuthCookies();
        Set<String> windowsBeforeClick = new HashSet<>(driverTools.getWindowHandles());

        Locator seeHow = driverTools.waitForAnyLocator(10, PASSPORT_DETAIL_SEE_HOW, PASSPORT_RENEWAL_SEE_HOW_LINK)
                .orElseThrow(() -> new AssertionError("No See how CTA on " + driverTools.getCurrentURL()));
        driverTools.clickWithJavascriptExecutor(seeHow);
        if (articleOpenedInNewTab(windowsBeforeClick)) {
            driverTools.switchToNewTab();
        }
        return new ArticleVisit(waitForArticleUrl(), passengerListUrl, authCookies);
    }

    public void returnToPassengerList(ArticleVisit visit) {
        if (driverTools.getWindowHandles().size() > 1) {
            driverTools.closeCurrentTab();
            driverTools.getWindowHandles().stream().findFirst().ifPresent(driverTools::switchToWindow);
            waitForPassengerList();
            return;
        }
        driverTools.get(visit.passengerListUrl());
        if (restoreMissingAuthCookies(visit.authCookies())) {
            driverTools.get(visit.passengerListUrl());
        }
        waitForPassengerList();
    }

    /**
     * Waits out the CTA, which either spawns a tab or navigates the current one, and reports
     * which of the two happened. The tab only becomes visible to the driver once the browser has
     * created it, so both outcomes have to be polled together.
     */
    private boolean articleOpenedInNewTab(Set<String> windowsBeforeClick) {
        driverTools.waitUntil(d -> {
            boolean tabOpened = driverTools.getWindowHandles().size() > windowsBeforeClick.size();
            return tabOpened || hasLeftPassengerPages(driverTools.getCurrentURL()) ? true : null;
        }, 15).orElseThrow(() -> new AssertionError(
                "See how neither opened a tab nor navigated, still on " + driverTools.getCurrentURL()));
        return driverTools.getWindowHandles().size() > windowsBeforeClick.size();
    }

    /**
     * The URL the CTA landed on. Deliberately waits for the navigation to settle rather than for
     * the renewal article specifically, so that asserting which article opened is left to the test.
     */
    private String waitForArticleUrl() {
        return driverTools.waitUntil(d -> {
            String url = driverTools.getCurrentURL();
            return hasLeftPassengerPages(url) ? url : null;
        }, 15).orElseThrow(() -> new AssertionError(
                "See how did not navigate anywhere, still on " + driverTools.getCurrentURL()));
    }

    private static boolean hasLeftPassengerPages(String url) {
        return url != null && !url.isBlank() && !url.startsWith("about:") && !url.contains(PASSENGER_LIST_PATH);
    }

    private static boolean isPassengerListUrl(String url) {
        return url != null && url.contains(PASSENGER_LIST_PATH) && !url.contains(PASSENGER_DETAIL_PATH);
    }

    /**
     * The saved-passenger list URL for whichever page the browser is on now, so that coming back
     * from the article keeps the host and locale the run started with instead of a hardcoded one.
     */
    private String passengerListUrlFromCurrentPage() {
        String url = driverTools.getCurrentURL();
        int pathIndex = url == null ? -1 : url.indexOf(PASSENGER_LIST_PATH);
        if (pathIndex < 0) {
            throw new AssertionError("Not on a saved-passenger page, current URL: " + url);
        }
        return url.substring(0, pathIndex + PASSENGER_LIST_PATH.length());
    }

    private Set<Cookie> currentAuthCookies() {
        Set<Cookie> cookies = new HashSet<>();
        for (String name : AUTH_COOKIE_NAMES) {
            Cookie cookie = driverTools.getCookie(name);
            if (cookie != null) {
                cookies.add(cookie);
            }
        }
        return cookies;
    }

    /**
     * Puts back any auth cookie the article navigation dropped and reports whether it had to, so
     * the caller can reload on the restored session. Runs only once the browser is back on a
     * tiket document, since a cookie can only be written for the origin that is loaded.
     */
    private boolean restoreMissingAuthCookies(Set<Cookie> authCookies) {
        boolean restored = false;
        for (Cookie cookie : authCookies) {
            if (driverTools.getCookie(cookie.getName()) == null) {
                logger.log("Restoring auth cookie dropped by the article navigation: " + cookie.getName());
                driverTools.addCookie(cookie.getName(), cookie.getValue(), AUTH_COOKIE_DOMAIN, "/");
                restored = true;
            }
        }
        return restored;
    }

    public boolean hasExpiringPassportIndicator(String firstName, String lastName) {
        Locator indicator = expiringPassportIndicatorLocator(firstName, lastName);
        return driverTools.waitForElementToAppear(indicator, 10).isPresent();
    }

    public void openSavedPassenger(String firstName, String lastName) {
        String passengerName = firstName + " " + lastName;
        Locator row = passengerRowLocator(firstName, lastName);
        driverTools.waitForElementToAppear(row, 10)
                .orElseThrow(() -> new AssertionError("No saved-passenger row for " + passengerName));
        driverTools.clickWithJavascriptExecutor(row);
        driverTools.waitUntil(d -> {
            String url = driverTools.getCurrentURL();
            return url != null && url.contains(PASSENGER_DETAIL_PATH) ? true : null;
        }, 15).orElseThrow(() -> new AssertionError("Profile detail never opened for " + passengerName
                + ", current URL: " + driverTools.getCurrentURL()));
        driverTools.waitForElementToAppear(PASSPORT_DETAIL_CARD, 15)
                .orElseThrow(() -> new AssertionError("Profile detail for " + passengerName
                        + " has no passport card"));
        driverTools.scrollIntoWebView(PASSPORT_DETAIL_CARD);
    }

    public String getPassportDetailTitle() {
        driverTools.scrollIntoWebView(PASSPORT_DETAIL_CARD);
        return waitForNonBlankText(PASSPORT_DETAIL_TITLE, 10);
    }

    public String getPassportExpiryWarningText() {
        return waitForNonBlankText(PASSPORT_EXPIRY_WARNING, 10);
    }

    public String getPassportDetailBannerText() {
        return waitForNonBlankText(PASSPORT_DETAIL_BANNER, 10);
    }

    public String getPassportDetailSeeHowText() {
        return waitForNonBlankText(PASSPORT_DETAIL_SEE_HOW, 10);
    }

    /**
     * Whether the account page came back as a guest. Cookie-injection login fails intermittently
     * and it fails silently: nothing throws, but the page renders the "Hi, t-mate" greeting and a
     * log-in call to action where the member's menu belongs. Anything that follows then breaks
     * with a misleading element-not-found rather than a login error.
     * <p>The log-in call to action decides the answer and the avatar only confirms it, because the
     * two mistakes do not cost the same: a guest read as a member defeats the retry this check
     * exists for, while a member read as a guest costs one extra page load.
     */
    public boolean isGuestSession() {
        boolean memberIconPresent = driverTools
                .waitForElementToAppear(MEMBER_PROFILE_ICON, MEMBER_VIEW_TIMEOUT).isPresent();
        return driverTools.isElementDisplayed(GUEST_LOGIN_CTA) || !memberIconPresent;
    }

    /**
     * Opens the account page and makes sure it really is the member's, logging in again whenever
     * it comes back as a guest. A single cookie injection is not reliable enough to build on -
     * see {@link #isGuestSession()}.
     */
    public void goToAccountPageAsMember(String email, String password)
            throws FailedLoginException, InterruptedException {
        LoginPageWeb<T> loginPage = LoginPageWeb.getInstance(session);
        for (int attempt = 1; attempt <= MEMBER_PAGE_ATTEMPTS; attempt++) {
            loginPage.goToAccountPage();
            if (!isGuestSession()) {
                return;
            }
            logger.log("Account page came back as a guest session on attempt " + attempt + " of "
                    + MEMBER_PAGE_ATTEMPTS + "; logging in again as " + email);
            loginPage.loginWithSpecificAccountViaCookie(email, password);
        }
        throw new AssertionError("Account page still shows a guest session after "
                + MEMBER_PAGE_ATTEMPTS + " login attempts as " + email);
    }

    /**
     * Waits for the create/edit profile form to be usable. The first-name field is the anchor
     * because it is the first control the form renders and it exists on both the add and the
     * edit route, so the same wait covers creating a profile and re-opening one.
     */
    public void waitForProfileForm() {
        driverTools.waitForElementToAppear(ACCOUNT_FIRST_NAME, 20)
                .orElseThrow(() -> new AssertionError(
                        "Profile form did not open, current URL: " + driverTools.getCurrentURL()));
    }

    /** Whether the form offers the mononym option at all, which is what gates the rest of the flow. */
    public boolean isNoLastNameOptionDisplayed() {
        return driverTools.waitForElementToAppear(NO_LAST_NAME_OPTION, 15).isPresent();
    }

    /**
     * Whether the mononym option is ticked now. The checkbox is resolved first so that an option
     * that never rendered fails as such instead of passing for an unticked one.
     */
    public boolean isNoLastNameChecked() {
        driverTools.waitForElementToPresence(NO_LAST_NAME_CHECKBOX, FIELD_STATE_TIMEOUT)
                .orElseThrow(() -> new AssertionError("The mononym option is not on the profile form"));
        return driverTools.isElementSelected(NO_LAST_NAME_CHECKBOX);
    }

    /** Whether Last Name is enabled now, failing rather than reporting false when it is absent. */
    public boolean isLastNameFieldEnabled() {
        driverTools.waitForElementToPresence(ACCOUNT_LAST_NAME, FIELD_STATE_TIMEOUT)
                .orElseThrow(() -> new AssertionError("Last Name is not on the profile form"));
        return driverTools.isElementEnabled(ACCOUNT_LAST_NAME);
    }

    /**
     * Whether Last Name went disabled within the re-render window. Ticking the mononym option and
     * the field reacting to it are two renders, so the state has to be polled rather than read once.
     */
    public boolean isLastNameFieldDisabled() {
        return driverTools.waitUntil(d -> isLastNameFieldEnabled() ? null : true, FIELD_STATE_TIMEOUT)
                .isPresent();
    }

    /** Whether Last Name emptied within the same re-render window. */
    public boolean isLastNameFieldEmpty() {
        return driverTools.waitUntil(d -> getLastNameValue().isEmpty() ? true : null, FIELD_STATE_TIMEOUT)
                .isPresent();
    }

    public String getLastNameValue() {
        return fieldValue(ACCOUNT_LAST_NAME);
    }

    public String getFirstNameValue() {
        return fieldValue(ACCOUNT_FIRST_NAME);
    }

    public void enterFirstName(String firstName) {
        clearAndTypeReactInput(ACCOUNT_FIRST_NAME, firstName);
    }

    /**
     * Ticks the mononym option and waits only for the checkbox itself to register the click.
     * What that tick is supposed to do to the Last Name field is deliberately left unasserted
     * here, so the test is the one that reports it rather than this method throwing first.
     */
    public void tickNoLastName() {
        driverTools.scrollIntoWebView(NO_LAST_NAME_OPTION);
        driverTools.clickWithJavascriptExecutor(NO_LAST_NAME_OPTION);
        driverTools.waitUntil(d -> isNoLastNameChecked() ? true : null, 10)
                .orElseThrow(() -> new AssertionError(
                        "The mononym option stayed unchecked after being clicked"));
    }

    /**
     * Fills everything the form requires besides the name: salutation, date of birth, nationality,
     * NIN, email and phone. Last Name is left alone on purpose - for a mononym it is the checkbox,
     * not a typed value, that is meant to satisfy the field.
     */
    public void fillMononymMandatoryFields(String nationality, String ninCode, String email, String phone) {
        driverTools.waitForElementToPresence(ACCOUNT_SALUTATION, 15)
                .orElseThrow(() -> new AssertionError("Salutation control never rendered"));
        driverTools.clickWithJavascriptExecutor(ACCOUNT_SALUTATION);
        driverTools.waitUntil(d -> driverTools.isElementSelected(ACCOUNT_SALUTATION) ? true : null, 5)
                .orElseThrow(() -> new AssertionError("Salutation stayed unselected after being clicked"));
        selectSex(SEX_MATCHING_DEFAULT_SALUTATION);
        selectDefaultDateOfBirth();
        selectNationality(nationality);
        clearAndTypeReactInput(NIN_CODE, ninCode);
        clearAndTypeReactInput(ACCOUNT_EMAIL, email);
        clearAndTypeReactInput(ACCOUNT_PHONE, phone);
    }

    /**
     * Saves the profile and waits for the saved-passenger list to come back. A rejected save leaves
     * the form open, so the list returning is what proves the save landed and the form's own
     * validation error is what explains it when it did not.
     */
    public void saveProfileAndWaitForList() {
        driverTools.scrollIntoWebView(SAVE_PASSENGER);
        driverTools.clickWithJavascriptExecutor(SAVE_PASSENGER);
        boolean listReturned = driverTools
                .waitUntil(d -> isPassengerListUrl(driverTools.getCurrentURL()) ? true : null, 20)
                .isPresent();
        if (!listReturned) {
            throw new AssertionError("Save did not return to the saved-passenger list. Form error: \""
                    + getFormErrorMessage() + "\", current URL: " + driverTools.getCurrentURL());
        }
        waitForPassengerList();
    }

    /** The validation error the form shows when a save is rejected, or empty when it is not showing. */
    public String getFormErrorMessage() {
        return driverTools.waitForElementToAppear(ERROR_MESSAGE, 3)
                .map(driverTools::getText)
                .orElse("")
                .trim();
    }

    /**
     * The saved-passenger name as the row renders it, with the salutation the list prefixes
     * ("Mr. Joe") stripped off. Reading the rendered text is the point of the mononym check: a
     * force-filled last name shows up here as a doubled name ("Joe Joe"), and leaving the
     * salutation in would only make the comparison depend on which title the form happened to pick.
     */
    public String getSavedPassengerDisplayName(String firstName) {
        return waitForNonBlankText(passengerNameLocator(firstName), 20)
                .trim().toLowerCase().replaceFirst("^\\S+\\.\\s*", "");
    }

    /** Opens the saved passenger whose row renders exactly this name, and waits for its form. */
    public void openSavedProfile(String displayName) {
        Locator row = passengerRowLocator(displayName);
        driverTools.waitForElementToAppear(row, 15)
                .orElseThrow(() -> new AssertionError("No saved-passenger row for " + displayName));
        driverTools.clickWithJavascriptExecutor(row);
        driverTools.waitUntil(d -> {
            String url = driverTools.getCurrentURL();
            return url != null && url.contains(PASSENGER_DETAIL_PATH) ? true : null;
        }, 15).orElseThrow(() -> new AssertionError("Profile detail never opened for " + displayName
                + ", current URL: " + driverTools.getCurrentURL()));
        waitForProfileForm();
        driverTools.waitUntil(d -> getFirstNameValue().isBlank() ? null : true, 10)
                .orElseThrow(() -> new AssertionError(
                        "Re-opened profile never loaded its saved values for " + displayName));
    }

    /**
     * Picks the Sex option by its code ("M" or "F"). The radio only stages the choice; the picker's
     * Save is what writes it to the field, so the field's own value confirms the pick landed. The
     * value lands before the picker finishes closing, so the picker's removal is awaited as well;
     * otherwise the next field is typed into while the closing overlay still covers it.
     */
    protected void selectSex(String sexCode) {
        driverTools.waitForElementToPresence(ACCOUNT_SEX, 15)
                .orElseThrow(() -> new AssertionError("Sex field never rendered"));
        driverTools.scrollIntoWebView(ACCOUNT_SEX);
        driverTools.clickWithJavascriptExecutor(ACCOUNT_SEX);
        Locator option = sexOption(sexCode);
        driverTools.waitForElementToPresence(option, 10)
                .orElseThrow(() -> new AssertionError("Sex picker has no option " + sexCode));
        driverTools.clickWithJavascriptExecutor(option);
        driverTools.waitForElementToPresence(SEX_PICKER_SAVE, 5)
                .orElseThrow(() -> new AssertionError("Sex picker has no Save button"));
        driverTools.clickWithJavascriptExecutor(SEX_PICKER_SAVE);
        driverTools.waitUntil(d -> Optional.ofNullable(driverTools.getValue(ACCOUNT_SEX)).orElse("").isBlank()
                        ? null : true, 10)
                .orElseThrow(() -> new AssertionError("Sex field stayed empty after picking " + sexCode));
        if (!driverTools.waitForElementToDisappear(SEX_PICKER_LIST, 10)) {
            throw new AssertionError("Sex picker stayed open after Save");
        }
    }

    /** The Sex picker's radio option for {@code sexCode} ("M" or "F"). */
    private static Locator sexOption(String sexCode) {
        return new Locator("SEX_OPTION_" + sexCode,
                new By.ByXPath(String.format("//input[@data-testid='gender-option-%s']", sexCode)));
    }

    /** Accepts whatever date the picker opens on; the case only needs the field to be filled. */
    private void selectDefaultDateOfBirth() {
        driverTools.clickWithJavascriptExecutor(DATE);
        driverTools.waitForElementToAppear(SAVE_DATE, 10)
                .orElseThrow(() -> new AssertionError("Date-of-birth picker did not open"));
        driverTools.clickWithJavascriptExecutor(SAVE_DATE);
        driverTools.waitForElementToDisappear(SAVE_DATE, 10);
    }

    /**
     * Picks a nationality by name. The list is long enough that the wanted entry can sit far below
     * the fold, so the entry is clicked through JavaScript and the field's own value is what
     * confirms the pick landed rather than the list closing.
     */
    private void selectNationality(String country) {
        driverTools.clickWithJavascriptExecutor(ACCOUNT_NATIONALITY_CODE);
        Locator entry = new Locator("COUNTRY_LIST_ITEM_" + country, new By.ByXPath(
                COUNTRY_LIST_ITEM_XPATH + "[starts-with(normalize-space(.),'" + country + "')]"));
        driverTools.waitForElementToAppear(entry, 15)
                .orElseThrow(() -> new AssertionError("Nationality list has no entry for " + country));
        driverTools.clickWithJavascriptExecutor(entry);
        driverTools.waitUntil(d -> country.equalsIgnoreCase(
                        Optional.ofNullable(driverTools.getValue(ACCOUNT_NATIONALITY_CODE)).orElse("").trim())
                        ? true : null, 10)
                .orElseThrow(() -> new AssertionError("Nationality never settled on " + country
                        + ", field reads: " + driverTools.getValue(ACCOUNT_NATIONALITY_CODE)));
    }

    /**
     * Types into a React-controlled input and confirms the value landed, retrying once. These forms
     * re-render on every keystroke, so a plain sendKeys can silently drop characters.
     */
    protected void clearAndTypeReactInput(Locator field, String value) {
        driverTools.waitForElementToPresence(field, 15)
                .orElseThrow(() -> new AssertionError(field + " never appeared on the profile form"));
        driverTools.scrollIntoWebView(field);
        driverTools.clickWithJavascriptExecutor(field);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(field, value);
        String actual = Optional.ofNullable(driverTools.getValue(field)).orElse("").trim();
        if (!value.equals(actual)) {
            driverTools.clearWithJsExecutor(field);
            driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(field, value);
            actual = Optional.ofNullable(driverTools.getValue(field)).orElse("").trim();
        }
        if (!value.equals(actual)) {
            throw new IllegalStateException("Failed to set " + field + ". Expected: " + value + ", actual: " + actual);
        }
    }

    /**
     * The value a form field holds, trimmed. A field that never rendered is reported by name rather
     * than reaching {@code getValue}, which throws a bare lookup failure, and is deliberately not
     * reported as an empty value: that would let an "is empty" expectation pass on a missing field.
     */
    private String fieldValue(Locator field) {
        WebElement element = driverTools.waitForElementToPresence(field, 10)
                .orElseThrow(() -> new AssertionError(field + " is not on the profile form"));
        return Optional.ofNullable(driverTools.getValue(element)).orElse("").trim();
    }

    /** The bold name span of the saved-passenger row carrying the given first name. */
    private static Locator passengerNameLocator(String firstName) {
        return new Locator("SAVED_PASSENGER_NAME", new By.ByXPath(String.format(
                "//div[contains(@class,'styles_passenger_name')]//span[contains(@class,'Text_weight_bold')]"
                        + "[contains(translate(normalize-space(.),'%s','%s'),'%s')]",
                UPPERCASE_LETTERS, LOWERCASE_LETTERS, firstName.toLowerCase())));
    }

    private static Locator passengerRowLocator(String displayName) {
        return new Locator("SAVED_PASSENGER_ROW", new By.ByXPath(passengerRowXPath(displayName)));
    }

    private static Locator passengerRowLocator(String firstName, String lastName) {
        return new Locator("SAVED_PASSENGER_ROW", new By.ByXPath(passengerRowXPath(firstName, lastName)));
    }

    private static Locator expiringPassportIndicatorLocator(String firstName, String lastName) {
        return new Locator("EXPIRING_PASSPORT_INDICATOR", new By.ByXPath(
                passengerRowXPath(firstName, lastName) + "//*[contains(@class,'styles_passenger_alert')]"));
    }

    private static String passengerRowXPath(String firstName, String lastName) {
        return passengerRowXPath(firstName + " " + lastName);
    }

    /** The saved-passenger row carrying the given name, matched without regard to case. */
    private static String passengerRowXPath(String displayName) {
        return String.format(
                "//div[contains(@class,'styles_passenger_name')][contains(translate(normalize-space(.),'%s','%s'),'%s')]",
                UPPERCASE_LETTERS, LOWERCASE_LETTERS, displayName.toLowerCase());
    }

    private String waitForNonBlankText(Locator locator, int timeoutSeconds) {
        return driverTools.waitUntil(d -> {
            String text = driverTools.getText(locator).orElse("");
            return text.isBlank() ? null : text;
        }, timeoutSeconds).orElseThrow(() -> new AssertionError(
                locator + " still had no text after " + timeoutSeconds + " seconds"));
    }
}
