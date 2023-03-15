package com.lucas.bank.transaction.application.port.in;

import com.lucas.bank.installment.application.port.out.InstallmentRepaymentAggregate;
import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.shared.persistenceManager.UnitOfWork;

import java.math.BigDecimal;
import java.util.List;

public interface RepayLoanUseCase {

    InstallmentRepaymentAggregate repayment(Long loanId, BigDecimal amount, UnitOfWork unitOfWork);

}
