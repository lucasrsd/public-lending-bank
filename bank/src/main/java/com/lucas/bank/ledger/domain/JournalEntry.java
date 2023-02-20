package com.lucas.bank.ledger.domain;

import lombok.*;

import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class JournalEntry {
    private final String entryId;
    private final Long loanAccountId;
    private final String transactionType;
    private final JournalTransaction credit;
    private final JournalTransaction debit;
    private final Date transactionDate;
}
