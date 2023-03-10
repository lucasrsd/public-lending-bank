package com.lucas.bank.tax.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class InstallmentTax {
    private Integer number;
    private BigDecimal totalTaxAmount;
    private Map<String, BigDecimal> composition;
}
