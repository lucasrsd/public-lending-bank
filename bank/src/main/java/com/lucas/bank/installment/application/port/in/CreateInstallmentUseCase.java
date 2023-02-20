package com.lucas.bank.installment.application.port.in;

import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.loan.domain.Loan;

import java.util.List;

public interface CreateInstallmentUseCase {

    void create(CreateInstallmentCommand command);
    List<Installment> preview(CreateInstallmentCommand command);

}
