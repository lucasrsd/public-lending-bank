package com.lucas.bank.ledger.adapter.in.contracts;

import com.lucas.bank.ledger.domain.LedgerAccount;
import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class LedgerAccountResponse {
    public Integer ledgerAccountId;
    public String name;
    public String accountType;

    public static LedgerAccountResponse mapToResponse(LedgerAccount ledgerAccount){
        if (ledgerAccount == null)
            return null;

        return LedgerAccountResponse
                .builder()
                .ledgerAccountId(ledgerAccount.getLedgerAccountId())
                .name(ledgerAccount.getName())
                .accountType(ledgerAccount.getAccountType().name())
                .build();
    }
}
