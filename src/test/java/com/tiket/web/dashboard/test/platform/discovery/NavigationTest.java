package com.tiket.web.dashboard.test.platform.discovery;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.platform.discovery.NavigationPage;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class NavigationTest extends BaseTest {
    @Author(names = {"Yanuar"})
    @Module(name = "Navigation")
    @Tribe(name = "Discovery")
    @Vertical(name = "Grand")
    @Test
    public void testAddMobileNavbar() throws InterruptedException {
        Map<String, String> map = getParam("testAddMobileNavbar");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        NavigationPage<?> navigationPage = new NavigationPage<>(session);

        final String username = map.get("username");
        final String password = map.get("password");

        step("Given admin go to Navigation Page - Home Dashboard");
        navigationPage.goToNavigationPage(username, password);
        navigationPage.verifyHomeDashboard(map);

        step("And admin go to Mobile Navbar menu");
        navigationPage.goToMobileNavBarPage();
        navigationPage.verifyMobileNavbarPage(map);

        step("And admin click on Edit Navbar");
        navigationPage.clickEditNavbarIcon();

        step("When admin click add Navigation");
        navigationPage.deleteLastNavBar(map);
        navigationPage.clickAddNavigation();

        step("And input all the forms");
        final File imageActive = new File("src/test/resources/dashboard/platform.discovery/account-active.png");
        final File imageInactive = new File("src/test/resources/dashboard/platform.discovery/account-inactive.png");

        navigationPage.inputForms(map, imageActive, imageInactive);

        step("Then admin should successfully add the navbar");
        navigationPage.verifySnackBar(map);
        navigationPage.verifyCreatedSnackBar(map);
    }
}
