package com.lucas.bank.transaction.adapter.in.contracts;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class RepayLoanRequest {

    @NotNull
    public Long loanId;

    @NotNull
    @Digits(integer = 18, fraction = 2)
    @Min(1)
    public BigDecimal amount;
}
