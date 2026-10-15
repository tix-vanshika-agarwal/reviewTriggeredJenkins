package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.auth.AdminDashboardLoginResponseParser;
import com.tiket.api.auth.GETProfileApi;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardApi;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardDynamicApi;
import com.tiket.api.member.GetAccountDataApi;
import com.tiket.api.member.GetAccountDataUsingEmailApi;
import com.tiket.api.utilities.NoDeeplinkInboxApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.auth.ProfileDetailResponse;
import com.tiket.pojo.member.AccountData;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import org.apache.commons.io.FileUtils;

public class MemberApi<T extends WebDriver> extends BasePage<T> {
    public MemberApi(Session<T> session) {
        super(session);
    }

    public ProfileDetailResponse getProfileData(String token) {
        GETProfileApi.Inputs input = new GETProfileApi.Inputs(token);
        GETProfileApi getProfileApi = new GETProfileApi(input);
        ApiResponse res = getProfileApi.hitApi(apiTools);
        Gson gson = new Gson();
        ProfileDetailResponse resDeserialized = gson.fromJson(res.getBody(), ProfileDetailResponse.class);
        return resDeserialized;
    }

    private static final ConcurrentHashMap<String, AccountData.Data> accountDataCache = new ConcurrentHashMap<>();

    /**
     * Retrieves account data by email, using a cache to avoid repeated API calls.
     * If the data is not in the cache, it fetches it from the API and stores it in the cache.
     *
     * @param email The email address to look up.
     * @return AccountData.Data object containing account information, or null if not found.
     */
    @SneakyThrows
    public AccountData.Data getAccountDataByEmail(String email) {
        // Check cache first
        AccountData.Data cached = accountDataCache.get(email);
        if (cached != null) {
            logger.log("Returning cached account data for email: " + email);
            return cached;
        }
        GetAccountDataUsingEmailApi.Inputs getAccountDataUsingEmailApiInputs = new GetAccountDataUsingEmailApi.Inputs(email);
        AccountData.Data data = getAccountDataUsingEmailApi(getAccountDataUsingEmailApiInputs);
        if (data != null) {
            accountDataCache.put(email, data);
        }
        return data;
    }

    private AccountData.Data getAccountDataUsingEmailApi(GetAccountDataUsingEmailApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GetAccountDataUsingEmailApi getAccountDataUsingEmailApi = new GetAccountDataUsingEmailApi(inputs);
        ApiResponse response = getAccountDataUsingEmailApi.hitApi(apiTools);
        Gson gson = new Gson();
        AccountData responsePOJO = gson.fromJson(response.getBody(), AccountData.class);
        if(responsePOJO.code.contains("SUCCESS")){
            logger.log("responsePOJO.data.accountId-> " + responsePOJO.data.accountId);
            return responsePOJO.data;
        }
        logger.fail("Failed to get account data");
        return null;
    }

    private static final ConcurrentHashMap<String, CachedToken> tokenCache = new ConcurrentHashMap<>();
    private static final String TOKEN_CACHE_KEY = "admin_dashboard";

    /**
     * A simple class to hold the cached token and its expiry time.
     */
    private static class CachedToken {
        String token;
        String cookieHeader;
        long expiry;
        CachedToken(String token, String cookieHeader, long expiry) {
            this.token = token;
            this.cookieHeader = cookieHeader;
            this.expiry = expiry;
        }
    }

    /**
     * Cookie header for care-dashboard loyalty APIs.
     */
    public String getAdminDashboardCookieHeader() {
        return ensureAdminDashboardSession().cookieHeader;
    }

    /**
     * Retrieves the admin access token for the dashboard.
     * This method caches the token to avoid unnecessary API calls.
     *
     * @return The admin access token prefixed with "Bearer ".
     * @throws Exception If the API call fails or if the token cannot be parsed.
     */
    public String getAdminAccessTokenDashboardAuth(){
        return "Bearer " + ensureAdminDashboardSession().token;
    }

    @SneakyThrows
    private CachedToken ensureAdminDashboardSession() {
        CachedToken cached = tokenCache.get(TOKEN_CACHE_KEY);
        if (cached != null && cached.expiry > System.currentTimeMillis()
                && cached.cookieHeader != null && !cached.cookieHeader.isBlank()) {
            logger.log("Returning cached admin dashboard session");
            return cached;
        }

        JSONArray loginBody = new JSONArray(FileUtils.readFileToString(
                new File("src/test/resources/api/auth/POSTGetAdminAccessTokenDashboard_body.json"),
                Charset.defaultCharset()));
        String username = loginBody.getJSONObject(0).getJSONObject("variables").getString("username");
        String password = loginBody.getJSONObject(0).getJSONObject("variables").getString("password");

        ApiResponse restResponse = new POSTGetAdminAccessTokenDashboardDynamicApi(
                new POSTGetAdminAccessTokenDashboardDynamicApi.Inputs(username, password)).hitApi(apiTools);
        ApiResponse graphqlResponse = null;
        String sessionToken = AdminDashboardLoginResponseParser.resolveSessionTokenOrNull(restResponse);
        if (sessionToken == null || sessionToken.isBlank()) {
            logger.log("REST admin login did not yield session_access_token; trying GraphQL admin login");
            graphqlResponse = new POSTGetAdminAccessTokenDashboardApi().hitApi(apiTools);
            sessionToken = AdminDashboardLoginResponseParser.resolveSessionTokenOrNull(graphqlResponse);
        }

        String cookieHeader = AdminDashboardLoginResponseParser.buildDashboardCookieHeader(
                firstCookie(restResponse, graphqlResponse, "aat"),
                firstCookie(restResponse, graphqlResponse, "art"),
                firstCookie(restResponse, graphqlResponse, "session_access_token"),
                firstCookie(restResponse, graphqlResponse, "session_refresh_token"));
        if ((sessionToken == null || sessionToken.isBlank()) && cookieHeader.contains("aat=")) {
            sessionToken = firstCookie(restResponse, graphqlResponse, "aat");
        }
        if (cookieHeader.isBlank() || sessionToken == null || sessionToken.isBlank()) {
            throw new Exception("Failed to get admin access token");
        }

        long expiry = System.currentTimeMillis() + 3600_000;
        CachedToken session = new CachedToken(sessionToken, cookieHeader, expiry);
        tokenCache.put(TOKEN_CACHE_KEY, session);
        logger.log("Admin dashboard session cached with expiry: " + new Date(expiry));
        return session;
    }

    private static String firstCookie(ApiResponse primary, ApiResponse secondary, String cookieName) {
        String value = AdminDashboardLoginResponseParser.resolveCookieValueOrNull(primary, cookieName);
        if (value != null && !value.isBlank()) {
            return value;
        }
        return AdminDashboardLoginResponseParser.resolveCookieValueOrNull(secondary, cookieName);
    }

    @SneakyThrows
    public AccountData  getAccountData(String accessToken) {
        GetAccountDataApi.Inputs getAccountDataApiInputs = new GetAccountDataApi.Inputs(CoreFunctions.normalizeToken(accessToken));
        return getAccountDataApi(getAccountDataApiInputs);
    }

    private AccountData getAccountDataApi(GetAccountDataApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GetAccountDataApi getAccountDataApi = new GetAccountDataApi(inputs);
        ApiResponse response = getAccountDataApi.hitApi(apiTools);
        Gson gson = new Gson();
        AccountData responsePOJO = gson.fromJson(response.getBody(), AccountData.class);
        if(responsePOJO.code.contains("SUCCESS")){
            logger.log("responsePOJO.data.accountId-> " + responsePOJO.data.accountId);
            return responsePOJO;
        }
        logger.fail("Failed to get account data");
        return null;
    }

    public int createNoDeeplinkMsg(String accountID, String msg) {
        var api = new NoDeeplinkInboxApi(new NoDeeplinkInboxApi.Inputs(accountID, msg));
        ApiResponse apiResponse = api.hitApi(apiTools);
        return apiResponse.getStatusCode();
    }
}
