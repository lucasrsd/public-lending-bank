package com.lucas.bank.loan.adapter.out;

import com.lucas.bank.loan.application.port.out.UpdateLoanPort;
import com.lucas.bank.shared.adapters.PersistenceAdapter;
import com.lucas.bank.loan.application.port.out.CreateLoanPort;
import com.lucas.bank.loan.application.port.out.LoadLoanPort;
import com.lucas.bank.loan.domain.Loan;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@PersistenceAdapter
class LoanPersistenceAdapter implements CreateLoanPort, LoadLoanPort, UpdateLoanPort {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;

    @Override
    public Long createLoan(Loan loan, PersistenceTransactionManager persistenceTransactionManager) {
        var loanPOJO = loanMapper.mapToPOJO(loan);
        persistenceTransactionManager.addTransaction(loanPOJO);
        return loanPOJO.getLoanId();
    }

    @Override
    public Loan updateLoan(Loan loan, PersistenceTransactionManager persistenceTransactionManager) {
        var loanPOJO = loanMapper.mapToPOJO(loan);
        persistenceTransactionManager.addTransaction(loanPOJO);
        return loanMapper.mapToDomainEntity(loanPOJO);
    }

    @Override
    public Loan loadLoan(Long loanId) {
        var loanPOJO = loanRepository.get(LoanPOJO.of(loanId));
        if (loanPOJO == null) throw new RuntimeException("Loan not found: " + loanId);
        return loanMapper.mapToDomainEntity(loanPOJO);
    }

    @Override
    public List<Loan> listLoans() {
        var loansPOJO = loanRepository.listByPkBeginsWithAndSk(LoanPOJO.pkPrefix, LoanPOJO.skPrefix);
        List<Loan> loans = new ArrayList<>();
        loansPOJO.forEach(i -> loans.add(loanMapper.mapToDomainEntity(i)));
        return loans;
    }
}
