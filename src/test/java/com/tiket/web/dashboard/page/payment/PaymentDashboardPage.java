package com.tiket.web.dashboard.page.payment;

import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;


public class PaymentDashboardPage<T extends WebDriver> extends BasePage<T> {

    private static final String adminPath = "/admin/member";
    private static final Locator DASH_BOARD_LOGO = new Locator("DASH_BOARD_LOGO", By.xpath("//*[contains(@alt,'logo_tiket')]"));
    private static final Locator PAYMENT = new Locator("PAYMENT", By.xpath("//a[@href='/admin/payment_v2']"));
    private static final Locator MESSAGE = new Locator("MESSAGE", By.xpath("//a[@href='/admin/message']"));

    public PaymentDashboardPage(Session<T> session) {
        super(session);
    }

    public void goToAdminCarePage(String username, String password) {
        driverTools.maximizeWindow();
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);
    }

    public void goToAdminCarePageViaUI(String username, String password) throws InterruptedException {
        driverTools.maximizeWindow();
        Deeplink deeplink = new Deeplink("ADMIN_CARE", "https://gatotkaca.tiket.com", "https://preprod.tiket.com", "https://tiket.com");
        String url = switch (Configs.ENVIRONMENT) {
                case PROD -> deeplink.prodDeeplink()+adminPath;
                case PREPROD -> deeplink.preprodDeeplink()+adminPath;
                case GK -> deeplink.gkDeeplink()+adminPath;
                default -> deeplink.gkDeeplink()+adminPath;
        };
        try {
            driverTools.get(url);
        } catch (UnhandledAlertException e) {
            driverTools.acceptBrowserAlert();
        } finally {
            if (!isPaymentAdminCareLoggedIn()) {
                PaymentDashboardLoginPage<?> paymentAdminCareLoginPage = new PaymentDashboardLoginPage<>(session);
                paymentAdminCareLoginPage.loginToPaymentDashBoard(username, password);
            }
        }
    }

    public boolean isPaymentAdminCareLoggedIn() {
        Optional<WebElement> dashboardLogo = driverTools.waitForElementToAppear(DASH_BOARD_LOGO, 1);
        return dashboardLogo.isPresent();
    }

    public void openPaymentsPage() {
        driverTools.waitForElementToAppear(DASH_BOARD_LOGO);
        driverTools.waitForElementToAppear(PAYMENT);
        driverTools.scrollIntoWebView(PAYMENT);
        driverTools.click(PAYMENT);
    }
    public void goToURL(String URL)
    {
        Deeplink deeplink = new Deeplink("ADMIN_CARE", "https://gatotkaca.tiket.com", "https://preprod.tiket.com", "https://tiket.com");
        String url = switch (Configs.ENVIRONMENT) {
            case PROD -> deeplink.prodDeeplink()+URL;
            case PREPROD -> deeplink.preprodDeeplink()+URL;
            case GK -> deeplink.gkDeeplink()+URL;
            default -> deeplink.gkDeeplink()+URL;
        };
        try {
            driverTools.get(url);
        } catch (UnhandledAlertException e) {
            driverTools.acceptBrowserAlert();
        }
    }
}
