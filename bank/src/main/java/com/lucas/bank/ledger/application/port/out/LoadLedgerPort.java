package com.lucas.bank.ledger.application.port.out;

import com.lucas.bank.ledger.domain.Ledger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface LoadLedgerPort {
    List<Ledger> loadLedgers(Long loanId);
    List<Ledger> summarizeLedgers();
}
