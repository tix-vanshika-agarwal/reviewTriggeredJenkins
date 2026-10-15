package com.tiket.analytics;

import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;

public class BaseAnalyticsTest extends BaseTest {

    protected static final int ANALYTICS_WAIT_SECONDS = 10;

    @Override
    @AfterMethod
    public void afterMethod(ITestResult result, ITestContext context) {
        try {
            Configs.MESSAGES.clear();
        } finally {
            super.afterMethod(result, context);
        }
    }

    @SneakyThrows
    @Override
    @AfterSuite
    public void afterSuite() {
        super.afterSuite();
        Configs.PROXY.stop();
    }
}
