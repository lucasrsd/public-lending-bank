package com.lucas.bank.ledger.application.port.out;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LedgerSummaryAggregate {
    private LocalDate date;
    private String side;
    private String type;
    private String ledgerName;
    private BigDecimal amount;
    private Integer transactionsCount;
}
