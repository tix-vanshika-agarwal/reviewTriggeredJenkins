package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.Matchers.is;

public class DepartmentPage <T extends WebDriver> extends BasePage<T> {

    private static final Locator CREATE_DEPARTMENT = new Locator("CREATE_DEPARTMENT", new By.ByXPath("//span[contains(text(),'Create Department')]"));
    private static final Locator CREATE_DEPARTMENT_POPUP = new Locator("CREATE_DEPARTMENT_POPUP", new By.ByXPath("//section[contains(@class, 'Modal')]//h2[contains(text(), 'Department')]"));
    private static final Locator FIRST_DEPARTMENT = new Locator("FIRST_DEPARTMENT", new By.ByXPath("//div[contains(@class, 'text-blue-500')]"));
    private static final Locator ADD_MEMBER = new Locator("ADD_MEMBER", new By.ByXPath("//button[contains(text(), 'Add member')]"));
    private static final Locator DEPARTMENT_DETAIL_NAME = new Locator("DEPARTMENT_DETAIL_NAME", new By.ByXPath("//span[contains(@class, 'text-2xl')]"));

    public DepartmentPage(Session<T> session) {
        super(session);
    }

    public  void clickDepartmentButton(){
        driverTools.waitForElementToAppear(CREATE_DEPARTMENT);
        Assertion.assertThat("Create Department Button is visible",driverTools.isElementDisplayed(CREATE_DEPARTMENT),is(true));
        driverTools.click(CREATE_DEPARTMENT);
    }

    public boolean isCreateDepartmentPopup(){
        driverTools.waitForElementToAppear(CREATE_DEPARTMENT_POPUP);
        return driverTools.isElementDisplayed(CREATE_DEPARTMENT_POPUP);
    }

    public void goToDepartmentDetailPage(){
        driverTools.waitForElementToAppear(FIRST_DEPARTMENT);
        Assertion.assertThat("Verify If Department Name is present in Department Column",driverTools.isElementDisplayed(FIRST_DEPARTMENT),is(true));
        driverTools.click(FIRST_DEPARTMENT);
    }

    public  boolean isAddMemberButton(){
        driverTools.waitForElementToAppear(ADD_MEMBER);
        return driverTools.isElementDisplayed(ADD_MEMBER);
    }

    public  boolean isDepartmentDetailPageOpened(){
        driverTools.waitForElementToAppear(DEPARTMENT_DETAIL_NAME);
        return driverTools.isElementDisplayed(DEPARTMENT_DETAIL_NAME);
    }

}
