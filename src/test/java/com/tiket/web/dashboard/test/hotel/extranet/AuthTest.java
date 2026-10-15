package com.tiket.web.dashboard.test.hotel.extranet;

import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.extranet.LandingPage;
import com.tiket.web.dashboard.page.hotel.extranet.LoginPage;
import com.tiket.web.dashboard.page.hotel.extranet.Navbar;

import static org.hamcrest.core.Is.is;

public class AuthTest extends BaseTest {
    public void testVerifyUserCanLoginToExtranet(String email, String password, Session<?> session) {
        step("Ensure user is logged in with correct credentials from testdata");
        log("Checking login status and ensuring user '" + email + "' is logged in");
        ensureUserIsLoggedInWithCorrectCredentials(email, password, session);
    }
    
    public void testVerifyUserCanLogoutFromExtranet(Session<?> session) {
        Navbar<?> navbar = new Navbar<>(session);
        LandingPage<?> landingPage = new LandingPage<>(session);

        step("Logout");
        navbar.clickLogoutMenu();
        Assertion.assertThat("Logout from extranet is success",landingPage.isExtranetLandingPageLoaded(), is(true));
    }

    /**
     * Checks if user is logged in. If not logged in, performs login.
     * If logged in with different credentials, logs out and logs in with new credentials.
     */
    public void ensureUserIsLoggedInWithCorrectCredentials(String email, String password, Session<?> session) {
        LoginPage<?> loginPage = new LoginPage<>(session);
        LandingPage<?> landingPage = new LandingPage<>(session);
        Navbar<?> navbar = new Navbar<>(session);

        log("Starting login verification process for user: " + email);
        log("Navigate to Extranet landing page");
        loginPage.goToExtranetLandingPage();

        log("Check if login button is present (indicates user is NOT logged in)");
        boolean isLoginButtonPresent = loginPage.isLoginButtonPresent();
        
        if (isLoginButtonPresent) {
            // Login button is present - user is NOT logged in - perform login
            log("Login button found - user is not logged in - performing login");
            loginPage.clickNavbarLogin();
            loginPage.loginToExtranet(email, password);
        } else {
            // Login button is NOT present - user IS logged in - validate user email matches test data
            log("Login button not found - user is logged in - validating user email");
            navbar.skipAndCloseAnyPopUp();

            String actualEmail = navbar.getDisplayedUserEmail().orElse("—");
            boolean isCorrectUserLoggedIn = actualEmail.equals(email);

            if (!isCorrectUserLoggedIn) {
                // Different user is logged in - logout and login with correct credentials
                log("Different user is logged in - logging out and logging in with correct credentials");
                navbar.clickLogoutMenu();
                Assertion.assertThat("Logout successful", landingPage.isExtranetLandingPageLoaded(), is(true));
                loginPage.clickNavbarLogin();
                loginPage.loginToExtranet(email, password);
            } else {
                // Correct user is already logged in - assert so report shows same account as test data
                Assertion.assertThat("User has been logged in to extranet. Expected: " + email + " Actual: " + actualEmail, isCorrectUserLoggedIn, is(true));
                log("Correct user is already logged in - no action needed");
            }
        }
    }
}