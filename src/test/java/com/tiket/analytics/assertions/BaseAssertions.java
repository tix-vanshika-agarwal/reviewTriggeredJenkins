package com.tiket.analytics.assertions;

import com.tiket.analytics.core.AnalyticsFunctions;
import com.tiket.logging.ILogger;
import io.appium.mitmproxy.InterceptedMessage;

import java.util.List;
import java.util.Map;

public class BaseAssertions {

    private final ILogger logger;

    public BaseAssertions(ILogger logger) {
        this.logger = logger;
    }

    public int verifyEvent(List<InterceptedMessage> messages, String event) {
        int count = 0;
        for (InterceptedMessage m : messages) {
            if (m.requestURL.toString().matches(".*api(-js)?\\.mixpanel\\.com.*")) {
                logger.log("encoded-body-> " + new String(m.requestBody));
                String body = AnalyticsFunctions.decodeMixpanelRequestBody(new String(m.requestBody));
                logger.log("body-> " + body);
                if (AnalyticsFunctions.haveEventInBody(body, event)) {
                    count++;
                }
            }
        }
        return count;
    }

    public int verifyFilteredEvent(List<InterceptedMessage> messages, String event, String screenName) {
        int count = 0;
        for (InterceptedMessage m : messages) {
            if (m.requestURL.toString().matches(".*api(-js)?\\.mixpanel\\.com.*")) {
                logger.log("encoded-body-> " + new String(m.requestBody));
                String body = AnalyticsFunctions.decodeMixpanelRequestBody(new String(m.requestBody));
                logger.log("body-> " + body);
                if (AnalyticsFunctions.haveFilteredEventInBody(body, event, screenName)) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean verifyEventProperty(List<InterceptedMessage> messages, String event, String screenName, String property) {
        for (InterceptedMessage m : messages) {
            if (m.requestURL.toString().matches(".*api(-js)?\\.mixpanel\\.com.*")) {
                logger.log("encoded-body-> " + new String(m.requestBody));
                String body = AnalyticsFunctions.decodeMixpanelRequestBody(new String(m.requestBody));
                logger.log("body-> " + body);
                if (AnalyticsFunctions.haveEventPropertyInBody(body, event, screenName, property)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean verifyUserProperty(List<InterceptedMessage> messages, String property) {
        for (InterceptedMessage m : messages) {
            if (m.requestURL.toString().matches(".*api(-js)?\\.mixpanel\\.com.*")) {
                logger.log("encoded-body-> " + new String(m.requestBody));
                String body = AnalyticsFunctions.decodeMixpanelRequestBody(new String(m.requestBody));
                logger.log("body-> " + body);
                if (AnalyticsFunctions.haveUserPropertyInBody(body, property)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean haveCorrectEventProperty(List<InterceptedMessage> messages, String event, String property, String value) {
        for (InterceptedMessage m : messages) {
            if (m.requestURL.toString().matches(".*api(-js)?\\.mixpanel\\.com.*")) {
                logger.log("encoded-body-> " + new String(m.requestBody));
                String body = AnalyticsFunctions.decodeMixpanelRequestBody(new String(m.requestBody));
                logger.log("body-> " + body);
                if (AnalyticsFunctions.haveEventPropertyWithValue(body, event, property, value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean filteredEventHaveCorrectEventProperty(List<InterceptedMessage> messages, String event, String screenName, String property, String value) {
        for (InterceptedMessage m : messages) {
            if (m.requestURL.toString().matches(".*api(-js)?\\.mixpanel\\.com.*")) {
                logger.log("encoded-body-> " + new String(m.requestBody));
                String body = AnalyticsFunctions.decodeMixpanelRequestBody(new String(m.requestBody));
                logger.log("body-> " + body);
                if (AnalyticsFunctions.filteredEventHaveEventPropertyWithValue(body, event, screenName, property, value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int countFilteredEventByProperties(List<InterceptedMessage> messages, String event, Map<String, String> filters) {
        int count = 0;
        for (InterceptedMessage m : messages) {
            if (m.requestURL.toString().matches(".*api(-js)?\\.mixpanel\\.com.*")) {
                logger.log("encoded-body-> " + new String(m.requestBody));
                String body = AnalyticsFunctions.decodeMixpanelRequestBody(new String(m.requestBody));
                logger.log("body-> " + body);
                if (AnalyticsFunctions.haveFilteredEventInBodyByProperties(body, event, filters)) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean filteredByPropertiesHaveCorrectEventProperty(List<InterceptedMessage> messages, String event, Map<String, String> filters, String property, String value) {
        for (InterceptedMessage m : messages) {
            if (m.requestURL.toString().matches(".*api(-js)?\\.mixpanel\\.com.*")) {
                logger.log("encoded-body-> " + new String(m.requestBody));
                String body = AnalyticsFunctions.decodeMixpanelRequestBody(new String(m.requestBody));
                logger.log("body-> " + body);
                if (AnalyticsFunctions.filteredByPropertiesHaveEventPropertyWithValue(body, event, filters, property, value)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean haveCorrectUserProperty(List<InterceptedMessage> messages, String property, String value) {
        for (InterceptedMessage m : messages) {
            if (m.requestURL.toString().matches(".*api(-js)?\\.mixpanel\\.com.*")) {
                logger.log("encoded-body-> " + new String(m.requestBody));
                String body = AnalyticsFunctions.decodeMixpanelRequestBody(new String(m.requestBody));
                logger.log("body-> " + body);
                if (AnalyticsFunctions.haveUserPropertyWithValue(body, property, value)) {
                    return true;
                }
            }
        }
        return false;
    }
}