package com.lucas.bank.account.adapter.in.contracts;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucas.bank.account.domain.Account;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
public class CreateAccountResponse {
    private final Long accountId;
    private final String holderName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final Date holderBirthDate;
    private final Boolean active;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private final Date createdAt;

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
