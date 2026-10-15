package com.tiket.web.dashboard.page.common;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public class InboxPageYopmail<T extends WebDriver> extends BasePage<T> {

    private static final Locator YOPMAIL_LOGO = new Locator("YOPMAIL_LOGO", new By.ByXPath("//img[contains(@alt, 'YOPmail')]"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", new By.ByXPath("//input[@class='ycptinput']"));
    private static final Locator CHECK_INBOX_BUTTON = new Locator("CHECK_INBOX_BUTTON", new By.ByXPath("//button[contains(@title, 'Check Inbox')]"));
    public static final Locator INBOX_IFRAME = new Locator("INBOX_IFRAME", By.xpath("//iframe[@id='ifinbox']"));
    public static final Locator MAIL_IFRAME = new Locator("MAIL_IFRAME", By.xpath("//iframe[@id='ifmail']"));
    public static final Locator MAIL_IFRAME_MOBILE = new Locator("MAIL_IFRAME_MOBILE", By.xpath("//iframe[@id='ifmobmail']"));
    Locator ORDER_ID = new Locator("ORDER_ID", new By.ByXPath("//strong[contains(text(), '#')]"));
    Locator DEPARTURE_STATION = new Locator("DEPARTURE_STATION", new By.ByXPath("(//p[contains(text(), '(')])[1]"));
    Locator ARRIVAL_STATION = new Locator("ARRIVAL_STATION", new By.ByXPath("(//p[contains(text(), '(')])[2]"));
    Locator DEPARTURE_DATE = new Locator("DEPARTURE_DATE", new By.ByXPath("(//p[contains(text(), ',')])[1]"));
    protected Locator REFRESH_INBOX = new Locator("REFRESH_INBOX", By.xpath("//*[@id='refresh']"));
    Locator EMAIL_CANDIDATE = new Locator("EMAIL_CANDIDATE", new By.ByXPath("//div[contains(text(), 'E-tiket')]"));
    protected static final String LOWER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    protected static final String UPPER = "abcdefghijklmnopqrstuvwxyz";
    // Inbox subject example: "Take a Break, Enjoy Your Gift Voucher IDR 50,000.00!"
    private static final String GIFT_VOUCHER_INBOX_XPATH = "//div[contains(@class,'m')]//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'gift voucher')] | //button[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'gift voucher')]";
    private static final Locator GIFT_VOUCHER_INBOX_CANDIDATE = new Locator("GIFT_VOUCHER_INBOX_CANDIDATE", new By.ByXPath(GIFT_VOUCHER_INBOX_XPATH));
    private static final Locator GIFT_VOUCHER_INBOX_FIRST = new Locator("GIFT_VOUCHER_INBOX_FIRST", new By.ByXPath("(//*[contains(translate(normalize-space(text()),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'payment receipt')])[1]//preceding::*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'gift voucher')]"));
    private static final Locator GIFT_VOUCHER_GREETING_TEXT = new Locator("GIFT_VOUCHER_GREETING_TEXT", new By.ByXPath("//*[contains(normalize-space(.),'Hi, sobat tiket') or contains(normalize-space(.),'Hi sobat tiket') or contains(normalize-space(.),'Cheer up')]"));
    private static final Locator GIFT_VOUCHER_WORTH_TEXT = new Locator("GIFT_VOUCHER_WORTH_TEXT", new By.ByXPath("//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'gift voucher worth')]"));
    private static final Locator GIFT_VOUCHER_CODE_TEXT = new Locator("GIFT_VOUCHER_CODE_TEXT", new By.ByXPath("//*[contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'voucher code')]"));
    private static final Locator GIFT_VOUCHER_TERMS_TEXT = new Locator("GIFT_VOUCHER_TERMS_TEXT", new By.ByXPath("//*[contains(normalize-space(.),'Terms & Conditions') or contains(normalize-space(.),'Terms &amp; Conditions')]"));
    private static final int GIFT_VOUCHER_POLL_ATTEMPTS = 5;
    private static final int GIFT_VOUCHER_WAIT_BEFORE_REFRESH_SECONDS = 15;
    private static final String E_VOUCHER_INBOX_XPATH =
            "//div[@class='m' and (contains(translate(normalize-space(.),'" + LOWER + "','" + UPPER + "'),'e-voucher')"
                    + " or contains(translate(normalize-space(.),'" + LOWER + "','" + UPPER + "'),'e-tiket')"
                    + " or contains(translate(normalize-space(.),'" + LOWER + "','" + UPPER + "'),'e-ticket'))]";
    private static final String ORDER_RECEIPT_INBOX_XPATH =
            "//div[@class='m' and (contains(translate(normalize-space(.),'" + LOWER + "','" + UPPER + "'),'payment receipt')"
                + " or contains(translate(normalize-space(.),'" + LOWER + "','" + UPPER + "'),'bukti pembayaran'))]";

    public InboxPageYopmail(Session<T> session) {
        super(session);
    }

    public void goToYopmail() {
        driverTools.get("https://yopmail.com/en/");
        driverTools.waitForElementToAppear(YOPMAIL_LOGO).orElseThrow();
    }

    public void inputEmail(String email) {
        driverTools.waitForElementToBeClickable(EMAIL_INPUT, 180);
        driverTools.click(EMAIL_INPUT);
        driverTools.sendKeys(EMAIL_INPUT, email);
    }

    public void goToInbox(){
        driverTools.click(CHECK_INBOX_BUTTON);
        waitForInboxToLoad();
    }

    public void waitForYopmailPage() {
        driverTools.waitForElementToAppear(YOPMAIL_LOGO).orElseThrow();
    }

    public void openYopmailInNewTab() {
        driverTools.openNewTabAndPasteUrl("https://yopmail.com/en/");
        waitForYopmailPage();
    }

    public void openYopmailInboxInNewTab(String email) {
        driverTools.openNewTabAndPasteUrl(buildInboxUrl(email));
        waitForInboxToLoad();
    }

    /** Opens Yopmail inbox in the device browser (Chrome/Safari) then switches into the webview. */
    public void openYopmailInboxInMobileBrowser(String email) {
        String inboxUrl = buildInboxUrl(email);
        driverTools.mAppOpenUrl(inboxUrl);
        driverTools.switchToWebViewUrl("yopmail", 10);
        waitForInboxToLoad();
    }

    public void openInboxForEmail(String email) {
        driverTools.get(buildInboxUrl(email));
        waitForInboxToLoad();
    }

    protected String buildInboxUrl(String email) {
        String inboxName = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        return "https://yopmail.com/en/?login=" + inboxName;
    }

    protected void waitForInboxToLoad() {
        driverTools.waitForElementToPresence(INBOX_IFRAME, 30).orElseThrow(
                () -> new RuntimeException("Yopmail inbox iframe did not load"));
    }

    /**
     * MAIL_IFRAME_MOBILE when the automation is running on mobile (native app or mobile web:
     * ANDROID, IOS, AMWEB, IMWEB); MAIL_IFRAME is only for desktop web (DWEB).
     */
    protected Locator getMailIframeLocator() {
        return Configs.PLATFORM == Platform.DWEB ? MAIL_IFRAME : MAIL_IFRAME_MOBILE;
    }

    public void closeYopmailAndReturnTo(String mainWindowHandle) {
        driverTools.closeCurrentTab();
        driverTools.switchToWindow(mainWindowHandle);
    }

    /**
     * Polls Yopmail inbox for the post-booking gift voucher email and validates its body.
     * After opening the inbox, waits ~1 minute then clicks refresh, up to 10 times,
     * until the gift voucher mail appears. Test account must use a {@code @yopmail.com} address.
     */
    public boolean verifyGiftVoucherEmailReceived() throws InterruptedException {
        return verifyGiftVoucherEmailReceived(GIFT_VOUCHER_POLL_ATTEMPTS, GIFT_VOUCHER_WAIT_BEFORE_REFRESH_SECONDS);
    }

    /**
     * Same as {@link #verifyGiftVoucherEmailReceived()} with custom poll attempts and wait interval (seconds).
     */
    public boolean verifyGiftVoucherEmailReceived(int maxAttempts, int waitBeforeRefreshSeconds) throws InterruptedException {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            logger.log("Waiting " + waitBeforeRefreshSeconds
                    + "s before Yopmail refresh, attempt " + attempt + "/" + maxAttempts);
            driverTools.dummyWait(waitBeforeRefreshSeconds);
            refreshInbox();
            logger.log("Polling Yopmail for gift voucher email, attempt " + attempt + "/" + maxAttempts);
            if (openGiftVoucherEmailFromInbox() && verifyGiftVoucherEmailBody()) {
                return true;
            } else {
                logger.log("gift voucher is not found at loop: " + attempt + ", continue to the next loop");
                continue;
            }
        }
        return false;
    }

    protected boolean openGiftVoucherEmailFromInbox() {
        driverTools.switchToIframe(driverTools.waitForElementToPresence(INBOX_IFRAME).orElseThrow());
        int candidateCount = driverTools.elements(GIFT_VOUCHER_INBOX_CANDIDATE).map(List::size).orElse(0);
        logger.log("Gift voucher inbox candidates found: " + candidateCount);
        if (candidateCount == 0) {
            driverTools.switchBackFromIframe();
            return false;
        }
        if (!driverTools.tryClickWhenPresent(GIFT_VOUCHER_INBOX_FIRST, 10)) {
            driverTools.switchBackFromIframe();
            return false;
        }
        driverTools.switchBackFromIframe();
        driverTools.waitForElementToPresence(getMailIframeLocator(), 30);
        return true;
    }

    public boolean verifyGiftVoucherEmailBody() {
        driverTools.switchToIframe(driverTools.waitForElementToPresence(getMailIframeLocator()).orElseThrow());
        driverTools.waitForElementToPresence(GIFT_VOUCHER_CODE_TEXT);
        boolean hasGreeting = driverTools.isElementDisplayed(GIFT_VOUCHER_GREETING_TEXT)
                || driverTools.isElementDisplayed(GIFT_VOUCHER_WORTH_TEXT);
        boolean hasVoucherCode = driverTools.isElementDisplayed(GIFT_VOUCHER_CODE_TEXT);
        boolean hasTerms = driverTools.isElementDisplayed(GIFT_VOUCHER_TERMS_TEXT);
        logger.log("Gift voucher email body checks - greeting/worth: " + hasGreeting
                + ", voucherCode: " + hasVoucherCode + ", terms: " + hasTerms);
        driverTools.switchBackFromIframe();
        return hasGreeting && hasVoucherCode && hasTerms;
    }

    /**
     * Polls Yopmail inbox for the TTD e-voucher email and validates that it contains the order ID.
     * Test account must use a {@code @yopmail.com} address.
     */
    public boolean verifyEvoucherEmailReceived(String orderId) throws InterruptedException {
        return verifyEmailReceived(orderId, getEVoucherInboxXpath(), "e-voucher");
    }

    /**
     * Polls Yopmail inbox for the order-receipt email and validates that it contains the order ID.
     * Test account must use a {@code @yopmail.com} address.
     */
    public boolean verifyOrderReceiptEmailReceived(String orderId) throws InterruptedException {
        return verifyEmailReceived(orderId, getOrderReceiptInboxXpath(), "order-receipt");
    }

    /** E-voucher inbox xpath for the webview/HTML DOM (Android app webview and all web platforms). */
    protected String getEVoucherInboxXpath() {
        return E_VOUCHER_INBOX_XPATH;
    }

    /** Order-receipt inbox xpath for the webview/HTML DOM (Android app webview and all web platforms). */
    protected String getOrderReceiptInboxXpath() {
        return ORDER_RECEIPT_INBOX_XPATH;
    }

    private boolean verifyEmailReceived(String orderId, String inboxXpath, String emailType) throws InterruptedException {
        int maxAttempts = 5;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            logger.log("Polling Yopmail for " + emailType + " email, attempt " + attempt + "/" + maxAttempts);
            if (openEmailFromInbox(inboxXpath) && verifyEmailBodyContainsOrderId(orderId)) {
                if (Configs.PLATFORM != Platform.DWEB) {
                    // On mobile, the opened mail replaces the inbox list, so navigate back to it natively before refreshing.
                    driverTools.back();
                }
                return true;
            }
            if (attempt < maxAttempts) {
                driverTools.dummyWait(3);
                refreshInbox();
            }
        }
        return false;
    }

    protected boolean openEmailFromInbox(String inboxXpath) {
        driverTools.switchToIframe(driverTools.waitForElementToPresence(INBOX_IFRAME).orElseThrow());
        Locator inboxCandidate = new Locator("INBOX_EMAIL_CANDIDATE", new By.ByXPath(inboxXpath));
        driverTools.waitForElementToPresence(inboxCandidate);
        int candidateCount = driverTools.elements(inboxCandidate).map(List::size).orElse(0);
        logger.log(emailTypeLabel(inboxXpath) + " inbox candidates found: " + candidateCount);
        if (candidateCount == 0) {
            driverTools.switchBackFromIframe();
            return false;
        }
        Locator firstEmail = new Locator("INBOX_EMAIL_FIRST", new By.ByXPath("(" + inboxXpath + ")[1]"));
        if (!driverTools.tryClickWhenPresent(firstEmail, 10)) {
            driverTools.switchBackFromIframe();
            return false;
        }
        driverTools.switchBackFromIframe();
        driverTools.waitForElementToPresence(getMailIframeLocator(), 30);
        return true;
    }

    protected String emailTypeLabel(String inboxXpath) {
        return inboxXpath.contains("e-voucher") ? "E-voucher" : "Order-receipt";
    }

    protected boolean verifyEmailBodyContainsOrderId(String orderId) {
        driverTools.switchToIframe(driverTools.waitForElementToPresence(getMailIframeLocator()).orElseThrow());
        Locator orderIdInMail = new Locator("ORDER_ID_IN_MAIL",
                new By.ByXPath("(//*[contains(text(), '#" + orderId + "') or contains(text(), '" + orderId + "')])[last()]"));
        boolean found = driverTools.waitForElementToAppear(orderIdInMail, 15).isPresent();
        logger.log("Order ID " + orderId + " found in email body: " + found);
        driverTools.switchBackFromIframe();
        return found;
    }

    public HashMap<String, Object> iterateToFindEmail(String orderID, ArrayList<String> passengerNames) throws InterruptedException {
        int iteration = 6;
        for (int i=0; i<iteration; i++){
            logger.log("METHOD NAME: IterateToFindEmail");
            Optional<String> xpath = Optional.ofNullable(findTheRightEmail(orderID, passengerNames));
            if (xpath.isPresent()) {
                logger.log("XPATH: " + xpath.orElse("kosong"));
                HashMap<String, Object> inboxContent = readInbox(passengerNames, xpath.orElseThrow());
                return inboxContent;
            }
            Thread.sleep(5000);
            refreshInbox();
        }
        return null;
    }

    public void refreshInbox(){
        driverTools.switchBackFromIframe();
        driverTools.click(REFRESH_INBOX);
        driverTools.waitForElementToPresence(INBOX_IFRAME, 15);
    }

    public String findTheRightEmail(String orderID, ArrayList<String> passengerNames) {
        driverTools.switchToIframe(driverTools.element(INBOX_IFRAME).orElseThrow());
        logger.log("Switched to INBOX_IFRAME");
        Optional<List<WebElement>> elements = Optional.of(driverTools.elements(EMAIL_CANDIDATE).orElseThrow());
        logger.log("No. of matching elements: " + elements.get().size());
        for (int i = 1; i <= elements.get().size(); i++) {
            logger.log("METHOD NAME: find the right email");
            String emailCandidateXpath = "(//div[contains(text(), 'E-tiket')])[" + i + "]";
            Locator EMAIL_CANDIDATE = new Locator("EMAIL_CANDIDATE", new By.ByXPath(emailCandidateXpath));
            driverTools.click(EMAIL_CANDIDATE);
            driverTools.switchBackFromIframe();
            if (isCorrectEmail(orderID)) {
                driverTools.switchBackFromIframe();
                logger.log(emailCandidateXpath);
                return emailCandidateXpath;
            }
            driverTools.switchToIframe(driverTools.element(INBOX_IFRAME).orElseThrow());
            logger.log("Switched to INBOX_IFRAME");
        }
        driverTools.switchBackFromIframe();
        return null;
    }

    public boolean isCorrectEmail(String orderID){
        logger.log("METHOD NAME: isCorrectEmail");
        driverTools.switchToIframe(driverTools.element(getMailIframeLocator()).orElseThrow());
        logger.log("Switched to MAIL_IFRAME");
        Locator ORDER_ID = new Locator("ORDER_ID", new By.ByXPath("//*[contains(text(), '#"+ orderID+"')]"));
        String mailOrderID = driverTools.getText(ORDER_ID).orElse("").replace("#","");
        logger.log("OrderID in isCorrectEmail: " + mailOrderID);
        driverTools.switchBackFromIframe();
        return mailOrderID.equals(orderID);
    }

    public void openInbox(String xpath){
        logger.log("METHOD NAME: openInbox");
        driverTools.switchToIframe(driverTools.element(INBOX_IFRAME).orElseThrow());
        logger.log("Switched to INBOX_IFRAME");
        Locator MAIL_WITH_TICKET = new Locator("MAIL_WITH_TICKET", new By.ByXPath(xpath));
        driverTools.waitForElementToAppear(MAIL_WITH_TICKET, 20).orElseThrow();
        driverTools.click(MAIL_WITH_TICKET);
        driverTools.switchBackFromIframe();
    }

    public HashMap<String, Object> readInbox(ArrayList<String> passengerNames, String xpath){
        logger.log("METHOD NAME: readInbox");
        HashMap<String, Object> voucherEmail = new HashMap<>();
        openInbox(xpath);
        driverTools.switchToIframe(driverTools.element(getMailIframeLocator()).orElseThrow());
        logger.log("Switched to MAIL_IFRAME");
        Locator ORDER_ID = new Locator("ORDER_ID", new By.ByXPath(xpath));
        voucherEmail.put("orderID", getOrderID());
        voucherEmail.put("departureStation", getDepartureStation());
        voucherEmail.put("arrivalStation", getArrivalStation());
        voucherEmail.put("departureDate", getDepartureDate());
        voucherEmail.put("allPassengerNamesExist", checkPassengerNames(passengerNames));
        driverTools.switchBackFromIframe();
        logger.log("From email: " + voucherEmail);
        return voucherEmail;
    }

    public String getOrderID() {
        String orderId = driverTools.getText(ORDER_ID).orElseThrow();
        orderId = orderId.replace("#", "");
        return orderId;
    }

    public String getDepartureStation() {
        String departureStation = driverTools.getText(DEPARTURE_STATION).orElseThrow();
        departureStation = departureStation.toLowerCase();
        return departureStation;
    }

    public String getArrivalStation() {
        String arrivalStation = driverTools.getText(ARRIVAL_STATION).orElseThrow();
        arrivalStation = arrivalStation.toLowerCase();
        return arrivalStation;
    }

    public String getDepartureDate(){
        String departureDate = driverTools.getText(DEPARTURE_DATE).orElseThrow();
        departureDate = departureDate.toLowerCase();
        departureDate = departureDate.split(", ")[1];
        return departureDate;
    }

    public boolean checkPassengerNames(ArrayList<String> passengers){
        boolean passengersExist = false;
        for (int i= 0; i<passengers.size(); i++) {
            passengersExist = false;
            String name = passengers.get(i).split(" ")[1];
            String temp = name.substring(1);
            name = name.toUpperCase();
            name = name.charAt(0) + temp;
            logger.log("//*[contains(text(), '"+ passengers.get(i)+"')]");
            Locator passengerName = new Locator("DEPARTURE_DATE" + i, new By.ByXPath("//*[contains(text(), '"+ name+"')]"));
            passengersExist = driverTools.isElementDisplayed(passengerName);
        }
        return  passengersExist;
    }
}