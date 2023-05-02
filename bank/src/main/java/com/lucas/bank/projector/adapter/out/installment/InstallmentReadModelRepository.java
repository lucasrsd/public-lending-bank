package com.lucas.bank.projector.adapter.out.installment;

import com.lucas.bank.shared.mySql.MySqlCommandClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InstallmentReadModelRepository extends MySqlCommandClient {
    public void save(Long loanId, List<InstallmentReadModel> installments){

        delete(loanId);

        var query = "INSERT INTO installments " +
                "(loan_id, `number`, amortization_type, state, due_date, payment_date, principal_amount, interest_amount, installment_amount, tax_amount, remaining_balance, paid_principal, paid_interest, paid_tax, tax_additional_iof, tax_daily_iof) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Map<Integer, List<Object>> queries = new HashMap<>();
        var count = 0;
        for(InstallmentReadModel installmentRm : installments){
            List<Object> parameters = new ArrayList<>();
            parameters.add(loanId);
            parameters.add(installmentRm.getNumber());
            parameters.add(installmentRm.getAmortizationType());
            parameters.add(installmentRm.getInstallmentState());
            parameters.add(installmentRm.getDueDate());
            parameters.add(installmentRm.getPaymentDate());
            parameters.add(installmentRm.getPrincipalAmount());
            parameters.add(installmentRm.getInterestAmount());
            parameters.add(installmentRm.getInstallmentAmount());
            parameters.add(installmentRm.getTaxAmount());
            parameters.add(installmentRm.getRemainingBalance());
            parameters.add(installmentRm.getPaidPrincipalAmount());
            parameters.add(installmentRm.getPaidInterestAmount());
            parameters.add(installmentRm.getPaidTaxAmount());
            parameters.add(installmentRm.getTaxAdditionalIof());
            parameters.add(installmentRm.getTaxDailyIof());

            queries.put(count++, parameters);
        }

        executeCommand(query, queries);
    }

    public void delete(Long id)
    {
        var query = "DELETE FROM installments where loan_id = ?";

        List<Object> parameters = new ArrayList<>();
        parameters.add(id);
        executeCommand(query, parameters);
    }
}