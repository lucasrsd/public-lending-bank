package com.lucas.bank.account.adapter.in.contracts;

import com.lucas.bank.account.application.port.in.CreateAccountCommand;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
public class CreateAccountRequest {

    @NotNull
    public String holderName;

    @NotNull
    public Date holderBirthDay;

    public CreateAccountCommand mapToCommand() {
        return CreateAccountCommand
                .builder()
                .holderBirthDate(getHolderBirthDay())
                .holderName(getHolderName())
                .build();
    }

}
