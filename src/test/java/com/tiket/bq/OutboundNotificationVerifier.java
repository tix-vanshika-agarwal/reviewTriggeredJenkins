package com.tiket.bq;

import com.google.cloud.bigquery.TableResult;
import com.tiket.dbclient.bigquery.BigQueryExecutor;
import com.tiket.dbclient.bigquery.BigQueryResultHelper;
import com.tiket.dbclient.bigquery.NotificationChannel;
import com.tiket.dbclient.bigquery.NotificationTemplateType;
import com.tiket.testbase.Assertion;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;

public class OutboundNotificationVerifier {

    private OutboundNotificationVerifier() {
        // prevent instantiation
    }

    public static void verify(
            String identity,
            String orderId,
            NotificationChannel channel
    ) {

        verify(identity, orderId, channel, null);
    }

    public static void verify(
            String identity,
            String orderId,
            NotificationChannel channel,
            NotificationTemplateType templateType
    ) {

        TableResult result = OutboundNotificationFetcher.fetch(
                identity,
                channel,
                orderId,
                LocalDate.now()
        );

        // basic validation
        Assertion.assertThat(
                "Outbound notification should exist",
                BigQueryResultHelper.hasAnyRow(result),
                is(true)
        );

        // EMAIL-only template validation
        if (channel == NotificationChannel.EMAIL && templateType != null) {

            Assertion.assertThat(
                    "Email template should match: " + templateType,
                    BigQueryResultHelper.anyRowMatchesTemplate(result, templateType),
                    is(true)
            );
        }
    }

    public static void verifyCurrency(
            String identity,
            String orderId,
            NotificationChannel channel,
            String expectedCurrency
    ) {
        String sql = OutboundNotificationQuery.build(
                identity,
                channel,
                orderId,
                LocalDate.now()
        );

        TableResult result = BigQueryExecutor.executeWithRetry(sql);

        BigQueryResultHelper.logSummary(result);
        BigQueryResultHelper.logFirstRow(result);

        Assertion.assertThat(
                "Outbound notification should be recorded",
                BigQueryResultHelper.hasAnyRow(result),
                is(true)
        );

        Assertion.assertThat(
                "Notification should contain currency: " + expectedCurrency,
                BigQueryResultHelper.anyRowContains(result, "request", expectedCurrency),
                is(true)
        );
    }
}
