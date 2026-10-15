package com.tiket.page.insurance.standalone;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class StandaloneSrp<T extends WebDriver> extends BasePage<T> {
    public StandaloneSrp(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> StandaloneSrp<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {return new StandaloneSrpIOS<>(session);}
            case ANDROID -> {return new StandaloneSrpAndroid<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator TOTAL_INSURANCE_PRICE = new Locator("TOTAL_INSURANCE_PRICE", AppiumBy.xpath("//*[contains(@class,'FooterMobile_total')]"));
    private static final Locator INSURANCE_TITLE = new Locator("INSURANCE_TITLE", AppiumBy.xpath("//*[contains(@class,'InsuranceCard_title')]"));
    private static final Locator INSURANCE_TITLE_PRICE_DETAIL = new Locator("INSURANCE_TITLE_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'PriceDetails_title')]"));
    private static final Locator INSURANCE_PRICE_DETAIL = new Locator("INSURANCE_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'PriceDetails_list_content') and not(contains(@class,'icon'))]//*[contains(text(),'IDR')]"));
    private static final Locator TOTAL_PRICE_DETAIL = new Locator("TOTAL_PRICE_DETAIL", AppiumBy.xpath("//*[contains(@class,'PriceDetails_footer')]//*[contains(text(),'IDR')]"));
    private static final Locator COLLAPSE_PRICE_DETAIL_ITEM = new Locator("COLLAPSE_PRICE_DETAIL_ITEM", AppiumBy.xpath("(//*[contains(@class,'CollapsibleCard_header')])[1]"));

    public boolean isOnStandaloneSrpPage(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("checkout");
        String url = driverTools.getCurrentURL();
        logger.log("current url " + url);
        boolean isUrlCorrect = url.toLowerCase().contains("/insurance/checkout");

        boolean isTitleCorrect = driverTools.waitForElementToPresence(INSURANCE_TITLE).isPresent();
        logger.log("is title correct " + isTitleCorrect);

        boolean isTotalInsurancePriceCorrect = driverTools.waitForElementToPresence(TOTAL_INSURANCE_PRICE).isPresent();
        logger.log("is total insurance price correct " + isTotalInsurancePriceCorrect);

        driverTools.switchToNative();
        return isUrlCorrect && isTitleCorrect && isTotalInsurancePriceCorrect;
    }

    public void openPriceDetail(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("checkout");
        driverTools.clickWithJavascriptExecutor(TOTAL_INSURANCE_PRICE);
        driverTools.switchToNative();
    }

    public boolean isPriceDetailCorrect(String insurancePrice){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("checkout");

        WebElement priceDetailTitleElement = driverTools.waitForElementToAppear(INSURANCE_TITLE_PRICE_DETAIL).orElseThrow();
        String priceDetailTitle = priceDetailTitleElement.getText();
        boolean isPriceDetailTitleCorrect = priceDetailTitle.matches(".*(Flight Insurance|Asuransi Penerbangan).*");
        Assertion.assertThat("is price detail title correct",priceDetailTitle,org.hamcrest.Matchers.anyOf(containsString("Flight Insurance"), containsString("Asuransi Penerbangan")));

        boolean isPriceSumCorrect = isPriceSumCorrect(insurancePrice);

        driverTools.switchToNative();
        return isPriceDetailTitleCorrect && isPriceSumCorrect;
    }

    private boolean isPriceSumCorrect(String insurancePrice) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("checkout");
        if(!driverTools.isElementDisplayed(INSURANCE_PRICE_DETAIL)){
            driverTools.clickWithJavascriptExecutor(COLLAPSE_PRICE_DETAIL_ITEM);
        }
        List<WebElement> insurancePriceElements = driverTools.waitForElementsToAppear(INSURANCE_PRICE_DETAIL).orElseThrow();
        double sum = 0;
        for (WebElement element : insurancePriceElements) {
            String priceText = driverTools.getText(element);
            if(priceText.matches(".*\\d.*")){
                logger.log("price text: " + priceText);
                sum+= CoreFunctions.convertPriceSafely(priceText);
            }
        }
        WebElement totalPriceTextElement = driverTools.waitForElementToAppear(TOTAL_PRICE_DETAIL).orElseThrow();
        String totalPriceText = driverTools.getText(totalPriceTextElement);
        double totalPrice = CoreFunctions.convertPriceSafely(totalPriceText);
        logger.log("calculated sum: " + sum + ", displayed total: " + totalPrice);

        double totalPriceExpected = CoreFunctions.convertPriceSafely(insurancePrice);
        Assertion.assertThat("is total price detail correct and match with order detail page",totalPrice, equalTo(totalPriceExpected));
        Assertion.assertThat("is total price detail correct and match with sum of price detail",totalPrice, equalTo(sum));

        boolean isPriceSumCorrect = (sum == totalPrice);
        logger.log("is price sum correct " + isPriceSumCorrect);

        boolean isTotalPriceMatchWithExpected = (totalPrice == totalPriceExpected);
        logger.log("is total price match with expected " + isTotalPriceMatchWithExpected);

        driverTools.switchToNative();
        return isPriceSumCorrect && isTotalPriceMatchWithExpected;
    }

}
