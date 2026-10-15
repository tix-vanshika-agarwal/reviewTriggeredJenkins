package com.tiket.dbclient.bigquery;

import com.tiket.common.JsonRequestExtractor;

public enum NotificationTemplateType {

    CHECKOUT {
        @Override
        public boolean matches(String requestJson) {
            String value = JsonRequestExtractor.extractCheckoutTemplate(requestJson);
            return containsIgnoreCase(value, "checkout");
        }
    },

    ERECEIPT {
        @Override
        public boolean matches(String requestJson) {
            String value = JsonRequestExtractor.extractIssuedTemplate(requestJson);
            return containsIgnoreCase(value, "receipt");
        }
    },

    ETICKET {
        @Override
        public boolean matches(String requestJson) {
            String value = JsonRequestExtractor.extractETicketSubject(requestJson);
            return containsIgnoreCase(value, "E-ticket");
        }
    },

    REFUND {
        @Override
        public boolean matches(String requestJson) {
            String subject = JsonRequestExtractor.extractETicketSubject(requestJson);
            if (containsIgnoreCase(subject, "refund")) return true;

            String templateCode = JsonRequestExtractor.extractIssuedTemplate(requestJson);
            if (containsIgnoreCase(templateCode, "refund")) return true;

            String checkoutTemplate = JsonRequestExtractor.extractCheckoutTemplate(requestJson);
            return containsIgnoreCase(checkoutTemplate, "refund");
        }
    };

    public abstract boolean matches(String requestJson);

    protected boolean containsIgnoreCase(String actual, String expected) {
        return actual != null && actual.toLowerCase().contains(expected.toLowerCase());
    }
}
