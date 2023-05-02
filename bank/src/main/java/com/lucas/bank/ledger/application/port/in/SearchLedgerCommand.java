package com.lucas.bank.ledger.application.port.in;

import com.lucas.bank.shared.SelfValidating;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@EqualsAndHashCode(callSuper = false)
@Builder
public class SearchLedgerCommand extends SelfValidating<SearchLedgerCommand> {

    @NotNull
    private final LocalDateTime startDate;

    @NotNull
    private final LocalDateTime endDate;

    public SearchLedgerCommand(
            LocalDateTime startDate,
            LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.validateSelf();
    }
}
