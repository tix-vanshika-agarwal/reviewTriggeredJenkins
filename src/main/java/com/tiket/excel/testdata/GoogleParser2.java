package com.tiket.excel.testdata;

import com.tiket.excel.Parser;
import com.tiket.io.network.GoogleSheetApi;
import com.tiket.model.TestDataElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoogleParser2 implements Parser<List<TestDataElement>> {

    public static final Logger logger = LogManager.getLogger(GoogleParser2.class);

    private static TestDataElement dataFrom(JSONArray array) {

        int index = 0;
        String title = getNullSafeStringValue(array, index++);
        String description = getNullSafeStringValue(array, index++);
        String platform = getNullSafeStringValue(array, index++).trim();
        boolean shouldRun = "y".equals(getNullSafeStringValue(array, index++));
        boolean gk = "y".equals(getNullSafeStringValue(array, index++));
        boolean prod = "y".equals(getNullSafeStringValue(array, index++));
        boolean preprod = "y".equals(getNullSafeStringValue(array, index++));
        boolean sanity = "y".equals(getNullSafeStringValue(array, index++));
        boolean regression = "y".equals(getNullSafeStringValue(array, index++));
        String tribe = getNullSafeStringValue(array, index++);
        String module = getNullSafeStringValue(array, index++);

        List<Map<String, String>> gkParams = Parser.getParams(getNullSafeStringValue(array, index++));
        List<Map<String, String>> prodParams = Parser.getParams(getNullSafeStringValue(array, index++));
        List<Map<String, String>> preprodParams = Parser.getParams(getNullSafeStringValue(array, index++));

        return new TestDataElement(title, description, platform, shouldRun, gk, prod, preprod, sanity, regression, tribe, module, gkParams, prodParams, preprodParams);
    }

    private static String getNullSafeStringValue(JSONArray array, int index) {
        try {
            return array.getString(index);
        } catch (JSONException je) {
            return "";
        }
    }

    @Override
    public List<TestDataElement> getData() {
        List<TestDataElement> rawList = new ArrayList<>();
        JSONObject object = GoogleSheetApi.getData();
        JSONArray values = (JSONArray) object.get("values");
        for (int i = 1; i < values.length(); i++) {
            JSONArray value = (JSONArray) values.get(i);
            rawList.add(dataFrom(value));
        }

        List<TestDataElement> filteredList = Parser.filterTestData(rawList);
        Parser.setFilteredParams(filteredList);
        return filteredList;
    }
}
