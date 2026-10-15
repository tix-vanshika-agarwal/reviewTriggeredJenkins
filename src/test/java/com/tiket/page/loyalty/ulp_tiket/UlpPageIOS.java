package com.tiket.page.loyalty.ulp_tiket;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
public class UlpPageIOS <T extends WebDriver> extends UlpPage<T> {
    
    private static final Locator ELITE_REWARD_HEADER = new Locator("ELITE_REWARD_HEADER" , AppiumBy.name("tiket Elite Rewards"));

    public UlpPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public boolean isOnEliteRewardPage(){
        return driverTools.waitForElementToAppear(ELITE_REWARD_HEADER,20).isPresent();
    }
    
    @Override
    public void verifyTixPointHistory(){} // FIXME: Implementation pending
    @Override
    public void clickEntryPointParityBanner(){} // FIXME: Implementation pending
    @Override
    public void clickMatchNowFromBlibli(){} // FIXME: Implementation pending
    @Override
    public void verifyUnregisteredBlibliAccountNotif(){} // FIXME: Implementation pending
    @Override
    public void openInfoPageEliteRewards(){} // FIXME: Implementation pending
    @Override
    public void verifyRedirectInfoPage(){} // FIXME: Implementation pending
    @Override
    public void clickCrossSellAT(){} // FIXME: Implementation pending
    @Override
    public void verifyCrossSellRedirectAT(){} // FIXME: Implementation pending
    @Override
    public void verifyMemberBenefit(){} // FIXME: Implementation pending
    @Override
    public void verifyBottomSheet(){} // FIXME: Implementation pending
    @Override
    public void verifyWrongOTP(){} // FIXME: Implementation pending

}