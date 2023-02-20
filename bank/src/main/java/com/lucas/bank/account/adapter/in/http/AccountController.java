package com.lucas.bank.account.adapter.in.http;

import com.lucas.bank.account.adapter.in.contracts.CreateAccountRequest;
import com.lucas.bank.account.adapter.in.contracts.CreateAccountResponse;
import com.lucas.bank.account.application.port.in.CreateAccountUseCase;
import com.lucas.bank.account.application.port.in.LoadAccountQuery;
import com.lucas.bank.account.domain.Account;
import com.lucas.bank.shared.adapters.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/account")
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;

    private final LoadAccountQuery loadAccountQuery;

    @PostMapping
    CreateAccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request) {
        var newAccountId = createAccountUseCase.createAccount(request.mapToCommand());
        var account = loadAccountQuery.loadAccount(newAccountId);
        return CreateAccountResponse.mapToResponse(account);
    }

    @GetMapping(path = "/{accountId}")
    CreateAccountResponse loadAccount(@PathVariable("accountId") Long accountId) {
        var account = loadAccountQuery.loadAccount(accountId);
        return CreateAccountResponse.mapToResponse(account);
    }
}
