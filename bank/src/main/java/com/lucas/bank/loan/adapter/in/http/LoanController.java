package com.lucas.bank.loan.adapter.in.http;

import com.lucas.bank.interest.application.port.in.AccrualUseCase;
import com.lucas.bank.loan.adapter.in.contracts.ListLoanResponse;
import com.lucas.bank.loan.adapter.in.contracts.LoanOverviewResponse;
import com.lucas.bank.loan.application.port.in.LoanTransactionUseCase;
import com.lucas.bank.shared.adapters.WebAdapter;
import com.lucas.bank.loan.adapter.in.contracts.CreateLoanRequest;
import com.lucas.bank.loan.application.port.in.CreateLoanUseCase;
import com.lucas.bank.loan.application.port.in.LoadLoanQuery;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;


@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/loan")
public class LoanController {

    private final CreateLoanUseCase createLoanUseCase;
    private final LoadLoanQuery loadLoanQuery;
    @PostMapping
    LoanOverviewResponse create(@Valid @RequestBody CreateLoanRequest request) {
        var transaction = UnitOfWork.newInstance();
        var loanId = createLoanUseCase.createLoan(request.mapToCommand(), transaction);
        transaction.commit();
        return LoanOverviewResponse.mapToResponse(loadLoanQuery.loadLoan(loanId));
    }

    @GetMapping(path = "/{loanId}")
    LoanOverviewResponse get(@PathVariable("loanId") Long loanId) {
        return LoanOverviewResponse.mapToResponse(loadLoanQuery.loadLoan(loanId));
    }

    @GetMapping(path = "/list")
    ListLoanResponse list() {
        return LoanOverviewResponse.mapToResponse(loadLoanQuery.listLoans().getLoans());
    }
}
