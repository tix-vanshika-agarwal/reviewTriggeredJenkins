package com.tiket.dbclient.mysql;

import com.tiket.logging.ILogger;
import com.tiket.model.DBProperties;
import com.tiket.testbase.BaseTest;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.logging.log4j.LogManager;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbBase {
    protected static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(DbBase.class);
    protected DBProperties conf;
    private BasicDataSource ds = new BasicDataSource();
    ILogger logger = BaseTest.mainLogger.get();

    public DbBase(DBProperties dbConfig) {
        this.conf = dbConfig;
        try {
            logger.log("Creting new db connection");
            this.setUpPool();
        } catch (Exception var3) {
            LOGGER.error("Error Creating JDBC Connection Pool :::: " + var3);
            System.exit(1);
        }

    }

    private void setUpPool() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(connectionURL());
        ds.setUsername(this.conf.dbUsername());
        ds.setPassword(this.conf.dbPassword());
        ds.setMinIdle(1);
        ds.setMaxIdle(2);
        ds.setMaxOpenPreparedStatements(2);
    }

    private String connectionURL() {
        return "jdbc:mysql://" + this.conf.dbHost() + ":" + this.conf.dbPort()
                +
                "?autoReconnect=true&useSSL=false&zeroDateTimeBehavior=CONVERT_TO_NULL";
    }

    public void closeDbConnection() {
        try {
            logger.log("Closing connection with configuration: " + this.conf);
            ds.close();
        } catch (Exception var2) {
            logger.fail("Error closing DB connection with " + this.conf);
        }

    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public CachedRowSet executeQuery(String query) throws SQLException {
        ILogger loggers = BaseTest.mainLogger.get();
        loggers.log("Query to be executed is ::::::::::: " + query);
        ResultSet rs = null;
        Connection connection = null;
        CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
        try {
            connection = this.getConnection();
            PreparedStatement stmnt = connection.prepareStatement(query);
            rs = stmnt.executeQuery();
            loggers.log("Query ::" + query + " is executed, result is :::: " + rs);
            cachedRowSet.populate(rs);
            return cachedRowSet;
        } catch (Exception e) {
            loggers.fail("Error executing query :::: " + e);
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Executes a parameterized DML statement (INSERT/UPDATE/DELETE) and returns the affected row count.
     * Bind values are passed as positional parameters so callers never concatenate user/test data into SQL.
     */
    public int executeUpdate(String query, Object... params) throws SQLException {
        ILogger loggers = BaseTest.mainLogger.get();
        loggers.log("Update to be executed is ::::::::::: " + query);
        try (Connection connection = this.getConnection();
             PreparedStatement stmnt = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                stmnt.setObject(i + 1, params[i]);
            }
            int affectedRows = stmnt.executeUpdate();
            loggers.log("Update executed, rows affected :::: " + affectedRows);
            return affectedRows;
        } catch (Exception e) {
            loggers.fail("Error executing update :::: " + e);
            throw e;
        }
    }
}