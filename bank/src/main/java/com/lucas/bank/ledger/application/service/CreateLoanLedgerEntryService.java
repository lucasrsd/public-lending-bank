package com.lucas.bank.ledger.application.service;

import com.google.gson.Gson;
import com.lucas.bank.ledger.application.port.in.CreateLoanLedgerEntryUseCase;
import com.lucas.bank.ledger.application.port.out.CreateLedgerPort;
import com.lucas.bank.ledger.application.port.out.LedgerAggregate;
import com.lucas.bank.ledger.domain.Ledger;
import com.lucas.bank.shared.staticInformation.StaticLedgerAccounts;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@RequiredArgsConstructor
@UseCase
public class CreateLoanLedgerEntryService implements CreateLoanLedgerEntryUseCase {

    private final Logger LOG = LoggerFactory.getLogger(CreateLoanLedgerEntryService.class);
    private final CreateLedgerPort createLedgerPort;

    @Override
    public LedgerAggregate disbursement(Long loanId, Long transactionId, BigDecimal amount, UnitOfWork unitOfWork) {

        var transactionName = "LOAN_DISBURSEMENT";
        var debitAccount = StaticLedgerAccounts.LOAN_PORTFOLIO;
        var creditAccount = StaticLedgerAccounts.LOAN_SOURCE;

        var debitLedger = Ledger.forDebit(loanId, transactionId, transactionName, amount, debitAccount);
        var creditLedger = Ledger.forCredit(loanId, transactionId, transactionName, amount, creditAccount);

        return create(debitLedger, creditLedger, unitOfWork);
    }

    @Override
    public LedgerAggregate interestApplied(Long loanId, Long transactionId, BigDecimal amount, UnitOfWork unitOfWork) {

        var transactionName = "LOAN_INTEREST_APPLIED";
        var debitAccount = StaticLedgerAccounts.LOAN_INTEREST_RECEIVABLE;
        var creditAccount = StaticLedgerAccounts.LOAN_INTEREST_INCOME;

        var debitLedger = Ledger.forDebit(loanId, transactionId, transactionName, amount, debitAccount);
        var creditLedger = Ledger.forCredit(loanId, transactionId, transactionName, amount, creditAccount);

        return create(debitLedger, creditLedger, unitOfWork);
    }

    @Override
    public LedgerAggregate taxApplied(Long loanId, Long transactionId, BigDecimal amount, UnitOfWork unitOfWork) {

        var transactionName = "LOAN_TAX_APPLIED";
        var debitAccount = StaticLedgerAccounts.LOAN_TAX_RECEIVABLE;
        var creditAccount = StaticLedgerAccounts.LOAN_TAX_PAYABLE;

        var debitLedger = Ledger.forDebit(loanId, transactionId, transactionName, amount, debitAccount);
        var creditLedger = Ledger.forCredit(loanId, transactionId, transactionName, amount, creditAccount);

        return create(debitLedger, creditLedger, unitOfWork);
    }

    @Override
    public LedgerAggregate principalRepayment(Long loanId, Long transactionId, BigDecimal affectedPrincipal, UnitOfWork unitOfWork) {

        var transactionName = "LOAN_REPAYMENT_PRINCIPAL";
        var debitAccount = StaticLedgerAccounts.LOAN_SOURCE;
        var creditAccount = StaticLedgerAccounts.LOAN_PORTFOLIO;

        var debitLedger = Ledger.forDebit(loanId, transactionId, transactionName, affectedPrincipal, debitAccount);
        var creditLedger = Ledger.forCredit(loanId, transactionId, transactionName, affectedPrincipal, creditAccount);

        return create(debitLedger, creditLedger, unitOfWork);
    }

    @Override
    public LedgerAggregate interestRepayment(Long loanId, Long transactionId, BigDecimal affectedInterest, UnitOfWork unitOfWork) {

        var transactionName = "LOAN_REPAYMENT_INTEREST";
        var debitAccount = StaticLedgerAccounts.LOAN_SOURCE;
        var creditAccount = StaticLedgerAccounts.LOAN_INTEREST_RECEIVABLE;

        var debitLedger = Ledger.forDebit(loanId, transactionId, transactionName, affectedInterest, debitAccount);
        var creditLedger = Ledger.forCredit(loanId, transactionId, transactionName, affectedInterest, creditAccount);

        return create(debitLedger, creditLedger, unitOfWork);
    }

    @Override
    public LedgerAggregate taxRepayment(Long loanId, Long transactionId, BigDecimal affectedTax, UnitOfWork unitOfWork) {

        var transactionName = "LOAN_REPAYMENT_TAX";
        var debitAccount = StaticLedgerAccounts.LOAN_SOURCE;
        var creditAccount = StaticLedgerAccounts.LOAN_TAXES_RECEIVABLE;

        var debitLedger = Ledger.forDebit(loanId, transactionId, transactionName, affectedTax, debitAccount);
        var creditLedger = Ledger.forCredit(loanId, transactionId, transactionName, affectedTax, creditAccount);

        return create(debitLedger, creditLedger, unitOfWork);
    }

    private LedgerAggregate create(Ledger debitLedger, Ledger creditLedger, UnitOfWork unitOfWork){
        createLedgerPort.createEntry(debitLedger, unitOfWork);
        createLedgerPort.createEntry(creditLedger, unitOfWork);

        LOG.info("Debit ledger result {}", new Gson().toJson(debitLedger));
        LOG.info("Debit ledger result {}", new Gson().toJson(creditLedger));

        return LedgerAggregate.builder().debit(debitLedger).credit(creditLedger).build();
    }
}
