package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.gamification.SnatchTreasureApi;
import com.tiket.api.loyalty.ActiveTiketPointsApi;
import com.tiket.api.loyalty.AddActivePointsApi;
import com.tiket.model.Exception.ApiToolsException;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.loyalty.ActiveTiketPointsResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.api.gamification.DeleteAdminProductTokenApi;
import com.tiket.api.gamification.GetAdminProductTokenApi;
import org.json.JSONArray;
import org.json.JSONObject;

import org.openqa.selenium.WebDriver;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;

public class GamificationApi<T extends WebDriver> extends BasePage<T> {

    private static final int POINTS_READ_ATTEMPTS = 3;
    private static final int SNATCH_ATTEMPTS = 3;

    /**
     * The pause before a retry, multiplied by the attempt so it lengthens as hope fades. An answer that
     * is still wrong on the second look is usually a service that has fallen behind rather than one that
     * is momentarily busy, and points are granted by a different service from the one that counts them.
     */
    private static final int RETRY_PAUSE_SECONDS = 3;

    /** What the loyalty service files a test's points as; the ratio is one point granted per point asked. */
    private static final String POINT_GRANT_TYPE = "REWARD";
    private static final int POINT_GRANT_RATIO = 1;

    public GamificationApi(Session<T> session) {
        super(session);
    }

    /**
     * Hands the treasure of a Berburu Tiket Murah hunt to {@code accountId} through the hunt backend,
     * without going through that account's browser session.
     *
     * <p>This is the setup a snatch test needs: the game keeps the snatch button inactive for whoever
     * is already holding the treasure, so the account under test can only bid once somebody else has
     * taken it.
     *
     * @param accountId the account that should end up holding the treasure
     * @param sessionId the hunt session of the campaign being bid on
     */
    public void snatchTreasureAs(long accountId, String sessionId) {
        if (SnatchTreasureApi.isUnSupportedEnvironment()) {
            Assertion.skip("this api is not supported in the " + Configs.ENVIRONMENT + " environment");
        }

        String lastAnswer = null;
        for (int attempt = 1; attempt <= SNATCH_ATTEMPTS; attempt++) {
            ApiResponse response = hitSnatchTreasure(accountId, sessionId);
            if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
                return;
            }

            lastAnswer = "[" + response.getStatusCode() + "] " + response.getBody();
            if (response.getStatusCode() == 404) {
                Assertion.fail("The hunt has no session " + sessionId + " to hit: " + lastAnswer
                        + ". That usually means the sessionId in the test data sheet belongs to a campaign "
                        + "that has since ended, and no amount of retrying will bring it back.");
            }
            logger.softWarning("Snatch attempt " + attempt + "/" + SNATCH_ATTEMPTS + " answered " + lastAnswer);
            pauseBetweenAttempts(attempt, SNATCH_ATTEMPTS);
        }

        Assertion.fail("Could not hand the treasure to account " + accountId + " over " + SNATCH_ATTEMPTS
                + " attempts, so there is nothing for the account under test to snatch. Last answer: "
                + lastAnswer + ". A 503 means the hunt backend behind the load balancer is down.");
    }

    private ApiResponse hitSnatchTreasure(long accountId, String sessionId) {
        String hitKey = UUID.randomUUID().toString();
        logger.log("Snatching the treasure via API: accountId=" + accountId + ", sessionId=" + sessionId
                + ", key=" + hitKey);

        SnatchTreasureApi api = new SnatchTreasureApi(
                new SnatchTreasureApi.Inputs(accountId, sessionId, hitKey));
        ApiResponse response = api.hitApi(apiTools);
        logger.log("Snatch treasure API response: " + response.getStatusCode() + " - " + response.getBody());
        return response;
    }

    /**
     * The account's Blibli Tiket Points, which are what the hunt trades for its tokens.
     *
     * <p>Read straight off the loyalty service rather than through the account's browser session, so
     * this holds before a login as well as after one. Transient failures are retried because the
     * endpoint answers a plain {@code RUNTIME_ERROR} when it is having trouble, which is indistinguishable
     * from a real answer of none.
     */
    public int getLoyaltyPoints(long accountId, String username) {
        ActiveTiketPointsApi api = new ActiveTiketPointsApi(
                new ActiveTiketPointsApi.Inputs(accountId, username));

        String lastFailure = null;
        for (int attempt = 1; attempt <= POINTS_READ_ATTEMPTS; attempt++) {
            ApiResponse response = api.hitApi(apiTools);
            ActiveTiketPointsResponse points = new Gson().fromJson(response.getBody(), ActiveTiketPointsResponse.class);
            if (points != null && points.data != null && points.data.trim().matches("-?\\d+")) {
                return Integer.parseInt(points.data.trim());
            }
            lastFailure = "[" + response.getStatusCode() + "] " + response.getBody();
            logger.softWarning("Points read attempt " + attempt + "/" + POINTS_READ_ATTEMPTS
                    + " came back without a number: " + lastFailure);
            pauseBetweenAttempts(attempt, POINTS_READ_ATTEMPTS);
        }
        throw new ApiToolsException("Could not read the account's tiket points after " + POINTS_READ_ATTEMPTS
                + " attempts. Last answer: " + lastFailure);
    }

    /**
     * Brings the account's points up to {@code minimumPoints}, leaving one that already holds enough
     * alone, and returns the balance the loyalty service settles on.
     *
     * <p>Topping up to a floor rather than granting a fixed amount is what keeps a test that spends
     * points repeatable: the balance settles around the floor instead of climbing on every run.
     *
     * <p>The service granting points is not the one that counts them, so the balance is read back
     * rather than assumed; granted points that have not been counted yet look exactly like a shortfall.
     *
     * <p>A balance that never reaches the floor fails here rather than being handed back for a caller
     * to notice. Returning a figure that is known to be mid-flight is worse than failing: the UI shows
     * the settled one a moment later, and the test then fails on a points assertion that looks like a
     * product bug instead of on the top-up that did not land.
     */
    public int topUpLoyaltyPointsTo(long accountId, String username, int minimumPoints) {
        int points = getLoyaltyPoints(accountId, username);
        if (points >= minimumPoints) {
            return points;
        }

        logger.log("Account holds " + points + " tiket points; granting " + (minimumPoints - points)
                + " to bring it up to " + minimumPoints);
        grantLoyaltyPoints(accountId, username, minimumPoints - points);

        for (int attempt = 1; attempt <= POINTS_READ_ATTEMPTS; attempt++) {
            points = getLoyaltyPoints(accountId, username);
            if (points >= minimumPoints) {
                return points;
            }
            logger.softWarning("Granted points are not counted yet on attempt " + attempt + "/"
                    + POINTS_READ_ATTEMPTS + ": the account holds " + points + " of the " + minimumPoints
                    + " asked for.");
            pauseBetweenAttempts(attempt, POINTS_READ_ATTEMPTS);
        }

        Assertion.fail("The loyalty service still counts only " + points + " of the " + minimumPoints
                + " tiket points asked for after " + POINTS_READ_ATTEMPTS + " reads. The grant was accepted, "
                + "so this is the service that counts points lagging behind the one that grants them.");
        return points;
    }

    private void grantLoyaltyPoints(long accountId, String username, int points) {
        AddActivePointsApi api = new AddActivePointsApi(new AddActivePointsApi.Inputs(
                Math.toIntExact(accountId), points, POINT_GRANT_TYPE, POINT_GRANT_RATIO, username));

        ApiResponse response = api.hitApi(apiTools);
        Assertion.assertThat("Granting " + points + " tiket points to account " + accountId + " should succeed. "
                        + "Without them there is nothing for the hunt to trade for tokens.",
                response.getStatusCode(), is(200));
    }

    private void pauseBetweenAttempts(int attempt, int attempts) {
        if (attempt >= attempts) {
            return;
        }
        try {
            TimeUnit.SECONDS.sleep((long) RETRY_PAUSE_SECONDS * attempt);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new ApiToolsException("Interrupted while waiting on the BTM backend", interrupted);
        }
    }


    /**
     * Wipes every harta-karun admin product-token override currently returned by
     * {@code GET /ggwp-server/harta-karun/admin/product-token}, then
     * {@code DELETE /ggwp-server/harta-karun/admin/product-token/{id}} for each id.
     *
     * Deletes one harta-karun admin product-token override: GET the list, find the row whose
     * {@code inventoryName} matches, then DELETE by that row's {@code id}.
     */
    public void deleteProductTokenByInventoryName(String inventoryName) {
        if (GetAdminProductTokenApi.isUnsupportedEnvironment()) {
            Assertion.skip("this api is not supported in the " + Configs.ENVIRONMENT + " environment");
        }

        String name = inventoryName == null ? "" : inventoryName.trim();
        if (name.isEmpty()) {
            Assertion.fail("inventoryName is required to find a product-token override to delete");
        }

        logger.log("Deleting product-token override for inventoryName=" + name);
        String productTokenId = findProductTokenIdByInventoryName(listProductTokenOverrides(), name);
        if (productTokenId == null) {
            Assertion.fail("No product-token override named '" + name + "'");
        }
        deleteProductToken(productTokenId);
    }

    private JSONArray listProductTokenOverrides() {
        ApiResponse response = new GetAdminProductTokenApi().hitApi(apiTools);
        logger.log("List product-token overrides: " + response.getStatusCode() + " - " + response.getBody());
        Assertion.assertThat("Listing product-token overrides should succeed so they can be cleaned",
                response.getStatusCode(), is(200));
        try {
            JSONObject body = new JSONObject(response.getBody());
            JSONArray data = body.optJSONArray("data");
            return data == null ? new JSONArray() : data;
        } catch (Exception e) {
            Assertion.fail("Product-token list was not JSON: " + response.getBody());
            return new JSONArray();
        }
    }

    private String findProductTokenIdByInventoryName(JSONArray overrides, String inventoryName) {
        for (int i = 0; i < overrides.length(); i++) {
            JSONObject item = overrides.optJSONObject(i);
            if (item == null) {
                continue;
            }
            if (!inventoryName.equals(item.optString("inventoryName", "").trim())) {
                continue;
            }
            String productTokenId = item.optString("id", "").trim();
            if (!productTokenId.isEmpty()) {
                return productTokenId;
            }
        }
        return null;
    }

    private void deleteProductToken(String productTokenId) {
        ApiResponse response = new DeleteAdminProductTokenApi(
                new DeleteAdminProductTokenApi.Inputs(productTokenId)).hitApi(apiTools);
        logger.log("Deleted product-token override " + productTokenId + ": "
                + response.getStatusCode() + " - " + response.getBody());
        int status = response.getStatusCode();
        if (status == 404) {
            logger.log("Product-token override " + productTokenId + " was already gone");
            return;
        }
        if (status != 200 && status != 204) {
            Assertion.fail("Could not delete product-token override " + productTokenId
                    + ": [" + status + "] " + response.getBody());
        }
    }
}
