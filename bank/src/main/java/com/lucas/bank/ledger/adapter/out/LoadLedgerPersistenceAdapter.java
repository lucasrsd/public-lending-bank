package com.lucas.bank.ledger.adapter.out;

import com.lucas.bank.ledger.application.port.out.CreateLedgerPort;
import com.lucas.bank.ledger.application.port.out.LoadLedgerPort;
import com.lucas.bank.ledger.domain.Ledger;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import com.lucas.bank.shared.adapters.PersistenceAdapter;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@PersistenceAdapter
class LoadLedgerPersistenceAdapter implements CreateLedgerPort, LoadLedgerPort {

    private final LedgerMapper ledgerMapper;
    private final LedgerRepository ledgerRepository;

    @Override
    public String createEntry(Ledger ledger, PersistenceTransactionManager persistenceTransactionManager) {
        var ledgerPOJO = ledgerMapper.mapToPOJO(ledger);
        persistenceTransactionManager.addTransaction(ledgerPOJO);
        return ledgerPOJO.getLedgerEntryId();
    }

    @Override
    public Map<String, BigDecimal> summarizeLedgerBalance() {
        // ToDo - URGENT FULLY REFACTOR - just an experiment + initial accounting version
        var ledgerEntries = ledgerRepository.indexFullScan(StaticInformation.LEDGER_ENTRIES_GSI_INDEX);

        Map<String, BigDecimal> summary = new HashMap<>();

        for(LedgerPOJO ledger : ledgerEntries){

            var entry = ledgerRepository.get(ledger);

            if (entry == null) continue;

            var creditAccountId = entry.getCredit().getLedgerAccountId().toString() + "_" + entry.getCredit().getAccountType() + "_" + entry.getCredit().getLedgerAccountName();
            var debitAccountId = entry.getDebit().getLedgerAccountId().toString() + "_" + entry.getDebit().getAccountType() + "_" + entry.getDebit().getLedgerAccountName();


            if (summary.containsKey(creditAccountId)){
                summary.put(creditAccountId, summary.get(creditAccountId).add(entry.getCredit().getAmount()));
            }
            else {
                summary.put(creditAccountId, entry.getCredit().getAmount());
            }

            if (summary.containsKey(debitAccountId)){
                summary.put(debitAccountId, summary.get(debitAccountId).add(entry.getDebit().getAmount()));
            }
            else {
                summary.put(debitAccountId, entry.getDebit().getAmount());
            }
        }

        return summary;
    }
}
