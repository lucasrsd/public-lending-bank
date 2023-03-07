package com.lucas.bank.transactions.adapter.in.contracts;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class DisburseLoanRequest {

    @NotNull
    public Long loanId;
}
