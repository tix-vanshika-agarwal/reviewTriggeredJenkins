package com.tiket.dbclient.bigquery;

import java.util.Map;

public class SqlTemplate {

    public static String resolve(String template, Map<String, String> params) {

        String sql = template;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sql = sql.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return sql;
    }
}
