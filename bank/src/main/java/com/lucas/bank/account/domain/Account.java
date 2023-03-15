package com.lucas.bank.account.domain;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class Account {

    private Long accountId;
    private String holderName;
    private LocalDateTime holderBirthDate;
    private Boolean active;
    private LocalDateTime createdAt;
}
