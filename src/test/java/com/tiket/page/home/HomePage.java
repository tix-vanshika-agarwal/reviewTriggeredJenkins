package com.tiket.page.home;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardApi;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.auth.PayExpressWithEnvApi;
import com.tiket.api.member.MemberVerificationByAdminApi;
import com.tiket.api.utilities.GlobalSearchLandingApi;
import com.tiket.api.utilities.GlobalSearchResultApi;
import com.tiket.api.utilities.HomeModuleApi;
import com.tiket.api.utilities.HomeModuleWithEnvApi;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.*;
import com.tiket.model.Exception.ApiToolsException;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.accommodation.homes.homeslandingpage.HomesLandingPage;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.page.accommodation.hotel.landingpage.LandingPage;
import com.tiket.page.account.AccountPage;
import com.tiket.page.accountcenter.AccountCenterPage;
import com.tiket.page.bus.search.BusSearchPage;
import com.tiket.page.carrental.searchpage.SearchPageCarRental;
import com.tiket.page.cartransfer.searchpage.CarTransferSearchPage;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.ferry.FerrySearchPage.FerrySearchPage;
import com.tiket.page.flight.searchpage.FlightSearchPage;
import com.tiket.page.flight.searchresultpage.FlightSearchResultPage;
import com.tiket.page.home.verticals.VerticalsPage;
import com.tiket.page.login.LoginPage;
import com.tiket.page.setting.SettingPage;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.page.ttd.landingpage.TTDLandingPage;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.pojo.member.RemovePhoneNumberVerificationResponse;
import com.tiket.pojo.utilities.*;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.rewards_page.RewardsPageWeb;
import com.tiket.web.page.ttd.TtdProductDetailPage.TtdProductDetailPageWeb;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import static com.tiket.page.payment.PaymentPageAndroid.LOADING_STATE;
import static com.tiket.page.home.HomePageIOS.CLOSE_GIVE_RATING_POPUP;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

@Slf4j
public abstract class HomePage <T extends WebDriver> extends BasePage<T> {

    public final VerticalsPage<?> verticalsPage;
    protected static final Map<String, Boolean> FLAG_MAP_OK_COACHMARK = new ConcurrentHashMap<>();

    public HomePage(Session<T> session) {
        super(session);
        this.verticalsPage = VerticalsPage.getInstance(session);
    }

    public static <T extends WebDriver> HomePage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new HomePageAndroid<>(session);
            }
            case IOS -> {
                return new HomePageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator INFO_PAGES_CONTENT = new Locator("INFO_PAGES_CONTENT", AppiumBy.xpath("//*[contains(@class,'InfoPageContent') or contains(@class,'webview-layout') or contains(@class,'help_topic_container')]"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//*[@class='btn-back']"));
    private static final Map<String, DeeplinkVerticalPOJO> deeplinkMapData = new HashMap<>();
    public static final Locator EDIT_RECIPIENT = new Locator("EDIT_RECIPIENT", AppiumBy.xpath("//*[@type='XCUIElementTypeImage' and ./preceding-sibling::*[@type='XCUIElementTypeOther']]"));

    /** "Welcome to Blibli Tiket ecosystem" screen that can take over the app right after a login deeplink. */
    private static final Locator ECOSYSTEM_WELCOME_CTA = new Locator("ECOSYSTEM_WELCOME_CTA", AppiumBy.xpath("//*[contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'get started') or contains(translate(@text,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'mulai sekarang') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'get started') or contains(translate(@label,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'mulai sekarang')]"));

    /**
     * Generic locator for an email value displayed on a native screen (Android or iOS).
     * <p>
     * Matches any element whose visible attribute contains an email-like pattern
     * (at minimum an {@code @} and a {@code .}). Attributes covered:
     * <ul>
     *   <li>Android native: {@code text}, {@code content-desc}</li>
     *   <li>iOS native: {@code label}, {@code name}, {@code value}</li>
     * </ul>
     * Use with {@link #fetchEmailFromNativeScreen()} which extracts the actual email substring.
     */
    public static final Locator EMAIL_ON_NATIVE_SCREEN = new Locator("EMAIL_ON_NATIVE_SCREEN", AppiumBy.xpath("//*[(contains(@text,'@') and contains(@text,'.')) or (contains(@label,'@') and contains(@label,'.')) or (contains(@name,'@') and contains(@name,'.')) or (contains(@value,'@') and contains(@value,'.')) or (contains(@content-desc,'@') and contains(@content-desc,'.'))]"));

    private static final java.util.regex.Pattern EMAIL_PATTERN =
            java.util.regex.Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");

    public boolean isInfoPageContentDisplayed() {
        driverTools.waitForElementToDisappear(LOADING_STATE, 20);
        driverTools.switchToWebView();
        boolean status = driverTools.waitForElementToPresence(INFO_PAGES_CONTENT).isPresent();
        driverTools.switchToNative();
        return status;
    }

    public String getAppUrl() {
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        String actualUrl = driverTools.getCurrentURL();
        int loop = 0;
        while (actualUrl.equals("about:blank")) {
            driverTools.switchToNative();
            loop++;
            driverTools.switchToWebView();
            actualUrl = driverTools.getCurrentURL();
            if (loop == 30) break;
        }
        driverTools.switchToNative();
        return actualUrl;
    }

    public void clickBackBtnWebview() {
        driverTools.switchToWebView();
        driverTools.click(BACK_BUTTON);
        driverTools.switchToNative();
    }

    private String getToken() throws ExecutionException, InterruptedException {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
    }

    private String getToken(Environment environment) throws ExecutionException, InterruptedException {
        PayExpressWithEnvApi.Inputs payExpressWithEnvApiInputs = new PayExpressWithEnvApi.Inputs(environment);
        PayExpressWithEnvApi payExpressWithEnvApi = new PayExpressWithEnvApi(payExpressWithEnvApiInputs);
        ApiResponse payExpressResponse = payExpressWithEnvApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
    }

    private String getAvailaibleModuleName(HomeModuleApi.Inputs inputs, String template) throws ExecutionException, InterruptedException {
        HomeModuleApi homeModuleApi = new HomeModuleApi(inputs);

        for (int x = 5; x > 0; x--) {
            ApiResponse homeModuleApiResponse = homeModuleApi.hitApi(apiTools);
            JSONObject homeModuleApiResponseObject = new JSONObject(homeModuleApiResponse.getBody());

            JSONObject responseData = homeModuleApiResponseObject.getJSONObject("data");

            JSONArray modulesData = responseData.getJSONArray("pageModules");

            for (int i = 0; i < modulesData.length(); i++) {
                JSONObject data = (JSONObject) modulesData.get(i);
                String templateCode = data.getString("templateCode");
                String processType = data.getString("processType");
                if (templateCode.equals(template.toUpperCase()) && processType.equals("ASYNC")) {
                    JSONArray contents = data.getJSONArray("dynamicContents");
                    JSONObject modulesTitileObj = (JSONObject) contents.get(0);
                    return modulesTitileObj.getString("title");
                }
            }
        }
        throw new ApiToolsException("Homes module " + template + " not found in API response");
    }

    @SneakyThrows
    private String[] getAvailaibleModuleNames(HomeModuleApi.Inputs inputs,String[] template){
        ArrayList<String> result = new ArrayList<>();
        List<String> templateList = Arrays.asList(template);
        List<String> foundTemplate = new ArrayList<>();

        HomeModuleApi homeModuleApi = new HomeModuleApi(inputs);
        ApiResponse homeModuleApiResponse = homeModuleApi.hitApi(apiTools);

        Gson gson = new GsonBuilder().registerTypeAdapter(HomePageModule.DynamicContent.class, new DynamicContentDeserializer())
                .create();
        HomePageModule homeModuleApiResponseObject = gson.fromJson(homeModuleApiResponse.getBody(), HomePageModule.class);
        HomePageModule.Data responseData = homeModuleApiResponseObject.data;
        ArrayList<HomePageModule.PageModules> modulesData = responseData.pageModules;
        for (HomePageModule.PageModules module : modulesData) {
            String templateCode = module.templateCode;
            String processType = module.processType;

            if (templateList.contains(templateCode) && processType.equals("ASYNC") && !foundTemplate.contains(templateCode)) {
                if(templateCode.equals("IMAGE_WITH_CHIPS")){
                    boolean isEmpty =isEmptyCard(module);
                    if(isEmpty){
                        logger.log("Empty card with title: "+getModuleTitle(module)+" , continuing");
                        continue;
                    }
                }
                foundTemplate.add(templateCode);
                String moduleTitle = getModuleTitle(module);
                result.add(moduleTitle);
                logger.log("Template Code : '" + templateCode + "' \nSection Title : '" + moduleTitle + "'");
            }
        }

        return result.toArray(new String[0]);
    }

    private boolean isEmptyCard(HomePageModule.PageModules module){
        return module.dynamicContents.get(0).cards.isEmpty();
    }

    @SneakyThrows
    private String getAvailableModuleNames(HomeModuleApi.Inputs inputs, String template, String sectionName) {
        ArrayList<String> result = new ArrayList<>();
        List<String> templateList = Arrays.asList(template);
        List<String> foundTemplate = new ArrayList<>();

        HomeModuleApi homeModuleApi = new HomeModuleApi(inputs);
        ApiResponse homeModuleApiResponse = homeModuleApi.hitApi(apiTools);

        Gson gson = new GsonBuilder().registerTypeAdapter(HomePageModule.DynamicContent.class, new DynamicContentDeserializer())
                .create();
        HomePageModule homeModuleApiResponseObject = gson.fromJson(homeModuleApiResponse.getBody(), HomePageModule.class);
        HomePageModule.Data responseData = homeModuleApiResponseObject.data;
        List<HomePageModule.PageModules> modulesData = responseData.pageModules;

        logger.log("All modules: " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(modulesData));

        for (HomePageModule.PageModules module : modulesData) {
            String templateCode = module.templateCode;
            String processType = module.processType;
            String moduleSectionName = module.sectionName;

            if (templateList.contains(templateCode) && "ASYNC".equals(processType)
                    && !foundTemplate.contains(templateCode)
                    && (sectionName.isEmpty() || sectionName.equals(moduleSectionName))) {
                foundTemplate.add(templateCode);
                String moduleTitle = getModuleTitle(module);
                String label = module.moduleDetail.label;
                String description = module.moduleDetail.description;
                String productName = module.dynamicContents.get(0).productName;
                String location = module.dynamicContents.get(0).location;
                String discountedPrice = module.dynamicContents.get(0).rateInfo.discountedPrice;
                String rating = getHighlightDiscoverRating(module);
                String review = module.dynamicContents.get(0).reviewsPlaceholder;
                result.add(moduleTitle);
                result.add(productName);
                result.add(location);
                result.add(discountedPrice);
                result.add(label);  result.add(description);   result.add(review); result.add(rating);
                logger.log("1 Module Title: '" + moduleTitle + " 2 Product Name: '" +   productName + "'\n 3 product location : '" + location +"' 4 Discount price : '" + discountedPrice + "\n' 5 Module label: '" + label + " 6 Product Description: ' "+description +"");
                logger.log("7 review: " +review+" 8 Product Rating: '" +  rating +"");
            }
        }
        if (result.isEmpty()) {
            throw new ApiToolsException("Home module " + template + " not found in API response");
        }
        return String.join(", ", result);
    }

    private String getHighlightDiscoverRating(HomePageModule.PageModules module) {
        for (HomePageModule.DynamicContent content : module.dynamicContents) {
            if (content.reviews != null && content.reviews.rating != null) {
                return content.reviews.rating;
            }
        }
        return "";
    }

    @SneakyThrows
    public String getHomeModuleSection(String username, String password, String templateHome, String lang, String sectionName, String currency) {
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password,"B2C");
        logger.log("access token: " + accessToken);

        HomeModuleApi.Inputs homeModuleData = new HomeModuleApi.Inputs(String.valueOf(Configs.PLATFORM), accessToken, lang,currency);
        String availableModule = getAvailableModuleNames(homeModuleData, templateHome, sectionName);
        logger.log("available home module -> " + availableModule);
        return availableModule;
    }

    private String getModuleTitle(HomePageModule.PageModules module) throws NoSuchMethodException {
        String result = "";
        switch (module.templateCode){
            case "FLASH_SALE":
                result += module.dynamicContents.get(0).title;
                break;
            case "UPCOMING_WIDGET":
            case "IMAGE_WITH_CHIPS":
                result += module.moduleDetail.heading.title;
                break;
            case "HIGHLIGHT_DISCOVER_PRODUCT":
                result += module.moduleDetail.title;
                break;
            default:
                throw new NoSuchMethodException();
        }
        return result;
    }


    @SneakyThrows
    public String getHomeModule(String username,String password,String templateHome,String lang, String currency){
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password,"B2C");
        logger.log("access token: " + accessToken);

        HomeModuleApi.Inputs homeModuleData = new HomeModuleApi.Inputs(String.valueOf(Configs.PLATFORM),accessToken, lang, currency);
        String availableModule = getAvailaibleModuleName(homeModuleData,templateHome);
        logger.log("available home module -> " + availableModule);
        return availableModule;
    }

    @SneakyThrows
    public HomePageModule getHomeModuleResponse(boolean needLogin,String username,String password,String lang, String currency){
        String accessToken;
        if(needLogin){
            Unm<T> unm = new Unm<>(session);
            accessToken = unm.getAccessToken(username, password,"B2C");
            logger.log("access token: " + accessToken);
        }else {
            accessToken = getToken();
            logger.log("accessToken-> " + accessToken);
        }

        HomeModuleApi.Inputs homeModuleInputs = new HomeModuleApi.Inputs(String.valueOf(Configs.PLATFORM),accessToken, lang, currency);
        HomeModuleApi homeModuleApi = new HomeModuleApi(homeModuleInputs);
        ApiResponse homeModuleApiResponse = homeModuleApi.hitApi(apiTools);
        Gson gson = new GsonBuilder().registerTypeAdapter(HomePageModule.DynamicContent.class, new DynamicContentDeserializer())
                .create();
        HomePageModule homeModuleApiResponseObject = gson.fromJson(homeModuleApiResponse.getBody(), HomePageModule.class);

        logger.log("available home module -> " + homeModuleApiResponse);
        return homeModuleApiResponseObject;
    }

    /**
     * Resolves the on-screen heading of a home page module straight from the home module API, so a
     * case follows whatever campaign is live instead of pinning a title in the test data.
     * <p>
     * {@code preferredTitle} takes the usual "English/Bahasa" form. Each variant is tried in turn
     * against the headings the API returned, and when none of them is live the first module of
     * {@code templateCode} is used instead, so the case still runs against whatever campaign is up.
     *
     * @param templateCode   module template, e.g. {@code DYNAMIC_WIDGET}
     * @param preferredTitle heading to prefer, or blank to just take the first module
     * @return the module heading as the app renders it, in {@code lang}
     */
    @SneakyThrows
    public String getModuleHeadingTitle(boolean needLogin, String username, String password, String lang,
                                        String currency, String templateCode, String preferredTitle) {
        HomePageModule response = getHomeModuleResponse(needLogin, username, password, lang, currency);

        List<String> headings = new ArrayList<>();
        for (HomePageModule.PageModules pageModule : response.data.pageModules) {
            if (!templateCode.equalsIgnoreCase(pageModule.templateCode)) {
                continue;
            }
            String heading = readModuleHeading(pageModule);
            if (isNotBlank(heading)) {
                headings.add(heading);
            }
        }
        if (headings.isEmpty()) {
            throw new ApiToolsException("Home module " + templateCode + " not found in API response");
        }
        logger.log(templateCode + " headings from API -> " + headings);

        if (isNotBlank(preferredTitle)) {
            for (String variant : CoreFunctions.splitLanguageData(preferredTitle, "/")) {
                for (String heading : headings) {
                    if (heading.toLowerCase().contains(variant.trim().toLowerCase())) {
                        logger.log("matched preferred " + templateCode + " heading -> " + heading);
                        return heading;
                    }
                }
            }
            logger.softWarning("No " + templateCode + " matched '" + preferredTitle
                    + "', falling back to '" + headings.get(0) + "'");
        }
        return headings.get(0);
    }

    /**
     * Heading of the home module of {@code templateCode} whose chips sell {@code chipProductType} —
     * e.g. the accommodation recommendation carousel is the {@code IMAGE_WITH_CHIPS} module whose
     * chips carry {@code TIXHOTEL}.
     *
     * <p>Prefer this over {@link #getModuleHeadingTitle(boolean, String, String, String, String, String, String)}
     * when the heading itself cannot be pinned down. Headings on this module are campaign copy and
     * are edited freely per environment ("Take a peek at these recommendations!" on one build,
     * "Recommendation in Indonesia ..." on another), while the chip {@code productType} is the
     * product the module is wired to and only changes if the module is rebuilt. {@code sectionName}
     * and the module {@code id} are no better: both are authoring-side values that differ per
     * environment.
     *
     * @param templateCode    module template, e.g. {@code IMAGE_WITH_CHIPS}
     * @param chipProductType product type on any of the module's chips, e.g. {@code TIXHOTEL}
     * @return the module heading as the app renders it, in {@code lang}
     */
    @SneakyThrows
    public String getModuleHeadingByChipProductType(boolean needLogin, String username, String password, String lang,
                                                    String currency, String templateCode, String chipProductType) {
        return getModuleHeadingByChipProductType(needLogin, username, password, lang, currency, templateCode,
                chipProductType, "");
    }

    /**
     * As {@link #getModuleHeadingByChipProductType(boolean, String, String, String, String, String, String)},
     * with {@code variantNamePrefix} breaking ties when more than one module of {@code templateCode}
     * sells {@code chipProductType} — a recommendation-engine module (e.g. {@code neoPolaris_v8})
     * then wins over a manually curated one.
     *
     * <p>Only ever a tiebreaker, and only ever a <b>prefix</b>. {@code variantName} is an
     * experiment/bucket value that carries a model version ({@code _v8} becomes {@code _v9} on the
     * next rollout) and can differ per account, so matching it exactly — or requiring it at all —
     * would make the lookup fail for reasons that have nothing to do with the module's identity.
     * When no module carries the prefix, the first {@code chipProductType} match is still returned.
     *
     * @param variantNamePrefix start of the recommendation variant to prefer, e.g. {@code neoPolaris};
     *                          blank to take the first match
     */
    @SneakyThrows
    public String getModuleHeadingByChipProductType(boolean needLogin, String username, String password, String lang,
                                                    String currency, String templateCode, String chipProductType,
                                                    String variantNamePrefix) {
        HomePageModule response = getHomeModuleResponse(needLogin, username, password, lang, currency);

        String firstHeading = "";
        for (HomePageModule.PageModules pageModule : response.data.pageModules) {
            if (!templateCode.equalsIgnoreCase(pageModule.templateCode) || !hasChipOfProductType(pageModule, chipProductType)) {
                continue;
            }
            String heading = readModuleHeading(pageModule);
            if (!isNotBlank(heading)) {
                logger.softWarning("Module " + pageModule.id + " sells " + chipProductType + " but carries no heading");
                continue;
            }
            String variantName = readVariantName(pageModule);
            logger.log("candidate " + templateCode + " module selling " + chipProductType + " -> '" + heading
                    + "' (variantName: " + variantName + ")");
            if (isNotBlank(variantNamePrefix) && variantName.toLowerCase().startsWith(variantNamePrefix.toLowerCase())) {
                logger.log("matched preferred variant '" + variantNamePrefix + "' -> " + heading);
                return heading;
            }
            if (!isNotBlank(firstHeading)) {
                firstHeading = heading;
            }
        }
        if (!isNotBlank(firstHeading)) {
            throw new ApiToolsException("No " + templateCode + " module with a " + chipProductType
                    + " chip found in the home module API response");
        }
        if (isNotBlank(variantNamePrefix)) {
            logger.softWarning("No " + templateCode + " module on variant '" + variantNamePrefix
                    + "', falling back to '" + firstHeading + "'");
        }
        return firstHeading;
    }

    private String readVariantName(HomePageModule.PageModules pageModule) {
        if (pageModule.dynamicContents == null || pageModule.dynamicContents.isEmpty()) {
            return "";
        }
        String variantName = pageModule.dynamicContents.get(0).variantName;
        return variantName == null ? "" : variantName;
    }

    private boolean hasChipOfProductType(HomePageModule.PageModules pageModule, String chipProductType) {
        if (pageModule.dynamicContents == null || pageModule.dynamicContents.isEmpty()) {
            return false;
        }
        ArrayList<HomePageModule.Chip> chips = pageModule.dynamicContents.get(0).chips;
        if (chips == null) {
            return false;
        }
        return chips.stream().anyMatch(chip -> chipProductType.equalsIgnoreCase(chip.productType));
    }

    /** Templates carry their heading in different shapes, so try each in turn. */
    private String readModuleHeading(HomePageModule.PageModules pageModule) {
        if (pageModule.moduleDetail != null) {
            if (pageModule.moduleDetail.heading != null && isNotBlank(pageModule.moduleDetail.heading.title)) {
                return pageModule.moduleDetail.heading.title;
            }
            if (isNotBlank(pageModule.moduleDetail.title)) {
                return pageModule.moduleDetail.title;
            }
        }
        if (pageModule.dynamicContents != null && !pageModule.dynamicContents.isEmpty()) {
            return pageModule.dynamicContents.get(0).title;
        }
        return "";
    }

    private static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    public HomePageModule.PageModules getFirstBannerCarouselV2(HomePageModule homePageModuleResponse, boolean withChips, boolean withCTAButton, boolean withSubtitle, boolean withSeeAllLink) {
        for(HomePageModule.PageModules pageModule : homePageModuleResponse.data.pageModules) {
            if(pageModule.templateCode.equalsIgnoreCase("BANNER_CAROUSEL_V2")) {
                logger.log(pageModule.moduleDetail.heading.title);
                if(!(withChips && pageModule.dynamicContents.get(0).chips.size() > 1)) {
                    logger.log("chip size is 0");
                    continue;
                }

                if(pageModule.dynamicContents.get(0).chips.get(0).buttonText == null && withCTAButton) {
                    logger.log("buttonText is 0");
                    continue;
                }
                if(withSubtitle && pageModule.moduleDetail.heading.subtitle.isEmpty()) {
                    logger.log("subtitle is 0");
                    continue;
                }

                if(pageModule.moduleDetail.seeAllUrl == null && withSeeAllLink) {
                    logger.log("seeAllUrl is 0");
                    continue;
                }
                return pageModule;
            }
        }

        return null;
    }

    @SneakyThrows
    public String[] getHomeModules(boolean needLogin, String username,String password,String[] templateHome,String lang, String currency){
        String accessToken;
        if(needLogin){
            Unm<T> unm = new Unm<>(session);
            accessToken = unm.getAccessToken(username, password,"B2C");
            logger.log("access token: " + accessToken);
        }else {
            accessToken = getToken();
            logger.log("accessToken-> " + accessToken);
        }

        HomeModuleApi.Inputs homeModuleData = new HomeModuleApi.Inputs(String.valueOf(Configs.PLATFORM),accessToken, lang, currency);
        String[] availaibleModuleTitles = getAvailaibleModuleNames(homeModuleData,templateHome);
        logger.log("available home module -> " + Arrays.toString(availaibleModuleTitles));
        return availaibleModuleTitles;
    }

    @SneakyThrows
    public String getHomeModuleNonLogin(String templateHome,String lang,String currency){
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        HomeModuleApi.Inputs homeModuleData = new HomeModuleApi.Inputs(String.valueOf(Configs.PLATFORM),accessToken,lang,currency);
        String availaibleModule = getAvailaibleModuleName(homeModuleData,templateHome);
        logger.log("available home module -> " + availaibleModule);
        return availaibleModule;
    }

    protected String translateVerticalNameToBhs(String vertical) {
        String translatedVerticalName = CoreFunctions.verticalEngToId(vertical);
        return translatedVerticalName == null ? vertical : translatedVerticalName;
    }

    public void openLiveEventViaDeeplink(String productLink) {
        Deeplink liveEvent = new Deeplink("Video Live event",
                "tiket://video-gatotkaca.tiket.com/live?event=" + productLink,
                "tiket://video-preprod.tiket.com/live?event=" + productLink,
                "tiket://video.tiket.com/live?event=" + productLink);
        driverTools.deeplink(liveEvent);
        driverTools.switchToNative();
    }

    public abstract boolean isSuggestionItemListsShown();
    public abstract ArrayList<String> getSuggestionItemListTitle();
    public abstract String selectInfoPagesTab(String title);
    public abstract void selectInfoPagesItem();
    public abstract String selectInfoPagesItem(JSONObject objectItem, String selectedItem);
    public abstract boolean isInfoPagesDisplayed();
    public abstract void gotoHome();
    public abstract void goToHomeDeeplink();
    public abstract void goToHomeUsingDeeplinkWithoutCondition();

    /**
     * @return true when the Welcome Rewards bottom sheet ("Enjoy the new user's lucky gift!") is
     * visible within the given timeout. Shown to new users after onboarding only when the selected
     * currency is IDR.
     */
    public abstract boolean isWelcomeRewardsShown(int seconds);

    /** Closes the Welcome Rewards bottom sheet via its cross/close button (Android: iv_close, iOS: "ic cancel grey"). */
    public abstract void closeWelcomeRewards();

    /**
     * Marks onboarding as completed for the current session in the per-session flag map, so a later
     * {@code gotoHome()} on the same pooled driver skips the fresh-install onboarding path. Used by the
     * onboarding page objects which complete onboarding without going through {@code gotoHome()}.
     */
    public abstract void markOnboardingComplete();

    /**
     * Android: clears app data (including guest passenger cache) and relaunches. Resets onboarding
     * session flags so the next {@link #gotoHome()} runs first-launch handling. iOS: no-op.
     */
    public abstract void clearAppDataAndRelaunch();

    /** Opens the Welcome Rewards bottom sheet directly via the member/welcome-rewards deeplink. */
    public void openWelcomeRewardsViaDeeplink() {
        Deeplink welcomeRewards = new Deeplink("WelcomeRewards",
                "tiket://gatotkaca.tiket.com/member/welcome-rewards",
                "tiket://preprod.tiket.com/member/welcome-rewards",
                "tiket://www.tiket.com/member/welcome-rewards");
        driverTools.deeplink(welcomeRewards);
        // deeplink() does not restore native context; subsequent native locator waits need it explicitly.
        driverTools.switchToNative();
    }

    /**
     * @return true when the Welcome Rewards sheet appears naturally after onboarding, WITHOUT using the
     * deeplink. A blocking promo popup (if any) is dismissed once before the final re-check.
     */
    public boolean isWelcomeRewardsShownNaturally() {
        // The "halo tiket" coach mark can appear once right after onboarding and overlay (and hide) the
        // Welcome Rewards sheet, so dismiss it first before checking.
        if (isWelcomeRewardsShown(10)) {
            return true;
        }
        logger.log("Welcome Rewards not visible yet, dismissing any coach mark / promo popup and re-checking");
        closePromoPopup();
        return isWelcomeRewardsShown(5);
    }

    /**
     * Verifies the Welcome Rewards sheet is shown. If it does not appear naturally after onboarding
     * (e.g. an interstitial popup dismisses it on iOS), it falls back to opening it via deeplink.
     */
    public boolean isWelcomeRewardsShownWithFallback() {
        if (isWelcomeRewardsShownNaturally()) {
            return true;
        }
        logger.log("Welcome Rewards still not visible, opening via deeplink");
        openWelcomeRewardsViaDeeplink();
        return isWelcomeRewardsShown(20);
    }
    public abstract boolean checkRecentSearch(boolean location, String recentSearchCardtitle, String recentSearchAttribute);
    public abstract boolean checkTotalAllGuestRecentSearch(int totalAllGuest);
    public abstract void goToRecentSearch(String chipTitle);
    public abstract void clearRecentSearch();
    public abstract void clickHomeIcon();

    public abstract Boolean waitForHomePageToLoad();

    public abstract void openAccount();

    public abstract void closePromoPopup();

    public abstract void closeGiveRatingPopup();

    /**
     * Dismisses the "Select your preferred currency and country" popup that can appear after
     * navigating via deeplink (e.g. the mission page deeplink). Optional: clicks the apply/continue
     * button only if the popup is present, otherwise does nothing.
     */
    public abstract void closeCurrencyCountryPopup();

    public abstract boolean isCurrencyCountryPopupDisplayed();

    public abstract void clickCurrencyCountryPopupPrimaryButton();

    public abstract String getLanguageApp();

    public abstract void openVerticalPage(String vertical);
    public abstract boolean checkVerticalPresence(String vertical);

    public abstract boolean isUserLogin();

    public abstract boolean isOnHomePage();
    public abstract boolean isOnHomePage(int duration);

    public abstract boolean isInboxListOpen();

    public abstract void clickInboxMessage(String Message);

    public abstract boolean isInboxDetailHasSubtitle();

    public abstract void verifyInboxIcon();

    public abstract void clickInboxIcon();
    public abstract String clickOneOfFlashSaleInventory();
    public abstract void clickSeeAllFlashSale(String flashSaleTitle);
    public abstract boolean verifyRedirectFlashSale(String flashSaleInveentoryName);
    public abstract boolean isFlashListSaleSwipeAble(String flashSaleInveentoryName);
    public abstract boolean isFlashSaleInventoryPricePresent(String currency);
    public abstract boolean isFlashSaleCountDownPresent();

    public abstract void swipeToCard(String cardType);
    public abstract void goToSection(String sectionTitle);
    public abstract String getCampaignProduct(String productName);
    public abstract String getTheSubtitle(String titleSection);
    public abstract boolean swipeChipsButton(String titleSection);
    public abstract boolean compareBeforeAfterSwipe(String titleSection, String item, String inventoryName, boolean isSwipe);
    public abstract String selectChipsButton(String titleSection, String chipsText);
    public abstract String getCtaButtonTextApp(String titleSection);
    public abstract String getInventoryName(String titleSection);

    /**
     * Title of the first inventory card inside the home page-module headed by {@code titleSection}.
     *
     * <p>Unlike {@link #getInventoryName(String)} the heading is matched with {@code contains()},
     * so callers can pass the heading without its trailing punctuation — headings such as
     * "Take a peek at these recommendations!" do not survive an exact {@code @text} match.
     */
    public abstract String getSectionInventoryName(String titleSection);

    /**
     * Final (post-discount) price shown on the inventory card named {@code inventoryName} inside the
     * home page-module titled {@code titleSection} — e.g. {@code "IDR 1,179,489"} for the
     * accommodation recommendation card of "Grand Sahid Jaya".
     *
     * <p>The text is returned verbatim (currency code included) so callers can assert both the
     * currency and the amount, which is what the currency-switch regression on the recommendation
     * module needs: after switching currency the card must not keep the price of the previous one.
     *
     * @param titleSection  heading of the page module, as returned by the home-module API
     * @param inventoryName product title displayed on the card
     * @return the displayed final price text, including the currency code
     */
    public abstract String getInventoryPrice(String titleSection, String inventoryName);

    /**
     * Currency code part of a displayed price such as {@code "NZD 120"} → {@code "NZD"}.
     * Returns an empty string when the text has no leading currency code.
     */
    public String getCurrencyFromPrice(String priceText) {
        String trimmed = priceText == null ? "" : priceText.trim();
        int firstSpace = trimmed.indexOf(' ');
        return firstSpace < 0 ? "" : trimmed.substring(0, firstSpace);
    }

    public abstract boolean swipeInventoryList(String titleSection);
    public abstract String selectInventory(String titleSection, String inventoryList, String productType);
    public abstract boolean verifyFinanceBarRedirect(boolean isOnProd);
    public abstract boolean verifyFinanceBarText(FinanceBarPOJO data);
    public abstract void refreshHomePage(int numberOfRefrsh);

    /**
     * Scrolls the home feed to the dynamic widget. Unlike {@link #swipeToCard(String)} this does not
     * rely on the heading carrying a tv_title resource-id, which the dynamic widget does not.
     */
    public abstract void swipeToDynamicWidget(String sectionTitle);
    /** Heading of the dynamic widget section identified by {@code sectionTitle}. */
    public abstract String getDynamicWidgetTitle(String sectionTitle);
    /** Sub title rendered right below the dynamic widget heading. */
    public abstract String getDynamicWidgetSubtitle(String sectionTitle);
    /** Whether the dynamic widget artwork (banner image) is rendered. */
    public abstract boolean isDynamicWidgetImageDisplayed(String sectionTitle);
    /** Taps the info ("i") button overlaying the dynamic widget artwork. */
    public abstract void clickDynamicWidgetInfoButton(String sectionTitle);
    /** Whether the content opened by {@link #clickDynamicWidgetInfoButton(String)} is shown. */
    public abstract boolean isDynamicWidgetInfoDetailDisplayed();

    @SneakyThrows
    public String getInventoryRedirection(String productType) {
        String inventoryName = "";
        switch (productType.toUpperCase()) {
            case "TIXHOTEL" -> {
                DetailPage<T> detailPage = DetailPage.getInstance(session);
                detailPage.isHotelDetailShown();
                inventoryName = detailPage.getHotelNameDetail();
                detailPage.clickBackButton();
            }
            case "TODO" -> {
                WebViewPage<?> webViewPage = WebViewPage.getInstance(session);
                TtdProductDetailPageWeb<?> ttdProductDetailPageWeb = TtdProductDetailPageWeb.getInstance(session);
                webViewPage.switchToWebViewUrl("to-do");
                inventoryName = ttdProductDetailPageWeb.getTTDName();
                ttdProductDetailPageWeb.clickBackButton();
                webViewPage.switchToNative();
            }
            case "FLIGHT" -> {
                FlightSearchResultPage<T> flightSearchResultPage = FlightSearchResultPage.getInstance(session);
                flightSearchResultPage.isOnSearchResultPage();
                inventoryName = flightSearchResultPage.getRouteText()
                        .replace("ke", "→")
                        .replace("to", "→")
                        .replace("-", " ");
                flightSearchResultPage.backToFlightPage();
                FlightSearchPage.getInstance(session).backToHomePage();
            }
            default -> throw new RuntimeException("invalid productType -> " + productType);
        }
        return inventoryName;
    }

    public abstract void clickCtaButton(String titleSection);
    public abstract void clickMemberCompleteNow();
    public abstract void clickCard(String cardType); // FIXME: Implementation pending
    public abstract void openMyOrderPage();
    public abstract void clickOnRewardButton();
    public abstract boolean verifyIconsProduct();
    public abstract void setLocationService(String action);
    public abstract void clickVideo();
    public abstract void clickHeroBanner();
    public abstract void clickGlobalSearch();
    public abstract boolean verifyGlobalSearchInfo(GSLandingResponse gsLandingData);
    public abstract boolean verifyGLobalSearchHolidayInspiration(GSLandingResponse res);
    public abstract void enterKeywordToGlobalSearchBar(String keyword);
    public abstract void openGlobalSearchResult(String keyword);
    public abstract boolean verifyGlobalSearcWarningMinimunChar(String expectedWarningKeywordId,String expectedWarningKeywordEn,String currentLang);
    public abstract void clickPromoChip();
    public abstract void clickActivatePinCard();
    public abstract boolean verifyLocationBanner();
    public abstract void clickAllowLocationFromBanner();
    public abstract boolean isLocationBannerDisappear();
    public abstract boolean isOnPromoPage();
    public abstract void clickCtaButton(String[] sectionTitle);
    public abstract boolean isCurrencyOnPageModuleCorrect(String[] targetModuleNames, String currency);
    public abstract boolean verifyCardDefaultChips(HomePageModule.PageModules pageModules);
    public abstract boolean VerifyScrollCardDefaultChips(HomePageModule.PageModules pageModules);
    public abstract boolean verifyClickCard(HomePageModule.PageModules pageModules);
    public abstract void clickBackBtn();
    public abstract boolean clickSeeAllBtn(HomePageModule.PageModules pageModules);
    public abstract boolean verifyGlobalSearchVerticalSection(String verticalName);
    public abstract boolean verifyGlobalSearchArticleSection(GSSearchPageResponse res);
    public abstract void inputSearchKeyword(String keyword);
    public abstract void openWishlist();
    public String getCurrentEmail(){
        if (!isUserLogin()){
            return "invalid";
        }
        waitForHomePageToLoad();
        openAccount();
        AccountPage<T> accountPage = AccountPage.getInstance(session);
        accountPage.clickAccountDetail();
        AccountCenterPage<?> accountCenterPage = AccountCenterPage.getInstance(session);
        String currentEmail = accountCenterPage.getEmailAssociatedWithAccount();
        logger.log("Current Email : " + currentEmail);
        goToHomeDeeplink();
        return currentEmail;
    }

    public GSLandingResponse getGSLandingDataNonLogin(String lang) throws JsonSyntaxException, InterruptedException, ExecutionException {
        Gson gson = new Gson();
        String token = getToken();
        GlobalSearchLandingApi.Inputs input = new GlobalSearchLandingApi.Inputs(Configs.PLATFORM.name().toString(), token, lang);
        GlobalSearchLandingApi globalSearchLandingApi = new GlobalSearchLandingApi(input);
        ApiResponse res = globalSearchLandingApi.hitApi(apiTools);
        logger.log("Response api GlobalSearchLandingApi: " + res.getBody());
        return gson.fromJson(globalSearchLandingApi.hitApi(apiTools).getBody(), GSLandingResponse.class);
    }

    public GSLandingResponse getGsLandingResponseLogin(String lang, String username, String password) throws ExecutionException, InterruptedException {
        Gson gson = new Gson();
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password,"B2C");
        logger.log("access token: " + accessToken);

        GlobalSearchLandingApi.Inputs gsLandingInput = new GlobalSearchLandingApi.Inputs(Configs.PLATFORM.name().toString(), accessToken, lang);
        GlobalSearchLandingApi globalSearchLandingApi = new GlobalSearchLandingApi(gsLandingInput);
        ApiResponse res = globalSearchLandingApi.hitApi(apiTools);
        logger.log("Response from landing page login : " + res.getBody());
        return gson.fromJson(res.getBody(), GSLandingResponse.class);
    }
    public String getLanguageId(){
        HomePage<T> homePage = HomePage.getInstance(this.session);
        String lang = homePage.getLanguageApp().toUpperCase();
        logger.log("languageCode: " + lang);
        return lang;
    }

    @SneakyThrows
    public Object getModuleSection(boolean needLogin, String username, String password, String language, String templateCode, String processType, String currency) {
        String accessToken;
        if(needLogin){
            Unm<T> unm = new Unm<>(session);
            accessToken = unm.getAccessToken(username, password,"B2C");
            logger.log("access token: " + accessToken);
        }else {
            accessToken = getToken();
            logger.log("accessToken-> " + accessToken);
        }

        HomeModuleApi.Inputs homeModuleData = new HomeModuleApi.Inputs(String.valueOf(Configs.PLATFORM), accessToken, language, currency);
        HomeModuleApi homeModuleApi = new HomeModuleApi(homeModuleData);
        ApiResponse homeModuleApiResponse = homeModuleApi.hitApi(apiTools);

        return filterModuleSection(homeModuleApiResponse, templateCode, processType);
    }

    @SneakyThrows
    private Object filterModuleSection(ApiResponse homeModuleData, String templateCode, String processType) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(HomePageModule.DynamicContent.class, new DynamicContentDeserializer())
                .create();
        HomePageModule homePageModule = gson.fromJson(homeModuleData.getBody(), HomePageModule.class);
        ArrayList<HomePageModule.PageModules> pageModulesArrayList = homePageModule.data.pageModules;

        int totalPageModules = pageModulesArrayList.size();
        logger.log("totalPageModules: " + totalPageModules);
        ObjectMapper mapper = new ObjectMapper();
        for (var pageModule : pageModulesArrayList) {
            logger.log(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pageModule));
        }
        String titleSection = "";
        int pageModuleIndex = 0;
        for (int i = 0; i < totalPageModules; i++) {
            HomePageModule.PageModules pageModules = pageModulesArrayList.get(i);
            String apiTemplateCode = pageModules.templateCode;
            String apiProcessType = pageModules.processType;
            if (templateCode.equals("INFO_PAGES") && apiTemplateCode.equals("BANNER_GRID") && apiProcessType.equals(processType) && pageModules.sectionName.contains("INFO PAGE")) {
                titleSection = pageModulesArrayList.get(i).moduleDetail.heading.title;
                logger.log("titleSection: " + titleSection);
                pageModuleIndex = i;
                break;
            } else if (apiTemplateCode.equals(templateCode) && apiProcessType.equals(processType) && !pageModules.sectionName.equals("Travis Single Banner")) {
                titleSection = pageModulesArrayList.get(i).moduleDetail.heading.title;
                pageModuleIndex = i;
                break;
            }
        }

        Object result = new Object();
        switch (templateCode.toUpperCase()) {
            case "INFO_PAGES":
                InfoPagesPOJO infoPagesObject = new InfoPagesPOJO();
                infoPagesObject.titleSection = titleSection;
                ArrayList<String> titleTab = getChipTextItem(pageModulesArrayList, pageModuleIndex);
                JSONObject titleTabObject = new JSONObject();
                for (int i = 0; i < titleTab.size(); i++) {
                    titleTabObject.put(titleTab.get(i), getClickUrlItem(pageModulesArrayList, pageModuleIndex, i));
                    infoPagesObject.chipsItem = titleTabObject;
                }
                result = infoPagesObject;
                break;
            case "BANNER_GRID":
            case "IMAGE_WITH_CHIPS":
                SectionModulePOJO bannerGridObject = new SectionModulePOJO();
                bannerGridObject.titleSection = titleSection;
                bannerGridObject.subTitleSection = getSubTitleSection(pageModulesArrayList, pageModuleIndex);
                bannerGridObject.chipsItem = getChipsItem(pageModulesArrayList, pageModuleIndex);
                result = bannerGridObject;
                break;
            case "BANNER_CAROUSEL_V2":
                break;
            case "BANNER_PUSH_NOTIF":
                break;
            case "CARD_DEFAULT_CHIPS":
                break;
            default:
                throw new NotImplementedException("There is no \"" + templateCode + "\" as templateCode");
        }

        return result;
    }

    private String getSubTitleSection(ArrayList<HomePageModule.PageModules> pageModulesArrayList, int pageModuleIndex) {
        return pageModulesArrayList.get(pageModuleIndex).moduleDetail.heading.subtitle;
    }


    private ArrayList<String> getChipTextItem(ArrayList<HomePageModule.PageModules> pageModulesArrayList, int pageModuleIndex) {
        ArrayList<HomePageModule.Chip> chipArrayList = pageModulesArrayList.get(pageModuleIndex).dynamicContents.get(0).chips;
        ArrayList<String> titleTab = new ArrayList<>();
        for (HomePageModule.Chip chip : chipArrayList) {
            titleTab.add(chip.text);
        }
        return titleTab;
    }

    private ArrayList<String> getClickUrlItem(ArrayList<HomePageModule.PageModules> pageModulesArrayList, int pageModuleIndex, int bannerContentsIndex) {
        ArrayList<HomePageModule.BannerContents> bannerContentsArrayList = pageModulesArrayList.get(pageModuleIndex).dynamicContents.get(0).chips.get(bannerContentsIndex).bannerContents;
        ArrayList<String> urlItem = new ArrayList<>();
        for (HomePageModule.BannerContents bannerContents : bannerContentsArrayList) {
            urlItem.add(bannerContents.clickUrl);
        }
        return urlItem;
    }

    private ArrayList<SectionModulePOJO.Chips> getChipsItem(ArrayList<HomePageModule.PageModules> pageModulesArrayList, int pageModuleIndex) {
        ArrayList<HomePageModule.Chip> chipResponse = pageModulesArrayList.get(pageModuleIndex).dynamicContents.get(0).chips;
        ArrayList<SectionModulePOJO.Chips> chipsArrayList = new ArrayList<>();
        for (int i = 0; i < chipResponse.size(); i++) {
            SectionModulePOJO.Chips object = new SectionModulePOJO.Chips();
            object.titleChips = chipResponse.get(i).text;
            object.buttonText = chipResponse.get(i).buttonText;
            object.productType = chipResponse.get(i).productType;
            if (chipResponse.get(i).bannerContents != null) {
                ArrayList<SectionModulePOJO.Content> contentResponse = getContentItems(chipResponse, i);
                for (int j = 0; j < contentResponse.size(); j++) {
                    object.contentsItem = contentResponse;
                }
            }
            chipsArrayList.add(object);
        }
        return chipsArrayList;
    }

    private ArrayList<SectionModulePOJO.Content> getContentItems(ArrayList<HomePageModule.Chip> chipResponse, int chipsIndex) {
        ArrayList<HomePageModule.BannerContents> bannerContentsResponse = chipResponse.get(chipsIndex).bannerContents;
        ArrayList<SectionModulePOJO.Content> contentArrayList = new ArrayList<>();
        for (HomePageModule.BannerContents bannerContents : bannerContentsResponse ) {
            SectionModulePOJO.Content object = new SectionModulePOJO.Content();
            object.clickUrl = bannerContents.clickUrl;
            contentArrayList.add(object);
        }
        return contentArrayList;
    }

    @SneakyThrows
    public boolean checkChipsButtonTextApi(SectionModulePOJO sectionModulePojo, String chipsButtonText) {
        return sectionModulePojo.chipsItem.stream().anyMatch(chips -> chips.titleChips.equals(chipsButtonText));
    }

    @SneakyThrows
    public String getCtaButtonTextApi(SectionModulePOJO imageWithChipsModule, String selectedChipButton) {
        SectionModulePOJO.Chips data = imageWithChipsModule.chipsItem.stream().filter(chips -> chips.titleChips.equals(selectedChipButton)).findFirst().orElseThrow();
        return data.buttonText;
    }

    @SneakyThrows
    public String[] getPromoSection(boolean needLogin, String username, String password, String language, String currency) {
        String accessToken;
        if(needLogin){
            Unm<T> unm = new Unm<>(session);
            accessToken = unm.getAccessToken(username, password,"B2C");
            logger.log("access token: " + accessToken);
        }else {
            accessToken = getToken();
            logger.log("accessToken-> " + accessToken);
        }

        HomeModuleApi.Inputs homeModuleData = new HomeModuleApi.Inputs(String.valueOf(Configs.PLATFORM),accessToken,language, currency);
        HomeModuleApi homeModuleApi = new HomeModuleApi(homeModuleData);
        ApiResponse homeModuleApiResponse = homeModuleApi.hitApi(apiTools);

        String[] promoSectionTitle = filterPromoSection(homeModuleApiResponse);

        return promoSectionTitle;
    }

    private String[] filterPromoSection(ApiResponse homeModuleData) {
        String result = "";

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(HomePageModule.DynamicContent.class, new DynamicContentDeserializer())
                .create();
        HomePageModule homeModuleApiResponseObject = gson.fromJson(homeModuleData.getBody(), HomePageModule.class);
        HomePageModule.Data responseData = homeModuleApiResponseObject.data;
        ArrayList<HomePageModule.PageModules> modulesData = responseData.pageModules;

        String buttonText = null;
        for (HomePageModule.PageModules module : modulesData) {
            String templateCode = module.templateCode;
            String processType = module.processType;
            logger.log(templateCode);

            if (templateCode.equals("BANNER_CAROUSEL_V2")) {
                ArrayList<HomePageModule.DynamicContent> dynamicContentArrayList = module.dynamicContents;
                HomePageModule.DynamicContent firstDynamicContent = dynamicContentArrayList.get(0);

                ArrayList<HomePageModule.Chip> chipsArrayList = firstDynamicContent.chips;
                HomePageModule.Chip firstChip = chipsArrayList.get(0);

                String buttonUrl = firstChip.buttonUrl == null ? "" : firstChip.buttonUrl;
                buttonText = firstChip.buttonText;

                if (buttonUrl.contains("tiket.com/promo") || buttonUrl.startsWith("/promo")) {
                    logger.log("Full buttonUrl : '" + buttonUrl + "'");
                    result += module.moduleDetail.heading.title;
                    break;
                }
            }
        }

        logger.log("Section Title : '" + result + "' \nButton Text : '" + buttonText + "'");
        return new String[]{result, buttonText};
    }

    @SneakyThrows
    public HomePageModule.PageModules getHomeModuleLogin(String username,String password,String templateHome,String lang, String currency){
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password,"B2C");
        logger.log("access token: " + accessToken);


        HomeModuleApi.Inputs homeModuleData = new HomeModuleApi.Inputs(String.valueOf(Configs.PLATFORM),accessToken,lang, currency);
        HomePageModule.PageModules pageModules = getHomePageModule(homeModuleData,templateHome);
        logger.log("available home module -> " + pageModules.templateCode);
        logger.log("language home module -> " + pageModules.moduleDetail.heading.lang);
        return pageModules;
    }

    @SneakyThrows
    public HomePageModule.PageModules getHomeModuleGuest(String templateHome,String lang, String currency){
        String accessToken = getToken();
        logger.log("accessToken-> " + accessToken);

        HomeModuleApi.Inputs homeModuleData = new HomeModuleApi.Inputs(String.valueOf(Configs.PLATFORM),accessToken,lang, currency);
        HomePageModule.PageModules pageModules = getHomePageModule(homeModuleData,templateHome);
        logger.log("available home module -> " + pageModules.templateCode);
        logger.log("language home module -> " + pageModules.moduleDetail.heading.lang);
        return pageModules;
    }

    private HomePageModule.PageModules getHomePageModule(HomeModuleApi.Inputs inputs,String templateCode) throws ExecutionException, InterruptedException{
        HomeModuleApi homeModuleApi = new HomeModuleApi(inputs);
        HomePageModule.Data data = null;
        int count = 0;
        do {
            ApiResponse apiResponse = homeModuleApi.hitApi(apiTools);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(HomePageModule.DynamicContent.class, new DynamicContentDeserializer())
                    .create();
            HomePageModule homePageModule = gson.fromJson(apiResponse.getBody(), HomePageModule.class);
            data = homePageModule.data;
            Thread.sleep(1000);
            logger.log("retryCount : " + count);
            count++;
        }while (data == null && count<5);

        for (HomePageModule.PageModules pageModules : data.pageModules){
            if (pageModules.templateCode.equals(templateCode)){
                logger.log("data: " + data);
                return pageModules;
            }
        }
        return null;
    }

    public GSSearchPageResponse getGlobalSearchResultResponse(String lang , String keyWord) throws ExecutionException, InterruptedException, ParseException {
        Gson gson = new Gson();

        String token = getToken();
        GlobalSearchResultApi.Inputs gsSearchInput = new GlobalSearchResultApi.Inputs(Configs.PLATFORM.name().toString(), token, lang,keyWord);
        GlobalSearchResultApi globalSearchPageApi = new GlobalSearchResultApi(gsSearchInput);
        String responseString = globalSearchPageApi.hitApi(apiTools).getBody();
        logger.log("respons global search result: " + responseString);
        GSSearchPageResponse resFinal = gson.fromJson(responseString, GSSearchPageResponse.class);
        return resFinal;
    }

    public void swipeUP() {
        driverTools.swipeUp();
    }

    public abstract boolean checkRunningTextFinanceBar();
    public abstract boolean checkRunningTextFinanceBarForNonIdrCurrency(String currency, String language);
    public abstract void clickPayLaterOnFinanceBar();

    protected String getNonIdrFinanceBarExpectedMessage(String currency, String language) {
        if ("ENG".equalsIgnoreCase(language) || "en".equalsIgnoreCase(language)) {
            return "These features are unavailable because you\u2019re currently using " + currency + ".";
        }
        return "Fitur-fitur ini nggak bisa dipakai karena saat ini kamu menggunakan " + currency + ".";
    }
    public abstract boolean areIdrLoginDetailsShown(String tier);
    public abstract boolean checkLoginTextFinanceBar();
    public abstract void clickFinanceBar();
    public abstract Boolean isFinanceBarDisabled();
    public abstract Boolean isToolTipMessageAppears(String currency);
    public abstract boolean isBannerCarouselV2ComponentValid(String pageModuleTitle, String sectionType);
    public abstract void clickPageModuleChip(String pageModuleTitle, String byType, String input);
    public abstract boolean verifySeasonalBanner(HomePageModule.PageModules pageModule);
    public abstract boolean verifySeasonalBannerClick(HomePageModule.PageModules pageModules);
    public abstract boolean verifySeasonalBannerScrollable(HomePageModule.PageModules pageModules);
    public abstract void clickBannerCarouselV2BannerByIndex(HomePageModule.PageModules targetPageModule, int index);
    public Boolean isBannerRedirectdValid(HomePageModule.PageModules targetPageModule,int chipIndex, int clickedBannerIndex) {
        String expectedUrl = targetPageModule.dynamicContents.get(0).chips.get(chipIndex).bannerContents.get(clickedBannerIndex-1).clickUrl;
        if(isOnWebviewPage(expectedUrl)) {
            return true;
        } else {
            return !isOnHomePage();
        }
    }

    public void changeCurrency(String currency,String username) {
        SettingPage<T> settingPage = SettingPage.getInstance(session);
        settingPage.changeCurrencyIfOtherCurrencyIsSelectedDeeplink(currency);
        refreshHomePage(1);
        if (Configs.PLATFORM == Platform.IOS) {
            driverTools.waitForElementToAppear(CLOSE_GIVE_RATING_POPUP, 5).ifPresent(driverTools::click);
        }
        this.waitForHomePageToLoad();
    }

    public boolean openAccountCenter(String email){
        if (Configs.PLATFORM == Platform.IOS) {
            driverTools.waitForElementToAppear(CLOSE_GIVE_RATING_POPUP, 5).ifPresent(driverTools::click);
        }
        AccountPage<T> accountPage = AccountPage.getInstance(session);
        SettingPage<T> settingPage = SettingPage.getInstance(session);
        openAccount();
        accountPage.clickEditProfileDetail();
        driverTools.swipeDown(2);
        settingPage.clickAccountCenter();
        AccountCenterPage<?> accountCenterPage = AccountCenterPage.getInstance(session);
        return email.equalsIgnoreCase(accountCenterPage.getEmailAssociatedWithAccount());
    }

    public boolean openAccountCenterViaDeeplink(String username){
        SettingPage<T> settingPage = SettingPage.getInstance(session);
        String endPoint = "myaccount/passengers/detail";
        Deeplink accountCenter = new Deeplink("AccountCenterPage/" + endPoint,
                "tiket://gatotkaca.tiket.com/" + endPoint,
                "tiket://preprod.tiket.com/" + endPoint,
                "tiket://tiket.com/" + endPoint);
        try {
            driverTools.deeplink(accountCenter);
        }catch (Exception e){
            logger.log(e.getLocalizedMessage());
        }
        return settingPage.isLoginWithCorrectCredentials(username);
    }

    public String openAccountCenter(){
        AccountPage<T> accountPage = AccountPage.getInstance(session);
        SettingPage<T> settingPage = SettingPage.getInstance(session);
        openAccount();
        accountPage.clickEditProfileDetail();
        driverTools.swipeDown(2);
        settingPage.clickAccountCenter();
        AccountCenterPage<?> accountCenterPage = AccountCenterPage.getInstance(session);
        return accountCenterPage.getEmailAssociatedWithAccount();
    }

    public String getTiketPointAndChangeCurrency(String currency){
        AccountPage<T> accountPage = AccountPage.getInstance(session);
        SettingPage<T> settingPage = SettingPage.getInstance(session);

        accountPage.openAccountPageUsingDeeplink();
        settingPage.changeCurrencyIfOtherCurrencyIsSelected(currency);
        String tiketPoints = accountPage.getLoyaltyPointValue();
        logger.log("total tiket points: " + tiketPoints);
        goToHomeDeeplink();
        for(int i=0; i<10 && !this.waitForHomePageToLoad(); i++) {
            logger.log("Waited for homepage to load, retrying " + (i+1) + " times.");
        }
        return tiketPoints;
    }

    public abstract boolean scrollUntilBottom(int numberOfClicks);
    public abstract boolean scrollBackToTop();
    public abstract boolean checkHighLightDiscoverModule(String title);
    public abstract boolean checkHighLightDiscoverModuleProduct(String product);
    public abstract boolean checkHighLightDiscoverModuleLocation(String Location);
    public abstract boolean checkHighLightDiscoverModulePrice(String price);
    public abstract boolean checkHighLightDiscoverModuleReview(String review);
    public abstract boolean checkHighLightDiscoverModuleRating(String rating);
    public abstract boolean verifyUnreadCount(Map<String,Integer> unreadCount);
    public abstract boolean verifySwipeHighlightDiscoverArticle(HighlightDiscoverPOJO highlightArticleSection);
    public abstract boolean verifyClickHighlightDiscoverArticle(HighlightDiscoverPOJO highlightArticleSection);
    public abstract boolean verifyHighlightDiscoverArticleTitle(HighlightDiscoverPOJO highlightArticleSection);
    public abstract boolean verifyDiscoverModuleProductRedirection(String title);

    public Map<String,Integer> getUnreadCount(String lang,String currency,String username,String password) throws ExecutionException, InterruptedException{
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(HomePageModule.DynamicContent.class, new DynamicContentDeserializer())
                .create();

        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password,"B2C");
        logger.log("access token: " + accessToken);
        Map<String,Integer> unreadCounts = new HashMap<String,Integer>();
        HomeModuleApi.Inputs homeModuleData = new HomeModuleApi.Inputs(String.valueOf(Configs.PLATFORM),accessToken, lang, currency);
        HomeModuleApi homeModuleApi = new HomeModuleApi(homeModuleData);
        ApiResponse apiResponse = homeModuleApi.hitApi(apiTools);
        HomePageModule homePageModule = gson.fromJson(apiResponse.getBody(), HomePageModule.class);
        logger.log("inboxTotalUnreadCount : " + homePageModule.data.inboxUnreadCount.totalCount);
        logger.log("chatTotalUnreadCount : " + homePageModule.data.chatUnreadCount);
        unreadCounts.put("inboxTotalUnreadCount", homePageModule.data.inboxUnreadCount.totalCount);
        unreadCounts.put("chatTotalUnreadCount", homePageModule.data.chatUnreadCount);
        return unreadCounts;
    }

    @SneakyThrows
    public FinanceBarPOJO getFinanceBarTexts(String lang,String currency,String username,String password){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(HomePageModule.DynamicContent.class, new DynamicContentDeserializer())
                .create();
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password,"B2C");
        logger.log("access token: " + accessToken);
        HomeModuleApi.Inputs homeModuleData = new HomeModuleApi.Inputs(String.valueOf(Configs.PLATFORM),accessToken, lang, currency);
        HomeModuleApi homeModuleApi = new HomeModuleApi(homeModuleData);
        ApiResponse apiResponse = homeModuleApi.hitApi(apiTools);
        HomePageModule homePageModule = gson.fromJson(apiResponse.getBody(), HomePageModule.class);

        FinanceBarPOJO result = new FinanceBarPOJO();
        result.tiketPoints = getTextFinanceBar(homePageModule.data.blibliTiketPoint,"blibliTiketPoint");
        result.payLater = getTextFinanceBar(homePageModule.data.paylater,"paylater");
        result.blibliPay = getTextFinanceBar(homePageModule.data.blipay,"blipay");

        return result;
    }

    private String getTextFinanceBar(HomePageModule.FinanceBarItem data, String type){

        String text;
        if(data.content == null){
            text = data.contents.get(0).text;
        } else{
            text = data.content.text;
        }
        logger.log("Got text from type " +type+" "+text);
        return text;
    }

    @SneakyThrows
    public HighlightDiscoverPOJO getHighlightDiscoverArticleSection(boolean needLogin, String username, String password, String language, String currency) {
        String accessToken;
        if(needLogin){
            Unm<T> unm = new Unm<>(session);
            accessToken = unm.getAccessToken(username, password,"B2C");
            logger.log("access token: " + accessToken);
        }else {
            accessToken = getToken();
            logger.log("accessToken-> " + accessToken);
        }

        HomeModuleApi.Inputs homeModuleData = new HomeModuleApi.Inputs(String.valueOf(Configs.PLATFORM),accessToken,language, currency);
        HomeModuleApi homeModuleApi = new HomeModuleApi(homeModuleData);
        ApiResponse homeModuleApiResponse = homeModuleApi.hitApi(apiTools);

        HighlightDiscoverPOJO highlightDiscoverArticleSection = filterHighlightDiscoverArticleSection(homeModuleApiResponse);

        return highlightDiscoverArticleSection;
    }

    private HighlightDiscoverPOJO filterHighlightDiscoverArticleSection(ApiResponse homeModuleData) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(HomePageModule.DynamicContent.class, new DynamicContentDeserializer())
                .create();
        HomePageModule homeModuleApiResponseObject = gson.fromJson(homeModuleData.getBody(), HomePageModule.class);
        HomePageModule.Data responseData = homeModuleApiResponseObject.data;
        ArrayList<HomePageModule.PageModules> modulesData = responseData.pageModules;

        HighlightDiscoverPOJO highlightDiscoverPOJO = new HighlightDiscoverPOJO();

        for (HomePageModule.PageModules module : modulesData) {
            String templateCode = module.templateCode;

            if (templateCode.equals("HIGHLIGHT_DISCOVER_ARTICLE")) {
                ArrayList<HomePageModule.DynamicContent> dynamicContentArrayList = module.dynamicContents;
                String moduleLabel = module.moduleDetail.label;
                String moduleTitle = module.moduleDetail.title;
                String moduleSubtitle = module.moduleDetail.description;

                HomePageModule.DynamicContent firstDynamicContent = dynamicContentArrayList.get(0);

                String dynamicContentTitle = firstDynamicContent.title;
                String dynamicContentButtonText = firstDynamicContent.buttonTitle;
                String dynamicContentUrlRedirect = firstDynamicContent.url;
                String dynamicContentSubtitle = firstDynamicContent.description;
                logger.log("Module Title : '" + moduleTitle + " \nSection Title : '" + dynamicContentTitle + " \nSection Subtitle : '" + dynamicContentSubtitle + "' \nButton Text : '" + dynamicContentButtonText + "'" + "' \nUrl : '" + dynamicContentUrlRedirect + "'");
                highlightDiscoverPOJO.moduleLabel =moduleLabel;
                highlightDiscoverPOJO.moduleTitle =moduleTitle;
                highlightDiscoverPOJO.moduleSubtitle =moduleSubtitle;
                highlightDiscoverPOJO.dynamicContentTitle =dynamicContentTitle;
                highlightDiscoverPOJO.dynamicContentButtonText =dynamicContentButtonText;
                highlightDiscoverPOJO.dynamicContentUrlRedirect =dynamicContentUrlRedirect;
                highlightDiscoverPOJO.dynamicContentSubtitle =dynamicContentSubtitle;

                return highlightDiscoverPOJO;
            }
        }

        throw new RuntimeException("No Home Module with template code: HIGHLIGHT_DISCOVER_ARTICLE");
    }

    public boolean canBeBookForHalfPoint(String totalPrice,String totalPoint){
        logger.log("total price : " + totalPrice);
        logger.log("total tiket points available : " + totalPoint);
        return Integer.parseInt(totalPrice) > Integer.parseInt(totalPoint);
    }

    public boolean canBeBookForFullPoint(String totalPrice,String totalPoint){
        logger.log("total price : " + totalPrice.replaceAll("\\.", ""));
        logger.log("total tiket points available : " + totalPoint.replaceAll("\\.", ""));
        return Double.parseDouble(totalPrice) <= Double.parseDouble(totalPoint);
    }

    public abstract void verifyContinueToPaymentSection(String name, String totalPrice);
    public abstract void clickContinueToPayment();
    public abstract void verifyClaimGVSection(HomePageModule.PageModules pageModules);
    public abstract void clickGVVoucher(ArrayList<HomePageModule.DynamicContent> dynamicContents);
    public abstract void clickGVCtaBtn(String btnTitle);
    public abstract void swipeToCtaBtn(String btnName);
    @SneakyThrows
    public boolean addOrRemovePhoneNumberVerification(
            String userAccountId, String note, boolean isVerification
    ){
        //to check unique note present in response
        note = note + CoreFunctions.getCurrentTime();
        logger.log("user account id: " + userAccountId);
        logger.log("user note: " + note);
        logger.log("verification: " + isVerification);
        String auth = getAdminAccessTokenDashboardAuth();

        MemberVerificationByAdminApi.Inputs memberVerificationByAdminApi = new MemberVerificationByAdminApi.Inputs(userAccountId,"Bearer " + auth,note,isVerification);
        boolean isRemovePNVSuccess = addOrRemovePNV(memberVerificationByAdminApi);
        logger.log("is remove pnv success : " + isRemovePNVSuccess);
        return true;
    }

    private String getAdminAccessTokenDashboardAuth() throws ExecutionException, InterruptedException{
        POSTGetAdminAccessTokenDashboardApi postGetAdminAccessTokenDashboardApi = new POSTGetAdminAccessTokenDashboardApi();
        ApiResponse response = postGetAdminAccessTokenDashboardApi.hitApi(apiTools);
        String isSuccessAuth = new JSONArray(response.getBody()).getJSONObject(0).getJSONObject("data").getJSONObject("loginUser").get("message").toString();
        logger.log("res : " + isSuccessAuth);
        if("SUCCESS".equals(isSuccessAuth)){
            Map<String,ArrayList<Object>> header = response.getHeaderParams();
            ArrayList<Object>  listSetCookies = header.get("set-cookie");
            String accessToken = listSetCookies.get(0).toString().contains("session_access_token")?listSetCookies.get(0).toString():listSetCookies.get(1).toString();
            logger.log("token : " + accessToken);
            int start = accessToken.indexOf('=') + 1;
            int end= accessToken.indexOf(';');
            accessToken = accessToken.substring(start,end);
            logger.log("session_access_token= " + accessToken);
            return accessToken;
        }
        return null;
    }

    private boolean addOrRemovePNV(MemberVerificationByAdminApi.Inputs removePhoneVerificationApiInputs) throws ExecutionException, InterruptedException{
        MemberVerificationByAdminApi removePhoneVerificationApi = new MemberVerificationByAdminApi((removePhoneVerificationApiInputs));
        ApiResponse response = removePhoneVerificationApi.hitApi(apiTools);
        Gson gson = new Gson();
        RemovePhoneNumberVerificationResponse removePhoneNumberVerificationResponse = gson.fromJson(response.getBody(),RemovePhoneNumberVerificationResponse.class);
        for (RemovePhoneNumberVerificationResponse.Datum data : removePhoneNumberVerificationResponse.data){
            if(data.note.equals(removePhoneVerificationApiInputs.note())){
                return true;
            }
        }
        return false;
    }

    @SneakyThrows
    public HomePageModule.PageModules getHomeModuleLoginGVClaim(String username,String password,String templateHome,String lang, String currency){
        Unm<T> unm = new Unm<>(session);
        String accessToken = unm.getAccessToken(username, password,"B2C");
        logger.log("access token: " + accessToken);

        HomeModuleApi.Inputs homeModuleData = new HomeModuleApi.Inputs(String.valueOf(Configs.PLATFORM),accessToken,lang, currency);
        HomePageModule.PageModules pageModules = getHomePageGVModule(homeModuleData,templateHome);
        logger.log("available home module -> " + pageModules.templateCode);
        logger.log("language home module -> " + pageModules.moduleDetail.heading.lang);
        return pageModules;
    }

    private HomePageModule.PageModules getHomePageGVModule(HomeModuleApi.Inputs inputs,String templateCode) throws ExecutionException, InterruptedException{
        HomeModuleApi homeModuleApi = new HomeModuleApi(inputs);
        ApiResponse apiResponse = homeModuleApi.hitApi(apiTools);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(HomePageModule.DynamicContent.class, new DynamicContentDeserializer())
                .create();
        HomePageModule homePageModule = gson.fromJson(apiResponse.getBody(), HomePageModule.class);
        for (HomePageModule.PageModules pageModules : homePageModule.data.pageModules){
            if (pageModules.templateCode.equals(templateCode) && pageModules.moduleDetail.cta != null){
                logger.log("template code: " + pageModules.templateCode);
                return pageModules;
            }
        }
        return null;
    }

    @SneakyThrows
    public HomePageVerticalDataPojo.VerticalIcons getHomeModuleVerticalGuest(String lang, String currency){
        Environment environment = Configs.ENVIRONMENT;
        if(environment == Environment.PROD)
            environment = Environment.PREPROD;

        String accessToken = getToken(environment);
        logger.log("accessToken-> " + accessToken);

        HomeModuleWithEnvApi.Inputs homeModuleWithEnvApiInputs = new HomeModuleWithEnvApi.Inputs(String.valueOf(Configs.PLATFORM),accessToken,lang, currency,environment);
        HomePageVerticalDataPojo.VerticalIcons verticalIcons = getHomePageModule(homeModuleWithEnvApiInputs);
        logger.log("sequence id -> " + verticalIcons.sequenceId);
        return verticalIcons;
    }

    private HomePageVerticalDataPojo.VerticalIcons getHomePageModule(HomeModuleWithEnvApi.Inputs inputs) throws ExecutionException, InterruptedException{
        HomeModuleWithEnvApi homeModuleApi = new HomeModuleWithEnvApi(inputs);
        ApiResponse apiResponse = homeModuleApi.hitApi(apiTools);

        Gson gson = new Gson();
        HomePageVerticalDataPojo homePageModule = gson.fromJson(apiResponse.getBody(), HomePageVerticalDataPojo.class);
        HomePageVerticalDataPojo.Data data = homePageModule.data;
        return data.verticalIcons;
    }

    private DeeplinkVerticalPOJO setDeeplinkGuestPojo(HomePageVerticalDataPojo.VerticalIcons verticalIcons){
        DeeplinkVerticalPOJO deeplinkPojo = new DeeplinkVerticalPOJO();
        for(HomePageVerticalDataPojo.Vertical vertical:verticalIcons.verticals){
            logger.log("vertical code: " + vertical.code + " | clickUrl: " + vertical.clickUrl);
            switch (vertical.code){
                case "flight" : deeplinkPojo.flightDeeplink = vertical.clickUrl;
                    break;
                case "hotel" : deeplinkPojo.hotelDeeplink = vertical.clickUrl;
                    break;
                case "homes" : deeplinkPojo.homesDeeplink = vertical.clickUrl;
                    break;
                case "to-do" : deeplinkPojo.ttdDeeplink = vertical.clickUrl;
                    break;
                case "train" : deeplinkPojo.trainDeeplink = vertical.clickUrl;
                    break;
                case "attractions": deeplinkPojo.attractionsDeeplink = vertical.clickUrl;
                    break;
                case "car" : deeplinkPojo.carDeeplink = vertical.clickUrl;
                    break;
                case "refer" : deeplinkPojo.referDeeplink = vertical.clickUrl;
                    break;
                case "airport-transfer" : deeplinkPojo.airportTransferDeeplink = vertical.clickUrl;
                    break;
                case "claim-center" : deeplinkPojo.claimCenterDeeplink = vertical.clickUrl;
                    break;
                case "ferry" : deeplinkPojo.ferryDeeplink = vertical.clickUrl;
                    break;
                case "bus" : deeplinkPojo.busDeeplink = vertical.clickUrl;
                    break;
                case "insurance" : deeplinkPojo.insuranceDeeplink = vertical.clickUrl;
                    break;
                case "kcic" : deeplinkPojo.whooshDeeplink = vertical.clickUrl;
                    break;
                default: logger.log("Unimplemented vertical type! " + vertical.code);
                    break;
            }
        }
        return deeplinkPojo;
    }

    public DeeplinkVerticalPOJO getDeeplinkVerticalGuest(String language,String currency){
        HomePageVerticalDataPojo.VerticalIcons verticalIcons= getHomeModuleVerticalGuest(language,currency);
        return setDeeplinkGuestPojo(verticalIcons);
    }

    /**
     * Uses hardcoded deeplink URL directly without requiring API POJO.
     * This is used as a fallback when API fails.
     */
    private void useHardcodedDeeplink(String verticalName) {
        String hardcodedUrl = getHardcodedDeeplink(verticalName);
        if (hardcodedUrl == null) {
            throw new RuntimeException("No hardcoded URL available for vertical: " + verticalName);
        }
        
        logger.softWarning("Using hardcoded deeplink URL for vertical '" + verticalName + "': " + hardcodedUrl);
        String cleanedPath = extractPathFromClickUrl(hardcodedUrl);
        logger.log("Hardcoded deeplink (raw): " + hardcodedUrl);
        logger.log("Hardcoded deeplink (cleaned): " + cleanedPath);
        
        String deeplinkGK = "tiket://m.gatotkaca.tiket.com" + cleanedPath;
        String deeplinkPreProd = "tiket://m.preprod.tiket.com" + cleanedPath;
        String deeplinkProd = "tiket://m.tiket.com" + cleanedPath;

        Deeplink verticalPage = new Deeplink("VerticalPage",
                deeplinkGK,
                deeplinkPreProd,
                deeplinkProd);
        
        driverTools.deeplink(verticalPage);
    }

    public void openVerticalPageDeeplink(String verticalName,String language,String currency){
        if(Configs.CODE_COVERAGE && Configs.PLATFORM == Platform.IOS) {
            openVerticalPage(verticalName);
        }

        String sessionId = session.sessionId();

        language = language.isEmpty() ? "en" : language;
        currency = currency.isEmpty() ? "IDR" : currency;

        try {
            if(deeplinkMapData.get(sessionId) == null || getVerticalDeeplink(verticalName,deeplinkMapData.get(sessionId)) == null) {
                DeeplinkVerticalPOJO deeplinkVerticalPOJO = getDeeplinkVerticalGuest(language, currency);
                deeplinkMapData.put(sessionId, deeplinkVerticalPOJO);
            }
            clickHomeProductDeeplink(verticalName, deeplinkMapData.get(sessionId));
        } catch (Exception e){
            logger.log("API call failed: " + e.getLocalizedMessage());
            // Try hardcoded URL before falling back to UI
            try {
                useHardcodedDeeplink(verticalName);
            } catch (Exception ex) {
                logger.log("Hardcoded deeplink also failed: " + ex.getLocalizedMessage());
                goToHomeUsingDeeplinkWithoutCondition();
                openVerticalPage(verticalName);
            }
        }
        if (Configs.PLATFORM == Platform.IOS) {
            driverTools.waitForElementToAppear(CLOSE_GIVE_RATING_POPUP, 5).ifPresent(driverTools::click);
        }
    }

    private String getVerticalDeeplink(String verticalName,DeeplinkVerticalPOJO deeplinkVerticalPOJO){
        return switch(verticalName){
            case "Homes", "homes" -> deeplinkVerticalPOJO.homesDeeplink;
            case "To Dos", "To Do" -> deeplinkVerticalPOJO.ttdDeeplink;
            case "Car Transfers", "Jemputan Bandara" -> deeplinkVerticalPOJO.airportTransferDeeplink;
            case "Hotels", "Hotel", "hotel" -> deeplinkVerticalPOJO.hotelDeeplink;
            case "Trains", "Train","Kereta Api" -> deeplinkVerticalPOJO.trainDeeplink;
            case "Whoosh","whoosh" -> deeplinkVerticalPOJO.whooshDeeplink;
            case "Flights","Pesawat" -> deeplinkVerticalPOJO.flightDeeplink;
            case "Car Rentals","Sewa Mobil" -> deeplinkVerticalPOJO.carDeeplink;
            case "Bus & Shuttle","Bus & Travel","Bus" -> deeplinkVerticalPOJO.busDeeplink;
            case "Claim Center" -> deeplinkVerticalPOJO.claimCenterDeeplink;
            case "ferry", "Ferry" -> deeplinkVerticalPOJO.ferryDeeplink;
            case "insurance", "Insurance" -> deeplinkVerticalPOJO.insuranceDeeplink;
            default -> throw new NotImplementedException("Unimplemented vertical type!");
        } ;
    }

    /**
     * Gets hardcoded deeplink URL for specific verticals as fallback when API fails.
     * Only returns hardcoded URLs for supported verticals.
     *
     * @param verticalName The name of the vertical
     * @return Hardcoded deeplink URL or null if not supported
     */
    private String getHardcodedDeeplink(String verticalName) {
        return switch(verticalName) {
            case "Homes", "homes" -> "/homes";
            case "To Dos", "To Do" -> "/to-do";
            case "Car Transfers", "Jemputan Bandara" -> "/airport-transfer";
            case "Hotels", "Hotel", "hotel" -> "/hotel";
            case "Trains", "Train", "Kereta Api" -> "/kereta-api";
            case "Whoosh", "whoosh" -> "/kereta-api?productType=whoosh";
            case "Flights", "Pesawat" -> "/pesawat";
            case "Car Rentals", "Sewa Mobil" -> "/sewa-mobil";
            case "Bus & Shuttle", "Bus & Travel", "Bus" -> "/bus-travel?hideToolbar=true";
            case "Claim Center" -> "/insurance-claim?hideToolbar=true";
            case "ferry", "Ferry" -> "/ferry?hideToolbar=true";
            case "insurance", "Insurance" -> "/insurance?hideToolbar=true";
            default -> null;
        };
    }

    /**
     * Gets deeplink URL from API, falling back to hardcoded URL if API fails.
     *
     * @param verticalName The name of the vertical
     * @param deeplinkVerticalPOJO The POJO containing deeplink URLs from API
     * @return Deeplink URL and a flag indicating if it's hardcoded
     */
    private DeeplinkResult getDeeplinkUrl(String verticalName, DeeplinkVerticalPOJO deeplinkVerticalPOJO) {
        // Try to get deeplink from API
        try {
            String apiDeeplink = getVerticalDeeplink(verticalName, deeplinkVerticalPOJO);
            if (apiDeeplink != null && !apiDeeplink.isEmpty()) {
                logger.log("Original deeplink: " + apiDeeplink);
                return new DeeplinkResult(apiDeeplink, false);
            }
        } catch (Exception e) {
            logger.log("Failed to get deeplink from API: " + e.getLocalizedMessage());
        }
        
        // Fallback to hardcoded URL
        String hardcodedUrl = getHardcodedDeeplink(verticalName);
        if (hardcodedUrl != null) {
            logger.softWarning("Using hardcoded deeplink URL for vertical '" + verticalName + "': " + hardcodedUrl);
            return new DeeplinkResult(hardcodedUrl, true);
        }
        
        return null;
    }

    /**
     * Creates a Deeplink object from a path for different environments.
     *
     * @param path The cleaned path (e.g., "/hotel", "/to-do")
     * @return Deeplink object with URLs for GK, PreProd, and Prod environments
     */
    private Deeplink createDeeplinkFromPath(String path) {
        String deeplinkGK = "tiket://m.gatotkaca.tiket.com" + path;
        String deeplinkPreProd = "tiket://m.preprod.tiket.com" + path;
        String deeplinkProd = "tiket://m.tiket.com" + path;
        
        return new Deeplink("VerticalPage", deeplinkGK, deeplinkPreProd, deeplinkProd);
    }

    /**
     * Navigates using deeplink with fallback to UI if navigation fails.
     *
     * @param verticalName The name of the vertical
     * @param deeplink The Deeplink object to navigate with
     * @param isHardcoded Whether the deeplink is hardcoded (for fallback logic)
     */
    private void navigateWithDeeplink(String verticalName, Deeplink deeplink, boolean isHardcoded) {
        try {
            driverTools.deeplink(deeplink);
        } catch (Exception e) {
            logger.log("Deeplink navigation failed: " + e.getLocalizedMessage());
            
            // If using hardcoded URL, fallback to UI
            if (isHardcoded) {
                logger.log("Hardcoded deeplink also failed, falling back to UI navigation for vertical: " + verticalName);
                openVerticalPage(verticalName);
                return;
            }
            
            // If API deeplink failed, try hardcoded URL as fallback
            String hardcodedUrl = getHardcodedDeeplink(verticalName);
            if (hardcodedUrl != null) {
                logger.softWarning("API deeplink failed, trying hardcoded URL for vertical '" + verticalName + "': " + hardcodedUrl);
                String cleanedPath = extractPathFromClickUrl(hardcodedUrl);
                Deeplink hardcodedDeeplink = createDeeplinkFromPath(cleanedPath);
                navigateWithDeeplink(verticalName, hardcodedDeeplink, true);
            } else {
                logger.log("No hardcoded URL available, falling back to UI navigation for vertical: " + verticalName);
                openVerticalPage(verticalName);
            }
        }
    }

    /**
     * Handles the deeplink click for a vertical product on the home page.
     * <p>
     * This method retrieves the deeplink URL for the given vertical, cleans the URL to remove any domain
     * (keeping only the path and query string), and constructs the deeplink for different environments
     * (GK, PreProd, Prod). It then triggers the deeplink navigation using driverTools.
     * <p>
     * If API deeplink fails or is null, it falls back to hardcoded URLs for supported verticals.
     * If hardcoded URL also fails, it falls back to UI navigation.
     * <p>
     * Example:
     * <ul>
     *   <li>Input: https://gatotkaca.tiket.com/admin/member</li>
     *   <li>Cleaned: /admin/member</li>
     *   <li>Deeplink: tiket://m.gatotkaca.tiket.com/admin/member</li>
     * </ul>
     *
     * @param verticalName The name of the vertical (e.g., "hotel", "flight", etc.)
     * @param deeplinkVerticalPOJO The POJO containing deeplink URLs for all verticals
     */
    private void clickHomeProductDeeplink(String verticalName, DeeplinkVerticalPOJO deeplinkVerticalPOJO) {
        // Get deeplink URL (API or hardcoded fallback)
        DeeplinkResult deeplinkResult = getDeeplinkUrl(verticalName, deeplinkVerticalPOJO);
        if (deeplinkResult == null) {
            logger.log("No deeplink URL available for vertical '" + verticalName + "', falling back to UI navigation");
            openVerticalPage(verticalName);
            return;
        }
        
        // Clean the deeplink path
        String cleanedPath = extractPathFromClickUrl(deeplinkResult.url);
        logger.log("deeplink (raw): " + deeplinkResult.url);
        logger.log("deeplink (cleaned): " + cleanedPath);
        
        // Create deeplink object and navigate
        Deeplink verticalPage = createDeeplinkFromPath(cleanedPath);
        navigateWithDeeplink(verticalName, verticalPage, deeplinkResult.isHardcoded);
    }

    /**
     * Helper record to hold deeplink URL and its source type.
     */
    private record DeeplinkResult(String url, boolean isHardcoded) {}

    private static String encodeDeeplinkValue(String value) {
        return java.net.URLEncoder.encode(value == null ? "" : value, java.nio.charset.StandardCharsets.UTF_8);
    }

    /**
     * Logs the app in as {@code username} via the dedicated {@code tiket-internal://login}
     * deeplink and lands on the <b>home page</b> — never touches the UI/webview login form.
     *
     * <p>Uses the {@code tiket-internal} custom scheme resolved by
     * {@code AutomationDeeplinkActivity} (debug builds only). A dedicated scheme avoids the
     * Android disambiguation chooser caused by {@code DeepLinkActivity}'s catch-all
     * {@code tiket://} filter.
     *
     * <p>Login-state aware (mirrors {@link LoginPage#ensureLoggedInAs(String, String, String)}):
     * <ol>
     *   <li><b>LOGGED OUT</b> → obtains a UNM session id via the PKCE flow
     *       ({@link Unm#getUnmSessionIdViaPKCE(String, String)} → {@code data.session.id} from
     *       {@code POST /auth/code-challenge/verify}) and fires
     *       {@code tiket-internal://login?unmSessionId=<sessionId>}. The deeplink relaunches
     *       the app to home, no extra navigation needed.</li>
     *   <li><b>LOGGED IN as same account</b> (verified via {@link #openAccountCenterViaDeeplink(String)})
     *       → no UNM call, no logout; just calls {@link #goToHomeUsingDeeplinkWithoutCondition()} once.</li>
     *   <li><b>LOGGED IN as a different account</b> → from the Account Center page, fires
     *       {@code tiket-internal://logout} <i>directly</i> (no intermediate home hop),
     *       dismisses the post-logout "Continue as guest" popup, fetches a fresh UNM session
     *       id, then fires {@code tiket-internal://login?unmSessionId=<sessionId>} — which
     *       relaunches the app to home, also without an intermediate home hop.</li>
     * </ol>
     *
     * <p>{@code AutomationDeeplinkActivity} hands the session ID to
     * {@code LoginOAuthViewModel.auth(sessionId)} which fetches login data from the
     * backend and updates local state, then relaunches the app
     * ({@code NEW_TASK | CLEAR_TASK}).
     *
     * <p><b>PROD guardrail:</b> {@code AutomationDeeplinkActivity} is absent from release
     * builds, so on {@link Environment#PROD} this method falls back to UI-based login via
     * {@link LoginPage#ensureLoggedInAs(String, String, String)}.
     *
     * <p>Example resulting deeplink:
     * <pre>tiket-internal://login?unmSessionId=UxEexzPxW3z9vqo24blCsZzVlDjMDqQD</pre>
     *
     * @param username account email used to call UNM auth and to verify the existing session
     * @param password account password used to call UNM auth (ignored on the same-account fast-path)
     * @throws RuntimeException if UNM login fails on GK/PREPROD
     */
    @SneakyThrows
    public void loginViaDeeplink(String username, String password) {
        final Locator LOGIN_FAILED = new Locator("LOGIN_FAILED", AppiumBy.xpath("//*[text()='Ada gangguan saat mencoba terhubung ke akunmu' or text()='There was a problem when connecting to your account']"));
        final Locator LOGIN_AGAIN = new Locator("LOGIN_AGAIN", AppiumBy.xpath("//*[text()='Log in again' or text()='Log in ulang']"));
        final Locator LOGIN_AS_GUEST = new Locator("LOGIN_AS_GUEST", AppiumBy.xpath("//*[text()='Continue as guest' or text()='Lanjut sebagai tamu']"));
        if (Configs.ENVIRONMENT == Environment.PROD) {
            logger.log("Env=PROD — AutomationDeeplinkActivity is not present in release builds; "
                    + "falling back to UI login via LoginPage.ensureLoggedInAs for user='" + username + "'");
            LoginPage.getInstance(session).ensureLoggedInAs("email", username, password);
            return;
        }

        String unmSessionId = ensureLoginViaDeeplinkAuth(username, password);
        if (unmSessionId == null) {
            logger.log("Already logged in as " + username + " — navigating to home page");
            goToHomeUsingDeeplinkWithoutCondition();
            coachMarkHandling();
            return;
        }

        String url = "tiket-internal://login?unmSessionId=" + encodeDeeplinkValue(unmSessionId);

        logger.log("Login-via-deeplink (home) user='" + username + "'");
        logger.log("Login-via-deeplink URL: " + url);

        driverTools.deeplink(new Deeplink("LoginViaDeeplink", url, url, url));

        if (Configs.PLATFORM == Platform.IOS) {
            driverTools.waitForElementToAppear(CLOSE_GIVE_RATING_POPUP, 5).ifPresent(driverTools::click);
        }

        dismissEcosystemWelcome();

        // Helps to stop propagate the "issue connecting with your account" error to next test when the login failed.
        if(driverTools.isElementDisplayed(LOGIN_FAILED) || driverTools.isElementDisplayed(LOGIN_AGAIN) || driverTools.isElementDisplayed(LOGIN_AS_GUEST)) {
            logger.step("Login via deeplink failed for user='" + username + "' — clicking 'Login Again'");
            try {
                driverTools.click(LOGIN_AGAIN);
            } catch (Exception e) {
                logger.step("Failed to click 'Login Again' button, trying 'Continue as guest' instead");
                driverTools.click(LOGIN_AS_GUEST);
            }
        }
        goToHomeUsingDeeplinkWithoutCondition();
        Assertion.assertThat("Verify app is logged in after login via deeplink", isUserLogin(), is(true));
        coachMarkHandling();
    }

    /**
     * The login deeplink relaunches the app, which sometimes lands on the ecosystem welcome
     * screen instead of home. Tap through it so the caller sees the home page.
     */
    private void dismissEcosystemWelcome() {
        driverTools.waitForElementToAppear(ECOSYSTEM_WELCOME_CTA, 5).ifPresent(cta -> {
            logger.log("Ecosystem welcome screen shown after login deeplink — dismissing it");
            driverTools.clickElementByCoordinate(cta);
        });
    }

    /**
     * Logs the app in as {@code username} via the {@code tiket-automation-login} deeplink hook
     * and then changes the home-page currency to {@code currency} via the change-currency
     * deeplink — landing back on the home page (NOT on any vertical landing page).
     *
     * <p>This composes {@link #loginViaDeeplink(String, String)} (which already handles all three
     * login-state cases — see {@link #ensureLoginViaDeeplinkAuth(String, String)}) with
     * {@link #changeCurrency(String, String)}:
     * <ol>
     *   <li><b>LOGGED OUT</b> → fetch UNM tokens, fire home login deeplink with {@code _ai*}
     *       auth params, then change currency on the home page.</li>
     *   <li><b>LOGGED IN as a different account</b> → log out via {@link LoginPage#loginAsGuest()},
     *       then proceed with the LOGGED OUT branch (login + change currency).</li>
     *   <li><b>LOGGED IN as same account</b> → skip UNM auth, just navigate to home and change
     *       currency.</li>
     * </ol>
     *
     * <p>Pair with {@link #openVerticalPageDeeplink(String, String, String)} when the test
     * needs to navigate to a vertical landing page <i>after</i> session + currency are set up.
     * Keeping login + currency separate from the vertical navigation avoids stacking auth
     * params onto a vertical deeplink (which has a higher chance of hitting webview quirks)
     * and lets the test choose the destination explicitly.
     *
     * <p><b>PROD guardrail:</b> the {@code tiket-automation-login} deeplink hook is intentionally
     * disabled on production builds. On {@link Environment#PROD}, {@link #loginViaDeeplink(String, String)}
     * transparently falls back to UI-based login via
     * {@link LoginPage#ensureLoggedInAs(String, String, String)}; the currency switch that follows
     * is still done via the change-currency deeplink. Net effect on PROD: <i>UI login + deeplink
     * currency change</i>. On GK / PREPROD the whole flow stays deeplink-only as documented above.
     *
     * @param username account email used for UNM auth and login-state verification
     * @param password account password for UNM auth (ignored on the same-account fast-path)
     * @param currency target currency code, e.g. {@code "IDR"}, {@code "USD"}
     */
    public void loginWithSpecificAccountViaDeeplink(String username, String password, String currency) {
        loginViaDeeplink(username, password);
        changeCurrency(currency, username);
        coachMarkHandling();
    }

    /**
     * Login-only variant of {@link #loginWithSpecificAccountViaDeeplink(String, String, String)}
     * that does <b>not</b> change currency afterwards.
     *
     * <p>Use this when the original test only had {@code loginWith()} without any
     * {@code changeCurrency()} call — adding an unneeded currency switch can alter test
     * preconditions or mask currency-related bugs.
     *
     * @param username account email used for UNM auth and login-state verification
     * @param password account password for UNM auth (ignored on the same-account fast-path)
     */
    public void loginWithSpecificAccountViaDeeplinkOnly(String username, String password) {
        loginViaDeeplink(username, password);
        goToHomeUsingDeeplinkWithoutCondition();
        coachMarkHandling();
    }

    /**
     * "Any-account" variant of {@link #loginWithSpecificAccountViaDeeplink(String, String, String)}:
     * logs in only when the app is currently <b>logged out</b>, otherwise skips the login step
     * entirely and proceeds straight to the currency change.
     *
     * <p>Unlike {@link #loginWithSpecificAccountViaDeeplink(String, String, String)} (which goes
     * through {@link #ensureLoginViaDeeplinkAuth(String, String)} and will <i>logout + re-login</i>
     * when the logged-in account differs from {@code username}), this method does NOT verify which
     * account is logged in. As soon as {@link #isUserLogin()} returns {@code true} the login step
     * is skipped — no Account Center lookup, no logout, no re-login on account mismatch.
     *
     * <p>Use this when the test only needs <i>any</i> logged-in session (e.g. a shared corporate
     * account across multiple sequential tests on the same device) and you want to skip the cost
     * of the same-account verification on subsequent runs.
     *
     * <p>Behavior:
     * <ol>
     *   <li><b>LOGGED IN</b> (any account) → skip login entirely, just change currency.</li>
     *   <li><b>LOGGED OUT</b> → delegate to {@link #loginViaDeeplink(String, String)}, which
     *       applies the same env-aware path:
     *       <ul>
     *         <li>GK / PREPROD → fire the {@code tiket-automation-login} deeplink with UNM-fetched
     *             {@code _ai*} auth params.</li>
     *         <li>PROD → fall back to UI login via
     *             {@link LoginPage#ensureLoggedInAs(String, String, String)} (the
     *             {@code tiket-automation-login} hook is intentionally disabled on production
     *             builds).</li>
     *       </ul>
     *       Then change currency on the home page.</li>
     * </ol>
     *
     * <p>Net effect on PROD when logged out: <i>UI login + deeplink currency change</i>. On GK /
     * PREPROD the whole flow stays deeplink-only.
     *
     * @param username account email used for UNM auth (or UI login on PROD); ignored when already
     *                 logged in
     * @param password account password used for UNM auth (or UI login on PROD); ignored when
     *                 already logged in
     * @param currency target currency code, e.g. {@code "IDR"}, {@code "USD"}
     */
    public void loginIfLoggedOutAndChangeCurrencyViaDeeplink(String username, String password, String currency) {
        if (isUserLogin()) {
            logger.log("App state: LOGGED IN — skipping login (any-account fast-path), "
                    + "proceeding to change currency to " + currency);
        } else {
            logger.log("App state: LOGGED OUT — logging in as " + username
                    + " before changing currency to " + currency);
            loginViaDeeplink(username, password);
        }
        changeCurrency(currency, username);
        coachMarkHandling();
    }

    /**
     * Login-only variant of {@link #loginIfLoggedOutAndChangeCurrencyViaDeeplink(String, String, String)}
     * that does <b>not</b> change currency afterwards.
     *
     * <p>Use this when the original test only had {@code loginWith()} without any
     * {@code changeCurrency()} call — adding an unneeded currency switch can alter test
     * preconditions or mask currency-related bugs.
     *
     * <p>If the app is already logged in (any account), the login step is skipped entirely
     * and no action is taken.
     *
     * @param username account email used for UNM auth (ignored when already logged in)
     * @param password account password used for UNM auth (ignored when already logged in)
     */
    public void loginIfLoggedOutViaDeeplink(String username, String password) {
        if (isUserLogin()) {
            logger.log("App state: LOGGED IN — skipping login (any-account fast-path, no currency change)");
        } else {
            logger.log("App state: LOGGED OUT — logging in as " + username + " (no currency change)");
            loginViaDeeplink(username, password);
        }
        coachMarkHandling();
    }

    /**
     * Shared pre-flight for the {@code *ViaDeeplink} login methods. Detects the current login
     * state and returns the UNM <b>session id</b> (the short id from
     * {@code POST /auth/code-challenge/verify} → {@code data.session.id}) only when a fresh
     * login is actually required.
     *
     * <p>Uses the PKCE flow via {@link Unm#getUnmSessionIdViaPKCE(String, String)}. The legacy
     * {@link Unm#getUnmSessionId(String, String)} (which returned a long {@code SERVICE_TICKET}
     * JWT) is intentionally not used here — {@code AutomationDeeplinkActivity} expects the short
     * PKCE session id.
     *
     * <p>Outcomes:
     * <ul>
     *   <li>Logged out → calls {@link Unm#getUnmSessionIdViaPKCE(String, String)} and returns
     *       the session id. Caller fires the login deeplink which relaunches the app to home.</li>
     *   <li>Logged in as the same {@code username} (verified via
     *       {@link #openAccountCenterViaDeeplink(String)}) → returns {@code null} so the caller
     *       can navigate home and skip the UNM round-trip. The home navigation happens in
     *       {@link #loginViaDeeplink(String, String)} — no second hop here.</li>
     *   <li>Logged in as a different account → fires {@code tiket-internal://logout}
     *       directly from the Account Center page (no intermediate home navigation),
     *       dismisses the post-logout "Continue as guest" popup, then fetches a new session id
     *       via PKCE. The caller's login deeplink relaunches the app to home, so we also skip
     *       the {@code goToHomeUsingDeeplinkWithoutCondition()} that public
     *       {@link #logoutViaDeeplink()} would normally append.</li>
     * </ul>
     *
     * @return UNM session id, or {@code null} when already authenticated as {@code username}
     * @throws SkipException if UNM authentication fails — marks the test as SKIPPED instead of FAILED
     */
    private String ensureLoginViaDeeplinkAuth(String username, String password) {
        if (isUserLogin()) {
            logger.log("App state: LOGGED IN — verifying account via Account Center deeplink for " + username);
            boolean isSameAccount = openAccountCenterViaDeeplink(username);

            if (isSameAccount) {
                logger.log("Same account confirmed for " + username
                        + " — skipping UNM auth (caller will navigate home)");
                return null;
            }

            logger.log("Different account on Account Center — firing logout deeplink directly "
                    + "(no intermediate home hop) before login-via-deeplink as " + username);
            fireLogoutDeeplink(buildLogoutDeeplink());
            if(Configs.PLATFORM == Platform.IOS) {
                goToHomeUsingDeeplinkWithoutCondition();
            }
        } else {
            logger.log("App state: LOGGED OUT — proceeding to login-via-deeplink as " + username);
        }

        Unm<T> unm = new Unm<>(session);
        String unmSessionId = unm.getUnmSessionIdViaPKCE(username, password);
        if (unmSessionId == null || unmSessionId.isBlank()) {
            throw new SkipException("UNM login failed for username: " + username
                    + " — cannot obtain session id for login-via-deeplink (PKCE)");
        }
        return unmSessionId;
    }

    private static Deeplink buildLogoutDeeplink() {
        String url = "tiket-internal://logout";
        return new Deeplink("Logout", url, url, url);
    }

    /**
     * Logs the app out via the dedicated {@code tiket-internal://logout} deeplink and
     * lands on the <b>home page</b> — never touches the Account → Logout UI flow.
     *
     * <p>Uses the {@code tiket-internal} custom scheme resolved by
     * {@code AutomationDeeplinkActivity} (debug builds only).
     *
     * <p>Login-state aware:
     * <ul>
     *   <li><b>Already logged out</b> → no deeplink fired; simply calls
     *       {@link #goToHomeUsingDeeplinkWithoutCondition()}.</li>
     *   <li><b>Logged in</b> → fires {@code tiket-internal://logout}.
     *       {@code AutomationDeeplinkActivity} delegates to
     *       {@code LogoutViewModel.logout()} which clears the local session, then
     *       relaunches the app ({@code NEW_TASK | CLEAR_TASK}). The per-session UNM
     *       token cache is also invalidated so the next UNM call re-authenticates.</li>
     * </ul>
     *
     * <p><b>PROD guardrail:</b> {@code AutomationDeeplinkActivity} is absent from release
     * builds. On {@link Environment#PROD}, this method transparently falls back to UI-based
     * logout via {@link LoginPage#loginAsGuest()}.
     */
    public void logoutViaDeeplink() {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            logger.log("Env=PROD — AutomationDeeplinkActivity is not present in release builds; "
                    + "falling back to UI logout via LoginPage.loginAsGuest()");
            LoginPage.getInstance(session).loginAsGuest();
            return;
        }

        if (!isUserLogin()) {
            logger.log("App state: LOGGED OUT — skipping logout deeplink, navigating to home page");
            goToHomeUsingDeeplinkWithoutCondition();
            return;
        }

        logger.log("Logout-via-deeplink URL: tiket-internal://logout");
        fireLogoutDeeplink(buildLogoutDeeplink());
        goToHomeUsingDeeplinkWithoutCondition();
    }

    /**
     * Logs out via deeplink when the test reached a logged-in state. Failures are swallowed so cleanup
     * never overturns the original test result.
     *
     * @param isLoggedIn {@code true} if the app reached the logged-in state and should be logged out
     */
    public void logoutAccount(boolean isLoggedIn) {
        if (!isLoggedIn) {
            logger.log("Skipping logout — test did not reach a logged-in state.");
            return;
        }
        try {
            driverTools.switchToNative();
            fireLogoutDeeplink(buildLogoutDeeplink());
        } catch (Throwable cleanupError) {
            logger.log("Cleanup: logout failed (ignored): " + cleanupError.getMessage());
        }
    }

    /**
     * Tears down a temporary test account created during an app test. This is the app-side
     * counterpart of {@code HomePageWeb.logoutAndDeleteTemporaryAccount(RegisterNewAccountData)}
     * and centralises the logout-then-delete cleanup that tests previously repeated inline.
     *
     * <p>Steps (each isolated so a failure in one never blocks the next):
     * <ol>
     *   <li><b>Go home, then logout</b> — {@link #gotoHome()} first restores the native context (a test
     *       may fail while still inside a webview) and lands on a fully-loaded home page, then delegates
     *       to the login-state-aware {@link #logoutViaDeeplink()}. The home hop is essential:
     *       {@code isUserLogin()} only recognises the logged-in state from home-page elements, so
     *       logging out from a mission/webview screen would otherwise give a false negative and skip the
     *       logout deeplink. A logout failure must not skip the account deletion below.</li>
     *   <li><b>Delete account</b> — removes the temporary UNM account so it does not pollute future
     *       runs (skipped when {@code accountData}/{@code unmData} is {@code null}).</li>
     * </ol>
     *
     * <p>Only runs on non-PROD environments (temporary accounts are not created on PROD). Every
     * failure — including an {@link AssertionError} from the UNM delete call — is caught and logged
     * so cleanup can never overturn the original test result.
     *
     * @param accountData the account created earlier in the test (may be {@code null} if creation failed).
     */
    public void logoutAndDeleteTemporaryAccount(RegisterNewAccountData accountData) {
        logoutAndDeleteTemporaryAccount(accountData, true);
    }

    /**
     * Variant of {@link #logoutAndDeleteTemporaryAccount(RegisterNewAccountData)} that lets the caller
     * declare whether the app ever reached the logged-in state.
     *
     * <p>The temporary account is created and deleted purely via the UNM API, so account deletion never
     * depends on the app session. The in-app logout is only meaningful when the test actually logged in.
     * When a test fails early — e.g. still on the onboarding/home screen before
     * {@code loginWithSpecificAccountViaDeeplinkOnly} completes — pass {@code appLoggedIn = false} so the
     * logout step (its {@code isUserLogin()} probe plus home navigation) is skipped entirely and cleanup
     * goes straight to deleting the account via API.
     *
     * @param accountData the account created earlier in the test (may be {@code null} if creation failed).
     * @param appLoggedIn {@code true} if the app reached the logged-in state and should be logged out;
     *                    {@code false} to skip logout and only delete the account via API.
     */
    public void logoutAndDeleteTemporaryAccount(RegisterNewAccountData accountData, boolean appLoggedIn) {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            return;
        }
        if (appLoggedIn) {
            try {
                driverTools.switchToNative();
            } catch (Throwable ignored) {
                logger.log("Cleanup: switchToNative failed (continuing): " + ignored.getMessage());
            }
            try {
                // Prefer the timeboxed forced logout — avoids isUserLogin() hang in webview.
                logoutAccount(true);
            } catch (Throwable logoutError) {
                // A logout failure must not skip the account deletion below.
                logger.warn("Logout during cleanup failed (ignored to preserve the original test result): "
                        + logoutError.getMessage());
                logger.exception(logoutError);
            }
        } else {
            logger.log("Skipping app logout during cleanup — test never reached the logged-in state; "
                    + "deleting the temporary account via API only.");
        }
        try {
            if (accountData != null && accountData.unmData != null) {
                new Unm<T>(session).deleteAccountViaApi(accountData.email);
            }
        } catch (Throwable deleteError) {
            logger.warn("Account deletion during cleanup failed (ignored to preserve the original test result): "
                    + deleteError.getMessage());
            logger.exception(deleteError);
        }
    }

    /**
     * Fires a logout deeplink and invalidates this driver session's UNM token cache so any
     * subsequent {@link Unm#unmLoginApi(String, String)} call performs a fresh authentication
     * (the server has revoked the token but its JWT {@code exp} is still in the future, so
     * token-expiry checks would otherwise let it through), then dismisses the post-logout
     * "Continue as guest" popup that lands on the Account page.
     *
     * <p><b>Logout confirmation:</b> {@link AccountPage#clickContinueAsGuest()} internally
     * waits for the {@code CONTINUE_AS_GUEST} popup and returns {@code true} only when the
     * popup actually appears — which only happens after the logout deeplink relaunches the
     * app on the Account page in the logged-out state. That is a stronger signal than polling
     * {@link #isUserLogin()} (which races with the {@code NEW_TASK | CLEAR_TASK} relaunch),
     * so we rely on the popup as our single logout-completion barrier.
     *
     * <p><b>Does NOT navigate to home.</b> Each caller decides what to do next:
     * <ul>
     *   <li>Public {@link #logoutViaDeeplink()} appends a {@link #goToHomeUsingDeeplinkWithoutCondition()}
     *       so its contract (lands on home) is preserved. The unconditional variant is safe
     *       here because the logout deeplink just relaunched the app
     *       ({@code NEW_TASK | CLEAR_TASK}) and the {@code Continue as guest} popup was just
     *       dismissed, so the app is in a known clean state — no need for the safety
     *       {@code closeApp()} that {@link #goToHomeDeeplink()} performs.</li>
     *   <li>{@link #ensureLoginViaDeeplinkAuth(String, String)} (logout-then-login flow) skips
     *       the home hop because the login deeplink fired right after will relaunch the app
     *       to home with {@code NEW_TASK | CLEAR_TASK}.</li>
     * </ul>
     */
    @SneakyThrows
    private void fireLogoutDeeplink(Deeplink deeplink) {
        driverTools.deeplink(deeplink);
        new Unm<T>(session).invalidateAllCachedTokensForCurrentSession();
        AccountPage<T> accountPage = AccountPage.getInstance(session);
        boolean isLogoutSuccessful;
        try {
            isLogoutSuccessful = accountPage.clickContinueAsGuest();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Logout deeplink failed — 'Continue as guest' popup locator not found on Account page; "
                            + "logout likely did not complete. Cause: " + e.getMessage(), e);
        }
        logger.log("Logout confirmed via 'Continue as Guest' popup: " + isLogoutSuccessful);
        if (!isLogoutSuccessful) {
            throw new RuntimeException(
                    "Logout deeplink failed — logged-out state not confirmed on Account page "
                            + "('Continue as guest' popup or login container not found); "
                            + "logout likely did not complete");
        }
    }

    private String extractPathFromClickUrl(String clickUrl) {
        logger.log("Extracting path from clickUrl: " + clickUrl);
        if (clickUrl == null || clickUrl.isEmpty()) return "/";
        try {
            // Handle standard URLs
            if (clickUrl.startsWith("http://") || clickUrl.startsWith("https://")) {
                java.net.URI uri = new java.net.URI(clickUrl);
                String path = uri.getPath();
                String query = uri.getQuery();
                logger.log("Parsed path: " + path + ", query: " + query);
                return (path != null ? path : "") + (query != null ? "?" + query : "");
            }
            // Handle custom domains (e.g., m.preprod.tiket.com/...)
            if (clickUrl.matches("^(?:[a-zA-Z0-9.-]+)\\.[a-zA-Z]{2,}(?:/.*)?$")) {
                int idx = clickUrl.indexOf('/');
                if (idx != -1) {
                    logger.log("Custom domain detected, extracted path: " + clickUrl.substring(idx));
                    return clickUrl.substring(idx);
                } else {
                    logger.log("Custom domain with no path, returning root '/'");
                    return "/";
                }
            }
            // Already a path or relative link
            if (clickUrl.startsWith("/")) {
                logger.log("ClickUrl is already a path: " + clickUrl);
                return clickUrl;
            }
            // Fallback: treat as path
            logger.log("Fallback, treating clickUrl as path: " + clickUrl);
            return "/" + clickUrl;
        } catch (Exception e) {
            logger.log("Failed to parse clickUrl: " + clickUrl + ", error: " + e.getMessage());
            return "/";
        }
    }

    public abstract Boolean isFlightUpcomingBookingDisplayed();
    public abstract Boolean isUpcomingBookingCountdownDisplayed();
    public abstract Boolean isUpcomingBookingHotelNameDisplayed();
    public abstract Boolean checkInDetails();
    public abstract void clickSeeAllUpcomingBooking();
    public abstract Boolean countOfSeeAll();
    public abstract Boolean isUpcomingCardClickable();
    public abstract Boolean isTitleCrossSellAppears();
    public abstract Boolean isCrossSellCardsClickableAndScrollable();
    public abstract void clickUpcomingBooking();
    public abstract void clickCrossSellCard();
    public abstract String getUpcomingBookingDepartureTime();
    public abstract String getUpcomingBookingRouteInformation();
    public abstract Boolean isUpcomingBookingVendorIconDisplayed();
    public abstract Boolean isUpcomingBookingSeeMoreDisplayed();
    public abstract Boolean isUpcomingBookingCloseButtonDisplayed();
    public abstract Boolean isUpcomingBookingCrossSellTitleDisplayed();
    public abstract Boolean isUpcomingBookingCrossSellCardDisplayed();
    public abstract Boolean verifyUpcomingBookingSeeMore();
    public abstract String getUpcomingBookingPassengerName();
    public abstract String getUpcomingBookingClassName();
    public abstract String getUpcomingBookingOriginStation();
    public abstract Boolean verifySeeMoreUpcomingBooking();
    public abstract Boolean verifyUpcomingBookingCardXButton();
    public abstract Boolean verifyUpcomingBookingCard();
    public abstract Boolean verifyUpcomingBookingVoucherBox();
    public abstract Boolean verifyUpcomingBookingAllPromoCard();
    public abstract Boolean verifyUpcomingBookingVerticalCard();
    public abstract String swipeToRecentlySearchedNearMe(String date);
    public abstract void coachMarkHandling();
    public Boolean verifyVertical(String verticalName) {
        boolean result = false;

        switch (verticalName.toLowerCase()) {
            case "hotel":
            case "hotels":
                LandingPage<T> landingPage = LandingPage.getInstance(session);
                return landingPage.isLandingPageValid();
            case "Villa & Apt":
                HomesLandingPage<T> homesLandingPage = HomesLandingPage.getInstance(session);
                //FIX ME: Need Implementation
                break;
            case "sewa mobil":
            case "car rentals":
                SearchPageCarRental<T> searchPageCarRental = SearchPageCarRental.getInstance(session);
                return searchPageCarRental.isLandingPageValid();
            case "airport pickups":
            case "jemputan bandara":
                CarTransferSearchPage<T> carTransferSearchPage = CarTransferSearchPage.getInstance(session);
                //FIX ME: Need Implementation
                break;
            case "pesawat":
            case "flight":
                FlightSearchPage<T> flightSearchPage = FlightSearchPage.getInstance(session);
                result =  flightSearchPage.isOnFlightLandingPage();
                break;
            case "to do":
            case "to dos":
                TTDLandingPage<T> ttdLandingPage = TTDLandingPage.getInstance(session);
                //FIX ME: Need Implementation
                break;
            case "train":
            case "trains":
            case "kereta api":
                TrainSearchPage<T> trainSearchPage = TrainSearchPage.getInstance(session);
                //FIX ME: Need Implementation
                break;
            case "ferry":
                FerrySearchPage<T> ferrySearchPage = FerrySearchPage.getInstance(session);
                //FIX ME: Need Implementation
                break;
            case "bus & shuttle":
                BusSearchPage<T> busSearchPage = BusSearchPage.getInstance(session);
                //FIX ME: Need Implementation
                break;
        }

        return result;
    }

    public void changeLanguageUsingDeeplink(String language){
        AccountPage<T> accountPage = AccountPage.getInstance(session);
        accountPage.openAccountPageUsingDeeplink();
        if (driverTools.isElementDisplayed(CLOSE_GIVE_RATING_POPUP)) {
            driverTools.clickElementByCoordinate(CLOSE_GIVE_RATING_POPUP);
        }
        accountPage.selectLanguage(language);
        goToHomeUsingDeeplinkWithoutCondition();
        for(int i=0; i<5 && !this.waitForHomePageToLoad(); i++) {
            logger.log("Waited for homepage to load, retrying " + (i+1) + " times.");
        }
    }

    public void deeplinkToRewardsPage() {
        Deeplink rewardsPage = new Deeplink("RewardsPage",
                "tiket://gatotkaca.tiket.com/rewards",
                "tiket://preprod.tiket.com/rewards",
                "tiket://tiket.com/rewards");
        try {
            driverTools.deeplink(rewardsPage);
        }catch (Exception e){
            logger.log(e.getLocalizedMessage());
        }
    }

    public void deeplinkToGiftCardPage(String currency) {
        Deeplink giftCardPage = new Deeplink("GiftCardsPage",
                "tiket://gatotkaca.tiket.com/gift-card?currency=" + currency,
                "tiket://preprod.tiket.com/gift-card?currency=" + currency,
                "tiket://www.tiket.com/gift-card?currency=" + currency);
        try {
            driverTools.deeplink(giftCardPage);
        }catch (Exception e){
            logger.log(e.getLocalizedMessage());
        }
    }

    /**
     * Opens the Blibli-Tiket account page via deeplink using only the half (clientId-less) link.
     * <p>
     * Base URL pattern (per env):
     * <ul>
     *   <li>GK      : {@code https://account.gatotkaca.bliblitiket.dev/account?entity=tiket&lang=<lang>}</li>
     *   <li>PREPROD : {@code https://account.preprod.bliblitiket.dev/account?entity=tiket&lang=<lang>}</li>
     *   <li>PROD    : {@code https://account.bliblitiket.com/account?entity=tiket&lang=<lang>}</li>
     * </ul>
     * The {@code clientId} query param is intentionally omitted to validate whether the
     * stripped-down link is still resolvable.
     *
     * @param lang language code to append (e.g. {@code id}, {@code en}); defaults to {@code id} when null/empty
     */
    public void deeplinkToAccountHalfLink(String lang) {
        String language = (lang == null || lang.isEmpty()) ? "id" : lang;
        String endPoint = "/account?entity=tiket&lang=" + language;
        Deeplink accountHalfLink = new Deeplink("AccountPageHalfLink",
                "https://account.gatotkaca.bliblitiket.dev" + endPoint,
                "https://account.preprod.bliblitiket.dev" + endPoint,
                "https://account.bliblitiket.com" + endPoint);
        logger.log("Opening account page via half deeplink (no clientId): " + endPoint);
        try {
            driverTools.deeplink(accountHalfLink);
        } catch (Exception e) {
            logger.log(e.getLocalizedMessage());
        }
    }

    /**
     * Fetches the first email-like value displayed on the current native screen.
     * <p>
     * Switches to native context, locates any element whose {@code text} / {@code label} /
     * {@code name} / {@code value} / {@code content-desc} attribute contains an email
     * pattern (via {@link #EMAIL_ON_NATIVE_SCREEN}), and extracts the email substring
     * with a strict regex so surrounding labels like {@code "Email: foo@bar.com"} are
     * trimmed away.
     *
     * @return the extracted email string, or {@code ""} if no element/email was found
     */
    public String fetchEmailFromNativeScreen() {
        driverTools.switchToNative();
        Optional<WebElement> elementOpt = driverTools.waitForElementToPresence(EMAIL_ON_NATIVE_SCREEN);
        if (elementOpt.isEmpty()) {
            logger.log("No element matching email pattern found on native screen");
            return "";
        }
        WebElement element = elementOpt.get();
        String[] candidates = new String[]{
                safeAttr(element, "text"),
                safeAttr(element, "label"),
                safeAttr(element, "name"),
                safeAttr(element, "value"),
                safeAttr(element, "content-desc"),
                driverTools.getText(element)
        };
        for (String candidate : candidates) {
            if (candidate == null || candidate.isEmpty()) continue;
            java.util.regex.Matcher matcher = EMAIL_PATTERN.matcher(candidate);
            if (matcher.find()) {
                String email = matcher.group();
                logger.log("Email fetched from native screen: " + email);
                return email;
            }
        }
        logger.log("Element matched email locator but no valid email substring could be parsed");
        return "";
    }

    private String safeAttr(WebElement element, String attribute) {
        try {
            return element.getAttribute(attribute);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Opens the Blibli-Tiket account half deeplink, fetches the email shown on the
     * resulting native screen, and asserts it contains an {@code @} character.
     * <p>
     * Combines {@link #deeplinkToAccountHalfLink(String)}, {@link #fetchEmailFromNativeScreen()},
     * and the assertion into one call so tests stay one-liners.
     *
     * @param lang language code (e.g. {@code id}, {@code en}); defaults to {@code id} when null/empty
     * @return the extracted email string
     */
    public String verifyEmailViaAccountHalfLink(String lang) {
        deeplinkToAccountHalfLink(lang);
        String email = fetchEmailFromNativeScreen();
        Assertion.assertThat("Email shown on account half link", email, containsString("@"));
        return email;
    }

    public String getPendingTiketPointsInRewardsPage() {
        logger.log("Navigating to Rewards Page using deeplink...");
        deeplinkToRewardsPage();

        logger.log("Waiting for webview to load rewards page...");
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("rewards");

        RewardsPageWeb<T> rewardsPageWeb = RewardsPageWeb.getInstance(session);

        // Open the pending tab and fetch the value
        rewardsPageWeb.openPendingPointHistoryPage();
        String pendingPoints = rewardsPageWeb.getPendingPointsValue();

        logger.log("Pending Points found: " + pendingPoints);

        // Switch back safely
        driverTools.switchToNative();
        goToHomeUsingDeeplinkWithoutCondition();

        return pendingPoints;
    }

    public String getActiveTiketPointsInRewardsPage() {
        logger.log("Navigating to Rewards Page using deeplink...");
        deeplinkToRewardsPage();

        logger.log("Waiting for webview to load rewards page...");
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("rewards");
        RewardsPageWeb<T> rewardsPageWeb = RewardsPageWeb.getInstance(session);
        String currentUrl = driverTools.getCurrentURL();
        String  totalPoints = rewardsPageWeb.getTotalPoints();
        logger.log("Current URL: " + currentUrl);
        logger.log("Total Points: " + totalPoints);
        driverTools.switchToNative();
        goToHomeUsingDeeplinkWithoutCondition();
        return totalPoints;
    }

    public boolean isOnRewardsPage() {
        logger.log("Waiting for webview to load rewards page...");
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("rewards");
        String currentUrl = driverTools.getCurrentURL();
        logger.log("Current URL: " + currentUrl);
        driverTools.switchToNative();
        return currentUrl.contains("rewards");
    }

    /**
     * @return true when the app is on a webview page whose URL contains {@code expectedUrl}. Used to
     * verify a redirection target after tapping a native entry point that opens a webview (e.g. an
     * inbox message carrying a deeplink).
     *
     * <p>Returns false when no webview is available at all, meaning the tap never left the native
     * screens — in that case the context is untouched and stays native.
     *
     * <p><b>Leaves the driver in the webview context</b> once a webview is found, so the caller can
     * keep asserting on web elements. Call {@code switchToNative()} before using native locators again.
     *
     * @param expectedUrl the URL (or any distinctive part of it, e.g. {@code promo}) the webview is
     *                    expected to land on
     * @throws IllegalArgumentException when {@code expectedUrl} is null or blank (usually a missing
     *                                  test data column)
     */
    public boolean isOnWebviewPage(String expectedUrl) {
        if (expectedUrl == null || expectedUrl.isBlank()) {
            throw new IllegalArgumentException("expectedUrl must not be blank — check the test data column");
        }
        logger.log("Waiting for webview with url containing: " + expectedUrl);
        if (!driverTools.waitForWebview()) {
            logger.log("No webview available — app is still on a native page");
            return false;
        }
        String currentUrl = driverTools.switchToWebViewUrl(expectedUrl);
        logger.log("Expected url: " + expectedUrl + ", got url: " + currentUrl);
        return currentUrl != null && currentUrl.contains(expectedUrl);
    }

    public boolean isRewardsPointsBalanceVisible() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("rewards");
        boolean result = RewardsPageWeb.getInstance(session).isPointsBalanceVisible();
        driverTools.switchToNative();
        return result;
    }

    public void scrollToBottomOfRewardsPage() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("rewards");
        RewardsPageWeb.getInstance(session).scrollToBottomOfRewardsPage();
        driverTools.switchToNative();
    }

    public boolean isRewardsPageScrolledToBottom() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("rewards");
        boolean result = RewardsPageWeb.getInstance(session).isScrolledToBottomOfRewardsPage();
        driverTools.switchToNative();
        return result;
    }

    public abstract void clickLoginButton();

    public abstract Boolean isPayLaterSectionAppear();
    public abstract Boolean isBlipaySectionAppear();
    public abstract Boolean verifyTiketPointRedirection();
    public abstract Boolean verifyPayLaterRedirection();
    public abstract Boolean verifyBlipayRedirection();

    public abstract void clickFinanceBarRunningText(String sectionTitle);
    public abstract boolean isOnBlibliTiketRewardsPage();
    public abstract boolean isOnPaylaterUspPage();
    public abstract boolean isOnBlipayDashboard();
}
