package com.lucas.bank.projector.adapter.out.ledger;

import com.lucas.bank.shared.mySql.MySqlCommandClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LedgerReadModelRepository extends MySqlCommandClient {
    public void save(LedgerReadModel ledgerReadModel){

        delete(ledgerReadModel.getLedgerTransactionEntryId());

        var query = "INSERT INTO ledger " +
                "(entry_id, loan_id, transaction_id, transaction_type, transaction_side, transaction_amount, ledger_account_id, ledger_account_name, ledger_account_type, ledger_date, ledger_booking_date) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        List<Object> parameters = new ArrayList<>();
        parameters.add(ledgerReadModel.getLedgerTransactionEntryId());
        parameters.add(ledgerReadModel.getLoanId());
        parameters.add(ledgerReadModel.getLedgerTransactionId());
        parameters.add(ledgerReadModel.getLedgerTransactionType());
        parameters.add(ledgerReadModel.getLedgerTransactionSide());
        parameters.add(ledgerReadModel.getLedgerTransactionAmount());
        parameters.add(ledgerReadModel.getLedgerAccountId());
        parameters.add(ledgerReadModel.getLedgerAccountName());
        parameters.add(ledgerReadModel.getLedgerAccountType());
        parameters.add(ledgerReadModel.getLedgerDate());
        parameters.add(ledgerReadModel.getLedgerBookingDate());

        executeCommand(query, parameters);
    }

    public void delete(String id)
    {
        var query = "DELETE FROM ledger where entry_id = ?";

        List<Object> parameters = new ArrayList<>();
        parameters.add(id);
        executeCommand(query, parameters);
    }
}