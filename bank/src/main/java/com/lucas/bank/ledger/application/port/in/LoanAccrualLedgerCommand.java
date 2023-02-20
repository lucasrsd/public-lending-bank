package com.lucas.bank.ledger.application.port.in;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Value
@EqualsAndHashCode(callSuper = false)
@Builder
public class LoanAccrualLedgerCommand {
    @NotNull
    private final Long loanAccountId;

    @NotNull
    private final BigDecimal accrualAmount;

    @NotNull
    private final Date bookingDate;

    public LoanAccrualLedgerCommand(Long loanAccountId, BigDecimal accrualAmount, Date bookingDate) {
        this.loanAccountId = loanAccountId;
        this.accrualAmount = accrualAmount;
        this.bookingDate = bookingDate;
    }
}