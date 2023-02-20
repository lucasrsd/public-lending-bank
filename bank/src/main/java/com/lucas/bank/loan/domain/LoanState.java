package com.lucas.bank.loan.domain;

import lombok.ToString;

@ToString
public enum LoanState {
    DRAFT,
    PENDING_APPROVAL,
    PENDING_DISBURSEMENT,
    ACTIVE,
    LATE,
    CANCELLED
}
