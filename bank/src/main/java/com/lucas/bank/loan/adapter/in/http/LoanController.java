package com.lucas.bank.loan.adapter.in.http;

import com.lucas.bank.loan.adapter.in.contracts.CreateLoanResponse;
import com.lucas.bank.shared.adapters.WebAdapter;
import com.lucas.bank.loan.adapter.in.contracts.CreateLoanRequest;
import com.lucas.bank.loan.application.port.in.CreateLoanUseCase;
import com.lucas.bank.loan.application.port.in.LoadLoanQuery;
import com.lucas.bank.loan.application.port.out.LoanAggregate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/loan")
public class LoanController {

    private final CreateLoanUseCase createLoanUseCase;
    private final LoadLoanQuery loadLoanQuery;

    @PostMapping
    CreateLoanResponse getLoan(@Valid @RequestBody CreateLoanRequest request) {
        var loanId = createLoanUseCase.createLoan(request.mapToCommand());
        return CreateLoanResponse.mapToResponse(loadLoanQuery.loadLoan(loanId));
    }
}
