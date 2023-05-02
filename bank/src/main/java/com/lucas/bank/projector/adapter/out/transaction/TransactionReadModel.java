package com.lucas.bank.projector.adapter.out.transaction;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReadModel {
    private Long transactionId;
    private Long loanId;
    private LocalDateTime date;
    private String type;
    private BigDecimal amount;
}
