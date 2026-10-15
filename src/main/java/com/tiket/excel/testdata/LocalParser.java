package com.tiket.excel.testdata;

import com.tiket.excel.Parser;
import com.tiket.model.TestDataElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static com.tiket.excel.Parser.*;

public class LocalParser implements Parser<List<TestDataElement>> {

    private static final Logger logger = LogManager.getLogger(LocalParser.class);

    private static TestDataElement dataFrom(Row row) {

        int index = 0;
        String title = getNullSafeStringValue(row, index++);
        String description = getNullSafeStringValue(row, index++);
        String platform = getNullSafeStringValue(row, index++).trim();
        boolean shouldRun = "y".equals(getNullSafeStringValue(row, index++));
        boolean gk = "y".equals(getNullSafeStringValue(row, index++));
        boolean prod = "y".equals(getNullSafeStringValue(row, index++));
        boolean preprod = "y".equals(getNullSafeStringValue(row, index++));
        boolean sanity = "y".equals(getNullSafeStringValue(row, index++));
        boolean regression = "y".equals(getNullSafeStringValue(row, index++));
        String tribe = getNullSafeStringValue(row, index++);
        String module = getNullSafeStringValue(row, index++);

        List<Map<String, String>> gkParams = Parser.getParams(getNullSafeStringValue(row, index++));
        List<Map<String, String>> prodParams = Parser.getParams(getNullSafeStringValue(row, index++));
        List<Map<String, String>> preprodParams = Parser.getParams(getNullSafeStringValue(row, index++));

        return new TestDataElement(title, description, platform, shouldRun, gk, prod, preprod, sanity, regression, tribe, module, gkParams, prodParams, preprodParams);
    }

    @Override
    public List<TestDataElement> getData() {

        String file = getFile();

        List<TestDataElement> rawList = StreamSupport.stream(getSheet(file, 0).spliterator(), false)
                .map(LocalParser::dataFrom)
                .toList();

        List<TestDataElement> filteredList = filterTestData(rawList);
        setFilteredParams(filteredList);
        return filteredList;
    }
}
