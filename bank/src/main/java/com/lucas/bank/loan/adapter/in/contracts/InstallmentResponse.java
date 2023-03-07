package com.lucas.bank.loan.adapter.in.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucas.bank.shared.PayableAmount;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Value
@Builder
public class InstallmentResponse {

    public Integer number;
    public String amortizationType;
    public String state;

    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date dueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    public Date paymentDate;

    public PayableAmount principal;
    public PayableAmount interest;
    public PayableAmount tax;
    public BigDecimal installmentAmount;

    public BigDecimal remainingBalance;
    public Map<String, BigDecimal> taxComposition;

}
