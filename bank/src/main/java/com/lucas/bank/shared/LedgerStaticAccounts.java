package com.lucas.bank.shared;

import com.lucas.bank.ledger.domain.AccountType;
import com.lucas.bank.ledger.domain.LedgerAccount;

public class LedgerStaticAccounts {
    public static final LedgerAccount ASSET_LENDING = LedgerAccount
            .builder()
            .ledgerAccountId(1)
            .accountType(AccountType.ASSET)
            .build();

    public static final LedgerAccount LIABILITY_LENDING = LedgerAccount
            .builder()
            .ledgerAccountId(2)
            .accountType(AccountType.LIABILITY)
            .build();
}
