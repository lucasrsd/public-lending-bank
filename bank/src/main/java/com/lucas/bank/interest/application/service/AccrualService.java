package com.lucas.bank.interest.application.service;


import com.lucas.bank.interest.application.port.in.AccrualCommand;
import com.lucas.bank.interest.application.port.in.AccrualUseCase;
import com.lucas.bank.interest.domain.Accrual;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.loan.application.port.in.LoadLoanQuery;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@UseCase
public class AccrualService implements AccrualUseCase {

    private final LoadLoanQuery loadLoanQuery;

    @Override
    public BigDecimal calculateDailyAccrual(AccrualCommand command) {
        var loan = loadLoanQuery.loadLoan(command.getLoanId());
        var accrual = Accrual.builder().build();
        return accrual.dailyAccrual(loan.getLoan().getAmount(), loan.getLoan().getInterest());
    }
}
