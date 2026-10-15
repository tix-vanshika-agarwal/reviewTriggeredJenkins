package com.tiket.web.dashboard.page.ttd.Ignis.SelectPartnerPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

public class IgnisSelectPartnerPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisSelectPartnerPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisSelectPartnerPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisSelectPartnerPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisSelectPartnerPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator PARTNER_BOX_NAMES = new Locator("PARTNER_BOX_NAMES", By.xpath("//div[@class='partner-box']/div[@class='partner-name']"));

    public void selectPartner(String partnerName) {
        driverTools.waitForElementToAppear(PARTNER_BOX_NAMES);
        List<WebElement> partnerNames = driverTools.elementsUntilExist(PARTNER_BOX_NAMES).get();

        if (partnerName.equalsIgnoreCase("random")) {
            int randomInt = CoreFunctions.generateRandomInt(0, partnerNames.size()-1);
            driverTools.click(partnerNames.get(randomInt));
        } else {
            boolean found = false;
            for (WebElement e : partnerNames) {
                String partnerNameText = driverTools.getText(e);
                if (partnerNameText.equalsIgnoreCase(partnerName)) {
                    found = true;
                    driverTools.click(e);
                    break;
                }
            }

            if (!found) {
                throw new NoSuchElementException("There is no partner '" + partnerName + "'");
            }
        }
    }
}
