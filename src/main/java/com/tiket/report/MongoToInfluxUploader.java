package com.tiket.report;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tiket.core.CoreFunctions;
import com.tiket.testbase.Configs;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static com.mongodb.client.model.Filters.*;

public class MongoToInfluxUploader {
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger(MongoToInfluxUploader.class);

    // ====== CONFIG ======
    // MongoDB
    private static final Map<String, String> klovConfig = getKlovConfigSafely();
    private static final String MONGO_URI = klovConfig != null ? klovConfig.get("mongoUri") : "";
    private static final String MONGO_DB = "klov";
    private static final String MONGO_COLLECTION = "test";

    private static Map<String, String> getKlovConfigSafely() {
        try {
            return CoreFunctions.getKlovAndMongoData();
        } catch (Exception e) {
            logger.error("Failed to get Klov configuration: " + e.getMessage(), e);
            return null;
        }
    }
    // InfluxDB
    private static final String INFLUX_BASE_URL = Configs.CONFIG.getProperty("influxDbProd")+":"+Configs.CONFIG.getProperty("influxDbPortProd");
    private static final String INFLUX_ORG = Configs.CREDENTIALS.getProperty("influx.org");
    private static final String INFLUX_BUCKET = Configs.CREDENTIALS.getProperty("influx.bucket");
    private static final String INFLUX_TOKEN = Configs.CREDENTIALS.getProperty("influx.token");
    // Measurement name in Influx
    private static final String MEASUREMENT = "automation_results";
    private static final String ACTIVE_PROJECT_NAME = CoreFunctions.getProjectName();

    public static void attachGrafanaReporter() {
        String projectName = ACTIVE_PROJECT_NAME;
        
        // Check for null first
        if (projectName == null || projectName.isEmpty()) {
            logger.warn("Project name is not set. Please configure properly.");
            return;
        }

        // Add null-safety check for MONGO_URI
        if (MONGO_URI == null || MONGO_URI.isEmpty()) {
            logger.error("MongoDB URI is not configured. Cannot attach Grafana reporter.");
            return;
        }

        try (MongoClient mongoClient = MongoClients.create(MONGO_URI)) {
            MongoDatabase db = mongoClient.getDatabase(MONGO_DB);
            MongoCollection<Document> testCollection = db.getCollection(MONGO_COLLECTION);
            
            // Resolve project _id from project name
            MongoCollection<Document> projectCollection = db.getCollection("project");
            Document projectDoc = projectCollection.find(eq("name", projectName)).first();
            
            if (projectDoc == null || !projectDoc.containsKey("_id")) {
                logger.warn("No project found with name: " + projectName);
                return;
            }
            
            ObjectId projectId = projectDoc.getObjectId("_id");
            
            // Build date range for "current date" (today in system default timezone)
            LocalDate today = LocalDate.now();
            Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
            //Date startOfDay = Date.from(today.minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date startOfTomorrow = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // Filter tests for this project executed today, with required
            // fields present. Both root test docs AND child step / assertion
            // docs are uploaded - the Grafana "Screen_*" panels count steps
            // by filtering on parentName == "<root test name>", so we MUST
            // emit child rows. The HTML wrapper on step names
            // ("<h4 style='color: blue'>Booking page</h4>") is harmless: it
            // becomes part of the 'name' tag and the dashboard's Flux query
            // strips the markup with strings.replaceAll before display.
            FindIterable<Document> docs = testCollection.find(and(
                    eq("project", projectId),
                    gte("startTime", startOfDay),
                    lt("startTime", startOfTomorrow),
                    exists("name"),
                    exists("status"),
                    exists("duration"),
                    exists("_id")
            ));
            
            StringBuilder lpBuilder = new StringBuilder();
            for (Document doc : docs) {
                String line = toLineProtocol(doc);
                if (line != null && !line.isEmpty()) {
                    lpBuilder.append(line).append("\n");
                }
            }
            
            String body = lpBuilder.toString().trim();
            if (body.isEmpty()) {
                logger.warn("No documents mapped, nothing to send to Influx.");
                return;
            }
            
            logger.info("Prepared line protocol:\n" + body);
            writeToInflux(body);
            
        } catch (Exception e) {
            logger.error("Failed to attach Grafana reporter: " + e.getMessage(), e);
        }
    }

    private static String toLineProtocol(Document doc) {
        try {
            String name = doc.getString("name");
            String status = doc.getString("status");
            Number durationNum = (Number) doc.get("duration");
            if (name == null || status == null || durationNum == null) {
                return null; // skip incomplete docs
            }
            long duration = durationNum.longValue(); // assuming already in ms or your unit
            Date startTime = doc.getDate("startTime");
            if (startTime == null) {
                return null; // or log and skip
            }
            long timestampMs = startTime.getTime();  // epoch millis (UTC)
            // We'll write with precision=ms

            // Klov stores two kinds of docs in the same collection:
            //   - Root test docs:  name = "AccommodationTest.completeBookingFoo"
            //                      (TestNG FQN, sometimes "Class.method", sometimes
            //                      bare method, sometimes with a "_<epoch>" suffix).
            //   - Child step docs: name = "<h4 style='color: blue'>Open About
            //                      Tiket.com</h4>"  (HTML-wrapped step description
            //                      from ExtentLogger.step). These can legitimately
            //                      contain dots (e.g. "Tiket.com") that must NOT be
            //                      treated as class-method separators.
            // We detect children via the `parent` ObjectId reference and skip the
            // class-prefix / run-id / platform-suffix pipeline for them - their
            // names pass through verbatim and the dashboard's Flux query strips
            // the HTML wrapper at render time.
            boolean isChild = doc.get("parent") != null;

            String adjustedName;
            if (isChild) {
                adjustedName = name;
            } else {
                // Root test: full normalization so historical series keep merging
                // across Klov format changes.
                //   1. extractMethodName: parses both new "fqcn#method@..."
                //      and legacy "Class.method" formats down to bare method.
                //   2. getBaseTestName:  drops a trailing "_<digits>" run-id.
                //   3. appendPlatformSuffix: "_android" / "_ios" / "_panel" for
                //      mobile + panel; web / mWeb keep the bare name (their method
                //      names already encode the platform - "...Web", "...Dweb").
                String baseName = getBaseTestName(extractMethodName(name));
                adjustedName = appendPlatformSuffix(baseName);
            }

            // Normalize parentName whenever it is present. Unlike a step's own
            // `name`, parentName always refers to a TestNG method (never an HTML
            // step description), so it is safe to run through the full pipeline.
            // The "Screen_*" Grafana panels filter on
            // `r.parentName == "<bare test name>"` for web and
            // `... == "<bare test name>_android"` for mobile, so we must produce
            // the exact same value as the parent doc's normalized `name`.
            String parentNameTag = "";
            if (doc.containsKey("parentName")) {
                String parentName = doc.getString("parentName");
                if (parentName != null && !parentName.isEmpty()) {
                    String baseParentName = getBaseTestName(extractMethodName(parentName));
                    String adjustedParentName = appendPlatformSuffix(baseParentName);
                    parentNameTag = ",parentName=" + escapeTagValue(adjustedParentName);
                }
            }
            // Escape tag values per Influx line protocol rules
            String escapedName = escapeTagValue(adjustedName);
            String escapedStatus = escapeTagValue(status);
            // measurement,tag1=...,tag2=... field1=... <timestamp>
            // duration as integer field -> add 'i' suffix
            return MEASUREMENT +
                    ",name=" + escapedName +
                    ",status=" + escapedStatus +
                    parentNameTag +
                    " duration=" + duration + "i " + timestampMs;
        } catch (Exception e) {
            logger.error("Failed to convert document to line protocol: {}", e.getMessage(), e);
            return null;
        }
    }

    // Escape spaces, commas, and equals in tag values per Influx line
    // protocol. Also collapse newlines / tabs / carriage-returns to a single
    // space first: Klov assertion-log documents sometimes carry multi-line
    // names (HTML <br/> rendered with embedded \n) and a raw newline mid-tag
    // would terminate the line-protocol record, causing Influx to reject the
    // batch with "missing fields" 400.
    private static String escapeTagValue(String value) {
        if (value == null) return "";
        return value
                .replaceAll("[\\r\\n\\t]+", " ")
                .replaceAll(" {2,}", " ")
                .replace("\\", "\\\\")
                .replace(" ", "\\ ")
                .replace(",", "\\,")
                .replace("=", "\\=");
    }
    /**
     * Appends the platform suffix used to disambiguate the {@code name} tag in
     * InfluxDB.
     * <p>
     * Why mobile gets a suffix but web does not: on mobile the same test
     * method runs on both iOS and Android (e.g.
     * {@code testDomesticOnewayDirectLoginProdMonitoringFlight}), so the
     * suffix is the only thing that lets a Grafana panel target one
     * platform's series. On web/mWeb the test method names already encode
     * the platform - {@code completeBookingIdrLoginProdMonitoringHotelWeb},
     * {@code testFlightDomesticRoundTripDweb}, etc. - so adding {@code _dweb}
     * etc. on top is redundant and would break the dashboards that were
     * built when the legacy uploader produced the bare-name form.
     * <p>
     * Suffix mapping (driven off {@code -Dplatform=...}, the same value used
     * everywhere else in the framework):
     * <ul>
     *   <li>{@code android} -&gt; {@code _android}
     *   <li>{@code ios}     -&gt; {@code _ios}
     *   <li>{@code panel}   -&gt; {@code _panel}
     *     (no legacy panel was tuned to a bare-panel name, so this is safe to add)
     *   <li>{@code dweb} / {@code amweb} / {@code imweb} -&gt; no suffix
     *     (preserves backward compatibility with existing {@code *_Web} dashboards)
     *   <li>anything else / missing -&gt; no suffix (fail open)
     * </ul>
     */
    private static String appendPlatformSuffix(String baseName) {
        if (baseName == null) {
            return null;
        }
        String suffix = resolvePlatformSuffix();
        return suffix.isEmpty() ? baseName : baseName + suffix;
    }

    private static String resolvePlatformSuffix() {
        String raw = System.getProperty("platform", "");
        if (raw == null || raw.isEmpty()) {
            return "";
        }
        return switch (raw.toLowerCase()) {
            case "android" -> "_android";
            case "ios"     -> "_ios";
            case "panel"   -> "_panel";
            // Web/mWeb intentionally fall through to no suffix - test method
            // names already carry the "Web"/"Dweb"/"Mweb" disambiguator,
            // and existing Grafana dashboards were built for that form.
            default        -> "";
        };
    }

    /**
     * Extracts the bare TestNG method name from a Klov test-doc {@code name}
     * field, stripping class qualifiers and uniqueness suffixes added by the
     * Extent report layer.
     * <p>
     * Supported formats:
     * <ul>
     *   <li><b>Current:</b> {@code fqcn#method@paramHash@nanoTime@seq}
     *       (e.g. {@code com.tiket.accommodation.AccommodationTest#completeBookingIdr@123@999@1})
     *       &rarr; returns {@code completeBookingIdr}
     *   <li><b>Legacy dot-separated:</b> {@code Class.method} or {@code fqcn.method}
     *       &rarr; returns the substring after the last dot
     *   <li><b>Bare method name:</b> returned as-is (may still carry a
     *       {@code _<epoch>} run-id suffix which is stripped separately by
     *       {@link #getBaseTestName(String)})
     * </ul>
     * <p>
     * <b>Caller contract:</b> only invoke on root-test or parentName values
     * (never on HTML-wrapped step names).
     */
    private static String extractMethodName(String name) {
        if (name == null) {
            return null;
        }

        // Current format: "fqcn#method@paramHash@nanoTime@seq"
        int hashIdx = name.indexOf('#');
        if (hashIdx >= 0) {
            String afterHash = name.substring(hashIdx + 1);
            int atIdx = afterHash.indexOf('@');
            if (atIdx > 0) {
                return afterHash.substring(0, atIdx);
            }
            return afterHash;
        }

        // Legacy format: "Class.method" or just "method"
        int lastDot = name.lastIndexOf('.');
        if (lastDot < 0 || lastDot == name.length() - 1) {
            return name;
        }
        return name.substring(lastDot + 1);
    }

    /**
     * Returns the base test name by stripping a numeric suffix after the last underscore.
     * Example: "testBookBusOnewayLoginProdMonitoring_1764856738080" -> "testBookBusOnewayLoginProdMonitoring"
     */
    private static String getBaseTestName(String name) {
        if (name == null) {
            return "";
        }
        int lastUnderscore = name.lastIndexOf('_');
        if (lastUnderscore <= 0 || lastUnderscore == name.length() - 1) {
            return name;
        }
        String suffix = name.substring(lastUnderscore + 1);
        // If everything after the last underscore is digits, strip it
        if (suffix.chars().allMatch(Character::isDigit)) {
            return name.substring(0, lastUnderscore);
        }
        return name;
    }
    private static void writeToInflux(String lineProtocolBody) {
        logger.info("Writing to InfluxDB at " + INFLUX_BASE_URL);
        try {
            String orgEncoded = URLEncoder.encode(INFLUX_ORG, StandardCharsets.UTF_8);
            String bucketEncoded = URLEncoder.encode(INFLUX_BUCKET, StandardCharsets.UTF_8);
            String writePath = String.format(
                    "/api/v2/write?org=%s&bucket=%s&precision=ms",
                    orgEncoded, bucketEncoded
            );
            Response response = RestAssured
                    .given()
                    .baseUri(INFLUX_BASE_URL)
                    .header("Authorization", "Token " + INFLUX_TOKEN)
                    .contentType("text/plain; charset=utf-8")
                    .body(lineProtocolBody)
                    .when()
                    .post(writePath)
                    .andReturn();
            int statusCode = response.statusCode();
            String respBody = response.getBody().asString();
            logger.info("Influx write status: " + statusCode);
            if (!respBody.isEmpty()) {
                logger.info("bucket: " + INFLUX_BUCKET + ", org: " + INFLUX_ORG + ", measurement: " + MEASUREMENT);
                logger.info("Influx response body: " + respBody);
            }
            if (statusCode != 204) {
                throw new RuntimeException("Failed to write to Influx, status=" + statusCode);
            }
        } catch (Exception e) {
            logger.error("Failed to write to Influx: " + e.getMessage(), e);
        }
    }
}
