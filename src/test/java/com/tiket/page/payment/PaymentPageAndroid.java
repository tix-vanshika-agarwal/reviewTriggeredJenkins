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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

import static com.tiket.page.home.HomePageAndroid.BRAZE_CLOSE;

public class PaymentPageAndroid <T extends WebDriver> extends PaymentPage<T> {

    public static final Locator ORDER_ID = new Locator("ORDER_ID", AppiumBy.xpath("//android.widget.TextView[contains(@text,'Order ID') or contains(@text,'ID Pesanan')] | //*[contains(@text,'Order ID:') or contains(@text,'ID Pesanan:')]"));
    private static final Locator PERMISSION_TYPE_ALLOW_ONCE = new Locator("PERMISSION_TYPE_ALLOW_ONCE", AppiumBy.xpath("//*[contains(@resource-id,'permission_allow_one_time_button')]"));
    private static final Locator WHILE_USING_APP_PERMISSION_BUTTON = new Locator("WHILE_USING_APP_PERMISSION_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'permission_allow_foreground_only_button')]"));
    // In-app custom permission-rationale screen (shown before the native OS dialog), separate from
    // the native PERMISSION_TYPE_ALLOW_ONCE/WHILE_USING_APP_PERMISSION_BUTTON dialogs above.
    private static final Locator CAMERA_ALLOW_ACCESS_BUTTON = new Locator("CAMERA_ALLOW_ACCESS_BUTTON", AppiumBy.xpath("//*[@text='Allow access' or @text='Beri izin akses']"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//*[contains(@resource-id,'toolbar')]//android.widget.ImageView"));
    public static final Locator LOADING_STATE = new Locator("LOADING_STATE", AppiumBy.id("loading"));
    /** DownPayment / pay-later confirmation bottom sheet shown before payment page. */
    private static final Locator DOWN_PAYMENT_CONFIRM_HEADER = new Locator("DOWN_PAYMENT_CONFIRM_HEADER", AppiumBy.xpath("//*[contains(@resource-id,'tds_txt_header_info_view') and (contains(@text,'full amount later') or contains(@text,'pay IDR 0') or contains(@text,'ll pay') or contains(@text,'nanti'))]"));
    private static final Locator DOWN_PAYMENT_CONFIRM_CANCEL = new Locator("DOWN_PAYMENT_CONFIRM_CANCEL", AppiumBy.xpath("//*[contains(@resource-id,'tds_btn_secondary_info_view')]//*[contains(@resource-id,'tds_button_view') and (@text='Cancel' or @text='Batal')]"));
    private static final Locator PAY_NOW_TIMING_OPTION = new Locator("PAY_NOW_TIMING_OPTION", AppiumBy.xpath("//*[contains(@text,'Pay now') or contains(@text,'Bayar sekarang') or contains(@text,'Pay today') or contains(@text,'Bayar hari ini') or contains(@text,'Pay in full') or contains(@text,'Bayar penuh')]"));
    private static final Locator PICK_WHEN_TO_PAY_CONTINUE = new Locator("PICK_WHEN_TO_PAY_CONTINUE", AppiumBy.xpath("//*[contains(@resource-id,'btn_continue') or contains(@resource-id,'tds_btn_primary') or contains(@resource-id,'ll_parent')]//*[contains(@resource-id,'tds_button_view') or contains(@resource-id,'tv_primary')][@text='Continue' or @text='Lanjutkan' or @text='Lanjut' or @text='Select Payment' or @text='Pilih Pembayaran' or contains(@text,'Continue to Payment') or contains(@text,'Lanjut ke Pembayaran')]"));
    private static final Locator COMPLETE_STRIPE = new Locator("COMPLETE_STRIPE", AppiumBy.xpath("//*[@resource-id='test-source-authorize-3ds']"));
    private static final Locator RECOMMENDED_PAYMENT_METHOD_WRAPPER = new Locator("RECOMMENDED_PAYMENT_METHOD_WRAPPER", AppiumBy.xpath("//*[contains(@class,'recommended_method_wrapper')]"));
    private static final Locator RECOMMENDED_PAYMENT_METHOD_CHECBOX = new Locator("RECOMMENDED_PAYMENT_METHOD_CHECBOX", AppiumBy.xpath("//*[contains(@class,'checkbox')]"));
    static final Locator CHANGE_PAYMENT_METHOD_BUTTON = new Locator("CHANGE_PAYMENT_METHOD_BUTTON", AppiumBy.xpath("//*[contains(@class,'change_button')]"));
    static final Locator CHOOSE_PAYMENT_METHOD_BUTTON = new Locator("CHOOSE_PAYMENT_METHOD_BUTTON", AppiumBy.xpath("(//*[contains(@class,'variant_primary')])[1]"));
    static final Locator CHOOSE_PAYMENT_METHOD_PARENT = new Locator("CHOOSE_PAYMENT_METHOD_PARENT", AppiumBy.xpath("//*[@class='bs-content-body bs-content-padding']"));
    private static final Locator PAYLATER_LIMIT_POPUP = new Locator("PAYLATER_LIMIT_POPUP", AppiumBy.xpath("//*[@class='android.widget.TextView' and contains(@text, 'limit')]"));
    private static final Locator CHECK_OVO_PAY_DISABLED = new Locator("CHECK_OVO_PAY_DISABLED", AppiumBy.xpath("//*[contains(@class,'Text_variant_disabled') and text()='OVO']"));
    private static final Locator APP_SCREEN = new Locator("APP_SCREEN", AppiumBy.xpath("//*[contains(@resource-id,'app')]"));
    private static final Locator GOPAY_SUCCESS = new Locator("GOPAY_SUCCESS", AppiumBy.cssSelector("[class='success']"));
    private static final Locator SEE_ORDER_LIST_BUTTON_HOTEL = new Locator("SEE_ORDER_LIST_BUTTON_HOTEL", AppiumBy.xpath("//*[contains(text(), 'See Order List') or contains(text(), 'Daftar Pesanan') or contains(text(), 'See order list') or contains(text(), 'daftar pesanan')]"));
    private static final Locator CARD_NUMBER = new Locator("CARD_NUMBER", AppiumBy.cssSelector("[name='ccNumber']"));
    private static final Locator EXPIRY = new Locator("EXPIRY", AppiumBy.cssSelector("[name='ccExp']"));
    private static final Locator CVV = new Locator("CVV", AppiumBy.cssSelector("[name='cardCvvNumber']"));
    private static final Locator CARD_NAME = new Locator("CARD_NAME", AppiumBy.cssSelector("[name='ccHolder']"));
    private static final Locator SAVE_CARD_DETAIL = new Locator("SAVE_CARD_DETAIL", AppiumBy.xpath("//*[contains(@type,'submit')]"));
    private static final Locator INSTALLMENTS = new Locator("INSTALLMENTS", AppiumBy.xpath("//*[contains(@id,'installment-radio')]"));
    private static final Locator SERVICE_CHARGE = new Locator("SERVICE_CHARGE", AppiumBy.xpath("(//*[contains(@id,'installment-radio') and @value='true']/preceding::*[contains(@class,'installment-notes')]/*[contains(@class,'emphasis hover')])[last()]"));
    private static final Locator PRICE_DETAIL = new Locator("PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'price')]//*[contains(@class,'tix-icon v4')]"));
    private static final Locator SERVICE_CHARGE_PRICE_DETAIL = new Locator("SERVICE_CHARGE_PRICE_DETAIL", AppiumBy.xpath("//*[contains(text(),'Biaya layanan') or contains(text(),'Service charge')]//following-sibling::*"));
    private static final Locator SELECT_PAYLATER_WEBVIEW = new Locator("SELECT_PAYLATER_WEBVIEW", AppiumBy.xpath("(//*[@class='content-wrapper']//*[@class='b2 mobile high-emphasis bold hover tix-font v4'])[1]"));
    private static final Locator PAYLATER_LIMIT_WEBVIEW = new Locator("PAYLATER_LIMIT_WEBVIEW", AppiumBy.xpath("//p[@class='b3 mobile high-emphasis hover tix-font v4' and contains(text(),'limit')]"));
    private static final Locator OTP_FORM_POPUP = new Locator("OTP_FORM_POPUP", AppiumBy.xpath("(//*[@class='bs-content-body bs-content-padding']//*[@class='form-otp'])[1]"));
    private static final Locator TIKET_POINT = new Locator("TIKET_POINTS", AppiumBy.xpath("//*[contains(@class,'TiketPoints_tiket_points')]//*[contains(@class,'weight')]"));
    private static final Locator TOOGLE_TIKET_POINT = new Locator("TOOGLE_TIKET_POINT", AppiumBy.xpath("//*[contains(@class,'_toggle_small')]"));
    private static final Locator OTP_FEILD = new Locator("OTP_FEILD", AppiumBy.xpath("//*[@class='otp-input']//input"));
    private static final Locator TOTAL_PRICE_SECOND_PAGE = new Locator("TOTAL_PRICE_SECOND_PAGE", AppiumBy.xpath("//*[contains(@class,'number_box')]//*[contains(text(),'IDR')]"));
    private static final Locator CHANGE_PAYMENT_BTN_SECOND_PAGE = new Locator("CHANGE_PAYMENT_BTN_SECOND_PAGE", AppiumBy.xpath("//*[contains(text(), 'Change Payment Method') or contains(text(), 'Ubah Metode Pembayaran')]"));
    private static final Locator CHANGE_PAYMENT_BTN_POPUP_SECOND_PAGE = new Locator("CHANGE_PAYMENT_BTN_POPUP__SECOND_PAGE", AppiumBy.xpath("//*[contains(@class,'pop_up')]//*[contains(text(), 'Change Payment Method') or contains(text(), 'Ubah Metode Pembayaran')]"));
    private static final Locator PROCESS_PAYMENT_POPUP = new Locator("PROCESS_PAYMENT_POPUP", AppiumBy.xpath("//*[contains(@class,'PaymentProcessingPage_popup')]//button[1]"));
    private static final Locator APPLY_PROMOCODE = new Locator("APPLY_PROMOCODE", AppiumBy.xpath("//*[@class='tix-button v4 mobile button waves-effect waves-default block large']"));
    private static final Locator CANT_COMBINE_PROMO_LABEL = new Locator("CANT_COMBINE_PROMO_LABEL", AppiumBy.xpath("//*[@class='lower-content landing']//*[@class='b3 mobile high-emphasis hover tix-font v4']"));
    private static final Locator PAY_WITH_BALANCE = new Locator("PAY_WITH_BALANCE", AppiumBy.id("cl_payment_type_container"));
    private static final Locator CONTINUE_REFUNDS = new Locator("CONTINUE_REFUNDS", AppiumBy.xpath("//*[@class='btn button-wrap btn yellow btn-continue-cashback btn-continue-cashback-enabled']"));
    private static final Locator TO_MY_ORDER = new Locator("TO_MY_ORDER", AppiumBy.xpath("//android.view.View[@content-desc='KE MY ORDER' or @content-desc='GO TO MY ORDER']"));
    private static final Locator PAYMENT_BUTTON_NATIVE = new Locator("PAYMENT_BUTTON_NATIVE", AppiumBy.xpath("//android.webkit.WebView//android.widget.Button"));
    private static final Locator VA_NUMBER = new Locator("VA_NUMBER", AppiumBy.xpath("//*[contains(@class,'AccountInfo_number_box')]//*[contains(@class,'size_h3')]"));
    private static final Locator ADD_OVO_ACCOUNT = new Locator("ADD_OVO_ACCOUNT", AppiumBy.xpath("//*[@text='+ Add OVO Account' or @text='+ Tambahkan Akun OVO']"));
    private static final Locator LINKAGE_TO_OVO = new Locator("LINKAGE_TO_OVO", AppiumBy.xpath("//*[@text='Linkage to OVO' or @text='Hubungkan ke OVO']"));
    private static final Locator SUBMIT_CC = new Locator("SUBMIT_CC", AppiumBy.xpath("//*[@class='tix-button v4 button-submit payment-submit mobile button waves-effect waves-default block large']"));
    private static final Locator SUBMIT = new Locator("SUBMIT", AppiumBy.xpath("//*[@type='submit']"));
    private static final Locator CREDIT_CARD_FULL_PAYMENT = new Locator("CREDIT_CARD_FULL_PAYMENT", AppiumBy.xpath("(//*[@class='installment-notes']/p[1])[1]"));
    private static final Locator PAY_AT_HOTEL_ORDER_DETAIL_ARROW = new Locator("PAY_AT_HOTEL_ORDER_DETAIL_ARROW", AppiumBy.xpath("//*[contains(@class,'styles_more')]"));
    private static final Locator PAY_AT_HOTEL_PRODUCT_NAME = new Locator("PAY_AT_HOTEL_PRODUCT_NAME", AppiumBy.xpath("//*[contains(@class,'styles_order_title_icon')]"));
    private static final Locator PAY_AT_HOTEL_TOTAL_PRICE = new Locator("PAY_AT_HOTEL_TOTAL_PRICE", AppiumBy.xpath("//*[contains(@class,'PriceSummary_final_price')]/span"));
    private static final Locator PAY_AT_HOTEL_NIGHT_DURATION = new Locator("PAY_AT_HOTEL_NIGHT_DURATION", AppiumBy.xpath("//*[contains(@class,'size_b2') and contains(text(),'Malam') or contains(text(),'night')]"));
    private static final Locator PAY_AT_HOTEL_CHECKIN_DATE = new Locator("PAY_AT_HOTEL_CHECKIN_DATE", AppiumBy.xpath("//*[contains(@class,'size_b2') and contains(text(),'entry') or contains(text(),'masuk')]//following-sibling::*"));
    private static final Locator PAY_AT_HOTEL_TOTAL_ROOM = new Locator("PAY_AT_HOTEL_TOTAL_ROOM", AppiumBy.xpath("//*[contains(@class,'Hotel_data_detail')]//child::*[contains(text(),'room') or contains(text(),'kamar')]"));
    private static final Locator PAY_AT_HOTEL_TOTAL_GUEST = new Locator("PAY_AT_HOTEL_TOTAL_GUEST", AppiumBy.xpath("//*[contains(@class,'Hotel_data_detail')]//child::*[contains(text(),'guest') or contains(text(),'tamu')]"));
    private static final Locator PAY_AT_HOTEL_CROSS_ORDER_DETAIL = new Locator("PAY_AT_HOTEL_CROSS_ORDER_DETAIL", AppiumBy.id("iv_toolbar_left_button"));
    private static final Locator PRICE_DETAIL_CLOSE_BUTTON = new Locator("PRICE_DETAIL_CLOSE_BUTTON", AppiumBy.xpath("//android.widget.Button[contains(@text, 'Close') or contains(@text, 'Tutup')]"));
    private static final Locator PROMO_CODE_TEXT = new Locator("PROMO_CODE_TEXT", AppiumBy.xpath("//*[@data-testid='btnApplyUnapplyPromo']//p | //*[contains(@class,'PromoCard_container')]//p | //*[contains(@class,'PromoDisplay_discount_wrapper__')]//*[contains(@class,'PromoDisplay_text_wrapper')]//span"));
    private static final Locator PRICE_LOCATOR = new Locator("PRICE_LOCATOR", AppiumBy.xpath("//li[contains(@class,'PriceDetails_price_group')]//*[contains(text(),',') or contains(text(),'.')]"));
    private static final Locator TOTAL_PRICE_FROM_PRICE_DETAIL = new Locator("TOTAL_PRICE_FROM_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'PriceDetails_total_payment')]//*[contains(@class,'text') and contains(text(),',') or contains(text(),'.')]"));
    public static final Locator SUBMIT_CC_BUTTON_NATIVE = new Locator("SUBMIT_CC_BUTTON_NATIVE", AppiumBy.xpath("//*[@class=\"android.widget.Button\" and (@text=\"Pay Now\" or @text=\"Bayar Sekarang\")]"));
    private static final Locator CLOSE_BTN_PRICE_DETAIL = new Locator("CLOSE_BTPROMO_CODE_TEXTN_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'close_button')]"));
    private static final Locator REFUND_DEPOSIT_BUTTON = new Locator("REFUND_DEPOSIT_BUTTON", AppiumBy.xpath("//*[contains(@text, 'Refund Deposit')]"));
    private static final Locator PAGE_2C2P_PAYMENT = new Locator("PAGE_2C2P_PAYMENT", AppiumBy.xpath("//*[contains(@text,'2C2P')]"));
    private static final Locator OTP_2C2P_PAYMENT = new Locator("OTP_2C2P_PAYMENT", AppiumBy.xpath("//*[contains(@text,'2C2P')]//following::android.widget.EditText"));
    private static final Locator SUBMIT_2C2P_PAYMENT = new Locator("SUBMIT_2C2P_PAYMENT", AppiumBy.xpath("//*[contains(@text,'2C2P')]//following::android.widget.Button[@text='Submit']"));
    private static final Locator OVO_PIN_INPUT = new Locator("OVO_PIN_INPUT", AppiumBy.xpath("//*[contains(@text,'OVO PIN')]//following-sibling::android.widget.EditText"));
    private static final Locator OVO_PAY_BUTTON = new Locator("OVO_PIN_INPUT", AppiumBy.xpath("//android.widget.Button[contains(@text,'BAYAR') or contains(@text,'PAY')]"));
    private static Map<String, Boolean> FLAG_MAP_BRAZE_ORDER = new HashMap<>();
    private static final Locator LOGIN_NOW = new Locator("LOGIN_NOW", AppiumBy.xpath("//button[.//span[text()='Log in now' or text()='Log in sekarang']]"));

    private static final Locator PAY_NOW_BUTTON = new Locator("PAY_NOW_BUTTON", AppiumBy.xpath("//*[@text='Pay Now' or @text='Bayar Sekarang']"));
    private static final Locator GO_TO_HOMEPAGE_LINK = new Locator("GO_TO_HOMEPAGE_LINK", AppiumBy.xpath("//*[contains(@text, 'Go to Homepage') or contains(@text, 'Ke Halaman Utama')]"));
    private static final Locator CC_NUMBER_INPUT = new Locator("CC_NUMBER_INPUT", AppiumBy.xpath("//android.widget.EditText[@text='Credit/debit card number' or contains(@text, 'Nomor kartu')] | //*[contains(@text, 'Credit/debit')]/ancestor::*//android.widget.EditText[1]"));
    private static final Locator CC_EXPIRY_INPUT = new Locator("CC_EXPIRY_INPUT", AppiumBy.xpath("//android.widget.EditText[@text='MM/YY'] | //*[@text='MM/YY']/ancestor::*//android.widget.EditText[1]"));
    private static final Locator CC_CVV_INPUT = new Locator("CC_CVV_INPUT", AppiumBy.xpath("//android.widget.EditText[@text='CVC/CVV'] | //*[@text='CVC/CVV']/ancestor::*//android.widget.EditText[1]"));
    private static final Locator CC_NAME_INPUT = new Locator("CC_NAME_INPUT", AppiumBy.xpath("//android.widget.EditText[@text='Cardholder name' or contains(@text, 'Nama pemegang')] | //*[contains(@text, 'Cardholder')]/ancestor::*//android.widget.EditText[1]"));
    private static final Locator INSTALLMENT_DROPDOWN = new Locator("INSTALLMENT_DROPDOWN", AppiumBy.xpath("//*[@text='Select installment' or contains(@text, 'cicilan')]"));
    private static final Locator FULL_PAYMENT_OPTION = new Locator("FULL_PAYMENT_OPTION", AppiumBy.xpath("//*[@text='Full Payment' or @text='Bayar Penuh']"));
    private static final Locator SAVE_BUTTON_PAYMENT = new Locator("SAVE_BUTTON", AppiumBy.xpath("//*[@text='Save' or @text='Simpan']"));
    private static final Locator ORDER_ID_WEBVIEW = new Locator("ORDER_ID_WEBVIEW", AppiumBy.xpath("//*[contains(text(),'Order ID') or contains(text(),'ID Pesanan')]"));

    public PaymentPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void fillOvoPin(String ovoPin) {
        driverTools.waitForElementToPresence(OVO_PIN_INPUT);
        driverTools.sendKeys(OVO_PIN_INPUT, ovoPin);

        driverTools.click(OVO_PAY_BUTTON);
    }

    @Override
    public void clickLoginFromNativePopup() {
        throw new NotImplementedException();
    }

    @Override
    public void clickLoginFromPopup() {
        driverTools.switchToWebViewUrl("payment");
        driverTools.click(LOGIN_NOW);
        driverTools.switchToNative();
    }

    @Override
    public void checkCcOtp() {
        if (driverTools.waitForElementToAppear(PAGE_2C2P_PAYMENT).isPresent()) {
            logger.log("Redirect to the 2C2P Payment Gateway");
            driverTools.waitForElementToPresence(OTP_2C2P_PAYMENT).orElseThrow();
            driverTools.sendKeys(OTP_2C2P_PAYMENT, "123456");
            driverTools.click(SUBMIT_2C2P_PAYMENT);
        } else {
            logger.log("There is no redirection to the Payment Gateway");
        }
    }

    @Override
    public String checkOrderID() {
        driverTools.switchToNative();
        driverTools.waitForElementToDisappear(LOADING_STATE,30);
        bypassDownPaymentAndChoosePayNowIfPresent();
        String orderID = "";
        if (driverTools.waitForElementToPresence(ORDER_ID, 15).isPresent()) {
            orderID = driverTools.getText(ORDER_ID).orElse("");
        }
        if (orderID.replaceAll("[^0-9]", "").isEmpty()) {
            orderID = getOrderIdFromPaymentWebView();
        }
        logger.log("original order_id : " + orderID);

        // Removing non-numeric characters
        String numericOrderID = orderID.replaceAll("[^0-9]", "");
        logger.log("numeric order_id : " + numericOrderID);
        if (numericOrderID.isEmpty()) {
            throw new NoSuchElementException("Order ID not found on payment page (native or webview)");
        }

        return numericOrderID;
    }

    private String getOrderIdFromPaymentWebView() {
        driverTools.waitForWebview();
        try {
            driverTools.switchToWebViewUrl("payment");
            return driverTools.waitForElementToPresence(ORDER_ID_WEBVIEW, 20)
                    .flatMap(ignored -> driverTools.getText(ORDER_ID_WEBVIEW))
                    .orElseThrow();
        } finally {
            driverTools.switchToNative();
        }
    }

    /**
     * DownPayment confirm (IDR 0 today) skips the payment page and goes straight to Order confirmed.
     * Cancel it, pick Pay now, then continue so Order ID is available on the payment page.
     */
    private void bypassDownPaymentAndChoosePayNowIfPresent() {
        if (!driverTools.waitForElementToAppear(DOWN_PAYMENT_CONFIRM_HEADER, 5).isPresent()) {
            return;
        }
        logger.log("Down payment confirm dialog shown — tapping Cancel (Continue would skip payment page)");
        if (driverTools.waitForElementToAppear(DOWN_PAYMENT_CONFIRM_CANCEL, 3).isPresent()) {
            driverTools.click(DOWN_PAYMENT_CONFIRM_CANCEL);
            driverTools.waitForElementToDisappear(DOWN_PAYMENT_CONFIRM_HEADER, 15);
        }

        if (driverTools.waitForElementToAppear(PAY_NOW_TIMING_OPTION, 8).isPresent()) {
            logger.log("Selecting Pay now timing option");
            driverTools.click(PAY_NOW_TIMING_OPTION);
        }
        if (driverTools.waitForElementToAppear(PICK_WHEN_TO_PAY_CONTINUE, 5).isPresent()) {
            logger.log("Continuing from pick-when-to-pay to payment page");
            driverTools.click(PICK_WHEN_TO_PAY_CONTINUE);
        }
        driverTools.waitForElementToDisappear(LOADING_STATE, 30);
    }


    @Override
    public void clickBackButton() {
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public void clickRecommendedPaymentMethod() {
        driverTools.waitForElementToDisappear(LOADING_STATE, 30);
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(RECOMMENDED_PAYMENT_METHOD_WRAPPER).orElseThrow();
        WebElement webElement = driverTools.element(RECOMMENDED_PAYMENT_METHOD_CHECBOX).orElseThrow();
        driverTools.click(webElement);
        driverTools.switchToNative();
    }

    @Override
    public void seeOrderList() {
        try {
            driverTools.switchToWebViewUrl("payment");
            if (driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON).isPresent()) {
                driverTools.swipeToElement(SEE_ORDER_LIST_BUTTON, Swipe.UP, 3).orElseThrow();
                driverTools.click(SEE_ORDER_LIST_BUTTON);
            } else {
                WebElement webElement = driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON_HOTEL).orElseThrow();
                driverTools.click(webElement);
            }
        } catch (Exception e) {
            logger.log(e.toString());
            driverTools.switchToWebViewUrl("my-refund");
            WebElement webElement = driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON_HOTEL).orElseThrow();
            driverTools.click(webElement);
        }
        driverTools.switchToNative();
        driverTools.waitForElementToAppear(LOADING_STATE);
        String sessionID = session.sessionId();
        if(Configs.ENVIRONMENT == Environment.PROD && FLAG_MAP_BRAZE_ORDER.get(sessionID)==null){
            if(driverTools.waitForElementToPresence(BRAZE_CLOSE,10).isPresent()){
                driverTools.clickElementByCoordinate(BRAZE_CLOSE);
                initFlagBraze(sessionID,true);
            }
        }
    }

    private void initFlagBraze(String sessionID, boolean check){
        FLAG_MAP_BRAZE_ORDER.put(sessionID,check);
    }

    @Override
    public void seeOrderList(String orderId) {
        driverTools.switchToWebViewUrl(orderId);
        if (driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON).isPresent()) {
            driverTools.scrollIntoWebView(SEE_ORDER_LIST_BUTTON);
            driverTools.click(SEE_ORDER_LIST_BUTTON);
        } else if (driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON_HOTEL, 3).isPresent()) {
            driverTools.scrollIntoWebView(SEE_ORDER_LIST_BUTTON_HOTEL);
            driverTools.click(SEE_ORDER_LIST_BUTTON_HOTEL);
        } else {
            logger.log("See Order List not found in webview, falling back to native context");
            driverTools.switchToNative();
            driverTools.waitForElementToAppear(SEE_ORDER_LIST_BUTTON_NATIVE_ANDROID_IOS, 10).orElseThrow();
            driverTools.click(SEE_ORDER_LIST_BUTTON_NATIVE_ANDROID_IOS);
        }
        driverTools.switchToNative();
        driverTools.waitForElementToAppear(LOADING_STATE);
    }

    @Override
    public boolean isThankYouPageShown() {
        try {
            driverTools.switchToWebViewUrl("payment");
            driverTools.waitForElementToAppear(THANKYOU_PAGE_TEXT, 60);
            boolean result = driverTools.isElementDisplayed(THANKYOU_PAGE_TEXT);
            if (!result) {
                try {
                    if (driverTools.waitForElementToAppear(PROCESS_PAYMENT_POPUP, 5).isPresent()) {
                        driverTools.clickWithJavascriptExecutor(PROCESS_PAYMENT_POPUP);
                        result = driverTools.waitForElementsToAppear(THANKYOU_PAGE_TEXT).isPresent();
                    }
                } catch (Exception ignored) {
                    logger.log("Process-payment popup handling skipped: " + ignored.getMessage());
                }
            }
            return result;
        } finally {
            try {
                driverTools.switchToNative();
            } catch (Exception ignored) {
                logger.log("switchToNative after thank-you check failed: " + ignored.getMessage());
            }
        }
    }

    @Override
    public boolean isThankYouPageShown(String orderId) {
        driverTools.switchToWebViewUrl(orderId);
        WebElement webElement = driverTools.waitForElementToAppear(THANKYOU_PAGE_TEXT, 60).orElseThrow();
        boolean result = driverTools.isElementDisplayed(webElement);
        driverTools.switchToNative();
        return result;
    }

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

    @Override
    public void selectPayLater() {
        driverTools.switchToWebView();
        WebElement webElement = driverTools.waitForElementToAppear(SELECT_PAYLATER_WEBVIEW, 15).orElseThrow();
        driverTools.click(webElement);
        driverTools.switchToNative();
    }

    @Override
    public boolean isOVOAsPaymentDisabledInGuest() {
        driverTools.switchToWebViewUrl("payment");
        boolean isSuccessOvoAsPayment = driverTools.waitForElementToAppear(CHECK_OVO_PAY_DISABLED).orElseThrow().isDisplayed();
        logger.log("is OVO payment disabled: " + isSuccessOvoAsPayment);
        driverTools.switchToNative();
        return isSuccessOvoAsPayment;
    }

    private Locator getChosenPaymentMethodLocator(String paymentType) {
        return new Locator(paymentType,AppiumBy.xpath("//*[contains(@class,'styles_account')]//*[text()='" + paymentType + "']"));
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

    public String getPayLaterLimitPopup() {
        String payLaterLimit = driverTools.waitForElementToAppear(PAYLATER_LIMIT_POPUP, 15).orElseThrow().getText();
        logger.log(payLaterLimit);
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

    private void fillOtp() {
        WebElement otpField = driverTools.waitForElementToAppear(OTP_FEILD).orElseThrow();
        otpField.sendKeys("1234");
    }

    @Override
    public String getAndToogleTiketPoint() {
        driverTools.switchToWebView();
        driverTools.click(TOOGLE_TIKET_POINT);
        if (driverTools.waitForElementToAppear(OTP_FEILD).isPresent()) {
            fillOtp();
        }
        String tiketPoint = driverTools.getText(TIKET_POINT).orElseThrow();
        logger.log("Tiket point: " + tiketPoint);
        driverTools.switchToNative();
        return tiketPoint;
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


    private String getPrice (String type) {
        Optional<WebElement> priceElement = driverTools.waitForElementToAppear(getPriceBreakdownLocator(type));
        boolean priceAvailable = priceElement.isPresent();
        if (priceAvailable) {
            String price = driverTools.getText(priceElement.orElseThrow());
            logger.log(type + " price:  " + price);
            return price;
        } else {
            return null;
        }
    }

    private boolean verifyIfBreakDownCorrect(String type,String actualPrice) {
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

    // "tiket Points"
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
    public boolean verifyTotalAmountSecondPage(String expectedPrice) {
        driverTools.waitForElementToAppear(APP_SCREEN,20);
        driverTools.switchToWebView();
        driverTools.swipeToElement(TOTAL_PRICE_SECOND_PAGE,Swipe.UP,3);
        String expPriceStr = driverTools.getText(TOTAL_PRICE_SECOND_PAGE).orElseThrow();

        double priceExpected = CoreFunctions.convertPrice(expectedPrice);
        double priceActual = CoreFunctions.convertPrice(expPriceStr);
        logger.log("price expected: " + priceExpected);
        logger.log("price actual: " + priceActual);

        driverTools.switchToNative();
        return priceActual == priceExpected;
    }

    @Override
    public void clickChangePaymentMethodBtnSecondPage() {
        driverTools.switchToWebView();
        driverTools.swipeToElement(CHANGE_PAYMENT_BTN_SECOND_PAGE,Swipe.UP,3);
        driverTools.click(CHANGE_PAYMENT_BTN_SECOND_PAGE);
        WebElement popUp = driverTools.waitForElementToAppear(CHANGE_PAYMENT_BTN_POPUP_SECOND_PAGE,20).orElseThrow();
        driverTools.click(popUp);
        driverTools.switchToNative();
    }

    @Override
    public void deToogleTiketPoint() {
        driverTools.waitForElementToAppear(APP_SCREEN,20);
        driverTools.switchToWebView();
        driverTools.click(TOOGLE_TIKET_POINT);
        driverTools.switchToNative();
    }

    public  boolean isVaNumberShow() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("payment");
        boolean isVaNumberShow = driverTools.waitForElementToAppear(VA_NUMBER).isPresent();
        driverTools.switchToNative();
        return isVaNumberShow;
    }

    @Override
    public void addOVOAccount() {
        WebElement ovoButton = driverTools.waitForElementToPresence(ADD_OVO_ACCOUNT).orElseThrow();
        driverTools.click(ovoButton);
    }

    @Override
    public boolean verifyLinkageOvo() {
        WebElement isLinkageOvoShow = driverTools.waitForElementToAppear(LINKAGE_TO_OVO).orElseThrow();
        return driverTools.isElementDisplayed(isLinkageOvoShow);
    }

    @Override
    public String getProductNamePayAtHotel() {
        driverTools.switchToWebView();
        WebElement productNamePayAtHotelElement = driverTools.waitForElementToAppear(PAY_AT_HOTEL_PRODUCT_NAME).orElseThrow();
        String text = driverTools.getText(productNamePayAtHotelElement);
        driverTools.switchToNative();
        return text;
    }

    @Override
    public String getCheckinDatePayAtHotel() {
        driverTools.switchToWebView();
        String[] result = driverTools.getText(PAY_AT_HOTEL_CHECKIN_DATE).orElseThrow().split(" ");
        if (result[1].length() == 1) {
            result[1] = "0" + result[1];
        }
        driverTools.switchToNative();
        return String.join(" ",result);
    }

    @Override
    public String getDurationPayAtHotel() {
        driverTools.switchToWebView();
        String text = driverTools.getText(PAY_AT_HOTEL_NIGHT_DURATION).orElseThrow().substring(0,1);
        driverTools.switchToNative();
        return text;
    }

    @Override
    public String getTotalRoomPayAtHotel() {
        driverTools.switchToWebView();
        String text = driverTools.getText(PAY_AT_HOTEL_TOTAL_ROOM).orElseThrow().substring(0,1);
        driverTools.switchToNative();
        return text;
    }

    @Override
    public String getTotalGuestPayAtHotel() {
        driverTools.switchToWebView();
        String text = driverTools.getText(PAY_AT_HOTEL_TOTAL_GUEST).orElseThrow().substring(0,1);
        driverTools.switchToNative();
        return text;
    }

    @Override
    public String getTotalPricePayAtHotel() {
        driverTools.switchToWebView();
        String text = driverTools.getText(PAY_AT_HOTEL_TOTAL_PRICE).orElseThrow();
        driverTools.switchToNative();
        return text;
    }

    @Override
    public void clickOrderDetailPayAtHotel() {
        driverTools.switchToWebView();
        driverTools.click(PAY_AT_HOTEL_ORDER_DETAIL_ARROW);
        driverTools.switchToNative();
    }

    @Override
    public void exitOrderDetailPayAtHotel() {
        driverTools.click(PAY_AT_HOTEL_CROSS_ORDER_DETAIL);
    }

    @Override
    public void clickPayAtHotelButton() {
        driverTools.click(PAY_WITH_BALANCE);
    }

    @Override
    public String getTotalPriceFromPriceDetail() {
        String totalPrice = driverTools.getText(TOTAL_PRICE_FROM_PRICE_DETAIL).orElseThrow();
        logger.log("total price in price detail : " + totalPrice);
        return totalPrice;
    }

    @Override
    public void clickPayButtonCC() {
        driverTools.switchToNative();
        driverTools.hideKeyboard();
        driverTools.swipeToElement(SUBMIT_CC_BUTTON_NATIVE, Swipe.UP, 5);
        driverTools.swipeUp();
        driverTools.waitForElementToPresence(SUBMIT_CC_BUTTON_NATIVE);
        driverTools.clickElementByCoordinate(SUBMIT_CC_BUTTON_NATIVE);
        if(!driverTools.waitForElementToDisappear(SUBMIT_CC_BUTTON_NATIVE,5)){
            driverTools.clickElementByCoordinate(SUBMIT_CC_BUTTON_NATIVE);
        }
        driverTools.waitForElementToDisappear(SUBMIT_CC_BUTTON_NATIVE);
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
        driverTools.waitForElementToAppear(CAMERA_ALLOW_ACCESS_BUTTON, 5).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(PERMISSION_TYPE_ALLOW_ONCE, 2).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(WHILE_USING_APP_PERMISSION_BUTTON, 2).ifPresent(driverTools::click);
    }

    @Override
    public void submitStripeNative() {
        driverTools.switchToNative();
        if(driverTools.isElementDisplayed(COMPLETE_STRIPE)){
            driverTools.click(COMPLETE_STRIPE);
        }
        driverTools.waitForElementToDisappear(COMPLETE_STRIPE);
        driverTools.waitForElementToDisappear(LOADING_STATE);
    }

    @Override
    public void openWaitingPaymentPage(String orderId, String orderHash) {
        Deeplink deepLink = new Deeplink("ORDER_DETAIL_PAGE_"+orderId,
                "https://mpayment-gatotkaca.tiket.com/next/v4?order_hash="+orderHash+"&order_id="+orderId+"&device_type="+Configs.PLATFORM.toString().toLowerCase()+"&lang=id",
                "http://mpayment.preprod.tiket.com/next/v4?order_hash="+orderHash+"&order_id="+orderId+"&device_type="+Configs.PLATFORM.toString().toLowerCase()+"&lang=id",
                "https://mpayment.tiket.com/next/v4?order_hash="+orderHash+"&order_id="+orderId+"&device_type="+Configs.PLATFORM.toString().toLowerCase()+"&lang=id");
        driverTools.deeplink(deepLink);
        driverTools.waitForElementToAppear(ORDER_ID);
    }
}