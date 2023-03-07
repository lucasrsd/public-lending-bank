package com.lucas.bank.ledger.domain;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class LedgerAccount {
    private final Integer ledgerAccountId;
    private final String name;
    private final AccountType accountType;
}
