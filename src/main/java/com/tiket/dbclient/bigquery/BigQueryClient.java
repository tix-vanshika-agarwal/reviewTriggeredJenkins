package com.tiket.dbclient.bigquery;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ImpersonatedCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.tiket.testbase.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;

public class BigQueryClient {

    private static final Logger logger = LogManager.getLogger(BigQueryClient.class);
    private static BigQuery bigQuery;

    private static final String TARGET_SERVICE_ACCOUNT = Configs.CREDENTIALS.getProperty("google.sa.email");
    private static final String BIGQUERY_SCOPE = "https://www.googleapis.com/auth/bigquery";

    public static synchronized BigQuery getClient() {

        if (bigQuery != null) {
            return bigQuery;
        }

        try {
            String projectId = Configs.CREDENTIALS.getProperty("google.project.id");
            if (projectId == null) {
                throw new RuntimeException("Missing google.project.id");
            }

            if (TARGET_SERVICE_ACCOUNT == null || TARGET_SERVICE_ACCOUNT.isBlank()) {
                throw new RuntimeException("Missing google.sa.email");
            }

            logger.debug("Using service account impersonation: {}", TARGET_SERVICE_ACCOUNT);

            GoogleCredentials sourceCredentials = GoogleCredentials.getApplicationDefault();
            GoogleCredentials credentials = ImpersonatedCredentials.newBuilder()
                    .setSourceCredentials(sourceCredentials)
                    .setTargetPrincipal(TARGET_SERVICE_ACCOUNT)
                    .setScopes(Collections.singletonList(BIGQUERY_SCOPE))
                    .build();

            bigQuery = BigQueryOptions.newBuilder()
                    .setProjectId(projectId)
                    .setCredentials(credentials)
                    .build()
                    .getService();

            logger.info("BigQuery client initialized for project: {}", projectId);

            return bigQuery;

        } catch (Exception e) {
            logger.error("Failed to init BigQuery client", e);
            throw new RuntimeException("Failed to init BigQuery client", e);
        }
    }
}
