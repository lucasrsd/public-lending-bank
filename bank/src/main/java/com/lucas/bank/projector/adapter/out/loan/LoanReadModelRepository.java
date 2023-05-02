package com.lucas.bank.projector.adapter.out.loan;

import com.lucas.bank.shared.mySql.MySqlCommandClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LoanReadModelRepository extends MySqlCommandClient {
    public void save(LoanReadModel loan){

        delete(loan.getLoanId());

        var query = "INSERT INTO loans " +
                "(loan_id, loan_type, account_id, amount, state, term, interest_rate, interest_frequency, creation_date, disbursement_date, last_accrual_date, accrued_interest, batch_block) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        List<Object> parameters = new ArrayList<>();
        parameters.add(loan.getLoanId());
        parameters.add(loan.getType());
        parameters.add(loan.getAccountId());
        parameters.add(loan.getAmount());
        parameters.add(loan.getLoanState());
        parameters.add(loan.getTerm());
        parameters.add(loan.getInterestRate());
        parameters.add(loan.getInterestFrequency());
        parameters.add(loan.getCreationDate());
        parameters.add(loan.getDisbursementDate());
        parameters.add(loan.getLastAccrualDate());
        parameters.add(loan.getAccruedInterest());
        parameters.add(loan.getBatchBlock());

        executeCommand(query, parameters);
    }

    public void delete(Long id)
    {
        var query = "DELETE FROM loans where loan_id = ?";

        List<Object> parameters = new ArrayList<>();
        parameters.add(id);
        executeCommand(query, parameters);
    }
}