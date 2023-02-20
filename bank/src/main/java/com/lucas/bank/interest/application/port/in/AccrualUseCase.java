package com.lucas.bank.interest.application.port.in;

import java.math.BigDecimal;

public interface AccrualUseCase {
    BigDecimal calculateDailyAccrual(AccrualCommand command);

}