package com.lucas.bank.ledger.adapter.out;

import com.lucas.bank.ledger.application.port.out.CreateLedgerPort;
import com.lucas.bank.ledger.application.port.out.LoadLedgerPort;
import com.lucas.bank.ledger.domain.Ledger;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import com.lucas.bank.shared.adapters.PersistenceAdapter;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@PersistenceAdapter
class LoadLedgerPersistenceAdapter implements CreateLedgerPort, LoadLedgerPort {

    private final LedgerMapper ledgerMapper;
    private final LedgerRepository ledgerRepository;

    @Override
    public String createEntry(Ledger ledger, UnitOfWork unitOfWork) {
        var ledgerPOJO = ledgerMapper.mapToPOJO(ledger);
        unitOfWork.addTransaction(ledgerPOJO);
        return ledgerPOJO.getLedgerTransactionEntryId();
    }

    @Override
    public List<Ledger> loadLedgers(Long loanId) {
        var ledgerPOJOS =  ledgerRepository.listByPkBeginsWith(LedgerPOJO.pkPrefix);
        List<Ledger> ledgerEntries = new ArrayList<>();
        ledgerPOJOS.forEach(i -> ledgerEntries.add(ledgerMapper.mapToDomainEntity(i)));
        return ledgerEntries;
    }
}
