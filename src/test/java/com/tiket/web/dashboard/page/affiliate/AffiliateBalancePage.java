package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AffiliateBalancePage<T extends WebDriver> extends BasePage<T> {
    public AffiliateBalancePage(Session<T> session) {
        super(session);
    }

    private static final Locator FINANCE_SECTION = new Locator("FINANCE_SECTION", By.xpath("//span[text()='Finance']/ancestor::div[contains(@class,'justify-between')]"));
    private static final Locator DROPDOWN_MENU = new Locator("DROPDOWN_MENU", By.xpath("//ul[normalize-space(@class)='bg-[#fafafa] pl-[63px] py-[10px] h-auto visible']"));
    private static final Locator BALANCE_SECTION = new Locator("BALANCE_SECTION", By.xpath("//li[.//span[text()='Finance']]//span[text()='Balance']"));
    private static final Locator BALANCE_PAGE = new Locator("BALANCE_SECTION", By.xpath("//p[text()='Current Balance']/parent::div"));
    private static final Locator ADVANCE_DEPOSIT_BALANCE = new Locator("ADVANCE_DEPOSIT_BALANCE", By.xpath("//p[normalize-space(text())='Advance Deposit Balance']/following-sibling::p[contains(@class,'Text_weight_bold')]"));
    private static final Locator CREDIT_LINE_BALANCE = new Locator("CREDIT_LINE_BALANCE", By.xpath("//p[normalize-space(text())='Credit Line Balance']/following-sibling::p[contains(@class,'Text_weight_bold')]"));
    private static final Locator PARTNER_SLIDER_ARROW_ICONS = new Locator("PARTNER_SLIDER_ARROW_ICONS", By.xpath("//section[contains(@class,'switch_partner_slider')]//*[name()='svg']"));
    private static final int MAX_PARTNER_SLIDER_PAGES = 10;

    public void clickFinanceDropdown() {
        driverTools.waitForElementToAppear(FINANCE_SECTION, 1);
        driverTools.click(FINANCE_SECTION);
    }

    public boolean isFinanceDropdown(){
        return driverTools.isElementDisplayed(DROPDOWN_MENU);
    }

    public void clickBalance(){
        driverTools.waitForElementToAppear(BALANCE_SECTION);
        driverTools.click(BALANCE_SECTION);
    }

    public boolean isBalancePage(){
        return driverTools.isElementDisplayed(BALANCE_PAGE);
    }

    public boolean isAdvanceDepositBalanceDisplayed() {
        driverTools.waitForElementToAppear(ADVANCE_DEPOSIT_BALANCE);
        return driverTools.isElementPresent(ADVANCE_DEPOSIT_BALANCE);
    }

    public boolean isCreditLineBalanceDisplayed() {
        driverTools.waitForElementToAppear(CREDIT_LINE_BALANCE);
        return driverTools.isElementPresent(CREDIT_LINE_BALANCE);
    }

    public void selectPartner(String partnerName) {
        Locator partnerCard = new Locator("PARTNER_CARD_" + partnerName, By.xpath("//p[contains(@class,'Text_weight_bold') and normalize-space(text())='" + partnerName + "']/ancestor::div[contains(@class,'Card_card_container')]"));
        driverTools.waitForElementToAppear(PARTNER_SLIDER_ARROW_ICONS);
        int pageCount = 0;
        while (driverTools.waitForElementToPresence(partnerCard, 1).isEmpty() && pageCount < MAX_PARTNER_SLIDER_PAGES) {
            Optional<List<WebElement>> icons = driverTools.elements(PARTNER_SLIDER_ARROW_ICONS);
            logger.log("Partner slider arrow icons found: " + icons.map(List::size).orElse(-1));
            Optional<WebElement> nextArrow = icons
                    .filter(list -> list.size() >= 2)
                    .flatMap(list -> list.stream().max(Comparator.comparingInt(icon -> icon.getLocation().getX())));
            if (nextArrow.isEmpty()) {
                logger.log("No next-arrow candidate found, stopping partner slider search.");
                break;
            }
            driverTools.click(nextArrow.get());
            pageCount++;
        }
        driverTools.waitForElementToAppear(partnerCard);
        driverTools.clickWithJavascriptExecutor(partnerCard);
    }
}