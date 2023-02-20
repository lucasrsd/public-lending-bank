package com.lucas.bank.account.application.port.in;

public interface CreateAccountUseCase {

    Long createAccount(CreateAccountCommand command);

}
