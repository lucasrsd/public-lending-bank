package com.lucas.bank.installment.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InstallmentRepaymentCalculationException extends ResponseStatusException {
    public InstallmentRepaymentCalculationException(String reason) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
    }
}
