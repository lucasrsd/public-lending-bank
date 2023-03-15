package com.lucas.bank.ledger.application.service;

import com.lucas.bank.ledger.application.port.in.LoadLedgerQuery;
import com.lucas.bank.ledger.application.port.out.LedgerEntriesAggregate;
import com.lucas.bank.ledger.application.port.out.LoadLedgerPort;
import com.lucas.bank.ledger.domain.Ledger;
import com.lucas.bank.shared.adapters.UseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public Map<String, BigDecimal> summary() {
        var entries = loadLedgerPort.summarizeLedgers();

        Map<String, BigDecimal> ledgerAccounts = new HashMap<>();

        for(Ledger entry : entries){

            var ledgerAccountName = entry.getAccount().getName();

            if (ledgerAccounts.containsKey(ledgerAccountName))
                ledgerAccounts.put(ledgerAccountName, ledgerAccounts.get(ledgerAccountName).add(entry.getAmount()));
            else
                ledgerAccounts.put(ledgerAccountName, entry.getAmount());
        }

        return ledgerAccounts;
    }
}
