package com.lucas.bank.shared;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ParameterConsistencyException extends ResponseStatusException {
    public ParameterConsistencyException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
