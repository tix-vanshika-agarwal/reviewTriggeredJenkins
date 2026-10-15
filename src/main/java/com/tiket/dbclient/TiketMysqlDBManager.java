package com.tiket.dbclient;

import com.tiket.dbclient.mysql.DbBase;
import com.tiket.model.DBProperties;
import com.tiket.testbase.Configs;
import org.apache.logging.log4j.LogManager;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TiketMysqlDBManager {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(TiketMysqlDBManager.class);
    private DbBase dbBase;
    // mapping config with correct instance
    private static Map<String, TiketMysqlDBManager> INSTANCE_MAP = new HashMap<>();

    private static DBProperties getDbProperties(String configType) {
        LOGGER.info("DB : " + Configs.ENVIRONMENT.name().toLowerCase());
        return Configs.DB_PROPERTIES.stream()
                .filter(db -> (db.tribe().equalsIgnoreCase(configType) && db.environment().toLowerCase().equals(Configs.ENVIRONMENT.name().toLowerCase())))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No DB properties found for tribe: " + configType));
    }

    public TiketMysqlDBManager(String configType) {
        try {
            DBProperties dbProperties = getDbProperties(configType);
            LOGGER.info("::::" + configType);
            dbBase = new DbBase(dbProperties);
            LOGGER.info("DbBase : " + dbBase);
        } catch (Exception e) {
            LOGGER.error("Error accessing DB Connection ::::: " + e);
            System.exit(1);
        }
    }

    // abstraction to executing query
    public CachedRowSet executeQuery(String query) throws SQLException {
        return dbBase.executeQuery(query);
    }

    // abstraction to executing parameterized DML (INSERT/UPDATE/DELETE), returns affected row count
    public int executeUpdate(String query, Object... params) throws SQLException {
        return dbBase.executeUpdate(query, params);
    }

    synchronized public static TiketMysqlDBManager getInstance(String configType) {
        LOGGER.info("Getting DB connection with config: " + configType);
        if (INSTANCE_MAP.get(configType) == null) {
            LOGGER.info("Creating new DB connection with config: " + configType);
            TiketMysqlDBManager tiketMysqlDBManager = new TiketMysqlDBManager(configType);
            INSTANCE_MAP.put(configType, tiketMysqlDBManager);
        }
        return INSTANCE_MAP.get(configType);
    }

    public static void closeDataSourceConnection() {
        LOGGER.info("Closing all DB connections");
        for (Map.Entry<String, TiketMysqlDBManager> db : INSTANCE_MAP.entrySet()) {
            db.getValue().dbBase.closeDbConnection();
        }
    }
}