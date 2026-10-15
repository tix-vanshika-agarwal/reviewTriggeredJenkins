package com.tiket.testdata;

import com.tiket.model.TestDataElement;

import java.util.List;
import java.util.Map;

public class TestData {

    public static Map<String, String> getParam(String testName) {
        List<Map<String, String>> paramList = TestDataElement.titleParams.get(testName);
        return paramList.get(0);
    }

    public static Object[][] getObjectParams(String testName) {
        List<Map<String, String>> paramList = TestDataElement.titleParams.get(testName);
        Object[][] objects = new Object[paramList.size()][];
        for (int i = 0; i < paramList.size(); i++) {
            objects[i] = new Object[]{paramList.get(i)};
        }
        return objects;
    }
}
