package com.lucas.bank.ledger.application.port.out;

import com.lucas.bank.ledger.domain.JournalEntry;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class LedgerAggregate {
    private final JournalEntry journalEntry;
}
