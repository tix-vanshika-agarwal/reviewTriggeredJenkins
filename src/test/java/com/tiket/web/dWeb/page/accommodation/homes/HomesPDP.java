package com.tiket.web.dWeb.page.accommodation.homes;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomesPDP<T extends WebDriver> extends WebPage<T> {
    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", new By.ByXPath("//h1[contains(@data-testid,'name')]"));
    public HomesPDP(Session<T> session) {
        super(session);
    }

    public void openHomesPDPlink() {
        String homesPdpPreprod = "https://preprod.tiket.com/homes/indonesia/ascott-waterplace-surabaya-108001534517698775";
        String homesPdpGK = "https://gatotkaca.tiket.com/homes/indonesia/lalasa-villas-test-redirection-805001746462766613";
        String homesPdpProd = "https://tiket.com/homes/indonesia/lalasa-villas-test-redirection-805001746462766613";
        String url="";
        if(Configs.ENVIRONMENT == Environment.GK){
            url = homesPdpGK;
        }else if(Configs.ENVIRONMENT == Environment.PREPROD){
            url = homesPdpPreprod;
        }else {
           url = homesPdpProd;
        }
        driverTools.get(url);
    }
    public String getHomesName(){
        WebElement webElement = driverTools.waitForElementToAppear(HOTEL_NAME).orElseThrow();
        return driverTools.getText(webElement);
    }

}




