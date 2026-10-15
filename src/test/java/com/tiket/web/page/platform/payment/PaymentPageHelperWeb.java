package com.tiket.web.page.platform.payment;

import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import javax.security.auth.login.FailedLoginException;

import static org.hamcrest.Matchers.is;

public abstract class PaymentPageHelperWeb<T extends WebDriver> extends WebPage<T> {
    public PaymentPageHelperWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> PaymentPageHelperWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new PaymentPageHelperDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new PaymentPageHelperMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    PaymentPageWeb<?> landingPage = PaymentPageWeb.getInstance(session);
    LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
    HomePageWeb<?> homePage = HomePageWeb.getInstance(session);

    public void loginAndSelectLanguage(String email , String passwordDweb , String otp) throws FailedLoginException, InterruptedException{
        logger.log("Login into dweb if not login using the given account");
        loginPage.loginIfNotWithSameEmail(email,passwordDweb,otp,0);

        logger.log("Select english language if not in english");
        homePage.selectEnglishLanguageOnly();
    }

    public void loginViaCookieAndSelectLanguage(String email, String passwordDweb) throws FailedLoginException, InterruptedException {
        logger.log("Login into dweb if not login using the given account (cookie)");
        loginPage.loginWithSpecificAccountViaCookie(email, passwordDweb);
        logger.log("Select English language if not in English");
        homePage.selectEnglishLanguageOnly();
    }

    public int loginViaCookieAndSelectPaymentMethod(String email, String passwordDweb, String orderIdCreated, String orderHashCreated, String paymentGroup, String paymentMethod, int serviceCharge) throws FailedLoginException, InterruptedException {
        loginViaCookieAndSelectLanguage(email, passwordDweb);
        return goToPaymentPageAndSelectPaymentMethod(orderIdCreated, orderHashCreated, paymentGroup, paymentMethod);
    }

    public int injectSessionAndSelectPaymentMethod(String email, String passwordDweb, String orderIdCreated, String orderHashCreated, String paymentGroup, String paymentMethod) throws FailedLoginException, InterruptedException {
        logger.log("Injecting the session cached by the order-creation API call for " + email);
        loginPage.injectSessionCookiesForApiCreatedOrder(email, passwordDweb);
        logger.log("Select English language if not in English");
        homePage.selectEnglishLanguageOnly();
        return goToPaymentPageAndSelectPaymentMethod(orderIdCreated, orderHashCreated, paymentGroup, paymentMethod);
    }

    public int goToPaymentPageAndSelectPaymentMethod(String orderIdCreated , String orderHashCreated , String paymentGroup , String paymentMethod) {
        logger.log("Go to the payment page");
        landingPage.goToPaymentPage(orderIdCreated, orderHashCreated);

        logger.log("Verify payment page is loaded or not");
        Assertion.assertThat("Verify the payment page is loaded or not", landingPage.isPaymentPageLoaded(), is(true));

        int beforePrice = (int) Double.parseDouble(landingPage.getFinalPrice()) ;

        logger.log("Click on select payment method");
        landingPage.clickOnSelectPaymentMethod();

        logger.log("Scroll down to the payment Group");
        landingPage.goToPaymentMethodsGroup(paymentGroup);

        logger.log("Select Payment Method");
        landingPage.clickOnPaymentGroup(paymentGroup);
        landingPage.selectPaymentMethod(paymentMethod);
        Assertion.assertThat("Verify the selected payment", landingPage.getSelectedPayment(), is(paymentMethod));

        logger.log("Fetch the price after payment method is applied");
        return (int) Double.parseDouble(landingPage.getFinalPrice());
    }

    public int loginAndSelectPaymentMethod(String email , String passwordDweb , String otp , String orderIdCreated , String orderHashCreated , String paymentGroup , String paymentMethod , int serviceCharge) throws FailedLoginException, InterruptedException{
        loginAndSelectLanguage(email , passwordDweb , otp);
        return goToPaymentPageAndSelectPaymentMethod(orderIdCreated , orderHashCreated , paymentGroup , paymentMethod);
    }

    public void inputCardDetails(String cardNumber ,String expiry ,String cvv ,String cardName){
        landingPage.inputCardNumber(cardNumber);
        landingPage.inputCardMonth(expiry);
        landingPage.inputCardCvv(cvv);
        landingPage.inputCardHolderName(cardName);
    }
}