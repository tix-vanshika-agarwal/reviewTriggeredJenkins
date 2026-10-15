package com.tiket.pojo.refund;

import java.math.BigDecimal;
import java.util.List;

public class FormMiddlewareApiBody {
    public BigDecimal amountFromVendor;
    public boolean isForceToBankTransfer;
    public List<String> items;
    public long orderDetailId;
    public long orderId;
    public String reasonId;
    public String remark;
    public Receiver receiver;

    public static class Receiver {
        public String bankAccountName;
        public String bankAccountNumber;
        public String bankCodeTiketIdentifier;
    }
}
