package com.lucas.bank.installment.application.service;

import com.lucas.bank.installment.domain.Installment;
import com.lucas.bank.loan.domain.AmortizationType;
import com.lucas.bank.shared.adapters.UseCase;
import com.lucas.bank.installment.application.port.in.CreateInstallmentCommand;
import com.lucas.bank.installment.application.port.in.CreateInstallmentUseCase;
import com.lucas.bank.installment.application.port.out.CreateInstallmentPort;
import com.lucas.bank.installment.domain.InstallmentFactory;
import com.lucas.bank.shared.transactionManager.PersistenceTransactionManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@UseCase
public class CreateInstallmentService implements CreateInstallmentUseCase {

    private final CreateInstallmentPort createInstallmentPort;

    @Override
    public void create(CreateInstallmentCommand command, PersistenceTransactionManager persistenceTransactionManager) {
        if (command.getLoanId() == null)
            throw new RuntimeException("Loan Id null");

        var installments = preview(command);
        createInstallmentPort.createInstallment(AmortizationType.valueOf(command.getAmortizationType()), command.getLoanId(), installments, persistenceTransactionManager);
    }

    @Override
    public List<Installment> preview(CreateInstallmentCommand command) {
        var installmentCalculator = InstallmentFactory.of(AmortizationType.valueOf(command.getAmortizationType()));
        return installmentCalculator.calculateAmortization(command.getDisbursementDate(), command.getRate(), command.getTerm(), command.getAmount(), command.getTaxes());
    }
}
