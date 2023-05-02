package com.lucas.bank.ledger.adapter.out;

import com.lucas.bank.ledger.application.port.out.CreateLedgerPort;
import com.lucas.bank.ledger.application.port.out.LedgerSummaryAggregate;
import com.lucas.bank.ledger.application.port.out.LoadLedgerPort;
import com.lucas.bank.ledger.application.port.out.LoadLedgerSummaryPort;
import com.lucas.bank.ledger.domain.Ledger;
import com.lucas.bank.shared.mySql.MySqlQueryClient;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import com.lucas.bank.shared.adapters.PersistenceAdapter;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@PersistenceAdapter
class LoadLedgerPersistenceAdapter implements CreateLedgerPort, LoadLedgerPort, LoadLedgerSummaryPort {

    private final LedgerMapper ledgerMapper;
    private final LedgerRepository ledgerRepository;
    private final MySqlQueryClient mySqlQueryClient;

    @Override
    public String createEntry(Ledger ledger, UnitOfWork unitOfWork) {
        var ledgerPOJO = ledgerMapper.mapToPOJO(ledger);
        unitOfWork.addTransaction(ledgerPOJO);
        return ledgerPOJO.getLedgerTransactionEntryId();
    }

    @Override
    public List<Ledger> loadLedgers(Long loanId) {
        var ledgerPOJOS = ledgerRepository.queryIndexByPk(LedgerPOJO.of(loanId), StaticInformation.LEDGER_ENTRIES_BY_LOAN_ID_GSI_INDEX);
        List<Ledger> ledgerEntries = new ArrayList<>();
        ledgerPOJOS.forEach(i -> ledgerEntries.add(ledgerMapper.mapToDomainEntity(i)));
        return ledgerEntries;
    }

    @Override
    public List<LedgerSummaryAggregate> summarizeLedgers(LocalDateTime startDate, LocalDateTime endDate) {

        var query = "select date(ledger_date) as date, l.transaction_side as side , l.transaction_type as type,  l.ledger_account_name as ledgerName, sum(transaction_amount) as amount, count(*) as transactionsCount\n" +
                "from ledger l \n" +
                "where ledger_date  BETWEEN ? and ? \n" +
                "group by date(ledger_date), l.transaction_side , l.transaction_type, l.ledger_account_name\n" +
                "order by date(ledger_date),l.transaction_type , l.ledger_account_name  ";

        List<LedgerSummaryPOJO> entries = mySqlQueryClient.executeQuery(query, LedgerSummaryPOJO.class, startDate, endDate);

        List<LedgerSummaryAggregate> result = new ArrayList<>();
        entries.forEach(l -> result.add(ledgerMapper.mapToAggregate(l)));
        return result;
    }
}
