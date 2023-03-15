package com.lucas.bank.account.adapter.in.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucas.bank.account.domain.Account;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;


@Value
@Builder
public class CreateAccountResponse {
    private final Long accountId;
    private final String holderName;
    private final LocalDateTime holderBirthDate;
    private final Boolean active;
    private final LocalDateTime createdAt;

    public static CreateAccountResponse mapToResponse(Account account){
        return CreateAccountResponse
                .builder()
                .accountId(account.getAccountId())
                .createdAt(account.getCreatedAt())
                .active(account.getActive())
                .holderBirthDate(account.getHolderBirthDate())
                .holderName(account.getHolderName())
                .build();
    }
}
