package com.lucas.bank.ledger.application.port.out;

import com.lucas.bank.ledger.domain.Ledger;

import java.util.List;


public interface LoadLedgerPort {
    List<Ledger> loadLedgers(Long loanId);
}
