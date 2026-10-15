package com.tiket.excel.dbproperties;

import com.tiket.excel.Parser;
import com.tiket.io.network.GoogleSheetApi2;
import com.tiket.model.DBProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.tiket.excel.Parser.getSafeStringValue;

public class GoogleParser implements Parser<List<DBProperties>> {

    private static final Logger logger = LogManager.getLogger(GoogleParser.class);

    private static DBProperties dataFrom(List<String> row) {
        int index = 0;
        String environment = getSafeStringValue(row, index++);
        String tribe = getSafeStringValue(row, index++);
        String dbHost = getSafeStringValue(row, index++);
        String dbPort = getSafeStringValue(row, index++);
        String dbUsername  =  getSafeStringValue(row, index++);
        String dbPassword = getSafeStringValue(row, index++);

        logger.debug("DB properties tribe: " + tribe + ", dbHost: " + dbHost);
        return new DBProperties(environment,tribe, dbHost,dbPort,dbUsername, dbPassword);
    }

    @Override
    public List<DBProperties> getData() {
        logger.debug("Getting db properties from google sheet");
        int sheetIndex = 2;
        List<List<String>> rows = GoogleSheetApi2.getData(sheetIndex);
        List<DBProperties> dbPropertiesList = rows.stream().skip(1)
                .map(GoogleParser::dataFrom)
                .toList();
        return dbPropertiesList;
    }
}
