package com.lucas.bank.projector.adapter.out.account;

import com.lucas.bank.shared.mySql.MySqlCommandClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountReadModelRepository extends MySqlCommandClient {
    public void save(AccountReadModel account){

        delete(account.getAccountId());

        var query = "INSERT INTO accounts (ID, HOLDER_NAME, HOLDER_BIRTH_DATE, CREATED_AT) " +
                "VALUES (?, ?, ?, ?)";

        List<Object> parameters = new ArrayList<>();
        parameters.add(account.getAccountId());
        parameters.add(account.getHolderName());
        parameters.add(account.getHolderBirthDate());
        parameters.add(account.getCreatedAt());

        executeCommand(query, parameters);
    }

    public void delete(Long id)
    {
        var query = "DELETE FROM accounts where ID = ?";

        List<Object> parameters = new ArrayList<>();
        parameters.add(id);
        executeCommand(query, parameters);
    }
}