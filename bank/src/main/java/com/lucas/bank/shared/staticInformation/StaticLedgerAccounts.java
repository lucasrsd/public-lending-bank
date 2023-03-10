package com.lucas.bank.shared.staticInformation;

import com.lucas.bank.ledger.domain.AccountType;
import com.lucas.bank.ledger.domain.LedgerAccount;

public class StaticLedgerAccounts {
    public static final LedgerAccount LOAN_SOURCE = LedgerAccount
            .builder()
            .ledgerAccountId(1)
            .name("SOURCE")
            .accountType(AccountType.ASSET)
            .build();

    public static final LedgerAccount LOAN_PORTFOLIO = LedgerAccount
            .builder()
            .ledgerAccountId(2)
            .name("PORTFOLIO")
            .accountType(AccountType.ASSET)
            .build();

    public static final LedgerAccount LOAN_INTEREST_RECEIVABLE = LedgerAccount
            .builder()
            .ledgerAccountId(3)
            .name("INTEREST_RECEIVABLE")
            .accountType(AccountType.ASSET)
            .build();

    public static final LedgerAccount LOAN_TAXES_RECEIVABLE = LedgerAccount
            .builder()
            .ledgerAccountId(4)
            .name("TAXES_RECEIVABLE")
            .accountType(AccountType.ASSET)
            .build();

    public static final LedgerAccount LOAN_INTEREST_INCOME = LedgerAccount
            .builder()
            .ledgerAccountId(5)
            .name("INTEREST_INCOME")
            .accountType(AccountType.INCOME)
            .build();

    public static final LedgerAccount LOAN_TAX_PAYABLE = LedgerAccount
            .builder()
            .ledgerAccountId(6)
            .name("TAX_PAYABLE")
            .accountType(AccountType.INCOME)
            .build();

    public static final LedgerAccount LOAN_TAX_RECEIVABLE = LedgerAccount
            .builder()
            .ledgerAccountId(7)
            .name("TAX_RECEIVABLE")
            .accountType(AccountType.INCOME)
            .build();

}
