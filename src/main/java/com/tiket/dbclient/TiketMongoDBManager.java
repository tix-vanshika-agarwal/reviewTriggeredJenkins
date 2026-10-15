package com.tiket.dbclient;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.tiket.dbclient.mongodb.DbBase;
import org.apache.logging.log4j.LogManager;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class TiketMongoDBManager {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(TiketMongoDBManager.class);
    private DbBase mongoDBBase;

    private static final Object MONGO_DB_INSTANCE_LOCK = new Object();
    private static final Map<String, TiketMongoDBManager> INSTANCE_MAP = new HashMap<>();

    public TiketMongoDBManager(String configType, String database) {
        try {
            LOGGER.info("::::" + configType);
            mongoDBBase = new DbBase(configType, database);
            LOGGER.info("DbBase : " + mongoDBBase);
        } catch (Exception e) {
            LOGGER.error("Error accessing MongoDB Connection ::::: " + e);
            System.exit(1);
        }
    }

    public MongoCollection<Document> getCollection(String collectionName, String configType, String database) {
        DbBase dbBase = new DbBase(configType, database);
        return dbBase.getCollection(collectionName);
    }

    synchronized public static TiketMongoDBManager getInstance(String configType, String database) {
        LOGGER.info("Getting MongoDB connection with config: " + configType);
        if (INSTANCE_MAP.get(configType) == null) {
            synchronized (MONGO_DB_INSTANCE_LOCK) {
                if (INSTANCE_MAP.get(configType) == null) {
                    LOGGER.info("Creating new MongoDB connection with config: " + configType);
                    TiketMongoDBManager tiketMongoDBManager = new TiketMongoDBManager(configType, database);
                    INSTANCE_MAP.put(configType, tiketMongoDBManager);
                }
            }
        }
        return INSTANCE_MAP.get(configType);
    }

    public static void closeMongoDBConnections() {
        LOGGER.info("Closing all MongoDB connections");
        for (Map.Entry<String, TiketMongoDBManager> db : INSTANCE_MAP.entrySet()) {
            db.getValue().mongoDBBase.closeMongoDBConnection();
        }
    }

    public static FindIterable<Document> searchInMongoDB(String configType, String dataBase, String collectionName, String queryString) {
        TiketMongoDBManager mongoDBManager = TiketMongoDBManager.getInstance(configType, dataBase);
        MongoCollection<Document> collection = mongoDBManager.getCollection(collectionName, configType, dataBase);
        Document query = Document.parse(queryString);
        return collection.find(query);
    }

    public static FindIterable<Document> searchInMongoDBWithProjectionString(String configType, String dataBase, String collectionName, String queryString, String projectionString) {
        TiketMongoDBManager mongoDBManager = TiketMongoDBManager.getInstance(configType, dataBase);
        MongoCollection<Document> collection = mongoDBManager.getCollection(collectionName, configType, dataBase);
        Document query = Document.parse(queryString);
        Document projection = Document.parse(projectionString);
        return collection.find(query).projection(projection);
    }

    public static FindIterable<Document> searchInMongoDBWithSortLimit(String configType, String dataBase, String collectionName, String queryString, String sortBy, int limit) {
        TiketMongoDBManager mongoDBManager = TiketMongoDBManager.getInstance(configType, dataBase);
        MongoCollection<Document> collection = mongoDBManager.getCollection(collectionName, configType, dataBase);
        Document query = Document.parse(queryString);
        Document sortField = Document.parse(sortBy);
        return collection.find(query).sort(sortField).limit(limit);
    }

    public static FindIterable<Document> searchInMongoDBWithDocument(String configType, String dataBase, String collectionName, Document data) {
        TiketMongoDBManager mongoDBManager = TiketMongoDBManager.getInstance(configType, dataBase);
        MongoCollection<Document> collection = mongoDBManager.getCollection(collectionName, configType, dataBase);
        return collection.find(data);
    }
}
