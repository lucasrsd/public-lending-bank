package com.lucas.bank.tax.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class InstallmentDetails {
    private final Integer number;
    private final LocalDateTime dueDate;
    private final BigDecimal principalAmount;
}
