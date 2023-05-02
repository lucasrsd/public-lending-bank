package com.lucas.bank.ledger.adapter.in.contracts;

import com.lucas.bank.ledger.application.port.in.SearchLedgerCommand;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
public class SearchLedgerRequest {

    @NotNull
    public String startDate;

    @NotNull
    public String endDate;

    public SearchLedgerCommand mapToCommand() {
        return SearchLedgerCommand
                .builder()
                .startDate(LocalDate.parse(startDate, StaticInformation.DATE_FORMATTER).atStartOfDay())
                .endDate(LocalDate.parse(endDate, StaticInformation.DATE_FORMATTER).atTime(23, 59, 59))
                .build();
    }
}
