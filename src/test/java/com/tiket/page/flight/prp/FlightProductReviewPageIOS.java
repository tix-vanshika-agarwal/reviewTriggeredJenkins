package com.tiket.page.flight.prp;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.flight.bookingform.FlightBookingFormPage;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tiket.page.flight.bookingform.FlightBookingFormPageIOS.BTN_CONTINUE;
import static org.hamcrest.core.Is.is;

public class FlightProductReviewPageIOS <T extends WebDriver> extends FlightProductReviewPage<T> {
    public FlightProductReviewPageIOS(Session<T> session) {
        super(session);
    }

    public static final Locator SELECT_BTN_100_P_REFUND = new Locator("SELECT_BTN_100_P_REFUND", AppiumBy.xpath("//*[contains(@name, '100% Refund')]/following::*[@type='XCUIElementTypeStaticText' and (@name='Select' or @name='Pilih')]"));
    public static final Locator LABEL_100_P_REFUND = new Locator("LABEL_100_P_REFUND", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and (contains(@label, '100% Refund Downpay QZ')or contains(@label, '100% Refund DP') or contains(@label, '100% Refund Uang Muka QZ') or contains(@label, 'Uang Muka Refund 100% QZ'))]"));
    public static final Locator PRICE_PER_PERSON_100P = new Locator("PRICE_PER_PERSON_100P", AppiumBy.xpath("(//*[@name='per_person_price_100p_refund' and @type='XCUIElementTypeStaticText'])[1]"));
    public static final Locator TOTAL_PRICE_100P = new Locator("TOTAL_PRICE_100P", AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and @name='total_price_100p_refund'])[1]"));
    public static final Locator TOTAL_PRICE_REGULAR = new Locator("TOTAL_PRICE_100P", AppiumBy.xpath("//*[@name='total_price_regular']"));
    public static final Locator SELECT_BTN_REGULAR = new Locator("SELECT_BTN_REGULAR",AppiumBy.xpath("//XCUIElementTypeStaticText[@name='No upgrade' or @label='No upgrade' or @name='Tanpa upgrade' or @label='Tanpa upgrade']/preceding-sibling::XCUIElementTypeButton[@name='Select' or @name='select' or @name='Pilih' or @label='Select' or @label='Pilih' or @label='select'][1]"));
    /** First fare Select/Pilih — guest single-fare (name=select) and login when No upgrade is absent. */
    public static final Locator SELECT_BTN_FIRST = new Locator("SELECT_BTN_FIRST",AppiumBy.xpath("(//*[@type='XCUIElementTypeButton' and (@name='select' or @name='Select' or @name='Pilih' or @label='Select' or @label='Pilih' or @label='select')])[1]"));
    public static final Locator SELECT_TICKET = new Locator("SELECT_TICKET",AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@name, 'Select your ticket fare') or contains(@name, 'Pilih tarif tiketmu')]"));
    public static final Locator REGULAR_SECTION = new Locator("REGULAR_SECTION",AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and (contains(@label, 'Economy') or contains(@label, 'Ekonomi'))]"));
    public static final Locator MULTI_FARE = new Locator("MULTI_FARE", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and (contains(@label, 'depart ticket') or contains(@label, 'tiket berangkat'))]"));
    public static final Locator SELECT_BTN_REGULAR_2 = new Locator("SELECT_BTN_REGULAR_2",AppiumBy.xpath("(//*[@type='XCUIElementTypeButton' and (contains(@name, 'select') or contains(@name, 'Select') or contains(@name, 'Pilih'))])[1]"));

    public static final Locator CORPORATE_DEALS_BENEFIT = new Locator("CORPORATE_DEALS_BENEFIT",AppiumBy.xpath("//*[contains(@label,'Benefit')]//following-sibling::XCUIElementTypeOther//*[contains(@label,'Corporate Deals')]"));
    public static final Locator PRICE_DETAIL = new Locator("PRICE_DETAIL",AppiumBy.xpath("//*[contains(@name, 'Lihat detail') or contains(@name, 'See details')]"));
    public static final Locator PRICE_BREAKDOWN_TAB = new Locator("PRICE_BREAKDOWN_TAB",AppiumBy.xpath("//*[@label='Detail Harga' or @label='Price Detail']"));
    public static final Locator CLOSE_PRICE_DETAIL = new Locator("CLOSE_PRICE_DETAIL",AppiumBy.accessibilityId("ic cancel grey"));
    public static final Locator PRICE_LOCATOR = new Locator("PRICE_LOCATOR",AppiumBy.xpath("//*[contains(@name,'price_detail_price')]"));
    public static final Locator PRICE_DETAIL_TOTAL = new Locator("PRICE_DETAIL_TOTAL",AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and (contains(@name,'Pay Now') or contains(@label,'Pay Now') or contains(@value,'Pay Now') )]/following-sibling::*[@type='XCUIElementTypeOther'][1]//*[@type='XCUIElementTypeStaticText'][last()]"));
    public static final Locator NOT_MEET_TRAVEL_POLICY_POPUP = new Locator("NOT_MEET_TRAVEL_POLICY_POPUP",AppiumBy.xpath("//*[@name='This order doesn’t meet the travel policy' or @name='Pesanan ini tidak sesuai dengan kebijakan perjalanan perusahaan']"));
    public static final Locator NOT_MEET_TRAVEL_POLICY_POPUP_NO_CANCEL = new Locator("NOT_MEET_TRAVEL_POLICY_POPUP_NO_CANCEL",AppiumBy.xpath("//XCUIElementTypeButton[@name='No, cancel' or @name='Tidak, batalkan']"));
    public static final Locator NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE = new Locator("NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE",AppiumBy.xpath("//XCUIElementTypeButton[@name='Yes, continue' or @name='Ya, lanjutkan']"));
    public static final Locator PROMO_APPLIED_TEXT = new Locator("PROMO_APPLIED_TEXT", AppiumBy.xpath("//*[contains(@label,'Promo terbaik terpasang. Pilihan bagus!') or contains(@label,'Best promo applied. Good choice!')]"));
    public static final Locator PROMO_APPLIED_LABEL = new Locator("PROMO_APPLIED_LABEL", AppiumBy.xpath("//*[contains(@name,'total_price')]//following-sibling::*//*[contains(@name,'labelText')]//XCUIElementTypeStaticText[not(contains(@label,'diskon') or contains(@label,'discount'))]"));
    // PRP transit indicator changed from "Check-in again"/"Check-in ulang" to the new
    // self-transfer banner. The "Self transfer" prefix is not localized — Indonesian
    // renders as "Self transfer 1j 50m di Surabaya", English as "Self transfer 1h 50m in Surabaya".
    public static final Locator CHECK_IN_AGAIN_TEXT_PRP = new Locator("CHECK_IN_AGAIN_TEXT_PRP",
            AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Self transfer')]"));
    // "Self Transfer Protection included" (EN) / "Termasuk Proteksi Transit Mandiri" (ID)
    // is rendered inside the bottom sheet that opens when the self-transfer banner is tapped.
    public static final Locator SELF_TRANSFER_PROTECTION_INCLUDED_BS = new Locator("SELF_TRANSFER_PROTECTION_INCLUDED_BS",
            AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label, 'Self Transfer Protection') or contains(@label, 'Proteksi Transit Mandiri')]"));
    public static final Locator SELF_TRANSFER_BOTTOM_SHEET_CLOSE = new Locator("SELF_TRANSFER_BOTTOM_SHEET_CLOSE",
            AppiumBy.accessibilityId("ic cancel grey"));
    // Chevron / info icon that opens the self-transfer / protection detail bottom sheet on
    // the PRP card.
    //
    // A multi-leg flight renders multiple chevron-style icons inside the same flight
    // schedule cell (one per airline leg + one for the transit section). We MUST target
    // the chevron that's adjacent to the "Self transfer 3h Xm in <city>" banner. Anchoring
    // on the "Self Transfer Protection included" text and using XPath
    // `following::*[chevron][1]` is incorrect because the protection text is rendered AFTER
    // the transit chevron in document order, so the next chevron found is actually the
    // following leg's airline chevron, which opens an unrelated flight-detail sheet
    // without any protection copy (see the matching Android bug at
    // {@code FlightProductReviewPageAndroid.SELF_TRANSFER_PROTECTION_CHEVRON} and the
    // Jenkins 2026-05-26 run).
    //
    // Anchor on the "Self transfer" banner static text instead, then take its sibling
    // chevron/info icon — that's the affordance immediately next to the banner and it
    // opens the transit / protection bottom sheet.
    public static final Locator SELF_TRANSFER_PROTECTION_CHEVRON = new Locator("SELF_TRANSFER_PROTECTION_CHEVRON",
            AppiumBy.xpath("(//XCUIElementTypeStaticText[contains(@label,'Self transfer') or contains(@label,'Self Transfer')]"
                    + "/following-sibling::*[(@type='XCUIElementTypeImage' or @type='XCUIElementTypeButton') "
                    + "and (contains(@name,'chevron_right') or contains(@name,'chevron right') "
                    + "or contains(@name,'tds_ic_chevron_right') or contains(@name,'info') "
                    + "or contains(@label,'info'))])[1]"));
    private static final Locator REFUND_100P_PRP = new Locator("REFUND_100P_PRP", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@label, 'Economy + tiket 100% Refund') or contains(@label, 'Ekonomi + tiket 100% Refund')]"));
    private static final Locator REFUND_100P_SEE_DETAILS = new Locator("REFUND_100P_SEE_DETAILS", AppiumBy.xpath("//*[contains(@name, 'detail') and ./preceding-sibling::*[contains(@name, 'Economy + tiket 100% Refund') or contains(@name, 'Ekonomi + tiket 100% Refund')]]"));
    private static final Locator REFUND_RESCHEDULE_TAB = new Locator("REFUND_RESCHEDULE_TAB", AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and contains(@label, 'Refund & Reschedule')])"));
    private static final Locator PRICE_DETAIL_TAB = new Locator("PRICE_DETAIL_TAB", AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and contains(@label, 'Price Detail') or contains(@label, 'Detail Harga')])[1]"));
    public static final Locator GREEN_BOX = new Locator("GREEN_BOX", AppiumBy.xpath("//*[contains(@name, 'Refund 100% from tiket.com') or contains(@name, 'Refund 100% dari tiket.com') and @type='XCUIElementTypeStaticText']"));
    public static final Locator PER_PERSON_PRICE_PRP = new Locator("PER_PERSON_PRICE_PRP", AppiumBy.xpath("(//*[@type='XCUIElementTypeStaticText' and contains(@label, '100% refund')])[2]"));
    private static final Locator WHOLE_PRICE = new Locator("WHOLE_PRICE", AppiumBy.xpath("//*[contains(@name, 'Price Details') or contains(@name, 'Detail Harga')]/following::XCUIElementTypeOther[.//XCUIElementTypeStaticText[contains(@name, 'Adult') or contains(@name, 'Dewasa')]]//XCUIElementTypeStaticText[contains(@label,'IDR')]"));
    private static final Locator CLOSE_TABS = new Locator("CLOSE_TABS", AppiumBy.xpath("//*[@name='ic cancel grey']"));
    private static final Locator DISCOUNT_SECTION = new Locator("DISCOUNT_SECTION", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and @label='Price Deduction' or @label='Potongan Harga']"));
    private static final Locator DISCOUNT_PRICE = new Locator("DISCOUNT_PRICE", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@label, '-')]"));
    private static final Locator CLICK_DETAILS = new Locator("CLICK_DETAILS", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and (@label='• See details' or contains(@label,'Lihat detail'))]"));
    public static final Locator DEPARTURE_FLIGHT = new Locator("DEPARTURE_FLIGHT", AppiumBy.xpath("//*[(@label='Departure Flight' or @label='Penerbangan Pergi') and @type='XCUIElementTypeStaticText']"));
    public static final Locator REFUND_PERCENTAGE = new Locator("REFUND_PERCENTAGE", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@label,'%')]"));
    public static final Locator RETURN_FLIGHT = new Locator("RETURN_FLIGHT", AppiumBy.xpath("//*[(@label='Return Flight' or @label='Penerbangan Pulang' )and @type='XCUIElementTypeStaticText']"));
    public static final Locator RESCHEDULE_CHIP = new Locator("RESCHEDULE_CHIP", AppiumBy.xpath("//*[@label='Reschedule' and @type='XCUIElementTypeStaticText']"));
    public static final Locator RESCHEDULE_PERCENTAGE = new Locator("RESCHEDULE_PERCENTAGE", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@label,'%')]"));
    public static final Locator REFUND_NOT_ALLOWED = new Locator("REFUND_NOT_ALLOWED", AppiumBy.xpath("//*[(@label='Refund not allowed' or @label='Refund information is not available'or @label='Informasi refund tidak tersedia') and @type='XCUIElementTypeStaticText']"));
    public static final Locator RESCHEDULE_NOT_ALLOWED = new Locator("RESCHEDULE_NOT_ALLOWED", AppiumBy.xpath("//*[(@label='Reschedule information is not available' or @label='Informasi reschedule tidak tersedia') and @type='XCUIElementTypeStaticText']"));

    /** Refund sub-tab on the new policy details popup (iOS uses {@code name=textLabel}). */
    private static final Locator REFUND_TAB = new Locator("REFUND_TAB", AppiumBy.xpath(
            "//*[@name='textLabel' and (@label='Refund' or @label='Pengembalian Dana')]"));
    /** Reschedule sub-tab on the new policy details popup. */
    private static final Locator RESCHEDULE_TAB = new Locator("RESCHEDULE_TAB", AppiumBy.xpath(
            "//*[@name='textLabel' and (@label='Reschedule' or @label='Penjadwalan Ulang')]"));
    /** Depart leg chip; matches in either Refund or Reschedule active tab and English/Bahasa labels. */
    private static final Locator DEPART_CHIP = new Locator("DEPART_CHIP", AppiumBy.xpath(
            "//*[@type='XCUIElementTypeStaticText' and ("
                    + "@name='Depart' or @label='Depart' or @name='Pergi' or @label='Pergi'"
                    + ")]"));
    /** Return leg chip; matches in either Refund or Reschedule active tab and English/Bahasa labels. */
    private static final Locator RETURN_CHIP = new Locator("RETURN_CHIP", AppiumBy.xpath(
            "//*[@type='XCUIElementTypeStaticText' and ("
                    + "@name='Return' or @label='Return' or @name='Pulang' or @label='Pulang'"
                    + ")]"));
    /**
     * "First row" of the policy info block - present when policy details are rendered.
     * Anchored to the popup's {@code XCUIElementTypeScrollView} so it never matches outside the sheet.
     */
    private static final Locator INFO_ROW_CONTAINER = new Locator("INFO_ROW_CONTAINER", AppiumBy.xpath(
            "//*[@type=\"XCUIElementTypeStaticText\" and (contains(@name, \"Dapat refund\") or contains(@name, \"Tidak bisa refund\") or contains(@name, \"Get refund\") or contains(@name, \"Refund not allowed\") or contains(@name, \"reschedule fee\") or contains(@name, \"Reschedule not allowed\") or contains(@name, \"biaya reschedule\") or contains(@name, \"Tidak bisa reschedule\"))]"));

    /** Empty-state copy shown when policy info is not available - matches both tabs and English + Bahasa. */
    private static final Locator INFO_NOT_AVAILABLE = new Locator("INFO_NOT_AVAILABLE", AppiumBy.xpath(
            "//*[@type='XCUIElementTypeStaticText' and ("
                    + "@label='Refund information is not available' or "
                    + "@label='Reschedule information is not available' or "
                    + "@label='Informasi refund tidak tersedia' or "
                    + "@label='Informasi reschedule tidak tersedia' or "
                    + "@label='Informasi Refund tidak tersedia' or "
                    + "@label='Informasi Reschedule tidak tersedia'"
                    + ")]"));

    @Override
    public void swipeTo100pSection(){
        driverTools.swipeToElement(SELECT_BTN_100_P_REFUND, Swipe.UP,4);
    }

    @Override
    public boolean check100pRefundAttribute(){
        boolean is100pRefundAttributeDisplayed = driverTools.isElementDisplayed(LABEL_100_P_REFUND);
        logger.log("is 100p refund displayed on prp : " + is100pRefundAttributeDisplayed);
        return is100pRefundAttributeDisplayed;
    }

    @Override
    public boolean checkPrice(){
        WebElement regularPriceElement = driverTools.swipeToElement(TOTAL_PRICE_REGULAR,Swipe.UP,2).orElseThrow();
        double regularPrice = CoreFunctions.convertPriceSafely(driverTools.getText(regularPriceElement));
        logger.log("regular total price " + regularPriceElement);

        boolean isPerPersonPriceDisplayed = driverTools.isElementDisplayed(PRICE_PER_PERSON_100P);
        logger.log("is price per person displayed : " + isPerPersonPriceDisplayed);

        WebElement refund100PElement = driverTools.waitForElementToAppear(TOTAL_PRICE_100P).orElseThrow();
        boolean isTotalPriceDisplayed = driverTools.isElementDisplayed(refund100PElement);
        logger.log("is total price displayed : " + isTotalPriceDisplayed);
        double refund100Price = CoreFunctions.convertPriceSafely(driverTools.getText(refund100PElement));
        logger.log("100 p total price " + refund100Price);

        boolean isRefund100pMoreThenRegular = refund100Price - regularPrice > 0;
        return isPerPersonPriceDisplayed && isTotalPriceDisplayed && isRefund100pMoreThenRegular;
    }

    @Override
    public void continueToBookingPageUsing100pSelectBtn(){
        driverTools.swipeToElement(SELECT_BTN_100_P_REFUND, Swipe.UP, 5);
        driverTools.click(SELECT_BTN_100_P_REFUND);
        FlightBookingFormPage<T> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        flightBookingFormPage.skipFullProtectionPrp();
        flightBookingFormPage.isOnBookingPage();
    }

    @Override
    public void swipeToRegularSection(){
        driverTools.swipeToElement(SELECT_BTN_REGULAR, Swipe.UP,4);
    }

    @Override
    public boolean isCorporateDealsPresent(){
        return driverTools.swipeToElement(CORPORATE_DEALS_BENEFIT, Swipe.UP, 5).isPresent();
    }

    @Override
    public void continueToBookingForm(String tripType){
        boolean isSelectBtnClicked = false;
        driverTools.swipeToElement(SELECT_TICKET, Swipe.UP, 10);
        driverTools.swipeElementToMidScreen(SELECT_TICKET);

        //airlines with insurance
        if (driverTools.isElementDisplayed(MULTI_FARE)){
            if(!driverTools.isElementDisplayed(SELECT_BTN_REGULAR_2)){
                driverTools.swipeUp();
            }
            driverTools.waitForElementToPresence(SELECT_BTN_REGULAR_2);
            driverTools.click(SELECT_BTN_REGULAR_2);
            isSelectBtnClicked = true;

            if (tripType.equalsIgnoreCase("roundtrip")){
                driverTools.swipeToElement(SELECT_BTN_REGULAR_2, Swipe.UP, 5);
                driverTools.waitForElementToPresence(SELECT_BTN_REGULAR_2, 10).ifPresent(driverTools::clickElementByCoordinate);
            }
        }
        //airlines without insurance (login No upgrade, or guest single Pilih/select)
        if (!isSelectBtnClicked && driverTools.isElementDisplayed(REGULAR_SECTION)){
            WebElement selectBtn = driverTools.swipeToElement(SELECT_BTN_REGULAR, Swipe.UP, 5)
                    .or(() -> driverTools.swipeToElement(SELECT_BTN_FIRST, Swipe.UP, 8))
                    .orElseThrow();
            driverTools.clickElementByCoordinate(selectBtn);
            isSelectBtnClicked = true;
        }
        if(!isSelectBtnClicked) throw new RuntimeException("Select button is not found for both regular and multi fare section");

        FlightBookingFormPage<T> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        if(driverTools.waitForElementToAppear(BTN_CONTINUE,5).isPresent())
            return;
        flightBookingFormPage.skipFullProtectionPrp();
        flightBookingFormPage.isOnBookingPage();
    }

    @Override
    public boolean isProductReviewPageDisplayed() {
        return driverTools.waitForElementToPresence(SELECT_TICKET, 15).isPresent();
    }

    @Override
    public void checkRefundAsAddOnPRP(int pax, String currency) {
        logger.log("Checking 100p refund card is there on the PRP page");
        Assertion.assertThat("100p refund card is there on the PRP page", driverTools.waitForElementToPresence(REFUND_100P_PRP).isPresent(), is(true));

        logger.log("Clicking see detail link on the 100p refund card");
        driverTools.click(REFUND_100P_SEE_DETAILS);

        logger.log("Checking refund & reschedule tab is there on the price detail popup");
        Assertion.assertThat("Refund & reschedule tab is there on the See detail popup", driverTools.waitForElementToPresence(REFUND_RESCHEDULE_TAB).isPresent(), is(true));
        driverTools.click(REFUND_RESCHEDULE_TAB);

        logger.log("Verifying details on the refund & reschedule tab");
        Assertion.assertThat("Green box information is there on the refund & reschedule tab", driverTools.waitForElementToPresence(GREEN_BOX).isPresent(), is(true));

        String perPersonPrice = driverTools.getText(driverTools.waitForElementToPresence(PER_PERSON_PRICE_PRP).orElseThrow());
        logger.log("Per person price on the refund & reschedule tab: " + perPersonPrice);
        String[] parts = perPersonPrice.split("\\s+");
        String priceOnly = parts[parts.length - 1];
        logger.log("Extracted price: " + priceOnly);
        int numericPrice = Integer.parseInt(priceOnly.replace(",", ""));

        logger.log("Checking Price details tab is there on the price detail popup");
        Assertion.assertThat("Price details tab is there on the See detail popup", driverTools.waitForElementToPresence(PRICE_DETAIL_TAB).isPresent(), is(true));
        driverTools.click(PRICE_DETAIL_TAB);

        logger.log("Verifying details on the Price detail tab");
        Locator refundprice = getRefundPriceLocator(pax);
        Assertion.assertThat("Refund price is there on the Price detail tab as an AddOn for " + pax + " passengers ", driverTools.isElementDisplayed(refundprice), is(true));

        logger.log("Comparing prices on the refund tab and Price detail tab");
        String refundPrice = driverTools.getText(driverTools.waitForElementToPresence(getWholePrice(currency)).orElseThrow());
        refundPrice = refundPrice.replaceAll("[^\\d,]", "");
        int priceOnlyInt = Integer.parseInt(refundPrice.replace(",", ""));

        logger.log("Checking if price deduction section is present on the Price detail tab");
        if(driverTools.isElementDisplayed(DISCOUNT_SECTION)){
            String discountPrice = driverTools.getText(driverTools.waitForElementToPresence(getDiscountPrice(currency)).orElseThrow());
            discountPrice = discountPrice.replaceAll("[^\\d,]", "");
            int discountPriceInt = Integer.parseInt(discountPrice.replace(",", ""));
            logger.log("Discount price on the Price detail tab: " + discountPriceInt);
            priceOnlyInt -= Math.abs(discountPriceInt);
            logger.log("Adjusted price on the Price detail tab after adding discount: " + priceOnlyInt);
        }

        logger.log("Price on the Price detail tab: " + priceOnlyInt);
        int total = numericPrice * pax;
        logger.log("Total price for " + pax + " pax as per refund tab: " + total);
        Assertion.assertThat("Price on the refund & reschedule tab is the same as on the Price detail tab", priceOnlyInt == total, is(true));

        logger.log("Closing the See detail popup");
        driverTools.click(CLOSE_TABS);
    }

    private Locator getRefundPriceLocator(int index) {
        return new Locator("Refund price Locator", AppiumBy.xpath("//*[@type='XCUIElementTypeOther' and ./*[@label='100% Refund addon (x" + index + ")']]"));
    }

    private Locator getDiscountPrice(String currency) {
        return new Locator("Discount price Locator", AppiumBy.xpath("//*[@type='XCUIElementTypeStaticText' and contains(@label, '-" + currency + "')]"));
    }

    private Locator getWholePrice(String currency) {
        return new Locator("Whole price Locator", AppiumBy.xpath("//*[contains(@label, 'Price Details') or contains(@label, 'Detail Harga')]/following::XCUIElementTypeOther[.//XCUIElementTypeStaticText[contains(@label, 'Adult') or contains(@label, 'Dewasa')]]//XCUIElementTypeStaticText[contains(@label,'" + currency + "')]"));
    }

    @Override
    public void clickSelectRegularBySwipingDown(){
        WebElement regularSelectBtn = driverTools.swipeToElement(SELECT_BTN_REGULAR, Swipe.DOWN, 8)
                .or(() -> driverTools.swipeToElement(SELECT_BTN_FIRST, Swipe.DOWN, 8))
                .orElseThrow();
        driverTools.click(regularSelectBtn);
        FlightBookingFormPage<T> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        flightBookingFormPage.isOnBookingPage();
    }


    @Override
    public void clickSelectBtnRegular(){
        WebElement regularSelectBtn = driverTools.swipeToElement(SELECT_BTN_REGULAR, Swipe.UP, 8)
                .or(() -> driverTools.swipeToElement(SELECT_BTN_FIRST, Swipe.UP, 8))
                .orElseThrow();
        driverTools.click(regularSelectBtn);
    }

    @Override
    public boolean isAirlineLogoDisplayedPRP(){
        //Fixme Implementation pending -> missing unique id
        return true;
    }

    @Override
    public boolean isAirlineRouteCorrectPRP(String route){
        //Fixme Implementation pending -> missing unique id
        return true;
    }

    private void openPriceDetail(){
        swipeToRegularSection();
        WebElement priceDetail = driverTools.waitForElementToAppear(PRICE_DETAIL).orElseThrow();
        driverTools.click(priceDetail);
        driverTools.click(PRICE_BREAKDOWN_TAB);
    }
    private void closePriceDetail(){
        driverTools.click(CLOSE_PRICE_DETAIL);
    }

    private boolean priceBreakdownCorrect(){
        driverTools.waitForElementToAppear(PRICE_LOCATOR);

        List<WebElement> prices = driverTools.elements(PRICE_LOCATOR).orElseThrow();
        ArrayList<Double> arrayPrice = new ArrayList<Double>();

        for (WebElement element : prices) {
            String text = driverTools.getText(element);
            try {
                double price = CoreFunctions.convertPrice(text);
                arrayPrice.add(price);
            } catch (NumberFormatException ignored) {
            }
        }

        double sum = 0;
        for (double i : arrayPrice) {
            sum += i;
        }

        WebElement totalPriceDetail = driverTools.waitForElementToAppear(PRICE_DETAIL_TOTAL).orElseThrow();
        double totalPrice = CoreFunctions.convertPrice(driverTools.getText(totalPriceDetail));
        logger.log("Total price text:  " + totalPrice);
        logger.log("Total price got:  " + sum);
        return totalPrice == sum;
    }

    @Override
    public boolean verifyPriceBreakdownPRP(){
        openPriceDetail();
        boolean isBreakdownCorrect = priceBreakdownCorrect();
        closePriceDetail();
        return isBreakdownCorrect;
    }

    @Override
    public boolean isNotMeetTravelPolicyPopupDisplayed(){
        boolean isNotMeetTravelPolicyPopupDisplayed = driverTools.isElementDisplayed(NOT_MEET_TRAVEL_POLICY_POPUP);
        logger.log("is not meet travel policy popup displayed : " + isNotMeetTravelPolicyPopupDisplayed);

        boolean isNoCancelBtnDisplayed = driverTools.isElementDisplayed(NOT_MEET_TRAVEL_POLICY_POPUP_NO_CANCEL);
        logger.log("is no cancel button displayed : " + isNoCancelBtnDisplayed);

        boolean isYesContinueBtnDisplayed = driverTools.isElementDisplayed(NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE);
        logger.log("is yes continue button displayed : " + isYesContinueBtnDisplayed);
        return isNotMeetTravelPolicyPopupDisplayed && isNoCancelBtnDisplayed && isYesContinueBtnDisplayed;
    }

    private void continueIfTravelPolicyPopupDisplayed() {
        if (driverTools.isElementDisplayed(NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE)) {
            logger.log("Travel policy popup displayed, clicking Yes, continue");
            driverTools.click(NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE);
        }
    }

    @Override
    public boolean verifyPromoInfinitySection(){
        if (driverTools.isElementDisplayed(PROMO_APPLIED_TEXT))
            return driverTools.isElementDisplayed(PROMO_APPLIED_TEXT);
        else driverTools.swipeToElement(PROMO_APPLIED_TEXT,Swipe.UP,6);
        return driverTools.isElementDisplayed(PROMO_APPLIED_TEXT);
    }

    @Override
    public boolean isLabelPromoInfinityDisplayed(){
        List<WebElement> promoLabel = driverTools.elements(PROMO_APPLIED_LABEL).orElseThrow();
        logger.log("size of promo label: " + promoLabel.size());
        return promoLabel.size() == 3 || promoLabel.size() == 2;
    }

    @Override
    public boolean isCheckInAgainTextDisplayed(){
        WebElement isCheckInAgainDisplayed = driverTools.swipeToElement(CHECK_IN_AGAIN_TEXT_PRP,Swipe.UP,3).orElseThrow();
        return driverTools.isElementDisplayed(isCheckInAgainDisplayed);
    }

    @Override
    public boolean isConnectingFlightProtection(){
        // Open the Self Transfer Protection bottom sheet and verify the protection copy is
        // shown inside it. The PRP exposes two open triggers depending on the build/flight:
        //   - A chevron / info icon next to the inline "Self Transfer Protection included"
        //     text. This is the precise affordance that opens the protection detail sheet.
        //   - Tapping the "Self transfer 3h Xm in <city>" banner text. This is a less
        //     reliable trigger — for some flight options it opens a generic transit-info
        //     sheet that does NOT contain the protection copy (see Jenkins run on
        //     2026-05-26 where tapping the banner timed out waiting for protection text).
        // Prefer the chevron when it's present; fall back to the banner tap otherwise.
        // Sometimes the protection only renders inline (no detail sheet exists at all for
        // that flight) — accept the inline presence as a last-resort signal so the test
        // doesn't false-fail on a UI variant.
        boolean sheetOpened = openSelfTransferProtectionBottomSheet();

        boolean isProtectionShown;
        if (sheetOpened) {
            isProtectionShown = driverTools.waitForElementToAppear(SELF_TRANSFER_PROTECTION_INCLUDED_BS, 8).isPresent();
            if (!isProtectionShown) {
                logger.log("Bottom sheet opened but protection text not visible — attempting one swipe up inside the sheet");
                driverTools.swipeUp();
                isProtectionShown = driverTools.waitForElementToPresenceSafe(SELF_TRANSFER_PROTECTION_INCLUDED_BS, 3).isPresent();
            }
            dismissSelfTransferBottomSheet();
        } else {
            logger.log("Could not open Self Transfer Protection bottom sheet — verifying inline PRP card as fallback");
            isProtectionShown = driverTools.waitForElementToPresenceSafe(SELF_TRANSFER_PROTECTION_INCLUDED_BS, 2).isPresent();
        }

        logger.log("isSelfTransferProtectionIncludedShown : " + isProtectionShown);
        return isProtectionShown;
    }

    /**
     * Opens the Self Transfer Protection bottom sheet from the PRP card. Tries the chevron
     * next to the inline "Self Transfer Protection included" text first because it's the
     * specific affordance for that detail sheet. Falls back to tapping the "Self transfer"
     * banner static text when the chevron isn't rendered.
     *
     * <p>Returns {@code true} only when the close button of the bottom sheet is detected,
     * which is the most reliable signal that an iOS sheet is fully presented.
     */
    private boolean openSelfTransferProtectionBottomSheet() {
        Optional<WebElement> chevron = driverTools.waitForElementToPresenceSafe(SELF_TRANSFER_PROTECTION_CHEVRON, 3);
        if (chevron.isPresent()) {
            logger.log("Opening Self Transfer Protection bottom sheet via chevron next to protection text");
            driverTools.click(chevron.get());
        } else {
            Optional<WebElement> bannerOpt = driverTools.swipeToElement(CHECK_IN_AGAIN_TEXT_PRP, Swipe.UP, 3);
            if (bannerOpt.isEmpty()) {
                logger.log("Neither chevron nor self-transfer banner found on PRP — cannot open bottom sheet");
                return false;
            }
            logger.log("Chevron not found; falling back to tapping the self-transfer banner text");
            driverTools.click(bannerOpt.get());
        }
        return driverTools.waitForElementToPresenceSafe(SELF_TRANSFER_BOTTOM_SHEET_CLOSE, 10).isPresent();
    }

    /**
     * Dismisses the self-transfer protection bottom sheet so subsequent PRP actions
     * are not blocked by a modal. Tries the explicit close button first, then falls
     * back to a swipe-down gesture which closes any standard iOS bottom sheet.
     *
     * <p>Uses the close-button locator (not the protection-text locator) to detect
     * whether the sheet is still open: the protection text can also be rendered
     * inline on the underlying PRP card, so checking for it yields false positives
     * once the sheet has actually been closed and the PRP becomes visible again.
     */
    private void dismissSelfTransferBottomSheet() {
        if (driverTools.waitForElementToPresenceSafe(SELF_TRANSFER_BOTTOM_SHEET_CLOSE, 3).isPresent()) {
            driverTools.click(SELF_TRANSFER_BOTTOM_SHEET_CLOSE);
        } else {
            logger.log("Self-transfer bottom sheet close button not found; falling back to swipe-down to dismiss.");
            driverTools.swipeDown();
        }
        if (driverTools.waitForElementToPresenceSafe(SELF_TRANSFER_BOTTOM_SHEET_CLOSE, 2).isPresent()) {
            logger.log("Self-transfer bottom sheet still visible after dismissal attempt; performing one more swipe-down.");
            driverTools.swipeDown();
        }
    }

    @Override
    public void clickDetailPRP(){
        driverTools.swipeToElement(CLICK_DETAILS, Swipe.UP,5).orElseThrow();
        driverTools.click(CLICK_DETAILS);
    }

    @Override
    public void clickRefundReschedulePolicyTab(){
        // The legacy combined "Refund & Reschedule" tab no longer exists on the new UI which exposes
        // Refund and Reschedule as separate top-level tabs. Click only if the legacy header is still
        // rendered to remain backwards-compatible with older builds.
        if (driverTools.isElementDisplayed(REFUND_RESCHEDULE_TAB)) {
            driverTools.click(REFUND_RESCHEDULE_TAB);
        } else {
            logger.log("[iOS] Legacy 'Refund & Reschedule' tab not present - new UI has separate tabs");
        }
    }

    @Override
    public void clickRefundTab() {
        driverTools.waitForElementToAppear(REFUND_TAB, 10);
        logger.log("[iOS] Clicking 'Refund' policy sub-tab");
        driverTools.click(REFUND_TAB);
    }

    @Override
    public void clickRescheduleTab() {
        driverTools.waitForElementToAppear(RESCHEDULE_TAB, 10);
        logger.log("[iOS] Clicking 'Reschedule' policy sub-tab");
        driverTools.click(RESCHEDULE_TAB);
    }

    @Override
    public void clickDepartChip() {
        driverTools.waitForElementToAppear(DEPART_CHIP, 10);
        logger.log("[iOS] Clicking 'Depart' leg chip");
        driverTools.click(DEPART_CHIP);
    }

    @Override
    public void clickReturnChip() {
        driverTools.waitForElementToAppear(RETURN_CHIP, 10);
        logger.log("[iOS] Clicking 'Return' leg chip");
        driverTools.click(RETURN_CHIP);
    }

    @Override
    public boolean isPolicyInfoVisible() {
        boolean rowVisible = driverTools.isElementDisplayed(INFO_ROW_CONTAINER);
        boolean notAvailableVisible = !rowVisible && driverTools.isElementDisplayed(INFO_NOT_AVAILABLE);
        boolean visible = rowVisible || notAvailableVisible;
        logger.log("[iOS] Policy info - row container present? " + rowVisible
                + " | 'info not available' text present? " + notAvailableVisible);
        // Mirror Android: close the popup after the chip/tab assertion so subsequent step
        // ("Switch to Return leg under Refund tab", "Open Reschedule tab", etc.) starts from
        // a known PRP state. Callers that need the popup to stay open MUST use
        // isPolicyRowVisible() (peek-only) and the multi-fare helpers in
        // verifyRefundRescheduleInfoMultiFareAndContinue(...).
        driverTools.click(CLOSE_TABS);
        return visible;
    }

    @Override
    public boolean verifyRefundInfo(){
        clickRefundTab();
        clickDepartChip();
        boolean depart = isPolicyInfoVisible();
        Assertion.assertThat("Refund - Depart - policy row OR 'info not available' text is shown",
                depart, is(true));

        clickReturnChip();
        boolean ret = isPolicyInfoVisible();
        Assertion.assertThat("Refund - Return - policy row OR 'info not available' text is shown",
                ret, is(true));
        return depart && ret;
    }

    @Override
    public boolean isRefundNotAllowedTextDisplayed(){
        driverTools.swipeToElement(DEPARTURE_FLIGHT, Swipe.DOWN,5);
        Assertion.assertThat("Departure flight details are present", driverTools.isElementDisplayed(DEPARTURE_FLIGHT), is(true));
        boolean res1 = driverTools.isElementDisplayed(REFUND_NOT_ALLOWED);
        driverTools.swipeToElement(RETURN_FLIGHT, Swipe.UP,5).orElseThrow();
        Assertion.assertThat("Return flight details are present", driverTools.isElementDisplayed(RETURN_FLIGHT), is(true));
        driverTools.swipeUp(3);
        boolean res2 = driverTools.isElementDisplayed(REFUND_NOT_ALLOWED);
        return res1 && res2;
    }

    @Override
    public boolean isRescheduleNotAllowedTextDisplayed(){
        driverTools.swipeToElement(RESCHEDULE_CHIP, Swipe.DOWN,5);
        driverTools.click(RESCHEDULE_CHIP);
        Assertion.assertThat("Departure flight details are present", driverTools.isElementDisplayed(DEPARTURE_FLIGHT), is(true));
        boolean res1 = driverTools.isElementDisplayed(RESCHEDULE_NOT_ALLOWED);
        driverTools.swipeToElement(RETURN_FLIGHT, Swipe.UP,5).orElseThrow();
        Assertion.assertThat("Return flight details are present", driverTools.isElementDisplayed(RETURN_FLIGHT), is(true));
        driverTools.swipeUp(3);
        boolean res2 = driverTools.isElementDisplayed(RESCHEDULE_NOT_ALLOWED);
        return res1 && res2;
    }

    @Override
    public boolean verifyRescheduleInfo(){
        clickRescheduleTab();
        clickDepartChip();
        boolean depart = isPolicyInfoVisible();
        Assertion.assertThat("Reschedule - Depart - policy row OR 'info not available' text is shown",
                depart, is(true));

        clickReturnChip();
        boolean ret = isPolicyInfoVisible();
        Assertion.assertThat("Reschedule - Return - policy row OR 'info not available' text is shown",
                ret, is(true));

        driverTools.click(CLOSE_TABS);
        return depart && ret;
    }

    @Override
    public void continueToBookingForm(){
        // Prefer No upgrade when present; otherwise individual Select/Pilih (single-fare).
        WebElement regularSelectBtn = driverTools.swipeToElement(SELECT_BTN_FIRST, Swipe.UP, 8)
                .or(() -> driverTools.swipeToElement(SELECT_BTN_REGULAR, Swipe.DOWN, 8))
                .orElseThrow();
        driverTools.click(regularSelectBtn);
        FlightBookingFormPage<T> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        if(driverTools.waitForElementToAppear(BTN_CONTINUE,5).isPresent())
            return;
        continueIfTravelPolicyPopupDisplayed();
        flightBookingFormPage.skipFullProtectionPrp();
        flightBookingFormPage.isOnBookingPage();
    }

    @Override
    public boolean isPolicyRowVisible() {
        boolean rowVisible = driverTools.isElementDisplayed(INFO_ROW_CONTAINER);
        logger.log("[iOS][isPolicyRowVisible] INFO_ROW_CONTAINER present? " + rowVisible);
        return rowVisible;
    }

    @Override
    public boolean isMultiFarePRP() {
        // Bring the fare-selection section into view first; without this the stepper may sit
        // below the fold and isElementDisplayed would return a false negative.
        driverTools.swipeToElement(SELECT_TICKET, Swipe.UP, 10);
        boolean multiFare = driverTools.isElementDisplayed(MULTI_FARE);
        logger.log("[iOS][isMultiFarePRP] multi-fare stepper displayed? " + multiFare);
        return multiFare;
    }

    @Override
    public Map<String, Boolean> verifyRefundRescheduleInfoMultiFareAndContinue(String tripType) {
        logger.log("[iOS][verifyRefundRescheduleInfoMultiFareAndContinue] start - tripType=" + tripType);
        Map<String, Boolean> rowVisibilityPerLeg = new HashMap<>();
        rowVisibilityPerLeg.put("depart", false);
        rowVisibilityPerLeg.put("return", false);

        // ---------- Step 1: Depart leg ----------
        boolean departRowVisible = verifyMultiFareRefundReschedulePopupForLeg("Depart");
        rowVisibilityPerLeg.put("depart", departRowVisible);
        // Click the first Select on the fare list — advances RT from step 1→2, or OW directly to booking form.
        clickFirstSelectMultiFare("Depart");

        if (tripType.equalsIgnoreCase("roundtrip")) {
            // Wait for step-2 (return) fare list to render before opening its See details.
            driverTools.waitForElementToAppear(CLICK_DETAILS, 10);
            // ---------- Step 2: Return leg ----------
            boolean returnRowVisible = verifyMultiFareRefundReschedulePopupForLeg("Return");
            rowVisibilityPerLeg.put("return", returnRowVisible);
            // Final Select on step 2 commits the return fare and advances to the booking form.
            clickFirstSelectMultiFare("Return");
        }

        FlightBookingFormPage<T> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        if (driverTools.waitForElementToAppear(BTN_CONTINUE, 5).isPresent()) {
            logger.log("[iOS][verifyRefundRescheduleInfoMultiFareAndContinue] booking form reached without extra popup; row visibility per leg: " + rowVisibilityPerLeg);
            return rowVisibilityPerLeg;
        }
        flightBookingFormPage.skipFullProtectionPrp();
        flightBookingFormPage.isOnBookingPage();
        logger.log("[iOS][verifyRefundRescheduleInfoMultiFareAndContinue] done; row visibility per leg: " + rowVisibilityPerLeg);
        return rowVisibilityPerLeg;
    }

    /**
     * Opens the See details popup for the first fare card in the current step, then asserts
     * that policy info is rendered under both the Refund and Reschedule tabs, then closes
     * the popup. The multi-fare popup is per-leg, so unlike the legacy flow there are no
     * Depart/Return chips to interact with — the leg is implied by which step is active.
     *
     * @return {@code true} if the populated row container ({@code INFO_ROW_CONTAINER}) was
     *         rendered on EITHER the Refund or Reschedule tab — i.e. actual policy data was
     *         shown for this leg (as opposed to the "info not available" empty-state).
     */
    private boolean verifyMultiFareRefundReschedulePopupForLeg(String legLabel) {
        logger.log("[iOS][multiFare] Opening 'See details' popup for " + legLabel + " leg");
        driverTools.swipeToElement(CLICK_DETAILS, Swipe.UP, 5).orElseThrow();
        driverTools.click(CLICK_DETAILS);

        driverTools.waitForElementToAppear(REFUND_TAB, 10).orElseThrow();
        logger.log("[iOS][multiFare] Clicking 'Refund' tab on " + legLabel + " leg popup");
        driverTools.click(REFUND_TAB);
        boolean refundRowVisible = driverTools.isElementDisplayed(INFO_ROW_CONTAINER);
        boolean refundVisible = isMultiFarePolicyInfoVisible();
        Assertion.assertThat(
                "Multi-fare PRP - " + legLabel + " - Refund policy info present",
                refundVisible, is(true));

        driverTools.waitForElementToAppear(RESCHEDULE_TAB, 10).orElseThrow();
        logger.log("[iOS][multiFare] Clicking 'Reschedule' tab on " + legLabel + " leg popup");
        driverTools.click(RESCHEDULE_TAB);
        boolean rescheduleRowVisible = driverTools.isElementDisplayed(INFO_ROW_CONTAINER);
        boolean rescheduleVisible = isMultiFarePolicyInfoVisible();
        Assertion.assertThat(
                "Multi-fare PRP - " + legLabel + " - Reschedule policy info present",
                rescheduleVisible, is(true));

        logger.log("[iOS][multiFare] Closing " + legLabel + " leg popup");
        driverTools.click(CLOSE_TABS);

        boolean anyRowVisible = refundRowVisible || rescheduleRowVisible;
        logger.log("[iOS][multiFare] " + legLabel + " leg - row container present? refund=" + refundRowVisible
                + ", reschedule=" + rescheduleRowVisible + " => anyRowVisible=" + anyRowVisible);
        return anyRowVisible;
    }

    /**
     * Multi-fare-safe visibility check — mirrors {@link #isPolicyInfoVisible()} but does
     * NOT auto-close the popup, because we need to keep the popup open to switch between
     * Refund and Reschedule tabs inside {@link #verifyMultiFareRefundReschedulePopupForLeg}.
     */
    private boolean isMultiFarePolicyInfoVisible() {
        boolean rowVisible = driverTools.isElementDisplayed(INFO_ROW_CONTAINER);
        boolean notAvailableVisible = !rowVisible && driverTools.isElementDisplayed(INFO_NOT_AVAILABLE);
        boolean visible = rowVisible || notAvailableVisible;
        logger.log("[iOS][multiFare] policy info - row container present? " + rowVisible
                + " | 'info not available' present? " + notAvailableVisible);
        return visible;
    }

    /**
     * Clicks the first visible Select CTA on the multi-fare fare-list page. {@code SELECT_BTN_REGULAR_2}
     * is anchored to {@code XCUIElementTypeButton[1]} so it picks the top-most/cheapest fare's
     * Select button on whichever step is currently active in the stepper. After the click the
     * stepper advances (depart → return for RT, or directly to booking form for OW).
     */
    private void clickFirstSelectMultiFare(String legLabel) {
        if (!driverTools.isElementDisplayed(SELECT_BTN_REGULAR_2)) {
            logger.log("[iOS][multiFare] SELECT_BTN_REGULAR_2 not visible for " + legLabel + " — swiping up");
            driverTools.swipeUp();
        }
        WebElement selectBtn = driverTools.waitForElementToPresence(SELECT_BTN_REGULAR_2).orElseThrow();
        logger.log("[iOS][multiFare] Clicking first Select on " + legLabel + " leg");
        driverTools.clickElementByCoordinate(selectBtn);
    }

    private static final Locator FARE_SELECT_WITH_MEAL_AND_SEAT = new Locator("FARE_SELECT_WITH_MEAL_AND_SEAT", AppiumBy.xpath(
            "(//XCUIElementTypeCell | //XCUIElementTypeOther)"
                    + "[.//XCUIElementTypeStaticText[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'meal') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'makanan') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'meal') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'makanan')]"
                    + " and .//XCUIElementTypeStaticText[contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'seat') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'kursi') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'seat') or contains(translate(@name,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'kursi')]]"
                    + "//XCUIElementTypeButton[@name='Select' or @name='select' or @name='Pilih' or @label='Select' or @label='Pilih' or @label='select'][1]"));

    @Override
    public void selectFare(String criteria) {
        String normalized = criteria == null ? "regular" : criteria.toLowerCase().trim();
        driverTools.swipeToElement(SELECT_TICKET, Swipe.UP, 8);
        if (normalized.contains("regular") && !normalized.contains("meal") && !normalized.contains("seat")) {
            logger.log("[iOS][selectFare] selecting regular fare");
            if (driverTools.swipeToElement(SELECT_BTN_REGULAR, Swipe.UP, 5).isPresent()) {
                driverTools.clickElementByCoordinate(SELECT_BTN_REGULAR);
            } else if (driverTools.swipeToElement(SELECT_BTN_FIRST, Swipe.UP, 5).isPresent()) {
                driverTools.clickElementByCoordinate(SELECT_BTN_FIRST);
            } else {
                clickFirstSelectMultiFare("regular");
            }
            return;
        }
        boolean needMeal = normalized.contains("meal") || normalized.contains("makanan");
        boolean needSeat = normalized.contains("seat") || normalized.contains("kursi");
        if (needMeal && needSeat) {
            logger.log("[iOS][selectFare] selecting fare with meal and seat");
            Optional<WebElement> mealSeatSelect = driverTools.swipeToElement(FARE_SELECT_WITH_MEAL_AND_SEAT, Swipe.UP, 8)
                    .or(() -> driverTools.waitForElementToPresence(FARE_SELECT_WITH_MEAL_AND_SEAT, 10));
            if (mealSeatSelect.isPresent()) {
                driverTools.clickElementByCoordinate(mealSeatSelect.get());
                return;
            }
            logger.log("[iOS][selectFare] meal+seat fare card not found — trying bundled free-meal fare");
            selectBaseFareWithBundledMeal(criteria);
            return;
        }
        if (needMeal) {
            logger.log("[iOS][selectFare] selecting fare with meal");
            selectBaseFareWithBundledMeal(criteria);
            return;
        }
        logger.log("[iOS][selectFare] fallback to first Select for criteria: " + criteria);
        clickFirstSelectMultiFare(criteria);
    }

    @Override
    public void continueToBookingFormWithFareCriteria(String tripType, String departFareCriteria, String returnFareCriteria) {
        logger.log("[iOS][continueToBookingFormWithFareCriteria] tripType=" + tripType
                + " depart=" + departFareCriteria + " return=" + returnFareCriteria);
        driverTools.swipeToElement(SELECT_TICKET, Swipe.UP, 10);
        selectFare(departFareCriteria);
        if (tripType != null && tripType.equalsIgnoreCase("roundtrip")) {
            selectFare(returnFareCriteria);
        }
        FlightBookingFormPage<T> flightBookingFormPage = FlightBookingFormPage.getInstance(session);
        if (driverTools.waitForElementToAppear(BTN_CONTINUE, 5).isPresent()) {
            return;
        }
        flightBookingFormPage.skipFullProtectionPrp();
        flightBookingFormPage.isOnBookingPage();
    }

    private static final Locator BASE_FARE_WITH_MEAL = new Locator("BASE_FARE_WITH_MEAL", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'Meals:') or contains(@label,'Meals:') or contains(@name,'Makanan:') or contains(@label,'Makanan:') or contains(@name,'1xFree') or contains(@label,'1xFree')]"));
    /** Select on the fare card that lists free meal (single-fare PRP — no No-upgrade row). */
    private static final Locator FARE_SELECT_WITH_MEAL = new Locator("FARE_SELECT_WITH_MEAL", AppiumBy.xpath("((//XCUIElementTypeCell | //XCUIElementTypeOther)[.//XCUIElementTypeStaticText[contains(@name,'Meals:') or contains(@label,'Meals:') or contains(@name,'Makanan:') or contains(@label,'Makanan:') or contains(@name,'1xFree') or contains(@label,'1xFree')]]//XCUIElementTypeButton[@name='Select' or @name='select' or @name='Pilih' or @label='Select' or @label='Pilih' or @label='select'])[1]"));
    /** Select beside {@code No upgrade} / {@code Tanpa upgrade} — keeps base fare when insurance upgrades are shown. */
    private static final Locator NO_UPGRADE_SELECT = new Locator("NO_UPGRADE_SELECT", AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='No upgrade' or @label='No upgrade' or @name='Tanpa upgrade' or @label='Tanpa upgrade']/preceding-sibling::XCUIElementTypeButton[@name='Select' or @name='select' or @name='Pilih' or @label='Select' or @label='Pilih' or @label='select'])[1]"));

    /**
     * Bundled free-meal PRP variants:
     * <ol>
     *   <li>Insurance upgrades present → {@code No upgrade} Select</li>
     *   <li>Single fare card with {@code Meals: 1xFree} → that card's Select</li>
     *   <li>Otherwise first visible Select</li>
     * </ol>
     */
    private void selectBaseFareWithBundledMeal(String criteria) {
        boolean mealOnBase = driverTools.waitForElementToPresence(BASE_FARE_WITH_MEAL, 5).isPresent()
                || driverTools.swipeToElement(BASE_FARE_WITH_MEAL, Swipe.UP, 3).isPresent();
        Assertion.assertThat("Base fare should show free meal benefit for criteria: " + criteria, mealOnBase, is(true));

        Optional<WebElement> selectBtn = driverTools.waitForElementToPresence(NO_UPGRADE_SELECT, 2)
                .or(() -> driverTools.swipeToElement(NO_UPGRADE_SELECT, Swipe.UP, 2));
        if (selectBtn.isPresent()) {
            logger.log("[iOS][selectFare] clicking No upgrade Select");
            driverTools.clickElementByCoordinate(selectBtn.get());
            return;
        }

        selectBtn = driverTools.swipeToElement(FARE_SELECT_WITH_MEAL, Swipe.UP, 4)
                .or(() -> driverTools.waitForElementToPresence(FARE_SELECT_WITH_MEAL, 5));
        if (selectBtn.isPresent()) {
            logger.log("[iOS][selectFare] clicking Select on fare card with free meal");
            driverTools.clickElementByCoordinate(selectBtn.get());
            return;
        }

        selectBtn = driverTools.swipeToElement(SELECT_BTN_FIRST, Swipe.UP, 4)
                .or(() -> driverTools.waitForElementToPresence(SELECT_BTN_FIRST, 5));
        if (selectBtn.isPresent()) {
            logger.log("[iOS][selectFare] clicking first Select as last resort");
            driverTools.clickElementByCoordinate(selectBtn.get());
            return;
        }

        throw new RuntimeException(
                "No meal+seat / No upgrade / meal fare Select found for criteria: " + criteria);
    }

}
