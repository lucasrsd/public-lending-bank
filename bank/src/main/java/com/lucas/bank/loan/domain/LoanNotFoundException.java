package com.lucas.bank.loan.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LoanNotFoundException extends ResponseStatusException {
    public LoanNotFoundException(Long loanId) {
        super(HttpStatus.NOT_FOUND, "Loan id: " + loanId + " not found");
    }
}
