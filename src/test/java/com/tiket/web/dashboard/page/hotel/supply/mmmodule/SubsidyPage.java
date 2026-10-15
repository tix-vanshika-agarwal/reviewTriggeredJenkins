package com.tiket.web.dashboard.page.hotel.supply.mmmodule;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class SubsidyPage <T extends WebDriver> extends BasePage<T> {


    private static final Locator ADD_SUBSIDY = new Locator("ADD_SUBSIDY", By.xpath("//a[contains(@href,'create')]"));
    private static final Locator SUBSIDY_LABEL = new Locator("SUBSIDY_LABEL", By.xpath("//div[@class='card-header']//h3"));
    private static final Locator ERROR_TEXT = new Locator("ERROR_TEXT", By.xpath("//p[@class='danger']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[@type='submit']"));


    public SubsidyPage(Session<T> session) {
        super(session);
    }

    public void createSubsidy(){
        driverTools.waitForElementToAppear(ADD_SUBSIDY);
        driverTools.click(ADD_SUBSIDY);
    }

    public boolean isSubsidyLabelPageOpened(){
        driverTools.waitForElementToAppear(SUBSIDY_LABEL,3);
        return driverTools.isElementDisplayed(SUBSIDY_LABEL);
    }

    public void clickSaveButton(){
        driverTools.waitForElementToAppear(SAVE_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    public ArrayList<String> getErrorTexts(){
        ArrayList<String> error=new ArrayList<>();
        List<WebElement>el=driverTools.waitForElementsToAppear(ERROR_TEXT).orElseThrow();

        for(WebElement e:el){
            driverTools.waitForElementToAppear(e);
            String text=driverTools.getText(e);
            error.add(text);

        }
        return error;
    }
}