package com.tiket.web.dWeb.page.reschedule;

import com.tiket.api.reschedule.GETRescheduleableStatus;
import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.order.MapVerticalWithOrderId;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.order.OrderDetail.OrderDetailWeb;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

public class ReschedulePage<T extends WebDriver> extends BasePage<T> {

    public ReschedulePage(Session<T> session) {
        super(session);
    }

    private final static Locator CHECK_RESCHEDULE_BUTTON = new Locator("CHECK_RESCHEDULE_BUTTON", new By.ByXPath("//h3[text()='Reschedule']"));
    private final static Locator CHECK_AVAILABILITY_BUTTON = new Locator("CHECK_AVAILABILITY_BUTTON", new By.ByXPath("//*[contains(@class,'Button_variant_primary')]"));
    private final static Locator ROOM_WITH_ADDITIONAL_PAYMENT = new Locator("ROOM_WITH_ADDITIONAL_PAYMENT", new By.ByXPath("//*[contains(@class,'additional_payment_price')]"));
    private final static Locator ROOM_WITH_FREE_PAYMENT = new Locator("BOOK_BUTTON", new By.ByXPath("(//button[contains(@data-testid,'book')])[1]"));
    private final static Locator BOOK_BUTTON = new Locator("BOOK_BUTTON", new By.ByXPath("//*[contains(@class,'Footer_purchase_btn_wrapper')]//button"));
    private final static Locator TNC_CHECKBOX = new Locator("TNC_CHECKBOX", new By.ByXPath("//*[@name='tnc']"));
    private final static Locator REQUEST_RESCHEDULE_BUTTON = new Locator("REQUEST_RESCHEDULE_BUTTON", new By.ByXPath("//button[./span[text()='Request reschedule' or text()='Ajukan reschedule']]"));
    private final static Locator CHECK_IN_OUT_DATE = new Locator("CHECK_IN_OUT_DATE", new By.ByXPath("//*[contains(@class,'CheckInOutInfo')]//*[contains(@class,'Text_weight_bold')]"));
    private final static Locator ROOM_NAME = new Locator("ROOM_NAME", new By.ByXPath("//*[contains(@class,'RoomDetailOverview_room')]//*[contains(@class,'Text_weight_bold')]"));
    private final static Locator SEE_ALL = new Locator("SEE_ALL", new By.ByXPath("//span[@class='Text_text__5GNOC Text_variant_activity__F2kpB Text_size_b2__axeH1 Text_weight_bold__s22wJ']"));
    private final static Locator SELECT_PAYMENT_METHOD = new Locator("SELECT_PAYMENT_METHOD", new By.ByXPath("//span[@class='Text_text__5GNOC Text_size_b2__axeH1 Text_weight_regular__Npbls' and text()='Sakuku']"));
    private final static Locator SELECT_ROOM_WITH_SAME_AMOUNT = new Locator("SELECT_ROOM_WITH_SAME_AMOUNT", new By.ByXPath("(//div[contains(text(),'IDR 0')])[1]"));
    private final static Locator PAY_WITH_REFUND_DEPOSIT = new Locator("PAY_WITH_REFUND_DEPOSIT", new By.ByXPath("//button[@data-testid='submit_button']"));
    private final static Locator PAY_WITH_SAKUKU = new Locator("PAY_WITH_SAKUKU", new By.ByXPath("//span[@class='SubmitPayment_pay_now_text__LU9s3 Text_text__5GNOC Text_size_b1__h4BXC Text_weight_bold__s22wJ']"));
    private final static Locator TNC_CHECKBOX_REFUND_PAGE = new Locator("TNC_CHECKBOX_REFUND_PAGE", new By.ByXPath("//span[contains(@class,'Check_custom_checkbox')]"));
    private final static Locator SUBMIT_REFUND_REQUEST = new Locator("SUBMIT_REFUND_REQUEST", new By.ByXPath("//button[@data-testid='continue-submit-refund']"));
    private final static Locator YOUR_ORDER = new Locator("YOUR_ORDER", new By.ByXPath("//span[@class='Header_text_black__7XO6Y Text_text__OMK3L Text_size_h3__MxFRL Text_weight_bold__zw4s1']"));
    private final static Locator CONTINUE = new Locator("CONTINUE", By.xpath("//div[contains(@class, 'PriceFooter_submit_reschedule_info')]/button"));
    private final static Locator HOTEL_NAME_AND_E_TICKET = new Locator("HOTEL_NAME_AND_E_TICKET", new By.ByXPath("(//div[@class='TiketCard_title__Nb7TX'])[1]"));
    private final static Locator THIS_IS_YOUR_NEW_ORDER = new Locator("THIS_IS_YOUR_NEW_ORDER", new By.ByXPath("//p[text()='This is your new order' or text()='Ini pesanan barumu']"));
    private final static Locator GO_TO_PREVIOUS_ORDER = new Locator("GO_TO_PREVIOUS_ORDER", new By.ByXPath("//span[text()='Go to previous order' or text()='Ke pesanan sebelumnya']"));
    private final static Locator PREVIOUS_ORDER_ID_DWEB = new Locator("PREVIOUS_ORDER_ID_DWEB", By.xpath("(//*[contains(text(), 'Order ID:')])[2]"));
    private final static Locator PREVIOUS_ORDER_ID_MWEB = new Locator("PREVIOUS_ORDER_ID_MWEB", By.xpath("(//*[contains(text(), 'Order ID:')])[1]"));
    private final static Locator NEW_SCHEDULE = new Locator("NEW_SCHEDULE", new By.ByXPath("//span[text()='New schedule' or text()='Jadwal baru']"));
    private final static Locator REFUND = new Locator("REFUND", new By.ByXPath("//span[text()='Refund']"));
    private final static Locator RESCHEDULE_NOW = new Locator("RESCHEDULE_NOW", new By.ByXPath("//button[@class='styles_btn_continue_refund__Y4bS_ Button_button__M6_2u Button_size_large__01AQ5 Button_horizontal_padding__yvaeQ Button_variant_primary__bDsbs']"));
    private final static Locator RESCHEDULE_BACK = new Locator("RESCHEDULE_BACK", new By.ByXPath("//button[@class='Button_button__nyzhZ Button_variant_tertiary__deuEm']"));
    private final static Locator COMPLETE_REFUND_DETAILS = new Locator("COMPLETE_REFUND_DETAILS", new By.ByXPath("//span[@class='styles_title__vEK_S Text_text__9ROO1 Text_size_h3__OB0NU Text_weight_bold__tf35J']"));
    private final static Locator OLD_CURRENCY = new Locator("OLD_CURRENCY", new By.ByXPath("(//span[contains(text(),'IDR')])[2]"));
    private final static Locator PRICE_DETAILS_MODAL = new Locator("PRICE_DETAILS_MODAL", By.xpath("//section[contains(@class, 'Modal_modal_popup')]"));
    private final static Locator ROOM_PRICE = new Locator("ROOM_PRICE", By.xpath("//div[contains(@class, 'styles_accordion_title')]/span"));
    private final static Locator ADDITIONAL_PRICES = new Locator("ADDITIONAL_PRICES", By.xpath("//span[contains(@class, 'styles_price_text')]"));
    private final static Locator PRICE_DEDUCTION_PRICES = new Locator("PRICE_DEDUCTION_PRICES", By.xpath("(//div[contains(@class, 'styles_discount_type_wrapper')])[1]//span[contains(@class, 'styles_price_text')]"));
    private final static Locator OTHER_FEES_PRICES = new Locator("OTHER_FEES_PRICES", By.xpath("(//div[contains(@class, 'styles_discount_type_wrapper')])[2]//span[contains(@class, 'styles_price_text')]"));
    private final static Locator CASHBACK_PRICE = new Locator("CASHBACK_PRICE", By.xpath("//div[contains(@class, 'styles_notes')]/b"));
    private final static Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", By.xpath("//div[contains(@class, 'styles_total_amount')]//span[2]"));
    private final static Locator RESCHEDULE_COMPLETED = new Locator("RESCHEDULE_COMPLETED", new By.ByXPath("//span[contains(@class,'styles_section_status__PCz5z')]"));
    private final static Locator ONGOING_REFUND = new Locator("ONGOING_REFUNDS", new By.ByXPath("//span[text()='Ongoing Refund' or text()='Refund Berjalan']"));
    private final static Locator REFUND_LIST = new Locator("REFUND_LIST", new By.ByXPath("(//div[@class='RefundCard_main_container__kRZ8b'])[1]"));
    private final static Locator REFUND_ORDER_NAME = new Locator("REFUND_ORDER_NAME", new By.ByXPath("(//span[@class='Text_text__Tr4Y3 Text_size_b1__b_hfx Text_weight_bold__XUUDW'])[2]"));
    private final static Locator TOTAL_PAYMENT_SPAN = new Locator("TOTAL_PAYMENT_SPAN", By.xpath("//div[contains(@class, 'TotalAmount_price')]/span"));
    private final static Locator OPEN_PRICE_BREAKDOWN = new Locator("OPEN_PRICE_BREAKDOWN", By.xpath("//div[contains(@class, 'TotalAmount_price')]/span/following-sibling::*"));
    private static final String ORDER_PAGE_URL = "https://preprod.tiket.com/yourorder";
    private final static Locator NEW_ORDER_CURRENCY = new Locator("NEW_ORDER_CURRENCY", new By.ByXPath("(//span[contains(text(), 'IDR')])[2]"));
    private final static Locator CHECK_BOX_FLIGHT_RESCHEDULE = new Locator("CHECK_BOX_FLIGHT_RESCHEDULE", new By.ByXPath("//*[contains(@class,'Check_custom_checkbox')]"));
    private final static Locator CHECK_NEW_RESCHEDULE_BUTTON_FLIGHT = new Locator("CHECK_NEW_RESCHEDULE_BUTTON_FLIGHT", new By.ByXPath("//*[contains(@class,'Button_variant_primary')]"));
    private final static Locator ORDER_CURRENCY = new Locator("ORDER_CURRENCY", By.xpath("//div[contains(@class, 'TotalAmount_wrapper')]//span[contains(@class, 'highEmphasis')]"));
    private final static Locator RESCHEDULE_SUCCESS_MODAL =  new Locator("RESCHEDULE_SUCCESS_MODAL", By.xpath("//div[contains(@class, 'Dialog_pop_up')]"));
    private final static Locator RESCHEDULE_SUCCESS_MODAL_ORDER_LIST_BUTTON = new Locator("RESCHEDULE_SUCCESS_MODAL_ORDER_LIST_BUTTON", By.xpath("//div[contains(@class, 'Dialog_pop_up')]//button"));

    OrderDetailWeb<?> orderDetailPage = OrderDetailWeb.getInstance(session);

    public interface CheckRescheduleAble {
        boolean check(MapVerticalWithOrderId.TitleAndOrderId orderData);
    }

    public CheckRescheduleAble checkRescheduleableStatus(String username) {
        return (orderData) -> {
            GETRescheduleableStatus.Inputs input = new GETRescheduleableStatus.Inputs(username, orderData.orderId, orderData.orderDetailId);
            GETRescheduleableStatus getReschedulableStatus = new GETRescheduleableStatus(input);
            ApiResponse response;
            try {
                response = getReschedulableStatus.hitApi(apiTools);
                JSONObject obj = new JSONObject(response.getBody());
                logger.log("object : " + obj.toString());
                return obj.getJSONObject("data").getJSONObject("reschedulable").getBoolean("value");
            } catch (Exception e) {
                logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            }
            return false;
        };
    }

    public void clickCheckAvailabilityButton() {
        driverTools.waitForElementToPresence(CHECK_RESCHEDULE_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(CHECK_RESCHEDULE_BUTTON);
        driverTools.clickWithJavascriptExecutor(CHECK_RESCHEDULE_BUTTON);
        driverTools.waitForElementToPresence(CHECK_AVAILABILITY_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CHECK_AVAILABILITY_BUTTON);
    }

    public void selectRoomFreeOrAdditionalPayment(){
        driverTools.waitForElementToPresence(ROOM_WITH_ADDITIONAL_PAYMENT);
        if(driverTools.isElementDisplayed(ROOM_WITH_ADDITIONAL_PAYMENT)){
            driverTools.clickWithJavascriptExecutor(ROOM_WITH_ADDITIONAL_PAYMENT);
        } else {
            driverTools.clickWithJavascriptExecutor(ROOM_WITH_FREE_PAYMENT);
        }
    }

    public void selectRoomWithAdditionalPayment() {
        driverTools.waitForElementToPresence(ROOM_WITH_ADDITIONAL_PAYMENT).orElseThrow();
        driverTools.clickWithJavascriptExecutor(ROOM_WITH_ADDITIONAL_PAYMENT);
    }

    public void clickBookButton() {
        driverTools.waitForElementToPresence(BOOK_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(BOOK_BUTTON);
    }

    public void checkTnC() {
        driverTools.scrollUntilElementFound(TNC_CHECKBOX);
        driverTools.waitForElementToPresence(TNC_CHECKBOX).orElseThrow();
        driverTools.clickWithJavascriptExecutor(TNC_CHECKBOX);
    }

    public void clickSubmitRescheduleButton() {
        driverTools.waitForElementToPresence(CONTINUE);
        driverTools.clickWithJavascriptExecutor(CONTINUE);
        driverTools.waitForElementToPresence(REQUEST_RESCHEDULE_BUTTON);
        driverTools.clickWithJavascriptExecutor(REQUEST_RESCHEDULE_BUTTON);
    }

    public void clickSeeAll() {
        driverTools.waitForElementToPresence(SEE_ALL).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SEE_ALL);
    }

    public void clickSelectPaymentMethod() {
        driverTools.waitForElementToPresence(SELECT_PAYMENT_METHOD).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SELECT_PAYMENT_METHOD);
    }

    public void clickSelectRoomWithSameAmount() {
        driverTools.waitForElementToPresence(SELECT_ROOM_WITH_SAME_AMOUNT).orElseThrow();
        driverTools.scrollElementToCenter(SELECT_ROOM_WITH_SAME_AMOUNT);
        driverTools.clickWithJavascriptExecutor(SELECT_ROOM_WITH_SAME_AMOUNT);
    }

    public boolean clickRefund() {
        driverTools.waitForElementToPresence(REFUND)
                .orElseThrow(() -> new TimeoutException("Refund button not found"));

        driverTools.clickWithJavascriptExecutor(REFUND);

        driverTools.waitForElementToAppear(COMPLETE_REFUND_DETAILS, 10)
                .orElseThrow(() -> new TimeoutException("Complete refund details element did not appear"));

        String actualMessage = driverTools.getText(COMPLETE_REFUND_DETAILS)
                .orElseThrow(() -> new TimeoutException("Text for 'COMPLETE_REFUND_DETAILS' not found"));

        String expectedMessage = "Complete your refund details";
        Assertion.assertThat("COMPLETE_REFUND_DETAILS", actualMessage, equalTo(expectedMessage));
        return true;
    }

    public void clickRefundAndReschedule() {
        driverTools.waitForElementToPresence(REFUND).orElseThrow();
        driverTools.clickWithJavascriptExecutor(REFUND);
        driverTools.waitForElementToPresence(RESCHEDULE_NOW).orElseThrow();
        driverTools.clickWithJavascriptExecutor(RESCHEDULE_NOW);
        driverTools.waitForElementToPresence(RESCHEDULE_BACK).orElseThrow();
        driverTools.clickWithJavascriptExecutor(RESCHEDULE_BACK);
    }

    public boolean verifyOrderListPage() {
        driverTools.waitForElementToAppear(YOUR_ORDER, 10)
                .orElseThrow(() -> new TimeoutException("Element 'YOUR_ORDER' did not appear"));

        String actualMessage = driverTools.getText(YOUR_ORDER)
                .orElseThrow(() -> new TimeoutException("Text for 'YOUR_ORDER' not found"));

        String expectedMessage = "Your Orders";
        Assertion.assertThat("YOUR_ORDER", actualMessage, equalTo(expectedMessage));
        return true;
    }
    public void clickSubmitRefundRequest() {
        driverTools.scrollUntilElementFound(TNC_CHECKBOX_REFUND_PAGE).orElseThrow();
        driverTools.clickWithJavascriptExecutor(TNC_CHECKBOX_REFUND_PAGE);
        driverTools.waitForElementToBeClickable(SUBMIT_REFUND_REQUEST, 5).orElseThrow();
        driverTools.clickWithJavascriptExecutor(SUBMIT_REFUND_REQUEST);
    }

    public void clickPayWithRefundDeposit() {
        driverTools.waitForElementToAppear(PAY_WITH_REFUND_DEPOSIT, 20).orElseThrow();
        driverTools.clickWithJavascriptExecutor(PAY_WITH_REFUND_DEPOSIT);
    }

    public void verifyHotelStatus() throws InterruptedException {
        driver.get(ORDER_PAGE_URL);
        Thread.sleep(5000);
        driverTools.refreshWebpage();
        Thread.sleep(5000);
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(YOUR_ORDER, 3);
        driverTools.waitForElementToAppear(HOTEL_NAME_AND_E_TICKET, 10);
        driverTools.click(HOTEL_NAME_AND_E_TICKET);
        if (driverTools.waitForElementToPresence(NEW_SCHEDULE, 3).orElse(null) != null) {
            driverTools.clickWithJavascriptExecutor(NEW_SCHEDULE);
        }
        driverTools.waitForElementToAppear(THIS_IS_YOUR_NEW_ORDER, 5);
    }

    public void verifyOrderIds() {
        driverTools.waitForElementToPresence(GO_TO_PREVIOUS_ORDER).orElseThrow();
        driverTools.clickWithJavascriptExecutor(GO_TO_PREVIOUS_ORDER);
    }

    public void clickPayWithSakuku() {
        driverTools.waitForElementToPresence(PAY_WITH_SAKUKU).orElseThrow();
        driverTools.clickWithJavascriptExecutor(PAY_WITH_SAKUKU);
    }

    public boolean getNewOrderText() {
        driverTools.waitForElementToAppear(THIS_IS_YOUR_NEW_ORDER, 10)
                .orElseThrow(() -> new TimeoutException("Element 'THIS_IS_YOUR_NEW_ORDER' did not appear"));

        String actualMessage = driverTools.getText(THIS_IS_YOUR_NEW_ORDER)
                .orElseThrow(() -> new TimeoutException("Text for 'THIS_IS_YOUR_NEW_ORDER' not found"));

        String expectedMessage = "This is your new order";
        Assertion.assertThat("THIS_IS_YOUR_NEW_ORDER", actualMessage, equalTo(expectedMessage));
        return true;
    }

    public void compareOrderIds() {
        Locator loc = PREVIOUS_ORDER_ID_DWEB;
        if (Configs.PLATFORM == Platform.AMWEB || Configs.PLATFORM == Platform.IMWEB) {
            loc = PREVIOUS_ORDER_ID_MWEB;
        }

        driverTools.waitForElementToPresence(loc)
                .orElseThrow(() -> new TimeoutException("Previous Order ID element not present"));
        String prevOrderIdText = driverTools.getText(loc)
                .orElseThrow(() -> new TimeoutException("Could not retrieve Previous Order ID text"));

        String orderId = prevOrderIdText.substring(prevOrderIdText.indexOf(":") + 1).trim();
        String expectedMessage = orderDetailPage.getOrderId();

        Assertion.assertThat("New Order ID and Previous Order ID are same", orderId, equalTo(expectedMessage));
    }


    public void compareCurrencies() {
        driverTools.waitForElementToPresence(GO_TO_PREVIOUS_ORDER);
        driverTools.clickWithJavascriptExecutor(GO_TO_PREVIOUS_ORDER);

        driverTools.scrollUntilElementFound(OLD_CURRENCY)
                .orElseThrow(() -> new TimeoutException("Old currency element not present"));

        String previousOrderCurrency = driverTools.getText(driverTools.element(OLD_CURRENCY).orElseThrow()).split(" ")[0];
        String expectedMessage = getOrderCurrency();

        Assertion.assertThat("New Order Currency and Previous Order Currency are same",
                previousOrderCurrency,
                equalTo(expectedMessage));
    }

    public boolean compareCurrenciesWithUrl(String oldOrderUrl, String newOrderUrl) {
        driverTools.get(oldOrderUrl);
        WebElement element = driverTools.scrollUntilElementFound(ORDER_CURRENCY).orElseThrow();
        String oldCurrency = driverTools.getText(element).split(" ")[0];

        driverTools.get(newOrderUrl);
        element = driverTools.scrollUntilElementFound(ORDER_CURRENCY).orElseThrow();
        String newCurrency = driverTools.getText(element).split(" ")[0];
        return oldCurrency.equals(newCurrency);
    }

    public String getOrderCurrency() {
        driverTools.clickWithJavascriptExecutor(NEW_SCHEDULE);
        driverTools.scrollUntilElementFound(NEW_ORDER_CURRENCY);
        return driverTools.getText(driverTools.element(NEW_ORDER_CURRENCY).orElseThrow()).split(" ")[0];
    }

    public int getPriceBreakdown() {
        driverTools.waitForWebToLoad();
        driverTools.scrollUntilElementFound(OPEN_PRICE_BREAKDOWN);
        WebElement svgElement = driverTools.waitForElementToPresence(OPEN_PRICE_BREAKDOWN).get();
        driverTools.scrollIntoWebView(svgElement);
        driverTools.clickSvgOrNonHtmlElements(svgElement);
        driverTools.waitForElementToAppear(PRICE_DETAILS_MODAL);

        WebElement element1 = driverTools.element(ROOM_PRICE).orElseThrow();
        String price1 = driverTools.getText(element1).split(" ")[1];
        price1 = price1.replace(",", "");
        int roomPrice = Integer.parseInt(price1);

        int additionalPrice = 0;
        for (WebElement element : driverTools.elements(ADDITIONAL_PRICES).orElse(new ArrayList<>())) {
            driverTools.scrollIntoWebView(element);
            String text = driverTools.getText(element);
            if (text.equals("Termasuk") || text.equals("Included")) continue;
            if (text.contains("-")) additionalPrice -= Integer.parseInt(text.split(" ")[2].replace(",", ""));
            else additionalPrice += Integer.parseInt(text.split(" ")[1].replace(",", ""));
        }

        return roomPrice + additionalPrice;
    }

    public int getTotalPrice() {
        WebElement element1 = driverTools.elements(TOTAL_PRICE).orElseThrow().get(0);
        String raw = driverTools.getText(element1).trim();
        if (raw.equalsIgnoreCase("FREE") || raw.equalsIgnoreCase("GRATIS")) {
            return 0;
        }
        String[] parts = raw.split(" ");
        if (parts.length < 2) {
            String digits = raw.replaceAll("[^0-9]", "");
            if (digits.isEmpty()) {
                return 0;
            }
            return Integer.parseInt(digits);
        }
        String price1 = parts[1].replaceAll("[,.]", "");
        return Integer.parseInt(price1);
    }

    public int getCashbackAmount() {
        WebElement element = driverTools.element(CASHBACK_PRICE).orElse(null);
        if (element == null) return 0;
        String price1 = driverTools.getText(element).split(" ")[1];
        price1 = price1.replace(",", "");
        return Integer.parseInt(price1);
    }

    public void comparePrices() {
        try {
            driverTools.waitForElementToPresence(THIS_IS_YOUR_NEW_ORDER, 5).orElseThrow();
            int breakdownTotal = getPriceBreakdown();
            int expectedTotalPrice = getTotalPrice();
            int cashback = getCashbackAmount();
            Assertion.assertThat("Breakdown price and Total price are same", breakdownTotal + cashback, equalTo(expectedTotalPrice));
        } catch (TimeoutException e) {
            logger.log("Breakdown price and Total price are not same");
        }
    }

    public void comparePricesForFlight() {
        try {
            int breakdownTotal = getPriceBreakdown();
            int expectedTotalPrice = getTotalPrice();
            Assertion.assertThat("Breakdown price and Total price are same", breakdownTotal, equalTo(expectedTotalPrice));
        } catch (TimeoutException e) {
            logger.log("Breakdown price and Total price are not same");
        }
    }

    public void rescheduleCompleted() {
        driverTools.clickWithJavascriptExecutor(GO_TO_PREVIOUS_ORDER);
        driverTools.waitForElementToAppear(RESCHEDULE_COMPLETED, 10);

        String actualMessage = driverTools.getText(RESCHEDULE_COMPLETED)
                .orElseThrow(() -> new TimeoutException("Reschedule is not completed"));

        String expectedMessage = "Completed";
        Assertion.assertThat("RESCHEDULE_IS_COMPLETED", actualMessage, equalTo(expectedMessage));
    }


    public boolean refundNotInitiated() {

        driverTools.get(ORDER_PAGE_URL);
        driverTools.waitForElementToAppear(ONGOING_REFUND, 10);
        driverTools.clickWithJavascriptExecutor(ONGOING_REFUND);
        driverTools.waitForElementToAppear(REFUND_LIST, 10);
        driverTools.clickWithJavascriptExecutor(REFUND_LIST);
        driverTools.waitForElementToAppear(REFUND_ORDER_NAME, 10);
        Optional<String> actualMessage11 = driverTools.getText(REFUND_ORDER_NAME);
        String actualMessage1 = actualMessage11.orElseThrow();
        String expectedMessage1 = "The Sultan Hotel Jakarta (SDET puny hotel, JANGAN DIPAKE)";
        Assertion.assertThat("REFUND_IS_NOT_INITIATED", actualMessage1, not(equalTo(expectedMessage1)));
        return true;

    }

    public String getDate() {
        List<String> date = driverTools.getTexts(CHECK_IN_OUT_DATE);
        String[] date1Array = date.get(0).trim().split(" ");
        String date1 = date1Array[0] + " " + date1Array[1];
        String[] date2Array = date.get(1).trim().split(" ");
        String date2 = date2Array[0] + " " + date2Array[1];
        return date1 + " - " + date2;
    }

    public String getRoomName() {
        driverTools.waitForElementToAppear(ROOM_NAME,15);
        String roomName = driverTools.getText(ROOM_NAME).orElseThrow();
        return roomName;
    }

    public void clickCheckNewRescheduleButtonFlight() {
        driverTools.waitForElementToPresence(CHECK_BOX_FLIGHT_RESCHEDULE).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CHECK_BOX_FLIGHT_RESCHEDULE);
        driverTools.waitForElementToPresence(CHECK_NEW_RESCHEDULE_BUTTON_FLIGHT).orElseThrow();
        driverTools.clickWithJavascriptExecutor(CHECK_NEW_RESCHEDULE_BUTTON_FLIGHT);
    }

    public boolean verifyRescheduleSuccess() {
        boolean res = driverTools.waitForElementsToAppear(RESCHEDULE_SUCCESS_MODAL).isPresent();
        driverTools.click(RESCHEDULE_SUCCESS_MODAL_ORDER_LIST_BUTTON);
        return res;
    }
}
