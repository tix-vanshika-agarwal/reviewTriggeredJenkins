package com.tiket.page.whoosh.searchpage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WhooshSearchPageIOS <T extends WebDriver> extends WhooshSearchPage<T> {

    private static final Locator ORIGIN = new Locator("Origin", AppiumBy.accessibilityId("topSearchForm"));
    private static final Locator DESTINATION = new Locator("Destination", AppiumBy.accessibilityId("bottomSearchForm"));
    private static final Locator SEARCH_BUTTON = new Locator("SearchButton", AppiumBy.xpath("//XCUIElementTypeStaticText[@value=\"Search whoosh\" or @value=\"Cari whoosh\"]"));
    private static final Locator ORIGIN_SEARCH_FIELD= new Locator("ORIGIN_SEARCH_FIELD",AppiumBy.xpath("//XCUIElementTypeTextField[@value='Enter city or station name.' or @value='Masukin nama kota atau stasiun.']\n"));
    private static final Locator DEPART_CALENDAR= new Locator("DEPART_CALENDAR",AppiumBy.xpath("//*[@type=\"XCUIElementTypeTable\"]/*[2]/*[7]"));
    private static final Locator PASSENGER_SAVE = new Locator("PASSENGER_SAVE",AppiumBy.xpath("//XCUIElementTypeStaticText[@label=\"Save\" or @label=\"Simpan\"]"));
    private static final Locator CLEAR_SEARCH = new Locator("CLEAR_SEARCH",AppiumBy.xpath("//*[@name='icSearchBoxClose' or @name='tds_ic_oval_cross']"));
    private static final Locator CURRENT_SELECTED_DATE = new Locator("CURRENT_SELECTED_DATE",AppiumBy.xpath("(//XCUIElementTypeStaticText[@name=\"Pergi\" or @name=\"Departure\"]/following-sibling::XCUIElementTypeStaticText[@visible='true'])[last()]"));
    private static final Locator SKIP_COACHMARK = new Locator("SKIP_COACHMARK", AppiumBy.xpath("//XCUIElementTypeStaticText[@label=\"Lewati\" or @label=\"Skip\"]"));
    public static final Locator PRICE  = new Locator("PRICE",AppiumBy.xpath("//XCUIElementTypeStaticText[@label='/pax']/preceding-sibling::*[contains(@label,'.') or contains(@label,',')] | //XCUIElementTypeStaticText[@label='/pax']/following-sibling::*[contains(@label,'.') or contains(@label,',')]"));

    public WhooshSearchPageIOS(Session<T> session) {
        super(session);
    }

    @Override
    public void selectOrigin(String stationName) {
        driverTools.waitForElementToAppear(ORIGIN).orElseThrow();
        driverTools.click(ORIGIN);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        driverTools.waitForElementToAppear(selectCityNameStationCode(stationName));
        driverTools.click(selectCityNameStationCode(stationName));
    }

    @Override
    public String selectOriginAndGetCityName(String stationName) {
        driverTools.waitForElementToAppear(ORIGIN).orElseThrow();
        driverTools.click(ORIGIN);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        WebElement clear = driverTools.waitForElementToPresence(CLEAR_SEARCH).orElseThrow();
        driverTools.click(clear);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        driverTools.waitForElementToAppear(selectCityNameStationCode(stationName));
        WebElement cityNameElement = driverTools.waitForElementToPresence(getCityName(stationName)).orElseThrow();
        String cityName = driverTools.getText(cityNameElement);
        logger.log("City name: "+cityName);
        driverTools.click(selectCityNameStationCode(stationName));
        return CoreFunctions.extractCityName(cityName);
    }

    private Locator selectCityNameStationCode(String stationName) {
        String capitalizedStationName = stationName.substring(0, 1).toUpperCase() + stationName.substring(1);
        return new Locator("FIRST_STATION", AppiumBy.xpath("//XCUIElementTypeStaticText[(contains(@name,'" + capitalizedStationName + "') or contains(@name,'" + capitalizedStationName.toUpperCase() + "')) and @visible='true']"));
    }

    private Locator getCityName(String stationName) {
        String capitalizedStationName = stationName.substring(0, 1).toUpperCase() + stationName.substring(1);
        return new Locator("FIRST_STATION", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@name,'" + capitalizedStationName + "') or contains(@name,'" + capitalizedStationName.toUpperCase() + "')]//preceding-sibling::XCUIElementTypeStaticText[@visible='true']"));
    }
    @Override
    public void selectDestination(String stationName) {
        driverTools.click(DESTINATION);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        driverTools.waitForElementToAppear(selectCityNameStationCode(stationName));
        driverTools.click(selectCityNameStationCode(stationName));
    }
    @Override
    public String selectDestinationAndGetCityName(String stationName) {
        driverTools.click(DESTINATION);
        logger.log("Searching station: "+stationName);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        WebElement clear = driverTools.waitForElementToPresence(CLEAR_SEARCH).orElseThrow();
        driverTools.click(clear);
        driverTools.sendKeys(ORIGIN_SEARCH_FIELD,stationName);
        driverTools.waitForElementToAppear(selectCityNameStationCode(stationName));
        WebElement cityNameElement = driverTools.waitForElementToPresence(getCityName(stationName)).orElseThrow();
        String cityName = driverTools.getText(cityNameElement);
        logger.log("City name: "+cityName);
        driverTools.click(selectCityNameStationCode(stationName));
        return CoreFunctions.extractCityName(cityName);
    }

    @Override
    public void selectTrainViaIndex(int index){
        driverTools.waitForElementToAppear(getTrainSelectLocatorByIndex(index),10);
        driverTools.click(getTrainSelectLocatorByIndex(index));
    }

    private Locator getTrainSelectLocatorByIndex(int index){
        return new Locator("Train_"+index, AppiumBy.xpath("//*[@type='XCUIElementTypeCell' and ./*[@name='train_class_" +index +"']]"));
    }

    @Override
    public void searchTrain() {
        driverTools.click(SEARCH_BUTTON);
        driverTools.waitForElementToDisappear(SEARCH_BUTTON);
    }

    // Whoosh stations are limited; Padalarang <-> Halim is the canonical route used
    // by this helper. The deeplink only needs to populate the date on the search form,
    // so origin/destination here are placeholders that the caller will overwrite via
    // selectOrigin/selectDestination after the back-navigation.
    private static final String WHOOSH_ORIGIN_CODE = "IDPGA";
    private static final String WHOOSH_ORIGIN_LABEL = "Padalarang Station";
    private static final String WHOOSH_DESTINATION_CODE = "IDHMA";
    private static final String WHOOSH_DESTINATION_LABEL = "Halim";

    @Override
    public void deeplinkToWhooshSrp(String yyyyMMddDate, int adult, int infant, int child) {
        String url = buildWhooshSrpPath(yyyyMMddDate, null, adult, infant, child);
        hitWhooshDeeplink("WHOOSH_SRP", url);
    }

    @Override
    public void deeplinkToWhooshSrpRoundTrip(String yyyyMMddDepart, String yyyyMMddReturn, int adult, int infant, int child) {
        String url = buildWhooshSrpPath(yyyyMMddDepart, yyyyMMddReturn, adult, infant, child);
        hitWhooshDeeplink("WHOOSH_SRP_RT", url);
    }

    private String buildWhooshSrpPath(String yyyyMMddDepart, String yyyyMMddReturn, int adult, int infant, int child) {
        StringBuilder sb = new StringBuilder("/kereta-api/cari?")
                .append("d=").append(WHOOSH_ORIGIN_CODE)
                .append("&dlabel=").append(URLEncoder.encode(WHOOSH_ORIGIN_LABEL, StandardCharsets.UTF_8))
                .append("&dt=STATION")
                .append("&a=").append(WHOOSH_DESTINATION_CODE)
                .append("&alabel=").append(URLEncoder.encode(WHOOSH_DESTINATION_LABEL, StandardCharsets.UTF_8))
                .append("&at=STATION")
                .append("&date=").append(yyyyMMddDepart)
                .append("&adult=").append(adult)
                .append("&infant=").append(infant)
                .append("&child=").append(child)
                .append("&productType=whoosh");
        if (yyyyMMddReturn != null && !yyyyMMddReturn.isBlank()) {
            sb.append("&ret_date=").append(yyyyMMddReturn);
        }
        return sb.toString();
    }

    private void hitWhooshDeeplink(String name, String urlPath) {
        Deeplink deeplink = new Deeplink(name,
                "tiket://m.gatotkaca.tiket.com" + urlPath,
                "tiket://m.preprod.tiket.com" + urlPath,
                "tiket://m.tiket.com" + urlPath);
        try {
            driverTools.deeplink(deeplink);
        } catch (Exception e) {
            logger.log(e.getLocalizedMessage());
        }
    }

}
