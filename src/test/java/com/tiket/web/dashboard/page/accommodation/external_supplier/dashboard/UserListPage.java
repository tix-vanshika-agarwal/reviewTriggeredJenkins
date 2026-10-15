package com.tiket.web.dashboard.page.accommodation.external_supplier.dashboard;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UserListPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator USER_LIST_MENU = new Locator("USER_LIST_MENU", By.xpath("//a[@href='/user-list']//span[normalize-space()='User List']"));
    private static final Locator USER_LIST_TABLE = new Locator("USER_LIST_TABLE", By.xpath("//table | //*[contains(@class,'table') or @role='table']"));
    private static final Locator USER_LIST_ROWS = new Locator("USER_LIST_ROWS", By.xpath("(//table//tbody/tr | //*[@role='row'][.//*[@role='cell']])[1]"));
    private static final Locator PAGINATION = new Locator("PAGINATION", By.xpath("//*[@data-testid='test-id-um-pagination' or @id='id-um-pagination']"));
    private static final Locator PAGINATION_NEXT = new Locator("PAGINATION_NEXT", By.xpath("//*[@data-testid='test-id-um-pagination']//button[@aria-label='Next']"));
    private static final Locator PAGINATION_ACTIVE_PAGE = new Locator("PAGINATION_ACTIVE_PAGE", By.xpath("//*[@data-testid='test-id-um-pagination']//button[@aria-current='true']"));

    public UserListPage(Session<T> session) {
        super(session);
    }

    public void goToUserList() {
        driverTools.waitForElementToAppear(USER_LIST_MENU, 15).orElseThrow();
        driverTools.click(USER_LIST_MENU);
       
    }

    public boolean isUserListDisplayed() {
        boolean tableDisplayed = driverTools.waitForElementToAppear(USER_LIST_TABLE, 30).isPresent();
        return tableDisplayed;
    }

    

    public boolean isPaginationDisplayed() {
        return driverTools.waitForElementToAppear(PAGINATION, 10).isPresent();
    }

    public String getActivePageNumber() {
        driverTools.waitForElementToAppear(PAGINATION_ACTIVE_PAGE, 10);
        return driverTools.getText(PAGINATION_ACTIVE_PAGE).orElse("");
    }

    public boolean goToNextPageAndVerifyChange() {
        if (!driverTools.isElementPresent(PAGINATION_NEXT) || !driverTools.isElementEnabled(PAGINATION_NEXT)) {
            return false;
        }
        String beforePage = getActivePageNumber();
        driverTools.waitForElementToAppear(USER_LIST_ROWS, 5);
        String firstRowBefore = driverTools.getTexts(USER_LIST_ROWS).stream().findFirst().orElse("");

        driverTools.waitForElementToAppear(PAGINATION_NEXT, 10);
        driverTools.scrollToElement(PAGINATION_NEXT);
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(PAGINATION_NEXT);

        driverTools.dummyWait(2);

        String afterPage = getActivePageNumber();

        driverTools.waitForElementToAppear(USER_LIST_ROWS, 5);
        String firstRowAfter = driverTools.getTexts(USER_LIST_ROWS).stream().findFirst().orElse("");

        boolean pageNumberChanged = !beforePage.isEmpty() && !beforePage.equals(afterPage);
        boolean rowContentChanged = !firstRowAfter.isEmpty() && !firstRowAfter.equals(firstRowBefore);
        return pageNumberChanged || rowContentChanged;
    }
}
