package com.lucas.bank.taxes.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class InstallmentDetails {
    private final Integer number;
    private final Date dueDate;
    private final BigDecimal principalAmount;
}
