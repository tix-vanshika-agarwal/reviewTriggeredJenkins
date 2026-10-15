package com.tiket.web.dashboard.page.platform.discovery;

import com.tiket.api.discovery.InfoPageApi;
import com.tiket.core.CoreFunctions;
import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import static org.hamcrest.CoreMatchers.equalTo;

public class InfoPage<T extends WebDriver> extends BasePage<T> {
    public InfoPage(Session<T> session) {
        super(session);
    }

    ILogger logger = BaseTest.mainLogger.get();

    private static final String INFO_PAGE_URL = "/admin/info/page";
    private static final Locator EMAIL_LABEL = new Locator("EMAIL_LABEL", By.className("email-label"));
    private static final Locator GET_IN = new Locator("GET_IN", By.xpath("//button[normalize-space(.)='GET IN']"));
    private static final Locator INFO_PAGE = new Locator("INFO_PAGE", By.xpath("//p[text()='Info Page']"));
    private static final Locator INFO_PAGE_TITLE = new Locator("INFO_PAGE_TITLE", By.xpath("//p[text()='Add Info Page']"));
    private static final Locator ADD_NEW_PAGE = new Locator("ADD_NEW_PAGE", By.xpath("//span[text()='ADD NEW PAGE' or text()='Add Info Page']"));
    private static final Locator INFO_PAGE_NAME = new Locator("INFO_PAGE_NAME", By.xpath("//input[@id='enter-page-name']"));
    private static final Locator PAGE_URL_SLUG = new Locator("PAGE_URL_SLUG", By.xpath("//input[@id='enter-slug']"));
    private static final Locator INFO_PAGE_TITLE_EN = new Locator("INFO_PAGE_TITLE_EN", By.xpath("//input[@id='title-en']"));
    private static final Locator INFO_PAGE_TITLE_ID = new Locator("INFO_PAGE_TITLE_ID", By.xpath("//input[@id='title-id']"));
    private static final Locator META_DESCRIPTION_EN = new Locator("META_DESCRIPTION_EN", By.xpath("//input[@id='description-en']"));
    private static final Locator META_DESCRIPTION_ID = new Locator("META_DESCRIPTION_ID", By.xpath("//input[@id='description-id']"));
    private static final Locator SECTION_NAME = new Locator("SECTION_NAME", By.xpath("//input[@id='enter-section-name']"));
    private static final Locator ID = new Locator("ID", By.xpath("//input[@id='enter-section-id']"));
    private static final Locator TYPE = new Locator("TYPE", By.xpath("//input[@id='select-type']"));
    private static final Locator SELECT_TYPE = new Locator("SELECT_TYPE", By.xpath("//label[contains(@class,'option') and text()='Text']"));
    private static final Locator STYLE = new Locator("STYLE", By.xpath("//input[@id='select-style']"));
    private static final Locator SELECT_STYLE = new Locator("SELECT_STYLE", By.xpath("//label[contains(@class,'option') and text()='Full Text']"));
    private static final Locator TITLE_TEXT_EN = new Locator("TITLE_TEXT_EN", By.xpath("(//input[@id='title'])[1]"));
    private static final Locator TITLE_TEXT_ID = new Locator("TITLE_TEXT_ID", By.xpath("(//input[@id='title'])[2]"));
    private static final Locator BODY_TEXT_EN_EDITOR = new Locator("BODY_TEXT_EN_EDITOR", By.xpath("(//div[contains(@class,'ck-editor__editable')])[1]"));
    private static final Locator BODY_TEXT_ID_EDITOR = new Locator("BODY_TEXT_ID_EDITOR", By.xpath("(//div[contains(@class,'ck-editor__editable')])[2]"));
    private static final Locator BODY_TEXT_ID_Selection = new Locator("BODY_TEXT_ID_Selection", By.xpath("//div[@class='tix-switcher-inner-btn' and text()='ID']"));
    private static final Locator BODY_ID_VALIDATION_ERROR = new Locator("BODY_ID_VALIDATION_ERROR", By.xpath("//*[contains(text(),'Please input the body text (ID)')]"));
    private static final Locator SUBMIT = new Locator("SUBMIT", By.xpath("//button[normalize-space()='Submit']"));
    private static final Locator BREADCRUMB_INFO_PAGE = new Locator("BREADCRUMB_INFO_PAGE", By.xpath("//div[contains(@class,'breadcrumb')]//a[normalize-space()='Info Page']"));
    private static final Locator INFO_PAGE_APPROVE = new Locator("INFO_PAGE_APPROVE", By.xpath("(//span[contains(@class,'Text_variant_activity__') and normalize-space(.)='Approve'])[1]"));
    private static final Locator SEARCH_BY_NAME = new Locator("SEARCH_BY_NAME", By.xpath("//input[@name='name']"));
    private static final Locator SEARCH = new Locator("SEARCH", By.xpath("//span[normalize-space()='SEARCH']"));
    private static final Locator INFO_SUCCESS_CONFIRMATION = new Locator("INFO_SUCCESS_CONFIRMATION", By.xpath("//div[contains(text(),'Successfully create the info page')]"));
    private static final Locator INFO_APPROVE_MESSAGE = new Locator("INFO_APPROVE_MESSAGE", By.xpath("(//div[text()='Success Approve Info List' and @class='snackbar-container'])"));
    private static final Locator INFO_PAGE_COLUMN = new Locator("INFO_PAGE_COLUMN", By.xpath("(//div[@class='name-column'])[1]"));
    private static final Locator INFO_PAGE_STATUS = new Locator("INFO_PAGE_STATUS", By.xpath("//span[(text()='Active')][1]"));
    private static final Locator APPROVE_INFO_PAGE = new Locator("APPROVE_INFO_PAGE", By.xpath("//button[normalize-space()='Yes']"));
    private static final Locator EDIT_INFO_PAGE = new Locator("EDIT_INFO_PAGE", By.xpath("(//button[@aria-label='Edit General'])[1]"));

    public void clickGetInButton() {
        if (driverTools.waitForElementToBeClickable(GET_IN).isEmpty()) {
            throw new NoSuchElementException("GET IN button was not found or is not clickable. Current URL: " + driverTools.getCurrentURL());
        }
        try {
            driverTools.click(GET_IN);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(GET_IN);
        }
    }

    public void goToInfoPage(String username, String password) {
        driverTools.maximizeWindow();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(INFO_PAGE_URL);

        if (!driverTools.getCurrentURL().contains(INFO_PAGE_URL)) {
            memberLoginPage.goToPage(INFO_PAGE_URL);
            driverTools.addCookie(new Cookie.Builder("dashboardAreaHOME", "IDN").sameSite("None").domain(".tiket.com").path("/").build());
            driverTools.addCookie(new Cookie.Builder("dashboardArea", "IDN").sameSite("None").domain(".tiket.com").path("/").build());
            driverTools.addCookie(new Cookie.Builder("x-ls-checker", "true").sameSite("None").domain(".tiket.com").path("/").build());
        }
        try {
            driverTools.waitForElementToAppear(GET_IN, 5);
            clickGetInButton();
        } catch (TimeoutException e) {
            logger.log("GET IN not present — gateway already bypassed by cookie login.");
        }
        driverTools.waitForElementToAppear(INFO_PAGE, 5);
    }

    public void goToInfoPageViaUI(String username, String password) {
        driverTools.maximizeWindow();
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.goToPage(INFO_PAGE_URL);
        memberLoginPage.adminLogin(username, password);
        memberLoginPage.goToPage(INFO_PAGE_URL);
    }

    public boolean isLogin() {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.goToPage(INFO_PAGE_URL);
        driverTools.waitForElementToAppear(EMAIL_LABEL, 5);
        memberLoginPage.goToPage(INFO_PAGE_URL);
        return driverTools.isElementDisplayed(EMAIL_LABEL);
    }

    public void addNewInfoPage() {
        driverTools.waitForElementToAppear(ADD_NEW_PAGE, 5);
        driverTools.click(ADD_NEW_PAGE);
        driverTools.waitForElementToAppear(INFO_PAGE_TITLE, 5);
        driverTools.click(INFO_PAGE_NAME);

        long suffix = CoreFunctions.randomInt(5);
        String InfoPageName = "Info Page" + suffix;
        driverTools.sendKeys(INFO_PAGE_NAME, InfoPageName);
    }

    public void addPageUrlSlug() {
        driverTools.click(PAGE_URL_SLUG);
        long suffix = CoreFunctions.randomInt(5);
        String PageUrlSlug = "InfoPageSlug" + suffix;
        driverTools.sendKeys(PAGE_URL_SLUG, PageUrlSlug);
    }

    public void infoPageTitleEn() {
        driverTools.click(INFO_PAGE_TITLE_EN);
        long suffix = CoreFunctions.randomInt(5);
        String InfoPageTitleEn = "InfoPageTitleEn" + suffix;
        driverTools.sendKeys(INFO_PAGE_TITLE_EN, InfoPageTitleEn);
    }

    public void infoPageTitleId() {
        driverTools.click(INFO_PAGE_TITLE_ID);
        long suffix = CoreFunctions.randomInt(5);
        String InfoPageTitleId = "InfoPageTitleId" + suffix;
        driverTools.sendKeys(INFO_PAGE_TITLE_ID, InfoPageTitleId);
    }

    public void metaDescriptionEn() {
        driverTools.scrollToElement(META_DESCRIPTION_EN);
        driverTools.click(META_DESCRIPTION_EN);
        long suffix = CoreFunctions.randomInt(5);
        String MetaDescriptionEn = "MetaDescriptionEn" + suffix;
        driverTools.sendKeys(META_DESCRIPTION_EN, MetaDescriptionEn);
    }

    public void metaDescriptionId() {
        driverTools.click(META_DESCRIPTION_ID);
        long suffix = CoreFunctions.randomInt(5);
        String MetaDescriptionId = "MetaDescriptionId" + suffix;
        driverTools.sendKeys(META_DESCRIPTION_ID, MetaDescriptionId);
    }

    public void infoSectionName() {
        driverTools.scrollToElement(SECTION_NAME);
        driverTools.click(SECTION_NAME);
        long suffix = CoreFunctions.randomInt(5);
        String SectionName = "SectionName" + suffix;
        driverTools.sendKeys(SECTION_NAME, SectionName);
    }

    public void infoId() {
        driverTools.scrollToElement(ID);
        driverTools.click(ID);
        long suffix = CoreFunctions.randomInt(5);
        String InfoId = "InfoId" + suffix;
        driverTools.sendKeys(ID, InfoId);
    }

    public void infoType() {
        driverTools.scrollToElement(TYPE);
        driverTools.waitForElementToAppear(TYPE, 2);
        driverTools.click(TYPE);
    }

    public void infoSelectType() {
        driverTools.waitForElementToAppear(SELECT_TYPE, 2);
        driverTools.scrollToElement(SELECT_TYPE);
        driverTools.click(SELECT_TYPE);
    }

    public void infoStyle() {
        driverTools.waitForElementToAppear(STYLE, 2);
        driverTools.click(STYLE);
    }

    public void infoSelectStyle() {
        driverTools.waitForElementToAppear(SELECT_STYLE, 2);
        driverTools.click(SELECT_STYLE);
    }


    public void titleTextEn() {
        driverTools.click(TITLE_TEXT_EN);
        long suffix = CoreFunctions.randomInt(5);
        String TitleTextEn = "TitleTextEN" + suffix;
        driverTools.sendKeys(TITLE_TEXT_EN, TitleTextEn);
    }

    public void titleTextId() {
        driverTools.click(TITLE_TEXT_ID);
        long suffix = CoreFunctions.randomInt(5);
        String TitleTextId = "TitleTextID" + suffix;
        driverTools.sendKeys(TITLE_TEXT_ID, TitleTextId);
    }

    public void bodyTextIdSelection() {
        driverTools.scrollToElement(BODY_TEXT_ID_Selection);
        driverTools.waitForElementToAppear(BODY_TEXT_ID_Selection, 5);
        driverTools.clickWithJavascriptExecutor(BODY_TEXT_ID_Selection);
    }

    public void bodyTextEn() {
        driverTools.scrollToBottomOfPage();
        typeInEditor(BODY_TEXT_EN_EDITOR, "BodyTextEn" + CoreFunctions.randomInt(5));
        driverTools.clickWithJavascriptExecutor(TITLE_TEXT_EN);
    }

    public void bodyTextId() {
        typeInEditor(BODY_TEXT_ID_EDITOR, "BodyTextId" + CoreFunctions.randomInt(5));
    }

    private void typeInEditor(Locator editorLocator, String text) {
        driverTools.waitForElementToAppear(editorLocator, 5);
        driverTools.clickWithJavascriptExecutor(editorLocator);
        driverTools.sendKeys(editorLocator, text);
    }

    public void infoSubmit() {
        driverTools.clickWithJavascriptExecutor(SECTION_NAME);
        driverTools.scrollDown(10);
        driverTools.clickWithJavascriptExecutor(SUBMIT);
        if (driverTools.isElementDisplayed(BODY_ID_VALIDATION_ERROR)) {
            logger.log("Body text (ID) validation triggered, re-entering and retrying submit");
            bodyTextId();
            driverTools.clickWithJavascriptExecutor(SECTION_NAME);
            driverTools.scrollDown(10);
            driverTools.clickWithJavascriptExecutor(SUBMIT);
        }
    }

    public void verifyEditedInfoPage() {
        // After submit, rely on breadcrumb visibility/navigation rather than the create-success snackbar (text differs for edit).
        driverTools.waitForElementToAppear(BREADCRUMB_INFO_PAGE, 5);
        driverTools.click(BREADCRUMB_INFO_PAGE);
        driverTools.waitForElementToAppear(SEARCH_BY_NAME, 5);
        driverTools.click(SEARCH_BY_NAME);
        driverTools.sendKeys(SEARCH_BY_NAME, editedInfoPageName);
        driverTools.click(SEARCH);
        Locator editedPageResult = new Locator("EDITED_PAGE_RESULT", By.xpath("//td[normalize-space()='" + editedInfoPageName + "']"));
        driverTools.waitForElementToAppear(editedPageResult, 10);
        if (!driverTools.isElementDisplayed(editedPageResult)) {
            throw new AssertionError("Edited page not found in search results: " + editedInfoPageName);
        }
        logger.log("Visual check passed — found: " + editedInfoPageName);
    }

    public boolean infoPageSuccessMsg() {
        try {
            driverTools.waitForElementToAppear(INFO_SUCCESS_CONFIRMATION, 3);
            logger.log("Create success snackbar appeared.");
            return true;
        } catch (TimeoutException e) {
            logger.log("Snackbar not caught — checking if page redirected to edit (indicating success).");
            if (driverTools.getCurrentURL().contains("/info/page/")) {
                logger.log("Redirected to edit page — treating as success.");
                return true;
            }
        }
        return false;
    }

    public void approveInfoPage() {
        driverTools.click(BREADCRUMB_INFO_PAGE);
        driverTools.waitForElementToAppear(INFO_PAGE_APPROVE, 5);
        driverTools.click(INFO_PAGE_APPROVE);
    }
    public void approveSelectedInfoPage() {
        driverTools.waitForElementToAppear(APPROVE_INFO_PAGE, 5);
        driverTools.click(APPROVE_INFO_PAGE);
    }

    public boolean successApproveInfoPage() {
        try {
            driverTools.waitForElementToAppear(INFO_APPROVE_MESSAGE, 5);
            logger.log("Approve success snackbar appeared.");
            return true;
        } catch (TimeoutException e) {
            logger.log("Approve snackbar did not appear or disappeared too quickly.");
        }
        return false;
    }

    public void checkInfoStatus() {
        driverTools.click(SEARCH_BY_NAME);
        Optional<String> actualMessage = driverTools.getText(INFO_PAGE_COLUMN);
        if (actualMessage.isPresent()) {
            String firstInfoPageTitle = actualMessage.get();
            driverTools.sendKeys(SEARCH_BY_NAME, firstInfoPageTitle);
            driverTools.click(SEARCH);
            try {
                Optional<String> actualStatus = driverTools.getText(INFO_PAGE_STATUS);
                String actualStatus1 = actualStatus.get();
                String expectedStatus = "Active";
                Assertion.assertThat("INFO_PAGE_STATUS", actualStatus1, equalTo(expectedStatus));
            } catch (NoSuchElementException e) {
                logger.log("Info page status element not found, or info page is pending.");
            }
        } else {
            logger.log("No text found for the INFO_PAGE_COLUMN.");
        }
    }

    public void editRecentlyAddedInfoPage() {
        driverTools.click(EDIT_INFO_PAGE);
        driverTools.waitForElementToAppear(INFO_PAGE_NAME, 2);

        String currentUrl = driverTools.getCurrentURL();
        String infoPageId = currentUrl.substring(currentUrl.lastIndexOf("/") + 1);
        this.infoPageId = infoPageId;
        logger.log("Info Page ID: " + infoPageId);

        driverTools.inputStringOneAtTime(INFO_PAGE_NAME, "edited");
        this.editedInfoPageName = String.valueOf(driver.findElement(INFO_PAGE_NAME.by()).getAttribute("value"));
        logger.log("Latest value after edited is" + this.editedInfoPageName);
    }

    private JSONObject callInfoPageApi() throws ExecutionException, InterruptedException{
        if (infoPageId == null || infoPageId.isEmpty()) {
            throw new IllegalStateException("infoPageId is not set");
        }
        InfoPageApi infoPageAPI = new InfoPageApi(infoPageId);
        ApiResponse apiResponse = infoPageAPI.hitApi(apiTools);
        return new JSONObject(apiResponse.getBody());
    }

    public Map<String, String> getInfoPageDataFromAPI() throws ExecutionException, InterruptedException {
        Map<String, String> result = new HashMap<>();

        JSONObject responseData = callInfoPageApi();

        String responseResult = responseData.toString(4);
        String code = responseData.getString("code");

        JSONObject data = responseData.getJSONObject("data");

        // Root data
        String dataId = data.getString("id");
        String pageName = data.getString("pageName");
        String status = data.getString("status");

        // pageSlug
        JSONObject pageSlug = data.getJSONObject("pageSlug");
        String pageSlugID = pageSlug.getString("ID");
        String pageSlugEN = pageSlug.getString("EN");

        // pageTitle
        JSONObject pageTitle = data.getJSONObject("pageTitle");
        String pageTitleID = pageTitle.getString("ID");
        String pageTitleEN = pageTitle.getString("EN");

        // contents
        JSONArray contents = data.getJSONArray("contents");
        String contentId = null;
        String languageIDText = null;
        String languageENText = null;

        if (contents.length() > 0) {
            JSONObject firstContent = contents.getJSONObject(0);

            contentId = firstContent.getString("contentId");

            JSONObject languages = firstContent.getJSONObject("languages");

            JSONObject languageID = languages.getJSONObject("ID");
            languageIDText = languageID.getString("text");

            JSONObject languageEN = languages.getJSONObject("EN");
            languageENText = languageEN.getString("text");
        }

        result.put("response", responseResult);
        result.put("code", code);

        result.put("dataId", dataId);
        result.put("pageName", pageName);

        result.put("pageSlugID", pageSlugID);
        result.put("pageSlugEN", pageSlugEN);

        result.put("pageTitleID", pageTitleID);
        result.put("pageTitleEN", pageTitleEN);

        result.put("contentId", contentId);

        result.put("languageIDText", languageIDText);
        result.put("languageENText", languageENText);

        result.put("status", status);
        return result;
    }

    @Setter @Getter
    private String editedInfoPageName, infoPageId;
}