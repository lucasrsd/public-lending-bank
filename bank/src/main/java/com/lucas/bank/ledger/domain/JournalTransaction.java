package com.lucas.bank.ledger.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class JournalTransaction {
    private final String journalTransactionId;
    private final OperationType operationType;
    private final BigDecimal amount;
    private final LedgerAccount ledgerAccount;
    private final Date transactionDate;
    private final Date bookingDate; // Reporting date - retroactively able to process backdated
}
