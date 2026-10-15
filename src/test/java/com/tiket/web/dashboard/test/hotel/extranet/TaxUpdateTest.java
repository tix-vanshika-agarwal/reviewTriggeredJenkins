package com.tiket.web.dashboard.test.hotel.extranet;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.hotel.extranet.ExclusivePrivateDealsPage;
import com.tiket.web.dashboard.page.hotel.extranet.Navbar;
import com.tiket.web.dashboard.page.hotel.extranet.SideMenu;
import com.tiket.web.dashboard.page.hotel.extranet.TaxPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class TaxUpdateTest extends BaseTest {
    @Author(names = {"Ketan", "Vipul Kumar"})
    @Module(name = "Tax")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToUpdateServiceCharge(){
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        TaxPage<?> taxPage = new TaxPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToUpdateServiceCharge");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String serviceCharge = param.get("serviceCharge");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to Tax Page");
        sideMenu.clickFinanceMenu();
        sideMenu.clickTaxMenu();
        Assertion.assertThat("Tax Page is Loaded", taxPage.isTaxPageLoaded(), containsString("Tax"));

        step("Click Edit Button");
        taxPage.clickEditButton();
        Assertion.assertThat("The Service Charge Form page is loaded", taxPage.isTaxFormPageLoaded(), is(true));

        step("Fill out Service Charge");
        taxPage.enterServiceCharge(serviceCharge);

        step("Click Save Button");
        taxPage.clickSaveButton();
        Assertion.assertThat("Tax Updated Successfully Toast Is Presented",taxPage.isTaxUpdatedSuccessfullyToastLoaded(),is(true));

        step("Check the new Service Charge");
        Assertion.assertThat("Tax Page is Loaded", taxPage.isTaxPageLoaded(), containsString("Tax"));
        Assertion.assertThat("New Service Charge is presented",taxPage.getProposedNewServiceCharge(),containsString(serviceCharge));
    }

    @Author(names = {"Ketan", "Vipul Kumar"})
    @Module(name = "Tax")
    @Tribe(name = "Extranet")
    @Vertical(name = "Accommodation")
    @Test
    public void testVerifyUserAbleToUpdateOtherCharge(){
        Session<?> session = BaseTest.sessionThreadLocal.get();
        Navbar<?> navbar = new Navbar<>(session);
        TaxPage<?> taxPage = new TaxPage<>(session);
        ExclusivePrivateDealsPage<?> exclusivePrivateDealsPage = new ExclusivePrivateDealsPage<>(session);
        SideMenu<?> sideMenu = new SideMenu<>(session);

        AuthTest authTest = new AuthTest();

        Map<String, String> param = getParam("testVerifyUserAbleToUpdateOtherCharge");
        BaseTest.paramsThreadLocal.set(param);
        String email = param.get("email");
        String password = param.get("password");
        String hotelName = param.get("hotelName");
        String otherServiceValue = param.get("otherServiceValue");
        String otherServiceName = param.get("otherServiceName");

        step("Login to Extranet");
        authTest.ensureUserIsLoggedInWithCorrectCredentials(email, password, session);

        step("Change hotel");
        navbar.changeHotel(hotelName);

        step("Go to Tax Page");
        sideMenu.clickFinanceMenu();
        sideMenu.clickTaxMenu();
        Assertion.assertThat("Tax Page is Loaded", taxPage.isTaxPageLoaded(), containsString("Tax"));

        step("Click Edit Button");
        taxPage.clickEditButton();
        Assertion.assertThat("The Service Charge Form page is loaded", taxPage.isTaxFormPageLoaded(), is(true));

        step("Switch Other Charge Toggle to On");
        exclusivePrivateDealsPage.switchStatusToggle(true);
        Assertion.assertThat("Other service Options are Loaded",taxPage.isOtherServiceOptionsLoaded(),is(true));
        taxPage.enterOtherServiceName(otherServiceName);
        taxPage.enterOtherServiceValue(otherServiceValue);

        step("Click Save Button");
        taxPage.clickSaveButton();
        Assertion.assertThat("Tax Updated Successfully Toast Is Presented",taxPage.isTaxUpdatedSuccessfullyToastLoaded(),is(true));

        step("Check the new Service Charge");
        Assertion.assertThat("Tax Page is Loaded", taxPage.isTaxPageLoaded(), containsString("Tax"));
        Assertion.assertThat("Other Service Name Is Presented",taxPage.getProposedNewOtherServiceName(),containsString(otherServiceName));
        Assertion.assertThat("Other Service Value Is Presented",taxPage.getProposedNewOtherServiceValue(),containsString(otherServiceValue));
    }
}
