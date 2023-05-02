package com.lucas.bank.ledger.adapter.out;

import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LedgerSummaryPOJO {
    private java.sql.Date date;
    private String side;
    private String type;
    private String ledgerName;
    private BigDecimal amount;
    private Integer transactionsCount;
}
