package com.tiket.bq;

import com.google.cloud.bigquery.TableResult;
import com.tiket.dbclient.bigquery.BigQueryExecutor;
import com.tiket.dbclient.bigquery.BigQueryResultHelper;
import com.tiket.dbclient.bigquery.NotificationChannel;

import java.time.LocalDate;

public class OutboundNotificationFetcher {

    private OutboundNotificationFetcher() {}

    public static TableResult fetch(
            String identity,
            NotificationChannel channel,
            String orderId,
            LocalDate date
    ) {

        String sql = OutboundNotificationQuery.build(
                identity,
                channel,
                orderId,
                date
        );

        TableResult result = BigQueryExecutor.executeWithRetry(sql);

        BigQueryResultHelper.logSummary(result);
        BigQueryResultHelper.logFirstRow(result);

        return result;
    }
}
