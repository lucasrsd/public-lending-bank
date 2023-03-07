package com.lucas.bank.installment.application.port.in;

import com.lucas.bank.installment.application.port.out.InstallmentRepaymentAggregate;
import com.lucas.bank.installment.domain.Installment;

import java.math.BigDecimal;
import java.util.List;

public interface InstallmentRepaymentUseCase {
    InstallmentRepaymentAggregate calculateRepayment(List<Installment> installments, BigDecimal amount);
}
