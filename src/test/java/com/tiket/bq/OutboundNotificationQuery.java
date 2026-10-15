package com.tiket.bq;

import com.tiket.dbclient.bigquery.NotificationChannel;
import com.tiket.dbclient.bigquery.SqlTemplate;
import com.tiket.testbase.Configs;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OutboundNotificationQuery {

    private static final String TEMPLATE_PATH = "src/test/resources/sql/outbound_notification.sql";

    private static final String PROJECT_ID = Configs.CREDENTIALS.getProperty("google.project.id");

    private static final String DATASET = Configs.CONFIG.getProperty("bigquery.dataset.outbound");

    private static final String TABLE = Configs.CONFIG.getProperty("bigquery.table.outbound");

    private static final int DEFAULT_LIMIT = Integer.parseInt(Configs.CONFIG.getProperty("bigquery.query.limit", "100"));

    private static String fullTableName() {
        return String.format("%s.%s.%s", PROJECT_ID, DATASET, TABLE);
    }

    public static String build(
            String identity,
            NotificationChannel channel,
            String requestKey,
            LocalDate date
    ) {

        try {
            String template = Files.readString(Paths.get(TEMPLATE_PATH));

            List<String> activityTypes = channel.getActivityTypes();

            Map<String, String> params = Map.of(
                    "TABLE", fullTableName(),
                    "DATE", date.toString(),
                    "IDENTITY", identity,
                    "ACTIVITY_TYPES", activityTypes.stream()
                            .map(a -> "'" + a + "'")
                            .collect(Collectors.joining(",")),
                    "REQUEST_KEY", requestKey,
                    "LIMIT", String.valueOf(DEFAULT_LIMIT)
            );

            return SqlTemplate.resolve(template, params);

        } catch (Exception e) {
            throw new RuntimeException("Failed to build outbound notification SQL", e);
        }
    }
}
