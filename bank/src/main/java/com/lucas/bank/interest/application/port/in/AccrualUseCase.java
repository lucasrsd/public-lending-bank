package com.lucas.bank.interest.application.port.in;

import com.lucas.bank.interest.domain.Interest;

import java.math.BigDecimal;


public interface AccrualUseCase {
    BigDecimal calculateDailyAccrual(BigDecimal amount, Interest interest, Long numberOfDays);
}