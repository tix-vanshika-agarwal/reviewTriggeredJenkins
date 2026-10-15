package com.tiket.web.dashboard.test.nft.train;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.nft.train.*;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class TrainTest extends BaseTest {
    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Finance")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testSeeTiketcomBalanceAtKAI(){
        Map<String, String> param = getParam("testSeeTiketcomBalanceAtKAI");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainFinancePage<?> trainFinancePage = new TrainFinancePage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Train dashboard via UI.");
        trainLoginPage.goToTrainPageViaUI(username, password);

        step("The user goes to the Finance menu.");
        trainFinancePage.goToFinanceMenu();

        step("The user sees the tiket.com balance at KAI on finance page.");
        Assertion.assertThat("The tiket.com balance is displayed and the balances shown are the same.", trainFinancePage.verifySeeTiketcomBalanceAtKAI(), is(true));
        trainFinancePage.verifyBalanceIsEqual();
    }

    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Train Reporting Sales")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testSeeSalesReport(){
        Map<String, String> param = getParam("testSeeSalesReport");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainFinancePage<?> trainFinancePage = new TrainFinancePage<>(session);

        String username = param.get("username");
        String password = param.get("password");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user goes to the Finance menu.");
        trainFinancePage.goToFinanceMenu();

        step("The user sees the sales report.");
        trainFinancePage.seeSalesReport();
        Assertion.assertThat("The use can see train sales report and the data is available and displayed.", trainFinancePage.verifySeeSalesReport(), is(false));
    }

    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Train Reporting Sales")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testDownloadCsvReportingSalesMonthly(){
        Map<String, String> param = getParam("testDownloadCsvReportingSalesMonthly");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainReportingSalesPage<?> trainReportingSalesPage = new TrainReportingSalesPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String timeOutMilliSec = param.get("timeOutMilliSec");
        String csvFileName = param.get("csvFileName");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user goes to the Train Reporting Sales menu.");
        trainReportingSalesPage.goToTrainReportingSalesMenu();

        step("The user goes to the line chart tab list.");
        trainReportingSalesPage.goToLineChartTabList();

        step("The user downloads the monthly csv reporting sales.");
        trainReportingSalesPage.downloadCsvReportingSalesMonthly();
        Assertion.assertThat("The file has been successfully downloaded and the downloaded file name is matched.", trainReportingSalesPage.verifyDownloadedExportCSV(timeOutMilliSec, csvFileName), is(true));
    }

    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Train Reporting Sales")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testDownloadCsvReportingSalesMonthly")
    public void testDownloadPngReportingSalesWeekly(){
        Map<String, String> param = getParam("testDownloadPngReportingSalesWeekly");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainReportingSalesPage<?> trainReportingSalesPage = new TrainReportingSalesPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String timeOutMilliSec = param.get("timeOutMilliSec");
        String pngFileName = param.get("pngFileName");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user goes to the Train Reporting Sales menu.");
        trainReportingSalesPage.goToTrainReportingSalesMenu();

        step("The user goes to the line chart tab list.");
        trainReportingSalesPage.goToLineChartTabList();

        step("The user downloads the weekly png reporting sales.");
        trainReportingSalesPage.downloadPngReportingSalesWeekly();
        Assertion.assertThat("The file has been successfully downloaded and the downloaded file name is matched.", trainReportingSalesPage.verifyDownloadedExportPNG(timeOutMilliSec, pngFileName), is(true));
    }

    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Train Reporting Sales")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testDownloadPngReportingSalesWeekly")
    public void testDownloadSvgReportingSalesDaily(){
        Map<String, String> param = getParam("testDownloadSvgReportingSalesDaily");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainReportingSalesPage<?> trainReportingSalesPage = new TrainReportingSalesPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String timeOutMilliSec = param.get("timeOutMilliSec");
        String svgFileName = param.get("svgFileName");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user goes to the Train Reporting Sales menu.");
        trainReportingSalesPage.goToTrainReportingSalesMenu();

        step("The user goes to the line chart tab list.");
        trainReportingSalesPage.goToLineChartTabList();

        step("The user downloads the daily svg reporting sales.");
        trainReportingSalesPage.downloadSvgReportingSalesDaily();
        Assertion.assertThat("The file has been successfully downloaded and the downloaded file name is matched.", trainReportingSalesPage.verifyDownloadedExportSVG(timeOutMilliSec, svgFileName), is(true));
    }

    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Train Stations Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testAddTrainStation(){
        Map<String, String> param = getParam("testAddTrainStation");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainStationsManagement<?> trainStationsManagement = new TrainStationsManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String stationType = param.get("stationType");
        String city = param.get("city");
        String stationOrCityName = param.get("stationOrCityName");
        String stationOrCityCode = param.get("stationOrCityCode");
        String latitude = param.get("latitude");
        String longitude = param.get("longitude");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user goes to the Train Stations Management menu.");
        trainStationsManagement.goToTrainStationsManagementMenu();

        step("The user adds a new train station.");
        trainStationsManagement.addTrainStation(stationType, city, stationOrCityName, stationOrCityCode, latitude, longitude);
        Assertion.assertThat("A new train station has been successfully added, displayed, and can be searched.", trainStationsManagement.verifyAddTrainStation(stationOrCityCode), is(true));
    }

    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Train Stations Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddTrainStation")
    public void testEditTrainStation(){
        Map<String, String> param = getParam("testEditTrainStation");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainStationsManagement<?> trainStationsManagement = new TrainStationsManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String existingStationType = param.get("existingStationType");
        String existingCity = param.get("existingCity");
        String existingStationOrCityCode = param.get("existingStationOrCityCode");
        String toBeEditedStationType = param.get("toBeEditedStationType");
        String toBeEditedCity = param.get("toBeEditedCity");
        String toBeEditedStationOrCityName = param.get("toBeEditedStationOrCityName");
        String toBeEditedStationOrCityCode = param.get("toBeEditedStationOrCityCode");
        String toBeEditedLatitude = param.get("toBeEditedLatitude");
        String toBeEditedLongitude = param.get("toBeEditedLongitude");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user goes to the Train Stations Management menu.");
        trainStationsManagement.goToTrainStationsManagementMenu();

        step("The user searches the existing train station.");
        trainStationsManagement.searchTrainStation(existingStationOrCityCode);

        step("The user edits an existing train station.");
        trainStationsManagement.editTrainStation(existingStationType, existingCity, toBeEditedStationType, toBeEditedCity, toBeEditedStationOrCityName, toBeEditedStationOrCityCode, toBeEditedLatitude, toBeEditedLongitude);
        Assertion.assertThat("A new train station has been successfully edited, displayed, and can be searched.", trainStationsManagement.verifyEditTrainStation(toBeEditedStationOrCityCode), is(true));
    }

    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Train Stations Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testEditTrainStation")
    public void testActivateTrainStation(){
        Map<String, String> param = getParam("testActivateTrainStation");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainStationsManagement<?> trainStationsManagement = new TrainStationsManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String stationOrCityCode = param.get("stationOrCityCode");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user goes to the Train Stations Management menu.");
        trainStationsManagement.goToTrainStationsManagementMenu();

        step("The user searches the existing train station.");
        trainStationsManagement.searchTrainStation(stationOrCityCode);

        step("The user activates a train station.");
        trainStationsManagement.activateTrainStation();
        Assertion.assertThat("A train station has been successfully activated.", trainStationsManagement.verifyActivateTrainStation(), is(true));
    }

    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Train Stations Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testActivateTrainStation")
    public void testDeactivateTrainStation(){
        Map<String, String> param = getParam("testDeactivateTrainStation");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainStationsManagement<?> trainStationsManagement = new TrainStationsManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String stationOrCityCode = param.get("stationOrCityCode");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user goes to the Train Stations Management menu.");
        trainStationsManagement.goToTrainStationsManagementMenu();

        step("The user searches the existing train station.");
        trainStationsManagement.searchTrainStation(stationOrCityCode);

        step("The user deactivates a train station.");
        trainStationsManagement.deactivateTrainStation();
        Assertion.assertThat("A train station has been successfully deactivated.", trainStationsManagement.verifyDeactivateTrainStation(), is(true));
    }

    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Train Stations Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testDeactivateTrainStation")
    public void testDeleteTrainStation(){
        Map<String, String> param = getParam("testDeleteTrainStation");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainStationsManagement<?> trainStationsManagement = new TrainStationsManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String stationOrCityCode = param.get("stationOrCityCode");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user goes to the Train Stations Management menu.");
        trainStationsManagement.goToTrainStationsManagementMenu();

        step("The user searches the existing train station.");
        trainStationsManagement.searchTrainStation(stationOrCityCode);

        step("The user deletes an existing train station.");
        trainStationsManagement.deleteTrainStation();
        Assertion.assertThat("A train station has been successfully deleted and can not be searched.", trainStationsManagement.verifyDeleteTrainStation(stationOrCityCode), is(true));
    }

    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Train Routes Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testAddTrainRoute(){
        Map<String, String> param = getParam("testAddTrainRoute");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainRoutesManagement<?> trainRoutesManagement = new TrainRoutesManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String origin = param.get("origin");
        String destination = param.get("destination");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user goes to the Train Routes Management menu.");
        trainRoutesManagement.goToTrainRoutesManagementMenu();

        step("The user adds a new train route.");
        trainRoutesManagement.addTrainRoute(origin, destination);
        Assertion.assertThat("A new train station has been successfully added, displayed, and can be searched.", trainRoutesManagement.verifyAddTrainRoute(), is(true));
    }

    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Train Routes Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testAddTrainRoute")
    public void testEditTrainRoute(){
        Map<String, String> param = getParam("testEditTrainRoute");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainRoutesManagement<?> trainRoutesManagement = new TrainRoutesManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String existingOrigin = param.get("existingOrigin");
        String existingDestination = param.get("existingDestination");
        String toBeEditedOrigin = param.get("toBeEditedOrigin");
        String toBeEditedDestination = param.get("toBeEditedDestination");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user goes to the Train Routes Management menu.");
        trainRoutesManagement.goToTrainRoutesManagementMenu();

        step("The user searches an existing train route.");
        trainRoutesManagement.searchTrainRoute(existingOrigin, existingDestination);

        step("The user edits an existing train route.");
        trainRoutesManagement.editTrainRoute(existingOrigin, existingDestination, toBeEditedOrigin, toBeEditedDestination);
        Assertion.assertThat("An existing train route has been successfully edited.", trainRoutesManagement.verifyEditTrainRoute(), is(true));
    }

    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Train Routes Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testEditTrainRoute")
    public void testActivateTrainRoute(){
        Map<String, String> param = getParam("testActivateTrainRoute");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainRoutesManagement<?> trainRoutesManagement = new TrainRoutesManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String origin = param.get("origin");
        String destination = param.get("destination");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user goes to the Train Routes Management menu.");
        trainRoutesManagement.goToTrainRoutesManagementMenu();

        step("The user searches an existing train route.");
        trainRoutesManagement.searchTrainRoute(origin, destination);

        step("The user activates an existing train route.");
        trainRoutesManagement.activateTrainRoute(origin, destination);
        Assertion.assertThat("An existing train route has been successfully activated.", trainRoutesManagement.verifyActivateTrainRoute(), is(true));
    }

    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Train Routes Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testActivateTrainRoute")
    public void testDeactivateTrainRoute(){
        Map<String, String> param = getParam("testDeactivateTrainRoute");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainRoutesManagement<?> trainRoutesManagement = new TrainRoutesManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String origin = param.get("origin");
        String destination = param.get("destination");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user goes to the Train Routes Management menu.");
        trainRoutesManagement.goToTrainRoutesManagementMenu();

        step("The user searches an existing train route.");
        trainRoutesManagement.searchTrainRoute(origin, destination);

        step("The user deactivate an existing train route.");
        trainRoutesManagement.deactivateTrainRoute(origin, destination);
        Assertion.assertThat("An existing train route has been successfully deactivated.", trainRoutesManagement.verifyDeactivateTrainRoute(), is(true));
    }

    @Author(names = { "Glend", "Raditya" })
    @Module(name = "Train Routes Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test(dependsOnMethods = "testDeactivateTrainRoute")
    public void testDeleteTrainRoute(){
        Map<String, String> param = getParam("testDeleteTrainRoute");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainRoutesManagement<?> trainRoutesManagement = new TrainRoutesManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String origin = param.get("origin");
        String destination = param.get("destination");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user goes to the Train Routes Management menu.");
        trainRoutesManagement.goToTrainRoutesManagementMenu();

        step("The user searches an existing train route.");
        trainRoutesManagement.searchTrainRoute(origin, destination);

        step("The user deletes an existing train route.");
        trainRoutesManagement.deleteTrainRoute(origin, destination);
        Assertion.assertThat("A train route has been successfully deleted.", trainRoutesManagement.verifyDeleteTrainRoute(), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Train SEO Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testCreateHomeSEO(){
        Map<String, String> param = getParam("testCreateHomeSEO");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainSEOManagement<?> trainSEOManagement = new TrainSEOManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String title = param.get("title");
        String subtitle = param.get("subtitle");
        String sectionName = param.get("sectionName");
        String url = param.get("url");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user click the Train SEO Management menu.");
        trainSEOManagement.goToTrainSEOManagementMenu();

        step("The user click the Home submenu.");
        trainSEOManagement.goToSubMenu("Home");

        step("The user create a new Home SEO");
        trainSEOManagement.createNewSEOHome(sectionName, title, subtitle, url);
        Assertion.assertThat("Verify the success banner is shown", trainSEOManagement.verifyCreatedHomeSEO(), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Train SEO Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testCreateTrainName() {
        Map<String, String> param = getParam("testCreateTrainName");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainSEOManagement<?> trainSEOManagement = new TrainSEOManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String title = param.get("title");
        String subtitle = param.get("subtitle");
        String sectionName = param.get("sectionName");
        String url = param.get("url");
        String name = param.get("name");
        String from = param.get("from");
        String to = param.get("to");
        String slug = param.get("slug");
        String pageTitle = param.get("pageTitle");
        String metaTitle = param.get("metaTitle");
        String metaDesc = param.get("metaDesc");


        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user click the Train SEO Management menu.");
        trainSEOManagement.goToTrainSEOManagementMenu();

        step("The user click the Train Name submenu.");
        trainSEOManagement.goToSubMenu("Train Name");

        step("The user create a new Train Name");
        trainSEOManagement.createNewTrainName(name, from, to, slug, pageTitle, metaTitle, metaDesc, sectionName, title, subtitle, url);
        Assertion.assertThat("Verify the success banner is shown", trainSEOManagement.verifySuccessCreation(name), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Train SEO Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testCreateStationName() {
        Map<String, String> param = getParam("testCreateStationName");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainSEOManagement<?> trainSEOManagement = new TrainSEOManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String title = param.get("title");
        String subtitle = param.get("subtitle");
        String sectionName = param.get("sectionName");
        String url = param.get("url");
        String name = param.get("name");
        String from = param.get("from");
        String to = param.get("to");
        String slug = param.get("slug");
        String pageTitle = param.get("pageTitle");
        String metaTitle = param.get("metaTitle");
        String metaDesc = param.get("metaDesc");


        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user click the Train SEO Management menu.");
        trainSEOManagement.goToTrainSEOManagementMenu();

        step("The user click the Station Name submenu.");
        trainSEOManagement.goToSubMenu("Station Name");

        step("The user create a new Station Name");
        trainSEOManagement.createNewStationName(name, from, to, slug, pageTitle, metaTitle, metaDesc, sectionName, title, subtitle, url);
        Assertion.assertThat("Verify the success banner is shown", trainSEOManagement.verifySuccessCreation(name), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Train SEO Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testCreateDestination() {
        Map<String, String> param = getParam("testCreateDestination");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainSEOManagement<?> trainSEOManagement = new TrainSEOManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String title = param.get("title");
        String subtitle = param.get("subtitle");
        String sectionName = param.get("sectionName");
        String url = param.get("url");
        String name = param.get("name");
        String from = param.get("from");
        String to = param.get("to");
        String slug = param.get("slug");
        String pageTitle = param.get("pageTitle");
        String metaTitle = param.get("metaTitle");
        String metaDesc = param.get("metaDesc");


        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user click the Train SEO Management menu.");
        trainSEOManagement.goToTrainSEOManagementMenu();

        step("The user click the Destination submenu.");
        trainSEOManagement.goToSubMenu("Destination");

        step("The user create a new Destination");
        trainSEOManagement.createNewDestination(name, from, to, slug, pageTitle, metaTitle, metaDesc, sectionName, title, subtitle, url);
        Assertion.assertThat("Verify the success banner is shown", trainSEOManagement.verifySuccessCreation(name), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Train SEO Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testCreateRoute() {
        Map<String, String> param = getParam("testCreateRoute");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainSEOManagement<?> trainSEOManagement = new TrainSEOManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String title = param.get("title");
        String subtitle = param.get("subtitle");
        String sectionName = param.get("sectionName");
        String url = param.get("url");
        String name = param.get("name");
        String from = param.get("from");
        String to = param.get("to");
        String slug = param.get("slug");
        String pageTitle = param.get("pageTitle");
        String metaTitle = param.get("metaTitle");
        String metaDesc = param.get("metaDesc");


        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user click the Train SEO Management menu.");
        trainSEOManagement.goToTrainSEOManagementMenu();

        step("The user click the Route submenu.");
        trainSEOManagement.goToSubMenu("Route");

        step("The user create a new Route");
        trainSEOManagement.createNewDestination(name, from, to, slug, pageTitle, metaTitle, metaDesc, sectionName, title, subtitle, url);
        Assertion.assertThat("Verify the success banner is shown", trainSEOManagement.verifySuccessCreation(name), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Train SEO Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testCreateSeasonal() {
        Map<String, String> param = getParam("testCreateSeasonal");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainSEOManagement<?> trainSEOManagement = new TrainSEOManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String title = param.get("title");
        String subtitle = param.get("subtitle");
        String sectionName = param.get("sectionName");
        String url = param.get("url");
        String name = param.get("name");
        String slug = param.get("slug");
        String pageTitle = param.get("pageTitle");
        String metaTitle = param.get("metaTitle");
        String metaDesc = param.get("metaDesc");


        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user click the Train SEO Management menu.");
        trainSEOManagement.goToTrainSEOManagementMenu();

        step("The user click the Seasonal submenu.");
        trainSEOManagement.goToSubMenu("Seasonal");

        step("The user create a new Seasonal");
        trainSEOManagement.createNewSeasonal(name, slug, pageTitle, metaTitle, metaDesc, sectionName, title, subtitle, url);
        Assertion.assertThat("Verify the success banner is shown", trainSEOManagement.verifySuccessCreation(name), is(true));
    }

    @Author(names = { "Adit" })
    @Module(name = "Train SEO Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testSetMaintenance() {
        Map<String, String> param = getParam("testSetMaintenance");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainAdminPage<?> trainAdminPage = new TrainAdminPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String hours = param.get("hours");
        String startMinutes = param.get("startMinutes");
        String endMinutes = param.get("endMinutes");
        String seconds = param.get("seconds");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user click the Train Admin menu.");
        trainAdminPage.goToTrainAdminMenu();

        step("The user click the Seasonal submenu.");
        trainAdminPage.goToSubMenu("Train Config");

        step("The user set a new Maintenance time");
        trainAdminPage.setTrainMaintenance(hours, startMinutes, endMinutes, seconds);
        Assertion.assertThat("Verify the success banner is shown", trainAdminPage.verifyMaintenanceSet(), is(true));
    }

    @Author(names = { "Sylvia" })
    @Module(name = "Train Stations Management")
    @Tribe(name = "Train")
    @Vertical(name = "NFT")
    @Test
    public void testSearchStationByStationCode() {
        Map<String, String> param = getParam("testSearchStationByStationCode");
        BaseTest.paramsThreadLocal.set(param);

        Session<?> session = BaseTest.sessionThreadLocal.get();

        TrainLoginPage<?> trainLoginPage = new TrainLoginPage<>(session);
        TrainStationsManagement<?> trainStationManagement = new TrainStationsManagement<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String codeOrStation = param.get ("codeOrStation");

        step("The user logins to the Train dashboard.");
        trainLoginPage.goToTrainPage(username, password);

        step("The user click The Train Station Management");
        trainStationManagement.goToTrainStationsManagementMenu();

        step("The user search station by Station Code");
        trainStationManagement.searchTrainStation(codeOrStation);
        Assertion.assertThat("Verify the code station is shown", trainStationManagement.verifyStationCodeIsShown(), is(codeOrStation.toLowerCase()));
    }

}
