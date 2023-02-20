package com.lucas.bank.account.application.service;

import com.lucas.bank.account.domain.Account;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(Account account) {
        super(String.format("Problems performing transaction: Account %s", account.getAccountId()));
    }
}