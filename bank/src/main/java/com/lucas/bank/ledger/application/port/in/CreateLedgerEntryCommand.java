package com.lucas.bank.ledger.application.port.in;

import com.lucas.bank.ledger.domain.LedgerAccount;
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
    private final String transactionType;

    @NotNull
    private final BigDecimal debitAmount;

    @NotNull
    private final BigDecimal creditAmount;

    @NotNull
    private final LedgerAccount ledgerDebitAccount;

    @NotNull
    private final LedgerAccount ledgerCreditAccount;

    @NotNull
    private final Date bookingDate;

    @NotNull
    private final Date transactionDate;

    public CreateLedgerEntryCommand(Long loanId,
                                    String transactionType,
                                    BigDecimal debitAmount,
                                    BigDecimal creditAmount,
                                    LedgerAccount ledgerDebitAccount,
                                    LedgerAccount ledgerCreditAccount,
                                    Date bookingDate,
                                    Date transactionDate) {
        this.loanId = loanId;
        this.transactionType = transactionType;
        this.debitAmount = debitAmount;
        this.creditAmount = creditAmount;
        this.ledgerDebitAccount = ledgerDebitAccount;
        this.ledgerCreditAccount = ledgerCreditAccount;
        this.bookingDate = bookingDate;
        this.transactionDate = transactionDate;
    }
}