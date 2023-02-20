package com.lucas.bank.installment.application.port.out;

import com.lucas.bank.installment.domain.Installment;

import java.util.List;

public interface LoadInstallmentPort {
    List<Installment> loadInstallments(Long loanId);
}
