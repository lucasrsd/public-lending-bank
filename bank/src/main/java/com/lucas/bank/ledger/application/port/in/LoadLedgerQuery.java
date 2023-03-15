package com.lucas.bank.ledger.application.port.in;

import com.lucas.bank.ledger.application.port.out.LedgerEntriesAggregate;

import java.math.BigDecimal;
import java.util.Map;

public interface LoadLedgerQuery {
    LedgerEntriesAggregate loadLedgers(Long loanId);
    Map<String, BigDecimal> summary();

}