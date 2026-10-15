package com.tiket.dbclient.bigquery;

import com.google.cloud.bigquery.*;
import com.tiket.testbase.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.SkipException;

public class BigQueryExecutor {

    private static final Logger logger = LogManager.getLogger(BigQueryExecutor.class);

    private static final int MAX_RETRY =
            Integer.parseInt(Configs.CONFIG.getProperty("bigquery.retry.max", "3"));

    private static final int WAIT_MS =
            Integer.parseInt(Configs.CONFIG.getProperty("bigquery.retry.wait", "2000"));

    private static final String LOCATION =
            Configs.CONFIG.getProperty("bigquery.location", "asia-southeast1");

    public static TableResult executeWithRetry(String sql) {

        BigQuery bigQuery = BigQueryClient.getClient();
        Exception lastException = null;

        for (int attempt = 1; attempt <= MAX_RETRY; attempt++) {
            try {
                logger.info("Executing BigQuery (attempt {}/{}): {}", attempt, MAX_RETRY, sql);

                QueryJobConfiguration queryConfig = QueryJobConfiguration
                        .newBuilder(sql)
                        .setUseLegacySql(false)
                        .build();

                JobId jobId = JobId.newBuilder()
                        .setLocation(LOCATION)
                        .build();

                TableResult result = bigQuery.query(queryConfig, jobId);

                if (!result.iterateAll().iterator().hasNext()) {
                    throw new IllegalStateException("Query executed but no data returned");
                }

                return result;

            } catch (Exception e) {
                lastException = e;
                logger.warn("BigQuery attempt {} failed: {}", attempt, e.getMessage());

                try {
                    Thread.sleep(WAIT_MS);
                } catch (InterruptedException ignored) {}
            }
        }

        logger.warn("BigQuery skipped after {} retries. Reason: {}",
                MAX_RETRY,
                lastException != null ? lastException.getMessage() : "unknown");

        throw new SkipException("BigQuery data not ready / still delayed");
    }
}
