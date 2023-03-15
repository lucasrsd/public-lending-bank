package com.lucas.bank.shared;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DistributedLockException extends ResponseStatusException {
    public DistributedLockException(String reason) {
        super(HttpStatus.CONFLICT, reason);
    }
}
