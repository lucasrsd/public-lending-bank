package com.lucas.bank.transactions.adapter.in.contracts;

import com.lucas.bank.loan.application.port.in.CreateLoanCommand;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;

@Data
public class RepayLoanRequest {

    @NotNull
    public Long loanId;

    @NotNull
    public BigDecimal amount;
}
