package com.lucas.bank.transaction.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class RepaymentException extends ResponseStatusException {
    public RepaymentException(String reason) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
    }
}
