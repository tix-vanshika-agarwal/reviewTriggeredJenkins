package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;

public class Navbar<T extends WebDriver> extends BasePage<T> {
    public Navbar(Session<T> session) {
        super(session);
    }

    private static final Locator HOTEL_NAME_LABEL_HEADER = new Locator("HOTEL_NAME_LABEL_HEADER", new By.ByXPath("(//header//div[contains(@class,'hotel-name') or contains(@class,'label-name')]/label) | (//header//div[contains(@class,'whitespace')]/span[1])"));
    /** Welcome + hotel name text: "Welcome, [HotelName]" or "Selamat datang, [HotelName]" etc. – by class */
    private static final Locator WELCOME_AND_HOTEL_NAME_SPAN = new Locator("WELCOME_AND_HOTEL_NAME_SPAN", new By.ByXPath("//header//span[contains(@class,'Text_text') and contains(@class,'Text_size_b2') and contains(@class,'Text_weight_bold')]"));
    private static final Locator HOTEL_ID_HEADER = new Locator("HOTEL_ID_HEADER", new By.ByXPath("//header//div[contains(@class,'whitespace-nowrap') and contains(@class,'flex-col')]//span[2]"));
    private static final Locator PROFILE_DROPDOWN_BUTTON = new Locator("DROPDOWN_BUTTON", new By.ByXPath("//*[contains(@class,'head-profile') or contains(@class,'rounded-full cursor-pointer') or contains(@class,'tixicon tixicon-arrow-bottom icon')]"));
    // Dropdown menu ul can use 'visible' or 'absolute' depending on app version
    private static final Locator PROFILE_DROPDOWN_MENU_LIST = new Locator("PROFILE_DROPDOWN_MENU_LIST", new By.ByXPath("//div[contains(@class, 'popoverlist-menu') and not(contains(@class, 'exit-active')) and .//span[contains(@class, 'tixicon-logout')]] | //ul[contains(@class, 'absolute') and contains(@class, 'scale-y-100') and .//img[contains(@src, 'logout-icon')]]"));
    private static final Locator LOGOUT_BUTTON = new Locator("LOGOUT_BUTTON", new By.ByXPath("//button[.//img[@alt='logout icon'] and .//span[text()='Log out' or text()='Keluar']] | //span[contains(@class,'popoverlist-list')][.//span[contains(@class,'tixicon-logout')]]"));
    private static final Locator CONFIRM_LOGOUT_BUTTON = new Locator("CONFIRM_LOGOUT_BUTTON", new By.ByXPath("(//div[contains(@class,'Dialog_pop_up')]//button[normalize-space(.)='Log out']) | (//div[@class='the-modal-content confirm-modal']//button[normalize-space(.)='Log Out'])"));
    private static final Locator CHANGE_HOTEL_BUTTON = new Locator("CHANGE_HOTEL_BUTTON", new By.ByXPath("//header//ul[contains(@class,'absolute') and contains(@class,'rounded-md')]/li/button[.//img[contains(@src,'property-icon')]] | //span[contains(@class,'popoverlist-list')][.//span[contains(@class,'tixicon-ic_property')]]"));
    private static final Locator CHAT_COACHMARK = new Locator("CHAT_COACHMARK", new By.ByXPath("//div[@class=\"coachmark-content\"]"));
    private static final Locator SKIP_COACHMARK_BUTTON = new Locator("SKIP_COACHMARK_BUTTON", new By.ByXPath("(//div[@class=\"coachmark-button\"]/button)[1]"));
    private static final Locator MISSING_INFORMATION_MODAL = new Locator("MISSING_INFORMATION_MODAL", new By.ById("content-score-reminder"));
    private static final Locator NOT_NOW_MISSING_INFORMATION_MODAL_BUTTON = new Locator("NOT_NOW_MISSING_INFORMATION_MODAL_BUTTON", new By.ByXPath("(//div[@id=\"content-score-reminder\"]//button)[1]"));
    private static final Locator ALLOW_NOTIFICATION_MODAL_BUTTON = new Locator("ALLOW_NOTIFICATION_MODAL_BUTTON", new By.ByXPath("(//div[@id=\"allow-notif-modal-root\"]//button)[1]"));
    private static final Locator NOT_NOW_JOIN_PP_MODAL_BUTTON = new Locator("NOT_NOW_JOIN_PP_MODAL_BUTTON", new By.ByXPath("//div[@class=\"join-preferred-partner-info\"]//button[contains(@class,'secondary')]"));
    private static final Locator WELCOME_MODAL = new Locator("WELCOME_MODAL", new By.ByXPath("//div[contains(@class,'welcome-modal')]"));
    private static final Locator CLOSE_WELCOME_MODAL_ICON = new Locator("CLOSE_MODAL_ICON", new By.ByXPath("//div[contains(@class,'welcome-modal')]//span[contains(@class,'close')]"));
    private static final Locator ADD_NEW_HOTEL_MODAL_BUTTON = new Locator("ADD_NEW_HOTEL_MODAL_BUTTON", new By.ByXPath("(//*[contains(@class, 'change-hotel-button-wrap')]/button[1]) | ((//section[contains(@class,'Modal')]//button)[last()])"));
    private static final Locator ADD_NEW_HOTEL_CONFIRMATION_MODAL_BUTTON = new Locator("ADD_NEW_HOTEL_CONFIRMATION_MODAL_BUTTON", new By.ByXPath("//*[contains(@class, 'change-hotel-button-wrap')]/button[2]"));
    private static final Locator ADD_NEW_HOTEL_CONFIRM_YES_BUTTON = new Locator("ADD_NEW_HOTEL_CONFIRM_YES_BUTTON", new By.ByXPath("(//button[@class='the-button primary change-hotel-button-wrap-yes']) | ((//div[contains(@class,'Dialog_body')]//button)[last()])"));
    private static final Locator ROUNDED_PROFILE_APP = new Locator("ROUNDED_PROFILE_APP", new By.ByXPath("(//span[contains(@class, 'bg-zinc-200/95')])[1]"));
    private static final Locator LOGOUT_BUTTON_APP = new Locator("LOGOUT_BUTTON_APP", new By.ByXPath("//span[text()='Log out']"));
    private static final Locator INCOMPLETE_CONTENT_MODAL_SKIP_BUTTON = new Locator("INCOMPLETE_CONTENT_MODAL_SKIP_BUTTON", new By.ByXPath("//div[contains(@class,'Dialog_pop_up')]//button[1]"));
    private static final Locator LATER_INCOMPLETE_INFORMATION_MODAL_BUTTON = new Locator("NOT_NOW_MISSING_INFORMATION_MODAL_BUTTON", new By.ByXPath("//button[contains(@class, 'Figure_secondary__vJpMm') and contains(@class, 'Button_variant_secondary__todT5')]"));
    private static final Locator PROFILE_EMAIL_IN_DROPDOWN = new Locator("PROFILE_EMAIL_IN_DROPDOWN", new By.ByXPath("//ul[contains(@class,'absolute')]//span[contains(@class,'text-n-600') and contains(.,'@')]"));
    private static final Locator CLOSE_POP_UP_ON_EXTRANET_HOMEPAGE = new Locator("CLOSE_POP_UP_ON_EXTRANET_HOMEPAGE", new By.ByXPath("(//div[contains(@class,'Figure_footer')]//button[contains(@class,'Button_variant_primary')]) | (//div[contains(@class,'coachmark-button')]//button[contains(@class,'coachmark-button-primary')])"));

    public Optional<String> getDisplayedUserEmail() {
        clickProfile();
        return driverTools.waitForElementToAppear(PROFILE_EMAIL_IN_DROPDOWN, 5)
                .flatMap(el -> Optional.ofNullable(driverTools.getText(el)).map(String::trim));
    }

    private static Locator getHotelNameOption(String text) {
        return new Locator("HOTEL_NAME_OPTION", new By.ByXPath(String.format("(//div[@class=\"hotel-name\"] | //div[@class=\"properti-items \"]//div[@class=\"detail\"]/div[contains(@class,'Text')][1])[text()='%s']", text)));
    }

    private static Locator getHotelNameLabel(String value) {
        return new Locator("HOTEL_NAME_LABEL_DROPDOWN", new By.ByXPath(String.format("//div[contains(normalize-space(), '%s')]/ancestor::div[contains(@class,'item flex')]", value)));
    }

    public Optional<String> getCurrentHotelIdFromHeader() {
        return driverTools.waitForElementToAppear(HOTEL_ID_HEADER)
                .flatMap(el -> Optional.ofNullable(driverTools.getText(el)).map(String::trim));
    }

    public boolean isCurrentHotelId(String hotelId) {
        return getCurrentHotelIdFromHeader()
                .filter(id -> !id.isEmpty())
                .map(id -> id.equals(hotelId))
                .orElse(false);
    }

    public boolean verifyHotelIdOnNavbar(String hotelId) {
        return isCurrentHotelId(hotelId);
    }

    public boolean isUserEmailPresented(String email) {
        return getDisplayedUserEmail().filter(disp -> disp.equals(email)).isPresent();
    }

    /** Returns only the hotel name (no "Welcome," / "Selamat datang," prefix). */
    public Optional<String> getDisplayedHotelName() {
        return driverTools.waitForElementToAppear(HOTEL_NAME_LABEL_HEADER, 5)
                .flatMap(el -> Optional.ofNullable(driverTools.getText(el)).map(String::trim))
                .map(text -> text.contains(", ") ? text.substring(text.indexOf(", ") + 2).trim() : text);
    }

    /** Full welcome line: "Welcome, [HotelName]" or "Selamat datang, [HotelName]" etc. */
    public Optional<String> getWelcomeAndHotelNameText() {
        return driverTools.waitForElementToAppear(WELCOME_AND_HOTEL_NAME_SPAN, 5)
                .flatMap(el -> Optional.ofNullable(driverTools.getText(el)).map(String::trim));
    }

    public boolean isHotelNamePresented(String text) {
        WebElement label = driverTools.waitForElementToAppear(HOTEL_NAME_LABEL_HEADER).orElseThrow();
        return driverTools.getText(label).contains(text);
    }

    private boolean isProfileDropdownPanelOpen() {
        return driverTools.isElementDisplayed(PROFILE_DROPDOWN_MENU_LIST);
    }

    /** If the panel is already open, wait for a menu action instead of clicking profile again (second click toggles closed). */
    private void openProfileDropdownIfClosed() {
        if (isProfileDropdownPanelOpen()) {
            if (!driverTools.waitForElementToBeClickable(CHANGE_HOTEL_BUTTON).isPresent()) {
                driverTools.waitForElementToBeClickable(LOGOUT_BUTTON).orElseThrow();
            }
            return;
        }
        WebElement dropdown = driverTools.waitForElementToAppear(PROFILE_DROPDOWN_BUTTON).orElseThrow();
        driverTools.click(dropdown);
        Assertion.assertThat("Profile dropdown menu is appeared", driverTools.waitForElementToAppear(PROFILE_DROPDOWN_MENU_LIST).isPresent(), is(true));
    }

    public void clickProfile() {
        openProfileDropdownIfClosed();
    }

    /**
     * Re-wait after scroll; JS click avoids native click/hover closing the panel before the handler runs.
     */
    private void clickChangeHotelMenuItemWhenDropdownOpen() {
        driverTools.waitForElementToPresence(CHANGE_HOTEL_BUTTON).ifPresent(button -> {
            driverTools.scrollIntoWebView(button);
            driverTools.waitForElementToPresence(CHANGE_HOTEL_BUTTON).ifPresent(driverTools::click);
        });
    }

    public void clickChangeHotelMenu() {
        clickChangeHotelMenuItemWhenDropdownOpen();
    }

    public void clickChangeHotel(String hotelName) {
        clickProfile();
        clickChangeHotelMenuItemWhenDropdownOpen();
        WebElement option = driverTools.waitForElementToBeClickable(getHotelNameLabel(hotelName)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(option);
        Assertion.assertThat(String.format("%s is presented", hotelName), isHotelNamePresented(hotelName), is(true));
    }

    private void doChangeHotelSelection(String value) {
        clickProfile();
        clickChangeHotelMenuItemWhenDropdownOpen();
        WebElement option = driverTools.waitForElementToBeClickable(getHotelNameLabel(value)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(option);
    }

    // Pass hotel ID (number) or hotel name. If already on that hotel, no change.
    public void changeHotel(String hotelIdOrName) {
        if (isHotelId(hotelIdOrName)) {
            if (isCurrentHotelId(hotelIdOrName)) {
                return; // already on this hotel
            }
            doChangeHotelSelection(hotelIdOrName);
            skipAndCloseAnyPopUp();
            Assertion.assertThat(String.format("Hotel ID %s is presented", hotelIdOrName), verifyHotelIdOnNavbar(hotelIdOrName), is(true));
        } else {
            boolean isHotelNameLabelPresent = driverTools.waitForElementToAppear(HOTEL_NAME_LABEL_HEADER).isPresent();
            if (!isHotelNameLabelPresent) {
                clickChangeHotel(hotelIdOrName);
                skipAndCloseAnyPopUp();
            } else {
                String label = driverTools.getText(HOTEL_NAME_LABEL_HEADER).orElseThrow();
                if (!label.contains(hotelIdOrName)) {
                    clickChangeHotel(hotelIdOrName);
                    skipAndCloseAnyPopUp();
                }
            }
        }
    }

    private static boolean isHotelId(String value) {
        return value != null && value.matches("\\d+"); // only digits = hotel ID
    }

    public void clickLogoutMenu() {
        clickProfile();
        WebElement logout = driverTools.waitForElementToPresence(LOGOUT_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(logout);
        driverTools.clickWithJavascriptExecutor(logout);
        driverTools.waitForElementToAppear(CONFIRM_LOGOUT_BUTTON, 10).ifPresent(driverTools::click);
    }

    public boolean verifyHotelNameOnNavbar(String name){
        WebElement hotelName = driver.findElement(new By.ByXPath(String.format("//label[text()='%s']", name)));
        driverTools.waitForElementToAppear(hotelName);
        return driverTools.isElementDisplayed(hotelName);
    }

    public void clickChangeHotel(){
        openProfileDropdownIfClosed();
        clickChangeHotelMenuItemWhenDropdownOpen();
    }

    public void clickAddNewHotel(){
        WebElement button = driverTools.waitForElementToBeClickable(ADD_NEW_HOTEL_MODAL_BUTTON).orElseThrow();
        button.click();
        WebElement confirm = driverTools.waitForElementToBeClickable(ADD_NEW_HOTEL_CONFIRM_YES_BUTTON).orElseThrow();
        confirm.click();
    }

    public void skipCoachmark() {
        try {
            WebElement button = driverTools.waitForElementToBeClickable(SKIP_COACHMARK_BUTTON, 3).orElseThrow();
            driverTools.click(button);
        } catch (Exception e) {
            logger.log("Chat coachmark is not displayed");
        }
    }

    public void closeAllowNotificationModal() {
        try {
            WebElement button = driverTools.waitForElementToAppear(ALLOW_NOTIFICATION_MODAL_BUTTON, 3).orElseThrow();
            driverTools.click(button);
        } catch (Exception e) {
            logger.log("Allow notification modal is not displayed");
        }
    }

    public void closeMissingInformationModal() {
        try {
            WebElement button = driverTools.waitForElementToAppear(NOT_NOW_MISSING_INFORMATION_MODAL_BUTTON, 3).orElseThrow();
            driverTools.click(button);
        } catch (Exception e) {
            logger.log("Missing information modal is not displayed");
        }
    }

    public void closeJoinPPModal() {
        try {
            WebElement button = driverTools.waitForElementToAppear(NOT_NOW_JOIN_PP_MODAL_BUTTON, 3).orElseThrow();
            driverTools.click(button);
        } catch (Exception e) {
            logger.log("Join Preferred Partner modal is not displayed");
        }
    }

    public void closeWelcomeModal() {
        try {
            WebElement button = driverTools.waitForElementToBeClickable(CLOSE_WELCOME_MODAL_ICON, 3).orElseThrow();
            driverTools.click(button);
        } catch (Exception e) {
            logger.log("Welcome modal is not displayed");
        }
    }

    public void closeIncompleteContentModal() {
        try {
            WebElement button = driverTools.waitForElementToBeClickable(INCOMPLETE_CONTENT_MODAL_SKIP_BUTTON, 10).orElseThrow();
            driverTools.click(button);
        } catch (Exception e) {
            logger.log("Incomplete content modal is not displayed");
        }
    }

    public void closeIncompleteInformationModal(){
        try {
            WebElement button = driverTools.waitForElementToAppear(LATER_INCOMPLETE_INFORMATION_MODAL_BUTTON, 3).orElseThrow();
            driverTools.click(button);
        } catch (Exception e) {
            logger.log("Incomplete information modal is not displayed");
        }
    }

    /**
     * Keeps clicking the extranet homepage pop-up's primary button as long as the pop-up
     * keeps re-appearing (handles stacked / chained pop-ups). Stops as soon as the close
     * button is no longer present, or after {@code maxAttempts} clicks (safety cap).
     */
    public void closeExtranetHomepagePopUp() {
        int maxAttempts = 5;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            Optional<WebElement> button = driverTools.waitForElementToBeClickable(CLOSE_POP_UP_ON_EXTRANET_HOMEPAGE, 3);
            if (button.isEmpty()) {
                logger.log("Extranet homepage pop-up no longer present after " + (attempt - 1) + " close click(s)");
                return;
            }
            driverTools.click(button.get());
        }
        logger.log("Stopped closing extranet homepage pop-up after reaching max attempts: " + maxAttempts);
    }

    public void skipAndCloseAnyPopUp() {
        closeIncompleteContentModal();
        closeJoinPPModal();
        closeWelcomeModal();
        closeAllowNotificationModal();
        closeMissingInformationModal();
        closeIncompleteInformationModal();
        skipCoachmark();
        closeExtranetHomepagePopUp();
    }
}