package com.lucas.bank.transaction.adapter.in.http;

import com.lucas.bank.shared.adapters.DistributedLock;
import com.lucas.bank.shared.adapters.WebAdapter;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import com.lucas.bank.transaction.adapter.in.contracts.DisburseLoanRequest;
import com.lucas.bank.transaction.adapter.in.contracts.RepayLoanRequest;
import com.lucas.bank.transaction.application.port.in.DisburseLoanUseCase;
import com.lucas.bank.transaction.application.port.in.LoadTransactionQuery;
import com.lucas.bank.transaction.application.port.in.RepayLoanUseCase;
import com.lucas.bank.transaction.application.port.out.TransactionAggregate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/loan-transactions")
public class LoanTransactionsController {

    private final DisburseLoanUseCase disburseLoanUseCase;
    private final RepayLoanUseCase repayLoanUseCase;
    private final DistributedLock distributedLock;
    private final LoadTransactionQuery loadTransactionQuery;

    @GetMapping(path = "/{loanId}")
    TransactionAggregate get(@PathVariable("loanId") Long loanId) {
        return loadTransactionQuery.forLoan(loanId);
    }

    @PostMapping(path = "disburse")
    void disburse(@Valid @RequestBody DisburseLoanRequest request) {

        // ToDo - review where to inject lock and transaction together (or not)
        // ToDo - check multiple partitions with same keys

        distributedLock.tryAcquire(request.getLoanId().toString());
        var transaction = UnitOfWork.newInstance();
        disburseLoanUseCase.disburse(request.getLoanId(), transaction);
        transaction.commit();
        distributedLock.release();
    }

    @PostMapping(path = "repayment")
    void repayment(@Valid @RequestBody RepayLoanRequest request) {
        distributedLock.tryAcquire(request.getLoanId().toString());
        var transaction = UnitOfWork.newInstance();
        repayLoanUseCase.repayment(request.getLoanId(), request.getAmount(), transaction);
        transaction.commit();
        distributedLock.release();
    }
}
