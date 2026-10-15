package com.tiket.pojo.payment;

import java.util.List;

public class ConfigUserTickerPOSTBody {
    private List<CurrencyMessage> items;

    public ConfigUserTickerPOSTBody() {}

    public ConfigUserTickerPOSTBody(List<CurrencyMessage> items) {
        this.items = items;
    }

    public List<CurrencyMessage> getItems() {
        return items;
    }

    public static class CurrencyMessage {
        private String en;
        private String id;

        public CurrencyMessage(String messageEn, String messageId) {
            this.en = messageEn;
            this.id = messageId;
        }

        public String getMessageEn() {
            return en;
        }

        public void setMessageEn(String messageEn) {
            this.en = messageEn;
        }

        public String getMessageId() {
            return id;
        }

        public void setMessageId(String messageId) {
            this.id = messageId;
        }
    }
}
