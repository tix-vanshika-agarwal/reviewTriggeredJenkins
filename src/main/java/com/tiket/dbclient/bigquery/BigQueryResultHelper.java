package com.tiket.dbclient.bigquery;

import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;

public class BigQueryResultHelper {

    private static final Logger logger =
            LogManager.getLogger(BigQueryResultHelper.class);

    public static void logSummary(TableResult result) {
        logger.info("BigQuery rows returned: {}", result.getTotalRows());
    }

    public static void logFirstRow(TableResult result) {
        Iterator<FieldValueList> it = result.iterateAll().iterator();
        if (it.hasNext()) {
            logger.info("BigQuery first row: {}", it.next());
        }
    }

    public static boolean hasAnyRow(TableResult result) {
        return result.iterateAll().iterator().hasNext();
    }

    public static boolean anyRowContains(
            TableResult result,
            String columnName,
            String expectedValue
    ) {
        for (FieldValueList row : result.iterateAll()) {
            if (row.get(columnName).getStringValue().contains(expectedValue)) {
                return true;
            }
        }
        return false;
    }

    public static boolean anyRowMatchesTemplate(
            TableResult result,
            NotificationTemplateType templateType
    ) {
        for (FieldValueList row : result.iterateAll()) {
            String request = getString(row, "request");
            if (templateType.matches(request)) {
                return true;
            }
        }
        return false;
    }

    private static String getString(FieldValueList row, String field) {
        FieldValue value = row.get(field);
        return value == null || value.isNull() ? null : value.getStringValue();
    }
}
