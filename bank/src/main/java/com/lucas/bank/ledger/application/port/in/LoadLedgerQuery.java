package com.lucas.bank.ledger.application.port.in;

import com.lucas.bank.ledger.application.port.out.LedgerEntriesAggregate;
import com.lucas.bank.ledger.application.port.out.LedgerSummaryAggregate;

import java.util.List;

public interface LoadLedgerQuery {
    LedgerEntriesAggregate loadLedgers(Long loanId);
    List<LedgerSummaryAggregate> summary(SearchLedgerCommand searchLedgerCommand);
}