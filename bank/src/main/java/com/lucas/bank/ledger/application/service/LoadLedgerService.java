package com.lucas.bank.ledger.application.service;

import com.lucas.bank.ledger.application.port.in.LoadLedgerQuery;
import com.lucas.bank.ledger.application.port.in.SearchLedgerCommand;
import com.lucas.bank.ledger.application.port.out.LedgerEntriesAggregate;
import com.lucas.bank.ledger.application.port.out.LedgerSummaryAggregate;
import com.lucas.bank.ledger.application.port.out.LoadLedgerPort;
import com.lucas.bank.ledger.application.port.out.LoadLedgerSummaryPort;
import com.lucas.bank.shared.adapters.UseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
@UseCase
public class LoadLedgerService implements LoadLedgerQuery {

    private final Logger LOG = LoggerFactory.getLogger(LoadLedgerService.class);
    private final LoadLedgerPort loadLedgerPort;
    private final LoadLedgerSummaryPort loadLedgerSummaryPort;

    @Override
    public LedgerEntriesAggregate loadLedgers(Long loanId) {
        var entries = loadLedgerPort.loadLedgers(loanId);
        return LedgerEntriesAggregate.builder().entries(entries).build();
    }

    @Override
    public List<LedgerSummaryAggregate> summary(SearchLedgerCommand command) {
        return loadLedgerSummaryPort.summarizeLedgers(command.getStartDate(), command.getEndDate());
    }
}
