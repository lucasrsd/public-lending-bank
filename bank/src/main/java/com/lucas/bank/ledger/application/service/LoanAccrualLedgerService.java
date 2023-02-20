package com.lucas.bank.ledger.application.service;

import com.google.gson.Gson;
import com.lucas.bank.ledger.application.port.in.LoanAccrualLedgerCommand;
import com.lucas.bank.ledger.application.port.in.LoanAccrualLedgerUseCase;
import com.lucas.bank.ledger.application.port.out.LedgerAggregate;
import com.lucas.bank.ledger.domain.JournalEntry;
import com.lucas.bank.ledger.domain.JournalTransaction;
import com.lucas.bank.ledger.domain.LedgerAccount;
import com.lucas.bank.ledger.domain.OperationType;
import com.lucas.bank.shared.LedgerStaticAccounts;
import com.lucas.bank.shared.adapters.UseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@UseCase
public class LoanAccrualLedgerService implements LoanAccrualLedgerUseCase {

    private final Logger LOG = LoggerFactory.getLogger(LoanAccrualLedgerService.class);
    private static final String TRANSACTION_TYPE = "LOAN_DAILY_INTEREST_ACCRUAL";


    public LedgerAggregate execute(LoanAccrualLedgerCommand command) {

        var assetAccount = LedgerStaticAccounts.ASSET_LENDING;
        var liabilityAccount = LedgerStaticAccounts.LIABILITY_LENDING;

        var transactionDate =  new Date();

        var creditTransaction = JournalTransaction
                .builder()
                .transactionId(UUID.randomUUID().toString())
                .operationType(OperationType.CREDIT)
                .bookingDate(command.getBookingDate())
                .transactionDate(transactionDate)
                .ledgerAccount(liabilityAccount)
                .amount(command.getAccrualAmount())
                .build();

        var debitTransaction = JournalTransaction
                .builder()
                .transactionId(UUID.randomUUID().toString())
                .operationType(OperationType.DEBIT)
                .bookingDate(command.getBookingDate())
                .transactionDate(transactionDate)
                .ledgerAccount(assetAccount)
                .amount(command.getAccrualAmount())
                .build();

        var journalEntry = JournalEntry
                .builder()
                .entryId(UUID.randomUUID().toString())
                .transactionDate(transactionDate)
                .credit(creditTransaction)
                .debit(debitTransaction)
                .loanAccountId(command.getLoanAccountId())
                .transactionType(TRANSACTION_TYPE)
                .build();

        LOG.info("Ledger finalised for account {}", command.getLoanAccountId());
        LOG.info("Journal entry result {}", new Gson().toJson(journalEntry));

        return LedgerAggregate.builder().journalEntry(journalEntry).build();
    }
}
