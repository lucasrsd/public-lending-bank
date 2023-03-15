package com.lucas.bank.loan.application.service;

import com.lucas.bank.installment.application.port.in.LoadInstallmentsQuery;
import com.lucas.bank.installment.application.port.in.UpdateInstallmentUseCase;
import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.installment.domain.InstallmentState;
import com.lucas.bank.interest.application.port.in.AccrualUseCase;
import com.lucas.bank.loan.application.port.in.LoanTransactionUseCase;
import com.lucas.bank.loan.application.port.out.LoadLoanPort;
import com.lucas.bank.loan.application.port.out.UpdateLoanPort;
import com.lucas.bank.loan.domain.LoanState;
import com.lucas.bank.loan.domain.LoanTransactionException;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import com.lucas.bank.shared.util.DateTimeUtil;
import com.lucas.bank.transaction.application.port.in.InterestAppliedLoanUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@UseCase
public class LoanTransactionServiceTransaction implements LoanTransactionUseCase {

    private final Logger log = LoggerFactory.getLogger(LoanTransactionServiceTransaction.class);
    private final UpdateLoanPort updateLoanPort;
    private final LoadLoanPort loadLoanPort;
    private final LoadInstallmentsQuery loadInstallmentsQuery;
    private final UpdateInstallmentUseCase updateInstallmentUseCase;
    private final AccrualUseCase accrualUseCase;
    private final InterestAppliedLoanUseCase interestAppliedLoanUseCase;

    @Override
    public void activateLoan(Long loanId, UnitOfWork unitOfWork) {
        var loan = loadLoanPort.loadLoan(loanId);

        if (!loan.canDisburse()) {
            throw new LoanTransactionException("Invalid loan account state to execute disbursement");
        }

        loan.setState(LoanState.ACTIVE);
        updateLoanPort.updateLoan(loan, unitOfWork);
    }

    @Override
    public void makeRepayment(Long loanId, List<Installment> newInstallments, UnitOfWork unitOfWork) {

        var loan = loadLoanPort.loadLoan(loanId);

        if (!loan.canRepay()) {
            throw new LoanTransactionException("Invalid loan account state to finalize repayment");
        }

        if (newInstallments.stream().allMatch(i -> i.getState().equals(InstallmentState.PAID))) {
            loan.setState(LoanState.PAID);
        }

        updateInstallmentUseCase.updateInstallments(loanId, newInstallments, unitOfWork);

        updateLoanPort.updateLoan(loan, unitOfWork);
    }

    @Override
    public void dailyAccrual(Long loanId, LocalDateTime bookingDate, UnitOfWork unitOfWork) {
        var loan = loadLoanPort.loadLoan(loanId);

        if (!loan.canAccrue()) {
            throw new LoanTransactionException("Invalid loan account state to execute accrual");
        }

        var daysFromDisbursement = ChronoUnit.DAYS.between(DateTimeUtil.convertToMidnight(loan.getDisbursementDate()), DateTimeUtil.convertToMidnight(bookingDate));

        if (daysFromDisbursement <= 0L) {
            throw new LoanTransactionException("Invalid accrual, disbursement date <= today");
        }

        var dailyAccrualAmount = accrualUseCase.calculateDailyAccrual(loan.getAmount(), loan.getInterest(), daysFromDisbursement);

        var installments = loadInstallmentsQuery.loadInstallments(loanId);

        if (installments.getInstallments().stream().anyMatch(i -> DateTimeUtil.isSameDate(bookingDate, i.getDueDate()))){

            // ToDo - check if interest has already been applied for the date

            log.info("Loan: {} has an installment with due date = today, applying interest", loanId);

            if (dailyAccrualAmount.compareTo(BigDecimal.ZERO) > 0){
                interestAppliedLoanUseCase.applyInterest(loanId, dailyAccrualAmount, unitOfWork);
            }

            loan.setAccruedInterest(BigDecimal.ZERO);
        } else {
            log.info("Loan: {} doesn't have installments with due date = today, updating last accrual and skipping", loanId);
            loan.setAccruedInterest(dailyAccrualAmount);
        }


        loan.setLastAccrualDate(bookingDate);
        loan.setBatchBlock(StaticInformation.generateRandomBatchBlock());
        updateLoanPort.updateLoan(loan, unitOfWork);
    }
}
