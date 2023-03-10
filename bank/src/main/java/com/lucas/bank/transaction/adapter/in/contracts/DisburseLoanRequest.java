package com.lucas.bank.transaction.adapter.in.contracts;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DisburseLoanRequest {

    @NotNull
    public Long loanId;
}
