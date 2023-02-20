package com.lucas.bank.loan.adapter.out;

import com.lucas.bank.shared.adapters.PersistenceAdapter;
import com.lucas.bank.loan.application.port.out.CreateLoanPort;
import com.lucas.bank.loan.application.port.out.LoadLoanPort;
import com.lucas.bank.loan.domain.Loan;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@PersistenceAdapter
class LoanPersistenceAdapter implements CreateLoanPort, LoadLoanPort {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    @Override
    public Long createLoan(Loan loan) {
        var loanPOJO = loanMapper.mapToPOJO(loan);
        loanRepository.put(loanPOJO);
        return loanPOJO.getLoanId();
    }

    @Override
    public Loan loadLoan(Long loanId) {
        var loanPOJO = loanRepository.get(LoanPOJO.buildPk(loanId), LoanPOJO.buildSk());
        if (loanPOJO == null) throw new RuntimeException("Loan not found: " + loanId);
        return loanMapper.mapToDomainEntity(loanPOJO);
    }
}
