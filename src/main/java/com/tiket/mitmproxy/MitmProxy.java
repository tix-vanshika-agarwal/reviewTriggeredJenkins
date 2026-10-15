package com.tiket.mitmproxy;

import com.tiket.testbase.Configs;
import io.appium.mitmproxy.InterceptedMessage;
import io.appium.mitmproxy.MitmproxyJava;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeoutException;

public class MitmProxy {

    public static final int PROXY_PORT = 8080;
    public static final List<InterceptedMessage> MESSAGES = new CopyOnWriteArrayList<>();
    public static final MitmproxyJava PROXY = new MitmproxyJava(
            "/Library/Frameworks/Python.framework/Versions/3.12/bin/mitmdump",
            (InterceptedMessage m) -> {
                System.out.println("intercepted request for " + m.requestURL.toString());
                MESSAGES.add(m);
                return m;
            }
    );

    static {
        if(Configs.PROXY_USE) {
            try {
                PROXY.start();
            } catch (IOException | TimeoutException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
