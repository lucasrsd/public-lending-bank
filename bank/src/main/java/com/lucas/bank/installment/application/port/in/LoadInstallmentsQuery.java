package com.lucas.bank.installment.application.port.in;

import com.lucas.bank.installment.application.port.out.InstallmentAggregate;


public interface LoadInstallmentsQuery {

    InstallmentAggregate loadInstallments(Long loanId);

}
