package com.tiket.testng;

import com.tiket.model.Exception.ApiToolsException;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Exception.ForbiddenException;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.NoSuchElementException;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int RETRY_LIMIT = Integer.parseInt(Configs.PROPERTIES_DATA.getOrDefault("retry.limit", "0"));
    private static final int SO_TIMEOUT_RETRY_LIMIT = Integer.parseInt(Configs.PROPERTIES_DATA.getOrDefault("so.timeout.retry.limit", "0"));
    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private int so_counter = 0;
    private int counter = 0;

    @Override
    public boolean retry(ITestResult result) {

        Throwable throwable = result.getThrowable();
        Class<? extends Throwable> error = throwable.getClass();

        logger.error("Analysing exception: " + ExceptionUtils.getStackTrace(throwable));
        BaseTest.mainLogger.get().exception(throwable);

        // For BrowserStack SO_timeout, device disconnection, etc.., without modifying counter
        if(so_counter < SO_TIMEOUT_RETRY_LIMIT && ForbiddenException.class.equals(error)) {
            so_counter++;
            return true;
        }

        if (counter < RETRY_LIMIT) {
            counter++;
            if (
                    DriverToolsException.class.equals(error) ||
                            ApiToolsException.class.equals(error) ||
                            AssertionError.class.equals(error) ||
                            NoSuchElementException.class.equals(error) ||
                            StaleElementReferenceException.class.equals(error)
            ) {
                logger.warn(result.getMethod().getMethodName() + ", Retry count: " + counter);
                return true;
            }
            return true; // bypass for all other exceptions, to retry up to RETRY_LIMIT times
        }
        return false;
    }
}