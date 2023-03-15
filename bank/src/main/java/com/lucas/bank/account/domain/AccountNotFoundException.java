package com.lucas.bank.account.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccountNotFoundException extends ResponseStatusException {
    public AccountNotFoundException(Long accountId) {
        super(HttpStatus.NOT_FOUND, "Account id: " + accountId + " not found");
    }
}
