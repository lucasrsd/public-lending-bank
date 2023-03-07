package com.lucas.bank.ledger.adapter.in.http;

import com.lucas.bank.ledger.application.port.in.LoadLedgerQuery;
import com.lucas.bank.ledger.application.port.out.SummaryAggregate;
import com.lucas.bank.shared.adapters.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/ledger")
public class LedgerController {

    private final LoadLedgerQuery loadLedgerQuery;

    @GetMapping(path = "/summary")
    SummaryAggregate get() {
        return loadLedgerQuery.summary();
    }
}
