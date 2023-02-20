package com.lucas.bank.ledger.application.service;

import com.lucas.bank.interest.application.port.in.AccrualCommand;
import com.lucas.bank.interest.application.port.in.AccrualUseCase;
import com.lucas.bank.ledger.application.port.in.DailyInterestAccrualUseCase;
import com.lucas.bank.ledger.application.port.in.LoanAccrualLedgerCommand;
import com.lucas.bank.ledger.application.port.in.LoanAccrualLedgerUseCase;
import com.lucas.bank.ledger.application.port.out.LedgerAggregate;
import com.lucas.bank.shared.adapters.UseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@RequiredArgsConstructor
@UseCase
public class DailyInterestAccrualService implements DailyInterestAccrualUseCase {

    private final Logger LOG = LoggerFactory.getLogger(DailyInterestAccrualService.class);

    private final AccrualUseCase accrualUseCase;

    private final LoanAccrualLedgerUseCase loanAccrualLedgerUseCase;

    @Override
    public LedgerAggregate loanAccountDailyAccrual(Long loanAccountId, Date bookingDate) {

        LOG.info("Starting loan accrual for account {} using bookingDate {}", loanAccountId, bookingDate);

        var dailyAccrualCommand = AccrualCommand
                .builder()
                .loanAccountId(loanAccountId)
                .referenceDate(bookingDate)
                .build();

        var accrual = accrualUseCase.calculateDailyAccrual(dailyAccrualCommand);

        var loanAccrualCommand = LoanAccrualLedgerCommand
                .builder()
                .loanAccountId(loanAccountId)
                .bookingDate(bookingDate)
                .accrualAmount(accrual)
                .build();

        return loanAccrualLedgerUseCase.execute(loanAccrualCommand);
    }
}
