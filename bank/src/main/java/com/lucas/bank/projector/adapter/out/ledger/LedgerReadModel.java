package com.lucas.bank.projector.adapter.out.ledger;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LedgerReadModel {
    private String ledgerTransactionEntryId;
    private Long loanId;
    private Long ledgerTransactionId;
    private String ledgerTransactionType;
    private String ledgerTransactionSide;
    private BigDecimal ledgerTransactionAmount;
    private Integer ledgerAccountId;
    private String ledgerAccountName;
    private String ledgerAccountType;
    private LocalDateTime ledgerDate;
    private LocalDateTime ledgerBookingDate;
}
