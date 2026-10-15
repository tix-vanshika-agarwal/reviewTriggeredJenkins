package com.tiket.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class RefundBulkPreviewDTO {
    BigInteger refund_request_id;
    BigInteger order_id;
    BigInteger order_detail_id;
    boolean is_refund_created;
}
