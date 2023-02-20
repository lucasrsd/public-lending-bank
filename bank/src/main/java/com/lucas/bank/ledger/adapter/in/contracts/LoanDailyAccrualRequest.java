package com.lucas.bank.ledger.adapter.in.contracts;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
public class LoanDailyAccrualRequest {

    @NotNull
    private Long loanId;
}
