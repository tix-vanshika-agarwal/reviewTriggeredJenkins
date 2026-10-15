package com.tiket.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class LoyaltyPointsDTO {
    String task_status;
    String task_type;
    String process_type;
    Long amount;
    BigInteger account_id;
    String type;
    String status;
}
