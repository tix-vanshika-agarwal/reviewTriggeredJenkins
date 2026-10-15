package com.tiket.excel;

import com.tiket.model.Environment;
import com.tiket.model.TestDataElement;
import com.tiket.model.TestType;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.function.Predicate;

public interface Parser<T> {

    Logger logger = LogManager.getLogger(Parser.class);

    static String getFile() {
        String file = Configs.CONFIG.getProperty("local.testdata");
        logger.info("Using testdata file: " + file);
        return file;
    }

    static Sheet getSheet(String file, int index) {
        File testData = new File(file);
        try (
                FileInputStream fis = new FileInputStream(testData);
                Workbook workbook = new XSSFWorkbook(fis)
        ) {
            return workbook.getSheetAt(index);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    static String getNullSafeStringValue(Row row, int index) {
        try {
            Cell cell = row.getCell(index);
            if (cell.getCellType() == CellType.NUMERIC) {
                return String.valueOf(cell.getNumericCellValue());
            } else if (cell.getCellType() == CellType.BOOLEAN) {
                return String.valueOf(cell.getBooleanCellValue());
            } else {
                return cell.getStringCellValue();
            }
        } catch (NullPointerException e) {
            return "";
        }
    }

    static String getSafeStringValue(List<String> row, int index) {
        try {
            return row.get(index);
        } catch (IndexOutOfBoundsException e) {
            logger.debug("Failed to get " + index + " element from " + row);
            return "";
        }
    }

    static List<Map<String, String>> getParams(String row) {
        List<Map<String, String>> params = new LinkedList<>();
        if (!row.isBlank()) {
            String[] maps = row.split(";");
            for (int i = 0; i < maps.length; i++) {
                maps[i] = maps[i].trim();
                maps[i] = maps[i].replace("(", "").replace(")", "");
            }
            for (String map : maps) {
                Map<String, String> kvMap = new HashMap<>();
                String[] keyValues = map.split(",");
                for (int j = 0; j < keyValues.length; j++) {
                    keyValues[j] = keyValues[j].trim();
                }
                Arrays.stream(keyValues).forEach(kv -> {
                    if (kv.contains(":")) {
                        String[] tokens = kv.split(":", 2);
                        if (tokens.length == 2) {
                            String key = tokens[0];
                            String value = tokens[1];
                            kvMap.put(key, value);
                        }
                    }
                });
                params.add(kvMap);
            }
        }
        return params;
    }

    static List<TestDataElement> filterTestData(List<TestDataElement> rawList) {

        String module = Configs.MODULE.toLowerCase();
        String tribe = Configs.TRIBE.toLowerCase();
        String testName = Configs.TEST_NAME.toLowerCase();

        Predicate<TestDataElement> matchingTribe = tde -> "all".equalsIgnoreCase(tribe) || tribe.equalsIgnoreCase(tde.tribe().trim().replaceAll(" ", ""));
        Predicate<TestDataElement> matchingModule = tde -> "all".equalsIgnoreCase(module) || module.equalsIgnoreCase(tde.module().trim().replaceAll(" ", ""));
        Predicate<TestDataElement> matchingTestName = tde -> "all".equalsIgnoreCase(testName) || testName.equalsIgnoreCase(tde.title());

        Predicate<TestDataElement> matchingForSanity = tde -> Configs.TEST_TYPE == TestType.SANITY && tde.sanity();
        Predicate<TestDataElement> matchingForRegression = tde -> Configs.TEST_TYPE == TestType.REGRESSION && tde.regression();
        Predicate<TestDataElement> matchingTestType = tde -> Configs.TEST_TYPE == TestType.ALL || matchingForSanity.test(tde) || matchingForRegression.test(tde);

        Predicate<TestDataElement> matchingForGk = tde -> Configs.ENVIRONMENT == Environment.GK && tde.gk();
        Predicate<TestDataElement> matchingForProd = tde -> Configs.ENVIRONMENT == Environment.PROD && tde.prod();
        Predicate<TestDataElement> matchingForPreProd = tde -> Configs.ENVIRONMENT == Environment.PREPROD && tde.preprod();
        Predicate<TestDataElement> matchingEnvironment = tde -> matchingForGk.test(tde) || matchingForProd.test(tde) || matchingForPreProd.test(tde);

        Predicate<TestDataElement> matchingPlatform = tde -> Configs.PLATFORM.name().equalsIgnoreCase(tde.platform());

        return rawList.stream()
                .sorted(Comparator.comparing(TestDataElement::title))
                .filter(matchingTribe)
                .filter(matchingModule)
                .filter(matchingTestName)
                .filter(TestDataElement::shouldRun)
                .filter(matchingTestType)
                .filter(matchingEnvironment)
                .filter(matchingPlatform)
                .toList();
    }

    static void setFilteredParams(List<TestDataElement> filteredTestDataElements) {
        filteredTestDataElements.forEach(tde -> {
            switch (Configs.ENVIRONMENT) {
                case GK -> putParamsInMap(tde.title(), tde.gkParams());
                case PREPROD -> putParamsInMap(tde.title(), tde.preprodParams());
                case PROD -> putParamsInMap(tde.title(), tde.prodParams());
                default -> throw new RuntimeException("Unknown environment");
            }
        });
    }

    private static void putParamsInMap(String title, List<Map<String, String>> params) {
        if (TestDataElement.titleParams.containsKey(title)) {
            List<Map<String, String>> mParams = new ArrayList<>();
            mParams.addAll(TestDataElement.titleParams.get(title).stream().toList());
            mParams.addAll(params);
            TestDataElement.titleParams.put(title, mParams);
        } else {
            TestDataElement.titleParams.put(title, params.stream().toList());
        }
    }

    T getData();
}
