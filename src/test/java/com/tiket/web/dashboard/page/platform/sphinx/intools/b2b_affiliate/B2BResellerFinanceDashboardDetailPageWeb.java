package com.tiket.web.dashboard.page.platform.sphinx.intools.b2b_affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class B2BResellerFinanceDashboardDetailPageWeb<T extends WebDriver> extends WebPage<T> {
    public B2BResellerFinanceDashboardDetailPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> B2BResellerFinanceDashboardDetailPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new B2BResellerFinanceDashboardDetailPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new B2BResellerFinanceDashboardDetailPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator ADD_TRANSACTION_BUTTON = new Locator("ADD_TRANSACTION_BUTTON", By.xpath("//button[text()='Add transaction']"));

    // Add Transaction Modal
    private static final Locator ADD_TRANSACTION_MODAL = new Locator("ADD_TRANSACTION_MODAL", By.xpath("//section[.//h2[text()='Add Transaction']]"));
    private static final Locator AMOUNT_INPUT = new Locator("AMOUNT_INPUT", By.xpath("//input[@name='amount']"));
    private static final Locator ORDER_ID_INPUT = new Locator("ORDER_ID_INPUT", By.xpath("//input[@name='orderId']"));
    private static final Locator MODAL_ADD_TRANSACTION_BUTTON = new Locator("MODAL_ADD_TRANSACTION_BUTTON", By.xpath("//section//button[text()='Add transaction']"));

    public void clickAddTransactionButton() {
        driverTools.click(ADD_TRANSACTION_BUTTON);
    }

    public void addNewTransaction(String amount, String orderId) {
        driverTools.waitForElementToAppear(ADD_TRANSACTION_MODAL, 2);
        driverTools.scrollIntoWebView(ORDER_ID_INPUT);
        driverTools.clickWithJavascriptExecutor(ORDER_ID_INPUT);
        driverTools.sendKeys(ORDER_ID_INPUT, orderId);

        driverTools.scrollElementToCenter(AMOUNT_INPUT);
        driverTools.click(AMOUNT_INPUT);
        driverTools.sendKeys(AMOUNT_INPUT, amount);

        driverTools.click(MODAL_ADD_TRANSACTION_BUTTON);
        driverTools.dummyWait(2);
    }

    public boolean verifyAddTransactionDataAlreadyInserted(String id, String amount) {
        driverTools.dummyWait(3);
        int resellerIdTableIndex = getIndexOfTableColumn("Reseller ID");
        int amountTableIndex = getIndexOfTableColumn("Transaction Amount");

        Locator resellerIdLoc = new Locator("RESELLER_ID", By.xpath("//table/tbody/tr[1]/td[" + resellerIdTableIndex + "]"));
        Locator amountLoc = new Locator("AMOUNT", By.xpath("//table/tbody/tr[1]/td[" + amountTableIndex + "]"));

        return driverTools.getText(resellerIdLoc).orElseThrow().equalsIgnoreCase(id) &&
                driverTools.getText(amountLoc).orElseThrow().contains(amount);
    }

    private int getIndexOfTableColumn(String colTitle) {
        List<WebElement> elements = driverTools.getDriver().findElements(
                By.xpath("//th[text()='" + colTitle +"']/preceding-sibling::th")
        );
        return elements.size()+1;
    }
}
