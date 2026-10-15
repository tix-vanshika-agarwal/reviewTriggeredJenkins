package com.tiket.excel.properties;

import com.tiket.excel.Parser;
import org.apache.poi.ss.usermodel.Row;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import static com.tiket.excel.Parser.*;

public class LocalParser implements Parser<Map<String, String>> {

    private static Map.Entry<String, String> dataFrom(Row row) {
        String key = getNullSafeStringValue(row, 0);
        String value = getNullSafeStringValue(row, 1);
        return Map.entry(key, value);
    }

    @Override
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        String file = getFile();
        StreamSupport.stream(getSheet(file, 1).spliterator(), false)
                .map(LocalParser::dataFrom)
                .forEach(entry -> data.put(entry.getKey(), entry.getValue()));
        return data;
    }
}
