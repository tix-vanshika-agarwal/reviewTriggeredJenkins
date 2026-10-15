package com.tiket.excel.properties;

import com.tiket.excel.Parser;
import com.tiket.io.network.GoogleSheetApi2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tiket.excel.Parser.getSafeStringValue;

public class GoogleParser implements Parser<Map<String, String>> {

    private static final Logger logger = LogManager.getLogger(GoogleParser.class);

    private static Map.Entry<String, String> dataFrom(List<String> row) {
        String key = getSafeStringValue(row, 0);
        String value = getSafeStringValue(row, 1);
        logger.debug("key: " + key + ", value: " + value);
        return Map.entry(key, value);
    }

    @Override
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        int sheetIndex = 1;
        List<List<String>> rows = GoogleSheetApi2.getData(sheetIndex);
        rows.stream()
                .map(GoogleParser::dataFrom)
                .forEach(entry -> data.put(entry.getKey(), entry.getValue()));
        return data;
    }
}
