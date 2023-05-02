package com.lucas.bank.account.adapter.in.contracts;

import com.lucas.bank.account.application.port.in.CreateAccountCommand;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Data
public class CreateAccountRequest {

    @NotNull
    public String holderName;

    @NotNull
    public String holderBirthDay;

    public CreateAccountCommand mapToCommand() {
        return CreateAccountCommand
                .builder()
                .holderBirthDate(LocalDate.parse(holderBirthDay, StaticInformation.DATE_FORMATTER).atStartOfDay())
                .holderName(getHolderName())
                .build();
    }

}
