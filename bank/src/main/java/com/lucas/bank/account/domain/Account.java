package com.lucas.bank.account.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class Account {

    private final Long accountId;
    private final String holderName;
    private final Date holderBirthDate;
    private final Boolean active;
    private final Date createdAt;
}
