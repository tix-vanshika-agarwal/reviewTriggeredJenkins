package com.tiket.web.dashboard.page.platform.discovery;

import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;

public class NavigationPage<T extends WebDriver> extends BasePage<T> {
    public NavigationPage(Session<T> session) { super(session); }
    JavascriptExecutor js = (JavascriptExecutor) driver;
    ILogger logger = BaseTest.mainLogger.get();
    private void threadWait(int milis) throws InterruptedException { Thread.sleep(milis); }
    private int totalNavBar = 0;

    private final String URL_NAVIGATION = "/admin/home/mobile-navbar";
    private final Locator GET_IN_BTN = new Locator("GET_IN_BTN", By.xpath("//div[@class='region-label']/following-sibling::button"));
    private final Locator LOGO_AREA = new Locator("LOGO_AREA", By.className("logo-area"));
    private final Locator NAVIGATION_MENU = new Locator("NAVIGATION_MENU", By.xpath("//div[contains(text(),'Navigation')]"));
    private final Locator MOBILE_NAVBAR_LINK = new Locator("MOBILE_NAVBAR_LINK", By.xpath("//a[contains(text(),'Mobile Navbar')]"));
    private final Locator PAGE_TITLE = new Locator("PAGE_TITLE", By.className("page-title"));
    private final Locator NAVBAR_CONTAINER = new Locator("NAVBAR_CONTAINER", By.className("container-menu-navbar"));
    private final Locator LANGUAGE_LIST = new Locator("LANGUAGE_LIST", By.className("language-list"));
    private final Locator EDIT_ICON = new Locator("EDIT_ICON", By.className("edit-icon"));
    private final Locator EDIT_NAVBAR_CONTAINER = new Locator("EDIT_NAVBAR_CONTAINER", By.className("edit-mobile-navbar"));
    private final Locator NAV_BARS = new Locator("NAV_BARS", By.cssSelector(".card-box.expanded.navbar-form-card:not(:last-child)"));
    private final Locator LAST_NAV_BAR = new Locator("LAST_NAV_BAR", By.cssSelector(".card-box.expanded.navbar-form-card:last-child"));
    private final Locator INPUT_CODE = new Locator("INPUT_CODE", By.name("code"));
    private final Locator DELETE_CODE_INPUT = new Locator("DELETE_CODE_INPUT", By.name("deleteCode"));
    private final Locator DELETE_CODE_BTN = new Locator("DELETE_CODE_BTN", By.xpath("//button[text()='DELETE']"));
    private final Locator DELETE_NAVIGATION = new Locator("DELETE_NAVIGATION", By.cssSelector(".delete-nav-container:last-child"));
    private final Locator USER_POPUP_MODAL = new Locator("USER_POPUP_MODAL", By.className("user-popup-modal"));
    private final Locator SNACKBAR_CONTAINER = new Locator("SNACKBAR_CONTAINER", By.className("snackbar-container"));
    private final Locator ADD_NAVBAR_BTN = new Locator("ADD_NAVBAR_BTN", By.className("add-navbar-btn"));
    private final Locator SUBMIT_NAVBAR_BTN = new Locator("SUBMIT_NAVBAR_BTN", By.className("submit-navbar-btn"));
    private final Locator UPLOAD_IMAGE_ACTIVE = new Locator("UPLOAD_IMAGE_ACTIVE", By.name("active"));
    private final Locator UPLOAD_IMAGE_INACTIVE = new Locator("UPLOAD_IMAGE_ACTIVE", By.name("inactive"));
    private final Locator EN_TITLE_FIELD = new Locator("EN_TITLE_FIELD", By.xpath("(//input[@name='title'])[position() = last() - 1]"));
    private final Locator ID_TITLE_FIELD = new Locator("ID_TITLE_FIELD", By.xpath("(//input[@name='title'])[last()]"));
    private final Locator EN_SLUG_FIELD = new Locator("EN_SLUG_FIELD", By.xpath("(//input[@name='link'])[position() = last() - 1]"));
    private final Locator ID_SLUG_FIELD = new Locator("ID_SLUG_FIELD", By.xpath("(//input[@name='link'])[last()]"));
    private final Locator SUBMIT_BTN = new Locator("SUBMIT_BTN", By.xpath("//button[text()='SUBMIT']"));
    private final Locator SPINNER_LOADING = new Locator("SPINNER_LOADING", By.className("spinner"));
    private WebElement getElemFromLastNavbar(Locator locator) {
        return driverTools.element(locator, driver.findElement(LAST_NAV_BAR.by())).orElseThrow();
    }
    private Locator getImageElement(String type) {
        return new Locator("IMAGE_" + type.toUpperCase(), By.xpath(String.format("//label[@for='icon-uploader-%s-%d']/descendant::img", type, totalNavBar - 1)));
    }

    public void goToNavigationPage(String username, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(URL_NAVIGATION);

        driverTools.waitForElementToAppear(GET_IN_BTN);
        if(driverTools.isElementDisplayed(GET_IN_BTN)) {
            driverTools.click(GET_IN_BTN);
        }
    }

    public void verifyHomeDashboard(Map<String, String> sheetData) {
        Assertion.assertThat("Verify current url is home dashboard", driverTools.getCurrentURL(), containsStringIgnoringCase(sheetData.get("url").replaceAll("#", "/")));
        Assertion.assertThat("Verify dashboard title is Dashboard Home", driverTools.getText(LOGO_AREA).orElse(""), is(sheetData.get("dashboardName")));
    }

    public void goToMobileNavBarPage() {
        driverTools.click(NAVIGATION_MENU);
        driverTools.waitForElementToAppear(MOBILE_NAVBAR_LINK);
        driverTools.click(MOBILE_NAVBAR_LINK);
    }

    public void verifyMobileNavbarPage(Map<String, String> sheetData) {
        Assertion.assertThat(String.format("Verify page title is: %s", sheetData.get("pageTitle")), driverTools.getText(PAGE_TITLE).orElse(""), is(sheetData.get("pageTitle")));
        Assertion.assertThat("Verify that navbar container is present", driverTools.isElementDisplayed(NAVBAR_CONTAINER), is(true));
        Assertion.assertThat("Verify that navbar language list is present", driverTools.isElementDisplayed(LANGUAGE_LIST), is(true));

        totalNavBar = driverTools.elements(new Locator("NAV_ITEM", By.className("menu-item")), driver.findElement(NAVBAR_CONTAINER.by())).orElseThrow().size();
        logger.log("total navbar: " + totalNavBar);
    }

    public void clickEditNavbarIcon() throws InterruptedException {
        driverTools.click(EDIT_ICON);
        driverTools.waitForElementToAppear(NAV_BARS);

        Optional<List<WebElement>> navBars = driverTools.elements(NAV_BARS);
        logger.log("navbars:" + navBars);
        Assertion.assertThat("Verify total navbar is " + totalNavBar, navBars.get().size() + 1, equalTo(totalNavBar));

        // collapsed all the navigation list
        for(WebElement nav : navBars.orElseThrow()) {
            js.executeScript("arguments[0].classList.remove('expanded');", nav);
            js.executeScript("arguments[0].style.maxHeight = '0em';", nav.findElement(By.cssSelector(".collapsible.home-dash")));
        }
        driverTools.scrollToElement(LAST_NAV_BAR);
        threadWait(2000);
    }

    public void deleteLastNavBar(Map<String, String> sheetData) throws InterruptedException {
        final int maxNavbar = Integer.parseInt(sheetData.get("maxNavbar"));
        if(totalNavBar == maxNavbar) {
            logger.log("Navbar is maxed out. Proceed deleting latest navbar.");
            final String codeInputText = driverTools.getValue(getElemFromLastNavbar(INPUT_CODE));
            final String expectedCode = sheetData.get("code");
            final String expectedDeleteMsg = sheetData.get("deleteMsg");

            if(!codeInputText.equals(expectedCode)) {
                Assertion.fail("Last Navbar is not '" + expectedCode + "'.\nPlease check manually.");
            }

            driverTools.click(getElemFromLastNavbar(DELETE_NAVIGATION));
            driverTools.waitForElementToAppear(USER_POPUP_MODAL);
            driverTools.sendKeys(DELETE_CODE_INPUT, expectedCode);
            driverTools.click(DELETE_CODE_BTN);
            driverTools.waitForElementToDisappear(USER_POPUP_MODAL);

            driverTools.waitForElementToAppear(SNACKBAR_CONTAINER);
            final String successDeleteMsgActual = driverTools.getText(SNACKBAR_CONTAINER).orElse("");
            Assertion.assertThat("Verify snackbar contains message: " + expectedDeleteMsg, successDeleteMsgActual, is(expectedDeleteMsg));
            driverTools.waitForElementToDisappear(SNACKBAR_CONTAINER);
            threadWait(1000);
        } else {
            logger.log("Navbar is not maxed. Continue process");
        }
    }

    public void clickAddNavigation() {
        driverTools.click(ADD_NAVBAR_BTN);
        driverTools.waitForElementToAppear(getElemFromLastNavbar(INPUT_CODE), 1);
        driverTools.scrollToElement(getElemFromLastNavbar(INPUT_CODE));
    }

    public void inputForms(Map<String, String> sheetData, File imageActive, File imageInactive) throws InterruptedException {
        driverTools.sendKeys(getElemFromLastNavbar(INPUT_CODE), sheetData.get("code"));
        driverTools.sendKeys(getElemFromLastNavbar(UPLOAD_IMAGE_ACTIVE), imageActive.getAbsolutePath());
        threadWait(1000);
        driverTools.sendKeys(getElemFromLastNavbar(UPLOAD_IMAGE_INACTIVE), imageInactive.getAbsolutePath());
        threadWait(1000);
        driverTools.sendKeys(getElemFromLastNavbar(EN_TITLE_FIELD), sheetData.get("titleEn"));
        driverTools.sendKeys(getElemFromLastNavbar(ID_TITLE_FIELD), sheetData.get("titleId"));
        driverTools.sendKeys(getElemFromLastNavbar(EN_SLUG_FIELD), "/" + sheetData.get("slug"));
        driverTools.sendKeys(getElemFromLastNavbar(ID_SLUG_FIELD), "/" + sheetData.get("slug"));
        driverTools.click(SUBMIT_BTN);
        driverTools.waitForElementToDisappear(SPINNER_LOADING);
    }

    public void verifySnackBar(Map<String, String> sheetData) {
        driverTools.waitForElementToAppear(SNACKBAR_CONTAINER);
        final String expectedSuccessMsg = sheetData.get("successMsg");
        final String successMsgActual = driverTools.getText(SNACKBAR_CONTAINER).orElse("");
        Assertion.assertThat("Verify snackbar contains message: " + expectedSuccessMsg, successMsgActual, is(expectedSuccessMsg));
        driverTools.waitForElementToDisappear(SNACKBAR_CONTAINER);
    }

    public void verifyCreatedSnackBar(Map<String, String> sheetData) throws InterruptedException {
        totalNavBar = driverTools.elements(new Locator("NAV_ITEM", By.className("menu-item")), driver.findElement(NAVBAR_CONTAINER.by())).orElseThrow().size();
        clickEditNavbarIcon();
        final String actualCode = driverTools.getValue(getElemFromLastNavbar(INPUT_CODE));
        final String actualTitleEn = driverTools.getValue(getElemFromLastNavbar(EN_TITLE_FIELD));
        final String actualTitleId = driverTools.getValue(getElemFromLastNavbar(ID_TITLE_FIELD));
        final String actualSlugEn = driverTools.getValue(getElemFromLastNavbar(EN_SLUG_FIELD));
        final String actualSlugId = driverTools.getValue(getElemFromLastNavbar(ID_SLUG_FIELD));

        Assertion.assertThat("Verify code navbar", actualCode, is(sheetData.get("code")));
        Assertion.assertThat("Verify active image is present", driverTools.isElementDisplayed(getImageElement("active")), is(true));
        Assertion.assertThat("Verify inactive image is present", driverTools.isElementDisplayed(getImageElement("inactive")), is(true));
        Assertion.assertThat("Verify EN title is correct", actualTitleEn, is(sheetData.get("titleEn")));
        Assertion.assertThat("Verify ID title is correct", actualTitleId, is(sheetData.get("titleId")));
        Assertion.assertThat("Verify EN Slug is correct", actualSlugEn, is("/" + sheetData.get("slug")));
        Assertion.assertThat("Verify ID Slug is correct", actualSlugId, is("/" + sheetData.get("slug")));
    }

}
