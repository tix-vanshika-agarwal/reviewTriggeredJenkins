package com.tiket.postprocessing;

import com.sun.mail.smtp.SMTPSendFailedException;
import com.tiket.core.CoreFunctions;
import com.tiket.io.local.PropertiesReader;
import com.tiket.model.TestType;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static com.tiket.core.CoreFunctions.capitalize;

public class EmailProcessor {

    private static final Logger logger = LogManager.getLogger(EmailProcessor.class);
    private static final Properties PROPERTIES = PropertiesReader.read("src/main/resources/email.properties");
    private static final String TO = Configs.PROPERTIES_DATA.get("recipients");
    private static final String TEST_REPORT_ZIP_NAME = TestReportZipBuilder.TEST_REPORT_ZIP_NAME;
    // SMTP 552: message rejected for its content or size (e.g. Gmail 552-5.7.0 blocked attachment)
    private static final int SMTP_CONTENT_REJECTED = 552;
    private static final String ATTACHMENT_REJECTED_NOTE =
            "<p style='color: darkorange'><b>Note:</b> " + TEST_REPORT_ZIP_NAME
                    + " was rejected by the mail server and has been omitted from this email."
                    + " Please check the Extent Report link or the build artifacts.</p>";

    @FunctionalInterface
    private interface EmailFactory {
        MimeMessage create(boolean withAttachment) throws MessagingException, IOException;
    }

    @SneakyThrows
    public static void processEmail(String imageFile) {

        Session session = getSession(buildMailProperties(), true);
        sendWithAttachmentFallback(withAttachment -> createEmail(session, imageFile, withAttachment));
        logger.info("Email sent successfully");
    }

    /**
     * Send the fallback email when the primary report flow could not produce a
     * dashboard screenshot. The caller supplies a pre-built HTML block to use in
     * place of the screenshot and a pre-built tribe-wise summary table so this
     * path does not depend on Selenium (which is what failed in the first place).
     *
     * @param resultBlockHtml HTML block rendered in place of the dashboard image.
     *                        May be {@code null} or empty to render the standard
     *                        "image unavailable" placeholder.
     * @param tableSummary    HTML rows for the tribe-wise summary table. May be
     *                        {@code null} or empty to render the standard
     *                        "summary unavailable" placeholder.
     */
    @SneakyThrows
    public static void processFallbackEmail(String resultBlockHtml, String tableSummary) {
        Session session = getSession(buildMailProperties(), true);
        sendWithAttachmentFallback(withAttachment ->
                createFallbackEmail(session, resultBlockHtml, tableSummary, withAttachment));
        logger.info("Fallback email sent successfully");
    }

    /**
     * Sends the email with the test report attachment. If the mail server rejects the content (SMTP 552),
     * resends once without the attachment so the run result is still delivered.
     */
    private static void sendWithAttachmentFallback(EmailFactory factory) throws MessagingException, IOException {
        try {
            Transport.send(factory.create(true));
        } catch (MessagingException e) {
            if (!isContentRejected(e)) {
                throw e;
            }
            logger.warn("Mail server rejected the email content, resending without attachment: {}", e.getMessage());
            Transport.send(factory.create(false));
        }
    }

    private static boolean isContentRejected(Exception e) {
        Exception current = e;
        while (current != null) {
            if (current instanceof SMTPSendFailedException smtpException
                    && smtpException.getReturnCode() == SMTP_CONTENT_REJECTED) {
                return true;
            }
            current = current instanceof MessagingException messagingException
                    ? messagingException.getNextException() : null;
        }
        return false;
    }

    private static Properties buildMailProperties() {
        Properties p = System.getProperties();
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.port", "465");
        p.put("mail.smtp.ssl.enable", "true");
        p.put("mail.smtp.auth", "true");
        return p;
    }

    private static Session getSession(Properties p, boolean debug) {
        Session session = Session.getInstance(p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(PROPERTIES.getProperty("email"), PROPERTIES.getProperty("password"));
            }
        });
        session.setDebug(debug);
        return session;
    }

    private static MimeMessage createEmail(Session session, String imageFile, boolean withAttachment)
            throws MessagingException, IOException {
        String htmlBody = createHTMLBody(imageFile);
        if (!withAttachment) {
            htmlBody = withAttachmentRejectedNote(htmlBody);
        }
        BodyPart bodyPart = createBodyPart(htmlBody);

        Multipart multipart = new MimeMultipart();
        // only add screenshot if exists
        if (imageFile != null && !imageFile.isEmpty()) {
            File f = new File(imageFile);
            if (f.exists()) {
                MimeBodyPart imagePart = createImagePart(imageFile);
                multipart.addBodyPart(imagePart);
            }
        }

        multipart.addBodyPart(bodyPart);

        if (withAttachment) {
            addAttachment(multipart);
        }

        MimeMessage message = createMimeMessage(session);
        message.setContent(multipart);
        return message;
    }

    private static MimeMessage createFallbackEmail(Session session,
                                                   String resultBlockHtml,
                                                   String tableSummary,
                                                   boolean withAttachment) throws MessagingException, IOException {
        String htmlBody = createFallbackHTMLBody(resultBlockHtml, tableSummary);
        if (!withAttachment) {
            htmlBody = withAttachmentRejectedNote(htmlBody);
        }

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(createBodyPart(htmlBody));

        if (withAttachment) {
            addAttachment(multipart);
        }

        MimeMessage message = createMimeMessage(session);
        message.setContent(multipart);
        return message;
    }

    private static String withAttachmentRejectedNote(String htmlBody) {
        int bodyEnd = htmlBody.lastIndexOf("</body>");
        if (bodyEnd < 0) {
            return htmlBody + ATTACHMENT_REJECTED_NOTE;
        }
        return htmlBody.substring(0, bodyEnd) + ATTACHMENT_REJECTED_NOTE + htmlBody.substring(bodyEnd);
    }

    private static String createSubject() {
        StringBuilder subject = new StringBuilder();

        if (Configs.TEST_TYPE == TestType.SANITY) {
            subject.append("Sanity-");
        } else if (Configs.TEST_TYPE == TestType.REGRESSION) {
            subject.append("Regression-");
        } else {
            subject.append(capitalize(String.valueOf(Configs.TEST_TYPE))).append("Type-");
        }

        subject.append(formatEnvironment(Configs.ENVIRONMENT.name())).append("-");

        String rawPlatform = System.getProperty("platform");
        switch (Configs.PLATFORM) {
            case ANDROID, IOS -> subject.append("App-");
            case DWEB -> {
                if ("panel".equalsIgnoreCase(rawPlatform)) {
                    subject.append("Panel-");
                } else {
                    subject.append("Web-");
                }
            }
            case AMWEB -> subject.append("AmWeb-");
            case IMWEB -> subject.append("ImWeb-");
            default -> subject.append(Configs.PLATFORM.name()).append("-");
        }

        if (CoreFunctions.isJenkinsSystem()) {
            subject.append(System.getenv("VERTICAL_NAME")).append("-");
        }

        if ("all".equalsIgnoreCase(Configs.TRIBE)) {
            subject.append("AllTribe-");
        } else {
            subject.append(capitalize(Configs.TRIBE)).append("-");
        }

        if (CoreFunctions.isJenkinsSystem()) {
            subject.append("JobId:").append(System.getenv("BUILD_ID")).append("-");
        }

        subject.append(CoreFunctions.getCurrentDateTime());

        return subject.toString();
    }

    private static String formatEnvironment(String env) {
        switch (env.toUpperCase()) {
            case "PREPROD":
                return "PreProd";
            case "GK":
                return "GK";
            case "PROD":
                return "Prod";
            default:
                return capitalize(env.toLowerCase());
        }
    }

    private static Multipart createMultipart(BodyPart bodyPart, MimeBodyPart imagePart) throws MessagingException, IOException {
        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(imagePart);
        multipart.addBodyPart(bodyPart);
        addAttachment(multipart);
        return multipart;
    }

    private static void addAttachment(Multipart multipart) {
        File compressedFile = TestReportZipBuilder.buildSafely();
        if (compressedFile == null) {
            logger.warn("Test report zip unavailable, sending email without attachment");
            return;
        }

        try {
            MimeBodyPart reportAttachment = new MimeBodyPart();
            reportAttachment.attachFile(compressedFile);
            reportAttachment.setFileName(TEST_REPORT_ZIP_NAME);
            multipart.addBodyPart(reportAttachment);
        } catch (Exception e) {
            // Attachment is best-effort - do not abort the email
            logger.warn("Failed to attach test report zip: {}", e.getMessage(), e);
        }
    }

    private static MimeMessage createMimeMessage(Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(PROPERTIES.getProperty("email"));
        message.setRecipients(Message.RecipientType.TO, TO);
        message.setSubject(createSubject());
        message.setText("Automation text");
        return message;
    }

    private static BodyPart createBodyPart(String htmlBody) throws MessagingException {
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(htmlBody, "text/html");
        return bodyPart;
    }

    private static MimeBodyPart createImagePart(String imageFile) throws MessagingException, IOException {
        MimeBodyPart imagePart = new MimeBodyPart();
        imagePart.setHeader("Content-ID", "<SummaryResults>");
        imagePart.setDisposition(MimeBodyPart.INLINE);
        imagePart.attachFile(imageFile);
        return imagePart;
    }

    private static String createHTMLBody(String imageFile) {
        if (CoreFunctions.isJenkinsSystem()) {
            return getJenkinsHTML(imageFile);
        } else {
            return getLocalHTML();
        }
    }

    private static String createFallbackHTMLBody(String resultBlockHtml, String tableSummary) {
        if (CoreFunctions.isJenkinsSystem()) {
            String buildUrl = System.getenv("BUILD_URL");
            String extentReportUrl = (buildUrl != null ? buildUrl : "") + "artifact/extent-reports/extent-report.html";
            return HTMLTemplate.bodyEmailFallback(
                    Configs.PLATFORM,
                    System.getenv("BUILD_USER"),
                    System.getenv("BUILD_ID"),
                    buildUrl,
                    extentReportUrl,
                    resultBlockHtml,
                    tableSummary);
        }
        return HTMLTemplate.bodyEmailFallback(
                Configs.PLATFORM,
                "Local",
                "Local",
                "Local",
                "Local",
                resultBlockHtml,
                tableSummary);
    }

    private static String getJenkinsHTML(String imageFile) {
        String buildUrl = System.getenv("BUILD_URL");
        String extentReportUrl = buildUrl + "artifact/extent-reports/extent-report.html";
        String imgCid = (imageFile != null && !imageFile.isEmpty()) ? "cid:SummaryResults" : null;
        String tableSummary = safeGetModuleSummary();
        return HTMLTemplate.bodyEmail(Configs.PLATFORM, System.getenv("BUILD_USER"), System.getenv("BUILD_ID"), buildUrl, extentReportUrl, imgCid, tableSummary);
    }

    private static String getLocalHTML() {
        return HTMLTemplate.bodyEmail(Configs.PLATFORM, "Local", "Local", "Local", "Local", "cid:SummaryResults", HtmlProcessor.getModuleNameContainer());
    }

    private static String safeGetModuleSummary() {
        try {
            String summary = HtmlProcessor.getModuleNameContainer();
            if (summary == null || summary.trim().isEmpty()) {
                return null;
            }
            return summary;
        } catch (Exception e) {
            return null;
        }
    }
}
