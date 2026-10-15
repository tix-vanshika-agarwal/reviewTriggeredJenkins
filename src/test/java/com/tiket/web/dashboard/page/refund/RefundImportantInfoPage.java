package com.tiket.web.dashboard.page.refund;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class RefundImportantInfoPage<T extends WebDriver> extends BasePage<T> {
    public RefundImportantInfoPage(Session<T> session) { super(session);}

    private static final Locator MENU_REFUND_IMP_INFO = new Locator("MENU_REFUND_IMP_INFO", new By.ByXPath("//span[contains(text(),'Important Info')]"));
    private static final Locator TITLE_REFUND_IMP_INFO = new Locator("TITLE_REFUND_IMP_INFO", new By.ByXPath("//h1[contains(text(),'Important Info')]"));
    private static final Locator BUTTON_PAYMENT_METHOD = new Locator("BUTTON_PAYMENT_METHOD", new By.ByXPath("//div//span[contains(text(),'Payment Method')]"));
    private static final Locator BUTTON_SEARCH = new Locator("BUTTON_SEARCH", new By.ByXPath("//button[@type='submit'][normalize-space()='Search']"));
    private static final Locator FIRST_ACTION_BUTTON = new Locator("FIRST_ACTION_BUTTON", new By.ByXPath("(//div[@aria-roledescription='button for toggle menu'])[1]"));
    private static final Locator BUTTON_SEE_LOG = new Locator("BUTTON_SEE_LOG", new By.ByXPath("//div[@role='menuitem' and contains(., 'See Log')]"));
    private static final Locator TITLE_SEE_LOG = new Locator("TITLE_SEE_LOG", new By.ByXPath("//h2[contains(text(),'See Log')]"));
    private static final Locator MODAL_DATA_TABLE = new Locator("MODAL_DATA_TABLE", new By.ByXPath("(//div[@data-testid='data-table'])[last()]"));
    private static final Locator TITLE_DETAILS = new Locator("TITLE_DETAILS", new By.ByXPath("//header//h2[normalize-space(text())='Details']"));
    private static final Locator MODAL_CLOSE_BUTTON = new Locator("MODAL_CLOSE_BUTTON", new By.ByXPath("//button[@data-testid='modal-close-button']"));
    private static final Locator TABLE_ROWS = new Locator("TABLE_ROWS", new By.ByXPath("//div[contains(@data-testid,'table')]//table//tbody//tr"));

    public void goToRefundImportantInfoTab() {
        driverTools.waitForElementToAppear(MENU_REFUND_IMP_INFO).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(MENU_REFUND_IMP_INFO + "element not found");
        });
    }

    public boolean isOnRefundImportantInfoTab() {
        driverTools.waitForElementToAppear(TITLE_REFUND_IMP_INFO);
        return driverTools.isElementDisplayed(TITLE_REFUND_IMP_INFO);
    }

    public void clickButtonPaymentMethodFilter() {
        driverTools.waitForElementToAppear(BUTTON_PAYMENT_METHOD).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(BUTTON_PAYMENT_METHOD + "element not found");
        });
    }

    private Locator getPaymentMethodLocator(String paymentMethod) {
        String paymentMethodLocator = String.format("//label//span[text()='%s']", paymentMethod);

        return new Locator("PAYMENT_METHOD_LOCATOR_" + paymentMethod.toUpperCase(), new By.ByXPath(paymentMethodLocator));
    }

    public void selectPaymentMethodInFilter(String paymentMethod) {
        Locator paymentMethodLocator = getPaymentMethodLocator(paymentMethod);
        driverTools.waitForElementToAppear(paymentMethodLocator).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(paymentMethodLocator + "element not found");
        });
        driverTools.waitForElementToAppear(BUTTON_SEARCH).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(BUTTON_SEARCH + "element not found");
        });
    }

    private Locator getPaymentMethodResultLocator(String paymentMethod) {
        String paymentMethodResultLocator = String.format("//p[text()='%s']", paymentMethod.toLowerCase());
        return new Locator("PAYMENT_METHOD_RESULT_" + paymentMethod.toUpperCase(), new By.ByXPath(paymentMethodResultLocator));
    }

    public boolean isResultForPaymentMethodAvailable(String paymentMethod) {
        Locator paymentMethodResultLocator = getPaymentMethodResultLocator(paymentMethod);

        return driverTools.waitForElementToAppear(paymentMethodResultLocator).isPresent();
    }

    private Locator getResultRowsForPaymentMethodLocator(String paymentMethod) {
        String resultRowsLocator = String.format("//div[contains(@data-testid,'table')]//table//tbody//tr[.//p[text()='%s']]", paymentMethod.toLowerCase());

        return new Locator("RESULT_ROWS_" + paymentMethod.toUpperCase(), new By.ByXPath(resultRowsLocator));
    }

    public boolean isAllResultsForPaymentMethod(String paymentMethod) {
        int totalRows = driverTools.waitForElementsToAppear(TABLE_ROWS).map(List::size).orElse(0);
        int matchingRows = driverTools.waitForElementsToAppear(getResultRowsForPaymentMethodLocator(paymentMethod)).map(List::size).orElse(0);

        return totalRows > 0 && totalRows == matchingRows;
    }

    public void clickButtonAction() {
        driverTools.waitForElementToAppear(FIRST_ACTION_BUTTON).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(FIRST_ACTION_BUTTON + "element not found");
        });
    }

    public void clickButtonSeeLog() {
        driverTools.waitForElementToAppear(BUTTON_SEE_LOG).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(BUTTON_SEE_LOG + "element not found");
        });
    }

    private Locator getDetailsButtonLocator(String paymentMethod) {
        String detailsButtonLocator = String.format("//tr[.//p[text()='%s']]//button[normalize-space(text())='Details']", paymentMethod.toLowerCase());

        return new Locator("BUTTON_DETAILS_" + paymentMethod.toUpperCase(), new By.ByXPath(detailsButtonLocator));
    }

    public void clickButtonDetails(String paymentMethod) {
        Locator detailsButtonLocator = getDetailsButtonLocator(paymentMethod);
        driverTools.waitForElementToAppear(detailsButtonLocator).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(detailsButtonLocator + "element not found");
        });
    }

    public boolean isModalDetailsOpened() {
        driverTools.waitForElementToAppear(TITLE_DETAILS);
        boolean isTitleDisplayed = driverTools.isElementDisplayed(TITLE_DETAILS);

        driverTools.waitForElementToAppear(MODAL_CLOSE_BUTTON);
        boolean isCloseButtonDisplayed = driverTools.isElementDisplayed(MODAL_CLOSE_BUTTON);

        return isTitleDisplayed && isCloseButtonDisplayed;
    }

    public boolean isModalSeeLogOpened() {
        driverTools.waitForElementToAppear(TITLE_SEE_LOG);
        boolean isTitleDisplayed = driverTools.isElementDisplayed(TITLE_SEE_LOG);

        driverTools.waitForElementToAppear(MODAL_DATA_TABLE);
        boolean isTableDisplayed = driverTools.isElementDisplayed(MODAL_DATA_TABLE);

        return isTitleDisplayed && isTableDisplayed;
    }

    private Locator getStatusToggleLocator(String paymentMethod) {
        String statusToggleLocator = String.format("//tr[.//p[text()='%s']]//button[@role='switch']", paymentMethod.toLowerCase());

        return new Locator("STATUS_TOGGLE_" + paymentMethod.toUpperCase(), new By.ByXPath(statusToggleLocator));
    }

    private Locator getStatusToggleLocator(String paymentMethod, boolean isActive) {
        String statusToggleLocator = String.format("//tr[.//p[text()='%s']]//button[@role='switch' and @aria-checked='%s']", paymentMethod.toLowerCase(), isActive);

        return new Locator("STATUS_TOGGLE_" + paymentMethod.toUpperCase() + "_ACTIVE_" + isActive, new By.ByXPath(statusToggleLocator));
    }

    public void clickStatusToggle(String paymentMethod) {
        Locator statusToggleLocator = getStatusToggleLocator(paymentMethod);
        driverTools.waitForElementToBeClickable(statusToggleLocator).ifPresentOrElse(driverTools::click, () -> {
            throw new NoSuchElementException(statusToggleLocator + "element not found");
        });
    }

    public boolean isStatusActive(String paymentMethod) {
        return driverTools.waitForElementToAppear(getStatusToggleLocator(paymentMethod, true)).isPresent();
    }

    public boolean isStatusInactive(String paymentMethod) {
        return driverTools.waitForElementToAppear(getStatusToggleLocator(paymentMethod, false)).isPresent();
    }
}