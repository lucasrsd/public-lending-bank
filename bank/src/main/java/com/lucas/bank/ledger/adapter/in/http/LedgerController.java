package com.lucas.bank.ledger.adapter.in.http;

import com.lucas.bank.ledger.adapter.in.contracts.ListLedgerResponse;
import com.lucas.bank.ledger.application.port.in.LoadLedgerQuery;
import com.lucas.bank.ledger.application.port.out.LedgerEntriesAggregate;
import com.lucas.bank.shared.adapters.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/ledger")
public class LedgerController {

    private final LoadLedgerQuery loadLedgerQuery;

    @GetMapping(path = "/{loanId}")
    ListLedgerResponse get(@PathVariable("loanId") Long loanId) {
        var result = loadLedgerQuery.loadLedgers(loanId);

        return ListLedgerResponse.mapToResponse(result);
    }

    @GetMapping(path = "/summary")
    Map<String, BigDecimal> summary() {
        return loadLedgerQuery.summary();
    }

}
