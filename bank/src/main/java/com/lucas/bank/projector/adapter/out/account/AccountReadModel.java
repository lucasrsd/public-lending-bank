package com.lucas.bank.projector.adapter.out.account;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountReadModel {

    private Long accountId;
    private String holderName;
    private LocalDateTime holderBirthDate;
    private LocalDateTime createdAt;
}
