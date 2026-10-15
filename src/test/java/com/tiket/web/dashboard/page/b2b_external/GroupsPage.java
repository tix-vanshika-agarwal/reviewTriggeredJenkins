package com.tiket.web.dashboard.page.b2b_external;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GroupsPage <T extends WebDriver> extends BasePage<T> {
    private static final Locator ADD_GROUP = new Locator("ADD_GROUP", new By.ByXPath("//button//span[contains(text(), 'Add Group')]"));
    private static final Locator ADD_GROUP_POPUP = new Locator("ADD_GROUP_POPUP", new By.ByXPath("//button//span[contains(text(), 'Create Group')]"));

    public GroupsPage(Session<T> session) {
        super(session);
    }

    public void clickAddGroup(){
        driverTools.waitForElementToAppear(ADD_GROUP);
        driverTools.click(ADD_GROUP);
    }

    public boolean isAddGroupPopupOpened(){
        return driverTools.waitForElementToAppear(ADD_GROUP_POPUP).isPresent();
    }
}
