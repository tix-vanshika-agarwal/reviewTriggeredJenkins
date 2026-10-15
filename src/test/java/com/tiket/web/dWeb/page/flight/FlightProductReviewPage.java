package com.tiket.web.dWeb.page.flight;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.ttd.SupplierId;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

public class FlightProductReviewPage <T extends WebDriver> extends BasePage<T> {
    public FlightProductReviewPage(Session<T> session) {
        super(session);
    }

    private static final Locator PRODUCT_REVIEW_PAGE_CHECK = new Locator("PRODUCT_REVIEW_PAGE_CHECK", new By.ByXPath("//div[contains(@class,'FareCardSectionDesktop_fare_card_wrapper')]"));

    private static final Locator SELECT_BTN_REGULAR = new Locator("SELECT_BTN_REGULAR", new By.ByXPath("(//*[contains(@class,'FareCard_select_btn')])[1]"));
    private static final Locator NOT_MEET_TRAVEL_POLICY_POPUP = new Locator("NOT_MEET_TRAVEL_POLICY_POPUP", new By.ByXPath("//*[contains(@class,'Figure_title') and (contains(text(),'This order doesn’t meet the travel policy') or contains(text(),'Pesanan ini tidak sesuai dengan kebijakan perjalanan perusahaan') )]"));
    private static final Locator NOT_MEET_TRAVEL_POLICY_POPUP_NO_CANCEL = new Locator("NOT_MEET_TRAVEL_POLICY_POPUP_NO_CANCEL", new By.ByXPath("//*[contains(@class,'Figure_secondary') and (contains(text(),'No, cancel') or contains(text(),'Tidak, batalkan') )]"));
    private static final Locator NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE = new Locator("NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE", new By.ByXPath("//*[contains(@class,'Button_variant_primary') and (contains(text(),'Yes, continue') or contains(text(),'Ya, lanjutkan') )]"));
    private static final Locator SELECT_BUTTON = new Locator("SELECT_BUTTON", By.xpath("//button[span[text()='Select' or text()='Pilih']]"));
    private static final Locator STEPPER_JOURNEY_ROUND_TRIP = new Locator("STEPPER_JOURNEY_ROUND_TRIP", By.xpath("//div[contains(@class,'StepperJourney_stepper_journey__')]"));
    private static final Locator FARE_CARDS = new Locator("FARE_CARDS", By.xpath("//div[contains(@class,'FareCardSectionDesktop_fare_card_wrapper')]"));
    private static final Locator PRICE_DETAIL_TAB = new Locator("PRICE_DETAIL_TAB", By.xpath("//button[.//span[normalize-space()='Price Detail' or normalize-space()='Price Details' or normalize-space()='Rincian Harga' or normalize-space()='Detail Harga']]"));
    private static final Locator ADULT_PRICE = new Locator("ADULT_PRICE", By.xpath("(//div[contains(@class,'PriceDetail_fare_component')])[1]" + "//div[contains(@class,'PriceDetail_fare_amount')]//span"));
    private static final Locator REFUND_TAB = new Locator("REFUND_TAB", By.xpath("//button[span[text()='Refund' or text()='Pengembalian Dana']]"));
    private static final Locator REFUND_BREAKDOWN_ITEMS = new Locator("REFUND_BREAKDOWN_ITEMS", By.xpath("//div[contains(@class,'RefundAndReschedule_timeline_item__')]"));
    private static final Locator REFUND_MODAL_CLOSE_BUTTON = new Locator("REFUND_MODAL_CLOSE_BUTTON", By.xpath("//button[@data-testid='modal-close-button']"));

    private Locator seeDetailsButton(int index) {
        return new Locator(
                "SEE_DETAILS_BUTTON_" + index,
                By.xpath("(//div[contains(@class,'FareCardSectionDesktop_fare_card_wrapper')])[" + index + "]//span[normalize-space()='See details' or normalize-space()='Lihat detail']")
        );
    }

    private Locator pdpViNudgesCheckin(String pdpVi){
        return new Locator("PDP_VI_NUDGES", new By.ByXPath("//div[contains(@class,'TravelRoute_schedule_info_list')]//span[normalize-space()='"+pdpVi+"']"));
    }

    public void continueToBookingForm(){
        driverTools.waitForElementToPresence(SELECT_BUTTON);
        driverTools.scrollIntoWebView(SELECT_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_BUTTON);
        if (driverTools.isElementPresent(STEPPER_JOURNEY_ROUND_TRIP)) {
            driverTools.waitForElementToPresence(SELECT_BUTTON);
            driverTools.scrollIntoWebView(SELECT_BUTTON);
            driverTools.clickWithJavascriptExecutor(SELECT_BUTTON);
        }
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        flightBookingFormPage.clickNoThanksOnFlightMayReschedulePopup();
        flightBookingFormPage.waitForBookingPageToLoad();
    }

    public void clickSelectBtnRegular(){
        driverTools.waitForElementToPresence(SELECT_BTN_REGULAR);
        driverTools.scrollIntoWebView(SELECT_BTN_REGULAR);
        driverTools.clickWithJavascriptExecutor(SELECT_BTN_REGULAR);
        if (driverTools.isElementPresent(STEPPER_JOURNEY_ROUND_TRIP)) {
            driverTools.waitForElementToPresence(SELECT_BTN_REGULAR);
            driverTools.scrollIntoWebView(SELECT_BTN_REGULAR);
            driverTools.clickWithJavascriptExecutor(SELECT_BTN_REGULAR);
        }
    }

    public boolean isNotMeetTravelPolicyPopupDisplayed(){
        driverTools.waitForElementToPresence(NOT_MEET_TRAVEL_POLICY_POPUP);
        boolean isNotMeetTravelPolicyPopupDisplayed = driverTools.waitForElementToPresence(NOT_MEET_TRAVEL_POLICY_POPUP).isPresent();
        logger.log("is not meet travel policy popup displayed : " + isNotMeetTravelPolicyPopupDisplayed);

        boolean isNoCancelBtnDisplayed = driverTools.waitForElementToPresence(NOT_MEET_TRAVEL_POLICY_POPUP_NO_CANCEL).isPresent();
        logger.log("is no cancel button displayed : " + isNoCancelBtnDisplayed);

        boolean isYesContinueBtnDisplayed = driverTools.waitForElementToPresence(NOT_MEET_TRAVEL_POLICY_POPUP_YES_CONTINUE).isPresent();
        logger.log("is yes continue button displayed : " + isYesContinueBtnDisplayed);
        return isNotMeetTravelPolicyPopupDisplayed && isNoCancelBtnDisplayed && isYesContinueBtnDisplayed;
    }
    public boolean isPresent(){
       return driverTools.waitForElementToAppear(PRODUCT_REVIEW_PAGE_CHECK).isPresent();

    }

    private double parsePercentage(String text) {
        Matcher matcher = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*%").matcher(text);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Unable to parse percentage from: [" + text + "]");
        }
        return Double.parseDouble(matcher.group(1));
    }

    private double parseAmount(String text) {
        Matcher matcher = Pattern.compile("IDR\\s*([\\d.,]+)").matcher(text);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Unable to parse amount from: [" + text + "]");
        }
        return Double.parseDouble(matcher.group(1).replace(",", ""));
    }

    public void verifyRefundPercentageBreakdownForAllFareCards() {

        driverTools.waitForElementToAppear(FARE_CARDS);

        int fareCardCount = driverTools.elements(FARE_CARDS).map(List::size).orElse(0);

        Assertion.assertThat("Fare cards should be displayed", fareCardCount > 0, is(true));

        for (int i = 1; i <= fareCardCount; i++) {

            driverTools.waitForElementToPresence(seeDetailsButton(i));
            driverTools.clickWithJavascriptExecutor(seeDetailsButton(i));

            driverTools.waitForElementToAppear(PRICE_DETAIL_TAB);
            driverTools.clickWithJavascriptExecutor(PRICE_DETAIL_TAB);

            String priceText = driverTools.getText(driverTools.waitForElementToAppear(ADULT_PRICE, 10).orElseThrow()).trim();
            long adultPrice = Long.parseLong(priceText.replaceAll("[^0-9]", ""));

            logger.log("Fare card " + i + " adult price: " + adultPrice);

            driverTools.waitForElementToPresence(REFUND_TAB);
            driverTools.clickWithJavascriptExecutor(REFUND_TAB);

            driverTools.waitForElementToAppear(REFUND_BREAKDOWN_ITEMS);
            List<WebElement> refundItems = driverTools.waitForElementsToAppear(REFUND_BREAKDOWN_ITEMS, 10).orElseThrow();
            Assertion.assertThat("Refund breakdown should be displayed for fare card " + i, refundItems.size() > 0, is(true));

            for (int j = 0; j < refundItems.size(); j++) {

                WebElement refundItem = refundItems.get(j);
                String refundText = refundItem.getText().trim();
                logger.log("Fare card " + i + " | Breakdown " + (j + 1) + " | Refund text: [" + refundText + "]");

                // Skip cases like "Refund not allowed"
                if (!refundText.contains("%")) {
                    logger.log("Fare card " + i + " | Breakdown " + (j + 1) + " | No refund percentage found, skipping");
                    continue;
                }

                double percentage = parsePercentage(refundText);
                WebElement amountElement = refundItem.findElement(By.xpath(".//p[contains(@class,'RefundAndReschedule_timeline_item_right__')]//b"));

                String amountText = amountElement.getText().trim();
                double actualRefundAmount = parseAmount(amountText);
                double expectedRefundAmount = (adultPrice * percentage) / 100;
                double error = expectedRefundAmount/100;

                logger.log("Fare card " + i + " | Breakdown " + (j + 1) + " | Adult price: " + adultPrice + " | Percentage: " + percentage + "%" + " | Expected: " + expectedRefundAmount + " | Actual: " + actualRefundAmount);
                Assertion.assertThat("Refund amount should match percentage for fare card " + i + ", breakdown " + (j + 1) + " within 1% error", actualRefundAmount, closeTo(expectedRefundAmount, error));
            }

            driverTools.waitForElementToPresence(REFUND_MODAL_CLOSE_BUTTON);
            driverTools.clickWithJavascriptExecutor(REFUND_MODAL_CLOSE_BUTTON);
            driverTools.waitForElementToDisappear(REFUND_MODAL_CLOSE_BUTTON, 3);
        }
    }

    public boolean viPresentOnPdp(String pdpViNudges){
        Locator pDpViNudgesCheck=pdpViNudgesCheckin(pdpViNudges);
        return driverTools.waitForElementToAppear(pDpViNudgesCheck).isPresent();
    }
}
