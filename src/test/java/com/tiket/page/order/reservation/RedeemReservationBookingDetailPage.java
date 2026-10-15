package com.tiket.page.order.reservation;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RedeemReservationBookingDetailPage<T extends WebDriver> extends BasePage<T> {
    public RedeemReservationBookingDetailPage(Session<T> session) {
        super(session);
    }

    private static final Locator FIRST_DATE_CARD = new Locator("FIRST_DATE_CARD", AppiumBy.xpath("(//div[contains(@class, 'PassesMultiDateFilter') and contains(@class, 'card') and not(contains(@class, 'list'))])[1]"));
    private static final Locator FIRST_PACKAGE_RADIOBUTTON = new Locator("FIRST_PACKAGE_RADIOBUTTON", AppiumBy.xpath("(//input[contains(@class, 'Radio_input') and contains(@name, 'package')])[1]"));
    private static final Locator FIRST_VISITOR_CHECKBOX = new Locator("FIRST_VISITOR_CHECKBOX", AppiumBy.xpath("(//input[contains(@class, 'Check_checkbox')])[1]"));
    private static final Locator RESERVE_BUTTON = new Locator("RESERVE_BUTTON", AppiumBy.xpath("//button[contains(@class, 'button_reserve_now')]"));

    public static <T extends WebDriver> RedeemReservationBookingDetailPage<T> getInstance(Session<T> session) {
        return new RedeemReservationBookingDetailPage<>(session);
    }

    public void completeDetailForm() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("to-do/reservation");

        WebElement dateCard = driverTools.waitForElementToAppear(FIRST_DATE_CARD).orElseThrow();
        driverTools.click(dateCard);

        WebElement radioButton = driverTools.waitForElementToPresence(FIRST_PACKAGE_RADIOBUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(radioButton);

        driverTools.swipeToPresentedElement(FIRST_VISITOR_CHECKBOX, Swipe.UP, 5);
        driverTools.clickWithJavascriptExecutor(FIRST_VISITOR_CHECKBOX);
        driverTools.switchToNative();
    }

    public void clickReserveButtonFooter() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("to-do/reservation");
        driverTools.click(RESERVE_BUTTON);
        driverTools.switchToNative();
    }
}
