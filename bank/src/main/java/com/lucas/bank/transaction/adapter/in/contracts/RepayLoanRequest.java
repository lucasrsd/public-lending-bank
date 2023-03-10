package com.lucas.bank.transaction.adapter.in.contracts;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class RepayLoanRequest {

    @NotNull
    public Long loanId;

    @NotNull
    public BigDecimal amount;
}
