package com.tiket.page.common.mailBox;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.dashboard.page.common.InboxPageYopmail;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * iOS override of {@link InboxPageYopmail}.
 * On iOS the Yopmail page opened via {@code mAppOpenUrl} is driven fully natively: there's no
 * webview context/iframe to switch into, so every overridden method here skips
 * switchToIframe/switchBackFromIframe entirely and queries elements directly. Locators that
 * matched HTML tags/attributes (div, button, @class, text()/.) are also rewritten to a generic
 * element ({@code *}) matched on {@code @label} instead.
 */
public class InboxPageYopmailIOS<T extends WebDriver> extends InboxPageYopmail<T> {

    public InboxPageYopmailIOS(Session<T> session) {
        super(session);
    }

    /** Opens Yopmail inbox in Safari; native only, so no webview context switch is needed. */
    @Override
    public void openYopmailInboxInMobileBrowser(String email) {
        String inboxUrl = buildInboxUrl(email);
        driverTools.mAppOpenUrl(inboxUrl);
        waitForInboxToLoad();
    }

    @Override
    protected void waitForInboxToLoad() {
        driverTools.waitForElementToPresence(REFRESH_INBOX, 30).orElseThrow(
                () -> new RuntimeException("Yopmail inbox did not load"));
    }

    @Override
    public void refreshInbox() {
        driverTools.back();
        driverTools.click(REFRESH_INBOX);
        waitForInboxToLoad();
    }

    private static final Locator GIFT_VOUCHER_INBOX_CANDIDATE = new Locator("GIFT_VOUCHER_INBOX_CANDIDATE",
            new By.ByXPath("//*[contains(translate(normalize-space(@label),'" + LOWER + "','" + UPPER + "'),'gift voucher')]"));
    private static final Locator GIFT_VOUCHER_INBOX_FIRST = new Locator("GIFT_VOUCHER_INBOX_FIRST",
            new By.ByXPath("(//*[contains(translate(normalize-space(@label),'" + LOWER + "','" + UPPER + "'),'payment receipt')])[1]//preceding::*[contains(translate(normalize-space(@label),'" + LOWER + "','" + UPPER + "'),'gift voucher')]"));
    private static final Locator GIFT_VOUCHER_GREETING_TEXT = new Locator("GIFT_VOUCHER_GREETING_TEXT",
            new By.ByXPath("//*[contains(normalize-space(@label),'Hi, sobat tiket') or contains(normalize-space(@label),'Hi sobat tiket') or contains(normalize-space(@label),'Cheer up')]"));
    private static final Locator GIFT_VOUCHER_WORTH_TEXT = new Locator("GIFT_VOUCHER_WORTH_TEXT",
            new By.ByXPath("//*[contains(translate(normalize-space(@label),'" + LOWER + "','" + UPPER + "'),'gift voucher worth')]"));
    private static final Locator GIFT_VOUCHER_CODE_TEXT = new Locator("GIFT_VOUCHER_CODE_TEXT",
            new By.ByXPath("//*[contains(translate(normalize-space(@label),'" + LOWER + "','" + UPPER + "'),'voucher code')]"));
    private static final Locator GIFT_VOUCHER_TERMS_TEXT = new Locator("GIFT_VOUCHER_TERMS_TEXT",
            new By.ByXPath("//*[contains(normalize-space(@label),'Terms & Conditions') or contains(normalize-space(@label),'Terms &amp; Conditions')]"));

    private static final String E_VOUCHER_INBOX_XPATH =
            "//*[contains(translate(normalize-space(@label),'" + LOWER + "','" + UPPER + "'),'e-voucher')"
                    + " or contains(translate(normalize-space(@label),'" + LOWER + "','" + UPPER + "'),'e-tiket')"
                    + " or contains(translate(normalize-space(@label),'" + LOWER + "','" + UPPER + "'),'e-ticket')]";
    private static final String ORDER_RECEIPT_INBOX_XPATH =
            "//*[contains(translate(normalize-space(@label),'" + LOWER + "','" + UPPER + "'),'payment receipt')"
                    + " or contains(translate(normalize-space(@label),'" + LOWER + "','" + UPPER + "'),'bukti pembayaran')]";

    @Override
    protected String getEVoucherInboxXpath() {
        return E_VOUCHER_INBOX_XPATH;
    }

    @Override
    protected String getOrderReceiptInboxXpath() {
        return ORDER_RECEIPT_INBOX_XPATH;
    }

    @Override
    protected boolean openEmailFromInbox(String inboxXpath) {
        Locator inboxCandidate = new Locator("INBOX_EMAIL_CANDIDATE", new By.ByXPath(inboxXpath));
        driverTools.waitForElementToPresence(inboxCandidate);
        int candidateCount = driverTools.elements(inboxCandidate).map(List::size).orElse(0);
        logger.log(emailTypeLabel(inboxXpath) + " inbox candidates found: " + candidateCount);
        if (candidateCount == 0) {
            return false;
        }
        Locator firstEmail = new Locator("INBOX_EMAIL_FIRST", new By.ByXPath("(" + inboxXpath + ")[1]"));
        return driverTools.tryClickWhenPresent(firstEmail, 10);
    }

    @Override
    protected boolean openGiftVoucherEmailFromInbox() {
        int candidateCount = driverTools.elements(GIFT_VOUCHER_INBOX_CANDIDATE).map(List::size).orElse(0);
        logger.log("Gift voucher inbox candidates found: " + candidateCount);
        if (candidateCount == 0) {
            return false;
        }
        return driverTools.tryClickWhenPresent(GIFT_VOUCHER_INBOX_FIRST, 10);
    }

    @Override
    public boolean verifyGiftVoucherEmailBody() {
        driverTools.waitForElementToPresence(GIFT_VOUCHER_CODE_TEXT);
        boolean hasGreeting = driverTools.isElementDisplayed(GIFT_VOUCHER_GREETING_TEXT)
                || driverTools.isElementDisplayed(GIFT_VOUCHER_WORTH_TEXT);
        boolean hasVoucherCode = driverTools.isElementDisplayed(GIFT_VOUCHER_CODE_TEXT);
        boolean hasTerms = driverTools.isElementDisplayed(GIFT_VOUCHER_TERMS_TEXT);
        logger.log("Gift voucher email body checks - greeting/worth: " + hasGreeting
                + ", voucherCode: " + hasVoucherCode + ", terms: " + hasTerms);
        return hasGreeting && hasVoucherCode && hasTerms;
    }

    @Override
    protected boolean verifyEmailBodyContainsOrderId(String orderId) {
        Locator orderIdInMail = new Locator("ORDER_ID_IN_MAIL",
                new By.ByXPath("(//*[contains(@label, '#" + orderId + "') or contains(@label, '" + orderId + "')])[last()]"));
        boolean found = driverTools.waitForElementToAppear(orderIdInMail, 15).isPresent();
        logger.log("Order ID " + orderId + " found in email body: " + found);
        return found;
    }
}
