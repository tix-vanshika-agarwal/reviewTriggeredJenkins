package com.tiket.dbclient.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tiket.logging.ILogger;
import com.tiket.model.DBProperties;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.apache.logging.log4j.LogManager;
import org.bson.Document;

public class DbBase {
    protected static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(DbBase.class);
    private DBProperties conf;
    private MongoClient mongoClient;
    private MongoDatabase database;
    ILogger logger = BaseTest.mainLogger.get();

    private DBProperties getDbProperties(String configType) {
        return Configs.DB_PROPERTIES.stream()
        .filter(db -> (db.tribe().equalsIgnoreCase(configType) && db.environment().toLowerCase().equals(Configs.ENVIRONMENT.name().toLowerCase())))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No DB properties found for tribe: " + configType));
    }

    public DbBase(String configType, String database) {
        this.conf = getDbProperties(configType);
        try {
            logger.log("Creating new MongoDB connection");
            this.setUpConnection(database);
        } catch (Exception e) {
            LOGGER.error("Error creating MongoDB connection :::: " + e);
            System.exit(1);
        }
    }

    private void setUpConnection(String database) {
        this.mongoClient = MongoClients.create(connectionString(database));
        this.database = this.mongoClient.getDatabase(database);
    }

    private String connectionString(String database) {
        return "mongodb://" + this.conf.dbUsername() +":" + this.conf.dbPassword() + "@" + this.conf.dbHost() + ":" + this.conf.dbPort() +"/";
    }

    public void closeMongoDBConnection() {
        try {
            logger.log("Closing MongoDB connection with configuration: " + this.conf);
            this.mongoClient.close();
        } catch (Exception e) {
            logger.fail("Error closing MongoDB connection with " + this.conf);
        }
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return this.database.getCollection(collectionName);
    }
}
