package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateFinanceDropdown<T extends WebDriver> extends BasePage<T> {
    public AffiliateFinanceDropdown(Session<T> session) {
        super(session);
    }

    private static final Locator FINANCE_SECTION = new Locator("FINANCE_SECTION", By.xpath("//span[normalize-space()='Finance']"));
    private static final Locator DROPDOWN_MENU = new Locator("DROPDOWN_MENU", By.xpath("//span[contains(text(),'History Transaction')]"));

    public void clickFinanceDropdown() {
        driverTools.click(FINANCE_SECTION);
    }

    public boolean isFinanceDropdown(){
        return driverTools.isElementDisplayed(DROPDOWN_MENU);
    }
}