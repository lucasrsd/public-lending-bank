package com.lucas.bank.loan.adapter.out;

import com.lucas.bank.loan.application.port.out.UpdateLoanPort;
import com.lucas.bank.shared.adapters.AtomicCounter;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import com.lucas.bank.shared.adapters.PersistenceAdapter;
import com.lucas.bank.loan.application.port.out.CreateLoanPort;
import com.lucas.bank.loan.application.port.out.LoadLoanPort;
import com.lucas.bank.loan.domain.Loan;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;
import lombok.RequiredArgsConstructor;

import java.util.*;


@RequiredArgsConstructor
@PersistenceAdapter
class LoanPersistenceAdapter implements CreateLoanPort, LoadLoanPort, UpdateLoanPort {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final AtomicCounter atomicCounter;

    @Override
    public Long createLoan(Loan loan, UnitOfWork unitOfWork) {

        if (loan.getLoanId() == null){
            loan.setLoanId(atomicCounter.generate());
        }

        if (loan.getBatchBlock() == null){
            loan.setBatchBlock(StaticInformation.generateRandomBatchBlock());
        }

        var loanPOJO = loanMapper.mapToPOJO(loan);
        unitOfWork.addTransaction(loanPOJO);
        return loanPOJO.getLoanId();
    }

    @Override
    public Loan updateLoan(Loan loan, UnitOfWork unitOfWork) {
        var loanPOJO = loanMapper.mapToPOJO(loan);
        unitOfWork.addTransaction(loanPOJO);
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

    @Override
    public Map<Long, String> listLoanByBatchBlock(Integer batchBlock, String status) {
        var loansPOJO = loanRepository.scanIndexByBatchBlockAndStatus(StaticInformation.LOAN_STATE_GSI_INDEX, batchBlock, status);

        Map<Long, String> loans = new HashMap<>();

        for(LoanPOJO loan : loansPOJO){
            loans.put(loan.getLoanId(), loan.getLoanState());
        }

        return loans;
    }
}
