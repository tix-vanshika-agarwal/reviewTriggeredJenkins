package com.tiket.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class RefundRequestDTO {
    BigInteger id;
    String request_type;
    String vertical_name;
    BigInteger reference_order_id;
    BigInteger reference_order_detail_id;
    String currency;
    String refund_option_key;
    String last_status;
    BigDecimal actual_refund_amount;
}
