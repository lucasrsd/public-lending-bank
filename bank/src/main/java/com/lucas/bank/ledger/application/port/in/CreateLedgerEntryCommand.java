package com.lucas.bank.ledger.application.port.in;

import com.lucas.bank.ledger.domain.LedgerAccount;
import com.lucas.bank.ledger.domain.Side;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Value
@EqualsAndHashCode(callSuper = false)
@Builder
public class CreateLedgerEntryCommand {

    
    @NotNull
    private final Long loanId;

    @NotNull
    private final Long transactionId;
    @NotNull
    private final String name;
    
    @NotNull
    private final Side type;
    @NotNull
    private final BigDecimal amount;
    
    @NotNull
    private final LedgerAccount ledgerAccount;
    
    @NotNull
    private final Date bookingDate;

    public CreateLedgerEntryCommand(Long loanId,
                                    Long transactionId,
                                    String name,
                                    Side type,
                                    BigDecimal amount,
                                    LedgerAccount ledgerAccount,
                                    Date bookingDate) {
        this.loanId = loanId;
        this.transactionId = transactionId;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.ledgerAccount = ledgerAccount;
        this.bookingDate = bookingDate;
    }
}