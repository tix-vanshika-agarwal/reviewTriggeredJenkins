package com.tiket.web.page.common.eTicket;

import java.io.IOException;
import java.net.URL;

import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.WebDriver;

public abstract class ETicketWeb<T extends WebDriver> extends WebPage<T> {
    public ETicketWeb(Session<T> session) {
        super(session);
    }
    public static <T extends WebDriver> ETicketWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new ETicketDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new ETicketMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }
    public String pdfContent(String pdfUrl) {
        try (PDDocument doc = PDDocument.load(new URL(pdfUrl).openStream())){

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);

            logger.log("----- PDF CONTENT START -----");
            logger.log(text);
            logger.log("----- PDF CONTENT END -----");
            doc.close();
            return text;
        } catch (IOException e) {
            logger.log("Error reading PDF content: " + e.getMessage());
        }
        return "";
    }
}
