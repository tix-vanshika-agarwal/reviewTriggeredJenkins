package com.tiket.testbase;

import com.tiket.model.Platform;
import com.tiket.model.Session;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record SessionSupplier(int timeLimit) implements Supplier<Session<WebDriver>> {

    private static final Logger logger = LogManager.getLogger(SessionSupplier.class);

    @Override
    public Session<WebDriver> get() {
        logger.info("Creating session with timelimit of: " + timeLimit);
        Platform platform = Platform.parse(System.getProperty("platform"));
        RemoteWebDriver driver = DriverFactory.getDriver(platform);
        setupProxyCertificate(driver);
        return new Session<>(driver.getSessionId().toString(), driver, System.currentTimeMillis(), timeLimit);
    }

    @SneakyThrows
    private void setupProxyCertificate(RemoteWebDriver driver) {
        logger.info("mitm flag: " + (Platform.IOS == Configs.PLATFORM && Configs.PROXY_USE));
        if(Platform.IOS == Configs.PLATFORM && Configs.PROXY_USE) {
            logger.info("Setting up mitm proxy certificate");
            Path certificatePath = Paths.get(System.getProperty("user.home"), ".mitmproxy", "mitmproxy-ca-cert.pem");
            Map<String, Object> args = new HashMap<>();
            byte[] byteContent = Files.readAllBytes(certificatePath);
            args.put("content", Base64.getEncoder().encodeToString(byteContent));
            driver.executeScript("mobile: installCertificate", args);
            logger.info("Done setting up mitm proxy certificate");
        }
    }
}
