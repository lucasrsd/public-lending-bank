package com.lucas.bank.ledger.application.service;

import com.google.gson.Gson;
import com.lucas.bank.ledger.application.port.in.CreateLedgerEntryCommand;
import com.lucas.bank.ledger.application.port.in.CreateLedgerEntryUseCase;
import com.lucas.bank.ledger.application.port.out.CreateLedgerPort;
import com.lucas.bank.ledger.application.port.out.LedgerAggregate;
import com.lucas.bank.ledger.domain.Ledger;
import com.lucas.bank.ledger.domain.JournalTransaction;
import com.lucas.bank.ledger.domain.OperationType;
import com.lucas.bank.shared.LedgerStaticAccounts;
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
public class CreateLedgerEntryService implements CreateLedgerEntryUseCase {

    private final Logger LOG = LoggerFactory.getLogger(CreateLedgerEntryService.class);
    private final CreateLedgerPort createLedgerPort;

    @Override
    public LedgerAggregate forLoanDisbursement(Long loanId, BigDecimal amount, PersistenceTransactionManager persistenceTransactionManager) {

        var source = LedgerStaticAccounts.LOAN_SOURCE;
        var portfolio = LedgerStaticAccounts.LOAN_PORTFOLIO;

        // ToDo - Check better way or place to manage ledger parameters
        var ledgerCommand = CreateLedgerEntryCommand
                .builder()
                .loanId(loanId)
                .bookingDate(new Date())
                .transactionDate(new Date())
                .transactionType("LOAN_DISBURSEMENT")
                .debitAmount(amount.negate())
                .ledgerDebitAccount(portfolio)
                .creditAmount(amount)
                .ledgerCreditAccount(source)
                .build();

        return create(ledgerCommand, persistenceTransactionManager);
    }

    @Override
    public LedgerAggregate forPrincipalRepayment(Long loanId, BigDecimal affectedPrincipal,PersistenceTransactionManager persistenceTransactionManager) {

        var tbdDebit = LedgerStaticAccounts.LOAN_SOURCE;
        var tbdCredit = LedgerStaticAccounts.LOAN_PORTFOLIO;

        // ToDo - Check better way or place to manage ledger parameters
        var ledgerCommand = CreateLedgerEntryCommand
                .builder()
                .loanId(loanId)
                .bookingDate(new Date())
                .transactionDate(new Date())
                .transactionType("LOAN_REPAYMENT_PRINCIPAL")
                .debitAmount(affectedPrincipal.negate())
                .ledgerDebitAccount(tbdDebit)
                .creditAmount(affectedPrincipal)
                .ledgerCreditAccount(tbdCredit)
                .build();

        return create(ledgerCommand, persistenceTransactionManager);
    }

    @Override
    public LedgerAggregate forInterestRepayment(Long loanId, BigDecimal affectedInterest, PersistenceTransactionManager persistenceTransactionManager) {
        var tbdDebit = LedgerStaticAccounts.LOAN_SOURCE;
        var tbdCredit = LedgerStaticAccounts.LOAN_INTEREST_RECEIVABLE;

        // ToDo - Check better way or place to manage ledger parameters
        var ledgerCommand = CreateLedgerEntryCommand
                .builder()
                .loanId(loanId)
                .bookingDate(new Date())
                .transactionDate(new Date())
                .transactionType("LOAN_REPAYMENT_INTEREST")
                .debitAmount(affectedInterest.negate())
                .ledgerDebitAccount(tbdDebit)
                .creditAmount(affectedInterest)
                .ledgerCreditAccount(tbdCredit)
                .build();

        return create(ledgerCommand, persistenceTransactionManager);
    }

    @Override
    public LedgerAggregate forTaxRepayment(Long loanId, BigDecimal affectedTax, PersistenceTransactionManager persistenceTransactionManager) {
        var tbdDebit = LedgerStaticAccounts.LOAN_SOURCE;
        var tbdCredit = LedgerStaticAccounts.LOAN_TAXES_RECEIVABLE;

        // ToDo - Check better way or place to manage ledger parameters
        var ledgerCommand = CreateLedgerEntryCommand
                .builder()
                .loanId(loanId)
                .bookingDate(new Date())
                .transactionDate(new Date())
                .transactionType("LOAN_REPAYMENT_TAX")
                .debitAmount(affectedTax.negate())
                .ledgerDebitAccount(tbdDebit)
                .creditAmount(affectedTax)
                .ledgerCreditAccount(tbdCredit)
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
        // ToDo - Add ledger account balance
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
