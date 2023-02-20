package com.lucas.bank.account.application.service;

import com.lucas.bank.account.application.port.in.CreateAccountCommand;
import com.lucas.bank.account.application.port.in.CreateAccountUseCase;
import com.lucas.bank.account.application.port.out.CreateAccountPort;
import com.lucas.bank.account.domain.Account;
import com.lucas.bank.shared.adapters.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
@UseCase
public class CreateAccountService implements CreateAccountUseCase {

    private final CreateAccountPort createAccountPort;

    @Override
    public Long createAccount(CreateAccountCommand command) {
        var account = Account
                .builder()
                .accountId(null)
                .holderName(command.getHolderName())
                .holderBirthDate(command.getHolderBirthDate())
                .active(false)
                .createdAt(new Date())
                .build();

        return createAccountPort.createAccount(account);
    }
}