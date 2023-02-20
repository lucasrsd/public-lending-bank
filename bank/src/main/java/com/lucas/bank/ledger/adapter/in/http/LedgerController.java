package com.lucas.bank.ledger.adapter.in.http;

import com.lucas.bank.ledger.adapter.in.contracts.LoanDailyAccrualRequest;
import com.lucas.bank.ledger.application.port.in.DailyInterestAccrualUseCase;
import com.lucas.bank.ledger.application.port.out.LedgerAggregate;
import com.lucas.bank.shared.adapters.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/ledger")
public class LedgerController {

    private final Logger LOG = LoggerFactory.getLogger(LedgerController.class);
    private final DailyInterestAccrualUseCase dailyInterestAccrualUseCase;

    @PostMapping("loan/accrual/today")
    LedgerAggregate loanAccrual(@Valid @RequestBody LoanDailyAccrualRequest request) {
        LOG.info("Starting loan accrual for loan account: {}", request.getLoanId());
        return dailyInterestAccrualUseCase.loanAccountDailyAccrual(request.getLoanId(), new Date());
    }
}
