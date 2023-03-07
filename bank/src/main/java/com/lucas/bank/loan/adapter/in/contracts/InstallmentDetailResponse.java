package com.lucas.bank.loan.adapter.in.contracts;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Map;

@Value
@Builder
public class InstallmentDetailResponse {
    public BigDecimal installmentsTotalAmount;
    public BigDecimal principalTotalAmount;
    public BigDecimal interestTotalAmount;
    public BigDecimal taxesTotalAmount;
    public Map<String, BigDecimal> taxes;

}
