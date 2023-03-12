package com.lucas.bank.installment.application.port.out;

import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.loan.domain.AmortizationType;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;

import java.util.List;

public interface UpdateInstallmentPort {
    void updateInstallments(Long loanId, AmortizationType amortizationType, List<Installment> installments, UnitOfWork unitOfWork);
}
