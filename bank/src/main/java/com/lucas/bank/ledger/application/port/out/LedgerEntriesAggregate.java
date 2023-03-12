package com.lucas.bank.ledger.application.port.out;

import com.lucas.bank.ledger.domain.Ledger;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class LedgerEntriesAggregate {
    private final List<Ledger> entries;
}
