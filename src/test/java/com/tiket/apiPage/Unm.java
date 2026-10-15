package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.algorithm.PKCE;
import com.tiket.algorithm.PKCEImpl;
import com.tiket.algorithm.PKCEModel;
import com.tiket.api.unm.*;
import com.tiket.common.RetryUtils;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Exception.ApiEnvironmentException;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.auth.MfaMethodsResponse;
import com.tiket.pojo.member.AccountData;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.pojo.unm.GETDeleteCheckUserUNMApiResponse;
import com.tiket.pojo.unm.GetProfileDetailApiResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Optional;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;

public class Unm<T extends WebDriver> extends BasePage<T> {
    private static final ConcurrentHashMap<String, String> accessTokenCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, LoginDataUnmResponse> loginDataCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, List<String>> bliblitiketCookiesCache = new ConcurrentHashMap<>();
    /**
     * Accounts created on this thread for {@link #deleteCreatedAccountQuietly()} to remove.
     * Accumulates so a test that registers more than one user still cleans all of them.
     */
    private static final ThreadLocal<List<AccountToDelete>> accountsToDelete =
            ThreadLocal.withInitial(ArrayList::new);
    private static final int TOKEN_EXPIRY_SKEW_SECONDS = 60;

    /** Max time to retry the service-ticket exchange (login and registration) through transient UNM/member-service 5xx errors and propagation lag. */
    private static final long AUTH_CODE_EXCHANGE_TIMEOUT_MS = 40_000L;

    /** Wait between service-ticket exchange attempts while the UNM/member service settles. */
    private static final long AUTH_CODE_EXCHANGE_INTERVAL_MS = 10_000L;

    private record AuthCodeResult(String authCode, List<String> bliblitiketSetCookies) {}

    private record RegisterResponse(JSONObject body, List<String> bliblitiketSetCookies) {}

    private record AccountToDelete(long userId, String email) {}

    private record RegisteredUnmUser(RegisterNewAccountData account, String authCode, RegisterResponse registerResponse) {}

    static List<String> extractSetCookieHeaders(ApiResponse response) {
        Map<String, ArrayList<Object>> headers = response.getHeaderParams();
        if (headers == null) return Collections.emptyList();
        for (Map.Entry<String, ArrayList<Object>> entry : headers.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("set-cookie") && entry.getValue() != null) {
                return entry.getValue().stream().map(Object::toString).toList();
            }
        }
        return Collections.emptyList();
    }

    public Unm(Session<T> session) {
        super(session);
    }

    public static boolean isTokenExpired(String token) {
        try {
            JSONObject json = decodeJwtPayload(token);
            long exp = json.getLong("exp");
            long nowSeconds = System.currentTimeMillis() / 1000;
            return nowSeconds >= (exp - TOKEN_EXPIRY_SKEW_SECONDS);
        } catch (Exception e) {
            return true;
        }
    }

    /** Base cache key (login data, bliblitiket cookies) for a user on the current driver session. */
    private String userCacheKey(String username) {
        return session.sessionId() + ":" + username;
    }

    /**
     * Access-token cache key, scoped by account type because B2B and B2C tokens have different shapes
     * (B2B is the raw token; B2C is {@code "Bearer "}-prefixed) and must never be served across types.
     * Any non-{@code B2B} value maps to {@code B2C}, mirroring the branch that decides the token format.
     */
    private String accessTokenCacheKey(String username, String accountType) {
        String typeSegment = accountType != null && accountType.equalsIgnoreCase("B2B") ? "B2B" : "B2C";
        return session.sessionId() + ":" + typeSegment + ":" + username;
    }

    /**
     * Removes any cached access token / login data for {@code username} on the current driver session.
     * Use after a UI logout: the server has revoked the token, but JWT {@code exp} is still in the
     * future so {@link #isTokenExpired(String)} cannot detect it. Without this, the next
     * {@code getAccessToken} call would hand back a revoked token and session-cookie injection
     * would silently fail.
     */
    public void invalidateCachedTokens(String username) {
        String userKey = userCacheKey(username);
        boolean removedToken = accessTokenCache.remove(accessTokenCacheKey(username, "B2C")) != null;
        removedToken |= accessTokenCache.remove(accessTokenCacheKey(username, "B2B")) != null;
        if (removedToken) {
            logger.log("Invalidated cached access token for " + userKey);
        }
        if (loginDataCache.remove(userKey) != null) {
            logger.log("Invalidated cached login data for " + userKey);
        }
        bliblitiketCookiesCache.remove(userKey);
    }

    /**
     * Removes all cached tokens/login data belonging to the current driver session,
     * regardless of username. Safe to call when the previously-logged-in email is unknown
     * (e.g. inside a generic {@code loginAsGuest()} that just performed a UI logout).
     */
    public void invalidateAllCachedTokensForCurrentSession() {
        String prefix = session.sessionId() + ":";
        accessTokenCache.keySet().removeIf(k -> k.startsWith(prefix));
        loginDataCache.keySet().removeIf(k -> k.startsWith(prefix));
        bliblitiketCookiesCache.keySet().removeIf(k -> k.startsWith(prefix));
    }

    public static String getEmailFromToken(String token) {
        try {
            JSONObject json = decodeJwtPayload(token);
            if (json.has("email")) return json.getString("email");
            if (json.has("sub")) return json.getString("sub");
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Tells a member session token apart from the <em>guest</em> session token the tiket web app mints
     * for anonymous visitors. Both are signed by {@code https://www.tiket.com} and both carry {@code sub}
     * and a two-week {@code exp}, so neither presence nor {@link #isTokenExpired(String)} can separate
     * them; only a member token carries the {@code accountid}/{@code email} claims.
     *
     * <p>Used by cookie-injection login to detect that the app has overwritten the injected member
     * token with a guest one, which otherwise surfaces much later as a logged-out UI or an account API
     * returning no data.
     */
    public static boolean isMemberToken(String token) {
        if (token == null || token.isBlank()) return false;
        try {
            JSONObject json = decodeJwtPayload(token);
            return json.has("accountid") || json.has("email");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * The {@code email} claim of a session token, or {@code null} when the token carries none
     * (guest token, or a shape without the claim). Unlike {@link #getEmailFromToken(String)} this
     * never falls back to {@code sub}, so the result is safe to compare against an expected account.
     */
    public static String getEmailClaim(String token) {
        if (token == null || token.isBlank()) return null;
        try {
            JSONObject json = decodeJwtPayload(token);
            return json.has("email") ? json.getString("email") : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * The session refresh token for {@code username}, reusing the login data cached by
     * {@link #getAccessToken(String, String, String)} / {@link #unmLoginApi(String, String)} on this
     * driver session and authenticating only when nothing usable is cached.
     *
     * <p>The browser session is the {@code session_access_token} + {@code session_refresh_token} pair:
     * injecting the access token alone leaves the web app unable to refresh, and it responds by issuing
     * a fresh guest session that overwrites the injected one.
     */
    @SneakyThrows
    public String getRefreshToken(String username, String password) {
        LoginDataUnmResponse cached = loginDataCache.get(userCacheKey(username));
        if (cached != null && cached.data != null && cached.data.refreshToken != null
                && !cached.data.refreshToken.isBlank() && !isTokenExpired(cached.data.accessToken)) {
            return cached.data.refreshToken;
        }
        return unmLoginApi(username, password).refreshToken;
    }

    public List<String> getBliblitiketCookies(String username) {
        return bliblitiketCookiesCache.getOrDefault(userCacheKey(username), Collections.emptyList());
    }

    private static JSONObject decodeJwtPayload(String token) {
        String raw = token.startsWith("Bearer ") ? token.substring(7) : token;
        String[] parts = raw.split("\\.");
        if (parts.length < 2) throw new IllegalArgumentException("Invalid JWT");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        return new JSONObject(payload);
    }

    /**
     * Retrieves the access token for a user by performing a multi-step UNM authentication flow.
     *
     * <p>This method:
     * <ol>
     *   <li>Requests an auth code using the provided username and password.</li>
     *   <li>Exchanges the auth code for a service ticket.</li>
     *   <li>Uses the service ticket to obtain the final access token.</li>
     * </ol>
     *
     * <p>For B2C the service-ticket exchange reads the whole login payload, so a fresh authentication
     * here also fills the login-data cache that {@link #unmLoginApi(String, String)} reads. Without
     * that, a UI cookie-injection login (which only needs a token) and a later API call needing the
     * account id would each run a full UNM authentication for the same account on the same driver
     * session. B2B keeps its original token-only exchange and caches nothing beyond its own token,
     * because its token has a different shape and its callers are not the ones paying for the second
     * login.
     *
     * @param username     The user's login username.
     * @param password     The corresponding password.
     * @param accountType  The type of account (e.g., B2B, B2C).
     * @return             The generated access token for the authenticated user.
     * @throws RuntimeException if any exception occurs during the process (due to {@code @SneakyThrows}).
     */
    @SneakyThrows
    public String getAccessToken(String username, String password,String accountType) {
        String tokenCacheKey = accessTokenCacheKey(username, accountType);
        String cachedToken = accessTokenCache.get(tokenCacheKey);
        if (cachedToken != null && !isTokenExpired(cachedToken)) {
            logger.log("Returning cached access token for " + tokenCacheKey);
            return cachedToken;
        }
        if (cachedToken != null) {
            logger.log("Cached token expired for " + tokenCacheKey + ", re-authenticating");
            accessTokenCache.remove(tokenCacheKey);
        }

        POSTAuthLoginUNMApi.Inputs authCodeInputs = new POSTAuthLoginUNMApi.Inputs(username,password);
        AuthCodeResult authCodeResult = getAuthCode(authCodeInputs);
        logger.log("auth code : " + authCodeResult.authCode());

        if (!authCodeResult.bliblitiketSetCookies().isEmpty()) {
            String cookieCacheKey = userCacheKey(username);
            bliblitiketCookiesCache.put(cookieCacheKey, authCodeResult.bliblitiketSetCookies());
            logger.log("Cached " + authCodeResult.bliblitiketSetCookies().size() + " bliblitiket Set-Cookie(s) for " + cookieCacheKey);
        }

        if (accountType != null && accountType.equalsIgnoreCase("B2B")) {
            String b2bToken = exchangeServiceTicketWithRetry(authCodeResult.authCode(),
                    serviceTicket -> getAccessToken(new POSTServiceTicketUNMApi.Inputs(serviceTicket), username, accountType));
            logger.log("access token : " + b2bToken);
            accessTokenCache.put(tokenCacheKey, b2bToken);
            return b2bToken;
        }

        LoginDataUnmResponse loginDataUnmResponse = exchangeServiceTicketWithRetry(authCodeResult.authCode(),
                serviceTicket -> postServiceTicketUNM(new POSTServiceTicketUNMApi.Inputs(serviceTicket)));
        loginDataCache.put(userCacheKey(username), loginDataUnmResponse);
        String accessToken = loginDataUnmResponse.data.accessToken;
        logger.log("access token : " + accessToken);
        accessTokenCache.put(tokenCacheKey, accessToken);
        return accessToken;
    }

    /**
     * Logs into the UNM system using the provided username and password.
     * <ul>
     *     <li>Retrieves an authorization code by submitting credentials.</li>
     *     <li>Uses the auth code to obtain a service ticket.</li>
     *     <li>Exchanges the service ticket for an access token and user data.</li>
     * </ul>
     *
     * @param username the UNM account email or username
     * @param password the corresponding password
     * @return {@link LoginDataUnmResponse.Data} for the authenticated user
     * @throws Exception if any step in the login flow fails, or if the service-ticket exchange does not
     *                   succeed within the transient-retry budget
     */
    @SneakyThrows
    public LoginDataUnmResponse.Data unmLoginApi(String username, String password) {
        String cacheKey = userCacheKey(username);
        LoginDataUnmResponse cachedLoginData = loginDataCache.get(cacheKey);
        if (cachedLoginData != null && cachedLoginData.data != null && !isTokenExpired(cachedLoginData.data.accessToken)) {
            logger.log("Returning cached LoginDataUnmResponse for " + cacheKey);
            return cachedLoginData.data;
        }
        if (cachedLoginData != null) {
            logger.log("Cached login data expired for " + cacheKey + ", re-authenticating");
            loginDataCache.remove(cacheKey);
        }
        logger.log("logging in with email:" + username);
        POSTAuthLoginUNMApi.Inputs authCodeInputs = new POSTAuthLoginUNMApi.Inputs(username,password);
        AuthCodeResult authCodeResult = getAuthCode(authCodeInputs);
        logger.log("auth code : " + authCodeResult.authCode());

        LoginDataUnmResponse loginDataUnmResponse = exchangeServiceTicketWithRetry(authCodeResult.authCode(),
                serviceTicket -> postServiceTicketUNM(new POSTServiceTicketUNMApi.Inputs(serviceTicket)));
        logger.log("access token : " + loginDataUnmResponse.data.accessToken);
        loginDataCache.put(cacheKey, loginDataUnmResponse);
        cacheB2cSessionArtifacts(username, loginDataUnmResponse.data.accessToken, authCodeResult.bliblitiketSetCookies());
        Assertion.assertThat("login via API success", loginDataUnmResponse.data.accountUsername.toLowerCase(), is(username.toLowerCase()));
        return loginDataUnmResponse.data;
    }

    /**
     * Warms the B2C-scoped access-token cache and the bliblitiket-cookie cache from a completed login or
     * registration, so a later {@code getAccessToken(..., "B2C")} or cookie-injection login reuses this
     * session instead of running a second full UNM login. The token is stored under the B2C-scoped key
     * only; B2B tokens have a different shape and live under their own key, so they are never affected.
     *
     * @param username    the account whose session this is (cache key)
     * @param bearerToken the {@code "Bearer "}-prefixed access token (B2C shape)
     * @param setCookies  the bliblitiket {@code Set-Cookie} headers, or empty/{@code null} if none captured
     */
    private void cacheB2cSessionArtifacts(String username, String bearerToken, List<String> setCookies) {
        accessTokenCache.put(accessTokenCacheKey(username, "B2C"), bearerToken);
        if (setCookies != null && !setCookies.isEmpty()) {
            String cookieCacheKey = userCacheKey(username);
            bliblitiketCookiesCache.put(cookieCacheKey, setCookies);
            logger.log("Cached " + setCookies.size() + " bliblitiket Set-Cookie(s) for " + cookieCacheKey);
        }
    }

    /**
     * <b>Legacy</b> non-PKCE flow that returns the {@code SERVICE_TICKET} JWT for the given
     * credentials. Kept only for backwards-compat with callers that still rely on the JWT shape
     * (e.g. tests that decode the embedded {@code sessionId}/{@code userId} claims directly).
     *
     * <p>Flow:
     * <ol>
     *   <li>{@code POST /auth/login} (no {@code codeChallenge}) → auth code</li>
     *   <li>{@code GET /auth/code/verify?authCode=…} → {@code data.serviceTicket}
     *       (a JWT with {@code topic=SERVICE_TICKET})</li>
     * </ol>
     *
     * <p><b>Do not use this for the {@code tiket-internal://login} deeplink.</b>
     * {@code AutomationDeeplinkActivity} now expects the short PKCE session id; passing the
     * long service-ticket JWT works only by accident in older app builds. Use
     * {@link #getUnmSessionIdViaPKCE(String, String)} instead, which returns
     * {@code data.session.id} from {@code POST /auth/code-challenge/verify}
     * (e.g. {@code UxEexzPxW3z9vqo24blCsZzVlDjMDqQD}).
     *
     * @param username the UNM account email
     * @param password the corresponding password
     * @return the service-ticket JWT string
     * @deprecated use {@link #getUnmSessionIdViaPKCE(String, String)} for deeplink login;
     *             this legacy method will be removed once all non-deeplink callers have migrated.
     */
    @Deprecated
    @SneakyThrows
    public String getUnmSessionId(String username, String password) {
        logger.log("Obtaining UNM service ticket (legacy non-PKCE flow) for " + username);

        POSTAuthLoginUNMApi.Inputs authCodeInputs = new POSTAuthLoginUNMApi.Inputs(username, password);
        AuthCodeResult authCodeResult = getAuthCode(authCodeInputs);
        String authCode = authCodeResult.authCode();
        logger.log("auth code: " + authCode);

        GETAuthLoginUNMApi.Inputs serviceTicketInputs = new GETAuthLoginUNMApi.Inputs(authCode);
        String serviceTicket = getServiceTicket(serviceTicketInputs);
        logger.log("UNM session ID (service ticket): " + serviceTicket);

        return serviceTicket;
    }

    /*
        * Retrieves the authorization code for a user by performing a multi-step UNM authentication flow.
        * <p>This method:
        * <ol>
        *   <li>Requests an auth code using the provided username and password.</li>
        *  <li>Handles multi-factor authentication (MFA) if required.</li>
        *  <li>Returns the final auth code in Bearer token format.</li>
        * </ol>
        * @param username     The user's login username.
        * @param password     The corresponding password.
        * @return             The generated authorization code in Bearer token format.
        * @throws RuntimeException if any exception occurs during the process (due to {@code @SneakyThrows}).
        * * <p>Note: This method may throw an exception if MFA is required and not handled properly.</p>
     */
    @SneakyThrows
    public String getAuthCode(String username, String password) {
        POSTAuthLoginUNMApi.Inputs authCodeInputs = new POSTAuthLoginUNMApi.Inputs(username, password);
        AuthCodeResult authCodeResult = getAuthCode(authCodeInputs);
        logger.log("auth code : " + authCodeResult.authCode());
        return "Bearer " + authCodeResult.authCode();
    }

    private AuthCodeResult getAuthCode(POSTAuthLoginUNMApi.Inputs inputs) throws ExecutionException, InterruptedException {
        POSTAuthLoginUNMApi postGenerateAuthCodeB2BApi = new POSTAuthLoginUNMApi(inputs);
        ApiResponse authCodeResponse = postGenerateAuthCodeB2BApi.hitApi(apiTools);
        JSONObject authCodeObject = new JSONObject(authCodeResponse.getBody());
        logger.log("auth code res -> " + authCodeObject);
        if(authCodeObject.getString("code").contains("MFA_REQUIRED")){
            logger.log("==MFA_REQUIRED==");
            AuthCodeResult mfaResult = removeMFA(authCodeObject.getJSONObject("data").getString("token"));
            return new AuthCodeResult(removeExtraFromUrlToGetAuthCode(mfaResult.authCode()), mfaResult.bliblitiketSetCookies());
        }
        List<String> setCookies = extractSetCookieHeaders(authCodeResponse);
        String redirectURL = authCodeObject.getJSONObject("data").getString("redirectUrl");
        return new AuthCodeResult(removeExtraFromUrlToGetAuthCode(redirectURL), setCookies);
    }

    private String removeExtraFromUrlToGetAuthCode(String  url){
        String part1 = url.split("authCode=")[1];
        return part1.split("&ref=")[0];
    }

    private String getChannelId(String type){
        return switch (type) {
            case "WA_OTP" -> "WHATS_APP";
            case "SMS_OTP" -> "SMS";
            case "EMAIL_OTP" -> "EMAIL";
            default -> throw new NotImplementedException("The type " + type + " is not implemented");
        };
    }

    @SneakyThrows
    private AuthCodeResult removeMFA(String token){
        logger.log("==proceedings with mfa==");

        GETMfaMethodsUNMApi.Inputs getMfaMethodsInputs = new GETMfaMethodsUNMApi.Inputs(token);
        MfaMethodsResponse mfaMethod = getMfaMethods(getMfaMethodsInputs);
        logger.log("otpRecipientId : " + mfaMethod.data.otpRecipientId);
        logger.log("token : " + mfaMethod.data.token);
        String type = mfaMethod.data.methods.get(0).type;
        logger.log("type : " + type);
        String channel = getChannelId(type);

        POSTOtpGenerateUNMApi.Inputs postOtpGenerateUnmInputs = new POSTOtpGenerateUNMApi.Inputs(mfaMethod.data.otpRecipientId,"LOGIN_OTP",channel);

        String otpId = postOtpGenerateUnm(postOtpGenerateUnmInputs);
        logger.log("otpID : " + otpId);

        POSTOtpVerifyUNMApi.Inputs postOtpVerifyUNMApiInputs = new POSTOtpVerifyUNMApi.Inputs(otpId,"LOGIN_OTP");
        String passCode = postOtpVerifyUNMApi(postOtpVerifyUNMApiInputs);
        logger.log("passCode : " + passCode);

        POSTMfaSubmitUNMApi.Inputs postMfaSubmitUNMApiInputs = new POSTMfaSubmitUNMApi.Inputs(passCode,mfaMethod.data.token,type);
        ApiResponse mfaSubmitResponse = postMfaSubmitUNMApiRaw(postMfaSubmitUNMApiInputs);
        JSONObject mfaSubmitObject = new JSONObject(mfaSubmitResponse.getBody());
        logger.log("code mfa submit : -> " + mfaSubmitObject.getString("code"));
        String redirectUrl = mfaSubmitObject.getJSONObject("data").getString("redirectUrl");
        logger.log("redirect url : " + redirectUrl);

        List<String> setCookies = extractSetCookieHeaders(mfaSubmitResponse);
        return new AuthCodeResult(redirectUrl, setCookies);
    }

    private MfaMethodsResponse getMfaMethods(GETMfaMethodsUNMApi.Inputs inputs)throws ExecutionException, InterruptedException{
        GETMfaMethodsUNMApi getMfaMethodsUNMApi = new GETMfaMethodsUNMApi(inputs);
        ApiResponse getMfaMethodsUNMApiResponse = getMfaMethodsUNMApi.hitApi(apiTools);
        JSONObject postOtpGenerateUNMApiObject = new JSONObject(getMfaMethodsUNMApiResponse.getBody());
        logger.log("code mfa methods res : -> " + postOtpGenerateUNMApiObject);

        Gson gson = new Gson();
        MfaMethodsResponse mfaMethodsResponse = gson.fromJson(getMfaMethodsUNMApiResponse.getBody(), MfaMethodsResponse.class);
        logger.log("code mfa methods : " + mfaMethodsResponse.code);
        return mfaMethodsResponse;
    }

    private String postOtpGenerateUnm(POSTOtpGenerateUNMApi.Inputs inputs)throws ExecutionException, InterruptedException{
        POSTOtpGenerateUNMApi postOtpGenerateUNMApi = new POSTOtpGenerateUNMApi(inputs);
        ApiResponse postOtpGenerateUNMApiResponse = postOtpGenerateUNMApi.hitApi(apiTools);
        JSONObject postOtpGenerateUNMApiObject = new JSONObject(postOtpGenerateUNMApiResponse.getBody());
        String code = postOtpGenerateUNMApiObject.optString("code", "UNKNOWN");
        logger.log("code otp generate : -> " + code);
        if (postOtpGenerateUNMApiObject.isNull("data")) {
            String msg = postOtpGenerateUNMApiObject.optString("message", "no message");
            throw new RuntimeException("OTP generate failed (code=" + code + "): " + msg);
        }
        return postOtpGenerateUNMApiObject.getJSONObject("data").getString("otpId");
    }

    private String postOtpVerifyUNMApi(POSTOtpVerifyUNMApi.Inputs inputs)throws ExecutionException, InterruptedException{
        POSTOtpVerifyUNMApi postOtpVerifyUNMApi = new POSTOtpVerifyUNMApi(inputs);
        ApiResponse postOtpVerifyUNMApiResponse = postOtpVerifyUNMApi.hitApi(apiTools);
        JSONObject postOtpVerifyUNMApiObject = new JSONObject(postOtpVerifyUNMApiResponse.getBody());
        String code = postOtpVerifyUNMApiObject.optString("code", "UNKNOWN");
        logger.log("code verify otp methods : -> " + code);
        if (postOtpVerifyUNMApiObject.isNull("data")) {
            String msg = postOtpVerifyUNMApiObject.optString("message", "no message");
            throw new RuntimeException("OTP verify failed (code=" + code + "): " + msg);
        }
        return postOtpVerifyUNMApiObject.getJSONObject("data").getString("passCode");
    }

    private ApiResponse postMfaSubmitUNMApiRaw(POSTMfaSubmitUNMApi.Inputs inputs)throws ExecutionException, InterruptedException{
        POSTMfaSubmitUNMApi postMfaSubmitUNMApi = new POSTMfaSubmitUNMApi(inputs);
        return postMfaSubmitUNMApi.hitApi(apiTools);
    }

    private String getServiceTicket(GETAuthLoginUNMApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GETAuthLoginUNMApi getServiceTicketB2BApi = new GETAuthLoginUNMApi(inputs);
        ApiResponse serviceTicketResponse = getServiceTicketB2BApi.hitApi(apiTools);
        JSONObject serviceTicketObject = new JSONObject(serviceTicketResponse.getBody());
        logger.log("service ticket res -> " + serviceTicketObject);

        return serviceTicketObject.getJSONObject("data").getString("serviceTicket");
    }

    /**
     * Token-only reading of the service-ticket exchange, kept for the B2B branch of
     * {@link #getAccessToken(String, String, String)}: B2B callers want the raw token rather than the
     * {@code "Bearer "}-prefixed one, and this parser treats a missing {@code data} - not a non-SUCCESS
     * {@code code} - as the "not ready yet" signal the retry budget is built around.
     */
    private String getAccessToken(POSTServiceTicketUNMApi.Inputs inputs,String username,String accountType) throws ExecutionException, InterruptedException {
        POSTServiceTicketUNMApi postGenerateAccessTokenB2BApi = new POSTServiceTicketUNMApi(inputs);
        ApiResponse accessTokenResponse = postGenerateAccessTokenB2BApi.hitApi(apiTools);
        JSONObject accessTokenObject = new JSONObject(accessTokenResponse.getBody());
        logger.log("access token res -> " + accessTokenObject);
        if (accessTokenObject.isNull("data")) {
            logger.softWarning("Access-token exchange not ready yet (code="
                    + accessTokenObject.optString("code", "UNKNOWN") + ")");
            return null;
        }
        boolean isB2BCorp = accessTokenObject.getJSONObject("data").getBoolean("isB2BCorp");
        String token = accessTokenObject.getJSONObject("data").getString("accessToken");

        if (accountType.equalsIgnoreCase("B2B")) {
            if (isB2BCorp) return token;
            throw new NotImplementedException("The account " + username + " is not a B2B account");
        } else{
            logger.log("the account " + username + " is not a B2C account :" + !isB2BCorp);
            return "Bearer " + token;
        }
    }

    private LoginDataUnmResponse postServiceTicketUNM(POSTServiceTicketUNMApi.Inputs inputs) throws ExecutionException, InterruptedException {
        POSTServiceTicketUNMApi postGenerateAccessTokenB2BApi = new POSTServiceTicketUNMApi(inputs);
        ApiResponse accessTokenResponse = postGenerateAccessTokenB2BApi.hitApi(apiTools);

        Gson gson = new Gson();
        LoginDataUnmResponse loginDataUnmResponse = gson.fromJson(accessTokenResponse.getBody(), LoginDataUnmResponse.class);
        if (loginDataUnmResponse.code.equalsIgnoreCase("SUCCESS")) {
            logger.log("The account " + loginDataUnmResponse.data.accountUsername
                    + " (B2C=" + !loginDataUnmResponse.data.isB2BCorp + ") is successfully logged in");
            loginDataUnmResponse.data.accessToken = "Bearer " + loginDataUnmResponse.data.accessToken;
            return loginDataUnmResponse;
        }
        logger.softWarning("Service-ticket exchange not SUCCESS yet (code=" + loginDataUnmResponse.code + ")");
        return null;
    }

    /**
     * Creates a new test account with randomized email, name, and phone number.
     *
     * @return a populated {@link RegisterNewAccountData} object for registration use.
     */
    private RegisterNewAccountData createNewAccountData() {
        RegisterNewAccountData registerNewAccountData = new RegisterNewAccountData();
        String email = "autApps" + CoreFunctions.getCurrentTime() + "@mailinator.com";
        String password = "automationTest" + CoreFunctions.randomInt(10);
        String fullName = CoreFunctions.randomName();
        String phoneCountryCode = "62";
        String phoneNationalNumber = "81" + CoreFunctions.randomInt(8);
        String phoneNumber = "+" + phoneCountryCode + phoneNationalNumber;

        registerNewAccountData.email = email;
        registerNewAccountData.password = password;
        registerNewAccountData.fullName = fullName;
        registerNewAccountData.phoneCountryCode = phoneCountryCode;
        registerNewAccountData.phoneNationalNumber = phoneNationalNumber;
        registerNewAccountData.phoneNumber = phoneNumber;

        logger.log("email : " + registerNewAccountData.email);
        logger.log("password : " + registerNewAccountData.password);
        logger.log("fullName : " + registerNewAccountData.fullName);
        logger.log("phoneCountryCode : " + registerNewAccountData.phoneCountryCode);
        logger.log("phoneNationalNumber : " + registerNewAccountData.phoneNationalNumber);
        logger.log("phoneNumber : " + registerNewAccountData.phoneNumber);

        return registerNewAccountData;
    }

    /**
     * Registers a new UNM account via API using WhatsApp OTP flow.
     * <ul>
     *     <li>Generates OTP for the random phone number</li>
     *     <li>Verifies the OTP</li>
     *     <li>Registers the user</li>
     *     <li>Retrieves auth code and service ticket</li>
     *     <li>Uses service ticket to fetch user token</li>
     * </ul>
     *
     * @return {@link RegisterNewAccountData} with additional login response details populated.
     * @throws NotImplementedException if registration fails or response is invalid.
     */
    @SneakyThrows
    public RegisterNewAccountData  registerNewAccountViaApiUNM() {
        RegisteredUnmUser registered = submitNormalUnmRegistration();
        RegisterNewAccountData registerNewAccountData = registered.account();
        LoginDataUnmResponse loginDataUnmResponse = exchangeAuthCodeForLoginData(registered.authCode());
        logger.log("unmId : " + loginDataUnmResponse.data.unmUserId);
        Assertion.assertThat("Register via API success", loginDataUnmResponse.data.accountUsername.toLowerCase(), is(registerNewAccountData.email.toLowerCase()));
        registerNewAccountData.unmData = loginDataUnmResponse.data;
        cacheRegisteredSession(registerNewAccountData.email, loginDataUnmResponse, registered.registerResponse().bliblitiketSetCookies());
        return registerNewAccountData;
    }

    /**
     * Registers a normal UNM user (OTP + {@code /registration/submit}) without exchanging a member-core
     * service ticket. Enough when the test only needs email, name, phone, and unm user id.
     */
    public RegisterNewAccountData registerNormalUnmUser() {
        RegisterNewAccountData account = submitNormalUnmRegistration().account();
        trackAccountForCleanup(account.unmData.unmUserId, account.email);
        return account;
    }

    private RegisteredUnmUser submitNormalUnmRegistration() {
        return RetryUtils.retryWithExponentialBackoff(
                3, 2000L,
                this::attemptNormalUnmRegistration,
                e -> !(e instanceof org.testng.SkipException),
                "UNM normal registration");
    }

    @SneakyThrows
    private RegisteredUnmUser attemptNormalUnmRegistration() {
        RegisterNewAccountData registerNewAccountData = createNewAccountData();

        POSTOtpGenerateUNMApi.Inputs postOtpGenerateUnmInputs = new POSTOtpGenerateUNMApi.Inputs(registerNewAccountData.phoneNumber, "REGISTER_OTP", "WHATS_APP");
        String otpId = postOtpGenerateUnm(postOtpGenerateUnmInputs);
        logger.log("otpID : " + otpId);

        POSTOtpVerifyUNMApi.Inputs postOtpVerifyUNMApiInputs = new POSTOtpVerifyUNMApi.Inputs(otpId, "REGISTER_OTP");
        String passCode = postOtpVerifyUNMApi(postOtpVerifyUNMApiInputs);
        logger.log("passCode : " + passCode);

        RegisterNewUserUNMApi.Inputs registerInput = new RegisterNewUserUNMApi.Inputs(registerNewAccountData.email, registerNewAccountData.fullName, passCode, registerNewAccountData.password, registerNewAccountData.phoneCountryCode, registerNewAccountData.phoneNationalNumber);
        RegisterResponse registerResponse = registerNewUserUNMApi(registerInput);
        JSONObject registerNewUserApi = registerResponse.body();

        if (!registerNewUserApi.getString("code").equalsIgnoreCase("success")) {
            logger.log("Response register api : \n" + registerNewUserApi.getString("message"));
            Assertion.fail("Register via api fail");
        }

        String authCode = removeExtraFromUrlToGetAuthCode(registerNewUserApi.getJSONObject("data").getString("redirectUrl"));
        logger.log("auth code : " + authCode);

        LoginDataUnmResponse.Data unmData = new LoginDataUnmResponse.Data();
        try {
            unmData.unmUserId = decodeJwtPayload(authCode).getLong("userId");
        } catch (RuntimeException e) {
            Assertion.fail("UNM registration succeeded but the auth code JWT had no userId: " + e.getMessage());
        }
        unmData.accountUsername = registerNewAccountData.email;
        registerNewAccountData.unmData = unmData;
        logger.log("unmId : " + unmData.unmUserId);
        return new RegisteredUnmUser(registerNewAccountData, authCode, registerResponse);
    }

    /**
     * Account created when BCA hits UNM entity registration. {@code derivedName} is what UNM stored
     * (from the email local-part, trailing digits stripped) — not necessarily the {@code name} sent
     * in the request.
     */
    public record BcaCobrandCardholder(
            String email,
            String derivedName,
            String phoneNationalNumber,
            long userId,
            boolean isNewUser
    ) {
    }

    /**
     * Registers a new BCA cobrand cardholder via {@code POST /entity/registration/submit}.
     * Email and phone are generated per call. Phone extra digits are 7–8: TestRail allows 7–9 after
     * the prefix, but 9 extra digits (12-digit national number) is rejected as invalid.
     */
    public BcaCobrandCardholder registerBcaCobrandCardholder(
            String name,
            String emailDomain,
            String phoneCountryCode,
            String phoneNationalNumberPrefix,
            String type
    ) {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            Assertion.skip("BCA cobrand entity registration has no production access");
        }

        POSTEntityRegistrationSubmitApi.Inputs inputs = buildCobrandRegistrationInputs(
                name, emailDomain, phoneCountryCode, phoneNationalNumberPrefix, type);
        logger.log("BCA cobrand registration email: " + inputs.email());
        logger.log("BCA cobrand registration phone: " + inputs.phoneCountryCode() + inputs.phoneNationalNumber());

        BcaCobrandCardholder cardholder = RetryUtils.retryWithExponentialBackoff(
                3, 2000L,
                () -> submitCobrandRegistration(inputs),
                e -> !(e instanceof org.testng.SkipException),
                "BCA cobrand entity registration");
        trackAccountForCleanup(cardholder.userId(), cardholder.email());
        logger.log("BCA cobrand userId: " + cardholder.userId() + ", derived name: " + cardholder.derivedName());
        return cardholder;
    }

    /**
     * Maps an existing UNM account as a BCA cobrand cardholder via {@code POST /entity/registration/submit}.
     * The request name is random so the dashboard can prove the stored name is not overwritten.
     */
    public BcaCobrandCardholder mapExistingAccountAsBcaCobrand(
            String email,
            String phoneCountryCode,
            String phoneNationalNumber,
            String type
    ) {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            Assertion.skip("BCA cobrand entity registration has no production access");
        }

        String randomName = CoreFunctions.randomName();
        logger.log("BCA cobrand mapping email: " + email);
        logger.log("BCA cobrand mapping random name: " + randomName);

        POSTEntityRegistrationSubmitApi.Inputs inputs = new POSTEntityRegistrationSubmitApi.Inputs(
                email, randomName, internationalCountryCode(phoneCountryCode), phoneNationalNumber, type);
        BcaCobrandCardholder cardholder = RetryUtils.retryWithExponentialBackoff(
                3, 2000L,
                () -> submitCobrandRegistration(inputs),
                e -> !(e instanceof org.testng.SkipException),
                "BCA cobrand existing-user mapping");
        List<AccountToDelete> tracked = accountsToDelete.get();
        boolean mappedTrackedAccount = tracked.stream().anyMatch(a -> a.userId() == cardholder.userId());
        if (cardholder.isNewUser()) {
            deleteCreatedAccountQuietly(cardholder.userId(), cardholder.email());
            Assertion.fail("BCA cobrand mapping created a new user instead of mapping the existing UNM account");
        }
        if (!tracked.isEmpty() && !mappedTrackedAccount) {
            deleteCreatedAccountQuietly(cardholder.userId(), cardholder.email());
            Assertion.fail("BCA cobrand mapping returned userId " + cardholder.userId()
                    + " but none of the tracked UNM accounts match");
        }
        logger.log("BCA cobrand mapped userId: " + cardholder.userId()
                + ", isNewUser: " + cardholder.isNewUser()
                + ", stored name: " + cardholder.derivedName());
        return cardholder;
    }

    private void trackAccountForCleanup(long userId, String email) {
        List<AccountToDelete> tracked = accountsToDelete.get();
        boolean alreadyTracked = tracked.stream().anyMatch(a -> a.userId() == userId);
        if (!alreadyTracked) {
            tracked.add(new AccountToDelete(userId, email));
        }
    }

    private POSTEntityRegistrationSubmitApi.Inputs buildCobrandRegistrationInputs(
            String name,
            String emailDomain,
            String phoneCountryCode,
            String phoneNationalNumberPrefix,
            String type
    ) {
        String emailLocal = name.trim().toLowerCase().replaceAll("[^a-z0-9]+", ".")
                .replaceAll("^\\.|\\.$", "")
                + CoreFunctions.generateRandomNumeric(8);
        int extraDigits = CoreFunctions.generateRandomInt(7, 8);
        return new POSTEntityRegistrationSubmitApi.Inputs(
                emailLocal + "@" + emailDomain,
                name,
                internationalCountryCode(phoneCountryCode),
                phoneNationalNumberPrefix + CoreFunctions.generateRandomNumeric(extraDigits),
                type);
    }

    private static String internationalCountryCode(String phoneCountryCode) {
        return phoneCountryCode.startsWith("+") ? phoneCountryCode : "+" + phoneCountryCode;
    }

    private BcaCobrandCardholder submitCobrandRegistration(POSTEntityRegistrationSubmitApi.Inputs inputs) {
        ApiResponse response = new POSTEntityRegistrationSubmitApi(inputs).hitApi(apiTools);
        int status = response.getStatusCode();
        String bodyText = response.getBody() == null ? "" : response.getBody().trim();

        if (status >= 400 && status < 500) {
            Assertion.fail("BCA cobrand registration HTTP " + status + ": " + bodyText);
        }
        if (status < 200 || status >= 300 || !bodyText.startsWith("{")) {
            throw new RuntimeException("BCA cobrand registration returned HTTP " + status
                    + (bodyText.isEmpty() ? " with empty body" : " with non-JSON body"));
        }

        JSONObject body = new JSONObject(bodyText);
        logger.log("BCA cobrand registration response: " + body);
        if (!body.optString("code").equalsIgnoreCase("SUCCESS")) {
            Assertion.fail("BCA cobrand registration failed: " + body.optString("message")
                    + " " + body.opt("errors"));
        }

        JSONObject data = body.getJSONObject("data");
        return new BcaCobrandCardholder(
                data.getString("email"),
                data.getString("name"),
                data.getString("phoneNationalNumber"),
                data.getLong("userId"),
                data.getBoolean("isNewUser"));
    }

    /**
     * Warms the auth caches with the session obtained during registration so the immediate follow-up
     * cookie-injection login ({@code loginWithSpecificAccountViaCookie}) reuses it instead of running a
     * second full UNM login. Registration already produced a valid access token (confirmed against the
     * member service via {@link #exchangeAuthCodeForLoginData(String)}) plus the bliblitiket session
     * cookies returned by {@code registration/submit} — exactly the inputs cookie injection needs.
     *
     * @param email                  the freshly registered account email (the key the login call reuses)
     * @param loginData              the successful login data from the registration service-ticket exchange
     * @param registrationSetCookies the {@code Set-Cookie} headers returned by the registration endpoint
     */
    private void cacheRegisteredSession(String email, LoginDataUnmResponse loginData, List<String> registrationSetCookies) {
        loginDataCache.put(userCacheKey(email), loginData);
        cacheB2cSessionArtifacts(email, loginData.data.accessToken, registrationSetCookies);
        logger.log("Cached access token + login data from registration for " + userCacheKey(email));
    }

    /**
     * Exchanges the registration auth code for the UNM login data, polling to absorb the propagation
     * delay between UNM and the member service.
     *
     * <p>A freshly registered account is not immediately visible to the member service, so the
     * service-ticket exchange can transiently fail for a few seconds right after registration. This
     * polls up to {@code AUTH_CODE_EXCHANGE_TIMEOUT_MS} (waiting {@code AUTH_CODE_EXCHANGE_INTERVAL_MS}
     * between attempts). A fresh service ticket is minted from the (reusable) auth code on every attempt
     * because a service ticket is single-use.
     *
     * @param authCode the auth code extracted from the registration redirect URL
     * @return the successful {@link LoginDataUnmResponse}
     * @throws NotImplementedException if login data does not become available within the timeout budget
     */
    private LoginDataUnmResponse exchangeAuthCodeForLoginData(String authCode) throws ExecutionException, InterruptedException {
        return exchangeServiceTicketWithRetry(authCode,
                serviceTicket -> postServiceTicketUNM(new POSTServiceTicketUNMApi.Inputs(serviceTicket)));
    }

    /** A service-ticket → result exchange step, retried by {@link #exchangeServiceTicketWithRetry}. */
    @FunctionalInterface
    private interface ServiceTicketExchange<R> {
        /**
         * @param serviceTicket a freshly minted, single-use service ticket
         * @return the exchanged value, or {@code null} to signal "not ready yet" (triggers a retry)
         */
        R apply(String serviceTicket) throws ExecutionException, InterruptedException;
    }

    /**
     * Mints a fresh service ticket from the (reusable) {@code authCode} and exchanges it for a result,
     * retrying on transient failures: environment errors surfaced as {@link ApiEnvironmentException}
     * (HTTP 5xx and other non-2xx) or a {@code null} result caused by UNM → member-service propagation
     * lag (the exchange returns {@code null} when the response is a 200 that is not yet {@code SUCCESS}).
     * A fresh service ticket is minted on every attempt
     * because a service ticket is single-use. Retries for up to {@link #AUTH_CODE_EXCHANGE_TIMEOUT_MS},
     * waiting {@link #AUTH_CODE_EXCHANGE_INTERVAL_MS} between attempts. Non-transient client errors (4xx,
     * e.g. invalid credentials) are not retried and propagate immediately.
     *
     * @param authCode the (reusable) auth code minted during login/registration
     * @param exchange the service-ticket → result exchange (returns {@code null} when not ready yet)
     * @return the first successful (non-{@code null}) result
     */
    private <R> R exchangeServiceTicketWithRetry(String authCode, ServiceTicketExchange<R> exchange)
            throws ExecutionException, InterruptedException {
        long deadline = System.currentTimeMillis() + AUTH_CODE_EXCHANGE_TIMEOUT_MS;
        int attempt = 0;
        ApiEnvironmentException lastTransientError = null;
        while (true) {
            attempt++;
            try {
                String serviceTicket = getServiceTicket(new GETAuthLoginUNMApi.Inputs(authCode));
                logger.log("service ticket (attempt " + attempt + ") : " + serviceTicket);
                R result = exchange.apply(serviceTicket);
                if (result != null) {
                    return result;
                }
                logger.log("Service-ticket exchange attempt " + attempt
                        + " returned no data yet (UNM -> member service propagation).");
            } catch (ApiEnvironmentException e) {
                lastTransientError = e;
                logger.log("Service-ticket exchange attempt " + attempt + " failed transiently: " + e.getMessage());
            }
            long remainingMs = deadline - System.currentTimeMillis();
            if (remainingMs <= 0) {
                break;
            }
            long sleepMs = Math.min(AUTH_CODE_EXCHANGE_INTERVAL_MS, remainingMs);
            logger.log("Retrying service-ticket exchange in " + (sleepMs / 1000)
                    + "s (attempt " + attempt + ", " + (AUTH_CODE_EXCHANGE_TIMEOUT_MS / 1000) + "s budget)");
            TimeUnit.MILLISECONDS.sleep(sleepMs);
        }
        if (lastTransientError != null) {
            throw lastTransientError;
        }
        throw new NotImplementedException("Service-ticket exchange did not return data within the "
                + (AUTH_CODE_EXCHANGE_TIMEOUT_MS / 1000) + "s budget (UNM -> member service propagation).");
    }

    /**
     * Hits the UNM registration API with the provided input payload.
     *
     * @param inputs the registration data (email, phone, password, etc.)
     * @return the register endpoint's JSON body together with its {@code Set-Cookie} headers (the freshly
     *         registered bliblitiket session cookies)
     * @throws ExecutionException if async API call fails.
     * @throws InterruptedException if the call is interrupted.
     */
    private RegisterResponse registerNewUserUNMApi(RegisterNewUserUNMApi.Inputs inputs)throws ExecutionException, InterruptedException{
        RegisterNewUserUNMApi registerNewUserUNMApi = new RegisterNewUserUNMApi(inputs);
        ApiResponse registerNewUserUNMApiResponse = registerNewUserUNMApi.hitApi(apiTools);
        JSONObject registerNewUserUNMApiResponseObject = new JSONObject(registerNewUserUNMApiResponse.getBody());

        logger.log("code verify otp methods : -> " + registerNewUserUNMApiResponseObject);
        return new RegisterResponse(registerNewUserUNMApiResponseObject,
                extractSetCookieHeaders(registerNewUserUNMApiResponse));
    }

    /**
     * Deletes a UNM account using the API based on login session data.
     * <ul>
     *   <li>Calls GET delete check API to verify if deletion is allowed.</li>
     *   <li>Submits delete request using the retrieved token.</li>
     *   <li>Asserts the deletion success based on API response.</li>
     * </ul>
     *
     * @param loginDataUnmResponse the login session object containing unmUserId and account details.
     * @throws Exception if API call fails.
     */
    @SneakyThrows
    public void deleteAccountViaApi(LoginDataUnmResponse.Data loginDataUnmResponse){
        GETDeleteCheckUserUNMApi.Inputs getDeleteCheckUserUNMApiInputs = new GETDeleteCheckUserUNMApi.Inputs(loginDataUnmResponse.unmUserId);
        GETDeleteCheckUserUNMApiResponse getDeleteCheckUserUNMApiResponse = getDeleteCheckUserUNMApi(getDeleteCheckUserUNMApiInputs,loginDataUnmResponse.unmUserId);
        if (getDeleteCheckUserUNMApiResponse!=null){
            POSTDeleteSubmitUserUNMApi.Inputs postDeleteSubmitUserUNMApiInputs = new POSTDeleteSubmitUserUNMApi.Inputs(loginDataUnmResponse.unmUserId, getDeleteCheckUserUNMApiResponse.data.token);
            boolean isAccountDeleted = submitDeleteAccount(postDeleteSubmitUserUNMApiInputs);
            Assertion.assertThat("delete account: " + loginDataUnmResponse.accountUsername +" success", isAccountDeleted, is(true));
        }
    }

    /**
     * Checks whether the user account is eligible for deletion by validating checklist items.
     *
     * @param inputs the request payload containing unmUserId.
     * @param unmId the UNM user ID for logging context.
     * @return {@link GETDeleteCheckUserUNMApiResponse} if deletion is allowed; null otherwise.
     * @throws ExecutionException if the API call fails.
     * @throws InterruptedException if the thread is interrupted during call.
     */
    private GETDeleteCheckUserUNMApiResponse getDeleteCheckUserUNMApi(GETDeleteCheckUserUNMApi.Inputs inputs,long unmId) throws ExecutionException, InterruptedException {
        GETDeleteCheckUserUNMApi getDeleteCheckUserUNMApi = new GETDeleteCheckUserUNMApi(inputs);
        ApiResponse getDeleteCheckUserUNMApiResponse = getDeleteCheckUserUNMApi.hitApi(apiTools);
        Gson gson = new Gson();
        GETDeleteCheckUserUNMApiResponse responsePOJO = gson.fromJson(getDeleteCheckUserUNMApiResponse.getBody(), GETDeleteCheckUserUNMApiResponse.class);
        if (responsePOJO.code.equalsIgnoreCase("SUCCESS")) {
            for (GETDeleteCheckUserUNMApiResponse.ChecklistItem checkListItem : responsePOJO.data.checkListItems) {
                if(checkListItem.restrictDeletion){
                    logger.warn("The account with unmId: " + unmId + " cannot be deleted because of " + checkListItem.title + " restrictDeletion : " + checkListItem.restrictDeletion);
                    return null;
                }
            }
        }
        logger.log("The account ith unmId:" + unmId + " can be deleted");
        return responsePOJO;
    }

    /**
     * Submits the delete account request using the deletion token.
     *
     * @param inputs request body with unmUserId and deletion token.
     * @return true if account deletion is successful; false otherwise.
     * @throws ExecutionException if API call fails.
     * @throws InterruptedException if thread is interrupted.
     */
    private boolean submitDeleteAccount(POSTDeleteSubmitUserUNMApi.Inputs inputs) throws ExecutionException, InterruptedException {
        POSTDeleteSubmitUserUNMApi postDeleteSubmitUserUNMApi = new POSTDeleteSubmitUserUNMApi(inputs);
        ApiResponse postDeleteSubmitUserUNMApiResponse = postDeleteSubmitUserUNMApi.hitApi(apiTools);
        JSONObject postDeleteSubmitUserUNMApiObject = new JSONObject(postDeleteSubmitUserUNMApiResponse.getBody());
        logger.log("code delete account : -> " + postDeleteSubmitUserUNMApiObject.getString("code"));
        return postDeleteSubmitUserUNMApiObject.getString("code").equalsIgnoreCase("SUCCESS");
    }

    /**
     * Drops thread-local cleanup targets without calling deletion APIs. Use after the test
     * already deleted the account (e.g. via the UNM dashboard) so {@link #deleteCreatedAccountQuietly()}
     * in {@code finally} is a no-op.
     */
    public void forgetCreatedAccount() {
        accountsToDelete.remove();
    }

    /**
     * Best-effort delete of accounts created on this thread, if any. No-op when none were registered
     * or when running in production (deletion APIs would fall through to preprod). Failures are logged
     * and never thrown so cleanup cannot change the original test result.
     */
    public void deleteCreatedAccountQuietly() {
        List<AccountToDelete> accounts = List.copyOf(accountsToDelete.get());
        accountsToDelete.remove();
        for (AccountToDelete account : accounts) {
            deleteCreatedAccountQuietly(account.userId(), account.email());
        }
    }

    private void deleteCreatedAccountQuietly(long unmUserId, String email) {
        if (Configs.ENVIRONMENT == Environment.PROD || unmUserId <= 0) {
            return;
        }
        try {
            RetryUtils.retryWithExponentialBackoff(
                    3, 2000L,
                    () -> {
                        deleteCreatedAccountOrThrow(unmUserId, email);
                        return true;
                    },
                    e -> true,
                    "Delete cobrand account unmId=" + unmUserId);
        } catch (Throwable cleanupError) {
            logger.warn("Cleanup failed deleting unmId " + unmUserId + " (" + email + "): "
                    + cleanupError.getMessage());
            logger.exception(cleanupError);
        }
    }

    @SneakyThrows
    private void deleteCreatedAccountOrThrow(long unmUserId, String email) {
        GETDeleteCheckUserUNMApiResponse checkResponse = getDeleteCheckUserUNMApi(
                new GETDeleteCheckUserUNMApi.Inputs(unmUserId), unmUserId);
        if (checkResponse == null || checkResponse.data == null || checkResponse.data.token == null) {
            throw new RuntimeException("deletion check did not return a token");
        }
        boolean deleted = submitDeleteAccount(
                new POSTDeleteSubmitUserUNMApi.Inputs(unmUserId, checkResponse.data.token));
        if (!deleted) {
            throw new RuntimeException("Delete submit did not return SUCCESS");
        }
        logger.log("Deleted account unmId=" + unmUserId + " email=" + email);
    }

    /**
     * Deletes a UNM account by looking up the user with the given email (username).
     * <ul>
     *   <li>Fetches account data by email.</li>
     *   <li>Checks deletion eligibility.</li>
     *   <li>Submits the deletion request and asserts the result.</li>
     * </ul>
     *
     * @param username the email of the user whose account should be deleted.
     * @throws Exception if account retrieval or API interaction fails.
     */
    @SneakyThrows
    public void deleteAccountViaApi(String username){
        MemberApi<T> memberApi = new MemberApi<>(session);
        AccountData.Data accountData = memberApi.getAccountDataByEmail(username);
        logger.log("unmId : " + accountData.unmUserId);

        GETDeleteCheckUserUNMApi.Inputs getDeleteCheckUserUNMApiInputs = new GETDeleteCheckUserUNMApi.Inputs(accountData.unmUserId);
        GETDeleteCheckUserUNMApiResponse getDeleteCheckUserUNMApiResponse = getDeleteCheckUserUNMApi(getDeleteCheckUserUNMApiInputs,accountData.unmUserId);
        if (getDeleteCheckUserUNMApiResponse!=null){
            POSTDeleteSubmitUserUNMApi.Inputs postDeleteSubmitUserUNMApiInputs = new POSTDeleteSubmitUserUNMApi.Inputs(accountData.unmUserId, getDeleteCheckUserUNMApiResponse.data.token);
            boolean isAccountDeleted = submitDeleteAccount(postDeleteSubmitUserUNMApiInputs);
            Assertion.assertThat("delete account: " + accountData.accountUsername +" success", isAccountDeleted, is(true));
        }
    }

    @SneakyThrows
    public void addPhoneNumberViaApi(String phoneNumber, long unmUserId) {
        String normalized = phoneNumber.startsWith("+") ? phoneNumber.substring(1) : phoneNumber;
        String phoneCountryCode = "62";
        String phoneNationalNumber = normalized.startsWith("62") ? normalized.substring(2) : normalized;
        String recipientNumber = "+" + phoneCountryCode + phoneNationalNumber;

        POSTOtpGenerateUNMApi.Inputs otpGenInputs = new POSTOtpGenerateUNMApi.Inputs(recipientNumber, "VERIFY_PHONE", "WHATS_APP", Optional.empty(), unmUserId);
        String otpId = postOtpGenerateUnm(otpGenInputs);
        logger.log("OTP ID for phone verification: " + otpId);

        POSTOtpVerifyUNMApi.Inputs otpVerifyInputs = new POSTOtpVerifyUNMApi.Inputs(otpId, "VERIFY_PHONE", Optional.empty(), unmUserId);
        String passCode = postOtpVerifyUNMApi(otpVerifyInputs);
        logger.log("PassCode for phone verification: " + passCode);

        PUTV1SecurityPhoneUNMApi.Inputs putPhoneInputs = new PUTV1SecurityPhoneUNMApi.Inputs(
                phoneCountryCode, phoneNationalNumber, passCode, unmUserId
        );
        PUTV1SecurityPhoneUNMApi putPhoneApi = new PUTV1SecurityPhoneUNMApi(putPhoneInputs);
        ApiResponse response = putPhoneApi.hitApi(apiTools);
        JSONObject responseJson = new JSONObject(response.getBody());
        logger.log("Add phone response: " + responseJson);
        Assertion.assertThat("Add phone number back to account",
                responseJson.getString("code").equalsIgnoreCase("SUCCESS"), is(true));

        waitUntilPhoneAttached(unmUserId);
    }

    private void waitUntilPhoneAttached(long unmUserId) {
        RetryUtils.retryWithExponentialBackoff(
                3, 1000L,
                () -> {
                    GetProfileDetailApi.Inputs inputs = new GetProfileDetailApi.Inputs(unmUserId);
                    GetProfileDetailApi api = new GetProfileDetailApi(inputs);
                    ApiResponse resp = api.hitApi(apiTools);
                    Gson gson = new Gson();
                    GetProfileDetailApiResponse profile = gson.fromJson(resp.getBody(), GetProfileDetailApiResponse.class);
                    if (profile == null || profile.data == null
                            || profile.data.phoneNationalNumber == null
                            || profile.data.phoneNationalNumber.isEmpty()) {
                        throw new RuntimeException("Phone not yet attached to account");
                    }
                    logger.log("Phone confirmed on account: " + profile.data.phoneNationalNumber);
                    return true;
                },
                e -> e.getMessage() != null && e.getMessage().contains("Phone not yet attached"),
                "Wait for phone attachment"
        );
    }

    @SneakyThrows
    public GetProfileDetailApiResponse getProfileDetail(String username) {
        MemberApi<?> memberApi = new MemberApi<>(session);
        AccountData.Data accountData = memberApi.getAccountDataByEmail(username);
        long unmUserId = accountData.unmUserId;

        GetProfileDetailApi.Inputs inputs = new GetProfileDetailApi.Inputs(unmUserId);
        GetProfileDetailApi getProfileDetailApi = new GetProfileDetailApi(inputs);
        ApiResponse response = getProfileDetailApi.hitApi(apiTools);
        Gson gson = new Gson();
        GetProfileDetailApiResponse responsePOJO = gson.fromJson(response.getBody(), GetProfileDetailApiResponse.class);
        if (responsePOJO.code.equalsIgnoreCase("SUCCESS")) {
            return responsePOJO;
        }
        logger.fail("Failed to get profile detail");
        return null;
    }

    /**
     * Forces the given account into an <b>email not verified</b> state via the UNM admin
     * (dashboard-be) {@code update-email} endpoint, by changing the email to a fresh unique one.
     *
     * <p><b>Order matters:</b> the caller must already be logged in <i>before</i> calling this.
     * A successful login auto-verifies the email, so unverifying must happen after login and the
     * subsequent verification must be done in the same session (without re-login).
     *
     * @param accountEmail the account's current email (used to resolve the target {@code unmId})
     * @param password     the account password (required by the admin endpoint)
     * @return the new, unverified email now set on the account (to be verified via the UI)
     */
    @SneakyThrows
    public String unverifyEmailViaAdminApi(String accountEmail, String password) {
        MemberApi<T> memberApi = new MemberApi<>(session);
        AccountData.Data accountData = memberApi.getAccountDataByEmail(accountEmail);
        long unmId = accountData.unmUserId;
        logger.log("unmId for " + accountEmail + " : " + unmId);

        String newEmail = generateUniqueEmail(accountEmail);
        logger.log("Setting account email to a fresh unverified email: " + newEmail);

        PUTAdminUpdateEmailUNMApi.Inputs inputs = new PUTAdminUpdateEmailUNMApi.Inputs(unmId, newEmail, "unverify email", password);
        PUTAdminUpdateEmailUNMApi api = new PUTAdminUpdateEmailUNMApi(inputs);
        ApiResponse response = api.hitApi(apiTools);
        logger.log("admin update-email (unverify) status -> " + response.getStatusCode() + ", body -> " + response.getBody());


        inputs = new PUTAdminUpdateEmailUNMApi.Inputs(unmId, accountEmail, "unverify email", password);
        api = new PUTAdminUpdateEmailUNMApi(inputs);
        response = api.hitApi(apiTools);
        logger.log("admin update-email (unverify) status -> " + response.getStatusCode() + ", body -> " + response.getBody());

        boolean success = response.getStatusCode() >= 200 && response.getStatusCode() < 300;
        if (success && response.getBody() != null && response.getBody().trim().startsWith("{")) {
            String code = new JSONObject(response.getBody()).optString("code", "SUCCESS");
            success = code.equalsIgnoreCase("SUCCESS");
        }
        Assertion.assertThat("Unverify email via admin API success for " + accountEmail, success, is(true));
        return accountEmail;
    }

    /**
     * Builds a unique email by plus-addressing the local part of {@code baseEmail} with a
     * timestamp + random suffix, so each run produces a brand-new (and thus unverified) email
     * that still routes to the same inbox.
     */
    private String generateUniqueEmail(String baseEmail) {
        int atIndex = baseEmail.indexOf('@');
        String localPart = baseEmail.substring(0, atIndex);
        String domain = baseEmail.substring(atIndex);
        return localPart + CoreFunctions.getCurrentTime() + CoreFunctions.randomInt(4) + domain;
    }

    // ====================================================================================
    // PKCE-based UNM login flow (sibling of getUnmSessionId / unmLoginApi).
    // The legacy non-PKCE methods above are intentionally untouched — these helpers add
    // support for the newer mobile SDK flow, which:
    //   1. POSTs to /auth/login with a SHA-256 codeChallenge and deeplink ref
    //   2. (optional) runs the MFA dance, where /mfa/submit additionally carries `identity`
    //   3. POSTs to /auth/code-challenge/verify with {authCode, codeVerifier}
    //      → returns data.session.{id,accessToken,refreshToken}
    //   4. (separate utility) POSTs to /auth/refresh with sessionRefreshToken
    //      → returns the same session shape, used to mint a fresh session.id without re-login
    // The "sessionID" used as the unmSessionId on the deeplink is data.session.id
    // returned by step 3 (or step 4 on refresh).
    // ====================================================================================

    private static final String UNM_CLIENT_ID = "9dc79e3916a042abc86c2aa525bff009";
    private static final String UNM_DEEPLINK_REF = "unm" + UNM_CLIENT_ID + "://loginResult";

    /**
     * PKCE counterpart of {@link #getUnmSessionId(String, String)}: returns the
     * {@code data.session.id} that the in-app {@code AutomationDeeplinkActivity} can use
     * directly as the {@code unmSessionId} query parameter on
     * {@code tiket-internal://login?unmSessionId=<sessionId>}.
     *
     * <p>Flow:
     * <ol>
     *   <li>Generate PKCE pair (verifier + S256 challenge) via {@link PKCEImpl}.</li>
     *   <li>{@code POST /auth/login} with the challenge → {@code authCode}
     *       (or {@code MFA_REQUIRED} → MFA dance via {@link #removeMfaPkce}).</li>
     *   <li>{@code POST /auth/code-challenge/verify} with {@code authCode + codeVerifier}
     *       → {@code data.session.id} (returned).</li>
     * </ol>
     */
    @SneakyThrows
    public String getUnmSessionIdViaPKCE(String username, String password) {
        logger.log("Obtaining UNM sessionId via PKCE flow for " + username);

        PKCE pkce = new PKCEImpl();
        PKCEModel pkceModel = pkce.generateCode();
        String codeVerifier = pkceModel.codeVerifier();
        String codeChallenge = pkceModel.codeChallenge();
        logger.log("PKCE codeChallenge: " + codeChallenge);

        POSTAuthLoginPKCEUNMApi.Inputs loginInputs =
                new POSTAuthLoginPKCEUNMApi.Inputs(username, password, codeChallenge, UNM_DEEPLINK_REF);
        String authCode = getAuthCodePKCE(loginInputs, username);
        logger.log("auth code (PKCE): " + authCode);

        POSTCodeChallengeVerifyUNMApi.Inputs verifyInputs =
                new POSTCodeChallengeVerifyUNMApi.Inputs(authCode, codeVerifier);
        String sessionId = postCodeChallengeVerify(verifyInputs);
        logger.log("UNM sessionId (PKCE): " + sessionId);
        return sessionId;
    }

    /**
     * Full session POJO returned by the PKCE verify (and refresh) endpoints, in case callers
     * need {@code accessToken} / {@code refreshToken} alongside {@code sessionId}.
     *
     * <p>Returned as a record (not the legacy {@code LoginDataUnmResponse}) so additions to the
     * legacy POJO do not accidentally affect this PKCE path.
     */
    public record PkceSession(String sessionId, String accessToken, String refreshToken) {
    }

    /**
     * PKCE login that returns the entire session (id + tokens) instead of just the
     * {@code sessionId}. Useful when the caller wants to subsequently call
     * {@link #refreshUnmSession(String)} or seed cookies directly.
     */
    @SneakyThrows
    public PkceSession loginViaPKCE(String username, String password) {
        logger.log("Performing UNM PKCE login for " + username);
        PKCEModel pkceModel = new PKCEImpl().generateCode();

        POSTAuthLoginPKCEUNMApi.Inputs loginInputs = new POSTAuthLoginPKCEUNMApi.Inputs(
                username, password, pkceModel.codeChallenge(), UNM_DEEPLINK_REF);
        String authCode = getAuthCodePKCE(loginInputs, username);

        POSTCodeChallengeVerifyUNMApi.Inputs verifyInputs =
                new POSTCodeChallengeVerifyUNMApi.Inputs(authCode, pkceModel.codeVerifier());
        return postCodeChallengeVerifyFull(verifyInputs);
    }

    /**
     * Mints a fresh session via {@code POST /auth/refresh} from a previously-obtained
     * {@code sessionRefreshToken}. Cheaper than a full re-login and avoids re-doing MFA.
     *
     * @param sessionRefreshToken the {@code data.session.refreshToken} from a prior PKCE login
     * @return the refreshed session (new {@code sessionId}, {@code accessToken}, {@code refreshToken})
     */
    @SneakyThrows
    public PkceSession refreshUnmSession(String sessionRefreshToken) {
        POSTAuthRefreshUNMApi refreshApi = new POSTAuthRefreshUNMApi(
                new POSTAuthRefreshUNMApi.Inputs(sessionRefreshToken));
        ApiResponse response = refreshApi.hitApi(apiTools);
        JSONObject obj = new JSONObject(response.getBody());
        logger.log("auth refresh res -> " + obj);
        if (!"SUCCESS".equalsIgnoreCase(obj.optString("code"))) {
            logger.fail("Failed to refresh UNM session: " + obj.optString("message"));
            throw new IllegalStateException("UNM /auth/refresh did not succeed: " + obj);
        }
        return parseSessionFromVerifyOrRefresh(obj);
    }

    /**
     * Runs the PKCE login and resolves the {@code authCode}, transparently handling the
     * {@code MFA_REQUIRED} branch by delegating to {@link #removeMfaPkce}.
     */
    private String getAuthCodePKCE(POSTAuthLoginPKCEUNMApi.Inputs inputs, String identity)
            throws ExecutionException, InterruptedException {
        POSTAuthLoginPKCEUNMApi loginApi = new POSTAuthLoginPKCEUNMApi(inputs);
        ApiResponse loginResponse = loginApi.hitApi(apiTools);
        JSONObject loginObj = new JSONObject(loginResponse.getBody());
        logger.log("auth login (PKCE) res -> " + loginObj);

        String code = loginObj.optString("code");
        if (code.contains("MFA_REQUIRED")) {
            logger.log("==MFA_REQUIRED (PKCE)==");
            String redirectUrl = removeMfaPkce(loginObj.getJSONObject("data").getString("token"), identity);
            return removeExtraFromUrlToGetAuthCode(redirectUrl);
        }
        String redirectURL = loginObj.getJSONObject("data").getString("redirectUrl");
        return removeExtraFromUrlToGetAuthCode(redirectURL);
    }

    /**
     * MFA dance for the PKCE flow. Mirrors the legacy {@code removeMFA} but submits the
     * MFA challenge through {@link POSTMfaSubmitPKCEUNMApi} (which carries {@code identity})
     * so the existing MFA submit code path is untouched.
     *
     * @return the {@code redirectUrl} from {@code /mfa/submit}, which contains the {@code authCode}
     */
    @SneakyThrows
    private String removeMfaPkce(String token, String identity) {
        logger.log("==proceeding with MFA (PKCE) for " + identity + "==");

        MfaMethodsResponse mfaMethod = getMfaMethodsForPkce(token);
        logger.log("otpRecipientId: " + mfaMethod.data.otpRecipientId);
        String mfaToken = mfaMethod.data.token;
        String type = mfaMethod.data.methods.get(0).type;
        String channel = getChannelIdForPkce(type);

        POSTOtpGenerateUNMApi.Inputs otpGenInputs = new POSTOtpGenerateUNMApi.Inputs(
                mfaMethod.data.otpRecipientId, "LOGIN_OTP", channel);
        String otpId = postOtpGenerateUnmForPkce(otpGenInputs);
        logger.log("otpId: " + otpId);

        POSTOtpVerifyUNMApi.Inputs otpVerifyInputs = new POSTOtpVerifyUNMApi.Inputs(otpId, "LOGIN_OTP");
        String passCode = postOtpVerifyUNMApiForPkce(otpVerifyInputs);
        logger.log("passCode: " + passCode);

        POSTMfaSubmitPKCEUNMApi.Inputs mfaSubmitInputs =
                new POSTMfaSubmitPKCEUNMApi.Inputs(passCode, mfaToken, type, identity);
        return postMfaSubmitPkce(mfaSubmitInputs);
    }

    private MfaMethodsResponse getMfaMethodsForPkce(String token)
            throws ExecutionException, InterruptedException {
        GETMfaMethodsUNMApi mfaApi = new GETMfaMethodsUNMApi(new GETMfaMethodsUNMApi.Inputs(token));
        ApiResponse response = mfaApi.hitApi(apiTools);
        logger.log("mfa methods (PKCE) res -> " + response.getBody());
        return new Gson().fromJson(response.getBody(), MfaMethodsResponse.class);
    }

    private String postOtpGenerateUnmForPkce(POSTOtpGenerateUNMApi.Inputs inputs)
            throws ExecutionException, InterruptedException {
        POSTOtpGenerateUNMApi api = new POSTOtpGenerateUNMApi(inputs);
        JSONObject obj = new JSONObject(api.hitApi(apiTools).getBody());
        logger.log("otp generate (PKCE) res -> " + obj.optString("code"));
        return obj.getJSONObject("data").getString("otpId");
    }

    private String postOtpVerifyUNMApiForPkce(POSTOtpVerifyUNMApi.Inputs inputs)
            throws ExecutionException, InterruptedException {
        POSTOtpVerifyUNMApi api = new POSTOtpVerifyUNMApi(inputs);
        JSONObject obj = new JSONObject(api.hitApi(apiTools).getBody());
        logger.log("otp verify (PKCE) res -> " + obj.optString("code"));
        return obj.getJSONObject("data").getString("passCode");
    }

    private String postMfaSubmitPkce(POSTMfaSubmitPKCEUNMApi.Inputs inputs)
            throws ExecutionException, InterruptedException {
        POSTMfaSubmitPKCEUNMApi api = new POSTMfaSubmitPKCEUNMApi(inputs);
        JSONObject obj = new JSONObject(api.hitApi(apiTools).getBody());
        logger.log("mfa submit (PKCE) res -> " + obj);
        return obj.getJSONObject("data").getString("redirectUrl");
    }

    /**
     * Calls {@code /auth/code-challenge/verify} and returns just {@code data.session.id}.
     * Most callers want only that — see {@link #postCodeChallengeVerifyFull} for full session.
     */
    private String postCodeChallengeVerify(POSTCodeChallengeVerifyUNMApi.Inputs inputs)
            throws ExecutionException, InterruptedException {
        return postCodeChallengeVerifyFull(inputs).sessionId();
    }

    private PkceSession postCodeChallengeVerifyFull(POSTCodeChallengeVerifyUNMApi.Inputs inputs)
            throws ExecutionException, InterruptedException {
        POSTCodeChallengeVerifyUNMApi api = new POSTCodeChallengeVerifyUNMApi(inputs);
        ApiResponse response = api.hitApi(apiTools);
        JSONObject obj = new JSONObject(response.getBody());
        logger.log("code-challenge verify res -> " + obj);
        if (!"SUCCESS".equalsIgnoreCase(obj.optString("code"))) {
            logger.fail("Failed to verify PKCE code challenge: " + obj.optString("message"));
            throw new IllegalStateException("UNM /auth/code-challenge/verify did not succeed: " + obj);
        }
        return parseSessionFromVerifyOrRefresh(obj);
    }

    /**
     * Extracts the {@code data.session.{id,accessToken,refreshToken}} block returned by both
     * {@code /auth/code-challenge/verify} and {@code /auth/refresh}.
     */
    private static PkceSession parseSessionFromVerifyOrRefresh(JSONObject responseObj) {
        JSONObject session = responseObj.getJSONObject("data").getJSONObject("session");
        return new PkceSession(
                session.getString("id"),
                session.optString("accessToken", null),
                session.optString("refreshToken", null));
    }

    /**
     * PKCE-flow channel mapping. Mirrors {@link #getChannelId} but is kept private to this
     * section so changes to legacy MFA channel mapping cannot regress the PKCE path.
     */
    private String getChannelIdForPkce(String type) {
        return switch (type) {
            case "WA_OTP" -> "WHATS_APP";
            case "SMS_OTP" -> "SMS";
            case "EMAIL_OTP" -> "EMAIL";
            default -> throw new NotImplementedException("The MFA type " + type + " is not implemented (PKCE flow)");
        };
    }
}
