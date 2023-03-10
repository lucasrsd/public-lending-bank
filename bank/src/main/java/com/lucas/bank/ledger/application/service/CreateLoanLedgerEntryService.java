package com.lucas.bank.ledger.application.service;

import com.google.gson.Gson;
import com.lucas.bank.ledger.application.port.in.CreateLedgerEntryCommand;
import com.lucas.bank.ledger.application.port.in.CreateLoanLedgerEntryUseCase;
import com.lucas.bank.ledger.application.port.out.CreateLedgerPort;
import com.lucas.bank.ledger.application.port.out.LedgerAggregate;
import com.lucas.bank.ledger.domain.Ledger;
import com.lucas.bank.ledger.domain.JournalTransaction;
import com.lucas.bank.ledger.domain.OperationType;
import com.lucas.bank.shared.staticInformation.StaticLedgerAccounts;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@UseCase
public class CreateLoanLedgerEntryService implements CreateLoanLedgerEntryUseCase {

    private final Logger LOG = LoggerFactory.getLogger(CreateLoanLedgerEntryService.class);
    private final CreateLedgerPort createLedgerPort;

    @Override
    public LedgerAggregate disbursement(Long loanId, BigDecimal amount, PersistenceTransactionManager persistenceTransactionManager) {

        var ledgerCommand = CreateLedgerEntryCommand
                .builder()
                .loanId(loanId)
                .bookingDate(new Date())
                .transactionDate(new Date())
                .transactionType("LOAN_DISBURSEMENT")
                .debitAmount(amount.negate())
                .ledgerDebitAccount(StaticLedgerAccounts.LOAN_PORTFOLIO)
                .creditAmount(amount)
                .ledgerCreditAccount(StaticLedgerAccounts.LOAN_SOURCE)
                .build();

        return create(ledgerCommand, persistenceTransactionManager);
    }

    @Override
    public LedgerAggregate interestApplied(Long loanId, BigDecimal amount, PersistenceTransactionManager persistenceTransactionManager) {

        var ledgerCommand = CreateLedgerEntryCommand
                .builder()
                .loanId(loanId)
                .bookingDate(new Date())
                .transactionDate(new Date())
                .transactionType("LOAN_INTEREST_APPLIED")
                .debitAmount(amount.negate())
                .ledgerDebitAccount(StaticLedgerAccounts.LOAN_INTEREST_RECEIVABLE)
                .creditAmount(amount)
                .ledgerCreditAccount(StaticLedgerAccounts.LOAN_INTEREST_INCOME)
                .build();

        return create(ledgerCommand, persistenceTransactionManager);
    }

    @Override
    public LedgerAggregate taxApplied(Long loanId, BigDecimal amount, PersistenceTransactionManager persistenceTransactionManager) {

        var ledgerCommand = CreateLedgerEntryCommand
                .builder()
                .loanId(loanId)
                .bookingDate(new Date())
                .transactionDate(new Date())
                .transactionType("LOAN_TAX_APPLIED")
                .debitAmount(amount.negate())
                .ledgerDebitAccount(StaticLedgerAccounts.LOAN_TAX_RECEIVABLE)
                .creditAmount(amount)
                .ledgerCreditAccount(StaticLedgerAccounts.LOAN_TAX_PAYABLE)
                .build();

        return create(ledgerCommand, persistenceTransactionManager);
    }

    @Override
    public LedgerAggregate principalRepayment(Long loanId, BigDecimal affectedPrincipal,PersistenceTransactionManager persistenceTransactionManager) {

        var ledgerCommand = CreateLedgerEntryCommand
                .builder()
                .loanId(loanId)
                .bookingDate(new Date())
                .transactionDate(new Date())
                .transactionType("LOAN_REPAYMENT_PRINCIPAL")
                .debitAmount(affectedPrincipal.negate())
                .ledgerDebitAccount(StaticLedgerAccounts.LOAN_SOURCE)
                .creditAmount(affectedPrincipal)
                .ledgerCreditAccount(StaticLedgerAccounts.LOAN_PORTFOLIO)
                .build();

        return create(ledgerCommand, persistenceTransactionManager);
    }

    @Override
    public LedgerAggregate interestRepayment(Long loanId, BigDecimal affectedInterest, PersistenceTransactionManager persistenceTransactionManager) {

        var ledgerCommand = CreateLedgerEntryCommand
                .builder()
                .loanId(loanId)
                .bookingDate(new Date())
                .transactionDate(new Date())
                .transactionType("LOAN_REPAYMENT_INTEREST")
                .debitAmount(affectedInterest.negate())
                .ledgerDebitAccount(StaticLedgerAccounts.LOAN_SOURCE)
                .creditAmount(affectedInterest)
                .ledgerCreditAccount(StaticLedgerAccounts.LOAN_INTEREST_RECEIVABLE)
                .build();

        return create(ledgerCommand, persistenceTransactionManager);
    }

    @Override
    public LedgerAggregate taxRepayment(Long loanId, BigDecimal affectedTax, PersistenceTransactionManager persistenceTransactionManager) {

        var ledgerCommand = CreateLedgerEntryCommand
                .builder()
                .loanId(loanId)
                .bookingDate(new Date())
                .transactionDate(new Date())
                .transactionType("LOAN_REPAYMENT_TAX")
                .debitAmount(affectedTax.negate())
                .ledgerDebitAccount(StaticLedgerAccounts.LOAN_SOURCE)
                .creditAmount(affectedTax)
                .ledgerCreditAccount(StaticLedgerAccounts.LOAN_TAXES_RECEIVABLE)
                .build();

        return create(ledgerCommand, persistenceTransactionManager);
    }

    private LedgerAggregate create(CreateLedgerEntryCommand command, PersistenceTransactionManager persistenceTransactionManager){
        var ledger = toLedger(command);

        createLedgerPort.createEntry(ledger, persistenceTransactionManager);

        LOG.info("Journal entry result {}", new Gson().toJson(ledger));

        return LedgerAggregate.builder().ledger(ledger).build();
    }

    private Ledger toLedger(CreateLedgerEntryCommand command){
        // ToDo - Think of splitting ledger and entries to different entities (not child object)

        if (command.getDebitAmount().compareTo(BigDecimal.ZERO) == 0){
            throw new RuntimeException("Ledger debit transaction should be greater than 0");
        }

        if (command.getCreditAmount().compareTo(BigDecimal.ZERO) == 0){
            throw new RuntimeException("Ledger credit transaction should be greater than 0");
        }

        if (command.getDebitAmount().add(command.getCreditAmount()).compareTo(BigDecimal.ZERO) != 0){
            throw new RuntimeException("Credit and debit amount should summarize 0");
        }

        var creditTransaction = JournalTransaction
                .builder()
                .journalTransactionId(UUID.randomUUID().toString())
                .operationType(OperationType.CREDIT)
                .bookingDate(command.getBookingDate())
                .transactionDate(command.getTransactionDate())
                .ledgerAccount(command.getLedgerCreditAccount())
                .amount(command.getCreditAmount())
                .build();

        var debitTransaction = JournalTransaction
                .builder()
                .journalTransactionId(UUID.randomUUID().toString())
                .operationType(OperationType.DEBIT)
                .bookingDate(command.getBookingDate())
                .transactionDate(command.getTransactionDate())
                .ledgerAccount(command.getLedgerDebitAccount())
                .amount(command.getDebitAmount())
                .build();

        var ledger = Ledger
                .builder()
                .ledgerEntryId(UUID.randomUUID().toString())
                .transactionDate(command.getTransactionDate())
                .credit(creditTransaction)
                .debit(debitTransaction)
                .loanId(command.getLoanId())
                .transactionType(command.getTransactionType())
                .build();

        return ledger;
    }
}
