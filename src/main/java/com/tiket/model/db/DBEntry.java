package com.tiket.model.db;

import com.tiket.model.Environment;
import com.tiket.model.Platform;
import com.tiket.model.TestType;

public record DBEntry(
        Status status,
        String verticalName,
        String tribeName,
        String moduleName,
        Platform platform,
        TestType testType,
        Environment environment,
        String testRailID,
        String runID,
        long timestamp
) {
}
