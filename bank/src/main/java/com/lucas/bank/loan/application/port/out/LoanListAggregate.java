package com.lucas.bank.loan.application.port.out;

import com.lucas.bank.loan.domain.Loan;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@Builder
@EqualsAndHashCode(callSuper = false)
public class LoanListAggregate {
    private final List<Loan> loans;
}
