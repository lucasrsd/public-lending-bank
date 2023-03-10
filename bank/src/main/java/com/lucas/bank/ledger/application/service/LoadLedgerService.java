package com.lucas.bank.ledger.application.service;

import com.lucas.bank.ledger.application.port.in.LoadLedgerQuery;
import com.lucas.bank.ledger.application.port.out.LedgerEntriesAggregate;
import com.lucas.bank.ledger.application.port.out.LoadLedgerPort;
import com.lucas.bank.shared.adapters.UseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@UseCase
public class LoadLedgerService implements LoadLedgerQuery {

    private final Logger LOG = LoggerFactory.getLogger(LoadLedgerService.class);
    private final LoadLedgerPort loadLedgerPort;

    @Override
    public LedgerEntriesAggregate loadLedgers(Long loanId) {
        var entries = loadLedgerPort.loadLedgers(loanId);
        return LedgerEntriesAggregate.builder().entries(entries).build();
    }
}
