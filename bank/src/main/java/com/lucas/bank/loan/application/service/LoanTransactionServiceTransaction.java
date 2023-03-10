package com.lucas.bank.loan.application.service;

import com.lucas.bank.installment.application.port.in.LoadInstallmentsQuery;
import com.lucas.bank.installment.application.port.in.UpdateInstallmentUseCase;
import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.installment.domain.InstallmentState;
import com.lucas.bank.interest.application.port.in.AccrualUseCase;
import com.lucas.bank.ledger.application.port.in.CreateLoanLedgerEntryUseCase;
import com.lucas.bank.loan.application.port.in.LoanTransactionUseCase;
import com.lucas.bank.loan.application.port.out.LoadLoanPort;
import com.lucas.bank.loan.application.port.out.UpdateLoanPort;
import com.lucas.bank.loan.domain.LoanState;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import com.lucas.bank.shared.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@UseCase
public class LoanTransactionServiceTransaction implements LoanTransactionUseCase {
    private final UpdateLoanPort updateLoanPort;
    private final LoadLoanPort loadLoanPort;
    private final LoadInstallmentsQuery loadInstallmentsQuery;
    private final UpdateInstallmentUseCase updateInstallmentUseCase;
    private final AccrualUseCase accrualUseCase;
    private final CreateLoanLedgerEntryUseCase createLoanLedgerEntryUseCase;

    @Override
    public void activateLoan(Long loanId, PersistenceTransactionManager persistenceTransactionManager) {
        var loan = loadLoanPort.loadLoan(loanId);

        if (!loan.canDisburse()) {
            throw new RuntimeException("Invalid loan account state to execute disbursement");
        }

        loan.setState(LoanState.ACTIVE);
        updateLoanPort.updateLoan(loan, persistenceTransactionManager);
    }

    @Override
    public void makeRepayment(Long loanId, List<Installment> newInstallments, PersistenceTransactionManager persistenceTransactionManager) {

        var loan = loadLoanPort.loadLoan(loanId);

        if (!loan.canRepay()) {
            throw new RuntimeException("Invalid loan account state to finalize repayment");
        }

        if (newInstallments.stream().allMatch(i -> i.getState().equals(InstallmentState.PAID))) {
            loan.setState(LoanState.PAID);
        }

        updateInstallmentUseCase.updateInstallments(loanId, newInstallments, persistenceTransactionManager);

        updateLoanPort.updateLoan(loan, persistenceTransactionManager);
    }

    @Override
    public void dailyAccrual(Long loanId, Date date, PersistenceTransactionManager persistenceTransactionManager) {
        var loan = loadLoanPort.loadLoan(loanId);

        if (!loan.canAccrue()) {
            throw new RuntimeException("Invalid loan account state to execute accrual");
        }

        var daysFromDisbursement = ChronoUnit.DAYS.between(DateTimeUtil.convertToLocalDateTimeViaMilisecond(loan.getDisbursementDate()), DateTimeUtil.convertToLocalDateTimeViaMilisecond(new Date()));

        if (daysFromDisbursement <= 0L) {
            throw new RuntimeException("Invalid accrual, disbursement >= today");
        }

        var dailyAccrualAmount = accrualUseCase.calculateDailyAccrual(loan.getAmount(), loan.getInterest(), daysFromDisbursement);

        var installments = loadInstallmentsQuery.loadInstallments(loanId);

        if (installments.getInstallments().stream().anyMatch(i -> isToday(i.getDueDate()))){

            // ToDo - check if interest has already been applied for the date

            if (dailyAccrualAmount.compareTo(BigDecimal.ZERO) > 0)
                createLoanLedgerEntryUseCase.interestApplied(loanId, dailyAccrualAmount, persistenceTransactionManager);

            loan.setAccruedInterest(BigDecimal.ZERO);
        } else {
            loan.setAccruedInterest(dailyAccrualAmount);
        }


        loan.setLastAccrualDate(date);
        loan.setBatchBlock(StaticInformation.generateRandomBatchBlock());
        updateLoanPort.updateLoan(loan, persistenceTransactionManager);
    }

    private Boolean isToday(Date date){
        return ChronoUnit.DAYS.between(DateTimeUtil.convertToLocalDateTimeViaMilisecond(date), DateTimeUtil.convertToLocalDateTimeViaMilisecond(new Date())) == 0;
    }
}
