package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.extranet.login.POSTExtranetAdminLoginApi;
import com.tiket.api.extranet.login.POSTExtranetAdminLoginMfaApplyApi;
import com.tiket.api.extranet.login.POSTExtranetAdminLoginMfaGenerateApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.extranet.login.ExtranetAdminLoginMfaApplyResponse;
import com.tiket.pojo.extranet.login.ExtranetAdminLoginMfaGenerateResponse;
import com.tiket.pojo.extranet.login.ExtranetAdminLoginResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.ConcurrentHashMap;

import static org.hamcrest.Matchers.is;

public class ExtranetHotelAuthApi <T extends WebDriver> extends BasePage<T> {
    private static final ConcurrentHashMap<String, ExtranetAdminLoginMfaApplyResponse> loginDataCache = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    public ExtranetHotelAuthApi(Session<T> session) {
        super(session);
    }

    /**
     * Logs into the Extranet Hotel Admin system using the provided username and password.
     * <ul>
     *     <li>Submits credentials to get access token or MFA requirement.</li>
     *     <li>If MFA is required, generates OTP and applies it to complete login.</li>
     *     <li>Returns the complete login data including access token and user information.</li>
     * </ul>
     *
     * @param username the admin account email or username
     * @param password the corresponding password
     * @return {@link ExtranetAdminLoginMfaApplyResponse.Data} if login is successful; otherwise returns {@code null}
     * @throws Exception if any step in the login flow fails
     */
    @SneakyThrows
    public ExtranetAdminLoginMfaApplyResponse.Data extranetHotelLoginApi(String username, String password) {
        String sessionId = session.sessionId();
        // Generate consistent deviceId for the entire login flow
        String deviceId = CoreFunctions.generateDeviceId();
        logger.log("Generated deviceId: " + deviceId);

        String cacheKey = sessionId + ":" + username + ":" + deviceId;
        ExtranetAdminLoginMfaApplyResponse cachedLoginData = loginDataCache.get(cacheKey);
        
        if (cachedLoginData != null && cachedLoginData.data != null) {
            logger.log("Returning cached ExtranetAdminLoginMfaApplyResponse for " + cacheKey);
            return cachedLoginData.data;
        }
        
        logger.log("Logging in to extranet with email: " + username);
        
        // Step 1: Initial login
        ExtranetAdminLoginResponse loginResponse = performInitialLogin(username, password, deviceId);
        if (loginResponse == null) {
            return null;
        }
        
        // Check if login succeeded without MFA
        if (loginResponse.isSuccess()) {
            ExtranetAdminLoginMfaApplyResponse finalResponse = convertLoginResponseToMfaApplyResponse(loginResponse);
            loginDataCache.put(cacheKey, finalResponse);
            Assertion.assertThat("login via API success", finalResponse.data.accountUsername.toLowerCase(), is(username.toLowerCase()));
            return finalResponse.data;
        }
        
        // Check if MFA is required
        if (!loginResponse.isMfaRequired()) {
            logger.fail("Login failed with code: " + loginResponse.code + ", message: " + loginResponse.message);
            return null;
        }
        
        // Step 2: Generate OTP
        ExtranetAdminLoginMfaGenerateResponse mfaGenerateResponse = performMfaGenerate(loginResponse, deviceId);
        if (mfaGenerateResponse == null) {
            return null;
        }
        
        // Step 3: Apply OTP
        ExtranetAdminLoginMfaApplyResponse mfaApplyResponse = performMfaApply(loginResponse, mfaGenerateResponse, deviceId);
        if (mfaApplyResponse == null) {
            return null;
        }
        
        logger.log("MFA OTP applied successfully");
        logger.log("Access token: " + mfaApplyResponse.data.accessToken);
        
        loginDataCache.put(cacheKey, mfaApplyResponse);
        Assertion.assertThat("login via API success", mfaApplyResponse.data.accountUsername.toLowerCase(), is(username.toLowerCase()));
        return mfaApplyResponse.data;
    }

    /**
     * Retrieves the access token for the Extranet Hotel Admin with "Bearer " prefix.
     * <p>This method performs the complete login flow including MFA if required,
     * and returns the access token formatted for Authorization headers.</p>
     *
     * <p>The returned token is prefixed with "Bearer " and can be directly used
     * in HTTP Authorization headers.</p>
     *
     * <p><b>Example usage:</b></p>
     * <pre>
     * String authToken = extranetHotelAuthApi.getAccessToken("user@example.com", "password");
     * // Returns: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     * headers.put("Authorization", authToken);
     * </pre>
     *
     * @param username the admin account email or username
     * @param password the corresponding password
     * @return the access token with "Bearer " prefix if login is successful;
     *         {@code null} if login fails at any step
     * @throws Exception if any step in the login flow fails
     * @see #extranetHotelLoginApi(String, String) for getting complete login data
     */
    @SneakyThrows
    public ExtranetAdminLoginMfaApplyResponse.Data getAccessToken(String username, String password){
        ExtranetAdminLoginMfaApplyResponse.Data loginData = extranetHotelLoginApi(username, password);
        
        if (loginData == null || loginData.accessToken == null) {
            logger.fail("Failed to get access token for user: " + username);
            return null;
        }

        if (!loginData.accessToken.startsWith("Bearer ")) {
            loginData.accessToken = "Bearer " + loginData.accessToken;
        }
        logger.log("Retrieved access token for user: " + username + " -> " + loginData.accessToken);
        logger.log("Retrieved account ID for user: " + username + " -> " + loginData.accountId);
        return loginData;
    }
    
    /**
     * Performs the initial admin login
     *
     * @param username the admin account email or username
     * @param password the corresponding password
     * @param deviceId the device identifier for consistency
     * @return {@link ExtranetAdminLoginResponse} if successful; otherwise returns {@code null}
     */
    private ExtranetAdminLoginResponse performInitialLogin(String username, String password, String deviceId) {
        POSTExtranetAdminLoginApi.Inputs loginInputs = new POSTExtranetAdminLoginApi.Inputs(username, password, deviceId);
        ExtranetAdminLoginResponse loginResponse = postExtranetAdminLogin(loginInputs);
        
        if (loginResponse == null) {
            logger.fail("Failed to get login response");
            return null;
        }
        
        logger.log("Login response code: " + loginResponse.code);
        return loginResponse;
    }
    
    /**
     * Performs MFA OTP generation
     *
     * @param loginResponse the initial login response containing MFA transaction ID
     * @param deviceId the device identifier for consistency
     * @return {@link ExtranetAdminLoginMfaGenerateResponse} if successful; otherwise returns {@code null}
     */
    private ExtranetAdminLoginMfaGenerateResponse performMfaGenerate(ExtranetAdminLoginResponse loginResponse, String deviceId) {
        logger.log("MFA required for this account");
        logger.log("MFA Transaction ID: " + loginResponse.data.mfaTrxId);
        
        String mfaTrxId = loginResponse.data.mfaTrxId;
        String aatToken = null; // Can be extracted from cookies if needed
        String artToken = null; // Can be extracted from cookies if needed
        long accountId = 0; // Default value, will be updated from response
        
        POSTExtranetAdminLoginMfaGenerateApi.Inputs mfaGenerateInputs = 
            new POSTExtranetAdminLoginMfaGenerateApi.Inputs(mfaTrxId, aatToken, artToken, accountId, null, deviceId);
        ExtranetAdminLoginMfaGenerateResponse mfaGenerateResponse = postExtranetAdminLoginMfaGenerate(mfaGenerateInputs);
        
        if (mfaGenerateResponse == null || !mfaGenerateResponse.isSuccess()) {
            logger.fail("Failed to generate MFA OTP");
            return null;
        }
        
        logger.log("MFA OTP generated successfully");
        logger.log("OTP Transaction ID: " + mfaGenerateResponse.data.otp.trxId);
        logger.log("Masked account: " + mfaGenerateResponse.data.otp.maskedAccount);
        
        return mfaGenerateResponse;
    }
    
    /**
     * Performs MFA OTP application to complete login
     *
     * @param loginResponse the initial login response containing MFA transaction ID
     * @param mfaGenerateResponse the MFA generate response containing OTP transaction ID
     * @param deviceId the device identifier for consistency
     * @return {@link ExtranetAdminLoginMfaApplyResponse} if successful; otherwise returns {@code null}
     */
    private ExtranetAdminLoginMfaApplyResponse performMfaApply(
            ExtranetAdminLoginResponse loginResponse,
            ExtranetAdminLoginMfaGenerateResponse mfaGenerateResponse,
            String deviceId) {
        
        String mfaTrxId = loginResponse.data.mfaTrxId;
        String otpTrxId = mfaGenerateResponse.data.otp.trxId;
        String aatToken = null; // Can be extracted from cookies if needed
        String artToken = null; // Can be extracted from cookies if needed
        long accountId = 0; // Default value, will be updated from response
        
        POSTExtranetAdminLoginMfaApplyApi.Inputs mfaApplyInputs = 
            new POSTExtranetAdminLoginMfaApplyApi.Inputs(mfaTrxId, otpTrxId, aatToken, artToken, accountId, null, deviceId);
        ExtranetAdminLoginMfaApplyResponse mfaApplyResponse = postExtranetAdminLoginMfaApply(mfaApplyInputs);
        
        if (mfaApplyResponse == null || !mfaApplyResponse.isSuccess()) {
            logger.fail("Failed to apply MFA OTP");
            return null;
        }
        
        return mfaApplyResponse;
    }
    
    /**
     * Converts ExtranetAdminLoginResponse to ExtranetAdminLoginMfaApplyResponse format for consistency
     *
     * @param loginResponse the login response to convert
     * @return {@link ExtranetAdminLoginMfaApplyResponse} with data from login response
     */
    private ExtranetAdminLoginMfaApplyResponse convertLoginResponseToMfaApplyResponse(ExtranetAdminLoginResponse loginResponse) {
        logger.log("Login successful without MFA");
        logger.log("Access token: " + loginResponse.data.accessToken);
        
        ExtranetAdminLoginMfaApplyResponse finalResponse = new ExtranetAdminLoginMfaApplyResponse();
        finalResponse.code = loginResponse.code;
        finalResponse.message = loginResponse.message;
        finalResponse.serverTime = loginResponse.serverTime;
        finalResponse.data = new ExtranetAdminLoginMfaApplyResponse.Data();
        finalResponse.data.accessToken = loginResponse.data.accessToken;
        finalResponse.data.refreshToken = loginResponse.data.refreshToken;
        finalResponse.data.accountId = loginResponse.data.accountId;
        finalResponse.data.accountUsername = loginResponse.data.accountUsername;
        finalResponse.data.accountFirstName = loginResponse.data.accountFirstName;
        finalResponse.data.accountLastName = loginResponse.data.accountLastName;
        finalResponse.data.accountPhoneCode = loginResponse.data.accountPhoneCode;
        finalResponse.data.accountPhoneNumber = loginResponse.data.accountPhoneNumber;
        finalResponse.data.currency = loginResponse.data.currency;
        finalResponse.data.lang = loginResponse.data.lang;
        finalResponse.data.uniqueId = loginResponse.data.uniqueId;
        finalResponse.data.verifiedEmail = loginResponse.data.verifiedEmail;
        finalResponse.data.verifiedPhoneNumber = loginResponse.data.verifiedPhoneNumber;
        
        return finalResponse;
    }
    
    /**
     * Executes the initial admin login API call
     */
    private ExtranetAdminLoginResponse postExtranetAdminLogin(POSTExtranetAdminLoginApi.Inputs inputs) {
        try {
            POSTExtranetAdminLoginApi api = new POSTExtranetAdminLoginApi(inputs);
            ApiResponse response = api.hitApi(apiTools);
            logger.log("Admin login API response: " + response.getBody());
            return gson.fromJson(response.getBody(), ExtranetAdminLoginResponse.class);
        } catch (Exception e) {
            logger.fail("Error calling admin login API: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Executes the MFA OTP generation API call
     */
    private ExtranetAdminLoginMfaGenerateResponse postExtranetAdminLoginMfaGenerate(POSTExtranetAdminLoginMfaGenerateApi.Inputs inputs) {
        try {
            POSTExtranetAdminLoginMfaGenerateApi api = new POSTExtranetAdminLoginMfaGenerateApi(inputs);
            ApiResponse response = api.hitApi(apiTools);
            logger.log("MFA generate API response: " + response.getBody());
            return gson.fromJson(response.getBody(), ExtranetAdminLoginMfaGenerateResponse.class);
        } catch (Exception e) {
            logger.fail("Error calling MFA generate API: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Executes the MFA OTP apply API call
     */
    private ExtranetAdminLoginMfaApplyResponse postExtranetAdminLoginMfaApply(POSTExtranetAdminLoginMfaApplyApi.Inputs inputs) {
        try {
            POSTExtranetAdminLoginMfaApplyApi api = new POSTExtranetAdminLoginMfaApplyApi(inputs);
            ApiResponse response = api.hitApi(apiTools);
            logger.log("MFA apply API response: " + response.getBody());
            return gson.fromJson(response.getBody(), ExtranetAdminLoginMfaApplyResponse.class);
        } catch (Exception e) {
            logger.fail("Error calling MFA apply API: " + e.getMessage());
            return null;
        }
    }
}

