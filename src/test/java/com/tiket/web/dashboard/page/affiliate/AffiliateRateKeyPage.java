package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

public class AffiliateRateKeyPage<T extends WebDriver> extends BasePage<T> {
    public AffiliateRateKeyPage(Session<T> session) {
        super(session);
    }

    private static final Locator API_PRODUCTS = new Locator("API_PRODUCTS", By.xpath("//p[text()='API Products']"));
    private static final Locator HOTELS_SECTION = new Locator("HOTELS_SECTION", By.xpath("(//div[text()='Hotels'])[1]"));
    private static final Locator SETTINGS = new Locator("SETTINGS", By.xpath("(//input[@placeholder='Settings'])[1]"));
    private static final Locator EDIT_RATE_KEY = new Locator("EDIT_RATE_KEY", By.xpath("//span[text()='Edit Rate Keys']"));
    private static final Locator EDIT_RATE_KEY_DISTRIBUTION = new Locator("EDIT_RATE_KEY_DISTRIBUTION", By.xpath("(//span[text()='Edit Rate Key Distribution'])[2]"));
    private static final Locator PARTNER_ID_EDIT_RATE_KEY = new Locator("PARTNER_ID_EDIT_RATE_KEY", By.xpath("//input[@id='partner-id']"));
    private static final Locator PARTNER_NAME_EDIT_RATE_KEY = new Locator("PARTNER_NAME_EDIT_RATE_KEY", By.xpath("//input[@id='partner-name']"));

    public void goToAffiliateLandingPage() {

        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/affiliate");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/affiliate");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://www.tiket.com/admin/affiliate");
        }
    }

    public boolean goToEditrateKey() {
        driverTools.waitForElementToAppear(API_PRODUCTS, 5);
        driverTools.click(API_PRODUCTS);
        driverTools.waitForElementToAppear(HOTELS_SECTION, 5);
        driverTools.click(HOTELS_SECTION);
        driverTools.waitForElementToAppear(SETTINGS, 5);
        driverTools.click(SETTINGS);
        driverTools.waitForElementToAppear(EDIT_RATE_KEY, 5);
        driverTools.click(EDIT_RATE_KEY);
        driverTools.waitForElementToAppear(EDIT_RATE_KEY_DISTRIBUTION, 5);
        Optional<String> actualMessage11 = driverTools.getText(EDIT_RATE_KEY_DISTRIBUTION);
        String actualMessage1 = actualMessage11.orElseThrow();
        String expectedMessage1 = "Edit Rate Key Distribution";
        Assertion.assertThat("Failed to navigate Edit Rate Key page", actualMessage1, equalTo(expectedMessage1));
        return true;
    }

    public boolean checkPartnerIdIsDisabled() {
        return driverTools.isElementEnabled(PARTNER_ID_EDIT_RATE_KEY);

    }

    public boolean checkPartnerNameIsDisabled() {
        return driverTools.isElementEnabled(PARTNER_NAME_EDIT_RATE_KEY);

    }


}






