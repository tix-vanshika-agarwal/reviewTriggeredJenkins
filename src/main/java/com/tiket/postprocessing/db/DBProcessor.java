package com.tiket.postprocessing.db;

import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.io.CachedPool;
import com.tiket.model.db.DBEntry;
import com.tiket.model.db.Status;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class DBProcessor {

    private static final Logger logger = LogManager.getLogger(DBProcessor.class);
    private static final String DB_URL = Configs.PROPERTIES_DATA.get("db.url");
    private static final String DB_USERNAME = Configs.PROPERTIES_DATA.get("db.username");
    private static final String DB_PASSWORD = Configs.PROPERTIES_DATA.get("db.password");
    private static final String DB_STATEMENT = "INSERT INTO results(status, vertical_name, tribe_name, module_name, platform, test_type, environment, testrail_id, run_id, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private Connection connection;

    @SneakyThrows
    public DBProcessor() {
        logger.info("DB flag: " + Configs.DB_USE);
        if (Configs.DB_USE) {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        }
    }

    @SneakyThrows
    public void close() {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public Optional<CompletableFuture<Boolean>> sendResult(ITestResult iTestResult, String runID, String caseID) {
        if (connection != null) {
            CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> execute(iTestResult, runID, caseID, 3), CachedPool.POOL)
                    .whenComplete((aBoolean, throwable) -> {
                        if (throwable != null) {
                            logger.error("Error sending result for caseID: " + caseID);
                        } else {
                            logger.info("successfully send result for caseID: " + caseID);
                        }
                    });
            return Optional.of(future);
        } else {
            return Optional.empty();
        }
    }

    private boolean execute(ITestResult iTestResult, String runID, String caseID, int times) {
        if (times < 1) return false;
        Status status = dbResultFrom(iTestResult);
        DBEntry entry = dbEntryFrom(iTestResult, status, runID, caseID);
        try {
            PreparedStatement statement = connection.prepareStatement(DB_STATEMENT);
            statement.setString(1, status.name());
            statement.setString(2, entry.verticalName());
            statement.setString(3, entry.tribeName());
            statement.setString(4, entry.moduleName());
            statement.setString(5, entry.platform().name());
            statement.setString(6, entry.testType().name());
            statement.setString(7, entry.environment().name());
            statement.setString(8, entry.testRailID());
            statement.setString(9, entry.runID());
            statement.setLong(10, entry.timestamp());
            return statement.execute();
        } catch (SQLException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            execute(iTestResult, runID, caseID, times - 1);
        }
        return false;
    }

    private DBEntry dbEntryFrom(ITestResult result, Status status, String runID, String caseID) {
        return new DBEntry(
                status,
                result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Vertical.class).name(),
                result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Tribe.class).name(),
                result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Module.class).name(),
                Configs.PLATFORM,
                Configs.TEST_TYPE,
                Configs.ENVIRONMENT,
                caseID,
                runID,
                System.currentTimeMillis()
        );
    }

    private Status dbResultFrom(ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.SUCCESS -> {
                return Status.PASS;
            }
            case ITestResult.SKIP -> {
                return Status.SKIP;
            }
            case ITestResult.FAILURE -> {
                return Status.FAIL;
            }
            default -> throw new NotImplementedException("Status not implemented: " + result.getStatus());
        }
    }
}
