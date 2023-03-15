package com.lucas.bank.loan.adapter.in.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucas.bank.shared.valueObjects.PayableAmount;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Value
@Builder
public class InstallmentResponse {

    public Integer number;
    public String amortizationType;
    public String state;

    public LocalDateTime dueDate;

    public LocalDateTime paymentDate;

    public PayableAmount principal;
    public PayableAmount interest;
    public PayableAmount tax;
    public BigDecimal installmentAmount;

    public BigDecimal remainingBalance;
    public Map<String, BigDecimal> taxComposition;

}
