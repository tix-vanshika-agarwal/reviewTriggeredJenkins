package com.tiket.web.page.order.OrderDetail;

import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.web.page.common.eTicket.ETicketWeb;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Locator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class OrderDetailDweb<T extends WebDriver> extends OrderDetailWeb<T> {

    private String orderDetailWindowHandle;

    public OrderDetailDweb(Session<T> session) {
        super(session);
    }

    private final static Locator ORDER_ID_LOCATOR = new Locator("ORDER_ID_LOCATOR", new By.ByXPath("//p[contains(@class,'DetailReservation_order_id') or contains(@class,'styles_order_id')] | //span[contains (@class, 'styles_order_id')] | //*[contains(@class,'GenericReservationDetails_order_id_label')]"));
    private final static Locator GIFT_CARD_ORDER_ID_LOCATOR = new Locator("GIFT_CARD_ORDER_ID_LOCATOR", new By.ByXPath("//p[contains(@class,'GenericReservationDetails_order_id_label') and contains(normalize-space(.), 'Order ID:')]"));
    private final static Locator GIFT_CARD_RECEIPT_WRAPPER = new Locator("GIFT_CARD_RECEIPT_WRAPPER", new By.ByXPath("//*[contains(@class, 'ManageOrder_action_buttons_wrapper')]"));
    private final static Locator GIFT_CARD_NAME_LOCATOR = new Locator("GIFT_CARD_NAME_LOCATOR", new By.ByXPath("//div[contains(@class,'ProductSummary_text_wrapper')]//p[contains(@class,'Text_size_b2')]"));
    private final static Locator GIFT_CARD_RECIPIENT_LOCATOR = new Locator("GIFT_CARD_RECIPIENT_LOCATOR", new By.ByXPath("//div[contains(@class,'ProductSummary_text_wrapper')]//p[contains(@class,'Text_size_b3')]"));
    private final static Locator GIFT_CARD_SENDER_LOCATOR = new Locator("GIFT_CARD_SENDER_LOCATOR", new By.ByXPath("//div[contains(@class,'SectionItem_detail_content_container')][.//p[normalize-space()='Gift Card Sender']]//span[contains(@class,'Text_size_b2')]"));
    private final static Locator GIFT_CARD_DELIVERY_DATETIME_LOCATOR = new Locator("GIFT_CARD_DELIVERY_DATETIME_LOCATOR", new By.ByXPath("//div[contains(@class,'SectionItem_detail_content_container')][.//p[normalize-space()='Delivery Date & Time']]//span[contains(@class,'Text_size_b2')]"));
    private final static Locator CLOSE_MODAL_BUTTON = new Locator("CLOSE_MODAL_BUTTON", new By.ByXPath("//button[contains(@class,'modal_close')] | //button[contains(@class,'coachmark_skip_btn')]"));
    private final static Locator AT_ORDER_ID_LOCATOR = new Locator("AT_ORDER_ID_LOCATOR", new By.ByXPath("//div[contains(@class,'DetailReservation')]//span[contains(text(),'Order ID')]"));
    private static final Locator RETURN_TRAIN_DETAILS = new Locator("RETURN_TRAIN_DETAILS",By.xpath("//div[@data-testid='order-item-container'][.//div[text()='Return']]"));
    private static final Locator RETURN_WHOOSH_NAME = new Locator("RETURN_WHOOSH_NAME", By.xpath("(//span[contains(@class, 'Text_variant_highEmphasis') and contains(text(), 'Whoosh')])[1]"));
    private static final Locator CLOSE_BTN = new Locator("CLOSE_BTN", By.xpath("//button[@data-testid='modal-close-button']"));
    private static final Locator RETURN_DEPARTURE_STATION = new Locator("RETURN_DEPARTURE_STATION", By.xpath("(//div[contains(@class, 'DetailReservation_flight_subtitle')])[1]"));
    private static final Locator RETURN_DESTINATION_STATION = new Locator("RETURN_DESTINATION_STATION", By.xpath("(//div[contains(@class, 'DetailReservation_flight_subtitle')])[2]"));
    private final static Locator CONVENIENCE_FEE_SPAN = new Locator("CONVENIENCE_FEE_SPAN", By.xpath("//span[contains(text(), 'Convenience fee') or contains(text(), 'Biaya platform')]"));
    private final static Locator BOOK_AGAIN_OR_REFUND_BUTTON = new Locator("BOOK_AGAIN_OR_REFUND_BUTTON", By.xpath("//button[contains(@class, 'AlertInfo_action_button')]"));

    @Override
    public String getOrderId() {
        driverTools.waitForElementToPresence(ORDER_ID_LOCATOR);
        driverTools.scrollIntoWebView(ORDER_ID_LOCATOR);
        WebElement element = driverTools.waitForElementToAppear(ORDER_ID_LOCATOR).orElseThrow();
        return driverTools.getText(element).replaceAll("[^\\d]", "");
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
        return driverTools.getText(element);
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
        WebElement element = driverTools.waitForElementToAppear(AT_ORDER_ID_LOCATOR).orElseThrow();
        logger.log(element.getText());
        return driverTools.getText(element);
    }

    @Override
    public void checkReturnTrainDetails(String returnWhoosh, String destinationStation, String originStation) {
        driverTools.waitForElementToAppear(RETURN_TRAIN_DETAILS);
        driverTools.clickWithJavascriptExecutor(RETURN_TRAIN_DETAILS);
        driverTools.waitForElementToDisappear(RETURN_TRAIN_DETAILS);
        driverTools.waitForElementToAppear(RETURN_WHOOSH_NAME, 5);
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
        int passengers = adultCount + infantCount;
        List<String> actualAdults = new ArrayList<>();

        for (int index = passengers + 1; index <= passengers + adultCount; index++) {
            Locator adult = getName(index);
            driverTools.scrollToElement(adult);
            String name = driverTools.getText(adult).orElseThrow();
            logger.log("Actual Adult : " + name);
            actualAdults.add(name);
        }
        return actualAdults;
    }

    @Override
    public List<String> verifyInfantName(int adultCount, int infantCount) {
        int passengers = 2 * adultCount + infantCount;
        List<String> actualInfants = new ArrayList<>();

        for (int index = passengers + 1; index <= passengers + infantCount; index++) {
            Locator infant = getName(index);
            driverTools.scrollToElement(infant);
            String name = driverTools.getText(infant).orElseThrow();
            logger.log("Actual Infant : " + name);
            actualInfants.add(name);
        }
        return actualInfants;
    }

    @Override
    public List<String> verifyPassengerIDs(int adultCount, int infantCount) {
        List<String> passengerIds = new ArrayList<>();
        int passengers = adultCount + infantCount;
        for (int i = passengers + 1; i <= passengers + adultCount + infantCount; i++) {
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
        driverTools.waitForElementToAppear(ORDER_DETAIL_WRAPPER).orElseThrow();
        WebElement element = driverTools.scrollUntilElementFound(TOTAL_PRICE, 600, 2, 10).orElseThrow();
        return driverTools.getText(element);
    }

    @Override
    public boolean verify100pRefundPriceDetail() {
        WebElement orderWrapper = driverTools.waitForElementToAppear(ORDER_DETAIL_WRAPPER).orElseThrow();
        driverTools.scrollInsideElement(orderWrapper);
        clickManageOrder();
        driverTools.waitForElementToBeClickable(PRICE_DETAIL_FLIGHT_LOCATOR);
        driverTools.click(PRICE_DETAIL_FLIGHT_LOCATOR);
        boolean hundredPRefund = driverTools.waitForElementToAppear(HUNDREDREFUND_PRICE_BREAKDOWN).isPresent();
        driverTools.click(CLOSE_MODAL_BUTTON);
        return hundredPRefund;
    }

    public boolean verifyPageModule() {
        driverTools.waitForElementToAppear(ORDER_DETAIL_WRAPPER, 5).orElseThrow();
        driverTools.scrollToBottomOfPage();
        driverTools.scrollIntoWebView(PAGE_MODULE_SECTION);
        return driverTools.waitForElementToAppear(PAGE_MODULE_SECTION, 10).isPresent();
    }

    @Override
    public boolean verifyManageOrderContainer() {
        driverTools.scrollIntoWebView(MANAGE_ORDER_BUTTONS);

        boolean eTicketButton = driverTools.waitForElementToAppear(E_TICKET_BUTTON).isPresent();

        boolean receiptButton = driverTools.waitForElementToAppear(RECEIPT_BUTTON).isPresent();

        return eTicketButton && receiptButton;
    }

    @Override
    public void refundButtonInweb() {
        //no need for now in dweb
    }

    @Override
    public void clickBookAgainOrRefund() {
        driverTools.waitForElementToPresence(BOOK_AGAIN_OR_REFUND_BUTTON);
        driverTools.click(BOOK_AGAIN_OR_REFUND_BUTTON);
    }

    @Override
    public void openETicket() {
        logger.log("=== Starting openETicket ===");

        orderDetailWindowHandle = driver.getWindowHandle();
        logger.log("Current window handle: " + orderDetailWindowHandle);
        logger.log("Window handles before click: " + driver.getWindowHandles());
        logger.log("Current URL before e-ticket: " + driver.getCurrentUrl());

        int handleCountBeforeClick = driver.getWindowHandles().size();

        driverTools.waitForElementToPresence(E_TICKET_BUTTON, 20);
        logger.log("E-ticket button is present");

        driverTools.scrollIntoWebView(E_TICKET_BUTTON);
        logger.log("Scrolled to e-ticket button");

        driverTools.clickWithJavascriptExecutor(E_TICKET_BUTTON);
        logger.log("Clicked e-ticket button");

        Locator englishOption = getETicketElement("English");

        if (driverTools.waitForElementToAppear(englishOption).isPresent()) {
            logger.log("English option found using getETicketElement");
            driverTools.clickWithJavascriptExecutor(englishOption);
            logger.log("Clicked English option");
        } else {
            logger.log("English option not found using getETicketElement, trying languageOption");
            driverTools.clickWithJavascriptExecutor(languageOption("English"));
            logger.log("Clicked English using languageOption");
        }

        logger.log("Window handles after English click: " + driver.getWindowHandles());
        logger.log("Current URL after English click: " + driver.getCurrentUrl());

        try {
            logger.log("Waiting for new e-ticket PDF tab...");

            driverTools.waitForNewTabAndSwitchToLatest(handleCountBeforeClick);

            logger.log("Switched to new tab");
            logger.log("Current URL after switching: " + driver.getCurrentUrl());

            driverTools.waitForWebToLoad();

            logger.log("PDF tab loaded");
        } catch (TimeoutException e) {
            logger.log("Eticket PDF tab did not open in time: " + e.getMessage());
            logger.log("Window handles at failure: " + driver.getWindowHandles());
            logger.log("Current URL at failure: " + driver.getCurrentUrl());
        }

        logger.log("=== Finished openETicket ===");
    }
    private Locator getETicketElement(String language) {
        return new Locator(
            "ETICKET_LANGUAGE",
                By.xpath(String.format(
                        "//div[contains(@class, 'List_list_content')][.//p[normalize-space()='%s']]",
                        language)));
    }
    public static Locator languageOption(String language) {
        return new Locator("LANGUAGE_OPTION", By.xpath(String.format("//*[contains(@class,'EVoucherContent_language_list')]//*[text()='%s']", language)));
    }

    @Override
    public void verifyCurrencyOnReceipt(String expectedCurrency) {
        String pdfContent = getPDFContentFile();
        String normalizedPdfContent = pdfContent.replaceAll("\\s+", "");
        logger.log("Receipt currency check for: " + expectedCurrency);
        Assertion.assertThat("Currency " + expectedCurrency + " should be visible on receipt",
                normalizedPdfContent, containsString(expectedCurrency));
    }

    @Override
    public void verifyOrderIDOnReceipt(String expectedOrderID) {
        String pdfContent = getPDFContentFile();
        String normalizedPdfContent = pdfContent.replaceAll("\\s+", "");
        logger.log("Receipt order ID check for: " + expectedOrderID);
        Assertion.assertThat("Receipt Order ID should match expected",
                normalizedPdfContent, containsString(expectedOrderID));
    }

    @Override
    public void openReceipt() {
        if (orderDetailWindowHandle == null) {
            orderDetailWindowHandle = driver.getWindowHandle();
        }

        // Receipt may already have been triggered by clickReceiptButton(); give the PDF tab time to appear.
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(d -> d.getWindowHandles().size() > 1);
        } catch (TimeoutException ignored) {
            // No new tab yet; openReceipt will click the receipt button below.
        }

        if (driver.getWindowHandles().size() > 1) {
            switchToPdfTabIfPresent();
            driverTools.waitForWebToLoad();
            return;
        }

        int handleCountBeforeClick = driver.getWindowHandles().size();
        driverTools.scrollIntoWebView(RECEIPT_BUTTON);
        driverTools.waitForElementToPresence(RECEIPT_BUTTON, 20);
        driverTools.clickWithJavascriptExecutor(RECEIPT_BUTTON);
        try {
            driverTools.waitForNewTabAndSwitchToLatest(handleCountBeforeClick);
            driverTools.waitForWebToLoad();
        } catch (TimeoutException e) {
            logger.log("Receipt PDF tab did not open in time: " + e.getMessage());
        }
    }

    @Override
    public String getPDFContentFile() {
        try {
            String pdfUrl = driverTools.getCurrentURL();
            logger.log("Reading PDF from URL: " + pdfUrl);
            ETicketWeb<?> eTicketWeb = ETicketWeb.getInstance(session);
            return eTicketWeb.pdfContent(pdfUrl);
        } finally {
            closePdfTabAndReturnToOrderDetail();
        }
    }

    private void switchToPdfTabIfPresent() {
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(orderDetailWindowHandle)) {
                driver.switchTo().window(handle);
                return;
            }
        }
    }

    private void closePdfTabAndReturnToOrderDetail() {
        if (orderDetailWindowHandle == null) {
            return;
        }
        String currentHandle = driver.getWindowHandle();
        if (!currentHandle.equals(orderDetailWindowHandle) && driver.getWindowHandles().size() > 1) {
            driverTools.closeCurrentTab();
        }
        if (driver.getWindowHandles().contains(orderDetailWindowHandle)) {
            driverTools.switchToWindow(orderDetailWindowHandle);
        }
    }

    @Override
    public String switchTabAndgetURL() {
        String original = driver.getWindowHandle();
        String url = null;
        for (String tab : driver.getWindowHandles()) {
            if (!tab.equals(original)) {
                driver.switchTo().window(tab);
                url = driver.getCurrentUrl();
                driver.switchTo().window(original);
                break;
            }
        }
        return url;
    }

    @Override
    public void clickCancelOrderButton() {
        driverTools.waitForElementToAppear(ORDER_DETAIL_WRAPPER).orElseThrow();
        driverTools.scrollUntilElementFound(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON, 600, 2, 8);
        driverTools.waitForElementToAppear(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON, 20).orElseThrow();
        driverTools.scrollIntoWebView(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON);
        driverTools.clickWithJavascriptExecutor(REFUND_RESCHEDULE_CANCEL_ORDER_BUTTON);
        driverTools.waitForElementToAppear(CANCEL_ORDER_BUTTON_POPUP, 15).orElseThrow();
        driverTools.scrollIntoWebView(CANCEL_ORDER_BUTTON_POPUP);
        driverTools.clickWithJavascriptExecutor(CANCEL_ORDER_BUTTON_POPUP);
        driverTools.waitForElementToAppear(CANCEL_ORDER_BUTTON_POPUP_CONFIRMATION, 15).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CANCEL_ORDER_BUTTON_POPUP_CONFIRMATION);
        isCanceledOrderBannerVisible(15);
    }
}
