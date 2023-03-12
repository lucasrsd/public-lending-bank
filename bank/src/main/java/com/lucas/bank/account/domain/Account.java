package com.lucas.bank.account.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class Account {

    private Long accountId;
    private String holderName;
    private Date holderBirthDate;
    private Boolean active;
    private Date createdAt;
}
