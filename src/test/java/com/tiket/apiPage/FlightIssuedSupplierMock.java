package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.flight.dashboard.FlightIntegratorConfigApi;
import com.tiket.api.flight.dashboard.PutIntegratorConfigV2Api;
import com.tiket.model.Environment;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.flight.dashboard.IntegratorConfigResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Points a supplier's ISSUED integrator configs at a stub that answers success, for as long as an
 * order needs them, then puts them back. The order reaches ISSUED on its own, so no ticket number
 * has to be written by hand.
 * <p>
 * {@link #hold(Collection)} and {@link #release()} come in a pair — release belongs in a
 * {@code finally}, or the supplier is left issuing tickets no airline sold.
 * </p>
 */
public class FlightIssuedSupplierMock<T extends WebDriver> extends BasePage<T> {

    /**
     * The "Dynamic Auto Issuance" stub on the flight mock server, matched exactly — a trailing
     * slash or a query string does not reach it. Each environment reaches its own mock server.
     */
    private static final String MOCK_SUCCESS_ISSUED_URL_PREPROD =
            "http://10.6.2.20:7228/auto-issuance-dynamic/issued";

    private static final String MOCK_SUCCESS_ISSUED_URL_GK =
            "http://192.168.48.191:7228/auto-issuance-dynamic/issued";

    public static String mockSuccessIssuedUrl() {
        return Configs.ENVIRONMENT == Environment.GK
                ? MOCK_SUCCESS_ISSUED_URL_GK
                : MOCK_SUCCESS_ISSUED_URL_PREPROD;
    }

    private static final String ACTION_ISSUED = "ISSUED";

    /** A supplier can hold one ISSUED config per distribution type, and all of them are pointed. */
    private static final String CONFIG_PAGE_SIZE = "50";

    /** The write goes through the dashboard gateway while the read-back reads rule discovery. */
    private static final int WRITE_ATTEMPTS = 3;
    private static final long WRITE_RETRY_DELAY_MS = 2_000L;

    /** Configs pointed at the stub by this JVM, keyed by config id, with the URL to put back. */
    private static final Map<String, Lease> LEASES = new ConcurrentHashMap<>();

    private static final Map<String, Object> LOCKS = new ConcurrentHashMap<>();

    private final List<Config> held = new ArrayList<>();

    private String cookieHeader;

    public FlightIssuedSupplierMock(Session<T> session) {
        super(session);
    }

    /**
     * @param supplierCodes the suppliers whose ISSUED configs may be pointed at the stub
     */
    public void hold(Collection<String> supplierCodes) {
        try {
            for (String supplierCode : supplierCodes) {
                for (Config config : readIssuedConfigs(supplierCode)) {
                    acquire(config);
                }
            }
        } catch (RuntimeException | Error e) {
            // Error too: a failed assertion on the third supplier must still put back the first two.
            release();
            throw e;
        }
    }

    /** Puts back every config this hold is the last to depend on. Safe when nothing was held. */
    public void release() {
        for (Config config : held) {
            releaseOne(config);
        }
        held.clear();
    }

    public List<String> heldConfigIds() {
        return held.stream().map(Config::id).toList();
    }

    private void acquire(Config config) {
        synchronized (lockFor(config.id())) {
            Lease lease = LEASES.get(config.id());
            if (lease != null) {
                lease.holders++;
                held.add(config);
                logger.log("Supplier " + config.supplierCode() + " is already on the mock for another "
                        + "order on config " + config.id() + ", now held by " + lease.holders);
                return;
            }

            Config current = readConfig(config.supplierCode(), config.id());
            if (current == null) {
                logger.log("Config " + config.id() + " of supplier " + config.supplierCode()
                        + " is no longer reported, so it is passed over");
                return;
            }

            if (mockSuccessIssuedUrl().equals(current.url())) {
                // No lease taken: a config already on the stub cannot be told apart from one this
                // class set, so restoring it would mean writing a URL that was never read.
                logger.log("Supplier " + config.supplierCode() + " already issues through the mock on "
                        + "config " + config.id() + ", so it is left as it is");
                return;
            }

            if (!setUrl(current, mockSuccessIssuedUrl())) {
                // The write may have landed unconfirmed, so put it back rather than leave the config
                // on the stub with no lease recording it.
                if (!setUrl(current, current.url())) {
                    logger.warn("MANUAL FIX NEEDED — config " + config.id() + " of supplier "
                            + config.supplierCode() + " could not be confirmed back on " + current.url()
                            + " after failing to be pointed at the issuance mock. Set its url to "
                            + current.url() + " before anything books this supplier.");
                }
                Assertion.fail("Config " + config.id() + " of supplier " + config.supplierCode()
                        + " could not be pointed at the issuance mock and confirmed there, so booking "
                        + "would risk real issuance.");
            }

            LEASES.put(config.id(), new Lease(current.url()));
            held.add(current);
            logger.log("Supplier " + config.supplierCode() + " now issues through the mock on config "
                    + config.id() + " and is held by 1 order. It will be put back to " + current.url());
        }
    }

    private void releaseOne(Config config) {
        synchronized (lockFor(config.id())) {
            Lease lease = LEASES.get(config.id());
            if (lease == null) {
                return;
            }
            lease.holders--;
            if (lease.holders > 0) {
                logger.log("Supplier " + config.supplierCode() + " stays on the mock: " + lease.holders
                        + " other order(s) still depend on config " + config.id());
                return;
            }

            if (setUrl(config, lease.originalUrl)) {
                LEASES.remove(config.id());
                return;
            }
            // Lease kept, so the next order that needs this config joins it and retries the restore.
            logger.warn("MANUAL FIX NEEDED — config " + config.id() + " of supplier "
                    + config.supplierCode() + " is still pointing at the issuance mock after "
                    + WRITE_ATTEMPTS + " attempts to put it back. Set its url to " + lease.originalUrl
                    + " or this supplier will keep issuing tickets no airline ever sold.");
        }
    }

    /** Points the config at {@code url} and confirms the config endpoint reports it. */
    private boolean setUrl(Config config, String url) {
        for (int attempt = 1; attempt <= WRITE_ATTEMPTS; attempt++) {
            if (writeUrl(config, url) && reportsUrl(config, url)) {
                return true;
            }
            logger.warn("Setting config " + config.id() + " to " + url + " did not take on attempt "
                    + attempt + " of " + WRITE_ATTEMPTS);
            if (attempt < WRITE_ATTEMPTS) {
                try {
                    Thread.sleep(WRITE_RETRY_DELAY_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return false;
    }

    private boolean reportsUrl(Config config, String url) {
        Config current = readConfig(config.supplierCode(), config.id());
        if (current == null) {
            logger.warn("Config " + config.id() + " of supplier " + config.supplierCode()
                    + " is no longer reported, so its URL cannot be confirmed");
            return false;
        }
        if (!url.equals(current.url())) {
            logger.warn("Config " + config.id() + " reports " + current.url() + " rather than " + url);
            return false;
        }
        return true;
    }

    private boolean writeUrl(Config config, String url) {
        logger.log((mockSuccessIssuedUrl().equals(url)
                ? "Pointing supplier " + config.supplierCode() + " at "
                : "Restoring supplier " + config.supplierCode() + " to ") + url);

        PutIntegratorConfigV2Api api = new PutIntegratorConfigV2Api(
                new PutIntegratorConfigV2Api.Inputs(adminCookieHeader(), "", config.id(),
                        config.action(), config.distributionType(), config.supplierCode(), "", url));
        ApiResponse response = api.hitApi(apiTools);
        String code = new JSONObject(response.getBody()).optString("code");
        if (!"SUCCESS".equalsIgnoreCase(code)) {
            logger.warn("Setting config " + config.id() + " to " + url + " answered " + code + ": "
                    + response.getBody());
            return false;
        }
        return true;
    }

    private Config readConfig(String supplierCode, String configId) {
        return readIssuedConfigs(supplierCode).stream()
                .filter(candidate -> configId.equals(candidate.id()))
                .findFirst()
                .orElse(null);
    }

    private List<Config> readIssuedConfigs(String supplierCode) {
        ApiResponse response = new FlightIntegratorConfigApi(
                new FlightIntegratorConfigApi.Inputs(supplierCode, CONFIG_PAGE_SIZE)).hitApi(apiTools);
        IntegratorConfigResponse parsed =
                new Gson().fromJson(response.getBody(), IntegratorConfigResponse.class);

        if (parsed == null || !"SUCCESS".equals(parsed.code) || parsed.data == null
                || parsed.data.content == null || parsed.data.content.isEmpty()) {
            logger.log("Supplier " + supplierCode + " has no ISSUED integrator config, so it is "
                    + "passed over");
            return List.of();
        }

        List<Config> configs = new ArrayList<>();
        for (IntegratorConfigResponse.Content row : parsed.data.content) {
            if (isBlank(row.id) || isBlank(row.url)) {
                logger.log("An ISSUED integrator config of supplier " + supplierCode + " states no url "
                        + "or id, so it is passed over");
                continue;
            }
            if (isBlank(row.distributionType)) {
                // The update call sends action, distributionType, supplierCode and url, so a field
                // that cannot be read cannot be written back as it was.
                Assertion.fail("Integrator config " + row.id + " of supplier " + supplierCode
                        + " states no distributionType, so writing it back would blank that field on a "
                        + "shared config.");
            }
            configs.add(new Config(row.id, supplierCode, row.url,
                    isBlank(row.action) ? ACTION_ISSUED : row.action, row.distributionType));
            logger.log("Supplier " + supplierCode + " issues through " + row.url + " for "
                    + row.distributionType);
        }
        return configs;
    }

    private String adminCookieHeader() {
        if (cookieHeader == null) {
            cookieHeader = new MemberApi<>(session).getAdminDashboardCookieHeader();
        }
        return cookieHeader;
    }

    private static Object lockFor(String configId) {
        return LOCKS.computeIfAbsent(configId, id -> new Object());
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private record Config(String id, String supplierCode, String url, String action,
                          String distributionType) {
    }

    /** Guarded by the config's entry in {@link #LOCKS}, so {@code holders} needs no lock of its own. */
    private static final class Lease {
        private final String originalUrl;
        private int holders = 1;

        private Lease(String originalUrl) {
            this.originalUrl = originalUrl;
        }
    }
}
