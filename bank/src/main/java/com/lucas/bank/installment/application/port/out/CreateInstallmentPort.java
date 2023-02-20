package com.lucas.bank.installment.application.port.out;

import com.lucas.bank.installment.domain.AmortizationType;
import com.lucas.bank.installment.domain.Installment;

import java.util.List;

public interface CreateInstallmentPort {
    void createInstallment(AmortizationType amortizationType, Long loanId, List<Installment> installments);
}
