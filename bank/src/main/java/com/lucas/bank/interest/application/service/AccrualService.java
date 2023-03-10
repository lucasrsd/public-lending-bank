package com.lucas.bank.interest.application.service;


import com.lucas.bank.interest.application.port.in.AccrualUseCase;
import com.lucas.bank.interest.domain.Accrual;
import com.lucas.bank.interest.domain.Interest;
import com.lucas.bank.shared.adapters.UseCase;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@UseCase
public class AccrualService implements AccrualUseCase {
    @Override
    public BigDecimal calculateDailyAccrual(BigDecimal amount, Interest interest, Long numberOfDays) {
        var accrual = Accrual.builder().build();

        // ToDo - Loop every installment with the principal reductor to calculate properly the interest accrual

        return accrual.dailyAccrual(amount, interest, numberOfDays);
    }
}
