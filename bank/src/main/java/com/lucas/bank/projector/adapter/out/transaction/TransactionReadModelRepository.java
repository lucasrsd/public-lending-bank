package com.lucas.bank.projector.adapter.out.transaction;

import com.lucas.bank.shared.mySql.MySqlCommandClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionReadModelRepository extends MySqlCommandClient {
    public void save(TransactionReadModel transactionReadModel){

        delete(transactionReadModel.getTransactionId());

        var query = "INSERT INTO transactions " +
                "(transaction_id, loan_id, `date`, `type`, amount) " +
                "VALUES(?, ?, ?, ?, ?) ";

        List<Object> parameters = new ArrayList<>();
        parameters.add(transactionReadModel.getTransactionId());
        parameters.add(transactionReadModel.getLoanId());
        parameters.add(transactionReadModel.getDate());
        parameters.add(transactionReadModel.getType());
        parameters.add(transactionReadModel.getAmount());

        executeCommand(query, parameters);
    }

    public void delete(Long id)
    {
        var query = "DELETE FROM transactions where transaction_id = ?";

        List<Object> parameters = new ArrayList<>();
        parameters.add(id);
        executeCommand(query, parameters);
    }
}