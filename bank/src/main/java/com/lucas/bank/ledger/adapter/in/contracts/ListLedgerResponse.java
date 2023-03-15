package com.lucas.bank.ledger.adapter.in.contracts;

import com.lucas.bank.ledger.application.port.out.LedgerEntriesAggregate;
import com.lucas.bank.ledger.domain.Ledger;
import com.lucas.bank.shared.valueObjects.Metadata;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Value
@Builder
public class ListLedgerResponse {
    public List<LedgerResponse> ledgers;
    public Metadata metadata;

    public static ListLedgerResponse empty() {
        return new ListLedgerResponse(null, Metadata.of(0));
    }

    public static ListLedgerResponse of(List<LedgerResponse> ledgerResponses) {
        return new ListLedgerResponse(ledgerResponses, Metadata.of(ledgerResponses.size()));
    }

    public static ListLedgerResponse mapToResponse(LedgerEntriesAggregate ledgerEntriesAggregate) {
        if (ledgerEntriesAggregate == null || ledgerEntriesAggregate.getEntries() == null) return empty();

        List<LedgerResponse> ledgerList = new ArrayList<>();

        for (Ledger ledger : ledgerEntriesAggregate.getEntries()) {
            ledgerList.add(LedgerResponse.mapToResponse(ledger));
        }

        Collections.sort(ledgerList, Comparator.comparing(LedgerResponse::getDate).reversed());

        return of(ledgerList);
    }
}
