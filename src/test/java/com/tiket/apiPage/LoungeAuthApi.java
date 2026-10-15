package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.auth.AdminDashboardLoginResponseParser;
import com.tiket.api.auth.AdminDashboardLoginResponseParser.ParsedCookie;
import com.tiket.api.auth.POSTAdminLoginApi;
import com.tiket.api.auth.POSTAdminLoginMfaApplyApi;
import com.tiket.api.auth.POSTAdminLoginMfaGenerateApi;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.auth.AdminLoginResponse;
import com.tiket.pojo.extranet.login.ExtranetAdminLoginMfaApplyResponse;
import com.tiket.pojo.extranet.login.ExtranetAdminLoginMfaGenerateResponse;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * Centralized (member-core) login for the Lounge dashboard, mirroring {@link MmDashboardApi}. It hits
 * {@code POST /tix-member-core/v3/admin/login} with the Lounge business/dashboard identifiers and transparently
 * completes the MFA challenge (generate + apply the default {@code 1234} OTP) when the device is not yet trusted,
 * returning the full set of session cookies that authenticate {@code reward*.tiket.com}.
 *
 * <p>The device id is fixed to {@code new-device-id} so it matches the value hardcoded by
 * {@link POSTAdminLoginMfaGenerateApi}/{@link POSTAdminLoginMfaApplyApi}. Once the first login for an account
 * clears MFA on that device, member-core marks the device trusted and subsequent logins return the session
 * directly (no OTP), which is what makes this reusable as a fast login for the Lounge suite.
 */
public class LoungeAuthApi<T extends WebDriver> extends BasePage<T> {

    private static final String ADMIN_TYPE = "EXTERNAL";
    private static final String BUSINESS_TYPE = "lounge";
    private static final String DASHBOARD_ID = "lounge";
    private static final String DEVICE_ID = "new-device-id";
    private static final String MFA_METHOD = "EMAIL";
    private static final String DEFAULT_OTP = "1234";
    private static final long DEFAULT_TTL_MILLIS = 6L * 60 * 60 * 1000;
    private static final long EXPIRY_SKEW_MILLIS = 60_000L;

    public LoungeAuthApi(Session<T> session) {
        super(session);
    }

    /** Full set of session cookies for the Lounge dashboard, valid until {@link #expiresAtMillis()}. */
    public record LoungeSession(List<ParsedCookie> cookies, long accountId, long expiresAtMillis) {

        public boolean isUsable() {
            return cookies != null && cookies.stream()
                    .anyMatch(c -> "aat".equalsIgnoreCase(c.name()) || "session_access_token".equalsIgnoreCase(c.name()));
        }

        public boolean isExpired() {
            return System.currentTimeMillis() >= expiresAtMillis;
        }
    }

    /**
     * Authenticates {@code email}/{@code password} against member-core and returns the resulting session cookies,
     * completing the MFA challenge automatically when required.
     *
     * @throws RuntimeException when the credentials are rejected or no {@code aat} session cookie is returned
     */
    public LoungeSession login(String email, String password) {
        POSTAdminLoginApi.Inputs inputs =
                new POSTAdminLoginApi.Inputs(email, password, ADMIN_TYPE, BUSINESS_TYPE, DASHBOARD_ID, DEVICE_ID);
        ApiResponse response = new POSTAdminLoginApi(inputs).hitApi(apiTools);

        AdminLoginResponse login = new Gson().fromJson(response.getBody(), AdminLoginResponse.class);
        if (login == null) {
            throw new RuntimeException("Lounge login returned an unparseable response: " + response.getBody());
        }
        if (login.isMfaRequired()) {
            return completeMfa(login);
        }
        if (login.isSuccess() && login.data != null
                && login.data.accessToken != null && !login.data.accessToken.isBlank()) {
            long accountId = login.data.accountId != null ? login.data.accountId : 0L;
            return buildSession(response, accountId);
        }
        throw new RuntimeException("Lounge login failed for " + email + ": " + response.getBody());
    }

    private LoungeSession completeMfa(AdminLoginResponse login) {
        long accountId = login.data != null && login.data.accountId != null ? login.data.accountId : 0L;

        POSTAdminLoginMfaGenerateApi.Inputs generateInputs =
                new POSTAdminLoginMfaGenerateApi.Inputs(login.data.mfaTrxId, MFA_METHOD, null, null, accountId);
        ApiResponse generateResponse = new POSTAdminLoginMfaGenerateApi(generateInputs).hitApi(apiTools);

        ExtranetAdminLoginMfaGenerateResponse generate =
                new Gson().fromJson(generateResponse.getBody(), ExtranetAdminLoginMfaGenerateResponse.class);
        if (generate == null || !generate.isSuccess() || generate.data == null || generate.data.otp == null
                || generate.data.otp.trxId == null || generate.data.otp.trxId.isBlank()) {
            throw new RuntimeException("Lounge MFA generate failed: " + generateResponse.getBody());
        }

        POSTAdminLoginMfaApplyApi.Inputs applyInputs = new POSTAdminLoginMfaApplyApi.Inputs(
                login.data.mfaTrxId, MFA_METHOD, generate.data.otp.trxId, DEFAULT_OTP, null, null, accountId);
        ApiResponse applyResponse = new POSTAdminLoginMfaApplyApi(applyInputs).hitApi(apiTools);

        ExtranetAdminLoginMfaApplyResponse apply =
                new Gson().fromJson(applyResponse.getBody(), ExtranetAdminLoginMfaApplyResponse.class);
        if (apply == null || !apply.isSuccess() || apply.data == null
                || apply.data.accessToken == null || apply.data.accessToken.isBlank()) {
            throw new RuntimeException("Lounge MFA apply failed: " + applyResponse.getBody());
        }
        long resolvedAccountId = apply.data.accountId != null ? apply.data.accountId : accountId;
        return buildSession(applyResponse, resolvedAccountId);
    }

    private LoungeSession buildSession(ApiResponse response, long accountId) {
        List<ParsedCookie> cookies = AdminDashboardLoginResponseParser.parseSetCookies(response, 0L);
        boolean hasAuthCookie = cookies.stream()
                .anyMatch(c -> "aat".equalsIgnoreCase(c.name()) || "session_access_token".equalsIgnoreCase(c.name()));
        if (!hasAuthCookie) {
            throw new RuntimeException(
                    "Lounge login succeeded but no session cookie (aat/session_access_token) was returned");
        }
        long expiresAt = System.currentTimeMillis() + resolveTtlMillis(cookies) - EXPIRY_SKEW_MILLIS;
        return new LoungeSession(cookies, accountId, expiresAt);
    }

    /** Derives the cache lifetime from the {@code aat} (or {@code session_access_token}) {@code Max-Age}. */
    private long resolveTtlMillis(List<ParsedCookie> cookies) {
        long aatTtl = -1L;
        long sessionTtl = -1L;
        for (ParsedCookie cookie : cookies) {
            if ("aat".equalsIgnoreCase(cookie.name())) {
                aatTtl = cookie.maxAgeMillis();
            } else if ("session_access_token".equalsIgnoreCase(cookie.name())) {
                sessionTtl = cookie.maxAgeMillis();
            }
        }
        if (aatTtl > 0) {
            return aatTtl;
        }
        if (sessionTtl > 0) {
            return sessionTtl;
        }
        return DEFAULT_TTL_MILLIS;
    }
}
