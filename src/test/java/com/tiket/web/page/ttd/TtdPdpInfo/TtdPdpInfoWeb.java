package com.tiket.web.page.ttd.TtdPdpInfo;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.Optional;

public abstract class TtdPdpInfoWeb<T extends WebDriver> extends WebPage<T> {

    public TtdPdpInfoWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TtdPdpInfoWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new TtdPdpInfoDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new TtdPdpInfoMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator PDP_INFO_BUTTON = new Locator("PDP_INFO_BUTTON", new By.ByXPath("//*[contains(@class,'EventSaleCountdown')]//*[(contains(@class,'Button_variant_primary') or contains(@class,'Button_truncation_not_applied')) and ((text()='Beli tiket sekarang') or (text()='Buy ticket now'))]"));
    private static final Locator PDP_INFO_BUTTON_SOLD_OUT = new Locator("PDP_INFO_BUTTON_SOLD_OUT", new By.ByXPath("//*[contains(@class,'ATFSection_info_section')]//*[contains(@class,'EventSaleCountdown_wrapper')]//*[contains(@class,'Button_truncation_not_applied') and (text()='Sold out' or text()='Terjual habis')]"));
    private static final Locator PDP_TITLE_EVENT = new Locator("PDP_TITLE_EVENT", new By.ByXPath("//h1[contains(@class, 'Text_variant_invert') and contains(@class, 'Text_weight_bold')]"));


    public void selectPdpInfoButton (){
        driverTools.waitForElementToAppear(PDP_INFO_BUTTON);
        driverTools.clickWithJavascriptExecutor(PDP_INFO_BUTTON);
    }

    public Locator generateTitleLocator () {
        WebElement eventTitleElement = driverTools.waitForElementToAppear(PDP_TITLE_EVENT).orElseThrow();
        String titleText = driverTools.getText(eventTitleElement).trim();

        By xpath = By.xpath("//h1[contains(@class, 'Text_variant_invert') and contains(@class, 'Text_weight_bold') and contains(., '" + titleText + "')]");
        return new Locator("EVENT_TITLE", xpath);
    }

    public boolean isCorrectProductOpened (String productName) throws InterruptedException {
        Locator locator = generateTitleLocator();
        driverTools.scroll(2, 10);

        Optional<WebElement> element = driverTools.waitForElementToAppear(locator);

        if (element.isPresent()) {
            return true;
        } else {
            logger.log("Product page not opened");
            return false;
        }
    }

    public String getInfoButtonLabel() {
        driverTools.waitForElementToAppear(PDP_INFO_BUTTON_SOLD_OUT);
        String label = driverTools.getText(PDP_INFO_BUTTON_SOLD_OUT).orElseThrow();
        logger.log("Got label "+label);
        return label;
    }
}
