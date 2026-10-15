package com.tiket.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class CorporateOrderDTO {
    BigInteger id;
    BigInteger order_id;
    BigDecimal amount;
    String product_type;
    String payment_status;
}
