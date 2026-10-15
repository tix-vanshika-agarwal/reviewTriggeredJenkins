package com.tiket.page.payment;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.*;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tiket.page.payment.PaymentPageAndroid.LOADING_STATE;
import static com.tiket.page.order.orderlist.OrderListPageIOS.YOUR_ORDER_HEADER;
import static org.hamcrest.Matchers.*;

public class PaymentPageIOS <T extends WebDriver> extends PaymentPage<T> {

    public PaymentPageIOS(Session<T> session) {
        super(session);
    }

    // webview locators
    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", AppiumBy.xpath("//*[@class='title']//span"));
    private static final Locator CHECKIN_DATE_AND_NIGHT_DURATION = new Locator("CHECKIN_DATE_AND_NIGHT_DURATION", AppiumBy.xpath("//*[contains(@class,'desc')]//span"));
    private static final Locator SELECT_PAYMENT_METHOD_WEBVIEW = new Locator("SELECT_PAYMENT_METHOD", AppiumBy.xpath("//*[contains(@class,'styles_title')]/following-sibling::*"));
    private static final Locator SELECT_PAYMENT_METHOD_WEBVIEW_NEXTJS = new Locator("SELECT_PAYMENT_METHOD_NEXTJS", AppiumBy.xpath("//p[contains(@class,'styles_title') and (contains(text(),'Metode Pembayaran') or contains(text(),'Payment Method'))]/following-sibling::button"));
    private static final Locator SUBMIT_PAYMENT_BUTTON = new Locator("SUBMIT_PAYMENT_BUTTON", AppiumBy.xpath("//*[contains(@class,'SubmitPayment')]"));
    private static final Locator GOPAY_SUCCESS = new Locator("GOPAY_SUCCESS", AppiumBy.cssSelector("[class='success']"));
    private static final Locator CREDIT_CARD_PAYMENT_METHOD =  new Locator("CREDIT_CARD_PAYMENT_METHOD", AppiumBy.xpath("//*[contains(text(),'Kredit/') or contains(text(),'Credit/')]"));
    private static final Locator CREDIT_CARD_PAYMENT_METHOD_NEXTJS = new Locator("CREDIT_CARD_PAYMENT_METHOD_NEXTJS", AppiumBy.xpath("//div[contains(@class,'styles_groups_title')]/p[contains(text(),'Kartu Kredit') or contains(text(),'Credit Card') or contains(text(),'Debit Card')]"));
    private static final Locator CARD_NUMBER = new Locator("CARD_NUMBER", AppiumBy.cssSelector("[name='ccNumber']"));
    private static final Locator EXPIRY = new Locator("EXPIRY", AppiumBy.cssSelector("[name='ccExp']"));
    private static final Locator CVV = new Locator("CVV", AppiumBy.cssSelector("[name='cardCvvNumber']"));
    private static final Locator CARD_NAME = new Locator("CARD_NAME", AppiumBy.cssSelector("[name='ccHolder']"));
    private static final Locator SAVE_CARD_DETAIL = new Locator("SAVE_CARD_DETAIL", AppiumBy.xpath("//*[contains(@type,'submit')]"));
    private static final Locator INSTALLMENTS = new Locator("INSTALLMENTS", AppiumBy.xpath("//*[contains(@id,'installment-radio')]"));
    private static final Locator SERVICE_CHARGE = new Locator("SERVICE_CHARGE", AppiumBy.xpath("(//*[contains(@id,'installment-radio') and @value='true']/preceding::*[contains(@class,'installment-notes')]/*[contains(@class,'emphasis hover')])[last()]"));
    private static final Locator PRICE_DETAIL = new Locator("PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'PriceSummary_final_price__')]//following-sibling::*"));
    private static final Locator SERVICE_CHARGE_PRICE_DETAIL = new Locator("SERVICE_CHARGE_PRICE_DETAIL", AppiumBy.xpath("//*[contains(text(),'Biaya layanan') or contains(text(),'Service charge')]//following-sibling::*"));
    private static final Locator PRICE_LOCATOR = new Locator("PRICE_LOCATOR", AppiumBy.xpath("//li[contains(@class,'PriceDetails_price_group')]//*[contains(text(),',') or contains(text(),'.')]"));
    private static final Locator TOTAL_PRICE_FROM_PRICE_DETAIL = new Locator("TOTAL_PRICE_FROM_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'PriceDetails_total_payment')]//*[contains(@class,'text') and contains(text(),',') or contains(text(),'.')]"));
    private static final Locator CLOSE_BTN_PRICE_DETAIL = new Locator("CLOSE_BTPROMO_CODE_TEXTN_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'close_button')]"));
    private static final Locator PROMO_DISCOUNT_PRICE = new Locator("PROMO_DISCOUNT_PRICE", AppiumBy.xpath("//*[@data-testid='btnApplyUnapplyPromo']//*[contains(@class,'ApplyPromoText_promo_text')] | //*[contains(@class,'PromoDisplay_discount_wrapper__')]//*[contains(@class,'PromoDisplay_text_wrapper')]//p"));
    private static final Locator TOTAL_PRICE_SECOND_PAGE = new Locator("TOTAL_PRICE_SECOND_PAGE", AppiumBy.xpath("//*[contains(@class,'number_box')]//*[contains(text(),'IDR')]"));
    private static final Locator CONTINUE_PAYMENT_SECOND_PAGE = new Locator("CONTINUE_PAYMENT_SECOND_PAGE", AppiumBy.xpath("//*[@class='tix-button v4 mobile button waves-effect waves-default large']"));
    private static final Locator CONTINUE_PAYMENT_SECOND_PAGE_NATIVE = new Locator("CONTINUE_PAYMENT_SECOND_PAGE_NATIVE", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Bayar') or contains(@name,'Pay')]"));
    private static final Locator RECOMMENDED_PAYMENT_METHOD_WRAPPER = new Locator("RECOMMENDED_PAYMENT_METHOD_WRAPPER", AppiumBy.xpath("//*[contains(@class,'recommended_method_wrapper')]"));
    private static final Locator RECOMMENDED_PAYMENT_METHOD_CHECBOX = new Locator("RECOMMENDED_PAYMENT_METHOD_CHECBOX", AppiumBy.xpath("//*[contains(@class,'recommended_method_wrapper')]//*[@type='radio']"));
    public static final Locator REFUND_AGREEMENT_CHECKB0X = new Locator("PROPOSE_RESCHEDULE_BUTTON", AppiumBy.xpath("//*[@class='tc-box']"));
    public static final Locator CONTINUE_REFUNDS = new Locator("CONTINUE_REFUNDS", AppiumBy.xpath("//*[@class='tix-button v4 mobile button waves-effect waves-default block']"));
    private static final Locator PRICE_DETAIL_CLOSE_BUTTON_WEBVIEW = new Locator("PRICE_DETAIL_CLOSE_BUTTON_WEBVIEW", AppiumBy.xpath("//button/*[1]"));
    private static final Locator PRICE_DETAIL_ARROW_BUTTON_WEBVIEW = new Locator("PRICE_DETAIL_ARROW_BUTTON_WEBVIEW", AppiumBy.xpath("//*[contains(@class, 'final_price')]/*[2]"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", AppiumBy.xpath("//*[contains(@class,'PriceSummary_final_price')]/span"));
    private static final Locator PAY_WITH_TIKET_POINT_TOGGLE = new Locator("PAY_WITH_TIKET_POINT_TOGGLE",AppiumBy.xpath("//*[contains(@class,'toggle_small')]"));
    private static final Locator CARD_NUMBER_V3 = new Locator("CARD_NUMBER_V3", AppiumBy.xpath("//*[contains(@name,'cardNumber')]"));
    private static final Locator EXPIRY_V3 = new Locator("EXPIRY_V3", AppiumBy.xpath("//*[contains(@data-rules,'expiredCard')]"));
    private static final Locator CVV_V3 = new Locator("CVV_V3", AppiumBy.xpath("//*[contains(@name,'cardCvvNumber')]"));
    private static final Locator CARD_NAME_V3 = new Locator("CARD_NAME_V3", AppiumBy.xpath("//*[contains(@name,'cardHolderName')]"));
    private static final Locator CC_LOADING_INDICATOR = new Locator("CC_LOADING_INDICATOR", AppiumBy.xpath("//svg[contains(@class,'loading')]"));

    // native locators
    private static final Locator CARD_NUMBER_NATIVE = new Locator("CARD_NUMBER", AppiumBy.xpath("//*[contains(@label,'Nomor') or contains(@label,'number')]"));
    private static final Locator EXPIRY_NATIVE = new Locator("EXPIRY_NATIVE", AppiumBy.xpath("//*[contains(@label,'MM')]"));
    private static final Locator CVV_NATIVE = new Locator("CVV_NATIVE", AppiumBy.xpath("//*[contains(@label,'CVV')]"));
    private static final Locator CARD_NAME_NATIVE = new Locator("CARD_NAME_NATIVE", AppiumBy.xpath("//*[contains(@label,'Nama') or contains(@label,'name')]"));
    private static final Locator POSTAL_CODE_NATIVE = new Locator("POSTAL_CODE_NATIVE", AppiumBy.xpath("//*[contains(@label,'Kode pos') or contains(@label,'Postal code')]"));
    private static final Locator BILLING_ADDRESS_NATIVE = new Locator("BILLING_ADDRESS_NATIVE", AppiumBy.xpath("//*[contains(@label,'Alamat penagihan kartu') or contains(@label,'Billing address')]"));
    public static final Locator COMPLETE_STRIPE = new Locator("COMPLETE_STRIPE", AppiumBy.xpath("//XCUIElementTypeButton[@name='COMPLETE']"));
    private static final Locator OTP_2C2P_INPUT = new Locator("OTP_2C2P_INPUT", AppiumBy.xpath("//*[@name='challengeDataEntry']"));
    private static final Locator SUBMIT_2C2P = new Locator("SUBMIT_2C2P", AppiumBy.xpath("//*[@class='acs-challenge-form-actions']//*[@class='acs-challenge-btn proceed']"));
    public static final Locator SUBMIT_CC_BUTTON_NATIVE = new Locator("SUBMIT_CC_BUTTON_NATIVE", AppiumBy.xpath("//XCUIElementTypeButton[@label='Pay Now' or @label='Bayar Sekarang']"));
    public static final Locator CONTINUE_REFUNDS_NATIVE = new Locator("CONTINUE_REFUNDS_NATIVE", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name, 'Refund')]"));
    public static final Locator REFUND_AGREEMENT_CHECKB0X_NATIVE = new Locator("PROPOSE_RESCHEDULE_BUTTON_NATIVE", AppiumBy.xpath("//XCUIElementTypeOther[@name='Reschedule']/XCUIElementTypeSwitch"));
    public static final Locator ORDER_ID = new Locator("ORDER_ID", AppiumBy.xpath("//*[contains(@name, \"Order ID\")]"));
    public static final Locator NATIVE_TOTAL_PRICE = new Locator("NATIVE_TOTAL_PRICE", AppiumBy.xpath("//*[contains(@name, 'IDR')]"));
    private static final Locator PAYLATER_LIMIT_POPUP = new Locator("PAYLATER_LIMIT_POPUP", AppiumBy.xpath("//*[@class='b3 limit-info mobile high-emphasis hover tix-font v4']"));
    private static final Locator SELECT_PAYLATER_WEBVIEW = new Locator("SELECT_PAYLATER_WEBVIEW", AppiumBy.xpath("(//*[@class='content-wrapper']//*[@class='b2 mobile high-emphasis bold hover tix-font v4'])[1]"));
    private static final Locator PAYLATER_LIMIT_WEBVIEW = new Locator("PAYLATER_LIMIT_WEBVIEW", AppiumBy.xpath("//*[@class='method-wrapper have-recommended have-installment selected']//p[@class='b3 mobile high-emphasis hover tix-font v4']"));
    private static final Locator OTP_FORM_POPUP = new Locator("OTP_FORM_POPUP", AppiumBy.xpath("(//*[@class='bs-content-body bs-content-padding']//*[@class='form-otp'])[1]"));
    private static final Locator DISCOUNT_AMOUNT_PROMO_CODE_SECTION = new Locator("DISCOUNT_AMOUNT_PROMO_CODE_SECTION", AppiumBy.xpath("//*[@class='b3 mobile high-emphasis benefit hover tix-font v4']"));
    private static final Locator CHOOSE_PAYMENT_METHOD_BUTTON = new Locator("CHOOSE_PAYMENT_METHOD_BUTTON", AppiumBy.xpath("//div[contains(@class, 'card_container')]//button"));
    private static final Locator CHOOSE_PAYMENT_METHOD_PARENT = new Locator("CHOOSE_PAYMENT_METHOD_PARENT", AppiumBy.xpath("//*[@class='bs-content-body bs-content-padding']"));
    private static final Locator CHANGE_PAYMENT_METHOD_BUTTON = new Locator("CHANGE_PAYMENT_METHOD_BUTTON", AppiumBy.xpath("//p[contains(@class, 'styles_change_button')]"));
    private static final Locator CHANGE_PAYMENT_METHOD_BUTTON_NEXTJS = new Locator("CHANGE_PAYMENT_METHOD_BUTTON_NEXTJS", AppiumBy.xpath("//p[contains(@class,'styles_title') and (contains(text(),'Metode Pembayaran') or contains(text(),'Payment Method'))]/following-sibling::p"));
    private static final Locator SEE_ORDER_LIST_BUTTON_2 = new Locator("SEE_ORDER_LIST_BUTTON_2", AppiumBy.xpath("//*[contains(text(), 'See Order List') or contains(text(), 'Lihat daftar pesanan') or contains(text(), 'See the order list')]"));
    private static final Locator CHOOSE_PAYMENT_METHOD_BUTTON_NATIVE = new Locator("CHOOSE_PAYMENT_METHOD_BUTTON_NATIVE", AppiumBy.xpath("//XCUIElementTypeStaticText[@value='Metode Pembayaran' or @value='Payment Method']/parent::*/following-sibling::*[1]"));
    private static final Locator DISCOUNT_AMOUNT_PROMO_CODE_SECTION_NATIVE = new Locator("DISCOUNT_AMOUNT_PROMO_CODE_SECTION_NATIVE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@value, 'You save') or contains(@value, 'Kamu hemat')]"));
    private static final Locator USE_PROMO_CODE_BUTTON_NATIVE = new Locator("USE_PROMO_CODE_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='Pakai' or @name='Apply']"));
    private static final Locator DISCOUNT_PERCENTAGE_NATIVE = new Locator("DISCOUNT_PERCENTAGE_NATIVE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@value,'%')]"));
    private static final Locator CROSSED_PRICE_BEFORE_NATIVE = new Locator("CROSSED_PRICE_BEFORE_NATIVE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@value,'%')]/parent::*[1]/preceding-sibling::*[1]//XCUIElementTypeStaticText"));
    private static final Locator CROSSED_PRICE_BEFORE_NATIVE_GK = new Locator("CROSSED_PRICE_BEFORE_NATIVE_GK", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@value,'%')]/parent::*[1]/preceding-sibling::XCUIElementTypeStaticText[1]"));
    private static final Locator CANT_COMBINE_PROMO_LABEL_NATIVE = new Locator("CANT_COMBINE_PROMO_LABEL_NATIVE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@value,\"tidak dapat\") or contains(@value,\"can't\")]"));
    private static final Locator PRICE_DETAIL_ARROW_BUTTON_NATIVE = new Locator("PRICE_DETAIL_ARROW_BUTTON_NATIVE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'IDR')]/parent::*/following::*[@type='XCUIElementTypeImage']"));
    private static final Locator DISCOUNT_AMOUNT_PRICE_DETAIL_NATIVE = new Locator("DISCOUNT_AMOUNT_PRICE_DETAIL_NATIVE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@value,'- IDR')]"));
    public static final Locator NATIVE_HOTEL_NAME = new Locator("NATIVE_HOTEL_NAME", AppiumBy.xpath("//XCUIElementTypeOther[@name='main']/XCUIElementTypeOther[3]//*[@type='XCUIElementTypeStaticText']"));
    public static final Locator NATIVE_CHECKIN_DATE = new Locator("NATIVE_CHECKIN_DATE", AppiumBy.xpath("//XCUIElementTypeOther[@name='main']/XCUIElementTypeOther[4]//*[@type='XCUIElementTypeStaticText'][1]"));
    public static final Locator NATIVE_NIGHT_DURATION = new Locator("NIGHT_DURATION", AppiumBy.xpath("//XCUIElementTypeOther[@name='main']/XCUIElementTypeOther[4]//*[@type='XCUIElementTypeStaticText'][3]"));
    public static final Locator NATIVE_BOOKING_DETAIL_ARROW_BUTTON = new Locator("NATIVE_BOOKING_DETAIL_ARROW_BUTTON", AppiumBy.xpath("//XCUIElementTypeOther[@name='main']/XCUIElementTypeImage[3]"));
    public static final Locator NATIVE_BOOKING_DETAIL_GUEST_NAME = new Locator("NATIVE_BOOKING_DETAIL_GUEST_NAME", AppiumBy.xpath("//XCUIElementTypeOther[@value='Order Kamu' or @value='Your Order'][1]/following-sibling::*[6]/XCUIElementTypeStaticText"));
    public static final Locator NATIVE_BACK_BUTTON = new Locator("NATIVE_BACK_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'back')]"));
    private static final Locator NATIVE_CHECK_OVO_PAY_DISABLED = new Locator("CHECK_OVO_PAY_DISABLED", AppiumBy.xpath("//XCUIElementTypeStaticText[@value='Log In untuk membayar dengan OVO' or @value='Log In to pay using OVO']"));
    private static final Locator APP_SCREEN = new Locator("APP_SCREEN", AppiumBy.xpath("//XCUIElementTypeApplication[contains(@name,'tiket.com')]"));
    private static final Locator VA_NUMBER = new Locator("VA_NUMBER", AppiumBy.xpath("//*[contains(@class,'AccountInfo_number_box')]//*[contains(@class,'size_h3')]"));
    private static final Locator SEE_ORDER_LIST_BUTTON_SECONDPAGE = new Locator("SEE_ORDER_LIST_BUTTON_SECONDPAGE", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label,'Lihat Daftar Pesanan') or contains(@label,'View Order List')]"));
    private static final Locator ADD_OVO_ACCOUNT = new Locator("ADD_OVO_ACCOUNT", AppiumBy.xpath("//*[@name='+ Add OVO Account' or @name='+ Tambahkan Akun OVO']"));
    private static final Locator LINKAGE_TO_OVO = new Locator("LINKAGE_TO_OVO", AppiumBy.xpath("//*[@name='Linkage to OVO' or @name='Hubungkan ke OVO']"));
    public static final Locator PAY_WITH_BALANCE = new Locator("PROPOSE_RESCHEDULE_BUTTON", AppiumBy.xpath("(//XCUIElementTypeOther[@name='Cashback']/following-sibling::*)[1]"));
    private static final Locator SUBMIT = new Locator("SUBMIT", AppiumBy.xpath("//*[@type='submit']"));
    private static final Locator SUBMIT_CC = new Locator("SUBMIT_CC", AppiumBy.xpath("//*[@class='tix-button v4 button-submit payment-submit mobile button waves-effect waves-default block large']"));
    private static final Locator PROMO_LABEL_ON_BUTTON_BAR = new Locator("PROMO_LABEL_ON_BUTTON_BAR", AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`label == 'Jaminan Harga Termurah' OR label == 'Lowest Price Guarantee'`][1]"));
    public static final Locator NATIVE_TOTAL_PRICE_ON_BUTTON_BAR = new Locator("NATIVE_TOTAL_PRICE_ON_BUTTON_BAR", AppiumBy.xpath("(//*[contains(@name, 'IDR')])[2]"));
    private static final Locator PAY_WITH_TIKET_POINT_OTP_CONFIRMATION = new Locator("PAY_WITH_TIKET_POINT_OTP_CONFIRMATION", AppiumBy.className("XCUIElementTypeTextField"));
    private static final Locator TOTAL_PRICE_CHEVRON = new Locator("TOTAL_PRICE_CHEVRON", AppiumBy.xpath("//XCUIElementTypeStaticText/following-sibling::XCUIElementTypeImage[last()]"));
    private static final Locator TRIP_TITLE = new Locator("TRIP_TITLE", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'→')]"));
    private static final Locator TRIP_HEADER = new Locator("TRIP_HEADER", AppiumBy.xpath("//XCUIElementTypeStaticText/preceding-sibling::XCUIElementTypeButton[not(contains(@label,'promo'))]"));

    // pay at hotel
    private static final Locator PAY_AT_HOTEL_ORDER_DETAIL_ARROW = new Locator("PAY_AT_HOTEL_ORDER_DETAIL_ARROW", AppiumBy.xpath("//XCUIElementTypeCell[2]/XCUIElementTypeButton"));
    private static final Locator PAY_AT_HOTEL_PRODUCT_NAME = new Locator("PAY_AT_HOTEL_PRODUCT_NAME", AppiumBy.xpath("//XCUIElementTypeCell[2]/XCUIElementTypeStaticText[3]"));
    private static final Locator PAY_AT_HOTEL_TOTAL_PRICE = new Locator("PAY_AT_HOTEL_TOTAL_PRICE", AppiumBy.xpath("//XCUIElementTypeCell[2]/XCUIElementTypeStaticText[2]"));
    private static final Locator PAY_AT_HOTEL_CHECKIN_DATE = new Locator("PAY_AT_HOTEL_CHECKIN_DATE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Check-in']/parent::*/XCUIElementTypeStaticText[2]"));
    private static final Locator PAY_AT_HOTEL_NIGHT_DURATION = new Locator("PAY_AT_HOTEL_NIGHT_DURATION", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Durasi' or @label='Duration']/parent::*/XCUIElementTypeStaticText[2]"));
    private static final Locator PAY_AT_HOTEL_TOTAL_ROOM = new Locator("PAY_AT_HOTEL_TOTAL_ROOM", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Kamar' or @label='Room']/parent::*/XCUIElementTypeStaticText[3]"));
    private static final Locator PAY_AT_HOTEL_TOTAL_GUEST = new Locator("PAY_AT_HOTEL_TOTAL_GUEST", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='Kamar' or @label='Room']/parent::*/XCUIElementTypeStaticText[5]"));
    private static final Locator PAY_AT_HOTEL_BUTTON = new Locator("PAY_AT_HOTEL_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@label='Bayar Di Hotel' or @label='Pay At Hotel']"));
    private static final Locator PAY_AT_HOTEL_CROSS_ORDER_DETAIL = new Locator("PAY_AT_HOTEL_CROSS_ORDER_DETAIL", AppiumBy.accessibilityId("leftBarButtonItem"));
    private static final Locator CANT_COMBINE_PROMO_LABEL = new Locator("CANT_COMBINE_PROMO_LABEL", AppiumBy.xpath("(//*[contains(@class,'TiketPoints_tiket_points')]//span[contains(@class,'text')])[1]"));
    private static final Locator REFUND_DEPOSIT_BUTTON = new Locator("REFUND_DEPOSIT_BUTTON", AppiumBy.iOSClassChain("**/*[`label CONTAINS 'Refund Deposit'`]"));
    private static final Locator PAGE_2C2P_PAYMENT = new Locator("PAGE_2C2P_PAYMENT", AppiumBy.xpath("//*[contains(@label,'2C2P')]"));
    private static final Locator OTP_2C2P_PAYMENT = new Locator("OTP_2C2P_PAYMENT", AppiumBy.className("XCUIElementTypeSecureTextField"));
    private static final Locator SUBMIT_2C2P_PAYMENT = new Locator("SUBMIT_2C2P_PAYMENT", AppiumBy.accessibilityId("Submit"));
    private static final Locator BACK_TO_MERCHANT_2C2P = new Locator("BACK_TO_MERCHANT_2C2P", AppiumBy.accessibilityId("Back to Merchant"));
    private static final Locator TIKET_POINTS = new Locator("TIKET_POINTS", AppiumBy.xpath("//*[contains(@class,'TiketPoints_tiket_points')]//*[contains(@class,'weight')]"));
    private static final Locator TOOGLE_TIKET_POINT = new Locator("TOOGLE_TIKET_POINT", AppiumBy.xpath("//*[contains(@class,'_toggle_small')]"));
    private static final Locator OTP_FEILD = new Locator("OTP_FEILD", AppiumBy.xpath("//*[contains(@class,'FormOtp_otp_input_')]"));
    private static final Locator OVO_PIN_INPUT = new Locator("OVO_PIN_INPUT", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'OVO PIN')]/following::XCUIElementTypeTextField[1]"));
    private static final Locator KEYBOARD_DONE_BUTTON = new Locator("KEYBOARD_DONE_BUTTON", AppiumBy.xpath("//XCUIElementTypeToolbar//XCUIElementTypeButton[@name='Done']"));
    private static final Locator OVO_PAY_BUTTON = new Locator("OVO_PIN_INPUT", AppiumBy.xpath("//XCUIElementTypeButton[@name='BAYAR' or @name='PAY']"));
    private static final Locator LOGIN_NOW = new Locator("LOGIN_NOW", AppiumBy.xpath("//XCUIElementTypeButton[@name='Log in now' or @name='Masuk sekarang']"));
    private static final Locator CAMERA_CONTINUE_BUTTON = new Locator("CAMERA_CONTINUE_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@name='buttonPrimary' or @label='Continue']"));
    private static final Locator CAMERA_OK_BUTTON = new Locator("CAMERA_OK_BUTTON", AppiumBy.xpath("//XCUIElementTypeButton[@label='OK' or @label='Oke' or @label='Allow']"));


    private boolean isOrderShow() {
        boolean orderId = driverTools.waitForElementToPresence(ORDER_ID, 45).isPresent();
        if (!orderId) {
            logger.log("Order ID is not displayed");
            driverTools.swipeDown(2);
            orderId = driverTools.waitForElementToPresence(ORDER_ID).isPresent();
        }
        return orderId;
    }

    @Override
    public void fillOvoPin(String ovoPin) {
        driverTools.waitForElementToPresence(OVO_PIN_INPUT);
        driverTools.sendKeysKeyboard(OVO_PIN_INPUT, ovoPin);

        // The keyboard covers BAYAR (y=792 vs keyboard from y=561), so dismiss it first
        driverTools.waitForElementToPresence(KEYBOARD_DONE_BUTTON, 3).ifPresent(driverTools::click);
        driverTools.click(OVO_PAY_BUTTON);
    }

    @Override
    public void checkCcOtp() {
        if (driverTools.waitForElementToAppear(PAGE_2C2P_PAYMENT).isPresent()) {
            logger.log("Redirect to the 2C2P Payment Gateway");
            driverTools.waitForElementToPresence(OTP_2C2P_PAYMENT).orElseThrow();
            driverTools.sendKeys(OTP_2C2P_PAYMENT, "123456");
            driverTools.hideKeyboard(1);
            driverTools.click(SUBMIT_2C2P_PAYMENT);
            driverTools.waitForElementToPresence(BACK_TO_MERCHANT_2C2P,5).ifPresent(driverTools::click);
        } else {
            logger.log("There is no redirection to the Payment Gateway");
        }
    }

    @Override
    public String checkOrderID() {
        driverTools.switchToNative();
        driverTools.waitForElementToDisappear(LOADING_STATE);
        isOrderShow();
        String orderID = driverTools.element(ORDER_ID).orElseThrow().getAttribute("value");
        logger.log("original order_id : " + orderID);

        // Removing non-numeric characters
        String numericOrderID = orderID.replaceAll("[^0-9]", "");
        logger.log("numeric order_id : " + numericOrderID);

        return numericOrderID;
    }

    @Override
    public void clickBackButton() {
        driverTools.click(NATIVE_BACK_BUTTON);
    }

    @Override
    public void clickRecommendedPaymentMethod() {
        driverTools.waitForElementToAppear(APP_SCREEN, 30);
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(RECOMMENDED_PAYMENT_METHOD_WRAPPER).orElseThrow();
        WebElement webElement = driverTools.element(RECOMMENDED_PAYMENT_METHOD_CHECBOX).orElseThrow();
        driverTools.click(webElement);
        driverTools.switchToNative();
    }

    @Override
    public void seeOrderList() {
        driverTools.switchToNative();

        if (driverTools.waitForElementToPresence(YOUR_ORDER_HEADER).isPresent()) {
            logger.log("Order list already opened, skipping to next step");
        } else {
            driverTools.switchToWebViewUrl("payment", 5);

            if (driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON).isPresent()) {
                logger.log("SEE_ORDER_LIST_BUTTON found on payment page");
                driverTools.clickOrFallbackToClickWithJavascriptExecutor(SEE_ORDER_LIST_BUTTON);
            } else if (driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON_WEBVIEW).isPresent()) {
                logger.log("SEE_ORDER_LIST_BUTTON_WEBVIEW found on payment page");
                driverTools.clickOrFallbackToClickWithJavascriptExecutor(SEE_ORDER_LIST_BUTTON_WEBVIEW);
            } else {
                logger.log("SEE_ORDER_LIST_BUTTON not found, checking my-refund page");

                driverTools.switchToWebViewUrl("my-refund");

                if (driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON_2).isPresent()) {
                    logger.log("SEE_ORDER_LIST_BUTTON_2 found on my-refund page");
                    driverTools.scrollIntoWebView(SEE_ORDER_LIST_BUTTON_2);
                    driverTools.clickOrFallbackToClickWithJavascriptExecutor(SEE_ORDER_LIST_BUTTON_2);
                } else {
                    logger.log("SEE_ORDER_LIST_BUTTON_2 not found on my-refund page, falling back to native See Order List button");
                    driverTools.switchToNative();
                    driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON_NATIVE_ANDROID_IOS).orElseThrow();
                    driverTools.clickOrFallbackToClickWithJavascriptExecutor(SEE_ORDER_LIST_BUTTON_NATIVE_ANDROID_IOS);
                }
            }
            driverTools.switchToNative();
            driverTools.waitForElementToAppear(LOADING_STATE);
            driverTools.swipeDown();
        }
    }
    @Override
    public void seeOrderList(String orderId) {
        driverTools.switchToWebViewUrl(orderId);
        driverTools.waitForElementToPresence(SEE_ORDER_LIST_BUTTON, 30);
        if (driverTools.element(SEE_ORDER_LIST_BUTTON).isPresent()) {
            driverTools.scrollIntoWebView(SEE_ORDER_LIST_BUTTON);
            driverTools.clickWithJavascriptExecutor(SEE_ORDER_LIST_BUTTON);
        } else if (driverTools.element(SEE_ORDER_LIST_BUTTON_2).isPresent()) {
            driverTools.switchToWebViewUrl(orderId);
            driverTools.scrollIntoWebView(SEE_ORDER_LIST_BUTTON_2);
            driverTools.clickWithJavascriptExecutor(SEE_ORDER_LIST_BUTTON_2);
        } else {
            logger.log("See Order List not found in webview, falling back to native context");
            driverTools.switchToNative();
            driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON_NATIVE_ANDROID_IOS, 10).orElseThrow();
            driverTools.click(SEE_ORDER_LIST_BUTTON_NATIVE_ANDROID_IOS);
        }
        driverTools.switchToNative();
        driverTools.waitForElementToAppear(LOADING_STATE);

        // refresh the page
        driverTools.swipeDown();
    }

    Locator getPaymentMethodLocator(String type) {
        return new Locator("PAYMENT_METHOD", AppiumBy.xpath("//p[contains(@class,'styles_title') and contains(text(),'"+type+"')]"));
    }

    private static final Locator PAYMENT_BUTTON_NATIVE = new Locator("PAYMENT_BUTTON_NATIVE", AppiumBy.xpath("//XCUIElementTypeStaticText[@label='See all' or @label='Lihat semua']"));

    @Override
    public void openPaymentMethod() {
        driverTools.waitForElementToAppear(PAYMENT_BUTTON_NATIVE,20);
        super.openPaymentMethod();
    }

    @Override
    public boolean selectCreditDebitPaymentTileRadio() {
        return selectPaymentTileRadio("Credit Card");
    }

    @Override
    public boolean selectPaymentTileRadio(String paymentMethod) {
        driverTools.waitForElementToAppear(PAYMENT_BUTTON_NATIVE, 20);
        return super.selectPaymentTileRadio(paymentMethod);
    }




    private Locator getServiceChargeLocator(int index) {
        return new Locator("SERVICE_CHARGE", AppiumBy.xpath("(//*[contains(@id,'installment-radio')]/preceding::*[contains(@class,'installment_notes')])['"+index+"']//*[contains(@class,'_size_b3')][last()]"));
    }


    @Override
    public boolean isOVOAsPaymentDisabledInGuest() {
        driverTools.switchToNative();
        boolean isSuccessOvoAsPayment = driverTools.waitForElementToAppear(NATIVE_CHECK_OVO_PAY_DISABLED).orElseThrow().isDisplayed();
        logger.log("is OVO payment disabled: " + isSuccessOvoAsPayment);
        return isSuccessOvoAsPayment;
    }

    @Override
    public void selectPayLater() {
        driverTools.switchToWebView();
        WebElement webElement = driverTools.waitForElementToAppear(SELECT_PAYLATER_WEBVIEW, 15).orElseThrow();
        driverTools.click(webElement);
        driverTools.switchToNative();
    }

    private Locator getChosenPaymentMethodLocator(String paymentType) {
        return new Locator(paymentType, AppiumBy.xpath("//*[contains(@class,'AccountInfo_account_detail_')]//*[text()='" + paymentType + "']"));
    }

    @Override
    public boolean isCorrectPaymentMethodChosen(String paymentType) {
        driverTools.switchToWebView();
        boolean isCorrectPaymentMethodChosen = driverTools.waitForElementToAppear(getChosenPaymentMethodLocator(paymentType), 15).isPresent();

        /*
        Special case for GoPay GK environment can have mocking to bypass payment process so flow can sometimes be different to PROD
         */
        if (paymentType.equals("GoPay") && !isCorrectPaymentMethodChosen && Configs.ENVIRONMENT == Environment.GK) {
            isCorrectPaymentMethodChosen = driverTools.waitForElementToAppear(GOPAY_SUCCESS, 15).isPresent();
        }

        logger.log("is correct payment method selected " + isCorrectPaymentMethodChosen);
        driverTools.switchToNative();
        return isCorrectPaymentMethodChosen;
    }

    @Override
    public String getPayLaterLimitPopup() {
        driverTools.switchToWebView();
        String payLaterLimit = driverTools.waitForElementToAppear(PAYLATER_LIMIT_POPUP, 15).orElseThrow().getText();
        logger.log(payLaterLimit);
        driverTools.switchToNative();
        return payLaterLimit;
    }

    @Override
    public String getPayLaterLimitWebview() {
        driverTools.switchToWebView();
        String payLaterLimit = driverTools.waitForElementToAppear(PAYLATER_LIMIT_WEBVIEW, 15).orElseThrow().getText();
        logger.log(payLaterLimit);
        driverTools.switchToNative();
        return payLaterLimit;
    }

    @Override
    public boolean checkOtp() {
        driverTools.switchToWebView();
        boolean checkOtp = driverTools.waitForElementToAppear(OTP_FORM_POPUP, 15).orElseThrow().isDisplayed();
        logger.log("Check send new OTP exist: " + checkOtp);
        driverTools.switchToNative();
        return checkOtp;
    }

    @Override
    public boolean isThankYouPageShown() {
        driverTools.switchToWebViewUrl("payment");
        driverTools.waitForElementToAppear(THANKYOU_PAGE_TEXT, 60);
        boolean result = driverTools.isElementDisplayed(THANKYOU_PAGE_TEXT);
        driverTools.switchToNative();
        return result;
    }

    @Override
    public boolean isThankYouPageShown(String orderId) {
        return isThankYouPageShown();
    }

    @Override
    public void openPriceDetail() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment", 3);
        driverTools.click(PRICE_DETAIL_ARROW_BUTTON_WEBVIEW);
        driverTools.switchToNative();
    }

    @Override
    public boolean getTripTitle(boolean isRoundTrip, String originStation, String destinationStation) {
        driverTools.switchToNative();
        boolean isTripTitleDisplayed = driverTools.waitForElementToPresence(TRIP_TITLE).isPresent();
        List<WebElement> tripTitle = driverTools.elements(TRIP_TITLE).orElseThrow();
        int tripTitleSize = tripTitle.size();
        if (isRoundTrip) {
            Assertion.assertThat("Trip title is displayed as round trip", tripTitleSize, is(2));
            Assertion.assertThat("Departure trip title is displayed as expected", driverTools.getText(tripTitle.get(0)), containsStringIgnoringCase(originStation + " → " + destinationStation));
            Assertion.assertThat("Return trip title is displayed as expected", driverTools.getText(tripTitle.get(1)), containsStringIgnoringCase(destinationStation + " → " + originStation));
        } else {
            Assertion.assertThat("Trip title is displayed as one way", tripTitleSize, is(1));
            Assertion.assertThat("Trip title is displayed as expected", driverTools.getText(TRIP_TITLE).orElseThrow(), containsStringIgnoringCase(originStation + " → " + destinationStation));
        }
        return isTripTitleDisplayed;
    }

    @Override
    public boolean getTripHeader(boolean isRoundTrip) {
        driverTools.switchToNative();
        boolean isTripHeaderDisplayed = driverTools.waitForElementToPresence(TRIP_HEADER).isPresent();
        List<WebElement> tripHeader = driverTools.elements(TRIP_HEADER).orElseThrow();
        int tripHeaderSize = tripHeader.size();
        if (isRoundTrip) {
            Assertion.assertThat("Trip header is displayed as round trip", tripHeaderSize, is(2));
            Assertion.assertThat("Departure trip header is not empty", driverTools.getText(tripHeader.get(0)), not(""));
            Assertion.assertThat("Return trip header is not empty", driverTools.getText(tripHeader.get(1)), not(""));
        } else {
            Assertion.assertThat("Trip header is displayed as one way", tripHeaderSize, is(1));
            Assertion.assertThat("Trip header is not empty", driverTools.getText(TRIP_HEADER).orElse(""), not(""));
        }
        return isTripHeaderDisplayed;
    }

    private Locator getPriceBreakdownLocator(String type) {
        switch (type) {
            case "Promo code":
                String typeIN = "Promo code";
                String typeEN = "Kode promo";
                return new Locator(type, AppiumBy.xpath("(//*[contains(text(),'" + typeIN + "') or contains(text(),'" + typeEN + "')]//following::*)[1]"));
            case "Tiket Points":
                return new Locator(type, AppiumBy.xpath("//span[contains(text(),'" + type + "')]//following::*[1]"));
            case "Gift voucher":
                String giftVoucherText = "Gift voucher";
                return new Locator(type, AppiumBy.xpath("(//span[contains(text(),'" +giftVoucherText+ "')]//following::*)[1]"));
            default:
                throw new NotImplementedException("Unimplemented price details type! " + type);
        }
    }

    private String getPrice(String type) {
        String price = driverTools.getText(getPriceBreakdownLocator(type)).orElseThrow();
        logger.log(type + " price:  " + price);
        return price;
    }

    private boolean verifyIfBreakDownCorrect(String type, String actualPrice) {
        if (actualPrice != null) {
            double price = CoreFunctions.convertPrice(actualPrice);
            String priceFromPriceDetail = getPrice(type);
            if (priceFromPriceDetail == null)
                return false;
            double priceDetailPopUP = -1 * CoreFunctions.convertPrice(priceFromPriceDetail);
            logger.log(type + " price " + price);
            logger.log(type + " price on price detail pop up " + priceDetailPopUP);
            return priceDetailPopUP == price;
        } else {
            return true;
        }
    }

    @Override
    public boolean priceBreakDown(String tiketPoint, String discount) {
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(getPriceBreakdownLocator(discount));
        List<WebElement> prices = driverTools.elements(PRICE_LOCATOR).orElseThrow();
        ArrayList<Double> arrayPrice = new ArrayList<Double>();
        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            boolean isContainNumber = text.matches(".*[0-9]+.*");
            if(isContainNumber) {
                String filterText = text.replaceFirst(",", ".");
                logger.log("price got: " + filterText);
                if (text.isEmpty()) filterText = "0";
                double price = CoreFunctions.convertPrice(filterText);
                logger.log("price got: " + price);
                arrayPrice.add(price);
            }
        }

        double sum = 0;
        for (double i : arrayPrice) {
            sum += i;
        }

        String price = driverTools.getText(TOTAL_PRICE_FROM_PRICE_DETAIL).orElseThrow();
        double totalPrice = CoreFunctions.convertPrice(price);
        logger.log("actual total: " + sum);
        logger.log("expected total: " + totalPrice);

        boolean isTotalPriceCorrect = ((totalPrice == sum) && (totalPrice > 0));
        boolean isTiketPriceCorrect = true;
        boolean isPromoPriceCorrect = true;
        if (discount.equals("tiket Points"))
            isTiketPriceCorrect = verifyIfBreakDownCorrect(discount, tiketPoint);
        else if (discount.equals("Promo code"))
            isPromoPriceCorrect = verifyIfBreakDownCorrect(discount, tiketPoint);

        logger.log("total price breakdown: " + isTotalPriceCorrect);
        logger.log("tiket point breakdown: " + isTiketPriceCorrect);
        driverTools.click(CLOSE_BTN_PRICE_DETAIL);

        driverTools.switchToNative();
        return isTotalPriceCorrect && isTiketPriceCorrect && isPromoPriceCorrect;
    }

    @Override
    public void clickChangePaymentMethodBtnSecondPage() {
        // FIXME: IMPLEMENTATION PENDING IOS
    }

    @Override
    public void allowCameraAccessIfPresent() {
        Map<String, Object> caps = driverTools.getCapabilities();
        boolean autoGrantPermissions = caps.get("autoGrantPermissions") != null && (boolean) caps.get("autoGrantPermissions");
        boolean autoAcceptAlerts = caps.get("autoAcceptAlerts") != null && (boolean) caps.get("autoAcceptAlerts");
        if (autoGrantPermissions || autoAcceptAlerts) {
            logger.log("Auto permission is enabled, no need to manually allow camera access.");
            return;
        }
        driverTools.switchToNative();
        driverTools.waitForElementToAppear(CAMERA_CONTINUE_BUTTON, 5).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(CAMERA_OK_BUTTON, 5).ifPresent(driverTools::click);
    }

    @Override
    public void deToogleTiketPoint() {
        // FIXME: IMPLEMENTATION PENDING IOS
    }

    @Override
    public String getAndToogleTiketPoint() {
        driverTools.switchToWebView();
        driverTools.click(TOOGLE_TIKET_POINT);
        if (driverTools.waitForElementToAppear(OTP_FEILD).isPresent()) {
            fillOtp();
        }
        String tiketPoint = driverTools.getText(TIKET_POINTS).orElseThrow();
        logger.log("Tiket point: " + tiketPoint);
        driverTools.switchToNative();
        return tiketPoint;
    }

    private void fillOtp() {
        WebElement otpField = driverTools.waitForElementToAppear(OTP_FEILD).orElseThrow();
        otpField.sendKeys("1234");
    }

    @Override
    public boolean verifyTotalAmountSecondPage(String expectedPrice) {
        driverTools.waitForElementToAppear(APP_SCREEN, 20);
        driverTools.switchToWebView();
        WebElement totalPrice = driverTools.swipeToElement(TOTAL_PRICE_SECOND_PAGE, Swipe.UP, 3).orElseThrow();
        String expPriceStr = driverTools.getText(totalPrice);
        double priceExpected = CoreFunctions.convertPrice(expectedPrice);
        double priceActual = CoreFunctions.convertPrice(expPriceStr);

        logger.log("price expected: " + priceExpected);
        logger.log("price actual: " + priceActual);
        driverTools.switchToNative();
        return priceActual == priceExpected;
    }

    @Override
    public boolean isVaNumberShow() {
        driverTools.switchToWebViewUrl("payment");
        boolean vaNumber = driverTools.waitForElementToPresence(VA_NUMBER,15).isPresent();
        return vaNumber;
    }

    @Override
    public void addOVOAccount() {
        driverTools.click(ADD_OVO_ACCOUNT);
    }

    @Override
    public boolean verifyLinkageOvo() {
        WebElement isLinkageOvoShow = driverTools.waitForElementToAppear(LINKAGE_TO_OVO).orElseThrow();
        return driverTools.isElementDisplayed(isLinkageOvoShow);
    }

    @Override
    public String getProductNamePayAtHotel() {
        WebElement productNamePayAtHotelElement = driverTools.waitForElementToAppear(PAY_AT_HOTEL_PRODUCT_NAME).orElseThrow();
        return driverTools.getText(productNamePayAtHotelElement);
    }

    @Override
    public String getCheckinDatePayAtHotel() {
        String[] result = driverTools.getText(PAY_AT_HOTEL_CHECKIN_DATE).orElseThrow().split(" ");
        if (result[1].length() == 1) {
            result[1] = "0" + result[1];
        }
        return String.join(" ",result);
    }

    @Override
    public String getDurationPayAtHotel() {
        return driverTools.getText(PAY_AT_HOTEL_NIGHT_DURATION).orElseThrow().substring(0,1);
    }

    @Override
    public String getTotalRoomPayAtHotel() {
        return driverTools.getText(PAY_AT_HOTEL_TOTAL_ROOM).orElseThrow().substring(0,1);
    }

    @Override
    public String getTotalGuestPayAtHotel() {
        return driverTools.getText(PAY_AT_HOTEL_TOTAL_GUEST).orElseThrow().substring(0,1);
    }

    @Override
    public String getTotalPricePayAtHotel() {
        return driverTools.getText(PAY_AT_HOTEL_TOTAL_PRICE).orElseThrow();
    }

    @Override
    public void clickOrderDetailPayAtHotel() {
        driverTools.click(PAY_AT_HOTEL_ORDER_DETAIL_ARROW);
    }

    @Override
    public void exitOrderDetailPayAtHotel() {
        driverTools.click(PAY_AT_HOTEL_CROSS_ORDER_DETAIL);
    }

    @Override
    public void clickPayAtHotelButton() {
        WebElement payAtHotelButton = driverTools.waitForElementToPresence(PAY_AT_HOTEL_BUTTON).orElseThrow();
        driverTools.click(payAtHotelButton);
    }

    @Override
    public String getTotalPriceFromPriceDetail() {
        String totalPrice = driverTools.getText(TOTAL_PRICE_FROM_PRICE_DETAIL).orElseThrow();
        return totalPrice;
    }

    @Override
    public void clickPayButtonCC() {
        driverTools.waitForElementToAppear(SUBMIT_CC_BUTTON_NATIVE);
        driverTools.clickElementByCoordinate(SUBMIT_CC_BUTTON_NATIVE);
        if(!driverTools.waitForElementToDisappear(SUBMIT_CC_BUTTON_NATIVE,5)){
            driverTools.clickElementByCoordinate(SUBMIT_CC_BUTTON_NATIVE);
        }
        driverTools.waitForElementToDisappear(SUBMIT_CC_BUTTON_NATIVE);
    }

    @Override
    public void fillCCDetailRealIssued(String cardNumber, String expiry, String cvv, String cardName) {
        driverTools.switchToNative();
        driverTools.waitForElementToAppear(CARD_NUMBER_NATIVE,20);
        driverTools.sendKeysKeyboard(CARD_NUMBER_NATIVE, cardNumber);
        driverTools.sendKeysKeyboard(EXPIRY_NATIVE, expiry);
        driverTools.sendKeysKeyboard(CVV_NATIVE, cvv);
        driverTools.sendKeysKeyboard(CARD_NAME_NATIVE, cardName);
        if (driverTools.isElementDisplayed(POSTAL_CODE_NATIVE)) {
            driverTools.sendKeysKeyboard(POSTAL_CODE_NATIVE, "12345");
        }
        if (driverTools.isElementDisplayed(BILLING_ADDRESS_NATIVE)) {
            driverTools.sendKeysKeyboard(BILLING_ADDRESS_NATIVE, "123 Main St, Anytown, USA");
        }
        driverTools.hideKeyboard();
    }

    @Override
    public void fillCCDetailRealIssued(String cardNumber, String expiry, String cvv, String cardName, String postalCode, String billingAddress) {
        driverTools.switchToNative();
        driverTools.waitForElementToAppear(CARD_NUMBER_NATIVE,20);
        driverTools.sendKeysKeyboard(CARD_NUMBER_NATIVE, cardNumber);
        driverTools.sendKeysKeyboard(EXPIRY_NATIVE, expiry);
        driverTools.sendKeysKeyboard(CVV_NATIVE, cvv);
        driverTools.sendKeysKeyboard(CARD_NAME_NATIVE, cardName);
        if (driverTools.isElementDisplayed(POSTAL_CODE_NATIVE)) {
            driverTools.sendKeysKeyboard(POSTAL_CODE_NATIVE, postalCode);
        }
        if (driverTools.isElementDisplayed(BILLING_ADDRESS_NATIVE)) {
            driverTools.sendKeysKeyboard(BILLING_ADDRESS_NATIVE, billingAddress);
        }
        driverTools.hideKeyboard();
    }

    @Override
    public void submitStripeNative() {
        driverTools.switchToNative();
        driverTools.waitForElementToAppear(COMPLETE_STRIPE);
        driverTools.click(COMPLETE_STRIPE);
        driverTools.waitForElementToDisappear(COMPLETE_STRIPE);
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    @Override
    public void openWaitingPaymentPage(String orderId, String orderHash) {
        Deeplink deepLink = new Deeplink("WAITING_PAYMENT_PAGE"+orderId,
                "tiket://mpayment-gatotkaca.tiket.com/next/v4?order_hash="+orderHash+"&order_id="+orderId+"&device_type="+Configs.PLATFORM.toString().toLowerCase()+"&lang=id",
                "tiket://mpayment-preprod.tiket.com/next/v4?order_hash="+orderHash+"&order_id="+orderId+"&device_type="+Configs.PLATFORM.toString().toLowerCase()+"&lang=id",
                "tiket://mpayment.tiket.com/next/v4?order_hash="+orderHash+"&order_id="+orderId+"&device_type="+Configs.PLATFORM.toString().toLowerCase()+"&lang=id");
        driverTools.deeplink(deepLink);
        driverTools.waitForElementToAppear(ORDER_ID);
    }

    @Override
    public void clickLoginFromNativePopup() {
        driverTools.click(LOGIN_NOW);
    }
}