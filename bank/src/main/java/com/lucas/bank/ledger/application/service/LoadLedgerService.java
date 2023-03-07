package com.lucas.bank.ledger.application.service;

import com.google.gson.Gson;
import com.lucas.bank.ledger.application.port.in.CreateLedgerEntryCommand;
import com.lucas.bank.ledger.application.port.in.CreateLedgerEntryUseCase;
import com.lucas.bank.ledger.application.port.in.LoadLedgerQuery;
import com.lucas.bank.ledger.application.port.out.CreateLedgerPort;
import com.lucas.bank.ledger.application.port.out.LedgerAggregate;
import com.lucas.bank.ledger.application.port.out.LoadLedgerPort;
import com.lucas.bank.ledger.application.port.out.SummaryAggregate;
import com.lucas.bank.ledger.domain.JournalTransaction;
import com.lucas.bank.ledger.domain.Ledger;
import com.lucas.bank.ledger.domain.OperationType;
import com.lucas.bank.shared.LedgerStaticAccounts;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@UseCase
public class LoadLedgerService implements LoadLedgerQuery {

    private final Logger LOG = LoggerFactory.getLogger(LoadLedgerService.class);
    private final LoadLedgerPort loadLedgerPort;

    @Override
    public SummaryAggregate summary() {
        var summary = loadLedgerPort.summarizeLedgerBalance();
        return SummaryAggregate.builder().summary(summary).build();
    }
}
