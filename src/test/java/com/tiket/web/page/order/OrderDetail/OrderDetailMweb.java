package com.tiket.web.page.order.OrderDetail;

import com.tiket.appium.Swipe;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Locator;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class OrderDetailMweb<T extends WebDriver> extends OrderDetailWeb<T> {

    public OrderDetailMweb(Session<T> session) {
        super(session);
    }

    private final static Locator ORDER_ID_LOCATOR = new Locator("ORDER_ID_LOCATOR", new By.ByXPath("//div[contains(@class,'styles_truncated_text')]//span[contains(@class,'Text_size_b3')]"));
    private final static Locator GIFT_CARD_ORDER_ID_LOCATOR = new Locator("GIFT_CARD_ORDER_ID_LOCATOR", new By.ByXPath("//div[contains(@class,'styles_truncated_text')]//span[contains(@class,'Text_size_b3') and contains(normalize-space(),'Order ID:')]"));
    private final static Locator GIFT_CARD_RECEIPT_WRAPPER = new Locator("GIFT_CARD_RECEIPT_WRAPPER", new By.ByXPath("//div[contains(@class,'ManageOrder_action_buttons_wrapper')]//span[normalize-space()='Receipt']/ancestor::div[contains(@class,'ManageOrder_action_buttons_wrapper')]"));
    private final static Locator GIFT_CARD_NAME_LOCATOR = new Locator("GIFT_CARD_NAME_LOCATOR", new By.ByXPath("//div[contains(@class,'ProductSummary_text_wrapper')]//p[contains(@class,'Text_size_b2')]"));
    private final static Locator GIFT_CARD_RECIPIENT_LOCATOR = new Locator("GIFT_CARD_RECIPIENT_LOCATOR", new By.ByXPath("//div[contains(@class,'ProductSummary_text_wrapper')]//p[contains(@class,'Text_size_b3')]"));
    private final static Locator GIFT_CARD_SENDER_LOCATOR = new Locator("GIFT_CARD_SENDER_LOCATOR", new By.ByXPath("//div[contains(@class,'SectionItem_detail_content_container')][.//p[normalize-space()='Gift Card Sender']]//span[contains(@class,'Text_size_b2')]"));
    private final static Locator GIFT_CARD_DELIVERY_DATETIME_LOCATOR = new Locator("GIFT_CARD_DELIVERY_DATETIME_LOCATOR", new By.ByXPath("//div[contains(@class,'SectionItem_detail_content_container')][.//p[normalize-space()='Delivery Date & Time']]//span[contains(@class,'Text_size_b2')]"));
    private final static Locator CLOSE_MODAL_BUTTON = new Locator("CLOSE_MODAL_BUTTON", new By.ByXPath("//button[contains(@class, 'BottomSheet_close_button')]"));
    private static final Locator RELATED_TRAIN_SECTION = new Locator("RELATED_TRAIN_SECTION", By.xpath("//div[@data-testid='order-item-container']"));
    private static final Locator RETURN_WHOOSH_HEADING = new Locator("RETURN_WHOOSH_HEADING", By.xpath("//div[contains(@class, 'DetailReservation_flight_title_wrapper')]//span[contains(text(), 'Return Train') or contains(text(), 'Kereta Pulang')]"));
    private static final Locator RETURN_WHOOSH_NAME = new Locator("RETURN_WHOOSH_NAME", By.xpath("(//span[contains(@class, 'Text_variant_highEmphasis') and contains(text(), 'Whoosh')])"));
    private static final Locator RETURN_DEPARTURE_STATION = new Locator("RETURN_DEPARTURE_STATION", By.xpath("(//div[contains(@class, 'DetailReservation_flight_subtitle')])[1]"));
    private static final Locator RETURN_DESTINATION_STATION = new Locator("RETURN_DESTINATION_STATION", By.xpath("(//div[contains(@class, 'DetailReservation_flight_subtitle')])[2]"));
    private final static Locator REFUND_MANAGE_ORDER_BUTTON = new Locator("REFUND_MANAGE_ORDER_BUTTON", new By.ByXPath("//div[contains(@class,'ManageOrder')]//span[text()='Refund'] | //div[contains(@class,'ActionButton_action_title')]//span[text()='Refund']"));
    private final static Locator CONVENIENCE_FEE_SPAN = new Locator("CONVENIENCE_FEE_SPAN", By.xpath("//div[contains(@class, 'BottomSheet_body')]//span[contains(text(), 'Convenience fee') or contains(text(), 'Biaya platform')]"));
    private final static Locator BOOK_AGAIN_OR_REFUND_BUTTON = new Locator("BOOK_AGAIN_OR_REFUND_BUTTON", By.xpath("//div[contains(@class, 'PaymentFooter')]//button[contains(@class, 'Button_variant_primary')]"));
    private static final Locator DRIVE = new Locator("DRIVE", new By.ByXPath("//*[@resource-id='android:id/title' and @text='Open with Drive']"));
    private static final Locator JUST_ONCE = new Locator("JUST_ONCE", new By.ByXPath("//*[contains(@resource-id, 'button_once')]"));
    private static final Locator ALLOW = new Locator("ALLOW", new By.ByXPath("//*[@type='XCUIElementTypeStaticText' and @label='Allow']"));
    private static final Locator OPEN = new Locator("OPEN", new By.ByXPath("//*[@text='Open']"));
    private static final Locator DOWNLOADED_FILE = new Locator("DOWNLOADED_FILE", By.xpath("//*[contains(@text, 'Receipt') and contains(@text, '.pdf')]"));

    @Override
    public String getOrderId() {
        driverTools.dummyWait(3);
        WebElement element = driverTools.waitForElementToAppear(ORDER_ID_LOCATOR).orElseThrow();
        logger.log(element.getText());
        return driverTools.getText(element).replaceAll("[^\\d]", "");
    }

    @Override
    public void closeModal() {
        driverTools.click(CLOSE_MODAL_BUTTON);
    }

    @Override
    public String verifyContactNameAtReservation() {
        List<WebElement> detailElements = driverTools.elements(CONTACT_DETAIL_RESERVATION_BUTTON).get();
        if (detailElements.size() > 1) {
            driverTools.click(detailElements.get(1));
        } else {
            driverTools.click(detailElements.get(0));
        }

        driverTools.waitForElementToBeClickable(CLOSE_MODAL_BUTTON);
        List<WebElement> dataElements = driverTools.waitForElementsToAppear(CONTACT_DETAIL_RESERVATION_DATA).get();

        return driverTools.getText(dataElements.get(0));
    }

    @Override
    public String verifyContactEmailAtReservation() {
        List<WebElement> dataElements = driverTools.waitForElementsToAppear(CONTACT_DETAIL_RESERVATION_DATA).get();
        String contactEmail = driverTools.getText(dataElements.get(2));
        driverTools.waitForElementToPresence(CLOSE_MODAL_BUTTON).ifPresent(driverTools::click);
        if (!driverTools.isElementDisplayed(ORDER_ID_LOCATOR)) {
            driverTools.waitForElementToPresence(CLOSE_MODAL_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
        }
        return contactEmail;
    }

    @Override
    public boolean isConvenienceFeeExist() {
        driverTools.waitForElementToPresence(CONVENIENCE_FEE_SPAN);
        return !driver.findElements(CONVENIENCE_FEE_SPAN.by()).isEmpty();
    }

    @Override
    public String getOrderIdAt() {
        WebElement element = driverTools.waitForElementToAppear(ORDER_ID_LOCATOR).orElseThrow();
        logger.log(element.getText());
        return driverTools.getText(element);
    }

    @Override
    public void checkReturnTrainDetails(String returnWhoosh, String destinationStation, String originStation) {
        driverTools.waitForElementToAppear(RELATED_TRAIN_SECTION);
        driverTools.click(RELATED_TRAIN_SECTION);

        driverTools.waitForElementToAppear(RETURN_WHOOSH_HEADING, 10);
        driverTools.waitForElementToAppear(RETURN_WHOOSH_NAME);
        String RetWhooshOnOrderDetailPage = driverTools.getText(RETURN_WHOOSH_NAME).orElseThrow();
        String originStationOnODP = driverTools.getText(RETURN_DEPARTURE_STATION).orElseThrow();
        String destinationStationOnODP = driverTools.getText(RETURN_DESTINATION_STATION).orElseThrow();
        assertThat("Departure whoosh does not match", RetWhooshOnOrderDetailPage, containsString(returnWhoosh));
        assertThat("Origin station does not match", originStationOnODP, containsString(originStation));
        assertThat("Destination station does not match", destinationStationOnODP, containsString(destinationStation));
    }

    public List<String> verifyAdultName(int adultCount, int infantCount) {
        driverTools.waitForElementToAppear(PASSENGER_DETAIL_BTN);
        driverTools.click(PASSENGER_DETAIL_BTN);
        List<String> actualAdults = new ArrayList<>();
        for (int index = 1; index <= adultCount; index++) {
            Locator adult = getName(index);
            driverTools.scrollIntoWebViewMWeb(adult);
            String name = driverTools.getText(adult).orElseThrow();
            logger.log("Actual Adult : " + name);
            actualAdults.add(name);
        }
        return actualAdults;
    }

    @Override
    public List<String> verifyInfantName(int adultCount, int infantCount) {
        List<String> actualInfants = new ArrayList<>();
        for (int index = adultCount + 1; index <= adultCount + infantCount; index++) {
            Locator infant = getName(index);
            driverTools.scrollIntoWebViewMWeb(infant);
            String name = driverTools.getText(infant).orElseThrow();
            logger.log("Actual Infant : " + name);
            actualInfants.add(name);
        }
        return actualInfants;
    }

    @Override
    public List<String> verifyPassengerIDs(int adultCount, int infantCount) {
        List<String> passengerIds = new ArrayList<>();
        for (int i = 1; i <= adultCount + infantCount; i++) {
            Locator ids = getIDs(i);
            driverTools.waitForElementToAppear(ids);
            String id = driverTools.getText(ids).orElseThrow();
            logger.log("Actual Passenger : " + id);
            passengerIds.add(id);
        }
        return passengerIds;
    }

    private Locator getName(int index) {
        String xpath = String.format("(//div[contains(@class, 'VPassenger_lable_passenger')]//span[contains(@class, 'Text_variant_highEmphasis') and contains(@class, 'Text_weight_bold')])[%d]", index);
        return new Locator("ADULT_NAME_" + index, By.xpath(xpath));
    }

    private Locator getIDs(int index) {
        String xpath = String.format("(//span[contains(@class, 'VPassenger_id_number_value')])[%d]", index);
        return new Locator("PASSENGER_ID_" + index, By.xpath(xpath));
    }

    @Override
    public String getVerticalTotalPrice() {
        driverTools.waitForElementToAppear(ORDER_DETAIL_WRAPPER, 10);
        WebElement element = driverTools.scrollUntilElementFound(TOTAL_PRICE, 600, 2, 10).orElseThrow();
        return driverTools.getText(element);
    }

    @Override
    public boolean verify100pRefundPriceDetail() {
        driverTools.swipeUp(4);
        clickManageOrder();
        driverTools.waitForElementsToAppear(PRICE_DETAIL_FLIGHT_LOCATOR);
        driverTools.click(PRICE_DETAIL_FLIGHT_LOCATOR);
        boolean hundredPRefund = driverTools.waitForElementToAppear(HUNDREDREFUND_PRICE_BREAKDOWN).isPresent();
        driverTools.click(CLOSE_MODAL_BUTTON);
        return hundredPRefund;
    }

    public boolean verifyPageModule() {
        driverTools.swipeToElement(PAGE_MODULE_SECTION, Swipe.UP, 15);
        return driverTools.waitForElementToAppear(PAGE_MODULE_SECTION, 10).isPresent();
    }

    @Override
    public boolean verifyManageOrderContainer() {
        driverTools.swipeToElement(MANAGE_ORDER_BUTTONS, Swipe.UP, 15);
        boolean eTicketButton = driverTools.waitForElementToAppear(E_TICKET_BUTTON).isPresent();

        boolean receiptButton = driverTools.waitForElementToAppear(RECEIPT_BUTTON).isPresent();

        return eTicketButton && receiptButton;
    }

    @Override
    public String getGiftCardOrderId() {
        driverTools.waitForElementToPresence(GIFT_CARD_ORDER_ID_LOCATOR);
        WebElement element = driverTools.waitForElementToAppear(GIFT_CARD_ORDER_ID_LOCATOR).orElseThrow();
        String text = driverTools.getText(element);
        return text.replaceAll("[^\\d]", "");
    }

    @Override
    public boolean isGiftCardReceiptButtonDisplayed() {
        return driverTools.waitForElementToPresence(GIFT_CARD_RECEIPT_WRAPPER).isPresent();
    }

    @Override
    public boolean isGiftCardNameDisplayed(String giftCardName) {
        WebElement element = driverTools.waitForElementToAppear(GIFT_CARD_NAME_LOCATOR).orElseThrow();
        String actual = driverTools.getText(element);
        return actual.equals(giftCardName);
    }

    @Override
    public boolean isGiftCardSenderDisplayed(String senderName) {
        WebElement element = driverTools.waitForElementToAppear(GIFT_CARD_SENDER_LOCATOR).orElseThrow();
        String actual = driverTools.getText(element);
        return actual.equals(senderName);
    }

    @Override
    public boolean isGiftCardRecipientDisplayed(String recipientName) {
        WebElement element = driverTools.waitForElementToAppear(GIFT_CARD_RECIPIENT_LOCATOR).orElseThrow();
        String actual = driverTools.getText(element);
        return actual.contains(recipientName);
    }

    @Override
    public String getGiftCardDeliveryDateTime() {
        WebElement element = driverTools.waitForElementToAppear(GIFT_CARD_DELIVERY_DATETIME_LOCATOR).orElseThrow();
        String fullText = driverTools.getText(element);
        String[] parts = fullText.split("•");
        return parts[0].trim();
    }

    @Override
    public void refundButtonInweb() {
        driverTools.swipeToElement(REFUND_MANAGE_ORDER_BUTTON, Swipe.UP, 15);
        driverTools.clickWithJavascriptExecutor(REFUND_MANAGE_ORDER_BUTTON);
    }

    @Override
    public void clickBookAgainOrRefund() {
        driverTools.waitForElementToPresence(BOOK_AGAIN_OR_REFUND_BUTTON);
        driverTools.click(BOOK_AGAIN_OR_REFUND_BUTTON);
    }

    @Override
    public void openETicket() {
        driverTools.scrollIntoWebView(E_TICKET_BUTTON);
        driverTools.waitForElementToAppear(E_TICKET_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(E_TICKET_BUTTON);
        if (Configs.PLATFORM == Platform.AMWEB) {
            driverTools.waitForElementToPresence(getETicketElementMweb("English"), 10).ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.switchToNative();
            if (driverTools.isElementDisplayed(DRIVE)) {
                driverTools.click(DRIVE);
                driverTools.waitForElementToPresence(JUST_ONCE).ifPresent(driverTools::click);
            }
            driverTools.waitForElementToAppear(OPEN, 15).ifPresent(driverTools::click);
            driverTools.waitForElementToPresence(DRIVE, 10).ifPresent(driverTools::click);
            driverTools.waitForElementToPresence(JUST_ONCE, 10).ifPresent(driverTools::click);
            driverTools.waitForWebToLoad();
            driverTools.switchToWebView();
        } else if (Configs.PLATFORM == Platform.IMWEB) {
            driverTools.waitForElementToAppear(getETicketElementMweb("English"), 10);
            driverTools.clickWithJavascriptExecutor(getETicketElementMweb("English"));
            driverTools.waitForElementToPresence(ALLOW, 10).ifPresent(driverTools::click);

        }
    }

    private Locator getETicketElementMweb(String language) {
        return new Locator("ETICKET_LANGUAGE", By.xpath(String.format("//div[contains(@class,'ChooseETicketLangPopup_choose_lang_contents') and .//span[text()='%s']]", language)));
    }

    @Override
    public void verifyCurrencyOnReceipt(String expectedCurrency) {
        if (Configs.PLATFORM == Platform.IMWEB) {
            driverTools.switchToNative();
            Locator receiptCurrencyLocator = getReceiptCurrencyLocatorImweb(expectedCurrency);

            boolean isCurrencyVisible = driverTools.waitForElementToAppear(receiptCurrencyLocator, 10).isPresent();
            logger.log("Currency " + expectedCurrency + " visibility on receipt: " + isCurrencyVisible);
            Assertion.assertThat("Currency " + expectedCurrency + " should be visible on receipt", isCurrencyVisible, is(true));

            logger.log("Switching back to webview context");
        } else if (Configs.PLATFORM == Platform.AMWEB) {
            driverTools.switchToNative();
            Locator receiptCurrencyLocator = getReceiptCurrencyLocator(expectedCurrency);
            boolean isCurrencyVisible = driverTools.waitForElementToAppear(receiptCurrencyLocator, 10).isPresent();
            logger.log("Currency " + expectedCurrency + " visibility on receipt: " + isCurrencyVisible);
            Assertion.assertThat("Currency " + expectedCurrency + " should be visible on receipt", isCurrencyVisible, is(true));

            logger.log("Switching back to webview context");
            driverTools.switchToWebView();
        }

    }

    private Locator getReceiptCurrencyLocator(String currency) {
        String dynamicXpath = "//android.view.ViewGroup[contains(@content-desc,'" + currency + " ')]";
        return new Locator("RECEIPT_CURRENCY_" + currency, AppiumBy.xpath(dynamicXpath));
    }

    private Locator getReceiptCurrencyLocatorImweb(String currency) {
        String dynamicXpath = "//*[@type='XCUIElementTypeImage' and contains(@label, '" + currency + "')]";
        return new Locator("RECEIPT_CURRENCY_" + currency, AppiumBy.xpath(dynamicXpath));
    }

    @Override
    public void verifyOrderIDOnReceipt(String expectedOrderID) {
        if (Configs.PLATFORM == Platform.IMWEB) {
            String pdfContent = getPDFContentFile();
            // Remove all whitespace/newlines from PDF content to handle split text
            String normalizedPdfContent = pdfContent.replaceAll("\\s+", "");
            Assertion.assertThat("Receipt Order ID should match expected", normalizedPdfContent, containsString(expectedOrderID));

        } else if (Configs.PLATFORM == Platform.AMWEB) {
            driverTools.switchToNative();

            Locator receiptOrderIdLocator = getReceiptOrderIdLocator(expectedOrderID);
            String receiptOrderIdText = driverTools.getAttribute(receiptOrderIdLocator.by(), "content-desc");
            logger.log("Receipt Order ID Text: " + receiptOrderIdText);
            Assertion.assertThat("Receipt Order ID should match expected", receiptOrderIdText, is(containsString(expectedOrderID)));

            logger.log("Switching back to webview context");
            driverTools.switchToWebView();

        }
    }

    private Locator getReceiptOrderIdLocator(String orderID) {
        String dynamicXpath = "//android.view.ViewGroup[contains(@content-desc,'Order ID #" + orderID + "')]";
        return new Locator("RECEIPT_ORDER_ID_" + orderID, AppiumBy.xpath(dynamicXpath));
    }

    @Override
    public void openReceipt() {
        if (Configs.PLATFORM == Platform.IMWEB) {
            driverTools.waitForElementToPresence(ALLOW).ifPresent(driverTools::click);
        } else if (Configs.PLATFORM == Platform.AMWEB) {
            driverTools.switchToNative();
            if (driverTools.isElementDisplayed(DRIVE)) {
                driverTools.click(DRIVE);
                driverTools.waitForElementToPresence(JUST_ONCE).ifPresent(driverTools::click);
            }
            driverTools.waitForElementToAppear(OPEN, 15);
            driverTools.click(OPEN);

            if (driverTools.isElementDisplayed(DOWNLOADED_FILE)) {
                driverTools.click(DOWNLOADED_FILE);
            }

            if (driverTools.isElementDisplayed(DRIVE)) {
                driverTools.click(DRIVE);
                driverTools.waitForElementToPresence(JUST_ONCE).ifPresent(driverTools::click);
            }
            driverTools.switchToWebView();
        }
    }

    @Override
    public String switchTabAndgetURL() {
        driverTools.switchToNewTab();
        String url = driverTools.getCurrentURL();
        logger.log("Current URL: " + url);
        return url;
    }

    @Override
    public void clickCancelOrderButton() {
        driverTools.waitForElementToAppear(ORDER_DETAIL_WRAPPER).orElseThrow();
        driverTools.scrollUntilElementFound(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON, 600, 2, 8);
        driverTools.waitForElementToAppear(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON, 20).orElseThrow();
        driverTools.scrollIntoWebView(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON);
        driverTools.clickWithJavascriptExecutor(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON);
        driverTools.waitForElementToPresence(CANCEL_ORDER_CONFIRMATION).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CANCEL_ORDER_CONFIRMATION);
        driverTools.click(CANCEL_ORDER_CONFIRMATION_MODAL);
        isCanceledOrderBannerVisible(15);
    }
}
