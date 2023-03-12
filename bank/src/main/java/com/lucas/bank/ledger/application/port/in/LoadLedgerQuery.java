package com.lucas.bank.ledger.application.port.in;

import com.lucas.bank.ledger.application.port.out.LedgerEntriesAggregate;

public interface LoadLedgerQuery {
    LedgerEntriesAggregate loadLedgers(Long loanId);
}