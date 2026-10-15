package com.tiket.postprocessing;

import com.tiket.core.CoreFunctions;
import com.tiket.io.local.PropertiesReader;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Properties;

public class GrafanaEmailSender {

    private static final Logger logger = LogManager.getLogger(GrafanaEmailSender.class);
    private static final Properties PROPERTIES = PropertiesReader.read("src/main/resources/email.properties");

    /**
     * Resolves the comma-separated recipient list for a given vertical.
     *
     * Resolution rules (generic - no code changes needed for new verticals):
     *   - Global key:   recipients              -> always included (sent for all verticals)
     *   - Vertical key: recipients.<vertical>   -> added on top when that vertical runs
     *
     * Examples:
     *   recipients=team@tiket.com
     *   recipients.accommodation=accommodation-team@tiket.com
     *   recipients.flight=flight-team@tiket.com
     *   recipients.ttd=ttd-team@tiket.com
     *
     * Adding a new vertical (e.g. "train") only requires adding:
     *   recipients.train=train-team@tiket.com
     *
     * Duplicates are removed and ordering is preserved (global first, then vertical-specific).
     */
    public static String resolveRecipients(String verticalName) {
        LinkedHashSet<String> all = new LinkedHashSet<>();
        addRecipients(all, Configs.PROPERTIES_DATA.get("recipients"));

        // Reject whitespace-only verticalName too, otherwise we'd look up the key
        // "recipients." and accidentally hit unrelated properties.
        String normalizedVertical = (verticalName == null) ? null : verticalName.trim().toLowerCase();
        if (normalizedVertical != null && !normalizedVertical.isEmpty()) {
            String verticalKey = "recipients." + normalizedVertical;
            addRecipients(all, Configs.PROPERTIES_DATA.get(verticalKey));
        }

        return String.join(",", all);
    }

    private static void addRecipients(LinkedHashSet<String> sink, String csv) {
        if (csv == null || csv.trim().isEmpty()) {
            return;
        }
        for (String email : csv.split(",")) {
            String trimmed = email.trim();
            if (!trimmed.isEmpty()) {
                sink.add(trimmed);
            }
        }
    }

    /**
     * Sends Grafana monitoring report via email with embedded images
     *
     * @param imagePaths List of file paths for Grafana screenshots
     * @param verticalName Name of the vertical (e.g., "accommodation")
     * @param platforms Array of platform names
     * @param metricTypes Array of metric type names
     * @param dashboardId Grafana dashboard ID
     * @param orgId Organization ID
     * @param panelIds Map of platform to metric type to panel ID
     * @param timezone Timezone to use in dashboard links (e.g., "Asia/Singapore")
     */
    @SneakyThrows
    public static void sendGrafanaEmail(ArrayList<String> imagePaths, String verticalName,
                                        String[] platforms, String[] metricTypes,
                                        String dashboardId, int orgId,
                                        java.util.Map<String, java.util.Map<String, Integer>> panelIds,
                                        String timezone) {
        // Back-compat overload: resolve recipients internally and use legacy "Last 24 hours" window.
        sendGrafanaEmail(imagePaths, verticalName, platforms, metricTypes,
                dashboardId, orgId, panelIds, timezone, resolveRecipients(verticalName),
                "now-24h", "now", "Last 24 hours", null);
    }

    @SneakyThrows
    public static void sendGrafanaEmail(ArrayList<String> imagePaths, String verticalName,
                                        String[] platforms, String[] metricTypes,
                                        String dashboardId, int orgId,
                                        java.util.Map<String, java.util.Map<String, Integer>> panelIds,
                                        String timezone, String recipients) {
        // Back-compat overload (no time-window/theme args): legacy "Last 24 hours" defaults.
        sendGrafanaEmail(imagePaths, verticalName, platforms, metricTypes,
                dashboardId, orgId, panelIds, timezone, recipients,
                "now-24h", "now", "Last 24 hours", null);
    }

    @SneakyThrows
    public static void sendGrafanaEmail(ArrayList<String> imagePaths, String verticalName,
                                        String[] platforms, String[] metricTypes,
                                        String dashboardId, int orgId,
                                        java.util.Map<String, java.util.Map<String, Integer>> panelIds,
                                        String timezone, String recipients,
                                        String from, String to, String reportPeriodLabel) {
        // Back-compat overload (no theme arg): delegate with null theme so the dashboard URL
        // uses the user's Grafana profile theme.
        sendGrafanaEmail(imagePaths, verticalName, platforms, metricTypes,
                dashboardId, orgId, panelIds, timezone, recipients,
                from, to, reportPeriodLabel, null);
    }

    /**
     * Primary entry point. Use this overload to guarantee that:
     *  - the audit log (in the caller) and the actual {@code To:} header are the same resolution
     *  - the captured snapshots, the click-through dashboard URL, the "Report Period" label,
     *    and the rendering theme in the email body all use the SAME values.
     *
     * @param recipients         comma-separated email list. Must be non-null and non-empty.
     * @param from               Grafana time-range start (e.g. "now-7d"). Used in dashboard URL.
     * @param to                 Grafana time-range end   (e.g. "now-2d"). Used in dashboard URL.
     * @param reportPeriodLabel  Human-readable label for the email body (e.g. "Last 7 days
     *                           (excluding last 2 days)"). Pass null/blank to use a generic label.
     * @param theme              Render theme for the click-through dashboard URL ("light" / "dark"
     *                           / "system"). Pass null/blank to omit the {@code &theme=} param and
     *                           use the user's Grafana profile theme.
     */
    @SneakyThrows
    public static void sendGrafanaEmail(ArrayList<String> imagePaths, String verticalName,
                                        String[] platforms, String[] metricTypes,
                                        String dashboardId, int orgId,
                                        java.util.Map<String, java.util.Map<String, Integer>> panelIds,
                                        String timezone, String recipients,
                                        String from, String to, String reportPeriodLabel,
                                        String theme) {

        if (imagePaths == null || imagePaths.isEmpty()) {
            logger.warn("No Grafana images to send via email");
            return;
        }

        if (recipients == null || recipients.isEmpty()) {
            String verticalKey = verticalName == null ? "<vertical>" : verticalName.toLowerCase();
            String msg = "No recipients resolved for vertical '" + verticalName + "'. "
                    + "Configure 'recipients' (global) and/or 'recipients." + verticalKey + "' in properties.";
            logger.error(msg);
            throw new IllegalStateException(msg);
        }

        logger.info("Preparing to send Grafana monitoring email...");

        Properties p = System.getProperties();
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.port", "465");
        p.put("mail.smtp.ssl.enable", "true");
        p.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        PROPERTIES.getProperty("email"),
                        PROPERTIES.getProperty("password")
                );
            }
        });
        session.setDebug(false);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(PROPERTIES.getProperty("email"));
        message.setRecipients(Message.RecipientType.TO, recipients);
        message.setSubject(createGrafanaSubject(verticalName));

        Multipart multipart = new MimeMultipart();

        // Add HTML body with dashboard links
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(createGrafanaEmailHTML(imagePaths.size(), verticalName, platforms, metricTypes,
                dashboardId, orgId, panelIds, timezone, from, to, reportPeriodLabel, theme),
                "text/html");
        multipart.addBodyPart(htmlPart);

        // Add inline images
        int index = 1;
        for (String path : imagePaths) {
            if (path != null && !path.isEmpty()) {
                File f = new File(path);
                if (f.exists()) {
                    MimeBodyPart imagePart = new MimeBodyPart();
                    imagePart.setHeader("Content-ID", "<GrafanaImg" + index + ">");
                    imagePart.setDisposition(MimeBodyPart.INLINE);
                    imagePart.attachFile(path);
                    multipart.addBodyPart(imagePart);
                    logger.info("Added image {} to email: {}", index, f.getName());
                    index++;
                } else {
                    logger.warn("Image file not found: {}", path);
                }
            }
        }

        message.setContent(multipart);
        Transport.send(message);
        logger.info("Grafana monitoring email sent successfully to: {}", recipients);
    }

    /**
     * Creates email subject line for Grafana report
     */
    private static String createGrafanaSubject(String verticalName) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        String environment = CoreFunctions.isJenkinsSystem() ?
                Configs.ENVIRONMENT.name() : "Local";

        return String.format("Grafana Monitoring Report - %s - %s - %s",
                capitalizeFirst(verticalName),
                environment,
                timestamp);
    }

    /**
     * Creates HTML body for Grafana monitoring email with clickable dashboard links
     */
    private static String createGrafanaEmailHTML(int imageCount, String verticalName,
                                                 String[] platforms, String[] metricTypes,
                                                 String dashboardId, int orgId,
                                                 java.util.Map<String, java.util.Map<String, Integer>> panelIds,
                                                 String timezone,
                                                 String from, String to, String reportPeriodLabel,
                                                 String theme) {
        StringBuilder html = new StringBuilder();

        // Get Grafana base URL from config
        String grafanaBase = Configs.CONFIG.getProperty("grafanaProd") + ":" +
                             Configs.CONFIG.getProperty("grafanaPortProd");
        
        // URL encode the timezone (e.g., "Asia/Singapore" becomes "Asia%2FSingapore")
        String encodedTimezone = timezone.replace("/", "%2F");

        // URL encode from/to (Grafana accepts relative expressions like "now-7d" verbatim,
        // but slashes in absolute timestamps and rounding suffixes need encoding).
        String safeFrom = (from == null || from.isBlank()) ? "now-24h" : from.trim();
        String safeTo = (to == null || to.isBlank()) ? "now" : to.trim();
        String encodedFrom = safeFrom.replace("/", "%2F");
        String encodedTo = safeTo.replace("/", "%2F");

        // Optional theme query suffix for the click-through dashboard URL.
        // Kept blank when no theme is configured so we don't override the user's Grafana profile theme.
        String themeQuerySuffix = (theme == null || theme.isBlank())
                ? ""
                : "&theme=" + theme.trim();

        // Friendly label for the email body. Falls back to a generic phrase if not provided.
        // HTML-escaped because the value can come from properties / auto-derived strings and
        // is interpolated into the email markup.
        String rawPeriodLabel = (reportPeriodLabel == null || reportPeriodLabel.isBlank())
                ? ("From " + safeFrom + " to " + safeTo)
                : reportPeriodLabel;
        String periodLabel = escapeHtml(rawPeriodLabel);
        String safeVerticalLabel = escapeHtml(capitalizeFirst(verticalName));

        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }");
        html.append(".container { max-width: 900px; margin: 20px auto; background-color: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        html.append(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; }");
        html.append(".header h1 { margin: 0; font-size: 24px; }");
        html.append(".content { padding: 30px; }");
        html.append(".metric-section { margin-bottom: 40px; }");
        html.append(".metric-section h2 { color: #667eea; border-bottom: 2px solid #667eea; padding-bottom: 10px; margin-bottom: 20px; }");
        html.append(".image-container { margin: 20px 0; text-align: center; background: #f9f9f9; padding: 15px; border-radius: 8px; }");
        html.append(".image-container img { max-width: 100%; height: auto; border: 1px solid #ddd; border-radius: 4px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); transition: transform 0.2s; }");
        html.append(".image-container a:hover img { transform: scale(1.02); }");
        html.append(".image-label { font-weight: 600; color: #555; margin-bottom: 10px; font-size: 16px; }");
        html.append(".dashboard-link { display: inline-block; margin-top: 10px; padding: 8px 16px; background: #667eea; color: white; text-decoration: none; border-radius: 4px; font-size: 14px; transition: background 0.2s; }");
        html.append(".dashboard-link:hover { background: #5568d3; }");
        html.append(".footer { background-color: #f8f9fa; padding: 20px; text-align: center; border-top: 1px solid #e0e0e0; }");
        html.append(".footer p { margin: 5px 0; color: #666; font-size: 14px; }");
        html.append(".info-box { background: #e8f4f8; border-left: 4px solid #17a2b8; padding: 15px; margin: 20px 0; border-radius: 4px; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");

        // Header
        html.append("<div class='header'>");
        html.append("<h1>Grafana Monitoring Report</h1>");
        html.append("<p style='margin: 10px 0 0 0; font-size: 16px;'>").append(safeVerticalLabel).append(" Vertical</p>");
        html.append("</div>");

        // Content
        html.append("<div class='content'>");
        html.append("<div class='info-box'>");
        html.append("<p style='margin: 0;'><strong>Report Period:</strong> ").append(periodLabel).append("</p>");
        html.append("<p style='margin: 5px 0 0 0;'><strong>Generated:</strong> ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("</p>");
        html.append("<p style='margin: 5px 0 0 0;'><strong>Total Snapshots:</strong> ").append(imageCount).append("</p>");
        html.append("</div>");

        html.append("<p style='color: #555; line-height: 1.6; font-size: 16px;'>Below are the Grafana monitoring snapshots showing execution status, execution time, and screen metrics across all platforms (Dweb, Android, iOS) for <strong>").append(periodLabel).append("</strong>. Click on any image or the 'View in Grafana' button to open the interactive dashboard.</p>");

        // Images grouped by platform with clickable links
        int imageIndex = 1;
        for (int p = 0; p < platforms.length; p++) {
            String safePlatform = escapeHtml(platforms[p]);
            html.append("<div class='metric-section'>");
            html.append("<h2>").append(safePlatform).append(" Platform</h2>");

            for (int m = 0; m < metricTypes.length; m++) {
                // Get panel ID for this platform-metric combination
                Integer panelId = panelIds.get(platforms[p]).get(metricTypes[m]);
                String safeMetric = escapeHtml(metricTypes[m]);

                // Build dashboard URL with panel ID using the same timezone, time window AND theme
                // as the captured snapshots, so the click-through view matches the embedded image.
                // verticalName is interpolated into the URL path; escape for the href attribute as well.
                String dashboardUrl = String.format("%s/d/%s/%s?orgId=%d&from=%s&to=%s&timezone=%s&viewPanel=%d%s",
                        grafanaBase, dashboardId, verticalName, orgId, encodedFrom, encodedTo, encodedTimezone, panelId, themeQuerySuffix);
                String safeDashboardUrl = escapeHtml(dashboardUrl);

                html.append("<div class='image-container'>");
                html.append("<div class='image-label'>").append(safeMetric).append("</div>");

                // Wrap image in clickable link
                html.append("<a href='").append(safeDashboardUrl).append("' target='_blank' style='text-decoration: none;'>");
                html.append("<img src='cid:GrafanaImg").append(imageIndex).append("' alt='")
                        .append(safePlatform).append(" - ").append(safeMetric).append("'>");
                html.append("</a>");

                // Add "View in Grafana" button
                html.append("<br/>");
                html.append("<a href='").append(safeDashboardUrl).append("' class='dashboard-link' target='_blank'>View in Grafana</a>");

                html.append("</div>");
                imageIndex++;
            }

            html.append("</div>");
        }

        html.append("</div>");

        // Footer
        html.append("<div class='footer'>");
        html.append("<p style='font-weight: 600; color: #333;'>Cheers,</p>");
        html.append("<p style='font-weight: 600; color: #333;'>SDET Team</p>");
        html.append("<p style='margin-top: 15px;'>Need help? Contact <strong>@sdet_app</strong> on Slack</p>");
        html.append("</div>");

        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    /**
     * Helper method to capitalize first letter
     */
    private static String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Minimal HTML escaper for values interpolated into the email body and into HTML
     * attribute values that we delimit with single quotes.
     *
     * Escapes the five XML-significant characters plus the single quote so that values
     * coming from properties / test data cannot break the markup or inject content.
     * Returns an empty string for null input to keep the email body well-formed.
     */
    private static String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        StringBuilder out = new StringBuilder(input.length() + 16);
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            switch (c) {
                case '&': out.append("&amp;"); break;
                case '<': out.append("&lt;"); break;
                case '>': out.append("&gt;"); break;
                case '"': out.append("&quot;"); break;
                case '\'': out.append("&#39;"); break;
                default: out.append(c);
            }
        }
        return out.toString();
    }
}
