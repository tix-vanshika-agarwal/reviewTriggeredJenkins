package com.tiket.excel.dbproperties;

import com.tiket.excel.Parser;
import com.tiket.model.DBProperties;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.stream.StreamSupport;

import static com.tiket.excel.Parser.*;

public class LocalParser implements Parser<List<DBProperties>> {

    private static DBProperties dataFrom(Row row) {
        int index = 0;
        String environtment = getNullSafeStringValue(row, index++);
        String tribe = getNullSafeStringValue(row, index++);
        String dbHost = getNullSafeStringValue(row, index++);
        String rawDbPort = getNullSafeStringValue(row, index++);
        String dbPort = rawDbPort.equals("")? "" :rawDbPort.substring(0, rawDbPort.indexOf("."));
        String dbUsername = getNullSafeStringValue(row, index++);
        String dbPassword = getNullSafeStringValue(row, index++);

        logger.debug("DB properties tribe: " + tribe + ", dbHost: " + dbHost);
        return new DBProperties(environtment,tribe, dbHost, dbPort,dbUsername, dbPassword);
    }

    @Override
    public List<DBProperties> getData() {
        logger.debug("Getting DB properties data from local excel file");
        String file = getFile();
        List<DBProperties> rawList = StreamSupport.stream(getSheet(file, 2).spliterator(), false).skip(1)
                .map(LocalParser::dataFrom)
                .toList();
        return rawList;
    }
}
