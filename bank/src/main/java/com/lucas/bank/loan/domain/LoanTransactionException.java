package com.lucas.bank.loan.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LoanTransactionException extends ResponseStatusException {
    public LoanTransactionException(String reason) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
    }
}
