package com.tiket.dto;

import lombok.Data;

@Data
public class MemberDTO {
    Long id;
    Long accountId;
    String accountUsername;
    boolean guest;
}