package com.tiket.web.dashboard.page.payment;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.web.page.platform.payment.PaymentPageWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public class PaymentLinkConsumerPage<T extends WebDriver> extends BasePage<T> {

    private static final String LAYOUT_SCOPE = "//*[@data-testid='payment-link-layout-container']";

    private static final Locator PAYMENT_LINK_LAYOUT = new Locator("PAYMENT_LINK_LAYOUT",
            By.cssSelector("[data-testid='payment-link-layout-container']"));
    private static final Locator PRODUCT_TITLE = new Locator("PRODUCT_TITLE",
            By.xpath(LAYOUT_SCOPE + "//span[contains(@class,'OrderSummaryItem_summary_title')]//div"));
    private static final Locator PRODUCT_DESCRIPTION = new Locator("PRODUCT_DESCRIPTION",
            By.xpath(LAYOUT_SCOPE + "//div[contains(@class,'OrderSummaryItem_summary_bottom_container')]//span[contains(@class,'Text_text')]"));
    private static final Locator FINAL_PRICE = new Locator("FINAL_PRICE",
            By.xpath("(//span[contains(@class,'Text_size_b2') and contains(@class,'Text_weight_bold') and starts-with(normalize-space(),'IDR')])[2]"));
    private static final Locator COUNTDOWN_TIMER = new Locator("COUNTDOWN_TIMER",
            By.xpath(LAYOUT_SCOPE + "//ul[contains(@class,'Countdown_countdown__')]"));
    private static final Locator COMPLETE_BEFORE_HEADING = new Locator("COMPLETE_BEFORE_HEADING",
            By.xpath(LAYOUT_SCOPE + "//div[contains(@class,'Countdown_countdown_timer')]//h2"));
    private static final Locator PAYMENT_METHOD_SELECTOR = new Locator("PAYMENT_METHOD_SELECTOR",
            By.cssSelector("[data-testid='payment-method-selector']"));
    private static final Locator PAY_NOW_BUTTON = new Locator("PAY_NOW_BUTTON",
            By.xpath(LAYOUT_SCOPE + "//button[contains(@class,'PaymentLinkContainer_submit_button')]"));
    private static final Locator PRICE_DETAIL_CHEVRON = new Locator("PRICE_DETAIL_CHEVRON",
            By.xpath(LAYOUT_SCOPE + "//div[contains(@class,'PaymentLinkContainer_final_price_wrapper')]//*[@data-testid='icon_chevron']"));
    private static final Locator PRICE_DETAIL_MODAL = new Locator("PRICE_DETAIL_MODAL",
            By.xpath("(//div[contains(@class,'PriceDetailModal_content_container')] | //section[contains(@class,'Modal_modal_popup')]//h2[normalize-space()='Price Details' or normalize-space()='Detail Harga'])[3]"));
    private static final Locator PRICE_DETAIL_CLOSE_BUTTON = new Locator("PRICE_DETAIL_CLOSE_BUTTON",
            By.xpath("//button[@data-testid='modal-close-button']"));
    private static final Locator PRICE_DETAIL_ITEM_AMOUNT = new Locator("PRICE_DETAIL_ITEM_AMOUNT",
            By.xpath("(//div[contains(@class,'PriceDetailModal_content_container')]//div[contains(@class,'PriceDetail_panel_item')]//span[contains(normalize-space(.),'IDR')])[2]"));
    private static final Locator TRANSACTION_FEE_AMOUNT = new Locator("TRANSACTION_FEE_AMOUNT",
            By.xpath("(//li[contains(@class,'PriceDetail_price_list_item')]//span[contains(@class,'PriceDetail_title_value') and (normalize-space()='Transaction fee' or normalize-space()='Biaya transaksi')]/ancestor::div[contains(@class,'PriceDetail_detail_content_wrapper')]//span[contains(normalize-space(.),'IDR')])[2]"));
    private static final Locator CHECKOUT_TOTAL_AMOUNT = new Locator("CHECKOUT_TOTAL_AMOUNT",
            By.xpath("//span[contains(@class,'Text_size_b2') and contains(@class,'Text_weight_bold') and contains(normalize-space(.),'IDR')]"));
    private static final Locator ORDER_ID_TEXT = new Locator("ORDER_ID_TEXT",
            By.xpath("(//*[starts-with(normalize-space(.),'Order ID:') or starts-with(normalize-space(.),'ID Pesanan:')])[4]"));
    private static final Locator PAYMENT_LINK_ERROR_DIALOG = new Locator("PAYMENT_LINK_ERROR_DIALOG", By.xpath("//div[contains(@class,'Dialog_body')]//section[contains(@class,'Figure_figure')]//*[contains(@class,'Figure_title') or contains(@class,'Figure_description')][contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'something went wrong') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'payment has not been processed') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'technical problem') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'terjadi kesalahan')]"));
    private static final Locator PAYMENT_LINK_ERROR_ACTION_BUTTON = new Locator("PAYMENT_LINK_ERROR_ACTION_BUTTON", By.xpath("//div[contains(@class,'Dialog_body')]//button[.//span[contains(normalize-space(.),'Change Payment Method') or contains(normalize-space(.),'Ganti metode pembayaran')]]"));
    private static final Locator LIMIT_REACHED_TEXT = new Locator("LIMIT_REACHED_TEXT", By.xpath("//*[self::h1 or self::h2 or self::p or self::span][contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'limit reached') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'link exhausted') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'payment link unavailable') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'sudah digunakan') or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'tidak tersedia')]"));
    private static final Locator FINAL_AMT = new Locator("FINAL_AMT",
            By.xpath("//*[@data-testid='payment-link-layout-container']//p[contains(@class,'PaymentLinkContainer_final_price')]"));
    private String adminWindowHandle;

    public PaymentLinkConsumerPage(Session<T> session) {
        super(session);
    }

    public void rememberAdminWindow() {
        adminWindowHandle = driver.getWindowHandle();
    }

    public void openPaymentLinkInNewTab(String paymentLinkUrl) {
        driverTools.openNewTabAndPasteUrl(paymentLinkUrl);
    }

    public void switchBackToAdminWindow() {
        if (adminWindowHandle != null) {
            driver.switchTo().window(adminWindowHandle);
            return;
        }
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            driver.switchTo().window(handle);
            if (driver.getCurrentUrl().contains("/admin/")) {
                return;
            }
        }
    }

    public void closeCurrentTabAndReturnToAdmin() {
        driver.close();
        switchBackToAdminWindow();
    }

    public boolean isPaymentPageLoaded() {
        return driverTools.waitForElementToAppear(PAYMENT_LINK_LAYOUT, 20).isPresent()
                && driverTools.waitForElementToAppear(FINAL_AMT, 20).isPresent();
    }

    public boolean isProductTitleDisplayed(String titleEn, String titleId) {
        String titleText = driverTools.getText(PRODUCT_TITLE).orElse("");
        return titleText.contains(titleEn) || titleText.contains(titleId);
    }

    public boolean isProductDescriptionDisplayed(String descriptionEn, String descriptionId) {
        String descriptionText = driverTools.getText(PRODUCT_DESCRIPTION).orElse("");
        return descriptionText.contains(descriptionEn) || descriptionText.contains(descriptionId);
    }

    public long getDisplayedTotalAmount() {
        String totalText = driverTools.getText(FINAL_AMT).orElse("0");
        return Long.parseLong(CoreFunctions.removeNonDigit(totalText));
    }

    public void waitForDisplayedTotalAmount(long expectedAmount, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(d -> getDisplayedTotalAmount() == expectedAmount);
    }

    public boolean isCountdownTimerVisible() {
        return driverTools.waitForElementToAppear(COUNTDOWN_TIMER, 10).isPresent();
    }

    public boolean isCompleteBeforeSectionVisible() {
        String heading = driverTools.getText(COMPLETE_BEFORE_HEADING).orElse("");
        return heading.contains("Complete before") || heading.contains("Selesaikan sebelum");
    }

    public boolean isPaymentMethodDisplayed(String paymentMethodConsumer) {
        return driverTools.waitForElementToAppear(getPaymentMethodTitleLocator(paymentMethodConsumer), 10).isPresent();
    }

    public void selectPaymentMethod(String paymentMethodConsumer) {
        Locator paymentMethodLabel = new Locator("PAYMENT_METHOD_LABEL_" + paymentMethodConsumer,
                By.xpath("//*[@data-testid='payment-method-selector']//label[.//span[contains(@class,'PaymentMethodSelector_method_title') and normalize-space()='"
                        + paymentMethodConsumer + "']]"));
        WebElement label = driverTools.waitForElementToBeClickable(paymentMethodLabel, 10).orElseThrow();
        driverTools.scrollElementToCenter(label);
        driverTools.clickWithActions(label);
        driverTools.dummyWait(1);
    }

    public boolean isPayNowButtonEnabled() {
        return driverTools.waitForElementToAppear(PAY_NOW_BUTTON, 10)
                .map(WebElement::isEnabled)
                .orElse(false);
    }

    public void openPriceDetail() {
        WebElement chevron = driverTools.waitForElementToBeClickable(PRICE_DETAIL_CHEVRON, 10).orElseThrow();
        driverTools.scrollElementToCenter(chevron);
        driverTools.clickWithActions(chevron);
        driverTools.waitForElementToAppear(PRICE_DETAIL_MODAL, 10);
    }

    public boolean isPriceDetailOpened() {
        return driverTools.waitForElementToAppear(PRICE_DETAIL_MODAL, 5).isPresent();
    }

    public long getTransactionFeeAmount() {
        String feeText = driverTools.getText(TRANSACTION_FEE_AMOUNT).orElse("0");
        return Long.parseLong(CoreFunctions.removeNonDigit(feeText));
    }

    public long getPriceDetailItemAmount() {
        String itemText = driverTools.getText(PRICE_DETAIL_ITEM_AMOUNT).orElse("0");
        return Long.parseLong(CoreFunctions.removeNonDigit(itemText));
    }

    public long getPriceDetailTotalAmount() {
        String totalText = driverTools.getText(FINAL_PRICE).orElse("0");
        return Long.parseLong(CoreFunctions.removeNonDigit(totalText));
    }

    public boolean isTransactionFeeDisplayed() {
        return driverTools.waitForElementToAppear(TRANSACTION_FEE_AMOUNT, 5).isPresent();
    }

    public boolean verifyBaseAmountPlusTransactionFeeEqualsTotal(long baseAmount) {
        if (!isTransactionFeeDisplayed()) {
            return false;
        }
        long itemAmount = getPriceDetailItemAmount();
        if (itemAmount == 0) {
            itemAmount = baseAmount;
        }
        long transactionFee = getTransactionFeeAmount();
        return itemAmount + transactionFee == getDisplayedTotalAmount();
    }

    public void closePriceDetail() {
        if (driverTools.waitForElementToAppear(PRICE_DETAIL_CLOSE_BUTTON, 3).isPresent()) {
            driverTools.click(PRICE_DETAIL_CLOSE_BUTTON);
            driverTools.waitForElementToDisappear(PRICE_DETAIL_MODAL, 5);
            return;
        }
        if (driverTools.waitForElementToAppear(PRICE_DETAIL_CHEVRON, 3).isPresent()) {
            driverTools.click(PRICE_DETAIL_CHEVRON);
            driverTools.waitForElementToDisappear(PRICE_DETAIL_MODAL, 5);
        }
    }

    public void clickPayNow() {
        WebElement payNowButton = driverTools.waitForElementToBeClickable(PAY_NOW_BUTTON, 10).orElseThrow();
        driverTools.scrollElementToCenter(payNowButton);
        driverTools.clickWithActions(payNowButton);
    }

    public long getCheckoutPageTotalAmount() {
        driverTools.waitForElementToPresence(CHECKOUT_TOTAL_AMOUNT, 15);
        String totalText = driverTools.getText(CHECKOUT_TOTAL_AMOUNT).orElse("0");
        return Long.parseLong(CoreFunctions.removeNonDigit(totalText));
    }

    public String getOrderId() {
        WebElement orderIdElement = driverTools.waitForElementToAppear(ORDER_ID_TEXT, 15).orElseThrow();
        String orderIdText = driverTools.getText(orderIdElement).trim();
        int colonIndex = orderIdText.indexOf(":");
        if (colonIndex >= 0) {
            return CoreFunctions.removeNonDigit(orderIdText.substring(colonIndex + 1));
        }
        return CoreFunctions.removeNonDigit(orderIdText);
    }

    public void completePaymentOnCheckoutPage() {
        PaymentPageWeb<?> paymentPage = PaymentPageWeb.getInstance(session);
        paymentPage.handle3DSPage(30);
    }

    public void reloadPaymentPage() {
        driverTools.refreshWebpage();
    }

    public boolean isPaymentCompletedSuccessfully() {
        return PaymentPageWeb.getInstance(session).isPaymentSuccessfulVisible();
    }

    public boolean isLimitReachedMessageDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(d -> driverTools.isElementDisplayed(PAYMENT_LINK_ERROR_DIALOG)
                            || driverTools.isElementDisplayed(PAYMENT_LINK_ERROR_ACTION_BUTTON)
                            || driverTools.isElementDisplayed(LIMIT_REACHED_TEXT));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isPayNowDisabledOrHidden() {
        if (driverTools.waitForElementToAppear(PAY_NOW_BUTTON, 5).isEmpty()) {
            return true;
        }
        WebElement payNowButton = driverTools.waitForElementToAppear(PAY_NOW_BUTTON).orElseThrow();
        return !payNowButton.isEnabled() || !payNowButton.isDisplayed();
    }

    private Locator getPaymentMethodTitleLocator(String paymentMethodConsumer) {
        return new Locator("PAYMENT_METHOD_TITLE_" + paymentMethodConsumer,
                By.xpath("//*[@data-testid='payment-method-selector']//span[contains(@class,'PaymentMethodSelector_method_title') and normalize-space()='"
                        + paymentMethodConsumer + "']"));
    }
}
