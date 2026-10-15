package com.tiket.web.dashboard.test.platform.eiffel;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.web.WebTest;
import com.tiket.web.dashboard.page.platform.eiffel.ggwp.CampaignSetupProductTokenPage;
import org.testng.annotations.Test;
import com.tiket.apiPage.GamificationApi;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;

public class CampaignSetupProductTokenTest extends WebTest {

    @Author(names = {"Arini"})
    @Module(name = "GGWP")
    @Tribe(name = "Eiffel")
    @Vertical(name = "Platform")
    @Test
    public void testAddNewProductTokenOverride() {
        Session<?> session = WebTest.sessionThreadLocal.get();
        Map<String, String> testData = getParam("testAddNewProductTokenOverride");
        WebTest.paramsThreadLocal.set(testData);

        String email = testData.get("email");
        String password = testData.get("password");
        String productType = testData.get("productType");
        String overrideToken = testData.get("overrideToken");
        String successMessage = testData.get("successMessage");

        CampaignSetupProductTokenPage<?> campaignSetupProductTokenPage = new CampaignSetupProductTokenPage<>(session);

        boolean isTokenCreated = false;
        String savedInventoryName = null;

        try {
            step("Given Admin logs in to the GGWP Campaign Setup dashboard");
            campaignSetupProductTokenPage.goToCampaignSetupProductTokenPageViaUI(email, password);

            step("Admin clicks the Add Product Override button");
            campaignSetupProductTokenPage.clickAddProductOverride();

            step("Admin selects the product type");
            campaignSetupProductTokenPage.selectProductType(productType);

            step("Admin selects the first available inventory ID");
            campaignSetupProductTokenPage.clickInventoryId();
            savedInventoryName = campaignSetupProductTokenPage.selectFirstAvailableInventoryId();

            step("Admin input override token");
            campaignSetupProductTokenPage.inputOverrideToken(overrideToken);

            step("Admin click Add product button");
            campaignSetupProductTokenPage.clickAddProduct();

            isTokenCreated = true;

            step("Then Admin should see a success snackbar");
            campaignSetupProductTokenPage.verifySuccessSnackbarDisplayed(successMessage);

            step("And the product token should be saved correctly in the UI");
            campaignSetupProductTokenPage.verifyProductTokenList(productType, savedInventoryName, overrideToken);

        } finally {
            if (isTokenCreated && savedInventoryName != null) {
                step("Cleaning up the created product token via API");

                GamificationApi<?> gamificationApi = new GamificationApi<>(session);

                gamificationApi.deleteProductTokenByInventoryName(savedInventoryName);
            }
        }
    }
}