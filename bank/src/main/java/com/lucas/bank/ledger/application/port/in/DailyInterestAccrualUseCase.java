package com.lucas.bank.ledger.application.port.in;

import com.lucas.bank.ledger.application.port.out.LedgerAggregate;

import java.util.Date;

public interface DailyInterestAccrualUseCase {
    LedgerAggregate loanAccountDailyAccrual(Long loanAccountId, Date bookingDate);
}