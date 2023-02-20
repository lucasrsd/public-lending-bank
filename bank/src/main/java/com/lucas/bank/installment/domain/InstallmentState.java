package com.lucas.bank.installment.domain;

import lombok.ToString;

@ToString
public enum InstallmentState {
    PENDING,
    PAID,
    LATE
}
