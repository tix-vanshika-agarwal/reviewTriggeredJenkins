package com.tiket.excel.testdata;

import com.tiket.excel.Parser;
import com.tiket.io.network.GoogleSheetApi2;
import com.tiket.model.TestDataElement;

import java.util.List;
import java.util.Map;

import static com.tiket.excel.Parser.getSafeStringValue;

public class GoogleParser implements Parser<List<TestDataElement>> {

    private static TestDataElement dataFrom(List<String> row) {

        int index = 0;
        String title = getSafeStringValue(row, index++);
        String description = getSafeStringValue(row, index++);
        String platform = getSafeStringValue(row, index++).trim();
        boolean shouldRun = "y".equals(getSafeStringValue(row, index++));
        boolean gk = "y".equals(getSafeStringValue(row, index++));
        boolean prod = "y".equals(getSafeStringValue(row, index++));
        boolean preprod = "y".equals(getSafeStringValue(row, index++));
        boolean sanity = "y".equals(getSafeStringValue(row, index++));
        boolean regression = "y".equals(getSafeStringValue(row, index++));
        String tribe = getSafeStringValue(row, index++);
        String module = getSafeStringValue(row, index++);

        List<Map<String, String>> gkParams = Parser.getParams(getSafeStringValue(row, index++));
        List<Map<String, String>> prodParams = Parser.getParams(getSafeStringValue(row, index++));
        List<Map<String, String>> preprodParams = Parser.getParams(getSafeStringValue(row, index++));

        return new TestDataElement(title, description, platform, shouldRun, gk, prod, preprod, sanity, regression, tribe, module, gkParams, prodParams, preprodParams);
    }

    @Override
    public List<TestDataElement> getData() {
        int sheetIndex = 0;
        List<List<String>> rows = GoogleSheetApi2.getData(sheetIndex);
        List<TestDataElement> rawList = rows.stream()
                .map(GoogleParser::dataFrom)
                .toList();

        List<TestDataElement> filteredList = Parser.filterTestData(rawList);
        Parser.setFilteredParams(filteredList);
        return filteredList;
    }
}
